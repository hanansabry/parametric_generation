/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.RelatedFeatures;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.se.Exceptions.ApprovedValueException;
import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.FeatureValueException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.Exceptions.PlException;
import com.se.common.CommonFunctions;

/**
 *
 * @author Hanan_Sabry
 */
public class MultiRelatedFeatures extends RelatedFeature {
    int fetNum;
    String[] fetNames;

    public MultiRelatedFeatures(String plName, String vendorName, String fetNames, String fetVals, String updatedFetName, String updatedFetVal) throws NotValidRelatedFeatureException {
        super(plName, vendorName, fetNames, fetVals, updatedFetName, updatedFetVal);
    }

    @Override
    public void validateIdFeatureName() throws FeatureNameException, PlException {
        String tmpFets = fetName;
        String colNms = "";
        //separate features
        fetNames = fetName.split(CONCATENATE_FET_CHAR_SEP);
        fetNum = fetNames.length;
        for (String fetName : fetNames) {
            super.fetName = fetName.trim();
            super.validateIdFeatureName();
            colNms += fetIdColNm + CONCATENATE_FET_CHAR;
        }
        colNms = colNms.substring(0, colNms.lastIndexOf(CONCATENATE_FET_CHAR));
        super.fetName = tmpFets;
        super.fetIdColNm = colNms;
    }

    @Override
    public void validateIdFeatureValue() throws NotValidRelatedFeatureException {
        String valIds = "";
    	String tmpVals = fetVal;
    	String tmpFets = fetName;
        if(fetVal==null){
        	throw new FeatureValueException("Id Feature Value is null");
        }else{
            //seperate feature values
            String[] fetVals = fetVal.split(CONCATENATE_FET_CHAR_SEP);
            if(fetVals.length != fetNum){
                throw new FeatureValueException("Features values not equal to the feature names");
            }else{
            	int i=0;
                for (String fetVal : fetVals) {                	
                	super.fetName = fetNames[i];
                    super.fetVal = fetVal;
                    super.validateIdFeatureValue();
                    valIds += fetValId + CONCATENATE_FET_CHAR;
                    i++;
                }
                valIds = valIds.substring(0, valIds.lastIndexOf(CONCATENATE_FET_CHAR));
                fetValId = valIds;
                super.fetVal = tmpVals;
                super.fetName = tmpFets;
            }
        }
    }
    
    public void validateIdFeatureValue(Object o) throws NotValidRelatedFeatureException{
    	super.validateIdFeatureValue();
    }
    
