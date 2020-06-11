package com.jpa.service;

import com.jpa.model.Coffee;
import com.jpa.model.CoffeeOrder;
import com.jpa.model.OrderState;
import com.jpa.repository.CoffeeOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Service
public class CoffeeOrderService {

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder createOrder(String customer, Coffee... coffees) {
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(Arrays.asList(coffees))
                .state(OrderState.INIT).build();
        CoffeeOrder save = coffeeOrderRepository.save(order);
        log.info("New Order: {}", save);
        return save;
    }

    public boolean updateState(CoffeeOrder order, OrderState state) {
        //更新状态不能小于当前订单状态，状态不可逆
        if (state.compareTo(order.getState()) <= 0) {
            log.warn("Wrong State Order: {} , {}", state, order.getState());
            return false;
        }
        order.setState(state);
        coffeeOrderRepository.save(order);
        log.info("Update Order: {}", order);
        return true;
    }
}
