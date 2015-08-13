package com.se.RelatedFeaturesNew;

import com.se.Exceptions.FormulaException;

public class FormulaExporterData extends DirectExporterData{
	
	public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";

	@Override
	public void setGeneratedFetVal(String updatedFetVal){
		String finalVal = "";
    	try {
        	String fetVal = getFetVal();
        	if(fetVal == null){
        		super.setGeneratedFetVal("");
        		return;
        	}
        	for(int i=0; i<fetVal.length(); i++){
        		if(!Character.isDigit(fetVal.charAt(i))){
        			fetVal = fetVal.substring(0, i);
        		}
        	}
        	double fetValI = Double.parseDouble(fetVal);
            if (!(updatedFetVal.contains(MULTIPLY) ^ updatedFetVal.contains(DIVIDE))) {
                throw new FormulaException();
            } else {
                int formulaVal = getMultipleValue(updatedFetVal);
                if (updatedFetVal.contains(MULTIPLY)) {
                	finalVal = (fetValI * formulaVal) + "";
                }else if(updatedFetVal.contains(DIVIDE)){
                	finalVal = (fetValI / formulaVal) + "";
                }else{
                    throw new FormulaException();
                }
            }
        } catch (NumberFormatException | FormulaException ex) {
            ex.printStackTrace();
        }
//        super.updatedFetVal = updatedFetVal;
        super.setGeneratedFetVal(finalVal);
	}
	
	public int getMultipleValue(String updatedFetVal) throws FormulaException{
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
}
