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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_ComercialBudget
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_ComercialBudget extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_ComercialBudget_ID id
    @param trx transaction
    */
    public X_XX_VMR_ComercialBudget (Ctx ctx, int XX_VMR_ComercialBudget_ID, Trx trx)
    {
        super (ctx, XX_VMR_ComercialBudget_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_ComercialBudget_ID == 0)
        {
            setXX_VMR_ComercialBudget_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_ComercialBudget (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27628479713789L;
    /** Last Updated Timestamp 2012-08-30 14:49:57.0 */
    public static final long updatedMS = 1346354397000L;
    /** AD_Table_ID=1000288 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_ComercialBudget");
        
    }
    ;
    
    /** TableName=XX_VMR_ComercialBudget */
    public static final String Table_Name="XX_VMR_ComercialBudget";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Year.
    @param C_Year_ID Calendar Year */
    public void setC_Year_ID (int C_Year_ID)
    {
        if (C_Year_ID <= 0) set_Value ("C_Year_ID", null);
        else
        set_Value ("C_Year_ID", Integer.valueOf(C_Year_ID));
        
    }
    
    /** Get Year.
    @return Calendar Year */
    public int getC_Year_ID() 
    {
        return get_ValueAsInt("C_Year_ID");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Annual Inflation.
    @param XX_AnnualInflation Annual Inflation */
    public void setXX_AnnualInflation (java.math.BigDecimal XX_AnnualInflation)
    {
        set_Value ("XX_AnnualInflation", XX_AnnualInflation);
        
    }
    
    /** Get Annual Inflation.
    @return Annual Inflation */
    public java.math.BigDecimal getXX_AnnualInflation() 
    {
        return get_ValueAsBigDecimal("XX_AnnualInflation");
        
    }
    
    /** Set Decrease.
    @param XX_Decrease Decrease */
    public void setXX_Decrease (java.math.BigDecimal XX_Decrease)
    {
        set_Value ("XX_Decrease", XX_Decrease);
        
    }
    
    /** Get Decrease.
    @return Decrease */
    public java.math.BigDecimal getXX_Decrease() 
    {
        return get_ValueAsBigDecimal("XX_Decrease");
        
    }
    
    /** Set Generate Initial Scenario.
    @param XX_GenerateInitialScenario Generate Initial Scenario */
    public void setXX_GenerateInitialScenario (String XX_GenerateInitialScenario)
    {
        set_Value ("XX_GenerateInitialScenario", XX_GenerateInitialScenario);
        
    }
    
    /** Get Generate Initial Scenario.
    @return Generate Initial Scenario */
    public String getXX_GenerateInitialScenario() 
    {
        return (String)get_Value("XX_GenerateInitialScenario");
        
    }
    
    /** Set Historic Employee Discount.
    @param XX_HistoricEmployeeDiscount Historic Employee Discount */
    public void setXX_HistoricEmployeeDiscount (java.math.BigDecimal XX_HistoricEmployeeDiscount)
    {
        set_Value ("XX_HistoricEmployeeDiscount", XX_HistoricEmployeeDiscount);
        
    }
    
    /** Get Historic Employee Discount.
    @return Historic Employee Discount */
    public java.math.BigDecimal getXX_HistoricEmployeeDiscount() 
    {
        return get_ValueAsBigDecimal("XX_HistoricEmployeeDiscount");
        
    }
    
    /** Set Margin Net Growth.
    @param XX_MarginNetGrowth Margin Net Growth */
    public void setXX_MarginNetGrowth (java.math.BigDecimal XX_MarginNetGrowth)
    {
        set_Value ("XX_MarginNetGrowth", XX_MarginNetGrowth);
        
    }
    
    /** Get Margin Net Growth.
    @return Margin Net Growth */
    public java.math.BigDecimal getXX_MarginNetGrowth() 
    {
        return get_ValueAsBigDecimal("XX_MarginNetGrowth");
        
    }
    
    /** Set Sales Growth.
    @param XX_SalesGrowth Sales Growth */
    public void setXX_SalesGrowth (java.math.BigDecimal XX_SalesGrowth)
    {
        set_Value ("XX_SalesGrowth", XX_SalesGrowth);
        
    }
    
    /** Get Sales Growth.
    @return Sales Growth */
    public java.math.BigDecimal getXX_SalesGrowth() 
    {
        return get_ValueAsBigDecimal("XX_SalesGrowth");
        
    }
    
    /** Set Comercial Budget.
    @param XX_VMR_ComercialBudget_ID Comercial Budget */
    public void setXX_VMR_ComercialBudget_ID (int XX_VMR_ComercialBudget_ID)
    {
        if (XX_VMR_ComercialBudget_ID < 1) throw new IllegalArgumentException ("XX_VMR_ComercialBudget_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ComercialBudget_ID", Integer.valueOf(XX_VMR_ComercialBudget_ID));
        
    }
    
    /** Get Comercial Budget.
    @return Comercial Budget */
    public int getXX_VMR_ComercialBudget_ID() 
    {
        return get_ValueAsInt("XX_VMR_ComercialBudget_ID");
        
    }
    
    
}
