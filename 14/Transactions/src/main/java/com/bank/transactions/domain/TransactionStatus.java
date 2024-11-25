package com.bank.transactions.domain;

import java.util.HashMap;
import java.util.Map;

public enum TransactionStatus {
    PENDING(1),
    PROCESSED(2),
    COMPLETED(3);

    private final byte id;
    private static Map<Byte, TransactionStatus> valuesMap;

    TransactionStatus(int id) {
        this.id = (byte) id;
        registerToMap(this);
    }

    public byte getId() {
        return id;
    }

    private static void registerToMap(TransactionStatus value) {
        if (valuesMap == null ) {
            valuesMap = new HashMap<>();
        }
        valuesMap.put(value.getId(), value);
    }

    public static TransactionStatus getById(byte id) {
        return valuesMap.get(id);
    }
}
