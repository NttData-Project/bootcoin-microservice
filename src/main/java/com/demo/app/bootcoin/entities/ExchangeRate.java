package com.demo.app.bootcoin.entities;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("ExchangeRate")
@Data
public class ExchangeRate {
    private Double salePrice;
    private Double purchasePrice;
}
