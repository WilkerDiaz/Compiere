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
/** Generated Model for XX_VMR_Factor
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Factor extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Factor_ID id
    @param trx transaction
    */
    public X_XX_VMR_Factor (Ctx ctx, int XX_VMR_Factor_ID, Trx trx)
    {
        super (ctx, XX_VMR_Factor_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Factor_ID == 0)
        {
            setXX_VMR_Factor_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Factor (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27631925941789L;
    /** Last Updated Timestamp 2012-10-09 12:07:05.0 */
    public static final long updatedMS = 1349800625000L;
    /** AD_Table_ID=1003053 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Factor");
        
    }
    ;
    
    /** TableName=XX_VMR_Factor */
    public static final String Table_Name="XX_VMR_Factor";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (int XX_Month)
    {
        set_Value ("XX_Month", Integer.valueOf(XX_Month));
        
    }
    
    /** Get Month.
    @return Month */
    public int getXX_Month() 
    {
        return get_ValueAsInt("XX_Month");
        
    }
    
    /** Set XX_MultiplicationFactor.
    @param XX_MultiplicationFactor XX_MultiplicationFactor */
    public void setXX_MultiplicationFactor (java.math.BigDecimal XX_MultiplicationFactor)
    {
        set_Value ("XX_MultiplicationFactor", XX_MultiplicationFactor);
        
    }
    
    /** Get XX_MultiplicationFactor.
    @return XX_MultiplicationFactor */
    public java.math.BigDecimal getXX_MultiplicationFactor() 
    {
        return get_ValueAsBigDecimal("XX_MultiplicationFactor");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Multiplication Factor.
    @param XX_VMR_Factor_ID Multiplication Factor */
    public void setXX_VMR_Factor_ID (int XX_VMR_Factor_ID)
    {
        if (XX_VMR_Factor_ID < 1) throw new IllegalArgumentException ("XX_VMR_Factor_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Factor_ID", Integer.valueOf(XX_VMR_Factor_ID));
        
    }
    
    /** Get Multiplication Factor.
    @return Multiplication Factor */
    public int getXX_VMR_Factor_ID() 
    {
        return get_ValueAsInt("XX_VMR_Factor_ID");
        
    }
    
    
}
