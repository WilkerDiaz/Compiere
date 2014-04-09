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
/** Generated Model for AD_ReportTemplate_Relation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ReportTemplate_Relation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ReportTemplate_Relation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ReportTemplate_Relation_ID id
    @param trx transaction
    */
    public X_AD_ReportTemplate_Relation (Ctx ctx, int AD_ReportTemplate_Relation_ID, Trx trx)
    {
        super (ctx, AD_ReportTemplate_Relation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ReportTemplate_Relation_ID == 0)
        {
            setAD_ReportTemplate_ID (0);
            setAD_ReportTemplate_Relation_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ReportTemplate_Relation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27532758788789L;
    /** Last Updated Timestamp 2009-08-18 15:11:12.0 */
    public static final long updatedMS = 1250633472000L;
    /** AD_Table_ID=1054 */
    public static final int Table_ID=1054;
    
    /** TableName=AD_ReportTemplate_Relation */
    public static final String Table_Name="AD_ReportTemplate_Relation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set External Report.
    @param AD_ReportTemplate_ID External Report */
    public void setAD_ReportTemplate_ID (int AD_ReportTemplate_ID)
    {
        if (AD_ReportTemplate_ID < 1) throw new IllegalArgumentException ("AD_ReportTemplate_ID is mandatory.");
        set_ValueNoCheck ("AD_ReportTemplate_ID", Integer.valueOf(AD_ReportTemplate_ID));
        
    }
    
    /** Get External Report.
    @return External Report */
    public int getAD_ReportTemplate_ID() 
    {
        return get_ValueAsInt("AD_ReportTemplate_ID");
        
    }
    
    /** Set Sub Report.
    @param AD_ReportTemplate_Related_ID Sub Report */
    public void setAD_ReportTemplate_Related_ID (int AD_ReportTemplate_Related_ID)
    {
        if (AD_ReportTemplate_Related_ID <= 0) set_Value ("AD_ReportTemplate_Related_ID", null);
        else
        set_Value ("AD_ReportTemplate_Related_ID", Integer.valueOf(AD_ReportTemplate_Related_ID));
        
    }
    
    /** Get Sub Report.
    @return Sub Report */
    public int getAD_ReportTemplate_Related_ID() 
    {
        return get_ValueAsInt("AD_ReportTemplate_Related_ID");
        
    }
    
    /** Set Report Template Relation.
    @param AD_ReportTemplate_Relation_ID Report Template Relation */
    public void setAD_ReportTemplate_Relation_ID (int AD_ReportTemplate_Relation_ID)
    {
        if (AD_ReportTemplate_Relation_ID < 1) throw new IllegalArgumentException ("AD_ReportTemplate_Relation_ID is mandatory.");
        set_ValueNoCheck ("AD_ReportTemplate_Relation_ID", Integer.valueOf(AD_ReportTemplate_Relation_ID));
        
    }
    
    /** Get Report Template Relation.
    @return Report Template Relation */
    public int getAD_ReportTemplate_Relation_ID() 
    {
        return get_ValueAsInt("AD_ReportTemplate_Relation_ID");
        
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
