/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.RelatedFeatures;

import com.se.Exceptions.FormulaException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import java.util.HashMap;

/**
 *
 * @author Hanan_Sabry
 */
public class FormulaRuleRelatedFeature extends FormulaRelatedFeature {

    public static final HashMap<String, Integer> rules = new HashMap();

    public FormulaRuleRelatedFeature(String plName, String vendorName, String fetNames, String fetVals, String updatedFetName, String updatedFetVal) throws NotValidRelatedFeatureException {
        super(plName, vendorName, fetNames, fetVals, updatedFetName, updatedFetVal);
    }

    static {
        //get rules from database
        rules.put("M", 1024);
        rules.put("G", 2048);
        rules.put("L", 512);
    }

    @Override
    public int getMultipleValue() throws FormulaException {        
        String formulaValue_ = updatedFetVal.split("\\" + MULTIPLY)[1].trim();
        if(rules.containsKey(formulaValue_)){
            return rules.get(formulaValue_);
        }else{
            throw new FormulaException();
        }
    }
}
