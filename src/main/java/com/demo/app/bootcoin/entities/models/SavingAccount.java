package com.demo.app.bootcoin.entities.models;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SavingAccount {
    private String id;
    private BigDecimal balance;
    private TypeCurrency currency;
    private String accountNumber;
    private Integer cvc;
    private String identifier;
}
