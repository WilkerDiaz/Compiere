package compiere.model.cds.callouts;
 

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MCountry;
import org.compiere.model.MCurrency;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MPaymentTerm;
import compiere.model.cds.Utilities;
import compiere.model.importcost.Ajuste;
import compiere.model.importcost.MImportCostDetail;
import compiere.model.importcost.MSUMMARY;


public class VLO_OrderStatusCallout extends CalloutEngine {

	Calendar cal = Calendar.getInstance();
    
    int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	int day = cal.get(Calendar.DATE);
	int hour = cal.get(Calendar.HOUR);
	int min = cal.get(Calendar.MINUTE);
	int sec = cal.get(Calendar.SECOND);
	int nano = cal.get(Calendar.SECOND);
    
    Timestamp XXExitDate = new Timestamp(year-1900,month,day,hour,min,sec,nano);
    
	/**
	 * Daniel Pellegrino --> Funcion
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusCoor(MOrder order,String Attachment, String status , String date) {
					
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Cuentas por Pagar'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
						 "Vendor: "+vendor.getName()+" \n"+
						 "Amount: "+order.getTotalLines()+" \n"+
						 "Currency: "+currency.getDescription()+"\n"+
						 "Payment Term: "+paymentTerm.getName()+"\n"+
						 "Country: "+country.getDescription()+"\n"+
						 date;
		
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
			
			
				while(rs2.next())
				{	
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
					
				}
			}
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}  
		return true;
	}//Enviar
	
	private boolean EnviarCorreosCambioStatusJefeImport(MOrder order,String Attachment, String status, String date ) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Importaciones'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
		 				"Vendor: "+vendor.getName()+" \n"+
		 				"Amount: "+order.getTotalLines()+" \n"+
		 				"Currency: "+currency.getDescription()+"\n"+
		 				"Payment Term: "+paymentTerm.getName()+"\n"+
		 				"Country: "+country.getDescription()+"\n"+
		 				date;
		
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
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;

				}
			}
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}  		
		
		return true;
	}//Enviar
	
	
	private boolean EnviarCorreosCambioStatusCoorImport(MOrder order,String Attachment, String status, String date ) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Importación'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
		 				"Vendor: "+vendor.getName()+" \n"+
		 				"Amount: "+order.getTotalLines()+" \n"+
		 				"Currency: "+currency.getDescription()+"\n"+
		 				"Payment Term: "+paymentTerm.getName()+"\n"+
		 				"Country: "+country.getDescription()+"\n"+
		 				date;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		PreparedStatement pstmt2 =  null;
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
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;

				}
			}
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}  	
		return true;
	}//Enviar

    
	private boolean EnviarCorreosCambioStatusJefeCat(MOrder order,String Attachment, String status , String date) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Categoria'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
						 "Vendor: "+vendor.getName()+" \n"+
						 "Amount: "+order.getTotalLines()+" \n"+
						 "Currency: "+currency.getDescription()+"\n"+
						 "Payment Term: "+paymentTerm.getName()+"\n"+
						 "Country: "+country.getDescription()+"\n"+
						 date;
		
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
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}
			}
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}  	
		return true;
	}//Enviar

	public String status (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
	    PreparedStatement pstmt1 = null;
		try
		{ // XX_CARAGENTDELIVREALDATE   compiere.model.cds.callouts.VLO_OrderStatusCallout.status

			String Attachment = null;
			String status = "in now in 'Entregada al Agente de Carga' Status";
			
			GridField campoOrderId  =  mTab.getField("C_Order_ID");  
		    Integer orderid = (Integer)campoOrderId.getValue();
						
		    String cargoAgent = null;
			try {
				cargoAgent = mField.getValue().toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			    if(cargoAgent != null)
			    {
			    	String year = null;
			    	String month = null;
			    	String day = null;
			    	String date = null;
			    	
			    	year = cargoAgent.substring(0,4);
			    	month = cargoAgent.substring(5,7);
			    	day = cargoAgent.substring(8,10);
			    	date = "Cargo Agent Delivery Real Date: "+day+"/"+month+"/"+year;
			    	
			    	mTab.setValue("XX_DefinitiveCargoAg", true);
			    	
			    	MOrder orden = new MOrder(Env.getCtx(), orderid, null);
			 			    	
				    String SQL1 = ("UPDATE C_ORDER SET XX_ORDERSTATUS= 'EAC' WHERE isSOTrx='N' AND C_ORDER_ID = '"+orderid+"' AND XX_ORDERSTATUS = 'EP' ");
					    	
				    DB.executeUpdate(null, SQL1 );

					orden.setXX_InsertedStatusDate(XXExitDate);
					orden.save();
						    
					//EnviarCorreosCambioStatusCoor(orden, Attachment, status, date); //COMENTADO POR GHUCHET A PETICIÓN DE FINANZAS
					EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
					EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
					EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
    
			    }// end cargoAgent
			    else
			    {
			    	mTab.setValue("XX_DefinitiveCargoAg", false);
			    }
		    
		}// end try 		
		catch (Exception e) {
			return"";
		} finally{			
			DB.closeStatement(pstmt1);
		}  	
		return "";
	}
	
	public String status1 (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		//XX_DefinitiveShippingDate     compiere.model.cds.callouts.VLO_OrderStatusCallout.status1
		//String Attachment = null;
		//String status = "is now in 'En Tránsito Internacional' Status";
		
		PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
		
		try
		{		
			 GridField camposp  =  mTab.getField("XX_SHIPPINGREALDATE");  
			 //String sp = camposp.getValue().toString();
			 Date sp = (Date)camposp.getValue();
			 
			 GridField campoGuiaid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
			 Integer guiaid = (Integer)campoGuiaid.getValue();
			 
			if(value.toString().equals("true"))
			{
				Boolean opcion = ADialog.ask(1, new Container(), "XX_Definitive");
				
				
				String SQL2 = ("SELECT A.XX_DefinitiveCargoAg AS DEFINITIVE, DOCUMENTNO AS ORDERNO " +
						"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
						"WHERE A.isSOTrx='N' AND A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
						"AND A.XX_VLO_BOARDINGGUIDE_ID = '"+guiaid+"' ");
				
				pstmt2 = DB.prepareStatement(SQL2, null); 
			    rs2 = pstmt2.executeQuery();
			    
			  	Boolean tal = true;
			  	
			  	Vector <String> orden = new Vector <String>();
			  	
			    while(rs2.next())
			    {
			    	  			    	
			    	if(rs2.getString("DEFINITIVE").trim().equals("N"))
			    	{
			    		tal = false;
			    		orden.add(rs2.getString("ORDERNO"));
				    }
						    	
			    }// end while
			    int i;
			    String todasOC = "";
			    
			    for (i=0; i<orden.size(); i++)
			    {
			    	todasOC +=" "+ orden.get(i);
			    }

				/*if(opcion && sp != null && tal == true)
				{
					Integer orderid = 0;
							    
				    if(sp != null)
				    {				    	
				    	String year = null;
				    	String month = null;
				    	String day = null;
				    	String date = null;
				    	
				    	year = sp.substring(0,4);
				    	month = sp.substring(5,7);
				    	day = sp.substring(8,10);
				    	date = "Shipping Real Date: "+day+"/"+month+"/"+year;
				    	
				    	String SQL = ("SELECT A.C_ORDER_ID AS ORDERID " +
								"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
								"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
								"AND A.XX_ORDERSTATUS = 'EAC' " +
								"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
								"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+guiaid+"' ");
						
						PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
					    ResultSet rs = pstmt.executeQuery();
					    
					    while(rs.next())
					    {
					    	orderid = rs.getInt("ORDERID");
					    	
					    	MOrder order = new MOrder(Env.getCtx(),orderid,null);
					    	
					    	String SQL1 = ("UPDATE C_ORDER SET XX_ORDERSTATUS= 'ETI' WHERE C_ORDER_ID = '"+orderid+"'");
						    
						    PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
						    pstmt1.executeUpdate(SQL1);
						    
						    order.setXX_InsertedStatusDate(XXExitDate);
						    order.save();
						    
						    EnviarCorreosCambioStatusCoor(order, Attachment, status, date);
						    EnviarCorreosCambioStatusJefeImport(order, Attachment, status, date);
						    EnviarCorreosCambioStatusJefeCat(order, Attachment, status, date);
						    EnviarCorreosCambioStatusCoorImport(order, Attachment, status, date);
						    	
					    } // end while
					    rs.close();
					    pstmt.close();	
				    	
				    }// end if fecha
	
				}// end if pregunta
		
				else
				{*/
				   if(tal == false && opcion)
				   {
					   ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_ShippingDate", new String[] {todasOC}));
					   mTab.setValue("XX_DefinitiveShippingDate", false);
				   }
				   else if (!opcion || sp == null)
					   mTab.setValue("XX_DefinitiveShippingDate", false);
					
