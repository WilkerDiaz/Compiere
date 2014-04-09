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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_ISLRUT
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_ISLRUT extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_ISLRUT_ID id
    @param trx transaction
    */
    public X_XX_VCN_ISLRUT (Ctx ctx, int XX_VCN_ISLRUT_ID, Trx trx)
    {
        super (ctx, XX_VCN_ISLRUT_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_ISLRUT_ID == 0)
        {
            setXX_VCN_ISLRUT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_ISLRUT (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27607322529789L;
    /** Last Updated Timestamp 2011-12-29 17:50:13.0 */
    public static final long updatedMS = 1325197213000L;
    /** AD_Table_ID=1002662 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_ISLRUT");
        
    }
    ;
    
    /** TableName=XX_VCN_ISLRUT */
    public static final String Table_Name="XX_VCN_ISLRUT";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Percent Of Retention.
    @param XX_PercentOfRetention Percent Of Retention */
    public void setXX_PercentOfRetention (java.math.BigDecimal XX_PercentOfRetention)
    {
        set_Value ("XX_PercentOfRetention", XX_PercentOfRetention);
        
    }
    
    /** Get Percent Of Retention.
    @return Percent Of Retention */
    public java.math.BigDecimal getXX_PercentOfRetention() 
    {
        return get_ValueAsBigDecimal("XX_PercentOfRetention");
        
    }
    
    /** Set Subtrahend.
    @param XX_Subtrahend Subtrahend */
    public void setXX_Subtrahend (int XX_Subtrahend)
    {
        set_Value ("XX_Subtrahend", Integer.valueOf(XX_Subtrahend));
        
    }
    
    /** Get Subtrahend.
    @return Subtrahend */
    public int getXX_Subtrahend() 
    {
        return get_ValueAsInt("XX_Subtrahend");
        
    }
    
    /** Set Max UT.
    @param XX_UTMax Max UT */
    public void setXX_UTMax (int XX_UTMax)
    {
        set_Value ("XX_UTMax", Integer.valueOf(XX_UTMax));
        
    }
    
    /** Get Max UT.
    @return Max UT */
    public int getXX_UTMax() 
    {
        return get_ValueAsInt("XX_UTMax");
        
    }
    
    /** Set Min UT.
    @param XX_UTMin Min UT */
    public void setXX_UTMin (int XX_UTMin)
    {
        set_Value ("XX_UTMin", Integer.valueOf(XX_UTMin));
        
    }
    
    /** Get Min UT.
    @return Min UT */
    public int getXX_UTMin() 
    {
        return get_ValueAsInt("XX_UTMin");
        
    }
    
    /** Set XX_VCN_ISLRUT_ID.
    @param XX_VCN_ISLRUT_ID XX_VCN_ISLRUT_ID */
    public void setXX_VCN_ISLRUT_ID (int XX_VCN_ISLRUT_ID)
    {
        if (XX_VCN_ISLRUT_ID < 1) throw new IllegalArgumentException ("XX_VCN_ISLRUT_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_ISLRUT_ID", Integer.valueOf(XX_VCN_ISLRUT_ID));
        
    }
    
    /** Get XX_VCN_ISLRUT_ID.
    @return XX_VCN_ISLRUT_ID */
    public int getXX_VCN_ISLRUT_ID() 
    {
        return get_ValueAsInt("XX_VCN_ISLRUT_ID");
        
    }
    
    
}
