package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRComercialBudget extends X_XX_VMR_ComercialBudget{
	
	
	private static final long serialVersionUID = 1L;
	
	public MVMRComercialBudget (Ctx ctx, int ComercialBudget_ID, Trx trx)
	{
		super (ctx, ComercialBudget_ID, trx);
		if (ComercialBudget_ID == 0)
		{
		//	setM_Requisition_ID (0);
//			setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_RequisitionLine WHERE M_Requisition_ID=@M_Requisition_ID@
//			setLineNetAmt (Env.ZERO);
//			setPriceActual (Env.ZERO);
//			setQty (Env.ONE);	// 1
//			setMargin (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setSalePrice (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setLinePVPAmount (Env.ZERO);	/////////////////////////////////////////////////////////////////
//			setLinePlusTaxAmount (Env.ZERO);	////////////////////////////////////////////////////////////
//			setPackageMultiple (Env.ONE);				///////////////////////////////////////////

		}
		
	}	
	
	public MVMRComercialBudget (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	

		int year_id = 0;
//		Select * FROM TABLA WHERE TO_CHAR(tab.XX_EntranceDate, 'DD-MM-YYYY')=TO_CHAR(Sysdate, 'DD-MM-YYYY')
				
		// Esta parte verifica que el periodo del presupuesto comercial exista		
		String sql1 = "SELECT C_YEAR_ID"
			+ " FROM C_PERIOD"
			+ " WHERE TO_DATE(TO_CHAR(STARTDATE,'DD-MM-YYYY'))<TO_DATE(TO_CHAR(Sysdate-700+365,'DD-MM-YYYY')) AND TO_DATE(TO_CHAR(ENDDATE,'DD-MM-YYYY'))>TO_DATE(TO_CHAR(Sysdate-700+365,'DD-MM-YYYY'))";

		PreparedStatement prst = DB.prepareStatement(sql1,null);
		ResultSet rs = null;
		try {
				rs = prst.executeQuery();
				if (rs.next())
				{
					year_id = rs.getInt(1);
				}
				else
				{
					ADialog.error(1, new Container(), Msg.getMsg(getCtx(), "XX_PeriodNotCreated"));
					return false;
				}
				
		} catch (SQLException e){
			System.out.println("Es posible que existan mas de un periodo con la misma fecha de inicio y fin " + e);			
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {prst.close();} catch (SQLException e) {e.printStackTrace();}
		}	
		
		setC_Year_ID(year_id);
		
		String yearName = "";
		
		sql1 = "SELECT FiscalYear"
			+ " FROM C_year"
			+ " WHERE c_year_id="+getC_Year_ID();

		prst = DB.prepareStatement(sql1,null);
		try {
				rs = prst.executeQuery();
				if (rs.next())
				{
					yearName = rs.getString(1);
				} 		
				rs.close();
				prst.close();
		
		} catch (SQLException e){
			System.out.println("Es posible que existan mas de dos periodos con la misma fecha de inicio y fin " + e);			
		}	
		setName(yearName);

		
/**     deprecated by Victor
		String periodo = "";
				
		int anioNuevo = 0;
		
		if (mesActual < 7)
			anioNuevo = anioActual;
		else
			anioNuevo = anioActual + 1;
		
		periodo = anioNuevo + "/" + (anioNuevo + 1); 
			
		setXX_YearCreate(periodo);
		
		System.out.println(periodo);
*/		
/**		
		if (getValue()==null)
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "XX_InvalidTaxonomyCode"));
			return false;
		}
					
		Integer CategoryCode = new Integer (getValue().replaceAll(" ", ""));
		if (CategoryCode.intValue()<1 | CategoryCode.intValue()>99)
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "XX_InvalidTaxonomyCode"));
			return false;
		}

		if (CategoryCode.toString().length()==1)
			setValue("0" + CategoryCode.toString());
		else
			setValue(CategoryCode.toString());
		
		setName(get_ValueAsString("Name").toUpperCase());	
		
*/
		
		return true;
					
	}//beforeSave

	
	

}
