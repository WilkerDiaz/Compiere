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
/** Generated Model for C_Wave
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Wave.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Wave extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Wave_ID id
    @param trx transaction
    */
    public X_C_Wave (Ctx ctx, int C_Wave_ID, Trx trx)
    {
        super (ctx, C_Wave_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Wave_ID == 0)
        {
            setC_Wave_ID (0);
            setDocumentNo (null);
            setM_Warehouse_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Wave (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27503793162789L;
    /** Last Updated Timestamp 2008-09-17 09:10:46.0 */
    public static final long updatedMS = 1221667846000L;
    /** AD_Table_ID=1019 */
    public static final int Table_ID=1019;
    
    /** TableName=C_Wave */
    public static final String Table_Name="C_Wave";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Wave.
    @param C_Wave_ID Group of selected order lines for which there is sufficient onhand quantity in the warehouse */
    public void setC_Wave_ID (int C_Wave_ID)
    {
        if (C_Wave_ID < 1) throw new IllegalArgumentException ("C_Wave_ID is mandatory.");
        set_ValueNoCheck ("C_Wave_ID", Integer.valueOf(C_Wave_ID));
        
    }
    
    /** Get Wave.
    @return Group of selected order lines for which there is sufficient onhand quantity in the warehouse */
    public int getC_Wave_ID() 
    {
        return get_ValueAsInt("C_Wave_ID");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    
}
