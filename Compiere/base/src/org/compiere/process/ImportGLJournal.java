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

import java.math.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

import org.compiere.Compiere;
import org.compiere.common.CompiereSQLException;
import org.compiere.common.CompiereStateException;
import org.compiere.framework.PO;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Import GL Journal Batch/JournalLine from I_Journal
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportGLJournal.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class ImportGLJournal extends SvrProcess
{
	/**	Client to be imported to		*/
	private int 			m_AD_Client_ID = 0;
	/**	Organization to be imported to	*/
	private int 			m_AD_Org_ID = 0;
	/**	Acct Schema to be imported to	*/
	private int				m_C_AcctSchema_ID = 0;
	/** Default Date					*/
	private Timestamp		m_DateAcct = null;
	/**	Delete old Imported				*/
	private boolean			m_DeleteOldImported = false;
	/**	Don't import					*/
	private boolean			m_IsValidateOnly = false;
	/** Import if no Errors				*/
	private boolean			m_IsImportOnlyNoErrors = true;

	private static final boolean TESTMODE = true;
	/** Commit every 100 entities	*/
	private static final int COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=? " ;	

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
			else if (name.equals("C_AcctSchema_ID"))
				m_C_AcctSchema_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DateAcct"))
				m_DateAcct = (Timestamp)element.getParameter();
			else if (name.equals("IsValidateOnly"))
				m_IsValidateOnly = "Y".equals(element.getParameter());
			else if (name.equals("IsImportOnlyNoErrors"))
				m_IsImportOnlyNoErrors = "Y".equals(element.getParameter());
			else if (name.equals("DeleteOldImported"))
				m_DeleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		log.info("IsValidateOnly=" + m_IsValidateOnly + ", IsImportOnlyNoErrors=" + m_IsImportOnlyNoErrors);
		String sql = null;
		int no = 0;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_DeleteOldImported)
		{
			sql = "DELETE FROM I_GLJournal "
				  + "WHERE I_IsImported='Y'"
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}

		//	Set IsActive, Created/Updated
		sql = "UPDATE I_GLJournal "
			+ "SET IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL";
		no = DB.executeUpdate(get_Trx(),sql);
		log.info ("Reset=" + no);

		//	Set Client from Name
		sql = "UPDATE I_GLJournal i "
			+ "SET AD_Client_ID=(SELECT c.AD_Client_ID FROM AD_Client c WHERE c.Value=i.ClientValue) "
			+ "WHERE (AD_Client_ID IS NULL OR AD_Client_ID=0) AND ClientValue IS NOT NULL"
			+ " AND I_IsImported<>'Y'";
		no = DB.executeUpdate(get_Trx(),sql);
		log.fine("Set Client from Value=" + no);
		
