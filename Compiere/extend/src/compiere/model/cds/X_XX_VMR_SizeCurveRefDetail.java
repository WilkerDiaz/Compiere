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
/** Generated Model for XX_VMR_SizeCurveRefDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_SizeCurveRefDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_SizeCurveRefDetail_ID id
    @param trx transaction
    */
    public X_XX_VMR_SizeCurveRefDetail (Ctx ctx, int XX_VMR_SizeCurveRefDetail_ID, Trx trx)
    {
        super (ctx, XX_VMR_SizeCurveRefDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_SizeCurveRefDetail_ID == 0)
        {
            setM_AttributeValue_ID (0);
            setXX_CurveValue (0);	// 0
            setXX_VMR_SIZECURVE_ID (0);
            setXX_VMR_SIZECURVEREFDETAIL_ID (0);
            setXX_VMR_VendorProdRef_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_SizeCurveRefDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27548722332789L;
    /** Last Updated Timestamp 2010-02-19 12:00:16.0 */
    public static final long updatedMS = 1266597016000L;
    /** AD_Table_ID=1000185 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_SizeCurveRefDetail");
        
    }
    ;
    
    /** TableName=XX_VMR_SizeCurveRefDetail */
    public static final String Table_Name="XX_VMR_SizeCurveRefDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Attribute Value.
    @param M_AttributeValue_ID Product Attribute Value */
    public void setM_AttributeValue_ID (int M_AttributeValue_ID)
    {
        if (M_AttributeValue_ID < 1) throw new IllegalArgumentException ("M_AttributeValue_ID is mandatory.");
        set_ValueNoCheck ("M_AttributeValue_ID", Integer.valueOf(M_AttributeValue_ID));
        
    }
    
    /** Get Attribute Value.
    @return Product Attribute Value */
    public int getM_AttributeValue_ID() 
    {
        return get_ValueAsInt("M_AttributeValue_ID");
        
    }
    
    /** Set Curve Value.
    @param XX_CurveValue Curve Value */
    public void setXX_CurveValue (int XX_CurveValue)
    {
        set_Value ("XX_CurveValue", Integer.valueOf(XX_CurveValue));
        
    }
    
    /** Get Curve Value.
    @return Curve Value */
    public int getXX_CurveValue() 
    {
        return get_ValueAsInt("XX_CurveValue");
        
    }
    
    /** Set XX_VMR_SIZECURVE_ID.
    @param XX_VMR_SIZECURVE_ID XX_VMR_SIZECURVE_ID */
    public void setXX_VMR_SIZECURVE_ID (int XX_VMR_SIZECURVE_ID)
    {
        if (XX_VMR_SIZECURVE_ID < 1) throw new IllegalArgumentException ("XX_VMR_SIZECURVE_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_SIZECURVE_ID", Integer.valueOf(XX_VMR_SIZECURVE_ID));
        
    }
    
    /** Get XX_VMR_SIZECURVE_ID.
    @return XX_VMR_SIZECURVE_ID */
    public int getXX_VMR_SIZECURVE_ID() 
    {
        return get_ValueAsInt("XX_VMR_SIZECURVE_ID");
        
    }
    
    /** Set XX_VMR_SIZECURVEREFDETAIL_ID.
    @param XX_VMR_SIZECURVEREFDETAIL_ID XX_VMR_SIZECURVEREFDETAIL_ID */
    public void setXX_VMR_SIZECURVEREFDETAIL_ID (int XX_VMR_SIZECURVEREFDETAIL_ID)
    {
        if (XX_VMR_SIZECURVEREFDETAIL_ID < 1) throw new IllegalArgumentException ("XX_VMR_SIZECURVEREFDETAIL_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_SIZECURVEREFDETAIL_ID", Integer.valueOf(XX_VMR_SIZECURVEREFDETAIL_ID));
        
    }
    
    /** Get XX_VMR_SIZECURVEREFDETAIL_ID.
    @return XX_VMR_SIZECURVEREFDETAIL_ID */
    public int getXX_VMR_SIZECURVEREFDETAIL_ID() 
    {
        return get_ValueAsInt("XX_VMR_SIZECURVEREFDETAIL_ID");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID < 1) throw new IllegalArgumentException ("XX_VMR_VendorProdRef_ID is mandatory.");
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
