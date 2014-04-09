package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.framework.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVLOBoardingGuide;
import compiere.model.cds.MVLOUnsolicitedProduct;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.X_XX_VMR_Order;

/**
 *  Display Windows de Tareas Criticas
 *
 *  @author     Rosmaira Arvelo
 *  @version    
 */

public class XX_DisplayWindowsCT extends SvrProcess {
	

	@Override
	protected String doIt() throws Exception {
		
		MVMRCriticalTaskForClose Task= new MVMRCriticalTaskForClose(getCtx(), getRecord_ID(), null);		
				
		//Muestra la ventana para ejecutar la tarea critica Asignacion de precio de ventas a un producto en la Distribucion
		if(Task.getXX_TypeTask().equals("AP"))
		{				
			MVMRDistributionHeader poLineProdDist = new MVMRDistributionHeader(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);
			
			if(poLineProdDist.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("XX_VMR_DistributionHeader_ID", poLineProdDist.getXX_VMR_DistributionHeader_ID());		
				String wind = Env.getCtx().getContext("#XX_L_W_DISTRIBUTIONHEADER_ID");
				Integer win = Integer.parseInt(wind);
				AWindow window = new AWindow();
				window.initWindow(win, query);
				AEnv.showCenterScreen(window);
							
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Muestra la ventana para ejecutar la tarea critica Asignacion de precio de ventas a un producto en la Distribucion con orden de compra
		if(Task.getXX_TypeTask().equals("AP1"))
		{
			MVMRDistributionHeader distribProdDet = new MVMRDistributionHeader(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);			
			
			if(distribProdDet.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("XX_VMR_DistributionHeader_ID", distribProdDet.get_ID());		
				String wind = Env.getCtx().getContext("#XX_L_W_DISTRIBUTIONHEADER_ID");
				Integer win = Integer.parseInt(wind);
				AWindow window = new AWindow();
				window.initWindow(win, query);
				AEnv.showCenterScreen(window);
							
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Muestra la ventana para ejecutar la tarea critica Asociar los Productos de una O/C
		if(Task.getXX_TypeTask().equals("AS"))
		{
			MOrder order = new MOrder(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);
			
			if(order.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("C_Order_ID", order.get_ID());		
				AWindow window = new AWindow();
				window.initWindow(181, query);
				AEnv.showCenterScreen(window);
						
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Muestra la ventana para ejecutar la tarea critica Validar un Producto
		if(Task.getXX_TypeTask().equals("VP"))
		{		
			MVLOUnsolicitedProduct unsolProd = new MVLOUnsolicitedProduct(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);
			
			if(unsolProd.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("XX_VLO_UnsolicitedProduct_ID", unsolProd.get_ID());		
				String wind = Env.getCtx().getContext("#XX_L_W_UNSOLICITEDPRODUCT_ID");
				Integer win = Integer.parseInt(wind);
				AWindow window = new AWindow();
				window.initWindow(win, query);
				AEnv.showCenterScreen(window);
						
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Muestra la ventana para ejecutar la tarea critica Asignar Codigo Arancelario a un producto
		if(Task.getXX_TypeTask().equals("AC"))
		{
			MProduct product = new MProduct(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);
			
			if(product.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("M_Product_ID", product.get_ID());
				AWindow window = new AWindow();
				window.initWindow(140, query);
				AEnv.showCenterScreen(window);
						
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Muestra la ventana para ejecutar la tarea critica Aprobacion de O/C con o sin sobregiro
		if((Task.getXX_TypeTask().equals("AO"))||(Task.getXX_TypeTask().equals("AO1")))
		{
			MOrder order = new MOrder(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);
			
			if(order.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("C_Order_ID", order.get_ID());
				AWindow window = new AWindow();
				window.initWindow(181, query);
				AEnv.showCenterScreen(window);
						
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Coloca read only el boton de ejecutar tarea citica cuando ésta es O/C por recibir
		if(Task.getXX_TypeTask().equals("OR"))
		{
			Task.setXX_PWFValProd("Y");
			Task.save();
		}
		//Muestra la ventana para ejecutar la tarea critica Ordenes de compra pendiente por chequear
		if(Task.getXX_TypeTask().equals("CO"))
		{			
			MInOut inOut = new MInOut(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);
			
			if(inOut.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("M_InOut_ID", Task.getXX_ActualRecord_ID());
				AWindow window = new AWindow();
				window.initWindow(184, query);
				AEnv.showCenterScreen(window);
						
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Muestra la ventana para ejecutar la tarea critica Pedidos pendientes por etiquetar con distribucion directa
		if(Task.getXX_TypeTask().equals("EP"))
		{
			X_XX_VMR_Order dd = new X_XX_VMR_Order(Env.getCtx(),getPendingLabel(Task.getXX_ActualRecord_ID()),null);
			
			if(dd.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("XX_VMR_Order_ID", dd.get_ID());
				String wind = Env.getCtx().getContext("#XX_L_W_DDPENDINGLABELS_ID");
				Integer win = Integer.parseInt(wind);
				AWindow window = new AWindow();
				window.initWindow(win, query);
				AEnv.showCenterScreen(window);
				
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
		//Muestra la ventana para ejecutar la tarea critica Pedidos pendientes por etiquetar con Pre-Distribucion 
		if(Task.getXX_TypeTask().equals("EP1"))
		{			
			Query query = Query.getEqualQuery("C_Order_ID", Task.getXX_ActualRecord_ID());
			String wind = Env.getCtx().getContext("#XX_L_W_PENDINGLABELS_ID");
			Integer win = Integer.parseInt(wind);
			AWindow window = new AWindow();
			window.initWindow(win, query);
			AEnv.showCenterScreen(window);
				
			while(window.isVisible())
				Thread.sleep(1000);
			
		}
		//Muestra la ventana para ejecutar la tarea critica Pedidos pendientes por etiquetar con Re-Distribucion
		if(Task.getXX_TypeTask().equals("EP2"))
		{			
			Query query = Query.getEqualQuery("SUBSTR(XX_OrderBecoCorrelative,5)", Task.getXX_ActualRecord_ID());
			String wind = Env.getCtx().getContext("#XX_L_W_PENDINGLABELS_ID");
			Integer win = Integer.parseInt(wind);
			AWindow window = new AWindow();
			window.initWindow(win, query);
			AEnv.showCenterScreen(window);
				
			while(window.isVisible())
				Thread.sleep(1000);
			
		}
		//Muestra la ventana para ejecutar la tarea critica Cálculo de Factores o Cierre de Expediente
		if((Task.getXX_TypeTask().equals("CF"))||(Task.getXX_TypeTask().equals("CE")))
		{
			MVLOBoardingGuide boardingGuide = new MVLOBoardingGuide(Env.getCtx(),Task.getXX_ActualRecord_ID(),null);
			
			if(boardingGuide.get_ID()!=0)
			{
				Query query = Query.getEqualQuery("XX_VLO_BoardingGuide_ID", Task.getXX_ActualRecord_ID());
				String wind = Env.getCtx().getContext("#XX_L_W_BOARDINGGUIDE_ID");
				Integer win = Integer.parseInt(wind);
				AWindow window = new AWindow();
				window.initWindow(win, query);
				AEnv.showCenterScreen(window);
						
				while(window.isVisible())
					Thread.sleep(1000);
			}
		}
				
		return "";
	}
	
	//Realizado por Rosmaira Arvelo
    /*
	 *	Obtengo el ID del pedido DD por etiquetar segun la orden
	 */
	private Integer getPendingLabel(Integer order){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_Order_ID FROM XX_VMR_Order "
			   + "WHERE C_Order_ID="+order;
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
	    ResultSet rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			criticalTask = rs.getInt("XX_VMR_Order_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return criticalTask;
	}
		
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	
}
