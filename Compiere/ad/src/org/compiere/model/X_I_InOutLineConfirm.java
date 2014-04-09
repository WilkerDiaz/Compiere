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
/** Generated Model for I_InOutLineConfirm
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_InOutLineConfirm.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_InOutLineConfirm extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_InOutLineConfirm_ID id
    @param trx transaction
    */
    public X_I_InOutLineConfirm (Ctx ctx, int I_InOutLineConfirm_ID, Trx trx)
    {
        super (ctx, I_InOutLineConfirm_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_InOutLineConfirm_ID == 0)
        {
            setI_InOutLineConfirm_ID (0);
            setI_IsImported (null);	// N
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_InOutLineConfirm (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511144584789L;
    /** Last Updated Timestamp 2008-12-11 10:14:28.0 */
    public static final long updatedMS = 1229019268000L;
    /** AD_Table_ID=740 */
    public static final int Table_ID=740;
    
    /** TableName=I_InOutLineConfirm */
    public static final String Table_Name="I_InOutLineConfirm";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Confirmation No.
    @param ConfirmationNo Confirmation Number */
    public void setConfirmationNo (String ConfirmationNo)
    {
        set_Value ("ConfirmationNo", ConfirmationNo);
        
    }
    
    /** Get Confirmation No.
    @return Confirmation Number */
    public String getConfirmationNo() 
    {
        return (String)get_Value("ConfirmationNo");
        
    }
    
    /** Set Confirmed Quantity.
    @param ConfirmedQty Confirmation of a received quantity */
    public void setConfirmedQty (java.math.BigDecimal ConfirmedQty)
    {
        set_Value ("ConfirmedQty", ConfirmedQty);
        
    }
    
    /** Get Confirmed Quantity.
    @return Confirmation of a received quantity */
    public java.math.BigDecimal getConfirmedQty() 
    {
        return get_ValueAsBigDecimal("ConfirmedQty");
        
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
    
    /** Set Difference.
    @param DifferenceQty Difference Quantity */
    public void setDifferenceQty (java.math.BigDecimal DifferenceQty)
    {
        set_Value ("DifferenceQty", DifferenceQty);
        
    }
    
    /** Get Difference.
    @return Difference Quantity */
    public java.math.BigDecimal getDifferenceQty() 
    {
        return get_ValueAsBigDecimal("DifferenceQty");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Set Ship/Receipt Confirmation Import Line.
    @param I_InOutLineConfirm_ID Material Shipment or Receipt Confirmation Import Line */
    public void setI_InOutLineConfirm_ID (int I_InOutLineConfirm_ID)
    {
        if (I_InOutLineConfirm_ID < 1) throw new IllegalArgumentException ("I_InOutLineConfirm_ID is mandatory.");
        set_ValueNoCheck ("I_InOutLineConfirm_ID", Integer.valueOf(I_InOutLineConfirm_ID));
        
    }
    
    /** Get Ship/Receipt Confirmation Import Line.
    @return Material Shipment or Receipt Confirmation Import Line */
    public int getI_InOutLineConfirm_ID() 
    {
        return get_ValueAsInt("I_InOutLineConfirm_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getI_InOutLineConfirm_ID()));
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set Ship/Receipt Confirmation Line.
    @param M_InOutLineConfirm_ID Material Shipment or Receipt Confirmation Line */
    public void setM_InOutLineConfirm_ID (int M_InOutLineConfirm_ID)
    {
        if (M_InOutLineConfirm_ID <= 0) set_Value ("M_InOutLineConfirm_ID", null);
        else
        set_Value ("M_InOutLineConfirm_ID", Integer.valueOf(M_InOutLineConfirm_ID));
        
    }
    
    /** Get Ship/Receipt Confirmation Line.
    @return Material Shipment or Receipt Confirmation Line */
    public int getM_InOutLineConfirm_ID() 
    {
        return get_ValueAsInt("M_InOutLineConfirm_ID");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Scrapped Quantity.
    @param ScrappedQty The Quantity scrapped due to QA issues */
    public void setScrappedQty (java.math.BigDecimal ScrappedQty)
    {
        set_Value ("ScrappedQty", ScrappedQty);
        
    }
    
    /** Get Scrapped Quantity.
    @return The Quantity scrapped due to QA issues */
    public java.math.BigDecimal getScrappedQty() 
    {
        return get_ValueAsBigDecimal("ScrappedQty");
        
    }
    
    
}
