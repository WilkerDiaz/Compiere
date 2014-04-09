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
package compiere.model.suppliesservices;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_POLimits
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_POLimits extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_POLimits_ID id
    @param trx transaction
    */
    public X_XX_POLimits (Ctx ctx, int XX_POLimits_ID, Trx trx)
    {
        super (ctx, XX_POLimits_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_POLimits_ID == 0)
        {
            setValue (null);
            setXX_Management_ID (0);
            setXX_POLIMITS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_POLimits (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27621077463789L;
    /** Last Updated Timestamp 2012-06-05 22:39:07.0 */
    public static final long updatedMS = 1338952147000L;
    /** AD_Table_ID=1002457 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_POLimits");
        
    }
    ;
    
    /** TableName=XX_POLimits */
    public static final String Table_Name="XX_POLimits";
    
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
    
    /** Set Management.
    @param XX_Management_ID Management */
    public void setXX_Management_ID (int XX_Management_ID)
    {
        if (XX_Management_ID < 1) throw new IllegalArgumentException ("XX_Management_ID is mandatory.");
        set_Value ("XX_Management_ID", Integer.valueOf(XX_Management_ID));
        
    }
    
    /** Get Management.
    @return Management */
    public int getXX_Management_ID() 
    {
        return get_ValueAsInt("XX_Management_ID");
        
    }
    
    /** Set XX_POLIMITS_ID.
    @param XX_POLIMITS_ID XX_POLIMITS_ID */
    public void setXX_POLIMITS_ID (int XX_POLIMITS_ID)
    {
        if (XX_POLIMITS_ID < 1) throw new IllegalArgumentException ("XX_POLIMITS_ID is mandatory.");
        set_ValueNoCheck ("XX_POLIMITS_ID", Integer.valueOf(XX_POLIMITS_ID));
        
    }
    
    /** Get XX_POLIMITS_ID.
    @return XX_POLIMITS_ID */
    public int getXX_POLIMITS_ID() 
    {
        return get_ValueAsInt("XX_POLIMITS_ID");
        
    }
    
    /** Set Responsable (Role).
    @param XX_Responsable_ID Responsable (Role) */
    public void setXX_Responsable_ID (int XX_Responsable_ID)
    {
        if (XX_Responsable_ID <= 0) set_Value ("XX_Responsable_ID", null);
        else
        set_Value ("XX_Responsable_ID", Integer.valueOf(XX_Responsable_ID));
        
    }
    
    /** Get Responsable (Role).
    @return Responsable (Role) */
    public int getXX_Responsable_ID() 
    {
        return get_ValueAsInt("XX_Responsable_ID");
        
    }
    
    /** Set Limit (UT).
    @param XX_UTLimit Limit (UT) */
    public void setXX_UTLimit (java.math.BigDecimal XX_UTLimit)
    {
        set_Value ("XX_UTLimit", XX_UTLimit);
        
    }
    
    /** Get Limit (UT).
    @return Limit (UT) */
    public java.math.BigDecimal getXX_UTLimit() 
    {
        return get_ValueAsBigDecimal("XX_UTLimit");
        
    }
    
    
}
