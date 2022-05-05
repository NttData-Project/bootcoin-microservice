package com.demo.app.bootcoin.services.impl;

import com.demo.app.bootcoin.entities.PaymentType;
import com.demo.app.bootcoin.entities.Status;
import com.demo.app.bootcoin.entities.Transaction;
import com.demo.app.bootcoin.entities.Wallet;
import com.demo.app.bootcoin.entities.models.CurrentAccount;
import com.demo.app.bootcoin.entities.models.FixedTermAccount;
import com.demo.app.bootcoin.entities.models.SavingAccount;
import com.demo.app.bootcoin.exception.CustomNotFoundException;
import com.demo.app.bootcoin.repositories.TransactionRepository;
import com.demo.app.bootcoin.repositories.WalletRepository;
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
    private final TransactionRepository transactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository, TransactionRepository transactionRepository,WebClient.Builder webClientPassiveCard
            ,@Value("${passive.card}") String passiveCardUrl) {
        this.walletRepository = walletRepository;
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
        Mono<Wallet> wallet = walletRepository.findById(walletId).flatMap(w-> transaction.flatMap(t->{
            if(t.getPaymentType().equals(PaymentType.YANKI)) {
                if(w.getBalance() > t.getAmount()) {
                    w.setBalance(w.getBalance() - t.getAmount());
                    t.setStatus(Status.PROCESSED);
                } else {
                    t.setStatus(Status.REJECTED);
                    return Mono.error(new CustomNotFoundException("El usuario no tiene suficiente credito"));
                }
                return transactionRepository.save(t).then(walletRepository.save(w));
            }
            else{
                Mono<CurrentAccount> currentAccount = findCurrentAccountByDni(w.getDocumentNumber());
                t.setStatus(Status.PROCESSED);
                return currentAccount.flatMap(x->{
                    x.setBalance(x.getBalance().add(BigDecimal.valueOf(t.getAmount()).negate()));
                    return updateCurrentAccount(x);
                }).then(transactionRepository.save(t)).then(walletRepository.save(w));
            }
        }));
        return wallet;
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
