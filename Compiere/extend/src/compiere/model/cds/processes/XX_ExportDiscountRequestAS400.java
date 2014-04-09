package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_XX_E_Sold01;
import compiere.model.cds.X_XX_E_Solm01;


/*
* Proceso que sincroniza los registros relacionados con los Pedidos
* a las tablas XX_E_SOLD01 y XX_E_SOLM01.
* 
* @author Jorge E. Pires G.
*/

public class XX_ExportDiscountRequestAS400 extends SvrProcess{

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
		
				
		/*
		 * Variables
		 */
		String DESSOL = new String(), TIPSOL = new String(), STDESP = new String(), GUIDES = new String(), 
			   USRCRE = new String(), USRACT = new String(), STASOL = new String(), CODASO = new String(),
			   CODDEP = new String();
		
		String DIASOL, MESSOL, AÑOSOL, TIENDA, DIAREG, MESREG, AÑOREG, DIASTA, MESSTA,
			   AÑOSTA, DSTDES, MSTDES, ASTDES, CANBUL, PRDORI, CONORI, CANORI, TADESP, NUMSOL;
		
		String CODPRO, CODLIN, SECCIO, TIMOSO, COMOSO, CANPRO, CAPRAP, CANFIS, CANDEP,
			   STMOSO, DSTMOV, MSTMOV, ASTMOV, CODAPR, TIENDA1, PREPRM, DSCPRM, CONPRE, COPRVI, CPROVI,
			   COEMPE, COSANT, VENANT; 
				
		Integer discountID = null;
		Integer discountDetailID = null;
		int numRegInserta =0;
		int XX_E_SOLM01_ID = 0;
		int XX_E_SOLD01_ID = 0;
		X_XX_E_Solm01 modESOLM01 = null;
		X_XX_E_Sold01 modESOLD01 = null;
		//Integer sigNUMSOL = null;

		
		
		/*
		 * Creo conexion con AS400
		 */
		As400DbManager as400 = new As400DbManager();
		as400.conectar();
		Statement ps_s = as400.conexion.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		/*
		 */
		
		/*
		 * Borro BECOFILE.SOLM01C
		 */
		String sql_delete= "DELETE FROM BECOFILE.SOLM01C ";

		/*	Ejecuto la sentencia
		* */
		numRegInserta = as400.realizarSentencia(sql_delete, ps_s);
		
		
		/*
		 * Borro BECOFILE.SOLD01C
		 */
		sql_delete= "DELETE FROM BECOFILE.SOLD01C ";

		/*	Ejecuto la sentencia
		* */
		numRegInserta = as400.realizarSentencia(sql_delete, ps_s);
		
/*		String sql_update2 = "UPDATE XX_E_Solm01 SET XX_SYNCHRONIZED = 'Y' WHERE XX_E_SOLM01_ID = ?";
		PreparedStatement ps_update2 = DB.prepareStatement(sql_update2, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, get_TrxName());*/

		sql = "SELECT XX_E_SOLM01_ID, " +
				"XX_SYNCHRONIZED, " +
				"XX_NUMSOL, " +
				"XX_DESSOL, " +
				"XX_TIPSOL, " +
				"XX_DIASOL, " +
				"XX_MESSOL, " +
				"XX_AÑOSOL, " +
				"XX_TIENDA, " +
				"XX_DIAREG, " +
				"XX_MESREG, " +
				"XX_AÑOREG, " +
				"XX_DIASTA, " +
				"XX_MESSTA, " +
				"XX_AÑOSTA, " +
				"XX_STDESP, " +
				"XX_DSTDES, " +
				"XX_MSTDES, " +
				"XX_ASTDES, " +
				"XX_GUIDES, " +
				"XX_CANBUL, " +
				"XX_USRCRE, " +
				"XX_USRACT, " +
				"XX_STASOL, " +
				"XX_CODASO, " +
				"XX_CODDEP, " +
				"XX_PRDORI, " +
				"XX_CONORI, " +
				"XX_CANORI, " +
				"XX_TADESP " +
			  "FROM XX_E_Solm01 ";

