package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.MTab;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVCNTradeAgreements;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_CreditNotifyReturn;
import compiere.model.cds.X_XX_VCN_TradeAgreements;
import compiere.model.cds.forms.XX_EntranceAuthorization_Form;
import compiere.model.tag.X_XX_VCN_DetailAdvice;
import compiere.model.tag.X_XX_VCN_TradeAgrCategory;

public class XX_TradeAgreementRegister extends SvrProcess {


	private int aviso= 0;

	/*
	 * Método para validar que al menos se haya ingresado un descuento por monto o %
	 * @param agreement datos de la ventana
	 * @return false si los datos son correctos, true de lo contrario.
	 */
	boolean validateDiscounts(X_XX_VCN_TradeAgreements agreement){
		if (agreement.getXX_CENTRADISCDELIAMOUNT().compareTo(BigDecimal.ZERO)==0 && agreement.getXX_CENTRADISCDELIPERCEN().compareTo(BigDecimal.ZERO)==0 && 
				agreement.getXX_DISCAFTERSALEAMOUNT().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_DISCAFTERSALEPERCEN().compareTo(BigDecimal.ZERO)==0 && 
				agreement.getXX_DISCOUNTGOSAMOUNT().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_DISCOUNTGOSPERCENTAGE().compareTo(BigDecimal.ZERO)==0 && 
				agreement.getXX_DISCRECOGDECLAMOUNT().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_DISCRECOGDECLPERCEN().compareTo(BigDecimal.ZERO)==0 && 
				agreement.getXX_DiscOpenStorAmount().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_DiscOpenStorPercent().compareTo(BigDecimal.ZERO)==0 && 
				agreement.getXX_FIXVOLDISCOAMOUNT().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_FIXVOLDISCOPERCEN().compareTo(BigDecimal.ZERO)==0 && 
				agreement.getXX_FIRSTVARVOLDISCOFROM().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_FIRSTVARVOLDISCOTO().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_FIRSTVARVOLDISCOPERCEN().compareTo(BigDecimal.ZERO)==0 && 
				agreement.getXX_SECONDVARVOLDISCOFROM().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_SECONDVARVOLDISCOTO().compareTo(BigDecimal.ZERO)==0 &&  agreement.getXX_SECONDVARVOLDISCOPERCEN().compareTo(BigDecimal.ZERO)==0 &&
				agreement.getXX_PubAmount().compareTo(BigDecimal.ZERO)==0 && agreement.getXX_PubPercent().compareTo(BigDecimal.ZERO)==0)

			return false;

		return true;
	}
	/*
	 * Método que calcula los descuentos por carta de compromiso y lo guarda en la tabla
	 * XX_CreditNotifyReturn
	 * @param agreement Acuerdo Comercial que contiene los datos de la carta de compromiso
	 */
	boolean calculateCommitmentNotify(X_XX_VCN_TradeAgreements agreement){

		BigDecimal pubContribution = BigDecimal.ZERO; //Descuento por aporte a publicidad
		BigDecimal iva = BigDecimal.ONE;
		BigDecimal totalLines = BigDecimal.ZERO;
		BigDecimal multiplyRate = BigDecimal.ONE; //Si es Bs
		MOrder mOrder = null;
		Calendar date = Calendar.getInstance();
		boolean nationalCurrency = false;
		X_XX_VCN_DetailAdvice detailAdvice = new X_XX_VCN_DetailAdvice(getCtx(), 0, null);
		X_XX_CreditNotifyReturn creditNotify = new X_XX_CreditNotifyReturn(getCtx(), 0, null);

		//Si no hay monto por publicidad
		if (agreement.getXX_PubAmount().compareTo(BigDecimal.ZERO)==0 || 
				agreement.getDOCUMENTNOORDER()==0){
			ADialog.error(1, new Container(), "XX_CommitmentLetterFieldError");
			return false;
		}

		String SQL = "SELECT C_ORDER_ID" +
					" FROM C_ORDER" +
					" WHERE DOCUMENTNO = "+ agreement.getDOCUMENTNOORDER()+
					" AND C_BPARTNER_ID = "+agreement.getC_BPartner_ID() +
					" AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID()+
					" AND XX_ORDERSTATUS IN ('CH','RE')" +
					" AND ISSOTRX = 'N'";

		PreparedStatement prst = null;
		ResultSet rs = null;

		try {
			prst = DB.prepareStatement(SQL,null);
			rs = prst.executeQuery();
			//Si la order de compra no existe
			if(!rs.next()){
				ADialog.error(1, new Container(), "XX_WrongOrderForPubContribError");
				return false;
			}
			pubContribution = agreement.getXX_PubAmount();
			mOrder = new MOrder(getCtx(), rs.getInt(1), get_TrxName());
		} 
		catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.SEVERE, e.getMessage());
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		String SQL1 = "SELECT TOTALLINES, C_CURRENCY_ID, M_WAREHOUSE_ID," +
					"C_INVOICE_ID, XX_VMR_DEPARTMENT_ID " +
					"FROM C_INVOICE " +
					"WHERE C_ORDER_ID = " + mOrder.getC_Order_ID() + " " +
					"AND AD_CLIENT_ID = "+ getCtx().getAD_Client_ID()+ " "+
					"AND C_DOCTYPETARGET_ID = " 
					+ Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")+
					" AND ISSOTRX = 'N'";
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;

