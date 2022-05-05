package com.demo.app.bootcoin.services.impl;

import com.demo.app.bootcoin.entities.Status;
import com.demo.app.bootcoin.entities.Transaction;
import com.demo.app.bootcoin.repositories.TransactionRepository;
import com.demo.app.bootcoin.services.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Transaction> findById(String id) {
        return transactionRepository.findById(id);
    }

    @Transactional
    @Override
    public Mono<Transaction> save(Transaction transaction) {
        transaction.setStatus(Status.PENDING);
        return transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public Mono<Transaction> update(Transaction transaction, String id) {
        return transactionRepository.findById(id).flatMap(x->{
            x.setFromAccount(transaction.getFromAccount());
            x.setToAccount(transaction.getToAccount());
            x.setStatus(transaction.getStatus());
            x.setAmount(transaction.getAmount());
            return transactionRepository.save(x);
        });
    }

    @Transactional
    @Override
    public Mono<String> delete(String id) {
        return transactionRepository.deleteById(id).thenReturn(id);
    }
}
