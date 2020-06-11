//package com.jpa;
//
//import com.jpa.model.Coffee;
//import com.jpa.model.CoffeeOrder;
//import com.jpa.model.OrderState;
//import com.jpa.repository.CoffeeOrderRepository;
//import com.jpa.repository.CoffeeRepository;
//import com.jpa.service.CoffeeOrderService;
//import com.jpa.service.CoffeeService;
//import lombok.extern.slf4j.Slf4j;
//import org.joda.money.CurrencyUnit;
//import org.joda.money.Money;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@SpringBootApplication
//@EnableJpaRepositories
//@EnableTransactionManagement
////开启缓存拦截
////@EnableCaching(proxyTargetClass = true)
////开启redis的Repositorie
//@EnableRedisRepositories
////开启Aspect
//@EnableAspectJAutoProxy
//@Slf4j
//public class JpaDemoApplication implements ApplicationRunner {
//
//    @Autowired
//    private CoffeeRepository coffeeRepository;
//
//    @Autowired
//    private CoffeeOrderRepository coffeeOrderRepository;
//
//    @Autowired
//    private CoffeeService coffeeService;
//
//    @Autowired
//    private CoffeeOrderService coffeeOrderService;
//
//
//    public static void main(String[] args) {
//        SpringApplication.run(JpaDemoApplication.class);
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
////        initOrders();
////        exeOrders();
//        //cachExe();
////        cachRedisExe();
////        redisCoffee();
////        redisCoffeeRepositorie();
//    }
//
//
//    private void redisCoffeeRepositorie() {
//        Optional<Coffee> coffee = coffeeService.findSimpleCoffeeFromCache("latte");
//        log.info("Coffee: {}", coffee);
//        for (int i = 0; i < 10; i++) {
//            coffee = coffeeService.findSimpleCoffeeFromCache("latte");
//        }
//
//        log.info("Value from Redis: {}", coffee);
//    }
//
//    private void redisCoffee() {
//        Optional<Coffee> coffee = coffeeService.findOneCoffee("latte");
//        log.info("Coffee: {}", coffee);
//        for (int i = 0; i < 10; i++) {
//            coffee = coffeeService.findOneCoffee("latte");
//        }
//
//        log.info("Value from Redis: {}", coffee);
//    }
//
//    private void cachRedisExe() throws InterruptedException {
//        log.info("Count: {}", coffeeService.findAllCoffee().size());
//        for (int i = 0; i < 10; i++) {
//            log.info("Reading from cache.");
//            coffeeService.findAllCoffee();
//        }
//        Thread.sleep(5000);
//        log.info("Reading after refresh.");
//        coffeeService.findAllCoffee().forEach(c -> log.info("coffee: {}", c));
//    }
//
//    private void cachExe() {
//        log.info("Count: {}", coffeeService.findAllCoffee().size());
//        for (int i = 0; i < 10; i++) {
//            log.info("Reading from cache.");
//            coffeeService.findAllCoffee();
//        }
//        coffeeService.reloadCoffee();
//        log.info("Reading after refresh.");
//        coffeeService.findAllCoffee().forEach(c -> log.info("coffee: {}", c));
//    }
//
//    private void exeOrders() {
//        List<Coffee> all = coffeeRepository.findAll();
//        log.info("All coffee {}", all);
//        Optional<Coffee> latte = coffeeService.findOneCoffee("Latte");
//        if (latte.isPresent()) {
//            CoffeeOrder order = coffeeOrderService.createOrder("Li Lei", latte.get());
//            log.info("Update INIT to PAID: {}", coffeeOrderService.updateState(order, OrderState.PAID));
//            log.info("Update PAID to INIT: {}", coffeeOrderService.updateState(order, OrderState.INIT));
//        }
//    }
//
//    private void initOrders() {
//        Coffee espresso = Coffee.builder().name("espresso")
//                .price(Money.of(CurrencyUnit.of("CNY"), 20.0)).build();
//        coffeeRepository.save(espresso);
//        log.info("Coffee: {}", espresso);
//
//        Coffee latte = Coffee.builder().name("latte")
//                .price(Money.of(CurrencyUnit.of("CNY"), 30.0)).build();
//        coffeeRepository.save(latte);
//        log.info("Coffee: {}", latte);
//
//        CoffeeOrder order = CoffeeOrder.builder()
//                .customer("Li Lei")
//                .items(Collections.singletonList(espresso))
//                .state(OrderState.INIT).build();
//        coffeeOrderRepository.save(order);
//        log.info("Order: {}", order);
//
//        order = CoffeeOrder.builder()
//                .customer("Li Lei")
//                .items(Arrays.asList(espresso, latte))
//                .state(OrderState.INIT).build();
//        coffeeOrderRepository.save(order);
//        log.info("Order: {}", order);
//    }
//
////    private void findOrders() {
////        coffeeRepository.findAll(new Sort(Sort.Direction.DESC, "id"))
////                .forEach(c -> log.info("Loading {}", c));
////
////        List<CoffeeOrder> list = coffeeOrderRepository.findTop3ByOrderByUpdateTimeDescIdAsc();
////        log.info("findTop3ByOrderByUpdateTimeDescIdAsc: {}",getJoinedOrdeId(list));
////
////        list = coffeeOrderRepository.findByCustomerOrderById("Li Lei");
////        log.info("findByCustomerOrderById: {}",getJoinedOrdeId(list));
////
////        list.forEach(o->{
////            log.info("Order {}",o.getId());
////            o.getItems().forEach(i->log.info(" Item {}",i));
////        });
////
////        list = coffeeOrderRepository.findByItems_Name("latte");
////        log.info("findByItems_Name: {}",getJoinedOrdeId(list));
////
////    }
////
////    private String getJoinedOrdeId(List<CoffeeOrder> list) {
////        return list.stream().map(o->o.getId().toString()).collect(Collectors.joining(","));
////    }
//}