//		Set Default Client, Doc Org, AcctSchema, DatAcct
		StringBuffer sqlbuffer = new StringBuffer("UPDATE I_GLJournal "
			  + "SET AD_Client_ID = COALESCE (AD_Client_ID,?),"
			  + " AD_OrgDoc_ID = COALESCE (AD_OrgDoc_ID,?),");
		if (m_C_AcctSchema_ID != 0)
			sqlbuffer.append(" C_AcctSchema_ID = COALESCE (C_AcctSchema_ID,?),");
		if (m_DateAcct != null)
			sqlbuffer.append(" DateAcct = COALESCE (DateAcct,?),");
		sqlbuffer.append(" Updated = COALESCE (Updated, SysDate) "
			  + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(),sqlbuffer.toString(),m_AD_Client_ID,m_AD_Org_ID,m_C_AcctSchema_ID,m_DateAcct);
		log.fine("Client/DocOrg/Default=" + no);

		//	Error Doc Org
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = "UPDATE I_GLJournal o "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Doc Org, '"
			+ "WHERE (AD_OrgDoc_ID IS NULL OR AD_OrgDoc_ID=0"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Doc Org=" + no);

		//	Set AcctSchema
		sql = "UPDATE I_GLJournal i "
			+ "SET C_AcctSchema_ID=(SELECT a.C_AcctSchema_ID FROM C_AcctSchema a"
			+ " WHERE i.AcctSchemaName=a.Name AND i.AD_Client_ID=a.AD_Client_ID) "
			+ "WHERE C_AcctSchema_ID IS NULL AND AcctSchemaName IS NOT NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set AcctSchema from Name=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET C_AcctSchema_ID=(SELECT c.C_AcctSchema1_ID FROM AD_ClientInfo c WHERE c.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_AcctSchema_ID IS NULL AND AcctSchemaName IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set AcctSchema from Client=" + no);
		//	Error AcctSchema
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid AcctSchema, '"
			+ "WHERE (C_AcctSchema_ID IS NULL OR C_AcctSchema_ID=0"
			+ " OR NOT EXISTS (SELECT * FROM C_AcctSchema a WHERE i.AD_Client_ID=a.AD_Client_ID))"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid AcctSchema=" + no);

		//	Set DateAcct (mandatory)
		sql = "UPDATE I_GLJournal i "
			+ "SET DateAcct=SysDate "
			+ "WHERE DateAcct IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set DateAcct=" + no);

		//	Document Type
		sql = "UPDATE I_GLJournal i "
			+ "SET C_DocType_ID=(SELECT d.C_DocType_ID FROM C_DocType d"
			+ " WHERE d.Name=i.DocTypeName AND d.DocBaseType='GLJ' AND i.AD_Client_ID=d.AD_Client_ID) "
			+ "WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set DocType=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid DocType, '"
			+ "WHERE (C_DocType_ID IS NULL OR C_DocType_ID=0"
			+ " OR NOT EXISTS (SELECT * FROM C_DocType d WHERE i.AD_Client_ID=d.AD_Client_ID AND d.DocBaseType='GLJ'))"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid DocType=" + no);

		//	GL Category
		sql = "UPDATE I_GLJournal i "
			+ "SET GL_Category_ID=(SELECT c.GL_Category_ID FROM GL_Category c"
			+ " WHERE c.Name=i.CategoryName AND i.AD_Client_ID=c.AD_Client_ID) "
			+ "WHERE GL_Category_ID IS NULL AND CategoryName IS NOT NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set DocType=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Category, '"
			+ "WHERE (GL_Category_ID IS NULL OR GL_Category_ID=0)"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Category=" + no);

		//	Set Currency
		sql = "UPDATE I_GLJournal i "
			+ "SET C_Currency_ID=(SELECT c.C_Currency_ID FROM C_Currency c"
			+ " WHERE c.ISO_Code=i.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_Currency_ID IS NULL AND ISO_Code IS NOT NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Currency from ISO=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET C_Currency_ID=(SELECT a.C_Currency_ID FROM C_AcctSchema a"
			+ " WHERE a.C_AcctSchema_ID=i.C_AcctSchema_ID AND a.AD_Client_ID=i.AD_Client_ID)"
			+ "WHERE C_Currency_ID IS NULL AND ISO_Code IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Default Currency=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Currency, '"
			+ "WHERE (C_Currency_ID IS NULL OR C_Currency_ID=0)"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Currency=" + no);

		//	Set Conversion Type
		sql = "UPDATE I_GLJournal i " 
			+ "SET ConversionTypeValue='S' "
			+ "WHERE C_ConversionType_ID IS NULL AND ConversionTypeValue IS NULL"
			+ " AND I_IsImported='N'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set CurrencyType Value to Spot =" + no);
		sql = "UPDATE I_GLJournal i " 
			+ "SET C_ConversionType_ID=(SELECT c.C_ConversionType_ID FROM C_ConversionType c"
			+ " WHERE c.Value=i.ConversionTypeValue AND c.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_ConversionType_ID IS NULL AND ConversionTypeValue IS NOT NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set CurrencyType from Value=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid CurrencyType, '"
			+ "WHERE (C_ConversionType_ID IS NULL OR C_ConversionType_ID=0) AND ConversionTypeValue IS NOT NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid CurrencyTypeValue=" + no);


		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No ConversionType, '"
			+ "WHERE (C_ConversionType_ID IS NULL OR C_ConversionType_ID=0)"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("No CourrencyType=" + no);

		//	Set/Overwrite Home Currency Rate
		sql = "UPDATE I_GLJournal i "
			+ "SET CurrencyRate=1"
			+ "WHERE EXISTS (SELECT * FROM C_AcctSchema a"
			+ " WHERE a.C_AcctSchema_ID=i.C_AcctSchema_ID AND a.C_Currency_ID=i.C_Currency_ID)"
			+ " AND C_Currency_ID IS NOT NULL AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Home CurrencyRate=" + no);
		//	Set Currency Rate
		sql = "UPDATE I_GLJournal i "
		   + "SET CurrencyRate=(SELECT MAX(r.MultiplyRate) FROM C_Conversion_Rate r, C_AcctSchema s"
		   + " WHERE s.C_AcctSchema_ID=i.C_AcctSchema_ID AND s.AD_Client_ID=i.AD_Client_ID"
		   + " AND r.C_Currency_ID=i.C_Currency_ID AND r.C_Currency_To_ID=s.C_Currency_ID"
		   + " AND r.AD_Client_ID=i.AD_Client_ID AND r.AD_Org_ID=i.AD_OrgDoc_ID"
		   + " AND r.C_ConversionType_ID=i.C_ConversionType_ID"
		   + " AND i.DateAcct BETWEEN r.ValidFrom AND r.ValidTo "
	   //	ORDER BY ValidFrom DESC
		   + ") WHERE CurrencyRate IS NULL OR CurrencyRate=0 AND C_Currency_ID>0"
		   + " AND I_IsImported<>'Y'"
		   + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Org Rate=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET CurrencyRate=(SELECT MAX(r.MultiplyRate) FROM C_Conversion_Rate r, C_AcctSchema s"
			+ " WHERE s.C_AcctSchema_ID=i.C_AcctSchema_ID AND s.AD_Client_ID=i.AD_Client_ID"
			+ " AND r.C_Currency_ID=i.C_Currency_ID AND r.C_Currency_To_ID=s.C_Currency_ID"
			+ " AND r.AD_Client_ID=i.AD_Client_ID"
			+ " AND r.C_ConversionType_ID=i.C_ConversionType_ID"
			+ " AND i.DateAcct BETWEEN r.ValidFrom AND r.ValidTo "
		//	ORDER BY ValidFrom DESC
			+ ") WHERE CurrencyRate IS NULL OR CurrencyRate=0 AND C_Currency_ID>0"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Client Rate=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No Rate, '"
			+ "WHERE CurrencyRate IS NULL OR CurrencyRate=0"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("No Rate=" + no);

		//	Set Period
		sql = "UPDATE I_GLJournal i "
			+ "SET C_Period_ID=(SELECT MAX(p.C_Period_ID) FROM C_Period p"
			+ " INNER JOIN C_Year y ON (y.C_Year_ID=p.C_Year_ID)"
			+ " INNER JOIN AD_ClientInfo c ON (c.C_Calendar_ID=y.C_Calendar_ID)"
			+ " WHERE c.AD_Client_ID=i.AD_Client_ID"
			+ " AND i.DateAcct BETWEEN p.StartDate AND p.EndDate AND p.PeriodType='S') "
			+ "WHERE C_Period_ID IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Period=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Period, '"
			+ "WHERE C_Period_ID IS NULL OR C_Period_ID NOt IN "
			+ "(SELECT C_Period_ID FROM C_Period p"
			+ " INNER JOIN C_Year y ON (y.C_Year_ID=p.C_Year_ID)"
			+ " INNER JOIN AD_ClientInfo c ON (c.C_Calendar_ID=y.C_Calendar_ID) "
			+ " WHERE c.AD_Client_ID=i.AD_Client_ID"
			+ " AND i.DateAcct BETWEEN p.StartDate AND p.EndDate AND p.PeriodType='S')"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Period=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_ErrorMsg="+ts +"||'WARN=Period Closed, ' "
			+ "WHERE C_Period_ID IS NOT NULL AND NOT EXISTS"
			+ " (SELECT * FROM C_PeriodControl pc WHERE pc.C_Period_ID=i.C_Period_ID AND DocBaseType='GLJ' AND PeriodStatus='O') "
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Period Closed=" + no);

		//	Budget Name
		sql = "UPDATE I_GLJournal i "
			+ "SET GL_Budget_ID= "
			+ " (SELECT GL_Budget_ID FROM GL_Budget b WHERE i.BudgetName=b.Name)"
			+ " WHERE GL_Budget_ID IS NULL AND BudgetName is NOT NULL "
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Budget =" + no);

		//	Posting Type
		sql = "UPDATE I_GLJournal i "
			+ "SET PostingType='B' "
			+ "WHERE PostingType IS NULL AND GL_Budget_ID IS NOT NULL AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Budget PostingType=" + no);

		sql = "UPDATE I_GLJournal i "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Budget specified for Actual Journal, ' "
				+ " WHERE PostingType='A' AND GL_Budget_ID IS NOT NULL"
				+ " AND I_IsImported<>'Y'"
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
			if (no != 0)
				log.warning ("Budget specified for Actual Journal =" + no);

