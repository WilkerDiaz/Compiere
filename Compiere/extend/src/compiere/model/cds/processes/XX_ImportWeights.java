package compiere.model.cds.processes;

import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import compiere.model.cds.X_XX_VMR_WeightAssigned;

public class XX_ImportWeights extends SvrProcess {

	static CLogger log = CLogger.getCLogger(XX_ImportWeights.class);
	private String wByLine = null;
	private String wBySection = null;
	private String wByLineConcept = null;
	private String wByDepBrand = null;
	private String wByStoreMonth = null;
	boolean badWBySec = false; 
	boolean badWByLin = false;
	boolean badWByLinConcept = false;
	boolean badWByDepBrand = false;
	boolean badWByStoreMonth = false;
	String msgWBySec = "";
	String msgWByLin = "";
	String msgWByLinConcept = "";
	String msgWByDepBrand = "";
	String msgWByStoreMonth = "";
	Vector<Integer> departmentIDs = new Vector<Integer>();
	Vector<String> departmentCodes = new Vector<String>();
	Vector<Integer> lineIDs = new Vector<Integer>();
	Vector<Integer> lineDepIDs = new Vector<Integer>();
	Vector<String> lineCodes = new Vector<String>();
	Vector<Integer> sectionIDs = new Vector<Integer>();
	Vector<Integer> sectionLinIDs = new Vector<Integer>();
	Vector<String> sectionCodes = new Vector<String>();
	Vector<String> conceptNames = new Vector<String>();
	Vector<Integer> conceptIDs = new Vector<Integer>();
	Vector<String> brandCodes = new Vector<String>();
	Vector<Integer> brandIDs = new Vector<Integer>();
	Vector<String> storeCodes = new Vector<String>();
	Vector<Integer> storeIDs = new Vector<Integer>();
	Trx transWBySection = Trx.get("trans");
	Trx transWByLine = Trx.get("trans");
	Trx transWByLineConcept = Trx.get("trans");
	Trx transWByDepBrand = Trx.get("trans");
	Trx transWByStoreMonth = Trx.get("trans");
	
