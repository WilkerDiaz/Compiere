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
/** Generated Model for XX_VCN_ReplacementFactor
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_ReplacementFactor extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_ReplacementFactor_ID id
    @param trx transaction
    */
    public X_XX_VCN_ReplacementFactor (Ctx ctx, int XX_VCN_ReplacementFactor_ID, Trx trx)
    {
        super (ctx, XX_VCN_ReplacementFactor_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_ReplacementFactor_ID == 0)
        {
            setXX_VCN_ReplacementFactor_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_ReplacementFactor (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27636236143789L;
    /** Last Updated Timestamp 2012-11-28 09:23:47.0 */
    public static final long updatedMS = 1354110827000L;
    /** AD_Table_ID=1003453 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_ReplacementFactor");
        
    }
    ;
    
    /** TableName=XX_VCN_ReplacementFactor */
    public static final String Table_Name="XX_VCN_ReplacementFactor";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Conversion Rate.
    @param C_Conversion_Rate_ID Rate used for converting currencies */
    public void setC_Conversion_Rate_ID (int C_Conversion_Rate_ID)
    {
        if (C_Conversion_Rate_ID <= 0) set_Value ("C_Conversion_Rate_ID", null);
        else
        set_Value ("C_Conversion_Rate_ID", Integer.valueOf(C_Conversion_Rate_ID));
        
    }
    
    /** Get Conversion Rate.
    @return Rate used for converting currencies */
    public int getC_Conversion_Rate_ID() 
    {
        return get_ValueAsInt("C_Conversion_Rate_ID");
        
    }
    
    /** Set Multiply Rate.
    @param MultiplyRate Multiply Rate */
    public void setMultiplyRate (java.math.BigDecimal MultiplyRate)
    {
        set_Value ("MultiplyRate", MultiplyRate);
        
    }
    
    /** Get Multiply Rate.
    @return Multiply Rate */
    public java.math.BigDecimal getMultiplyRate() 
    {
        return get_ValueAsBigDecimal("MultiplyRate");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
    }
    
    /** Set PercentageIncrease.
    @param XX_PercentageIncrease PercentageIncrease */
    public void setXX_PercentageIncrease (java.math.BigDecimal XX_PercentageIncrease)
    {
        set_Value ("XX_PercentageIncrease", XX_PercentageIncrease);
        
    }
    
    /** Get PercentageIncrease.
    @return PercentageIncrease */
    public java.math.BigDecimal getXX_PercentageIncrease() 
    {
        return get_ValueAsBigDecimal("XX_PercentageIncrease");
        
    }
    
    /** Set Replacement Factor.
    @param XX_ReplacementFactor Factor de Reposición */
    public void setXX_ReplacementFactor (java.math.BigDecimal XX_ReplacementFactor)
    {
        set_Value ("XX_ReplacementFactor", XX_ReplacementFactor);
        
    }
    
    /** Get Replacement Factor.
    @return Factor de Reposición */
    public java.math.BigDecimal getXX_ReplacementFactor() 
    {
        return get_ValueAsBigDecimal("XX_ReplacementFactor");
        
    }
    
    /** Set XX_VCN_ReplacementFactor_ID.
    @param XX_VCN_ReplacementFactor_ID XX_VCN_ReplacementFactor_ID */
    public void setXX_VCN_ReplacementFactor_ID (int XX_VCN_ReplacementFactor_ID)
    {
        if (XX_VCN_ReplacementFactor_ID < 1) throw new IllegalArgumentException ("XX_VCN_ReplacementFactor_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_ReplacementFactor_ID", Integer.valueOf(XX_VCN_ReplacementFactor_ID));
        
    }
    
    /** Get XX_VCN_ReplacementFactor_ID.
    @return XX_VCN_ReplacementFactor_ID */
    public int getXX_VCN_ReplacementFactor_ID() 
    {
        return get_ValueAsInt("XX_VCN_ReplacementFactor_ID");
        
    }
    
    
}
