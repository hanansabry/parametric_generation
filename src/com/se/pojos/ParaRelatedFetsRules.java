package com.se.pojos;

// Generated Jul 8, 2015 9:10:55 AM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

/**
 * ParaRelatedFetsRules generated by hbm2java
 */
public class ParaRelatedFetsRules implements java.io.Serializable {

	private BigDecimal id;
	private BigDecimal plId;
	private String plName;
	private BigDecimal manId;
	private String manName;
	private String fetName;
	private BigDecimal fetId;
	private String fetIdColNm;
	private String fetVal;
	private String fetValId;
	private String updatedFetName;
	private BigDecimal updatedFetId;
	private String updatedFetColNm;
	private String updatedFetVal;
	private BigDecimal updatedFetValId;
	private String relation;
	private Date insertionDate;
	private String exportStmt;

	public ParaRelatedFetsRules() {
	}

	public ParaRelatedFetsRules(BigDecimal id) {
		this.id = id;
	}

	public ParaRelatedFetsRules(BigDecimal id, BigDecimal plId, String plName,
			BigDecimal manId, String manName, String fetName, BigDecimal fetId,
			String fetIdColNm, String fetVal, String fetValId,
			String updatedFetName, BigDecimal updatedFetId,
			String updatedFetIdColNm, String updatedFetVal,
			BigDecimal updatedFetValId, String relation, Date insertionDate) {
		this.id = id;
		this.plId = plId;
		this.plName = plName;
		this.manId = manId;
		this.manName = manName;
		this.fetName = fetName;
		this.fetId = fetId;
		this.fetIdColNm = fetIdColNm;
		this.fetVal = fetVal;
		this.fetValId = fetValId;
		this.updatedFetName = updatedFetName;
		this.updatedFetId = updatedFetId;
		this.updatedFetColNm = updatedFetIdColNm;
		this.updatedFetVal = updatedFetVal;
		this.updatedFetValId = updatedFetValId;
		this.relation = relation;
		this.insertionDate = insertionDate;
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getPlId() {
		return this.plId;
	}

	public void setPlId(BigDecimal plId) {
		this.plId = plId;
	}

	public String getPlName() {
		return this.plName;
	}

	public void setPlName(String plName) {
		this.plName = plName;
	}

	public BigDecimal getManId() {
		return this.manId;
	}

	public void setManId(BigDecimal manId) {
		this.manId = manId;
	}

	public String getManName() {
		return this.manName;
	}

	public void setManName(String manName) {
		this.manName = manName;
	}

	public String getFetName() {
		return this.fetName;
	}

	public void setFetName(String fetName) {
		this.fetName = fetName;
	}

	public BigDecimal getFetId() {
		return this.fetId;
	}

	public void setFetId(BigDecimal fetId) {
		this.fetId = fetId;
	}

	public String getFetIdColNm() {
		return this.fetIdColNm;
	}

	public void setFetIdColNm(String fetIdColNm) {
		this.fetIdColNm = fetIdColNm;
	}

	public String getFetVal() {
		return this.fetVal;
	}

	public void setFetVal(String fetVal) {
		this.fetVal = fetVal;
	}

	public String getFetValId() {
		return this.fetValId;
	}

	public void setFetValId(String fetValId) {
		this.fetValId = fetValId;
	}

	public String getUpdatedFetName() {
		return this.updatedFetName;
	}

	public void setUpdatedFetName(String updatedFetName) {
		this.updatedFetName = updatedFetName;
	}

	public BigDecimal getUpdatedFetId() {
		return this.updatedFetId;
	}

	public void setUpdatedFetId(BigDecimal updatedFetId) {
		this.updatedFetId = updatedFetId;
	}

	public String getUpdatedFetColNm() {
		return this.updatedFetColNm;
	}

	public void setUpdatedFetColNm(String updatedFetIdColNm) {
		this.updatedFetColNm = updatedFetIdColNm;
	}

	public String getUpdatedFetVal() {
		return this.updatedFetVal;
	}

	public void setUpdatedFetVal(String updatedFetVal) {
		this.updatedFetVal = updatedFetVal;
	}

	public BigDecimal getUpdatedFetValId() {
		return this.updatedFetValId;
	}

	public void setUpdatedFetValId(BigDecimal updatedFetValId) {
		this.updatedFetValId = updatedFetValId;
	}

	public String getRelation() {
		return this.relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Date getInsertionDate() {
		return this.insertionDate;
	}

	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}
	
	public void setExportStmt(String exportStmt){
		this.exportStmt = exportStmt;
	}
	
	public String getExportStmt(){
		return exportStmt;
	}

}