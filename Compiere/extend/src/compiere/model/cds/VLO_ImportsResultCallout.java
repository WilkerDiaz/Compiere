package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class VLO_ImportsResultCallout extends CalloutEngine {
	
	
	public String importResult (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
		try
		{
			Integer orderNro = 0;
			Integer orderID = 0;
			
		
			String SQL = ("SELECT A.XX_ORDERNUM AS ORDERNRO " +
					"FROM XX_VLO_IMPORTSDETAIL A, XX_VLO_IMPORT B, C_ORDER C " +
					"WHERE A.XX_VLO_IMPORT_ID = B.XX_VLO_IMPORT_ID " +
					"AND C.DOCUMENTNO = A.XX_ORDERNUM");
			
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    String SQL2 = ("SELECT C.C_ORDER_ID AS ORDERID  " +
					"FROM XX_VLO_IMPORTSDETAIL A, XX_VLO_IMPORT B, C_ORDER C " +
					"WHERE A.XX_VLO_IMPORT_ID = B.XX_VLO_IMPORT_ID " +
					"AND C.DOCUMENTNO = A.XX_ORDERNUM");
			
			
			PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
		    ResultSet rs2 = pstmt2.executeQuery();
		    
		    while(rs.next() && rs2.next())
		    {
		    	X_XX_VLO_ImportsResult impResul = new X_XX_VLO_ImportsResult(ctx, 0, null); 
		    	
		    	orderNro = rs.getInt("ORDERNRO");
		    	orderID = rs2.getInt("ORDERID");
		    	
		    	String SQL3 = ("SELECT A.NAME AS CATEGORIA, B.DOCUMENTNO AS ORDERNUM, B.XX_ARRIVALDATE AS LLEGADA, C.VALUE AS DPTO, D.NAME AS CATA, " +
		    			"E.NAME AS PROV, B.XX_ORDERSTATUS AS STATUS, B.XX_CONSIGNEENAME AS CONSEG, F.NAME AS COUNTRY, B.XX_PRODUCTQUANTITY AS QUANTITY, " +
		    			"XX_INCOTERMS AS ICO, I.NAME AS PAGO, B.GRANDTOTAL AS TOTAL, G.XX_INVOCECANCEAMOUNT AS TOTALPROV, G.XX_VENDORINVOICE AS FACNRO, " +
		    			"H.XX_NACFRETREALAMOUNT AS FLETENAC, H.XX_INTERFRETREALAMOUNT AS FLETEIN, " +
		    			"H.XX_TYPELOAD AS CARGA, H.XX_GUIDENUMBER AS GUIA, H.XX_SHIPFLIGHTNAME AS NAMEBV, " +
		    			"H.XX_CARGOAGENTNAME AS CARGONAME, H.XX_SHIPPINGPORT AS EPUERTO, H.XX_ARRIVALPORT AS LLPUERTO, H.XX_CUSTOMSAGENTNAME AS CUSTOMNAME, " +
		    			"H.XX_EXPENSEACCOUNT AS GASTOS, H.XX_CUSTOMSAGENTANTICAMOUNT AS ANTI, H.XX_NACTREASREALAMOUNT AS TESORERIA, H.XX_SENIATREALESTEEMEDAMOUNT AS SENIAT, " +
		    			"B.DATEPROMISED AS DAYENTREGA, H.XX_CARAGENTDELIVESTEMEDDATE AS DAYAGENCAR, H.XX_CARAGENTDELIVREALDATE AS REALCARGO, " +
		    			"(H.XX_CARAGENTDELIVREALDATE - B.DATEPROMISED) AS TOTALDAY, H.XX_SHIPPREALESTEEMEDDATE AS EMBARESTI, H.XX_SHIPPINGREALDATE AS EMBARREAL, " +
		    			"G.XX_VZLAARRIVALESTEMEDDATE AS VZLAES, G.XX_VZLAARRIVALREALDATE AS VZLARE, " +
		    			"H.XX_SINDUNEATRANSESTEEMEDDATE AS SIDUESTI, H.XX_SINDUNEATRANSREALDATE AS SIDUREAL, H.XX_CUSTOMRIGHTSCANCELDATE AS DATENAC, " +
		    			"H.XX_CDARRIVALESTEEMEDDATE AS CDESTI, H.XX_CDARRIVALREALDATE AS CDREAL, (H.XX_CDARRIVALREALDATE - H.XX_CARAGENTDELIVREALDATE) AS TotalDayProvCD, " +
		    			"(XX_CDARRIVALREALDATE + 7) AS CheckDate, H.XX_CUSTAGENINVOIEMISIONDATE AS DAYFLETEE, H.XX_CUSTAGENINVOIRECEPTDATE AS DAYFLETER, " +
		    			"H.XX_AAREALAMOUNT AS MONTOAA, H.XX_CUSTOMAGENTINVOICE AS NROAA, H.XX_CARGOAGENTINVOEMISIONDATE AS CARESTI, H.XX_CARGOAGENTINVORECEPDATE AS CARRECP, " +
		    			"H.XX_AGENTLOADINVOICEAMOUNT AS MONTOAAS, H.XX_CARGOAGENTINVOICE AS NROFAC " +
		    			"FROM XX_VMR_CATEGORY A, C_ORDER B, XX_VMR_DEPARTMENT C, XX_VMR_CAMPAIGN D, C_BPARTNER E, C_COUNTRY F, XX_VLO_IMPORTSDETAIL G, " +
		    			"XX_VLO_IMPORT H, C_PAYMENTTERM I " +
		    			"WHERE A.XX_VMR_CATEGORY_ID = B.XX_CATEGORY_ID " +
		    			"AND C.XX_VMR_DEPARTMENT_ID = B.XX_VMR_DEPARTMENT_ID " +
		    			"AND D.XX_VMR_CAMPAIGN_ID = B.XX_BROCHURE_ID " +
		    			"AND B.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    			"AND B.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    			"AND G.XX_VLO_IMPORT_ID = H.XX_VLO_IMPORT_ID " +
		    			"AND B.C_PAYMENTTERM_ID = I.C_PAYMENTTERM_ID " +
		    			"AND G.XX_ORDERNUM = '"+orderNro+"' " +
		    			"AND B.C_ORDER_ID = '"+orderID+"'");
		    	
		    	PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
		    	ResultSet rs3 = pstmt3.executeQuery();

		    	if(rs3.next())
		    	{
    		
		    		impResul.setAD_Client_ID(1000005);
		    		impResul.setAD_Org_ID(1000030);
		    		
		    		
		    		impResul.setXX_CATEGORY(rs3.getString("CATEGORIA"));
		    		impResul.setXX_ORDERNUM(rs3.getInt("ORDERNUM"));
		    		impResul.setXX_ArrivalDate(rs3.getTimestamp("LLEGADA"));
		    		impResul.setXX_DEPARTMENT(rs3.getString("DPTO"));
		    		impResul.setXX_CATALOGCODE(rs3.getString("CATA"));
		    		impResul.setC_BPartner(rs3.getString("PROV"));
		    		impResul.setXX_OrderStatus(rs3.getString("STATUS"));
		    		impResul.setXX_CONSIGNEE(rs3.getString("CONSEG"));
		    		impResul.setXX_Country(rs3.getString("COUNTRY"));
		    		impResul.setXX_PIECESTOTAL(rs3.getInt("QUANTITY"));
		    		impResul.setXX_INCOTERMS(rs3.getString("ICO"));
		    		impResul.setXX_CONDITIONPAYMENT(rs3.getString("PAGO"));
		    		impResul.setXX_AMOUNTTOTALDOL(rs3.getInt("TOTAL"));
		    		impResul.setXX_TOTALDOLINVOICE(rs3.getInt("TOTALPROV"));
		    		impResul.setXX_INVOICENRO(rs3.getString("FACNRO"));
		    		impResul.setXX_NacFretRealAmount(rs3.getInt("FLETENAC"));
		    		impResul.setXX_InterFretRealAmount(rs3.getInt("FLETEIN"));
		    		impResul.setXX_TYPELOAD(rs3.getString("CARGA"));
		    		impResul.setXX_GUIANUM(rs3.getInt("GUIA"));
		    		impResul.setXX_SHIPFLIGHTNAME(rs3.getString("NAMEBV"));
		    		//impResul.setXX_CARGOAGENTNAME(rs3.getString("CARGONAME"));
		    		impResul.setXX_SHIPPINGPORT(rs3.getString("EPUERTO"));
		    		impResul.setXX_ARRIVALPORT(rs3.getString("LLPUERTO"));
		    		impResul.setXX_CUSTOMSAGENTNAME(rs3.getString("CUSTOMNAME"));
		    		impResul.setXX_EXPENSEACCOUNT(rs3.getInt("GASTOS"));
		    		impResul.setXX_CUSTOMSAGENTANTICAMOUNT(rs3.getInt("ANTI"));
		    		impResul.setXX_NACTREASREALAMOUNT(rs3.getInt("TESORERIA"));
		    		impResul.setXX_SENIATREALESTEEMEDAMOUNT(rs3.getInt("SENIAT"));
		    		impResul.setDatePromised(rs3.getTimestamp("DAYENTREGA"));
		    		impResul.setXX_CARAGENTDELIVESTEMEDDATE(rs3.getTimestamp("DAYAGENCAR"));
		    		impResul.setXX_CARAGENTDELIVREALDATE(rs3.getTimestamp("REALCARGO"));
		    		impResul.setXX_TOTALDAY(rs3.getInt("TOTALDAY"));
		    		impResul.setXX_SHIPPREALESTEEMEDDATE(rs3.getTimestamp("EMBARESTI"));
		    		impResul.setXX_SHIPPINGREALDATE(rs3.getTimestamp("EMBARREAL"));
		    		impResul.setXX_VZLAARRIVALESTEMEDDATE(rs3.getTimestamp("VZLAES"));
		    		impResul.setXX_VZLAARRIVALREALDATE(rs3.getTimestamp("VZLARE"));
		    		impResul.setXX_SINDUNEATRANSESTEEMEDDATE(rs3.getTimestamp("SIDUESTI"));
		    		impResul.setXX_SINDUNEATRANSREALDATE(rs3.getTimestamp("SIDUREAL"));
		    		impResul.setXX_CUSTOMRIGHTSCANCELDATE(rs3.getTimestamp("DATENAC"));
		    		impResul.setXX_CDARRIVALESTEEMEDDATE(rs3.getTimestamp("CDESTI"));
		    		impResul.setXX_CDARRIVALREALDATE(rs3.getTimestamp("CDREAL"));
		    		impResul.setXX_TOTALDAYPROVCD(rs3.getInt("TotalDayProvCD"));
		    		impResul.setXX_CHECKDATE(rs3.getTimestamp("CheckDate"));
		    		impResul.setXX_CUSTAGENINVOIEMISIONDATE(rs3.getTimestamp("DAYFLETEE"));
		    		impResul.setXX_CUSTAGENINVOIRECEPTDATE(rs3.getTimestamp("DAYFLETER"));
		    		impResul.setXX_AARealAmount(rs3.getInt("MONTOAA"));
		    		impResul.setXX_CUSTOMAGENTINVOICE(rs3.getInt("NROAA"));
		    		impResul.setXX_CARGOAGENTINVOEMISIONDATE(rs3.getTimestamp("CARESTI"));
		    		impResul.setXX_CARGOAGENTINVORECEPDATE(rs3.getTimestamp("CARRECP"));
		    		impResul.setXX_AGENTLOADINVOICEAMOUNT(rs3.getInt("MONTOAAS"));
		    		impResul.setXX_CARGOAGENTINVOICE(rs3.getInt("NROFAC"));

		    		impResul.save();

		    		
		    	}rs3.close(); pstmt3.close();
		    	
		    	
		    }// end while
		    rs.close(); pstmt.close();
		    rs2.close(); pstmt2.close();
		    
			
		} // end try
		
		catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "";
		
	}
	

}
