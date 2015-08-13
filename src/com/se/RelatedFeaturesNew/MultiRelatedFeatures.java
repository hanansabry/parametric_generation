package com.se.RelatedFeaturesNew;

import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.FeatureValueException;
import com.se.Exceptions.NotValidRelatedFeatureException;

public class MultiRelatedFeatures extends RelatedFeatures{

	String[] fetNames;
	public MultiRelatedFeatures(String plName, String manName, String fetName,
			String fetVal, String updatedFetName, String updateVal) throws NotValidRelatedFeatureException {
		super(plName, manName, fetName, fetVal, updatedFetName, updateVal);
	}

	@Override
	public void validateFetName(String fetName) throws FeatureNameException {
        String colNms = "";
        String fetIds = "";
        //separate features
        fetNames = fetName.split(CONCATENATE_FET_CHAR_SEP);
        for (String fet : fetNames) {
            super.validateFetName(fet);
            fetIds += fetId + CONCATENATE_FET_CHAR;
            colNms += fetColNm + CONCATENATE_FET_CHAR;
        }
        colNms = colNms.substring(0, colNms.lastIndexOf(CONCATENATE_FET_CHAR));
        fetIds = fetIds.substring(0, fetIds.lastIndexOf(CONCATENATE_FET_CHAR));
        fetColNm = colNms;
        fetId = fetIds;
	}
	
	@Override
	public void validateFetVal(String fetVal, String fetName)
			throws FeatureValueException {
		String valIds = "";
    	String tmpVals = fetVal;
    	String tmpFets = fetName;
        if(fetVal==null || fetVal.equals("")){
//        	throw new FeatureValueException("Id Feature Value is null");
        }else{
            //seperate feature values
            String[] fetVals = fetVal.split(CONCATENATE_FET_CHAR_SEP);
            if(fetVals.length != fetNames.length){
                throw new FeatureValueException("Features values not equal to the feature names");
            }else{
            	int i=0;
                for (String fetV : fetVals) {
                	String fetN = fetNames[i];
                    super.validateFetVal(fetV, fetN);
                    valIds += fetValId + CONCATENATE_FET_CHAR;
                    i++;
                }
                valIds = valIds.substring(0, valIds.lastIndexOf(CONCATENATE_FET_CHAR));
                fetValId = valIds;
            }
        }
	}
	
	
	public static String generateExportStatement(int plId, int manId,String fetId, String fetColNm, String fetValId, boolean man) {
		String query = "";
		query = "SELECT c.com_id,"
				+ "  cm.get_pl_name(c.pl_id) pl,"
				+ "  c.com_partnum ,"
				+ "  get_man_name(c.MAN_ID) ,"
//				+ "  get_man_code(c.MAN_ID) ,"
				+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
				+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
				+ "  getPDF_url(c.pdf_id) ,"
//				+ "  c.lc_state ,"
//				+ "  c.rohs,"
				+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
				+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
				+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
				+ " FROM cm.xlp_se_component c ,"
				+ "  cm.DYNAMIC_FLAT d"
				+ " WHERE c.com_id = d.com_id"
				+ " AND c.pl_id    = "+plId;
		if(fetValId != null){
			String[] colNms = fetColNm.split(CONCATENATE_FET_CHAR_SEP);
			String[] fetVals = fetValId.split(CONCATENATE_FET_CHAR_SEP);
			for (int i=0; i<colNms.length; i++) {
				query += " AND d."+colNms[i]+" = "+ fetVals[i];
			}			
		}
//	}
	if(man)
		query += " AND c.man_id = "+manId;
	return query;
	}
}
