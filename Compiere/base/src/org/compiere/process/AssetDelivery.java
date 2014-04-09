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

import java.net.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Deliver Assets Electronically
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: AssetDelivery.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class AssetDelivery extends SvrProcess
{
	private MClient		m_client = null;

	private int			m_A_Asset_Group_ID = 0;
	private int			m_M_Product_ID = 0;
	private int			m_C_BPartner_ID = 0;
	private int			m_A_Asset_ID = 0;
	private Timestamp	m_GuaranteeDate = null;
	private int			m_NoGuarantee_MailText_ID = 0;
	private boolean		m_AttachAsset = false;
	//
	private MMailText	m_MailText = null;


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
			else if (name.equals("A_Asset_Group_ID"))
				m_A_Asset_Group_ID = element.getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				m_M_Product_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				m_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("A_Asset_ID"))
				m_A_Asset_ID = element.getParameterAsInt();
			else if (name.equals("GuaranteeDate"))
				m_GuaranteeDate = (Timestamp)element.getParameter();
			else if (name.equals("NoGuarantee_MailText_ID"))
				m_NoGuarantee_MailText_ID = element.getParameterAsInt();
			else if (name.equals("AttachAsset"))
				m_AttachAsset = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_GuaranteeDate == null)
			m_GuaranteeDate = new Timestamp (System.currentTimeMillis());
		//
		m_client = MClient.get(getCtx());
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		log.info("");
		long start = System.currentTimeMillis();

		//	Test
		if (m_client.getSmtpHost() == null || m_client.getSmtpHost().length() == 0)
			throw new Exception ("No Client SMTP Info");
		if (m_client.getRequestEMail() == null)
			throw new Exception ("No Client Request User");

		//	Asset selected
		if (m_A_Asset_ID != 0)
		{
			String msg = deliverIt (m_A_Asset_ID);
			addLog (m_A_Asset_ID, null, null, msg);
			return msg;
		}
		//
		StringBuffer sql = new StringBuffer ("SELECT A_Asset_ID, GuaranteeDate "
			+ "FROM A_Asset a"
			+ " INNER JOIN M_Product p ON (a.M_Product_ID=p.M_Product_ID) "
			+ "WHERE ");
		if (m_A_Asset_Group_ID != 0)
			sql.append("a.A_Asset_Group_ID= ? ").append(" AND ");
		if (m_M_Product_ID != 0)
			sql.append("p.M_Product_ID= ? ").append(" AND ");
		if (m_C_BPartner_ID != 0)
			sql.append("a.C_BPartner_ID= ? ").append(" AND ");
		String s = sql.toString();
		if (s.endsWith(" WHERE "))
			throw new Exception ("@RestrictSelection@");
		//	No mail to expired
		if (m_NoGuarantee_MailText_ID == 0)
		{
			sql.append("TRUNC(GuaranteeDate,'DD') >= TRUNC(?,'DD') ");
			s = sql.toString();
		}
		//	Clean up
		if (s.endsWith(" AND "))
			s = sql.substring(0, sql.length()-5);
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		int errors = 0;
		int reminders = 0;
		int index = 1;
		try
		{
			pstmt = DB.prepareStatement(s, get_Trx());
			if (m_A_Asset_Group_ID != 0)
				pstmt.setInt(index++,m_A_Asset_Group_ID);
			if (m_M_Product_ID != 0)
				pstmt.setInt(index++,m_M_Product_ID);
			if (m_C_BPartner_ID != 0)
				pstmt.setInt(index++, m_C_BPartner_ID);
			if (m_NoGuarantee_MailText_ID == 0)
				pstmt.setTimestamp(index++, m_GuaranteeDate);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int A_Asset_ID = rs.getInt(1);
				Timestamp GuaranteeDate = rs.getTimestamp(2);

				//	Guarantee Expired
				if (GuaranteeDate != null && GuaranteeDate.before(m_GuaranteeDate))
				{
					if (m_NoGuarantee_MailText_ID != 0)
					{
						sendNoGuaranteeMail (A_Asset_ID, m_NoGuarantee_MailText_ID, get_TrxName());
						reminders++;
					}
				}
				else	//	Guarantee valid
				{
					String msg = deliverIt (A_Asset_ID);
					addLog (A_Asset_ID, null, null, msg);
					if (msg.startsWith ("** "))
						errors++;
					else
						count++;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, s, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		log.info("Count=" + count + ", Errors=" + errors + ", Reminder=" + reminders
			+ " - " + (System.currentTimeMillis()-start) + "ms");
		return "@Sent@=" + count + " - @Errors@=" + errors;
	}	//	doIt


	/**
	 * 	Send No Guarantee EMail
	 * 	@param A_Asset_ID asset
	 * 	@param R_MailText_ID mail to send
	 * 	@return message - delivery errors start with **
	 */
	private String sendNoGuaranteeMail (int A_Asset_ID, int R_MailText_ID, Trx trx)
	{
		MAsset asset = new MAsset (getCtx(), A_Asset_ID, trx);
		if (asset.getAD_User_ID() == 0)
			return "** No Asset User";
		MUser user = new MUser (getCtx(), asset.getAD_User_ID(), get_TrxName());
		if (user.getEMail() == null || user.getEMail().length() == 0)
			return "** No Asset User Email";
		if (m_MailText == null || m_MailText.getR_MailText_ID() != R_MailText_ID)
			m_MailText = new MMailText (getCtx(), R_MailText_ID, get_TrxName());
		if (m_MailText.getMailHeader() == null || m_MailText.getMailHeader().length() == 0)
			return "** No Subject";

		//	Create Mail
		EMail email = m_client.createEMail(user.getEMail(), user.getName(), null, null);
		if (email == null)
			return "** Invalid: " + user.getEMail();
		m_MailText.setPO(user);
		m_MailText.setPO(asset);
		String message = m_MailText.getMailText(true);
		if (m_MailText.isHtml())
			email.setMessageHTML(m_MailText.getMailHeader(), message);
		else
		{
			email.setSubject (m_MailText.getMailHeader());
			email.setMessageText (message);
		}
		String msg = email.send();
		new MUserMail(m_MailText, asset.getAD_User_ID(), email).save();
		if (!EMail.SENT_OK.equals(msg))
			return "** Not delivered: " + user.getEMail() + " - " + msg;
		//
		return user.getEMail();
	}	//	sendNoGuaranteeMail

	
	/**************************************************************************
	 * 	Deliver Asset
	 * 	@param A_Asset_ID asset
	 * 	@return message - delivery errors start with **
	 */
	private String deliverIt (int A_Asset_ID)
	{
		log.fine("A_Asset_ID=" + A_Asset_ID);
		long start = System.currentTimeMillis();
		//
		MAsset asset = new MAsset (getCtx(), A_Asset_ID, get_TrxName());
		if (asset.getAD_User_ID() == 0)
			return "** No Asset User";
		MUser user = new MUser (getCtx(), asset.getAD_User_ID(), get_TrxName());
		if (user.getEMail() == null || user.getEMail().length() == 0)
			return "** No Asset User Email";
		if (asset.getProductR_MailText_ID() == 0)
			return "** Product Mail Text";
		if (m_MailText == null || m_MailText.getR_MailText_ID() != asset.getProductR_MailText_ID())
			m_MailText = new MMailText (getCtx(), asset.getProductR_MailText_ID(), get_TrxName());
		if (m_MailText.getMailHeader() == null || m_MailText.getMailHeader().length() == 0)
			return "** No Subject";

		//	Create Mail
		EMail email = m_client.createEMail(user.getEMail(), user.getName(), null, null);
		if (email == null || !email.isValid())
		{
			asset.setHelp(asset.getHelp() + " - Invalid EMail");
			asset.setIsActive(false);
			return "** Invalid EMail: " + user.getEMail() + " - " + email;
		}
		if (m_client.isSmtpAuthorization())
			email.createAuthenticator(m_client.getRequestUser(), m_client.getRequestUserPW());
		m_MailText.setUser(user);
		m_MailText.setPO(asset);
		String message = m_MailText.getMailText(true);
		if (m_MailText.isHtml() || m_AttachAsset)
			email.setMessageHTML(m_MailText.getMailHeader(), message);
		else
		{
			email.setSubject (m_MailText.getMailHeader());
			email.setMessageText (message);
		}
		if (m_AttachAsset)
		{
			MProductDownload[] pdls = asset.getProductDownloads();
			if (pdls != null)
			{
				for (MProductDownload element : pdls) {
					URL url = element.getDownloadURL(m_client.getDocumentDir());
					if (url != null)
						email.addAttachment(url);
				}
			}
			else
				log.warning("No DowloadURL for A_Asset_ID=" + A_Asset_ID);
		}
		String msg = email.send();
		new MUserMail(m_MailText, asset.getAD_User_ID(), email).save();
		if (!EMail.SENT_OK.equals(msg))
			return "** Not delivered: " + user.getEMail() + " - " + msg;

		MAssetDelivery ad = asset.confirmDelivery(email, user.getAD_User_ID());
		ad.save();
		asset.save();
		//
		log.fine((System.currentTimeMillis()-start) + " ms");
		//	success
		return user.getEMail() + " - " + asset.getProductVersionNo();
	}	//	deliverIt

}	//	AssetDelivery
