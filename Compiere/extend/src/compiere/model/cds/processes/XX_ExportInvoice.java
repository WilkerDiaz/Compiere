package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MPaymentTerm;
import compiere.model.cds.X_E_XX_VCN_CPPD01;
import compiere.model.cds.X_E_XX_VMR_NRAM02;

/*
 * Proceso que sincroniza los registros relacionados con facturación y avisos de proveedores en Compiere
 * a la tabla E_XX_VCN_CPPD01. 
 * 
 * @author Claudia Afonso.
 */

public class XX_ExportInvoice extends SvrProcess {

	private X_E_XX_VCN_CPPD01 CPPD01 = null;

	@Override
	protected String doIt() throws Exception {

		//Borrado de la tabla
		System.out.println("Borramos de la E_XX_VCN_CPPD01");
		String sql_delete = "DELETE E_XX_VCN_CPPD01";
		DB.executeUpdate(null,sql_delete);
		
		//Borrado de la tabla
		System.out.println("Borramos de la E_XX_VMR_NRAM02");
		sql_delete = "DELETE E_XX_VMR_NRAM02";
		DB.executeUpdate(null,sql_delete);
		
		System.out.println("Guardamos en la E_XX_VCN_CPPD01");
		
		System.out.println("Desde Invoice");
		 
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sqlConsulta = null;
		PreparedStatement pstmtConsulta = null;
		ResultSet rsConsulta = null;
	
		Date fechaAprobacion;
		Date fechaAPR;
		Date fechaVencimiento;
		Date fechaAjuste;
		BigDecimal montoMovimiento = new BigDecimal(0.00);
		BigDecimal montoImpuesto = new BigDecimal(0.00);	
		BigDecimal factorCampo = new BigDecimal(0.00);
		BigDecimal montoTruncate = new BigDecimal(0.00);
		BigDecimal impuestoTruncate = new BigDecimal(0.00);
		
		
		Integer codigoProveedor = 0, consecutivoFactura = 0, consecutivoAvisoDeb = 0, consecutivoAvisoCre = 0,
				consecutivo = 0, consecutivo2 = 0, consecutivo3 = 0, diaReg = null,	mesReg = null,	añoReg = null;
		Integer diaMovimiento = null, mesMovimiento = null, añoMovimiento = null, diaVencimiento = null, mesVencimiento = null,
				añoVencimiento = null, factura = null, debito =null, credito = null;
		Integer condicionPago, numeroComprobante = null, ref_invoice_id, diasFinanciamiento = 0;
		Integer bPartnerID, orderID, invoiceID, formaPago, decimalPlaces = 2, inout=0;
		
		Integer diaAprobacion = 0, mesAprobacion = 0, añoAprobacion = 0, mesAnulacion = 0,	añoAnulacion = 0, diaAnulacion = 0;
		Integer mesAnticipo = 0, añoAnticipo = 0, diaAnticipo = 0, ordenPago = 0;
		Integer statusGrabacion = 1, statusCuenta = 5, tipo = 0, codigoMoneda = 1, codPais;
	
		String statusGiro = "", descripcion = "PAGO A PROVEEDORES", tipoProveedor = "PRO";
		String numeroAviso = null, fechaCadena, numeroControl, fecha, descripcionCondicionPago = null, pais = null;
		String invoiceDocumentNo, status,documentoAfectado = null, tipoMovimiento = null, documentoOrigen;
		
		X_E_XX_VMR_NRAM02 receipt_M02=null;
		String fechaFactura, dia, mes, ano;
		
		/*
		 * Sentencia que toma todas las facturas, avisos, notas de débito y crédito
		 * que se encuentran completadas y aprobadas y que no estan sincronizadas. 
		 * */
		
		sql = "select mio.Documentno as NUMREC, ci.C_BPARTNER_ID, ci.C_Order_ID, ci.C_Invoice_ID, (Select a.value from C_PaymentTerm a where a.C_PaymentTerm_ID=co.C_PaymentTerm_ID) C_PaymentTerm_ID, ci.TotalLines, ci.XX_TaxAmount, " +
	    	"ci.XX_ControlNumber, co.XX_RECEPTIONDATE, co.XX_INVOICEDATE, ci.ref_invoice_id, ci.created, ci.DOCSTATUS, co.XX_InvoicingStatus, co.paymentRule, " +
	    	"ci.C_DOCTYPETARGET_ID, co.DOCUMENTNO ORDERDOCUMENTNO, ci.DOCUMENTNO INVOICEDOCUMENTNO, co.c_country_id, NVL(co.XX_REPLACEMENTFACTOR, 0) AS FACCAM, " +
	    	"CASE WHEN CC.ISO_CODE='VEB' THEN 1 " +
		    "WHEN CC.ISO_CODE='USD' THEN 2 " +
		    "WHEN CC.ISO_CODE='EUR' THEN 6 " +
		    "END AS CODMON, " +
		    "cb.VALUE AS COEMPE, " +
		    "mw.VALUE AS TIEFAC, mio.XX_COUNTEDPACKAGESQTY AS BULREC " +
	    	"from c_invoice ci, c_order co, m_inout mio, C_Currency cc, C_Bpartner cb, M_Warehouse mw " +
	        "where ci.c_order_id = co.c_order_id and (co.XX_InvoicingStatus ='AP') " +
	        /* Todo documento debe estar completado  CCAPOTE      */
	        //"and ((ci.DOCSTATUS ='CO' and ci.C_DOCTYPETARGET_ID = (select XX_C_DOCTYPE_ID from XX_VSI_keynameinfo)) or (ci.DOCSTATUS = 'DR' and ci.C_DOCTYPETARGET_ID <> (select XX_C_DOCTYPE_ID from XX_VSI_keynameinfo)))" +
	        "and co.xx_PoType = 'POM' " +
	        "and ci.DOCSTATUS ='CO' " +
	        "and ci.ISSOTRX = 'N' " +
	        "and co.ISSOTRX = 'N' " +
            "and ci.XX_SYNCHRONIZED='N' " +
            "and mio.c_order_id(+)=co.c_order_id " +
            "and co.C_Currency_ID(+)=cc.C_Currency_ID " +
            "and cb.C_Bpartner_ID(+)=ci.C_Bpartner_ID " +
            "and mw.M_Warehouse_ID=co.M_Warehouse_ID " +
            "and ci.c_invoice_id is not null " +
            "and co.c_order_id is not null " +
            "and co.DOCUMENTNO is not null " +
            "and ci.DOCUMENTNO is not null " +
            "and ci.C_DOCTYPETARGET_ID is not null " +
            "and ci.TotalLines <> 0" +
	        "order by co.c_order_id, ci.c_invoice_id";
		
		
		try {
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while (rs.next()){

				invoiceID = rs.getInt("C_INVOICE_ID"); 					// ID de la factura. 
				inout = rs.getInt("NUMREC"); 					        // recepcion. 
				orderID = rs.getInt("C_ORDER_ID"); 						// ID de la orden.
				documentoOrigen = rs.getString("ORDERDOCUMENTNO"); 		// Número de Orden de compra.
				invoiceDocumentNo = rs.getString("INVOICEDOCUMENTNO"); 	// Número de factura.
				ref_invoice_id = rs.getInt("REF_INVOICE_ID"); 			// ID de la factura a la que hace referencia en caso de que sea nota de débito o crédito.
				fechaAjuste = rs.getDate("CREATED"); 				    // Fecha de match entre facturación y chequeo.
			    fechaAprobacion = rs.getDate("XX_INVOICEDATE"); 		// Fecha de match entre facturación y chequeo.
			    fechaAPR = rs.getDate("XX_RECEPTIONDATE");
				montoMovimiento = rs.getBigDecimal("TOTALLINES"); 		// Monto del movimiento.
				
				// Forma de pago de la orden de compra.
				if(rs.getString("PAYMENTRULE").equals("B"))
					formaPago = 1;
				else if(rs.getString("PAYMENTRULE").equals("S"))
					formaPago = 2;
				else if(rs.getString("PAYMENTRULE").equals("P"))
					formaPago = 3;
				else if(rs.getString("PAYMENTRULE").equals("T"))
					formaPago = 4;
				else
					formaPago = 0;
				
				condicionPago = rs.getInt("C_PAYMENTTERM_ID"); 			// ID de la condición de pago.
				montoImpuesto = rs.getBigDecimal("XX_TAXAMOUNT"); 		// Monto del Impuesto.
				bPartnerID = rs.getInt("C_BPARTNER_ID"); 				// ID del proveedor de la factura.	
				tipo = rs.getInt("C_DOCTYPETARGET_ID"); 				// Tipo de documento: factura o aviso.
				numeroControl = rs.getString("XX_CONTROLNUMBER"); 		// Número de control de la factura.
				status = rs.getString("DOCSTATUS");						// Estado de la factura.
				codPais = rs.getInt("C_COUNTRY_ID");					// ID del país de la O/C.
							
				/*
				 * Inicialiando las variables si algo de la base de datos esta NULL.
				 * */
				
				if (invoiceID == null){
					invoiceID = 0;
				}				
				if (orderID == null){
					orderID = 0;
				}
				if (formaPago == null){
					formaPago = 0;
				}
				if (ref_invoice_id == null){
					ref_invoice_id = 0;
				}
				if (condicionPago == null){
					condicionPago = 0;
				}
				if (bPartnerID == null){
					bPartnerID = 0;
				}
				if (tipo == null){
					tipo = 0;
				}
				if (codPais == null){
					codPais = 0;
				}
				if (montoMovimiento == null){
					montoMovimiento = Env.ZERO;
				}
				if (montoImpuesto == null){
					montoImpuesto = Env.ZERO;
				}
				
				
				/*
				 * Sentencia que devuelve el ID del AS del proveedor asociado al registro de la factura actual.
				 * */
				
				sqlConsulta = "select value " +
							"from c_bpartner " +
							"where c_bpartner_id = "+bPartnerID+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					codigoProveedor = rsConsulta.getInt("VALUE");
				}
				
				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);
				
				if (codigoProveedor == null){
					codigoProveedor = 0;
				}

				/*
				 * Sentencia que devuelve la descripción de la condicion de pago de la orden de pago actual.
				 * */
 
				descripcionCondicionPago = condicionPago.toString();
				
				/*
				 * Sentencia que devuelve el país de distribución de la O/C.
				 * */
	
				sqlConsulta = "select value from c_country where c_country_id = "+codPais+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
	
				while (rsConsulta.next()){
					pais = rsConsulta.getString(1);
				}
				
				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);
				
				/*
				 * El siguiente código toma la fecha de creación del registro y la divide en cadenas para obtener dia, mes y año.
				 * */
				
				fecha = fechaAprobacion.toString();
				String fechaTokenizer = fecha;
				StringTokenizer tokens = new StringTokenizer(fechaTokenizer, "-");   
				Integer count = tokens.countTokens();
				
				for (int i = 1; i <= count; i++) {
					
					if (i==1){
						añoReg = Integer.parseInt(tokens.nextToken());
						añoMovimiento = añoReg;
						
					}else if (i==2){
						mesReg = Integer.parseInt(tokens.nextToken());
						mesMovimiento = mesReg;
						
					}else if (i==3){
						diaReg = Integer.parseInt(tokens.nextToken());
						diaMovimiento = diaReg;
					}
				}
				
				
				/*
				 * El siguiente código toma la fecha de aprobacion y la divide en cadenas para obtener dia, mes y año.
				 * */
				
				fecha = null;
				fecha = fechaAPR.toString();
				fechaTokenizer = fecha;
				tokens = new StringTokenizer(fechaTokenizer, "-");   
				count = tokens.countTokens();
				
				for (int i = 1; i <= count; i++) {
					
					if (i==1){
						añoAprobacion = Integer.parseInt(tokens.nextToken());
						
					}else if (i==2){
						mesAprobacion = Integer.parseInt(tokens.nextToken());
						
					}else if (i==3){
						diaAprobacion = Integer.parseInt(tokens.nextToken());
					}
				}
				
			
				/*
				 * Sentencia que devuelve la fecha de vencimiento a partir de la fecha de aprobación y los dás de financiamiento.
				 * */
	
				sqlConsulta = "select xx_daysfunding from c_paymentterm where c_paymentterm_id = " +
							"(select c_paymentterm_id from c_invoice where c_invoice_id ="+invoiceID+")";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
	
