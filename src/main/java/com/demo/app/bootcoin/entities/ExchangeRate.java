package com.demo.app.bootcoin.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("ExchangeRate")
@Data
public class ExchangeRate{
    @Id
    private String id;
    private Double salePrice;
    private Double purchasePrice;
}
