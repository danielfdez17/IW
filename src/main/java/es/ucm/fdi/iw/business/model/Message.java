package es.ucm.fdi.iw.business.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * A message that users can send each other.
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Message.countUnread",
	query="SELECT COUNT(m) FROM Message m "
			+ "WHERE m.recipient.id = :userId AND m.dateRead = null")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Transferable<Message.Transfer> {
	
	private static Logger log = LogManager.getLogger(Message.class);	
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;
	@ManyToOne
	private User sender;
	@ManyToOne
	private User recipient;
	private String text;
	
	private LocalDateTime dateSent;
	@Column(nullable = true)
	private LocalDateTime dateRead;
	
	@PrePersist
	public void prePersist(){
		this.dateSent = LocalDateTime.now();
	}

	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 */
    @Getter
    @AllArgsConstructor
	public static class Transfer {
		private String from;
		private String to;
		private String sent;
		private String received;
		private String text;
		long id;
		public Transfer(Message m) {
			this.from = m.getSender().getUsername();
			this.to = m.getRecipient().getUsername();
			this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateSent());
			this.received = m.getDateRead() == null ?
					null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateRead());
			this.text = m.getText();
			this.id = m.getId();
		}
	}

	@Override
	public Transfer toTransfer() {
		return new Transfer(sender.getUsername(), recipient.getUsername(), 
			DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
			dateRead == null ? null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateRead),
			text, id
        );
    }
}