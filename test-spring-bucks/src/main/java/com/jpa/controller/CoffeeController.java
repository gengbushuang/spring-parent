package com.jpa.controller;

import com.jpa.controller.request.NewCoffeeRequest;
import com.jpa.model.Coffee;
import com.jpa.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.xml.ws.RequestWrapper;
import java.awt.*;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/coffee")
public class CoffeeController {

    @Autowired
    private CoffeeService coffeeService;

    /**
     * DispatcherServlet.doDispatch
     * AbstractHandlerMethodAdapter.handle
     * RequestMappingHandlerAdapter.handleInternal
     * RequestMappingHandlerAdapter.invokeHandlerMethod
     * ServletInvocableHandlerMethod.invokeAndHandle
     * 这个会调用真实@Controller对应的方法，返回结果
     * Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
     * setResponseStatus(webRequest);
     * <p>
     * if (returnValue == null) {
     * if (isRequestNotModified(webRequest) || getResponseStatus() != null || mavContainer.isRequestHandled()) {
     * mavContainer.setRequestHandled(true);
     * return;
     * }}
     * else if (StringUtils.hasText(getResponseStatusReason())) {
     * mavContainer.setRequestHandled(true);
     * return;
     * }
     * <p>
     * mavContainer.setRequestHandled(false);
     * Assert.state(this.returnValueHandlers != null, "No return value handlers");
     * try {
     * 这个会选择一种返回的returnValueHandlers对结果数据进行处理
     * this.returnValueHandlers.handleReturnValue(returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
     * }
     * catch (Exception ex) {
     * if (logger.isTraceEnabled()) {
     * logger.trace(formatErrorForReturnValue(returnValue), ex);
     * }
     * throw ex;
     * }
     *
     * @return
     */
    @GetMapping(path = "/", params = "!name")
    @ResponseBody
    public List<Coffee> getAll() {
        return coffeeService.getAllCoffee();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Coffee getById(@PathVariable Long id) {
        Coffee coffee = coffeeService.getCoffee(id);
        return coffee;
    }

    @GetMapping(path = "/", params = "name")
    @ResponseBody
    public Coffee getByName(@RequestParam String name) {
        return coffeeService.getCoffee(name);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffee(@Valid NewCoffeeRequest coffeeRequest, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Binding Errors: {}", result);
            return null;
        }
        return coffeeService.saveCoffee(coffeeRequest.getName(), coffeeRequest.getPrice());
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addJsonCoffee(@Valid @RequestBody NewCoffeeRequest coffeeRequest, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Binding Errors: {}", result);
            throw new ValidationException(result.toString());
        }
        return coffeeService.saveCoffee(coffeeRequest.getName(), coffeeRequest.getPrice());
    }
}
