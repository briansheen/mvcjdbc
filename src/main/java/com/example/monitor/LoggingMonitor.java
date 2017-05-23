package com.example.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Created by bsheen on 5/23/17.
 */

@Aspect
@Component
public class LoggingMonitor {

    @Around("execution(* com.example.controller..*.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
        Object retVal = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("Start method ");
            sb.append(joinPoint.getSignature().getName());
            sb.append("(");

            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; ++i) {
                sb.append(args[i]).append(",");
            }
            if (args.length > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(")");

            logger.info(sb.toString());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            retVal = joinPoint.proceed();

            stopWatch.stop();

            StringBuffer endMessage = new StringBuffer();
            endMessage.append("Finish method ");
            endMessage.append(joinPoint.getSignature().getName());
            endMessage.append("(), execution time: ");
            endMessage.append(stopWatch.getTotalTimeMillis());
            endMessage.append(" ms.");

            logger.info(endMessage.toString());
        } catch (Throwable e) {
            logger.error("ERROR ", e);
            throw e;
        }
        return retVal;
    }
}
