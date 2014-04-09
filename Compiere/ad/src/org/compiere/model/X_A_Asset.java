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
/** Generated Model for A_Asset
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_A_Asset.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_A_Asset extends PO
{
    /** Standard Constructor
    @param ctx context
    @param A_Asset_ID id
    @param trx transaction
    */
    public X_A_Asset (Ctx ctx, int A_Asset_ID, Trx trx)
    {
        super (ctx, A_Asset_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (A_Asset_ID == 0)
        {
            setA_Asset_Group_ID (0);
            setA_Asset_ID (0);
            setIsDepreciated (false);
            setIsDisposed (false);
            setIsFullyDepreciated (false);	// N
            setIsInPosession (false);
            setIsOwned (false);
            setName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_A_Asset (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27506909532789L;
    /** Last Updated Timestamp 2008-10-23 10:50:16.0 */
    public static final long updatedMS = 1224784216000L;
    /** AD_Table_ID=539 */
    public static final int Table_ID=539;
    
    /** TableName=A_Asset */
    public static final String Table_Name="A_Asset";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Asset Group.
    @param A_Asset_Group_ID Group of Assets */
    public void setA_Asset_Group_ID (int A_Asset_Group_ID)
    {
        if (A_Asset_Group_ID < 1) throw new IllegalArgumentException ("A_Asset_Group_ID is mandatory.");
        set_Value ("A_Asset_Group_ID", Integer.valueOf(A_Asset_Group_ID));
        
    }
    
    /** Get Asset Group.
    @return Group of Assets */
    public int getA_Asset_Group_ID() 
    {
        return get_ValueAsInt("A_Asset_Group_ID");
        
    }
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID < 1) throw new IllegalArgumentException ("A_Asset_ID is mandatory.");
        set_ValueNoCheck ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Asset Depreciation Date.
    @param AssetDepreciationDate Date of last depreciation */
    public void setAssetDepreciationDate (Timestamp AssetDepreciationDate)
    {
        set_Value ("AssetDepreciationDate", AssetDepreciationDate);
        
    }
    
    /** Get Asset Depreciation Date.
    @return Date of last depreciation */
    public Timestamp getAssetDepreciationDate() 
    {
        return (Timestamp)get_Value("AssetDepreciationDate");
        
    }
    
    /** Set Asset Disposal Date.
    @param AssetDisposalDate Date when the asset is/was disposed */
    public void setAssetDisposalDate (Timestamp AssetDisposalDate)
    {
        set_Value ("AssetDisposalDate", AssetDisposalDate);
        
    }
    
    /** Get Asset Disposal Date.
    @return Date when the asset is/was disposed */
    public Timestamp getAssetDisposalDate() 
    {
        return (Timestamp)get_Value("AssetDisposalDate");
        
    }
    
    /** Set In Service Date.
    @param AssetServiceDate Date when Asset was put into service */
    public void setAssetServiceDate (Timestamp AssetServiceDate)
    {
        set_Value ("AssetServiceDate", AssetServiceDate);
        
    }
    
    /** Get In Service Date.
    @return Date when Asset was put into service */
    public Timestamp getAssetServiceDate() 
    {
        return (Timestamp)get_Value("AssetServiceDate");
        
    }
    
    /** Set Agent.
    @param C_BPartnerSR_ID Business Partner (Agent or Sales Rep) */
    public void setC_BPartnerSR_ID (int C_BPartnerSR_ID)
    {
        if (C_BPartnerSR_ID <= 0) set_Value ("C_BPartnerSR_ID", null);
        else
        set_Value ("C_BPartnerSR_ID", Integer.valueOf(C_BPartnerSR_ID));
        
    }
    
    /** Get Agent.
    @return Business Partner (Agent or Sales Rep) */
    public int getC_BPartnerSR_ID() 
    {
        return get_ValueAsInt("C_BPartnerSR_ID");
        
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
        else
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Address.
    @param C_Location_ID Location or Address */
    public void setC_Location_ID (int C_Location_ID)
    {
        if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
        else
        set_Value ("C_Location_ID", Integer.valueOf(C_Location_ID));
        
    }
    
    /** Get Address.
    @return Location or Address */
    public int getC_Location_ID() 
    {
        return get_ValueAsInt("C_Location_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
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
    
    /** Set Expiration Date.
    @param GuaranteeDate Date when guarantee expires */
    public void setGuaranteeDate (Timestamp GuaranteeDate)
    {
        set_Value ("GuaranteeDate", GuaranteeDate);
        
    }
    
    /** Get Expiration Date.
    @return Date when guarantee expires */
    public Timestamp getGuaranteeDate() 
    {
        return (Timestamp)get_Value("GuaranteeDate");
        
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
    
    /** Set Depreciate.
    @param IsDepreciated The asset will be depreciated */
    public void setIsDepreciated (boolean IsDepreciated)
    {
        set_Value ("IsDepreciated", Boolean.valueOf(IsDepreciated));
        
    }
    
    /** Get Depreciate.
    @return The asset will be depreciated */
    public boolean isDepreciated() 
    {
        return get_ValueAsBoolean("IsDepreciated");
        
    }
    
    /** Set Disposed.
    @param IsDisposed The asset is disposed */
    public void setIsDisposed (boolean IsDisposed)
    {
        set_Value ("IsDisposed", Boolean.valueOf(IsDisposed));
        
    }
    
    /** Get Disposed.
    @return The asset is disposed */
    public boolean isDisposed() 
    {
        return get_ValueAsBoolean("IsDisposed");
        
    }
    
    /** Set Fully depreciated.
    @param IsFullyDepreciated The asset is fully depreciated */
    public void setIsFullyDepreciated (boolean IsFullyDepreciated)
    {
        set_ValueNoCheck ("IsFullyDepreciated", Boolean.valueOf(IsFullyDepreciated));
        
    }
    
    /** Get Fully depreciated.
    @return The asset is fully depreciated */
    public boolean isFullyDepreciated() 
    {
        return get_ValueAsBoolean("IsFullyDepreciated");
        
    }
    
    /** Set In Possession.
    @param IsInPosession The asset is in the possession of the organization */
    public void setIsInPosession (boolean IsInPosession)
    {
        set_Value ("IsInPosession", Boolean.valueOf(IsInPosession));
        
    }
    
    /** Get In Possession.
    @return The asset is in the possession of the organization */
    public boolean isInPosession() 
    {
        return get_ValueAsBoolean("IsInPosession");
        
    }
    
    /** Set Owned.
    @param IsOwned The asset is owned by the organization */
    public void setIsOwned (boolean IsOwned)
    {
        set_Value ("IsOwned", Boolean.valueOf(IsOwned));
        
    }
    
    /** Get Owned.
    @return The asset is owned by the organization */
    public boolean isOwned() 
    {
        return get_ValueAsBoolean("IsOwned");
        
    }
    
    /** Set TrialPhase.
    @param IsTrialPhase This is a trial phase */
    public void setIsTrialPhase (boolean IsTrialPhase)
    {
        set_Value ("IsTrialPhase", Boolean.valueOf(IsTrialPhase));
        
    }
    
    /** Get TrialPhase.
    @return This is a trial phase */
    public boolean isTrialPhase() 
    {
        return get_ValueAsBoolean("IsTrialPhase");
        
    }
    
    /** Set Last Maintenance.
    @param LastMaintenanceDate Last Maintenance Date */
    public void setLastMaintenanceDate (Timestamp LastMaintenanceDate)
    {
        set_Value ("LastMaintenanceDate", LastMaintenanceDate);
        
    }
    
    /** Get Last Maintenance.
    @return Last Maintenance Date */
    public Timestamp getLastMaintenanceDate() 
    {
        return (Timestamp)get_Value("LastMaintenanceDate");
        
    }
    
    /** Set Last Note.
    @param LastMaintenanceNote Last Maintenance Note */
    public void setLastMaintenanceNote (String LastMaintenanceNote)
    {
        set_Value ("LastMaintenanceNote", LastMaintenanceNote);
        
    }
    
    /** Get Last Note.
    @return Last Maintenance Note */
    public String getLastMaintenanceNote() 
    {
        return (String)get_Value("LastMaintenanceNote");
        
    }
    
    /** Set Last Unit.
    @param LastMaintenanceUnit Last Maintenance Unit */
    public void setLastMaintenanceUnit (int LastMaintenanceUnit)
    {
        set_Value ("LastMaintenanceUnit", Integer.valueOf(LastMaintenanceUnit));
        
    }
    
    /** Get Last Unit.
    @return Last Maintenance Unit */
    public int getLastMaintenanceUnit() 
    {
        return get_ValueAsInt("LastMaintenanceUnit");
        
    }
    
    /** Set Lease Termination.
    @param LeaseTerminationDate Lease Termination Date */
    public void setLeaseTerminationDate (Timestamp LeaseTerminationDate)
    {
        set_Value ("LeaseTerminationDate", LeaseTerminationDate);
        
    }
    
    /** Get Lease Termination.
    @return Lease Termination Date */
    public Timestamp getLeaseTerminationDate() 
    {
        return (Timestamp)get_Value("LeaseTerminationDate");
        
    }
    
    /** Set Lessor.
    @param Lease_BPartner_ID The Business Partner who rents or leases */
    public void setLease_BPartner_ID (int Lease_BPartner_ID)
    {
        if (Lease_BPartner_ID <= 0) set_Value ("Lease_BPartner_ID", null);
        else
        set_Value ("Lease_BPartner_ID", Integer.valueOf(Lease_BPartner_ID));
        
    }
    
    /** Get Lessor.
    @return The Business Partner who rents or leases */
    public int getLease_BPartner_ID() 
    {
        return get_ValueAsInt("Lease_BPartner_ID");
        
    }
    
    /** Set Life use Units.
    @param LifeUseUnits Units of use until the asset is not usable anymore */
    public void setLifeUseUnits (java.math.BigDecimal LifeUseUnits)
    {
        set_Value ("LifeUseUnits", LifeUseUnits);
        
    }
    
    /** Get Life use Units.
    @return Units of use until the asset is not usable anymore */
    public java.math.BigDecimal getLifeUseUnits() 
    {
        return get_ValueAsBigDecimal("LifeUseUnits");
        
    }
    
    /** Set Location comment.
    @param LocationComment Additional comments or remarks concerning the location */
    public void setLocationComment (String LocationComment)
    {
        set_Value ("LocationComment", LocationComment);
        
    }
    
    /** Get Location comment.
    @return Additional comments or remarks concerning the location */
    public String getLocationComment() 
    {
        return (String)get_Value("LocationComment");
        
    }
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_Value ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_ValueNoCheck ("M_AttributeSetInstance_ID", null);
        else
        set_ValueNoCheck ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID <= 0) set_Value ("M_InOutLine_ID", null);
        else
        set_Value ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Next Maintenance.
    @param NextMaintenenceDate Next Maintenance Date */
    public void setNextMaintenenceDate (Timestamp NextMaintenenceDate)
    {
        set_Value ("NextMaintenenceDate", NextMaintenenceDate);
        
    }
    
    /** Get Next Maintenance.
    @return Next Maintenance Date */
    public Timestamp getNextMaintenenceDate() 
    {
        return (Timestamp)get_Value("NextMaintenenceDate");
        
    }
    
    /** Set Next Unit.
    @param NextMaintenenceUnit Next Maintenance Unit */
    public void setNextMaintenenceUnit (int NextMaintenenceUnit)
    {
        set_Value ("NextMaintenenceUnit", Integer.valueOf(NextMaintenenceUnit));
        
    }
    
    /** Get Next Unit.
    @return Next Maintenance Unit */
    public int getNextMaintenenceUnit() 
    {
        return get_ValueAsInt("NextMaintenenceUnit");
        
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
    
    /** Set Serial No.
    @param SerNo Product Serial Number */
    public void setSerNo (String SerNo)
    {
        set_Value ("SerNo", SerNo);
        
    }
    
    /** Get Serial No.
    @return Product Serial Number */
    public String getSerNo() 
    {
        return (String)get_Value("SerNo");
        
    }
    
    /** Evaluation = E */
    public static final String SYSTEMSTATUS_Evaluation = X_Ref_AD_System_Status.EVALUATION.getValue();
    /** Implementation = I */
    public static final String SYSTEMSTATUS_Implementation = X_Ref_AD_System_Status.IMPLEMENTATION.getValue();
    /** Production = P */
    public static final String SYSTEMSTATUS_Production = X_Ref_AD_System_Status.PRODUCTION.getValue();
    /** Set System Status.
    @param SystemStatus Status of the system - Support priority depends on system status */
    public void setSystemStatus (String SystemStatus)
    {
        if (!X_Ref_AD_System_Status.isValid(SystemStatus))
        throw new IllegalArgumentException ("SystemStatus Invalid value - " + SystemStatus + " - Reference_ID=374 - E - I - P");
        set_Value ("SystemStatus", SystemStatus);
        
    }
    
    /** Get System Status.
    @return Status of the system - Support priority depends on system status */
    public String getSystemStatus() 
    {
        return (String)get_Value("SystemStatus");
        
    }
    
    /** Set Usable Life - Months.
    @param UseLifeMonths Months of the usable life of the asset */
    public void setUseLifeMonths (int UseLifeMonths)
    {
        set_Value ("UseLifeMonths", Integer.valueOf(UseLifeMonths));
        
    }
    
    /** Get Usable Life - Months.
    @return Months of the usable life of the asset */
    public int getUseLifeMonths() 
    {
        return get_ValueAsInt("UseLifeMonths");
        
    }
    
    /** Set Usable Life - Years.
    @param UseLifeYears Years of the usable life of the asset */
    public void setUseLifeYears (int UseLifeYears)
    {
        set_Value ("UseLifeYears", Integer.valueOf(UseLifeYears));
        
    }
    
    /** Get Usable Life - Years.
    @return Years of the usable life of the asset */
    public int getUseLifeYears() 
    {
        return get_ValueAsInt("UseLifeYears");
        
    }
    
    /** Set Use Units.
    @param UseUnits Used units of the assets */
    public void setUseUnits (java.math.BigDecimal UseUnits)
    {
        set_ValueNoCheck ("UseUnits", UseUnits);
        
    }
    
    /** Get Use Units.
    @return Used units of the assets */
    public java.math.BigDecimal getUseUnits() 
    {
        return get_ValueAsBigDecimal("UseUnits");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Version No.
    @param VersionNo Version Number */
    public void setVersionNo (String VersionNo)
    {
        set_Value ("VersionNo", VersionNo);
        
    }
    
    /** Get Version No.
    @return Version Number */
    public String getVersionNo() 
    {
        return (String)get_Value("VersionNo");
        
    }
    
    
}
