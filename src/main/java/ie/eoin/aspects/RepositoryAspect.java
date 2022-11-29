package ie.eoin.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
@Profile("dev")
public class RepositoryAspect {

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryMethods(){}

    @Before("repositoryMethods()")
    public void beforeMethods(JoinPoint joinPoint){
        log.info(joinPoint.getSignature().toShortString() + " method was called with arguments" + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "repositoryMethods()",returning = "returnValue")
    public void afterReturn(JoinPoint joinPoint, Object returnValue){
        log.info(joinPoint.getSignature().toShortString() + "method returned value: " + returnValue);
    }
}
