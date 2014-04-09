package compiere.model.cds.processes;

import java.awt.Window;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.Level;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import org.compiere.apps.AWindow;
import org.compiere.model.MCampaign;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_PO_ProductDistrib;
import compiere.model.cds.X_XX_VMR_Package;
//import compiere.model.cds.X_XX_VMR_Season;
import compiere.model.dynamic.X_XX_VMA_Season;

public class XX_CheckupLabeling extends SvrProcess {

	private String printer = "";
	private int qty = 1;
	
	@Override
	protected String doIt() throws Exception {
		
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		PrintService psZebra = null;
		
		for(int i=0; i<services.length; i++){
			
			if(services[i].getName().equals(printer)){
				psZebra = services[i];	
			}
		}
		
		DocPrintJob job = psZebra.createPrintJob();
		
		/*
		 * Variables Globales
		 */
		String barCode = "11111111";
		//String barCodeText="hola";
		String checkupDate = "";
		String categoryLabel = "";
		String departmentLabel = "";
		String seasonLabel = "";
		String collectionLabel = "";
		String packagesLabel = "";
		String aceptedPiecesLabel = "";
		MOrder order = null;
		/*
		 * Fin - Variables Globales
		 */
		
		String nombreVentana = "";
		Window[] List = AWindow.getWindows();
		for (int i = 0; i < List.length; i++) {
			if(List[i].isFocused()){
				nombreVentana = List[i].getName();
				System.out.println("Afuera: ."+ List[i].getName()+".");				
			}
		}
		
		if(nombreVentana.equals("AWindow_1000222")){
			X_XX_VMR_PO_ProductDistrib productDistrib = new X_XX_VMR_PO_ProductDistrib(getCtx(), getRecord_ID(), null);
			X_XX_VMR_DistributionHeader distribHeader = new X_XX_VMR_DistributionHeader(getCtx(), productDistrib.getXX_VMR_DistributionHeader_ID(), null);
		
			order = new MOrder( getCtx(), distribHeader.getC_Order_ID(), null);
			
			//Piezas Aceptadas
			String SQL = "SELECT SUM(XX_PRODUCTQUANTITY) " +
						 "FROM XX_VMR_PO_DISTRIBDETAIL " +
						 "WHERE XX_VMR_PO_PRODUCTDISTRIB_ID IN (SELECT XX_VMR_PO_PRODUCTDISTRIB_ID " +
						 "										FROM XX_VMR_PO_PRODUCTDISTRIB " +
						 "										WHERE XX_VMR_DISTRIBUTIONHEADER_ID = "+distribHeader.get_ID()+") "+
						 "AND M_WAREHOUSE_ID = "+getCtx().getContext("#XX_L_WAREHOUSECENTRODIST_ID")+" ";
	
			int aceptedPieces = 0;
			try {
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next()) {
					aceptedPieces = rs.getBigDecimal("SUM(XX_PRODUCTQUANTITY)").intValue();
				} 
				rs.close();
				pstmt.close();
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL, e);
			}
			
