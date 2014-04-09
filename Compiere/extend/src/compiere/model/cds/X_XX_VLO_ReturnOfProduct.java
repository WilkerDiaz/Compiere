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
/** Generated Model for XX_VLO_ReturnOfProduct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_ReturnOfProduct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_ReturnOfProduct_ID id
    @param trx transaction
    */
    public X_XX_VLO_ReturnOfProduct (Ctx ctx, int XX_VLO_ReturnOfProduct_ID, Trx trx)
    {
        super (ctx, XX_VLO_ReturnOfProduct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_ReturnOfProduct_ID == 0)
        {
            setC_BPartner_ID (0);
            setXX_Status (null);	// DPR
            setXX_VLO_ReturnOfProduct_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_ReturnOfProduct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27609214713789L;
    /** Last Updated Timestamp 2012-01-20 15:26:37.0 */
    public static final long updatedMS = 1327089397000L;
    /** AD_Table_ID=1000237 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_ReturnOfProduct");
        
    }
    ;
    
    /** TableName=XX_VLO_ReturnOfProduct */
    public static final String Table_Name="XX_VLO_ReturnOfProduct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Inventory Move.
    @param M_Movement_ID Movement of Inventory */
    public void setM_Movement_ID (int M_Movement_ID)
    {
        if (M_Movement_ID <= 0) set_Value ("M_Movement_ID", null);
        else
        set_Value ("M_Movement_ID", Integer.valueOf(M_Movement_ID));
        
    }
    
    /** Get Inventory Move.
    @return Movement of Inventory */
    public int getM_Movement_ID() 
    {
        return get_ValueAsInt("M_Movement_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** EMail+Notice = B */
    public static final String NOTIFICATIONTYPE_EMailPlusNotice = X_Ref_AD_User_NotificationType.E_MAIL_PLUS_NOTICE.getValue();
    /** Calls   = C */
    public static final String NOTIFICATIONTYPE_Calls = X_Ref_AD_User_NotificationType.CALLS.getValue();
    /** EMail = E */
    public static final String NOTIFICATIONTYPE_EMail = X_Ref_AD_User_NotificationType.E_MAIL.getValue();
    /** Notice = N */
    public static final String NOTIFICATIONTYPE_Notice = X_Ref_AD_User_NotificationType.NOTICE.getValue();
    /** None = X */
    public static final String NOTIFICATIONTYPE_None = X_Ref_AD_User_NotificationType.NONE.getValue();
    /** Set Notification Type.
    @param NotificationType Type of Notifications */
    public void setNotificationType (String NotificationType)
    {
        if (!X_Ref_AD_User_NotificationType.isValid(NotificationType))
        throw new IllegalArgumentException ("NotificationType Invalid value - " + NotificationType + " - Reference_ID=344 - B - C - E - N - X");
        set_Value ("NotificationType", NotificationType);
        
    }
    
    /** Get Notification Type.
    @return Type of Notifications */
    public String getNotificationType() 
    {
        return (String)get_Value("NotificationType");
        
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
    
    /** Set Cedula/RIF.
    @param XX_CI_RIF Cedula o RIF del Proveedor o Cliente */
    public void setXX_CI_RIF (String XX_CI_RIF)
    {
        set_Value ("XX_CI_RIF", XX_CI_RIF);
        
    }
    
    /** Get Cedula/RIF.
    @return Cedula o RIF del Proveedor o Cliente */
    public String getXX_CI_RIF() 
    {
        return (String)get_Value("XX_CI_RIF");
        
    }
    
    /** Set Vendor Delivery Date.
    @param XX_DateDeliveryVendor Provider Delivery Date */
    public void setXX_DateDeliveryVendor (Timestamp XX_DateDeliveryVendor)
    {
        set_ValueNoCheck ("XX_DateDeliveryVendor", XX_DateDeliveryVendor);
        
    }
    
    /** Get Vendor Delivery Date.
    @return Provider Delivery Date */
    public Timestamp getXX_DateDeliveryVendor() 
    {
        return (Timestamp)get_Value("XX_DateDeliveryVendor");
        
    }
    
    /** Set Return Date.
    @param XX_DateDEV Return Date  */
    public void setXX_DateDEV (Timestamp XX_DateDEV)
    {
        throw new IllegalArgumentException ("XX_DateDEV is virtual column");
        
    }
    
    /** Get Return Date.
    @return Return Date  */
    public Timestamp getXX_DateDEV() 
    {
        return (Timestamp)get_Value("XX_DateDEV");
        
    }
    
    /** Set Descuento.
    @param XX_Descuento Descuento */
    public void setXX_Descuento (String XX_Descuento)
    {
        throw new IllegalArgumentException ("XX_Descuento is virtual column");
        
    }
    
    /** Get Descuento.
    @return Descuento */
    public String getXX_Descuento() 
    {
        return (String)get_Value("XX_Descuento");
        
    }
    
    /** Set Discount ?.
    @param XX_Discount Discount Application */
    public void setXX_Discount (boolean XX_Discount)
    {
        set_Value ("XX_Discount", Boolean.valueOf(XX_Discount));
        
    }
    
    /** Get Discount ?.
    @return Discount Application */
    public boolean isXX_Discount() 
    {
        return get_ValueAsBoolean("XX_Discount");
        
    }
    
    /** Set Document Number.
    @param XX_DocumentNo Document Number */
    public void setXX_DocumentNo (String XX_DocumentNo)
    {
        set_Value ("XX_DocumentNo", XX_DocumentNo);
        
    }
    
    /** Get Document Number.
    @return Document Number */
    public String getXX_DocumentNo() 
    {
        return (String)get_Value("XX_DocumentNo");
        
    }
    
    /** Set Assistant ID.
    @param XX_IdAsis_ID Assistant ID */
    public void setXX_IdAsis_ID (int XX_IdAsis_ID)
    {
        if (XX_IdAsis_ID <= 0) set_Value ("XX_IdAsis_ID", null);
        else
        set_Value ("XX_IdAsis_ID", Integer.valueOf(XX_IdAsis_ID));
        
    }
    
    /** Get Assistant ID.
    @return Assistant ID */
    public int getXX_IdAsis_ID() 
    {
        return get_ValueAsInt("XX_IdAsis_ID");
        
    }
    
    /** Set Notification Date.
    @param XX_NotificationDate Notification Date */
    public void setXX_NotificationDate (Timestamp XX_NotificationDate)
    {
        set_Value ("XX_NotificationDate", XX_NotificationDate);
        
    }
    
    /** Get Notification Date.
    @return Notification Date */
    public Timestamp getXX_NotificationDate() 
    {
        return (Timestamp)get_Value("XX_NotificationDate");
        
    }
    
    /** Set 2°- Notification  Date.
    @param XX_NotificationDate2 Notification Second Date */
    public void setXX_NotificationDate2 (Timestamp XX_NotificationDate2)
    {
        set_Value ("XX_NotificationDate2", XX_NotificationDate2);
        
    }
    
    /** Get 2°- Notification  Date.
    @return Notification Second Date */
    public Timestamp getXX_NotificationDate2() 
    {
        return (Timestamp)get_Value("XX_NotificationDate2");
        
    }
    
    /** Set 3°- Notification  Date.
    @param XX_NotificationDate3 Notification Third Date */
    public void setXX_NotificationDate3 (Timestamp XX_NotificationDate3)
    {
        set_Value ("XX_NotificationDate3", XX_NotificationDate3);
        
    }
    
    /** Get 3°- Notification  Date.
    @return Notification Third Date */
    public Timestamp getXX_NotificationDate3() 
    {
        return (Timestamp)get_Value("XX_NotificationDate3");
        
    }
    
    /** Set Name Person withdraws Return.
    @param XX_NPerWithdrawsReturn Name Person withdraws Return */
    public void setXX_NPerWithdrawsReturn (String XX_NPerWithdrawsReturn)
    {
        set_Value ("XX_NPerWithdrawsReturn", XX_NPerWithdrawsReturn);
        
    }
    
    /** Get Name Person withdraws Return.
    @return Name Person withdraws Return */
    public String getXX_NPerWithdrawsReturn() 
    {
        return (String)get_Value("XX_NPerWithdrawsReturn");
        
    }
    
    /** Set Order Info.
    @param XX_OrderInfo_ID Order Info */
    public void setXX_OrderInfo_ID (int XX_OrderInfo_ID)
    {
        if (XX_OrderInfo_ID <= 0) set_Value ("XX_OrderInfo_ID", null);
        else
        set_Value ("XX_OrderInfo_ID", Integer.valueOf(XX_OrderInfo_ID));
        
    }
    
    /** Get Order Info.
    @return Order Info */
    public int getXX_OrderInfo_ID() 
    {
        return get_ValueAsInt("XX_OrderInfo_ID");
        
    }
    
    /** Set Name Person Receives Call.
    @param XX_PersonReceivesCall Name person receives call */
    public void setXX_PersonReceivesCall (String XX_PersonReceivesCall)
    {
        set_Value ("XX_PersonReceivesCall", XX_PersonReceivesCall);
        
    }
    
    /** Get Name Person Receives Call.
    @return Name person receives call */
    public String getXX_PersonReceivesCall() 
    {
        return (String)get_Value("XX_PersonReceivesCall");
        
    }
    
    /** Set Name Person Receives 2°- Call .
    @param XX_PersonReceivesCall2 Name Person Receives 2°- Call  */
    public void setXX_PersonReceivesCall2 (String XX_PersonReceivesCall2)
    {
        set_Value ("XX_PersonReceivesCall2", XX_PersonReceivesCall2);
        
    }
    
    /** Get Name Person Receives 2°- Call .
    @return Name Person Receives 2°- Call  */
    public String getXX_PersonReceivesCall2() 
    {
        return (String)get_Value("XX_PersonReceivesCall2");
        
    }
    
    /** Set Name Person Receives 3°- Call.
    @param XX_PersonReceivesCall3 Name Person Receives 3°- Call   */
    public void setXX_PersonReceivesCall3 (String XX_PersonReceivesCall3)
    {
        set_Value ("XX_PersonReceivesCall3", XX_PersonReceivesCall3);
        
    }
    
    /** Get Name Person Receives 3°- Call.
    @return Name Person Receives 3°- Call   */
    public String getXX_PersonReceivesCall3() 
    {
        return (String)get_Value("XX_PersonReceivesCall3");
        
    }
    
    /** Set Print Product Return's Document.
    @param XX_PrintProductReturn Print Product Return's Document */
    public void setXX_PrintProductReturn (String XX_PrintProductReturn)
    {
        set_Value ("XX_PrintProductReturn", XX_PrintProductReturn);
        
    }
    
    /** Get Print Product Return's Document.
    @return Print Product Return's Document */
    public String getXX_PrintProductReturn() 
    {
        return (String)get_Value("XX_PrintProductReturn");
        
    }
    
    /** Quality Control = CH */
    public static final String XX_RETURNEDFROM_QualityControl = X_Ref_XX_ReturnedFrom.QUALITY_CONTROL.getValue();
    /** Store = ST */
    public static final String XX_RETURNEDFROM_Store = X_Ref_XX_ReturnedFrom.STORE.getValue();
    /** Set Returned From.
    @param XX_ReturnedFrom Returned From */
    public void setXX_ReturnedFrom (String XX_ReturnedFrom)
    {
        if (!X_Ref_XX_ReturnedFrom.isValid(XX_ReturnedFrom))
        throw new IllegalArgumentException ("XX_ReturnedFrom Invalid value - " + XX_ReturnedFrom + " - Reference_ID=1000259 - CH - ST");
        set_Value ("XX_ReturnedFrom", XX_ReturnedFrom);
        
    }
    
    /** Get Returned From.
    @return Returned From */
    public String getXX_ReturnedFrom() 
    {
        return (String)get_Value("XX_ReturnedFrom");
        
    }
    
    /** Abandonada = ABA */
    public static final String XX_STATUS_Abandonada = X_Ref_XX_StatusReturn.ABANDONADA.getValue();
    /** Pendiente Por Retirar = DPR */
    public static final String XX_STATUS_PendientePorRetirar = X_Ref_XX_StatusReturn.PENDIENTE_POR_RETIRAR.getValue();
    /** Retirada por el Proveedor = DRE */
    public static final String XX_STATUS_RetiradaPorElProveedor = X_Ref_XX_StatusReturn.RETIRADA_POR_EL_PROVEEDOR.getValue();
    /** Devolucion Pendiente por Completar Chequeo = PRE */
    public static final String XX_STATUS_DevolucionPendientePorCompletarChequeo = X_Ref_XX_StatusReturn.DEVOLUCION_PENDIENTE_POR_COMPLETAR_CHEQUEO.getValue();
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (String XX_Status)
    {
        if (XX_Status == null) throw new IllegalArgumentException ("XX_Status is mandatory");
        if (!X_Ref_XX_StatusReturn.isValid(XX_Status))
        throw new IllegalArgumentException ("XX_Status Invalid value - " + XX_Status + " - Reference_ID=1000214 - ABA - DPR - DRE - PRE");
        set_Value ("XX_Status", XX_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_Status() 
    {
        return (String)get_Value("XX_Status");
        
    }
    
    /** Set Total Pieces Returned.
    @param XX_TotalPieces Total Pieces Returned */
    public void setXX_TotalPieces (int XX_TotalPieces)
    {
        throw new IllegalArgumentException ("XX_TotalPieces is virtual column");
        
    }
    
    /** Get Total Pieces Returned.
    @return Total Pieces Returned */
    public int getXX_TotalPieces() 
    {
        return get_ValueAsInt("XX_TotalPieces");
        
    }
    
    /** Set Nro. Return (ID).
    @param XX_VLO_ReturnOfProduct_ID Id Return Of Product */
    public void setXX_VLO_ReturnOfProduct_ID (int XX_VLO_ReturnOfProduct_ID)
    {
        if (XX_VLO_ReturnOfProduct_ID < 1) throw new IllegalArgumentException ("XX_VLO_ReturnOfProduct_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_ReturnOfProduct_ID", Integer.valueOf(XX_VLO_ReturnOfProduct_ID));
        
    }
    
    /** Get Nro. Return (ID).
    @return Id Return Of Product */
    public int getXX_VLO_ReturnOfProduct_ID() 
    {
        return get_ValueAsInt("XX_VLO_ReturnOfProduct_ID");
        
    }
    
    
}
