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
/** Generated Model for PA_GoalRestriction
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_GoalRestriction.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_GoalRestriction extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_GoalRestriction_ID id
    @param trx transaction
    */
    public X_PA_GoalRestriction (Ctx ctx, int PA_GoalRestriction_ID, Trx trx)
    {
        super (ctx, PA_GoalRestriction_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_GoalRestriction_ID == 0)
        {
            setGoalRestrictionType (null);
            setName (null);
            setPA_GoalRestriction_ID (0);
            setPA_Goal_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_GoalRestriction (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=832 */
    public static final int Table_ID=832;
    
    /** TableName=PA_GoalRestriction */
    public static final String Table_Name="PA_GoalRestriction";
    
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
    
    /** Business Partner = B */
    public static final String GOALRESTRICTIONTYPE_BusinessPartner = X_Ref_PA_Goal_Restriction_Type.BUSINESS_PARTNER.getValue();
    /** Product Category = C */
    public static final String GOALRESTRICTIONTYPE_ProductCategory = X_Ref_PA_Goal_Restriction_Type.PRODUCT_CATEGORY.getValue();
    /** Bus.Partner Group = G */
    public static final String GOALRESTRICTIONTYPE_BusPartnerGroup = X_Ref_PA_Goal_Restriction_Type.BUS_PARTNER_GROUP.getValue();
    /** Organization = O */
    public static final String GOALRESTRICTIONTYPE_Organization = X_Ref_PA_Goal_Restriction_Type.ORGANIZATION.getValue();
    /** Product = P */
    public static final String GOALRESTRICTIONTYPE_Product = X_Ref_PA_Goal_Restriction_Type.PRODUCT.getValue();
    /** Set Restriction Type.
    @param GoalRestrictionType Goal Restriction Type */
    public void setGoalRestrictionType (String GoalRestrictionType)
    {
        if (GoalRestrictionType == null) throw new IllegalArgumentException ("GoalRestrictionType is mandatory");
        if (!X_Ref_PA_Goal_Restriction_Type.isValid(GoalRestrictionType))
        throw new IllegalArgumentException ("GoalRestrictionType Invalid value - " + GoalRestrictionType + " - Reference_ID=368 - B - C - G - O - P");
        set_Value ("GoalRestrictionType", GoalRestrictionType);
        
    }
    
    /** Get Restriction Type.
    @return Goal Restriction Type */
    public String getGoalRestrictionType() 
    {
        return (String)get_Value("GoalRestrictionType");
        
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
    
    /** Set Goal Restriction.
    @param PA_GoalRestriction_ID Performance Goal Restriction */
    public void setPA_GoalRestriction_ID (int PA_GoalRestriction_ID)
    {
        if (PA_GoalRestriction_ID < 1) throw new IllegalArgumentException ("PA_GoalRestriction_ID is mandatory.");
        set_ValueNoCheck ("PA_GoalRestriction_ID", Integer.valueOf(PA_GoalRestriction_ID));
        
    }
    
    /** Get Goal Restriction.
    @return Performance Goal Restriction */
    public int getPA_GoalRestriction_ID() 
    {
        return get_ValueAsInt("PA_GoalRestriction_ID");
        
    }
    
    /** Set Goal.
    @param PA_Goal_ID Performance Goal */
    public void setPA_Goal_ID (int PA_Goal_ID)
    {
        if (PA_Goal_ID < 1) throw new IllegalArgumentException ("PA_Goal_ID is mandatory.");
        set_ValueNoCheck ("PA_Goal_ID", Integer.valueOf(PA_Goal_ID));
        
    }
    
    /** Get Goal.
    @return Performance Goal */
    public int getPA_Goal_ID() 
    {
        return get_ValueAsInt("PA_Goal_ID");
        
    }
    
    
}
