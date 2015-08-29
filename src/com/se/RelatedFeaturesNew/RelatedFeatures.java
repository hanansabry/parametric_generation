package com.se.RelatedFeaturesNew;

import static com.se.common.CommonFunctions.getFamCrossValId;
import static com.se.common.CommonFunctions.getFamilyId;
import static com.se.common.CommonFunctions.getGenValId;
import static com.se.common.CommonFunctions.getMaskValId;
import static com.se.common.CommonFunctions.getParaFeatureId;
import static com.se.common.CommonFunctions.getPlId;
import static com.se.common.CommonFunctions.getPlType;
import static com.se.common.CommonFunctions.getValId;
import static com.se.common.CommonFunctions.getVendorId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.FeatureValueException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.Exceptions.PlException;
import com.se.Exceptions.VendorException;
import com.se.common.CommonFunctions;

public class RelatedFeatures {
	public int plId;
	public int manId;
	public String fetId;
	public String fetColNm;
	public String fetValId;
	public String updatedFetId;
	public String updatedVal;
	
	public static final String[] cmpFetsA = {"mask", "family cross", "generic", "family"};
    public static HashMap<String, String> fetsMapping;
    public static HashMap<String, Integer> notAcceptedFetsMapping;

    public static final String[] notAcceptedFetsA = {"rohs", "life cycle", "pin count", "supplier package", "standard package name", "temperature grade"};
    public static final ArrayList<String> notAcceptedFets = new ArrayList<String>(Arrays.asList(notAcceptedFetsA));
    public static final String CONCATENATE_FET_CHAR = "$";
    public static final String CONCATENATE_FET_CHAR_SEP = "\\$";
	
	public static final String MASK_FET_ID = "Mask";
    public static final String FAM_FET_ID = "Family Cross";
    public static final String GEN_FET_ID = "Generic";
    public static final String FAMILY_FET_ID = "Family";
    public static final String N_A_VAL_ID = "n/a";
    public static final String N_R_VAL_ID = "n/r";
    
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
	
	public RelatedFeatures() {
		// TODO Auto-generated constructor stub
	}
	
	public RelatedFeatures(String plName, String manName, String fetName,String fetVal,
								 String updatedFetName, String updateVal) throws NotValidRelatedFeatureException{
		try {
			validatePlName(plName);
			validateManName(manName);
			validateFetName(fetName);
			validateFetVal(fetVal, fetName);		
			validateUpdateFetName(updatedFetName);
			this.updatedVal = updateVal;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new NotValidRelatedFeatureException(e.getMessage());
		}
		
	}
	
	private static void populateBasicFeatues() {
        fetsMapping = new HashMap<>();
        fetsMapping.put(cmpFetsA[0], MASK_FET_ID);
        fetsMapping.put(cmpFetsA[1], FAM_FET_ID);
        fetsMapping.put(cmpFetsA[2], GEN_FET_ID);
        fetsMapping.put(cmpFetsA[3], FAMILY_FET_ID);
    }
	
	public void validatePlName(String plName) throws PlException{
		plId = getPlId(plName);
	}
	
	public void validateManName(String manName) throws VendorException{
		manId = getVendorId(manName);
	}
	
	public void validateFetName(String fetName) throws FeatureNameException{
		/*1- check if the feature is one of the features not found in SE Feature table
         *{"mask", "family cross", "generic", "family"}
         */
        if (fetsMapping.containsKey(fetName.toLowerCase())) {
            fetId = fetsMapping.get(fetName.toLowerCase()).toString();
        } else {
            Object[] c = getParaFeatureId(fetName, plId);
            fetId = (c[0]==null?-1:Integer.parseInt(c[0].toString()))+"";
			fetColNm= c[1]==null?null:c[1].toString();
        }
	}
	
	public void validateFetVal(String fetVal, String fetName) throws FeatureValueException{
		if (fetVal == null || fetVal.equals("")) {
		
        }else if (fetId.equals(MASK_FET_ID)) {
            fetValId = getMaskValId(fetVal, false)+"";
        } else if (fetId.equals(FAM_FET_ID)) {
            fetValId = getFamCrossValId(fetVal, false)+"";
        } else if (fetId.equals(GEN_FET_ID)) {
            fetValId = getGenValId(fetVal, false)+"";
        } else if (fetId.equals(FAMILY_FET_ID)) {
            fetValId = getFamilyId(fetVal, manId, false)+"";
        } else {
            fetValId = CommonFunctions.getValId(fetVal, fetName, plId, false)+"";
        }
	}
	
	public void validateUpdateFetName(String updatedFetName) throws FeatureNameException{
		/*
         *1- check if the updated feature name can't be updated
         */
        if (notAcceptedFets.contains(updatedFetName.toLowerCase())) {
            throw new FeatureNameException("Reject, Can't update value of the Updated feature");
        } /*
         *2- check if the feature is one of the features not found in SE Feature table
         *{"mask", "family cross", "generic", "family"}
         */ 
        else if (fetsMapping.containsKey(updatedFetName.toLowerCase())) {
            updatedFetId = fetsMapping.get(updatedFetName.toLowerCase());
        } else {
            //get the feature id of the input feature
            Object[] c = getParaFeatureId(updatedFetName, plId);
            updatedFetId = (c[0]==null?-1:Integer.parseInt(c[0].toString()))+"";
        }
	}
	
