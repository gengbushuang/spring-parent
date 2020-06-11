package com.jpa.support;

import com.jpa.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

//health
@Component
public class CoffeeIndicator implements HealthIndicator {

    @Autowired
    private CoffeeService coffeeService;

    @Override
    public Health health() {
        long count = coffeeService.getCoffeeCount();
        Health health;
        if (count > 0) {
            health = Health.up().withDetail("count", count)
                    .withDetail("message", "We have enouth coffee.")
                    .build();
        } else {
            health = Health.down().withDetail("count", 0)
                    .withDetail("message", "We are out of coffee.")
                    .build();
        }
        return health;
    }
}
