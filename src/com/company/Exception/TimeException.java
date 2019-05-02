package com.company.Exception;

public class TimeException extends Exception {

    public TimeException(String errorMessage) {
        super(errorMessage);
        System.out.println("TimeException");
    }

}
