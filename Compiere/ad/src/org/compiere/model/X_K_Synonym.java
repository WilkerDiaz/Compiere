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
/** Generated Model for K_Synonym
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_K_Synonym.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_K_Synonym extends PO
{
    /** Standard Constructor
    @param ctx context
    @param K_Synonym_ID id
    @param trx transaction
    */
    public X_K_Synonym (Ctx ctx, int K_Synonym_ID, Trx trx)
    {
        super (ctx, K_Synonym_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (K_Synonym_ID == 0)
        {
            setAD_Language (null);
            setK_Synonym_ID (0);
            setName (null);
            setSynonymName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_K_Synonym (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=608 */
    public static final int Table_ID=608;
    
    /** TableName=K_Synonym */
    public static final String Table_Name="K_Synonym";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_Value ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Set Knowledge Synonym.
    @param K_Synonym_ID Knowledge Keyword Synonym */
    public void setK_Synonym_ID (int K_Synonym_ID)
    {
        if (K_Synonym_ID < 1) throw new IllegalArgumentException ("K_Synonym_ID is mandatory.");
        set_ValueNoCheck ("K_Synonym_ID", Integer.valueOf(K_Synonym_ID));
        
    }
    
    /** Get Knowledge Synonym.
    @return Knowledge Keyword Synonym */
    public int getK_Synonym_ID() 
    {
        return get_ValueAsInt("K_Synonym_ID");
        
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
    
    /** Set Synonym Name.
    @param SynonymName The synonym for the name */
    public void setSynonymName (String SynonymName)
    {
        if (SynonymName == null) throw new IllegalArgumentException ("SynonymName is mandatory.");
        set_Value ("SynonymName", SynonymName);
        
    }
    
    /** Get Synonym Name.
    @return The synonym for the name */
    public String getSynonymName() 
    {
        return (String)get_Value("SynonymName");
        
    }
    
    
}