    @Override
    public String generateExportStatement(boolean man){
    	String query = "";
//    	if(fetId==MASK_FET_ID){
//			query = "SELECT c.com_id,"
//					+ "        cm.get_pl_name(c.pl_id) pl ,"
//					+ "        c.com_partnum ,"
//					+ "        get_man_name(c.MAN_ID) ,"
////					+ "        get_man_code(c.MAN_ID) ,"
//					+ "        CM.GET_FAMILY_NAME(c.family_id) ,"
//					+ "        REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
//					+ "        getPDF_url(c.pdf_id) ,"
////					+ "        c.lc_state ,"
////					+ "        c.rohs,"
//					+ "        IMPORTER.GET_MSTR_MASK(c.com_id),"
//					+ "        IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
//					+ "        IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
//					+ "      FROM cm.xlp_se_component c ,"
//					+ "        CM.XLP_SE_COMPONENT_PART2 c2,"
//					+ "        cm.TBL_MSTR_MASK m"
//					+ "      WHERE c.com_id  = c2.com_id"
//					+ "      AND c2.MASK_ID  = m.ID"
//					+ "      AND c.pl_id     = :plId";					
//			if(fetVal != null){
//				query += "      AND m.ID = :valId ";
//			}
//		}else if(fetId == FAM_FET_ID){
//			query = "SELECT c.com_id,"
//					+ "  cm.get_pl_name(c.pl_id) pl ,"
//					+ "  c.com_partnum ,"
//					+ "  get_man_name(c.MAN_ID) ,"
////					+ "  get_man_code(c.MAN_ID) ,"
//					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
//					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
//					+ "  getPDF_url(c.pdf_id) ,"
////					+ "  c.lc_state ,"
////					+ "  c.rohs,"
//					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
//					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
//					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
//					+ " FROM cm.xlp_se_component c ,"
//					+ "  cm.tbl_map_fam f"
//					+ " WHERE pl_id  = :plId"
//					+ " AND c.fam_id = f.FAM_ID";					
//			if(fetVal != null){
//				query += " AND f.FAM_ID = :valId ";
//			}
//		}else if(fetId == GEN_FET_ID){
//			query = "SELECT c.com_id,"
//					+ "  cm.get_pl_name(c.pl_id) pl,"
//					+ "  c.com_partnum ,"
//					+ "  get_man_name(c.MAN_ID) ,"
////					+ "  get_man_code(c.MAN_ID) ,"
//					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
//					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
//					+ "  getPDF_url(c.pdf_id) ,"
////					+ "  c.lc_state ,"
////					+ "  c.rohs,"
//					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
//					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
//					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
//					+ " FROM cm.xlp_se_component c ,"
//					+ "   cm.TBL_MAP_GEN g"
//					+ " WHERE pl_id   = :plId"
//					+ " AND c.GEN_ID  = g.GEN_ID";					
//			if(fetVal != null){
//				query += " AND g.GEN_ID = :valId ";
//			}
//		}else if(fetId == FAMILY_FET_ID){
//			query = "SELECT c.com_id,"
//					+ "  cm.get_pl_name(c.pl_id) pl ,"
//					+ "  c.com_partnum ,"
//					+ "  get_man_name(c.MAN_ID) ,"
////					+ "  get_man_code(c.MAN_ID) ,"
//					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
//					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
//					+ "  getPDF_url(c.pdf_id) ,"
////					+ "  c.lc_state ,"
////					+ "  c.rohs,"
//					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
//					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
//					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
//					+ " FROM cm.xlp_se_component c ,"
//					+ "  cm.XLP_SE_FAMILY f"
//					+ " WHERE pl_id  = :plId"
//					+ " AND c.FAMILY_ID = f.SERIES_ID";					
//			if(fetVal != null){
//				query += " AND f.SERIES_ID = :valId ";
//			}
//		}else {
			query = "SELECT c.com_id,"
					+ "  cm.get_pl_name(c.pl_id) pl,"
					+ "  c.com_partnum ,"
					+ "  get_man_name(c.MAN_ID) ,"
//					+ "  get_man_code(c.MAN_ID) ,"
					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
					+ "  getPDF_url(c.pdf_id) ,"
//					+ "  c.lc_state ,"
//					+ "  c.rohs,"
					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
					+ " FROM cm.xlp_se_component c ,"
					+ "  cm.DYNAMIC_FLAT d"
					+ " WHERE c.com_id = d.com_id"
					+ " AND c.pl_id    = "+plId;
			if(fetVal != null){
				String[] colNms = fetIdColNm.split(CONCATENATE_FET_CHAR_SEP);
				String[] fetVals = fetValId.split(CONCATENATE_FET_CHAR_SEP);
				for (int i=0; i<colNms.length; i++) {
					query += " AND d."+colNms[i]+" = "+ fetVals[i];
				}			
//				for(int i=colNms.length-1; i>=0; i--){
//					query += " AND d."+colNms[i]+" = :valId";
//				}
			}
//		}
		if(man)
			query += " AND c.man_id = "+vendorId;
		
//		SQLQuery sql = session.createSQLQuery(query);
//		sql.setInteger("plId", plId);
//		if(fetVal!=null){
//			String[] fetVals = fetValId.split(CONCATENATE_FET_CHAR_SEP);
//			for (String valId : fetVals) {
//				sql.setString("valId", valId);
//			}			
//		}if (man)
//			sql.setInteger("manId", vendorId);
		return query;
    }

    @Override
    public String getFetVal() throws NotValidRelatedFeatureException {
    	String finalVals = "";
    	for (String fet: fetNames) {
			String val = CommonFunctions.getFetVal(fet, plId, comId);
			finalVals += val + CONCATENATE_FET_CHAR;
		}
    	finalVals = finalVals.substring(0, finalVals.lastIndexOf(CONCATENATE_FET_CHAR));
    	return finalVals;
    }
}
