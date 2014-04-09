package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.X_Ref_XX_Ref_TypeDispatchGuide;
import compiere.model.cds.X_XX_VMR_Order;

public class XX_ShowPhysiDistri extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT XX_VMR_ORDER_ID " +
					 "FROM XX_VLO_DetailDispatchGuide " +
					 "WHERE XX_VLO_DispatchGuide_ID="+getRecord_ID()+" " +
					 "AND XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"'";
		ps = DB.prepareStatement(sql, get_TrxName());
		rs = ps.executeQuery();
		int orderAux = 0;
		while (rs.next())
		{
			orderAux = rs.getInt(1);
			new compiere.model.cds.Utilities().showReportPlacedOrder(new X_XX_VMR_Order(Env.getCtx(), orderAux, null));
			
		}
		rs.close();
		ps.close();
		return "";
	}

	@Override
	protected void prepare() {

	}

}
