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
/** Generated Model for I_MasterDemand
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_MasterDemand.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_MasterDemand extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_MasterDemand_ID id
    @param trx transaction
    */
    public X_I_MasterDemand (Ctx ctx, int I_MasterDemand_ID, Trx trx)
    {
        super (ctx, I_MasterDemand_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_MasterDemand_ID == 0)
        {
            setI_IsImported (null);	// N
            setI_MasterDemand_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_MasterDemand (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27532675665789L;
    /** Last Updated Timestamp 2009-08-17 16:05:49.0 */
    public static final long updatedMS = 1250550349000L;
    /** AD_Table_ID=2112 */
    public static final int Table_ID=2112;
    
    /** TableName=I_MasterDemand */
    public static final String Table_Name="I_MasterDemand";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID <= 0) set_Value ("C_Period_ID", null);
        else
        set_Value ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_Value ("C_UOM_ID", null);
        else
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
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set I_MasterDemand_ID.
    @param I_MasterDemand_ID Identifies a master demand */
    public void setI_MasterDemand_ID (int I_MasterDemand_ID)
    {
        if (I_MasterDemand_ID < 1) throw new IllegalArgumentException ("I_MasterDemand_ID is mandatory.");
        set_ValueNoCheck ("I_MasterDemand_ID", Integer.valueOf(I_MasterDemand_ID));
        
    }
    
    /** Get I_MasterDemand_ID.
    @return Identifies a master demand */
    public int getI_MasterDemand_ID() 
    {
        return get_ValueAsInt("I_MasterDemand_ID");
        
    }
    
    /** Set Master Demand Line.
    @param MRP_MasterDemandLine_ID Master Demand line */
    public void setMRP_MasterDemandLine_ID (int MRP_MasterDemandLine_ID)
    {
        if (MRP_MasterDemandLine_ID <= 0) set_Value ("MRP_MasterDemandLine_ID", null);
        else
        set_Value ("MRP_MasterDemandLine_ID", Integer.valueOf(MRP_MasterDemandLine_ID));
        
    }
    
    /** Get Master Demand Line.
    @return Master Demand line */
    public int getMRP_MasterDemandLine_ID() 
    {
        return get_ValueAsInt("MRP_MasterDemandLine_ID");
        
    }
    
    /** Set Master Demand.
    @param MRP_MasterDemand_ID Master Demand for material requirements */
    public void setMRP_MasterDemand_ID (int MRP_MasterDemand_ID)
    {
        if (MRP_MasterDemand_ID <= 0) set_Value ("MRP_MasterDemand_ID", null);
        else
        set_Value ("MRP_MasterDemand_ID", Integer.valueOf(MRP_MasterDemand_ID));
        
    }
    
    /** Get Master Demand.
    @return Master Demand for material requirements */
    public int getMRP_MasterDemand_ID() 
    {
        return get_ValueAsInt("MRP_MasterDemand_ID");
        
    }
    
    /** Set Plan.
    @param MRP_Plan_ID Material Requirements Plan */
    public void setMRP_Plan_ID (int MRP_Plan_ID)
    {
        if (MRP_Plan_ID <= 0) set_Value ("MRP_Plan_ID", null);
        else
        set_Value ("MRP_Plan_ID", Integer.valueOf(MRP_Plan_ID));
        
    }
    
    /** Get Plan.
    @return Material Requirements Plan */
    public int getMRP_Plan_ID() 
    {
        return get_ValueAsInt("MRP_Plan_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Name 2.
    @param Name2 Additional Name */
    public void setName2 (String Name2)
    {
        set_Value ("Name2", Name2);
        
    }
    
    /** Get Name 2.
    @return Additional Name */
    public String getName2() 
    {
        return (String)get_Value("Name2");
        
    }
    
    /** Set Period Name.
    @param PeriodName Period */
    public void setPeriodName (String PeriodName)
    {
        set_Value ("PeriodName", PeriodName);
        
    }
    
    /** Get Period Name.
    @return Period */
    public String getPeriodName() 
    {
        return (String)get_Value("PeriodName");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_Value ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        set_Value ("ProductValue", ProductValue);
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getProductValue());
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    
}