	public static String generateExportStatement(int plId, int manId,String fetId, String fetColNm, String fetValId, boolean man){
		String query = "";
    	if(fetId.equalsIgnoreCase(MASK_FET_ID)){
			query = "SELECT c.com_id,"
					+ "        cm.get_pl_name(c.pl_id) pl ,"
					+ "        c.com_partnum ,"
					+ "        get_man_name(c.MAN_ID) ,"
					+ "        get_man_code(c.MAN_ID) ,"
					+ "        CM.GET_FAMILY_NAME(c.family_id) ,"
					+ "        REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
					+ "        getPDF_url(c.pdf_id) ,"
					+ "        c.lc_state ,"
					+ "        c.rohs,"
					+ "        IMPORTER.GET_MSTR_MASK(c.com_id),"
					+ "        IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
					+ "        IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
					+ "      FROM cm.xlp_se_component c ,"
					+ "        CM.XLP_SE_COMPONENT_PART2 c2,"
					+ "        cm.TBL_MSTR_MASK m"
					+ "      WHERE c.com_id  = c2.com_id"
					+ "      AND c2.MASK_ID  = m.ID"
					+ "      AND c.pl_id     = "+plId;					
			if(fetValId != null){
				query += "      AND m.ID = "+fetValId;
			}
		}else if(fetId.equals(FAM_FET_ID)){
			query = "SELECT c.com_id,"
					+ "  cm.get_pl_name(c.pl_id) pl ,"
					+ "  c.com_partnum ,"
					+ "  get_man_name(c.MAN_ID) ,"
					+ "  get_man_code(c.MAN_ID) ,"
					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
					+ "  getPDF_url(c.pdf_id) ,"
					+ "  c.lc_state ,"
					+ "  c.rohs,"
					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
					+ " FROM cm.xlp_se_component c ,"
					+ "  cm.tbl_map_fam f"
					+ " WHERE pl_id  ="+plId
					+ " AND c.fam_id = f.FAM_ID";					
			if(fetValId != null){
				query += " AND f.FAM_ID =  "+fetValId;
			}
		}else if(fetId.equalsIgnoreCase(GEN_FET_ID)){
			query = "SELECT c.com_id,"
					+ "  cm.get_pl_name(c.pl_id) pl,"
					+ "  c.com_partnum ,"
					+ "  get_man_name(c.MAN_ID) ,"
					+ "  get_man_code(c.MAN_ID) ,"
					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
					+ "  getPDF_url(c.pdf_id) ,"
					+ "  c.lc_state ,"
					+ "  c.rohs,"
					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
					+ " FROM cm.xlp_se_component c ,"
					+ "   cm.TBL_MAP_GEN g"
					+ " WHERE pl_id   = "+plId
					+ " AND c.GEN_ID  = g.GEN_ID";					
			if(fetValId != null){
				query += " AND g.GEN_ID =  "+fetValId;
			}
		}else if(fetId.equalsIgnoreCase(FAMILY_FET_ID)){
			query = "SELECT c.com_id,"
					+ "  cm.get_pl_name(c.pl_id) pl ,"
					+ "  c.com_partnum ,"
					+ "  get_man_name(c.MAN_ID) ,"
					+ "  get_man_code(c.MAN_ID) ,"
					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
					+ "  getPDF_url(c.pdf_id) ,"
					+ "  c.lc_state ,"
					+ "  c.rohs,"
					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
					+ " FROM cm.xlp_se_component c ,"
					+ "  cm.XLP_SE_FAMILY f"
					+ " WHERE pl_id  = "+plId
					+ " AND c.FAMILY_ID = f.SERIES_ID";					
			if(fetValId != null){
				query += " AND f.SERIES_ID = "+fetValId;
			}
		}else {
			query = "SELECT c.com_id,"
					+ "  cm.get_pl_name(c.pl_id) pl,"
					+ "  c.com_partnum ,"
					+ "  get_man_name(c.MAN_ID) ,"
					+ "  get_man_code(c.MAN_ID) ,"
					+ "  CM.GET_FAMILY_NAME(c.family_id) ,"
					+ "  REPLACE(c.com_desc,CHR(153),'CHAR_153_||') ,"
					+ "  getPDF_url(c.pdf_id) ,"
					+ "  c.lc_state ,"
					+ "  c.rohs,"
					+ "  IMPORTER.GET_MSTR_MASK(c.com_id),"
					+ "  IMPORTER.GET_FAM_BY_COM_ID (c.COM_ID),"
					+ "  IMPORTER.GET_GEN_BY_COM_ID (c.COM_ID)"
					+ " FROM cm.xlp_se_component c ,"
					+ "  cm.DYNAMIC_FLAT d"
					+ " WHERE c.com_id = d.com_id"
					+ " AND c.pl_id    = "+plId;
			if(fetValId != null){
				query += " AND d."+fetColNm+" = "+fetValId;
			}
		}
		if(man)
			query += " AND c.man_id =  "+manId;

		return query;
	}
}
