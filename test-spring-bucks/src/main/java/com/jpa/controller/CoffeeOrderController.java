package com.jpa.controller;

import com.jpa.controller.request.NewOrderRequest;
import com.jpa.model.Coffee;
import com.jpa.model.CoffeeOrder;
import com.jpa.service.CoffeeOrderService;
import com.jpa.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService orderService;

    @Autowired
    private CoffeeService coffeeService;

    /**
     * consumes 指定处理请求的提交内容类型
     * produces 指定返回的内容类型
     *
     * @param newOrderRequest
     * @return
     */
    @PostMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrderRequest) {
        log.info("Receive new Order {}", newOrderRequest);
        Coffee[] coffees = coffeeService.getCoffeeByName(newOrderRequest.getItems()).toArray(new Coffee[]{});
        return orderService.createOrder(newOrderRequest.getCustomer(), coffees);
    }
}
