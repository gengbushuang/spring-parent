package com.jpa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * DispatcherServlet.doService
 * DispatcherServlet.doDispatch
 * DispatcherServlet.processDispatchResult
 * DispatcherServlet.processHandlerException
 * 如果有异常就做异常的部分处理
 * if (exception != null) {
 * 			if (exception instanceof ModelAndViewDefiningException) {
 * 				logger.debug("ModelAndViewDefiningException encountered", exception);
 * 				mv = ((ModelAndViewDefiningException) exception).getModelAndView();
 *                        }
 * 			else {
 * 				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
 * 				mv = processHandlerException(request, response, handler, exception);
 * 				errorView = (mv != null);
 *            }        * 		}
 */


@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(ValidationException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("message", exception.getMessage());
        return map;
    }
}
