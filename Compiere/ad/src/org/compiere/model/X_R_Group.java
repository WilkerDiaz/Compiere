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
/** Generated Model for R_Group
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_Group.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_Group extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_Group_ID id
    @param trx transaction
    */
    public X_R_Group (Ctx ctx, int R_Group_ID, Trx trx)
    {
        super (ctx, R_Group_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_Group_ID == 0)
        {
            setName (null);
            setR_Group_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_Group (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=773 */
    public static final int Table_ID=773;
    
    /** TableName=R_Group */
    public static final String Table_Name="R_Group";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set BOM.
    @param M_BOM_ID Bill of Materials */
    public void setM_BOM_ID (int M_BOM_ID)
    {
        if (M_BOM_ID <= 0) set_Value ("M_BOM_ID", null);
        else
        set_Value ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
    }
    
    /** Get BOM.
    @return Bill of Materials */
    public int getM_BOM_ID() 
    {
        return get_ValueAsInt("M_BOM_ID");
        
    }
    
    /** Set Change Notice.
    @param M_ChangeNotice_ID Bill of Materials (Engineering) Change Notice (Version) */
    public void setM_ChangeNotice_ID (int M_ChangeNotice_ID)
    {
        if (M_ChangeNotice_ID <= 0) set_Value ("M_ChangeNotice_ID", null);
        else
        set_Value ("M_ChangeNotice_ID", Integer.valueOf(M_ChangeNotice_ID));
        
    }
    
    /** Get Change Notice.
    @return Bill of Materials (Engineering) Change Notice (Version) */
    public int getM_ChangeNotice_ID() 
    {
        return get_ValueAsInt("M_ChangeNotice_ID");
        
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
    
    /** Set Group.
    @param R_Group_ID Request Group */
    public void setR_Group_ID (int R_Group_ID)
    {
        if (R_Group_ID < 1) throw new IllegalArgumentException ("R_Group_ID is mandatory.");
        set_ValueNoCheck ("R_Group_ID", Integer.valueOf(R_Group_ID));
        
    }
    
    /** Get Group.
    @return Request Group */
    public int getR_Group_ID() 
    {
        return get_ValueAsInt("R_Group_ID");
        
    }
    
    
}
