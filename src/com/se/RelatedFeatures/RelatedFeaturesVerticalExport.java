package com.se.RelatedFeatures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jdk.internal.jfr.events.FileWriteEvent;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;

public class RelatedFeaturesVerticalExport extends RelatedFeatureExporter {
	
	public static final String SAVING_PATH = "D:\\Parametric_Generation\\";

	public static final String FILE_HEADER = "Product Line\tPRODUCT_NAME\tVendor\tFamily\tDescription\t"
			+ "PRODUCT_EXTERNAL_DATASHEET\tMask\tFamily Cross\tGeneric\tFeature Name\tFeature Value\t"
			+ "Generated Feature Value\tID Feature Name\tID Feature Value\tRelation";
	int plId;
	int manId;
	
	public RelatedFeaturesVerticalExport(int plId, int manId, Session session) {
		super(session);
		this.plId = plId;
		this.manId = manId;
	}
	
	public void exportParts(PrintWriter verticalExport){
		//create file
//				try {
//					verticalExport = new PrintWriter(new FileWriter(new File(SAVING_PATH+"vertical_export.txt")));
					verticalExport.println(FILE_HEADER);
					
					//get rules of the pl
					ArrayList<RelatedFeature> rules = getPLRules(plId, manId);
					for (RelatedFeature rule : rules) {
						ArrayList<ArrayList<String>> parts = getPlParts(plId, manId, rule);
						for (ArrayList<String> row : parts) {
							verticalExport.println(row.get(0)+"\t"+row.get(1)+"\t"+row.get(2)+"\t"+row.get(3)+"\t"+row.get(4)+"\t"+
									row.get(5)+"\t"+row.get(6)+"\t"+row.get(7)+"\t"+row.get(8)+"\t"+
									row.get(9)+"\t"+row.get(10)+"\t"+row.get(11)+"\t"+row.get(12)+"\t"+
									row.get(13)+"\t"+row.get(14));
							verticalExport.flush();
						}
					}
					verticalExport.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
	}
	
	public RelatedFeaturesVerticalExport(ArrayList<ArrayList<String>> parts, Session session) {
		super(session);
	}

	// get parts of pl of specific rule
	public ArrayList<ArrayList<String>> getPlParts(int plId, int manId,
			RelatedFeature row)  {
		ArrayList<ArrayList<String>> partsDataList = new ArrayList<ArrayList<String>>();
		
		boolean man = manId == 0 ? false : true;
		String exportStatement = row.generateExportStatement(man);
		SQLQuery sql = session.createSQLQuery(exportStatement);
//		sql.setInteger("plId", row.plId);
//		if(row.fetVal!=null)
//			sql.setString("valId", row.fetValId);
//		if (man)
//			sql.setInteger("manId", row.vendorId);

//		SQLQuery sql = row.generateExportStatement(man, session);
		
//		System.out.println(row.generateExportStatement(man));
		@SuppressWarnings("unchecked")

		ArrayList<Object[]> result = (ArrayList<Object[]>) sql.list();
		
		int i=0;
		for (Object[] r : result) {
			
			System.out.println(i++);
			if(i==342){
				System.out.println("ss");
			}
			ArrayList<String> rr = new ArrayList<>();
			try{
			row.setComId(Long.parseLong(r[0].toString()));
			String finalFetVal = row.getFetVal();
//			rr.add(r[0] == null ? null : r[0].toString());
			rr.add(r[1] == null ? null : r[1].toString());
			rr.add(r[2] == null ? null : r[2].toString());
			rr.add(r[3] == null ? null : r[3].toString());
			rr.add(r[4] == null ? null : r[4].toString());
			rr.add(r[5] == null ? null : r[5].toString());
			rr.add(r[6] == null ? null : r[6].toString());
			rr.add(r[7] == null ? null : r[7].toString());
			rr.add(r[8] == null ? null : r[8].toString());
			rr.add(r[9] == null ? null : r[9].toString());
//			System.out.println(rr);

			// add updated feature name, value, id fet name, id fet value
			rr.add(row.updatedFetName);
			rr.add(row.getDBUpdatedFetVal());
			rr.add(row.getUpdatedFetValue());
			rr.add(row.fetName);
			rr.add(finalFetVal);
			}catch(Exception ex){
				System.err.println(ex.getMessage());
			}
			//add relation name
			rr.add(row.relation);
			partsDataList.add(rr);
		}
		return partsDataList;
	}

//	public static void main(String[] args){
//		Session session = SessionUtil.getSession();
//			
//		RelatedFeaturesVerticalExport export = new RelatedFeaturesVerticalExport(2515, 0, session);
//		export.exportParts();
//	}
}
