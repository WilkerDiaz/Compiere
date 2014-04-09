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
/** Generated Model for C_Remuneration
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Remuneration.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Remuneration extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Remuneration_ID id
    @param trx transaction
    */
    public X_C_Remuneration (Ctx ctx, int C_Remuneration_ID, Trx trx)
    {
        super (ctx, C_Remuneration_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Remuneration_ID == 0)
        {
            setC_Remuneration_ID (0);
            setGrossRAmt (Env.ZERO);
            setGrossRCost (Env.ZERO);
            setName (null);
            setOvertimeAmt (Env.ZERO);
            setOvertimeCost (Env.ZERO);
            setRemunerationType (null);
            setStandardHours (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Remuneration (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=792 */
    public static final int Table_ID=792;
    
    /** TableName=C_Remuneration */
    public static final String Table_Name="C_Remuneration";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Remuneration.
    @param C_Remuneration_ID Wage or Salary */
    public void setC_Remuneration_ID (int C_Remuneration_ID)
    {
        if (C_Remuneration_ID < 1) throw new IllegalArgumentException ("C_Remuneration_ID is mandatory.");
        set_ValueNoCheck ("C_Remuneration_ID", Integer.valueOf(C_Remuneration_ID));
        
    }
    
    /** Get Remuneration.
    @return Wage or Salary */
    public int getC_Remuneration_ID() 
    {
        return get_ValueAsInt("C_Remuneration_ID");
        
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
    
    /** Set Gross Amount.
    @param GrossRAmt Gross Remuneration Amount */
    public void setGrossRAmt (java.math.BigDecimal GrossRAmt)
    {
        if (GrossRAmt == null) throw new IllegalArgumentException ("GrossRAmt is mandatory.");
        set_Value ("GrossRAmt", GrossRAmt);
        
    }
    
    /** Get Gross Amount.
    @return Gross Remuneration Amount */
    public java.math.BigDecimal getGrossRAmt() 
    {
        return get_ValueAsBigDecimal("GrossRAmt");
        
    }
    
    /** Set Gross Cost.
    @param GrossRCost Gross Remuneration Costs */
    public void setGrossRCost (java.math.BigDecimal GrossRCost)
    {
        if (GrossRCost == null) throw new IllegalArgumentException ("GrossRCost is mandatory.");
        set_Value ("GrossRCost", GrossRCost);
        
    }
    
    /** Get Gross Cost.
    @return Gross Remuneration Costs */
    public java.math.BigDecimal getGrossRCost() 
    {
        return get_ValueAsBigDecimal("GrossRCost");
        
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
    
    /** Set Overtime Amount.
    @param OvertimeAmt Hourly Overtime Rate */
    public void setOvertimeAmt (java.math.BigDecimal OvertimeAmt)
    {
        if (OvertimeAmt == null) throw new IllegalArgumentException ("OvertimeAmt is mandatory.");
        set_Value ("OvertimeAmt", OvertimeAmt);
        
    }
    
    /** Get Overtime Amount.
    @return Hourly Overtime Rate */
    public java.math.BigDecimal getOvertimeAmt() 
    {
        return get_ValueAsBigDecimal("OvertimeAmt");
        
    }
    
    /** Set Overtime Cost.
    @param OvertimeCost Hourly Overtime Cost */
    public void setOvertimeCost (java.math.BigDecimal OvertimeCost)
    {
        if (OvertimeCost == null) throw new IllegalArgumentException ("OvertimeCost is mandatory.");
        set_Value ("OvertimeCost", OvertimeCost);
        
    }
    
    /** Get Overtime Cost.
    @return Hourly Overtime Cost */
    public java.math.BigDecimal getOvertimeCost() 
    {
        return get_ValueAsBigDecimal("OvertimeCost");
        
    }
    
    /** Bi-Weekly = B */
    public static final String REMUNERATIONTYPE_Bi_Weekly = X_Ref_C_Remuneration_Type.BI__WEEKLY.getValue();
    /** Daily = D */
    public static final String REMUNERATIONTYPE_Daily = X_Ref_C_Remuneration_Type.DAILY.getValue();
    /** Hourly = H */
    public static final String REMUNERATIONTYPE_Hourly = X_Ref_C_Remuneration_Type.HOURLY.getValue();
    /** Monthly = M */
    public static final String REMUNERATIONTYPE_Monthly = X_Ref_C_Remuneration_Type.MONTHLY.getValue();
    /** Twice Monthly = T */
    public static final String REMUNERATIONTYPE_TwiceMonthly = X_Ref_C_Remuneration_Type.TWICE_MONTHLY.getValue();
    /** Weekly = W */
    public static final String REMUNERATIONTYPE_Weekly = X_Ref_C_Remuneration_Type.WEEKLY.getValue();
    /** Set Remuneration Type.
    @param RemunerationType Type of Remuneration */
    public void setRemunerationType (String RemunerationType)
    {
        if (RemunerationType == null) throw new IllegalArgumentException ("RemunerationType is mandatory");
        if (!X_Ref_C_Remuneration_Type.isValid(RemunerationType))
        throw new IllegalArgumentException ("RemunerationType Invalid value - " + RemunerationType + " - Reference_ID=346 - B - D - H - M - T - W");
        set_Value ("RemunerationType", RemunerationType);
        
    }
    
    /** Get Remuneration Type.
    @return Type of Remuneration */
    public String getRemunerationType() 
    {
        return (String)get_Value("RemunerationType");
        
    }
    
    /** Set Standard Hours.
    @param StandardHours Standard Work Hours based on Remuneration Type */
    public void setStandardHours (int StandardHours)
    {
        set_Value ("StandardHours", Integer.valueOf(StandardHours));
        
    }
    
    /** Get Standard Hours.
    @return Standard Work Hours based on Remuneration Type */
    public int getStandardHours() 
    {
        return get_ValueAsInt("StandardHours");
        
    }
    
    
}
