package compiere.model.cds.imports;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

//import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MBPartnerLocation;
import compiere.model.cds.MLocation;
import compiere.model.cds.MUser;
import compiere.model.cds.X_I_BPartner;

public class ImportBPartner extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				p_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			p_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		p_DateValue = null;

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
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (p_DateValue == null)
			p_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		//Trx trx = Trx.get("auxiliar", true);

		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + p_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (p_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_BPartner "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Impored =" + no);
		}

		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(p_AD_Client_ID).append("),"
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
		//System.out.println(sql);
		log.fine("Reset=" + no);

		//	Set BP_Group
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET GroupValue=(SELECT MAX(Value) FROM C_BP_Group g WHERE g.IsDefault='Y'"
			+ " AND g.AD_Client_ID=i.AD_Client_ID) ");
		sql.append(" WHERE GroupValue IS NULL AND C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Group Default=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_BP_Group_ID=(SELECT C_BP_Group_ID FROM C_BP_Group g"
			+ " WHERE i.GroupValue=g.Value AND g.AD_Client_ID=i.AD_Client_ID) "
			+ " WHERE C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Group=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Group, ' "
			+ " WHERE C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.config("Invalid Group=" + no);

		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c"
			+ " WHERE i.CountryCode=c.CountryCode AND c.IsSummary='N' AND c.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ " WHERE C_Country_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Country=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Country, ' "
			+ " WHERE C_Country_ID IS NULL AND (City IS NOT NULL OR Address1 IS NOT NULL)"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.config("Invalid Country=" + no);

		//	Set Region
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "Set RegionName=(SELECT Name FROM C_Region r"
			+ " WHERE r.IsDefault='Y' AND r.C_Country_ID=i.C_Country_ID"
			+ " AND r.AD_Client_ID IN (0, i.AD_Client_ID)) " );
		sql.append(" WHERE RegionName IS NULL AND C_Region_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Region Default=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r"
			+ " WHERE r.Name=i.RegionName AND r.C_Country_ID=i.C_Country_ID"
			+ " AND r.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ " WHERE C_Region_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Region=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Region, ' "
			+ " WHERE C_Region_ID IS NULL "
			+ " AND EXISTS (SELECT * FROM C_Country c"
			+ " WHERE c.C_Country_ID=i.C_Country_ID AND c.HasRegion='Y')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.config("Invalid Region=" + no);

		//	Set Greeting
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_Greeting_ID=(SELECT C_Greeting_ID FROM C_Greeting g"
			+ " WHERE i.BPContactGreeting=g.Name AND g.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ " WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Greeting=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Greeting, ' "
			+ " WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.config("Invalid Greeting=" + no);

		//	Existing User ?
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET (C_BPartner_ID,AD_User_ID)="
				+ "(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u "
				+ " WHERE i.EMail=u.EMail AND u.AD_Client_ID=i.AD_Client_ID) "
			+ " WHERE i.EMail IS NOT NULL AND I_IsImported='N'").append(clientCheck);

		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Found EMail User=" + no);

		//	Existing BPartner ? Match Value
		/*sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p"
			+ " WHERE i.Value=p.Value AND p.AD_Client_ID=i.AD_Client_ID) "
			+ " WHERE C_BPartner_ID IS NULL AND Value IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Found BPartner=" + no);*/

		//	Existing Contact ? Match Name
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET AD_User_ID=(SELECT AD_User_ID FROM AD_User c"
			+ " WHERE i.ContactName=c.Name AND i.C_BPartner_ID=c.C_BPartner_ID AND c.AD_Client_ID=i.AD_Client_ID) "
			+ " WHERE C_BPartner_ID IS NOT NULL AND AD_User_ID IS NULL AND ContactName IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Found Contact=" + no);

		//	Existing Location ? Exact Match
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_BPartner_Location_ID=(SELECT C_BPartner_Location_ID"
			+ " FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)"
			+ " WHERE i.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=i.AD_Client_ID"
			+ " AND COALESCE(i.Address1,N' ')=COALESCE(l.Address1,N' ') "
			+ " AND COALESCE(i.Address2,N' ')=COALESCE(l.Address2,N' ')"
			+ " AND COALESCE(i.Address3,N' ')=COALESCE(l.Address3,N' ') "
			+ " AND COALESCE(i.Address4,N' ')=COALESCE(l.Address4,N' ')"
			+ " AND COALESCE((select name from C_City where C_City_ID = i.City),N' ')=COALESCE(l.City,N' ') "
			+ " AND COALESCE(i.Postal,N' ')=COALESCE(l.Postal,N' ') "
			+ " AND COALESCE(i.Postal_Add,N' ')=COALESCE(l.Postal_Add,N' ')"
			+ " AND COALESCE(i.C_Region_ID,0)=COALESCE(l.C_Region_ID,0)"
			+ " AND COALESCE(i.C_Country_ID,0)=COALESCE(l.C_Country_ID,0)) "
			+ " WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Found Location=" + no);

		//	Interest Area
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET R_InterestArea_ID=(SELECT R_InterestArea_ID FROM R_InterestArea ia "
			+ " WHERE i.InterestAreaName=ia.Name AND ia.AD_Client_ID=i.AD_Client_ID) "
			+ " WHERE R_InterestArea_ID IS NULL AND InterestAreaName IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Interest Area=" + no);

		
		/*
		 * Agregado Por Jorge E. Pires G.
		 * */
		//	Economic Activities Value al EconomicActivities ID
/**		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.XX_EconomicActivities_ID=(SELECT ea.XX_VCN_ECONOACTIVI_ID FROM XX_VCN_EconoActivi ea "
			+ " WHERE i.XX_EconomicActivities_Value = ea.Value and ea.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.XX_EconomicActivities_ID IS NULL AND i.XX_EconomicActivities_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		//System.out.println(sql);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Economic Activities =" + no);
		
		//	Cause Retention Value al Cause Retention ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.XX_CauseRetention_ID=(SELECT cr.XX_VCN_CAUSERETENTION_ID FROM XX_VCN_CAUSERETENTION cr "
			+ " WHERE i.XX_CauseRetention_Value = cr.Value and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.XX_CauseRetention_ID IS NULL AND i.XX_CauseRetention_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Cause Retention =" + no);
		
		//	City Value al City ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.City_ID=(SELECT c.C_City_ID FROM C_City c "
			+ " WHERE i.XX_City_Value = c.Value and c.IsActive = 'Y' and c.C_COUNTRY_ID = (select p.C_COUNTRY_ID from C_COUNTRY p where p.value = i.XX_Country_Value))"
			+ " WHERE i.City_ID IS NULL AND i.XX_City_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set City =" + no);
		
		//	Country Value al Country ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.C_Country_ID=(SELECT c.C_Country_ID FROM C_Country c "
			+ " WHERE i.XX_Country_Value = c.Value and c.IsActive = 'Y') "
			+ " WHERE i.C_Country_ID IS NULL AND i.XX_Country_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Country =" + no);
		
		//	Type TaxPayer Value al Type TaxPayer ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.XX_TypeTaxPayer_ID=(SELECT cr.XX_VCN_TYPETAXPAYER_ID FROM XX_VCN_TYPETAXPAYER cr "
			+ " WHERE i.XX_TypeTaxPayer_Value = cr.Value and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.XX_TypeTaxPayer_ID IS NULL AND i.XX_TypeTaxPayer_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Type TaxPayer =" + no);	
		
		//	XX_VendorType Value al XX_VendorType_ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.XX_VendorType_ID=(SELECT cr.XX_VCN_VendorType_ID FROM XX_VCN_VendorType cr "
			+ " WHERE i.XX_VendorType_Value = cr.Value and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.XX_VendorType_ID IS NULL AND i.XX_VendorType_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Vendor Type =" + no);
		
		//	XX_ProductClass Value al XX_ProductClass_ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.XX_ProductClass_ID =(SELECT cr.XX_VMR_ProductClass_ID FROM XX_VMR_ProductClass cr "
			+ " WHERE i.XX_ProductClass_Value = cr.Value and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.XX_ProductClass_ID IS NULL AND i.XX_ProductClass_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Product Classification =" + no);
		
		//	PO Payment Term Value al PO_PaymentTerm_ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.PO_PaymentTerm_ID =(SELECT cr.C_PaymentTerm_ID FROM C_PaymentTerm cr "
			+ " WHERE i.PO_PaymentTerm_Value = cr.Value and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.PO_PaymentTerm_ID IS NULL AND i.PO_PaymentTerm_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Payment Term =" + no);
		/*
		// Payment Rule Value
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.PAYMENTRULEPO =(SELECT cr.XX_VCN_PAYMENTRULE_ID FROM XX_VCN_PAYMENTRULE cr "
			+ " WHERE i.XX_PAYMENTRULE = cr.XX_CODEPAYMENTRULE and cr.IsActive = 'Y')"
			+ " WHERE i.XX_PAYMENTRULE IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Payment Rule =" + no);
		
		//	Percentaje Retention Value al Percentaje Retention ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.XX_PercentajeRetention_ID =(SELECT cr.XX_VCN_PercenReten_ID FROM XX_VCN_PercenReten cr "
			+ " WHERE i.XX_PercentajeRetention_Value = cr.XX_PERCENRETEN and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.XX_PercentajeRetention_ID IS NULL AND i.XX_PercentajeRetention_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Percentaje Retention =" + no);
		
		//	Type Tax Value al Type Tax ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.XX_TypeTax_ID =(SELECT cr.XX_VMR_TypeTax_ID FROM XX_VMR_TypeTax cr "
			+ " WHERE i.XX_TypeTax_Value = cr.Value and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.XX_TypeTax_ID IS NULL AND i.XX_TypeTax_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Type Tax =" + no);
		
		//	Bank Value al Bank ID
		sql = new StringBuffer ("UPDATE I_BPartner i " 
			+ " SET i.C_Bank_ID =(SELECT cr.C_Bank_ID FROM C_Bank cr "
			+ " WHERE i.C_Bank_Value = cr.Value and cr.IsActive = 'Y'");
			sql.append(clientCheck);
			sql.append(") "
			+ " WHERE i.C_Bank_ID IS NULL AND i.C_Bank_Value IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//System.out.println(sql);
		log.fine("Set Bank =" + no);
*/
		commit();
		
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;

		//	Go through Records
		/*sql = new StringBuffer ("SELECT * FROM I_BPartner "
			+ " WHERE I_IsImported='N'").append(clientCheck);*/
		
		sql = new StringBuffer ("SELECT * FROM I_BPartner "
				+ " WHERE I_IsImported='N'");
			
		
		System.out.println(sql);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_I_BPartner impBP = new X_I_BPartner (getCtx(), rs, get_TrxName());
				log.fine("I_BPartner_ID=" + impBP.getI_BPartner_ID()
					+ ", C_BPartner_ID=" + impBP.getC_BPartner_ID()
					+ ", C_BPartner_Location_ID=" + impBP.getC_BPartner_Location_ID()
					+ ", AD_User_ID=" + impBP.getAD_User_ID());


				
				//	****	Create/Update BPartner	****
				MBPartner bp;
				Boolean nuevo;
				
				if(impBP.getC_BPartner_ID() == new Integer(0)){
					bp = new MBPartner(getCtx(), 0, get_TrxName());
					nuevo = new Boolean(true);
				}	
				else{
					bp = new MBPartner(getCtx(), impBP.getC_BPartner_ID(), get_TrxName());
					nuevo = new Boolean(false);
				}
				
				
				/*
				 * Cambiando los campos de la cabecera de BPartner
				 */
				if (impBP.getName() != null){
					bp.setName(impBP.getName());
					bp.setName2(impBP.getName2());
				}
				if (impBP.getValue() != null)
					bp.setValue(impBP.getValue());
				if (impBP.getDescription() != null)
					bp.setDescription(impBP.getDescription());
				if (impBP.getXX_TypePerson() != null)
					bp.setXX_TypePerson(impBP.getXX_TypePerson());
				if (impBP.getXX_CI_RIF() != null)
					bp.setXX_CI_RIF(impBP.getXX_CI_RIF());
				if (impBP.getXX_NIT() != null)
					bp.setXX_NIT(impBP.getXX_NIT());
				if (impBP.getXX_RegistrationData() != null)
					bp.setXX_RegistrationData(impBP.getXX_RegistrationData());
				if (impBP.getXX_EntryDate() != null)
					bp.setXX_EntryDate(impBP.getXX_EntryDate());
				if (impBP.getXX_ExitDate() != null)
					bp.setXX_ExitDate(impBP.getXX_ExitDate());
				
				if (impBP.getC_BP_Group_ID() != 0)
					bp.setC_BP_Group_ID(impBP.getC_BP_Group_ID());
				
				/*
				 * Cambios de la Pestana de Vendor 
				 */
				if (impBP.getIsVendor() != null)
					bp.setIsVendor(impBP.getIsVendor());
				if (impBP.getXX_VendorEmail() != null)
					bp.setXX_VendorEmail(impBP.getXX_VendorEmail());
				if (impBP.getXX_VendorCategory() != null)
					bp.setXX_VendorCategory(impBP.getXX_VendorCategory());
				if (impBP.getXX_FigureVendor() != null)
					bp.setXX_FigureVendor(impBP.getXX_FigureVendor());
				if (impBP.getXX_VendorType_ID() != null)
					bp.setXX_VendorType_ID(impBP.getXX_VendorType_ID());
				if (impBP.getXX_ProductClass_ID() != null)
					bp.setXX_ProductClass_ID(impBP.getXX_ProductClass_ID());
				if (impBP.getXX_EconomicActivities_ID() != null)
					bp.setXX_EconomicActivities_ID(impBP.getXX_EconomicActivities_ID());
				if (impBP.get_PaymentRulePO() != null)
					bp.setPaymentRulePO(impBP.get_PaymentRulePO());
				if (impBP.get_PO_PaymentTerm_ID() != null)
					bp.setPO_PaymentTerm_ID(new Integer (impBP.get_PO_PaymentTerm_ID()));	
				if (impBP.getXX_TypeTaxPayer_ID() != null)
					bp.setXX_TypeTaxPayer_ID(impBP.getXX_TypeTaxPayer_ID());
				if (impBP.getXX_PercentajeRetention_ID() != null)
					bp.setXX_PercentajeRetention_ID(impBP.getXX_PercentajeRetention_ID());
				if (impBP.getXX_TypeTax_ID() != null)
					bp.setXX_TypeTax_ID(impBP.getXX_TypeTax_ID());
				if (impBP.getXX_RetentionIndicator() != null)
					bp.setXX_RetentionIndicator(impBP.getXX_RetentionIndicator());
				if (impBP.getXX_CauseRetention_ID() != null)
					bp.setXX_CauseRetention_ID(impBP.getXX_CauseRetention_ID());

				
				// Agregado por dpellegrino
				// *** create/update BPartner Costumer	****
				
				if(impBP.getXX_IsCostumer() == true)
				{
					bp.setIsCustomer(impBP.getXX_IsCostumer());
					if(impBP.getM_Warehouse_ID() != 0)
						bp.setM_Warehouse_ID(impBP.getM_Warehouse_ID());
					if(impBP.getXX_FechaNac() != null)
						bp.setXX_FechaNac(impBP.getXX_FechaNac());
					if(impBP.getXX_EstadoCivil() != null)
						bp.setXX_EstadoCivil(impBP.getXX_EstadoCivil());
					if(impBP.getXX_Registrado() != null)
						bp.setXX_Registrado(impBP.getXX_Registrado());
					if(impBP.getXX_Genero() != null)
						bp.setXX_Genero(impBP.getXX_Genero());
					if(impBP.getXX_Email() != null)
						bp.setXX_Email(impBP.getXX_Email());
					if(impBP.getXX_TypeTax_ID() != 0)
						bp.setXX_TypeTax_ID(impBP.getXX_TypeTax_ID());
					if(impBP.getXX_Iscontac() != null)
						bp.setXX_Iscontac(impBP.getXX_Iscontac());
				}
				else
					bp.setIsCustomer(impBP.getXX_IsCostumer());
				
				
				// Agregado por dpellegrino
				// *** create/update BPartner Employee	****
				
				if(impBP.getXX_IsEmployee() == true)
				{
					bp.setIsEmployee(impBP.getXX_IsEmployee());
					if(impBP.getC_Job_ID() != 0)
						bp.setC_Job_ID(impBP.getC_Job_ID());
					if(impBP.getXX_NumberTap() != null)
						bp.setXX_NumberTap(impBP.getXX_NumberTap());
					bp.setXX_CollaboratorClass("FI");
						
				}
				else
					bp.setIsEmployee(impBP.getXX_IsEmployee());
				
				//nacional o internacional
				if (impBP.getXX_VendorClass() != null)
					bp.setXX_VendorClass(new String(impBP.getXX_VendorClass()));
				
				if (bp.save())
				{					
					impBP.setI_IsImported("Y");
					impBP.setProcessed(true);
					impBP.setProcessing(false);
					impBP.setC_BPartner_ID(bp.getC_BPartner_ID());
					
					impBP.save();
										
					log.finest("Insert/Update BPartner - " + bp.getC_BPartner_ID());
					if (nuevo.booleanValue()) {
						noInsert++;						
					}else{
						noUpdate++;
					}
				}
				else
				{
					sql = new StringBuffer ("UPDATE I_BPartner i "
						+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
						.append("Cannot Insert BPartner")
						.append("' WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
					continue;
				}
				
				
				
				
		
							
				//	****	Create/Update BPartner Location	****
				MBPartnerLocation bpl;
				if(impBP.getC_BPartner_Location_ID() == new Integer(0))
				{
					bpl = new MBPartnerLocation(getCtx(), 0, get_TrxName());
					nuevo = new Boolean(true);
				}	
				else  
				{
					bpl = new MBPartnerLocation(getCtx(), impBP.getC_BPartner_Location_ID(), get_TrxName());
					nuevo = new Boolean(false);
				}
				//System.out.println("Contexto " + getCtx() +" Pais "+ impBP.getC_Country_ID() + " Ciudad " +
					//	 impBP.getCity_ID() + " Trx " + get_TrxName());
				
				MLocation location = new MLocation(getCtx(), impBP.getC_Country_ID()
						, impBP.getCity_ID(), get_TrxName());
				location.setAddress1(impBP.getAddress1());
				location.setAddress2(impBP.getAddress2());
				location.setAddress3(impBP.getAddress3());
				location.setAddress4(impBP.getAddress4());

				if (location.save())
					log.finest("Insert Location - " + location.getC_Location_ID());
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer ("UPDATE I_BPartner i "
						+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
						.append("Cannot Insert Location")
						.append("' WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
					continue;
				}
				//
				bpl = new MBPartnerLocation (bp);
				bpl.setC_Location_ID(location.getC_Location_ID());
				bpl.setPhone(impBP.getXX_Phone());
				bpl.setPhone2(impBP.getXX_Phone2());
				bpl.setFax(impBP.getFax());
				if (bpl.save())
				{
					log.finest("Insert BP Location - " + bpl.getC_BPartner_Location_ID());
					impBP.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
					
					impBP.save();
				}
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer ("UPDATE I_BPartner i "
						+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
						.append("Cannot Insert BPLocation")
						.append("' WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
					DB.executeUpdate(get_Trx(), sql.toString());
					continue;
				}			
				
				
				//	****	Create/Update Contact	****
				MUser user;
				if (impBP.getAD_User_ID() != 0)
				{
					user = new MUser (getCtx(), impBP.getAD_User_ID(), get_TrxName());
					if (user.getC_BPartner_ID() == 0)
						user.setC_BPartner_ID(bp.getC_BPartner_ID());
					else if (user.getC_BPartner_ID() != bp.getC_BPartner_ID())
					{
						rollback();
						noInsert--;
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
							.append("BP of User <> BP")
							.append("' WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						continue;
					}
					
					if (impBP.getC_Greeting_ID() != 0)
						user.setC_Greeting_ID(impBP.getC_Greeting_ID());
					
					String name = impBP.getContactName();
					if (name == null || name.length() == 0)
						name = impBP.getEMail();
					user.setName(name);
					if (impBP.getXX_ContactType() != null)
						user.setXX_ContactType(impBP.getXX_ContactType());
					
					if (impBP.getXX_Phone() != null)
						user.setPhone(impBP.getXX_Phone());
					if (impBP.getXX_Phone2() != null)
						user.setPhone2(impBP.getXX_Phone2());
					if (impBP.getXX_Fax() != null)
						user.setFax(impBP.getXX_Fax());
					if (impBP.getEMail() != null)
						user.setEMail(impBP.getEMail());


					if (bpl != null)
						user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
					if (user.save())
					{
						log.finest("Update BP Contact - " + user.getAD_User_ID());
					}
					else
					{
						rollback();
						noInsert--;
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
							.append("Cannot Update BP Contact")
							.append("' WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						continue;
					}
				}
				 	//	New Contact
				else if (impBP.getContactName() != null || impBP.getEMail() != null)
				{
					user = new MUser (bp);
					if (impBP.getC_Greeting_ID() != 0)
						user.setC_Greeting_ID(impBP.getC_Greeting_ID());
					String name = impBP.getContactName();
					if (name == null || name.length() == 0)
						name = impBP.getEMail();
					user.setName(name);
					user.setXX_ContactType(impBP.getXX_ContactType());
					user.setPhone(impBP.getXX_Phone());
					user.setPhone2(impBP.getXX_Phone2());
					user.setFax(impBP.getXX_Fax());
					user.setEMail(impBP.getEMail());
					
					if (bpl != null)
						user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
					if (user.save())
					{
						log.finest("Insert BP Contact - " + user.getAD_User_ID());
						impBP.setAD_User_ID(user.getAD_User_ID());
					}
					else
					{
						rollback();
						noInsert--;
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
							.append("Cannot Insert BPContact")
							.append("' WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						continue;
					}
				}
				/**
				//	****	Create/Update CBPartner Bank Account	****
				MBPBankAccount bpBankAccount;
				if(impBP.getC_BP_BankAccount_ID() == new Integer(0))
				{
					bpBankAccount = new MBPBankAccount(getCtx(), 0, get_TrxName());
					nuevo = new Boolean(true);
				}	
				else  
				{
					bpBankAccount = new MBPBankAccount(getCtx(), impBP.getC_BP_BankAccount_ID(), get_TrxName());
					nuevo = new Boolean(false);
				}
							
				bpBankAccount.setC_Bank_ID(impBP.getC_Bank_ID());				
				bpBankAccount.setAccountNo(impBP.get_AccountNo());
				if(impBP.getXX_IsPrimary()){
					bpBankAccount.setXX_IsPrimary(true);		
				}else{
					bpBankAccount.setXX_IsPrimary(false);
				}
				bpBankAccount.setBankAccountType(impBP.get_BankAccountType());
				bpBankAccount.setC_BPartner_ID(bp.getC_BPartner_ID());
				
				if (bpBankAccount.save())
				{
					log.finest("Insert BP Bank Account - " + bpBankAccount.getC_BP_BankAccount_ID());
					impBP.setC_BP_BankAccount_ID(bpBankAccount.getC_BP_BankAccount_ID());
					
					impBP.save();
				}
				else
				{
					rollback();
					noInsert--;
					sql = new StringBuffer ("UPDATE I_BPartner i "
						+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
						.append("Cannot Insert BP Bank Account")
						.append("' WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
					DB.executeUpdate(sql.toString(), get_TrxName());
					continue;
				}*/
				//
				impBP.setI_IsImported(X_I_BPartner.I_ISIMPORTED_Yes);
				impBP.setProcessed(true);
				impBP.setProcessing(false);
				impBP.save();
				commit();
			}	//	for all I_Product
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "", e);
			rollback();
		}

		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@C_BPartner_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@C_BPartner_ID@: @Updated@");
		return "";
	}	//	doIt

}	//	ImportBPartner
