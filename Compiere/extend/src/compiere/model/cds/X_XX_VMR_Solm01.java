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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_Solm01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Solm01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Solm01_ID id
    @param trx transaction
    */
    public X_XX_VMR_Solm01 (Ctx ctx, int XX_VMR_Solm01_ID, Trx trx)
    {
        super (ctx, XX_VMR_Solm01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Solm01_ID == 0)
        {
            setM_Product_ID (0);
            setM_Warehouse_ID (0);
            setXX_ApplicationDescription (null);
            setXX_ApplicationStatus (null);
            setXX_ApplicationYear (Env.ZERO);
            setXX_CancellationCode (null);
            setXX_CodeStore2 (Env.ZERO);
            setXX_ConsecutivePrice (Env.ZERO);
            setXX_DayRegistration (Env.ZERO);
            setXX_DayReleaseStatus (Env.ZERO);
            setXX_DayStatus (Env.ZERO);
            setXX_MonthApplication (Env.ZERO);
            setXX_MonthRegister (Env.ZERO);
            setXX_MonthReleaseStatus (Env.ZERO);
            setXX_MonthStatus (Env.ZERO);
            setXX_NumberOfPackages (Env.ZERO);
            setXX_QuantityOfProductStorage (Env.ZERO);
            setXX_ReleaseGuide (null);
            setXX_RequestDay (Env.ZERO);
            setXX_RequestNumber (Env.ZERO);
            setXX_ShopByRoomStatus (null);
            setXX_TypeOfApplication (null);
            setXX_UserCreation (null);
            setXX_UserUpdate (null);
            setXX_VMR_Department_ID (0);
            setXX_VMR_Solm01_ID (0);
            setXX_YearOfReleaseStatus (Env.ZERO);
            setXX_YearRegistration (Env.ZERO);
            setXX_YearStatus (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Solm01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566527177789L;
    /** Last Updated Timestamp 2010-09-13 13:47:41.0 */
    public static final long updatedMS = 1284401861000L;
    /** AD_Table_ID=1000046 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Solm01");
        
    }
    ;
    
    /** TableName=XX_VMR_Solm01 */
    public static final String Table_Name="XX_VMR_Solm01";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set XX_ApplicationDescription.
    @param XX_ApplicationDescription XX_ApplicationDescription */
    public void setXX_ApplicationDescription (String XX_ApplicationDescription)
    {
        if (XX_ApplicationDescription == null) throw new IllegalArgumentException ("XX_ApplicationDescription is mandatory.");
        set_Value ("XX_ApplicationDescription", XX_ApplicationDescription);
        
    }
    
    /** Get XX_ApplicationDescription.
    @return XX_ApplicationDescription */
    public String getXX_ApplicationDescription() 
    {
        return (String)get_Value("XX_ApplicationDescription");
        
    }
    
    /** Set XX_ApplicationStatus.
    @param XX_ApplicationStatus XX_ApplicationStatus */
    public void setXX_ApplicationStatus (String XX_ApplicationStatus)
    {
        if (XX_ApplicationStatus == null) throw new IllegalArgumentException ("XX_ApplicationStatus is mandatory.");
        set_Value ("XX_ApplicationStatus", XX_ApplicationStatus);
        
    }
    
    /** Get XX_ApplicationStatus.
    @return XX_ApplicationStatus */
    public String getXX_ApplicationStatus() 
    {
        return (String)get_Value("XX_ApplicationStatus");
        
    }
    
    /** Set XX_ApplicationYear.
    @param XX_ApplicationYear XX_ApplicationYear */
    public void setXX_ApplicationYear (java.math.BigDecimal XX_ApplicationYear)
    {
        if (XX_ApplicationYear == null) throw new IllegalArgumentException ("XX_ApplicationYear is mandatory.");
        set_Value ("XX_ApplicationYear", XX_ApplicationYear);
        
    }
    
    /** Get XX_ApplicationYear.
    @return XX_ApplicationYear */
    public java.math.BigDecimal getXX_ApplicationYear() 
    {
        return get_ValueAsBigDecimal("XX_ApplicationYear");
        
    }
    
    /** Inactive Business Partner = CBPARTNER */
    public static final String XX_CANCELLATIONCODE_InactiveBusinessPartner = X_Ref_XX_SourceCancellationMotive.INACTIVE_BUSINESS_PARTNER.getValue();
    /** Change Estimated Date Purchase Order = CORDCHANGE */
    public static final String XX_CANCELLATIONCODE_ChangeEstimatedDatePurchaseOrder = X_Ref_XX_SourceCancellationMotive.CHANGE_ESTIMATED_DATE_PURCHASE_ORDER.getValue();
    /** Anull Purchase Order  = CORDER */
    public static final String XX_CANCELLATIONCODE_AnullPurchaseOrder = X_Ref_XX_SourceCancellationMotive.ANULL_PURCHASE_ORDER.getValue();
    /** Entrance Fiscal Data Rejection Motives = ENTRANCE */
    public static final String XX_CANCELLATIONCODE_EntranceFiscalDataRejectionMotives = X_Ref_XX_SourceCancellationMotive.ENTRANCE_FISCAL_DATA_REJECTION_MOTIVES.getValue();
    /** Placed Order Modification Motive = PORDER */
    public static final String XX_CANCELLATIONCODE_PlacedOrderModificationMotive = X_Ref_XX_SourceCancellationMotive.PLACED_ORDER_MODIFICATION_MOTIVE.getValue();
    /** Motives for Return of Products to CD = RETURNCD */
    public static final String XX_CANCELLATIONCODE_MotivesForReturnOfProductsToCD = X_Ref_XX_SourceCancellationMotive.MOTIVES_FOR_RETURN_OF_PRODUCTS_TO_CD.getValue();
    /** Motives for Product Returns  = RETURNM */
    public static final String XX_CANCELLATIONCODE_MotivesForProductReturns = X_Ref_XX_SourceCancellationMotive.MOTIVES_FOR_PRODUCT_RETURNS.getValue();
    /** Motives for Transfer of Products between Stores = TRANSFERCD */
    public static final String XX_CANCELLATIONCODE_MotivesForTransferOfProductsBetweenStores = X_Ref_XX_SourceCancellationMotive.MOTIVES_FOR_TRANSFER_OF_PRODUCTS_BETWEEN_STORES.getValue();
    /** Inactive Vendor Category = VENDORCAT */
    public static final String XX_CANCELLATIONCODE_InactiveVendorCategory = X_Ref_XX_SourceCancellationMotive.INACTIVE_VENDOR_CATEGORY.getValue();
    /** Set XX_CancellationCode.
    @param XX_CancellationCode XX_CancellationCode */
    public void setXX_CancellationCode (String XX_CancellationCode)
    {
        if (XX_CancellationCode == null) throw new IllegalArgumentException ("XX_CancellationCode is mandatory");
        if (!X_Ref_XX_SourceCancellationMotive.isValid(XX_CancellationCode))
        throw new IllegalArgumentException ("XX_CancellationCode Invalid value - " + XX_CancellationCode + " - Reference_ID=1000174 - CBPARTNER - CORDCHANGE - CORDER - ENTRANCE - PORDER - RETURNCD - RETURNM - TRANSFERCD - VENDORCAT");
        set_Value ("XX_CancellationCode", XX_CancellationCode);
        
    }
    
    /** Get XX_CancellationCode.
    @return XX_CancellationCode */
    public String getXX_CancellationCode() 
    {
        return (String)get_Value("XX_CancellationCode");
        
    }
    
    /** Set XX_CodeStore2.
    @param XX_CodeStore2 XX_CodeStore2 */
    public void setXX_CodeStore2 (java.math.BigDecimal XX_CodeStore2)
    {
        if (XX_CodeStore2 == null) throw new IllegalArgumentException ("XX_CodeStore2 is mandatory.");
        set_Value ("XX_CodeStore2", XX_CodeStore2);
        
    }
    
    /** Get XX_CodeStore2.
    @return XX_CodeStore2 */
    public java.math.BigDecimal getXX_CodeStore2() 
    {
        return get_ValueAsBigDecimal("XX_CodeStore2");
        
    }
    
    /** Set Consecutive Price.
    @param XX_ConsecutivePrice Consecutivo de Precio */
    public void setXX_ConsecutivePrice (java.math.BigDecimal XX_ConsecutivePrice)
    {
        if (XX_ConsecutivePrice == null) throw new IllegalArgumentException ("XX_ConsecutivePrice is mandatory.");
        set_Value ("XX_ConsecutivePrice", XX_ConsecutivePrice);
        
    }
    
    /** Get Consecutive Price.
    @return Consecutivo de Precio */
    public java.math.BigDecimal getXX_ConsecutivePrice() 
    {
        return get_ValueAsBigDecimal("XX_ConsecutivePrice");
        
    }
    
    /** Set XX_DayRegistration.
    @param XX_DayRegistration XX_DayRegistration */
    public void setXX_DayRegistration (java.math.BigDecimal XX_DayRegistration)
    {
        if (XX_DayRegistration == null) throw new IllegalArgumentException ("XX_DayRegistration is mandatory.");
        set_Value ("XX_DayRegistration", XX_DayRegistration);
        
    }
    
    /** Get XX_DayRegistration.
    @return XX_DayRegistration */
    public java.math.BigDecimal getXX_DayRegistration() 
    {
        return get_ValueAsBigDecimal("XX_DayRegistration");
        
    }
    
    /** Set XX_DayReleaseStatus.
    @param XX_DayReleaseStatus XX_DayReleaseStatus */
    public void setXX_DayReleaseStatus (java.math.BigDecimal XX_DayReleaseStatus)
    {
        if (XX_DayReleaseStatus == null) throw new IllegalArgumentException ("XX_DayReleaseStatus is mandatory.");
        set_Value ("XX_DayReleaseStatus", XX_DayReleaseStatus);
        
    }
    
    /** Get XX_DayReleaseStatus.
    @return XX_DayReleaseStatus */
    public java.math.BigDecimal getXX_DayReleaseStatus() 
    {
        return get_ValueAsBigDecimal("XX_DayReleaseStatus");
        
    }
    
    /** Set XX_DayStatus.
    @param XX_DayStatus XX_DayStatus */
    public void setXX_DayStatus (java.math.BigDecimal XX_DayStatus)
    {
        if (XX_DayStatus == null) throw new IllegalArgumentException ("XX_DayStatus is mandatory.");
        set_Value ("XX_DayStatus", XX_DayStatus);
        
    }
    
    /** Get XX_DayStatus.
    @return XX_DayStatus */
    public java.math.BigDecimal getXX_DayStatus() 
    {
        return get_ValueAsBigDecimal("XX_DayStatus");
        
    }
    
    /** Set XX_MonthApplication.
    @param XX_MonthApplication XX_MonthApplication */
    public void setXX_MonthApplication (java.math.BigDecimal XX_MonthApplication)
    {
        if (XX_MonthApplication == null) throw new IllegalArgumentException ("XX_MonthApplication is mandatory.");
        set_Value ("XX_MonthApplication", XX_MonthApplication);
        
    }
    
    /** Get XX_MonthApplication.
    @return XX_MonthApplication */
    public java.math.BigDecimal getXX_MonthApplication() 
    {
        return get_ValueAsBigDecimal("XX_MonthApplication");
        
    }
    
    /** Set XX_MonthRegister.
    @param XX_MonthRegister XX_MonthRegister */
    public void setXX_MonthRegister (java.math.BigDecimal XX_MonthRegister)
    {
        if (XX_MonthRegister == null) throw new IllegalArgumentException ("XX_MonthRegister is mandatory.");
        set_Value ("XX_MonthRegister", XX_MonthRegister);
        
    }
    
    /** Get XX_MonthRegister.
    @return XX_MonthRegister */
    public java.math.BigDecimal getXX_MonthRegister() 
    {
        return get_ValueAsBigDecimal("XX_MonthRegister");
        
    }
    
    /** Set XX_MonthReleaseStatus.
    @param XX_MonthReleaseStatus XX_MonthReleaseStatus */
    public void setXX_MonthReleaseStatus (java.math.BigDecimal XX_MonthReleaseStatus)
    {
        if (XX_MonthReleaseStatus == null) throw new IllegalArgumentException ("XX_MonthReleaseStatus is mandatory.");
        set_Value ("XX_MonthReleaseStatus", XX_MonthReleaseStatus);
        
    }
    
    /** Get XX_MonthReleaseStatus.
    @return XX_MonthReleaseStatus */
    public java.math.BigDecimal getXX_MonthReleaseStatus() 
    {
        return get_ValueAsBigDecimal("XX_MonthReleaseStatus");
        
    }
    
    /** Set XX_MonthStatus.
    @param XX_MonthStatus XX_MonthStatus */
    public void setXX_MonthStatus (java.math.BigDecimal XX_MonthStatus)
    {
        if (XX_MonthStatus == null) throw new IllegalArgumentException ("XX_MonthStatus is mandatory.");
        set_Value ("XX_MonthStatus", XX_MonthStatus);
        
    }
    
    /** Get XX_MonthStatus.
    @return XX_MonthStatus */
    public java.math.BigDecimal getXX_MonthStatus() 
    {
        return get_ValueAsBigDecimal("XX_MonthStatus");
        
    }
    
    /** Set XX_NumberOfPackages.
    @param XX_NumberOfPackages XX_NumberOfPackages */
    public void setXX_NumberOfPackages (java.math.BigDecimal XX_NumberOfPackages)
    {
        if (XX_NumberOfPackages == null) throw new IllegalArgumentException ("XX_NumberOfPackages is mandatory.");
        set_Value ("XX_NumberOfPackages", XX_NumberOfPackages);
        
    }
    
    /** Get XX_NumberOfPackages.
    @return XX_NumberOfPackages */
    public java.math.BigDecimal getXX_NumberOfPackages() 
    {
        return get_ValueAsBigDecimal("XX_NumberOfPackages");
        
    }
    
    /** Set XX_QuantityOfProductStorage.
    @param XX_QuantityOfProductStorage XX_QuantityOfProductStorage */
    public void setXX_QuantityOfProductStorage (java.math.BigDecimal XX_QuantityOfProductStorage)
    {
        if (XX_QuantityOfProductStorage == null) throw new IllegalArgumentException ("XX_QuantityOfProductStorage is mandatory.");
        set_Value ("XX_QuantityOfProductStorage", XX_QuantityOfProductStorage);
        
    }
    
    /** Get XX_QuantityOfProductStorage.
    @return XX_QuantityOfProductStorage */
    public java.math.BigDecimal getXX_QuantityOfProductStorage() 
    {
        return get_ValueAsBigDecimal("XX_QuantityOfProductStorage");
        
    }
    
    /** Set XX_ReleaseGuide.
    @param XX_ReleaseGuide XX_ReleaseGuide */
    public void setXX_ReleaseGuide (String XX_ReleaseGuide)
    {
        if (XX_ReleaseGuide == null) throw new IllegalArgumentException ("XX_ReleaseGuide is mandatory.");
        set_Value ("XX_ReleaseGuide", XX_ReleaseGuide);
        
    }
    
    /** Get XX_ReleaseGuide.
    @return XX_ReleaseGuide */
    public String getXX_ReleaseGuide() 
    {
        return (String)get_Value("XX_ReleaseGuide");
        
    }
    
    /** Set XX_RequestDay.
    @param XX_RequestDay XX_RequestDay */
    public void setXX_RequestDay (java.math.BigDecimal XX_RequestDay)
    {
        if (XX_RequestDay == null) throw new IllegalArgumentException ("XX_RequestDay is mandatory.");
        set_Value ("XX_RequestDay", XX_RequestDay);
        
    }
    
    /** Get XX_RequestDay.
    @return XX_RequestDay */
    public java.math.BigDecimal getXX_RequestDay() 
    {
        return get_ValueAsBigDecimal("XX_RequestDay");
        
    }
    
    /** Set XX_RequestNumber.
    @param XX_RequestNumber XX_RequestNumber */
    public void setXX_RequestNumber (java.math.BigDecimal XX_RequestNumber)
    {
        if (XX_RequestNumber == null) throw new IllegalArgumentException ("XX_RequestNumber is mandatory.");
        set_Value ("XX_RequestNumber", XX_RequestNumber);
        
    }
    
    /** Get XX_RequestNumber.
    @return XX_RequestNumber */
    public java.math.BigDecimal getXX_RequestNumber() 
    {
        return get_ValueAsBigDecimal("XX_RequestNumber");
        
    }
    
    /** Create and Update = B */
    public static final String XX_SHOPBYROOMSTATUS_CreateAndUpdate = X_Ref_AD_AssignSet_Rule.CREATE_AND_UPDATE.getValue();
    /** Create only = C */
    public static final String XX_SHOPBYROOMSTATUS_CreateOnly = X_Ref_AD_AssignSet_Rule.CREATE_ONLY.getValue();
    /** Create and Update if not Processed = P */
    public static final String XX_SHOPBYROOMSTATUS_CreateAndUpdateIfNotProcessed = X_Ref_AD_AssignSet_Rule.CREATE_AND_UPDATE_IF_NOT_PROCESSED.getValue();
    /** Update if not Processed = Q */
    public static final String XX_SHOPBYROOMSTATUS_UpdateIfNotProcessed = X_Ref_AD_AssignSet_Rule.UPDATE_IF_NOT_PROCESSED.getValue();
    /** Update only = U */
    public static final String XX_SHOPBYROOMSTATUS_UpdateOnly = X_Ref_AD_AssignSet_Rule.UPDATE_ONLY.getValue();
    /** Set XX_ShopByRoomStatus.
    @param XX_ShopByRoomStatus XX_ShopByRoomStatus */
    public void setXX_ShopByRoomStatus (String XX_ShopByRoomStatus)
    {
        if (XX_ShopByRoomStatus == null) throw new IllegalArgumentException ("XX_ShopByRoomStatus is mandatory");
        if (!X_Ref_AD_AssignSet_Rule.isValid(XX_ShopByRoomStatus))
        throw new IllegalArgumentException ("XX_ShopByRoomStatus Invalid value - " + XX_ShopByRoomStatus + " - Reference_ID=424 - B - C - P - Q - U");
        set_Value ("XX_ShopByRoomStatus", XX_ShopByRoomStatus);
        
    }
    
    /** Get XX_ShopByRoomStatus.
    @return XX_ShopByRoomStatus */
    public String getXX_ShopByRoomStatus() 
    {
        return (String)get_Value("XX_ShopByRoomStatus");
        
    }
    
    /** Create and Update = B */
    public static final String XX_TYPEOFAPPLICATION_CreateAndUpdate = X_Ref_AD_AssignSet_Rule.CREATE_AND_UPDATE.getValue();
    /** Create only = C */
    public static final String XX_TYPEOFAPPLICATION_CreateOnly = X_Ref_AD_AssignSet_Rule.CREATE_ONLY.getValue();
    /** Create and Update if not Processed = P */
    public static final String XX_TYPEOFAPPLICATION_CreateAndUpdateIfNotProcessed = X_Ref_AD_AssignSet_Rule.CREATE_AND_UPDATE_IF_NOT_PROCESSED.getValue();
    /** Update if not Processed = Q */
    public static final String XX_TYPEOFAPPLICATION_UpdateIfNotProcessed = X_Ref_AD_AssignSet_Rule.UPDATE_IF_NOT_PROCESSED.getValue();
    /** Update only = U */
    public static final String XX_TYPEOFAPPLICATION_UpdateOnly = X_Ref_AD_AssignSet_Rule.UPDATE_ONLY.getValue();
    /** Set XX_TypeOfApplication.
    @param XX_TypeOfApplication XX_TypeOfApplication */
    public void setXX_TypeOfApplication (String XX_TypeOfApplication)
    {
        if (XX_TypeOfApplication == null) throw new IllegalArgumentException ("XX_TypeOfApplication is mandatory");
        if (!X_Ref_AD_AssignSet_Rule.isValid(XX_TypeOfApplication))
        throw new IllegalArgumentException ("XX_TypeOfApplication Invalid value - " + XX_TypeOfApplication + " - Reference_ID=424 - B - C - P - Q - U");
        set_Value ("XX_TypeOfApplication", XX_TypeOfApplication);
        
    }
    
    /** Get XX_TypeOfApplication.
    @return XX_TypeOfApplication */
    public String getXX_TypeOfApplication() 
    {
        return (String)get_Value("XX_TypeOfApplication");
        
    }
    
    /** Set XX_UserCreation.
    @param XX_UserCreation XX_UserCreation */
    public void setXX_UserCreation (String XX_UserCreation)
    {
        if (XX_UserCreation == null) throw new IllegalArgumentException ("XX_UserCreation is mandatory.");
        set_Value ("XX_UserCreation", XX_UserCreation);
        
    }
    
    /** Get XX_UserCreation.
    @return XX_UserCreation */
    public String getXX_UserCreation() 
    {
        return (String)get_Value("XX_UserCreation");
        
    }
    
    /** Set XX_UserUpdate.
    @param XX_UserUpdate XX_UserUpdate */
    public void setXX_UserUpdate (String XX_UserUpdate)
    {
        if (XX_UserUpdate == null) throw new IllegalArgumentException ("XX_UserUpdate is mandatory.");
        set_Value ("XX_UserUpdate", XX_UserUpdate);
        
    }
    
    /** Get XX_UserUpdate.
    @return XX_UserUpdate */
    public String getXX_UserUpdate() 
    {
        return (String)get_Value("XX_UserUpdate");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID < 1) throw new IllegalArgumentException ("XX_VMR_Department_ID is mandatory.");
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set XX_VMR_Solm01_ID.
    @param XX_VMR_Solm01_ID XX_VMR_Solm01_ID */
    public void setXX_VMR_Solm01_ID (int XX_VMR_Solm01_ID)
    {
        if (XX_VMR_Solm01_ID < 1) throw new IllegalArgumentException ("XX_VMR_Solm01_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Solm01_ID", Integer.valueOf(XX_VMR_Solm01_ID));
        
    }
    
    /** Get XX_VMR_Solm01_ID.
    @return XX_VMR_Solm01_ID */
    public int getXX_VMR_Solm01_ID() 
    {
        return get_ValueAsInt("XX_VMR_Solm01_ID");
        
    }
    
    /** Set XX_YearOfReleaseStatus.
    @param XX_YearOfReleaseStatus XX_YearOfReleaseStatus */
    public void setXX_YearOfReleaseStatus (java.math.BigDecimal XX_YearOfReleaseStatus)
    {
        if (XX_YearOfReleaseStatus == null) throw new IllegalArgumentException ("XX_YearOfReleaseStatus is mandatory.");
        set_Value ("XX_YearOfReleaseStatus", XX_YearOfReleaseStatus);
        
    }
    
    /** Get XX_YearOfReleaseStatus.
    @return XX_YearOfReleaseStatus */
    public java.math.BigDecimal getXX_YearOfReleaseStatus() 
    {
        return get_ValueAsBigDecimal("XX_YearOfReleaseStatus");
        
    }
    
    /** Set XX_YearRegistration.
    @param XX_YearRegistration XX_YearRegistration */
    public void setXX_YearRegistration (java.math.BigDecimal XX_YearRegistration)
    {
        if (XX_YearRegistration == null) throw new IllegalArgumentException ("XX_YearRegistration is mandatory.");
        set_Value ("XX_YearRegistration", XX_YearRegistration);
        
    }
    
    /** Get XX_YearRegistration.
    @return XX_YearRegistration */
    public java.math.BigDecimal getXX_YearRegistration() 
    {
        return get_ValueAsBigDecimal("XX_YearRegistration");
        
    }
    
    /** Set XX_YearStatus.
    @param XX_YearStatus XX_YearStatus */
    public void setXX_YearStatus (java.math.BigDecimal XX_YearStatus)
    {
        if (XX_YearStatus == null) throw new IllegalArgumentException ("XX_YearStatus is mandatory.");
        set_Value ("XX_YearStatus", XX_YearStatus);
        
    }
    
    /** Get XX_YearStatus.
    @return XX_YearStatus */
    public java.math.BigDecimal getXX_YearStatus() 
    {
        return get_ValueAsBigDecimal("XX_YearStatus");
        
    }
    
    
}
