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
/** Generated Model for AD_PInstance_Para
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PInstance_Para.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PInstance_Para extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PInstance_Para_ID id
    @param trx transaction
    */
    public X_AD_PInstance_Para (Ctx ctx, int AD_PInstance_Para_ID, Trx trx)
    {
        super (ctx, AD_PInstance_Para_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PInstance_Para_ID == 0)
        {
            setAD_PInstance_ID (0);
            setSeqNo (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PInstance_Para (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27510884923789L;
    /** Last Updated Timestamp 2008-12-08 10:06:47.0 */
    public static final long updatedMS = 1228759607000L;
    /** AD_Table_ID=283 */
    public static final int Table_ID=283;
    
    /** TableName=AD_PInstance_Para */
    public static final String Table_Name="AD_PInstance_Para";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
        
    }
    
    /** Set Info.
    @param Info Information */
    public void setInfo (String Info)
    {
        set_Value ("Info", Info);
        
    }
    
    /** Get Info.
    @return Information */
    public String getInfo() 
    {
        return (String)get_Value("Info");
        
    }
    
    /** Set Info To.
    @param Info_To Info To */
    public void setInfo_To (String Info_To)
    {
        set_Value ("Info_To", Info_To);
        
    }
    
    /** Get Info To.
    @return Info To */
    public String getInfo_To() 
    {
        return (String)get_Value("Info_To");
        
    }
    
    /** Set Process Date.
    @param P_Date Process Parameter */
    public void setP_Date (Timestamp P_Date)
    {
        set_Value ("P_Date", P_Date);
        
    }
    
    /** Get Process Date.
    @return Process Parameter */
    public Timestamp getP_Date() 
    {
        return (Timestamp)get_Value("P_Date");
        
    }
    
    /** Set Process Date To.
    @param P_Date_To Process Parameter */
    public void setP_Date_To (Timestamp P_Date_To)
    {
        set_Value ("P_Date_To", P_Date_To);
        
    }
    
    /** Get Process Date To.
    @return Process Parameter */
    public Timestamp getP_Date_To() 
    {
        return (Timestamp)get_Value("P_Date_To");
        
    }
    
    /** Set Process Number.
    @param P_Number Process Parameter */
    public void setP_Number (java.math.BigDecimal P_Number)
    {
        set_Value ("P_Number", P_Number);
        
    }
    
    /** Get Process Number.
    @return Process Parameter */
    public java.math.BigDecimal getP_Number() 
    {
        return get_ValueAsBigDecimal("P_Number");
        
    }
    
    /** Set Process Number To.
    @param P_Number_To Process Parameter */
    public void setP_Number_To (java.math.BigDecimal P_Number_To)
    {
        set_Value ("P_Number_To", P_Number_To);
        
    }
    
    /** Get Process Number To.
    @return Process Parameter */
    public java.math.BigDecimal getP_Number_To() 
    {
        return get_ValueAsBigDecimal("P_Number_To");
        
    }
    
    /** Set Process String.
    @param P_String Process Parameter */
    public void setP_String (String P_String)
    {
        set_Value ("P_String", P_String);
        
    }
    
    /** Get Process String.
    @return Process Parameter */
    public String getP_String() 
    {
        return (String)get_Value("P_String");
        
    }
    
    /** Set Process String To.
    @param P_String_To Process Parameter */
    public void setP_String_To (String P_String_To)
    {
        set_Value ("P_String_To", P_String_To);
        
    }
    
    /** Get Process String To.
    @return Process Parameter */
    public String getP_String_To() 
    {
        return (String)get_Value("P_String_To");
        
    }
    
    /** Set Parameter Name.
    @param ParameterName Parameter Name */
    public void setParameterName (String ParameterName)
    {
        set_Value ("ParameterName", ParameterName);
        
    }
    
    /** Get Parameter Name.
    @return Parameter Name */
    public String getParameterName() 
    {
        return (String)get_Value("ParameterName");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_ValueNoCheck ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    
}
