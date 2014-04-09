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
/** Generated Model for AD_Tree
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Tree.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Tree extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Tree_ID id
    @param trx transaction
    */
    public X_AD_Tree (Ctx ctx, int AD_Tree_ID, Trx trx)
    {
        super (ctx, AD_Tree_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Tree_ID == 0)
        {
            setAD_Table_ID (0);
            setAD_Tree_ID (0);
            setIsAllNodes (false);
            setIsDefault (false);	// N
            setName (null);
            setTreeType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Tree (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27498372559789L;
    /** Last Updated Timestamp 2008-07-16 15:27:23.0 */
    public static final long updatedMS = 1216247243000L;
    /** AD_Table_ID=288 */
    public static final int Table_ID=288;
    
    /** TableName=AD_Tree */
    public static final String Table_Name="AD_Tree";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Tree.
    @param AD_Tree_ID Identifies a Tree */
    public void setAD_Tree_ID (int AD_Tree_ID)
    {
        if (AD_Tree_ID < 1) throw new IllegalArgumentException ("AD_Tree_ID is mandatory.");
        set_ValueNoCheck ("AD_Tree_ID", Integer.valueOf(AD_Tree_ID));
        
    }
    
    /** Get Tree.
    @return Identifies a Tree */
    public int getAD_Tree_ID() 
    {
        return get_ValueAsInt("AD_Tree_ID");
        
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
    
    /** Set All Nodes.
    @param IsAllNodes All Nodes are included (Complete Tree) */
    public void setIsAllNodes (boolean IsAllNodes)
    {
        set_Value ("IsAllNodes", Boolean.valueOf(IsAllNodes));
        
    }
    
    /** Get All Nodes.
    @return All Nodes are included (Complete Tree) */
    public boolean isAllNodes() 
    {
        return get_ValueAsBoolean("IsAllNodes");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
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
    
    /** Activity = AY */
    public static final String TREETYPE_Activity = X_Ref_AD_TreeType_Type.ACTIVITY.getValue();
    /** BoM = BB */
    public static final String TREETYPE_BoM = X_Ref_AD_TreeType_Type.BO_M.getValue();
    /** BPartner = BP */
    public static final String TREETYPE_BPartner = X_Ref_AD_TreeType_Type.B_PARTNER.getValue();
    /** CM Container = CC */
    public static final String TREETYPE_CMContainer = X_Ref_AD_TreeType_Type.CM_CONTAINER.getValue();
    /** CM Media = CM */
    public static final String TREETYPE_CMMedia = X_Ref_AD_TreeType_Type.CM_MEDIA.getValue();
    /** CM Container Stage = CS */
    public static final String TREETYPE_CMContainerStage = X_Ref_AD_TreeType_Type.CM_CONTAINER_STAGE.getValue();
    /** CM Template = CT */
    public static final String TREETYPE_CMTemplate = X_Ref_AD_TreeType_Type.CM_TEMPLATE.getValue();
    /** Element Value = EV */
    public static final String TREETYPE_ElementValue = X_Ref_AD_TreeType_Type.ELEMENT_VALUE.getValue();
    /** Campaign = MC */
    public static final String TREETYPE_Campaign = X_Ref_AD_TreeType_Type.CAMPAIGN.getValue();
    /** Menu = MM */
    public static final String TREETYPE_Menu = X_Ref_AD_TreeType_Type.MENU.getValue();
    /** Organization = OO */
    public static final String TREETYPE_Organization = X_Ref_AD_TreeType_Type.ORGANIZATION.getValue();
    /** Product Category = PC */
    public static final String TREETYPE_ProductCategory = X_Ref_AD_TreeType_Type.PRODUCT_CATEGORY.getValue();
    /** Project = PJ */
    public static final String TREETYPE_Project = X_Ref_AD_TreeType_Type.PROJECT.getValue();
    /** Product = PR */
    public static final String TREETYPE_Product = X_Ref_AD_TreeType_Type.PRODUCT.getValue();
    /** Sales Region = SR */
    public static final String TREETYPE_SalesRegion = X_Ref_AD_TreeType_Type.SALES_REGION.getValue();
    /** User 1 = U1 */
    public static final String TREETYPE_User1 = X_Ref_AD_TreeType_Type.USER1.getValue();
    /** User 2 = U2 */
    public static final String TREETYPE_User2 = X_Ref_AD_TreeType_Type.USER2.getValue();
    /** User 3 = U3 */
    public static final String TREETYPE_User3 = X_Ref_AD_TreeType_Type.USER3.getValue();
    /** User 4 = U4 */
    public static final String TREETYPE_User4 = X_Ref_AD_TreeType_Type.USER4.getValue();
    /** Other = XX */
    public static final String TREETYPE_Other = X_Ref_AD_TreeType_Type.OTHER.getValue();
    /** Set Type | Area.
    @param TreeType Element this tree is built on (i.e. Product, Business Partner) */
    public void setTreeType (String TreeType)
    {
        if (TreeType == null) throw new IllegalArgumentException ("TreeType is mandatory");
        if (!X_Ref_AD_TreeType_Type.isValid(TreeType))
        throw new IllegalArgumentException ("TreeType Invalid value - " + TreeType + " - Reference_ID=120 - AY - BB - BP - CC - CM - CS - CT - EV - MC - MM - OO - PC - PJ - PR - SR - U1 - U2 - U3 - U4 - XX");
        set_ValueNoCheck ("TreeType", TreeType);
        
    }
    
    /** Get Type | Area.
    @return Element this tree is built on (i.e. Product, Business Partner) */
    public String getTreeType() 
    {
        return (String)get_Value("TreeType");
        
    }
    
    
}
