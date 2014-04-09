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
/** Generated Model for GL_FundRestriction
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_GL_FundRestriction.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_GL_FundRestriction extends PO
{
    /** Standard Constructor
    @param ctx context
    @param GL_FundRestriction_ID id
    @param trx transaction
    */
    public X_GL_FundRestriction (Ctx ctx, int GL_FundRestriction_ID, Trx trx)
    {
        super (ctx, GL_FundRestriction_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (GL_FundRestriction_ID == 0)
        {
            setC_ElementValue_ID (0);
            setGL_FundRestriction_ID (0);
            setGL_Fund_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_GL_FundRestriction (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=824 */
    public static final int Table_ID=824;
    
    /** TableName=GL_FundRestriction */
    public static final String Table_Name="GL_FundRestriction";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Account Element.
    @param C_ElementValue_ID Account Element */
    public void setC_ElementValue_ID (int C_ElementValue_ID)
    {
        if (C_ElementValue_ID < 1) throw new IllegalArgumentException ("C_ElementValue_ID is mandatory.");
        set_Value ("C_ElementValue_ID", Integer.valueOf(C_ElementValue_ID));
        
    }
    
    /** Get Account Element.
    @return Account Element */
    public int getC_ElementValue_ID() 
    {
        return get_ValueAsInt("C_ElementValue_ID");
        
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
    
    /** Set Fund Restriction.
    @param GL_FundRestriction_ID Restriction of Funds */
    public void setGL_FundRestriction_ID (int GL_FundRestriction_ID)
    {
        if (GL_FundRestriction_ID < 1) throw new IllegalArgumentException ("GL_FundRestriction_ID is mandatory.");
        set_ValueNoCheck ("GL_FundRestriction_ID", Integer.valueOf(GL_FundRestriction_ID));
        
    }
    
    /** Get Fund Restriction.
    @return Restriction of Funds */
    public int getGL_FundRestriction_ID() 
    {
        return get_ValueAsInt("GL_FundRestriction_ID");
        
    }
    
    /** Set GL Fund.
    @param GL_Fund_ID General Ledger Funds Control */
    public void setGL_Fund_ID (int GL_Fund_ID)
    {
        if (GL_Fund_ID < 1) throw new IllegalArgumentException ("GL_Fund_ID is mandatory.");
        set_ValueNoCheck ("GL_Fund_ID", Integer.valueOf(GL_Fund_ID));
        
    }
    
    /** Get GL Fund.
    @return General Ledger Funds Control */
    public int getGL_Fund_ID() 
    {
        return get_ValueAsInt("GL_Fund_ID");
        
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
