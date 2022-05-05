package com.demo.app.bootcoin.controllers;

import com.demo.app.bootcoin.entities.Transaction;
import com.demo.app.bootcoin.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Test APIs", description = "Test APIs for demo purpose")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<Flux<Transaction>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    public Mono<Transaction> findById(@PathVariable String id) {
        return transactionService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Mono<Transaction>> saveExchangeRate(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.save(transaction));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Transaction>> update(@RequestBody Transaction transaction, @PathVariable String id) {
        return transactionService.update(transaction, id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return transactionService.delete(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}