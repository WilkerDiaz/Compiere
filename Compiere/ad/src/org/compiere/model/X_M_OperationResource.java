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
/** Generated Model for M_OperationResource
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_OperationResource.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_OperationResource extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_OperationResource_ID id
    @param trx transaction
    */
    public X_M_OperationResource (Ctx ctx, int M_OperationResource_ID, Trx trx)
    {
        super (ctx, M_OperationResource_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_OperationResource_ID == 0)
        {
            setM_OperationResource_ID (0);
            setM_ProductOperation_ID (0);
            setName (null);
            setSetupTime (Env.ZERO);
            setTeardownTime (Env.ZERO);
            setUnitRuntime (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_OperationResource (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=797 */
    public static final int Table_ID=797;
    
    /** TableName=M_OperationResource */
    public static final String Table_Name="M_OperationResource";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_Value ("A_Asset_ID", null);
        else
        set_Value ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Position.
    @param C_Job_ID Job Position */
    public void setC_Job_ID (int C_Job_ID)
    {
        if (C_Job_ID <= 0) set_Value ("C_Job_ID", null);
        else
        set_Value ("C_Job_ID", Integer.valueOf(C_Job_ID));
        
    }
    
    /** Get Position.
    @return Job Position */
    public int getC_Job_ID() 
    {
        return get_ValueAsInt("C_Job_ID");
        
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
    
    /** Set Operation Resource.
    @param M_OperationResource_ID Product Operation Resource */
    public void setM_OperationResource_ID (int M_OperationResource_ID)
    {
        if (M_OperationResource_ID < 1) throw new IllegalArgumentException ("M_OperationResource_ID is mandatory.");
        set_ValueNoCheck ("M_OperationResource_ID", Integer.valueOf(M_OperationResource_ID));
        
    }
    
    /** Get Operation Resource.
    @return Product Operation Resource */
    public int getM_OperationResource_ID() 
    {
        return get_ValueAsInt("M_OperationResource_ID");
        
    }
    
    /** Set Product Operation.
    @param M_ProductOperation_ID Product Manufacturing Operation */
    public void setM_ProductOperation_ID (int M_ProductOperation_ID)
    {
        if (M_ProductOperation_ID < 1) throw new IllegalArgumentException ("M_ProductOperation_ID is mandatory.");
        set_ValueNoCheck ("M_ProductOperation_ID", Integer.valueOf(M_ProductOperation_ID));
        
    }
    
    /** Get Product Operation.
    @return Product Manufacturing Operation */
    public int getM_ProductOperation_ID() 
    {
        return get_ValueAsInt("M_ProductOperation_ID");
        
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
    
    /** Set Teardown Time.
    @param TeardownTime Time at the end of the operation */
    public void setTeardownTime (java.math.BigDecimal TeardownTime)
    {
        if (TeardownTime == null) throw new IllegalArgumentException ("TeardownTime is mandatory.");
        set_Value ("TeardownTime", TeardownTime);
        
    }
    
    /** Get Teardown Time.
    @return Time at the end of the operation */
    public java.math.BigDecimal getTeardownTime() 
    {
        return get_ValueAsBigDecimal("TeardownTime");
        
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
