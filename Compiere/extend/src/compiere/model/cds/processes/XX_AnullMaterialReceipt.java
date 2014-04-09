package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;

public class XX_AnullMaterialReceipt extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		MInOut inOut = new MInOut( getCtx(), getRecord_ID(), null);
		
		String sql = "SELECT * " +
	     "FROM C_INVOICE " +
	     "WHERE C_ORDER_ID="+ inOut.getC_Order_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		
		boolean invoice = false;
		try {
			rs = prst.executeQuery();
			
			while(rs.next()){
				invoice=true;
			}
			
			rs.close();
			prst.close();
		} 
		catch (SQLException e){
			try {
				rs.close();
				prst.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.log(Level.SEVERE, e.getMessage());
		}	
		
		if(!invoice){
			inOut.setXX_InOutStatus("AN");
			inOut.setIsActive(false);
			inOut.save();
			
			MOrder order = new MOrder( getCtx(), inOut.getC_Order_ID(), null);
			
			if(order.getXX_OrderType().equalsIgnoreCase("Nacional")){
				order.setXX_OrderStatus("AP");
			}else{
				order.setXX_OrderStatus(order.getXX_StatusWhenReceipt());
			}
			
			order.save();
		}
		else{
			ADialog.error(1, new Container(),"CantAnullReceipt");
		}
		
		return "";
	}

	@Override
	protected void prepare() {
		

	}

}
