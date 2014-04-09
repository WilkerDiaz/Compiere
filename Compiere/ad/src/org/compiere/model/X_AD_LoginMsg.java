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
/** Generated Model for AD_LoginMsg
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_LoginMsg.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_LoginMsg extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_LoginMsg_ID id
    @param trx transaction
    */
    public X_AD_LoginMsg (Ctx ctx, int AD_LoginMsg_ID, Trx trx)
    {
        super (ctx, AD_LoginMsg_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_LoginMsg_ID == 0)
        {
            setAD_LoginMsg_ID (0);
            setLoginMsgFrequency (null);	// O
            setLoginMsgType (null);	// I
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_LoginMsg (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27500271967789L;
    /** Last Updated Timestamp 2008-08-07 15:04:11.0 */
    public static final long updatedMS = 1218146651000L;
    /** AD_Table_ID=1065 */
    public static final int Table_ID=1065;
    
    /** TableName=AD_LoginMsg */
    public static final String Table_Name="AD_LoginMsg";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Login Message.
    @param AD_LoginMsg_ID Login Message for System Users */
    public void setAD_LoginMsg_ID (int AD_LoginMsg_ID)
    {
        if (AD_LoginMsg_ID < 1) throw new IllegalArgumentException ("AD_LoginMsg_ID is mandatory.");
        set_ValueNoCheck ("AD_LoginMsg_ID", Integer.valueOf(AD_LoginMsg_ID));
        
    }
    
    /** Get Login Message.
    @return Login Message for System Users */
    public int getAD_LoginMsg_ID() 
    {
        return get_ValueAsInt("AD_LoginMsg_ID");
        
    }
    
    /** Set Classname.
    @param Classname Java Classname */
    public void setClassname (String Classname)
    {
        set_Value ("Classname", Classname);
        
    }
    
    /** Get Classname.
    @return Java Classname */
    public String getClassname() 
    {
        return (String)get_Value("Classname");
        
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
    
    /** Daily = D */
    public static final String LOGINMSGFREQUENCY_Daily = X_Ref_AD_LoginMsg_Fequency.DAILY.getValue();
    /** Once per Month = M */
    public static final String LOGINMSGFREQUENCY_OncePerMonth = X_Ref_AD_LoginMsg_Fequency.ONCE_PER_MONTH.getValue();
    /** Once = O */
    public static final String LOGINMSGFREQUENCY_Once = X_Ref_AD_LoginMsg_Fequency.ONCE.getValue();
    /** Once per Week = W */
    public static final String LOGINMSGFREQUENCY_OncePerWeek = X_Ref_AD_LoginMsg_Fequency.ONCE_PER_WEEK.getValue();
    /** Set Frequency.
    @param LoginMsgFrequency Display Frequency */
    public void setLoginMsgFrequency (String LoginMsgFrequency)
    {
        if (LoginMsgFrequency == null) throw new IllegalArgumentException ("LoginMsgFrequency is mandatory");
        if (!X_Ref_AD_LoginMsg_Fequency.isValid(LoginMsgFrequency))
        throw new IllegalArgumentException ("LoginMsgFrequency Invalid value - " + LoginMsgFrequency + " - Reference_ID=476 - D - M - O - W");
        set_Value ("LoginMsgFrequency", LoginMsgFrequency);
        
    }
    
    /** Get Frequency.
    @return Display Frequency */
    public String getLoginMsgFrequency() 
    {
        return (String)get_Value("LoginMsgFrequency");
        
    }
    
    /** Confirmation = C */
    public static final String LOGINMSGTYPE_Confirmation = X_Ref_AD_LoginMsg_Type.CONFIRMATION.getValue();
    /** Info = I */
    public static final String LOGINMSGTYPE_Info = X_Ref_AD_LoginMsg_Type.INFO.getValue();
    /** License = L */
    public static final String LOGINMSGTYPE_License = X_Ref_AD_LoginMsg_Type.LICENSE.getValue();
    /** Set Message Type.
    @param LoginMsgType Login Message Type */
    public void setLoginMsgType (String LoginMsgType)
    {
        if (LoginMsgType == null) throw new IllegalArgumentException ("LoginMsgType is mandatory");
        if (!X_Ref_AD_LoginMsg_Type.isValid(LoginMsgType))
        throw new IllegalArgumentException ("LoginMsgType Invalid value - " + LoginMsgType + " - Reference_ID=475 - C - I - L");
        set_Value ("LoginMsgType", LoginMsgType);
        
    }
    
    /** Get Message Type.
    @return Login Message Type */
    public String getLoginMsgType() 
    {
        return (String)get_Value("LoginMsgType");
        
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
    
    /** Set Text Message.
    @param TextMsg Text Message */
    public void setTextMsg (String TextMsg)
    {
        set_Value ("TextMsg", TextMsg);
        
    }
    
    /** Get Text Message.
    @return Text Message */
    public String getTextMsg() 
    {
        return (String)get_Value("TextMsg");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
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
