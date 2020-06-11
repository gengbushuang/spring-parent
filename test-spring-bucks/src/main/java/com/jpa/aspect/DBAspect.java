package com.jpa.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//说明这是个切面
@Aspect
@Component
@Slf4j
public class DBAspect {

    //通知，在连接点执行动作
    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        String name = "_";
        String result = "Y";
        try {
            name = pjp.getSignature().toShortString();
            return pjp.proceed();
        } catch (Throwable e) {
            result = "N";
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("{};{};{}ms", name, result, (endTime - startTime));
        }
    }

    //切入点，说明如何匹配连接点
    @Pointcut("execution(* com.jpa.repository..*(..))")
    private void repositoryOps() {
    }
}