/*			//	Posting Type
		sql = "UPDATE I_GLJournal i "
			+ "SET PostingType='A' "
			+ "WHERE PostingType IS NULL AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Actual PostingType=" + no);*/
		
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid PostingType, ' "
			+ "WHERE PostingType IS NULL OR NOT EXISTS"
			+ " (SELECT * FROM AD_Ref_List r WHERE r.AD_Reference_ID=125 AND i.PostingType=r.Value)"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid PostingTypee=" + no);


		//	** Account Elements (optional) **
		//	(C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0)

		//	Set Org from Name (* is overwritten and default)
		sql = "UPDATE I_GLJournal i "
			+ "SET AD_Org_ID=(SELECT o.AD_Org_ID FROM AD_Org o"
			+ " WHERE o.Value=i.OrgValue AND o.IsSummary='N' AND i.AD_Client_ID=o.AD_Client_ID) "
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0) AND OrgValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'";
		no = DB.executeUpdate(get_Trx(),sql);
		log.fine("Set Org from Value=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET AD_Org_ID=AD_OrgDoc_ID "
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0) AND OrgValue IS NULL AND AD_OrgDoc_ID IS NOT NULL AND AD_OrgDoc_ID<>0"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Org from Doc Org=" + no);
		//	Error Org
		sql = "UPDATE I_GLJournal o "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Org, '"
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Org=" + no);

		//	Set Account
		sql = "UPDATE I_GLJournal i "
			+ "SET Account_ID=(SELECT MAX(ev.C_ElementValue_ID) FROM C_ElementValue ev"
			+ " INNER JOIN C_Element e ON (e.C_Element_ID=ev.C_Element_ID)"
			+ " INNER JOIN C_AcctSchema_Element ase ON (e.C_Element_ID=ase.C_Element_ID AND ase.ElementType='AC')"
			+ " WHERE ev.Value=i.AccountValue AND ev.IsSummary='N'"
			+ " AND i.C_AcctSchema_ID=ase.C_AcctSchema_ID AND i.AD_Client_ID=ev.AD_Client_ID) "
			+ "WHERE Account_ID IS NULL AND AccountValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Account from Value=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Account, '"
			+ "WHERE (Account_ID IS NULL OR Account_ID=0)"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Account=" + no);

		//	Set BPartner
		sql = "UPDATE I_GLJournal i "
			+ "SET C_BPartner_ID=(SELECT bp.C_BPartner_ID FROM C_BPartner bp"
			+ " WHERE bp.Value=i.BPartnerValue AND bp.IsSummary='N' AND i.AD_Client_ID=bp.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set BPartner from Value=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid BPartner, '"
			+ "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid BPartner=" + no);

		//	Set Product
		sql = "UPDATE I_GLJournal i "
			+ "SET M_Product_ID=(SELECT MAX(p.M_Product_ID) FROM M_Product p"
			+ " WHERE (p.Value=i.ProductValue OR p.UPC=i.UPC OR p.SKU=i.SKU)"
			+ " AND p.IsSummary='N' AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE M_Product_ID IS NULL AND (ProductValue IS NOT NULL OR UPC IS NOT NULL OR SKU IS NOT NULL)"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Product from Value=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Product, '"
			+ "WHERE M_Product_ID IS NULL AND (ProductValue IS NOT NULL OR UPC IS NOT NULL OR SKU IS NOT NULL)"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Product=" + no);

		//	Set Project
		sql = "UPDATE I_GLJournal i "
			+ "SET C_Project_ID=(SELECT p.C_Project_ID FROM C_Project p"
			+ " WHERE p.Value=i.ProjectValue AND p.IsSummary='N' AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE C_Project_ID IS NULL AND ProjectValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set Project from Value=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Project, '"
			+ "WHERE C_Project_ID IS NULL AND ProjectValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Project=" + no);

		//	Set TrxOrg
		sql = "UPDATE I_GLJournal i "
			+ "SET AD_OrgTrx_ID=(SELECT o.AD_Org_ID FROM AD_Org o"
			+ " WHERE o.Value=i.OrgValue AND o.IsSummary='N' AND i.AD_Client_ID=o.AD_Client_ID) "
			+ "WHERE AD_OrgTrx_ID IS NULL AND OrgTrxValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set OrgTrx from Value=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid OrgTrx, '"
			+ "WHERE AD_OrgTrx_ID IS NULL AND OrgTrxValue IS NOT NULL"
			+ " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid OrgTrx=" + no);


		//	Source Amounts
		sql = "UPDATE I_GLJournal "
			+ "SET AmtSourceDr = 0 "
			+ "WHERE AmtSourceDr IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set 0 Source Dr=" + no);
		sql = "UPDATE I_GLJournal "
			+ "SET AmtSourceCr = 0 "
			+ "WHERE AmtSourceCr IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Set 0 Source Cr=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_ErrorMsg="+ts +"||'WARN=Zero Source Balance, ' "
			+ "WHERE (AmtSourceDr-AmtSourceCr)=0"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Zero Source Balance=" + no);

		//	Accounted Amounts (Only if No Error)
		sql = "UPDATE I_GLJournal "
			+ "SET AmtAcctDr = ROUND(AmtSourceDr * CurrencyRate, 2) "	//	HARDCODED rounding
			+ "WHERE AmtAcctDr IS NULL OR AmtAcctDr=0"
			+ " AND I_IsImported='N'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Calculate Acct Dr=" + no);
		sql = "UPDATE I_GLJournal "
			+ "SET AmtAcctCr = ROUND(AmtSourceCr * CurrencyRate, 2) "
			+ "WHERE AmtAcctCr IS NULL OR AmtAcctCr=0"
			+ " AND I_IsImported='N'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		log.fine("Calculate Acct Cr=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_ErrorMsg="+ts +"||'WARN=Zero Acct Balance, ' "
			+ "WHERE (AmtSourceDr-AmtSourceCr)<>0 AND (AmtAcctDr-AmtAcctCr)=0"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Zero Acct Balance=" + no);
		sql = "UPDATE I_GLJournal i "
			+ "SET I_ErrorMsg="+ts +"||'WARN=Check Acct Balance, ' "
			+ "WHERE ABS(AmtAcctDr-AmtAcctCr)>100000000"	//	100 mio
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Chack Acct Balance=" + no);


		/*********************************************************************/

		//	Get Balance
		sql = "SELECT SUM(AmtSourceDr)-SUM(AmtSourceCr), SUM(AmtAcctDr)-SUM(AmtAcctCr) "
			+ "FROM I_GLJournal "
			+ "WHERE I_IsImported='N'"
			+ STD_CLIENT_CHECK;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			 pstmt.setInt(1, m_AD_Client_ID);
			 rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				BigDecimal source = rs.getBigDecimal(1);
				BigDecimal acct = rs.getBigDecimal(2);
				if (source != null && source.signum() == 0
					&& acct != null && acct.signum() == 0)
					log.info ("Import Balance = 0");
				else
					log.warning("Balance Source=" + source  + ", Acct=" + acct);
				if (source != null)
					addLog (0, null, source, "@AmtSourceDr@ - @AmtSourceCr@");
				if (acct != null)
					addLog (0, null, acct, "@AmtAcctDr@ - @AmtAcctCr@");
			}
		
		
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
	
		}

	
		//	Count Errors
		int errors = QueryUtil.getSQLValue(get_Trx(), 
			"SELECT COUNT(*) FROM I_GLJournal WHERE I_IsImported NOT IN ('Y','N')" + STD_CLIENT_CHECK,m_AD_Client_ID);

		commit();

		if (errors != 0)
		{
			if (m_IsValidateOnly || m_IsImportOnlyNoErrors)
				throw new Exception ("@Errors@=" + errors);
		}
		else if (m_IsValidateOnly)
			return "@Errors@=" + errors;

		log.info("Validation Errors=" + errors);
		
		/*********************************************************************/
		//	Set Error to indicator to not imported
		String errorIndicatorSql = "UPDATE I_GLJournal "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;

		int noInsert = 0;
		int noInsertJournal = 0;
		int noInsertLine = 0;

		MJournalBatch batch = null;		//	Change Batch per Batch DocumentNo
		String BatchDocumentNo = "";
		MJournal journal = null;
		String JournalDocumentNo = "";
		Timestamp DateAcct = null;
		
		Map<Integer, X_I_GLJournal> importGLJournalBatchMap = new HashMap<Integer, X_I_GLJournal>();
		
		Map <MJournalBatch, List<MJournal>> journalBatchMap = new HashMap <MJournalBatch, List<MJournal>> ();

		Map <MJournal, List<MJournalLine>> journalMap = new HashMap <MJournal, List<MJournalLine>> ();
		
		List<MJournal> journals = null;
		List<MJournalLine> journalLines = null;

		//	Go through Journal Records
		sql = "SELECT * FROM I_GLJournal "
			+ "WHERE I_IsImported='N'"+ STD_CLIENT_CHECK
			+ " ORDER BY COALESCE(BatchDocumentNo, TO_NCHAR(I_GLJournal_ID)),"
			+ " COALESCE(JournalDocumentNo, TO_NCHAR(I_GLJournal_ID)), C_AcctSchema_ID," 
			+ "PostingType, C_DocType_ID, GL_Category_ID, C_Currency_ID, TRUNC(DateAcct,'DD')," 
			+ " Line, I_GLJournal_ID";
		try
		{
			pstmt = DB.prepareStatement (sql.toString (), get_Trx());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery ();
			//
			while (rs.next())
			{
				
				
				X_I_GLJournal imp = new X_I_GLJournal (getCtx (), rs, get_Trx());
			
				
				//	New Batch if Batch Document No changes
				String impBatchDocumentNo = imp.getBatchDocumentNo();
				if (impBatchDocumentNo == null)
					impBatchDocumentNo = "";
				if (batch == null
					|| imp.isCreateNewBatch()
					|| journal.getC_AcctSchema_ID() != imp.getC_AcctSchema_ID()
					|| !BatchDocumentNo.equals(impBatchDocumentNo))
				{
					if(journalBatchMap.size() >= COMMITCOUNT) {
						saveJournals(importGLJournalBatchMap,journalBatchMap,journalMap);
						importGLJournalBatchMap.clear();
						journalBatchMap.clear();
						journalMap.clear();
						journals.clear();
						journalLines.clear();
					}
					
					BatchDocumentNo = impBatchDocumentNo;	//	cannot compare real DocumentNo
					batch = new MJournalBatch (imp);
								
				
					
					String description = imp.getBatchDescription();
					if (description == null || description.length() == 0)
						description = "*Import-";
					else
						description += " *Import-";
					description += new Timestamp(System.currentTimeMillis());
					batch.setDescription(description);
					
				
				
					journals = new ArrayList<MJournal>();
					journalBatchMap.put(batch,journals);
					
										
					noInsert++;
					journal = null;
				}
				importGLJournalBatchMap.put(imp.getI_GLJournal_ID(), imp);
				//	Journal
				String impJournalDocumentNo = imp.getJournalDocumentNo();
				if (impJournalDocumentNo == null)
					impJournalDocumentNo = "";
				Timestamp impDateAcct = TimeUtil.getDay(imp.getDateAcct());
				if (journal == null
					|| imp.isCreateNewJournal()
					|| !JournalDocumentNo.equals(impJournalDocumentNo)
					|| journal.getC_DocType_ID() != imp.getC_DocType_ID()
					|| journal.getGL_Category_ID() != imp.getGL_Category_ID()
					|| !journal.getPostingType().equals(imp.getPostingType())
					|| journal.getC_Currency_ID() != imp.getC_Currency_ID()
					|| !impDateAcct.equals(DateAcct)
				)
				{
				
					JournalDocumentNo = impJournalDocumentNo;	//	cannot compare real DocumentNo
					DateAcct = impDateAcct;
					journal = new MJournal (imp);

					String description = imp.getBatchDescription();
					if (description == null || description.length() == 0)
						description = "(Import)";
					journal.setDescription (description);
					
						
					journalLines = new ArrayList<MJournalLine>();
					journalMap.put(journal, journalLines);
					
					journals.add(journal);
					
					noInsertJournal++;
				}

				//	Lines
				MJournalLine line = new MJournalLine (imp);

				//	Set/Get Account Combination
				if (imp.getC_ValidCombination_ID() == 0)
				{
					MAccount acct = MAccount.get(getCtx(), imp.getAD_Client_ID(), imp.getAD_Org_ID(), 
						imp.getC_AcctSchema_ID(), imp.getAccount_ID(), 0,
						imp.getM_Product_ID(), imp.getC_BPartner_ID(), imp.getAD_OrgTrx_ID(),
						imp.getC_LocFrom_ID(), imp.getC_LocTo_ID(), imp.getC_SalesRegion_ID(),
						imp.getC_Project_ID(), imp.getC_Campaign_ID(), imp.getC_Activity_ID(),
						imp.getUser1_ID(), imp.getUser2_ID(), 0, 0);
					if (acct != null && acct.get_ID() == 0)
						acct.save();
					if (acct == null || acct.get_ID() == 0)
					{
						imp.setI_ErrorMsg("ERROR creating Account");
						imp.setI_IsImported(X_I_GLJournal.I_ISIMPORTED_No);
						imp.save();
						continue;
					}
					else
					{
						line.setC_ValidCombination_ID(acct.get_ID());
						imp.setC_ValidCombination_ID(acct.get_ID());
					}
				}
				else
					line.setC_ValidCombination_ID (imp.getC_ValidCombination_ID());
				

				journalLines.add(line);
				
				noInsertLine++;
				
				
			}	//	while records
			
			saveJournals(importGLJournalBatchMap,journalBatchMap,journalMap);

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "GLJournals - "+sql, e);
			noInsert=0;
			noInsertJournal=0;;
			noInsertLine=0;
			no = DB.executeUpdate(get_Trx(),errorIndicatorSql,m_AD_Client_ID);
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@GL_JournalBatch_ID@: @Inserted@");
			addLog (0, null, new BigDecimal (noInsertJournal), "@GL_Journal_ID@: @Inserted@");
			addLog (0, null, new BigDecimal (noInsertLine), "@GL_JournalLine_ID@: @Inserted@");
			throw new CompiereSQLException();

		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);

		}
	
		no = DB.executeUpdate(get_Trx(),errorIndicatorSql,m_AD_Client_ID);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@GL_JournalBatch_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertJournal), "@GL_Journal_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertLine), "@GL_JournalLine_ID@: @Inserted@");
		return "";
	}	//	doIt
	
	

	public void saveJournals(Map<Integer, X_I_GLJournal> importGLJournalBatchMap,
							Map <MJournalBatch, List<MJournal>>	journalBatchMap,
							Map <MJournal, List<MJournalLine>>	journalMap)
	{
		
		if(journalBatchMap.isEmpty())
			return;
		
		//Save journal batch
		List<MJournalBatch> journalBatchToSave = new ArrayList<MJournalBatch>(journalBatchMap.keySet());
		if(!PO.saveAll(get_Trx(), journalBatchToSave)) 
			throw new CompiereStateException("Could not save Journal Batch");
		
		//Save Journal
		List<MJournal> journalsToSave = new ArrayList<MJournal>();
		for(Map.Entry<MJournalBatch, List<MJournal>> entry : journalBatchMap.entrySet()) {
			MJournalBatch journalBatch = entry.getKey();
			for(MJournal journal : entry.getValue()) {
				journal.setGL_JournalBatch_ID (journalBatch.getGL_JournalBatch_ID());
				journalsToSave.add(journal);
			}
		}
		
		if(!PO.saveAll(get_Trx(), journalsToSave)) 
			throw new CompiereStateException("Could not save Journals");
		
		journalsToSave.clear();		
		BigDecimal totAmtCr = Env.ZERO;
		BigDecimal totAmtDr = Env.ZERO;
		
		//Save Journal line
		List<MJournalLine> journalLinesToSave = new ArrayList<MJournalLine>();
		for(Map.Entry<MJournal, List<MJournalLine>> entry : journalMap.entrySet()) {
			MJournal journal = entry.getKey();
			for(MJournalLine journalline : entry.getValue()) {				
				journalline.setGL_Journal_ID (journal.getGL_Journal_ID());
				journalLinesToSave.add(journalline);
				totAmtCr  = totAmtCr.add(journalline.getAmtSourceCr());
				totAmtDr  = totAmtDr.add(journalline.getAmtSourceDr());				

			}
			journal.setTotalCr(totAmtCr);
			journal.setTotalDr(totAmtDr);
			journalsToSave.add(journal);
			totAmtCr = Env.ZERO;
			totAmtDr = Env.ZERO;

		}
		
		if(!PO.saveAll(get_Trx(), journalLinesToSave)) 
			throw new CompiereStateException("Could not save JournalLines");
		
		
		if(!PO.saveAll(get_Trx(), journalsToSave)) 
			throw new CompiereStateException("Could not save Journals TotalCr & TotalDr");
		
		
		//Updating JournalBatch TotalCr and TotalDr from Journals
		journalBatchToSave.clear();
			 
			for(Map.Entry<MJournalBatch, List<MJournal>> entry : journalBatchMap.entrySet()) {
				MJournalBatch journalBatch = entry.getKey();
				for(MJournal journal: entry.getValue()) {
					totAmtCr  = totAmtCr.add(journal.getTotalCr());
					totAmtDr  = totAmtDr.add(journal.getTotalDr());				
				}
				journalBatch.setTotalCr(totAmtCr);
				journalBatch.setTotalDr(totAmtDr);
				journalBatchToSave.add(journalBatch);
				totAmtCr = Env.ZERO;
				totAmtDr = Env.ZERO;

			}
		 
			if(!PO.saveAll(get_Trx(), journalBatchToSave)) 
				throw new CompiereStateException("Could not save JournalBatches TotalCr & TotalDr");
 
		//Stamping the import records with batch id, journal id and journal line id
		
		List<X_I_GLJournal> importJournalsToSave = new ArrayList<X_I_GLJournal>();
	
		
		for(Map.Entry<MJournal, List<MJournalLine>> entry : journalMap.entrySet()) {
			MJournal journal = entry.getKey();
			for(MJournalLine journalline : entry.getValue()) {				
				X_I_GLJournal imp = importGLJournalBatchMap.get(journalline.getI_GLJournal_ID());
				if(imp != null) 
				{
					imp.setGL_JournalBatch_ID(journal.getGL_JournalBatch_ID());
					imp.setGL_Journal_ID(journalline.getGL_Journal_ID());
					imp.setGL_JournalLine_ID(journalline.getGL_JournalLine_ID());	
					imp.setI_IsImported(X_I_GLJournal.I_ISIMPORTED_Yes);
					imp.setProcessed(true);
					importJournalsToSave.add(imp);
				}
			}
		}
		
				
		if(!PO.saveAll (get_Trx(), importJournalsToSave))
			throw new CompiereStateException("Could not save Import journal records");

			commit();	

		
	}
	
	

	public static void main(String[] args)
	{
		System.setProperty ("PropertyFile", "C:/Compiere.properties");
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
		CLogMgt.setLoggerLevel(Level.FINE, null);
		CLogMgt.setLevel(Level.FINE);

		//	Data from Login Context
		int AD_Client_ID = ctx.getAD_Client_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		//	Hardcoded
		int AD_Process_ID = 220;
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
		ImportGLJournal test = new ImportGLJournal();
		test.m_AD_Client_ID = ctx.getAD_Client_ID();
		test.m_AD_Org_ID=ctx.getAD_Org_ID();
		test.m_C_AcctSchema_ID=101;
		test.m_IsValidateOnly=false;
		test.m_IsImportOnlyNoErrors=true;
		test.m_DateAcct= new java.sql.Timestamp(new java.util.Date().getTime());
		
		
		test.m_DeleteOldImported = true;
		
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

}	//	ImportGLJournal
