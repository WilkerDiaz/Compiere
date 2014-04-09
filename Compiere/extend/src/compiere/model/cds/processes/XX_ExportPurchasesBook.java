package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import org.compiere.model.X_Ref_PriorityImplementation;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.MInvoice;
import compiere.model.cds.X_E_XX_VCN_ESTD01;
import compiere.model.cds.X_Ref_M_Product_ProductType;
import compiere.model.cds.X_Ref_XX_TypeReg;
import compiere.model.payments.X_Ref_XX_OrderType;
import compiere.model.payments.X_Ref_XX_POType_LV;

/*
* Proceso que sincroniza los registros relacionados con el Libro de Compras a la tabla E_XX_VCN_ESTD01.
* 
* @author Claudia Afonso.
*/

public class XX_ExportPurchasesBook extends SvrProcess{

	private X_E_XX_VCN_ESTD01 ESTD01 = null;
	
	@Override
	protected String doIt() throws Exception {
		
		//Borrado de la tabla
		System.out.println("Borramos de la E_XX_VCN_ESTD01");
		String sql_delete = "DELETE E_XX_VCN_ESTD01";
		DB.executeUpdate(get_Trx(),sql_delete);
		
		System.out.println("Guardamos en la E_XX_VCN_ESTD01");
		
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		
		String sqlConsulta = null;
		PreparedStatement pstmtConsulta = null;
		ResultSet rsConsulta = null;
		
		BigDecimal costoTotal = new BigDecimal(0.000);
		BigDecimal baseImponible = new BigDecimal(0.000);
		BigDecimal baseExenta = new BigDecimal(0.000);
		BigDecimal baseNoSujeta = new BigDecimal(0.000);
		BigDecimal impuesto = new BigDecimal(0.000);
		BigDecimal retencion = new BigDecimal(0.000);
		BigDecimal tasa = new BigDecimal(0.000);
		
		String fechaDocumento;
		Date fechaCreacion;
	
		String documentNoFactura = null, nombre = null, rif = null, tipoPersona = null, pais, paisString = null, origen, numeroControl,
				statusEliminacion, numeroAviso = "", fechaString, numeroExpediente, tienda;
		Integer dia = null, mes = null, año = null, codigoProveedor = null, compañia, codigoFactura, codigoOrden, 
				numeroComprobante, documentNoID, proveedorID, documentNoOrden = null, 
				numeroPlanilla, IDpurchasesBook;
			
		sql = "select (Select c.value from m_warehouse c where c.m_warehouse_id=a.m_warehouse_id) m_warehouse, " 
				+"a.c_invoice_id, a.c_order_id, a.c_Bpartner_ID, ROUND(XX_TotalInvCost,2) XX_TotalInvCost, a.created, " 
				+"NVL((select b.RATE from c_tax b where b.c_tax_id=a.c_tax_id),0) c_tax_id, "
				+"ROUND(XX_TaxableBase,2) XX_TaxableBase, ROUND(XX_ExemptBase,2) XX_ExemptBase, ROUND(XX_NotSubjectBase,2) XX_NotSubjectBase," 
				+"ROUND(XX_TaxAmount,2) XX_TaxAmount, NVL(XX_withholding,0) XX_withholding,  "
				+"NVL(XX_ControlNumber,0) XX_ControlNumber, ROUND(XX_withholdingTax,2) XX_withholdingTax, XX_DocumentDate, "
				+"NVL(XX_DocumentNo,' ') XX_DocumentNo, NVL(XX_ExpedientNumber,0) XX_ExpedientNumber, XX_VCN_PURCHASESBOOK_ID "  
				+"from xx_vcn_purchasesbook a "
				+"where c_invoice_id is not null "
				+"and xx_synchronizedpb = 'N'  "
				+"order by c_order_id";

		pstmt = DB.prepareStatement(sql, null);
		
		try {
			
			rs = pstmt.executeQuery();
			
			compañia = 8;
			origen = "A";
			numeroPlanilla = 0;
			statusEliminacion = "";
			MInvoice invoiceObj = null;	
			
			while (rs.next()){
				
				tienda = rs.getString("M_WAREHOUSE");
				codigoFactura = rs.getInt("C_INVOICE_ID");				// ID de la factura afectada.
				
				invoiceObj = new MInvoice( Env.getCtx(), codigoFactura, null); 
				
				if(invoiceObj.getXX_InvoiceType().equalsIgnoreCase(X_Ref_M_Product_ProductType.ITEM.getValue()))
					origen = "A";
				else
					origen = "M";
				
				codigoOrden = rs.getInt("C_ORDER_ID");
				proveedorID = rs.getInt("C_BPARTNER_ID");
				costoTotal = rs.getBigDecimal("XX_TOTALINVCOST");
				baseImponible = rs.getBigDecimal("XX_TAXABLEBASE");
				baseExenta = rs.getBigDecimal("XX_EXEMPTBASE");
				baseNoSujeta = rs.getBigDecimal("XX_NOTSUBJECTBASE");
				impuesto = rs.getBigDecimal("XX_TAXAMOUNT");
				retencion = rs.getBigDecimal("XX_WITHHOLDINGTAX");
				numeroComprobante = rs.getInt("XX_WITHHOLDING");
				numeroControl = rs.getString("XX_CONTROLNUMBER");
				fechaDocumento = rs.getString("XX_DOCUMENTDATE");
				numeroAviso = rs.getString("XX_DOCUMENTNO");			// ID de la factura acutal.
				numeroExpediente = rs.getString("XX_EXPEDIENTNUMBER");
				IDpurchasesBook = rs.getInt("XX_VCN_PURCHASESBOOK_ID");
				fechaCreacion = rs.getDate("CREATED");
				tasa = rs.getBigDecimal("C_TAX_ID");
				
				fechaDocumento = fechaDocumento.toString().substring(0, 4) + fechaDocumento.toString().substring(5, 7) + fechaDocumento.toString().substring(8, 10);						
				
				//Cerrar Prepare y Result WDIAZ
				DB.closeStatement(pstmtConsulta);
				DB.closeResultSet(rsConsulta);
				
				/*
				 * El siguiente código toma la fecha de creación del registro y la divide en cadenas para obtener dia, mes y año.
				 * */
				
				fechaString = fechaCreacion.toString();
				String fechaTokenizer = fechaString;
				StringTokenizer tokens = new StringTokenizer(fechaTokenizer, "-");   
				Integer count = tokens.countTokens();
				
				for (int i = 1; i <= count; i++) {
					
					if (i==1){
						año = Integer.parseInt(tokens.nextToken());
						
					}else if (i==2){
						mes = Integer.parseInt(tokens.nextToken());
						
					}else if (i==3){
						dia = Integer.parseInt(tokens.nextToken());
					}
				}
						

				/*
				 * Sentencia que devuelve los datos del proveedor asociado al registro de la factura actual.
				 * */
				
				sqlConsulta = "select value, name, XX_TypePerson, XX_CI_RIF " +
							"from c_bpartner " +
							"where c_bpartner_id = "+proveedorID+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					
					codigoProveedor = rsConsulta.getInt("VALUE");
					if(rsConsulta.getString("NAME").length()>35)
						nombre = rsConsulta.getString("NAME").substring(0, 35);
					else
						nombre = rsConsulta.getString("NAME");
					
					tipoPersona = rsConsulta.getString("XX_TYPEPERSON");
					rif = rsConsulta.getString("XX_CI_RIF");
				}
				
				if (codigoProveedor == null){
					
					codigoProveedor = 0;
				}
			
				//Cerrar Prepare y Result WDIAZ
				DB.closeStatement(pstmtConsulta);
				DB.closeResultSet(rsConsulta);
				
				/*
				 * Sentencia que devuelve el país de distribución de la O/C.
				 * */
	
				sqlConsulta = "select name from c_country where c_country_id = (select c_country_id from c_order where c_order_id="+codigoOrden+")";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
	
				while (rsConsulta.next()){
					
					paisString = rsConsulta.getString(1);
				}
				
				if (paisString.equalsIgnoreCase("VENEZUELA")){
					
					pais = "N";
				}else{
					
					pais = "E";
				}
				
				//Cerrar Prepare y Result WDIAZ
				DB.closeStatement(pstmtConsulta);
				DB.closeResultSet(rsConsulta);
				
				/*
				 * Sentencia que devuelve el número de documento de la factura actual.
				 * */

				sqlConsulta = "SELECT DOCUMENTNO FROM C_INVOICE WHERE C_INVOICE_ID = "+codigoFactura+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
				
				while (rsConsulta.next()){
					
					documentNoFactura = rsConsulta.getString("DOCUMENTNO");	
				}
				
				//Cerrar Prepare y Result WDIAZ
				DB.closeStatement(pstmtConsulta);
				DB.closeResultSet(rsConsulta);
				
				/*
				 * Sentencia que devuelve el número de documento de la O/C.
				 * */

				sqlConsulta = "SELECT DOCUMENTNO FROM M_INOUT WHERE XX_POTYPE = 'POM' AND C_ORDER_ID = "+codigoOrden+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
				documentNoOrden=0;
				
				while (rsConsulta.next()){
					
					documentNoOrden = Integer.parseInt(rsConsulta.getString("DOCUMENTNO"));	
				}
				
				//Cerrar Prepare y Result WDIAZ
				DB.closeStatement(pstmtConsulta);
				DB.closeResultSet(rsConsulta);
				
		/*		System.out.println(codigoTienda+" "+codigoFactura+" "+codigoOrden+" prov: "+proveedorID+" - "+costoTotal+" "+
						baseImponible+" "+baseExenta+" "+baseNoSujeta+" "+impuesto+" "+retencion+" "+numeroComprobante+" "+
						numeroControl+" "+fechaDocumento+" "+numeroAviso+" "+numeroExpediente);
		*/		
			//	if (existe){
				
					insertar(compañia, tienda, documentNoFactura, documentNoOrden, codigoProveedor, nombre, 
							tipoPersona, rif, pais, costoTotal, baseImponible, baseNoSujeta, impuesto, origen,
							numeroComprobante, numeroControl, new Integer(fechaDocumento), retencion, numeroPlanilla, 
							numeroExpediente, statusEliminacion, numeroAviso, baseExenta, IDpurchasesBook,
							dia, mes, año, tasa);
					
					ESTD01.save();
					
	//			}
	//			else{
					
	//			}
				
				// Incializo todas las variables para el próximo registro.
				
				tienda = "";  
				documentNoFactura = null;
				documentNoOrden = null; 
				codigoProveedor = 0;
				nombre = null;
				tipoPersona = null;
				rif = null;
				pais = null;
				costoTotal = Env.ZERO;
				baseImponible = Env.ZERO;
				baseNoSujeta = Env.ZERO;
				impuesto = Env.ZERO;
				numeroComprobante = null;
				numeroControl = null;
				fechaDocumento = null;
				retencion = null; 
				numeroExpediente = null;
				numeroAviso = null;
				baseExenta = Env.ZERO;
				codigoFactura = null;
				documentNoID = null;
				dia = null;
				mes = null;
				año = null;
				tasa = null;
				
			}
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			return "Fallo en Sincronización";	
		} finally {
			DB.closeResultSet(rsConsulta);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmtConsulta);
			DB.closeStatement(pstmt);
		}

		//Exportamos al AS
		System.out.println("Exportamos al AS");
		exportE_XX_VMR_ESTD01DB2();
		
		System.out.println("FIN");
		
		return "Sincronización Completa";
	}
		
	@Override	
	protected void prepare() {
			
	}
	
	/*
	 * Procedimiento en el cuál se realiza el set de los datos para la tabla E_XX_VCN_ESTD01.
	 * 
	 *  @param compañia campo que siempre lleva el valor 8.
	 *  @param tienda dato del AS con el ID de la tienda.
	 *  @param documentNoFactura número del documento de la factura.
	 *  @param documentNoOrden número del documento de la O/C.
	 *  @param codigoProveedor dato del AS con el ID del proveedor.
	 *  @param nombre es el nombre del proveedor.
	 *  @param tipoPersona si el tipo de proveedor es Natural o Jurídico.
	 *  @param rif cédula o rif del proveedor.
	 *  @param país el pais de distribuciòn de la O/C. 'N' si es Venezuela y 'E' para las importadas.
	 *  @param costoTotal montol total de la factura sin IVA.
	 *  @param baseImponible
	 *  @param baseNoSujeta
	 *  @param impuesto 
	 *  @param origen campo que siempre lleva el dato 'A'.
	 *  @param numeroComprobante es el número de comprobante de retención de la factura.
	 *  @param numeroControl es el número de control de la factura.
	 *  @param fechaDocumento es la fecha de la factura.
	 *  @param retencion es el monto de la retencíon.
	 *  @param numeroPlanilla campo que siempre va en blanco.
	 *  @param numeroExpediente es el número del expediente del registro.
	 *  @param statusEliminacion campo que siempre va en blanco.
	 *  @param numeroAviso es el número del documento del aviso o nota.
	 *  @param baseExenta 
	 *  @param IDpurchasesBook es el ID del registro en la tabla XX_VCN_PURCHASESBOOK.
	 *  @param dia es la fecha en que carga la factura en el sistema.
	 *  @param mes es la fecha en que carga la factura en el sistema.
	 *  @param año es la fecha en que carga la factura en el sistema.
	 *  @param tasa es la descripción de la Tasa del Impuesto IVA.
	 *  
	 */
	
	public void insertar (int compañia, String tienda, String documentNoFactura,
			Integer documentNoInOut, int codigoProveedor, String nombre, String tipoPersona, String rif, String pais, 
			BigDecimal costoTotal, BigDecimal baseImponible, BigDecimal baseNoSujeta, BigDecimal impuesto, 
			String origen, int numeroComprobante, String numeroControl, int fechaDocumento, BigDecimal retencion, 
			Integer numeroPlanilla, String numeroExpediente, String statusEliminacion, String numeroAviso, 
			BigDecimal baseExenta, int IDpurchasesBook, int dia, int mes, int año, BigDecimal tasa){
		
		ESTD01  = new X_E_XX_VCN_ESTD01(getCtx(), 0, get_TrxName());
				
        try{
        	ESTD01.setXX_Company(compañia);
        	ESTD01.setXX_StoreCode(tienda);
        	ESTD01.setXX_DocumentNoInvoice(documentNoFactura);
        	ESTD01.setXX_DocumentNoOrder(documentNoInOut);
        	ESTD01.setC_BPartner_ID(codigoProveedor);
        	ESTD01.setXX_NameOfVendor(nombre);
        	ESTD01.setXX_TypePerson(tipoPersona);
        	ESTD01.setXX_CI_RIF(rif);
        	ESTD01.setCountryName(pais);
        	ESTD01.setXX_TotalInvCost(costoTotal);
        	ESTD01.setXX_TaxableBase(baseImponible);
        	ESTD01.setXX_NotSubjectBase(baseNoSujeta);
        	ESTD01.setXX_TaxAmount(impuesto);
        	ESTD01.setXX_Origin(origen);
            ESTD01.setXX_Withholding(numeroComprobante);
            ESTD01.setXX_ControlNumber(numeroControl);
            ESTD01.setXX_WithholdingTax(retencion);
        	ESTD01.setXX_DocumentDate(fechaDocumento);
        	ESTD01.setXX_PayrollNumber(numeroPlanilla);
        	ESTD01.setXX_ExpedientNumber(numeroExpediente);
        	ESTD01.setXX_StateOfElimination(statusEliminacion);
        	ESTD01.setXX_ControlNumberDebCre(numeroAviso);
        	ESTD01.setXX_ExemptBase(baseExenta);
        	ESTD01.setAD_Org_ID(0);
        	ESTD01.setXX_DayCreate(dia);
        	ESTD01.setXX_MonthCreate(mes);
        	ESTD01.setXX_YearCreate(año);
        	ESTD01.setXX_Tax(tasa);
       			
			actualizarRegistroCompiere (IDpurchasesBook);
			
        }
        catch (Exception e) {
        	System.out.println(ESTD01.getXX_DocumentNoOrder());
        	e.printStackTrace();   
        }
	}
	
	/*
	 * Procedimiento que actualiza en la tabla XX_VCN_PURCHASESBOOK el campo XX_SYNCHRONIZEDPB a 'Y' para indicar que el registro ya
	 * fue exportado.
	 * 
	 *  @param invoiceID es el ID de la factura en la tabla XX_VCN_PURCHASESBOOK.
	 */
	
	public void actualizarRegistroCompiere(int IDpurchasesBook)
	{
		String sqlUpdate = null;
		PreparedStatement pstmtUpdate = null;

		try{
			sqlUpdate = "UPDATE XX_VCN_PURCHASESBOOK SET XX_SYNCHRONIZEDPB ='Y' WHERE XX_VCN_PURCHASESBOOK_ID= " + IDpurchasesBook;
			DB.executeUpdate(get_TrxName(),sqlUpdate);
		}
	    catch (Exception e) {
	        	
	    	e.printStackTrace();   
		} finally {
			DB.closeStatement(pstmtUpdate);
		}
	}
	
	/*
	 * Exporta la E_XX_VMR_NRAD01 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_ESTD01DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS;
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
        Statement sentencia = null;
        try
        {    
        	
        	//Borra la data de la tabla NRAD01C en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.ESTD01C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere =  "SELECT XX_COMPANY, XX_STORECODE, XX_DOCUMENTNOINVOICE, XX_DOCUMENTNOORDER, " 
	        					+ "XX_DAYCREATE, XX_MONTHCREATE, XX_YEARCREATE,C_BPARTNER_ID, XX_NAMEOFVENDOR, " 
	        					+ "XX_TYPEPERSON, XX_CI_RIF, COUNTRYNAME, XX_TAX, XX_TOTALINVCOST, XX_TAXABLEBASE,"        
	        					+ "XX_EXEMPTBASE, XX_NOTSUBJECTBASE, XX_TAXAMOUNT, XX_ORIGIN, XX_WITHHOLDING, "
	        					+ "NVL(XX_CONTROLNUMBER, ' ') XX_CONTROLNUMBER, XX_WITHHOLDINGTAX, XX_DOCUMENTDATE,"    
	        					+ "NVL(XX_CONTROLNUMBERDEBCRE,' ') XX_CONTROLNUMBERDEBCRE, XX_EXPEDIENTNUMBER "
	        					+"FROM E_XX_VCN_ESTD01";
	        	
	        	PreparedStatement ps_Compiere = DB.prepareStatement(SQL_Compiere,get_TrxName());
	        	ResultSet rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.ESTD01C (CIAS, COTIEN, COFACT, CONOTA, CODIAS, COMESS, "
		            			 +"COAÑOS,COPROV, NOMBRE, CODRIF, COPRIF, COPAIS, TASAS, COCOST, COBASE,"      
		            			 +"COBASEX, COBASENS, COIMPU, COORIG, NROCOM, NUMCTL, MONRET, FECDOC,"
		            			 +"DEBCRE, NUMEXP) "
				            	+ " VALUES("
			                	    + rs.getInt("XX_COMPANY") +"," 
			                	    + rs.getInt("XX_STORECODE") +",'" 
			                	    + rs.getString("XX_DOCUMENTNOINVOICE") +"'," 
			                		+ rs.getInt("XX_DOCUMENTNOORDER") +","
			                		+ rs.getInt("XX_DAYCREATE") +"," 
			               			+ rs.getInt("XX_MONTHCREATE") +"," 
			               			+ rs.getInt("XX_YEARCREATE") +"," 
			               			+ rs.getInt("C_BPARTNER_ID") +",'" 
			                		+ rs.getString("XX_NAMEOFVENDOR") +"','" 
			                		+ rs.getString("XX_TYPEPERSON") +"','"
			                		+ rs.getString("XX_CI_RIF") +"','" 
			                		+ rs.getString("COUNTRYNAME") +"','" 
			                		+ rs.getString("XX_TAX") +"'," 
			                		+ rs.getBigDecimal("XX_TOTALINVCOST") +"," 
			                		+ rs.getBigDecimal("XX_TAXABLEBASE") +"," 
			                		+ rs.getBigDecimal("XX_EXEMPTBASE") +"," 
			                		+ rs.getBigDecimal("XX_NOTSUBJECTBASE") +"," 
			                		+ rs.getBigDecimal("XX_TAXAMOUNT") +",'" 
			                		+ rs.getString("XX_ORIGIN") +"'," 
			                		+ rs.getInt("XX_WITHHOLDING") +",'" 
			                		+ rs.getString("XX_CONTROLNUMBER") +"',"
			                		+ rs.getBigDecimal("XX_WITHHOLDINGTAX") +",'" 
			                		+ rs.getString("XX_DOCUMENTDATE") +"','" 
			                		+ rs.getString("XX_CONTROLNUMBERDEBCRE") +"','" 
			                		+ rs.getString("XX_EXPEDIENTNUMBER") 
			                		+"')" ;
			                		
						if(i==9001 || i==18000){
							As.desconectar();
							As.conectar();
						}
						            
			            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            r2 = As.realizarSentencia(SQL_AS, sentencia);
			            
			            if(r2<0) //Si la inserción da error
			    			return r2;   	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR" +SQL_AS);
						e.printStackTrace();
					}	           
	        	}
        	}
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		} finally {
			DB.closeStatement(sentencia);
		}
	
        As.desconectar();
		return r;
		
	}

}
