package com.demo.app.bootcoin.controllers;

import com.demo.app.bootcoin.entities.Wallet;
import com.demo.app.bootcoin.services.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/wallet")
@Tag(name = "Test APIs", description = "Test APIs for demo purpose")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ResponseEntity<Flux<Wallet>> findAll() {
        return ResponseEntity.ok(walletService.findAll());
    }

    @GetMapping("/{id}")
    public Mono<Wallet> findById(@PathVariable String id) {
        return walletService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Mono<Wallet>> save(@RequestBody Wallet wallet) {
        return ResponseEntity.ok(walletService.save(wallet));
    }

    @GetMapping("/wallet/{walletId}/transaction/{transactionId}")
    public Mono<ResponseEntity<Wallet>> validateTransaction(@PathVariable String walletId,@PathVariable String transactionId) {
        return walletService.validateTransaction(transactionId,walletId).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Wallet>> update(@RequestBody Wallet wallet, @PathVariable String id) {
        return walletService.update(wallet, id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return walletService.delete(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
