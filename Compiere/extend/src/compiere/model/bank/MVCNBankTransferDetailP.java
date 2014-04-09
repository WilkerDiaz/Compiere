package compiere.model.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MVCNBankTransferDetailP extends X_XX_VCN_BankTransferDetailP{

	public MVCNBankTransferDetailP(Ctx ctx, int XX_VCN_BankTransferDetailP_ID,
			Trx trx) {
		super(ctx, XX_VCN_BankTransferDetailP_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MVCNBankTransferDetailP (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * LUEGO DE QUE GUARDO ACTUALIZO EL MONTO TOTAL DE LA TRANSFERENCIA BANCARIA
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		boolean save = super.afterSave(newRecord, success);

		if(save){
			
			int transferencia = get_ValueAsInt("XX_VCN_BankTransfer_ID");
			String montos = get_ValueAsString("XX_VCN_Amount");
			double monto = Double.parseDouble(montos);
			recalcularMonto(transferencia, monto, "S");
			
		}
		return save;
	}//afterSave
	
/*
 * LUEGO DE QUE ELIMINO ACTUALIZO EL MONTO TOTAL DE LA TRANSFERENCIA BANCARIA
 */

	protected boolean beforeDelete()
	{
		int transferencia = get_ValueAsInt("XX_VCN_BankTransfer_ID");
		String montos = get_ValueAsString("XX_VCN_Amount");
		double monto = Double.parseDouble(montos);
		recalcularMonto(transferencia, monto,"R");

		return true;
	} 	//	beforeDelete
	
/*
* METODO QUE SE ENCARGA DE ACTUALIZAR EL MONTO DE LA TRANSFERENCIA BANCARIA
*/
	
public void recalcularMonto(int transferencia,Double monto, String operacion){
		
		Double total = (double) 0 ;
		
		//SI QUIERO AGREGAR UN CAMPO SUMO SI QUIERO ELIMINAR UN MONTO RESTO
		if(operacion.equals("S"))
		 total= monto;
		if(operacion.equals("R"))
			 total= -monto;
		
		//SELECCION DE PAGO
		String sql=
		"SELECT SUM(PS.TOTALAMT) AS SUMA"
		+" FROM C_PAYSELECTION PS, XX_VCN_BANKTRANSFERDETAILS BTDS, XX_VCN_BANKTRANSFER BT"
		+" WHERE PS.C_PAYSELECTION_ID= BTDS.C_PAYSELECTION_ID"
		+" AND BT.XX_VCN_BANKTRANSFER_ID = BTDS.XX_VCN_BANKTRANSFER_ID"
		+" AND BT.XX_VCN_BANKTRANSFER_ID = "+transferencia;
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//ejecuto la sentencia SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			
		   			total= total + rs.getDouble("SUMA");
		   			
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				System.out.println(e);
			}
		   	finally {
		   	//CERRAR CONEXION
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}

		   	
			//ANTICIPO
			String sql1=
			"SELECT SUM(P.PAYAMT) AS SUMA"
			+" FROM C_PAYMENT P, XX_VCN_BANKTRANSFERDETAILP BTDP, XX_VCN_BANKTRANSFER BT"
			+" WHERE P.C_PAYMENT_ID= BTDP.C_PAYMENT_ID"
			+" AND BT.XX_VCN_BANKTRANSFER_ID = BTDP.XX_VCN_BANKTRANSFER_ID"
			+" AND BT.XX_VCN_BANKTRANSFER_ID = "+transferencia;
			
			PreparedStatement prst1 = DB.prepareStatement(sql1,null);
		   	ResultSet rs1 = null;
		   	//ejecuto la sentencia SQL
			   	try {
			   		rs1 = prst1.executeQuery();
			   		if (rs1.next()){
			   			total= total + rs1.getDouble("SUMA");
			   		}// Fin if
			   	} 
			   	catch (Exception e){
					System.out.println(e);
				}
			   	finally {
			   	//CERRAR CONEXION
					DB.closeResultSet(rs1);
					DB.closeStatement(prst1);
				}
			   	
			   	
			   	
			//CASTEO A DOS DECIMALES Y ASIGNO EL VALOR A LA VENTANA TRANSFERENCIA
			total = Math.round(total*Math.pow(10,2))/Math.pow(10,2);
			
			//ASIGNO EL FORMATO DE DOS DECIMALES
			DecimalFormat formateador = new DecimalFormat("#.00");
			
		   	BigDecimal bd1 = new BigDecimal (formateador.format (total).replace(",", "."));

		   	int result = 0;
		   	String sqlupdate = 
		   	"UPDATE XX_VCN_BANKTRANSFER"
		   	+" SET XX_VCN_AMOUNT = "+bd1
		   	+" WHERE XX_VCN_BANKTRANSFER_ID="+transferencia;
			try {
				result = DB.executeUpdateEx(sqlupdate, get_Trx());
			} 
			catch (SQLException e) { e.printStackTrace(); }
	} 
}
