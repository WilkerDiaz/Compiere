package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.MMovement;
import compiere.model.cds.X_Ref_XX_Ref_TypeDispatchGuide;

public class XX_ShowProductTransfer extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT M_MOVEMENTT_ID " +
					 "FROM XX_VLO_DetailDispatchGuide " +
					 "WHERE XX_VLO_DispatchGuide_ID="+getRecord_ID()+" " +
					 "AND XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.TRANSFERS_BETWEEN_STORES.getValue()+"'";
		ps = DB.prepareStatement(sql, get_TrxName());
		rs = ps.executeQuery();
		int movementAux = 0;
		while (rs.next())
		{
			movementAux = rs.getInt(1);
			new compiere.model.cds.Utilities().showReportTransfer(new MMovement(Env.getCtx(), movementAux, null));
			
		}
		rs.close();
		ps.close();
		return "";
	}

	@Override
	protected void prepare() {

	}

}
