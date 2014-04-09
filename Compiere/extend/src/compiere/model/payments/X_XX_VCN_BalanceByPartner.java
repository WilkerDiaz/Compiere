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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_BalanceByPartner
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_BalanceByPartner extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_BalanceByPartner_ID id
    @param trx transaction
    */
    public X_XX_VCN_BalanceByPartner (Ctx ctx, int XX_VCN_BalanceByPartner_ID, Trx trx)
    {
        super (ctx, XX_VCN_BalanceByPartner_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_BalanceByPartner_ID == 0)
        {
            setXX_VCN_BalanceByPartner_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_BalanceByPartner (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27661836418789L;
    /** Last Updated Timestamp 2013-09-20 16:35:02.0 */
    public static final long updatedMS = 1379711102000L;
    /** AD_Table_ID=1002655 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_BalanceByPartner");
        
    }
    ;
    
    /** TableName=XX_VCN_BalanceByPartner */
    public static final String Table_Name="XX_VCN_BalanceByPartner";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Amount Month Import.
    @param XX_AmountMonthImport Amount Month Import */
    public void setXX_AmountMonthImport (java.math.BigDecimal XX_AmountMonthImport)
    {
        set_Value ("XX_AmountMonthImport", XX_AmountMonthImport);
        
    }
    
    /** Get Amount Month Import.
    @return Amount Month Import */
    public java.math.BigDecimal getXX_AmountMonthImport() 
    {
        return get_ValueAsBigDecimal("XX_AmountMonthImport");
        
    }
    
    /** Set Amount Month National.
    @param XX_AmountMonthNational Amount Month National */
    public void setXX_AmountMonthNational (java.math.BigDecimal XX_AmountMonthNational)
    {
        set_Value ("XX_AmountMonthNational", XX_AmountMonthNational);
        
    }
    
    /** Get Amount Month National.
    @return Amount Month National */
    public java.math.BigDecimal getXX_AmountMonthNational() 
    {
        return get_ValueAsBigDecimal("XX_AmountMonthNational");
        
    }
    
    /** Assets = A */
    public static final String XX_INVOICETYPE_Assets = X_Ref_M_Product_ProductType.ASSETS.getValue();
    /** Expense type = E */
    public static final String XX_INVOICETYPE_ExpenseType = X_Ref_M_Product_ProductType.EXPENSE_TYPE.getValue();
    /** Item = I */
    public static final String XX_INVOICETYPE_Item = X_Ref_M_Product_ProductType.ITEM.getValue();
    /** Online = O */
    public static final String XX_INVOICETYPE_Online = X_Ref_M_Product_ProductType.ONLINE.getValue();
    /** Resource = R */
    public static final String XX_INVOICETYPE_Resource = X_Ref_M_Product_ProductType.RESOURCE.getValue();
    /** Service = S */
    public static final String XX_INVOICETYPE_Service = X_Ref_M_Product_ProductType.SERVICE.getValue();
    /** Set Invoice Type.
    @param XX_InvoiceType Invoice Type */
    public void setXX_InvoiceType (String XX_InvoiceType)
    {
        if (!X_Ref_M_Product_ProductType.isValid(XX_InvoiceType))
        throw new IllegalArgumentException ("XX_InvoiceType Invalid value - " + XX_InvoiceType + " - Reference_ID=270 - A - E - I - O - R - S");
        set_Value ("XX_InvoiceType", XX_InvoiceType);
        
    }
    
    /** Get Invoice Type.
    @return Invoice Type */
    public String getXX_InvoiceType() 
    {
        return (String)get_Value("XX_InvoiceType");
        
    }
    
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (int XX_Month)
    {
        set_Value ("XX_Month", Integer.valueOf(XX_Month));
        
    }
    
    /** Get Month.
    @return Month */
    public int getXX_Month() 
    {
        return get_ValueAsInt("XX_Month");
        
    }
    
    /** Set XX_VCN_BalanceByPartner_ID.
    @param XX_VCN_BalanceByPartner_ID XX_VCN_BalanceByPartner_ID */
    public void setXX_VCN_BalanceByPartner_ID (int XX_VCN_BalanceByPartner_ID)
    {
        if (XX_VCN_BalanceByPartner_ID < 1) throw new IllegalArgumentException ("XX_VCN_BalanceByPartner_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_BalanceByPartner_ID", Integer.valueOf(XX_VCN_BalanceByPartner_ID));
        
    }
    
    /** Get XX_VCN_BalanceByPartner_ID.
    @return XX_VCN_BalanceByPartner_ID */
    public int getXX_VCN_BalanceByPartner_ID() 
    {
        return get_ValueAsInt("XX_VCN_BalanceByPartner_ID");
        
    }
    
    
}
