package ru.letsdigit.springaop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RecoverExceptionAspect {

    @Pointcut("@annotation(ru.letsdigit.springaop.aspect.RecoverException)")
    public void recoverExceptionMethod() {
    }

    @Around("recoverExceptionMethod()")
    public Object recoverException(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // В случае Exception для всех заданных параметром аннотации RuntimeException-ов
            for (Class<? extends RuntimeException> exception : signature
                    .getMethod()
                    .getAnnotation(RecoverException.class)
                    .noRecoverFor()
            ) {
                /* При этом, если тип исключения входит в список перечисленных в noRecoverFor исключений,
                то исключение НЕ прерывается и прокидывается выше. */
                if (exception.isAssignableFrom(e.getClass())) {
                    throw e;
                }
            }
            /*Аннотация работает так: если во время исполнения метода был экспешн, то не прокидывать его
            выше и возвращать из метода значение по умолчанию (null, 0, false, ...).*/
            return null;
        }
    }
}
