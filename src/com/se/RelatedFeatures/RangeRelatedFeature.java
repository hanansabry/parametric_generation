/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.RelatedFeatures;

import com.se.Exceptions.ApprovedValueException;
import com.se.Exceptions.FeatureValueException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.Exceptions.RangeValueException;
import static com.se.common.CommonFunctions.*;

/**
 *
 * @author Hanan_Sabry
 */
public class RangeRelatedFeature extends RelatedFeature {

    public static final String RANGE_VALS_SEPARATOR = "@";
    public static final String MULTI_VAL_SEPARATOR = "|";
    String fetValBK;
    String[] rangeVals;

    public RangeRelatedFeature(String plName, String vendorName, String fetNames, String fetVals, String updatedFetName, String updatedFetVal) throws NotValidRelatedFeatureException {
        super(plName, vendorName, fetNames, fetVals, updatedFetName, updatedFetVal);
        rangeVals = updatedFetVal.split(RANGE_VALS_SEPARATOR);
        fetValBK = fetVal;
    }

    @Override
    public String getUpdatedFetValue() throws NotValidRelatedFeatureException {
//        String tmpFetVal = fetVal;
//    	fetValBK = getFetVal(fetName, plId, comId);
    	if(fetVal==null){
    		fetValBK = getFetVal();
    	}else{
    		fetValBK = fetVal;
    	}
    	if(fetValBK==null){
    		return "";
    	}
        if (fetValBK.equalsIgnoreCase(N_A_VAL)) {
            return N_A_VAL;
        } else if (fetValBK.equalsIgnoreCase(N_R_VAL)) {
            return N_R_VAL;
        } //check if the value is multi (values is seperated by "|")
        else if (fetValBK.contains(MULTI_VAL_SEPARATOR)) {
            String finalVal = "";
            String[] fetVals = fetValBK.split("\\" + MULTI_VAL_SEPARATOR);
            for (String singleVal : fetVals) {
                this.fetValBK = singleVal;
                fetVal = fetValBK;
                finalVal += getUpdatedFetValue() + MULTI_VAL_SEPARATOR;
            }
            finalVal = finalVal.substring(0, finalVal.lastIndexOf(MULTI_VAL_SEPARATOR));
//            this.fetVal = tmpFetVal;
            return finalVal;
        }else if(fetValBK.contains("to")){
        	String[] fetVals = fetValBK.split("to");
        	for (String singleVal : fetVals) {
				this.fetValBK = singleVal.trim();
				fetVal = fetValBK;
			}
        }

        for (String val : rangeVals) {
            if (checkValInRange(val)) {
                return val;
            }
        }
        throw new RangeValueException();
    }
    
    @Override
    public void validateUpdatedFeatureValue() throws FeatureValueException,
    		ApprovedValueException {
    	// TODO Auto-generated method stub
//    	super.validateUpdatedFeatureValue();
    }

    private boolean checkValInRange(String rangeVal) throws RangeValueException {
        try {
            double fetValI = Double.parseDouble(fetValBK);
            double[] ranges = new double[2];
            //range
            if (rangeVal.contains("to")) {
                String[] ranges_ = rangeVal.split("to");
                ranges[0] = Double.parseDouble(ranges_[0].trim());
                ranges[1] = Double.parseDouble(ranges_[1].trim());
                if (fetValI >= ranges[0] && fetValI <= ranges[1]) {
                    return true;
                }
            } //the value is less than
            else if (rangeVal.contains("<")) {
                String[] ranges_ = rangeVal.split("<");
                ranges[0] = 0;
                ranges[1] = Double.parseDouble(ranges_[1].trim());
                if (fetValI < ranges[1]) {
                    return true;
                }
            } //the value is greater than
            else if (rangeVal.contains(">")) {
                String[] ranges_ = rangeVal.split(">");
                ranges[0] = 0;
                ranges[1] = Double.parseDouble(ranges_[1].trim());
                if (fetValI > ranges[1]) {
                    return true;
                }
            }
        }catch(NumberFormatException ex){
    		throw new RangeValueException("Current Value is not number");
    	}catch (Exception ex) {
            throw new RangeValueException();
        }
        return false;
    }
}
