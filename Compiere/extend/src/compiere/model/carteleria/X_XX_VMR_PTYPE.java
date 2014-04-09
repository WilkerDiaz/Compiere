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
/** Generated Model for XX_VMR_PTYPE
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PTYPE extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PTYPE_ID id
    @param trx transaction
    */
    public X_XX_VMR_PTYPE (Ctx ctx, int XX_VMR_PTYPE_ID, Trx trx)
    {
        super (ctx, XX_VMR_PTYPE_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PTYPE_ID == 0)
        {
            setXX_VMR_PTYPE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PTYPE (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27596160320789L;
    /** Last Updated Timestamp 2011-08-22 13:13:24.0 */
    public static final long updatedMS = 1314035004000L;
    /** AD_Table_ID=1000953 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PTYPE");
        
    }
    ;
    
    /** TableName=XX_VMR_PTYPE */
    public static final String Table_Name="XX_VMR_PTYPE";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set highPoster.
    @param highPoster high Poster */
    public void sethighPoster (java.math.BigDecimal highPoster)
    {
        set_Value ("highPoster", highPoster);
        
    }
    
    /** Get highPoster.
    @return high Poster */
    public java.math.BigDecimal gethighPoster() 
    {
        return get_ValueAsBigDecimal("highPoster");
        
    }
    
    /** Set imageAux.
    @param imageAux Auxiliary poster image for help */
    public void setimageAux (int imageAux)
    {
        set_Value ("imageAux", Integer.valueOf(imageAux));
        
    }
    
    /** Get imageAux.
    @return Auxiliary poster image for help */
    public int getimageAux() 
    {
        return get_ValueAsInt("imageAux");
        
    }
    
    /** Set Image attached.
    @param ImageIsAttached The image to be printed is attached to the record */
    public void setImageIsAttached (int ImageIsAttached)
    {
        set_Value ("ImageIsAttached", Integer.valueOf(ImageIsAttached));
        
    }
    
    /** Get Image attached.
    @return The image to be printed is attached to the record */
    public int getImageIsAttached() 
    {
        return get_ValueAsInt("ImageIsAttached");
        
    }
    
    /** Set maxAmount.
    @param maxAmount maximum amount of products on the poster */
    public void setmaxAmount (java.math.BigDecimal maxAmount)
    {
        set_Value ("maxAmount", maxAmount);
        
    }
    
    /** Get maxAmount.
    @return maximum amount of products on the poster */
    public java.math.BigDecimal getmaxAmount() 
    {
        return get_ValueAsBigDecimal("maxAmount");
        
    }
    
    /** Set minAmount.
    @param minAmount min amount the product */
    public void setminAmount (java.math.BigDecimal minAmount)
    {
        set_Value ("minAmount", minAmount);
        
    }
    
    /** Get minAmount.
    @return min amount the product */
    public java.math.BigDecimal getminAmount() 
    {
        return get_ValueAsBigDecimal("minAmount");
        
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
    
    /** Set widthPoster.
    @param widthPoster widthPoster */
    public void setwidthPoster (java.math.BigDecimal widthPoster)
    {
        set_Value ("widthPoster", widthPoster);
        
    }
    
    /** Get widthPoster.
    @return widthPoster */
    public java.math.BigDecimal getwidthPoster() 
    {
        return get_ValueAsBigDecimal("widthPoster");
        
    }
    
    /** Set XX_VMR_PTYPE_ID.
    @param XX_VMR_PTYPE_ID XX_VMR_PTYPE_ID */
    public void setXX_VMR_PTYPE_ID (int XX_VMR_PTYPE_ID)
    {
        if (XX_VMR_PTYPE_ID < 1) throw new IllegalArgumentException ("XX_VMR_PTYPE_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PTYPE_ID", Integer.valueOf(XX_VMR_PTYPE_ID));
        
    }
    
    /** Get XX_VMR_PTYPE_ID.
    @return XX_VMR_PTYPE_ID */
    public int getXX_VMR_PTYPE_ID() 
    {
        return get_ValueAsInt("XX_VMR_PTYPE_ID");
        
    }
    
    
}
