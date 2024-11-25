package com.bank.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankTransactionsApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BankTransactionsApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

}
