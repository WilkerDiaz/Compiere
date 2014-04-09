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
/** Generated Model for XX_VMR_Prld03
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Prld03 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Prld03_ID id
    @param trx transaction
    */
    public X_XX_VMR_Prld03 (Ctx ctx, int XX_VMR_Prld03_ID, Trx trx)
    {
        super (ctx, XX_VMR_Prld03_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Prld03_ID == 0)
        {
            setXX_VMR_PRLD03_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Prld03 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27589149651789L;
    /** Last Updated Timestamp 2011-06-02 09:48:55.0 */
    public static final long updatedMS = 1307024335000L;
    /** AD_Table_ID=1000099 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Prld03");
        
    }
    ;
    
    /** TableName=XX_VMR_Prld03 */
    public static final String Table_Name="XX_VMR_Prld03";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set XX_BUDGETDAY.
    @param XX_BUDGETDAY XX_BUDGETDAY */
    public void setXX_BUDGETDAY (int XX_BUDGETDAY)
    {
        set_Value ("XX_BUDGETDAY", Integer.valueOf(XX_BUDGETDAY));
        
    }
    
    /** Get XX_BUDGETDAY.
    @return XX_BUDGETDAY */
    public int getXX_BUDGETDAY() 
    {
        return get_ValueAsInt("XX_BUDGETDAY");
        
    }
    
    /** Set Moneje.
    @param XX_MONEJE Moneje */
    public void setXX_MONEJE (java.math.BigDecimal XX_MONEJE)
    {
        set_Value ("XX_MONEJE", XX_MONEJE);
        
    }
    
    /** Get Moneje.
    @return Moneje */
    public java.math.BigDecimal getXX_MONEJE() 
    {
        return get_ValueAsBigDecimal("XX_MONEJE");
        
    }
    
    /** Set XX_MONESTIR.
    @param XX_MONESTIR XX_MONESTIR */
    public void setXX_MONESTIR (java.math.BigDecimal XX_MONESTIR)
    {
        set_Value ("XX_MONESTIR", XX_MONESTIR);
        
    }
    
    /** Get XX_MONESTIR.
    @return XX_MONESTIR */
    public java.math.BigDecimal getXX_MONESTIR() 
    {
        return get_ValueAsBigDecimal("XX_MONESTIR");
        
    }
    
    /** Set XX_MONTHBUDGET.
    @param XX_MONTHBUDGET XX_MONTHBUDGET */
    public void setXX_MONTHBUDGET (int XX_MONTHBUDGET)
    {
        set_Value ("XX_MONTHBUDGET", Integer.valueOf(XX_MONTHBUDGET));
        
    }
    
    /** Get XX_MONTHBUDGET.
    @return XX_MONTHBUDGET */
    public int getXX_MONTHBUDGET() 
    {
        return get_ValueAsInt("XX_MONTHBUDGET");
        
    }
    
    /** Set XX_REGISTRATIONSTATUS.
    @param XX_REGISTRATIONSTATUS XX_REGISTRATIONSTATUS */
    public void setXX_REGISTRATIONSTATUS (String XX_REGISTRATIONSTATUS)
    {
        set_Value ("XX_REGISTRATIONSTATUS", XX_REGISTRATIONSTATUS);
        
    }
    
    /** Get XX_REGISTRATIONSTATUS.
    @return XX_REGISTRATIONSTATUS */
    public String getXX_REGISTRATIONSTATUS() 
    {
        return (String)get_Value("XX_REGISTRATIONSTATUS");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
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
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set XX_VMR_PRLD03_ID.
    @param XX_VMR_PRLD03_ID XX_VMR_PRLD03_ID */
    public void setXX_VMR_PRLD03_ID (int XX_VMR_PRLD03_ID)
    {
        if (XX_VMR_PRLD03_ID < 1) throw new IllegalArgumentException ("XX_VMR_PRLD03_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PRLD03_ID", Integer.valueOf(XX_VMR_PRLD03_ID));
        
    }
    
    /** Get XX_VMR_PRLD03_ID.
    @return XX_VMR_PRLD03_ID */
    public int getXX_VMR_PRLD03_ID() 
    {
        return get_ValueAsInt("XX_VMR_PRLD03_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    /** Set XX_YEARBUDGET.
    @param XX_YEARBUDGET XX_YEARBUDGET */
    public void setXX_YEARBUDGET (int XX_YEARBUDGET)
    {
        set_Value ("XX_YEARBUDGET", Integer.valueOf(XX_YEARBUDGET));
        
    }
    
    /** Get XX_YEARBUDGET.
    @return XX_YEARBUDGET */
    public int getXX_YEARBUDGET() 
    {
        return get_ValueAsInt("XX_YEARBUDGET");
        
    }
    
    
}
