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
package org.compiere.model;

import java.rmi.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.db.*;
import org.compiere.interfaces.*;
import org.compiere.util.*;

/**
 * 	Web Store
 *  @author Jorg Janke
 *  @version $Id: MStore.java,v 1.4 2006/07/30 00:51:05 jjanke Exp $
 */
public class MStore extends X_W_Store
{
    /** Logger for class MStore */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MStore.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get WStore from Cache
	 *	@param ctx context
	 *	@param W_Store_ID id
	 *	@return WStore
	 */
	public static MStore get (Ctx ctx, int W_Store_ID)
	{
		Integer key = Integer.valueOf (W_Store_ID);
		MStore retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MStore (ctx, W_Store_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get WStore from Cache
	 *	@param ctx context
	 *	@param contextPath web server context path
	 *	@return WStore
	 */
	public static MStore get (Ctx ctx, String contextPath)
	{
		MStore wstore = null;
		Iterator<MStore> it = s_cache.values().iterator();
		while (it.hasNext())
		{
			wstore = it.next();
			if (wstore.getWebContext().equals(contextPath))
				return wstore;
		}

		//	Search by context
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM W_Store WHERE WebContext=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, contextPath);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				wstore = new MStore (ctx, rs, null);
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	Try client
		if (wstore == null)
		{
			sql = "SELECT * FROM W_Store WHERE AD_Client_ID=? AND IsActive='Y' ORDER BY W_Store_ID";
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt (1, ctx.getAD_Client_ID());
				rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					wstore = new MStore (ctx, rs, null);
					s_log.warning("Context " + contextPath 
						+ " Not found - Found via AD_Client_ID=" + ctx.getAD_Client_ID());
				}
			}
			catch (Exception e)
			{
				s_log.log (Level.SEVERE, sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		//	Nothing
		if (wstore == null)
			return null;
		
		//	Save
		Integer key = Integer.valueOf (wstore.getW_Store_ID());
		s_cache.put (key, wstore);
		return wstore;
	}	//	get
	
	/**
	 * 	Get active Web Stores of Clieny
	 *	@param client client
	 *	@return array of web stores
	 */
	public static MStore[] getOfClient (MClient client)
	{
		ArrayList<MStore> list = new ArrayList<MStore>();
		String sql = "SELECT * FROM W_Store WHERE AD_Client_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, client.get_Trx());
			pstmt.setInt (1, client.getAD_Client_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MStore (client.getCtx(), rs, client.get_Trx()));
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		MStore[] retValue = new MStore[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfClient

	/**
	 * 	Get Active Web Stores
	 *	@return cached web stores - may return none!
	 */
	public static MStore[] getActive()
	{
		s_log.info("" + s_cache.size());
		if (s_cache.size() == 0)
		{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "SELECT * FROM W_Store WHERE IsActive='Y'";
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					MStore wstore = new MStore (Env.getCtx(), rs, null);
					s_cache.put(new Integer(wstore.getW_Store_ID()), wstore);
				}
			}
			catch (Exception e)
			{
				s_log.log (Level.SEVERE, sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		//	Get from Cache
		try
		{
			Collection<MStore> cc = s_cache.values();
			Object[] oo = cc.toArray();
			for (int i = 0; i < oo.length; i++)
				s_log.info(i + ": " + oo[i]);
			
			MStore[] retValue = new MStore[oo.length];
			for (int i = 0; i < oo.length; i++)
				retValue[i] = (MStore)oo[i];
			return retValue;
		}
		catch (Exception e)
		{
			s_log.severe(e.toString());
		}
		return new MStore[]{};
	}	//	getActive

	/**	Cache						*/
	private static final CCache<Integer,MStore> s_cache
		= new CCache<Integer,MStore>("W_Store", 2);
	/**	Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MStore.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param W_Store_ID id
	 *	@param trx p_trx
	 */
	public MStore (Ctx ctx, int W_Store_ID, Trx trx)
	{
		super (ctx, W_Store_ID, trx);
		if (W_Store_ID == 0)
		{
			setIsDefault (false);
			setIsMenuAssets (true);	// Y
			setIsMenuContact (true);	// Y
			setIsMenuInterests (true);	// Y
			setIsMenuInvoices (true);	// Y
			setIsMenuOrders (true);	// Y
			setIsMenuPayments (true);	// Y
			setIsMenuRegistrations (true);	// Y
			setIsMenuRequests (true);	// Y
			setIsMenuRfQs (true);	// Y
			setIsMenuShipments (true);	// Y
			
		//	setC_PaymentTerm_ID (0);
		//	setM_PriceList_ID (0);
		//	setM_Warehouse_ID (0);
		//	setName (null);
		//	setSalesRep_ID (0);
		//	setURL (null);
		//	setWebContext (null);
		}	
	}	//	MWStore

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MStore (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MWStore
	
	/**	The Messages						*/
	private MMailMsg[]	m_msgs	= null;
	
	/**
	 * 	Get Web Context 
	 *	@param full if true fully qualified
	 *	@return web context
	 */
	public String getWebContext(boolean full)
	{
		if (!full)
			return super.getURL();
		String url = super.getURL();
		if (url == null || url.length() == 0)
			url = "http://localhost";
		if (url.endsWith("/"))
			url += url.substring(0, url.length()-1);
		return url + getWebContext();	//	starts with /
	}	//	getWebContext
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("WStore[");
		sb.append(getWebContext(true))
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Context to start with /
		if (!getWebContext().startsWith("/"))
			setWebContext("/" + getWebContext());
		
		//	Org to Warehouse
		if (newRecord || is_ValueChanged("M_Warehouse_ID") || getAD_Org_ID() == 0)
		{
			MWarehouse wh = MWarehouse.get (getCtx(), getM_Warehouse_ID());
			setAD_Org_ID(wh.getAD_Org_ID());
		}
		
		String url = getURL();
		if (url == null)
			url = "";
		boolean urlOK = url.startsWith("http://") || url.startsWith("https://");
		if (!urlOK) // || url.indexOf("localhost") != -1)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "URL") 
				+ " - e.g. http://www.compiere.org");
			return false;
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	Create Messages for new
	 * 	@param newRecord new
	 * 	@param success success
	 * 	@return success
	 */
	@Override
	protected boolean afterSave(boolean newRecord, boolean success)
	{
		if (newRecord && success)
			createMessages();
	    return success;
	}	//	afterSave
	
	
	/************
	 * 	Create EMail from Request User
	 *	@param toEMail recipient
	 *	@param subject subject
	 *	@param message message
	 *	@return EMail
	 */
	public EMail createEMail (String toEMail, String toName, 
		String subject, String message)
	{
		if (toEMail == null || toEMail.length() == 0)
		{
			log.warning("No To");
			return null;
		}
		//
		MClient client = MClient.get(getCtx(), getAD_Client_ID());
		String from = getWStoreEMail();
		if (from == null || from.length() == 0)
			from = client.getRequestEMail();
		EMail email = new EMail (client,
				   from, client.getName(), toEMail, toName,
				   subject, message, client.isServerEMail());
		//	Authorization
		if (client.isSmtpAuthorization())
		{
			if (getWStoreEMail() != null && getWStoreUser() != null && getWStoreUserPW() != null)
				email.createAuthenticator (getWStoreUser(), getWStoreUserPW());
			else
				email.createAuthenticator (client.getRequestUser(), client.getRequestUserPW());
		}
		//	Bcc
		email.addBcc(from);
		//
		return email;
	}	//	createEMail

	/**
	 * 	Send EMail from WebStore User
	 *	@param toEMail recipient email address
	 *	@param subject subject
	 *	@param message message - add header & footer 
	 *	@return true if sent
	 */
	public boolean sendEMail (String toEMail, String toName, 
		String subject, String message)
	{
		if (message == null || message.length() == 0)
		{
			log.warning("No Message");
			return false;
		}
		StringBuffer msgText = new StringBuffer();
		if (getEMailHeader() != null)
			msgText.append(getEMailHeader());
		msgText.append(message);
		if (getEMailFooter() != null)
			msgText.append(getEMailFooter());
		//
		EMail email = createEMail(toEMail, toName, subject, msgText.toString());
		if (email == null)
			return false;
		
		try
		{
			String msg = email.send();
			if (EMail.SENT_OK.equals (email.send ()))
			{
				log.info("Sent EMail " + subject + " to " + toEMail);
				return true;
			}
			else
			{
				log.warning("Could NOT Send Email: " + subject 
					+ " to " + toEMail + ": " + msg
					+ " (" + getName() + ")");
				return false;
			}
		}
		catch (Exception ex)
		{
			log.severe(getName() + " - " + ex.getLocalizedMessage());
			return false;
		}
	}	//	sendEMail
	
	/**
	 * 	Test WebStore EMail
	 *	@return OK or error
	 */
	public String testEMail()
	{
		if (getWStoreEMail() == null || getWStoreEMail().length() == 0)
			return "No Web Store EMail for " + getName();
		//
		EMail email = createEMail (getWStoreEMail(), "WebStore",
			"Compiere WebStore EMail Test", 
			"Compiere WebStore EMail Test: " + toString());
		if (email == null)
			return "Could not create Web Store EMail: " + getName();
		try
		{
			String msg = email.send();
			if (EMail.SENT_OK.equals (email.send ()))
			{
				log.info("Sent Test EMail to " + getWStoreEMail());
				return "OK";
			}
			else
			{
				log.warning("Could NOT send Test Email to " 
					+ getWStoreEMail() + ": " + msg);
				return msg;
			}
		}
		catch (Exception ex)
		{
			log.severe(getName() + " - " + ex.getLocalizedMessage());
			return ex.getLocalizedMessage();
		}
	}	//	testEMail

	/**
	 * 	Get Messages
	 *	@param reload reload data
	 *	@return array of messages
	 */
	public MMailMsg[] getMailMsgs (boolean reload)
	{
		if (m_msgs != null && !reload)
			return m_msgs;
		ArrayList<MMailMsg> list = new ArrayList<MMailMsg>();
		//
		String sql = "SELECT * FROM W_MailMsg WHERE W_Store_ID=? ORDER BY MailMsgType";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getW_Store_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMailMsg (getCtx(), rs, get_Trx()));
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
		//
		m_msgs = new MMailMsg[list.size ()];
		list.toArray (m_msgs);
		return m_msgs;
	}	//	getMailMsgs

	/**
	 * 	Get Mail Msg and if not found create it
	 *	@param MailMsgType mail message type
	 *	@return message
	 */
	public MMailMsg getMailMsg (String MailMsgType)
	{
		if (m_msgs == null)
			getMailMsgs(false);
		
		//	existing msg
		for (MMailMsg element : m_msgs) {
			if (element.getMailMsgType().equals(MailMsgType))
				return element;
		}
		
		//	create missing
		if (createMessages() == 0)
		{
			log.severe("Not created/found: " + MailMsgType);
			return null;
		}
		getMailMsgs(true);
		//	try again
		for (MMailMsg element : m_msgs) {
			if (element.getMailMsgType().equals(MailMsgType))
				return element;
		}
		
		//	nothing found
		log.severe("Not found: " + MailMsgType);
		return null;
	}	//	getMailMsg
	
	
	/**************************************************************************
	 * 	Create (missing) Messages
	 * 	@return number of messages created
	 */
	public int createMessages()
	{
		String[][] initMsgs = new String[][]
		{
			new String[]{X_W_MailMsg.MAILMSGTYPE_UserVerification,
				"EMail Verification", 
				"EMail Verification ",
				"Dear ", 
				"\nYou requested the Verification Code: ",
				"\nPlease enter the verification code to get access."},
			new String[]{X_W_MailMsg.MAILMSGTYPE_UserPassword,
				"Password Request", 
				"Password Request ",
				"Dear ", 
				"\nWe received a 'Send Password' request from: ",
				"\nYour password is: "},
			new String[]{X_W_MailMsg.MAILMSGTYPE_Subscribe,
				"Subscription New", 
				"New Subscription ",
				"Dear ", 
				"\nYou requested to be added to the list: ",
				"\nThanks for your interest."},
			new String[]{X_W_MailMsg.MAILMSGTYPE_Unsubscribe,
				"Subscription Removed", 
				"Remove Subscription ",
				"Dear ", 
				"\nYou requested to be removed from the list: ",
				"\nSorry to see you go.  This is effictive immediately."},
			new String[]{X_W_MailMsg.MAILMSGTYPE_OrderAcknowledgement,
				"Order Acknowledgement", 
				"Compiere Web - Order ",
				"Dear ", 
				"\nThank you for your purchase: ",
				"\nYou can view your Orders, Invoices, Payments in the Web Store."
				+ "\nFrom there, you also download your Assets (Documentation, etc.)"},
			new String[]{X_W_MailMsg.MAILMSGTYPE_PaymentAcknowledgement,
				"Payment Success", 
				"Compiere Web - Payment ",
				"Dear ", 
				"\nThank you for your payment of ",
				"\nYou can view your Orders, Invoices, Payments in the Web Store."
				+ "\nFrom there you also download your Assets (Documentation, etc.)"},
			new String[]{X_W_MailMsg.MAILMSGTYPE_PaymentError,
				"Payment Error", 
				"Compiere Web - Declined Payment ",
				"Dear ",
				"\nUnfortunately your payment was declined: ",
				"\nPlease check and try again. You can pay later by going to 'My Orders' or 'My Invoices' - or by directly creating a payment in 'My Payments'"},
			new String[]{X_W_MailMsg.MAILMSGTYPE_Request,
				"Request", 
				"Request ",
				"Dear ",
				"\nThank you for your request: " + MRequest.SEPARATOR,
				MRequest.SEPARATOR + "\nPlease check back for updates."},
				
			new String[]{X_W_MailMsg.MAILMSGTYPE_UserAccount,
				"Welcome Message", 
				"Welcome",
				"Welcome to our Web Store",
				"This is the Validation Code to access information:",
				""},
		};
		
		if (m_msgs == null)
			getMailMsgs(false);
		if (m_msgs.length == initMsgs.length)	//	may create a problem if user defined own ones - unlikely
			return 0;		//	nothing to do
		
		int counter = 0;
		for (String[] element : initMsgs) 
		{
			boolean found = false;
			for (MMailMsg element2 : m_msgs) 
			{
				if (element[0].equals(element2.getMailMsgType()))
				{
					found = true;
					break;
				}
			}	//	for all existing msgs
			if (found)
				continue;
			MMailMsg msg = new MMailMsg(this, element[0], element[1], 
				element[2], element[3], element[4], element[5]);
			if (msg.save(get_Trx()))
				counter++;
			else
				log.severe("Not created MailMsgType=" + element[0]);
		}	//	for all initMsgs
		
		log.info("#" + counter);
		m_msgs = null;		//	reset
		return counter;
	}	//	createMessages
	
}	//	MWStore
