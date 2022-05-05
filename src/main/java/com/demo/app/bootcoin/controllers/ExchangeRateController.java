package com.demo.app.bootcoin.controllers;

import com.demo.app.bootcoin.entities.ExchangeRate;
import com.demo.app.bootcoin.services.ExchangeRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchange")
@Tag(name = "Test APIs", description = "Test APIs for demo purpose")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<Flux<ExchangeRate>> findAll() {
        return ResponseEntity.ok(exchangeRateService.findAll());
    }

    @GetMapping("/{id}")
    public Mono<ExchangeRate> findById(@PathVariable String id) {
        return exchangeRateService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Mono<ExchangeRate>> saveExchangeRate(@RequestBody ExchangeRate exchangeRate) {
        return ResponseEntity.ok(exchangeRateService.save(exchangeRate));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ExchangeRate>> update(@RequestBody ExchangeRate exchangeRate, @PathVariable String id) {
        return exchangeRateService.update(exchangeRate, id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return exchangeRateService.delete(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
