package ie.eoin.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@Profile("dev")
public class ControllerAspect {

    @Pointcut("execution(* ie.eoin.controllers.WebService.*(..))")
    public void webServiceMethods(){
    }

    @Before("webServiceMethods()")
    public void theBeforeAction(JoinPoint joinPoint){
        log.info(joinPoint.getSignature().toShortString() + " with arguments " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(pointcut = "webServiceMethods()",returning = "returnValue")
    public void theAfterAction(JoinPoint joinPoint, Object returnValue){
        log.info(joinPoint.getSignature().toShortString() + " return value " + returnValue);
    }
}
