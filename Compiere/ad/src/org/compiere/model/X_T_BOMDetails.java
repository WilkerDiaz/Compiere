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
/** Generated Model for T_BOMDetails
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_BOMDetails.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_BOMDetails extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_BOMDetails_ID id
    @param trx transaction
    */
    public X_T_BOMDetails (Ctx ctx, int T_BOMDetails_ID, Trx trx)
    {
        super (ctx, T_BOMDetails_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_BOMDetails_ID == 0)
        {
            setAD_PInstance_ID (0);
            setLevelNo (0);
            setM_Product_ID (0);
            setT_BOMDetails_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_BOMDetails (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27530282064789L;
    /** Last Updated Timestamp 2009-07-20 23:12:28.0 */
    public static final long updatedMS = 1248156748000L;
    /** AD_Table_ID=2135 */
    public static final int Table_ID=2135;
    
    /** TableName=T_BOMDetails */
    public static final String Table_Name="T_BOMDetails";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Set Level no.
    @param LevelNo Level Number */
    public void setLevelNo (int LevelNo)
    {
        set_ValueNoCheck ("LevelNo", Integer.valueOf(LevelNo));
        
    }
    
    /** Get Level no.
    @return Level Number */
    public int getLevelNo() 
    {
        return get_ValueAsInt("LevelNo");
        
    }
    
    /** Set BOM.
    @param M_BOM_ID Bill of Materials */
    public void setM_BOM_ID (int M_BOM_ID)
    {
        if (M_BOM_ID <= 0) set_Value ("M_BOM_ID", null);
        else
        set_Value ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
    }
    
    /** Get BOM.
    @return Bill of Materials */
    public int getM_BOM_ID() 
    {
        return get_ValueAsInt("M_BOM_ID");
        
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (int Qty)
    {
        set_Value ("Qty", Integer.valueOf(Qty));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getQty() 
    {
        return get_ValueAsInt("Qty");
        
    }
    
    /** Set T_BOMDetails_ID.
    @param T_BOMDetails_ID T_BOMDetails_ID */
    public void setT_BOMDetails_ID (int T_BOMDetails_ID)
    {
        if (T_BOMDetails_ID < 1) throw new IllegalArgumentException ("T_BOMDetails_ID is mandatory.");
        set_ValueNoCheck ("T_BOMDetails_ID", Integer.valueOf(T_BOMDetails_ID));
        
    }
    
    /** Get T_BOMDetails_ID.
    @return T_BOMDetails_ID */
    public int getT_BOMDetails_ID() 
    {
        return get_ValueAsInt("T_BOMDetails_ID");
        
    }
    
    /** Set ParentBOMDetails.
    @param T_ParentBOMDetails_ID ParentBOMDetails */
    public void setT_ParentBOMDetails_ID (int T_ParentBOMDetails_ID)
    {
        if (T_ParentBOMDetails_ID <= 0) set_Value ("T_ParentBOMDetails_ID", null);
        else
        set_Value ("T_ParentBOMDetails_ID", Integer.valueOf(T_ParentBOMDetails_ID));
        
    }
    
    /** Get ParentBOMDetails.
    @return ParentBOMDetails */
    public int getT_ParentBOMDetails_ID() 
    {
        return get_ValueAsInt("T_ParentBOMDetails_ID");
        
    }
    
    /** Set RootBOMDetails.
    @param T_RootBOMDetails_ID RootBOMDetails */
    public void setT_RootBOMDetails_ID (int T_RootBOMDetails_ID)
    {
        if (T_RootBOMDetails_ID <= 0) set_Value ("T_RootBOMDetails_ID", null);
        else
        set_Value ("T_RootBOMDetails_ID", Integer.valueOf(T_RootBOMDetails_ID));
        
    }
    
    /** Get RootBOMDetails.
    @return RootBOMDetails */
    public int getT_RootBOMDetails_ID() 
    {
        return get_ValueAsInt("T_RootBOMDetails_ID");
        
    }
    
    
}
