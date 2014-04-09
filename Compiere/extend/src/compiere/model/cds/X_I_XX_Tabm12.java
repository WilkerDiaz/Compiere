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
/** Generated Model for I_XX_Tabm12
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_Tabm12 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_Tabm12_ID id
    @param trx transaction
    */
    public X_I_XX_Tabm12 (Ctx ctx, int I_XX_Tabm12_ID, Trx trx)
    {
        super (ctx, I_XX_Tabm12_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_Tabm12_ID == 0)
        {
            setI_XX_TABM12_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_Tabm12 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27545019937789L;
    /** Last Updated Timestamp 2010-01-07 15:33:41.0 */
    public static final long updatedMS = 1262894621000L;
    /** AD_Table_ID=1000063 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_Tabm12");
        
    }
    ;
    
    /** TableName=I_XX_Tabm12 */
    public static final String Table_Name="I_XX_Tabm12";
    
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
    
    /** Set I_XX_TABM12_ID.
    @param I_XX_TABM12_ID I_XX_TABM12_ID */
    public void setI_XX_TABM12_ID (int I_XX_TABM12_ID)
    {
        if (I_XX_TABM12_ID < 1) throw new IllegalArgumentException ("I_XX_TABM12_ID is mandatory.");
        set_ValueNoCheck ("I_XX_TABM12_ID", Integer.valueOf(I_XX_TABM12_ID));
        
    }
    
    /** Get I_XX_TABM12_ID.
    @return I_XX_TABM12_ID */
    public int getI_XX_TABM12_ID() 
    {
        return get_ValueAsInt("I_XX_TABM12_ID");
        
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
    
    /** Set Department Code	.
    @param XX_CodDep Codigo del Departameto */
    public void setXX_CodDep (int XX_CodDep)
    {
        set_Value ("XX_CodDep", Integer.valueOf(XX_CodDep));
        
    }
    
    /** Get Department Code	.
    @return Codigo del Departameto */
    public int getXX_CodDep() 
    {
        return get_ValueAsInt("XX_CodDep");
        
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
    
    /** Set Department Description.
    @param XX_DesDep Descripcción de departamento */
    public void setXX_DesDep (String XX_DesDep)
    {
        set_Value ("XX_DesDep", XX_DesDep);
        
    }
    
    /** Get Department Description.
    @return Descripcción de departamento */
    public String getXX_DesDep() 
    {
        return (String)get_Value("XX_DesDep");
        
    }
    
    /** Set Department status.
    @param XX_EstDep Estatus de departamento */
    public void setXX_EstDep (int XX_EstDep)
    {
        set_Value ("XX_EstDep", Integer.valueOf(XX_EstDep));
        
    }
    
    /** Get Department status.
    @return Estatus de departamento */
    public int getXX_EstDep() 
    {
        return get_ValueAsInt("XX_EstDep");
        
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
    
    /** Set Assistant Value.
    @param XX_IdAsis identificador del asistente */
    public void setXX_IdAsis (String XX_IdAsis)
    {
        set_Value ("XX_IdAsis", XX_IdAsis);
        
    }
    
    /** Get Assistant Value.
    @return identificador del asistente */
    public String getXX_IdAsis() 
    {
        return (String)get_Value("XX_IdAsis");
        
    }
    
    /** Set Assistant ID.
    @param XX_IdAsis_ID Assistant ID */
    public void setXX_IdAsis_ID (int XX_IdAsis_ID)
    {
        if (XX_IdAsis_ID <= 0) set_Value ("XX_IdAsis_ID", null);
        else
        set_Value ("XX_IdAsis_ID", Integer.valueOf(XX_IdAsis_ID));
        
    }
    
    /** Get Assistant ID.
    @return Assistant ID */
    public int getXX_IdAsis_ID() 
    {
        return get_ValueAsInt("XX_IdAsis_ID");
        
    }
    
    /** Set Buyer Value.
    @param XX_IdCompra Value de comprador */
    public void setXX_IdCompra (String XX_IdCompra)
    {
        set_Value ("XX_IdCompra", XX_IdCompra);
        
    }
    
    /** Get Buyer Value.
    @return Value de comprador */
    public String getXX_IdCompra() 
    {
        return (String)get_Value("XX_IdCompra");
        
    }
    
    /** Set Buyer ID.
    @param XX_IdCompra_ID ID de comprador */
    public void setXX_IdCompra_ID (int XX_IdCompra_ID)
    {
        if (XX_IdCompra_ID <= 0) set_Value ("XX_IdCompra_ID", null);
        else
        set_Value ("XX_IdCompra_ID", Integer.valueOf(XX_IdCompra_ID));
        
    }
    
    /** Get Buyer ID.
    @return ID de comprador */
    public int getXX_IdCompra_ID() 
    {
        return get_ValueAsInt("XX_IdCompra_ID");
        
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
    
    
}
