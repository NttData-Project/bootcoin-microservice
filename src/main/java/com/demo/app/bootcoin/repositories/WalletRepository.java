package com.demo.app.bootcoin.repositories;

import com.demo.app.bootcoin.entities.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends ReactiveMongoRepository<Wallet,String> {
}
