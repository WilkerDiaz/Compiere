package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.processes.XX_BatchNumberInfo;

public class MVMRDiscountAppliDetail extends X_XX_VMR_DiscountAppliDetail{

/**
*  Realizado por Rosmaira Arvelo 
*/
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	public MVMRDiscountAppliDetail(Ctx ctx, int XX_VMR_DiscountAppliDetail_ID,
			Trx trx) {
		super(ctx, XX_VMR_DiscountAppliDetail_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRDiscountAppliDetail (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	/**
	 * 	Before Save
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{	
		boolean save = super.beforeSave(newRecord);
		
		if (getXX_VMR_DiscountType_ID()==1000007)
			setXX_AmountRebated(new BigDecimal(getXX_LoweringQuantity()).multiply(getXX_PriceBeforeDiscount()));
		else
			setXX_AmountRebated(new BigDecimal(getXX_LoweringQuantity()).multiply(getXX_DiscountPrice()));
		
		if(save)
		{
			try 
			{
				if(getXX_LoweringQuantity()==0)
				{
					log.saveError("",Msg.translate(Env.getCtx(), "LoweringQuantityNotZero"));
					return false;
				}
				
				if(newRecord)
				{
					Integer count = getDiscountAppliDetailCount(getXX_VMR_DiscountRequest_ID(),getM_Product_ID(),getXX_VMR_PriceConsecutive_ID());
								
					if(count==1)
					{
						log.saveError("",Msg.translate(Env.getCtx(), "AlreadySavedProduct"));
						return false;
					}
				}
				
				//Verificar que las cantidades requeridas sean menores que las disponibles
				XX_BatchNumberInfo lote_info = new XX_BatchNumberInfo();
				XX_BatchNumberInfo.Informacion info = lote_info.crearInfo();
				
				MVMRDiscountRequest dr = new MVMRDiscountRequest(Env.getCtx(),getXX_VMR_DiscountRequest_ID(),get_Trx());
				//MLocator locator = Utilities.obtenerLocatorEnTienda(dr.getM_Warehouse_ID());
				X_XX_VMR_PriceConsecutive pc = new X_XX_VMR_PriceConsecutive(Env.getCtx(),getXX_VMR_PriceConsecutive_ID(),get_Trx()); 
				
				info.setConsecutivo(pc.getXX_PriceConsecutive());
				info.setLocator(getLocator(dr.getM_Warehouse_ID()));
				info.setLote(pc.getM_AttributeSetInstance_ID());
				info.setProducto(getM_Product_ID());
				info.setAlmacen(dr.getM_Warehouse_ID());

				//Buscar la cantidad disponible
				String respuesta = info.cantidadProductoConsecutivo();
				if (info.isCorrecto()) {
					/**
					if (new BigDecimal(getXX_LoweringQuantity()).compareTo(info.getCantidadDisponible()) == 1) {
						String mss = Msg.getMsg(Env.getCtx(), "XX_ReqLessThanAvail", 
								new String[] {
									"" + getXX_LoweringQuantity(), 
									"" + info.getCantidadDisponible(),							
									"" + pc.getXX_PriceConsecutive()});			
						log.saveError("Error", mss);
						return false;
					}
					*/
				} else {
					log.saveError("Error", respuesta);
					return false;
				}
			}
			catch(Exception e)
			{	
				e.getMessage();
				return false;				
			}			
		}
		return save;
	}	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		boolean save = super.afterSave(newRecord, success);
		
		if(save)
		{			
			
			String SQL = "SELECT SUM(XX_LoweringQuantity) AS QTY, SUM(XX_TotalPrice) AS PRICE, SUM(XX_SpendingOfDiscount) AS SPENDING "
					   + "FROM XX_VMR_DiscountAppliDetail "
				   	   + "WHERE XX_VMR_DiscountRequest_ID="+getXX_VMR_DiscountRequest_ID();
		
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try 
			{	
				pstmt = DB.prepareStatement(SQL, get_Trx());
				rs = pstmt.executeQuery();
				
				if(rs.next())
				{
					String sql = "UPDATE XX_VMR_DiscountRequest"
						+ " SET TotalQty=" + rs.getInt("QTY") 
						+ "     ,XX_TotalPVPPlusTax=" + rs.getBigDecimal("PRICE")
						+ "		,XX_TotalSpendingOfDiscount=" + 	rs.getBigDecimal("SPENDING")
						+ " where XX_VMR_DiscountRequest_ID=" + getXX_VMR_DiscountRequest_ID();
					int no2 = DB.executeUpdate(get_Trx(), sql);
				}							
			}
			catch(Exception e)
			{	
				e.getMessage();
			}
			finally
			{				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		return save;
	}
	
	@Override
	protected boolean beforeDelete()
	{
		boolean delete = super.beforeDelete();
		
		if(delete){
			
			MVMRDiscountRequest discoRequest = new MVMRDiscountRequest(Env.getCtx(),getXX_VMR_DiscountRequest_ID(),get_Trx());
			
			String SQL = "SELECT SUM(XX_LoweringQuantity) AS QTY, SUM(XX_TotalPrice) AS PRICE, SUM(XX_SpendingOfDiscount) AS SPENDING "
					   + "FROM XX_VMR_DiscountAppliDetail "
				   	   + "WHERE XX_VMR_DiscountRequest_ID="+discoRequest.get_ID();
		
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try 
			{	
				pstmt = DB.prepareStatement(SQL, get_Trx());
				rs = pstmt.executeQuery();
				
				if(rs.next())
				{
					discoRequest.setTotalQty(rs.getInt("QTY")-getXX_LoweringQuantity());
					discoRequest.setXX_TotalPVPPlusTax(rs.getBigDecimal("PRICE").subtract(getXX_TotalPrice()));
					discoRequest.setXX_TotalSpendingOfDiscount(rs.getBigDecimal("SPENDING").subtract(getXX_SpendingOfDiscount()));
					discoRequest.save();
				}							
			}
			catch(Exception e)
			{	
				e.getMessage();
			}
			finally
			{				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		
		return delete;
		
	}	//	beforeDelete
	
	/*
	 *	Comprueba que un determinado producto con un determinado consecutivo de precio
	 *  este o no en la solicitud 
	 */
	private Integer getDiscountAppliDetailCount(Integer discountRequest, Integer product, Integer consecutive)
	{		
		Integer count=0;
		
		String SQL = "SELECT COUNT(*) AS Cuenta FROM XX_VMR_DiscountAppliDetail "
			   	   + "WHERE XX_VMR_DiscountRequest_ID="+discountRequest
			   	   + " AND M_Product_ID="+product
			   	   + " AND XX_VMR_PriceConsecutive_ID="+consecutive;
		PreparedStatement pstmt = DB.prepareStatement(SQL, get_Trx()); 
	    ResultSet rs = null;
		try
		{
			rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				count = rs.getInt("Cuenta");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{				
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return count;
	}
	
	/*
	 *	Obtengo el ID del Locator segun el Warehouse del producto 
	 */
	private Integer getLocator(Integer M_Warehouse_ID){
		
		Integer locator=0;
		String SQL = "SELECT l.M_Locator_ID FROM M_Locator l, M_Warehouse w "
				   + "WHERE l.M_Warehouse_ID=w.M_Warehouse_ID AND IsDefault='Y' "
				   + "AND w.M_Warehouse_ID="+M_Warehouse_ID+" AND l.IsActive='Y'";
		PreparedStatement pstmt = DB.prepareStatement(SQL, get_Trx()); 
	    ResultSet rs = null;
		try
		{
			rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				locator = rs.getInt("M_Locator_ID");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{				
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return locator;
	}
}
