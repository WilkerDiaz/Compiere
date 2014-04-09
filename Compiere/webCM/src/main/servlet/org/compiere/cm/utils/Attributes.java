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
package org.compiere.cm.utils;

import java.util.*;

import javax.servlet.http.*;

public class Attributes implements HttpSessionAttributeListener {
	private static Hashtable<String, Integer> projectSessions = new Hashtable<String, Integer>();

	public void attributeAdded(HttpSessionBindingEvent se) {
		if (se.getName().equals("isy.user.project_id")) {
			String project_id = se.getValue().toString();
			int projectSessionCount = 0;
			// Es gibt bereits einen Eintrag in der Hashtable
			if (projectSessions.containsKey(project_id)) {
				projectSessionCount= (projectSessions.get(project_id)).intValue();
				projectSessionCount++;
				projectSessions.put(project_id,new Integer(projectSessionCount));
			} else {
				projectSessionCount = 1;
				projectSessions.put(project_id,new Integer(projectSessionCount));
			}
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent se) {
		if (se.getName().equals("isy.user.project_id")) {
			String project_id = se.getValue().toString();
			int projectSessionCount = 0;
			// Es gibt bereits einen Eintrag in der Hashtable
			if (projectSessions.containsKey(project_id)) {
				projectSessionCount= (projectSessions.get(project_id)).intValue();
				if (projectSessionCount>0) projectSessionCount--;
				projectSessions.put(project_id,new Integer(projectSessionCount));
			} else {
				projectSessionCount = 0;
				projectSessions.put(project_id,new Integer(projectSessionCount));
			}
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
	}

	public static int getActiveSessions(String project_id) {
		int projectSessionCount = 0;
		if (projectSessions.containsKey(project_id)) {
			projectSessionCount= (projectSessions.get(project_id)).intValue();
		}
		return projectSessionCount;
	}
}
