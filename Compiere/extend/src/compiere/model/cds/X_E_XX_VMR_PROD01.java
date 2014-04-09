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
/** Generated Model for E_XX_VMR_PROD01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_PROD01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_PROD01_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_PROD01 (Ctx ctx, int E_XX_VMR_PROD01_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_PROD01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_PROD01_ID == 0)
        {
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_PROD01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198463789L;
    /** Last Updated Timestamp 2010-11-29 18:55:47.0 */
    public static final long updatedMS = 1291073147000L;
    /** AD_Table_ID=1000369 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_PROD01");
        
    }
    ;
    
    /** TableName=E_XX_VMR_PROD01 */
    public static final String Table_Name="E_XX_VMR_PROD01";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set ANOACT.
    @param ANOACT ANOACT */
    public void setANOACT (int ANOACT)
    {
        set_Value ("ANOACT", Integer.valueOf(ANOACT));
        
    }
    
    /** Get ANOACT.
    @return ANOACT */
    public int getANOACT() 
    {
        return get_ValueAsInt("ANOACT");
        
    }
    
    /** Set ANOCRE.
    @param ANOCRE ANOCRE */
    public void setANOCRE (int ANOCRE)
    {
        set_Value ("ANOCRE", Integer.valueOf(ANOCRE));
        
    }
    
    /** Get ANOCRE.
    @return ANOCRE */
    public int getANOCRE() 
    {
        return get_ValueAsInt("ANOCRE");
        
    }
    
    /** Set ANOREG.
    @param ANOREG ANOREG */
    public void setANOREG (int ANOREG)
    {
        set_Value ("ANOREG", Integer.valueOf(ANOREG));
        
    }
    
    /** Get ANOREG.
    @return ANOREG */
    public int getANOREG() 
    {
        return get_ValueAsInt("ANOREG");
        
    }
    
    /** Set Product Code.
    @param CODPRO PRODUCT CODE REFERENCE */
    public void setCODPRO (String CODPRO)
    {
        set_Value ("CODPRO", CODPRO);
        
    }
    
    /** Get Product Code.
    @return PRODUCT CODE REFERENCE */
    public String getCODPRO() 
    {
        return (String)get_Value("CODPRO");
        
    }
    
    /** Set CONPRE.
    @param CONPRE CONPRE */
    public void setCONPRE (java.math.BigDecimal CONPRE)
    {
        set_Value ("CONPRE", CONPRE);
        
    }
    
    /** Get CONPRE.
    @return CONPRE */
    public java.math.BigDecimal getCONPRE() 
    {
        return get_ValueAsBigDecimal("CONPRE");
        
    }
    
    /** Set DIAACT.
    @param DIAACT DIAACT */
    public void setDIAACT (int DIAACT)
    {
        set_Value ("DIAACT", Integer.valueOf(DIAACT));
        
    }
    
    /** Get DIAACT.
    @return DIAACT */
    public int getDIAACT() 
    {
        return get_ValueAsInt("DIAACT");
        
    }
    
    /** Set DIACRE.
    @param DIACRE DIACRE */
    public void setDIACRE (int DIACRE)
    {
        set_Value ("DIACRE", Integer.valueOf(DIACRE));
        
    }
    
    /** Get DIACRE.
    @return DIACRE */
    public int getDIACRE() 
    {
        return get_ValueAsInt("DIACRE");
        
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
    
    /** Set MESACT.
    @param MESACT MESACT */
    public void setMESACT (int MESACT)
    {
        set_Value ("MESACT", Integer.valueOf(MESACT));
        
    }
    
    /** Get MESACT.
    @return MESACT */
    public int getMESACT() 
    {
        return get_ValueAsInt("MESACT");
        
    }
    
    /** Set MESCRE.
    @param MESCRE MESCRE */
    public void setMESCRE (int MESCRE)
    {
        set_Value ("MESCRE", Integer.valueOf(MESCRE));
        
    }
    
    /** Get MESCRE.
    @return MESCRE */
    public int getMESCRE() 
    {
        return get_ValueAsInt("MESCRE");
        
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
    
    /** Set Receipt Number.
    @param NUMREC receipt number */
    public void setNUMREC (int NUMREC)
    {
        set_Value ("NUMREC", Integer.valueOf(NUMREC));
        
    }
    
    /** Get Receipt Number.
    @return receipt number */
    public int getNUMREC() 
    {
        return get_ValueAsInt("NUMREC");
        
    }
    
    /** Set ORIPRE.
    @param ORIPRE ORIPRE */
    public void setORIPRE (String ORIPRE)
    {
        set_Value ("ORIPRE", ORIPRE);
        
    }
    
    /** Get ORIPRE.
    @return ORIPRE */
    public String getORIPRE() 
    {
        return (String)get_Value("ORIPRE");
        
    }
    
    /** Set PRECOM.
    @param PRECOM PRECOM */
    public void setPRECOM (java.math.BigDecimal PRECOM)
    {
        set_Value ("PRECOM", PRECOM);
        
    }
    
    /** Get PRECOM.
    @return PRECOM */
    public java.math.BigDecimal getPRECOM() 
    {
        return get_ValueAsBigDecimal("PRECOM");
        
    }
    
    /** Set PREVEN.
    @param PREVEN PREVEN */
    public void setPREVEN (java.math.BigDecimal PREVEN)
    {
        set_Value ("PREVEN", PREVEN);
        
    }
    
    /** Get PREVEN.
    @return PREVEN */
    public java.math.BigDecimal getPREVEN() 
    {
        return get_ValueAsBigDecimal("PREVEN");
        
    }
    
    /** Set Status.
    @param Status Status of the currently running check */
    public void setStatus (String Status)
    {
        set_Value ("Status", Status);
        
    }
    
    /** Get Status.
    @return Status of the currently running check */
    public String getStatus() 
    {
        return (String)get_Value("Status");
        
    }
    
    /** Set Actualization User.
    @param USRACT Actualization User */
    public void setUSRACT (String USRACT)
    {
        set_Value ("USRACT", USRACT);
        
    }
    
    /** Get Actualization User.
    @return Actualization User */
    public String getUSRACT() 
    {
        return (String)get_Value("USRACT");
        
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
