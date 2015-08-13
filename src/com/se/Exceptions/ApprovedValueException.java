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
public class ApprovedValueException extends FeatureValueException{

    public ApprovedValueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
    
    public ApprovedValueException() {
		super("Contains Approved Value");
		// TODO Auto-generated constructor stub
	}

	@Override
    public String getMessage() {
        return "Contains Approved Value";
    }
    
}
