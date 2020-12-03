package com.somecom.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum TransactionType {

    INCOME((byte) 1, "收入"),
    OUTCOME((byte) 2, "转出");

    private Byte code;

    private String message;

    TransactionType(Byte code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TransactionType of(Byte code) {
        Optional<TransactionType> first = Stream.of(TransactionType.values()).filter(s -> code.equals(s.getCode())).findFirst();
        first.orElseThrow(() -> new IllegalArgumentException("code" + code +
                "枚举不存在"));
        return first.get();
    }
}

