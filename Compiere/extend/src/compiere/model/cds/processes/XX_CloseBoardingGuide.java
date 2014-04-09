package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MOrder;
import compiere.model.cds.MVLOBoardingGuide;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.X_XX_VLO_BoardingGuide;

public class XX_CloseBoardingGuide extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		X_XX_VLO_BoardingGuide imports = new X_XX_VLO_BoardingGuide(getCtx(),getRecord_ID(),get_TrxName());
	
		Boolean seguir = false;
		seguir = ADialog.ask(1, new Container(), "XX_CloseGuideMessage");
		
		if(seguir)
		{
			Integer orderid = 0;
			
			imports.setXX_CloseGuideCheck(true);
						
			String SQL = ("SELECT A.C_ORDER_ID AS ORDERID " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
					"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
					"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+getRecord_ID()+"' ");
			
			PreparedStatement pstmt = null; 
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(SQL, null); 
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					orderid = rs.getInt("ORDERID");
			    	
			    	MOrder order = new MOrder(Env.getCtx(),orderid,null);
			    	
			    	order.setXX_CloseGuideCheck(true);
			    	order.save();
			    	
				}			    
			    imports.save();
			    commit();
			}
			catch (SQLException e) {
				e.printStackTrace();
			} finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}	
			
			
			
			//Realizado por Rosmaira Arvelo
			if(imports.isXX_CloseGuideCheck()==true)
			{				
				MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(imports.get_ID()),get_Trx());
				MVLOBoardingGuide boarding = new MVLOBoardingGuide(Env.getCtx(),task.getXX_ActualRecord_ID(),get_Trx());
				MOrder order = new MOrder(Env.getCtx(),getOrderID(boarding.get_ID()),get_Trx());
				
				//Llama al proceso cerrar alerta
				if((boarding.get_ID()==imports.get_ID())&&(order.get_ID()!=0)&&(order.getXX_Alert6()==true)&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("CE")))
				{					
					MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task.get_ID()); 
					mpi.save();
						
					ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
					pi.setRecord_ID(mpi.getRecord_ID());
					pi.setAD_PInstance_ID(mpi.get_ID());
					pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
					pi.setClassName(""); 
					pi.setTitle(""); 
						
					ProcessCtl pc = new ProcessCtl(null ,pi,null); 
					pc.start();				
				}
			}//fin RArvelo
				
		}
		return "";
	}
	
	//Realizado por Rosmaira Arvelo
	/*
	 *	Obtengo el ID de la tarea critica segun el Boarding Guide
	 */
	private Integer getCriticalTaskForClose(Integer boardingGuide){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			   + "WHERE XX_ActualRecord_ID="+boardingGuide;
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;	
		try
		{
		pstmt = DB.prepareStatement(SQL, null); 
	    rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			criticalTask = rs.getInt("XX_VMR_CriticalTaskForClose_ID");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	
		
		return criticalTask;
	}//fin getCriticalTaskForClose
	
	/*
	 *	Obtengo el ID de la orden segun el Boarding Guide
	 */
	private Integer getOrderID(Integer boardingGuide){
		
		Integer id=0;
		String SQL = "SELECT C_Order_ID FROM C_Order "
			       + "WHERE XX_VLO_BoardingGuide_ID="+boardingGuide;
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;	
	try
	{
		pstmt = DB.prepareStatement(SQL, null); 
	    rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			id = rs.getInt("C_Order_ID");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}  finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	
		
		return id;
	}//fin getOrderID RArvelo

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
