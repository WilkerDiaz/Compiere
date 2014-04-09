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

import java.io.*;
import java.math.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.util.*;
 
/**
 *  Shopping Cart.
 * 	you could add a new line via parameter
 * 	?M_Product_ID=11&Name=aaaaa&Quantity=1&Price=11.11
 * 	if price list found, the price will be potentially overwritten
 *
 *  @author ategawinata
 *  @version  $Id: ShoppingCartServlet.java $
 */
public class ShoppingCartServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(ShoppingCartServlet.class);
	/** Name						*/
	static public final String		NAME = "basketServlet";
	/** SalesRep Parameter			*/
	static public final String		P_SalesRep_ID = "SalesRep_ID";
	/** Product Parameter			*/
	static public final String		P_Product_ID = "M_Product_ID";

	/** Post parameters			*/
	static public final String		ACTION = "act";		// Actions parameter
	static public final String		ACTION_CHECKOUT = "c";		// checkout
	static public final String		ACTION_ADD = "a";		// add multiple
	static public final String		ACTION_ADD_SINGLE = "a1";  // add single
	static public final String		ACTION_UPDATE = "u";		// checkout
	static public final String		INPUT_PRODUCT_ID = "prod";// Product ID
	static public final String		INPUT_QUANTITY = "qty";	// Quantity
	static public final String		INPUT_DELETE = "del";	// Delete
	static public final String		INPUT_LICENSE = "agreeLicense";	// Licence checked
	
	
	
	/**
	 *	Initialize global variables
	 *
	 *  @param config Configuration
	 *  @throws ServletException
	 */
	@Override
	public void init(ServletConfig config)
		throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("ShoppingCartServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	@Override
	public String getServletInfo()
	{
		return "Compiere Web Basket";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	@Override
	public void destroy()
	{
		log.fine("");
	}   //  destroy


	/**************************************************************************
	 *  Process the HTTP Get request.
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		//	Go back to basket
		String url = "/cart.jsp";
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}	//	doGet
	
	
	private void updateCart(HttpServletRequest request, PriceList pl, WebBasket wb)
	{
		Map<String, CartItem> linesMap = new HashMap<String, CartItem>();
		Enumeration<?> en = request.getParameterNames ();
		String action = WebUtil.getParameter(request, ACTION); 

		while (en.hasMoreElements ())
		{
			String param = en.nextElement().toString();
			int idx = param.indexOf("_");
			if (idx < 0) continue;
			String key = param.substring(idx+1);
			CartItem ci = linesMap.get(key);
			if (ci == null) {
				ci = new CartItem();
			}
			if (param.startsWith(INPUT_QUANTITY)) {
				String sQty = WebUtil.getParameter(request, param);
				if (!sQty.isEmpty()) {
					BigDecimal qty = new BigDecimal(sQty);
					ci.qty = qty;
				}
			}
			else if (param.startsWith(INPUT_PRODUCT_ID)) {
				String sId = WebUtil.getParameter(request, param);
				int prodId = Integer.parseInt(sId);
				ci.prodId = prodId;
			}
			else if (param.startsWith(INPUT_DELETE)) {
				String sDel = WebUtil.getParameter(request, param);
				ci.delete = "y".equals(sDel) || "Y".equals(sDel);
			}
			linesMap.put(key, ci);
		}
		
		for (CartItem ci : linesMap.values()) {
			PriceListProduct plp = pl.getPriceListProduct(ci.prodId);
			if (plp != null)
			{
				BigDecimal price = plp.getPrice ();
				String name = plp.getName ();
				log.fine("Found in PL = " + name + " - " + price);
				WebBasketLine wbl = null;
				if (ACTION_ADD.equals(action)) {
					if (ci.qty.compareTo(BigDecimal.ZERO) > 0) {  
						wbl = wb.add(ci.prodId, name, ci.qty, price);
					}
				}
				else if (ACTION_ADD_SINGLE.equals(action)) {
					if ((ci.qty.compareTo(BigDecimal.ZERO) > 0) && !ci.delete) {  
						wbl = wb.add(ci.prodId, name, ci.qty, price);
					}
				}
				else {
					if ((ci.qty.compareTo(BigDecimal.ONE) < 0) || (ci.delete)) {
						wbl = wb.deleteItem(ci.prodId);
					}
					else {
						wbl = wb.addOrUpdate(ci.prodId, name, ci.qty, price);
					}
				}
				log.fine("Basket line = " + wbl);
			}
		}
	}
	
	
	/**
	 *  Process the HTTP Post request
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{

		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr()
				+ " - " + request.getRequestURL());
		JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);

		//	Create WebBasket
		WebBasket wb = (WebBasket)session.getAttribute(WebBasket.NAME);
		if (wb == null)
			wb = new WebBasket();
		//	SalesRep
		int SalesRep_ID = WebUtil.getParameterAsInt (request, P_SalesRep_ID);
		if (SalesRep_ID != 0)
		{
			wb.setSalesRep_ID(SalesRep_ID);
			log.fine("SalesRep_ID=" + SalesRep_ID);
		}

		//	Get Price List
		PriceList pl = (PriceList)session.getAttribute(PriceList.NAME);
		if (pl == null)
		{
			log.fine("No Price List in session");
			pl = (PriceList)request.getAttribute(PriceList.NAME);
		}
		log.fine("PL=" + pl);
		Ctx ctx = JSPEnv.getCtx(request);
		
		if (pl == null) {
			//	Create complete Price List
			int M_PriceList_ID = WebUtil.getParameterAsInt (request, "M_PriceList_ID");
			int M_PriceList_Version_ID = WebUtil.getParameterAsInt (request, "M_PriceList_Version_ID");
			wb.setM_PriceList_ID (M_PriceList_ID);
			wb.setM_PriceList_Version_ID (M_PriceList_Version_ID);
			
			int AD_Client_ID = ctx.getContextAsInt( "AD_Client_ID");
				pl = PriceList.get (ctx, AD_Client_ID, M_PriceList_ID, null, null, true);
				session.setAttribute(PriceList.NAME, pl);
		}
		
		wb.setCurrency_ID(pl.getCurrency_ID());
			
		updateCart(request, pl, wb);
		//compiere specific validation
		boolean isCWS = isCompiereWebStore(ctx); 
		if (isCWS) {
			wb.setMessage(CompiereCartValidator.validate(wb));
			
		}
		wb.getTotal(true);
		session.setAttribute(WebBasket.NAME, wb);
		
		String url = "/cart.jsp";
		String action = WebUtil.getParameter(request, ACTION); 
		String licenseChecked = WebUtil.getParameter(request, INPUT_LICENSE); 
		if (ACTION_CHECKOUT.equals(action)) {
			if ((isCWS && licenseChecked.equals("y"))
					|| (!isCWS)) {
				session.setAttribute(WebBasket.NAME, wb);
				url = ctx.get(WebSessionCtx.CTX_SERVER_CONTEXT) + "/checkOutServlet";
				response.sendRedirect(url);
			}
			else {
				session.setAttribute(CheckOutServlet.ATTR_CHECKOUT, "N");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
				dispatcher.forward (request, response);
			}
		}
		else {
			session.setAttribute(CheckOutServlet.ATTR_CHECKOUT, "N");
			session.setAttribute(WebBasket.NAME, wb);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
			dispatcher.forward (request, response);
		}
		log.info ("Forward to " + url);
	}
	
	
	static private boolean isCompiereWebStore(Ctx ctx) {
		return (ctx.get(WebSessionCtx.KEY_CWS) != null); 
	}
	
	
	static private class CartItem {
		public int prodId;
		public BigDecimal qty = BigDecimal.ZERO;
		public boolean delete = false;
	}
	
}   //  Cart
