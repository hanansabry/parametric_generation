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
public class NotValidRelatedFeatureException extends Exception {

    String message;

    public NotValidRelatedFeatureException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
