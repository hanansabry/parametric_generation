/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.Exceptions;

/**
 *
 * @author Hanan_Sabry
 */
public class RangeValueException extends NotValidRelatedFeatureException {

    public RangeValueException(String message) {
        super(message);
    }

    public RangeValueException() {
        super("Range Value Format is not valid");
    }

    @Override
    public String getMessage() {
        return message;
    }
}
