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
/** Generated Model for C_AcctSchema_Element
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_AcctSchema_Element.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_AcctSchema_Element extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_AcctSchema_Element_ID id
    @param trx transaction
    */
    public X_C_AcctSchema_Element (Ctx ctx, int C_AcctSchema_Element_ID, Trx trx)
    {
        super (ctx, C_AcctSchema_Element_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_AcctSchema_Element_ID == 0)
        {
            setC_AcctSchema_Element_ID (0);
            setC_AcctSchema_ID (0);
            setElementType (null);
            setIsBalanced (false);
            setIsMandatory (false);
            setName (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM C_AcctSchema_Element WHERE C_AcctSchema_ID=@C_AcctSchema_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_AcctSchema_Element (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=279 */
    public static final int Table_ID=279;
    
    /** TableName=C_AcctSchema_Element */
    public static final String Table_Name="C_AcctSchema_Element";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Acct.Schema Element.
    @param C_AcctSchema_Element_ID Acct.Schema Element */
    public void setC_AcctSchema_Element_ID (int C_AcctSchema_Element_ID)
    {
        if (C_AcctSchema_Element_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_Element_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_Element_ID", Integer.valueOf(C_AcctSchema_Element_ID));
        
    }
    
    /** Get Acct.Schema Element.
    @return Acct.Schema Element */
    public int getC_AcctSchema_Element_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_Element_ID");
        
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
    
    /** Set Element.
    @param C_Element_ID Accounting Element */
    public void setC_Element_ID (int C_Element_ID)
    {
        if (C_Element_ID <= 0) set_Value ("C_Element_ID", null);
        else
        set_Value ("C_Element_ID", Integer.valueOf(C_Element_ID));
        
    }
    
    /** Get Element.
    @return Accounting Element */
    public int getC_Element_ID() 
    {
        return get_ValueAsInt("C_Element_ID");
        
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
        if (ElementType == null) throw new IllegalArgumentException ("ElementType is mandatory");
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
    
    /** Set Balanced.
    @param IsBalanced Accounting Element needs to be balanced */
    public void setIsBalanced (boolean IsBalanced)
    {
        set_Value ("IsBalanced", Boolean.valueOf(IsBalanced));
        
    }
    
    /** Get Balanced.
    @return Accounting Element needs to be balanced */
    public boolean isBalanced() 
    {
        return get_ValueAsBoolean("IsBalanced");
        
    }
    
    /** Set Mandatory.
    @param IsMandatory Data is required in this column */
    public void setIsMandatory (boolean IsMandatory)
    {
        set_Value ("IsMandatory", Boolean.valueOf(IsMandatory));
        
    }
    
    /** Get Mandatory.
    @return Data is required in this column */
    public boolean isMandatory() 
    {
        return get_ValueAsBoolean("IsMandatory");
        
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
