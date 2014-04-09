package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_I_XX_C_Orcm20;

/**
 * Import Order Purchase from ORCM20
 * 
 * @author Patricia Ayuso
 */
public class ImportCOrderOrcm20 extends SvrProcess {

	/** Data to be imported to */
	private int s_AD_Client_ID = 0;
	private int s_C_PaymentTerm_ID = 0;
	private int s_M_PriceList_ID = 0;
	private int s_C_DocType_ID = 0;
	private String s_XX_OrderType = "";

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;

			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("C_PaymentTerm_ID"))
				s_C_PaymentTerm_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("M_PriceList_ID"))
				s_M_PriceList_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("C_DocType_ID"))
				s_C_DocType_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("XX_OrderType"))
				s_XX_OrderType = element.getInfo();			
			else if (name.equals("DeleteOldImported")) s_deleteOldImported =
			"Y".equals(element.getParameter()); else log.log(Level.SEVERE,
			"Unknown Parameter: " + name);
			
		}

	}

	/**
	 * Perform process.
	 * 
	 * @return Message
	 * @throws Exception
	 */
	@Override
	protected String doIt() throws Exception {

		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;
		
		// Delete Old Imported		
		if (s_deleteOldImported) { sql = new StringBuffer
		("DELETE FROM I_XX_C_ORCM20 " +
		" WHERE I_IsImported='Y'").append(clientCheck); no =
		DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Delete Old Imported =" + no); }
		
		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_C_ORCM20 "
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

		//	Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VMR_Department_ID = (SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT d "
			+ " WHERE i.XX_CodDep = d.Value and d.IsActive = 'Y') "
			+ " WHERE i.XX_CodDep IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
		
		//	Updating Dispatch Route ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VLO_DispatchRoute_ID = (SELECT XX_VLO_DispatchRoute_ID FROM XX_VLO_DispatchRoute l "
			+ " WHERE i.XX_CODVIA = l.Value and l.IsActive = 'Y') "
			+ " WHERE i.XX_CODVIA IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Dispatch Route ID=" + no);
		
		//	Updating Import ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VLO_Import_ID = (SELECT XX_VLO_Import_ID FROM XX_VLO_Import s "
			+ " WHERE i.XX_GUIAEM = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_GUIAEM IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Import ID=" + no);
		
		//	Updating Payment Rule ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VCN_PaymentRule_ID = (SELECT XX_VCN_PaymentRule_ID FROM XX_VCN_PaymentRule s "
			+ " WHERE i.XX_FORPAG = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_FORPAG IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Payment Rule ID=" + no);
		
		//	Updating Country ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET C_Country_ID = (SELECT C_Country_ID FROM C_Country d "
			+ " WHERE i.XX_PAIS = d.Value and d.IsActive = 'Y') "
			+ " WHERE i.XX_PAIS IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);
		
		//	Updating Conversion Rate ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET C_Conversion_Rate_ID = (SELECT C_Conversion_Rate_ID FROM C_Conversion_Rate l "
			+ " WHERE i.XX_FACCAM = l.Value and l.IsActive = 'Y') "
			+ " WHERE i.XX_FACCAM IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Conversion Rate ID=" + no);
		
		//	Updating Shipping Condition ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VLO_ShippingCondition_ID = (SELECT XX_VLO_ShippingCondition_ID " 
			+ " FROM XX_VLO_ShippingCondition s "
			+ " WHERE i.XX_CODCEM = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_CODCEM IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Shipping Condition ID=" + no);
		
		//	Updating Delivery Location ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VLO_DeliveryLocation_ID = (SELECT XX_VLO_DeliveryLocation_ID " 
			+ " FROM XX_VLO_DeliveryLocation s "
			+ " WHERE i.XX_CODUBE = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_CODUBE IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Delivery Location ID=" + no);

		//	Updating Campaing ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VMR_CAMPAIGN_ID = (SELECT XX_VMR_CAMPAIGN_ID FROM XX_VMR_CAMPAIGN l "
			+ " WHERE i.XX_CODTEMP = l.Value and l.IsActive = 'Y') "
			+ " WHERE i.XX_CODTEMP IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Campaing ID=" + no);
		
		//	Updating Currency ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET C_Currency_ID = (SELECT C_Currency_ID FROM C_Currency s "
			+ " WHERE i.XX_CODMON = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_CODMON IS NOT NULL "
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Currency ID=" + no);
		
		//	Updating Arrival Port ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
			+ " SET XX_VLO_ArrivalPort_ID = (SELECT XX_VLO_ArrivalPort_ID " 
			+ " FROM XX_VLO_ArrivalPort s "
			+ " WHERE i.XX_PTOLLE = s.Value and s.IsActive = 'Y') "
			+ " WHERE i.XX_PTOLLE IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Arrival Port ID=" + no);
		
		// Updating Employee ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 i " 
				+ " SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner bp "
				+ " WHERE i.XX_COEMPE = bp.Value and bp.IsActive = 'Y') "
				+ " WHERE i.XX_COEMPE IS NOT NULL"
				+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Employee ID=" + no);
		
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  
			//java bug, it could not be used directly
				
		//	Reference Error
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM20 SET I_IsImported='E', "
							+ " I_ErrorMsg='ERR=No se encontro referencia,'||" + ts 
							+ " WHERE ((C_BPARTNER_ID IS NULL) " 
							+ " OR (C_CURRENCY_ID IS NULL)) "
							+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
		

		commit();
		// -----------------------------------------------------------------------------------

		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_C_ORCM20 "
				+ " WHERE I_IsImported='N'").append(clientCheck);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				
				X_I_XX_C_Orcm20 impIorder = new X_I_XX_C_Orcm20(getCtx(), rs, get_TrxName());
				log.fine("I_XX_C_ORCM20_ID=" + impIorder.getI_XX_C_ORCM20_ID());

				MOrder impMorder = null;

				Integer docNo = rs.getInt("NUMORD");
				
				String SQL0 = "SELECT C_Order_ID AS id "
						+ " FROM C_Order WHERE DOCUMENTNO = " + docNo;

				PreparedStatement pstmt0 = DB.prepareStatement(SQL0, null);
				ResultSet rs0;
				try {
					rs0 = pstmt0.executeQuery();

					// Si existe el NUMORD en C_Order...
					if (rs0.next()) {
						Integer MOrder_ID = rs.getInt("id");
						impMorder = new MOrder(getCtx(), MOrder_ID, get_TrxName());
						
					}else {
						impMorder = new MOrder(getCtx(), 0, get_TrxName());
						
					}
					rs0.close();
					pstmt0.close();
					
					impMorder.setValue(String.valueOf(impIorder.getDocumentNo()));
					
					impMorder.setXX_EstimatedMargin(BigDecimal
							.valueOf(impIorder.getXX_MARGEN()));

					impMorder.setXX_DefinitiveFactor(BigDecimal
							.valueOf(impIorder.getXX_FACCOP()));

					impMorder.setXX_EstimatedFactor(BigDecimal
							.valueOf(impIorder.getXX_FACCOE()));
										
					//TODO: Modificar los gets cuando se agreguen los campos
					impMorder.setXX_VLO_DispatchRoute_ID(impIorder.getXX_CODVIA());
					
					impMorder.setXX_VMR_DEPARTMENT_ID(impIorder.getXX_CodDep());

					impMorder.setXX_VLO_BOARDINGGUIDE_ID(impIorder.getXX_GUIAEM());
					
					Integer ForPag = impIorder.getXX_FORPAG();
					//impMorder.setXX_VCN_PaymentRule_ID(ForPag.toString());
					
					impMorder.setC_Country_ID(impIorder.getXX_PAIS_ID());
										
					impMorder.setXX_ConversionRate_ID(impIorder.getXX_FACCAM());
					
					impMorder.setXX_VLO_ShippingCondition_ID(impIorder.getXX_CODCEM());
					
					impMorder.setXX_VLO_DeliveryLocation_ID(impIorder.getXX_CODUBE());
					
					impMorder.setXX_Season_ID(impIorder.getXX_CODTEMP());
					
					impMorder.setC_Currency_ID(impIorder.getXX_CODMON());
					
					impMorder.setXX_VLO_ArrivalPort_ID(impIorder.getXX_PTOLLE());
					
					impMorder.setC_BPartner_ID(impIorder.getXX_COEMPE());
					//
					
					impMorder.setXX_OrderStatus(impIorder.getXX_STAORD());

					

					impMorder.setTotalPVP(BigDecimal.valueOf(impIorder
							.getXX_TOTVEN()));

					impMorder.setXX_TotalPVPPlusTax(BigDecimal
							.valueOf(impIorder.getXX_TOTIVA()));

					impMorder.setXX_EstimatedDate(impIorder.getXX_FECENTEST());

					impMorder.setXX_ArrivalDate(impIorder.getXX_FECENT());

					impMorder.setXX_Discount1(BigDecimal.valueOf(impIorder
							.getXX_DESCU1()));

					impMorder.setXX_Discount2(BigDecimal.valueOf(impIorder
							.getXX_DESCU2()));

					impMorder.setXX_Discount3(BigDecimal.valueOf(impIorder
							.getXX_DESCU3()));

					impMorder.setXX_Discount4(BigDecimal.valueOf(impIorder
							.getXX_DESCU4()));

					impMorder.setXX_Brochure_ID(impIorder.getXX_CODFOLL());

					impMorder.setXX_DispatchDate(impIorder.getXX_FECDESP());
					
					impMorder.setXX_ConsignmentDate(impIorder.getXX_FECCON());

					impMorder.setTotalLines(BigDecimal.valueOf(impIorder
							.getXX_TOTCOS()));

					impMorder.setDocumentNo(String.valueOf(impIorder
							.getDocumentNo()));
					
					impMorder.setC_DocTypeTarget_ID();
					
					impMorder.setC_PaymentTerm_ID(s_C_PaymentTerm_ID);
					
					impMorder.setM_PriceList_ID(s_M_PriceList_ID);
					
					impMorder.setC_DocType_ID(s_C_DocType_ID);
					
					impMorder.setXX_OrderType(s_XX_OrderType);
					
					if (impMorder.save()) {
						System.out.println("datos salvados...");

						log.finest("Insert/Update Order - "
								+ impMorder.getC_Order_ID());
						noInsert++;

						impIorder.setI_IsImported("'Y'");
						impIorder.setProcessed(true);
						impIorder.setProcessing(false);
						impIorder.save();

					} else {
						rollback();
						noInsert--;
						sql = new StringBuffer("UPDATE I_XX_C_ORCM20 i "
								+ "SET I_IsImported='E', I_ErrorMsg=" + ts
								+ "|| '").append("Cannot Insert Order...")
								.append("' WHERE I_XX_C_ORCM20_ID=").append(
										impIorder.getI_XX_C_ORCM20_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						// continue;
					}

					commit();
						
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}

			} // end while

			rs.close();
			pstmt.close();

		} // end try

		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}

		// Set Error to indicator to not imported
		sql = new StringBuffer("UPDATE I_XX_C_ORCM20 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), /* OJO */
				"@C_Order_ID@: @Inserted@");

		System.out.println(sql);
		return "";

	}
	
}
