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
/** Generated Model for I_XX_Prod02i
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_Prod02i extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_Prod02i_ID id
    @param trx transaction
    */
    public X_I_XX_Prod02i (Ctx ctx, int I_XX_Prod02i_ID, Trx trx)
    {
        super (ctx, I_XX_Prod02i_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_Prod02i_ID == 0)
        {
            setI_XX_PROD02I_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_Prod02i (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567320759789L;
    /** Last Updated Timestamp 2010-09-22 18:14:03.0 */
    public static final long updatedMS = 1285195443000L;
    /** AD_Table_ID=1000214 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_Prod02i");
        
    }
    ;
    
    /** TableName=I_XX_Prod02i */
    public static final String Table_Name="I_XX_Prod02i";
    
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
    
    /** Set I_XX_PROD02I_ID.
    @param I_XX_PROD02I_ID I_XX_PROD02I_ID */
    public void setI_XX_PROD02I_ID (int I_XX_PROD02I_ID)
    {
        if (I_XX_PROD02I_ID < 1) throw new IllegalArgumentException ("I_XX_PROD02I_ID is mandatory.");
        set_ValueNoCheck ("I_XX_PROD02I_ID", Integer.valueOf(I_XX_PROD02I_ID));
        
    }
    
    /** Get I_XX_PROD02I_ID.
    @return I_XX_PROD02I_ID */
    public int getI_XX_PROD02I_ID() 
    {
        return get_ValueAsInt("I_XX_PROD02I_ID");
        
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
    
    /** Set Is field Error.
    @param XX_ISFIELDERROR Is field Error */
    public void setXX_ISFIELDERROR (String XX_ISFIELDERROR)
    {
        set_Value ("XX_ISFIELDERROR", XX_ISFIELDERROR);
        
    }
    
    /** Get Is field Error.
    @return Is field Error */
    public String getXX_ISFIELDERROR() 
    {
        return (String)get_Value("XX_ISFIELDERROR");
        
    }
    
    /** Set Charactertistic name.
    @param XX_NomCar Nombre de característica */
    public void setXX_NomCar (String XX_NomCar)
    {
        set_Value ("XX_NomCar", XX_NomCar);
        
    }
    
    /** Get Charactertistic name.
    @return Nombre de característica */
    public String getXX_NomCar() 
    {
        return (String)get_Value("XX_NomCar");
        
    }
    
    /** Set Characteristic Name 1.
    @param XX_NomCar1 Characteristic Name 1 */
    public void setXX_NomCar1 (String XX_NomCar1)
    {
        set_Value ("XX_NomCar1", XX_NomCar1);
        
    }
    
    /** Get Characteristic Name 1.
    @return Characteristic Name 1 */
    public String getXX_NomCar1() 
    {
        return (String)get_Value("XX_NomCar1");
        
    }
    
    /** Set Characteristic Name 2.
    @param XX_NomCar2 Characteristic Name 2 */
    public void setXX_NomCar2 (String XX_NomCar2)
    {
        set_Value ("XX_NomCar2", XX_NomCar2);
        
    }
    
    /** Get Characteristic Name 2.
    @return Characteristic Name 2 */
    public String getXX_NomCar2() 
    {
        return (String)get_Value("XX_NomCar2");
        
    }
    
    /** Set Characteristic type.
    @param XX_TipCar Tipo de característica */
    public void setXX_TipCar (int XX_TipCar)
    {
        set_Value ("XX_TipCar", Integer.valueOf(XX_TipCar));
        
    }
    
    /** Get Characteristic type.
    @return Tipo de característica */
    public int getXX_TipCar() 
    {
        return get_ValueAsInt("XX_TipCar");
        
    }
    
    /** Set XX_TIPCAR1.
    @param XX_TIPCAR1 Characteristic Type 1 - Tipo Caracteristica 1 */
    public void setXX_TIPCAR1 (int XX_TIPCAR1)
    {
        set_Value ("XX_TIPCAR1", Integer.valueOf(XX_TIPCAR1));
        
    }
    
    /** Get XX_TIPCAR1.
    @return Characteristic Type 1 - Tipo Caracteristica 1 */
    public int getXX_TIPCAR1() 
    {
        return get_ValueAsInt("XX_TIPCAR1");
        
    }
    
    /** Set XX_TIPCAR2.
    @param XX_TIPCAR2 Characteristic Type 2 - Tipo Caracteristica 2 */
    public void setXX_TIPCAR2 (int XX_TIPCAR2)
    {
        set_Value ("XX_TIPCAR2", Integer.valueOf(XX_TIPCAR2));
        
    }
    
    /** Get XX_TIPCAR2.
    @return Characteristic Type 2 - Tipo Caracteristica 2 */
    public int getXX_TIPCAR2() 
    {
        return get_ValueAsInt("XX_TIPCAR2");
        
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
    public void setXX_VMR_DynamicCharact_ID (int XX_VMR_DynamicCharact_ID)
    {
        if (XX_VMR_DynamicCharact_ID <= 0) set_Value ("XX_VMR_DynamicCharact_ID", null);
        else
        set_Value ("XX_VMR_DynamicCharact_ID", Integer.valueOf(XX_VMR_DynamicCharact_ID));
        
    }
    
    /** Get XX_VMR_DynamicCharact_ID.
    @return Dynamic Charactistic ID */
    public int getXX_VMR_DynamicCharact_ID() 
    {
        return get_ValueAsInt("XX_VMR_DynamicCharact_ID");
        
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
