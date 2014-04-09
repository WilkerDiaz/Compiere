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
package org.compiere.print.layout;

import java.awt.*;
import java.awt.geom.*;

import org.compiere.print.*;
import org.compiere.util.*;

/**
 *	Graphic Element
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: GraphElement.java,v 1.2 2006/07/30 00:53:02 jjanke Exp $
 */
public class GraphElement extends PrintElement
{
	/**
	 *	Constructor
	 *  @param pg graph model
	 */
	public GraphElement(MPrintGraph pg)
	{
		p_info = "?";
	}	//	GraphElement

	/**
	 * 	Layout and Calculate Size
	 * 	Set p_width & p_height
	 * 	@return true if calculated
	 */
	@Override
	protected boolean calculateSize()
	{
		return false;
	}	//	calcluateSize

	/**
	 * 	Paint/Print.
	 * 	@param g2D Graphics
	 *  @param pageNo page number for multi page support (0 = header/footer)
	 *  @param pageStart top left Location of page
	 *  @param ctx context
	 *  @param isView true if online view (IDs are links)
	 */
	@Override
	public void paint (Graphics2D g2D, int pageNo, Point2D pageStart, Ctx ctx, boolean isView)
	{

	}	//	paint

}	//	GraphElement

