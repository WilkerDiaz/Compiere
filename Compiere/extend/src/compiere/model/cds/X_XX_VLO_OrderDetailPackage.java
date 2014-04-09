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
/** Generated Model for XX_VLO_OrderDetailPackage
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_OrderDetailPackage extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_OrderDetailPackage_ID id
    @param trx transaction
    */
    public X_XX_VLO_OrderDetailPackage (Ctx ctx, int XX_VLO_OrderDetailPackage_ID, Trx trx)
    {
        super (ctx, XX_VLO_OrderDetailPackage_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_OrderDetailPackage_ID == 0)
        {
            setValue (null);
            setXX_VLO_OrderDetailPackage_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_OrderDetailPackage (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27563155802789L;
    /** Last Updated Timestamp 2010-08-05 13:18:06.0 */
    public static final long updatedMS = 1281030486000L;
    /** AD_Table_ID=1000303 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_OrderDetailPackage");
        
    }
    ;
    
    /** TableName=XX_VLO_OrderDetailPackage */
    public static final String Table_Name="XX_VLO_OrderDetailPackage";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Aprobado = APR */
    public static final String XX_ORDERDETAILPACKAGESTATUS_Aprobado = X_Ref_XX_Ref_OrderDetailPackageStatus.APROBADO.getValue();
    /** Pendiente = PEN */
    public static final String XX_ORDERDETAILPACKAGESTATUS_Pendiente = X_Ref_XX_Ref_OrderDetailPackageStatus.PENDIENTE.getValue();
    /** Recibido = REC */
    public static final String XX_ORDERDETAILPACKAGESTATUS_Recibido = X_Ref_XX_Ref_OrderDetailPackageStatus.RECIBIDO.getValue();
    /** Set Order Detail Package Status.
    @param XX_OrderDetailPackageStatus Order Detail Package Status */
    public void setXX_OrderDetailPackageStatus (String XX_OrderDetailPackageStatus)
    {
        if (!X_Ref_XX_Ref_OrderDetailPackageStatus.isValid(XX_OrderDetailPackageStatus))
        throw new IllegalArgumentException ("XX_OrderDetailPackageStatus Invalid value - " + XX_OrderDetailPackageStatus + " - Reference_ID=1000247 - APR - PEN - REC");
        set_Value ("XX_OrderDetailPackageStatus", XX_OrderDetailPackageStatus);
        
    }
    
    /** Get Order Detail Package Status.
    @return Order Detail Package Status */
    public String getXX_OrderDetailPackageStatus() 
    {
        return (String)get_Value("XX_OrderDetailPackageStatus");
        
    }
    
    /** Set Dispatch Guide.
    @param XX_VLO_DispatchGuide_ID Dispatch Guide */
    public void setXX_VLO_DispatchGuide_ID (int XX_VLO_DispatchGuide_ID)
    {
        if (XX_VLO_DispatchGuide_ID <= 0) set_Value ("XX_VLO_DispatchGuide_ID", null);
        else
        set_Value ("XX_VLO_DispatchGuide_ID", Integer.valueOf(XX_VLO_DispatchGuide_ID));
        
    }
    
    /** Get Dispatch Guide.
    @return Dispatch Guide */
    public int getXX_VLO_DispatchGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_DispatchGuide_ID");
        
    }
    
    /** Set XX_VLO_OrderDetailPackage_ID.
    @param XX_VLO_OrderDetailPackage_ID XX_VLO_OrderDetailPackage_ID */
    public void setXX_VLO_OrderDetailPackage_ID (int XX_VLO_OrderDetailPackage_ID)
    {
        if (XX_VLO_OrderDetailPackage_ID < 1) throw new IllegalArgumentException ("XX_VLO_OrderDetailPackage_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_OrderDetailPackage_ID", Integer.valueOf(XX_VLO_OrderDetailPackage_ID));
        
    }
    
    /** Get XX_VLO_OrderDetailPackage_ID.
    @return XX_VLO_OrderDetailPackage_ID */
    public int getXX_VLO_OrderDetailPackage_ID() 
    {
        return get_ValueAsInt("XX_VLO_OrderDetailPackage_ID");
        
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
    
    
}
