package com.demo.app.bootcoin.services.impl;

import com.demo.app.bootcoin.entities.ExchangeRate;
import com.demo.app.bootcoin.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExchangeRateImpl implements ExchangeRateService {

    private final static String KEY = "ExchangeRate";

    private final ReactiveRedisOperations<String,ExchangeRate> redisOperations;
    private final ReactiveHashOperations<String, String,ExchangeRate> hashOperations;

    @Autowired
    public ExchangeRateImpl(ReactiveRedisOperations<String,ExchangeRate> redisOperations) {
        this.redisOperations = redisOperations;
        this.hashOperations = redisOperations.opsForHash();
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<ExchangeRate> findAll() {
        return hashOperations.values(KEY);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<ExchangeRate> findById(String id) {
        return hashOperations.get(KEY, id);
    }

    @Transactional
    @Override
    public Mono<ExchangeRate> save(ExchangeRate exchangeRate) {
        return hashOperations.put(KEY,exchangeRate.getId(),exchangeRate).thenReturn(exchangeRate);
    }

    @Transactional
    @Override
    public Mono<ExchangeRate> update(ExchangeRate exchangeRate, String id) {
        exchangeRate.setId(id);
        return hashOperations.put(KEY,id,exchangeRate).thenReturn(exchangeRate);
    }

    @Transactional
    @Override
    public Mono<Void> delete(String id) {
        return hashOperations.remove(KEY,id).then();
    }
}
