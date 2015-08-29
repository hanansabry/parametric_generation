package com.se.handlers;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules;

import static com.se.RelatedFeatures.RelatedFeature.*;

public class RelatedFeatureExporterHandler {
	ParaRelatedFetsRules rule;
	Session session;
	public RelatedFeatureExporterHandler(ParaRelatedFetsRules rule, Session session) {
		this.rule = rule;
		this.session = session;
	}
	
	public void getRuleParts(){
		String exportStmt = rule.getExportStmt();
		SQLQuery sql = session.createSQLQuery(exportStmt);
		ArrayList<Object[]> parts = (ArrayList<Object[]>)sql.list();
		for (Object[] partRow : parts) {
			System.out.println(partRow);
//			String comId = 
		}
	}
	
	public static void main(String[] args){
		Session session = SessionUtil.getSession();
		Criteria cr = session.createCriteria(ParaRelatedFetsRules.class);
		cr.add(Restrictions.eq("plId", new BigDecimal(2314)));
		cr.add(Restrictions.eq("manId", new BigDecimal(0)));
		ParaRelatedFetsRules rule = (ParaRelatedFetsRules) cr.uniqueResult();
		
		RelatedFeatureExporterHandler h = new RelatedFeatureExporterHandler(rule, session);
		h.getRuleParts();
	}
}
