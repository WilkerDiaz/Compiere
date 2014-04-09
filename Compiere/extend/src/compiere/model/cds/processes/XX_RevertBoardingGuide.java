package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_XX_VLO_BoardingGuide;

public class XX_RevertBoardingGuide extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
	
		Integer orderID = 0;
		//Boolean aux = true;
		Integer ordenRecibida = 0;
		X_XX_VLO_BoardingGuide imports = new X_XX_VLO_BoardingGuide(getCtx(),getRecord_ID(),get_TrxName());
		
		Integer importId = getRecord_ID();
		
		String SQL1 = ("SELECT COUNT (A.C_ORDER_ID) AS OCRE " +
				"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
				"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
				"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' " +
				"AND A.XX_ORDERSTATUS= 'RE' ");
		 
		try
		{
			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
		    ResultSet rs1 = pstmt1.executeQuery();
		    
		    if(rs1.next())
		    {
		    	ordenRecibida = rs1.getInt("OCRE");
		    }
		    rs1.close();
		    pstmt1.close();
		}
		catch (Exception e) {
			log.log(Level.SEVERE,SQL1,e);
			return "";
		}

		if(ordenRecibida == 0)
		{
			String SQL = ("SELECT A.C_ORDER_ID AS ORDERID, DOCUMENTNO AS NUMORDER " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
					"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
					"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' ");

			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			    ResultSet rs = pstmt.executeQuery();
			    
			    while(rs.next())
			    {
			    	orderID = rs.getInt("ORDERID");
			    	
			    	MOrder orden = new MOrder(getCtx(),orderID,null);
			    	
			    	orden.setXX_OrderStatus("LCD");
				    orden.setXX_DefinitiveFactor(new BigDecimal(0));
				    imports.setXX_ProcessedImport(false);
				    orden.save();
				    imports.save();

			    }
				rs.close();
				pstmt.close();
			    
			}
			catch (Exception e) {
				log.log(Level.SEVERE,SQL,e);
				return "";
			}
		}
		else
		{
			String SQL = ("SELECT A.C_ORDER_ID AS ORDERID, DOCUMENTNO AS NUMORDER " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
					"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
					"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' " +
					"AND A.XX_ORDERSTATUS= 'RE' ");
			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			    ResultSet rs = pstmt.executeQuery();
			    
			    Vector <String> order = new Vector <String>();
			    
			    while(rs.next())
			    {
			    	//orderID = rs.getInt("ORDERID");
			    	
			    	//MOrder orden = new MOrder(getCtx(),orderID,null);
			   		order.add(rs.getString("NUMORDER"));

			    }
				rs.close();
				pstmt.close();

				if(order.size() !=0)
				{
					int i;
				    String todasREOC = "";
				    
				    for (i=0; i<order.size(); i++)
				    {
				    	todasREOC +=" "+ order.get(i);
				    }// end for
					
					ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_RevertGuide", new String[] {todasREOC}));
				}	
				
			}
			catch(Exception a)
			{	
				log.log(Level.SEVERE,SQL,a);
				return "";
			}
			
		}// end else
		
		
		/*String SQL = ("SELECT A.C_ORDER_ID AS ORDERID, DOCUMENTNO AS NUMORDER " +
				"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
				"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
				"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' ");

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    Vector <String> order = new Vector <String>();
		    
		    while(rs.next())
		    {
		    	orderID = rs.getInt("ORDERID");
		    	
		    	MOrder orden = new MOrder(getCtx(),orderID,null);
		    	
		    	if(orden.getXX_OrderStatus().equalsIgnoreCase("RE"))
		    	{
		    		order.add(rs.getString("NUMORDER"));
		    	}
		    	else
		    	{
		    		orden.setXX_OrderStatus("LCD");
			    	orden.setXX_DefinitiveFactor(new BigDecimal(0));
			    	imports.setXX_ProcessedImport(false);
			    	orden.save();
			    	imports.save();

		    	}

		    }
			rs.close();
			pstmt.close();

			if(order.size() !=0)
			{
				int i;
			    String todasREOC = "";
			    
			    for (i=0; i<order.size(); i++)
			    {
			    	todasREOC +=" "+ order.get(i);
			    }// end for
				
				ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_RevertGuide", new String[] {todasREOC}));
			}	
			
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
			return "";
		}*/

		return "Proceso Realizado";
	}

	@Override
	protected void prepare() {
		
		
	}

}
