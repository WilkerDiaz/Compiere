package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.X_I_Inventory;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_ProductBatch;

public class CreateLotInventory extends SvrProcess  {

	/**
	 * @param args
	 */
	MAttributeSetInstance NuevoAtributo = null;

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		As400DbManager As = new As400DbManager();
		As.conectar();
		String sql = null;
		X_XX_VMR_ProductBatch pb =null;
		try 
		{
		
			//puedo eliminar todos los datos de XX_ProductBatch y las Creaciones de Instancias
			
		/*	sql = "select * from BECOFILE.LOTES_CDS1 WHERE IMPORTADO = 'N'"; 
			Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs= As.realizarConsulta(sql,sentencia);
			
				while(rs.next())
				{
						// Realizamos el pase de As a Compiere	
						pb = new X_XX_VMR_ProductBatch(getCtx(), 0, null);
						pb.setProductValue(rs.getString("CODPRO"));
						pb.setLot(rs.getString("LOTE"));
						pb.setPriceActual(rs.getBigDecimal("PRECOM"));
						pb.setXX_SalePrice(rs.getBigDecimal("PREVEN"));
						pb.setIsCreated(false);
						if(pb.save()){
						   rs.updateString("IMPORTADO", "Y");
						   rs.updateRow();
						}else
							updateExist(rs);
				}
			As.desconectar();
			sentencia.close();
			rs.close();*/
			//Actualizamos Los ID de Productos
			//updateIdProduct();
			
			//Creamos los Lotes
			//insertInstance();
			//Insertamos en la Inventory
			//insertI_Inventory();
			
			// estamos actualizando El campo Lote con el ID Del Atributo correspondiente	
			//UpdateLoteProduct();
			// Set del almacen, Ubicacion y Organizacion
			//UpdateWarehouse();
			
			//new Utilities().ActuaInvPedido(new X_XX_VMR_Order(getCtx(), 1003826, null), 0, 1000082, Env.getCtx().getContextAsInt("#XX_L_LOCATORCDENTRANSITO_ID"));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.saveError("Error", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		
		return null;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
	private void insertInstance()
	{
		MProduct producto = null;
		String sql = "select * from XX_VMR_ProductBatch where IsCreated = 'N' and M_Product_ID IS NOT NULL";
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs;
		X_XX_VMR_ProductBatch pb =null;
		try 
		{
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				pb = new X_XX_VMR_ProductBatch(getCtx(), rs, null);
				producto = new MProduct(getCtx(), rs.getInt("M_Product_ID"), null);
			    NuevoAtributo = new MAttributeSetInstance(getCtx(), 0, null);
		    	NuevoAtributo.setLot(rs.getString("Lot"));
		    	NuevoAtributo.setDescription("<<"+rs.getString("Lot")+">>");
		    	NuevoAtributo.setIsActive(true);
		    	if (producto.getM_AttributeSet_ID() > 0)
		    		 NuevoAtributo.setM_AttributeSet_ID(producto.getM_AttributeSet_ID());
		    	else
		    	     NuevoAtributo.setM_AttributeSet_ID(Env.getCtx().getContextAsInt("#XX_L_P_ATTRIBUTESETST_ID"));
		    	
		    	NuevoAtributo.set_Value("PriceActual", rs.getBigDecimal("PriceActual"));
		    	NuevoAtributo.set_Value("XX_SalePrice", rs.getBigDecimal("XX_SalePrice"));
		    	
		    	NuevoAtributo.save();
		    	
		    	pb.setM_AttributeSetInstance_ID(NuevoAtributo.getM_AttributeSetInstance_ID());
		    	pb.setIsCreated(true);
		    	pb.save();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateIdProduct()
	{
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE XX_VMR_ProductBatch i "
				+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product "
				+ "WHERE i.ProductValue=Value) "
				+ "WHERE i.M_Product_ID IS NULL " 
				+ "AND IsCreated='N' AND ProductValue IS NOT NULL";
		
		try {
			DB.executeUpdate(null, sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.saveError("Error", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
	private void updateExist(ResultSet rs)
	{
		try {
			String sql = "update XX_ProductBatch set ProductValue = '"+rs.getString("CODPRO")+"', Lot = '"+rs.getString("LOTE")+"', " +
			"PriceActual = "+rs.getBigDecimal("PRECOM")+", XX_SalePrice = "+rs.getBigDecimal("PREVEN")+" " +
			"where ProductValue = '"+rs.getString("CODPRO")+"' and Lot = '"+rs.getString("LOTE")+"'";
			DB.executeUpdate(null,sql);
		} catch (Exception e) {
			e.printStackTrace();
			log.saveError("Error", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
	}
	
	private String completarID(String tienda)
	{
		for (int i = tienda.length(); i<3; i++)
			tienda = "0"+tienda;
			
		return tienda;
	}
	
	private void UpdateLoteProduct()
	{
		String sql = "UPDATE I_Inventory i "
			+ "  Set  Lot = (select coalesce(to_nchar(M_AttributeSetInstance_ID), Lot) from XX_VMR_ProductBatch pb where pb.Lot = i.Lot and i.Value=pb.ProductValue )"
			+ "WHERE Lot is not null " 
			+ "AND Value IS NOT NULL";
	
		try {
			DB.executeUpdate(null,sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.saveError("Error", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
	private void UpdateWarehouse()
	{
		String sql = "UPDATE I_Inventory i "
			+ "  Set  M_Locator_ID = (select M_Locator_ID from M_Locator l where l.M_WareHouse_ID = " +
			  " (select M_WareHouse_ID from M_WareHouse m where m.Value = i.WarehouseValue and m.AD_Client_ID = "+getAD_Client_ID()+") and l.IsDefault = 'Y') " +
			  " , AD_Org_ID = (select AD_Org_ID from M_WareHouse w where w.M_WareHouse_ID = " +
			  " (select M_WareHouse_ID from M_WareHouse m where m.Value = i.WarehouseValue and m.AD_Client_ID = "+getAD_Client_ID()+"))" +
			  "WHERE WarehouseValue is not null";
	
		try {
			DB.executeUpdate(null,sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.saveError("Error", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
	
	private void insertI_Inventory()
	{
		X_I_Inventory Iinvent = null;
		As400DbManager As = new As400DbManager();
		As.conectar();
		String sql = "select * from BECOFILE.LOTES_CDS3 WHERE IMPORTADO = 'N' and cantidad > 0";
		try{
			Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs= As.realizarConsulta(sql,sentencia);
			
				while(rs.next())
				{
					Iinvent = new X_I_Inventory(getCtx(), 0, null);
					Iinvent.setWarehouseValue(completarID(rs.getString("TIENDA")));
					Iinvent.setQtyCount(rs.getBigDecimal("CANTIDAD"));
					//Iinvent.setValue(rs.getString("CODPRO"));
					Iinvent.setLot(rs.getString("LOTE"));
					Iinvent.set_Value("PriceActual", rs.getBigDecimal("PRECOM"));
					Iinvent.set_Value("XX_SalePrice", rs.getBigDecimal("PREVEN"));
					Iinvent.setSerNo("Y");
					if(Iinvent.save()) {
						rs.updateString("IMPORTADO", "Y");
						rs.updateRow();	
					}
					else
						log.saveError("Error", Msg.getMsg(getCtx(), rs.getString("CODPRO")+" "+ rs.getString("LOTE")));
				}
				rs.close();
				sentencia.close();
			
		As.desconectar();	
				
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	

}
