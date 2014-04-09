package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRLongCharacteristic extends X_XX_VMR_LongCharacteristic{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	public MVMRLongCharacteristic (Ctx ctx, int carac_id, Trx trx)
	{
		super (ctx, carac_id, trx);
		if (carac_id == 0)
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
	
	public MVMRLongCharacteristic (Ctx ctx, ResultSet rs, Trx trx)
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
		setName(get_ValueAsString("Name").toUpperCase());
		
		String SQL = "SELECT * FROM XX_VMR_LongCharacteristic "
			   + "WHERE XX_VMR_Department_ID="+getXX_VMR_Department_ID()+ " AND XX_VMR_Line_ID="+getXX_VMR_Line_ID() + 
			   " AND XX_VMR_Section_ID="+getXX_VMR_Section_ID() + " AND isactive='Y' AND name = '" + getName()+"'";
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				log.saveError("", Msg.getMsg(getCtx(), "XX_CharacteristicRepeated"));
				return false;
			}
						
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
			
			
		return true;
					
	}//beforeSave
	


}

