package compiere.model.cds.imports;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.X_I_XX_VMR_Solm01;
import compiere.model.cds.X_XX_VMR_Solm01;


public class ImportVMRSolm01 extends SvrProcess {

	/**	Client to be imported to*/
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean s_deleteOldImported = false;


	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
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
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_VMR_Solm01 "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(s_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Reset=" + no);
		
		//Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 i " 
			+ " SET XX_VMR_Department_ID = (SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT d "
			+ " WHERE i.XX_CodDep = d.Value and d.IsActive = 'Y') "
			+ " WHERE i.XX_CodDep IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
		
		//Updating Product ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 i " 
			+ " SET M_PRODUCT_ID = (SELECT M_PRODUCT_ID FROM M_PRODUCT p "
			+ " WHERE i.XX_PRDORI = p.Value and p.IsActive = 'Y') "
			+ " WHERE i.XX_PRDORI IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Product ID=" + no);
		
		commit();
		
		//Updating Warehouse ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 i " 
			+ " SET M_Warehouse_ID = (SELECT M_Warehouse_ID FROM M_Warehouse s "
			+ " WHERE i.XX_TIENDA = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_TIENDA IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Warehouse ID=" + no);
		
		
		// XX_CANORI Consecutivo de precio
		sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 i " 
			+ " SET XX_VMR_PriceConsecutive_ID = (SELECT XX_VMR_PriceConsecutive_ID FROM XX_VMR_PriceConsecutive s "
			+ " WHERE i.XX_CANORI = s.XX_PRICECONSECUTIVE and i.M_PRODUCT_ID = s.M_PRODUCT_ID and s.IsActive = 'Y') "
			+ " WHERE i.XX_CANORI IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Warehouse ID=" + no);
		
		
		// XX_USRCRE Ad_User
		sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 i " 
				+ " SET AD_USER_ID = (SELECT AD_USER_ID FROM AD_USER s "
				+ " WHERE i.XX_USRCRE = s.Value and s.IsActive = 'Y') "
				+ " WHERE i.XX_USRCRE IS NOT NULL "
				+ " AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Set Warehouse ID=" + no);

		
		/*sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 i "
				+ "SET XX_VMR_Solm01_ID=(SELECT XX_VMR_Solm01_ID FROM XX_VMR_Solm01 "
				+ "WHERE i.XX_NUMSOL=XX_REQUESTNUMBER AND AD_CLIENT_ID=i.AD_CLIENT_ID)"
				+ "WHERE i.XX_VMR_Solm01_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Found Solm1=" + no);	*/

		
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		
		commit();
	//-----------------------------------------------------------------------------------
		
		int noInsert = 0;

		//	Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer ("SELECT * FROM I_XX_VMR_Solm01 "
			+ " WHERE I_IsImported='N'").append(clientCheck);
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VMR_Solm01 impIsolm = new X_I_XX_VMR_Solm01 (getCtx(), rs, get_TrxName());
				log.fine("I_XX_VMR_SOLM01_ID=" + impIsolm.getI_XX_VMR_SOLM01_ID());
			
//				****	Create/Update Solm01	****
				
				X_XX_VMR_Solm01 impXsolm = null;
// Pregunto por ID de la tabla compiere que es igual al de la tabla I
				if(impIsolm.getXX_VMR_Solm01_ID() == 0)
				{
					impXsolm = new X_XX_VMR_Solm01(getCtx(), 0, get_TrxName());
				}	
				else  
				{
						impXsolm = 	new X_XX_VMR_Solm01(getCtx(), impIsolm.getXX_VMR_Solm01_ID(), get_TrxName());
				}					 		
			
					impXsolm.setXX_RequestNumber(impIsolm.getXX_NUMSOL());
							
					impXsolm.setXX_ApplicationDescription(impIsolm.getXX_DESSOL());
								
					impXsolm.setXX_TypeOfApplication(impIsolm.getXX_TIPSOL());
				
					impXsolm.setXX_RequestDay(impIsolm.getXX_DIASOL());
				
					impXsolm.setXX_MonthApplication(impIsolm.getXX_MESSOL());
				
					impXsolm.setXX_ApplicationYear(impIsolm.getXX_AÑOSOL());
				
					impXsolm.setM_Warehouse_ID(impIsolm.getXX_TIENDA().intValue());
				
					impXsolm.setXX_DayRegistration(impIsolm.getXX_DiaReg());
				
					impXsolm.setXX_MonthRegister(impIsolm.getXX_MesReg());
				
					impXsolm.setXX_YearRegistration(impIsolm.getXX_AñoReg());
				
					impXsolm.setXX_DayStatus(impIsolm.getXX_DIASTA());
				
					impXsolm.setXX_MonthStatus(impIsolm.getXX_MESSTA());
				
					impXsolm.setXX_YearStatus(impIsolm.getXX_AÑOSTA());
				
					impXsolm.setXX_ShopByRoomStatus(impIsolm.getXX_STDESP());
				
					impXsolm.setXX_DayReleaseStatus(impIsolm.getXX_DSTDES());
				
					impXsolm.setXX_MonthReleaseStatus(impIsolm.getXX_MSTDES());
				
					impXsolm.setXX_YearOfReleaseStatus(impIsolm.getXX_ASTDES());
				
					impXsolm.setXX_ReleaseGuide(impIsolm.getXX_GUIDES());
				
					impXsolm.setXX_NumberOfPackages(impIsolm.getXX_CANBUL());
				
					impXsolm.setXX_UserCreation(impIsolm.getXX_USRCRE());
				
					impXsolm.setXX_UserUpdate(impIsolm.getXX_USRACT());
				
					impXsolm.setXX_ApplicationStatus(impIsolm.getXX_STASOL());
								
					impXsolm.setXX_CancellationCode(impIsolm.getXX_CODASO());
				
					impXsolm.setXX_VMR_Department_ID(new Integer(impIsolm.getXX_CodDep()));
				
					impXsolm.setM_Product_ID(impIsolm.getXX_PRDORI().intValue());
					
					impXsolm.setXX_ConsecutivePrice(impIsolm.getXX_CONORI());
				
					impXsolm.setXX_QuantityOfProductStorage(impIsolm.getXX_CONORI());
				
					impXsolm.setXX_CodeStore2(impIsolm.getXX_TADESP());
					
					
				//
				if(impXsolm.save())
				{
					log.finest("Insert/Update Solm01 - " +  impXsolm.getXX_VMR_Solm01_ID());
					noInsert++;
									
					impIsolm.setI_IsImported(true);
					impIsolm.setProcessed(true);
					impIsolm.setProcessing(false);
					impIsolm.save();
				}
				else
				{		
					rollback();
					noInsert--;						
					sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
							.append("Cannot Insert Solm01...")
							.append("' WHERE I_XX_VMR_Solm01_ID=").append(impIsolm.getI_XX_VMR_SOLM01_ID());
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
		
			sql = new StringBuffer ("UPDATE I_XX_VMR_Solm01 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@XX_VMR_Solm01_ID@: @Inserted@");
			return "";
			
			//
	
	} // end doIt

} // end ImportVRMSolm01
