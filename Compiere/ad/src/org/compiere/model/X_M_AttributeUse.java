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
/** Generated Model for M_AttributeUse
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_AttributeUse.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_AttributeUse extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_AttributeUse_ID id
    @param trx transaction
    */
    public X_M_AttributeUse (Ctx ctx, int M_AttributeUse_ID, Trx trx)
    {
        super (ctx, M_AttributeUse_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_AttributeUse_ID == 0)
        {
            setM_AttributeSet_ID (0);
            setM_Attribute_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_AttributeUse WHERE M_AttributeSet_ID=@M_AttributeSet_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_AttributeUse (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=563 */
    public static final int Table_ID=563;
    
    /** TableName=M_AttributeUse */
    public static final String Table_Name="M_AttributeUse";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Attribute Set.
    @param M_AttributeSet_ID Product Attribute Set */
    public void setM_AttributeSet_ID (int M_AttributeSet_ID)
    {
        if (M_AttributeSet_ID < 0) throw new IllegalArgumentException ("M_AttributeSet_ID is mandatory.");
        set_ValueNoCheck ("M_AttributeSet_ID", Integer.valueOf(M_AttributeSet_ID));
        
    }
    
    /** Get Attribute Set.
    @return Product Attribute Set */
    public int getM_AttributeSet_ID() 
    {
        return get_ValueAsInt("M_AttributeSet_ID");
        
    }
    
    /** Set Attribute.
    @param M_Attribute_ID Product Attribute */
    public void setM_Attribute_ID (int M_Attribute_ID)
    {
        if (M_Attribute_ID < 1) throw new IllegalArgumentException ("M_Attribute_ID is mandatory.");
        set_ValueNoCheck ("M_Attribute_ID", Integer.valueOf(M_Attribute_ID));
        
    }
    
    /** Get Attribute.
    @return Product Attribute */
    public int getM_Attribute_ID() 
    {
        return get_ValueAsInt("M_Attribute_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Attribute_ID()));
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    
    public String getValue() 
    {
        return get_ValueAsString("Value");
        
    }
    
}
