package compiere.model.cds.processes;
 
import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MClient;
import org.compiere.model.MPInstance;
import org.compiere.model.MUOMConversion;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_Order;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MVLOBoardingGuide;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_XX_VLO_BoardingGuide;
import compiere.model.cds.X_XX_VMR_Category;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_IncreaseFactor;
import compiere.model.cds.X_XX_VMR_PO_Approval;
import compiere.model.cds.X_XX_VMR_PO_ProductDistrib;
import compiere.model.cds.X_XX_VMR_Package;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.cds.X_XX_VMR_UnitConversion;
import compiere.model.cds.X_XX_VSI_Client;

public class XX_ApproveImports extends SvrProcess{

	Vector <String> orderSITME_I = new Vector <String>();
	Vector <String> orderSITME_N = new Vector <String>();
	
	@Override
	protected String doIt() throws Exception {
		
		X_XX_VLO_BoardingGuide imports = new X_XX_VLO_BoardingGuide(getCtx(),getRecord_ID(),get_Trx()); 
				
		Integer importId = getRecord_ID();
		Integer orderNum = 0;
		Integer orderID = 0;
		BigDecimal factorEst;
		BigDecimal val, val2, val3;
		BigDecimal priceListPO = new BigDecimal(1);
		Integer conteo = 0;
		Integer conteoY = 0;
		Integer cantidaLine = 0; 
		Integer cantidaRef = 0;
		//Integer priceDef = 0;
		BigDecimal montoFactura = new BigDecimal(0);
		BigDecimal factorCambio = new BigDecimal(0);
		BigDecimal sumFac = new BigDecimal(0);
		BigDecimal montoFacturaBs = new BigDecimal(0);
		BigDecimal percentOC = new BigDecimal(0);
		BigDecimal factorDef = new BigDecimal(0);
		BigDecimal factorPart1 = new BigDecimal(0);
		BigDecimal factorPart2 = new BigDecimal(0);
		BigDecimal factorPart3 = new BigDecimal(0);
		BigDecimal factorPart4 = new BigDecimal(0);
		
		Calendar cal = Calendar.getInstance();
        
        int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int nano = cal.get(Calendar.SECOND);
        
        Timestamp XXExitDate = new Timestamp(year-1900,month,day,hour,min,sec,nano);
       
        // Valido que el monto de la factura del proveedor no este cero en alguna O/C
        String SQL30 = ("SELECT A.DOCUMENTNO AS POID, A.XX_VendorInvoiceAmount AS MONTOPROV " +
        		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
        		"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
        		"AND A.XX_ORDERTYPE = 'Importada' AND A.ISSOTRX = 'N' " +
        		"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' ");
        
        PreparedStatement pstmt30 = DB.prepareStatement(SQL30, null); 
	    ResultSet rs30 = pstmt30.executeQuery();
	    
	    Boolean aux = true;
	    
	    Vector <String> order = new Vector <String>();
	    
	    while(rs30.next())
	    {
	    	montoFactura = rs30.getBigDecimal("MONTOPROV");
	    		    	
	    	if(montoFactura == null || montoFactura.compareTo(new BigDecimal(0)) == 0)
	    	{
	    		aux = false;
	    		order.add(rs30.getString("POID"));
	    	}
	    	
	    }// end while
	    rs30.close();
	    pstmt30.close();
	    int i;
	    String todasOC = "";
	    
	    for (i=0; i<order.size(); i++)
	    {
	    	todasOC +=" "+ order.get(i);
	    }// end for
	    
	    Boolean seguir = false;
	    BigDecimal aux1 = new BigDecimal(0);
	    
	    // Pregunto si unos de los gastos para el calculo del Factor Def es cero
	    // Se quito imports.getXX_NACINVOICEAMOUNT().compareTo(aux1)== 0  de la validacion
	    if(imports.getXX_AARealAmount().compareTo(aux1)== 0 || imports.getXX_REALINSURANCEAMOUNT().compareTo(aux1)== 0 || 
	    	imports.getXX_InterFretRealAmount().compareTo(aux1)== 0 || imports.getXX_SENIATREALESTEEMEDAMOUNT().compareTo(aux1)== 0 
	    	|| imports.getXX_NACTREASREALAMOUNT().compareTo(aux1) == 0)
	    {
	    	seguir = ADialog.ask(1, new Container(), "XX_MessageFactor");
	    }
	    else
	    {
	    	seguir = true;
	    }
	    // Valido que el factor de cambio para las importaciones no este cero en alguna O/C
	    String SQL31 = ("SELECT A.DOCUMENTNO AS POID, A.XX_RealExchangeRate AS RATE " +
        		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
        		"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
        		"AND A.XX_ORDERTYPE = 'Importada' AND A.ISSOTRX = 'N' " +
        		"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' ");
        
        PreparedStatement pstmt31 = DB.prepareStatement(SQL31, null); 
	    ResultSet rs31 = pstmt31.executeQuery();
	    
	    Boolean aux2 = true;
	    
	    Vector <String> orderRate = new Vector <String>();
	    
	    while(rs31.next())
	    {
	    	if(rs31.getBigDecimal("RATE")== null || rs31.getBigDecimal("RATE").compareTo(new BigDecimal(0)) == 0)
	    	{
	    		aux2 = false;
	    		orderRate.add(rs31.getString("POID"));
	    	}
	    }// end while
	    rs31.close();
	    pstmt31.close();
	    
	    int y;
	    String todasOCRate = "";
	    
	    for (y=0; y<orderRate.size(); y++)
	    {
	    	todasOCRate +=" "+ orderRate.get(y);
	    }// end for
	    
	    
	 if(aux && seguir && aux2)   
	 {      
        //(AHORA)EL ID, EL FACTOR ESTIMADO Y EL DOCUMENNO DE LAS O/C ASOCIADAS A LA GUIA
        
        String SQL1 = ("SELECT A.C_ORDER_ID AS POID, A.DOCUMENTNO AS ORDERNNO, A.XX_ESTIMATEDFACTOR AS FACESTI, A.XX_VendorInvoiceAmount AS MONTOPROV, XX_RealExchangeRate AS C_RATE " +
        		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
        		"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
        		"AND A.XX_ORDERTYPE = 'Importada' AND A.ISSOTRX = 'N' AND A.XX_ORDERSTATUS <> 'SIT' " +
        		"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' ");
        
		PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
	    ResultSet rs1 = pstmt1.executeQuery();
	    
	    Vector <String> orderPENLA = new Vector <String>();
	    Vector <String> orderPENFac = new Vector <String>();
	    Vector <String> orderAP = new Vector <String>();
	    
	    while(rs1.next())
	    {
	    	factorEst = rs1.getBigDecimal("FACESTI");
	    	orderNum = rs1.getInt("ORDERNNO");
	    	orderID = rs1.getInt("POID");
	    	factorCambio = rs1.getBigDecimal("C_RATE");
	    	montoFactura = rs1.getBigDecimal("MONTOPROV");
	    	
	    	//Busco si todos los limites estan aprobados
	    	
			String SQL12 = ("SELECT COUNT (*) AS CONTEO FROM XX_VMR_PO_APPROVAL WHERE C_ORDER_ID = '"+orderID+"'"); 
			PreparedStatement pstmt12 = DB.prepareStatement(SQL12, null); 
		    ResultSet rs12 = pstmt12.executeQuery();

		    if(rs12.next())
		    {
		    	conteo = rs12.getInt("CONTEO");

		    	String SQL13 = ("SELECT COUNT (*) AS CONTEOY FROM XX_VMR_PO_APPROVAL WHERE C_ORDER_ID = '"+orderID+"' AND ISAPPROVED = 'Y'"); 
				PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null); 
			    ResultSet rs13 = pstmt13.executeQuery();
			    
			    if(rs13.next())
			    {
			    	conteoY = rs13.getInt("CONTEOY");
			    }
			    rs13.close();
			    pstmt13.close();
			    
		    }
		    rs12.close();
		    pstmt12.close();
	    	
		 // Busco si todos los producto esta en la Matrix (SI LAS REFERENCIAS TIENEN PRODUCTO ASOCIADO)
		   
		    
		    String SQL21 = ("SELECT COUNT (A.XX_VMR_PO_LINEREFPROV_ID) AS CONTEOLINE " +
		    		"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B " +
		    		"WHERE A.C_ORDER_ID = '"+orderID+"' " +
		    		"AND A.C_ORDER_ID = B.C_ORDER_ID AND B.ISSOTRX='N' ");
		    
		    PreparedStatement pstmt21 = DB.prepareStatement(SQL21, null); 
		    ResultSet rs21 = pstmt21.executeQuery();
		    
		    if(rs21.next())
		    {
		    	cantidaLine = rs21.getInt("CONTEOLINE");
		    	
		    	String SQL22 = ("SELECT COUNT (A.XX_VMR_PO_LINEREFPROV_ID) AS CONTEOREF " +
		    			"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B " +
		    			"WHERE A.C_ORDER_ID = '"+orderID+"' " +
		    			"AND  A.XX_ReferenceIsAssociated = 'Y' " +
		    			"AND A.C_ORDER_ID = B.C_ORDER_ID AND B.ISSOTRX='N' ");
		    	
		    	PreparedStatement pstmt22 = DB.prepareStatement(SQL22, null); 
			    ResultSet rs22 = pstmt22.executeQuery();
			    
			    if(rs22.next())
			    {
			    	cantidaRef = rs22.getInt("CONTEOREF");
			    }
			    rs22.close();
			    pstmt22.close();
		    	
		    }
		    rs21.close();
		    pstmt21.close();
		    
		    /////////////////////////////////////
	    	//Calculating Definitive Factor
	    	////////////////////////////////////
		    MOrder orden = new MOrder(getCtx(),orderID,get_Trx());
		    
			// Calculando la sumatoria de la factura de toda las O/C que estan el Guia
		    
		    String SQL23 = ("SELECT SUM (A.XX_VendorInvoiceAmount * XX_RealExchangeRate) AS SUMTOTALFAC " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B, C_CONVERSION_RATE C " +
		    		"WHERE A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
		    		"AND A.ISSOTRX='N' AND C.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_VLO_BOARDINGGUIDE_ID = '"+importId+"' ");
		    
		    PreparedStatement pstmt23 = DB.prepareStatement(SQL23, null); 
		    ResultSet rs23 = pstmt23.executeQuery();
		    
		    if(rs23.next())
		    {
		    	sumFac = rs23.getBigDecimal("SUMTOTALFAC");
		    }
		    rs23.close();
		    pstmt23.close();
		    
		    
		    montoFacturaBs = montoFactura.multiply(factorCambio);

			      
		    percentOC = (montoFacturaBs.divide(sumFac, 4, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
					
		    //////
			//Calculo de las Gastos!
		    //////
			
			// EN BG 			
			//Gastos para el calculo del factor
		    
		    //Monto total del agente de aduana
		    BigDecimal g1 = imports.getXX_AARealAmount();
		    //System.out.println(g1);
		    //Monto real de Seguro
		    BigDecimal g2 = imports.getXX_REALINSURANCEAMOUNT();
		    //System.out.println(g2);
		    //Monto de la factura del Agente de Carga
		    BigDecimal g3 = imports.getXX_InterFretRealAmount();
		    //System.out.println(g3);
		    //Monto del flete Nacional
		    //BigDecimal g4 = imports.getXX_NACINVOICEAMOUNT();
		    BigDecimal g4 = new BigDecimal(0);
		    //System.out.println(g4);
		    //Monto de tesoreria Nacional
		    BigDecimal g5 = imports.getXX_NACTREASREALAMOUNT();
		    //System.out.println(g5);
		    //Monto real SENIAT
		    BigDecimal g6 = imports.getXX_SENIATREALESTEEMEDAMOUNT();
		    //System.out.println(g6);
		    
			BigDecimal t1 = g1.add(g2);
			BigDecimal t2 = g3.add(g4);
			BigDecimal t3 = g5.add(g6);
			
			BigDecimal tot1 = t1.add(t2);
			
		//AGREGADO POR GHUCHET - Total Gastos usado para calcular el factor de incremento
			BigDecimal totalGastosInc = t3.add(tot1);
			BigDecimal factorPart1Inc = new BigDecimal(0);
			BigDecimal factorPart2Inc = new BigDecimal(0);
			BigDecimal factorDefInc = new BigDecimal(0);

			// % O/C por el Total de Gastos
			factorPart1Inc = (totalGastosInc.multiply(percentOC)).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
			//System.out.println("% O/C por el Total de Gastos "+factorPart1);
			
			// (% O/C por el Total de Gastos) mas la Factura en BS
			factorPart2Inc = factorPart1Inc.add(montoFacturaBs); 
			//System.out.println("(% O/C por el Total de Gastos) mas la Factura en BS "+factorPart2);
			factorDefInc = factorPart2Inc.divide(montoFactura, 4, RoundingMode.HALF_UP); 
			//System.out.println("FACTOR DEFINITIVO PARA INCREMENTO: "+factorDefInc);
		//FIN AGREGADO POR GHUCHET - Total Gastos usado para calcular el factor de incremento
			
			//Gasto comercial no incluye FEE ni Manejo de Mercancia
			BigDecimal totalGastoComercial= totalGastosInc;


	//AGREGADO POR GHUCHET - Total Gasto Comercial usado para calcular el factor definitivo Comercial
			
	// Calculando la sumatoria de la factura de toda las O/C que estan el Guia
			BigDecimal tasaComercial  = new BigDecimal(0);
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM");
          	// Calendar to Date Conversion
          	Date auxDate = new Date((year-1900), month, day);
          	String fecha=formatter.format(auxDate);
			
          	//Se busca el factor de reposición del periodo en curso y de la moneda correspondiente a la O/C
			String SQLTrade = "Select C.XX_REPLACEMENTFACTOR  AS TASACOMERCIAL"+
					"\nFROM C_CONVERSION_RATE C, C_PERIOD P WHERE C.XX_PERIOD_ID =  P.C_PERIOD_ID AND " +
					"\nP.ISACTIVE='Y' AND P.AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID()+" AND P.STARTDATE =  TO_DATE('"+fecha+"','YYYY-MM')" +
					"\n AND C_CURRENCY_ID = (SELECT C_CURRENCY_ID FROM C_ORDER WHERE C_ORDER_ID = "+orderID+" )";
		    
		    PreparedStatement pstmtTrade = DB.prepareStatement(SQLTrade, null); 
		    ResultSet rsTrade = pstmtTrade.executeQuery();
		    System.out.println(SQLTrade);
		    if(rsTrade.next())
		    {
		    	tasaComercial = rsTrade.getBigDecimal("TASACOMERCIAL");
		    }
		    rsTrade.close();
		    pstmtTrade.close();

			BigDecimal factorPart1Comercial = new BigDecimal(0);
			BigDecimal factorPart2Comercial = new BigDecimal(0);
			BigDecimal factorDefComercial = new BigDecimal(0);

		    BigDecimal montoComercial = montoFactura.multiply(tasaComercial);

			// % O/C por el Total de Gastos
			factorPart1Comercial = (totalGastoComercial.multiply(percentOC)).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
			//System.out.println("% O/C por el Total de Gastos "+factorPart1Comercial);
			
			// (% O/C por el Total de Gastos) mas la Factura en BS
			factorPart2Comercial = factorPart1Comercial.add(montoComercial); 
			//System.out.println("(% O/C por el Total de Gastos) mas la Factura en BS "+factorPart2Comercial);
			factorDefComercial = factorPart2Comercial.divide(montoFactura, 4, RoundingMode.HALF_UP); 
			//System.out.println("FACTOR DEFINITIVO PARA INCREMENTO: "+factorDefComercial);
	//FIN AGREGADO POR GHUCHET - Total Gasto Comercial usado para calcular el factor definitivo Comercial
			
			
			//AGREGADO POR GHUCHET - FEE
			BigDecimal totalFee = new BigDecimal(0);
			if(orden.getXX_ImportingCompany_ID() == Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID"))
				totalFee = getTotalFee(importId);
			
			//Al total gastos se le suma el total del Fee
			totalGastoComercial = totalGastoComercial.add(totalFee);
			imports.setXX_FeeRealAmount(totalFee);
			//FIN AGREGADO POR GHUCHET - FEE
			
			//SUMAR % del monto de las facturas (solo O/C CentroBeco) (Variable de Entorno/Compañia)
			BigDecimal percent = new BigDecimal(0);
			if(orden.getXX_ImportingCompany_ID() == Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID"))
				percent = new BigDecimal(Env.getCtx().getContext("#XX_L_VCN_STORAGECHARGE"));
			percent = sumFac.multiply(percent);
	
			//Total gastos
			BigDecimal totalGastos =  totalGastoComercial.add(percent);

			
			// % O/C por el Total de Gastos
			factorPart1 = (totalGastos.multiply(percentOC)).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
			//System.out.println("% O/C por el Total de Gastos "+factorPart1);
			
			// (% O/C por el Total de Gastos) mas la Factura en BS
			factorPart2 = factorPart1.add(montoFacturaBs); 
			//System.out.println("(% O/C por el Total de Gastos) mas la Factura en BS "+factorPart2);
			factorDef = factorPart2.divide(montoFactura, 4, RoundingMode.HALF_UP); 
			//System.out.println("FACTOR DEFINITIVO: "+factorDef);
		    
			// SETEO LOS VALORES CON QUE SE CALCULO EL FACTOR DEFINITIVO
			imports.setXX_CargoAgentInvoiceFac(imports.getXX_InterFretRealAmount());
			//imports.setXX_NacInvoiceFac(imports.getXX_NACINVOICEAMOUNT());
			imports.setXX_SeniatRealAmountFac(imports.getXX_SENIATREALESTEEMEDAMOUNT());
			imports.setXX_AARealAmountFac(imports.getXX_AARealAmount());
			imports.setXX_NacTreasRealFac(imports.getXX_NACTREASREALAMOUNT());
			imports.setXX_RealInsuranceAmountFac(imports.getXX_REALINSURANCEAMOUNT());
			imports.setXX_RealMerchManCost(percent);
			imports.setXX_FeeRealAmountFac(totalFee);
			
			
			if(factorDef.compareTo(factorEst) == 1)
			{
				//System.out.println("entro al if");
	    		BigDecimal val1 = new BigDecimal(100);
		    	val = factorDef.multiply(val1);
		    	val2 = val.divide(factorEst, 2, RoundingMode.HALF_UP);
		    	val3 = val2.subtract(val1);
		    	
		    	
		    	// val3 se va a guardar la diferencia en % entre los dos factores
		    	//System.out.println(val3+"%");
 /** obsoleto	
		    	if(val3.compareTo(new BigDecimal(5)) == 1)
		    	{
		    		// enviar correo al Comprador y Jefe de Categoría alertando que existe una O/C con esta condición.
		    		//System.out.println("ENVIA EL MAIL");
		    	
		    		X_XX_VMR_Category Categoria =  new X_XX_VMR_Category(getCtx(), orden.getXX_Category_ID(), get_Trx());
		    		MBPartner Comprador = new MBPartner(getCtx(), orden.getXX_UserBuyer_ID(), get_Trx()) ;
					MBPartner JefeCategoria = new MBPartner(getCtx(), Categoria.getXX_CategoryManager_ID(), get_Trx()) ;
					
					String Attachment = null;

					if(orden.get_ValueAsInt("XX_ImportingCompany_ID") == Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID")){
						
						EnviarCorreosDiferenciaFactor(Comprador.get_ID(), orderNum.toString(), Attachment);
						EnviarCorreosDiferenciaFactor(JefeCategoria.get_ID(), orderNum.toString(), Attachment);
						orden.setXX_OrderStatus("PEN");
						CorreoJefedePlanyComprador(orden);
						orderPENFac.add(orderNum.toString());
					}
					else{
						
						orden.setXX_OrderStatus("SIT");
						CorreoJefedePlanyCompradorSITME(orden);
					}    		    
					
					orden.setXX_DefinitiveFactor(factorDef);
					orden.setXX_InsertedStatusDate(XXExitDate);
					imports.setXX_ProcessedImport(true);
					orden.save();
					imports.save();
					get_Trx().commit();
					
					//Si el factor definitivo es distinto de 0 se actualiza el precio actual y el margen
					if(orden.getXX_DefinitiveFactor().compareTo(BigDecimal.ZERO)!=0)
					{
						modifyPriceActualMargin(orden.get_ID());
					}
					
					// Calculo del Increase Factor
					increaseFactor(orderID, factorDef);
					
					if(orden.getXX_DefinitiveFactor().compareTo(BigDecimal.ZERO)!=0)
					{
						try
						{
							closeAlert();
						}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
					}
					
					// Conversion SITME
					if(orden.get_ValueAsInt("XX_ImportingCompany_ID") != Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID")){
						convertSITME(orden);
					}
		    	}
*/
		    	if(conteo == conteoY && cantidaLine == cantidaRef && orden.get_ValueAsInt("XX_ImportingCompany_ID") == Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID"))
		    	//else if(conteo == conteoY)
		    	{
		    		// Apruebo la O/Cy mando los correo a los responsables
		    		OrdenAprobada (orderID, factorDef, XXExitDate, factorDefInc, factorDefComercial);
		    						
		 		    imports.setXX_ProcessedImport(true);
		 		    imports.save();
		 		    get_Trx().commit();
		 		    //ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POApprove", new String[] {orderNum.toString()}));
		 		   orderAP.add(orderNum.toString());
		 		    
		    	}
		    	else
		    	{	
		    		X_XX_VMR_Category Categoria =  new X_XX_VMR_Category(getCtx(), orden.getXX_Category_ID(), get_Trx());
		    		
		    		MBPartner Comprador = new MBPartner(getCtx(), orden.getXX_UserBuyer_ID(), get_Trx()) ;
					MBPartner JefeCategoria = new MBPartner(getCtx(), Categoria.getXX_CategoryManager_ID(), get_Trx()) ;
		    		
					String Attachment = null;
		    		
					//Verificamos SITME    
					if(orden.get_ValueAsInt("XX_ImportingCompany_ID") == Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID")){
						
						orden.setXX_OrderStatus("PEN");
						orderPENLA.add(orderNum.toString());
						CorreoJefedePlanyComprador(orden);
						EnviarCorreosRefProd(Comprador.get_ID(), orderNum.toString(), Attachment);
						EnviarCorreosRefProd(JefeCategoria.get_ID(), orderNum.toString(), Attachment);
					}
					else{
						
						orden.setXX_OrderStatus("SIT");
						CorreoJefedePlanyCompradorSITME(orden);
					}

					orden.setXX_DefinitiveFactor(factorDef);
					orden.setXX_DefinitiveTradeFactor(factorDefComercial);
					//orden.setXX_Void(true);
					orden.setXX_InsertedStatusDate(XXExitDate);
					imports.setXX_ProcessedImport(true);
		 		    imports.save();
					orden.save();
					get_Trx().commit();
					
					//Si el factor definitivo es distinto de 0 se actualiza el precio actual y el margen
					if(orden.getXX_DefinitiveFactor().compareTo(BigDecimal.ZERO)!=0)
					{
						modifyPriceActualMargin(orden.get_ID(), factorDefComercial);
					}
					
					// Calculo del Increase Factor
					increaseFactor(orderID, factorDefInc);
					
					if(orden.getXX_DefinitiveFactor().compareTo(BigDecimal.ZERO)!=0)
					{
						try
						{
							closeAlert();
						}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
					}
		
					//Conversion SITME
					if(orden.get_ValueAsInt("XX_ImportingCompany_ID") != Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID")){
						convertSITME(orden);
					}
		    	}
			}

			else if(conteo == conteoY && cantidaLine == cantidaRef)
			{
				// Apruebo O/C y mando los correo a los responsables
				OrdenAprobada (orderID, factorDef, XXExitDate, factorDefInc, factorDefComercial);
				//String Attachment = null;
	 		    //EnviarCorreosCambioStatusBPartner(orden.getC_BPartner_ID(),orden.getDocumentNo(), Attachment);
	 		    //EnviarCorreosCambioStatusImportadas(orden.getDocumentNo(), Attachment);
	 		    //EnviarCorreosCambioStatusCoor(orden.getDocumentNo(), Attachment);
	 		    //ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POApprove"));
				orden.setXX_Void(true);
			    imports.setXX_ProcessedImport(true);
	 		    imports.save();
	 		    get_Trx().commit();
	 		    //ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POApprove", new String[] {orderNum.toString()}));
	 		    orderAP.add(orderNum.toString());
	 		    
			}
			else
			{
				X_XX_VMR_Category Categoria =  new X_XX_VMR_Category(getCtx(), orden.getXX_Category_ID(), get_Trx());
				X_XX_VMR_Department Departamento = new X_XX_VMR_Department(getCtx(), orden.getXX_Department(), get_Trx());
	    		
	    		MBPartner Comprador = new MBPartner(getCtx(), orden.getXX_UserBuyer_ID(), get_Trx()) ;
				MBPartner JefeCategoria = new MBPartner(getCtx(), Categoria.getXX_CategoryManager_ID(), get_Trx()) ;
				MBPartner PlanificadorInventario = new MBPartner(getCtx(), Departamento.getXX_InventorySchedule_ID(), get_Trx());
	    		
				String Attachment = null;
				
				//Verificamos SITME    
				if(orden.get_ValueAsInt("XX_ImportingCompany_ID") == Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID")){
					
					orden.setXX_OrderStatus("PEN");
					CorreoJefedePlanyComprador(orden);
					EnviarCorreosRefProd(Comprador.get_ID(), orderNum.toString(), Attachment);
					EnviarCorreosRefProd(JefeCategoria.get_ID(), orderNum.toString(), Attachment);
					EnviarCorreosRefProd(PlanificadorInventario.get_ID(), orderNum.toString(), Attachment);
					orderPENLA.add(orderNum.toString());
				}
				else{
					
					orden.setXX_OrderStatus("SIT");
					CorreoJefedePlanyCompradorSITME(orden);
				}
				
				orden.setXX_DefinitiveFactor(factorDef);
				orden.setXX_DefinitiveTradeFactor(factorDefComercial);
				//orden.setXX_Void(true);
				orden.setXX_InsertedStatusDate(XXExitDate);
				imports.setXX_ProcessedImport(true);
	 		    imports.save();
				orden.save();
				get_Trx().commit();
				
				//Si el factor definitivo es distinto de 0 se actualiza el precio actual y el margen
				if(orden.getXX_DefinitiveFactor().compareTo(BigDecimal.ZERO)!=0)
				{
					modifyPriceActualMargin(orden.get_ID(), factorDefComercial);
				}
				
				// Calculo del Increase Factor
				increaseFactor(orderID, factorDefInc);
				
				if(orden.getXX_DefinitiveFactor().compareTo(BigDecimal.ZERO)!=0)
				{
					try
					{
						closeAlert();
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE,e.getMessage());
					}
				}
				
				//Conversion SITME
				if(orden.get_ValueAsInt("XX_ImportingCompany_ID") != Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID")){
					convertSITME(orden);
				}
			}
							    	
	    }// end while
	    rs1.close();
	    pstmt1.close();
	    
	    if(orderPENFac.size() != 0)
	    {
	    	int f;
	 	    String todasOCPENFac = "";
	 	    
	 	    for (f=0; f<orderPENFac.size(); f++)
	 	    {
	 	    	todasOCPENFac +=" "+ orderPENFac.get(f);
	 	    		 	    	
	 	    }// end for
	 	    
	 	   ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_OCPendienteFac", new String[] {todasOCPENFac}));
  
	    }
	    
	    if(orderPENLA.size() != 0)
	    {
	    	int f;
	 	    String todasOCPENLA = "";
	 	    
	 	    for (f=0; f<orderPENLA.size(); f++)
	 	    {
	 	    	todasOCPENLA +=" "+ orderPENLA.get(f);
	 	    }// end for
	 	    
	 	   ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_OCPendiente", new String[] {todasOCPENLA}));
	 	    
	    }
	    
	    if(orderAP.size() != 0)
	    {
	    	int f;
	 	    String todasOCAP = "";
	 	    
	 	    for (f=0; f<orderAP.size(); f++)
	 	    {
	 	    	todasOCAP +=" "+ orderAP.get(f);
	 	    }// end for
	 	    
	 	   ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POApprove", new String[] {todasOCAP}));
	    }
	    
	    if(orderSITME_I.size() != 0)
	    {
	 	    String allSITME_IOrders = "";
	 	    String allSITME_NOrders = "";
	 	    
	 	    for (int f=0; f<orderSITME_I.size(); f++)
	 	    {
	 	    	allSITME_IOrders +=" "+ orderSITME_I.get(f);
	 	    }// end for
	 	    
	 	    for (int f=0; f<orderSITME_N.size(); f++)
	 	    {
	 	    	allSITME_NOrders +=" "+ orderSITME_N.get(f);
	 	    }// end for
	 	    
	 	   ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_POSitme", new String[] {allSITME_IOrders,allSITME_NOrders}));
	    }
	    
	 }// end if boolean
	 else
	 {
		 //mensaje
		 if (seguir)
		 {	 
			 if(aux == false)
			 
				ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_ProcessGuide", new String[] {todasOC}));
		
			 if(aux2 == false)
				 ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_ProcessGuideRate", new String[] {todasOCRate}));
		 }
		 return "Proceso No Realizado";
	 }
	 
	 	// Agregado por VLOMONACO
	 	// Esta parte actualiza el margen de la cabecera de la orden con el factor definitivo
	 	
	 	// Primero buscamos las ordenes de la guia de embarque
	 	String sqlV1 = "select c_order_id from XX_VLO_DetailGuide where  XX_VLO_BoardingGuide_ID=" + imports.getXX_VLO_BoardingGuide_ID();

	 	
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
		 try{
				
				pstmt = DB.prepareStatement(sqlV1, null); 
			    rs = pstmt.executeQuery();
					 
			    while(rs.next())
			    {
			    	MOrder orden = new MOrder(getCtx(),rs.getInt(1),get_Trx());
			 
					if(orden.getXX_DefinitiveFactor()!= new BigDecimal(0))
					{
					 	BigDecimal costoBs = orden.getTotalLines().multiply(orden.getXX_DefinitiveFactor());
						
					 	String sql2 = "UPDATE C_Order po"											//////////////////////
								+ " SET XX_TotalPVPPlusTax = (select SUM(CASE WHEN a.SALEQTY <> 0 THEN (XX_SALEPRICEPLUSTAX*a.SALEQTY) ELSE (XX_SALEPRICEPLUSTAX*a.XX_LINEQTY) END) from XX_VMR_PO_LineRefProv a " +
																	 "where a.C_ORDER_ID = po.C_ORDER_ID), " +
								  " TotalPVP = (select SUM(CASE WHEN b.SALEQTY <> 0 THEN (XX_SALEPRICE*b.SALEQTY) ELSE (XX_SALEPRICE*b.XX_LINEQTY) END) from XX_VMR_PO_LineRefProv b " +
																	 " where b.C_ORDER_ID = po.C_ORDER_ID), " +
								" XX_TotalCostBs = "+ costoBs +", XX_EstimatedMargin ="											//////////////////////
									+ "round((((select SUM(CASE WHEN c.SALEQTY <> 0 THEN (XX_SALEPRICE*c.SALEQTY) ELSE (XX_SALEPRICE*c.XX_LINEQTY) END) from XX_VMR_PO_LineRefProv c " +
																	 " where c.C_ORDER_ID = po.C_ORDER_ID)-"+costoBs+")/" +
										"(select SUM(CASE WHEN d.SALEQTY <> 0 THEN (XX_SALEPRICE*d.SALEQTY) ELSE (XX_SALEPRICE*d.XX_LINEQTY) END) from XX_VMR_PO_LineRefProv d " +
																	 " where d.C_ORDER_ID = po.C_ORDER_ID))*100,2)"
								+ " WHERE po.ISSOTRX='N' AND po.C_Order_ID=" + orden.getC_Order_ID();						//////////////////////
							System.out.println(sql2);
							DB.executeUpdate(get_TrxName(), sql2 );
						
						try {
							DB.commit(true, get_TrxName());
						} catch (SQLException e){
							System.out.println("Error al actualizar el margen en la cabecera de orden de compra " + e);
						} 
					}
			    	
			    }
				    
			 }catch (Exception e) {
				 log.log(Level.SEVERE, sqlV1);
			 } finally
				{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}

	 	
		// fin VLOMONACO
		
		this.proratedInvoiceAA(imports.getXX_AARealAmountFac(), imports.getXX_VLO_BoardingGuide_ID());
		this.proratedInsurance(imports.getXX_RealInsuranceAmountFac(), imports.getXX_VLO_BoardingGuide_ID());
		this.proratedInvoiceInt(imports.getXX_CargoAgentInvoiceFac(), imports.getXX_VLO_BoardingGuide_ID());
		//this.proratedInvoiceNac(imports.getXX_NacInvoiceFac(), imports.getXX_VLO_BoardingGuide_ID());
		this.proratedAmountTreasury(imports.getXX_NacTreasRealFac(), imports.getXX_VLO_BoardingGuide_ID());
		this.proratedAmountSeniat(imports.getXX_SeniatRealAmountFac(), imports.getXX_VLO_BoardingGuide_ID());
		return "Proceso Realizado";
	}

	/**Calcula la suma del gasto FEE de las O/C de la guía de embarque
	 * @author ghuchet*/
	private BigDecimal getTotalFee(Integer importId ) {
		
		BigDecimal result = new BigDecimal(0);
		BigDecimal percentStorage =  new BigDecimal(Env.getCtx().getContext("#XX_L_VCN_STORAGECHARGE"));
		// Calculando la sumatoria de FEE de toda las O/C que estan el Guia
	    
	    String sql = "SELECT SUM(O.XX_VENDORINVOICEAMOUNT * O.XX_RealExchangeRate * NVL(T.XX_FEEPERCENTAGE,0)/100 * NVL("+percentStorage+",0)) AS SUMTOTALFEE " +
	    		"FROM C_ORDER O " +
	    		"JOIN  XX_VLO_BOARDINGGUIDE B ON (O.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID) " +
	    		"JOIN XX_VMR_TRADEREPRESENTATIVE T ON (T.XX_VMR_TRADEREPRESENTATIVE_ID = O.XX_VMR_TRADEREPRESENTATIVE_ID) " +
	    		"WHERE O.ISSOTRX='N' " +
	    		"AND O.XX_VLO_BOARDINGGUIDE_ID = "+importId;
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, null); 
	    ResultSet rs = null;
	    try {
	    	rs = pstmt.executeQuery();
	    	if(rs.next()){
	  	    	result = rs.getBigDecimal("SUMTOTALFEE");
	  	    }
	    }catch (Exception e) {
			 log.log(Level.SEVERE, sql);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	   if(result == null){
		   return new BigDecimal(0);
	   }
		return result;
	}

	@Override
	protected void prepare() {
		
	}
	
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner;
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			} finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		
		return AD_User_ID;
	}
	
	private boolean EnviarCorreosDiferenciaFactor(Integer BPartner, String orderID, String Attachment) {
		
		String sql = "SELECT u.AD_User_ID, u.name "
				   + "FROM AD_User u "
				   + "where IsActive = 'Y' and "
				   + "C_BPARTNER_ID = " + BPartner;

		PreparedStatement pstmt =null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Integer UserAuxID = rs.getInt("AD_User_ID");
				
				//Utilities f = new Utilities(getCtx(), get_TrxName(),1000005, orderID, -1, 1019155 ,-1, UserAuxID, null);
				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_DIFFERENCEFACTOR_ID"), orderID, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
				f.ejecutarMail(); 
				f = null;
			}
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}
	
