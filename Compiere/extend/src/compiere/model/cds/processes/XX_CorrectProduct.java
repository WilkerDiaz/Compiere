package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.ProcessDialog;
import org.compiere.apps.ProcessParameter;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MPInstance;
import org.compiere.model.X_C_Order;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.swing.CPanel;
import org.compiere.util.ASyncProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MInOut;
import compiere.model.cds.MInOutLine;
import compiere.model.cds.MVLOUnsolicitedProduct;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_Section;

/**
*
* @author Rosmaira Arvelo
*/
public class XX_CorrectProduct extends SvrProcess implements ASyncProcess{
	
	private int m_WindowNo = Env.getCtx().getContextAsInt("#XX_L_W_UNSOLICITEDPRODUCT_ID");	
	private CPanel mainPanel = new CPanel();	
	
	@Override
	protected String doIt() throws Exception 
	{
		MVLOUnsolicitedProduct product = new MVLOUnsolicitedProduct(getCtx(),getRecord_ID(),get_TrxName());		
		
		//** Valido si el chequeo esta completado para poder mover el inventario
		MInOutLine inOutLine = new MInOutLine(Env.getCtx(),product.getM_InOutLine_ID(),null);
		MInOut inOut = new MInOut(Env.getCtx(),inOutLine.getM_InOut_ID(),null);
		
		if(inOut.getDocStatus().equals("CO"))
		{				
			int lin = product.getXX_VMR_Line_ID();
			X_XX_VMR_Line linea = new X_XX_VMR_Line(getCtx(),lin,null);
			
			int sec = product.getXX_VMR_Section_ID();
			X_XX_VMR_Section section = new X_XX_VMR_Section(getCtx(),sec,get_TrxName());
			
			if(linea.getValue().equals("99") || section.getValue().equals("99"))
			{
				//Crea una variable de sesion
				Env.getCtx().setContext("#XX_PVP_ID", product.getXX_VMR_Department_ID());
				
				//Ejecuta un Proceso de Compiere desde otro proceso
				MPInstance mpi = new MPInstance( getCtx(), getCtx().getContextAsInt("#XX_L_PROCESSVALIDATEPRODUCT_ID"), getRecord_ID());
				mpi.save();
				
				ProcessInfo pi = new ProcessInfo("", getCtx().getContextAsInt("#XX_L_PROCESSVALIDATEPRODUCT_ID"));
				pi=this.getProcessInfo();
				pi.setAD_PInstance_ID(mpi.get_ID());
				pi.setAD_Process_ID(getCtx().getContextAsInt("#XX_L_PROCESSVALIDATEPRODUCT_ID"));
				pi.setClassName("");
				pi.setTitle("Validate Product");
				
				ProcessDialog pd = new ProcessDialog(getCtx().getContextAsInt("#XX_L_PROCESSVALIDATEPRODUCT_ID"));
				pd.init();
				pd.pack();
	
				ProcessParameter pp = new ProcessParameter(Env.getFrame((Container)pd), EnvConstants.WINDOW_INFO, pi);
				
				if (pp.initDialog())
				{
					pp.setModal(false);
					pp.getModalCtl(pd,pi,get_Trx());
					pp.setVisible(true);
					
				}
				else
				{
					ProcessCtl worker = new ProcessCtl(pd ,pi,get_Trx());
					worker.start();
				}
				
				//Borra la variable de sesion
				Env.getCtx().remove("#XX_PVP_ID");
				
				while(pp.isVisible())
					Thread.sleep(1000);
			}
			else{
				
				if(ADialog.ask(m_WindowNo, this.mainPanel,  Msg.getMsg(getCtx(), "CorrectProduct?")))
				{
					// Hago el movimiento de inventario del locator "Por Validar"
				    // al Locator "Chequeado" 	
					BigDecimal quantity = inOutLine.getMovementQty();
					
					MMovement movement = new MMovement(Env.getCtx(),0,null);
					movement.setC_DocType_ID(1000124);
					movement.save();
					    
					MMovementLine movementLine = new MMovementLine(Env.getCtx(),0,null);
					movementLine.setM_AttributeSetInstance_ID(inOutLine.getM_AttributeSetInstance_ID());
					movementLine.setM_Movement_ID(movement.get_ID());
					movementLine.setM_Locator_ID(inOutLine.getM_Locator_ID());
					movementLine.setM_LocatorTo_ID(getLocatorCheck(inOutLine.getM_Warehouse_ID()));
					movementLine.setM_Product_ID(inOutLine.getM_Product_ID());
					movementLine.setMovementQty(quantity);
					movementLine.save();
					    
					movement.setDocAction(MMovement.DOCACTION_Complete);
					DocumentEngine.processIt(movement, MMovement.DOCACTION_Complete);
					movement.save();
					   
					product.setXX_ValidateProduct(true);
					product.setXX_Record_ID(product.getM_Product_ID());
					product.save();
									
				}
			}			
		}
		else
		{
			ADialog.info(m_WindowNo, this.mainPanel, "ValidateProdError");
		}
		
		return "";
	}
	
	@Override
	protected void prepare() {
		
	}
	
	@Override
	public void executeASync(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUILocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockUI(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockUI(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 *	Obtengo el ID del Locator "Chequeado"
	 */
	private Integer getLocatorCheck(Integer M_Warehouse_ID){
		
		Integer locator=0;
		String SQL = "SELECT l.M_Locator_ID FROM M_Locator l, M_Warehouse w "
				   + "WHERE l.M_Warehouse_ID=w.M_Warehouse_ID AND IsDefault='Y' "
				   + "AND w.M_Warehouse_ID="+M_Warehouse_ID+" AND l.IsActive='Y'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				locator = rs.getInt("M_Locator_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return locator;
	}

}
