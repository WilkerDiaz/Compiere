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
package compiere.model.tag;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_DetailAdvice
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_DetailAdvice extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_DetailAdvice_ID id
    @param trx transaction
    */
    public X_XX_VCN_DetailAdvice (Ctx ctx, int XX_VCN_DetailAdvice_ID, Trx trx)
    {
        super (ctx, XX_VCN_DetailAdvice_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_DetailAdvice_ID == 0)
        {
            setValue (null);
            setXX_VCN_DETAILADVICE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_DetailAdvice (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27582516140789L;
    /** Last Updated Timestamp 2011-03-17 15:10:24.0 */
    public static final long updatedMS = 1300390824000L;
    /** AD_Table_ID=1002555 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_DetailAdvice");
        
    }
    ;
    
    /** TableName=XX_VCN_DetailAdvice */
    public static final String Table_Name="XX_VCN_DetailAdvice";
    
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
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
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
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_CENTRADISCDELIAMOUNT.
    @param XX_CENTRADISCDELIAMOUNT XX_CENTRADISCDELIAMOUNT */
    public void setXX_CENTRADISCDELIAMOUNT (java.math.BigDecimal XX_CENTRADISCDELIAMOUNT)
    {
        set_Value ("XX_CENTRADISCDELIAMOUNT", XX_CENTRADISCDELIAMOUNT);
        
    }
    
    /** Get XX_CENTRADISCDELIAMOUNT.
    @return XX_CENTRADISCDELIAMOUNT */
    public java.math.BigDecimal getXX_CENTRADISCDELIAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_CENTRADISCDELIAMOUNT");
        
    }
    
    /** Set Nro. Credit Notify.
    @param XX_CreditNotifyReturn_ID Número de Aviso de Credito */
    public void setXX_CreditNotifyReturn_ID (int XX_CreditNotifyReturn_ID)
    {
        if (XX_CreditNotifyReturn_ID <= 0) set_Value ("XX_CreditNotifyReturn_ID", null);
        else
        set_Value ("XX_CreditNotifyReturn_ID", Integer.valueOf(XX_CreditNotifyReturn_ID));
        
    }
    
    /** Get Nro. Credit Notify.
    @return Número de Aviso de Credito */
    public int getXX_CreditNotifyReturn_ID() 
    {
        return get_ValueAsInt("XX_CreditNotifyReturn_ID");
        
    }
    
    /** Set XX_DISCAFTERSALEAMOUNT.
    @param XX_DISCAFTERSALEAMOUNT XX_DISCAFTERSALEAMOUNT */
    public void setXX_DISCAFTERSALEAMOUNT (java.math.BigDecimal XX_DISCAFTERSALEAMOUNT)
    {
        set_Value ("XX_DISCAFTERSALEAMOUNT", XX_DISCAFTERSALEAMOUNT);
        
    }
    
    /** Get XX_DISCAFTERSALEAMOUNT.
    @return XX_DISCAFTERSALEAMOUNT */
    public java.math.BigDecimal getXX_DISCAFTERSALEAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_DISCAFTERSALEAMOUNT");
        
    }
    
    /** Set XX_DISCRECOGDECLAMOUNT.
    @param XX_DISCRECOGDECLAMOUNT XX_DISCRECOGDECLAMOUNT */
    public void setXX_DISCRECOGDECLAMOUNT (java.math.BigDecimal XX_DISCRECOGDECLAMOUNT)
    {
        set_Value ("XX_DISCRECOGDECLAMOUNT", XX_DISCRECOGDECLAMOUNT);
        
    }
    
    /** Get XX_DISCRECOGDECLAMOUNT.
    @return XX_DISCRECOGDECLAMOUNT */
    public java.math.BigDecimal getXX_DISCRECOGDECLAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_DISCRECOGDECLAMOUNT");
        
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
    
    /** Set XX_PubAmount.
    @param XX_PubAmount Aporte a publicidad (Monto) */
    public void setXX_PubAmount (java.math.BigDecimal XX_PubAmount)
    {
        set_Value ("XX_PubAmount", XX_PubAmount);
        
    }
    
    /** Get XX_PubAmount.
    @return Aporte a publicidad (Monto) */
    public java.math.BigDecimal getXX_PubAmount() 
    {
        return get_ValueAsBigDecimal("XX_PubAmount");
        
    }
    
    /** Set Origin Currency Cost.
    @param XX_UnitPurchasePrice Origin Currency Cost */
    public void setXX_UnitPurchasePrice (java.math.BigDecimal XX_UnitPurchasePrice)
    {
        set_Value ("XX_UnitPurchasePrice", XX_UnitPurchasePrice);
        
    }
    
    /** Get Origin Currency Cost.
    @return Origin Currency Cost */
    public java.math.BigDecimal getXX_UnitPurchasePrice() 
    {
        return get_ValueAsBigDecimal("XX_UnitPurchasePrice");
        
    }
    
    /** Set XX_VCN_DETAILADVICE_ID.
    @param XX_VCN_DETAILADVICE_ID XX_VCN_DETAILADVICE_ID */
    public void setXX_VCN_DETAILADVICE_ID (int XX_VCN_DETAILADVICE_ID)
    {
        if (XX_VCN_DETAILADVICE_ID < 1) throw new IllegalArgumentException ("XX_VCN_DETAILADVICE_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_DETAILADVICE_ID", Integer.valueOf(XX_VCN_DETAILADVICE_ID));
        
    }
    
    /** Get XX_VCN_DETAILADVICE_ID.
    @return XX_VCN_DETAILADVICE_ID */
    public int getXX_VCN_DETAILADVICE_ID() 
    {
        return get_ValueAsInt("XX_VCN_DETAILADVICE_ID");
        
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
    
    /** Set Year.
    @param XX_Year Year */
    public void setXX_Year (int XX_Year)
    {
        set_Value ("XX_Year", Integer.valueOf(XX_Year));
        
    }
    
    /** Get Year.
    @return Year */
    public int getXX_Year() 
    {
        return get_ValueAsInt("XX_Year");
        
    }
    
    
}
