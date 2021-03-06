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
package org.compiere.web;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: ServerStatus.java 7514 2009-04-20 21:57:40Z freyes $
 */
public class ServerStatus extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	doGet
	 *	@see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 *	@param arg0
	 *	@param arg1
	 *	@throws javax.servlet.ServletException
	 *	@throws java.io.IOException
	 */
	@Override
	protected void doGet (HttpServletRequest arg0, HttpServletResponse arg1)
		throws ServletException, IOException
	{
		super.doGet (arg0, arg1);
	}

	/**
	 * 	doPost
	 *	@see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 *	@param arg0
	 *	@param arg1
	 *	@throws javax.servlet.ServletException
	 *	@throws java.io.IOException
	 */
	@Override
	protected void doPost (HttpServletRequest arg0, HttpServletResponse arg1)
		throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		super.doPost (arg0, arg1);
	}

	/**
	 * 	getServletInfo
	 *	@see javax.servlet.Servlet#getServletInfo()
	 *	@return servlet info
	 */
	@Override
	public String getServletInfo ()
	{
		return super.getServletInfo ();
	}

	/**
	 * 	init
	 *	@see javax.servlet.GenericServlet#init()
	 *	@throws javax.servlet.ServletException
	 */
	@Override
	public void init ()
		throws ServletException
	{
		super.init ();
	}

	/**
	 * 	init
	 *	@see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 *	@param arg0
	 *	@throws javax.servlet.ServletException
	 */
	@Override
	public void init (ServletConfig arg0)
		throws ServletException
	{
		// TODO Auto-generated method stub
		super.init (arg0);
	}

}	//	ServerStatus
