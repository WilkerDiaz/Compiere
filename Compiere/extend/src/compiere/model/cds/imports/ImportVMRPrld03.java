package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VMR_Prld03;
import compiere.model.cds.X_XX_VMR_Prld03;

public class ImportVMRPrld03 extends SvrProcess {

	/**	Client to be imported to*/
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean s_deleteOldImported = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
		
	@Override
	protected void prepare() 
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
	protected String doIt() throws Exception 
	{
		Integer iprld03Id = 0;
		String dpto = null;
		String section = null;
		String line = null;
		String tienda = null;
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;
/*
		//	****	Prepare	****

		//	Delete Old Imported
		if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_VMR_Prld03 "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 "
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
		
		
		String SQL = ("SELECT I_XX_VMR_PRLD03_ID AS PRLD03ID, XX_CODDEP AS DPTO, XX_CODSEC AS SEC, XX_CODLIN AS LINE, XX_TIENDA AS TIENDA " +
				"FROM I_XX_VMR_PRLD03 ");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    while(rs.next())
		    {
		    	iprld03Id = rs.getInt("PRLD03ID");
		    	dpto =  rs.getString("DPTO");
		    	line = rs.getString("SEC");
		    	section = rs.getString("LINE");
		    	tienda = rs.getString("TIENDA");
		    	
		    	X_I_XX_VMR_Prld03 impIprld = new X_I_XX_VMR_Prld03 (getCtx(), iprld03Id, get_TrxName());
		    	
		    	
		    	if(dpto != null)
		    	{
		    		if(dpto.length() == 1)
			    	{
			    		dpto = "0"+dpto;
			    		System.out.println(dpto);
			    		impIprld.setXX_CodDep(dpto);
			    	}
		    	}
		    	
		    	if(line != null)
		    	{
		    		if(line.length() == 1)
			    	{
			    		line = "0"+line;
			    		impIprld.setXX_CodLin(line);
			    	}
		    	}
		    	
		    	if(section != null)
		    	{
		    		if(section.length() == 1)
			    	{
			    		section = "0"+section;
			    		impIprld.setXX_Codsec(section);
			    	}
		    	}
		    	
		    	if(tienda != null)
		    	{
		    		if(tienda.length() == 1)
			    	{
			    		tienda = "00"+tienda;
			    		impIprld.setXX_TIENDA(tienda);
			    	}
		    		if(tienda.length() == 2)
		    		{
		    			tienda = "0"+tienda;
			    		impIprld.setXX_TIENDA(tienda);
			    		System.out.println(tienda);
		    		}
		    	}
		    
		    	impIprld.save();
		    	commit();
		    }
			rs.close();
			pstmt.close();
			
		}
		catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		
		//	Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 i " 
			+ " SET XX_VMR_Department_ID = (SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT d "
			+ " WHERE i.XX_CodDep = d.Value and d.IsActive = 'Y') "
			+ " WHERE i.XX_CodDep IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
				
		//	Updating Line ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 i " 
			+ " SET XX_VMR_Line_ID = (SELECT XX_VMR_Line_ID FROM XX_VMR_Line l "
			+ " WHERE i.XX_CodLin = l.Value and l.IsActive = 'Y'" 
			+ " AND i.XX_VMR_Department_ID = l.XX_VMR_Department_ID) "
			+ " WHERE i.XX_CodLin IS NOT NULL AND XX_VMR_Department_ID IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Line ID=" + no);
		System.out.println("listo linea");
		
		//	Updating Section ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 i " 
			+ " SET XX_VMR_Section_ID = (SELECT XX_VMR_Section_ID FROM XX_VMR_Section s "
			+ " WHERE i.XX_Codsec = s.Value and s.IsActive = 'Y'" 
			+ " AND i.XX_VMR_Line_ID = s.XX_VMR_Line_ID) "
			+ " WHERE i.XX_Codsec IS NOT NULL AND XX_VMR_Line_ID IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Section ID=" + no);
		System.out.println("listo seccion");
		
		//	Updating Warehouse ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 i " 
			+ " SET M_Warehouse_ID = (SELECT M_Warehouse_ID FROM M_Warehouse s "
			+ " WHERE i.XX_TIENDA = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_TIENDA IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Warehouse ID=" + no);
		System.out.println("listo wh");*/
		
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  
			//java bug, it could not be used directly
		/*
		//	Reference Error
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 SET I_IsImported='E', "
							+ " I_ErrorMsg='ERR=No se encontro referencia,||" + ts 
							+ "' WHERE ((XX_VMR_Department_ID IS NULL) " 
							+ " OR (XX_VMR_Line_ID IS NULL) " 
							+ " OR (XX_VMR_Section_ID IS NULL) "
							+ " OR (M_Warehouse_ID IS NULL)) "
							+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
		
		
		commit();*/
		//-----------------------------------------------------------------------------------
			
			int noInsert = 0;

			//	Go through Records
			log.fine("start inserting...");
			sql = new StringBuffer ("SELECT * FROM I_XX_VMR_Prld03 "
				+ " WHERE I_IsImported='N'").append(clientCheck);
		
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					
					X_I_XX_VMR_Prld03 impIprld = new X_I_XX_VMR_Prld03 (getCtx(), rs, get_TrxName());
					log.fine("I_XX_VMR_PRLD03_ID=" + impIprld.getI_XX_VMR_PRLD03_ID());
					
					X_XX_VMR_Prld03 impXprld = null;
											
					if(impIprld.getXX_VMR_PRLD03_ID() == 0)
					{
			
						impXprld = new X_XX_VMR_Prld03(getCtx(), 0, get_TrxName());
					}	
					else  
					{
						impXprld = 	new X_XX_VMR_Prld03(getCtx(), impIprld.getXX_VMR_PRLD03_ID(), get_TrxName());
				
					}
											
					impXprld.setXX_YEARBUDGET(impIprld.getXX_AÑOPRE());
					
					impXprld.setXX_MONTHBUDGET(impIprld.getXX_MESPRE());
					
					impXprld.setXX_BUDGETDAY(impIprld.getXX_DIAPRE());
					
					impXprld.setM_Warehouse_ID(impIprld.getM_Warehouse_ID());
					
					impXprld.setXX_VMR_Department_ID(impIprld.getXX_VMR_Department_ID());
					
					impXprld.setXX_VMR_Line_ID(impIprld.getXX_VMR_Line_ID());
					
					impXprld.setXX_VMR_Section_ID(impIprld.getXX_VMR_Section_ID());
					
					impXprld.setXX_MONESTIR(impIprld.getXX_MONEST());
					
					impXprld.setXX_MONEJE(impIprld.getXX_MONEJE());
					
					impXprld.setXX_REGISTRATIONSTATUS(impIprld.getXX_Status());

				
					if(impXprld.save())
					{
						log.finest("Insert/Update Prld03 - " +  impXprld.getXX_VMR_PRLD03_ID());
						noInsert++;
						
						impIprld.setXX_VMR_PRLD03_ID(impXprld.getXX_VMR_PRLD03_ID());
						
						impIprld.setI_IsImported(true);
						impIprld.setProcessed(true);
						impIprld.setProcessing(false);
						impIprld.save();
					}
					else
					{		
						rollback();
						noInsert--;						
						sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 i "
								+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
								.append("Cannot Insert Prld03...")
								.append("' WHERE I_XX_VMR_Prld03_ID=").append(impIprld.getI_XX_VMR_PRLD03_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						//continue;							
					}

					commit();
					//	
					
				} // end while
				
				rs.close();
				pstmt.close();
				
			} // end try
			
			
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "", e);
				rollback();
			}
			
			//	Set Error to indicator to not imported
			
			sql = new StringBuffer ("UPDATE I_XX_VMR_Prld03 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@XX_VMR_Prld03_ID@: @Inserted@");

			return "";
	
	} // end doIt

} // end ImportVMRPrld03
