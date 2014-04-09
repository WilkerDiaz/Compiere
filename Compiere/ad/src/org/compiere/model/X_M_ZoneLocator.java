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
/** Generated Model for M_ZoneLocator
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_ZoneLocator.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ZoneLocator extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ZoneLocator_ID id
    @param trx transaction
    */
    public X_M_ZoneLocator (Ctx ctx, int M_ZoneLocator_ID, Trx trx)
    {
        super (ctx, M_ZoneLocator_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ZoneLocator_ID == 0)
        {
            setM_Locator_ID (0);	// -1
            setM_ZoneLocator_ID (0);
            setM_Zone_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ZoneLocator (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=1012 */
    public static final int Table_ID=1012;
    
    /** TableName=M_ZoneLocator */
    public static final String Table_Name="M_ZoneLocator";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Zone Locator.
    @param M_ZoneLocator_ID Locator included in the zone */
    public void setM_ZoneLocator_ID (int M_ZoneLocator_ID)
    {
        if (M_ZoneLocator_ID < 1) throw new IllegalArgumentException ("M_ZoneLocator_ID is mandatory.");
        set_ValueNoCheck ("M_ZoneLocator_ID", Integer.valueOf(M_ZoneLocator_ID));
        
    }
    
    /** Get Zone Locator.
    @return Locator included in the zone */
    public int getM_ZoneLocator_ID() 
    {
        return get_ValueAsInt("M_ZoneLocator_ID");
        
    }
    
    /** Set Zone.
    @param M_Zone_ID Warehouse zone */
    public void setM_Zone_ID (int M_Zone_ID)
    {
        if (M_Zone_ID < 1) throw new IllegalArgumentException ("M_Zone_ID is mandatory.");
        set_ValueNoCheck ("M_Zone_ID", Integer.valueOf(M_Zone_ID));
        
    }
    
    /** Get Zone.
    @return Warehouse zone */
    public int getM_Zone_ID() 
    {
        return get_ValueAsInt("M_Zone_ID");
        
    }
    
    
}
