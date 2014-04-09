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
import java.sql.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;

import com.lowagie.text.pdf.PdfContentByte;

/**
 *	Print Invoices on Paperor send PDFs
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: InvoicePrint.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class InvoicePrint extends SvrProcess
{
	/**	Mail PDF			*/
	private boolean		p_EMailPDF = false;
	/** Mail Template		*/
	private int			p_R_MailText_ID = 0;
	
	private Timestamp	m_dateInvoiced_From = null;
	private Timestamp	m_dateInvoiced_To = null;
	private int			m_C_BPartner_ID = 0;
	private int			m_C_Invoice_ID = 0;
	private String		m_DocumentNo_From = null;
	private String		m_DocumentNo_To = null;
	private boolean		m_IsSOTrx = true;
	private boolean		m_IncludeDraftInvoices = true;
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
			else if (name.equals("DateInvoiced"))
			{
				m_dateInvoiced_From = ((Timestamp)element.getParameter());
				m_dateInvoiced_To = ((Timestamp)element.getParameter_To());
			}
			else if (name.equals("EMailPDF"))
				p_EMailPDF = "Y".equals(element.getParameter());
			else if (name.equals("R_MailText_ID"))
				p_R_MailText_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				m_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("C_Invoice_ID"))
				m_C_Invoice_ID = element.getParameterAsInt();
			else if (name.equals("DocumentNo"))
			{
				m_DocumentNo_From = (String)element.getParameter();
				m_DocumentNo_To = (String)element.getParameter_To();
			}
			else if (name.equals("IsSOTrx"))
				m_IsSOTrx = "Y".equals(element.getParameter());
			else if (name.equals("IncludeDraftInvoices"))
				m_IncludeDraftInvoices = "Y".equals(element.getParameter());
			else if (name.equals("ConsolidateInvoices"))
				m_consolidateInvoices = "Y".equals(element.getParameter());			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DocumentNo_From != null && m_DocumentNo_From.length() == 0)
			m_DocumentNo_From = null;
		if (m_DocumentNo_To != null && m_DocumentNo_To.length() == 0)
			m_DocumentNo_To = null;
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		//	Need to have Template
		if (p_EMailPDF && p_R_MailText_ID == 0)
			throw new CompiereUserException ("@NotFound@: @R_MailText_ID@");
		log.info ("C_BPartner_ID=" + m_C_BPartner_ID
			+ ", C_Invoice_ID=" + m_C_Invoice_ID
			+ ", IsSOTrx=" + m_IsSOTrx
			+ ", EmailPDF=" + p_EMailPDF + ",R_MailText_ID=" + p_R_MailText_ID
			+ ", DateInvoiced=" + m_dateInvoiced_From + "-" + m_dateInvoiced_To
			+ ", DocumentNo=" + m_DocumentNo_From + "-" + m_DocumentNo_To
			+ ", IncludeDrafts="+m_IncludeDraftInvoices);
		
		MMailText mText = null;
		if (p_R_MailText_ID != 0)
		{
			mText = new MMailText(getCtx(), p_R_MailText_ID, get_TrxName());
			if (mText.get_ID() != p_R_MailText_ID)
				throw new CompiereUserException ("@NotFound@: @R_MailText_ID@ - " + p_R_MailText_ID);
		}

		//	Too broad selection
		if (m_C_BPartner_ID == 0 && m_C_Invoice_ID == 0 && m_dateInvoiced_From == null && m_dateInvoiced_To == null
			&& m_DocumentNo_From == null && m_DocumentNo_To == null)
			throw new CompiereUserException ("@RestrictSelection@");

		MClient client = MClient.get(getCtx());
		
		File f = null;
		com.lowagie.text.Document doc=null;
		PdfContentByte cb=null;
		//	Get Info
		StringBuffer sql = new StringBuffer (
			"SELECT i.C_Invoice_ID,bp.AD_Language,c.IsMultiLingualDocument,"		//	1..3
			+ " COALESCE(bp.Invoice_PrintFormat_ID, dt.AD_PrintFormat_ID, pf.Invoice_PrintFormat_ID),"	//	4 
			+ " dt.DocumentCopies+bp.DocumentCopies,"								//	5
			+ " bpc.AD_User_ID, i.DocumentNo,"										//	6..7
			+ " bp.C_BPartner_ID "													//	8
			+ "FROM C_Invoice i"
			+ " INNER JOIN C_BPartner bp ON (i.C_BPartner_ID=bp.C_BPartner_ID)"
			+ " LEFT OUTER JOIN AD_User bpc ON (i.AD_User_ID=bpc.AD_User_ID)"
			+ " INNER JOIN AD_Client c ON (i.AD_Client_ID=c.AD_Client_ID)"
			+ " INNER JOIN AD_PrintForm pf ON (i.AD_Client_ID=pf.AD_Client_ID)"
			+ " INNER JOIN C_DocType dt ON (i.C_DocType_ID=dt.C_DocType_ID)")
			.append(" WHERE pf.AD_Org_ID IN (0,i.AD_Org_ID) AND ");	//	more them 1 PF
		if (m_C_Invoice_ID != 0)
			sql.append("i.C_Invoice_ID= ? ");
		else
		{
			if (m_IsSOTrx)
				sql.append ("i.IsSOTrx='Y'");
			else
				sql.append ("i.IsSOTrx='N'");
			
			if (!m_IncludeDraftInvoices)
				sql.append("AND i.DocStatus NOT IN ('DR')");
			
			if (m_C_BPartner_ID != 0)
				sql.append (" AND i.C_BPartner_ID= ? ");
			if (m_dateInvoiced_From != null && m_dateInvoiced_To != null)
				sql.append(" AND TRUNC(i.DateInvoiced,'DD') BETWEEN TRUNC(?,'DD') AND TRUNC(?,'DD')");
			else if (m_dateInvoiced_From != null)
				sql.append(" AND TRUNC(i.DateInvoiced,'DD') >= TRUNC(?,'DD') ");
			else if (m_dateInvoiced_To != null)
				sql.append(" AND TRUNC(i.DateInvoiced,'DD') <= TRUNC(?,'DD')");
			else if (m_DocumentNo_From != null && m_DocumentNo_To != null)
				sql.append(" AND i.DocumentNo BETWEEN ? AND ? ");
			else if (m_DocumentNo_From != null)
			{
				sql.append(" AND ");
				if (m_DocumentNo_From.indexOf('%') == -1)
					sql.append("i.DocumentNo >= ? ");
				else
					sql.append("i.DocumentNo LIKE ? ");
			}
		}
		sql.append(" ORDER BY i.C_Invoice_ID, pf.AD_Org_ID DESC");	//	more than 1 PF record
		log.finer(sql.toString());

		MPrintFormat format = null;
		int old_AD_PrintFormat_ID = -1;
		int old_C_Invoice_ID = -1;
		int C_BPartner_ID = 0;
		int count = 0;
		int errors = 0;
		
		if (m_consolidateInvoices){
			f = File.createTempFile( "U" + getCtx().getAD_User_ID() + "_R", ".pdf", new File( System.getProperty( "java.io.tmpdir" ) ) );
			doc=ReportEngine.createDocument();
			cb=ReportEngine.getContentByte(doc, f);			
		}
		PreparedStatement pstmt  = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			if(m_C_Invoice_ID != 0)
				pstmt.setInt(index++, m_C_Invoice_ID);
			else
			{
				if (m_C_BPartner_ID != 0)
					pstmt.setInt(index++, m_C_BPartner_ID);
				if (m_dateInvoiced_From != null && m_dateInvoiced_To != null)
				{
					pstmt.setTimestamp(index++, m_dateInvoiced_From);
					pstmt.setTimestamp(index++, m_dateInvoiced_To);
				}
				else if (m_dateInvoiced_From != null)
					pstmt.setTimestamp(index++, m_dateInvoiced_From);
				else if (m_dateInvoiced_To != null)
					pstmt.setTimestamp(index++, m_dateInvoiced_To);
				else if (m_DocumentNo_From != null && m_DocumentNo_To != null)
				{
					pstmt.setString(index++, m_DocumentNo_From);
					pstmt.setString(index++, m_DocumentNo_To);
				}
				else if (m_DocumentNo_From != null)
				{
					pstmt.setString(index++, m_DocumentNo_From);
				}
			}
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int C_Invoice_ID = rs.getInt(1);
				if (C_Invoice_ID == old_C_Invoice_ID)	//	multiple pf records
					continue;
				old_C_Invoice_ID = C_Invoice_ID;
				//	Set Language when enabled
				Language language = Language.getLoginLanguage();		//	Base Language
				String AD_Language = rs.getString(2);
				if (AD_Language != null && "Y".equals(rs.getString(3)))
					language = Language.getLanguage(AD_Language);
				//
				int AD_PrintFormat_ID = rs.getInt(4);
				int copies = rs.getInt(5);
				if (copies == 0)
					copies = 1;
				int AD_User_ID = rs.getInt(6);
				MUser to = new MUser (getCtx(), AD_User_ID, get_TrxName());
				String DocumentNo = rs.getString(7);
				C_BPartner_ID = rs.getInt(8);
				//
				String documentDir = client.getDocumentDir();
				if (documentDir == null || documentDir.length() == 0)
					documentDir = ".";
				//
				if (p_EMailPDF && (to.get_ID() == 0 || to.getEMail() == null || to.getEMail().length() == 0))
				{
					addLog (C_Invoice_ID, null, null, DocumentNo + " @RequestActionEMailNoTo@");
					errors++;
					continue;
				}
				if (AD_PrintFormat_ID == 0)
				{
					addLog (C_Invoice_ID, null, null, DocumentNo + " No Print Format");
					errors++;
					continue;
				}
				//	Get Format & Data
				if (AD_PrintFormat_ID != old_AD_PrintFormat_ID)
				{
					format = MPrintFormat.get (getCtx(), AD_PrintFormat_ID, false);
					old_AD_PrintFormat_ID = AD_PrintFormat_ID;
				}
				format.setLanguage(language);
				format.setTranslationLanguage(language);
				//	query
				Query query = new Query("C_Invoice_Header_v");
				query.addRestriction("C_Invoice_ID", Query.EQUAL, Integer.valueOf(C_Invoice_ID));

				//	Engine
				PrintInfo info = new PrintInfo(
					DocumentNo,
					X_C_Invoice.Table_ID,
					C_Invoice_ID,
					C_BPartner_ID);
				info.setCopies(copies);
				ReportEngine re = new ReportEngine(getCtx(), format, query, info);
				boolean printed = false;
				if (p_EMailPDF)
				{
					String subject = mText.getMailHeader() + " - " + DocumentNo;
					EMail email = client.createEMail(to.getEMail(), to.getName(), subject, null);
					if (email == null || !email.isValid())
					{
						addLog (C_Invoice_ID, null, null,
						  DocumentNo + " @RequestActionEMailError@ Invalid EMail: " + to);
						errors++;
						continue;
					}
					mText.setUser(to);					//	Context
					mText.setBPartner(C_BPartner_ID);	//	Context
					mText.setPO(new MInvoice(getCtx(), C_Invoice_ID, get_TrxName()));
					String message = mText.getMailText(true);
					if (mText.isHtml())
						email.setMessageHTML(subject, message);
					else
					{
						email.setSubject (subject);
						email.setMessageText (message);
					}
					//
					File invoice = null;
					if (!Ini.isClient())
						invoice = new File(MInvoice.getPDFFileName(documentDir, C_Invoice_ID));
					File attachment = re.getPDF(invoice);
					log.fine(to + " - " + attachment);
					email.addAttachment(attachment);
					//
					String msg = email.send();
					MUserMail um = new MUserMail(mText, getAD_User_ID(), email);
					um.save();
					if (msg.equals(EMail.SENT_OK))
					{
						addLog (C_Invoice_ID, null, null,
						  DocumentNo + " @RequestActionEMailOK@ - " + to.getEMail());
						count++;
						printed = true;
					}
					else
					{
						addLog (C_Invoice_ID, null, null,
						  DocumentNo + " @RequestActionEMailError@ " + msg
						  + " - " + to.getEMail());
						errors++;
					}
				}
				else
				{
					if (m_consolidateInvoices){
						re.consolidateInvoices(doc,cb);
					}
					else{
						re.print();
					}
					count++;
					printed = true;
				}
				//	Print Confirm
				if (printed)
				{
					StringBuffer sb = new StringBuffer ("UPDATE C_Invoice "
						+ "SET DatePrinted=SysDate, IsPrinted='Y' WHERE C_Invoice_ID= ?");
					DB.executeUpdate(get_TrxName(), sb.toString(),C_Invoice_ID);
				}
			}	//	for all entries
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			throw new Exception (e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		if (p_EMailPDF)
			return "@Sent@=" + count + " - @Errors@=" + errors;
		if (m_consolidateInvoices){
			doc.close();
			return f.getName();
		}
		return "@Printed@=" + count;
	}	//	doIt

}	//	InvoicePrint
