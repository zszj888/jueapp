package com.somecom;

import lombok.Data;

@Data
public class ResponseEntity {
    private static final Integer STATUS_OK = 0;
    private static final Integer STATUS_ERR = -1;
    private Integer code;
    private String message;

    private ResponseEntity(Integer code) {
        this.code = code;
    }

    private ResponseEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity ok() {
        return new ResponseEntity(STATUS_OK);
    }

    public static ResponseEntity err() {
        return new ResponseEntity(STATUS_ERR);
    }

    public static ResponseEntity code(Integer code) {
        return new ResponseEntity(code);
    }

    public static ResponseEntity with(Integer code, String message) {
        return new ResponseEntity(code, message);
    }
}
