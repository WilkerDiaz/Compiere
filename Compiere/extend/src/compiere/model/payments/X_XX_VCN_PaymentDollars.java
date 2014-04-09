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
/** Generated Model for XX_VCN_PaymentDollars
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_PaymentDollars extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_PaymentDollars_ID id
    @param trx transaction
    */
    public X_XX_VCN_PaymentDollars (Ctx ctx, int XX_VCN_PaymentDollars_ID, Trx trx)
    {
        super (ctx, XX_VCN_PaymentDollars_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_PaymentDollars_ID == 0)
        {
            setXX_Concept (null);
            setXX_RateType (null);
            setXX_VCN_PaymentDollars_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_PaymentDollars (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27643946533789L;
    /** Last Updated Timestamp 2013-02-25 15:10:17.0 */
    public static final long updatedMS = 1361821217000L;
    /** AD_Table_ID=1002663 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_PaymentDollars");
        
    }
    ;
    
    /** TableName=XX_VCN_PaymentDollars */
    public static final String Table_Name="XX_VCN_PaymentDollars";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bank.
    @param C_Bank_ID Bank */
    public void setC_Bank_ID (int C_Bank_ID)
    {
        if (C_Bank_ID <= 0) set_Value ("C_Bank_ID", null);
        else
        set_Value ("C_Bank_ID", Integer.valueOf(C_Bank_ID));
        
    }
    
    /** Get Bank.
    @return Bank */
    public int getC_Bank_ID() 
    {
        return get_ValueAsInt("C_Bank_ID");
        
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
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Date.
    @param Date1 Date when business is not conducted */
    public void setDate1 (Timestamp Date1)
    {
        set_Value ("Date1", Date1);
        
    }
    
    /** Get Date.
    @return Date when business is not conducted */
    public Timestamp getDate1() 
    {
        return (Timestamp)get_Value("Date1");
        
    }
    
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Amount Foreign.
    @param XX_AmountForeign Payment in foreign currency */
    public void setXX_AmountForeign (java.math.BigDecimal XX_AmountForeign)
    {
        set_Value ("XX_AmountForeign", XX_AmountForeign);
        
    }
    
    /** Get Amount Foreign.
    @return Payment in foreign currency */
    public java.math.BigDecimal getXX_AmountForeign() 
    {
        return get_ValueAsBigDecimal("XX_AmountForeign");
        
    }
    
    /** Set Amount Local.
    @param XX_AmountLocal Payment in local currency */
    public void setXX_AmountLocal (java.math.BigDecimal XX_AmountLocal)
    {
        set_Value ("XX_AmountLocal", XX_AmountLocal);
        
    }
    
    /** Get Amount Local.
    @return Payment in local currency */
    public java.math.BigDecimal getXX_AmountLocal() 
    {
        return get_ValueAsBigDecimal("XX_AmountLocal");
        
    }
    
    /** Set Average Exchange.
    @param XX_AverageExchange Average Exchange */
    public void setXX_AverageExchange (java.math.BigDecimal XX_AverageExchange)
    {
        set_Value ("XX_AverageExchange", XX_AverageExchange);
        
    }
    
    /** Get Average Exchange.
    @return Average Exchange */
    public java.math.BigDecimal getXX_AverageExchange() 
    {
        return get_ValueAsBigDecimal("XX_AverageExchange");
        
    }
    
    /** Set Balance Foreign.
    @param XX_BalanceForeign Balance Foreign */
    public void setXX_BalanceForeign (java.math.BigDecimal XX_BalanceForeign)
    {
        set_Value ("XX_BalanceForeign", XX_BalanceForeign);
        
    }
    
    /** Get Balance Foreign.
    @return Balance Foreign */
    public java.math.BigDecimal getXX_BalanceForeign() 
    {
        return get_ValueAsBigDecimal("XX_BalanceForeign");
        
    }
    
    /** Set Balance Local.
    @param XX_BalanceLocal Balance Local */
    public void setXX_BalanceLocal (java.math.BigDecimal XX_BalanceLocal)
    {
        set_Value ("XX_BalanceLocal", XX_BalanceLocal);
        
    }
    
    /** Get Balance Local.
    @return Balance Local */
    public java.math.BigDecimal getXX_BalanceLocal() 
    {
        return get_ValueAsBigDecimal("XX_BalanceLocal");
        
    }
    
    /** Set Concept.
    @param XX_Concept Concept */
    public void setXX_Concept (String XX_Concept)
    {
        if (XX_Concept == null) throw new IllegalArgumentException ("XX_Concept is mandatory.");
        set_Value ("XX_Concept", XX_Concept);
        
    }
    
    /** Get Concept.
    @return Concept */
    public String getXX_Concept() 
    {
        return (String)get_Value("XX_Concept");
        
    }
    
    /** Set Consolidated Rate.
    @param XX_ConsolidatedRate Consolidated Rate */
    public void setXX_ConsolidatedRate (boolean XX_ConsolidatedRate)
    {
        set_Value ("XX_ConsolidatedRate", Boolean.valueOf(XX_ConsolidatedRate));
        
    }
    
    /** Get Consolidated Rate.
    @return Consolidated Rate */
    public boolean isXX_ConsolidatedRate() 
    {
        return get_ValueAsBoolean("XX_ConsolidatedRate");
        
    }
    
    /** Set Final Balance.
    @param XX_FinalBalance Final Balance */
    public void setXX_FinalBalance (boolean XX_FinalBalance)
    {
        set_Value ("XX_FinalBalance", Boolean.valueOf(XX_FinalBalance));
        
    }
    
    /** Get Final Balance.
    @return Final Balance */
    public boolean isXX_FinalBalance() 
    {
        return get_ValueAsBoolean("XX_FinalBalance");
        
    }
    
    /** Set Initial Balance.
    @param XX_InitialBalance Initial Balance */
    public void setXX_InitialBalance (boolean XX_InitialBalance)
    {
        set_Value ("XX_InitialBalance", Boolean.valueOf(XX_InitialBalance));
        
    }
    
    /** Get Initial Balance.
    @return Initial Balance */
    public boolean isXX_InitialBalance() 
    {
        return get_ValueAsBoolean("XX_InitialBalance");
        
    }
    
    /** Set Is Amount.
    @param XX_IsAmount Is Amount */
    public void setXX_IsAmount (boolean XX_IsAmount)
    {
        set_Value ("XX_IsAmount", Boolean.valueOf(XX_IsAmount));
        
    }
    
    /** Get Is Amount.
    @return Is Amount */
    public boolean isXX_IsAmount() 
    {
        return get_ValueAsBoolean("XX_IsAmount");
        
    }
    
    /** Set Rate.
    @param XX_Rate Terifa  */
    public void setXX_Rate (java.math.BigDecimal XX_Rate)
    {
        set_Value ("XX_Rate", XX_Rate);
        
    }
    
    /** Get Rate.
    @return Terifa  */
    public java.math.BigDecimal getXX_Rate() 
    {
        return get_ValueAsBigDecimal("XX_Rate");
        
    }
    
    /** Bonus = B */
    public static final String XX_RATETYPE_Bonus = X_Ref_XX_RateTypeList.BONUS.getValue();
    /** Diff Ops = O */
    public static final String XX_RATETYPE_DiffOps = X_Ref_XX_RateTypeList.DIFF_OPS.getValue();
    /** Sitme = S */
    public static final String XX_RATETYPE_Sitme = X_Ref_XX_RateTypeList.SITME.getValue();
    /** Set Rate Type.
    @param XX_RateType Rate Type */
    public void setXX_RateType (String XX_RateType)
    {
        if (XX_RateType == null) throw new IllegalArgumentException ("XX_RateType is mandatory");
        if (!X_Ref_XX_RateTypeList.isValid(XX_RateType))
        throw new IllegalArgumentException ("XX_RateType Invalid value - " + XX_RateType + " - Reference_ID=1001859 - B - O - S");
        set_Value ("XX_RateType", XX_RateType);
        
    }
    
    /** Get Rate Type.
    @return Rate Type */
    public String getXX_RateType() 
    {
        return (String)get_Value("XX_RateType");
        
    }
    
    /** Average Exchange from Previos Record = CP */
    public static final String XX_RATETYPESITME_AverageExchangeFromPreviosRecord = X_Ref_XX_RateTypeSitmeList.AVERAGE_EXCHANGE_FROM_PREVIOS_RECORD.getValue();
    /** User Input = IU */
    public static final String XX_RATETYPESITME_UserInput = X_Ref_XX_RateTypeSitmeList.USER_INPUT.getValue();
    /** Amount Bs/Amount $ = MS */
    public static final String XX_RATETYPESITME_AmountBsAmount$ = X_Ref_XX_RateTypeSitmeList.AMOUNT_BS_AMOUNT$.getValue();
    /** Set Rate Type Sitme.
    @param XX_RateTypeSitme Rate Type Sitme */
    public void setXX_RateTypeSitme (String XX_RateTypeSitme)
    {
        if (!X_Ref_XX_RateTypeSitmeList.isValid(XX_RateTypeSitme))
        throw new IllegalArgumentException ("XX_RateTypeSitme Invalid value - " + XX_RateTypeSitme + " - Reference_ID=1001860 - CP - IU - MS");
        set_Value ("XX_RateTypeSitme", XX_RateTypeSitme);
        
    }
    
    /** Get Rate Type Sitme.
    @return Rate Type Sitme */
    public String getXX_RateTypeSitme() 
    {
        return (String)get_Value("XX_RateTypeSitme");
        
    }
    
    /** Set XX_VCN_PaymentDollars_ID.
    @param XX_VCN_PaymentDollars_ID XX_VCN_PaymentDollars_ID */
    public void setXX_VCN_PaymentDollars_ID (int XX_VCN_PaymentDollars_ID)
    {
        if (XX_VCN_PaymentDollars_ID < 1) throw new IllegalArgumentException ("XX_VCN_PaymentDollars_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_PaymentDollars_ID", Integer.valueOf(XX_VCN_PaymentDollars_ID));
        
    }
    
    /** Get XX_VCN_PaymentDollars_ID.
    @return XX_VCN_PaymentDollars_ID */
    public int getXX_VCN_PaymentDollars_ID() 
    {
        return get_ValueAsInt("XX_VCN_PaymentDollars_ID");
        
    }
    
    
}
