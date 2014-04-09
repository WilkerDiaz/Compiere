/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.common.CompiereStateException;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLocation;
import org.compiere.model.MPInstance;
import org.compiere.model.MUser;
import org.compiere.model.X_C_Invoice;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_I_Invoice;
import org.compiere.util.CLogMgt;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Language;
import org.compiere.util.Login;

/**
 *	Import Invoice from I_Invoice
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportInvoice.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ImportInvoice extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Organization to be imported to		*/
	private int				m_AD_Org_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;
	/**	Document Action					*/
	private String			m_docAction = X_C_Invoice.DOCACTION_Prepare;


	/** Effective						*/
	private Timestamp		m_DateValue = null;

	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=? " ;	

	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

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
				m_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("AD_Org_ID"))
				m_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(element.getParameter());
			else if (name.equals("DocAction"))
				m_docAction = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return clear Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		String sql = null;
		int no = 0;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = "DELETE FROM I_Invoice "
				  + "WHERE I_IsImported='Y'"
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = "UPDATE I_Invoice "
			  + "SET AD_Client_ID = COALESCE (AD_Client_ID,?),"
			  + " AD_Org_ID = COALESCE (AD_Org_ID,?),"
			  + " IsActive = COALESCE (IsActive, 'Y'),"
			  + " Created = COALESCE (Created, SysDate),"
			  + " CreatedBy = COALESCE (CreatedBy, 0),"
			  + " Updated = COALESCE (Updated, SysDate),"
			  + " UpdatedBy = COALESCE (UpdatedBy, 0),"
			  + " I_ErrorMsg = NULL,"
			  + " I_IsImported = 'N' "
			  + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL";
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID, m_AD_Org_ID});
		log.info ("Reset=" + no);

		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = "UPDATE I_Invoice o "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Org, '"
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND I_IsImported<>'Y' " 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Org=" + no);

		//	Document Type - PO - SO
		sql = "UPDATE I_Invoice o "
			  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName"
			  + " AND d.DocBaseType IN ('API','APC') AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.fine("Set PO DocType=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName"
			  + " AND d.DocBaseType IN ('ARI','ARC') AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.fine("Set SO DocType=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName"
			  + " AND d.DocBaseType IN ('API','ARI','APC','ARC') AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.fine("Set DocType=" + no);
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid DocTypeName, ' "
			  + "WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid DocTypeName=" + no);
		//	DocType Default
		sql = "UPDATE I_Invoice o "
			  + "SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'"
			  + " AND d.DocBaseType='API' AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.fine("Set PO Default DocType=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'"
			  + " AND d.DocBaseType='ARI' AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.fine("Set SO Default DocType=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'"
			  + " AND d.DocBaseType IN('ARI','API') AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx IS NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.fine("Set Default DocType=" + no);
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No DocType, ' "
			  + "WHERE C_DocType_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("No DocType=" + no);

		//	Set IsSOTrx
		sql = "UPDATE I_Invoice o SET IsSOTrx='Y' "
			  + "WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='ARI' AND o.AD_Client_ID=d.AD_Client_ID)"
			  + " AND C_DocType_ID IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set IsSOTrx=Y=" + no);
		sql = "UPDATE I_Invoice o SET IsSOTrx='N' "
			  + "WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='API' AND o.AD_Client_ID=d.AD_Client_ID)"
			  + " AND C_DocType_ID IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set IsSOTrx=N=" + no);

		//	Price List
		sql = "UPDATE I_Invoice o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'"
			  + " AND p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Default Currency PriceList=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'"
			  + " AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Default PriceList=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p "
			  + " WHERE p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Currency PriceList=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p "
			  + " WHERE p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set PriceList=" + no);
		//
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No PriceList, ' "
			  + "WHERE M_PriceList_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning("No PriceList=" + no);

		//	Payment Rule
		//  We support Payment Rule being input in the login language 
		Language language = Language.getLoginLanguage();		//	Base Language
		String AD_Language = language.getAD_Language();
		sql = "UPDATE I_Invoice O " + 
				"SET PaymentRule= " +
			  	"(SELECT R.Value "+
			  	"  FROM AD_Ref_List R " + 
			  	"  left outer join AD_Ref_List_Trl RT " + 
			  	"  on RT.AD_Ref_List_ID = R.AD_Ref_List_ID and RT.AD_Language = ? " +
			  	"  WHERE R.AD_Reference_ID = 195 and coalesce( RT.Name, R.Name ) = O.PaymentRuleName ) " +
			    "WHERE PaymentRule is null AND PaymentRuleName IS NOT NULL AND I_IsImported<>'Y'" 
			  	+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{ AD_Language, m_AD_Client_ID });
		log.fine("Set PaymentRule=" + no);
		// do not set a default; if null, the import logic will derive from the business partner
		// do not error in absence of a default

		//	Payment Term
		sql = "UPDATE I_Invoice o "
			  + "SET C_PaymentTerm_ID=(SELECT C_PaymentTerm_ID FROM C_PaymentTerm p"
			  + " WHERE o.PaymentTermValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE C_PaymentTerm_ID IS NULL AND PaymentTermValue IS NOT NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set PaymentTerm=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET C_PaymentTerm_ID=(SELECT MAX(C_PaymentTerm_ID) FROM C_PaymentTerm p"
			  + " WHERE p.IsDefault='Y' AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE C_PaymentTerm_ID IS NULL AND o.PaymentTermValue IS NULL AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Default PaymentTerm=" + no);
		//
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No PaymentTerm, ' "
			  + "WHERE C_PaymentTerm_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("No PaymentTerm=" + no);

		//	BP from EMail
		sql = "UPDATE I_Invoice o "
			  + "SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u"
			  + " WHERE o.EMail=u.EMail AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) "
			  + "WHERE C_BPartner_ID IS NULL AND EMail IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set BP from EMail=" + no);
		//	BP from ContactName
		sql = "UPDATE I_Invoice o "
			  + "SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u"
			  + " WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) "
			  + "WHERE C_BPartner_ID IS NULL AND ContactName IS NOT NULL"
			  + " AND EXISTS (SELECT Name FROM AD_User u WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL GROUP BY Name HAVING COUNT(*)=1)"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set BP from ContactName=" + no);
		//	BP from Value
		sql = "UPDATE I_Invoice o "
			  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
			  + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set BP from Value=" + no);
		//	Default BP
		sql = "UPDATE I_Invoice o "
			  + "SET C_BPartner_ID=(SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo c"
			  + " WHERE o.AD_Client_ID=c.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NULL AND Name IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Default BP=" + no);

		//	Existing Location ? Exact Match
		sql = "UPDATE I_Invoice o "
			  + "SET C_BPartner_Location_ID=(SELECT C_BPartner_Location_ID"
			  + " FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)"
			  + " WHERE o.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=o.AD_Client_ID"
			  + " AND DUMP(o.Address1)=DUMP(l.Address1) AND DUMP(o.Address2)=DUMP(l.Address2)"
			  + " AND DUMP(o.City)=DUMP(l.City) AND DUMP(o.Postal)=DUMP(l.Postal)"
			  + " AND DUMP(o.C_Region_ID)=DUMP(l.C_Region_ID) AND DUMP(o.C_Country_ID)=DUMP(l.C_Country_ID)) "
			  + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			  + " AND I_IsImported='N'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Found Location=" + no);
		//	Set Location from BPartner
		sql = "UPDATE I_Invoice o "
			  + "SET C_BPartner_Location_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l"
			  + " WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID"
			  + " AND ((l.IsBillTo='Y' AND o.IsSOTrx='Y') OR o.IsSOTrx='N')"
			  + ") "
			  + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set BP Location from BP=" + no);
		//
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No BP Location, ' "
			  + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("No BP Location=" + no);

		// Check for Duplicate Document Number/BP/Doc Type Combinations
		sql = "UPDATE I_Invoice i "
				  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Duplicate Document No, ' "
				  + "WHERE EXISTS (SELECT 1 FROM C_Invoice inv WHERE inv.C_BPartner_ID=i.C_BPartner_ID "
				  + " AND inv.C_DocTypeTarget_ID = i.C_DocType_ID"
				  + " AND inv.DocumentNo = i.DocumentNo)"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Duplicate Document Number=" + no);

		//	Set Country
		/**
		sql = "UPDATE I_Invoice o "
			  + "SET CountryCode=(SELECT CountryCode FROM C_Country c WHERE c.IsDefault='Y'"
			  + " AND c.AD_Client_ID IN (0, o.AD_Client_ID) AND ROWNUM=1) "
			  + "WHERE C_BPartner_ID IS NULL AND CountryCode IS NULL AND C_Country_ID IS NULL"
			  + " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(sql, new Object[] {m_AD_Client_ID}, get_Trx());
		log.fine("Set Country Default=" + no);
		**/
		sql = "UPDATE I_Invoice o "
			  + "SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c"
			  + " WHERE o.CountryCode=c.CountryCode AND c.IsSummary='N' AND c.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL AND CountryCode IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Country=" + no);
		//
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Country, ' "
			  + "WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Country=" + no);

		//	Set Region
		sql = "UPDATE I_Invoice o "
			  + "Set RegionName=(SELECT MAX(Name) FROM C_Region r"
			  + " WHERE r.IsDefault='Y' AND r.C_Country_ID=o.C_Country_ID"
			  + " AND r.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Region Default=" + no);
		//
		sql = "UPDATE I_Invoice o "
			  + "Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r"
			  + " WHERE r.Name=o.RegionName AND r.C_Country_ID=o.C_Country_ID"
			  + " AND r.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Region=" + no);
		//
		sql = "UPDATE I_Invoice o "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Region, ' "
			  + "WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL "
			  + " AND EXISTS (SELECT * FROM C_Country c"
			  + " WHERE c.C_Country_ID=o.C_Country_ID AND c.HasRegion='Y')"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Region=" + no);

		//	Product
		sql = "UPDATE I_Invoice o "
			  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
			  + " WHERE o.ProductValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Product from Value=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
			  + " WHERE o.UPC=p.UPC AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_Product_ID IS NULL AND UPC IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Product from UPC=" + no);
		sql = "UPDATE I_Invoice o "
			  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
			  + " WHERE o.SKU=p.SKU AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_Product_ID IS NULL AND SKU IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Product fom SKU=" + no);
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Product, ' "
			  + "WHERE M_Product_ID IS NULL AND (ProductValue IS NOT NULL OR UPC IS NOT NULL OR SKU IS NOT NULL)"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Product=" + no);

		// Charge
		sql = "UPDATE I_Invoice i "
				  + "SET C_Charge_ID=(SELECT MAX(C_Charge_ID) FROM C_Charge c"
				  + " WHERE i.ChargeName=c.Name AND i.AD_Client_ID=c.AD_Client_ID) "
				  + "WHERE C_Charge_ID IS NULL AND ChargeName IS NOT NULL"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
			log.fine("Set Charge from Name=" + no);

		sql = "UPDATE I_Invoice "
				  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Charge, ' "
				  + "WHERE C_Charge_ID IS NULL AND (ChargeName IS NOT NULL)"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
			if (no != 0)
				log.warning ("Invalid Charge=" + no);

		sql = "UPDATE I_Invoice "
				  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Both Charge and Product are specified, ' "
				  + "WHERE C_Charge_ID IS NOT NULL AND M_Product_ID IS NOT NULL"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
			if (no != 0)
				log.warning ("Charge and Product are specified=" + no);

		//	Tax
		sql = "UPDATE I_Invoice o "
			  + "SET C_Tax_ID=(SELECT MAX(C_Tax_ID) FROM C_Tax t"
			  + " WHERE o.TaxIndicator=t.TaxIndicator AND o.AD_Client_ID=t.AD_Client_ID) "
			  + "WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Tax=" + no);
		sql = "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Tax, ' "
			  + "WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Tax=" + no);
		
		commit();
		
		//	-- New BPartner ---------------------------------------------------
		sql =  "UPDATE I_Invoice "
			  + "SET BPartnerValue = COALESCE(EMail,Name) " 
			  + "WHERE C_BPartner_ID IS NULL "
			  + "AND BPartnerValue IS NULL "
			  + "AND (Email IS NOT NULL OR Name IS NOT NULL)"
			  + "AND I_IsImported='N'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.fine ("Update BPartnerValue from Email/Name=" + no);
	
		sql =  "UPDATE I_Invoice "
				  + "SET Name = COALESCE(ContactName,BPartnerValue) " 
				  + "WHERE C_BPartner_ID IS NULL "
				  + "AND Name IS NULL "
				  + "AND (ContactName IS NOT NULL OR BPartnerValue IS NOT NULL) "
				  + "AND I_IsImported='N'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.fine ("Update Name from ContactName/BPartnerValue=" + no);
	
		//	BP from Value
		sql =  "UPDATE I_Invoice o "
			  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
			  + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
			  + " AND I_IsImported='N'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.fine("Set BP from updated Value=" + no);
	
		Map <String, MBPartner> bpartnerMap = new HashMap <String, MBPartner>();		
		Map <String, List<MLocation>> bpLocationMap= new HashMap <String, List<MLocation>>();
		Map <String, List<MUser>> bpUserMap= new HashMap <String, List<MUser>>();
		
		//	Go through remaining Order Records w/o C_BPartner_ID
		sql =  	"SELECT * FROM I_Invoice "
			  + "WHERE I_IsImported='N' "
			  + "AND C_BPartner_ID IS NULL "
			  + "AND BPartnerValue IS NOT NULL" 
			  + STD_CLIENT_CHECK
			  + " ORDER by BPartnerValue ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_Trx());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery ();
	
			while (rs.next ())
			{
				X_I_Invoice imp = new X_I_Invoice (getCtx(), rs, get_Trx());
		
				MBPartner bp = bpartnerMap.get(imp.getBPartnerValue());
	
				if(bp == null) {
					if(bpartnerMap.size() >= COMMITCOUNT) {
						saveBPartners(bpartnerMap, bpLocationMap, bpUserMap);
						bpartnerMap.clear();
						bpLocationMap.clear();
						bpUserMap.clear();
					}
					
					bp = new MBPartner (getCtx(), -1, get_Trx());
					bp.setClientOrg (imp.getAD_Client_ID (), imp.getAD_Org_ID ());
					bp.setValue (imp.getBPartnerValue());
					bp.setName (imp.getName());
					bpartnerMap.put(imp.getBPartnerValue(), bp);
				}
				
				List<MLocation> bpLocations = bpLocationMap.get(imp.getBPartnerValue());
				if(bpLocations == null) {
					bpLocations = new ArrayList <MLocation>();
					bpLocationMap.put(imp.getBPartnerValue(), bpLocations);
				}
				
				MLocation location = null;
				for(MLocation loc : bpLocations) {
					if(loc.equals(imp.getC_Country_ID(), imp.getC_Region_ID(), 
							imp.getPostal(), "", imp.getCity(), 
							imp.getAddress1(), imp.getAddress2())) {
					location = loc;
					break;
					}
				}
				
				if(location == null) {
					location = new MLocation (getCtx(), 0, get_Trx());
					location.setAddress1 (imp.getAddress1 ());
					location.setAddress2 (imp.getAddress2 ());
					location.setCity (imp.getCity ());
					location.setPostal (imp.getPostal ());
					if (imp.getC_Region_ID () != 0)
						location.setC_Region_ID (imp.getC_Region_ID ());
					location.setC_Country_ID (imp.getC_Country_ID ());
					bpLocations.add(location);
				}
				
				List<MUser> bpUsers = bpUserMap.get(imp.getBPartnerValue());
				if(bpUsers == null) {
					bpUsers = new ArrayList <MUser>();
					bpUserMap.put(imp.getBPartnerValue(), bpUsers);
				}
				
				MUser contact = null;
				for(MUser user : bpUsers) {
					if(user.getName().equals(imp.getContactName()) ||
							user.getName().equals(imp.getName())) {
					contact = user;
					break;
					}
				}
				
				if(contact == null) {
					contact = new MUser (getCtx(), 0, get_Trx());
					if (imp.getContactName () == null)
						contact.setName (imp.getName ());
					else
						contact.setName (imp.getContactName ());
					contact.setEMail (imp.getEMail ());
					contact.setPhone (imp.getPhone ());
					bpUsers.add(contact);
				}
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, "BP - " + sql.toString(), e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		saveBPartners(bpartnerMap, bpLocationMap, bpUserMap);
		sql =  "UPDATE I_Invoice "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No BPartner, ' "
			  + "WHERE C_BPartner_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning ("No BPartner=" + no);
		
		commit();

		//	-- New Invoices -----------------------------------------------------
		int noInsert = 0;
		int noInsertLine = 0;

		Map<Integer, X_I_Invoice> importInvoiceMap = new HashMap<Integer, X_I_Invoice>();
		Map <MInvoice, List<MInvoiceLine>> invoiceMap = new HashMap<MInvoice, List<MInvoiceLine>>();
			
		//	Go through Invoice Records w/o
		sql = "SELECT * FROM I_Invoice "
			  + "WHERE I_IsImported='N'" 
			  + STD_CLIENT_CHECK
			  + " ORDER BY Ad_Org_ID,C_BPartner_ID, C_BPartner_Location_ID, I_Invoice_ID";
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_Trx());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery ();
			//	Group Change
			int oldC_BPartner_ID = 0;
			int oldC_BPartner_Location_ID = 0;
			int oldAD_Org_ID = 0;
			String oldDocumentNo = "";
			int oldDocumentType = 0;
			//
			MInvoice invoice = null;
			
			int lineNo = 0;
			
			List<MInvoiceLine> invoiceLines = null;
			while (rs.next ())
			{
				X_I_Invoice imp = new X_I_Invoice (getCtx (), rs, get_Trx());
				
				String cmpDocumentNo = imp.getDocumentNo();
				if (cmpDocumentNo == null)
					cmpDocumentNo = "";
				//	New Invoice
				if (invoice == null
					||	oldAD_Org_ID != imp.getAD_Org_ID() 
					|| oldC_BPartner_ID != imp.getC_BPartner_ID() 
					|| oldC_BPartner_Location_ID != imp.getC_BPartner_Location_ID()
					|| oldDocumentType != imp.getC_DocType_ID()
					|| !oldDocumentNo.equals(cmpDocumentNo)	)
				{
					if(invoiceMap.size() >= COMMITCOUNT) {
						saveInvoices (invoiceMap, importInvoiceMap);
						invoiceMap.clear();
						importInvoiceMap.clear();
					}

					//	Group Change
					oldAD_Org_ID = imp.getAD_Org_ID();
					oldC_BPartner_ID = imp.getC_BPartner_ID();
					oldC_BPartner_Location_ID = imp.getC_BPartner_Location_ID();
					oldDocumentNo = imp.getDocumentNo();
					oldDocumentType = imp.getC_DocType_ID();
					if (oldDocumentNo == null)
						oldDocumentNo = "";
					//
					invoice = new MInvoice (imp);
					
					//	Default SalesRep from person running the import, if not specified
					if (invoice.getSalesRep_ID() == 0)
						invoice.setSalesRep_ID(getAD_User_ID());
					
					noInsert++;
					
					invoiceLines = new ArrayList<MInvoiceLine>();
					invoiceMap.put(invoice, invoiceLines);
					lineNo = 10;
				}
				
				importInvoiceMap.put(imp.getI_Invoice_ID(), imp);
				imp.setC_Invoice_ID (invoice.getC_Invoice_ID());
				//	New InvoiceLine
				MInvoiceLine line = new MInvoiceLine (invoice, imp.getI_Invoice_ID());
				if (imp.getLineDescription() != null)
					line.setDescription(imp.getLineDescription());
				line.setLine(lineNo);
				lineNo += 10;
				if (imp.getM_Product_ID() != 0)
					line.setM_Product_ID(imp.getM_Product_ID(), true);
				
				if (imp.getC_Charge_ID() != 0)
					line.setC_Charge_ID(imp.getC_Charge_ID());

				line.setQty(imp.getQtyOrdered());
				line.setPrice();
				BigDecimal price = imp.getPriceActual();
				if (price != null && Env.ZERO.compareTo(price) != 0)
					line.setPrice(price);
				if (imp.getC_Tax_ID() != 0)
					line.setC_Tax_ID(imp.getC_Tax_ID());
				else
				{
					line.setTax();
					imp.setC_Tax_ID(line.getC_Tax_ID());
				}
				BigDecimal taxAmt = imp.getTaxAmt();
				if (taxAmt != null && Env.ZERO.compareTo(taxAmt) != 0)
					line.setTaxAmt(taxAmt);
				
				noInsertLine ++;
				invoiceLines.add(line);
				
				imp.setI_IsImported(X_I_Invoice.I_ISIMPORTED_Yes);
				imp.setProcessed(true);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			log.log(Level.SEVERE, "CreateInvoice", e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		saveInvoices (invoiceMap, importInvoiceMap);
		
		//	Set Error to indicator to not imported
		sql = "UPDATE I_Invoice "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y' "
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		addLog (0, null, new BigDecimal (no), "@Errors@");
		//
		addLog (0, null, new BigDecimal (noInsert), "@C_Invoice_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertLine), "@C_InvoiceLine_ID@: @Inserted@");
		return "";
	}	//	doIt

	public void saveBPartners (Map<String, MBPartner> bpartnerMap, 
								Map<String, List<MLocation>> bpLocationMap,
								Map<String, List<MUser>> bpUserMap) throws Exception {

		if(bpartnerMap.isEmpty()) 
			return;


		List<MBPartner> bpartnersToSave = new ArrayList<MBPartner>(bpartnerMap.values());
		if(!PO.saveAll(get_Trx(), bpartnersToSave)) 
			throw new CompiereStateException("Could not save Business Partners");

		List<MLocation> locationsToSave = new ArrayList<MLocation>();
		for(List<MLocation> locations : bpLocationMap.values())
			locationsToSave.addAll(locations);

		if(!PO.saveAll(get_Trx(), locationsToSave)) 
			throw new CompiereStateException("Could not save Locations");

		List <MBPartnerLocation> bpLocationsToSave = new ArrayList<MBPartnerLocation> ();
		for(Map.Entry<String, List<MLocation>> entry : bpLocationMap.entrySet()) {
			MBPartner bp = bpartnerMap.get(entry.getKey());
			for(MLocation loc : entry.getValue()) {
				MBPartnerLocation bpl = new MBPartnerLocation (bp);
				bpl.setC_Location_ID (loc.getC_Location_ID ());
				bpLocationsToSave.add(bpl);
			}
		}

		if(!PO.saveAll(get_Trx(), bpLocationsToSave)) 
			throw new CompiereStateException("Could not save BP Locations");

		List <MUser> usersToSave = new ArrayList<MUser>();
		for(Map.Entry<String, List<MUser>> entry : bpUserMap.entrySet()) {
			MBPartner bp = bpartnerMap.get(entry.getKey());
			for(MUser user : entry.getValue()) {
				user.setC_BPartner_ID(bp.getC_BPartner_ID());
				usersToSave.add(user);
			}
		}

		if(!PO.saveAll(get_Trx(), usersToSave)) 
			throw new CompiereStateException("Could not save Users");

		// Update BPs for new Business Partners
		String sql =  "UPDATE I_Order o "
						+ "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp "
						+ "WHERE o.BPartnerValue=bp.Value "
						+ "AND o.AD_Client_ID=bp.AD_Client_ID) "
						+ "WHERE C_BPartner_ID IS NULL "
						+ "AND BPartnerValue IS NOT NULL "
						+ "AND I_IsImported='N'" 
						+ STD_CLIENT_CHECK;
		int no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.fine("Set BP for newly created BPs =" + no);

		//	Update BP Location for new Business Partners
		sql =  "UPDATE I_Order o "
				+ "SET (BillTo_ID, C_BPartner_Location_ID)=(SELECT C_BPartner_Location_ID,C_BPartner_Location_ID "
				+ "FROM C_BPartner_Location bpl "
				+ "INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID) "
				+ "WHERE o.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=o.AD_Client_ID "
				+ "AND DUMP(o.Address1)=DUMP(l.Address1) AND DUMP(o.Address2)=DUMP(l.Address2) "
				+ "AND DUMP(o.City)=DUMP(l.City) AND DUMP(o.Postal)=DUMP(l.Postal) "
				+ "AND DUMP(o.C_Region_ID)=DUMP(l.C_Region_ID) AND DUMP(o.C_Country_ID)=DUMP(l.C_Country_ID)) "
				+ "WHERE C_BPartner_ID IS NOT NULL "
				+ "AND C_BPartner_Location_ID IS NULL "
				+ "AND I_IsImported='N'" 
				+ STD_CLIENT_CHECK;
				no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
				log.fine("Set BP Location for newly created BPs=" + no);

				//	Set User for new Business Partners
			sql =  "UPDATE I_Order o "
				+ "SET AD_User_ID=(SELECT AD_User_ID FROM AD_User u "
				+ "WHERE u.AD_Client_ID=o.AD_Client_ID "
				+ "AND u.C_BPartner_ID=o.C_BPartner_ID " 
				+ "AND u.Name=COALESCE(o.ContactName, o.Name) )"
				+ "WHERE C_BPartner_ID IS NOT NULL "
				+ "AND AD_User_ID IS NULL "
				+ "AND EXISTS (SELECT 1 FROM AD_User u WHERE o.C_BPartner_ID=u.C_BPartner_ID "
				+ "AND o.AD_Client_ID=u.AD_Client_ID "
				+ "AND u.Name=COALESCE(o.ContactName, o.Name)) "
				+ "AND I_IsImported<>'N'" 
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
			log.fine("Set User for newly created BPs=" + no);
				
			commit();
		}	

	public void saveInvoices (	Map<MInvoice, List<MInvoiceLine>> invoiceMap,
								Map<Integer, X_I_Invoice> importInvoiceMap) throws Exception{

		if(invoiceMap.isEmpty()) 
			return;

		List<MInvoice> invoicesToSave = new ArrayList<MInvoice>(invoiceMap.keySet());
		if(!PO.saveAll(get_Trx(), invoicesToSave)) 
			throw new CompiereStateException("Could not save Invoices");

		List<MInvoiceLine> invoiceLinesToSave = new ArrayList<MInvoiceLine>();
		for(Map.Entry<MInvoice, List<MInvoiceLine>> entry : invoiceMap.entrySet()) {
			MInvoice invoice = entry.getKey();
			for(MInvoiceLine invoiceLine : entry.getValue()) {
				invoiceLine.setC_Invoice_ID(invoice.getC_Invoice_ID());
				invoiceLinesToSave.add(invoiceLine);
			}
		}

		if(!PO.saveAll(get_Trx(), invoiceLinesToSave)) 
			throw new CompiereStateException("Could not save Invoices");

		List<X_I_Invoice> importInvoicesToSave = new ArrayList<X_I_Invoice>();
		for(MInvoiceLine line :invoiceLinesToSave) {
			X_I_Invoice imp = importInvoiceMap.get(line.getI_Invoice_ID());
			if(imp != null) {
				imp.setC_InvoiceLine_ID(line.getC_InvoiceLine_ID());
				imp.setC_Invoice_ID(line.getC_Invoice_ID());
				importInvoicesToSave.add(imp);
			}
		}

		commit();

		for(MInvoice invoice : invoicesToSave) {
			invoice.setDocAction(m_docAction);
			if(!DocumentEngine.processIt(invoice, m_docAction)) {
				// Ignore errors in processing
				log.warning("Could not process Invoice : " + invoice.getDocumentNo());
			}
		}

		if(!PO.saveAll (get_Trx(), invoicesToSave))
			throw new CompiereStateException("Could not save Invoice");

		if(!PO.saveAll (get_Trx(), importInvoicesToSave))
			throw new CompiereStateException("Could not save Import Invoice records");

		commit();
	}

	public static void main(String[] args)
    {
		System.setProperty ("PropertyFile", "/home/namitha/Useful/Compiere.properties");
		Compiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//	Same Login entries as entered
		Ini.setProperty(Ini.P_UID, "GardenAdmin");
		Ini.setProperty(Ini.P_PWD, "GardenAdmin");
		Ini.setProperty(Ini.P_ROLE, "GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG, "HQ");
		Ini.setProperty(Ini.P_WAREHOUSE, "HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE, "English");
		Ini.setProperty(Ini.P_IMPORT_BATCH_SIZE, "100");
		
		Ctx ctx = Env.getCtx();
		Login login = new Login(ctx);
		if (!login.batchLogin(null, null))
			System.exit(1);

		//	Reduce Log level for performance
		CLogMgt.setLoggerLevel(Level.WARNING, null);
		CLogMgt.setLevel(Level.WARNING);

		//	Data from Login Context
		int AD_Client_ID = ctx.getAD_Client_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		//	Hardcoded
		int AD_Process_ID = 207;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();
		
		ProcessInfo pi = new ProcessInfo("Import", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(false);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		DB.startLoggingUpdates();
		
		// Step 3: Run the process directly
		ImportInvoice test = new ImportInvoice();
		test.m_AD_Client_ID = ctx.getAD_Client_ID();
		test.m_AD_Org_ID = ctx.getAD_Org_ID();
		test.m_deleteOldImported = true;
		test.m_docAction = X_C_Order.DOCACTION_Complete;
		
		long start = System.currentTimeMillis();
		
		test.startProcess(ctx, pi, null);
		
		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		System.out.println("Total: " + duration + "s");

		// Step 4: get results
		if (pi.isError())
			System.err.println("Error: " + pi.getSummary());
		else
			System.out.println("OK: " + pi.getSummary());
		System.out.println(pi.getLogInfo());

		// stop logging database updates
		String logResult = DB.stopLoggingUpdates(0);
		System.out.println(logResult);
		
    }
}
