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
/** Generated Model for AD_ComponentReg
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ComponentReg.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ComponentReg extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ComponentReg_ID id
    @param trx transaction
    */
    public X_AD_ComponentReg (Ctx ctx, int AD_ComponentReg_ID, Trx trx)
    {
        super (ctx, AD_ComponentReg_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ComponentReg_ID == 0)
        {
            setAD_ComponentReg_ID (0);
            setAD_User_ID (0);
            setC_BPartner_ID (0);
            setComponentName (null);
            setComponentType (null);	// A
            setDistributionType (null);
            setIsApproved (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ComponentReg (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27497147669789L;
    /** Last Updated Timestamp 2008-07-02 11:12:33.0 */
    public static final long updatedMS = 1215022353000L;
    /** AD_Table_ID=1001 */
    public static final int Table_ID=1001;
    
    /** TableName=AD_ComponentReg */
    public static final String Table_Name="AD_ComponentReg";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Component Registration.
    @param AD_ComponentReg_ID Component Registration */
    public void setAD_ComponentReg_ID (int AD_ComponentReg_ID)
    {
        if (AD_ComponentReg_ID < 1) throw new IllegalArgumentException ("AD_ComponentReg_ID is mandatory.");
        set_ValueNoCheck ("AD_ComponentReg_ID", Integer.valueOf(AD_ComponentReg_ID));
        
    }
    
    /** Get Component Registration.
    @return Component Registration */
    public int getAD_ComponentReg_ID() 
    {
        return get_ValueAsInt("AD_ComponentReg_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Approval Comment.
    @param ApprovalComment Comment about approval */
    public void setApprovalComment (String ApprovalComment)
    {
        set_Value ("ApprovalComment", ApprovalComment);
        
    }
    
    /** Get Approval Comment.
    @return Comment about approval */
    public String getApprovalComment() 
    {
        return (String)get_Value("ApprovalComment");
        
    }
    
    /** Set BinaryData.
    @param BinaryData Binary Data */
    public void setBinaryData (byte[] BinaryData)
    {
        set_Value ("BinaryData", BinaryData);
        
    }
    
    /** Get BinaryData.
    @return Binary Data */
    public byte[] getBinaryData() 
    {
        return (byte[])get_Value("BinaryData");
        
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
    
    /** Set Component Name.
    @param ComponentName Entity Type or Component Name */
    public void setComponentName (String ComponentName)
    {
        if (ComponentName == null) throw new IllegalArgumentException ("ComponentName is mandatory.");
        set_Value ("ComponentName", ComponentName);
        
    }
    
    /** Get Component Name.
    @return Entity Type or Component Name */
    public String getComponentName() 
    {
        return (String)get_Value("ComponentName");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getComponentName());
        
    }
    
    /** Application Component = A */
    public static final String COMPONENTTYPE_ApplicationComponent = X_Ref_AD_ComponentReg_Type.APPLICATION_COMPONENT.getValue();
    /** Core Dictionary = C */
    public static final String COMPONENTTYPE_CoreDictionary = X_Ref_AD_ComponentReg_Type.CORE_DICTIONARY.getValue();
    /** Data Component = D */
    public static final String COMPONENTTYPE_DataComponent = X_Ref_AD_ComponentReg_Type.DATA_COMPONENT.getValue();
    /** Translation = T */
    public static final String COMPONENTTYPE_Translation = X_Ref_AD_ComponentReg_Type.TRANSLATION.getValue();
    /** Set Component Type.
    @param ComponentType Type of Component */
    public void setComponentType (String ComponentType)
    {
        if (ComponentType == null) throw new IllegalArgumentException ("ComponentType is mandatory");
        if (!X_Ref_AD_ComponentReg_Type.isValid(ComponentType))
        throw new IllegalArgumentException ("ComponentType Invalid value - " + ComponentType + " - Reference_ID=437 - A - C - D - T");
        set_Value ("ComponentType", ComponentType);
        
    }
    
    /** Get Component Type.
    @return Type of Component */
    public String getComponentType() 
    {
        return (String)get_Value("ComponentType");
        
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
    
    /** Business Partner (User) = B */
    public static final String DISTRIBUTIONTYPE_BusinessPartnerUser = X_Ref_AD_Component_Distribution.BUSINESS_PARTNER_USER.getValue();
    /** Free - Unlimited = F */
    public static final String DISTRIBUTIONTYPE_Free_Unlimited = X_Ref_AD_Component_Distribution.FREE__UNLIMITED.getValue();
    /** License = L */
    public static final String DISTRIBUTIONTYPE_License = X_Ref_AD_Component_Distribution.LICENSE.getValue();
    /** Business Partner Relation = R */
    public static final String DISTRIBUTIONTYPE_BusinessPartnerRelation = X_Ref_AD_Component_Distribution.BUSINESS_PARTNER_RELATION.getValue();
    /** Set Distribution Type.
    @param DistributionType Type of Distribution */
    public void setDistributionType (String DistributionType)
    {
        if (DistributionType == null) throw new IllegalArgumentException ("DistributionType is mandatory");
        if (!X_Ref_AD_Component_Distribution.isValid(DistributionType))
        throw new IllegalArgumentException ("DistributionType Invalid value - " + DistributionType + " - Reference_ID=438 - B - F - L - R");
        set_Value ("DistributionType", DistributionType);
        
    }
    
    /** Get Distribution Type.
    @return Type of Distribution */
    public String getDistributionType() 
    {
        return (String)get_Value("DistributionType");
        
    }
    
    /** Set Documentation Text.
    @param DocumentationText Documentation */
    public void setDocumentationText (String DocumentationText)
    {
        set_Value ("DocumentationText", DocumentationText);
        
    }
    
    /** Get Documentation Text.
    @return Documentation */
    public String getDocumentationText() 
    {
        return (String)get_Value("DocumentationText");
        
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
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
    }
    
    /** Set Published.
    @param IsPublished The entry is published and can be viewed */
    public void setIsPublished (boolean IsPublished)
    {
        set_Value ("IsPublished", Boolean.valueOf(IsPublished));
        
    }
    
    /** Get Published.
    @return The entry is published and can be viewed */
    public boolean isPublished() 
    {
        return get_ValueAsBoolean("IsPublished");
        
    }
    
    /** Set Price List Version.
    @param M_PriceList_Version_ID Identifies a unique instance of a Price List */
    public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
    {
        if (M_PriceList_Version_ID <= 0) set_Value ("M_PriceList_Version_ID", null);
        else
        set_Value ("M_PriceList_Version_ID", Integer.valueOf(M_PriceList_Version_ID));
        
    }
    
    /** Get Price List Version.
    @return Identifies a unique instance of a Price List */
    public int getM_PriceList_Version_ID() 
    {
        return get_ValueAsInt("M_PriceList_Version_ID");
        
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
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
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
    
    /** Set Standard Price.
    @param PriceStd Standard Price */
    public void setPriceStd (java.math.BigDecimal PriceStd)
    {
        set_Value ("PriceStd", PriceStd);
        
    }
    
    /** Get Standard Price.
    @return Standard Price */
    public java.math.BigDecimal getPriceStd() 
    {
        return get_ValueAsBigDecimal("PriceStd");
        
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
    
    /** Set Prerequisite Version.
    @param RequireCompiereVersion Prerequisite Compiere Base Version */
    public void setRequireCompiereVersion (String RequireCompiereVersion)
    {
        set_Value ("RequireCompiereVersion", RequireCompiereVersion);
        
    }
    
    /** Get Prerequisite Version.
    @return Prerequisite Compiere Base Version */
    public String getRequireCompiereVersion() 
    {
        return (String)get_Value("RequireCompiereVersion");
        
    }
    
    /** Set Prerequisite Applications.
    @param RequireComponentVersion Prerequisite Applications with optional Version */
    public void setRequireComponentVersion (String RequireComponentVersion)
    {
        set_Value ("RequireComponentVersion", RequireComponentVersion);
        
    }
    
    /** Get Prerequisite Applications.
    @return Prerequisite Applications with optional Version */
    public String getRequireComponentVersion() 
    {
        return (String)get_Value("RequireComponentVersion");
        
    }
    
    /** Set Suggested Price.
    @param SuggestedPrice Suggested Price */
    public void setSuggestedPrice (java.math.BigDecimal SuggestedPrice)
    {
        set_Value ("SuggestedPrice", SuggestedPrice);
        
    }
    
    /** Get Suggested Price.
    @return Suggested Price */
    public java.math.BigDecimal getSuggestedPrice() 
    {
        return get_ValueAsBigDecimal("SuggestedPrice");
        
    }
    
    /** Set Trial Phase Days.
    @param TrialPhaseDays Days for a Trail */
    public void setTrialPhaseDays (int TrialPhaseDays)
    {
        set_Value ("TrialPhaseDays", Integer.valueOf(TrialPhaseDays));
        
    }
    
    /** Get Trial Phase Days.
    @return Days for a Trail */
    public int getTrialPhaseDays() 
    {
        return get_ValueAsInt("TrialPhaseDays");
        
    }
    
    /** Set Version.
    @param Version Version of the table definition */
    public void setVersion (String Version)
    {
        set_Value ("Version", Version);
        
    }
    
    /** Get Version.
    @return Version of the table definition */
    public String getVersion() 
    {
        return (String)get_Value("Version");
        
    }
    
    
}
