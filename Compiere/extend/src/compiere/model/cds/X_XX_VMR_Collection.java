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
/** Generated Model for XX_VMR_Collection
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Collection extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Collection_ID id
    @param trx transaction
    */
    public X_XX_VMR_Collection (Ctx ctx, int XX_VMR_Collection_ID, Trx trx)
    {
        super (ctx, XX_VMR_Collection_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Collection_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VMR_Collection_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Collection (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27603572417789L;
    /** Last Updated Timestamp 2011-11-16 08:08:21.0 */
    public static final long updatedMS = 1321447101000L;
    /** AD_Table_ID=1000036 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Collection");
        
    }
    ;
    
    /** TableName=XX_VMR_Collection */
    public static final String Table_Name="XX_VMR_Collection";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set TimeFrame (in days).
    @param TimeFrame The timeframe dictates the number of days after shipment that the goods can be returned. */
    public void setTimeFrame (String TimeFrame)
    {
        throw new IllegalArgumentException ("TimeFrame is virtual column");
        
    }
    
    /** Get TimeFrame (in days).
    @return The timeframe dictates the number of days after shipment that the goods can be returned. */
    public String getTimeFrame() 
    {
        return (String)get_Value("TimeFrame");
        
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
    
    /** Set Ending Day.
    @param XX_EndingDay Ending Day */
    public void setXX_EndingDay (int XX_EndingDay)
    {
        set_Value ("XX_EndingDay", Integer.valueOf(XX_EndingDay));
        
    }
    
    /** Get Ending Day.
    @return Ending Day */
    public int getXX_EndingDay() 
    {
        return get_ValueAsInt("XX_EndingDay");
        
    }
    
    /** Set Ending Month.
    @param XX_EndingMonth Ending Month */
    public void setXX_EndingMonth (int XX_EndingMonth)
    {
        set_Value ("XX_EndingMonth", Integer.valueOf(XX_EndingMonth));
        
    }
    
    /** Get Ending Month.
    @return Ending Month */
    public int getXX_EndingMonth() 
    {
        return get_ValueAsInt("XX_EndingMonth");
        
    }
    
    /** Set Period.
    @param XX_Period Period */
    public void setXX_Period (String XX_Period)
    {
        set_Value ("XX_Period", XX_Period);
        
    }
    
    /** Get Period.
    @return Period */
    public String getXX_Period() 
    {
        return (String)get_Value("XX_Period");
        
    }
    
    /** Set Starting Day.
    @param XX_StartingDay Starting Day */
    public void setXX_StartingDay (int XX_StartingDay)
    {
        set_Value ("XX_StartingDay", Integer.valueOf(XX_StartingDay));
        
    }
    
    /** Get Starting Day.
    @return Starting Day */
    public int getXX_StartingDay() 
    {
        return get_ValueAsInt("XX_StartingDay");
        
    }
    
    /** Set Starting Month.
    @param XX_StartingMonth Starting Month */
    public void setXX_StartingMonth (int XX_StartingMonth)
    {
        set_Value ("XX_StartingMonth", Integer.valueOf(XX_StartingMonth));
        
    }
    
    /** Get Starting Month.
    @return Starting Month */
    public int getXX_StartingMonth() 
    {
        return get_ValueAsInt("XX_StartingMonth");
        
    }
    
    /** Set Collection.
    @param XX_VMR_Collection_ID ID de Colección */
    public void setXX_VMR_Collection_ID (int XX_VMR_Collection_ID)
    {
        if (XX_VMR_Collection_ID < 1) throw new IllegalArgumentException ("XX_VMR_Collection_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Collection_ID", Integer.valueOf(XX_VMR_Collection_ID));
        
    }
    
    /** Get Collection.
    @return ID de Colección */
    public int getXX_VMR_Collection_ID() 
    {
        return get_ValueAsInt("XX_VMR_Collection_ID");
        
    }
    
    /** Set Season.
    @param XX_VMR_Season_ID Season */
    public void setXX_VMR_Season_ID (int XX_VMR_Season_ID)
    {
        if (XX_VMR_Season_ID <= 0) set_ValueNoCheck ("XX_VMR_Season_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_Season_ID", Integer.valueOf(XX_VMR_Season_ID));
        
    }
    
    /** Get Season.
    @return Season */
    public int getXX_VMR_Season_ID() 
    {
        return get_ValueAsInt("XX_VMR_Season_ID");
        
    }
    
    
}
