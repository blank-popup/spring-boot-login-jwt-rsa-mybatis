package com.example.loginJwtRSA.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class AspectLog {
    @Pointcut("within(com.example.loginJwtRSA..*)")
    public void controller() {}

    @Before("controller()")
    public void beforeRequest(JoinPoint joinPoint) {
        log.info("##### Start request {}", joinPoint.getSignature().toShortString());
        log.info("Parameter request {}", joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterReturningLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("##### End request {}", joinPoint.getSignature().toShortString());
        if (returnValue == null) {
            log.info("returnValue : NULL");
        }
        else {
            log.info("returnValue : {}", returnValue.toString());
        }
    }

    @AfterThrowing(pointcut = "controller()", throwing = "e")
    public void afterThrowingLogging(JoinPoint joinPoint, Exception e) {
        log.error("##### Occured error in request {}", joinPoint.getSignature().toShortString());
        log.error("Message : {}", e.getMessage());
    }
}
