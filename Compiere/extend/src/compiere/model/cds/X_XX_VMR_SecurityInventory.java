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
/** Generated Model for XX_VMR_SecurityInventory
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_SecurityInventory extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_SecurityInventory_ID id
    @param trx transaction
    */
    public X_XX_VMR_SecurityInventory (Ctx ctx, int XX_VMR_SecurityInventory_ID, Trx trx)
    {
        super (ctx, XX_VMR_SecurityInventory_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_SecurityInventory_ID == 0)
        {
            setXX_VMR_SECURITYINVENTORY_ID (0);
            setXX_VMR_VendorProdRef_ID (0);	// @XX_VMR_VendorProdRef_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_SecurityInventory (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27614665479789L;
    /** Last Updated Timestamp 2012-03-23 17:32:43.0 */
    public static final long updatedMS = 1332540163000L;
    /** AD_Table_ID=1001853 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_SecurityInventory");
        
    }
    ;
    
    /** TableName=XX_VMR_SecurityInventory */
    public static final String Table_Name="XX_VMR_SecurityInventory";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Inventory.
    @param Inventory Inventory */
    public void setInventory (int Inventory)
    {
        set_Value ("Inventory", Integer.valueOf(Inventory));
        
    }
    
    /** Get Inventory.
    @return Inventory */
    public int getInventory() 
    {
        return get_ValueAsInt("Inventory");
        
    }
    
    /** Set Centro de Distribución.
    @param T001 Centro de Distribución */
    public void setT001 (int T001)
    {
        set_Value ("T001", Integer.valueOf(T001));
        
    }
    
    /** Get Centro de Distribución.
    @return Centro de Distribución */
    public int getT001() 
    {
        return get_ValueAsInt("T001");
        
    }
    
    /** Set Puente Yanes.
    @param T002 Puente Yanes */
    public void setT002 (int T002)
    {
        set_Value ("T002", Integer.valueOf(T002));
        
    }
    
    /** Get Puente Yanes.
    @return Puente Yanes */
    public int getT002() 
    {
        return get_ValueAsInt("T002");
        
    }
    
    /** Set Chacaito.
    @param T003 Chacaito */
    public void setT003 (int T003)
    {
        set_Value ("T003", Integer.valueOf(T003));
        
    }
    
    /** Get Chacaito.
    @return Chacaito */
    public int getT003() 
    {
        return get_ValueAsInt("T003");
        
    }
    
    /** Set Tamanaco.
    @param T007 Tamanaco */
    public void setT007 (int T007)
    {
        set_Value ("T007", Integer.valueOf(T007));
        
    }
    
    /** Get Tamanaco.
    @return Tamanaco */
    public int getT007() 
    {
        return get_ValueAsInt("T007");
        
    }
    
    /** Set La Granja.
    @param T009 La Granja */
    public void setT009 (int T009)
    {
        set_Value ("T009", Integer.valueOf(T009));
        
    }
    
    /** Get La Granja.
    @return La Granja */
    public int getT009() 
    {
        return get_ValueAsInt("T009");
        
    }
    
    /** Set Las Trinitarias.
    @param T010 Las Trinitarias */
    public void setT010 (int T010)
    {
        set_Value ("T010", Integer.valueOf(T010));
        
    }
    
    /** Get Las Trinitarias.
    @return Las Trinitarias */
    public int getT010() 
    {
        return get_ValueAsInt("T010");
        
    }
    
    /** Set La Trinidad.
    @param T015 La Trinidad */
    public void setT015 (int T015)
    {
        set_Value ("T015", Integer.valueOf(T015));
        
    }
    
    /** Get La Trinidad.
    @return La Trinidad */
    public int getT015() 
    {
        return get_ValueAsInt("T015");
        
    }
    
    /** Set Maracaibo.
    @param T016 Maracaibo */
    public void setT016 (int T016)
    {
        set_Value ("T016", Integer.valueOf(T016));
        
    }
    
    /** Get Maracaibo.
    @return Maracaibo */
    public int getT016() 
    {
        return get_ValueAsInt("T016");
        
    }
    
    /** Set T017.
    @param T017 T017 */
    public void setT017 (int T017)
    {
        set_Value ("T017", Integer.valueOf(T017));
        
    }
    
    /** Get T017.
    @return T017 */
    public int getT017() 
    {
        return get_ValueAsInt("T017");
        
    }
    
    /** Set XX_VMR_SECURITYINVENTORY_ID.
    @param XX_VMR_SECURITYINVENTORY_ID XX_VMR_SECURITYINVENTORY_ID */
    public void setXX_VMR_SECURITYINVENTORY_ID (int XX_VMR_SECURITYINVENTORY_ID)
    {
        if (XX_VMR_SECURITYINVENTORY_ID < 1) throw new IllegalArgumentException ("XX_VMR_SECURITYINVENTORY_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_SECURITYINVENTORY_ID", Integer.valueOf(XX_VMR_SECURITYINVENTORY_ID));
        
    }
    
    /** Get XX_VMR_SECURITYINVENTORY_ID.
    @return XX_VMR_SECURITYINVENTORY_ID */
    public int getXX_VMR_SECURITYINVENTORY_ID() 
    {
        return get_ValueAsInt("XX_VMR_SECURITYINVENTORY_ID");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID < 1) throw new IllegalArgumentException ("XX_VMR_VendorProdRef_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
