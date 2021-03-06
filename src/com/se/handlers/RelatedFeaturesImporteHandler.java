package com.se.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import java.io.BufferedInputStream;

import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.Exceptions.PlException;
import com.se.Exceptions.VendorException;
import com.se.RelatedFeatures.RelatedFeature;
import com.se.RelatedFeaturesNew.MultiRelatedFeatures;
import com.se.RelatedFeaturesNew.RelatedFeatures;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;
import com.se.pojos.ParaRelatedFetsRules;
import com.se.pojos.ParaRelatedFetsRules2;

@ManagedBean
@SessionScoped
public class RelatedFeaturesImporteHandler {
	private UploadedFile file;
	public String doneMessage;
	private StreamedContent downloadedFile;
	private String exportBy;
	
	private String fileName;
	private boolean disableImport;
	public static final String UPLOAD_PATH = "D:\\UploadFile\\";
	public static final String SAVING_PATH = "D:\\Parametric_Generation\\";
	private boolean importDone;
	
	public RelatedFeaturesImporteHandler() {
		this.disableImport = true;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFileName(String filePath) {
		this.fileName = filePath;
	}

	public String getFileName() {
		return fileName;
	}
	
	public boolean isDisableImport(){
		return disableImport;
	}
	
	public void setDisableImport(boolean enableImport){
		this.disableImport = enableImport;
	}
	
	public void setImportDone(boolean importDone){
		this.importDone = importDone;
	}
	
	public boolean isImportDone(){
		return importDone;
	}
	
	public void setDoneMessage(String doneMessage){
		this.doneMessage = doneMessage;
	}
	
	public String getDoneMessage(){
		return doneMessage;
	}
	
	public void setExportBy(String exportBy){
		this.exportBy = exportBy;
	}
	
	public String getExportBy(){
		return exportBy;
	}
	
	public void upload() throws IOException {
		String uploadMessage = "";
		String summary = "";
		if (file != null) {
			if (file.getFileName().toLowerCase().endsWith(".txt")) {
				// do stuff
				uploadMessage = file.getFileName() + " is uploaded.";
				summary = "Succesful";
				System.out.println(file.getFileName());
				InputStream input = file.getInputstream();
				String filePath = UPLOAD_PATH + getFileName();
				System.out.println("File path = " + filePath);
				OutputStream output = new FileOutputStream(new File(filePath));

				try {
					IOUtils.copy(input, output);
				} finally {
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(output);
				}
			} else {
				uploadMessage = "Uploaded files must be in txt format, please try again";
				summary = "Error";
			}

			FacesMessage message = new FacesMessage(summary, uploadMessage);
			FacesContext.getCurrentInstance().addMessage(null, message);

		}
	}

//	public void importFile() throws IOException {
//		System.out.println("I pressed the import button");
//		try {
//			// Session session = SessionUtil.getSession();
//			// Transaction tx = null;
//			
//			PrintWriter logFile = new PrintWriter(new FileWriter(new File(SAVING_PATH+"related_fets_log.txt")));
//			logFile.println("Product Line\tVendor\tID Feature Name\tID Featuer Value\tUpdated Feature Name\tUpdated Feature Value\t"
//					+ "Relation\tLog Comment");
//			
//			ArrayList<RelatedFeature> rows = new ArrayList<>();
//			System.out.println("File path = " + fileName);
//			BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+ fileName)));
//			String line = reader.readLine();
//			//check input header
//			String requiredHeader = "Product Line	Vendor	ID Featuer Name	ID Featuer Value	Updated Feature Name	Updated Feature Value	Relation";
//			if(!requiredHeader.equalsIgnoreCase(line)){
//				doneMessage = "Wrong Header";
//				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", getDoneMessage());
////				FacesContext.getCurrentInstance().addMessage(null, message);
//				RequestContext.getCurrentInstance().showMessageInDialog(message);
//				return;
//			}
//			int i = 0;
//			RelatedFeature row = null;
//			ParaRelatedFetsRules r = null;
//			while ((line = reader.readLine()) != null) {
//				System.out.println(i++);
//				try {
//					Session session = SessionUtil.getSession();
//					Transaction tx = null;
//					// System.out.println(line);
//					String[] cols = line.split("\t");
//					String plName = cols[0];
//					String vendorName = cols[1] == null ? null : cols[1]
//							.equals("") ? null : cols[1];
//					String idFetName = cols[2];
//					String idFetVal = cols[3] == null ? null : cols[3]
//							.equals("") ? null : cols[3];
//					String updatedFetName = cols[4];
//					String updatedFetVal = cols[5];
//					String relation = cols[6];
//
//					
//					switch (relation) {
//					case RelatedFeature.DIRECT:
//						row = new DirectRelatedFeature(plName, vendorName,
//								idFetName, idFetVal, updatedFetName,
//								updatedFetVal);
//						break;
//					case RelatedFeature.MULTI_DIRECT:
//						row = new MultiRelatedFeatures(plName, vendorName,
//								idFetName, idFetVal, updatedFetName,
//								updatedFetVal);
//						break;
//					case RelatedFeature.RANGE:
//						row = new RangeRelatedFeature(plName, vendorName,
//								idFetName, idFetVal, updatedFetName,
//								updatedFetVal);
//						break;
//					case RelatedFeature.CONSTANT_FORMULA:
//						row = new FormulaRelatedFeature(plName, vendorName,
//								idFetName, idFetVal, updatedFetName,
//								updatedFetVal);
//						break;
//					case RelatedFeature.RULE_FORMULA:
//						row = new FormulaRuleRelatedFeature(plName, vendorName,
//								idFetName, idFetVal, updatedFetName,
//								updatedFetVal);
//						break;
//					case RelatedFeature.CONCATENATION:
//						row = new ConcatenateRelatedFeature(plName, vendorName,
//								idFetName, idFetVal, updatedFetName,
//								updatedFetVal);
//						break;
//					default:
//						throw new NotValidRelatedFeatureException(
//								"Not Valid Relation, Relation must be one of the following : "
//										+ "Direct - Multi Direct - Range - Constant Formula - Rule Formula - Concatenation");
//					}
//					row.validateFields();
//					// row.setComId(32986803);
//					r = new ParaRelatedFetsRules();
//					r.setPlId(new BigDecimal(row.plId));
//					r.setPlName(row.plName);
//					r.setManId(new BigDecimal(row.vendorId));
//					r.setManName(row.vendorName);
//					r.setFetName(row.fetName);
//					r.setFetId(new BigDecimal(row.fetId));
//					r.setFetIdColNm(row.fetIdColNm);
//					r.setFetVal(row.fetVal);
//					r.setFetValId(row.fetValId);
//					r.setUpdatedFetName(row.updatedFetName);
//					r.setUpdatedFetColNm(row.updatedFetColNm);
//					r.setUpdatedFetId(new BigDecimal(row.updatedFetId));
//					r.setUpdatedFetName(row.updatedFetName);
//					r.setUpdatedFetVal(row.updatedFetVal);
//					r.setUpdatedFetValId(new BigDecimal(row.updatedFetValId));
//					r.setRelation(relation);
//					r.setInsertionDate(new Date());
//
////					try {
//						tx = session.getTransaction();
//						session.save(r);
//						tx.commit();
////					} catch (Exception ex) {
////						tx.rollback();
////					}
//					logFile.println(row.plName+"\t"+row.vendorName+"\t"+row.fetName+"\t"+row.fetVal+"\t"+
//							row.updatedFetName+"\t"+row.updatedFetVal+"\t"+r.getRelation()+"\tDone");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					System.err.println(e.getMessage());
//					importDone = false;
//					setDoneMessage("Error");
//					logFile.println(row.plName+"\t"+row.vendorName+"\t"+row.fetName+"\t"+row.fetVal+"\t"+
//							row.updatedFetName+"\t"+row.updatedFetVal+"\t"+r.getRelation()+"\t"+e.getMessage().trim());
//				}
//				logFile.flush();
//			}
//			logFile.close();
//			importDone = true;
//			System.err.println("Done");
//			setDoneMessage("File is imported succssfully");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			setDoneMessage(e.getMessage());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			importDone = false;
//			setDoneMessage(e.getMessage());
//		}
//		System.out.println("import Done = "+importDone);
//		System.out.println("import done message = "+doneMessage);
////		downloadFile("related_fets_log.txt");
//		FacesMessage message = null;
//		if(importDone){
//			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Done", getDoneMessage());
//		}else{
//			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", getDoneMessage());
//		}
//		
//		downloadFile("related_fets_log.txt");
//		FacesContext.getCurrentInstance().addMessage(null, message);
//		RequestContext.getCurrentInstance().showMessageInDialog(message);		
//	}
	
	public void importFile() throws IOException{
		System.out.println("I pressed the import button");
		try {
			// Session session = SessionUtil.getSession();
			// Transaction tx = null;
			
			PrintWriter logFile = new PrintWriter(new FileWriter(new File(SAVING_PATH+"related_fets_log.txt")));
			logFile.println("Product Line\tVendor\tID Feature Name\tID Featuer Value\tUpdated Feature Name\tUpdated Feature Value\t"
					+ "Relation\tLog Comment");
						
			BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+ fileName)));			
			String line = reader.readLine();			
			//check input header
			String requiredHeader = "Product Line	Vendor	ID Featuer Name	ID Featuer Value	Updated Feature Name	Updated Feature Value	Relation";
			if(!requiredHeader.equalsIgnoreCase(line)){
				doneMessage = "Wrong Header";
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", getDoneMessage());
//				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
			int i = 0;
			
			String plName = "", manName = "", fetName = "", fetVal = "", uFetName = "", uFetVal = "", relation = "";
			System.out.println("before looping on the file");
			while ((line = reader.readLine()) != null) {
				System.out.println(i++);
				System.out.println(line);
				try {
					System.out.println("before getting the session");
					Session session = SessionUtil.getSession();
					Transaction tx = null;
					// System.out.println(line);
					String[] cols = line.split("\t");
					plName = cols[0];
					manName = cols[1] == null ? null : cols[1].equals("") ? null : cols[1];
					fetName = cols[2];
					fetVal = cols[3] == null ? null : cols[3].equals("") ? null : cols[3];
					uFetName = cols[4];
					uFetVal = cols[5];
					relation = cols[6];

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
					
					System.out.println("before save the row using session");
//					try {
						tx = session.getTransaction();
						System.out.println("tx = session.getTransaction();");
						session.save(ruleRow);
						System.out.println("session.save(ruleRow);");
						tx.commit();
						System.out.println("tx.commit();");
//					} catch (Exception ex) {
//						tx.rollback();
//					}
						
						
					logFile.println(plName+"\t"+manName+"\t"+fetName+"\t"+fetVal+"\t"+
							uFetName+"\t"+uFetVal+"\t"+relation+"\tDone");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
					importDone = false;
					setDoneMessage("Error");
					logFile.println(plName+"\t"+manName+"\t"+fetName+"\t"+fetVal+"\t"+
							uFetName+"\t"+uFetVal+"\t"+relation+"\t"+e.getMessage().trim());
				}
				logFile.flush();
			}
			System.out.println("after reading the file");
			logFile.close();
			importDone = true;
			System.err.println("Done");
			setDoneMessage("File is imported succssfully");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setDoneMessage(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			importDone = false;
			setDoneMessage(e.getMessage());
		}
		System.out.println("import Done = "+importDone);
		System.out.println("import done message = "+doneMessage);
//		downloadFile("related_fets_log.txt");
		FacesMessage message = null;
		if(importDone){
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Done", getDoneMessage());
		}else{
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", getDoneMessage());
		}
		
		downloadFile("related_fets_log.txt");
		FacesContext.getCurrentInstance().addMessage(null, message);
		RequestContext.getCurrentInstance().showMessageInDialog(message);		
	}

