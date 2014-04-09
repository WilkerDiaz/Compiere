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
/** Generated Model for T_CalendarReview
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: GenerateModel.java 8757 2010-05-12 21:32:32Z nnayak $ */
public class X_T_CalendarReview extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_CalendarReview_ID id
    @param trx transaction
    */
    public X_T_CalendarReview (Ctx ctx, int T_CalendarReview_ID, Trx trx)
    {
        super (ctx, T_CalendarReview_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_CalendarReview_ID == 0)
        {
            setAD_PInstance_ID (0);
            setC_Calendar_ID (0);
            setC_Period_ID (0);
            setC_Year_ID (0);
            setErrorMsg (null);
            setT_CalendarReview_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_CalendarReview (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27558592364789L;
    /** Last Updated Timestamp 2010-06-14 03:40:48.0 */
    public static final long updatedMS = 1276467048000L;
    /** AD_Table_ID=2167 */
    public static final int Table_ID=2167;
    
    /** TableName=T_CalendarReview */
    public static final String Table_Name="T_CalendarReview";
    
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
        set_Value ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID < 1) throw new IllegalArgumentException ("C_Calendar_ID is mandatory.");
        set_Value ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
    }
    
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID < 1) throw new IllegalArgumentException ("C_Period_ID is mandatory.");
        set_Value ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Set Year.
    @param C_Year_ID Calendar Year */
    public void setC_Year_ID (int C_Year_ID)
    {
        if (C_Year_ID < 1) throw new IllegalArgumentException ("C_Year_ID is mandatory.");
        set_Value ("C_Year_ID", Integer.valueOf(C_Year_ID));
        
    }
    
    /** Get Year.
    @return Calendar Year */
    public int getC_Year_ID() 
    {
        return get_ValueAsInt("C_Year_ID");
        
    }
    
    /** Set Error Message.
    @param ErrorMsg Error Message */
    public void setErrorMsg (String ErrorMsg)
    {
        if (ErrorMsg == null) throw new IllegalArgumentException ("ErrorMsg is mandatory.");
        set_Value ("ErrorMsg", ErrorMsg);
        
    }
    
    /** Get Error Message.
    @return Error Message */
    public String getErrorMsg() 
    {
        return (String)get_Value("ErrorMsg");
        
    }
    
    /** Set T_CalendarReview_ID.
    @param T_CalendarReview_ID T_CalendarReview_ID */
    public void setT_CalendarReview_ID (int T_CalendarReview_ID)
    {
        if (T_CalendarReview_ID < 1) throw new IllegalArgumentException ("T_CalendarReview_ID is mandatory.");
        set_ValueNoCheck ("T_CalendarReview_ID", Integer.valueOf(T_CalendarReview_ID));
        
    }
    
    /** Get T_CalendarReview_ID.
    @return T_CalendarReview_ID */
    public int getT_CalendarReview_ID() 
    {
        return get_ValueAsInt("T_CalendarReview_ID");
        
    }
    
    
}
