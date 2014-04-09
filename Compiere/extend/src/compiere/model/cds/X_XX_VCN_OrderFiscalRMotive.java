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
/** Generated Model for XX_VCN_OrderFiscalRMotive
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_OrderFiscalRMotive extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_OrderFiscalRMotive_ID id
    @param trx transaction
    */
    public X_XX_VCN_OrderFiscalRMotive (Ctx ctx, int XX_VCN_OrderFiscalRMotive_ID, Trx trx)
    {
        super (ctx, XX_VCN_OrderFiscalRMotive_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_OrderFiscalRMotive_ID == 0)
        {
            setC_Order_ID (0);
            setXX_VCN_OrderFiscalRMotive_ID (0);
            setXX_VMR_CancellationMotive_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_OrderFiscalRMotive (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27547856637789L;
    /** Last Updated Timestamp 2010-02-09 11:32:01.0 */
    public static final long updatedMS = 1265731321000L;
    /** AD_Table_ID=1000224 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_OrderFiscalRMotive");
        
    }
    ;
    
    /** TableName=XX_VCN_OrderFiscalRMotive */
    public static final String Table_Name="XX_VCN_OrderFiscalRMotive";
    
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
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Order / Fiscal Data Rejection Motive.
    @param XX_VCN_OrderFiscalRMotive_ID Order / Fiscal Data Rejection Motive */
    public void setXX_VCN_OrderFiscalRMotive_ID (int XX_VCN_OrderFiscalRMotive_ID)
    {
        if (XX_VCN_OrderFiscalRMotive_ID < 1) throw new IllegalArgumentException ("XX_VCN_OrderFiscalRMotive_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_OrderFiscalRMotive_ID", Integer.valueOf(XX_VCN_OrderFiscalRMotive_ID));
        
    }
    
    /** Get Order / Fiscal Data Rejection Motive.
    @return Order / Fiscal Data Rejection Motive */
    public int getXX_VCN_OrderFiscalRMotive_ID() 
    {
        return get_ValueAsInt("XX_VCN_OrderFiscalRMotive_ID");
        
    }
    
    /** Set Motive.
    @param XX_VMR_CancellationMotive_ID Motivo de cancelaciones, devoluciones */
    public void setXX_VMR_CancellationMotive_ID (int XX_VMR_CancellationMotive_ID)
    {
        if (XX_VMR_CancellationMotive_ID < 1) throw new IllegalArgumentException ("XX_VMR_CancellationMotive_ID is mandatory.");
        set_Value ("XX_VMR_CancellationMotive_ID", Integer.valueOf(XX_VMR_CancellationMotive_ID));
        
    }
    
    /** Get Motive.
    @return Motivo de cancelaciones, devoluciones */
    public int getXX_VMR_CancellationMotive_ID() 
    {
        return get_ValueAsInt("XX_VMR_CancellationMotive_ID");
        
    }
    
    
}
