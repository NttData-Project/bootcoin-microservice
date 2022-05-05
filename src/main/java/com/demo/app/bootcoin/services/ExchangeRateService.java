package com.demo.app.bootcoin.services;

import com.demo.app.bootcoin.entities.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Flux<ExchangeRate> findAll();
    Mono<ExchangeRate> findById(String id);
    Mono<ExchangeRate> save(ExchangeRate exchangeRate);
    Mono<ExchangeRate> update(ExchangeRate exchangeRate,String id);
    Mono<Void> delete(String id);
}
