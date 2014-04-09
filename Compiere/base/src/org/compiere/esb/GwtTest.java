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
package org.compiere.esb;

import java.sql.*;
import java.util.*;

import org.compiere.*;
import org.compiere.common.*;
import org.compiere.controller.*;
import org.compiere.util.*;
import org.compiere.vos.*;


/**
 *	Gwt Test Class
 *
 *  @author Jorg Janke
 *  @version $Id$
 */
public class GwtTest
{

	/**	Logger	*/
    private static CLogger log = CLogger.getCLogger(GwtTest.class);

    /**
     * 	Login
     *	@return server
     */
    private static GwtServer login()
    {
		GwtServer server = new GwtServer();
		Login login = server.getLogin();
		server.setLocale(login.setLanguage("English"));
		KeyNamePair[] roles = login.getRoles("SuperUser", "System");
		KeyNamePair role = roles[0];
		for (KeyNamePair rr : roles)
        {
	        if (rr.toString().startsWith("System"))
	        {
	        	role = rr;
	        	break;
	        }
        }
		log.info(role.toString());
		KeyNamePair[] clients = login.getClients(role);
		log.info(clients[0].toString());
		KeyNamePair[] orgs = login.getOrgs(clients[0]);
		log.info(orgs[0].toString());
		KeyNamePair[] whs = login.getWarehouses(orgs[0]);
		KeyNamePair wh = null;
		if (whs != null && whs.length > 0)
		{
			wh = whs[0];
			log.info(wh.toString());
		}
		//	Defaults
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String printerName = "";
		String error = login.loadPreferences(orgs[0], wh, timestamp, printerName);
		log.info(error);
		return server;
    }	//	login

    /**
     * 	Dump data to out
     *	@param data
     */
    private static void dump (Object[][] data)
    {
    	System.out.println("Data:");
    	if (data == null)
    		return;
    	int rows = data.length;
    	for (int r = 0; r < rows; r++)
        {
    		if (data[r] == null)
    	        System.out.println("Row " + r + ": NULL");
    		else
    		{
    			int cols = data[r].length;
    			System.out.print("Row " + r + ": ");
    			for (int c = 0; c < cols; c++)
    			{
    				if (c != 0)
    					System.out.print(", ");
    				System.out.print(data[r][c]);
    			}
    			System.out.println();
    		}
        }
    }	//	dump

    /**
     * 	Add Key/Value pairs to map
     *	@param map map
     *	@param keys keys
     *	@param values values
     */
    private static void addToHashMap (HashMap<String,String> map, String[] keys, String[] values)
    {
    	for (int i = 0; i < values.length; i++)
		{
			map.put(keys[i], values[i]);
		}
    }	//	add

	/**************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup (true);
		GwtServer server = login();
		//	ToDo
		server.getMenuTree();
		//
		if (true)	//	newRow
		{
			int windowNo = 11;
			int AD_Window_ID = 127;	// test -		sales order
			server.getWindow(windowNo, AD_Window_ID, 0);
			int AD_Tab_ID = 152;	// test -		sales order 186
			Map<String,String> context = new HashMap<String,String>();
			server.newRow(windowNo, AD_Tab_ID, context);
		}


		if (true)	//	fieldChanged
		{
			int AD_Window_ID = 116;		//	Currency Rate
			server.getWindow(1, AD_Window_ID, 0);
			int AD_Field_ID = 1340;
			int AD_Tab_ID = 198;
			String oldValue = "12.34";
			String newValue = "0.33";
			Map<String,String> context = new HashMap<String,String>();
			ChangeVO vo = server.fieldChanged(1, AD_Field_ID, AD_Tab_ID,
					oldValue, newValue, context);
			System.out.println(vo.toString());
		}


		int AD_Window_ID = 143;		//	178=Greeting, 143=SO
		int AD_Menu_ID = 0;
		UIWindow order = server.getWindow(1, AD_Window_ID, AD_Menu_ID);
		log.info(order.toString());
		order.dump();
		//	Get Data
		int AD_Tab_ID = 186;		//	282=Greeting, 186=SO
		UITab tab = server.getTab(AD_Tab_ID);
		QueryVO queryVO = null;
		HashMap<String,String> context = new HashMap<String,String>();
		String[] columns = tab.getColumnNames();
		int queryResultID = 1;
		server.executeQuery(AD_Tab_ID, queryVO, context, queryResultID);
		String[][] data = server.getResults (queryResultID, 0, 100);
		dump (data);

		//
		int AD_Field_ID = 1573;		//	C_BPartner_ID
		String oldValue = "118";	//	118=Joe
		String newValue = "";
		addToHashMap(context, columns, data[0]);
		server.fieldChanged(1, AD_Field_ID, AD_Tab_ID, oldValue, newValue, context);

		//	Save
		Ctx ctx = new Ctx("{AD_Org_ID=11, IsDefault=N, IsActive=Y, Greeting=Mr, Name=Mr, C_Greeting_ID=100, AD_Client_ID=11, IsFirstNameOnly=N}");
		int relRowNo = 0;
		System.out.println(server.updateRow(1, AD_Tab_ID, queryResultID, relRowNo, ctx));
		//	Delete
//		server.deleteRow(AD_Tab_ID, ctx);

	}	//	GwtTest

}	//	GwtTest
