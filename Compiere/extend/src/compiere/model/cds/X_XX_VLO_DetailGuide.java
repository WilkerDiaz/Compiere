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
/** Generated Model for XX_VLO_DetailGuide
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_DetailGuide extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_DetailGuide_ID id
    @param trx transaction
    */
    public X_XX_VLO_DetailGuide (Ctx ctx, int XX_VLO_DetailGuide_ID, Trx trx)
    {
        super (ctx, XX_VLO_DetailGuide_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_DetailGuide_ID == 0)
        {
            setC_Order_ID (0);
            setXX_VLO_BoardingGuide_ID (0);
            setXX_VLO_DETAILGUIDE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_DetailGuide (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27594957374789L;
    /** Last Updated Timestamp 2011-08-08 15:04:18.0 */
    public static final long updatedMS = 1312832058000L;
    /** AD_Table_ID=1000234 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_DetailGuide");
        
    }
    ;
    
    /** TableName=XX_VLO_DetailGuide */
    public static final String Table_Name="XX_VLO_DetailGuide";
    
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
        if (C_Order_ID < 1) throw new IllegalArgumentException ("C_Order_ID is mandatory.");
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** ANULADA = AN */
    public static final String XX_ORDERSTATUS_ANULADA = X_Ref_XX_OrderStatus.ANULADA.getValue();
    /** APROBADA = AP */
    public static final String XX_ORDERSTATUS_APROBADA = X_Ref_XX_OrderStatus.APROBADA.getValue();
    /** CHEQUEADA = CH */
    public static final String XX_ORDERSTATUS_CHEQUEADA = X_Ref_XX_OrderStatus.CHEQUEADA.getValue();
    /** EN ADUANA = EA */
    public static final String XX_ORDERSTATUS_ENADUANA = X_Ref_XX_OrderStatus.ENADUANA.getValue();
    /** ENTREGADA AL AGENTE DE CARGA = EAC */
    public static final String XX_ORDERSTATUS_ENTREGADAALAGENTEDECARGA = X_Ref_XX_OrderStatus.ENTREGADAALAGENTEDECARGA.getValue();
    /** EN PRODUCCIÓN = EP */
    public static final String XX_ORDERSTATUS_ENPRODUCCIÓN = X_Ref_XX_OrderStatus.ENPRODUCCIÓN.getValue();
    /** EN PROCESO DE NACIONALIZACIÓN = EPN */
    public static final String XX_ORDERSTATUS_ENPROCESODENACIONALIZACIÓN = X_Ref_XX_OrderStatus.ENPROCESODENACIONALIZACIÓN.getValue();
    /** EN TRÁNSITO INTERNACIONAL = ETI */
    public static final String XX_ORDERSTATUS_ENTRÁNSITOINTERNACIONAL = X_Ref_XX_OrderStatus.ENTRÁNSITOINTERNACIONAL.getValue();
    /** EN TRÁNSITO NACIONAL = ETN */
    public static final String XX_ORDERSTATUS_ENTRÁNSITONACIONAL = X_Ref_XX_OrderStatus.ENTRÁNSITONACIONAL.getValue();
    /** LLEGADA A CD = LCD */
    public static final String XX_ORDERSTATUS_LLEGADAACD = X_Ref_XX_OrderStatus.LLEGADAACD.getValue();
    /** LLEGADA A VENEZUELA = LVE */
    public static final String XX_ORDERSTATUS_LLEGADAAVENEZUELA = X_Ref_XX_OrderStatus.LLEGADAAVENEZUELA.getValue();
    /** PENDIENTE = PEN */
    public static final String XX_ORDERSTATUS_PENDIENTE = X_Ref_XX_OrderStatus.PENDIENTE.getValue();
    /** PROFORMA = PRO */
    public static final String XX_ORDERSTATUS_PROFORMA = X_Ref_XX_OrderStatus.PROFORMA.getValue();
    /** RECIBIDA = RE */
    public static final String XX_ORDERSTATUS_RECIBIDA = X_Ref_XX_OrderStatus.RECIBIDA.getValue();
    /** SITME = SIT */
    public static final String XX_ORDERSTATUS_SITME = X_Ref_XX_OrderStatus.SITME.getValue();
    /** Set PO Status.
    @param XX_OrderStatus Estado de la Orden de Compra */
    public void setXX_OrderStatus (String XX_OrderStatus)
    {
        if (!X_Ref_XX_OrderStatus.isValid(XX_OrderStatus))
        throw new IllegalArgumentException ("XX_OrderStatus Invalid value - " + XX_OrderStatus + " - Reference_ID=1000103 - AN - AP - CH - EA - EAC - EP - EPN - ETI - ETN - LCD - LVE - PEN - PRO - RE - SIT");
        throw new IllegalArgumentException ("XX_OrderStatus is virtual column");
        
    }
    
    /** Get PO Status.
    @return Estado de la Orden de Compra */
    public String getXX_OrderStatus() 
    {
        return (String)get_Value("XX_OrderStatus");
        
    }
    
    /** Set File Number.
    @param XX_VLO_BoardingGuide_ID File Number */
    public void setXX_VLO_BoardingGuide_ID (int XX_VLO_BoardingGuide_ID)
    {
        if (XX_VLO_BoardingGuide_ID < 1) throw new IllegalArgumentException ("XX_VLO_BoardingGuide_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_BoardingGuide_ID", Integer.valueOf(XX_VLO_BoardingGuide_ID));
        
    }
    
    /** Get File Number.
    @return File Number */
    public int getXX_VLO_BoardingGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_BoardingGuide_ID");
        
    }
    
    /** Set XX_VLO_DETAILGUIDE_ID.
    @param XX_VLO_DETAILGUIDE_ID XX_VLO_DETAILGUIDE_ID */
    public void setXX_VLO_DETAILGUIDE_ID (int XX_VLO_DETAILGUIDE_ID)
    {
        if (XX_VLO_DETAILGUIDE_ID < 1) throw new IllegalArgumentException ("XX_VLO_DETAILGUIDE_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_DETAILGUIDE_ID", Integer.valueOf(XX_VLO_DETAILGUIDE_ID));
        
    }
    
    /** Get XX_VLO_DETAILGUIDE_ID.
    @return XX_VLO_DETAILGUIDE_ID */
    public int getXX_VLO_DETAILGUIDE_ID() 
    {
        return get_ValueAsInt("XX_VLO_DETAILGUIDE_ID");
        
    }
    
    
}
