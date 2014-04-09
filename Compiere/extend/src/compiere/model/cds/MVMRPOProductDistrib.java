package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRPOProductDistrib extends X_XX_VMR_PO_ProductDistrib{

	public MVMRPOProductDistrib(Ctx ctx, int XX_VMR_PO_ProductDistrib_ID,
			Trx trx) {
		super(ctx, XX_VMR_PO_ProductDistrib_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRPOProductDistrib.class);	
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		if(success){
			if(isXX_IsDefinitive()){
				
				if(get_ValueOld("XX_IsDefinitive").equals(false) || !get_ValueOld("XX_SalePricePlusTax").equals(get_Value("XX_SalePricePlusTax"))){

					X_XX_VMR_PO_ProductDistrib dpd = new X_XX_VMR_PO_ProductDistrib(getCtx(), get_ID(), get_Trx());
					String sql = "SELECT DPD1.XX_VMR_PO_PRODUCTDISTRIB_ID " +
					 "FROM XX_VMR_PO_PRODUCTDISTRIB DPD1, M_PRODUCT P1 " +
					 "WHERE P1.M_PRODUCT_ID = DPD1.M_PRODUCT_ID " +
					 "AND DPD1.XX_VMR_DISTRIBUTIONHEADER_ID = "+dpd.getXX_VMR_DistributionHeader_ID()+" " +
					 "AND DPD1.XX_VMR_PO_PRODUCTDISTRIB_ID <> "+ dpd.getXX_VMR_PO_ProductDistrib_ID() +" " +
					 "AND P1.XX_VMR_VENDORPRODREF_ID IN ( " +
						 "SELECT XX_VMR_VENDORPRODREF_ID " +
						 "FROM XX_VMR_PO_PRODUCTDISTRIB DPD, M_PRODUCT P " +
						 "WHERE P.M_PRODUCT_ID = DPD.M_PRODUCT_ID " +
						 "AND DPD.XX_VMR_PO_PRODUCTDISTRIB_ID = "+dpd.getXX_VMR_PO_ProductDistrib_ID()+" " +
					 ")" +  " AND DPD1.XX_ISDEFINITIVEINDIVIDUAL = 'N'";
					try{
						//System.out.println(sql);
						PreparedStatement pstmt = DB.prepareStatement(sql, null);
						ResultSet rs = pstmt.executeQuery();
						
						Integer dpdID = null;
						int cantidad = 0;
						
						while(rs.next()){
							dpdID = rs.getInt("XX_VMR_PO_PRODUCTDISTRIB_ID");
							X_XX_VMR_PO_ProductDistrib dpd1_misma_ref = new X_XX_VMR_PO_ProductDistrib(getCtx(), dpdID, get_Trx());
							dpd1_misma_ref.setXX_TaxAmount(dpd.getXX_TaxAmount());
							dpd1_misma_ref.setXX_Margin(dpd.getXX_Margin());
							dpd1_misma_ref.setXX_SalePrice(dpd.getXX_SalePrice());
							dpd1_misma_ref.setXX_SalePricePlusTax(dpd.getXX_SalePricePlusTax());
							dpd1_misma_ref.setXX_IsDefinitive(true);
							dpd1_misma_ref.save();
							cantidad++;
						}
						rs.close();
						pstmt.close();
						
						if(cantidad > 0){
							MProduct producto = new MProduct(getCtx(), getM_Product_ID(), null);
							MVMRVendorProdRef referencia = new MVMRVendorProdRef(getCtx(), producto.getXX_VMR_VendorProdRef_ID(), null);
							ADialog.info(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(getCtx(), "XX_PriceDefinitiveReference", new String[]{referencia.getName()}));							
						}
					}
					catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}		
				}
			}	
		}
		return true;
	}
}
