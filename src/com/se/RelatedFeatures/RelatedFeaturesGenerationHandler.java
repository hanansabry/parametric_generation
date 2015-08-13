package com.se.RelatedFeatures;

import java.io.BufferedReader;
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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.se.Exceptions.FeatureNameException;
import com.se.Exceptions.NotValidRelatedFeatureException;
import com.se.Exceptions.PlException;
import com.se.Exceptions.VendorException;
import com.se.RelatedFeaturesNew.RelatedFeaturesExporter;
import com.se.common.CommonFunctions;
import com.se.connection.SessionUtil;

@ManagedBean(name="relatedFeatureGeneration")
@SessionScoped
public class RelatedFeaturesGenerationHandler {
	private String[] options;
	private UploadedFile file;
	private String fileName;
	private StreamedContent downloadedFile;
	public static final String SAVING_PATH = "D:\\Parametric_Generation\\";
	public static final String UPLOAD_PATH = "D:\\UploadFile\\";
	
	public void setOptions(String[] options){
		this.options = options;
	}
	
	public String[] getOptions(){
		return options;
	}
	
	public void setFile(UploadedFile file){
		this.file = file;
	}
	
	public UploadedFile getFile(){
		return file;
	}
	
	public void setFileName(String filePath){
		this.fileName = filePath;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public void setDownloadedFile(StreamedContent downloadedFile){
		this.downloadedFile = downloadedFile;
	}
	
	public StreamedContent getDownloadedFile(){
		return downloadedFile;
	}
	
	public void showOutput(){
		System.out.println("Selected Options : ");
		for (String option : options) {
			System.out.print(option+", ");
		}
		System.out.println();
		System.out.println("File path = "+file.getFileName());
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
			} else {
				uploadMessage = "Uploaded files must be in txt format, please try again";
				summary = "Error";
			}						
			FacesMessage message = new FacesMessage(summary, uploadMessage);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void generateByPl() throws IOException, FeatureNameException, NotValidRelatedFeatureException{
		if(file==null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "You must upload file");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		Session session = SessionUtil.getSession();
		
		//read the file
		BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+getFileName())));
		String line = reader.readLine();
		
		//check header
		if(!line.equalsIgnoreCase("PL_Name")){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Wrong Header");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		//get pls
		ArrayList<Integer> pls = new ArrayList<>();
		while((line = reader.readLine())!=null){
			try {
				int plId = CommonFunctions.getPlId(line);
				pls.add(plId);
			} catch (PlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		reader.close();
		
		PrintWriter verticalExport = new PrintWriter(new FileWriter(new File(SAVING_PATH+"vertical_export.txt")));
		verticalExport.println(RelatedFeaturesExporter.FILE_HEADER);
		
		for (String option : options) {
			if(option.equalsIgnoreCase("vertical")){
				for (int plId : pls) {
//					RelatedFeaturesVerticalExport export = new RelatedFeaturesVerticalExport(plId, 0, session);
//					export.exportParts(verticalExport);
					RelatedFeaturesExporter export = new RelatedFeaturesExporter(plId, 0, verticalExport, session);
					export.exportParts();
				}
				verticalExport.close();
				downloadFile("vertical_export.txt");
			}else if(option.equalsIgnoreCase("cp")){
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Coming Soon", "Sorry, CP File option is not available yet");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
		}
	}
	
	public void generateByVendor() throws IOException, FeatureNameException, NotValidRelatedFeatureException{
		if(file==null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "You must upload file");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		Session session = SessionUtil.getSession();
		
		//read the file
		BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+getFileName())));
		String line = reader.readLine();
		
		//check header
		if(!line.equalsIgnoreCase("Vendor_Name")){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Wrong Header");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		//get pls
		ArrayList<Integer> vendors = new ArrayList<>();
		while((line = reader.readLine())!=null){
			try {
				int manId = CommonFunctions.getVendorId(line);
				vendors.add(manId);
			} catch (VendorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		reader.close();
		
		PrintWriter verticalExport = new PrintWriter(new FileWriter(new File(SAVING_PATH+"vertical_export.txt")));
		verticalExport.println(RelatedFeaturesExporter.FILE_HEADER);
		for (String option : options) {
			if(option.equalsIgnoreCase("vertical")){
				for (int manId : vendors) {
					RelatedFeaturesExporter export = new RelatedFeaturesExporter(0, manId, verticalExport, session);
					export.exportParts();
				}				
				downloadFile("vertical_export.txt");
			}else if(option.equalsIgnoreCase("cp")){
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Coming Soon", "Sorry, CP File option is not available yet");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
		}
	}
	
	public void generateByPlVendor() throws IOException{
		if(file==null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "You must upload file");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		Session session = SessionUtil.getSession();
		
		//read the file
		BufferedReader reader = new BufferedReader(new FileReader(new File(UPLOAD_PATH+getFileName())));
		String line = reader.readLine();
		
		//check header
		if(!line.equalsIgnoreCase("PL_Name	Vendor")){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Wrong Header");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			return;
		}
		
		//get pls
		ArrayList<Integer[]> plmans = new ArrayList<>();
		while((line = reader.readLine())!=null){
			String[] fields = line.split("\t");
			Integer[] pm = new Integer[2];
			try {
				
				String plName = fields[0];
				int plId = CommonFunctions.getPlId(plName);
				pm[0] = plId;
				if(fields.length>1){
					String vendorName = fields[1];
					int manId = CommonFunctions.getVendorId(vendorName);								
					pm[1] = manId;					
				}else{
					pm[1] = 0;
				}
				plmans.add(pm);				
			} catch (VendorException e) {
				e.printStackTrace();
			} catch (PlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		reader.close();
		
		String file = "vertical_export_"+Math.random()+".txt";
		PrintWriter verticalExport = new PrintWriter(new FileWriter(new File(SAVING_PATH+file)));
		System.out.println(RelatedFeaturesExporter.FILE_HEADER);
		verticalExport.println(RelatedFeaturesExporter.FILE_HEADER);
		
		for (String option : options) {
			if(option.equalsIgnoreCase("vertical")){
				try{
					for (Integer[] plman : plmans) {
						int plId = plman[0];
						int manId = plman[1];
						RelatedFeaturesExporter export = new RelatedFeaturesExporter(plId, manId, verticalExport, session);
						export.exportParts();
					}				
				}catch(Exception ex){
					ex.printStackTrace();
				}
				downloadFile(file);
			}else if(option.equalsIgnoreCase("cp")){
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Coming Soon", "Sorry, CP File option is not available yet");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
		}
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
}
