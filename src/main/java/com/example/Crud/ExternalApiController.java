package com.example.Crud;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

@RestController
@RequestMapping("/api/external")
public class ExternalApiController {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiController.class);
    private final Random random = new Random();

    public ExternalApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/fetch")
    @CircuitBreaker(name = "externalService", fallbackMethod = "fallbackResponse")
    public ResponseEntity<String> fetchExternalData() {
        logger.info("Fetching data from external API");
        if(random.nextInt(3)==0){
            String response = restTemplate.getForObject("d", String.class);

        }
        String response = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts", String.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> fallbackResponse(Exception ex) {
        logger.warn("External API failed, using fallback response");
        return ResponseEntity.status(503).body("Fallback: External Service Unavailable");
    }
}