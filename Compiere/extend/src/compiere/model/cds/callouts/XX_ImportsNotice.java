package compiere.model.cds.callouts;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MOrder;
import compiere.model.cds.MVLOBoardingGuide;
import compiere.model.cds.Utilities;

public class XX_ImportsNotice extends CalloutEngine {
	
	
	private boolean EnviarCorreosCambioStatusCoor(String Attachment, String date, String guide) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Cuentas por Pagar')) ";
		
		
		String Mensaje =  "\nGuide Number: "+guide+" \n"+ 
							date ;
				 
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		
		try{
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					//Utilities f = new Utilities(getCtx(), get_Trx,1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_ALERTIMPORT_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
					
				}
				
			}

		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}//Enviar	
	
	
private boolean EnviarCorreosCambioStatusCoorOC(String Attachment, String date, String ordernum) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Cuentas por Pagar')) ";
		
		
		String Mensaje =  "\nPurchase Order Number: "+ordernum+" \n"+ 
							date ;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
				 
		try{
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					//Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_ALERTORDERIMPORT_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}
				
			}

		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		} 
		
		return true;
	}//Enviar
	
	
	
	private boolean EnviarCorreosCambioStatusJefeImport(String Attachment, String date, String guide) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Importaciones'))";
		
		String Mensaje = "\nGuide Number: "+guide+" \n"+ 
								date ;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		
		try{
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					//Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_ALERTIMPORT_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}
			}

		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}  
		
		return true;
	}//Enviar
	
	
	
private boolean EnviarCorreosCambioStatusJefeImportOC(String Attachment, String date, String ordernum) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Importaciones'))";
		
		String Mensaje =  "\nPurchase Order Number: "+ordernum+" \n"+ 
							date ;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		
		try{
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					//Utilities f = new Utilities(getCtx(), get_TrxName(),1000007, Mensaje, -1, 1019149 , -1, UserAuxID, Attachment);
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_ALERTORDERIMPORT_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}

			}

		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}  
		
		return true;
	}//Enviar
	
	
	
public String notice (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)	{
	
		// •Fecha Recepción Factura Agente de Carga 
		GridField campoStatusNotice  =  mTab.getField("XX_CARGOAGENTINVORECEPDATE");  
		String cargoStatus = null;
		try {
			cargoStatus = campoStatusNotice.getValue().toString();
		} catch (Exception e) {
			cargoStatus = null;
		}
		
	       
	    GridField campoStatusid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
	    Integer cargoStatusid = (Integer)campoStatusid.getValue();
	    
	    if(cargoStatusid != null)
	    {
	    	String year = null;
	    	String month = null;
	    	String day = null;
	    	String date = null;
	    	String Attachment = null;
	    	String guideNumber = null;
	    	MVLOBoardingGuide guide = new MVLOBoardingGuide(Env.getCtx(), cargoStatusid, null); 
	    	
	    	year = cargoStatus.substring(0,4);
	    	month = cargoStatus.substring(5,7);
	    	day = cargoStatus.substring(8,10);
	    	guideNumber = guide.getValue();
	    	date = "Reception Date Freight Invoice: "+day+"/"+month+"/"+year;
	    	EnviarCorreosCambioStatusCoor(Attachment, date, guideNumber);
	    	EnviarCorreosCambioStatusJefeImport(Attachment, date, guideNumber);
	    	
	    }

		return "";
	}

