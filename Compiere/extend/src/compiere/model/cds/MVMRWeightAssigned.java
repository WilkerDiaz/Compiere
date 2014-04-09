package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRWeightAssigned extends X_XX_VMR_WeightAssigned {

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRWeightAssigned.class);

	public MVMRWeightAssigned(Ctx ctx, int XX_VMR_WeightAssigned_ID,Trx trx) {
		super(ctx, XX_VMR_WeightAssigned_ID, trx);
	}
	public MVMRWeightAssigned(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	protected boolean beforeSave(boolean newRecord)
	{
		
		String sql = "";
		int linea = getXX_VMR_Line_ID();
		int seccion = getXX_VMR_Section_ID();
		int conceptoValor = getXX_VME_ConceptValue_ID();
		int marca = getXX_VMR_Brand_ID();
		int store = getXX_Store_ID();		
		
		if(seccion>0 && store==0){
			setXX_WeightType(1);
		}
		else if(linea>0 && seccion==0 && conceptoValor==0){
			setXX_WeightType(2);
		}
		else if(conceptoValor>0){
			setXX_WeightType(3);
		}
		else if(marca>0){
			setXX_WeightType(4);
		}
		else if(store>0){
			setXX_WeightType(5);
		}
		
		int type = getXX_WeightType();

		if (type==1){
			sql = "select * from XX_VMR_WeightAssigned where XX_VMR_Department_ID = "+getXX_VMR_Department_ID()+"" +
				  " and XX_VMR_Line_ID = "+linea+" and XX_VMR_Section_ID = "+seccion+" and XX_VME_ConceptValue_ID is null " +
				  " and XX_VMR_Brand_ID is null and XX_WeightType=1 and XX_Store_ID IS NULL and XX_VMR_WeightAssigned_ID <> "+getXX_VMR_WeightAssigned_ID()+"";
		}
		else
			if (type==2)
			{
				sql = "select * from XX_VMR_WeightAssigned where XX_VMR_Department_ID = "+getXX_VMR_Department_ID()+"" +
				  " and XX_VMR_Line_ID = "+linea+" and XX_VMR_Section_ID is null and XX_VME_ConceptValue_ID is null" +
				  " and XX_VMR_Brand_ID is null and XX_WeightType=2 and XX_Store_ID IS NULL and XX_VMR_WeightAssigned_ID <> "+getXX_VMR_WeightAssigned_ID()+"";
			}
			else
				if (type==3)
				{
					sql = "select * from XX_VMR_WeightAssigned where XX_VMR_Department_ID = "+getXX_VMR_Department_ID()+"" +
					  " and XX_VMR_Line_ID = "+linea+" and XX_VME_ConceptValue_ID = "+conceptoValor+" and XX_VMR_Section_ID is null" +
					  " and XX_VMR_Brand_ID is null and XX_WeightType=3 and XX_Store_ID IS NULL and XX_VMR_WeightAssigned_ID <> "+getXX_VMR_WeightAssigned_ID()+"";
				}
				else
					if (type==4)
					{
						sql = "select * from XX_VMR_WeightAssigned where XX_VMR_Department_ID = "+getXX_VMR_Department_ID()+"" +
						  " and XX_VMR_Brand_ID = "+marca+" and XX_VME_ConceptValue_ID is null and XX_VMR_Line_ID is null" +
						  " and XX_VMR_Section_ID is null and XX_WeightType=4 and XX_Store_ID IS NULL and XX_VMR_WeightAssigned_ID <> "+getXX_VMR_WeightAssigned_ID()+"";
					}
					else if(type==5){
						sql = "select * from XX_VMR_WeightAssigned where XX_VMR_Department_ID = "+getXX_VMR_Department_ID()+"" +
						  " and XX_VMR_Line_ID = "+linea+" and XX_VMR_Section_ID = "+seccion+" and XX_VME_ConceptValue_ID is null " +
						  " and XX_VMR_Brand_ID is null and XX_WeightType=5 and XX_Store_ID = "+store+" and XX_VMR_WeightAssigned_ID <> "+getXX_VMR_WeightAssigned_ID()+"";
						}
		
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null;
		try{
			priceRulePstmt = DB.prepareStatement(sql, null);
			rs = priceRulePstmt.executeQuery();
			if (rs.next()){
				log.saveError("Error", Msg.getMsg(getCtx(), "Created"));
				return false;}
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {priceRulePstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	return true;
	}
	
	

}
