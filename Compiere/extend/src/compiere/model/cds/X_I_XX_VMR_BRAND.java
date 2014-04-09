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
/** Generated Model for I_XX_VMR_BRAND
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_BRAND extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_BRAND_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_BRAND (Ctx ctx, int I_XX_VMR_BRAND_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_BRAND_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_BRAND_ID == 0)
        {
            setI_XX_VMR_BRAND_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_BRAND (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27565921212789L;
    /** Last Updated Timestamp 2010-09-06 13:28:16.0 */
    public static final long updatedMS = 1283795896000L;
    /** AD_Table_ID=1000181 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_BRAND");
        
    }
    ;
    
    /** TableName=I_XX_VMR_BRAND */
    public static final String Table_Name="I_XX_VMR_BRAND";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set I_XX_VMR_BRAND_ID.
    @param I_XX_VMR_BRAND_ID I_XX_VMR_BRAND_ID */
    public void setI_XX_VMR_BRAND_ID (int I_XX_VMR_BRAND_ID)
    {
        if (I_XX_VMR_BRAND_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_BRAND_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_BRAND_ID", Integer.valueOf(I_XX_VMR_BRAND_ID));
        
    }
    
    /** Get I_XX_VMR_BRAND_ID.
    @return I_XX_VMR_BRAND_ID */
    public int getI_XX_VMR_BRAND_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_BRAND_ID");
        
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
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
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
    
    /** Set XX_ONWBRAND_VALUE.
    @param XX_ONWBRAND_VALUE XX_ONWBRAND_VALUE */
    public void setXX_ONWBRAND_VALUE (boolean XX_ONWBRAND_VALUE)
    {
        set_Value ("XX_ONWBRAND_VALUE", Boolean.valueOf(XX_ONWBRAND_VALUE));
        
    }
    
    /** Get XX_ONWBRAND_VALUE.
    @return XX_ONWBRAND_VALUE */
    public boolean isXX_ONWBRAND_VALUE() 
    {
        return get_ValueAsBoolean("XX_ONWBRAND_VALUE");
        
    }
    
    
}
