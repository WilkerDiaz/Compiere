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
/** Generated Model for XX_VCN_ISLRAmount
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_ISLRAmount extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_ISLRAmount_ID id
    @param trx transaction
    */
    public X_XX_VCN_ISLRAmount (Ctx ctx, int XX_VCN_ISLRAmount_ID, Trx trx)
    {
        super (ctx, XX_VCN_ISLRAmount_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_ISLRAmount_ID == 0)
        {
            setC_Invoice_ID (0);
            setXX_RetainedAmount (Env.ZERO);
            setXX_VCN_ISLRAmount_ID (0);
            setXX_VCN_ISLRRetention_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_ISLRAmount (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27620539750789L;
    /** Last Updated Timestamp 2012-05-30 17:17:14.0 */
    public static final long updatedMS = 1338414434000L;
    /** AD_Table_ID=1002660 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_ISLRAmount");
        
    }
    ;
    
    /** TableName=XX_VCN_ISLRAmount */
    public static final String Table_Name="XX_VCN_ISLRAmount";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Retained Amount.
    @param XX_RetainedAmount Retained Amount */
    public void setXX_RetainedAmount (java.math.BigDecimal XX_RetainedAmount)
    {
        if (XX_RetainedAmount == null) throw new IllegalArgumentException ("XX_RetainedAmount is mandatory.");
        set_Value ("XX_RetainedAmount", XX_RetainedAmount);
        
    }
    
    /** Get Retained Amount.
    @return Retained Amount */
    public java.math.BigDecimal getXX_RetainedAmount() 
    {
        return get_ValueAsBigDecimal("XX_RetainedAmount");
        
    }
    
    /** Set XX_VCN_ISLRAmount_ID.
    @param XX_VCN_ISLRAmount_ID XX_VCN_ISLRAmount_ID */
    public void setXX_VCN_ISLRAmount_ID (int XX_VCN_ISLRAmount_ID)
    {
        if (XX_VCN_ISLRAmount_ID < 1) throw new IllegalArgumentException ("XX_VCN_ISLRAmount_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_ISLRAmount_ID", Integer.valueOf(XX_VCN_ISLRAmount_ID));
        
    }
    
    /** Get XX_VCN_ISLRAmount_ID.
    @return XX_VCN_ISLRAmount_ID */
    public int getXX_VCN_ISLRAmount_ID() 
    {
        return get_ValueAsInt("XX_VCN_ISLRAmount_ID");
        
    }
    
    /** Set ISRL Retention.
    @param XX_VCN_ISLRRetention_ID ISRL Retention */
    public void setXX_VCN_ISLRRetention_ID (int XX_VCN_ISLRRetention_ID)
    {
        if (XX_VCN_ISLRRetention_ID < 1) throw new IllegalArgumentException ("XX_VCN_ISLRRetention_ID is mandatory.");
        set_Value ("XX_VCN_ISLRRetention_ID", Integer.valueOf(XX_VCN_ISLRRetention_ID));
        
    }
    
    /** Get ISRL Retention.
    @return ISRL Retention */
    public int getXX_VCN_ISLRRetention_ID() 
    {
        return get_ValueAsInt("XX_VCN_ISLRRetention_ID");
        
    }
    
    
}
