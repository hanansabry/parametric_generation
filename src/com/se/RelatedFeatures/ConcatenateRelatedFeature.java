/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.RelatedFeatures;

import com.se.Exceptions.*;
import com.se.common.CommonFunctions;

import static com.se.common.CommonFunctions.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hanan_Sabry
 */
public class ConcatenateRelatedFeature extends MultiRelatedFeatures {

    public static String FET_VAL_CONCATENATE;

    public ConcatenateRelatedFeature(String plName, String vendorName, String fetNames, String fetVals, String updatedFetName, String updatedFetVal) throws NotValidRelatedFeatureException {
        super(plName, vendorName, fetNames, fetVals, updatedFetName, updatedFetVal);
    }

    @Override
    public String getUpdatedFetValue() throws NotValidRelatedFeatureException {
        String updatedFetVal = "";
//        String idFetVal = "";
        if(fetVal==null){
        	fetVal = getFetVal();
        }
        String[] fetVals = fetVal.split(CONCATENATE_FET_CHAR_SEP);
        boolean na = false;
        boolean nr = false;
        boolean valB = false;
        for (String val : fetVals) {
            if ((val.equalsIgnoreCase(N_R_VAL) || val.equalsIgnoreCase(N_A_VAL)) && !valB) {
                if (val.equalsIgnoreCase(N_A_VAL)) {
                    na = true;
                } else {
                    nr = true;
                }
            } else {
                na = false;
                nr = false;
                valB = true;
                updatedFetVal += val + FET_VAL_CONCATENATE;
            }
        }
        if (na && nr) {
            return N_A_VAL;
        } else if (na) {
            return N_A_VAL;
        } else if (nr) {
            return N_R_VAL;
        } else {
            updatedFetVal = updatedFetVal.substring(0, updatedFetVal.lastIndexOf(FET_VAL_CONCATENATE));
            return updatedFetVal;
        }        
    }
    
    @Override
    public String getFetVal() throws NotValidRelatedFeatureException {
    	String updatedFetVal = "";
        String idFetVal = "";
    	if(fetVal==null){
        	for (String fet : fetNames) {
//    			super.fetName = fet;
//    			super.validateIdFeatureValue(null);
        		fetName = fet;
    			String tmpVal = getFetVal();
				idFetVal += tmpVal + CONCATENATE_FET_CHAR;
//				fetVal = null;
			}
    		idFetVal = idFetVal.substring(0, idFetVal.lastIndexOf(CONCATENATE_FET_CHAR));
    		fetVal = idFetVal;
        }    	
    	return fetVal;
    }

    @Override
    public void validateUpdatedFeatureValue() throws FeatureValueException, ApprovedValueException {
        FET_VAL_CONCATENATE = updatedFetVal;
    }
    
    @Override
    public void validateIdFeatureValue() throws NotValidRelatedFeatureException {
    	String idFetVal = "";
    	if(fetVal==null){
    		//get current values from db
//    		for (String fet : fetNames) {
////    			super.fetName = fet;
////    			super.validateIdFeatureValue(null);
//    			String tmpVal = getFetVal(fet, plId, comId);
//				idFetVal += fetVal + CONCATENATE_FET_CHAR;
//				fetVal = null;
//			}
//    		idFetVal = idFetVal.substring(0, idFetVal.lastIndexOf(CONCATENATE_FET_CHAR));
//    		fetVal = idFetVal;
    	}else{
    		super.validateIdFeatureValue();
    	}    	
    }    
}
