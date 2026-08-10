package com.ayoub.url_shortener.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class CacheTimingAspect {

    private static final Logger log = LoggerFactory.getLogger(CacheTimingAspect.class);

    @Around("@annotation(com.ayoub.url_shortener.aop.TrackExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.nanoTime();

        Object proceed = joinPoint.proceed();

        long executionTime = System.nanoTime() - start;
        double milliseconds = executionTime / 1_000_000.0;

        log.info("Method [{}] executed in {} ms",
                joinPoint.getSignature().getName(),
                String.format("%.2f", milliseconds));

        return proceed;
    }


}