		try
		{	
			pstmt1 = DB.prepareStatement(SQL1, null); 
			rs1 = pstmt1.executeQuery();
			if (rs1.next()){
				totalLines = rs1.getBigDecimal("TOTALLINES");
				creditNotify.setXX_UnitPurchasePrice(totalLines);
				detailAdvice.setXX_VMR_Department_ID(rs1.getInt("XX_VMR_DEPARTMENT_ID"));
				detailAdvice.setM_Warehouse_ID(rs1.getInt("M_WAREHOUSE_ID"));
				detailAdvice.setC_Invoice_ID(rs1.getInt("C_INVOICE_ID"));
				//Si la moneda es nacional
				if (rs1.getInt("C_CURRENCY_ID")==205)
					nationalCurrency = true;
			}
			else{ 
				ADialog.error(1, new Container(), "XX_WrongOrderForPubContribError");
				return false;
			}		    
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.SEVERE, e.getMessage());
		}
		finally{
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);
		}

		if (nationalCurrency){

			String SQL5 = "SELECT A.RATE, A.C_TAX_ID, B.C_TAXCATEGORY_ID " +
						"FROM C_TAX A, C_TAXCATEGORY B " +
						"WHERE C_TAX_ID= "+ agreement.getXX_TypeTax_ID()+
						" AND A.AD_CLIENT_ID="+  getCtx().getAD_Client_ID();
			PreparedStatement pstmt5 = null;
			ResultSet rs5 = null;

			try
			{	
				pstmt5 = DB.prepareStatement(SQL5, null); 
				rs5 = pstmt5.executeQuery();

				while(rs5.next())
				{
					creditNotify.setC_TaxCategory_ID(rs5.getInt("C_TAXCATEGORY_ID"));
					iva = rs5.getBigDecimal("RATE");
					iva = iva.divide(new BigDecimal(100));
					creditNotify.setC_Tax_ID(rs5.getInt("C_TAX_ID"));			    	
				}
			}
			catch (Exception e) {
				System.out.println("ERROR GENERANDO EL AVISO DE DESCUENTO");
			}
			finally{
				DB.closeResultSet(rs5);
				DB.closeStatement(pstmt5);
			}
		}
		else{
			creditNotify.setC_Tax_ID(getCtx().getContextAsInt("#XX_L_TAX_EXENTO_ID")); //Si no es moneda nacional esta EXENTO(1000021) de iva
			creditNotify.setC_TaxCategory_ID(getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID"));
			iva = BigDecimal.ZERO;
		}

		//Se calcula el IVA y se guarda
		creditNotify.setXX_Amount_IVA(pubContribution.multiply(iva));
		//Si hay tasa de cambio calculo el costo original en Bs
		if(mOrder.getXX_ConversionRate_ID()!=0){
			String SQL6 = "SELECT MULTIPLYRATE "+
							"FROM C_CONVERSION_RATE " +
							"WHERE C_CONVERSION_RATE_ID="+mOrder.getXX_ConversionRate_ID()+
							" AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();

			PreparedStatement pstmt6 = null;
			ResultSet rs6 = null;

			try
			{	
				pstmt6 = DB.prepareStatement(SQL6, null); 
				rs6 = pstmt6.executeQuery();
				if(rs6.next()){
					multiplyRate = rs6.getBigDecimal("MULTIPLYRATE");
				}
			}
			catch (SQLException e) {
				log.log(Level.SEVERE, SQL6, e);
			}
			finally{
				DB.closeResultSet(rs6);
				DB.closeStatement(pstmt6);
			}
		}
		creditNotify.setXX_UnitPurchasePriceBs(totalLines.multiply(multiplyRate));
		creditNotify.setC_Order_ID(mOrder.getC_Order_ID());
		creditNotify.setXX_NotificationType("CAC"); //Carta Compromiso
		creditNotify.setC_Currency_ID(agreement.getC_Currency_ID());
		creditNotify.setDescription("Carta Compromiso");
		creditNotify.setXX_Amount(pubContribution);
		if (!creditNotify.save()){
			log.log(Level.WARNING,"Credit Notify wasn't successfully saved");
			return false;
		}


		detailAdvice.setXX_CreditNotifyReturn_ID(
				creditNotify.getXX_CreditNotifyReturn_ID());
		detailAdvice.setXX_UnitPurchasePrice(creditNotify.getXX_UnitPurchasePrice());
		detailAdvice.setXX_PubAmount(pubContribution);
		detailAdvice.setC_Order_ID(mOrder.getC_Order_ID());
		detailAdvice.setXX_Month(date.get(Calendar.MONTH)+1);
		detailAdvice.setXX_Year(date.get(Calendar.YEAR));
		if (!detailAdvice.save()){
			log.log(Level.WARNING,"Detail Advice wasn't successfully saved");
			return false;
		}

		aviso = creditNotify.get_ID();

		return true;
	}

	@Override
	protected String doIt() throws Exception {

		CLogger log = CLogger.getCLogger(XX_EntranceAuthorization_Form.class);
		X_XX_VCN_TradeAgreements agreement = new X_XX_VCN_TradeAgreements(Env.getCtx(), getRecord_ID(), get_TrxName());
		boolean flag= false;
		boolean flagQuery= false;

		if(agreement.getXX_AgreementType().equals("CC"))
			if(calculateCommitmentNotify(agreement)){
				agreement.setXX_Status("VIGENTE");
				agreement.save();
				ADialog.info(1, new Container(), "XX_RegisterTradeAgree");
				commit();
				//Envio el correo si se desea 
				Utilities u = null;
				u = new Utilities();
				X_XX_CreditNotifyReturn creditNotify = new X_XX_CreditNotifyReturn(getCtx(), aviso, null);
				if(creditNotify.getXX_NotificationType().equals("CAC"))
					u.reportEngagementLetter(creditNotify.getXX_CreditNotifyReturn_ID());

				return "OK";
			}
			else
				return "Not Saved";


		String sql = "SELECT *"
					+ " FROM XX_VCN_TRADEAGRCATEGORY "
					+ " WHERE XX_VCN_TRADEAGREEMENTS_ID="+getRecord_ID()+
					" AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {

			rs = prst.executeQuery();
			//Si la consulta no retorna ningún registro, quiere decir que no tiene categoría asociada a ese registro y "flag" queda false
			if (rs.next())
				flag = true;
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		String sqlQuery = "SELECT *"
						+ " FROM XX_VCN_TRADEAGRDEPARTMENT "
						+ " WHERE XX_VCN_TRADEAGREEMENTS_ID="+getRecord_ID()+
						" AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();

		PreparedStatement prstat = null;
		ResultSet rsul = null;
		try {
			prstat = DB.prepareStatement(sqlQuery,null);
			rsul = prstat.executeQuery();
			//Si la consulta no retorna ningún registro, quiere decir que no tiene categoría asociada a ese registro y "flag" queda false
			if (rsul.next())
				flagQuery = true;

		} 
		catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}
		finally{
			DB.closeResultSet(rsul);
			DB.closeStatement(prstat);
		}



		//Valido que tenga categoría o departamento
		if(flag || flagQuery){		
			//Valido que sea de tipo Acuerdo Comercial
			if(agreement.getXX_AgreementType().equals("AC"))
			{
				if (validateDiscounts(agreement) )
				{
					agreement.setXX_Status("VIGENTE");
					agreement.save();
					ADialog.info(1, new Container(), "XX_RegisterTradeAgree");
					return "OK";
				}
				else
					ADialog.error(1, new Container(), "XX_TradeAgreeRegError");
			}
		}
		else
			ADialog.error(1, new Container(), "XX_TradeAgreeRegError");
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}


}
