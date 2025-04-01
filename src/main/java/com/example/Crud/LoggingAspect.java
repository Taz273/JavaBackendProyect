package com.example.Crud;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.Crud.UserService.*(..))")
    public void userServiceMethods() {}

    @Before("userServiceMethods()")
    public void logBefore() {
        logger.info("A method in UserService is being called.");
    }

    @AfterReturning("userServiceMethods()")
    public void logAfterReturning() {
        logger.info("A method in UserService executed successfully.");
    }

    @AfterThrowing(pointcut = "userServiceMethods()", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        logger.error("An error occurred: {}", ex.getMessage());
    }
}