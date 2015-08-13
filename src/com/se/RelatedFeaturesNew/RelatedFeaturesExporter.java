package com.se.RelatedFeaturesNew;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules2;

import static com.se.RelatedFeaturesNew.RelatedFeatures.*;

public class RelatedFeaturesExporter {
	
	public static final String FILE_HEADER = "PProduct Line\tPRODUCT_NAME\tVendor\tFamily\tDescription\t"
			+ "PRODUCT_EXTERNAL_DATASHEET\tMask\tFamily Cross\tGeneric\tFeature Name\tFeature Value\t"
			+ "Generated Feature Value\tID Feature Name\tID Feature Value\tRelation";
	Session session;
	PrintWriter verticalExport;
	int plId;
	int manId;
	
	public RelatedFeaturesExporter(int plId, int manId, PrintWriter verticalExport, Session session) {
		this.plId = plId;
		this.manId = manId;
		this.verticalExport = verticalExport;
		this.session = session;
	}

	public ArrayList<ParaRelatedFetsRules2> getRules(){
		Criteria cr = session.createCriteria(ParaRelatedFetsRules2.class);
		if(plId!=0){
			cr.add(Restrictions.eq("plId", new BigDecimal(plId)));
		}
		if(manId==0){
			cr.add(Restrictions.eq("manId", new BigDecimal(0)));
		}else{
			cr.add(Restrictions.eq("manId", new BigDecimal(manId)));
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<ParaRelatedFetsRules2> rules = (ArrayList<ParaRelatedFetsRules2>) cr.list();
		return rules;
	}
	
	public void exportParts() throws FeatureNameException, NotValidRelatedFeatureException{
		ArrayList<ParaRelatedFetsRules2> rules = getRules();
		
		
		
//		verticalExport.println(FILE_HEADER);
		
		String exportStmt = "";
		
		for (ParaRelatedFetsRules2 rule : rules) {
			DirectExporterData data = null;
			//get export statement to export targe parts
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
			
			int i=0;
			for (Object[] field : result) {
				System.out.println(++i);
				String comId = field[0]==null?"":field[0].toString();
				String plName = field[1]==null?"":field[1].toString();
				String comPartNum = field[2]==null?"":field[2].toString();
				String manName = field[3]==null?"":field[3].toString();
				String family = field[4]==null?"":field[4].toString();
				String comDesc = field[5]==null?"":field[5].toString();
				String pdfUrl = field[6]==null?"":field[6].toString();
				String mask = field[7]==null?"":field[7].toString();
				String famCross = field[8]==null?"":field[8].toString();
				String generic = field[9]==null?"":field[9].toString();
				
//				DirectExporterData data = new DirectExporterData();
//				DirectExporterData data = new RangeExporterData();
//				DirectExporterData data = new FormulaExporterData();
//				DirectExporterData data = new RuleFormulaExporterData();
//				DirectExporterData data = new MultiExporterData();
//				DirectExporterData data = new ConcatenateExporterData();
				switch (rule.getRelation()){
					case DIRECT : 
						data = new DirectExporterData();	break;
					case RANGE :
						data = new RangeExporterData();	 break;
					case CONSTANT_FORMULA :
						data = new FormulaExporterData();	break;
					case RULE_FORMULA :
						data = new RuleFormulaExporterData();	break;
					case MULTI_DIRECT :
						data = new MultiExporterData();	break;
					case CONCATENATION :
						data = new ConcatenateExporterData();	break;
				}
				
				data.comId = Long.parseLong(comId);
				data.plName = plName;
				data.comPartNum = comPartNum;
				data.manName = manName;
				data.family = family;
				data.comDesc = comDesc;
				data.pdfUrl = pdfUrl;
				data.mask = mask;
				data.famCross = famCross;
				data.generic = generic;
				data.updatedFetVal = rule.getUpdatedFetVal();
				
				data.setFetName(rule.getFetId(), rule.getFetValId());
				data.setUpdatedFetName(rule.getUpdatedFetId());
				data.setGeneratedFetVal(rule.getUpdatedFetVal());				
				
				System.out.println("comId = "+data.comId+", DB val = "+data.getUpdatedFetValDB()+
						", Generated Val = "+data.getGeneratedFetVal()+", Fet Val = "+data.getFetVal()+
						", Updated fet Val = "+data.updatedFetVal);
				
				verticalExport.println(data.plName+"\t"+data.comPartNum+"\t"+data.manName+"\t"+
									   data.family+"\t"+data.comDesc+"\t"+data.pdfUrl+"\t"+
									   data.mask+"\t"+data.famCross+"\t"+data.generic+"\t"+
									   data.getUpdatedFetName()+"\t"+data.getUpdatedFetValDB()+"\t"+
									   data.getGeneratedFetVal()+"\t"+data.getFetName()+"\t"+
									   data.getFetVal()+"\t"+rule.getRelation());
				verticalExport.flush();
			}			
		}
	}
	
	public static void main(String[] args) throws FeatureNameException, NotValidRelatedFeatureException, IOException{
		Session session = SessionUtil.getSession();
		PrintWriter verticalExport = new PrintWriter(new FileWriter(new File("D:\\"+"vertical_export.txt")));
		RelatedFeaturesExporter ex = new RelatedFeaturesExporter(1282, 0, verticalExport, session);
		ex.exportParts();
		verticalExport.close();
		System.out.println(ex.getRules().size());
	}
}
