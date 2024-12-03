package co.leapwise.assignments.expression_evaluator.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @Around("execution(* co.leapwise.assignments.expression_evaluator.web.advice.GlobalExceptionHandler.*(..))")
    public Object logGlobalExceptionHandlerResponses(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();

        if(nonNull(methodArgs)) {
            for (Object arg : methodArgs) {
                if (arg instanceof Throwable) {
                    LOG.error("-- Handling exception", (Throwable) arg);
                }
            }
        }

        try {
            Object response = joinPoint.proceed();
            LOG.error("-- Error response from {}", response);
            return response;
        } catch (Throwable throwable) {
            LOG.error("-- Exception in error mapping", throwable);
            throw throwable;
        }
    }
}