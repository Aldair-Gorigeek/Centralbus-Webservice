package com.gorigeek.springboot;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String url = request.getRequestURL().toString();
            String methodType = request.getMethod();

            String originalUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");
            if (originalUrl == null) {
                originalUrl = url;
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Module moduleAnnotation = method.getAnnotation(Module.class);
            if (moduleAnnotation == null) {
                moduleAnnotation = joinPoint.getTarget().getClass().getAnnotation(Module.class);
            }
            String module = moduleAnnotation != null ? moduleAnnotation.value() : "Unknown";

//            logger.info("Modulo: " + module + " | Original Endpoint: " + methodType + " " + originalUrl + " Ejecutado en " + executionTime + "ms");
            System.err.println("Modulo: " + module + " | Original Endpoint: " + methodType + " " + originalUrl
                    + " Ejecutado en " + executionTime + "ms");
        }

        return proceed;
    }
}