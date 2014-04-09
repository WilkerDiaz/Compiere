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
/** Generated Model for XX_VLO_Travel
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_Travel extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_Travel_ID id
    @param trx transaction
    */
    public X_XX_VLO_Travel (Ctx ctx, int XX_VLO_Travel_ID, Trx trx)
    {
        super (ctx, XX_VLO_Travel_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_Travel_ID == 0)
        {
            setValue (null);
            setXX_TravelDate (new Timestamp(System.currentTimeMillis()));
            setXX_VLO_Travel_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_Travel (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27564121616789L;
    /** Last Updated Timestamp 2010-08-16 17:35:00.0 */
    public static final long updatedMS = 1281996300000L;
    /** AD_Table_ID=1000301 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_Travel");
        
    }
    ;
    
    /** TableName=XX_VLO_Travel */
    public static final String Table_Name="XX_VLO_Travel";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Arrived At Destination.
    @param XX_ArrivedAtDestination Arrived At Destination */
    public void setXX_ArrivedAtDestination (boolean XX_ArrivedAtDestination)
    {
        set_Value ("XX_ArrivedAtDestination", Boolean.valueOf(XX_ArrivedAtDestination));
        
    }
    
    /** Get Arrived At Destination.
    @return Arrived At Destination */
    public boolean isXX_ArrivedAtDestination() 
    {
        return get_ValueAsBoolean("XX_ArrivedAtDestination");
        
    }
    
    /** Set Equivalent Package Quantity.
    @param XX_EquivalentPackageQuantity Equivalent Package Quantity */
    public void setXX_EquivalentPackageQuantity (int XX_EquivalentPackageQuantity)
    {
        set_Value ("XX_EquivalentPackageQuantity", Integer.valueOf(XX_EquivalentPackageQuantity));
        
    }
    
    /** Get Equivalent Package Quantity.
    @return Equivalent Package Quantity */
    public int getXX_EquivalentPackageQuantity() 
    {
        return get_ValueAsInt("XX_EquivalentPackageQuantity");
        
    }
    
    /** Set Total Packages.
    @param XX_TotalPackages Total Packages */
    public void setXX_TotalPackages (int XX_TotalPackages)
    {
        set_Value ("XX_TotalPackages", Integer.valueOf(XX_TotalPackages));
        
    }
    
    /** Get Total Packages.
    @return Total Packages */
    public int getXX_TotalPackages() 
    {
        return get_ValueAsInt("XX_TotalPackages");
        
    }
    
    /** Set Total Packages Receive.
    @param XX_TotalPackagesReceive Total Packages Receive */
    public void setXX_TotalPackagesReceive (int XX_TotalPackagesReceive)
    {
        set_Value ("XX_TotalPackagesReceive", Integer.valueOf(XX_TotalPackagesReceive));
        
    }
    
    /** Get Total Packages Receive.
    @return Total Packages Receive */
    public int getXX_TotalPackagesReceive() 
    {
        return get_ValueAsInt("XX_TotalPackagesReceive");
        
    }
    
    /** Set Total Packages Sent.
    @param XX_TotalPackagesSent Total Packages Sent */
    public void setXX_TotalPackagesSent (int XX_TotalPackagesSent)
    {
        set_Value ("XX_TotalPackagesSent", Integer.valueOf(XX_TotalPackagesSent));
        
    }
    
    /** Get Total Packages Sent.
    @return Total Packages Sent */
    public int getXX_TotalPackagesSent() 
    {
        return get_ValueAsInt("XX_TotalPackagesSent");
        
    }
    
    /** Set Travel Date.
    @param XX_TravelDate Travel Date */
    public void setXX_TravelDate (Timestamp XX_TravelDate)
    {
        if (XX_TravelDate == null) throw new IllegalArgumentException ("XX_TravelDate is mandatory.");
        set_Value ("XX_TravelDate", XX_TravelDate);
        
    }
    
    /** Get Travel Date.
    @return Travel Date */
    public Timestamp getXX_TravelDate() 
    {
        return (Timestamp)get_Value("XX_TravelDate");
        
    }
    
    /** Set Fleet.
    @param XX_VLO_Fleet_ID Fleet */
    public void setXX_VLO_Fleet_ID (int XX_VLO_Fleet_ID)
    {
        if (XX_VLO_Fleet_ID <= 0) set_Value ("XX_VLO_Fleet_ID", null);
        else
        set_Value ("XX_VLO_Fleet_ID", Integer.valueOf(XX_VLO_Fleet_ID));
        
    }
    
    /** Get Fleet.
    @return Fleet */
    public int getXX_VLO_Fleet_ID() 
    {
        return get_ValueAsInt("XX_VLO_Fleet_ID");
        
    }
    
    /** Set Travel.
    @param XX_VLO_Travel_ID Travel */
    public void setXX_VLO_Travel_ID (int XX_VLO_Travel_ID)
    {
        if (XX_VLO_Travel_ID < 1) throw new IllegalArgumentException ("XX_VLO_Travel_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_Travel_ID", Integer.valueOf(XX_VLO_Travel_ID));
        
    }
    
    /** Get Travel.
    @return Travel */
    public int getXX_VLO_Travel_ID() 
    {
        return get_ValueAsInt("XX_VLO_Travel_ID");
        
    }
    
    
}
