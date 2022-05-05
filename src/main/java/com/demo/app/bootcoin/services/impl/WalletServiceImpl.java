package com.demo.app.bootcoin.services.impl;

import com.demo.app.bootcoin.entities.*;
import com.demo.app.bootcoin.entities.models.CurrentAccount;
import com.demo.app.bootcoin.exception.CustomNotFoundException;
import com.demo.app.bootcoin.repositories.TransactionRepository;
import com.demo.app.bootcoin.repositories.WalletRepository;
import com.demo.app.bootcoin.services.ExchangeRateService;
import com.demo.app.bootcoin.services.WalletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WebClient webClientPassiveCard;
    private final ExchangeRateService exchangeRateService;
    private final TransactionRepository transactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository, TransactionRepository transactionRepository, WebClient.Builder webClientPassiveCard
            , @Value("${passive.card}") String passiveCardUrl, ExchangeRateService exchangeRateService) {
        this.walletRepository = walletRepository;
        this.exchangeRateService = exchangeRateService;
        this.webClientPassiveCard = webClientPassiveCard.baseUrl(passiveCardUrl).build();
        this.transactionRepository = transactionRepository;
    }

    private Mono<CurrentAccount> findCurrentAccountByDni(String dni){
        return webClientPassiveCard.get().uri("/currentAccount/identifier/" + dni).
                retrieve().bodyToMono(CurrentAccount.class);
    }

    private Mono<CurrentAccount> updateCurrentAccount(CurrentAccount currentAccount) {
        return webClientPassiveCard.put().uri("/currentAccount/" + currentAccount.getId()).body(Mono.just(currentAccount), CurrentAccount.class).retrieve().bodyToMono(CurrentAccount.class);
    }

    @Override
    public Flux<Wallet> findAll() {
        return walletRepository.findAll();
    }

    @Override
    public Mono<Wallet> findById(String id) {
        return walletRepository.findById(id);
    }

    @Override
    public Mono<Wallet> save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Mono<Wallet> validateTransaction(String transactionId,String walletId) {
        Mono<Transaction> transaction = transactionRepository.findById(transactionId);
        Mono<ExchangeRate> exchange = exchangeRateService.findById("100");
        return exchange.flatMap(e-> transaction.flatMap(t-> walletRepository.findById(t.getFromAccount()).flatMap(w1-> walletRepository.findById(t.getToAccount()).flatMap(w2->{
            if(t.getPaymentType().equals(PaymentType.YANKI)) {
                if(w1.getBalance() > t.getAmount()) {
                    w1.setBalance(w1.getBalance() - (t.getAmount()*e.getSalePrice()));
                    w2.setBalance(w2.getBalance() + (t.getAmount()*e.getPurchasePrice()));
                    t.setStatus(Status.PROCESSED);
                } else {
                    t.setStatus(Status.REJECTED);
                    return Mono.error(new CustomNotFoundException("El usuario no tiene suficiente credito"));
                }
                return transactionRepository.save(t).then(walletRepository.save(w1)).then(walletRepository.save(w2));
            }
            else{
                Mono<CurrentAccount> currentAccount = findCurrentAccountByDni(w1.getDocumentNumber());
                w2.setBalance(w2.getBalance() + (t.getAmount()*e.getPurchasePrice()));
                t.setStatus(Status.PROCESSED);
                return currentAccount.flatMap(x->{
                    x.setBalance(x.getBalance().add(BigDecimal.valueOf(t.getAmount()*e.getSalePrice()).negate()));
                    return updateCurrentAccount(x);
                }).then(transactionRepository.save(t)).then(walletRepository.save(w1)).then(walletRepository.save(w2));
            }
        }))));
    }

    @Override
    public Mono<Wallet> update(Wallet wallet, String id) {
        return walletRepository.findById(id).flatMap(x->{
            x.setBalance(wallet.getBalance());
            x.setEmail(wallet.getEmail());
            x.setDocumentNumber(wallet.getDocumentNumber());
            x.setPhoneNumber(wallet.getPhoneNumber());
            return walletRepository.save(x);
        });
    }

    @Override
    public Mono<String> delete(String id) {
        return walletRepository.deleteById(id).thenReturn(id);
    }
}
