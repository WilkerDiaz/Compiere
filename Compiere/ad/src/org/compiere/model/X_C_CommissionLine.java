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
/** Generated Model for C_CommissionLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_CommissionLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_CommissionLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_CommissionLine_ID id
    @param trx transaction
    */
    public X_C_CommissionLine (Ctx ctx, int C_CommissionLine_ID, Trx trx)
    {
        super (ctx, C_CommissionLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_CommissionLine_ID == 0)
        {
            setAmtMultiplier (Env.ZERO);
            setAmtSubtract (Env.ZERO);
            setC_CommissionLine_ID (0);
            setC_Commission_ID (0);
            setCommissionOrders (false);
            setIsPositiveOnly (false);
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_CommissionLine WHERE C_Commission_ID=@C_Commission_ID@
            setQtyMultiplier (Env.ZERO);
            setQtySubtract (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_CommissionLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=431 */
    public static final int Table_ID=431;
    
    /** TableName=C_CommissionLine */
    public static final String Table_Name="C_CommissionLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Multiplier Amount.
    @param AmtMultiplier Multiplier Amount for generating commissions */
    public void setAmtMultiplier (java.math.BigDecimal AmtMultiplier)
    {
        if (AmtMultiplier == null) throw new IllegalArgumentException ("AmtMultiplier is mandatory.");
        set_Value ("AmtMultiplier", AmtMultiplier);
        
    }
    
    /** Get Multiplier Amount.
    @return Multiplier Amount for generating commissions */
    public java.math.BigDecimal getAmtMultiplier() 
    {
        return get_ValueAsBigDecimal("AmtMultiplier");
        
    }
    
    /** Set Subtract Amount.
    @param AmtSubtract Subtract Amount for generating commissions */
    public void setAmtSubtract (java.math.BigDecimal AmtSubtract)
    {
        if (AmtSubtract == null) throw new IllegalArgumentException ("AmtSubtract is mandatory.");
        set_Value ("AmtSubtract", AmtSubtract);
        
    }
    
    /** Get Subtract Amount.
    @return Subtract Amount for generating commissions */
    public java.math.BigDecimal getAmtSubtract() 
    {
        return get_ValueAsBigDecimal("AmtSubtract");
        
    }
    
    /** Set Business Partner Group.
    @param C_BP_Group_ID Business Partner Group */
    public void setC_BP_Group_ID (int C_BP_Group_ID)
    {
        if (C_BP_Group_ID <= 0) set_Value ("C_BP_Group_ID", null);
        else
        set_Value ("C_BP_Group_ID", Integer.valueOf(C_BP_Group_ID));
        
    }
    
    /** Get Business Partner Group.
    @return Business Partner Group */
    public int getC_BP_Group_ID() 
    {
        return get_ValueAsInt("C_BP_Group_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Commission Line.
    @param C_CommissionLine_ID Commission Line */
    public void setC_CommissionLine_ID (int C_CommissionLine_ID)
    {
        if (C_CommissionLine_ID < 1) throw new IllegalArgumentException ("C_CommissionLine_ID is mandatory.");
        set_ValueNoCheck ("C_CommissionLine_ID", Integer.valueOf(C_CommissionLine_ID));
        
    }
    
    /** Get Commission Line.
    @return Commission Line */
    public int getC_CommissionLine_ID() 
    {
        return get_ValueAsInt("C_CommissionLine_ID");
        
    }
    
    /** Set Commission.
    @param C_Commission_ID Commission */
    public void setC_Commission_ID (int C_Commission_ID)
    {
        if (C_Commission_ID < 1) throw new IllegalArgumentException ("C_Commission_ID is mandatory.");
        set_ValueNoCheck ("C_Commission_ID", Integer.valueOf(C_Commission_ID));
        
    }
    
    /** Get Commission.
    @return Commission */
    public int getC_Commission_ID() 
    {
        return get_ValueAsInt("C_Commission_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Commission_ID()));
        
    }
    
    /** Set Sales Region.
    @param C_SalesRegion_ID Sales coverage region */
    public void setC_SalesRegion_ID (int C_SalesRegion_ID)
    {
        if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
        else
        set_Value ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
    }
    
    /** Get Sales Region.
    @return Sales coverage region */
    public int getC_SalesRegion_ID() 
    {
        return get_ValueAsInt("C_SalesRegion_ID");
        
    }
    
    /** Set Commission only specified Orders.
    @param CommissionOrders Commission only Orders or Invoices where this Sales Rep is entered */
    public void setCommissionOrders (boolean CommissionOrders)
    {
        set_Value ("CommissionOrders", Boolean.valueOf(CommissionOrders));
        
    }
    
    /** Get Commission only specified Orders.
    @return Commission only Orders or Invoices where this Sales Rep is entered */
    public boolean isCommissionOrders() 
    {
        return get_ValueAsBoolean("CommissionOrders");
        
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
    
    /** Set Positive only.
    @param IsPositiveOnly Do not generate negative commissions */
    public void setIsPositiveOnly (boolean IsPositiveOnly)
    {
        set_Value ("IsPositiveOnly", Boolean.valueOf(IsPositiveOnly));
        
    }
    
    /** Get Positive only.
    @return Do not generate negative commissions */
    public boolean isPositiveOnly() 
    {
        return get_ValueAsBoolean("IsPositiveOnly");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Set Product Category.
    @param M_Product_Category_ID Category of a Product */
    public void setM_Product_Category_ID (int M_Product_Category_ID)
    {
        if (M_Product_Category_ID <= 0) set_Value ("M_Product_Category_ID", null);
        else
        set_Value ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
        
    }
    
    /** Get Product Category.
    @return Category of a Product */
    public int getM_Product_Category_ID() 
    {
        return get_ValueAsInt("M_Product_Category_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Org.
    @param Org_ID Organizational entity within client */
    public void setOrg_ID (int Org_ID)
    {
        if (Org_ID <= 0) set_Value ("Org_ID", null);
        else
        set_Value ("Org_ID", Integer.valueOf(Org_ID));
        
    }
    
    /** Get Org.
    @return Organizational entity within client */
    public int getOrg_ID() 
    {
        return get_ValueAsInt("Org_ID");
        
    }
    
    /** Set Multiplier Quantity.
    @param QtyMultiplier Indicates the value to multiply quantities by for generating commissions. */
    public void setQtyMultiplier (java.math.BigDecimal QtyMultiplier)
    {
        if (QtyMultiplier == null) throw new IllegalArgumentException ("QtyMultiplier is mandatory.");
        set_Value ("QtyMultiplier", QtyMultiplier);
        
    }
    
    /** Get Multiplier Quantity.
    @return Indicates the value to multiply quantities by for generating commissions. */
    public java.math.BigDecimal getQtyMultiplier() 
    {
        return get_ValueAsBigDecimal("QtyMultiplier");
        
    }
    
    /** Set Subtract Quantity.
    @param QtySubtract Indicates the quantity to subtract when generating commissions */
    public void setQtySubtract (java.math.BigDecimal QtySubtract)
    {
        if (QtySubtract == null) throw new IllegalArgumentException ("QtySubtract is mandatory.");
        set_Value ("QtySubtract", QtySubtract);
        
    }
    
    /** Get Subtract Quantity.
    @return Indicates the quantity to subtract when generating commissions */
    public java.math.BigDecimal getQtySubtract() 
    {
        return get_ValueAsBigDecimal("QtySubtract");
        
    }
    
    
}
