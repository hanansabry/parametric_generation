package com.se.RelatedFeaturesNew;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules2;

import static com.se.RelatedFeaturesNew.RelatedFeatures.*;

public class DirectExporterData {
	Long comId;
	String plName;
	String comPartNum;
	String manName;
	String manCode;
	String family;
	String comDesc;
	String pdfUrl;
	String lcState;
	String rohs;
	String mask;
	String famCross;
	String generic;
	private String uFetName;
	String updatedFetVal;
	private String uFetValDb;
	private String uFetValG;
	protected String fetName;
	protected String fetVal;
	
	public void setUpdatedFetName(String uFetId) throws FeatureNameException, NotValidRelatedFeatureException{
		if(fetsMapping.containsKey(uFetId)){
			uFetName = fetsMapping.get(uFetId);
		}else{
			uFetName = CommonFunctions.getFeatureName(uFetId);
			uFetValDb = CommonFunctions.getFetVal(uFetName, plName, comId);
		}
	}
	
	public String getUpdatedFetName(){
		return uFetName;
	}
	
	public String getUpdatedFetValDB(){
		return uFetValDb;
	}
	
	public void setFetName(String fetId, String fetValId) throws FeatureNameException, NotValidRelatedFeatureException{
		if(fetsMapping.containsKey(fetId)){
			fetName = fetsMapping.get(fetId);
		}else{
			fetName = CommonFunctions.getFeatureName(fetId);
			if(fetValId==null || fetValId.equals("")){
				this.fetVal = CommonFunctions.getFetVal(fetName, plName, comId);
			}else{
				this.fetVal = CommonFunctions.getFetValById(fetName, fetValId);
			}
		}
	}
	
	public String getFetName(){
		return fetName;
	}
	
	public String getFetVal(){
		return fetVal;
	}
	
	public void setGeneratedFetVal(String uFetVal){
		uFetValG = uFetVal;
	}
	
	public String getGeneratedFetVal(){
		return uFetValG;
	}
	
	
	public static void main(String[] args) throws FeatureNameException, NotValidRelatedFeatureException{
		Session session = SessionUtil.getSession();
		Criteria cr = session.createCriteria(ParaRelatedFetsRules2.class);
		cr.add(Restrictions.eq("plId", new BigDecimal(2515)));
		cr.add(Restrictions.eq("manId", new BigDecimal(0)));
		
		ParaRelatedFetsRules2 rule = (ParaRelatedFetsRules2) cr.uniqueResult();
		
		
		if(rule.getRelation().equals(CONCATENATION)){
			String export = MultiRelatedFeatures.generateExportStatement(rule.getPlId().intValue(), rule.getManId().intValue(), rule.getFetId(), 
					rule.getFetColNm(), rule.getFetValId(), false);
			
			SQLQuery sql = session.createSQLQuery(export);
			ArrayList<Object[]> result = (ArrayList<Object[]>)sql.list();
			ArrayList<DirectExporterData> dataExport = new ArrayList<DirectExporterData>();
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
				DirectExporterData data = new ConcatenateExporterData();
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
				
//				RuleFormulaExporterData.setRules(session, plName);
				
				data.setFetName(rule.getFetId(), rule.getFetValId());
				data.setUpdatedFetName(rule.getUpdatedFetId());
				data.setGeneratedFetVal(rule.getUpdatedFetVal());
				
				dataExport.add(data);
				
				System.out.println("comId = "+data.comId+", DB val = "+data.getUpdatedFetValDB()+
						", Generated Val = "+data.getGeneratedFetVal()+", Fet Val = "+data.getFetVal()+
						", Updated fet Val = "+data.updatedFetVal);
			}
			
			System.err.println(dataExport.size());
			
		}
		
	}
}
