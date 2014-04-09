package compiere;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.DB;

/**
 * Clase para métodos comunes relacionados con el proyecto Dinamica Comercial
 * @author jgraterol
 *
 */
public class XX_VMA_VCODUtils {

	/**
	 * Indica si la pagina contiene departamentos asociados
	 * @return boolean
	 */
	public static boolean pageContainsDepartments(int page){
		String sql = "select * from XX_VMA_PageDepartment pd where XX_VMA_BROCHUREPAGE_ID="+page;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				//Si hay departamentos asociados
				return true;
			}else{
				rs.close();
				pstmt.close();
				
			}
		
		} catch(Exception e){
			
		}
		return false;
	}
}