				while (rsConsulta.next()){
					diasFinanciamiento = rsConsulta.getInt(1);
				}

				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);
				
				if (diasFinanciamiento == null){
					
					diasFinanciamiento = 0;
				}
				
				fechaVencimiento = calcularFechaVencimiento(fechaAprobacion,diasFinanciamiento); /* Se le agrega a la fecha de aprobación los días de financiamiento
																									para obtener la fecha de vencimiento */				
				/*
				 * El siguiente código toma la fecha de vencimiento del registro y la divide en cadenas para obtener dia, mes y año.
				 * */
				
				fechaCadena = fechaVencimiento.toString();
				String fechaToken = fechaCadena;
				StringTokenizer token2 = new StringTokenizer(fechaToken, "-");   
				Integer count2 = token2.countTokens();
				
				for (int i = 1; i <= count2; i++) {
					if (i==1){
						añoVencimiento = Integer.parseInt(token2.nextToken());
												
					}else if (i==2){
						mesVencimiento = Integer.parseInt(token2.nextToken());
												
					}else if (i==3){
						diaVencimiento = Integer.parseInt(token2.nextToken());
							
					}
				}
				
				/*
				 * Se obtienen las variable de entorno "Factura", debito y credito.
				 * */
				
				factura = Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID");
				credito = Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID");
				debito = Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEDEBIT_ID");
				
								
				/*
				* Se asigna el código de AS para el tipo de movimiento dependiendo de si es una factura o aviso.
				* Se asigna el número de control para las facturas.
				* Se asigna el nuúmero de comprobante para caso TIMOPA 01.
				* */
				
				if (tipo.compareTo(factura)==0){ // Si el registro actual es una factura.
					
					/*
					 * Sentencia que devuelve la cantidad de facturas que tiene asociada la orden compra actual para realizar el consecutivo.
					 * */
		
					sqlConsulta = "select count(ci.c_invoice_id)" +
								"from c_invoice ci, c_order co " +
								"where ci.c_order_id = co.c_order_id " +
								"and co.XX_InvoicingStatus ='AP' and ci.DOCSTATUS ='CO' and ci.C_DOCTYPETARGET_ID = "+factura+" " +
								"and ci.ISSOTRX = 'N' " +
								"and ci.c_order_id = "+orderID+"";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
		
					while (rsConsulta.next()){
						consecutivoFactura = rsConsulta.getInt(1);
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
					
					tipoMovimiento = "01";
					numeroAviso = "";
					documentoAfectado = invoiceDocumentNo;	
					
					/*
					 * Sentencia que devuelve el número de comprobante para de la factura.
					 * */
		
					sqlConsulta = "select xx_withholding from xx_vcn_purchasesbook where c_invoice_id ="+invoiceID+" and XX_DOCUMENTNO_ID is null";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
		
					while (rsConsulta.next()){
						
						numeroComprobante = rsConsulta.getInt("XX_WITHHOLDING");
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
					
					if (numeroComprobante == null){
						numeroComprobante = 0; 
					}

					consecutivo ++;
					
					montoTruncate = montoMovimiento.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
					impuestoTruncate = montoImpuesto.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
					
					// Se Inserta en la tabla el registro completo de la factura actual
					
					insertar(inout,orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
							consecutivo, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
							diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento,
							mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda,
							factorCampo, montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, 
							diaAnticipo, mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso);
										
			/*		System.out.println("Fact "+invoiceID+" "+orderID+" "+tipoProveedor+" "+documentoOrigen+" "+documentoAfectado+" consec: "+consecutivo+" - "+tipoMovimiento
							+" "+diaReg+" "+mesReg+" "+añoReg+" "+diaAprobacion+" "+mesAprobacion+" "+añoAprobacion+" "+diaMovimiento+" "+mesMovimiento
							+" "+añoMovimiento+" "+diaAnulacion+" "+mesAnulacion+" "+añoAnulacion+" "+diaVencimiento+" "+mesVencimiento+" "+añoVencimiento
							+" "+formaPago+" "+descripcionCondicionPago+" "+descripcion+" "+codigoMoneda+" "+factorCampo+" "+montoMovimiento+" "+montoImpuesto
							+" "+statusCuenta+" "+statusGiro+" "+statusGrabacion+" "+ordenPago+" "+diaAnticipo+" "+mesAnticipo+" "+añoAnticipo+" "+pais
							+" "+numeroControl+" "+numeroComprobante+" "+numeroAviso);
			*/		
					if (consecutivo == consecutivoFactura ){ // Reinicio el contador del consecutivo para las próximas facturas
						
						consecutivo = 0;						
					}
					CPPD01.save();
					
					// Importación de Data de Factura a la Tabla E_XX_VMR_NRAM02 WDIAZ Cambio porque cuando no esta la facturación y se 
					//exporta el Chequeo no se pasan a la tabla E de compiere, posteriormente no se refleja en el AS400
					try{
		
						receipt_M02 = new X_E_XX_VMR_NRAM02(getCtx(), 0, get_TrxName());
						
						receipt_M02.setNUMREC(String.valueOf(inout));
						receipt_M02.setNUMORD(documentoOrigen);
						//receipt_M02.setGUIAEM(null);
						receipt_M02.setCODMON(rs.getString("CODMON"));
						receipt_M02.setNUMFAC(invoiceDocumentNo);
						receipt_M02.setFACCAM(rs.getBigDecimal("FACCAM"));
						receipt_M02.setMONFAC(montoMovimiento);
						
						fechaFactura=fechaAPR.toString();

						if (fechaFactura!=null){
							tokens = new StringTokenizer(fechaFactura,"-"); 
							ano=tokens.nextToken();
							receipt_M02.setXX_ANOREG(Integer.parseInt(ano));
							mes=tokens.nextToken();
							receipt_M02.setMESREG(Integer.parseInt(mes));
							dia=tokens.nextToken();
							receipt_M02.setDIAREG(Integer.parseInt(dia));  
						}
						else
						{
							receipt_M02.setXX_ANOREG(0);
							receipt_M02.setMESREG(0);  
							receipt_M02.setDIAREG(0); 
						}
						receipt_M02.setTIEMPE(null);
						receipt_M02.setCOEMPE(rs.getString("COEMPE"));
						receipt_M02.setVolume(null);
						receipt_M02.setBULTO(null);
						receipt_M02.setBULREC(rs.getInt("BULREC")); //TODO FIX
						receipt_M02.setMONIMP(montoImpuesto);
						receipt_M02.setTIEFAC(rs.getString("TIEFAC"));
						receipt_M02.setSTAANU(null);
						receipt_M02.save();
				
						commit();
				//	}
					
					}catch (SQLException e){
						e.printStackTrace();
						System.out.println("Error al sincronizar facturas " + e);

					}catch (Exception e) {
						e.printStackTrace();
						System.out.println("Error al sincronizar facturas " + e);	
						
					}
					//
						
				}
				else if((tipo.compareTo(debito)== 0) || (tipo.compareTo(credito)== 0)){ // Si el registro actual es un aviso.
						
						/*
						 * Sentencia que devuelve el número de la factura a la que hace referencia el aviso actual.
						 * */
			
						MInvoice documentoAfectadoConsulta = new MInvoice(getCtx(), ref_invoice_id, null); 
						documentoAfectado = documentoAfectadoConsulta.getDocumentNo();
						
						/*
						 * Asigno el tipo de movimiento si es débito o crédito
						 * */
						
						if ((montoMovimiento.compareTo(new BigDecimal(0))) == -1){ // El monto es menor a 0, aviso de crédito.
							
							
							/*
							 * Sentencia que devuelve la cantidad de avisos de crédito que tiene asociada la orden compra actual para realizar el consecutivo.
							 * */
							
							sqlConsulta = "select count(ci.c_invoice_id) " +
									"from c_invoice ci, c_order co " +
									"where ci.c_order_id = co.c_order_id " +
									"and co.XX_InvoicingStatus ='AP' and ci.DOCSTATUS = 'DR' and ci.C_DOCTYPETARGET_ID <> "+factura+" " +
									"and ci.ISSOTRX = 'N' " +
									"and ci.TOTALLINES < 0" +
									"and ci.c_order_id = "+orderID+"";
							pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
							rsConsulta = pstmtConsulta.executeQuery();
				
							while (rsConsulta.next()){
								consecutivoAvisoCre = rsConsulta.getInt(1);
							}

							DB.closeResultSet(rsConsulta);
							DB.closeStatement(pstmtConsulta);
							
							//TODO
							tipoMovimiento = "55";
							numeroComprobante = 0;
							numeroAviso = invoiceDocumentNo;
							consecutivo2 ++;
							
							montoMovimiento = montoMovimiento.multiply(new BigDecimal(-1));
							montoImpuesto = montoImpuesto.multiply(new BigDecimal(-1));
							
							montoTruncate = montoMovimiento.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
							impuestoTruncate = montoImpuesto.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
							
							// Se Inserta en la tabla el registro completo del aviso de crédito actual
							
							
							fecha = fechaAjuste.toString();
							String fechaTokenizer1 = fecha;
							StringTokenizer tokens1 = new StringTokenizer(fechaTokenizer1, "-");   
							Integer count1 = tokens1.countTokens();
							
							for (int i = 1; i <= count1; i++) {
								
								if (i==1){
									añoReg = Integer.parseInt(tokens1.nextToken());
									añoMovimiento = añoReg;
									
								}else if (i==2){
									mesReg = Integer.parseInt(tokens1.nextToken());
									mesMovimiento = mesReg;
									
								}else if (i==3){
									diaReg = Integer.parseInt(tokens1.nextToken());
									diaMovimiento = diaReg;
								}
							}
							
							insertar(inout,orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
									consecutivo2, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
									diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento,
									mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda,
									factorCampo, montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, 
									diaAnticipo, mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso);
							
		//					actualizarStatusAviso(invoiceID);
						
							// Para que se pueda saltar el siguiente IF.
							
							montoMovimiento = montoMovimiento.multiply(new BigDecimal(-1));
							montoImpuesto = montoImpuesto.multiply(new BigDecimal(-1));
							
		/*					System.out.println("Aviso "+invoiceID+" "+orderID+" "+tipoProveedor+" "+documentoOrigen+" "+documentoAfectado+" "+consecutivo2+" "+tipoMovimiento
							+" "+diaReg+" "+mesReg+" "+añoReg+" "+diaAprobacion+" "+mesAprobacion+" "+añoAprobacion+" "+diaMovimiento+" "+mesMovimiento
							+" "+añoMovimiento+" "+diaAnulacion+" "+mesAnulacion+" "+añoAnulacion+" "+diaVencimiento+" "+mesVencimiento+" "+añoVencimiento
							+" "+formaPago+" "+descripcionCondicionPago+" "+descripcion+" "+codigoMoneda+" "+factorCampo+" "+montoMovimiento+" "+montoImpuesto
							+" "+statusCuenta+" "+statusGiro+" "+statusGrabacion+" "+ordenPago+" "+diaAnticipo+" "+mesAnticipo+" "+añoAnticipo+" "+pais
							+" "+numeroControl+" "+numeroComprobante+" "+numeroAviso);
		*/			
						
							if (consecutivo2 == consecutivoAvisoCre ){ // Reinicio el contador del consecutivo para los próximos avisos de 
								
								consecutivo2 = 0;
							}
							CPPD01.save();
							
						}if ((montoMovimiento.compareTo(new BigDecimal(0))) == 1){ // El monto es mayor a 0, aviso de débito.
							
							/*
							 * Sentencia que devuelve la cantidad de avisos de débito que tiene asociada la orden compra actual para realizar el consecutivo.
							 * */
							
							sqlConsulta = "select count(ci.c_invoice_id) " +
									"from c_invoice ci, c_order co " +
									"where ci.c_order_id = co.c_order_id " +
									"and co.XX_InvoicingStatus ='AP' and ci.DOCSTATUS = 'DR' and ci.C_DOCTYPETARGET_ID <> "+factura+" " +
									"and ci.ISSOTRX = 'N' " +
									"and ci.TOTALLINES > 0" +
									"and ci.c_order_id = "+orderID+"";
							pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
							rsConsulta = pstmtConsulta.executeQuery();
				
							while (rsConsulta.next()){
								
								consecutivoAvisoDeb = rsConsulta.getInt(1);
							}

							DB.closeResultSet(rsConsulta);
							DB.closeStatement(pstmtConsulta);
							
							tipoMovimiento = "04";	
							numeroComprobante = 0;
							numeroAviso = invoiceDocumentNo;
							
							consecutivo3 ++;
							
							montoTruncate = montoMovimiento.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
							impuestoTruncate = montoImpuesto.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
							
							// Se Inserta en la tabla el registro completo del aviso de débito actual
							
							fecha = fechaAjuste.toString();
							String fechaTokenizer2 = fecha;
							StringTokenizer tokens2 = new StringTokenizer(fechaTokenizer2, "-");   
							Integer count3 = tokens2.countTokens();
							
							for (int i = 1; i <= count3; i++) {
								
								if (i==1){
									añoReg = Integer.parseInt(tokens2.nextToken());
									añoMovimiento = añoReg;
									
								}else if (i==2){
									mesReg = Integer.parseInt(tokens2.nextToken());
									mesMovimiento = mesReg;
									
								}else if (i==3){
									diaReg = Integer.parseInt(tokens2.nextToken());
									diaMovimiento = diaReg;
								}
							}
							
							insertar(inout,orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
									consecutivo3, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
									diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento,
									mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda,
									factorCampo, montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, 
									diaAnticipo, mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso);
			
				/*			System.out.println("Aviso "+invoiceID+" "+orderID+" "+tipoProveedor+" "+documentoOrigen+" "+documentoAfectado+" "+consecutivo3+" "+tipoMovimiento
							+" "+diaReg+" "+mesReg+" "+añoReg+" "+diaAprobacion+" "+mesAprobacion+" "+añoAprobacion+" "+diaMovimiento+" "+mesMovimiento
							+" "+añoMovimiento+" "+diaAnulacion+" "+mesAnulacion+" "+añoAnulacion+" "+diaVencimiento+" "+mesVencimiento+" "+añoVencimiento
							+" "+formaPago+" "+descripcionCondicionPago+" "+descripcion+" "+codigoMoneda+" "+factorCampo+" "+montoMovimiento+" "+montoImpuesto
							+" "+statusCuenta+" "+statusGiro+" "+statusGrabacion+" "+ordenPago+" "+diaAnticipo+" "+mesAnticipo+" "+añoAnticipo+" "+pais
							+" "+numeroControl+" "+numeroComprobante+" "+numeroAviso);
				*/	
							
							if (consecutivo3 == consecutivoAvisoDeb){ // Reinicio el contador del consecutivo para los próximos avisos
								
								consecutivo3 = 0;
							}
							CPPD01.save();
						}
					}		
	
				
				// Inicializo todas las variables para el proximo registro.
				
				orderID = 0;
				inout=0;
				invoiceID = 0;
				consecutivoFactura = 0;
				consecutivoAvisoDeb = 0;
				consecutivoAvisoCre = 0;
				codigoProveedor = null;
				documentoOrigen = null;
				documentoAfectado = null;
				tipoMovimiento = null;
				diaReg = 0;
				mesReg = 0;
				añoReg = 0;
				diaMovimiento = 0;
				mesMovimiento = 0;
				añoMovimiento = 0;
				diaVencimiento = 0;
				mesVencimiento = 0;
				añoVencimiento = 0;
				formaPago = 0;
				descripcionCondicionPago = null;
				montoMovimiento = Env.ZERO;
				montoImpuesto = Env.ZERO;
				pais = null;
				numeroControl = null;
				numeroComprobante = null;
				numeroAviso = null;
					
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "Fallo en Sincronización";
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		System.out.println("Exportando la NRAM02 al AS");
		exportE_XX_VMR_NRAM02DB2();
		System.out.println("ya exportó NRAM02");
		
		System.out.println("Desde WithHolding");
		exportWithHolding();
		
		System.out.println("Desde las Notas");
		exportNotes();

		System.out.println("Exportando al AS");
		exportE_XX_VMR_CPPD01DB2();
		
		System.out.println("FIN");
		
		
		return "Sincronización Completa";
	}


	@Override
	protected void prepare() {
		
	}
	
	/*
	 * Función que suma la cantidad de dias correspodientes a la condición de pago de la O/C a la fecha.
	 * 
	 *  @param fch es la fecha.
	 *  @param dias son los dias de financiamiento de esa O/C.
	 *  
	 *  @return cal fecha calculada.
	 */
	
	private java.sql.Date calcularFechaVencimiento(java.sql.Date fch, int dias){ 
	   
		Calendar cal = new GregorianCalendar(); 
	     cal.setTimeInMillis(fch.getTime()); 
	     cal.add(Calendar. DATE, dias); 
	     return new Date(cal.getTimeInMillis()); 
	} 
	
	/*
	 * Procedimiento en el cuál se realiza el set de los datos para la tabla E_XX_VCN_CPPD01.
	 * 
	 *  @param orderID es el ID de la orden de compra asociada a los registros de C_INVOICE.
	 *  @param invoiceID es el ID de la factura en la tabla C_INVOICE.
	 *  @param codigoProveedor es el ID del proveedor en el AS.
	 *  @param tipoProveedor string con el texto "PRO".
	 *  @param documentoOrigen número del documento de la orden de compra.
	 *  @param documentoAfectado número del documento de la factura.
	 *  @param consecutivo correlativo según el número de facturas que tenga asociada la O/C.
	 *  @param tipoMovimiento código del AS utilizado para identificar las facturas de las notas de débito, crédito y retenciones.
	 *  @param diaReg Día de generación de la aprobación, es decir día del match entre facturación y chequeo.
	 *  @param mesReg Mes de generación de la aprobación, es decir mes del match entre facturación y chequeo.
	 *  @param añoReg Año de generación de la aprobación, es decir Año del match entre facturación y chequeo.
	 *  @param diaAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param mesAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param añoAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param diaMovimiento Día de generación de la aprobación, es decir día del match entre facturación y chequeo.
	 *  @param mesMovimiento Mes de generación de la aprobación, es decir mes del match entre facturación y chequeo.
	 *  @param añoMovimiento Año de generación de la aprobación, es decir Año del match entre facturación y chequeo.
	 *  @param diaAnulacion campo que se llena siempre con el valor cero(0).
	 *  @param mesAnulacion campo que se llena siempre con el valor cero(0).
	 *  @param añoAnulacion campo que se llena siempre con el valor cero(0).	
	 *  @param diaVencimiento día de vecimiento en base a la condición de pago. 
	 *  @param mesVencimiento mes de vecimiento en base a la condición de pago. 
	 *  @param añoVencimiento año de vecimiento en base a la condición de pago. 
	 *  @param formaPago forma de pago de la O/C.
	 *  @param descripcionCondicionPago condicion de pago de la O/C.
	 *  @param descripcion string con el texto "PAGO A PROVEEDORES".
	 *  @param codigoMoneda campo que siempre lleva el ID = 1.
	 *  @param factorCampo campo que siempre lleva 0,000.
	 *  @param montoMovimiento Total de la Factura sin IVA.
	 *  @param montoImpuesto Total del Impuesto.
	 *  @param statusCuenta campo que siempre lleva el valor = 5.
	 *  @param statusGiro campo que siempre va en blanco.
	 *  @param statusGrabacion campo que siempre lleva el valor = 1.
	 *  @param ordenPago campo que siempre lleva el valor = 0.
	 *  @param diaAnticipo campo que se llena siempre con el valor cero(0).
	 *  @param mesAnticipo campo que se llena siempre con el valor cero(0).
	 *  @param añoAnticipo campo que se llena siempre con el valor cero(0).		 
	 *  @param paisProveedor es el país de distribución de la O/C.
	 *  @param numeroControl número de control de la factura en el libro de compras.
	 *  @param numeroComprobante número de comprobante de retención de la factura.
	 *  @param númeroAviso número de documento del aviso de débito o crédito.
	 *  
	 */
	
	public void insertar (Integer inout, int orderID, int invoiceID, int codigoProveedor, String tipoProveedor, String documentoOrigen,
			String documentoAfectado, int consecutivo, String tipoMovimiento, int diaReg, int mesReg, int añoReg, int diaAprobacion,
			int mesAprobacion, int añoAprobacion, int diaMovimiento, int mesMovimiento, int añoMovimiento, int diaAnulacion,
			int mesAnulacion, int añoAnulacion, int diaVencimiento, int mesVencimiento, int añoVencimiento, int formaPago,
			String descripcionCondicionPago, String descripcion, int codigoMoneda, BigDecimal factorCampo, BigDecimal montoMovimiento,
			BigDecimal montoImpuesto, int statusCuenta, String statusGiro, int statusGrabacion, int ordenPago, int diaAnticipo,
			int mesAnticipo, int añoAnticipo, String paisProveedor, String numeroControl, int numeroComprobante, String numeroAviso){
		
		CPPD01  = new X_E_XX_VCN_CPPD01(getCtx(), 0, get_TrxName());
		
        try{
        	
        	CPPD01.setNUMREC(inout);
			CPPD01.setC_Order_ID(orderID);
			CPPD01.setC_Invoice_ID(invoiceID);
			CPPD01.setC_BPartner_ID(codigoProveedor);
			CPPD01.setXX_VendorClass(tipoProveedor);
			CPPD01.setXX_DocumentNoOrder(inout.toString());
			CPPD01.setXX_DocumentNoInvoice(documentoAfectado);
			CPPD01.setXX_Consecutive(consecutivo);
			CPPD01.setXX_TypeOfMovement(tipoMovimiento);
			CPPD01.setXX_RegistrationDay(diaReg);
			CPPD01.setXX_RegistrationMonth(mesReg);
			CPPD01.setXX_RegistrationYear(añoReg);
			CPPD01.setXX_ApprovalDay(diaAprobacion);
			CPPD01.setXX_ApprovalMonth(mesAprobacion);
			CPPD01.setXX_ApprovalYear(añoAprobacion);
			CPPD01.setXX_MovementDay(diaMovimiento);
			CPPD01.setXX_MovementMonth(mesMovimiento);
			CPPD01.setXX_MovementYear(añoMovimiento);
			CPPD01.setXX_CancellationDay(diaAnulacion);
			CPPD01.setXX_CancellationMonth(mesAnulacion);
			CPPD01.setXX_CancellationYear(añoAnulacion);
			CPPD01.setXX_ExpirationDay(diaVencimiento);
			CPPD01.setXX_ExpirationMonth(mesVencimiento);
			CPPD01.setXX_ExpirationYear(añoVencimiento);
			CPPD01.setPaymentRule(formaPago);
			CPPD01.setPaymentTerm(descripcionCondicionPago);
			CPPD01.setXX_OrderDescription(descripcion);
			CPPD01.setC_Currency_ID(codigoMoneda);
			CPPD01.setXX_FieldFactor(factorCampo);
			CPPD01.setTotalLines(montoMovimiento);
			CPPD01.setXX_TaxAmount(montoImpuesto);
			CPPD01.setXX_DebtBalanceState(statusCuenta);
			CPPD01.setXX_EmittedDraftStatus(statusGiro);
			CPPD01.setXX_RecordingStatus(statusGrabacion);
			CPPD01.setXX_PaymentOrder(ordenPago);
			CPPD01.setXX_DayOfPaymentAdvance(diaAnticipo);
			CPPD01.setXX_MonthOfPaymentAdvance(mesAnticipo);
			CPPD01.setXX_YearOfPaymentAdvance(añoAnticipo);
			CPPD01.setCountryName(paisProveedor);
			CPPD01.setXX_ControlNumber(numeroControl);
			CPPD01.setXX_Withholding(numeroComprobante);
			CPPD01.setXX_ControlNumberDebCre(numeroAviso);
			CPPD01.setAD_Org_ID(0);
			
			actualizarInvoice (invoiceID);
			
        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
        }
	}

	/*
	 * Procedimiento que actualiza en la tabla C_INVOICE el campo XX_SYNCHRONIZED a 'Y' para indicar que el registro ya
	 * fue exportado.
	 * 
	 *  @param invoiceID es el ID de la factura en la tabla C_INVOICE.
	 */
	
	public void actualizarInvoice(int invoiceID)
	{
		String sqlUpdate = null;
		PreparedStatement pstmtUpdate = null;
		
        try{
		    sqlUpdate = "UPDATE C_INVOICE SET XX_SYNCHRONIZED='Y' WHERE C_INVOICE_ID="+invoiceID;
		    DB.executeUpdate(get_TrxName(),sqlUpdate);
        }
        catch (Exception e) {      	
        	e.printStackTrace();   
		}
	}
	
	
	private void exportWithHolding(){
		
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sqlConsulta = null;
		PreparedStatement pstmtConsulta = null;
		ResultSet rsConsulta = null;
	
		Date fechaAprobacion = null;
		Date fechaVencimiento;
		Date fecha1 = null;
				 
		//BigDecimal montoMovimiento = new BigDecimal(0.000);
		BigDecimal montoImpuesto = new BigDecimal(0.000);
		BigDecimal factorCampo = new BigDecimal(0.000);
		BigDecimal montoTruncate = new BigDecimal(0.000);
		BigDecimal impuestoTruncate = new BigDecimal(0.000);
		
		Integer codigoProveedor = 0, consecutivo = 0, contador = 0;
		Integer diaReg = null,	mesReg = null,	añoReg = null;
		Integer diaMovimiento = null, mesMovimiento = null, añoMovimiento = null, diaVencimiento = null, mesVencimiento = null, añoVencimiento = null;
		Integer condicionPago = null, numeroComprobante = null, diasFinanciamiento = 0;
		Integer bPartnerID, orderID, invoiceID, formaPago = null;
		
		Integer diaAprobacion = 0, mesAprobacion = 0, añoAprobacion = 0; 
		Integer mesAnulacion = 0,	añoAnulacion = 0, diaAnulacion = 0;
		Integer codigoMoneda = 1, documentNoID;
		Integer mesAnticipo = 0, añoAnticipo = 0, diaAnticipo = 0, ordenPago = 0;
		Integer statusGrabacion = 1, statusCuenta = 5, decimalPlaces = 2, inout=0;
	
		String statusGiro = "", descripcion = "PAGO A PROVEEDORES", tipoProveedor = "PRO", descripcionCondicionPago;
		String numeroAviso = "", fechaCadena, numeroControl, fecha, pais = null;
		String documentoAfectado = null, tipoMovimiento = null, documentoOrigen = null;
		
		sql = "select mio.DOCUMENTNO NUMREC, pb.C_bpartner_ID, pb.C_Order_ID, pb.C_Invoice_ID, pb.xx_documentno_ID, " +
				"pb.XX_ControlNumber, pb.xx_withholdingtax, pb.xx_withholding, pb.Created " +
				"from xx_vcn_purchasesbook pb, m_inout mio " +
				"where pb.xx_withholdingtax <> 0 " +
				"and pb.C_Order_ID is not null " +   // Por que algunos de éstos datos son de prueba entonces éstos campos 
				"and pb.C_Invoice_ID is not null " + // no se pueden recibir nulos para consultas posteriores.
                "and pb.XX_SYNCHRONIZEDWITHHOLDING ='N' " +
                "and pb.XX_ControlNumber is not null " +
                "and pb.c_order_id(+)=mio.c_order_id " +
                "and pb.xx_withholding <> 0 " +
                "and mio.xx_potype = 'POM'" +   // Para que no se transfieran las retenciones de bienes 
                "and pb.xx_withholdingtax is not null " +
				"order by pb.C_Order_ID";  
		
		pstmt = DB.prepareStatement(sql, null);
	
		try {
			
			rs = pstmt.executeQuery();
			tipoMovimiento = "";									// Tipo de movimiento.
			numeroAviso = "";										// Número del aviso.
			
			while (rs.next()){
				
				inout = rs.getInt("NUMREC"); 
				documentNoID = rs.getInt("C_INVOICE_ID"); 				// ID de la factura afectada.
				orderID = rs.getInt("C_ORDER_ID");						// ID de la orden.
				montoImpuesto = rs.getBigDecimal("XX_WITHHOLDINGTAX");	// Monto de la retención.
				bPartnerID = rs.getInt("C_BPARTNER_ID"); 				// ID del proveedor de la factura.
				numeroControl = rs.getString("XX_CONTROLNUMBER"); 		// Número de control de la factura.
				invoiceID = rs.getInt("xx_documentno_ID"); 				// ID de la Nota de Debito o Credito
				numeroComprobante = rs.getInt("xx_withholding");		// Número de Comprobante de la factura.
				fecha1 = rs.getDate("created");							// Fecha de Creación del registro en el LDC
								
				/*
				 * Inicialiando las variables si algo de la base de datos esta NULL.
				 * */
				
				if (montoImpuesto == null){
					
					montoImpuesto = Env.ZERO;
				}
				if (documentNoID == null){
					
					documentNoID = 0;
				}
				if (bPartnerID == null){
					
					bPartnerID = 0;
				}
				if (numeroComprobante == null){
					
					numeroComprobante = 0;
				}
				if (invoiceID == null){
					
					invoiceID = 0;
				}
				
				/*
				 * Sentencia que devuelve la cantidad de retenciones que tiene asociada la orden de compra actual para generar
				 * el consecutivo.
				 * */

				sqlConsulta = "select count(pb.C_invoice_ID) " +
							"from xx_vcn_purchasesbook pb " +
							"where pb.xx_withholdingtax <> 0 " +
							"and pb.C_Order_ID is not null " +
							"and pb.C_Invoice_ID is not null " +
							"and pb.C_Order_ID ="+orderID+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
				
				while (rsConsulta.next()){
					
					contador = rsConsulta.getInt(1);			
				}
				
				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);
				
				/*
				 * Sentencia que devuelve el número de documento de la factura a la que hace referencia la retención.
				 * */

				sqlConsulta = "SELECT DOCUMENTNO FROM C_INVOICE WHERE C_INVOICE_ID = "+documentNoID+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
				
				while (rsConsulta.next()){
					
					documentoAfectado = rsConsulta.getString("DOCUMENTNO");	
				}

				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);
				
				
				
				
				/*
				 * Sentencia que devuelve los datos relacionados a la orden de pago correspondiente al registro actual.
				 * */
	
				sqlConsulta = "select co.CREATED, (Select a.value from C_PaymentTerm a where a.C_PaymentTerm_ID=co.C_PaymentTerm_ID) C_PAYMENTTERM_ID, co.XX_InvoicingStatus, co.paymentRule, co.DOCUMENTNO, " +
								"co.XX_INVOICEDATE " +
								"from c_order co " +
								"where co.c_order_id = "+orderID+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
	
				while (rsConsulta.next()){
					
					fechaAprobacion = rsConsulta.getDate("XX_INVOICEDATE");			// Fecha de match entre facturación y chequeo.
					//fechaAprobacion = rsConsulta.getDate("CREATED");		// Fecha de match entre facturación y chequeo.
					documentoOrigen = rsConsulta.getString("DOCUMENTNO");	// Número de Orden de compra.
					condicionPago = rsConsulta.getInt("C_PAYMENTTERM_ID");	// ID de la condición de pago.
					
					// Forma de pago de la orden de compra.
					if(rsConsulta.getString("PAYMENTRULE").equals("B"))
						formaPago = 1;
					else if(rsConsulta.getString("PAYMENTRULE").equals("S"))
						formaPago = 2;
					else if(rsConsulta.getString("PAYMENTRULE").equals("P"))
						formaPago = 3;
					else if(rsConsulta.getString("PAYMENTRULE").equals("T"))
						formaPago = 4;
					else
						formaPago = 0;			

				}

				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);

				if (condicionPago == null){
					
					condicionPago = 0;
				}		
				if (formaPago == null){
					
					formaPago = 0;
				}
				
				/*
				 * Sentencia que devuelve la fecha de vencimiento a partir de la fecha de aprobación y los dás de financiamiento.
				 * */
	
				sqlConsulta = "select xx_daysfunding from c_paymentterm where c_paymentterm_id = "+condicionPago+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
	
				while (rsConsulta.next()){
					diasFinanciamiento = rsConsulta.getInt(1);
				}

				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);
				
				if (diasFinanciamiento == null){
					
					diasFinanciamiento = 0;
				}
				
				/* 
				 * Suma a la fecha de aprobación los días de financiamiento
				para obtener la fecha de vencimiento */
				
				fechaVencimiento = calcularFechaVencimiento(fechaAprobacion,diasFinanciamiento);				
								
				/*
				 * El siguiente código toma la fecha de vencimiento del registro y la divide en cadenas para obtener dia, mes y año.
				 * */
				
				fechaCadena = fechaVencimiento.toString();
				String fechaToken = fechaCadena;
				StringTokenizer token2 = new StringTokenizer(fechaToken, "-");   
				Integer count2 = token2.countTokens();
				
				for (int i = 1; i <= count2; i++) {
					
					if (i == 1){
						añoVencimiento = Integer.parseInt(token2.nextToken());
												
					}else if (i == 2){
						mesVencimiento = Integer.parseInt(token2.nextToken());
												
					}else if (i == 3){
						diaVencimiento = Integer.parseInt(token2.nextToken());
					
					}
				}
				
								/*
				 * Asignación de Tipo de Movimiento
				 * */
				
				
				if ((montoImpuesto.compareTo(new BigDecimal(0))) == -1){  // El monto es menor a 0.
					
					tipoMovimiento = "23";
				}
				if ((montoImpuesto.compareTo(new BigDecimal(0))) == 1){  // El monto es mayor a 0.
					
					if(invoiceID == 0){
						
						tipoMovimiento = "70";
					}
					if(invoiceID != 0){
						
						tipoMovimiento = "71";
					}
				}
							
				/*
				 * El siguiente código toma la fecha de creación del registro y la divide en cadenas para obtener dia, mes y año.
				 * */
				
				fecha = fechaAprobacion.toString();	
				
				String fechaTokenizer = fecha;
				StringTokenizer tokens = new StringTokenizer(fechaTokenizer, "-");   
				Integer count = tokens.countTokens();
				
				for (int i = 1; i <= count; i++) {
					
					if (i == 1){
						añoAprobacion = Integer.parseInt(tokens.nextToken()); 
						
					}else if (i == 2){
						mesAprobacion = Integer.parseInt(tokens.nextToken());
						
					}else if (i == 3){
						diaAprobacion = Integer.parseInt(tokens.nextToken());

					}
				}
				
				numeroAviso = "";
				
				if (!tipoMovimiento.equals("70")) {
					
					fecha = fecha1.toString();
					
					/*
					 * Sentencia que devuelve el número de documento de la factura actual.
					 * */

					sqlConsulta = "SELECT DOCUMENTNO FROM C_INVOICE WHERE C_INVOICE_ID = "+invoiceID+"";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
					
					while (rsConsulta.next()){
						
						numeroAviso = rsConsulta.getString("DOCUMENTNO");	
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
										
				}
								
				String fechaTokenizer3 = fecha;
				StringTokenizer tokens3 = new StringTokenizer(fechaTokenizer3, "-");   
				Integer count3 = tokens3.countTokens();
				
				for (int i = 1; i <= count3; i++) {
					
					if (i == 1){
						añoReg = Integer.parseInt(tokens3.nextToken());
						añoMovimiento = añoReg;
						
					}else if (i == 2){
						mesReg = Integer.parseInt(tokens3.nextToken());
						mesMovimiento = mesReg;
						
					}else if (i == 3){
						diaReg = Integer.parseInt(tokens3.nextToken());
						diaMovimiento = diaReg;
					}
				}

				/*
				 * Sentencia que devuelve la descripción de la condicion de pago de la orden de pago actual.
				 * */

				descripcionCondicionPago = condicionPago.toString();
				
				/*
				 * Sentencia que devuelve el ID del AS del proveedor asociado al registro de la factura actual.
				 * */
				
				sqlConsulta = "select value " +
							"from c_bpartner " +
							"where c_bpartner_id = "+bPartnerID+"";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
			
				while (rsConsulta.next()){
					codigoProveedor = rsConsulta.getInt("VALUE");
				}

				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);
				
				if (codigoProveedor == null){
					codigoProveedor = 0;
				}
				
				/*
				 * Sentencia que devuelve el país de distribución de la O/C.
				 * */
	
				sqlConsulta = "select value from c_country where c_country_id = (select c_country_id from c_order where c_order_id="+orderID+")";
				pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
				rsConsulta = pstmtConsulta.executeQuery();
	
				while (rsConsulta.next()){
					pais = rsConsulta.getString(1);
				}

				DB.closeResultSet(rsConsulta);
				DB.closeStatement(pstmtConsulta);

				consecutivo ++;
				
		/*		System.out.println("Order "+orderID+"Invoice "+invoiceID+"CodPro "+codigoProveedor+" "+tipoProveedor+"DocOri "+documentoOrigen+"DocAfec "+documentoAfectado+"Consec "+ 
						consecutivo+"Timopa "+tipoMovimiento+"FecReg "+diaReg+" "+mesReg+" "+añoReg+"DiaAp "+diaAprobacion+" "+mesAprobacion+" "+añoAprobacion+"FecMov "+
						diaMovimiento+" "+mesMovimiento+" "+añoMovimiento+"FecAn "+diaAnulacion+" "+mesAnulacion+" "+añoAnulacion+"FecVen "+diaVencimiento+" "+
						mesVencimiento+" "+añoVencimiento+"ForPag "+formaPago+"DescPago "+descripcionCondicionPago+"Desc "+descripcion+"CodMon "+codigoMoneda+"FacCampo "+factorCampo+"MonMov "+
						montoMovimiento+"MonImp "+montoImpuesto+"StaC "+statusCuenta+"StaG "+statusGiro+"StaGr "+statusGrabacion+"OrdenP "+ordenPago+"FecAntc "+diaAnticipo+" "+
						mesAnticipo+" "+añoAnticipo+"Pais "+paisProveedor+"NCtl "+numeroControl+"nroComp "+numeroComprobante+"nroAviso "+numeroAviso);
		*/		
				
				if ((montoImpuesto.compareTo(new BigDecimal(0))) == -1){  // El monto es menor a 0.
					
					montoImpuesto = montoImpuesto.multiply(new BigDecimal(-1));

				}

				impuestoTruncate = montoImpuesto.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
					
				insertarWithHolding(inout, orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
						consecutivo, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
						diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento, 
						mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda, factorCampo, 
						montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, diaAnticipo, 
						mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso, documentNoID);
				
				if (consecutivo == contador ){ // Reinicio el contador del consecutivo para las próximas retenciones de la siguiente orden.
					
					consecutivo = 0;	
				}
				
				CPPD01.save();
				
				// Inicializo todas las variables para el proximo registro.
				
				inout=0;
				orderID = 0;
				invoiceID = 0;
				contador = 0;
				codigoProveedor = null;
				documentoOrigen = null;
				documentoAfectado = null;
				diaReg = 0;
				mesReg = 0;
				añoReg = 0;
				diaMovimiento = 0;
				mesMovimiento = 0;
				añoMovimiento = 0;
				diaVencimiento = 0;
				mesVencimiento = 0;
				añoVencimiento = 0;
				formaPago = 0;
				descripcionCondicionPago = null;
				montoImpuesto = Env.ZERO;
				pais = null;
				numeroControl = null;
				numeroComprobante = null;
				documentNoID = 0;	
				tipoMovimiento = null;
				
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
	}
	
	
	/*
	 * Procedimiento que en el cuál se realizar el set de los datos en la tabla E_XX_VCN_CPPD01.
	 * 
	 *  @param orderID es el ID de la orden de compra asociada a los registros de C_INVOICE.
	 *  @param invoiceID es el ID de la factura en la tabla C_INVOICE.
	 *  @param codigoProveedor es el ID del proveedor en el AS.
	 *  @param tipoProveedor string con el texto "PRO".
	 *  @param documentoOrigen número del documento de la orden de compra.
	 *  @param documentoAfectado número del documento de la factura.
	 *  @param consecutivo correlativo según el número de facturas que tenga asociada la O/C.
	 *  @param tipoMovimiento código del AS utilizado para identificar las facturas de las notas de débito, crédito y retenciones.
	 *  @param diaReg Día de generación de la aprobación, es decir día del match entre facturación y chequeo.
	 *  @param mesReg Mes de generación de la aprobación, es decir mes del match entre facturación y chequeo.
	 *  @param añoReg Año de generación de la aprobación, es decir Año del match entre facturación y chequeo.
	 *  @param diaAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param mesAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param añoAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param diaMovimiento Día de generación de la aprobación, es decir día del match entre facturación y chequeo.
	 *  @param mesMovimiento Mes de generación de la aprobación, es decir mes del match entre facturación y chequeo.
	 *  @param añoMovimiento Año de generación de la aprobación, es decir Año del match entre facturación y chequeo.
	 *  @param diaAnulacion campo que se llena siempre con el valor cero(0).
	 *  @param mesAnulacion campo que se llena siempre con el valor cero(0).
	 *  @param añoAnulacion campo que se llena siempre con el valor cero(0).	
	 *  @param diaVencimiento día de vecimiento en base a la condición de pago. 
	 *  @param mesVencimiento mes de vecimiento en base a la condición de pago. 
	 *  @param añoVencimiento año de vecimiento en base a la condición de pago. 
	 *  @param formaPago forma de pago de la O/C.
	 *  @param descripcionCondicionPago condicion de pago de la O/C.
	 *  @param descripcion string con el texto "PAGO A PROVEEDORES".
	 *  @param codigoMoneda campo que siempre lleva el ID = 1.
	 *  @param factorCampo campo que siempre lleva 0,000.
	 *  @param montoMovimiento Total de la Factura sin IVA.
	 *  @param montoImpuesto Total del Impuesto.
	 *  @param statusCuenta campo que siempre lleva el valor = 5.
	 *  @param statusGiro campo que siempre va en blanco.
	 *  @param statusGrabacion campo que siempre lleva el valor = 1.
	 *  @param ordenPago campo que siempre lleva el valor = 0.
	 *  @param diaAnticipo campo que se llena siempre con el valor cero(0).
	 *  @param mesAnticipo campo que se llena siempre con el valor cero(0).
	 *  @param añoAnticipo campo que se llena siempre con el valor cero(0).		 
	 *  @param paisProveedor es el país de distribución de la O/C.
	 *  @param numeroControl número de control de la factura en el libro de compras.
	 *  @param numeroComprobante número de comprobante de retención de la factura.
	 *  @param númeroAviso número de documento del aviso de débito o crédito.
	 *  
	 */
	
	public void insertarWithHolding (Integer inout, int orderID, int invoiceID, int codigoProveedor, String tipoProveedor, String documentoOrigen,
			String documentoAfectado, int consecutivo, String tipoMovimiento, int diaReg, int mesReg, int añoReg, int diaAprobacion,
			int mesAprobacion, int añoAprobacion, int diaMovimiento, int mesMovimiento, int añoMovimiento, int diaAnulacion,
			int mesAnulacion, int añoAnulacion, int diaVencimiento, int mesVencimiento, int añoVencimiento, int formaPago,
			String descripcionCondicionPago, String descripcion, int codigoMoneda, BigDecimal factorCampo, BigDecimal montoMovimiento,
			BigDecimal montoImpuesto, int statusCuenta, String statusGiro, int statusGrabacion, int ordenPago, int diaAnticipo,
			int mesAnticipo, int añoAnticipo, String paisProveedor, String numeroControl, int numeroComprobante, String numeroAviso,
			int documentNoID){
		
		CPPD01  = new X_E_XX_VCN_CPPD01(getCtx(), 0, get_TrxName());
		
        try{
        	
        	CPPD01.setNUMREC(inout);
			CPPD01.setC_Order_ID(orderID);
			CPPD01.setC_Invoice_ID(invoiceID);
			CPPD01.setC_BPartner_ID(codigoProveedor);
			CPPD01.setXX_VendorClass(tipoProveedor);
			CPPD01.setXX_DocumentNoOrder(inout.toString());
			CPPD01.setXX_DocumentNoInvoice(documentoAfectado);
			CPPD01.setXX_Consecutive(consecutivo);
			CPPD01.setXX_TypeOfMovement(tipoMovimiento);
			CPPD01.setXX_RegistrationDay(diaReg);
			CPPD01.setXX_RegistrationMonth(mesReg);
			CPPD01.setXX_RegistrationYear(añoReg);
			CPPD01.setXX_ApprovalDay(diaAprobacion);
			CPPD01.setXX_ApprovalMonth(mesAprobacion);
			CPPD01.setXX_ApprovalYear(añoAprobacion);
			CPPD01.setXX_MovementDay(diaMovimiento);
			CPPD01.setXX_MovementMonth(mesMovimiento);
			CPPD01.setXX_MovementYear(añoMovimiento);
			CPPD01.setXX_CancellationDay(diaAnulacion);
			CPPD01.setXX_CancellationMonth(mesAnulacion);
			CPPD01.setXX_CancellationYear(añoAnulacion);
			CPPD01.setXX_ExpirationDay(diaVencimiento);
			CPPD01.setXX_ExpirationMonth(mesVencimiento);
			CPPD01.setXX_ExpirationYear(añoVencimiento);
			CPPD01.setPaymentRule(formaPago);
			CPPD01.setPaymentTerm(descripcionCondicionPago);
			CPPD01.setXX_OrderDescription(descripcion);
			CPPD01.setC_Currency_ID(codigoMoneda);
			CPPD01.setXX_FieldFactor(factorCampo);
			CPPD01.setTotalLines(montoMovimiento);
			CPPD01.setXX_TaxAmount(montoImpuesto);
			CPPD01.setXX_DebtBalanceState(statusCuenta);
			CPPD01.setXX_EmittedDraftStatus(statusGiro);
			CPPD01.setXX_RecordingStatus(statusGrabacion);
			CPPD01.setXX_PaymentOrder(ordenPago);
			CPPD01.setXX_DayOfPaymentAdvance(diaAnticipo);
			CPPD01.setXX_MonthOfPaymentAdvance(mesAnticipo);
			CPPD01.setXX_YearOfPaymentAdvance(añoAnticipo);
			CPPD01.setCountryName(paisProveedor);
			CPPD01.setXX_ControlNumber(numeroControl);
			CPPD01.setXX_Withholding(numeroComprobante);
			CPPD01.setXX_ControlNumberDebCre(numeroAviso);		
			CPPD01.setAD_Org_ID(0);
			
			actualizarPurchaseBook(invoiceID, documentNoID);
			
        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
        }
	}
	
	
	/*
	 * Procedimiento que actualiza en la tabla C_INVOICE el campo XX_SYNCHRONIZED a 'Y' para indicar que el registro ya
	 * fue exportado.
	 * 
	 *  @param invoiceID es el ID de la factura en la tabla C_INVOICE.
	 */
	
	public void actualizarPurchaseBook(int invoiceID, int documentoAfectado)
	{
		String sqlUpdate = null;
		PreparedStatement pstmtUpdate = null;
       
		if (invoiceID == 0){
			
	        try{
			    sqlUpdate = "UPDATE XX_VCN_PURCHASESBOOK SET XX_SYNCHRONIZEDWITHHOLDING ='Y' WHERE C_INVOICE_ID=  " + documentoAfectado +
			    			" and XX_DOCUMENTNO_ID is null";
			    DB.executeUpdate(get_TrxName(),sqlUpdate);
	        }
	        catch (Exception e) {
	        	
	        	e.printStackTrace();   
			}
		}else{
			
	        try{
	  			sqlUpdate = "UPDATE XX_VCN_PURCHASESBOOK SET XX_SYNCHRONIZEDWITHHOLDING ='Y' WHERE C_INVOICE_ID=  " + documentoAfectado +
	  						" and XX_DOCUMENTNO_ID = " + invoiceID;
	  			DB.executeUpdate(get_TrxName(),sqlUpdate);
	  	    }
	  	    catch (Exception e) {	  	        	
	  	        e.printStackTrace();   
	  		}
		}
	}
	
	/*
	 * Export Notes
	 */
	private void exportNotes(){
		
		
		MPaymentTerm condicionPagoConsulta = null;

		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sqlConsulta = null;
		PreparedStatement pstmtConsulta = null;
		ResultSet rsConsulta = null;
	
		Date fechaAprobacion = null;
		Date fechaVencimiento;
		 
		BigDecimal montoMovimiento = new BigDecimal(0.000);
		BigDecimal montoImpuesto = new BigDecimal(0.000);
		BigDecimal factorCampo = new BigDecimal(0.000);
		BigDecimal impuestoTruncate = new BigDecimal(0.000);
		BigDecimal montoTruncate = new BigDecimal(0.000);
		
		Integer codigoProveedor = 0, consecutivo = 0, contador = 0;
		Integer diaReg = null,	mesReg = null,	añoReg = null;
		Integer diaMovimiento = null, mesMovimiento = null, añoMovimiento = null, diaVencimiento = null, mesVencimiento = null, añoVencimiento = null;
		Integer condicionPago = null, numeroComprobante = null, diasFinanciamiento = 0;
		Integer bPartnerID, orderID, invoiceID, formaPago = null, inout=0;
		
		Integer diaAprobacion = 0, mesAprobacion = 0, añoAprobacion = 0; 
		Integer mesAnulacion = 0,	añoAnulacion = 0, diaAnulacion = 0;
		Integer codigoMoneda = 1, documentNoID, IDpurchasesBook;
		Integer mesAnticipo = 0, añoAnticipo = 0, diaAnticipo = 0, ordenPago = 0;
		Integer statusGrabacion = 1, statusCuenta = 5, decimalPlaces = 2;
	
		String statusGiro = "", descripcion = "PAGO A PROVEEDORES", tipoProveedor = "PRO", descripcionCondicionPago;
		String numeroAviso = null, fechaCadena, numeroControl, fecha, pais = null;
		String documentoAfectado = null, tipoMovimiento = null, documentoOrigen = null, converted;
		
		sql = "select mio.DOCUMENTNO as NUMREC, pb.C_bpartner_ID, pb.C_Order_ID, pb.C_Invoice_ID, pb.xx_documentno_ID, pb.XX_VCN_PurchasesBook_ID, " +
			"pb.XX_ControlNumber, pb.xx_withholdingtax, pb.xx_withholding, pb.XX_TaxableBase, pb.XX_TaxAmount, pb.xx_converted " +
			"from xx_vcn_purchasesbook pb, m_inout mio " +
			"where pb.XX_SYNCHRONIZED ='N' " +
			"and mio.c_order_id(+)=pb.c_order_id " +
			"and pb.xx_documentno_ID is not null " +
			"and pb.C_Invoice_ID is not null " +
			"and pb.C_Order_ID is not null " +
			"and pb.xx_converted = 'Y' " +
			//CCapote para que se vengan al as400 los reversos de las notas
			"and pb.xx_withholding is not null " +
			"order by pb.C_Order_ID, pb.C_Invoice_ID, pb.XX_VCN_PurchasesBook_ID";  
		
		pstmt = DB.prepareStatement(sql, null);
	
		try {
			
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				
				inout = rs.getInt("NUMREC");
				documentNoID = rs.getInt("C_INVOICE_ID"); 				// ID de la factura afectada.
				orderID = rs.getInt("C_ORDER_ID");						// ID de la orden.
				bPartnerID = rs.getInt("C_BPARTNER_ID"); 				// ID del proveedor de la factura.
				numeroControl = rs.getString("XX_CONTROLNUMBER"); 		// Número de control de la factura. Si el registro es un aviso no tiene número de comprobante.
				invoiceID = rs.getInt("xx_documentno_ID"); 				// ID de la factura actual.
				numeroComprobante = rs.getInt("xx_withholding");		// Número de Comprobante de la factura.
				montoMovimiento = rs.getBigDecimal("XX_TAXABLEBASE");	// Monto de la nota.
				montoImpuesto = rs.getBigDecimal("XX_TAXAMOUNT");		// Impuesto de la nota.
				converted = rs.getString("XX_CONVERTED");				// Indica si es un reverso o una nota generada a partir de éste.
				IDpurchasesBook = rs.getInt("XX_VCN_PurchasesBook_ID"); // ID del registro en la tabla de Libro de Compras.
				
				/*
				 * Se filtran las facturas ya que solo se insertan reversos y notas en caso de que tenga el estado xx_converted = 'Y'.
				 * */
				
				if (invoiceID != 0){	
					
					/*
					 * Inicialiando las variables si algo de la base de datos esta NULL.
					 * */
					
					if (numeroComprobante == null){
						
						numeroComprobante = 0;
					}
					if (invoiceID == null){
						
						invoiceID = 0;
					}
					if (bPartnerID == null){
						
						bPartnerID = 0;
					}
					if (orderID == null){
										
						orderID = 0;
					}
					if (documentNoID == null){
						
						documentNoID = 0;
					}
					if (montoMovimiento == null){
						
						montoMovimiento = Env.ZERO;
					}
					if (montoImpuesto == null){
						
						montoImpuesto = Env.ZERO;
					}
	
					
					if ((montoMovimiento.compareTo(new BigDecimal(0)))== -1){
						
						tipoMovimiento = "55"; // Crédito
						
						MInvoice invoice = new MInvoice(Env.getCtx(), invoiceID, null);

						if(invoice.getDescription()!=null && invoice.getDescription().equalsIgnoreCase("Descuento por Retraso en la Entrega Nacional"))
							tipoMovimiento = "86";
						
						if(invoice.getDescription()!=null && invoice.getDescription().equalsIgnoreCase("Acuerdo Comercial"))
							tipoMovimiento = "85";
						
						/*
						 * Sentencia que devuelve la cantidad de avisos y notas de crédito que ya están en la tabla E_XX_VCN_CPPD01.
						 * */
		
						sqlConsulta = "select count(C_Order_ID) " +
								"from E_XX_VCN_CPPD01 " +
								"where xx_typeofmovement = '55' " +
								"and C_Order_ID = "+orderID+"";
						pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
						rsConsulta = pstmtConsulta.executeQuery();
						
						while (rsConsulta.next()){
							
							contador = rsConsulta.getInt(1);	
						}

						DB.closeResultSet(rsConsulta);
						DB.closeStatement(pstmtConsulta);
	
					}
					if ((montoMovimiento.compareTo(new BigDecimal(0)))== 1){
						
						tipoMovimiento = "04";	// Débito
						
						/*
						 * Sentencia que devuelve la cantidad de avisos y notas de débito que ya están en la tabla E_XX_VCN_CPPD01.
						 * */
		
						sqlConsulta = "select count(C_Order_ID) " +
								"from E_XX_VCN_CPPD01 " +
								"where xx_typeofmovement = '04' " +
								"and C_Order_ID = "+orderID+"";
						pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
						rsConsulta = pstmtConsulta.executeQuery();
						
						while (rsConsulta.next()){
							
							contador = rsConsulta.getInt(1);	
						}

						DB.closeResultSet(rsConsulta);
						DB.closeStatement(pstmtConsulta);
					}
				
					/*
					 * Sentencia que devuelve el ID del AS del proveedor asociado al registro de la factura actual.
					 * */
					
					sqlConsulta = "select value " +
								"from c_bpartner " +
								"where c_bpartner_id = "+bPartnerID+"";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
				
					while (rsConsulta.next()){
						codigoProveedor = rsConsulta.getInt("VALUE");
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
					
					if (codigoProveedor == null){
						codigoProveedor = 0;
					}
					
					/*
					 * Sentencia que devuelve los datos relacionados a la orden de pago correspondiente al registro actual.
					 * */
		
					sqlConsulta = "SELECT CASE " +
								  "WHEN co.XX_INVOICEDATE>co.XX_CHECKUPDATE THEN XX_INVOICEDATE " +
								  "ELSE co.XX_CHECKUPDATE END NOTEDATE, (Select a.value from C_PaymentTerm a where a.C_PaymentTerm_ID=co.C_PaymentTerm_ID) C_PAYMENTTERM_ID, co.paymentRule, co.DOCUMENTNO, " +
								  "co.created " +
								  "from c_order co " +
								  "where co.c_order_id = "+orderID+"";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
		
					while (rsConsulta.next()){
						
				//		fechaAprobacion = rs.getDate("XX_INVOICEDATE");			// Fecha de match entre facturación y chequeo.
						fechaAprobacion = rsConsulta.getDate("NOTEDATE");	    // Fecha de match entre facturación y chequeo.
						documentoOrigen = rsConsulta.getString("DOCUMENTNO");	// Número de Orden de compra.
						condicionPago = rsConsulta.getInt("C_PAYMENTTERM_ID");	// ID de la condición de pago.
						// Forma de pago de la orden de compra.
						if(rsConsulta.getString("PAYMENTRULE").equals("B"))
							formaPago = 1;
						else if(rsConsulta.getString("PAYMENTRULE").equals("S"))
							formaPago = 2;
						else if(rsConsulta.getString("PAYMENTRULE").equals("P"))
							formaPago = 3;
						else if(rsConsulta.getString("PAYMENTRULE").equals("T"))
							formaPago = 4;
						else
							formaPago = 0;
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
	
					if (condicionPago == null){
						
						condicionPago = 0;
					}		
					if (formaPago == null){
						
						formaPago = 0;
					}
					
					/*
					 * Sentencia que devuelve la descripción de la condicion de pago de la orden de pago actual.
					 * */
	 
					descripcionCondicionPago = condicionPago.toString();
					
					if (condicionPagoConsulta == null){
						descripcionCondicionPago = "";
					}
					
					/*
					 * Sentencia que devuelve la fecha de vencimiento a partir de la fecha de aprobación y los dás de financiamiento.
					 * */
		
					sqlConsulta = "select xx_daysfunding from c_paymentterm where c_paymentterm_id = "+condicionPago+"";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
		
					while (rsConsulta.next()){
						diasFinanciamiento = rsConsulta.getInt(1);
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
					
					if (diasFinanciamiento == null){
						
						diasFinanciamiento = 0;
					}
					
					/* 
					 * Suma a la fecha de aprobación los días de financiamiento
					para obtener la fecha de vencimiento */
					
					fechaVencimiento = calcularFechaVencimiento(fechaAprobacion,diasFinanciamiento);				
									
					/*
					 * El siguiente código toma la fecha de vencimiento del registro y la divide en cadenas para obtener dia, mes y año.
					 * */
					
					fechaCadena = fechaVencimiento.toString();
					String fechaToken = fechaCadena;
					StringTokenizer token2 = new StringTokenizer(fechaToken, "-");   
					Integer count2 = token2.countTokens();
					
					for (int i = 1; i <= count2; i++) {
						
						if (i == 1){
							añoVencimiento = Integer.parseInt(token2.nextToken());
													
						}else if (i == 2){
							mesVencimiento = Integer.parseInt(token2.nextToken());
													
						}else if (i == 3){
							diaVencimiento = Integer.parseInt(token2.nextToken());
						
						}
					}
					
					/*
					 * El siguiente código toma la fecha de creación del registro y la divide en cadenas para obtener dia, mes y año.
					 * */
					
					fecha = fechaAprobacion.toString();
					String fechaTokenizer = fecha;
					StringTokenizer tokens = new StringTokenizer(fechaTokenizer, "-");   
					Integer count = tokens.countTokens();
					
					for (int i = 1; i <= count; i++) {
						
						if (i == 1){
							añoReg = Integer.parseInt(tokens.nextToken());
							añoMovimiento = añoReg;
							
						}else if (i == 2){
							mesReg = Integer.parseInt(tokens.nextToken());
							mesMovimiento = mesReg;
							
						}else if (i == 3){
							diaReg = Integer.parseInt(tokens.nextToken());
							diaMovimiento = diaReg;
						}
					}
					
					/*
					 * Sentencia que devuelve el país de distribución de la O/C.
					 * */
		
					sqlConsulta = "select value from c_country where c_country_id = (select c_country_id from c_order where c_order_id="+orderID+")";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
		
					while (rsConsulta.next()){
						pais = rsConsulta.getString(1);
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
	
					/*
					 * Sentencia que devuelve el número de documento de la factura actual.
					 * */
	
					sqlConsulta = "SELECT DOCUMENTNO FROM C_INVOICE WHERE C_INVOICE_ID = "+invoiceID+"";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, null);
					rsConsulta = pstmtConsulta.executeQuery();
					
					while (rsConsulta.next()){
						
						numeroAviso = rsConsulta.getString("DOCUMENTNO");	
					}
					rsConsulta.close();
					pstmtConsulta.close();
					
					/*
					 * Sentencia que devuelve el número de documento de la factura a la que hace referencia la retención.
					 * */
	
					sqlConsulta = "SELECT DOCUMENTNO FROM C_INVOICE WHERE C_INVOICE_ID = "+documentNoID+"";
					pstmtConsulta = DB.prepareStatement(sqlConsulta, get_TrxName());
					rsConsulta = pstmtConsulta.executeQuery();
					
					while (rsConsulta.next()){
						
						documentoAfectado = rsConsulta.getString("DOCUMENTNO");	
					}

					DB.closeResultSet(rsConsulta);
					DB.closeStatement(pstmtConsulta);
					
					/*
					 *  Se asigna el consecutivo al registro actual y se cambian los montos a positivo ya que el código
					 *  del as es el que indica su signo.
					 */
					
	/*				System.out.println(tipoProveedor+" idProve: "+codigoProveedor+" docOri: "+documentoOrigen+" Consec: "+" docAfec: "+
							documentoAfectado+" Timopa: "+tipoMovimiento+" fechaReg: "+añoReg+"/"+mesReg+"/"+diaReg+" fechaApr"+
							añoAprobacion+"/"+mesAprobacion+"/"+diaAprobacion+" fechaMov: "+añoMovimiento+"/"+mesMovimiento+"/"+
							diaMovimiento+" fecAnu: "+añoAnulacion+"/"+mesAnulacion+"/"+diaAnulacion+" fecVen: "+añoVencimiento+"/"+
							mesVencimiento+"/"+diaVencimiento+" forPago: "+formaPago+" CondPagoDias: "+descripcionCondicionPago+
							" descO/P: "+descripcion+" codMon: "+codigoMoneda+" facCamp: "+factorCampo+" monMov: "+montoMovimiento+
							" monImp: "+montoImpuesto+" staCtaxP: "+statusCuenta+" staGiro: "+statusGiro+" staGrab: "+statusGrabacion+
							" o/p: "+ordenPago+" fecAntic: "+añoAnticipo+"/"+mesAnticipo+"/"+diaAnticipo+" Pais: "+paisProveedor+
							" nroCtl: "+numeroControl+" nroComp: "+numeroComprobante+" nroAviso: "+numeroAviso);
	*/				
					/*
					 *  Se verifica si es un reverso de un aviso o una nota de débito o crédito y se inserta en la tabla.
					 */
					
					if (!(numeroControl == null)){ // El registro actual es una Nota
						
						
						contador++;
						consecutivo = contador;
						
						if ((montoMovimiento.compareTo(new BigDecimal(0)))== -1){ 
							
							montoMovimiento = montoMovimiento.multiply(new BigDecimal(-1));
							montoImpuesto = montoImpuesto.multiply(new BigDecimal(-1));
						}
						
						montoTruncate = montoMovimiento.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
						impuestoTruncate = montoImpuesto.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
						
						if (converted.equals("N")){ // Hay que actualizar el aviso en la tabla E_XX_VCN_CPPD01
							
							Integer existe = existeAviso(invoiceID);
							
							if( existe != 0){
							
								actualizarAviso(numeroAviso, invoiceID, numeroControl);
								actualizarNotas(IDpurchasesBook);
								
							}else{
								
								System.out.println("Nota: "+invoiceID+" order: "+orderID+" consec: "+consecutivo);
								
								insertarNotas(inout, orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
										consecutivo, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
										diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento,
										mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda,
										factorCampo, montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, 
										diaAnticipo, mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso, IDpurchasesBook);
								
								CPPD01.save();
							}
							
						}else { // Se inserta la nota nueva.
							
							System.out.println("Nota: "+invoiceID+" order: "+orderID+" consec: "+consecutivo);
							
							insertarNotas(inout,orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
									consecutivo, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
									diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento,
									mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda,
									factorCampo, montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, 
									diaAnticipo, mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso,IDpurchasesBook);
							CPPD01.save();
						}

						
					}else{ // El registro actual es un aviso
						
						if (converted.equals("Y")){ // El registro actual es un Reverso
							
							contador++;
							consecutivo = contador;
							
							if ((montoMovimiento.compareTo(new BigDecimal(0)))== -1){ 
								
								montoMovimiento = montoMovimiento.multiply(new BigDecimal(-1));
								montoImpuesto = montoImpuesto.multiply(new BigDecimal(-1));
							}
							
							montoTruncate = montoMovimiento.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
							impuestoTruncate = montoImpuesto.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
							
							System.out.println("Reverso "+invoiceID+" order: "+orderID+" consec: "+consecutivo);
							
							actualizarAviso(numeroAviso, invoiceID, numeroControl);
							
							insertarNotas(inout, orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
									consecutivo, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
									diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento,
									mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda,
									factorCampo, montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, 
									diaAnticipo, mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso,IDpurchasesBook);
							
							CPPD01.save();
							
							
						}else if (converted.equals("N")){ // El registro actual es un Aviso
														
							Integer existe = existeAvisoEnInvoice(invoiceID);
							
							if( existe.compareTo(0) == 0){ // Esta completada en Facturación.
								
								Integer existeCPPD01 = existeAviso(invoiceID);
								
								if (existeCPPD01 == 0){
									
									contador++;
									consecutivo = contador;
									
									if ((montoMovimiento.compareTo(new BigDecimal(0)))== -1){ 
										
										montoMovimiento = montoMovimiento.multiply(new BigDecimal(-1));
										montoImpuesto = montoImpuesto.multiply(new BigDecimal(-1));
									}
									
									montoTruncate = montoMovimiento.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
									impuestoTruncate = montoImpuesto.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
									
									System.out.println("Aviso "+invoiceID+" order: "+orderID+" consec: "+consecutivo);
									
									insertarNotas(inout, orderID, invoiceID, codigoProveedor, tipoProveedor, documentoOrigen, documentoAfectado, 
											consecutivo, tipoMovimiento, diaReg, mesReg, añoReg, diaAprobacion, mesAprobacion, añoAprobacion, 
											diaMovimiento, mesMovimiento, añoMovimiento, diaAnulacion, mesAnulacion, añoAnulacion, diaVencimiento,
											mesVencimiento, añoVencimiento, formaPago, descripcionCondicionPago, descripcion, codigoMoneda,
											factorCampo, montoTruncate, impuestoTruncate, statusCuenta, statusGiro, statusGrabacion, ordenPago, 
											diaAnticipo, mesAnticipo, añoAnticipo, pais, numeroControl, numeroComprobante, numeroAviso, IDpurchasesBook);
									
									CPPD01.save();
								}
								
							}
						}
						
					}
					
					// Inicializo todas las variables para el proximo registro.
					
					inout = 0;
					orderID = 0;
					invoiceID = 0;
					consecutivo = 0; 
					contador = 0;
					codigoProveedor = 0;
					documentoOrigen = null;
					documentoAfectado = null;
					tipoMovimiento = null;
					añoReg = 0;
					mesReg = 0;
					diaReg = 0;
					añoMovimiento = 0;
					mesMovimiento = 0;
					diaMovimiento = 0;
					añoVencimiento = 0;
					mesVencimiento = 0;
					diaVencimiento = 0;
					formaPago = 0;
					descripcionCondicionPago = null;
					montoMovimiento = Env.ZERO;
					montoImpuesto = Env.ZERO;
					pais = null;
					numeroControl = null;
					numeroComprobante = null;
					numeroAviso = null;
					
				} // Fin del IF que filtra las facturas.	
			}
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	private Integer existeAvisoEnInvoice(Integer invoiceID) {
		
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer existe = null;
		String status = null;
       
        try{

		    sql = "select docstatus from c_invoice where c_invoice_id ="+invoiceID+"";
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery(); 
			
			while (rs.next()){
				status = rs.getString("DOCSTATUS");
			}
			rs.close();
			pstmt.close();
			
			if(status.equals("CO")){
				
				existe = 0;
			}
			else{
				
				existe = 1;
			}
			
        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
		}
       
		return existe;
	}
	
	/*
	 * Procedimiento que verifica si el aviso ya fue exportado desde la tabla c_invoice y se encuentra en e_xx_vcn_cppd01.
	 * @param invoiceID es el ID de la factura en la tabla XX_VCN_PURCHASESBOOK.
	 */
	
	Integer existeAviso(int invoiceID)
	{
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer existe = null;
       
        try{
        	
		    sql = "select count(c_invoice_id) from e_xx_vcn_cppd01 where c_invoice_id ="+invoiceID+" and xx_controlnumber is null";
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery(); 
			while (rs.next()){
				existe = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			
			if (existe > 0){
				
				existe = 1;
			}
			else if (existe == 0){
				
				existe = 0;
			}
        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
		}
       
		return existe;
	}
	
	/*
	 * Procedimiento que actualiza en la tabla E_XX_VCN_CPPD01 el campo XX_WITHHOLDING, XX_ControlNumber 
	 * y XX_ControlNumberDebCre en caso de que se haya convertido un solo aviso a nota por lo que no se genero
	 * un reverso.
	 * 
	 *  @param invoiceID es el ID de la factura en la tabla C_INVOICE.
	 */
	
	public void actualizarAviso(String documentNo, int invoiceID, String numeroControl)
	{
		String sqlUpdate = null;
        try{        	
		    sqlUpdate = "UPDATE E_XX_VCN_CPPD01 SET XX_ControlNumberDebCre = " + documentNo +
		    		" , XX_ControlNumber = " + numeroControl + 
		    		" WHERE C_INVOICE_ID = " + invoiceID;
		    DB.executeUpdate(null,sqlUpdate);
        }
        catch (Exception e) {       	
        	e.printStackTrace();   
		}
	}
	
	/*
	 * Procedimiento que actualiza en la tabla XX_VCN_PURCHASESBOOK el campo XX_SYNCHRONIZED a 'Y' para indicar que el registro ya
	 * fue exportado.
	 * 
	 *  @param invoiceID es el ID de la factura en la tabla XX_VCN_PURCHASESBOOK.
	 */
	
	public void actualizarNotas(int ID)
	{
		String sqlUpdate = null;
       
        try{
        	
		    sqlUpdate = "UPDATE XX_VCN_PURCHASESBOOK SET XX_SYNCHRONIZED ='Y' WHERE XX_VCN_PurchasesBook_ID = "+ID+"";
		    DB.executeUpdate(get_TrxName(),sqlUpdate);

        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
		}
	}
	
	
	/*
	 * Procedimiento en el cuál se realiza el set de los datos para la tabla E_XX_VCN_CPPD01.
	 * 
	 *  @param orderID es el ID de la orden de compra asociada a los registros de C_INVOICE.
	 *  @param invoiceID es el ID de la factura en la tabla C_INVOICE.
	 *  @param codigoProveedor es el ID del proveedor en el AS.
	 *  @param tipoProveedor string con el texto "PRO".
	 *  @param documentoOrigen número del documento de la orden de compra.
	 *  @param documentoAfectado número del documento de la factura.
	 *  @param consecutivo correlativo según el número de facturas que tenga asociada la O/C.
	 *  @param tipoMovimiento código del AS utilizado para identificar las facturas de las notas de débito, crédito y retenciones.
	 *  @param diaReg Día de generación de la aprobación, es decir día del match entre facturación y chequeo.
	 *  @param mesReg Mes de generación de la aprobación, es decir mes del match entre facturación y chequeo.
	 *  @param añoReg Año de generación de la aprobación, es decir Año del match entre facturación y chequeo.
	 *  @param diaAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param mesAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param añoAprobacion campo que se llena siempre con el valor cero(0).
	 *  @param diaMovimiento Día de generación de la aprobación, es decir día del match entre facturación y chequeo.
	 *  @param mesMovimiento Mes de generación de la aprobación, es decir mes del match entre facturación y chequeo.
	 *  @param añoMovimiento Año de generación de la aprobación, es decir Año del match entre facturación y chequeo.
	 *  @param diaAnulacion campo que se llena siempre con el valor cero(0).
	 *  @param mesAnulacion campo que se llena siempre con el valor cero(0).
	 *  @param añoAnulacion campo que se llena siempre con el valor cero(0).	
	 *  @param diaVencimiento día de vecimiento en base a la condición de pago. 
	 *  @param mesVencimiento mes de vecimiento en base a la condición de pago. 
	 *  @param añoVencimiento año de vecimiento en base a la condición de pago. 
	 *  @param formaPago forma de pago de la O/C.
	 *  @param descripcionCondicionPago condicion de pago de la O/C.
	 *  @param descripcion string con el texto "PAGO A PROVEEDORES".
	 *  @param codigoMoneda campo que siempre lleva el ID = 1.
	 *  @param factorCampo campo que siempre lleva 0,000.
	 *  @param montoMovimiento Total de la Factura sin IVA.
	 *  @param montoImpuesto Total del Impuesto.
	 *  @param statusCuenta campo que siempre lleva el valor = 5.
	 *  @param statusGiro campo que siempre va en blanco.
	 *  @param statusGrabacion campo que siempre lleva el valor = 1.
	 *  @param ordenPago campo que siempre lleva el valor = 0.
	 *  @param diaAnticipo campo que se llena siempre con el valor cero(0).
	 *  @param mesAnticipo campo que se llena siempre con el valor cero(0).
	 *  @param añoAnticipo campo que se llena siempre con el valor cero(0).		 
	 *  @param paisProveedor es el país de distribución de la O/C.
	 *  @param numeroControl número de control de la factura en el libro de compras.
	 *  @param numeroComprobante número de comprobante de retención de la factura.
	 *  @param númeroAviso número de documento del aviso de débito o crédito.
	 *  @param IDpurchasesbook ID del registro en el libro de compras.
	 */
	
	public void insertarNotas (Integer inout, int orderID, int invoiceID, int codigoProveedor, String tipoProveedor, String documentoOrigen,
			String documentoAfectado, int consecutivo, String tipoMovimiento, int diaReg, int mesReg, int añoReg, int diaAprobacion,
			int mesAprobacion, int añoAprobacion, int diaMovimiento, int mesMovimiento, int añoMovimiento, int diaAnulacion,
			int mesAnulacion, int añoAnulacion, int diaVencimiento, int mesVencimiento, int añoVencimiento, int formaPago,
			String descripcionCondicionPago, String descripcion, int codigoMoneda, BigDecimal factorCampo, BigDecimal montoMovimiento,
			BigDecimal montoImpuesto, int statusCuenta, String statusGiro, int statusGrabacion, int ordenPago, int diaAnticipo,
			int mesAnticipo, int añoAnticipo, String paisProveedor, String numeroControl, int numeroComprobante, String numeroAviso,
			int IDpurchasesbook){
		
		CPPD01  = new X_E_XX_VCN_CPPD01(getCtx(), 0, get_TrxName());
		
        try{
        	
        	CPPD01.setNUMREC(inout);
			CPPD01.setC_Order_ID(orderID);
			CPPD01.setC_Invoice_ID(invoiceID);
			CPPD01.setC_BPartner_ID(codigoProveedor);
			CPPD01.setXX_VendorClass(tipoProveedor);
			CPPD01.setXX_DocumentNoOrder(inout.toString());
			CPPD01.setXX_DocumentNoInvoice(documentoAfectado);
			CPPD01.setXX_Consecutive(consecutivo);
			CPPD01.setXX_TypeOfMovement(tipoMovimiento);
			CPPD01.setXX_RegistrationDay(diaReg);
			CPPD01.setXX_RegistrationMonth(mesReg);
			CPPD01.setXX_RegistrationYear(añoReg);
			CPPD01.setXX_ApprovalDay(diaAprobacion);
			CPPD01.setXX_ApprovalMonth(mesAprobacion);
			CPPD01.setXX_ApprovalYear(añoAprobacion);
			CPPD01.setXX_MovementDay(diaMovimiento);
			CPPD01.setXX_MovementMonth(mesMovimiento);
			CPPD01.setXX_MovementYear(añoMovimiento);
			CPPD01.setXX_CancellationDay(diaAnulacion);
			CPPD01.setXX_CancellationMonth(mesAnulacion);
			CPPD01.setXX_CancellationYear(añoAnulacion);
			CPPD01.setXX_ExpirationDay(diaVencimiento);
			CPPD01.setXX_ExpirationMonth(mesVencimiento);
			CPPD01.setXX_ExpirationYear(añoVencimiento);
			CPPD01.setPaymentRule(formaPago);
			CPPD01.setPaymentTerm(descripcionCondicionPago);
			CPPD01.setXX_OrderDescription(descripcion);
			CPPD01.setC_Currency_ID(codigoMoneda);
			CPPD01.setXX_FieldFactor(factorCampo);
			CPPD01.setTotalLines(montoMovimiento);
			CPPD01.setXX_TaxAmount(montoImpuesto);
			CPPD01.setXX_DebtBalanceState(statusCuenta);
			CPPD01.setXX_EmittedDraftStatus(statusGiro);
			CPPD01.setXX_RecordingStatus(statusGrabacion);
			CPPD01.setXX_PaymentOrder(ordenPago);
			CPPD01.setXX_DayOfPaymentAdvance(diaAnticipo);
			CPPD01.setXX_MonthOfPaymentAdvance(mesAnticipo);
			CPPD01.setXX_YearOfPaymentAdvance(añoAnticipo);
			CPPD01.setCountryName(paisProveedor);
			CPPD01.setXX_ControlNumber(numeroControl);
			CPPD01.setXX_Withholding(numeroComprobante);
			CPPD01.setXX_ControlNumberDebCre(numeroAviso);
			CPPD01.setAD_Org_ID(0);
			
			actualizarNotas(IDpurchasesbook);
			
        }
        catch (Exception e) {
        	
        	e.printStackTrace();   
        }
	}
	
	/*
	 * Exporta la E_XX_VMR_CPPD01 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_CPPD01DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS;
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
        	
        try
        {    
        	
        	//Borra la data de la tabla CPPD01 en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.CPPD01C";
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere =  "SELECT XX_VENDORCLASS,C_BPARTNER_ID,XX_DOCUMENTNOORDER,XX_CONSECUTIVE,"
	        					+"NVL(XX_DOCUMENTNOINVOICE,' ') XX_DOCUMENTNOINVOICE,XX_TYPEOFMOVEMENT,XX_REGISTRATIONDAY,XX_REGISTRATIONMONTH,"
	        					+"XX_REGISTRATIONYEAR,XX_APPROVALDAY,XX_APPROVALMONTH,XX_APPROVALYEAR,"
	        					+"XX_MOVEMENTDAY,XX_MOVEMENTMONTH,XX_MOVEMENTYEAR,XX_CANCELLATIONDAY,"
	        					+"XX_CANCELLATIONMONTH,XX_CANCELLATIONYEAR,XX_EXPIRATIONDAY,XX_EXPIRATIONMONTH,"
				        		+"XX_EXPIRATIONYEAR,PAYMENTTERM,PAYMENTRULE,XX_ORDERDESCRIPTION,C_CURRENCY_ID,"
								+"XX_FIELDFACTOR,TOTALLINES,XX_TAXAMOUNT,XX_DAYOFPAYMENTADVANCE,"
								+"XX_MONTHOFPAYMENTADVANCE,XX_YEAROFPAYMENTADVANCE,COUNTRYNAME,NVL(XX_CONTROLNUMBER,' ') XX_CONTROLNUMBER,"
								+"XX_WITHHOLDING,NVL(XX_CONTROLNUMBERDEBCRE,' ') XX_CONTROLNUMBERDEBCRE "
	        					+"FROM E_XX_VCN_CPPD01";
	        	
	        	PreparedStatement ps_Compiere = DB.prepareStatement(SQL_Compiere,get_TrxName());
	        	ResultSet rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	
		            	SQL_AS = "INSERT INTO BECOFILE.CPPD01C (TIEMPE,COEMPE,DOCORI,CONSEC,DOCAFE,TIMOPA,DIAREG,"
		            	+"MESREG,AÑOREG,DIAAPR,MESAPR,AÑOAPR,DIAMOV,MESMOV,AÑOMOV,DIAANU,MESANU,AÑOANU,DIAVEN," 
		            	+"MESVEN,AÑOVEN,CONPAG,FORPAG,DESORP,CODMON,FACCAM,MONMOV,MIPMOV," 
		            	+"DIAANT,MESANT,AÑOANT,PAIS,NUMCTL,NROCOM,DEBCRE) "
		            	+ " VALUES('"
			            + rs.getString("XX_VENDORCLASS") +"'," 
			            + rs.getInt("C_BPARTNER_ID") +",'" 
			            + rs.getString("XX_DOCUMENTNOORDER") +"'," 
			            + rs.getInt("XX_CONSECUTIVE") +",'"
			            + rs.getString("XX_DOCUMENTNOINVOICE") +"','" 
			            + rs.getString("XX_TYPEOFMOVEMENT") +"'," 
			            + rs.getInt("XX_REGISTRATIONDAY") +","    
			            + rs.getInt("XX_REGISTRATIONMONTH") +"," 
			            + rs.getInt("XX_REGISTRATIONYEAR") +"," 
			            + rs.getInt("XX_APPROVALDAY") +","
			            + rs.getInt("XX_APPROVALMONTH") +"," 
			            + rs.getInt("XX_APPROVALYEAR") +"," 
			            + rs.getInt("XX_MOVEMENTDAY") +"," 
			            + rs.getInt("XX_MOVEMENTMONTH") +"," 
			            + rs.getInt("XX_MOVEMENTYEAR") +"," 
			            + rs.getBigDecimal("XX_CANCELLATIONDAY") +"," 
			            + rs.getBigDecimal("XX_CANCELLATIONMONTH") +"," 
			            + rs.getBigDecimal("XX_CANCELLATIONYEAR") +"," 
			            + rs.getInt("XX_EXPIRATIONDAY") +"," 
			            + rs.getInt("XX_EXPIRATIONMONTH") +"," 
			            + rs.getInt("XX_EXPIRATIONYEAR") +","
			            + rs.getInt("PAYMENTTERM") +"," 
			            + rs.getInt("PAYMENTRULE") +",'" 
			            + rs.getString("XX_ORDERDESCRIPTION") +"'," 
			            + rs.getInt("C_CURRENCY_ID") +"," 
			            + rs.getBigDecimal("XX_FIELDFACTOR") +"," 
			            + rs.getBigDecimal("TOTALLINES") +"," 
			            + rs.getBigDecimal("XX_TAXAMOUNT") +"," 
			            + rs.getInt("XX_DAYOFPAYMENTADVANCE") +"," 
			            + rs.getInt("XX_MONTHOFPAYMENTADVANCE") +"," 
			            + rs.getInt("XX_YEAROFPAYMENTADVANCE") +",'" 
			            + rs.getString("COUNTRYNAME") +"','" 
			            + rs.getString("XX_CONTROLNUMBER") +"'," 
			            + rs.getInt("XX_WITHHOLDING") +",'" 
			            + rs.getString("XX_CONTROLNUMBERDEBCRE") 
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
        	
        	sentencia.close();
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		}
	
        As.desconectar();
		return r;
		
	}
	
	/*
	 * Exporta la E_XX_VMR_NRAM02 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_NRAM02DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS;
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
    	Statement sentencia = null;
    	PreparedStatement ps_Compiere = null;
    	ResultSet rs = null;
        try
        {    
        	
        	//Borra la data de la tabla NRAM02C en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.NRAM02C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere =  "SELECT XX_STATUS_SINC, NUMFAC, NUMORD, NUMREC, GUIAEM, CODMON, "
	        				  + "ROUND(FACCAM, 3) AS FACCAM, ROUND(MONFAC, 2) AS MONFAC, "   
	        				  + "DIAREG, MESREG, XX_ANOREG, COEMPE, BULREC, round(MONIMP, 2) as MONIMP, "
	        				  + "TIEFAC FROM E_XX_VMR_NRAM02";
	        	
	        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,null);
	        	rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.NRAM02C (NUMORD, NUMFAC, NUMREC, GUIAEM, CODMON, "
	        				   + "FACCAM, MONFAC, DIAREG, MESREG, AÑOREG, COEMPE, "   
	        				   + "BULREC, MONIMP, TIEFAC)"
	        				   + " VALUES("
	        				   		+ rs.getInt("NUMORD") +",'" 
	        				   		+ rs.getString("NUMFAC") + "',"
			                	    + rs.getInt("NUMREC") +",'" 
			                	    + rs.getString("GUIAEM") +"'," 
			                		+ rs.getInt("CODMON") +","
			                		+ "coalesce("+rs.getBigDecimal("FACCAM")+",0)," 
			               			+ rs.getBigDecimal("MONFAC") +"," 
			               			+ rs.getInt("DIAREG") +"," 
			               			+ rs.getInt("MESREG") +"," 
			                		+ rs.getInt("XX_ANOREG") +"," 
			                		+ rs.getInt("COEMPE") +"," 
			                		+ rs.getInt("BULREC") +"," 
			                		+ rs.getBigDecimal("MONIMP") +"," 
			                		+ rs.getInt("TIEFAC")
			                		+")" ;
			                		
			                		
		            	if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 || i==81000){
							As.desconectar();
							As.conectar();
						}
						            
			            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            r2 = As.realizarSentencia(SQL_AS, sentencia);
			            
			            //Si se inserto correctamente lo actualizo en Y
			            if(r2==1){
			            	rs.updateString("XX_STATUS_SINC", "Y");
			            	rs.updateRow();
			            }
			            
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
			DB.closeResultSet(rs);
			DB.closeStatement(ps_Compiere);
			DB.closeStatement(sentencia);
		}
	
        As.desconectar();
		return r;
		
	}
		
	

}