package com.demo.app.bootcoin.services.impl;

import com.demo.app.bootcoin.entities.Wallet;
import com.demo.app.bootcoin.repositories.WalletRepository;
import com.demo.app.bootcoin.services.WalletService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
