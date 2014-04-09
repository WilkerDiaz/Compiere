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
/** Generated Model for XX_VLO_Fleet
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_Fleet extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_Fleet_ID id
    @param trx transaction
    */
    public X_XX_VLO_Fleet (Ctx ctx, int XX_VLO_Fleet_ID, Trx trx)
    {
        super (ctx, XX_VLO_Fleet_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_Fleet_ID == 0)
        {
            setValue (null);
            setXX_VLO_Fleet_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_Fleet (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27570249136789L;
    /** Last Updated Timestamp 2010-10-26 15:40:20.0 */
    public static final long updatedMS = 1288123820000L;
    /** AD_Table_ID=1000281 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_Fleet");
        
    }
    ;
    
    /** TableName=XX_VLO_Fleet */
    public static final String Table_Name="XX_VLO_Fleet";
    
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
    
    /** Set Adjustments Amount.
    @param XX_AdjustmentsAmount Monto de Ajuste */
    public void setXX_AdjustmentsAmount (java.math.BigDecimal XX_AdjustmentsAmount)
    {
        set_Value ("XX_AdjustmentsAmount", XX_AdjustmentsAmount);
        
    }
    
    /** Get Adjustments Amount.
    @return Monto de Ajuste */
    public java.math.BigDecimal getXX_AdjustmentsAmount() 
    {
        return get_ValueAsBigDecimal("XX_AdjustmentsAmount");
        
    }
    
    /** Set First Assistant.
    @param XX_Assistant1_ID First Assistant */
    public void setXX_Assistant1_ID (int XX_Assistant1_ID)
    {
        if (XX_Assistant1_ID <= 0) set_Value ("XX_Assistant1_ID", null);
        else
        set_Value ("XX_Assistant1_ID", Integer.valueOf(XX_Assistant1_ID));
        
    }
    
    /** Get First Assistant.
    @return First Assistant */
    public int getXX_Assistant1_ID() 
    {
        return get_ValueAsInt("XX_Assistant1_ID");
        
    }
    
    /** Set Second Assistant.
    @param XX_Assistant2_ID Second Assistant */
    public void setXX_Assistant2_ID (int XX_Assistant2_ID)
    {
        if (XX_Assistant2_ID <= 0) set_Value ("XX_Assistant2_ID", null);
        else
        set_Value ("XX_Assistant2_ID", Integer.valueOf(XX_Assistant2_ID));
        
    }
    
    /** Get Second Assistant.
    @return Second Assistant */
    public int getXX_Assistant2_ID() 
    {
        return get_ValueAsInt("XX_Assistant2_ID");
        
    }
    
    /** Set Average Capacity Equivalent Packages.
    @param XX_AverCapEquiPack Average Capacity Equivalent Packages */
    public void setXX_AverCapEquiPack (java.math.BigDecimal XX_AverCapEquiPack)
    {
        set_Value ("XX_AverCapEquiPack", XX_AverCapEquiPack);
        
    }
    
    /** Get Average Capacity Equivalent Packages.
    @return Average Capacity Equivalent Packages */
    public java.math.BigDecimal getXX_AverCapEquiPack() 
    {
        return get_ValueAsBigDecimal("XX_AverCapEquiPack");
        
    }
    
    /** Set Beco Fleet.
    @param XX_BecoFleet Beco Fleet */
    public void setXX_BecoFleet (boolean XX_BecoFleet)
    {
        set_Value ("XX_BecoFleet", Boolean.valueOf(XX_BecoFleet));
        
    }
    
    /** Get Beco Fleet.
    @return Beco Fleet */
    public boolean isXX_BecoFleet() 
    {
        return get_ValueAsBoolean("XX_BecoFleet");
        
    }
    
    /** Set Brand.
    @param XX_Brand_Name Brand */
    public void setXX_Brand_Name (String XX_Brand_Name)
    {
        set_Value ("XX_Brand_Name", XX_Brand_Name);
        
    }
    
    /** Get Brand.
    @return Brand */
    public String getXX_Brand_Name() 
    {
        return (String)get_Value("XX_Brand_Name");
        
    }
    
    /** Set Car Plate.
    @param XX_CarPlate Car Plate */
    public void setXX_CarPlate (String XX_CarPlate)
    {
        set_Value ("XX_CarPlate", XX_CarPlate);
        
    }
    
    /** Get Car Plate.
    @return Car Plate */
    public String getXX_CarPlate() 
    {
        return (String)get_Value("XX_CarPlate");
        
    }
    
    /** Set Driver.
    @param XX_Driver_ID Driver */
    public void setXX_Driver_ID (int XX_Driver_ID)
    {
        if (XX_Driver_ID <= 0) set_Value ("XX_Driver_ID", null);
        else
        set_Value ("XX_Driver_ID", Integer.valueOf(XX_Driver_ID));
        
    }
    
    /** Get Driver.
    @return Driver */
    public int getXX_Driver_ID() 
    {
        return get_ValueAsInt("XX_Driver_ID");
        
    }
    
    /** Set Equivalent Package Quantity.
    @param XX_EquivalentPackageQuantity Equivalent Package Quantity */
    public void setXX_EquivalentPackageQuantity (java.math.BigDecimal XX_EquivalentPackageQuantity)
    {
        throw new IllegalArgumentException ("XX_EquivalentPackageQuantity is virtual column");
        
    }
    
    /** Get Equivalent Package Quantity.
    @return Equivalent Package Quantity */
    public java.math.BigDecimal getXX_EquivalentPackageQuantity() 
    {
        return get_ValueAsBigDecimal("XX_EquivalentPackageQuantity");
        
    }
    
    /** Set Maximum Capacity.
    @param XX_MaximumCapacity Maximum Capacity */
    public void setXX_MaximumCapacity (int XX_MaximumCapacity)
    {
        set_Value ("XX_MaximumCapacity", Integer.valueOf(XX_MaximumCapacity));
        
    }
    
    /** Get Maximum Capacity.
    @return Maximum Capacity */
    public int getXX_MaximumCapacity() 
    {
        return get_ValueAsInt("XX_MaximumCapacity");
        
    }
    
    /** Set Maximum Weight.
    @param XX_MaximumWeight Maximum Weight */
    public void setXX_MaximumWeight (java.math.BigDecimal XX_MaximumWeight)
    {
        set_Value ("XX_MaximumWeight", XX_MaximumWeight);
        
    }
    
    /** Get Maximum Weight.
    @return Maximum Weight */
    public java.math.BigDecimal getXX_MaximumWeight() 
    {
        return get_ValueAsBigDecimal("XX_MaximumWeight");
        
    }
    
    /** Set Number of Axles.
    @param XX_NumberOfAxles Number of Axles */
    public void setXX_NumberOfAxles (int XX_NumberOfAxles)
    {
        set_Value ("XX_NumberOfAxles", Integer.valueOf(XX_NumberOfAxles));
        
    }
    
    /** Get Number of Axles.
    @return Number of Axles */
    public int getXX_NumberOfAxles() 
    {
        return get_ValueAsInt("XX_NumberOfAxles");
        
    }
    
    /** Set Package Quantity.
    @param XX_PackageQuantity Quantity of Packages of an Order Request */
    public void setXX_PackageQuantity (int XX_PackageQuantity)
    {
        set_Value ("XX_PackageQuantity", Integer.valueOf(XX_PackageQuantity));
        
    }
    
    /** Get Package Quantity.
    @return Quantity of Packages of an Order Request */
    public int getXX_PackageQuantity() 
    {
        return get_ValueAsInt("XX_PackageQuantity");
        
    }
    
    /** Cava = CAVA */
    public static final String XX_TYPEVEHICLE_Cava = X_Ref_XX_Ref_TypeVehicle.CAVA.getValue();
    /** Plataforma = PLATAFORMA */
    public static final String XX_TYPEVEHICLE_Plataforma = X_Ref_XX_Ref_TypeVehicle.PLATAFORMA.getValue();
    /** Set Type of Vehicle.
    @param XX_TypeVehicle Type of Vehicle */
    public void setXX_TypeVehicle (String XX_TypeVehicle)
    {
        if (!X_Ref_XX_Ref_TypeVehicle.isValid(XX_TypeVehicle))
        throw new IllegalArgumentException ("XX_TypeVehicle Invalid value - " + XX_TypeVehicle + " - Reference_ID=1000234 - CAVA - PLATAFORMA");
        set_Value ("XX_TypeVehicle", XX_TypeVehicle);
        
    }
    
    /** Get Type of Vehicle.
    @return Type of Vehicle */
    public String getXX_TypeVehicle() 
    {
        return (String)get_Value("XX_TypeVehicle");
        
    }
    
    /** Set Fleet.
    @param XX_VLO_Fleet_ID Fleet */
    public void setXX_VLO_Fleet_ID (int XX_VLO_Fleet_ID)
    {
        if (XX_VLO_Fleet_ID < 1) throw new IllegalArgumentException ("XX_VLO_Fleet_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_Fleet_ID", Integer.valueOf(XX_VLO_Fleet_ID));
        
    }
    
    /** Get Fleet.
    @return Fleet */
    public int getXX_VLO_Fleet_ID() 
    {
        return get_ValueAsInt("XX_VLO_Fleet_ID");
        
    }
    
    
}
