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
/** Generated Model for E_XX_VMR_ORCM20
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_ORCM20 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_ORCM20_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_ORCM20 (Ctx ctx, int E_XX_VMR_ORCM20_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_ORCM20_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_ORCM20_ID == 0)
        {
            setE_XX_VMR_ORCM20_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_ORCM20 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198683789L;
    /** Last Updated Timestamp 2010-11-29 18:59:27.0 */
    public static final long updatedMS = 1291073367000L;
    /** AD_Table_ID=1000349 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_ORCM20");
        
    }
    ;
    
    /** TableName=E_XX_VMR_ORCM20 */
    public static final String Table_Name="E_XX_VMR_ORCM20";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Annulment Code.
    @param CODANU REASON FOR CANCELLATION */
    public void setCODANU (String CODANU)
    {
        set_Value ("CODANU", CODANU);
        
    }
    
    /** Get Annulment Code.
    @return REASON FOR CANCELLATION */
    public String getCODANU() 
    {
        return (String)get_Value("CODANU");
        
    }
    
    /** Set CODCEM.
    @param CODCEM SHIPPING CONDITION */
    public void setCODCEM (String CODCEM)
    {
        set_Value ("CODCEM", CODCEM);
        
    }
    
    /** Get CODCEM.
    @return SHIPPING CONDITION */
    public String getCODCEM() 
    {
        return (String)get_Value("CODCEM");
        
    }
    
    /** Set CODCMP.
    @param CODCMP ALTERNATE CODE PER/EMP */
    public void setCODCMP (String CODCMP)
    {
        set_Value ("CODCMP", CODCMP);
        
    }
    
    /** Get CODCMP.
    @return ALTERNATE CODE PER/EMP */
    public String getCODCMP() 
    {
        return (String)get_Value("CODCMP");
        
    }
    
    /** Set Department Code.
    @param CODDEP Department Code */
    public void setCODDEP (int CODDEP)
    {
        set_Value ("CODDEP", Integer.valueOf(CODDEP));
        
    }
    
    /** Get Department Code.
    @return Department Code */
    public int getCODDEP() 
    {
        return get_ValueAsInt("CODDEP");
        
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
    
    /** Set CODTEMP.
    @param CODTEMP SEASON CODE (NAME) */
    public void setCODTEMP (String CODTEMP)
    {
        set_Value ("CODTEMP", CODTEMP);
        
    }
    
    /** Get CODTEMP.
    @return SEASON CODE (NAME) */
    public String getCODTEMP() 
    {
        return (String)get_Value("CODTEMP");
        
    }
    
    /** Set CODUBE.
    @param CODUBE DELIVERY LOCATION CODE (NAME) */
    public void setCODUBE (String CODUBE)
    {
        set_Value ("CODUBE", CODUBE);
        
    }
    
    /** Get CODUBE.
    @return DELIVERY LOCATION CODE (NAME) */
    public String getCODUBE() 
    {
        return (String)get_Value("CODUBE");
        
    }
    
    /** Set CODVIA.
    @param CODVIA DISPATCH VIA */
    public void setCODVIA (String CODVIA)
    {
        set_Value ("CODVIA", CODVIA);
        
    }
    
    /** Get CODVIA.
    @return DISPATCH VIA */
    public String getCODVIA() 
    {
        return (String)get_Value("CODVIA");
        
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
    
    /** Set XX_CORCON.
    @param CORCON XX_CORCON */
    public void setCORCON (int CORCON)
    {
        set_Value ("CORCON", Integer.valueOf(CORCON));
        
    }
    
    /** Get XX_CORCON.
    @return XX_CORCON */
    public int getCORCON() 
    {
        return get_ValueAsInt("CORCON");
        
    }
    
    /** Set Discount 1.
    @param DESCU1 Discount 1 */
    public void setDESCU1 (java.math.BigDecimal DESCU1)
    {
        set_Value ("DESCU1", DESCU1);
        
    }
    
    /** Get Discount 1.
    @return Discount 1 */
    public java.math.BigDecimal getDESCU1() 
    {
        return get_ValueAsBigDecimal("DESCU1");
        
    }
    
    /** Set Discount 2.
    @param DESCU2 Discount 2 */
    public void setDESCU2 (java.math.BigDecimal DESCU2)
    {
        set_Value ("DESCU2", DESCU2);
        
    }
    
    /** Get Discount 2.
    @return Discount 2 */
    public java.math.BigDecimal getDESCU2() 
    {
        return get_ValueAsBigDecimal("DESCU2");
        
    }
    
    /** Set Discount 3.
    @param DESCU3 Discount 3 */
    public void setDESCU3 (java.math.BigDecimal DESCU3)
    {
        set_Value ("DESCU3", DESCU3);
        
    }
    
    /** Get Discount 3.
    @return Discount 3 */
    public java.math.BigDecimal getDESCU3() 
    {
        return get_ValueAsBigDecimal("DESCU3");
        
    }
    
    /** Set Discount 4.
    @param DESCU4 Discount 4 */
    public void setDESCU4 (java.math.BigDecimal DESCU4)
    {
        set_Value ("DESCU4", DESCU4);
        
    }
    
    /** Get Discount 4.
    @return Discount 4 */
    public java.math.BigDecimal getDESCU4() 
    {
        return get_ValueAsBigDecimal("DESCU4");
        
    }
    
    /** Set E_XX_VMR_ORCM20_ID.
    @param E_XX_VMR_ORCM20_ID E_XX_VMR_ORCM20_ID */
    public void setE_XX_VMR_ORCM20_ID (int E_XX_VMR_ORCM20_ID)
    {
        if (E_XX_VMR_ORCM20_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_ORCM20_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_ORCM20_ID", Integer.valueOf(E_XX_VMR_ORCM20_ID));
        
    }
    
    /** Get E_XX_VMR_ORCM20_ID.
    @return E_XX_VMR_ORCM20_ID */
    public int getE_XX_VMR_ORCM20_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_ORCM20_ID");
        
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
    
    /** Set Estimated Factor.
    @param FACCOE ESTIMATED CONVERSION FACTOR */
    public void setFACCOE (java.math.BigDecimal FACCOE)
    {
        set_Value ("FACCOE", FACCOE);
        
    }
    
    /** Get Estimated Factor.
    @return ESTIMATED CONVERSION FACTOR */
    public java.math.BigDecimal getFACCOE() 
    {
        return get_ValueAsBigDecimal("FACCOE");
        
    }
    
    /** Set FACCOP.
    @param FACCOP AVERAGE CONVERSION FACTOR */
    public void setFACCOP (java.math.BigDecimal FACCOP)
    {
        set_Value ("FACCOP", FACCOP);
        
    }
    
    /** Get FACCOP.
    @return AVERAGE CONVERSION FACTOR */
    public java.math.BigDecimal getFACCOP() 
    {
        return get_ValueAsBigDecimal("FACCOP");
        
    }
    
    /** Set FECCON.
    @param FECCON APPROPRIATION DATE */
    public void setFECCON (String FECCON)
    {
        set_Value ("FECCON", FECCON);
        
    }
    
    /** Get FECCON.
    @return APPROPRIATION DATE */
    public String getFECCON() 
    {
        return (String)get_Value("FECCON");
        
    }
    
    /** Set FECCRE.
    @param FECCRE CREATION DATE */
    public void setFECCRE (String FECCRE)
    {
        set_Value ("FECCRE", FECCRE);
        
    }
    
    /** Get FECCRE.
    @return CREATION DATE */
    public String getFECCRE() 
    {
        return (String)get_Value("FECCRE");
        
    }
    
    /** Set FECDESP.
    @param FECDESP RELEASE DATE */
    public void setFECDESP (String FECDESP)
    {
        set_Value ("FECDESP", FECDESP);
        
    }
    
    /** Get FECDESP.
    @return RELEASE DATE */
    public String getFECDESP() 
    {
        return (String)get_Value("FECDESP");
        
    }
    
    /** Set FECDESREAL.
    @param FECDESREAL FECDESREAL */
    public void setFECDESREAL (String FECDESREAL)
    {
        set_Value ("FECDESREAL", FECDESREAL);
        
    }
    
    /** Get FECDESREAL.
    @return FECDESREAL */
    public String getFECDESREAL() 
    {
        return (String)get_Value("FECDESREAL");
        
    }
    
    /** Set FECENT.
    @param FECENT DELIVERY DATE */
    public void setFECENT (String FECENT)
    {
        set_Value ("FECENT", FECENT);
        
    }
    
    /** Get FECENT.
    @return DELIVERY DATE */
    public String getFECENT() 
    {
        return (String)get_Value("FECENT");
        
    }
    
    /** Set FECENTAGE.
    @param FECENTAGE FECENTAGE */
    public void setFECENTAGE (String FECENTAGE)
    {
        set_Value ("FECENTAGE", FECENTAGE);
        
    }
    
    /** Get FECENTAGE.
    @return FECENTAGE */
    public String getFECENTAGE() 
    {
        return (String)get_Value("FECENTAGE");
        
    }
    
    /** Set FECENTEST.
    @param FECENTEST ESTIMATED DATE */
    public void setFECENTEST (String FECENTEST)
    {
        set_Value ("FECENTEST", FECENTEST);
        
    }
    
    /** Get FECENTEST.
    @return ESTIMATED DATE */
    public String getFECENTEST() 
    {
        return (String)get_Value("FECENTEST");
        
    }
    
    /** Set FECENTORI.
    @param FECENTORI FECENTORI */
    public void setFECENTORI (String FECENTORI)
    {
        set_Value ("FECENTORI", FECENTORI);
        
    }
    
    /** Get FECENTORI.
    @return FECENTORI */
    public String getFECENTORI() 
    {
        return (String)get_Value("FECENTORI");
        
    }
    
    /** Set FORPAG.
    @param FORPAG FORPAG */
    public void setFORPAG (int FORPAG)
    {
        set_Value ("FORPAG", Integer.valueOf(FORPAG));
        
    }
    
    /** Get FORPAG.
    @return FORPAG */
    public int getFORPAG() 
    {
        return get_ValueAsInt("FORPAG");
        
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
    
    /** Set Margin.
    @param MARGEN Margin */
    public void setMARGEN (java.math.BigDecimal MARGEN)
    {
        set_Value ("MARGEN", MARGEN);
        
    }
    
    /** Get Margin.
    @return Margin */
    public java.math.BigDecimal getMARGEN() 
    {
        return get_ValueAsBigDecimal("MARGEN");
        
    }
    
    /** Set MARPRM.
    @param MARPRM AVERAGE MARGIN PERCENTAGE */
    public void setMARPRM (java.math.BigDecimal MARPRM)
    {
        set_Value ("MARPRM", MARPRM);
        
    }
    
    /** Get MARPRM.
    @return AVERAGE MARGIN PERCENTAGE */
    public java.math.BigDecimal getMARPRM() 
    {
        return get_ValueAsBigDecimal("MARPRM");
        
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
    
    /** Set PTOLLE.
    @param PTOLLE PORT OF ARRIVAL */
    public void setPTOLLE (String PTOLLE)
    {
        set_Value ("PTOLLE", PTOLLE);
        
    }
    
    /** Get PTOLLE.
    @return PORT OF ARRIVAL */
    public String getPTOLLE() 
    {
        return (String)get_Value("PTOLLE");
        
    }
    
    /** Set STAMOV.
    @param STAMOV MOVEMENT STATUS */
    public void setSTAMOV (String STAMOV)
    {
        set_Value ("STAMOV", STAMOV);
        
    }
    
    /** Get STAMOV.
    @return MOVEMENT STATUS */
    public String getSTAMOV() 
    {
        return (String)get_Value("STAMOV");
        
    }
    
    /** Set STAORD.
    @param STAORD  PURCHASE ORDER STATUS */
    public void setSTAORD (String STAORD)
    {
        set_Value ("STAORD", STAORD);
        
    }
    
    /** Get STAORD.
    @return  PURCHASE ORDER STATUS */
    public String getSTAORD() 
    {
        return (String)get_Value("STAORD");
        
    }
    
    /** Set TOTCOST.
    @param TOTCOST TOTAL COST OF PURCHASE ORDER */
    public void setTOTCOST (java.math.BigDecimal TOTCOST)
    {
        set_Value ("TOTCOST", TOTCOST);
        
    }
    
    /** Get TOTCOST.
    @return TOTAL COST OF PURCHASE ORDER */
    public java.math.BigDecimal getTOTCOST() 
    {
        return get_ValueAsBigDecimal("TOTCOST");
        
    }
    
    /** Set TOTIVA.
    @param TOTIVA Tax Amount for a document */
    public void setTOTIVA (java.math.BigDecimal TOTIVA)
    {
        set_Value ("TOTIVA", TOTIVA);
        
    }
    
    /** Get TOTIVA.
    @return Tax Amount for a document */
    public java.math.BigDecimal getTOTIVA() 
    {
        return get_ValueAsBigDecimal("TOTIVA");
        
    }
    
    /** Set TOTPRO.
    @param TOTPRO Product Quantity */
    public void setTOTPRO (java.math.BigDecimal TOTPRO)
    {
        set_Value ("TOTPRO", TOTPRO);
        
    }
    
    /** Get TOTPRO.
    @return Product Quantity */
    public java.math.BigDecimal getTOTPRO() 
    {
        return get_ValueAsBigDecimal("TOTPRO");
        
    }
    
    /** Set TOTVEN.
    @param TOTVEN Total PVP de las lineas */
    public void setTOTVEN (java.math.BigDecimal TOTVEN)
    {
        set_Value ("TOTVEN", TOTVEN);
        
    }
    
    /** Get TOTVEN.
    @return Total PVP de las lineas */
    public java.math.BigDecimal getTOTVEN() 
    {
        return get_ValueAsBigDecimal("TOTVEN");
        
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