	public void exportFile() throws IOException {
		System.out.println("export option is "+getExportBy());
		PrintWriter exportFile = new PrintWriter(SAVING_PATH+"related_fets_export.txt");
		exportFile.println("Product Line\tVendor\tID Featuer Name\tID Featuer Value\tUpdated Feature Name\tUpdated Feature Value\tRelation\tInsertion_Date");
		
		//export data from the table
		Session session = SessionUtil.getSession();
		Criteria cr = session.createCriteria(ParaRelatedFetsRules2.class);
		ArrayList<ParaRelatedFetsRules2> rules  = (ArrayList<ParaRelatedFetsRules2>)cr.list();
		for (ParaRelatedFetsRules2 rule : rules) {
			try {
				String plName = CommonFunctions.getPlName(rule.getPlId().intValue());
				String vendorName = CommonFunctions.getManName(rule.getManId().intValue());
				String fetName = "";
				if(rule.getFetId().contains(RelatedFeatures.CONCATENATE_FET_CHAR)){
					String[] fetIds = rule.getFetId().split(RelatedFeatures.CONCATENATE_FET_CHAR_SEP);
					for (String fetId : fetIds) {
						fetName += CommonFunctions.getFeatureName(fetId) + RelatedFeatures.CONCATENATE_FET_CHAR;
					}
					fetName = fetName.substring(0, fetName.lastIndexOf(RelatedFeatures.CONCATENATE_FET_CHAR));
				}else{
					fetName = CommonFunctions.getFeatureName(rule.getFetId()) + RelatedFeatures.CONCATENATE_FET_CHAR;
				}
				
				String fetVal = "";
				if(rule.getFetValId() == null || rule.getFetValId().equals("")){
					fetVal = "";
				}else if(rule.getFetValId().contains(RelatedFeatures.CONCATENATE_FET_CHAR)){
					String[] fetValIds = rule.getFetValId().split(RelatedFeatures.CONCATENATE_FET_CHAR_SEP);
					for (String fetValId : fetValIds) {
						fetVal += CommonFunctions.getFetValById(fetName, fetValId) + RelatedFeatures.CONCATENATE_FET_CHAR;
					}
					fetVal = fetVal.substring(0, fetVal.lastIndexOf(RelatedFeatures.CONCATENATE_FET_CHAR));
				}else{
					fetVal = CommonFunctions.getFetValById(fetName, rule.getFetValId());
				}
				String updatedFetName = CommonFunctions.getFeatureName(rule.getUpdatedFetId());
				String updatedFetVal = rule.getUpdatedFetVal();
				String relation = rule.getRelation();
				String insertionDate = rule.getInsertionDate().toString();
				
				exportFile.println(plName+"\t"+vendorName+"\t"+fetName+"\t"+fetVal+"\t"+
								   updatedFetName+"\t"+updatedFetVal+"\t"+relation+"\t"+insertionDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			exportFile.println(rule.getPlName()+"\t"+rule.getManName()+"\t"+rule.getFetName()+"\t"+rule.getFetVal()+"\t"+
//							   rule.getUpdatedFetName()+"\t"+rule.getUpdatedFetVal()+"\t"+rule.getRelation()+"\t"+rule.getInsertionDate());
			exportFile.flush();
		}
		exportFile.close();
		downloadFile("related_fets_export.txt");
		FacesMessage message = new FacesMessage("Done", "File is exported successfully");
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
	
	public void exportByPL() throws IOException{
		if(file==null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "You must upload file");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		System.out.println("export option is "+getExportBy());
		PrintWriter exportFile = new PrintWriter(SAVING_PATH+"related_fets_export_by_pl.txt");
		exportFile.println("Product Line\tVendor\tID Featuer Name\tID Featuer Value\tUpdated Feature Name\tUpdated Feature Value\tRelation\tInsertion_Date");
		
		//read the file
		BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+getFileName())));
		String line = reader.readLine();
		if(!line.equalsIgnoreCase("PL_Name")){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Wrong Header");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		ArrayList<BigDecimal> pls = new ArrayList<>();
		while((line=reader.readLine())!=null){
			try {
				long plId = CommonFunctions.getPlId(line);
				pls.add(new BigDecimal(plId));
			} catch (PlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//export data from the table
		Session session = SessionUtil.getSession();
		Criteria cr = session.createCriteria(ParaRelatedFetsRules2.class);
		cr.add(Restrictions.in("plId", pls));
		ArrayList<ParaRelatedFetsRules2> rules  = (ArrayList<ParaRelatedFetsRules2>)cr.list();
		for (ParaRelatedFetsRules2 rule : rules) {
			try {
				String plName = CommonFunctions.getPlName(rule.getPlId().intValue());
				String vendorName = CommonFunctions.getManName(rule.getManId().intValue());
				String fetName = CommonFunctions.getFeatureName(rule.getFetId());
				String fetVal = CommonFunctions.getFetValById(fetName, rule.getFetValId());
				String updatedFetName = CommonFunctions.getFeatureName(rule.getUpdatedFetId());
				String updatedFetVal = rule.getUpdatedFetVal();
				String relation = rule.getRelation();
				String insertionDate = rule.getInsertionDate().toString();
				
				exportFile.println(plName+"\t"+vendorName+"\t"+fetName+"\t"+fetVal+"\t"+
								   updatedFetName+"\t"+updatedFetVal+"\t"+relation+"\t"+insertionDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		exportFile.close();
		downloadFile("related_fets_export_by_pl.txt");
		FacesMessage message = new FacesMessage("Done", "File is exported successfully");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}	
	
	public void exportByVendor() throws IOException{
		if(file==null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "You must upload file");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		System.out.println("export option is "+getExportBy());
		PrintWriter exportFile = new PrintWriter(SAVING_PATH+"related_fets_export_by_vendor.txt");
		exportFile.println("Product Line\tVendor\tID Featuer Name\tID Featuer Value\tUpdated Feature Name\tUpdated Feature Value\tRelation\tInsertion_Date");
		
		//read the file
		BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+getFileName())));
		String line = reader.readLine();
		if(!line.equalsIgnoreCase("Vendor_Name")){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Wrong Header");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		ArrayList<BigDecimal> vendors = new ArrayList<>();
		while((line=reader.readLine())!=null){
			try {
				int manId = CommonFunctions.getVendorId(line);
				vendors.add(new BigDecimal(manId));
			} catch (VendorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//export data from the table
		Session session = SessionUtil.getSession();
		Criteria cr = session.createCriteria(ParaRelatedFetsRules2.class);
		cr.add(Restrictions.in("manId", vendors));
		ArrayList<ParaRelatedFetsRules2> rules  = (ArrayList<ParaRelatedFetsRules2>)cr.list();
		for (ParaRelatedFetsRules2 rule : rules) {
			try {
				String plName = CommonFunctions.getPlName(rule.getPlId().intValue());
				String vendorName = CommonFunctions.getManName(rule.getManId().intValue());
				String fetName = CommonFunctions.getFeatureName(rule.getFetId());
				String fetVal = CommonFunctions.getFetValById(fetName, rule.getFetValId());
				String updatedFetName = CommonFunctions.getFeatureName(rule.getUpdatedFetId());
				String updatedFetVal = rule.getUpdatedFetVal();
				String relation = rule.getRelation();
				String insertionDate = rule.getInsertionDate().toString();
				
				exportFile.println(plName+"\t"+vendorName+"\t"+fetName+"\t"+fetVal+"\t"+
								   updatedFetName+"\t"+updatedFetVal+"\t"+relation+"\t"+insertionDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		exportFile.close();
		downloadFile("related_fets_export_by_vendor.txt");
		FacesMessage message = new FacesMessage("Done", "File is exported successfully");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void exportByPlVendor() throws IOException{
		if(file==null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "You must upload file");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
		
		System.out.println("export option is "+getExportBy());
		PrintWriter exportFile = new PrintWriter(SAVING_PATH+"related_fets_export_by_pl_vendor.txt");
		exportFile.println("Product Line\tVendor\tID Featuer Name\tID Featuer Value\tUpdated Feature Name\tUpdated Feature Value\tRelation\tInsertion_Date");
		
		//read the file
		BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+getFileName())));
		String line = reader.readLine();
		if(!line.equalsIgnoreCase("PL_Name	Vendor_Name")){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Wrong Header");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		ArrayList<BigDecimal> vendors = new ArrayList<>();
		ArrayList<BigDecimal> pls = new ArrayList<>();
		
		Session session = SessionUtil.getSession();
		Criteria cr ;
		ArrayList<ParaRelatedFetsRules2> rules = new ArrayList<ParaRelatedFetsRules2>();
		while((line=reader.readLine())!=null){
			cr = session.createCriteria(ParaRelatedFetsRules2.class);
			try {
				String[] plman = line.split("\t");
				String plName = plman[0];
				String vendorName = plman[1];
				
				int plId = CommonFunctions.getPlId(plName);
				int manId = CommonFunctions.getVendorId(vendorName);
				
				cr.add(Restrictions.eq("manId", new BigDecimal(manId)));
				cr.add(Restrictions.eq("plId", new BigDecimal(plId)));
				
				rules.addAll((ArrayList<ParaRelatedFetsRules2>)cr.list());
//				vendors.add(new BigDecimal(manId));
			} catch (VendorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//export data from the table
		
		
//		ArrayList<ParaRelatedFetsRules> rules  = (ArrayList<ParaRelatedFetsRules>)cr.list();
		for (ParaRelatedFetsRules2 rule : rules) {
			try {
				String plName = CommonFunctions.getPlName(rule.getPlId().intValue());
				String vendorName = CommonFunctions.getManName(rule.getManId().intValue());
				String fetName = CommonFunctions.getFeatureName(rule.getFetId());
				String fetVal = CommonFunctions.getFetValById(fetName, rule.getFetValId());
				String updatedFetName = CommonFunctions.getFeatureName(rule.getUpdatedFetId());
				String updatedFetVal = rule.getUpdatedFetVal();
				String relation = rule.getRelation();
				String insertionDate = rule.getInsertionDate().toString();
				
				exportFile.println(plName+"\t"+vendorName+"\t"+fetName+"\t"+fetVal+"\t"+
								   updatedFetName+"\t"+updatedFetVal+"\t"+relation+"\t"+insertionDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		exportFile.close();
		downloadFile("related_fets_export_by_pl_vendor.txt");
		FacesMessage message = new FacesMessage("Done", "File is exported successfully");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		String uploadMessage = "";
		String summary = "";
		this.setFile(event.getFile());
		this.setFileName(event.getFile().getFileName());
		if (file != null) {
			if (file.getFileName().toLowerCase().endsWith(".txt")) {
				// do stuff
				uploadMessage = file.getFileName() + " is uploaded.";
				summary = "Succesful";
				System.out.println(file.getFileName());
				String fileName = file.getFileName();
				InputStream input = file.getInputstream();
				
				
				fileName = "D:\\UploadFile\\" + fileName;
				System.out.println("File path = " + fileName);
				OutputStream output = new FileOutputStream(new File(fileName));

				try {
					IOUtils.copy(input, output);
				} finally {
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(output);
				}
				setDisableImport(false);
			} else {
				uploadMessage = "Uploaded files must be in txt format, please try again";
				summary = "Error";
			}						
			FacesMessage message = new FacesMessage(summary, uploadMessage);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}	

	public StreamedContent getDownloadedFile() {
        return downloadedFile;
    }
	
	public void setDownloadedFile(StreamedContent downloadedFile){
		this.downloadedFile = downloadedFile;
	}
	
	public void downloadFile(String fileName) throws IOException{
//		InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/images/optimus.jpg");
		InputStream stream;
		try {
			String filePath = SAVING_PATH + fileName;
			stream = new FileInputStream(new File(filePath));
			System.out.println(stream.read());
			downloadedFile = new DefaultStreamedContent(stream, "text/plain",fileName);
			System.out.println("done");

		} catch (FileNotFoundException e) {
			String outputMessage = "No File To Download";
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"", outputMessage);
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			// e.printStackTrace();
		}
	}
	
//	public void downloadFile() throws IOException{
////		InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/images/optimus.jpg");
////		importFile();
//		InputStream stream;
//		String fileName = "related_fets_log.txt";
//		try {
//			String filePath = SAVING_PATH + fileName;
//			stream = new FileInputStream(new File(filePath));
//			System.out.println(stream.read());
//			downloadedFile = new DefaultStreamedContent(stream, "text/plain",fileName);
//			System.out.println("done");
//
//		} catch (FileNotFoundException e) {
//			String outputMessage = "No File To Download";
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"", outputMessage);
//			RequestContext.getCurrentInstance().showMessageInDialog(message);
//			// e.printStackTrace();
//		}
//	}
}
