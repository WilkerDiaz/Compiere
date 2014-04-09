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
/** Generated Model for E_XX_VMR_PROM01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_PROM01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_PROM01_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_PROM01 (Ctx ctx, int E_XX_VMR_PROM01_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_PROM01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_PROM01_ID == 0)
        {
            setE_XX_VMR_PROM01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_PROM01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27597960948789L;
    /** Last Updated Timestamp 2011-09-12 09:23:52.0 */
    public static final long updatedMS = 1315835632000L;
    /** AD_Table_ID=1000370 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_PROM01");
        
    }
    ;
    
    /** TableName=E_XX_VMR_PROM01 */
    public static final String Table_Name="E_XX_VMR_PROM01";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set ALTO.
    @param ALTO ALTO */
    public void setALTO (String ALTO)
    {
        set_Value ("ALTO", ALTO);
        
    }
    
    /** Get ALTO.
    @return ALTO */
    public String getALTO() 
    {
        return (String)get_Value("ALTO");
        
    }
    
    /** Set ANCHO.
    @param ANCHO ANCHO */
    public void setANCHO (String ANCHO)
    {
        set_Value ("ANCHO", ANCHO);
        
    }
    
    /** Get ANCHO.
    @return ANCHO */
    public String getANCHO() 
    {
        return (String)get_Value("ANCHO");
        
    }
    
    /** Set ANOFIN.
    @param ANOFIN ANOFIN */
    public void setANOFIN (int ANOFIN)
    {
        set_Value ("ANOFIN", Integer.valueOf(ANOFIN));
        
    }
    
    /** Get ANOFIN.
    @return ANOFIN */
    public int getANOFIN() 
    {
        return get_ValueAsInt("ANOFIN");
        
    }
    
    /** Set ANOING.
    @param ANOING ANOING */
    public void setANOING (int ANOING)
    {
        set_Value ("ANOING", Integer.valueOf(ANOING));
        
    }
    
    /** Get ANOING.
    @return ANOING */
    public int getANOING() 
    {
        return get_ValueAsInt("ANOING");
        
    }
    
    /** Set CANMAX.
    @param CANMAX CANMAX */
    public void setCANMAX (String CANMAX)
    {
        set_Value ("CANMAX", CANMAX);
        
    }
    
    /** Get CANMAX.
    @return CANMAX */
    public String getCANMAX() 
    {
        return (String)get_Value("CANMAX");
        
    }
    
    /** Set CANMIN.
    @param CANMIN CANMIN */
    public void setCANMIN (String CANMIN)
    {
        set_Value ("CANMIN", CANMIN);
        
    }
    
    /** Get CANMIN.
    @return CANMIN */
    public String getCANMIN() 
    {
        return (String)get_Value("CANMIN");
        
    }
    
    /** Set Long Characteristic.
    @param CARACT FULL FEATURE DESCRIPTION */
    public void setCARACT (String CARACT)
    {
        set_Value ("CARACT", CARACT);
        
    }
    
    /** Get Long Characteristic.
    @return FULL FEATURE DESCRIPTION */
    public String getCARACT() 
    {
        return (String)get_Value("CARACT");
        
    }
    
    /** Set CARACT3.
    @param CARACT3 CARACT3 */
    public void setCARACT3 (String CARACT3)
    {
        set_Value ("CARACT3", CARACT3);
        
    }
    
    /** Get CARACT3.
    @return CARACT3 */
    public String getCARACT3() 
    {
        return (String)get_Value("CARACT3");
        
    }
    
    /** Set CARACT4.
    @param CARACT4 CARACT4 */
    public void setCARACT4 (String CARACT4)
    {
        set_Value ("CARACT4", CARACT4);
        
    }
    
    /** Get CARACT4.
    @return CARACT4 */
    public String getCARACT4() 
    {
        return (String)get_Value("CARACT4");
        
    }
    
    /** Set CARACT5.
    @param CARACT5 CARACT5 */
    public void setCARACT5 (String CARACT5)
    {
        set_Value ("CARACT5", CARACT5);
        
    }
    
    /** Get CARACT5.
    @return CARACT5 */
    public String getCARACT5() 
    {
        return (String)get_Value("CARACT5");
        
    }
    
    /** Set CARAC1.
    @param CARAC1 CARAC1 */
    public void setCARAC1 (String CARAC1)
    {
        set_Value ("CARAC1", CARAC1);
        
    }
    
    /** Get CARAC1.
    @return CARAC1 */
    public String getCARAC1() 
    {
        return (String)get_Value("CARAC1");
        
    }
    
    /** Set CARAC2.
    @param CARAC2 CARAC2 */
    public void setCARAC2 (String CARAC2)
    {
        set_Value ("CARAC2", CARAC2);
        
    }
    
    /** Get CARAC2.
    @return CARAC2 */
    public String getCARAC2() 
    {
        return (String)get_Value("CARAC2");
        
    }
    
    /** Set CAUFIN.
    @param CAUFIN CAUFIN */
    public void setCAUFIN (String CAUFIN)
    {
        set_Value ("CAUFIN", CAUFIN);
        
    }
    
    /** Get CAUFIN.
    @return CAUFIN */
    public String getCAUFIN() 
    {
        return (String)get_Value("CAUFIN");
        
    }
    
    /** Set CODARA.
    @param CODARA CODARA */
    public void setCODARA (int CODARA)
    {
        set_Value ("CODARA", Integer.valueOf(CODARA));
        
    }
    
    /** Get CODARA.
    @return CODARA */
    public int getCODARA() 
    {
        return get_ValueAsInt("CODARA");
        
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
    
    /** Set Line Code.
    @param CODLIN LINE CODE */
    public void setCODLIN (String CODLIN)
    {
        set_Value ("CODLIN", CODLIN);
        
    }
    
    /** Get Line Code.
    @return LINE CODE */
    public String getCODLIN() 
    {
        return (String)get_Value("CODLIN");
        
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
    
    /** Set CODP01.
    @param CODP01 CODP01 */
    public void setCODP01 (String CODP01)
    {
        set_Value ("CODP01", CODP01);
        
    }
    
    /** Get CODP01.
    @return CODP01 */
    public String getCODP01() 
    {
        return (String)get_Value("CODP01");
        
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
    
    /** Set CONCEP.
    @param CONCEP CONCEP */
    public void setCONCEP (String CONCEP)
    {
        set_Value ("CONCEP", CONCEP);
        
    }
    
    /** Get CONCEP.
    @return CONCEP */
    public String getCONCEP() 
    {
        return (String)get_Value("CONCEP");
        
    }
    
    /** Set COPRRE.
    @param COPRRE COPRRE */
    public void setCOPRRE (int COPRRE)
    {
        set_Value ("COPRRE", Integer.valueOf(COPRRE));
        
    }
    
    /** Get COPRRE.
    @return COPRRE */
    public int getCOPRRE() 
    {
        return get_ValueAsInt("COPRRE");
        
    }
    
    /** Set DIAFIN.
    @param DIAFIN DIAFIN */
    public void setDIAFIN (int DIAFIN)
    {
        set_Value ("DIAFIN", Integer.valueOf(DIAFIN));
        
    }
    
    /** Get DIAFIN.
    @return DIAFIN */
    public int getDIAFIN() 
    {
        return get_ValueAsInt("DIAFIN");
        
    }
    
    /** Set DIAING.
    @param DIAING DIAING */
    public void setDIAING (int DIAING)
    {
        set_Value ("DIAING", Integer.valueOf(DIAING));
        
    }
    
    /** Get DIAING.
    @return DIAING */
    public int getDIAING() 
    {
        return get_ValueAsInt("DIAING");
        
    }
    
    /** Set E_XX_VMR_PROM01_ID.
    @param E_XX_VMR_PROM01_ID E_XX_VMR_PROM01_ID */
    public void setE_XX_VMR_PROM01_ID (int E_XX_VMR_PROM01_ID)
    {
        if (E_XX_VMR_PROM01_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_PROM01_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_PROM01_ID", Integer.valueOf(E_XX_VMR_PROM01_ID));
        
    }
    
    /** Get E_XX_VMR_PROM01_ID.
    @return E_XX_VMR_PROM01_ID */
    public int getE_XX_VMR_PROM01_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_PROM01_ID");
        
    }
    
    /** Set FRAGIL.
    @param FRAGIL FRAGIL */
    public void setFRAGIL (String FRAGIL)
    {
        set_Value ("FRAGIL", FRAGIL);
        
    }
    
    /** Get FRAGIL.
    @return FRAGIL */
    public String getFRAGIL() 
    {
        return (String)get_Value("FRAGIL");
        
    }
    
    /** Set GRUPRO.
    @param GRUPRO GRUPRO */
    public void setGRUPRO (String GRUPRO)
    {
        set_Value ("GRUPRO", GRUPRO);
        
    }
    
    /** Get GRUPRO.
    @return GRUPRO */
    public String getGRUPRO() 
    {
        return (String)get_Value("GRUPRO");
        
    }
    
    /** Set INDINV.
    @param INDINV INDINV */
    public void setINDINV (String INDINV)
    {
        set_Value ("INDINV", INDINV);
        
    }
    
    /** Get INDINV.
    @return INDINV */
    public String getINDINV() 
    {
        return (String)get_Value("INDINV");
        
    }
    
    /** Set INDPRI.
    @param INDPRI INDPRI */
    public void setINDPRI (String INDPRI)
    {
        set_Value ("INDPRI", INDPRI);
        
    }
    
    /** Get INDPRI.
    @return INDPRI */
    public String getINDPRI() 
    {
        return (String)get_Value("INDPRI");
        
    }
    
    /** Set INIMOC.
    @param INIMOC INIMOC */
    public void setINIMOC (String INIMOC)
    {
        set_Value ("INIMOC", INIMOC);
        
    }
    
    /** Get INIMOC.
    @return INIMOC */
    public String getINIMOC() 
    {
        return (String)get_Value("INIMOC");
        
    }
    
    /** Set Brand.
    @param MARCA Brand */
    public void setMARCA (String MARCA)
    {
        set_Value ("MARCA", MARCA);
        
    }
    
    /** Get Brand.
    @return Brand */
    public String getMARCA() 
    {
        return (String)get_Value("MARCA");
        
    }
    
    /** Set MESFIN.
    @param MESFIN MESFIN */
    public void setMESFIN (int MESFIN)
    {
        set_Value ("MESFIN", Integer.valueOf(MESFIN));
        
    }
    
    /** Get MESFIN.
    @return MESFIN */
    public int getMESFIN() 
    {
        return get_ValueAsInt("MESFIN");
        
    }
    
    /** Set MESING.
    @param MESING MESING */
    public void setMESING (int MESING)
    {
        set_Value ("MESING", Integer.valueOf(MESING));
        
    }
    
    /** Get MESING.
    @return MESING */
    public int getMESING() 
    {
        return get_ValueAsInt("MESING");
        
    }
    
    /** Set Product's English Name.
    @param NOMING Product's English Name */
    public void setNOMING (String NOMING)
    {
        set_Value ("NOMING", NOMING);
        
    }
    
    /** Get Product's English Name.
    @return Product's English Name */
    public String getNOMING() 
    {
        return (String)get_Value("NOMING");
        
    }
    
    /** Set NOMPRO.
    @param NOMPRO NOMPRO */
    public void setNOMPRO (String NOMPRO)
    {
        set_Value ("NOMPRO", NOMPRO);
        
    }
    
    /** Get NOMPRO.
    @return NOMPRO */
    public String getNOMPRO() 
    {
        return (String)get_Value("NOMPRO");
        
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
    
    /** Set Weight (CB98).
    @param PESO Weight (CB98) */
    public void setPESO (java.math.BigDecimal PESO)
    {
        set_Value ("PESO", PESO);
        
    }
    
    /** Get Weight (CB98).
    @return Weight (CB98) */
    public java.math.BigDecimal getPESO() 
    {
        return get_ValueAsBigDecimal("PESO");
        
    }
    
    /** Set PROFUN.
    @param PROFUN PROFUN */
    public void setPROFUN (String PROFUN)
    {
        set_Value ("PROFUN", PROFUN);
        
    }
    
    /** Get PROFUN.
    @return PROFUN */
    public String getPROFUN() 
    {
        return (String)get_Value("PROFUN");
        
    }
    
    /** Set Label Price.
    @param PVPETI Label Price */
    public void setPVPETI (java.math.BigDecimal PVPETI)
    {
        set_Value ("PVPETI", PVPETI);
        
    }
    
    /** Get Label Price.
    @return Label Price */
    public java.math.BigDecimal getPVPETI() 
    {
        return get_ValueAsBigDecimal("PVPETI");
        
    }
    
    /** Set Vendor Reference.
    @param REFPRO Alphanumeric identifier of the entity */
    public void setREFPRO (String REFPRO)
    {
        set_Value ("REFPRO", REFPRO);
        
    }
    
    /** Get Vendor Reference.
    @return Alphanumeric identifier of the entity */
    public String getREFPRO() 
    {
        return (String)get_Value("REFPRO");
        
    }
    
    /** Set REFPR1.
    @param REFPR1 REFPR1 */
    public void setREFPR1 (String REFPR1)
    {
        set_Value ("REFPR1", REFPR1);
        
    }
    
    /** Get REFPR1.
    @return REFPR1 */
    public String getREFPR1() 
    {
        return (String)get_Value("REFPR1");
        
    }
    
    /** Set Section.
    @param SECCIO Section at CB98 */
    public void setSECCIO (String SECCIO)
    {
        set_Value ("SECCIO", SECCIO);
        
    }
    
    /** Get Section.
    @return Section at CB98 */
    public String getSECCIO() 
    {
        return (String)get_Value("SECCIO");
        
    }
    
    /** Set Product Status.
    @param STAPRO Product Status at CB98 */
    public void setSTAPRO (String STAPRO)
    {
        set_Value ("STAPRO", STAPRO);
        
    }
    
    /** Get Product Status.
    @return Product Status at CB98 */
    public String getSTAPRO() 
    {
        return (String)get_Value("STAPRO");
        
    }
    
    /** Set SUBLIN.
    @param SUBLIN SUBLIN */
    public void setSUBLIN (String SUBLIN)
    {
        set_Value ("SUBLIN", SUBLIN);
        
    }
    
    /** Get SUBLIN.
    @return SUBLIN */
    public String getSUBLIN() 
    {
        return (String)get_Value("SUBLIN");
        
    }
    
    /** Set TIEMAX.
    @param TIEMAX TIEMAX */
    public void setTIEMAX (String TIEMAX)
    {
        set_Value ("TIEMAX", TIEMAX);
        
    }
    
    /** Get TIEMAX.
    @return TIEMAX */
    public String getTIEMAX() 
    {
        return (String)get_Value("TIEMAX");
        
    }
    
    /** Set TIEPER.
    @param TIEPER TIEPER */
    public void setTIEPER (String TIEPER)
    {
        set_Value ("TIEPER", TIEPER);
        
    }
    
    /** Get TIEPER.
    @return TIEPER */
    public String getTIEPER() 
    {
        return (String)get_Value("TIEPER");
        
    }
    
    /** Set TIPEMB.
    @param TIPEMB TIPEMB */
    public void setTIPEMB (String TIPEMB)
    {
        set_Value ("TIPEMB", TIPEMB);
        
    }
    
    /** Get TIPEMB.
    @return TIPEMB */
    public String getTIPEMB() 
    {
        return (String)get_Value("TIPEMB");
        
    }
    
    /** Set Label Type.
    @param TIPETI Label Type */
    public void setTIPETI (String TIPETI)
    {
        set_Value ("TIPETI", TIPETI);
        
    }
    
    /** Get Label Type.
    @return Label Type */
    public String getTIPETI() 
    {
        return (String)get_Value("TIPETI");
        
    }
    
    /** Set Tax Type.
    @param TIPIMP Tax Type */
    public void setTIPIMP (String TIPIMP)
    {
        set_Value ("TIPIMP", TIPIMP);
        
    }
    
    /** Get Tax Type.
    @return Tax Type */
    public String getTIPIMP() 
    {
        return (String)get_Value("TIPIMP");
        
    }
    
    /** Set Inventory Type.
    @param TIPINV Inventory Type */
    public void setTIPINV (String TIPINV)
    {
        set_Value ("TIPINV", TIPINV);
        
    }
    
    /** Get Inventory Type.
    @return Inventory Type */
    public String getTIPINV() 
    {
        return (String)get_Value("TIPINV");
        
    }
    
    /** Set Product Class.
    @param TIPPRO Product Class */
    public void setTIPPRO (String TIPPRO)
    {
        set_Value ("TIPPRO", TIPPRO);
        
    }
    
    /** Get Product Class.
    @return Product Class */
    public String getTIPPRO() 
    {
        return (String)get_Value("TIPPRO");
        
    }
    
    /** Set Unit Purchase.
    @param UNICOM PURCHASE UNIT OF MEASURE */
    public void setUNICOM (String UNICOM)
    {
        set_Value ("UNICOM", UNICOM);
        
    }
    
    /** Get Unit Purchase.
    @return PURCHASE UNIT OF MEASURE */
    public String getUNICOM() 
    {
        return (String)get_Value("UNICOM");
        
    }
    
    /** Set UNIDIM.
    @param UNIDIM UNIDIM */
    public void setUNIDIM (String UNIDIM)
    {
        set_Value ("UNIDIM", UNIDIM);
        
    }
    
    /** Get UNIDIM.
    @return UNIDIM */
    public String getUNIDIM() 
    {
        return (String)get_Value("UNIDIM");
        
    }
    
    /** Set UNIPES.
    @param UNIPES UNIPES */
    public void setUNIPES (String UNIPES)
    {
        set_Value ("UNIPES", UNIPES);
        
    }
    
    /** Get UNIPES.
    @return UNIPES */
    public String getUNIPES() 
    {
        return (String)get_Value("UNIPES");
        
    }
    
    /** Set Unit of Measure.
    @param UNIVEN SELLING UNIT OF MEASURE */
    public void setUNIVEN (String UNIVEN)
    {
        set_Value ("UNIVEN", UNIVEN);
        
    }
    
    /** Get Unit of Measure.
    @return SELLING UNIT OF MEASURE */
    public String getUNIVEN() 
    {
        return (String)get_Value("UNIVEN");
        
    }
    
    /** Set Barcode.
    @param UPCEAM Barcode */
    public void setUPCEAM (String UPCEAM)
    {
        set_Value ("UPCEAM", UPCEAM);
        
    }
    
    /** Get Barcode.
    @return Barcode */
    public String getUPCEAM() 
    {
        return (String)get_Value("UPCEAM");
        
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
