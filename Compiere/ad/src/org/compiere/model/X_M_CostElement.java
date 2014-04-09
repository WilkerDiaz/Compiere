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
/** Generated Model for M_CostElement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_CostElement.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_CostElement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_CostElement_ID id
    @param trx transaction
    */
    public X_M_CostElement (Ctx ctx, int M_CostElement_ID, Trx trx)
    {
        super (ctx, M_CostElement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_CostElement_ID == 0)
        {
            setCostElementType (null);
            setIsCalculated (false);
            setIsMfgMaterialCost (false);
            setM_CostElement_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_CostElement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520782047789L;
    /** Last Updated Timestamp 2009-04-01 23:18:51.0 */
    public static final long updatedMS = 1238656731000L;
    /** AD_Table_ID=770 */
    public static final int Table_ID=770;
    
    /** TableName=M_CostElement */
    public static final String Table_Name="M_CostElement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Burden (M.Overhead) = B */
    public static final String COSTELEMENTTYPE_BurdenMOverhead = X_Ref_M_CostElement_Type.BURDEN_M_OVERHEAD.getValue();
    /** Material = M */
    public static final String COSTELEMENTTYPE_Material = X_Ref_M_CostElement_Type.MATERIAL.getValue();
    /** Overhead = O */
    public static final String COSTELEMENTTYPE_Overhead = X_Ref_M_CostElement_Type.OVERHEAD.getValue();
    /** Resource = R */
    public static final String COSTELEMENTTYPE_Resource = X_Ref_M_CostElement_Type.RESOURCE.getValue();
    /** Outside Processing = X */
    public static final String COSTELEMENTTYPE_OutsideProcessing = X_Ref_M_CostElement_Type.OUTSIDE_PROCESSING.getValue();
    /** Set Cost Element Type.
    @param CostElementType Type of Cost Element */
    public void setCostElementType (String CostElementType)
    {
        if (CostElementType == null) throw new IllegalArgumentException ("CostElementType is mandatory");
        if (!X_Ref_M_CostElement_Type.isValid(CostElementType))
        throw new IllegalArgumentException ("CostElementType Invalid value - " + CostElementType + " - Reference_ID=338 - B - M - O - R - X");
        set_Value ("CostElementType", CostElementType);
        
    }
    
    /** Get Cost Element Type.
    @return Type of Cost Element */
    public String getCostElementType() 
    {
        return (String)get_Value("CostElementType");
        
    }
    
    /** Average PO = A */
    public static final String COSTINGMETHOD_AveragePO = X_Ref_C_AcctSchema_Costing_Method.AVERAGE_PO.getValue();
    /** FiFo = F */
    public static final String COSTINGMETHOD_FiFo = X_Ref_C_AcctSchema_Costing_Method.FI_FO.getValue();
    /** Average Invoice = I */
    public static final String COSTINGMETHOD_AverageInvoice = X_Ref_C_AcctSchema_Costing_Method.AVERAGE_INVOICE.getValue();
    /** LiFo = L */
    public static final String COSTINGMETHOD_LiFo = X_Ref_C_AcctSchema_Costing_Method.LI_FO.getValue();
    /** Standard Costing = S */
    public static final String COSTINGMETHOD_StandardCosting = X_Ref_C_AcctSchema_Costing_Method.STANDARD_COSTING.getValue();
    /** User Defined = U */
    public static final String COSTINGMETHOD_UserDefined = X_Ref_C_AcctSchema_Costing_Method.USER_DEFINED.getValue();
    /** Last Invoice = i */
    public static final String COSTINGMETHOD_LastInvoice = X_Ref_C_AcctSchema_Costing_Method.LAST_INVOICE.getValue();
    /** Last PO Price = p */
    public static final String COSTINGMETHOD_LastPOPrice = X_Ref_C_AcctSchema_Costing_Method.LAST_PO_PRICE.getValue();
    /** _ = x */
    public static final String COSTINGMETHOD__ = X_Ref_C_AcctSchema_Costing_Method._.getValue();
    /** Set Costing Method.
    @param CostingMethod Indicates how Costs will be calculated */
    public void setCostingMethod (String CostingMethod)
    {
        if (!X_Ref_C_AcctSchema_Costing_Method.isValid(CostingMethod))
        throw new IllegalArgumentException ("CostingMethod Invalid value - " + CostingMethod + " - Reference_ID=122 - A - F - I - L - S - U - i - p - x");
        set_Value ("CostingMethod", CostingMethod);
        
    }
    
    /** Get Costing Method.
    @return Indicates how Costs will be calculated */
    public String getCostingMethod() 
    {
        return (String)get_Value("CostingMethod");
        
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
    
    /** Set Calculated.
    @param IsCalculated The value is calculated by the system */
    public void setIsCalculated (boolean IsCalculated)
    {
        set_Value ("IsCalculated", Boolean.valueOf(IsCalculated));
        
    }
    
    /** Get Calculated.
    @return The value is calculated by the system */
    public boolean isCalculated() 
    {
        return get_ValueAsBoolean("IsCalculated");
        
    }
    
    /** Set Manufacturing Material Cost.
    @param IsMfgMaterialCost Indicates that this Cost Element stores Material cost of components required for Manufacturing */
    public void setIsMfgMaterialCost (boolean IsMfgMaterialCost)
    {
        set_Value ("IsMfgMaterialCost", Boolean.valueOf(IsMfgMaterialCost));
        
    }
    
    /** Get Manufacturing Material Cost.
    @return Indicates that this Cost Element stores Material cost of components required for Manufacturing */
    public boolean isMfgMaterialCost() 
    {
        return get_ValueAsBoolean("IsMfgMaterialCost");
        
    }
    
    /** Set Cost Element.
    @param M_CostElement_ID Product Cost Element */
    public void setM_CostElement_ID (int M_CostElement_ID)
    {
        if (M_CostElement_ID < 1) throw new IllegalArgumentException ("M_CostElement_ID is mandatory.");
        set_ValueNoCheck ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
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
