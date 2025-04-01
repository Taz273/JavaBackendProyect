package com.Security.Security;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitFilter implements Filter {

    private final UserRepository userRepository;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createFreeTierBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        if (uri.startsWith("/swagger-ui") ||
                uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-resources") ||
                uri.startsWith("/configuration") ||
                uri.startsWith("/webjars") ||
                uri.startsWith("/h2-console") ||
                uri.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            System.out.println("Authenticated username: " + username);
        } else {
            System.out.println("Unauthenticated or no principal");
        }

        if (username != null) {
            User user = userRepository.findByUsername(username).orElse(null);
            System.out.println("Fetched user: " + user);
            if (user != null && "PAID".equalsIgnoreCase(user.getRole())) {
                System.out.println("Skipping rate limit for PAID user: " + username);
                chain.doFilter(request, response);
                return;
            }
        }

        String key = (username != null) ? username : httpRequest.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(key, k -> createFreeTierBucket());

        if (bucket.tryConsume(1)) {
            System.out.println("Rate limit OK for: " + key);
            chain.doFilter(request, response);
        } else {
            System.out.println("Rate limit EXCEEDED for: " + key);
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("⛔ Rate limit exceeded. Upgrade to PAID for unlimited access.");
        }
    }
}