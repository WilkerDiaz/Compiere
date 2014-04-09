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
/** Generated Model for M_RoutingOperation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_RoutingOperation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_RoutingOperation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_RoutingOperation_ID id
    @param trx transaction
    */
    public X_M_RoutingOperation (Ctx ctx, int M_RoutingOperation_ID, Trx trx)
    {
        super (ctx, M_RoutingOperation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_RoutingOperation_ID == 0)
        {
            setC_UOM_ID (0);	// @SQL=SELECT C_UOM_ID FROM C_UOM WHERE Name= 'Day'
            setIsHazmat (false);	// N
            setIsOptional (false);	// N
            setIsPermitRequired (false);	// N
            setM_Operation_ID (0);
            setM_RoutingOperation_ID (0);
            setM_Routing_ID (0);
            setM_WorkCenter_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_RoutingOperation WHERE M_Routing_ID=@M_Routing_ID@
            setSetupTime (Env.ZERO);	// 0
            setUnitRuntime (Env.ZERO);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_RoutingOperation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27524964906789L;
    /** Last Updated Timestamp 2009-05-20 10:13:10.0 */
    public static final long updatedMS = 1242839590000L;
    /** AD_Table_ID=1028 */
    public static final int Table_ID=1028;
    
    /** TableName=M_RoutingOperation */
    public static final String Table_Name="M_RoutingOperation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Hazmat.
    @param IsHazmat Involves hazardous materials */
    public void setIsHazmat (boolean IsHazmat)
    {
        set_Value ("IsHazmat", Boolean.valueOf(IsHazmat));
        
    }
    
    /** Get Hazmat.
    @return Involves hazardous materials */
    public boolean isHazmat() 
    {
        return get_ValueAsBoolean("IsHazmat");
        
    }
    
    /** Set Optional.
    @param IsOptional Optional */
    public void setIsOptional (boolean IsOptional)
    {
        set_Value ("IsOptional", Boolean.valueOf(IsOptional));
        
    }
    
    /** Get Optional.
    @return Optional */
    public boolean isOptional() 
    {
        return get_ValueAsBoolean("IsOptional");
        
    }
    
    /** Set Permit Required.
    @param IsPermitRequired Indicates if a permit or similar authorization is required for use or execution of a product, resource or work order operation. */
    public void setIsPermitRequired (boolean IsPermitRequired)
    {
        set_Value ("IsPermitRequired", Boolean.valueOf(IsPermitRequired));
        
    }
    
    /** Get Permit Required.
    @return Indicates if a permit or similar authorization is required for use or execution of a product, resource or work order operation. */
    public boolean isPermitRequired() 
    {
        return get_ValueAsBoolean("IsPermitRequired");
        
    }
    
    /** Set Operation.
    @param M_Operation_ID Manufacturing Operation */
    public void setM_Operation_ID (int M_Operation_ID)
    {
        if (M_Operation_ID < 1) throw new IllegalArgumentException ("M_Operation_ID is mandatory.");
        set_ValueNoCheck ("M_Operation_ID", Integer.valueOf(M_Operation_ID));
        
    }
    
    /** Get Operation.
    @return Manufacturing Operation */
    public int getM_Operation_ID() 
    {
        return get_ValueAsInt("M_Operation_ID");
        
    }
    
    /** Set Routing Operation.
    @param M_RoutingOperation_ID Standard Routing Operation */
    public void setM_RoutingOperation_ID (int M_RoutingOperation_ID)
    {
        if (M_RoutingOperation_ID < 1) throw new IllegalArgumentException ("M_RoutingOperation_ID is mandatory.");
        set_ValueNoCheck ("M_RoutingOperation_ID", Integer.valueOf(M_RoutingOperation_ID));
        
    }
    
    /** Get Routing Operation.
    @return Standard Routing Operation */
    public int getM_RoutingOperation_ID() 
    {
        return get_ValueAsInt("M_RoutingOperation_ID");
        
    }
    
    /** Set Routing.
    @param M_Routing_ID Routing for an assembly */
    public void setM_Routing_ID (int M_Routing_ID)
    {
        if (M_Routing_ID < 1) throw new IllegalArgumentException ("M_Routing_ID is mandatory.");
        set_ValueNoCheck ("M_Routing_ID", Integer.valueOf(M_Routing_ID));
        
    }
    
    /** Get Routing.
    @return Routing for an assembly */
    public int getM_Routing_ID() 
    {
        return get_ValueAsInt("M_Routing_ID");
        
    }
    
    /** Set Standard Operation.
    @param M_StandardOperation_ID Identifies a standard operation template */
    public void setM_StandardOperation_ID (int M_StandardOperation_ID)
    {
        if (M_StandardOperation_ID <= 0) set_ValueNoCheck ("M_StandardOperation_ID", null);
        else
        set_ValueNoCheck ("M_StandardOperation_ID", Integer.valueOf(M_StandardOperation_ID));
        
    }
    
    /** Get Standard Operation.
    @return Identifies a standard operation template */
    public int getM_StandardOperation_ID() 
    {
        return get_ValueAsInt("M_StandardOperation_ID");
        
    }
    
    /** Set Work Center.
    @param M_WorkCenter_ID Identifies a production area within a warehouse consisting of people and equipment */
    public void setM_WorkCenter_ID (int M_WorkCenter_ID)
    {
        if (M_WorkCenter_ID < 1) throw new IllegalArgumentException ("M_WorkCenter_ID is mandatory.");
        set_ValueNoCheck ("M_WorkCenter_ID", Integer.valueOf(M_WorkCenter_ID));
        
    }
    
    /** Get Work Center.
    @return Identifies a production area within a warehouse consisting of people and equipment */
    public int getM_WorkCenter_ID() 
    {
        return get_ValueAsInt("M_WorkCenter_ID");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    /** Set Setup Time.
    @param SetupTime Setup time before starting Production */
    public void setSetupTime (java.math.BigDecimal SetupTime)
    {
        if (SetupTime == null) throw new IllegalArgumentException ("SetupTime is mandatory.");
        set_Value ("SetupTime", SetupTime);
        
    }
    
    /** Get Setup Time.
    @return Setup time before starting Production */
    public java.math.BigDecimal getSetupTime() 
    {
        return get_ValueAsBigDecimal("SetupTime");
        
    }
    
    /** Set Runtime per Unit.
    @param UnitRuntime Time to produce one unit */
    public void setUnitRuntime (java.math.BigDecimal UnitRuntime)
    {
        if (UnitRuntime == null) throw new IllegalArgumentException ("UnitRuntime is mandatory.");
        set_Value ("UnitRuntime", UnitRuntime);
        
    }
    
    /** Get Runtime per Unit.
    @return Time to produce one unit */
    public java.math.BigDecimal getUnitRuntime() 
    {
        return get_ValueAsBigDecimal("UnitRuntime");
        
    }
    
    
}
