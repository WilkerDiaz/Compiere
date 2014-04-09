/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for T_Selection
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_Selection.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_Selection extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_Selection_ID id
    @param trx transaction
    */
    public X_T_Selection (Ctx ctx, int T_Selection_ID, Trx trx)
    {
        super (ctx, T_Selection_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_Selection_ID == 0)
        {
            setT_Selection_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_Selection (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671707789L;
    /** Last Updated Timestamp 2008-12-17 12:39:51.0 */
    public static final long updatedMS = 1229546391000L;
    /** AD_Table_ID=917 */
    public static final int Table_ID=917;
    
    /** TableName=T_Selection */
    public static final String Table_Name="T_Selection";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Selection.
    @param T_Selection_ID *** DO NOT USE *** */
    public void setT_Selection_ID (int T_Selection_ID)
    {
        if (T_Selection_ID < 1) throw new IllegalArgumentException ("T_Selection_ID is mandatory.");
        set_ValueNoCheck ("T_Selection_ID", Integer.valueOf(T_Selection_ID));
        
    }
    
    /** Get Selection.
    @return *** DO NOT USE *** */
    public int getT_Selection_ID() 
    {
        return get_ValueAsInt("T_Selection_ID");
        
    }
    
    
}
