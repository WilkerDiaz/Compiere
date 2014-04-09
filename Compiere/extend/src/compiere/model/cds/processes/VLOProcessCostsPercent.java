package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VLO_CostsPercent;
import compiere.model.cds.X_XX_VLO_DateCostPercent;
import compiere.model.cds.X_XX_VLO_PCProcess;

public class VLOProcessCostsPercent extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
				
		try
		{
			X_XX_VLO_PCProcess PC = new X_XX_VLO_PCProcess(getCtx(),getRecord_ID(),get_TrxName());
			X_XX_VLO_DateCostPercent dPC = new X_XX_VLO_DateCostPercent(getCtx(),0,get_TrxName());
			
			Integer dayD = (Integer)PC.get_Value("XX_DATED");
			Integer dayH = (Integer)PC.get_Value("XX_DATEH");
			Integer yearD = (Integer)PC.get_Value("XX_YEARD");
			Integer yearH = (Integer)PC.get_Value("XX_YEARH");
			
			//Integer i = 0;
			
			BigDecimal cant = new BigDecimal(0);
			
			BigDecimal pefi = new BigDecimal(0);
			BigDecimal pefiest = new BigDecimal(0);
			BigDecimal pefiT = new BigDecimal(0);
			BigDecimal pefiC = new BigDecimal(0);
			
			BigDecimal pefn = new BigDecimal(0);
			BigDecimal pefnest = new BigDecimal(0);
			BigDecimal pefnC = new BigDecimal(0);
			BigDecimal pefnT = new BigDecimal(0);
			
			BigDecimal peaa = new BigDecimal(0);
			BigDecimal peaaest = new BigDecimal(0);
			BigDecimal peaaC = new BigDecimal(0);
			BigDecimal peaaT = new BigDecimal(0);
			
			
			BigDecimal pse = new BigDecimal(0);
			BigDecimal pseT = new BigDecimal(0);

			
			//DESDE
		    String resul1 = new String(""+dayD);
		    String resul2 = new String(""+yearD);
		    String resulT = new String (resul2+resul1);
		    Integer desde = new Integer(resulT);
		    String resulTo = new String (resul2+"-"+resul1);
		    
		    //HASTA
		    String resul3 = new String(""+dayH);
		    String resul4 = new String(""+yearH);
		    String resulT2 = new String (resul4+resul3);
		    Integer hasta = new Integer(resulT2);
		    String resulTo2 = new String (resul4+"-"+resul3);
	
		    
		    String SQL = ("SELECT DISTINCT B.NAME AS PORT, C.NAME AS PARTNER, D.NAME AS COUNTRY, C.C_BPARTNER_ID AS PROVID, D.C_COUNTRY_ID AS COUNTRYID, B.XX_VLO_ARRIVALPORT_ID AS PORTID " +
		    		"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D " +
		    		"WHERE A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
		    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
		    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
		    		"AND A.XX_ORDERSTATUS = 'RE' " +
		    		"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM')");
		    
		    PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
	
		    while(rs.next())
		    {
		    	X_XX_VLO_CostsPercent pcp  = new X_XX_VLO_CostsPercent(getCtx(), 0, get_TrxName());
		    	
		    	String namePort = rs.getString("PORT");
		    	String namePartner = rs.getString("PARTNER");
		    	String nameCountry = rs.getString("COUNTRY");
		    	Integer provid = rs.getInt("PROVID");
				Integer countryid = rs.getInt("COUNTRYID");
				Integer puertoid = rs.getInt("PORTID");
		    	
		   
		    	String SQL2 = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    			"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D, C_CONVERSION_RATE F " +
		    			"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    			"AND A.XX_ORDERTYPE = 'Importada' " +
		    			"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
		    			"AND B.NAME= '"+namePort+"' " +
		    			"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
		    			"AND D.NAME = '"+nameCountry+"' " +
		    			"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
		    			"AND C.NAME = '"+namePartner+"' " +
		    			"AND A.XX_ORDERSTATUS = 'RE' " +
		    			"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM')");
		    	 
		    
			    PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
			    ResultSet rs2 = pstmt2.executeQuery();
			    
			    if(rs2.next())
			    {
			    	cant = rs2.getBigDecimal("TOTALOR");
			    }
			    rs2.close();
			    pstmt2.close();
			    
			    
			    // CON GUIA DE EMBARQUE (TOMO LOS VALORES REALES)
			    
			    String SQL3 = ("SELECT SUM (A.XX_NACINVOICEAMOUNTPRO) AS PEFN " +
			    		"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D, XX_VLO_BOARDINGGUIDE E " +
			    		"WHERE A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
			    		"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
			    		"AND B.NAME= '"+namePort+"' " +
			    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
			    		"AND D.NAME = '"+nameCountry+"' " +
			    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
			    		"AND C.NAME = '"+namePartner+"' " +
			    		"AND A.XX_ORDERSTATUS = 'RE' " +
			    		"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM')");
		
			    			    
			    PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
			    ResultSet rs3 = pstmt3.executeQuery();
			    
			    		    
			    // SIN GUIA DE EMBARQUE (TOMO LOS VALORES ESTIMADOS)
			    
			    String SQL6 = ("SELECT SUM (A.XX_NationalEsteemedAmount) AS PEFNEST " +
			    		"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D " +
			    		"WHERE A.XX_ORDERTYPE = 'Importada' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID IS NULL " +
			    		"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
			    		"AND B.NAME= '"+namePort+"' " +
			    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
			    		"AND D.NAME = '"+nameCountry+"' " +
			    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
			    		"AND C.NAME = '"+namePartner+"' " +
			    		"AND A.XX_ORDERSTATUS = 'RE' " +
			    		"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM') ");
			     
			    PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
			    ResultSet rs6 = pstmt6.executeQuery();
			    
			    
			    if (rs3.next())
			    {
			    	pefn = rs3.getBigDecimal("PEFN");
			    	//pefnT = pefn.divide(cant, 2, RoundingMode.HALF_UP);
			    	if(pefn == null)
			    	{
			    		pefn = new BigDecimal(0);
			    	}
			    	//System.out.println("pefn real "+pefn);
			   
			    }
			    rs3.close();
			    pstmt3.close();
			    
			    if (rs6.next())
			    {
			    	pefnest = rs6.getBigDecimal("PEFNEST");
			    	
			    	if(pefnest == null)
			    	{
			    		pefnest = new BigDecimal(0);
			    	}
			    	//System.out.println("pefnest estimado "+pefnest);
			    }
			    rs6.close();
			    pstmt6.close();
			    
			    pefnC = pefn.add(pefnest);
			    //System.out.println("suma de los dos "+pefnC);
			    // % ESTIMADO DE FLETE NACIONAL 
			    pefnT = pefnC.divide(cant, 2, RoundingMode.HALF_UP);
			    //System.out.println("total "+pefnT);
					    
			    
			    // SE CALCULA EL % ESTIMADO DE FLETE INTERNACIONAL
		    
			    String SQL4 = ("SELECT SUM (A.XX_InterFretRealAmountPro) AS PEFI " +
			    		"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D, XX_VLO_BOARDINGGUIDE E " +
			    		"WHERE A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
			    		"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
			    		"AND B.NAME= '"+namePort+"' " +
			    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
			    		"AND D.NAME = '"+nameCountry+"' " +
			    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
			    		"AND C.NAME = '"+namePartner+"' " +
			    		"AND A.XX_ORDERSTATUS = 'RE' " +
			    		"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM')");
			    
			    PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null); 
			    ResultSet rs4 = pstmt4.executeQuery();
			    
			    String SQL7 = ("SELECT SUM (A.XX_INTNACESTMEDAMOUNT) AS PEFIEST " +
			    		"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D " +
			    		"WHERE A.XX_ORDERTYPE = 'Importada' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID IS NULL " +
			    		"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
			    		"AND B.NAME= '"+namePort+"' " +
			    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
			    		"AND D.NAME = '"+nameCountry+"' " +
			    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
			    		"AND C.NAME = '"+namePartner+"' " +
			    		"AND A.XX_ORDERSTATUS = 'RE' " +
			    		"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM') ");
			     
			    PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null); 
			    ResultSet rs7 = pstmt7.executeQuery();
			    
			     
			    if (rs4.next())
			    {
			    	pefi = rs4.getBigDecimal("PEFI");
			    	//pefiT = pefi.divide(cant, 2, RoundingMode.HALF_UP);
			    	if (pefi == null)
			    	{
			    		pefi = new BigDecimal(0);
			    	}
			   
			    }
			    rs4.close();
			    pstmt4.close();
			    
			    if (rs7.next())
			    {
			    	pefiest = rs7.getBigDecimal("PEFIEST");
			    	
			    	if (pefiest == null)
			    	{
			    		pefiest = new BigDecimal(0);
			    	}
			    }
			    rs7.close();
			    pstmt7.close();
			    
			    pefiC = pefi.add(pefiest);
			    pefiT = pefiC.divide(cant, 2, RoundingMode.HALF_UP);
					    
			    // % ESTIMADO DE AGENTE ADUANAL

			    String SQL5 = ("SELECT SUM (A.XX_CUSTOMAGENTAMOUNTPRO) AS PEAA " +
			    		"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D, XX_VLO_BOARDINGGUIDE E " +
			    		"WHERE A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
			    		"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
			    		"AND B.NAME= '"+namePort+"' " +
			    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
			    		"AND D.NAME = '"+nameCountry+"' " +
			    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
			    		"AND C.NAME = '"+namePartner+"' " +
			    		"AND A.XX_ORDERSTATUS = 'RE' " +
			    		"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM')");
			    
			    PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
			    ResultSet rs5 = pstmt5.executeQuery();
			    
			    String SQL8 = ("SELECT SUM (A.XX_CustomsAgentEsteemedAmount) AS PEAAEST " +
			    		"FROM C_ORDER A, XX_VLO_ARRIVALPORT B, C_BPARTNER C, C_COUNTRY D " +
			    		"WHERE A.XX_ORDERTYPE = 'Importada' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID IS NULL " +
			    		"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
			    		"AND B.NAME= '"+namePort+"' " +
			    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
			    		"AND D.NAME = '"+nameCountry+"' " +
			    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
			    		"AND C.NAME = '"+namePartner+"' " +
			    		"AND A.XX_ORDERSTATUS = 'RE' " +
			    		"AND A.XX_ARRIVALDATE BETWEEN TO_DATE('"+desde+"', 'YYYYMM') AND TO_DATE('"+hasta+"', 'YYYYMM') ");
			     
			    PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
			    ResultSet rs8 = pstmt8.executeQuery();
			   
			    
			    if (rs5.next())
			    {
			    	peaa =rs5.getBigDecimal("PEAA");
			    	//peaaT = peaa.divide(cant, 2, RoundingMode.HALF_UP);
			    	if (peaa == null)
			    	{
			    		peaa = new BigDecimal(0);
			    	}

			    }
			    rs5.close();
			    pstmt5.close();
			    
			    if (rs8.next())
			    {
			    	peaaest = rs8.getBigDecimal("PEAAEST");
			    	
			    	if(peaaest == null)
			    	{
			    		peaaest = new BigDecimal(0);
			    	}
			    }
			    rs8.close();
			    pstmt8.close();
			    
			    peaaC = peaa.add(peaaest);			    
			    peaaT = peaaC.divide(cant, 2, RoundingMode.HALF_UP);
			    
			    
			    // 	ESTO NO VA POR AHORA 
			    
			    /*String SQL6 = ("SELECT SUM (TO_NUMBER(E.XX_RealInsuranceAmount/(A.TOTALLINES * G.MULTIPLYRATE))) AS PSE " +
			    		"FROM C_ORDER A, XX_VLO_IMPORT E, XX_VLO_IMPORTSDETAIL F, XX_VLO_ARRIVALPORT B, C_COUNTRY D, C_BPARTNER C, C_CONVERSION_RATE G " +
			    		"WHERE E.XX_VLO_IMPORT_ID = F.XX_VLO_IMPORT_ID " +
			    		"AND G.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND F.XX_ORDERNUM = A.DOCUMENTNO " +
			    		"AND A.XX_ORDERTYPE = '10000003' " +
			    		"AND A.XX_VLO_ARRIVALPORT_ID = B.XX_VLO_ARRIVALPORT_ID " +
			    		"AND B.NAME= '"+namePort+"' " +
			    		"AND A.C_COUNTRY_ID = D.C_COUNTRY_ID " +
			    		"AND D.NAME = '"+nameCountry+"' " +
			    		"AND A.C_BPARTNER_ID = C.C_BPARTNER_ID " +
			    		"AND C.NAME = '"+namePartner+"'");
			    
			    PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
			    ResultSet rs6 = pstmt6.executeQuery();
			    
			    
			    if (rs6.next())
			    {
			    	pse = rs6.getBigDecimal("PSE");
			    	pseT = pse.divide(cant, 2, RoundingMode.HALF_UP);
			    }
			    rs6.close();
			    pstmt6.close();*/
			    
			    //pcp.setAD_Client_ID(1000005);
			    //pcp.setAD_Org_ID(1000030);
			    pcp.setXX_YEARH(resulTo2);
			    pcp.setXX_DATEH(resulTo);
			    //pcp.setXX_Country(nameCountry);
			    //pcp.setXX_BPARTHNER(namePartner);
			    //pcp.setXX_ARRIVALPORT(namePort);
			    pcp.setC_Country_ID(countryid);
			    pcp.setC_BPartner_ID(provid);
			    pcp.setXX_VLO_ArrivalPort_ID(puertoid);
			    pcp.setXX_INTERFREESTIMATEPERT(pefiT);
			    pcp.setXX_NACFREESTIMATEPERT(pefnT);
			    pcp.setXX_ESTIMATEDPERTUSAGENT(peaaT);
			    //pcp.setXX_ESTIMATEDPERTINSUR(pseT);
		    	pcp.save();
			    //i ++;

		    	
		    }//end while
		    rs.close();
		    pstmt.close();

		    dPC.setXX_DateFrom(resulTo);
		    dPC.setXX_DateUntil(resulTo2);
		    dPC.save();
		    
		    return "Proceso Realizado";
		    
		}//end try
		
		catch (Exception e) {
			return e.getMessage();
			
			// TODO: handle exception
		}// end catch
		
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
