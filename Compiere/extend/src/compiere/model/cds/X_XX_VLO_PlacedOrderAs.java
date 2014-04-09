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
/** Generated Model for XX_VLO_PlacedOrderAs
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_PlacedOrderAs extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_PlacedOrderAs_ID id
    @param trx transaction
    */
    public X_XX_VLO_PlacedOrderAs (Ctx ctx, int XX_VLO_PlacedOrderAs_ID, Trx trx)
    {
        super (ctx, XX_VLO_PlacedOrderAs_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_PlacedOrderAs_ID == 0)
        {
            setXX_VLO_PlacedOrderAs_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_PlacedOrderAs (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27555650525789L;
    /** Last Updated Timestamp 2010-05-10 16:30:09.0 */
    public static final long updatedMS = 1273525209000L;
    /** AD_Table_ID=1000309 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_PlacedOrderAs");
        
    }
    ;
    
    /** TableName=XX_VLO_PlacedOrderAs */
    public static final String Table_Name="XX_VLO_PlacedOrderAs";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Complete.
    @param IsComplete It is complete */
    public void setIsComplete (boolean IsComplete)
    {
        set_Value ("IsComplete", Boolean.valueOf(IsComplete));
        
    }
    
    /** Get Complete.
    @return It is complete */
    public boolean isComplete() 
    {
        return get_ValueAsBoolean("IsComplete");
        
    }
    
    /** Set Association Number.
    @param XX_AssociationNumber Association Number */
    public void setXX_AssociationNumber (int XX_AssociationNumber)
    {
        set_Value ("XX_AssociationNumber", Integer.valueOf(XX_AssociationNumber));
        
    }
    
    /** Get Association Number.
    @return Association Number */
    public int getXX_AssociationNumber() 
    {
        return get_ValueAsInt("XX_AssociationNumber");
        
    }
    
    /** Set Placed Order Asssociation.
    @param XX_VLO_PlacedOrderAs_ID Placed Order Asssociation */
    public void setXX_VLO_PlacedOrderAs_ID (int XX_VLO_PlacedOrderAs_ID)
    {
        if (XX_VLO_PlacedOrderAs_ID < 1) throw new IllegalArgumentException ("XX_VLO_PlacedOrderAs_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_PlacedOrderAs_ID", Integer.valueOf(XX_VLO_PlacedOrderAs_ID));
        
    }
    
    /** Get Placed Order Asssociation.
    @return Placed Order Asssociation */
    public int getXX_VLO_PlacedOrderAs_ID() 
    {
        return get_ValueAsInt("XX_VLO_PlacedOrderAs_ID");
        
    }
    
    /** Set Placed Order.
    @param XX_VMR_Order_ID Placed Order */
    public void setXX_VMR_Order_ID (int XX_VMR_Order_ID)
    {
        if (XX_VMR_Order_ID <= 0) set_Value ("XX_VMR_Order_ID", null);
        else
        set_Value ("XX_VMR_Order_ID", Integer.valueOf(XX_VMR_Order_ID));
        
    }
    
    /** Get Placed Order.
    @return Placed Order */
    public int getXX_VMR_Order_ID() 
    {
        return get_ValueAsInt("XX_VMR_Order_ID");
        
    }
    
    
}
