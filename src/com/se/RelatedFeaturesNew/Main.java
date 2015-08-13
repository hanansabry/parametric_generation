package com.se.RelatedFeaturesNew;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules2;

public class Main {

	public static void main(String[] args) throws IOException, NotValidRelatedFeatureException{
		BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\2015\\Q2\\Parametric\\Parametric Generation\\samplee.txt")));
		String line = reader.readLine();
		while((line=reader.readLine())!=null){
			String[] cols = line.split("\t");
			String plName = cols[0];
			String manName = cols[1];
			String fetName = cols[2];
			String fetVal = cols[3];
			String uFetName = cols[4];
			String uFetVal = cols[5];
			String relation = cols[6];
			
			RelatedFeatures rule = null;
			if(relation.equals(RelatedFeatures.DIRECT)||relation.equals(RelatedFeatures.RULE_FORMULA)
					||relation.equals(RelatedFeatures.CONSTANT_FORMULA)||relation.equals(RelatedFeatures.RANGE)){
				rule = new RelatedFeatures(plName, manName, fetName, fetVal, uFetName, uFetVal);
			}else if(relation.equals(RelatedFeatures.MULTI_DIRECT)|| relation.equals(RelatedFeatures.CONCATENATION)){
				rule = new MultiRelatedFeatures(plName, manName, fetName, fetVal, uFetName, uFetVal);
			}else{
				System.out.println("Wrong relation");
				continue;
			}
			
			ParaRelatedFetsRules2 ruleRow = new ParaRelatedFetsRules2();
			ruleRow.setPlId(new BigDecimal(rule.plId));
			ruleRow.setManId(new BigDecimal(rule.manId));
			ruleRow.setFetId(rule.fetId);
			ruleRow.setFetValId(rule.fetValId);
			ruleRow.setFetColNm(rule.fetColNm);
			ruleRow.setUpdatedFetId(rule.updatedFetId);
			ruleRow.setUpdatedFetVal(rule.updatedVal);
			ruleRow.setRelation(relation);
			ruleRow.setInsertionDate(new Date());
			
			Session session = SessionUtil.getSession();
			Transaction tx = null;
			tx = session.getTransaction();
			session.save(ruleRow);
			tx.commit();
			session.close();
			
		}
		
	}
}
