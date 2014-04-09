package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRCategory extends X_XX_VMR_Category{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	public MVMRCategory (Ctx ctx, int PO_Category_ID, Trx trx)
	{
		super (ctx, PO_Category_ID, trx);
		if (PO_Category_ID == 0)
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
	
	public MVMRCategory (Ctx ctx, ResultSet rs, Trx trx)
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
		
		return true;
					
	}//beforeSave

}
