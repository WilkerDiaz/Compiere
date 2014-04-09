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
/** Generated Model for I_XX_Prom02
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_Prom02 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_Prom02_ID id
    @param trx transaction
    */
    public X_I_XX_Prom02 (Ctx ctx, int I_XX_Prom02_ID, Trx trx)
    {
        super (ctx, I_XX_Prom02_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_Prom02_ID == 0)
        {
            setI_XX_PROM02_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_Prom02 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567125592789L;
    /** Last Updated Timestamp 2010-09-20 12:01:16.0 */
    public static final long updatedMS = 1285000276000L;
    /** AD_Table_ID=1000067 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_Prom02");
        
    }
    ;
    
    /** TableName=I_XX_Prom02 */
    public static final String Table_Name="I_XX_Prom02";
    
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
    
    /** Set I_XX_PROM02_ID.
    @param I_XX_PROM02_ID I_XX_PROM02_ID */
    public void setI_XX_PROM02_ID (int I_XX_PROM02_ID)
    {
        if (I_XX_PROM02_ID < 1) throw new IllegalArgumentException ("I_XX_PROM02_ID is mandatory.");
        set_ValueNoCheck ("I_XX_PROM02_ID", Integer.valueOf(I_XX_PROM02_ID));
        
    }
    
    /** Get I_XX_PROM02_ID.
    @return I_XX_PROM02_ID */
    public int getI_XX_PROM02_ID() 
    {
        return get_ValueAsInt("I_XX_PROM02_ID");
        
    }
    
    /** Set Attribute.
    @param M_Attribute_ID Product Attribute */
    public void setM_Attribute_ID (java.math.BigDecimal M_Attribute_ID)
    {
        set_Value ("M_Attribute_ID", M_Attribute_ID);
        
    }
    
    /** Get Attribute.
    @return Product Attribute */
    public java.math.BigDecimal getM_Attribute_ID() 
    {
        return get_ValueAsBigDecimal("M_Attribute_ID");
        
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
    
    /** Set Status date.
    @param XX_FecEst Estatus de fecha */
    public void setXX_FecEst (Timestamp XX_FecEst)
    {
        set_ValueNoCheck ("XX_FecEst", XX_FecEst);
        
    }
    
    /** Get Status date.
    @return Estatus de fecha */
    public Timestamp getXX_FecEst() 
    {
        return (Timestamp)get_Value("XX_FecEst");
        
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
    
    /** Set Characteristic type.
    @param XX_TipCar Tipo de característica */
    public void setXX_TipCar (String XX_TipCar)
    {
        set_Value ("XX_TipCar", XX_TipCar);
        
    }
    
    /** Get Characteristic type.
    @return Tipo de característica */
    public String getXX_TipCar() 
    {
        return (String)get_Value("XX_TipCar");
        
    }
    
    
}
