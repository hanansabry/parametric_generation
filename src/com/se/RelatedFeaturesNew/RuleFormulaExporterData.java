package com.se.RelatedFeaturesNew;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.se.Exceptions.FormulaException;
import com.se.Exceptions.PlException;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;
import com.se.pojos.MathRuleRelatedFets;

public class RuleFormulaExporterData extends FormulaExporterData{
	public static final HashMap<String, Integer> rules = new HashMap();
	
//	static {
//        //get rules from database		
//        rules.put("M", 1024);
//        rules.put("G", 2048);
//        rules.put("L", 512);                       
//    }
	
//	public RuleFormulaExporterData(String plName, Session session) {
//		this.plName = plName;
//		setRules(session);
//	}
	
//	
//	{
//		////////////////////////////////////////////////////
//		Session session = SessionUtil.getSession();
//		Criteria cr = session.createCriteria(MathRuleRelatedFets.class);
//		try {
//			int plId = CommonFunctions.getPlId(plName);
//			cr.add(Restrictions.eq("plId", plId));
//			ArrayList<Object[]> result = (ArrayList<Object[]>)cr.list();
//			for (Object[] row : result) {
//				String prefix = row[2].toString();
//				Integer value = Integer.parseInt(row[3].toString());
//				
//				rules.put(prefix, value);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public int getMultipleValue(String updatedFetVal) throws FormulaException{
		String formulaValue_ = "";
		if(updatedFetVal.contains(MULTIPLY)){
			formulaValue_ = updatedFetVal.split("\\" + MULTIPLY)[1].trim();
		}else if(updatedFetVal.contains(DIVIDE)){
			formulaValue_ = updatedFetVal.split("\\" + DIVIDE)[1].trim();
		}else{
			throw new FormulaException();
		}				
		return getFormulaValue(formulaValue_);		
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
	
//	public void setRules(Session session){
//		
//		Criteria cr = session.createCriteria(MathRuleRelatedFets.class);
//		try {
//			int plId = CommonFunctions.getPlId(plName);
//			cr.add(Restrictions.eq("plId", new BigDecimal(plId)));
//			ArrayList<MathRuleRelatedFets> result = (ArrayList<MathRuleRelatedFets>)cr.list();
//			for (MathRuleRelatedFets rule : result) {
//				String prefix = rule.getPrefix();
//				Integer value = rule.getValue().intValue();
//				rules.put(prefix, value);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
