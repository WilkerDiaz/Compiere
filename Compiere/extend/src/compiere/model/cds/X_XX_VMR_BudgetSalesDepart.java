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
/** Generated Model for XX_VMR_BudgetSalesDepart
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_BudgetSalesDepart extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_BudgetSalesDepart_ID id
    @param trx transaction
    */
    public X_XX_VMR_BudgetSalesDepart (Ctx ctx, int XX_VMR_BudgetSalesDepart_ID, Trx trx)
    {
        super (ctx, XX_VMR_BudgetSalesDepart_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_BudgetSalesDepart_ID == 0)
        {
            setXX_VMR_BudgetSalesDepart_ID (0);
            setXX_VMR_RealComercialBudget_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_BudgetSalesDepart (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27558925864789L;
    /** Last Updated Timestamp 2010-06-17 14:19:08.0 */
    public static final long updatedMS = 1276800548000L;
    /** AD_Table_ID=1000299 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_BudgetSalesDepart");
        
    }
    ;
    
    /** TableName=XX_VMR_BudgetSalesDepart */
    public static final String Table_Name="XX_VMR_BudgetSalesDepart";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Department Historic Weight.
    @param XX_DepartmentHistoricWeight Department Historic Weight */
    public void setXX_DepartmentHistoricWeight (java.math.BigDecimal XX_DepartmentHistoricWeight)
    {
        set_Value ("XX_DepartmentHistoricWeight", XX_DepartmentHistoricWeight);
        
    }
    
    /** Get Department Historic Weight.
    @return Department Historic Weight */
    public java.math.BigDecimal getXX_DepartmentHistoricWeight() 
    {
        return get_ValueAsBigDecimal("XX_DepartmentHistoricWeight");
        
    }
    
    /** Set Sales.
    @param XX_SalesDistribution Sales */
    public void setXX_SalesDistribution (java.math.BigDecimal XX_SalesDistribution)
    {
        set_Value ("XX_SalesDistribution", XX_SalesDistribution);
        
    }
    
    /** Get Sales.
    @return Sales */
    public java.math.BigDecimal getXX_SalesDistribution() 
    {
        return get_ValueAsBigDecimal("XX_SalesDistribution");
        
    }
    
    /** Set XX_VMR_BudgetSalesDepart_ID.
    @param XX_VMR_BudgetSalesDepart_ID XX_VMR_BudgetSalesDepart_ID */
    public void setXX_VMR_BudgetSalesDepart_ID (int XX_VMR_BudgetSalesDepart_ID)
    {
        if (XX_VMR_BudgetSalesDepart_ID < 1) throw new IllegalArgumentException ("XX_VMR_BudgetSalesDepart_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_BudgetSalesDepart_ID", Integer.valueOf(XX_VMR_BudgetSalesDepart_ID));
        
    }
    
    /** Get XX_VMR_BudgetSalesDepart_ID.
    @return XX_VMR_BudgetSalesDepart_ID */
    public int getXX_VMR_BudgetSalesDepart_ID() 
    {
        return get_ValueAsInt("XX_VMR_BudgetSalesDepart_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
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
