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
/** Generated Model for E_XX_VMR_ORCD24
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_ORCD24 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_ORCD24_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_ORCD24 (Ctx ctx, int E_XX_VMR_ORCD24_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_ORCD24_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_ORCD24_ID == 0)
        {
            setE_XX_VMR_ORCD24_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_ORCD24 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198617789L;
    /** Last Updated Timestamp 2010-11-29 18:58:21.0 */
    public static final long updatedMS = 1291073301000L;
    /** AD_Table_ID=1000347 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_ORCD24");
        
    }
    ;
    
    /** TableName=E_XX_VMR_ORCD24 */
    public static final String Table_Name="E_XX_VMR_ORCD24";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set E_XX_VMR_ORCD24_ID.
    @param E_XX_VMR_ORCD24_ID E_XX_VMR_ORCD24_ID */
    public void setE_XX_VMR_ORCD24_ID (int E_XX_VMR_ORCD24_ID)
    {
        if (E_XX_VMR_ORCD24_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_ORCD24_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_ORCD24_ID", Integer.valueOf(E_XX_VMR_ORCD24_ID));
        
    }
    
    /** Get E_XX_VMR_ORCD24_ID.
    @return E_XX_VMR_ORCD24_ID */
    public int getE_XX_VMR_ORCD24_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_ORCD24_ID");
        
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
    
    /** Set PORDIS.
    @param PORDIS PERCENTAGE DISTRIBUTION */
    public void setPORDIS (int PORDIS)
    {
        set_Value ("PORDIS", Integer.valueOf(PORDIS));
        
    }
    
    /** Get PORDIS.
    @return PERCENTAGE DISTRIBUTION */
    public int getPORDIS() 
    {
        return get_ValueAsInt("PORDIS");
        
    }
    
    /** Set Elimination Status.
    @param STAELI ELIMINATION STATUS */
    public void setSTAELI (String STAELI)
    {
        set_Value ("STAELI", STAELI);
        
    }
    
    /** Get Elimination Status.
    @return ELIMINATION STATUS */
    public String getSTAELI() 
    {
        return (String)get_Value("STAELI");
        
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
    
    /** Set Creation User.
    @param USRCRE Creation User */
    public void setUSRCRE (String USRCRE)
    {
        set_Value ("USRCRE", USRCRE);
        
    }
    
    /** Get Creation User.
    @return Creation User */
    public String getUSRCRE() 
    {
        return (String)get_Value("USRCRE");
        
    }
    
    /** Set Elimantion User.
    @param USRELI ELIMINATION OF USER */
    public void setUSRELI (String USRELI)
    {
        set_Value ("USRELI", USRELI);
        
    }
    
    /** Get Elimantion User.
    @return ELIMINATION OF USER */
    public String getUSRELI() 
    {
        return (String)get_Value("USRELI");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
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
