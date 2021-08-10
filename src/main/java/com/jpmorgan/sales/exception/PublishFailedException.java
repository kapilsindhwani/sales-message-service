package com.jpmorgan.sales.exception;

public class PublishFailedException extends RuntimeException{

    public PublishFailedException(String errorMessage) {
        super(errorMessage);
    }

}
