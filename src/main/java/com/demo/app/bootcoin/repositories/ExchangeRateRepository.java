package com.demo.app.bootcoin.repositories;

import com.demo.app.bootcoin.entities.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate,String>{
}
