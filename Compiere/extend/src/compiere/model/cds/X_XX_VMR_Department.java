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
/** Generated Model for XX_VMR_Department
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Department extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Department_ID id
    @param trx transaction
    */
    public X_XX_VMR_Department (Ctx ctx, int XX_VMR_Department_ID, Trx trx)
    {
        super (ctx, XX_VMR_Department_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Department_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VMR_Category_ID (0);
            setXX_VMR_Department_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Department (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566856908789L;
    /** Last Updated Timestamp 2010-09-17 09:23:12.0 */
    public static final long updatedMS = 1284731592000L;
    /** AD_Table_ID=1000035 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Department");
        
    }
    ;
    
    /** TableName=XX_VMR_Department */
    public static final String Table_Name="XX_VMR_Department";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
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
    
    /** Set Inventory Schedule.
    @param XX_InventorySchedule_ID Planificador de Inventario */
    public void setXX_InventorySchedule_ID (int XX_InventorySchedule_ID)
    {
        if (XX_InventorySchedule_ID <= 0) set_Value ("XX_InventorySchedule_ID", null);
        else
        set_Value ("XX_InventorySchedule_ID", Integer.valueOf(XX_InventorySchedule_ID));
        
    }
    
    /** Get Inventory Schedule.
    @return Planificador de Inventario */
    public int getXX_InventorySchedule_ID() 
    {
        return get_ValueAsInt("XX_InventorySchedule_ID");
        
    }
    
    /** Set User Asistant.
    @param XX_UserAssistant_ID Usuario asistente */
    public void setXX_UserAssistant_ID (int XX_UserAssistant_ID)
    {
        if (XX_UserAssistant_ID <= 0) set_Value ("XX_UserAssistant_ID", null);
        else
        set_Value ("XX_UserAssistant_ID", Integer.valueOf(XX_UserAssistant_ID));
        
    }
    
    /** Get User Asistant.
    @return Usuario asistente */
    public int getXX_UserAssistant_ID() 
    {
        return get_ValueAsInt("XX_UserAssistant_ID");
        
    }
    
    /** Set User Buyer.
    @param XX_UserBuyer_ID Usuario Comprador */
    public void setXX_UserBuyer_ID (int XX_UserBuyer_ID)
    {
        if (XX_UserBuyer_ID <= 0) set_Value ("XX_UserBuyer_ID", null);
        else
        set_Value ("XX_UserBuyer_ID", Integer.valueOf(XX_UserBuyer_ID));
        
    }
    
    /** Get User Buyer.
    @return Usuario Comprador */
    public int getXX_UserBuyer_ID() 
    {
        return get_ValueAsInt("XX_UserBuyer_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID < 1) throw new IllegalArgumentException ("XX_VMR_Category_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID < 1) throw new IllegalArgumentException ("XX_VMR_Department_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Product Classification.
    @param XX_VMR_ProductClass_ID Clase de Productos que vende el Proveedor */
    public void setXX_VMR_ProductClass_ID (int XX_VMR_ProductClass_ID)
    {
        if (XX_VMR_ProductClass_ID <= 0) set_Value ("XX_VMR_ProductClass_ID", null);
        else
        set_Value ("XX_VMR_ProductClass_ID", Integer.valueOf(XX_VMR_ProductClass_ID));
        
    }
    
    /** Get Product Classification.
    @return Clase de Productos que vende el Proveedor */
    public int getXX_VMR_ProductClass_ID() 
    {
        return get_ValueAsInt("XX_VMR_ProductClass_ID");
        
    }
    
    
}
