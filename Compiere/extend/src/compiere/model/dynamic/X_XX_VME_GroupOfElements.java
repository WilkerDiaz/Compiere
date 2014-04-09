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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VME_GroupOfElements
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_GroupOfElements extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_GroupOfElements_ID id
    @param trx transaction
    */
    public X_XX_VME_GroupOfElements (Ctx ctx, int XX_VME_GroupOfElements_ID, Trx trx)
    {
        super (ctx, XX_VME_GroupOfElements_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_GroupOfElements_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VME_GroupOfElements_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_GroupOfElements (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27612743912789L;
    /** Last Updated Timestamp 2012-03-01 11:46:36.0 */
    public static final long updatedMS = 1330618596000L;
    /** AD_Table_ID=1000954 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_GroupOfElements");
        
    }
    ;
    
    /** TableName=XX_VME_GroupOfElements */
    public static final String Table_Name="XX_VME_GroupOfElements";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
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
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Brochure.
    @param XX_VMA_Brochure_ID Identifier of the Brochure. */
    public void setXX_VMA_Brochure_ID (int XX_VMA_Brochure_ID)
    {
        if (XX_VMA_Brochure_ID <= 0) set_Value ("XX_VMA_Brochure_ID", null);
        else
        set_Value ("XX_VMA_Brochure_ID", Integer.valueOf(XX_VMA_Brochure_ID));
        
    }
    
    /** Get Brochure.
    @return Identifier of the Brochure. */
    public int getXX_VMA_Brochure_ID() 
    {
        return get_ValueAsInt("XX_VMA_Brochure_ID");
        
    }
    
    /** Set Group Of Elements.
    @param XX_VME_GroupOfElements_ID Group Of Elements */
    public void setXX_VME_GroupOfElements_ID (int XX_VME_GroupOfElements_ID)
    {
        if (XX_VME_GroupOfElements_ID < 1) throw new IllegalArgumentException ("XX_VME_GroupOfElements_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_GroupOfElements_ID", Integer.valueOf(XX_VME_GroupOfElements_ID));
        
    }
    
    /** Get Group Of Elements.
    @return Group Of Elements */
    public int getXX_VME_GroupOfElements_ID() 
    {
        return get_ValueAsInt("XX_VME_GroupOfElements_ID");
        
    }
    
    
}