		//pstmt = DB.prepareStatement(sql, null);
		pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, get_TrxName());
		try {			
			rs = pstmt.executeQuery();
			
			while (rs.next()){			
				numRegInserta = 0;
				
				XX_E_SOLM01_ID = rs.getInt("XX_E_SOLM01_ID");
				NUMSOL = rs.getString("XX_NUMSOL");
				DESSOL = rs.getString("XX_DESSOL");
				TIPSOL = rs.getString("XX_TIPSOL");
				DIASOL = rs.getString("XX_DIASOL");
				MESSOL = rs.getString("XX_MESSOL");
				AÑOSOL = rs.getString("XX_AÑOSOL");
				TIENDA = rs.getString("XX_TIENDA");
				DIAREG = rs.getString("XX_DIAREG");
				MESREG = rs.getString("XX_MESREG");
				AÑOREG = rs.getString("XX_AÑOREG");
				DIASTA = rs.getString("XX_DIASTA");
				MESSTA = rs.getString("XX_MESSTA");
				AÑOSTA = rs.getString("XX_AÑOSTA");
				STDESP = rs.getString("XX_STDESP");
				DSTDES = rs.getString("XX_DSTDES");
				MSTDES = rs.getString("XX_MSTDES");
				ASTDES = rs.getString("XX_ASTDES");
				GUIDES = rs.getString("XX_GUIDES");
				CANBUL = rs.getString("XX_CANBUL");
				USRCRE = rs.getString("XX_USRCRE");
				USRACT = rs.getString("XX_USRACT");
				STASOL = rs.getString("XX_STASOL");
				CODASO = rs.getString("XX_CODASO");
				CODDEP = rs.getString("XX_CODDEP");
				PRDORI = rs.getString("XX_PRDORI");
				CONORI = rs.getString("XX_CONORI");
				CANORI = rs.getString("XX_CANORI");
				TADESP = rs.getString("XX_TADESP");
				
				
				/*
				 * Luego de Traerme los Datos los Inserto en el AS400
				 */
				String sql_Insert= "INSERT INTO BECOFILE.SOLM01C" +
											  "(NUMSOL ,DESSOL ,TIPSOL ,DIASOL ,MESSOL ,AÑOSOL ,TIENDA ,DIAREG ,MESREG ,AÑOREG , " +
											   "DIASTA ,MESSTA ,AÑOSTA ,STDESP ,DSTDES ,MSTDES ,ASTDES ,GUIDES ,CANBUL ,USRCRE , " +
											   "USRACT ,STASOL ,CODASO ,CODDEP ,PRDORI ,CONORI ,CANORI ,TADESP) " +
									   "VALUES (" + NUMSOL +" ,"+
									   		   " '"+ DESSOL +"', "+
									   		   " '"+ TIPSOL +"', "+				
									   		   " "+ DIASOL +", "+				
									   		   " "+ MESSOL +", "+				
									   		   " "+ AÑOSOL +", "+				
									   		   " "+ TIENDA +", "+				
									   		   " "+ DIAREG +", "+				
									   		   " "+ MESREG +", "+				
									   		   " "+ AÑOREG +", "+				
									   		   " "+ DIASTA +", "+				
									   		   " "+ MESSTA +", "+				
									   		   " "+ AÑOSTA +", "+				
									   		   " '"+ STDESP +"', "+				
									   		   " "+ DSTDES +", "+				
									   		   " "+ MSTDES +", "+				
									   		   " "+ ASTDES +", "+				
									   		   " '"+ GUIDES +"', "+				
									   		   " "+ CANBUL +", "+				
									   		   " '"+ USRCRE +"', "+				
									   		   " '"+ USRACT +"', "+				
									   		   " '"+ STASOL +"', "+				
									   		   " '"+ CODASO +"', ";	
											   if(CODDEP != null){
												   sql_Insert = sql_Insert + " '"+  CODDEP +"', ";
											   }else{
												   sql_Insert = sql_Insert + " '0', ";
											   }
											   sql_Insert = sql_Insert + 
											   " "+ PRDORI +", "+				
									   		   " "+ CONORI +", "+				
									   		   " "+ CANORI +", "+				
									   		   " "+ TADESP +")";
				
				/*	Ejecuto la sentencia
				 * */
				numRegInserta = as400.realizarSentencia(sql_Insert, ps_s);
				/*if(numRegInserta > 0){
					rs.updateString("XX_SYNCHRONIZED", "Y");
					rs.updateRow();
				}*/
				modESOLM01 = new X_XX_E_Solm01(getCtx(), XX_E_SOLM01_ID, get_TrxName());
				modESOLM01.setXX_Synchronized(true);
				modESOLM01.save();
				
				commit();
				
				
				
			}
				
			
			/*
			 * Busco la SOld01
			 * */
			sql = "SELECT XX_E_SOLD01_ID, " +
					"XX_SYNCHRONIZED, "+
					"XX_NUMSOL, " +
					"XX_CODPRO, " +
					"XX_CODDEP, " +
					"XX_CODLIN, " +
					"XX_SECCIO, " +
					"XX_TIMOSO, " +
					"XX_CANPRO, " +
					"XX_COMOSO, " +
					"XX_CAPRAP, " +
					"XX_CANFIS, " +
					"XX_CANDEP, " +
					"XX_STMOSO, " +
					"XX_DSTMOV, " +
					"XX_MSTMOV, " +
					"XX_ASTMOV, " +
					"XX_CODAPR, " +
					"XX_TIENDA, " +
					"XX_PREPRM, " +
					"XX_DSCPRM, " +
					"XX_CONPRE, " +
					"XX_COPRVI, " +
					"XX_CPROVI, " +
					"XX_COEMPE, " +
					"XX_COSANT, " +
					"XX_VENANT  " +
		  	  "FROM XX_E_Sold01";  

			//pstmt = DB.prepareStatement(sql, null);
			pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next()){
				numRegInserta = 0 ;
				XX_E_SOLD01_ID = rs.getInt("XX_E_SOLD01_ID");
				NUMSOL = rs.getString("XX_NUMSOL");
				CODPRO = rs.getString("XX_CODPRO");
				CODDEP = rs.getString("XX_CODDEP");
				CODLIN = rs.getString("XX_CODLIN");
				SECCIO = rs.getString("XX_SECCIO");
				TIMOSO = rs.getString("XX_TIMOSO");
				COMOSO = rs.getString("XX_COMOSO");
				CANPRO = rs.getString("XX_CANPRO");
				CAPRAP = rs.getString("XX_CAPRAP");
				CANFIS = rs.getString("XX_CANFIS");
				CANDEP = rs.getString("XX_CANDEP");
				STMOSO = rs.getString("XX_STMOSO");
				DSTMOV = rs.getString("XX_DSTMOV");
				MSTMOV = rs.getString("XX_MSTMOV");
				ASTMOV = rs.getString("XX_ASTMOV");
				CODAPR = rs.getString("XX_CODAPR");
				TIENDA = rs.getString("XX_TIENDA");
				PREPRM = rs.getString("XX_PREPRM");
				DSCPRM = rs.getString("XX_DSCPRM");
				CONPRE = rs.getString("XX_CONPRE");
				COPRVI = rs.getString("XX_COPRVI");
				CPROVI = rs.getString("XX_CPROVI");
				COEMPE = rs.getString("XX_COEMPE");
				COSANT = rs.getString("xx_COSANT");
				VENANT = rs.getString("XX_VENANT");
				
				
				/*
				 * Luego de Traerme los Datos los Inserto en el AS400
				 */
				String sql_Insert= "INSERT INTO BECOFILE.SOLD01C" +
											  "(NUMSOL, CODPRO, CODDEP, CODLIN, SECCIO, TIMOSO, COMOSO, CANPRO, CAPRAP, " +
											   "CANFIS, CANDEP, STMOSO, DSTMOV, MSTMOV, ASTMOV, CODAPR, TIENDA, PREPRM, " +
											   "DSCPRM, CONPRE, COPRVI, CPROVI, COEMPE, COSANT, VENANT ) " +
									   "VALUES (" + NUMSOL +" ,"+
									   		   " "+ CODPRO +", "+
									   		   " '"+ CODDEP +"', "+				
									   		   " '"+ CODLIN +"', "+				
									   		   " '"+ SECCIO +"', "+				
									   		   " '"+ TIMOSO +"', "+				
									   		   " '"+ COMOSO +"', "+				
									   		   " "+ CANPRO +", "+				
									   		   " "+ CAPRAP +", "+				
									   		   " "+ CANFIS +", "+				
									   		   " "+ CANDEP +", "+				
									   		   " '"+ STMOSO +"', "+				
									   		   " "+ DSTMOV +", "+				
									   		   " "+ MSTMOV +", "+				
									   		   " "+ ASTMOV +", "+				
									   		   " '"+ CODAPR +"', "+				
									   		   " "+ TIENDA +", "+				
									   		   " "+ PREPRM +", "+				
									   		   " "+ DSCPRM +", "+				
									   		   " "+ CONPRE +", "+				
									   		   " "+ COPRVI +", "+				
									   		   " "+ CPROVI +", "+				
									   		   " "+ COEMPE +", "+	
									   		   " "+ COSANT +", "+	
									   		   " "+ VENANT +") ";	
				
				/*	Ejecuto la sentencia
				 * */
				numRegInserta = as400.realizarSentencia(sql_Insert, ps_s);
				/*if(numRegInserta > 0){
					rs.updateString("XX_SYNCHRONIZED", "Y");
					rs.updateRow();
				}*/
				modESOLD01 = new X_XX_E_Sold01(getCtx(), XX_E_SOLD01_ID, get_TrxName());
				modESOLD01.setXX_Synchronized(true);
				modESOLD01.save();
				
				commit();
			}

			
			/*
			 * Cierro Conexion AS400
			 */
			
		}catch (SQLException e) {
			e.printStackTrace();
			return "Fallo en Sincronización";
		} finally{
			DB.closeStatement(ps_s);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			as400.desconectar();
		}
		return "Sincronización Completa";
	}
}

