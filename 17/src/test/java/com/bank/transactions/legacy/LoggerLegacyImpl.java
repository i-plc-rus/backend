
package com.bank.transactions.legacy;

import org.springframework.stereotype.Component;

@Component
public class LoggerLegacyImpl {

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
