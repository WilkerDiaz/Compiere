package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import org.compiere.model.MFactAcct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;
import compiere.model.payments.X_XX_VCN_AccoutingEntry;


/**
 * Se encarga de guardar en la tabla de asientos contables (XX_VCN_AccountingEntry), 
 * con sus correspondientes detalles, las cuentas por pagar que hayan sido aprobadas
 * @author Jessica Mendoza
 * 	
 * 		compiere.model.payments.processes.XX_ProcessTransferCX017
 *
 */
public class XX_ProcessTransferCX017 extends SvrProcess{
	
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_User_ID = Env.getCtx().getAD_User_ID();
	Utilities util = new Utilities();
	Calendar cal = Calendar.getInstance();
	X_XX_VCN_AccoutingEntry accountingEntry;
	MFactAcct detailFactA;
	String typeTransfer = "";
	String processType = "";
	int m_Warehouse_ID = 0;
	String Warehouse_Name = "";
	String typeTransferSp = "";
	int month; 
	int year;
	int line = 0;
	String option = "";
	StringBuffer m_sql = null;
	StringBuffer m_sqlElment = null;
	StringBuffer m_where = null;
	int numberOption = 0;
	BigDecimal debe = new BigDecimal(0);
	BigDecimal haber = new BigDecimal(0);
	HashMap<String, Vector<BigDecimal>> checkDiff = new HashMap<String, Vector<BigDecimal>>();
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)	
				;
			else if (name.equals("XX_ListCX017")){
				typeTransfer = element.getParameter().toString();
			}else if (name.equals("XX_ProcessType")){
				processType = element.getParameter().toString();				
			}else if (name.equals("XX_Options")){
				option = element.getParameter().toString();
			}else if (name.equals("XX_Warehouse_ID")){
				m_Warehouse_ID = ((BigDecimal)element.getParameter()).intValue();
								
			}
		}		
	}

	@Override
	protected String doIt() throws Exception {
		
		if (typeTransfer.equals("CA"))
			processAccountsPayableApprove(1,new Integer(option));
		else if (typeTransfer.equals("CE"))
			processAccountsPayableEstimated(2);

		return "Procesado";
	}
	
	/**
	 * Proceso que se encarga de cargar en los asientos 
	 * contables las cuentas por pagar aprobadas (de lunes a domingo)
	 * @param numTransfer número de la transferencia que se utiliza para 
	 * el número de comprobante o número de control
	 */
	public void processAccountsPayableApprove(int numTransfer, int option){
		//Recordar con los meses con Calendar comienzan en cero (enero = 0)
		int days = 0;
		Calendar dateInit = Calendar.getInstance();
		Calendar dateLast = Calendar.getInstance();
		Calendar temporalInit = Calendar.getInstance();
		Calendar temporalLast = Calendar.getInstance();
		
		/***Validaciones con respecto a las fechas de generación de los procesos***/
		if (processType.equals("I")){ //Mercancía
			if (option == 1){ //Mensual (30 de cada mes)

				//Se resta un dia, proque se corre el 1ero de c/mes
				dateLast.set(Calendar.DATE, dateLast.get(Calendar.DATE) - 1);
				dateInit.set(Calendar.DATE, dateInit.get(Calendar.DATE) - 1);
				
				days = numberDays(dateLast.get(Calendar.DAY_OF_WEEK));
				dateInit.set(Calendar.DATE, dateInit.get(Calendar.DATE) - days);
				
			}else if (option == 3){ //Semanal (lunes)
				//(-1) Porque el proceso corre los lunes y se quiere que se tome en cuenta los asientos desde 
				//lun-dom de la semana anterior
				if (dateLast.get(Calendar.DAY_OF_MONTH) == 1)
					dateInit.set(Calendar.DAY_OF_MONTH, 1);
				else{
					dateLast.set(Calendar.DATE, dateLast.get(Calendar.DATE) - 1); 
					if (dateLast.get(Calendar.DAY_OF_MONTH) < 7){
						dateInit.set(Calendar.DAY_OF_MONTH, 1);
					}else{
						days = 6;
						dateInit.set(Calendar.DATE, dateLast.get(Calendar.DATE) - days);
					}
				}			
			}
			
			buildHeadCxPA(new Timestamp(dateInit.getTimeInMillis()),new Timestamp(dateLast.getTimeInMillis()),numTransfer);
		
		}else{ //Bienes/Servicio
	
			if (option == 2){ //Diario (de lunes a viernes)
		
				int mes;
				int year;
				int lastDayMonth;
				
				//Si es el primero de del mes y es lunes
				if (dateInit.get(Calendar.DAY_OF_MONTH) == 1 && dateInit.get(Calendar.DAY_OF_WEEK) == 2){
		
					mes = dateInit.get(Calendar.MONTH);
					
					//proceso 1: se pasa el asiento del sábado y domingo
					if (mes == 0){
						mes = 11;
						year = dateInit.get(Calendar.YEAR) - 1;
					}else
						year = dateInit.get(Calendar.YEAR);						
					lastDayMonth = util.ultimoDiaMes(mes + 1);
					temporalLast.set(Calendar.DAY_OF_MONTH, lastDayMonth);
					temporalLast.set(Calendar.MONTH, mes);
					temporalLast.set(Calendar.YEAR, year);
					temporalInit.set(Calendar.DAY_OF_MONTH, lastDayMonth - 1);
					temporalInit.set(Calendar.MONTH, mes);
					temporalInit.set(Calendar.YEAR, year);
			
					buildHeadCxPA(new Timestamp(temporalInit.getTimeInMillis()),new Timestamp(temporalLast.getTimeInMillis()),
							numTransfer);
		
					//proceso 2: se pasa el asiento del lunes (01)
					buildHeadCxPA(new Timestamp(dateInit.getTimeInMillis()),new Timestamp(dateLast.getTimeInMillis()),
							numTransfer);
			
				}else if (dateInit.get(Calendar.DAY_OF_MONTH) == 2 && dateInit.get(Calendar.DAY_OF_WEEK) == 2){	

					mes = dateInit.get(Calendar.MONTH);
					
					//proceso 1: se pasa el asiento del sábado					
					if (mes == 0){	
						mes = 11;
						year = dateInit.get(Calendar.YEAR) - 1;
					}else
						year = dateInit.get(Calendar.YEAR);
					
					lastDayMonth = util.ultimoDiaMes(mes + 1);
					temporalInit.set(Calendar.DAY_OF_MONTH, lastDayMonth);
					temporalInit.set(Calendar.MONTH, mes);
					temporalInit.set(Calendar.YEAR, year);
					buildHeadCxPA(new Timestamp(temporalInit.getTimeInMillis()),new Timestamp(temporalInit.getTimeInMillis()),
							numTransfer);

					//proceso 2: se pasa el asiento del domingo y lunes (01-02)
					dateInit.set(Calendar.DAY_OF_MONTH, dateInit.get(Calendar.DAY_OF_MONTH) - 1);
					buildHeadCxPA(new Timestamp(dateInit.getTimeInMillis()),new Timestamp(dateLast.getTimeInMillis()),
							numTransfer);

				}else if (dateInit.get(Calendar.DAY_OF_WEEK) == 2){
	
					dateInit.set(Calendar.DAY_OF_MONTH, dateLast.get(Calendar.DAY_OF_MONTH) - 2);
					buildHeadCxPA(new Timestamp(dateInit.getTimeInMillis()),new Timestamp(dateLast.getTimeInMillis()),
							numTransfer);
				}else{
					buildHeadCxPA(new Timestamp(dateInit.getTimeInMillis()),new Timestamp(dateLast.getTimeInMillis()),
							numTransfer);
				}
			}
		}
	}
	
	/**
	 * Proceso que se encarga de cargar en los asientos 
	 * contables las cuentas por pagar estimadas
	 * @param numTransfer número de la transferencia que se utiliza para 
	 * el número de comprobante o número de control
	 */
	public void processAccountsPayableEstimated(int numTransfer){
		
		Calendar dateInit = Calendar.getInstance();
		Calendar dateLast = Calendar.getInstance();
		
		if(dateLast.get(2) == 0){
			dateLast.set(2, 11);
			dateLast.set(1, dateLast.get(1) - 1);
		}
		else{
			dateLast.set(2, dateLast.get(2) - 1);
		}
		
		dateInit.setTime(dateLast.getTime());
		
		dateInit.set(Calendar.DAY_OF_MONTH, 1);
		dateLast.set(Calendar.DAY_OF_MONTH, dateLast.getActualMaximum(Calendar.DAY_OF_MONTH));		
		
		buildHeadCxPE(new Timestamp(dateInit.getTimeInMillis()), new Timestamp(dateLast.getTimeInMillis()), numTransfer);
	}
	
	
	/**
	 * Se encarga de construir el query de la cabecera a partir de las opciones de 
	 * la agenda y el tipo de procesamiento
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param numTransfer número de la transferencia
	 */
	public void buildHeadCxPA(Timestamp dateFrom, Timestamp dateTo, int numTransfer){
		
		StringBuffer m_sql = new StringBuffer();
		StringBuffer m_where = new StringBuffer();
		String sql = "";
				
		if (processType.equals("I")){
			//Procesamiento 1: Mercancía Nacional
			m_sql.append("select sum(ROUND(NVL(pb.XX_TotalInvCost, 0) - NVL(pb.XX_WithholdingTax, 0),2)) " +
		     			 "from XX_VCN_PurchasesBook pb " +
		     			 "left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID) " + 
		     			 "left join C_Invoice nota ON (nota.C_Invoice_ID = pb.XX_DocumentNo_ID) " +  
						 "inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) "); 
			m_where.append("where pb.XX_Status='CO' AND pb.XX_ControlNumber is not null AND pb.XX_ControlNumber <> ' ' " +  
                           "and (inv.isSoTrx = 'N' or nota.isSoTrx = 'N') " +
                           "and (inv.XX_InvoiceType = 'I'  or nota.XX_InvoiceType = 'I') " + 
                           "and (inv.C_CURRENCY_ID = 205 or nota.C_CURRENCY_ID = 205) " + 
                           "and (inv.DocStatus = 'CO' or nota.DocStatus = 'CO')  " +
					       "and TRUNC(pb.created) between " +
			 		       "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
			 			    	",'yyyy-MM-dd') " + 
			 		       "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
			 			    	",'yyyy-MM-dd') " +
						   "and (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL) " +
						   "and (nota.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR nota.Description IS NULL) ");
			sql = m_sql.toString() + m_where.toString();
			executeHead(sql, dateFrom, dateTo, 1, numTransfer);;
		}else{
			
			dateFrom = dateTo;
			
			//Procesamiento 2: Suministro/Material y Contratos sin Arrendamiento Nacional
			m_sql = new StringBuffer();
			m_where = new StringBuffer();
			
			m_sql.append("select NVL(sum(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN (inv.totallines + inv.XX_TaxAmount) " +
								 "ELSE (nota.totallines + nota.XX_TaxAmount) END), 0) total ");
			
			m_where.append(
			"FROM XX_VCN_PurchasesBook pb " +
			"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') " +
			                         "AND (inv.C_Order_ID IS NOT NULL OR inv.XX_Contract_ID IS NOT NULL)) " +
			"left join C_Invoice nota ON (nota.C_Invoice_ID = pb.XX_DocumentNo_ID AND nota.XX_InvoiceType in ('A','S')) " +
			"left join C_Order ord ON (ord.C_Order_ID = pb.C_Order_ID) " +
			"left join XX_Contract con ON (con.XX_Contract_ID = inv.XX_Contract_ID) " +
			"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
			"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
			"left join C_DocType doc_nota on (nota.C_DocTypeTarget_ID = doc_nota.C_DocType_ID) " +
			"where pb.XX_Status='CO' and inv.isSoTrx = 'N'  AND pb.XX_ControlNumber <> ' ' and inv.DocStatus = 'CO' " +
			"AND (con.XX_Contract_ID IS NULL OR con.XX_Lease = 'N') " +
			"AND (ord.C_Order_ID IS NULL OR (ord.XX_PurchaseType = 'SU' OR ord.XX_PurchaseType = 'SE')) " +
			"AND TRUNC(pb.created) = to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
			"AND (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL)"
			);
			
			sql = m_sql.toString() + m_where.toString();	
			executeHead(sql, dateFrom, dateTo, 2, numTransfer);
			commit();
			
			//Procesamiento 3: Activo Fijo Nacional
			m_sql = new StringBuffer();
			m_sql.append("select coalesce(sum(inv.totalLines + inv.XX_TaxAmount),0) " +
				     "from C_Invoice inv " +
				     "left join C_Order ord ON (ord.C_Order_ID = inv.C_Order_ID) " +
				     "left join XX_Contract con ON (con.XX_Contract_ID = inv.XX_Contract_ID) ");
			
			m_where = new StringBuffer();
			m_where.append("where inv.isSoTrx = 'N' " +
					   "and inv.DocStatus = 'CO' " +
					   //"and inv.XX_SynchronizationAccount = 'N' " +
					   "and inv.XX_ApprovalDate is not null ");
			
			m_where.append("and ord.XX_InvoicingStatus = 'AP' " +
					   "and inv.XX_InvoiceType = '" + processType + "' " +
					   "and ord.XX_OrderType = 'Nacional' " +
				       "and inv.XX_ApprovalDate between " +
		 		       "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
		 			    	",'yyyy-MM-dd') " + 
		 		       "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
		 			    	",'yyyy-MM-dd') ");
			
			m_where.append("and (ord.XX_OrderType = 'Nacional' or con.XX_ContractType = 'Nacional') " +
						   "and ord.XX_POType = 'POA' " +
						   "and inv.XX_ApprovalDate between " +
			 		       "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
			 			   	",'yyyy-MM-dd') " + 
			 		       "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
			 			   	",'yyyy-MM-dd') " +
						   "and ord.XX_PurchaseType = 'FA' ");
			sql = m_sql.toString() + m_where.toString();
			executeHead(sql, dateFrom, dateTo, 3, numTransfer);
			
			m_where = new StringBuffer();
			m_where.append("where inv.isSoTrx = 'N' " +
					   "and inv.DocStatus = 'CO' " +
					   //"and inv.XX_SynchronizationAccount = 'N' " +
					   "and inv.XX_ApprovalDate is not null ");
			m_where.append("and (inv.XX_InvoicingStatusContract = 'AP' or ord.XX_InvoicingStatus = 'AP') " +
				       "and inv.XX_ApprovalDate between  " +
	 		           "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
	 		     	 		",'yyyy-MM-dd') " +
	 		     	   "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
		 			    	",'yyyy-MM-dd') ");	
			
			//Procesamiento 4: Servicios Importados
			m_where.append("and (ord.XX_OrderType = 'Importada' or con.XX_ContractType = 'Importada') " +
						   "and inv.XX_ApprovalDate between " +
		 		           "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
		 			    	",'yyyy-MM-dd') " + 
		 		            "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
		 			    	",'yyyy-MM-dd') " +
							"and inv.XX_InvoiceType = 'S' ");
			sql = m_sql.toString() + m_where.toString();
			executeHead(sql, dateFrom, dateTo, 4, numTransfer);
			
			m_where = new StringBuffer();
			m_where.append("where inv.isSoTrx = 'N' " +
					   "and inv.DocStatus = 'CO' " +
					   //"and inv.XX_SynchronizationAccount = 'N' " +
					   "and inv.XX_ApprovalDate is not null ");
			m_where.append("and (inv.XX_InvoicingStatusContract = 'AP' or ord.XX_InvoicingStatus = 'AP') " +
				       "and inv.XX_ApprovalDate between  " +
	 		           "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
	 		     	 		",'yyyy-MM-dd') " +
	 		     	   "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
		 			    	",'yyyy-MM-dd') ");	
			
			//Procesamiento 5: Arrendamiento Nacional
			m_sql.append("inner join XX_CONTRACT con on (inv.XX_CONTRACT_Id = con.XX_CONTRACT_Id) ");
			m_where = new StringBuffer();
			m_where.append("where inv.isSoTrx = 'N' " +
					   "and inv.DocStatus = 'CO' " +
					    "and inv.XX_ApprovalDate is not null ");
			m_where.append("and con.XX_ContractType = 'Nacional' " +
						   "and inv.XX_InvoiceType = 'S' " +
						   "and inv.XX_ApprovalDate between " +
			 		       "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
			 			    	",'yyyy-MM-dd') " + 
			 		       "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
			 			    	",'yyyy-MM-dd') " +
						   "and con.XX_Lease = 'Y' ");
			sql = m_sql.toString() + m_where.toString();
			executeHead(sql, dateFrom, dateTo, 5, numTransfer);
			
			m_where = new StringBuffer();
			m_where.append("where inv.isSoTrx = 'N' " +
					   "and inv.DocStatus = 'CO' " +
					   //"and inv.XX_SynchronizationAccount = 'N' " +
					   "and inv.XX_ApprovalDate is not null ");
			m_where.append("and (inv.XX_InvoicingStatusContract = 'AP' or ord.XX_InvoicingStatus = 'AP') " +
				       "and inv.XX_ApprovalDate between  " +
	 		           "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
	 		     	 		",'yyyy-MM-dd') " +
	 		     	   "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
		 			    	",'yyyy-MM-dd') ");	
			
			//Procesamiento 6: Activo Fijo Importado
			m_where.append("and (ord.XX_OrderType = 'Importada' or con.XX_ContractType = 'Importada') " +
						   "and ord.XX_POType = 'POA' " +
						   "and inv.XX_ApprovalDate between " +
			 		       "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "'" +
			 			   	",'yyyy-MM-dd') " + 
			 		       "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "'" +
			 			   ",'yyyy-MM-dd') " +
						   "and ord.XX_PurchaseType = 'FA' ");
			sql = m_sql.toString() + m_where.toString();
			executeHead(sql, dateFrom, dateTo, 6, numTransfer);
			
			
			//Procesamiento 7: Suministro/Material sin OC ni Contratos
			m_sql = new StringBuffer("SELECT coalesce(sum(inv.totalLines + inv.XX_TaxAmount),0)");
			
			m_where = new StringBuffer(
					"FROM XX_VCN_PurchasesBook pb " +
					"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') " +
												  "AND (inv.C_Order_ID IS NULL AND inv.XX_Contract_ID IS NULL)) " +
					"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
					"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " + 
					"where pb.XX_Status='CO' and inv.isSoTrx = 'N' AND pb.XX_ControlNumber <> ' ' and inv.DocStatus = 'CO' " +
					"AND TRUNC(pb.created) = to_date('"+ new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) +"','yyyy-MM-dd') " +
					"AND C_DocTypeTarget_ID NOT IN ("+ Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTNEG_ID") +", "+ Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTPOS_ID") +") " +
					"AND (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL)"
					);
			
			sql = m_sql.toString() + m_where.toString();
			executeHead(sql, dateFrom, dateTo, 7, numTransfer);
		}		
	}
	
	
	BigDecimal rateCxPE = BigDecimal.ZERO;
	boolean flag = true;
	
	/**
	 * Se encarga de construir el query de la cabecera a partir de las opciones de 
	 * la agenda y el tipo de procesamiento
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param numTransfer número de la transferencia
	 */
	public void buildHeadCxPE(Timestamp dateFrom, Timestamp dateTo, int numTransfer){
		
		m_sql = new StringBuffer();
		StringBuffer m_sql_update = new StringBuffer();
		m_where = new StringBuffer();
		String sql = "";
		
		rateCxPE = rateDollar(dateTo);
		
		//Si hay cambio de dolar //IMPORTADAS
		if(rateCxPE.compareTo(BigDecimal.ZERO)>0){
				
			//Procesamiento 1 O/C Importadas
			processType = "I";
			
			m_sql.append("select NVL(sum(totallines), 0) * ("+rateCxPE+") MONTO " +
				 	     "from c_order " + 
				 	     "where isSOTrx = 'N' AND XX_POType = 'POM' AND XX_OrderType = 'Importada' " +
						 "AND XX_OrderStatus NOT IN ('PRO', 'AN', 'RE', 'CH', 'SIT') AND XX_InvoicingStatus <> 'AP' " +
						 "AND XX_SyncAccounting = 'N' " +
						 "AND CREATED between " +
						 	"to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " + 
				 			"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd')" );
								
			sql = m_sql.toString();
			
			executeHead(sql, dateFrom, dateTo, 1, numTransfer);
			
			if(flag){
				
				m_sql_update = new StringBuffer();
				m_sql_update.append("select c_order_id " +
				 	     "from c_order " + 
				 	     "where isSOTrx = 'N' AND XX_POType = 'POM' AND XX_OrderType = 'Importada' " +
						 "AND XX_OrderStatus NOT IN ('PRO', 'AN', 'RE', 'CH', 'SIT') AND XX_InvoicingStatus <> 'AP' " +
						 "AND XX_SyncAccounting = 'N' " +
						 "AND CREATED between " +
						 	"to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " + 
				 			"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
				 		 "group by c_order_id " );
				
				sincronizeOrder(m_sql_update.toString());
			}
			else
				return;
	
			
			//Procesamiento 3 Servicios Importados
			processType = "I";
			
			m_sql = new StringBuffer();
			m_sql.append("select NVL(totallines,0) * ("+rateCxPE+"), c_order_id from c_order a " +
						 "where a.isSOTrx = 'N' AND a.XX_POType = 'POA' " +
						 "AND a.XX_OrderStatus NOT IN ('PRO', 'AN') AND a.XX_PurchaseType = 'SE' " +
						 "AND XX_InvoicingStatus <> 'AP' " +
						 "AND XX_SyncAccounting = 'N' AND XX_OrderType = 'Importada'" +
						 "AND CREATED between " +
						 	"to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " + 
				 			"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd')" );
									
			sql = m_sql.toString();
							
			executeHead(sql, dateFrom, dateTo, 3, numTransfer);
			
			if(flag){
				
				m_sql_update = new StringBuffer();
				m_sql_update.append("select c_order_id from c_order a " +
						 			"where a.isSOTrx = 'N' AND a.XX_POType = 'POA' " +
									 "AND a.XX_OrderStatus NOT IN ('PRO', 'AN') AND a.XX_PurchaseType = 'SE' " +
									 "AND XX_InvoicingStatus <> 'AP' " +
									 "AND XX_SyncAccounting = 'N' AND XX_OrderType = 'Importada'" +
									 "AND CREATED between " +
									 	"to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " + 
							 			"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd')" );
				
				sincronizeOrder(m_sql_update.toString());
			}
			else
				return;
		}
		
		//NACIONALES
		rateCxPE = BigDecimal.ONE;
		
		//Procesamiento 2 O/C Bienes y Servicios nacionales
		processType = "I";
								
		m_sql = new StringBuffer();
		m_sql.append("select totallines, c_order_id from c_order a " +
					 "where a.isSOTrx = 'N' AND a.XX_POType = 'POA' " +
					 "AND ( " +
					 	"(a.XX_OrderStatus NOT IN ('PRO', 'AN', 'RE', 'CH') AND a.XX_PurchaseType = 'SU') OR " +
					 	"(a.XX_OrderStatus NOT IN ('PRO', 'AN') AND a.XX_PurchaseType = 'SE') " +
					 ") " +
					 "AND XX_InvoicingStatus <> 'AP' " +
					 "AND XX_SyncAccounting = 'N' AND XX_OrderType = 'Nacional'" +
					 "AND CREATED between " +
					   "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " + 
					   "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd')" );
													
		sql = m_sql.toString();
								
		executeHead(sql, dateFrom, dateTo, 2, numTransfer);
		
		if(flag){
			
			m_sql_update = new StringBuffer();
			m_sql_update.append("select c_order_id from c_order a " +
								"where a.isSOTrx = 'N' AND a.XX_POType = 'POA' " +
								"AND ( " +
								 	"(a.XX_OrderStatus NOT IN ('PRO', 'AN', 'RE', 'CH') AND a.XX_PurchaseType = 'SU') OR " +
								 	"(a.XX_OrderStatus NOT IN ('PRO', 'AN') AND a.XX_PurchaseType = 'SE') " +
								") " +
								"AND XX_InvoicingStatus <> 'AP' " +
								"AND XX_SyncAccounting = 'N' AND XX_OrderType = 'Nacional'" +
								"AND CREATED between " +
								   "to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " + 
								   "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd')" );
			
			sincronizeOrder(m_sql_update.toString());
		}
		else
			return;
		
		
		//Procesamiento 4 Contratos
		processType = "I";
										
		m_sql = new StringBuffer();
		m_sql.append("select " +
					 "SUM(CASE WHEN XX_CONTRACTTYPE = 'Nacional' THEN XX_AMOUNTLOCAL " +
					 "ELSE XX_AMOUNTFOREIGN * (select sum(XX_RATE) from XX_VCN_PaymentDollars " +
					 "where XX_RATETYPE = 'S' AND XX_RATETYPESITME IS NULL " + 
					 "AND TO_CHAR(DATE1) = TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') and rownum=1) " +
					 "END) totallines, b.XX_Contract_ID " +
					 "from XX_VCN_ESTIMATEDAPAYABLE a, XX_Contract b " +
					 "where a.XX_CONTRACT_ID is not null AND a.XX_Contract_ID = b.XX_Contract_ID " +
				
					 "AND XX_DATEESTIMATED between to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " +
					 "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd')");
	
		m_sql.append(" group by b.XX_Contract_ID");
		
		sql = m_sql.toString();
										
		executeHead(sql, dateFrom, dateTo, 4, numTransfer);
		
	}
	

	/**
	 * Este metodo se encarga de colocar las O/C procesadas como sincronizadas con contabilidad
	 * @param sql
	 */
	public void sincronizeOrder(String sql){
		
		String update = "";
		
		update = "UPDATE C_ORDER SET XX_SyncAccounting = 'Y' WHERE C_ORDER_ID IN ("
				 + sql +")";
		
		int result = DB.executeUpdate(get_Trx(), update);
		
		if(result<0)
			flag=false;
	}
	
	
	/**
	 * Se encarga de generar la cabecera de los asientos contables, y almacenarlos en su tabla correspondiente 
	 * @param sql
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param process tipo de procesamiento
	 * @param numTransfer número de la transferencia
	 */
	public void executeHead(String sql, Timestamp dateFrom, Timestamp dateTo, int process, int numTransfer){
		Boolean result = true;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			//verifica el process 7 el sql
			if (process == 7)
			{
				m_where = new StringBuffer();
				m_where.append(" and inv.XX_Contract_ID is null and inv.C_Order_ID is null");
				sql = sql + m_where.toString();
			}	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {

				if (rs.getBigDecimal(1).compareTo(new BigDecimal(0)) != 0 || (numTransfer == 3 && rs.getBigDecimal(2).compareTo(new BigDecimal(0)) != 0)){
					
					accountingEntry = new X_XX_VCN_AccoutingEntry(Env.getCtx(), 0, get_Trx());
					accountingEntry.setAD_Client_ID(m_AD_Client_ID);
					accountingEntry.setAD_User_ID(m_AD_User_ID);
					accountingEntry.setDateFrom(dateFrom);
					accountingEntry.setDateTo(dateTo);
					accountingEntry.setDateTrx(new Timestamp(Calendar.getInstance().getTimeInMillis()));					
					accountingEntry.setDescription("COMPROBANTE DIARIO");		
						
					//Buscar número de comprobante
					String numberControl = String.valueOf(numTransfer) + "0" + process + String.valueOf(numberControl());
					accountingEntry.setXX_ControlNumber(numberControl);
					accountingEntry.setXX_ListCX017(typeTransfer);
					
					if (numTransfer == 1 || numTransfer == 2){
						accountingEntry.setXX_ProcessType(processType);
					}else{		
						if(!typeTransferSp.endsWith("ANNUAL")){
							accountingEntry.setXX_TypeTransferSP(typeTransferSp.substring(0,1)); 
						}
						accountingEntry.setM_Warehouse_ID(m_Warehouse_ID);
						accountingEntry.setXX_Office(getWarehouse(m_Warehouse_ID));
					}
					
					if ((process == 4 && numTransfer!=2) || (process == 6)){
						accountingEntry.setXX_TotalShould(rs.getBigDecimal(1).multiply(averageDollar(dateTo)));
						accountingEntry.setXX_TotalHave(rs.getBigDecimal(1).multiply(averageDollar(dateTo)));
					}else{			
						
						if(numTransfer == 3){
							accountingEntry.setXX_TotalShould(rs.getBigDecimal(1).abs());
							accountingEntry.setXX_TotalHave(rs.getBigDecimal(2).abs());
						}else{
							accountingEntry.setXX_TotalShould(rs.getBigDecimal(1));
							accountingEntry.setXX_TotalHave(rs.getBigDecimal(1));
						}					
					}

					accountingEntry.save();
					
					if (numTransfer == 1){ //Cuentas por Pagar Aprobadas
						//Crear líneas asociadas a los procesamientos
						if (process == 1)
							result = buildDetailOneCxPA(dateFrom, dateTo, accountingEntry.getXX_VCN_AccoutingEntry_ID());
						else if (process == 2)
							result = buildDetailTwoCxPA(dateFrom, dateTo, accountingEntry.getXX_VCN_AccoutingEntry_ID());
						else if (process == 3)
							result = buildDetailThreeCxPA(dateFrom, dateTo, accountingEntry.getXX_VCN_AccoutingEntry_ID());
						else if (process == 4)
							result = buildDetailFourCxPA(dateFrom, dateTo, accountingEntry.getXX_VCN_AccoutingEntry_ID());
						else if (process == 5)
							result = buildDetailFiveCxPA(dateFrom, dateTo, accountingEntry.getXX_VCN_AccoutingEntry_ID());
						else if (process == 6)
							result = buildDetailSixCxPA(dateFrom, dateTo, accountingEntry.getXX_VCN_AccoutingEntry_ID());
						else if (process == 7)
							result = buildDetailSevenCxPA(dateFrom, dateTo, accountingEntry.getXX_VCN_AccoutingEntry_ID());
					
						
					}else if (numTransfer == 2){ //Cuentas por Pagar Estimadas 
						
						if (process == 1)
							result = buildDetailCxPE_P1(dateFrom, dateTo, accountingEntry);
						else if (process == 2)
							result = buildDetailCxPE_P2P3(process, dateFrom, dateTo, accountingEntry, rs.getInt("C_ORDER_ID"));
						else if (process == 3)
							result = buildDetailCxPE_P2P3(process, dateFrom, dateTo, accountingEntry, rs.getInt("C_ORDER_ID"));
						else if (process == 4)
							result = buildDetailCxPE_P4(dateFrom, dateTo, accountingEntry, rs.getInt("XX_CONTRACT_ID"));
					}
					
					if (!result){
						rollback();
						flag = false;
					}
				}
			}
			rs.close();
			pstmt.close();
		}catch(SQLException e){	
			e.getMessage();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	/**
	 * Procesamiento para los asientos de mercancía nacional
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param headID identificador de la cabecera
	 */
	public Boolean buildDetailOneCxPA(Timestamp dateFrom, Timestamp dateTo, int headID){
		
		int linea = 1;
		int linea2 = 1;
		String sql = "";
		String description = "";
		GregorianCalendar calFrom = new GregorianCalendar();
		calFrom.setTime(dateFrom);
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal debe = new BigDecimal(0);
		BigDecimal haber = new BigDecimal(0);
		BigDecimal debeTotal = BigDecimal.ZERO;
		BigDecimal haberTotal = BigDecimal.ZERO;

		while (linea < 5){
			haber = new BigDecimal(0);
			debe = new BigDecimal(0);
			m_sql = new StringBuffer();			
			if (linea == 1){
				
				m_sql.append("SELECT ROUND(sum(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.totalLines ELSE nota.totalLines END),2) as subTotal");
				
			}else if (linea != 4){
				
				m_sql.append("select round((NVL(pb.XX_TaxAmount, 0)), 2) as impuesto, round(NVL(pb.XX_withholdingtax, 0), 2) as IVA, " +
							 "ROUND(NVL(pb.XX_TotalInvCost, 0) - NVL(pb.XX_WithholdingTax, 0),2) as total, " +
							 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN doc.XX_CodDoc ELSE doc_nota.XX_CodDoc END) XX_CodDoc, " +
							 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN  to_char(inv.XX_DueDate,'dd/MM/yyyy') else to_char(nota.XX_DueDate,'dd/MM/yyyy') END) fve, " +
                             "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.DateInvoiced,'dd/MM/yyyy') ELSE to_char(nota.DateInvoiced,'dd/MM/yyyy') END) ffac,  " +
							 "substr(upper(par.name),1,40) name, " +
							 "pb.AD_Org_ID, par.C_BPartner_ID, " +
							 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN pb.C_Invoice_ID ELSE pb.XX_DocumentNo_ID END) C_Invoice_ID, " +										  
							 "(CASE WHEN inv.XX_ApprovalDate IS NULL THEN to_char(inv.CREATED,'dd/MM/yyyy') ELSE to_char(inv.XX_ApprovalDate,'dd/MM/yyyy') END) fdo ");
							
			}else{
				m_sql.append("SELECT sum(CASE WHEN pb.XX_DocumentNo_ID IS NULL " +
							 "THEN (ROUND(NVL(inv.totalLines, 0), 2) + ROUND(NVL(inv.XX_TaxAmount, 0), 2) - ROUND(NVL(pb.XX_WithHoldingTax, 0), 2))" +
							 "ELSE (ROUND(NVL(nota.totalLines, 0), 2) + ROUND(NVL(nota.XX_TaxAmount,0 ), 2) - ROUND(NVL(pb.XX_WithHoldingTax, 0), 2)) END) as TOTAL" );
			}
			
			m_sql.append(
					
					" from XX_VCN_PurchasesBook pb " +
					"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID) " +
					"left join C_Invoice nota ON (nota.C_Invoice_ID = pb.XX_DocumentNo_ID) " +
					//"left join C_Order ord ON (ord.C_Order_ID = pb.C_Order_ID and ord.XX_InvoicingStatus = 'AP') " +
					"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
					"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
					"left join C_DocType doc_nota on (nota.C_DocTypeTarget_ID = doc_nota.C_DocType_ID) " +
					"where pb.XX_Status='CO' and (inv.isSoTrx = 'N' or nota.isSoTrx = 'N') and (inv.XX_InvoiceType = 'I'  or nota.XX_InvoiceType = 'I') " +
					"AND pb.XX_ControlNumber is not null AND pb.XX_ControlNumber <> ' ' and (inv.C_CURRENCY_ID = 205 or nota.C_CURRENCY_ID = 205) " +
					"and (inv.DocStatus = 'CO' or nota.DocStatus = 'CO')  " +
					"AND TRUNC(pb.created) between to_date('" +
 						new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime())+ "','yyyy-MM-dd') " +
 							"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
					"and (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL) " +
					"and (nota.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR nota.Description IS NULL)"
					);
			
			sql = m_sql.toString();
			StringBuffer m_sqlElmentUnion = new StringBuffer();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();	
		
				while (rs.next()){	
					
					haber = new BigDecimal(0);
					debe = new BigDecimal(0);
					m_sqlElment = new StringBuffer();		
					m_sqlElment.append("select C_ElementValue_ID, value, " +
							   "(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
							   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
							   " else null " +
							   " end) div, " +
							   "(case when XX_Sec = 'Y' then (select XX_Div from AD_ClientInfo" +
							   		" where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
							   " else null " +
							   " end) sec, " +
							   "(select TO_CHAR(XX_Suc) from AD_Org where XX_AccPrincipal = 'Y') suc, ");
					
					
					if (linea == 2 || linea == 3){
						
						m_sqlElment.append("(case when XX_Dep = 'Y' then " +
										   "(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt("AD_Org_ID") + ") " +
										   " else null " +
										   " end) dep, " +						   
										   "(case when XX_Aux = 'Y' then " +
										   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
										   " else null " +
										   " end) aux, " +
										   "(case when XX_FEV = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, ");
										if  (linea == 3)
											m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo") + "' ");
										if (linea == 2 )	   
											m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("ffac") + "' " ) ;
										m_sqlElment.append(" else null end) fdo, " );
					}else if (linea == 4){
						m_sqlElment.append("null dep, null aux, " +
										   "(case when XX_Fdo = 'Y' then '" + 
										   		new SimpleDateFormat("dd/MM/yyyy").format(dateTo.getTime()) + "' " +
										   " else null" +
										   " end) fdo, " +
										   "(case when XX_FEV = 'Y' then '01/01/0001' " +
										   " else null" +
										   " end) fve, ");
					}else{
						m_sqlElment.append("null dep, null aux, null fdo, ");
					}
										
					if (linea == 1){
						m_sqlElment.append("null doc, null tipDoc " +
										   "from C_ElementValue " +
										   "where M_Product_Category_ID = " + 
										   		Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID") + " " +
										   "and XX_ElementType = 'Nacional' " +
										   "and AccountType = 'L' " +
										   "and XX_Transitional = 'Y' ");
						
						description = "COMPRAS SEM. D/" + new SimpleDateFormat("dd").format(dateFrom.getTime()) +
											"-" + new SimpleDateFormat("MM").format(dateFrom.getTime()) + 
											" H/" + new SimpleDateFormat("dd").format(dateTo.getTime()) +
											"-" + new SimpleDateFormat("MM").format(dateTo.getTime()) + 
											"-" + new SimpleDateFormat("yy").format(dateTo.getTime());		
						if(rs.getBigDecimal("subTotal").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("subTotal");
						} else
							haber = rs.getBigDecimal("subTotal").multiply(BigDecimal.valueOf(-1));
					}else if (linea == 2) {
						m_sqlElment.append("(case when XX_Doc = 'Y' then " +
										   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
										   		rs.getInt("C_Invoice_ID") + ") " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue " +
										   "where AccountType = 'L' " +
										   "and XX_Tax = 'Y' ");
						description = rs.getString("name");
						if(rs.getBigDecimal("impuesto").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("impuesto");
						} else
							haber = rs.getBigDecimal("impuesto").multiply(BigDecimal.valueOf(-1));
						if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)
							linea2++;
					}else if (linea == 3){
						m_sqlElment.append("(case when XX_Doc = 'Y' then XX_WithHolding " +
										   " else null " + 
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then 'RET' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue a, XX_VCN_PurchasesBook b ");
						
						m_sqlElmentUnion = new StringBuffer();
						m_sqlElmentUnion.append(" UNION ");
						m_sqlElmentUnion.append(m_sqlElment);
										   
						m_sqlElment.append("inner join C_Invoice inv ON (inv.C_INVOICE_ID = b.C_INVOICE_ID) "+
										  "where b.XX_Status='CO' and AccountType = 'L' " +
										   "and XX_WithholdingTaxMarck = 'Y' AND XX_WITHHOLDING IS NOT NULL AND XX_DOCUMENTNO_ID IS NULL " +
										   "and (TRUNC(inv.XX_ApprovalDate,'DDD') between to_date('" + 
										   new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
										   "','yyyy-MM-dd') " +
										   "and to_date('" + 
								   new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
										   "','yyyy-MM-dd')) " + 
										   "AND inv.C_Invoice_ID = " + rs.getInt("C_Invoice_ID"));
						
						m_sqlElmentUnion.append(
								   "inner join C_invoice cred ON (cred.C_INVOICE_ID = b.XX_DOCUMENTNO_ID) "+
								   "where b.XX_Status='CO' and AccountType = 'L' " +
								   "and XX_WithholdingTaxMarck = 'Y' AND XX_WITHHOLDING IS NOT NULL AND XX_DOCUMENTNO_ID IS NOT NULL " +
								   "and (TRUNC(b.created,'DDD') between to_date('" + 
								   new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
										   "','yyyy-MM-dd') " +
										   "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
										   "','yyyy-MM-dd')) and cred.XX_ControlNumber is not null and " +
										   "b.XX_DOCUMENTNO_ID = " + rs.getInt("C_Invoice_ID"));
						
						m_sqlElment.append(m_sqlElmentUnion);
						
						description = rs.getString("name");
						
						if(rs.getBigDecimal("IVA").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("IVA");
						} else
							debe = rs.getBigDecimal("IVA").multiply(BigDecimal.valueOf(-1));
						if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)	
							linea2++;
						//}
					}else{
						m_sqlElment.append("(case when XX_Doc = 'Y' then '1' " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then 'AST' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue " +
										   "where M_Product_Category_ID = " + 
							   					Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID") + " " +
									   	   "and AccountType = 'L' " +
					   	   				   "and XX_ElementType = 'Nacional' " +
					   	   				   "and XX_Transitional <> 'Y' ");
						description = "COMPRAS SEM. D/" + new SimpleDateFormat("dd").format(dateFrom.getTime()) +
									  "-" + new SimpleDateFormat("MM").format(dateFrom.getTime()) + 
									  " H/" + new SimpleDateFormat("dd").format(dateTo.getTime()) +
									  "-" + new SimpleDateFormat("MM").format(dateTo.getTime()) + 
									  "-" + new SimpleDateFormat("yy").format(dateTo.getTime());
						if(rs.getBigDecimal("total").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("total");
						} else
							debe = rs.getBigDecimal("total").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					
					if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0){
						if (!executeDetailCxPA(rs, m_sqlElment.toString(), linea2, headID, description, debe, haber, linea,true))
							return false;
						else{
							debeTotal = debeTotal.add(debe);
							haberTotal = haberTotal.add(haber);
						}
					}
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			aEnt.setXX_TotalHave(haberTotal);
			aEnt.setXX_TotalShould(debeTotal);
			aEnt.save();
			
			linea++;
		}
		return true;
	}
	
	//Detalle al cual se le cargaran los decimales con diferencia
	int detailDiff = 0;
	
	/**
	 * Procesamiento para los asientos de suministros/materiales y servicios 
	 * sin tomasr en cuenta los arrendamientos nacional
	 * @param dateFrom fecha desde 
	 * @param dateTo fecha hasta
	 * @param headID identificador de la cabecera
	 */
	public Boolean buildDetailTwoCxPA(Timestamp dateFrom, Timestamp dateTo, int headID){
		int linea = 1;
		int linea2 = 0;
		String sql = "";
		String description = "";
		GregorianCalendar calFrom = new GregorianCalendar();
		calFrom.setTime(dateFrom);
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal debe = new BigDecimal(0);
		BigDecimal haber = new BigDecimal(0);
		BigDecimal debeTotal = BigDecimal.ZERO;
		BigDecimal haberTotal = BigDecimal.ZERO;
		BigDecimal diff = BigDecimal.ZERO;
		detailDiff = 0;
		
		while (linea < 6){
			haber = new BigDecimal(0);
			debe = new BigDecimal(0);
			m_sql = new StringBuffer();
			if (linea == 1){

				m_sql.append(
						"SELECT " +
						"sum( " +
						        "ROUND((CASE WHEN inv.XX_Contract_ID IS NULL THEN " +
						                "((CASE " +
						                "WHEN PPDA.XX_AMOUNTPERCC IS NOT NULL " +
						                "THEN ((PPDA.XX_AMOUNTPERCC*100)/(ili.lineNetAmt)) " +
						                "ELSE 100 END)/100) * " +
						                "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ili.lineNetAmt ELSE iliN.lineNetAmt END) " +
						        "ELSE " +
						                 "((ppdS.XX_PERCENAMOUNT /100)) * " +
						                 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ili.lineNetAmt " +
							         "ELSE iliN.lineNetAmt END) " +
						        "END),2) " +      
						") SUBTOTAL, " + 
						"(CASE WHEN pac.XX_PInventory_ID IS NULL THEN elvB.C_ElementValue_ID ELSE elvA.C_ElementValue_ID END) account, " +
						"(CASE WHEN inv.XX_Contract_ID IS NULL THEN ppdA.XX_Org_ID ELSE ppdS.XX_COSTCENTER_ID END) suc, " +
						"inv.XX_InvoiceType, par.C_BPartner_ID, substr(par.name,0,40) name, inv.XX_Month || inv.XX_Year ma, " +
						"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID ELSE nota.C_Invoice_ID END) C_INVOICE_ID, " +
						"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Currency_ID ELSE nota.C_Currency_ID END) C_CURRENCY_ID, " +
						"doc.XX_CodDoc, to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo2, to_char(inv.XX_DueDate,'dd/MM/yyyy') fve " +
						"FROM XX_VCN_PurchasesBook pb " +
						"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') " +
                        "AND (inv.C_Order_ID IS NOT NULL OR inv.XX_Contract_ID IS NOT NULL)) " +
                        "left join C_Invoice nota ON (nota.C_Invoice_ID = pb.XX_DocumentNo_ID AND nota.XX_InvoiceType in ('A','S')) " +
						"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
						"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
						"left join C_DocType doc_nota on (nota.C_DocTypeTarget_ID = doc_nota.C_DocType_ID) " +
						"inner join C_InvoiceLine ili on (inv.C_Invoice_ID = ili.C_Invoice_ID) " +
						"left join C_InvoiceLine iliN on (nota.C_Invoice_ID = iliN.C_Invoice_ID AND ili.M_Product_ID = iliN.M_Product_ID) " +
						"left join XX_ProductPercentDistrib ppdA ON (ppdA.C_InvoiceLine_ID = ili.C_InvoiceLine_ID) " +
						"left join XX_ContractDetail ppdS ON (inv.XX_PayContract_ID = ppdS.XX_PayContract_ID) " +
						"left  join M_Product_Acct pac on (ili.M_Product_ID = pac.M_Product_ID) " +
						"left join C_ElementValue elvA on (pac.XX_PInventory_ID = elvA.C_ElementValue_ID) " +
						"left join C_ElementValue elvB on (pac.XX_PExpenses_ID = elvB.C_ElementValue_ID) " +
						"left join C_Order ord ON (ord.C_Order_ID = pb.C_Order_ID) " +
						"left join XX_Contract con ON (con.XX_Contract_ID = inv.XX_Contract_ID) " +
						"where pb.XX_Status='CO' and inv.isSoTrx = 'N' and inv.XX_InvoiceType in ('A','S') AND pb.XX_ControlNumber <> ' ' " +
						"AND inv.DocStatus = 'CO' " +
						"AND (con.XX_Contract_ID IS NULL OR con.XX_Lease = 'N') " +
						"AND (ord.C_Order_ID IS NULL OR (ord.XX_PurchaseType = 'SU' OR ord.XX_PurchaseType = 'SE')) " +
						"AND TRUNC(pb.created) = to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
						"and (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL) " +
						"and pb.C_Order_ID IS not NULL " +
						"GROUP BY inv.DOCUMENTNO, nota.DOCUMENTNO, inv.XX_InvoiceType, par.C_BPartner_ID, par.name, doc.XX_CodDoc, inv.DateInvoiced, " +
						"elvA.C_ElementValue_ID, pac.XX_PInventory_ID, inv.XX_Contract_ID, ppdA.XX_Org_ID, ppdS.XX_COSTCENTER_ID, " +
						"inv.XX_DueDate, inv.XX_Month, inv.XX_Year, inv.C_Invoice_ID, nota.C_Invoice_ID, inv.C_Currency_ID, nota.C_Currency_ID, pb.XX_DocumentNo_ID, elvB.C_ElementValue_ID " +
						"HAVING " +
						"sum( " +
						        "(CASE WHEN inv.XX_Contract_ID IS NULL THEN " +
						                "((CASE " +
						                "WHEN PPDA.XX_AMOUNTPERCC IS NOT NULL THEN ((PPDA.XX_AMOUNTPERCC*100)/(ili.XX_PriceEnteredInvoice*ili.qtyEntered)) " +
						                "ELSE 100 END)/100) * " +
						                "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN (ili.lineNetAmt) ELSE iliN.lineNetAmt END) " +
						        "ELSE " +
						                 "((ROUND(ppdS.XX_PERCENAMOUNT, 2) /100)) * " +
						                 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ROUND(inv.totallines, 2) " +
							         "ELSE ROUND(nota.totallines, 2) END) " +
						        "END) " +       
						") <> 0 " +
						"ORDER BY par.NAME");
				
			}else{
				if (linea != 5){
					
					m_sql.append(
							"SELECT " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ROUND(inv.XX_TaxAmount, 2)  ELSE ROUND(nota.XX_TaxAmount, 2) END) as impuesto, " +
							"ROUND(pb.XX_WithHoldingTax, 2) as IVA, " +
							"NVL((select sum(ROUND(NVL(a.XX_RetainedAmount, 0), 2)) from XX_VCN_ISLRAMOUNT a where a.C_Invoice_ID = " +
							        "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID  ELSE nota.C_Invoice_ID END)),0) as ISLR, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN doc.XX_CodDoc  ELSE  doc_nota.XX_CodDoc  END) XX_CodDoc, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.AD_Org_ID  ELSE nota.AD_Org_ID END) AD_Org_ID, " +
							"substr(upper(par.name),1,40) name, " +
							"par.C_BPartner_ID, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID  ELSE nota.C_Invoice_ID END) C_Invoice_ID, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Currency_ID ELSE nota.C_Currency_ID END) C_CURRENCY_ID, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.XX_DueDate,'dd/MM/yyyy') " +
							"ELSE to_char(inv.XX_DueDate,'dd/MM/yyyy') END) fve, " + 
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.DateInvoiced,'dd/MM/yyyy') " +
							"ELSE to_char(inv.DateInvoiced,'dd/MM/yyyy') END) fdo2, " +
							"to_char(inv.XX_ApprovalDate,'dd/MM/yyyy') fdo34 "
					);
					
				}else{
					m_sql.append(
							"select " +
							"( " +
							    "(CASE WHEN pb.XX_DocumentNo_ID IS NULL " +
							    "THEN (ROUND(inv.totalLines, 2) + ROUND(inv.XX_TaxAmount, 2) - ROUND(pb.XX_WithHoldingTax, 2)) " +
							    "ELSE (ROUND(nota.totalLines, 2) + ROUND(nota.XX_TaxAmount, 2) - ROUND(pb.XX_WithHoldingTax, 2)) END) " +
							    "- NVL((select sum(ROUND(NVL(a.XX_RetainedAmount,0),2)) from XX_VCN_ISLRAMOUNT a where a.C_Invoice_ID = " +
							        "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID  ELSE nota.C_Invoice_ID END) " +
							    "),0) " +
							") as total, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN doc.XX_CodDoc  ELSE  doc_nota.XX_CodDoc  END) XX_CodDoc, " + 
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.documentNo ELSE nota.documentNo END) documentNo, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID  ELSE nota.C_Invoice_ID END) C_Invoice_ID, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Currency_ID  ELSE nota.C_Currency_ID END) C_Currency_ID, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.DateInvoiced,'dd/MM/yyyy') " +
							"ELSE to_char(nota.DateInvoiced,'dd/MM/yyyy') END) fdo, " +
							"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.XX_DueDate,'dd/MM/yyyy') " +
							"ELSE to_char(inv.XX_DueDate,'dd/MM/yyyy') END) fve, " +
							"substr(upper(par.name),1,40) name, par.C_BPartner_ID "
					);
				}
				
				m_sql.append(
						"FROM XX_VCN_PurchasesBook pb " +
						"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') " +
						                         "AND (inv.C_Order_ID IS NOT NULL OR inv.XX_Contract_ID IS NOT NULL)) " +
						"left join C_Invoice nota ON (nota.C_Invoice_ID = pb.XX_DocumentNo_ID AND nota.XX_InvoiceType in ('A','S') " +
												 "AND (nota.C_Order_ID IS NOT NULL OR nota.XX_Contract_ID IS NOT NULL)) " +
						"left join C_Order ord ON (ord.C_Order_ID = pb.C_Order_ID) " +
						"left join XX_Contract con ON (con.XX_Contract_ID = inv.XX_Contract_ID) " +
						"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
						"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
						"left join C_DocType doc_nota on (nota.C_DocTypeTarget_ID = doc_nota.C_DocType_ID) " +
						"where pb.XX_Status='CO' and inv.isSoTrx = 'N'  AND pb.XX_ControlNumber <> ' ' and inv.DocStatus = 'CO' " +
						"AND (con.XX_Contract_ID IS NULL OR con.XX_Lease = 'N') " +
						"AND (ord.C_Order_ID IS NULL OR (ord.XX_PurchaseType = 'SU' OR ord.XX_PurchaseType = 'SE')) " +
						"AND TRUNC(pb.created) = to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
						"AND (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL) " +
						"and pb.C_Order_ID IS not NULL "
				);
			}
			
			sql = m_sql.toString();
			 
			StringBuffer m_sqlElmentUnion = new StringBuffer();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				while (rs.next()){		
					haber = new BigDecimal(0);
					debe = new BigDecimal(0);
					m_sqlElment = new StringBuffer();					
					m_sqlElment.append("select C_ElementValue_ID, value, " +
									   "(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
									   "where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null end) div, " +
									   "(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
									   "where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null end) sec, " +
									   "(select XX_Suc from AD_Org where AD_Org_ID = " + Env.getCtx().getAD_Client_ID() + ") suc, ");
					
					if (linea == 1){
						m_sqlElment = new StringBuffer();	
						m_sqlElment.append("select C_ElementValue_ID, value, " +
								   			"(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
								   			"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
								   			" else null end) div, " +
								   			"(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
								   			"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
								   			" else null end) sec, " +
								   			"(select XX_Suc from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") suc, " +
								   			"(case when XX_Aux = 'Y' then " +
											"(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
											" else null " +
											" end) aux, " +
								   			"(case when XX_Doc = 'Y' then " +
											   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " +
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
											   " else null " +
											   " end) tipdoc, " +
											   " (case when XX_Fdo = 'Y' then '" + rs.getString("fdo2") + "' " +
										   	   " else null " +
										       " end) fdo, " +
											   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, " +
								   			"(case when XX_Dep = 'Y' then " +
								   			"(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") " +
								   			" else null " +
								   			" end) dep " +
								   			"from C_ElementValue " +
								   			"where C_ElementValue_ID = " + rs.getInt("account"));
						if (rs.getString(5).equals("S"))
							description = rs.getString("name").substring(0, rs.getString("name").length()-6) + "/"+
								rs.getString("ma");
						else
							description = rs.getString("name");
						if(rs.getBigDecimal("subTotal").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("subTotal");
						} else
							haber = rs.getBigDecimal("subTotal").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					
					if (linea == 2 || linea == 3 || linea == 4){
						m_sqlElment.append("(case when XX_Dep = 'Y' then " +
										   "(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt("AD_Org_ID") + ") " +
										   " else null " +
										   " end) dep, " +						   
										   "(case when XX_Aux = 'Y' then " +
										   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
										   " else null " +
										   " end) aux, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, ");
						
						if (linea == 2) {
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo2") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " +
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_Tax = 'Y' ");
							description = rs.getString("name");
							if(rs.getBigDecimal("impuesto").compareTo(new BigDecimal(0)) > 0){
								debe = rs.getBigDecimal("impuesto");
							} else
								haber = rs.getBigDecimal("impuesto").multiply(BigDecimal.valueOf(-1));
							if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)
								linea2++;
						}else if (linea == 3){
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then XX_WithHolding " +
										       " else null " + 
										       " end) doc, " +
										       "(case when XX_Doc = 'Y' then 'RET' " +
										       " else null " +
										       " end) tipdoc " +
									   			"from C_ElementValue a, XX_VCN_PurchasesBook b ");
					
					m_sqlElmentUnion = new StringBuffer();
					m_sqlElmentUnion.append(" UNION ");
					m_sqlElmentUnion.append(m_sqlElment);
									   
					m_sqlElment.append("inner join C_Invoice inv ON (inv.C_INVOICE_ID = b.C_INVOICE_ID) "+
									  "where b.XX_Status='CO' and AccountType = 'L' " +
									   "and XX_WithholdingTaxMarck = 'Y' AND XX_WITHHOLDING IS NOT NULL AND XX_DOCUMENTNO_ID IS NULL " +
									   "and (inv.XX_ApprovalDate between to_date('" + 
									   new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
									   "','yyyy-MM-dd') " +
									   "and to_date('" + 
							   new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
									   "','yyyy-MM-dd')) " + 
									   "AND inv.C_Invoice_ID = " + rs.getInt("C_Invoice_ID"));
					
					m_sqlElmentUnion.append(
							   "inner join C_invoice cred ON (cred.C_INVOICE_ID = b.XX_DOCUMENTNO_ID) "+
							   "where b.XX_Status='CO' and AccountType = 'L' " +
							   "and XX_WithholdingTaxMarck = 'Y' AND XX_WITHHOLDING IS NOT NULL AND XX_DOCUMENTNO_ID IS NOT NULL " +
							   "and to_char(b.created,'yyyy-mm-dd') =  '" + 
							   new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
									   "' and cred.XX_ControlNumber is not null and " +
									   "b.XX_DOCUMENTNO_ID = " + rs.getInt("C_Invoice_ID"));
					
					m_sqlElment.append(m_sqlElmentUnion);
							description = rs.getString("name");	
							if(rs.getBigDecimal("IVA").compareTo(new BigDecimal(0)) > 0){
								haber = rs.getBigDecimal("IVA");
							} else
								debe = rs.getBigDecimal("IVA").multiply(BigDecimal.valueOf(-1));
							if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)	
								linea2++;
						}else{
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select XX_IslrReceiptNo from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " + 
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then 'RET' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_ISLR = 'Y' ");
							description = rs.getString("name");
							if (rs.getBigDecimal("ISLR").compareTo(new BigDecimal(0)) != 0){
								haber = rs.getBigDecimal("ISLR");
								debe = new BigDecimal(0);
								linea2++;
							}
						}
					}else if (linea == 5){
						m_sqlElment.append("null dep, null aux, " +
										   "(case when XX_Fdo = 'Y' then '" + rs.getString("fdo") + "' " +
										   " else null " +
										   " end) fdo, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fdo") + "' " +
										   " else null " +
										   " end) fve, " +
										   "(case when XX_Doc = 'Y' then '" + rs.getString("documentNo") + "' " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then 'FAC' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue " +
										   "where M_Product_Category_ID = " + 
					   							Env.getCtx().getContextAsInt("#M_PRODUCT_CATEGORY_ID") + " " +
							   	   		   "and AccountType = 'L' ");
						description = rs.getString("name");
						if(rs.getBigDecimal("total").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("total");
						} else
							debe = rs.getBigDecimal("total").multiply(BigDecimal.valueOf(-1));
						if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)	
						linea2++;
					}
					if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)											
						if (!executeDetailCxPA(rs, m_sqlElment.toString(), linea2, headID, description, debe, haber, linea,false)){
							return false;
						}
						else{
							debeTotal = debeTotal.add(debe);
							haberTotal = haberTotal.add(haber);
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			diff = haberTotal.subtract(debeTotal);
			
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			aEnt.setXX_TotalHave(haberTotal);
			aEnt.setXX_TotalShould(debeTotal);
			aEnt.save();
			
			linea++;
		}
		
		//Agrega el diferencial en decimales de los asientos
		if(diff.compareTo(BigDecimal.ZERO)!=0 && diff.abs().compareTo(BigDecimal.valueOf(2))<0)
		{
			boolean should = true;
			MFactAcct detailFactA = new MFactAcct(Env.getCtx(), detailDiff, get_Trx());
			
			if(detailFactA.getAmtAcctDr().compareTo(BigDecimal.ZERO)!=0 & detailFactA.getAmtSourceDr().compareTo(BigDecimal.ZERO)!=0){
				detailFactA.setAmtAcctDr(detailFactA.getAmtAcctDr().add(diff));
				detailFactA.setAmtSourceDr(detailFactA.getAmtSourceDr().add(diff));
			}
			else{
				detailFactA.setAmtAcctCr(detailFactA.getAmtAcctCr().add(diff.multiply(BigDecimal.valueOf(-1))));
				detailFactA.setAmtSourceCr(detailFactA.getAmtSourceCr().add(diff.multiply(BigDecimal.valueOf(-1))));
				should = false;
			}
			
			detailFactA.save();
			
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			if(should)
				aEnt.setXX_TotalShould(aEnt.getXX_TotalShould().add(diff));
			else
				aEnt.setXX_TotalHave(aEnt.getXX_TotalHave().add(diff.multiply(BigDecimal.valueOf(-1))));
			
			aEnt.save();
		}
		
		detailDiff = 0;
		
		return true;
	}
	
	/**
	 * Procesamiento para los asientos de activos fijos nacional
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param headID identificador de la cabecera
	 */
	public Boolean buildDetailThreeCxPA(Timestamp dateFrom, Timestamp dateTo, int headID){		
		int linea = 1;
		int linea2 = 0;
		String sql = "";
		String description = "";
		GregorianCalendar calFrom = new GregorianCalendar();
		calFrom.setTime(dateFrom);
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal debe = new BigDecimal(0);
		BigDecimal haber = new BigDecimal(0);
		BigDecimal debeTotal = BigDecimal.ZERO;
		BigDecimal haberTotal = BigDecimal.ZERO;

		while (linea < 6){
			haber = new BigDecimal(0);
			debe = new BigDecimal(0);
			m_sql = new StringBuffer();
			if (linea == 1){
				m_sql.append("SELECT " +
						"sum( " +
						        "ROUND((CASE WHEN inv.XX_Contract_ID IS NULL THEN " +
						                "((CASE " +
						                "WHEN PPDA.XX_AMOUNTPERCC IS NOT NULL " +
						                "THEN ((PPDA.XX_AMOUNTPERCC*100)/(ili.lineNetAmt)) " +
						                "ELSE 100 END)/100) * " +
						                "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ili.lineNetAmt ELSE iliN.lineNetAmt END) " +
						        "ELSE " +
						                 "((ppdS.XX_PERCENAMOUNT /100)) * " +
						                 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ili.lineNetAmt " +
							         "ELSE iliN.lineNetAmt END) " +
						        "END),2) " +      
						") SUBTOTAL, " + 
						"(CASE WHEN pac.XX_PInventory_ID IS NULL THEN elvB.C_ElementValue_ID ELSE elvA.C_ElementValue_ID END) account, " +
						"(CASE WHEN inv.XX_Contract_ID IS NULL THEN ppdA.XX_Org_ID ELSE ppdS.XX_COSTCENTER_ID END) suc, " +
						"inv.XX_InvoiceType, par.C_BPartner_ID, substr(par.name,0,40) name, inv.XX_Month || inv.XX_Year ma, " +
						"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID ELSE nota.C_Invoice_ID END) C_INVOICE_ID, " +
						"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Currency_ID ELSE nota.C_Currency_ID END) C_CURRENCY_ID, " +
						"doc.XX_CodDoc, to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo2, to_char(inv.XX_DueDate,'dd/MM/yyyy') fve " +
						"FROM XX_VCN_PurchasesBook pb " +
						"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType = 'A' " +
                        "AND (inv.C_Order_ID IS NOT NULL OR inv.XX_Contract_ID IS NOT NULL)) " +
                        "left join C_Invoice nota ON (nota.C_Invoice_ID = pb.XX_DocumentNo_ID AND nota.XX_InvoiceType = 'A') " +
						"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
						"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
						"left join C_DocType doc_nota on (nota.C_DocTypeTarget_ID = doc_nota.C_DocType_ID) " +
						"inner join C_InvoiceLine ili on (inv.C_Invoice_ID = ili.C_Invoice_ID) " +
						"left join C_InvoiceLine iliN on (nota.C_Invoice_ID = iliN.C_Invoice_ID AND ili.M_Product_ID = iliN.M_Product_ID) " +
						"left join XX_ProductPercentDistrib ppdA ON (ppdA.C_InvoiceLine_ID = ili.C_InvoiceLine_ID) " +
						"left join XX_ContractDetail ppdS ON (inv.XX_PayContract_ID = ppdS.XX_PayContract_ID) " +
						"left  join M_Product_Acct pac on (ili.M_Product_ID = pac.M_Product_ID) " +
						"left join C_ElementValue elvA on (pac.XX_PInventory_ID = elvA.C_ElementValue_ID) " +
						"left join C_ElementValue elvB on (pac.XX_PExpenses_ID = elvB.C_ElementValue_ID) " +
						"left join C_Order ord ON (ord.C_Order_ID = pb.C_Order_ID) " +
						"left join XX_Contract con ON (con.XX_Contract_ID = inv.XX_Contract_ID) " +
						"where pb.XX_Status='CO' and inv.isSoTrx = 'N' and inv.XX_InvoiceType in ('A','S') AND pb.XX_ControlNumber <> ' ' " +
						"AND inv.DocStatus = 'CO' " +
						"AND (con.XX_Contract_ID IS NULL OR con.XX_Lease = 'N') " +
						"AND (ord.C_Order_ID IS NULL OR ord.XX_PurchaseType = 'FA') " +
						"AND TRUNC(pb.created) = to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
						"and (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL) " +
						"and pb.C_Order_ID IS not NULL " +
						"GROUP BY inv.DOCUMENTNO, nota.DOCUMENTNO, inv.XX_InvoiceType, par.C_BPartner_ID, par.name, doc.XX_CodDoc, inv.DateInvoiced, " +
						"elvA.C_ElementValue_ID, pac.XX_PInventory_ID, inv.XX_Contract_ID, ppdA.XX_Org_ID, ppdS.XX_COSTCENTER_ID, " +
						"inv.XX_DueDate, inv.XX_Month, inv.XX_Year, inv.C_Invoice_ID, nota.C_Invoice_ID, inv.C_Currency_ID, nota.C_Currency_ID, pb.XX_DocumentNo_ID, elvB.C_ElementValue_ID " +
						"HAVING " +
						"sum( " +
						        "(CASE WHEN inv.XX_Contract_ID IS NULL THEN " +
						                "((CASE " +
						                "WHEN PPDA.XX_AMOUNTPERCC IS NOT NULL THEN ((PPDA.XX_AMOUNTPERCC*100)/(ili.XX_PriceEnteredInvoice*ili.qtyEntered)) " +
						                "ELSE 100 END)/100) * " +
						                "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN (ili.lineNetAmt) ELSE iliN.lineNetAmt END) " +
						        "ELSE " +
						                 "((ROUND(ppdS.XX_PERCENAMOUNT, 2) /100)) * " +
						                 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ROUND(inv.totallines, 2) " +
							         "ELSE ROUND(nota.totallines, 2) END) " +
						        "END) " +       
						") <> 0 " +
						"ORDER BY par.NAME");
			}else{
				if (linea != 5){
					m_sql.append("select TRUNC(inv.XX_TaxAmount, 2) as impuesto, " +
							 "coalesce((select withHoldingTaxInvoice(inv.C_Invoice_ID) from dual),0) as IVA, " +
							 "nvl((select sum(a.XX_RetainedAmount) " +
							 	"from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0) as ISLR," +
							 "doc.XX_CodDoc, to_char(inv.XX_DueDate,'dd/MM/yyyy') fve, substr(upper(par.name),1,40) name, " +
							 "inv.AD_Org_ID, par.C_BPartner_ID, inv.C_Invoice_ID, inv.C_Currency_ID," +
							 "to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo2, " +
							 "to_char(inv.XX_ApprovalDate,'dd/MM/yyyy') fdo34 ");
				}else{
					m_sql.append("select TRUNC(((inv.TotalLines + inv.XX_TaxAmount) - " +
							 "coalesce((select withHoldingTaxInvoice(inv.C_Invoice_ID) from dual),0) - " +
							 "nvl((select sum(a.XX_RetainedAmount) " +
							 	"from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0)), 2) as total, " +
							 "doc.XX_CodDoc, inv.documentNo, to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo, " +
							 "to_char(inv.XX_DueDate,'dd/MM/yyyy') fve, substr(upper(par.name),1,40) name, " +
							 "inv.AD_Org_ID, par.C_BPartner_ID, inv.C_Invoice_ID,  inv.C_Currency_ID ");		
				}
				m_sql.append("from C_Invoice inv " +
							 "left  join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
							 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
							 "inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
							 "where inv.XX_InvoiceType = 'A' " +
							 "and inv.C_Order_ID is not null " +
							 "and ord.XX_InvoicingStatus = 'AP' " +
							 "and inv.XX_ApprovalDate between to_date('" + 
						 		new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
						 			"','yyyy-MM-dd') " +
						 		"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
						 			"','yyyy-MM-dd') " +
							 "and ord.XX_OrderType = 'Nacional' " +
							 "and inv.DocStatus = 'CO' " +
							 "and ord.XX_POType = 'POA' " +
							 "and ord.XX_PurchaseType = 'FA' ");
			}
			sql = m_sql.toString();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();	
				while (rs.next()){	
					haber = new BigDecimal(0);
					debe = new BigDecimal(0);
					m_sqlElment = new StringBuffer();					
					m_sqlElment.append("select C_ElementValue_ID, value, " +
									   "(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) div, " +
									   "(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) sec, ");
	
					if (linea == 1){
						m_sqlElment.append("null aux, null doc, null tipdoc, null fdo, null fve, null dep, " +
										   "(select XX_Suc from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") suc " +
						     			   "from C_ElementValue " +
										   "where C_ElementValue_ID = " + rs.getInt("account"));
						description = rs.getString("name");
						if(rs.getBigDecimal("subTotal").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("subTotal");
						} else
							haber = rs.getBigDecimal("subTotal").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					
					if (linea == 2 || linea == 3 || linea == 4){
						m_sqlElment.append("(case when XX_Dep = 'Y' then " +
										   "(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt("AD_Org_ID") + ") " +
										   " else null " +
										   " end) dep, " +						   
										   "(case when XX_Aux = 'Y' then " +
										   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
										   " else null " +
										   " end) aux, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, " +
										   "(select XX_Suc from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") suc, ");
						
						if (linea == 2) {
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo2") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " +
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_Tax = 'Y' ");
							description = rs.getString("name");
							if(rs.getBigDecimal("impuesto").compareTo(new BigDecimal(0)) > 0){
								debe = rs.getBigDecimal("impuesto");
							} else
								haber = rs.getBigDecimal("impuesto").multiply(BigDecimal.valueOf(-1));
							linea2++;
						}else if (linea == 3){
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then XX_WithHolding " + 
											   " else null " + 
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then 'RET' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue a, XX_VCN_PurchasesBook b " +
											   "where b.XX_Status='CO' and AccountType = 'L' " +
											   "and XX_WithholdingTaxMarck = 'Y' AND C_Invoice_ID = " + rs.getInt("C_Invoice_ID"));
							description = rs.getString("name");
							if(rs.getBigDecimal("IVA").compareTo(new BigDecimal(0)) > 0){
								haber = rs.getBigDecimal("IVA");
							} else
								debe = rs.getBigDecimal("IVA").multiply(BigDecimal.valueOf(-1));
								linea2++;
							}else{
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select XX_IslrReceiptNo from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " + 
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then 'RET' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_ISLR = 'Y' ");
							description = rs.getString("name");
							if (rs.getBigDecimal("ISLR").compareTo(new BigDecimal(0)) != 0){
								haber = rs.getBigDecimal("ISLR");
								debe = new BigDecimal(0);
								linea2++;
							}
						}
					}else if (linea == 5){
						m_sqlElment.append("null dep, " +
										   "(case when XX_Aux = 'Y' then " +
										   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
										   " else null " +
										   " end) aux, " +
										   "(case when XX_Fdo = 'Y' then '" + rs.getString("fdo") + "' " +
										   " else null " +
										   " end) fdo, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fdo") + "' " +
										   " else null " +
										   " end) fve, " +
										   "(case when XX_Doc = 'Y' then '" + rs.getString("documentNo") + "' " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then 'FAC' " +
										   " else null " +
										   " end) tipdoc, " +
										   "(select XX_Suc from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") suc " +
										   "from C_ElementValue " +
										   "where M_Product_Category_ID = " + 
					   							Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID") + " " +
							   	   		   "and AccountType = 'L' " +
							   	   		   "and XX_AccountPayable = 'Y' "); 
						description = rs.getString("name");
						if(rs.getBigDecimal("total").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("total");
						} else
							debe = rs.getBigDecimal("total").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)											
						if (!executeDetailCxPA(rs, m_sqlElment.toString(), linea2, headID, description, debe, haber, linea,false))
							return false;
						else{
							debeTotal = debeTotal.add(debe);
							haberTotal = haberTotal.add(haber);
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			aEnt.setXX_TotalHave(haberTotal);
			aEnt.setXX_TotalShould(debeTotal);
			aEnt.save();
			
			linea++;
		}
		return true;
	}
	
	/**
	 * Procesamiento para los asientos de servicios importados
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param headID identificador de la cabecera
	 */
	public Boolean buildDetailFourCxPA(Timestamp dateFrom, Timestamp dateTo, int headID){
		int linea = 1;
		int linea2 = 0;
		String sql = "";
		String description = "";
		GregorianCalendar calFrom = new GregorianCalendar();
		calFrom.setTime(dateFrom);
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal debe = new BigDecimal(0);
		BigDecimal haber = new BigDecimal(0);
		BigDecimal debeTotal = BigDecimal.ZERO;
		BigDecimal haberTotal = BigDecimal.ZERO;

		while (linea < 3){
			haber = new BigDecimal(0);
			debe = new BigDecimal(0);
			m_sql = new StringBuffer();
			if (linea == 1){
				m_sql.append("select sum(((ppd.XX_AmountPerCC*100)/ inv.totallines)* inv.totallines /100 ) * " + averageDollar(dateTo) + ") subTotal, " +
						 "elv.C_ElementValue_ID account, ppd.XX_Org_ID cc, par.C_BPartner_ID, " +
						 "substr(par.name,0,40) name, inv.XX_Month || inv.XX_Year ma " +
						 "from XX_ProductPercentDistrib ppd " +
						 "inner join C_InvoiceLine ili on (ppd.C_InvoiceLine_ID = ili.C_InvoiceLine_ID) " +
						 "inner join C_Invoice inv on (inv.C_Invoice_ID = ili.C_Invoice_ID) " +
						 "inner join C_Order ord on (ppd.C_Order_ID = ord.C_Order_ID) " +
						 "inner join M_Product_Acct pac on (ili.M_Product_ID = pac.M_Product_ID) " +
						 "inner join C_ElementValue elv on (pac.XX_PInventory_ID = elv.C_ElementValue_ID or " +
						 	"pac.XX_PExpenses_ID = elv.C_ElementValue_ID) " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "where inv.isSoTrx = 'N' " +
						 "and inv.C_Order_ID is not null " +
						 "and inv.XX_ApprovalDate between to_date('" + 
						 	new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
						 		"','yyyy-MM-dd') " +
						 	"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
						 		"','yyyy-MM-dd') " +
						 "and (inv.XX_InvoiceType = 'A' or inv.XX_InvoiceType = 'S') " +
						 "and inv.DocStatus = 'CO' " +
						 "and ord.XX_InvoicingStatus = 'AP' " +
						 "and ord.XX_OrderType = 'Importada' " +
						 //"and inv.XX_SynchronizationAccount = 'N' " +
						 "and inv.XX_ApprovalDate is not null " +
						 "and (ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE') " +
						 "and inv.XX_InvoiceType = 'S' " +
						 "group by  elv.C_ElementValue_ID, ppd.XX_Org_ID, par.C_BPartner_ID, " +
						 "par.name, inv.XX_Month, inv.XX_Year " +
						 "union all " +	
						 "select TRUNC(sum((cde.XX_PERCENAMOUNT/100)*inv.totallines), 2) * " + averageDollar(dateTo) + ") subTotal, " +
						 "cinv.XX_PExpenses_ID  account, cde.XX_CostCenter_ID cc, par.C_BPartner_ID, " +
						 "substr(par.name,0,40) name, inv.XX_Month || inv.XX_Year ma " +
						 "from XX_ContractDetail cde " +
						 "inner join XX_Contract con on (con.XX_Contract_ID = cde.XX_Contract_ID) " +
						 "inner join C_Invoice inv on (con.XX_Contract_ID = inv.XX_Contract_ID) " +
						 "inner join XX_VCN_ContraCtInvoice cinv on (con.XX_Contract_ID = cinv.XX_Contract_ID) " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "where inv.isSoTrx = 'N' " +
						 "and inv.XX_InvoiceType = 'S' " +
						 "and inv.XX_Contract_ID is not null " +
						 "and inv.XX_InvoicingStatusContract = 'AP' " +
						 "and inv.XX_ApprovalDate between to_date('" + 
						 	new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
						 		"','yyyy-MM-dd') " +
						 	"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
						 		"','yyyy-MM-dd') " +
						 "and con.XX_ContractType = 'Importada' " +
						 "and inv.DocStatus = 'CO' " +
						 //"and inv.XX_SynchronizationAccount = 'N' " +
						 "group by cinv.XX_PExpenses_ID, cde.XX_CostCenter_ID, par.C_BPartner_ID, par.name, inv.XX_Month, inv.XX_Year ORDER BY NAME ");
			}else{
				m_sql.append("select (inv.GrandTotal * " + averageDollar(dateTo) + ") as total, " +
						 "doc.XX_CodDoc, inv.documentNo, to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo, " +
						 "to_char(inv.XX_DueDate,'dd/MM/yyyy') fve, substr(upper(par.name),1,40) name, " +
						 "inv.AD_Org_ID, par.C_BPartner_ID, inv.C_Invoice_ID, inv.C_Currency_ID, inv.XX_Month || inv.XX_Year ma " +		
						 "from C_Invoice inv " +
						 "left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
						 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
						 "where inv.XX_InvoiceType = 'S' " +
						 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') " +
						 "and (ord.XX_OrderType = 'Importada' or con.XX_ContractType = 'Importada') " +
						 "and inv.XX_ApprovalDate between to_date('" + 
					 		new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
					 			"','yyyy-MM-dd') " +
					 		"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
					 			"','yyyy-MM-dd') " +
						 "and inv.DocStatus = 'CO' " +
						 //"and inv.XX_SynchronizationAccount = 'N' " +
						 "and ((ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE') or  inv.XX_Contract_ID is not null)");
			}
			sql = m_sql.toString();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();	
				while (rs.next()){	
					haber = new BigDecimal(0);
					debe = new BigDecimal(0);
					m_sqlElment = new StringBuffer();					
					m_sqlElment.append("select C_ElementValue_ID, value, " +
									   "(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) div, " +
									   "(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) sec, " +
									   "(select TO_CHAR(XX_Suc) from AD_Org where XX_AccPrincipal = 'Y') suc, ");
					
					if (linea == 1){
						m_sqlElment.append("null aux, null doc, null tipdoc, null fdo, null fve, " +
										   "(case when XX_Dep = 'Y' then " +
										   	"(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt("cc") + ") " +
										   " else null " +
										   " end) dep " +
										   "from C_ElementValue " +
										   "where C_ElementValue_ID = " + rs.getInt("account"));
						description = rs.getString("name") + "/" + rs.getString("ma");
						if(rs.getBigDecimal("subTotal").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("subTotal");
						} else
							haber = rs.getBigDecimal("subTotal").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}else{
						m_sqlElment.append("null aux, null dep, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, " +
										   "(case when XX_Fdo = 'Y' then '" + rs.getString("fdo") + "' " +
									   	   " else null " +
									       " end) fdo, " +
									       "(case when XX_Doc = 'Y' then " +
										   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
										   		rs.getInt("C_Invoice_ID") + ") " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue " +
										   "where AccountType = 'L' " +
										   "and M_Product_Category_ID =  " + Env.getCtx().getContextAsInt("#M_PRODUCT_CATEGORY_ID"));
						description = rs.getString("name") +"/"+  rs.getString("ma");
						if(rs.getBigDecimal("total").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("total");
						} else
							debe = rs.getBigDecimal("total").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)
						if (!executeDetailCxPA(rs, m_sqlElment.toString(), linea2, headID, description, debe, haber, linea,false))
							return false;
						else{
							debeTotal = debeTotal.add(debe);
							haberTotal = haberTotal.add(haber);
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			aEnt.setXX_TotalHave(haberTotal);
			aEnt.setXX_TotalShould(debeTotal);
			aEnt.save();
			
			linea++;
		}
		
		return true;
	}
	
	/**
	 * Procesamiento para los asientos de arrendamiento nacional
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 */
	public Boolean buildDetailFiveCxPA(Timestamp dateFrom, Timestamp dateTo, int headID){
		int linea = 1;
		int linea2 = 0;
		String sql = "";
		String description = "";
		GregorianCalendar calFrom = new GregorianCalendar();
		calFrom.setTime(dateFrom);
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal debe = new BigDecimal(0);
		BigDecimal haber = new BigDecimal(0);
		BigDecimal debeTotal = BigDecimal.ZERO;
		BigDecimal haberTotal = BigDecimal.ZERO;

		while (linea < 6){
			haber = new BigDecimal(0);
			debe = new BigDecimal(0);
			m_sql = new StringBuffer();
			if (linea == 1){
				m_sql.append("select ROUND(sum((ppdS.XX_PERCENAMOUNT/100)*ili.lineNetAmt), 2) subTotal, " +
                             "(CASE WHEN pac.XX_PExpenses_ID IS NULL THEN elvA.C_ElementValue_ID ELSE elvB.C_ElementValue_ID END)  account, " + 
                             "ppdS.XX_CostCenter_ID, par.C_BPartner_ID, substr(par.name,0,34) name, " +  
                             "inv.AD_Org_ID, inv.XX_Month || inv.XX_Year ma, " + 
                             "inv.C_INVOICE_ID, " + 
                             "doc.XX_CodDoc, to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo2, to_char(inv.XX_DueDate,'dd/MM/yyyy') fve   " +
							 "FROM XX_VCN_PurchasesBook pb " +
							 "left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') " +
							 "AND inv.XX_Contract_ID IS NOT NULL) " +
							 "inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " + 
							 "left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
							 "inner join C_InvoiceLine ili on (inv.C_Invoice_ID = ili.C_Invoice_ID) " +
							 "left join XX_ProductPercentDistrib ppdA ON (ppdA.C_InvoiceLine_ID = ili.C_InvoiceLine_ID) " + 
							 "left join XX_ContractDetail ppdS ON (inv.XX_PayContract_ID = ppdS.XX_PayContract_ID) " +
							 "left  join M_Product_Acct pac on (ili.M_Product_ID = pac.M_Product_ID) " +
							 "left join C_ElementValue elvA on (pac.XX_PInventory_ID = elvA.C_ElementValue_ID) " +
							 "left join C_ElementValue elvB on (pac.XX_PExpenses_ID = elvB.C_ElementValue_ID) " +
							 "left join XX_Contract con ON (con.XX_Contract_ID = inv.XX_Contract_ID) "+ 
							 "where inv.isSoTrx = 'N' " +
							 "and inv.XX_InvoiceType = 'S' " +
							 "and inv.XX_Contract_ID is not null " +
							 "and inv.XX_InvoicingStatusContract = 'AP' " +
							 "and inv.XX_ApprovalDate =  to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
							 		"','yyyy-MM-dd') " +
							 "and con.XX_ContractType = 'Nacional' " +
							 "and inv.DocStatus = 'CO' " +
							 "and con.XX_Lease = 'Y' " +
							 "group by pac.XX_PExpenses_ID, " +
							 "ppdS.XX_CostCenter_ID, par.C_BPartner_ID, " +
							 "par.name,  inv.AD_Org_ID, inv.XX_Month, inv.XX_Year, " + 
							 "inv.C_INVOICE_ID, doc.XX_CodDoc, inv.DateInvoiced, inv.XX_DueDate , elvB.C_ElementValue_ID, elvA.C_ElementValue_ID ");
			}else{
				if (linea != 5){
					m_sql.append("select ROUND(inv.XX_TaxAmount, 2) as impuesto, " +
							 "coalesce(ROUND((select withHoldingTaxInvoice(inv.C_Invoice_ID) from dual), 2),0) as IVA, " +
							 "nvl((select ROUND(sum(a.XX_RetainedAmount), 2) " +
							 	"from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0) as ISLR," +
							 "doc.XX_CodDoc, to_char(inv.XX_DueDate,'dd/MM/yyyy') fve, substr(upper(par.name),0,34) name, " +
							 "inv.AD_Org_ID, par.C_BPartner_ID, inv.C_Invoice_ID, inv.C_Currency_ID, " +
							 "to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo2, " +
							 "to_char(inv.XX_ApprovalDate,'dd/MM/yyyy') fdo34, " +
							 "inv.XX_Month || inv.XX_Year ma ");
					//CCAPOTE
					m_sql.append("from C_Invoice inv " +
							 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
							 "inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
							 "inner join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
							 //"inner join XX_ContractDetail cde on (con.XX_Contract_ID = cde.XX_Contract_ID) " +
							 "where inv.isSoTrx = 'N' and inv.XX_InvoiceType = 'S' " +
							 "and inv.XX_Contract_ID is not null " +
							 "and inv.XX_InvoicingStatusContract = 'AP' " +
							 "and inv.XX_ApprovalDate between to_date('" + 
						 		new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
						 			"','yyyy-MM-dd') " +
						 	 "and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
						 			"','yyyy-MM-dd') " +
							 "and con.XX_ContractType = 'Nacional' " +
							 "and inv.DocStatus = 'CO' " +
							 "and con.XX_Lease = 'Y' ");
				}else{
					m_sql.append("select ROUND(sum((ppdS.XX_PERCENAMOUNT/100)* " +
								"((ROUND(inv.totalLines, 2) + ROUND(inv.XX_TaxAmount, 2) - ROUND(pb.XX_WithHoldingTax, 2)) " +
								" - nvl((select sum(ROUND(NVL(a.XX_RetainedAmount,0),2)) from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0))),2) as total, " + 
								"ppdS.XX_CostCenter_ID, par.C_BPartner_ID, " +
								"substr(upper(par.name),0,34) name, inv.AD_Org_ID, inv.XX_Month || inv.XX_Year ma, " + 
								"doc.XX_CodDoc, inv.documentNo, to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo,  " +
								"to_char(inv.XX_DueDate,'dd/MM/yyyy') fve,  inv.C_Invoice_ID, inv.C_Currency_ID ");	
					//CCAPOTE
					m_sql.append("from XX_VCN_PurchasesBook pb  " +
								"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') AND inv.XX_Contract_ID IS NOT NULL) "+
								"left join XX_Contract con ON (con.XX_Contract_ID = inv.XX_Contract_ID) " +
								"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
								"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
								"inner join C_InvoiceLine ili on (inv.C_Invoice_ID = ili.C_Invoice_ID) " +
								"left join XX_ContractDetail ppdS ON (inv.XX_PayContract_ID = ppdS.XX_PayContract_ID) " +
								"where inv.isSoTrx = 'N' " +
								"and inv.XX_InvoiceType = 'S' " +
								"and inv.XX_Contract_ID is not null " +
								"and inv.XX_InvoicingStatusContract = 'AP' " +
								"and inv.XX_ApprovalDate =  to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
							  	"','yyyy-MM-dd') " +
							  	"and con.XX_ContractType = 'Nacional' " +
							 	"and inv.DocStatus = 'CO' " +
							 	"and con.XX_Lease = 'Y' " +
							 	"group by ppdS.XX_CostCenter_ID, par.C_BPartner_ID, " +
							 	"par.name, inv.AD_Org_ID, inv.XX_Month, inv.XX_Year, " +
								"doc.XX_CodDoc, inv.documentNo, inv.DateInvoiced, inv.XX_DueDate,  inv.C_Invoice_ID, inv.C_Currency_ID ");
				}
				
			}
			sql = m_sql.toString();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();	
				while (rs.next()){	
					haber = new BigDecimal(0);
					debe = new BigDecimal(0);
					m_sqlElment = new StringBuffer();					
					m_sqlElment.append("select C_ElementValue_ID, value, " +
									   "(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) div, " +
									   "(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) sec, ");
					
					if (linea == 1){
						m_sqlElment.append("null aux, null doc, null tipdoc, null fdo, null fve, " +
										   "(case when XX_Dep = 'Y' then " +
										   "(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt("XX_CostCenter_ID") + ") " +
										   " else null " +
										   " end) dep, " +
										   "(select " +
										   		"case " +
										   		"when exists " +
											   			"(select * from m_warehouse mw " +
											   			"where UPPER(mw.name) LIKE '%' || UPPER(ado.description) || '%') " +
											   		"then to_char(ado.value)" +
											   	   	"else " +
											   		"(select TO_CHAR(XX_Suc) from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") " +
											   	"end  suc " +
										   "from AD_Org ado where Description is not null " +
										   "and ado.AD_Org_ID = " + rs.getInt("XX_CostCenter_ID") + ") suc " + 
										   "from C_ElementValue " +
										   "where C_ElementValue_ID = " + rs.getInt("account"));
						description = rs.getString("name") + "/" + rs.getString("ma");
						if(rs.getBigDecimal("subTotal").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("subTotal");
						} else
							haber = rs.getBigDecimal("subTotal").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					
					if (linea == 2 || linea == 3 || linea == 4){
						m_sqlElment.append("(case when XX_Dep = 'Y' then " +
										   "(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt("AD_Org_ID") + ") " +
										   " else null " +
										   " end) dep, " +						   
										   "(case when XX_Aux = 'Y' then " +
										   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
										   " else null " +
										   " end) aux, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, " +
										   "'001' suc, ");
						
						if (linea == 2) {
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo2") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " +
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_Tax = 'Y' ");
							description = rs.getString("name");
							if(rs.getBigDecimal("impuesto").compareTo(new BigDecimal(0)) > 0){
								debe = rs.getBigDecimal("impuesto");
							} else
								haber = rs.getBigDecimal("impuesto").multiply(BigDecimal.valueOf(-1));
							linea2++;
						}else if (linea == 3){
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " XX_WithHolding " +
											   " else null " + 
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then 'RET' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue a, XX_VCN_PurchasesBook b " +
											   "where b.XX_Status='CO' and AccountType = 'L' " +
											   "and XX_WithholdingTaxMarck = 'Y' AND  C_Invoice_ID = " + rs.getInt("C_Invoice_ID"));
							description = rs.getString("name");
							if(rs.getBigDecimal("IVA").compareTo(new BigDecimal(0)) > 0){
								haber = rs.getBigDecimal("IVA");
							} else
								debe = rs.getBigDecimal("IVA").multiply(BigDecimal.valueOf(-1));
								linea2++;
							}else{
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select XX_IslrReceiptNo from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " + 
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then 'RET' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_ISLR = 'Y' ");
							description = rs.getString("name");
							if (rs.getBigDecimal("ISLR").compareTo(new BigDecimal(0)) != 0){
								haber = rs.getBigDecimal("ISLR");
								debe = new BigDecimal(0);
								linea2++;
							}
						}
					}else if (linea == 5){
						m_sqlElment.append("null dep, null aux, " +
										   "(select " +
											   		"case " +
											   		"when exists " +
												   			"(select * from m_warehouse mw " +
												   			"where UPPER(mw.name) LIKE '%' || UPPER(ado.description) || '%') " +
												   		"then to_char(ado.value)" +
												     	"else " +
												   		"'001' " +
												   	"end  suc " +
										   "from AD_Org ado where Description is not null " +
										   "and ado.AD_Org_ID = " + rs.getInt("XX_CostCenter_ID") + ") suc, " + 
										   "(case when XX_Fdo = 'Y' then '" + rs.getString("fdo") + "' " +
										   " else null " +
										   " end) fdo, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, " +
										   "(case when XX_Doc = 'Y' then '" + rs.getString("documentNo") + "' " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then 'FAC' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue " +
										   "where M_Product_Category_ID = " + 
					   							Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID") + " " +
							   	   		   "and AccountType = 'L' " +
							   	   		   "and XX_Lease = 'Y' ");
						description = rs.getString("name");
						if(rs.getBigDecimal("total").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("total");
						} else
							debe = rs.getBigDecimal("total").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)											
						if (!executeDetailCxPA(rs, m_sqlElment.toString(), linea2, headID, description, debe, haber, linea,false))
							return false;
						else{
							debeTotal = debeTotal.add(debe);
							haberTotal = haberTotal.add(haber);
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			aEnt.setXX_TotalHave(haberTotal);
			aEnt.setXX_TotalShould(debeTotal);
			aEnt.save();
			
			linea++;
		}
		
		return true;
	}
	
	/**
	 * Procesamiento para los asientos de activo fijo importado
	 * @param dateFrom
	 * @param dateTo
	 */
	public Boolean buildDetailSixCxPA(Timestamp dateFrom, Timestamp dateTo, int headID){
		int linea = 1;
		int linea2 = 0;
		String sql = "";
		String description = "";
		GregorianCalendar calFrom = new GregorianCalendar();
		calFrom.setTime(dateFrom);
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal debe = new BigDecimal(0);
		BigDecimal haber = new BigDecimal(0);
		BigDecimal debeTotal = BigDecimal.ZERO;
		BigDecimal haberTotal = BigDecimal.ZERO;

		while (linea < 3){
			haber = new BigDecimal(0);
			debe = new BigDecimal(0);
			m_sql = new StringBuffer();
			if (linea == 1){
				m_sql.append("select sum(((ppd.XX_AmountPerCC*100)/ inv.totallines)* inv.totallines /100 )  * " + averageDollar(dateTo) + ") subTotal, " +
						 "elv.C_ElementValue_ID account, ppd.XX_Org_ID cc, par.C_BPartner_ID, " +
						 "substr(par.name,0,34) name, inv.XX_Month || inv.XX_Year ma " +
						 "from XX_ProductPercentDistrib ppd " +
						 "inner join C_InvoiceLine ili on (ppd.C_InvoiceLine_ID = ili.C_InvoiceLine_ID) " +
						 "inner join C_Invoice inv on (inv.C_Invoice_ID = ili.C_Invoice_ID) " +
						 "inner join C_Order ord on (ppd.C_Order_ID = ord.C_Order_ID) " +
						 "inner join M_Product_Acct pac on (ili.M_Product_ID = pac.M_Product_ID) " +
						 "inner join C_ElementValue elv on (pac.XX_PInventory_ID = elv.C_ElementValue_ID or " +
						 	"pac.XX_PExpenses_ID = elv.C_ElementValue_ID) " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "where inv.isSoTrx = 'N' " +
						 "and inv.C_Order_ID is not null " +
						 "and inv.XX_ApprovalDate between to_date('" + 
						 	new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
						 		"','yyyy-MM-dd') " +
						 	"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
						 		"','yyyy-MM-dd') " +
						 "and inv.XX_InvoiceType = 'A' " +
						 "and inv.DocStatus = 'CO' " +
						 "and ord.XX_InvoicingStatus = 'AP' " +
						 "and ord.XX_OrderType = 'Importada' " +
						 //"and inv.XX_SynchronizationAccount = 'N' " +
						 "and (ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'FA') " +
						 "group by  elv.C_ElementValue_ID, ppd.XX_Org_ID, par.C_BPartner_ID, " +
						 "par.name, inv.XX_Month, inv.XX_Year ");
			}else{
				m_sql.append("select (inv.GrandTotal * " + averageDollar(dateTo) + ") as total, " +
						 "doc.XX_CodDoc, inv.documentNo, to_char(inv.DateInvoiced,'dd/MM/yyyy') fdo, " +
						 "to_char(inv.XX_DueDate,'dd/MM/yyyy') fve, substr(upper(par.name),1,34) name, " +
						 "inv.AD_Org_ID, par.C_BPartner_ID, inv.C_Invoice_ID, inv.C_Currency_ID, inv.XX_Month || inv.XX_Year ma " +
						 "from C_Invoice inv " +
						 "inner join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
						 "where inv.XX_InvoiceType = 'A' " +
						 "and ord.XX_InvoicingStatus = 'AP' " +
						 "and ord.XX_OrderType = 'Importada' " +
						 "and inv.XX_ApprovalDate between to_date('" + 
					 		new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + 
					 			"','yyyy-MM-dd') " +
					 		"and to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
					 			"','yyyy-MM-dd') " +
						 "and inv.DocStatus = 'CO' " +
						 //"and inv.XX_SynchronizationAccount = 'N' " +
						 "and (ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'FA') ");
			}
			sql = m_sql.toString();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();	
				while (rs.next()){	
					haber = new BigDecimal(0);
					debe = new BigDecimal(0);
					m_sqlElment = new StringBuffer();					
					m_sqlElment.append("select C_ElementValue_ID, value, " +
									   "(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) div, " +
									   "(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
									   		"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null " +
									   " end) sec, " +
									   "(select TO_CHAR(XX_Suc) from AD_Org where XX_AccPrincipal = 'Y') suc, ");

					if (linea == 1){
						m_sqlElment.append("null aux, null doc, null tipdoc, null fdo, null fve, " +
										   "null dep " +
										   "from C_ElementValue " +
										   "where C_ElementValue_ID = " + rs.getInt("account"));
						description = rs.getString("name") + "/" + rs.getString("ma");
						if(rs.getBigDecimal("subTotal").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("subTotal");
						} else
							haber = rs.getBigDecimal("subTotal").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}else{
						m_sqlElment.append("null dep, " +
										   "(case when XX_Aux = 'Y' then " +
										   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
										   " else null " +
										   " end) aux, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, " +
										   "(case when XX_Fdo = 'Y' then '" + rs.getString("fdo") + "' " +
									   	   " else null " +
									       " end) fdo, " +
									       "(case when XX_Doc = 'Y' then " +
										   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
										   		rs.getInt("C_Invoice_ID") + ") " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue " +
										   "where AccountType = 'L' " +
										   "and XX_AccountPayable = 'Y' " +
										   "and M_Product_Category_ID =  " + Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID"));
						description = rs.getString("name") + "/" + rs.getString("ma");
						if(rs.getBigDecimal("total").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("total");
						} else
							debe = rs.getBigDecimal("total").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)
						if (!executeDetailCxPA(rs, m_sqlElment.toString(), linea2, headID, description, debe, haber, linea,false))
							return false;
						else{
							debeTotal = debeTotal.add(debe);
							haberTotal = haberTotal.add(haber);
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			aEnt.setXX_TotalHave(haberTotal);
			aEnt.setXX_TotalShould(debeTotal);
			aEnt.save();
			
			linea++;
		}
		
		return false;
	}
	
	
/**
	 * Procesamiento para los asientos de suministros/materiales y servicios 
	 * sin Orden de Compras ni contratos
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param headID identificador de la cabecera
	 * RC 05062013
	 */
	public Boolean buildDetailSevenCxPA(Timestamp dateFrom, Timestamp dateTo, int headID){
		int linea = 1;
		int linea2 = 0;
		String sql = "";
		String description = "";
		GregorianCalendar calFrom = new GregorianCalendar();
		calFrom.setTime(dateFrom);
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal debe = new BigDecimal(0);
		BigDecimal haber = new BigDecimal(0);
		BigDecimal debeTotal = BigDecimal.ZERO;
		BigDecimal haberTotal = BigDecimal.ZERO;
		BigDecimal diff = BigDecimal.ZERO;
		detailDiff = 0;

		while (linea < 6){
			haber = new BigDecimal(0);
			debe = new BigDecimal(0);
			m_sql = new StringBuffer();
			if (linea == 1){
				m_sql.append(
		
				"SELECT " +
						
				"NVL(ROUND(sum(CASE WHEN pb.XX_DocumentNo_ID IS NULL "+
				        "THEN ((((PPDI.XX_AMOUNTPERCC * 100)/(ili.lineNetAmt))/100) * ili.lineNetAmt) "+
				        "ELSE ((((PPDN.XX_AMOUNTPERCC * 100)/(iliN.lineNetAmt))/100) * iliN.lineNetAmt) "+
				        "END "+
				 "),2),0) subTotal, "+
				
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN elvI.C_ElementValue_ID ELSE elvN.C_ElementValue_ID END) account, " +
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN ppdI.XX_Org_ID ELSE ppdN.XX_Org_ID END) suc, " +
		
				"par.C_BPartner_ID, " +
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.XX_InvoiceType ELSE nota.XX_InvoiceType END) XX_InvoiceType, " +
				"substr(par.name,0,40) name, inv.XX_Month || inv.XX_Year ma, " +
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID ELSE nota.C_Invoice_ID END) C_INVOICE_ID, " +
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Currency_ID ELSE nota.C_Currency_ID END) C_CURRENCY_ID, " +
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN doc.XX_CodDoc ELSE docN.XX_CodDoc END) XX_CodDoc, " +
				
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.DateInvoiced,'dd/MM/yyyy') ELSE to_char(nota.DateInvoiced,'dd/MM/yyyy') END) fdo2, " +
				"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.XX_DueDate,'dd/MM/yyyy') ELSE to_char(nota.XX_DueDate,'dd/MM/yyyy') END) fve " +
				"FROM XX_VCN_PurchasesBook pb " +
				"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') " +
											 "AND (inv.C_Order_ID IS NULL AND inv.XX_Contract_ID IS NULL) AND inv.DocStatus = 'CO') " +
				"left join C_Invoice nota  ON (nota.C_Invoice_ID = pb.XX_DOCUMENTNO_ID AND nota.XX_InvoiceType in ('A','S') " +
											 "AND (nota.C_Order_ID IS NULL AND nota.XX_Contract_ID IS NULL) AND nota.DocStatus = 'CO') "+
				"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) " +
				"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
				"left join C_DocType docN on (nota.C_DocTypeTarget_ID = docN.C_DocType_ID) " +
				"left join C_InvoiceLine ili on (inv.C_Invoice_ID = ili.C_Invoice_ID) " +
				"left join C_InvoiceLine iliN on (nota.C_Invoice_ID = iliN.C_Invoice_ID) " + 						
				"left join XX_ProductPercentDistrib ppdI ON (ppdI.C_InvoiceLine_ID = ili.C_InvoiceLine_ID) "+
				"left join XX_ProductPercentDistrib ppdN ON (ppdN.C_InvoiceLine_ID = iliN.C_InvoiceLine_ID) "+ 
				"left  join M_Product_Acct pacI on (ili.M_Product_ID = pacI.M_Product_ID) "+
				"left  join M_Product_Acct pacN on (iliN.M_Product_ID = pacN.M_Product_ID) "+ 
				"left join C_ElementValue elvI on (pacI.XX_PExpenses_ID = elvI.C_ElementValue_ID) "+
				"left join C_ElementValue elvN on (pacN.XX_PExpenses_ID = elvN.C_ElementValue_ID) "+ 
				"where pb.XX_Status='CO' and ((inv.isSoTrx = 'N' and inv.XX_InvoiceType in ('A','S')) or (nota.isSoTrx = 'N' and nota.XX_InvoiceType in ('A','S'))) " +
				"AND pb.XX_ControlNumber <> ' ' " +
				"AND TRUNC(pb.created) = to_date('"+ new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) +"','yyyy-MM-dd') " +
				//CCapote lo puse en comentario no se para que sirve 
				//"AND inv.C_DocTypeTarget_ID NOT IN ("+ Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTNEG_ID") +", "+ Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTPOS_ID") +") " +
				"and (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL) " +
				"GROUP BY inv.XX_InvoiceType, nota.XX_InvoiceType, par.C_BPartner_ID, par.name, doc.XX_CodDoc, docN.XX_CodDoc, inv.DateInvoiced, nota.DateInvoiced, " +
				"inv.XX_Contract_ID, ppdI.XX_Org_ID, ppdN.XX_Org_ID, " +
				"inv.XX_DueDate, nota.XX_DueDate, inv.XX_Month, inv.XX_Year, inv.C_Invoice_ID, nota.C_Invoice_ID, inv.C_Currency_ID, nota.C_Currency_ID, pb.XX_DocumentNo_ID, elvI.C_ElementValue_ID, " +
				"elvN.C_ElementValue_ID " +
				"ORDER BY par.NAME ");

			}else{
				if (linea != 5){
					m_sql.append("SELECT " +
								"ROUND((CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.XX_TaxAmount ELSE nota.XX_TaxAmount END), 2) as impuesto, "+
								"ROUND(pb.XX_WithHoldingTax, 2) as IVA, "+
								"NVL((select ROUND(sum(a.XX_RetainedAmount), 2) from XX_VCN_ISLRAMOUNT a where a.C_Invoice_ID = "+
							    "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID ELSE nota.C_Invoice_ID END)),0) as ISLR, "+
							    "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN doc.XX_CodDoc ELSE docN.XX_CodDoc END) XX_CodDoc, "+
								"to_char(inv.XX_DueDate,'dd/MM/yyyy') fve, substr(upper(par.name),1,40) name, "+
								"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.AD_Org_ID ELSE nota.AD_Org_ID END) AD_Org_ID, "+
								" par.C_BPartner_ID, " +
								"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID ELSE nota.C_Invoice_ID END) C_INVOICE_ID, "+
								"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Currency_ID ELSE nota.C_Currency_ID END) C_CURRENCY_ID, "+								
								"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.DateInvoiced,'dd/MM/yyyy') ELSE to_char(nota.DateInvoiced,'dd/MM/yyyy') END) fdo2, " +
								"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.XX_ApprovalDate,'dd/MM/yyyy') ELSE to_char(inv.XX_ApprovalDate,'dd/MM/yyyy') END) fdo34 " 
								);
				}else{
					m_sql.append("SELECT " +
								"ROUND((" +
								"((CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.TotalLines ELSE nota.TotalLines END) + "+
								"(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.XX_TaxAmount ELSE nota.XX_TaxAmount END)) "+
								" - " +
								 "coalesce(pb.XX_WithHoldingTax,0) - " +
								 "NVL((select ROUND(sum(a.XX_RetainedAmount), 2) from XX_VCN_ISLRAMOUNT a where a.C_Invoice_ID = "+
								    "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID ELSE nota.C_Invoice_ID END)),0)), 2) as total, " +
								 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN doc.XX_CodDoc ELSE docN.XX_CodDoc END) XX_CodDoc, "+
								 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.documentNo ELSE nota.documentNo END) documentNo, " +
								 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.DateInvoiced,'dd/MM/yyyy') ELSE to_char(nota.DateInvoiced,'dd/MM/yyyy') END) fdo, " +
								 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN to_char(inv.XX_DueDate,'dd/MM/yyyy') ELSE to_char(nota.XX_DueDate,'dd/MM/yyyy') END) fve, " +
								 " substr(upper(par.name),1,40) name, " +
								 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.AD_Org_ID ELSE nota.AD_Org_ID END) AD_Org_ID, "+
								 " par.C_BPartner_ID, " +
								 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Invoice_ID ELSE nota.C_Invoice_ID END) C_INVOICE_ID, " +
								 "(CASE WHEN pb.XX_DocumentNo_ID IS NULL THEN inv.C_Currency_ID ELSE nota.C_Currency_ID END) C_CURRENCY_ID "
								 );		
				}
				
				m_sql.append(
						" FROM XX_VCN_PurchasesBook pb "+
						"left join C_Invoice inv  ON (inv.C_Invoice_ID = pb.C_Invoice_ID AND inv.XX_InvoiceType in ('A','S') "+
						          "AND (inv.C_Order_ID IS NULL AND inv.XX_Contract_ID IS NULL) AND inv.DocStatus = 'CO') "+
						"left join C_Invoice nota  ON (nota.C_Invoice_ID = pb.XX_DOCUMENTNO_ID AND nota.XX_InvoiceType in ('A','S') "+ 
						          "AND (nota.C_Order_ID IS NULL AND nota.XX_Contract_ID IS NULL) AND nota.DocStatus = 'CO') "+  
						"inner join C_BPartner par ON (par.C_Bpartner_ID = pb.C_Bpartner_ID) "+ 
						"left join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) "+ 
						"left join C_DocType docN on (nota.C_DocTypeTarget_ID = docN.C_DocType_ID) "+ 
						"where pb.XX_Status='CO' and ((inv.isSoTrx = 'N' and inv.XX_InvoiceType in ('A','S')) or (nota.isSoTrx = 'N' and nota.XX_InvoiceType in ('A','S'))) " +
						"AND pb.XX_ControlNumber <> ' ' "+
						"AND TRUNC(pb.created) = to_date('"+ new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) +"','yyyy-MM-dd') " +
						//CCapote lo puse en comentario no se para que sirve 
						//"AND inv.C_DocTypeTarget_ID NOT IN ("+ Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTNEG_ID") +", "+ Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTPOS_ID") +") " +
						"and (inv.Description not in ('Ajuste al Costo','Descuento por Retraso en la Entrega Nacional') OR inv.Description IS NULL) "+
						"ORDER BY par.NAME"
						);
			}
			
			sql = m_sql.toString();
			StringBuffer m_sqlElmentUnion = new StringBuffer();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery(); 	
				while (rs.next()){		
					haber = new BigDecimal(0);
					debe = new BigDecimal(0);
					m_sqlElment = new StringBuffer();					
					m_sqlElment.append("select C_ElementValue_ID, value, " +
									   "(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
									   "where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null end) div, " +
									   "(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
									   "where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
									   " else null end) sec, " +
									   "(select XX_Suc from AD_Org where AD_Org_ID = " + Env.getCtx().getAD_Client_ID() + ") suc, ");
					
					if (linea == 1){
						m_sqlElment = new StringBuffer();	
						m_sqlElment.append("select C_ElementValue_ID, value, " +
								   			"(case when XX_Div = 'Y' then (SELECT XX_DIVISION FROM AD_ClientInfo " +
								   			"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
								   			" else null end) div, " +
								   			"(case when XX_Sec = 'Y' then (SELECT XX_SECTIONCODE FROM AD_ClientInfo " +
								   			"where AD_Client_ID = " + Env.getCtx().getAD_Client_ID() + ") " +
								   			" else null end) sec, " +
								   			"(select XX_Suc from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") suc, " +
								   			"(case when XX_Aux = 'Y' then " +
											   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
											   " else null " +
											   " end) aux, " +
								   			"(case when XX_Doc = 'Y' then " +
											   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " +
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
											   " else null " +
											   " end) tipdoc, " +
											   " (case when XX_Fdo = 'Y' then '" + rs.getString("fdo2") + "' " +
										   	   " else null " +
										       " end) fdo, " +
											   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, " +
								   			"(case when XX_Dep = 'Y' then " +
								   			"(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt(3) + ") " +
								   			" else null " +
								   			" end) dep " +
								   			"from C_ElementValue " +
								   			"where C_ElementValue_ID = " + rs.getInt("account"));
						if (rs.getString(5).equals("S"))
						{
							//RC 06062013 Se agrega el If length() == 5 Para nombres de proveedores pequeño
							if (rs.getString("name").length() == 5)
								description = rs.getString("name").substring(0, rs.getString("name").length()) + "/"+ rs.getString("ma");
							else	
								description = rs.getString("name").substring(0, rs.getString("name").length()-6) + "/"+ rs.getString("ma");
						
						} else
							description = rs.getString("name");
						if(rs.getBigDecimal("subTotal").compareTo(new BigDecimal(0)) > 0){
							debe = rs.getBigDecimal("subTotal");
						} else
							haber = rs.getBigDecimal("subTotal").multiply(BigDecimal.valueOf(-1));
						linea2++;
					}
					
					if (linea == 2 || linea == 3 || linea == 4){
						m_sqlElment.append("(case when XX_Dep = 'Y' then " +
										   "(select XX_CodDepAccount from AD_Org where AD_Org_ID = " + rs.getInt("AD_Org_ID") + ") " +
										   " else null " +
										   " end) dep, " +						   
										   "(case when XX_Aux = 'Y' then " +
										   "(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
										   " else null " +
										   " end) aux, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fve") + "' " +
										   " else null " +
										   " end) fve, ");
						
						if (linea == 2) {
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo2") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select substr(documentNo, 0, 10) from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " +
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then '" + rs.getString("XX_CodDoc") + "' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_Tax = 'Y' ");
							description = rs.getString("name");
							if(rs.getBigDecimal("impuesto").compareTo(new BigDecimal(0)) > 0){
								debe = rs.getBigDecimal("impuesto");
							} else
								haber = rs.getBigDecimal("impuesto").multiply(BigDecimal.valueOf(-1));
							if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)
								linea2++;
						}else if (linea == 3){
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then XX_WithHolding " +
										       " else null " + 
										       " end) doc, " +
										       "(case when XX_Doc = 'Y' then 'RET' " +
										       " else null " +
										       " end) tipdoc " +
									   			"from C_ElementValue a, XX_VCN_PurchasesBook b ");
					
					m_sqlElmentUnion = new StringBuffer();
					m_sqlElmentUnion.append(" UNION ");
					m_sqlElmentUnion.append(m_sqlElment);
									   
					m_sqlElment.append("inner join C_Invoice inv ON (inv.C_INVOICE_ID = b.C_INVOICE_ID) "+
									  "where b.XX_Status='CO' and AccountType = 'L' " +
									   "and XX_WithholdingTaxMarck = 'Y' AND XX_WITHHOLDING IS NOT NULL AND XX_DOCUMENTNO_ID IS NULL " +
									   "and (inv.XX_ApprovalDate between to_date('" + 
									   new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
									   "','yyyy-MM-dd') " +
									   "and to_date('" + 
							   new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
									   "','yyyy-MM-dd')) " + 
									   "AND inv.C_Invoice_ID = " + rs.getInt("C_Invoice_ID"));
					
					m_sqlElmentUnion.append(
							   "inner join C_invoice cred ON (cred.C_INVOICE_ID = b.XX_DOCUMENTNO_ID) "+
							   "where b.XX_Status='CO' and AccountType = 'L' " +
							   "and XX_WithholdingTaxMarck = 'Y' AND XX_WITHHOLDING IS NOT NULL AND XX_DOCUMENTNO_ID IS NOT NULL " +
							   "and to_char(b.created,'yyyy-mm-dd') =  '" + 
							   new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
									   "' and cred.XX_ControlNumber is not null and " +
									   "b.XX_DOCUMENTNO_ID = " + rs.getInt("C_Invoice_ID"));
					
					m_sqlElment.append(m_sqlElmentUnion);
							description = rs.getString("name");	
							if(rs.getBigDecimal("IVA").compareTo(new BigDecimal(0)) > 0){
								haber = rs.getBigDecimal("IVA");
							} else
								debe = rs.getBigDecimal("IVA").multiply(BigDecimal.valueOf(-1));
							if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)	
								linea2++;
						}else{
							m_sqlElment.append("(case when XX_Fdo = 'Y' then '" + rs.getString("fdo34") + "' " +
										   	   " else null " +
										       " end) fdo, " +
										       "(case when XX_Doc = 'Y' then " +
											   " (select XX_IslrReceiptNo from C_Invoice where C_Invoice_ID = " + 
											   		rs.getInt("C_Invoice_ID") + ") " +
											   " else null " + 
											   " end) doc, " +
											   "(case when XX_Doc = 'Y' then 'RET' " +
											   " else null " +
											   " end) tipdoc " +
											   "from C_ElementValue " +
											   "where AccountType = 'L' " +
											   "and XX_ISLR = 'Y' ");
							description = rs.getString("name");
							if (rs.getBigDecimal("ISLR").compareTo(new BigDecimal(0)) != 0){
								haber = rs.getBigDecimal("ISLR");
								debe = new BigDecimal(0);
								linea2++;
							}
						}
					}else if (linea == 5){
						m_sqlElment.append("null dep, " +
											"(case when XX_Aux = 'Y' then " +
											"(select value from C_BPartner where C_BPartner_ID = " + rs.getInt("C_BPartner_ID") + ") " +
											" else null " +
											" end) aux, " +
										   "(case when XX_Fdo = 'Y' then '" + rs.getString("fdo") + "' " +
										   " else null " +
										   " end) fdo, " +
										   "(case when XX_Fev = 'Y' then '" + rs.getString("fdo") + "' " +
										   " else null " +
										   " end) fve, " +
										   "(case when XX_Doc = 'Y' then '" + rs.getString("documentNo") + "' " +
										   " else null " +
										   " end) doc, " +
										   "(case when XX_Doc = 'Y' then 'FAC' " +
										   " else null " +
										   " end) tipdoc " +
										   "from C_ElementValue " +
										   "where M_Product_Category_ID = " + 
					   							Env.getCtx().getContextAsInt("#M_PRODUCT_CATEGORY_ID") + " " +
							   	   		   "and AccountType = 'L' ");
						description = rs.getString("name");
						if(rs.getBigDecimal("total").compareTo(new BigDecimal(0)) > 0){
							haber = rs.getBigDecimal("total");
						} else
							debe = rs.getBigDecimal("total").multiply(BigDecimal.valueOf(-1));
						if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)	
						linea2++;
					}
					if (debe.compareTo(BigDecimal.ZERO)!=0 || haber.compareTo(BigDecimal.ZERO)!=0)											
						if (!executeDetailCxPA(rs, m_sqlElment.toString(), linea2, headID, description, debe, haber, linea,false))
							return false;
						else{
							debeTotal = debeTotal.add(debe);
							haberTotal = haberTotal.add(haber);
						}
					
					/*
					 * Verificar Diferencias por Proveedor del debe y el haber.
					 */
					/*if(checkDiff.containsKey(rs.getString("name"))){
						Vector<BigDecimal> valuesOld = checkDiff.get(rs.getString("name"));
						checkDiff.remove(rs.getString("name"));
						BigDecimal debeAux = valuesOld.get(0);
						BigDecimal haberAux = valuesOld.get(1);
						
						Vector<BigDecimal> values = new Vector<BigDecimal>();
						values.add(debeAux.add(debe));
						values.add(haberAux.add(haber));
						checkDiff.put(rs.getString("name"), values);
					}
					else{
						Vector<BigDecimal> values = new Vector<BigDecimal>();
						values.add(debe);
						values.add(haber);
						checkDiff.put(rs.getString("name"), values);
					}*/
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			diff = haberTotal.subtract(debeTotal);
			
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			aEnt.setXX_TotalHave(haberTotal);
			aEnt.setXX_TotalShould(debeTotal);
			aEnt.save();
			
			linea++;
		}
		
		//Agrega el diferencial en decimales de los asientos
		if(diff.compareTo(BigDecimal.ZERO)!=0 && diff.abs().compareTo(BigDecimal.valueOf(2))<0)
		{
			boolean should = true;
			MFactAcct detailFactA = new MFactAcct(Env.getCtx(), detailDiff, get_Trx());
			
			if(detailFactA.getAmtAcctDr().compareTo(BigDecimal.ZERO)!=0 & detailFactA.getAmtSourceDr().compareTo(BigDecimal.ZERO)!=0){
				detailFactA.setAmtAcctDr(detailFactA.getAmtAcctDr().add(diff));
				detailFactA.setAmtSourceDr(detailFactA.getAmtSourceDr().add(diff));
			}
			else{
				detailFactA.setAmtAcctCr(detailFactA.getAmtAcctCr().add(diff.multiply(BigDecimal.valueOf(-1))));
				detailFactA.setAmtSourceCr(detailFactA.getAmtSourceCr().add(diff.multiply(BigDecimal.valueOf(-1))));
				should = false;
			}
			
			detailFactA.save();
			
			X_XX_VCN_AccoutingEntry aEnt = new X_XX_VCN_AccoutingEntry( Env.getCtx(), headID, get_Trx());
			if(should)
				aEnt.setXX_TotalShould(aEnt.getXX_TotalShould().add(diff));
			else
				aEnt.setXX_TotalHave(aEnt.getXX_TotalHave().add(diff.multiply(BigDecimal.valueOf(-1))));
			
			aEnt.save();
		}
		
		detailDiff = 0;
		
		//IMPRIMIR VECTOR DE DIFERENCIAS
		/*for(String element : checkDiff.keySet()) {
			Vector<BigDecimal> valuesOld = checkDiff.get(element);
			if(valuesOld.get(0).compareTo(valuesOld.get(1))!=0)
				System.out.println(element + ":  " + valuesOld.get(0) + " - " + valuesOld.get(1));
		}*/
		
		return true;
	}
	
	/**
	 * Procesamiento para los asientos de CxPE
	 * @param dateFrom Fecha desde
	 * @param dateTo Fecha hasta
	 * @param process Numero de procesamiento
	 * @param headID Identificador de la cabezera
	 * @return Success boolean
	 */
	public Boolean buildDetailCxPE_P1(Timestamp dateFrom, Timestamp dateTo, X_XX_VCN_AccoutingEntry accEntry){
		
		String sql = "";	
		BigDecimal result = BigDecimal.ZERO;
		
		Calendar date = Calendar.getInstance();
		date.setTime(dateFrom);
		
		String description = "O/C.IMPORTADAS " + (date.get(Calendar.MONTH)+1) +"-" + date.get(Calendar.YEAR);
		
		//Linea 1 (Debe)
		sql = "SELECT b.C_ElementValue_ID ELEMENT, " +
			  "CASE WHEN b.XX_SUC = 'Y' THEN (c.XX_SUC) ELSE NULL END SUC, " +
			  "CASE WHEN b.XX_DIV = 'Y' THEN (e.XX_DIVISION) ELSE NULL END DIV, " +
			  "CASE WHEN b.XX_DEP = 'Y' THEN (XX_CODDEPACCOUNT) ELSE NULL END DEP, " +
			  "CASE WHEN b.XX_SEC = 'Y' THEN (e.XX_SECTIONCODE) ELSE NULL END SEC, NULL AUX, " +
			  "CASE WHEN b.XX_DOC = 'Y' THEN (1) ELSE NULL END DOCNO, " +
			  "CASE WHEN b.XX_DOC = 'Y' THEN (xx_coddoc) ELSE NULL END DOCTYPE, " +
			  "TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') FECDOC, " +
			  "NULL FECVEN, NULL DVEDOC, " +
			  " " + accEntry.getXX_TotalShould() + " DEBE, 0 HABER, " +
			  "NULL TIPITF, NULL IMPAUT " +
			  "FROM C_ElementValue b, AD_ORG c, C_DOCTYPE d, AD_CLIENTINFO e " +
			  "WHERE " +
			  "d.C_DOCTYPE_ID = " + Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEORDER_ID") + " " + 
			  "AND b.C_ElementValue_ID IN " +
			  		"(SELECT C_ElementValue_ID FROM C_ElementValue WHERE M_Product_Category_ID = " + Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID") +
			  		 " AND XX_ELEMENTTYPE = 'Importada' AND ESTIMATED = 'Y' AND ACCOUNTTYPE = 'A') " +
			  "AND c.AD_ORG_ID = (SELECT AD_ORG_ID FROM AD_ORG WHERE XX_ACCPRINCIPAL = 'Y') " +
			  "AND c.AD_CLIENT_ID = e.AD_CLIENT_ID ";
		
		result = executeDetailPE(sql, 1, accEntry.get_ID(), description);
		
		if(result.compareTo(accEntry.getXX_TotalShould())!=0)
			return false;
		
		//Linea 2 (Haber)
		sql = "SELECT b.C_ElementValue_ID ELEMENT, " +
				  "CASE WHEN b.XX_SUC = 'Y' THEN (c.XX_SUC) ELSE NULL END SUC, " +
				  "CASE WHEN b.XX_DIV = 'Y' THEN (e.XX_DIVISION) ELSE NULL END DIV, " +
				  "CASE WHEN b.XX_DEP = 'Y' THEN (XX_CODDEPACCOUNT) ELSE NULL END DEP, " +
				  "CASE WHEN b.XX_SEC = 'Y' THEN (e.XX_SECTIONCODE) ELSE NULL END SEC, NULL AUX, " +
				  "CASE WHEN b.XX_DOC = 'Y' THEN (1) ELSE NULL END DOCNO, " +
				  "CASE WHEN b.XX_DOC = 'Y' THEN (xx_coddoc) ELSE NULL END DOCTYPE, " +
				  "CASE WHEN b.XX_FDO = 'Y' THEN TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
				  "ELSE NULL END FECDOC, " +
				  "CASE WHEN b.XX_FEV = 'Y' THEN TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
				  "ELSE NULL END FECVEN, " +
				  "NULL DVEDOC, " +
				  "0 DEBE, " + accEntry.getXX_TotalHave() + " HABER, " +
				  "NULL TIPITF, NULL IMPAUT " +
				  "FROM C_ElementValue b, AD_ORG c, C_DOCTYPE d, AD_CLIENTINFO e " +
				  "WHERE " +
				  "d.C_DOCTYPE_ID = " + Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEORDER_ID") + " " + 
				  "AND b.C_ElementValue_ID IN " +
				  		"(SELECT C_ElementValue_ID FROM C_ElementValue WHERE M_Product_Category_ID = " + Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID") +
				  		 " AND XX_ELEMENTTYPE = 'Importada' AND ESTIMATED = 'Y' AND ACCOUNTTYPE = 'A') " +
				  "AND c.AD_ORG_ID = (SELECT AD_ORG_ID FROM AD_ORG WHERE XX_ACCPRINCIPAL = 'Y') " +
				  "AND c.AD_CLIENT_ID = e.AD_CLIENT_ID ";	
		
		result = executeDetailPE(sql, 2, accEntry.get_ID(), description);
		
		if(result.compareTo(accEntry.getXX_TotalHave())!=0)
			return false;
		
		return true;
	}
	
	/**
	 * Procesamiento para los asientos de CxPE
	 * @param dateFrom Fecha desde
	 * @param dateTo Fecha hasta
	 * @param process Numero de procesamiento
	 * @param headID Identificador de la cabezera
	 * @return Success boolean
	 */
	public Boolean buildDetailCxPE_P2P3(int pNumber, Timestamp dateFrom, Timestamp dateTo, X_XX_VCN_AccoutingEntry accEntry, int orderID){
		
		String sql = "";	
		BigDecimal result = BigDecimal.ZERO;
		
		Calendar date = Calendar.getInstance();
		date.setTime(dateFrom);
		String description = "";
		
		if(pNumber == 2)
			description = "V/SUM.SERV. " + (date.get(Calendar.MONTH)+1) +"-" + date.get(Calendar.YEAR);
		else if(pNumber==3){
			
			MOrder order = new MOrder( Env.getCtx(), orderID, null);
			MBPartner partner = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
			description = partner.getName() + " HON. " + (date.get(Calendar.MONTH)+1) +"-" + date.get(Calendar.YEAR);
		}
		
		//Linea 1 (Debe)
		sql = "select f.C_ElementValue_ID ELEMENT," +
			  "CASE WHEN f.XX_SUC = 'Y' THEN (g.XX_SUC) ELSE NULL END SUC, " +
			  "CASE WHEN XX_DIV = 'Y' THEN (j.XX_DIVISION) ELSE NULL END DIV," +
			  "CASE WHEN XX_DEP = 'Y' THEN (g.XX_CODDEPACCOUNT) ELSE NULL END DEP, " +
			  "CASE WHEN XX_SEC = 'Y' THEN (j.XX_SECTIONCODE) ELSE NULL END SEC, " +
			  "CASE WHEN XX_AUX = 'Y' THEN (h.value) ELSE NULL END AUX, " +
			  "CASE WHEN XX_DOC = 'Y' THEN (a.DocumentNO) ELSE NULL END DOCNO, " +
			  "CASE WHEN XX_DOC = 'Y' THEN (i.xx_coddoc) ELSE NULL END DOCTYPE, " +
			  "CASE WHEN XX_FDO = 'Y' THEN (a.CREATED) ELSE NULL END FECDOC, " +
			  "CASE WHEN XX_FEV = 'Y' THEN (a.CREATED) ELSE NULL END FECVEN, " +
			  "NULL DVEDOC, ROUND(sum(XX_AMOUNTPERCC) * ("+rateCxPE+"),2) DEBE, sum(0) HABER, NULL TIPITF, NULL IMPAUT " +
			  "FROM c_order a, c_orderline b, XX_ProductPercentDistrib c, M_Product d, " +
			  "M_Product_Acct e, C_ElementValue f, AD_ORG g, C_BPartner H, C_DOCTYPE I, AD_CLIENTINFO J " +
			  "WHERE " + 
			  "a.c_order_ID = " + orderID + " " +
			  "AND a.C_Order_id = b.C_Order_id  AND b.C_OrderLine_ID = c.C_OrderLine_ID " +
			  "AND b.M_Product_ID = d.M_Product_ID  AND d.M_Product_ID = e.M_Product_ID " + 
			  "AND g.AD_ORG_ID = c.XX_ORG_ID  AND a.C_BPartner_ID = H.C_BPartner_ID " +
			  "AND a.C_DOCTYPE_ID = I.C_DOCTYPE_ID " +
			  "AND f.VALUE = (select substr(value,0,7)||'1900' from C_ELEMENTVALUE ev WHERE ev.C_ELEMENTVALUE_ID  = e.XX_PExpenses_ID) " +
			  "AND J.AD_CLIENT_ID = g.AD_CLIENT_ID " +	
			  "group by f.C_ElementValue_ID, f.XX_SUC, g.XX_SUC, XX_DIV, XX_DEP, g.XX_CODDEPACCOUNT, " +
			  "XX_SEC, XX_AUX, h.value, XX_DOC , a.DocumentNO, i.xx_coddoc, XX_FDO, a.CREATED, XX_FEV, j.XX_DIVISION, j.XX_SECTIONCODE ";
		
		result = executeDetailPE(sql, 1, accEntry.get_ID(), description);
		
		if(result.compareTo(accEntry.getXX_TotalShould())!=0)
			return false;
		
		//Linea 2 (Haber)
		sql = "SELECT b.C_ElementValue_ID ELEMENT, " +
			  "CASE WHEN b.XX_SUC = 'Y' THEN (c.XX_SUC) ELSE NULL END SUC, " +
			  "CASE WHEN b.XX_DIV = 'Y' THEN (e.XX_DIVISION) ELSE NULL END DIV, " +
			  "CASE WHEN b.XX_DEP = 'Y' THEN (XX_CODDEPACCOUNT) ELSE NULL END DEP, " +
			  "CASE WHEN b.XX_SEC = 'Y' THEN (e.XX_SECTIONCODE) ELSE NULL END SEC, NULL AUX, " +
			  "CASE WHEN XX_DOC = 'Y' THEN (1) ELSE NULL END DOCNO, " +
			  "CASE WHEN b.XX_DOC = 'Y' THEN (xx_coddoc) ELSE NULL END DOCTYPE, " +
			  "NULL FECDOC, NULL FECVEN, NULL DVEDOC, " +
			  "0 DEBE, ROUND(TOTALLINES * ("+rateCxPE+"),2) HABER, " +
			  "NULL TIPITF, NULL IMPAUT " +
			  "FROM c_order a, C_ElementValue b, AD_ORG c, C_DOCTYPE d, AD_CLIENTINFO e " +
			  "WHERE " +
			  "a.c_order_id = " + orderID + " " +
			  "AND a.C_DOCTYPE_ID = d.C_DOCTYPE_ID " +
			  "AND b.C_ElementValue_ID IN (SELECT C_ElementValue_ID FROM C_ElementValue WHERE ESTIMATED = 'Y' AND  ACCOUNTTYPE = 'L') " +
			  "AND c.AD_ORG_ID = (SELECT AD_ORG_ID FROM AD_ORG WHERE XX_ACCPRINCIPAL = 'Y') " +
			  "AND c.AD_CLIENT_ID = e.AD_CLIENT_ID";	
		
		result = executeDetailPE(sql, 2, accEntry.get_ID(), description);
		
		if(result.compareTo(accEntry.getXX_TotalHave())!=0)
			return false;
		
		return true;
	}
	
	
	/**
	 * Procesamiento para los asientos de CxPE
	 * @param dateFrom Fecha desde
	 * @param dateTo Fecha hasta
	 * @param process Numero de procesamiento
	 * @param headID Identificador de la cabezera
	 * @return Success boolean
	 */
	public Boolean buildDetailCxPE_P4(Timestamp dateFrom, Timestamp dateTo, X_XX_VCN_AccoutingEntry accEntry, int contractID){
		
		String sql = "";	
		BigDecimal result = BigDecimal.ZERO;
		
		Calendar date = Calendar.getInstance();
		date.setTime(dateFrom);

		String description = "CONTRATO " + (date.get(Calendar.MONTH)+1) +"-" + date.get(Calendar.YEAR);
		
		//Linea 1 (Debe)
		sql = "select b.C_ElementValue_ID ELEMENT, " +
			  "CASE WHEN b.XX_SUC = 'Y' THEN (f.XX_SUC) ELSE NULL END SUC, " +
			  "CASE WHEN b.XX_DIV = 'Y' THEN (h.XX_DIVISION) ELSE NULL END DIV, " +
			  "CASE WHEN b.XX_DEP = 'Y' THEN (f.XX_CODDEPACCOUNT) ELSE NULL END DEP, " + 
			  "CASE WHEN b.XX_SEC = 'Y' THEN (h.XX_SECTIONCODE) ELSE NULL END SEC, " +
			  "CASE WHEN b.XX_AUX = 'Y' THEN (g.value) ELSE NULL END AUX, " +
			  "CASE WHEN b.XX_DOC = 'Y' THEN (a.value) ELSE NULL END DOCNO, " +  
			  "CASE WHEN b.XX_DOC = 'Y' THEN ('') ELSE NULL END DOCTYPE, " +
			  "CASE WHEN b.XX_FDO = 'Y' THEN (a.CREATED) ELSE NULL END FECDOC, " +
			  "CASE WHEN b.XX_FEV = 'Y' THEN (a.CREATED) ELSE NULL END FECVEN, " +
			  "NULL DVEDOC, " +
			  "nvl(sum(CASE WHEN a.XX_CONTRACTTYPE = 'Nacional' THEN e.XX_CONTRACTAMOUNT " +
			  "ELSE e.XX_CONTRACTAMOUNT * (select sum(XX_RATE) from XX_VCN_PaymentDollars " +
			  "where XX_RATETYPE = 'S' AND XX_RATETYPESITME IS NULL " +
			  "AND TO_CHAR(DATE1) = TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') and rownum=1) " +
			  "END),0) DEBE," +
			  "0 HABER " + 
			  "FROM XX_Contract a, C_ElementValue b, XX_VCN_ESTIMATEDAPAYABLE c, XX_PayContract d, " + 
			  "XX_ContractDetail e, AD_ORG f, C_BPartner g, AD_CLIENTINFO h, XX_VCN_ContraCtInvoice i " +
			  "WHERE " +
			  "a.XX_Contract_ID = " + contractID + " " +
			  "AND a.XX_Contract_ID = c.XX_Contract_ID AND h.AD_CLIENT_ID = f.AD_CLIENT_ID " +
			  "AND XX_DATEESTIMATED between " +
			  "TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " +
			  "AND TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
			  "AND c.XX_PayContract_ID = d.XX_PayContract_ID AND d.XX_PayContract_ID = e.XX_PayContract_ID " +
			  "AND f.AD_ORG_ID = e.XX_COSTCENTER_ID AND a.XX_Contract_ID = i.XX_Contract_ID AND i.XX_VCN_ContraCtInvoice_ID=d.XX_VCN_ContraCtInvoice_ID AND i.C_BPartner_ID = g.C_BPartner_ID " +
		      "AND b.VALUE = (select substr(value,0,7)||'1900' from C_ELEMENTVALUE ev WHERE ev.C_ELEMENTVALUE_ID  = i.XX_PExpenses_ID) " +
		
			  "group by b.C_ElementValue_ID, b.XX_SUC, b.XX_DIV, b.XX_DEP, b.XX_SEC, b.XX_AUX, b.XX_DOC, b.XX_FDO, b.XX_FEV, " +
			  "f.XX_SUC, h.XX_DIVISION, f.XX_CODDEPACCOUNT, h.XX_SECTIONCODE, g.value, a.value, a.CREATED";
		
		result = executeDetailPE(sql, 1, accEntry.get_ID(), description);
		result = result.setScale( 2, BigDecimal.ROUND_DOWN);
		
		if(result.compareTo(accEntry.getXX_TotalShould())!=0){
			return false;
		}
		
		//Linea 2 (Haber)
		sql = "SELECT b.C_ElementValue_ID ELEMENT, " +
			  "CASE WHEN b.XX_SUC = 'Y' THEN (f.XX_SUC) ELSE NULL END SUC, " +
			  "CASE WHEN b.XX_DIV = 'Y' THEN (h.XX_DIVISION) ELSE NULL END DIV, " +
			  "CASE WHEN b.XX_DEP = 'Y' THEN (f.XX_CODDEPACCOUNT) ELSE NULL END DEP, " + 
			  "CASE WHEN b.XX_SEC = 'Y' THEN (h.XX_SECTIONCODE) ELSE NULL END SEC, " +
			  "CASE WHEN b.XX_AUX = 'Y' THEN (g.value) ELSE NULL END AUX, " +
			  "CASE WHEN b.XX_DOC = 'Y' THEN (a.value) ELSE NULL END DOCNO, " + 
			  "CASE WHEN b.XX_DOC = 'Y' THEN (' ') ELSE NULL END DOCTYPE, " +
			  "CASE WHEN b.XX_FDO = 'Y' THEN (a.CREATED) ELSE NULL END FECDOC, " +
			  "CASE WHEN b.XX_FEV = 'Y' THEN (a.CREATED) ELSE NULL END FECVEN, " +
			  "sum(0) DEBE, " +
			  "nvl(sum(CASE WHEN a.XX_CONTRACTTYPE = 'Nacional' THEN e.XX_CONTRACTAMOUNT " +
			  "ELSE e.XX_CONTRACTAMOUNT * (select sum(XX_RATE) from XX_VCN_PaymentDollars " +
			  "where XX_RATETYPE = 'S' AND XX_RATETYPESITME IS NULL " +
			  "AND TO_CHAR(DATE1) = TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') and rownum=1) " +
			  "END),0) HABER " +
			  "FROM XX_Contract a, C_ElementValue b, XX_VCN_ESTIMATEDAPAYABLE c, XX_PayContract d, " +
			  "XX_ContractDetail e, AD_ORG f, C_BPartner g, AD_CLIENTINFO h, XX_VCN_ContraCtInvoice i " +
			  "WHERE " +
			  "a.XX_Contract_ID = " + contractID + " " +
			  "AND a.XX_Contract_ID = c.XX_Contract_ID AND h.AD_CLIENT_ID = f.AD_CLIENT_ID " +
			  "AND XX_DATEESTIMATED between " +
			  "TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()) + "','yyyy-MM-dd') " +
			  "AND TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + "','yyyy-MM-dd') " +
			  "AND c.XX_PayContract_ID = d.XX_PayContract_ID AND d.XX_PayContract_ID = e.XX_PayContract_ID " +
			  "AND a.XX_Contract_ID = i.XX_Contract_ID AND i.XX_VCN_ContraCtInvoice_ID=d.XX_VCN_ContraCtInvoice_ID AND i.C_BPartner_ID = g.C_BPartner_ID " +
			  "AND b.C_ElementValue_ID IN (SELECT C_ElementValue_ID FROM C_ElementValue WHERE ESTIMATED = 'Y' AND  ACCOUNTTYPE = 'L') " +
			  "AND f.AD_ORG_ID = (SELECT AD_ORG_ID FROM AD_ORG WHERE XX_ACCPRINCIPAL = 'Y') " +
			
			  "group by b.C_ElementValue_ID, b.XX_SUC, b.XX_DIV, b.XX_DEP, b.XX_SEC, f.XX_SUC, b.XX_AUX, " +
			  "b.XX_DOC, b.XX_FDO, b.XX_FEV, h.XX_DIVISION, f.XX_CODDEPACCOUNT, h.XX_SECTIONCODE, g.value, a.value, a.CREATED";	
		
		result = executeDetailPE(sql, 2, accEntry.get_ID(), description);
		result = result.setScale( 2, BigDecimal.ROUND_DOWN);
		
		if(result.compareTo(accEntry.getXX_TotalHave())!=0)
			return false;
		
		return true;
	}
	

	
	/**
	 * Se encarga de generar el detalle, a apartir de la cabecera del asiento contable
	 * @param sqlDetail resulSet del query del detalle
	 * @param sql sql de la cuenta contable
	 * @param lineaAsiento número de línea
	 * @param headID identificador de la cabecera del asiento contable
	 * @return Success Boolean
	 */
	public BigDecimal executeDetailPE(String sql, int lineaAsiento, int headID, String descrition){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BigDecimal result = BigDecimal.ZERO;
		java.util.Date utilDate = new java.util.Date(); // fecha actual
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

		
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				detailFactA = new MFactAcct(Env.getCtx(), 0, get_Trx());
				detailFactA.set_Value("XX_VCN_AccoutingEntry_ID", headID);
				detailFactA.set_Value("XX_VCN_Line", lineaAsiento);
				detailFactA.setAccount_ID(rs.getInt("ELEMENT"));
				detailFactA.set_Value("XX_Office",rs.getString("SUC"));
				detailFactA.set_Value("XX_Division",rs.getString("DIV"));
				detailFactA.set_Value("XX_Departament",rs.getString("DEP"));
				detailFactA.set_Value("XX_SectionCode",rs.getString("SEC"));
				detailFactA.set_Value("XX_Aux",rs.getString("AUX"));
				if(rs.getString("DOCNO") != null)
					detailFactA.set_Value("DocumentNo",rs.getString("DOCNO"));
				detailFactA.set_Value("XX_DocumentType",rs.getString("DOCTYPE"));
				detailFactA.set_Value("XX_DocumentDate",rs.getTimestamp("FECDOC"));	
				detailFactA.set_Value("XX_DueDate",rs.getTimestamp("FECVEN"));
				detailFactA.setDescription(descrition);
				detailFactA.setAmtAcctDr(rs.getBigDecimal("DEBE"));
				detailFactA.setAmtSourceDr(rs.getBigDecimal("DEBE"));
				detailFactA.setAmtAcctCr(rs.getBigDecimal("HABER"));
				detailFactA.setAmtSourceCr(rs.getBigDecimal("HABER"));
				result = result.add(rs.getBigDecimal("DEBE")).add(rs.getBigDecimal("HABER"));
				
				detailFactA.setAD_Table_ID(318);//Ad_table_id de Invoice
				detailFactA.setC_AcctSchema_ID(searchScheme());//C_AcctSchema_ID del cliente
				//if(sqlDetail.getString("C_Currency_ID") != null)
				//	detailFactA.setC_Currency_ID(sqlDetail.getInt("C_Currency_ID"));//C_Currency_ID del bolivar
				detailFactA.setC_Period_ID(buscarPeriodoContable());//Periodo contable actual
				detailFactA.setDateAcct(sqlTimestamp);//Fecha actual
				detailFactA.setDateTrx(sqlTimestamp);//Fecha actual
				detailFactA.setPostingType("A");//PostingType
				//if(sqlDetail.getString("C_INVOICE_ID") != null)
				//	detailFactA.setRecord_ID(sqlDetail.getInt("C_INVOICE_ID"));//Id de la factura

				
				detailFactA.save();					
			}			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}		
		
		return result;
	}
	
	
	/**
	 * Se encarga de generar el detalle, a apartir de la cabecera del asiento contable
	 * @param sqlDetail resulSet del query del detalle
	 * @param sql sql de la cuenta contable
	 * @param lineaAsiento número de línea
	 * @param headID identificador de la cabecera del asiento contable
	 */
	public Boolean executeDetailCxPA(ResultSet sqlDetail, String sql, int lineaAsiento, int headID, 
			String description, BigDecimal debe, BigDecimal haber, int lineaOriginal, boolean consolidado){
		String msj = "", div = "", dep = "", sec = "", aux = "", doc = "", typeDoc = "";
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaDoc = null;
		Date fechaVen = null;
		java.util.Date utilDate = new java.util.Date(); // fecha actual
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean has = false;
		
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			
			while (rs.next()){
				
				has = true;
			
				if (processType.equals("I") && lineaAsiento == 1){					
					if (rs.getString("dep") != null){	
						dep = rs.getString("dep");
						msj = "DEP";
						System.out.println("La cuenta " + rs.getString(2) + " no debe tener tildada la opción DEP. CORREGIR ");
					}
					if (rs.getString("aux") != null){
						aux = rs.getString("aux");
						msj = msj + " ,AUX";
						System.out.println("La cuenta " + rs.getString(2) + " no debe tener tildada la opción AUX. CORREGIR ");
					}
					if (consolidado == false){
						if (rs.getString("doc") != null){
							doc = rs.getString("doc");
							typeDoc = sqlDetail.getString("XX_CodDoc");
							msj = msj + " ,DOC";
							System.out.println("La cuenta " + rs.getString(2) + " no debe tener tildada la opción DOC. CORREGIR ");
						}
					}
					if (!msj.equals("")){
						//enviar correo notificando que la cuenta xxx.xx.xxxx no debe tener lo que tiene la variable msj almacenada
						System.out.println("Enviar correo a contabilidad y soporte para que estén al tanto del error ");
						return false;
					}else
						flag = true;
				}else{ //Validaciones para bienes y servicios
					if (rs.getString("dep") != null)
						dep = rs.getString("dep");
					if (rs.getString("sec") != null)
						sec = rs.getString("sec");
					flag = true;
				}

				if (flag){
					if (rs.getString("div") != null)
						div = rs.getString("div");
					if (rs.getString("sec") != null)
						sec = rs.getString("sec");
					if (rs.getString("doc") != null){
						doc = rs.getString("doc");
						typeDoc = rs.getString("tipdoc");
					}
					if (rs.getString("aux") != null)
						aux = rs.getString("aux");

					detailFactA = new MFactAcct(Env.getCtx(), 0, get_Trx());
					detailFactA.set_Value("XX_VCN_AccoutingEntry_ID", headID);
					detailFactA.set_Value("XX_VCN_Line", lineaAsiento);
					detailFactA.setAccount_ID(rs.getInt("C_ELEMENTVALUE_ID"));
					detailFactA.set_Value("XX_Division",div);
					detailFactA.set_Value("XX_Departament",dep);
					detailFactA.set_Value("XX_SectionCode",sec);
					detailFactA.set_Value("XX_Aux",aux);
					detailFactA.set_Value("XX_Office",rs.getString("suc"));
					
					//Numero del Doc max 10
					if(doc!=null)
						if(doc.length()>10)
							detailFactA.set_Value("DocumentNo",doc.substring(0,10));
						else
							detailFactA.set_Value("DocumentNo",doc);
					
					detailFactA.set_Value("XX_DocumentType",typeDoc);
					
					if (!processType.equals("I")){	
						//RC y CC 1062013 No entraba en la 1era linea
						//if (lineaOriginal != 1){
							if(consolidado == false)
								detailFactA.set_Value("C_Invoice_ID",sqlDetail.getInt("C_Invoice_ID"));
							else
								detailFactA.set_Value("C_Invoice_ID",null);
							
							if (rs.getString("fve") != null){
								fechaVen = sdf.parse(rs.getString("fve"));
								detailFactA.set_Value("XX_DueDate",new Timestamp(fechaVen.getTime()));
							}
							if (rs.getString("fdo") != null){
								fechaDoc = sdf.parse(rs.getString("fdo"));
								detailFactA.set_Value("XX_DocumentDate",new Timestamp(fechaDoc.getTime()));	
							}						
						//}
					}else{
						if (lineaOriginal == 2 | lineaOriginal == 3)
							if (consolidado == false)
								detailFactA.set_Value("C_Invoice_ID",sqlDetail.getInt("C_Invoice_ID"));
							else
								detailFactA.set_Value("C_Invoice_ID",null);
						if (lineaOriginal != 1){
							fechaDoc = sdf.parse(rs.getString("fdo"));
							detailFactA.set_Value("XX_DocumentDate",new Timestamp(fechaDoc.getTime()));
							
							if(rs.getString("fve")!=null){
								fechaVen = sdf.parse(rs.getString("fve"));
								detailFactA.set_Value("XX_DueDate",new Timestamp(fechaVen.getTime()));
							}
						}
					}
					detailFactA.setDescription(description);
					detailFactA.setAmtAcctDr(debe);
					detailFactA.setAmtSourceDr(debe);
					detailFactA.setAmtAcctCr(haber);
					detailFactA.setAmtSourceCr(haber);
					detailFactA.setAD_Table_ID(318);//Ad_table_id de Invoice
					detailFactA.setC_AcctSchema_ID(searchScheme());//C_AcctSchema_ID del cliente
					if (consolidado == false){
						if(sqlDetail.getString("C_Currency_ID") != null)
							detailFactA.setC_Currency_ID(sqlDetail.getInt("C_Currency_ID"));//C_Currency_ID del bolivar
					}else
							detailFactA.setC_Currency_ID(205);//C_Currency_ID del bolivar
					detailFactA.setC_Period_ID(buscarPeriodoContable());//Periodo contable actual
					detailFactA.setDateAcct(sqlTimestamp);//Fecha actual
					detailFactA.setDateTrx(sqlTimestamp);//Fecha actual
					detailFactA.setPostingType("A");//PostingType
					if (consolidado == false){
						if(sqlDetail.getString("C_INVOICE_ID") != null)
							detailFactA.setRecord_ID(sqlDetail.getInt("C_INVOICE_ID"));//Id de la factura
					}
					
					detailFactA.save();
					System.out.println("----------------");
					System.out.println("ID DEL DETALLE: "+detailFactA.getFact_Acct_ID());
					System.out.println("ID DE LA CABECERA: "+detailFactA.get_ValueAsInt("XX_VCN_AccoutingEntry_ID"));
					System.out.println("CREADO El: "+detailFactA.getCreated().toString());
					
					if(detailDiff == 0)
						detailDiff = detailFactA.get_ID();
				}				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return has;
	}

	
	/**
	 * Busca el número de comprobante siguiente, correspondiente a la interfaz CX017
	 * @return número de comprobante
	 */
	public int numberControl(){
		int number = 0;
		String sql_comprobante = "select XX_NUMCOMPROBANTECX017.NEXTVAL from dual";
		PreparedStatement ps_comprobante = null;
		ResultSet rs_comprobante = null;
		try{
			ps_comprobante = DB.prepareStatement(sql_comprobante, null);
			rs_comprobante = ps_comprobante.executeQuery();
			rs_comprobante.next();
			number = rs_comprobante.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return number;
	}
	
	/**
	 * Retorna el número de días que se le deben restar a la fecha actual para generar el asiento. 
	 * Se utiliza siempre y cuando el día sea el último día del mes
	 * @param week día de la semana
	 * @return
	 */
	public int numberDays(int week){
		int days = 0;
		
		switch(week){
		case 1: //Domingo
			days = 6;
			break;
		case 2: //Lunes
			days = 0;
			break;
		case 3: //Martes
			days = 1;
			break;
		case 4: //Miércoles
			days = 2;
			break;
		case 5: //Jueves
			days = 3;
			break;
		case 6: //Viernes
			days = 4;
			break;
		case 7: //Sábado
			days = 5;
			break;
		}
		return days;
	}
	
	/**
	 * Se encarga de buscar el dolar promedio sitme, de la máxima fecha predeterminada
	 * @param dateTo fecha hasta
	 */
	public BigDecimal averageDollar(Timestamp dateTo){
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(dateTo);
		BigDecimal dollar = new BigDecimal(0);
		
		String sql = "select XX_AverageExchange " +
					 "from XX_VCN_PaymentDollars " +
					 "where date1 = " +
					 "(" +
					 	"select max(date1)  " +
					 	"from XX_VCN_PaymentDollars " +
					 	"where XX_RateType = 'S' " +
					 	"and date1 <= to_date('" + 
						 		new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()) + 
					 			"','yyyy-MM-dd') " +
					 ")";

		PreparedStatement psmt = null;
		ResultSet rs = null;
		try{
			psmt = DB.prepareStatement(sql, null);
			rs = psmt.executeQuery();
			rs.next();
			dollar = rs.getBigDecimal(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dollar;
	}
	
	/**
	 * Obtiene la lista de todos los alamacenes
	 * @author Gustavo Briceño
	 * @return Un arreglo con todos los M_Warehouse_ID del sistema
	 * 
	 */
	public int[] getWarehouses(){
		String sql = "select count(M_Warehouse_id) from M_Warehouse where AD_CLient_ID = " + m_AD_Client_ID;		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);	
		ResultSet rs;
		int size = 0;
		
		try {
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				size = rs.getInt(1);
			}			
			
			rs.close();		
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sql = "select M_Warehouse_id from M_Warehouse where AD_CLient_ID = " + m_AD_Client_ID;			
		pstmt = DB.prepareStatement(sql, null);			
		
		int warehouses[] = null;
		
		try {
			rs = pstmt.executeQuery();
			int i = 0;
			warehouses = new int[size]; 
			
			while(rs.next()){
				warehouses[i] = rs.getInt(1);
				i++;
			}			
			
			rs.close();		
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return warehouses;
	}
	
	/**
	 * Obtiene el nombre de un almacen
	 * @author Gustavo Briceño
	 * @param Warehouse_id El id del alamacen.
	 * @return El nombre del almacen.
	 */
	public String getWarehouse(int Warehouse_id){
		String result = "";
		String sql = "Select value from M_Warehouse where M_Warehouse_ID = " + Warehouse_id + " and AD_Client_id = " + m_AD_Client_ID;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				result = rs.getString(1);
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return result;
	}
	
	/**
	 * Obtiene el ultimo mes del periodo actual
	 * @return El mes en formato de numero entero
	 */
	public int getMonth(){
		int result = 0;		
		
		String sql = "select to_char(max(startdate),'mm') " +
				"from c_period prd, c_year yrd, c_calendar cld " +
				"where prd.c_year_id = yrd.c_year_id " +
				"and yrd.c_calendar_id = cld.c_calendar_id " +
				"and cld.name like '%Calendar' " +
				"and yrd.fiscalyear like '%"+year+"%'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				result = rs.getInt(1);
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	/**
	 * Elimina los datos de un posible asiento anterior del mismo mes.
	 */
	public void deletePast(){
		String sql = "select dae.FACT_ACCT_ID " +
				"from  FACT_ACCT dae " +
				"where dae.XX_VCN_ACCOUTINGENTRY_ID in ( " +
				"select distinct ace.XX_VCN_ACCOUTINGENTRY_ID " +
				"from XX_VCN_ACCOUTINGENTRY ace " +
				"where ace.datefrom = to_date('01-"+month+"-"+year+"','dd-mm-yyyy') " +
				"and ace.xx_typetransfersp is not null)";
		
		String sql2 = "select distinct ace.XX_VCN_ACCOUTINGENTRY_ID " +
				"from XX_VCN_ACCOUTINGENTRY ace " +
				"where ace.datefrom = to_date('01-"+month+"-"+year+"','dd-mm-yyyy') " +
				"and ace.xx_typetransfersp is not null";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			
			while(rs.next()){
				detailFactA = new MFactAcct(getCtx(), rs.getInt(1), get_Trx());
				detailFactA.delete(true, get_Trx());
			}
			
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			pstmt = DB.prepareStatement(sql2, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				accountingEntry = new X_XX_VCN_AccoutingEntry(Env.getCtx(), rs.getInt(1), get_Trx());
				accountingEntry.delete(true, get_Trx());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Se encarga de buscar la tasa del Dolar, de la máxima fecha predeterminada
	 * @param dateTo fecha hasta
	 */
	public BigDecimal rateDollar(Timestamp date){
		GregorianCalendar calTo = new GregorianCalendar();
		calTo.setTime(date);
		BigDecimal dollar = new BigDecimal(0);
		
		String sql = "select XX_RATE " +
					 "from XX_VCN_PaymentDollars " +
					 "where XX_RATETYPE = 'S' AND XX_RATETYPESITME IS NULL AND TO_CHAR(DATE1) = TO_DATE('" + 
						 		new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()) + 
					 			"','yyyy-MM-dd') ";

		PreparedStatement psmt = null;
		ResultSet rs = null;
		try{
			psmt = DB.prepareStatement(sql, null);
			rs = psmt.executeQuery();
			if(rs.next())
				dollar = rs.getBigDecimal(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dollar;
	}
	
	/**
	 * Se encarga de buscar el C_ACCTSCHEMA de la compañia
	 * Ivan Valdes Migliore
	 */
	public int searchScheme(){
		
		String sql = "SELECT C_ACCTSCHEMA_ID FROM C_ACCTSCHEMA";
		int id=0;

		PreparedStatement psmt = null;
		ResultSet rs = null;
		try{
			psmt = DB.prepareStatement(sql, null);
			rs = psmt.executeQuery();
			if(rs.next())
				id = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	
	/*
	 * METODO QUE SE ENCARGA DE BUSCAR EL PERIODO ACTUAL
	 */
	private int buscarPeriodoContable() {
		// TODO Auto-generated method stub
		int id = 0;
		Calendar c1 = Calendar.getInstance();
		int mes = c1.get(Calendar.MONTH);
		mes=mes+1;
		String mesletra = "";
		int year = c1.get(Calendar.YEAR);

		if (mes == 1)
			mesletra = "Enero";
		else if (mes == 2)
			mesletra = "Febrero";
		else if (mes == 3)
			mesletra = "Marzo";
		else if (mes == 4)
			mesletra = "Abril";
		else if (mes == 5)
			mesletra = "Mayo";
		else if (mes == 6)
			mesletra = "Junio";
		else if (mes == 7)
			mesletra = "Julio";
		else if (mes == 8)
			mesletra = "Agosto";
		else if (mes == 9)
			mesletra = "Septiembre";
		else if (mes == 10)
			mesletra = "Octubre";
		else if (mes == 11)
			mesletra = "Noviembre";
		else if (mes == 12)
			mesletra = "Diciembre";

		mesletra = mesletra + " " + year;
		String mesletraMayuscula = mesletra.toUpperCase();

		String sql = "select c_period_id" + " from c_period"
				+ " where name = '" + mesletra + "' OR name= '"
				+ mesletraMayuscula + "'";
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return id;
	}
}
