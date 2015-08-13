/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.RelatedFeatures;

import com.se.Exceptions.*;
import com.se.common.CommonFunctions;

import static com.se.common.CommonFunctions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 *
 * @author Hanan_Sabry
 */
public class RelatedFeature implements RelatedFeatureValidation {

	public long comId;
    public String plName;
    public int plId;
    public String vendorName;
    public int vendorId;
    public String fetName;
    public long fetId;
    public String fetIdColNm;
    public String fetVal;
    public String fetValId;
    public String updatedFetName;
    public String updatedFetColNm;
    public long updatedFetId;
    public String updatedFetVal;
    public long updatedFetValId;
    public String relation;

    public static final String[] cmpFetsA = {"mask", "family cross", "generic", "family"};
    public static HashMap<String, Integer> fetsMapping;
    public static HashMap<String, Integer> notAcceptedFetsMapping;

    public static final String[] notAcceptedFetsA = {"rohs", "life cycle", "pin count", "supplier package", "standard package name", "temperature grade"};
    public static final ArrayList<String> notAcceptedFets = new ArrayList<String>(Arrays.asList(notAcceptedFetsA));
    public static final String CONCATENATE_FET_CHAR = "$";
    public static final String CONCATENATE_FET_CHAR_SEP = "\\$";

    public static final int MASK_FET_ID = 0;
    public static final int FAM_FET_ID = 1;
    public static final int GEN_FET_ID = 2;
    public static final int FAMILY_FET_ID = 3;
    public static final int N_A_VAL_ID = -1;
    public static final int N_R_VAL_ID = -2;
    
    public static final String N_A_VAL = "N/A";
    public static final String N_R_VAL = "N/R";
    
    public static final String DIRECT = "Direct";
	public static final String MULTI_DIRECT = "Multi Direct";
	public static final String RANGE = "Range";
	public static final String CONSTANT_FORMULA = "Constant Formula";
	public static final String RULE_FORMULA = "Rule Formula";
	public static final String CONCATENATION = "Concatenation";

    static {
        populateBasicFeatues();
    }   

    public RelatedFeature(String plName, String vendorName, String fetNames, String fetVals, String updatedFetName, String updatedFetVal) throws NotValidRelatedFeatureException {
        this.plName = plName;
        this.vendorName = vendorName;
        this.fetName = fetNames;
        this.fetVal = fetVals;
        this.updatedFetName = updatedFetName;
        this.updatedFetVal = updatedFetVal;
//        validateFields();
    }

    private static void populateBasicFeatues() {
        fetsMapping = new HashMap<>();
        fetsMapping.put(cmpFetsA[0], MASK_FET_ID);
        fetsMapping.put(cmpFetsA[1], FAM_FET_ID);
        fetsMapping.put(cmpFetsA[2], GEN_FET_ID);
        fetsMapping.put(cmpFetsA[3], FAMILY_FET_ID);
    }
    
    public void validateFields() throws NotValidRelatedFeatureException{
        try {
            validatePlName();
            validateVendorName();
            validateIdFeatureName();
            validateIdFeatureValue();
            validateUpdatedFeatureName();
            validateUpdatedFeatureValue();
        } catch (FeatureNameException | FeatureValueException | PlException | VendorException ex) {
            throw new NotValidRelatedFeatureException(ex.getMessage());
        }
    }

    public String getUpdatedFetValue() throws NotValidRelatedFeatureException{
        return updatedFetVal;
    }

