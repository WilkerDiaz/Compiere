package compiere.model.bank.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.bank.X_XX_VCN_BankTransferDetailP;
import compiere.model.cds.MPayment;

public class XX_VCN_DeleteDetailP extends SvrProcess{
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		//CREO EL OBJETO TRANSFERENCIA 
		X_XX_VCN_BankTransferDetailP aux = new X_XX_VCN_BankTransferDetailP(getCtx(),getRecord_ID(),get_TrxName());
				//OBTENGO EL ID DEL C_PAYMENT
				int pago= aux.getC_Payment_ID();
				
				//LOAD C_PAYMENT
				MPayment pay = new MPayment (getCtx(), pago, get_TrxName());
		
				//ESTO ES PARA QUE SE RECALCULE EL MONTO DE UNA TRANSFERENCIA
				int transferencia = aux.getXX_VCN_BankTransfer_ID();
				BigDecimal monto = pay.getPayAmt();
				double monto1= monto.doubleValue();
				recalcularMonto(transferencia,monto1,"R");
				
		aux.delete(false);
		return null;
	}
	
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
		   	BigDecimal bd = new BigDecimal (total);
		   	bd = bd.setScale(2, RoundingMode.CEILING);
		   	
		   	int result = 0;
		   	String sqlupdate = 
		   	"UPDATE XX_VCN_BANKTRANSFER"
		   	+" SET XX_VCN_AMOUNT = "+bd
		   	+" WHERE XX_VCN_BANKTRANSFER_ID="+transferencia;
			try {
				result = DB.executeUpdateEx(sqlupdate, get_Trx());
			} 
			catch (SQLException e) { e.printStackTrace(); }
	} 

}
