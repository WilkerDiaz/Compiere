package compiere.model.payments.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_Invoice;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MInvoiceLine;
import compiere.model.cds.MVCNApplicationNumber;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VCN_PercenReten;
import compiere.model.cds.X_XX_VCN_PurchasesBook;
import compiere.model.importcost.X_XX_VLO_SUMMARY;

/**
 * 
 * @author Jessica Mendoza
 *
 */
public class XX_CompleteAdjustments extends SvrProcess {
	
	Utilities util = new Utilities();

	@Override
	protected String doIt() throws Exception {

		int idInvoice;
		int count = 0;
		int aux = 0;
		Boolean bool = true;
		BigDecimal monto = new BigDecimal(0);
		BigDecimal impuesto = new BigDecimal(0);
		X_XX_VLO_SUMMARY summary = new X_XX_VLO_SUMMARY(Env.getCtx(), getRecord_ID(), null);

		if (summary.isXX_Visible()){
			generateReport(summary.getXX_VLO_Summary_ID());
		}else{
			if ((summary.getXX_Cosant() == null) || 
					(summary.getXX_CostFree() == null) || 
					(summary.getXX_CostNotSubject() == null)){
				//System.out.println("Debe agregar algún monto para el ajuste");
				ADialog.error(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_FieldsComplete"));
			}else{
				if (summary.getXX_Cosant().compareTo(new BigDecimal(0))!=0 && 
						summary.getXX_CostFree().compareTo(new BigDecimal(0))==0 &&
						summary.getXX_CostNotSubject().compareTo(new BigDecimal(0))==0){
					monto = summary.getXX_Cosant();
					aux = 1;
				}else if (summary.getXX_Cosant().compareTo(new BigDecimal(0))==0 && 
							summary.getXX_CostFree().compareTo(new BigDecimal(0))!=0 &&
							summary.getXX_CostNotSubject().compareTo(new BigDecimal(0))==0){
					monto = summary.getXX_CostFree();
					aux = 2;
				}else if (summary.getXX_Cosant().compareTo(new BigDecimal(0))==0  && 
							summary.getXX_CostFree().compareTo(new BigDecimal(0))==0  &&
							summary.getXX_CostNotSubject().compareTo(new BigDecimal(0))!=0){
					monto = summary.getXX_CostNotSubject();	
					aux = 3;
				}else{
					//System.out.println("No pueden haber dos montos");
					bool = false;
					ADialog.error(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_NotCanAmount"));					
				}

				if (bool){
					if (monto.compareTo(new BigDecimal(0)) == -1){
						/****Nota de Crédito****/
						//System.out.println("nota de crédito");
						idInvoice = crearAjusteFactura(summary,"#XX_L_C_DOCTYPECREDIT_ID",aux);
						crearAjusteLCompra(summary, idInvoice,aux);
					}else{
						/****Nota de Débito****/
						//System.out.println("nota de débito");
						idInvoice = crearAjusteFactura(summary, "#XX_L_C_DOCTYPEDEBIT_ID",aux);
						crearAjusteLCompra(summary, idInvoice,aux);		
					}
						
					/****Verificar si está marcada la opción de distribución por tienda, y el 
					 * ajuste es de tienda 001, para distribuir el monto con los % por tienda****/
					MWarehouse war = new MWarehouse(Env.getCtx(),summary.getM_Warehouse_ID(), null);
					if ((summary.isXX_DistributionByStore()) && (!war.get_ValueAsBoolean("XX_IsStore"))){
						
						/****Crear para los 8 tiendas el monto distribuido, en la tabla Summary, 
						 * y modificar la que ya ha sido creada****/
						String sql = "select M_Warehouse_ID, rate from XX_VSI_DistImportCosts";
						impuesto = summary.getXX_VLO_TaxCost();
						PreparedStatement pstmt = null; 
						ResultSet rs = null;
						try{
							pstmt = DB.prepareStatement(sql, null); 
							rs = pstmt.executeQuery();
							while (rs.next()){
								count = count + 1;
								if (count == 1){
									summary.setM_Warehouse_ID(rs.getInt("M_Warehouse_ID"));
									if (aux == 1)
										summary.setXX_Cosant(monto.multiply(rs.getBigDecimal("rate")).divide(new BigDecimal(100)));
									else if (aux == 2)
										summary.setXX_CostFree(monto.multiply(rs.getBigDecimal("rate")).divide(new BigDecimal(100)));
									else if (aux == 3)
										summary.setXX_CostNotSubject(monto.multiply(rs.getBigDecimal("rate")).divide(new BigDecimal(100)));
									summary.setXX_VLO_TaxCost(summary.getXX_VLO_TaxCost().multiply(rs.getBigDecimal("rate")).divide(new BigDecimal(100)));
									summary.setXX_Visible(true);
									summary.save();
								}else
									crearAjusteDistribuido(rs.getInt("M_Warehouse_ID"),
											rs.getBigDecimal("rate"),summary,monto,impuesto,aux);		
							}
							
						}catch (Exception e) {
							log.log(Level.SEVERE, sql);
						}finally{
							rs.close();
							pstmt.close();
						}
						
						commit();
						generateReport(summary.getXX_VLO_Summary_ID());
					}else{
						summary.setXX_Visible(true);
						summary.save();
					}
				}
			}
		}
		
		return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");
	}

	@Override
	protected void prepare() {
		
	}
	
	/**
	 * Se encarga de generar el reporte de ajustes
	 * @param controlNo numero de control
	 */
	public void generateReport(int summary){
		String designName = "CheckSetting";

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("summary");
		myReport.parameterValue.add(String.valueOf(summary));
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}

	/**
	 * Se encarga de crear el ajuste positivo o negativo en factura
	 * @param summary
	 * @param tipoDocumento tipo de documento a crear
	 * @return id de la factura
	 * @throws ParseException
	 */
	public int crearAjusteFactura(X_XX_VLO_SUMMARY summary, String tipoDocumento, int aux) throws ParseException{
		
		int idInvoice = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS"); 
		Date fechaRegistro = sdf.parse(summary.getXX_OrderDate()); 
		Timestamp fecha = new Timestamp(fechaRegistro.getTime());
		String tipoFactura = "I";
		Vector<String> datosRequeridos = new Vector<String>(5);
		datosRequeridos = datos(summary.getC_Order_ID());
		if (!datosRequeridos.get(2).equals("POM"))
			//mandar un mensaje de error, porque no se puede crear un ajuste con una O/C de bienes o servicios
			ADialog.error(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_NotSetting"));
		else{
			MInvoice invoice = new MInvoice(Env.getCtx(),0,get_TrxName());			
			invoice.setAD_Client_ID(summary.getAD_Client_ID());
			invoice.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCONTROL_ID"));
			invoice.setC_BPartner_ID(summary.getC_BPartner_ID());
			invoice.setC_PaymentTerm_ID(summary.getC_PaymentTerm_ID());
			invoice.setDateInvoiced(summary.getXX_DocumentDate());
			invoice.setC_Order_ID(summary.getC_Order_ID());
			//invoice.setXX_VMR_Department_ID(summary.getXX_VMR_Department_ID());
			//invoice.setM_Warehouse_ID(summary.getM_Warehouse_ID());
			invoice.setXX_AccountPayableStatus("A");
			invoice.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt(tipoDocumento));
			invoice.setXX_DueDate(summary.getXX_DocumentDate());
			invoice.setXX_SynchronizationBank(false);
			invoice.setDescription("Ajuste al Costo");
			invoice.setC_BPartner_Location_ID(Integer.parseInt(datosRequeridos.get(0)));
			invoice.setC_Currency_ID(Integer.parseInt(datosRequeridos.get(1)));
			invoice.setXX_InvoiceType(tipoFactura);
			invoice.setXX_ControlNumber(summary.getXX_ControlNumber());
			invoice.setDateOrdered(fecha);
			invoice.setXX_DatePaid(summary.getXX_DocumentDate());
			invoice.setDocumentNo(summary.getXX_NoteCreditDebit());
			invoice.setDateAcct(summary.getXX_DocumentDate());
			invoice.setM_PriceList_ID(Env.getCtx().getContextAsInt("#M_PriceList_ID"));
			invoice.setXX_ApprovalDate(summary.getXX_DateRegistration());
			
			//invoice.setTotalLines(summary.getXX_Cosant().add(summary.getXX_CostFree()).add(summary.getXX_CostNotSubject())); //Base
			invoice.setXX_TaxAmount(summary.getXX_VLO_TaxCost()); //Monto del Impuesto
			//invoice.setGrandTotal(invoice.getTotalLines().add(summary.getXX_VLO_TaxCost())); //Total de la Factura
			
			invoice.setIsSOTrx(false);
			invoice.save();
			idInvoice = invoice.getC_Invoice_ID();
			
			if (Integer.parseInt(datosRequeridos.get(1)) == 205)
				invoice.setXX_GrandTotalLocal(invoice.getGrandTotal().
						subtract(new BigDecimal(util.retencion(idInvoice))));
			else
				invoice.setXX_GrandTotalLocal(new BigDecimal(0));

			//Crear Linea
			MInvoiceLine line = new MInvoiceLine( Env.getCtx(), 0, get_TrxName());
			line.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCONTROL_ID"));
			line.setC_Invoice_ID(invoice.getC_Invoice_ID());
			line.setQty(BigDecimal.ONE);
			line.setC_Tax_ID(summary.getC_Tax_ID());
			line.setPriceActual(summary.getXX_Cosant().add(summary.getXX_CostFree()).add(summary.getXX_CostNotSubject()));
			line.save();
			
			invoice.setDocAction(X_C_Invoice.DOCACTION_Complete);
			DocumentEngine.processIt(invoice, X_C_Invoice.DOCACTION_Complete);
			invoice.save();
		}
		
		return idInvoice;
	}

	/**
	 * Se encarga de crear el ajuste positivo o negativo en el libro de compra, 
	 * y además se valida que si la opción de Retención está marcada, se genera 
	 * la misma dependiendo de la fecha de registro y de la factura del período impositivo del mes actual
	 * @param summary
	 * @param idInvoice id de la factura
	 */
	public void crearAjusteLCompra(X_XX_VLO_SUMMARY summary, int idInvoice, int aux){
		
		BigDecimal retencion = new BigDecimal(0);
		BigDecimal perc = BigDecimal.ZERO;
		
		if (summary.isXX_WithholdingTaxMarck()){
			
			MBPartner part = new MBPartner( Env.getCtx(), summary.getC_BPartner_ID(), null);
			X_XX_VCN_PercenReten ret = null;
			
			if(part.getXX_PercentajeRetention_ID()>0){
				ret = new X_XX_VCN_PercenReten( Env.getCtx(), part.getXX_PercentajeRetention_ID(), null);
				perc = ret.getXX_PERCENRETEN();
			}
			
			retencion = summary.getXX_VLO_TaxCost().multiply(perc).divide(new BigDecimal(100));
		
		}else{
			//no se genera retencion, es decir, que se coloca en cero
		}

		X_XX_VCN_PurchasesBook pBook = new X_XX_VCN_PurchasesBook(Env.getCtx(),0,get_TrxName());
		pBook.setAD_Client_ID(summary.getAD_Client_ID());
		pBook.setAD_Org_ID(summary.getAD_Org_ID());
		pBook.setM_Warehouse_ID(summary.getM_Warehouse_ID());
		pBook.setC_Invoice_ID(summary.getC_Invoice_ID());
		pBook.setC_BPartner_ID(summary.getC_BPartner_ID());
		pBook.setC_Order_ID(summary.getC_Order_ID());
		pBook.setXX_DocumentDate(summary.getXX_DocumentDate());
		pBook.setXX_ControlNumber(summary.getXX_ControlNumber());
		pBook.setXX_isManual(true);		
		pBook.setXX_TaxableBase(summary.getXX_Cosant()); //Base Imponible
		pBook.setXX_ExemptBase(summary.getXX_CostFree()); //Base Exenta
		pBook.setXX_NotSubjectBase(summary.getXX_CostNotSubject()); //Base no Sujeta
		pBook.setXX_TaxAmount(summary.getXX_VLO_TaxCost()); //Monto del Impuesto
		//pBook.setC_Tax_ID(buscarIvaActual()); //Impuesto
		pBook.setC_Tax_ID(summary.getC_Tax_ID());
		pBook.setXX_DATE(summary.getXX_DateRegistration()); //Fecha
		pBook.setXX_DocumentNo_ID(idInvoice);
		pBook.setXX_DocumentNo(summary.getXX_NoteCreditDebit());
		
		if (aux == 1){
			pBook.setXX_TotalInvCost(summary.getXX_Cosant().add(summary.getXX_VLO_TaxCost())); //Total del Documento
		}else if (aux == 2){
			pBook.setXX_TotalInvCost(summary.getXX_CostFree()); //Total del Documento
		}else if (aux == 3){
			pBook.setXX_TotalInvCost(summary.getXX_CostNotSubject()); //Total del Documento
		}
		
		if (util.clientRetentionAgent(getAD_Client_ID()) && summary.isXX_WithholdingTaxMarck() && retencion.compareTo(BigDecimal.ZERO)!=0){
			// Generating Application Number
			MVCNApplicationNumber applNum = new MVCNApplicationNumber(Env.getCtx(), 0, null);
			Date utilDate = new Date(); //actual date
			long lnMilisegundos = utilDate.getTime();
			Timestamp fechaActual = new Timestamp(lnMilisegundos);
			int apNum = applNum.generateApplicationNumber(fechaActual, 0, false, get_TrxName()); 
			pBook.setXX_Withholding(apNum);		
			pBook.setXX_WithholdingTax(retencion); //Total del Impuesto Retenido
		}
		else
			pBook.setXX_WithholdingTax(new BigDecimal(0));
		pBook.save();
	
	}
	
	/**
	 * Se encarga de buscar información necesaria para crear el ajuste en la factura
	 * @param idOrder id de la orden de compra
	 * @return un vector con la información requerida
	 */
	public Vector<String> datos(int idOrder){
		Vector<String> vector = new Vector<String>(5);
		String sql = "select bpl.C_BPartner_Location_ID, ord.C_Currency_ID, ord.XX_POType " +
					 "from C_Order ord, C_BPartner par, C_BPartner_Location bpl " +
					 "where ord.C_Order_ID = " + idOrder + " " +
					 "and bpl.C_BPartner_ID = par.C_BPartner_ID " +
					 "and par.C_BPartner_ID = ord.C_BPartner_ID ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				vector.add(String.valueOf(rs.getInt("C_BPartner_Location_ID")));
				vector.add(String.valueOf(rs.getInt("C_Currency_ID")));
				vector.add(rs.getString("XX_POType"));
				//vector.add(rs.getString("name"));
				//vector.add(rs.getString("XX_PurchaseType"));
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
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
		
		return vector;
	}
	
	/**
	 * Se encarga de crear el ajuste en la tabla XX_VLO_SUMMARY, con las diferentes 
	 * tiendas, con sus correspondientes porcentaje en el monto original
	 * @param idTienda id de la tienda
	 * @param porcentaje porcentaje a aplicar al monto
	 * @param summary
	 */
	public void crearAjusteDistribuido(int idTienda, BigDecimal porcentaje, 
			X_XX_VLO_SUMMARY summary, BigDecimal monto, BigDecimal tax, int aux){

		X_XX_VLO_SUMMARY summaryNew = new X_XX_VLO_SUMMARY(Env.getCtx(),0,get_TrxName());
		
		summaryNew.setAD_Client_ID(summary.getAD_Client_ID());
		summaryNew.setAD_Org_ID(summary.getAD_Org_ID());
		summaryNew.setXX_VMR_Department_ID(summary.getXX_VMR_Department_ID());
		summaryNew.setC_Order_ID(summary.getC_Order_ID());
		summaryNew.setC_Invoice_ID(summary.getC_Invoice_ID());
		summaryNew.setC_PaymentTerm_ID(summary.getC_PaymentTerm_ID());
		summaryNew.setC_BPartner_ID(summary.getC_BPartner_ID());
		summaryNew.setC_Country_ID(summary.getC_Country_ID());
		summaryNew.setXX_Quantity(summary.getXX_Quantity());
		summaryNew.setDataType(summary.getDataType());
		summaryNew.setM_Warehouse_ID(idTienda);
		summaryNew.setXX_NoteCreditDebit(summary.getXX_NoteCreditDebit());
		summaryNew.setXX_ControlNumber(summary.getXX_ControlNumber());
		summaryNew.setXX_InvoiceNro(summary.getXX_InvoiceNro());
		summaryNew.setXX_InvoiceDate(summary.getXX_InvoiceDate());
		summaryNew.setXX_WithholdingTaxMarck(summary.isXX_WithholdingTaxMarck());
		summaryNew.setXX_DistributionByStore(summary.isXX_DistributionByStore());
		summaryNew.setXX_DateRegistration(summary.getXX_DateRegistration());
		summaryNew.setXX_DocumentDate(summary.getXX_DocumentDate());
		summaryNew.setXX_MontVentas(summary.getXX_MontVentas());
		summaryNew.setXX_VLO_TaxSales(summary.getXX_VLO_TaxSales());
		summaryNew.setC_Tax_ID(summary.getC_Tax_ID());
		
		if (aux == 1){
			summaryNew.setXX_CostFree(summary.getXX_CostFree());
			summaryNew.setXX_CostNotSubject(summary.getXX_CostNotSubject());		
			summaryNew.setXX_Cosant(monto.multiply(porcentaje).divide(new BigDecimal(100)));
		}else if (aux == 2){
			summaryNew.setXX_CostFree(monto.multiply(porcentaje).divide(new BigDecimal(100)));
			summaryNew.setXX_CostNotSubject(summary.getXX_CostNotSubject());		
			summaryNew.setXX_Cosant(summary.getXX_Cosant());
		}else if (aux == 3){
			summaryNew.setXX_CostFree(summary.getXX_CostFree());
			summaryNew.setXX_CostNotSubject(monto.multiply(porcentaje).divide(new BigDecimal(100)));		
			summaryNew.setXX_Cosant(summary.getXX_Cosant());
		}
		
		summaryNew.setXX_VLO_TaxCost(tax.multiply(porcentaje).divide(new BigDecimal(100)));
		summaryNew.setXX_Visible(true);
		summaryNew.save();
		
	}
	
	/**
	 * Buscar el id de iva más actual
	 * @return id del iva
	 */
	public int buscarIvaActual(){
		int iva = 0;
		String sql = "select C_Tax_ID " +
					 "from C_Tax " +
					 "where ValidFrom = " +
					 	"(select max(ValidFrom) " +
					 	"from C_Tax " +
					 	"where C_TaxCategory_ID = " + Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_IVA_ID") + " " +
					 	"and validFrom <= sysdate) ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				iva = rs.getInt("C_Tax_ID");
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
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
		
		return iva;
	}
	
}
