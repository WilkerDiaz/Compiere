package org.compiere.excel;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.*;

import javax.swing.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.apache.poi.poifs.filesystem.*;
import org.compiere.util.*;

/**
 * Allow users to create Excel files from Compiere Data
 */
public class Excel {
	
	protected static CLogger s_log = CLogger.getCLogger(Excel.class);

	public static final short CELLSTYLE_HEADER = HSSFCellStyle.BORDER_THICK;
	public static final short CELLSTYLE_NONE = (short)-1;
	
	protected HSSFWorkbook wb;
	protected HSSFSheet sheet;
	protected HSSFDataFormat format;
    
	protected HSSFCellStyle cellStyleNormal;
	protected HSSFCellStyle cellStyleDate;
	protected HSSFCellStyle cellStyleNumber;
	protected HSSFCellStyle cellStyleInteger;
	
	protected FileOutputStream fileOut;
	protected String fileName;
	
	protected HSSFCellStyle HEADER;	

	public static final int DISPLAY_TYPE_STRING = 0;
	public static final int DISPLAY_TYPE_NUMBER = 10;
	public static final int DISPLAY_TYPE_INTEGER = 15;
	public static final int DISPLAY_TYPE_DATE 	= 20;	

	/**
	 * Open an Excel file in Office using JDIC
	 */
	/*public static boolean open(String fileName){
		boolean ok = false;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				Desktop.open(file);
				ok = true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ok;
	}*/

	/**
	 * Print an Excel file
	 */
	/*public static boolean print(String fileName){
		boolean ok = false;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				if (Desktop.isPrintable(file)) {
					Desktop.print(file);
					ok = true;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ok;
	}*/

	/*public void open(){
		Excel.open(fileName);
	}*/
	
