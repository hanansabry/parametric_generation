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
public class FeatureValueException extends NotValidRelatedFeatureException {

    String message;

    public FeatureValueException(String message) {
        super(message);
    }
    
    public FeatureValueException() {
		super("Error While handling Feature Value");
	}

    @Override
    public String getMessage() {
        return message;
    }

}
