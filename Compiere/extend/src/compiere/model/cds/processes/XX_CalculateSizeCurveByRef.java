package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VMR_SizeCurve;
import compiere.model.cds.X_XX_VMR_SizeCurveRefDetail;

public class XX_CalculateSizeCurveByRef extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
			
		X_XX_VMR_SizeCurve curve = 
			new X_XX_VMR_SizeCurve(getCtx(), getRecord_ID() ,get_TrxName());
		int header_id = curve.getXX_VMR_DistributionHeader_ID();
		int curve_id = curve.get_ID();
		
		String SQL_query = 
			" SELECT D.M_ATTRIBUTEVALUE_ID, R.XX_VMR_VENDORPRODREF_ID " +
				" FROM XX_VMR_SIZECURVE S JOIN XX_VMR_SIZECURVEDETAIL D " +
				" ON ( S.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID) JOIN " +
				" XX_VMR_SIZECURVEREFERENCE R ON " +
				"(R.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID)" +
				" WHERE S.XX_VMR_DISTRIBUTIONHEADER_ID = " + header_id +
				" AND S.XX_VMR_SIZECURVE_ID = " + curve_id;			
		try {
			PreparedStatement pstmt_b = DB.prepareStatement(SQL_query, null);
			ResultSet rs_b = pstmt_b.executeQuery();
			while (rs_b.next()) {
				X_XX_VMR_SizeCurveRefDetail ref_size_detail 
					= new X_XX_VMR_SizeCurveRefDetail(getCtx(), 0, get_TrxName());				
				ref_size_detail.setXX_VMR_VendorProdRef_ID(rs_b.getInt("XX_VMR_VENDORPRODREF_ID"));				
				ref_size_detail.setXX_VMR_SIZECURVE_ID(curve_id);
				ref_size_detail.setM_AttributeValue_ID(rs_b.getInt("M_ATTRIBUTEVALUE_ID"));
				ref_size_detail.save();				
			}			
			pstmt_b.close();
			rs_b.close();
			String SQL_delete = "DELETE FROM XX_VMR_SIZECURVEDETAIL " +
				"WHERE XX_VMR_SIZECURVE_ID = " + curve_id;
			
			DB.executeUpdate(null,SQL_delete);
			
		} catch (SQLException e) {			
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");
			e.printStackTrace();
			return "XX_DatabaseAccessError";
		}
		curve.setXX_CalculatedSizeCurveRef(true);
		curve.save();
		
		return "";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