			aceptedPiecesLabel = "#Piezas: " + aceptedPieces;
		}else{
			MInOut mInOut = new MInOut( getCtx(), getRecord_ID(), null);
			
			mInOut.setXX_PrintedLabels(qty);
			mInOut.save();
			
			//Piezas Aceptadas
			String SQL = "SELECT SUM(MOVEMENTQTY) " +
						 "FROM M_INOUTLINE " +
						 "WHERE M_INOUT_ID = "+getRecord_ID();
	
			int aceptedPieces = 0;
			try {
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next()) {
					aceptedPieces = rs.getBigDecimal("SUM(MOVEMENTQTY)").intValue();
				} 
				rs.close();
				pstmt.close();
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL, e);
			}
			
			aceptedPiecesLabel = "#Piezas: " + aceptedPieces;
			
			order = new MOrder( getCtx(), mInOut.getC_Order_ID(), null);
		}
		
		
			//Fecha
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(order.getXX_CheckupDate().getTime());
			DecimalFormat myFormat = new DecimalFormat("00");
			int month = date.get(Calendar.MONTH) + 1;
			checkupDate = "Fecha: " + myFormat.format(date.get(Calendar.DAY_OF_MONTH))+"/"+myFormat.format(month)+"/"+date.get(Calendar.YEAR);
			
			//Categoria	
			MVMRCategory category = new MVMRCategory( getCtx(), order.getXX_Category_ID(), null);
			categoryLabel = "Cat: " + category.getValue() +"-"+ category.getName();
			categoryLabel = convertTON(categoryLabel);
				
			//Departamento
			MVMRDepartment depart = new MVMRDepartment( getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
			departmentLabel = "Dep: " + depart.getValue() +"-"+ depart.getName();
			departmentLabel = convertTON(departmentLabel);
			
			//Temporada
			X_XX_VMA_Season season = new X_XX_VMA_Season( getCtx(), order.getXX_Season_ID(), null);
			seasonLabel = "Temp: " + season.getName();
			seasonLabel = convertTON(seasonLabel);
				
			//Coleccion
			X_XX_VMR_Collection collection = new X_XX_VMR_Collection( getCtx(), order.getXX_Collection_ID(), null);
			collectionLabel = "Col: " + collection.getName();
			collectionLabel = convertTON(collectionLabel);
			
			//Paquete
			X_XX_VMR_Package packages = new X_XX_VMR_Package( getCtx(), order.getXX_VMR_Package_ID(), null);
			packagesLabel = "Paq: " + packages.getName();
			packagesLabel = convertTON(packagesLabel);
			
			//Codigo de Barras
			//barCode="11111111";
			//barCodeText="    "+"";
		
		
		//Creacion de la Estructura de la etiqueta
		String s =  
		"^XA^PRD^XZ\n" +    
		"^XA^JMA^\n" +              
		"^LH07,02^FS\n" +       
		"^FO90,15^B3N,40,N^BY3, 0.5,120^FD"+ barCode +"^FS\n" +
		//"^FO130,95^AB,35,15^FD"+ barCodeText +"^FS\n" +   
		"^FO15,190^AB,90,30^FD"+ checkupDate +"^FS\n" +          
		"^FO15,300^AB,90,30^FD"+ categoryLabel +"^FS\n" +
		"^FO15,410^AB,90,25^FD"+ departmentLabel +"^FS\n" +
		"^FO15,520^AB,90,30^FD"+ seasonLabel +"^FS\n" +
		"^FO15,630^AB,90,25^FD"+ collectionLabel +"^FS\n" +
		"^FO15,740^AB,90,30^FD"+ packagesLabel +"^FS\n" +
		"^FO15,850^AB,90,30^FD"+ aceptedPiecesLabel+"^FS\n" +
	
		"^PQ" + qty + "^FS\n"+
		"^XZ\n";
		
		//Control Label
		/*"^XA^PRD^XZ\n"+
		"^XA^JMA^\n"+
		"^LH00,15^FS"+
		"^FO2,5^AD,38,10^FD*CONTROL*     36 072009^FS\n"+
		"^FO05,45^A0,30,07^FD\n"+//                  TDA:  3^FS\n"+
		"^FO28,48^A0,15,14^FDCANT:     "+cantidadEtiquetas+"      PRECIO     "+precio+"^FS\n"+
		"^FO05,95^A0,18,10^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative +"^FS\n"+
		" ^FO05,120^A0,18,10^FD"+name+"^FS\n"+
		"^FO05,140^A0,18,10^FD                                                 ^FS\n"+
		"^PQ1^FS\n"+
		"^XZ";*/
		
		byte[] by = s.getBytes();   
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; 
		Doc doc = new SimpleDoc(by, flavor, null);   			   
		job.print(doc, null); 
	
		return "";
	}

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Printer"))
				printer = element.getParameter().toString();
			else if (name.equals("Qty"))
				qty = new Integer (element.getParameter().toString());
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
	}
	
	
	private String convertTON(String oldString){
		
		String newString = "";
		
		for(int i=0; i<oldString.length(); i++){
			
			if(i+1<=oldString.length()){
				if(oldString.substring(i,i+1).equals("Ñ")){
					newString+="N";
				}
				else{
					newString+=oldString.substring(i, i+1);
				}
			}
		}
		
		return newString;
	}

}
