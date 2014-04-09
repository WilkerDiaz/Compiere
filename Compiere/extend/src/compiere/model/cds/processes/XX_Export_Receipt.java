package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_E_XX_VMR_NRAD01;
import compiere.model.cds.X_E_XX_VMR_NRAD02;
import compiere.model.cds.X_E_XX_VMR_NRAM01;
import compiere.model.cds.X_E_XX_VMR_NRAM02;

public class XX_Export_Receipt extends SvrProcess {

	protected String doIt() throws Exception {

		
		
		System.out.println("Borro las E");
		
		//Borrado de la NRAM01
		String sql_delete = "DELETE E_XX_VMR_NRAM01 ";
		DB.executeUpdate(get_Trx(),sql_delete);
		
		//Borrado de la NRAM02
		String sql_delete2 = "DELETE E_XX_VMR_NRAM02 ";
		DB.executeUpdate(get_Trx(),sql_delete2);
		
		//Borrado de la NRAD01
		String sql_delete3 = "DELETE E_XX_VMR_NRAD01 ";
		
		DB.executeUpdate(get_Trx(),sql_delete3);
		
		commit();
		
		X_E_XX_VMR_NRAM01 receipt_M01=null;
		X_E_XX_VMR_NRAM02 receipt_M02=null;
		X_E_XX_VMR_NRAD01 receipt_D01=null;

		String numOrden="";
		String numRecepcion="";
		String numFactura="";

		String fechaRecepcion="";
		String fechaRegistro="";
		String fechaAprobacion="";
		String fechaFactura="";
		String fechaChequeo="";

		String dia;
		String mes;
		String ano;

		StringTokenizer tokens;
		String statusOC="";
		String proforma="PRO";
		boolean status=false;

		ResultSet rs4 = null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		ResultSet rs5=null;
		ResultSet rs6=null;
		ResultSet rs7=null;
		ResultSet rs9=null;

		BigDecimal Descu1=new BigDecimal("0.0");
		BigDecimal Descu2=new BigDecimal("0.0");
		BigDecimal Descu3=new BigDecimal("0.0");
		BigDecimal Descu4=new BigDecimal("0.0");
		BigDecimal mondes=new BigDecimal("0.0");
		BigDecimal mcosto=new BigDecimal("0.0");

		BigDecimal mcosfa=new BigDecimal(0);
		BigDecimal calculo=new BigDecimal(0);
		BigDecimal monfac=new BigDecimal(0);
		int canpro=0;


		String createdByOC="";
		String factur=null;
		String usract=null;
		String fopago=null;


		String sql_update = " UPDATE M_INOUT SET XX_STATUS_SINC = 'Y' WHERE DOCUMENTNO= ?";

		//Marcar el producto como actualizado
		PreparedStatement ps_update = DB.prepareStatement(sql_update, null);

		//Busco todas las recepciones y parto de ahi
		String SqlRec= "Select M.DOCUMENTNO as NUMREC,M.XX_COUNTEDPACKAGESQTY AS BULREC,To_Char(M.CREATED,'YYYY-MM-DD') as fechaRegistro," 
					   + "CASE WHEN M.XX_INOUTSTATUS = 'RE' THEN 'G'" 
					   + "WHEN M.XX_INOUTSTATUS = 'CH' THEN '7' "
					   +"END  AS STAREC,UPPER(A.VALUE) AS RECEPT,To_Char(C.XX_RECEPTIONDATE,'YYYY-MM-DD') as fechaRecepcion," 
					   + "CB.VALUE AS COEMPE "                                                                                                                                                                                                                                                                                                                                                                
			 		   + "from M_INOUT M,AD_USER A,C_BPARTNER CB, C_ORDER C "
			 		   + "where M.XX_STATUS_SINC='N' and M.ISSOTRX='N' and (M.XX_INOUTSTATUS ='RE' OR M.XX_INOUTSTATUS ='CH') "
			 		   + "AND M.CREATEDBY=A.AD_USER_ID AND CB.C_BPARTNER_ID=M.C_BPARTNER_ID "
                       + "AND M.C_ORDER_ID=C.C_ORDER_ID " 
                       // Incluido por CCapote
                       + "and c.xx_PoType = 'POM' ";

		String SqlRecDetalle="Select DISTINCT M.DOCUMENTNO AS NUMREC,To_Char(M.CREATED,'YYYY-MM-DD') as fechaRegistro,M.XX_INOUTSTATUS AS STAREC,"
			+ "MW.VALUE AS TIENDA,"
			+ "MP.VALUE AS CODPRO,ML.PICKEDQTY AS CANCHQ ,XVP.VALUE AS REFPRO"
			+ " from M_INOUT M,AD_USER A,M_WAREHOUSE MW,M_INOUTLINE ML,M_PRODUCT MP,C_BPARTNER CB, XX_VMR_VENDORPRODREF XVP"
			+ " where M.XX_STATUS_SINC='N' AND M.CREATEDBY=A.AD_USER_ID AND MW.M_WAREHOUSE_ID(+)=M.M_WAREHOUSE_ID"
			+ " and M.ISSOTRX='N' AND ML.M_INOUT_ID=M.M_INOUT_ID AND ML.M_PRODUCT_ID=MP.M_PRODUCT_ID"
			+ " AND MP.XX_VMR_VENDORPRODREF_ID = XVP.XX_VMR_VENDORPRODREF_ID "
			+ " AND CB.C_BPARTNER_ID=M.C_BPARTNER_ID AND M.DOCUMENTNO= ?";
		
		String SqlRecDetalle1="Select DISTINCT M.DOCUMENTNO AS NUMREC,To_Char(M.CREATED,'YYYY-MM-DD') as fechaRegistro," +
			"M.XX_INOUTSTATUS AS STAREC,"
			+ "MW.VALUE AS TIENDA,"
			+ "MP.VALUE AS CODPRO,ML.SCRAPPEDQTY AS CANDEV, XVP.VALUE AS REFPRO"
			+ " from M_INOUT M,AD_USER A,M_WAREHOUSE MW,M_INOUTLINE ML,M_PRODUCT MP,C_BPARTNER CB, XX_VMR_VENDORPRODREF XVP"
			+ " where M.XX_STATUS_SINC='N' AND M.CREATEDBY=A.AD_USER_ID AND MW.M_WAREHOUSE_ID(+)=M.M_WAREHOUSE_ID"
			+ " and M.ISSOTRX='N' AND ML.M_INOUT_ID=M.M_INOUT_ID AND ML.M_PRODUCT_ID=MP.M_PRODUCT_ID"
			+ " AND MP.XX_VMR_VENDORPRODREF_ID = XVP.XX_VMR_VENDORPRODREF_ID "
			+ " AND CB.C_BPARTNER_ID=M.C_BPARTNER_ID AND M.DOCUMENTNO= ?";

		//Busco todas las ordenes de compra de esa recepcion
		
		
		
		String OC= "Select NVL(C.XX_REPLACEMENTFACTOR, 0) AS FACCAM, C.C_ORDER_ID C_ORDER_ID, C.DOCUMENTNO as NUMORD,To_Char(C.XX_RECEPTIONDATE,'YYYY-MM-DD') AS fechaRecepcion,"
				+"To_Char(C.XX_INVOICEDATE,'YYYY-MM-DD') AS fechaFacturacion,"
				+"To_Char(C.XX_CHECKUPDATE,'YYYY-MM-DD') AS fechaChequeo,NVL(XB.XX_GUIDENUMBER,' ') AS GUIAEM,"
				+"CASE WHEN CC.ISO_CODE='VEB' THEN 1 "
			    +"WHEN CC.ISO_CODE='USD' THEN 2 "
			    +"WHEN CC.ISO_CODE='EUR' THEN 6 " 
			    +"END AS CODMON, "
		        +"UPPER(A.VALUE) AS COEMPE "
				+"from c_order c,m_inout m,XX_VLO_BOARDINGGUIDE XB,C_CURRENCY CC,AD_USER A "
				+"where C.C_ORDER_ID=M.C_ORDER_ID "
				 // Incluido por CCapote
                + "and c.xx_PoType = 'POM' "
				+"AND XB.XX_VLO_BOARDINGGUIDE_ID(+)=C.XX_VLO_BOARDINGGUIDE_ID "
				+"AND CC.C_CURRENCY_ID(+)=C.C_CURRENCY_ID "
				+"AND C.CREATEDBY=A.AD_USER_ID AND M.DOCUMENTNO= ?";

		String SqlOC="Select C.DOCUMENTNO as NUMORD,MP.VALUE AS CODPRO,RM.XX_QUANTITYV AS CANPRO," 
					 +"ROUND(((XP.PRICEACTUAL*RM.XX_QUANTITYC) / " +
					 "CASE WHEN RM.XX_QUANTITYV = 0 THEN 1 " +
					 "ELSE RM.XX_QUANTITYV END " +
					 "), 2) AS MCOSTO,XP.XX_SALEPRICE AS MVENTA,"
					 +"XD.VALUE AS CODDEP,XL.VALUE AS CODLIN,XS.VALUE AS SECCIO, XVP.VALUE AS REFPRO,"
					 +"RM.XX_QUANTITYO as CNTOBS,XP.XX_REBATE1 AS DESCU1,XP.XX_REBATE2 AS DESCU2,"
                     +"XP.XX_REBATE3 AS DESCU3,XP.XX_REBATE4 AS DESCU4,"
					 +"MW.VALUE AS TIENDA,UPPER(A.VALUE) AS COEMPE "
					 +"from C_ORDER C,M_INOUT M,XX_VMR_DEPARTMENT XD,XX_VMR_LINE XL,XX_VMR_PO_LINEREFPROV XP,"
					 +"XX_VMR_SECTION XS,C_BPARTNER CB,XX_VLO_BOARDINGGUIDE XB,C_CURRENCY CC,M_PRODUCT MP,M_WAREHOUSE MW,AD_USER A,"
					 +"XX_VMR_REFERENCEMATRIX RM, XX_VMR_VENDORPRODREF XVP "
					 +"WHERE C.C_ORDER_ID=M.C_ORDER_ID AND M.DOCUMENTNO= ? "
					 // Incluido por CCapote
		                + " and c.xx_PoType = 'POM' "
					 +"AND C.XX_VMR_DEPARTMENT_ID=XD.XX_VMR_DEPARTMENT_ID "
					 +"AND XL.XX_VMR_LINE_ID=XP.XX_VMR_LINE_ID AND C.CREATEDBY=A.AD_USER_ID "
					 +"AND XP.XX_VMR_PO_LINEREFPROV_ID=RM.XX_VMR_PO_LINEREFPROV_ID "
					 +"AND XS.XX_VMR_SECTION_ID=XP.XX_VMR_SECTION_ID AND CB.C_BPARTNER_ID=C.C_BPARTNER_ID "
					 +"and XB.XX_VLO_BOARDINGGUIDE_ID(+)=C.XX_VLO_BOARDINGGUIDE_ID AND CC.C_CURRENCY_ID(+)=C.C_CURRENCY_ID "
					 +"AND RM.M_PRODUCT=MP.M_PRODUCT_ID AND C.M_WAREHOUSE_ID=MW.M_WAREHOUSE_ID "
					 +" AND XP.XX_VMR_VENDORPRODREF_ID = XVP.XX_VMR_VENDORPRODREF_ID "
					 +"AND C.ISSOTRX='N' AND XP.C_ORDER_ID=C.C_ORDER_ID";
		
		String Factura="Select C.XX_INVOICINGSTATUS INVSTATUS,NVL(CI.DOCUMENTNO, ' ') AS NUMFAC,CI.TOTALLINES AS MONFAC,To_Char(CI.CREATED,'YYYY-MM-DD') AS fechaReg,"
						+ "CB.VALUE AS COEMPE,MW.VALUE AS TIEFAC,CI.XX_TAXAMOUNT AS MONIMP, CP.VALUE AS FOPAGO"
						+ " from c_invoice ci,c_order c,C_BPARTNER CB,M_WAREHOUSE MW,C_PAYMENTTERM CP"
						+ " where CI.ISSOTRX='N' AND (CI.DOCSTATUS ='CO' and " +
								"CI.C_DOCTYPETARGET_ID="+ getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")+")"
						+ " and C.XX_InvoicingStatus ='AP' and CI.C_ORDER_ID=C.C_ORDER_ID and C.DOCUMENTNO= ?"
						+ " AND CB.C_BPARTNER_ID(+)=CI.C_BPARTNER_ID"
						+ " AND MW.M_WAREHOUSE_ID=C.M_WAREHOUSE_ID " 
						+ " AND CP.C_PAYMENTTERM_ID(+)=CI.C_PAYMENTTERM_ID";

