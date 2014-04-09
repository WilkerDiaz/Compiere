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
/** Generated Model for XX_VMR_RealComercialBudget
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_RealComercialBudget extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_RealComercialBudget_ID id
    @param trx transaction
    */
    public X_XX_VMR_RealComercialBudget (Ctx ctx, int XX_VMR_RealComercialBudget_ID, Trx trx)
    {
        super (ctx, XX_VMR_RealComercialBudget_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_RealComercialBudget_ID == 0)
        {
            setXX_VMR_RealComercialBudget_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_RealComercialBudget (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27558902714789L;
    /** Last Updated Timestamp 2010-06-17 07:53:18.0 */
    public static final long updatedMS = 1276777398000L;
    /** AD_Table_ID=1000296 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_RealComercialBudget");
        
    }
    ;
    
    /** TableName=XX_VMR_RealComercialBudget */
    public static final String Table_Name="XX_VMR_RealComercialBudget";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID <= 0) set_Value ("C_Period_ID", null);
        else
        set_Value ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
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
    
    /** Set Budget Sales Bs.
    @param XX_BudgetSalesBs Budget Sales Bs */
    public void setXX_BudgetSalesBs (java.math.BigDecimal XX_BudgetSalesBs)
    {
        set_Value ("XX_BudgetSalesBs", XX_BudgetSalesBs);
        
    }
    
    /** Get Budget Sales Bs.
    @return Budget Sales Bs */
    public java.math.BigDecimal getXX_BudgetSalesBs() 
    {
        return get_ValueAsBigDecimal("XX_BudgetSalesBs");
        
    }
    
    /** Set Budget Sales in Pieces.
    @param XX_BudgetSalesPieces Budget Sales in Pieces */
    public void setXX_BudgetSalesPieces (int XX_BudgetSalesPieces)
    {
        set_Value ("XX_BudgetSalesPieces", Integer.valueOf(XX_BudgetSalesPieces));
        
    }
    
    /** Get Budget Sales in Pieces.
    @return Budget Sales in Pieces */
    public int getXX_BudgetSalesPieces() 
    {
        return get_ValueAsInt("XX_BudgetSalesPieces");
        
    }
    
    /** Set Company Average Price.
    @param XX_CompanyAveragePrice Company Average Price */
    public void setXX_CompanyAveragePrice (java.math.BigDecimal XX_CompanyAveragePrice)
    {
        set_Value ("XX_CompanyAveragePrice", XX_CompanyAveragePrice);
        
    }
    
    /** Get Company Average Price.
    @return Company Average Price */
    public java.math.BigDecimal getXX_CompanyAveragePrice() 
    {
        return get_ValueAsBigDecimal("XX_CompanyAveragePrice");
        
    }
    
    /** Set XX_VMR_RealComercialBudget_ID.
    @param XX_VMR_RealComercialBudget_ID XX_VMR_RealComercialBudget_ID */
    public void setXX_VMR_RealComercialBudget_ID (int XX_VMR_RealComercialBudget_ID)
    {
        if (XX_VMR_RealComercialBudget_ID < 1) throw new IllegalArgumentException ("XX_VMR_RealComercialBudget_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_RealComercialBudget_ID", Integer.valueOf(XX_VMR_RealComercialBudget_ID));
        
    }
    
    /** Get XX_VMR_RealComercialBudget_ID.
    @return XX_VMR_RealComercialBudget_ID */
    public int getXX_VMR_RealComercialBudget_ID() 
    {
        return get_ValueAsInt("XX_VMR_RealComercialBudget_ID");
        
    }
    
    
}
