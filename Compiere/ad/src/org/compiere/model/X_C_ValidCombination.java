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
/** Generated Model for C_ValidCombination
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_C_ValidCombination.java 9155 2010-08-03 08:54:53Z ragrawal $ */
public class X_C_ValidCombination extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_ValidCombination_ID id
    @param trx transaction
    */
    public X_C_ValidCombination (Ctx ctx, int C_ValidCombination_ID, Trx trx)
    {
        super (ctx, C_ValidCombination_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_ValidCombination_ID == 0)
        {
            setAccount_ID (0);
            setC_AcctSchema_ID (0);
            setC_ValidCombination_ID (0);
            setIsFullyQualified (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_ValidCombination (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27562905077789L;
    /** Last Updated Timestamp 2010-08-03 01:39:21.0 */
    public static final long updatedMS = 1280779761000L;
    /** AD_Table_ID=176 */
    public static final int Table_ID=176;
    
    /** TableName=C_ValidCombination */
    public static final String Table_Name="C_ValidCombination";
    
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
        if (AD_OrgTrx_ID <= 0) set_ValueNoCheck ("AD_OrgTrx_ID", null);
        else
        set_ValueNoCheck ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
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
    
    /** Set Alias.
    @param Alias Defines an alternate method of indicating an account combination. */
    public void setAlias (String Alias)
    {
        set_Value ("Alias", Alias);
        
    }
    
    /** Get Alias.
    @return Defines an alternate method of indicating an account combination. */
    public String getAlias() 
    {
        return (String)get_Value("Alias");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_ValueNoCheck ("C_Activity_ID", null);
        else
        set_ValueNoCheck ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
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
        if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
        else
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
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
        if (C_Campaign_ID <= 0) set_ValueNoCheck ("C_Campaign_ID", null);
        else
        set_ValueNoCheck ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
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
        if (C_LocFrom_ID <= 0) set_ValueNoCheck ("C_LocFrom_ID", null);
        else
        set_ValueNoCheck ("C_LocFrom_ID", Integer.valueOf(C_LocFrom_ID));
        
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
        if (C_LocTo_ID <= 0) set_ValueNoCheck ("C_LocTo_ID", null);
        else
        set_ValueNoCheck ("C_LocTo_ID", Integer.valueOf(C_LocTo_ID));
        
    }
    
    /** Get Location To.
    @return Location that inventory was moved to */
    public int getC_LocTo_ID() 
    {
        return get_ValueAsInt("C_LocTo_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_ValueNoCheck ("C_Project_ID", null);
        else
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
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
        if (C_SalesRegion_ID <= 0) set_ValueNoCheck ("C_SalesRegion_ID", null);
        else
        set_ValueNoCheck ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
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
    
    /** Set Combination.
    @param C_ValidCombination_ID Valid Account Combination */
    public void setC_ValidCombination_ID (int C_ValidCombination_ID)
    {
        if (C_ValidCombination_ID < 1) throw new IllegalArgumentException ("C_ValidCombination_ID is mandatory.");
        set_ValueNoCheck ("C_ValidCombination_ID", Integer.valueOf(C_ValidCombination_ID));
        
    }
    
    /** Get Combination.
    @return Valid Account Combination */
    public int getC_ValidCombination_ID() 
    {
        return get_ValueAsInt("C_ValidCombination_ID");
        
    }
    
    /** Set Combination.
    @param Combination Unique combination of account elements */
    public void setCombination (String Combination)
    {
        set_ValueNoCheck ("Combination", Combination);
        
    }
    
    /** Get Combination.
    @return Unique combination of account elements */
    public String getCombination() 
    {
        return (String)get_Value("Combination");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getCombination());
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_ValueNoCheck ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Element_AY.
    @param Element_AY Element_AY */
    public void setElement_AY (boolean Element_AY)
    {
        throw new IllegalArgumentException ("Element_AY is virtual column");
        
    }
    
    /** Get Element_AY.
    @return Element_AY */
    public boolean isElement_AY() 
    {
        return get_ValueAsBoolean("Element_AY");
        
    }
    
    /** Set Element_BP.
    @param Element_BP Element_BP */
    public void setElement_BP (boolean Element_BP)
    {
        throw new IllegalArgumentException ("Element_BP is virtual column");
        
    }
    
    /** Get Element_BP.
    @return Element_BP */
    public boolean isElement_BP() 
    {
        return get_ValueAsBoolean("Element_BP");
        
    }
    
    /** Set Element_LF.
    @param Element_LF Element_LF */
    public void setElement_LF (boolean Element_LF)
    {
        throw new IllegalArgumentException ("Element_LF is virtual column");
        
    }
    
    /** Get Element_LF.
    @return Element_LF */
    public boolean isElement_LF() 
    {
        return get_ValueAsBoolean("Element_LF");
        
    }
    
    /** Set Element_LT.
    @param Element_LT Element_LT */
    public void setElement_LT (boolean Element_LT)
    {
        throw new IllegalArgumentException ("Element_LT is virtual column");
        
    }
    
    /** Get Element_LT.
    @return Element_LT */
    public boolean isElement_LT() 
    {
        return get_ValueAsBoolean("Element_LT");
        
    }
    
    /** Set Element_MC.
    @param Element_MC Element_MC */
    public void setElement_MC (boolean Element_MC)
    {
        throw new IllegalArgumentException ("Element_MC is virtual column");
        
    }
    
    /** Get Element_MC.
    @return Element_MC */
    public boolean isElement_MC() 
    {
        return get_ValueAsBoolean("Element_MC");
        
    }
    
    /** Set Element_OT.
    @param Element_OT Element_OT */
    public void setElement_OT (boolean Element_OT)
    {
        throw new IllegalArgumentException ("Element_OT is virtual column");
        
    }
    
    /** Get Element_OT.
    @return Element_OT */
    public boolean isElement_OT() 
    {
        return get_ValueAsBoolean("Element_OT");
        
    }
    
    /** Set Element_PJ.
    @param Element_PJ Element_PJ */
    public void setElement_PJ (boolean Element_PJ)
    {
        throw new IllegalArgumentException ("Element_PJ is virtual column");
        
    }
    
    /** Get Element_PJ.
    @return Element_PJ */
    public boolean isElement_PJ() 
    {
        return get_ValueAsBoolean("Element_PJ");
        
    }
    
    /** Set Element_PR.
    @param Element_PR Element_PR */
    public void setElement_PR (boolean Element_PR)
    {
        throw new IllegalArgumentException ("Element_PR is virtual column");
        
    }
    
    /** Get Element_PR.
    @return Element_PR */
    public boolean isElement_PR() 
    {
        return get_ValueAsBoolean("Element_PR");
        
    }
    
    /** Set Element_SA.
    @param Element_SA Element_SA */
    public void setElement_SA (boolean Element_SA)
    {
        throw new IllegalArgumentException ("Element_SA is virtual column");
        
    }
    
    /** Get Element_SA.
    @return Element_SA */
    public boolean isElement_SA() 
    {
        return get_ValueAsBoolean("Element_SA");
        
    }
    
    /** Set Element_SR.
    @param Element_SR Element_SR */
    public void setElement_SR (boolean Element_SR)
    {
        throw new IllegalArgumentException ("Element_SR is virtual column");
        
    }
    
    /** Get Element_SR.
    @return Element_SR */
    public boolean isElement_SR() 
    {
        return get_ValueAsBoolean("Element_SR");
        
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
    
    /** Set Fully Qualified.
    @param IsFullyQualified This account is fully qualified */
    public void setIsFullyQualified (boolean IsFullyQualified)
    {
        set_ValueNoCheck ("IsFullyQualified", Boolean.valueOf(IsFullyQualified));
        
    }
    
    /** Get Fully Qualified.
    @return This account is fully qualified */
    public boolean isFullyQualified() 
    {
        return get_ValueAsBoolean("IsFullyQualified");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_ValueNoCheck ("User1_ID", null);
        else
        set_ValueNoCheck ("User1_ID", Integer.valueOf(User1_ID));
        
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
        if (User2_ID <= 0) set_ValueNoCheck ("User2_ID", null);
        else
        set_ValueNoCheck ("User2_ID", Integer.valueOf(User2_ID));
        
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
