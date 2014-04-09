package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VCN_SalePurchase;
import compiere.model.cds.X_XX_VCN_SalePurchase;

public class ImportVCNInvd02 extends SvrProcess {


	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;
	
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null);

			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("DeleteOldImported"))
				s_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	} // end prepare
	
	protected String doIt() throws Exception {
		
		String store = null;
		//String store1 = null;
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		// **** Prepare **** //

		// Delete Old Imported
		if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_VCN_SALEPURCHASE "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		
		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_VCN_SALEPURCHASE "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(
				s_AD_Client_ID).append(
				")," + " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
				+ " IsActive = COALESCE (IsActive, 'Y'),"
				+ " Created = COALESCE (Created, SysDate),"
				+ " CreatedBy = COALESCE (CreatedBy, 0),"
				+ " Updated = COALESCE (Updated, SysDate),"
				+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
				+ " I_ErrorMsg = NULL," + " I_IsImported = 'N' "
				+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Reset=" + no);
		
	
		// Department
		sql = new StringBuffer(
				"UPDATE I_XX_VCN_SALEPURCHASE i "
			  + "SET i.XX_VMR_Department_ID= " +
						"(SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT "
					   + "WHERE TO_NUMBER(i.XX_CVDPT) = value AND ROWNUM = 1) "
			  + "WHERE i.XX_VMR_DEPARTMENT_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL "
		      + "AND I_IsImported='N' AND i.XX_CVDPT IS NOT NULL ");
		
		
		/*sql = new StringBuffer("UPDATE I_XX_VCN_SALEPURCHASE i "
				+ "SET i.XX_VMR_DEPARTMENT_ID=(SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT "
				+ "WHERE i.XX_CVDPT= VALUE AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.XX_VMR_DEPARTMENT_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_CVDPT IS NOT NULL");*/
				
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
	
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		
		
		//X_I_XX_VCN_SalePurchase salesPurc = new X_I_XX_VCN_SalePurchase (getCtx(), getRecord_ID(), get_TrxName());
		
		String SQL1 = ("SELECT XX_CVTIE AS TIENDA " +
				"FROM I_XX_VCN_SalePurchase " +
				"WHERE I_XX_VCN_SalePurchase_ID = '"+getRecord_ID()+"' ");
		
		try
		{
			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
		    ResultSet rs1 = pstmt1.executeQuery();
			
			if(rs1.next())
			{
				store = rs1.getString("TIENDA");
				
				if(store.length()==1)
				{
					store ="00"+store;
				}
				else if(store.length()==2)
				{
					store ="0"+store;
				}
			}
			rs1.close();
			pstmt1.close();
			
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
		}
		
		sql = new StringBuffer("UPDATE I_XX_VCN_SALEPURCHASE i "
				+ "SET i.M_WAREHOUSE_ID=(SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE "
				+ "WHERE '"+store+"'=Value AND AD_CLIENT_ID=i.AD_CLIENT_ID AND ISACTIVE='Y') "
				+ "WHERE i.M_WAREHOUSE_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_CVTIE IS NOT NULL");
				
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Warehouse ID=" + no);
		
		/*String ts1 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		*/
		
		sql = new StringBuffer ("UPDATE I_XX_VCN_SALEPURCHASE i " 
				+ " SET i.XX_TYPEREG = (CASE WHEN (XX_CVTRE ='1') THEN 'VC' " 
				+ " WHEN (XX_CVTRE ='2') THEN 'MDSV' " +
						" WHEN (XX_CVTRE ='3') THEN 'TVN' " +
						" WHEN (XX_CVTRE ='4') THEN '04' " +
						" WHEN (XX_CVTRE ='5') THEN '05' " +
						" WHEN (XX_CVTRE ='6') THEN '06' " +
						" WHEN (XX_CVTRE ='7') THEN 'TVM' " +
						" WHEN (XX_CVTRE ='8') THEN '08' " +
						" WHEN (XX_CVTRE ='9') THEN 'IIPV' " +
						" WHEN (XX_CVTRE ='10') THEN 'CPV' " +
						" WHEN (XX_CVTRE ='11') THEN 'AEE' " +
						" WHEN (XX_CVTRE ='12') THEN '12' " +
						" WHEN (XX_CVTRE ='13') THEN '13' " +
						" WHEN (XX_CVTRE ='14') THEN 'MD' " +
						" WHEN (XX_CVTRE ='15') THEN 'MRE' " +
						" WHEN (XX_CVTRE ='16') THEN '16' " +
						" WHEN (XX_CVTRE ='17') THEN 'MVAC' " +
						" WHEN (XX_CVTRE ='18') THEN 'MRMIF' " +
						" WHEN (XX_CVTRE ='19') THEN 'IFPV' " +
						" WHEN (XX_CVTRE ='20') THEN '20' " +
						" WHEN (XX_CVTRE ='21') THEN 'MGI' " +
						" WHEN (XX_CVTRE ='22') THEN 'MSC' " +
						" WHEN (XX_CVTRE ='23') THEN 'MAE' " +
						" WHEN (XX_CVTRE ='24') THEN '24' " +
						" WHEN (XX_CVTRE ='25') THEN 'MGSD' " +
						" WHEN (XX_CVTRE ='26') THEN 'MMGF' " +
						" WHEN (XX_CVTRE ='27') THEN 'MMR' " +
						" WHEN (XX_CVTRE ='28') THEN 'MRMPIF' " +
						" WHEN (XX_CVTRE ='29') THEN '29' " +
						" WHEN (XX_CVTRE ='30') THEN 'MG' " +
						" WHEN (XX_CVTRE ='31') THEN 'MVC' " +
						" WHEN (XX_CVTRE ='32') THEN '32' " +
						" WHEN (XX_CVTRE ='33') THEN 'CDV' " +
						" WHEN (XX_CVTRE ='34') THEN '34' " +
						" WHEN (XX_CVTRE ='35') THEN 'CVAC' " +
						" WHEN (XX_CVTRE ='36') THEN 'TCDV' " +
						" WHEN (XX_CVTRE ='37') THEN '37' " +
						" WHEN (XX_CVTRE ='38') THEN 'GBSV' " +
						" WHEN (XX_CVTRE ='39') THEN 'PSV' " +
						" ELSE 'PMPGSIF' END) "
				+ " WHERE I_IsImported<>'Y' ");
		
		
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Inventory Type=" + no);	

		commit();
	//-----------------------------------------------------------------------------------------------------------------------------
		
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VCN_SALEPURCHASE "
				+ " WHERE I_IsImported='N'");

		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),
					get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VCN_SalePurchase impIsalpurc = new X_I_XX_VCN_SalePurchase (getCtx(), rs, get_TrxName());
				log.fine("I_XX_VCN_SalePurchase_ID=" + impIsalpurc.getI_XX_VCN_SALEPURCHASE_ID());
				
				// *** Create/Update **** //
				X_XX_VCN_SalePurchase impXsalpurc = null;
				
								
				// Pregunto por ID de la tabla compiere que es igual al de la
				// tabla I //
				if(impIsalpurc.getXX_VCN_SALEPURCHASE_ID() == 0)
				{
					impXsalpurc = new X_XX_VCN_SalePurchase(getCtx(), 0, get_TrxName()); 
				}
				else
				{
					impXsalpurc = new X_XX_VCN_SalePurchase(getCtx(), impIsalpurc.getXX_VCN_SALEPURCHASE_ID(), get_TrxName());
				}
				
				Locale locale = new Locale("es", "ES");
				NumberFormat nf = NumberFormat.getInstance(locale);
				
				Number numero = nf.parse(impIsalpurc.getXX_AMOUNTACU());	
				Number numero1 = nf.parse(impIsalpurc.getXX_AMOUNTACU());
				
				BigDecimal acumulado = new BigDecimal(numero.toString());
				BigDecimal acumuladoMes = new BigDecimal(numero1.toString());
				
				impXsalpurc.setXX_Year(new Integer(impIsalpurc.getXX_Year()));
				impXsalpurc.setXX_Month(new Integer(impIsalpurc.getXX_Month()));
				impXsalpurc.setXX_VMR_Department_ID(impIsalpurc.getXX_VMR_Department_ID());
				impXsalpurc.setM_Warehouse_ID(impIsalpurc.getM_Warehouse_ID());
				impXsalpurc.setXX_TypeReg(impIsalpurc.getXX_TYPEREG());
				impXsalpurc.setXX_AmountAcu(acumulado);
				impXsalpurc.setXX_AmountMonth(acumuladoMes);
				
				
				if(impXsalpurc.save())
				{
					log.finest("Insert/Update SalesPurchase - "
							+ impXsalpurc.getXX_VCN_SALEPURCHASE_ID());
					
					impIsalpurc.setXX_VCN_SALEPURCHASE_ID(impXsalpurc.getXX_VCN_SALEPURCHASE_ID());
					
					noInsert++;
					
					impIsalpurc.setI_IsImported(true);
					impIsalpurc.setProcessed(true);
					//impIsalpurc.setProcessing(false);
					impIsalpurc.save();
				}
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VCN_SALEPURCHASE i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert SALEPURCHASE...").append(
									"' WHERE I_XX_VCN_SALEPURCHASE_ID=").append(
											impIsalpurc.getI_XX_VCN_SALEPURCHASE_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
					// continue;
				}
				
				commit();
								
			}// end while
			rs.close();
			pstmt.close();
			
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}
		
		// Set Error to indicator to not imported

		sql = new StringBuffer("UPDATE I_XX_VCN_SALEPURCHASE "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VCN_SALEPURCHASE_ID@: @Inserted@");
		return "";
	
	}

}
