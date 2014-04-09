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
package org.compiere.cm.cache;

import org.compiere.cm.*;
import org.compiere.framework.*;
import org.compiere.model.*;

/**
 *	Chat Cache Object
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class Chat extends CO {
	
	/**
	 * 	getCM_Chat
	 *	@param ID
	 *	@return Chat
	 */
	public MChat getCM_Chat(int ID) {
		return getCM_Chat(""+ ID);
	}
	
	/**
	 * 	getCM_Chat
	 *	@param ID
	 *	@return Chat
	 */
	public MChat getCM_Chat(String ID) {
		if (cache.containsKey(ID)) {
			use(ID);
			return (MChat) cache.get(ID);
		} else {
			int[] tableKeys = PO.getAllIDs("CM_Chat", "CM_Chat_ID=" + ID, HttpServletCM.getTrx());
			if (tableKeys.length==0) {
				// No Chat entry
				return null;
			} else if (tableKeys.length==1) {
				MChat thisChat = new MChat(ctx, tableKeys[0], HttpServletCM.getTrx()); 
				put ("" + thisChat.get_ID(),thisChat);
				return thisChat;
			} else {
				// More than one result, this is funny, normally this is not possible :-/
				return null;
			}
		}
	}
}
