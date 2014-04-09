package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class VLO_EstimatedCallout extends CalloutEngine {
	
	
/*	public String Insert(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
		try
		{		
			GridField campoOrderNro =  mTab.getField("XX_ORDERNUM");  
		    Integer OrderNro = (Integer)campoOrderNro.getValue();
		    
		    GridField campoImport =  mTab.getField("XX_VLO_Import_ID");  
		    Integer Import = (Integer)campoImport.getValue();
		 		   		    
		    if (OrderNro != 0)
		    {		    
			    X_XX_VLO_ImportsDetail detail = new X_XX_VLO_ImportsDetail(ctx, 0, null);	 
			    
			    detail.setXX_ORDERNUM(OrderNro);
			    //detail.setXX_VLO_IMPORT_ID(Import);
			    detail.setXX_VLO_Import_ID(Import);
			    detail.save();
			    EstimatedMusts(ctx, Import, mTab, campoImport, oldValue, oldValue);
		    }
	    
	
			return "";
			
		}//end try

		catch (Exception e) {
			// TODO: handle exception}
			return e.getMessage();
		}
	}*/
	
	public String EstimatedMusts(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		try
		{	
			
			BigDecimal mefi = new BigDecimal(0);
			BigDecimal mefn = new BigDecimal(0);
			BigDecimal meaa = new BigDecimal(0);
			BigDecimal cif = new BigDecimal(0);
			BigDecimal rate = new BigDecimal(0);
			BigDecimal mes = new BigDecimal(0);
			BigDecimal part1 = new BigDecimal(0);
			BigDecimal part2 = new BigDecimal(0);
			BigDecimal pes = new BigDecimal(0);
			BigDecimal part3 = new BigDecimal(0);
			BigDecimal part4 = new BigDecimal(0);
			
			Timestamp fee = new Timestamp(1);
			Timestamp fecda = new Timestamp(1);
			Timestamp fele = new Timestamp(1);
			Timestamp fecd = new Timestamp(1);
			
			GridField campoOrderNro =  mTab.getField("XX_ORDERNUM");  
		    Integer OrderNro = (Integer)campoOrderNro.getValue();
		    
		   
		    String SQL = ("SELECT A.XX_INTERFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFI " +
		    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F, C_CONVERSION_RATE G " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND A.XX_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.XX_BPARTHNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
		    
		    PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		  
		    if(rs.next())
		    {
		    	mefi = rs.getBigDecimal("MEFI");
		    	mTab.setValue("XX_INTNACESTMEDAMOUNT", mefi);
		    	
		    }
		    rs.close();
		    pstmt.close();
		    
		    String SQL2 = ("SELECT DISTINCT A.XX_NACFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFN " +
		    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F, C_CONVERSION_RATE G " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND  C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.XX_BPARTHNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
		    
		    PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
		    ResultSet rs2 = pstmt2.executeQuery();
		 
		    if(rs2.next())
		    {
		    	mefn = rs2.getBigDecimal("MEFN");
		    	mTab.setValue("XX_NACESTEEMEDAMOUNT", mefn);
		    	
		    }
		    rs2.close();
		    pstmt2.close();
		    
		    
		    String SQL3 = ("SELECT A.XX_ESTIMATEDPERTUSAGENT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEAA " +
		    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F, C_CONVERSION_RATE G " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND  C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.XX_BPARTHNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
		    
		    PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
		    ResultSet rs3 = pstmt3.executeQuery();
		
		    if(rs3.next())
		    {
		    	meaa = rs3.getBigDecimal("MEAA");
		    	mTab.setValue("XX_CUSTOMSAGENTESTEMEDAMOUNT", meaa);
		    	
		    }
		    rs3.close();
		    pstmt3.close();
		    
		    String SQL4 = ("SELECT (A.TOTALLINES + B.XX_NacFretRealAmount + B.XX_InterFretRealAmount) CIF " +
		    		"FROM C_ORDER A, XX_VLO_IMPORT B, XX_VLO_IMPORTSDETAIL C " +
		    		"WHERE A.DOCUMENTNO = C.XX_ORDERNUM " +
		    		"AND B.XX_VLO_IMPORT_ID = C.XX_VLO_IMPORT_ID " +
		    		"AND C.XX_ORDERNUM = A.DOCUMENTNO " +
		    		"AND C.XX_ORDERNUM = '"+OrderNro+"'");
		    
   
		    PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null); 
		    ResultSet rs4 = pstmt4.executeQuery();
		    
		    if (rs4.next())
		    {
		    	cif = rs4.getBigDecimal("CIF");
		    }
		    rs4.close();
		    pstmt4.close();
		    
		    String SQL5 = ("SELECT XX_RATE AS RATE FROM XX_VLO_IMPORTRATE WHERE NAME = 'Tasa de Servicio de aduanas SENIAT'");
		    
		    PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
		    ResultSet rs5 = pstmt5.executeQuery();
		    
		    if (rs5.next())
		    {
		    	rate = rs5.getBigDecimal("RATE");
		    }
		    rs5.close();
		    pstmt5.close();
		    
		    mes = cif.multiply(rate);
		    mTab.setValue("XX_SENIATESTEEMEDAMUNT", mes);
		    
		    String SQL6 = ("SELECT G.XX_RATE * (C.TOTALLINES * H.MULTIPLYRATE) AS PART1 " +
		    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F, XX_VLO_DISPATCHROUTE G, C_CONVERSION_RATE H " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND C.XX_VLO_DISPATCHROUTE_ID = G.XX_VLO_DISPATCHROUTE_ID " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND H.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.XX_BPARTHNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
		    
		    PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
		    ResultSet rs6 = pstmt6.executeQuery();
		    
		    if(rs6.next())
		    {
		    	part1 = rs6.getBigDecimal("PART1");
		    	part2 = mefi.add(mefn);
		    	part3 = meaa.add(mes);
		    	part4 = part2.add(part3);
		    	pes = part1.add(part4);
		    	mTab.setValue("XX_ESTEEMEDINSURANCEAMOUNT", pes);
		    }
		    rs6.close();
		    pstmt6.close();
		    

		    ////////////// ahora las fechas //////////////
		    
		    String SQL7 = ("SELECT TO_DATE(A.XX_INTERNACARRIVALTIMETEI + C.DATEPROMISED) AS FEE " +
		    		"FROM XX_VLO_LEADTIMES A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND A.XX_VLO_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.C_BPARTNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
	
		    PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null); 
		    ResultSet rs7 = pstmt7.executeQuery();
		   
		    if(rs7.next())
		    {
		    	fee = rs7.getTimestamp("FEE");
		    	mTab.setValue("XX_SHIPPREALESTEEMEDDATE", fee);
		    
		    }
		    rs7.close();
		    pstmt7.close();
		    
		    String SQL8 = ("SELECT TO_DATE(A.XX_INTERNACARRIVALTIMETEI + A.XX_TRANSITTIMETT + A.XX_NATIONALIZATIONTIMETNAC + C.DATEPROMISED) AS FECDA " +
		    		"FROM XX_VLO_LEADTIMES A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND A.XX_VLO_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.C_BPARTNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
		 	    
		    PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
		    ResultSet rs8 = pstmt8.executeQuery();
		    
		    if(rs8.next())
		    {
		    	fecda = rs8.getTimestamp("FECDA");
		    	mTab.setValue("XX_CUSTDUTICANESTIDATE", fecda);
		    	
		    }
		    rs8.close();
		    pstmt8.close();
		    
		    String SQL9 = ("SELECT TO_DATE(A.XX_INTERNACARRIVALTIMETEI + A.XX_TRANSITTIMETT + C.DATEPROMISED) AS FELE " +
		    		"FROM XX_VLO_LEADTIMES A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND A.XX_VLO_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.C_BPARTNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
		 
		    
		    PreparedStatement pstmt9 = DB.prepareStatement(SQL9, null); 
		    ResultSet rs9 = pstmt9.executeQuery();
		    
		    if(rs9.next())
		    {
		    	fele = rs9.getTimestamp("FELE");
		    	mTab.setValue("XX_VZLAARRIVALESTEMEDDATE", fele);
		    	
		    }
		    rs9.close();
		    pstmt9.close();
		    
		    String SQL10 = ("SELECT TO_DATE(A.XX_INTERNACARRIVALTIMETEI + A.XX_TRANSITTIMETT + XX_NATIONALIZATIONTIMETNAC + XX_NACARRIVALTIMETEN + C.DATEPROMISED) AS FECD " +
		    		"FROM XX_VLO_LEADTIMES A, C_ORDER C, XX_VLO_ARRIVALPORT D, C_BPARTNER E, C_COUNTRY F " +
		    		"WHERE C.DOCUMENTNO = '"+OrderNro+"' " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = D.XX_VLO_ARRIVALPORT_ID " +
		    		"AND A.XX_VLO_ARRIVALPORT = D.NAME " +
		    		"AND C.C_BPARTNER_ID = E.C_BPARTNER_ID " +
		    		"AND A.C_BPARTNER = E.NAME " +
		    		"AND C.C_COUNTRY_ID = F.C_COUNTRY_ID " +
		    		"AND A.XX_COUNTRY = F.NAME ");
		    
		    PreparedStatement pstmt10 = DB.prepareStatement(SQL10, null); 
		    ResultSet rs10 = pstmt10.executeQuery();
		    
		    if(rs10.next())
		    {
		    	fecd = rs10.getTimestamp("FECD");
		    	mTab.setValue("XX_CDARRIVALESTEEMEDDATE", fecd);
		    	
		    }
		    rs10.close();
		    pstmt10.close();
		    
		    
		   /* if (OrderNro != 0)	
		    {
			    String SQL20 = ("delete from xx_vlo_importsdetail " +
			    		"where XX_VLO_IMPORTSDETAIL_ID IN (SELECT MAX (XX_VLO_IMPORTSDETAIL_ID) FROM XX_VLO_IMPORTSDETAIL)");
			    
			    PreparedStatement pstmt20 = DB.prepareStatement(SQL20, null); 
			    ResultSet rs20 = pstmt20.executeQuery();
			    
			   // NUEVO  
			    String SQL21 = ("delete from xx_vlo_importsdetail " +
	    		"where XX_ORDERNUM = '"+OrderNro+"' ");
	    
			    PreparedStatement pstmt21 = DB.prepareStatement(SQL21, null); 
			    ResultSet rs21 = pstmt21.executeQuery();
			    
			    //System.out.println(SQL20);
			    
		    }*/
		    
			return "";
			
		}//end try

		
		catch (Exception e) {
			// TODO: handle exception}
			return e.getMessage();
		}

	}

}
