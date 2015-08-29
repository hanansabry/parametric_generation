/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.common;

import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.se.Exceptions.*;
import com.se.RelatedFeaturesNew.RelatedFeatures;
import com.se.connection.SessionUtil;

/**
 * 
 * @author Hanan_Sabry
 */
public class CommonFunctions {
	static Session session;
	static {
		session = SessionUtil.getSession();
	}

	public static long getComId(String pn, int manId) throws PartNumberException{
		try {
			SQLQuery aa = session.createSQLQuery("SELECT COM_ID FROM CM.XLP_SE_COMPONENT "
					+ " WHERE MAN_ID = "+manId+" AND COM_PARTNUM = '"+pn+"'");
			long comId = Long.parseLong(aa.uniqueResult().toString());
			return comId;
		} catch (Exception ex) {
			throw new PartNumberException();
		}
	}
	
	public static int getPlId(String plName) throws PlException {
		if (plName == null) {
			throw new PlException();
		} else {
			try {
				SQLQuery aa = session.createSQLQuery("select pl_Id from cm.xlp_se_pl where lower(pl_name) = lower('"+ plName + "')");
				int plId = Integer.parseInt(aa.uniqueResult().toString());
				return plId;
			} catch (Exception ex) {
				throw new PlException();
			}
		}
	}

	public static int getVendorId(String vendorName) throws VendorException {
		if (vendorName == null || vendorName.equals("")) {
			return 0;
		} else {
			try{
				SQLQuery aa = session.createSQLQuery("select man_id from CM.XLP_SE_MANUFACTURER where MAN_NAME = '"+ vendorName + "'");
				int vendorId = Integer.parseInt(aa.uniqueResult().toString());
				return vendorId;
			}catch(Exception ex){
				throw new VendorException();
			}
		}
	}

	public static Object[] getParaFeatureId(String fetName, int plId)
			throws FeatureNameException {
		if (fetName == null) {
			throw new FeatureNameException("Not Valid SE Feature");
		}else{
			try{
//				SELECT * FROM CM.XLP_SE_FEATURE WHERE FET_NAME = 'Logic Family' AND pl_id = 2314
				SQLQuery aa = session.createSQLQuery("SELECT FET_ID, COL_NM FROM CM.XLP_SE_FEATURE WHERE FET_NAME = '"+fetName+"' AND pl_id = "+plId);
				Object[] c = (Object[])aa.uniqueResult();
//				long fetId = Integer.parseInt(c[0].toString());
//				String colnm = c[1].toString();
				return c;
			}catch(Exception ex){
				ex.printStackTrace();
				throw new FeatureNameException("Not Valid SE Feature");
//				try{
//					int fetId = getPkgFeatureId(fetName);
//					return fetId;
//				}catch(Exception e){
//					throw new FeatureNameException("Error While validating Feature Name");
//				}
			}
		}
	}

	public static int getPkgFeatureId(String fetName)
			throws FeatureNameException {
		if (fetName == null) {
			throw new FeatureNameException("There is not feature");
		} else {
			try{
				SQLQuery aa = session.createSQLQuery("SELECT fet_id FROM CM.TBL_PKG_FEATURES WHERE FET_NAME = '"+fetName+"'");
				int fetId = Integer.parseInt(aa.uniqueResult().toString());
				return fetId;
			}catch(Exception ex){
				throw new FeatureNameException("Error While validating Feature Name");
			}
		}
	}

	public static int getPlType(int plid) throws PlException {
		try{
			SQLQuery aa = session.createSQLQuery("SELECT pl_type FROM cm.xlp_se_pl WHERE pl_id = "+plid);
			int plType = Integer.parseInt(aa.uniqueResult().toString());
			return plType;
		}catch(Exception ex){
			throw new PlException();
		}
	}

	public static int getValId(String fetVal, String fetName, int plId, boolean updated) throws FeatureValueException {
		try{
			SQLQuery aa = session.createSQLQuery("select cm.GET_FINAL_ID(" + plId + ", '"+ fetName + "', '" + fetVal + "') from dual");
			int fetValId = Integer.parseInt(aa.uniqueResult().toString());
			if(fetValId == 0 && !updated){
				throw new FeatureValueException("Reject, Feature Value is not Found");
			}else if (fetValId == 0 && updated){
				throw new ApprovedValueException();
			}
			return fetValId;
		}catch(Exception ex){
			throw new FeatureValueException("Reject, Feature Value is not Found");
		}
	}
	
	public static int getMaskValId(String fetVal, boolean updated) throws FeatureValueException {
		try{
			SQLQuery aa = session.createSQLQuery("select cm.GET_MSK_MSTR_ID('" + fetVal+ "') from dual");
			int fetValId = Integer.parseInt(aa.uniqueResult().toString());
			if(fetValId == 0 && !updated){
				throw new FeatureValueException("Reject, Feature Value is not Found");
			}else if (fetValId == 0 && updated){
				throw new ApprovedValueException();
			}
			return fetValId;
		}catch(Exception ex){
			throw new FeatureValueException("Reject, Feature Value is not Found");
		}
	}

