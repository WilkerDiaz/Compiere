package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRDepartment extends X_XX_VMR_Department{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	public MVMRDepartment (Ctx ctx, int PO_Department_ID, Trx trx)
	{
		super (ctx, PO_Department_ID, trx);
		if (PO_Department_ID == 0)
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
	
	public MVMRDepartment (Ctx ctx, ResultSet rs, Trx trx)
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
		
		/*El campo de comprador solo puede ser modificado por el jefe de categor�a del departamemto o Gerente de Merchandising */
		if(getXX_UserBuyer_ID() != get_ValueOldAsInt("XX_UserBuyer_ID")){
			int user_ID = Env.getCtx().getContextAsInt("##AD_User_ID");
			MUser user = new  MUser(Env.getCtx(), user_ID, null); 
			
			MVMRCategory category = new MVMRCategory(getCtx(), getXX_VMR_Category_ID(), null);
			
			if(user.getC_BPartner_ID() != category.getXX_CategoryManager_ID() && Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID")!= Env.getCtx().getContextAsInt("#AD_Role_ID") ){
				ADialog.error(1, new Container(), "El campo de comprador solo puede ser modificado por el jefe de categor�a del departamemto o Gerente de Merchandising." );
				return false;
			}
		}
		
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
			
		if(!isActive()){ //Si desactivo la el departamento
			
			//Desactivamos las Lineas, Secciones, Productos y Referencias
			String SQL = "UPDATE XX_VMR_LINE SET ISACTIVE='N' "+
						 "WHERE XX_VMR_DEPARTMENT_ID = " + get_ID();
			
			DB.executeUpdate(get_Trx(), SQL );
			
			SQL = "UPDATE XX_VMR_SECTION SET ISACTIVE='N' WHERE XX_VMR_LINE_ID IN "+
				    "(SELECT XX_VMR_LINE_ID FROM XX_VMR_LINE WHERE XX_VMR_DEPARTMENT_ID = "+  get_ID() +")";
	
			DB.executeUpdate(get_Trx(), SQL );
			
			SQL	= "UPDATE M_PRODUCT SET ISACTIVE='N' WHERE XX_VMR_DEPARTMENT_ID =" + get_ID();
	
			DB.executeUpdate(get_Trx(), SQL );
			
			SQL = "UPDATE XX_VMR_VENDORPRODREF SET ISACTIVE='N' WHERE XX_VMR_DEPARTMENT_ID = " + get_ID();
			
			DB.executeUpdate(get_Trx(), SQL );
		}
			
		return true;
								
	}//afterSave

}