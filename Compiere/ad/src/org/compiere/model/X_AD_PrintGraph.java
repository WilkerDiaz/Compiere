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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for AD_PrintGraph
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintGraph.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintGraph extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintGraph_ID id
    @param trx transaction
    */
    public X_AD_PrintGraph (Ctx ctx, int AD_PrintGraph_ID, Trx trx)
    {
        super (ctx, AD_PrintGraph_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintGraph_ID == 0)
        {
            setAD_PrintFormat_ID (0);
            setAD_PrintGraph_ID (0);
            setData_PrintFormatItem_ID (0);
            setDescription_PrintFormatItem_ID (0);
            setGraphType (null);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintGraph (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=521 */
    public static final int Table_ID=521;
    
    /** TableName=AD_PrintGraph */
    public static final String Table_Name="AD_PrintGraph";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Print Format.
    @param AD_PrintFormat_ID Data Print Format */
    public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
    {
        if (AD_PrintFormat_ID < 1) throw new IllegalArgumentException ("AD_PrintFormat_ID is mandatory.");
        set_Value ("AD_PrintFormat_ID", Integer.valueOf(AD_PrintFormat_ID));
        
    }
    
    /** Get Print Format.
    @return Data Print Format */
    public int getAD_PrintFormat_ID() 
    {
        return get_ValueAsInt("AD_PrintFormat_ID");
        
    }
    
    /** Set Graph.
    @param AD_PrintGraph_ID Graph included in Reports */
    public void setAD_PrintGraph_ID (int AD_PrintGraph_ID)
    {
        if (AD_PrintGraph_ID < 1) throw new IllegalArgumentException ("AD_PrintGraph_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintGraph_ID", Integer.valueOf(AD_PrintGraph_ID));
        
    }
    
    /** Get Graph.
    @return Graph included in Reports */
    public int getAD_PrintGraph_ID() 
    {
        return get_ValueAsInt("AD_PrintGraph_ID");
        
    }
    
    /** Set Data Column 2.
    @param Data1_PrintFormatItem_ID Data Column for Line Charts */
    public void setData1_PrintFormatItem_ID (int Data1_PrintFormatItem_ID)
    {
        if (Data1_PrintFormatItem_ID <= 0) set_Value ("Data1_PrintFormatItem_ID", null);
        else
        set_Value ("Data1_PrintFormatItem_ID", Integer.valueOf(Data1_PrintFormatItem_ID));
        
    }
    
    /** Get Data Column 2.
    @return Data Column for Line Charts */
    public int getData1_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("Data1_PrintFormatItem_ID");
        
    }
    
    /** Set Data Column 3.
    @param Data2_PrintFormatItem_ID Data Column for Line Charts */
    public void setData2_PrintFormatItem_ID (int Data2_PrintFormatItem_ID)
    {
        if (Data2_PrintFormatItem_ID <= 0) set_Value ("Data2_PrintFormatItem_ID", null);
        else
        set_Value ("Data2_PrintFormatItem_ID", Integer.valueOf(Data2_PrintFormatItem_ID));
        
    }
    
    /** Get Data Column 3.
    @return Data Column for Line Charts */
    public int getData2_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("Data2_PrintFormatItem_ID");
        
    }
    
    /** Set Data Column 4.
    @param Data3_PrintFormatItem_ID Data Column for Line Charts */
    public void setData3_PrintFormatItem_ID (int Data3_PrintFormatItem_ID)
    {
        if (Data3_PrintFormatItem_ID <= 0) set_Value ("Data3_PrintFormatItem_ID", null);
        else
        set_Value ("Data3_PrintFormatItem_ID", Integer.valueOf(Data3_PrintFormatItem_ID));
        
    }
    
    /** Get Data Column 4.
    @return Data Column for Line Charts */
    public int getData3_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("Data3_PrintFormatItem_ID");
        
    }
    
    /** Set Data Column 5.
    @param Data4_PrintFormatItem_ID Data Column for Line Charts */
    public void setData4_PrintFormatItem_ID (int Data4_PrintFormatItem_ID)
    {
        if (Data4_PrintFormatItem_ID <= 0) set_Value ("Data4_PrintFormatItem_ID", null);
        else
        set_Value ("Data4_PrintFormatItem_ID", Integer.valueOf(Data4_PrintFormatItem_ID));
        
    }
    
    /** Get Data Column 5.
    @return Data Column for Line Charts */
    public int getData4_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("Data4_PrintFormatItem_ID");
        
    }
    
    /** Set Data Column.
    @param Data_PrintFormatItem_ID Data Column for Pie and Line Charts */
    public void setData_PrintFormatItem_ID (int Data_PrintFormatItem_ID)
    {
        if (Data_PrintFormatItem_ID < 1) throw new IllegalArgumentException ("Data_PrintFormatItem_ID is mandatory.");
        set_Value ("Data_PrintFormatItem_ID", Integer.valueOf(Data_PrintFormatItem_ID));
        
    }
    
    /** Get Data Column.
    @return Data Column for Pie and Line Charts */
    public int getData_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("Data_PrintFormatItem_ID");
        
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
    
    /** Set Description Column.
    @param Description_PrintFormatItem_ID Description Column for Pie/Line/Bar Charts */
    public void setDescription_PrintFormatItem_ID (int Description_PrintFormatItem_ID)
    {
        if (Description_PrintFormatItem_ID < 1) throw new IllegalArgumentException ("Description_PrintFormatItem_ID is mandatory.");
        set_Value ("Description_PrintFormatItem_ID", Integer.valueOf(Description_PrintFormatItem_ID));
        
    }
    
    /** Get Description Column.
    @return Description Column for Pie/Line/Bar Charts */
    public int getDescription_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("Description_PrintFormatItem_ID");
        
    }
    
    /** Bar Chart = B */
    public static final String GRAPHTYPE_BarChart = X_Ref_AD_Print_Graph_Type.BAR_CHART.getValue();
    /** Line Chart = L */
    public static final String GRAPHTYPE_LineChart = X_Ref_AD_Print_Graph_Type.LINE_CHART.getValue();
    /** Pie Chart = P */
    public static final String GRAPHTYPE_PieChart = X_Ref_AD_Print_Graph_Type.PIE_CHART.getValue();
    /** Set Graph Type.
    @param GraphType Type of graph to be painted */
    public void setGraphType (String GraphType)
    {
        if (GraphType == null) throw new IllegalArgumentException ("GraphType is mandatory");
        if (!X_Ref_AD_Print_Graph_Type.isValid(GraphType))
        throw new IllegalArgumentException ("GraphType Invalid value - " + GraphType + " - Reference_ID=265 - B - L - P");
        set_Value ("GraphType", GraphType);
        
    }
    
    /** Get Graph Type.
    @return Type of graph to be painted */
    public String getGraphType() 
    {
        return (String)get_Value("GraphType");
        
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
    
    
}
