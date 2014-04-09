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
package org.compiere.install;

import org.apache.tools.ant.Main;

public class CompiereAntMain extends Main
{
	static int retCode = -1;
	
	public CompiereAntMain() 
	{
	}
    /**
     * Start Ant
     * @param args command line args
     *
     * @since Ant 1.6
     */
    public int startAnt(String[] args) 
    {
    	startAnt(args, null, null);
    	return retCode;
    }  // startAnt()
    
    /**
     * This operation is expected to call {@link System#exit(int)}, which
     * is what the base version does.
     * However, it is possible to do something else.
     * @param exitCode code to exit with
     */
    @Override
	protected void exit(int exitCode) 
    {
        //System.exit(exitCode);
    	retCode = exitCode;
    }  // exit()
}
