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
/** Generated Model for XX_VSI_KeyNameInfo
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VSI_KeyNameInfo extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VSI_KeyNameInfo_ID id
    @param trx transaction
    */
    public X_XX_VSI_KeyNameInfo (Ctx ctx, int XX_VSI_KeyNameInfo_ID, Trx trx)
    {
        super (ctx, XX_VSI_KeyNameInfo_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VSI_KeyNameInfo_ID == 0)
        {
            setXX_VSI_KeyNameInfo_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VSI_KeyNameInfo (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27636694146789L;
    /** Last Updated Timestamp 2012-12-03 16:37:10.0 */
    public static final long updatedMS = 1354568830000L;
    /** AD_Table_ID=1000211 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VSI_KeyNameInfo");
        
    }
    ;
    
    /** TableName=XX_VSI_KeyNameInfo */
    public static final String Table_Name="XX_VSI_KeyNameInfo";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Document Type.
    @param XX_C_DocType_ID Document Type */
    public void setXX_C_DocType_ID (int XX_C_DocType_ID)
    {
        if (XX_C_DocType_ID <= 0) set_Value ("XX_C_DocType_ID", null);
        else
        set_Value ("XX_C_DocType_ID", Integer.valueOf(XX_C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document Type */
    public int getXX_C_DocType_ID() 
    {
        return get_ValueAsInt("XX_C_DocType_ID");
        
    }
    
    /** Set DocType Vendor Return.
    @param XX_C_DocTypeVendorReturn_ID DocType Vendor Return */
    public void setXX_C_DocTypeVendorReturn_ID (int XX_C_DocTypeVendorReturn_ID)
    {
        if (XX_C_DocTypeVendorReturn_ID <= 0) set_Value ("XX_C_DocTypeVendorReturn_ID", null);
        else
        set_Value ("XX_C_DocTypeVendorReturn_ID", Integer.valueOf(XX_C_DocTypeVendorReturn_ID));
        
    }
    
    /** Get DocType Vendor Return.
    @return DocType Vendor Return */
    public int getXX_C_DocTypeVendorReturn_ID() 
    {
        return get_ValueAsInt("XX_C_DocTypeVendorReturn_ID");
        
    }
    
    /** Set Area Administration Manager.
    @param XX_L_AreaAdminManager_ID Area Administration Manager */
    public void setXX_L_AreaAdminManager_ID (int XX_L_AreaAdminManager_ID)
    {
        if (XX_L_AreaAdminManager_ID <= 0) set_Value ("XX_L_AreaAdminManager_ID", null);
        else
        set_Value ("XX_L_AreaAdminManager_ID", Integer.valueOf(XX_L_AreaAdminManager_ID));
        
    }
    
    /** Get Area Administration Manager.
    @return Area Administration Manager */
    public int getXX_L_AreaAdminManager_ID() 
    {
        return get_ValueAsInt("XX_L_AreaAdminManager_ID");
        
    }
    
    /** Set Marketing Manager by Area.
    @param XX_L_AreaMarketingManager_ID Marketing Manager by Area */
    public void setXX_L_AreaMarketingManager_ID (int XX_L_AreaMarketingManager_ID)
    {
        if (XX_L_AreaMarketingManager_ID <= 0) set_Value ("XX_L_AreaMarketingManager_ID", null);
        else
        set_Value ("XX_L_AreaMarketingManager_ID", Integer.valueOf(XX_L_AreaMarketingManager_ID));
        
    }
    
    /** Get Marketing Manager by Area.
    @return Marketing Manager by Area */
    public int getXX_L_AreaMarketingManager_ID() 
    {
        return get_ValueAsInt("XX_L_AreaMarketingManager_ID");
        
    }
    
    /** Set Return Motive Automatic Transfer.
    @param XX_L_AutomaticTransfer_ID Return Motive Automatic Transfer */
    public void setXX_L_AutomaticTransfer_ID (int XX_L_AutomaticTransfer_ID)
    {
        if (XX_L_AutomaticTransfer_ID <= 0) set_Value ("XX_L_AutomaticTransfer_ID", null);
        else
        set_Value ("XX_L_AutomaticTransfer_ID", Integer.valueOf(XX_L_AutomaticTransfer_ID));
        
    }
    
    /** Get Return Motive Automatic Transfer.
    @return Return Motive Automatic Transfer */
    public int getXX_L_AutomaticTransfer_ID() 
    {
        return get_ValueAsInt("XX_L_AutomaticTransfer_ID");
        
    }
    
    /** Set Benefit Adelanto Pago.
    @param XX_L_BenefitAdePag_ID Benefit Adelanto Pago */
    public void setXX_L_BenefitAdePag_ID (int XX_L_BenefitAdePag_ID)
    {
        if (XX_L_BenefitAdePag_ID <= 0) set_Value ("XX_L_BenefitAdePag_ID", null);
        else
        set_Value ("XX_L_BenefitAdePag_ID", Integer.valueOf(XX_L_BenefitAdePag_ID));
        
    }
    
    /** Get Benefit Adelanto Pago.
    @return Benefit Adelanto Pago */
    public int getXX_L_BenefitAdePag_ID() 
    {
        return get_ValueAsInt("XX_L_BenefitAdePag_ID");
        
    }
    
    /** Set Benefit Modificación Fecha Entrega.
    @param XX_L_BenefitModFechEntre_ID Benefit Modificación Fecha Entrega */
    public void setXX_L_BenefitModFechEntre_ID (int XX_L_BenefitModFechEntre_ID)
    {
        if (XX_L_BenefitModFechEntre_ID <= 0) set_Value ("XX_L_BenefitModFechEntre_ID", null);
        else
        set_Value ("XX_L_BenefitModFechEntre_ID", Integer.valueOf(XX_L_BenefitModFechEntre_ID));
        
    }
    
    /** Get Benefit Modificación Fecha Entrega.
    @return Benefit Modificación Fecha Entrega */
    public int getXX_L_BenefitModFechEntre_ID() 
    {
        return get_ValueAsInt("XX_L_BenefitModFechEntre_ID");
        
    }
    
    /** Set No Check Benefit.
    @param XX_L_BenefitNoCheck_ID No Check Benefit */
    public void setXX_L_BenefitNoCheck_ID (int XX_L_BenefitNoCheck_ID)
    {
        if (XX_L_BenefitNoCheck_ID <= 0) set_Value ("XX_L_BenefitNoCheck_ID", null);
        else
        set_Value ("XX_L_BenefitNoCheck_ID", Integer.valueOf(XX_L_BenefitNoCheck_ID));
        
    }
    
    /** Get No Check Benefit.
    @return No Check Benefit */
    public int getXX_L_BenefitNoCheck_ID() 
    {
        return get_ValueAsInt("XX_L_BenefitNoCheck_ID");
        
    }
    
    /** Set Benefit Aplicará Porcentaje Total Descuento.
    @param XX_L_BenefitPorcTotalDescu_ID Benefit Aplicará Porcentaje Total Descuento */
    public void setXX_L_BenefitPorcTotalDescu_ID (int XX_L_BenefitPorcTotalDescu_ID)
    {
        if (XX_L_BenefitPorcTotalDescu_ID <= 0) set_Value ("XX_L_BenefitPorcTotalDescu_ID", null);
        else
        set_Value ("XX_L_BenefitPorcTotalDescu_ID", Integer.valueOf(XX_L_BenefitPorcTotalDescu_ID));
        
    }
    
    /** Get Benefit Aplicará Porcentaje Total Descuento.
    @return Benefit Aplicará Porcentaje Total Descuento */
    public int getXX_L_BenefitPorcTotalDescu_ID() 
    {
        return get_ValueAsInt("XX_L_BenefitPorcTotalDescu_ID");
        
    }
    
    /** Set Credit Memo Document Type.
    @param XX_L_C_DocTypeCredit_ID Credit Memo Document Type */
    public void setXX_L_C_DocTypeCredit_ID (int XX_L_C_DocTypeCredit_ID)
    {
        if (XX_L_C_DocTypeCredit_ID <= 0) set_Value ("XX_L_C_DocTypeCredit_ID", null);
        else
        set_Value ("XX_L_C_DocTypeCredit_ID", Integer.valueOf(XX_L_C_DocTypeCredit_ID));
        
    }
    
    /** Get Credit Memo Document Type.
    @return Credit Memo Document Type */
    public int getXX_L_C_DocTypeCredit_ID() 
    {
        return get_ValueAsInt("XX_L_C_DocTypeCredit_ID");
        
    }
    
    /** Set AR Credit Memo Document Type.
    @param XX_L_C_DocTypeCreditMemo_ID AR Credit Memo Document Type */
    public void setXX_L_C_DocTypeCreditMemo_ID (int XX_L_C_DocTypeCreditMemo_ID)
    {
        if (XX_L_C_DocTypeCreditMemo_ID <= 0) set_Value ("XX_L_C_DocTypeCreditMemo_ID", null);
        else
        set_Value ("XX_L_C_DocTypeCreditMemo_ID", Integer.valueOf(XX_L_C_DocTypeCreditMemo_ID));
        
    }
    
    /** Get AR Credit Memo Document Type.
    @return AR Credit Memo Document Type */
    public int getXX_L_C_DocTypeCreditMemo_ID() 
    {
        return get_ValueAsInt("XX_L_C_DocTypeCreditMemo_ID");
        
    }
    
    /** Set Document Type Debit.
    @param XX_L_C_DocTypeDebit_ID Document Type Debit */
    public void setXX_L_C_DocTypeDebit_ID (int XX_L_C_DocTypeDebit_ID)
    {
        if (XX_L_C_DocTypeDebit_ID <= 0) set_Value ("XX_L_C_DocTypeDebit_ID", null);
        else
        set_Value ("XX_L_C_DocTypeDebit_ID", Integer.valueOf(XX_L_C_DocTypeDebit_ID));
        
    }
    
    /** Get Document Type Debit.
    @return Document Type Debit */
    public int getXX_L_C_DocTypeDebit_ID() 
    {
        return get_ValueAsInt("XX_L_C_DocTypeDebit_ID");
        
    }
    
    /** Set MM Shipment Document Type.
    @param XX_L_C_DocTypeMMShipment_ID MM Shipment Document Type */
    public void setXX_L_C_DocTypeMMShipment_ID (int XX_L_C_DocTypeMMShipment_ID)
    {
        if (XX_L_C_DocTypeMMShipment_ID <= 0) set_Value ("XX_L_C_DocTypeMMShipment_ID", null);
        else
        set_Value ("XX_L_C_DocTypeMMShipment_ID", Integer.valueOf(XX_L_C_DocTypeMMShipment_ID));
        
    }
    
    /** Get MM Shipment Document Type.
    @return MM Shipment Document Type */
    public int getXX_L_C_DocTypeMMShipment_ID() 
    {
        return get_ValueAsInt("XX_L_C_DocTypeMMShipment_ID");
        
    }
    
    /** Set Purchase Order Document Type.
    @param XX_L_C_DocTypeOrder_ID Purchase Order Document Type */
    public void setXX_L_C_DocTypeOrder_ID (int XX_L_C_DocTypeOrder_ID)
    {
        if (XX_L_C_DocTypeOrder_ID <= 0) set_Value ("XX_L_C_DocTypeOrder_ID", null);
        else
        set_Value ("XX_L_C_DocTypeOrder_ID", Integer.valueOf(XX_L_C_DocTypeOrder_ID));
        
    }
    
    /** Get Purchase Order Document Type.
    @return Purchase Order Document Type */
    public int getXX_L_C_DocTypeOrder_ID() 
    {
        return get_ValueAsInt("XX_L_C_DocTypeOrder_ID");
        
    }
    
    /** Set Material Receipt Document Type.
    @param XX_L_C_DocTypeReceipt_ID Material Receipt Document Type */
    public void setXX_L_C_DocTypeReceipt_ID (int XX_L_C_DocTypeReceipt_ID)
    {
        if (XX_L_C_DocTypeReceipt_ID <= 0) set_Value ("XX_L_C_DocTypeReceipt_ID", null);
        else
        set_Value ("XX_L_C_DocTypeReceipt_ID", Integer.valueOf(XX_L_C_DocTypeReceipt_ID));
        
    }
    
    /** Get Material Receipt Document Type.
    @return Material Receipt Document Type */
    public int getXX_L_C_DocTypeReceipt_ID() 
    {
        return get_ValueAsInt("XX_L_C_DocTypeReceipt_ID");
        
    }
    
    /** Set Invoice Return DocType.
    @param XX_L_C_DocTypeReturn_ID Invoice Return DocType */
    public void setXX_L_C_DocTypeReturn_ID (int XX_L_C_DocTypeReturn_ID)
    {
        if (XX_L_C_DocTypeReturn_ID <= 0) set_Value ("XX_L_C_DocTypeReturn_ID", null);
        else
        set_Value ("XX_L_C_DocTypeReturn_ID", Integer.valueOf(XX_L_C_DocTypeReturn_ID));
        
    }
    
    /** Get Invoice Return DocType.
    @return Invoice Return DocType */
    public int getXX_L_C_DocTypeReturn_ID() 
    {
        return get_ValueAsInt("XX_L_C_DocTypeReturn_ID");
        
    }
    
    /** Set Client CentroBeco.
    @param XX_L_ClientCentroBeco_ID Client CentroBeco */
    public void setXX_L_ClientCentroBeco_ID (int XX_L_ClientCentroBeco_ID)
    {
        if (XX_L_ClientCentroBeco_ID <= 0) set_Value ("XX_L_ClientCentroBeco_ID", null);
        else
        set_Value ("XX_L_ClientCentroBeco_ID", Integer.valueOf(XX_L_ClientCentroBeco_ID));
        
    }
    
    /** Get Client CentroBeco.
    @return Client CentroBeco */
    public int getXX_L_ClientCentroBeco_ID() 
    {
        return get_ValueAsInt("XX_L_ClientCentroBeco_ID");
        
    }
    
    /** Set Close Alert.
    @param XX_L_CloseAlertCT_ID Cierra la alerta */
    public void setXX_L_CloseAlertCT_ID (int XX_L_CloseAlertCT_ID)
    {
        if (XX_L_CloseAlertCT_ID <= 0) set_Value ("XX_L_CloseAlertCT_ID", null);
        else
        set_Value ("XX_L_CloseAlertCT_ID", Integer.valueOf(XX_L_CloseAlertCT_ID));
        
    }
    
    /** Get Close Alert.
    @return Cierra la alerta */
    public int getXX_L_CloseAlertCT_ID() 
    {
        return get_ValueAsInt("XX_L_CloseAlertCT_ID");
        
    }
    
    /** Sales = 10000024 */
    public static final String XX_L_CONTACTTYPEADMINISTRATIVE_Sales = X_Ref_XX_Ref_ContactType.SALES.getValue();
    /** Administrative = 10000025 */
    public static final String XX_L_CONTACTTYPEADMINISTRATIVE_Administrative = X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue();
    /** Legal Representative = 10000026 */
    public static final String XX_L_CONTACTTYPEADMINISTRATIVE_LegalRepresentative = X_Ref_XX_Ref_ContactType.LEGAL_REPRESENTATIVE.getValue();
    /** Comercial = 10000027 */
    public static final String XX_L_CONTACTTYPEADMINISTRATIVE_Comercial = X_Ref_XX_Ref_ContactType.COMERCIAL.getValue();
    /** Set Contact Type Administrative.
    @param XX_L_ContactTypeAdministrative Contact Type Administrative */
    public void setXX_L_ContactTypeAdministrative (String XX_L_ContactTypeAdministrative)
    {
        if (!X_Ref_XX_Ref_ContactType.isValid(XX_L_ContactTypeAdministrative))
        throw new IllegalArgumentException ("XX_L_ContactTypeAdministrative Invalid value - " + XX_L_ContactTypeAdministrative + " - Reference_ID=1000138 - 10000024 - 10000025 - 10000026 - 10000027");
        set_Value ("XX_L_ContactTypeAdministrative", XX_L_ContactTypeAdministrative);
        
    }
    
    /** Get Contact Type Administrative.
    @return Contact Type Administrative */
    public String getXX_L_ContactTypeAdministrative() 
    {
        return (String)get_Value("XX_L_ContactTypeAdministrative");
        
    }
    
    /** Sales = 10000024 */
    public static final String XX_L_CONTACTTYPELEGAL_Sales = X_Ref_XX_Ref_ContactType.SALES.getValue();
    /** Administrative = 10000025 */
    public static final String XX_L_CONTACTTYPELEGAL_Administrative = X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue();
    /** Legal Representative = 10000026 */
    public static final String XX_L_CONTACTTYPELEGAL_LegalRepresentative = X_Ref_XX_Ref_ContactType.LEGAL_REPRESENTATIVE.getValue();
    /** Comercial = 10000027 */
    public static final String XX_L_CONTACTTYPELEGAL_Comercial = X_Ref_XX_Ref_ContactType.COMERCIAL.getValue();
    /** Set Contact Type Legal.
    @param XX_L_ContactTypeLegal Contact Type Legal */
    public void setXX_L_ContactTypeLegal (String XX_L_ContactTypeLegal)
    {
        if (!X_Ref_XX_Ref_ContactType.isValid(XX_L_ContactTypeLegal))
        throw new IllegalArgumentException ("XX_L_ContactTypeLegal Invalid value - " + XX_L_ContactTypeLegal + " - Reference_ID=1000138 - 10000024 - 10000025 - 10000026 - 10000027");
        set_Value ("XX_L_ContactTypeLegal", XX_L_ContactTypeLegal);
        
    }
    
    /** Get Contact Type Legal.
    @return Contact Type Legal */
    public String getXX_L_ContactTypeLegal() 
    {
        return (String)get_Value("XX_L_ContactTypeLegal");
        
    }
    
    /** Sales = 10000024 */
    public static final String XX_L_CONTACTTYPESALES_Sales = X_Ref_XX_Ref_ContactType.SALES.getValue();
    /** Administrative = 10000025 */
    public static final String XX_L_CONTACTTYPESALES_Administrative = X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue();
    /** Legal Representative = 10000026 */
    public static final String XX_L_CONTACTTYPESALES_LegalRepresentative = X_Ref_XX_Ref_ContactType.LEGAL_REPRESENTATIVE.getValue();
    /** Comercial = 10000027 */
    public static final String XX_L_CONTACTTYPESALES_Comercial = X_Ref_XX_Ref_ContactType.COMERCIAL.getValue();
    /** Set Contact Type Sales.
    @param XX_L_ContactTypeSales Contact Type Sales */
    public void setXX_L_ContactTypeSales (String XX_L_ContactTypeSales)
    {
        if (!X_Ref_XX_Ref_ContactType.isValid(XX_L_ContactTypeSales))
        throw new IllegalArgumentException ("XX_L_ContactTypeSales Invalid value - " + XX_L_ContactTypeSales + " - Reference_ID=1000138 - 10000024 - 10000025 - 10000026 - 10000027");
        set_Value ("XX_L_ContactTypeSales", XX_L_ContactTypeSales);
        
    }
    
    /** Get Contact Type Sales.
    @return Contact Type Sales */
    public String getXX_L_ContactTypeSales() 
    {
        return (String)get_Value("XX_L_ContactTypeSales");
        
    }
    
    /** Set ConversionTypeEstimado.
    @param XX_L_ConversionTypeEstimado_ID ConversionTypeEstimado */
    public void setXX_L_ConversionTypeEstimado_ID (int XX_L_ConversionTypeEstimado_ID)
    {
        if (XX_L_ConversionTypeEstimado_ID <= 0) set_Value ("XX_L_ConversionTypeEstimado_ID", null);
        else
        set_Value ("XX_L_ConversionTypeEstimado_ID", Integer.valueOf(XX_L_ConversionTypeEstimado_ID));
        
    }
    
    /** Get ConversionTypeEstimado.
    @return ConversionTypeEstimado */
    public int getXX_L_ConversionTypeEstimado_ID() 
    {
        return get_ValueAsInt("XX_L_ConversionTypeEstimado_ID");
        
    }
    
    /** Set C_Order Column.
    @param XX_L_C_OrderColumn_ID C_Order Column */
    public void setXX_L_C_OrderColumn_ID (int XX_L_C_OrderColumn_ID)
    {
        if (XX_L_C_OrderColumn_ID <= 0) set_Value ("XX_L_C_OrderColumn_ID", null);
        else
        set_Value ("XX_L_C_OrderColumn_ID", Integer.valueOf(XX_L_C_OrderColumn_ID));
        
    }
    
    /** Get C_Order Column.
    @return C_Order Column */
    public int getXX_L_C_OrderColumn_ID() 
    {
        return get_ValueAsInt("XX_L_C_OrderColumn_ID");
        
    }
    
    /** Set MM Vendor Return.
    @param XX_L_C_ReturnVendor_ID MM Vendor Return */
    public void setXX_L_C_ReturnVendor_ID (int XX_L_C_ReturnVendor_ID)
    {
        if (XX_L_C_ReturnVendor_ID <= 0) set_Value ("XX_L_C_ReturnVendor_ID", null);
        else
        set_Value ("XX_L_C_ReturnVendor_ID", Integer.valueOf(XX_L_C_ReturnVendor_ID));
        
    }
    
    /** Get MM Vendor Return.
    @return MM Vendor Return */
    public int getXX_L_C_ReturnVendor_ID() 
    {
        return get_ValueAsInt("XX_L_C_ReturnVendor_ID");
        
    }
    
    /** Set Days to Calculate Vendor Rating.
    @param XX_L_DaysCalculaVendorRating Days to Calculate Vendor Rating */
    public void setXX_L_DaysCalculaVendorRating (int XX_L_DaysCalculaVendorRating)
    {
        set_Value ("XX_L_DaysCalculaVendorRating", Integer.valueOf(XX_L_DaysCalculaVendorRating));
        
    }
    
    /** Get Days to Calculate Vendor Rating.
    @return Days to Calculate Vendor Rating */
    public int getXX_L_DaysCalculaVendorRating() 
    {
        return get_ValueAsInt("XX_L_DaysCalculaVendorRating");
        
    }
    
    /** Set DiscountTypeDef.
    @param XX_L_DiscountTypeDef_ID DiscountTypeDef */
    public void setXX_L_DiscountTypeDef_ID (int XX_L_DiscountTypeDef_ID)
    {
        if (XX_L_DiscountTypeDef_ID <= 0) set_Value ("XX_L_DiscountTypeDef_ID", null);
        else
        set_Value ("XX_L_DiscountTypeDef_ID", Integer.valueOf(XX_L_DiscountTypeDef_ID));
        
    }
    
    /** Get DiscountTypeDef.
    @return DiscountTypeDef */
    public int getXX_L_DiscountTypeDef_ID() 
    {
        return get_ValueAsInt("XX_L_DiscountTypeDef_ID");
        
    }
    
    /** Set DiscountTypeFR.
    @param XX_L_DiscountTypeFR_ID DiscountTypeFR */
    public void setXX_L_DiscountTypeFR_ID (int XX_L_DiscountTypeFR_ID)
    {
        if (XX_L_DiscountTypeFR_ID <= 0) set_Value ("XX_L_DiscountTypeFR_ID", null);
        else
        set_Value ("XX_L_DiscountTypeFR_ID", Integer.valueOf(XX_L_DiscountTypeFR_ID));
        
    }
    
    /** Get DiscountTypeFR.
    @return DiscountTypeFR */
    public int getXX_L_DiscountTypeFR_ID() 
    {
        return get_ValueAsInt("XX_L_DiscountTypeFR_ID");
        
    }
    
    /** Set Dispatch Route Aereo.
    @param XX_L_DispatchRoute Dispatch Route Aereo */
    public void setXX_L_DispatchRoute (int XX_L_DispatchRoute)
    {
        set_Value ("XX_L_DispatchRoute", Integer.valueOf(XX_L_DispatchRoute));
        
    }
    
    /** Get Dispatch Route Aereo.
    @return Dispatch Route Aereo */
    public int getXX_L_DispatchRoute() 
    {
        return get_ValueAsInt("XX_L_DispatchRoute");
        
    }
    
    /** Set Dispatch Route Maritimo.
    @param XX_L_DispatchRouteMar Dispatch Route Maritimo */
    public void setXX_L_DispatchRouteMar (int XX_L_DispatchRouteMar)
    {
        set_Value ("XX_L_DispatchRouteMar", Integer.valueOf(XX_L_DispatchRouteMar));
        
    }
    
    /** Get Dispatch Route Maritimo.
    @return Dispatch Route Maritimo */
    public int getXX_L_DispatchRouteMar() 
    {
        return get_ValueAsInt("XX_L_DispatchRouteMar");
        
    }
    
    /** Set Dispatch Route Terrestre.
    @param XX_L_DispatchRouteTer Dispatch Route Terrestre */
    public void setXX_L_DispatchRouteTer (int XX_L_DispatchRouteTer)
    {
        set_Value ("XX_L_DispatchRouteTer", Integer.valueOf(XX_L_DispatchRouteTer));
        
    }
    
    /** Get Dispatch Route Terrestre.
    @return Dispatch Route Terrestre */
    public int getXX_L_DispatchRouteTer() 
    {
        return get_ValueAsInt("XX_L_DispatchRouteTer");
        
    }
    
    /** Set Distribution by Budget.
    @param XX_L_DistributionTypeBudget_ID Distribution by Budget */
    public void setXX_L_DistributionTypeBudget_ID (int XX_L_DistributionTypeBudget_ID)
    {
        if (XX_L_DistributionTypeBudget_ID <= 0) set_Value ("XX_L_DistributionTypeBudget_ID", null);
        else
        set_Value ("XX_L_DistributionTypeBudget_ID", Integer.valueOf(XX_L_DistributionTypeBudget_ID));
        
    }
    
    /** Get Distribution by Budget.
    @return Distribution by Budget */
    public int getXX_L_DistributionTypeBudget_ID() 
    {
        return get_ValueAsInt("XX_L_DistributionTypeBudget_ID");
        
    }
    
    /** Set Distribution by Percentages.
    @param XX_L_DistributionTypePercen_ID Distribution by Percentages */
    public void setXX_L_DistributionTypePercen_ID (int XX_L_DistributionTypePercen_ID)
    {
        if (XX_L_DistributionTypePercen_ID <= 0) set_Value ("XX_L_DistributionTypePercen_ID", null);
        else
        set_Value ("XX_L_DistributionTypePercen_ID", Integer.valueOf(XX_L_DistributionTypePercen_ID));
        
    }
    
    /** Get Distribution by Percentages.
    @return Distribution by Percentages */
    public int getXX_L_DistributionTypePercen_ID() 
    {
        return get_ValueAsInt("XX_L_DistributionTypePercen_ID");
        
    }
    
    /** Set Distribution By Pieces.
    @param XX_L_DistributionTypePieces_ID Distribution By Pieces */
    public void setXX_L_DistributionTypePieces_ID (int XX_L_DistributionTypePieces_ID)
    {
        if (XX_L_DistributionTypePieces_ID <= 0) set_Value ("XX_L_DistributionTypePieces_ID", null);
        else
        set_Value ("XX_L_DistributionTypePieces_ID", Integer.valueOf(XX_L_DistributionTypePieces_ID));
        
    }
    
    /** Get Distribution By Pieces.
    @return Distribution By Pieces */
    public int getXX_L_DistributionTypePieces_ID() 
    {
        return get_ValueAsInt("XX_L_DistributionTypePieces_ID");
        
    }
    
    /** Set Distribution by Replacement.
    @param XX_L_DistributionTypeReplac_ID Distribution by Replacement */
    public void setXX_L_DistributionTypeReplac_ID (int XX_L_DistributionTypeReplac_ID)
    {
        if (XX_L_DistributionTypeReplac_ID <= 0) set_Value ("XX_L_DistributionTypeReplac_ID", null);
        else
        set_Value ("XX_L_DistributionTypeReplac_ID", Integer.valueOf(XX_L_DistributionTypeReplac_ID));
        
    }
    
    /** Get Distribution by Replacement.
    @return Distribution by Replacement */
    public int getXX_L_DistributionTypeReplac_ID() 
    {
        return get_ValueAsInt("XX_L_DistributionTypeReplac_ID");
        
    }
    
    /** Set Distribution by Sales/Budget.
    @param XX_L_DistributionTypeSalBud_ID Distribution by Sales/Budget */
    public void setXX_L_DistributionTypeSalBud_ID (int XX_L_DistributionTypeSalBud_ID)
    {
        if (XX_L_DistributionTypeSalBud_ID <= 0) set_Value ("XX_L_DistributionTypeSalBud_ID", null);
        else
        set_Value ("XX_L_DistributionTypeSalBud_ID", Integer.valueOf(XX_L_DistributionTypeSalBud_ID));
        
    }
    
    /** Get Distribution by Sales/Budget.
    @return Distribution by Sales/Budget */
    public int getXX_L_DistributionTypeSalBud_ID() 
    {
        return get_ValueAsInt("XX_L_DistributionTypeSalBud_ID");
        
    }
    
    /** Set Distribution by Sales.
    @param XX_L_DistributionTypeSales_ID Distribution by Sales */
    public void setXX_L_DistributionTypeSales_ID (int XX_L_DistributionTypeSales_ID)
    {
        if (XX_L_DistributionTypeSales_ID <= 0) set_Value ("XX_L_DistributionTypeSales_ID", null);
        else
        set_Value ("XX_L_DistributionTypeSales_ID", Integer.valueOf(XX_L_DistributionTypeSales_ID));
        
    }
    
    /** Get Distribution by Sales.
    @return Distribution by Sales */
    public int getXX_L_DistributionTypeSales_ID() 
    {
        return get_ValueAsInt("XX_L_DistributionTypeSales_ID");
        
    }
    
    /** Set DocType Return CD.
    @param XX_L_DocTypeReturn_ID DocType Return CD */
    public void setXX_L_DocTypeReturn_ID (int XX_L_DocTypeReturn_ID)
    {
        if (XX_L_DocTypeReturn_ID <= 0) set_Value ("XX_L_DocTypeReturn_ID", null);
        else
        set_Value ("XX_L_DocTypeReturn_ID", Integer.valueOf(XX_L_DocTypeReturn_ID));
        
    }
    
    /** Get DocType Return CD.
    @return DocType Return CD */
    public int getXX_L_DocTypeReturn_ID() 
    {
        return get_ValueAsInt("XX_L_DocTypeReturn_ID");
        
    }
    
    /** Set DocType Transfer.
    @param XX_L_DocTypeTransfer_ID DocType Transfer */
    public void setXX_L_DocTypeTransfer_ID (int XX_L_DocTypeTransfer_ID)
    {
        if (XX_L_DocTypeTransfer_ID <= 0) set_Value ("XX_L_DocTypeTransfer_ID", null);
        else
        set_Value ("XX_L_DocTypeTransfer_ID", Integer.valueOf(XX_L_DocTypeTransfer_ID));
        
    }
    
    /** Get DocType Transfer.
    @return DocType Transfer */
    public int getXX_L_DocTypeTransfer_ID() 
    {
        return get_ValueAsInt("XX_L_DocTypeTransfer_ID");
        
    }
    
    /** Set Evaluation Criteria Compliance with Delivery in Assortment.
    @param XX_L_EC_CompiDeliveAssort_ID Evaluation Criteria Compliance with Delivery in Assortment */
    public void setXX_L_EC_CompiDeliveAssort_ID (int XX_L_EC_CompiDeliveAssort_ID)
    {
        if (XX_L_EC_CompiDeliveAssort_ID <= 0) set_Value ("XX_L_EC_CompiDeliveAssort_ID", null);
        else
        set_Value ("XX_L_EC_CompiDeliveAssort_ID", Integer.valueOf(XX_L_EC_CompiDeliveAssort_ID));
        
    }
    
    /** Get Evaluation Criteria Compliance with Delivery in Assortment.
    @return Evaluation Criteria Compliance with Delivery in Assortment */
    public int getXX_L_EC_CompiDeliveAssort_ID() 
    {
        return get_ValueAsInt("XX_L_EC_CompiDeliveAssort_ID");
        
    }
    
    /** Set Evaluation Criteria Compliance with Delivery Date.
    @param XX_L_EC_CompiDeliveDate_ID Evaluation Criteria Compliance with Delivery Date */
    public void setXX_L_EC_CompiDeliveDate_ID (int XX_L_EC_CompiDeliveDate_ID)
    {
        if (XX_L_EC_CompiDeliveDate_ID <= 0) set_Value ("XX_L_EC_CompiDeliveDate_ID", null);
        else
        set_Value ("XX_L_EC_CompiDeliveDate_ID", Integer.valueOf(XX_L_EC_CompiDeliveDate_ID));
        
    }
    
    /** Get Evaluation Criteria Compliance with Delivery Date.
    @return Evaluation Criteria Compliance with Delivery Date */
    public int getXX_L_EC_CompiDeliveDate_ID() 
    {
        return get_ValueAsInt("XX_L_EC_CompiDeliveDate_ID");
        
    }
    
    /** Set Evaluation Criteria Quality Compliance delivery.
    @param XX_L_EC_CompiDeliveQuali_ID Evaluation Criteria Quality Compliance delivery */
    public void setXX_L_EC_CompiDeliveQuali_ID (int XX_L_EC_CompiDeliveQuali_ID)
    {
        if (XX_L_EC_CompiDeliveQuali_ID <= 0) set_Value ("XX_L_EC_CompiDeliveQuali_ID", null);
        else
        set_Value ("XX_L_EC_CompiDeliveQuali_ID", Integer.valueOf(XX_L_EC_CompiDeliveQuali_ID));
        
    }
    
    /** Get Evaluation Criteria Quality Compliance delivery.
    @return Evaluation Criteria Quality Compliance delivery */
    public int getXX_L_EC_CompiDeliveQuali_ID() 
    {
        return get_ValueAsInt("XX_L_EC_CompiDeliveQuali_ID");
        
    }
    
    /** Set Evaluation Criteria Compliance with Delivery in Quantity.
    @param XX_L_EC_CompiDeliveQuanti_ID Evaluation Criteria Compliance with Delivery in Quantity */
    public void setXX_L_EC_CompiDeliveQuanti_ID (int XX_L_EC_CompiDeliveQuanti_ID)
    {
        if (XX_L_EC_CompiDeliveQuanti_ID <= 0) set_Value ("XX_L_EC_CompiDeliveQuanti_ID", null);
        else
        set_Value ("XX_L_EC_CompiDeliveQuanti_ID", Integer.valueOf(XX_L_EC_CompiDeliveQuanti_ID));
        
    }
    
    /** Get Evaluation Criteria Compliance with Delivery in Quantity.
    @return Evaluation Criteria Compliance with Delivery in Quantity */
    public int getXX_L_EC_CompiDeliveQuanti_ID() 
    {
        return get_ValueAsInt("XX_L_EC_CompiDeliveQuanti_ID");
        
    }
    
    /** Set Evaluation Criteria Direct Store Delivery.
    @param XX_L_EC_DireStoDeli_ID Evaluation Criteria Direct Store Delivery */
    public void setXX_L_EC_DireStoDeli_ID (int XX_L_EC_DireStoDeli_ID)
    {
        if (XX_L_EC_DireStoDeli_ID <= 0) set_Value ("XX_L_EC_DireStoDeli_ID", null);
        else
        set_Value ("XX_L_EC_DireStoDeli_ID", Integer.valueOf(XX_L_EC_DireStoDeli_ID));
        
    }
    
    /** Get Evaluation Criteria Direct Store Delivery.
    @return Evaluation Criteria Direct Store Delivery */
    public int getXX_L_EC_DireStoDeli_ID() 
    {
        return get_ValueAsInt("XX_L_EC_DireStoDeli_ID");
        
    }
    
    /** Set Evaluation Criteria Discount Trade Agreement.
    @param XX_L_EC_DiscoTradAgre_ID Evaluation Criteria Discount Trade Agreement */
    public void setXX_L_EC_DiscoTradAgre_ID (int XX_L_EC_DiscoTradAgre_ID)
    {
        if (XX_L_EC_DiscoTradAgre_ID <= 0) set_Value ("XX_L_EC_DiscoTradAgre_ID", null);
        else
        set_Value ("XX_L_EC_DiscoTradAgre_ID", Integer.valueOf(XX_L_EC_DiscoTradAgre_ID));
        
    }
    
    /** Get Evaluation Criteria Discount Trade Agreement.
    @return Evaluation Criteria Discount Trade Agreement */
    public int getXX_L_EC_DiscoTradAgre_ID() 
    {
        return get_ValueAsInt("XX_L_EC_DiscoTradAgre_ID");
        
    }
    
    /** Set Evaluation Criteria Identification Merchandise and document.
    @param XX_L_EC_IdentiMercDoc_ID Evaluation Criteria Identification Merchandise and document */
    public void setXX_L_EC_IdentiMercDoc_ID (int XX_L_EC_IdentiMercDoc_ID)
    {
        if (XX_L_EC_IdentiMercDoc_ID <= 0) set_Value ("XX_L_EC_IdentiMercDoc_ID", null);
        else
        set_Value ("XX_L_EC_IdentiMercDoc_ID", Integer.valueOf(XX_L_EC_IdentiMercDoc_ID));
        
    }
    
    /** Get Evaluation Criteria Identification Merchandise and document.
    @return Evaluation Criteria Identification Merchandise and document */
    public int getXX_L_EC_IdentiMercDoc_ID() 
    {
        return get_ValueAsInt("XX_L_EC_IdentiMercDoc_ID");
        
    }
    
    /** Set Evaluation Criteria Payment Term.
    @param XX_L_EC_PaymentTerm_ID Evaluation Criteria Payment Term */
    public void setXX_L_EC_PaymentTerm_ID (int XX_L_EC_PaymentTerm_ID)
    {
        if (XX_L_EC_PaymentTerm_ID <= 0) set_Value ("XX_L_EC_PaymentTerm_ID", null);
        else
        set_Value ("XX_L_EC_PaymentTerm_ID", Integer.valueOf(XX_L_EC_PaymentTerm_ID));
        
    }
    
    /** Get Evaluation Criteria Payment Term.
    @return Evaluation Criteria Payment Term */
    public int getXX_L_EC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("XX_L_EC_PaymentTerm_ID");
        
    }
    
    /** Set Evaluation Criteria Score Accumulated Previous.
    @param XX_L_EC_ScoreAccumuPrevi_ID Evaluation Criteria Score Accumulated Previous */
    public void setXX_L_EC_ScoreAccumuPrevi_ID (int XX_L_EC_ScoreAccumuPrevi_ID)
    {
        if (XX_L_EC_ScoreAccumuPrevi_ID <= 0) set_Value ("XX_L_EC_ScoreAccumuPrevi_ID", null);
        else
        set_Value ("XX_L_EC_ScoreAccumuPrevi_ID", Integer.valueOf(XX_L_EC_ScoreAccumuPrevi_ID));
        
    }
    
    /** Get Evaluation Criteria Score Accumulated Previous.
    @return Evaluation Criteria Score Accumulated Previous */
    public int getXX_L_EC_ScoreAccumuPrevi_ID() 
    {
        return get_ValueAsInt("XX_L_EC_ScoreAccumuPrevi_ID");
        
    }
    
    /** Set Evaluation Criteria Total Score O/C.
    @param XX_L_EC_TotalScoreOC_ID Evaluation Criteria Total Score O/C */
    public void setXX_L_EC_TotalScoreOC_ID (int XX_L_EC_TotalScoreOC_ID)
    {
        if (XX_L_EC_TotalScoreOC_ID <= 0) set_Value ("XX_L_EC_TotalScoreOC_ID", null);
        else
        set_Value ("XX_L_EC_TotalScoreOC_ID", Integer.valueOf(XX_L_EC_TotalScoreOC_ID));
        
    }
    
    /** Get Evaluation Criteria Total Score O/C.
    @return Evaluation Criteria Total Score O/C */
    public int getXX_L_EC_TotalScoreOC_ID() 
    {
        return get_ValueAsInt("XX_L_EC_TotalScoreOC_ID");
        
    }
    
    /** Set XX_L_FormChangeProductL_ID.
    @param XX_L_FormChangeProductL_ID XX_L_FormChangeProductL_ID */
    public void setXX_L_FormChangeProductL_ID (int XX_L_FormChangeProductL_ID)
    {
        if (XX_L_FormChangeProductL_ID <= 0) set_Value ("XX_L_FormChangeProductL_ID", null);
        else
        set_Value ("XX_L_FormChangeProductL_ID", Integer.valueOf(XX_L_FormChangeProductL_ID));
        
    }
    
    /** Get XX_L_FormChangeProductL_ID.
    @return XX_L_FormChangeProductL_ID */
    public int getXX_L_FormChangeProductL_ID() 
    {
        return get_ValueAsInt("XX_L_FormChangeProductL_ID");
        
    }
    
    /** Set Complete Placed Order.
    @param XX_L_FormCompletePLO_ID Complete Placed Order */
    public void setXX_L_FormCompletePLO_ID (int XX_L_FormCompletePLO_ID)
    {
        if (XX_L_FormCompletePLO_ID <= 0) set_Value ("XX_L_FormCompletePLO_ID", null);
        else
        set_Value ("XX_L_FormCompletePLO_ID", Integer.valueOf(XX_L_FormCompletePLO_ID));
        
    }
    
    /** Get Complete Placed Order.
    @return Complete Placed Order */
    public int getXX_L_FormCompletePLO_ID() 
    {
        return get_ValueAsInt("XX_L_FormCompletePLO_ID");
        
    }
    
    /** Set Form View Detail Vendor Rating.
    @param XX_L_FormDetailVendorRating_ID Form View Detail Vendor Rating */
    public void setXX_L_FormDetailVendorRating_ID (int XX_L_FormDetailVendorRating_ID)
    {
        if (XX_L_FormDetailVendorRating_ID <= 0) set_Value ("XX_L_FormDetailVendorRating_ID", null);
        else
        set_Value ("XX_L_FormDetailVendorRating_ID", Integer.valueOf(XX_L_FormDetailVendorRating_ID));
        
    }
    
    /** Get Form View Detail Vendor Rating.
    @return Form View Detail Vendor Rating */
    public int getXX_L_FormDetailVendorRating_ID() 
    {
        return get_ValueAsInt("XX_L_FormDetailVendorRating_ID");
        
    }
    
    /** Set XX_L_FormDist_Budget_ID.
    @param XX_L_FormDist_Budget_ID XX_L_FormDist_Budget_ID */
    public void setXX_L_FormDist_Budget_ID (int XX_L_FormDist_Budget_ID)
    {
        if (XX_L_FormDist_Budget_ID <= 0) set_Value ("XX_L_FormDist_Budget_ID", null);
        else
        set_Value ("XX_L_FormDist_Budget_ID", Integer.valueOf(XX_L_FormDist_Budget_ID));
        
    }
    
    /** Get XX_L_FormDist_Budget_ID.
    @return XX_L_FormDist_Budget_ID */
    public int getXX_L_FormDist_Budget_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_Budget_ID");
        
    }
    
    /** Set Distribution O/C by Budget Form.
    @param XX_L_FormDist_OCBudget_ID Distribution O/C by Budget Form */
    public void setXX_L_FormDist_OCBudget_ID (int XX_L_FormDist_OCBudget_ID)
    {
        if (XX_L_FormDist_OCBudget_ID <= 0) set_Value ("XX_L_FormDist_OCBudget_ID", null);
        else
        set_Value ("XX_L_FormDist_OCBudget_ID", Integer.valueOf(XX_L_FormDist_OCBudget_ID));
        
    }
    
    /** Get Distribution O/C by Budget Form.
    @return Distribution O/C by Budget Form */
    public int getXX_L_FormDist_OCBudget_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_OCBudget_ID");
        
    }
    
    /** Set Distribution O/C by Percentages Form.
    @param XX_L_FormDist_OCPercent_ID Distribution O/C by Percentages Form */
    public void setXX_L_FormDist_OCPercent_ID (int XX_L_FormDist_OCPercent_ID)
    {
        if (XX_L_FormDist_OCPercent_ID <= 0) set_Value ("XX_L_FormDist_OCPercent_ID", null);
        else
        set_Value ("XX_L_FormDist_OCPercent_ID", Integer.valueOf(XX_L_FormDist_OCPercent_ID));
        
    }
    
    /** Get Distribution O/C by Percentages Form.
    @return Distribution O/C by Percentages Form */
    public int getXX_L_FormDist_OCPercent_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_OCPercent_ID");
        
    }
    
    /** Set Distribution O/C by Replacement.
    @param XX_L_FormDist_OCReplac_ID Distribution O/C by Replacement */
    public void setXX_L_FormDist_OCReplac_ID (int XX_L_FormDist_OCReplac_ID)
    {
        if (XX_L_FormDist_OCReplac_ID <= 0) set_Value ("XX_L_FormDist_OCReplac_ID", null);
        else
        set_Value ("XX_L_FormDist_OCReplac_ID", Integer.valueOf(XX_L_FormDist_OCReplac_ID));
        
    }
    
    /** Get Distribution O/C by Replacement.
    @return Distribution O/C by Replacement */
    public int getXX_L_FormDist_OCReplac_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_OCReplac_ID");
        
    }
    
    /** Set Distribution O/C by Sales/Budget.
    @param XX_L_FormDist_OCSalBud_ID Distribution O/C by Sales/Budget */
    public void setXX_L_FormDist_OCSalBud_ID (int XX_L_FormDist_OCSalBud_ID)
    {
        if (XX_L_FormDist_OCSalBud_ID <= 0) set_Value ("XX_L_FormDist_OCSalBud_ID", null);
        else
        set_Value ("XX_L_FormDist_OCSalBud_ID", Integer.valueOf(XX_L_FormDist_OCSalBud_ID));
        
    }
    
    /** Get Distribution O/C by Sales/Budget.
    @return Distribution O/C by Sales/Budget */
    public int getXX_L_FormDist_OCSalBud_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_OCSalBud_ID");
        
    }
    
    /** Set Distribution O/C by Sales.
    @param XX_L_FormDist_OCSales_ID Distribution O/C by Sales */
    public void setXX_L_FormDist_OCSales_ID (int XX_L_FormDist_OCSales_ID)
    {
        if (XX_L_FormDist_OCSales_ID <= 0) set_Value ("XX_L_FormDist_OCSales_ID", null);
        else
        set_Value ("XX_L_FormDist_OCSales_ID", Integer.valueOf(XX_L_FormDist_OCSales_ID));
        
    }
    
    /** Get Distribution O/C by Sales.
    @return Distribution O/C by Sales */
    public int getXX_L_FormDist_OCSales_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_OCSales_ID");
        
    }
    
    /** Set XX_L_FormDist_Percent_ID.
    @param XX_L_FormDist_Percent_ID XX_L_FormDist_Percent_ID */
    public void setXX_L_FormDist_Percent_ID (int XX_L_FormDist_Percent_ID)
    {
        if (XX_L_FormDist_Percent_ID <= 0) set_Value ("XX_L_FormDist_Percent_ID", null);
        else
        set_Value ("XX_L_FormDist_Percent_ID", Integer.valueOf(XX_L_FormDist_Percent_ID));
        
    }
    
    /** Get XX_L_FormDist_Percent_ID.
    @return XX_L_FormDist_Percent_ID */
    public int getXX_L_FormDist_Percent_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_Percent_ID");
        
    }
    
    /** Set XX_L_FormDist_Pieces_ID.
    @param XX_L_FormDist_Pieces_ID XX_L_FormDist_Pieces_ID */
    public void setXX_L_FormDist_Pieces_ID (int XX_L_FormDist_Pieces_ID)
    {
        if (XX_L_FormDist_Pieces_ID <= 0) set_Value ("XX_L_FormDist_Pieces_ID", null);
        else
        set_Value ("XX_L_FormDist_Pieces_ID", Integer.valueOf(XX_L_FormDist_Pieces_ID));
        
    }
    
    /** Get XX_L_FormDist_Pieces_ID.
    @return XX_L_FormDist_Pieces_ID */
    public int getXX_L_FormDist_Pieces_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_Pieces_ID");
        
    }
    
    /** Set XX_L_FormDist_Replace_ID.
    @param XX_L_FormDist_Replace_ID XX_L_FormDist_Replace_ID */
    public void setXX_L_FormDist_Replace_ID (int XX_L_FormDist_Replace_ID)
    {
        if (XX_L_FormDist_Replace_ID <= 0) set_Value ("XX_L_FormDist_Replace_ID", null);
        else
        set_Value ("XX_L_FormDist_Replace_ID", Integer.valueOf(XX_L_FormDist_Replace_ID));
        
    }
    
    /** Get XX_L_FormDist_Replace_ID.
    @return XX_L_FormDist_Replace_ID */
    public int getXX_L_FormDist_Replace_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_Replace_ID");
        
    }
    
    /** Set XX_L_FormDist_SaleBud_ID.
    @param XX_L_FormDist_SaleBud_ID XX_L_FormDist_SaleBud_ID */
    public void setXX_L_FormDist_SaleBud_ID (int XX_L_FormDist_SaleBud_ID)
    {
        if (XX_L_FormDist_SaleBud_ID <= 0) set_Value ("XX_L_FormDist_SaleBud_ID", null);
        else
        set_Value ("XX_L_FormDist_SaleBud_ID", Integer.valueOf(XX_L_FormDist_SaleBud_ID));
        
    }
    
    /** Get XX_L_FormDist_SaleBud_ID.
    @return XX_L_FormDist_SaleBud_ID */
    public int getXX_L_FormDist_SaleBud_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_SaleBud_ID");
        
    }
    
    /** Set XX_L_FormDist_Sales_ID.
    @param XX_L_FormDist_Sales_ID XX_L_FormDist_Sales_ID */
    public void setXX_L_FormDist_Sales_ID (int XX_L_FormDist_Sales_ID)
    {
        if (XX_L_FormDist_Sales_ID <= 0) set_Value ("XX_L_FormDist_Sales_ID", null);
        else
        set_Value ("XX_L_FormDist_Sales_ID", Integer.valueOf(XX_L_FormDist_Sales_ID));
        
    }
    
    /** Get XX_L_FormDist_Sales_ID.
    @return XX_L_FormDist_Sales_ID */
    public int getXX_L_FormDist_Sales_ID() 
    {
        return get_ValueAsInt("XX_L_FormDist_Sales_ID");
        
    }
    
    /** Set Print Discount Product Label Form.
    @param XX_L_FormPrintDiscProdLabel_ID Print Discount Product Label Form */
    public void setXX_L_FormPrintDiscProdLabel_ID (int XX_L_FormPrintDiscProdLabel_ID)
    {
        if (XX_L_FormPrintDiscProdLabel_ID <= 0) set_Value ("XX_L_FormPrintDiscProdLabel_ID", null);
        else
        set_Value ("XX_L_FormPrintDiscProdLabel_ID", Integer.valueOf(XX_L_FormPrintDiscProdLabel_ID));
        
    }
    
    /** Get Print Discount Product Label Form.
    @return Print Discount Product Label Form */
    public int getXX_L_FormPrintDiscProdLabel_ID() 
    {
        return get_ValueAsInt("XX_L_FormPrintDiscProdLabel_ID");
        
    }
    
    /** Set Form Print Dist Label.
    @param XX_L_FormPrintDistLabel_ID ID de la forma XX_PrintDistributionLabels */
    public void setXX_L_FormPrintDistLabel_ID (int XX_L_FormPrintDistLabel_ID)
    {
        if (XX_L_FormPrintDistLabel_ID <= 0) set_Value ("XX_L_FormPrintDistLabel_ID", null);
        else
        set_Value ("XX_L_FormPrintDistLabel_ID", Integer.valueOf(XX_L_FormPrintDistLabel_ID));
        
    }
    
    /** Get Form Print Dist Label.
    @return ID de la forma XX_PrintDistributionLabels */
    public int getXX_L_FormPrintDistLabel_ID() 
    {
        return get_ValueAsInt("XX_L_FormPrintDistLabel_ID");
        
    }
    
    /** Set Print Labels by  Price Consecutive.
    @param XX_L_FormPrintLabelPriceCon_ID ID de proceso que permite reimprimir las etiquetas de un producto con un consecutivo de precio específico */
    public void setXX_L_FormPrintLabelPriceCon_ID (int XX_L_FormPrintLabelPriceCon_ID)
    {
        if (XX_L_FormPrintLabelPriceCon_ID <= 0) set_Value ("XX_L_FormPrintLabelPriceCon_ID", null);
        else
        set_Value ("XX_L_FormPrintLabelPriceCon_ID", Integer.valueOf(XX_L_FormPrintLabelPriceCon_ID));
        
    }
    
    /** Get Print Labels by  Price Consecutive.
    @return ID de proceso que permite reimprimir las etiquetas de un producto con un consecutivo de precio específico */
    public int getXX_L_FormPrintLabelPriceCon_ID() 
    {
        return get_ValueAsInt("XX_L_FormPrintLabelPriceCon_ID");
        
    }
    
    /** Set Print Product Label Form.
    @param XX_L_FormPrintProductLabel_ID Print Product Label Form */
    public void setXX_L_FormPrintProductLabel_ID (int XX_L_FormPrintProductLabel_ID)
    {
        if (XX_L_FormPrintProductLabel_ID <= 0) set_Value ("XX_L_FormPrintProductLabel_ID", null);
        else
        set_Value ("XX_L_FormPrintProductLabel_ID", Integer.valueOf(XX_L_FormPrintProductLabel_ID));
        
    }
    
    /** Get Print Product Label Form.
    @return Print Product Label Form */
    public int getXX_L_FormPrintProductLabel_ID() 
    {
        return get_ValueAsInt("XX_L_FormPrintProductLabel_ID");
        
    }
    
    /** Set Form Unsolicited Product.
    @param XX_L_FormUnsolProd_ID Form Unsolicited Product */
    public void setXX_L_FormUnsolProd_ID (int XX_L_FormUnsolProd_ID)
    {
        if (XX_L_FormUnsolProd_ID <= 0) set_Value ("XX_L_FormUnsolProd_ID", null);
        else
        set_Value ("XX_L_FormUnsolProd_ID", Integer.valueOf(XX_L_FormUnsolProd_ID));
        
    }
    
    /** Get Form Unsolicited Product.
    @return Form Unsolicited Product */
    public int getXX_L_FormUnsolProd_ID() 
    {
        return get_ValueAsInt("XX_L_FormUnsolProd_ID");
        
    }
    
    /** Set Form Unsolicited Product Incorrect.
    @param XX_L_FormUnsolProdIncor_ID Form Unsolicited Product Incorrect */
    public void setXX_L_FormUnsolProdIncor_ID (int XX_L_FormUnsolProdIncor_ID)
    {
        if (XX_L_FormUnsolProdIncor_ID <= 0) set_Value ("XX_L_FormUnsolProdIncor_ID", null);
        else
        set_Value ("XX_L_FormUnsolProdIncor_ID", Integer.valueOf(XX_L_FormUnsolProdIncor_ID));
        
    }
    
    /** Get Form Unsolicited Product Incorrect.
    @return Form Unsolicited Product Incorrect */
    public int getXX_L_FormUnsolProdIncor_ID() 
    {
        return get_ValueAsInt("XX_L_FormUnsolProdIncor_ID");
        
    }
    
    /** Set Form View Benefit Vendor.
    @param XX_L_FormViewBenefitVendor_ID Form View Benefit Vendor */
    public void setXX_L_FormViewBenefitVendor_ID (int XX_L_FormViewBenefitVendor_ID)
    {
        if (XX_L_FormViewBenefitVendor_ID <= 0) set_Value ("XX_L_FormViewBenefitVendor_ID", null);
        else
        set_Value ("XX_L_FormViewBenefitVendor_ID", Integer.valueOf(XX_L_FormViewBenefitVendor_ID));
        
    }
    
    /** Get Form View Benefit Vendor.
    @return Form View Benefit Vendor */
    public int getXX_L_FormViewBenefitVendor_ID() 
    {
        return get_ValueAsInt("XX_L_FormViewBenefitVendor_ID");
        
    }
    
    /** Set Placed Order Search.
    @param XX_L_FPlacedOrderSearch_ID Placed Order Search */
    public void setXX_L_FPlacedOrderSearch_ID (int XX_L_FPlacedOrderSearch_ID)
    {
        if (XX_L_FPlacedOrderSearch_ID <= 0) set_Value ("XX_L_FPlacedOrderSearch_ID", null);
        else
        set_Value ("XX_L_FPlacedOrderSearch_ID", Integer.valueOf(XX_L_FPlacedOrderSearch_ID));
        
    }
    
    /** Get Placed Order Search.
    @return Placed Order Search */
    public int getXX_L_FPlacedOrderSearch_ID() 
    {
        return get_ValueAsInt("XX_L_FPlacedOrderSearch_ID");
        
    }
    
    /** Set Generated Alert.
    @param XX_L_GeneratedAlertCT_ID Para generar alerta */
    public void setXX_L_GeneratedAlertCT_ID (int XX_L_GeneratedAlertCT_ID)
    {
        if (XX_L_GeneratedAlertCT_ID <= 0) set_Value ("XX_L_GeneratedAlertCT_ID", null);
        else
        set_Value ("XX_L_GeneratedAlertCT_ID", Integer.valueOf(XX_L_GeneratedAlertCT_ID));
        
    }
    
    /** Get Generated Alert.
    @return Para generar alerta */
    public int getXX_L_GeneratedAlertCT_ID() 
    {
        return get_ValueAsInt("XX_L_GeneratedAlertCT_ID");
        
    }
    
    /** Set XX_L_JobPosition_AdminManag_ID.
    @param XX_L_JobPosition_AdminManag_ID XX_L_JobPosition_AdminManag_ID */
    public void setXX_L_JobPosition_AdminManag_ID (int XX_L_JobPosition_AdminManag_ID)
    {
        if (XX_L_JobPosition_AdminManag_ID <= 0) set_Value ("XX_L_JobPosition_AdminManag_ID", null);
        else
        set_Value ("XX_L_JobPosition_AdminManag_ID", Integer.valueOf(XX_L_JobPosition_AdminManag_ID));
        
    }
    
    /** Get XX_L_JobPosition_AdminManag_ID.
    @return XX_L_JobPosition_AdminManag_ID */
    public int getXX_L_JobPosition_AdminManag_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_AdminManag_ID");
        
    }
    
    /** Set Job Position Assistent.
    @param XX_L_JobPosition_Assis_ID Job Position Assistent */
    public void setXX_L_JobPosition_Assis_ID (int XX_L_JobPosition_Assis_ID)
    {
        if (XX_L_JobPosition_Assis_ID <= 0) set_Value ("XX_L_JobPosition_Assis_ID", null);
        else
        set_Value ("XX_L_JobPosition_Assis_ID", Integer.valueOf(XX_L_JobPosition_Assis_ID));
        
    }
    
    /** Get Job Position Assistent.
    @return Job Position Assistent */
    public int getXX_L_JobPosition_Assis_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_Assis_ID");
        
    }
    
    /** Set Job Position Ayudante.
    @param XX_L_JobPosition_Ayudante_ID Job Position Ayudante */
    public void setXX_L_JobPosition_Ayudante_ID (int XX_L_JobPosition_Ayudante_ID)
    {
        if (XX_L_JobPosition_Ayudante_ID <= 0) set_Value ("XX_L_JobPosition_Ayudante_ID", null);
        else
        set_Value ("XX_L_JobPosition_Ayudante_ID", Integer.valueOf(XX_L_JobPosition_Ayudante_ID));
        
    }
    
    /** Get Job Position Ayudante.
    @return Job Position Ayudante */
    public int getXX_L_JobPosition_Ayudante_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_Ayudante_ID");
        
    }
    
    /** Set Job Position User Buyer.
    @param XX_L_JobPosition_Buyer_ID Job Position User Buyer */
    public void setXX_L_JobPosition_Buyer_ID (int XX_L_JobPosition_Buyer_ID)
    {
        if (XX_L_JobPosition_Buyer_ID <= 0) set_Value ("XX_L_JobPosition_Buyer_ID", null);
        else
        set_Value ("XX_L_JobPosition_Buyer_ID", Integer.valueOf(XX_L_JobPosition_Buyer_ID));
        
    }
    
    /** Get Job Position User Buyer.
    @return Job Position User Buyer */
    public int getXX_L_JobPosition_Buyer_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_Buyer_ID");
        
    }
    
    /** Set Job Position Category Manager.
    @param XX_L_JobPosition_CategMan_ID Job Position Category Manager */
    public void setXX_L_JobPosition_CategMan_ID (int XX_L_JobPosition_CategMan_ID)
    {
        if (XX_L_JobPosition_CategMan_ID <= 0) set_Value ("XX_L_JobPosition_CategMan_ID", null);
        else
        set_Value ("XX_L_JobPosition_CategMan_ID", Integer.valueOf(XX_L_JobPosition_CategMan_ID));
        
    }
    
    /** Get Job Position Category Manager.
    @return Job Position Category Manager */
    public int getXX_L_JobPosition_CategMan_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_CategMan_ID");
        
    }
    
    /** Set Checkup Assistant.
    @param XX_L_JobPosition_ChAssi_ID Checkup Assistant */
    public void setXX_L_JobPosition_ChAssi_ID (int XX_L_JobPosition_ChAssi_ID)
    {
        if (XX_L_JobPosition_ChAssi_ID <= 0) set_Value ("XX_L_JobPosition_ChAssi_ID", null);
        else
        set_Value ("XX_L_JobPosition_ChAssi_ID", Integer.valueOf(XX_L_JobPosition_ChAssi_ID));
        
    }
    
    /** Get Checkup Assistant.
    @return Checkup Assistant */
    public int getXX_L_JobPosition_ChAssi_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_ChAssi_ID");
        
    }
    
    /** Set Job Position Checkup Auxiliary.
    @param XX_L_JobPosition_ChAuxi_ID Job Position Checkup Auxiliary */
    public void setXX_L_JobPosition_ChAuxi_ID (int XX_L_JobPosition_ChAuxi_ID)
    {
        if (XX_L_JobPosition_ChAuxi_ID <= 0) set_Value ("XX_L_JobPosition_ChAuxi_ID", null);
        else
        set_Value ("XX_L_JobPosition_ChAuxi_ID", Integer.valueOf(XX_L_JobPosition_ChAuxi_ID));
        
    }
    
    /** Get Job Position Checkup Auxiliary.
    @return Job Position Checkup Auxiliary */
    public int getXX_L_JobPosition_ChAuxi_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_ChAuxi_ID");
        
    }
    
    /** Set Job Position Coordinador Chequeo.
    @param XX_L_JobPosition_CoorCheq_ID Job Position Coordinador Chequeo */
    public void setXX_L_JobPosition_CoorCheq_ID (int XX_L_JobPosition_CoorCheq_ID)
    {
        if (XX_L_JobPosition_CoorCheq_ID <= 0) set_Value ("XX_L_JobPosition_CoorCheq_ID", null);
        else
        set_Value ("XX_L_JobPosition_CoorCheq_ID", Integer.valueOf(XX_L_JobPosition_CoorCheq_ID));
        
    }
    
    /** Get Job Position Coordinador Chequeo.
    @return Job Position Coordinador Chequeo */
    public int getXX_L_JobPosition_CoorCheq_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_CoorCheq_ID");
        
    }
    
    /** Set Asesor de Deposito.
    @param XX_L_JobPosition_DepAse_ID Asesor de Deposito */
    public void setXX_L_JobPosition_DepAse_ID (int XX_L_JobPosition_DepAse_ID)
    {
        if (XX_L_JobPosition_DepAse_ID <= 0) set_Value ("XX_L_JobPosition_DepAse_ID", null);
        else
        set_Value ("XX_L_JobPosition_DepAse_ID", Integer.valueOf(XX_L_JobPosition_DepAse_ID));
        
    }
    
    /** Get Asesor de Deposito.
    @return Asesor de Deposito */
    public int getXX_L_JobPosition_DepAse_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_DepAse_ID");
        
    }
    
    /** Set Job Position Driver.
    @param XX_L_JobPosition_Driver_ID Job Position Driver */
    public void setXX_L_JobPosition_Driver_ID (int XX_L_JobPosition_Driver_ID)
    {
        if (XX_L_JobPosition_Driver_ID <= 0) set_Value ("XX_L_JobPosition_Driver_ID", null);
        else
        set_Value ("XX_L_JobPosition_Driver_ID", Integer.valueOf(XX_L_JobPosition_Driver_ID));
        
    }
    
    /** Get Job Position Driver.
    @return Job Position Driver */
    public int getXX_L_JobPosition_Driver_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_Driver_ID");
        
    }
    
    /** Set Job Position Marketing Area Manager.
    @param XX_L_JobPosition_GAMerc_ID Job Position Marketing Area Manager */
    public void setXX_L_JobPosition_GAMerc_ID (int XX_L_JobPosition_GAMerc_ID)
    {
        if (XX_L_JobPosition_GAMerc_ID <= 0) set_Value ("XX_L_JobPosition_GAMerc_ID", null);
        else
        set_Value ("XX_L_JobPosition_GAMerc_ID", Integer.valueOf(XX_L_JobPosition_GAMerc_ID));
        
    }
    
    /** Get Job Position Marketing Area Manager.
    @return Job Position Marketing Area Manager */
    public int getXX_L_JobPosition_GAMerc_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_GAMerc_ID");
        
    }
    
    /** Set Job Position Import Manager.
    @param XX_L_JobPosition_ImporMan_ID Job Position Import Manager */
    public void setXX_L_JobPosition_ImporMan_ID (int XX_L_JobPosition_ImporMan_ID)
    {
        if (XX_L_JobPosition_ImporMan_ID <= 0) set_Value ("XX_L_JobPosition_ImporMan_ID", null);
        else
        set_Value ("XX_L_JobPosition_ImporMan_ID", Integer.valueOf(XX_L_JobPosition_ImporMan_ID));
        
    }
    
    /** Get Job Position Import Manager.
    @return Job Position Import Manager */
    public int getXX_L_JobPosition_ImporMan_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_ImporMan_ID");
        
    }
    
    /** Set Inventory Assesor.
    @param XX_L_JobPosition_InvAsses_ID Inventory Assesor */
    public void setXX_L_JobPosition_InvAsses_ID (int XX_L_JobPosition_InvAsses_ID)
    {
        if (XX_L_JobPosition_InvAsses_ID <= 0) set_Value ("XX_L_JobPosition_InvAsses_ID", null);
        else
        set_Value ("XX_L_JobPosition_InvAsses_ID", Integer.valueOf(XX_L_JobPosition_InvAsses_ID));
        
    }
    
    /** Get Inventory Assesor.
    @return Inventory Assesor */
    public int getXX_L_JobPosition_InvAsses_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_InvAsses_ID");
        
    }
    
    /** Set Job Position Inventory Scheduler.
    @param XX_L_JobPosition_InvenSched_ID Job Position Inventory Scheduler */
    public void setXX_L_JobPosition_InvenSched_ID (int XX_L_JobPosition_InvenSched_ID)
    {
        if (XX_L_JobPosition_InvenSched_ID <= 0) set_Value ("XX_L_JobPosition_InvenSched_ID", null);
        else
        set_Value ("XX_L_JobPosition_InvenSched_ID", Integer.valueOf(XX_L_JobPosition_InvenSched_ID));
        
    }
    
    /** Get Job Position Inventory Scheduler.
    @return Job Position Inventory Scheduler */
    public int getXX_L_JobPosition_InvenSched_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_InvenSched_ID");
        
    }
    
    /** Set Job Position Planning Manager.
    @param XX_L_JobPosition_PlanMan_ID Job Position Planning Manager */
    public void setXX_L_JobPosition_PlanMan_ID (int XX_L_JobPosition_PlanMan_ID)
    {
        if (XX_L_JobPosition_PlanMan_ID <= 0) set_Value ("XX_L_JobPosition_PlanMan_ID", null);
        else
        set_Value ("XX_L_JobPosition_PlanMan_ID", Integer.valueOf(XX_L_JobPosition_PlanMan_ID));
        
    }
    
    /** Get Job Position Planning Manager.
    @return Job Position Planning Manager */
    public int getXX_L_JobPosition_PlanMan_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_PlanMan_ID");
        
    }
    
    /** Set Job Position Asistente de Almacén.
    @param XX_L_JobPosition_StoreAssis_ID Job Position Asistente de Almacén */
    public void setXX_L_JobPosition_StoreAssis_ID (int XX_L_JobPosition_StoreAssis_ID)
    {
        if (XX_L_JobPosition_StoreAssis_ID <= 0) set_Value ("XX_L_JobPosition_StoreAssis_ID", null);
        else
        set_Value ("XX_L_JobPosition_StoreAssis_ID", Integer.valueOf(XX_L_JobPosition_StoreAssis_ID));
        
    }
    
    /** Get Job Position Asistente de Almacén.
    @return Job Position Asistente de Almacén */
    public int getXX_L_JobPosition_StoreAssis_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_StoreAssis_ID");
        
    }
    
    /** Set Job Position Store Manager.
    @param XX_L_JobPosition_StoreMan_ID Job Position Store Manager */
    public void setXX_L_JobPosition_StoreMan_ID (int XX_L_JobPosition_StoreMan_ID)
    {
        if (XX_L_JobPosition_StoreMan_ID <= 0) set_Value ("XX_L_JobPosition_StoreMan_ID", null);
        else
        set_Value ("XX_L_JobPosition_StoreMan_ID", Integer.valueOf(XX_L_JobPosition_StoreMan_ID));
        
    }
    
    /** Get Job Position Store Manager.
    @return Job Position Store Manager */
    public int getXX_L_JobPosition_StoreMan_ID() 
    {
        return get_ValueAsInt("XX_L_JobPosition_StoreMan_ID");
        
    }
    
    /** Set Locator CD-Chequeado.
    @param XX_L_LocatorCDChequeado_ID Locator CD-Chequeado */
    public void setXX_L_LocatorCDChequeado_ID (int XX_L_LocatorCDChequeado_ID)
    {
        if (XX_L_LocatorCDChequeado_ID <= 0) set_Value ("XX_L_LocatorCDChequeado_ID", null);
        else
        set_Value ("XX_L_LocatorCDChequeado_ID", Integer.valueOf(XX_L_LocatorCDChequeado_ID));
        
    }
    
    /** Get Locator CD-Chequeado.
    @return Locator CD-Chequeado */
    public int getXX_L_LocatorCDChequeado_ID() 
    {
        return get_ValueAsInt("XX_L_LocatorCDChequeado_ID");
        
    }
    
    /** Set Locator CD-Devolucion.
    @param XX_L_LocatorCDDevolucion_ID Locator CD-Devolucion */
    public void setXX_L_LocatorCDDevolucion_ID (int XX_L_LocatorCDDevolucion_ID)
    {
        if (XX_L_LocatorCDDevolucion_ID <= 0) set_Value ("XX_L_LocatorCDDevolucion_ID", null);
        else
        set_Value ("XX_L_LocatorCDDevolucion_ID", Integer.valueOf(XX_L_LocatorCDDevolucion_ID));
        
    }
    
    /** Get Locator CD-Devolucion.
    @return Locator CD-Devolucion */
    public int getXX_L_LocatorCDDevolucion_ID() 
    {
        return get_ValueAsInt("XX_L_LocatorCDDevolucion_ID");
        
    }
    
    /** Set Locator CD-EnTransito.
    @param XX_L_LocatorCDEnTransito_ID Locator CD-EnTransito */
    public void setXX_L_LocatorCDEnTransito_ID (int XX_L_LocatorCDEnTransito_ID)
    {
        if (XX_L_LocatorCDEnTransito_ID <= 0) set_Value ("XX_L_LocatorCDEnTransito_ID", null);
        else
        set_Value ("XX_L_LocatorCDEnTransito_ID", Integer.valueOf(XX_L_LocatorCDEnTransito_ID));
        
    }
    
    /** Get Locator CD-EnTransito.
    @return Locator CD-EnTransito */
    public int getXX_L_LocatorCDEnTransito_ID() 
    {
        return get_ValueAsInt("XX_L_LocatorCDEnTransito_ID");
        
    }
    
    /** Set Locator CD-Merma.
    @param XX_L_LocatorCDMerma_ID Locator CD-Merma */
    public void setXX_L_LocatorCDMerma_ID (int XX_L_LocatorCDMerma_ID)
    {
        if (XX_L_LocatorCDMerma_ID <= 0) set_Value ("XX_L_LocatorCDMerma_ID", null);
        else
        set_Value ("XX_L_LocatorCDMerma_ID", Integer.valueOf(XX_L_LocatorCDMerma_ID));
        
    }
    
    /** Get Locator CD-Merma.
    @return Locator CD-Merma */
    public int getXX_L_LocatorCDMerma_ID() 
    {
        return get_ValueAsInt("XX_L_LocatorCDMerma_ID");
        
    }
    
    /** Set Color Attribute.
    @param XX_L_M_AttributeColor_ID Color Attribute */
    public void setXX_L_M_AttributeColor_ID (int XX_L_M_AttributeColor_ID)
    {
        if (XX_L_M_AttributeColor_ID <= 0) set_Value ("XX_L_M_AttributeColor_ID", null);
        else
        set_Value ("XX_L_M_AttributeColor_ID", Integer.valueOf(XX_L_M_AttributeColor_ID));
        
    }
    
    /** Get Color Attribute.
    @return Color Attribute */
    public int getXX_L_M_AttributeColor_ID() 
    {
        return get_ValueAsInt("XX_L_M_AttributeColor_ID");
        
    }
    
    /** Set Size Attribute.
    @param XX_L_M_AttributeSize_ID Size Attribute */
    public void setXX_L_M_AttributeSize_ID (int XX_L_M_AttributeSize_ID)
    {
        if (XX_L_M_AttributeSize_ID <= 0) set_Value ("XX_L_M_AttributeSize_ID", null);
        else
        set_Value ("XX_L_M_AttributeSize_ID", Integer.valueOf(XX_L_M_AttributeSize_ID));
        
    }
    
    /** Get Size Attribute.
    @return Size Attribute */
    public int getXX_L_M_AttributeSize_ID() 
    {
        return get_ValueAsInt("XX_L_M_AttributeSize_ID");
        
    }
    
    /** Set Estimated Date Motive Change (Vendor).
    @param XX_L_MotiveChangedByVendor_ID Estimated Date Motive Change (Vendor) */
    public void setXX_L_MotiveChangedByVendor_ID (int XX_L_MotiveChangedByVendor_ID)
    {
        if (XX_L_MotiveChangedByVendor_ID <= 0) set_Value ("XX_L_MotiveChangedByVendor_ID", null);
        else
        set_Value ("XX_L_MotiveChangedByVendor_ID", Integer.valueOf(XX_L_MotiveChangedByVendor_ID));
        
    }
    
    /** Get Estimated Date Motive Change (Vendor).
    @return Estimated Date Motive Change (Vendor) */
    public int getXX_L_MotiveChangedByVendor_ID() 
    {
        return get_ValueAsInt("XX_L_MotiveChangedByVendor_ID");
        
    }
    
    /** Set Motive Stays CD.
    @param XX_L_MotiveStaysCD_ID Motive Stays CD */
    public void setXX_L_MotiveStaysCD_ID (int XX_L_MotiveStaysCD_ID)
    {
        if (XX_L_MotiveStaysCD_ID <= 0) set_Value ("XX_L_MotiveStaysCD_ID", null);
        else
        set_Value ("XX_L_MotiveStaysCD_ID", Integer.valueOf(XX_L_MotiveStaysCD_ID));
        
    }
    
    /** Get Motive Stays CD.
    @return Motive Stays CD */
    public int getXX_L_MotiveStaysCD_ID() 
    {
        return get_ValueAsInt("XX_L_MotiveStaysCD_ID");
        
    }
    
    /** Set MovementCD.
    @param XX_L_MovementCD_ID MovementCD */
    public void setXX_L_MovementCD_ID (int XX_L_MovementCD_ID)
    {
        if (XX_L_MovementCD_ID <= 0) set_Value ("XX_L_MovementCD_ID", null);
        else
        set_Value ("XX_L_MovementCD_ID", Integer.valueOf(XX_L_MovementCD_ID));
        
    }
    
    /** Get MovementCD.
    @return MovementCD */
    public int getXX_L_MovementCD_ID() 
    {
        return get_ValueAsInt("XX_L_MovementCD_ID");
        
    }
    
    /** Set Mail Template Alert Import.
    @param XX_L_MT_AlertImport_ID Mail Template Alert Import */
    public void setXX_L_MT_AlertImport_ID (int XX_L_MT_AlertImport_ID)
    {
        if (XX_L_MT_AlertImport_ID <= 0) set_Value ("XX_L_MT_AlertImport_ID", null);
        else
        set_Value ("XX_L_MT_AlertImport_ID", Integer.valueOf(XX_L_MT_AlertImport_ID));
        
    }
    
    /** Get Mail Template Alert Import.
    @return Mail Template Alert Import */
    public int getXX_L_MT_AlertImport_ID() 
    {
        return get_ValueAsInt("XX_L_MT_AlertImport_ID");
        
    }
    
    /** Set Mail Template Alert Order Import.
    @param XX_L_MT_AlertOrderImport_ID Mail Template Alert Order Import */
    public void setXX_L_MT_AlertOrderImport_ID (int XX_L_MT_AlertOrderImport_ID)
    {
        if (XX_L_MT_AlertOrderImport_ID <= 0) set_Value ("XX_L_MT_AlertOrderImport_ID", null);
        else
        set_Value ("XX_L_MT_AlertOrderImport_ID", Integer.valueOf(XX_L_MT_AlertOrderImport_ID));
        
    }
    
    /** Get Mail Template Alert Order Import.
    @return Mail Template Alert Order Import */
    public int getXX_L_MT_AlertOrderImport_ID() 
    {
        return get_ValueAsInt("XX_L_MT_AlertOrderImport_ID");
        
    }
    
    /** Set Mail Template Authorized Order.
    @param XX_L_MT_AuthorizedOrder_ID Mail Template Authorized Order */
    public void setXX_L_MT_AuthorizedOrder_ID (int XX_L_MT_AuthorizedOrder_ID)
    {
        if (XX_L_MT_AuthorizedOrder_ID <= 0) set_Value ("XX_L_MT_AuthorizedOrder_ID", null);
        else
        set_Value ("XX_L_MT_AuthorizedOrder_ID", Integer.valueOf(XX_L_MT_AuthorizedOrder_ID));
        
    }
    
    /** Get Mail Template Authorized Order.
    @return Mail Template Authorized Order */
    public int getXX_L_MT_AuthorizedOrder_ID() 
    {
        return get_ValueAsInt("XX_L_MT_AuthorizedOrder_ID");
        
    }
    
    /** Set Mail Template Cambio Fecha de Llegada.
    @param XX_L_MT_CambFechLleg_ID Mail Template Cambio Fecha de Llegada */
    public void setXX_L_MT_CambFechLleg_ID (int XX_L_MT_CambFechLleg_ID)
    {
        if (XX_L_MT_CambFechLleg_ID <= 0) set_Value ("XX_L_MT_CambFechLleg_ID", null);
        else
        set_Value ("XX_L_MT_CambFechLleg_ID", Integer.valueOf(XX_L_MT_CambFechLleg_ID));
        
    }
    
    /** Get Mail Template Cambio Fecha de Llegada.
    @return Mail Template Cambio Fecha de Llegada */
    public int getXX_L_MT_CambFechLleg_ID() 
    {
        return get_ValueAsInt("XX_L_MT_CambFechLleg_ID");
        
    }
    
    /** Set Mail Template Code Tariff.
    @param XX_L_MT_CodeTariff_ID Mail Template Code Tariff */
    public void setXX_L_MT_CodeTariff_ID (int XX_L_MT_CodeTariff_ID)
    {
        if (XX_L_MT_CodeTariff_ID <= 0) set_Value ("XX_L_MT_CodeTariff_ID", null);
        else
        set_Value ("XX_L_MT_CodeTariff_ID", Integer.valueOf(XX_L_MT_CodeTariff_ID));
        
    }
    
    /** Get Mail Template Code Tariff.
    @return Mail Template Code Tariff */
    public int getXX_L_MT_CodeTariff_ID() 
    {
        return get_ValueAsInt("XX_L_MT_CodeTariff_ID");
        
    }
    
    /** Set Mail Template Credit Notify Return.
    @param XX_L_MT_CreditNotify_ID Mail Template Credit Notify Return */
    public void setXX_L_MT_CreditNotify_ID (int XX_L_MT_CreditNotify_ID)
    {
        if (XX_L_MT_CreditNotify_ID <= 0) set_Value ("XX_L_MT_CreditNotify_ID", null);
        else
        set_Value ("XX_L_MT_CreditNotify_ID", Integer.valueOf(XX_L_MT_CreditNotify_ID));
        
    }
    
    /** Get Mail Template Credit Notify Return.
    @return Mail Template Credit Notify Return */
    public int getXX_L_MT_CreditNotify_ID() 
    {
        return get_ValueAsInt("XX_L_MT_CreditNotify_ID");
        
    }
    
    /** Set Mail Template Critical Task Manager.
    @param XX_L_MT_CriticalTaskMana_ID Mail Template Critical Task Manager */
    public void setXX_L_MT_CriticalTaskMana_ID (int XX_L_MT_CriticalTaskMana_ID)
    {
        if (XX_L_MT_CriticalTaskMana_ID <= 0) set_Value ("XX_L_MT_CriticalTaskMana_ID", null);
        else
        set_Value ("XX_L_MT_CriticalTaskMana_ID", Integer.valueOf(XX_L_MT_CriticalTaskMana_ID));
        
    }
    
    /** Get Mail Template Critical Task Manager.
    @return Mail Template Critical Task Manager */
    public int getXX_L_MT_CriticalTaskMana_ID() 
    {
        return get_ValueAsInt("XX_L_MT_CriticalTaskMana_ID");
        
    }
    
    /** Set Mail Template Critical Task Supervisor .
    @param XX_L_MT_CriticalTaskSupe_ID Mail Template Critical Task Supervisor  */
    public void setXX_L_MT_CriticalTaskSupe_ID (int XX_L_MT_CriticalTaskSupe_ID)
    {
        if (XX_L_MT_CriticalTaskSupe_ID <= 0) set_Value ("XX_L_MT_CriticalTaskSupe_ID", null);
        else
        set_Value ("XX_L_MT_CriticalTaskSupe_ID", Integer.valueOf(XX_L_MT_CriticalTaskSupe_ID));
        
    }
    
    /** Get Mail Template Critical Task Supervisor .
    @return Mail Template Critical Task Supervisor  */
    public int getXX_L_MT_CriticalTaskSupe_ID() 
    {
        return get_ValueAsInt("XX_L_MT_CriticalTaskSupe_ID");
        
    }
    
    /** Set XX_L_MT_DebCredAdvise_ID.
    @param XX_L_MT_DebCredAdvise_ID XX_L_MT_DebCredAdvise_ID */
    public void setXX_L_MT_DebCredAdvise_ID (int XX_L_MT_DebCredAdvise_ID)
    {
        if (XX_L_MT_DebCredAdvise_ID <= 0) set_Value ("XX_L_MT_DebCredAdvise_ID", null);
        else
        set_Value ("XX_L_MT_DebCredAdvise_ID", Integer.valueOf(XX_L_MT_DebCredAdvise_ID));
        
    }
    
    /** Get XX_L_MT_DebCredAdvise_ID.
    @return XX_L_MT_DebCredAdvise_ID */
    public int getXX_L_MT_DebCredAdvise_ID() 
    {
        return get_ValueAsInt("XX_L_MT_DebCredAdvise_ID");
        
    }
    
    /** Set PO Denied Approval Mail Template.
    @param XX_L_MT_DeniedApproval_ID PO Denied Approval Mail Template */
    public void setXX_L_MT_DeniedApproval_ID (int XX_L_MT_DeniedApproval_ID)
    {
        if (XX_L_MT_DeniedApproval_ID <= 0) set_Value ("XX_L_MT_DeniedApproval_ID", null);
        else
        set_Value ("XX_L_MT_DeniedApproval_ID", Integer.valueOf(XX_L_MT_DeniedApproval_ID));
        
    }
    
    /** Get PO Denied Approval Mail Template.
    @return PO Denied Approval Mail Template */
    public int getXX_L_MT_DeniedApproval_ID() 
    {
        return get_ValueAsInt("XX_L_MT_DeniedApproval_ID");
        
    }
    
    /** Set Mail Template Difference Factor.
    @param XX_L_MT_DifferenceFactor_ID Mail Template Difference Factor */
    public void setXX_L_MT_DifferenceFactor_ID (int XX_L_MT_DifferenceFactor_ID)
    {
        if (XX_L_MT_DifferenceFactor_ID <= 0) set_Value ("XX_L_MT_DifferenceFactor_ID", null);
        else
        set_Value ("XX_L_MT_DifferenceFactor_ID", Integer.valueOf(XX_L_MT_DifferenceFactor_ID));
        
    }
    
    /** Get Mail Template Difference Factor.
    @return Mail Template Difference Factor */
    public int getXX_L_MT_DifferenceFactor_ID() 
    {
        return get_ValueAsInt("XX_L_MT_DifferenceFactor_ID");
        
    }
    
    /** Set Mail Template Need Approve Distrib Auto.
    @param XX_L_MT_NeedApproveDistAuto_ID Mail Template Need Approve Distrib Auto */
    public void setXX_L_MT_NeedApproveDistAuto_ID (int XX_L_MT_NeedApproveDistAuto_ID)
    {
        if (XX_L_MT_NeedApproveDistAuto_ID <= 0) set_Value ("XX_L_MT_NeedApproveDistAuto_ID", null);
        else
        set_Value ("XX_L_MT_NeedApproveDistAuto_ID", Integer.valueOf(XX_L_MT_NeedApproveDistAuto_ID));
        
    }
    
    /** Get Mail Template Need Approve Distrib Auto.
    @return Mail Template Need Approve Distrib Auto */
    public int getXX_L_MT_NeedApproveDistAuto_ID() 
    {
        return get_ValueAsInt("XX_L_MT_NeedApproveDistAuto_ID");
        
    }
    
    /** Set Mail Template Need Distrib PO.
    @param XX_L_MT_NeedDistribPO_ID Mail Template Need Distrib PO */
    public void setXX_L_MT_NeedDistribPO_ID (int XX_L_MT_NeedDistribPO_ID)
    {
        if (XX_L_MT_NeedDistribPO_ID <= 0) set_Value ("XX_L_MT_NeedDistribPO_ID", null);
        else
        set_Value ("XX_L_MT_NeedDistribPO_ID", Integer.valueOf(XX_L_MT_NeedDistribPO_ID));
        
    }
    
    /** Get Mail Template Need Distrib PO.
    @return Mail Template Need Distrib PO */
    public int getXX_L_MT_NeedDistribPO_ID() 
    {
        return get_ValueAsInt("XX_L_MT_NeedDistribPO_ID");
        
    }
    
    /** Set Mail Template Need Redistrib PO.
    @param XX_L_MT_NeedRedistribPO_ID Mail Template Need Redistrib PO */
    public void setXX_L_MT_NeedRedistribPO_ID (int XX_L_MT_NeedRedistribPO_ID)
    {
        if (XX_L_MT_NeedRedistribPO_ID <= 0) set_Value ("XX_L_MT_NeedRedistribPO_ID", null);
        else
        set_Value ("XX_L_MT_NeedRedistribPO_ID", Integer.valueOf(XX_L_MT_NeedRedistribPO_ID));
        
    }
    
    /** Get Mail Template Need Redistrib PO.
    @return Mail Template Need Redistrib PO */
    public int getXX_L_MT_NeedRedistribPO_ID() 
    {
        return get_ValueAsInt("XX_L_MT_NeedRedistribPO_ID");
        
    }
    
    /** Set Mail Template News Report.
    @param XX_L_MT_NewsReport_ID Mail Template News Report */
    public void setXX_L_MT_NewsReport_ID (int XX_L_MT_NewsReport_ID)
    {
        if (XX_L_MT_NewsReport_ID <= 0) set_Value ("XX_L_MT_NewsReport_ID", null);
        else
        set_Value ("XX_L_MT_NewsReport_ID", Integer.valueOf(XX_L_MT_NewsReport_ID));
        
    }
    
    /** Get Mail Template News Report.
    @return Mail Template News Report */
    public int getXX_L_MT_NewsReport_ID() 
    {
        return get_ValueAsInt("XX_L_MT_NewsReport_ID");
        
    }
    
    /** Set Next Delivery Mail Template.
    @param XX_L_MT_NextDelivery_ID Next Delivery Mail Template */
    public void setXX_L_MT_NextDelivery_ID (int XX_L_MT_NextDelivery_ID)
    {
        if (XX_L_MT_NextDelivery_ID <= 0) set_Value ("XX_L_MT_NextDelivery_ID", null);
        else
        set_Value ("XX_L_MT_NextDelivery_ID", Integer.valueOf(XX_L_MT_NextDelivery_ID));
        
    }
    
    /** Get Next Delivery Mail Template.
    @return Next Delivery Mail Template */
    public int getXX_L_MT_NextDelivery_ID() 
    {
        return get_ValueAsInt("XX_L_MT_NextDelivery_ID");
        
    }
    
    /** Set Mail Template For Not Authorized Order.
    @param XX_L_MT_NotAuthorizedOrder_ID Mail Template For Not Authorized Order */
    public void setXX_L_MT_NotAuthorizedOrder_ID (int XX_L_MT_NotAuthorizedOrder_ID)
    {
        if (XX_L_MT_NotAuthorizedOrder_ID <= 0) set_Value ("XX_L_MT_NotAuthorizedOrder_ID", null);
        else
        set_Value ("XX_L_MT_NotAuthorizedOrder_ID", Integer.valueOf(XX_L_MT_NotAuthorizedOrder_ID));
        
    }
    
    /** Get Mail Template For Not Authorized Order.
    @return Mail Template For Not Authorized Order */
    public int getXX_L_MT_NotAuthorizedOrder_ID() 
    {
        return get_ValueAsInt("XX_L_MT_NotAuthorizedOrder_ID");
        
    }
    
    /** Set Mail Template Approve and Distributed is the O/C.
    @param XX_L_MT_OrderApprDistrib_ID Mail Template Approve and Distributed is the O/C */
    public void setXX_L_MT_OrderApprDistrib_ID (int XX_L_MT_OrderApprDistrib_ID)
    {
        if (XX_L_MT_OrderApprDistrib_ID <= 0) set_Value ("XX_L_MT_OrderApprDistrib_ID", null);
        else
        set_Value ("XX_L_MT_OrderApprDistrib_ID", Integer.valueOf(XX_L_MT_OrderApprDistrib_ID));
        
    }
    
    /** Get Mail Template Approve and Distributed is the O/C.
    @return Mail Template Approve and Distributed is the O/C */
    public int getXX_L_MT_OrderApprDistrib_ID() 
    {
        return get_ValueAsInt("XX_L_MT_OrderApprDistrib_ID");
        
    }
    
    /** Set MT Overdrawn Order.
    @param XX_L_MT_OverdrawnOrder_ID MT Overdrawn Order */
    public void setXX_L_MT_OverdrawnOrder_ID (int XX_L_MT_OverdrawnOrder_ID)
    {
        if (XX_L_MT_OverdrawnOrder_ID <= 0) set_Value ("XX_L_MT_OverdrawnOrder_ID", null);
        else
        set_Value ("XX_L_MT_OverdrawnOrder_ID", Integer.valueOf(XX_L_MT_OverdrawnOrder_ID));
        
    }
    
    /** Get MT Overdrawn Order.
    @return MT Overdrawn Order */
    public int getXX_L_MT_OverdrawnOrder_ID() 
    {
        return get_ValueAsInt("XX_L_MT_OverdrawnOrder_ID");
        
    }
    
    /** Set Load Mail Template Purchase Order Is Ready.
    @param XX_L_MT_PO_Ready_ID Load Mail Template Purchase Order Is Ready */
    public void setXX_L_MT_PO_Ready_ID (int XX_L_MT_PO_Ready_ID)
    {
        if (XX_L_MT_PO_Ready_ID <= 0) set_Value ("XX_L_MT_PO_Ready_ID", null);
        else
        set_Value ("XX_L_MT_PO_Ready_ID", Integer.valueOf(XX_L_MT_PO_Ready_ID));
        
    }
    
    /** Get Load Mail Template Purchase Order Is Ready.
    @return Load Mail Template Purchase Order Is Ready */
    public int getXX_L_MT_PO_Ready_ID() 
    {
        return get_ValueAsInt("XX_L_MT_PO_Ready_ID");
        
    }
    
    /** Set Mail Template PO Was Distributed.
    @param XX_L_MT_POWasDistrib_ID Mail Template PO Was Distributed */
    public void setXX_L_MT_POWasDistrib_ID (int XX_L_MT_POWasDistrib_ID)
    {
        if (XX_L_MT_POWasDistrib_ID <= 0) set_Value ("XX_L_MT_POWasDistrib_ID", null);
        else
        set_Value ("XX_L_MT_POWasDistrib_ID", Integer.valueOf(XX_L_MT_POWasDistrib_ID));
        
    }
    
    /** Get Mail Template PO Was Distributed.
    @return Mail Template PO Was Distributed */
    public int getXX_L_MT_POWasDistrib_ID() 
    {
        return get_ValueAsInt("XX_L_MT_POWasDistrib_ID");
        
    }
    
    /** Set Mail Template Product Reference.
    @param XX_L_MT_ProductReference_ID Mail Template Product Reference */
    public void setXX_L_MT_ProductReference_ID (int XX_L_MT_ProductReference_ID)
    {
        if (XX_L_MT_ProductReference_ID <= 0) set_Value ("XX_L_MT_ProductReference_ID", null);
        else
        set_Value ("XX_L_MT_ProductReference_ID", Integer.valueOf(XX_L_MT_ProductReference_ID));
        
    }
    
    /** Get Mail Template Product Reference.
    @return Mail Template Product Reference */
    public int getXX_L_MT_ProductReference_ID() 
    {
        return get_ValueAsInt("XX_L_MT_ProductReference_ID");
        
    }
    
    /** Set XX_L_MT_PTransferApproval_ID.
    @param XX_L_MT_PTransferApproval_ID XX_L_MT_PTransferApproval_ID */
    public void setXX_L_MT_PTransferApproval_ID (int XX_L_MT_PTransferApproval_ID)
    {
        if (XX_L_MT_PTransferApproval_ID <= 0) set_Value ("XX_L_MT_PTransferApproval_ID", null);
        else
        set_Value ("XX_L_MT_PTransferApproval_ID", Integer.valueOf(XX_L_MT_PTransferApproval_ID));
        
    }
    
    /** Get XX_L_MT_PTransferApproval_ID.
    @return XX_L_MT_PTransferApproval_ID */
    public int getXX_L_MT_PTransferApproval_ID() 
    {
        return get_ValueAsInt("XX_L_MT_PTransferApproval_ID");
        
    }
    
    /** Set Mail Template Product Transfer .
    @param XX_L_MT_PTransferCreation_ID Mail Template Product Transfer  */
    public void setXX_L_MT_PTransferCreation_ID (int XX_L_MT_PTransferCreation_ID)
    {
        if (XX_L_MT_PTransferCreation_ID <= 0) set_Value ("XX_L_MT_PTransferCreation_ID", null);
        else
        set_Value ("XX_L_MT_PTransferCreation_ID", Integer.valueOf(XX_L_MT_PTransferCreation_ID));
        
    }
    
    /** Get Mail Template Product Transfer .
    @return Mail Template Product Transfer  */
    public int getXX_L_MT_PTransferCreation_ID() 
    {
        return get_ValueAsInt("XX_L_MT_PTransferCreation_ID");
        
    }
    
    /** Set Mail Template Send Invoice.
    @param XX_L_MT_SendInvoice_ID Mail Template Send Invoice */
    public void setXX_L_MT_SendInvoice_ID (int XX_L_MT_SendInvoice_ID)
    {
        if (XX_L_MT_SendInvoice_ID <= 0) set_Value ("XX_L_MT_SendInvoice_ID", null);
        else
        set_Value ("XX_L_MT_SendInvoice_ID", Integer.valueOf(XX_L_MT_SendInvoice_ID));
        
    }
    
    /** Get Mail Template Send Invoice.
    @return Mail Template Send Invoice */
    public int getXX_L_MT_SendInvoice_ID() 
    {
        return get_ValueAsInt("XX_L_MT_SendInvoice_ID");
        
    }
    
    /** Set Mail Template Return .
    @param XX_L_MT_SendMailReturns_ID Mail Template Return  */
    public void setXX_L_MT_SendMailReturns_ID (int XX_L_MT_SendMailReturns_ID)
    {
        if (XX_L_MT_SendMailReturns_ID <= 0) set_Value ("XX_L_MT_SendMailReturns_ID", null);
        else
        set_Value ("XX_L_MT_SendMailReturns_ID", Integer.valueOf(XX_L_MT_SendMailReturns_ID));
        
    }
    
    /** Get Mail Template Return .
    @return Mail Template Return  */
    public int getXX_L_MT_SendMailReturns_ID() 
    {
        return get_ValueAsInt("XX_L_MT_SendMailReturns_ID");
        
    }
    
    /** Set Mail Template Status Change.
    @param XX_L_MT_StaChange_ID Mail Template Status Change */
    public void setXX_L_MT_StaChange_ID (int XX_L_MT_StaChange_ID)
    {
        if (XX_L_MT_StaChange_ID <= 0) set_Value ("XX_L_MT_StaChange_ID", null);
        else
        set_Value ("XX_L_MT_StaChange_ID", Integer.valueOf(XX_L_MT_StaChange_ID));
        
    }
    
    /** Get Mail Template Status Change.
    @return Mail Template Status Change */
    public int getXX_L_MT_StaChange_ID() 
    {
        return get_ValueAsInt("XX_L_MT_StaChange_ID");
        
    }
    
    /** Set Mail Template Unsolicited Product.
    @param XX_L_MT_UnsolicitedProduct_ID Mail Template Unsolicited Product */
    public void setXX_L_MT_UnsolicitedProduct_ID (int XX_L_MT_UnsolicitedProduct_ID)
    {
        if (XX_L_MT_UnsolicitedProduct_ID <= 0) set_Value ("XX_L_MT_UnsolicitedProduct_ID", null);
        else
        set_Value ("XX_L_MT_UnsolicitedProduct_ID", Integer.valueOf(XX_L_MT_UnsolicitedProduct_ID));
        
    }
    
    /** Get Mail Template Unsolicited Product.
    @return Mail Template Unsolicited Product */
    public int getXX_L_MT_UnsolicitedProduct_ID() 
    {
        return get_ValueAsInt("XX_L_MT_UnsolicitedProduct_ID");
        
    }
    
    /** Set Mail Template Vendor Return.
    @param XX_L_MT_VendorReturn_ID Mail Template Vendor Return */
    public void setXX_L_MT_VendorReturn_ID (int XX_L_MT_VendorReturn_ID)
    {
        if (XX_L_MT_VendorReturn_ID <= 0) set_Value ("XX_L_MT_VendorReturn_ID", null);
        else
        set_Value ("XX_L_MT_VendorReturn_ID", Integer.valueOf(XX_L_MT_VendorReturn_ID));
        
    }
    
    /** Get Mail Template Vendor Return.
    @return Mail Template Vendor Return */
    public int getXX_L_MT_VendorReturn_ID() 
    {
        return get_ValueAsInt("XX_L_MT_VendorReturn_ID");
        
    }
    
    /** Set News Report Vendor.
    @param XX_L_NewsReportVendor_ID News Report Vendor */
    public void setXX_L_NewsReportVendor_ID (int XX_L_NewsReportVendor_ID)
    {
        if (XX_L_NewsReportVendor_ID <= 0) set_Value ("XX_L_NewsReportVendor_ID", null);
        else
        set_Value ("XX_L_NewsReportVendor_ID", Integer.valueOf(XX_L_NewsReportVendor_ID));
        
    }
    
    /** Get News Report Vendor.
    @return News Report Vendor */
    public int getXX_L_NewsReportVendor_ID() 
    {
        return get_ValueAsInt("XX_L_NewsReportVendor_ID");
        
    }
    
    /** Set Organization Centro de Distribucion.
    @param XX_L_OrganizationCD_ID Organization Centro de Distribucion */
    public void setXX_L_OrganizationCD_ID (int XX_L_OrganizationCD_ID)
    {
        if (XX_L_OrganizationCD_ID <= 0) set_Value ("XX_L_OrganizationCD_ID", null);
        else
        set_Value ("XX_L_OrganizationCD_ID", Integer.valueOf(XX_L_OrganizationCD_ID));
        
    }
    
    /** Get Organization Centro de Distribucion.
    @return Organization Centro de Distribucion */
    public int getXX_L_OrganizationCD_ID() 
    {
        return get_ValueAsInt("XX_L_OrganizationCD_ID");
        
    }
    
    /** Set Organization Control.
    @param XX_L_OrganizationControl_ID Organization Control */
    public void setXX_L_OrganizationControl_ID (int XX_L_OrganizationControl_ID)
    {
        if (XX_L_OrganizationControl_ID <= 0) set_Value ("XX_L_OrganizationControl_ID", null);
        else
        set_Value ("XX_L_OrganizationControl_ID", Integer.valueOf(XX_L_OrganizationControl_ID));
        
    }
    
    /** Get Organization Control.
    @return Organization Control */
    public int getXX_L_OrganizationControl_ID() 
    {
        return get_ValueAsInt("XX_L_OrganizationControl_ID");
        
    }
    
    /** Set Organización Logistica.
    @param XX_L_OrganizationLogistica_ID Organización Logistica */
    public void setXX_L_OrganizationLogistica_ID (int XX_L_OrganizationLogistica_ID)
    {
        if (XX_L_OrganizationLogistica_ID <= 0) set_Value ("XX_L_OrganizationLogistica_ID", null);
        else
        set_Value ("XX_L_OrganizationLogistica_ID", Integer.valueOf(XX_L_OrganizationLogistica_ID));
        
    }
    
    /** Get Organización Logistica.
    @return Organización Logistica */
    public int getXX_L_OrganizationLogistica_ID() 
    {
        return get_ValueAsInt("XX_L_OrganizationLogistica_ID");
        
    }
    
    /** Set AttributeSet Standard.
    @param XX_L_P_AttributeSetSt_ID AttributeSet Standard */
    public void setXX_L_P_AttributeSetSt_ID (int XX_L_P_AttributeSetSt_ID)
    {
        if (XX_L_P_AttributeSetSt_ID <= 0) set_Value ("XX_L_P_AttributeSetSt_ID", null);
        else
        set_Value ("XX_L_P_AttributeSetSt_ID", Integer.valueOf(XX_L_P_AttributeSetSt_ID));
        
    }
    
    /** Get AttributeSet Standard.
    @return AttributeSet Standard */
    public int getXX_L_P_AttributeSetSt_ID() 
    {
        return get_ValueAsInt("XX_L_P_AttributeSetSt_ID");
        
    }
    
    /** Set Product Class Combinado.
    @param XX_L_PC_Combinado_ID Product Class Combinado */
    public void setXX_L_PC_Combinado_ID (int XX_L_PC_Combinado_ID)
    {
        if (XX_L_PC_Combinado_ID <= 0) set_Value ("XX_L_PC_Combinado_ID", null);
        else
        set_Value ("XX_L_PC_Combinado_ID", Integer.valueOf(XX_L_PC_Combinado_ID));
        
    }
    
    /** Get Product Class Combinado.
    @return Product Class Combinado */
    public int getXX_L_PC_Combinado_ID() 
    {
        return get_ValueAsInt("XX_L_PC_Combinado_ID");
        
    }
    
    /** Set Product Class No Textil.
    @param XX_L_PC_NoTextil_ID Product Class No Textil */
    public void setXX_L_PC_NoTextil_ID (int XX_L_PC_NoTextil_ID)
    {
        if (XX_L_PC_NoTextil_ID <= 0) set_Value ("XX_L_PC_NoTextil_ID", null);
        else
        set_Value ("XX_L_PC_NoTextil_ID", Integer.valueOf(XX_L_PC_NoTextil_ID));
        
    }
    
    /** Get Product Class No Textil.
    @return Product Class No Textil */
    public int getXX_L_PC_NoTextil_ID() 
    {
        return get_ValueAsInt("XX_L_PC_NoTextil_ID");
        
    }
    
    /** Set Product Class Textil.
    @param XX_L_PC_Textil_ID Product Class Textil */
    public void setXX_L_PC_Textil_ID (int XX_L_PC_Textil_ID)
    {
        if (XX_L_PC_Textil_ID <= 0) set_Value ("XX_L_PC_Textil_ID", null);
        else
        set_Value ("XX_L_PC_Textil_ID", Integer.valueOf(XX_L_PC_Textil_ID));
        
    }
    
    /** Get Product Class Textil.
    @return Product Class Textil */
    public int getXX_L_PC_Textil_ID() 
    {
        return get_ValueAsInt("XX_L_PC_Textil_ID");
        
    }
    
    /** Set Print Format OC Imported Proforma.
    @param XX_L_PF_OCImportedPro_ID Print Format OC Imported Proforma */
    public void setXX_L_PF_OCImportedPro_ID (int XX_L_PF_OCImportedPro_ID)
    {
        if (XX_L_PF_OCImportedPro_ID <= 0) set_Value ("XX_L_PF_OCImportedPro_ID", null);
        else
        set_Value ("XX_L_PF_OCImportedPro_ID", Integer.valueOf(XX_L_PF_OCImportedPro_ID));
        
    }
    
    /** Get Print Format OC Imported Proforma.
    @return Print Format OC Imported Proforma */
    public int getXX_L_PF_OCImportedPro_ID() 
    {
        return get_ValueAsInt("XX_L_PF_OCImportedPro_ID");
        
    }
    
    /** Set Print Format OC National Proforma.
    @param XX_L_PF_OCNationalPro_ID Print Format OC National Proforma */
    public void setXX_L_PF_OCNationalPro_ID (int XX_L_PF_OCNationalPro_ID)
    {
        if (XX_L_PF_OCNationalPro_ID <= 0) set_Value ("XX_L_PF_OCNationalPro_ID", null);
        else
        set_Value ("XX_L_PF_OCNationalPro_ID", Integer.valueOf(XX_L_PF_OCNationalPro_ID));
        
    }
    
    /** Get Print Format OC National Proforma.
    @return Print Format OC National Proforma */
    public int getXX_L_PF_OCNationalPro_ID() 
    {
        return get_ValueAsInt("XX_L_PF_OCNationalPro_ID");
        
    }
    
    /** Set Subject Surtido Regular.
    @param XX_L_PO_Subj_SurtReg_ID Subject Surtido Regular */
    public void setXX_L_PO_Subj_SurtReg_ID (int XX_L_PO_Subj_SurtReg_ID)
    {
        if (XX_L_PO_Subj_SurtReg_ID <= 0) set_Value ("XX_L_PO_Subj_SurtReg_ID", null);
        else
        set_Value ("XX_L_PO_Subj_SurtReg_ID", Integer.valueOf(XX_L_PO_Subj_SurtReg_ID));
        
    }
    
    /** Get Subject Surtido Regular.
    @return Subject Surtido Regular */
    public int getXX_L_PO_Subj_SurtReg_ID() 
    {
        return get_ValueAsInt("XX_L_PO_Subj_SurtReg_ID");
        
    }
    
    /** Set Product PriceList Standard.
    @param XX_L_P_PriceListSt_ID Lista de Precio Standard para el Producto */
    public void setXX_L_P_PriceListSt_ID (int XX_L_P_PriceListSt_ID)
    {
        if (XX_L_P_PriceListSt_ID <= 0) set_Value ("XX_L_P_PriceListSt_ID", null);
        else
        set_Value ("XX_L_P_PriceListSt_ID", Integer.valueOf(XX_L_P_PriceListSt_ID));
        
    }
    
    /** Get Product PriceList Standard.
    @return Lista de Precio Standard para el Producto */
    public int getXX_L_P_PriceListSt_ID() 
    {
        return get_ValueAsInt("XX_L_P_PriceListSt_ID");
        
    }
    
    /** Set Forma para impresión de etiquetas de rebaja .
    @param XX_L_PrintDiscProdLabel2_ID Forma para impresión de etiquetas de rebaja  */
    public void setXX_L_PrintDiscProdLabel2_ID (int XX_L_PrintDiscProdLabel2_ID)
    {
        if (XX_L_PrintDiscProdLabel2_ID <= 0) set_Value ("XX_L_PrintDiscProdLabel2_ID", null);
        else
        set_Value ("XX_L_PrintDiscProdLabel2_ID", Integer.valueOf(XX_L_PrintDiscProdLabel2_ID));
        
    }
    
    /** Get Forma para impresión de etiquetas de rebaja .
    @return Forma para impresión de etiquetas de rebaja  */
    public int getXX_L_PrintDiscProdLabel2_ID() 
    {
        return get_ValueAsInt("XX_L_PrintDiscProdLabel2_ID");
        
    }
    
    /** Set Process Approve Distribution.
    @param XX_L_ProcessApproveDist_ID Process Approve Distribution */
    public void setXX_L_ProcessApproveDist_ID (int XX_L_ProcessApproveDist_ID)
    {
        if (XX_L_ProcessApproveDist_ID <= 0) set_Value ("XX_L_ProcessApproveDist_ID", null);
        else
        set_Value ("XX_L_ProcessApproveDist_ID", Integer.valueOf(XX_L_ProcessApproveDist_ID));
        
    }
    
    /** Get Process Approve Distribution.
    @return Process Approve Distribution */
    public int getXX_L_ProcessApproveDist_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessApproveDist_ID");
        
    }
    
    /** Set Assortment Plan.
    @param XX_L_ProcessAssortmentPlan_ID Assortment Plan */
    public void setXX_L_ProcessAssortmentPlan_ID (int XX_L_ProcessAssortmentPlan_ID)
    {
        if (XX_L_ProcessAssortmentPlan_ID <= 0) set_Value ("XX_L_ProcessAssortmentPlan_ID", null);
        else
        set_Value ("XX_L_ProcessAssortmentPlan_ID", Integer.valueOf(XX_L_ProcessAssortmentPlan_ID));
        
    }
    
    /** Get Assortment Plan.
    @return Assortment Plan */
    public int getXX_L_ProcessAssortmentPlan_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessAssortmentPlan_ID");
        
    }
    
    /** Set Calculation Piece.
    @param XX_L_ProcessCalcuPiece_ID Calculation Piece */
    public void setXX_L_ProcessCalcuPiece_ID (int XX_L_ProcessCalcuPiece_ID)
    {
        if (XX_L_ProcessCalcuPiece_ID <= 0) set_Value ("XX_L_ProcessCalcuPiece_ID", null);
        else
        set_Value ("XX_L_ProcessCalcuPiece_ID", Integer.valueOf(XX_L_ProcessCalcuPiece_ID));
        
    }
    
    /** Get Calculation Piece.
    @return Calculation Piece */
    public int getXX_L_ProcessCalcuPiece_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessCalcuPiece_ID");
        
    }
    
    /** Set XX_L_ProcessExportSalesAsso_ID.
    @param XX_L_ProcessExportSalesAsso_ID ID de proceso que sincroniza el surtido  de ventas  */
    public void setXX_L_ProcessExportSalesAsso_ID (int XX_L_ProcessExportSalesAsso_ID)
    {
        if (XX_L_ProcessExportSalesAsso_ID <= 0) set_Value ("XX_L_ProcessExportSalesAsso_ID", null);
        else
        set_Value ("XX_L_ProcessExportSalesAsso_ID", Integer.valueOf(XX_L_ProcessExportSalesAsso_ID));
        
    }
    
    /** Get XX_L_ProcessExportSalesAsso_ID.
    @return ID de proceso que sincroniza el surtido  de ventas  */
    public int getXX_L_ProcessExportSalesAsso_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessExportSalesAsso_ID");
        
    }
    
    /** Set Generate Comercial Budget.
    @param XX_L_ProcessGenComBudget_ID Generate Comercial Budget */
    public void setXX_L_ProcessGenComBudget_ID (int XX_L_ProcessGenComBudget_ID)
    {
        if (XX_L_ProcessGenComBudget_ID <= 0) set_Value ("XX_L_ProcessGenComBudget_ID", null);
        else
        set_Value ("XX_L_ProcessGenComBudget_ID", Integer.valueOf(XX_L_ProcessGenComBudget_ID));
        
    }
    
    /** Get Generate Comercial Budget.
    @return Generate Comercial Budget */
    public int getXX_L_ProcessGenComBudget_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessGenComBudget_ID");
        
    }
    
    /** Set Generation Pattern Of Discount.
    @param XX_L_ProcessPatternDiscount_ID Generation Pattern Of Discount */
    public void setXX_L_ProcessPatternDiscount_ID (int XX_L_ProcessPatternDiscount_ID)
    {
        if (XX_L_ProcessPatternDiscount_ID <= 0) set_Value ("XX_L_ProcessPatternDiscount_ID", null);
        else
        set_Value ("XX_L_ProcessPatternDiscount_ID", Integer.valueOf(XX_L_ProcessPatternDiscount_ID));
        
    }
    
    /** Get Generation Pattern Of Discount.
    @return Generation Pattern Of Discount */
    public int getXX_L_ProcessPatternDiscount_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessPatternDiscount_ID");
        
    }
    
    /** Set Print Labels Direct Dispatch .
    @param XX_L_ProcessPrintLabelDD_ID ID Proceso de Imprimir Etiquetas de Despacho Directo */
    public void setXX_L_ProcessPrintLabelDD_ID (int XX_L_ProcessPrintLabelDD_ID)
    {
        if (XX_L_ProcessPrintLabelDD_ID <= 0) set_Value ("XX_L_ProcessPrintLabelDD_ID", null);
        else
        set_Value ("XX_L_ProcessPrintLabelDD_ID", Integer.valueOf(XX_L_ProcessPrintLabelDD_ID));
        
    }
    
    /** Get Print Labels Direct Dispatch .
    @return ID Proceso de Imprimir Etiquetas de Despacho Directo */
    public int getXX_L_ProcessPrintLabelDD_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessPrintLabelDD_ID");
        
    }
    
    /** Set Purchase Plan.
    @param XX_L_ProcessPurchasePlan_ID Purchase Plan */
    public void setXX_L_ProcessPurchasePlan_ID (int XX_L_ProcessPurchasePlan_ID)
    {
        if (XX_L_ProcessPurchasePlan_ID <= 0) set_Value ("XX_L_ProcessPurchasePlan_ID", null);
        else
        set_Value ("XX_L_ProcessPurchasePlan_ID", Integer.valueOf(XX_L_ProcessPurchasePlan_ID));
        
    }
    
    /** Get Purchase Plan.
    @return Purchase Plan */
    public int getXX_L_ProcessPurchasePlan_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessPurchasePlan_ID");
        
    }
    
    /** Set Store Distribution.
    @param XX_L_ProcessStoreDistri_ID Store Distribution */
    public void setXX_L_ProcessStoreDistri_ID (int XX_L_ProcessStoreDistri_ID)
    {
        if (XX_L_ProcessStoreDistri_ID <= 0) set_Value ("XX_L_ProcessStoreDistri_ID", null);
        else
        set_Value ("XX_L_ProcessStoreDistri_ID", Integer.valueOf(XX_L_ProcessStoreDistri_ID));
        
    }
    
    /** Get Store Distribution.
    @return Store Distribution */
    public int getXX_L_ProcessStoreDistri_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessStoreDistri_ID");
        
    }
    
    /** Set Validate Product.
    @param XX_L_ProcessValidateProduct_ID Proceso que valida productos no solicitados cuando tienen linea y seccion genericas */
    public void setXX_L_ProcessValidateProduct_ID (int XX_L_ProcessValidateProduct_ID)
    {
        if (XX_L_ProcessValidateProduct_ID <= 0) set_Value ("XX_L_ProcessValidateProduct_ID", null);
        else
        set_Value ("XX_L_ProcessValidateProduct_ID", Integer.valueOf(XX_L_ProcessValidateProduct_ID));
        
    }
    
    /** Get Validate Product.
    @return Proceso que valida productos no solicitados cuando tienen linea y seccion genericas */
    public int getXX_L_ProcessValidateProduct_ID() 
    {
        return get_ValueAsInt("XX_L_ProcessValidateProduct_ID");
        
    }
    
    /** Set Purchase Order Window.
    @param XX_L_PurchaseOrderWindow_ID Purchase Order Window */
    public void setXX_L_PurchaseOrderWindow_ID (int XX_L_PurchaseOrderWindow_ID)
    {
        if (XX_L_PurchaseOrderWindow_ID <= 0) set_Value ("XX_L_PurchaseOrderWindow_ID", null);
        else
        set_Value ("XX_L_PurchaseOrderWindow_ID", Integer.valueOf(XX_L_PurchaseOrderWindow_ID));
        
    }
    
    /** Get Purchase Order Window.
    @return Purchase Order Window */
    public int getXX_L_PurchaseOrderWindow_ID() 
    {
        return get_ValueAsInt("XX_L_PurchaseOrderWindow_ID");
        
    }
    
    /** Set Credit Notify Gast Almc Return Report.
    @param XX_L_Report_CNGastAlmDev_ID Reporte del Aviso de Credito por Gatos de Almacenaje Devolucion */
    public void setXX_L_Report_CNGastAlmDev_ID (int XX_L_Report_CNGastAlmDev_ID)
    {
        if (XX_L_Report_CNGastAlmDev_ID <= 0) set_Value ("XX_L_Report_CNGastAlmDev_ID", null);
        else
        set_Value ("XX_L_Report_CNGastAlmDev_ID", Integer.valueOf(XX_L_Report_CNGastAlmDev_ID));
        
    }
    
    /** Get Credit Notify Gast Almc Return Report.
    @return Reporte del Aviso de Credito por Gatos de Almacenaje Devolucion */
    public int getXX_L_Report_CNGastAlmDev_ID() 
    {
        return get_ValueAsInt("XX_L_Report_CNGastAlmDev_ID");
        
    }
    
    /** Set Credit Notify Retraso O/C Report.
    @param XX_L_Report_CNRetOC_ID Reporte de aviso de Credito Retraso Entrega O/C */
    public void setXX_L_Report_CNRetOC_ID (int XX_L_Report_CNRetOC_ID)
    {
        if (XX_L_Report_CNRetOC_ID <= 0) set_Value ("XX_L_Report_CNRetOC_ID", null);
        else
        set_Value ("XX_L_Report_CNRetOC_ID", Integer.valueOf(XX_L_Report_CNRetOC_ID));
        
    }
    
    /** Get Credit Notify Retraso O/C Report.
    @return Reporte de aviso de Credito Retraso Entrega O/C */
    public int getXX_L_Report_CNRetOC_ID() 
    {
        return get_ValueAsInt("XX_L_Report_CNRetOC_ID");
        
    }
    
    /** Set Invalid Receipt Report.
    @param XX_L_Report_InvReceipt_ID Invalid Receipt Report */
    public void setXX_L_Report_InvReceipt_ID (int XX_L_Report_InvReceipt_ID)
    {
        if (XX_L_Report_InvReceipt_ID <= 0) set_Value ("XX_L_Report_InvReceipt_ID", null);
        else
        set_Value ("XX_L_Report_InvReceipt_ID", Integer.valueOf(XX_L_Report_InvReceipt_ID));
        
    }
    
    /** Get Invalid Receipt Report.
    @return Invalid Receipt Report */
    public int getXX_L_Report_InvReceipt_ID() 
    {
        return get_ValueAsInt("XX_L_Report_InvReceipt_ID");
        
    }
    
    /** Set Accounts to Pay Coordinator.
    @param XX_L_Role_AccountToPayCoord_ID Accounts to Pay Coordinator */
    public void setXX_L_Role_AccountToPayCoord_ID (int XX_L_Role_AccountToPayCoord_ID)
    {
        if (XX_L_Role_AccountToPayCoord_ID <= 0) set_Value ("XX_L_Role_AccountToPayCoord_ID", null);
        else
        set_Value ("XX_L_Role_AccountToPayCoord_ID", Integer.valueOf(XX_L_Role_AccountToPayCoord_ID));
        
    }
    
    /** Get Accounts to Pay Coordinator.
    @return Accounts to Pay Coordinator */
    public int getXX_L_Role_AccountToPayCoord_ID() 
    {
        return get_ValueAsInt("XX_L_Role_AccountToPayCoord_ID");
        
    }
    
    /** Set Buyer Role.
    @param XX_L_RoleBuyer_ID Buyer Role */
    public void setXX_L_RoleBuyer_ID (int XX_L_RoleBuyer_ID)
    {
        if (XX_L_RoleBuyer_ID <= 0) set_Value ("XX_L_RoleBuyer_ID", null);
        else
        set_Value ("XX_L_RoleBuyer_ID", Integer.valueOf(XX_L_RoleBuyer_ID));
        
    }
    
    /** Get Buyer Role.
    @return Buyer Role */
    public int getXX_L_RoleBuyer_ID() 
    {
        return get_ValueAsInt("XX_L_RoleBuyer_ID");
        
    }
    
    /** Set Category Manager Role.
    @param XX_L_RoleCategoryManager_ID Category Manager Role */
    public void setXX_L_RoleCategoryManager_ID (int XX_L_RoleCategoryManager_ID)
    {
        if (XX_L_RoleCategoryManager_ID <= 0) set_Value ("XX_L_RoleCategoryManager_ID", null);
        else
        set_Value ("XX_L_RoleCategoryManager_ID", Integer.valueOf(XX_L_RoleCategoryManager_ID));
        
    }
    
    /** Get Category Manager Role.
    @return Category Manager Role */
    public int getXX_L_RoleCategoryManager_ID() 
    {
        return get_ValueAsInt("XX_L_RoleCategoryManager_ID");
        
    }
    
    /** Set Checkup Assistant Role.
    @param XX_L_RoleCheckupAssistant_ID Checkup Assistant Role */
    public void setXX_L_RoleCheckupAssistant_ID (int XX_L_RoleCheckupAssistant_ID)
    {
        if (XX_L_RoleCheckupAssistant_ID <= 0) set_Value ("XX_L_RoleCheckupAssistant_ID", null);
        else
        set_Value ("XX_L_RoleCheckupAssistant_ID", Integer.valueOf(XX_L_RoleCheckupAssistant_ID));
        
    }
    
    /** Get Checkup Assistant Role.
    @return Checkup Assistant Role */
    public int getXX_L_RoleCheckupAssistant_ID() 
    {
        return get_ValueAsInt("XX_L_RoleCheckupAssistant_ID");
        
    }
    
    /** Set Checkup Auxiliary Role.
    @param XX_L_RoleCheckupAuxiliary_ID Checkup Auxiliary Role */
    public void setXX_L_RoleCheckupAuxiliary_ID (int XX_L_RoleCheckupAuxiliary_ID)
    {
        if (XX_L_RoleCheckupAuxiliary_ID <= 0) set_Value ("XX_L_RoleCheckupAuxiliary_ID", null);
        else
        set_Value ("XX_L_RoleCheckupAuxiliary_ID", Integer.valueOf(XX_L_RoleCheckupAuxiliary_ID));
        
    }
    
    /** Get Checkup Auxiliary Role.
    @return Checkup Auxiliary Role */
    public int getXX_L_RoleCheckupAuxiliary_ID() 
    {
        return get_ValueAsInt("XX_L_RoleCheckupAuxiliary_ID");
        
    }
    
    /** Set Checkup Coordinator Role.
    @param XX_L_RoleCheckupCoord_ID Checkup Coordinator Role */
    public void setXX_L_RoleCheckupCoord_ID (int XX_L_RoleCheckupCoord_ID)
    {
        if (XX_L_RoleCheckupCoord_ID <= 0) set_Value ("XX_L_RoleCheckupCoord_ID", null);
        else
        set_Value ("XX_L_RoleCheckupCoord_ID", Integer.valueOf(XX_L_RoleCheckupCoord_ID));
        
    }
    
    /** Get Checkup Coordinator Role.
    @return Checkup Coordinator Role */
    public int getXX_L_RoleCheckupCoord_ID() 
    {
        return get_ValueAsInt("XX_L_RoleCheckupCoord_ID");
        
    }
    
    /** Set Checkup Coordinator Role.
    @param XX_L_RoleCheckupCoordinator_ID Checkup Coordinator Role */
    public void setXX_L_RoleCheckupCoordinator_ID (int XX_L_RoleCheckupCoordinator_ID)
    {
        if (XX_L_RoleCheckupCoordinator_ID <= 0) set_Value ("XX_L_RoleCheckupCoordinator_ID", null);
        else
        set_Value ("XX_L_RoleCheckupCoordinator_ID", Integer.valueOf(XX_L_RoleCheckupCoordinator_ID));
        
    }
    
    /** Get Checkup Coordinator Role.
    @return Checkup Coordinator Role */
    public int getXX_L_RoleCheckupCoordinator_ID() 
    {
        return get_ValueAsInt("XX_L_RoleCheckupCoordinator_ID");
        
    }
    
    /** Set Distribution Center Manager Role.
    @param XX_L_RoleDistCenterManager_ID Distribution Center Manager Role */
    public void setXX_L_RoleDistCenterManager_ID (int XX_L_RoleDistCenterManager_ID)
    {
        if (XX_L_RoleDistCenterManager_ID <= 0) set_Value ("XX_L_RoleDistCenterManager_ID", null);
        else
        set_Value ("XX_L_RoleDistCenterManager_ID", Integer.valueOf(XX_L_RoleDistCenterManager_ID));
        
    }
    
    /** Get Distribution Center Manager Role.
    @return Distribution Center Manager Role */
    public int getXX_L_RoleDistCenterManager_ID() 
    {
        return get_ValueAsInt("XX_L_RoleDistCenterManager_ID");
        
    }
    
    /** Set Schedule Manager Role.
    @param XX_L_RoleScheduleManager_ID Schedule Manager Role */
    public void setXX_L_RoleScheduleManager_ID (int XX_L_RoleScheduleManager_ID)
    {
        if (XX_L_RoleScheduleManager_ID <= 0) set_Value ("XX_L_RoleScheduleManager_ID", null);
        else
        set_Value ("XX_L_RoleScheduleManager_ID", Integer.valueOf(XX_L_RoleScheduleManager_ID));
        
    }
    
    /** Get Schedule Manager Role.
    @return Schedule Manager Role */
    public int getXX_L_RoleScheduleManager_ID() 
    {
        return get_ValueAsInt("XX_L_RoleScheduleManager_ID");
        
    }
    
    /** Set Inventory Scheduler Role.
    @param XX_L_RoleScheduler_ID Inventory Scheduler Role */
    public void setXX_L_RoleScheduler_ID (int XX_L_RoleScheduler_ID)
    {
        if (XX_L_RoleScheduler_ID <= 0) set_Value ("XX_L_RoleScheduler_ID", null);
        else
        set_Value ("XX_L_RoleScheduler_ID", Integer.valueOf(XX_L_RoleScheduler_ID));
        
    }
    
    /** Get Inventory Scheduler Role.
    @return Inventory Scheduler Role */
    public int getXX_L_RoleScheduler_ID() 
    {
        return get_ValueAsInt("XX_L_RoleScheduler_ID");
        
    }
    
    /** Set Warehouse Assistant Role.
    @param XX_L_RoleWarehouseAssistant_ID Warehouse Assistant Role */
    public void setXX_L_RoleWarehouseAssistant_ID (int XX_L_RoleWarehouseAssistant_ID)
    {
        if (XX_L_RoleWarehouseAssistant_ID <= 0) set_Value ("XX_L_RoleWarehouseAssistant_ID", null);
        else
        set_Value ("XX_L_RoleWarehouseAssistant_ID", Integer.valueOf(XX_L_RoleWarehouseAssistant_ID));
        
    }
    
    /** Get Warehouse Assistant Role.
    @return Warehouse Assistant Role */
    public int getXX_L_RoleWarehouseAssistant_ID() 
    {
        return get_ValueAsInt("XX_L_RoleWarehouseAssistant_ID");
        
    }
    
    /** Set Warehouse Coordinator Role.
    @param XX_L_RoleWarehouseCoord_ID Warehouse Coordinator Role */
    public void setXX_L_RoleWarehouseCoord_ID (int XX_L_RoleWarehouseCoord_ID)
    {
        if (XX_L_RoleWarehouseCoord_ID <= 0) set_Value ("XX_L_RoleWarehouseCoord_ID", null);
        else
        set_Value ("XX_L_RoleWarehouseCoord_ID", Integer.valueOf(XX_L_RoleWarehouseCoord_ID));
        
    }
    
    /** Get Warehouse Coordinator Role.
    @return Warehouse Coordinator Role */
    public int getXX_L_RoleWarehouseCoord_ID() 
    {
        return get_ValueAsInt("XX_L_RoleWarehouseCoord_ID");
        
    }
    
    /** Set Standard Product.
    @param XX_L_StandardProduct_ID Standard Product */
    public void setXX_L_StandardProduct_ID (int XX_L_StandardProduct_ID)
    {
        if (XX_L_StandardProduct_ID <= 0) set_Value ("XX_L_StandardProduct_ID", null);
        else
        set_Value ("XX_L_StandardProduct_ID", Integer.valueOf(XX_L_StandardProduct_ID));
        
    }
    
    /** Get Standard Product.
    @return Standard Product */
    public int getXX_L_StandardProduct_ID() 
    {
        return get_ValueAsInt("XX_L_StandardProduct_ID");
        
    }
    
    /** Set Role Storage Especialist.
    @param XX_L_StorageEspecialist_ID Role Storage Especialist */
    public void setXX_L_StorageEspecialist_ID (int XX_L_StorageEspecialist_ID)
    {
        if (XX_L_StorageEspecialist_ID <= 0) set_Value ("XX_L_StorageEspecialist_ID", null);
        else
        set_Value ("XX_L_StorageEspecialist_ID", Integer.valueOf(XX_L_StorageEspecialist_ID));
        
    }
    
    /** Get Role Storage Especialist.
    @return Role Storage Especialist */
    public int getXX_L_StorageEspecialist_ID() 
    {
        return get_ValueAsInt("XX_L_StorageEspecialist_ID");
        
    }
    
    /** Set Store Manager.
    @param XX_L_StoreManager_ID Store Manager */
    public void setXX_L_StoreManager_ID (int XX_L_StoreManager_ID)
    {
        if (XX_L_StoreManager_ID <= 0) set_Value ("XX_L_StoreManager_ID", null);
        else
        set_Value ("XX_L_StoreManager_ID", Integer.valueOf(XX_L_StoreManager_ID));
        
    }
    
    /** Get Store Manager.
    @return Store Manager */
    public int getXX_L_StoreManager_ID() 
    {
        return get_ValueAsInt("XX_L_StoreManager_ID");
        
    }
    
    /** Set Surplus Cancellation Motive.
    @param XX_L_SurplusCancelMotive_ID Surplus Cancellation Motive */
    public void setXX_L_SurplusCancelMotive_ID (int XX_L_SurplusCancelMotive_ID)
    {
        if (XX_L_SurplusCancelMotive_ID <= 0) set_Value ("XX_L_SurplusCancelMotive_ID", null);
        else
        set_Value ("XX_L_SurplusCancelMotive_ID", Integer.valueOf(XX_L_SurplusCancelMotive_ID));
        
    }
    
    /** Get Surplus Cancellation Motive.
    @return Surplus Cancellation Motive */
    public int getXX_L_SurplusCancelMotive_ID() 
    {
        return get_ValueAsInt("XX_L_SurplusCancelMotive_ID");
        
    }
    
    /** Set Tax Category Exento.
    @param XX_L_TaxCategory_Exen_ID Tax Category Exento */
    public void setXX_L_TaxCategory_Exen_ID (int XX_L_TaxCategory_Exen_ID)
    {
        if (XX_L_TaxCategory_Exen_ID <= 0) set_Value ("XX_L_TaxCategory_Exen_ID", null);
        else
        set_Value ("XX_L_TaxCategory_Exen_ID", Integer.valueOf(XX_L_TaxCategory_Exen_ID));
        
    }
    
    /** Get Tax Category Exento.
    @return Tax Category Exento */
    public int getXX_L_TaxCategory_Exen_ID() 
    {
        return get_ValueAsInt("XX_L_TaxCategory_Exen_ID");
        
    }
    
    /** Set Tax Category IVA Adicional.
    @param XX_L_TaxCategory_IVAAdic_ID Tax Category IVA Adicional */
    public void setXX_L_TaxCategory_IVAAdic_ID (int XX_L_TaxCategory_IVAAdic_ID)
    {
        if (XX_L_TaxCategory_IVAAdic_ID <= 0) set_Value ("XX_L_TaxCategory_IVAAdic_ID", null);
        else
        set_Value ("XX_L_TaxCategory_IVAAdic_ID", Integer.valueOf(XX_L_TaxCategory_IVAAdic_ID));
        
    }
    
    /** Get Tax Category IVA Adicional.
    @return Tax Category IVA Adicional */
    public int getXX_L_TaxCategory_IVAAdic_ID() 
    {
        return get_ValueAsInt("XX_L_TaxCategory_IVAAdic_ID");
        
    }
    
    /** Set Tax Category IVA.
    @param XX_L_TaxCategory_IVA_ID Tax Category IVA */
    public void setXX_L_TaxCategory_IVA_ID (int XX_L_TaxCategory_IVA_ID)
    {
        if (XX_L_TaxCategory_IVA_ID <= 0) set_Value ("XX_L_TaxCategory_IVA_ID", null);
        else
        set_Value ("XX_L_TaxCategory_IVA_ID", Integer.valueOf(XX_L_TaxCategory_IVA_ID));
        
    }
    
    /** Get Tax Category IVA.
    @return Tax Category IVA */
    public int getXX_L_TaxCategory_IVA_ID() 
    {
        return get_ValueAsInt("XX_L_TaxCategory_IVA_ID");
        
    }
    
    /** Set Tax Category IVA Reducido.
    @param XX_L_TaxCategory_IVARedu_ID Tax Category IVA Reducido */
    public void setXX_L_TaxCategory_IVARedu_ID (int XX_L_TaxCategory_IVARedu_ID)
    {
        if (XX_L_TaxCategory_IVARedu_ID <= 0) set_Value ("XX_L_TaxCategory_IVARedu_ID", null);
        else
        set_Value ("XX_L_TaxCategory_IVARedu_ID", Integer.valueOf(XX_L_TaxCategory_IVARedu_ID));
        
    }
    
    /** Get Tax Category IVA Reducido.
    @return Tax Category IVA Reducido */
    public int getXX_L_TaxCategory_IVARedu_ID() 
    {
        return get_ValueAsInt("XX_L_TaxCategory_IVARedu_ID");
        
    }
    
    /** Set Tax Exento.
    @param XX_L_Tax_Exento_ID Tax Exento */
    public void setXX_L_Tax_Exento_ID (int XX_L_Tax_Exento_ID)
    {
        if (XX_L_Tax_Exento_ID <= 0) set_Value ("XX_L_Tax_Exento_ID", null);
        else
        set_Value ("XX_L_Tax_Exento_ID", Integer.valueOf(XX_L_Tax_Exento_ID));
        
    }
    
    /** Get Tax Exento.
    @return Tax Exento */
    public int getXX_L_Tax_Exento_ID() 
    {
        return get_ValueAsInt("XX_L_Tax_Exento_ID");
        
    }
    
    /** Set Locator For Products To Validate.
    @param XX_L_TOVALIDATELOCATOR_ID Locator For Products To Validate */
    public void setXX_L_TOVALIDATELOCATOR_ID (int XX_L_TOVALIDATELOCATOR_ID)
    {
        if (XX_L_TOVALIDATELOCATOR_ID <= 0) set_Value ("XX_L_TOVALIDATELOCATOR_ID", null);
        else
        set_Value ("XX_L_TOVALIDATELOCATOR_ID", Integer.valueOf(XX_L_TOVALIDATELOCATOR_ID));
        
    }
    
    /** Get Locator For Products To Validate.
    @return Locator For Products To Validate */
    public int getXX_L_TOVALIDATELOCATOR_ID() 
    {
        return get_ValueAsInt("XX_L_TOVALIDATELOCATOR_ID");
        
    }
    
    /** Set Table Unsolicited Product.
    @param XX_L_T_UnsolicitedProduct Table Unsolicited Product */
    public void setXX_L_T_UnsolicitedProduct (int XX_L_T_UnsolicitedProduct)
    {
        set_Value ("XX_L_T_UnsolicitedProduct", Integer.valueOf(XX_L_T_UnsolicitedProduct));
        
    }
    
    /** Get Table Unsolicited Product.
    @return Table Unsolicited Product */
    public int getXX_L_T_UnsolicitedProduct() 
    {
        return get_ValueAsInt("XX_L_T_UnsolicitedProduct");
        
    }
    
    /** Set Type Inventory Basico.
    @param XX_L_TypeInventoryBasico_ID Type Inventory Basico */
    public void setXX_L_TypeInventoryBasico_ID (int XX_L_TypeInventoryBasico_ID)
    {
        if (XX_L_TypeInventoryBasico_ID <= 0) set_Value ("XX_L_TypeInventoryBasico_ID", null);
        else
        set_Value ("XX_L_TypeInventoryBasico_ID", Integer.valueOf(XX_L_TypeInventoryBasico_ID));
        
    }
    
    /** Get Type Inventory Basico.
    @return Type Inventory Basico */
    public int getXX_L_TypeInventoryBasico_ID() 
    {
        return get_ValueAsInt("XX_L_TypeInventoryBasico_ID");
        
    }
    
    /** Set Type Inventory Tendencia.
    @param XX_L_TypeInventoryTendencia_ID Type Inventory Tendencia */
    public void setXX_L_TypeInventoryTendencia_ID (int XX_L_TypeInventoryTendencia_ID)
    {
        if (XX_L_TypeInventoryTendencia_ID <= 0) set_Value ("XX_L_TypeInventoryTendencia_ID", null);
        else
        set_Value ("XX_L_TypeInventoryTendencia_ID", Integer.valueOf(XX_L_TypeInventoryTendencia_ID));
        
    }
    
    /** Get Type Inventory Tendencia.
    @return Type Inventory Tendencia */
    public int getXX_L_TypeInventoryTendencia_ID() 
    {
        return get_ValueAsInt("XX_L_TypeInventoryTendencia_ID");
        
    }
    
    /** Set Label Type.
    @param XX_L_TypeLabelColgante_ID Label Type */
    public void setXX_L_TypeLabelColgante_ID (int XX_L_TypeLabelColgante_ID)
    {
        if (XX_L_TypeLabelColgante_ID <= 0) set_Value ("XX_L_TypeLabelColgante_ID", null);
        else
        set_Value ("XX_L_TypeLabelColgante_ID", Integer.valueOf(XX_L_TypeLabelColgante_ID));
        
    }
    
    /** Get Label Type.
    @return Label Type */
    public int getXX_L_TypeLabelColgante_ID() 
    {
        return get_ValueAsInt("XX_L_TypeLabelColgante_ID");
        
    }
    
    /** Set Label Type.
    @param XX_L_TypeLabelEngomada_ID Label Type */
    public void setXX_L_TypeLabelEngomada_ID (int XX_L_TypeLabelEngomada_ID)
    {
        if (XX_L_TypeLabelEngomada_ID <= 0) set_Value ("XX_L_TypeLabelEngomada_ID", null);
        else
        set_Value ("XX_L_TypeLabelEngomada_ID", Integer.valueOf(XX_L_TypeLabelEngomada_ID));
        
    }
    
    /** Get Label Type.
    @return Label Type */
    public int getXX_L_TypeLabelEngomada_ID() 
    {
        return get_ValueAsInt("XX_L_TypeLabelEngomada_ID");
        
    }
    
    /** Set Type Taxpayer Formal.
    @param XX_L_TypeTaxpayerFor_ID Type Taxpayer Formal */
    public void setXX_L_TypeTaxpayerFor_ID (int XX_L_TypeTaxpayerFor_ID)
    {
        if (XX_L_TypeTaxpayerFor_ID <= 0) set_Value ("XX_L_TypeTaxpayerFor_ID", null);
        else
        set_Value ("XX_L_TypeTaxpayerFor_ID", Integer.valueOf(XX_L_TypeTaxpayerFor_ID));
        
    }
    
    /** Get Type Taxpayer Formal.
    @return Type Taxpayer Formal */
    public int getXX_L_TypeTaxpayerFor_ID() 
    {
        return get_ValueAsInt("XX_L_TypeTaxpayerFor_ID");
        
    }
    
    /** Set User Mail From Mail Factor Definitive.
    @param XX_L_UserFromMailFac_ID User Mail From Mail Factor Definitive */
    public void setXX_L_UserFromMailFac_ID (int XX_L_UserFromMailFac_ID)
    {
        if (XX_L_UserFromMailFac_ID <= 0) set_Value ("XX_L_UserFromMailFac_ID", null);
        else
        set_Value ("XX_L_UserFromMailFac_ID", Integer.valueOf(XX_L_UserFromMailFac_ID));
        
    }
    
    /** Get User Mail From Mail Factor Definitive.
    @return User Mail From Mail Factor Definitive */
    public int getXX_L_UserFromMailFac_ID() 
    {
        return get_ValueAsInt("XX_L_UserFromMailFac_ID");
        
    }
    
    /** Set User From Mail.
    @param XX_L_UserFromMail_ID User From Mail */
    public void setXX_L_UserFromMail_ID (int XX_L_UserFromMail_ID)
    {
        if (XX_L_UserFromMail_ID <= 0) set_Value ("XX_L_UserFromMail_ID", null);
        else
        set_Value ("XX_L_UserFromMail_ID", Integer.valueOf(XX_L_UserFromMail_ID));
        
    }
    
    /** Get User From Mail.
    @return User From Mail */
    public int getXX_L_UserFromMail_ID() 
    {
        return get_ValueAsInt("XX_L_UserFromMail_ID");
        
    }
    
    /** Set Storage Charge.
    @param XX_L_VCN_StorageCharge Storage Charge */
    public void setXX_L_VCN_StorageCharge (java.math.BigDecimal XX_L_VCN_StorageCharge)
    {
        set_Value ("XX_L_VCN_StorageCharge", XX_L_VCN_StorageCharge);
        
    }
    
    /** Get Storage Charge.
    @return Storage Charge */
    public java.math.BigDecimal getXX_L_VCN_StorageCharge() 
    {
        return get_ValueAsBigDecimal("XX_L_VCN_StorageCharge");
        
    }
    
    /** Nacional = 10000005 */
    public static final String XX_L_VENDCLASSIMP_Nacional = X_Ref_XX_Ref_VendorClass.NACIONAL.getValue();
    /** Importado = 10000006 */
    public static final String XX_L_VENDCLASSIMP_Importado = X_Ref_XX_Ref_VendorClass.IMPORTADO.getValue();
    /** Set Vendor Class Importado.
    @param XX_L_VendClassImp Vendor Class Importado */
    public void setXX_L_VendClassImp (String XX_L_VendClassImp)
    {
        if (!X_Ref_XX_Ref_VendorClass.isValid(XX_L_VendClassImp))
        throw new IllegalArgumentException ("XX_L_VendClassImp Invalid value - " + XX_L_VendClassImp + " - Reference_ID=1000050 - 10000005 - 10000006");
        set_Value ("XX_L_VendClassImp", XX_L_VendClassImp);
        
    }
    
    /** Get Vendor Class Importado.
    @return Vendor Class Importado */
    public String getXX_L_VendClassImp() 
    {
        return (String)get_Value("XX_L_VendClassImp");
        
    }
    
    /** Set Vigilant Role.
    @param XX_L_VigilantRole_ID Vigilant Role */
    public void setXX_L_VigilantRole_ID (int XX_L_VigilantRole_ID)
    {
        if (XX_L_VigilantRole_ID <= 0) set_Value ("XX_L_VigilantRole_ID", null);
        else
        set_Value ("XX_L_VigilantRole_ID", Integer.valueOf(XX_L_VigilantRole_ID));
        
    }
    
    /** Get Vigilant Role.
    @return Vigilant Role */
    public int getXX_L_VigilantRole_ID() 
    {
        return get_ValueAsInt("XX_L_VigilantRole_ID");
        
    }
    
    /** Set Company CENTROBECO.
    @param XX_L_VSI_ClientCentroBeco_ID Company CENTROBECO */
    public void setXX_L_VSI_ClientCentroBeco_ID (int XX_L_VSI_ClientCentroBeco_ID)
    {
        if (XX_L_VSI_ClientCentroBeco_ID <= 0) set_Value ("XX_L_VSI_ClientCentroBeco_ID", null);
        else
        set_Value ("XX_L_VSI_ClientCentroBeco_ID", Integer.valueOf(XX_L_VSI_ClientCentroBeco_ID));
        
    }
    
    /** Get Company CENTROBECO.
    @return Company CENTROBECO */
    public int getXX_L_VSI_ClientCentroBeco_ID() 
    {
        return get_ValueAsInt("XX_L_VSI_ClientCentroBeco_ID");
        
    }
    
    /** Set Warehouse Distribution Center.
    @param XX_L_WarehouseCentroDist_ID Warehouse Distribution Center */
    public void setXX_L_WarehouseCentroDist_ID (int XX_L_WarehouseCentroDist_ID)
    {
        if (XX_L_WarehouseCentroDist_ID <= 0) set_Value ("XX_L_WarehouseCentroDist_ID", null);
        else
        set_Value ("XX_L_WarehouseCentroDist_ID", Integer.valueOf(XX_L_WarehouseCentroDist_ID));
        
    }
    
    /** Get Warehouse Distribution Center.
    @return Warehouse Distribution Center */
    public int getXX_L_WarehouseCentroDist_ID() 
    {
        return get_ValueAsInt("XX_L_WarehouseCentroDist_ID");
        
    }
    
    /** Set Window Boarding Guide.
    @param XX_L_W_BoardingGuide_ID Window Boarding Guide */
    public void setXX_L_W_BoardingGuide_ID (int XX_L_W_BoardingGuide_ID)
    {
        if (XX_L_W_BoardingGuide_ID <= 0) set_Value ("XX_L_W_BoardingGuide_ID", null);
        else
        set_Value ("XX_L_W_BoardingGuide_ID", Integer.valueOf(XX_L_W_BoardingGuide_ID));
        
    }
    
    /** Get Window Boarding Guide.
    @return Window Boarding Guide */
    public int getXX_L_W_BoardingGuide_ID() 
    {
        return get_ValueAsInt("XX_L_W_BoardingGuide_ID");
        
    }
    
    /** Set Window Direct Dispatch & Pending Labels.
    @param XX_L_W_DDPendingLabels_ID Window Direct Dispatch & Pending Labels */
    public void setXX_L_W_DDPendingLabels_ID (int XX_L_W_DDPendingLabels_ID)
    {
        if (XX_L_W_DDPendingLabels_ID <= 0) set_Value ("XX_L_W_DDPendingLabels_ID", null);
        else
        set_Value ("XX_L_W_DDPendingLabels_ID", Integer.valueOf(XX_L_W_DDPendingLabels_ID));
        
    }
    
    /** Get Window Direct Dispatch & Pending Labels.
    @return Window Direct Dispatch & Pending Labels */
    public int getXX_L_W_DDPendingLabels_ID() 
    {
        return get_ValueAsInt("XX_L_W_DDPendingLabels_ID");
        
    }
    
    /** Set Discount Request.
    @param XX_L_W_DiscountRequest_ID Discount Request */
    public void setXX_L_W_DiscountRequest_ID (int XX_L_W_DiscountRequest_ID)
    {
        if (XX_L_W_DiscountRequest_ID <= 0) set_Value ("XX_L_W_DiscountRequest_ID", null);
        else
        set_Value ("XX_L_W_DiscountRequest_ID", Integer.valueOf(XX_L_W_DiscountRequest_ID));
        
    }
    
    /** Get Discount Request.
    @return Discount Request */
    public int getXX_L_W_DiscountRequest_ID() 
    {
        return get_ValueAsInt("XX_L_W_DiscountRequest_ID");
        
    }
    
    /** Set Window Dispatch Guide.
    @param XX_L_W_DispatchGuide_ID Window Dispatch Guide */
    public void setXX_L_W_DispatchGuide_ID (int XX_L_W_DispatchGuide_ID)
    {
        if (XX_L_W_DispatchGuide_ID <= 0) set_Value ("XX_L_W_DispatchGuide_ID", null);
        else
        set_Value ("XX_L_W_DispatchGuide_ID", Integer.valueOf(XX_L_W_DispatchGuide_ID));
        
    }
    
    /** Get Window Dispatch Guide.
    @return Window Dispatch Guide */
    public int getXX_L_W_DispatchGuide_ID() 
    {
        return get_ValueAsInt("XX_L_W_DispatchGuide_ID");
        
    }
    
    /** Set Window Distribution.
    @param XX_L_W_DistributionHeader_ID Window Distribution */
    public void setXX_L_W_DistributionHeader_ID (int XX_L_W_DistributionHeader_ID)
    {
        if (XX_L_W_DistributionHeader_ID <= 0) set_Value ("XX_L_W_DistributionHeader_ID", null);
        else
        set_Value ("XX_L_W_DistributionHeader_ID", Integer.valueOf(XX_L_W_DistributionHeader_ID));
        
    }
    
    /** Get Window Distribution.
    @return Window Distribution */
    public int getXX_L_W_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_L_W_DistributionHeader_ID");
        
    }
    
    /** Set Assign Price Sales.
    @param XX_L_WF_AssignPriceSales_ID ID Proceso de generacion del workflow para Asignacion de precios de Ventas  */
    public void setXX_L_WF_AssignPriceSales_ID (int XX_L_WF_AssignPriceSales_ID)
    {
        if (XX_L_WF_AssignPriceSales_ID <= 0) set_Value ("XX_L_WF_AssignPriceSales_ID", null);
        else
        set_Value ("XX_L_WF_AssignPriceSales_ID", Integer.valueOf(XX_L_WF_AssignPriceSales_ID));
        
    }
    
    /** Get Assign Price Sales.
    @return ID Proceso de generacion del workflow para Asignacion de precios de Ventas  */
    public int getXX_L_WF_AssignPriceSales_ID() 
    {
        return get_ValueAsInt("XX_L_WF_AssignPriceSales_ID");
        
    }
    
    /** Set Assign Tariff Code.
    @param XX_L_WF_AssignTariffCode_ID Assign Tariff Code */
    public void setXX_L_WF_AssignTariffCode_ID (int XX_L_WF_AssignTariffCode_ID)
    {
        if (XX_L_WF_AssignTariffCode_ID <= 0) set_Value ("XX_L_WF_AssignTariffCode_ID", null);
        else
        set_Value ("XX_L_WF_AssignTariffCode_ID", Integer.valueOf(XX_L_WF_AssignTariffCode_ID));
        
    }
    
    /** Get Assign Tariff Code.
    @return Assign Tariff Code */
    public int getXX_L_WF_AssignTariffCode_ID() 
    {
        return get_ValueAsInt("XX_L_WF_AssignTariffCode_ID");
        
    }
    
    /** Set XX_L_WindowStoredProduct_ID.
    @param XX_L_WindowStoredProduct_ID XX_L_WindowStoredProduct_ID */
    public void setXX_L_WindowStoredProduct_ID (int XX_L_WindowStoredProduct_ID)
    {
        if (XX_L_WindowStoredProduct_ID <= 0) set_Value ("XX_L_WindowStoredProduct_ID", null);
        else
        set_Value ("XX_L_WindowStoredProduct_ID", Integer.valueOf(XX_L_WindowStoredProduct_ID));
        
    }
    
    /** Get XX_L_WindowStoredProduct_ID.
    @return XX_L_WindowStoredProduct_ID */
    public int getXX_L_WindowStoredProduct_ID() 
    {
        return get_ValueAsInt("XX_L_WindowStoredProduct_ID");
        
    }
    
    /** Set O/C Manage Priority.
    @param XX_L_W_OCPriority_ID O/C Manage Priority */
    public void setXX_L_W_OCPriority_ID (int XX_L_W_OCPriority_ID)
    {
        if (XX_L_W_OCPriority_ID <= 0) set_Value ("XX_L_W_OCPriority_ID", null);
        else
        set_Value ("XX_L_W_OCPriority_ID", Integer.valueOf(XX_L_W_OCPriority_ID));
        
    }
    
    /** Get O/C Manage Priority.
    @return O/C Manage Priority */
    public int getXX_L_W_OCPriority_ID() 
    {
        return get_ValueAsInt("XX_L_W_OCPriority_ID");
        
    }
    
    /** Set Working Day.
    @param XX_L_WorkingDay Working Day */
    public void setXX_L_WorkingDay (java.math.BigDecimal XX_L_WorkingDay)
    {
        set_Value ("XX_L_WorkingDay", XX_L_WorkingDay);
        
    }
    
    /** Get Working Day.
    @return Working Day */
    public java.math.BigDecimal getXX_L_WorkingDay() 
    {
        return get_ValueAsBigDecimal("XX_L_WorkingDay");
        
    }
    
    /** Set Pattern Of Discount.
    @param XX_L_W_PatternOfDiscount_ID Pattern Of Discount */
    public void setXX_L_W_PatternOfDiscount_ID (int XX_L_W_PatternOfDiscount_ID)
    {
        if (XX_L_W_PatternOfDiscount_ID <= 0) set_Value ("XX_L_W_PatternOfDiscount_ID", null);
        else
        set_Value ("XX_L_W_PatternOfDiscount_ID", Integer.valueOf(XX_L_W_PatternOfDiscount_ID));
        
    }
    
    /** Get Pattern Of Discount.
    @return Pattern Of Discount */
    public int getXX_L_W_PatternOfDiscount_ID() 
    {
        return get_ValueAsInt("XX_L_W_PatternOfDiscount_ID");
        
    }
    
    /** Set Window Pending Labels.
    @param XX_L_W_PendingLabels_ID Window Pending Labels */
    public void setXX_L_W_PendingLabels_ID (int XX_L_W_PendingLabels_ID)
    {
        if (XX_L_W_PendingLabels_ID <= 0) set_Value ("XX_L_W_PendingLabels_ID", null);
        else
        set_Value ("XX_L_W_PendingLabels_ID", Integer.valueOf(XX_L_W_PendingLabels_ID));
        
    }
    
    /** Get Window Pending Labels.
    @return Window Pending Labels */
    public int getXX_L_W_PendingLabels_ID() 
    {
        return get_ValueAsInt("XX_L_W_PendingLabels_ID");
        
    }
    
    /** Set Placed Order Priority.
    @param XX_L_W_PlacedOPriority_ID Placed Order Priority */
    public void setXX_L_W_PlacedOPriority_ID (int XX_L_W_PlacedOPriority_ID)
    {
        if (XX_L_W_PlacedOPriority_ID <= 0) set_Value ("XX_L_W_PlacedOPriority_ID", null);
        else
        set_Value ("XX_L_W_PlacedOPriority_ID", Integer.valueOf(XX_L_W_PlacedOPriority_ID));
        
    }
    
    /** Get Placed Order Priority.
    @return Placed Order Priority */
    public int getXX_L_W_PlacedOPriority_ID() 
    {
        return get_ValueAsInt("XX_L_W_PlacedOPriority_ID");
        
    }
    
    /** Set Window Unsolicited Product.
    @param XX_L_W_UnsolicitedProduct_ID Window Unsolicited Product */
    public void setXX_L_W_UnsolicitedProduct_ID (int XX_L_W_UnsolicitedProduct_ID)
    {
        if (XX_L_W_UnsolicitedProduct_ID <= 0) set_Value ("XX_L_W_UnsolicitedProduct_ID", null);
        else
        set_Value ("XX_L_W_UnsolicitedProduct_ID", Integer.valueOf(XX_L_W_UnsolicitedProduct_ID));
        
    }
    
    /** Get Window Unsolicited Product.
    @return Window Unsolicited Product */
    public int getXX_L_W_UnsolicitedProduct_ID() 
    {
        return get_ValueAsInt("XX_L_W_UnsolicitedProduct_ID");
        
    }
    
    /** Set Window Vendor Product Reference.
    @param XX_L_W_VendorProduct_ID Window Vendor Product Reference */
    public void setXX_L_W_VendorProduct_ID (int XX_L_W_VendorProduct_ID)
    {
        if (XX_L_W_VendorProduct_ID <= 0) set_Value ("XX_L_W_VendorProduct_ID", null);
        else
        set_Value ("XX_L_W_VendorProduct_ID", Integer.valueOf(XX_L_W_VendorProduct_ID));
        
    }
    
    /** Get Window Vendor Product Reference.
    @return Window Vendor Product Reference */
    public int getXX_L_W_VendorProduct_ID() 
    {
        return get_ValueAsInt("XX_L_W_VendorProduct_ID");
        
    }
    
    /** Set InvoiceDocument.
    @param XX_P_InvoiceDocument InvoiceDocument */
    public void setXX_P_InvoiceDocument (int XX_P_InvoiceDocument)
    {
        set_Value ("XX_P_InvoiceDocument", Integer.valueOf(XX_P_InvoiceDocument));
        
    }
    
    /** Get InvoiceDocument.
    @return InvoiceDocument */
    public int getXX_P_InvoiceDocument() 
    {
        return get_ValueAsInt("XX_P_InvoiceDocument");
        
    }
    
    /** Set DebtCreditPieces.
    @param XX_R_DebtCreditPieces_ID DebtCreditPieces */
    public void setXX_R_DebtCreditPieces_ID (int XX_R_DebtCreditPieces_ID)
    {
        if (XX_R_DebtCreditPieces_ID <= 0) set_Value ("XX_R_DebtCreditPieces_ID", null);
        else
        set_Value ("XX_R_DebtCreditPieces_ID", Integer.valueOf(XX_R_DebtCreditPieces_ID));
        
    }
    
    /** Get DebtCreditPieces.
    @return DebtCreditPieces */
    public int getXX_R_DebtCreditPieces_ID() 
    {
        return get_ValueAsInt("XX_R_DebtCreditPieces_ID");
        
    }
    
    /** Set DebtCreditReport.
    @param XX_R_DebtCreditReport_ID DebtCreditReport */
    public void setXX_R_DebtCreditReport_ID (int XX_R_DebtCreditReport_ID)
    {
        if (XX_R_DebtCreditReport_ID <= 0) set_Value ("XX_R_DebtCreditReport_ID", null);
        else
        set_Value ("XX_R_DebtCreditReport_ID", Integer.valueOf(XX_R_DebtCreditReport_ID));
        
    }
    
    /** Get DebtCreditReport.
    @return DebtCreditReport */
    public int getXX_R_DebtCreditReport_ID() 
    {
        return get_ValueAsInt("XX_R_DebtCreditReport_ID");
        
    }
    
    /** Set XX_R_DispatchGuideReport_ID.
    @param XX_R_DispatchGuideReport_ID XX_R_DispatchGuideReport_ID */
    public void setXX_R_DispatchGuideReport_ID (int XX_R_DispatchGuideReport_ID)
    {
        if (XX_R_DispatchGuideReport_ID <= 0) set_Value ("XX_R_DispatchGuideReport_ID", null);
        else
        set_Value ("XX_R_DispatchGuideReport_ID", Integer.valueOf(XX_R_DispatchGuideReport_ID));
        
    }
    
    /** Get XX_R_DispatchGuideReport_ID.
    @return XX_R_DispatchGuideReport_ID */
    public int getXX_R_DispatchGuideReport_ID() 
    {
        return get_ValueAsInt("XX_R_DispatchGuideReport_ID");
        
    }
    
    /** Set Return Motive.
    @param XX_ReturnMotive_ID Motivo de Devolución */
    public void setXX_ReturnMotive_ID (int XX_ReturnMotive_ID)
    {
        if (XX_ReturnMotive_ID <= 0) set_Value ("XX_ReturnMotive_ID", null);
        else
        set_Value ("XX_ReturnMotive_ID", Integer.valueOf(XX_ReturnMotive_ID));
        
    }
    
    /** Get Return Motive.
    @return Motivo de Devolución */
    public int getXX_ReturnMotive_ID() 
    {
        return get_ValueAsInt("XX_ReturnMotive_ID");
        
    }
    
    /** Set XX_R_ExitAuthoReport_ID.
    @param XX_R_ExitAuthoReport_ID XX_R_ExitAuthoReport_ID */
    public void setXX_R_ExitAuthoReport_ID (int XX_R_ExitAuthoReport_ID)
    {
        if (XX_R_ExitAuthoReport_ID <= 0) set_Value ("XX_R_ExitAuthoReport_ID", null);
        else
        set_Value ("XX_R_ExitAuthoReport_ID", Integer.valueOf(XX_R_ExitAuthoReport_ID));
        
    }
    
    /** Get XX_R_ExitAuthoReport_ID.
    @return XX_R_ExitAuthoReport_ID */
    public int getXX_R_ExitAuthoReport_ID() 
    {
        return get_ValueAsInt("XX_R_ExitAuthoReport_ID");
        
    }
    
    /** Set XX_R_PhysicalDistriReport_ID.
    @param XX_R_PhysicalDistriReport_ID XX_R_PhysicalDistriReport_ID */
    public void setXX_R_PhysicalDistriReport_ID (int XX_R_PhysicalDistriReport_ID)
    {
        if (XX_R_PhysicalDistriReport_ID <= 0) set_Value ("XX_R_PhysicalDistriReport_ID", null);
        else
        set_Value ("XX_R_PhysicalDistriReport_ID", Integer.valueOf(XX_R_PhysicalDistriReport_ID));
        
    }
    
    /** Get XX_R_PhysicalDistriReport_ID.
    @return XX_R_PhysicalDistriReport_ID */
    public int getXX_R_PhysicalDistriReport_ID() 
    {
        return get_ValueAsInt("XX_R_PhysicalDistriReport_ID");
        
    }
    
    /** Set XX_VSI_KeyNameInfo_ID.
    @param XX_VSI_KeyNameInfo_ID XX_VSI_KeyNameInfo_ID */
    public void setXX_VSI_KeyNameInfo_ID (int XX_VSI_KeyNameInfo_ID)
    {
        if (XX_VSI_KeyNameInfo_ID < 1) throw new IllegalArgumentException ("XX_VSI_KeyNameInfo_ID is mandatory.");
        set_ValueNoCheck ("XX_VSI_KeyNameInfo_ID", Integer.valueOf(XX_VSI_KeyNameInfo_ID));
        
    }
    
    /** Get XX_VSI_KeyNameInfo_ID.
    @return XX_VSI_KeyNameInfo_ID */
    public int getXX_VSI_KeyNameInfo_ID() 
    {
        return get_ValueAsInt("XX_VSI_KeyNameInfo_ID");
        
    }
    
    /** Set Workflow Asignación de Precios Definitivos.
    @param XX_WF_DefinitivePrices Workflow Asignación de Precios Definitivos */
    public void setXX_WF_DefinitivePrices (int XX_WF_DefinitivePrices)
    {
        set_Value ("XX_WF_DefinitivePrices", Integer.valueOf(XX_WF_DefinitivePrices));
        
    }
    
    /** Get Workflow Asignación de Precios Definitivos.
    @return Workflow Asignación de Precios Definitivos */
    public int getXX_WF_DefinitivePrices() 
    {
        return get_ValueAsInt("XX_WF_DefinitivePrices");
        
    }
    
    /** Set Workflow Aprobación OC sobregirada .
    @param XX_WF_POApproval Workflow Aprobación OC sobregirada  */
    public void setXX_WF_POApproval (int XX_WF_POApproval)
    {
        set_Value ("XX_WF_POApproval", Integer.valueOf(XX_WF_POApproval));
        
    }
    
    /** Get Workflow Aprobación OC sobregirada .
    @return Workflow Aprobación OC sobregirada  */
    public int getXX_WF_POApproval() 
    {
        return get_ValueAsInt("XX_WF_POApproval");
        
    }
    
    /** Set Workflow aprobacion OC - Jefe de Caregoría.
    @param XX_WF_POApproval_Category Workflow aprobacion OC - Jefe de Caregoría */
    public void setXX_WF_POApproval_Category (int XX_WF_POApproval_Category)
    {
        set_Value ("XX_WF_POApproval_Category", Integer.valueOf(XX_WF_POApproval_Category));
        
    }
    
    /** Get Workflow aprobacion OC - Jefe de Caregoría.
    @return Workflow aprobacion OC - Jefe de Caregoría */
    public int getXX_WF_POApproval_Category() 
    {
        return get_ValueAsInt("XX_WF_POApproval_Category");
        
    }
    
    /** Set Workflow aprobacion OC - Jefe de Planificación.
    @param XX_WF_POApproval_Planning Workflow aprobacion OC - Jefe de Planificación */
    public void setXX_WF_POApproval_Planning (int XX_WF_POApproval_Planning)
    {
        set_Value ("XX_WF_POApproval_Planning", Integer.valueOf(XX_WF_POApproval_Planning));
        
    }
    
    /** Get Workflow aprobacion OC - Jefe de Planificación.
    @return Workflow aprobacion OC - Jefe de Planificación */
    public int getXX_WF_POApproval_Planning() 
    {
        return get_ValueAsInt("XX_WF_POApproval_Planning");
        
    }
    
    
}
