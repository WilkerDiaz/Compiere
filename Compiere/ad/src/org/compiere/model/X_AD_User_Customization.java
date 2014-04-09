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
/** Generated Model for AD_User_Customization
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_User_Customization.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_User_Customization extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_User_Customization_ID id
    @param trx transaction
    */
    public X_AD_User_Customization (Ctx ctx, int AD_User_Customization_ID, Trx trx)
    {
        super (ctx, AD_User_Customization_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_User_Customization_ID == 0)
        {
            setAD_COMPONENT_ID (Env.ZERO);
            setAD_User_Customization_ID (Env.ZERO);
            setAD_User_ID (0);
            setCUSTOM (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_User_Customization (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27539413484789L;
    /** Last Updated Timestamp 2009-11-03 14:42:48.0 */
    public static final long updatedMS = 1257288168000L;
    /** AD_Table_ID=2141 */
    public static final int Table_ID=2141;
    
    /** TableName=AD_User_Customization */
    public static final String Table_Name="AD_User_Customization";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set AD_COMPONENT_ID.
    @param AD_COMPONENT_ID AD_COMPONENT_ID */
    public void setAD_COMPONENT_ID (java.math.BigDecimal AD_COMPONENT_ID)
    {
        if (AD_COMPONENT_ID == null) throw new IllegalArgumentException ("AD_COMPONENT_ID is mandatory.");
        set_Value ("AD_COMPONENT_ID", AD_COMPONENT_ID);
        
    }
    
    /** Get AD_COMPONENT_ID.
    @return AD_COMPONENT_ID */
    public java.math.BigDecimal getAD_COMPONENT_ID() 
    {
        return get_ValueAsBigDecimal("AD_COMPONENT_ID");
        
    }
    
    /** Set AD_User_Customization_ID.
    @param AD_User_Customization_ID AD_User_Customization_ID */
    public void setAD_User_Customization_ID (java.math.BigDecimal AD_User_Customization_ID)
    {
        if (AD_User_Customization_ID == null) throw new IllegalArgumentException ("AD_User_Customization_ID is mandatory.");
        set_ValueNoCheck ("AD_User_Customization_ID", AD_User_Customization_ID);
        
    }
    
    /** Get AD_User_Customization_ID.
    @return AD_User_Customization_ID */
    public java.math.BigDecimal getAD_User_Customization_ID() 
    {
        return get_ValueAsBigDecimal("AD_User_Customization_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set CUSTOM.
    @param CUSTOM CUSTOM */
    public void setCUSTOM (String CUSTOM)
    {
        if (CUSTOM == null) throw new IllegalArgumentException ("CUSTOM is mandatory.");
        set_Value ("CUSTOM", CUSTOM);
        
    }
    
    /** Get CUSTOM.
    @return CUSTOM */
    public String getCUSTOM() 
    {
        return (String)get_Value("CUSTOM");
        
    }
    
    
}
