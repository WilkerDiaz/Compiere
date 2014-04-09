package compiere.model.bank.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MPaySelection;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.bank.X_XX_VCN_BankTransfer;
import compiere.model.bank.X_XX_VCN_BankTransferDetailP;
import compiere.model.bank.X_XX_VCN_BankTransferDetailS;

public class XX_VCN_TransferVoid extends SvrProcess{
	
	//VARIABLES GLOBALES
	private int			transfer_ID = 0;

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		X_XX_VCN_BankTransfer aux = new X_XX_VCN_BankTransfer(getCtx(),getRecord_ID(),get_TrxName());
		transfer_ID= aux.getXX_VCN_BankTransfer_ID();
		
		//BUSCAR LAS SELECCIONES DE PAGO
		
				String sql= "SELECT XX_VCN_BANKTRANSFERDETAILS_ID, C_PAYSELECTION_ID"
				+" FROM    XX_VCN_BANKTRANSFERDETAILS"
				+" WHERE ISACTIVE= 'Y' AND XX_VCN_BANKTRANSFER_ID = "+transfer_ID;
				
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		while (rs.next()){

				   			//OBTENGO EL ID DE LAS SELECCIONES DE PAGO
				   			int detalle= rs.getInt("XX_VCN_BANKTRANSFERDETAILS_ID");
				   			ReversarDetalle(detalle);
				   			
				   			
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

			//BUSCAR LOS ANTICIPOS
					
					String sql1= "SELECT XX_VCN_BANKTRANSFERDETAILP_ID"
					+" FROM    XX_VCN_BANKTRANSFERDETAILP"
					+" WHERE ISACTIVE= 'Y' AND XX_VCN_BANKTRANSFER_ID = "+transfer_ID;
					
					PreparedStatement prst1 = DB.prepareStatement(sql1,null);
				   	ResultSet rs1 = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs1 = prst1.executeQuery();
					   		while (rs1.next()){

					   			//OBTENGO EL ID DE LAS SELECCIONES DE PAGO
					   			int anticipo= rs1.getInt("XX_VCN_BANKTRANSFERDETAILP_ID");
					   			ReversarAnticipo(anticipo);
					   			
					   			
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
				   	
				   	
					aux.setXX_VCN_TransferState("3"); //PASA A ESTADO ANULADA
					aux.setIsActive(false); //PASA A ESTADO DESACTIVADA
					
					java.util.Date utilDate = new java.util.Date(); //FECHA ACTUAL
				    long lnMilisegundos = utilDate.getTime();
				    java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
				
					aux.setXX_VCN_CancellationDate(sqlTimestamp); //ASIGNO LA FECHA DE CANCELACION
					
					aux.setXX_VCN_UserAnnulation(Env.getCtx().getAD_User_ID()); // ASIGNO EL USUARIO DE CANCELACION
					
					aux.save();
		
		return null;
	}
	
private void ReversarAnticipo(int Id) {
		// TODO Auto-generated method stub
	X_XX_VCN_BankTransferDetailP det = new X_XX_VCN_BankTransferDetailP (getCtx(), Id, get_TrxName());
	
	//BUSCO Y REVERSO LOS DETALLES
	
			   //REVERSO LA SELECCION DE PAGO
			   	det.setXX_VCN_StateTransferDetail("2");
				det.setIsActive(false);
			   	det.save();

	
	}

public void ReversarDetalle(int Id){
		X_XX_VCN_BankTransferDetailS det = new X_XX_VCN_BankTransferDetailS (getCtx(), Id, get_TrxName());
		
		//BUSCO Y REVERSO LOS DETALLES
		
				   //REVERSO LA SELECCION DE PAGO
				   	det.setXX_VCN_StateTransferDetail("2");
					det.setIsActive(false);
				   	det.save();
	}

}
