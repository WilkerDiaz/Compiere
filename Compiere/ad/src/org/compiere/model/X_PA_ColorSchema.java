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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for PA_ColorSchema
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_ColorSchema.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_ColorSchema extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_ColorSchema_ID id
    @param trx transaction
    */
    public X_PA_ColorSchema (Ctx ctx, int PA_ColorSchema_ID, Trx trx)
    {
        super (ctx, PA_ColorSchema_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_ColorSchema_ID == 0)
        {
            setAD_PrintColor1_ID (0);
            setAD_PrintColor2_ID (0);
            setEntityType (null);	// U
            setMark1Percent (0);
            setMark2Percent (0);
            setName (null);
            setPA_ColorSchema_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_ColorSchema (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=831 */
    public static final int Table_ID=831;
    
    /** TableName=PA_ColorSchema */
    public static final String Table_Name="PA_ColorSchema";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Color 1.
    @param AD_PrintColor1_ID First color used */
    public void setAD_PrintColor1_ID (int AD_PrintColor1_ID)
    {
        if (AD_PrintColor1_ID < 1) throw new IllegalArgumentException ("AD_PrintColor1_ID is mandatory.");
        set_Value ("AD_PrintColor1_ID", Integer.valueOf(AD_PrintColor1_ID));
        
    }
    
    /** Get Color 1.
    @return First color used */
    public int getAD_PrintColor1_ID() 
    {
        return get_ValueAsInt("AD_PrintColor1_ID");
        
    }
    
    /** Set Color 2.
    @param AD_PrintColor2_ID Second color used */
    public void setAD_PrintColor2_ID (int AD_PrintColor2_ID)
    {
        if (AD_PrintColor2_ID < 1) throw new IllegalArgumentException ("AD_PrintColor2_ID is mandatory.");
        set_Value ("AD_PrintColor2_ID", Integer.valueOf(AD_PrintColor2_ID));
        
    }
    
    /** Get Color 2.
    @return Second color used */
    public int getAD_PrintColor2_ID() 
    {
        return get_ValueAsInt("AD_PrintColor2_ID");
        
    }
    
    /** Set Color 3.
    @param AD_PrintColor3_ID Third color used */
    public void setAD_PrintColor3_ID (int AD_PrintColor3_ID)
    {
        if (AD_PrintColor3_ID <= 0) set_Value ("AD_PrintColor3_ID", null);
        else
        set_Value ("AD_PrintColor3_ID", Integer.valueOf(AD_PrintColor3_ID));
        
    }
    
    /** Get Color 3.
    @return Third color used */
    public int getAD_PrintColor3_ID() 
    {
        return get_ValueAsInt("AD_PrintColor3_ID");
        
    }
    
    /** Set Color 4.
    @param AD_PrintColor4_ID Forth color used */
    public void setAD_PrintColor4_ID (int AD_PrintColor4_ID)
    {
        if (AD_PrintColor4_ID <= 0) set_Value ("AD_PrintColor4_ID", null);
        else
        set_Value ("AD_PrintColor4_ID", Integer.valueOf(AD_PrintColor4_ID));
        
    }
    
    /** Get Color 4.
    @return Forth color used */
    public int getAD_PrintColor4_ID() 
    {
        return get_ValueAsInt("AD_PrintColor4_ID");
        
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
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
    }
    
    /** Set Mark 1 Percent.
    @param Mark1Percent Percentage up to this color is used */
    public void setMark1Percent (int Mark1Percent)
    {
        set_Value ("Mark1Percent", Integer.valueOf(Mark1Percent));
        
    }
    
    /** Get Mark 1 Percent.
    @return Percentage up to this color is used */
    public int getMark1Percent() 
    {
        return get_ValueAsInt("Mark1Percent");
        
    }
    
    /** Set Mark 2 Percent.
    @param Mark2Percent Percentage up to this color is used */
    public void setMark2Percent (int Mark2Percent)
    {
        set_Value ("Mark2Percent", Integer.valueOf(Mark2Percent));
        
    }
    
    /** Get Mark 2 Percent.
    @return Percentage up to this color is used */
    public int getMark2Percent() 
    {
        return get_ValueAsInt("Mark2Percent");
        
    }
    
    /** Set Mark 3 Percent.
    @param Mark3Percent Percentage up to this color is used */
    public void setMark3Percent (int Mark3Percent)
    {
        set_Value ("Mark3Percent", Integer.valueOf(Mark3Percent));
        
    }
    
    /** Get Mark 3 Percent.
    @return Percentage up to this color is used */
    public int getMark3Percent() 
    {
        return get_ValueAsInt("Mark3Percent");
        
    }
    
    /** Set Mark 4 Percent.
    @param Mark4Percent Percentage up to this color is used */
    public void setMark4Percent (int Mark4Percent)
    {
        set_Value ("Mark4Percent", Integer.valueOf(Mark4Percent));
        
    }
    
    /** Get Mark 4 Percent.
    @return Percentage up to this color is used */
    public int getMark4Percent() 
    {
        return get_ValueAsInt("Mark4Percent");
        
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
    
    /** Set Color Schema.
    @param PA_ColorSchema_ID Performance Color Schema */
    public void setPA_ColorSchema_ID (int PA_ColorSchema_ID)
    {
        if (PA_ColorSchema_ID < 1) throw new IllegalArgumentException ("PA_ColorSchema_ID is mandatory.");
        set_ValueNoCheck ("PA_ColorSchema_ID", Integer.valueOf(PA_ColorSchema_ID));
        
    }
    
    /** Get Color Schema.
    @return Performance Color Schema */
    public int getPA_ColorSchema_ID() 
    {
        return get_ValueAsInt("PA_ColorSchema_ID");
        
    }
    
    
}
