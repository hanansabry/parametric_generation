package com.se.RelatedFeaturesNew;

import static com.se.RelatedFeaturesNew.RelatedFeatures.CONSTANT_FORMULA;
import static com.se.RelatedFeaturesNew.RelatedFeatures.DIRECT;
import static com.se.RelatedFeaturesNew.RelatedFeatures.RANGE;
import static com.se.RelatedFeaturesNew.RelatedFeatures.RULE_FORMULA;
import static com.se.RelatedFeaturesNew.RelatedFeatures.generateExportStatement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules2;

public class RelatedFeaturesCPExporter extends RelatedFeaturesExporter{

	public String FILE_HEADER = "Com_ID\tPRODUCT_NAME\tVendor Code\tVendor\tFamily\tDescription\tPRODUCT_EXTERNAL_DATASHEET\tLife Cycle\tROHS\tMask\tFamily Cross\tGeneric\tPin Count\tSupplier Package\tPRODUCT_URL\t";
	
//	public static String FILE_HEADER = "PProduct Line\tPRODUCT_NAME\tVendor\tFamily\tDescription\t"
//			+ "PRODUCT_EXTERNAL_DATASHEET\tMask\tFamily Cross\tGeneric\tPin Count\tSupplier Package\tPRODUCT_URL\t";
	
	public RelatedFeaturesCPExporter(int plId, int manId,
			PrintWriter verticalExport, Session session) {
		super(plId, manId, verticalExport, session);
	}	
	//	public HashMap<BigDecimal, ArrayList<ParaRelatedFetsRules2>> getRules() throws FeatureNameException{
//		HashSet<String> plAffectedFets = new HashSet<String>();
//		Criteria cr = session.createCriteria(ParaRelatedFetsRules2.class);
//		if(plId!=0){
//			cr.add(Restrictions.eq("plId", new BigDecimal(plId)));
//		}
//		if(manId==0){
//			cr.add(Restrictions.eq("manId", new BigDecimal(0)));
//		}else{
//			cr.add(Restrictions.eq("manId", new BigDecimal(manId)));
//		}
//		
//		@SuppressWarnings("unchecked")
//		ArrayList<ParaRelatedFetsRules2> rules = (ArrayList<ParaRelatedFetsRules2>) cr.list();
//		HashMap<BigDecimal, ArrayList<ParaRelatedFetsRules2>> plRules = new HashMap<>();
//		for (ParaRelatedFetsRules2 rule : rules) {
//			ArrayList<ParaRelatedFetsRules2> tmp = new ArrayList<>();
//			if(plRules.isEmpty()){
//				tmp.add(rule);
//				plRules.put(rule.getPlId(), tmp);
//			}else{
//				if(plRules.containsKey(rule.getPlId())){
//					plRules.get(rule.getPlId()).add(rule);
//				}else{
//					tmp.add(rule);
//					plRules.put(rule.getPlId(), tmp);
//				}
//			}
//			plAffectedFets.add(CommonFunctions.getFeatureName(rule.getUpdatedFetId()));
//		}
//		return plRules;
//	}
	
	public void exportParts() throws FeatureNameException, NotValidRelatedFeatureException{
		ArrayList<ParaRelatedFetsRules2> rules = getRules();
		ArrayList<String> plFets = CommonFunctions.getPlFeatures(plId, session);
		HashSet<String> affectedFets = getPlAffectedFets(rules);
		
		
		//add pl features to the header
		for (String feature : plFets) {
			FILE_HEADER += feature + "\t";
			if(affectedFets.contains(feature)){
				FILE_HEADER += feature + "_Generated\t"; 
			}
		}
		verticalExport.println(FILE_HEADER);
				
		//get parts main data from export statment
		String exportStmt = "";
		for (ParaRelatedFetsRules2 rule : rules) {
			DirectExporterData data = null;
			//get export statement to export target parts
			if(rule.getRelation().equals(DIRECT) ||rule.getRelation().equals(RANGE)
					|| rule.getRelation().equals(CONSTANT_FORMULA) || rule.getRelation().equals(RULE_FORMULA)){
				
				exportStmt = generateExportStatement(rule.getPlId().intValue(), rule.getManId().intValue(), 
						rule.getFetId(), rule.getFetColNm(), rule.getFetValId(), rule.getManId().intValue()==0?false:true);
				
			}else{
				exportStmt = MultiRelatedFeatures.generateExportStatement(rule.getPlId().intValue(), rule.getManId().intValue(), 
						rule.getFetId(), rule.getFetColNm(), rule.getFetValId(), rule.getManId().intValue()==0?false:true);
			}
			
			SQLQuery sql = session.createSQLQuery(exportStmt);
			ArrayList<Object[]> result = (ArrayList<Object[]>)sql.list();
			int x = 0;
			for (Object[] row : result) {
				System.out.println(x++);
				String rowdata = "";
				data = setData(rule, row);
				
				//set main data				
				rowdata += data.comId +"\t"+ data.plName +"\t"+ data.manCode +"\t"+ data.manName +"\t"+ data.family +"\t"+ 
						   data.comDesc +"\t"+ data.pdfUrl +"\t"+ data.lcState +"\t"+ data.rohs +"\t"+ data.mask +"\t"+ 
						   data.famCross +"\t"+ data.generic +"\t";  
				
				//add values of these feature Pin Count,Supplier Package,PRODUCT_URL
				String pincount = CommonFunctions.getFetVal("Pin Count", plId, data.comId);
				String supplierPkg = CommonFunctions.getFetVal("Supplier Package", plId, data.comId);
				String productUtl = CommonFunctions.getFetVal("PRODUCT_URL", plId, data.comId);
				rowdata += pincount +"\t"+ supplierPkg +"\t"+ productUtl +"\t";
				
				//get values of pl features
				for (int i=0; i<plFets.size(); i++) {
					String feature = plFets.get(i);				
					//get value of the feature
					String fetValue = CommonFunctions.getFetVal(feature, plId, data.comId);
					rowdata += fetValue +"\t";
					
					//get generated value of updated feature
					if(affectedFets.contains(feature)){						
						String generatedFetValue = data.getGeneratedFetVal();
						rowdata += generatedFetValue +"\t";
					}
					
				}		
				verticalExport.println(rowdata.replace("null", ""));
			}
		}		
	}
	
	public HashSet<String> getPlAffectedFets(ArrayList<ParaRelatedFetsRules2> rules) throws FeatureNameException{
		HashSet<String> affectedFets = new HashSet<String>();
		for (ParaRelatedFetsRules2 rule : rules) {
			affectedFets.add(CommonFunctions.getFeatureName(rule.getUpdatedFetId()));
		}
		return affectedFets;
	}
	
	public static void main(String[] args){
		try {
			PrintWriter file = new PrintWriter(new File("D:\\Parametric_Generation\\"+CommonFunctions.getPlName(1771)+".txt"));
			Session session = SessionUtil.getSession();
			RelatedFeaturesCPExporter cp = new RelatedFeaturesCPExporter(1771, 1614, file, session);
			cp.exportParts();
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
