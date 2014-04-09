package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VMR_Prld01;
import compiere.model.cds.X_XX_VMR_Prld01;

public class ImportVMRPrld01 extends SvrProcess {

	/**	Client to be imported to*/
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean s_deleteOldImported = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	//
	
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
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		/*if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_VMR_Prld01 "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}*/
		
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  
		//java bug, it could not be used directly
		
		//	Set Client, Org, IsActive, Created/Updated
		/*sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 "
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
		log.fine("Reset=" + no);*/
		
		//NO DESCOMENTAR
		/*sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 i "
				+ "SET XX_VMR_Prld01_ID=(SELECT XX_VMR_Prld01_ID FROM XX_VMR_Prld01 "
				+ "WHERE i.XX_VMR_SECTION_ID=XX_VMR_SECTION_ID AND i.M_Warehouse_ID = M_Warehouse_ID " 
				+ "AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.XX_VMR_Prld01_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND i.XX_VMR_SECTION_ID IS NOT NULL AND I_IsImported='N'").append(clientCheck);		
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Found Prld01=" + no);*/
	/*

		//	Updating Category ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 i " 
			+ " SET XX_VMR_Category_ID = (SELECT XX_VMR_CATEGORY_ID FROM XX_VMR_CATEGORY c "
			+ " WHERE TO_NUMBER(i.XX_CodCat) = c.Value AND ROWNUM = 1) "
			+ " WHERE i.XX_CodCat IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Category ID=" + no);
		
		//	Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 i " 
			+ " SET XX_VMR_Department_ID = (SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT d "
			+ " WHERE TO_NUMBER(i.XX_CodDep) = d.Value AND ROWNUM = 1) "
			+ " WHERE i.XX_CodDep IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
		
		//	Updating Line ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 i " 
			+ " SET XX_VMR_Line_ID = (SELECT XX_VMR_Line_ID FROM XX_VMR_Line l "
			+ " WHERE TO_NUMBER(i.XX_CodLin) = l.Value" 
			+ " AND i.XX_VMR_Department_ID = l.XX_VMR_Department_ID AND ROWNUM = 1) "
			+ " WHERE i.XX_CodLin IS NOT NULL AND XX_VMR_Department_ID IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Line ID=" + no);
	
		//	Updating Section ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 i " 
			+ " SET XX_VMR_Section_ID = (SELECT XX_VMR_Section_ID FROM XX_VMR_Section s "
			+ " WHERE TO_NUMBER(i.XX_Codsec) = s.Value" 
			+ " AND i.XX_VMR_Line_ID = s.XX_VMR_Line_ID AND ROWNUM = 1) "
			+ " WHERE i.XX_Codsec IS NOT NULL AND XX_VMR_Line_ID IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Section ID=" + no);
	
		//	Updating Warehouse ID TRABAJAR CON XX_CODTIE
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 i " 
			+ " SET M_Warehouse_ID = (SELECT M_Warehouse_ID FROM M_Warehouse s "
			+ " WHERE TO_NUMBER(i.XX_CODTIE) = s.Value AND ROWNUM = 1) "
			+ " WHERE i.XX_CODTIE IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Warehouse ID=" + no);	
		
		
		//	Reference Error
		sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 SET "
							+ " I_ErrorMsg='ERR=No se encontro referencia,||" + ts 
							+ "' WHERE ((XX_VMR_Category_ID IS NULL)" 
							+ " OR (XX_VMR_Department_ID IS NULL) " 
							+ " OR (XX_VMR_Line_ID IS NULL) " 
							+ " OR (XX_VMR_Section_ID IS NULL) "
							+ " OR (M_Warehouse_ID IS NULL)) "
							+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);		
		
		commit();*/
		//-----------------------------------------------------------------------------------
		//delete I_XX_VMR_PRLD01 where XX_CodCat = 3 and XX_CodDep = 20 and XX_CodLin = 35 and XX_Codsec = 6
		int noInsert = 0;

