/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.se.Exceptions;

import java.io.Console;

/**
 *
 * @author Hanan_Sabry
 */
public class Test {
    public static void main(String[]args){
        Console c = System.console();
        c.readPassword();
    }
    class MyUtiltiy{        
        String doStuff(String arg1){
            return "Result is "+arg1;
        }
    }
}
