package compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MPROMOCIONES extends X_XX_PROMOCIONES {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MPROMOCIONES (Ctx ctx, int XX_PROMOCIONES_ID, Trx trx)
    {super (ctx, XX_PROMOCIONES_ID, trx);}
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public MPROMOCIONES (Ctx ctx, ResultSet rs, Trx trx)
    {super (ctx, rs, trx);}
	
    protected boolean beforeSave(boolean newRecord){
		String sql = "Select TipoPromocion, FechaFin, FechaInicio, prioridad, xx_promociones_id " +
				"from xx_promociones where (to_char(FechaInicio,'yyyy-mm-dd hh:mm:ss.*') " +
				"between '"+getFechaInicio()+"' and '"+getFechaFin()+
				"' or to_char(FechaFin,'yyyy-mm-dd hh:mm:ss.*') between '"+getFechaInicio()+"' and '"+getFechaFin()+"') " +
				"and isactive='Y' and (aprobadomer='Y' or aprobadomar='Y' or aprobadogg='Y') and xx_promociones_id<>"+getXX_PROMOCIONES_ID();
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				int prioridad=rs.getInt("prioridad");
				String tipoPromocion=rs.getString("TipoPromocion");
				int codigo = rs.getInt("xx_promociones_id");
				if (getTipoPromocion().equals(tipoPromocion)){
					if(getTipoPromocion().equals("1000400")) 
						if(getXX_PROMOCIONES_ID()==codigo)
							return true;
						else return false;
					if(getTipoPromocion().equals("1000200"))
						if(getXX_PROMOCIONES_ID()==codigo)
							return true;
						else return false;
					else if(getprioridad()==prioridad){
						if(ADialog.ask(EnvConstants.WINDOW_INFO,null,"¡La promoción numero "+codigo+" conincide en la prioridad," +
								"se colocara una prioridad menor!")){
							setprioridad(getprioridad()+1);
							return true;
						}else return false;
					}
				}else if((getTipoPromocion().equals("1000300") || getTipoPromocion().equals("1000500") 
							|| getTipoPromocion().equals("1000600") || getTipoPromocion().equals("1001300")) 
						&& (tipoPromocion.equals("1000300") || tipoPromocion.equals("1000500") 
								|| tipoPromocion.equals("1000600") || tipoPromocion.equals("1001300"))){
					if(ADialog.ask(EnvConstants.WINDOW_INFO,null,"La promocion numero "+codigo+" conincide en la prioridad," +
							"se colocara una prioridad menor!")){
						setprioridad(getprioridad()+1);
						return true;
					}else return false;
				} 		
			}
		}catch (SQLException e){e.printStackTrace();}
    	return true;
    }
}
