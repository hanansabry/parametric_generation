/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.RelatedFeatures;

import com.se.Exceptions.ApprovedValueException;
import com.se.Exceptions.FeatureValueException;
import com.se.Exceptions.FormulaException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.common.CommonFunctions;

import static com.se.common.CommonFunctions.*;

/**
 *
 * @author Hanan_Sabry
 */
public class FormulaRelatedFeature extends RelatedFeature {

    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";

    public FormulaRelatedFeature(String plName, String vendorName, String fetNames, String fetVals, String updatedFetName, String updatedFetVal) throws NotValidRelatedFeatureException {
        super(plName, vendorName, fetNames, fetVals, updatedFetName, updatedFetVal);
    }

    @Override
    public String getUpdatedFetValue() throws NotValidRelatedFeatureException {
        String finalVal = "";
    	try {
        	String fetValBK = getFetVal();
        	if(fetValBK == null){
        		return "";
        	}
        	for(int i=0; i<fetValBK.length(); i++){
        		if(!Character.isDigit(fetValBK.charAt(i))){
        			fetValBK = fetValBK.substring(0, i);
        		}
        	}
        	double fetValI = Double.parseDouble(fetValBK);
            if (!(updatedFetVal.contains(MULTIPLY) ^ updatedFetVal.contains(DIVIDE))) {
                throw new FormulaException();
            } else {
                int formulaVal = getMultipleValue();
                if (updatedFetVal.contains(MULTIPLY)) {
                	finalVal = (fetValI * formulaVal) + "";
                }else if(updatedFetVal.contains(DIVIDE)){
                	finalVal = (fetValI / formulaVal) + "";
                }else{
                    throw new FormulaException();
                }
            }
        } catch (NumberFormatException | FormulaException ex) {
            throw new FormulaException();
        }
//        super.updatedFetVal = updatedFetVal;
        return finalVal;
    }        

    public int getMultipleValue() throws FormulaException{
        int formulaValue;
        if (updatedFetVal.contains(MULTIPLY)) {
            String formulaValue_ = updatedFetVal.split("\\" + MULTIPLY)[1].trim();
            formulaValue = Integer.parseInt(formulaValue_);
        } else if (updatedFetVal.contains(DIVIDE)) {
            String formulaValue_ = updatedFetVal.split("\\" + DIVIDE)[1].trim();
            formulaValue = Integer.parseInt(formulaValue_);
        } else {
            throw new FormulaException();
        }
        return formulaValue;
    }
    
    @Override
    public void validateUpdatedFeatureValue() throws FeatureValueException,
    		ApprovedValueException {
    	// TODO Auto-generated method stub
//    	super.validateUpdatedFeatureValue();
    }
   
}
