package com.demo.app.bootcoin.config;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    @KafkaListener(topics = "bootcoins",groupId = "myGroup")
    void listener(String data){
        System.out.println("Data recibida: " + data);
    }
}
