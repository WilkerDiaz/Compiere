package compiere.model.suppliesservices.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import compiere.model.cds.MOrder;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class XX_PO_OrderReady  extends SvrProcess {
	
	@Override
	protected void prepare() {

	}
	
	@Override
	protected String doIt() throws Exception {
		boolean distribution = false;
		MOrder aux = new MOrder(Env.getCtx(), getRecord_ID(), null);
		
		// Orden de compra de BYS
		if(aux.getXX_POType().equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
			// Se verifica la distribución de las líneas
			distribution = get_Distribution (getRecord_ID());
			if(distribution){
				//Seatea la O/C como ready 
				aux.setXX_OrderReadyStatus(true);
				Calendar today = Calendar.getInstance();
				aux.setXX_OrderReadyDate(new java.sql.Timestamp(today.getTimeInMillis()));
				aux.save();	
				
				// Se llama al proceso de aprobación de la OC 
				//approvePOBYS();
				
				//Thread.sleep(5000);
			}
			else {
				ADialog.info(1, new Container(),Msg.translate(Env.getCtx(), "XX_OrderDistrib"));
			}// else distribucion
			
		}// BYS
		return "Ejecución de Order Ready";
	} // doIt
	
	/** approvePO
	 * Aprueba la O/C de BYS
	 * Se llama al proceso de aprobación de OC
	 */
	private void approvePOBYS()	{
		MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_POAPPROVALPROCESS"), getRecord_ID()); 
		mpi.save();
			
		ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_POAPPROVALPROCESS")); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_POAPPROVALPROCESS")); 
		pi.setClassName(""); 
		pi.setTitle(""); 
			
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
		
	}//
	/** get_Distribution
	 * @param Order_ID
	 * @return distribtion
	 *  */
	private boolean get_Distribution (int Order_ID){
		/*
		boolean disttribReturn = true;
		
		// Se obtiene el número de líneas asociadas a la OC
		String sql = " SELECT L.C_ORDERLINE_ID LINES, " +
				" L.XX_ISDISTRIBAPPLIED AP" +
				" FROM C_ORDERLINE L " +
				" WHERE L.C_ORDER_ID = " + Order_ID;
		//System.out.println(sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				if(!rs.getString("AP").equalsIgnoreCase("Y")){
					disttribReturn = false;
				}
			}// while
		}
		catch (Exception e){
			log.saveError("ErrorSql Verificacion de Distribucion de OC BYS", 
					Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		*/
		boolean disttribReturn = true; //VARIABLE DE VERIFICACION 

		  //VALIDA QUE LAS LINEAS DE FACTURA TENGAN LINEAS DE DISTRIBUCION
		  String sql=
		  "SELECT CI.C_ORDER_ID AS ORDEN ,CIL.C_ORDERLINE_ID AS LINEA, XXP.XX_PRODUCTPERCENTDISTRIB_ID AS DISTRIBUCION"
		  +" FROM C_ORDER CI, C_ORDERLINE CIL"
		  +" LEFT OUTER JOIN XX_PRODUCTPERCENTDISTRIB XXP ON CIL.C_ORDERLINE_ID = XXP.C_ORDERLINE_ID"
		  +" WHERE CI.C_ORDER_ID = CIL.C_ORDER_ID AND CI.C_ORDER_ID = " + Order_ID;
		  
			PreparedStatement pstmt = null; 
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, get_Trx()); 
				rs = pstmt.executeQuery();
				while(rs.next()){
					if(rs.getString("DISTRIBUCION") == null){
						disttribReturn=false;
					}
				}
				
			}catch (Exception e) {
				log.log(Level.SEVERE, sql);
			}finally{
					try {
						rs.close();
						pstmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			//VALIDA QUE EL MONTO DE LAS LINEAS DE FACTURAS SEAN IGUAL A LA SUMA DEL MONTO DE LAS LINEAS DE DISTRIBUCION
			  
			String sql1=
					  "SELECT C.LINENETAMT AS LINEA, (SELECT round(SUM(X.XX_AMOUNTPERCC),2) FROM XX_ProductPercentDistrib X WHERE C.C_OrderLine_ID=X.C_OrderLine_ID ) AS SUMA"
					  +" FROM C_OrderLine C, C_Order I"
					  +" WHERE I.C_Order_ID = " + Order_ID+" AND I.C_Order_ID=C.C_Order_ID";

					  
						PreparedStatement pstmt1 = null; 
						ResultSet rs1 = null;
						try{
							pstmt1 = DB.prepareStatement(sql1, get_Trx()); 
							rs1 = pstmt1.executeQuery();
							while(rs1.next()){
								if(rs1.getString("LINEA").compareTo(rs1.getString("SUMA")) != 0){
									disttribReturn=false;
								}
							}
							
						}catch (Exception e) {
							log.log(Level.SEVERE, sql);
						}finally{
								try {
									rs1.close();
									pstmt1.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}

				//VALIDA QUE EL MONTO DE LAS LINEAS DE FACTURAS SEAN IGUAL A LA SUMA DEL MONTO DE LAS LINEAS DE DISTRIBUCION
						  
				String sql2=
							  "SELECT 100 AS LINEA, (SELECT SUM(X.XX_PERCENTAGEPERCC) FROM XX_ProductPercentDistrib X WHERE C.C_OrderLine_ID=X.C_OrderLine_ID ) AS SUMA"
							  +" FROM C_OrderLine C, C_Order I"
							  +" WHERE I.C_Order_ID = " + Order_ID+" AND I.C_Order_ID=C.C_Order_ID";

								  
								PreparedStatement pstmt2 = null; 
								ResultSet rs2 = null;
								try{
									pstmt2 = DB.prepareStatement(sql2, get_Trx()); 
									rs2 = pstmt2.executeQuery();
									while(rs2.next()){
										if(rs2.getString("LINEA").compareTo(rs2.getString("SUMA")) != 0){
											disttribReturn=false;
										}
									}
										
								}catch (Exception e) {
									log.log(Level.SEVERE, sql2);
								}finally{
										try {
											rs2.close();
											pstmt2.close();
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();											}
									}
		return disttribReturn;
		
	}// Fin get_Distribution

}// Fin XX_PO_OrderReady
