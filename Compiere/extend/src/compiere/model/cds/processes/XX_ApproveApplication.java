package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_Inventory;
import org.compiere.model.X_Ref_Quantity_Type;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MVMRDiscountAppliDetail;
import compiere.model.cds.MVMRDiscountRequest;
import compiere.model.cds.MVMRDiscountType;
import compiere.model.cds.X_XX_VMR_DiscountAppliDetail;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;

/**
 * 
 * @author Rosmaira Arvelo
 *
 */
public class XX_ApproveApplication extends SvrProcess{

	private int m_WindowNo = Env.getCtx().getContextAsInt("#XX_L_W_DISCOUNTREQUEST_ID");	
	private CPanel mainPanel = new CPanel();
	
	@Override
	protected String doIt() throws Exception 
	{
		MVMRDiscountRequest discountRequest = new MVMRDiscountRequest(Env.getCtx(),getRecord_ID(),get_Trx());
		Integer attributeSetInstance = 0;
		BigDecimal unitPurchasePrice = new BigDecimal(0);
		Integer consecutive = 0;
		
		//Comprueba que existan productos en la solicitud de rebaja
		if(getDiscountAppliDetailCount(discountRequest.get_ID())!=0)
		{
			//Busca todos los productos de la solicitud de rebaja
			String SQL = "SELECT * FROM XX_VMR_DiscountAppliDetail "
				   + "WHERE XX_VMR_DiscountRequest_ID="+discountRequest.get_ID();
			PreparedStatement pstmt = null; 
		    ResultSet rs = null;	
			try
			{
				pstmt = DB.prepareStatement(SQL, null); 
			    rs = pstmt.executeQuery();			
			    
				while (rs.next())
				{	
					//Crea el nuevo Consecutivo de Precio con el precio y lote seleccionados
					X_XX_VMR_PriceConsecutive priceConsecutive = new X_XX_VMR_PriceConsecutive(Env.getCtx(),0,get_Trx());
					X_XX_VMR_PriceConsecutive priceConsecutiveViejo = new X_XX_VMR_PriceConsecutive(Env.getCtx(),rs.getInt("XX_VMR_PriceConsecutive_ID"),get_Trx());
					priceConsecutive.setM_Product_ID(rs.getInt("M_Product_ID"));
					priceConsecutive.setXX_SalePrice(rs.getBigDecimal("XX_DiscountPrice"));
					priceConsecutive.setXX_VMR_GeneratedBy_ID(rs.getInt("XX_VMR_DiscountRequest_ID"));
					
					String SQL2 = "SELECT M_AttributeSetInstance_ID, XX_UnitPurchasePrice " 
						+ "FROM XX_VMR_PriceConsecutive "
						+ "WHERE XX_VMR_PriceConsecutive_ID="+rs.getInt("XX_VMR_PriceConsecutive_ID");
					
					PreparedStatement pstmt1 = null; 
				    ResultSet rs1 = null;	
					try
					{
						pstmt1 = DB.prepareStatement(SQL2, null); 
					    rs1 = pstmt1.executeQuery();			
					    
						while (rs1.next())
						{
							attributeSetInstance = rs1.getInt("M_AttributeSetInstance_ID");
							unitPurchasePrice = rs1.getBigDecimal("XX_UnitPurchasePrice");
						}
					}				
					catch (SQLException e)
					{
						e.printStackTrace();
					} finally{
						DB.closeResultSet(rs1);
						DB.closeStatement(pstmt1);
					}	
					
					//Busca el último Consecutivo de Precio, para crear el siguiente 
					String SQL3 = "SELECT MAX(XX_PriceConsecutive) AS Maximo "
							    + "FROM XX_VMR_PriceConsecutive "
							    + "WHERE M_Product_ID="+rs.getInt("M_Product_ID");
					
					PreparedStatement pstmt2 = null; 
				    ResultSet rs2 = null;	
					try
					{
						pstmt2 = DB.prepareStatement(SQL3, get_Trx()); 
					    rs2 = pstmt2.executeQuery();			
					    
						while (rs2.next())
						{
							consecutive = rs2.getInt("Maximo");
							consecutive = consecutive+1;
						}
					}				
					catch (SQLException e)
					{
						e.printStackTrace();
					} finally{
						DB.closeResultSet(rs2);
						DB.closeStatement(pstmt2);
					}
					
					priceConsecutive.setM_AttributeSetInstance_ID(attributeSetInstance);					
					priceConsecutive.setXX_UnitPurchasePrice(unitPurchasePrice);
					priceConsecutive.setXX_PriceConsecutive(consecutive);					
					priceConsecutive.setXX_ConsecutiveOrigin("R");
					priceConsecutive.setXX_Status_Sinc(false);
					priceConsecutive.save(); 

					// Luego de salvar nuestro consecutivo le debemos de poner como fecha de creacion la misma del consecutivo padre
					
					String SQL5 =
						"UPDATE XX_VMR_PriceConsecutive SET "
					    +"CREATED = (select created from xx_vmr_priceconsecutive where m_product_id="+priceConsecutive.getM_Product_ID()+ " and xx_priceconsecutive="+priceConsecutiveViejo.getXX_PriceConsecutive()+" and rownum=1) "+
					    " WHERE XX_VMR_PriceConsecutive_ID="+priceConsecutive.get_ID();
					    
						DB.executeUpdate(get_TrxName(), SQL5 );	 
					
										
					//Guarda el nuevo Consecutivo de Precio en la línea de la Solicitud de Rebaja
					MVMRDiscountAppliDetail discountAppliDet = new MVMRDiscountAppliDetail(Env.getCtx(),rs.getInt("XX_VMR_DiscountAppliDetail_ID"),get_Trx());
					discountAppliDet.setXX_PriceConsecutive_ID(priceConsecutive.get_ID());
					discountAppliDet.save();
					
					String SQL6 =
						"UPDATE xx_vmr_discountapplidetail "
					    +" set xx_priceconsecutive_id="+priceConsecutive.get_ID()+
					    " WHERE xx_priceconsecutive_id is null and xx_vmr_discountapplidetail_id="+rs.getInt("XX_VMR_DiscountAppliDetail_ID");
					    
						DB.executeUpdate(get_TrxName(), SQL6 );
					
				}
				
				//setea el dia, mes y año de aprobacion en el maestro
				discountRequest.setXX_Status("AP");
				discountRequest.setProcessing(true);
				discountRequest.save();
				
				String SQL7 =
					"UPDATE xx_vmr_discountrequest "
				    +" set xx_dayupdate=(select extract(day from sysdate) from dual),xx_monthupdate=(select extract(month from sysdate) from dual),XX_YEARUPDATE=(select extract(year from sysdate) from dual)"+
				    " WHERE xx_vmr_discountrequest_id=" + discountRequest.get_ID();
				
				DB.executeUpdate(get_TrxName(), SQL7 );

				
				
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			} finally{
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
			}			
		}
		else //Si la Solicitud de Rebaja no tiene productos asociados
		{
			ADialog.warn(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"RequestNotApproved"));
		}
		commit();
		// inicio vlomonaco
		// La parte inferior se encarga de rebajar las cantidades en inventario
		
