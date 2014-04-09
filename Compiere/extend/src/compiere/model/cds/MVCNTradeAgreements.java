package compiere.model.cds;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

//import com.sun.org.apache.xml.internal.security.encryption.AgreementMethod;

public class MVCNTradeAgreements extends X_XX_VCN_TradeAgreements{

	public MVCNTradeAgreements(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	public MVCNTradeAgreements(Ctx ctx, int XX_VCN_TradeAgreements_ID,
			Trx trxName) {
		super(ctx, XX_VCN_TradeAgreements_ID, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);
	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord){
			boolean flag= false;
			String sql = "SELECT *"
				+ " FROM XX_VCN_TRADEAGREEMENTS "
				+ " WHERE C_BPARTNER_ID="+ getC_BPartner_ID()
				+ " AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID()
				+ " AND XX_STATUS in ('VIGENTE', 'BORRADOR')";
			PreparedStatement prst = DB.prepareStatement(sql,null);
			
			try {
				
				ResultSet rs = prst.executeQuery();
				//Si la consulta no retorna ningún registro, quiere decir que no existe un Acuerdo comercial vigente de ese proveedor
				if (rs.next())
					flag = true;
						
				rs.close();
				prst.close();
			} catch (SQLException e){
				log.log(Level.SEVERE, e.getMessage());
			}
	
			if (flag){
				ADialog.error(1, new Container(), "XX_TradeAgreeSave");
				return false;
			}

		}
	
		if ((getXX_CENTRADISCDELIAMOUNT().compareTo(BigDecimal.ZERO)!=0 && getXX_CENTRADISCDELIPERCEN().compareTo(BigDecimal.ZERO)!=0) || 
				(getXX_DISCAFTERSALEAMOUNT().compareTo(BigDecimal.ZERO)!=0 && getXX_DISCAFTERSALEPERCEN().compareTo(BigDecimal.ZERO)!=0) || 
				(getXX_DISCOUNTGOSAMOUNT().compareTo(BigDecimal.ZERO)!=0 && getXX_DISCOUNTGOSPERCENTAGE().compareTo(BigDecimal.ZERO)!=0) || 
				(getXX_DISCRECOGDECLAMOUNT().compareTo(BigDecimal.ZERO)!=0 && getXX_DISCRECOGDECLPERCEN().compareTo(BigDecimal.ZERO)!=0) || 
				(getXX_DiscOpenStorAmount().compareTo(BigDecimal.ZERO)!=0 && getXX_DiscOpenStorPercent().compareTo(BigDecimal.ZERO)!=0) || 
				(getXX_FIXVOLDISCOAMOUNT().compareTo(BigDecimal.ZERO)!=0 && getXX_FIXVOLDISCOPERCEN().compareTo(BigDecimal.ZERO)!=0) || 
				(getXX_PubAmount().compareTo(BigDecimal.ZERO)!=0 && getXX_PubPercent().compareTo(BigDecimal.ZERO)!=0)){
			ADialog.error(1, new Container(), "XX_TradeAgDiscError");
			return false;
		}
		return true;

	}
	
	@Override
	protected boolean beforeDelete()
	{
		//Solo se puede borrar cuando sea status BORRADOR
		if(getXX_Status().equals("BORRADOR"))
			return true;
		else{
			ADialog.error(1, new Container(), "XX_TradeAgreementEraseError");
			return false;
		}
		
	}
	
	
}
