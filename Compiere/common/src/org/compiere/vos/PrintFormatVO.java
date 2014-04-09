package org.compiere.vos;

import java.util.ArrayList;

import org.compiere.vos.ResponseVO;

public class PrintFormatVO extends ResponseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name, description, isActive, isDefault,isLandscape;
	
	private int AD_PrintFontID=0;
	
	private int AD_PrintColorID=0, AD_PrintFormatID = 0,AD_Client_ID=-1,headerMargin=-1,footerMargin=-1,topMargin=-1,leftMargin=-1,rightMargin=-1,bottomMargin=-1;
	
	private double paperWidth=0.0,paperHeight=0.0;
	
	
	
	private ArrayList<PrintFormatItemVO> pf_items = new ArrayList<PrintFormatItemVO>();

	public PrintFormatVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<PrintFormatItemVO> getPf_items() {
		return pf_items;
	}

	public void setPf_items(ArrayList<PrintFormatItemVO> pf_items) {
		this.pf_items = pf_items;
	}

	public int getAD_PrintFormatID() {
		return AD_PrintFormatID;
	}
	
	public int getAD_PrintFontID() {
		return AD_PrintFontID;
	}
	
	public void setAD_PrintFontID(int printFontID) {
		AD_PrintFontID = printFontID;
	}
	
	public int getAD_PrintColorID() {
		return AD_PrintColorID;
	}
	
	public void setAD_PrintColorID(int printColorID) {
		AD_PrintColorID = printColorID;
	}

	public void setAD_PrintFormatID(int printFormatID) {
		AD_PrintFormatID = printFormatID;
	}

	public int getAD_Client_ID() {
		return AD_Client_ID;
	}

	public void setAD_Client_ID(int client_ID) {
		AD_Client_ID = client_ID;
	}

	public int getHeaderMargin() {
		return headerMargin;
	}

	public void setHeaderMargin(int headerMargin) {
		this.headerMargin = headerMargin;
	}

	public int getFooterMargin() {
		return footerMargin;
	}

	public void setFooterMargin(int footerMargin) {
		this.footerMargin = footerMargin;
	}

	public int getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public int getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	public int getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public String isLandscape() {
		return isLandscape;
	}

	public void setLandscape(String isLandscape) {
		this.isLandscape = isLandscape;
	}

	public double getPaperWidth() {
		return paperWidth;
	}

	public void setPaperWidth(double paperWidth) {
		this.paperWidth = paperWidth;
	}

	public double getPaperHeight() {
		return paperHeight;
	}

	public void setPaperHeight(double paperHeight) {
		this.paperHeight = paperHeight;
	}

}
