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
/** Generated Model for XX_VMR_DepartmentBudget
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DepartmentBudget extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DepartmentBudget_ID id
    @param trx transaction
    */
    public X_XX_VMR_DepartmentBudget (Ctx ctx, int XX_VMR_DepartmentBudget_ID, Trx trx)
    {
        super (ctx, XX_VMR_DepartmentBudget_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DepartmentBudget_ID == 0)
        {
            setXX_VMR_ComercialBudget_ID (0);
            setXX_VMR_DepartmentBudget_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_DepartmentBudget (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27629583183789L;
    /** Last Updated Timestamp 2012-09-12 09:21:07.0 */
    public static final long updatedMS = 1347457867000L;
    /** AD_Table_ID=1000295 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DepartmentBudget");
        
    }
    ;
    
    /** TableName=XX_VMR_DepartmentBudget */
    public static final String Table_Name="XX_VMR_DepartmentBudget";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Min Amount.
    @param XX_MinAmount Min Amount */
    public void setXX_MinAmount (java.math.BigDecimal XX_MinAmount)
    {
        set_Value ("XX_MinAmount", XX_MinAmount);
        
    }
    
    /** Get Min Amount.
    @return Min Amount */
    public java.math.BigDecimal getXX_MinAmount() 
    {
        return get_ValueAsBigDecimal("XX_MinAmount");
        
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
    
    /** Set Department Budget.
    @param XX_VMR_DepartmentBudget_ID Department Budget */
    public void setXX_VMR_DepartmentBudget_ID (int XX_VMR_DepartmentBudget_ID)
    {
        if (XX_VMR_DepartmentBudget_ID < 1) throw new IllegalArgumentException ("XX_VMR_DepartmentBudget_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DepartmentBudget_ID", Integer.valueOf(XX_VMR_DepartmentBudget_ID));
        
    }
    
    /** Get Department Budget.
    @return Department Budget */
    public int getXX_VMR_DepartmentBudget_ID() 
    {
        return get_ValueAsInt("XX_VMR_DepartmentBudget_ID");
        
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
    
    
}
