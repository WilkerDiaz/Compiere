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
/** Generated Model for XX_VMR_Brand
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Brand extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Brand_ID id
    @param trx transaction
    */
    public X_XX_VMR_Brand (Ctx ctx, int XX_VMR_Brand_ID, Trx trx)
    {
        super (ctx, XX_VMR_Brand_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Brand_ID == 0)
        {
            setDescription (null);
            setName (null);
            setXX_BrandOwn (false);
            setXX_VMR_Brand_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Brand (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27583705788789L;
    /** Last Updated Timestamp 2011-03-31 09:37:52.0 */
    public static final long updatedMS = 1301580472000L;
    /** AD_Table_ID=1000114 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Brand");
        
    }
    ;
    
    /** TableName=XX_VMR_Brand */
    public static final String Table_Name="XX_VMR_Brand";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Comments.
    @param Comments Comments or additional information */
    public void setComments (String Comments)
    {
        set_Value ("Comments", Comments);
        
    }
    
    /** Get Comments.
    @return Comments or additional information */
    public String getComments() 
    {
        return (String)get_Value("Comments");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        if (Description == null) throw new IllegalArgumentException ("Description is mandatory.");
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
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
    
    /** Set Own Brand.
    @param XX_BrandOwn Si es marca propia */
    public void setXX_BrandOwn (boolean XX_BrandOwn)
    {
        set_Value ("XX_BrandOwn", Boolean.valueOf(XX_BrandOwn));
        
    }
    
    /** Get Own Brand.
    @return Si es marca propia */
    public boolean isXX_BrandOwn() 
    {
        return get_ValueAsBoolean("XX_BrandOwn");
        
    }
    
    /** Set XX_OLDVALUE.
    @param XX_OLDVALUE XX_OLDVALUE */
    public void setXX_OLDVALUE (String XX_OLDVALUE)
    {
        set_Value ("XX_OLDVALUE", XX_OLDVALUE);
        
    }
    
    /** Get XX_OLDVALUE.
    @return XX_OLDVALUE */
    public String getXX_OLDVALUE() 
    {
        return (String)get_Value("XX_OLDVALUE");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID < 1) throw new IllegalArgumentException ("XX_VMR_Brand_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    
}
