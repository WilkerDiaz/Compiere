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
/** Generated Model for I_XX_VMR_IncreaseFactor
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_IncreaseFactor extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_IncreaseFactor_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_IncreaseFactor (Ctx ctx, int I_XX_VMR_IncreaseFactor_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_IncreaseFactor_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_IncreaseFactor_ID == 0)
        {
            setI_XX_VMR_INCREASEFACTOR_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_IncreaseFactor (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27558319350789L;
    /** Last Updated Timestamp 2010-06-10 13:50:34.0 */
    public static final long updatedMS = 1276194034000L;
    /** AD_Table_ID=1000319 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_IncreaseFactor");
        
    }
    ;
    
    /** TableName=I_XX_VMR_IncreaseFactor */
    public static final String Table_Name="I_XX_VMR_IncreaseFactor";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
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
    
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set I_XX_VMR_INCREASEFACTOR_ID.
    @param I_XX_VMR_INCREASEFACTOR_ID I_XX_VMR_INCREASEFACTOR_ID */
    public void setI_XX_VMR_INCREASEFACTOR_ID (int I_XX_VMR_INCREASEFACTOR_ID)
    {
        if (I_XX_VMR_INCREASEFACTOR_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_INCREASEFACTOR_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_INCREASEFACTOR_ID", Integer.valueOf(I_XX_VMR_INCREASEFACTOR_ID));
        
    }
    
    /** Get I_XX_VMR_INCREASEFACTOR_ID.
    @return I_XX_VMR_INCREASEFACTOR_ID */
    public int getI_XX_VMR_INCREASEFACTOR_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_INCREASEFACTOR_ID");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
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
    
    /** Set Country Code.
    @param XX_CountryCode Country Code */
    public void setXX_CountryCode (String XX_CountryCode)
    {
        set_Value ("XX_CountryCode", XX_CountryCode);
        
    }
    
    /** Get Country Code.
    @return Country Code */
    public String getXX_CountryCode() 
    {
        return (String)get_Value("XX_CountryCode");
        
    }
    
    /** Set Department Code.
    @param XX_DepartmentCode Codigo de Departamento */
    public void setXX_DepartmentCode (String XX_DepartmentCode)
    {
        set_Value ("XX_DepartmentCode", XX_DepartmentCode);
        
    }
    
    /** Get Department Code.
    @return Codigo de Departamento */
    public String getXX_DepartmentCode() 
    {
        return (String)get_Value("XX_DepartmentCode");
        
    }
    
    /** Set Dispatch Route Code.
    @param XX_DISPATCHROUTECODE Dispatch Route Code */
    public void setXX_DISPATCHROUTECODE (String XX_DISPATCHROUTECODE)
    {
        set_Value ("XX_DISPATCHROUTECODE", XX_DISPATCHROUTECODE);
        
    }
    
    /** Get Dispatch Route Code.
    @return Dispatch Route Code */
    public String getXX_DISPATCHROUTECODE() 
    {
        return (String)get_Value("XX_DISPATCHROUTECODE");
        
    }
    
    /** Set Increase Factor.
    @param XX_IncreaseFactor Increase Factor */
    public void setXX_IncreaseFactor (java.math.BigDecimal XX_IncreaseFactor)
    {
        set_Value ("XX_IncreaseFactor", XX_IncreaseFactor);
        
    }
    
    /** Get Increase Factor.
    @return Increase Factor */
    public java.math.BigDecimal getXX_IncreaseFactor() 
    {
        return get_ValueAsBigDecimal("XX_IncreaseFactor");
        
    }
    
    /** Set Dispatch Route.
    @param XX_VLO_DispatchRoute_ID ID de vía de despacho */
    public void setXX_VLO_DispatchRoute_ID (int XX_VLO_DispatchRoute_ID)
    {
        if (XX_VLO_DispatchRoute_ID <= 0) set_Value ("XX_VLO_DispatchRoute_ID", null);
        else
        set_Value ("XX_VLO_DispatchRoute_ID", Integer.valueOf(XX_VLO_DispatchRoute_ID));
        
    }
    
    /** Get Dispatch Route.
    @return ID de vía de despacho */
    public int getXX_VLO_DispatchRoute_ID() 
    {
        return get_ValueAsInt("XX_VLO_DispatchRoute_ID");
        
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
    
    /** Set Increase Factor.
    @param XX_VMR_IncreaseFactor_ID Increase Factor ID */
    public void setXX_VMR_IncreaseFactor_ID (int XX_VMR_IncreaseFactor_ID)
    {
        if (XX_VMR_IncreaseFactor_ID <= 0) set_Value ("XX_VMR_IncreaseFactor_ID", null);
        else
        set_Value ("XX_VMR_IncreaseFactor_ID", Integer.valueOf(XX_VMR_IncreaseFactor_ID));
        
    }
    
    /** Get Increase Factor.
    @return Increase Factor ID */
    public int getXX_VMR_IncreaseFactor_ID() 
    {
        return get_ValueAsInt("XX_VMR_IncreaseFactor_ID");
        
    }
    
    
}