	private boolean EnviarCorreosRefProd(Integer BPartner, String orderID, String Attachment) {
		
		String sql = "SELECT u.AD_User_ID, u.name "
				   + "FROM AD_User u "
				   + "where IsActive = 'Y' and "
				   + "C_BPARTNER_ID = " + BPartner;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Integer UserAuxID = rs.getInt("AD_User_ID");
				
				//Utilities f = new Utilities(getCtx(), get_TrxName(),1000005, orderID, -1, 1019155 ,-1, UserAuxID, null);
				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_PRODUCTREFERENCE_ID"), orderID, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
				f.ejecutarMail();
				f = null;
			}
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}
	
	private boolean OrdenAprobada (Integer orderID, BigDecimal factorDef, Timestamp XXExitDate, BigDecimal factorDefInc, BigDecimal XX_DefTradeFactor)
	{
		
		BigDecimal priceListPO = new BigDecimal(1);
		MOrder orden = new MOrder(getCtx(),orderID,get_Trx());
		
		String SQL2 = ("UPDATE C_ORDER SET XX_ORDERSTATUS= 'AP' WHERE ISSOTRX='N' AND C_ORDER_ID = '"+orderID+"'");
		
		PreparedStatement pstmt4 = null; 
	    ResultSet rs4 = null;
		try
		{
		    DB.executeUpdate(get_Trx(),SQL2);
		    get_Trx().commit();
		    
		    // genero el PO_line para las O/C internacionales
		    String SQL4 = ("SELECT DISTINCT A.XX_VMR_PO_LINEREFPROV_ID AS IDLINE, B.C_BPARTNER_ID AS PROV, TO_NUMBER (TO_CHAR (B.DATEPROMISED, 'YYYYMMDD')) AS DATEPRO, TO_NUMBER (TO_CHAR (B.DATEORDERED, 'YYYYMMDD')) AS DATEOR, " +
		    		"C.M_PRODUCT AS PRODUCT, A.QTY AS CANTIDAD, A.XX_VMR_UNITPURCHASE_ID AS UOM, " +
		    		"A.XX_COSTWITHDISCOUNTS AS PRECIO, " +
		    		"( SELECT C_TAX_ID FROM C_Tax WHERE ValidFrom = (SELECT MAX(ValidFrom) FROM C_Tax WHERE C_TaxCategory_ID = A.C_TAXcategory_ID)) as TAX, C.XX_QUANTITYC AS CC " +
		    		"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B, XX_VMR_REFERENCEMATRIX C " +
		    		"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
		    		"AND C.XX_QUANTITYV <> 0 AND B.ISSOTRX='N' " +
		    		"AND C.XX_VMR_PO_LINEREFPROV_ID = A.XX_VMR_PO_LINEREFPROV_ID " +
		    		"AND C.M_PRODUCT IS NOT NULL " +
		    		"AND B.C_ORDER_ID = '"+orderID+"'");
		    
		    pstmt4 = DB.prepareStatement(SQL4, null); 
		    rs4 = pstmt4.executeQuery();
		   
		    while(rs4.next())
		    {
		    	
		    	MOrderLine orderLine = new MOrderLine(getCtx(), 0,get_Trx());
		    	
		    	BigDecimal PriceEntered, PriceActual, LineAmount;
		    	
		    	PriceEntered = MUOMConversion.convertProductFrom(getCtx(), rs4.getInt("PRODUCT"), 100, rs4.getBigDecimal("PRECIO"));
		    	
		    	if(PriceEntered != null)
		    	{
		    		orderLine.setPriceEntered(PriceEntered);
			       	PriceActual = PriceEntered;
			       	log.fine("PriceEntered=" + PriceEntered 
							+ " -> PriceActual=" + PriceActual);
			       	orderLine.setPriceActual(PriceActual);
			       	LineAmount = rs4.getBigDecimal("CANTIDAD").multiply(PriceActual); 
			       	orderLine.setLineNetAmt(LineAmount);
		    		
		    	}
		      	else
		       	{
		       		return false;
		       	}
		    	
		    	//orderLine.setAD_Client_ID(1000005);
		    	orderLine.setAD_Org_ID(orden.getAD_Org_ID());
		    	orderLine.setC_Order_ID(orderID);
		       	orderLine.setXX_VMR_PO_LineRefProv_ID(rs4.getInt("IDLINE"));
		       	orderLine.setC_BPartner_ID(rs4.getInt("PROV"));
		       	orderLine.setM_Product_ID(rs4.getInt("PRODUCT"));
		       	orderLine.setQtyEntered(rs4.getBigDecimal("CC"));
		       	orderLine.setC_UOM_ID(100);
		       	orderLine.setC_Tax_ID(rs4.getInt("TAX"));
		       	orderLine.setPriceList(priceListPO);
		       	orderLine.save();
		       	get_Trx().commit();
		       	
		    } // end while PO_Line
		    
		    orden.setDocAction(X_C_Order.DOCACTION_Prepare);
		    
		    if (!DocumentEngine.processIt(orden, X_C_Order.DOCACTION_Prepare)) {
		    	rollback();
		    	ADialog.error(1, new Container(), "XX_InvalidPurchaseOrder");
		    	return false;
		    } 
		    	    
		    orden.setDocAction(X_C_Order.DOCACTION_Complete);
		    DocumentEngine.processIt(orden, X_C_Order.DOCACTION_Complete);
		    orden.setXX_DefinitiveFactor(factorDef);
			orden.setXX_DefinitiveTradeFactor(XX_DefTradeFactor);
		    //orden.setXX_ApprovalDate(yearActual);
		    orden.setXX_ApprovalDate(XXExitDate);
		    orden.save();
		    get_Trx().commit();
		    //Si el factor definitivo es distinto de 0 se actualiza el precio actual y el margen
			if(orden.getXX_DefinitiveFactor()!=new BigDecimal(0))
			{
				modifyPriceActualMargin(orden.get_ID(), XX_DefTradeFactor);
			}
		    
		    // Calculo del Increase Factor
			increaseFactor(orderID, factorDefInc);
		    
		    if(orden.getXX_DefinitiveFactor()!= new BigDecimal(0))
			{
		    	try
		    	{
		    		closeAlert();
		    	}
				catch(Exception e)
				{
					log.log(Level.SEVERE,e.getMessage());
				}
			}
		    
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally{
			DB.closeResultSet(rs4);
			DB.closeStatement(pstmt4);
		} 
		
		return true;
	}
	
	private boolean increaseFactor(Integer orderID, BigDecimal factorDefInc)
	{
		Boolean a = true;
		BigDecimal exchangeRate, facIncreConver, factorNew;
		BigDecimal facIncreConver2, factorNew2, factorDef2;
		Integer department, country, dispRoute;
		X_XX_VMR_IncreaseFactor  increaseFactObj = null;
		
		String SQL = ("SELECT  XX_DefinitiveFactor AS FACTORDEF, XX_VMR_DEPARTMENT_ID AS DEP, " +
				"C_Country_ID AS COUNTRY, XX_VLO_DispatchRoute_ID AS ROUTE, XX_REALEXCHANGERATE AS RATE " +
				"FROM C_ORDER " +
				"WHERE ISSOTRX='N' AND C_ORDER_ID = '"+orderID+"' ");
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
		PreparedStatement pstmt0 = null; 
	    ResultSet rs0 = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
		    
		    if(rs.next())
		    {

		    	exchangeRate = rs.getBigDecimal("RATE");
		    	department = rs.getInt("DEP");
		    	country = rs.getInt("COUNTRY");
		    	dispRoute = rs.getInt("ROUTE");
		    	factorDef2 =  rs.getBigDecimal("FACTORDEF");
		    
		    	String SQL0 = "SELECT XX_VMR_INCREASEFACTOR_ID AS id, XX_INCREASEFACTOR AS factor " 
					+ "FROM XX_VMR_INCREASEFACTOR WHERE C_COUNTRY_ID = " + country
					+ " AND XX_VLO_DISPATCHROUTE_ID = "	+ dispRoute
					+ " AND XX_VMR_DEPARTMENT_ID = " + department;

				//System.out.println(SQL0);
				
				pstmt0 = DB.prepareStatement(SQL0, null); 
			    rs0 = pstmt0.executeQuery();
			    
			    if(rs0.next())
			    {
			    	Integer factorID = rs0.getInt("id");
					BigDecimal factorOld = rs0.getBigDecimal("factor");		
			    	increaseFactObj =  new X_XX_VMR_IncreaseFactor(getCtx(), factorID, get_Trx());
					    		
			    	factorNew = factorDefInc.divide(exchangeRate, 2, RoundingMode.HALF_UP);
			    	factorNew = factorNew.subtract(new BigDecimal(1));
			    		
			    	facIncreConver = factorOld.add(factorNew);
			    	facIncreConver = facIncreConver.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);

			    	/* GHUCHET - ELIMINAR LUEGO DE PROBAR*/
		    		
			    	factorNew2 = factorDef2.divide(exchangeRate, 2, RoundingMode.HALF_UP);
			    	factorNew2 = factorNew2.subtract(new BigDecimal(1));
			    		
			    	facIncreConver2 = factorOld.add(factorNew2);
			    	facIncreConver2 = facIncreConver2.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
			    	//System.out.println("factor incremento SIN % manejo de mercancia: "+facIncreConver);
			    	//System.out.println("factor incremento CON % manejo de mercancia: "+facIncreConver2);
			    	/*GHUCHET - HASTA AQUI ELIMINAR LUEGO DE PROBAR*/
			    }
			    else
			    {
			    	increaseFactObj =  new X_XX_VMR_IncreaseFactor(getCtx(), 0, get_Trx());
			    	
			    	facIncreConver = factorDefInc.divide(exchangeRate, 2, RoundingMode.HALF_UP);
		    		facIncreConver = facIncreConver.subtract(new BigDecimal(1));
		    		
		    		increaseFactObj.setXX_VMR_Department_ID(department);
		    		increaseFactObj.setC_Country_ID(country);
		    		increaseFactObj.setXX_VLO_DispatchRoute_ID(dispRoute);
		    		
		    		/* GHUCHET - ELIMINAR LUEGO DE PROBAR*/
		    		
			    	facIncreConver2 = factorDef2.divide(exchangeRate, 2, RoundingMode.HALF_UP);
		    		facIncreConver2 = facIncreConver2.subtract(new BigDecimal(1));
			    	
			    	//System.out.println("factor incremento SIN % manejo de mercancia: "+facIncreConver);
			    	//System.out.println("factor incremento CON % manejo de mercancia: "+facIncreConver2);
			    	/*GHUCHET - HASTA AQUI ELIMINAR LUEGO DE PROBAR*/
			    }
				
			    increaseFactObj.setXX_IncreaseFactor(facIncreConver);
			    increaseFactObj.save();
			    get_Trx().commit();
		    }
		}
		catch (Exception e) {
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs0);
			DB.closeStatement(pstmt0);
		} 
		
		return a;
	}
	
	private boolean CorreoJefedePlanyComprador( MOrder orden) {
	
	    // Inicio Victor Lo Monaco
	    // Esta parte manda correo al comprador, planificador y jefe de planificacion
	    // cuando pasa a estado pendiente
	    
	    // Correo al jefe de planificacion
		String sql = "SELECT AD_USER_ID FROM AD_User_Roles WHERE AD_ROLE_ID=(SELECT AD_Role_ID " +
		"FROM AD_Role " +
		"WHERE name = 'BECO Jefe de Planificacion') and IsActive='Y'";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				// A partir de aca enviamos el correo
				Integer UserAuxID = rs.getInt("AD_USER_ID");
				// Este es el mensaje que se le enviara al Jefe de planificacion

				//Proveedor y Departamento:
				X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), get_Trx());
				X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), get_Trx());
				MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), get_Trx());
				MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), get_Trx());
				String Mensaje = Msg.getMsg( getCtx(), "XX_OrderInPendingStatus", new String[]{orden.getDocumentNo(),vendor.getName(),dep.getValue(), collection.getName()});
				
				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_OVERDRAWNORDER_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
				f.ejecutarMail(); 
				f = null; 

			}
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// correo al comprador
		int UserAuxID = orden.getXX_UserBuyer_ID();
		//Proveedor y Departamento:
		X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), get_Trx());
		X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), get_Trx());
		MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), get_Trx());
		MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), get_Trx());
		String Mensaje = Msg.getMsg( getCtx(), "XX_OrderInPendingStatus", new String[]{orden.getDocumentNo(),vendor.getName(),dep.getValue(), collection.getName()});
		try {
			Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_OVERDRAWNORDER_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, getAD_User_ID(UserAuxID), null);
			f.ejecutarMail(); 
			f = null; 	
		} catch (Exception e)
		{
			System.out.println("Error al enviar correo al comprador " + e);
		}
			    
	    // Fin Victor Lo Monaco
		
		return true;
	}
	
	private boolean CorreoJefedePlanyCompradorSITME( MOrder order) {
		
	    // Inicio Victor Lo Monaco
	    // Esta parte manda correo al comprador, planificador y jefe de planificacion
	    // cuando pasa a estado pendiente
	    
	    // Correo al jefe de planificacion
		String sql = "SELECT AD_USER_ID FROM AD_User_Roles WHERE AD_ROLE_ID=(SELECT AD_Role_ID " +
		"FROM AD_Role " +
		"WHERE name = 'BECO Jefe de Planificacion') and IsActive='Y'";

		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				// A partir de aca enviamos el correo
				Integer UserAuxID = rs.getInt("AD_USER_ID");
				// Este es el mensaje que se le enviara al Jefe de planificacion

				//Proveedor y Departamento:
				X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), get_Trx());
				X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), get_Trx());
				MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), get_Trx());
				MVMRDepartment dep = new MVMRDepartment( getCtx(), order.getXX_VMR_DEPARTMENT_ID(), get_Trx());
				String Mensaje = Msg.getMsg( getCtx(), "XX_OrderInSITMEStatus", new String[]{order.getDocumentNo(),vendor.getName(),dep.getValue(), collection.getName()});
				
				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
				f.ejecutarMail(); 
				f = null; 

			}
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		} 
		
		// correo al comprador
		int UserAuxID = order.getXX_UserBuyer_ID();
		//Proveedor y Departamento:
		X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), get_Trx());
		X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), get_Trx());
		MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), get_Trx());
		MVMRDepartment dep = new MVMRDepartment( getCtx(), order.getXX_VMR_DEPARTMENT_ID(), get_Trx());
		String Mensaje = Msg.getMsg( getCtx(), "XX_OrderInSITMEStatus", new String[]{order.getDocumentNo(),vendor.getName(),dep.getValue(), collection.getName()});
		try {
			Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, getAD_User_ID(UserAuxID), null);
			f.ejecutarMail(); 
			f = null; 	
		} catch (Exception e)
		{
			System.out.println("Error al enviar correo al comprador " + e);
		}
			    
	    // Fin Victor Lo Monaco
		
		return true;
	}
	// AGREGADO GHUCHET
	/** Envía correo a Comprador de la O/C SITME y a los jefes de categoría indicando que se genero una O/C Nacional correspondiente*/
	private void sendMailSitmeToNational(int orderID, int orderSITME_ID) {
		
		try {
			MOrder order = new MOrder(getCtx(), orderID, null);
			MOrder orderSITME = new MOrder(getCtx(), orderSITME_ID, null);
			
			int buyerID = getAD_User_ID(order.getXX_UserBuyer_ID());
			X_AD_User buyer = new X_AD_User( Env.getCtx(), buyerID, null);
			
			MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
			MVMRCategory catg = new MVMRCategory(getCtx(),dep.getXX_VMR_Category_ID(), null);
			
			int catgManagerID = getAD_User_ID(catg.getXX_CategoryManager_ID());
			X_AD_User catgManager = new X_AD_User( Env.getCtx(), catgManagerID, null);
			
			String emailTo = buyer.getEMail();
			String emailTo2 = catgManager.getEMail();
			if(emailTo.contains("@")){
				MClient m_client = MClient.get(Env.getCtx());
				String subject = " Generada O/C Nacional Nro "+order.getValue();
				MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
				MBPartner vendorSITME = new MBPartner(getCtx(), orderSITME.getC_BPartner_ID(), null);
				String msg = "Se ha generado la O/C Nacional -PROFORMA- Nro "+order.getDocumentNo()+" del Proveedor "+vendor.getValue()+"-"+vendor.getName()+
						" que sustituye a la O/C Importada "+orderSITME.getDocumentNo()+" del Proveedor "+vendorSITME.getValue()+"-"+vendorSITME.getName();
				msg +="\n\n Favor proceder a realizar la asociación de referencias.";
	
				EMail email = m_client.createEMail(null, emailTo, "Comprador", subject, msg);
						
				if (email != null){		
					if(emailTo2.contains("@")){
						email.addTo(emailTo2, "Jefe de Categoría");
					}
					String status = email.send();
					log.info("Email Send status: " + status);
					if (email.isSentOK()){}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}// FIN GHUCHET
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Relación de costos de importaciones
	 * @author aavila
	 * @author ccapote
	 * */
	public String proratedInvoiceAA (BigDecimal montoCA, int guiaNroID)
	{
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		try
		{
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mfaaP = new BigDecimal(0);
			Integer orderId = 0;
			
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
		    		"AND A.ISSOTRX='N' AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
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
		    		"AND A.ISSOTRX='N' AND A.XX_ORDERTYPE = 'Importada' " +
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
			    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
			    		"AND A.XX_VLO_BOARDINGGUIDE_ID = E.XX_VLO_BOARDINGGUIDE_ID " +
			    		"AND A.C_ORDER_ID = '"+orderId+"' " +
			    		"AND E.XX_VLO_BOARDINGGUIDE_ID = '"+guiaNroID+"' ");
			    
			    pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    if(rs3.next())
			    {
			    	cant = rs3.getBigDecimal("TOTAL");
			    	//System.out.println(cant);
			    }
			    DB.closeResultSet(rs3);
			    DB.closeStatement(pstmt3);
			    
			    // %O/C
			    cantTotal = cant.divide(cantT, 2, RoundingMode.HALF_UP);
			    //System.out.println(cantTotal);
			    mfaaP = montoCA.multiply(cantTotal);
			    //System.out.println(mfaaP);
			    
			    MOrder orden = new MOrder(getCtx(), orderId, get_Trx());
			    if (mfaaP.intValue()!=0){
			    	orden.setXX_CustomAgentAmountProEst(mfaaP);
			    	orden.save();
			    } else {
			    	orden.setXX_CustomAgentAmountProEst(new BigDecimal(0));
			    	orden.save();
			    }
		    	get_Trx().commit();
		    }// end while
		    
			return "";
		}// end try
		catch (Exception e) {
			return "";
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
		} 
		
	}// end
	
	public String proratedInsurance (BigDecimal montoCA, Integer guiaNroID)
	{
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		try
		{
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal masrP = new BigDecimal(0);
			Integer orderId = 0;

			
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
			    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
			    
			    
			    MOrder orden = new MOrder(getCtx(), orderId, get_Trx());
			    if(masrP.intValue()!=0){
				    orden.setXX_InsuranceAmProEst(masrP);
				    orden.save();
				}else {
			    	orden.setXX_InsuranceAmProEst(new BigDecimal(0));
			    	orden.save();
			    }
		    	get_Trx().commit();
		    	
		    }//end while

		
			return "";
		}//end try
		catch (Exception e) {
			return "";
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		} 
		
	}//end
	
	public String proratedInvoiceInt (BigDecimal montoCA, Integer guiaNroID)
	{
		
	    PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    PreparedStatement pstmt2 = null; 
	    ResultSet rs2 = null;
	    PreparedStatement pstmt3 = null; 
	    ResultSet rs3 = null;
		try
		{
			
			BigDecimal cantT = new BigDecimal(0);
			BigDecimal cant = new BigDecimal(0);
			BigDecimal cantTotal = new BigDecimal(0);
			BigDecimal mffiP = new BigDecimal(0);
			Integer orderId = 0;
			
			//GridField campoAmountT =  mTab.getField("XX_InterFretRealAmount");  
		    //BigDecimal montoCA = (BigDecimal)campoAmountT.getValue();
		    
		    //GridField campoGuia =  mTab.getField("XX_TRANSPORTDOCUMENTNUMBER");  
		    //String guiaNro = (String)campoGuia.getValue();
		    
		    //GridField campoid =  mTab.getField("XX_VLO_BOARDINGGUIDE_ID");  
		    //Integer guiaNroID = (Integer)campoid.getValue();
		    
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
			    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
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

			    mffiP = montoCA.multiply(cantTotal);

			    
			    MOrder orden = new MOrder(getCtx(), orderId, get_Trx());
			    if (mffiP.intValue()!=0){
				    orden.setXX_InterFretAmountProEst(mffiP);
				    orden.save();
			    }else {
			    	orden.setXX_InterFretAmountProEst(new BigDecimal(0));
			    	orden.save();
			    }
			    get_Trx().commit();
		    }//end while

		    
			
			return "";
		}// end try
		
		catch (Exception e) {
			return "";
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		} 
	}// end
	
	public String proratedInvoiceNac (BigDecimal montoCA, Integer guiaNroID)
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
			
   
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE A.ISSOTRX='N'AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
			    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
	
			    MOrder orden = new MOrder(getCtx(), orderId, get_Trx());
			    if (mffnP.intValue()!=0){
				    orden.setXX_NacInvoiceAmountProEst(mffnP);
				    orden.save();
			    }else {
			    	orden.setXX_NacInvoiceAmountProEst(new BigDecimal(0));
			    	orden.save();
			    }
		    	get_Trx().commit();
		    }//end while

	
			return "";
		}// end try
		
		catch (Exception e) {
			return "";
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		}
		
	}// end
	
	public String proratedAmountTreasury (BigDecimal montoCA, Integer guiaNroID)
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
			
		    
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
			    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
			    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
			    
			    mrtnP = montoCA.multiply(cantTotal);
			    
			    MOrder orden = new MOrder(getCtx(), orderId, get_Trx());
			    if(mrtnP.intValue()!=0){
				    orden.setXX_NatTreasAmProEst(mrtnP);
				    orden.save();
			    }else {
			    	orden.setXX_NatTreasAmProEst(new BigDecimal(0));
			    	orden.save();
			    }
		    	get_Trx().commit();
		    }// end while
			
			return "";
			
		} // end try
		
		catch (Exception e) {
			return "";
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		}
	
		
	}
	
	public String proratedAmountSeniat (BigDecimal montoCA, Integer guiaNroID)
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
			
			
		    String SQL = ("SELECT SUM (A.TOTALLINES * F.MULTIPLYRATE) AS TOTALOR " +
		    		"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE E, C_CONVERSION_RATE F " +
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
		    		"WHERE A.ISSOTRX='N' AND F.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
		    		"AND A.XX_ORDERTYPE = 'Importada' " +
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
			    		"AND A.ISSOTRX='N' AND A.XX_ORDERTYPE = 'Importada' " +
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

			    
			    MOrder orden = new MOrder(getCtx(), orderId, get_Trx());
			    if(mrsP.intValue()!=0){
				    orden.setXX_SenEstAmProEst(mrsP);
				    orden.save();
			    }else {
			    	orden.setXX_SenEstAmProEst(new BigDecimal(0));
			    	orden.save();
			    }
			    get_Trx().commit();
		    }// end while
			return "";
			
		}// end try
		catch (Exception e) {
			return "";
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs3);
			DB.closeStatement(pstmt3);
		}
		
		
	} // end
	
	//Realizado por Rosmaira Arvelo
	public void closeAlert()
	{
		MVLOBoardingGuide boarding = new MVLOBoardingGuide(Env.getCtx(),getRecord_ID(),get_Trx());
		MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(getRecord_ID()),get_Trx());			
			
		//Llama al proceso cerrar alerta
		if ((boarding.isXX_Alert()==true)&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("CF")))
		{					
			MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task.get_ID()); 
			mpi.save();
			get_Trx().commit();
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
			pi.setClassName(""); 
			pi.setTitle(""); 
				
			ProcessCtl pc = new ProcessCtl(null ,pi,null); 
			pc.start();
		}		
	}//fin closeAlert
	
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
		}  finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return criticalTask;
	}//fin getCriticalTaskForClose 
	
	/*
	 *  Actualiza el precio actual y el margen de los POLineRefProv segun la orden de compra y costo comercial 
	 */
	private void modifyPriceActualMargin(Integer orderID, BigDecimal XX_DefTradeFactor)
	{		
		BigDecimal costoMonedaNac = new BigDecimal(0);
		BigDecimal salePrice = new BigDecimal(0);
		BigDecimal tradeCost = new BigDecimal(0);
		
		MOrder order = new MOrder(Env.getCtx(),orderID,get_Trx());
		
		String SQL = "SELECT (po.XX_CostWithDiscounts*co.XX_DefinitiveFactor) AS CostoNacional, " +
				" co.XX_DefinitiveFactor, po.XX_VMR_PO_LineRefProv_ID, po.XX_SalePrice," +
				"XX_CostWithDiscounts, XX_PiecesBySale_ID, XX_VMR_UnitConversion_ID "
				   + "FROM XX_VMR_PO_LineRefProv po, C_Order co "
				   + "WHERE co.ISSOTRX='N' and po.C_Order_ID=co.C_Order_ID " +
				   "AND co.DocumentNo='"+order.getDocumentNo()+"'";		
		
		//Agregado por Javier Pino -- Actualiza los precios actuales y margenes de la distribución
		String sqlDistribucion = "SELECT * FROM XX_VMR_PO_ProductDistrib PO where PO.XX_VMR_PO_LINEREFPROV_ID = ? ";
		PreparedStatement psDistribucion = DB.prepareStatement(sqlDistribucion, null);
		ResultSet rsDistribucion = null;
		X_XX_VMR_PO_ProductDistrib distrib = null;
		BigDecimal distribMargin = null;
		//Fin Jp
		PreparedStatement pstmt =  null; 
	    ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();					    
			while (rs.next()) {		
				
				//Actualiza el precio actual y el margen del POLineRefProv
				MVMRPOLineRefProv poLine = new MVMRPOLineRefProv(Env.getCtx(),rs.getInt("XX_VMR_PO_LineRefProv_ID"),get_Trx());
			

				BigDecimal CostoConDescuento = rs.getBigDecimal("XX_CostWithDiscounts");
				
				costoMonedaNac = rs.getBigDecimal("CostoNacional");
				costoMonedaNac = costoMonedaNac.setScale(2,BigDecimal.ROUND_HALF_UP);
				salePrice = rs.getBigDecimal("XX_SalePrice");
				
				BigDecimal unitConv = new BigDecimal(new X_XX_VMR_UnitConversion(getCtx(),rs.getInt("XX_VMR_UnitConversion_ID"), null).getXX_UnitConversion());
				BigDecimal unitSale = new BigDecimal(new X_XX_VMR_UnitConversion(getCtx(), rs.getInt("XX_PiecesBySale_ID"), null).getXX_UnitConversion());
				BigDecimal unitCompVent = unitConv.divide(unitSale);
				tradeCost = CostoConDescuento.divide(unitCompVent, 4, BigDecimal.ROUND_HALF_UP);
				tradeCost = tradeCost.multiply(XX_DefTradeFactor);
				tradeCost = tradeCost.setScale(2,BigDecimal.ROUND_HALF_UP);
				
				//Calcula el margen por producto segun la orden 
				BigDecimal margen = ((new BigDecimal(1)).subtract(costoMonedaNac.divide(salePrice, 2, RoundingMode.HALF_UP))).multiply(new BigDecimal(100));
				margen = margen.setScale (2,BigDecimal.ROUND_HALF_UP);
				
				poLine.setPriceActual(costoMonedaNac);
				//AGREGADO GHUCHET - COSTO COMERCIAL
				poLine.setXX_TradeCost(tradeCost);
				
				//Si el nuevo margen es menor de 20% se calcula el PVP con el margen anterior
				if(margen.compareTo(BigDecimal.valueOf(20))==-1){
					
					//Tax
					BigDecimal taxRate_Aux = getTaxRate(poLine.getC_TaxCategory_ID());
					BigDecimal taxRate = taxRate_Aux;
					
					taxRate = taxRate.divide( BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
					taxRate = taxRate.add(BigDecimal.ONE);

					salePrice = (poLine.getXX_CostWithDiscounts().multiply(order.getXX_DefinitiveFactor())).divide(
							(BigDecimal.ONE.subtract(poLine.getXX_Margin().divide(BigDecimal.valueOf(100)))),RoundingMode.HALF_UP).multiply(taxRate);
					
					BigDecimal becoPrice = becoPrice(salePrice);
					BigDecimal taxAmount = becoPrice.divide(taxRate, 2, BigDecimal.ROUND_HALF_UP);
					poLine.setXX_TaxAmount(becoPrice.subtract(taxAmount));
					poLine.setXX_SalePrice(taxAmount);
					poLine.setXX_SalePricePlusTax(becoPrice);
					poLine.setXX_LinePVPAmount(poLine.getXX_SalePrice().multiply(BigDecimal.valueOf(poLine.getSaleQty())));
					poLine.setXX_LinePlusTaxAmount(poLine.getXX_SalePricePlusTax().multiply(BigDecimal.valueOf(poLine.getSaleQty())));
				}
				else{
					poLine.setXX_Margin(margen);
				}
				
				poLine.save();
				get_Trx().commit();
				
				//Agregado por Javier Pino --Busca los detalles de distribución y los actualiza
				psDistribucion.setInt(1, rs.getInt("XX_VMR_PO_LineRefProv_ID"));
				rsDistribucion = psDistribucion.executeQuery();
				while (rsDistribucion.next()) {
					distrib = new X_XX_VMR_PO_ProductDistrib(Env.getCtx(), rsDistribucion, null);
					distrib.setPriceActual(costoMonedaNac);
					
					//Actualizar los valores en la ventana
					distribMargin = ((distrib.getXX_SalePrice().subtract(costoMonedaNac))
							.divide(distrib.getXX_SalePrice(), 4, RoundingMode.HALF_UP)).multiply(Env.ONEHUNDRED);		
					distribMargin = distribMargin.setScale (2,BigDecimal.ROUND_HALF_UP);
					distrib.setXX_Margin(distribMargin);
					distrib.save();
				}				
				rsDistribucion.close();
				rsDistribucion = null;
				//Fin				
				//Agregado por Javier Pino --Busca los detalles de distribución y los actualiza
				psDistribucion.setInt(1, rs.getInt("XX_VMR_PO_LineRefProv_ID"));
				rsDistribucion = psDistribucion.executeQuery();
				while (rsDistribucion.next()) {
					distrib = new X_XX_VMR_PO_ProductDistrib(Env.getCtx(), rsDistribucion, null);
					distrib.setPriceActual(costoMonedaNac);
					
					//Actualizar los valores en la ventana
					distribMargin = ((distrib.getXX_SalePrice().subtract(costoMonedaNac))
							.divide(distrib.getXX_SalePrice(), 4, RoundingMode.HALF_UP)).multiply(Env.ONEHUNDRED);		
					distribMargin = distribMargin.setScale (2,BigDecimal.ROUND_HALF_UP);
					distrib.setXX_Margin(distribMargin);
					distrib.save();
				}				
				rsDistribucion.close();
				rsDistribucion = null;
				//Fin				
			}
			}
			catch (Exception e) {
				e.printStackTrace();
			}	finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
				DB.closeResultSet(rsDistribucion);
				DB.closeStatement(psDistribucion);
			}				
		
	}//fin modifyPriceActualMargin 
	//fin RArvelo
	
	/**
	 * Author: JTrias
	 * Convierte la O/C Importada SITME a O/C Nacional
	 * @param order: la orden de compra a convertir
	 */
	private void convertSITME(MOrder i_Order){
		
		MOrder n_Order = new MOrder( getCtx(), 0, get_Trx());
		
		//Se copian y colocan los valores de los campos
		
		n_Order.setXX_ComesFromSITME(true);
		n_Order.setDocStatus("DR");
		n_Order.setDocAction("CO");
		n_Order.setAD_Org_ID(i_Order.getAD_Org_ID());
		n_Order.setC_Activity_ID(i_Order.getC_Activity_ID()); 
		n_Order.setC_DocTypeTarget_ID(i_Order.getC_DocTypeTarget_ID()); 
		n_Order.setC_DocType_ID(i_Order.getC_DocType_ID());			
		n_Order.setC_PaymentTerm_ID(i_Order.getC_PaymentTerm_ID()); 
		n_Order.setChargeAmt(i_Order.getChargeAmt()); 
		n_Order.setCopyFrom(i_Order.getCopyFrom()); 
		n_Order.setDateAcct(i_Order.getDateAcct()); 
		n_Order.setDateOrdered(i_Order.getDateOrdered()); 
		n_Order.setDatePromised(i_Order.getDatePromised()); 
		n_Order.setDeliveryViaRule(i_Order.getDeliveryViaRule());
		n_Order.setDescription(i_Order.getDescription()); 
		n_Order.setFreightAmt(i_Order.getFreightAmt()); 
		n_Order.setFreightCostRule(i_Order.getFreightCostRule()); 			
		n_Order.setInvoiceRule(i_Order.getInvoiceRule()); 
		n_Order.setIsActive(i_Order.isActive());
		n_Order.setIsApproved(i_Order.isApproved()); 
		n_Order.setIsCreditApproved(i_Order.isCreditApproved()); 
		n_Order.setIsDelivered(i_Order.isDelivered()); 
		n_Order.setIsDiscountPrinted(i_Order.isDiscountPrinted()); 
		n_Order.setIsDropShip(i_Order.isDropShip());
		n_Order.setIsInvoiced(i_Order.isInvoiced()); 
		n_Order.setIsPrinted(i_Order.isPrinted()); 
		n_Order.setIsReturnTrx(i_Order.isReturnTrx()); 
		n_Order.setIsSOTrx(i_Order.isSOTrx()); 
		n_Order.setIsSelected(i_Order.isSelected());
		n_Order.setIsSelfService(i_Order.isSelfService()); 
		n_Order.setIsTaxIncluded(i_Order.isTaxIncluded()); 
		n_Order.setIsTransferred(i_Order.isTransferred()); 
		n_Order.setM_PriceList_ID(i_Order.getM_PriceList_ID()); 
		n_Order.setM_RMACategory_ID(i_Order.getM_RMACategory_ID());
		n_Order.setM_ReturnPolicy_ID(i_Order.getM_ReturnPolicy_ID()); 
		n_Order.setM_Shipper_ID(i_Order.getM_Shipper_ID()); 
		n_Order.setOrig_InOut_ID(i_Order.getOrig_InOut_ID()); 
		n_Order.setOrig_Order_ID(i_Order.getOrig_Order_ID());
		n_Order.setPOReference(i_Order.getPOReference()); 
		n_Order.setPay_BPartner_ID(i_Order.getPay_BPartner_ID()); 
		n_Order.setPay_Location_ID(i_Order.getPay_Location_ID()); 
		n_Order.setPosted(i_Order.isPosted());
		n_Order.setPaymentRule(i_Order.getPaymentRule());
		n_Order.setPriorityRule(i_Order.getPriorityRule()); 
		n_Order.setProcessed(false); 
		n_Order.setProcessing(false); 
		n_Order.setRef_Order_ID(i_Order.get_ID()); 
		n_Order.setSendEMail(i_Order.isSendEMail()); 
		n_Order.setUser1_ID(i_Order.getUser1_ID());
		n_Order.setUser2_ID(i_Order.getUser2_ID()); 
		n_Order.setVolume(i_Order.getVolume()); 
		n_Order.setWeight(i_Order.getWeight());
		n_Order.setC_BPartnerSR_ID(i_Order.getC_BPartnerSR_ID()); 
		n_Order.setC_BP_BankAccount_ID(i_Order.getC_BP_BankAccount_ID());
		n_Order.setXX_Department(i_Order.getXX_Department()); 
		n_Order.setXX_ConsigneeName(i_Order.getXX_ConsigneeName()); 		
		n_Order.setXX_Discount1(i_Order.getXX_Discount1());
		n_Order.setXX_Discount2(i_Order.getXX_Discount2()); 
		n_Order.setXX_Discount3(i_Order.getXX_Discount3()); 
		n_Order.setXX_Discount4(i_Order.getXX_Discount4()); 
		n_Order.setXX_BuyersComments(i_Order.getXX_BuyersComments());
		n_Order.setXX_PurchaseOrderComments(i_Order.getXX_PurchaseOrderComments()); 
		n_Order.setXX_VMR_Subject_ID(i_Order.getXX_VMR_Subject_ID());
		n_Order.setXX_VLO_TypeDelivery(i_Order.getXX_VLO_TypeDelivery()); 
		n_Order.setXX_VMR_Package_ID(i_Order.getXX_VMR_Package_ID()); 
		n_Order.setXX_Collection_ID(i_Order.getXX_Collection_ID()); 
		n_Order.setXX_Season_ID(i_Order.getXX_Season_ID());
		n_Order.setXX_Brochure_ID(i_Order.getXX_Brochure_ID()); 
		n_Order.setXX_Category_ID(i_Order.getXX_Category_ID());
		n_Order.setXX_ComesFromCopy(i_Order.getXX_ComesFromCopy());
		n_Order.setXX_Void(false); 
		n_Order.setXX_MontLimit(i_Order.getXX_MontLimit()); 
		n_Order.setXX_UserBuyer_ID(i_Order.getXX_UserBuyer_ID()); 
		n_Order.setXX_ProductQuantity(i_Order.getXX_ProductQuantity());
		
		n_Order.setXX_ArrivalDate(i_Order.getXX_ArrivalDate());
		n_Order.setXX_EstimatedDate(i_Order.getXX_EstimatedDate());
		
		if (i_Order.getXX_VMR_CancellationMotive_ID()!=0) {
			n_Order.setXX_VMR_CancellationMotive_ID(i_Order.getXX_VMR_CancellationMotive_ID());
		}
		
		if (i_Order.getM_Warehouse_ID()!=0) {
			n_Order.setM_Warehouse_ID(i_Order.getM_Warehouse_ID());
		}
		
		n_Order.setXX_OrderType(X_Ref_XX_OrderType.NACIONAL.getValue());
		n_Order.setC_Country_ID(339); //Venezuela
		n_Order.setC_Currency_ID(205); //Bs.F
		n_Order.setXX_OrderStatus("PRO");
		
		//Obtenemos el proveedor asociado a la compañia importadora
		X_XX_VSI_Client client = new X_XX_VSI_Client( getCtx(), i_Order.get_ValueAsInt("XX_ImportingCompany_ID"), get_Trx());
		n_Order.setC_BPartner_ID(client.getC_BPartner_ID());
		
		//Obtenemos el contacto de ventas del proveedor
		String SQL = "SELECT AD_USER_ID "+
			         "FROM AD_USER ADU "+
			         "WHERE XX_CONTACTTYPE = "+ getCtx().getContext("#XX_L_CONTACTTYPESALES")+" "+
			         "AND C_BPARTNER_ID = "+ client.getC_BPartner_ID() +" AND ROWNUM=1 " ;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				n_Order.setAD_User_ID(rs.getInt("AD_USER_ID"));
			}

		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	

		Calendar today = Calendar.getInstance();
		
		//Si la fecha de llegada es mayor a la fecha actual le coloco la fecha actual para que deje guardar la O/C
		if(i_Order.getXX_ArrivalDate().before(today.getTime())){
			Date actualDate = Utilities.currentServerDate();
			n_Order.setXX_ArrivalDate(new Timestamp(actualDate.getTime()));  //MODIFICADO POR GHUCHET
			n_Order.setXX_EstimatedDate(new Timestamp(actualDate.getTime())); //MODIFICADO POR GHUCHET
		}
		else{
			n_Order.setXX_ArrivalDate(i_Order.getXX_ArrivalDate()); 
			n_Order.setXX_EstimatedDate(i_Order.getXX_EstimatedDate());
		}
		
		n_Order.setGrandTotal(i_Order.getGrandTotal()); 
		n_Order.setTotalLines(i_Order.getTotalLines());
		n_Order.setXX_EstimatedMargin(i_Order.getXX_EstimatedMargin()); 
		n_Order.setTotalPVP(i_Order.getTotalPVP()); 
		n_Order.setXX_TotalPVPPlusTax(i_Order.getXX_TotalPVPPlusTax()); 
		n_Order.set_CustomColumn("XX_TOTALCOSTBS", 0);
		n_Order.setXX_POType("POM");

		n_Order.setXX_Alert10("Esta O/C proviene de la O/C importada "+i_Order.getDocumentNo()+" (SITME)");
		
		if (n_Order.save()){
			get_Trx().commit();
			i_Order.setXX_Alert10("Esta O/C fué convertida a la O/C nacional "+n_Order.getDocumentNo()+" (SITME)");
			orderSITME_I.add(i_Order.getDocumentNo());
			orderSITME_N.add(n_Order.getDocumentNo());
			i_Order.setProcessed(true);
			i_Order.save();
			get_Trx().commit();
			//Ahora se copian los detalles
			String sql1 = "SELECT * "
				+ "FROM XX_VMR_PO_LINEREFPROV "
				+ "WHERE C_ORDER_ID=" + i_Order.getC_Order_ID();
	
			PreparedStatement prst = DB.prepareStatement(sql1,null);
	
			//Porcentaje de incremento en los costos de la O/C
			BigDecimal incPercentage = getIncPercentage(i_Order.getXX_ImportingCompany_ID());
			incPercentage = BigDecimal.ONE.add(incPercentage.divide(new BigDecimal(100)));
			rs = null;
			PreparedStatement prst2 = null;
			ResultSet rs2 = null;
			try {
				rs = prst.executeQuery();
				while (rs.next()){
	
					MVMRVendorProdRef reference = new MVMRVendorProdRef(getCtx(),rs.getInt("XX_VMR_VENDORPRODREF_ID"), get_Trx());
					MVMRPOLineRefProv detail = new MVMRPOLineRefProv(getCtx(),0, get_Trx());
					
					if (reference.isActive())
					{
						// setea cada uno de los detalles de la orden de compra
						detail.setXX_IsDefinitive(rs.getString("XX_ISDEFINITIVE").equals("Y")); 
						detail.setXX_WithCharacteristic(rs.getString("XX_WITHCHARACTERISTIC").equals("Y")); 
						detail.setXX_SaleUnit_ID(rs.getInt("XX_SaleUnit_ID")); 
						detail.setXX_PiecesBySale_ID(rs.getInt("XX_PiecesBySale_ID")); 
						detail.setXX_VME_ConceptValue_ID(rs.getInt("XX_VME_ConceptValue_ID"));
						detail.setIsActive(rs.getString("ISACTIVE").equals("Y")); 
						detail.setDescription(rs.getString("DESCRIPTION")); 
						detail.setXX_VMR_UnitPurchase_ID(rs.getInt("XX_VMR_UnitPurchase_ID")); 
						detail.setXX_VMR_UnitConversion_ID(rs.getInt("XX_VMR_UnitConversion_ID")); 
						detail.setXX_Characteristic1_ID(rs.getInt("XX_CHARACTERISTIC1_ID")); 
						detail.setXX_Characteristic2_ID(rs.getInt("XX_CHARACTERISTIC2_ID")); 
						detail.setXX_CostPerOrder(rs.getBigDecimal("XX_COSTPERORDER")); 
						detail.setQty(rs.getInt("QTY")); 
						detail.setXX_VMR_VendorProdRef_ID(rs.getInt("XX_VMR_VENDORPRODREF_ID")); 
						detail.setXX_PackageMultiple(rs.getInt("XX_PACKAGEMULTIPLE")); 
						detail.setXX_LastSalePrice(rs.getBigDecimal("XX_LASTSALEPRICE"));
						detail.setXX_VMR_Line_ID(rs.getInt("XX_VMR_Line_ID")); 
						detail.setXX_VMR_Section_ID(rs.getInt("XX_VMR_SECTION_ID")); 
						detail.setC_Order_ID (n_Order.getC_Order_ID()); 
						detail.setLine(rs.getBigDecimal("LINE"));
						detail.setXX_Characteristic1Value1_ID(rs.getInt("XX_CHARACTERISTIC1VALUE1_ID")); 
						detail.setXX_Characteristic1Value2_ID(rs.getInt("XX_CHARACTERISTIC1VALUE2_ID")); 
						detail.setC_TaxCategory_ID(rs.getInt("C_TAXCATEGORY_ID")); 
						detail.setXX_Characteristic1Value3_ID(rs.getInt("XX_CHARACTERISTIC1VALUE3_ID")); 
						detail.setXX_Characteristic1Value4_ID(rs.getInt("XX_CHARACTERISTIC1VALUE4_ID")); 
						detail.setXX_Characteristic1Value5_ID(rs.getInt("XX_CHARACTERISTIC1VALUE5_ID")); 
						detail.setXX_Characteristic1Value6_ID(rs.getInt("XX_CHARACTERISTIC1VALUE6_ID"));
						detail.setXX_Characteristic1Value7_ID(rs.getInt("XX_CHARACTERISTIC1VALUE7_ID")); 
						detail.setXX_Characteristic1Value8_ID(rs.getInt("XX_CHARACTERISTIC1VALUE8_ID")); 
						detail.setXX_Characteristic1Value9_ID(rs.getInt("XX_CHARACTERISTIC1VALUE9_ID")); 
						detail.setXX_Characteristic1Value10_ID(rs.getInt("XX_CHARACTERISTIC1VALUE10_ID")); 
						detail.setXX_Characteristic2Value1_ID(rs.getInt("XX_CHARACTERISTIC2VALUE1_ID")); 
						detail.setXX_Characteristic2Value2_ID(rs.getInt("XX_CHARACTERISTIC2VALUE2_ID"));
						detail.setXX_Characteristic2Value3_ID(rs.getInt("XX_CHARACTERISTIC2VALUE3_ID")); 
						detail.setXX_Characteristic2Value4_ID(rs.getInt("XX_CHARACTERISTIC2VALUE4_ID")); 
						detail.setXX_Characteristic2Value5_ID(rs.getInt("XX_CHARACTERISTIC2VALUE5_ID")); 
						detail.setXX_Characteristic2Value6_ID(rs.getInt("XX_CHARACTERISTIC2VALUE6_ID")); 
						detail.setXX_Characteristic2Value7_ID(rs.getInt("XX_CHARACTERISTIC2VALUE7_ID")); 
						detail.setXX_Characteristic2Value8_ID(rs.getInt("XX_CHARACTERISTIC2VALUE8_ID"));
						detail.setXX_Characteristic2Value9_ID(rs.getInt("XX_CHARACTERISTIC2VALUE9_ID")); 
						detail.setXX_Characteristic2Value10_ID(rs.getInt("XX_CHARACTERISTIC2VALUE10_ID")); 
						detail.setXX_LineQty(rs.getInt("XX_LINEQTY")); 
						detail.setXX_GiftsQty(rs.getInt("XX_GIFTSQTY"));
						detail.setXX_Rebate4(rs.getBigDecimal("XX_REBATE4")); 
						detail.setXX_Rebate3(rs.getBigDecimal("XX_REBATE3")); 
						detail.setXX_Rebate2(rs.getBigDecimal("XX_REBATE2")); 
						detail.setXX_Rebate1(rs.getBigDecimal("XX_REBATE1")); 
						detail.setSaleQty(rs.getInt("SaleQty")); 
						detail.setXX_AssociateReference(rs.getString("XX_AssociateReference"));
						detail.setXX_VMR_LongCharacteristic_ID(rs.getInt("XX_VMR_LongCharacteristic_ID")); 
						detail.setXX_VMR_Brand_ID(rs.getInt("XX_VMR_Brand_ID"));
						detail.setXX_ReferenceIsAssociated(false);
						detail.setXX_GenerateMatrix(rs.getString("XX_GenerateMatrix"));
						detail.setXX_DeleteMatrix(rs.getString("XX_DeleteMatrix"));
						detail.setXX_ShowMatrix(rs.getString("XX_ShowMatrix"));		
						
						detail.setXX_LinePlusTaxAmount(rs.getBigDecimal("XX_LINEPLUSTAXAMOUNT")); 
						detail.setXX_LinePVPAmount(rs.getBigDecimal("XX_LINEPVPAMOUNT"));
						detail.setXX_TaxAmount(rs.getBigDecimal("XX_TaxAmount")); 
						detail.setXX_SalePrice(rs.getBigDecimal("XX_SALEPRICE"));
						detail.setXX_SalePricePlusTax(rs.getBigDecimal("XX_SALEPRICEPLUSTAX")); 
						
						//Convertimos a Bs todos los costos y le agregamos el porcentaje de incremento
						//detail.setXX_UnitPurchasePrice((rs.getBigDecimal("XX_UNITPURCHASEPRICE").multiply(i_Order.getXX_DefinitiveFactor())).multiply(incPercentage));
						detail.setXX_UnitPurchasePrice(rs.getBigDecimal("PRICEACTUAL").multiply(incPercentage).setScale (2,BigDecimal.ROUND_HALF_UP));
						detail.setLineNetAmt((rs.getBigDecimal("LINENETAMT").multiply(i_Order.getXX_DefinitiveFactor())).multiply(incPercentage).setScale (2,BigDecimal.ROUND_HALF_UP));
						detail.setPriceActual((rs.getBigDecimal("XX_CostWithDiscounts").multiply(i_Order.getXX_DefinitiveFactor())).multiply(incPercentage).setScale (2,BigDecimal.ROUND_HALF_UP));
						//detail.setXX_CostWithDiscounts(rs.getBigDecimal("PRICEACTUAL").multiply(incPercentage));
						detail.setXX_CostWithDiscounts((rs.getBigDecimal("XX_CostWithDiscounts").multiply(i_Order.getXX_DefinitiveFactor())).multiply(incPercentage).setScale (2,BigDecimal.ROUND_HALF_UP));
						
						//AGREGADO GHUCHET - Costo Comercial (revisar si se debe usar es el XX_CostWithDiscounts en vez de XX_UNITPURCHASEPRICE
						detail.setXX_TradeCost((rs.getBigDecimal("XX_CostWithDiscounts").multiply(i_Order.getXX_DefinitiveTradeFactor())).multiply(incPercentage).setScale (2,BigDecimal.ROUND_HALF_UP));
						
					
						//Recalculamos el Margen						
						BigDecimal newMargin = ((new BigDecimal(1)).subtract(detail.getXX_UnitPurchasePrice().divide(detail.getXX_SalePrice(), 2, RoundingMode.HALF_UP))).multiply(new BigDecimal(100));
						newMargin = newMargin.setScale (2,BigDecimal.ROUND_HALF_UP);
						detail.setXX_Margin(newMargin);
						
						detail.setXX_IsGeneratedCharac1Value1(rs.getString("XX_IsGeneratedCharac1Value1").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value2(rs.getString("XX_IsGeneratedCharac1Value2").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value3(rs.getString("XX_IsGeneratedCharac1Value3").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value4(rs.getString("XX_IsGeneratedCharac1Value4").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value5(rs.getString("XX_IsGeneratedCharac1Value5").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value6(rs.getString("XX_IsGeneratedCharac1Value6").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value7(rs.getString("XX_IsGeneratedCharac1Value7").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value8(rs.getString("XX_IsGeneratedCharac1Value8").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value9(rs.getString("XX_IsGeneratedCharac1Value9").equals("Y"));
						detail.setXX_IsGeneratedCharac1Value10(rs.getString("XX_IsGeneratedCharac1Value10").equals("Y"));
						
						detail.setXX_IsGeneratedCharac2Value1(rs.getString("XX_IsGeneratedCharac2Value1").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value2(rs.getString("XX_IsGeneratedCharac2Value2").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value3(rs.getString("XX_IsGeneratedCharac2Value3").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value4(rs.getString("XX_IsGeneratedCharac2Value4").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value5(rs.getString("XX_IsGeneratedCharac2Value5").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value6(rs.getString("XX_IsGeneratedCharac2Value6").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value7(rs.getString("XX_IsGeneratedCharac2Value7").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value8(rs.getString("XX_IsGeneratedCharac2Value8").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value9(rs.getString("XX_IsGeneratedCharac2Value9").equals("Y"));
						detail.setXX_IsGeneratedCharac2Value10(rs.getString("XX_IsGeneratedCharac2Value10").equals("Y"));
						
						detail.save();			
						get_Trx().commit();
						sql1 = "SELECT *"
							+ " FROM XX_VMR_REFERENCEMATRIX"
							+ " WHERE XX_VMR_PO_LINEREFPROV_ID=" + rs.getInt("XX_VMR_PO_LineRefProv_ID");	
						
						prst2 = DB.prepareStatement(sql1,null);
						rs2 = prst2.executeQuery();
						
						while (rs2.next()){
							
							// Esta parte setea la matriz de referencias
							
							X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(getCtx(),0,get_Trx());
									
							matrix.setXX_VALUE1(rs2.getInt("XX_VALUE1"));
							matrix.setXX_VALUE2(rs2.getInt("XX_VALUE2"));
							matrix.setXX_COLUMN(rs2.getInt("XX_COLUMN"));
							matrix.setXX_ROW(rs2.getInt("XX_ROW"));
							matrix.setXX_QUANTITYC(rs2.getInt("XX_QUANTITYC"));
							matrix.setXX_QUANTITYV(rs2.getInt("XX_QUANTITYV"));
							matrix.setXX_QUANTITYO(rs2.getInt("XX_QUANTITYO"));
							matrix.setXX_VMR_PO_LineRefProv_ID(detail.getXX_VMR_PO_LineRefProv_ID());
							matrix.save();
							get_Trx().commit();
						}
						DB.closeStatement(prst2);

					}
				}
			} catch (SQLException e){
				System.out.println("Error al copiar los detalles o la matriz de referencias " + e);			
			}	finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
				DB.closeStatement(prst);
				DB.closeResultSet(rs2);
				DB.closeStatement(prst2);
			}	
			
			
			//Ahora se copian los limites
			String sql = "SELECT * "
				+ "FROM XX_VMR_PO_APPROVAL "
				+ "WHERE C_ORDER_ID=" + i_Order.getC_Order_ID();
	
			PreparedStatement prst3 = DB.prepareStatement(sql,null);
			ResultSet rs3 = null;
			try 
			{
				rs3 = prst3.executeQuery();
				while (rs3.next()){
					
					X_XX_VMR_PO_Approval aprov = new X_XX_VMR_PO_Approval( getCtx(), 0, get_Trx());
					
					aprov.setXX_VMR_Department_ID(rs3.getInt("XX_VMR_Department_ID"));
					aprov.setXX_VMR_Line_ID(rs3.getInt("XX_VMR_Line_ID"));
					aprov.setXX_VMR_Section_ID(rs3.getInt("XX_VMR_Section_ID"));
					aprov.setXX_PercentageExcess(rs3.getBigDecimal("XX_PercentageExcess"));
					aprov.setXX_Limit(rs3.getBigDecimal("XX_Limit"));
					aprov.setXX_LimitTotal(rs3.getBigDecimal("XX_LimitTotal"));
					aprov.setIsApproved(rs3.getString("IsApproved").equals("Y"));
					aprov.setC_Order_ID(n_Order.get_ID());
					aprov.save();
					get_Trx().commit();
				}
				
			}catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			} finally{
				DB.closeResultSet(rs3);
				DB.closeStatement(prst3);
			}	
			//Proceso Asociar Todas las Referencias: XX_AssociateAllReferences 
			int ASSOCIATEPROCESS_ID = Env.getCtx().getContextAsInt("#XX_L_PROCESSASSOCIATEALLREF_ID");

			//AGREGADO GHUCHET
			//Se llama al proceso de Asociar Todas las Referencias: XX_AssociateAllReferences 
			Env.getCtx().setContext("#APPROVEPOPROCESS",1);
			callProcess(ASSOCIATEPROCESS_ID, n_Order.get_ID());
			int wait = 1;
			Env.getCtx().setContext("#WAITPROCESS",1);
			while (wait == 1){
				wait = Env.getCtx().getContextAsInt("#WAITPROCESS");
				//System.out.println(wait);
			}
			Env.getCtx().remove("#WAITPROCESS");
			//FIN GHUCHET
			//AGREGADO GHUCHET
			//sendMailSitmeToNational(n_Order.get_ID(), i_Order.get_ID());
		}
		
	}
	/** Obtiene el procentaje de incremento de una compañía importadora
	 * @author ghuchet
	 * */
	private BigDecimal getIncPercentage(int XX_ImportingCompany_ID) {
		BigDecimal incPerc = new BigDecimal(0);
		
		String sql = "SELECT nvl(XX_ADMINISTRATIVEFEE,0) "
				+ "FROM XX_VSI_CLIENT "
				+ "WHERE XX_VSI_CLIENT_ID=" +XX_ImportingCompany_ID;
	
			PreparedStatement prst = DB.prepareStatement(sql,null);
			ResultSet rs = null;
			try {
				rs = prst.executeQuery();
				while (rs.next()){
					incPerc = rs.getBigDecimal("XX_ADMINISTRATIVEFEE");
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally{
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}	
		
		return incPerc;
	}

	private void callProcess(int ProcessID, int recordID){
		MPInstance mpi = new MPInstance( Env.getCtx(), ProcessID, recordID); 
		mpi.save();
		ProcessInfo pi = new ProcessInfo("", ProcessID); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(ProcessID); 
		pi.setClassName(""); 
		pi.setTitle(""); 
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
	}//
	
	/*
	 * Precio Beco
	 */
	private BigDecimal becoPrice(BigDecimal myPrice){
		
		BigDecimal price = null;	
		PreparedStatement priceRulePstmt = null;
		ResultSet priceRuleRs = null;
		Integer precioInt = null;
		BigDecimal precioBig = null;
		Integer auxPrice = 0;
		BigDecimal terminacion = null;
		BigDecimal var = null;
		BigDecimal beco = BigDecimal.ZERO;
		price = myPrice;

		if(price.compareTo(BigDecimal.ZERO)>0){

			precioInt = 0;
			precioBig = BigDecimal.ZERO;
			terminacion = BigDecimal.ZERO;
			var = BigDecimal.ZERO;
			auxPrice = 0;
			
			String priceRuleSQL = "SELECT XX_Lowrank,XX_Highrank,XX_Termination,xx_increase,XX_infinitevalue " +
								  "FROM xx_vme_pricerule ORDER BY (xx_lowrank)";
			
			try{
				
				priceRulePstmt = DB.prepareStatement(priceRuleSQL, null);
				priceRuleRs = priceRulePstmt.executeQuery();
		
				precioInt = price.intValue();
				precioBig = new BigDecimal(precioInt);
				
				while(priceRuleRs.next())
				{	
					if(precioBig.compareTo(priceRuleRs.getBigDecimal("xx_lowrank"))>=0 && precioBig.compareTo(priceRuleRs.getBigDecimal("xx_highrank"))<=0) 
					{
						Integer incremento = priceRuleRs.getInt("xx_increase");
					    	  
					    for(auxPrice=priceRuleRs.getInt("xx_lowrank")-1;auxPrice<=priceRuleRs.getInt("xx_highrank");auxPrice=auxPrice+incremento)
					    {
					    	var = new BigDecimal(auxPrice);
					    		 
					    	if(precioBig.compareTo(var) <= 0)
					    	{
					    		beco = var;
					    			  
					    		terminacion = priceRuleRs.getBigDecimal("xx_termination");
					    		if(terminacion.intValue()==0)
					    		{
					    			beco = var.add(terminacion);
					    		}
					    		else
					    		{
					    			var = var.divide(new BigDecimal(10));
					    			Integer aux = var.intValue()*10;
					    			beco = new BigDecimal(aux).add(terminacion);
					    		}
					    			 
					    		priceRuleRs.close();
					 			priceRulePstmt.close();
					    	 }
						}
					}
				}
				priceRuleRs.close();
				priceRulePstmt.close();
			}
			catch (Exception e) {
				
			}
		}
		return beco;
	}
	
	/*
	 * Impuesto
	 */
	private BigDecimal getTaxRate(int taxCategID){
		
		BigDecimal taxRate = BigDecimal.ZERO;
		
		String SQL = "select RATE from C_TAX where VALIDFROM = " +
					 "(SELECT MAX(validfrom) FROM C_TAX " +
					 "WHERE VALIDFROM <= sysdate AND C_TAXCATEGORY_ID = " + taxCategID +
					 " GROUP BY C_TaxCategory_ID)";
		
		PreparedStatement pstmt = DB.prepareStatement(SQL, null);
		ResultSet rs = null;
		
		try {

			rs = pstmt.executeQuery();

			if (rs.next())
				taxRate = rs.getBigDecimal(1);
		} 
		catch (SQLException e){
			System.out.println("Error " + SQL);
		}
		finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		
		return taxRate;
	}
}