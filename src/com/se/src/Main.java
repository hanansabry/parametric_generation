/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.src;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

import com.se.Exceptions.*;
import com.se.RelatedFeatures.*;
import com.se.connection.HibernateUtil;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules;

/**
 *
 * @author Hanan_Sabry
 */
public class Main {

    public static void main(String[] args) {
//        try {
//            RelatedFeature[] objs = {new DirectRelatedFeature("Gates", null, "Logic Family", "ACT", "Fabrication Technology", "CMOS"),
//                new MultiRelatedFeatures("Op Amp", "Linear Technology", "Minimum Dual Supply Voltage$Maximum Dual Supply Voltage", "N/R$N/R", "Power Supply Type", "Single"),
//                new RangeRelatedFeature("Controlled Oscillator", null, "Upper Frequency", "3|20", "Upper Frequency Range", "<5@5to10@10 to 50@50 to 100@>100"),
//                new FormulaRelatedFeature("DRAM Chip", "Toshiba", "Density", null, "Density in Bits", "* 1024"),
//                new FormulaRuleRelatedFeature("DRAM Chip", "Toshiba", "Density", null, "Density in Bits", "* M"),
//                new ConcatenateRelatedFeature("Analog Switch Multiplexer", null, "Number of Channels per Chip$Switch Architecture", "N/A$val1", "Configuration", "|")};
//            for (RelatedFeature obj : objs) {
//                System.out.println("-----------------");
//                System.out.println("Id fet name = " + obj.fetName + ", id fet val = " + obj.fetVal);
//                System.out.println("updted fet name = " + obj.updatedFetName + ", updted fet val = " + obj.getUpdatedFetValue());
//                System.out.println(obj.fetVal);
//            }
//        } catch (NotValidRelatedFeatureException ex) {
//            ex.printStackTrace();
//        }
    	try {
//    		Session session = SessionUtil.getSession();
//    		Transaction tx = null;
    		ArrayList<RelatedFeature> rows = new ArrayList<>();
			BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\2015\\Q2\\Parametric\\Parametric Generation\\samplee.txt")));
			String line = reader.readLine();
			int i=0;
			while((line = reader.readLine())!= null){
				System.out.println(i++);
				try{
					Session session = SessionUtil.getSession();
		    		Transaction tx = null;
	//				System.out.println(line);
					String[] cols = line.split("\t");
					String plName = cols[0];
					String vendorName = cols[1]==null?null:cols[1].equals("")?null:cols[1];
					String idFetName = cols[2];
					String idFetVal = cols[3]==null?null:cols[3].equals("")?null:cols[3];
					String updatedFetName = cols[4];
					String updatedFetVal = cols[5];
					String relation = cols[6];
					
					RelatedFeature row ;					
					switch (relation){
						case RelatedFeature.DIRECT :
							row = new DirectRelatedFeature(plName, vendorName, idFetName, idFetVal, updatedFetName, updatedFetVal);							
							break;
						case RelatedFeature.MULTI_DIRECT :
							row = new MultiRelatedFeatures(plName, vendorName, idFetName, idFetVal, updatedFetName, updatedFetVal);
							break;
						case RelatedFeature.RANGE :
							row = new RangeRelatedFeature(plName, vendorName, idFetName, idFetVal, updatedFetName, updatedFetVal);
							break;
						case RelatedFeature.CONSTANT_FORMULA :
							row = new FormulaRelatedFeature(plName, vendorName, idFetName, idFetVal, updatedFetName, updatedFetVal);
							break;
						case RelatedFeature.RULE_FORMULA :
							row = new FormulaRuleRelatedFeature(plName, vendorName, idFetName, idFetVal, updatedFetName, updatedFetVal);
							break;
						case RelatedFeature.CONCATENATION :
							row = new ConcatenateRelatedFeature(plName, vendorName, idFetName, idFetVal, updatedFetName, updatedFetVal);
							break;
						default :
							throw new NotValidRelatedFeatureException("Not Valid Relation, Relation must be one of the following : "
									+ "Direct - Multi Direct - Range - Constant Formula - Rule Formula - Concatenation");
					}
					row.validateFields();
//					row.setComId(32986803);
					ParaRelatedFetsRules r = new ParaRelatedFetsRules();
					r.setPlId(new BigDecimal(row.plId));
					r.setPlName(row.plName);
					r.setManId(new BigDecimal(row.vendorId));
					r.setManName(row.vendorName);
					r.setFetName(row.fetName);
					r.setFetId(new BigDecimal(row.fetId));
					r.setFetIdColNm(row.fetIdColNm);
					r.setFetVal(row.fetVal);
					r.setFetValId(row.fetValId);
					r.setUpdatedFetName(row.updatedFetName);
					r.setUpdatedFetColNm(row.updatedFetColNm);
					r.setUpdatedFetId(new BigDecimal(row.updatedFetId));
					r.setUpdatedFetName(row.updatedFetName);
					r.setUpdatedFetVal(row.updatedFetVal);
					r.setUpdatedFetValId(new BigDecimal(row.updatedFetValId));
					r.setRelation(relation);
					r.setInsertionDate(new Date());
					r.setExportStmt(row.generateExportStatement(row.vendorId==0?false:true));
					
					try{
						tx = session.getTransaction();
						session.save(r);
						tx.commit();
					}catch(TransactionException ex){
						tx.rollback();
					}
					
					
//					System.err.println("------------"+relation+"---------------");
//		            System.out.println("Id fet name = " + row.fetName + ", id fet val = " + row.fetVal);
//		            System.out.println("updted fet name = " + row.updatedFetName + ", updted fet val = " + row.getUpdatedFetValue());
				}catch (NotValidRelatedFeatureException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	Session session = SessionUtil.getSession();
//    	SQLQuery aa = session.createSQLQuery("select pl_Id from cm.xlp_se_pl where pl_name = 'ADC'");
//    	System.out.println(aa.uniqueResult().toString());
    }
}
