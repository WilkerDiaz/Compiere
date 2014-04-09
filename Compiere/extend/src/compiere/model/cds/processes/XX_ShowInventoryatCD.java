package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.framework.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MInOut;

public class XX_ShowInventoryatCD extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		MInOut mInOut = new MInOut( getCtx(), getRecord_ID(), null);
		
		String sql = "SELECT XX_VMR_DistributionHeader_ID " +
					 "FROM XX_VMR_DistributionHeader " +
					 "WHERE C_ORDER_ID = "+mInOut.getC_Order_ID()+"";
		
		//System.out.println(sql);
		int distriHeader = 0;
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				distriHeader = rs.getInt("XX_VMR_DistributionHeader_ID");
			} 
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		
	   	AWindow window_PlacedOrder = new AWindow();
    	Query query = Query.getEqualQuery("XX_VMR_DistributionHeader_ID", distriHeader);
    	window_PlacedOrder.initWindow(1000222,query);
    	AEnv.showCenterScreen(window_PlacedOrder);
		
		return "";
	}

	@Override
	protected void prepare() {
	}

}
