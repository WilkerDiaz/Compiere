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
/** Generated Model for C_Channel
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Channel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Channel extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Channel_ID id
    @param trx transaction
    */
    public X_C_Channel (Ctx ctx, int C_Channel_ID, Trx trx)
    {
        super (ctx, C_Channel_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Channel_ID == 0)
        {
            setC_Channel_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Channel (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=275 */
    public static final int Table_ID=275;
    
    /** TableName=C_Channel */
    public static final String Table_Name="C_Channel";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Print Color.
    @param AD_PrintColor_ID Color used for printing and display */
    public void setAD_PrintColor_ID (int AD_PrintColor_ID)
    {
        if (AD_PrintColor_ID <= 0) set_Value ("AD_PrintColor_ID", null);
        else
        set_Value ("AD_PrintColor_ID", Integer.valueOf(AD_PrintColor_ID));
        
    }
    
    /** Get Print Color.
    @return Color used for printing and display */
    public int getAD_PrintColor_ID() 
    {
        return get_ValueAsInt("AD_PrintColor_ID");
        
    }
    
    /** Set Channel.
    @param C_Channel_ID Sales Channel */
    public void setC_Channel_ID (int C_Channel_ID)
    {
        if (C_Channel_ID < 1) throw new IllegalArgumentException ("C_Channel_ID is mandatory.");
        set_ValueNoCheck ("C_Channel_ID", Integer.valueOf(C_Channel_ID));
        
    }
    
    /** Get Channel.
    @return Sales Channel */
    public int getC_Channel_ID() 
    {
        return get_ValueAsInt("C_Channel_ID");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    
}
