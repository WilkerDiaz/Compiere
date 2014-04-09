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
/** Generated Model for XX_VLO_CircuitTour
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_CircuitTour extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_CircuitTour_ID id
    @param trx transaction
    */
    public X_XX_VLO_CircuitTour (Ctx ctx, int XX_VLO_CircuitTour_ID, Trx trx)
    {
        super (ctx, XX_VLO_CircuitTour_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_CircuitTour_ID == 0)
        {
            setXX_VLO_CircuitTour_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_CircuitTour (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27570343475789L;
    /** Last Updated Timestamp 2010-10-27 17:52:39.0 */
    public static final long updatedMS = 1288218159000L;
    /** AD_Table_ID=1000282 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_CircuitTour");
        
    }
    ;
    
    /** TableName=XX_VLO_CircuitTour */
    public static final String Table_Name="XX_VLO_CircuitTour";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Arrival Warehouse.
    @param XX_ArrivalWarehouse_ID Arrival Warehouse */
    public void setXX_ArrivalWarehouse_ID (int XX_ArrivalWarehouse_ID)
    {
        if (XX_ArrivalWarehouse_ID <= 0) set_Value ("XX_ArrivalWarehouse_ID", null);
        else
        set_Value ("XX_ArrivalWarehouse_ID", Integer.valueOf(XX_ArrivalWarehouse_ID));
        
    }
    
    /** Get Arrival Warehouse.
    @return Arrival Warehouse */
    public int getXX_ArrivalWarehouse_ID() 
    {
        return get_ValueAsInt("XX_ArrivalWarehouse_ID");
        
    }
    
    /** Set Circuit Kilometers.
    @param XX_CircuitKilometers Circuit Kilometers */
    public void setXX_CircuitKilometers (java.math.BigDecimal XX_CircuitKilometers)
    {
        set_Value ("XX_CircuitKilometers", XX_CircuitKilometers);
        
    }
    
    /** Get Circuit Kilometers.
    @return Circuit Kilometers */
    public java.math.BigDecimal getXX_CircuitKilometers() 
    {
        return get_ValueAsBigDecimal("XX_CircuitKilometers");
        
    }
    
    /** Set Departure Warehouse.
    @param XX_DepartureWarehouse_ID Departure Warehouse */
    public void setXX_DepartureWarehouse_ID (int XX_DepartureWarehouse_ID)
    {
        if (XX_DepartureWarehouse_ID <= 0) set_Value ("XX_DepartureWarehouse_ID", null);
        else
        set_Value ("XX_DepartureWarehouse_ID", Integer.valueOf(XX_DepartureWarehouse_ID));
        
    }
    
    /** Get Departure Warehouse.
    @return Departure Warehouse */
    public int getXX_DepartureWarehouse_ID() 
    {
        return get_ValueAsInt("XX_DepartureWarehouse_ID");
        
    }
    
    /** Set Kilometers for Dispatch.
    @param XX_KilometersDispatch Kilometers for Dispatch */
    public void setXX_KilometersDispatch (java.math.BigDecimal XX_KilometersDispatch)
    {
        set_Value ("XX_KilometersDispatch", XX_KilometersDispatch);
        
    }
    
    /** Get Kilometers for Dispatch.
    @return Kilometers for Dispatch */
    public java.math.BigDecimal getXX_KilometersDispatch() 
    {
        return get_ValueAsBigDecimal("XX_KilometersDispatch");
        
    }
    
    /** Set XX_TimeForDispatch.
    @param XX_TimeForDispatch XX_TimeForDispatch */
    public void setXX_TimeForDispatch (int XX_TimeForDispatch)
    {
        set_Value ("XX_TimeForDispatch", Integer.valueOf(XX_TimeForDispatch));
        
    }
    
    /** Get XX_TimeForDispatch.
    @return XX_TimeForDispatch */
    public int getXX_TimeForDispatch() 
    {
        return get_ValueAsInt("XX_TimeForDispatch");
        
    }
    
    /** Set Total Kilometers.
    @param XX_TotalKilometers Total Kilometers */
    public void setXX_TotalKilometers (java.math.BigDecimal XX_TotalKilometers)
    {
        throw new IllegalArgumentException ("XX_TotalKilometers is virtual column");
        
    }
    
    /** Get Total Kilometers.
    @return Total Kilometers */
    public java.math.BigDecimal getXX_TotalKilometers() 
    {
        return get_ValueAsBigDecimal("XX_TotalKilometers");
        
    }
    
    /** Set XX_VLO_CircuitTour_ID.
    @param XX_VLO_CircuitTour_ID XX_VLO_CircuitTour_ID */
    public void setXX_VLO_CircuitTour_ID (int XX_VLO_CircuitTour_ID)
    {
        if (XX_VLO_CircuitTour_ID < 1) throw new IllegalArgumentException ("XX_VLO_CircuitTour_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_CircuitTour_ID", Integer.valueOf(XX_VLO_CircuitTour_ID));
        
    }
    
    /** Get XX_VLO_CircuitTour_ID.
    @return XX_VLO_CircuitTour_ID */
    public int getXX_VLO_CircuitTour_ID() 
    {
        return get_ValueAsInt("XX_VLO_CircuitTour_ID");
        
    }
    
    
}
