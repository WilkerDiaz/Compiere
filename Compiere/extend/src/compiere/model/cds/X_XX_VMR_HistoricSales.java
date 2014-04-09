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
/** Generated Model for XX_VMR_HistoricSales
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_HistoricSales extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_HistoricSales_ID id
    @param trx transaction
    */
    public X_XX_VMR_HistoricSales (Ctx ctx, int XX_VMR_HistoricSales_ID, Trx trx)
    {
        super (ctx, XX_VMR_HistoricSales_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_HistoricSales_ID == 0)
        {
            setValue (null);
            setXX_VMR_HistoricSales_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_HistoricSales (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27641120680789L;
    /** Last Updated Timestamp 2013-01-23 22:12:44.0 */
    public static final long updatedMS = 1358995364000L;
    /** AD_Table_ID=1003653 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_HistoricSales");
        
    }
    ;
    
    /** TableName=XX_VMR_HistoricSales */
    public static final String Table_Name="XX_VMR_HistoricSales";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
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
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
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
    
    /** Set XX_VMR_HistoricSales_ID.
    @param XX_VMR_HistoricSales_ID XX_VMR_HistoricSales_ID */
    public void setXX_VMR_HistoricSales_ID (int XX_VMR_HistoricSales_ID)
    {
        if (XX_VMR_HistoricSales_ID < 1) throw new IllegalArgumentException ("XX_VMR_HistoricSales_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_HistoricSales_ID", Integer.valueOf(XX_VMR_HistoricSales_ID));
        
    }
    
    /** Get XX_VMR_HistoricSales_ID.
    @return XX_VMR_HistoricSales_ID */
    public int getXX_VMR_HistoricSales_ID() 
    {
        return get_ValueAsInt("XX_VMR_HistoricSales_ID");
        
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
    
    /** Set Sales acumulated amount.
    @param XX_VMR_SalesAcumAmount Sales Acumulated Amount */
    public void setXX_VMR_SalesAcumAmount (java.math.BigDecimal XX_VMR_SalesAcumAmount)
    {
        set_Value ("XX_VMR_SalesAcumAmount", XX_VMR_SalesAcumAmount);
        
    }
    
    /** Get Sales acumulated amount.
    @return Sales Acumulated Amount */
    public java.math.BigDecimal getXX_VMR_SalesAcumAmount() 
    {
        return get_ValueAsBigDecimal("XX_VMR_SalesAcumAmount");
        
    }
    
    /** Set Sales acumulated pieces.
    @param XX_VMR_SalesAcumPieces Sales acumulated pieces */
    public void setXX_VMR_SalesAcumPieces (int XX_VMR_SalesAcumPieces)
    {
        set_Value ("XX_VMR_SalesAcumPieces", Integer.valueOf(XX_VMR_SalesAcumPieces));
        
    }
    
    /** Get Sales acumulated pieces.
    @return Sales acumulated pieces */
    public int getXX_VMR_SalesAcumPieces() 
    {
        return get_ValueAsInt("XX_VMR_SalesAcumPieces");
        
    }
    
    /** Set Sales budget amount.
    @param XX_VMR_SalesButgetAmount Sales budget amount */
    public void setXX_VMR_SalesButgetAmount (java.math.BigDecimal XX_VMR_SalesButgetAmount)
    {
        set_Value ("XX_VMR_SalesButgetAmount", XX_VMR_SalesButgetAmount);
        
    }
    
    /** Get Sales budget amount.
    @return Sales budget amount */
    public java.math.BigDecimal getXX_VMR_SalesButgetAmount() 
    {
        return get_ValueAsBigDecimal("XX_VMR_SalesButgetAmount");
        
    }
    
    /** Set Sales Date.
    @param XX_VMR_SalesDate Sales Date */
    public void setXX_VMR_SalesDate (Timestamp XX_VMR_SalesDate)
    {
        set_Value ("XX_VMR_SalesDate", XX_VMR_SalesDate);
        
    }
    
    /** Get Sales Date.
    @return Sales Date */
    public Timestamp getXX_VMR_SalesDate() 
    {
        return (Timestamp)get_Value("XX_VMR_SalesDate");
        
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
    
    
}
