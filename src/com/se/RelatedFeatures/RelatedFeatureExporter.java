package com.se.RelatedFeatures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.Exceptions.PartNumberException;
import com.se.Exceptions.VendorException;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules;

public class RelatedFeatureExporter {
	public static final int REJECTED_PARTS_INDX = 0;
	public static final int ACCEPTED_PARTS_INDX = 1;
	Session session;

	public RelatedFeatureExporter(Session session) {
		this.session = session;
	}	

	// get PL Rules
	public ArrayList<RelatedFeature> getPLRules(int plId, int manId) {
		Criteria cr = session.createCriteria(ParaRelatedFetsRules.class);
		if(plId!=0){
			cr.add(Restrictions.eq("plId", new BigDecimal(plId)));
		}
		if(manId == 0){
			cr.add(Restrictions.eq("manId", new BigDecimal(0)));
		}else{
			cr.add(Restrictions.eq("manId", new BigDecimal(manId)));
		}
		@SuppressWarnings("unchecked")
		ArrayList<ParaRelatedFetsRules> rules = (ArrayList<ParaRelatedFetsRules>)cr.list();
		ArrayList<RelatedFeature> rfRules = new ArrayList<RelatedFeature>();
		for (ParaRelatedFetsRules rule : rules) {
			try {
				RelatedFeature row;
				switch (rule.getRelation()){
					case RelatedFeature.DIRECT :
						row = new DirectRelatedFeature(rule.getPlName(), rule.getManName(), rule.getFetName(), rule.getFetVal(), rule.getUpdatedFetName(), rule.getUpdatedFetVal());							
						break;
					case RelatedFeature.MULTI_DIRECT :
						row = new MultiRelatedFeatures(rule.getPlName(), rule.getManName(), rule.getFetName(), rule.getFetVal(), rule.getUpdatedFetName(), rule.getUpdatedFetVal());
						break;
					case RelatedFeature.RANGE :
						row = new RangeRelatedFeature(rule.getPlName(), rule.getManName(), rule.getFetName(), rule.getFetVal(), rule.getUpdatedFetName(), rule.getUpdatedFetVal());
						break;
					case RelatedFeature.CONSTANT_FORMULA :
						row = new FormulaRelatedFeature(rule.getPlName(), rule.getManName(), rule.getFetName(), rule.getFetVal(), rule.getUpdatedFetName(), rule.getUpdatedFetVal());
						break;
					case RelatedFeature.RULE_FORMULA :
						row = new FormulaRuleRelatedFeature(rule.getPlName(), rule.getManName(), rule.getFetName(), rule.getFetVal(), rule.getUpdatedFetName(), rule.getUpdatedFetVal());
						break;
					case RelatedFeature.CONCATENATION :
						row = new ConcatenateRelatedFeature(rule.getPlName(), rule.getManName(), rule.getFetName(), rule.getFetVal(), rule.getUpdatedFetName(), rule.getUpdatedFetVal());
						break;
					default :
						throw new NotValidRelatedFeatureException("Not Valid Relation, Relation must be one of the following : "
								+ "Direct - Multi Direct - Range - Constant Formula - Rule Formula - Concatenation");
				}
				row.relation = rule.getRelation();
				row.plId = rule.getPlId().intValue();
				row.vendorId = rule.getManId().intValue();
				row.fetId = rule.getFetId().longValue();
				row.fetIdColNm = rule.getFetIdColNm();
				row.fetValId = rule.getFetValId();
				row.updatedFetId = rule.getUpdatedFetId().longValue();
				row.updatedFetColNm = rule.getUpdatedFetColNm();
				row.updatedFetValId = rule.getUpdatedFetValId().longValue();
				
//				row.validateFields();
//				System.out.println(row.generateExportStatement(row.vendorId==0?false:true));
				rfRules.add(row);				
			} catch (NotValidRelatedFeatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return rfRules;
	}

	// get parts of pl and vendor
	public ArrayList<Long> getPlParts(int plId, int manId) {
		String query = "SELECT COM_ID FROM CM.XLP_SE_COMPONENT WHERE PL_ID = "
				+ plId + " AND MAN_ID = " + manId;
		SQLQuery sql = session.createSQLQuery(query);
		@SuppressWarnings("unchecked")
		ArrayList<Long> parts = (ArrayList<Long>) sql.list();
		return parts;
	}

	// validate parts and seperated by pl
	public List[] validateParts(ArrayList<ArrayList<String>> inputParts) {
		List[] result = new ArrayList[2];
		ArrayList<ArrayList<String>> rejectParts = new ArrayList<ArrayList<String>>();
		ArrayList<Long> comIds = new ArrayList<Long>();
		for (ArrayList<String> row : inputParts) {
			String pn = row.get(0);
			String vendor = row.get(1);
			try {
				int manId = CommonFunctions.getVendorId(vendor);
				long comId = CommonFunctions.getComId(pn, manId);
				comIds.add(comId);
			} catch (VendorException e) {
				ArrayList<String> rejectPart = new ArrayList<String>();
				rejectPart.add(pn);
				rejectPart.add(vendor);
				rejectPart.add(e.getMessage());
				rejectParts.add(rejectPart);
			} catch (PartNumberException e) {
				ArrayList<String> rejectPart = new ArrayList<String>();
				rejectPart.add(pn);
				rejectPart.add(vendor);
				rejectPart.add(e.getMessage());
				rejectParts.add(rejectPart);
			}
		}
		result[REJECTED_PARTS_INDX] = rejectParts;
		result[ACCEPTED_PARTS_INDX] = comIds;
		return result;
	}

	public static void main(String[] args) throws NotValidRelatedFeatureException {
		Session session = SessionUtil.getSession();
		RelatedFeatureExporter r = new RelatedFeatureExporter(session);
		// ArrayList<ArrayList<String>> parts = new
		// ArrayList<ArrayList<String>>();
		// ArrayList<String> p1 = new ArrayList<String>();
		// p1.add("DR1010-150M-3A-UL-WG3Z0");
		// p1.add("3L Electronic");
		// parts.add(p1);
		//
		// ArrayList<String> p2 = new ArrayList<String>();
		// p2.add("EC38-4R7K-T5A");
		// p2.add("3L Electronic");
		// parts.add(p2);
		//
		// ArrayList<String> p3 = new ArrayList<String>();
		// p3.add("FC0525-5R0K-6A");
		// p3.add("3L Electronic");
		// parts.add(p3);
		//
		// ArrayList<String> p4 = new ArrayList<String>();
		// p4.add("PKH0910-100K");
		// p4.add("3L Electronic");
		// parts.add(p4);
		//
		// ArrayList<String> p5 = new ArrayList<String>();
		// p5.add("PKH0910-100K");
		// p5.add("2L Electronic");
		// parts.add(p5);
		//
		// ArrayList<String> p6 = new ArrayList<String>();
		// p6.add("PKH0910-100AK");
		// p6.add("3L Electronic");
		// parts.add(p6);
		//
		// List[] result = r.validateParts(parts);
		// System.out.println(result[REJECTED_PARTS_INDX].size());
		// System.out.println(result[ACCEPTED_PARTS_INDX].size());

//		ArrayList<RelatedFeature> rfrules = r.getPLRules(2314, 0);
//		for (RelatedFeature rule : rfrules) {
//			r.getPlParts(2314,0, rule);
//		}
		// DR1010-150M-3A-UL-WG3Z0 3L Electronic
		// EC38-4R7K-T5A 3L Electronic
		// FC0525-5R0K-6A 3L Electronic
		// PKH0910-100K 3L Electronic
	}
}