	public static int getFamCrossValId(String fetVal, boolean updated) throws FeatureValueException {
		try{
			SQLQuery aa = session.createSQLQuery("select cm.GET_FAM_ID_Fam('" + fetVal + "') from dual");
			int fetValId = Integer.parseInt(aa.uniqueResult().toString());
			if(fetValId == 0 && !updated){
				throw new FeatureValueException("Reject, Feature Value is not Found");
			}else if (fetValId == 0 && updated){
				throw new ApprovedValueException();
			}
			return fetValId;
		}catch(Exception ex){
			throw new FeatureValueException("Reject, Feature Value is not Found");
		}
	}

	public static int getGenValId(String fetVal, boolean updated) throws FeatureValueException {
		try{
			SQLQuery aa = session.createSQLQuery("select importer.get_generic_id('" + fetVal + "') from dual");
			int fetValId = Integer.parseInt(aa.uniqueResult().toString());
			if(fetValId == 0 && !updated){
				throw new FeatureValueException("Reject, Feature Value is not Found");
			}else if (fetValId == 0 && updated){
				throw new ApprovedValueException();
			}
			return fetValId;
		}catch(Exception ex){
			throw new FeatureValueException("Reject, Feature Value is not Found");
		}
	}

	public static int getFamilyId(String fetVal, int vendorId, boolean updated) throws FeatureValueException {
		try{
			SQLQuery aa = session.createSQLQuery("select CM.GET_SERIES_ID('"+fetVal+"', "+vendorId+") from dual");
			int fetValId = Integer.parseInt(aa.uniqueResult().toString());
			if(fetValId == 0 && !updated){
				throw new FeatureValueException("Reject, Feature Value is not Found");
			}else if (fetValId == 0 && updated){
				throw new ApprovedValueException();
			}
			return fetValId;
		}catch(Exception ex){
			throw new FeatureValueException("Reject, Feature Value is not Found");
		}
	}

	public static String getFetVal(String fetName, int plId, long comId) throws NotValidRelatedFeatureException{
		String query = "";
		try{
			if (fetName.equalsIgnoreCase("Mask")) {
				query  = "SELECT CM.GET_MSK_PART_BY_MSK_ID(GET_MSK_ID_BY_COM_ID("+comId+")) FROM DUAL";
			} else if (fetName.equalsIgnoreCase("Family Cross")) {
				query = "SELECT CM.GET_FAM_NAME(CM.GET_FAMILY_ID("+comId+")) FROM DUAL";
			} else if (fetName.equalsIgnoreCase("Generic")) {
				query = "SELECT IMPORTER.GET_GEN_BY_COM_ID("+comId+") FROM DUAL";
			} else if (fetName.equalsIgnoreCase("family")){
				query = "SELECT CM.GET_FAMILY_NAME(CM.GET_FAMILY_ID("+comId+")) FROM DUAL";
			} else{
				query = "SELECT CM.GET_FET_VALUE("+plId+", CM.GET_FET_ID("+plId+", '"+fetName+"'), "+comId+")FROM DUAL";
			}
			
//			System.out.println(query);
			SQLQuery aa = session.createSQLQuery(query);
			String value = aa.uniqueResult()==null?null:aa.uniqueResult().toString();
			return value;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new NotValidRelatedFeatureException("Can't Get Feature Value");
		}
	}
	
	public static String getFetVal(String fetName, String plName, long comId) throws NotValidRelatedFeatureException{
 		String query = "";
		try{
			if (fetName.equalsIgnoreCase("Mask")) {
				query  = "SELECT CM.GET_MSK_PART_BY_MSK_ID(GET_MSK_ID_BY_COM_ID("+comId+")) FROM DUAL";
			} else if (fetName.equalsIgnoreCase("Family Cross")) {
				query = "SELECT CM.GET_FAM_NAME(CM.GET_FAMILY_ID("+comId+")) FROM DUAL";
			} else if (fetName.equalsIgnoreCase("Generic")) {
				query = "SELECT IMPORTER.GET_GEN_BY_COM_ID("+comId+") FROM DUAL";
			} else if (fetName.equalsIgnoreCase("family")){
				query = "SELECT CM.GET_FAMILY_NAME(CM.GET_FAMILY_ID("+comId+")) FROM DUAL";
			} else{
				query = "SELECT CM.GET_FET_VALUE(CM.GET_PL_ID('"+plName+"'), "
						+ "CM.GET_FET_ID(CM.GET_PL_ID('"+plName+"'), '"+fetName+"'), "+comId+")FROM DUAL";
			}
			
//			System.out.println(query);
			SQLQuery aa = session.createSQLQuery(query);
			String value = aa.uniqueResult()==null?null:aa.uniqueResult().toString();
			return value;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new NotValidRelatedFeatureException("Can't Get Feature Value");
		}
	}
	
