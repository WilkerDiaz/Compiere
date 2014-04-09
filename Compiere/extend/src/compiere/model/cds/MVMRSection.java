package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRSection extends X_XX_VMR_Section{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRSection.class);
	
	public MVMRSection (Ctx ctx, int PO_Section_ID, Trx trx)
	{
		super (ctx, PO_Section_ID, trx);
		if (PO_Section_ID == 0)
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
	
	public MVMRSection (Ctx ctx, ResultSet rs, Trx trx)
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
		X_XX_VMR_Line line = new X_XX_VMR_Line(Env.getCtx(), getXX_VMR_Line_ID(), null);
		X_XX_VMR_TypeInventory type = new X_XX_VMR_TypeInventory(Env.getCtx(), line.getXX_VMR_TypeInventory_ID(), null);
		String sql = "SELECT XX_VMR_TypeBasic_ID FROM XX_VMR_TypeBasic WHERE value = 'Z' ";
		
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
		
		if(type.getValue().equals("B") && getXX_VMR_TypeBasic_ID()==0){
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs =  pstmt.executeQuery();
				if(rs.next()){
					setXX_VMR_TypeBasic_ID(rs.getInt(1));
				}
			}catch (SQLException e) {
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else if(!type.getValue().equals("B")){
			setXX_VMR_TypeBasic_ID(0);
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
			
		if(!isActive()){ //Si desactivo la seccion
			
			//Desactivamos las Lineas, Secciones, Productos y Referencias
			String SQL = "UPDATE XX_VMR_SECTION SET ISACTIVE='N' WHERE XX_VMR_SECTION_ID =" + get_ID();
	
			DB.executeUpdate(get_Trx(), SQL );
			
			SQL	= "UPDATE M_PRODUCT SET ISACTIVE='N' WHERE XX_VMR_SECTION_ID =" + get_ID();
	
			DB.executeUpdate(get_Trx(), SQL );
			
			SQL = "UPDATE XX_VMR_VENDORPRODREF SET ISACTIVE='N' WHERE XX_VMR_SECTION_ID = " + get_ID();
			
			DB.executeUpdate(get_Trx(), SQL );
		}
		
		Integer basic = getXX_VMR_TypeBasic_ID();
		if(basic==0)
			basic = null;
		
		String SQLSectionRef = "UPDATE XX_VMR_VENDORPRODREF SET XX_VMR_TYPEBASIC_ID = "+ basic +
				" WHERE XX_VMR_SECTION_ID = "+get_ID()+" AND XX_VMR_TYPEBASIC_ID NOT IN "+
				"(SELECT t.XX_VMR_TYPEBASIC_ID FROM XX_VMR_TYPEBASIC t WHERE t.XX_VMR_ClassificationLevel = 'R')";
		DB.executeUpdate(get_Trx(), SQLSectionRef );
		return true;
								
	}//afterSave

}
