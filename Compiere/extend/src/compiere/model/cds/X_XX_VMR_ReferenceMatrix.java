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
/** Generated Model for XX_VMR_ReferenceMatrix
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_ReferenceMatrix extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_ReferenceMatrix_ID id
    @param trx transaction
    */
    public X_XX_VMR_ReferenceMatrix (Ctx ctx, int XX_VMR_ReferenceMatrix_ID, Trx trx)
    {
        super (ctx, XX_VMR_ReferenceMatrix_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_ReferenceMatrix_ID == 0)
        {
            setXX_COLUMN (0);
            setXX_QUANTITYC (0);
            setXX_QUANTITYO (0);
            setXX_QUANTITYV (0);
            setXX_ROW (0);
            setXX_VALUE1 (0);
            setXX_VALUE2 (0);
            setXX_VMR_PO_LineRefProv_ID (0);
            setXX_VMR_ReferenceMatrix_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_ReferenceMatrix (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566000291789L;
    /** Last Updated Timestamp 2010-09-07 11:26:15.0 */
    public static final long updatedMS = 1283874975000L;
    /** AD_Table_ID=1000176 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_ReferenceMatrix");
        
    }
    ;
    
    /** TableName=XX_VMR_ReferenceMatrix */
    public static final String Table_Name="XX_VMR_ReferenceMatrix";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Product.
    @param M_Product Product */
    public void setM_Product (int M_Product)
    {
        set_Value ("M_Product", Integer.valueOf(M_Product));
        
    }
    
    /** Get Product.
    @return Product */
    public int getM_Product() 
    {
        return get_ValueAsInt("M_Product");
        
    }
    
    /** Set XX_COLUMN.
    @param XX_COLUMN XX_COLUMN */
    public void setXX_COLUMN (int XX_COLUMN)
    {
        set_Value ("XX_COLUMN", Integer.valueOf(XX_COLUMN));
        
    }
    
    /** Get XX_COLUMN.
    @return XX_COLUMN */
    public int getXX_COLUMN() 
    {
        return get_ValueAsInt("XX_COLUMN");
        
    }
    
    /** Set XX_QUANTITYC.
    @param XX_QUANTITYC XX_QUANTITYC */
    public void setXX_QUANTITYC (int XX_QUANTITYC)
    {
        set_Value ("XX_QUANTITYC", Integer.valueOf(XX_QUANTITYC));
        
    }
    
    /** Get XX_QUANTITYC.
    @return XX_QUANTITYC */
    public int getXX_QUANTITYC() 
    {
        return get_ValueAsInt("XX_QUANTITYC");
        
    }
    
    /** Set XX_QUANTITYO.
    @param XX_QUANTITYO XX_QUANTITYO */
    public void setXX_QUANTITYO (int XX_QUANTITYO)
    {
        set_Value ("XX_QUANTITYO", Integer.valueOf(XX_QUANTITYO));
        
    }
    
    /** Get XX_QUANTITYO.
    @return XX_QUANTITYO */
    public int getXX_QUANTITYO() 
    {
        return get_ValueAsInt("XX_QUANTITYO");
        
    }
    
    /** Set QUANTITYV.
    @param XX_QUANTITYV QUANTITYV */
    public void setXX_QUANTITYV (int XX_QUANTITYV)
    {
        set_Value ("XX_QUANTITYV", Integer.valueOf(XX_QUANTITYV));
        
    }
    
    /** Get QUANTITYV.
    @return QUANTITYV */
    public int getXX_QUANTITYV() 
    {
        return get_ValueAsInt("XX_QUANTITYV");
        
    }
    
    /** Set XX_ROW.
    @param XX_ROW XX_ROW */
    public void setXX_ROW (int XX_ROW)
    {
        set_Value ("XX_ROW", Integer.valueOf(XX_ROW));
        
    }
    
    /** Get XX_ROW.
    @return XX_ROW */
    public int getXX_ROW() 
    {
        return get_ValueAsInt("XX_ROW");
        
    }
    
    /** Set Attribute Value.
    @param XX_VALUE1 Attribute Value */
    public void setXX_VALUE1 (int XX_VALUE1)
    {
        set_Value ("XX_VALUE1", Integer.valueOf(XX_VALUE1));
        
    }
    
    /** Get Attribute Value.
    @return Attribute Value */
    public int getXX_VALUE1() 
    {
        return get_ValueAsInt("XX_VALUE1");
        
    }
    
    /** Set Attribute Value.
    @param XX_VALUE2 Attribute Value */
    public void setXX_VALUE2 (int XX_VALUE2)
    {
        set_Value ("XX_VALUE2", Integer.valueOf(XX_VALUE2));
        
    }
    
    /** Get Attribute Value.
    @return Attribute Value */
    public int getXX_VALUE2() 
    {
        return get_ValueAsInt("XX_VALUE2");
        
    }
    
    /** Set Vendor Reference.
    @param XX_VMR_PO_LineRefProv_ID Vendor Reference */
    public void setXX_VMR_PO_LineRefProv_ID (int XX_VMR_PO_LineRefProv_ID)
    {
        if (XX_VMR_PO_LineRefProv_ID < 1) throw new IllegalArgumentException ("XX_VMR_PO_LineRefProv_ID is mandatory.");
        set_Value ("XX_VMR_PO_LineRefProv_ID", Integer.valueOf(XX_VMR_PO_LineRefProv_ID));
        
    }
    
    /** Get Vendor Reference.
    @return Vendor Reference */
    public int getXX_VMR_PO_LineRefProv_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_LineRefProv_ID");
        
    }
    
    /** Set XX_VMR_ReferenceMatrix_ID.
    @param XX_VMR_ReferenceMatrix_ID XX_VMR_ReferenceMatrix_ID */
    public void setXX_VMR_ReferenceMatrix_ID (int XX_VMR_ReferenceMatrix_ID)
    {
        if (XX_VMR_ReferenceMatrix_ID < 1) throw new IllegalArgumentException ("XX_VMR_ReferenceMatrix_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ReferenceMatrix_ID", Integer.valueOf(XX_VMR_ReferenceMatrix_ID));
        
    }
    
    /** Get XX_VMR_ReferenceMatrix_ID.
    @return XX_VMR_ReferenceMatrix_ID */
    public int getXX_VMR_ReferenceMatrix_ID() 
    {
        return get_ValueAsInt("XX_VMR_ReferenceMatrix_ID");
        
    }
    
    
}
