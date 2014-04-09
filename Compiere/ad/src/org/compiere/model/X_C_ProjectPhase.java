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
/** Generated Model for C_ProjectPhase
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_ProjectPhase.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_ProjectPhase extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_ProjectPhase_ID id
    @param trx transaction
    */
    public X_C_ProjectPhase (Ctx ctx, int C_ProjectPhase_ID, Trx trx)
    {
        super (ctx, C_ProjectPhase_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_ProjectPhase_ID == 0)
        {
            setC_ProjectPhase_ID (0);
            setC_Project_ID (0);
            setCommittedAmt (Env.ZERO);
            setIsCommitCeiling (false);
            setIsComplete (false);
            setName (null);
            setPlannedAmt (Env.ZERO);
            setPlannedQty (Env.ZERO);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_ProjectPhase WHERE C_Project_ID=@C_Project_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_ProjectPhase (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=576 */
    public static final int Table_ID=576;
    
    /** TableName=C_ProjectPhase */
    public static final String Table_Name="C_ProjectPhase";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Standard Phase.
    @param C_Phase_ID Standard Phase of the Project Type */
    public void setC_Phase_ID (int C_Phase_ID)
    {
        if (C_Phase_ID <= 0) set_ValueNoCheck ("C_Phase_ID", null);
        else
        set_ValueNoCheck ("C_Phase_ID", Integer.valueOf(C_Phase_ID));
        
    }
    
    /** Get Standard Phase.
    @return Standard Phase of the Project Type */
    public int getC_Phase_ID() 
    {
        return get_ValueAsInt("C_Phase_ID");
        
    }
    
    /** Set Project Phase.
    @param C_ProjectPhase_ID Phase of a Project */
    public void setC_ProjectPhase_ID (int C_ProjectPhase_ID)
    {
        if (C_ProjectPhase_ID < 1) throw new IllegalArgumentException ("C_ProjectPhase_ID is mandatory.");
        set_ValueNoCheck ("C_ProjectPhase_ID", Integer.valueOf(C_ProjectPhase_ID));
        
    }
    
    /** Get Project Phase.
    @return Phase of a Project */
    public int getC_ProjectPhase_ID() 
    {
        return get_ValueAsInt("C_ProjectPhase_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID < 1) throw new IllegalArgumentException ("C_Project_ID is mandatory.");
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Committed Amount.
    @param CommittedAmt The (legal) commitment amount */
    public void setCommittedAmt (java.math.BigDecimal CommittedAmt)
    {
        if (CommittedAmt == null) throw new IllegalArgumentException ("CommittedAmt is mandatory.");
        set_Value ("CommittedAmt", CommittedAmt);
        
    }
    
    /** Get Committed Amount.
    @return The (legal) commitment amount */
    public java.math.BigDecimal getCommittedAmt() 
    {
        return get_ValueAsBigDecimal("CommittedAmt");
        
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
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        set_Value ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
    }
    
    /** Set Generate Order.
    @param GenerateOrder Generate Order */
    public void setGenerateOrder (String GenerateOrder)
    {
        set_Value ("GenerateOrder", GenerateOrder);
        
    }
    
    /** Get Generate Order.
    @return Generate Order */
    public String getGenerateOrder() 
    {
        return (String)get_Value("GenerateOrder");
        
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
    
    /** Set Commitment is Ceiling.
    @param IsCommitCeiling The commitment amount/quantity is the chargeable ceiling */
    public void setIsCommitCeiling (boolean IsCommitCeiling)
    {
        set_Value ("IsCommitCeiling", Boolean.valueOf(IsCommitCeiling));
        
    }
    
    /** Get Commitment is Ceiling.
    @return The commitment amount/quantity is the chargeable ceiling */
    public boolean isCommitCeiling() 
    {
        return get_ValueAsBoolean("IsCommitCeiling");
        
    }
    
    /** Set Complete.
    @param IsComplete It is complete */
    public void setIsComplete (boolean IsComplete)
    {
        set_Value ("IsComplete", Boolean.valueOf(IsComplete));
        
    }
    
    /** Get Complete.
    @return It is complete */
    public boolean isComplete() 
    {
        return get_ValueAsBoolean("IsComplete");
        
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
    
    /** Set Planned Amount.
    @param PlannedAmt Planned amount for this project */
    public void setPlannedAmt (java.math.BigDecimal PlannedAmt)
    {
        if (PlannedAmt == null) throw new IllegalArgumentException ("PlannedAmt is mandatory.");
        set_Value ("PlannedAmt", PlannedAmt);
        
    }
    
    /** Get Planned Amount.
    @return Planned amount for this project */
    public java.math.BigDecimal getPlannedAmt() 
    {
        return get_ValueAsBigDecimal("PlannedAmt");
        
    }
    
    /** Set Planned Quantity.
    @param PlannedQty Planned quantity for this project */
    public void setPlannedQty (java.math.BigDecimal PlannedQty)
    {
        if (PlannedQty == null) throw new IllegalArgumentException ("PlannedQty is mandatory.");
        set_Value ("PlannedQty", PlannedQty);
        
    }
    
    /** Get Planned Quantity.
    @return Planned quantity for this project */
    public java.math.BigDecimal getPlannedQty() 
    {
        return get_ValueAsBigDecimal("PlannedQty");
        
    }
    
    /** Set Unit Price.
    @param PriceActual Actual Price */
    public void setPriceActual (java.math.BigDecimal PriceActual)
    {
        set_Value ("PriceActual", PriceActual);
        
    }
    
    /** Get Unit Price.
    @return Actual Price */
    public java.math.BigDecimal getPriceActual() 
    {
        return get_ValueAsBigDecimal("PriceActual");
        
    }
    
    /** None = - */
    public static final String PROJINVOICERULE_None = X_Ref_C_Project_InvoiceRule.NONE.getValue();
    /** Committed Amount = C */
    public static final String PROJINVOICERULE_CommittedAmount = X_Ref_C_Project_InvoiceRule.COMMITTED_AMOUNT.getValue();
    /** Product Quantity = P */
    public static final String PROJINVOICERULE_ProductQuantity = X_Ref_C_Project_InvoiceRule.PRODUCT_QUANTITY.getValue();
    /** Time&Material = T */
    public static final String PROJINVOICERULE_TimeMaterial = X_Ref_C_Project_InvoiceRule.TIME_MATERIAL.getValue();
    /** Time&Material max Committed = c */
    public static final String PROJINVOICERULE_TimeMaterialMaxCommitted = X_Ref_C_Project_InvoiceRule.TIME_MATERIAL_MAX_COMMITTED.getValue();
    /** Set Invoice Rule.
    @param ProjInvoiceRule Invoice Rule for the project */
    public void setProjInvoiceRule (String ProjInvoiceRule)
    {
        if (!X_Ref_C_Project_InvoiceRule.isValid(ProjInvoiceRule))
        throw new IllegalArgumentException ("ProjInvoiceRule Invalid value - " + ProjInvoiceRule + " - Reference_ID=383 - - - C - P - T - c");
        set_Value ("ProjInvoiceRule", ProjInvoiceRule);
        
    }
    
    /** Get Invoice Rule.
    @return Invoice Rule for the project */
    public String getProjInvoiceRule() 
    {
        return (String)get_Value("ProjInvoiceRule");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        set_Value ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    
}
