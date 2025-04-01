package com.example.Crud;

import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class RetryableService {

    private final Random random = new Random();

    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String unreliableMethod() {
        if (random.nextBoolean()) {
            throw new RuntimeException("Temporary failure, retrying...");
        }
        return "Success!";
    }
}