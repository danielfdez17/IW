package es.ucm.fdi.iw.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucm.fdi.iw.business.fileconfiglocal.LocalData;
import es.ucm.fdi.iw.business.model.Message;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.model.Transferable;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.model.User.Role;
import es.ucm.fdi.iw.business.repository.SubastaRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * User management.
 *
 * Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    // @Autowired
    private final EntityManager entityManager;

    // @Autowired
    private final LocalData localData;

    // @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    // @Autowired
    private final PasswordEncoder passwordEncoder;

    private final SubastaRepository subastaRepository;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    /**
     * Exception to use when denying access to unauthorized users.
     * 
     * In general, admins are always authorized, but users cannot modify
     * each other's profiles.
     */
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No eres administrador, y éste no es tu perfil") // 403
    public static class NoEsTuPerfilException extends RuntimeException {
    }

    /**
     * Encodes a password, so that it can be saved for future checking. Notice
     * that encoding the same password multiple times will yield different
     * encodings, since encodings contain a randomly-generated salt.
     * 
     * @param rawPassword to encode
     * @return the encoded password (typically a 60-character string)
     *         for example, a possible encoding of "test" is
     *         {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean comparePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Generates random tokens. From https://stackoverflow.com/a/44227131/15472
     * 
     * @param byteLength
     * @return
     */
    public static String generateRandomBase64Token(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token); // base64 encoding
    }

    /**
     * Landing page for a user profile
     */
    @GetMapping("{id}")
    public String index(@PathVariable long id, Model model, HttpSession session) {
        User target = entityManager.find(User.class, id);
        model.addAttribute("user", target);
        List<Subasta> listaSubastas = subastaRepository.findByCreador(id);
        listaSubastas.forEach(subasta -> {
            boolean isEnabled = (subasta.getFechaFin().isEqual(LocalDateTime.now())
                            || subasta.getFechaFin().isAfter(LocalDateTime.now()));
            subasta.setEnabled(isEnabled);
        });

        //subastas en las que ha pujado
        List<Subasta> subastasPujadas = subastaRepository.findSubastasByUserId(id);
        int valoracion = (int) Math.round(subastasPujadas.stream()
                                        .map(Subasta::getValoracionGanador)
                                        .filter(Objects::nonNull)
                                        .mapToInt(Byte::intValue)
                                        .average()
                                        .orElse(0));
        model.addAttribute("valoracion", valoracion);
        model.addAttribute("subastasPujadas", subastasPujadas.isEmpty() ? null : subastasPujadas);
        model.addAttribute("subastas", listaSubastas.isEmpty() ? null : listaSubastas);
        return "user";
    }

    /**
     * Alter or create a user
     */
    @PostMapping("/{id}")
    @Transactional
    public String postUser(
            HttpServletResponse response,
            @PathVariable long id,
            @ModelAttribute User edited,
            @RequestParam(required = false) String pass2,
            @RequestParam(required = false) MultipartFile photo,
            Model model, HttpSession session) throws IOException {

        User requester = (User) session.getAttribute("u");
        User target = null;
        if (id == -1 && requester.hasRole(Role.ADMIN)) {
            // create new user with random password
            target = new User();
            target.setPassword(encodePassword(generateRandomBase64Token(12)));
            target.setEnabled(true);
            entityManager.persist(target);
            entityManager.flush(); // forces DB to add user & assign valid id
            id = target.getId(); // retrieve assigned id from DB
        }

        if (!photo.getOriginalFilename().isBlank()) {

            try {
                String filePath = System.getProperty("user.dir") + "\\iwdata\\users\\" + id + ".jpg";
                FileOutputStream fout = new FileOutputStream(filePath);
                fout.write(photo.getBytes());

                fout.close();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        // retrieve requested user
        target = entityManager.find(User.class, id);
        model.addAttribute("user", target);

        if (requester.getId() != target.getId() &&
                !requester.hasRole(Role.ADMIN)) {
            throw new NoEsTuPerfilException();
        }

        if (edited.getPassword() != null) {
            if (!edited.getPassword().equals(pass2)) {
                log.warn("Passwords do not match - returning to user form");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                model.addAttribute("user", target);
                return "user";
            } else {
                // save encoded version of password
                target.setPassword(encodePassword(edited.getPassword()));
            }
        }
        target.setUsername(edited.getUsername());
        target.setFirstName(edited.getFirstName());
        target.setLastName(edited.getLastName());
        target.setDeliveryAddress(edited.getDeliveryAddress());
        if (!comparePassword(pass2, target.getPassword())) {
            target.setPassword(encodePassword(pass2));
            session.invalidate();
            return "redirect:/login";
        }
        // update user session so that changes are persisted in the session, too
        if (requester.getId() == target.getId()) {
            session.setAttribute("u", target);
        }

        return "user";
    }

    /**
     * Returns the default profile pic
     * 
     * @return
     */
    private static InputStream defaultPic() {
        return new BufferedInputStream(Objects.requireNonNull(
                UserController.class.getClassLoader().getResourceAsStream(
                        "static/img/default-pic.jpg")));
    }

    /**
     * Downloads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("{id}/pic")
    public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
        File f = localData.getFile("users", "" + id + ".jpg");
        InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : UserController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
    }

    /**
     * Uploads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("{id}/pic")
    @ResponseBody
    public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
            HttpServletResponse response, HttpSession session, Model model) throws IOException {

        User target = entityManager.find(User.class, id);
        model.addAttribute("user", target);

        // check permissions
        User requester = (User) session.getAttribute("u");
        if (requester.getId() != target.getId() &&
                !requester.hasRole(Role.ADMIN)) {
            throw new NoEsTuPerfilException();
        }

        log.info("Updating photo for user {}", id);
        File f = localData.getFile("user", "" + id + ".jpg");
        if (photo.isEmpty()) {
            log.info("failed to upload photo: emtpy file?");
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                log.warn("Error uploading " + id + " ", e);
            }
        }
        return "{\"status\":\"photo uploaded correctly\"}";
    }

    @GetMapping("error")
    public String error(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("sess", session);
        model.addAttribute("req", request);
        return "error";
    }

    /**
     * Returns JSON with all received messages
     */
    @GetMapping(path = "received", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
    public List<Message.Transfer> retrieveMessages(HttpSession session) {
        long userId = ((User) session.getAttribute("u")).getId();
        User u = entityManager.find(User.class, userId);
        log.info("Generating message list for user {} ({} messages)",
                u.getUsername(), u.getReceived().size());
        return u.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
    }

    /**
     * Returns JSON with count of unread messages
     */
    @GetMapping(path = "unread", produces = "application/json")
    @ResponseBody
    public String checkUnread(HttpSession session) {
        long userId = ((User) session.getAttribute("u")).getId();
        long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        session.setAttribute("unread", unread);
        return "{\"unread\": " + unread + "}";
    }

    /**
     * Posts a message to a user.
     * 
     * @param id of target user (source user is from ID)
     * @param o  JSON-ized message, similar to {"message": "text goes here"}
     * @throws JsonProcessingException
     */
    @PostMapping("/{id}/msg")
    @ResponseBody
    @Transactional
    public String postMsg(@PathVariable long id,
            @RequestBody JsonNode o, Model model, HttpSession session)
            throws JsonProcessingException {

        String text = o.get("message").asText();
        User u = entityManager.find(User.class, id);
        User sender = entityManager.find(
                User.class, ((User) session.getAttribute("u")).getId());
        model.addAttribute("user", u);

        // construye mensaje, lo guarda en BD
        Message m = new Message();
        m.setRecipient(u);
        m.setSender(sender);
        m.setDateSent(LocalDateTime.now());
        m.setText(text);
        entityManager.persist(m);
        entityManager.flush(); // to get Id before commit

        ObjectMapper mapper = new ObjectMapper();
        /*
         * // construye json: método manual
         * ObjectNode rootNode = mapper.createObjectNode();
         * rootNode.put("from", sender.getUsername());
         * rootNode.put("to", u.getUsername());
         * rootNode.put("text", text);
         * rootNode.put("id", m.getId());
         * String json = mapper.writeValueAsString(rootNode);
         */
        // persiste objeto a json usando Jackson
        String json = mapper.writeValueAsString(m.toTransfer());

        log.info("Sending a message to {} with contents '{}'", id, json);

        messagingTemplate.convertAndSend("/user/" + u.getUsername() + "/queue/updates", json);
        return "{\"result\": \"message sent.\"}";
    }

    @PostMapping("/{id}/addMoney")
    @Transactional
    public String addMoney(@PathVariable long id, @RequestParam double amount) {
        User u = entityManager.find(User.class, id);
        if (u != null) {
            u.setAvailableMoney(u.getAvailableMoney() + amount);
        }
        return "redirect:/user/" + id;
    }
}
