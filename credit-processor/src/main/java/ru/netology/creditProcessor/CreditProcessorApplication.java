package ru.netology.creditProcessor;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class CreditProcessorApplication  {
    public static void main(String[] args) {
        SpringApplication.run(CreditProcessorApplication.class, args);
    }
}