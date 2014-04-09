package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.X_XX_VMR_PatternOfDiscount;

public class XX_ExportPatternOfDiscount extends SvrProcess{

	Calendar calendario = Calendar.getInstance();
	
	@Override
	protected String doIt() throws Exception 
	{
		//Obtiene fecha actual del sistema
		calendario.getTime();
		int r=0;
		As400DbManager As = new As400DbManager();
		
		//Si el día es el 1ero del mes le resto uno al mes para obtener lo registros 
		//de la Pauta de Rebajas en el mes actual 
		if(calendario.get(Calendar.DAY_OF_MONTH) == 1)
		{
			calendario.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		int month = 0;
		int year = 0;
		
		month = calendario.get(Calendar.MONTH)+1;
		year = calendario.get(Calendar.YEAR);
		String fecha = "";
		
		if(month<10){
			 fecha = "0"+month+year;
		}else{
			fecha = ""+month+year;
		}
		
		//Busca la información de la Pauta de Rebajas en el mes actual
		String SQL = "SELECT * FROM XX_VMR_PatternOfDiscount "
				   + "WHERE to_char(Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mmyyyy'),'mmyyyy')";
		
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();			
		    
			while(rs.next())
			{
				X_XX_VMR_PatternOfDiscount pd = new X_XX_VMR_PatternOfDiscount(Env.getCtx(),rs.getInt("XX_VMR_PatternOfDiscount_ID"),null);
				
				//Exporta la Pauta de Rebajas al AS/400 
				r=exportPatternOfDiscountDB2(pd,As);	
				
				if(r<0)
					return "No se pudo realizar la sincronización";
				
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}		
		
		return "Se realizó la sincronización";
	}
	
	/*
	 * Exporta la Pauta de Rebajas de Compiere al AS/400
	 */
	public int exportPatternOfDiscountDB2(X_XX_VMR_PatternOfDiscount pd, As400DbManager As)
	{
		String SQL;
		String SQL1;
		int r = 0;
        int r2 = 0;
        MProduct producto = new MProduct(Env.getCtx(),pd.getM_Product_ID(),null);
        MVMRDepartment depart = new MVMRDepartment(Env.getCtx(),producto.getXX_VMR_Department_ID(),null);
        
        //Conexión con el AS/400
        As.conectar();
        	
        //Hay que cambiar la longitud del campo CAN_FIN en la tabla INVT53 del AS/400
        try
        {    
        	//Borra la data de la tabla de Pauta de Rebajas en el AS/400
        	//prueba con tabla ROSMA en el AS/400
        	SQL = "DELETE FROM BECOFILE.ROSMA"; //INVT53
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarSentencia(SQL, sentencia);
            
            try
            {
            	if(r>0) //Si se borró la tabla correctamente
            	{
	                //Query que permite hacer un registro nuevo en la pauta de rebajas en el AS/400
            		//prueba con tabla ROSMA en el AS/400
	                SQL1 = "INSERT INTO BECOFILE.ROSMA(" //INVT53
	                 	 + "CODPRO,"
	                	 + "CODDEP,"
	                     + "CAN_FIN,"
	                     + "MTO_FIN,"
	                     + "ROTACIO,"
	                     + "PORROTPRE,"
	                     + "CAN_PRM_20,"
	                     + "MTO_PRM_20,"
	                     + "CAN_PRM_30,"
	                     + "MTO_PRM_30,"
	                     + "CAN_REB_50,"
	                     + "MTO_REB_50) "
	                     + "VALUES("
	                     + producto.getValue()+","
	                     + depart.getValue()+","
	                     + pd.getXX_FinalInventoryQuantity()+","
	                     + pd.getXX_FinalInventoryAmount()+","
	                     + "0,"
	                     + "0,"
	                     + pd.getXX_QuantityPromotion20()+","
	                     + pd.getXX_AmountPromotion20()+","
	                     + pd.getXX_QuantityPromotion30()+","
	                     + pd.getXX_AmountPromotion30()+","
	                     + pd.getXX_QuantityPromotion50()+","
	                     + pd.getXX_AmountPromotion50()+")";
	                                           		
	                     sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	                     r2 = As.realizarSentencia(SQL1, sentencia);    	                     
            	}            	
            	sentencia.close();
	        }
            catch (Exception e) {
				e.printStackTrace();
			}	         
           
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(r2<0) //Si la inserción da error
			return r2;
		
		return r;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
