package io.github.lizhifuabc.db.dynamic.datasource.aop;

import io.github.lizhifuabc.db.dynamic.datasource.DynamicDataSourceContextHolder;
import io.github.lizhifuabc.db.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 动态数据源切面
 * 拦截带有@DS注解的方法或类，实现数据源切换
 *
 * @author lizhifu
 */
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
@Slf4j
public class DynamicDataSourceAspect {

    /**
     * 切入点：所有带有@DS注解的方法
     */
    @Pointcut("@annotation(io.github.lizhifuabc.db.dynamic.datasource.annotation.DS)")
    public void dataSourcePointCut() {
    }

    /**
     * 切入点：所有带有@DS注解的类中的所有方法
     */
    @Pointcut("@within(io.github.lizhifuabc.db.dynamic.datasource.annotation.DS)")
    public void classDataSourcePointCut() {
    }

    /**
     * 环绕通知
     * 在方法执行前后切换数据源
     *
     * @param point 切入点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("dataSourcePointCut() || classDataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String dataSourceName = getDataSourceName(point);
        
        // 如果获取到了数据源名称，则切换数据源
        if (StringUtils.hasText(dataSourceName)) {
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceName(dataSourceName);
            log.debug("切换数据源到: {}", dataSourceName);
        }
        
        try {
            // 执行原方法
            return point.proceed();
        } finally {
            // 恢复默认数据源
            if (StringUtils.hasText(dataSourceName)) {
                DynamicDataSourceContextHolder.clearDataSourceName();
                log.debug("恢复默认数据源");
            }
        }
    }

    /**
     * 获取数据源名称
     * 优先从方法上获取，如果方法上没有，则从类上获取
     *
     * @param point 切入点
     * @return 数据源名称
     */
    private String getDataSourceName(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        
        // 优先从方法上获取
        DS dsAnnotation = AnnotationUtils.findAnnotation(method, DS.class);
        
        // 如果方法上没有，则从类上获取
        if (dsAnnotation == null) {
            dsAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), DS.class);
        }
        
        if (Objects.nonNull(dsAnnotation)) {
            return dsAnnotation.value();
        }
        
        return null;
    }
}