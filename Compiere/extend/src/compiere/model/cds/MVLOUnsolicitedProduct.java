package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

public class MVLOUnsolicitedProduct extends X_XX_VLO_UnsolicitedProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVLOUnsolicitedProduct.class);
	
	public MVLOUnsolicitedProduct(Ctx ctx, int XX_VLO_UnsolicitedProduct_ID,
			Trx trx) {
		super(ctx, XX_VLO_UnsolicitedProduct_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MVLOUnsolicitedProduct (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MVLOUnsolicitedProduct
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		boolean save = super.afterSave(newRecord, success);
		
		if(save){
			if(!newRecord)
			{
				MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(get_ID()),null);
				
				//Llama al proceso para cerrar la alerta de Confirmación de los productos en línea genérica		
				if((isXX_Alert()==true)&&(isXX_ValidateProduct()==true)&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("VP")))
				{
					try
					{
						MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task.get_ID()); 
						mpi.save();
						
						ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
						pi.setRecord_ID(mpi.getRecord_ID());
						pi.setAD_PInstance_ID(mpi.get_ID());
						pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
						pi.setClassName(""); 
						pi.setTitle(""); 
						
						ProcessCtl pc = new ProcessCtl(null ,pi,null); 
						pc.start();
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE,e.getMessage());
					}
				}
			}
		}
		return save;
	}
	
	/*
	 *	Obtengo el ID de la tarea critica segun el producto no solicitado 
	 */
	private Integer getCriticalTaskForClose(Integer unsolProd){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			   + "WHERE XX_ActualRecord_ID="+unsolProd;
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
	    ResultSet rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			criticalTask = rs.getInt("XX_VMR_CriticalTaskForClose_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return criticalTask;
	}
	

}
