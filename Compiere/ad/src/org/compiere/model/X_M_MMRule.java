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
/** Generated Model for M_MMRule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_MMRule.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_MMRule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_MMRule_ID id
    @param trx transaction
    */
    public X_M_MMRule (Ctx ctx, int M_MMRule_ID, Trx trx)
    {
        super (ctx, M_MMRule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_MMRule_ID == 0)
        {
            setIsMaintainUOMIntegrity (false);
            setMMType (null);
            setM_MMRule_ID (0);
            setM_Warehouse_ID (0);
            setName (null);
            setRule (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_MMRule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27503879835789L;
    /** Last Updated Timestamp 2008-09-18 09:15:19.0 */
    public static final long updatedMS = 1221754519000L;
    /** AD_Table_ID=1039 */
    public static final int Table_ID=1039;
    
    /** TableName=M_MMRule */
    public static final String Table_Name="M_MMRule";
    
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
    
    /** Set Maintain UOM Integrity.
    @param IsMaintainUOMIntegrity This checkbox can be used to restrict the locator selection based on the stocking UOM and picking UOM of the locator */
    public void setIsMaintainUOMIntegrity (boolean IsMaintainUOMIntegrity)
    {
        set_Value ("IsMaintainUOMIntegrity", Boolean.valueOf(IsMaintainUOMIntegrity));
        
    }
    
    /** Get Maintain UOM Integrity.
    @return This checkbox can be used to restrict the locator selection based on the stocking UOM and picking UOM of the locator */
    public boolean isMaintainUOMIntegrity() 
    {
        return get_ValueAsBoolean("IsMaintainUOMIntegrity");
        
    }
    
    /** Material Picking = PCK */
    public static final String MMTYPE_MaterialPicking = X_Ref_TaskType.MATERIAL_PICKING.getValue();
    /** Material Putaway = PUT */
    public static final String MMTYPE_MaterialPutaway = X_Ref_TaskType.MATERIAL_PUTAWAY.getValue();
    /** Set Type.
    @param MMType Warehouse Management Rule Type */
    public void setMMType (String MMType)
    {
        if (MMType == null) throw new IllegalArgumentException ("MMType is mandatory");
        if (!X_Ref_TaskType.isValid(MMType))
        throw new IllegalArgumentException ("MMType Invalid value - " + MMType + " - Reference_ID=455 - PCK - PUT");
        set_Value ("MMType", MMType);
        
    }
    
    /** Get Type.
    @return Warehouse Management Rule Type */
    public String getMMType() 
    {
        return (String)get_Value("MMType");
        
    }
    
    /** Set Warehouse Management Rule.
    @param M_MMRule_ID Rule to determine the putaway or pick location for goods stocked in the warehouse */
    public void setM_MMRule_ID (int M_MMRule_ID)
    {
        if (M_MMRule_ID < 1) throw new IllegalArgumentException ("M_MMRule_ID is mandatory.");
        set_ValueNoCheck ("M_MMRule_ID", Integer.valueOf(M_MMRule_ID));
        
    }
    
    /** Get Warehouse Management Rule.
    @return Rule to determine the putaway or pick location for goods stocked in the warehouse */
    public int getM_MMRule_ID() 
    {
        return get_ValueAsInt("M_MMRule_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Find a fixed locator = MMA */
    public static final String RULE_FindAFixedLocator = X_Ref_Material_Management_Rule.FIND_A_FIXED_LOCATOR.getValue();
    /** Find a floating locator with the same product = MMB */
    public static final String RULE_FindAFloatingLocatorWithTheSameProduct = X_Ref_Material_Management_Rule.FIND_A_FLOATING_LOCATOR_WITH_THE_SAME_PRODUCT.getValue();
    /** Find any locator with available capacity = MMC */
    public static final String RULE_FindAnyLocatorWithAvailableCapacity = X_Ref_Material_Management_Rule.FIND_ANY_LOCATOR_WITH_AVAILABLE_CAPACITY.getValue();
    /** Find item in the warehouse = MMD */
    public static final String RULE_FindItemInTheWarehouse = X_Ref_Material_Management_Rule.FIND_ITEM_IN_THE_WAREHOUSE.getValue();
    /** Find item in the warehouse (Pick to clean) = MME */
    public static final String RULE_FindItemInTheWarehousePickToClean = X_Ref_Material_Management_Rule.FIND_ITEM_IN_THE_WAREHOUSE_PICK_TO_CLEAN.getValue();
    /** Find any empty floating locator = MMF */
    public static final String RULE_FindAnyEmptyFloatingLocator = X_Ref_Material_Management_Rule.FIND_ANY_EMPTY_FLOATING_LOCATOR.getValue();
    /** Hard allocate purchased to order products = MMH */
    public static final String RULE_HardAllocatePurchasedToOrderProducts = X_Ref_Material_Management_Rule.HARD_ALLOCATE_PURCHASED_TO_ORDER_PRODUCTS.getValue();
    /** Use custom class = MMZ */
    public static final String RULE_UseCustomClass = X_Ref_Material_Management_Rule.USE_CUSTOM_CLASS.getValue();
    /** Set Rule.
    @param Rule Indicates which locators will qualify  */
    public void setRule (String Rule)
    {
        if (Rule == null) throw new IllegalArgumentException ("Rule is mandatory");
        if (!X_Ref_Material_Management_Rule.isValid(Rule))
        throw new IllegalArgumentException ("Rule Invalid value - " + Rule + " - Reference_ID=456 - MMA - MMB - MMC - MMD - MME - MMF - MMH - MMZ");
        set_Value ("Rule", Rule);
        
    }
    
    /** Get Rule.
    @return Indicates which locators will qualify  */
    public String getRule() 
    {
        return (String)get_Value("Rule");
        
    }
    
    /** Set Custom Class.
    @param RuleClass Custom java class for Warehouse Management Rule */
    public void setRuleClass (String RuleClass)
    {
        set_Value ("RuleClass", RuleClass);
        
    }
    
    /** Get Custom Class.
    @return Custom java class for Warehouse Management Rule */
    public String getRuleClass() 
    {
        return (String)get_Value("RuleClass");
        
    }
    
    
}
