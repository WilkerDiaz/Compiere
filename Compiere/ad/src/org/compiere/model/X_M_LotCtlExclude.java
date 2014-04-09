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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for M_LotCtlExclude
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_LotCtlExclude.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_LotCtlExclude extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_LotCtlExclude_ID id
    @param trx transaction
    */
    public X_M_LotCtlExclude (Ctx ctx, int M_LotCtlExclude_ID, Trx trx)
    {
        super (ctx, M_LotCtlExclude_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_LotCtlExclude_ID == 0)
        {
            setAD_Table_ID (0);
            setIsSOTrx (false);
            setM_LotCtlExclude_ID (0);
            setM_LotCtl_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_LotCtlExclude (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=810 */
    public static final int Table_ID=810;
    
    /** TableName=M_LotCtlExclude */
    public static final String Table_Name="M_LotCtlExclude";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
    }
    
    /** Set Exclude Lot.
    @param M_LotCtlExclude_ID Exclude the ability to create Lots in Attribute Sets */
    public void setM_LotCtlExclude_ID (int M_LotCtlExclude_ID)
    {
        if (M_LotCtlExclude_ID < 1) throw new IllegalArgumentException ("M_LotCtlExclude_ID is mandatory.");
        set_ValueNoCheck ("M_LotCtlExclude_ID", Integer.valueOf(M_LotCtlExclude_ID));
        
    }
    
    /** Get Exclude Lot.
    @return Exclude the ability to create Lots in Attribute Sets */
    public int getM_LotCtlExclude_ID() 
    {
        return get_ValueAsInt("M_LotCtlExclude_ID");
        
    }
    
    /** Set Lot Control.
    @param M_LotCtl_ID Product Lot Control */
    public void setM_LotCtl_ID (int M_LotCtl_ID)
    {
        if (M_LotCtl_ID < 1) throw new IllegalArgumentException ("M_LotCtl_ID is mandatory.");
        set_ValueNoCheck ("M_LotCtl_ID", Integer.valueOf(M_LotCtl_ID));
        
    }
    
    /** Get Lot Control.
    @return Product Lot Control */
    public int getM_LotCtl_ID() 
    {
        return get_ValueAsInt("M_LotCtl_ID");
        
    }
    
    
}
