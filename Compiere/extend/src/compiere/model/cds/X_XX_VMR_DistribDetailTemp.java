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
/** Generated Model for XX_VMR_DistribDetailTemp
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DistribDetailTemp extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DistribDetailTemp_ID id
    @param trx transaction
    */
    public X_XX_VMR_DistribDetailTemp (Ctx ctx, int XX_VMR_DistribDetailTemp_ID, Trx trx)
    {
        super (ctx, XX_VMR_DistribDetailTemp_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DistribDetailTemp_ID == 0)
        {
            setC_BPartner_ID (0);
            setM_Product_ID (0);
            setXX_DesiredQuantity (Env.ZERO);
            setXX_VMR_Brand_ID (0);
            setXX_VMR_Department_ID (0);
            setXX_VMR_DISTRIBDETAILTEMP_ID (0);
            setXX_VMR_DistributionDetail_ID (0);
            setXX_VMR_Line_ID (0);
            setXX_VMR_Section_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_DistribDetailTemp (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27636844242789L;
    /** Last Updated Timestamp 2012-12-05 10:18:46.0 */
    public static final long updatedMS = 1354718926000L;
    /** AD_Table_ID=1000161 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DistribDetailTemp");
        
    }
    ;
    
    /** TableName=XX_VMR_DistribDetailTemp */
    public static final String Table_Name="XX_VMR_DistribDetailTemp";
    
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
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
    
    /** Set Consecutive Price.
    @param XX_ConsecutivePrice Consecutivo de Precio */
    public void setXX_ConsecutivePrice (java.math.BigDecimal XX_ConsecutivePrice)
    {
        set_Value ("XX_ConsecutivePrice", XX_ConsecutivePrice);
        
    }
    
    /** Get Consecutive Price.
    @return Consecutivo de Precio */
    public java.math.BigDecimal getXX_ConsecutivePrice() 
    {
        return get_ValueAsBigDecimal("XX_ConsecutivePrice");
        
    }
    
    /** Set XX_DesiredQuantity.
    @param XX_DesiredQuantity XX_DesiredQuantity */
    public void setXX_DesiredQuantity (java.math.BigDecimal XX_DesiredQuantity)
    {
        if (XX_DesiredQuantity == null) throw new IllegalArgumentException ("XX_DesiredQuantity is mandatory.");
        set_Value ("XX_DesiredQuantity", XX_DesiredQuantity);
        
    }
    
    /** Get XX_DesiredQuantity.
    @return XX_DesiredQuantity */
    public java.math.BigDecimal getXX_DesiredQuantity() 
    {
        return get_ValueAsBigDecimal("XX_DesiredQuantity");
        
    }
    
    /** Set XX_REGISTERSTATUS.
    @param XX_REGISTERSTATUS XX_REGISTERSTATUS */
    public void setXX_REGISTERSTATUS (java.math.BigDecimal XX_REGISTERSTATUS)
    {
        set_Value ("XX_REGISTERSTATUS", XX_REGISTERSTATUS);
        
    }
    
    /** Get XX_REGISTERSTATUS.
    @return XX_REGISTERSTATUS */
    public java.math.BigDecimal getXX_REGISTERSTATUS() 
    {
        return get_ValueAsBigDecimal("XX_REGISTERSTATUS");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID < 1) throw new IllegalArgumentException ("XX_VMR_Brand_ID is mandatory.");
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
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
    
    /** Set XX_VMR_DISTRIBDETAILTEMP_ID.
    @param XX_VMR_DISTRIBDETAILTEMP_ID XX_VMR_DISTRIBDETAILTEMP_ID */
    public void setXX_VMR_DISTRIBDETAILTEMP_ID (int XX_VMR_DISTRIBDETAILTEMP_ID)
    {
        if (XX_VMR_DISTRIBDETAILTEMP_ID < 1) throw new IllegalArgumentException ("XX_VMR_DISTRIBDETAILTEMP_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DISTRIBDETAILTEMP_ID", Integer.valueOf(XX_VMR_DISTRIBDETAILTEMP_ID));
        
    }
    
    /** Get XX_VMR_DISTRIBDETAILTEMP_ID.
    @return XX_VMR_DISTRIBDETAILTEMP_ID */
    public int getXX_VMR_DISTRIBDETAILTEMP_ID() 
    {
        return get_ValueAsInt("XX_VMR_DISTRIBDETAILTEMP_ID");
        
    }
    
    /** Set XX_VMR_DistributionDetail_ID.
    @param XX_VMR_DistributionDetail_ID Distribution Detail */
    public void setXX_VMR_DistributionDetail_ID (int XX_VMR_DistributionDetail_ID)
    {
        if (XX_VMR_DistributionDetail_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistributionDetail_ID", Integer.valueOf(XX_VMR_DistributionDetail_ID));
        
    }
    
    /** Get XX_VMR_DistributionDetail_ID.
    @return Distribution Detail */
    public int getXX_VMR_DistributionDetail_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionDetail_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID < 1) throw new IllegalArgumentException ("XX_VMR_Line_ID is mandatory.");
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID < 1) throw new IllegalArgumentException ("XX_VMR_Section_ID is mandatory.");
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    
}
