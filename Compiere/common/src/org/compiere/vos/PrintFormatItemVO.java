package org.compiere.vos;

public class PrintFormatItemVO  extends ResponseVO implements Comparable<PrintFormatItemVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrintFormatItemVO(){
		
	}
	
	private String name,printText,isActive,isPrinted,fieldGroupName,isOrderBy,isGroupBy,isAveraged,isCounted,isDeviationCalc,isMaxCalc,isMinCalc,IsSummarized,IsVarianceCalc,printFormatType, referenceName,printAreaType,isRelativePosition,imageURL,isNextLine,isNextPage,isSetNLPosition,fieldAlignmentType,lineAlignmentType;
	private String isHeightOneLine, isAscending, isRunningTotal, isCentrallyMaintained, isSuppressNull;
	private String isFixedWidth,isPageBreak, printNameSuffix, shapeType,imageIsAttached,isImageField,isFilledRectangle;
	private int printColorID, printFontID, recordSortNumber,adFieldGroupID,printFormatItemID,seqNo, AD_Reference_ID,xSpace,ySpace,maxHeight,maxWidth,xPosition,yPosition,fieldLength, runningTotalLines;	
    private int lineWidth,arcDiameter, AD_PrintFormatChild_ID;
    
	public int getAdFieldGroupID() {
		return adFieldGroupID;
	}

	public void setAdFieldGroupID(int adFieldGroupID) {
		this.adFieldGroupID = adFieldGroupID;
	}
	
	public int getAD_PrintFormatChild_ID() {
		return AD_PrintFormatChild_ID;
	}

	public void setAD_PrintFormatChild_ID(int AD_PrintFormatChild_ID) {
		this.AD_PrintFormatChild_ID = AD_PrintFormatChild_ID;
	}
	
	public String getImageIsAttached() {
		return imageIsAttached;
	}

	public void setImageIsAttached(String imageIsAttached2) {
		this.imageIsAttached = imageIsAttached2;
	}
	
	public String getIsCentrallyMaintained() {
		return isCentrallyMaintained;
	}

	public void setIsCentrallyMaintained(String isCentrallyMaintained) {
		this.isCentrallyMaintained = isCentrallyMaintained;
	}
	
	public String getIsSuppressNull() {
		return isSuppressNull;
	}

	public void setIsSuppressNull(String isSuppressNull) {
		this.isSuppressNull = isSuppressNull;
	}
	
	public String getShapeType() {
		return shapeType;
	}

	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}
	
	public String getIsFilledRectangle() {
		return isFilledRectangle;
	}

	public void setIsFilledRectangle(String isFilledRectangle) {
		this.isFilledRectangle = isFilledRectangle;
	}
	
	public int getArcDiameter() {
		return arcDiameter;
	}

	public void setArcDiameter(int arcDiameter) {
		this.arcDiameter = arcDiameter;
	}
	
	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	public String getIsImageField() {
		return isImageField;
	}

	public void setIsImageField(String isImageField) {
		this.isImageField = isImageField;
	}

	public String getIsPrinted() {
		return isPrinted;
	}

	public void setIsPrinted(String isPrinted) {
		this.isPrinted = isPrinted;
	}

	public int getPrintFormatItemID() {
		return printFormatItemID;
	}

	public void setPrintFormatItemID(int printFormatItemID) {
		this.printFormatItemID = printFormatItemID;
	}	

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public String getPrintText() {
		return printText;
	}

	public void setPrintText(String printText) {
		this.printText = printText;
	}

	public int getRecordSortNumber() {
		return recordSortNumber;
	}

	public void setRecordSortNumber(int recordSortNumber) {
		this.recordSortNumber = recordSortNumber;
	}	

	public String getFieldGroupName() {
		return fieldGroupName;
	}

	public void setFieldGroupName(String fieldGroupName) {
		this.fieldGroupName = fieldGroupName;
	}
	
	@Override
	public String toString(){
		return getName();
	}

	public String getIsAscending() {
		return isAscending;
	}

	public void setIsAscending(String isAscending) {
		this.isAscending = isAscending;
	}
	
	public String getIsRunningTotal() {
		return isRunningTotal;
	}

	public void setIsRunningTotal(String isRunningTotal) {
		this.isRunningTotal = isRunningTotal;
	}	
	
	
	public String getIsHeightOneLine() {
		return isHeightOneLine;
	}

	public void setIsHeightOneLine(String isHeightOneLine) {
		this.isHeightOneLine = isHeightOneLine;
	}
	
	public String getIsOrderBy() {
		return isOrderBy;
	}

	public void setIsOrderBy(String isOrderBy) {
		this.isOrderBy = isOrderBy;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	
	public int compareTo(PrintFormatItemVO o) {
		if (this.getAdFieldGroupID() != o.getAdFieldGroupID())
			return (this.getAdFieldGroupID()-o.getAdFieldGroupID());
		else if (this.seqNo != o.getSeqNo())
			return (this.getSeqNo()-o.getSeqNo());
		else
			return (this.getName().compareTo(o.getName()));		
	}

	public String getIsGroupBy() {
		return isGroupBy;
	}

	public void setIsGroupBy(String isGroupBy) {
		this.isGroupBy = isGroupBy;
	}

	public String getIsAveraged() {
		return isAveraged;
	}

	public void setIsAveraged(String isAveraged) {
		this.isAveraged = isAveraged;
	}

	public String getIsCounted() {
		return isCounted;
	}

	public void setIsCounted(String isCounted) {
		this.isCounted = isCounted;
	}

	public String getIsDeviationCalc() {
		return isDeviationCalc;
	}

	public void setIsDeviationCalc(String isDeviationCalc) {
		this.isDeviationCalc = isDeviationCalc;
	}

	public String getIsMaxCalc() {
		return isMaxCalc;
	}

	public void setIsMaxCalc(String isMaxCalc) {
		this.isMaxCalc = isMaxCalc;
	}

	public String getIsMinCalc() {
		return isMinCalc;
	}

	public void setIsMinCalc(String isMinCalc) {
		this.isMinCalc = isMinCalc;
	}

	public String getIsSummarized() {
		return IsSummarized;
	}

	public void setIsSummarized(String isSummarized) {
		IsSummarized = isSummarized;
	}

	public String getIsVarianceCalc() {
		return IsVarianceCalc;
	}

	public void setIsVarianceCalc(String isVarianceCalc) {
		IsVarianceCalc = isVarianceCalc;
	}

	public String getPrintFormatType() {
		return printFormatType;
	}

	public void setPrintFormatType(String printFormatType) {
		this.printFormatType = printFormatType;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public int getAD_Reference_ID() {
		return AD_Reference_ID;
	}

	public void setAD_Reference_ID(int reference_ID) {
		AD_Reference_ID = reference_ID;
	}

	public String getPrintAreaType() {
		return printAreaType;
	}

	public void setPrintAreaType(String printAreaType) {
		this.printAreaType = printAreaType;
	}

	public String getIsRelativePosition() {
		return isRelativePosition;
	}

	public void setIsRelativePosition(String isRelativePosition) {
		this.isRelativePosition = isRelativePosition;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getIsNextLine() {
		return isNextLine;
	}

	public void setIsNextLine(String isNextLine) {
		this.isNextLine = isNextLine;
	}

	public String getIsNextPage() {
		return isNextPage;
	}

	public void setIsNextPage(String isNextPage) {
		this.isNextPage = isNextPage;
	}

	public String getIsSetNLPosition() {
		return isSetNLPosition;
	}

	public void setIsSetNLPosition(String isSetNLPosition) {
		this.isSetNLPosition = isSetNLPosition;
	}

	public int getXSpace() {
		return xSpace;
	}

	public void setXSpace(int space) {
		xSpace = space;
	}

	public int getYSpace() {
		return ySpace;
	}

	public void setYSpace(int space) {
		ySpace = space;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public String getFieldAlignmentType() {
		return fieldAlignmentType;
	}

	public void setFieldAlignmentType(String fieldAlignmentType) {
		this.fieldAlignmentType = fieldAlignmentType;
	}

	public int getXPosition() {
		return xPosition;
	}

	public void setXPosition(int position) {
		xPosition = position;
	}

	public int getYPosition() {
		return yPosition;
	}

	public void setYPosition(int position) {
		yPosition = position;
	}

	public String getLineAlignmentType() {
		return lineAlignmentType;
	}

	public void setLineAlignmentType(String lineAlignmentType) {
		this.lineAlignmentType = lineAlignmentType;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	public void setAD_PrintFontID(int AD_PrintFontID) {
		printFontID = AD_PrintFontID;
		
	}
	public int getAD_PrintFontID()
	{
		return printFontID;
	}
	
	public void setAD_PrintColorID(int AD_PrintColorID) {
		printColorID = AD_PrintColorID;
		
	}
	public int getAD_PrintColorID()
	{
		return printColorID;
	}

	public void setIsFixedWidth(String isFixedWidth) {
		this.isFixedWidth = isFixedWidth;		
	}
	
	public String getIsFixedWidth() {
		return isFixedWidth;		
	}

	public void setIsPageBreak(String isPageBreak) {
		this.isPageBreak = isPageBreak;		
	}
	
	public String getIsPageBreak() {
		return isPageBreak;		
	}

	public void setRunningTotalLines(int runningTotalLines) {
		this.runningTotalLines = runningTotalLines;		
	}
	
	public int getRunningTotalLines() {
		return runningTotalLines ;		
	}

	public void setPrintNameSuffix(String printNameSuffix) {
		this.printNameSuffix = printNameSuffix;		
	}
	
	public String getPrintNameSuffix() {
		return printNameSuffix;		
	}
}
