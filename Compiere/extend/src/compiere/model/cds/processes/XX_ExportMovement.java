package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import compiere.model.cds.MMovement;
import compiere.model.cds.MMovementLine;
import compiere.model.cds.X_XX_E_Sold01;
import compiere.model.cds.X_XX_E_Solm01;


/*
* Proceso que sincroniza los registros relacionados con las solicitudes de descuento
* a las tablas XX_E_SOLD01 y XX_E_SOLM01.
* 
* @author Victor Lo Monaco.
*/

public class XX_ExportMovement extends SvrProcess{

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
			   COEMPE, COSANT, VENANT, NUMREC; 
				
		Integer movementID = null;
		Integer movementDetailID = null;
		//Integer sigNUMSOL = null;
		int delete, fallasSolM, fallasSolD;
		
		try {
			
			/*
			 * Borro las Tablas XX_E_SOLM01 y XX_E_SOLD01
			 */
			sql = "DELETE FROM XX_E_SOLD01 WHERE XX_SYNCHRONIZED = 'Y'";
			try{
				DB.executeUpdate(null,sql);
				
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} 
			
			sql = "DELETE FROM XX_E_SOLM01 WHERE XX_SYNCHRONIZED = 'Y'";
			try{
				DB.executeUpdate(null,sql);
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} 
					
			
			/*
			 * 
			 */
	
			sql =  "select M_Movement_id, " +
			   "SUBSTR(description,1,49) as DESSOL, " +
			   "DocumentNo as NUMSOL, " +
			   "CASE " +
			    "WHEN H.C_DOCTYPE_ID = 1000355 " +
			    "THEN '01' " +
			    "WHEN H.C_DOCTYPE_ID = 1000356 OR H.C_DOCTYPE_ID = 1000335 " + //1000335 Movimiento de Inventario (se maneja igual como un traspaso en el AS400)
			    "THEN '02' " +
			    "END AS TIPSOL, " +
			   "TO_CHAR(H.CREATED,'DD') as DIASOL, " +
			   "TO_CHAR(H.CREATED,'MM') as MESSOL, " +
			   "TO_CHAR(H.CREATED,'YYYY') as AÑOSOL, " +
			   "(select SUBSTR(value,2) from m_warehouse where m_warehouse_id=H.M_WarehouseFrom_ID ) as TIENDA, " +			   
			   "TO_CHAR(" +
					   "CASE WHEN H.C_DOCTYPE_ID = 1000355  THEN (SELECT UPDATED FROM XX_VLO_RETURNOFPRODUCT WHERE M_Movement_ID = H.M_Movement_ID)" +
					   "ELSE UPDATED END,'DD') as DIAREG, " +
			   "TO_CHAR(" +
					   "CASE WHEN H.C_DOCTYPE_ID = 1000355  THEN (SELECT UPDATED FROM XX_VLO_RETURNOFPRODUCT WHERE M_Movement_ID = H.M_Movement_ID)" +
					   "ELSE UPDATED END,'MM') as MESREG, " +
			   "TO_CHAR(" +
					   "CASE WHEN H.C_DOCTYPE_ID = 1000355  THEN (SELECT UPDATED FROM XX_VLO_RETURNOFPRODUCT WHERE M_Movement_ID = H.M_Movement_ID)" +
					   "ELSE UPDATED END,'YYYY') as AÑOREG," +
			   "TO_CHAR(" +
					   "CASE WHEN H.C_DOCTYPE_ID = 1000355  THEN (SELECT created FROM XX_VLO_RETURNOFPRODUCT WHERE M_Movement_ID = H.M_Movement_ID)" +
					   "ELSE XX_DispatchDate  END,'DD') as DIASTA, " +
			   "TO_CHAR(" +
					   "CASE WHEN H.C_DOCTYPE_ID = 1000355  THEN (SELECT created FROM XX_VLO_RETURNOFPRODUCT WHERE M_Movement_ID = H.M_Movement_ID)" +
					   "ELSE XX_DispatchDate  END,'MM') as MESSTA, " +
			   "TO_CHAR(" +
					   "CASE WHEN H.C_DOCTYPE_ID = 1000355  THEN (SELECT created FROM XX_VLO_RETURNOFPRODUCT WHERE M_Movement_ID = H.M_Movement_ID)" +
					   "ELSE XX_DispatchDate  END,'YYYY') as AÑOSTA," +
			   "' ' as STDESP, " +
			   "TO_CHAR(XX_DispatchDate,'DD') as DSTDES, " +
			   "TO_CHAR(XX_DispatchDate,'MM') as MSTDES, " +
			   "TO_CHAR(XX_DispatchDate,'YYYY') as ASTDES, " +
			   "(SELECT D.DOCUMENTNO FROM XX_VLO_DETAILDISPATCHGUIDE DD, XX_VLO_DISPATCHGUIDE D WHERE D.XX_VLO_DISPATCHGUIDE_ID = DD.XX_VLO_DISPATCHGUIDE_ID AND (DD.M_MOVEMENTT_ID = H.M_MOVEMENT_ID OR DD.M_MOVEMENTR_ID = H.M_MOVEMENT_ID ) AND ROWNUM = 1) AS GUIDES, " +
			   "0 as CANBUL, " +
			   "(SELECT U.NAME FROM AD_USER U WHERE U.AD_USER_ID = H.CREATEDBY) as USRCRE, " +
			   "(SELECT U.NAME FROM AD_USER U WHERE U.AD_USER_ID = H.UPDATEDBY) USRACT, " +
			   "6 as STASOL, " + 
			   "' ' as CODASO, " +
			   "(select value from xx_vmr_department where xx_vmr_department_ID=H.XX_VMR_Department_ID) as CODDEP, " +
			  "0 PRDORI, " +
			  "0 CONORI, " +
			  "0 CANORI, " +
			  "(select SUBSTR(value,2) from m_warehouse where m_warehouse_id=H.M_WarehouseTo_ID) as TADESP " +
			  "from M_Movement H " + 
			  "where H.documentno>1000000 and ((XX_Status='AC' and H.C_DOCTYPE_ID = 1000335 and H.M_WarehouseTo_ID != H.M_WarehouseFrom_ID) or " +
			  "(XX_Status='AC' and H.C_DOCTYPE_ID = 1000355) or (XX_Status='AT' and H.C_DOCTYPE_ID = 1000356)) " +
			  "and XX_SYNCHRONIZED = 'N' and (H.C_DOCTYPE_ID = 1000355 or H.C_DOCTYPE_ID = 1000356 or H.C_DOCTYPE_ID = 1000335)"; 
			try{
	 			pstmt = DB.prepareStatement(sql, null);
					
				rs = pstmt.executeQuery();	
				
				fallasSolM=0;
				
				while (rs.next()){
					try{
						movementID = rs.getInt("M_Movement_id");
						DESSOL = rs.getString("DESSOL");
						if (DESSOL==null)
							DESSOL = " ";
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
						if (GUIDES==null)
							GUIDES = " ";
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
									   new Integer(CANORI), new Integer(TADESP), movementID);
						
		
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
					} catch(Exception e){
						e.printStackTrace();
						fallasSolM++;
					}
				}
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			sql1 ="SELECT " +
			  "M_MovementLine_ID, " +
			  "H.DocumentNo  as NUMSOL, " +
			  "(SELECT P.VALUE FROM M_PRODUCT P WHERE P.M_PRODUCT_ID = D.M_PRODUCT_ID) as CODPRO, " +
			  "(select dep.value from xx_vmr_department dep where dep.xx_vmr_department_id=H.XX_VMR_Department_ID) as CODDEP, " +
			  "(select lin.value from xx_vmr_line lin where lin.xx_vmr_line_id = d.xx_vmr_line_id) as CODLIN, " +
			  "(select sec.value from xx_vmr_section sec where sec.xx_vmr_section_id=d.xx_vmr_section_id) as SECCIO, " +
			  "'01' as TIMOSO, " +			  
			  "coalesce((CASE " +
			  "    WHEN ((select T.name from C_DocType T where T.C_DocType_ID=H.C_DocType_ID) like '%Traspaso%') THEN " +
			  			" (CASE " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=H.xx_transfermotive_ID) like '%TIENDA%') THEN '52' " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=H.xx_transfermotive_ID) like '%INVENTARIO%') THEN '57' " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=H.xx_transfermotive_ID) like '%PLANIFICADOR%') THEN '51' " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=H.xx_transfermotive_ID) like '%CORPORATIVA%') THEN '55' " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=H.xx_transfermotive_ID) like '%ROTACIÓN%') THEN '56' END )" +			  				
			  "    else (CASE " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=d.xx_returnmotive_id) like '%DAÑADA%') THEN '01' " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=d.xx_returnmotive_id) like '%TIEMPO%') THEN '02' " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=d.xx_returnmotive_id) like '%CONSIGNACIÓN%') THEN '03' " +
			  				"WHEN  ((select mo.name from xx_vmr_cancellationmotive mo where mo.xx_vmr_cancellationmotive_id=d.xx_returnmotive_id) like '%NO ACORDE%') THEN '04' END )" +
			  "END),' ') as COMOSO, " +
			  "coalesce(XX_ApprovedQty,0)  as CANPRO, " +
			  "coalesce(XX_ApprovedQty,0)  as CAPRAP, " +
			  "coalesce(XX_ApprovedQty,0)   as CANFIS, " +
			  "coalesce(XX_ApprovedQty,0)   as CANDEP, " +
			  "' ' as STMOSO, " +
			  "TO_CHAR(H.Updated,'DD') as DSTMOV, " +
			  "TO_CHAR(H.Updated,'MM') as MSTMOV, " +
			  "TO_CHAR(H.Updated,'YYYY') as ASTMOV, " +
			  "' ' as CODAPR, " +
			  "(select W.value from M_warehouse W where W.M_Warehouse_Id=H.M_WarehouseTo_Id) as TIENDA, " +
			  "XX_SalePrice as PREPRM, " +
			  "0 as DSCPRM, " +
			  "NVL(D.XX_PRICECONSECUTIVE,0) as CONPRE, " +
			  "D.XX_PRICECONSECUTIVE as COPRVI, " +
			  "0 as CPROVI, " +
			  "(select p.value from c_bpartner p where c_bpartner_id=(select pro.c_bpartner_id from m_product pro where pro.m_product_id=d.m_product_id)) as COEMPE, " +			  
			  "ROUND(d.XX_SalePrice ,2) as VENANT, " +
			  "0 as COSANT, " +
			  "(CASE WHEN H.C_DOCTYPE_ID = 1000335 THEN " +
			  "     (SELECT MIN(TO_NUMBER(IO.DOCUMENTNO)) " +
			  "			FROM  M_INOUT IO, M_INOUTLINE IOL " +
			  "			WHERE IOL.M_INOUT_ID = IO.M_INOUT_ID AND D.M_PRODUCT_ID = IOL.M_PRODUCT_ID AND IO.DOCUMENTNO IS NOT NULL " +
			  "			AND D.M_ATTRIBUTESETINSTANCE_ID = IOL.M_ATTRIBUTESETINSTANCE_ID) " +		  				
			  "    ELSE 0 END) as NUMREC " +
			  "FROM M_MovementLine D, M_Movement H " +
			  "WHERE d.M_Movement_id=h.M_Movement_id " +
			  "AND  H.documentno>1000000 and ((XX_Status='AC' and H.C_DOCTYPE_ID <> 1000356) or (XX_Status='AT' and H.C_DOCTYPE_ID = 1000356)) and D.XX_SYNCHRONIZED = 'N' "; 
			
			try{
				pstmt1 = DB.prepareStatement(sql1, null);
				rs1 = pstmt1.executeQuery();
				fallasSolD=0;
				while (rs1.next()){
					movementDetailID = rs1.getInt("M_MovementLine_ID");
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
					NUMREC = rs1.getString("NUMREC");
					
					X_XX_E_Sold01 SOLD01  = new X_XX_E_Sold01(getCtx(), 0, null);
					
					try{
					
						SOLD01.setXX_NUMSOL(new BigDecimal(NUMSOL));
						SOLD01.setXX_CODPRO(new BigDecimal(CODPRO));
						SOLD01.setXX_CodDep(CODDEP1);
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
						SOLD01.setXX_TIENDA(new BigDecimal(TIENDA1));
						SOLD01.setXX_PREPRM(new BigDecimal(PREPRM).setScale(2, RoundingMode.HALF_UP));
						SOLD01.setXX_DSCPRM(new BigDecimal(DSCPRM));
						SOLD01.setXX_CONPRE(new BigDecimal(CONPRE));
						SOLD01.setXX_COPRVI(new BigDecimal(COPRVI));
						SOLD01.setXX_CPROVI(new BigDecimal(CPROVI));
						SOLD01.setXX_COEMPE(new BigDecimal(COEMPE));
						SOLD01.setXX_COSANT(new BigDecimal(COSANT).setScale(2, RoundingMode.HALF_UP));
						SOLD01.setXX_VENANT(new BigDecimal(VENANT).setScale(2, RoundingMode.HALF_UP));
						SOLD01.setNUMREC(NUMREC);
						
						
						SOLD01.save();
						
						String SQL5 =
							"UPDATE M_MOVEMENTLINE SET "
						    +"XX_Synchronized = 'Y' "+
						    " WHERE M_MOVEMENTLINE_ID="+movementDetailID;
						
						try {
							DB.executeUpdate(null,SQL5);
						} catch (Exception e) {
							log.log(Level.SEVERE, e.getMessage());
						} 
						commit();
	
					} catch (Exception e) {   
						fallasSolD++;
						e.printStackTrace();   
					}
					
	/**				insertarSOLD01(new Integer(NUMSOL), new Integer(CODPRO), CODDEP1, CODLIN, SECCIO, TIMOSO, COMOSO, new Integer(CANPRO), 
								   new Integer(CAPRAP), new Integer(CANFIS), new Integer(CANDEP), STMOSO, new Integer(DSTMOV), 
								   new Integer(MSTMOV), new Integer(ASTMOV), CODAPR, new Integer(TIENDA1), new Double(PREPRM),
								   new Integer(DSCPRM), new Integer(CONPRE), new Integer(COPRVI), new Integer(CPROVI),
								   new Integer(COEMPE), new Double(COSANT), new Double(VENANT), movementDetailID);
	*/						
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
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} finally{
				DB.closeResultSet(rs1);
				DB.closeStatement(pstmt1);
			}		
			
		}catch (SQLException e) {
			e.printStackTrace();
			return "Fallo en Sincronización";
		}
		if(fallasSolD!=0 || fallasSolM!=0){
			return "Sincronización Completa pero con errores: Fallas en XX_E_Sold01: "+fallasSolD+"; Fallas en XX_E_Solm01: "+fallasSolM;
		} else {
			return "Sincronización Completa";
		}
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
								String CODDEP, int PRDORI, int CONORI, int CANORI, int TADESP, int movementID){
		
		X_XX_E_Solm01 SOLM01  = new X_XX_E_Solm01(getCtx(), 0, null);
				
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
			
        	MMovement movement = new MMovement(getCtx(), movementID, null);
        	movement.setXX_Synchronized(true);
        	movement.save();
        				
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
								int COEMPE, Double COSANT, Double VENANT, int movementDetailID, String NUMREC){
		System.out.println("entra");
		X_XX_E_Sold01 SOLD01  = new X_XX_E_Sold01(getCtx(), 0, null);
		
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
			SOLD01.setNUMREC(NUMREC);
			
			
			SOLD01.save();
			
			MMovementLine movementDetail = new MMovementLine(getCtx(), movementDetailID, null);
			movementDetail.setXX_Synchronized(true);
			movementDetail.save();
		}
		catch (Exception e) {        	
			e.printStackTrace();   
		}
	}

}
