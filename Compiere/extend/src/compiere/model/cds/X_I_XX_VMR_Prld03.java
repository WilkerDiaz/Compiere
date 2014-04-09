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
/** Generated Model for I_XX_VMR_Prld03
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_Prld03 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_Prld03_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_Prld03 (Ctx ctx, int I_XX_VMR_Prld03_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_Prld03_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_Prld03_ID == 0)
        {
            setI_XX_VMR_PRLD03_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_Prld03 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27564980992789L;
    /** Last Updated Timestamp 2010-08-26 16:17:56.0 */
    public static final long updatedMS = 1282855676000L;
    /** AD_Table_ID=1000101 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_Prld03");
        
    }
    ;
    
    /** TableName=I_XX_VMR_Prld03 */
    public static final String Table_Name="I_XX_VMR_Prld03";
    
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
    
    /** Set I_XX_VMR_PRLD03_ID.
    @param I_XX_VMR_PRLD03_ID I_XX_VMR_PRLD03_ID */
    public void setI_XX_VMR_PRLD03_ID (int I_XX_VMR_PRLD03_ID)
    {
        if (I_XX_VMR_PRLD03_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_PRLD03_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_PRLD03_ID", Integer.valueOf(I_XX_VMR_PRLD03_ID));
        
    }
    
    /** Get I_XX_VMR_PRLD03_ID.
    @return I_XX_VMR_PRLD03_ID */
    public int getI_XX_VMR_PRLD03_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_PRLD03_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Set Year Budget .
    @param XX_AÑOPRE Año De Presupuesto */
    public void setXX_AÑOPRE (int XX_AÑOPRE)
    {
        set_Value ("XX_AÑOPRE", Integer.valueOf(XX_AÑOPRE));
        
    }
    
    /** Get Year Budget .
    @return Año De Presupuesto */
    public int getXX_AÑOPRE() 
    {
        return get_ValueAsInt("XX_AÑOPRE");
        
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
    
    /** Set Budget Day .
    @param XX_DIAPRE Dia De Presupuesto	 */
    public void setXX_DIAPRE (int XX_DIAPRE)
    {
        set_Value ("XX_DIAPRE", Integer.valueOf(XX_DIAPRE));
        
    }
    
    /** Get Budget Day .
    @return Dia De Presupuesto	 */
    public int getXX_DIAPRE() 
    {
        return get_ValueAsInt("XX_DIAPRE");
        
    }
    
    /** Set Month Budget .
    @param XX_MESPRE Mes De Presupuesto	 */
    public void setXX_MESPRE (int XX_MESPRE)
    {
        set_Value ("XX_MESPRE", Integer.valueOf(XX_MESPRE));
        
    }
    
    /** Get Month Budget .
    @return Mes De Presupuesto	 */
    public int getXX_MESPRE() 
    {
        return get_ValueAsInt("XX_MESPRE");
        
    }
    
    /** Set Moneje.
    @param XX_MONEJE Moneje */
    public void setXX_MONEJE (java.math.BigDecimal XX_MONEJE)
    {
        set_Value ("XX_MONEJE", XX_MONEJE);
        
    }
    
    /** Get Moneje.
    @return Moneje */
    public java.math.BigDecimal getXX_MONEJE() 
    {
        return get_ValueAsBigDecimal("XX_MONEJE");
        
    }
    
    /** Set Monest.
    @param XX_MONEST Monest */
    public void setXX_MONEST (java.math.BigDecimal XX_MONEST)
    {
        set_Value ("XX_MONEST", XX_MONEST);
        
    }
    
    /** Get Monest.
    @return Monest */
    public java.math.BigDecimal getXX_MONEST() 
    {
        return get_ValueAsBigDecimal("XX_MONEST");
        
    }
    
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (String XX_Status)
    {
        set_Value ("XX_Status", XX_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_Status() 
    {
        return (String)get_Value("XX_Status");
        
    }
    
    /** Set Store.
    @param XX_TIENDA Tienda */
    public void setXX_TIENDA (String XX_TIENDA)
    {
        set_Value ("XX_TIENDA", XX_TIENDA);
        
    }
    
    /** Get Store.
    @return Tienda */
    public String getXX_TIENDA() 
    {
        return (String)get_Value("XX_TIENDA");
        
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
    
    /** Set XX_VMR_PRLD03_ID.
    @param XX_VMR_PRLD03_ID XX_VMR_PRLD03_ID */
    public void setXX_VMR_PRLD03_ID (int XX_VMR_PRLD03_ID)
    {
        if (XX_VMR_PRLD03_ID <= 0) set_Value ("XX_VMR_PRLD03_ID", null);
        else
        set_Value ("XX_VMR_PRLD03_ID", Integer.valueOf(XX_VMR_PRLD03_ID));
        
    }
    
    /** Get XX_VMR_PRLD03_ID.
    @return XX_VMR_PRLD03_ID */
    public int getXX_VMR_PRLD03_ID() 
    {
        return get_ValueAsInt("XX_VMR_PRLD03_ID");
        
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
