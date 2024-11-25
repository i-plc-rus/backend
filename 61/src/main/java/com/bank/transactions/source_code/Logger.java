
package com.bank.transactions.source_code;

import org.springframework.stereotype.Component;

@Component
public class Logger {

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
