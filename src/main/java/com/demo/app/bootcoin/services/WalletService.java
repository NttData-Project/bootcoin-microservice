package com.demo.app.bootcoin.services;

import com.demo.app.bootcoin.entities.Wallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {
    Flux<Wallet> findAll();
    Mono<Wallet> findById(String id);
    Mono<Wallet> save(Wallet wallet);
    Mono<Wallet> update(Wallet wallet,String id);
    Mono<String> delete(String id);
}
