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
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Invoice Not realized Gain & Loss.
 * 	The actual data shown is T_InvoiceGL_v
 *  @author Jorg Janke
 *  @version $Id: InvoiceNGL.java,v 1.3 2006/08/04 03:53:59 jjanke Exp $
 */
public class InvoiceNGL extends SvrProcess
{
	/**	Mandatory Acct Schema			*/
	private int				p_C_AcctSchema_ID = 0;
	/** Mandatory Conversion Type		*/
	private int				p_C_ConversionTypeReval_ID = 0;
	/** Revaluation Date				*/
	private Timestamp		p_DateReval = null;
	/** Only AP/AR Transactions			*/
	private String			p_APAR = "A";
	private static String	ONLY_AP = "P";
	private static String	ONLY_AR = "R";
	/** Report all Currencies			*/
	private boolean			p_IsAllCurrencies = false;
	/** Optional Invoice Currency		*/
	private int				p_C_Currency_ID = 0;
	/** GL Document Type				*/
	private int				p_C_DocTypeReval_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = element.getParameterAsInt();
			else if (name.equals("C_ConversionTypeReval_ID"))
				p_C_ConversionTypeReval_ID = element.getParameterAsInt();
			else if (name.equals("DateReval"))
				p_DateReval = (Timestamp)element.getParameter();
			else if (name.equals("APAR"))
				p_APAR = (String)element.getParameter();
			else if (name.equals("IsAllCurrencies"))
				p_IsAllCurrencies = "Y".equals(element.getParameter());
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = element.getParameterAsInt();
			else if (name.equals("C_DocTypeReval_ID"))
				p_C_DocTypeReval_ID = element.getParameterAsInt();
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		if (p_IsAllCurrencies)
			p_C_Currency_ID = 0;
		log.info("C_AcctSchema_ID=" + p_C_AcctSchema_ID 
			+ ",C_ConversionTypeReval_ID=" + p_C_ConversionTypeReval_ID
			+ ",DateReval=" + p_DateReval 
			+ ", APAR=" + p_APAR
			+ ", IsAllCurrencies=" + p_IsAllCurrencies
			+ ",C_Currency_ID=" + p_C_Currency_ID
			+ ", C_DocType_ID=" + p_C_DocTypeReval_ID);
		
		//	Parameter
		if (p_DateReval == null)
			p_DateReval = new Timestamp(System.currentTimeMillis());
		
