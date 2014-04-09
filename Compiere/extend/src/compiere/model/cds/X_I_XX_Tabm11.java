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
/** Generated Model for I_XX_Tabm11
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_Tabm11 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_Tabm11_ID id
    @param trx transaction
    */
    public X_I_XX_Tabm11 (Ctx ctx, int I_XX_Tabm11_ID, Trx trx)
    {
        super (ctx, I_XX_Tabm11_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_Tabm11_ID == 0)
        {
            setI_XX_TABM11_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_Tabm11 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27545019799789L;
    /** Last Updated Timestamp 2010-01-07 15:31:23.0 */
    public static final long updatedMS = 1262894483000L;
    /** AD_Table_ID=1000062 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_Tabm11");
        
    }
    ;
    
    /** TableName=I_XX_Tabm11 */
    public static final String Table_Name="I_XX_Tabm11";
    
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
    
    /** Set I_XX_TABM11_ID.
    @param I_XX_TABM11_ID I_XX_TABM11_ID */
    public void setI_XX_TABM11_ID (int I_XX_TABM11_ID)
    {
        if (I_XX_TABM11_ID < 1) throw new IllegalArgumentException ("I_XX_TABM11_ID is mandatory.");
        set_ValueNoCheck ("I_XX_TABM11_ID", Integer.valueOf(I_XX_TABM11_ID));
        
    }
    
    /** Get I_XX_TABM11_ID.
    @return I_XX_TABM11_ID */
    public int getI_XX_TABM11_ID() 
    {
        return get_ValueAsInt("I_XX_TABM11_ID");
        
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
    
    /** Set Category Code.
    @param XX_CodCat Codigo de Categoria */
    public void setXX_CodCat (int XX_CodCat)
    {
        set_Value ("XX_CodCat", Integer.valueOf(XX_CodCat));
        
    }
    
    /** Get Category Code.
    @return Codigo de Categoria */
    public int getXX_CodCat() 
    {
        return get_ValueAsInt("XX_CodCat");
        
    }
    
    /** Set Closing Cause Code.
    @param XX_CodCauCie Código de causa de cierre */
    public void setXX_CodCauCie (int XX_CodCauCie)
    {
        set_Value ("XX_CodCauCie", Integer.valueOf(XX_CodCauCie));
        
    }
    
    /** Get Closing Cause Code.
    @return Código de causa de cierre */
    public int getXX_CodCauCie() 
    {
        return get_ValueAsInt("XX_CodCauCie");
        
    }
    
    /** Set Boss Category Value.
    @param XX_CodJefCat Value de jefe de categoría */
    public void setXX_CodJefCat (String XX_CodJefCat)
    {
        set_Value ("XX_CodJefCat", XX_CodJefCat);
        
    }
    
    /** Get Boss Category Value.
    @return Value de jefe de categoría */
    public String getXX_CodJefCat() 
    {
        return (String)get_Value("XX_CodJefCat");
        
    }
    
    /** Set Boss Category Code.
    @param XX_CodJefCat_ID Codigo de jefe de categoría */
    public void setXX_CodJefCat_ID (int XX_CodJefCat_ID)
    {
        if (XX_CodJefCat_ID <= 0) set_Value ("XX_CodJefCat_ID", null);
        else
        set_Value ("XX_CodJefCat_ID", Integer.valueOf(XX_CodJefCat_ID));
        
    }
    
    /** Get Boss Category Code.
    @return Codigo de jefe de categoría */
    public int getXX_CodJefCat_ID() 
    {
        return get_ValueAsInt("XX_CodJefCat_ID");
        
    }
    
    /** Set Creation user code.
    @param XX_CodUsuCre Código de usuario creación */
    public void setXX_CodUsuCre (String XX_CodUsuCre)
    {
        set_Value ("XX_CodUsuCre", XX_CodUsuCre);
        
    }
    
    /** Get Creation user code.
    @return Código de usuario creación */
    public String getXX_CodUsuCre() 
    {
        return (String)get_Value("XX_CodUsuCre");
        
    }
    
    /** Set Category description.
    @param XX_DesCat Descripción de categoría */
    public void setXX_DesCat (String XX_DesCat)
    {
        set_Value ("XX_DesCat", XX_DesCat);
        
    }
    
    /** Get Category description.
    @return Descripción de categoría */
    public String getXX_DesCat() 
    {
        return (String)get_Value("XX_DesCat");
        
    }
    
    /** Set Category status.
    @param XX_EstCat Estatus de categoría */
    public void setXX_EstCat (int XX_EstCat)
    {
        set_Value ("XX_EstCat", Integer.valueOf(XX_EstCat));
        
    }
    
    /** Get Category status.
    @return Estatus de categoría */
    public int getXX_EstCat() 
    {
        return get_ValueAsInt("XX_EstCat");
        
    }
    
    /** Set Closing date.
    @param XX_FecCierr Fecha de cierre */
    public void setXX_FecCierr (Timestamp XX_FecCierr)
    {
        set_ValueNoCheck ("XX_FecCierr", XX_FecCierr);
        
    }
    
    /** Get Closing date.
    @return Fecha de cierre */
    public Timestamp getXX_FecCierr() 
    {
        return (Timestamp)get_Value("XX_FecCierr");
        
    }
    
    /** Set Creation date.
    @param XX_FecCreac Fecha de creación de departamento */
    public void setXX_FecCreac (Timestamp XX_FecCreac)
    {
        set_ValueNoCheck ("XX_FecCreac", XX_FecCreac);
        
    }
    
    /** Get Creation date.
    @return Fecha de creación de departamento */
    public Timestamp getXX_FecCreac() 
    {
        return (Timestamp)get_Value("XX_FecCreac");
        
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
    
    
}
