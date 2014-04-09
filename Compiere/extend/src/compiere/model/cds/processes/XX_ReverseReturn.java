package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MLocator;
import compiere.model.cds.MMovement;
import compiere.model.cds.MMovementLine;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VLO_ReturnOfProduct;

public class XX_ReverseReturn extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		int origen = 0;
		int destino = 0;
		String MLine = "";
		String Description = "";
		String devRetirar = "";
		MMovementLine moveLine = null;
		PreparedStatement prst = null;
		ResultSet rs = null;
		
		/**Obtengo los datos del movimiento a reversar*/
		MMovement move = new MMovement(Env.getCtx(), getRecord_ID(), get_TrxName());
		MMovement aux = new MMovement(Env.getCtx(), 0, get_TrxName());
		//
		Description = "Reverso de la devolución número "+move.getDocumentNo();
		origen = move.getM_Locator_ID();
		destino = move.getM_LocatorTo_ID();
		
		/*Estatus Anulado para la devolucion original*/
		move.setXX_Status("VO");
		move.save(); 
		
		/*Reversion de la devolucion*/
		aux.setDescription(Description);
		aux.setXX_VMR_Category_ID(move.getXX_VMR_Category_ID());
		aux.setXX_VMR_Department_ID(move.getXX_VMR_Department_ID());
		aux.setXX_PackageQuantity(move.getXX_PackageQuantity());
		aux.setDateReceived(move.getDateReceived());
		aux.setXX_RequestDate(move.getXX_RequestDate());
		aux.setMovementDate(move.getMovementDate());
		aux.setC_DocType_ID(move.getC_DocType_ID());
		aux.setXX_MovementFrom_ID(move.getM_Movement_ID());
		
		/*Locator en devolucion del almacen destino del movimiento anulado*/
		MLocator hasta = Utilities.obtenerLocatorDevolucion(
				move.getM_WarehouseTo_ID());
				aux.setM_Locator_ID(hasta.get_ID());
		
		aux.setM_LocatorTo_ID(origen);
		aux.setM_WarehouseFrom_ID(move.getM_WarehouseTo_ID());
		aux.setM_WarehouseTo_ID(move.getM_WarehouseFrom_ID());
		aux.save();
		commit();
		
		
		MLine = "SELECT *"
			+ " FROM M_MovementLine"
			+ " WHERE M_Movement_ID=" + move.getM_Movement_ID();
		
		/*Copia de los productos asociados a la devolución*/
		prst = DB.prepareStatement(MLine,null);
		rs = prst.executeQuery();
		try{
 			while(rs.next()){
				moveLine = new MMovementLine(Env.getCtx(), null, get_TrxName());
				moveLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
				moveLine.setLine(rs.getInt("Line"));
				moveLine.setC_TaxCategory_ID(rs.getInt("C_TaxCategory_ID"));
				moveLine.setDescription(rs.getString("Description"));
				moveLine.setM_AttributeSetInstance_ID(rs.getInt("M_AttributeSetInstance_ID"));
				moveLine.setM_AttributeSetInstanceTo_ID(rs.getInt("M_AttributeSetInstanceTo_ID"));
				moveLine.setM_Locator_ID(aux.getM_Locator_ID());
				moveLine.setM_LocatorTo_ID(aux.getM_LocatorTo_ID());
				moveLine.setM_Movement_ID(aux.getM_Movement_ID());
				moveLine.setMovementQty(rs.getBigDecimal("MovementQty"));
				moveLine.setConfirmedQty(rs.getBigDecimal("ConfirmedQty"));
				moveLine.setM_Product_ID(rs.getInt("M_Product_ID"));
				moveLine.setPriceActual(rs.getBigDecimal("PriceActual"));
				moveLine.setXX_ReturnMotive_ID(rs.getInt("XX_ReturnMotive_ID"));
				moveLine.setQtyRequired(rs.getBigDecimal("QtyRequired"));
				moveLine.setScrappedQty(rs.getBigDecimal("ScrappedQty"));
				moveLine.setTargetQty(rs.getBigDecimal("TargetQty"));
				moveLine.setTaxAmt(rs.getBigDecimal("TaxAmt"));
				moveLine.setXX_PriceConsecutive(rs.getInt("XX_PriceConsecutive"));
				moveLine.setXX_SalePrice(rs.getBigDecimal("XX_SalePrice"));
				moveLine.setXX_VMR_Brand_ID(rs.getInt("XX_VMR_Brand_ID"));
				moveLine.setXX_VMR_Line_ID(rs.getInt("XX_VMR_Line_ID"));
				moveLine.set_Value("XX_ApprovedQty", rs.getBigDecimal("QtyRequired"));
				moveLine.set_Value("XX_QuantityReceived", rs.getBigDecimal("QtyRequired"));
				moveLine.save();
			}
		}catch(SQLException e){
			System.out.println(e);
		}finally{
			rs.close();
			prst.close();
		}
		commit();
		
		/*Cambiar estatus de la devolucion reversada*/
		devRetirar = "SELECT * "
			+ " FROM XX_VLO_ReturnOfProduct"
			+ " WHERE M_Movement_ID=" + move.getM_Movement_ID();
		prst = DB.prepareStatement(devRetirar,null);
		rs = prst.executeQuery();
		try{
			if(rs.next()){
				X_XX_VLO_ReturnOfProduct Dev = new X_XX_VLO_ReturnOfProduct(Env.getCtx(), rs.getInt("XX_VLO_RETURNOFPRODUCT_ID"), get_TrxName());
				Dev.set_ValueNoCheck("XX_Status", "VO");
				Dev.save();
				commit();
			}
		}
		catch(SQLException e){
			System.out.println(e);
		}
		finally{
			rs.close();
			prst.close();
		}
		
		/*Autocompletar devolución*/
		aux.setXX_Status("RV");
				
		aux.setDocAction(X_M_Movement.DOCACTION_Complete);
	    DocumentEngine.processIt(aux, X_M_Movement.DOCACTION_Complete);
	    
	    
		aux.save();
		return "";	
	}

	@Override
	protected void prepare() {
				
	}

}
