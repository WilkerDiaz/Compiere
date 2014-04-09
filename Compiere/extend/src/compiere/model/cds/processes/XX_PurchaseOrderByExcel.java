package compiere.model.cds.processes;


import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;
import java.util.logging.Level;



import jxl.Sheet;
import jxl.Workbook;



import org.compiere.apps.ADialog;
import org.compiere.model.MConversionRate;
import org.compiere.model.X_C_Conversion_Rate;





import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;





import compiere.model.cds.MOrder;

import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.MVMRVendorProdRef;

import compiere.model.cds.X_XX_VMR_ReferenceMatrix;

import compiere.model.cds.callouts.VME_PriceProductCallout;

/**
 *  Permite crear lineas de referencias de proveedor a una orden de compra mediante excel
 *
 *  @author     VLOMONACO
 *  @version    
 */


public class XX_PurchaseOrderByExcel extends SvrProcess {

	static CLogger log = CLogger.getCLogger(XX_ImportPTransfer.class);
	private String file = null;

	
	@Override
	protected String doIt() throws Exception {
		String msg = "";
		if (file == null) {
			msg =  Msg.translate( getCtx(), "File Not Loaded");
		} else {
			
			if(!file.substring(file.length()-4, file.length()).equals(".xls")){
				if(file.substring(file.length()-5, file.length()).equals(".xlsx")){
					msg = Msg.translate( getCtx(), "Excel Earlier Format");;	
				}else{
					msg =Msg.translate( getCtx(), "Not Excel");
				}
			}else{	
				msg = readFile();
			}
		}
		return msg;
	}
	
	int totalcompra = 0;

