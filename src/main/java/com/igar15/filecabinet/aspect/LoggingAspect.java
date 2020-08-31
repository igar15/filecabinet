package com.igar15.filecabinet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.igar15.filecabinet.service.impl.*.*(..))")
    private void pointcutForServiceImplPackage() {}

    @Before("pointcutForServiceImplPackage()")
    public void loggingMethodBeforeAdvice(JoinPoint joinPoint) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String simpleClassName = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userName).append("::");
        stringBuilder.append(simpleClassName).append(" >>> ").append(methodName).append("(");

        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        if (parameters.length > 0) {
            for (Parameter tempParameter : parameters) {
                String tempParameterTypeName = tempParameter.getType().getSimpleName();
                String tempParameterName = tempParameter.getName();
                stringBuilder.append(tempParameterTypeName).append(" ").append(tempParameterName).append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append(")").append(" : ");

            for (Object tempArg : args) {
                stringBuilder.append(tempArg).append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        else {
            stringBuilder.append(")");
        }

        String logMessage = stringBuilder.toString();

        log.info(logMessage);
    }

}
