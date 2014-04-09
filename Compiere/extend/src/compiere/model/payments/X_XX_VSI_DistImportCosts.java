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
/** Generated Model for XX_VSI_DistImportCosts
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VSI_DistImportCosts extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VSI_DistImportCosts_ID id
    @param trx transaction
    */
    public X_XX_VSI_DistImportCosts (Ctx ctx, int XX_VSI_DistImportCosts_ID, Trx trx)
    {
        super (ctx, XX_VSI_DistImportCosts_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VSI_DistImportCosts_ID == 0)
        {
            setXX_VSI_DistImportCosts_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VSI_DistImportCosts (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27601177866789L;
    /** Last Updated Timestamp 2011-10-19 14:59:10.0 */
    public static final long updatedMS = 1319052550000L;
    /** AD_Table_ID=1000410 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VSI_DistImportCosts");
        
    }
    ;
    
    /** TableName=XX_VSI_DistImportCosts */
    public static final String Table_Name="XX_VSI_DistImportCosts";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        set_ValueNoCheck ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Set Date To.
    @param DateTo End date of a date range */
    public void setDateTo (Timestamp DateTo)
    {
        set_ValueNoCheck ("DateTo", DateTo);
        
    }
    
    /** Get Date To.
    @return End date of a date range */
    public Timestamp getDateTo() 
    {
        return (Timestamp)get_Value("DateTo");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Rate.
    @param Rate Rate or Tax or Exchange */
    public void setRate (java.math.BigDecimal Rate)
    {
        set_Value ("Rate", Rate);
        
    }
    
    /** Get Rate.
    @return Rate or Tax or Exchange */
    public java.math.BigDecimal getRate() 
    {
        return get_ValueAsBigDecimal("Rate");
        
    }
    
    /** Set Dist Import Costs.
    @param XX_VSI_DistImportCosts_ID Dist Import Costs */
    public void setXX_VSI_DistImportCosts_ID (int XX_VSI_DistImportCosts_ID)
    {
        if (XX_VSI_DistImportCosts_ID < 1) throw new IllegalArgumentException ("XX_VSI_DistImportCosts_ID is mandatory.");
        set_ValueNoCheck ("XX_VSI_DistImportCosts_ID", Integer.valueOf(XX_VSI_DistImportCosts_ID));
        
    }
    
    /** Get Dist Import Costs.
    @return Dist Import Costs */
    public int getXX_VSI_DistImportCosts_ID() 
    {
        return get_ValueAsInt("XX_VSI_DistImportCosts_ID");
        
    }
    
    
}
