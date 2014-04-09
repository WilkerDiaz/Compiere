package compiere.model.cds;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRDistribProductDetail extends X_XX_VMR_DistribProductDetail{

	public MVMRDistribProductDetail(Ctx ctx,
			int XX_VMR_DistribProductDetail_ID, Trx trx) {
		super(ctx, XX_VMR_DistribProductDetail_ID, trx);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	

		if (getXX_SalePricePlusTax().compareTo(new BigDecimal(0))<=0)
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "XX_InvalidSalePricePlusTax"));
			return false;
		}
					
		return true;
				
	}//beforeSave
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		if(success){
			if(isXX_IsDefinitive()){

				if(get_ValueOld("XX_IsDefinitive").equals(false) || !get_ValueOld("XX_SalePricePlusTax").equals(get_Value("XX_SalePricePlusTax"))){
					X_XX_VMR_DistribProductDetail dpd = new X_XX_VMR_DistribProductDetail(getCtx(), get_ID(), get_Trx());
					String sql = "SELECT DPD1.XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
								 "FROM XX_VMR_DISTRIBPRODUCTDETAIL DPD1, M_PRODUCT P1 " +
								 "WHERE P1.M_PRODUCT_ID = DPD1.M_PRODUCT_ID " +
								 "AND DPD1.XX_VMR_DISTRIBUTIONDETAIL_ID = "+dpd.getXX_VMR_DistributionDetail_ID()+" " +
								 "AND DPD1.XX_VMR_DISTRIBPRODUCTDETAIL_ID <> "+ dpd.getXX_VMR_DistribProductDetail_ID() +" " +
								 "AND P1.XX_VMR_VENDORPRODREF_ID IN ( " +
									 "SELECT XX_VMR_VENDORPRODREF_ID " +
									 "FROM XX_VMR_DISTRIBPRODUCTDETAIL DPD, M_PRODUCT P " +
									 "WHERE P.M_PRODUCT_ID = DPD.M_PRODUCT_ID " +
									 "AND DPD.XX_VMR_DISTRIBPRODUCTDETAIL_ID = "+dpd.getXX_VMR_DistribProductDetail_ID()+" " +
								 ") AND DPD1.XX_ISDEFINITIVEINDIVIDUAL = 'N'";
					try{
						//System.out.println(sql);
						PreparedStatement pstmt = DB.prepareStatement(sql, null);
						ResultSet rs = pstmt.executeQuery();
						
						Integer dpdID = null;
						int cantidad = 0;
						
						while(rs.next()){
							dpdID = rs.getInt("XX_VMR_DISTRIBPRODUCTDETAIL_ID");
							X_XX_VMR_DistribProductDetail dpd1 = new X_XX_VMR_DistribProductDetail(getCtx(), dpdID, get_Trx());
							dpd1.setXX_TaxAmount(dpd.getXX_TaxAmount());
							dpd1.setXX_Margin(dpd.getXX_Margin());
							dpd1.setXX_SalePrice(dpd.getXX_SalePrice());
							dpd1.setXX_SalePricePlusTax(dpd.getXX_SalePricePlusTax());
							dpd1.setXX_IsDefinitive(dpd.isXX_IsDefinitive());
							dpd1.save();
							cantidad++;
						}
						rs.close();
						pstmt.close();
						
						if(cantidad > 0){
							MProduct producto = new MProduct(getCtx(), getM_Product_ID(), null);
							MVMRVendorProdRef referencia = new MVMRVendorProdRef(getCtx(), producto.getXX_VMR_VendorProdRef_ID(), null);
							get_Trx().commit();
							ADialog.info(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(getCtx(), "XX_PriceDefinitiveReference", new String[]{referencia.getName()}));							
						}
					}
					catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}	
				}
				/* Agregado por GHUCHET*/
			}else if(get_ValueOld("XX_IsDefinitive").equals(true)){
				X_XX_VMR_DistribProductDetail dpd = new X_XX_VMR_DistribProductDetail(getCtx(), get_ID(), get_Trx());
				String sql = "SELECT DPD1.XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
							 "FROM XX_VMR_DISTRIBPRODUCTDETAIL DPD1, M_PRODUCT P1 " +
							 "WHERE P1.M_PRODUCT_ID = DPD1.M_PRODUCT_ID " +
							 "AND DPD1.XX_VMR_DISTRIBUTIONDETAIL_ID = "+dpd.getXX_VMR_DistributionDetail_ID()+" " +
							 "AND DPD1.XX_VMR_DISTRIBPRODUCTDETAIL_ID <> "+ dpd.getXX_VMR_DistribProductDetail_ID() +" " +
							 "AND P1.XX_VMR_VENDORPRODREF_ID IN ( " +
								 "SELECT XX_VMR_VENDORPRODREF_ID " +
								 "FROM XX_VMR_DISTRIBPRODUCTDETAIL DPD, M_PRODUCT P " +
								 "WHERE P.M_PRODUCT_ID = DPD.M_PRODUCT_ID " +
								 "AND DPD.XX_VMR_DISTRIBPRODUCTDETAIL_ID = "+dpd.getXX_VMR_DistribProductDetail_ID()+" " +
							 ") AND DPD1.XX_ISDEFINITIVEINDIVIDUAL = 'N'";
				try{
					//System.out.println(sql);
					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					ResultSet rs = pstmt.executeQuery();
					
					Integer dpdID = null;
					int cantidad = 0;
					
					while(rs.next()){
						dpdID = rs.getInt("XX_VMR_DISTRIBPRODUCTDETAIL_ID");
						X_XX_VMR_DistribProductDetail dpd1 = new X_XX_VMR_DistribProductDetail(getCtx(), dpdID, get_Trx());
						dpd1.setXX_IsDefinitive(dpd.isXX_IsDefinitive());
						dpd1.save();
						cantidad++;
					}
					rs.close();
					pstmt.close();
					
					if(cantidad > 0){
						MProduct producto = new MProduct(getCtx(), getM_Product_ID(), null);
						MVMRVendorProdRef referencia = new MVMRVendorProdRef(getCtx(), producto.getXX_VMR_VendorProdRef_ID(), null);
						get_Trx().commit();
						ADialog.info(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(getCtx(), "XX_NoPriceDefinitiveReference", new String[]{referencia.getName()}));							
					}
				}
				catch (SQLException e){
					log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
				}	
			}/* Hasta aquí Agregado por GHUCHET*/
		}
		return true;
	}
}
