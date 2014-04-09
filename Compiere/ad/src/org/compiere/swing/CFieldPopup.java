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
package org.compiere.swing;


/**
 *  Compiere Field external popup Interface.
 *  The actual class must be a JDialog
 *
 *  @author     Jorg Janke
 *  @version    $Id: CFieldPopup.java 8244 2009-12-04 23:25:29Z freyes $
 */
public interface CFieldPopup
{
	/**
	 *  Show Popup
	 */
	public void show();

	/**
	 *  Set Value
	 *  @param value
	 */
	public void setValue (Object value);

	/**
	 *  Get Value
	 *  @return value
	 */
	public Object getValue();

	/**
	 *  Set Format
	 *  @param format
	 */
	public void setFormat (Object format);

	/**
	 *  Get Format
	 *  @return format
	 */
	public Object getFormat();

}   //  CFieldPopup
