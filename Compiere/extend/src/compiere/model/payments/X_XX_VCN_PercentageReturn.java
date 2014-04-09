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
/** Generated Model for XX_VCN_PercentageReturn
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_PercentageReturn extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_PercentageReturn_ID id
    @param trx transaction
    */
    public X_XX_VCN_PercentageReturn (Ctx ctx, int XX_VCN_PercentageReturn_ID, Trx trx)
    {
        super (ctx, XX_VCN_PercentageReturn_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_PercentageReturn_ID == 0)
        {
            setXX_VCN_PercentageReturn_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_PercentageReturn (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27661836418789L;
    /** Last Updated Timestamp 2013-09-20 16:35:02.0 */
    public static final long updatedMS = 1379711102000L;
    /** AD_Table_ID=1002664 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_PercentageReturn");
        
    }
    ;
    
    /** TableName=XX_VCN_PercentageReturn */
    public static final String Table_Name="XX_VCN_PercentageReturn";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Percentage.
    @param XX_Percentage Porcentaje del Valor para el calculo de Peso */
    public void setXX_Percentage (java.math.BigDecimal XX_Percentage)
    {
        set_Value ("XX_Percentage", XX_Percentage);
        
    }
    
    /** Get Percentage.
    @return Porcentaje del Valor para el calculo de Peso */
    public java.math.BigDecimal getXX_Percentage() 
    {
        return get_ValueAsBigDecimal("XX_Percentage");
        
    }
    
    /** Set XX_VCN_PercentageReturn_ID.
    @param XX_VCN_PercentageReturn_ID XX_VCN_PercentageReturn_ID */
    public void setXX_VCN_PercentageReturn_ID (int XX_VCN_PercentageReturn_ID)
    {
        if (XX_VCN_PercentageReturn_ID < 1) throw new IllegalArgumentException ("XX_VCN_PercentageReturn_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_PercentageReturn_ID", Integer.valueOf(XX_VCN_PercentageReturn_ID));
        
    }
    
    /** Get XX_VCN_PercentageReturn_ID.
    @return XX_VCN_PercentageReturn_ID */
    public int getXX_VCN_PercentageReturn_ID() 
    {
        return get_ValueAsInt("XX_VCN_PercentageReturn_ID");
        
    }
    
    
}
