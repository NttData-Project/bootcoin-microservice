package com.demo.app.bootcoin.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@JsonPropertyOrder({"id","fromAccount","toAccount","amount","status","createdAt","updateAt"})
@Document(collection = "transaction")
@Data
public class Transaction {
    @Id
    private String id;

    @NotEmpty
    @Field(name = "from_account")
    private String fromAccount;

    @NotEmpty
    @Field(name = "to_account")
    private String toAccount;

    @Min(value = 1)
    private Double amount;

    @NotEmpty
    private String status;
}
