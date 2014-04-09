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
/** Generated Model for PA_RatioElement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_RatioElement.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_RatioElement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_RatioElement_ID id
    @param trx transaction
    */
    public X_PA_RatioElement (Ctx ctx, int PA_RatioElement_ID, Trx trx)
    {
        super (ctx, PA_RatioElement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_RatioElement_ID == 0)
        {
            setName (null);
            setPA_RatioElement_ID (0);
            setPA_Ratio_ID (0);
            setRatioElementType (null);
            setRatioOperand (null);	// P
            setSeqNo (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_RatioElement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=836 */
    public static final int Table_ID=836;
    
    /** TableName=PA_RatioElement */
    public static final String Table_Name="PA_RatioElement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Account.
    @param Account_ID Account used */
    public void setAccount_ID (int Account_ID)
    {
        if (Account_ID <= 0) set_Value ("Account_ID", null);
        else
        set_Value ("Account_ID", Integer.valueOf(Account_ID));
        
    }
    
    /** Get Account.
    @return Account used */
    public int getAccount_ID() 
    {
        return get_ValueAsInt("Account_ID");
        
    }
    
    /** Set Constant Value.
    @param ConstantValue Constant value */
    public void setConstantValue (java.math.BigDecimal ConstantValue)
    {
        set_Value ("ConstantValue", ConstantValue);
        
    }
    
    /** Get Constant Value.
    @return Constant value */
    public java.math.BigDecimal getConstantValue() 
    {
        return get_ValueAsBigDecimal("ConstantValue");
        
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
    
    /** Set Measure Calculation.
    @param PA_MeasureCalc_ID Calculation method for measuring performance */
    public void setPA_MeasureCalc_ID (int PA_MeasureCalc_ID)
    {
        if (PA_MeasureCalc_ID <= 0) set_Value ("PA_MeasureCalc_ID", null);
        else
        set_Value ("PA_MeasureCalc_ID", Integer.valueOf(PA_MeasureCalc_ID));
        
    }
    
    /** Get Measure Calculation.
    @return Calculation method for measuring performance */
    public int getPA_MeasureCalc_ID() 
    {
        return get_ValueAsInt("PA_MeasureCalc_ID");
        
    }
    
    /** Set Ratio Element.
    @param PA_RatioElement_ID Performance Ratio Element */
    public void setPA_RatioElement_ID (int PA_RatioElement_ID)
    {
        if (PA_RatioElement_ID < 1) throw new IllegalArgumentException ("PA_RatioElement_ID is mandatory.");
        set_ValueNoCheck ("PA_RatioElement_ID", Integer.valueOf(PA_RatioElement_ID));
        
    }
    
    /** Get Ratio Element.
    @return Performance Ratio Element */
    public int getPA_RatioElement_ID() 
    {
        return get_ValueAsInt("PA_RatioElement_ID");
        
    }
    
    /** Set Ratio Used.
    @param PA_RatioUsed_ID Performance Ratio Used */
    public void setPA_RatioUsed_ID (int PA_RatioUsed_ID)
    {
        if (PA_RatioUsed_ID <= 0) set_Value ("PA_RatioUsed_ID", null);
        else
        set_Value ("PA_RatioUsed_ID", Integer.valueOf(PA_RatioUsed_ID));
        
    }
    
    /** Get Ratio Used.
    @return Performance Ratio Used */
    public int getPA_RatioUsed_ID() 
    {
        return get_ValueAsInt("PA_RatioUsed_ID");
        
    }
    
    /** Set Ratio.
    @param PA_Ratio_ID Performance Ratio */
    public void setPA_Ratio_ID (int PA_Ratio_ID)
    {
        if (PA_Ratio_ID < 1) throw new IllegalArgumentException ("PA_Ratio_ID is mandatory.");
        set_ValueNoCheck ("PA_Ratio_ID", Integer.valueOf(PA_Ratio_ID));
        
    }
    
    /** Get Ratio.
    @return Performance Ratio */
    public int getPA_Ratio_ID() 
    {
        return get_ValueAsInt("PA_Ratio_ID");
        
    }
    
    /** Actual = A */
    public static final String POSTINGTYPE_Actual = X_Ref__Posting_Type.ACTUAL.getValue();
    /** Budget = B */
    public static final String POSTINGTYPE_Budget = X_Ref__Posting_Type.BUDGET.getValue();
    /** Commitment = E */
    public static final String POSTINGTYPE_Commitment = X_Ref__Posting_Type.COMMITMENT.getValue();
    /** Reservation = R */
    public static final String POSTINGTYPE_Reservation = X_Ref__Posting_Type.RESERVATION.getValue();
    /** Statistical = S */
    public static final String POSTINGTYPE_Statistical = X_Ref__Posting_Type.STATISTICAL.getValue();
    /** Set PostingType.
    @param PostingType The type of posted amount for the transaction */
    public void setPostingType (String PostingType)
    {
        if (!X_Ref__Posting_Type.isValid(PostingType))
        throw new IllegalArgumentException ("PostingType Invalid value - " + PostingType + " - Reference_ID=125 - A - B - E - R - S");
        set_Value ("PostingType", PostingType);
        
    }
    
    /** Get PostingType.
    @return The type of posted amount for the transaction */
    public String getPostingType() 
    {
        return (String)get_Value("PostingType");
        
    }
    
    /** Account Value = A */
    public static final String RATIOELEMENTTYPE_AccountValue = X_Ref_PA_Ratio_ElementType.ACCOUNT_VALUE.getValue();
    /** Constant = C */
    public static final String RATIOELEMENTTYPE_Constant = X_Ref_PA_Ratio_ElementType.CONSTANT.getValue();
    /** Ratio = R */
    public static final String RATIOELEMENTTYPE_Ratio = X_Ref_PA_Ratio_ElementType.RATIO.getValue();
    /** Calculation = X */
    public static final String RATIOELEMENTTYPE_Calculation = X_Ref_PA_Ratio_ElementType.CALCULATION.getValue();
    /** Set Element Type.
    @param RatioElementType Ratio Element Type */
    public void setRatioElementType (String RatioElementType)
    {
        if (RatioElementType == null) throw new IllegalArgumentException ("RatioElementType is mandatory");
        if (!X_Ref_PA_Ratio_ElementType.isValid(RatioElementType))
        throw new IllegalArgumentException ("RatioElementType Invalid value - " + RatioElementType + " - Reference_ID=372 - A - C - R - X");
        set_Value ("RatioElementType", RatioElementType);
        
    }
    
    /** Get Element Type.
    @return Ratio Element Type */
    public String getRatioElementType() 
    {
        return (String)get_Value("RatioElementType");
        
    }
    
    /** Divide = D */
    public static final String RATIOOPERAND_Divide = X_Ref_PA_Ratio_Operand.DIVIDE.getValue();
    /** Multiply = M */
    public static final String RATIOOPERAND_Multiply = X_Ref_PA_Ratio_Operand.MULTIPLY.getValue();
    /** Minus = N */
    public static final String RATIOOPERAND_Minus = X_Ref_PA_Ratio_Operand.MINUS.getValue();
    /** Plus = P */
    public static final String RATIOOPERAND_Plus = X_Ref_PA_Ratio_Operand.PLUS.getValue();
    /** Set Operand.
    @param RatioOperand Ratio Operand */
    public void setRatioOperand (String RatioOperand)
    {
        if (RatioOperand == null) throw new IllegalArgumentException ("RatioOperand is mandatory");
        if (!X_Ref_PA_Ratio_Operand.isValid(RatioOperand))
        throw new IllegalArgumentException ("RatioOperand Invalid value - " + RatioOperand + " - Reference_ID=373 - D - M - N - P");
        set_Value ("RatioOperand", RatioOperand);
        
    }
    
    /** Get Operand.
    @return Ratio Operand */
    public String getRatioOperand() 
    {
        return (String)get_Value("RatioOperand");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    
}
