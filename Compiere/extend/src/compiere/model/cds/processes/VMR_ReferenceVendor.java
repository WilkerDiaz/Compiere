package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;

import compiere.model.cds.X_XX_VMR_PO_LineRefProv;
import compiere.model.cds.X_XX_VMR_VendorReference;

public class VMR_ReferenceVendor extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null;
	    PreparedStatement pstmt1 = null; 
	    ResultSet rs1 = null;
		try
		{
			X_XX_VMR_PO_LineRefProv poLineRef = new X_XX_VMR_PO_LineRefProv(getCtx(),getRecord_ID(),get_TrxName());
			
			Integer refProv = getRecord_ID();
			
			Date fechaActual = new Date();

			Integer order = 0;
			Integer store = 0;
			Integer cantventa = 0;
			Integer invtfinal = 0;
			
			
		    SimpleDateFormat formatoY = new SimpleDateFormat("yyyy");
			String year = formatoY.format(fechaActual);
			
			SimpleDateFormat formatoD = new SimpleDateFormat("MM");
			String mount = formatoD.format(fechaActual);
			
			SimpleDateFormat formatoDY = new SimpleDateFormat("MM - yyyy");
			String yearmount = formatoDY.format(fechaActual);
			
			String SQL1 = ("SELECT A.C_ORDER_ID AS ORDERD " +
					"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B " +
					"WHERE A.C_ORDER_ID = B.C_ORDER_ID AND A.XX_VMR_PO_LINEREFPROV_ID = '"+refProv+"'");
			
			System.out.println(SQL1);
			pstmt1 = DB.prepareStatement(SQL1, null); 
		    rs1 = pstmt1.executeQuery();
		   
		    if(rs1.next())
		    {
		    	order = rs1.getInt("ORDERD");
		    }
		    else
		    {
		    	return null;
		    }
		    rs1.close();
		    pstmt1.close();
		    
		   String SQL2 = ("DELETE FROM XX_VMR_VENDORREFERENCE " +
		    		"WHERE XX_VMR_PO_LINEREFPROV_ID = '"+refProv+"' ");

			pstmt2 = DB.prepareStatement(SQL2, null); 
		    pstmt2.executeQuery();
		      
	
			String SQL = ("SELECT A.M_PRODUCT, C.XX_STORE AS STORE, SUM (XX_SALESQUANTITY) AS CANTVENTA, " +
					"SUM (C.XX_INITIALINVENTORYQUANTITY + C.XX_SHOPPINGQUANTITY + C.XX_MOVEMENTQUANTITY + C.XX_ADJUSTMENTSQUANTITY + C.XX_PREVIOUSADJUSTMENTSQUANTITY - C.XX_SALESQUANTITY) AS INV " +
					"FROM XX_VMR_REFERENCEMATRIX A, XX_VMR_PO_LINEREFPROV B, XX_VCN_INVENTORY C " +
					"WHERE A.XX_VMR_PO_LINEREFPROV_ID = B.XX_VMR_PO_LINEREFPROV_ID " +
					"AND B.XX_VMR_PO_LINEREFPROV_ID = '"+refProv+"' " +
					"AND C.XX_CODEPRODUCT = A.M_PRODUCT " +
					"AND C.XX_INVENTORYMONTH = '"+mount+"' " +
					"AND C.XX_INVENTORYYEAR = '"+year+"' " +
					"GROUP BY A.M_PRODUCT, C.XX_STORE ");

			System.out.println(SQL);
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();

	
		   Trx tra = Trx.get("tras"); 
		    while(rs.next())
		    {
		    	X_XX_VMR_VendorReference vendoRed = new X_XX_VMR_VendorReference (getCtx(), 0, get_TrxName());
		    	
		    	store = rs.getInt("STORE");
		    	cantventa = rs.getInt("CANTVENTA");
		    	invtfinal = rs.getInt("INV");
				
		    	vendoRed.setAD_Client_ID(1000005);
		    	vendoRed.setAD_Org_ID(1000030);
		    	vendoRed.setXX_STORES(store);
		    	vendoRed.setXX_SALESQUANTITY(cantventa);
		    	vendoRed.setXX_FINALINVENTORY(invtfinal);
		    	vendoRed.setXX_VMR_PO_LineRefProv_ID(refProv);
		    	vendoRed.setC_Order_ID(order);
		    	vendoRed.setXX_DATE(yearmount);
		    	vendoRed.save();
		    		
		    } // end while
		   
		
		    tra.commit();
			return "Proceso Realizado";
		} //end try
		
		catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		} finally{
			 DB.closeResultSet(rs);
			 DB.closeStatement(pstmt);
			 DB.closeStatement(pstmt2);
			 DB.closeResultSet(rs1);
			 DB.closeStatement(pstmt1);
		}

	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
