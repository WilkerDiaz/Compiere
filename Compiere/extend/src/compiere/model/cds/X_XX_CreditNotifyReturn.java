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
/** Generated Model for XX_CreditNotifyReturn
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_CreditNotifyReturn extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_CreditNotifyReturn_ID id
    @param trx transaction
    */
    public X_XX_CreditNotifyReturn (Ctx ctx, int XX_CreditNotifyReturn_ID, Trx trx)
    {
        super (ctx, XX_CreditNotifyReturn_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_CreditNotifyReturn_ID == 0)
        {
            setXX_CreditNotifyReturn_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_CreditNotifyReturn (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27619491212789L;
    /** Last Updated Timestamp 2012-05-18 14:01:36.0 */
    public static final long updatedMS = 1337365896000L;
    /** AD_Table_ID=1000279 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_CreditNotifyReturn");
        
    }
    ;
    
    /** TableName=XX_CreditNotifyReturn */
    public static final String Table_Name="XX_CreditNotifyReturn";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
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
    
    /** Set Tax Category.
    @param C_TaxCategory_ID Tax Category */
    public void setC_TaxCategory_ID (int C_TaxCategory_ID)
    {
        if (C_TaxCategory_ID <= 0) set_Value ("C_TaxCategory_ID", null);
        else
        set_Value ("C_TaxCategory_ID", Integer.valueOf(C_TaxCategory_ID));
        
    }
    
    /** Get Tax Category.
    @return Tax Category */
    public int getC_TaxCategory_ID() 
    {
        return get_ValueAsInt("C_TaxCategory_ID");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID <= 0) set_Value ("C_Tax_ID", null);
        else
        set_Value ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
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
    
    /** Set Document Type.
    @param DocumentType Document Type */
    public void setDocumentType (int DocumentType)
    {
        set_Value ("DocumentType", Integer.valueOf(DocumentType));
        
    }
    
    /** Get Document Type.
    @return Document Type */
    public int getDocumentType() 
    {
        return get_ValueAsInt("DocumentType");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Amount by notification.
    @param XX_Amount Monto del aviso de Credito */
    public void setXX_Amount (java.math.BigDecimal XX_Amount)
    {
        set_Value ("XX_Amount", XX_Amount);
        
    }
    
    /** Get Amount by notification.
    @return Monto del aviso de Credito */
    public java.math.BigDecimal getXX_Amount() 
    {
        return get_ValueAsBigDecimal("XX_Amount");
        
    }
    
    /** Set Amount IVA.
    @param XX_Amount_IVA Monto Impuesto al Valor Agregado  */
    public void setXX_Amount_IVA (java.math.BigDecimal XX_Amount_IVA)
    {
        set_Value ("XX_Amount_IVA", XX_Amount_IVA);
        
    }
    
    /** Get Amount IVA.
    @return Monto Impuesto al Valor Agregado  */
    public java.math.BigDecimal getXX_Amount_IVA() 
    {
        return get_ValueAsBigDecimal("XX_Amount_IVA");
        
    }
    
    /** Set Cancel Credit Notification.
    @param XX_Cancel_Credit_Notification Cancel Credit Notification */
    public void setXX_Cancel_Credit_Notification (String XX_Cancel_Credit_Notification)
    {
        set_Value ("XX_Cancel_Credit_Notification", XX_Cancel_Credit_Notification);
        
    }
    
    /** Get Cancel Credit Notification.
    @return Cancel Credit Notification */
    public String getXX_Cancel_Credit_Notification() 
    {
        return (String)get_Value("XX_Cancel_Credit_Notification");
        
    }
    
    /** Set Factor of Change.
    @param XX_ChangeFactor Factor de  Cambio */
    public void setXX_ChangeFactor (java.math.BigDecimal XX_ChangeFactor)
    {
        set_Value ("XX_ChangeFactor", XX_ChangeFactor);
        
    }
    
    /** Get Factor of Change.
    @return Factor de  Cambio */
    public java.math.BigDecimal getXX_ChangeFactor() 
    {
        return get_ValueAsBigDecimal("XX_ChangeFactor");
        
    }
    
    /** Set Create Credit Note.
    @param XX_Create_Credit_Note Create Credit Note */
    public void setXX_Create_Credit_Note (String XX_Create_Credit_Note)
    {
        set_Value ("XX_Create_Credit_Note", XX_Create_Credit_Note);
        
    }
    
    /** Get Create Credit Note.
    @return Create Credit Note */
    public String getXX_Create_Credit_Note() 
    {
        return (String)get_Value("XX_Create_Credit_Note");
        
    }
    
    /** Set Nro. Credit Notify.
    @param XX_CreditNotifyReturn_ID Número de Aviso de Credito */
    public void setXX_CreditNotifyReturn_ID (int XX_CreditNotifyReturn_ID)
    {
        if (XX_CreditNotifyReturn_ID < 1) throw new IllegalArgumentException ("XX_CreditNotifyReturn_ID is mandatory.");
        set_ValueNoCheck ("XX_CreditNotifyReturn_ID", Integer.valueOf(XX_CreditNotifyReturn_ID));
        
    }
    
    /** Get Nro. Credit Notify.
    @return Número de Aviso de Credito */
    public int getXX_CreditNotifyReturn_ID() 
    {
        return get_ValueAsInt("XX_CreditNotifyReturn_ID");
        
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
    
    /** APORTE A PUBLICIDAD = AAP */
    public static final String XX_NOTIFICATIONTYPE_APORTEAPUBLICIDAD = X_Ref_XX_NotificationType.APORTEAPUBLICIDAD.getValue();
    /** ACUERDO COMERCIAL = ACC */
    public static final String XX_NOTIFICATIONTYPE_ACUERDOCOMERCIAL = X_Ref_XX_NotificationType.ACUERDOCOMERCIAL.getValue();
    /** DEMORA EN ENTREGA = DEE */
    public static final String XX_NOTIFICATIONTYPE_DEMORAENENTREGA = X_Ref_XX_NotificationType.DEMORAENENTREGA.getValue();
    /** GASTOS DE ALMACENAJE = GAL */
    public static final String XX_NOTIFICATIONTYPE_GASTOSDEALMACENAJE = X_Ref_XX_NotificationType.GASTOSDEALMACENAJE.getValue();
    /** Set Notification Type.
    @param XX_NotificationType Notification Type */
    public void setXX_NotificationType (String XX_NotificationType)
    {
        if (!X_Ref_XX_NotificationType.isValid(XX_NotificationType))
        throw new IllegalArgumentException ("XX_NotificationType Invalid value - " + XX_NotificationType + " - Reference_ID=1000296 - AAP - ACC - DEE - GAL");
        set_Value ("XX_NotificationType", XX_NotificationType);
        
    }
    
    /** Get Notification Type.
    @return Notification Type */
    public String getXX_NotificationType() 
    {
        return (String)get_Value("XX_NotificationType");
        
    }
    
    /** Active = ACT */
    public static final String XX_STATUS_Active = X_Ref_XX_StatusNotificationCredit.ACTIVE.getValue();
    /** Anulado = ANU */
    public static final String XX_STATUS_Anulado = X_Ref_XX_StatusNotificationCredit.ANULADO.getValue();
    /** Cerrado = CER */
    public static final String XX_STATUS_Cerrado = X_Ref_XX_StatusNotificationCredit.CERRADO.getValue();
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (String XX_Status)
    {
        if (!X_Ref_XX_StatusNotificationCredit.isValid(XX_Status))
        throw new IllegalArgumentException ("XX_Status Invalid value - " + XX_Status + " - Reference_ID=1000231 - ACT - ANU - CER");
        set_Value ("XX_Status", XX_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_Status() 
    {
        return (String)get_Value("XX_Status");
        
    }
    
    /** Set Origin Currency Cost.
    @param XX_UnitPurchasePrice Origin Currency Cost */
    public void setXX_UnitPurchasePrice (java.math.BigDecimal XX_UnitPurchasePrice)
    {
        set_Value ("XX_UnitPurchasePrice", XX_UnitPurchasePrice);
        
    }
    
    /** Get Origin Currency Cost.
    @return Origin Currency Cost */
    public java.math.BigDecimal getXX_UnitPurchasePrice() 
    {
        return get_ValueAsBigDecimal("XX_UnitPurchasePrice");
        
    }
    
    /** Set XX_UnitPurchasePriceBs.
    @param XX_UnitPurchasePriceBs XX_UnitPurchasePriceBs */
    public void setXX_UnitPurchasePriceBs (java.math.BigDecimal XX_UnitPurchasePriceBs)
    {
        set_Value ("XX_UnitPurchasePriceBs", XX_UnitPurchasePriceBs);
        
    }
    
    /** Get XX_UnitPurchasePriceBs.
    @return XX_UnitPurchasePriceBs */
    public java.math.BigDecimal getXX_UnitPurchasePriceBs() 
    {
        return get_ValueAsBigDecimal("XX_UnitPurchasePriceBs");
        
    }
    
    /** Set Week Created.
    @param XX_WeekCreated Week Created */
    public void setXX_WeekCreated (java.math.BigDecimal XX_WeekCreated)
    {
        set_Value ("XX_WeekCreated", XX_WeekCreated);
        
    }
    
    /** Get Week Created.
    @return Week Created */
    public java.math.BigDecimal getXX_WeekCreated() 
    {
        return get_ValueAsBigDecimal("XX_WeekCreated");
        
    }
    
    
}
