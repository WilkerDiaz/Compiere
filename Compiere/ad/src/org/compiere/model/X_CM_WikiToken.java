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
/** Generated Model for CM_WikiToken
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_WikiToken.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_WikiToken extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_WikiToken_ID id
    @param trx transaction
    */
    public X_CM_WikiToken (Ctx ctx, int CM_WikiToken_ID, Trx trx)
    {
        super (ctx, CM_WikiToken_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_WikiToken_ID == 0)
        {
            setCM_WikiToken_ID (0);
            setName (null);
            setTokenType (null);	// I
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_WikiToken (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=905 */
    public static final int Table_ID=905;
    
    /** TableName=CM_WikiToken */
    public static final String Table_Name="CM_WikiToken";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_ValueNoCheck ("AD_Table_ID", null);
        else
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Wiki Token.
    @param CM_WikiToken_ID Wiki Token */
    public void setCM_WikiToken_ID (int CM_WikiToken_ID)
    {
        if (CM_WikiToken_ID < 1) throw new IllegalArgumentException ("CM_WikiToken_ID is mandatory.");
        set_ValueNoCheck ("CM_WikiToken_ID", Integer.valueOf(CM_WikiToken_ID));
        
    }
    
    /** Get Wiki Token.
    @return Wiki Token */
    public int getCM_WikiToken_ID() 
    {
        return get_ValueAsInt("CM_WikiToken_ID");
        
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
    
    /** Set Macro.
    @param Macro Macro */
    public void setMacro (String Macro)
    {
        set_Value ("Macro", Macro);
        
    }
    
    /** Get Macro.
    @return Macro */
    public String getMacro() 
    {
        return (String)get_Value("Macro");
        
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
    
    /** Set Sql SELECT.
    @param SelectClause SQL SELECT clause */
    public void setSelectClause (String SelectClause)
    {
        set_Value ("SelectClause", SelectClause);
        
    }
    
    /** Get Sql SELECT.
    @return SQL SELECT clause */
    public String getSelectClause() 
    {
        return (String)get_Value("SelectClause");
        
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
    
    /** External Link = E */
    public static final String TOKENTYPE_ExternalLink = X_Ref_CM_WikiToken_Type.EXTERNAL_LINK.getValue();
    /** Internal Link = I */
    public static final String TOKENTYPE_InternalLink = X_Ref_CM_WikiToken_Type.INTERNAL_LINK.getValue();
    /** SQL Command = Q */
    public static final String TOKENTYPE_SQLCommand = X_Ref_CM_WikiToken_Type.SQL_COMMAND.getValue();
    /** Style = S */
    public static final String TOKENTYPE_Style = X_Ref_CM_WikiToken_Type.STYLE.getValue();
    /** Set TokenType.
    @param TokenType Wiki Token Type */
    public void setTokenType (String TokenType)
    {
        if (TokenType == null) throw new IllegalArgumentException ("TokenType is mandatory");
        if (!X_Ref_CM_WikiToken_Type.isValid(TokenType))
        throw new IllegalArgumentException ("TokenType Invalid value - " + TokenType + " - Reference_ID=397 - E - I - Q - S");
        set_Value ("TokenType", TokenType);
        
    }
    
    /** Get TokenType.
    @return Wiki Token Type */
    public String getTokenType() 
    {
        return (String)get_Value("TokenType");
        
    }
    
    /** Set Sql WHERE.
    @param WhereClause Fully qualified SQL WHERE clause */
    public void setWhereClause (String WhereClause)
    {
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
