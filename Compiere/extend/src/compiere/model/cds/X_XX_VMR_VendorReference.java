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
/** Generated Model for XX_VMR_VendorReference
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_VendorReference extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_VendorReference_ID id
    @param trx transaction
    */
    public X_XX_VMR_VendorReference (Ctx ctx, int XX_VMR_VendorReference_ID, Trx trx)
    {
        super (ctx, XX_VMR_VendorReference_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_VendorReference_ID == 0)
        {
            setC_Order_ID (0);	// @C_Order_ID@
            setXX_VMR_VENDORREFERENCE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_VendorReference (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27537850811789L;
    /** Last Updated Timestamp 2009-10-16 16:08:15.0 */
    public static final long updatedMS = 1255725495000L;
    /** AD_Table_ID=1000173 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_VendorReference");
        
    }
    ;
    
    /** TableName=XX_VMR_VendorReference */
    public static final String Table_Name="XX_VMR_VendorReference";
    
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
    
    /** Set Date.
    @param XX_DATE Date */
    public void setXX_DATE (String XX_DATE)
    {
        set_Value ("XX_DATE", XX_DATE);
        
    }
    
    /** Get Date.
    @return Date */
    public String getXX_DATE() 
    {
        return (String)get_Value("XX_DATE");
        
    }
    
    /** Set Final Inventory.
    @param XX_FINALINVENTORY Final Inventory */
    public void setXX_FINALINVENTORY (int XX_FINALINVENTORY)
    {
        set_Value ("XX_FINALINVENTORY", Integer.valueOf(XX_FINALINVENTORY));
        
    }
    
    /** Get Final Inventory.
    @return Final Inventory */
    public int getXX_FINALINVENTORY() 
    {
        return get_ValueAsInt("XX_FINALINVENTORY");
        
    }
    
    /** Set Sales Quantity.
    @param XX_SALESQUANTITY Cantidad de Ventas */
    public void setXX_SALESQUANTITY (int XX_SALESQUANTITY)
    {
        set_Value ("XX_SALESQUANTITY", Integer.valueOf(XX_SALESQUANTITY));
        
    }
    
    /** Get Sales Quantity.
    @return Cantidad de Ventas */
    public int getXX_SALESQUANTITY() 
    {
        return get_ValueAsInt("XX_SALESQUANTITY");
        
    }
    
    /** Set Store.
    @param XX_STORES Store */
    public void setXX_STORES (int XX_STORES)
    {
        set_Value ("XX_STORES", Integer.valueOf(XX_STORES));
        
    }
    
    /** Get Store.
    @return Store */
    public int getXX_STORES() 
    {
        return get_ValueAsInt("XX_STORES");
        
    }
    
    /** Set Vendor Reference.
    @param XX_VMR_PO_LineRefProv_ID Vendor Reference */
    public void setXX_VMR_PO_LineRefProv_ID (int XX_VMR_PO_LineRefProv_ID)
    {
        if (XX_VMR_PO_LineRefProv_ID <= 0) set_ValueNoCheck ("XX_VMR_PO_LineRefProv_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_PO_LineRefProv_ID", Integer.valueOf(XX_VMR_PO_LineRefProv_ID));
        
    }
    
    /** Get Vendor Reference.
    @return Vendor Reference */
    public int getXX_VMR_PO_LineRefProv_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_LineRefProv_ID");
        
    }
    
    /** Set XX_VMR_VENDORREFERENCE_ID.
    @param XX_VMR_VENDORREFERENCE_ID XX_VMR_VENDORREFERENCE_ID */
    public void setXX_VMR_VENDORREFERENCE_ID (int XX_VMR_VENDORREFERENCE_ID)
    {
        if (XX_VMR_VENDORREFERENCE_ID < 1) throw new IllegalArgumentException ("XX_VMR_VENDORREFERENCE_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_VENDORREFERENCE_ID", Integer.valueOf(XX_VMR_VENDORREFERENCE_ID));
        
    }
    
    /** Get XX_VMR_VENDORREFERENCE_ID.
    @return XX_VMR_VENDORREFERENCE_ID */
    public int getXX_VMR_VENDORREFERENCE_ID() 
    {
        return get_ValueAsInt("XX_VMR_VENDORREFERENCE_ID");
        
    }
    
    
}
