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
/** Generated Model for AD_IndexColumn
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_IndexColumn.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_IndexColumn extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_IndexColumn_ID id
    @param trx transaction
    */
    public X_AD_IndexColumn (Ctx ctx, int AD_IndexColumn_ID, Trx trx)
    {
        super (ctx, AD_IndexColumn_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_IndexColumn_ID == 0)
        {
            setAD_Column_ID (0);
            setAD_IndexColumn_ID (0);
            setAD_TableIndex_ID (0);
            setEntityType (null);	// U
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_IndexColumn WHERE AD_TableIndex_ID=@AD_TableIndex_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_IndexColumn (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313937789L;
    /** Last Updated Timestamp 2009-03-04 09:43:41.0 */
    public static final long updatedMS = 1236188621000L;
    /** AD_Table_ID=910 */
    public static final int Table_ID=910;
    
    /** TableName=AD_IndexColumn */
    public static final String Table_Name="AD_IndexColumn";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID < 1) throw new IllegalArgumentException ("AD_Column_ID is mandatory.");
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Index Column.
    @param AD_IndexColumn_ID Index Column */
    public void setAD_IndexColumn_ID (int AD_IndexColumn_ID)
    {
        if (AD_IndexColumn_ID < 1) throw new IllegalArgumentException ("AD_IndexColumn_ID is mandatory.");
        set_ValueNoCheck ("AD_IndexColumn_ID", Integer.valueOf(AD_IndexColumn_ID));
        
    }
    
    /** Get Index Column.
    @return Index Column */
    public int getAD_IndexColumn_ID() 
    {
        return get_ValueAsInt("AD_IndexColumn_ID");
        
    }
    
    /** Set Table Index.
    @param AD_TableIndex_ID Table Index */
    public void setAD_TableIndex_ID (int AD_TableIndex_ID)
    {
        if (AD_TableIndex_ID < 1) throw new IllegalArgumentException ("AD_TableIndex_ID is mandatory.");
        set_ValueNoCheck ("AD_TableIndex_ID", Integer.valueOf(AD_TableIndex_ID));
        
    }
    
    /** Get Table Index.
    @return Table Index */
    public int getAD_TableIndex_ID() 
    {
        return get_ValueAsInt("AD_TableIndex_ID");
        
    }
    
    /** Set Column SQL.
    @param ColumnSQL Virtual Column (r/o) */
    public void setColumnSQL (String ColumnSQL)
    {
        set_Value ("ColumnSQL", ColumnSQL);
        
    }
    
    /** Get Column SQL.
    @return Virtual Column (r/o) */
    public String getColumnSQL() 
    {
        return (String)get_Value("ColumnSQL");
        
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
    
    
}
