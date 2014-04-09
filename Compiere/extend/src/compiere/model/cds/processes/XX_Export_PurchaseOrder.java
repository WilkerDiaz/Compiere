package compiere.model.cds.processes;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_E_XX_VMR_ORCD20;
import compiere.model.cds.X_E_XX_VMR_ORCD22;
import compiere.model.cds.X_E_XX_VMR_ORCD24;
import compiere.model.cds.X_E_XX_VMR_ORCD25;
import compiere.model.cds.X_E_XX_VMR_ORCM20;
import compiere.model.cds.X_E_XX_VMR_ORCM21;

public class XX_Export_PurchaseOrder extends SvrProcess {


	protected String doIt() throws Exception {

	
		String numOrden ="";
		int orderID =0;
		
		String tienda="";
		String usuarioCrea = "";
		//String estatus="";
		//int cOrderId= 0;
		//String proveedor="";
		int consecutivo=0;
		int consecutivo2=0;

		int numRegistrosORCD20=0;
		int numRegistrosORCD22=0;
		int numRegistrosORCD24=0;
		int numRegistrosORCD25=0;
		int numRegistrosORCM20=0;
		int numRegistrosORCM21=0;

		BigDecimal Descu1=new BigDecimal("0.0");
		BigDecimal Descu2=new BigDecimal("0.0");
		BigDecimal Descu3=new BigDecimal("0.0");
		BigDecimal Descu4=new BigDecimal("0.0");
		BigDecimal mondes=new BigDecimal("0.0");
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal hundredFixed = new BigDecimal(99.99);
		//BigDecimal mcosve=new BigDecimal("0.0");
		BigDecimal mcosto=new BigDecimal("0.0");
		BigDecimal pvp=new BigDecimal("0.0");
		BigDecimal pvp_iva=new BigDecimal("0.0");
		BigDecimal iva=new BigDecimal("0.0");
		//String producto;


		X_E_XX_VMR_ORCD25 purchaseOrder25 = null;
		X_E_XX_VMR_ORCD22 purchaseOrder22 = null;
		X_E_XX_VMR_ORCD20 purchaseOrder20 = null;

		System.out.println("Borro las E");
		  //Borrado de la ORCM20
		  String sql_delete = "DELETE E_XX_VMR_ORCM20";
		  DB.executeUpdate(get_Trx(),sql_delete);
		  
		  //Borrado de la PROD01
		  String sql_delete2 = "DELETE E_XX_VMR_ORCM21";
		  DB.executeUpdate(get_Trx(),sql_delete2);
		  
		  String sql_delete3 = "DELETE E_XX_VMR_ORCD20";
		  DB.executeUpdate(get_Trx(),sql_delete3);
		  
		  String sql_delete4 = "DELETE E_XX_VMR_ORCD22";
		  DB.executeUpdate(get_Trx(),sql_delete4);
		  
		  String sql_delete5 = "DELETE E_XX_VMR_ORCD24";
		  DB.executeUpdate(get_Trx(),sql_delete5);
		  
		  String sql_delete6 = "DELETE E_XX_VMR_ORCD25";
		  DB.executeUpdate(get_Trx(),sql_delete6);
		  commit();
		//Sincronizacion de la tabla ORCM20 (Maestro de Orden de Compra) 

		String sql_update = " UPDATE C_ORDER SET XX_STATUS_SINC = 'Y' WHERE DOCUMENTNO = ?";
		//Marcar la orden como actualizada
		PreparedStatement ps_update = DB.prepareStatement(sql_update, get_TrxName());
		
		
		String sql8= "SELECT DISTINCT C.C_ORDER_ID ORDER_ID, To_Char(C.XX_APPROVALDATE,'YYYY-MM-DD') AS APPROVALDATE, C.DOCUMENTNO as NUMORDEN,CO.VALUE AS PAIS,"
					 + "To_Char(C.Created,'YYYY-MM-DD') as fechaCrea,CB.VALUE AS COEMPE, "
                     + "CASE WHEN C.XX_ORDERSTATUS = 'PRO' THEN '1' "
                     		+ "WHEN C.XX_ORDERSTATUS IS NULL THEN '1' "
                     		+ "WHEN C.XX_ORDERSTATUS = 'SIT' THEN '6' "
                            + "WHEN C.XX_ORDERSTATUS = 'AN' THEN '6' " 
                            + "WHEN C.XX_ORDERSTATUS =  'AP' THEN '4' "
                            + "WHEN C.XX_ORDERSTATUS in ('CH', 'RE') THEN '5' "
                            + "WHEN C.XX_ORDERSTATUS = 'PEN' THEN '3' "
                            + "ELSE '2' END AS STAORD, "
                            + "To_Char(C.XX_DISPATCHDATE,'YYYY-MM-DD') as fechaDispatch, "
                        	+ "To_Char(C.XX_ARRIVALDATE,'YYYY-MM-DD') as fechaArrival, "
							+ "To_Char(C.XX_ESTIMATEDDATE,'YYYY-MM-DD') as fechaEstimate, "
                     +"CASE WHEN XS.NAME = 'OTOÑO - INVIERNO' THEN 25 "
                            + "WHEN XS.NAME = 'PRIMAVERA - VERANO' THEN 23 "
                            + "END AS CODTEMP, "
                       	    + "NVL(C.XX_ESTIMATEDMARGIN,0) AS MARPRM,C.TOTALLINES AS TOTCOST,C.TOTALPVP AS TOTVEN, "
							+ "C.XX_PRODUCTQUANTITY AS TOTPRO, NVL(CR.MULTIPLYRATE,0) AS FACCOE, "
			                + "NVL(C.XX_DEFINITIVEFACTOR,0) AS FACCOP, NVL(C.XX_REPLACEMENTFACTOR, 0) AS FACCAM, "
                            + "NVL(C.XX_DISCOUNT1,0) AS DESCU1,NVL(C.XX_DISCOUNT2,0) AS DESCU2,"
                            + "NVL(C.XX_DISCOUNT3,0) AS DESCU3,NVL(C.XX_DISCOUNT4,0) AS DESCU4, "
			                + "NVL(XBR.XX_GUIDENUMBER,' ') AS GUIAEM,XD.VALUE AS CODDEP,"
			                + "CB1.VALUE AS CODCMP, NVL(XDL.VALUE,0) AS CODUBE,NVL(XSC.VALUE,0) AS CODCEM,"
			                + "NVL(XAP.VALUE,0) AS PTOLLE,CPT.VALUE AS CONPAG,NVL(XDR.VALUE,0) AS CODVIA, "
		                    + "CASE WHEN C.PAYMENTRULE = 'B' THEN '01' " 
		                    + "WHEN C.PAYMENTRULE = 'S' THEN '02' " 
		                    + "WHEN C.PAYMENTRULE = 'P' THEN '03' " 
		                    + "WHEN C.PAYMENTRULE = 'T' THEN '04' " 
		                    + "END AS FORPAG, " 
                     + "CASE WHEN CC.ISO_CODE='VEB' THEN 1 "
                            + "WHEN CC.ISO_CODE='USD' THEN 2 "
                            + "WHEN CC.ISO_CODE='EUR' THEN 6 "
                          	+ "END AS CODMON, "
                            + " NVL(XCM.VALUE,' ') AS CODANU,C.XX_TotalPVPPlusTax AS MONTOCONIVA "
                     + "FROM C_ORDER C,C_COUNTRY CO,C_BPARTNER CB,C_BPARTNER CB1," 
                     + "XX_VMA_SEASON XS, XX_VMA_BROCHURE XB, C_CONVERSION_RATE CR,XX_VLO_BOARDINGGUIDE XB," 
                     + "XX_VLO_BOARDINGGUIDE XBR,XX_VMR_DEPARTMENT XD,XX_VLO_DELIVERYLOCATION XDL, "
                     + "XX_VLO_SHIPPINGCONDITION XSC,XX_VLO_ARRIVALPORT XAP,C_PAYMENTTERM CPT, XX_VLO_DISPATCHROUTE XDR," 
                     + "C_CURRENCY CC,XX_VMR_CANCELLATIONMOTIVE XCM "
                     + "WHERE CO.C_COUNTRY_ID=C.C_COUNTRY_ID AND CB.C_BPARTNER_ID=C.C_BPARTNER_ID "
                     + "AND C.XX_VMA_SEASON_ID=XS.XX_VMA_SEASON_ID(+) " 
                     + "AND XB.XX_VMA_BROCHURE_ID(+)=C.XX_VMA_BROCHURE_ID "
                     + "AND CR.C_CONVERSION_RATE_ID(+)=C.XX_CONVERSIONRATE_ID "
                     + "AND XBR.XX_VLO_BOARDINGGUIDE_ID(+)=C.XX_VLO_BOARDINGGUIDE_ID "
                     + "AND C.XX_VMR_DEPARTMENT_ID=XD.XX_VMR_DEPARTMENT_ID AND CB1.C_BPARTNER_ID=C.XX_USERBUYER_ID "
                     + "AND XDL.XX_VLO_DELIVERYLOCATION_ID(+)=C.XX_VLO_DELIVERYLOCATION_ID "
                     + "AND XSC.XX_VLO_SHIPPINGCONDITION_ID(+)=C.XX_VLO_SHIPPINGCONDITION_ID "
                     + "AND XAP.XX_VLO_ARRIVALPORT_ID(+)=C.XX_VLO_ARRIVALPORT_ID AND CPT.C_PAYMENTTERM_ID(+)=C.C_PAYMENTTERM_ID "
                     + "AND XDR.XX_VLO_DISPATCHROUTE_ID(+)=C.XX_VLO_DISPATCHROUTE_ID AND CC.C_CURRENCY_ID(+)=C.C_CURRENCY_ID "
                     + "AND XCM.XX_VMR_CANCELLATIONMOTIVE_ID(+)=C.XX_VMR_CANCELLATIONMOTIVE_ID AND C.XX_STATUS_SINC = 'N' AND C.ISSOTRX='N' " 
                     // Incluido por CCapote
                     + "and c.xx_PoType = 'POM' ";

		X_E_XX_VMR_ORCM20 purchaseOrderM20 = null;
		PreparedStatement ps_ORCM20 = DB.prepareStatement(sql8,null);
		ResultSet rs7 = null;
		
		try {
			rs7 = ps_ORCM20.executeQuery();
			
			int i=0;
			while (rs7.next()) {
				
				System.out.println("Orden: " + rs7.getString("NUMORDEN"));
				System.out.println("i: " + i++);
				
				numRegistrosORCM20++;
				purchaseOrderM20 = new X_E_XX_VMR_ORCM20(getCtx(), 0, get_TrxName());

				pvp=rs7.getBigDecimal("TOTVEN");
				pvp_iva=rs7.getBigDecimal("MONTOCONIVA");
				iva=pvp_iva.subtract(pvp);

				numOrden=rs7.getString("NUMORDEN");
				orderID=rs7.getInt("ORDER_ID");
				purchaseOrderM20.setNUMORD(rs7.getString("NUMORDEN"));
				purchaseOrderM20.setPAIS(rs7.getString("PAIS"));
				purchaseOrderM20.setFECCRE(rs7.getString("fechaCrea"));
				purchaseOrderM20.setCOEMPE(rs7.getString("COEMPE"));
				purchaseOrderM20.setCORCON(0);
				purchaseOrderM20.setSTAORD(rs7.getString("STAORD"));
				purchaseOrderM20.setFECENT(rs7.getString("fechaArrival"));
				purchaseOrderM20.setFECENTEST(rs7.getString("fechaEstimate"));
				purchaseOrderM20.setFECDESP(rs7.getString("fechaDispatch"));
				purchaseOrderM20.setFECDESREAL(null);
				purchaseOrderM20.setFECCON(rs7.getString("APPROVALDATE"));
				purchaseOrderM20.setFECENTAGE(null);
				purchaseOrderM20.setFECENTORI(null);
				purchaseOrderM20.setCODTEMP(rs7.getString("CODTEMP"));
				purchaseOrderM20.setMARGEN(rs7.getBigDecimal("MARPRM"));
				purchaseOrderM20.setMARPRM(rs7.getBigDecimal("MARPRM"));
				purchaseOrderM20.setTOTCOST(rs7.getBigDecimal("TOTCOST"));
				purchaseOrderM20.setTOTVEN(pvp);
				purchaseOrderM20.setTOTIVA(iva);
				purchaseOrderM20.setTOTPRO(rs7.getBigDecimal("TOTPRO"));
				purchaseOrderM20.setFACCOE(rs7.getBigDecimal("FACCOE"));
				purchaseOrderM20.setFACCOP(rs7.getBigDecimal("FACCOP"));
				purchaseOrderM20.setFACCAM(rs7.getBigDecimal("FACCAM"));
				purchaseOrderM20.setDESCU1(rs7.getBigDecimal("DESCU1"));
				purchaseOrderM20.setDESCU2(rs7.getBigDecimal("DESCU2"));
				purchaseOrderM20.setDESCU3(rs7.getBigDecimal("DESCU3"));
				purchaseOrderM20.setDESCU4(rs7.getBigDecimal("DESCU4"));
				purchaseOrderM20.setGUIAEM(rs7.getString("GUIAEM"));
				purchaseOrderM20.setCODDEP(rs7.getInt("CODDEP"));
				purchaseOrderM20.setCODCMP(rs7.getString("CODCMP"));
				purchaseOrderM20.setCODUBE(rs7.getString("CODUBE"));
				purchaseOrderM20.setCODCEM(rs7.getString("CODCEM"));
				purchaseOrderM20.setPTOLLE(rs7.getString("PTOLLE"));
				purchaseOrderM20.setCONPAG(rs7.getString("CONPAG"));
				purchaseOrderM20.setCODVIA(rs7.getString("CODVIA"));
				purchaseOrderM20.setFORPAG(rs7.getInt("FORPAG"));
				purchaseOrderM20.setCODMON(rs7.getString("CODMON"));
				purchaseOrderM20.setCODANU(rs7.getString("CODANU"));
				purchaseOrderM20.setSTAMOV("");
				
				purchaseOrderM20.save();
				/*if (!purchaseOrderM20.save()) {
					log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");
				}
				else {
					System.out.println("se guardo");
				}*/

			
				// Sincronizacion de Tabla ORCM21 (Maestro de Referencias de Orden de Compra)  
				
				String sql30="SELECT "
							 +"XVP.VALUE AS REFPRO, "
							 +"C.DOCUMENTNO, XD.VALUE AS DEPARTAMENTO, XL.VALUE AS LINEA, XS.VALUE AS SECCION, "
							 +"XP.XX_UNITPURCHASEPRICE AS MCOSTO, XP.XX_SALEPRICE AS MVENTA, " 
							 + "ROUND(("
							 +"(XP.PRICEACTUAL/(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID))"
							 +"*(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID)),2) AS MCOSVE, "
							 +"CASE WHEN TC.NAME='IVA' THEN 2"
							 +"WHEN TC.NAME='EXENTO' THEN 1"
							 +"ELSE 0 "
							 +"END AS TIPIMP," 
							 +"XP.XX_TAXAMOUNT AS IMPUESTO, XP.XX_MARGIN AS MARGEN, XVP.XX_ENGLISHDESCRIPTION AS EDESC,XVP.NAME AS PRODUCTO, "
							 +"NVL(XP.XX_REBATE1,0) AS DESCU1, NVL(XP.XX_REBATE2,0) AS DESCU2, NVL(XP.XX_REBATE3,0) AS DESCU3, "
							 +"NVL(XP.XX_REBATE4,0) AS DESCU4, "
							 +"(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE XP.XX_VMR_UnitPurchase_ID = UP.XX_VMR_UNITPURCHASE_ID) AS UNICOM, "
							 +"(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE XP.XX_SaleUnit_ID = UP.XX_VMR_UNITPURCHASE_ID) AS UNIVEN, "
							 +"(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) AS CANEMC, "
							 +"(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) AS CANEMV, "
							 +"XP.XX_PACKAGEMULTIPLE AS MULEMP, "
							 +"NVL((Select MA.VALUE from M_ATTRIBUTE MA where MA.M_ATTRIBUTE_ID=XP.XX_CHARACTERISTIC1_ID),0) AS TIPCAR1, "
							 +"NVL((Select MA.VALUE from M_ATTRIBUTE MA where MA.M_ATTRIBUTE_ID=XP.XX_CHARACTERISTIC2_ID),0) AS TIPCAR2,"
							 +"NVL(lc.NAME,' ') AS CARACT, UPPER(AD.VALUE) AS USUARIO, "
							 +"XP.XX_VMR_PO_LINEREFPROV_ID AS NUMPOS "
							 +"FROM C_ORDER C,C_BPARTNER CB, XX_VMR_PO_LINEREFPROV XP, XX_VMR_DEPARTMENT XD, XX_VMR_LINE XL, XX_VMR_SECTION XS, "
							 +"C_TAXCATEGORY TC, XX_VMR_VENDORPRODREF XVP, "
		                     +"XX_VMR_LongCharacteristic lc, AD_USER AD "
							 +"WHERE C.C_ORDER_ID = ? and "
		                     +"C.C_BPARTNER_ID=CB.C_BPARTNER_ID "
		                     +"AND XP.C_ORDER_ID=C.C_ORDER_ID "
							 +"AND C.XX_VMR_DEPARTMENT_ID=XD.XX_VMR_DEPARTMENT_ID "
							 +"AND XL.XX_VMR_LINE_ID=XP.XX_VMR_LINE_ID "
		                     +"AND XS.XX_VMR_SECTION_ID=XP.XX_VMR_SECTION_ID " 
							 +"AND TC.C_TAXCATEGORY_ID(+)=XP.C_TAXCATEGORY_ID "
							 +"AND XVP.XX_VMR_VENDORPRODREF_ID=XP.XX_VMR_VENDORPRODREF_ID "
							 +"AND lc.XX_VMR_LongCharacteristic_ID(+) = XP.XX_VMR_LongCharacteristic_ID "
							 +"AND AD.AD_USER_ID(+)=XP.CREATEDBY "
                             +"AND C.XX_STATUS_SINC = 'N' AND C.ISSOTRX='N'";
	
				X_E_XX_VMR_ORCM21 purchaseOrderM21 = null;

				PreparedStatement ps_ORCM21 = DB.prepareStatement(sql30,get_TrxName());
				ResultSet rs10 = null;
				try {

					ps_ORCM21.setInt(1, orderID);
					rs10 = ps_ORCM21.executeQuery();
					
					while (rs10.next()){

						purchaseOrderM21 = new X_E_XX_VMR_ORCM21(getCtx(), 0, get_TrxName());
						numRegistrosORCM21++;

						consecutivo2=consecutivo2 + 1; 	

						Descu1=rs10.getBigDecimal("DESCU1");
						if (Descu1==null){
							Descu1=new BigDecimal("0.0");}
						
						Descu2=rs10.getBigDecimal("DESCU2");
						if (Descu2==null){
							Descu2=new BigDecimal("0.0");}
	
						Descu3=rs10.getBigDecimal("DESCU3");							
						if (Descu3==null){
							Descu3=new BigDecimal("0.0");}
						
						Descu4=rs10.getBigDecimal("DESCU4");
						if (Descu4==null){
							Descu4=new BigDecimal("0.0");}
						
						//Si el descuento es 100 le colocamos 99.99
						if(Descu1.compareTo(hundred)==0)
							Descu1 = hundredFixed;
						if(Descu2.compareTo(hundred)==0)
							Descu2 = hundredFixed;
						if(Descu3.compareTo(hundred)==0)
							Descu3 = hundredFixed;
						if(Descu4.compareTo(hundred)==0)
							Descu4 = hundredFixed;
						
						mcosto=rs10.getBigDecimal("MCOSTO");
						
						if (mcosto!=null){

							if ((Descu1.compareTo(new BigDecimal(0)) == 0)&&(Descu2.compareTo(new BigDecimal(0)) == 0)&&(Descu3.compareTo(new BigDecimal(0)) == 0)
								&&(Descu4.compareTo(new BigDecimal(0)) == 0)){
								mondes=(new BigDecimal(0));
								}	
							else{
							mondes=mcosto.subtract(mcosto.multiply(Descu1.divide(new BigDecimal(100))));
							mondes=mondes.subtract(mondes.multiply(Descu2.divide(new BigDecimal(100))));
							mondes=mondes.subtract(mondes.multiply(Descu3.divide(new BigDecimal(100))));
							mondes=mondes.subtract(mondes.multiply(Descu4.divide(new BigDecimal(100))));
							}
							
							purchaseOrderM21.setMCOSTO(mcosto);
							purchaseOrderM21.setMONDES(mondes);
							
						}
						else{
							purchaseOrderM21.setMCOSTO(null);
							purchaseOrderM21.setMONDES(null);
						}

						purchaseOrderM21.setNUMORD(numOrden);
						purchaseOrderM21.setREFPRO(rs10.getString("REFPRO"));
						purchaseOrderM21.setCODDEP(rs10.getInt("DEPARTAMENTO"));
						purchaseOrderM21.setCODLIN(rs10.getInt("LINEA"));
						purchaseOrderM21.setCODSEC(rs10.getInt("SECCION"));
						purchaseOrderM21.setNUMPOS(rs10.getInt("NUMPOS"));
						
						purchaseOrderM21.setMCOSVE(rs10.getBigDecimal("MCOSVE"));
						purchaseOrderM21.setMVENTA(rs10.getBigDecimal("MVENTA"));
						purchaseOrderM21.setTIPIMP(rs10.getString("TIPIMP"));
						purchaseOrderM21.setIMPVEN(rs10.getBigDecimal("IMPUESTO"));
						purchaseOrderM21.setMARGEN(rs10.getBigDecimal("MARGEN"));
						purchaseOrderM21.setNOMING(rs10.getString("EDESC"));
						purchaseOrderM21.setNOMPRO(rs10.getString("PRODUCTO"));
						purchaseOrderM21.setDESCU1(Descu1);
						purchaseOrderM21.setDESCU2(Descu2);
						purchaseOrderM21.setDESCU3(Descu3);
						purchaseOrderM21.setDESCU4(Descu4);
						
						purchaseOrderM21.setUNICOM(rs10.getString("UNICOM"));
						purchaseOrderM21.setUNIVEN(rs10.getString("UNIVEN"));
						purchaseOrderM21.setCANEMC(rs10.getInt("CANEMC"));
						purchaseOrderM21.setCANEMV(rs10.getInt("CANEMV"));
						purchaseOrderM21.setMULEMP(rs10.getInt("MULEMP"));
						purchaseOrderM21.setTIPCAR1(rs10.getString("TIPCAR1"));
						purchaseOrderM21.setTIPCAR2(rs10.getString("TIPCAR2"));
						purchaseOrderM21.setCARACT(rs10.getString("CARACT"));
						purchaseOrderM21.setUSRCRE(rs10.getString("USUARIO"));
						purchaseOrderM21.setUSRELIM("");
						purchaseOrderM21.setSTAREF("");
						purchaseOrderM21.save();
						/*if (!purchaseOrderM21.save())
						{
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");
						}
						else {
							System.out.println("se guardo");
						}*/
					}
					//System.out.println("Se importaron "+numRegistrosORCD20+ " ordenes a la ORCM21");

				} catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCM21 " + e);			
				}
				catch (Exception e) {
					System.out.println("Error ORCM21 CC: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(ps_ORCM21);
					DB.closeResultSet(rs10);
				}
				
				
				
				//Queries para la sincronizacion a la Tabla ORCD22 (Detalle de Referencias) 

				//Sin caracteristicas (las cantidades se toman directamente de la tabla de referencia PO_Line)

				String sql5="Select XV.VALUE AS REFPRO,XD.VALUE AS DEPARTAMENTO, XL.VALUE AS LINEA, XS.VALUE AS SECCION, "
							+"NVL((Select MAV.NAME from M_ATTRIBUTEVALUE MAV  where MAV.M_ATTRIBUTEVALUE_ID=XM.XX_VALUE1),0) AS CARAC1,"
							+"NVL((Select MA.NAME from M_ATTRIBUTEVALUE MA  where MA.M_ATTRIBUTEVALUE_ID=XM.XX_VALUE2),0) AS CARAC2,"
							+"XP.Qty AS CANTIDADC,XP.SaleQty AS CANTIDADV,XP.XX_GiftsQty AS CANTIDADOBS,"
							+"MP.VALUE AS PRODUCTO,UPPER(AU.VALUE) AS USUARIO, "
							+"XP.XX_VMR_PO_LINEREFPROV_ID AS NUMPOS "
							+"FROM C_ORDER C, XX_VMR_PO_LINEREFPROV XP, XX_VMR_VENDORPRODREF XV,XX_VMR_DEPARTMENT XD,AD_USER AU, "
							+"XX_VMR_LINE XL, XX_VMR_SECTION XS,XX_VMR_REFERENCEMATRIX XM,M_PRODUCT MP "
							+"WHERE C.C_ORDER_ID = ? AND "
							+"XP.C_ORDER_ID=C.C_ORDER_ID "
							+"AND XV.XX_VMR_VENDORPRODREF_ID(+)= XP.XX_VMR_VENDORPRODREF_ID "
							+"AND C.XX_VMR_DEPARTMENT_ID=XD.XX_VMR_DEPARTMENT_ID "
							+"AND XL.XX_VMR_LINE_ID(+)=XP.XX_VMR_LINE_ID "
							+"AND XS.XX_VMR_SECTION_ID(+)=XP.XX_VMR_SECTION_ID "
							+"AND AU.AD_USER_ID=C.CREATEDBY "
							+"AND XM.XX_VMR_PO_LINEREFPROV_ID(+)=XP.XX_VMR_PO_LINEREFPROV_ID "
							+"AND C.XX_STATUS_SINC = 'N' "
							+"AND XM.M_PRODUCT=MP.M_PRODUCT_ID(+) "
							+"AND XP.XX_WITHCHARACTERISTIC ='N' AND C.ISSOTRX='N'";

				//Con caracteristicas (las cantidades se toman de la matriz de referencia)	

				String sql6="Select XV.VALUE AS REFPRO,XD.VALUE AS DEPARTAMENTO, XL.VALUE AS LINEA, XS.VALUE AS SECCION,"
							+"(Select MAV.VALUE from M_ATTRIBUTEVALUE MAV  where MAV.M_ATTRIBUTEVALUE_ID=XM.XX_VALUE1) AS CARAC1,"
							+"NVL((Select MA.VALUE from M_ATTRIBUTEVALUE MA  where MA.M_ATTRIBUTEVALUE_ID=XM.XX_VALUE2),0) AS CARAC2,"
							+"XM.XX_QUANTITYC AS CANTIDADC,XM.XX_QUANTITYV AS CANTIDADV,XM.XX_QUANTITYO AS CANTIDADOBS,"
					        +"MP.VALUE AS PRODUCTO, UPPER(AU.VALUE) AS USUARIO, "
					        +"XP.XX_VMR_PO_LINEREFPROV_ID AS NUMPOS "
							+"FROM C_ORDER C, XX_VMR_PO_LINEREFPROV XP,XX_VMR_DEPARTMENT XD,AD_USER AU,"
							+"XX_VMR_LINE XL, XX_VMR_SECTION XS,XX_VMR_VENDORPRODREF XV,XX_VMR_REFERENCEMATRIX XM,M_PRODUCT MP "
							+"WHERE C.C_ORDER_ID= ? AND " 
					        +"C.C_ORDER_ID=XP.C_ORDER_ID "
					        +"AND XV.XX_VMR_VENDORPRODREF_ID = XP.XX_VMR_VENDORPRODREF_ID "
						    +"AND XD.XX_VMR_DEPARTMENT_ID=C.XX_VMR_DEPARTMENT_ID "
						    +"AND XP.XX_VMR_LINE_ID=XL.XX_VMR_LINE_ID AND XS.XX_VMR_SECTION_ID=XP.XX_VMR_SECTION_ID "
						    +"AND AU.AD_USER_ID=C.CREATEDBY "
						    +"AND XM.XX_VMR_PO_LINEREFPROV_ID=XP.XX_VMR_PO_LINEREFPROV_ID "
							+"AND C.XX_STATUS_SINC = 'N' and XM.M_PRODUCT=MP.M_PRODUCT_ID(+) "
							+"AND XP.XX_WITHCHARACTERISTIC ='Y' AND C.ISSOTRX='N'";

			
				
				PreparedStatement ps_sinCarac_Orcd22 = DB.prepareStatement(sql5,get_TrxName());
				PreparedStatement ps_conCarac_Orcd22 = DB.prepareStatement(sql6,get_TrxName());
				ResultSet rs4 = null;
				try{
					ps_sinCarac_Orcd22.setInt(1, orderID);
					rs4 = ps_sinCarac_Orcd22.executeQuery();
					while (rs4.next()) //Llena tabla ORCD22 sin carac
					{
						purchaseOrder22 = new X_E_XX_VMR_ORCD22(getCtx(), 0, get_TrxName());
						numRegistrosORCD22++;
						consecutivo=consecutivo + 1;
						purchaseOrder22.setNUMORD(numOrden);
						purchaseOrder22.setREFPRO(rs4.getString("REFPRO"));	
						purchaseOrder22.setCODDEP(rs4.getInt("DEPARTAMENTO"));
						purchaseOrder22.setCODLIN(rs4.getInt("LINEA"));
						purchaseOrder22.setCODSEC(rs4.getInt("SECCION"));
						purchaseOrder22.setNUMPOS(rs4.getInt("NUMPOS"));
						purchaseOrder22.setCARAC1("0");
						purchaseOrder22.setCARAC2("0");
						purchaseOrder22.setCANCOM(rs4.getInt("CANTIDADC"));
						purchaseOrder22.setCANVEN(rs4.getInt("CANTIDADV"));
						purchaseOrder22.setCANOBS(rs4.getInt("CANTIDADOBS"));
						purchaseOrder22.setCODPRO(rs4.getString("PRODUCTO"));
						purchaseOrder22.setUSRCRE(rs4.getString("USUARIO"));
						purchaseOrder22.setUSRELI("");
						purchaseOrder22.setSTAELI("");
						purchaseOrder22.save();
						/*if (!purchaseOrder22.save()){
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");
						}
						else {
							System.out.println("se guardo");
						}*/
					}
					//System.out.println("Se importaron las ordenes a la ORCD22 sin caracteristicas");

				}catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCD22 sin carac " + e);			
				}catch (Exception e) {
					System.out.println("Error ORCD22 SC: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(ps_sinCarac_Orcd22);
					DB.closeResultSet(rs4);
				}

				ResultSet rs5 = null;
				try{
					ps_conCarac_Orcd22.setInt(1, orderID);
					rs5 = ps_conCarac_Orcd22.executeQuery();

					while (rs5.next()) //Llena tabla ORCD22 con carac
					{   numRegistrosORCD22++;
						consecutivo=consecutivo + 1;
						purchaseOrder22 = new X_E_XX_VMR_ORCD22(getCtx(), 0, get_TrxName());						
						purchaseOrder22.setNUMORD(numOrden);
						purchaseOrder22.setREFPRO(rs5.getString("REFPRO"));	
						purchaseOrder22.setCODDEP(rs5.getInt("DEPARTAMENTO"));
						purchaseOrder22.setCODLIN(rs5.getInt("LINEA"));
						purchaseOrder22.setCODSEC(rs5.getInt("SECCION"));
						purchaseOrder22.setNUMPOS(rs5.getInt("NUMPOS"));
						purchaseOrder22.setCARAC1(rs5.getString("CARAC1"));
						purchaseOrder22.setCARAC2(rs5.getString("CARAC2"));
						purchaseOrder22.setCANCOM(rs5.getInt("CANTIDADC"));
						purchaseOrder22.setCANVEN(rs5.getInt("CANTIDADV"));
						purchaseOrder22.setCANOBS(rs5.getInt("CANTIDADOBS"));
						purchaseOrder22.setCODPRO(rs5.getString("PRODUCTO"));
						purchaseOrder22.setUSRCRE(rs5.getString("USUARIO"));
						purchaseOrder22.setUSRELI("");
						purchaseOrder22.setSTAELI("");
					/*	if (!purchaseOrder22.save()) {
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
						} 
						else {
							System.out.println("se guardo");
						}*/
					purchaseOrder22.save();		
					}
					//System.out.println("Se importaron: "+numRegistrosORCD22+" a la ORCD22 con caracteristicas");

				}catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCD22 con carac " + e);			
				}
				catch (Exception e) {
					System.out.println("Error ORCD22 CC: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(ps_conCarac_Orcd22);
					DB.closeResultSet(rs5);
				}

				// Sincronizacion de Tabla ORDC24 (Detalle de Distribucion porcentual por tienda) 

				String sql1 = "SELECT C.DOCUMENTNO as CODIGO, M.VALUE AS TIENDA, UPPER(AU.VALUE) AS USUARIO"
					+ " FROM C_ORDER C,M_WAREHOUSE M, AD_USER AU"
					+ " WHERE C.C_ORDER_ID = ? AND AU.AD_USER_ID=C.CREATEDBY"
					+ " AND M.M_WAREHOUSE_ID=C.M_WAREHOUSE_ID AND C.XX_STATUS_SINC = 'N'";
				
				
				PreparedStatement prst = DB.prepareStatement(sql1,get_TrxName());
				X_E_XX_VMR_ORCD24 purchaseOrder24= null;
				ResultSet rs = null;
				try {
					prst.setInt(1,orderID);
					rs = prst.executeQuery();
				
					while (rs.next()){
						numRegistrosORCD24++;
						
						purchaseOrder24 = new X_E_XX_VMR_ORCD24(getCtx(), 0, get_TrxName());

						//cOrderId= rs.getInt("CODIGO");

						//MOrder cOrder= new MOrder(getCtx(),cOrderId, get_TrxName());

						tienda= rs.getString("TIENDA");

						usuarioCrea =rs.getString("USUARIO");

						purchaseOrder24.setNUMORD(numOrden);
						purchaseOrder24.setTienda(tienda);
						purchaseOrder24.setPORDIS(100);
						purchaseOrder24.setUSRCRE(usuarioCrea);
						purchaseOrder24.setUSRELI("");
						purchaseOrder24.setSTAELI("");
						purchaseOrder24.save();	

					}
					
					//System.out.println("Se actualizaron: " +numRegistrosORCD24);			


				} catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCD24" + e);			
				}
				catch (Exception e) {
					System.out.println("Error ORCD24: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(prst);
					DB.closeResultSet(rs);
				}

				//Queries para la sincronizacion a la Tabla ORCD20 (Detalle de Orden de Compra Producto)


				//Sin caracteristicas (las cantidades se toman directamente de la tabla de referencia PO_Line)

				String sql19="Select MP.VALUE AS CODPRO, NVL(XPC.XX_PRICECONSECUTIVE,0)AS CONPRE,XM.XX_QUANTITYC AS CANCOM,"
					+ "(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) AS CANEMC,"
					+ "XP.PRICEACTUAL AS MCOSTO," 
					+ "NVL(XP.XX_REBATE1,0) AS DESCU1,NVL(XP.XX_REBATE2,0) AS DESCU2,NVL(XP.XX_REBATE3,0) AS DESCU3,NVL(XP.XX_REBATE4,0) AS DESCU4,XM.XX_QUANTITYV AS CANVEN,XM.XX_QUANTITYO AS CANOBS," 
					+ "(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) AS CANEMV,"
					+ "XP.XX_PACKAGEMULTIPLE AS MULEMP,XP.XX_MARGIN AS MARUNI,XP.XX_SALEPRICE AS MVENTA,"
					+ "(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE XP.XX_VMR_UnitPurchase_ID = UP.XX_VMR_UNITPURCHASE_ID) AS UNICOM,"
					+ "(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE XP.XX_SaleUnit_ID = UP.XX_VMR_UNITPURCHASE_ID) AS UNIVEN,"
					+ "CASE WHEN TC.NAME='IVA' THEN 2"
					+ "WHEN TC.NAME='EXENTO' THEN 1"
					+ "ELSE 0 "
					+ "END AS TIPIMP," 
					+ "XP.XX_TAXAMOUNT AS IMPVEN, UPPER(AU.VALUE) AS USRCRE, NVL(C.XX_ESTIMATEDFACTOR,0) AS FACCOE,NVL(CR.MULTIPLYRATE,0) AS FACCAM,NVL(C.XX_DEFINITIVEFACTOR,0) AS FACCOM,"
					+ "XP.XX_SALEPRICE AS MCOSVE, XD.VALUE AS CODDEP, XL.VALUE AS CODLIN, XS.VALUE AS CODSEC"
					+ " FROM C_ORDER C, XX_VMR_PO_LineRefProv XP,XX_VMR_VendorProdRef XV,C_TAXCATEGORY TC, AD_USER AU,XX_VMR_REFERENCEMATRIX XM,"
					+ "XX_VMR_PRICECONSECUTIVE XPC, C_CONVERSION_RATE CR, XX_VMR_DEPARTMENT XD,XX_VMR_LINE XL, XX_VMR_SECTION XS,M_PRODUCT MP,"
					+ "M_INOUT INO, M_INOUTLINE INL"
					+ " WHERE C.C_ORDER_ID = ? and XP.C_ORDER_ID(+)=C.C_ORDER_ID and XV.XX_VMR_VENDORPRODREF_ID(+)= XP.XX_VMR_VENDORPRODREF_ID"
					+ " and TC.C_TAXCATEGORY_ID(+)=XP.C_TAXCATEGORY_ID"
					+ " AND AU.AD_USER_ID(+)=C.CREATEDBY"
					+ " AND CR.C_CONVERSION_RATE_ID(+)=C.XX_CONVERSIONRATE_ID"
					+ " AND C.XX_VMR_DEPARTMENT_ID=XD.XX_VMR_DEPARTMENT_ID"
					+ " AND XL.XX_VMR_LINE_ID(+)=XP.XX_VMR_LINE_ID"
					+ " AND XS.XX_VMR_SECTION_ID(+)=XP.XX_VMR_SECTION_ID and C.XX_STATUS_SINC = 'N'"
					+ " and (XP.XX_CHARACTERISTIC2_ID is null and XP.XX_CHARACTERISTIC1_ID is null) AND C.ISSOTRX='N'" 
					+ " AND XM.XX_VMR_PO_LINEREFPROV_ID=XP.XX_VMR_PO_LINEREFPROV_ID AND XM.M_PRODUCT=MP.M_PRODUCT_ID" 
					+ " AND INO.C_ORDER_ID=C.C_ORDER_ID AND INL.M_INOUT_ID=INO.M_INOUT_ID "
					+ " AND INL.M_PRODUCT_ID=MP.M_PRODUCT_ID AND XPC.M_PRODUCT_ID(+)=INL.M_PRODUCT_ID "
					+ " AND XPC.M_ATTRIBUTESETINSTANCE_ID(+)=INL.M_ATTRIBUTESETINSTANCE_ID";	


				//Con caracteristicas (las cantidades se toman de la matriz de referencia)	

				String sql20="Select MP.VALUE AS CODPRO, NVL(XPC.XX_PRICECONSECUTIVE,0) AS CONPRE,XM.XX_QUANTITYC AS CANCOM,"
					+ "(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_VMR_UnitConversion_ID = UC.XX_VMR_UNITCONVERSION_ID) AS CANEMC,"
					+ "XP.PRICEACTUAL AS MCOSTO,"
					+ "NVL(XP.XX_REBATE1,0) AS DESCU1,NVL(XP.XX_REBATE2,0) AS DESCU2,NVL(XP.XX_REBATE3,0) AS DESCU3,NVL(XP.XX_REBATE4,0) AS DESCU4,XM.XX_QUANTITYV AS CANVEN,XM.XX_QUANTITYO AS CANOBS," 
					+ "(SELECT UC.XX_UNITCONVERSION FROM XX_VMR_UNITCONVERSION UC WHERE XP.XX_PiecesBySale_ID = UC.XX_VMR_UNITCONVERSION_ID) AS CANEMV,"
					+ "XP.XX_PACKAGEMULTIPLE AS MULEMP,XP.XX_MARGIN AS MARUNI,XP.XX_SALEPRICE AS MVENTA,"
					+ "(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE XP.XX_VMR_UnitPurchase_ID = UP.XX_VMR_UNITPURCHASE_ID) AS UNICOM,"
					+ "(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE XP.XX_SaleUnit_ID = UP.XX_VMR_UNITPURCHASE_ID) AS UNIVEN,"
					+ "CASE WHEN TC.NAME='IVA' THEN 2"
					+ "WHEN TC.NAME='EXENTO' THEN 1"
					+ "ELSE 0 "
					+ "END AS TIPIMP,"
					+ "XP.XX_TAXAMOUNT AS IMPVEN, AU.VALUE AS USRCRE,NVL(C.XX_ESTIMATEDFACTOR,0) AS FACCOE,NVL(CR.MULTIPLYRATE,0) AS FACCAM,NVL(C.XX_DEFINITIVEFACTOR,0) AS FACCOM,"
					+ "XP.XX_SALEPRICE AS MCOSVE, XD.VALUE AS CODDEP, XL.VALUE AS CODLIN, XS.VALUE AS CODSEC"
					+ " FROM C_ORDER C, XX_VMR_PO_LineRefProv XP,XX_VMR_VendorProdRef XV,C_TAXCATEGORY TC,AD_USER AU,M_PRODUCT MP,"
					+ "XX_VMR_PRICECONSECUTIVE XPC, C_CONVERSION_RATE CR, XX_VMR_DEPARTMENT XD,XX_VMR_LINE XL, XX_VMR_SECTION XS,"
				    + "XX_VMR_REFERENCEMATRIX XM, M_INOUT INO, M_INOUTLINE INL"
					+ " WHERE C.C_ORDER_ID = ? and C.C_ORDER_ID=XP.C_ORDER_ID and XP.XX_VMR_VENDORPRODREF_ID=XV.XX_VMR_VENDORPRODREF_ID"
					+ " and TC.C_TAXCATEGORY_ID=XP.C_TAXCATEGORY_ID"
					+ " AND AU.AD_USER_ID=C.CREATEDBY"
					+ " AND CR.C_CONVERSION_RATE_ID(+)=C.XX_CONVERSIONRATE_ID"
					+ " AND XV.XX_VMR_DEPARTMENT_ID=XD.XX_VMR_DEPARTMENT_ID"
					+ " AND XV.XX_VMR_LINE_ID=XL.XX_VMR_LINE_ID"
					+ " AND XM.XX_VMR_PO_LINEREFPROV_ID=XP.XX_VMR_PO_LINEREFPROV_ID"
					+ " AND XV.XX_VMR_SECTION_ID=XS.XX_VMR_SECTION_ID and C.XX_STATUS_SINC = 'N'"
					+ " AND XP.XX_WITHCHARACTERISTIC ='Y' AND C.ISSOTRX='N'" 
					+ " AND XM.M_PRODUCT=MP.M_PRODUCT_ID AND XM.XX_QuantityC<>0 "
					+ " AND INO.C_ORDER_ID=C.C_ORDER_ID AND INL.M_INOUT_ID=INO.M_INOUT_ID "
					+ " AND INL.M_PRODUCT_ID=MP.M_PRODUCT_ID AND XPC.M_PRODUCT_ID(+)=INL.M_PRODUCT_ID "
					+ " AND XPC.M_ATTRIBUTESETINSTANCE_ID(+)=INL.M_ATTRIBUTESETINSTANCE_ID";

				PreparedStatement ps_sinCarac_Orcd20 = DB.prepareStatement(sql19,get_TrxName());
				PreparedStatement ps_conCarac_Orcd20 = DB.prepareStatement(sql20,get_TrxName());
				ResultSet rs12 = null;
				try{
					ps_sinCarac_Orcd20.setInt(1, orderID);
					rs12 = ps_sinCarac_Orcd20.executeQuery();
					

					while (rs12.next()) //Llena tabla ORCD20 sin carac
					{
						purchaseOrder20 = new X_E_XX_VMR_ORCD20(getCtx(), 0, get_TrxName());

						numRegistrosORCD20++;

						Descu1=rs12.getBigDecimal("DESCU1");
						if (Descu1==null){
							Descu1=new BigDecimal("0.0");}
						
						Descu2=rs12.getBigDecimal("DESCU2");
						if (Descu2==null){
							Descu2=new BigDecimal("0.0");}
	
						Descu3=rs12.getBigDecimal("DESCU3");
						if (Descu3==null){
							Descu3=new BigDecimal("0.0");}
						
						Descu4=rs12.getBigDecimal("DESCU4");
						if (Descu4==null){
							Descu4=new BigDecimal("0.0");}

						//Si el descuento es 100 le colocamos 99.99
						if(Descu1.compareTo(hundred)==0)
							Descu1 = hundredFixed;
						if(Descu2.compareTo(hundred)==0)
							Descu2 = hundredFixed;
						if(Descu3.compareTo(hundred)==0)
							Descu3 = hundredFixed;
						if(Descu4.compareTo(hundred)==0)
							Descu4 = hundredFixed;
						
						mcosto=rs12.getBigDecimal("MCOSTO");
						//producto=rs12.getString("CODPRO");
						
	
						if (mcosto!=null){
	
								if ((Descu1.compareTo(new BigDecimal(0)) == 0)&&(Descu2.compareTo(new BigDecimal(0)) == 0)&&(Descu3.compareTo(new BigDecimal(0)) == 0)
								&&(Descu4.compareTo(new BigDecimal(0)) == 0)){
									mondes=(new BigDecimal(0));
								}	
								else{
									mondes=mcosto.subtract(mcosto.multiply(Descu1.divide(new BigDecimal(100))));
									mondes=mondes.subtract(mondes.multiply(Descu2.divide(new BigDecimal(100))));
									mondes=mondes.subtract(mondes.multiply(Descu3.divide(new BigDecimal(100))));
									mondes=mondes.subtract(mondes.multiply(Descu4.divide(new BigDecimal(100))));
									}
								purchaseOrderM21.setMCOSTO(mcosto);
								purchaseOrderM21.setMONDES(mondes);
						}
						
								

						purchaseOrder20.setNUMORD(numOrden);
						purchaseOrder20.setCODPRO(rs12.getString("CODPRO"));
						purchaseOrder20.setCONPRE(rs12.getBigDecimal("CONPRE"));
						purchaseOrder20.setCANCOM(rs12.getInt("CANCOM"));
						purchaseOrder20.setCANEMC(rs12.getInt("CANEMC"));
						purchaseOrder20.setMCOSTO(rs12.getBigDecimal("MCOSTO"));
						purchaseOrder20.setDESCU1(rs12.getBigDecimal("DESCU1"));
						purchaseOrder20.setDESCU2(rs12.getBigDecimal("DESCU2"));
						purchaseOrder20.setDESCU3(rs12.getBigDecimal("DESCU3"));
						purchaseOrder20.setDESCU4(rs12.getBigDecimal("DESCU4"));
						purchaseOrder20.setMONDES(mondes);
						purchaseOrder20.setCANVEN(rs12.getInt("CANVEN"));
						purchaseOrder20.setCANOBS(rs12.getInt("CANOBS"));
						purchaseOrder20.setCANEMV(rs12.getInt("CANEMV"));
						purchaseOrder20.setMULEMP(rs12.getBigDecimal("MULEMP"));
						purchaseOrder20.setMARUNI(rs12.getBigDecimal("MARUNI"));
						purchaseOrder20.setMARGEN(rs12.getBigDecimal("MARUNI"));
						purchaseOrder20.setMVENTA(rs12.getBigDecimal("MVENTA"));
						purchaseOrder20.setUNICOM(rs12.getString("UNICOM"));
						purchaseOrder20.setUNIVEN(rs12.getString("UNIVEN"));
						purchaseOrder20.setTIPIMP(rs12.getString("TIPIMP"));
						purchaseOrder20.setIMPVEN(rs12.getBigDecimal("IMPVEN"));
						purchaseOrder20.setUSRCRE(rs12.getString("USRCRE"));
						purchaseOrder20.setUSRELI(null);
						purchaseOrder20.setFACCOE(rs12.getBigDecimal("FACCOE"));
						purchaseOrder20.setFACCAM(rs12.getBigDecimal("FACCAM"));
						purchaseOrder20.setFACCOM(rs12.getBigDecimal("FACCOM"));
						purchaseOrder20.setMCOSVE(rs12.getBigDecimal("MCOSVE"));
						purchaseOrder20.setCODDEP(rs12.getInt("CODDEP"));
						purchaseOrder20.setCODLIN(rs12.getInt("CODLIN"));
						purchaseOrder20.setCODSEC(rs12.getInt("CODSEC"));
						purchaseOrder20.setSTAELI("");
						purchaseOrder20.save();
						/*if (!purchaseOrder20.save()){
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");
						}*/
					
					}
					//System.out.println("Se importaron las ordenes a la ORCD20 sin caracteristicas");					
			
				}
				catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCD20 sin carac " + e);			
				}
				catch (Exception e) {
					System.out.println("Error ORCD20 SC: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(ps_sinCarac_Orcd20);
					DB.closeResultSet(rs12);
				}
				
				ResultSet rs8 = null;
				try{

					ps_conCarac_Orcd20.setInt(1, orderID);
					rs8 = ps_conCarac_Orcd20.executeQuery();
					
					int count=0;
					while (rs8.next()) //Llena tabla ORCD20 con carac
					{
						count++;
						purchaseOrder20 = new X_E_XX_VMR_ORCD20(getCtx(), 0, get_TrxName());

						numRegistrosORCD20++;

						Descu1=rs8.getBigDecimal("DESCU1");
						if (Descu1==null){
							Descu1=new BigDecimal("0.0");}
						
						Descu2=rs8.getBigDecimal("DESCU2");
						if (Descu2==null){
							Descu2=new BigDecimal("0.0");}
	
						Descu3=rs8.getBigDecimal("DESCU3");
						if (Descu3==null){
							Descu3=new BigDecimal("0.0");}
						
						Descu4=rs8.getBigDecimal("DESCU4");
						if (Descu4==null){
							Descu4=new BigDecimal("0.0");}

						//Si el descuento es 100 le colocamos 99.99
						if(Descu1.compareTo(hundred)==0)
							Descu1 = hundredFixed;
						if(Descu2.compareTo(hundred)==0)
							Descu2 = hundredFixed;
						if(Descu3.compareTo(hundred)==0)
							Descu3 = hundredFixed;
						if(Descu4.compareTo(hundred)==0)
							Descu4 = hundredFixed;
						
						mcosto=rs8.getBigDecimal("MCOSTO");
						//producto=rs8.getString("CODPRO");
						
						if (mcosto!=null){

							if ((Descu1.compareTo(new BigDecimal(0)) == 0)&&(Descu2.compareTo(new BigDecimal(0)) == 0)&&(Descu3.compareTo(new BigDecimal(0)) == 0)
								&&(Descu4.compareTo(new BigDecimal(0)) == 0)){
								mondes=(new BigDecimal(0));
							}	
							else{
								mondes=mcosto.subtract(mcosto.multiply(Descu1.divide(new BigDecimal(100))));
								mondes=mondes.subtract(mondes.multiply(Descu2.divide(new BigDecimal(100))));
								mondes=mondes.subtract(mondes.multiply(Descu3.divide(new BigDecimal(100))));
								mondes=mondes.subtract(mondes.multiply(Descu4.divide(new BigDecimal(100))));
								}
						}

						purchaseOrder20.setNUMORD(numOrden);
						purchaseOrder20.setCODPRO(rs8.getString("CODPRO"));
						purchaseOrder20.setCONPRE(rs8.getBigDecimal("CONPRE"));
						purchaseOrder20.setCANCOM(rs8.getInt("CANCOM"));
						purchaseOrder20.setCANEMC(rs8.getInt("CANEMC"));
						purchaseOrder20.setMCOSTO(rs8.getBigDecimal("MCOSTO"));
						purchaseOrder20.setDESCU1(rs8.getBigDecimal("DESCU1"));
						purchaseOrder20.setDESCU2(rs8.getBigDecimal("DESCU2"));
						purchaseOrder20.setDESCU3(rs8.getBigDecimal("DESCU3"));
						purchaseOrder20.setDESCU4(rs8.getBigDecimal("DESCU4"));
						purchaseOrder20.setMONDES(mondes);
						purchaseOrder20.setCANVEN(rs8.getInt("CANVEN"));
						purchaseOrder20.setCANOBS(rs8.getInt("CANOBS"));
						purchaseOrder20.setCANEMV(rs8.getInt("CANEMV"));
						purchaseOrder20.setMULEMP(rs8.getBigDecimal("MULEMP"));
						purchaseOrder20.setMARUNI(rs8.getBigDecimal("MARUNI"));
						purchaseOrder20.setMARGEN(rs8.getBigDecimal("MARUNI"));
						purchaseOrder20.setMVENTA(rs8.getBigDecimal("MVENTA"));
						purchaseOrder20.setUNICOM(rs8.getString("UNICOM"));
						purchaseOrder20.setUNIVEN(rs8.getString("UNIVEN"));
						purchaseOrder20.setTIPIMP(rs8.getString("TIPIMP"));
						purchaseOrder20.setIMPVEN(rs8.getBigDecimal("IMPVEN"));
						purchaseOrder20.setUSRCRE(rs8.getString("USRCRE"));
						purchaseOrder20.setUSRELI(null);
						purchaseOrder20.setFACCOE(rs8.getBigDecimal("FACCOE"));
						purchaseOrder20.setFACCAM(rs8.getBigDecimal("FACCAM"));
						purchaseOrder20.setFACCOM(rs8.getBigDecimal("FACCOM"));
						purchaseOrder20.setMCOSVE(rs8.getBigDecimal("MCOSVE"));
						purchaseOrder20.setCODDEP(rs8.getInt("CODDEP"));
						purchaseOrder20.setCODLIN(rs8.getInt("CODLIN"));
						purchaseOrder20.setCODSEC(rs8.getInt("CODSEC"));
						purchaseOrder20.setSTAELI("");
						if (!purchaseOrder20.save()) {
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");
						}
				
					}
					//System.out.println("Se importaron: "+numRegistrosORCD20+" a la ORCD20 con caracteristicas");				
				}
				catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCD22 con carac " + e);			
				}
				catch (Exception e) {
					System.out.println("Error ORCD20 CC: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(ps_conCarac_Orcd20);
					DB.closeResultSet(rs8);
				}


				//Queries para la sincronizacion a la Tabla ORCD25 (Detalle de Distribucion por Producto)

				//Sin caracteristicas (las cantidades se toman directamente de la tabla de referencia PO_Line)

				String sql3 = "SELECT M.VALUE AS WH,MP.VALUE AS PRODUCTO,XM.XX_QUANTITYV AS CANTIDADV,XM.XX_QUANTITYO AS CANTIDADOBS, UPPER(AU.VALUE) AS USUARIO"
							+ " FROM C_ORDER C, M_WAREHOUSE M, AD_USER AU, XX_VMR_PO_LINEREFPROV XP,M_PRODUCT MP,XX_VMR_REFERENCEMATRIX XM"
							+ " WHERE C.C_ORDER_ID = ? and XP.C_ORDER_ID=C.C_ORDER_ID and AU.AD_USER_ID(+)=C.CREATEDBY AND M.M_WAREHOUSE_ID(+)=C.M_WAREHOUSE_ID"
							+ " and (XP.XX_CHARACTERISTIC2_ID is null and XP.XX_CHARACTERISTIC1_ID is null)"
							+ " AND C.ISSOTRX='N' AND MP.M_PRODUCT_ID=XM.M_PRODUCT"
							+ " AND XM.M_PRODUCT=MP.M_PRODUCT_ID AND XM.XX_VMR_PO_LINEREFPROV_ID=XP.XX_VMR_PO_LINEREFPROV_ID";

				//Con caracteristicas (las cantidades se toman de la matriz de referencia)

				String sql4= "SELECT  M.VALUE AS WH,MP.VALUE AS PRODUCTO,XM.XX_QUANTITYV AS CANTIDADV,XM.XX_QUANTITYO AS CANTIDADOBS, UPPER(AU.VALUE) AS USUARIO"
					 		 +" FROM C_ORDER C, M_WAREHOUSE M, AD_USER AU, XX_VMR_PO_LINEREFPROV XP,"
					 		 + "XX_VMR_REFERENCEMATRIX XM,M_PRODUCT MP"
					 		 + " WHERE C.C_ORDER_ID = ? and C.C_ORDER_ID=XP.C_ORDER_ID and AU.AD_USER_ID=C.CREATEDBY AND M.M_WAREHOUSE_ID=C.M_WAREHOUSE_ID"
					 		 + " AND XM.XX_VMR_PO_LINEREFPROV_ID=XP.XX_VMR_PO_LINEREFPROV_ID AND C.XX_STATUS_SINC = 'N'"
					 		 + " and (XP.XX_CHARACTERISTIC2_ID is not null or XP.XX_CHARACTERISTIC1_ID is not null) AND C.ISSOTRX='N'"
					 		 + " AND XM.M_PRODUCT=MP.M_PRODUCT_ID";
				
				

				PreparedStatement ps_sinCarac_Orcd25 = DB.prepareStatement(sql3,get_TrxName());
				PreparedStatement ps_conCarac_Orcd25 = DB.prepareStatement(sql4,get_TrxName());		
				ResultSet rs2 = null;
				try{    
					ps_sinCarac_Orcd25.setInt(1, orderID);
					rs2 = ps_sinCarac_Orcd25.executeQuery();

					while (rs2.next())//Llena tabla ORCD25 con carac
					{
						numRegistrosORCD25++;
						purchaseOrder25 = new X_E_XX_VMR_ORCD25(getCtx(), 0, get_TrxName());
						purchaseOrder25.setNUMORD(numOrden);
						purchaseOrder25.setTienda(rs2.getString("WH"));
						purchaseOrder25.setCODPRO(rs2.getString("PRODUCTO"));
						purchaseOrder25.setCANPRO(rs2.getInt("CANTIDADV"));
						purchaseOrder25.setCANREG(rs2.getInt("CANTIDADOBS"));
						purchaseOrder25.setUSRCRE(rs2.getString("USUARIO"));
						purchaseOrder25.setSTAELI("");
						purchaseOrder25.setUSRELI("");

						if (!purchaseOrder25.save()) {
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
						} 
					
					}
					//System.out.println("Se importaron las ordenes a la ORCD25 sin caracteristicas");
				
				}
				catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCD25 sin carac " + e);			
				}
				catch (Exception e) {
					System.out.println("Error ORCD25 SC: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(ps_sinCarac_Orcd25);
					DB.closeResultSet(rs2);
				}
				
				ResultSet rs3 = null;
				try{
					ps_conCarac_Orcd25.setInt(1, orderID);
					rs3 = ps_conCarac_Orcd25.executeQuery();

					while (rs3.next())//Llena tabla ORCD25 sin carac
					{
						numRegistrosORCD25++;
						purchaseOrder25 = new X_E_XX_VMR_ORCD25(getCtx(), 0, get_TrxName());
						purchaseOrder25.setNUMORD(numOrden);
						purchaseOrder25.setTienda(rs3.getString("WH"));
						purchaseOrder25.setCODPRO(rs3.getString("PRODUCTO"));
						purchaseOrder25.setCANPRO(rs3.getInt("CANTIDADV"));
						purchaseOrder25.setCANREG(rs3.getInt("CANTIDADOBS"));
						purchaseOrder25.setUSRCRE(rs3.getString("USUARIO"));
						purchaseOrder25.setSTAELI(null);
						purchaseOrder25.setUSRELI(null);

						if (!purchaseOrder25.save()) {
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
						} 
					
								

					}
					//System.out.println("Se importaron: "+numRegistrosORCD25+ " a la ORCD25 con caracteristicas");	
					
					//Actualizar como sinc					
					ps_update.setString(1, numOrden);
					if (ps_update.executeUpdate() == 0) {
						log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
					} 
					
				}catch (SQLException e){
					System.out.println("Error al sincronizar la tabla ORCD22 con carac " + e);			
				}
				catch (Exception e) {
					System.out.println("Error ORCD25 CC: " + e);	
					System.out.println("Orden numero: "+numOrden);
				} finally{
					DB.closeStatement(ps_conCarac_Orcd25);
					DB.closeResultSet(rs3);
				}
				
				commit();

			}

			//System.out.println("Se importaron: "+numRegistrosORCM20+  "ordenes a la ORCM20");
			
			/*
			 * CIERRO EL PREPARE STAMENTE DEL UPDATE
			 */
			ps_update.close();
			
			
		} catch (SQLException e){
			System.out.println("Error al sincronizar la tabla ORCM20 " + e);			
		} catch (Exception e) {
			System.out.println("Error ORCM20: " + e);	
			System.out.println("Orden numero: "+numOrden);
		} finally{
			DB.closeStatement(ps_ORCM20);
			DB.closeResultSet(rs7);
		}
		
		
		
		System.out.println("Exportando la ORCM20 al AS");
		exportE_XX_VMR_ORCM20DB2();
		System.out.println("ya exportó ORCM20");
		
		System.out.println("Exportando la ORCM21 al AS");
		exportE_XX_VMR_ORCM21DB2();
		System.out.println("ya exportó ORCM21");
		
		System.out.println("Exportando la ORCD22 al AS");
		exportE_XX_VMR_ORCD22DB2();
		System.out.println("ya exportó ORCD22");
		
		System.out.println("Exportando la ORCD24 al AS");
		exportE_XX_VMR_ORCD24DB2();
		System.out.println("ya exportó ORCD24");
		
		System.out.println("Exportando la ORCD20 al AS");
		exportE_XX_VMR_ORCD20DB2();
		System.out.println("ya exportó ORCD20");
		
		System.out.println("Exportando la ORCD25 al AS");
		exportE_XX_VMR_ORCD25DB2();
		System.out.println("ya exportó ORCD25");
		
		System.out.println("FIN!");
		
		return "FIN";
	}

