package com.se.RelatedFeaturesNew;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.se.Exceptions.FormulaException;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;
import com.se.pojos.MathRuleRelatedFets;

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
//        	for(int i=0; i<fetVal.length(); i++){
//        		if(!Character.isDigit(fetVal.charAt(i))){
//        			fetVal = fetVal.substring(0, i);
//        		}
//        	}
        	
        	//get prefix from fetvel
        	int prefixVal = 1;
        	char ch = fetVal.charAt(fetVal.length()-1);
        	if(!Character.isDigit(ch)){
        		String prefix = ch+"";
        		prefixVal = getFormulaValue(prefix);
        		fetVal = fetVal.substring(0, fetVal.length()-1);
        	}
        	
        	double fetValI = Double.parseDouble(fetVal) * prefixVal;
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
	
	public int getFormulaValue(String formulaValue_) throws FormulaException{
		Session session = SessionUtil.getSession();
		Criteria cr = session.createCriteria(MathRuleRelatedFets.class);
		try{
			int plId = CommonFunctions.getPlId(plName);
			cr.add(Restrictions.eq("plId", new BigDecimal(plId)));
			cr.add(Restrictions.eq("idFetName", getFetName()));
			cr.add(Restrictions.eq("prefix", formulaValue_));
			cr.add(Restrictions.eq("updatedFetName", getUpdatedFetName()));
			
			MathRuleRelatedFets rule = (MathRuleRelatedFets)cr.uniqueResult();
			if(rule==null){
				throw new FormulaException();
			}else{
				return rule.getValue().intValue();
			}
		}catch(Exception ex){
			throw new FormulaException();
		}finally{
			session.close();
		}
		
	}
}
