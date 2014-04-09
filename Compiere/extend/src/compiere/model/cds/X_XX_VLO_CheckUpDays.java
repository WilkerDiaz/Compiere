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
/** Generated Model for XX_VLO_CheckUpDays
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_CheckUpDays extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_CheckUpDays_ID id
    @param trx transaction
    */
    public X_XX_VLO_CheckUpDays (Ctx ctx, int XX_VLO_CheckUpDays_ID, Trx trx)
    {
        super (ctx, XX_VLO_CheckUpDays_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_CheckUpDays_ID == 0)
        {
            setXX_VLO_CheckupDays_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_CheckUpDays (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27593473720789L;
    /** Last Updated Timestamp 2011-07-22 10:56:44.0 */
    public static final long updatedMS = 1311348404000L;
    /** AD_Table_ID=1000390 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_CheckUpDays");
        
    }
    ;
    
    /** TableName=XX_VLO_CheckUpDays */
    public static final String Table_Name="XX_VLO_CheckUpDays";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Collection Order Request.
    @param XX_CollectionOrderRequest_ID Collection Order Request */
    public void setXX_CollectionOrderRequest_ID (int XX_CollectionOrderRequest_ID)
    {
        if (XX_CollectionOrderRequest_ID <= 0) set_Value ("XX_CollectionOrderRequest_ID", null);
        else
        set_Value ("XX_CollectionOrderRequest_ID", Integer.valueOf(XX_CollectionOrderRequest_ID));
        
    }
    
    /** Get Collection Order Request.
    @return Collection Order Request */
    public int getXX_CollectionOrderRequest_ID() 
    {
        return get_ValueAsInt("XX_CollectionOrderRequest_ID");
        
    }
    
    /** Set Date Order Request.
    @param XX_DateOrderRequest Date Order Request */
    public void setXX_DateOrderRequest (Timestamp XX_DateOrderRequest)
    {
        set_Value ("XX_DateOrderRequest", XX_DateOrderRequest);
        
    }
    
    /** Get Date Order Request.
    @return Date Order Request */
    public Timestamp getXX_DateOrderRequest() 
    {
        return (Timestamp)get_Value("XX_DateOrderRequest");
        
    }
    
    /** Set Date Order Request Until.
    @param XX_DateOrderRequestUntil Date Order Request Until */
    public void setXX_DateOrderRequestUntil (Timestamp XX_DateOrderRequestUntil)
    {
        set_Value ("XX_DateOrderRequestUntil", XX_DateOrderRequestUntil);
        
    }
    
    /** Get Date Order Request Until.
    @return Date Order Request Until */
    public Timestamp getXX_DateOrderRequestUntil() 
    {
        return (Timestamp)get_Value("XX_DateOrderRequestUntil");
        
    }
    
    /** Set Time in Days:Hours:Minutes.
    @param XX_DaysHoursMinutes Time in Days:Hours:Minutes */
    public void setXX_DaysHoursMinutes (String XX_DaysHoursMinutes)
    {
        set_Value ("XX_DaysHoursMinutes", XX_DaysHoursMinutes);
        
    }
    
    /** Get Time in Days:Hours:Minutes.
    @return Time in Days:Hours:Minutes */
    public String getXX_DaysHoursMinutes() 
    {
        return (String)get_Value("XX_DaysHoursMinutes");
        
    }
    
    /** Anulado = AN */
    public static final String XX_ORDERREQUESTSTATUS_Anulado = X_Ref_XX_VMR_OrderStatus.ANULADO.getValue();
    /** En Bahia = EB */
    public static final String XX_ORDERREQUESTSTATUS_EnBahia = X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue();
    /** En Tránsito = ET */
    public static final String XX_ORDERREQUESTSTATUS_EnTránsito = X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue();
    /** Por Fijar Precio = FP */
    public static final String XX_ORDERREQUESTSTATUS_PorFijarPrecio = X_Ref_XX_VMR_OrderStatus.POR_FIJAR_PRECIO.getValue();
    /** Por Etiquetar = PE */
    public static final String XX_ORDERREQUESTSTATUS_PorEtiquetar = X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue();
    /** En Tienda = TI */
    public static final String XX_ORDERREQUESTSTATUS_EnTienda = X_Ref_XX_VMR_OrderStatus.EN_TIENDA.getValue();
    /** Set Order Status.
    @param XX_OrderRequestStatus Order Status */
    public void setXX_OrderRequestStatus (String XX_OrderRequestStatus)
    {
        if (!X_Ref_XX_VMR_OrderStatus.isValid(XX_OrderRequestStatus))
        throw new IllegalArgumentException ("XX_OrderRequestStatus Invalid value - " + XX_OrderRequestStatus + " - Reference_ID=1000183 - AN - EB - ET - FP - PE - TI");
        set_Value ("XX_OrderRequestStatus", XX_OrderRequestStatus);
        
    }
    
    /** Get Order Status.
    @return Order Status */
    public String getXX_OrderRequestStatus() 
    {
        return (String)get_Value("XX_OrderRequestStatus");
        
    }
    
    /** CHEQUED = CH */
    public static final String XX_ORDERSTATUS_CHEQUED = X_Ref_XX_OrderStatusReceivedChequed.CHEQUED.getValue();
    /** RECEIVED = RE */
    public static final String XX_ORDERSTATUS_RECEIVED = X_Ref_XX_OrderStatusReceivedChequed.RECEIVED.getValue();
    /** Set PO Status.
    @param XX_OrderStatus Estado de la Orden de Compra */
    public void setXX_OrderStatus (String XX_OrderStatus)
    {
        if (!X_Ref_XX_OrderStatusReceivedChequed.isValid(XX_OrderStatus))
        throw new IllegalArgumentException ("XX_OrderStatus Invalid value - " + XX_OrderStatus + " - Reference_ID=1000346 - CH - RE");
        set_Value ("XX_OrderStatus", XX_OrderStatus);
        
    }
    
    /** Get PO Status.
    @return Estado de la Orden de Compra */
    public String getXX_OrderStatus() 
    {
        return (String)get_Value("XX_OrderStatus");
        
    }
    
    /** Set Package Order Request.
    @param XX_PackageOrderRequest_ID Package Order Request */
    public void setXX_PackageOrderRequest_ID (int XX_PackageOrderRequest_ID)
    {
        if (XX_PackageOrderRequest_ID <= 0) set_Value ("XX_PackageOrderRequest_ID", null);
        else
        set_Value ("XX_PackageOrderRequest_ID", Integer.valueOf(XX_PackageOrderRequest_ID));
        
    }
    
    /** Get Package Order Request.
    @return Package Order Request */
    public int getXX_PackageOrderRequest_ID() 
    {
        return get_ValueAsInt("XX_PackageOrderRequest_ID");
        
    }
    
    /** Set Reception Date.
    @param XX_ReceptionDate Reception Date */
    public void setXX_ReceptionDate (Timestamp XX_ReceptionDate)
    {
        set_Value ("XX_ReceptionDate", XX_ReceptionDate);
        
    }
    
    /** Get Reception Date.
    @return Reception Date */
    public Timestamp getXX_ReceptionDate() 
    {
        return (Timestamp)get_Value("XX_ReceptionDate");
        
    }
    
    /** Set Reception Date Until.
    @param XX_ReceptionDateUntil Reception Date Until */
    public void setXX_ReceptionDateUntil (Timestamp XX_ReceptionDateUntil)
    {
        set_Value ("XX_ReceptionDateUntil", XX_ReceptionDateUntil);
        
    }
    
    /** Get Reception Date Until.
    @return Reception Date Until */
    public Timestamp getXX_ReceptionDateUntil() 
    {
        return (Timestamp)get_Value("XX_ReceptionDateUntil");
        
    }
    
    /** Set XX_VLO_CheckupDays_ID.
    @param XX_VLO_CheckupDays_ID XX_VLO_CheckupDays_ID */
    public void setXX_VLO_CheckupDays_ID (int XX_VLO_CheckupDays_ID)
    {
        if (XX_VLO_CheckupDays_ID < 1) throw new IllegalArgumentException ("XX_VLO_CheckupDays_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_CheckupDays_ID", Integer.valueOf(XX_VLO_CheckupDays_ID));
        
    }
    
    /** Get XX_VLO_CheckupDays_ID.
    @return XX_VLO_CheckupDays_ID */
    public int getXX_VLO_CheckupDays_ID() 
    {
        return get_ValueAsInt("XX_VLO_CheckupDays_ID");
        
    }
    
    /** Set Collection.
    @param XX_VMR_Collection_ID ID de Colección */
    public void setXX_VMR_Collection_ID (int XX_VMR_Collection_ID)
    {
        if (XX_VMR_Collection_ID <= 0) set_Value ("XX_VMR_Collection_ID", null);
        else
        set_Value ("XX_VMR_Collection_ID", Integer.valueOf(XX_VMR_Collection_ID));
        
    }
    
    /** Get Collection.
    @return ID de Colección */
    public int getXX_VMR_Collection_ID() 
    {
        return get_ValueAsInt("XX_VMR_Collection_ID");
        
    }
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        if (XX_VMR_DistributionHeader_ID <= 0) set_Value ("XX_VMR_DistributionHeader_ID", null);
        else
        set_Value ("XX_VMR_DistributionHeader_ID", Integer.valueOf(XX_VMR_DistributionHeader_ID));
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set Placed Order.
    @param XX_VMR_Order_ID Placed Order */
    public void setXX_VMR_Order_ID (int XX_VMR_Order_ID)
    {
        if (XX_VMR_Order_ID <= 0) set_Value ("XX_VMR_Order_ID", null);
        else
        set_Value ("XX_VMR_Order_ID", Integer.valueOf(XX_VMR_Order_ID));
        
    }
    
    /** Get Placed Order.
    @return Placed Order */
    public int getXX_VMR_Order_ID() 
    {
        return get_ValueAsInt("XX_VMR_Order_ID");
        
    }
    
    /** Set Package.
    @param XX_VMR_Package_ID Package */
    public void setXX_VMR_Package_ID (int XX_VMR_Package_ID)
    {
        if (XX_VMR_Package_ID <= 0) set_Value ("XX_VMR_Package_ID", null);
        else
        set_Value ("XX_VMR_Package_ID", Integer.valueOf(XX_VMR_Package_ID));
        
    }
    
    /** Get Package.
    @return Package */
    public int getXX_VMR_Package_ID() 
    {
        return get_ValueAsInt("XX_VMR_Package_ID");
        
    }
    
    
}
