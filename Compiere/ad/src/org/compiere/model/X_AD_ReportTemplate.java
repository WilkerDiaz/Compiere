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
/** Generated Model for AD_ReportTemplate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ReportTemplate.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ReportTemplate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ReportTemplate_ID id
    @param trx transaction
    */
    public X_AD_ReportTemplate (Ctx ctx, int AD_ReportTemplate_ID, Trx trx)
    {
        super (ctx, AD_ReportTemplate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ReportTemplate_ID == 0)
        {
            setAD_ReportTemplate_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ReportTemplate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27502780838789L;
    /** Last Updated Timestamp 2008-09-05 15:58:42.0 */
    public static final long updatedMS = 1220655522000L;
    /** AD_Table_ID=1053 */
    public static final int Table_ID=1053;
    
    /** TableName=AD_ReportTemplate */
    public static final String Table_Name="AD_ReportTemplate";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business View.
    @param AD_BView_ID The logical subset of related data for the purposes of reporting */
    public void setAD_BView_ID (int AD_BView_ID)
    {
        if (AD_BView_ID <= 0) set_Value ("AD_BView_ID", null);
        else
        set_Value ("AD_BView_ID", Integer.valueOf(AD_BView_ID));
        
    }
    
    /** Get Business View.
    @return The logical subset of related data for the purposes of reporting */
    public int getAD_BView_ID() 
    {
        return get_ValueAsInt("AD_BView_ID");
        
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
    
    /** Jasper = 1 */
    public static final String AD_REPORTTEMPLATE_TYPE_Jasper = X_Ref_AD_ReportTemplate_Type.JASPER.getValue();
    /** Crystal = 2 */
    public static final String AD_REPORTTEMPLATE_TYPE_Crystal = X_Ref_AD_ReportTemplate_Type.CRYSTAL.getValue();
    /** Set Report Template Type.
    @param AD_ReportTemplate_Type Report Template Type */
    public void setAD_ReportTemplate_Type (String AD_ReportTemplate_Type)
    {
        if (!X_Ref_AD_ReportTemplate_Type.isValid(AD_ReportTemplate_Type))
        throw new IllegalArgumentException ("AD_ReportTemplate_Type Invalid value - " + AD_ReportTemplate_Type + " - Reference_ID=462 - 1 - 2");
        set_Value ("AD_ReportTemplate_Type", AD_ReportTemplate_Type);
        
    }
    
    /** Get Report Template Type.
    @return Report Template Type */
    public String getAD_ReportTemplate_Type() 
    {
        return (String)get_Value("AD_ReportTemplate_Type");
        
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
