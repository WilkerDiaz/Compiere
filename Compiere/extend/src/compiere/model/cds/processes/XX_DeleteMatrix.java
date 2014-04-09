package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VMR_PO_LineRefProv;


public class XX_DeleteMatrix extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		X_XX_VMR_PO_LineRefProv aux = new X_XX_VMR_PO_LineRefProv(getCtx(),getRecord_ID(),get_TrxName());
	
		aux.setXX_Characteristic1_ID(0);
		aux.setXX_Characteristic1Value1_ID(0);
		aux.setXX_Characteristic1Value2_ID(0);
		aux.setXX_Characteristic1Value3_ID(0);
		aux.setXX_Characteristic1Value4_ID(0);
		aux.setXX_Characteristic1Value5_ID(0);
		aux.setXX_Characteristic1Value6_ID(0);
		aux.setXX_Characteristic1Value7_ID(0);
		aux.setXX_Characteristic1Value8_ID(0);
		aux.setXX_Characteristic1Value9_ID(0);
		aux.setXX_Characteristic1Value10_ID(0);
		aux.setXX_Characteristic2_ID(0);
		aux.setXX_Characteristic2Value1_ID(0);
		aux.setXX_Characteristic2Value2_ID(0);
		aux.setXX_Characteristic2Value3_ID(0);
		aux.setXX_Characteristic2Value4_ID(0);
		aux.setXX_Characteristic2Value5_ID(0);
		aux.setXX_Characteristic2Value6_ID(0);
		aux.setXX_Characteristic2Value7_ID(0);
		aux.setXX_Characteristic2Value8_ID(0);
		aux.setXX_Characteristic2Value9_ID(0);
		aux.setXX_Characteristic2Value10_ID(0);
		
		aux.setXX_DeleteMatrix("N");
		aux.setXX_ShowMatrix("N");
		aux.setXX_GenerateMatrix("N");
		aux.setXX_ReferenceIsAssociated(false);
		aux.setQty(0);
		
		aux.setXX_IsGeneratedCharac1Value1(false);
		aux.setXX_IsGeneratedCharac1Value2(false);
		aux.setXX_IsGeneratedCharac1Value3(false);
		aux.setXX_IsGeneratedCharac1Value4(false);
		aux.setXX_IsGeneratedCharac1Value5(false);
		aux.setXX_IsGeneratedCharac1Value6(false);
		aux.setXX_IsGeneratedCharac1Value7(false);
		aux.setXX_IsGeneratedCharac1Value8(false);
		aux.setXX_IsGeneratedCharac1Value9(false);
		aux.setXX_IsGeneratedCharac1Value10(false);
		
		aux.setXX_IsGeneratedCharac2Value1(false);
		aux.setXX_IsGeneratedCharac2Value2(false);
		aux.setXX_IsGeneratedCharac2Value3(false);
		aux.setXX_IsGeneratedCharac2Value4(false);
		aux.setXX_IsGeneratedCharac2Value5(false);
		aux.setXX_IsGeneratedCharac2Value6(false);
		aux.setXX_IsGeneratedCharac2Value7(false);
		aux.setXX_IsGeneratedCharac2Value8(false);
		aux.setXX_IsGeneratedCharac2Value9(false);
		aux.setXX_IsGeneratedCharac2Value10(false);
		
		aux.save();
		
		String SQL =
				"Delete XX_VMR_REFERENCEMATRIX " 
				+"where XX_VMR_PO_LINEREFPROV_ID="+aux.get_Value("XX_VMR_PO_LineRefProv_ID");		 
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = DB.prepareStatement(SQL, null);
			DB.executeUpdateEx(SQL, null);				
							
		}
		catch(Exception e)
		{	
			return e.getMessage();
		} finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		
		return "";
					
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}
