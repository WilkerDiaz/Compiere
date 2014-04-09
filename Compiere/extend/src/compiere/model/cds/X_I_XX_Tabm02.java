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
/** Generated Model for I_XX_Tabm02
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_Tabm02 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_Tabm02_ID id
    @param trx transaction
    */
    public X_I_XX_Tabm02 (Ctx ctx, int I_XX_Tabm02_ID, Trx trx)
    {
        super (ctx, I_XX_Tabm02_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_Tabm02_ID == 0)
        {
            setI_XX_TABM02_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_Tabm02 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567112216789L;
    /** Last Updated Timestamp 2010-09-20 08:18:20.0 */
    public static final long updatedMS = 1284986900000L;
    /** AD_Table_ID=1000066 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_Tabm02");
        
    }
    ;
    
    /** TableName=I_XX_Tabm02 */
    public static final String Table_Name="I_XX_Tabm02";
    
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
    
    /** Set I_XX_TABM02_ID.
    @param I_XX_TABM02_ID I_XX_TABM02_ID */
    public void setI_XX_TABM02_ID (int I_XX_TABM02_ID)
    {
        if (I_XX_TABM02_ID < 1) throw new IllegalArgumentException ("I_XX_TABM02_ID is mandatory.");
        set_ValueNoCheck ("I_XX_TABM02_ID", Integer.valueOf(I_XX_TABM02_ID));
        
    }
    
    /** Get I_XX_TABM02_ID.
    @return I_XX_TABM02_ID */
    public int getI_XX_TABM02_ID() 
    {
        return get_ValueAsInt("I_XX_TABM02_ID");
        
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
    
    /** Set Characteristic.
    @param XX_Caract Característica */
    public void setXX_Caract (int XX_Caract)
    {
        set_Value ("XX_Caract", Integer.valueOf(XX_Caract));
        
    }
    
    /** Get Characteristic.
    @return Característica */
    public int getXX_Caract() 
    {
        return get_ValueAsInt("XX_Caract");
        
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
    
    /** Set Code Line.
    @param XX_CodLin Codigo de Linea */
    public void setXX_CodLin (int XX_CodLin)
    {
        set_Value ("XX_CodLin", Integer.valueOf(XX_CodLin));
        
    }
    
    /** Get Code Line.
    @return Codigo de Linea */
    public int getXX_CodLin() 
    {
        return get_ValueAsInt("XX_CodLin");
        
    }
    
    /** Set Category Description.
    @param XX_DesCar Descripción de categoría */
    public void setXX_DesCar (String XX_DesCar)
    {
        set_Value ("XX_DesCar", XX_DesCar);
        
    }
    
    /** Get Category Description.
    @return Descripción de categoría */
    public String getXX_DesCar() 
    {
        return (String)get_Value("XX_DesCar");
        
    }
    
    /** Set Section.
    @param XX_Seccio Seccion */
    public void setXX_Seccio (int XX_Seccio)
    {
        set_Value ("XX_Seccio", Integer.valueOf(XX_Seccio));
        
    }
    
    /** Get Section.
    @return Seccion */
    public int getXX_Seccio() 
    {
        return get_ValueAsInt("XX_Seccio");
        
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
    
    /** Set Main Characteristic.
    @param XX_VMR_LongCharacteristic_ID Main Characteristic */
    public void setXX_VMR_LongCharacteristic_ID (int XX_VMR_LongCharacteristic_ID)
    {
        if (XX_VMR_LongCharacteristic_ID <= 0) set_Value ("XX_VMR_LongCharacteristic_ID", null);
        else
        set_Value ("XX_VMR_LongCharacteristic_ID", Integer.valueOf(XX_VMR_LongCharacteristic_ID));
        
    }
    
    /** Get Main Characteristic.
    @return Main Characteristic */
    public int getXX_VMR_LongCharacteristic_ID() 
    {
        return get_ValueAsInt("XX_VMR_LongCharacteristic_ID");
        
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
