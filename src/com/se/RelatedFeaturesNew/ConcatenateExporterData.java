package com.se.RelatedFeaturesNew;

import static com.se.RelatedFeaturesNew.RelatedFeatures.*;

import java.math.BigDecimal;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.se.Exceptions.PlException;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaMappingRelatedFets;

public class ConcatenateExporterData extends MultiExporterData{
	public static String FET_VAL_CONCATENATE;
	public static final String SPACE_SEPERATOR = "\\s";
	public static final HashMap<String, String> mapping = new HashMap();
	
//	static{
//		mapping.put("1", "Single");
//		mapping.put("2","Dual");
//		mapping.put("4", "Quad");
//	}
	
	@Override
	public void setGeneratedFetVal(String uFetVal) {
		String finalVal = "";
		String fetVal = getFetVal();
		if(uFetVal==null||uFetVal.trim().equals("")){
			FET_VAL_CONCATENATE = SPACE_SEPERATOR;
		}else{
			FET_VAL_CONCATENATE = uFetVal;
		}
		
        String[] fetVals = fetVal.split(CONCATENATE_FET_CHAR_SEP);
        String[] fetNames = fetName.split(CONCATENATE_FET_CHAR_SEP);
        boolean na = false;
        boolean nr = false;
        boolean valB = false;
        int i=0;
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
                if(FET_VAL_CONCATENATE.equals(SPACE_SEPERATOR)){
                	finalVal += getMappedValue(fetNames[i], val) + " ";
                }else{
                	finalVal += getMappedValue(fetNames[i], val) + FET_VAL_CONCATENATE;
                }
            }
            i++;
        }
        if (na && nr) {
            super.setGeneratedFetVal(N_R_VAL);
            return;
        } else if (na) {
            super.setGeneratedFetVal(N_A_VAL);
        } else if (nr) {
            super.setGeneratedFetVal(N_R_VAL);
        } else {
        	if(FET_VAL_CONCATENATE.equals(SPACE_SEPERATOR)){
        		finalVal = finalVal.trim();
        	}else{
        		finalVal = finalVal.substring(0, finalVal.lastIndexOf(FET_VAL_CONCATENATE));
        	}
        	finalVal = finalVal.replace(N_A_VAL, "");
        	finalVal = finalVal.replace(N_R_VAL, "");
            
            super.setGeneratedFetVal(finalVal);
        }
	}
	
	public String getMappedValue(String fetName, String val){
		Session session = SessionUtil.getSession();
		Criteria cr = session.createCriteria(ParaMappingRelatedFets.class);
		try {
			int plId = CommonFunctions.getPlId(plName);		
			cr.add(Restrictions.eq("plId", new BigDecimal(plId)));
			System.out.println("fetname = "+fetName+", fetval = "+val);
			cr.add(Restrictions.eq("idFetName", fetName));
			cr.add(Restrictions.eq("idFetValue", val));
			ParaMappingRelatedFets rule = (ParaMappingRelatedFets) cr.uniqueResult();
			if(rule == null){
				return val;
			}else{
				return rule.getMappedFetValue();
			}
		} catch (PlException e) {
			e.printStackTrace();
			return val;
		}		
	}
}
