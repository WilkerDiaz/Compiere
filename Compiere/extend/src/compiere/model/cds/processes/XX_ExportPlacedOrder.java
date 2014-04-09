package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_Ref_XX_Ref_TypeDispatchGuide;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_E_Sold01;
import compiere.model.cds.X_XX_E_Solm01;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;

/*
* Proceso que sincroniza los registros relacionados con los Pedidos
* a las tablas XX_E_SOLD01 y XX_E_SOLM01.
* 
* @author Jorge E. Pires G.
*/

public class XX_ExportPlacedOrder extends SvrProcess{

//	private X_E_XX_VCN_INVM14 INVM14 = null;
//	private X_E_XX_VCN_INVD53 INVD53 = null;

//	private X_XX_E_Sold01 SOLD01 = null;
//	private X_XX_E_Solm01 SOLM01 = null;

	@Override
	protected void prepare() {
		
	}
	
	@Override
	protected String doIt() throws Exception {		
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		String sql1 = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;		
		
				
		/*
		 * Variables
		 */
		String DESSOL, TIPSOL, DIASOL, MESSOL, AÑOSOL, TIENDA, DIAREG, MESREG, AÑOREG, DIASTA, MESSTA,
			   AÑOSTA, STDESP, DSTDES, MSTDES, ASTDES, GUIDES, CANBUL, USRCRE, USRACT, STASOL, CODASO, 
			   CODDEP, PRDORI, CONORI, CANORI, TADESP, NUMSOL;
		
		String CODPRO, CODDEP1, CODLIN, SECCIO, TIMOSO, COMOSO, CANPRO, CAPRAP, CANFIS, CANDEP,
			   STMOSO, DSTMOV, MSTMOV, ASTMOV, CODAPR, TIENDA1, PREPRM, DSCPRM, CONPRE, COPRVI, CPROVI,
			   COEMPE, COSANT, VENANT; 
				
		Integer orderID = null;
		Integer orderDetailID = null;
		//Integer sigNUMSOL = null;
		
		
		try {
			
			/*
			 * Borro las Tablas XX_E_SOLM01 y XX_E_SOLD01
			 */
			sql = "DELETE FROM XX_E_SOLD01" ;
			DB.executeUpdate(null, sql );
			
			sql = "DELETE FROM XX_E_SOLM01";
			
			DB.executeUpdate(null, sql );
			
			
			/*
			 * 
			 */
			sql = "\nSELECT " +
					"\nO.XX_VMR_ORDER_ID, " +
					"\n'Redistribución nro: '||O.XX_VMR_DISTRIBUTIONHEADER_ID||' T-'||W.VALUE||' a T-'||(SELECT M.VALUE FROM M_WAREHOUSE M WHERE M.M_WAREHOUSE_ID = O.M_WAREHOUSE_ID)||' DPT. '||(SELECT D.VALUE FROM C_ORDER CO, XX_VMR_DEPARTMENT D WHERE CO.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID AND CO.C_ORDER_ID = O.C_ORDER_ID) DESSOL, " +
					"\n(SELECT W.VALUE FROM M_WAREHOUSE W WHERE O.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID)|| O.XX_VMR_DISTRIBUTIONHEADER_ID NUMSOL, " +
					"\n'10' TIPSOL, " +
					"\nTO_CHAR(O.CREATED,'DD') DIASOL, " +
					"\nTO_CHAR(O.CREATED,'MM') MESSOL, " +
					"\nTO_CHAR(O.CREATED,'YYYY') AÑOSOL, " +
//				  "'01' TIENDA, " +
					"\nW.VALUE TIENDA, " + 
				  "\nTO_CHAR(O.CREATED,'DD') DIAREG, " +
				  "\nTO_CHAR(O.CREATED,'MM') MESREG, " +
				  "\nTO_CHAR(O.CREATED,'YYYY') AÑOREG, " +
				  
				  //"TO_CHAR(O.CREATED,'DD') DIASTA, " +
				  //"TO_CHAR(O.CREATED,'MM') MESSTA, " +
				  //"TO_CHAR(O.CREATED,'YYYY') AÑOSTA, " +
				  
				  /*
				   * Este cambio es para evitar que los pedidos se queden en el mes de la 
				   * fecha de despacho de la guia (XX_DISPATCHDATE) en vez del mes en que llegaron a tienda.
				   * Cambio Por: Mariluz Quintal
				   * Fecha: 14/01/2013
				   */
				   
				  "\nNVL(TO_CHAR(O.XX_DATESTATUSONSTORE,'DD'),TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'DD')) DIASTA, " +
				  "\nNVL(TO_CHAR(O.XX_DATESTATUSONSTORE,'MM'),TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'MM')) MESSTA, " +
				  "\nNVL(TO_CHAR(O.XX_DATESTATUSONSTORE,'YYYY'),TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'YYYY')) AÑOSTA, " +
				  
				  //"\nNVL(TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'DD'),TO_CHAR(O.CREATED,'DD')) DIASTA, " +
				  //"\nNVL(TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'MM'),TO_CHAR(O.CREATED,'MM')) MESSTA, " +
				  //"\nNVL(TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'YYYY'),TO_CHAR(O.CREATED,'YYYY')) AÑOSTA, " +
				  
				  "\nCASE " +
				  "\n    WHEN (SELECT D.XX_DISPATCHGUIDESTATUS FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1) = 'TRA' THEN '2' " +
				  "\n    ELSE '1' " +
				  "\nEND STDESP, " +
				  "\nNVL(TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'DD'),0) DSTDES, " +
				  "\nNVL(TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'MM'),0) MSTDES, " +
				  "\nNVL(TO_CHAR((SELECT D.XX_DISPATCHDATE FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1),'YYYY'),0) ASTDES, " +
				  "\n(SELECT D.DOCUMENTNO FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND DD.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID AND DD.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' AND D.XX_DISPATCHGUIDESTATUS <> 'SUG' AND ROWNUM = 1) GUIDES, " +
 				  "\nNVL(O.XX_PACKAGEQUANTITY,0) CANBUL, " +
 				  "\n(SELECT U.NAME FROM AD_USER U WHERE U.AD_USER_ID = O.CREATEDBY) USRCRE, " +
				  "\n(SELECT U.NAME FROM AD_USER U WHERE U.AD_USER_ID = O.UPDATEDBY) USRACT, " +
				  "\nCASE " +
				  "\n    WHEN O.XX_ORDERREQUESTSTATUS = 'ET' THEN '3' " +
				  "\n    WHEN O.XX_ORDERREQUESTSTATUS = 'TI' THEN '6' " +
				  "\nEND STASOL, " +
				  "\n' ' CODASO, " +
				  "\nCASE  " +
				  "\n    WHEN O.C_ORDER_ID IS NOT NULL THEN (SELECT D.VALUE FROM C_ORDER CO, XX_VMR_DEPARTMENT D WHERE CO.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID AND CO.C_ORDER_ID = O.C_ORDER_ID) " +
				  "\n    ELSE NULL " +
				  "\nEND CODDEP, " +
				  "\n0 PRDORI, " +
				  "\n0 CONORI, " +
				  "\n0 CANORI, " +
				  "\n(SELECT W.VALUE FROM M_WAREHOUSE W WHERE O.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID) TADESP " +
				  "\n	" +
				  "\nFROM XX_VMR_ORDER O, M_WAREHOUSE W " +
				  "\nWHERE (O.XX_ORDERREQUESTSTATUS = '"+X_Ref_XX_VMR_OrderStatus.EN_TIENDA.getValue()+"' OR " +
				  "\nO.XX_ORDERREQUESTSTATUS = '"+X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue()+"') " +
				  "\nAND XX_SYNCHRONIZED = 'N' " +
				  "\nAND O.AD_ORG_ID = W.AD_ORG_ID " + 
				  "\nAND O.XX_OrderRequestType != 'D'"; //No toma en cuanta Pedidos de Despacho Directo

 			pstmt = DB.prepareStatement(sql, null);
System.out.println(sql);
			rs = pstmt.executeQuery();			
			while (rs.next()){
				orderID = rs.getInt("XX_VMR_ORDER_ID");
				DESSOL = rs.getString("DESSOL");
				TIPSOL = rs.getString("TIPSOL");
				DIASOL = rs.getString("DIASOL");
				MESSOL = rs.getString("MESSOL");
				AÑOSOL = rs.getString("AÑOSOL");
				TIENDA = rs.getString("TIENDA");
				DIAREG = rs.getString("DIAREG");
				MESREG = rs.getString("MESREG");
				AÑOREG = rs.getString("AÑOREG");
				DIASTA = rs.getString("DIASTA");
				MESSTA = rs.getString("MESSTA");
				AÑOSTA = rs.getString("AÑOSTA");
				STDESP = rs.getString("STDESP");
				DSTDES = rs.getString("DSTDES");
				MSTDES = rs.getString("MSTDES");
				ASTDES = rs.getString("ASTDES");
				GUIDES = rs.getString("GUIDES");
				CANBUL = rs.getString("CANBUL");
				USRCRE = rs.getString("USRCRE");
				USRACT = rs.getString("USRACT");
				STASOL = rs.getString("STASOL");
				CODASO = rs.getString("CODASO");
				CODDEP = rs.getString("CODDEP");
				PRDORI = rs.getString("PRDORI");
				CONORI = rs.getString("CONORI");
				CANORI = rs.getString("CANORI");
				TADESP = rs.getString("TADESP");
				NUMSOL = rs.getString("NUMSOL");
				
				/*
				 * Se insertan los registros en la tabla de XX_E_SOLM01 que contiene la cabecera del pedido 
				 * 
				 * */
				//sigNUMSOL = sigNUMSOL + 1;
				insertarSOLM01(new Integer(NUMSOL), DESSOL, TIPSOL, new Integer(DIASOL), new Integer(MESSOL), new Integer(AÑOSOL), new Integer(TIENDA),
							   new Integer(DIAREG), new Integer(MESREG), new Integer(AÑOREG), new Integer(DIASTA), new Integer(MESSTA), 
							   new Integer(AÑOSTA), STDESP, new Integer(DSTDES), new Integer(MSTDES), new Integer(ASTDES), GUIDES, 
							   new Integer(CANBUL), USRCRE, USRACT, STASOL, CODASO, CODDEP, new Integer(PRDORI), new Integer(CONORI), 
							   new Integer(CANORI), new Integer(TADESP), orderID);
				

				/*
				 * Inicializo las variables para el proximo registro.
				 * 
				 * */			 	   
		 	   DESSOL= null;
		 	   TIPSOL= null;
		 	   DIASOL= null;
		 	   MESSOL= null;
		 	   AÑOSOL= null;
		 	   TIENDA= null;
		 	   DIAREG= null;
		 	   MESREG= null;
		 	   AÑOREG= null;
		 	   DIASTA= null;
		 	   MESSTA= null;
			   AÑOSTA= null;
			   STDESP= null;
			   DSTDES= null;
			   MSTDES= null;
			   ASTDES= null;
			   GUIDES= null;
			   CANBUL= null;
			   USRCRE= null;
			   USRACT= null;
			   STASOL= null;
			   CODASO= null;
			   CODDEP= null;
			   PRDORI= null;
			   CONORI= null;
			   CANORI= null;
			   TADESP= null;
			   NUMSOL= null;
			}
			
			DB.closeStatement(pstmt);
			
			sql1 ="\nSELECT " +
			  "\nO.XX_VMR_ORDERREQUESTDETAIL_ID, " +
			  "\n(SELECT W.VALUE FROM M_WAREHOUSE W WHERE ORD.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID) || ORD.XX_VMR_DISTRIBUTIONHEADER_ID NUMSOL, " +
			  "\n(SELECT P.VALUE FROM M_PRODUCT P WHERE P.M_PRODUCT_ID = O.M_PRODUCT_ID) CODPRO, " +
			  "\nD.VALUE CODDEP, " +
			  "\nL.VALUE CODLIN, " +
			  "\nS.VALUE SECCIO, " +
			  "\n'02' TIMOSO, " +
			  "\n'51' COMOSO, " +
			  "\nO.XX_PRODUCTQUANTITY CANPRO, " +
			  "\nO.XX_PRODUCTQUANTITY CAPRAP, " +
			  "\nO.XX_PRODUCTQUANTITY CANFIS, " +
			  //"0 CANDEP, " +
			  "\nCASE " +
			  "\n    WHEN ORD.XX_ORDERREQUESTSTATUS = 'ET' THEN 0 " +
			  "\n    WHEN ORD.XX_ORDERREQUESTSTATUS = 'TI' THEN O.XX_PRODUCTQUANTITY " +
			  "\nEND CANDEP, " +
			  "\n'3' STMOSO, " +
			  "\nTO_CHAR(O.CREATED,'DD') DSTMOV, " +
			  "\nTO_CHAR(O.CREATED,'MM') MSTMOV, " +
			  "\nTO_CHAR(O.CREATED,'YYYY') ASTMOV, " +
			  "\n' ' CODAPR, " +
//			  "'001' TIENDA, " +
			  "\nW.VALUE TIENDA, " +
			  "\nROUND(O.XX_SALEPRICE,2) PREPRM, " +
			  "\n0 DSCPRM, " +
			  "\nO.XX_PRICECONSECUTIVE CONPRE, " +
			  "\n0 COPRVI, " +
			  "\n0 CPROVI, " +
			  "\nBP.VALUE COEMPE, " +
			  "\nROUND(O.XX_UNITPURCHASEPRICE,2) COSANT, " +
			  "\nROUND((SELECT ASI.XX_SALEPRICE FROM M_ATTRIBUTESETINSTANCE ASI, XX_VMR_PRICECONSECUTIVE PC WHERE O.M_PRODUCT_ID = PC.M_PRODUCT_ID AND O.XX_PRICECONSECUTIVE = PC.XX_PRICECONSECUTIVE AND ASI.M_ATTRIBUTESETINSTANCE_ID = PC.M_ATTRIBUTESETINSTANCE_ID AND ROWNUM = 1),2) VENANT " +
			  "\n " +
			  "\nFROM XX_VMR_ORDERREQUESTDETAIL O, XX_VMR_ORDER ORD, " +
			  "\nC_BPARTNER BP, XX_VMR_VENDORPRODREF VPR, M_PRODUCT P, " +
			  "\nXX_VMR_DEPARTMENT D, XX_VMR_LINE L, XX_VMR_SECTION S, " +
			  "\nM_WAREHOUSE W " +
			  "\n " +
			  "\nWHERE O.XX_VMR_ORDER_ID = ORD.XX_VMR_ORDER_ID " +
			  "\nAND D.XX_VMR_DEPARTMENT_ID = O.XX_VMR_DEPARTMENT_ID " +
			  "\nAND L.XX_VMR_LINE_ID = O.XX_VMR_LINE_ID " +
			  "\nAND S.XX_VMR_SECTION_ID = O.XX_VMR_SECTION_ID " +
			  "\nAND P.M_PRODUCT_ID = O.M_PRODUCT_ID AND P.XX_VMR_VENDORPRODREF_ID = VPR.XX_VMR_VENDORPRODREF_ID AND VPR.C_BPARTNER_ID = BP.C_BPARTNER_ID " +
			  "\nAND (ORD.XX_ORDERREQUESTSTATUS = 'TI' OR ORD.XX_ORDERREQUESTSTATUS = 'ET') " +
			  "\nAND O.XX_SYNCHRONIZED = 'N' " +
			  "\nAND ORD.AD_ORG_ID = W.AD_ORG_ID " + 
			  "\nAND ORD.XX_OrderRequestType != 'D' "+ //No toma en cuanta Pedidos de Despacho Directo
			  "\nORDER BY NUMSOL";
			pstmt1 = DB.prepareStatement(sql1, null);
			
			System.out.println(sql1);
			rs1 = pstmt1.executeQuery();			
			while (rs1.next()){
				orderDetailID = rs1.getInt("XX_VMR_ORDERREQUESTDETAIL_ID");
				CODPRO = rs1.getString("CODPRO");
				CODDEP1 = rs1.getString("CODDEP");
				CODLIN = rs1.getString("CODLIN");
				SECCIO = rs1.getString("SECCIO");
				TIMOSO = rs1.getString("TIMOSO");
				COMOSO = rs1.getString("COMOSO");
				CANPRO = rs1.getString("CANPRO");
				CAPRAP = rs1.getString("CAPRAP");
				CANFIS = rs1.getString("CANFIS");
				CANDEP = rs1.getString("CANDEP");
				STMOSO = rs1.getString("STMOSO");
				DSTMOV = rs1.getString("DSTMOV");
				MSTMOV = rs1.getString("MSTMOV");
				ASTMOV = rs1.getString("ASTMOV");
				CODAPR = rs1.getString("CODAPR");
				TIENDA1 = rs1.getString("TIENDA");
				PREPRM = rs1.getString("PREPRM");
				DSCPRM = rs1.getString("DSCPRM");
				CONPRE = rs1.getString("CONPRE");
				COPRVI = rs1.getString("COPRVI");
				CPROVI = rs1.getString("CPROVI");
				COEMPE = rs1.getString("COEMPE");
				COSANT = rs1.getString("COSANT");
				VENANT = rs1.getString("VENANT");
				NUMSOL = rs1.getString("NUMSOL");
				
				if (VENANT != null && CONPRE != null && CANPRO != null)
				{
				insertarSOLD01(new Integer(NUMSOL), new Integer(CODPRO), CODDEP1, CODLIN, SECCIO, TIMOSO, COMOSO, new Integer(CANPRO), 
							   new Integer(CAPRAP), new Integer(CANFIS), new Integer(CANDEP), STMOSO, new Integer(DSTMOV), 
							   new Integer(MSTMOV), new Integer(ASTMOV), CODAPR, new Integer(TIENDA1), new Double(PREPRM),
							   new Integer(DSCPRM), new Integer(CONPRE), new Integer(COPRVI), new Integer(CPROVI),
							   new Integer(COEMPE), new Double(COSANT), new Double(VENANT), orderDetailID);
				}		
		 	   CODPRO= null;
		 	   CODDEP1= null;
		 	   CODLIN= null;
		 	   SECCIO= null;
		 	   TIMOSO= null;
		 	   COMOSO= null;
		 	   CANPRO= null;
		 	   CAPRAP= null;
		 	   CANFIS= null;
		 	   CANDEP= null;
		 	   STMOSO= null;
		 	   DSTMOV= null;
		 	   MSTMOV= null;
		 	   ASTMOV= null;
		 	   CODAPR= null;
		 	   TIENDA1= null;
		 	   PREPRM= null;
		 	   DSCPRM= null;
		 	   CONPRE= null;
		 	   COPRVI= null;
		 	   CPROVI= null;
			   COEMPE= null;
			   COSANT= null;
			   VENANT = null; 
			   NUMSOL = null;
		}
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
			return "Fallo en Sincronización";
		} finally{
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "Sincronización Completa";
	} 
	

	/**
	 * 
	 * @param NUMSOL
	 * @param DESSOL
	 * @param TIPSOL
	 * @param DIASOL
	 * @param MESSOL
	 * @param AÑOSOL
	 * @param TIENDA
	 * @param DIAREG
	 * @param MESREG
	 * @param AÑOREG
	 * @param DIASTA
	 * @param MESSTA
	 * @param AÑOSTA
	 * @param STDESP
	 * @param DSTDES
	 * @param MSTDES
	 * @param ASTDES
	 * @param GUIDES
	 * @param CANBUL
	 * @param USRCRE
	 * @param USRACT
	 * @param STASOL
	 * @param CODASO
	 * @param CODDEP
	 * @param PRDORI
	 * @param CONORI
	 * @param CANORI
	 * @param TADESP
	 * @param XX_ORDER_ID
	 */
	public void insertarSOLM01 (int NUMSOL, String DESSOL, String TIPSOL, int DIASOL, int MESSOL, int AÑOSOL, int TIENDA, int DIAREG, 
								int MESREG, int AÑOREG, int DIASTA, int MESSTA, int AÑOSTA, String STDESP, int DSTDES, int MSTDES, 
								int ASTDES, String GUIDES, int CANBUL, String USRCRE, String USRACT, String STASOL, String CODASO, 
								String CODDEP, int PRDORI, int CONORI, int CANORI, int TADESP, int XX_ORDER_ID){
		
		X_XX_E_Solm01 SOLM01  = new X_XX_E_Solm01(getCtx(), 0, get_Trx());
				
        try{
        	
        	SOLM01.setXX_NUMSOL(new BigDecimal(NUMSOL));
        	SOLM01.setXX_DESSOL(DESSOL);
        	SOLM01.setXX_TIPSOL(TIPSOL);
        	SOLM01.setXX_DIASOL(new BigDecimal(DIASOL));
        	SOLM01.setXX_MESSOL(new BigDecimal(MESSOL));
        	SOLM01.setXX_AÑOSOL(new BigDecimal(AÑOSOL));
        	SOLM01.setXX_TIENDA(new BigDecimal(TIENDA));
        	SOLM01.setXX_DiaReg(new BigDecimal(DIAREG));
        	SOLM01.setXX_MesReg(new BigDecimal(MESREG));
        	SOLM01.setXX_AñoReg(new BigDecimal(AÑOREG));
        	SOLM01.setXX_DIASTA(new BigDecimal(DIASTA));
        	SOLM01.setXX_MESSTA(new BigDecimal(MESSTA));
        	SOLM01.setXX_AÑOSTA(new BigDecimal(AÑOSTA));
        	SOLM01.setXX_STDESP(STDESP);
        	SOLM01.setXX_DSTDES(new BigDecimal(DSTDES));
        	SOLM01.setXX_MSTDES(new BigDecimal(MSTDES));
        	SOLM01.setXX_ASTDES(new BigDecimal(ASTDES));
        	SOLM01.setXX_GUIDES(GUIDES);
        	SOLM01.setXX_CANBUL(new BigDecimal(CANBUL));
        	SOLM01.setXX_USRCRE(USRCRE);
        	SOLM01.setXX_USRACT(USRACT);
        	SOLM01.setXX_STASOL(STASOL);
           	SOLM01.setXX_CODASO(CODASO);
        	SOLM01.setXX_CodDep(CODDEP);
        	SOLM01.setXX_PRDORI(new BigDecimal(PRDORI));
           	SOLM01.setXX_CONORI(new BigDecimal(CONORI));
        	SOLM01.setXX_CANORI(new BigDecimal(CANORI));
        	SOLM01.setXX_TADESP(new BigDecimal(TADESP));
        	SOLM01.save();
			
        	X_XX_VMR_Order orderAux = new X_XX_VMR_Order(getCtx(), XX_ORDER_ID, get_Trx());
        	orderAux.setXX_Synchronized(true);
        	orderAux.save();
        	commit();
        				
        }
        catch (Exception e) {        	
        	e.printStackTrace();   
        } 
	}
	
	/**
	 * 
	 * @param NUMSOL
	 * @param CODPRO
	 * @param CODDEP
	 * @param CODLIN
	 * @param SECCIO
	 * @param TIMOSO
	 * @param COMOSO
	 * @param CANPRO
	 * @param CAPRAP
	 * @param CANFIS
	 * @param CANDEP
	 * @param STMOSO
	 * @param DSTMOV
	 * @param MSTMOV
	 * @param ASTMOV
	 * @param CODAPR
	 * @param TIENDA
	 * @param PREPRM
	 * @param DSCPRM
	 * @param CONPRE
	 * @param COPRVI
	 * @param CPROVI
	 * @param COEMPE
	 * @param COSANT
	 * @param VENANT
	 * @param XX_VMR_ORDERREQUESTDETAIL_ID
	 */
	public void insertarSOLD01 (int NUMSOL, int CODPRO, String CODDEP, String CODLIN, String SECCIO, String TIMOSO, String COMOSO,
								int CANPRO, int CAPRAP, int CANFIS, int CANDEP, String STMOSO, int DSTMOV, int MSTMOV, int ASTMOV,
								String CODAPR, int TIENDA, Double PREPRM, int DSCPRM, int CONPRE, int COPRVI, int CPROVI,
								int COEMPE, Double COSANT, Double VENANT, int XX_VMR_ORDERREQUESTDETAIL_ID){

		X_XX_E_Sold01 SOLD01  = new X_XX_E_Sold01(getCtx(), 0, get_Trx());
		
		try{
		
			SOLD01.setXX_NUMSOL(new BigDecimal(NUMSOL));
			SOLD01.setXX_CODPRO(new BigDecimal(CODPRO));
			SOLD01.setXX_CodDep(CODDEP);
			SOLD01.setXX_CodLin(CODLIN);
			SOLD01.setXX_Seccio(SECCIO);
			SOLD01.setXX_TIMOSO(TIMOSO);
			SOLD01.setXX_COMOSO(COMOSO);
			SOLD01.setXX_CANPRO(new BigDecimal(CANPRO));
			SOLD01.setXX_CAPRAP(new BigDecimal(CAPRAP));
			SOLD01.setXX_CANFIS(new BigDecimal(CANFIS));
			SOLD01.setXX_CANDEP(new BigDecimal(CANDEP));
			SOLD01.setXX_STMOSO(STMOSO);
			SOLD01.setXX_DSTMOV(new BigDecimal(DSTMOV));
			SOLD01.setXX_MSTMOV(new BigDecimal(MSTMOV));
			SOLD01.setXX_ASTMOV(new BigDecimal(ASTMOV));
			SOLD01.setXX_CODAPR(CODAPR);
			SOLD01.setXX_TIENDA(new BigDecimal(TIENDA));
			SOLD01.setXX_PREPRM(new BigDecimal(PREPRM).setScale(2, RoundingMode.HALF_UP));
			SOLD01.setXX_DSCPRM(new BigDecimal(DSCPRM));
			SOLD01.setXX_CONPRE(new BigDecimal(CONPRE));
			SOLD01.setXX_COPRVI(new BigDecimal(COPRVI));
			SOLD01.setXX_CPROVI(new BigDecimal(CPROVI));
			SOLD01.setXX_COEMPE(new BigDecimal(COEMPE));
			SOLD01.setXX_COSANT(new BigDecimal(COSANT).setScale(2, RoundingMode.HALF_UP));
			SOLD01.setXX_VENANT(new BigDecimal(VENANT).setScale(2, RoundingMode.HALF_UP));
			
			
			SOLD01.save();
			
			X_XX_VMR_OrderRequestDetail orderRequestAux = new X_XX_VMR_OrderRequestDetail(getCtx(), XX_VMR_ORDERREQUESTDETAIL_ID, get_Trx());
			orderRequestAux.setXX_Synchronized(true);
			orderRequestAux.save();
			commit();
		}
		catch (Exception e) {        	
			e.printStackTrace();   
		}
	}

}
