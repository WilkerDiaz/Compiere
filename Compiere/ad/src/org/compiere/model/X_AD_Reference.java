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
/** Generated Model for AD_Reference
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Reference.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Reference extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Reference_ID id
    @param trx transaction
    */
    public X_AD_Reference (Ctx ctx, int AD_Reference_ID, Trx trx)
    {
        super (ctx, AD_Reference_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Reference_ID == 0)
        {
            setAD_Reference_ID (0);
            setEntityType (null);	// U
            setName (null);
            setValidationType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Reference (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313560789L;
    /** Last Updated Timestamp 2009-03-04 09:37:24.0 */
    public static final long updatedMS = 1236188244000L;
    /** AD_Table_ID=102 */
    public static final int Table_ID=102;
    
    /** TableName=AD_Reference */
    public static final String Table_Name="AD_Reference";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Reference.
    @param AD_Reference_ID System Reference and Validation */
    public void setAD_Reference_ID (int AD_Reference_ID)
    {
        if (AD_Reference_ID < 1) throw new IllegalArgumentException ("AD_Reference_ID is mandatory.");
        set_ValueNoCheck ("AD_Reference_ID", Integer.valueOf(AD_Reference_ID));
        
    }
    
    /** Get Reference.
    @return System Reference and Validation */
    public int getAD_Reference_ID() 
    {
        return get_ValueAsInt("AD_Reference_ID");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
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
    
    /** Set Value Format.
    @param VFormat Format of the value;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public void setVFormat (String VFormat)
    {
        set_Value ("VFormat", VFormat);
        
    }
    
    /** Get Value Format.
    @return Format of the value;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public String getVFormat() 
    {
        return (String)get_Value("VFormat");
        
    }
    
    /** DataType = D */
    public static final String VALIDATIONTYPE_DataType = X_Ref_AD_Reference_Validation_Types.DATA_TYPE.getValue();
    /** List Validation = L */
    public static final String VALIDATIONTYPE_ListValidation = X_Ref_AD_Reference_Validation_Types.LIST_VALIDATION.getValue();
    /** Table Validation = T */
    public static final String VALIDATIONTYPE_TableValidation = X_Ref_AD_Reference_Validation_Types.TABLE_VALIDATION.getValue();
    /** Set Validation type.
    @param ValidationType Different method of validating data */
    public void setValidationType (String ValidationType)
    {
        if (ValidationType == null) throw new IllegalArgumentException ("ValidationType is mandatory");
        if (!X_Ref_AD_Reference_Validation_Types.isValid(ValidationType))
        throw new IllegalArgumentException ("ValidationType Invalid value - " + ValidationType + " - Reference_ID=2 - D - L - T");
        set_Value ("ValidationType", ValidationType);
        
    }
    
    /** Get Validation type.
    @return Different method of validating data */
    public String getValidationType() 
    {
        return (String)get_Value("ValidationType");
        
    }
    
    
}
