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
/** Generated Model for GL_JournalLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_GL_JournalLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_GL_JournalLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param GL_JournalLine_ID id
    @param trx transaction
    */
    public X_GL_JournalLine (Ctx ctx, int GL_JournalLine_ID, Trx trx)
    {
        super (ctx, GL_JournalLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (GL_JournalLine_ID == 0)
        {
            setAmtAcctCr (Env.ZERO);
            setAmtAcctDr (Env.ZERO);
            setAmtSourceCr (Env.ZERO);
            setAmtSourceDr (Env.ZERO);
            setC_ConversionType_ID (0);	// @C_ConversionType_ID@
            setC_Currency_ID (0);	// @C_Currency_ID@
            setC_ValidCombination_ID (0);
            setCurrencyRate (Env.ZERO);	// @CurrencyRate@;
            1
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @DateAcct@
            setGL_JournalLine_ID (0);
            setGL_Journal_ID (0);
            setIsGenerated (false);
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM GL_JournalLine WHERE GL_Journal_ID=@GL_Journal_ID@
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_GL_JournalLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=226 */
    public static final int Table_ID=226;
    
    /** TableName=GL_JournalLine */
    public static final String Table_Name="GL_JournalLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Accounted Credit.
    @param AmtAcctCr Accounted Credit Amount */
    public void setAmtAcctCr (java.math.BigDecimal AmtAcctCr)
    {
        if (AmtAcctCr == null) throw new IllegalArgumentException ("AmtAcctCr is mandatory.");
        set_ValueNoCheck ("AmtAcctCr", AmtAcctCr);
        
    }
    
    /** Get Accounted Credit.
    @return Accounted Credit Amount */
    public java.math.BigDecimal getAmtAcctCr() 
    {
        return get_ValueAsBigDecimal("AmtAcctCr");
        
    }
    
    /** Set Accounted Debit.
    @param AmtAcctDr Accounted Debit Amount */
    public void setAmtAcctDr (java.math.BigDecimal AmtAcctDr)
    {
        if (AmtAcctDr == null) throw new IllegalArgumentException ("AmtAcctDr is mandatory.");
        set_ValueNoCheck ("AmtAcctDr", AmtAcctDr);
        
    }
    
    /** Get Accounted Debit.
    @return Accounted Debit Amount */
    public java.math.BigDecimal getAmtAcctDr() 
    {
        return get_ValueAsBigDecimal("AmtAcctDr");
        
    }
    
    /** Set Source Credit.
    @param AmtSourceCr Source Credit Amount */
    public void setAmtSourceCr (java.math.BigDecimal AmtSourceCr)
    {
        if (AmtSourceCr == null) throw new IllegalArgumentException ("AmtSourceCr is mandatory.");
        set_Value ("AmtSourceCr", AmtSourceCr);
        
    }
    
    /** Get Source Credit.
    @return Source Credit Amount */
    public java.math.BigDecimal getAmtSourceCr() 
    {
        return get_ValueAsBigDecimal("AmtSourceCr");
        
    }
    
    /** Set Source Debit.
    @param AmtSourceDr Source Debit Amount */
    public void setAmtSourceDr (java.math.BigDecimal AmtSourceDr)
    {
        if (AmtSourceDr == null) throw new IllegalArgumentException ("AmtSourceDr is mandatory.");
        set_Value ("AmtSourceDr", AmtSourceDr);
        
    }
    
    /** Get Source Debit.
    @return Source Debit Amount */
    public java.math.BigDecimal getAmtSourceDr() 
    {
        return get_ValueAsBigDecimal("AmtSourceDr");
        
    }
    
    /** Set Account Alias.
    @param C_AccountAlias_ID Account Alias */
    public void setC_AccountAlias_ID (int C_AccountAlias_ID)
    {
        if (C_AccountAlias_ID <= 0) set_Value ("C_AccountAlias_ID", null);
        else
        set_Value ("C_AccountAlias_ID", Integer.valueOf(C_AccountAlias_ID));
        
    }
    
    /** Get Account Alias.
    @return Account Alias */
    public int getC_AccountAlias_ID() 
    {
        return get_ValueAsInt("C_AccountAlias_ID");
        
    }
    
    /** Set Rate Type.
    @param C_ConversionType_ID Currency Conversion Rate Type */
    public void setC_ConversionType_ID (int C_ConversionType_ID)
    {
        if (C_ConversionType_ID < 1) throw new IllegalArgumentException ("C_ConversionType_ID is mandatory.");
        set_Value ("C_ConversionType_ID", Integer.valueOf(C_ConversionType_ID));
        
    }
    
    /** Get Rate Type.
    @return Currency Conversion Rate Type */
    public int getC_ConversionType_ID() 
    {
        return get_ValueAsInt("C_ConversionType_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_Value ("C_UOM_ID", null);
        else
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Combination.
    @param C_ValidCombination_ID Valid Account Combination */
    public void setC_ValidCombination_ID (int C_ValidCombination_ID)
    {
        if (C_ValidCombination_ID < 1) throw new IllegalArgumentException ("C_ValidCombination_ID is mandatory.");
        set_Value ("C_ValidCombination_ID", Integer.valueOf(C_ValidCombination_ID));
        
    }
    
    /** Get Combination.
    @return Valid Account Combination */
    public int getC_ValidCombination_ID() 
    {
        return get_ValueAsInt("C_ValidCombination_ID");
        
    }
    
    /** Set Rate.
    @param CurrencyRate Currency Conversion Rate */
    public void setCurrencyRate (java.math.BigDecimal CurrencyRate)
    {
        if (CurrencyRate == null) throw new IllegalArgumentException ("CurrencyRate is mandatory.");
        set_ValueNoCheck ("CurrencyRate", CurrencyRate);
        
    }
    
    /** Get Rate.
    @return Currency Conversion Rate */
    public java.math.BigDecimal getCurrencyRate() 
    {
        return get_ValueAsBigDecimal("CurrencyRate");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        if (DateAcct == null) throw new IllegalArgumentException ("DateAcct is mandatory.");
        set_Value ("DateAcct", DateAcct);
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
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
    
    /** Set Journal Line.
    @param GL_JournalLine_ID General Ledger Journal Line */
    public void setGL_JournalLine_ID (int GL_JournalLine_ID)
    {
        if (GL_JournalLine_ID < 1) throw new IllegalArgumentException ("GL_JournalLine_ID is mandatory.");
        set_ValueNoCheck ("GL_JournalLine_ID", Integer.valueOf(GL_JournalLine_ID));
        
    }
    
    /** Get Journal Line.
    @return General Ledger Journal Line */
    public int getGL_JournalLine_ID() 
    {
        return get_ValueAsInt("GL_JournalLine_ID");
        
    }
    
    /** Set Journal.
    @param GL_Journal_ID General Ledger Journal */
    public void setGL_Journal_ID (int GL_Journal_ID)
    {
        if (GL_Journal_ID < 1) throw new IllegalArgumentException ("GL_Journal_ID is mandatory.");
        set_ValueNoCheck ("GL_Journal_ID", Integer.valueOf(GL_Journal_ID));
        
    }
    
    /** Get Journal.
    @return General Ledger Journal */
    public int getGL_Journal_ID() 
    {
        return get_ValueAsInt("GL_Journal_ID");
        
    }
    
    /** Set Use Account Alias.
    @param HasAlias Ability to select (partial) account combinations by an Alias */
    public void setHasAlias (boolean HasAlias)
    {
        throw new IllegalArgumentException ("HasAlias is virtual column");
        
    }
    
    /** Get Use Account Alias.
    @return Ability to select (partial) account combinations by an Alias */
    public boolean isHasAlias() 
    {
        return get_ValueAsBoolean("HasAlias");
        
    }
    
    /** Set Generated.
    @param IsGenerated This Line is generated */
    public void setIsGenerated (boolean IsGenerated)
    {
        set_ValueNoCheck ("IsGenerated", Boolean.valueOf(IsGenerated));
        
    }
    
    /** Get Generated.
    @return This Line is generated */
    public boolean isGenerated() 
    {
        return get_ValueAsBoolean("IsGenerated");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    
}
