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
/** Generated Model for E_XX_VMR_ORCM21
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_ORCM21 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_ORCM21_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_ORCM21 (Ctx ctx, int E_XX_VMR_ORCM21_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_ORCM21_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_ORCM21_ID == 0)
        {
            setE_XX_VMR_ORCM21_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_ORCM21 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198703789L;
    /** Last Updated Timestamp 2010-11-29 18:59:47.0 */
    public static final long updatedMS = 1291073387000L;
    /** AD_Table_ID=1000350 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_ORCM21");
        
    }
    ;
    
    /** TableName=E_XX_VMR_ORCM21 */
    public static final String Table_Name="E_XX_VMR_ORCM21";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set E_XX_VMR_ORCM21_ID.
    @param E_XX_VMR_ORCM21_ID E_XX_VMR_ORCM21_ID */
    public void setE_XX_VMR_ORCM21_ID (int E_XX_VMR_ORCM21_ID)
    {
        if (E_XX_VMR_ORCM21_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_ORCM21_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_ORCM21_ID", Integer.valueOf(E_XX_VMR_ORCM21_ID));
        
    }
    
    /** Get E_XX_VMR_ORCM21_ID.
    @return E_XX_VMR_ORCM21_ID */
    public int getE_XX_VMR_ORCM21_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_ORCM21_ID");
        
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
    public void setMULEMP (int MULEMP)
    {
        set_Value ("MULEMP", Integer.valueOf(MULEMP));
        
    }
    
    /** Get Package Multiple.
    @return Package Multiple */
    public int getMULEMP() 
    {
        return get_ValueAsInt("MULEMP");
        
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
    
    /** Set Position Number.
    @param NUMPOS Position Number */
    public void setNUMPOS (int NUMPOS)
    {
        set_Value ("NUMPOS", Integer.valueOf(NUMPOS));
        
    }
    
    /** Get Position Number.
    @return Position Number */
    public int getNUMPOS() 
    {
        return get_ValueAsInt("NUMPOS");
        
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
    
    /** Set STAREF.
    @param STAREF Status of the currently running check */
    public void setSTAREF (String STAREF)
    {
        set_Value ("STAREF", STAREF);
        
    }
    
    /** Get STAREF.
    @return Status of the currently running check */
    public String getSTAREF() 
    {
        return (String)get_Value("STAREF");
        
    }
    
    /** Set Characteristic 1 Type.
    @param TIPCAR1 KIND OF ATTRIBUTE */
    public void setTIPCAR1 (String TIPCAR1)
    {
        set_Value ("TIPCAR1", TIPCAR1);
        
    }
    
    /** Get Characteristic 1 Type.
    @return KIND OF ATTRIBUTE */
    public String getTIPCAR1() 
    {
        return (String)get_Value("TIPCAR1");
        
    }
    
    /** Set Characteristic 2 Type.
    @param TIPCAR2 KIND OF ATTRIBUTTE */
    public void setTIPCAR2 (String TIPCAR2)
    {
        set_Value ("TIPCAR2", TIPCAR2);
        
    }
    
    /** Get Characteristic 2 Type.
    @return KIND OF ATTRIBUTTE */
    public String getTIPCAR2() 
    {
        return (String)get_Value("TIPCAR2");
        
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
    
    /** Set Elimination User.
    @param USRELIM Elimination User */
    public void setUSRELIM (String USRELIM)
    {
        set_Value ("USRELIM", USRELIM);
        
    }
    
    /** Get Elimination User.
    @return Elimination User */
    public String getUSRELIM() 
    {
        return (String)get_Value("USRELIM");
        
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
