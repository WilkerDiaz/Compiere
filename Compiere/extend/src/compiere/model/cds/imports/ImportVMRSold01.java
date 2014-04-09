package compiere.model.cds.imports;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.X_I_XX_VMR_Sold01;
import compiere.model.cds.X_XX_VMR_Sold01;

public class ImportVMRSold01 extends SvrProcess {

	/**	Client to be imported to*/
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean s_deleteOldImported = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	//
	public void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null);
			
			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				s_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}


	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_VMR_Sold01 "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_VMR_Sold01 "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(s_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Reset=" + no);
		//
		sql = new StringBuffer ("UPDATE I_XX_VMR_Sold01 i "
				+ "SET XX_VMR_Sold01_ID=(SELECT XX_VMR_Sold01_ID FROM XX_VMR_Sold01 "
				+ "WHERE i.XX_NUMSOL=XX_ApplicationNumber AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.XX_VMR_Sold01_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Found Sold1=" + no);
			
			String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
			
		//TODO: Hacer búsquedas de IDs
			
			commit();
		//-----------------------------------------------------------------------------------
			int noInsert = 0;

			//	Go through Records
			log.fine("start inserting...");
			sql = new StringBuffer ("SELECT * FROM I_XX_VMR_Sold01 "
				+ " WHERE I_IsImported='N'").append(clientCheck);
			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					X_I_XX_VMR_Sold01 impIsold = new X_I_XX_VMR_Sold01 (getCtx(), rs, get_TrxName());
					log.fine("I_XX_VMR_SOLM01_ID=" + impIsold.getI_XX_VMR_SOLD01_ID());
					
                         //***	Create/Update Solm01	****
					
					X_XX_VMR_Sold01 impXsold = null;
										
	// Pregunto por ID de la tabla compiere que es igual al de la tabla I //
					
					if(impIsold.getXX_VMR_Sold01_ID() == 0)
					{
						impXsold = new X_XX_VMR_Sold01(getCtx(), 0, get_TrxName());
					}	
					else  
					{
							impXsold = 	new X_XX_VMR_Sold01(getCtx(), impIsold.getXX_VMR_Sold01_ID(), get_TrxName());
					}
					
					// get and set
						
					
					
					impXsold.setXX_ApplicationNumber(impIsold.getXX_NUMSOL());
					
					impXsold.setM_Product_ID(impIsold.getXX_CODPRO().intValue());
					
					impXsold.setXX_VMR_Department_ID(new Integer(impIsold.getXX_CodDep()));
					
					impXsold.setXX_VMR_Line_ID(new Integer (impIsold.getXX_CodLin()));
					
					impXsold.setXX_VMR_Section_ID(new Integer (impIsold.getXX_Seccio()));
					
					impXsold.setXX_MovementTypeApplication(impIsold.getXX_TIMOSO());
					
					impXsold.setXX_CodeMovementRequest(impIsold.getXX_COMOSO());
					
					impXsold.setXX_QuantityProductStore(impIsold.getXX_CANPRO());
					
					impXsold.setXX_QuantityProductBuyer(impIsold.getXX_CAPRAP());
					
					impXsold.setXX_QuantityPhysicalStore(impIsold.getXX_CANFIS());
					
					impXsold.setXX_QuantityProductStorage(impIsold.getXX_CANDEP());
					
					impXsold.setXX_MovementRequestStatus(impIsold.getXX_STMOSO());
					
					impXsold.setXX_MovementDayStatus(impIsold.getXX_DSTMOV());
					
					impXsold.setXX_MonthStatusMovement(impIsold.getXX_MSTMOV());
					
					impXsold.setXX_YearMovementStatus(impIsold.getXX_ASTMOV());
					
					impXsold.setXX_AnnulmentProductCode(impIsold.getXX_CODAPR());
					
					impXsold.setM_Warehouse_ID(impIsold.getXX_TIENDA().intValue());
					
					impXsold.setXX_PricePromotion(impIsold.getXX_PREPRM());
					
					impXsold.setXX_DiscountPromotion(impIsold.getXX_DSCPRM());
					
					impXsold.setXX_ConsecutivePrice(impIsold.getXX_CONPRE());
					
					impXsold.setXX_ConsecutivePrices(impIsold.getXX_COPRVI());
					
					impXsold.setXX_ProductCode(impIsold.getXX_CPROVI());
					
					impXsold.setXX_AlternateCodePEREMP(impIsold.getXX_COEMPE());
					
//JessicaM					impXsold.setXX_AmountCost(impIsold.getXX_Cosant());
					
					impXsold.setXX_AmountOfSale(impIsold.getXX_VENANT());
					
					
					//
					if(impXsold.save())
					{
						log.finest("Insert/Update Sold01 - " +  impXsold.getXX_VMR_Sold01_ID());
						
						impIsold.setXX_VMR_Sold01_ID(impXsold.getXX_VMR_Sold01_ID());
						
						noInsert++;
										
						impIsold.setI_IsImported(true);
						impIsold.setProcessed(true);
						impIsold.setProcessing(false);
						impIsold.save();
					}
					else
					{		
						rollback();
						noInsert--;						
						sql = new StringBuffer ("UPDATE I_XX_VMR_Sold01 i "
								+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
								.append("Cannot Insert Sold01...")
								.append("' WHERE I_XX_VMR_Sold01_ID=").append(impIsold.getI_XX_VMR_SOLD01_ID());
							DB.executeUpdate(get_Trx(), sql.toString());
							//continue;							
					}

					commit();
					//
					
				} // end while
				
				rs.close();
				pstmt.close();
				
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "", e);
				rollback();
			}
		
			//	Set Error to indicator to not imported
			
			sql = new StringBuffer ("UPDATE I_XX_VMR_Sold01 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@XX_VMR_Sold01_ID@: @Inserted@");
			return "";
	}
	
}
