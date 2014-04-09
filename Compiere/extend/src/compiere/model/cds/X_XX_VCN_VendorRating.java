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
/** Generated Model for XX_VCN_VendorRating
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_VendorRating extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_VendorRating_ID id
    @param trx transaction
    */
    public X_XX_VCN_VendorRating (Ctx ctx, int XX_VCN_VendorRating_ID, Trx trx)
    {
        super (ctx, XX_VCN_VendorRating_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_VendorRating_ID == 0)
        {
            setC_BPartner_ID (0);
            setXX_VCN_VENDORRATING_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_VendorRating (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566087442789L;
    /** Last Updated Timestamp 2010-09-08 11:38:46.0 */
    public static final long updatedMS = 1283962126000L;
    /** AD_Table_ID=1000129 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_VendorRating");
        
    }
    ;
    
    /** TableName=XX_VCN_VendorRating */
    public static final String Table_Name="XX_VCN_VendorRating";
    
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
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Fecha de Llegada.
    @param XX_ArrivalDate Fecha de Llegada */
    public void setXX_ArrivalDate (Timestamp XX_ArrivalDate)
    {
        throw new IllegalArgumentException ("XX_ArrivalDate is virtual column");
        
    }
    
    /** Get Fecha de Llegada.
    @return Fecha de Llegada */
    public Timestamp getXX_ArrivalDate() 
    {
        return (Timestamp)get_Value("XX_ArrivalDate");
        
    }
    
    /** Set Purchase Order.
    @param XX_C_Order_ID Orden de Compra */
    public void setXX_C_Order_ID (int XX_C_Order_ID)
    {
        if (XX_C_Order_ID <= 0) set_ValueNoCheck ("XX_C_Order_ID", null);
        else
        set_ValueNoCheck ("XX_C_Order_ID", Integer.valueOf(XX_C_Order_ID));
        
    }
    
    /** Get Purchase Order.
    @return Orden de Compra */
    public int getXX_C_Order_ID() 
    {
        return get_ValueAsInt("XX_C_Order_ID");
        
    }
    
    /** Set Points.
    @param XX_Points Puntos del Criterio  */
    public void setXX_Points (java.math.BigDecimal XX_Points)
    {
        set_ValueNoCheck ("XX_Points", XX_Points);
        
    }
    
    /** Get Points.
    @return Puntos del Criterio  */
    public java.math.BigDecimal getXX_Points() 
    {
        return get_ValueAsBigDecimal("XX_Points");
        
    }
    
    /** Set Evaluation Criteria ID.
    @param XX_VCN_EvaluationCriteria_ID ID de la Tabla Criterio de Evaluación */
    public void setXX_VCN_EvaluationCriteria_ID (int XX_VCN_EvaluationCriteria_ID)
    {
        if (XX_VCN_EvaluationCriteria_ID <= 0) set_ValueNoCheck ("XX_VCN_EvaluationCriteria_ID", null);
        else
        set_ValueNoCheck ("XX_VCN_EvaluationCriteria_ID", Integer.valueOf(XX_VCN_EvaluationCriteria_ID));
        
    }
    
    /** Get Evaluation Criteria ID.
    @return ID de la Tabla Criterio de Evaluación */
    public int getXX_VCN_EvaluationCriteria_ID() 
    {
        return get_ValueAsInt("XX_VCN_EvaluationCriteria_ID");
        
    }
    
    /** Set Vendor Rating ID.
    @param XX_VCN_VENDORRATING_ID ID de la Tabla Puntuación del Proveedor (XX_VCN_VendorRating) */
    public void setXX_VCN_VENDORRATING_ID (int XX_VCN_VENDORRATING_ID)
    {
        if (XX_VCN_VENDORRATING_ID < 1) throw new IllegalArgumentException ("XX_VCN_VENDORRATING_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_VENDORRATING_ID", Integer.valueOf(XX_VCN_VENDORRATING_ID));
        
    }
    
    /** Get Vendor Rating ID.
    @return ID de la Tabla Puntuación del Proveedor (XX_VCN_VendorRating) */
    public int getXX_VCN_VENDORRATING_ID() 
    {
        return get_ValueAsInt("XX_VCN_VENDORRATING_ID");
        
    }
    
    
}