	/**
	 * Create Excel file	  
	 * @param fileName
	 */
	public Excel(String fileName){
		this();	    
		this.fileName = fileName;	   
	    try {
			fileOut = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create Excel by passing in a Java file object	 
	 * @param file
	 */
	public Excel(File file){
		this();
		setFileOutputStream(file);
	}
	
	
	public Excel(){
		wb = new HSSFWorkbook();
	    sheet = wb.createSheet("Export Compiere");
	    sheet.setDefaultColumnWidth((short)25);

		this.format = wb.createDataFormat();
	    
	    cellStyleNormal = wb.createCellStyle();
	    cellStyleDate = wb.createCellStyle();
	    
	    //cellStyleDate.setDataFormat(format.getFormat("dd/MM/yy"));
	    cellStyleDate.setDataFormat(format.getFormat(Env.getLanguage(Env.getCtx()).getDateFormat().toPattern()));
	    cellStyleNumber = wb.createCellStyle();
    	//cellStyleNumber.setDataFormat(format.getFormat("#,##0.0#")); Comentado por BECO
	    
	    //Agregado por por BECO 
	    if(1 == Env.getCtx().getContextAsInt("#EXCEL9DECIMALPLACES"))
	    	cellStyleNumber.setDataFormat(format.getFormat("#,##0.0##########"));    
	    else
	    	cellStyleNumber.setDataFormat(format.getFormat("#,##0.0#"));
	    
	    cellStyleInteger = wb.createCellStyle();
	    cellStyleInteger.setDataFormat(format.getFormat("#,##0"));	    
		
	    HSSFFont headerFont = wb.createFont();
	    headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    
	    HEADER = wb.createCellStyle();
	    HEADER.setFont(headerFont);
	    //HEADER.setFillBackgroundColor(HSSFColor.LIGHT_ORANGE.index);
	    HEADER.setFillForegroundColor(HSSFColor.WHITE.index);
	    HEADER.setFillPattern(HSSFCellStyle.FINE_DOTS);   
	}
	
	
	public void setFileOutputStream(File f){
		try {
			fileOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the Excel file.	 
	 */
	public void close(){
		try {
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create new Row or Cell.	 
	 * @param rowPos Position for the Row
	 * @param cellPos Position in the Cell
	 * @param s Object to be rendered
	 * @param str Object en format String si jamais l'Objet ne peut etre interpreter
	 * @param cellStyle : HEADER or NONE
	 * @param displayType : String/Number/Date
	 */
	public void createRow(short rowPos, short cellPos, Object s, String str, short cellStyle, int displayType) {
		HSSFRow row = sheet.createRow(rowPos);
		HSSFCell cell = row.createCell(cellPos);
		if (cellStyle == CELLSTYLE_HEADER) {
			cell.setCellStyle(HEADER);
		}
		else {
		    switch (displayType) {
			case DISPLAY_TYPE_DATE:
			    cell.setCellStyle(cellStyleDate);
				break;
			case DISPLAY_TYPE_NUMBER:
			    cell.setCellStyle(cellStyleNumber);
				break;
			case DISPLAY_TYPE_INTEGER:
				cell.setCellStyle(cellStyleInteger);
				break;
			default:
				cell.setCellStyle(cellStyleNormal);
				break;
			}
		    
		}
		if (s instanceof String)
		{	HSSFRichTextString hrts = new HSSFRichTextString((String)s);
			cell.setCellValue(hrts);
		}
		else if (s instanceof BigDecimal)
			cell.setCellValue(((BigDecimal)s).doubleValue());
		else if (s instanceof Date)
			cell.setCellValue((Date)s);
		else {
			HSSFRichTextString hrts = new HSSFRichTextString(str);
			cell.setCellValue(hrts);
		}
	}
	
	/**
	 * Agregado por Javier Pino
	 * Centrobeco C. A.
	 * Crea una nueva hoja, y la coloca como hoja actual  
	 */
	public void createAndSetSheet (String name) {
		sheet = wb.createSheet(name);
	    sheet.setDefaultColumnWidth((short)25);		
	}
	
	/** 
	 * Lecture d'un fichier Excel
	 */
	public static Map<Integer, Map<Integer,String>> Parsing(String fileName) throws IOException 
	{				
		InputStream input = new FileInputStream(fileName);
		POIFSFileSystem fs = new POIFSFileSystem( input );
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<Integer,Map<Integer,String>> data = new HashMap<Integer,Map<Integer,String>>();		
		Iterator<?> rows = sheet.rowIterator(); 
		while( rows.hasNext() ) { 
			 Map<Integer,String> Columndata = new HashMap<Integer,String>();
		     HSSFRow row = (HSSFRow) rows.next();		     		     
		     short c1 = row.getFirstCellNum();
		     short c2 = row.getLastCellNum();
		     for (short c=c1; c<c2; c++) { 
		          HSSFCell cell = row.getCell(c);
		          String result = null;
		          if (cell != null) 
		          {		        	  
		       	      int cellType = cell.getCellType();		       	      
		       	      switch (cellType) {
		       	      case HSSFCell.CELL_TYPE_BLANK:
		       	        result = "";
		       	        break;
		       	      case HSSFCell.CELL_TYPE_BOOLEAN:
		       	        result = cell.getBooleanCellValue() ?
		       	          "Y" : "N";
		       	        break;
		       	      case HSSFCell.CELL_TYPE_FORMULA:
		       	        result = cell.getCellFormula();
		       	        break;
		       	      case HSSFCell.CELL_TYPE_NUMERIC:
		       	          HSSFCellStyle cellStyle = cell.getCellStyle();
		       	          short dataFormat = cellStyle.getDataFormat();	          
		       	          if (dataFormat == 15) {
		       	            result = cell.getDateCellValue().toString();
		       	          } else {
		       	            result = String.valueOf (
		       	              cell.getNumericCellValue());
		       	          }

		       	          break;
		       	       case HSSFCell.CELL_TYPE_STRING:
		       	          result = cell.getRichStringCellValue().getString();
		       	          break;
		       	        default: result = cell.getRichStringCellValue().getString();
		       	       }	
		       	      if (!result.equals(""))
		       	    	  Columndata.put(new Integer(c),result);
		            
		          }
		         
		     }		     
		     if (!Columndata.isEmpty())
		         data.put(new Integer(row.getRowNum()),Columndata);
		}		
		return data;
	}

	/**
	 * Export Excel 	 
	 * @param excel
	 * @param table
	 * @return
	 */
	public Excel createExcel(JTable table) {
		try {
			//	for all rows (-1 = header row)
			for (int row = -1; row < table.getRowCount(); row++) {
				int colPos = 0;
				for (int col = 0; col < table.getColumnCount(); col++){
					Object item = table.getValueAt(row, col);
					//if (item.isPrinted()){
						//	header row
						if (row == -1) {
							this.createRow(
									(short)0, 
									(short)colPos, 
									table.getColumnName(col),
									null,
									Excel.CELLSTYLE_HEADER,
									Excel.DISPLAY_TYPE_STRING);
						}
						else
						{
							int displayType = Excel.DISPLAY_TYPE_STRING;
							Object obj = item;
							Object data = "";
							String dataStr = "";
							if (obj == null)
								;
							else if (obj instanceof KeyNamePair) {
								dataStr = ((org.compiere.util.KeyNamePair)obj).getName();
								data = dataStr;
							}
							else if (obj instanceof String) {
								dataStr = (String)obj;
								data = dataStr;
							}
							else if (obj instanceof Timestamp) {
								data = obj;
								displayType = Excel.DISPLAY_TYPE_DATE;
							}
							else if (obj instanceof BigDecimal) {
								data = obj;
								displayType = Excel.DISPLAY_TYPE_NUMBER;
							}
							else
								s_log.log(Level.SEVERE, "Appelez Vincent avec l'info " + obj.getClass());
							this.createRow(
									(short)(row + 1), 
									(short)colPos, 
									data, 
									dataStr,
									Excel.CELLSTYLE_NONE,
									displayType);
							
						}
						colPos++;
				}	//	for all columns
			}	//	for all rows
		}
		catch (Exception e){
			s_log.log(Level.SEVERE, "createExcel(w)", e);
		}
		this.close();
		return this;
	}	
}