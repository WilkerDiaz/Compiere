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
package compiere.model.carteleria;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_PRODUCTPOSTER
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PRODUCTPOSTER extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PRODUCTPOSTER_ID id
    @param trx transaction
    */
    public X_XX_VMR_PRODUCTPOSTER (Ctx ctx, int XX_VMR_PRODUCTPOSTER_ID, Trx trx)
    {
        super (ctx, XX_VMR_PRODUCTPOSTER_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PRODUCTPOSTER_ID == 0)
        {
            setValue (null);
            setXX_VMR_PRODUCTPOSTER_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PRODUCTPOSTER (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27620005379789L;
    /** Last Updated Timestamp 2012-05-24 12:51:03.0 */
    public static final long updatedMS = 1337880063000L;
    /** AD_Table_ID=1001955 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PRODUCTPOSTER");
        
    }
    ;
    
    /** TableName=XX_VMR_PRODUCTPOSTER */
    public static final String Table_Name="XX_VMR_PRODUCTPOSTER";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set charactPrd.
    @param charactPrd charactPrd */
    public void setcharactPrd (String charactPrd)
    {
        set_Value ("charactPrd", charactPrd);
        
    }
    
    /** Get charactPrd.
    @return charactPrd */
    public String getcharactPrd() 
    {
        return (String)get_Value("charactPrd");
        
    }
    
    /** Set codProm.
    @param codProm codProm */
    public void setcodProm (int codProm)
    {
        set_Value ("codProm", Integer.valueOf(codProm));
        
    }
    
    /** Get codProm.
    @return codProm */
    public int getcodProm() 
    {
        return get_ValueAsInt("codProm");
        
    }
    
    /** Set DescProd.
    @param DescProd DescProd */
    public void setDescProd (String DescProd)
    {
        set_Value ("DescProd", DescProd);
        
    }
    
    /** Get DescProd.
    @return DescProd */
    public String getDescProd() 
    {
        return (String)get_Value("DescProd");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set PriorityProm.
    @param PriorityProm PriorityProm */
    public void setPriorityProm (int PriorityProm)
    {
        set_Value ("PriorityProm", Integer.valueOf(PriorityProm));
        
    }
    
    /** Get PriorityProm.
    @return PriorityProm */
    public int getPriorityProm() 
    {
        return get_ValueAsInt("PriorityProm");
        
    }
    
    /** Set showDisc.
    @param showDisc showDisc */
    public void setshowDisc (boolean showDisc)
    {
        set_Value ("showDisc", Boolean.valueOf(showDisc));
        
    }
    
    /** Get showDisc.
    @return showDisc */
    public boolean isshowDisc() 
    {
        return get_ValueAsBoolean("showDisc");
        
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
    
    /** Set XX_VMR_POSTER_ID.
    @param XX_VMR_POSTER_ID XX_VMR_POSTER_ID */
    public void setXX_VMR_POSTER_ID (int XX_VMR_POSTER_ID)
    {
        if (XX_VMR_POSTER_ID <= 0) set_Value ("XX_VMR_POSTER_ID", null);
        else
        set_Value ("XX_VMR_POSTER_ID", Integer.valueOf(XX_VMR_POSTER_ID));
        
    }
    
    /** Get XX_VMR_POSTER_ID.
    @return XX_VMR_POSTER_ID */
    public int getXX_VMR_POSTER_ID() 
    {
        return get_ValueAsInt("XX_VMR_POSTER_ID");
        
    }
    
    /** Set XX_VMR_PRODUCTPOSTER_ID.
    @param XX_VMR_PRODUCTPOSTER_ID XX_VMR_PRODUCTPOSTER_ID */
    public void setXX_VMR_PRODUCTPOSTER_ID (int XX_VMR_PRODUCTPOSTER_ID)
    {
        if (XX_VMR_PRODUCTPOSTER_ID < 1) throw new IllegalArgumentException ("XX_VMR_PRODUCTPOSTER_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PRODUCTPOSTER_ID", Integer.valueOf(XX_VMR_PRODUCTPOSTER_ID));
        
    }
    
    /** Get XX_VMR_PRODUCTPOSTER_ID.
    @return XX_VMR_PRODUCTPOSTER_ID */
    public int getXX_VMR_PRODUCTPOSTER_ID() 
    {
        return get_ValueAsInt("XX_VMR_PRODUCTPOSTER_ID");
        
    }
    
    
}