	@Override
	protected String doIt() throws Exception {
		
		if(wBySection==null && wByLine==null && wByLineConcept==null && wByDepBrand==null && wByStoreMonth==null){
			return "";
		}
		else{
			getAllDeparments();
			getAllLines();
		}
		
		if(wBySection!=null){
			System.out.println(wBySection);
			if(!wBySection.substring(wBySection.length()-4, wBySection.length()).equals(".xls")){
				badWBySec=true;
				
				if(wBySection.substring(wBySection.length()-5, wBySection.length()).equals(".xlsx")){
					msgWBySec=Msg.translate( getCtx(), "Excel Earlier Format");;	
				}else{
					msgWBySec=Msg.translate( getCtx(), "Not Excel");
				}
			}else{
				setInputFile(wBySection);
				getAllSections();
				readBySection();	
			}
		}else{
			msgWBySec += Msg.translate( getCtx(), "File Not Loaded");
		}
		
		if(wByLine!=null){
			System.out.println(wByLine);
			if(!wByLine.substring(wByLine.length()-4, wByLine.length()).equals(".xls")){
				badWByLin=true;
				if(wByLine.substring(wByLine.length()-5, wByLine.length()).equals(".xlsx")){
					msgWByLin=Msg.translate( getCtx(), "Excel Earlier Format");;	
				}else{
					msgWByLin=Msg.translate( getCtx(), "Not Excel");
				}
			}else{
				setInputFile(wByLine);
				readByLine();	
			}
		}else{
			msgWByLin += Msg.translate( getCtx(), "File Not Loaded");
		}
		
		if(wByLineConcept!=null){
			System.out.println(wByLineConcept);
			if(!wByLineConcept.substring(wByLineConcept.length()-4, wByLineConcept.length()).equals(".xls")){
				badWByLinConcept=true;
				if(wByLineConcept.substring(wByLineConcept.length()-5, wByLineConcept.length()).equals(".xlsx")){
					msgWByLinConcept=Msg.translate( getCtx(), "Excel Earlier Format");	
				}else{
					Msg.translate( getCtx(), "Not Excel");
					msgWByLinConcept=Msg.translate( getCtx(), "Not Excel");
				}
			}else{
				setInputFile(wByLineConcept);
				getAllConcepts();
				readByLineConcept();
			}
		}else{
			msgWByLinConcept += Msg.translate( getCtx(), "File Not Loaded");
		}
		
		if(wByDepBrand!=null){
			System.out.println(wByDepBrand);
			if(!wByDepBrand.substring(wByDepBrand.length()-4, wByDepBrand.length()).equals(".xls")){
				badWByDepBrand=true;
				if(wByDepBrand.substring(wByDepBrand.length()-5, wByDepBrand.length()).equals(".xlsx")){
					msgWByDepBrand=Msg.translate( getCtx(), "Excel Earlier Format");
				}else{
					msgWByDepBrand=Msg.translate( getCtx(), "Not Excel");
				}
			}else{
				setInputFile(wByDepBrand);
				getAllBrands();
				readByDepBrand();	
			}
		}else{
			msgWByDepBrand += Msg.translate( getCtx(), "File Not Loaded");
		}
		
		if(wByStoreMonth!=null){
			System.out.println(wByStoreMonth);
			if(!wByStoreMonth.substring(wByStoreMonth.length()-4, wByStoreMonth.length()).equals(".xls")){
				badWByStoreMonth=true;
				if(wByStoreMonth.substring(wByStoreMonth.length()-5, wByStoreMonth.length()).equals(".xlsx")){
					msgWByStoreMonth=Msg.translate( getCtx(), "Excel Earlier Format");
				}else{
					msgWByStoreMonth=Msg.translate( getCtx(), "Not Excel");
				}
			}else{
				setInputFile(wByStoreMonth);
				
				if(wBySection==null){
					getAllSections();
				}
				
				getAllStores();
				readByStoreMonth();	
			}
		}else{
			msgWByStoreMonth += Msg.translate( getCtx(), "File Not Loaded");
		}
		
		//Mensaje de error de formato y borro y guardo los registros
		
		if(badWBySec){
			//msgWBySec += "Error";
			transWBySection.rollback();
		}
		else if(wBySection!=null){
			deleteAllWbySec();
			transWBySection.commit();
			msgWBySec += "Ok";
		}
		
		if(badWByLin){
			//msgWByLin += "Error";
			transWByLine.rollback();	
		}
		else if(wByLine!=null){
			deleteAllWbyLin();
			transWByLine.commit();
			msgWByLin += "Ok";
		}
		
		if(badWByLinConcept){
			//msgWByLinConcept += "Error";
			transWByLineConcept.rollback();
		}
		else if(wByLineConcept!=null){
			deleteAllWbyLinConcept();
			transWByLineConcept.commit();
			msgWByLinConcept += "Ok";
		}
		
		if(badWByDepBrand){
			//msgWByLinConcept += "Error";
			transWByDepBrand.rollback();
		}
		else if(wByDepBrand!=null){
			deleteAllWbyDepBrand();
			transWByDepBrand.commit();
			msgWByDepBrand += "Ok";
		}
		
		if(badWByStoreMonth){
			//msgWByLinConcept += "Error";
			transWByStoreMonth.rollback();
		}
		else if(wByStoreMonth!=null){
			deleteAllWbyStore();
			transWByStoreMonth.commit();
			msgWByStoreMonth += "Ok";
		}
		
		//Si algunas de las cargas dio error
		if(badWBySec || badWByLin || badWByLinConcept || badWByDepBrand || badWByStoreMonth){
			ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "Weight Message", new String[] {msgWBySec, msgWByLin, msgWByLinConcept, msgWByDepBrand, msgWByStoreMonth}));
			return Msg.translate( getCtx(), "Format Error");
		}
		else{
			ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "Weight Message", new String[] {msgWBySec, msgWByLin, msgWByLinConcept, msgWByDepBrand, msgWByStoreMonth}));
			return Msg.translate( getCtx(), "Weights Loaded	");
		}
	}

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		int i=0;
		for (ProcessInfoParameter element : parameter) {
			
			i++;
			if (i==1 && element.getParameter()!=null)
				wBySection = element.getParameter().toString();
			else if (i==2 && element.getParameter()!=null)
				wByLine = element.getParameter().toString();
			else if (i==3 && element.getParameter()!=null)
				wByLineConcept = element.getParameter().toString();
			else if (i==4 && element.getParameter()!=null)
				wByDepBrand = element.getParameter().toString();
			else if (i==5 && element.getParameter()!=null)
				wByStoreMonth = element.getParameter().toString();
		}
		
	}
	
	private String inputFile;

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void readBySection() throws IOException  {
		
		File inputWorkbook = new File(inputFile);
		Workbook w;
		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			
			//int defaultRows = sheet.getRows();
			
			//Si la cantidad de columnas es 4
			if(sheet.getColumns()==4 && sheet.getRows()>1){
		
				//Valido que las cabeceras tengan el formato correcto
				if(!sheet.getCell(0, 0).getContents().equals("CODDEP") || !sheet.getCell(1, 0).getContents().equals("CODLIN") || !sheet.getCell(2, 0).getContents().equals("CODSEC") || !sheet.getCell(3, 0).getContents().equals("PESO"))
				{	badWBySec=true;
					msgWBySec = Msg.translate( getCtx(), "Column Names");}
				
				if(!badWBySec){
					
					for (int i = 1; i < sheet.getRows(); i++) {
					
						//Capturo el departamento
						String department = sheet.getCell(0, i).getContents();
						int departmentID = getDepartmentID(department);
						
						if(departmentID==0){
							
							
							
							msgWBySec += Msg.translate( getCtx(), "Cell A Error")+(i+1);
							badWBySec=true;
							break;
						}
						
						//Capturo la linea
						String line = sheet.getCell(1, i).getContents();
						int lineID = getLineID(line,departmentID);
						
						if(lineID==0){
							msgWBySec += Msg.translate( getCtx(), "Cell B Error")+(i+1);
							badWBySec=true;
							break;
						}
						
						//Capturo la seccion
						String section = sheet.getCell(2, i).getContents();
						int sectionID = getSectionID(section,lineID);
						
						if(sectionID==0){
							msgWBySec += Msg.translate( getCtx(), "Cell C Error")+(i+1);
							badWBySec=true;
							break;
						}
						
						//capturo el peso
						if(sheet.getCell(3, i).getContents().contains(",")){
							msgWBySec += Msg.translate( getCtx(), "Cell D Error")+(i+1)+Msg.translate( getCtx(), "Weight too Large");
							badWBySec = true;
							break;
						}
						
						NumberCell aux = (NumberCell) sheet.getCell(3,i);
						BigDecimal weightValue = new BigDecimal(aux.getValue());
						weightValue = weightValue.setScale (9,BigDecimal.ROUND_HALF_UP);
						
						if(weightValue.compareTo(new BigDecimal(100))==1){
							msgWBySec += Msg.translate( getCtx(), "Cell D Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWBySec=true;
							break;
						}
						else if(weightValue.compareTo(new BigDecimal(0))==-1){
							msgWBySec += Msg.translate( getCtx(), "Cell D Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Small");
							badWBySec = true;
							break;
						}
						
						//Seteo los valores
						X_XX_VMR_WeightAssigned weight = new X_XX_VMR_WeightAssigned( getCtx(), 0, transWBySection);				
						weight.setXX_VMR_Department_ID(departmentID);
						weight.setXX_VMR_Line_ID(lineID);
						weight.setXX_VMR_Section_ID(sectionID);
						weight.setWeight(weightValue);
						weight.setXX_WeightType(1);
						weight.save(); 
					}
					
				}
				
			}
			else{
				msgWBySec += Msg.translate( getCtx(), "4 Columns");
				badWBySec = true;
			}
			
		} catch (BiffException e){
			badWBySec = true;
			log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void readByLine() throws IOException  {
		
		File inputWorkbook = new File(inputFile);
		Workbook w;
		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
						
			//Si la cantidad de columnas es 3
			if(sheet.getColumns()==3 && sheet.getRows()>1){
		
				//Valido que las cabeceras tengan el formato correcto
				if(!sheet.getCell(0, 0).getContents().equals("CODDEP") || !sheet.getCell(1, 0).getContents().equals("CODLIN") || !sheet.getCell(2, 0).getContents().equals("PESO"))
				{	badWByLin=true;
					msgWByLin = Msg.translate( getCtx(), "Column Names");}
				
				if(!badWByLin){
					
					for (int i = 1; i < sheet.getRows(); i++) {
					
						//Capturo el departamento
						String department = sheet.getCell(0, i).getContents();
						int departmentID = getDepartmentID(department);
						
						if(departmentID==0){
							msgWByLin += Msg.translate( getCtx(), "Cell A Error")+(i+1);
							badWByLin=true;
							break;
						}
						
						//Capturo la linea
						String line = sheet.getCell(1, i).getContents();
						int lineID = getLineID(line,departmentID);
						
						if(lineID==0){
							msgWByLin += Msg.translate( getCtx(), "Cell B Error")+(i+1);
							badWByLin=true;
							break;
						}
						
						//capturo el peso
						if(sheet.getCell(2, i).getContents().contains(",")){
							msgWByLin += Msg.translate( getCtx(), "Cell C Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWByLin=true;
							break;
						}
						
						NumberCell aux = (NumberCell) sheet.getCell(2,i);
						BigDecimal weightValue = new BigDecimal(aux.getValue());
						weightValue = weightValue.setScale (9,BigDecimal.ROUND_HALF_UP);
						
						if(weightValue.compareTo(new BigDecimal(100))==1){
							msgWByLin += Msg.translate( getCtx(), "Cell C Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWByLin=true;
							break;
						}
						else if(weightValue.compareTo(new BigDecimal(0))==-1){
							msgWByLin += Msg.translate( getCtx(), "Cell C Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Small");
							badWByLin=true;
							break;
						}
						
						//Seteo los valores
						X_XX_VMR_WeightAssigned weight = new X_XX_VMR_WeightAssigned( getCtx(), 0, transWByLine);				
						weight.setXX_VMR_Department_ID(departmentID);
						weight.setXX_VMR_Line_ID(lineID);
						weight.setWeight(weightValue);
						weight.setXX_WeightType(2);
						weight.save();
					}
					
				}
				
			}
			else{
				
				msgWByLin += Msg.translate( getCtx(), "3 Columns");
				badWByLin = true;
			}
			
		} catch (BiffException e){
			badWByLin = true;
			log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void readByLineConcept() throws IOException  {
		
		File inputWorkbook = new File(inputFile);
		Workbook w;
		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			
			//int defaultRows = sheet.getRows();
			
			//Si la cantidad de columnas es 3
			if(sheet.getColumns()==4 && sheet.getRows()>1){
					
				//Valido que las cabeceras tengan el formato correcto
				if(!sheet.getCell(0, 0).getContents().equals("CODDEP") || !sheet.getCell(1, 0).getContents().equals("CODLIN") || !sheet.getCell(2, 0).getContents().equals("CONCEPTO") || !sheet.getCell(3, 0).getContents().equals("PESO"))
				{	badWByLinConcept=true;
				    msgWByLinConcept = Msg.translate( getCtx(), "Column Names");}
				
				if(!badWByLinConcept){
					
					for (int i = 1; i < sheet.getRows(); i++) {
					
						//Capturo el departamento
						String department = sheet.getCell(0, i).getContents();
						int departmentID = getDepartmentID(department);
						
						if(departmentID==0){
							msgWByLinConcept += Msg.translate( getCtx(), "Cell A Error")+(i+1);
							badWByLinConcept=true;
							break;
						}
						
						//Capturo la linea
						String line = sheet.getCell(1, i).getContents();
						int lineID = getLineID(line,departmentID);
						
						if(lineID==0){
							msgWByLinConcept += Msg.translate( getCtx(), "Cell B Error")+(i+1);
							badWByLinConcept=true;
							break;
						}
						
						//Capturo el concepto
						String concept = sheet.getCell(2, i).getContents();
						int conceptID = getConceptID(concept);
						
						if(conceptID==0){
							msgWByLinConcept += Msg.translate( getCtx(), "Cell C Error")+(i+1);
							badWByLinConcept=true;
							break;
						}
						
						//capturo el peso
						if(sheet.getCell(3, i).getContents().contains(",")){
							msgWByLinConcept += Msg.translate( getCtx(), "Cell D Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWByLinConcept=true;
							break;
						}
						
						NumberCell aux = (NumberCell) sheet.getCell(3,i);
						BigDecimal weightValue = new BigDecimal(aux.getValue());
						weightValue = weightValue.setScale (9,BigDecimal.ROUND_HALF_UP);
						
						if(weightValue.compareTo(new BigDecimal(100))==1){
							msgWByLinConcept += Msg.translate( getCtx(), "Cell D Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWByLinConcept=true;
							break;
						}
						else if(weightValue.compareTo(new BigDecimal(0))==-1){
							msgWByLinConcept += Msg.translate( getCtx(), "Cell D Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Small");
							badWByLinConcept=true;
							break;
						}
						
						//Seteo los valores
						X_XX_VMR_WeightAssigned weight = new X_XX_VMR_WeightAssigned( getCtx(), 0, transWByLineConcept);				
						weight.setXX_VMR_Department_ID(departmentID);
						weight.setXX_VMR_Line_ID(lineID);
						weight.setXX_VME_ConceptValue_ID(conceptID);
						weight.setWeight(weightValue);
						weight.setXX_WeightType(3);
						weight.save();
					}
					
				}
				
			}
			else{
				
				msgWByLinConcept += Msg.translate( getCtx(), "4 Columns");
				badWByLinConcept = true;
			}
			
		} catch (BiffException e){
			badWByLinConcept = true;
			log.log(Level.SEVERE, e.getMessage());
		}
	}

	public void readByDepBrand() throws IOException  {
		
		File inputWorkbook = new File(inputFile);
		Workbook w;
		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
						
			//Si la cantidad de columnas es 3
			if(sheet.getColumns()==3 && sheet.getRows()>1){
		
				//Valido que las cabeceras tengan el formato correcto
				if(!sheet.getCell(0, 0).getContents().equals("CODDEP") || !sheet.getCell(1, 0).getContents().equals("MARCA") || !sheet.getCell(2, 0).getContents().equals("PESO")){
					badWByDepBrand=true;
					msgWByDepBrand += Msg.translate( getCtx(), "Column Names");
				}
				
				if(!badWByDepBrand){
					
					for (int i = 1; i < sheet.getRows(); i++) {
					
						//Capturo el departamento
						String department = sheet.getCell(0, i).getContents();
						int departmentID = getDepartmentID(department);
						
						if(departmentID==0){
							msgWByDepBrand += Msg.translate( getCtx(), "Cell A Error")+(i+1);
							badWByDepBrand=true;
							break;
						}
						
						//Capturo la marca
						String brand = sheet.getCell(1, i).getContents();
						int brandID = getBrandID(brand);
						
						if(brandID==0){
							msgWByDepBrand += Msg.translate( getCtx(), "Cell B Error")+(i+1);
							badWByDepBrand=true;
							break;
						}
						
						//capturo el peso
						if(sheet.getCell(2, i).getContents().contains(",")){
							msgWByDepBrand += Msg.translate( getCtx(), "Cell C Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWByDepBrand=true;
							break;
						}
						
						NumberCell aux = (NumberCell) sheet.getCell(2,i);
						BigDecimal weightValue = new BigDecimal(aux.getValue());
						weightValue = weightValue.setScale (9,BigDecimal.ROUND_HALF_UP);
						
						if(weightValue.compareTo(new BigDecimal(100))==1){
							msgWByDepBrand += Msg.translate( getCtx(), "Cell C Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWByDepBrand=true;
							break;
						}
						else if(weightValue.compareTo(new BigDecimal(0))==-1){
							msgWByDepBrand += Msg.translate( getCtx(), "Cell C Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Small");
							badWByDepBrand=true;
							break;
						}
						
						//Seteo los valores
						X_XX_VMR_WeightAssigned weight = new X_XX_VMR_WeightAssigned( getCtx(), 0, transWByDepBrand);				
						weight.setXX_VMR_Department_ID(departmentID);
						weight.setXX_VMR_Brand_ID(brandID);
						weight.setWeight(weightValue);
						weight.setXX_WeightType(4);
						weight.save();
					}
					
				}
				
			}
			else{
				msgWByDepBrand += Msg.translate( getCtx(), "3 Columns");
				badWByDepBrand = true;
			}
			
		} catch (BiffException e){
			badWByDepBrand = true;
			log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void readByStoreMonth() throws IOException  {
		
		File inputWorkbook = new File(inputFile);
		Workbook w;
		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			
			//Si la cantidad de columnas es 6
			if(sheet.getColumns()==6 && sheet.getRows()>1){
		
				//Valido que las cabeceras tengan el formato correcto
				if(!sheet.getCell(0, 0).getContents().equals("CODDEP") || !sheet.getCell(1, 0).getContents().equals("CODLIN") || !sheet.getCell(2, 0).getContents().equals("CODSEC") || !sheet.getCell(3, 0).getContents().equals("TIENDA") || !sheet.getCell(4, 0).getContents().equals("MES") || !sheet.getCell(5, 0).getContents().equals("PESO"))
				{	badWByStoreMonth=true;
					msgWByStoreMonth = Msg.translate( getCtx(), "Column Names");}
				
				if(!badWByStoreMonth){
					
					for (int i = 1; i < sheet.getRows(); i++) {
					
						//Capturo el departamento
						String department = sheet.getCell(0, i).getContents();
						int departmentID = getDepartmentID(department);
						
						if(departmentID==0){
							
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell A Error")+(i+1);
							badWByStoreMonth=true;
							break;
						}
						
						//Capturo la linea
						String line = sheet.getCell(1, i).getContents();
						int lineID = getLineID(line,departmentID);
						
						if(lineID==0){
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell B Error")+(i+1);
							badWByStoreMonth=true;
							break;
						}
						
						//Capturo la seccion
						String section = sheet.getCell(2, i).getContents();
						int sectionID = getSectionID(section,lineID);
						
						if(sectionID==0){
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell C Error")+(i+1);
							badWByStoreMonth=true;
							break;
						}
						
						//Capturo la Tienda
						String store = sheet.getCell(3, i).getContents();
						int storeID = getStoreID(store);
						
						if(sectionID==0){
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell D Error")+(i+1);
							badWByStoreMonth=true;
							break;
						}
						
						//Capturo el Mes
						String month = sheet.getCell(4, i).getContents();
						if(month.length()==1)
							month = "0" + month;
						
						if(!month.equals("01") && !month.equals("02") && !month.equals("03") && !month.equals("04") && !month.equals("05") && !month.equals("06") && !month.equals("07") && !month.equals("08") && !month.equals("09") && !month.equals("10") && !month.equals("11") && !month.equals("12")){
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell E Error")+(i+1);
							badWByStoreMonth=true;
							break;
						}
						
						//capturo el peso

						
						if(sheet.getCell(5, i).getContents().contains(",")){
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell F Error")+(i+1)+Msg.translate( getCtx(), "Weight too Large");
							badWByStoreMonth = true;
							break;
						}
						
						NumberCell aux = (NumberCell) sheet.getCell(5,i);
						
						BigDecimal weightValue = new BigDecimal(aux.getValue());
						weightValue = weightValue.setScale (9,BigDecimal.ROUND_HALF_UP);
						
						if(weightValue.compareTo(new BigDecimal(100))==1){
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell F Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Large");
							badWByStoreMonth=true;
							break;
						}
						else if(weightValue.compareTo(new BigDecimal(0))==-1){
							msgWByStoreMonth += Msg.translate( getCtx(), "Cell F Error")+(i+1)+ Msg.translate( getCtx(), "Weight too Small");
							badWByStoreMonth = true;
							break;
						}
						
						//Seteo los valores
						X_XX_VMR_WeightAssigned weight = new X_XX_VMR_WeightAssigned( getCtx(), 0, transWByStoreMonth);				
						weight.setXX_VMR_Department_ID(departmentID);
						weight.setXX_VMR_Line_ID(lineID);
						weight.setXX_VMR_Section_ID(sectionID);
						weight.setXX_Store_ID(storeID);
						weight.setXX_Month(month);
						weight.setWeight(weightValue);
						weight.setXX_WeightType(5);
						weight.save(); 
					}
					
				}
				
			}
			else{
				msgWByStoreMonth += Msg.translate( getCtx(), "5 Columns");
				badWByStoreMonth = true;
			}
			
		} catch (BiffException e){
			badWByStoreMonth = true;
			log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private void getAllDeparments(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID " +
				     "FROM XX_VMR_DEPARTMENT WHERE ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				departmentCodes.add(rs.getString("VALUE"));
				departmentIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllLines(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
				     "FROM XX_VMR_LINE WHERE " +
				     //"ISACTIVE='Y' AND " + 
				     "AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				lineCodes.add(rs.getString("VALUE"));
				lineIDs.add(rs.getInt("XX_VMR_LINE_ID"));
				lineDepIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllSections(){
		
		String sql = "SELECT XX_VMR_LINE_ID, VALUE, XX_VMR_SECTION_ID " +
				     "FROM XX_VMR_SECTION WHERE ISACTIVE='Y' " + 
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				sectionCodes.add(rs.getString("VALUE"));
				sectionIDs.add(rs.getInt("XX_VMR_SECTION_ID"));
				sectionLinIDs.add(rs.getInt("XX_VMR_LINE_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllConcepts(){
		
		String sql = "SELECT NAME, XX_VME_ConceptValue_ID " +
				     "FROM XX_VME_ConceptValue WHERE ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				conceptNames.add(rs.getString("Name"));
				conceptIDs.add(rs.getInt("XX_VME_ConceptValue_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			badWByLinConcept=true;
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllBrands(){
		
		String sql = "SELECT VALUE, XX_VMR_BRAND_ID " +
				     "FROM XX_VMR_BRAND WHERE ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				brandCodes.add(rs.getString("VALUE"));
				brandIDs.add(rs.getInt("XX_VMR_BRAND_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			badWByDepBrand = true;
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllStores(){
		
		String sql = "SELECT VALUE, AD_ORG_ID " +
				     "FROM AD_ORG " +
				     "WHERE UPPER(VALUE) = LOWER(VALUE) " +
				     "AND VALUE <> '001' " +
				     "AND NAME <> '*' "+ 
				     "AND ISSUMMARY = 'N' " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				storeCodes.add(rs.getString("VALUE"));
				storeIDs.add(rs.getInt("AD_ORG_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			badWByStoreMonth = true;
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private int getDepartmentID(String cellValue){
	
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<departmentCodes.size(); i++){
			
			if(departmentCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return departmentIDs.get(index);
		else
			return 0;
	}
	
	private int getLineID(String cellValue, int depID){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<lineCodes.size(); i++){
			
			if(lineCodes.get(i).equals(cellValue) && lineDepIDs.get(i)==depID){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return lineIDs.get(index);
		else
			return 0;
	}
	
	private int getSectionID(String cellValue, int lineID){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}

		for(int i=0; i<sectionCodes.size(); i++){
			
			if(sectionCodes.get(i).equals(cellValue) && sectionLinIDs.get(i)==lineID){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return sectionIDs.get(index);
		else
			return 0;
	}
	
	private int getConceptID(String cellValue){
		
		int index=-1;
		for(int i=0; i<conceptNames.size(); i++){
			
			if(conceptNames.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return conceptIDs.get(index);
		else
			return 0;
	}
	
	private int getBrandID(String cellValue){
		
		int index=-1;
		for(int i=0; i<brandCodes.size(); i++){
			
			if(brandCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return brandIDs.get(index);
		else
			return 0;
	}

	private int getStoreID(String cellValue){
		
		int index=-1;  // OJO CON ESTO (ACTUALMENTE LAS TIENDAS TIENEN 3 DIGITOS Ejm: 002)
		if(cellValue.length()==1){
			cellValue = "00" + cellValue;
		}else if (cellValue.length()==2) {
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<storeCodes.size(); i++){
			
			if(storeCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return storeIDs.get(index);
		else
			return 0;
	}
	
	private void deleteAllWbySec(){
		
		String sql = "DELETE FROM XX_VMR_WeightAssigned " +
				     "WHERE XX_WeightType=1 " + 
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.executeQuery();					
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private void deleteAllWbyLin(){
		
		String sql = "DELETE FROM XX_VMR_WeightAssigned " +
				     "WHERE XX_WeightType=2 "+
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.executeQuery();					
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private void deleteAllWbyLinConcept(){
		
		String sql = "DELETE FROM XX_VMR_WeightAssigned " +
				     "WHERE XX_WeightType=3 " + 
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.executeQuery();					
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, e.getMessage());
		}
	}

	private void deleteAllWbyDepBrand(){
		
		String sql = "DELETE FROM XX_VMR_WeightAssigned " +
				     "WHERE XX_WeightType=4 "+
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.executeQuery();					
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private void deleteAllWbyStore(){
		
		String sql = "DELETE FROM XX_VMR_WeightAssigned " +
				     "WHERE XX_WeightType=5" +
					 "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.executeQuery();					
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, e.getMessage());
		}
	}
}
