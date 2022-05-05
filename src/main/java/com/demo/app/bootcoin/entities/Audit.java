package com.demo.app.bootcoin.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class Audit {
    @CreatedDate
    @Field(name = "create_at")
    private Date createAt;

    @LastModifiedDate
    @Field(name = "update_at")
    private Date updateAt;
}