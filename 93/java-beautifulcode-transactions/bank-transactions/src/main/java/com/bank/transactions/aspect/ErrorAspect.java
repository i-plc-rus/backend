package com.bank.transactions.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Класс предназначен для централизованной обработки исключений в сервисном слое.
 */

@Slf4j
@Aspect
@Component
@Profile("!test")
public class ErrorAspect {

    @Pointcut("execution(* com.bank.transactions.service.*.*(..))")
    static void allServiceMethods() {
    }

    @Around("allServiceMethods()")
    public Object throwingAdvice(ProceedingJoinPoint point) {
        try {
            return point.proceed();
        } catch (Throwable e) {
            log.error("Throwing advice. Exception: {}", e.getMessage());
            return null;
        }
    }
}
