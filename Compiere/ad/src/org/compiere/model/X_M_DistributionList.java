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
/** Generated Model for M_DistributionList
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_DistributionList.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_DistributionList extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_DistributionList_ID id
    @param trx transaction
    */
    public X_M_DistributionList (Ctx ctx, int M_DistributionList_ID, Trx trx)
    {
        super (ctx, M_DistributionList_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_DistributionList_ID == 0)
        {
            setM_DistributionList_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_DistributionList (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=666 */
    public static final int Table_ID=666;
    
    /** TableName=M_DistributionList */
    public static final String Table_Name="M_DistributionList";
    
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
    
    /** Set Distribution List.
    @param M_DistributionList_ID Distribution Lists allow to distribute products to a selected list of partners */
    public void setM_DistributionList_ID (int M_DistributionList_ID)
    {
        if (M_DistributionList_ID < 1) throw new IllegalArgumentException ("M_DistributionList_ID is mandatory.");
        set_ValueNoCheck ("M_DistributionList_ID", Integer.valueOf(M_DistributionList_ID));
        
    }
    
    /** Get Distribution List.
    @return Distribution Lists allow to distribute products to a selected list of partners */
    public int getM_DistributionList_ID() 
    {
        return get_ValueAsInt("M_DistributionList_ID");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Total Ratio.
    @param RatioTotal Total of relative weight in a distribution */
    public void setRatioTotal (java.math.BigDecimal RatioTotal)
    {
        set_Value ("RatioTotal", RatioTotal);
        
    }
    
    /** Get Total Ratio.
    @return Total of relative weight in a distribution */
    public java.math.BigDecimal getRatioTotal() 
    {
        return get_ValueAsBigDecimal("RatioTotal");
        
    }
    
    
}
