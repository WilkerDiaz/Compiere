package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRConceptValDptBrand extends X_XX_VMR_ConceptValDptBrand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	public MVMRConceptValDptBrand(Ctx ctx, int XX_VMR_ConceptValDptBrand_ID,	Trx trx) 
	{
		super(ctx, XX_VMR_ConceptValDptBrand_ID, trx);
	}//MVMRConceptValDptBrand

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRConceptValDptBrand(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVMRConceptValDptBrand
	
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		String sql= "";
		PreparedStatement priceRulePstmt = null;;
		ResultSet rs = null;
		try{
		///if (newRecord)
		//{
			Integer Departamento = getXX_VMR_Department_ID(); 
			Integer        Marca = getXX_VMR_Brand_ID();
			
			if (newRecord)
				sql = "select * from XX_VMR_CONCEPTVALDPTBRAND  where" +
					     " XX_VMR_DEPARTMENT_ID = "+Departamento+" and XX_VMR_BRAND_ID = "+Marca+" and ISACTIVE = 'Y'";
			else{
				sql = "select * from XX_VMR_CONCEPTVALDPTBRAND  where" +
			     	  " XX_VMR_DEPARTMENT_ID = "+Departamento+" and XX_VMR_BRAND_ID = "+Marca+" and ISACTIVE = 'Y'" +
			     	  " and XX_VMR_CONCEPTVALDPTBRAND_ID != "+getXX_VMR_ConceptValDptBrand_ID();
			}
			priceRulePstmt = DB.prepareStatement(sql, null);
			rs = priceRulePstmt.executeQuery();
			
			if (rs.next())
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "Departamento y Marca Ya poseen Tipo de Concepto de Valor Asignado"));
				return false;
			}
		//}
			
		}catch (Exception e) {
			log.saveError("Error", Msg.getMsg(getCtx(), e.getMessage()));
			return false;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {priceRulePstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	 return true;
	}
	
	
	
}
