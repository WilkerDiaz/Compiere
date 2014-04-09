package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MVCNApplicationNumber;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_I_XX_VCN_BookPurchases;
import compiere.model.cds.X_XX_VCN_PurchasesBook;

public class ImportVCVEstd01 extends SvrProcess {

	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;
	Utilities util = new Utilities();
	
		
	@Override
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
		
	} // prepare

	
	@Override
	protected String doIt() throws Exception {
		
		String store = null;
		String fecha = null;
		String year = null;
		String mes = null;
		String dia = null;
		
		Integer yearI = 0;
		Integer mesI = 0;
		Integer diaI = 0;
		Integer numApp = 0;
		//String store1 = null;
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		// **** Prepare **** //

		// Delete Old Imported
		if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_VCN_BookPurchases "
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases "
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
		

		String SQL2 = ("SELECT I_XX_VCN_BookPurchases_ID AS BPID, XX_FECDOC AS FECDOC, XX_NUMPLA AS NUMAPP ,XX_DAY AS DIA, XX_MONTH AS MES, XX_YEAR AS ANO FROM I_XX_VCN_BookPurchases ");

		try
		{
			PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
		    ResultSet rs2 = pstmt2.executeQuery();
		    
		    while(rs2.next())
		    {
		    	
		    	String SQL1 = ("SELECT XX_COTIEN AS TIENDA " +
						"FROM I_XX_VCN_BookPurchases " +
						"WHERE I_XX_VCN_BookPurchases_ID = '"+rs2.getInt("BPID")+"' ");
		    	
						
				PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
			    ResultSet rs1 = pstmt1.executeQuery();
			    
			    fecha = rs2.getString("FECDOC");
			    yearI = rs2.getInt("ANO");
			    mesI = rs2.getInt("MES");
			    diaI = rs2.getInt("DIA");
			    numApp = rs2.getInt("NUMAPP");
			    
			    X_I_XX_VCN_BookPurchases bookPurc = new X_I_XX_VCN_BookPurchases (getCtx(), rs2.getInt("BPID"), get_TrxName());
			    
			    if(fecha.length() == 8)
			    {
			    	//X_I_XX_VCN_BookPurchases bookPurc = new X_I_XX_VCN_BookPurchases (getCtx(), rs2.getInt("BPID"), get_TrxName());
			    	
				    year = fecha.substring(0, 4);
				    mes = fecha.substring(4, 6);
				    dia = fecha.substring(6, 8);
					
				    Calendar now = Calendar.getInstance();
	                now.set(new Integer(year) , new Integer(mes) -1, new Integer(dia));
	                
	                /*Calendar nowI = Calendar.getInstance();
	                nowI.set(yearI , mesI -1, diaI);*/
	                	
	                bookPurc.setXX_DOCDATE(new Timestamp(now.getTimeInMillis()));
	               // bookPurc.setXX_DATE(new Timestamp(nowI.getTimeInMillis()));
	                bookPurc.save();
	   
			    }// end if
			    if(yearI != 0 && mesI != 0 && diaI !=0)		   
			    {
			    	//X_I_XX_VCN_BookPurchases bookPurc = new X_I_XX_VCN_BookPurchases (getCtx(), rs2.getInt("BPID"), get_TrxName());
			    	
			    	Calendar nowI = Calendar.getInstance();
	                nowI.set(yearI , mesI -1, diaI);
			    	
	                bookPurc.setXX_DATE(new Timestamp(nowI.getTimeInMillis()));
	                
	                MVCNApplicationNumber app = new MVCNApplicationNumber (getCtx(), 0,get_TrxName());
	               
	                Integer appid = 0;
	                
	                if(numApp != 0)        
	                	appid = app.generateApplicationNumber(new Timestamp(nowI.getTimeInMillis()), numApp);
	                
	                if(appid != 0)
	                	bookPurc.setXX_VCN_PurchasesBook_ID(appid);
	                
	                bookPurc.save();
	   
			    }
			    
				if(rs1.next())
				{
					store = rs1.getString("TIENDA");
					//System.out.println(store);
					if(store.length()==1)
					{
						store ="00"+store;
						//System.out.println(store);
					}
					else if(store.length()==2)
					{
						store ="0"+store;
						//System.out.println(store);
					}
				}
				rs1.close();
				pstmt1.close();
				
				//Warehouse	
				sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
						+ "SET i.M_WAREHOUSE_ID=(SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE "
						+ "WHERE '"+store+"'=Value AND AD_CLIENT_ID=i.AD_CLIENT_ID AND ISACTIVE='Y') "
						+ "WHERE i.AD_CLIENT_ID IS NOT NULL " 
						+ "AND I_IsImported='N' AND i.XX_COTIEN IS NOT NULL AND I_XX_VCN_BookPurchases_ID = '"+rs2.getInt("BPID")+"' ");

				no = DB.executeUpdate(get_Trx(), sql.toString());
				log.fine("Set Warehouse ID=" + no);
				/*
				String ts1 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
						: "I_ErrorMsg"; // java bug, it could not be used directly
				*/

				sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
						+ "SET i.C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner "
						+ "WHERE i.XX_COPROV = VALUE AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
						+ "WHERE i.C_BPartner_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
						+ "AND I_IsImported='N' AND i.XX_COPROV IS NOT NULL AND I_XX_VCN_BookPurchases_ID = '"+rs2.getInt("BPID")+"' ");
						
				no = DB.executeUpdate(get_Trx(), sql.toString());
				log.fine("Set BPartner ID=" + no);
			
			}// end while
		    rs2.close();
			pstmt2.close();	
		    commit();
		    
		}// end try
		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
		}

		//BPartner		
		/*sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
				+ "SET i.C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner "
				+ "WHERE i.XX_COPROV = VALUE AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.C_BPartner_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_COPROV IS NOT NULL");
				
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set BPartner ID=" + no);
	
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly*/
		
		//User
		sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
				+ "SET i.AD_User_ID=(SELECT AD_User_ID FROM AD_User "
				+ "WHERE i.XX_USRELI = VALUE AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.AD_User_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_USRELI IS NOT NULL");
				
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set User ID=" + no);
		
		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		
		//Invoice (Factura)
		sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
				+ "SET i.C_Invoice_ID =(SELECT C_Invoice_ID FROM C_Invoice "
				+ "WHERE i.XX_COFACT = DocumentNo AND i.C_BPartner_ID = C_BPartner_ID AND AD_CLIENT_ID = i.AD_CLIENT_ID) "
				+ "WHERE i.C_Invoice_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_COFACT IS NOT NULL ");
				
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Invoice ID=" + no);
		/*
		String ts3 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		*/
		
		//Tax
		sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
				+ "SET i.C_Tax_ID =(SELECT C_Tax_ID FROM C_Tax "
				+ "WHERE i.XX_TASAS = Rate AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.C_Tax_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_TASAS IS NOT NULL");
				
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Tax ID=" + no);
		/*
		String ts4 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		*/
		
		//Invoice (Nota de Deb o Cre) 
		sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
				+ "SET i.XX_DocumentNo_ID =(SELECT C_Invoice_ID FROM C_Invoice "
				+ "WHERE i.XX_DEBCRE = DocumentNo AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.C_Invoice_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND i.XX_DEBCRE IS NOT NULL");
				
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Invoice ID=" + no);
		/*
		String ts5 = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		*/
		
		
		commit();
	//-----------------------------------------------------------------------------------------------------------------------------
		
		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_VCN_BookPurchases "
				+ " WHERE I_IsImported='N'");

		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(),
					get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{		
				X_I_XX_VCN_BookPurchases bookIpurc = new X_I_XX_VCN_BookPurchases (getCtx(), rs, get_TrxName());
				log.fine("I_XX_VCN_BookPurchases=" + bookIpurc.getI_XX_VCN_BOOKPURCHASES_ID());
				
				// *** Create/Update **** //
				X_XX_VCN_PurchasesBook bookXpurc = null;
				
				
				// Pregunto por ID de la tabla compiere que es igual al de la
				// tabla I //
				if(bookIpurc.getXX_VCN_PurchasesBook_ID() == 0)
				{
					bookXpurc = new X_XX_VCN_PurchasesBook(getCtx(), 0, get_TrxName()); 
				}
				else
				{
					bookXpurc = new X_XX_VCN_PurchasesBook(getCtx(), bookIpurc.getXX_VCN_PurchasesBook_ID(), get_TrxName());
				}
				
				bookXpurc.setM_Warehouse_ID(bookIpurc.getM_Warehouse_ID());
				bookXpurc.setC_BPartner_ID(bookIpurc.getC_BPartner_ID());
				bookXpurc.setXX_ControlNumber(bookIpurc.getXX_NUMCTL());
				bookXpurc.setXX_DATE(bookIpurc.getXX_DATE());
				bookXpurc.setC_Invoice_ID(bookIpurc.getC_Invoice_ID());
				bookXpurc.setXX_DocumentDate(bookIpurc.getXX_DOCDATE());
				bookXpurc.setXX_DocumentNo_ID(bookIpurc.getXX_DocumentNo_ID());
			
				if(bookIpurc.getXX_COCOST() == null)
				{
					bookXpurc.setXX_TotalInvCost(new BigDecimal(0));
				}
				else
				{
					bookXpurc.setXX_TotalInvCost(new BigDecimal(bookIpurc.getXX_COCOST()));
				}
				
				if(bookIpurc.getXX_COIMPU() == null)
				{
					bookXpurc.setXX_TaxAmount(new BigDecimal(0));
				}
				else
				{
					bookXpurc.setXX_TaxAmount(new BigDecimal(bookIpurc.getXX_COIMPU()));
				}
				
				if(bookIpurc.getXX_COBASE() == null)
				{
					bookXpurc.setXX_TaxableBase(new BigDecimal(0));
				}
				else
				{
					bookXpurc.setXX_TaxableBase(new BigDecimal(bookIpurc.getXX_COBASE()));
				}
				
				if(bookIpurc.getXX_COBASEX() == null)
				{
					bookXpurc.setXX_ExemptBase(new BigDecimal(0));
				}
				else
				{
					bookXpurc.setXX_ExemptBase(new BigDecimal(bookIpurc.getXX_COBASEX()));
				}
				
				if(bookIpurc.getXX_COBASENS() == null)
				{
					bookXpurc.setXX_NotSubjectBase(new BigDecimal(0));
				}
				else
				{
					bookXpurc.setXX_NotSubjectBase(new BigDecimal(bookIpurc.getXX_COBASENS()));
				}	
				
				if(bookIpurc.getXX_NROCOM() == null)
				{
					bookXpurc.setXX_Withholding(new Integer(0));
				}
				else
				{
					bookXpurc.setXX_Withholding(new Integer(bookIpurc.getXX_NROCOM()));
				}
				
				if (util.clientRetentionAgent(getAD_Client_ID())){
					if(bookIpurc.getXX_MONRET() == null)
					{
						bookXpurc.setXX_WithholdingTax(new BigDecimal(0));
					}
					else
					{
						bookXpurc.setXX_WithholdingTax(new BigDecimal(bookIpurc.getXX_MONRET()));
					}
				}else
					bookXpurc.setXX_WithholdingTax(new BigDecimal(0));
			
				if(bookIpurc.getXX_NUMEXP()==null)
				{
					bookXpurc.setXX_ExpedientNumber(new Integer(0));
				}
				else
				{
					bookXpurc.setXX_ExpedientNumber(new Integer(bookIpurc.getXX_NUMEXP()));
				}
						
				bookXpurc.setC_Tax_ID(bookIpurc.getC_Tax_ID());
				/*try {
					bookXpurc.setXX_DocumentNo_ID(Integer.valueOf(bookIpurc.getXX_DEBCRE()));
				} catch (Exception e) {
					bookXpurc.setXX_DocumentNo_ID(0);
				}*/
				bookXpurc.setXX_VCN_ApplicationNumber_ID(bookIpurc.getXX_VCN_PurchasesBook_ID());
			
				// origen
				if(bookIpurc.getXX_COORIG().equalsIgnoreCase("A"))
				{
					bookXpurc.setXX_isManual(false);
				}
				else if(bookIpurc.getXX_COORIG().equalsIgnoreCase("M"))
				{
					bookXpurc.setXX_isManual(true);
				}
				
				
				if(bookXpurc.save())
				{
					log.finest("Insert/Update SalesPurchase - "
							+ bookXpurc.getXX_VCN_PurchasesBook_ID());
					
					bookIpurc.setXX_VCN_PurchasesBook_ID(bookXpurc.getXX_VCN_PurchasesBook_ID());
					
					noInsert++;
					
					bookIpurc.setI_IsImported(true);
					bookIpurc.setProcessed(true);
					//impIsalpurc.setProcessing(false);
					bookIpurc.save();
				}
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert SALEPURCHASE...").append(
									"' WHERE I_XX_VCN_BookPurchases=").append(
											bookIpurc.getI_XX_VCN_BOOKPURCHASES_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
					// continue;
				}
				
				commit();
				
			}// end while
			rs.close();
			pstmt.close();
			
		}// end try
		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}
		
		// Set Error to indicator to not imported

		sql = new StringBuffer("UPDATE I_XX_VCN_BookPurchases "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@XX_VCN_PurchasesBook_ID@: @Inserted@");
		return "";
	}

}
