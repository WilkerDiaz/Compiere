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
/** Generated Model for XX_VMR_UnitConversion
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_UnitConversion extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_UnitConversion_ID id
    @param trx transaction
    */
    public X_XX_VMR_UnitConversion (Ctx ctx, int XX_VMR_UnitConversion_ID, Trx trx)
    {
        super (ctx, XX_VMR_UnitConversion_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_UnitConversion_ID == 0)
        {
            setXX_UnitConversion (0);	// 1
            setXX_VMR_UnitConversion_ID (0);
            setXX_VMR_UnitPurchase_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_UnitConversion (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27546908876789L;
    /** Last Updated Timestamp 2010-01-29 12:16:00.0 */
    public static final long updatedMS = 1264783560000L;
    /** AD_Table_ID=1000132 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_UnitConversion");
        
    }
    ;
    
    /** TableName=XX_VMR_UnitConversion */
    public static final String Table_Name="XX_VMR_UnitConversion";
    
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
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_UnitConversion.
    @param XX_UnitConversion XX_UnitConversion */
    public void setXX_UnitConversion (int XX_UnitConversion)
    {
        set_Value ("XX_UnitConversion", Integer.valueOf(XX_UnitConversion));
        
    }
    
    /** Get XX_UnitConversion.
    @return XX_UnitConversion */
    public int getXX_UnitConversion() 
    {
        return get_ValueAsInt("XX_UnitConversion");
        
    }
    
    /** Set Unit Conversion.
    @param XX_VMR_UnitConversion_ID Unit Conversion */
    public void setXX_VMR_UnitConversion_ID (int XX_VMR_UnitConversion_ID)
    {
        if (XX_VMR_UnitConversion_ID < 1) throw new IllegalArgumentException ("XX_VMR_UnitConversion_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_UnitConversion_ID", Integer.valueOf(XX_VMR_UnitConversion_ID));
        
    }
    
    /** Get Unit Conversion.
    @return Unit Conversion */
    public int getXX_VMR_UnitConversion_ID() 
    {
        return get_ValueAsInt("XX_VMR_UnitConversion_ID");
        
    }
    
    /** Set Purchase Unit.
    @param XX_VMR_UnitPurchase_ID Purchase Unit */
    public void setXX_VMR_UnitPurchase_ID (int XX_VMR_UnitPurchase_ID)
    {
        if (XX_VMR_UnitPurchase_ID < 1) throw new IllegalArgumentException ("XX_VMR_UnitPurchase_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_UnitPurchase_ID", Integer.valueOf(XX_VMR_UnitPurchase_ID));
        
    }
    
    /** Get Purchase Unit.
    @return Purchase Unit */
    public int getXX_VMR_UnitPurchase_ID() 
    {
        return get_ValueAsInt("XX_VMR_UnitPurchase_ID");
        
    }
    
    
}