public String noticeFRFP (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	
	{

		// •Fecha Recepción Factura Proveedor de producto
		
		GridField campoStatusNotice  =  mTab.getField("XX_VENDINVOICERECEPTDATE");  
		String cargoStatus = null;
		try {
			cargoStatus = campoStatusNotice.getValue().toString();
		} catch (Exception e) {
			cargoStatus = null;
		}
	    
	    GridField campoStatusid  =  mTab.getField("C_ORDER_ID");  
	    Integer cargoStatusid = (Integer)campoStatusid.getValue();
	    
	    if(cargoStatusid != null)
	    {	
	    	MOrder order = new MOrder(Env.getCtx(), cargoStatusid, null); 
	    	
	    	String year = null;
	    	String month = null;
	    	String day = null;
	    	String date = null;
	    	String Attachment = null;
	    	String orderNumber = null;
	    	
	    	year = cargoStatus.substring(0,4);
	    	month = cargoStatus.substring(5,7);
	    	day = cargoStatus.substring(8,10);
	    	orderNumber = order.getDocumentNo();
	    	date = "Vendor Invoice Reception Date: "+day+"/"+month+"/"+year;
	    	EnviarCorreosCambioStatusCoorOC(Attachment, date, orderNumber);
	    	EnviarCorreosCambioStatusJefeImportOC(Attachment, date, orderNumber);
	    	
	    }
	

		return "";
	}


 public String noticeFRFAA (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{

	//Fecha Recepción Factura Agente Aduanal 
	
	GridField campoStatusNotice  =  mTab.getField("XX_CUSTAGENINVOIRECEPTDATE");  
	String cargoStatus = null;
	try {
		cargoStatus = campoStatusNotice.getValue().toString();
	} catch (Exception e) {
		cargoStatus = null;
	}
    
    GridField campoStatusid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
    Integer cargoStatusid = (Integer)campoStatusid.getValue();
    
    if(cargoStatus != null)
    {    	
    	String year = null;
    	String month = null;
    	String day = null;
    	String date = null;
    	String Attachment = null;
    	String guideNumber = null;
    	MVLOBoardingGuide guide = new MVLOBoardingGuide(Env.getCtx(), cargoStatusid, null);
    	
    	year = cargoStatus.substring(0,4);
    	month = cargoStatus.substring(5,7);
    	day = cargoStatus.substring(8,10);
    	guideNumber = guide.getValue();
    	date = "Customs Agent Invoice Reception Date: "+day+"/"+month+"/"+year;
    	EnviarCorreosCambioStatusCoor(Attachment, date, guideNumber);
    	EnviarCorreosCambioStatusJefeImport(Attachment, date, guideNumber);
    	
    }

	return "";
}


/*public String noticeFRDE (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{
		
	// NO SE USA
	GridField campoStatusNotice  =  mTab.getField("XX_SHIPPDOCUMENTRECEPTIONDATE");  
    String cargoStatus = campoStatusNotice.getValue().toString();
    
    if(cargoStatus != null)
    {
    	mTab.setValue("XX_ActFRDE", "Y");

    	System.out.println("Entro "+mTab.get_ValueAsString("XX_ActFRDE"));
    }

	return "";
}*/


public String noticeFREAC (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{	
	// ESTE CALLOUT SE EJECUTA EN VLO_ORDERSTATUSCALLOUT en Status
	// ESTA EN O/C •Fecha Real Entrega Agente de Carga
	
	GridField campoStatusNotice  =  mTab.getField("XX_CARAGENTDELIVREALDATE");  
	String cargoStatus = null;
	try {
		cargoStatus = campoStatusNotice.getValue().toString();
	} catch (Exception e) {
		cargoStatus = null;
	}
    
    if(cargoStatus != null)
    {
    	mTab.setValue("XX_ActFREAC", "Y");

    	//System.out.println("Entro "+mTab.get_ValueAsString("XX_ActFREAC"));
    }


	return "";
}



public String noticeSIDU (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{	
	//Fecha Real de Registro y Cancelación de Derechos de Aduana

	GridField campoStatusNotice  =  mTab.getField("XX_CUSTOMRIGHTSCANCELDATE");  
	String cargoStatus = null;
	try {
		cargoStatus = campoStatusNotice.getValue().toString();
	} catch (Exception e) {
		cargoStatus = null;
	}
    
    GridField campoStatusid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
    Integer cargoStatusid = (Integer)campoStatusid.getValue();
    
    if(cargoStatus != null)
    {
 
    	
    }


	return "";
}


/*public String noticeFCDA (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{	
		
 // IGUAL QUE EL DE ARRIBA 
		GridField campoStatusNotice  =  mTab.getField("XX_CUSTOMRIGHTSCANCELDATE");  
	    String cargoStatus = campoStatusNotice.getValue().toString();
	    
	    if(cargoStatus != null)
	    {
	    	mTab.setValue("XX_ActFCDA", "Y");
	    	//System.out.println("Entro "+mTab.get_ValueAsString("XX_ActFCDA"));
	    }


	return "";
}*/


public String noticePAAA (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{	
		
		// •Fecha de Pago Anticipo Agente Aduanal 
		
		GridField campoStatusNotice  =  mTab.getField("XX_CUSTOMSAGENTPAYMENTDATE");  
		String cargoStatus = null;
		try {
			cargoStatus = campoStatusNotice.getValue().toString();
		} catch (Exception e) {
			cargoStatus = null;
		}
	    
	    GridField campoStatusid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
	    Integer cargoStatusid = (Integer)campoStatusid.getValue();
	    
	    if(cargoStatus != null)
	    {
	    	//mTab.setValue("XX_ActPAAA", "Y");
	    	//System.out.println("Entro "+mTab.get_ValueAsString("XX_ActPAAA"));
	    	
	    	String year = null;
	    	String month = null;
	    	String day = null;
	    	String date = null;
	    	String Attachment = null;
	    	String guideNumber = null;
	    	MVLOBoardingGuide guide = new MVLOBoardingGuide(Env.getCtx(), cargoStatusid, null);
	    	
	    	year = cargoStatus.substring(0,4);
	    	month = cargoStatus.substring(5,7);
	    	day = cargoStatus.substring(8,10);
	    	guideNumber = guide.getValue();
	    	date = "Customs Agent Advance Payment Date: "+day+"/"+month+"/"+year;
	    	EnviarCorreosCambioStatusCoor(Attachment, date, guideNumber);
	    	EnviarCorreosCambioStatusJefeImport(Attachment, date, guideNumber);
	    	
	    }

	return "";
}


public String noticeFCD (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{	
		
	PreparedStatement pstmt = null;
	String SQL = null;
	try
	{
		//•	Fecha Real de Llegada a CD 
		GridField campoStatusNotice  =  mTab.getField("XX_CDARRIVALREALDATE");  
		String cargoStatus = null;
		try {
			cargoStatus = campoStatusNotice.getValue().toString();
		} catch (Exception e) {
			cargoStatus = null;
		}
	    
	    GridField campoStatusid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		String cargoStatusid = null;
		try {
			cargoStatusid = campoStatusid.getValue().toString();
		} catch (Exception e) {
			cargoStatusid = null;
		}
	    
	    if(cargoStatus != null)
	    {
	    	//System.out.println("Entro");
	    	//mTab.setValue("XX_ActFCD", "Y");
	    	//System.out.println("Entro "+mTab.get_ValueAsString("XX_ActFCD"));
	    	
	    	SQL = ("UPDATE XX_VLO_BOARDINGGUIDE SET XX_ActFCD = 'Y' WHERE XX_VLO_BOARDINGGUIDE_ID = '"+cargoStatusid+"'");
	    	DB.executeUpdate(null, SQL );

	    }

	}
	catch (Exception e) {
		System.out.println("Error al ejecutar el update en la linea 596 de la clase XX_ImportsNotice (callouts) " + SQL );
	} finally{			
		DB.closeStatement(pstmt);
	}  
	return "";
}


public String noticeDOC (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)

{	
	PreparedStatement pstmt = null;
	String SQL = null;
	try
	{
		//•	Fecha Real de Despacho de Aduana 
		// XX_ActDOC = 'Y' 
		//UPDATE XX_VLO_BOARDINGGUIDE SET XX_ActDOC = 'N'  WHERE XX_ActDOC = 'Y'
		
		
		GridField campoStatusNotice  =  mTab.getField("XX_REALDISPATCHDATE");  
		String cargoStatus = null;
		try {
			cargoStatus = campoStatusNotice.getValue().toString();
		} catch (Exception e) {
			cargoStatus = null;
		}
	    
	    GridField campoStatusid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		String cargoStatusid = null;
		try {
			cargoStatusid = campoStatusid.getValue().toString();
		} catch (Exception e) {
			cargoStatusid = null;
		}
	    
	    if(cargoStatus != null)
	    {
	    	//System.out.println("Entro");
	    	
	    	//mTab.setValue("XX_ActDOC", "Y");
	    	//System.out.println("Entro "+mTab.get_ValueAsString("XX_ActDOC"));
	    	SQL = ("UPDATE XX_VLO_BOARDINGGUIDE SET XX_ActDOC = 'Y' WHERE XX_VLO_BOARDINGGUIDE_ID = '"+cargoStatusid+"'");
	    	DB.executeUpdate(null, SQL );

	    }

	}
	catch (Exception e) {
		System.out.println("Error al ejecutar el update en la linea 596 de la clase XX_ImportsNotice (callouts) " + SQL );
	} finally{			
		DB.closeStatement(pstmt);
	}  
	return "";
}


}
