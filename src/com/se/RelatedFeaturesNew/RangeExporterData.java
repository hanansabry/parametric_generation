package com.se.RelatedFeaturesNew;

import com.se.Exceptions.RangeValueException;

import static com.se.RelatedFeaturesNew.RelatedFeatures.*;

public class RangeExporterData extends DirectExporterData{
	
	public static final String RANGE_VALS_SEPARATOR = "@";
    public static final String MULTI_VAL_SEPARATOR = "|";
    String[] rangeVals;
    
    
	@Override
	public void setGeneratedFetVal(String uFetVal) {
		rangeVals = uFetVal.split(RANGE_VALS_SEPARATOR);
		try{
		String fetVal = getFetVal();
		if(fetVal==null){
			super.setGeneratedFetVal("");
			return;
    	}
    		
        if (fetVal.equalsIgnoreCase(N_A_VAL)) {
        	super.setGeneratedFetVal(N_A_VAL);
        	return;
        } else if (fetVal.equalsIgnoreCase(N_R_VAL)) {
        	super.setGeneratedFetVal(N_R_VAL);
        	return;
        } //check if the value is multi (values is seperated by "|")
        else if (fetVal.contains(MULTI_VAL_SEPARATOR)) {
            String finalVal = "";
            String[] fetVals = fetVal.split("\\" + MULTI_VAL_SEPARATOR);
            for (String singleVal : fetVals) {
                finalVal += getRangeValue(singleVal) + MULTI_VAL_SEPARATOR;
            }
            finalVal = finalVal.substring(0, finalVal.lastIndexOf(MULTI_VAL_SEPARATOR));
//            this.fetVal = tmpFetVal;
            super.setGeneratedFetVal(finalVal);
            return;
        }else if(fetVal.contains("to")){
        	String[] fetVals = fetVal.split("to");
        	for (String singleVal : fetVals) {
				fetVal = singleVal.trim();
			}
        }

       
        String generatedVal = getRangeValue(fetVal);
        super.setGeneratedFetVal(generatedVal);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private String getRangeValue(String fetVal) throws RangeValueException{
		 for (String val : rangeVals) {
	        if (checkValInRange(val, fetVal)) {
	           return val;
	        }
	     }
		 throw new RangeValueException();
	}
	
	private boolean checkValInRange(String rangeVal, String fetVal) throws RangeValueException {
        try {
            double fetValI = Double.parseDouble(fetVal);
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
