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
/** Generated Model for M_DistributionListLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_DistributionListLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_DistributionListLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_DistributionListLine_ID id
    @param trx transaction
    */
    public X_M_DistributionListLine (Ctx ctx, int M_DistributionListLine_ID, Trx trx)
    {
        super (ctx, M_DistributionListLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_DistributionListLine_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setM_DistributionListLine_ID (0);
            setM_DistributionList_ID (0);
            setMinQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_DistributionListLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=665 */
    public static final int Table_ID=665;
    
    /** TableName=M_DistributionListLine */
    public static final String Table_Name="M_DistributionListLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID < 1) throw new IllegalArgumentException ("C_BPartner_Location_ID is mandatory.");
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
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
    
    /** Set Distribution List Line.
    @param M_DistributionListLine_ID Distribution List Line with Business Partner and Quantity/Percentage */
    public void setM_DistributionListLine_ID (int M_DistributionListLine_ID)
    {
        if (M_DistributionListLine_ID < 1) throw new IllegalArgumentException ("M_DistributionListLine_ID is mandatory.");
        set_ValueNoCheck ("M_DistributionListLine_ID", Integer.valueOf(M_DistributionListLine_ID));
        
    }
    
    /** Get Distribution List Line.
    @return Distribution List Line with Business Partner and Quantity/Percentage */
    public int getM_DistributionListLine_ID() 
    {
        return get_ValueAsInt("M_DistributionListLine_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_DistributionList_ID()));
        
    }
    
    /** Set Minimum Quantity.
    @param MinQty Minimum quantity for the business partner */
    public void setMinQty (java.math.BigDecimal MinQty)
    {
        if (MinQty == null) throw new IllegalArgumentException ("MinQty is mandatory.");
        set_Value ("MinQty", MinQty);
        
    }
    
    /** Get Minimum Quantity.
    @return Minimum quantity for the business partner */
    public java.math.BigDecimal getMinQty() 
    {
        return get_ValueAsBigDecimal("MinQty");
        
    }
    
    /** Set Ratio.
    @param Ratio Relative Ratio for Distributions */
    public void setRatio (java.math.BigDecimal Ratio)
    {
        set_Value ("Ratio", Ratio);
        
    }
    
    /** Get Ratio.
    @return Relative Ratio for Distributions */
    public java.math.BigDecimal getRatio() 
    {
        return get_ValueAsBigDecimal("Ratio");
        
    }
    
    
}
