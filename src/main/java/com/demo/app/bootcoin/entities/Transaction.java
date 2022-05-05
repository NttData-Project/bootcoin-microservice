package com.demo.app.bootcoin.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@JsonPropertyOrder({"id","fromAccount","toAccount","amount","status","createdAt","updateAt"})
@Document(collection = "transaction")
@Data
public class Transaction extends Audit{
    @Id
    private String id;

    @NotEmpty
    @Field(name = "from_account")
    private String fromAccount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @NotEmpty
    @Field(name = "to_account")
    private String toAccount;

    @Min(value = 1)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private Status status;
}
