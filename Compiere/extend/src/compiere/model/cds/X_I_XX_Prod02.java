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
/** Generated Model for I_XX_Prod02
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_Prod02 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_Prod02_ID id
    @param trx transaction
    */
    public X_I_XX_Prod02 (Ctx ctx, int I_XX_Prod02_ID, Trx trx)
    {
        super (ctx, I_XX_Prod02_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_Prod02_ID == 0)
        {
            setI_XX_PROD02_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_Prod02 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567205387789L;
    /** Last Updated Timestamp 2010-09-21 10:11:11.0 */
    public static final long updatedMS = 1285080071000L;
    /** AD_Table_ID=1000097 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_Prod02");
        
    }
    ;
    
    /** TableName=I_XX_Prod02 */
    public static final String Table_Name="I_XX_Prod02";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set I_XX_PROD02_ID.
    @param I_XX_PROD02_ID I_XX_PROD02_ID */
    public void setI_XX_PROD02_ID (int I_XX_PROD02_ID)
    {
        if (I_XX_PROD02_ID < 1) throw new IllegalArgumentException ("I_XX_PROD02_ID is mandatory.");
        set_ValueNoCheck ("I_XX_PROD02_ID", Integer.valueOf(I_XX_PROD02_ID));
        
    }
    
    /** Get I_XX_PROD02_ID.
    @return I_XX_PROD02_ID */
    public int getI_XX_PROD02_ID() 
    {
        return get_ValueAsInt("I_XX_PROD02_ID");
        
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
    
    /** Set Department Code	.
    @param XX_CodDep Codigo del Departameto */
    public void setXX_CodDep (String XX_CodDep)
    {
        set_Value ("XX_CodDep", XX_CodDep);
        
    }
    
    /** Get Department Code	.
    @return Codigo del Departameto */
    public String getXX_CodDep() 
    {
        return (String)get_Value("XX_CodDep");
        
    }
    
    /** Set Code Line.
    @param XX_CodLin Codigo de Linea */
    public void setXX_CodLin (String XX_CodLin)
    {
        set_Value ("XX_CodLin", XX_CodLin);
        
    }
    
    /** Get Code Line.
    @return Codigo de Linea */
    public String getXX_CodLin() 
    {
        return (String)get_Value("XX_CodLin");
        
    }
    
    /** Set Code Section.
    @param XX_Codsec Codigo de Seccion */
    public void setXX_Codsec (String XX_Codsec)
    {
        set_Value ("XX_Codsec", XX_Codsec);
        
    }
    
    /** Get Code Section.
    @return Codigo de Seccion */
    public String getXX_Codsec() 
    {
        return (String)get_Value("XX_Codsec");
        
    }
    
    /** Set XX_Contbl.
    @param XX_Contbl Consecutivo de tabla */
    public void setXX_Contbl (java.math.BigDecimal XX_Contbl)
    {
        set_Value ("XX_Contbl", XX_Contbl);
        
    }
    
    /** Get XX_Contbl.
    @return Consecutivo de tabla */
    public java.math.BigDecimal getXX_Contbl() 
    {
        return get_ValueAsBigDecimal("XX_Contbl");
        
    }
    
    /** Set Charactertistic name.
    @param XX_NomCar Nombre de caracterÝstica */
    public void setXX_NomCar (String XX_NomCar)
    {
        set_Value ("XX_NomCar", XX_NomCar);
        
    }
    
    /** Get Charactertistic name.
    @return Nombre de caracterÝstica */
    public String getXX_NomCar() 
    {
        return (String)get_Value("XX_NomCar");
        
    }
    
    /** Set Characteristic type.
    @param XX_TipCar Tipo de caracterÝstica */
    public void setXX_TipCar (java.math.BigDecimal XX_TipCar)
    {
        set_Value ("XX_TipCar", XX_TipCar);
        
    }
    
    /** Get Characteristic type.
    @return Tipo de caracterÝstica */
    public java.math.BigDecimal getXX_TipCar() 
    {
        return get_ValueAsBigDecimal("XX_TipCar");
        
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
    
    /** Set XX_VMR_DynamicCharact_ID.
    @param XX_VMR_DynamicCharact_ID Dynamic Charactistic ID */
    public void setXX_VMR_DynamicCharact_ID (java.math.BigDecimal XX_VMR_DynamicCharact_ID)
    {
        set_Value ("XX_VMR_DynamicCharact_ID", XX_VMR_DynamicCharact_ID);
        
    }
    
    /** Get XX_VMR_DynamicCharact_ID.
    @return Dynamic Charactistic ID */
    public java.math.BigDecimal getXX_VMR_DynamicCharact_ID() 
    {
        return get_ValueAsBigDecimal("XX_VMR_DynamicCharact_ID");
        
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
