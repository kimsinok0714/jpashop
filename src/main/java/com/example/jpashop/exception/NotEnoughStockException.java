package com.example.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException() {
        super();

    }

    public NotEnoughStockException(String message) {
        super(message);

    }

}
