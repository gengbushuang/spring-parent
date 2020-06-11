package com.jpa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ispatcherServlet.doDispatch方法里面
 */
@Slf4j
public class PerformanceInteceptor implements HandlerInterceptor {
    private ThreadLocal<StopWatch> threadLocal = new ThreadLocal<>();

    //方法处理前

    /**
     *
     * if (!mappedHandler.applyPreHandle(processedRequest, response)) {
     * 		return;
     * }
     *
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch sw = new StopWatch();
        threadLocal.set(sw);
        sw.start();
        return true;
    }
    //方法处理后

    /**
     * mappedHandler.applyPostHandle(processedRequest, response, mv);
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        threadLocal.get().stop();
        threadLocal.get().start();
    }

    //方法处理后

    /**
     * mappedHandler.triggerAfterCompletion(request, response, ex);
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch stopWatch = threadLocal.get();
        stopWatch.stop();

        String method = handler.getClass().getSimpleName();
        if (handler instanceof HandlerMethod) {
            String beanType = ((HandlerMethod) handler).getBeanType().getName();
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            method = beanType + "." + methodName;
        }

        log.info("{};{};{};{};{}ms;{}ms;{}ms", request.getRequestURI(), method, response.getStatus(),
                ex == null ? "-" : ex.getClass().getSimpleName(), stopWatch.getTotalTimeMillis(),
                stopWatch.getTotalTimeMillis() - stopWatch.getLastTaskTimeMillis(), stopWatch.getLastTaskTimeMillis());

        threadLocal.remove();
    }
}
