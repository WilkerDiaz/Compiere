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
/** Generated Model for Fact_Acct_Balance
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_Fact_Acct_Balance.java 9155 2010-08-03 08:54:53Z ragrawal $ */
public class X_Fact_Acct_Balance extends PO
{
    /** Standard Constructor
    @param ctx context
    @param Fact_Acct_Balance_ID id
    @param trx transaction
    */
    public X_Fact_Acct_Balance (Ctx ctx, int Fact_Acct_Balance_ID, Trx trx)
    {
        super (ctx, Fact_Acct_Balance_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (Fact_Acct_Balance_ID == 0)
        {
            setAccount_ID (0);
            setAmtAcctCr (Env.ZERO);
            setAmtAcctDr (Env.ZERO);
            setC_AcctSchema_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));
            setPostingType (null);
            setQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_Fact_Acct_Balance (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27562900211789L;
    /** Last Updated Timestamp 2010-08-03 00:18:15.0 */
    public static final long updatedMS = 1280774895000L;
    /** AD_Table_ID=547 */
    public static final int Table_ID=547;
    
    /** TableName=Fact_Acct_Balance */
    public static final String Table_Name="Fact_Acct_Balance";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
    }
    
    /** Set Account.
    @param Account_ID Account used */
    public void setAccount_ID (int Account_ID)
    {
        if (Account_ID < 1) throw new IllegalArgumentException ("Account_ID is mandatory.");
        set_ValueNoCheck ("Account_ID", Integer.valueOf(Account_ID));
        
    }
    
    /** Get Account.
    @return Account used */
    public int getAccount_ID() 
    {
        return get_ValueAsInt("Account_ID");
        
    }
    
    /** Set Accounted Credit.
    @param AmtAcctCr Accounted Credit Amount */
    public void setAmtAcctCr (java.math.BigDecimal AmtAcctCr)
    {
        if (AmtAcctCr == null) throw new IllegalArgumentException ("AmtAcctCr is mandatory.");
        set_Value ("AmtAcctCr", AmtAcctCr);
        
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
        set_Value ("AmtAcctDr", AmtAcctDr);
        
    }
    
    /** Get Accounted Debit.
    @return Accounted Debit Amount */
    public java.math.BigDecimal getAmtAcctDr() 
    {
        return get_ValueAsBigDecimal("AmtAcctDr");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_Value ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
        
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
    
    /** Set Location From.
    @param C_LocFrom_ID Location that inventory was moved from */
    public void setC_LocFrom_ID (int C_LocFrom_ID)
    {
        if (C_LocFrom_ID <= 0) set_Value ("C_LocFrom_ID", null);
        else
        set_Value ("C_LocFrom_ID", Integer.valueOf(C_LocFrom_ID));
        
    }
    
    /** Get Location From.
    @return Location that inventory was moved from */
    public int getC_LocFrom_ID() 
    {
        return get_ValueAsInt("C_LocFrom_ID");
        
    }
    
    /** Set Location To.
    @param C_LocTo_ID Location that inventory was moved to */
    public void setC_LocTo_ID (int C_LocTo_ID)
    {
        if (C_LocTo_ID <= 0) set_Value ("C_LocTo_ID", null);
        else
        set_Value ("C_LocTo_ID", Integer.valueOf(C_LocTo_ID));
        
    }
    
    /** Get Location To.
    @return Location that inventory was moved to */
    public int getC_LocTo_ID() 
    {
        return get_ValueAsInt("C_LocTo_ID");
        
    }
    
    /** Set Project Phase.
    @param C_ProjectPhase_ID Phase of a Project */
    public void setC_ProjectPhase_ID (int C_ProjectPhase_ID)
    {
        if (C_ProjectPhase_ID <= 0) set_ValueNoCheck ("C_ProjectPhase_ID", null);
        else
        set_ValueNoCheck ("C_ProjectPhase_ID", Integer.valueOf(C_ProjectPhase_ID));
        
    }
    
    /** Get Project Phase.
    @return Phase of a Project */
    public int getC_ProjectPhase_ID() 
    {
        return get_ValueAsInt("C_ProjectPhase_ID");
        
    }
    
    /** Set Project Task.
    @param C_ProjectTask_ID Actual Project Task in a Phase */
    public void setC_ProjectTask_ID (int C_ProjectTask_ID)
    {
        if (C_ProjectTask_ID <= 0) set_ValueNoCheck ("C_ProjectTask_ID", null);
        else
        set_ValueNoCheck ("C_ProjectTask_ID", Integer.valueOf(C_ProjectTask_ID));
        
    }
    
    /** Get Project Task.
    @return Actual Project Task in a Phase */
    public int getC_ProjectTask_ID() 
    {
        return get_ValueAsInt("C_ProjectTask_ID");
        
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
    
    /** Set Sub Account.
    @param C_SubAcct_ID Sub account for Element Value */
    public void setC_SubAcct_ID (int C_SubAcct_ID)
    {
        if (C_SubAcct_ID <= 0) set_ValueNoCheck ("C_SubAcct_ID", null);
        else
        set_ValueNoCheck ("C_SubAcct_ID", Integer.valueOf(C_SubAcct_ID));
        
    }
    
    /** Get Sub Account.
    @return Sub account for Element Value */
    public int getC_SubAcct_ID() 
    {
        return get_ValueAsInt("C_SubAcct_ID");
        
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
    
    /** Set Element_U1.
    @param Element_U1 Element_U1 */
    public void setElement_U1 (boolean Element_U1)
    {
        throw new IllegalArgumentException ("Element_U1 is virtual column");
        
    }
    
    /** Get Element_U1.
    @return Element_U1 */
    public boolean isElement_U1() 
    {
        return get_ValueAsBoolean("Element_U1");
        
    }
    
    /** Set Element_U2.
    @param Element_U2 Element_U2 */
    public void setElement_U2 (boolean Element_U2)
    {
        throw new IllegalArgumentException ("Element_U2 is virtual column");
        
    }
    
    /** Get Element_U2.
    @return Element_U2 */
    public boolean isElement_U2() 
    {
        return get_ValueAsBoolean("Element_U2");
        
    }
    
    /** Set Element_X1.
    @param Element_X1 Element_X1 */
    public void setElement_X1 (boolean Element_X1)
    {
        throw new IllegalArgumentException ("Element_X1 is virtual column");
        
    }
    
    /** Get Element_X1.
    @return Element_X1 */
    public boolean isElement_X1() 
    {
        return get_ValueAsBoolean("Element_X1");
        
    }
    
    /** Set Element_X2.
    @param Element_X2 Element_X2 */
    public void setElement_X2 (boolean Element_X2)
    {
        throw new IllegalArgumentException ("Element_X2 is virtual column");
        
    }
    
    /** Get Element_X2.
    @return Element_X2 */
    public boolean isElement_X2() 
    {
        return get_ValueAsBoolean("Element_X2");
        
    }
    
    /** Set Balance Aggregation.
    @param Fact_Accumulation_ID Balance Aggregation */
    public void setFact_Accumulation_ID (int Fact_Accumulation_ID)
    {
        if (Fact_Accumulation_ID <= 0) set_Value ("Fact_Accumulation_ID", null);
        else
        set_Value ("Fact_Accumulation_ID", Integer.valueOf(Fact_Accumulation_ID));
        
    }
    
    /** Get Balance Aggregation.
    @return Balance Aggregation */
    public int getFact_Accumulation_ID() 
    {
        return get_ValueAsInt("Fact_Accumulation_ID");
        
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_Value ("User1_ID", null);
        else
        set_Value ("User1_ID", Integer.valueOf(User1_ID));
        
    }
    
    /** Get User List 1.
    @return User defined list element #1 */
    public int getUser1_ID() 
    {
        return get_ValueAsInt("User1_ID");
        
    }
    
    /** Set User List 2.
    @param User2_ID User defined list element #2 */
    public void setUser2_ID (int User2_ID)
    {
        if (User2_ID <= 0) set_Value ("User2_ID", null);
        else
        set_Value ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    /** Set User Element 1.
    @param UserElement1_ID User defined accounting Element */
    public void setUserElement1_ID (int UserElement1_ID)
    {
        if (UserElement1_ID <= 0) set_Value ("UserElement1_ID", null);
        else
        set_Value ("UserElement1_ID", Integer.valueOf(UserElement1_ID));
        
    }
    
    /** Get User Element 1.
    @return User defined accounting Element */
    public int getUserElement1_ID() 
    {
        return get_ValueAsInt("UserElement1_ID");
        
    }
    
    /** Set User Element 2.
    @param UserElement2_ID User defined accounting Element */
    public void setUserElement2_ID (int UserElement2_ID)
    {
        if (UserElement2_ID <= 0) set_Value ("UserElement2_ID", null);
        else
        set_Value ("UserElement2_ID", Integer.valueOf(UserElement2_ID));
        
    }
    
    /** Get User Element 2.
    @return User defined accounting Element */
    public int getUserElement2_ID() 
    {
        return get_ValueAsInt("UserElement2_ID");
        
    }
    
    
}
