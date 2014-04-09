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
/** Generated Model for C_BP_Withholding
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BP_Withholding.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BP_Withholding extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BP_Withholding_ID id
    @param trx transaction
    */
    public X_C_BP_Withholding (Ctx ctx, int C_BP_Withholding_ID, Trx trx)
    {
        super (ctx, C_BP_Withholding_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BP_Withholding_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_Withholding_ID (0);
            setIsMandatoryWithholding (false);
            setIsTemporaryExempt (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BP_Withholding (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=299 */
    public static final int Table_ID=299;
    
    /** TableName=C_BP_Withholding */
    public static final String Table_Name="C_BP_Withholding";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_BPartner_ID()));
        
    }
    
    /** Set Withholding.
    @param C_Withholding_ID Withholding type defined */
    public void setC_Withholding_ID (int C_Withholding_ID)
    {
        if (C_Withholding_ID < 1) throw new IllegalArgumentException ("C_Withholding_ID is mandatory.");
        set_ValueNoCheck ("C_Withholding_ID", Integer.valueOf(C_Withholding_ID));
        
    }
    
    /** Get Withholding.
    @return Withholding type defined */
    public int getC_Withholding_ID() 
    {
        return get_ValueAsInt("C_Withholding_ID");
        
    }
    
    /** Set Exempt reason.
    @param ExemptReason Reason for not withholding */
    public void setExemptReason (String ExemptReason)
    {
        set_Value ("ExemptReason", ExemptReason);
        
    }
    
    /** Get Exempt reason.
    @return Reason for not withholding */
    public String getExemptReason() 
    {
        return (String)get_Value("ExemptReason");
        
    }
    
    /** Set Mandatory Withholding.
    @param IsMandatoryWithholding Monies must be withheld */
    public void setIsMandatoryWithholding (boolean IsMandatoryWithholding)
    {
        set_Value ("IsMandatoryWithholding", Boolean.valueOf(IsMandatoryWithholding));
        
    }
    
    /** Get Mandatory Withholding.
    @return Monies must be withheld */
    public boolean isMandatoryWithholding() 
    {
        return get_ValueAsBoolean("IsMandatoryWithholding");
        
    }
    
    /** Set Temporary exempt.
    @param IsTemporaryExempt Temporarily do not withhold taxes */
    public void setIsTemporaryExempt (boolean IsTemporaryExempt)
    {
        set_Value ("IsTemporaryExempt", Boolean.valueOf(IsTemporaryExempt));
        
    }
    
    /** Get Temporary exempt.
    @return Temporarily do not withhold taxes */
    public boolean isTemporaryExempt() 
    {
        return get_ValueAsBoolean("IsTemporaryExempt");
        
    }
    
    
}
