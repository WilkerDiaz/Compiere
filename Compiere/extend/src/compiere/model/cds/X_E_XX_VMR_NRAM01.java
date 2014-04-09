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
/** Generated Model for E_XX_VMR_NRAM01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_NRAM01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_NRAM01_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_NRAM01 (Ctx ctx, int E_XX_VMR_NRAM01_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_NRAM01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_NRAM01_ID == 0)
        {
            setE_XX_VMR_NRAM01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_NRAM01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198852789L;
    /** Last Updated Timestamp 2010-11-29 19:02:16.0 */
    public static final long updatedMS = 1291073536000L;
    /** AD_Table_ID=1000367 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_NRAM01");
        
    }
    ;
    
    /** TableName=E_XX_VMR_NRAM01 */
    public static final String Table_Name="E_XX_VMR_NRAM01";
    
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
    public void setCODANU (int CODANU)
    {
        set_Value ("CODANU", Integer.valueOf(CODANU));
        
    }
    
    /** Get Annulment Code.
    @return REASON FOR CANCELLATION */
    public int getCODANU() 
    {
        return get_ValueAsInt("CODANU");
        
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
    
    /** Set CPREVI.
    @param CPREVI CPREVI CB98 */
    public void setCPREVI (String CPREVI)
    {
        set_Value ("CPREVI", CPREVI);
        
    }
    
    /** Get CPREVI.
    @return CPREVI CB98 */
    public String getCPREVI() 
    {
        return (String)get_Value("CPREVI");
        
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
    
    /** Set DIASTA.
    @param DIASTA Status Day at CB98 */
    public void setDIASTA (int DIASTA)
    {
        set_Value ("DIASTA", Integer.valueOf(DIASTA));
        
    }
    
    /** Get DIASTA.
    @return Status Day at CB98 */
    public int getDIASTA() 
    {
        return get_ValueAsInt("DIASTA");
        
    }
    
    /** Set E_XX_VMR_NRAM01_ID.
    @param E_XX_VMR_NRAM01_ID E_XX_VMR_NRAM01_ID */
    public void setE_XX_VMR_NRAM01_ID (int E_XX_VMR_NRAM01_ID)
    {
        if (E_XX_VMR_NRAM01_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_NRAM01_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_NRAM01_ID", Integer.valueOf(E_XX_VMR_NRAM01_ID));
        
    }
    
    /** Get E_XX_VMR_NRAM01_ID.
    @return E_XX_VMR_NRAM01_ID */
    public int getE_XX_VMR_NRAM01_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_NRAM01_ID");
        
    }
    
    /** Set Creation User (Invoice).
    @param FACTUR Creation User (Invoice) at CB98 */
    public void setFACTUR (String FACTUR)
    {
        set_Value ("FACTUR", FACTUR);
        
    }
    
    /** Get Creation User (Invoice).
    @return Creation User (Invoice) at CB98 */
    public String getFACTUR() 
    {
        return (String)get_Value("FACTUR");
        
    }
    
    /** Set Reception Month.
    @param MESREC Reception Month at CB98 */
    public void setMESREC (int MESREC)
    {
        set_Value ("MESREC", Integer.valueOf(MESREC));
        
    }
    
    /** Get Reception Month.
    @return Reception Month at CB98 */
    public int getMESREC() 
    {
        return get_ValueAsInt("MESREC");
        
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
    
    /** Set Status Month.
    @param MESSTA Status Month at CB98 */
    public void setMESSTA (int MESSTA)
    {
        set_Value ("MESSTA", Integer.valueOf(MESSTA));
        
    }
    
    /** Get Status Month.
    @return Status Month at CB98 */
    public int getMESSTA() 
    {
        return get_ValueAsInt("MESSTA");
        
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
    
    /** Set Creation User (Reception).
    @param RECEPT Creation User (Reception) at CB98 */
    public void setRECEPT (String RECEPT)
    {
        set_Value ("RECEPT", RECEPT);
        
    }
    
    /** Get Creation User (Reception).
    @return Creation User (Reception) at CB98 */
    public String getRECEPT() 
    {
        return (String)get_Value("RECEPT");
        
    }
    
    /** Set Reception Status.
    @param STAREC Reception Status at CB98 */
    public void setSTAREC (String STAREC)
    {
        set_Value ("STAREC", STAREC);
        
    }
    
    /** Get Reception Status.
    @return Reception Status at CB98 */
    public String getSTAREC() 
    {
        return (String)get_Value("STAREC");
        
    }
    
    /** Set TCHEQU.
    @param TCHEQU TCHEQU */
    public void setTCHEQU (String TCHEQU)
    {
        set_Value ("TCHEQU", TCHEQU);
        
    }
    
    /** Get TCHEQU.
    @return TCHEQU */
    public String getTCHEQU() 
    {
        return (String)get_Value("TCHEQU");
        
    }
    
    /** Set TCPREV.
    @param TCPREV TCPREV */
    public void setTCPREV (String TCPREV)
    {
        set_Value ("TCPREV", TCPREV);
        
    }
    
    /** Get TCPREV.
    @return TCPREV */
    public String getTCPREV() 
    {
        return (String)get_Value("TCPREV");
        
    }
    
    /** Set TFACTU.
    @param TFACTU TFACTU at CB87 */
    public void setTFACTU (String TFACTU)
    {
        set_Value ("TFACTU", TFACTU);
        
    }
    
    /** Get TFACTU.
    @return TFACTU at CB87 */
    public String getTFACTU() 
    {
        return (String)get_Value("TFACTU");
        
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
    
    /** Set Receipt Type.
    @param TIPREC Receipt Type at CB98 */
    public void setTIPREC (String TIPREC)
    {
        set_Value ("TIPREC", TIPREC);
        
    }
    
    /** Get Receipt Type.
    @return Receipt Type at CB98 */
    public String getTIPREC() 
    {
        return (String)get_Value("TIPREC");
        
    }
    
    /** Set Employee Type (Reception).
    @param TRECEP Employee Type (Reception) at CB98 */
    public void setTRECEP (String TRECEP)
    {
        set_Value ("TRECEP", TRECEP);
        
    }
    
    /** Get Employee Type (Reception).
    @return Employee Type (Reception) at CB98 */
    public String getTRECEP() 
    {
        return (String)get_Value("TRECEP");
        
    }
    
    /** Set TREVIR.
    @param TREVIR TREVIR */
    public void setTREVIR (String TREVIR)
    {
        set_Value ("TREVIR", TREVIR);
        
    }
    
    /** Get TREVIR.
    @return TREVIR */
    public String getTREVIR() 
    {
        return (String)get_Value("TREVIR");
        
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
    
    /** Set Creation User (Invoice).
    @param USRREG Creation User (Invoice) at CB98 */
    public void setUSRREG (String USRREG)
    {
        set_Value ("USRREG", USRREG);
        
    }
    
    /** Get Creation User (Invoice).
    @return Creation User (Invoice) at CB98 */
    public String getUSRREG() 
    {
        return (String)get_Value("USRREG");
        
    }
    
    /** Set Checkup Year.
    @param XX_ACHEQU Checkup Year at CB98 */
    public void setXX_ACHEQU (int XX_ACHEQU)
    {
        set_Value ("XX_ACHEQU", Integer.valueOf(XX_ACHEQU));
        
    }
    
    /** Get Checkup Year.
    @return Checkup Year at CB98 */
    public int getXX_ACHEQU() 
    {
        return get_ValueAsInt("XX_ACHEQU");
        
    }
    
    /** Set Previous Approval  Year .
    @param XX_ACPREV Previous Approval  Year at CB98 */
    public void setXX_ACPREV (int XX_ACPREV)
    {
        set_Value ("XX_ACPREV", Integer.valueOf(XX_ACPREV));
        
    }
    
    /** Get Previous Approval  Year .
    @return Previous Approval  Year at CB98 */
    public int getXX_ACPREV() 
    {
        return get_ValueAsInt("XX_ACPREV");
        
    }
    
    /** Set Invoice Year.
    @param XX_AFACTU Invoice Year */
    public void setXX_AFACTU (int XX_AFACTU)
    {
        set_Value ("XX_AFACTU", Integer.valueOf(XX_AFACTU));
        
    }
    
    /** Get Invoice Year.
    @return Invoice Year */
    public int getXX_AFACTU() 
    {
        return get_ValueAsInt("XX_AFACTU");
        
    }
    
    /** Set Reception Year.
    @param XX_ANOREC Reception Year at CB98 */
    public void setXX_ANOREC (int XX_ANOREC)
    {
        set_Value ("XX_ANOREC", Integer.valueOf(XX_ANOREC));
        
    }
    
    /** Get Reception Year.
    @return Reception Year at CB98 */
    public int getXX_ANOREC() 
    {
        return get_ValueAsInt("XX_ANOREC");
        
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
    
    /** Set Status Year(Reception).
    @param XX_ANOSTA Status Year(Reception) at CB98 */
    public void setXX_ANOSTA (int XX_ANOSTA)
    {
        set_Value ("XX_ANOSTA", Integer.valueOf(XX_ANOSTA));
        
    }
    
    /** Get Status Year(Reception).
    @return Status Year(Reception) at CB98 */
    public int getXX_ANOSTA() 
    {
        return get_ValueAsInt("XX_ANOSTA");
        
    }
    
    /** Set ARECEP.
    @param XX_ARECEP ARECEP */
    public void setXX_ARECEP (String XX_ARECEP)
    {
        set_Value ("XX_ARECEP", XX_ARECEP);
        
    }
    
    /** Get ARECEP.
    @return ARECEP */
    public String getXX_ARECEP() 
    {
        return (String)get_Value("XX_ARECEP");
        
    }
    
    /** Set Review Approval Year.
    @param XX_AREVIS Review Approval Year */
    public void setXX_AREVIS (int XX_AREVIS)
    {
        set_Value ("XX_AREVIS", Integer.valueOf(XX_AREVIS));
        
    }
    
    /** Get Review Approval Year.
    @return Review Approval Year */
    public int getXX_AREVIS() 
    {
        return get_ValueAsInt("XX_AREVIS");
        
    }
    
    /** Set CACHEQ.
    @param XX_CACHEQ CACHEQ */
    public void setXX_CACHEQ (String XX_CACHEQ)
    {
        set_Value ("XX_CACHEQ", XX_CACHEQ);
        
    }
    
    /** Get CACHEQ.
    @return CACHEQ */
    public String getXX_CACHEQ() 
    {
        return (String)get_Value("XX_CACHEQ");
        
    }
    
    /** Set COCHEQ.
    @param XX_COCHEQ COCHEQ */
    public void setXX_COCHEQ (String XX_COCHEQ)
    {
        set_Value ("XX_COCHEQ", XX_COCHEQ);
        
    }
    
    /** Get COCHEQ.
    @return COCHEQ */
    public String getXX_COCHEQ() 
    {
        return (String)get_Value("XX_COCHEQ");
        
    }
    
    /** Set Priority Code.
    @param XX_CODPRI Priority Code */
    public void setXX_CODPRI (String XX_CODPRI)
    {
        set_Value ("XX_CODPRI", XX_CODPRI);
        
    }
    
    /** Get Priority Code.
    @return Priority Code */
    public String getXX_CODPRI() 
    {
        return (String)get_Value("XX_CODPRI");
        
    }
    
    /** Set COREVI.
    @param XX_COREVI COREVI */
    public void setXX_COREVI (String XX_COREVI)
    {
        set_Value ("XX_COREVI", XX_COREVI);
        
    }
    
    /** Get COREVI.
    @return COREVI */
    public String getXX_COREVI() 
    {
        return (String)get_Value("XX_COREVI");
        
    }
    
    /** Set Reason for Retention.
    @param XX_CRETEN Reason for Retention */
    public void setXX_CRETEN (String XX_CRETEN)
    {
        set_Value ("XX_CRETEN", XX_CRETEN);
        
    }
    
    /** Get Reason for Retention.
    @return Reason for Retention */
    public String getXX_CRETEN() 
    {
        return (String)get_Value("XX_CRETEN");
        
    }
    
    /** Set Checkup day.
    @param XX_DCHEQU Checkup day */
    public void setXX_DCHEQU (int XX_DCHEQU)
    {
        set_Value ("XX_DCHEQU", Integer.valueOf(XX_DCHEQU));
        
    }
    
    /** Get Checkup day.
    @return Checkup day */
    public int getXX_DCHEQU() 
    {
        return get_ValueAsInt("XX_DCHEQU");
        
    }
    
    /** Set XX_DCPREV.
    @param XX_DCPREV XX_DCPREV */
    public void setXX_DCPREV (String XX_DCPREV)
    {
        set_Value ("XX_DCPREV", XX_DCPREV);
        
    }
    
    /** Get XX_DCPREV.
    @return XX_DCPREV */
    public String getXX_DCPREV() 
    {
        return (String)get_Value("XX_DCPREV");
        
    }
    
    /** Set Invoice Day.
    @param XX_DFACTU Invoice Day */
    public void setXX_DFACTU (int XX_DFACTU)
    {
        set_Value ("XX_DFACTU", Integer.valueOf(XX_DFACTU));
        
    }
    
    /** Get Invoice Day.
    @return Invoice Day */
    public int getXX_DFACTU() 
    {
        return get_ValueAsInt("XX_DFACTU");
        
    }
    
    /** Set Reception Day.
    @param XX_DIAREC Reception Day at CB98 */
    public void setXX_DIAREC (int XX_DIAREC)
    {
        set_Value ("XX_DIAREC", Integer.valueOf(XX_DIAREC));
        
    }
    
    /** Get Reception Day.
    @return Reception Day at CB98 */
    public int getXX_DIAREC() 
    {
        return get_ValueAsInt("XX_DIAREC");
        
    }
    
    /** Set Reception Day.
    @param XX_DRECEP Reception Day */
    public void setXX_DRECEP (String XX_DRECEP)
    {
        set_Value ("XX_DRECEP", XX_DRECEP);
        
    }
    
    /** Get Reception Day.
    @return Reception Day */
    public String getXX_DRECEP() 
    {
        return (String)get_Value("XX_DRECEP");
        
    }
    
    /** Set Review Approval Day.
    @param XX_DREVIS Review Approval Day */
    public void setXX_DREVIS (int XX_DREVIS)
    {
        set_Value ("XX_DREVIS", Integer.valueOf(XX_DREVIS));
        
    }
    
    /** Get Review Approval Day.
    @return Review Approval Day */
    public int getXX_DREVIS() 
    {
        return get_ValueAsInt("XX_DREVIS");
        
    }
    
    /** Set Payment Rule.
    @param XX_FOPAGO Payment Rule */
    public void setXX_FOPAGO (String XX_FOPAGO)
    {
        set_Value ("XX_FOPAGO", XX_FOPAGO);
        
    }
    
    /** Get Payment Rule.
    @return Payment Rule */
    public String getXX_FOPAGO() 
    {
        return (String)get_Value("XX_FOPAGO");
        
    }
    
    /** Set Checkup Month.
    @param XX_MCHEQU Checkup Month at CB98 */
    public void setXX_MCHEQU (int XX_MCHEQU)
    {
        set_Value ("XX_MCHEQU", Integer.valueOf(XX_MCHEQU));
        
    }
    
    /** Get Checkup Month.
    @return Checkup Month at CB98 */
    public int getXX_MCHEQU() 
    {
        return get_ValueAsInt("XX_MCHEQU");
        
    }
    
    /** Set Previous Approval  Month.
    @param XX_MCPREV Previous Approval  Month at CB98 */
    public void setXX_MCPREV (int XX_MCPREV)
    {
        set_Value ("XX_MCPREV", Integer.valueOf(XX_MCPREV));
        
    }
    
    /** Get Previous Approval  Month.
    @return Previous Approval  Month at CB98 */
    public int getXX_MCPREV() 
    {
        return get_ValueAsInt("XX_MCPREV");
        
    }
    
    /** Set Month Invoice.
    @param XX_MFACTU Month Invoice */
    public void setXX_MFACTU (int XX_MFACTU)
    {
        set_Value ("XX_MFACTU", Integer.valueOf(XX_MFACTU));
        
    }
    
    /** Get Month Invoice.
    @return Month Invoice */
    public int getXX_MFACTU() 
    {
        return get_ValueAsInt("XX_MFACTU");
        
    }
    
    /** Set XX_MRECEP.
    @param XX_MRECEP XX_MRECEP */
    public void setXX_MRECEP (String XX_MRECEP)
    {
        set_Value ("XX_MRECEP", XX_MRECEP);
        
    }
    
    /** Get XX_MRECEP.
    @return XX_MRECEP */
    public String getXX_MRECEP() 
    {
        return (String)get_Value("XX_MRECEP");
        
    }
    
    /** Set Review Approval Day.
    @param XX_MREVIS Review Approval Day */
    public void setXX_MREVIS (int XX_MREVIS)
    {
        set_Value ("XX_MREVIS", Integer.valueOf(XX_MREVIS));
        
    }
    
    /** Get Review Approval Day.
    @return Review Approval Day */
    public int getXX_MREVIS() 
    {
        return get_ValueAsInt("XX_MREVIS");
        
    }
    
    /** Set Fist Bill Status.
    @param XX_SFACUA Fist Bill Status */
    public void setXX_SFACUA (String XX_SFACUA)
    {
        set_Value ("XX_SFACUA", XX_SFACUA);
        
    }
    
    /** Get Fist Bill Status.
    @return Fist Bill Status */
    public String getXX_SFACUA() 
    {
        return (String)get_Value("XX_SFACUA");
        
    }
    
    /** Set XX_STAAUT.
    @param XX_STAAUT XX_STAAUT */
    public void setXX_STAAUT (String XX_STAAUT)
    {
        set_Value ("XX_STAAUT", XX_STAAUT);
        
    }
    
    /** Get XX_STAAUT.
    @return XX_STAAUT */
    public String getXX_STAAUT() 
    {
        return (String)get_Value("XX_STAAUT");
        
    }
    
    /** Set STAIMP.
    @param XX_STAIMP STAIMP */
    public void setXX_STAIMP (String XX_STAIMP)
    {
        set_Value ("XX_STAIMP", XX_STAIMP);
        
    }
    
    /** Get STAIMP.
    @return STAIMP */
    public String getXX_STAIMP() 
    {
        return (String)get_Value("XX_STAIMP");
        
    }
    
    /** Set Suspension Status.
    @param XX_STASUS Suspension Status */
    public void setXX_STASUS (String XX_STASUS)
    {
        set_Value ("XX_STASUS", XX_STASUS);
        
    }
    
    /** Get Suspension Status.
    @return Suspension Status */
    public String getXX_STASUS() 
    {
        return (String)get_Value("XX_STASUS");
        
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
    
    /** Set XX_TACHEQ.
    @param XX_TACHEQ XX_TACHEQ */
    public void setXX_TACHEQ (String XX_TACHEQ)
    {
        set_Value ("XX_TACHEQ", XX_TACHEQ);
        
    }
    
    /** Get XX_TACHEQ.
    @return XX_TACHEQ */
    public String getXX_TACHEQ() 
    {
        return (String)get_Value("XX_TACHEQ");
        
    }
    
    /** Set AUTORIZATION USER.
    @param XX_USRAUT AUTORIZATION USER */
    public void setXX_USRAUT (String XX_USRAUT)
    {
        set_Value ("XX_USRAUT", XX_USRAUT);
        
    }
    
    /** Get AUTORIZATION USER.
    @return AUTORIZATION USER */
    public String getXX_USRAUT() 
    {
        return (String)get_Value("XX_USRAUT");
        
    }
    
    
}
