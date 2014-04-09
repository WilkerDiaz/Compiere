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

import java.io.*;
import java.math.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;

import com.lowagie.text.pdf.PdfContentByte;

/**
 *	Dunning Letter Print
 *	
 *  @author Jorg Janke
 *  @version $Id: DunningPrint.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class DunningPrint extends SvrProcess
{
	/**	Mail PDF				*/
	private boolean		p_EMailPDF = false;
	/** Mail Template			*/
	private int			p_R_MailText_ID = 0;
	/** Dunning Run				*/
	private int			p_C_DunningRun_ID = 0;
	/** Print only Outstanding	*/
	private boolean		p_IsOnlyIfBPBalance = true;
	private boolean 	m_consolidateInvoices = false;
	
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
			else if (name.equals("EMailPDF"))
				p_EMailPDF = "Y".equals(element.getParameter());
			else if (name.equals("R_MailText_ID"))
				p_R_MailText_ID = element.getParameterAsInt();
			else if (name.equals("C_DunningRun_ID"))
				p_C_DunningRun_ID = element.getParameterAsInt();
			else if (name.equals("IsOnlyIfBPBalance"))
				p_IsOnlyIfBPBalance = "Y".equals(element.getParameter());
			else if (name.equals("ConsolidateInvoices"))
				m_consolidateInvoices = "Y".equals(element.getParameter());	
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
		log.info("C_DunningRun_ID=" + p_C_DunningRun_ID + ",R_MailText_ID=" + p_R_MailText_ID 
			+ ", EmailPDF=" + p_EMailPDF + ",IsOnlyIfBPBalance=" + p_IsOnlyIfBPBalance);
		
		//	Need to have Template
		if (p_EMailPDF && p_R_MailText_ID == 0)
			throw new CompiereUserException ("@NotFound@: @R_MailText_ID@");
		MMailText mText = null;
		if (p_EMailPDF)
		{
			mText = new MMailText (getCtx(), p_R_MailText_ID, get_TrxName());
			if (p_EMailPDF && mText.get_ID() == 0)
				throw new CompiereUserException ("@NotFound@: @R_MailText_ID@ - " + p_R_MailText_ID);
			mText.getMailHeader();
		}
		//
		File f = null;
		com.lowagie.text.Document doc=null;
		PdfContentByte cb=null;
		
		if (m_consolidateInvoices){
			f = File.createTempFile( "U" + getCtx().getAD_User_ID() + "_R", ".pdf", new File( System.getProperty( "java.io.tmpdir" ) ) );
			doc=ReportEngine.createDocument();
			cb=ReportEngine.getContentByte(doc, f);			
		}
		MDunningRun run = new MDunningRun (getCtx(), p_C_DunningRun_ID, get_TrxName());
		if (run.get_ID() == 0)
			throw new CompiereUserException ("@NotFound@: @C_DunningRun_ID@ - " + p_C_DunningRun_ID);
		//	Print Format on Dunning Level
		MDunningLevel level = new MDunningLevel (getCtx(), run.getC_DunningLevel_ID(), get_TrxName());
		MPrintFormat format = MPrintFormat.get (getCtx(), level.getDunning_PrintFormat_ID(), false);
		
		MClient client = MClient.get(getCtx());
		
		int count = 0;
		int errors = 0;
		MDunningRunEntry[] entries = run.getEntries(false);
		for (MDunningRunEntry entry : entries) {
			if (p_IsOnlyIfBPBalance && entry.getAmt().signum() <= 0)
				continue;
			//	To BPartner
			MBPartner bp = new MBPartner (getCtx(), entry.getC_BPartner_ID(), get_TrxName());
			if (bp.get_ID() == 0)
			{
				addLog (entry.get_ID(), null, null, "@NotFound@: @C_BPartner_ID@ " + entry.getC_BPartner_ID());
				errors++;
				continue;
			}
			//	To User
			MUser to = new MUser (getCtx(), entry.getAD_User_ID(), get_TrxName());
			if (p_EMailPDF)
			{
				if (to.get_ID() == 0)
				{
					addLog (entry.get_ID(), null, null, "@NotFound@: @AD_User_ID@ - " + bp.getName());
					errors++;
					continue;
				}
				else if (to.getEMail() == null || to.getEMail().length() == 0)
				{
					addLog (entry.get_ID(), null, null, "@NotFound@: @EMail@ - " + to.getName());
					errors++;
					continue;
				}
			}
			//	BP Language
			Language language = Language.getLoginLanguage();		//	Base Language
			String tableName = "C_Dunning_Header_v";
			if (client.isMultiLingualDocument())
			{
				tableName += "t";
				String AD_Language = bp.getAD_Language();
				if (AD_Language != null)
					language = Language.getLanguage(AD_Language);
			}
			format.setLanguage(language);
			format.setTranslationLanguage(language);
			//	query
			Query query = new Query(tableName);
			query.addRestriction("C_DunningRunEntry_ID", Query.EQUAL, 
				Integer.valueOf(entry.getC_DunningRunEntry_ID()));

			//	Engine
			PrintInfo info = new PrintInfo(
				bp.getName(),
				X_C_DunningRunEntry.Table_ID,
				entry.getC_DunningRunEntry_ID(),
				entry.getC_BPartner_ID());
			info.setDescription(bp.getName() + ", Amt=" + entry.getAmt());
			ReportEngine re = new ReportEngine(getCtx(), format, query, info);
			boolean printed = false;
			if (p_EMailPDF)
			{
				EMail email = client.createEMail(to.getEMail(), to.getName(), null, null);
				if (email == null || !email.isValid())
				{
					addLog (entry.get_ID(), null, null, 
						"@RequestActionEMailError@ Invalid EMail: " + to);
					errors++;
					continue;
				}
				mText.setUser(to);	//	variable context
				mText.setBPartner(bp);
				mText.setPO(entry);
				String message = mText.getMailText(true);
				if (mText.isHtml())
					email.setMessageHTML(mText.getMailHeader(), message);
				else
				{
					email.setSubject (mText.getMailHeader());
					email.setMessageText (message);
				}
				//
				File attachment = re.getPDF(File.createTempFile("Dunning", ".pdf"));
				log.fine(to + " - " + attachment);
				email.addAttachment(attachment);
				//
				String msg = email.send();
				MUserMail um = new MUserMail(mText, entry.getAD_User_ID(), email);
				um.save();
				if (msg.equals(EMail.SENT_OK))
				{
					addLog (entry.get_ID(), null, null,
						bp.getName() + " @RequestActionEMailOK@");
					count++;
					printed = true;
				}
				else
				{
					addLog (entry.get_ID(), null, null,
						bp.getName() + " @RequestActionEMailError@ " + msg);
					errors++;
				}
			}
			else
			{
				if (m_consolidateInvoices){
					re.consolidateInvoices(doc,cb);
				}
				else{
					re.print ();
				}
				count++;
				printed = true;
			}
			if (printed)
			{
				entry.setProcessed (true);
				entry.save ();
				dunningLevelConsequences(level, entry);
			}

		}	//	for all dunning letters
		if (errors == 0) 
		{
			run.setProcessed (true);
			run.save ();
		}
			
		if (p_EMailPDF)
			return "@Sent@=" + count + " - @Errors@=" + errors;
		if (m_consolidateInvoices){
			doc.close();
			return f.getName();
		}
		return "@Printed@=" + count;
	}	//	doIt
	
	/**
	 * 	Dunning Level Consequences
	 */
	private void dunningLevelConsequences (MDunningLevel level, MDunningRunEntry entry)
	{
		//	Update Business Partner based on Level
		if (level.isSetCreditStop() || level.isSetPaymentTerm ()) 
		{
			MBPartner thisBPartner = new MBPartner(getCtx(), entry.getC_BPartner_ID(), get_TrxName());
			if (level.isSetCreditStop ())
				thisBPartner.setSOCreditStatus (X_C_BPartner.SOCREDITSTATUS_CreditStop);
			if (level.isSetPaymentTerm() && level.getC_PaymentTerm_ID() != 0)
				thisBPartner.setC_PaymentTerm_ID (level.getC_PaymentTerm_ID ());
			thisBPartner.save ();
		}
		//	Update Invoices if not Statement (Statement is hardcoded -9999 see also MDunningLevel)
		if (!level.getDaysAfterDue ().equals (new BigDecimal(-9999)) && level.getInvoiceCollectionType() != null)
		{
			MDunningRunLine[] lines = entry.getLines();
			for (MDunningRunLine line : lines) {
				if (line.getC_Invoice_ID() != 0 && line.isActive())
				{
					MInvoice invoice = new MInvoice (getCtx(), line.getC_Invoice_ID(), get_TrxName());
					invoice.setInvoiceCollectionType (level.getInvoiceCollectionType());
					invoice.save ();
				}
			}
		}
	}	//	dunningLevelConsequences
	
	
}	//	DunningPrint
