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
import java.sql.ResultSet;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Trx;
/** Generated Model for XX_RequisitionApproval
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id$ */
public class X_XX_RequisitionApproval extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_RequisitionApproval_ID id
    @param trxName transaction
    */
    public X_XX_RequisitionApproval (Ctx ctx, int XX_RequisitionApproval_ID, Trx trx)
    {
        super (ctx, XX_RequisitionApproval_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_RequisitionApproval_ID == 0)
        {
            setC_Order_ID (0);	// @C_Order_ID@
            setXX_DEPARTMENT (0);
            setXX_Limit (Env.ZERO);
            setXX_LimitTotal (Env.ZERO);
            setXX_Line (0);
            setXX_REQUISITIONAPPROVAL_ID (0);
            setXX_SECTION (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_XX_RequisitionApproval (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27543088310789L;
    /** Last Updated Timestamp 2009-12-16 12:29:54.0 */
    public static final long updatedMS = 1260962994000L;
    /** AD_Table_ID=1000109 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_RequisitionApproval");
        
    }
    ;
    
    /** TableName=XX_RequisitionApproval */
    public static final String Table_Name="XX_RequisitionApproval";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"XX_RequisitionApproval");
    

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
    
    /** Set Department.
    @param XX_DEPARTMENT Departamento */
    public void setXX_DEPARTMENT (int XX_DEPARTMENT)
    {
        set_Value ("XX_DEPARTMENT", Integer.valueOf(XX_DEPARTMENT));
        
    }
    
    /** Get Department.
    @return Departamento */
    public int getXX_DEPARTMENT() 
    {
        return get_ValueAsInt("XX_DEPARTMENT");
        
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
    
    /** Set Total BsF O/C.
    @param XX_LimitTotal Limite calculado por departamento linea y seccion */
    public void setXX_LimitTotal (java.math.BigDecimal XX_LimitTotal)
    {
        if (XX_LimitTotal == null) throw new IllegalArgumentException ("XX_LimitTotal is mandatory.");
        set_Value ("XX_LimitTotal", XX_LimitTotal);
        
    }
    
    /** Get Total BsF O/C.
    @return Limite calculado por departamento linea y seccion */
    public java.math.BigDecimal getXX_LimitTotal() 
    {
        return get_ValueAsBigDecimal("XX_LimitTotal");
        
    }
    
    /** Set Line Code.
    @param XX_Line Código de línea */
    public void setXX_Line (int XX_Line)
    {
        set_Value ("XX_Line", Integer.valueOf(XX_Line));
        
    }
    
    /** Get Line Code.
    @return Código de línea */
    public int getXX_Line() 
    {
        return get_ValueAsInt("XX_Line");
        
    }
    
    /** Set XX_REQUISITIONAPPROVAL_ID.
    @param XX_REQUISITIONAPPROVAL_ID XX_REQUISITIONAPPROVAL_ID */
    public void setXX_REQUISITIONAPPROVAL_ID (int XX_REQUISITIONAPPROVAL_ID)
    {
        if (XX_REQUISITIONAPPROVAL_ID < 1) throw new IllegalArgumentException ("XX_REQUISITIONAPPROVAL_ID is mandatory.");
        set_ValueNoCheck ("XX_REQUISITIONAPPROVAL_ID", Integer.valueOf(XX_REQUISITIONAPPROVAL_ID));
        
    }
    
    /** Get XX_REQUISITIONAPPROVAL_ID.
    @return XX_REQUISITIONAPPROVAL_ID */
    public int getXX_REQUISITIONAPPROVAL_ID() 
    {
        return get_ValueAsInt("XX_REQUISITIONAPPROVAL_ID");
        
    }
    
    /** Set Section.
    @param XX_SECTION Section */
    public void setXX_SECTION (int XX_SECTION)
    {
        set_Value ("XX_SECTION", Integer.valueOf(XX_SECTION));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_SECTION() 
    {
        return get_ValueAsInt("XX_SECTION");
        
    }
    
    
}
