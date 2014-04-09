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
package org.compiere.wstore;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.ecs.xhtml.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Info Links (Menu).
 * 	Creates Invoice/Payment/Asset/AddressInfo/PaymentInfo Link
 *  <pre>
 *  <cws:infoLink/>
 *	</pre>
 *
 *  @author Jorg Janke
 *  @version $Id: InfoLinkTag.java,v 1.6 2006/09/21 20:45:30 jjanke Exp $
 */
public class InfoLinkTag extends TagSupport
{
	/** SV */
	private static final long serialVersionUID = 7608741032814139346L;
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (InfoLinkTag.class);
	/** One Line						*/
	private boolean			m_oneLine = false;

	/**
	 *	Set to one line
	 *	@param var Y or something else
	 */
	public void setOneLine (String var)
	{
		m_oneLine = "Y".equals(var);
	}	//	setOneLine

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	@Override
	public int doStartTag() throws JspException
	{
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Ctx ctx = JSPEnv.getCtx(request);		//	creates wsc/wu
		WebSessionCtx wsc = WebSessionCtx.get(request, true);
		//
		HttpSession session = pageContext.getSession();
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		JspWriter out = pageContext.getOut();
		
		HtmlCode html = new HtmlCode();
		menuCommon(html, wsc.wstore, wu);
		
		if (wu != null && wu.isLoggedIn())
		{
			if (ctx != null)
			{
				WebInfo info = (WebInfo)session.getAttribute(WebInfo.NAME);
				if (info == null || wu.getAD_User_ID() != info.getAD_User_ID())
				{
					info = new WebInfo (ctx, wu);
					session.setAttribute (WebInfo.NAME, info);
				}
			}
			//
		//	log.fine("WebUser exists - " + wu);
			//
			//
			
			if (wu.isCustomer() || wu.isVendor())
				menuBPartner (html, wsc.wstore);
			if (wu.isSalesRep())
				menuSalesRep (html, wsc.wstore);
			if (wu.isEmployee() || wu.isSalesRep())
				menuUser (html, wu.isEmployee(), wsc.wstore);
			menuAll (html, wsc.wstore);
			//
		}
		else
		{
			if (CLogMgt.isLevelFiner())
				log.fine("No WebUser");
			if (session.getAttribute(WebInfo.NAME) == null)
				session.setAttribute (WebInfo.NAME, WebInfo.getGeneral());
		}
		html.output(out);
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	Add Business Partner Links.
	 * 		My Downloads
	 * 		My Invoices
	 * 		My Payments
	 * 		My Orders
	 * 		My Shipments
	 * 		My RfQ
	 *	@param html code
	 *	@param wstore web store
	 */
	private void menuBPartner (HtmlCode html, MStore wstore)
	{
		boolean first = true;
		if (wstore.isMenuAssets())
		{
			first = false;
			li litem = new li();
			a aitem = new a ("assets.jsp");
			aitem.addElement ("My Downloads");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		//
		if (wstore.isMenuInvoices())
		{
			first = false;
			li litem = new li();
			a aitem = new a ("invoices.jsp");
			aitem.addElement ("My Invoices");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		//
		if (wstore.isMenuPayments())
		{
			first = false;
			li litem = new li();
			a aitem = new a ("payments.jsp");
			aitem.addElement ("My Payments");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		//
		if (wstore.isMenuOrders())
		{
			//			--- Orders
			first = false;
			li litem = new li();
			a aitem = new a ("orders.jsp");
			aitem.addElement ("My Orders");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		//
		if (wstore.isMenuShipments())
		{
			//			--- Shipments
			first = false;
			li litem = new li();
			a aitem = new a ("shipments.jsp");
			aitem.addElement ("My Shipments");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		//
		if (wstore.isMenuRfQs())
		{
			//			--- Rfqs
			first = false;
			li litem = new li();
			a aitem = new a ("rfqs.jsp");
			aitem.addElement ("My RfQ's");
			litem.addElement(aitem);
			html.addElement (litem);
		}
	}	//	menuCustomer

	/**
	 * 	Add Links for all.
	 * 		My Requests
	 * 		Interest Area
	 * 		Registration
	 *	@param html code
	 *	@param wstore web store
	 */
	private void menuAll (HtmlCode html, MStore wstore)
	{
//		// account
//		{
//			li litem = new li();
//			a aitem = new a ("update.jsp");
//			aitem.addElement ("My Account");
//			litem.addElement(aitem);
//			html.addElement (litem);
//		}

		//		--- Requests
		if (wstore.isMenuRequests())
		{
			li litem = new li();
			a aitem = new a ("requests.jsp");
			aitem.addElement ("My Requests");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		//
		if (wstore.isMenuInterests())
		{
			li litem = new li();
			a aitem = new a ("info.jsp");
			aitem.addElement ("Interest Area");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		
		if (wstore.isMenuRegistrations())
		{
			li litem = new li();
			a aitem = new a ("registrations.jsp");
			aitem.addElement ("Registration");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		
	}	//	menuAll

	/**
	 * 	Add Links for Sales Reps.
	 * 		Open Requests
	 * 		Advertisements
	 * 		Commissions
	 * 		C.Invoices
	 *	@param html code
	 *	@param wstore web store
	 */
	private void menuSalesRep (HtmlCode html, MStore wstore)
	{
		li litem = new li();
		a aitem = new a ("requests_sr.jsp");
		aitem.addElement ("Open Requests");
		litem.addElement(aitem);
		html.addElement (litem);
		//
		//							--- Advertisements
		aitem = new a ("advertisements.jsp");
		aitem.addElement ("Advertisements");
		html.addElement (aitem);
		//
		//							--- Commissions
		aitem = new a ("commissionRuns.jsp");
		aitem.addElement ("Commissions");
		html.addElement (aitem);
		//							--- C.Invoices
		aitem = new a ("commissionedInvoices.jsp");
		//a.setClass ("menuDetail");
		aitem.addElement ("C.Invoices");
		html.addElement (aitem);
		//
	}	//	menuSalesRep

	/**
	 * 	Add Links for Users.
	 * 		Notes
	 * 		Expenses
	 *	@param html code
	 *	@param isEmployee employee
	 *	@param wstore web store
	 */
	private void menuUser (HtmlCode html, boolean isEmployee, MStore wstore)
	{
		//							--- Notices
		if (isEmployee)
		{
			li litem = new li();
			a aitem = new a ("notes.jsp");
			//a.setClass ("menuMain");
			aitem.addElement ("Notices");
			litem.addElement(aitem);
			html.addElement (litem);
			//
		}
		//							--- Expense
		a a = new a ("expenses.jsp");
		//a.setClass ("a");
		a.addElement ("Expenses");
		html.addElement (a);
	}	//	menuUser
	
	/**
	 * 	Add Links for Users.
	 * 		Notes
	 * 		Expenses
	 *	@param html code
	 *	@param isEmployee employee
	 *	@param wstore web store
	 */
	private void menuCommon (HtmlCode html, MStore wstore, WebUser wu)
	{
		{ // Home
			li litem = new li();
			a aitem = new a (wstore.getURL());
			aitem.addElement ("Home");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		{ // Search
			li litem = new li();
			a aitem = new a ("index.jsp");
			aitem.addElement ("Search");
			litem.addElement(aitem);
			html.addElement (litem);
		}
		if (wu != null && wu.isLoggedIn())
		{ // Account
			li litem = new li();
			a aitem = new a ("update.jsp");
			aitem.addElement ("My Account");
			litem.addElement(aitem);
			html.addElement (litem);
		}
	}
	

	/**
	 * 	Add New Line / Break
	 * 	@param html code
	 * 	@param hr insert HR rather BR
	 */
	private void nl (HtmlCode html, boolean hr)
	{
		if (m_oneLine)
			html.addElement("&nbsp;- ");
		else if (hr)
			html.addElement(new hr("90%", "left"));
		else
			html.addElement(new br());
	}	//	nl

	/**
	 * 	End Tag
	 * 	@return EVAL_PAGE
	 * 	@throws JspException
	 */
	@Override
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}	//	doEndTag
	


}	//	InfoLinkTag
