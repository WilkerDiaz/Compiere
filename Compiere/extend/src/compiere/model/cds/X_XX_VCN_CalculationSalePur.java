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
/** Generated Model for XX_VCN_CalculationSalePur
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_CalculationSalePur extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_CalculationSalePur_ID id
    @param trx transaction
    */
    public X_XX_VCN_CalculationSalePur (Ctx ctx, int XX_VCN_CalculationSalePur_ID, Trx trx)
    {
        super (ctx, XX_VCN_CalculationSalePur_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_CalculationSalePur_ID == 0)
        {
            setXX_VCN_CALCULATIONSALEPUR_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_CalculationSalePur (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27552968118789L;
    /** Last Updated Timestamp 2010-04-09 15:23:22.0 */
    public static final long updatedMS = 1270842802000L;
    /** AD_Table_ID=1000274 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_CalculationSalePur");
        
    }
    ;
    
    /** TableName=XX_VCN_CalculationSalePur */
    public static final String Table_Name="XX_VCN_CalculationSalePur";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (String XX_Month)
    {
        throw new IllegalArgumentException ("XX_Month is virtual column");
        
    }
    
    /** Get Month.
    @return Month */
    public String getXX_Month() 
    {
        return (String)get_Value("XX_Month");
        
    }
    
    /** Set XX_VCN_CALCULATIONSALEPUR_ID.
    @param XX_VCN_CALCULATIONSALEPUR_ID XX_VCN_CALCULATIONSALEPUR_ID */
    public void setXX_VCN_CALCULATIONSALEPUR_ID (int XX_VCN_CALCULATIONSALEPUR_ID)
    {
        if (XX_VCN_CALCULATIONSALEPUR_ID < 1) throw new IllegalArgumentException ("XX_VCN_CALCULATIONSALEPUR_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_CALCULATIONSALEPUR_ID", Integer.valueOf(XX_VCN_CALCULATIONSALEPUR_ID));
        
    }
    
    /** Get XX_VCN_CALCULATIONSALEPUR_ID.
    @return XX_VCN_CALCULATIONSALEPUR_ID */
    public int getXX_VCN_CALCULATIONSALEPUR_ID() 
    {
        return get_ValueAsInt("XX_VCN_CALCULATIONSALEPUR_ID");
        
    }
    
    /** Set Year.
    @param XX_Year Year */
    public void setXX_Year (int XX_Year)
    {
        set_Value ("XX_Year", Integer.valueOf(XX_Year));
        
    }
    
    /** Get Year.
    @return Year */
    public int getXX_Year() 
    {
        return get_ValueAsInt("XX_Year");
        
    }
    
    
}
