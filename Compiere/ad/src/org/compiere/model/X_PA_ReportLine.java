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
/** Generated Model for PA_ReportLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_ReportLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_ReportLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_ReportLine_ID id
    @param trx transaction
    */
    public X_PA_ReportLine (Ctx ctx, int PA_ReportLine_ID, Trx trx)
    {
        super (ctx, PA_ReportLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_ReportLine_ID == 0)
        {
            setIsPrinted (true);	// Y
            setIsSummary (false);
            setLineType (null);
            setName (null);
            setPA_ReportLineSet_ID (0);
            setPA_ReportLine_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM PA_ReportLine WHERE PA_ReportLineSet_ID=@PA_ReportLineSet_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_ReportLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346889789L;
    /** Last Updated Timestamp 2008-11-20 14:52:53.0 */
    public static final long updatedMS = 1227221573000L;
    /** AD_Table_ID=448 */
    public static final int Table_ID=448;
    
    /** TableName=PA_ReportLine */
    public static final String Table_Name="PA_ReportLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Period Balance = BP */
    public static final String AMOUNTTYPE_PeriodBalance = X_Ref_PA_Report_AmountType.PERIOD_BALANCE.getValue();
    /** Total Balance = BT */
    public static final String AMOUNTTYPE_TotalBalance = X_Ref_PA_Report_AmountType.TOTAL_BALANCE.getValue();
    /** Year Balance = BY */
    public static final String AMOUNTTYPE_YearBalance = X_Ref_PA_Report_AmountType.YEAR_BALANCE.getValue();
    /** Period Credit Only = CP */
    public static final String AMOUNTTYPE_PeriodCreditOnly = X_Ref_PA_Report_AmountType.PERIOD_CREDIT_ONLY.getValue();
    /** Total Credit Only = CT */
    public static final String AMOUNTTYPE_TotalCreditOnly = X_Ref_PA_Report_AmountType.TOTAL_CREDIT_ONLY.getValue();
    /** Year Credit Only = CY */
    public static final String AMOUNTTYPE_YearCreditOnly = X_Ref_PA_Report_AmountType.YEAR_CREDIT_ONLY.getValue();
    /** Period Debit Only = DP */
    public static final String AMOUNTTYPE_PeriodDebitOnly = X_Ref_PA_Report_AmountType.PERIOD_DEBIT_ONLY.getValue();
    /** Total Debit Only = DT */
    public static final String AMOUNTTYPE_TotalDebitOnly = X_Ref_PA_Report_AmountType.TOTAL_DEBIT_ONLY.getValue();
    /** Year Debit Only = DY */
    public static final String AMOUNTTYPE_YearDebitOnly = X_Ref_PA_Report_AmountType.YEAR_DEBIT_ONLY.getValue();
    /** Period Quantity = QP */
    public static final String AMOUNTTYPE_PeriodQuantity = X_Ref_PA_Report_AmountType.PERIOD_QUANTITY.getValue();
    /** Total Quantity = QT */
    public static final String AMOUNTTYPE_TotalQuantity = X_Ref_PA_Report_AmountType.TOTAL_QUANTITY.getValue();
    /** Year Quantity = QY */
    public static final String AMOUNTTYPE_YearQuantity = X_Ref_PA_Report_AmountType.YEAR_QUANTITY.getValue();
    /** Set Amount Type.
    @param AmountType Type of amount to report */
    public void setAmountType (String AmountType)
    {
        if (!X_Ref_PA_Report_AmountType.isValid(AmountType))
        throw new IllegalArgumentException ("AmountType Invalid value - " + AmountType + " - Reference_ID=235 - BP - BT - BY - CP - CT - CY - DP - DT - DY - QP - QT - QY");
        set_Value ("AmountType", AmountType);
        
    }
    
    /** Get Amount Type.
    @return Type of amount to report */
    public String getAmountType() 
    {
        return (String)get_Value("AmountType");
        
    }
    
    /** Add (Op1+Op2) = A */
    public static final String CALCULATIONTYPE_AddOp1PlusOp2 = X_Ref_PA_Report_CalculationType.ADD_OP1_PLUS_OP2.getValue();
    /** Percentage (Op1 of Op2) = P */
    public static final String CALCULATIONTYPE_PercentageOp1OfOp2 = X_Ref_PA_Report_CalculationType.PERCENTAGE_OP1_OF_OP2.getValue();
    /** Add Range (Op1 to Op2) = R */
    public static final String CALCULATIONTYPE_AddRangeOp1ToOp2 = X_Ref_PA_Report_CalculationType.ADD_RANGE_OP1_TO_OP2.getValue();
    /** Subtract (Op1-Op2) = S */
    public static final String CALCULATIONTYPE_SubtractOp1_Op2 = X_Ref_PA_Report_CalculationType.SUBTRACT_OP1__OP2.getValue();
    /** Set Calculation.
    @param CalculationType Type of Calcultion to be performed */
    public void setCalculationType (String CalculationType)
    {
        if (!X_Ref_PA_Report_CalculationType.isValid(CalculationType))
        throw new IllegalArgumentException ("CalculationType Invalid value - " + CalculationType + " - Reference_ID=236 - A - P - R - S");
        set_Value ("CalculationType", CalculationType);
        
    }
    
    /** Get Calculation.
    @return Type of Calcultion to be performed */
    public String getCalculationType() 
    {
        return (String)get_Value("CalculationType");
        
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
    
    /** Set Budget.
    @param GL_Budget_ID General Ledger Budget */
    public void setGL_Budget_ID (int GL_Budget_ID)
    {
        if (GL_Budget_ID <= 0) set_Value ("GL_Budget_ID", null);
        else
        set_Value ("GL_Budget_ID", Integer.valueOf(GL_Budget_ID));
        
    }
    
    /** Get Budget.
    @return General Ledger Budget */
    public int getGL_Budget_ID() 
    {
        return get_ValueAsInt("GL_Budget_ID");
        
    }
    
    /** Set Printed.
    @param IsPrinted Indicates if this document / line is printed */
    public void setIsPrinted (boolean IsPrinted)
    {
        set_Value ("IsPrinted", Boolean.valueOf(IsPrinted));
        
    }
    
    /** Get Printed.
    @return Indicates if this document / line is printed */
    public boolean isPrinted() 
    {
        return get_ValueAsBoolean("IsPrinted");
        
    }
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_Value ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
    }
    
    /** Calculation = C */
    public static final String LINETYPE_Calculation = X_Ref_PA_Report_LineType.CALCULATION.getValue();
    /** Segment Value = S */
    public static final String LINETYPE_SegmentValue = X_Ref_PA_Report_LineType.SEGMENT_VALUE.getValue();
    /** Set Line Type.
    @param LineType Line Type */
    public void setLineType (String LineType)
    {
        if (LineType == null) throw new IllegalArgumentException ("LineType is mandatory");
        if (!X_Ref_PA_Report_LineType.isValid(LineType))
        throw new IllegalArgumentException ("LineType Invalid value - " + LineType + " - Reference_ID=241 - C - S");
        set_Value ("LineType", LineType);
        
    }
    
    /** Get Line Type.
    @return Line Type */
    public String getLineType() 
    {
        return (String)get_Value("LineType");
        
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
    
    /** Set Operand 1.
    @param Oper_1_ID First operand for calculation */
    public void setOper_1_ID (int Oper_1_ID)
    {
        if (Oper_1_ID <= 0) set_Value ("Oper_1_ID", null);
        else
        set_Value ("Oper_1_ID", Integer.valueOf(Oper_1_ID));
        
    }
    
    /** Get Operand 1.
    @return First operand for calculation */
    public int getOper_1_ID() 
    {
        return get_ValueAsInt("Oper_1_ID");
        
    }
    
    /** Set Operand 2.
    @param Oper_2_ID Second operand for calculation */
    public void setOper_2_ID (int Oper_2_ID)
    {
        if (Oper_2_ID <= 0) set_Value ("Oper_2_ID", null);
        else
        set_Value ("Oper_2_ID", Integer.valueOf(Oper_2_ID));
        
    }
    
    /** Get Operand 2.
    @return Second operand for calculation */
    public int getOper_2_ID() 
    {
        return get_ValueAsInt("Oper_2_ID");
        
    }
    
    /** Set Report Line Set.
    @param PA_ReportLineSet_ID Report Line Set */
    public void setPA_ReportLineSet_ID (int PA_ReportLineSet_ID)
    {
        if (PA_ReportLineSet_ID < 1) throw new IllegalArgumentException ("PA_ReportLineSet_ID is mandatory.");
        set_ValueNoCheck ("PA_ReportLineSet_ID", Integer.valueOf(PA_ReportLineSet_ID));
        
    }
    
    /** Get Report Line Set.
    @return Report Line Set */
    public int getPA_ReportLineSet_ID() 
    {
        return get_ValueAsInt("PA_ReportLineSet_ID");
        
    }
    
    /** Set Report Line.
    @param PA_ReportLine_ID Report Line */
    public void setPA_ReportLine_ID (int PA_ReportLine_ID)
    {
        if (PA_ReportLine_ID < 1) throw new IllegalArgumentException ("PA_ReportLine_ID is mandatory.");
        set_ValueNoCheck ("PA_ReportLine_ID", Integer.valueOf(PA_ReportLine_ID));
        
    }
    
    /** Get Report Line.
    @return Report Line */
    public int getPA_ReportLine_ID() 
    {
        return get_ValueAsInt("PA_ReportLine_ID");
        
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
