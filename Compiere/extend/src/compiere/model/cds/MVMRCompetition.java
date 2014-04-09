package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRCompetition extends X_XX_VMR_Competition{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRCompetition.class);

	public MVMRCompetition(Ctx ctx, int XX_VMR_Competition_ID, Trx trx) {
		super(ctx, XX_VMR_Competition_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MVMRCompetition(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	
		if(getCompetition(get_ValueAsString("Name"))> 0){
			ADialog.error(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_NameAlreadyExists" ));
			return false;
		}
		setName(get_ValueAsString("Name").toUpperCase());	
		
		return true;
					
					
	}//beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
			
		if(!isActive()){ //Si desactivo la Competencia (Marca)
			
			//Desactivamos los Precios asociados a esa competencia
			String SQL = "UPDATE XX_VMR_COMPETITIONPRICE SET ISACTIVE='N' "+
						 "WHERE XX_VMR_COMPETITION_ID = " + get_ID();
			
			DB.executeUpdate(get_Trx(), SQL);
			
		}
			
		return true;
								
	}//afterSave

	private Integer getCompetition(String competition_name) {
		Integer returned = 0;		
		if (competition_name == null) 
			return returned;
		
		String sql = "\nSELECT d.XX_VMR_Competition_ID " +
		"\nFROM XX_VMR_Competition d " +
		"\nWHERE  UPPER(d.name) = '" + competition_name.toUpperCase()+"'";
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = rs.getInt(1);			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		return returned;
	}
}
