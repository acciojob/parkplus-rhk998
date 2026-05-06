package com.driver.Exceptions;

public class InsufficientAmount extends RuntimeException{
    public InsufficientAmount(String message){
        super(message);
    }
}