    public String generateExportStatement(boolean man){
    	String query = "";
    	if(fetId==MASK_FET_ID){
			query = "SELECT c.com_id,"
					+ "        cm.get_pl_name(c.pl_id) pl ,"
					+ "        c.com_partnum ,"
					+ "        get_man_name(c.MAN_ID) ,"
//					+ "        get_man_code(c.MAN_ID) ,"
					+ "        CM.GET_FAMILY_NAME(c.family_id) ,"
					+ "        REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
					+ "        getPDF_url(c.pdf_id) ,"
//					+ "        c.lc_state ,"
//					+ "        c.rohs,"
					+ "        IMPORTER.GET_MSTR_MASK(c.com_id),"
					+ "        IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
					+ "        IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
					+ "      FROM cm.xlp_se_component c ,"
					+ "        CM.XLP_SE_COMPONENT_PART2 c2,"
					+ "        cm.TBL_MSTR_MASK m"
					+ "      WHERE c.com_id  = c2.com_id"
					+ "      AND c2.MASK_ID  = m.ID"
					+ "      AND c.pl_id     = "+plId;					
			if(fetVal != null){
				query += "      AND m.ID = "+fetValId;
			}
		}else if(fetId == FAM_FET_ID){
			query = "SELECT c.com_id,"
					+ "  cm.get_pl_name(c.pl_id) pl ,"
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
					+ "  cm.tbl_map_fam f"
					+ " WHERE pl_id  ="+plId
					+ " AND c.fam_id = f.FAM_ID";					
			if(fetVal != null){
				query += " AND f.FAM_ID =  "+fetValId;
			}
		}else if(fetId == GEN_FET_ID){
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
					+ "   cm.TBL_MAP_GEN g"
					+ " WHERE pl_id   = "+plId
					+ " AND c.GEN_ID  = g.GEN_ID";					
			if(fetVal != null){
				query += " AND g.GEN_ID =  "+fetValId;
			}
		}else if(fetId == FAMILY_FET_ID){
			query = "SELECT c.com_id,"
					+ "  cm.get_pl_name(c.pl_id) pl ,"
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
					+ "  cm.XLP_SE_FAMILY f"
					+ " WHERE pl_id  = "+plId
					+ " AND c.FAMILY_ID = f.SERIES_ID";					
			if(fetVal != null){
				query += " AND f.SERIES_ID = "+fetValId;
			}
		}else {
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
				query += " AND d."+fetIdColNm+" = "+fetValId;
			}
		}
		if(man)
			query += " AND c.man_id =  "+vendorId;
		
		
//		SQLQuery sql = session.createSQLQuery(query);
//		sql.setInteger("plId", plId);
//		if(fetVal!=null)
//			sql.setString("valId", fetValId);
//		if (man)
//			sql.setInteger("manId", vendorId);
		return query;
    }

    public String getFetVal() throws NotValidRelatedFeatureException{
//    	if(fetVal==null){
    		fetVal = CommonFunctions.getFetVal(fetName, plId, comId);
//    	}
    	return fetVal;
    }
    
    public String getDBUpdatedFetVal() throws NotValidRelatedFeatureException{
    	return CommonFunctions.getFetVal(updatedFetName, plId, comId);    	
    }

    @Override
    public void validatePlName() throws PlException {
        plId = getPlId(plName);
    }

    @Override
    public void validateVendorName() throws VendorException {
        vendorId = getVendorId(vendorName);
    }

    @Override
    public void validateIdFeatureName() throws FeatureNameException, PlException {
        /*1- check if the feature is one of the features not found in SE Feature table
         *{"mask", "family cross", "generic", "family"}
         */
        if (fetsMapping.containsKey(fetName.toLowerCase())) {
            fetId = fetsMapping.get(fetName.toLowerCase());
        } else {
            //get plType to check if we will search for the feature in xlp_se_feature table or package table
            int plType = getPlType(plId);
            //if plType = 2 >> Pemco, xlp_se_feature, dynamic table
//            if (plType == 2) {
                //get the feature id of the input feature
                Object[] c = getParaFeatureId(fetName, plId);
                fetId = c[0]==null?-1:Integer.parseInt(c[0].toString());
				fetIdColNm= c[1]==null?null:c[1].toString();
//            } //if plType = 1 >> PKG, tbl_pkg_features , tbl_main_pkg_table
//            else if (plType == 1) {
                //get the feature id of the input feature
//                fetId = getPkgFeatureId(fetName);
//            }
        }
    }

    @Override
    public void validateIdFeatureValue() throws NotValidRelatedFeatureException {
        if (fetVal == null) {
            //get current value from db
//            fetValId = 1234;
//            fetVal = 1234+"";
        	fetValId = 0+"";
        	fetVal = null;
        } 
//        else if(fetVal.equalsIgnoreCase(N_A_VAL)){
//        	fetVal = N_A_VAL;
//        	fetValId = N_A_VAL_ID+"";
//        } 
//        else if(fetVal.equalsIgnoreCase(N_R_VAL)){
//        	fetVal = N_R_VAL;
//        	fetValId = N_R_VAL_ID+"";
//        } 
        else if (fetId == MASK_FET_ID) {
            fetValId = getMaskValId(fetVal, false)+"";
        } else if (fetId == FAM_FET_ID) {
            fetValId = getFamCrossValId(fetVal, false)+"";
        } else if (fetId == GEN_FET_ID) {
            fetValId = getGenValId(fetVal, false)+"";
        } else if (fetId == FAMILY_FET_ID) {
            fetValId = getFamilyId(fetVal, vendorId, false)+"";
        } else {
            fetValId = getValId(fetVal, fetName, plId, false)+"";
        }
    }

    @Override
    public void validateUpdatedFeatureName() throws FeatureNameException {
        /*
         *1- check if the updated feature name can't be updated
         */
        if (notAcceptedFets.contains(updatedFetName.toLowerCase())) {
            throw new FeatureNameException("Reject, Can't update value of the Updated feature");
        } /*
         *2- check if the feature is one of the features not found in SE Feature table
         *{"mask", "family cross", "generic", "family"}
         */ else if (fetsMapping.containsKey(updatedFetName.toLowerCase())) {
            updatedFetId = fetsMapping.get(updatedFetName.toLowerCase());
        } else {
            //get the feature id of the input feature
            Object[] c = getParaFeatureId(updatedFetName, plId);
            updatedFetId = c[0]==null?-1:Integer.parseInt(c[0].toString());
			updatedFetColNm= c[1]==null?null:c[1].toString();
        }
    }

    @Override
    public void validateUpdatedFeatureValue() throws FeatureValueException, ApprovedValueException {
        /*
         *if the feature is one of those {"mask", "family cross", "generic", "family"}
         *we can't update their values to n/r || n/a
         */
        if (fetsMapping.containsKey(updatedFetName.toLowerCase()) && (updatedFetVal.equalsIgnoreCase("n/a")
                || updatedFetVal.equalsIgnoreCase("n/r"))) {
            throw new FeatureValueException("Reject, Can't update this feature to N/R");
        } else {
            if (updatedFetVal == null) {
                throw new FeatureValueException("Reject, Updated Feature Value is null");
            } 
            
//            else if (fetId == MASK_FET_ID) {
//                updatedFetValId = getMaskValId(updatedFetVal, true);
//            } else if (fetId == FAM_FET_ID) {
//                updatedFetValId = getFamCrossValId(updatedFetVal, true);
//            } else if (fetId == GEN_FET_ID) {
//                updatedFetValId = getGenValId(updatedFetVal, true);
//            } else if (fetId == FAMILY_FET_ID) {
//                updatedFetValId = getFamilyId(updatedFetVal, vendorId, true);
//            } else {
//                updatedFetValId = getValId(updatedFetVal, updatedFetName, plId, true);
//            }
        }
    }
    
    public void setComId(long comId) throws NotValidRelatedFeatureException{
    	this.comId = comId;
//    	if(fetVal==null){
//    		fetVal = getFetVal(fetName, plId, comId);
//    	}
//    	if(updatedfet)
    }
}
