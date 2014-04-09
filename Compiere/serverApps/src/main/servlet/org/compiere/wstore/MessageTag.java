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

import org.compiere.util.*;

/**
 *  Message/Translation Tag.
 * 	<cws:message txt="AD_Message"/>
 *
 *  @author Jorg Janke
 *  @version $Id: MessageTag.java,v 1.2 2006/07/30 00:53:21 jjanke Exp $
 */
public class MessageTag  extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logger				*/
	private CLogger		log = CLogger.getCLogger (getClass());
	/** Text				*/
	private String		m_txt;

	/**
	 * 	Set text
	 * 	@param txt text to be translated
	 */
	public void setTxt (String txt)
	{
		m_txt = txt;
	}	//	setVar


	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	@Override
	public int doStartTag() throws JspException
	{
		if (m_txt != null && m_txt.length() > 0)
		{
			Ctx ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
			String msg = Msg.translate(ctx, m_txt);
			log.fine(m_txt + "->" + msg);
			//
			try
			{
				JspWriter out = pageContext.getOut();
				out.print (msg);
			}
			catch (Exception e)
			{
				throw new JspException(e);
			}
		}
		return (SKIP_BODY);
	}   //  doStartTag

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

}	//	MessageTag