	@Override
	/** Obtener los parametros */
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("File") ) {
					file = element.getParameter().toString();
				}
			}			
		}
	}
	
	
	public String readFile()  throws IOException  {
		
		MOrder orden= new MOrder(getCtx(),getRecord_ID(),get_TrxName());

		File inputWorkbook = new File(file);
		Workbook w;
		try {
			String msg = "";
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			
			boolean finEncontrado = false;
			// Validamos que hayan colocado la palabra FIN
			for (int i = 1; i < sheet.getRows() && !finEncontrado; i++) {
				if ((sheet.getCell(0, i).getContents().equals("FIN")))
				{
					finEncontrado = true;
				}				
			}
			if (!finEncontrado)
			{
				ADialog.info(1, new Container(), "El Excel no contiene la palabra clave FIN al finalizar las referencias ");
				return ("El Excel no contiene la palabra clave FIN al finalizar las referencias ");
			}
			
			
			
			// Validamos que las caracteristicas principales existan
			Vector<Integer> validado = validateCharacteristics(sheet, orden);
			if (!validado.isEmpty())
			{
				ADialog.info(1, new Container(), "La característica principal no existe en el maestro en las siguientes lineas del excel " + validado.toString());
				return ("La característica principal no existe en algunos campos del Excel");
			}
				
	
			//int defaultRows = sheet.getRows();
			
			//Si la cantidad de columnas es 6
			if(sheet.getColumns()>=20 && sheet.getColumns()<40 && sheet.getRows() > 1){
		
				//Valido que las cabeceras tengan el formato correcto	
				if(!sheet.getCell(0, 0).getContents().equals("REFERENCIA") 
						|| !sheet.getCell(1, 0).getContents().equals("DESCRIPCION")
						|| !sheet.getCell(2, 0).getContents().equals("LINEA")
						|| !sheet.getCell(3, 0).getContents().equals("SECCION")
						|| !sheet.getCell(4, 0).getContents().equals("MARCA")
						|| !sheet.getCell(5, 0).getContents().equals("CARACTERISTICA PRINCIPAL")
						|| !sheet.getCell(6, 0).getContents().equals("POSEE IMPUESTO")
						|| !sheet.getCell(7, 0).getContents().equals("UNIDAD DE COMPRA")
						|| !sheet.getCell(8, 0).getContents().equals("CANTIDAD DE COMPRA")
						|| !sheet.getCell(9, 0).getContents().equals("UNIDAD DE VENTA")
						|| !sheet.getCell(10, 0).getContents().equals("CANTIDAD DE VENTA")
						|| !sheet.getCell(11, 0).getContents().equals("MULTIPLO DE EMPAQUE")
						|| !sheet.getCell(12, 0).getContents().equals("DESCUENTO 1")
						|| !sheet.getCell(13, 0).getContents().equals("DESCUENTO 2")
						|| !sheet.getCell(14, 0).getContents().equals("DESCUENTO 3")
						|| !sheet.getCell(15, 0).getContents().equals("DESCUENTO 4")
						|| !sheet.getCell(16, 0).getContents().equals("COSTO MONEDA ORIGEN")
						|| !sheet.getCell(17, 0).getContents().equals("PVP CON IVA")
						|| !sheet.getCell(18, 0).getContents().equals("CANTIDAD SIN CARACTERISTICA")
						|| !sheet.getCell(19, 0).getContents().equals("CARACTERISTICAS")
				) {
					ADialog.info(1, new Container(), "El Encabezado del excel (nombres en la primera línea) fue modificado. El mismo debe estar exactamente igual al formato montado en la red.");
					msg = Msg.translate( getCtx(), "Column Names");
					return msg;
				}	
				
					// tomamos los valores de las caracteristicas dinamicas del encabezado

				
					String caracB1 = (sheet.getColumns()>20) ? sheet.getCell(20, 0).getContents() : "";
					String caracB2 = (sheet.getColumns()>21) ? sheet.getCell(21, 0).getContents() : "";
					String caracB3 = (sheet.getColumns()>22) ? sheet.getCell(22, 0).getContents() : "";
					String caracB4 = (sheet.getColumns()>23) ? sheet.getCell(23, 0).getContents() : "";
					String caracB5 = (sheet.getColumns()>24) ? sheet.getCell(24, 0).getContents() : "";
					String caracB6 = (sheet.getColumns()>25) ? sheet.getCell(25, 0).getContents() : "";
					String caracB7 = (sheet.getColumns()>26) ? sheet.getCell(26, 0).getContents() : "";
					String caracB8 = (sheet.getColumns()>27) ? sheet.getCell(27, 0).getContents() : "";
					String caracB9 = (sheet.getColumns()>28) ? sheet.getCell(28, 0).getContents() : "";
					String caracB10 = (sheet.getColumns()>29) ? sheet.getCell(29, 0).getContents() : "";

					
					// tranformamos a id la caracteristica dinamicas de arriba (horizontales)
					
					int tipoCarac2 = getCharacteristicType(caracB1);
					int caractB1;
					if (getAttribute(caracB1, tipoCarac2)==-1)
					{   
						ADialog.info(1, new Container(), "La característica "+caracB1+" en la celda (1,21) no existe ");
						return ("La característica de la celda (1,21) no existe");
					}
					else 
						caractB1 = getAttribute(caracB1, tipoCarac2);
					
					int caractB2;
					if (getAttribute(caracB2, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB2+" en la celda (1,22) no existe ");
						return ("La característica de la celda (1,22) no existe");
					}
					else 
						caractB2 = getAttribute(caracB2, tipoCarac2);
					
					int caractB3;
					if (getAttribute(caracB3, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB3+" en la celda (1,23) no existe ");
						return ("La característica de la celda (1,23) no existe");
					}
					else 
						caractB3 = getAttribute(caracB3, tipoCarac2);
					
					int caractB4;
					if (getAttribute(caracB4, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB4+" en la celda (1,24) no existe ");
						return ("La característica de la celda (1,24) no existe");
					}
					else 
						caractB4 = getAttribute(caracB4, tipoCarac2);
					
					int caractB5;
					if (getAttribute(caracB5, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB5+" en la celda (1,25) no existe ");
						return ("La característica de la celda (1,25) no existe");
					}
					else 
						caractB5 = getAttribute(caracB5, tipoCarac2);
					
					int caractB6;
					if (getAttribute(caracB6, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB6+" en la celda (1,26) no existe ");
						return ("La característica de la celda (1,26) no existe");
					}
					else 
						caractB6 = getAttribute(caracB6, tipoCarac2);
					
					int caractB7;
					if (getAttribute(caracB7, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB7+" en la celda (1,27) no existe ");
						return ("La característica de la celda (1,27) no existe");
					}
					else 
						caractB7 = getAttribute(caracB7, tipoCarac2);
					
					int caractB8;
					if (getAttribute(caracB8, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB8+" en la celda (1,28) no existe ");
						return ("La característica de la celda (1,28) no existe");
					}
					else 
						caractB8 = getAttribute(caracB8, tipoCarac2);
					
					int caractB9;
					if (getAttribute(caracB9, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB9+" en la celda (1,29) no existe ");
						return ("La característica de la celda (1,29) no existe");
					}
					else 
						caractB9 = getAttribute(caracB9, tipoCarac2);
					
					int caractB10;
					if (getAttribute(caracB10, tipoCarac2)==-1)
					{
						ADialog.info(1, new Container(), "La característica "+caracB10+" en la celda (1,30) no existe ");
						return ("La característica de la celda (1,30) no existe");
					}
					else 
						caractB10 = getAttribute(caracB10, tipoCarac2);



					for (int i = 1; i <= sheet.getRows() && !(sheet.getCell(0, i).getContents().equals("FIN")); i++) {
						
						
						String reference_value = sheet.getCell(0, i).getContents();
						
						if (reference_value.isEmpty()){
							ADialog.info(1, new Container(), "La referencia esta vacia en la linea " + (i+1));
							rollback();
							break;
						} 
						
						String descripcion_ingles = sheet.getCell(1, i).getContents();
						
						int linea_value = new Integer(sheet.getCell(2, i).getContents());
						if (linea_value==0){
							ADialog.info(1, new Container(), "La linea esta vacia en la linea " + (i+1));
							rollback();
							break;
						} 
						
						int seccion_value = new Integer(sheet.getCell(3, i).getContents());
						if (seccion_value==0){
							ADialog.info(1, new Container(), "La seccion esta vacia en la linea " + (i+1));
							rollback();
							break;
						} 
						
						String marca_name = sheet.getCell(4, i).getContents();
						if (marca_name.isEmpty()){
							ADialog.info(1, new Container(), "La marca esta vacia en la linea " + (i+1));
							rollback();
							break;
						}
						
						String carac_principal_name = sheet.getCell(5, i).getContents();
						
						String impuesto = sheet.getCell(6, i).getContents();
						if (impuesto.isEmpty()){
							ADialog.info(1, new Container(), "El impuesto esta vacio en la linea " + (i+1));
							rollback();
							break;
						}
						
						String unidad_compra_name = sheet.getCell(7, i).getContents();
						if (unidad_compra_name.isEmpty()){
							ADialog.info(1, new Container(), "La unidad de compra esta vacia en la linea " + (i+1));
							rollback();
							break;
						}
						
						int cant_compra = new Integer(sheet.getCell(8, i).getContents());
						if (cant_compra==0){
							ADialog.info(1, new Container(), "La cantidad de compra esta vacia en la linea " + (i+1));
							rollback();
							break;
						}
						
						String unidad_venta_name = sheet.getCell(9, i).getContents();
						if (unidad_venta_name.isEmpty()){
							ADialog.info(1, new Container(), "La unidad de venta esta vacia en la linea " + (i+1));
							rollback();
							break;
						}
						
						int cant_venta = new Integer(sheet.getCell(10, i).getContents());
						if (cant_venta==0){
							ADialog.info(1, new Container(), "La cantidad de venta esta vacia en la linea " + (i+1));
							rollback();
							break;
						}
						
						int multiplo_empaque = new Integer(sheet.getCell(11, i).getContents());
						if (multiplo_empaque==0){
							ADialog.info(1, new Container(), "El multiplo de empaque esta vacio en la linea " + (i+1));
							rollback();
							break;
						}
						
						BigDecimal descuento1 = new BigDecimal((!sheet.getCell(12, i).getContents().isEmpty()) ? sheet.getCell(12, i).getContents().replaceAll(",", "."):"0");
						
						BigDecimal descuento2 = new BigDecimal((!sheet.getCell(13, i).getContents().isEmpty()) ? sheet.getCell(13, i).getContents().replaceAll(",", "."):"0");
						
						BigDecimal descuento3 = new BigDecimal((!sheet.getCell(14, i).getContents().isEmpty()) ? sheet.getCell(14, i).getContents().replaceAll(",", "."):"0");
						
						BigDecimal descuento4 = new BigDecimal((!sheet.getCell(15, i).getContents().isEmpty()) ? sheet.getCell(15, i).getContents().replaceAll(",", "."):"0");
						
						BigDecimal costo_origen = new BigDecimal(sheet.getCell(16, i).getContents().replaceAll(",", "."));
						if (costo_origen.compareTo(new BigDecimal(0))==0)
						{
							ADialog.info(1, new Container(), "El costo esta vacio en la linea " + (i+1));
							rollback();
							break;
						}
						
						BigDecimal pvp = new BigDecimal(sheet.getCell(17, i).getContents().replaceAll(",", "."));
						if (pvp.compareTo(new BigDecimal(0))==0)
						{
							ADialog.info(1, new Container(), "El PVP esta vacio en la linea " + (i+1));
							rollback();
							break;
						}

						int cant_sin_carac = new Integer((!sheet.getCell(18, i).getContents().isEmpty()) ? sheet.getCell(18, i).getContents() : "0");
						
						// si no tiene cantidad sin caracteristicas es porque formamos la matriz como esta abajo
//						if (cant_sin_carac==0)
//							obtenerCantidades(i, sheet);
						
						// Ya tomados todos los datos de una referencia. Ahora buscamos los ids
						int proveedor_ID = orden.getC_BPartner_ID();
						int departamento_ID = orden.getXX_VMR_DEPARTMENT_ID();
						int linea_ID = getLine_ID(departamento_ID,linea_value);
						if (linea_ID==0)
						{
							ADialog.info(1, new Container(), "La línea ingresada es inválida en la línea " + (i+1));
							rollback();
							break;
						}
						int seccion_ID = getSection_ID(linea_ID, seccion_value);
						if (seccion_ID==0)
						{
							ADialog.info(1, new Container(), "La sección ingresada es inválida en la línea " + (i+1));
							rollback();
							break;
						}
						int marca_ID = getBrand_ID(marca_name);
						if (marca_ID==0)
						{
							ADialog.info(1, new Container(), "La marca ingresada es inválida en la línea " + (i+1));
							rollback();
							break;
						}
						int caract_larga_ID;
						if (carac_principal_name.equals(""))
							caract_larga_ID = 0;
						else
						{
							if (getLongCharact(seccion_ID, carac_principal_name)==-1)
							{
								ADialog.info(1, new Container(), "La característica principal no existe en el maestro en la línea " + (i+1));
								rollback();
								break;
							}
							else 
								caract_larga_ID = getLongCharact(seccion_ID, carac_principal_name);
						}

						int impuesto_ID = getTaxCategory(impuesto);
						int canCompra_ID = getCanCompra(unidad_compra_name, cant_compra);
						int uniCompra_ID = getPurchaseUnit(canCompra_ID);
						if (uniCompra_ID==0)
						{
							ADialog.info(1, new Container(), "La unidad de compra ingresada es inválida en la línea " + (i+1));
							rollback();
							break;
						}
						int canVenta_ID = getCanVenta(unidad_venta_name, cant_venta);
						int uniVenta_ID = getSaleUnit(canVenta_ID);
						if (uniVenta_ID==0)
						{
							ADialog.info(1, new Container(), "La unidad de venta ingresada es inválida en la línea " + (i+1));
							rollback();
							break;
						}
						
						// Ahora vemos si la referencia existe. Si existe, la tomamos, sino, la creamos
						int referencia_ID = getReferencia(proveedor_ID, seccion_ID, marca_ID, caract_larga_ID, impuesto_ID,
											uniCompra_ID, canCompra_ID, uniVenta_ID, canVenta_ID, reference_value);
					
						if (referencia_ID==0)
						{						
							// Si la referencia es cero la creamos
							MVMRVendorProdRef newRef = new MVMRVendorProdRef(getCtx(), 0, get_Trx());
							newRef.setAD_Org_ID(0);
							newRef.setAD_Client_ID(getCtx().getAD_Client_ID());
							newRef.setC_BPartner_ID(proveedor_ID);
							newRef.setC_TaxCategory_ID(impuesto_ID);
							newRef.setXX_ComesFromExcel(true);     
							newRef.setXX_EnglishDescription(descripcion_ingles); 
							newRef.setDescription(reference_value+"_"+getSeccionName(seccion_ID)+"_Line:"+linea_value+"_Sec:"+ seccion_value+"_Caract:"+ carac_principal_name);
							newRef.setIsActive(true);							
							newRef.setM_AttributeSet_ID(getAttributeSet(seccion_ID));  	
							newRef.setName(getSeccionName(seccion_ID)); 
							newRef.setValue(reference_value);
							newRef.setXX_PackageMultiple(multiplo_empaque);
							newRef.setXX_PiecesBySale_ID(canVenta_ID);
							newRef.setXX_SaleUnit_ID(uniVenta_ID);
							if (getConceptoValor(departamento_ID, marca_ID)==-1)
							{	
								rollback();
								return ("No existe concepto de valor para la marca " + marca_name + " en este departamento. Debe crearlo primero");
							}					
							newRef.setXX_VMR_Brand_ID(marca_ID);
							newRef.setXX_VMR_Department_ID(departamento_ID);
							newRef.setXX_VMR_Line_ID(linea_ID);
							newRef.setXX_VMR_LongCharacteristic_ID(caract_larga_ID);
							newRef.setXX_VMR_Section_ID(seccion_ID);
			//				newaux.setXX_VMR_TypeBasic_ID(XX_VMR_TypeBasic_ID);       Por los momentos no le ponemos tipo de basico
							newRef.setXX_VMR_UnitConversion_ID(canCompra_ID);
							newRef.setXX_VMR_UnitPurchase_ID(uniCompra_ID);
							System.out.println(newRef.getValue()+ " " + carac_principal_name);
							newRef.save();
							referencia_ID = newRef.getXX_VMR_VendorProdRef_ID(); 
						}
						
						// Ahora cargamos la referencia a la orden de compra
						if (referencia_ID!=0)
						{
							MVMRPOLineRefProv detalle = new MVMRPOLineRefProv(getCtx(),0, get_Trx());
							
							int j = 0;
							detalle.setXX_ComesFromExcel(true);
							detalle.setXX_WithCharacteristic(cant_sin_carac==0); 
							detalle.setXX_SaleUnit_ID(uniVenta_ID);
							detalle.setXX_PiecesBySale_ID(canVenta_ID);
							detalle.setXX_VME_ConceptValue_ID(getConceptoValor(departamento_ID, marca_ID));
							detalle.setIsActive(true);
							detalle.setXX_VMR_UnitPurchase_ID(uniCompra_ID); 
							detalle.setXX_VMR_UnitConversion_ID(canCompra_ID); 
							if (cant_sin_carac==0)   // Si tiene caracteristicas
							{	
								// seteamos el tipo de caracteristica
								if (getCharacteristicType(sheet.getCell(19, i).getContents())!=-1)
									detalle.setXX_Characteristic1_ID(getCharacteristicType(sheet.getCell(19, i).getContents())); 
								else
								{
									ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
									rollback();
									break;
								}
								
								detalle.setXX_Characteristic2_ID(tipoCarac2); 
								
								// seteamos la caractaristica2 (la que esta horizontalmente)
								detalle.setXX_Characteristic2Value1_ID(caractB1); 								
								detalle.setXX_Characteristic2Value2_ID(caractB2);
								detalle.setXX_Characteristic2Value3_ID(caractB3); 
								detalle.setXX_Characteristic2Value4_ID(caractB4); 
								detalle.setXX_Characteristic2Value5_ID(caractB5); 
								detalle.setXX_Characteristic2Value6_ID(caractB6);
								detalle.setXX_Characteristic2Value7_ID(caractB7); 
								detalle.setXX_Characteristic2Value8_ID(caractB8); 
								detalle.setXX_Characteristic2Value9_ID(caractB9); 
								detalle.setXX_Characteristic2Value10_ID(caractB10); 
								
								// setamos la caracteristica1 (las que vienen verticalmente)
								boolean fin = false;
								j = i;
								if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
								{
									detalle.setXX_Characteristic1Value1_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())); 
									i++;
								} else 
								{	
									ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
									rollback();
									break;
								}
								if (sheet.getCell(0, i).getContents().isEmpty())
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value2_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value3_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value4_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value5_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value6_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value7_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value8_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value9_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								if (sheet.getCell(0, i).getContents().isEmpty() && !fin)
								{
									if (getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID())!=-1)
									{
										detalle.setXX_Characteristic1Value10_ID(getAttribute(sheet.getCell(19, i).getContents(), detalle.getXX_Characteristic1_ID()));
										i++;
									} else 
									{	
										ADialog.info(1, new Container(), "La caracteristica ingresada no existe en el maestro para la celda T " + (i));
										rollback();
										break;
									}
								}
								else
									fin = true;
								
								
								detalle.setXX_IsGeneratedCharac2Value1(caractB1!=0);
								detalle.setXX_IsGeneratedCharac2Value2(caractB2!=0);
								detalle.setXX_IsGeneratedCharac2Value3(caractB3!=0);
								detalle.setXX_IsGeneratedCharac2Value4(caractB4!=0);
								detalle.setXX_IsGeneratedCharac2Value5(caractB5!=0);
								detalle.setXX_IsGeneratedCharac2Value6(caractB6!=0);
								detalle.setXX_IsGeneratedCharac2Value7(caractB7!=0);
								detalle.setXX_IsGeneratedCharac2Value8(caractB8!=0);
								detalle.setXX_IsGeneratedCharac2Value9(caractB9!=0);
								detalle.setXX_IsGeneratedCharac2Value10(caractB10!=0);
								
								detalle.setXX_IsGeneratedCharac1Value1(detalle.getXX_Characteristic1Value1_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value2(detalle.getXX_Characteristic1Value2_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value3(detalle.getXX_Characteristic1Value3_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value4(detalle.getXX_Characteristic1Value4_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value5(detalle.getXX_Characteristic1Value5_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value6(detalle.getXX_Characteristic1Value6_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value7(detalle.getXX_Characteristic1Value7_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value8(detalle.getXX_Characteristic1Value8_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value9(detalle.getXX_Characteristic1Value9_ID()!=0);
								detalle.setXX_IsGeneratedCharac1Value10(detalle.getXX_Characteristic1Value10_ID()!=0);
								detalle.setXX_ShowMatrix("Y");
								detalle.setXX_GenerateMatrix("N");
								detalle.setQty(1);
								
							
								
							}
							else 
								detalle.setQty(cant_sin_carac);
							

							
							
							
							detalle.setXX_PackageMultiple(multiplo_empaque); 
							detalle.setXX_VMR_VendorProdRef_ID(referencia_ID); 
							detalle.setXX_LastSalePrice(getLastSalePrice(referencia_ID));
							detalle.setXX_VMR_Line_ID(linea_ID); 
							detalle.setXX_VMR_Section_ID(seccion_ID); 
							detalle.setC_Order_ID(orden.getC_Order_ID()); 
							detalle.setC_TaxCategory_ID(impuesto_ID); 
							detalle.setLine(new BigDecimal(0));
							detalle.setXX_Rebate4(descuento4); 
							detalle.setXX_Rebate3(descuento3); 
							detalle.setXX_Rebate2(descuento2); 
							detalle.setXX_Rebate1(descuento1); 
//							detalle.setXX_AssociateReference(rs.getString("XX_AssociateReference"));   No por los momentos
							detalle.setXX_VMR_LongCharacteristic_ID(caract_larga_ID); 
							detalle.setXX_VMR_Brand_ID(marca_ID);
							detalle.setXX_ReferenceIsAssociated(false);
//							detalle.setXX_GenerateMatrix(rs.getString("XX_GenerateMatrix")); No por los momentos estos tres
//							detalle.setXX_DeleteMatrix(rs.getString("XX_DeleteMatrix"));
//							detalle.setXX_ShowMatrix(rs.getString("XX_ShowMatrix"));
							
				
							// Parte del calculo de precios
							
							// primero tomo el pvp y calculo el precio Beco
							VME_PriceProductCallout priceBecoG = new VME_PriceProductCallout();					
							BigDecimal precioBeco = new BigDecimal(priceBecoG.priceBecoGlobal(getCtx(), 0	, null, null, null, null, 5,pvp));
							detalle.setXX_SalePricePlusTax(precioBeco.setScale (4,BigDecimal.ROUND_HALF_UP)); 
							
							BigDecimal SalePrice = (precioBeco.multiply(new BigDecimal(100))).divide((getTaxRate(impuesto_ID).add(new BigDecimal(100))),4,RoundingMode.HALF_UP);
							SalePrice = SalePrice.setScale (4,BigDecimal.ROUND_HALF_UP);
							detalle.setXX_SalePrice(SalePrice); 
														
							BigDecimal CostoNacional = costo_origen;	
							

							if (descuento1!=null && 0!=(descuento1.compareTo(new BigDecimal(0))) )
								CostoNacional = CostoNacional.subtract(CostoNacional.multiply(descuento1.multiply(new BigDecimal(0.01))));
							if (descuento2!=null && 0!=(descuento2.compareTo(new BigDecimal(0))) )
								CostoNacional = CostoNacional.subtract(CostoNacional.multiply(descuento2.multiply(new BigDecimal(0.01))));
							if (descuento3!=null && 0!=(descuento3.compareTo(new BigDecimal(0))))
								CostoNacional = CostoNacional.subtract(CostoNacional.multiply(descuento3.multiply(new BigDecimal(0.01))));
							if (descuento4!=null &&  0!=(descuento4.compareTo(new BigDecimal(0))))
								CostoNacional = CostoNacional.subtract(CostoNacional.multiply(descuento4.multiply(new BigDecimal(0.01))));
							
							BigDecimal CostoConDescuento = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
							detalle.setXX_CostWithDiscounts(CostoConDescuento);
							
							String OrderType = orden.getXX_OrderType();
							
							BigDecimal FactorEstimado = orden.getXX_EstimatedFactor();
							BigDecimal FactorReposicion = orden.getXX_ReplacementFactor();
							
							CostoNacional = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
							detalle.setPriceActual(CostoNacional);
							
							if (OrderType.equals("Importada"))	// Si la orden de compra es Internacional
							{
								Integer ConversionRate_ID = orden.getXX_ConversionRate_ID();
								X_C_Conversion_Rate  cr = new X_C_Conversion_Rate(getCtx(), ConversionRate_ID, null);
								BigDecimal conversion = cr.getMultiplyRate();
								
								BigDecimal costoMostrado = CostoNacional.multiply(FactorEstimado);
								costoMostrado = costoMostrado.setScale (2,BigDecimal.ROUND_HALF_UP);
								detalle.setPriceActual(costoMostrado);
								
//								CostoNacional = CostoNacional.divide(FactorEstimado, 2, RoundingMode.HALF_UP);
								CostoNacional = CostoNacional.multiply(FactorReposicion);
								CostoNacional = CostoNacional.setScale (4,BigDecimal.ROUND_HALF_UP);
							}
											
		
							
							
							BigDecimal Costo = (CostoNacional.divide(new BigDecimal(cant_compra), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(cant_venta));
							Costo = Costo.setScale (4,BigDecimal.ROUND_HALF_UP);					
						
							 //Agregado GHUCHET - Costo comercial
							detalle.set_Value("XX_TradeCost", Costo);
							
							BigDecimal TaxAmount = precioBeco.subtract(SalePrice);
							TaxAmount = TaxAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
							detalle.setXX_TaxAmount(TaxAmount);	
							
							detalle.setXX_UnitPurchasePrice(costo_origen);
							
							BigDecimal Margin = ((SalePrice.subtract(Costo)).divide(SalePrice,4, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));		
							Margin = Margin.setScale (4,BigDecimal.ROUND_HALF_UP);
							detalle.setXX_Margin(Margin);
							
							// Esta parte actualiza los valores del detalle para la cabecera
							

							Integer SaleQty = (cant_sin_carac*cant_compra)/cant_venta;
							detalle.setSaleQty(SaleQty);
							
							BigDecimal LinePVPAmount = (SalePrice.multiply(new BigDecimal(SaleQty)));				
							LinePVPAmount = LinePVPAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
							detalle.setXX_LinePVPAmount(LinePVPAmount);		
							
							BigDecimal LinePlusTaxAmount = (precioBeco.multiply(new BigDecimal(SaleQty)));				
							LinePlusTaxAmount = LinePlusTaxAmount.setScale (4,BigDecimal.ROUND_HALF_UP);	
							detalle.setXX_LinePlusTaxAmount(LinePlusTaxAmount);
							
							detalle.setXX_LineQty(cant_sin_carac);
							detalle.setXX_GiftsQty(0);
							
							detalle.setXX_LineMargin(Margin.multiply(new BigDecimal(cant_sin_carac)));
							
							BigDecimal LineNetAmt = CostoConDescuento.multiply(new BigDecimal(cant_sin_carac));						
							LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
							detalle.setLineNetAmt(LineNetAmt);
							detalle.setXX_ReferenceIsAssociated(false);
						
							boolean guardo = detalle.save();
							
							if (!guardo)
							{
								actualizarI(sheet,i);
								if (cant_sin_carac==0)
									i--;
								continue;
							}
							
							// Ahora seteamos la matriz en los casos que tenga caracteristicas
							if (cant_sin_carac==0)  
							{
								generateMatrix(sheet,detalle,j,i, cant_compra, cant_venta, orden);   // AQUI FUE CAMBIADO
								i--;
								detalle.setQty(totalcompra);
								
								if ((totalcompra%multiplo_empaque)!=0)
								{
									detalle.setXX_PackageMultiple(1);
								}
								
								// se setean los totales por linea
								SaleQty = (totalcompra*cant_compra)/cant_venta;
								detalle.setSaleQty(SaleQty);
								
								LinePVPAmount = (SalePrice.multiply(new BigDecimal(SaleQty)));				
								LinePVPAmount = LinePVPAmount.setScale (4,BigDecimal.ROUND_HALF_UP);
								detalle.setXX_LinePVPAmount(LinePVPAmount);		
								
								LinePlusTaxAmount = (precioBeco.multiply(new BigDecimal(SaleQty)));				
								LinePlusTaxAmount = LinePlusTaxAmount.setScale (4,BigDecimal.ROUND_HALF_UP);	
								detalle.setXX_LinePlusTaxAmount(LinePlusTaxAmount);
								
								detalle.setXX_LineQty(totalcompra);
								detalle.setXX_GiftsQty(0);
								
								detalle.setXX_LineMargin(Margin.multiply(new BigDecimal(totalcompra)));
								
								LineNetAmt = CostoConDescuento.multiply(new BigDecimal(totalcompra));						
								LineNetAmt = LineNetAmt.setScale (4,BigDecimal.ROUND_HALF_UP);
								detalle.setLineNetAmt(LineNetAmt);
								
								
							} else
							{
								// si no tiene caracteristicas, lo unico que tenemos que hacer es crear el registro en reference matrix y asociar
								X_XX_VMR_ReferenceMatrix matriz = null;								
								detalle.setXX_PackageMultiple(multiplo_empaque); 
								if ((detalle.getQty()%multiplo_empaque)!=0)
								{
									detalle.setXX_PackageMultiple(1);
								}
								
								boolean asociada = true;
								int productoBeco_ID = 0;
								// buscamos su asociacion con un producto beco
								if (orden.getXX_ImportingCompany_ID()==1000000)
									productoBeco_ID = obtenerAsociacionSinCarac(detalle);
								
								if (productoBeco_ID!=0)
								{
									matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
									matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
									matriz.setXX_VALUE1(0);
									matriz.setXX_VALUE2(0);
									matriz.setXX_COLUMN(0);
									matriz.setXX_ROW(0);
									matriz.setXX_QUANTITYC(detalle.getQty());
									matriz.setXX_QUANTITYV(detalle.getQty()*cant_compra/cant_venta);
									matriz.setXX_QUANTITYO(0);	
									matriz.setM_Product(productoBeco_ID);
									matriz.save();
								}
								else
									asociada = false;

								detalle.setXX_ReferenceIsAssociated(asociada);
								
								
							}
							
							detalle.save();
							
							
						}
						
						

					}
					
					// ya con todas las referecncias cargadas en nuestra orden de compra debemos actualizar
					// la cabecera de la orden de compra
					updateHeader();
					
					// OJO Falta el after save que tiene que ver con la matriz
					
					
					
					
			}	else 
			{
				ADialog.info(1, new Container(), "El Excel es muy ancho para ser cargado. No debe exceder de la columna AD");
			}
		}catch (Exception e) {
			e.printStackTrace();
			rollback();
		} finally {
			
		}
						
						

		return "Referencias Importadas ";	
	}
	
	
	private Vector<Integer> validateCharacteristics(Sheet sheet, MOrder orden) {
		
		String carac_principal_name = "";
		int departamento_ID = orden.getXX_VMR_DEPARTMENT_ID();
		Vector<Integer> errores = new Vector<Integer>();
		
		for (int i = 1; i <sheet.getRows() && !(sheet.getCell(0, i).getContents().equals("FIN")); i++) {

			carac_principal_name = sheet.getCell(5, i).getContents();
			
			if (!carac_principal_name.isEmpty())
			{				
				int linea_ID = getLine_ID(departamento_ID,new Integer(sheet.getCell(2, i).getContents()));
				int seccion_ID = getSection_ID(linea_ID, new Integer(sheet.getCell(3, i).getContents()));

					if (getLongCharact(seccion_ID, carac_principal_name)==-1)
					{
						errores.add(i+1);
					}
				
			}
					
		}		
		
		return errores;
	}


	private void actualizarI(Sheet sheet, int i) {
		
		while (sheet.getCell(0, i).getContents().isEmpty())
		{
			i++;
		}		
	}


	private int obtenerAsociacionSinCarac(MVMRPOLineRefProv detalle) {
		
		int producto = 0;
		String SQL = "SELECT M_Product_ID FROM  M_Product WHERE XX_VMR_VENDORPRODREF_ID = " + detalle.getXX_VMR_VendorProdRef_ID(); 
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				producto = rs.getInt(1);				
		    }

			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
				
		return producto;
	}
	
	private void generateMatrix(Sheet sheet, MVMRPOLineRefProv detalle, int j, int i, int cant_compra, int cant_venta, MOrder orden) {
		
		totalcompra = 0;
		X_XX_VMR_ReferenceMatrix matriz = null;
		int productoBeco_ID = 0;
		boolean asociada = true;

		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(0);
		if (!sheet.getCell(20, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
			// buscamos su asociacion con un producto beco
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}
	
		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(3);
		if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;	
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}

		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(6);
		if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}
	
		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(9);
		if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}	
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}
	
		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(12);
		if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}
	
		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(15);
		if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}
	
		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(18);
		if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}

		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(21);
		if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}

		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(24);
		if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}

		matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
		matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
		matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value1_ID());
		matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
		matriz.setXX_COLUMN(0);
		matriz.setXX_ROW(27);
		if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
		{
			matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
			matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
			totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
			if (orden.getXX_ImportingCompany_ID()==1000000)
				productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value1_ID());
			if (productoBeco_ID!=0)
				matriz.setM_Product(productoBeco_ID);	
			else
				asociada = false;
			matriz.setXX_QUANTITYO(0);
			matriz.save();
		}
		else
		{
			matriz.setXX_QUANTITYC(0);
			matriz.setXX_QUANTITYV(0);
			matriz.setXX_QUANTITYO(0);	
			matriz.save();
		}
		
		j++;
		if (j<i)
		{
			
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value2_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(1);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value2_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}
			
		}	
		
		j++;
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value3_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(2);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value3_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

			
			
		}
		j++;
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value4_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(3);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value4_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

						
		}
		j++;
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value5_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(4);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value5_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}
		
			
		}
		j++;
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value6_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(5);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value6_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}
			
		}
		j++;
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value7_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(6);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value7_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

			
		}
		j++;
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value8_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(7);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value8_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}
			
		}
		j++;
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value9_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(8);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value9_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

			
		}
		j++;
		
		if (j<i)
		{
			
				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value1_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(0);
				if (sheet.getColumns()>20 && !sheet.getCell(20, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(20, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(20, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(20, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value1_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value2_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(3);
				if (sheet.getColumns()>21 && !sheet.getCell(21, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(21, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(21, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(21, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value2_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);;
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value3_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(6);
				if (sheet.getColumns()>22 && !sheet.getCell(22, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(22, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(22, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(22, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value3_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value4_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(9);
				if (sheet.getColumns()>23 && !sheet.getCell(23, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(23, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(23, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(23, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value4_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value5_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(12);
				if (sheet.getColumns()>24 && !sheet.getCell(24, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(24, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(24, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(24, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value5_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value6_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(15);
				if (sheet.getColumns()>25 && !sheet.getCell(25, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(25, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(25, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(25, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value6_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value7_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(18);
				if (sheet.getColumns()>26 && !sheet.getCell(26, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(26, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(26, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(26, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value7_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value8_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(21);
				if (sheet.getColumns()>27 && !sheet.getCell(27, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(27, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(27, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(27, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value8_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value9_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(24);
				if (sheet.getColumns()>28 && !sheet.getCell(28, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(28, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(28, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(28, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value9_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}

				matriz = new X_XX_VMR_ReferenceMatrix(getCtx(), 0, get_Trx());								
				matriz.setXX_VMR_PO_LineRefProv_ID(detalle.getXX_VMR_PO_LineRefProv_ID());
				matriz.setXX_VALUE1(detalle.getXX_Characteristic1Value10_ID());
				matriz.setXX_VALUE2(detalle.getXX_Characteristic2Value10_ID());
				matriz.setXX_COLUMN(9);
				matriz.setXX_ROW(27);
				if (sheet.getColumns()>29 && !sheet.getCell(29, j).getContents().isEmpty())
				{
					matriz.setXX_QUANTITYC(Integer.parseInt(sheet.getCell(29, j).getContents()));
					matriz.setXX_QUANTITYV(Integer.parseInt(sheet.getCell(29, j).getContents())*cant_compra/cant_venta);
					totalcompra = totalcompra + Integer.parseInt(sheet.getCell(29, j).getContents());
					if (orden.getXX_ImportingCompany_ID()==1000000)
						productoBeco_ID = obtenerAsociacion(detalle, detalle.getXX_Characteristic1Value10_ID(),detalle.getXX_Characteristic2Value10_ID());
					if (productoBeco_ID!=0)
						matriz.setM_Product(productoBeco_ID);	
					else
						asociada = false;
					matriz.setXX_QUANTITYO(0);
					matriz.save();
				}
				else
				{
					matriz.setXX_QUANTITYC(0);
					matriz.setXX_QUANTITYV(0);
					matriz.setXX_QUANTITYO(0);	
					matriz.save();
				}
		
		}	
		detalle.setXX_ReferenceIsAssociated(asociada);
		i--;
		
	}
	
	

	private int obtenerAsociacion(MVMRPOLineRefProv detalle,
			int xx_Characteristic1, int xx_Characteristic2) {
			
		int producto = 0;
		String SQL = "SELECT M_Product_ID FROM  M_Product WHERE XX_VMR_VENDORPRODREF_ID = " + detalle.getXX_VMR_VendorProdRef_ID() +
					 " AND M_ATTRIBUTESETINSTANCE_ID IN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where M_ATTRIBUTEVALUE_ID = "+xx_Characteristic1+") " +
					 " AND M_ATTRIBUTESETINSTANCE_ID IN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where (M_ATTRIBUTEVALUE_ID = "+xx_Characteristic2+" or 0 = "+xx_Characteristic2+")) ";
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				producto = rs.getInt(1);				
		    }

			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
				
		return producto;
	}


	private int getCharacteristicType(String caracA1) {
		if (caracA1.equals(""))
			return 0;
		
		String SQL = ("SELECT M_Attribute_ID FROM M_AttributeValue WHERE TRIM(UPPER(NAME))=TRIM(UPPER('" + caracA1 + "'))");
		int attribute_id = -1;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			while(rs.next())
			{
				if (attribute_id > rs.getInt(1) || (attribute_id == -1))
				attribute_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return attribute_id;
	}
	
	private int getCharacteristicType_ID(int caracA1) {

		
		String SQL = ("SELECT M_Attribute_ID FROM M_AttributeValue WHERE M_AttributeValue_ID=" + caracA1);
		int attribute_id = -1;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				attribute_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return attribute_id;
	}

	private boolean updateHeader()
	{
		
		MOrder order = new MOrder(Env.getCtx(), getRecord_ID(), null);
		
		// mejoras a esta parte empleando el modelo y no los updates
		
		MConversionRate conversionRate = new MConversionRate(Env.getCtx(), order.getXX_ConversionRate_ID(), null);
		BigDecimal multiplyRate = conversionRate.getMultiplyRate();
			
		
		String SQL;

		String sql = "";

		sql = "UPDATE C_Order po"											//////////////////////
		+ " SET (XX_ProductQuantity, TotalPVP, XX_TotalPVPPlusTax, TotalLines, XX_TotalCostBs ) ="											//////////////////////
			+ "(SELECT COALESCE(SUM(XX_LineQty),0), COALESCE(SUM(XX_LinePVPAmount),0), COALESCE(SUM(XX_LinePlusTaxAmount),0), COALESCE(SUM(LineNetAmt),0), COALESCE(SUM(LineNetAmt),0)*" + multiplyRate + "  FROM XX_VMR_PO_LINEREFPROV line "
			+ "WHERE po.C_Order_ID=line.C_Order_ID)"
		+ "WHERE po.C_Order_ID=" + getRecord_ID();						//////////////////////
				//////////////////////
		DB.executeUpdate(get_Trx(), sql);

		String sql3 = "UPDATE C_Order po"											//////////////////////
			+ " SET XX_TotalCostBs = TotalLines"											//////////////////////
			+ " WHERE XX_TotalCostBs<1 and po.C_Order_ID=" + getRecord_ID();						//////////////////////
		
		DB.executeUpdate(get_Trx(), sql3);
		

		SQL = ("select count(*) from xx_vmr_po_linerefprov where c_order_id= " + getRecord_ID());
		int cantrefs = 0;
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				cantrefs = rs.getInt(1);
		    }
			rs.close();
		    pstmt.close();
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		}

		String sql2="";
		if (cantrefs!=0)
			sql2 = "update c_order set xx_estimatedmargin=(select round(sum(xx_margin)/"+cantrefs+",2) from xx_vmr_po_linerefprov where c_order_id= "+getRecord_ID()+") where c_order_id=" + getRecord_ID();						//////////////////////
		else 
			sql2 = "update c_order set xx_estimatedmargin=(select round(sum(xx_margin)/1,2) from xx_vmr_po_linerefprov where c_order_id= "+getRecord_ID()+") where c_order_id=" + getRecord_ID();						//////////////////////
			
		DB.executeUpdate(get_Trx(),sql2);	
		
		
		if  (order.getXX_DispatchDate()!=null || order.getXX_ArrivalDate()!=null)
		{					
			
			/** 
			 * 
			 * Logica de la data de imports
			 * Por: JTrias
			 * 
			 */
			
			
			if (order.getXX_OrderType().equals("Importada"))
			{
				
				//XX_INTNACESTMEDAMOUNT
				SQL = ("SELECT A.XX_INTERFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFI " +
				    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
				    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
				    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
				    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
				    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
				    		"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
				    		"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")");
				
				PreparedStatement pstmt = null; 
				ResultSet rs = null;
				try{
					pstmt = DB.prepareStatement(SQL, null); 
					rs = pstmt.executeQuery();
					  
					if(rs.next())
					{
						//debo dividir el valor entre 100 porq es un porcentaje
						BigDecimal aux = rs.getBigDecimal("MEFI").divide(new BigDecimal(100));
						//Lo rendondeo  a 2 decimales
						aux=aux.setScale(2,BigDecimal.ROUND_UP);
						//seteo
						
						sql = "UPDATE C_Order po"											
							+ " SET XX_INTNACESTMEDAMOUNT=" + aux
							+ " WHERE po.C_Order_ID=" + getRecord_ID();						
						DB.executeUpdate(get_Trx(), sql);
				    }
					
				}catch (Exception e) {
					log.log(Level.SEVERE, SQL);
				} finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				
				//XX_NationalEsteemedAmount
				SQL = ("SELECT DISTINCT A.XX_NACFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFN " +
				    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
				    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
				    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
				    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
				    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
				    		"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
				    		"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")");
				    
				pstmt = null; 
			    rs = null;
				 try{
					
					pstmt = DB.prepareStatement(SQL, null); 
				    rs = pstmt.executeQuery();
					 
				    if(rs.next())
				    {
				    	//debo dividir el valor entre 100 porq es un porcentaje
						BigDecimal aux = rs.getBigDecimal("MEFN").divide(new BigDecimal(100));
						//Lo rendondeo  a 2 decimales
						aux=aux.setScale(2,BigDecimal.ROUND_UP);
						//seteo
						sql = "UPDATE C_Order po"											
							+ " SET XX_NationalEsteemedAmount=" + aux
							+ " WHERE po.C_Order_ID=" + getRecord_ID();						
						DB.executeUpdate(get_Trx(), sql);
				    }
				    
				 }catch (Exception e) {
					 log.log(Level.SEVERE, SQL);
				 } finally {
					 DB.closeResultSet(rs);
					 DB.closeStatement(pstmt);
				 }
		
				//XX_CustomsAgentEsteemedAmount
				SQL = ("SELECT A.XX_ESTIMATEDPERTUSAGENT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEAA " +
			    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
			    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
			    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
			    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
			    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
			    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
			    		"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
			    		"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")");
					
				pstmt = null; 
			    rs = null;
				 try{
						
					pstmt = DB.prepareStatement(SQL, null); 
				    rs = pstmt.executeQuery();
						 
				    if(rs.next())
				    {
				    	//debo dividir el valor entre 100 porq es un porcentaje
						BigDecimal aux = rs.getBigDecimal("MEAA").divide(new BigDecimal(100));
						//Lo rendondeo  a 2 decimales
						aux=aux.setScale(2,BigDecimal.ROUND_UP);
						//seteo
						sql = "UPDATE C_Order po"											
							+ " SET XX_CustomsAgentEsteemedAmount=" + aux
							+ " WHERE po.C_Order_ID=" + getRecord_ID();						
						DB.executeUpdate(get_Trx(), sql);
				    }
				 }catch (Exception e) {
					 log.log(Level.SEVERE, SQL);
				 } finally {
					 DB.closeResultSet(rs);
					 DB.closeStatement(pstmt);
				 }
				 
				//XX_EsteemedInsuranceAmount
				 BigDecimal costo = new BigDecimal(0);
				 BigDecimal impuesto = new BigDecimal(0);
				 
				 SQL = ("SELECT G.XX_RATE,(C.TOTALLINES * H.MULTIPLYRATE) AS Costo " +
				    		"FROM C_ORDER C, XX_VLO_DISPATCHROUTE G, C_CONVERSION_RATE H " +
				    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
				    		"AND C.XX_VLO_DISPATCHROUTE_ID = G.XX_VLO_DISPATCHROUTE_ID " +
				    		"AND H.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " );
						    
				 pstmt = null; 
				 rs = null;
				 try{
							
					 pstmt = DB.prepareStatement(SQL, null); 
					 rs = pstmt.executeQuery();
					    
					 if(rs.next())
					 {
						 costo = rs.getBigDecimal("Costo");
					     costo = costo.add(order.getXX_NationalEsteemedAmount());
					     costo = costo.add(order.getXX_INTNACESTMEDAMOUNT());
					     costo = costo.add(impuesto);
					     costo = costo.multiply(rs.getBigDecimal("XX_RATE"));
					     //debo dividir el valor entre 100 porq es un porcentaje
					     costo = costo.divide(new BigDecimal(100));
					     
					     BigDecimal aux = costo;
						 //Lo rendondeo  a 2 decimales
						 aux=aux.setScale(2,BigDecimal.ROUND_UP);
						 //seteo
							sql = "UPDATE C_Order po"											
								+ " SET XX_ESTEEMEDINSURANCEAMOUNT=" + aux
								+ " WHERE po.C_Order_ID=" + getRecord_ID();						
							DB.executeUpdate(get_Trx(), sql);
					 }
						    
				}catch (Exception e) {
					log.log(Level.SEVERE, SQL);
				} finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				
				//Guardo la orden de compra
				get_Trx().commit();
			}
			
//			POLimits(order);
			
		}//fin del Arrival date null
		
		return true;
	}	//	updateHeader
	
	

	private BigDecimal getTaxRate(int impuesto_ID) {

		String sql = "SELECT rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + impuesto_ID+")";	


		PreparedStatement prst = DB.prepareStatement(sql,null);
		BigDecimal Tax = new BigDecimal(1);
		try {
			ResultSet rs = prst.executeQuery();

			while (rs.next()){
				Tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al buscar la tasa de impuesto " + e);
		}
		return Tax;
	}

	private BigDecimal getLastSalePrice(int referencia_ID) {

		String sqlPrice = "SELECT MAX(C.XX_SALEPRICE) "+
		"\nFROM XX_VMR_PRICECONSECUTIVE C JOIN  M_PRODUCT  P  ON (P.M_PRODUCT_ID = C.M_PRODUCT_ID) "+
		"\nWHERE  P.XX_VMR_VENDORPRODREF_ID = "+referencia_ID+ 
		"\nAND C.CREATED IN (SELECT MAX(C2.CREATED) "+
		"\nFROM XX_VMR_PRICECONSECUTIVE C2 JOIN  M_PRODUCT  P2  ON (P2.M_PRODUCT_ID = C2.M_PRODUCT_ID) "+
		"\nWHERE  P2.XX_VMR_VENDORPRODREF_ID = "+referencia_ID+
		"\nAND C2.XX_CONSECUTIVEORIGIN = 'P')";
		
		BigDecimal ultimoPrecio = new BigDecimal(0);
		PreparedStatement pstmtPrice = null;
		ResultSet rsPrice = null;
		try{
			//System.out.println(sqlPrice);
			pstmtPrice = DB.prepareStatement(sqlPrice, null);
			rsPrice = pstmtPrice.executeQuery();
			
			if(rsPrice.next()){
				ultimoPrecio = rsPrice.getBigDecimal(1);
			}		
		}
		catch (SQLException e){
			System.out.println("Error al buscar el precio referencial " + e);
		}finally{
			try {
				rsPrice.close();
				pstmtPrice.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		
		return ultimoPrecio;
		
	}

	private int getAttributeSet(int seccion_ID) {
		
		String sql = "SELECT M_AttributeSet_ID"
			+ " FROM M_AttributeSet"
			+ " WHERE M_AttributeSet_ID=(select M_Attributeset_ID from XX_VMR_DynamicCharact  where XX_VMR_Section_ID="+seccion_ID+")";					
	
		PreparedStatement prst = DB.prepareStatement(sql,null);
		Integer Attribute_ID = 0;
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				Attribute_ID = rs.getInt("M_AttributeSet_ID");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al captar el atributte set al seleccionar la linea en funcion SetAttribute");			
		}
		
		return Attribute_ID;
	}

	private int getConceptoValor(int departamento_ID, int marca_ID) {
		String SQL =  "SELECT XX_VME_CONCEPTVALUE_ID"											
			+ " FROM XX_VMR_CONCEPTVALDPTBRAND"
			+ " WHERE ISACTIVE = 'Y' AND XX_VMR_DEPARTMENT_ID=" + departamento_ID + " AND XX_VMR_BRAND_ID=" + marca_ID;						
		int concepto_id = -1;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				concepto_id = rs.getInt(1);
		    } 
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return concepto_id;
	}

	private String getSeccionName(int seccion_ID) {
		
		String SQL = "SELECT NAME FROM XX_VMR_SECTION WHERE XX_VMR_SECTION_ID=" + seccion_ID;
		String name = "";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				name = rs.getString(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return name;
		
	}

	private int getReferencia(int proveedor_ID, int seccion_ID, int marca_ID, int caract_larga_ID,
			int impuesto_ID, int uniCompra_ID, int canCompra_ID,int uniVenta_ID, int canVenta_ID, String value) {
		
		String SQL = "";
		if (caract_larga_ID!=0)
			SQL = ("SELECT XX_VMR_VENDORPRODREF_ID FROM XX_VMR_VENDORPRODREF WHERE ISACTIVE='Y' AND C_BPARTNER_ID="+proveedor_ID+
						" AND XX_VMR_SECTION_ID=" + seccion_ID + " AND XX_VMR_BRAND_ID="+marca_ID+
						" AND XX_VMR_LONGCHARACTERISTIC_ID="+caract_larga_ID+
						" AND TRIM(UPPER(VALUE))=TRIM(UPPER('" + value + "'))");
		else
			SQL = ("SELECT XX_VMR_VENDORPRODREF_ID FROM XX_VMR_VENDORPRODREF WHERE ISACTIVE='Y' AND C_BPARTNER_ID="+proveedor_ID+
					" AND XX_VMR_SECTION_ID=" + seccion_ID + " AND XX_VMR_BRAND_ID="+marca_ID+
					" AND XX_VMR_LONGCHARACTERISTIC_ID IS NULL " +
					" AND TRIM(UPPER(VALUE))=TRIM(UPPER('" + value + "'))");
		int reference_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				reference_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return reference_id;
	}



	private int getAttribute(String caracA1, int tipoCarac2) {
		if (caracA1.equals(""))
			return 0;
		
		String SQL = ("SELECT M_AttributeValue_ID FROM M_AttributeValue WHERE m_attribute_id=" + tipoCarac2 + " and TRIM(UPPER(NAME))=TRIM(UPPER('" + caracA1 + "'))");
		int attribute_id = -1;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				attribute_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return attribute_id;
	}

	private int getCanVenta(String uniVenta, int cant_venta) {
		String SQL = ("SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UnitConversion =" + cant_venta + " AND XX_VMR_UnitPurchase_ID IN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE TRIM(UPPER(NAME))=TRIM(UPPER('"+uniVenta+"')))");
		int purchaseUnit_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				purchaseUnit_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return purchaseUnit_id;
	}

	private int getSaleUnit(int canVenta_ID) {
		String SQL = ("SELECT XX_VMR_UnitPurchase_ID FROM XX_VMR_UnitConversion WHERE xx_vmr_unitconversion_id=" + canVenta_ID);
		int purchaseUnit_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				purchaseUnit_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return purchaseUnit_id;
	}

	private int getCanCompra(String uniCompra, int cant_compra) {
		String SQL = ("SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UnitConversion =" + cant_compra + " AND XX_VMR_UnitPurchase_ID IN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE TRIM(UPPER(NAME))=TRIM(UPPER('"+uniCompra+"')))");
		int purchaseUnit_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				purchaseUnit_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return purchaseUnit_id;
	}

	private int getPurchaseUnit(int canCompra_ID) {
		String SQL = ("SELECT XX_VMR_UnitPurchase_ID FROM XX_VMR_UnitConversion WHERE xx_vmr_unitconversion_id=" + canCompra_ID);
		int purchaseUnit_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				purchaseUnit_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return purchaseUnit_id;
	}

	private int getTaxCategory(String impuesto) {
		int taxCategory = 0;
		if (impuesto.equalsIgnoreCase("SI"))
			taxCategory = Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_IVA_ID");
			//taxCategory = 1000017;
		else
			taxCategory = Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID");
			//taxCategory = 1000013;
				
		return taxCategory;
	}

	private int getLongCharact(int seccion_ID, String carac_principal_name) {
		String SQL = ("SELECT XX_VMR_LongCharacteristic_ID FROM XX_VMR_LongCharacteristic WHERE XX_VMR_SECTION_ID=" + seccion_ID + " AND TRIM(UPPER(NAME))=TRIM(UPPER('" + carac_principal_name + "'))");
		int longCharact_id = -1;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				longCharact_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return longCharact_id;
	}

	private int getBrand_ID(String marca_name) {
		String SQL = ("SELECT XX_VMR_BRAND_ID FROM XX_VMR_BRAND WHERE TRIM(UPPER(NAME)) =TRIM(UPPER('"+marca_name+"'))");
		int brand_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				brand_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return brand_id;
	}

	private int getSection_ID(int linea_ID, int seccion_value) {
		String SQL = ("SELECT XX_VMR_SECTION_ID FROM XX_VMR_SECTION WHERE XX_VMR_LINE_ID=" + linea_ID + " AND VALUE=" + seccion_value);
		int section_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				section_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return section_id;
	}

	private int getLine_ID(int department_ID, int linea_value) {
		String SQL = ("SELECT XX_VMR_LINE_ID FROM XX_VMR_LINE WHERE XX_VMR_DEPARTMENT_ID=" + department_ID + " AND VALUE=" + linea_value);
		int line_id = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				line_id = rs.getInt(1);
		    }
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return line_id;
	}


}

