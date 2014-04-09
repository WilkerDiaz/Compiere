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
/** Generated Model for E_XX_VMR_NRAM02
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_NRAM02 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_NRAM02_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_NRAM02 (Ctx ctx, int E_XX_VMR_NRAM02_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_NRAM02_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_NRAM02_ID == 0)
        {
            setE_XX_VMR_NRAM02_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_NRAM02 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198874789L;
    /** Last Updated Timestamp 2010-11-29 19:02:38.0 */
    public static final long updatedMS = 1291073558000L;
    /** AD_Table_ID=1000368 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_NRAM02");
        
    }
    ;
    
    /** TableName=E_XX_VMR_NRAM02 */
    public static final String Table_Name="E_XX_VMR_NRAM02";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Receipt Package.
    @param BULREC Receipt Package */
    public void setBULREC (int BULREC)
    {
        set_Value ("BULREC", Integer.valueOf(BULREC));
        
    }
    
    /** Get Receipt Package.
    @return Receipt Package */
    public int getBULREC() 
    {
        return get_ValueAsInt("BULREC");
        
    }
    
    /** Set Package.
    @param BULTO Package at CB98 */
    public void setBULTO (String BULTO)
    {
        set_Value ("BULTO", BULTO);
        
    }
    
    /** Get Package.
    @return Package at CB98 */
    public String getBULTO() 
    {
        return (String)get_Value("BULTO");
        
    }
    
    /** Set CODMON.
    @param CODMON CODMON */
    public void setCODMON (String CODMON)
    {
        set_Value ("CODMON", CODMON);
        
    }
    
    /** Get CODMON.
    @return CODMON */
    public String getCODMON() 
    {
        return (String)get_Value("CODMON");
        
    }
    
    /** Set COEMPE.
    @param COEMPE COEMPE */
    public void setCOEMPE (String COEMPE)
    {
        set_Value ("COEMPE", COEMPE);
        
    }
    
    /** Get COEMPE.
    @return COEMPE */
    public String getCOEMPE() 
    {
        return (String)get_Value("COEMPE");
        
    }
    
    /** Set Creation  Day (Reception).
    @param DIAREG Creation  Day (Reception) at CB98 */
    public void setDIAREG (int DIAREG)
    {
        set_Value ("DIAREG", Integer.valueOf(DIAREG));
        
    }
    
    /** Get Creation  Day (Reception).
    @return Creation  Day (Reception) at CB98 */
    public int getDIAREG() 
    {
        return get_ValueAsInt("DIAREG");
        
    }
    
    /** Set E_XX_VMR_NRAM02_ID.
    @param E_XX_VMR_NRAM02_ID E_XX_VMR_NRAM02_ID */
    public void setE_XX_VMR_NRAM02_ID (int E_XX_VMR_NRAM02_ID)
    {
        if (E_XX_VMR_NRAM02_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_NRAM02_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_NRAM02_ID", Integer.valueOf(E_XX_VMR_NRAM02_ID));
        
    }
    
    /** Get E_XX_VMR_NRAM02_ID.
    @return E_XX_VMR_NRAM02_ID */
    public int getE_XX_VMR_NRAM02_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_NRAM02_ID");
        
    }
    
    /** Set Convertion Rate.
    @param FACCAM FIELD FACTOR */
    public void setFACCAM (java.math.BigDecimal FACCAM)
    {
        set_Value ("FACCAM", FACCAM);
        
    }
    
    /** Get Convertion Rate.
    @return FIELD FACTOR */
    public java.math.BigDecimal getFACCAM() 
    {
        return get_ValueAsBigDecimal("FACCAM");
        
    }
    
    /** Set GUIAEM.
    @param GUIAEM SHIPPING GUIDE */
    public void setGUIAEM (String GUIAEM)
    {
        set_Value ("GUIAEM", GUIAEM);
        
    }
    
    /** Get GUIAEM.
    @return SHIPPING GUIDE */
    public String getGUIAEM() 
    {
        return (String)get_Value("GUIAEM");
        
    }
    
    /** Set Creation Month (Reception).
    @param MESREG Creation Month (Reception) at CB98 */
    public void setMESREG (int MESREG)
    {
        set_Value ("MESREG", Integer.valueOf(MESREG));
        
    }
    
    /** Get Creation Month (Reception).
    @return Creation Month (Reception) at CB98 */
    public int getMESREG() 
    {
        return get_ValueAsInt("MESREG");
        
    }
    
    /** Set Invoice Total Amount.
    @param MONFAC Invoice Total Amount at CB98 */
    public void setMONFAC (java.math.BigDecimal MONFAC)
    {
        set_Value ("MONFAC", MONFAC);
        
    }
    
    /** Get Invoice Total Amount.
    @return Invoice Total Amount at CB98 */
    public java.math.BigDecimal getMONFAC() 
    {
        return get_ValueAsBigDecimal("MONFAC");
        
    }
    
    /** Set Amount Tax.
    @param MONIMP Amount Tax */
    public void setMONIMP (java.math.BigDecimal MONIMP)
    {
        set_Value ("MONIMP", MONIMP);
        
    }
    
    /** Get Amount Tax.
    @return Amount Tax */
    public java.math.BigDecimal getMONIMP() 
    {
        return get_ValueAsBigDecimal("MONIMP");
        
    }
    
    /** Set Invoice Document Number .
    @param NUMFAC Invoice Document Number at CB98 */
    public void setNUMFAC (String NUMFAC)
    {
        set_Value ("NUMFAC", NUMFAC);
        
    }
    
    /** Get Invoice Document Number .
    @return Invoice Document Number at CB98 */
    public String getNUMFAC() 
    {
        return (String)get_Value("NUMFAC");
        
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
    
    /** Set Receipt Number.
    @param NUMREC receipt number */
    public void setNUMREC (String NUMREC)
    {
        set_Value ("NUMREC", NUMREC);
        
    }
    
    /** Get Receipt Number.
    @return receipt number */
    public String getNUMREC() 
    {
        return (String)get_Value("NUMREC");
        
    }
    
    /** Set Anulation Status.
    @param STAANU Anulation Status */
    public void setSTAANU (String STAANU)
    {
        set_Value ("STAANU", STAANU);
        
    }
    
    /** Get Anulation Status.
    @return Anulation Status */
    public String getSTAANU() 
    {
        return (String)get_Value("STAANU");
        
    }
    
    /** Set Invoice Wharehouse.
    @param TIEFAC Invoice Wharehouse */
    public void setTIEFAC (String TIEFAC)
    {
        set_Value ("TIEFAC", TIEFAC);
        
    }
    
    /** Get Invoice Wharehouse.
    @return Invoice Wharehouse */
    public String getTIEFAC() 
    {
        return (String)get_Value("TIEFAC");
        
    }
    
    /** Set TIEMPE.
    @param TIEMPE TIEMPE */
    public void setTIEMPE (String TIEMPE)
    {
        set_Value ("TIEMPE", TIEMPE);
        
    }
    
    /** Get TIEMPE.
    @return TIEMPE */
    public String getTIEMPE() 
    {
        return (String)get_Value("TIEMPE");
        
    }
    
    /** Set Volume Unity.
    @param UNIVOL Volume Unity */
    public void setUNIVOL (String UNIVOL)
    {
        set_Value ("UNIVOL", UNIVOL);
        
    }
    
    /** Get Volume Unity.
    @return Volume Unity */
    public String getUNIVOL() 
    {
        return (String)get_Value("UNIVOL");
        
    }
    
    /** Set Volume.
    @param Volume Volume of a product */
    public void setVolume (java.math.BigDecimal Volume)
    {
        set_Value ("Volume", Volume);
        
    }
    
    /** Get Volume.
    @return Volume of a product */
    public java.math.BigDecimal getVolume() 
    {
        return get_ValueAsBigDecimal("Volume");
        
    }
    
    /** Set Creation Year (Reception).
    @param XX_ANOREG Creation Year (Reception) at  CB98 */
    public void setXX_ANOREG (int XX_ANOREG)
    {
        set_Value ("XX_ANOREG", Integer.valueOf(XX_ANOREG));
        
    }
    
    /** Get Creation Year (Reception).
    @return Creation Year (Reception) at  CB98 */
    public int getXX_ANOREG() 
    {
        return get_ValueAsInt("XX_ANOREG");
        
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
