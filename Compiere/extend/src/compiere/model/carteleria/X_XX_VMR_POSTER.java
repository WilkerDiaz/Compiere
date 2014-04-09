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
/** Generated Model for XX_VMR_POSTER
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_POSTER extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_POSTER_ID id
    @param trx transaction
    */
    public X_XX_VMR_POSTER (Ctx ctx, int XX_VMR_POSTER_ID, Trx trx)
    {
        super (ctx, XX_VMR_POSTER_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_POSTER_ID == 0)
        {
            setName (null);
            setXX_VMR_Poster_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_POSTER (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27603172712789L;
    /** Last Updated Timestamp 2011-11-11 17:06:36.0 */
    public static final long updatedMS = 1321047396000L;
    /** AD_Table_ID=1000453 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_POSTER");
        
    }
    ;
    
    /** TableName=XX_VMR_POSTER */
    public static final String Table_Name="XX_VMR_POSTER";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set descPoster.
    @param descPoster descPoster */
    public void setdescPoster (String descPoster)
    {
        set_Value ("descPoster", descPoster);
        
    }
    
    /** Get descPoster.
    @return descPoster */
    public String getdescPoster() 
    {
        return (String)get_Value("descPoster");
        
    }
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set posterLocator.
    @param posterLocator posterLocator */
    public void setposterLocator (int posterLocator)
    {
        set_Value ("posterLocator", Integer.valueOf(posterLocator));
        
    }
    
    /** Get posterLocator.
    @return posterLocator */
    public int getposterLocator() 
    {
        return get_ValueAsInt("posterLocator");
        
    }
    
    /** Set posterWarehouse.
    @param posterWarehouse posterWarehouse */
    public void setposterWarehouse (String posterWarehouse)
    {
        set_Value ("posterWarehouse", posterWarehouse);
        
    }
    
    /** Get posterWarehouse.
    @return posterWarehouse */
    public String getposterWarehouse() 
    {
        return (String)get_Value("posterWarehouse");
        
    }
    
    /** Set XX_VMR_Poster_ID.
    @param XX_VMR_Poster_ID XX_VMR_Poster_ID */
    public void setXX_VMR_Poster_ID (int XX_VMR_Poster_ID)
    {
        if (XX_VMR_Poster_ID < 1) throw new IllegalArgumentException ("XX_VMR_Poster_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Poster_ID", Integer.valueOf(XX_VMR_Poster_ID));
        
    }
    
    /** Get XX_VMR_Poster_ID.
    @return XX_VMR_Poster_ID */
    public int getXX_VMR_Poster_ID() 
    {
        return get_ValueAsInt("XX_VMR_Poster_ID");
        
    }
    
    /** Set XX_VMR_Promotional.
    @param XX_VMR_Promotional XX_VMR_Promotional */
    public void setXX_VMR_Promotional (boolean XX_VMR_Promotional)
    {
        set_Value ("XX_VMR_Promotional", Boolean.valueOf(XX_VMR_Promotional));
        
    }
    
    /** Get XX_VMR_Promotional.
    @return XX_VMR_Promotional */
    public boolean isXX_VMR_Promotional() 
    {
        return get_ValueAsBoolean("XX_VMR_Promotional");
        
    }
    
    /** Set XX_VMR_PTYPE_ID.
    @param XX_VMR_PTYPE_ID XX_VMR_PTYPE_ID */
    public void setXX_VMR_PTYPE_ID (int XX_VMR_PTYPE_ID)
    {
        if (XX_VMR_PTYPE_ID <= 0) set_Value ("XX_VMR_PTYPE_ID", null);
        else
        set_Value ("XX_VMR_PTYPE_ID", Integer.valueOf(XX_VMR_PTYPE_ID));
        
    }
    
    /** Get XX_VMR_PTYPE_ID.
    @return XX_VMR_PTYPE_ID */
    public int getXX_VMR_PTYPE_ID() 
    {
        return get_ValueAsInt("XX_VMR_PTYPE_ID");
        
    }
    
    
}