	public static String getFeatureName(long fetId) throws FeatureNameException{
//		GET_FET_NAME
		try{
			SQLQuery aa = session.createSQLQuery("SELECT CM.GET_FET_NAME("+fetId+") from dual");
			String fetName = aa.uniqueResult().toString();
			return fetName;
		}catch(Exception ex){
			throw new FeatureNameException("Feature not found");
		}
	}
	
	public static String getFeatureValueByID(String fetValId) throws FeatureValueException{
//		GET_FET_NAME
		try{
			SQLQuery aa = session.createSQLQuery("SELECT CM.GET_FETVAL("+fetValId+") from dual");
			String fetName = aa.uniqueResult().toString();
			return fetName;
		}catch(Exception ex){
			throw new FeatureValueException();
		}
	}
	
	public static String getFeatureName(String fetId) throws FeatureNameException{
//		GET_FET_NAME
		try{
			if(fetId.equalsIgnoreCase(RelatedFeatures.MASK_FET_ID)){
				return RelatedFeatures.MASK_FET_ID;
			}else if(fetId.equalsIgnoreCase(RelatedFeatures.FAM_FET_ID)){
				return RelatedFeatures.FAM_FET_ID;
			}else if(fetId.equalsIgnoreCase(RelatedFeatures.FAMILY_FET_ID)){
				return RelatedFeatures.FAMILY_FET_ID;
			}else if(fetId.equalsIgnoreCase(RelatedFeatures.GEN_FET_ID)){
				return RelatedFeatures.GEN_FET_ID;
			}
			SQLQuery aa = session.createSQLQuery("SELECT CM.GET_FET_NAME("+fetId+") from dual");
			String fetName = aa.uniqueResult().toString();
			return fetName;
		}catch(Exception ex){
			throw new FeatureNameException("Feature not found");
		}
	}
	
	public static String getPlName(int plId) throws PlException{
		try {
			SQLQuery aa = session.createSQLQuery("select pl_name from cm.xlp_se_pl where pl_id = "+plId);
			String plName = aa.uniqueResult().toString();
			return plName;
		} catch (Exception ex) {
			throw new PlException();
		}
	}
	
	public static String getManName(int manId) throws VendorException{
		if(manId==0){
			return "";
		}
		try {
			SQLQuery aa = session.createSQLQuery("select man_name from cm.XLP_SE_MANUFACTURER where man_id = "+manId);
			String manName = aa.uniqueResult().toString();
			return manName;
		} catch (Exception ex) {
			throw new VendorException();
		}
	}
	
	public static String getFetValById(String fetName, String fetValid) throws NotValidRelatedFeatureException{
		String query = "";
		try{
			if (fetName.equalsIgnoreCase("Mask")) {
				query  = "SELECT CM.GET_MSK_PART_BY_MSK_ID("+fetValid+") FROM DUAL";
			} else if (fetName.equalsIgnoreCase("Family Cross")) {
				query = "SELECT CM.GET_FAM_NAME("+fetValid+") FROM DUAL";
			} else if (fetName.equalsIgnoreCase("Generic")) {
				query = "SELECT IMPORTER.GET_GEN_BY_GEN_ID("+fetValid+") FROM DUAL";
			} else if (fetName.equalsIgnoreCase("family")){
				query = "SELECT CM.GET_FAMILY_NAME("+fetValid+") FROM DUAL";
			} else{
				query = "SELECT CM.GET_FETVAL("+fetValid+")FROM DUAL";
			}
			
//			System.out.println(query);
			SQLQuery aa = session.createSQLQuery(query);
			String value = aa.uniqueResult()==null?null:aa.uniqueResult().toString();
			return value;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new NotValidRelatedFeatureException("Can't Get Feature Value");
		}		
	}
	
	public static void main(String[] args) throws NotValidRelatedFeatureException{
		getFetVal("Mask", 2515, 30556064);
	}
	
	public static ArrayList<String> getPlFeatures(int plId, Session session) {
        SQLQuery cr = session.createSQLQuery("SELECT fet_name FROM CM.XLP_SE_FEATURE WHERE lower(fet_name)"
                + " not in('pin count','supplier package','product_url','family','life cycle','rohs','standard package name','vendor') "
                + " and pl_id= " + plId + "and COL_NM is not null order by FET_EXPERTSHEETORDER asc");

        @SuppressWarnings("unchecked")
		ArrayList<String> fets = (ArrayList<String>) cr.list();
        return fets;
    }
}
