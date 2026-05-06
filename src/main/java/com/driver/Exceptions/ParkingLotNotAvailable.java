package com.driver.Exceptions;

public class ParkingLotNotAvailable extends RuntimeException{
    public ParkingLotNotAvailable(String message){
        super(message);
    }
}
