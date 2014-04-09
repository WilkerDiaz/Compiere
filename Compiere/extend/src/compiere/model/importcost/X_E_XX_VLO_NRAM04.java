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
package compiere.model.importcost;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for E_XX_VLO_NRAM04
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VLO_NRAM04 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VLO_NRAM04_ID id
    @param trx transaction
    */
    public X_E_XX_VLO_NRAM04 (Ctx ctx, int E_XX_VLO_NRAM04_ID, Trx trx)
    {
        super (ctx, E_XX_VLO_NRAM04_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VLO_NRAM04_ID == 0)
        {
            setE_XX_VLO_NRAM04_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VLO_NRAM04 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27592888180789L;
    /** Last Updated Timestamp 2011-07-15 16:17:44.0 */
    public static final long updatedMS = 1310762864000L;
    /** AD_Table_ID=1000450 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VLO_NRAM04");
        
    }
    ;
    
    /** TableName=E_XX_VLO_NRAM04 */
    public static final String Table_Name="E_XX_VLO_NRAM04";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set AÑOPRO.
    @param AÑOPRO AÑOPRO */
    public void setAÑOPRO (java.math.BigDecimal AÑOPRO)
    {
        set_Value ("AÑOPRO", AÑOPRO);
        
    }
    
    /** Get AÑOPRO.
    @return AÑOPRO */
    public java.math.BigDecimal getAÑOPRO() 
    {
        return get_ValueAsBigDecimal("AÑOPRO");
        
    }
    
    /** Set Department Code.
    @param CODDEP Department Code */
    public void setCODDEP (String CODDEP)
    {
        set_Value ("CODDEP", CODDEP);
        
    }
    
    /** Get Department Code.
    @return Department Code */
    public String getCODDEP() 
    {
        return (String)get_Value("CODDEP");
        
    }
    
    /** Set COEMPE.
    @param COEMPE COEMPE */
    public void setCOEMPE (String COEMPE)
    {
        set_Value ("COEMPE", COEMPE);
        
    }
    
    /** Set Amount Cost.
    @param XX_COSANT Monto de costo */
    public void setXX_COSANT (java.math.BigDecimal XX_COSANT)
    {
        set_Value ("XX_COSANT", XX_COSANT);
        
    }
    
    /** Get COEMPE.
    @return COEMPE */
    public String getCOEMPE() 
    {
        return (String)get_Value("COEMPE");
        
    }
    
    /** Set CONPAG.
    @param CONPAG CONPAG */
    public void setCONPAG (String CONPAG)
    {
        set_Value ("CONPAG", CONPAG);
        
    }
    
    /** Get CONPAG.
    @return CONPAG */
    public String getCONPAG() 
    {
        return (String)get_Value("CONPAG");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set E_XX_VLO_NRAM04_ID.
    @param E_XX_VLO_NRAM04_ID E_XX_VLO_NRAM04_ID */
    public void setE_XX_VLO_NRAM04_ID (int E_XX_VLO_NRAM04_ID)
    {
        if (E_XX_VLO_NRAM04_ID < 1) throw new IllegalArgumentException ("E_XX_VLO_NRAM04_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VLO_NRAM04_ID", Integer.valueOf(E_XX_VLO_NRAM04_ID));
        
    }
    
    /** Get E_XX_VLO_NRAM04_ID.
    @return E_XX_VLO_NRAM04_ID */
    public int getE_XX_VLO_NRAM04_ID() 
    {
        return get_ValueAsInt("E_XX_VLO_NRAM04_ID");
        
    }
    
    /** Set MESPRO.
    @param MESPRO MESPRO */
    public void setMESPRO (java.math.BigDecimal MESPRO)
    {
        set_Value ("MESPRO", MESPRO);
        
    }
    
    /** Get MESPRO.
    @return MESPRO */
    public java.math.BigDecimal getMESPRO() 
    {
        return get_ValueAsBigDecimal("MESPRO");
        
    }
    
    /** Set Order Number.
    @param NUMORD Order Number */
    public void setNUMORD (String NUMORD)
    {
        set_Value ("NUMORD", NUMORD);
        
    }
    
    /** Get Order Number.
    @return Order Number */
    public String getNUMORD() 
    {
        return (String)get_Value("NUMORD");
        
    }
    
    /** Set PAIS.
    @param PAIS PAIS */
    public void setPAIS (String PAIS)
    {
        set_Value ("PAIS", PAIS);
        
    }
    
    /** Get PAIS.
    @return PAIS */
    public String getPAIS() 
    {
        return (String)get_Value("PAIS");
        
    }
    
    /** Set Tienda.
    @param Tienda Wharehouse */
    public void setTienda (String Tienda)
    {
        set_Value ("Tienda", Tienda);
        
    }
    
    /** Get Tienda.
    @return Wharehouse */
    public String getTienda() 
    {
        return (String)get_Value("Tienda");
        
    }
    
    /** Set Amount Cost.
    @param XX_COSANT Monto de costo */
    public void setXX_COSANT (int XX_COSANT)
    {
        set_Value ("XX_COSANT", Integer.valueOf(XX_COSANT));
        
    }
    
    /** Get Amount Cost.
    @return Monto de costo */
    public int getXX_COSANT() 
    {
        return get_ValueAsInt("XX_COSANT");
        
    }
    
    /** Set XX_DATATYPE.
    @param XX_DATATYPE XX_DATATYPE */
    public void setXX_DATATYPE (String XX_DATATYPE)
    {
        set_Value ("XX_DATATYPE", XX_DATATYPE);
        
    }
    
    /** Get XX_DATATYPE.
    @return XX_DATATYPE */
    public String getXX_DATATYPE() 
    {
        return (String)get_Value("XX_DATATYPE");
        
    }
    
    /** Set Sincronization Status .
    @param XX_Status_Sinc Sincronization Status  */
    public void setXX_Status_Sinc (boolean XX_Status_Sinc)
    {
        set_Value ("XX_Status_Sinc", Boolean.valueOf(XX_Status_Sinc));
        
    }
    
    /** Get Sincronization Status .
    @return Sincronization Status  */
    public boolean isXX_Status_Sinc() 
    {
        return get_ValueAsBoolean("XX_Status_Sinc");
        
    }
    
    
}
