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
/** Generated Model for E_XX_VMR_ORCD20
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_ORCD20 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_ORCD20_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_ORCD20 (Ctx ctx, int E_XX_VMR_ORCD20_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_ORCD20_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_ORCD20_ID == 0)
        {
            setE_XX_VMR_ORCD20_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_ORCD20 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198542789L;
    /** Last Updated Timestamp 2010-11-29 18:57:06.0 */
    public static final long updatedMS = 1291073226000L;
    /** AD_Table_ID=1000345 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_ORCD20");
        
    }
    ;
    
    /** TableName=E_XX_VMR_ORCD20 */
    public static final String Table_Name="E_XX_VMR_ORCD20";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Purchase Quantity.
    @param CANCOM PURCHASE QUANTITY */
    public void setCANCOM (int CANCOM)
    {
        set_Value ("CANCOM", Integer.valueOf(CANCOM));
        
    }
    
    /** Get Purchase Quantity.
    @return PURCHASE QUANTITY */
    public int getCANCOM() 
    {
        return get_ValueAsInt("CANCOM");
        
    }
    
    /** Set Pieces by Purchase.
    @param CANEMC Pieces by Purchase */
    public void setCANEMC (int CANEMC)
    {
        set_Value ("CANEMC", Integer.valueOf(CANEMC));
        
    }
    
    /** Get Pieces by Purchase.
    @return Pieces by Purchase */
    public int getCANEMC() 
    {
        return get_ValueAsInt("CANEMC");
        
    }
    
    /** Set Sale Package Quantity.
    @param CANEMV SALE PACKAGE QUANTITY */
    public void setCANEMV (int CANEMV)
    {
        set_Value ("CANEMV", Integer.valueOf(CANEMV));
        
    }
    
    /** Get Sale Package Quantity.
    @return SALE PACKAGE QUANTITY */
    public int getCANEMV() 
    {
        return get_ValueAsInt("CANEMV");
        
    }
    
    /** Set Gift Quantity.
    @param CANOBS GIFT AMOUNT */
    public void setCANOBS (int CANOBS)
    {
        set_Value ("CANOBS", Integer.valueOf(CANOBS));
        
    }
    
    /** Get Gift Quantity.
    @return GIFT AMOUNT */
    public int getCANOBS() 
    {
        return get_ValueAsInt("CANOBS");
        
    }
    
    /** Set Sale Amount.
    @param CANVEN Sale Amount */
    public void setCANVEN (int CANVEN)
    {
        set_Value ("CANVEN", Integer.valueOf(CANVEN));
        
    }
    
    /** Get Sale Amount.
    @return Sale Amount */
    public int getCANVEN() 
    {
        return get_ValueAsInt("CANVEN");
        
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
    
    /** Set Line Code.
    @param CODLIN LINE CODE */
    public void setCODLIN (int CODLIN)
    {
        set_Value ("CODLIN", Integer.valueOf(CODLIN));
        
    }
    
    /** Get Line Code.
    @return LINE CODE */
    public int getCODLIN() 
    {
        return get_ValueAsInt("CODLIN");
        
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
    
    /** Set Section Code.
    @param CODSEC Section Code */
    public void setCODSEC (int CODSEC)
    {
        set_Value ("CODSEC", Integer.valueOf(CODSEC));
        
    }
    
    /** Get Section Code.
    @return Section Code */
    public int getCODSEC() 
    {
        return get_ValueAsInt("CODSEC");
        
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
    
    /** Set E_XX_VMR_ORCD20_ID.
    @param E_XX_VMR_ORCD20_ID E_XX_VMR_ORCD20_ID */
    public void setE_XX_VMR_ORCD20_ID (int E_XX_VMR_ORCD20_ID)
    {
        if (E_XX_VMR_ORCD20_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_ORCD20_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_ORCD20_ID", Integer.valueOf(E_XX_VMR_ORCD20_ID));
        
    }
    
    /** Get E_XX_VMR_ORCD20_ID.
    @return E_XX_VMR_ORCD20_ID */
    public int getE_XX_VMR_ORCD20_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_ORCD20_ID");
        
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
    
    /** Set Definitive Factor.
    @param FACCOM Factor Definitivo */
    public void setFACCOM (java.math.BigDecimal FACCOM)
    {
        set_Value ("FACCOM", FACCOM);
        
    }
    
    /** Get Definitive Factor.
    @return Factor Definitivo */
    public java.math.BigDecimal getFACCOM() 
    {
        return get_ValueAsBigDecimal("FACCOM");
        
    }
    
    /** Set Sale Tax.
    @param IMPVEN TAX AMOUNT */
    public void setIMPVEN (java.math.BigDecimal IMPVEN)
    {
        set_Value ("IMPVEN", IMPVEN);
        
    }
    
    /** Get Sale Tax.
    @return TAX AMOUNT */
    public java.math.BigDecimal getIMPVEN() 
    {
        return get_ValueAsBigDecimal("IMPVEN");
        
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
    
    /** Set Unit Margin.
    @param MARUNI Unit Margin */
    public void setMARUNI (java.math.BigDecimal MARUNI)
    {
        set_Value ("MARUNI", MARUNI);
        
    }
    
    /** Get Unit Margin.
    @return Unit Margin */
    public java.math.BigDecimal getMARUNI() 
    {
        return get_ValueAsBigDecimal("MARUNI");
        
    }
    
    /** Set Actual Price.
    @param MCOSTO Actual Price */
    public void setMCOSTO (java.math.BigDecimal MCOSTO)
    {
        set_Value ("MCOSTO", MCOSTO);
        
    }
    
    /** Get Actual Price.
    @return Actual Price */
    public java.math.BigDecimal getMCOSTO() 
    {
        return get_ValueAsBigDecimal("MCOSTO");
        
    }
    
    /** Set Sale Cost.
    @param MCOSVE Sale Cost */
    public void setMCOSVE (java.math.BigDecimal MCOSVE)
    {
        set_Value ("MCOSVE", MCOSVE);
        
    }
    
    /** Get Sale Cost.
    @return Sale Cost */
    public java.math.BigDecimal getMCOSVE() 
    {
        return get_ValueAsBigDecimal("MCOSVE");
        
    }
    
    /** Set Discount Amount.
    @param MONDES Discount Amount */
    public void setMONDES (java.math.BigDecimal MONDES)
    {
        set_Value ("MONDES", MONDES);
        
    }
    
    /** Get Discount Amount.
    @return Discount Amount */
    public java.math.BigDecimal getMONDES() 
    {
        return get_ValueAsBigDecimal("MONDES");
        
    }
    
    /** Set Package Multiple.
    @param MULEMP Package Multiple */
    public void setMULEMP (java.math.BigDecimal MULEMP)
    {
        set_Value ("MULEMP", MULEMP);
        
    }
    
    /** Get Package Multiple.
    @return Package Multiple */
    public java.math.BigDecimal getMULEMP() 
    {
        return get_ValueAsBigDecimal("MULEMP");
        
    }
    
    /** Set Sale Amount.
    @param MVENTA SALE AMOUNT */
    public void setMVENTA (java.math.BigDecimal MVENTA)
    {
        set_Value ("MVENTA", MVENTA);
        
    }
    
    /** Get Sale Amount.
    @return SALE AMOUNT */
    public java.math.BigDecimal getMVENTA() 
    {
        return get_ValueAsBigDecimal("MVENTA");
        
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
