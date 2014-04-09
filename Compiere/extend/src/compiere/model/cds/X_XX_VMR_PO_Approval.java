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
/** Generated Model for XX_VMR_PO_Approval
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PO_Approval extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PO_Approval_ID id
    @param trx transaction
    */
    public X_XX_VMR_PO_Approval (Ctx ctx, int XX_VMR_PO_Approval_ID, Trx trx)
    {
        super (ctx, XX_VMR_PO_Approval_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PO_Approval_ID == 0)
        {
            setC_Order_ID (0);
            setXX_Limit (Env.ZERO);
            setXX_LimitTotal (Env.ZERO);
            setXX_VMR_Department_ID (0);
            setXX_VMR_Line_ID (0);
            setXX_VMR_PO_APPROVAL_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PO_Approval (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27605406900789L;
    /** Last Updated Timestamp 2011-12-07 13:43:04.0 */
    public static final long updatedMS = 1323281584000L;
    /** AD_Table_ID=1000213 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PO_Approval");
        
    }
    ;
    
    /** TableName=XX_VMR_PO_Approval */
    public static final String Table_Name="XX_VMR_PO_Approval";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID < 1) throw new IllegalArgumentException ("C_Order_ID is mandatory.");
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
    }
    
    /** Set Limit.
    @param XX_Limit Limite de compra por departamento linea y seccion */
    public void setXX_Limit (java.math.BigDecimal XX_Limit)
    {
        if (XX_Limit == null) throw new IllegalArgumentException ("XX_Limit is mandatory.");
        set_Value ("XX_Limit", XX_Limit);
        
    }
    
    /** Get Limit.
    @return Limite de compra por departamento linea y seccion */
    public java.math.BigDecimal getXX_Limit() 
    {
        return get_ValueAsBigDecimal("XX_Limit");
        
    }
    
    /** Set Total Amount Purchase Order.
    @param XX_LimitTotal Limit Calculated for department, line and section */
    public void setXX_LimitTotal (java.math.BigDecimal XX_LimitTotal)
    {
        if (XX_LimitTotal == null) throw new IllegalArgumentException ("XX_LimitTotal is mandatory.");
        set_Value ("XX_LimitTotal", XX_LimitTotal);
        
    }
    
    /** Get Total Amount Purchase Order.
    @return Limit Calculated for department, line and section */
    public java.math.BigDecimal getXX_LimitTotal() 
    {
        return get_ValueAsBigDecimal("XX_LimitTotal");
        
    }
    
    /** Set Percentage Excess.
    @param XX_PercentageExcess Percentage Excess */
    public void setXX_PercentageExcess (java.math.BigDecimal XX_PercentageExcess)
    {
        set_Value ("XX_PercentageExcess", XX_PercentageExcess);
        
    }
    
    /** Get Percentage Excess.
    @return Percentage Excess */
    public java.math.BigDecimal getXX_PercentageExcess() 
    {
        return get_ValueAsBigDecimal("XX_PercentageExcess");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID < 1) throw new IllegalArgumentException ("XX_VMR_Department_ID is mandatory.");
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID < 1) throw new IllegalArgumentException ("XX_VMR_Line_ID is mandatory.");
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set XX_VMR_PO_APPROVAL_ID.
    @param XX_VMR_PO_APPROVAL_ID XX_VMR_PO_APPROVAL_ID */
    public void setXX_VMR_PO_APPROVAL_ID (int XX_VMR_PO_APPROVAL_ID)
    {
        if (XX_VMR_PO_APPROVAL_ID < 1) throw new IllegalArgumentException ("XX_VMR_PO_APPROVAL_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PO_APPROVAL_ID", Integer.valueOf(XX_VMR_PO_APPROVAL_ID));
        
    }
    
    /** Get XX_VMR_PO_APPROVAL_ID.
    @return XX_VMR_PO_APPROVAL_ID */
    public int getXX_VMR_PO_APPROVAL_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_APPROVAL_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    
}
