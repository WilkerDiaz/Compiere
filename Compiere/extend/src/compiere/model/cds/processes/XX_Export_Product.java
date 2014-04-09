package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_E_XX_VMR_PROD01;
import compiere.model.cds.X_E_XX_VMR_PROM01;


public class XX_Export_Product extends SvrProcess{
	
	protected String doIt() throws Exception {
		
		System.out.println("Ahora sincronizamos PROM01");
		//Sincronizacion PROM01 Cabecera
		
		X_E_XX_VMR_PROM01 product_M01=null;
		X_E_XX_VMR_PROD01 product_D01=null;
		  
		  String producto="";
		  
		  String fechaRegistro="";
		  String fechaActualizacion="";
		  
		  String dia;
		  String mes;
		  String ano;
		  
		  String tipoProducto="";
		  String concepto="";
		  
		  StringTokenizer tokens;
		  
		  int contador=0;
		  String caract;
		  String origenPrecioConsec="";
		  
		  
		  //Borrado de la PROM01
			String sql2 = "DELETE E_XX_VMR_PROM01 where XX_STATUS_SINC = 'Y'";						
			System.out.println(sql2);
			DB.executeUpdate(null, sql2 );
		  
		  //Borrado de la PROD01
			sql2 = "DELETE E_XX_VMR_PROD01 where XX_STATUS_SINC = 'Y'";						
			System.out.println(sql2);
			DB.executeUpdate(null, sql2 );
		  
		  String sql_update = " UPDATE M_PRODUCT SET XX_STATUS_SINC = 'Y' WHERE VALUE = ?";
		  //Marcar el producto como actualizado
		  PreparedStatement ps_update = DB.prepareStatement(sql_update, get_TrxName());
		  
	  
		  String SqlProm01="Select distinct(MP.Value) CODPRO,CB.VALUE CODPROV,MP.NAME NOMPRO,XV.XX_ENGLISHDESCRIPTION NOMING,XD.VALUE AS CODDEP," 
			+ "XL.VALUE AS CODLIN,XS.VALUE AS SECCIO,B.VALUE AS MARCA,"
            + "(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE UP.XX_VMR_UNITPURCHASE_ID(+)=MP.XX_VMR_UNITPURCHASE_ID) AS UNICOM,"
            + "(SELECT UP.VALUE FROM XX_VMR_UNITPURCHASE UP WHERE UP.XX_VMR_UNITPURCHASE_ID(+)=MP.XX_SALEUNIT_ID) AS UNIVEN,"
            + " TL.NAME AS TIPETI,DD.XX_SALEPRICE AS PVPETI,TC.NAME AS TIPIMP,MP.WEIGHT AS PESO,"
            + "To_Char(MP.CREATED,'YYYY-MM-DD') as fechacrea,MP.ISACTIVE AS STAPRO,UPPER(AD.VALUE) AS USRCRE,"
            + "UPPER(AD2.VALUE) AS USRACT,XV.VALUE AS REFPRO,"
            + "PC.NAME AS TIPPRO,CV.NAME AS CONCEP,TI.NAME as TIPINV,CO.VALUE as PAIS,LC.NAME as CARACT"
            + " from M_PRODUCT MP,XX_VMR_VENDORPRODREF XV,XX_VMR_DEPARTMENT XD, XX_VMR_LINE XL, XX_VMR_SECTION XS,"
            + "XX_VMR_BRAND B,XX_VMR_TYPELABEL TL,XX_VMR_DISTRIBPRODUCTDETAIL DD,C_TAXCATEGORY TC,C_BPARTNER CB,"
            + "XX_VMR_PRODUCTCLASS PC,XX_VME_CONCEPTVALUE CV,XX_VMR_TYPEINVENTORY TI, C_COUNTRY CO,"
            + "XX_VMR_LONGCHARACTERISTIC LC,AD_USER AD,AD_USER AD2"
            + " WHERE XV.XX_VMR_VENDORPRODREF_ID(+)=MP.XX_VMR_VENDORPRODREF_ID"
            + " AND MP.XX_VMR_DEPARTMENT_ID=XD.XX_VMR_DEPARTMENT_ID"
            + " AND MP.XX_VMR_LINE_ID=XL.XX_VMR_LINE_ID AND MP.XX_VMR_SECTION_ID=XS.XX_VMR_SECTION_ID"
            + " AND B.XX_VMR_BRAND_ID(+)=MP.XX_VMR_BRAND_ID AND TL.XX_VMR_TYPELABEL_ID(+)=MP.XX_VMR_TYPELABEL_ID"
            + " AND DD.M_PRODUCT_ID(+)=MP.M_PRODUCT_ID"
            + " and TC.C_TAXCATEGORY_ID(+)=MP.C_TAXCATEGORY_ID"
            + " AND CB.C_BPARTNER_ID(+)=XV.C_BPARTNER_ID AND PC.XX_VMR_PRODUCTCLASS_ID(+)=MP.XX_VMR_PRODUCTCLASS_ID"
            + " AND CV.XX_VME_CONCEPTVALUE_ID(+)=MP.XX_VME_CONCEPTVALUE_ID"
            + " and TI.XX_VMR_TYPEINVENTORY_ID(+)=MP.XX_VMR_TYPEINVENTORY_ID AND CO.C_COUNTRY_ID(+)=MP.C_COUNTRY_ID"
            + " and LC.XX_VMR_LONGCHARACTERISTIC_ID(+)=MP.XX_VMR_LONGCHARACTERISTIC_ID" 
            + " AND AD.AD_USER_ID=MP.CREATEDBY AND AD2.AD_USER_ID=MP.UPDATEDBY AND TL.NAME IS NOT NULL AND MP.XX_STATUS_SINC='N' ";
		
		  String SqlCarac="Select MA.VALUE||MAV.VALUE as CARAC from M_ATTRIBUTE MA,M_ATTRIBUTEINSTANCE MAI," 
		  				  +"M_PRODUCT MP,M_ATTRIBUTEVALUE MAV "
			  			  +"WHERE MP.VALUE= ? "
			  			  +"AND MAI.M_ATTRIBUTESETINSTANCE_ID = MP.M_ATTRIBUTESETINSTANCE_ID "
			  			  +"AND MAI.M_ATTRIBUTEVALUE_ID = MAV.M_ATTRIBUTEVALUE_ID "
			  			  +"AND MA.M_ATTRIBUTE_ID = MAI.M_ATTRIBUTE_ID";
	
		  PreparedStatement ps_PROM01 = DB.prepareStatement(SqlProm01,null);
		  PreparedStatement ps_Carac = DB.prepareStatement(SqlCarac,get_TrxName());
	
		ResultSet rs = null;
		ResultSet rs2 = null;
		  try{
		
			rs = ps_PROM01.executeQuery();
		
			// --------------------------SINCRONIZACION DE LA TABLA PROM01----------------------------------------------
		
		
			while(rs.next()){
	
				try{
					
					product_M01=new X_E_XX_VMR_PROM01(getCtx(),0, get_TrxName());
					
					producto=rs.getString("CODPRO");
					
					product_M01.setCODPRO(producto);
					
					fechaRegistro=rs.getString("fechacrea");
					
					if (fechaRegistro!=null){
						tokens = new StringTokenizer(fechaRegistro,"-"); 
						ano=tokens.nextToken();
						product_M01.setANOING(Integer.parseInt(ano));
						mes=tokens.nextToken();
						product_M01.setMESING(Integer.parseInt(mes));  
						dia=tokens.nextToken();
						product_M01.setDIAING(Integer.parseInt(dia));  
					}
					else
					{
						product_M01.setANOING(0);
						product_M01.setMESING(0);  
						product_M01.setDIAING(0); 
					}
				
					tipoProducto=rs.getString("TIPPRO");
				
					if(tipoProducto!=null){
				
						if (tipoProducto.equalsIgnoreCase("Textil"))
						{
							product_M01.setTIPPRO("TE");
						}
				
						if (tipoProducto.equalsIgnoreCase("No Textil"))
						{
							product_M01.setTIPPRO("NT");
						}
				
						if (tipoProducto.equalsIgnoreCase( "Combinado" ))
						{
							product_M01.setTIPPRO("CO");
						}	
			
					}else{
						product_M01.setTIPPRO(null);
					}
				
				
					concepto=rs.getString("CONCEP");
					if(concepto!=null){
						if(concepto.equalsIgnoreCase("Bueno")){
							product_M01.setCONCEP("B");	
						}
						if(concepto.equalsIgnoreCase("Mejor")){
							product_M01.setCONCEP("M");	
						}
					
						if((concepto.equalsIgnoreCase("Optimo"))||(concepto.equalsIgnoreCase("Super Mejor"))){
							product_M01.setCONCEP("O");	
						}
		
					}
				
					ps_Carac.setString(1, producto);
					rs2 = ps_Carac.executeQuery();
				
					while (rs2.next()){ 
						caract=rs2.getString("CARAC");
						contador++; 	//------->Verifica la cantidad de carateristicas dinamicas del Producto
						if(contador==1){	
							product_M01.setCARAC1(caract);
						}
						if(contador==2){	
							product_M01.setCARAC2(caract);
						}
						else{
							product_M01.setCARAC2(null);
						}
						if(contador==3){	
							product_M01.setCARACT3(caract);
						}
						else{
							product_M01.setCARACT3(null);
						}
				
					}
				
					contador=0;
					
					product_M01.setNOMPRO(rs.getString("NOMPRO"));
					product_M01.setNOMING(rs.getString("NOMING"));
					product_M01.setCODDEP(rs.getString("CODDEP"));
					product_M01.setCODLIN(rs.getString("CODLIN"));
					product_M01.setSECCIO(rs.getString("SECCIO"));
					product_M01.setMARCA(rs.getString("MARCA"));
					product_M01.setUPCEAM(null);
					product_M01.setCODARA(0);
					product_M01.setUNICOM(rs.getString("UNICOM"));
					product_M01.setUNIVEN(rs.getString("UNIVEN"));
					product_M01.setTIPETI(rs.getString("TIPETI"));
					product_M01.setPVPETI(new BigDecimal(0));
					product_M01.setTIPIMP(rs.getString("TIPIMP"));
					product_M01.setTIEPER(null);
					product_M01.setCANMIN(null);
					product_M01.setCANMAX(null);
					product_M01.setPESO(rs.getBigDecimal("PESO"));
					product_M01.setUNIPES(null);
					product_M01.setALTO(null);
					product_M01.setANCHO(null);
					product_M01.setPROFUN(null);
					product_M01.setUNIDIM(null);
					product_M01.setDIAFIN(0);
					product_M01.setMESFIN(0);
					product_M01.setANOFIN(0);
					product_M01.setCAUFIN(null);
					product_M01.setSTAPRO(rs.getString("STAPRO"));
					product_M01.setUSRCRE(rs.getString("USRCRE"));
					product_M01.setUSRACT(rs.getString("USRACT"));
					product_M01.setCOEMPE(rs.getString("CODPROV"));
					product_M01.setREFPR1(null);
					product_M01.setREFPRO(rs.getString("REFPRO"));
					product_M01.setGRUPRO(null);
					product_M01.setSUBLIN(null);
					product_M01.setFRAGIL(null);
					product_M01.setTIEMAX(null);
					product_M01.setTIPINV(rs.getString("TIPINV"));
					product_M01.setINDINV(null);
					product_M01.setINDPRI(null);
					product_M01.setCOPRRE(0);
					product_M01.setTIPEMB(null);
					product_M01.setPAIS(rs.getString("PAIS"));
					product_M01.setINIMOC("S");
					product_M01.setCARACT(rs.getString("CARACT"));
					product_M01.setCARACT4(null);
					product_M01.setCARACT5(null);
					
					if(product_M01.save()){
						ps_update.setString(1, producto);
						if (ps_update.executeUpdate() == 0) {
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
						} 
					} 
				}catch (Exception e) {
					log.log(Level.SEVERE, e.getMessage());
				}
			}
		
	}catch (SQLException e){
		System.out.println("Error al sincronizar la tabla PROM01 " + e);		
	}catch (Exception e) {
		System.out.println("Error PROM01: " + e);	
	} finally{
		DB.closeStatement(ps_PROM01);
		DB.closeResultSet(rs);
		DB.closeStatement(ps_update);
		DB.closeStatement(ps_Carac);
		if(rs2!=null)
			DB.closeResultSet(rs2);
		commit();
	}
	
	
	System.out.println("Ahora sincronizamos PROD01 (generados por pedidos)");
	//------------------------------------- SINCRONIZACION DE LA TABLA PROD01-----------------------------------------
		
	String SqlProd01="SELECT (SELECT XX_VMR_DISTRIBUTIONHEADER_ID FROM XX_VMR_ORDER WHERE XX_VMR_ORDER_ID = "
					 +"(SELECT O.XX_VMR_ORDER_ID FROM XX_VMR_ORDERREQUESTDETAIL O "
					 +"WHERE O.XX_PRICECONSECUTIVE = AUX.XX_PRICECONSECUTIVE "
					 +"AND AUX.M_PRODUCT_ID = O.M_PRODUCT_ID AND ROWNUM = 1)) NUMREC,"
					 +"AUX.PRICECON_ID,AUX.CONPRE,AUX.PRECOM,AUX.CODPRO,AUX.PREVEN,AUX.fechacrea, "
					 +"AUX.fechActualizacion,AUX.USRCRE, AUX.USRACT, AUX.STATUS,AUX.ORIPRE "
					 +"FROM (select DISTINCT " 
					 +"PC.XX_PRICECONSECUTIVE ,PC.M_PRODUCT_ID,PC.XX_PRICECONSECUTIVE AS CONPRE,"
					 +"PC.XX_VMR_PRICECONSECUTIVE_ID AS PRICECON_ID,PC.XX_UNITPURCHASEPRICE AS PRECOM,"
					 +"(SELECT P.VALUE FROM M_PRODUCT P WHERE PC.M_PRODUCT_ID=P.M_PRODUCT_ID) CODPRO,"
					 +"PC.XX_SALEPRICE AS PREVEN, To_Char(PC.CREATED,'YYYY-MM-DD') as fechacrea,"
					 +"To_Char(PC.UPDATED,'YYYY-MM-DD') as fechActualizacion,"
					 +"UPPER((SELECT AD.VALUE FROM AD_USER AD WHERE AD.AD_USER_ID=ORD.CREATEDBY)) AS USRCRE,"
					 +"UPPER((SELECT AD.VALUE FROM AD_USER AD WHERE AD.AD_USER_ID=ORD.UPDATEDBY)) AS USRACT,"
					 +"PC.ISACTIVE as STATUS,"
					 +"PC.XX_CONSECUTIVEORIGIN as ORIPRE "
						+"FROM XX_VMR_PRICECONSECUTIVE PC, XX_VMR_ORDERREQUESTDETAIL ORD "
						+"WHERE PC.XX_STATUS_SINC = 'N' "
						+"AND PC.XX_CONSECUTIVEORIGIN='P' "
						+"AND PC.M_PRODUCT_ID = ORD.M_PRODUCT_ID "
						+"AND PC.XX_PRICECONSECUTIVE = ORD.XX_PRICECONSECUTIVE"
						+") AUX";

	String sql_update2 = " UPDATE XX_VMR_PRICECONSECUTIVE SET XX_STATUS_SINC = 'Y' WHERE XX_VMR_PRICECONSECUTIVE_ID = ?";
	PreparedStatement ps_update2 = DB.prepareStatement(sql_update2, get_TrxName());
	
	PreparedStatement ps_PROD01 = DB.prepareStatement(SqlProd01,get_TrxName());
	//ps_PROD01.setString(1, producto);
	ResultSet rs3 = null;
	int priceConsecutive = 0;
	
	try{
		
		rs3 = ps_PROD01.executeQuery();
			
		while (rs3.next()){
			
			try{
				priceConsecutive = rs3.getInt("PRICECON_ID");
				
				product_D01=new X_E_XX_VMR_PROD01(getCtx(),0, get_TrxName());
				fechaRegistro=rs3.getString("fechacrea");
				if (fechaRegistro!=null){
				tokens = new StringTokenizer(fechaRegistro,"-"); 
				ano=tokens.nextToken();
				product_D01.setANOREG(Integer.parseInt(ano));
				product_D01.setANOCRE(Integer.parseInt(ano));
				mes=tokens.nextToken();
				product_D01.setMESREG(Integer.parseInt(mes));  
				product_D01.setMESCRE(Integer.parseInt(mes)); 
				dia=tokens.nextToken();
				product_D01.setDIAREG(Integer.parseInt(dia));  
				product_D01.setDIACRE(Integer.parseInt(dia));  
				}
				else
				{
					product_D01.setANOREG(0);
					product_D01.setANOCRE(0);
					product_D01.setMESREG(0);  
					product_D01.setMESCRE(0); 
					product_D01.setDIAREG(0); 
					product_D01.setDIACRE(0);
				}
				
				
				fechaActualizacion=rs3.getString("fechActualizacion");
				if (fechaActualizacion!=null){
				tokens = new StringTokenizer(fechaRegistro,"-"); 
				ano=tokens.nextToken();
				product_D01.setANOACT(Integer.parseInt(ano));
				mes=tokens.nextToken();
				product_D01.setMESACT(Integer.parseInt(mes));  
				dia=tokens.nextToken();
				product_D01.setDIAACT(Integer.parseInt(dia));  
				}
				else
				{
					product_D01.setANOACT(0);
					product_D01.setMESACT(0);  
					product_D01.setDIAACT(0); 
				}
				
				product_D01.setORIPRE("REC");
				product_D01.setNUMREC(rs3.getInt("NUMREC"));
				product_D01.setCODPRO(rs3.getString("CODPRO"));
				product_D01.setCONPRE(rs3.getBigDecimal("CONPRE"));
				product_D01.setPRECOM(rs3.getBigDecimal("PRECOM"));
				product_D01.setPREVEN(rs3.getBigDecimal("PREVEN"));
				product_D01.setUSRCRE(rs3.getString("USRCRE"));
				product_D01.setUSRACT(rs3.getString("USRACT"));
				product_D01.setStatus(rs3.getString("STATUS"));
	
				if(product_D01.save()){
					ps_update2.setInt(1, priceConsecutive);
					if (ps_update2.executeUpdate() == 0) {
						log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
					} 
				}
			}
			catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		
	}catch (SQLException e){
		System.out.println("Error al sincronizar la tabla PROD01 (pedidos) " + e);	
		log.log(Level.SEVERE, "Error al sincronizar la tabla PROD01 (pedidos) " + e);
	}catch (Exception e) {
		System.out.println("Error PROD01 (pedidos): " + e);			
	} finally{
		DB.closeStatement(ps_PROD01);
		DB.closeResultSet(rs3);
		DB.closeStatement(ps_update2);
		DB.closeStatement(ps_Carac);
		commit();
	}
		
		
		System.out.println("Ahora sincronizamos PROD01 (generados por rebajas)");
		//------------------------------------- SINCRONIZACION DE LA TABLA PROD01-----------------------------------------
			
		String SqlProd01R="SELECT (SELECT VALUE FROM XX_VMR_DISCOUNTREQUEST WHERE XX_VMR_DISCOUNTREQUEST_ID = "
						  +"(SELECT O.XX_VMR_DISCOUNTREQUEST_ID FROM XX_VMR_DISCOUNTAPPLIDETAIL O "
						  +"WHERE O.XX_PRICECONSECUTIVE_ID = AUX.PRICECON_ID "
						  +"AND AUX.M_PRODUCT_ID = O.M_PRODUCT_ID AND ROWNUM = 1)) NUMREC,"
						  +"AUX.PRICECON_ID,AUX.CONPRE,AUX.PRECOM,AUX.CODPRO,AUX.PREVEN,AUX.fechacrea," 
						  +"AUX.fechActualizacion,AUX.USRCRE, AUX.USRACT, AUX.STATUS,AUX.ORIPRE "
						  +"FROM (select DISTINCT  "
						  +"PC.XX_PRICECONSECUTIVE ,PC.M_PRODUCT_ID,PC.XX_PRICECONSECUTIVE AS CONPRE,"
						  +"PC.XX_VMR_PRICECONSECUTIVE_ID AS PRICECON_ID,PC.XX_UNITPURCHASEPRICE AS PRECOM,"
						  +"(SELECT P.VALUE FROM M_PRODUCT P WHERE PC.M_PRODUCT_ID=P.M_PRODUCT_ID) CODPRO,"
						  +"PC.XX_SALEPRICE AS PREVEN,To_Char(PC.CREATED,'YYYY-MM-DD') as fechacrea,"
						  +"To_Char(PC.UPDATED,'YYYY-MM-DD') as fechActualizacion,"
						  +"UPPER((SELECT AD.VALUE FROM AD_USER AD WHERE AD.AD_USER_ID=DAD.CREATEDBY)) AS USRCRE,"
						  +"UPPER((SELECT AD.VALUE FROM AD_USER AD WHERE AD.AD_USER_ID=DAD.UPDATEDBY)) AS USRACT,"
						  +"PC.ISACTIVE as STATUS,"
						  +"PC.XX_CONSECUTIVEORIGIN as ORIPRE " 
						  +"FROM XX_VMR_PRICECONSECUTIVE PC,  xx_vmr_discountapplidetail DAD "
						  +"WHERE PC.XX_STATUS_SINC = 'N' "
                          +"AND PC.XX_CONSECUTIVEORIGIN='R' "
					      +"AND PC.M_PRODUCT_ID = dad.M_PRODUCT_ID " 
						  +") AUX";

		String sql_update3 = " UPDATE XX_VMR_PRICECONSECUTIVE SET XX_STATUS_SINC = 'Y' WHERE XX_VMR_PRICECONSECUTIVE_ID = ?";
		PreparedStatement ps_update3 = DB.prepareStatement(sql_update3, get_TrxName());
			
		PreparedStatement ps_PROD01_R = DB.prepareStatement(SqlProd01R,get_TrxName());
		//ps_PROD01.setString(1, producto);
		ResultSet rs3_R = null;
		int priceConsecutive_R = 0;
		
		try{
			
			rs3_R = ps_PROD01_R.executeQuery();
				
			while (rs3_R.next()){
				
				try{
					priceConsecutive_R = rs3_R.getInt("PRICECON_ID");
					
					product_D01=new X_E_XX_VMR_PROD01(getCtx(),0, get_TrxName());
					fechaRegistro=rs3_R.getString("fechacrea");
					if (fechaRegistro!=null){
					tokens = new StringTokenizer(fechaRegistro,"-"); 
					ano=tokens.nextToken();
					product_D01.setANOREG(Integer.parseInt(ano));
					product_D01.setANOCRE(Integer.parseInt(ano));
					mes=tokens.nextToken();
					product_D01.setMESREG(Integer.parseInt(mes));  
					product_D01.setMESCRE(Integer.parseInt(mes)); 
					dia=tokens.nextToken();
					product_D01.setDIAREG(Integer.parseInt(dia));  
					product_D01.setDIACRE(Integer.parseInt(dia));  
					}
					else
					{
						product_D01.setANOREG(0);
						product_D01.setANOCRE(0);
						product_D01.setMESREG(0);  
						product_D01.setMESCRE(0); 
						product_D01.setDIAREG(0); 
						product_D01.setDIACRE(0);
					}
					
					
					fechaActualizacion=rs3_R.getString("fechActualizacion");
					if (fechaActualizacion!=null){
					tokens = new StringTokenizer(fechaRegistro,"-"); 
					ano=tokens.nextToken();
					product_D01.setANOACT(Integer.parseInt(ano));
					mes=tokens.nextToken();
					product_D01.setMESACT(Integer.parseInt(mes));  
					dia=tokens.nextToken();
					product_D01.setDIAACT(Integer.parseInt(dia));  
					}
					else
					{
						product_D01.setANOACT(0);
						product_D01.setMESACT(0);  
						product_D01.setDIAACT(0); 
					}
					
					product_D01.setORIPRE("REB");
					product_D01.setNUMREC(rs3_R.getInt("NUMREC"));
					product_D01.setCODPRO(rs3_R.getString("CODPRO"));
					product_D01.setCONPRE(rs3_R.getBigDecimal("CONPRE"));
					product_D01.setPRECOM(rs3_R.getBigDecimal("PRECOM"));
					product_D01.setPREVEN(rs3_R.getBigDecimal("PREVEN"));
					product_D01.setUSRCRE(rs3_R.getString("USRCRE"));
					product_D01.setUSRACT(rs3_R.getString("USRACT"));
					product_D01.setStatus(rs3_R.getString("STATUS"));
	
					if(product_D01.save()){
						ps_update3.setInt(1, priceConsecutive_R);
						if (ps_update3.executeUpdate() == 0) {
							log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
						} 
					}
				}catch (Exception e) {
					log.log(Level.SEVERE, e.getMessage());
				}
			}
			
		}catch (SQLException e){
			System.out.println("Error al sincronizar la tabla PROD01 (rebaja) " + e);	
			log.log(Level.SEVERE, "Error al sincronizar la tabla PROD01 (rebaja) " + e);
		}catch (Exception e) {
			System.out.println("Error PROD01 (rebaja): " + e);			
		} finally{
			DB.closeStatement(ps_update3);
			DB.closeResultSet(rs3);
			DB.closeStatement(ps_PROD01_R);
			commit();
		}

		commit();
		//Ahora pasamos los registros al AS400
		
		System.out.println("Sincronizamos al AS la PROM01");
		int returns = exportE_XX_VMR_PROM01DB2();
		
		if(returns<0){
			System.out.println("Error al sincronizar AS LA PROM01");
		}
		
		System.out.println("Sincronizamos al AS la PROD01");
		int returns2 = exportE_XX_VMR_PROD01DB2();
		if(returns2<0){
			System.out.println("Error al sincronizar AS LA PROD01");
		}
		
		//exportE_XX_VMR_PROD01DB2();
		System.out.println("Fin de la ejecucion");
		return "FIN";
	
	}

	/*
	 * Exporta la E_XX_VMR_PROM01 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_PROM01DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS = "";
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
        	
        try
        {    
        	
        	//Borra la data de la tabla de Pauta de Rebajas en el AS/400
        	//TODO
        	// Esta parte fue comentada debido a que ahora el borrado de la tabla se hace en el as
        	// En caso de que se descomente, se debe borrar el r=1; que esta en la linea de abajo
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
/**        	SQL_AS = "DELETE FROM BECOFILE.PROM01C";
            r = As.realizarSentencia(SQL_AS, sentencia);
   */
           r = 1;	
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere = "SELECT XX_STATUS_SINC,CODPRO, NVL(NOMPRO,' ') as NOMPRO,"
		                		 +  "NVL(NOMING,' ') as NOMING, CODDEP, CODLIN, SECCIO, MARCA, UPCEAM, CODARA, UNICOM,"
		                		 +  "UNIVEN, TIPETI, PVPETI, TIPIMP, TIEPER, CANMIN, CANMAX, PESO, "    
		                		 +  "ALTO, ANCHO, PROFUN, UNIDIM, DIAING, MESING, ANOING, "  
		                		 +  "DIAFIN, MESFIN, ANOFIN, CAUFIN, STAPRO, USRCRE, USRACT, COEMPE,"   
		                		 +  "REFPR1, REFPRO, NVL(GRUPRO,' ') as GRUPRO, NVL(SUBLIN,' ') SUBLIN, NVL(FRAGIL,' ') FRAGIL, TIEMAX, TIPPRO, NVL(CONCEP,' ') CONCEP, "  
		                		 + "TIPINV, INDINV, INDPRI, COPRRE, TIPEMB, NVL(PAIS,' ') PAIS, NVL(INIMOC,' ') INIMOC, NVL(CARACT,' ') CARACT, "
		                		 + "NVL(CARAC1,' ') as CARAC1, NVL(CARAC2,' ') as CARAC2, NVL(CARACT3,' ') CARACT3,  E_XX_VMR_PROM01_ID FROM "
		                  		 + "E_XX_VMR_PROM01";
	        	
	        	PreparedStatement ps_Compiere = DB.prepareStatement(SQL_Compiere,null);
	        	ResultSet rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
			            SQL_AS = "INSERT INTO BECOFILE.PROM01C (CODPRO, NOMPRO, NOMING, CODDEP," 
			                 + "CODLIN, SECCIO, MARCA, CODARA, UNICOM, UNIVEN, TIPETI,"    
			               	 + "PVPETI, TIPIMP, PESO,"
			           		 + "DIAING, MESING, AÑOING, DIAFIN, MESFIN, AÑOFIN,"
			           		 + "STAPRO, USRCRE, USRACT, COEMPE, REFPRO, GRUPRO,"  
			           		 + "SUBLIN, FRAGIL, TIPPRO, CONCEP, TIPINV," 
			           		 + "PAIS, INIMOC, CARACT, CARAC1, CARAC2, CARAC3) " +
			                		 
			                "VALUES("
			                	    + rs.getInt("CODPRO") + ",'";
			                	    
			                	if(rs.getString("NOMPRO").length()>50)
			                		SQL_AS += rs.getString("NOMPRO").substring(0, 50).replace("'", " ") + "','";
			                	else
			                		SQL_AS += rs.getString("NOMPRO").replace("'", " ") + "','";
			            
			            		if(rs.getString("NOMING").length()>50)
			            			SQL_AS += rs.getString("NOMING").substring(0, 50).replace("'", " ") + "','";
			            		else
			            			SQL_AS += rs.getString("NOMING").replace("'", " ") + "','";
			            			
			            		SQL_AS += rs.getString("CODDEP") +"','" 
			                		+ rs.getString("CODLIN") +"','" 
			                		+ rs.getString("SECCIO") +"','";
			                			
			                	if(rs.getString("MARCA").length()>5){
			                		SQL_AS += rs.getString("MARCA").substring(0, 5) +"','";
			                	}
			                	else
			                		SQL_AS += rs.getString("MARCA") +"','";
			                		
			                	SQL_AS += rs.getString("CODARA") +"','" 
			                		+ rs.getString("UNICOM") +"','" 
			                		+ rs.getString("UNIVEN") +"','";
			            		
			                		if(rs.getString("TIPETI").length()>2)
			                			SQL_AS += rs.getString("TIPETI").substring(0, 2) +"','";
			                		else
			                			SQL_AS += rs.getString("TIPETI") +"','";
			                		
			                		SQL_AS += rs.getString("PVPETI") +"','"; 
			                		
			                		if(rs.getString("TIPIMP").length()>3)
			                			SQL_AS += rs.getString("TIPIMP").substring(0, 3) +"',";
			                		else
			                			SQL_AS += rs.getString("TIPIMP").substring(0, 3) +"',";
			                		
			                		SQL_AS += rs.getInt("PESO") +"," 
			                	    + rs.getInt("DIAING") +"," 
			                	    + rs.getInt("MESING") +"," 
			                	    + rs.getInt("ANOING") +"," 
			                		+ rs.getInt("DIAFIN") +"," 
			                		+ rs.getInt("MESFIN") +"," 
			               			+ rs.getInt("ANOFIN") +",'" 
			               			+ rs.getString("STAPRO") +"','" 
			               			+ rs.getString("USRCRE") +"','" 
			               			+ rs.getString("USRACT") +"'," 
			               			+ rs.getInt("COEMPE") +",'";
			               			
			               			if(rs.getString("REFPRO").length()>25){
			               				SQL_AS += rs.getString("REFPRO").substring(0, 25) +"','" ;
			               			}
			               			else{
			               				SQL_AS += rs.getString("REFPRO") +"','" ;
			               			}
			               				
			               			SQL_AS += rs.getString("GRUPRO") +"','" 
			                		+ rs.getString("SUBLIN") +"','" 
			                		+ rs.getString("FRAGIL") +"','" 
			                		+ rs.getString("TIPPRO") +"','" 
			                		+ rs.getString("CONCEP").substring(0, 1) +"','" 
			                		+ rs.getString("TIPINV").substring(0,2) +"','" 
			                		+ rs.getString("PAIS") +"','" 
			                		+ rs.getString("INIMOC") +"','" 
			                		+ rs.getString("CARACT").replace("'", " ") +"','" 
			                		+ rs.getString("CARAC1") +"','" 
			                		+ rs.getString("CARAC2") +"','" 
			                		+ rs.getString("CARACT3")
			                		+"')";
			                   
						if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000){
							As.desconectar();
							As.conectar();
		   			    }
			               			
			            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            r2 = As.realizarSentencia(SQL_AS, sentencia);
			            
