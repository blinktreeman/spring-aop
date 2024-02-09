package ru.letsdigit.springaop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimerAspect {

    @Pointcut("@annotation(ru.letsdigit.springaop.aspect.Timer)")
    public void timeMeasuredMethod() {

    }

    @Around("timeMeasuredMethod()")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        long time = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            time = System.nanoTime() - time;
            log.info("className: {} - methodName: {}, #{} nanoseconds", className, methodName, time);
        }
    }
}
