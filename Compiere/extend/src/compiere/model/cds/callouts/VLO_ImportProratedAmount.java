package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;

public class VLO_ImportProratedAmount extends CalloutEngine {
	
	public String proratedAgentLoad (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) 
	{
		
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
	    
		try
		{	
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedAgentLoad
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mfacP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountAL =  mTab.getField("XX_AGENTLOADINVOICEAMOUNT");  
		    BigDecimal montoAC = (BigDecimal)campoAmountAL.getValue();
		    
		    GridField campoGuia =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNro = (Integer)campoGuia.getValue();
		    
		   
		    
		    // Sumatoria de los montos de todas las O/C de ese embarque en Bs
		    /*String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_TRANSPORTDOCUMENTNUMBER = '"+guiaNro+"' ");*/
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNro+"' ");
		    
		    
		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	//Monto de todas las O/C
		    	cantT = rs.getBigDecimal("TOTALOR");
		    	
		    }	    
		    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNro+"'");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	// ID de la O/C
		    	orderId = rs2.getInt("ORDERID");
		    	
		    	
		    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNro+"' " +
			    		"AND A.C_ORDER_ID = '"+orderId+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	//Monto de una O/C especifica
			    	cant = rs3.getBigDecimal("TOTAL");
			    	
			    }
			    
			    // %O/C
			    //Monto que divide
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);
			    //Total
			    mfacP = montoAC.multiply(cantTotal);
			    
			    
			 String sql = "UPDATE C_Order po"											
					+ " SET XX_FreightAgentInvoiceAmount="  + mfacP
					+ " WHERE po.C_Order_ID=" + orderId;						
				DB.executeUpdate(null, sql);
				
			    
		    	
		    } // end while
	   
			return "";
		} // end try
		catch (Exception e) {
			// TODO: handle exception
			return "";
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		}  	
	} // end proratedAgentLoad
	
	public String proratedCustomAgent (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
	    
		try
		{
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedCustomAgent
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal maaaP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountAL =  mTab.getField("XX_CUSTOMSAGENTANTICAMOUNT");  
		    BigDecimal montoCA = (BigDecimal)campoAmountAL.getValue();
		    
		    //GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		    //String guiaNro = (String)campoGuia.getValue();
		    
		    GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNroID = (Integer)campoid.getValue();
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	// Monto de todas las O/C
		    	cantT = rs.getBigDecimal("TOTALOR");
		    	
		    }
		    		    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	orderId = rs2.getInt("ORDERID");
		    	
		    			    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.C_ORDER_ID = '"+orderId+"' " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	//Monto de una O/C especifica
			    	cant = rs3.getBigDecimal("TOTAL");
			    	
			    }
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);
			    
			    maaaP = montoCA.multiply(cantTotal);
			    
			 String sql = "UPDATE C_Order po"											
					+ " SET XX_AntiAmAgentPro="  + maaaP
					+ " WHERE po.C_Order_ID=" + orderId;						
				DB.executeUpdate(null, sql);

			    
		    } // while 
		    
			return "";
			
		} // end try		
		catch (Exception e) {
			// TODO: handle exception
			return "";
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		}  	
	} // end proratedCustomAgent

	public String proratedAmountTreasury (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
	    
		try
		{
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedAmountTreasury
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mrtnP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountT =  mTab.getField("XX_NACTREASREALAMOUNT");  
		    BigDecimal montoCA = (BigDecimal)campoAmountT.getValue();
		    
		    //GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		    //String guiaNro = (String)campoGuia.getValue();
		    
		    GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNroID = (Integer)campoid.getValue();
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	cantT = rs.getBigDecimal("TOTALOR");
		    	
		    }
		  	    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	
		    	orderId = rs2.getInt("ORDERID");
		    			    	
		    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.C_ORDER_ID = "+orderId+" " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = "+guiaNroID+" ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	cant = rs3.getBigDecimal("TOTAL");
			    	
			    }
			    
			    DB.closeResultSet(rs3);
			    DB.closeStatement(pstmt3);
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);
			    
			    mrtnP = montoCA.multiply(cantTotal);
			    
				 String sql = "UPDATE C_Order po"											
						+ " SET XX_NatTreasRealAmPro="  + mrtnP
						+ " WHERE po.C_Order_ID=" + orderId;						
					DB.executeUpdate(null, sql);

		    	
		    }// end while

			return "";
			
		} // end try		
		catch (Exception e) {
			// TODO: handle exception
			return "";		
		} finally{	

			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		}  	
	
		
	}
	
	public String proratedAmountSeniat (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		try
		{
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedAmountSeniat
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mrsP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountT =  mTab.getField("XX_SENIATREALESTEEMEDAMOUNT");  
		    BigDecimal montoCA = (BigDecimal)campoAmountT.getValue();
		    
		    //GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		    //String guiaNro = (String)campoGuia.getValue();
		    
		    GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNroID = (Integer)campoid.getValue();
			
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    

		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	cantT = rs.getBigDecimal("TOTALOR");

		    }		    
		    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	orderId = rs2.getInt("ORDERID");
		    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			      		"AND A.C_ORDER_ID = '"+orderId+"' " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	cant = rs3.getBigDecimal("TOTAL");

			    }
			    DB.closeResultSet(rs3);
			    DB.closeStatement(pstmt3);
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);

			    mrsP = montoCA.multiply(cantTotal);

				 String sql = "UPDATE C_Order po"											
						+ " SET XX_SenRealEstAmPro="  + mrsP
						+ " WHERE po.isSOTRX ='N' AND po.C_Order_ID=" + orderId;						
					DB.executeUpdate(null, sql);
					
		    }// end while

			return "";
			
		}// end try
		catch (Exception e) {
			// TODO: handle exception
			return "";
		
		} finally{	
			DB.closeResultSet(rs3);
			DB.closeResultSet(rs2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt3);
			DB.closeStatement(pstmt2);
			DB.closeStatement(pstmt);
		}  	
		
		
	} // end
	
	public String proratedInvoiceAA (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		try
		{
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedInvoiceAA
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mfaaP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountT =  mTab.getField("XX_AARealAmount");  
		    BigDecimal montoCA = (BigDecimal)campoAmountT.getValue();
		    
		   // GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		   // String guiaNro = (String)campoGuia.getValue();
		    
		    GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNroID = (Integer)campoid.getValue();
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	cantT = rs.getBigDecimal("TOTALOR");

		    }	    
		    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	
		    	orderId = rs2.getInt("ORDERID");
		    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.C_ORDER_ID = '"+orderId+"' " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	cant = rs3.getBigDecimal("TOTAL");

			    }
			    
			    DB.closeResultSet(rs3);
			    DB.closeStatement(pstmt3);
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);

			    mfaaP = montoCA.multiply(cantTotal);

				 String sql = "UPDATE C_Order po"											
						+ " SET XX_CustomAgentAmountPro="  + mfaaP
						+ " WHERE po.issotrx='N' AND po.C_Order_ID=" + orderId;						
					DB.executeUpdate(null, sql);
					
		    	
		    }// end while
		    
			return "";
		}// end try
		catch (Exception e) {
			// TODO: handle exception
			return "";
		
		} finally{	
			DB.closeResultSet(rs3);
			DB.closeResultSet(rs2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt3);
			DB.closeStatement(pstmt2);
			DB.closeStatement(pstmt);
		}  	
		
	}// end
	
	public String proratedInvoiceNac (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
	    
		try
		{
			
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedInvoiceNac
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mffnP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountT =  mTab.getField("XX_NACINVOICEAMOUNT");  
		    BigDecimal montoCA = (BigDecimal)campoAmountT.getValue();
		    
		    //GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		    //String guiaNro = (String)campoGuia.getValue();
		    
		    GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNroID = (Integer)campoid.getValue();
		    
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    

		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	cantT = rs.getBigDecimal("TOTALOR");

		    }
		    
		    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	
		    	orderId = rs2.getInt("ORDERID");
		    	
		    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.C_ORDER_ID = '"+orderId+"' " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	cant = rs3.getBigDecimal("TOTAL");
	
			    }
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);
	
			    mffnP = montoCA.multiply(cantTotal);
	
			 String sql = "UPDATE C_Order po"											
					+ " SET XX_NacInvoiceAmountPro="  + mffnP
					+ " WHERE po.isSOTRX='N' AND po.C_Order_ID=" + orderId;						
				DB.executeUpdate(null, sql);
		    	
		    }//end while
	
			return "";
		}// end try
		
		catch (Exception e) {
			// TODO: handle exception
			return "";
		
		} finally{	
			DB.closeResultSet(rs3);
			DB.closeResultSet(rs2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt3);
			DB.closeStatement(pstmt2);
			DB.closeStatement(pstmt);
		}  	
		
	}// end
	
	public String proratedInvoiceInt (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		try
		{
			
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedInvoiceInt
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mffiP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountT =  mTab.getField("XX_InterFretRealAmount");  
		    BigDecimal montoCA = (BigDecimal)campoAmountT.getValue();
		    System.out.println("Valor "+montoCA);
		    //GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		    //String guiaNro = (String)campoGuia.getValue();
		    
		    GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNroID = (Integer)campoid.getValue();
		    
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
	
		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	cantT = rs.getBigDecimal("TOTALOR");
		    	
		    }		    
		    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    
		    while(rs2.next())
		    {
		    	
		    	orderId = rs2.getInt("ORDERID");
		    	
			    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.C_ORDER_ID = '"+orderId+"' " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	cant = rs3.getBigDecimal("TOTAL");
			    	
			    }
			    
			    DB.closeResultSet(rs3);
			    DB.closeStatement(pstmt3);
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);
			    System.out.println("cantidad total "+cantTotal);
			    mffiP = montoCA.multiply(cantTotal);
			    System.out.println("PRO "+mffiP);
			    
			 String sql = "UPDATE C_Order po"											
					+ " SET XX_InterFretRealAmountPro="  + mffiP
					+ " WHERE po.isSOTRX='N' AND po.C_Order_ID=" + orderId;						
				DB.executeUpdate(null, sql);
					
			    
		    }//end while
		    
			
			return "";
		}// end try
		
		catch (Exception e) {
			// TODO: handle exception
			return "";
		
		} finally{	
			DB.closeResultSet(rs3);
			DB.closeResultSet(rs2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt3);
			DB.closeStatement(pstmt2);
			DB.closeStatement(pstmt);
		}  	
	}// end
	
	
	public String proratedInsurance (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		try
		{
			//compiere.model.cds.callouts.VLO_ImportProratedAmount.proratedInsurance
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal masrP = new BigDecimal(0);
			Integer orderId = 0;
			
			GridField campoAmountT =  mTab.getField("XX_REALINSURANCEAMOUNT");  
		    BigDecimal montoCA = (BigDecimal)campoAmountT.getValue();
		    
		    //GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		    //String guiaNro = (String)campoGuia.getValue();
		    
		    GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    Integer guiaNroID = (Integer)campoid.getValue();
			
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {
		    	cantT = rs.getBigDecimal("TOTALOR");

		    }	
		    
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		    
		    String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
		    
		    
		    pstmt2 = DB.prepareStatement(SQL2, null); 
		    rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	
		    	orderId = rs2.getInt("ORDERID");
		    	
		    			    	
		    	// Monto de la O/C en Bs 
		    	String SQL3 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTAL " +
			    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
			    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' AND A.isSOTRX ='N' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.C_ORDER_ID = '"+orderId+"' " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	cant = rs3.getBigDecimal("TOTAL");
			    
			    }
			    
				DB.closeResultSet(rs3);
				DB.closeStatement(pstmt3);
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);
			    
			    masrP = montoCA.multiply(cantTotal);
			    
			    
			 String sql = "UPDATE C_Order po"											
					+ " SET XX_RealInsuranceAmPro="  + masrP
					+ " WHERE po.isSOTRX='N' AND po.C_Order_ID=" + orderId;						
				DB.executeUpdate(null, sql);
		    	
		    	
		    }//end while
		
			return "";
		}//end try
		catch (Exception e) {
			// TODO: handle exception
			return "";
		
		} finally{	
			DB.closeResultSet(rs3);
			DB.closeResultSet(rs2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt3);
			DB.closeStatement(pstmt2);
			DB.closeStatement(pstmt);
		}  	
		
	}//end
	
}