		//debo buscar el locator predeterminado para mi warehouse
		String SQL3 = "SELECT M_Locator_ID "
				    + "FROM M_Locator "
				    + "WHERE IsDefault='Y' AND M_Warehouse_ID=" + discountRequest.getM_Warehouse_ID();
		int locator = 0;
		PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;	
		try
		{
			pstmt2 = DB.prepareStatement(SQL3, null); 
		    rs2 = pstmt2.executeQuery();			
		    
			while (rs2.next())
			{
				locator = rs2.getInt("M_Locator_ID");
			}

		}				
		catch (SQLException e)
		{
			e.printStackTrace();
		} finally{
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs2);
		}	

				
		int doctype = 1000336;
		MInventory inventory = null;
		boolean save=false;
				
		String SQL = "select * from XX_VMR_DiscountAppliDetail where XX_VMR_DiscountRequest_id = " + getRecord_ID();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			//Creo las lineas del inventory
			int i=0;
			BigDecimal qty = BigDecimal.ZERO;
			BigDecimal qtyToSet = BigDecimal.ZERO;
			X_XX_VMR_DiscountAppliDetail detail = null;
			X_XX_VMR_PriceConsecutive priceConsecutive = null;
			Hashtable<Integer, Integer> aSIQty = new Hashtable<Integer, Integer>();
			
			while (rs.next())
			{
				// unicamente reduzco las cantidades si se trata de una rebaja a cero
				MVMRDiscountType tipoDescuento = new MVMRDiscountType(Env.getCtx(), rs.getInt("XX_VMR_DiscountType_ID"), get_Trx());
				if (tipoDescuento.getName().contains("CERO"))
				{	
					if(!save){
						//Creo la cabecera del inventory
						inventory = new MInventory( getCtx(), 0, null);
						inventory.setC_DocType_ID(doctype);
						inventory.setAD_Org_ID(discountRequest.getAD_Org_ID());
						inventory.setM_Warehouse_ID(discountRequest.getM_Warehouse_ID());
						inventory.setDescription("Movimiento de inventario inducido por Rebaja a cero numero " + discountRequest.getValue());
						inventory.save();
						save=true;
					}
				
					i++;
					detail= new X_XX_VMR_DiscountAppliDetail( Env.getCtx(), rs.getInt("XX_VMR_DiscountAppliDetail_ID"), get_Trx());
					priceConsecutive = new X_XX_VMR_PriceConsecutive( Env.getCtx(), detail.getXX_VMR_PriceConsecutive_ID(), get_Trx());
					
					MInventoryLine inventoryLine = null;
					
					if(aSIQty.containsKey(priceConsecutive.getM_AttributeSetInstance_ID()) && priceConsecutive.getM_AttributeSetInstance_ID()!=0){
						
						inventoryLine = new MInventoryLine( Env.getCtx(), aSIQty.get(priceConsecutive.getM_AttributeSetInstance_ID()), get_Trx());
						
						qtyToSet = BigDecimal.ZERO;
						qtyToSet = inventoryLine.getQtyCount();
						qtyToSet = qtyToSet.subtract(rs.getBigDecimal("XX_LoweringQuantity"));
						
						inventoryLine.setQtyCount(qtyToSet);
					}
					else{
						
						inventoryLine = new MInventoryLine( getCtx(), 0, get_Trx());
						inventoryLine.setLine(i);
						inventoryLine.setAD_Org_ID(discountRequest.getAD_Org_ID());
						inventoryLine.setM_Inventory_ID(inventory.get_ID());
						inventoryLine.setM_Locator_ID(locator);
						inventoryLine.setInventoryType("D");
						inventoryLine.setM_Product_ID(rs.getInt("M_Product_ID"));
						
						qty = setQtyBook( priceConsecutive.getM_AttributeSetInstance_ID(), rs.getInt("M_Product_ID"), locator);
						inventoryLine.setQtyBook(qty);
						
						inventoryLine.setQtyCount(qty.subtract(rs.getBigDecimal("XX_LoweringQuantity")));
						
						if(priceConsecutive.getM_AttributeSetInstance_ID()>0)
							inventoryLine.setM_AttributeSetInstance_ID(priceConsecutive.getM_AttributeSetInstance_ID());
						
						inventoryLine.save();
						aSIQty.put(priceConsecutive.getM_AttributeSetInstance_ID(), inventoryLine.get_ID());
					}

					inventoryLine.save();
					
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error al actualizar cantidades en inventario");
		} finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		// fin vlomonaco
		if(save){
			inventory.setDocAction(X_M_Inventory.DOCACTION_Complete);
			DocumentEngine.processIt(inventory, X_M_Inventory.DOCACTION_Complete);
			inventory.save();
		}
		else
		{
			System.out.println("Error al actualizar las cantidades en inventario");
		}
		commit();
		
		return "";
	}

	@Override
	protected void prepare() {

	}

	/*
	 *	Obtengo la cantidad de productos a rebajar
	 */
	private Integer getDiscountAppliDetailCount(Integer discountRequest)
	{		
		Integer count=0;
		String SQL = "SELECT COUNT(XX_VMR_DiscountAppliDetail_ID) AS Cuenta FROM XX_VMR_DiscountAppliDetail "
			   + "WHERE XX_VMR_DiscountRequest_ID="+discountRequest;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				count = rs.getInt("Cuenta");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return count;
	}
	
	/**
	 * kviiksaar
	 * 
	 * Returns the current Book Qty for given parameters or 0
	 * 
	 * @param M_AttributeSetInstance_ID
	 * @param M_Product_ID
	 * @param M_Locator_ID
	 * @return
	 * @throws Exception
	 */
	public BigDecimal setQtyBook (int M_AttributeSetInstance_ID, int M_Product_ID, int M_Locator_ID) throws Exception {
		
		// Set QtyBook from first storage location
		BigDecimal bd = null;
		String sql = "SELECT QtyOnHand FROM M_Storage "
			+ "WHERE M_Product_ID=?"	//	1
			+ " AND M_Locator_ID=?"		//	2
			+ " AND M_AttributeSetInstance_ID=?";
		if (M_AttributeSetInstance_ID == 0)
			sql = "SELECT SUM(QtyOnHand) FROM M_Storage "
			+ "WHERE M_Product_ID=?"	//	1
			+ " AND M_Locator_ID=?";	//	2
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			if (M_AttributeSetInstance_ID != 0)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				bd = rs.getBigDecimal(1);
				if (bd != null)
					return bd;
			} else {
				// gwu: 1719401: clear Booked Quantity to zero first in case the query returns no rows, 
				// for example when the locator has never stored a particular product.
				return new BigDecimal(0);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			throw new Exception(e.getLocalizedMessage());
		} finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return new BigDecimal(0);
	}
}
