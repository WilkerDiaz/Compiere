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
/** Generated Model for M_WorkCenterResource
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkCenterResource.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkCenterResource extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkCenterResource_ID id
    @param trx transaction
    */
    public X_M_WorkCenterResource (Ctx ctx, int M_WorkCenterResource_ID, Trx trx)
    {
        super (ctx, M_WorkCenterResource_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkCenterResource_ID == 0)
        {
            setM_Product_ID (0);
            setM_WorkCenterResource_ID (0);
            setM_WorkCenter_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkCenterResource (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27519614496789L;
    /** Last Updated Timestamp 2009-03-19 10:59:40.0 */
    public static final long updatedMS = 1237489180000L;
    /** AD_Table_ID=2085 */
    public static final int Table_ID=2085;
    
    /** TableName=M_WorkCenterResource */
    public static final String Table_Name="M_WorkCenterResource";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Work Center Resource.
    @param M_WorkCenterResource_ID Production resources assigned to a work center */
    public void setM_WorkCenterResource_ID (int M_WorkCenterResource_ID)
    {
        if (M_WorkCenterResource_ID < 1) throw new IllegalArgumentException ("M_WorkCenterResource_ID is mandatory.");
        set_ValueNoCheck ("M_WorkCenterResource_ID", Integer.valueOf(M_WorkCenterResource_ID));
        
    }
    
    /** Get Work Center Resource.
    @return Production resources assigned to a work center */
    public int getM_WorkCenterResource_ID() 
    {
        return get_ValueAsInt("M_WorkCenterResource_ID");
        
    }
    
    /** Set Work Center.
    @param M_WorkCenter_ID Identifies a production area within a warehouse consisting of people and equipment */
    public void setM_WorkCenter_ID (int M_WorkCenter_ID)
    {
        if (M_WorkCenter_ID < 1) throw new IllegalArgumentException ("M_WorkCenter_ID is mandatory.");
        set_ValueNoCheck ("M_WorkCenter_ID", Integer.valueOf(M_WorkCenter_ID));
        
    }
    
    /** Get Work Center.
    @return Identifies a production area within a warehouse consisting of people and equipment */
    public int getM_WorkCenter_ID() 
    {
        return get_ValueAsInt("M_WorkCenter_ID");
        
    }
    
    
}
