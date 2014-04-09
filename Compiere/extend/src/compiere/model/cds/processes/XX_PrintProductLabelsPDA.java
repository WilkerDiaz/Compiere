package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_AttributeSetInstance;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_LongCharacteristic;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;
import compiere.model.cds.X_XX_VMR_Section;

/**
 * Proceso que permite imprimir etieutas de productos por lote desde PDA
 * @author Gabrielle Huchet.
 *
 */
public class XX_PrintProductLabelsPDA extends SvrProcess{
	
	static CLogger log = CLogger.getCLogger(XX_PrintProductLabelsPDA.class);
	private Integer lote = null;
	private Integer M_Warehouse_ID = null;
	private String M_Warehouse_Value = null;
	static int glued = -1, hanging = -1;

	protected void prepare() {
		
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();		
			if (element.getParameter()!=null) {
				if (name.equals("XX_Lot") ) {
					lote = new BigDecimal(element.getParameterAsInt()).intValue();
				}else if (name.equals("M_Warehouse_ID") ) {
					M_Warehouse_ID = new BigDecimal(element.getParameterAsInt()).intValue();
				}else if (name.equals("Printer_Hanging")) {
					for(int i=0; i<services.length; i++){
						if(services[i].getName().equals(element.getParameter())){
							hanging = i;
							break;
						}
					}
				} else if (name.equals("Printer_Glued")) {
					for(int i=0; i<services.length; i++){
						if(services[i].getName().equals(element.getParameter())){
							glued = i;
							break;
						}
					}
				}
			}			
		}
	}

	@Override
	protected String doIt() throws Exception {
		try{
			String msg = "";
			msg = readFile();
			return msg;
		}catch (Exception e) {
			return "Error en el proceso: "+e.getMessage();
		}
		
	}
	
	public String readFile()  throws Exception  {
		
		//Obtengo el value de la tienda 
		MWarehouse warehouse = new MWarehouse(Env.getCtx(), M_Warehouse_ID, null);
		M_Warehouse_Value = warehouse.getValue();
		
		/*
		 * Creo conexión con AS400
		 */
		ResultSet rsAS400 = null;
		As400DbManager as400 = new As400DbManager();
		as400.conectar();
		Statement ps_s = null;
		int lineas = 0;
		try {
			ps_s = as400.conexion.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		/*
		 */
		String sql = "SELECT CANPTC, PRDPTC " +
					 "FROM ICTFILE.SOLPTC " +
					 "WHERE INTEGER(LOTPTC) = "+new Integer(lote)+ " " +
					 "AND TNDPTC = '"+M_Warehouse_Value+"' " +
					 "AND STSPTC <> '1' ";
		System.out.println("sql select: "+sql);
		rsAS400 = as400.realizarConsulta(sql, ps_s);

			//El vector que almacena la estructura de devoluciones
		 Vector<Etiqueta> etiquetas = new Vector<Etiqueta>();
		 Etiqueta etiqueta;
			try {
				while(rsAS400.next()) {

					//Capturo el producto
					String p_idAux = rsAS400.getString("PRDPTC");
					
					String p_id = p_idAux.substring(0, p_idAux.length()-3);
					MProduct producto = getProduct(new Integer(p_id).toString());					
					if (producto == null ) {
						return Msg.getMsg(Env.getCtx(), "XX_ProductError");   
					}					
					
					//Capturo el consecutivo
					Integer consecutivo = null;
					try {
						consecutivo = Integer.parseInt(p_idAux.substring(p_idAux.length()-3, p_idAux.length()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					if (consecutivo == null) {
												
					}

					//Capturo las piezas
					Integer piezas = null;
					try {
						piezas = rsAS400.getInt("CANPTC");
						
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					if (piezas == null) {
						
					}
					Integer consecutivoID = getPriceConsective(producto.get_ID(), consecutivo);
					if(consecutivoID ==null){
						return Msg.getMsg(Env.getCtx(), "No se encontro el consecutivo de precio: "+consecutivo+
								" para el producto: "+producto.getValue()+"-"+producto.getName());	
					}
					etiqueta = new Etiqueta(M_Warehouse_ID,producto.get_ID(),consecutivoID,piezas);
					etiquetas.add(etiqueta);
					lineas = lineas + 1;
				}
				
				
				/*
				 * Actualizo el campo STSPTC en '1' para no volver a seleccionar ese lote
				 */
				String sql_update= "UPDATE ICTFILE.SOLPTC " +
								   "SET STSPTC = '1' " +
								   "WHERE INTEGER(LOTPTC) = "+new Integer(lote)+ " " +
								   "AND TNDPTC = '"+M_Warehouse_Value+"' ";

				/*	Ejecuto la sentencia
				* */
				Integer numRegUpdated = as400.realizarSentencia(sql_update, ps_s);
				System.out.println(numRegUpdated);
								
				rsAS400.close();
				ps_s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if(lineas == 0){
				MWarehouse almaAux = new MWarehouse(getCtx(), M_Warehouse_ID, null);
				return Msg.getMsg(Env.getCtx(), "XX_NotHaveLot", 
						new String[] { lote.toString() , M_Warehouse_Value + " - " + almaAux.getName()});	
			}else{
				//Una vez leido todo el archivo se procede a imprimir las etiquetas
				 procesarImpresionEtiquetas(etiquetas);
			}
			return "";
	}
	
//	private String procesarImpresionEtiquetas(Vector<Etiqueta> etiquetas)  throws Exception {
//		
//		Etiqueta temp;
//		for (int i = 0; i < etiquetas.size(); i++) {
//			//XX_ProductLabelsPending
//			temp = etiquetas.get(i);
//			X_XX_ProductLabelsPending prodLabel = new X_XX_ProductLabelsPending(getCtx(), 0, get_TrxName());
//			prodLabel.setM_Product_ID(temp.producto);
//			prodLabel.setXX_Quantity(temp.cantidad);
//			prodLabel.setXX_VMR_PriceConsecutive_ID(temp.consecutivo);
//			prodLabel.setM_Warehouse_ID(temp.tienda);
//			prodLabel.setValue(lote.toString());
//			
//			if (!(prodLabel.save())) {
//				throw new Exception("Error Insertando registro en la tabla XX_ProductLabelsPending: "+prodLabel);
//			}
//			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Lote", lote.toString());
//			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Warehouse_ID", M_Warehouse_ID);
//
//			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Hanging",hanging);
//			Env.getCtx().setContext( "#XX_VMR_PRINTLABEL_Glued", glued);
//		}
//		return "Proceso Completado.";
//	}
	private void procesarImpresionEtiquetas(Vector<Etiqueta> etiquetas)  throws Exception {
			Etiqueta temp;
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
			PrintService psZebra_glued = services[glued];
			for (int i = 0; i < etiquetas.size(); i++) {
				temp = etiquetas.get(i);
				print_labels(psZebra_glued, temp, true);
			}
	}

	public void print_labels (PrintService psZebra, Etiqueta etiqueta , boolean glued) throws Exception {
 			
			int cantidadEtiquetas = etiqueta.cantidad;
			X_XX_VMR_PriceConsecutive consecutivo = new X_XX_VMR_PriceConsecutive(Env.getCtx(), etiqueta.consecutivo, null);
			MProduct producto = new MProduct(Env.getCtx(), etiqueta.producto, null);	
			
			String name = producto.getName();
			/*
			 * Caracteristica larga
			 * */
			X_XX_VMR_LongCharacteristic caracLarga = new X_XX_VMR_LongCharacteristic(Env.getCtx(), producto.getXX_VMR_LongCharacteristic_ID(), null);
			String descripcion = null;
			if(producto.getM_AttributeSetInstance_ID()!=0){
				X_M_AttributeSetInstance attrSet = new X_M_AttributeSetInstance(Env.getCtx(), producto.getM_AttributeSetInstance_ID(), null);
				descripcion = attrSet.getDescription();
			}
			//DecimalFormat formato = new DecimalFormat(".##");
			DecimalFormat formato = new DecimalFormat("###,###,###.00");
			X_XX_VMR_Department dep = new X_XX_VMR_Department(Env.getCtx(), producto.getXX_VMR_Department_ID() , null);			
			String departmentCode = dep.getValue();
					
			X_XX_VMR_Line lin = new X_XX_VMR_Line(Env.getCtx(), producto.getXX_VMR_Line_ID(), null);
			String lineCode = lin.getValue();
			
			X_XX_VMR_Section sec = new X_XX_VMR_Section(Env.getCtx(), producto.getXX_VMR_Section_ID(), null);
			String seccionCode = sec.getValue();  
			
			String precio = formato.format(consecutivo.getXX_SalePrice());

			MWarehouse tienda = new MWarehouse(Env.getCtx(), etiqueta.tienda, null);
			
			
			//Debo buscar semana, mes y año de la fecha de creacion del consecutivo
		
			Date date = (Date)consecutivo.getCreated();
			Calendar cal = new GregorianCalendar();  
			cal.setTime(date);			
			int mes = cal.get(Calendar.MONTH)+1;
			int año = cal.get(Calendar.YEAR);
			int semana = cal.get(Calendar.WEEK_OF_YEAR);

			DateFormat fechaformato = new SimpleDateFormat("MM yyyy");

			
			//Calcular el impuesto		
			Integer TaxCategory_ID = producto.getC_TaxCategory_ID();		
			String sql = "SELECT rate"
						+ " FROM C_Tax"
						+ " WHERE ValidFrom="			
						+ " (SELECT MAX(ValidFrom)"											
						+ " FROM C_Tax"
						+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+" AND ValidFrom <= "+DB.TO_DATE((Timestamp)date,true)+")";	
			PreparedStatement prst = DB.prepareStatement(sql,null);
			ResultSet rs = null;
			BigDecimal impuesto = new BigDecimal(1);
			try {
				rs = prst.executeQuery();
				if (rs.next()){
					impuesto = rs.getBigDecimal("rate").divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
					System.out.println("impuesto "+impuesto);
				}			
			}catch (Exception e){
				System.out.println("error al calcular el impuesto");
			}finally {
				rs.close();
				prst.close();
			}
			BigDecimal taxAux = getTaxForLabel((Timestamp)date,TaxCategory_ID);
			
			String product_plus_correlative = "" + producto.getValue() + consecutivoTostring(consecutivo.getXX_PriceConsecutive());
			
			boolean exento = false;
			if (producto.getC_TaxCategory_ID() == Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID")) {
				exento = true;				
			}
			String s = "";
			DocPrintJob job = psZebra.createPrintJob();
			
			precio = formato.format((consecutivo.getXX_SalePrice().add((consecutivo.getXX_SalePrice()).multiply(impuesto))).setScale(2, BigDecimal.ROUND_HALF_UP));
			while (product_plus_correlative.length() < 12) {
				product_plus_correlative = "0" + product_plus_correlative;
			}
//				s="^XA^PRD^XZ\n"+                                                                     
//				"^XA^JMA^\n"+                                                                       
//				"^LH07,02^FS\n"+                                                                    
//				"^FO10,10^BE,25,N^BY3, 0.5,45^FD"+ product_plus_correlative +"^FS\n"+                                 
//				 "^FO10,72^A0,15,14^FD"+departmentCode+"-"+lineCode+"-"+seccionCode+"-"+ product_plus_correlative +"       ^FS\n"+                          
//				 "^FO10,87^A0,15,14^FD"+(name.length() > 30 ? name.substring(0,29) : name)+"^FS\n"; 
//				 if(descripcion!=null ){
//					s=s+"^FO10,102^A0,15,14^FD"+ (descripcion.length() > 30 ? descripcion.substring(0,29) : descripcion) + "^FS\n";
//				 }else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
//					s=s+"^FO10,102^A0,15,14^FD"+ (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS\n";
//				 }
//
//				 s=s+"^FO10,120^AB,13,07^FDPRECIO BS^FS\n"+                                             
//				 "^FO10,173^AB,11,07^FDRIF J-00046517-7 "+  (exento ? "" : "Incluye IVA "+ taxAux +"%")+"^FS\n"+                   
//				 "^FO10,158^AB,11,07^CI10^FD"+semana+" "+fechaformato.format(date)+"^FS\n"+                                                                                   
//				 "^FO200,105^AO,25,10^CI10^FD"+precio+"^FS\n"+  
//				 "^FO200,153^AD,26,16^FDBECO^FS" +
//				 "^PQ"+cantidadEtiquetas+"^FS\n"+                                                                        
//				 "^XZ\n"+       
//				 //Control label                                                                    
//				 "^XA^PRD^XZ\n"+                                                                     
//				 "^XA^JMA^\n"+                                                                       
//				 "^LH00,15^FS\n"+                                                                    
//				 "^FO07,20^AD,50,10^FD*CONTROL*     "+semana+" "+ fechaformato.format(date)+"^FS\n"+            
//				 "^FO28,70^A0,15,14^FDTDA:  "+tienda.getValue()+"^FS\n"+                      
//				 "^FO28,85^A0,15,14^FDCANT:     "+cantidadEtiquetas+"      PRECIO      "+precio+"^FS\n"+               
//				 "^FO28,100^A0,15,14^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative +"^FS\n"+                                   
//				 "^FO28,118^A0,15,14^FD"+(name.length() > 50 ? name.substring(0,49) : name)+"^FS\n"+                            
//				 "^FO05,140^A0,15,10^FD                                                  ^FS\n"+    
//				 "^PQ1^FS\n"+                                                                        
//				 "^XZ"; 
				
				s="^XA^PRD^XZ\n"+                                                                     
				"^XA^JMA^\n"+                                                                       
				"^LH07,02^FS\n"+                                                                    
				"^FO10,03^BE,25,N^BY3, 0.5,45^FD"+ product_plus_correlative +"^FS\n"+                                 
				 "^FO10,62^AA,15,12^FD"+departmentCode+"-"+lineCode+"-"+seccionCode+"-"+ product_plus_correlative +"       ^FS\n"+                          
				 "^FO10,82^AA,20,10^FD"+(name.length() > 30 ? name.substring(0,29) : name)+"^FS\n"; 
				 if(descripcion!=null ){
					s=s+"^FO10,97^AA,20,10^FD"+ (descripcion.length() > 30 ? descripcion.substring(0,29) : descripcion) + "^FS\n";
				 }else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
					s=s+"^FO10,97^AA,20,10^FD"+ (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS\n";
				 }

				 s=s+"^FO10,116^AA,14,10^FDPRECIO BS^FS\n"+                                             
				 "^FO10,170^AB,11,07^FDRIF J-00046517-7 "+  (exento ? "" : "Incluye IVA "+ taxAux +"%")+"^FS\n"+                   
				 "^FO10,153^AB,11,07^CI10^FD"+semana+" "+fechaformato.format(date)+"^FS\n"+                                                                                   
				 "^FO167,153^AD,26,26^FDBECO^FS" +
				 "^FO55,115^AO,22,15^CI10^FD       "+precio+"^FS\n"+  
				 "^PQ"+cantidadEtiquetas+"^FS\n"+                                                                        
				 "^XZ\n"+       
				 //Control label 
				 "^XA^PRD^XZ\n"+                                                                     
				 "^XA^JMA^\n"+                                                                       
				 "^LH00,15^FS\n"+                                                                    
				 "^FO07,20^AD,50,10^FD*CONTROL*     "+semana+" "+ fechaformato.format(date)+"^FS\n"+            
				 "^FO28,70^A0,15,14^FDTDA:  "+tienda.getValue()+"^FS\n"+                      
				 "^FO28,85^A0,15,14^FDCANT:     "+cantidadEtiquetas+"      PRECIO      "+precio+"^FS\n"+               
				 "^FO28,100^A0,15,14^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative +"^FS\n"+                                   
				 "^FO28,118^A0,15,14^FD"+(name.length() > 50 ? name.substring(0,49) : name)+"^FS\n"+                            
				 "^FO05,140^A0,15,10^FD                                                  ^FS\n"+    
				 "^PQ1^FS\n"+                                                                        
				 "^XZ"; 

			byte[] by = s.getBytes();   
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; 
			Doc doc = new SimpleDoc(by, flavor, null);   			   
			job.print(doc, null);   
	
	}

	/** Retorna un producto de acuerdo a un value */
	private MProduct getProduct (String cellValue) {
		
		MProduct result = null;		
		if (cellValue == null) 
			return result;
		
		String sql_prod = "SELECT M_PRODUCT_ID FROM M_PRODUCT WHERE VALUE = ?";		
		PreparedStatement ps = DB.prepareStatement(sql_prod, null);
		try {
			ps.setString(1, cellValue);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = new MProduct(Env.getCtx(), rs.getInt(1), null);			
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** Retorna un consecutivo de precio dado un ID de producto y un número de consecutivo de precio*/
	private Integer getPriceConsective (int producto, int consecutivo ) {
		
		Integer result = null;		
		
		String sql_prod = "SELECT XX_VMR_PRICECONSECUTIVE_ID FROM XX_VMR_PRICECONSECUTIVE WHERE M_PRODUCT_ID = ? AND XX_PRICECONSECUTIVE = ? ";		
		PreparedStatement ps = DB.prepareStatement(sql_prod, null);
		ResultSet rs = null;
		try {
			ps.setInt(1, producto);
			ps.setInt(2, consecutivo);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return result;
	}
	
	private static BigDecimal getTaxForLabel(Timestamp date, int C_TaxCategory_ID){
		
		BigDecimal tax = BigDecimal.ZERO;
		
		Integer TaxCategory_ID = C_TaxCategory_ID;		
		String sql = "SELECT rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+ " "
					+ "AND ValidFrom <= TO_DATE('"+date.toString().substring(0,10)+"','YYYY-MM-DD'))";	
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		
		return tax;
	}
	
	private  String consecutivoTostring(int consecutivo)
	{
		String con = "";
		if (consecutivo<10)
			con = "00"+consecutivo;
		else if (consecutivo<100)
			con = "0"+consecutivo;
		else
			con = ""+consecutivo;
		return con;
	}
	
	private class Etiqueta {
		int tienda = 0;
		int producto = 0;
		int consecutivo =0;
		int cantidad = 0;

		
		private Etiqueta (int tienda, int producto, int consecutivo, int cantidad) {
			this.tienda = tienda;
			this.producto = producto;
			this.consecutivo = consecutivo;
			this.cantidad = cantidad;
		}
		
	}

}
