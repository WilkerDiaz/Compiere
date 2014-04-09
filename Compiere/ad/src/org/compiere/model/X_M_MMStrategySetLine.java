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
/** Generated Model for M_MMStrategySetLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_MMStrategySetLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_MMStrategySetLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_MMStrategySetLine_ID id
    @param trx transaction
    */
    public X_M_MMStrategySetLine (Ctx ctx, int M_MMStrategySetLine_ID, Trx trx)
    {
        super (ctx, M_MMStrategySetLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_MMStrategySetLine_ID == 0)
        {
            setM_MMStrategySetLine_ID (0);
            setM_MMStrategySet_ID (0);
            setM_MMStrategy_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_MMStrategySetLine WHERE M_MMStrategySet_ID=@M_MMStrategySet_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_MMStrategySetLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27541768752789L;
    /** Last Updated Timestamp 2009-11-30 20:57:16.0 */
    public static final long updatedMS = 1259643436000L;
    /** AD_Table_ID=1043 */
    public static final int Table_ID=1043;
    
    /** TableName=M_MMStrategySetLine */
    public static final String Table_Name="M_MMStrategySetLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Order Type Group.
    @param C_DocTypeGroup_ID Order Type Groups allows grouping of different standard Sales Order types */
    public void setC_DocTypeGroup_ID (int C_DocTypeGroup_ID)
    {
        if (C_DocTypeGroup_ID <= 0) set_Value ("C_DocTypeGroup_ID", null);
        else
        set_Value ("C_DocTypeGroup_ID", Integer.valueOf(C_DocTypeGroup_ID));
        
    }
    
    /** Get Order Type Group.
    @return Order Type Groups allows grouping of different standard Sales Order types */
    public int getC_DocTypeGroup_ID() 
    {
        return get_ValueAsInt("C_DocTypeGroup_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Material Management Strategy Set Line.
    @param M_MMStrategySetLine_ID Selection criteria to be used in the strategy set */
    public void setM_MMStrategySetLine_ID (int M_MMStrategySetLine_ID)
    {
        if (M_MMStrategySetLine_ID < 1) throw new IllegalArgumentException ("M_MMStrategySetLine_ID is mandatory.");
        set_ValueNoCheck ("M_MMStrategySetLine_ID", Integer.valueOf(M_MMStrategySetLine_ID));
        
    }
    
    /** Get Material Management Strategy Set Line.
    @return Selection criteria to be used in the strategy set */
    public int getM_MMStrategySetLine_ID() 
    {
        return get_ValueAsInt("M_MMStrategySetLine_ID");
        
    }
    
    /** Set Material Management Strategy Set.
    @param M_MMStrategySet_ID Group of selection criteria to be used in the putaway and picking processes */
    public void setM_MMStrategySet_ID (int M_MMStrategySet_ID)
    {
        if (M_MMStrategySet_ID < 1) throw new IllegalArgumentException ("M_MMStrategySet_ID is mandatory.");
        set_ValueNoCheck ("M_MMStrategySet_ID", Integer.valueOf(M_MMStrategySet_ID));
        
    }
    
    /** Get Material Management Strategy Set.
    @return Group of selection criteria to be used in the putaway and picking processes */
    public int getM_MMStrategySet_ID() 
    {
        return get_ValueAsInt("M_MMStrategySet_ID");
        
    }
    
    /** Set Warehouse Management Strategy.
    @param M_MMStrategy_ID Sequential group of rules used for picking or putaway */
    public void setM_MMStrategy_ID (int M_MMStrategy_ID)
    {
        if (M_MMStrategy_ID < 1) throw new IllegalArgumentException ("M_MMStrategy_ID is mandatory.");
        set_Value ("M_MMStrategy_ID", Integer.valueOf(M_MMStrategy_ID));
        
    }
    
    /** Get Warehouse Management Strategy.
    @return Sequential group of rules used for picking or putaway */
    public int getM_MMStrategy_ID() 
    {
        return get_ValueAsInt("M_MMStrategy_ID");
        
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
    
    /** Set Zone.
    @param M_Zone_ID Warehouse zone */
    public void setM_Zone_ID (int M_Zone_ID)
    {
        if (M_Zone_ID <= 0) set_Value ("M_Zone_ID", null);
        else
        set_Value ("M_Zone_ID", Integer.valueOf(M_Zone_ID));
        
    }
    
    /** Get Zone.
    @return Warehouse zone */
    public int getM_Zone_ID() 
    {
        return get_ValueAsInt("M_Zone_ID");
        
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
    
    
}