		String SqlFactura="Select CI.DOCUMENTNO AS NUMFAC,MP.VALUE AS CODPRO, "
				          + "ROUND((CIL.QTYINVOICED * "
			              + "(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XVP.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID)"
				          + "/ (SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XVP.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID)"
			              + "),2) AS CANPRO,"
						  + "A.VALUE AS FACTUR,A2.VALUE AS USRACT,"
						  + "ROUND((coalesce(CIL.XX_PRICEENTEREDINVOICE,0)/"
					      + "(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XVP.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID)"
						  + "* (SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XVP.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID)"
						  + "),2) AS MCOSTO,"
						  + "ROUND((CIL.XX_PRICEACTUALINVOICE/"
						  + "(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XVP.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID)"
						  + "* (SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XVP.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID)"
						  + " ),2) AS MCOSFA,"
						  + "CI.XX_DISCOUNT1 AS DESCU1,CI.XX_DISCOUNT2 AS DESCU2, XVP.VALUE AS REFPRO,"
						  + "CI.XX_DISCOUNT3 AS DESCU3,CI.XX_DISCOUNT4 AS DESCU4,CIL.LINETOTALAMT AS TOTALINEA,MW.VALUE AS TIEFAC,CIL.TAXAMT AS MONIMP "
						  + "FROM C_INVOICE CI,C_ORDER C,C_INVOICELINE CIL,M_PRODUCT MP,M_WAREHOUSE MW,AD_USER A,AD_USER A2,C_BPARTNER CB, "
						  + "XX_VMR_VENDORPRODREF XVP "
						  + "WHERE CI.C_ORDER_ID=C.C_ORDER_ID "
						  + "AND CIL.C_INVOICE_ID=CI.C_INVOICE_ID "
						  + "AND CIL.M_PRODUCT_ID=MP.M_PRODUCT_ID " 
						  + "AND A2.AD_USER_ID(+)=CI.UPDATEDBY "
						  + "and CI.ISSOTRX='N' AND (CI.DOCSTATUS ='CO' and CI.C_DOCTYPETARGET_ID="+ getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")+") "
						  + "and C.XX_InvoicingStatus ='AP' AND MW.M_WAREHOUSE_ID=C.M_WAREHOUSE_ID and C.DOCUMENTNO= ? "
						  + "AND A.AD_USER_ID(+)=CI.CREATEDBY "
						  + "AND CI.C_BPARTNER_ID=CB.C_BPARTNER_ID"
						  + " AND MP.XX_VMR_VENDORPRODREF_ID = XVP.XX_VMR_VENDORPRODREF_ID ";

	
		PreparedStatement ps_Rec = DB.prepareStatement(SqlRec,get_TrxName());
		
		PreparedStatement ps_OC = DB.prepareStatement(OC,get_TrxName());
		PreparedStatement ps_RecDetalle = DB.prepareStatement(SqlRecDetalle,get_TrxName());
		PreparedStatement ps_RecDetalle1 = DB.prepareStatement(SqlRecDetalle1,get_TrxName());
		PreparedStatement ps_FacturaDetalle = DB.prepareStatement(SqlFactura,get_TrxName());
		PreparedStatement ps_Factura = DB.prepareStatement(Factura,get_TrxName());
		PreparedStatement ps_OCDetalle = DB.prepareStatement(SqlOC,get_TrxName());
		
		ResultSet rs = null;
		try{
			rs = ps_Rec.executeQuery();


			while (rs.next()){ // Recepcion 

				numRecepcion=rs.getString("NUMREC");
				//System.out.println(numRecepcion);
				ps_OC.setString(1, numRecepcion);
				try{
					rs2=ps_OC.executeQuery();

					status= false;

					while (rs2.next()){ // Orden de compra

						receipt_M01 = new X_E_XX_VMR_NRAM01(getCtx(), 0, get_TrxName());
					//	receipt_D01 = new X_E_XX_VMR_NRAD01(getCtx(), 0, get_TrxName());
						
						numOrden=rs2.getString("NUMORD");
						System.out.println("orden: " + numOrden);

						String SqlVerifOC="Select XX_ORDERSTATUS from c_order where C_ORDER_ID = " + rs2.getInt("C_ORDER_ID");
						PreparedStatement ps_VerifOC = DB.prepareStatement(SqlVerifOC,get_TrxName());
						//ps_VerifOC.setInt(1, rs2.getInt("C_ORDER_ID"));

						rs4 = ps_VerifOC.executeQuery();

						if (rs4.next()){
							statusOC=rs4.getString("XX_ORDERSTATUS");
							if (statusOC.equals(proforma))
							{
								status=true;

							}
						}
						
						if (status==false){

							//----------------------------Llenado de la NRAM01 con los datos de la OC
							receipt_M01.setNUMORD(numOrden);


							fechaRecepcion=rs2.getString("fechaRecepcion");
							if (fechaRecepcion!=null){
								tokens = new StringTokenizer(fechaRecepcion,"-"); 
								ano=tokens.nextToken();
								receipt_M01.setXX_ANOREC(Integer.parseInt(ano));
								mes=tokens.nextToken();
								receipt_M01.setMESREC(Integer.parseInt(mes));  
								dia=tokens.nextToken();
								receipt_M01.setXX_DIAREC(Integer.parseInt(dia));  
							}
							else
							{
								receipt_M01.setXX_ANOREC(0);
								receipt_M01.setMESREC(0);  
								receipt_M01.setXX_DIAREC(0); 
							}


							fechaFactura=rs2.getString("fechaFacturacion");
							if (fechaFactura!=null){
								tokens = new StringTokenizer(fechaFactura,"-"); 
								ano=tokens.nextToken();
								receipt_M01.setXX_AFACTU(Integer.parseInt(ano));
								mes=tokens.nextToken();
								receipt_M01.setXX_MFACTU(Integer.parseInt(mes));
								dia=tokens.nextToken();
								receipt_M01.setXX_DFACTU(Integer.parseInt(dia));  
							}
							else
							{
								
								fechaChequeo=rs2.getString("fechaChequeo");
								if (fechaChequeo!=null){
									
									tokens = new StringTokenizer(fechaChequeo,"-"); 
									ano=tokens.nextToken();
									receipt_M01.setXX_AFACTU(Integer.parseInt(ano));
									mes=tokens.nextToken();
									receipt_M01.setXX_MFACTU(Integer.parseInt(mes));  
									dia=tokens.nextToken();
									receipt_M01.setXX_DFACTU(Integer.parseInt(dia));  
									
								}else{
									
									fechaRecepcion=rs2.getString("fechaRecepcion");
							
									if (fechaRecepcion!=null){
										tokens = new StringTokenizer(fechaRecepcion,"-"); 
										ano=tokens.nextToken();
										receipt_M01.setXX_AFACTU(Integer.parseInt(ano));
										mes=tokens.nextToken();
										receipt_M01.setXX_MFACTU(Integer.parseInt(mes));  
										dia=tokens.nextToken();
										receipt_M01.setXX_DFACTU(Integer.parseInt(dia));  
									}
									else
									{
										receipt_M01.setXX_AFACTU(0);
										receipt_M01.setXX_MFACTU(0);  
										receipt_M01.setXX_DFACTU(0); 
									}
								}
							}

							fechaChequeo=rs2.getString("fechaChequeo");
							if (fechaChequeo!=null){
								tokens = new StringTokenizer(fechaChequeo,"-"); 
								ano=tokens.nextToken();
								receipt_M01.setXX_ACHEQU(Integer.parseInt(ano));
								mes=tokens.nextToken();
								receipt_M01.setXX_MCHEQU(Integer.parseInt(mes));
								dia=tokens.nextToken();
								receipt_M01.setXX_DCHEQU(Integer.parseInt(dia));  
							}
							else
							{
								receipt_M01.setXX_ACHEQU(0);
								receipt_M01.setXX_MCHEQU(0);  
								receipt_M01.setXX_DCHEQU(0); 
							}

						
							
							
							//--------------------------------Llenado de la NRAD01 con los datos de la Factura

							
							//--------------------------------Llenado de la NRAM02 con los datos de la OC y Factura	
							
							ps_Factura.setString(1, numOrden);
							String STARECAUX = "";
							
							try{
								rs7 = ps_Factura.executeQuery();
								
							while (rs7.next()){
								
							/*	receipt_M02 = new X_E_XX_VMR_NRAM02(getCtx(), 0, get_TrxName());
								receipt_M02.setNUMREC(numRecepcion);
								receipt_M02.setNUMORD(numOrden);
								receipt_M02.setGUIAEM(rs2.getString("GUIAEM"));
								receipt_M02.setCODMON(rs2.getString("CODMON"));
								receipt_M02.setNUMFAC(rs7.getString("NUMFAC"));
								receipt_M02.setFACCAM(rs2.getBigDecimal("FACCAM"));
								receipt_M02.setMONFAC(rs7.getBigDecimal("MONFAC"));
								fechaFactura=rs7.getString("fechaReg");
	 
								if (fechaFactura!=null){
									tokens = new StringTokenizer(fechaFactura,"-"); 
									ano=tokens.nextToken();
									receipt_M02.setXX_ANOREG(Integer.parseInt(ano));
									mes=tokens.nextToken();
									receipt_M02.setMESREG(Integer.parseInt(mes));
									dia=tokens.nextToken();
									receipt_M02.setDIAREG(Integer.parseInt(dia));  
								}
								else
								{
									receipt_M02.setXX_ANOREG(0);
									receipt_M02.setMESREG(0);  
									receipt_M02.setDIAREG(0); 
								}
								receipt_M02.setTIEMPE(null);
								receipt_M02.setCOEMPE(rs7.getString("COEMPE"));
								receipt_M02.setVolume(null);
								receipt_M02.setBULTO(null);
								receipt_M02.setBULREC(rs.getInt("BULREC")); //TODO FIX
								receipt_M02.setMONIMP(rs7.getBigDecimal("MONIMP"));
								receipt_M02.setTIEFAC(rs7.getString("TIEFAC"));
								receipt_M02.setSTAANU(null);
								receipt_M02.save();
								commit();
								*/
								STARECAUX = rs7.getString("INVSTATUS");
								fopago=rs7.getString("FOPAGO");
								
							}
							
							}catch (SQLException e){
								e.printStackTrace();
								System.out.println("Error al sincronizar facturas " + e);

							}catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error al sincronizar facturas " + e);	
								
							}
				
							ps_FacturaDetalle.setString(1, numOrden);									
							try{
								rs5 = ps_FacturaDetalle.executeQuery();

								monfac=new BigDecimal(0);

								while (rs5.next()){ // Factura
									receipt_D01 = new X_E_XX_VMR_NRAD01(getCtx(), 0, get_TrxName());
									numFactura=rs5.getString("NUMFAC");
									mcosfa=rs5.getBigDecimal("MCOSFA");
									canpro=rs5.getInt("CANPRO");

									factur=rs5.getString("FACTUR");
									usract=rs5.getString("USRACT");
									

									calculo=mcosfa.multiply(new BigDecimal(canpro));
									monfac=monfac.add(calculo); //Monto Total de la suma de facturas
									
									
									Descu1=rs5.getBigDecimal("DESCU1");
									if (Descu1==null){
										Descu1=new BigDecimal("0.0");}

									Descu2=rs5.getBigDecimal("DESCU2");
									if (Descu2==null){
										Descu2=new BigDecimal("0.0");}

									Descu3=rs5.getBigDecimal("DESCU3");
									if (Descu3==null){
										Descu3=new BigDecimal("0.0");}

									Descu4=rs5.getBigDecimal("DESCU4");
									if (Descu4==null){
										Descu4=new BigDecimal("0.0");}

									mcosto=rs5.getBigDecimal("TOTALINEA");

									if (mcosto!=null){

										if ((Descu1.compareTo(new BigDecimal(0)) == 0)&&(Descu2.compareTo(new BigDecimal(0)) == 0)&&(Descu3.compareTo(new BigDecimal(0)) == 0)
												&&(Descu4.compareTo(new BigDecimal(0)) == 0)){
											mondes=new BigDecimal(0);
										}	
										else{
											mondes=(mcosto.multiply(Descu1.divide(new BigDecimal(100))));
											mondes=(mondes.multiply(Descu2.divide(new BigDecimal(100))));
											mondes=(mondes.multiply(Descu3.divide(new BigDecimal(100))));
											mondes=(mondes.multiply(Descu4.divide(new BigDecimal(100))));
										}
										receipt_D01.setMCOSTO(mcosto);
										receipt_D01.setMONDES(mondes);
										receipt_D01.setMDESSF(mondes);
									}
									
	
									receipt_D01.setNUMREC(numRecepcion);
									receipt_D01.setNUMORD(numOrden);
									receipt_D01.setCODOPE(51);
									receipt_D01.setTienda(rs5.getString("TIEFAC"));
									receipt_D01.setBULTO(0);
									receipt_D01.setCODPRO(rs5.getString("CODPRO"));
								
									receipt_D01.setCANPRO(canpro);
									receipt_D01.setMCOSTO(rs5.getBigDecimal("MCOSTO"));
									receipt_D01.setMVENTA(new BigDecimal("0.0"));
									receipt_D01.setNUMTRA(0);
									receipt_D01.setCOMPRE(0);
									receipt_D01.setCODDEP(0);
									receipt_D01.setCODLIN(0);
									receipt_D01.setSECCIO(0);
									receipt_D01.setREFPRO(rs5.getString("REFPRO"));
									receipt_D01.setSTAPRO(null);
									receipt_D01.setCNTOBS(0);
									
									receipt_D01.setNUMFAC(numFactura);
									receipt_D01.setMCOSFA(mcosfa);
									
									receipt_D01.save();
									commit();
	

								}//fin del while del query de Factura
								
							}catch (SQLException e){
								e.printStackTrace();
								System.out.println("Error al sincronizar facturas " + e);

							}catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error al sincronizar facturas " + e);	
								
							}
							
		
							
							//--------------------------------Llenado de la NRAD01 con los datos de la OC	
							ps_OCDetalle.setString(1, numRecepcion);
						
							
							try{
								rs6=ps_OCDetalle.executeQuery();
								
								while (rs6.next()){
							
									receipt_D01 = new X_E_XX_VMR_NRAD01(getCtx(), 0, get_TrxName());
									receipt_D01.setNUMREC(numRecepcion);
									receipt_D01.setNUMORD(numOrden);
									receipt_D01.setCODOPE(01);
									receipt_D01.setTienda(rs6.getString("TIENDA"));
									receipt_D01.setBULTO(0);
									receipt_D01.setCODPRO(rs6.getString("CODPRO"));
									receipt_D01.setCANPRO(rs6.getInt("CANPRO"));
									
									receipt_D01.setMVENTA(rs6.getBigDecimal("MVENTA"));
									receipt_D01.setNUMTRA(0);
									receipt_D01.setCOMPRE(0);
									receipt_D01.setCODDEP(rs6.getInt("CODDEP"));
									receipt_D01.setCODLIN(rs6.getInt("CODLIN"));
									receipt_D01.setSECCIO(rs6.getInt("SECCIO"));
									receipt_D01.setREFPRO(rs6.getString("REFPRO"));
									receipt_D01.setSTAPRO(null);
									receipt_D01.setCNTOBS(rs6.getInt("CNTOBS"));
									receipt_D01.setNUMFAC(null);
									receipt_D01.setMCOSFA(new BigDecimal("0.0"));
						


									Descu1=rs6.getBigDecimal("DESCU1");
									if (Descu1==null){
										Descu1=new BigDecimal("0.0");}
		
									Descu2=rs6.getBigDecimal("DESCU2");
									if (Descu2==null){
										Descu2=new BigDecimal("0.0");}
		
									Descu3=rs6.getBigDecimal("DESCU3");
									if (Descu3==null){
										Descu3=new BigDecimal("0.0");}
		
									Descu4=rs6.getBigDecimal("DESCU4");
									if (Descu4==null){
										Descu4=new BigDecimal("0.0");}
		
									mcosto=rs6.getBigDecimal("MCOSTO");

									if (mcosto!=null){
		
										if ((Descu1.compareTo(new BigDecimal(0)) == 0)&&(Descu2.compareTo(new BigDecimal(0)) == 0)&&(Descu3.compareTo(new BigDecimal(0)) == 0)
												&&(Descu4.compareTo(new BigDecimal(0)) == 0)){
											mondes=new BigDecimal(0);
										}	
										else{
											mondes=(mcosto.multiply(Descu1.divide(new BigDecimal(100))));
											mondes=(mondes.multiply(Descu2.divide(new BigDecimal(100))));
											mondes=(mondes.multiply(Descu3.divide(new BigDecimal(100))));
											mondes=(mondes.multiply(Descu4.divide(new BigDecimal(100))));
										}
										receipt_D01.setMCOSTO(mcosto);
										receipt_D01.setMONDES(mondes);
										receipt_D01.setMDESSF(mondes);
									}

									receipt_D01.save(); //Se guarda el registro completo en la NRAD01
									commit();
								}

							}catch (SQLException e){
								e.printStackTrace();
								System.out.println("Error al sincronizar detalle OC " + e);		
							
							}catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error al sincronizar detalle OC " + e);	
								
							}
							
							//----------------------------Llenado de la NRAM01 con los datos de la Recepcion

							receipt_M01.setNUMREC(numRecepcion);
							fechaRegistro=rs.getString("fechaRegistro");

							if (fechaRegistro!=null){
								tokens = new StringTokenizer(fechaRegistro,"-"); 
								ano=tokens.nextToken();
								receipt_M01.setXX_ANOREG(Integer.parseInt(ano));
								mes=tokens.nextToken();
								receipt_M01.setMESREG(Integer.parseInt(mes));  
								dia=tokens.nextToken();
								receipt_M01.setDIAREG(Integer.parseInt(dia));  
							}
							else
							{
								receipt_M01.setXX_ANOREG(0);
								receipt_M01.setMESREG(0);  
								receipt_M01.setDIAREG(0); 
							}

							if(STARECAUX.equals("AP") && rs.getString("STAREC").equalsIgnoreCase("G")){
								receipt_M01.setSTAREC("3");
							}
							else{
								receipt_M01.setSTAREC(rs.getString("STAREC"));
							}
							
							createdByOC=rs.getString("RECEPT");
							receipt_M01.setRECEPT(createdByOC);

							
							if(rs2.getDate("fechaChequeo")!=null && rs2.getDate("fechaFacturacion")!=null){
								if(rs2.getDate("fechaChequeo").compareTo(rs2.getDate("fechaFacturacion"))==-1)
									fechaAprobacion=rs2.getString("fechaFacturacion");
								else
									fechaAprobacion=rs2.getString("fechaChequeo");
							}
							else
								fechaAprobacion = null;
							
							if (fechaAprobacion!=null){
								tokens = new StringTokenizer(fechaAprobacion,"-"); 
								ano=tokens.nextToken();
								receipt_M01.setXX_AREVIS(Integer.parseInt(ano));
								mes=tokens.nextToken();
								receipt_M01.setXX_MREVIS(Integer.parseInt(mes));
								dia=tokens.nextToken();
								receipt_M01.setXX_DREVIS(Integer.parseInt(dia));
							}
							else
							{
								receipt_M01.setXX_AREVIS(0);
								receipt_M01.setXX_MREVIS(0); 
								receipt_M01.setXX_DREVIS(0);
							}
							
							//Fecha de Recepcion
							if (rs2.getString("fechaRecepcion")!=null){
								tokens = new StringTokenizer(rs2.getString("fechaRecepcion"),"-"); 
								ano=tokens.nextToken();
								receipt_M01.setXX_ARECEP(ano);
								mes=tokens.nextToken();
								receipt_M01.setXX_MRECEP(mes); 
								dia=tokens.nextToken();
								receipt_M01.setXX_DRECEP(dia);					
							}
							else
							{
								receipt_M01.setXX_ARECEP("0");
								receipt_M01.setXX_MRECEP("0");  
								receipt_M01.setXX_DRECEP("0"); 
							}
							
							



							//--------------------------------Llenado de la NRAM01 con el resto de los datos

							receipt_M01.setCOEMPE(rs.getString("COEMPE"));
							receipt_M01.setNUMFAC(numFactura);
							receipt_M01.setMONFAC(monfac);				
							receipt_M01.setTIPREC(null);
							receipt_M01.setUSRREG(factur);
							receipt_M01.setUSRACT(usract);
							receipt_M01.setTRECEP(null);

							receipt_M01.setCODANU(0);
							receipt_M01.setTCPREV(null);
							receipt_M01.setCPREVI(null);
							receipt_M01.setTFACTU(null);
							receipt_M01.setFACTUR(factur);
							receipt_M01.setTCHEQU(null);
							receipt_M01.setXX_COCHEQ(null);
							receipt_M01.setTREVIR(null);
							receipt_M01.setXX_COREVI(null);
							receipt_M01.setXX_STAAUT(null);
							receipt_M01.setXX_USRAUT(null);
							receipt_M01.setXX_DCPREV(null);
							receipt_M01.setXX_MCPREV(0);
							receipt_M01.setXX_ACPREV(0);
							receipt_M01.setXX_TACHEQ(null);
							receipt_M01.setXX_CACHEQ(null);
							receipt_M01.setXX_STAIMP(null);
							receipt_M01.setXX_STASUS(null);
							receipt_M01.setXX_CRETEN(null);
							receipt_M01.setXX_CODPRI(null);
							receipt_M01.setXX_SFACUA(null);
							receipt_M01.setXX_FOPAGO(fopago);

							receipt_M01.save();
							commit();
							
							//--------------------------------Llenado de la NRAD01 con los datos de la Recepcion

							try{
								ps_RecDetalle.setString(1, numRecepcion);		
								rs3 = ps_RecDetalle.executeQuery();



								while(rs3.next()){    // Recepcion Detalle
									receipt_D01 = new X_E_XX_VMR_NRAD01(getCtx(), 0, get_TrxName());
									receipt_D01.setNUMREC(numRecepcion);
									receipt_D01.setNUMORD(numOrden);
									receipt_D01.setCODOPE(52);
									receipt_D01.setTienda(rs3.getString("TIENDA"));
									receipt_D01.setBULTO(0);
									receipt_D01.setCODPRO(rs3.getString("CODPRO"));
									receipt_D01.setCANPRO(rs3.getInt("CANCHQ"));
									receipt_D01.setMCOSTO(new BigDecimal("0.0"));
									receipt_D01.setMVENTA(new BigDecimal("0.0"));
									receipt_D01.setNUMTRA(0);
									receipt_D01.setCOMPRE(0);
									receipt_D01.setCODDEP(0);
									receipt_D01.setCODLIN(0);
									receipt_D01.setSECCIO(0);
									receipt_D01.setREFPRO(rs3.getString("REFPRO"));
									receipt_D01.setSTAPRO(null);
									receipt_D01.setCNTOBS(0);
									receipt_D01.setMONDES(new BigDecimal("0.0"));
									receipt_D01.setNUMFAC(null);
									receipt_D01.setMCOSFA(new BigDecimal("0.0"));
									receipt_D01.setMDESSF(new BigDecimal("0.0"));
									receipt_D01.save();
									commit();
								}//fin del while del query de Recepcion

							}catch (SQLException e){
								e.printStackTrace();
								System.out.println("Error al sincronizar recepciones " + e);		
							
							}catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error al sincronizar recepciones " + e);	
								
							}
							
							try{
								ps_RecDetalle1.setString(1, numRecepcion);		
								rs9 = ps_RecDetalle1.executeQuery();



								while(rs9.next()){    // Recepcion Detalle
									receipt_D01 = new X_E_XX_VMR_NRAD01(getCtx(), 0, get_TrxName());
									receipt_D01.setNUMREC(numRecepcion);
									receipt_D01.setNUMORD(numOrden);
									receipt_D01.setCODOPE(61);
									receipt_D01.setTienda(rs9.getString("TIENDA"));
									receipt_D01.setBULTO(0);
									receipt_D01.setCODPRO(rs9.getString("CODPRO"));
									receipt_D01.setCANPRO(rs9.getInt("CANDEV"));
									receipt_D01.setMCOSTO(new BigDecimal("0.0"));
									receipt_D01.setMVENTA(new BigDecimal("0.0"));
									receipt_D01.setNUMTRA(0);
									receipt_D01.setCOMPRE(0);
									receipt_D01.setCODDEP(0);
									receipt_D01.setCODLIN(0);
									receipt_D01.setSECCIO(0);
									receipt_D01.setREFPRO(rs9.getString("REFPRO"));
									receipt_D01.setSTAPRO(null);
									receipt_D01.setCNTOBS(0);
									receipt_D01.setMONDES(new BigDecimal("0.0"));
									receipt_D01.setNUMFAC(null);
									receipt_D01.setMCOSFA(new BigDecimal("0.0"));
									receipt_D01.setMDESSF(new BigDecimal("0.0"));
									receipt_D01.save();
									commit();
								}//fin del while del query de Recepcion

							}catch (SQLException e){
								e.printStackTrace();
								System.out.println("Error al sincronizar recepciones " + e);		
							
							}catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error al sincronizar recepciones " + e);	
							}

						} //fin del If la orden de compra no es PROFORMA


					}//fin del while que recorre las ordenes de compra

				}catch (SQLException e){
					e.printStackTrace();
					System.out.println("Error al sincronizar ordenes de compra " + e);		
				
				}catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error al sincronizar ordenes de compra " + e);	
					
				}
				
				ps_update.setString(1, numRecepcion);
				if (ps_update.executeUpdate() == 0) {
					log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
				} 
			}//---------fin de rs
			

		}catch (SQLException e){
			e.printStackTrace();
			System.out.println("Error al sincronizar la recepcion " + e);		
			/*	System.out.println("Orden numero: "+numOrden);
			System.out.println("Factura numero: "+numFactura);
			 */	System.out.println("Recepcion numero: "+numRecepcion);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al sincronizar la recepcion: " + e);	
			/*	System.out.println("Orden numero: "+numOrden);
			System.out.println("Factura numero: "+numFactura);
			 */System.out.println("Recepcion numero: "+numRecepcion);
		} finally {
			DB.closeResultSet(rs);
			DB.closeResultSet(rs2);
			DB.closeResultSet(rs3);
			DB.closeResultSet(rs4);
			DB.closeResultSet(rs5);
			DB.closeResultSet(rs6);
			DB.closeResultSet(rs7);
			DB.closeResultSet(rs9);
			DB.closeStatement(ps_update);
			DB.closeStatement(ps_RecDetalle1);
			DB.closeStatement(ps_RecDetalle);
			DB.closeStatement(ps_Rec);
			DB.closeStatement(ps_OCDetalle);
			DB.closeStatement(ps_OC);
			DB.closeStatement(ps_FacturaDetalle);
			DB.closeStatement(ps_Factura);
		}

		System.out.println("Exportando la NRAM01 al AS");
		exportE_XX_VMR_NRAM01DB2();
		System.out.println("ya exportó NRAM01");
		
		/*System.out.println("Exportando la NRAM02 al AS");
		exportE_XX_VMR_NRAM02DB2();
		System.out.println("ya exportó NRAM02");*/
		
		System.out.println("Exportando la NRAD01 al AS");
		exportE_XX_VMR_NRAD01DB2();
		System.out.println("ya exportó NRAD01");
		
		System.out.println("FIN");

		return "FIN";
	}


	protected void prepare() {

	}	
		/*
		 * Exporta la E_XX_VMR_NRAM01 de Compiere al AS/400
		 */
		public int exportE_XX_VMR_NRAM01DB2()
		{
			As400DbManager As = new As400DbManager();
			String SQL_Compiere="";
			String SQL_AS;
			int r = 0;
	        int r2 = 0;
	        
	        //Conexión con el AS/400
	        As.conectar();
	        Statement sentencia = null;
        	PreparedStatement ps_Compiere = null;
        	ResultSet rs = null;
	        try
	        {    
	        	
	        	//Borra la data de la tabla de NRAM01C en el AS/400
	        	SQL_AS = "DELETE FROM BECOFILE.NRAM01C";
	        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	            r = As.realizarSentencia(SQL_AS, sentencia);
	        	
	           if(r>=0) //Si se borró la tabla correctamente
	           {
		        	SQL_Compiere =  "SELECT XX_STATUS_SINC, NUMORD, NUMREC, XX_DIAREC, MESREC, XX_ANOREC, DIAREG, MESREG,"       
		        				  + " XX_ANOREG, STAREC, COEMPE, NVL(NUMFAC,' ') AS NUMFAC, MONFAC, NVL(USRREG, ' ') AS USRREG, " 
		        				  + " NVL(USRACT, ' ') AS USRACT, XX_DCPREV, "      
		        				  + "XX_MCPREV, XX_ACPREV, XX_DRECEP, XX_MRECEP, XX_ARECEP, XX_DFACTU, XX_MFACTU, XX_AFACTU,"      
		        				  + " XX_DCHEQU, XX_MCHEQU, XX_ACHEQU, XX_DREVIS, XX_MREVIS, "
		        				  + "XX_AREVIS,XX_FOPAGO FROM E_XX_VMR_NRAM01 ";
		        				System.out.println("SQLprimero " + SQL_Compiere);
		        	
		        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,null);
		        	rs = ps_Compiere.executeQuery();
		        	
		        	int i=0;
		        	while(rs.next()){
		        	    i++;
			            try
			            {
			            	//Query que permite hacer un registro nuevo en el AS/400
			        
			            	SQL_AS = "INSERT INTO BECOFILE.NRAM01C (NUMORD, NUMREC, DIAREC, MESREC, AÑOREC, DIAREG, MESREG,"       
		        				  + " AÑOREG, STAREC, COEMPE, NUMFAC, MONFAC, USRREG, USRACT, DCPREV,"      
		        				  + " MCPREV, ACPREV, DRECEP, MRECEP, ARECEP, DFACTU, MFACTU, AFACTU,"      
		        				  + " DCHEQU, MCHEQU, ACHEQU, DREVIS, MREVIS,"
		        				  + " AREVIS, FOPAGO) " 
					            	+ " VALUES("
				                	    + rs.getInt("NUMORD") +"," 
				                	    + rs.getInt("NUMREC") +"," 
				                	    + rs.getInt("XX_DIAREC") +"," 
				                		+ rs.getInt("MESREC") +","
				                		+ rs.getInt("XX_ANOREC") +"," 
				               			+ rs.getInt("DIAREG") +"," 
				               			+ rs.getInt("MESREG") +"," 
				               			+ rs.getInt("XX_ANOREG") +",'" 
				                		+ rs.getString("STAREC") +"'," 
				                		+ rs.getInt("COEMPE") +",'" 
				                		+ rs.getString("NUMFAC") +"'," 
				                		+ rs.getBigDecimal("MONFAC") +",'" 
				                		+ rs.getString("USRREG") +"','" 
				                		+ rs.getString("USRACT") +"'," 
				                		+ rs.getInt("XX_DCPREV") +"," 
				                		+ rs.getInt("XX_MCPREV") +"," 
				                		+ rs.getInt("XX_ACPREV") +"," 
				                		+ rs.getInt("XX_DRECEP") +"," 
				                		+ rs.getInt("XX_MRECEP") +"," 
				                		+ rs.getInt("XX_ARECEP") +"," 
				                		+ rs.getInt("XX_DFACTU") +"," 
				                		+ rs.getInt("XX_MFACTU") +"," 
				                		+ rs.getInt("XX_AFACTU") +"," 
				                		+ rs.getInt("XX_DCHEQU") +","
				                		+ rs.getInt("XX_MCHEQU") +","
				                		+ rs.getInt("XX_ACHEQU") +","
				                		+ rs.getInt("XX_DREVIS") +","
				                		+ rs.getInt("XX_MREVIS") +","
				                		+ rs.getInt("XX_AREVIS") +","
				                		+ rs.getInt("XX_FOPAGO")
				                		+ ")" ;
			            			System.out.println("SQL yo " + SQL_AS);
				                		
				                		
			            	if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 || i==81000){
								As.desconectar();
								As.conectar();
							}
							            
				            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				            r2 = As.realizarSentencia(SQL_AS, sentencia);
				            
				            //Si se inserto correctamente lo actualizo en Y
				            if(r2==1){
				            	rs.updateString("XX_STATUS_SINC", "Y");
				            	rs.updateRow();
				            }
				            
				            if(r2<0) //Si la inserción da error
				    			return r2;   	                     	       
				        }
			            catch (Exception e) {
			            	System.out.println("SQL CON ERROR" +SQL_AS);
							e.printStackTrace();
						}	           
		        	}
	        	}
	        
	        }catch (Exception e) {
	        	System.out.println("ERROR");
	        	e.printStackTrace();
			} finally {
				DB.closeStatement(sentencia);
				DB.closeStatement(ps_Compiere);
				DB.closeResultSet(rs);

			}
		
	        As.desconectar();
			return r;
			
		}
			
		
		
		/*
		 * Exporta la E_XX_VMR_NRAD01 de Compiere al AS/400
		 */
		public int exportE_XX_VMR_NRAD01DB2()
		{
			As400DbManager As = new As400DbManager();
			String SQL_Compiere="";
			String SQL_AS;
			int r = 0;
	        int r2 = 0;
	        
	        //Conexión con el AS/400
	        As.conectar();
	        Statement sentencia = null;
        	PreparedStatement ps_Compiere = null;
        	ResultSet rs = null;
	        try
	        {    
	        	
	        	//Borra la data de la tabla NRAD01C en el AS/400
	        	SQL_AS = "DELETE FROM BECOFILE.NRAD01C";
	        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	            r = As.realizarSentencia(SQL_AS, sentencia);
	        	
	           if(r>=0) //Si se borró la tabla correctamente
	           {
		        	SQL_Compiere =  "SELECT XX_STATUS_SINC, NUMREC, NUMORD, CODOPE, TIENDA, BULTO, CODPRO, CANPRO, "      
		        				  + "MCOSTO, round(MVENTA, 2) AS MVENTA, NUMTRA, COMPRE, CODDEP, CODLIN, SECCIO, REFPRO, "    
		        				  + "NVL(STAPRO, ' ') AS STAPRO, CNTOBS, MONDES, NVL(NUMFAC, ' ') AS NUMFAC, "
		        				  + "ROUND(MCOSFA, 2) AS MCOSFA, ROUND(MDESSF, 2)AS MDESSF  FROM E_XX_VMR_NRAD01";
		        	
		        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,null);
		        	rs = ps_Compiere.executeQuery();
		        	
		        	int i=0;
		        	while(rs.next()){
		        	    i++;
			            try
			            {
			            	if (rs.getBigDecimal("MCOSTO")==null)
			            		System.out.println("orden " + rs.getInt("NUMORD") + " producto " + rs.getInt("CODPRO"));
			            	//Query que permite hacer un registro nuevo en el AS/400
			            	SQL_AS = "INSERT INTO BECOFILE.NRAD01C (NUMREC, NUMORD, CODOPE, TIENDA, BULTO, CODPRO, CANPRO, "      
		        				    + "MCOSTO, MVENTA, NUMTRA, CONPRE, CODDEP, CODLIN, SECCIO, REFPRO, "    
		        				    + "STAPRO, CNTOBS, MONDES, NUMFAC, "
		        				    + "MCOSFA, MDESSF ) " 
					            	+ " VALUES("
				                	    + rs.getInt("NUMREC") +"," 
				                	    + rs.getInt("NUMORD") +",'" 
				                	    + rs.getString("CODOPE") +"'," 
				                		+ rs.getInt("TIENDA") +","
				                		+ rs.getInt("BULTO") +"," 
				               			+ rs.getInt("CODPRO") +"," 
				               			+ rs.getInt("CANPRO") +"," 
				               			+ rs.getBigDecimal("MCOSTO") +"," 
				                		+ rs.getBigDecimal("MVENTA") +"," 
				                		+ rs.getInt("NUMTRA") +","
				                		+ rs.getInt("COMPRE") +",'" 
				                		+ rs.getString("CODDEP") +"','" 
				                		+ rs.getString("CODLIN") +"','" 
				                		+ rs.getString("SECCIO") +"','" 
				                		+ rs.getString("REFPRO") +"','" 
				                		+ rs.getString("STAPRO") +"'," 
				                		+ rs.getInt("CNTOBS") +"," 
				                		+ rs.getBigDecimal("MONDES") +",'" 
				                		+ rs.getString("NUMFAC") +"'," 
				                		+ rs.getBigDecimal("MCOSFA") +"," 
				                		+ rs.getBigDecimal("MDESSF")
				                		+")" ;
				                		
				                		
							if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 || i==81000){
								As.desconectar();
								As.conectar();
							}
							            
				            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				            r2 = As.realizarSentencia(SQL_AS, sentencia);
				            
				            //Si se inserto correctamente lo actualizo en Y
				            if(r2==1){
				            	rs.updateString("XX_STATUS_SINC", "Y");
				            	rs.updateRow();
				            }
				            
				            if(r2<0) //Si la inserción da error
				    			return r2;   	                     	       
				        }
			            catch (Exception e) {
			            	System.out.println("SQL CON ERROR" +SQL_AS);
							e.printStackTrace();
						}	           
		        	}
	        	}
	        	
	        
	        }catch (Exception e) {
	        	System.out.println("ERROR");
	        	e.printStackTrace();
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps_Compiere);
				DB.closeStatement(sentencia);
			}
		
	        As.desconectar();
			return r;
			
		}
		
		public int dispatchDirectReception() throws Exception {
			
			String sql = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;		
			int fallasNRAD02=0;
			
			sql = "\nSELECT IO.DOCUMENTNO NUMREC, "+
					"\nW.VALUE TIENDA,  "+
					"\nNVL(IO.XX_INVPACKAGEQTY,0) BULBEC, "+
					"\nNVL(IO.XX_COUNTEDPACKAGESQTY,0) BULTIE, "+
					"\n'2' STATUS, "+
					"\nTO_CHAR(XX_DATESTATUSONSTORE,'DD') DIASTA, "+
					"\nTO_CHAR(XX_DATESTATUSONSTORE,'MM') MESSTA, "+
					"\nTO_CHAR(XX_DATESTATUSONSTORE,'YYYY') ANOSTA, "+
					"\n'2' STDESP, "+
					"\nTO_CHAR(XX_DATESTATUSONSTORE,'DD') DSTDEP, "+
					"\nTO_CHAR(XX_DATESTATUSONSTORE,'MM')  MSTDEP, "+
					"\nTO_CHAR(XX_DATESTATUSONSTORE,'YYYY') ASTDEP, "+
					"\n'GUIA0'||W.VALUE GUIDES, "+
					"\nSUM(IOL.PICKEDQTY) TOTPRO, "+
					"\n' ' AREACH, "+
					"\n' ' STAAUT, "+
					"\n' ' USRAUT, "+
					"\n' ' STAIMP, "+
					"\nROUND(SUM(ASI.XX_SALEPRICE*IOL.PICKEDQTY),2) MONREC "+
					"\nFROM  C_ORDER O "+
					"\nJOIN M_INOUT IO ON (O.C_ORDER_ID = IO.C_ORDER_ID) "+
					"\nJOIN M_INOUTLINE IOL ON (IO.M_INOUT_ID = IOL.M_INOUT_ID) "+
					"\nJOIN XX_VMR_ORDER PE ON (PE.C_ORDER_ID = O.C_ORDER_ID) "+
					"\nJOIN M_WAREHOUSE W ON (W.M_WAREHOUSE_ID = O.M_WAREHOUSE_ID) "+
					"\nJOIN M_ATTRIBUTESETINSTANCE ASI ON (ASI.M_ATTRIBUTESETINSTANCE_ID = IOL.M_ATTRIBUTESETINSTANCE_ID) "+
					"\nWHERE O.XX_VLO_TYPEDELIVERY = 'DD' "+
					"\nAND O.XX_ORDERSTATUS = 'CH' "+
					"\nAND O.XX_PROCESSRECEPTIONSTORE = 'Y' "+
					"\nAND PE.XX_SYNCHRONIZED = 'N' "+
					"\nGROUP BY IO.DOCUMENTNO, XX_DATESTATUSONSTORE, W.VALUE, IO.XX_INVPACKAGEQTY, IO.XX_COUNTEDPACKAGESQTY";
			
					try{
			 			pstmt = DB.prepareStatement(sql, null);					
						rs = pstmt.executeQuery();	

						while (rs.next()){
							try{
								X_E_XX_VMR_NRAD02 receipt_D02 = new X_E_XX_VMR_NRAD02(getCtx(), 0, get_TrxName());
								receipt_D02.setAREACH(rs.getString("AREACH"));
								receipt_D02.setBULBEC(rs.getInt("BULBEC"));
								receipt_D02.setBULTIE(rs.getInt("BULTIE"));
								receipt_D02.setDIASTA(rs.getInt("DIASTA"));
								receipt_D02.setMESSTA(rs.getInt("MESSTA"));
								receipt_D02.setXX_ANOSTA(rs.getInt("ANOSTA"));
								receipt_D02.setMONREC(rs.getBigDecimal("MONREC"));
								receipt_D02.setNUMREC(rs.getInt("NUMREC"));			
								receipt_D02.setStatus(rs.getString("STATUS"));
								receipt_D02.setTienda(rs.getInt("TIENDA"));
								receipt_D02.setXX_DSTDES(rs.getInt("DSTDES"));
								receipt_D02.setXX_MSTDES(rs.getInt("MSTDES"));
								receipt_D02.setXX_ASTDES(rs.getInt("ASTDES"));
								receipt_D02.setXX_GUIDES(rs.getString("GUIDES"));
								receipt_D02.setXX_STAAUT(rs.getString("STAAUT"));
								receipt_D02.setXX_STAIMP(rs.getString("STAIMP"));
								receipt_D02.setXX_STDESP(rs.getString("STDESP"));
								receipt_D02.setXX_TOTPRO(rs.getInt("TOTPRO"));
								receipt_D02.setXX_USRAUT(rs.getString("USRAUT"));

							} catch(Exception e){
								e.printStackTrace();
								fallasNRAD02++;
							}
						}
					} catch(Exception e){
						e.printStackTrace();
						throw e;
					} finally {
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}
				
				return fallasNRAD02;
		}
		
		
		/**
		 * Exporta la E_XX_VMR_NRAD02 de Compiere al AS/400
		 * @author ghuchet
		 */
		public int exportE_XX_VMR_NRAD02DB2() {
			As400DbManager As = new As400DbManager();
			String SQL_Compiere="";
			String SQL_AS;
			int r = 0;
	        int r2 = 0;
	        
	        //Conexión con el AS/400
	        As.conectar();
	        Statement sentencia = null;
        	PreparedStatement ps_Compiere = null;
        	ResultSet rs = null;
	        try
	        {    
	        	
	        	//Borra la data de la tabla NRAD02C en el AS/400
	        	SQL_AS = "DELETE FROM BECOFILE.NRAD02C";
	        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	            r = As.realizarSentencia(SQL_AS, sentencia);
	        	
	           if(r>=0) //Si se borró la tabla correctamente
	           {
		        	SQL_Compiere =  "SELECT AREACH, " +
		        			"BULBEC, " +
		        			"BULTIE, " +
		        			"DIASTA, " +
		        			"MESSTA, " +
		        			"XX_ANOSTA, " +
		        			"MONREC, " +
		        			"NUMREC, " +
		        			"STATUS, " +
		        			"TIENDA, " +
		        			"XX_DSTDES, " +
		        			"XX_MSTDES, " +
		        			"XX_ASTDES, " +
		        			"XX_GUIDES, " +
		        			"XX_STAAUT, " +
		        			"XX_STAIMP, " +
		        			"XX_STDESP, " +
		        			"XX_TOTPRO, " +
		        			"XX_USRAUT, " +
		        			"FROM E_XX_VMR_NRAD02";

		        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,null);
		        	rs = ps_Compiere.executeQuery();
		        	
		        	int i=0;
		        	while(rs.next()){
		        	    i++;
			            try
			            {
			            	//Query que permite hacer un registro nuevo en el AS/400
			            	SQL_AS = "INSERT INTO BECOFILE.NRAD02C (AREACH, BULBEC, BULTIE, DIASTA, MESSTA, AÑOSTA, "      
		        				    + "MONREC, NUMREC, STATUS, TIENDA, DSTDES, MSTDES, ASTDES, GUIDES, "    
		        				    + "STAAUT, STAIMP, STDESP, TOTPRO, USRAUT ) " 
					            	+ " VALUES("			    
				                	    + rs.getString("AREACH") +"," 
				                	    + rs.getInt("BULBEC") +",'" 
				                	    + rs.getInt("BULTIE") +"'," 
				                		+ rs.getInt("DIASTA") +"," 
				               			+ rs.getInt("MESSTA") +"," 
				               			+ rs.getInt("AÑOSTA") +"," 
				               			+ rs.getBigDecimal("MONREC") +"," 
				                		+ rs.getInt("NUMREC") +","
				                		+ rs.getString("STATUS") +"','" 
				                		+ rs.getInt("TIENDA") +","
				                		+ rs.getInt("DSTDES") +"," 
				               			+ rs.getInt("MSTDES") +","
				               			+ rs.getInt("ASTDES") +","				               			 
				               			+ rs.getString("GUIDES") +","
				                		+ rs.getString("STAAUT") +"','" 
				                		+ rs.getString("STAIMP") +"','" 
				                		+ rs.getString("STDESP") +"','"  
				                		+ rs.getInt("TOTPRO") +"," 				               
				                		+ rs.getString("USRAUT") +"'," 
				                		+")" ;
				                		
				                		
							if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 || i==81000){
								As.desconectar();
								As.conectar();
							}
							            
				            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				            r2 = As.realizarSentencia(SQL_AS, sentencia);
				            
				            //Si se inserto correctamente lo actualizo en Y
				            if(r2==1){
				            	
				           //GHUCHET -DEBO AGREGAR ESTE CAMPO A LA TABLA !!!!!!!!!!!!!!!!!
				            	rs.updateString("XX_STATUS_SINC", "Y");
				            	rs.updateRow();
				            }
				            
				            if(r2<0) //Si la inserción da error
				    			return r2;   	                     	       
				        }
			            catch (Exception e) {
			            	System.out.println("SQL CON ERROR" +SQL_AS);
							e.printStackTrace();
						}	           
		        	}
	        	}
	        	
	        
	        }catch (Exception e) {
	        	System.out.println("ERROR");
	        	e.printStackTrace();
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps_Compiere);
				DB.closeStatement(sentencia);
			}
		
	        As.desconectar();
			return r;
			
		}
}