				//}
	
			}// end if value
			else
			{
				return "";
				
			}// end else value

		} // end try
		
		catch (Exception e) {
			//e.printStackTrace();
			return"";
		} finally{			
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
		}  	
	
		return"";
	}
	
	public String status2 (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		//String Attachment = null;
		//String status = "is now in 'Llegada a Venezuela' Status";
		
			GridField camposp  =  mTab.getField("XX_VZLAARRIVALREALDATE");  
			//String country = camposp.getValue().toString();
			Date sp = (Date)camposp.getValue();
			
			if(value.toString().equals("true"))
			{
				Boolean opcion = ADialog.ask(1, new Container(), "XX_Definitive");
							
				/*if(opcion && country != null)
				{
					Integer orderid = 0;
					
					String year = null;
			    	String month = null;
			    	String day = null;
			    	String date = null;
			    	
			    	year = country.substring(0,4);
			    	month = country.substring(5,7);
			    	day = country.substring(8,10);
			    	date = "VZLA Arrival Real Date: "+day+"/"+month+"/"+year;

					GridField campoGuiaid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
				    Integer guiaid = (Integer)campoGuiaid.getValue();
				 	
				    	String SQL = ("SELECT A.C_ORDER_ID AS ORDERID " +
								"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
								"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
								"AND A.XX_ORDERSTATUS = 'ETI' " +
								"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
								"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+guiaid+"' ");
						
						PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
					    ResultSet rs = pstmt.executeQuery();
					   
					    while(rs.next())
					    {
					    	orderid = rs.getInt("ORDERID");
					    	
					    	MOrder orden = new MOrder(Env.getCtx(),orderid,null);
					    	
							String SQL2 = ("UPDATE C_ORDER SET XX_ORDERSTATUS= 'LVE' WHERE C_ORDER_ID = '"+orderid+"'");
			    				    
							PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
							pstmt2.executeUpdate(SQL2);
							
							orden.setXX_InsertedStatusDate(XXExitDate);
						    orden.save();
						    
						    EnviarCorreosCambioStatusCoor(orden, Attachment, status, date);
						    EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
						    EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
						    EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
					
					    }// end while
					    rs.close();
					    pstmt.close();	
				}
				else
				{
					// tengo que setiar que N al defitivo
					mTab.setValue("XX_DefinitiveVzlaDate", false);
				//}*/
				
				if(!opcion || sp == null)
					mTab.setValue("XX_DefinitiveVzlaDate", false);
									
			}// end value

		return "";
	}
	
	public String status3 (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		//String Attachment = null;
		//String status = "is now in 'En Proceso de Nacionalización' Status";

			GridField camposp  =  mTab.getField("XX_CUSTOMRIGHTSCANCELDATE");  
		    //String sp = camposp.getValue().toString();
			Date sp = (Date)camposp.getValue();
			
			if(value.toString().equals("true"))
			{
				Boolean opcion = ADialog.ask(1, new Container(), "XX_Definitive");
								
					
				/*if(opcion && sp != null)
				{
					Integer orderid = 0;
					
					GridField campoGuiaid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
				    Integer guiaid = (Integer)campoGuiaid.getValue();
		
				    if(sp != null)
				    {
				    	String year = null;
				    	String month = null;
				    	String day = null;
				    	String date = null;
				    	
				    	year = sp.substring(0,4);
				    	month = sp.substring(5,7);
				    	day = sp.substring(8,10);
				    	date = "Custom Rights Cancel Date: "+day+"/"+month+"/"+year;
			
				    	String SQL = ("SELECT A.C_ORDER_ID AS ORDERID " +
								"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
								"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
								"AND A.XX_ORDERSTATUS = 'LVE' " +
								"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
								"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+guiaid+"' ");
						
						PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
					    ResultSet rs = pstmt.executeQuery();
					    
					    while(rs.next())
					    {
					    	orderid = rs.getInt("ORDERID");
					    	
					    	MOrder orden = new MOrder(Env.getCtx(),orderid,null);
					    	
					    	String SQL1 = ("UPDATE C_ORDER SET XX_ORDERSTATUS= 'EPN' WHERE C_ORDER_ID = '"+orderid+"'");
						    
						    PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
						    pstmt1.executeUpdate(SQL1);
						    
						    orden.setXX_InsertedStatusDate(XXExitDate);
						    orden.save();
						    
						    EnviarCorreosCambioStatusCoor(orden, Attachment, status, date);
						    EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
						    EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
						    EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
				
					    }// end while
					    rs.close();
					    pstmt.close();		
				    } // end de fecha

				}//end opcion
				else
				{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveDA", false);
				}*/
				
				if(!opcion || sp == null)
					mTab.setValue("XX_DefinitiveDA", false);
				
			}// end value

		return "";
	}
	
	public String status5 (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		//String Attachment = null;
		//String status = "is now in 'En Tránsito Nacional' Status";

			//implementar aqui el Status4
			GridField camposp  =  mTab.getField("XX_REALDISPATCHDATE");  
		    //String sp = camposp.getValue().toString();
			Date sp = (Date)camposp.getValue();

			if(value.toString().equals("true"))
			{
				
				Boolean opcion = ADialog.ask(1, new Container(), "XX_Definitive");
				
				/*if(opcion && sp != null)
				{
					Integer orderid = 0;
					
					String year = null;
			    	String month = null;
			    	String day = null;
			    	String date = null;
			    	
			    	year = sp.substring(0,4);
			    	month = sp.substring(5,7);
			    	day = sp.substring(8,10);
			    	date = "Customs Dispatch Real Date: "+day+"/"+month+"/"+year;
					
					GridField campoGuiaid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
				    Integer guiaid = (Integer)campoGuiaid.getValue();
				 	
				    String SQL = ("SELECT A.C_ORDER_ID AS ORDERID " +
							"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
							"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
							"AND A.XX_ORDERSTATUS = 'EPN' " +
							"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
							"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+guiaid+"' ");
						
					PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next())
					{
						orderid = rs.getInt("ORDERID");
				    	
						MOrder orden = new MOrder(Env.getCtx(),orderid,null);
						
				    	String SQL1 = ("UPDATE C_ORDER SET XX_ORDERSTATUS= 'ETN' WHERE C_ORDER_ID = '"+orderid+"'");
					    
					    PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
					    pstmt1.executeUpdate(SQL1);
					    
					    orden.setXX_InsertedStatusDate(XXExitDate);
					    orden.save();
					    
					    EnviarCorreosCambioStatusCoor(orden, Attachment, status, date);
					    EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
					    EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
					    EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
									
					}// end while
					rs.close();
				    pstmt.close();	
				}// end opcion
				else
				{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveDispatch", false);
				}*/
				
				if(!opcion || sp == null)
					mTab.setValue("XX_DefinitiveDispatch", false);
				
				
			}// end if value
	
			return "";

	}
	
	public String status6 (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		//String Attachment = null;
		//String status = "is now in 'Llegada a CD' Status";

			GridField camposp  =  mTab.getField("XX_CDARRIVALREALDATE");  
		    //String sp = camposp.getValue().toString();
			Date sp = (Date)camposp.getValue();
		    
		    //GridField campoStatusid  =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    //String cargoStatusid = campoStatusid.getValue().toString();

			if(value.toString().equals("true"))
			{
				Boolean opcion = ADialog.ask(1, new Container(), "XX_Definitive");
				
				if(!opcion || sp == null)
					mTab.setValue("XX_DefinitiveCD", false);
				
			}// end if value

			return "";

	}
	
	public String status10 (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{

			GridField camposp  =  mTab.getField("XX_VALIDATIONREALDATE");  
			Date sp = (Date)camposp.getValue();

			if(value.toString().equals("true"))
			{				
				Boolean opcion = ADialog.ask(1, new Container(), "XX_Definitive");
				
				if(opcion && sp != null)
				{
					
				}// end opcion
				else
				{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveValidation", false);
				}
				
			}// end if value
	
			return "";
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Relación de costos de importaciones
	 * @author aavila
	 * @author ccapote
	 * */
	
	public String definitiveNacTreas (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){

			GridField camposp  =  mTab.getField("XX_NACTREASREALAMOUNT");  
			BigDecimal sp = (BigDecimal)camposp.getValue();

			if(value.toString().equals("true"))
			{
				
				Boolean opcion = ADialog.ask(1, new Container(), "XX_DefinitiveAmount");
				
				if(opcion){
					if (sp.intValue()==0){
						Boolean opcion2 = ADialog.ask(1, new Container(), "XX_campo_cero");
						if (!opcion2)
							mTab.setValue("XX_DefinitiveNacTreas", false);
						else 
							registrarAjustes(mTab,ctx);
					}else{
						registrarAjustes(mTab,ctx);
					}// end opcion
				}else{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveNacTreas", false);
				}
				
			}// end if value
			
			return "";

	}
	
	public String definitiveAgent (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		

			GridField camposp  =  mTab.getField("XX_AARealAmount");  
			BigDecimal sp = (BigDecimal)camposp.getValue();

			if(value.toString().equals("true"))
			{
				
				Boolean opcion = ADialog.ask(1, new Container(), "XX_DefinitiveAmount");
				
				if(opcion){
					if (sp.intValue()==0){
						Boolean opcion2 = ADialog.ask(1, new Container(), "XX_campo_cero");
						if (!opcion2)
							mTab.setValue("XX_DefinitiveAgent", false);
						else registrarAjustes(mTab,ctx);
					}else{registrarAjustes(mTab,ctx);}// end opcion
				}else {
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveAgent", false);
				}
				
			}// end if value
			
			return "";

	}
	public String definitiveInsurance (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		

			GridField camposp  =  mTab.getField("XX_REALINSURANCEAMOUNT");  
			BigDecimal sp = (BigDecimal)camposp.getValue();

			if(value.toString().equals("true"))
			{
				
				Boolean opcion = ADialog.ask(1, new Container(), "XX_DefinitiveAmount");
				
				if(opcion)
					if (sp.intValue()==0){
						Boolean opcion2 = ADialog.ask(1, new Container(), "XX_campo_cero");
						if (!opcion2)
							mTab.setValue("XX_DefinitiveInsurance", false);
						else registrarAjustes(mTab,ctx);
					}else{
						registrarAjustes(mTab,ctx);
					}// end opcion
				else
				{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveInsurance", false);
				}
				
			}// end if value
			
			
			
			return "";

	}
	public String definitiveInterFret (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
	    	
			GridField camposp  =  mTab.getField("XX_InterFretRealAmount");  
			BigDecimal sp = (BigDecimal)camposp.getValue();

			if(value.toString().equals("true"))
			{
				Boolean opcion = ADialog.ask(1, new Container(), "XX_DefinitiveAmount");
				
				if(opcion)
					if (sp.intValue()==0){
						Boolean opcion2 = ADialog.ask(1, new Container(), "XX_campo_cero");
						if (!opcion2)
							mTab.setValue("XX_DefinitiveInterFret", false);
						else registrarAjustes(mTab,ctx);
					}else{registrarAjustes(mTab,ctx);}
				else
				{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveInterFret", false);
				}
				
			}// end if value
			
			return "";
	}
	
	public String definitiveNacInv (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		
			GridField camposp  =  mTab.getField("XX_NACINVOICEAMOUNT");  
			BigDecimal sp = (BigDecimal)camposp.getValue();

			if(value.toString().equals("true"))
			{
				
				Boolean opcion = ADialog.ask(1, new Container(), "XX_DefinitiveAmount");
				
				if(opcion)
					if (sp.intValue()==0){
						Boolean opcion2 = ADialog.ask(1, new Container(), "XX_campo_cero");
						if (!opcion2)
							mTab.setValue("XX_DefinitiveNacInv", false);
						else registrarAjustes(mTab,ctx);
					}else{registrarAjustes(mTab,ctx);}// end opcion
				else
				{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveNacInv", false);
				}
				
			}// end if value
			
			return "";

	}
	
	public String definitiveSeniat (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		
			GridField camposp  =  mTab.getField("XX_SENIATREALESTEEMEDAMOUNT");  
			BigDecimal sp = (BigDecimal)camposp.getValue();

			if(value.toString().equals("true")){
				Boolean opcion = ADialog.ask(1, new Container(), "XX_DefinitiveAmount");
				
				if(opcion){ 
					if (sp.intValue()==0){
						Boolean opcion2 = ADialog.ask(1, new Container(), "XX_campo_cero");
						if (!opcion2)
							mTab.setValue("XX_DefinitiveSeniat", false);
						else registrarAjustes(mTab,ctx);
					}else{registrarAjustes(mTab,ctx);}
				// end opcion
				}else{
					// debo porner en N el campo DIF
					mTab.setValue("XX_DefinitiveSeniat", false);
				}
			}// end if value
			return "";

	} 
	
	
	public String definitiveFee (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		
			GridField camposp  =  mTab.getField("XX_FeeRealAmount");  
			BigDecimal sp = (BigDecimal)camposp.getValue();

			if(value.toString().equals("true")){
				Boolean opcion = ADialog.ask(1, new Container(), "XX_DefinitiveAmount");
				
				if(opcion){ 
					if (sp.intValue()==0){
						Boolean opcion2 = ADialog.ask(1, new Container(), "XX_campo_cero");
						if (!opcion2)
							mTab.setValue("XX_DefinitiveFee", false);
						else registrarAjustes(mTab,ctx);
					}else{registrarAjustes(mTab,ctx);}
				// end opcion
				}else{
					// debo porner en N el campo 
					mTab.setValue("XX_DefinitiveFee", false);
				}
			}// end if value
			return "";

	} 
	
	
	protected void registrarAjustes(GridTab mTab, Ctx ctx){
	    Timestamp fechaActual = new Timestamp(new Date().getTime());
		Vector<Ajuste> ordenes = new Vector<Ajuste>();
		GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
	    Integer guiaNroID = (Integer)campoid.getValue();
		String sql = "SELECT A.C_ORDER_ID, a.XX_VLO_TypeDelivery" +
					", a.m_Warehouse_id , a.C_COUNTRY_ID, a.C_BPARTNER_ID, a.C_PAYMENTTERM_ID" +
					", a.XX_VMR_DEPARTMENT_ID, i.XX_VLO_ImportCostDetail_id" +
					", A.TOTALLINES * A.XX_REALEXCHANGERATE AS TOTAL " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E " +
					", XX_VLO_ImportCostDetail I  " +
					"WHERE A.isSOTrx='N' AND A.XX_ORDERTYPE = 'Importada' " +
					"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
					"AND A.XX_VLO_BOARDINGGUIDE_ID = I.XX_VLO_BOARDINGGUIDE_ID " +
					"AND A.C_BPARTNER_ID=I.C_BPARTNER_ID " +
					"AND E.XX_VLO_BOARDINGGUIDE_ID="+guiaNroID;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = null;
    	PreparedStatement pstmt1 = null;
	    try {
			rs = pstmt.executeQuery();
			while (rs.next()){
				BigDecimal cant = rs.getBigDecimal("TOTAL");
				String typeDelivery = rs.getString("XX_VLO_TypeDelivery");
		    	int warehouseId = rs.getInt("m_Warehouse_id");
		    	int socio = rs.getInt("C_BPARTNER_ID");
		    	int pais = rs.getInt("C_COUNTRY_ID");
		    	int orden = rs.getInt("C_ORDER_ID"); 
		    	int formaPago = rs.getInt("C_PAYMENTTERM_ID");   		 
	    		int departamento = rs.getInt("XX_VMR_DEPARTMENT_ID");
	    		int detail = rs.getInt("XX_VLO_IMPORTCOSTDETAIL_ID");
	    		
	    		ordenes.add(new Ajuste(cant, typeDelivery, warehouseId, socio, pais, orden, formaPago
	    				, departamento, detail, guiaNroID));
			}

			Iterator<Ajuste> i = ordenes.iterator();
			while (i.hasNext()){
				Ajuste ajuste = i.next();
				MImportCostDetail icd = new MImportCostDetail(Env.getCtx(), ajuste.getDetail(), null);
				BigDecimal monto = new BigDecimal((ajuste.getPeso().doubleValue()*icd.getXX_TotalDifference().doubleValue())
				-ajuste.getMontoAjustado(ajuste.getOrden()).doubleValue());
				
				// Si el tipo de distribución es distinta a Pre-Distribuida 
				if (!ajuste.getTypeDelivery().equals("PD") && monto.doubleValue()!=0){
					String sql1 = "";
			    	pstmt1 = null;
			    	sql1="SELECT M_WAREHOUSE_ID,  RATE FROM XX_VSI_DISTIMPORTCOSTS " +
			    			"where (current_date between DATEFROM and DATETO) and ISACTIVE='Y' " +
			    			"and AD_client_ID="+ctx.getAD_Client_ID();
			    	pstmt1 = DB.prepareStatement(sql1, null);
			    	ResultSet rs1 = null;
			    	try {
						rs1 = pstmt1.executeQuery();
						while (rs1.next()){
							MSUMMARY summary = new MSUMMARY(Env.getCtx(), 0, null, ajuste.getSocio()
					    			, ajuste.getPais(), ajuste.getOrden(), ajuste.getFormaPago()
					    			, new BigDecimal(monto.doubleValue()*rs1.getDouble("RATE")*0.01)
									, ajuste.getDepartamento(), rs1.getInt("M_WAREHOUSE_ID"), "T"); 						
					    	/****Jessica Mendoza****/
							summary.setXX_InvoiceDate(fechaActual);
							summary.setXX_DateRegistration(fechaActual);
							summary.setC_Invoice_ID(0);
							summary.setXX_Visible(true);
							summary.setXX_InvoiceNro(" ");
							summary.setXX_ControlNumber(" ");
							/****Fin código - Jessica Mendoza****/
							summary.save();	
						}
						rs1.close();
						pstmt1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally{
						DB.closeResultSet(rs1);
						DB.closeStatement(pstmt1);
					}
			    }else {// Si el tipo de distribución es Pre-Distribuida 
			    	String sql1 = "";

			    	if (monto.doubleValue()!=0){
			    		sql1 = "SELECT b.M_WAREHOUSE_ID, b.XX_WAREHOUSEPERCENTAGE " +
			    					"FROM XX_VMR_DistributionHeader a, XX_VMR_StorePercentage b " +
			    					"WHERE a.XX_VMR_DistributionHeader_ID= b.XX_VMR_DistributionHeader_ID AND " +
			    					"A.C_ORDER_ID=" + ajuste.getOrden();
			    	
			    	
			    		pstmt1 = DB.prepareStatement(sql1, null);
			    	}
			    	ResultSet rs1 = null;
			    	try {
						rs1 = pstmt1.executeQuery();
						while (rs1.next()){
							int Warehouse = rs1.getInt("M_WAREHOUSE_ID");
							BigDecimal porcentaje= rs1.getBigDecimal("XX_WAREHOUSEPERCENTAGE");
							BigDecimal monto1 = new BigDecimal((porcentaje.doubleValue() * 0.01) * monto.doubleValue());
							MSUMMARY summary = new MSUMMARY(Env.getCtx(), 0, null, ajuste.getSocio()
					    			, ajuste.getPais(), ajuste.getOrden(), ajuste.getFormaPago()
					    			, monto1, ajuste.getDepartamento(), Warehouse, "T");
							/****Jessica Mendoza****/
							summary.setXX_InvoiceDate(fechaActual);
							summary.setXX_DateRegistration(fechaActual);
							summary.setXX_Visible(true);
							summary.setC_Invoice_ID(0);
							summary.setXX_InvoiceNro(" ");
							summary.setXX_ControlNumber(" ");
							/****Fin código - Jessica Mendoza****/
					    	summary.save();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally{			
						DB.closeResultSet(rs1);
					}  	
			    }

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{			
			DB.closeStatement(pstmt1);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}  		
	}
}
