
package com.bank.transactions.old;

import org.springframework.stereotype.Component;

@Component
public class OldLogger {

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