		//	Delete - just to be sure
		String sql = "DELETE FROM T_InvoiceGL WHERE AD_PInstance_ID= ? ";
		int no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no > 0)
			log.info("Deleted #" + no);
		
		//	Insert Trx
		sql = "INSERT INTO T_InvoiceGL (AD_Client_ID, AD_Org_ID, IsActive, Created,CreatedBy, Updated,UpdatedBy,"
									+ " AD_PInstance_ID, C_Invoice_ID, GrandTotal, OpenAmt, "
									+ " Fact_Acct_ID, AmtSourceBalance, AmtAcctBalance, "
									+ " AmtRevalDr, AmtRevalCr, C_DocTypeReval_ID, IsAllCurrencies, "
									+ " DateReval, C_ConversionTypeReval_ID, AmtRevalDrDiff, AmtRevalCrDiff, APAR) "
			+ " SELECT i.AD_Client_ID, i.AD_Org_ID, i.IsActive, i.Created,i.CreatedBy, i.Updated,i.UpdatedBy,"
			       + " ? , i.C_Invoice_ID, i.GrandTotal, invoiceOpen(i.C_Invoice_ID, 0), "
			       + " fa.Fact_Acct_ID, fa.AmtSourceDr-fa.AmtSourceCr, fa.AmtAcctDr-fa.AmtAcctCr, " 
			       + " currencyConvert(fa.AmtSourceDr, i.C_Currency_ID, a.C_Currency_ID, "
			                       + " TRUNC(?,'DD'),? ,i.AD_Client_ID, i.AD_Org_ID), "
		           + " currencyConvert(fa.AmtSourceCr, i.C_Currency_ID, a.C_Currency_ID, "
		                           + " TRUNC(?,'DD'),? ,i.AD_Client_ID, i.AD_Org_ID),"
		           + "? ,?, TRUNC(?,'DD'),? , 0, 0, ? "
		    + " FROM C_Invoice_v1 i"
		    + " INNER JOIN Fact_Acct fa ON (fa.AD_Table_ID=318 AND fa.Record_ID=i.C_Invoice_ID"
			                            + " AND ((charAt(i.DocBaseType,3) = 'C' "
			                            + " AND (i.GrandTotal*-1=fa.AmtSourceDr "
			                                  + " OR i.GrandTotal*-1=fa.AmtSourceCr))"
		                                  + " OR (charAt(i.DocBaseType,3) != 'C' "
		                                    + " AND (i.GrandTotal=fa.AmtSourceDr "
		                                         + " OR i.GrandTotal=fa.AmtSourceCr))"
		                                      + "))" 
		    + " INNER JOIN C_AcctSchema a ON (fa.C_AcctSchema_ID=a.C_AcctSchema_ID) "
		    + " WHERE i.IsPaid='N'"
		    + " AND EXISTS (SELECT 1 "
		                + " FROM C_ElementValue ev "
		    	        + " WHERE ev.C_ElementValue_ID=fa.Account_ID "
		    	        + " AND (ev.AccountType='A' OR ev.AccountType='L'))"
		    + " AND fa.C_AcctSchema_ID= ? ";
		if (!p_IsAllCurrencies)
			sql += " AND i.C_Currency_ID<>a.C_Currency_ID";
		if (ONLY_AR.equals(p_APAR))
			sql += " AND i.IsSOTrx='Y'";
		else if (ONLY_AP.equals(p_APAR))
			sql += " AND i.IsSOTrx='N'";
		if (!p_IsAllCurrencies && p_C_Currency_ID != 0)
			sql += " AND i.C_Currency_ID= ? ";
		
		if (!p_IsAllCurrencies && p_C_Currency_ID != 0)		
			no = DB.executeUpdate(get_TrxName(), sql,
					getAD_PInstance_ID(),p_DateReval,p_C_ConversionTypeReval_ID,
					p_DateReval,p_C_ConversionTypeReval_ID,
					(p_C_DocTypeReval_ID==0 ? "NULL" : String.valueOf(p_C_DocTypeReval_ID)),
					(p_IsAllCurrencies ? "'Y'" : "'N'"),
					p_DateReval,p_C_ConversionTypeReval_ID,p_APAR,
					p_C_AcctSchema_ID,p_C_Currency_ID);
		else
			no = DB.executeUpdate(get_TrxName(), sql,
					getAD_PInstance_ID(),p_DateReval,p_C_ConversionTypeReval_ID,
					p_DateReval,p_C_ConversionTypeReval_ID,
					(p_C_DocTypeReval_ID==0 ? "NULL" : String.valueOf(p_C_DocTypeReval_ID)),
					(p_IsAllCurrencies ? "'Y'" : "'N'"),
					p_DateReval,p_C_ConversionTypeReval_ID,p_APAR,
					p_C_AcctSchema_ID);

		if (no != 0)
			log.info("Inserted #" + no);
		else if (CLogMgt.isLevelFiner())
			log.warning("Inserted #" + no + " - " + sql);
		else 
			log.warning("Inserted #" + no);

		//	Calculate Difference
		sql = "UPDATE T_InvoiceGL gl "
			+ "SET (AmtRevalDrDiff,AmtRevalCrDiff)="
				+ "(SELECT gl.AmtRevalDr-fa.AmtAcctDr, gl.AmtRevalCr-fa.AmtAcctCr "
				+ "FROM Fact_Acct fa "
				+ "WHERE gl.Fact_Acct_ID=fa.Fact_Acct_ID) "
			+ "WHERE AD_PInstance_ID= ? ";
		int noT = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (noT > 0)
			log.config("Difference #" + noT);
		
		//	Percentage
		sql = "UPDATE T_InvoiceGL SET PercentGL = 100 "
			+ "WHERE GrandTotal=OpenAmt AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no > 0)
			log.info("Not Paid #" + no);

		sql = "UPDATE T_InvoiceGL SET PercentGL = ROUND(OpenAmt*100/GrandTotal,6) "
			+ "WHERE GrandTotal<>OpenAmt AND GrandTotal <> 0 AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no > 0)
			log.info("Partial Paid #" + no);

		sql = "UPDATE T_InvoiceGL SET AmtRevalDr = AmtRevalDr * PercentGL/100,"
			+ " AmtRevalCr = AmtRevalCr * PercentGL/100,"
			+ " AmtRevalDrDiff = AmtRevalDrDiff * PercentGL/100,"
			+ " AmtRevalCrDiff = AmtRevalCrDiff * PercentGL/100 "
			+ "WHERE PercentGL <> 100 AND AD_PInstance_ID= ? " ;
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no > 0)
			log.config("Partial Calc #" + no);
		
		//	Create Document
		String info = "";
		if (p_C_DocTypeReval_ID != 0)
		{
			if (p_C_Currency_ID != 0)
				log.warning("Can create Journal only for all currencies");
			else
				info = createGLJournal();
		}
		return "#" + noT + info;
	}	//	doIt

	/**
	 * 	Create GL Journal
	 * 	@return document info
	 */
	private String createGLJournal()
	{
		ArrayList<X_T_InvoiceGL> list = new ArrayList<X_T_InvoiceGL>();
		String sql = "SELECT * FROM T_InvoiceGL "
			+ "WHERE AD_PInstance_ID= ? "
			+ " ORDER BY AD_Org_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt(1, getAD_PInstance_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new X_T_InvoiceGL (getCtx(), rs, get_TrxName()));
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (list.size() == 0)
			return " - No Records found";
		
		//
		MAcctSchema as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		MAcctSchemaDefault asDefaultAccts = MAcctSchemaDefault.get(getCtx(), p_C_AcctSchema_ID);
		MGLCategory cat = MGLCategory.getDefaultSystem(getCtx());
		if (cat == null)
		{
			MDocType docType = MDocType.get(getCtx(), p_C_DocTypeReval_ID);
			cat = MGLCategory.get(getCtx(), docType.getGL_Category_ID());
		}
		//
		MJournalBatch batch = new MJournalBatch(getCtx(), 0, get_TrxName());
		batch.setDescription (getName());
		batch.setC_DocType_ID(p_C_DocTypeReval_ID);
		batch.setGL_Category_ID(cat.getGL_Category_ID());
		batch.setDateDoc(new Timestamp(System.currentTimeMillis()));
		batch.setDateAcct(p_DateReval);
		batch.setC_Currency_ID(as.getC_Currency_ID());
		if (!batch.save())
			return " - Could not create Batch";
		//
		MJournal journal = null;
		BigDecimal drTotal = Env.ZERO;
		BigDecimal crTotal = Env.ZERO;
		int AD_Org_ID = 0;
		for (int i = 0; i < list.size(); i++)
		{
			X_T_InvoiceGL gl = list.get(i);
			if (gl.getAmtRevalDrDiff().signum() == 0 && gl.getAmtRevalCrDiff().signum() == 0)
				continue;
			MInvoice invoice = new MInvoice(getCtx(), gl.getC_Invoice_ID(), null);
			if (invoice.getC_Currency_ID() == as.getC_Currency_ID())
				continue;
			//
			if (journal == null)
			{
				journal = new MJournal (batch);
				journal.setC_AcctSchema_ID (as.getC_AcctSchema_ID());
				journal.setC_Currency_ID(as.getC_Currency_ID());
				journal.setC_ConversionType_ID(p_C_ConversionTypeReval_ID);
				MOrg org = MOrg.get(getCtx(), gl.getAD_Org_ID());
				journal.setDescription (getName() + " - " + org.getName());
				journal.setGL_Category_ID (cat.getGL_Category_ID());
				if (!journal.save())
					return " - Could not create Journal";
			}
			//
			MJournalLine line = new MJournalLine(journal);
			line.setLine((i+1) * 10);
			line.setDescription(invoice.getSummary());
			//
			MFactAcct fa = new MFactAcct (getCtx(), gl.getFact_Acct_ID(), null);
			line.setC_ValidCombination_ID(MAccount.get(fa));
			BigDecimal dr = gl.getAmtRevalDrDiff();
			BigDecimal cr = gl.getAmtRevalCrDiff();
			drTotal = drTotal.add(dr);
			crTotal = crTotal.add(cr);
			line.setAmtSourceDr (dr);
			line.setAmtAcctDr (dr);
			line.setAmtSourceCr (cr);
			line.setAmtAcctCr (cr);
			line.save();
			//
			if (AD_Org_ID == 0)		//	invoice org id
				AD_Org_ID = gl.getAD_Org_ID();
			//	Change in Org
			if (AD_Org_ID != gl.getAD_Org_ID())
			{
				createBalancing (asDefaultAccts, journal, drTotal, crTotal, AD_Org_ID, (i+1) * 10);
				//
				AD_Org_ID = gl.getAD_Org_ID();
				drTotal = Env.ZERO;
				crTotal = Env.ZERO;
				journal = null;
			}
		}
		createBalancing (asDefaultAccts, journal, drTotal, crTotal, AD_Org_ID, (list.size()+1) * 10);
		
		return " - " + batch.getDocumentNo() + " #" + list.size();
	}	//	createGLJournal

	/**
	 * 	Create Balancing Entry
	 *	@param asDefaultAccts acct schema default accounts
	 *	@param journal journal
	 *	@param drTotal dr
	 *	@param crTotal cr
	 *	@param AD_Org_ID org
	 *	@param lineNo base line no
	 */
	private void createBalancing (MAcctSchemaDefault asDefaultAccts, MJournal journal, 
		BigDecimal drTotal, BigDecimal crTotal, int AD_Org_ID, int lineNo)
	{
		if (journal == null)
			throw new IllegalArgumentException("Jornal is null");
		//		CR Entry = Gain
		if (drTotal.signum() != 0)
		{
			MJournalLine line = new MJournalLine(journal);
			line.setLine(lineNo+1);
			MAccount base = MAccount.get(getCtx(), asDefaultAccts.getUnrealizedGain_Acct());
			MAccount acct = MAccount.get(getCtx(), asDefaultAccts.getAD_Client_ID(), AD_Org_ID, 
				asDefaultAccts.getC_AcctSchema_ID(), base.getAccount_ID(), base.getC_SubAcct_ID(),
				base.getM_Product_ID(), base.getC_BPartner_ID(), base.getAD_OrgTrx_ID(), 
				base.getC_LocFrom_ID(), base.getC_LocTo_ID(), base.getC_SalesRegion_ID(), 
				base.getC_Project_ID(), base.getC_Campaign_ID(), base.getC_Activity_ID(),
				base.getUser1_ID(), base.getUser2_ID(), base.getUserElement1_ID(), base.getUserElement2_ID());
			line.setDescription(Msg.getElement(getCtx(), "UnrealizedGain_Acct"));
			line.setC_ValidCombination_ID(acct.getC_ValidCombination_ID());
			line.setAmtSourceCr (drTotal);
			line.setAmtAcctCr (drTotal);
			line.save();
		}
		//	DR Entry = Loss
		if (crTotal.signum() != 0)
		{
			MJournalLine line = new MJournalLine(journal);
			line.setLine(lineNo+2);
			MAccount base = MAccount.get(getCtx(), asDefaultAccts.getUnrealizedLoss_Acct());
			MAccount acct = MAccount.get(getCtx(), asDefaultAccts.getAD_Client_ID(), AD_Org_ID, 
				asDefaultAccts.getC_AcctSchema_ID(), base.getAccount_ID(), base.getC_SubAcct_ID(),
				base.getM_Product_ID(), base.getC_BPartner_ID(), base.getAD_OrgTrx_ID(), 
				base.getC_LocFrom_ID(), base.getC_LocTo_ID(), base.getC_SalesRegion_ID(), 
				base.getC_Project_ID(), base.getC_Campaign_ID(), base.getC_Activity_ID(),
				base.getUser1_ID(), base.getUser2_ID(), base.getUserElement1_ID(), base.getUserElement2_ID());
			line.setDescription(Msg.getElement(getCtx(), "UnrealizedLoss_Acct"));
			line.setC_ValidCombination_ID(acct.getC_ValidCombination_ID());
			line.setAmtSourceDr (crTotal);
			line.setAmtAcctDr (crTotal);
			line.save();
		}
	}	//	createBalancing

}	//	InvoiceNGL
