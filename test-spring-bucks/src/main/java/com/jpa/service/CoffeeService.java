package com.jpa.service;

import com.jpa.model.Coffee;
import com.jpa.model.CoffeeCache;
import com.jpa.repository.CoffeeCacheRepository;
import com.jpa.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
//@CacheConfig(cacheNames = "coffee")
public class CoffeeService {

    private static final String CACHE = "springbucks-coffee";

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeCacheRepository cacheRepository;

//    @Autowired
//    private RedisTemplate<String,Coffee> redisTemplate;

    //执行后放到缓存里面
//    @Cacheable(cacheNames = "coffee")
    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    public List<Coffee> getAllCoffee() {
        return coffeeRepository.findAll();
    }

    //清理缓存
//    @CacheEvict
    public void reloadCoffee() {

    }

    public Optional<Coffee> findSimpleCoffeeFromCache(String name) {
        Optional<CoffeeCache> cache = cacheRepository.findOneByName(name);
        if (cache.isPresent()) {
            CoffeeCache coffeeCache = cache.get();
            Coffee coffee = Coffee.builder().name(coffeeCache.getName()).price(coffeeCache.getPrice()).build();
            log.info("Coffee {} found in cache.", coffeeCache);
            return Optional.of(coffee);
        } else {
            Optional<Coffee> oneCoffee = this.findOneCoffee(name);
            oneCoffee.ifPresent(c -> {
                CoffeeCache coffeeCache = CoffeeCache.builder().id(c.getId())
                        .name(c.getName()).price(c.getPrice())
                        .build();
                log.info("Save Coffee {} to cache.", coffeeCache);
                cacheRepository.save(coffeeCache);
            });
            return oneCoffee;
        }
    }


    public Optional<Coffee> findOneCoffee(String name) {
        //从redis里面获取
//        HashOperations<String, String, Coffee> hashOperations = redisTemplate.opsForHash();
//        if(redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE,name)){
//            log.info("Get coffee {} from Redis.",name);
//            return Optional.of(hashOperations.get(CACHE,name));
//        }

        //完全匹配name，忽略大小写
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());

        Example<Coffee> example = Example.of(Coffee.builder().name(name).build(), matcher);

        Optional<Coffee> coffee = coffeeRepository.findOne(example);

        log.info("Coffee Found: {}", coffee);
//        if(coffee.isPresent()){
//            //放进redis里面
//            log.info("Put coffee {} to Redis.",name);
//            hashOperations.put(CACHE,name,coffee.get());
//            redisTemplate.expire(CACHE,1,TimeUnit.MINUTES);
//        }

        return coffee;
    }

    public List<Coffee> getCoffeeByName(List<String> names) {
        return coffeeRepository.findByNameInOrderById(names);
    }

    public Coffee saveCoffee(String name, Money price) {
        Coffee coffee = Coffee.builder().name(name)
                .price(price).build();
        coffeeRepository.save(coffee);
        log.info("Coffee save: {}", coffee);
        return coffee;
    }

    public Coffee getCoffee(String name) {
        return this.findOneCoffee(name).get();
    }

    public Coffee getCoffee(Long id) {
        Optional<Coffee> coffee = coffeeRepository.findById(id);
        log.info("Coffee Found: {}", coffee);
        return coffee.get();
    }

    public long getCoffeeCount() {
        return coffeeRepository.count();
    }
}
