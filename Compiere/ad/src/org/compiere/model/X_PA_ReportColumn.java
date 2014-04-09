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
/** Generated Model for PA_ReportColumn
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_ReportColumn.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_ReportColumn extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_ReportColumn_ID id
    @param trx transaction
    */
    public X_PA_ReportColumn (Ctx ctx, int PA_ReportColumn_ID, Trx trx)
    {
        super (ctx, PA_ReportColumn_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_ReportColumn_ID == 0)
        {
            setColumnType (null);	// R
            setIsPrinted (true);	// Y
            setName (null);
            setPA_ReportColumnSet_ID (0);
            setPA_ReportColumn_ID (0);
            setPostingType (null);	// A
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM PA_ReportColumn WHERE PA_ReportColumnSet_ID=@PA_ReportColumnSet_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_ReportColumn (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27521257774789L;
    /** Last Updated Timestamp 2009-04-07 11:27:38.0 */
    public static final long updatedMS = 1239132458000L;
    /** AD_Table_ID=446 */
    public static final int Table_ID=446;
    
    /** TableName=PA_ReportColumn */
    public static final String Table_Name="PA_ReportColumn";
    
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
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
        else
        set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Account Element.
    @param C_ElementValue_ID Account Element */
    public void setC_ElementValue_ID (int C_ElementValue_ID)
    {
        if (C_ElementValue_ID <= 0) set_Value ("C_ElementValue_ID", null);
        else
        set_Value ("C_ElementValue_ID", Integer.valueOf(C_ElementValue_ID));
        
    }
    
    /** Get Account Element.
    @return Account Element */
    public int getC_ElementValue_ID() 
    {
        return get_ValueAsInt("C_ElementValue_ID");
        
    }
    
    /** Set Address.
    @param C_Location_ID Location or Address */
    public void setC_Location_ID (int C_Location_ID)
    {
        if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
        else
        set_Value ("C_Location_ID", Integer.valueOf(C_Location_ID));
        
    }
    
    /** Get Address.
    @return Location or Address */
    public int getC_Location_ID() 
    {
        return get_ValueAsInt("C_Location_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Sales Region.
    @param C_SalesRegion_ID Sales coverage region */
    public void setC_SalesRegion_ID (int C_SalesRegion_ID)
    {
        if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
        else
        set_Value ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
    }
    
    /** Get Sales Region.
    @return Sales coverage region */
    public int getC_SalesRegion_ID() 
    {
        return get_ValueAsInt("C_SalesRegion_ID");
        
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
    
    /** Calculation = C */
    public static final String COLUMNTYPE_Calculation = X_Ref_PA_Report_ColumnType.CALCULATION.getValue();
    /** Relative Period = R */
    public static final String COLUMNTYPE_RelativePeriod = X_Ref_PA_Report_ColumnType.RELATIVE_PERIOD.getValue();
    /** Segment Value = S */
    public static final String COLUMNTYPE_SegmentValue = X_Ref_PA_Report_ColumnType.SEGMENT_VALUE.getValue();
    /** Set Column Type.
    @param ColumnType Column Type */
    public void setColumnType (String ColumnType)
    {
        if (ColumnType == null) throw new IllegalArgumentException ("ColumnType is mandatory");
        if (!X_Ref_PA_Report_ColumnType.isValid(ColumnType))
        throw new IllegalArgumentException ("ColumnType Invalid value - " + ColumnType + " - Reference_ID=237 - C - R - S");
        set_Value ("ColumnType", ColumnType);
        
    }
    
    /** Get Column Type.
    @return Column Type */
    public String getColumnType() 
    {
        return (String)get_Value("ColumnType");
        
    }
    
    /** Accounting Currency = A */
    public static final String CURRENCYTYPE_AccountingCurrency = X_Ref_PA_Report_CurrencyType.ACCOUNTING_CURRENCY.getValue();
    /** Source Currency = S */
    public static final String CURRENCYTYPE_SourceCurrency = X_Ref_PA_Report_CurrencyType.SOURCE_CURRENCY.getValue();
    /** Set Currency Conversion Type.
    @param CurrencyType Currency Conversion Type */
    public void setCurrencyType (String CurrencyType)
    {
        if (!X_Ref_PA_Report_CurrencyType.isValid(CurrencyType))
        throw new IllegalArgumentException ("CurrencyType Invalid value - " + CurrencyType + " - Reference_ID=238 - A - S");
        set_Value ("CurrencyType", CurrencyType);
        
    }
    
    /** Get Currency Conversion Type.
    @return Currency Conversion Type */
    public String getCurrencyType() 
    {
        return (String)get_Value("CurrencyType");
        
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
    
    /** Account = AC */
    public static final String ELEMENTTYPE_Account = X_Ref_C_AcctSchema_ElementType.ACCOUNT.getValue();
    /** Activity = AY */
    public static final String ELEMENTTYPE_Activity = X_Ref_C_AcctSchema_ElementType.ACTIVITY.getValue();
    /** BPartner = BP */
    public static final String ELEMENTTYPE_BPartner = X_Ref_C_AcctSchema_ElementType.B_PARTNER.getValue();
    /** Location From = LF */
    public static final String ELEMENTTYPE_LocationFrom = X_Ref_C_AcctSchema_ElementType.LOCATION_FROM.getValue();
    /** Location To = LT */
    public static final String ELEMENTTYPE_LocationTo = X_Ref_C_AcctSchema_ElementType.LOCATION_TO.getValue();
    /** Campaign = MC */
    public static final String ELEMENTTYPE_Campaign = X_Ref_C_AcctSchema_ElementType.CAMPAIGN.getValue();
    /** Organization = OO */
    public static final String ELEMENTTYPE_Organization = X_Ref_C_AcctSchema_ElementType.ORGANIZATION.getValue();
    /** Org Trx = OT */
    public static final String ELEMENTTYPE_OrgTrx = X_Ref_C_AcctSchema_ElementType.ORG_TRX.getValue();
    /** Project = PJ */
    public static final String ELEMENTTYPE_Project = X_Ref_C_AcctSchema_ElementType.PROJECT.getValue();
    /** Product = PR */
    public static final String ELEMENTTYPE_Product = X_Ref_C_AcctSchema_ElementType.PRODUCT.getValue();
    /** Sub Account = SA */
    public static final String ELEMENTTYPE_SubAccount = X_Ref_C_AcctSchema_ElementType.SUB_ACCOUNT.getValue();
    /** Sales Region = SR */
    public static final String ELEMENTTYPE_SalesRegion = X_Ref_C_AcctSchema_ElementType.SALES_REGION.getValue();
    /** User List 1 = U1 */
    public static final String ELEMENTTYPE_UserList1 = X_Ref_C_AcctSchema_ElementType.USER_LIST1.getValue();
    /** User List 2 = U2 */
    public static final String ELEMENTTYPE_UserList2 = X_Ref_C_AcctSchema_ElementType.USER_LIST2.getValue();
    /** User Element 1 = X1 */
    public static final String ELEMENTTYPE_UserElement1 = X_Ref_C_AcctSchema_ElementType.USER_ELEMENT1.getValue();
    /** User Element 2 = X2 */
    public static final String ELEMENTTYPE_UserElement2 = X_Ref_C_AcctSchema_ElementType.USER_ELEMENT2.getValue();
    /** Set Type.
    @param ElementType Element Type (account or user defined) */
    public void setElementType (String ElementType)
    {
        if (!X_Ref_C_AcctSchema_ElementType.isValid(ElementType))
        throw new IllegalArgumentException ("ElementType Invalid value - " + ElementType + " - Reference_ID=181 - AC - AY - BP - LF - LT - MC - OO - OT - PJ - PR - SA - SR - U1 - U2 - X1 - X2");
        set_Value ("ElementType", ElementType);
        
    }
    
    /** Get Type.
    @return Element Type (account or user defined) */
    public String getElementType() 
    {
        return (String)get_Value("ElementType");
        
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
    
    /** Set Adhoc Conversion.
    @param IsAdhocConversion Perform conversion for all amounts to currency */
    public void setIsAdhocConversion (boolean IsAdhocConversion)
    {
        set_Value ("IsAdhocConversion", Boolean.valueOf(IsAdhocConversion));
        
    }
    
    /** Get Adhoc Conversion.
    @return Perform conversion for all amounts to currency */
    public boolean isAdhocConversion() 
    {
        return get_ValueAsBoolean("IsAdhocConversion");
        
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
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Org.
    @param Org_ID Organizational entity within client */
    public void setOrg_ID (int Org_ID)
    {
        if (Org_ID <= 0) set_Value ("Org_ID", null);
        else
        set_Value ("Org_ID", Integer.valueOf(Org_ID));
        
    }
    
    /** Get Org.
    @return Organizational entity within client */
    public int getOrg_ID() 
    {
        return get_ValueAsInt("Org_ID");
        
    }
    
    /** Set Report Column Set.
    @param PA_ReportColumnSet_ID Collection of Columns for Report */
    public void setPA_ReportColumnSet_ID (int PA_ReportColumnSet_ID)
    {
        if (PA_ReportColumnSet_ID < 1) throw new IllegalArgumentException ("PA_ReportColumnSet_ID is mandatory.");
        set_ValueNoCheck ("PA_ReportColumnSet_ID", Integer.valueOf(PA_ReportColumnSet_ID));
        
    }
    
    /** Get Report Column Set.
    @return Collection of Columns for Report */
    public int getPA_ReportColumnSet_ID() 
    {
        return get_ValueAsInt("PA_ReportColumnSet_ID");
        
    }
    
    /** Set Report Column.
    @param PA_ReportColumn_ID Column in Report */
    public void setPA_ReportColumn_ID (int PA_ReportColumn_ID)
    {
        if (PA_ReportColumn_ID < 1) throw new IllegalArgumentException ("PA_ReportColumn_ID is mandatory.");
        set_ValueNoCheck ("PA_ReportColumn_ID", Integer.valueOf(PA_ReportColumn_ID));
        
    }
    
    /** Get Report Column.
    @return Column in Report */
    public int getPA_ReportColumn_ID() 
    {
        return get_ValueAsInt("PA_ReportColumn_ID");
        
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
        if (PostingType == null) throw new IllegalArgumentException ("PostingType is mandatory");
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
    
    /** Set Relative Period.
    @param RelativePeriod Period offset (0 is current) */
    public void setRelativePeriod (java.math.BigDecimal RelativePeriod)
    {
        set_Value ("RelativePeriod", RelativePeriod);
        
    }
    
    /** Get Relative Period.
    @return Period offset (0 is current) */
    public java.math.BigDecimal getRelativePeriod() 
    {
        return get_ValueAsBigDecimal("RelativePeriod");
        
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
