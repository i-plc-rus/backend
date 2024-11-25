package com.example.sbertech.log;

import org.springframework.stereotype.Component;

/**
 * Junior logger. Remained for profiling goal. To delete after.
 */
@Component
public class Logger {

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
