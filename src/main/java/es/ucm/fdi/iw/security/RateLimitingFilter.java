package es.ucm.fdi.iw.security;

import java.io.IOException;
import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final LoadingCache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofHours(1))
            .build(this::newBucket);

    private Bucket newBucket(String key) {
        Refill refill = Refill.greedy(10, Duration.ofMinutes(1)); // 10 peticiones/min
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
                                    throws IOException, ServletException {
        if (isProtected(req)) {
            String key = req.getRemoteAddr();
            Bucket bucket = buckets.get(key);
            if (!bucket.tryConsume(1)) {
                res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                res.getWriter().write("Rate limit exceeded");
                return;
            }
        }
        chain.doFilter(req, res);
    }

    private boolean isProtected(HttpServletRequest r) {
        return "POST".equalsIgnoreCase(r.getMethod()) &&
               (r.getRequestURI().equals("/login")
             || r.getRequestURI().equals("/signup"));
    }
}