		//	Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer ("SELECT * FROM I_XX_VMR_Prld01 "
			+ " WHERE I_IsImported='N' AND I_ErrorMsg IS NULL").append(clientCheck);
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VMR_Prld01 impIprld = new X_I_XX_VMR_Prld01 (getCtx(), rs, get_TrxName());
				log.fine("I_XX_VMR_PRLD01_ID=" + impIprld.getI_XX_VMR_Prld01_ID());
				
				X_XX_VMR_Prld01 impXprld = null;
				
				// Pregunto por ID de la tabla compiere que es igual al de la tabla I //
								
					if(impIprld.getXX_VMR_PRLD01_ID() == 0)
					{			
						impXprld = new X_XX_VMR_Prld01(getCtx(), 0, get_TrxName());
						
					}	
					else  
					{
						impXprld = 	new X_XX_VMR_Prld01(getCtx(), impIprld.getXX_VMR_PRLD01_ID(), get_TrxName());
				
					}						

					impXprld.setXX_PECTSALEPROMINTERESTS(impIprld.getXX_PORCOBPRE());
					
					impXprld.setXX_PURCHQUANTPLACEDMONTHS(impIprld.getXX_CACOMCOMA());

					impXprld.setXX_BUDGETYEARMONTH(impIprld.getXX_AÑOMESPRE());
					
					impXprld.setXX_INVEFECBUDGETEDAMOUNT(impIprld.getXX_MOINVINPR());
											 						 
					impXprld.setXX_NACPURCHAMOUNTRECEIVED(impIprld.getXX_MOCOMNREC());
					
					impXprld.setXX_PURCHAMOUNTPLACEDCOSTIMP(impIprld.getXX_MOCOMICCO());
					
					impXprld.setXX_NUMREALFINALINV(impIprld.getXX_CAINVFIRE());
					
					impXprld.setXX_NUMTRANSFSENT(impIprld.getXX_CANTRAENV());
					
					impXprld.setXX_QUANTITYPURCHLIMIT(impIprld.getXX_CANLIMCOM());
					
					impXprld.setXX_NETMARGPERTROYALLIVESTOCK(impIprld.getXX_POMARNGRE());
					
					impXprld.setXX_PURCHAMOUNTPLADPASTMONTHS(impIprld.getXX_MOCOMCOMA());
					
					impXprld.setXX_FINALINVAMOUNTPROJD(impIprld.getXX_MOINVFIPY());
					
					impXprld.setXX_AMOUNTINIINVEREAL(impIprld.getXX_MOINVINRE());
					
					impXprld.setXX_PURCHAMOUNTREDPASTMONTHS(impIprld.getXX_MOCOMREMA());
					
					impXprld.setXX_INVAMOUNTFINALREAL(impIprld.getXX_MOINVFIRE());
					
					impXprld.setXX_ROTATIONBUD(impIprld.getXX_PORROTPRE());
					
					impXprld.setXX_QUANTPURCHNAC(impIprld.getXX_CACOMNREC());
					
					impXprld.setXX_PERTSALEFRINTERESTS(impIprld.getXX_POREBPRRE());
					
					impXprld.setXX_PURCHQUANTPLACEDMONTHS(impIprld.getXX_CACOMCOMA());
					
					impXprld.setXX_PURCHQUANTIMPDPLACED(impIprld.getXX_CACOMICOL());
					
					impXprld.setXX_NUMNACSHOPPINGPLACED(impIprld.getXX_CACOMNCOL());
					
					impXprld.setXX_TRANSFAMOUNTSENT(impIprld.getXX_MONTRAENV());
					
					impXprld.setXX_PURCHAMOUNTBUDGETED(impIprld.getXX_MONCOMPRE());
					
					impXprld.setXX_AMOUNTSALEFRBUD(impIprld.getXX_MOREBFRPR());
					
					impXprld.setXX_ACTAMOUNTSALEFR(impIprld.getXX_MOREBFRRE());
					
					impXprld.setXX_LISCKGROSSMARGPERTREAL(impIprld.getXX_POMARBGRE());
					
					impXprld.setXX_REALDECLINE(impIprld.getXX_MOMERMREA());
					
					impXprld.setXX_TRANSFAMOUNTRECEIVED(impIprld.getXX_MONTRAREC());
					
					impXprld.setXX_NUMPROJDFINALINV(impIprld.getXX_CAINVFIPY());
					
					impXprld.setXX_PROMSALEAMOUNTBUD(impIprld.getXX_MOREBPRPR());
					
					impXprld.setXX_FINALINVAMOUNTBUD(impIprld.getXX_CAINVFIPR());
					
					impXprld.setXX_NUMMONTHSREDSHOP(impIprld.getXX_CACOMREMA());
					
					impXprld.setXX_QUANTPURCHAMOUNTSREV(impIprld.getXX_CACOMIREC());
					
					impXprld.setXX_PERCENTACTFINALSALE(impIprld.getXX_POREBDFRE());
					
					impXprld.setXX_PORTSALEFRBUD(impIprld.getXX_POREBFRPR());
					
					impXprld.setXX_REALPERCCOVERAGE(impIprld.getXX_PORCOBREA());
				
					impXprld.setXX_PERCENTSQALEFINALBUD(impIprld.getXX_POREBDFPR());
					
					impXprld.setXX_SALESAMOUNTBUD(impIprld.getXX_CANVENPRE());
					
					impXprld.setXX_PERTSALEFRINTERESTS(impIprld.getXX_POREBFRRE());
					 
					impXprld.setXX_PECTSALEPROMBUD(impIprld.getXX_POREBPRPR());
					
					impXprld.setXX_FINALACTAMOUNTSALE(impIprld.getXX_MOREBDFRE());
					
					impXprld.setXX_INVAMOUNTORIGBUDGETED(impIprld.getXX_CAINVINPR());
					
					impXprld.setXX_VMR_Section_ID(impIprld.getXX_VMR_Section_ID());
					
					impXprld.setXX_NUMBTRANSFREV(impIprld.getXX_CANTRAREC());
					
					impXprld.setXX_NETMARGPERTCATTLEBUD(impIprld.getXX_POMARNGPR());
					
					impXprld.setXX_ACTAMOUNTSALEPROM(impIprld.getXX_MOREBPRRE());
					
					impXprld.setXX_AMOUNTACTUALSALE(impIprld.getXX_MONVENREA());
					
					impXprld.setXX_AMOUNTPLACEDNACCAMPRA(impIprld.getXX_MOCOMNCOL());
					
					impXprld.setXX_FINALINVAMOUNTBUD2(impIprld.getXX_MOINVFIPR());
					
					impXprld.setXX_AMOUNTSALECOST(impIprld.getXX_MONVENCOS());
					
					impXprld.setXX_AMOUNTPURCHASELIMIT(impIprld.getXX_MONLIMCOM());
					
					impXprld.setXX_MARGACCORDINGBUYREAL(impIprld.getXX_POMARSCRE());
					
					impXprld.setXX_BUDDDECLINE(impIprld.getXX_MOMERMPRE());
					
					impXprld.setXX_LISCKGROSSMARGPERCTBUD(impIprld.getXX_POMARBGPR());
					
					impXprld.setXX_INIAMOUNTINVECOST(impIprld.getXX_MOINVINCO());
					
					impXprld.setXX_PURCHAMOUNTREVIMPD(impIprld.getXX_MOCOMIREC());
					
					impXprld.setXX_AMOUNTPLACEDNACPURCHCOST(impIprld.getXX_MOCOMNCCO());
					
					impXprld.setXX_NUMINIINVEREAL(impIprld.getXX_CAINVINRE());
					
					impXprld.setXX_FINALSALEAMOUNTINTERESTS(impIprld.getXX_CAREBDFRE());
					
					impXprld.setXX_BUDAMOUNTFRSALE(impIprld.getXX_CAREBFRPR());
					
					impXprld.setXX_VMR_Category_ID(impIprld.getXX_VMR_Category_ID());
					
					impXprld.setXX_VMR_Line_ID(impIprld.getXX_VMR_Line_ID());
																	
					impXprld.setXX_PROMSALENUMBUD(impIprld.getXX_CAREBPRPR());
					
					impXprld.setXX_AMOUNTSALEFRINTERESTS(impIprld.getXX_CAREBFRRE());
					
					impXprld.setXX_VMR_Department_ID(impIprld.getXX_VMR_Department_ID());
											
					impXprld.setXX_PURCHAMOUNTPLACEDIMPD(impIprld.getXX_MOCOMICOL());
					 
					impXprld.setXX_QUANTBUDGETEDSHOPPING(impIprld.getXX_CANCOMPRE());
					
					impXprld.setXX_BYWINMARGPERTBUD(impIprld.getXX_POMARPGPR());
					
					impXprld.setXX_AMOUNTSALEPROMINTERESTS(impIprld.getXX_CAREBPRRE());
					
					impXprld.setXX_FINALBUDAMOUNTSALE(impIprld.getXX_CAREBDFPR());
					
					impXprld.setXX_ROTATIONREAL(impIprld.getXX_PORROTREA());
					
					impXprld.setXX_PERCNBUDCOVERAGE(impIprld.getXX_PORCOBPRE());
					
					impXprld.setXX_PERTWINGMARGREAL(impIprld.getXX_POMARPGRE());
					
					impXprld.setXX_MARGACCORDINGBUDPURCH(impIprld.getXX_POMARSCPR());
										
					impXprld.setM_Warehouse_ID(impIprld.getM_Warehouse_ID());
					
					impXprld.setXX_QUANTACTUALSALE(impIprld.getXX_CANVENREA());
					
					impXprld.setXX_FINALSALEAMOUNTBUD(impIprld.getXX_MOREBDFPR());
					
					impXprld.setXX_SALESAMOUNTBUD2(impIprld.getXX_MONVENPRE());
					
					//
					if(impXprld.save())
					{
						log.finest("Insert/Update Prld01 - " +  impXprld.getXX_VMR_PRLD01_ID());
						noInsert++;
										
						impIprld.setXX_VMR_PRLD01_ID(impXprld.getXX_VMR_PRLD01_ID());
						impIprld.setI_IsImported(true);
						impIprld.setProcessed(true);
						impIprld.setProcessing(false);
						impIprld.save();
					}
					else
					{		
						rollback();
						noInsert--;						
						sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 i "
								+ "SET I_IsImported='N', I_ErrorMsg=' " + ts + " || ")
								.append("Cannot Insert Prld01...")
								.append("' WHERE I_XX_VMR_Prld01_ID=").append(impIprld.getI_XX_VMR_Prld01_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						//continue;							
					}

					commit();
					//						
								
			}//end while
			
			rs.close();
			pstmt.close();
			
		} //end try
		
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "", e);
			rollback();
		}	
			
			//	Set Error to indicator to not imported			
			sql = new StringBuffer ("UPDATE I_XX_VMR_Prld01 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@XX_VMR_Prld01_ID@: @Inserted@");
			return "";
			
	} //end doIt

} // end ImportVMRPrld01