			            //Si se inserto correctamente lo actualizo en Y
//			            if(r2==1){						
							String sql2 = "UPDATE E_XX_VMR_PROM01"											
								+ " SET XX_STATUS_SINC = 'Y'"											
								+ " WHERE E_XX_VMR_PROM01_ID=" + rs.getInt("E_XX_VMR_PROM01_ID");						
							DB.executeUpdate(null, sql2 );
							
							
//			            	rs.updateString("XX_STATUS_SINC", "Y");
//			            	rs.updateRow();
//			            }
			            
			            if(r2<0) //Si la inserción da error
			    			return r2;   	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR" +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}
	        	}
	        	rs.close();
	        	ps_Compiere.close();	
           }
        	
        	sentencia.close();
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
        	log.log(Level.SEVERE, e.getMessage());
		} 
        As.desconectar();
		return r;
	}
	
	
	/*
	 * Exporta la E_XX_VMR_PROM01 de Compiere al AS/400
	 */
	public int exportE_XX_VMR_PROD01DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS = "";
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
        	
        try
        {    
        	
        	//Borra la data de la tabla de Pauta de Rebajas en el AS/400
        	//TODO
        	// Esta parte se comento porque ahora el borrado se hace en el as
        	// En ese caso se debe  comentar o borrar la linea r=1;
        	//Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
/**        	SQL_AS = "DELETE FROM BECOFILE.PROD01C";
        	r = As.realizarSentencia(SQL_AS, sentencia);
*/
        	r = 1;
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere = "SELECT XX_STATUS_SINC,NUMREC, CODPRO, CONPRE, PRECOM, PREVEN, DIAREG, MESREG,"
		                	 + "ANOREG, DIACRE, MESCRE, ANOCRE, DIAACT, MESACT, ANOACT, USRCRE,"
		                	 + "USRACT, STATUS, ORIPRE, E_XX_VMR_PROD01_ID FROM E_XX_VMR_PROD01 ";
	        	       	
	        	PreparedStatement ps_Compiere = DB.prepareStatement(SQL_Compiere,null);
	        	ResultSet rs = ps_Compiere.executeQuery();
	        	
	        	int i=0;
	        	while(rs.next()){
	        		i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
			            SQL_AS = "INSERT INTO BECOFILE.PROD01C (NUMREC, CODPRO, CONPRE, PRECOM," 
			                 + "PREVEN, DIAREG, MESREG, AÑOREG, DIACRE, MESCRE, AÑOCRE,"    
			               	 + "DIAACT, MESACT, AÑOACT, USRCRE, USRACT, STATUS, ORIPRE) " 		                		 
			                 + "VALUES("
			                	    + rs.getInt("NUMREC") +"," 
			                	    + rs.getInt("CODPRO") +"," 
			                	    + rs.getInt("CONPRE") +"," 
			                		+ rs.getBigDecimal("PRECOM") +","
			                		+ rs.getBigDecimal("PREVEN") +"," 
			               			+ rs.getInt("DIAREG") +",'" 
			               			+ rs.getString("MESREG") +"','" 
			               			+ rs.getString("ANOREG") +"','" 
			                		+ rs.getString("DIACRE") +"','" 
			                		+ rs.getString("MESCRE") +"','" 
			                		+ rs.getString("ANOCRE") +"','" 
			                		+ rs.getString("DIAACT") +"','" 
			                		+ rs.getString("MESACT") +"','" 
			                		+ rs.getString("ANOACT") +"','" 
			                		+ rs.getString("USRCRE") +"','" 
			                		+ rs.getString("USRACT") +"','" 
			                		+ rs.getString("STATUS") +"','" 
			                		+ rs.getString("ORIPRE")
			                		+"')";
			                         
			            if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000){
							As.desconectar();
							As.conectar();
						}
			            
			            Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            try{
				            r2 = As.realizarSentencia(SQL_AS, sentencia);
				            
				            //Si se inserto correctamente lo actualizo en Y
				            if(r2==1){
								String sql2 = "UPDATE E_XX_VMR_PROD01"											
									+ " SET XX_STATUS_SINC = 'Y'"											
									+ " WHERE E_XX_VMR_PROD01_ID=" + rs.getInt("E_XX_VMR_PROD01_ID");						
								DB.executeUpdate(null, sql2 );
				            	
//				            	rs.updateString("XX_STATUS_SINC", "Y");
//				            	rs.updateRow();
				            }
				            
				            if(r2<0) //Si la inserción da error
				    			return r2;   	               
			            } catch(Exception e){
			            	e.printStackTrace();
			            } finally{
			            	DB.closeStatement(sentencia);
			            }
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR" +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}	           
	        	}
	        	DB.closeStatement(ps_Compiere);
	        	DB.closeResultSet(rs);
        	}
        	
        	//sentencia.close();
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
        	log.log(Level.SEVERE, e.getMessage());
		}
        As.desconectar();
		return r;
	}
	
	
	protected void prepare() {

	}

}
