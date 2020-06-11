package com.jpa.repository;

import com.jpa.model.Coffee;
import com.jpa.model.CoffeeCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
//redis的Repository
public interface CoffeeCacheRepository extends CrudRepository<CoffeeCache,Long> {
    Optional<CoffeeCache> findOneByName(String name);
}
