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
package compiere.model.promociones;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VSI_DetailPromPOS
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VSI_DetailPromPOS extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VSI_DetailPromPOS_ID id
    @param trx transaction
    */
    public X_XX_VSI_DetailPromPOS (Ctx ctx, int XX_VSI_DetailPromPOS_ID, Trx trx)
    {
        super (ctx, XX_VSI_DetailPromPOS_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VSI_DetailPromPOS_ID == 0)
        {
            setXX_VSI_DetailPromPOS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VSI_DetailPromPOS (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27612590786789L;
    /** Last Updated Timestamp 2012-02-28 17:14:30.0 */
    public static final long updatedMS = 1330465470000L;
    /** AD_Table_ID=1000409 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VSI_DetailPromPOS");
        
    }
    ;
    
    /** TableName=XX_VSI_DetailPromPOS */
    public static final String Table_Name="XX_VSI_DetailPromPOS";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Department Code.
    @param XX_DepartmentCode Codigo de Departamento */
    public void setXX_DepartmentCode (String XX_DepartmentCode)
    {
        set_Value ("XX_DepartmentCode", XX_DepartmentCode);
        
    }
    
    /** Get Department Code.
    @return Codigo de Departamento */
    public String getXX_DepartmentCode() 
    {
        return (String)get_Value("XX_DepartmentCode");
        
    }
    
    /** Set Discount Amount.
    @param XX_DiscountAmount Discount Amount */
    public void setXX_DiscountAmount (java.math.BigDecimal XX_DiscountAmount)
    {
        set_Value ("XX_DiscountAmount", XX_DiscountAmount);
        
    }
    
    /** Get Discount Amount.
    @return Discount Amount */
    public java.math.BigDecimal getXX_DiscountAmount() 
    {
        return get_ValueAsBigDecimal("XX_DiscountAmount");
        
    }
    
    /** Set Discount Rate.
    @param XX_DiscountRate Discount Rate */
    public void setXX_DiscountRate (int XX_DiscountRate)
    {
        set_Value ("XX_DiscountRate", Integer.valueOf(XX_DiscountRate));
        
    }
    
    /** Get Discount Rate.
    @return Discount Rate */
    public int getXX_DiscountRate() 
    {
        return get_ValueAsInt("XX_DiscountRate");
        
    }
    
    /** Set Line Code.
    @param XX_LineCode Código de línea */
    public void setXX_LineCode (String XX_LineCode)
    {
        set_Value ("XX_LineCode", XX_LineCode);
        
    }
    
    /** Get Line Code.
    @return Código de línea */
    public String getXX_LineCode() 
    {
        return (String)get_Value("XX_LineCode");
        
    }
    
    /** Set Promotion ID.
    @param XX_VMR_Promotion_ID Promotion ID */
    public void setXX_VMR_Promotion_ID (int XX_VMR_Promotion_ID)
    {
        if (XX_VMR_Promotion_ID <= 0) set_Value ("XX_VMR_Promotion_ID", null);
        else
        set_Value ("XX_VMR_Promotion_ID", Integer.valueOf(XX_VMR_Promotion_ID));
        
    }
    
    /** Get Promotion ID.
    @return Promotion ID */
    public int getXX_VMR_Promotion_ID() 
    {
        return get_ValueAsInt("XX_VMR_Promotion_ID");
        
    }
    
    /** Set XX_VSI_DetailPromPOS_ID.
    @param XX_VSI_DetailPromPOS_ID XX_VSI_DetailPromPOS_ID */
    public void setXX_VSI_DetailPromPOS_ID (int XX_VSI_DetailPromPOS_ID)
    {
        if (XX_VSI_DetailPromPOS_ID < 1) throw new IllegalArgumentException ("XX_VSI_DetailPromPOS_ID is mandatory.");
        set_ValueNoCheck ("XX_VSI_DetailPromPOS_ID", Integer.valueOf(XX_VSI_DetailPromPOS_ID));
        
    }
    
    /** Get XX_VSI_DetailPromPOS_ID.
    @return XX_VSI_DetailPromPOS_ID */
    public int getXX_VSI_DetailPromPOS_ID() 
    {
        return get_ValueAsInt("XX_VSI_DetailPromPOS_ID");
        
    }
    
    
}
