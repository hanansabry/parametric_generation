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
public class FormulaException extends NotValidRelatedFeatureException{
    
    public FormulaException(String message) {
        super(message);
    }

    public FormulaException() {
        super("Not Valid Formula");
    }    

    @Override
    public String getMessage() {
        return "Not Valid Formula";
    }    
}
