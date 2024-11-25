package com.bank.transactions.v2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.bank.common.exceptions.ApplicationRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class LoggerTest {

    @InjectMocks
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLog() {
        assertDoesNotThrow(() -> logger.log(this, "this_is_message"));
    }

    @Test
    void testLogThrows() {
        assertDoesNotThrow(() -> logger.log(this, "this_is_message", new ApplicationRuntimeException("this_is_exception")));
    }
}