	protected void prepare() {

	}
	
	/*
	 * Exporta la E_XX_VMR_ORCM20 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_ORCM20DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS;
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
    	Statement sentencia = null;
    	Statement sentencia2 = null;
        try
        {    
        	
        	//Borra la data de la tabla de Pauta de Rebajas en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.ORCM20C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere = "SELECT XX_STATUS_SINC, NUMORD, PAIS, FECCRE, COEMPE, STAORD, NVL(FECENT,'1970-01-01') FECENT, "
	        				   + "NVL(FECENTEST,'1970-01-01') FECENTEST,"    
							   + "NVL(FECDESP,'1970-01-01') FECDESP, CODTEMP, ROUND(MARGEN,3) MARGEN, ROUND(MARPRM,3) MARPRM,"
							   + " TOTCOST, TOTVEN, TOTIVA,"     
							   + "TOTPRO, ROUND(FACCOE,3) FACCOE, ROUND(FACCOP,3) FACCOP, ROUND(FACCAM,3) FACCAM, "
							   + "DESCU1, DESCU2, DESCU3, DESCU4,"   
							   + "CODDEP, CODCMP, CODUBE, CODCEM, PTOLLE, CONPAG, CODVIA, FORPAG,"       
							   + "CODMON, CODANU, NVL(FECCON,'1970-01-01') FECCON FROM E_XX_VMR_ORCM20 ";
	        	
	        	PreparedStatement ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,get_Trx());
	        	ResultSet rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.ORCM20C (NUMORD, PAIS, FECCRE, COEMPE, STAORD, FECENT, FECENTEST,"        
				            	+ "FECDESP, CODTEMP, MARGEN, MARPRM, TOTCOS, TOTVEN, TOTIVA, " 
				            	+ "TOTPRO, FACCOE, FACCOP, FACCAM, DESCU1, DESCU2, DESCU3, DESCU4,"      
				            	+ "CODDEP, CODCMP, CODUBE, CODCEM, PTOLLE, CONPAG, CODVIA, FORPAG, "      
				            	+ "CODMON, CODANU, FECAPROB) " 
				            	+ " VALUES("
			                	    + rs.getInt("NUMORD") +",'" 
			                	    + rs.getString("PAIS") +"','" 
			                	    + rs.getString("FECCRE") +"'," 
			                		+ rs.getInt("COEMPE") +",'"
			                		+ rs.getString("STAORD") +"','" 
			               			+ rs.getString("FECENT") +"','" 
			               			+ rs.getString("FECENTEST") +"','" 
			               			+ rs.getString("FECDESP") +"'," 
			                		+ rs.getInt("CODTEMP") +"," 
			                		+ rs.getBigDecimal("MARGEN") +"," 
			                		+ rs.getBigDecimal("MARPRM") +"," 
			                		+ rs.getBigDecimal("TOTCOST") +"," 
			                		+ rs.getBigDecimal("TOTVEN") +"," 
			                		+ rs.getBigDecimal("TOTIVA") +"," 
			                		+ rs.getInt("TOTPRO") +"," 
			                		+ rs.getBigDecimal("FACCOE") +"," 
			                		+ rs.getBigDecimal("FACCOP") +"," 
			                		+ rs.getBigDecimal("FACCAM") +"," 
			                		+ rs.getBigDecimal("DESCU1") +"," 
			                		+ rs.getBigDecimal("DESCU2") +"," 
			                		+ rs.getBigDecimal("DESCU3") +"," 
			                		+ rs.getBigDecimal("DESCU4") +","
		            				+ rs.getInt("CODDEP") +",";
		            	
			                		try {
			                			SQL_AS	+= rs.getInt("CODCMP") +",";
									} catch (Exception e) {
										SQL_AS	+= "0,";
									}
			                		
									SQL_AS += rs.getInt("CODUBE") +","
			                		+ rs.getInt("CODCEM") +","
			                		+ rs.getInt("PTOLLE") +","
			                		+ rs.getInt("CONPAG") +","
			                		+ rs.getInt("CODVIA") +","
			                		+ rs.getInt("FORPAG") +","
			                		+ rs.getInt("CODMON") +",'";
			                		if(rs.getString("CODANU").length()==3)
			                			SQL_AS += rs.getString("CODANU").substring(1,3);
			                		else
			                			SQL_AS += rs.getString("CODANU");
			                		
			                		SQL_AS += "','" + rs.getString("FECCON"); //USAMOS FECCON PARA FECABROB
			                		
			                		SQL_AS += "')";
			                   
			            if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 
					            			|| i==81000 || i==90000 || i==99000 || i==108000 || i==117000 || i==126000 || i==135000 
					            			|| i==144000 || i==153000 || i==162000 || i==171000){
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
			            {
			            	log.log(Level.SEVERE, "SQL CON ERROR" +SQL_AS);
			            	return r2;  
			            }
			    			 	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR" +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					} 
	        	}
        	}
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		} finally{
			DB.closeStatement(sentencia);
			As.desconectar();
		}
	
        As.desconectar();
		return r;
	}
	
	/*
	 * Exporta la E_XX_VMR_ORCM21 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_ORCM21DB2()
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
        	
        	//Borra la data de la tabla en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.ORCM21C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere = "SELECT XX_STATUS_SINC, NUMORD, REFPRO, CODDEP, CODLIN, CODSEC, NUMPOS, ROUND(MCOSTO,4) MCOSTO, ROUND(MCOSVE,2) MCOSVE,"         
							   + "ROUND(MVENTA,2) MVENTA, TIPIMP, ROUND(IMPVEN,2) IMPVEN, ROUND(MARGEN,3) MARGEN, NOMING, NOMPRO, DESCU1, DESCU2,"           
							   + "DESCU3, DESCU4, ROUND(MONDES,2) MONDES, UNICOM, UNIVEN, CANEMC, CANEMV, MULEMP,"          
							   + "TIPCAR1, TIPCAR2, CARACT, USRCRE FROM E_XX_VMR_ORCM21";
	        	
	        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,get_Trx());
	        	rs = ps_Compiere.executeQuery();
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.ORCM21C (NUMORD, REFPRO, CODDEP, CODLIN, CODSEC, NUMPO1, MCOSTO, MCOSVE,"         
							   	+ "MVENTA, TIPIMP, IMPVEN, MARGEN, NOMING, NOMPRO, DESCU1, DESCU2,"           
							   	+ "DESCU3, DESCU4, MONDES, UNICOM, UNIVEN, CANEMC, CANEMV, MULEMP,"          
							   	+ "TIPCAR1, TIPCAR2, CARACT, USRCRE) " 
				            	+ " VALUES("
			                	    + rs.getInt("NUMORD") +",'" 
			                	    + rs.getString("REFPRO") +"'," 
			                	    + rs.getInt("CODDEP") +"," 
			                		+ rs.getInt("CODLIN") +","
			                		+ rs.getInt("CODSEC") +","
			                		+ rs.getInt("NUMPOS") +","
			                		+ rs.getBigDecimal("MCOSTO") +"," 
			                		+ rs.getBigDecimal("MCOSVE") +"," 
			                		+ rs.getBigDecimal("MVENTA") +"," 
			                		+ rs.getInt("TIPIMP") +"," 
			                		+ rs.getBigDecimal("IMPVEN") +"," 
			                		+ rs.getBigDecimal("MARGEN") +",'" 
			                		+ rs.getString("NOMING").replace("'", " ") +"','" 
			               			+ rs.getString("NOMPRO").replace("'", " ") +"'," 
			               			+ rs.getBigDecimal("DESCU1") +"," 
			                		+ rs.getBigDecimal("DESCU2") +"," 
			                		+ rs.getBigDecimal("DESCU3") +"," 
			                		+ rs.getBigDecimal("DESCU4") +"," 
			                		+ rs.getBigDecimal("MONDES") +",'" 
			                		+ rs.getString("UNICOM") +"','" 
			               			+ rs.getString("UNIVEN") +"'," 
			               			+ rs.getInt("CANEMC") +"," 
			               			+ rs.getInt("CANEMV") +"," 
			               			+ rs.getInt("MULEMP") +"," 
			               			+ rs.getInt("TIPCAR1") +"," 
			               			+ rs.getInt("TIPCAR2") +",'" 
			               			+ rs.getString("CARACT").replace("'", " ") +"','"
			               			+ rs.getString("USRCRE")
			               			+ "')";
			                
		            	if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 
		            			|| i==81000 || i==90000 || i==99000 || i==108000 || i==117000 || i==126000 || i==135000 
		            			|| i==144000 || i==153000 || i==162000 || i==171000){
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
			            {
			            	log.log(Level.SEVERE, "SQL CON ERROR" +SQL_AS);
			            	return r2;  
			            }  	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR" +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}	           
	        	}
        	}
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		} finally{
			DB.closeStatement(sentencia);
			As.desconectar();
		}

		return r;
	}
	
	/*
	 * Exporta la E_XX_VMR_ORCD22 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_ORCD22DB2()
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
        	
        	//Borra la data de la tabla en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.ORCD22C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	
        	r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere = "SELECT XX_STATUS_SINC, NUMORD, REFPRO, CODDEP, CODLIN, CODSEC, NUMPOS, CARAC1, CARAC2, "     
	        				   + "CANCOM, CANVEN, CANOBS, NVL(CODPRO,0) CODPRO, USRCRE FROM E_XX_VMR_ORCD22";
	        	
	        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,get_Trx());
	        	rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.ORCD22C (NUMORD, REFPRO, CODDEP, CODLIN, CODSEC, NUMPO1, CARAC1, CARAC2,"      
		            			+ "CANCOM, CANVEN, CANOBS, CODPRO, USRCRE) " 
				            	+ " VALUES("
			                	    + rs.getInt("NUMORD") +",'" 
			                	    + rs.getString("REFPRO") +"'," 
			                	    + rs.getInt("CODDEP") +"," 
			                		+ rs.getInt("CODLIN") +","
			                		+ rs.getInt("CODSEC") +"," 
			                		+ rs.getInt("NUMPOS") +","
			                		+ rs.getInt("CARAC1") +"," 
			               			+ rs.getInt("CARAC2") +"," 
			               			+ rs.getInt("CANCOM") + "," 
			               			+ rs.getInt("CANVEN") + ","
			               			+ rs.getInt("CANOBS") +","
			                		+ rs.getInt("CODPRO") +",'"
			                		+ rs.getString("USRCRE")
			               			+ "')";
			 
		            	if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 
		            			|| i==81000 || i==90000 || i==99000 || i==108000 || i==117000 || i==126000 || i==135000 
		            			|| i==144000 || i==153000 || i==162000 || i==171000){
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
			            {
			            	log.log(Level.SEVERE, "SQL CON ERROR" +SQL_AS);
			            	return r2;  
			            }	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("i sentencia: " + i);
		            	System.out.println("SQL CON ERROR" +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}	           
	        	}
        	}
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		} finally{
			DB.closeStatement(sentencia);
			As.desconectar();
		}
        
		return r;
	}
	
	/*
	 * Exporta la E_XX_VMR_ORCD24 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_ORCD24DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS;
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
        	
    	PreparedStatement ps_Compiere = null;
    	ResultSet rs = null;
    	Statement sentencia = null;
        try
        {    
        	
        	//Borra la data de la tabla en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.ORCD24C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	
        	r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere = "SELECT XX_STATUS_SINC, NUMORD, TIENDA, PORDIS, USRCRE FROM E_XX_VMR_ORCD24";
	        	
	        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,get_Trx());
	        	rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.ORCD24C (NUMORD, TIENDA, PORDIS, USRCRE) " 
				            	+ " VALUES("
			                	    + rs.getInt("NUMORD") +"," 
			                	    + rs.getInt("TIENDA") +"," 
			                	    + rs.getBigDecimal("PORDIS") +",'" 
			                		+ rs.getString("USRCRE")
			               			+ "')";
			 
		            	if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 
		            			|| i==81000 || i==90000 || i==99000 || i==108000 || i==117000 || i==126000 || i==135000 
		            			|| i==144000 || i==153000 || i==162000 || i==171000){
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
			            {
			            	log.log(Level.SEVERE, "SQL CON ERROR" +SQL_AS);
			            	return r2;  
			            }  	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR " +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}	           
	        	}
        	}
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		} finally{
			DB.closeStatement(sentencia);
			As.desconectar();
		}
        
        As.desconectar();
		return r;
	}


	/*
	 * Exporta la E_XX_VMR_ORCD20 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_ORCD20DB2()
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
        	
        	//Borra la data de la tabla en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.ORCD20C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	
        	r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
        	   	   
	        	SQL_Compiere = "SELECT XX_STATUS_SINC, NUMORD, CODPRO, CONPRE, CODDEP, CODLIN, CODSEC, ROUND(MCOSTO,4) MCOSTO, ROUND(MCOSVE,2) MCOSVE,"         
							   + "ROUND(MVENTA,2) MVENTA, TIPIMP, ROUND(IMPVEN,2) IMPVEN, ROUND(MARGEN,3) MARGEN, "
							   + "DESCU1, DESCU2,CANCOM, CANVEN, CANOBS, ROUND(MARUNI,3) MARUNI ,"      
							   + "ROUND(FACCOE,3) FACCOE, ROUND(FACCOM,3) FACCOM, ROUND(FACCAM,3) FACCAM,"
							   + "DESCU3, DESCU4, ROUND(MONDES,2) MONDES, UNICOM, UNIVEN, CANEMC, CANEMV, MULEMP,"          
							   + "USRCRE FROM E_XX_VMR_ORCD20";
	        	
	        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,get_Trx());
	        	rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.ORCD20C (NUMORD, CODPRO, CONPRE, CANCOM, CANEMC, MCOSTO, DESCU1,"    
								 + "DESCU2, DESCU3, DESCU4, MONDES, CANVEN, CANOBS, CANEMV, MULEMP,"
								 + "MARUNI, MARGEN, MVENTA, UNICOM, UNIVEN, TIPIMP, IMPVEN, USRCRE, "     
								 + "FACCOE, FACCAM, FACCOM, MCOSVE, CODDEP, CODLIN, CODSEC) " 
				            	 + "VALUES("
			                	    + rs.getInt("NUMORD") +"," 
			                	    + rs.getInt("CODPRO") +","
			                	    + rs.getInt("CONPRE") +","
			                	    + rs.getInt("CANCOM") + "," 
			                	    + rs.getInt("CANEMC") +"," 
			                	    + rs.getBigDecimal("MCOSTO") +"," 
			                		+ rs.getBigDecimal("DESCU1") +"," 
			                		+ rs.getBigDecimal("DESCU2") +"," 
			                		+ rs.getBigDecimal("DESCU3") +"," 
			                		+ rs.getBigDecimal("DESCU4") +"," 
			                		+ rs.getBigDecimal("MONDES") +"," 
			                		+ rs.getInt("CANVEN") + ","
			               			+ rs.getInt("CANOBS") +","
			               			+ rs.getInt("CANEMV") +"," 
			               			+ rs.getInt("MULEMP") +"," 
			               			+ rs.getBigDecimal("MARUNI") +","
			               			+ rs.getBigDecimal("MARGEN") +","
			               			+ rs.getBigDecimal("MVENTA") +",'" 
			               			+ rs.getString("UNICOM") +"','" 
			               			+ rs.getString("UNIVEN") +"'," 
			               			+ rs.getInt("TIPIMP") +"," 
			               			+ rs.getBigDecimal("IMPVEN") +",'" 
			               			+ rs.getString("USRCRE") +"'," 
			               			+ rs.getBigDecimal("FACCOE") +"," 
			                		+ rs.getBigDecimal("FACCAM") +"," 
			                		+ rs.getBigDecimal("FACCOM") +"," 
			               			+ rs.getBigDecimal("MCOSVE") +"," 
			               			+ rs.getInt("CODDEP") +"," 
			                		+ rs.getInt("CODLIN") +","
			                		+ rs.getInt("CODSEC")
			               			+ ")";
			 
		            	if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 
		            			|| i==81000 || i==90000 || i==99000 || i==108000 || i==117000 || i==126000 || i==135000 
		            			|| i==144000 || i==153000 || i==162000 || i==171000){
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
			            {
			            	log.log(Level.SEVERE, "SQL CON ERROR" +SQL_AS);
			            	return r2;  
			            }   	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR " +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}	           
	        	}
        	}
        	
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		} finally{
			DB.closeStatement(sentencia);
			As.desconectar();
		}
        
        As.desconectar();
		return r;
	}

	/*
	 * Exporta la E_XX_VMR_ORCD25 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_ORCD25DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS;
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
    	PreparedStatement ps_Compiere = null;
    	ResultSet rs = null;
    	Statement sentencia = null;
    	  
        try
        {    
        	
        	//Borra la data de la tabla en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.ORCD25C";
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	
        	r = As.realizarSentencia(SQL_AS, sentencia);
        	
           if(r>=0) //Si se borró la tabla correctamente
           {
       
	        	SQL_Compiere = "SELECT XX_STATUS_SINC, NUMORD, TIENDA, CODPRO, CANPRO, CANREG, USRCRE FROM E_XX_VMR_ORCD25";
	        	
	        	ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,get_Trx());
	        	rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.ORCD25C (NUMORD, TIENDA, CODPRO, CANPRO, CANREG, USRCRE) " 
				            	+ " VALUES("
			                	    + rs.getInt("NUMORD") +"," 
			                	    + rs.getInt("TIENDA") +","
			                	    + rs.getInt("CODPRO") +","
			                	    + rs.getInt("CANPRO") +","
			                	    + rs.getInt("CANREG") +",'" 
			                		+ rs.getString("USRCRE")
			               			+ "')";
			 
		            	if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 
		            			|| i==81000 || i==90000 || i==99000 || i==108000 || i==117000 || i==126000 || i==135000 
		            			|| i==144000 || i==153000 || i==162000 || i==171000){
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
			            {
			            	log.log(Level.SEVERE, "SQL CON ERROR" +SQL_AS);
			            	return r2;  
			            }	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR " +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}	           
	        	}
        	}

        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		} finally{
			DB.closeStatement(sentencia);
			As.desconectar();
		}
        
        As.desconectar();
		return r;
	}
}








