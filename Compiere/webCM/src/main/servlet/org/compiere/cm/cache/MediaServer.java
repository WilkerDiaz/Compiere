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

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * MediaServer CO stores Cache Objects for the Media Servers, so later we will
 * always return kind of nearest / fastest media server to user...
 * 
 * @author Yves Sandfort
 * @version  $Id$
 */
public class MediaServer extends CO {
	
	/**
	 * getMediaServer will return the correct MediaServer for this request
	 * @param ctx Context
	 * @param WebProjectID The Web Project ID
	 * @param trx TrxName
	 * @return String with URL, or null if none found
	 */
	public String getMediaServer(Ctx ctx, int WebProjectID, Trx trx) {
		String mediaServer = null;
		if (cache.containsKey("" + WebProjectID)) {
			use("" + WebProjectID);
			return (String) cache.get("" + WebProjectID);
		} else {
			int[] mServerIDs = PO.getAllIDs("CM_Media_Server","CM_WebProject_ID=" + WebProjectID, trx);
			if (mServerIDs.length==0) {
				return null;
			} else if (mServerIDs.length==1) {
				// One Result, that's easy, so put it to cache...
				MMediaServer thisMediaServer = new MMediaServer(ctx, mServerIDs[0], trx);
				mediaServer = thisMediaServer.getURL();
				put("" + WebProjectID, mediaServer);
				return mediaServer;
			} else {
				// TODO: We have more than one result, so this means we should be able to run round robin
				MMediaServer thisMediaServer = new MMediaServer(ctx, mServerIDs[0], trx);
				mediaServer = thisMediaServer.getURL();
				put("" + WebProjectID, mediaServer);
				return mediaServer;
				
			}
		}
	}
}
