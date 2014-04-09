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
/** Generated Model for E_XX_VMR_NRAD01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_NRAD01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_NRAD01_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_NRAD01 (Ctx ctx, int E_XX_VMR_NRAD01_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_NRAD01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_NRAD01_ID == 0)
        {
            setE_XX_VMR_NRAD01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_NRAD01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198835789L;
    /** Last Updated Timestamp 2010-11-29 19:01:59.0 */
    public static final long updatedMS = 1291073519000L;
    /** AD_Table_ID=1000366 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_NRAD01");
        
    }
    ;
    
    /** TableName=E_XX_VMR_NRAD01 */
    public static final String Table_Name="E_XX_VMR_NRAD01";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Package.
    @param BULTO Package at CB98 */
    public void setBULTO (int BULTO)
    {
        set_Value ("BULTO", Integer.valueOf(BULTO));
        
    }
    
    /** Get Package.
    @return Package at CB98 */
    public int getBULTO() 
    {
        return get_ValueAsInt("BULTO");
        
    }
    
    /** Set CANPRO.
    @param CANPRO PRODUCT AMOUNT */
    public void setCANPRO (int CANPRO)
    {
        set_Value ("CANPRO", Integer.valueOf(CANPRO));
        
    }
    
    /** Get CANPRO.
    @return PRODUCT AMOUNT */
    public int getCANPRO() 
    {
        return get_ValueAsInt("CANPRO");
        
    }
    
    /** Set Gift Quantity.
    @param CNTOBS Gift Quantity at CB98 */
    public void setCNTOBS (int CNTOBS)
    {
        set_Value ("CNTOBS", Integer.valueOf(CNTOBS));
        
    }
    
    /** Get Gift Quantity.
    @return Gift Quantity at CB98 */
    public int getCNTOBS() 
    {
        return get_ValueAsInt("CNTOBS");
        
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
    
    /** Set Operation Code.
    @param CODOPE Operation Code at CB98 */
    public void setCODOPE (int CODOPE)
    {
        set_Value ("CODOPE", Integer.valueOf(CODOPE));
        
    }
    
    /** Get Operation Code.
    @return Operation Code at CB98 */
    public int getCODOPE() 
    {
        return get_ValueAsInt("CODOPE");
        
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
    
    /** Set Price Consecutive.
    @param COMPRE Price Consecutive at CB98 */
    public void setCOMPRE (int COMPRE)
    {
        set_Value ("COMPRE", Integer.valueOf(COMPRE));
        
    }
    
    /** Get Price Consecutive.
    @return Price Consecutive at CB98 */
    public int getCOMPRE() 
    {
        return get_ValueAsInt("COMPRE");
        
    }
    
    /** Set E_XX_VMR_NRAD01_ID.
    @param E_XX_VMR_NRAD01_ID E_XX_VMR_NRAD01_ID */
    public void setE_XX_VMR_NRAD01_ID (int E_XX_VMR_NRAD01_ID)
    {
        if (E_XX_VMR_NRAD01_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_NRAD01_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_NRAD01_ID", Integer.valueOf(E_XX_VMR_NRAD01_ID));
        
    }
    
    /** Get E_XX_VMR_NRAD01_ID.
    @return E_XX_VMR_NRAD01_ID */
    public int getE_XX_VMR_NRAD01_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_NRAD01_ID");
        
    }
    
    /** Set Total Amount Invoice.
    @param MCOSFA Total Amount Invoice for a product at CB98 */
    public void setMCOSFA (java.math.BigDecimal MCOSFA)
    {
        set_Value ("MCOSFA", MCOSFA);
        
    }
    
    /** Get Total Amount Invoice.
    @return Total Amount Invoice for a product at CB98 */
    public java.math.BigDecimal getMCOSFA() 
    {
        return get_ValueAsBigDecimal("MCOSFA");
        
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
    
    /** Set Amount Discount at Invoice.
    @param MDESSF Amount Discount at Invoice /CB98 */
    public void setMDESSF (java.math.BigDecimal MDESSF)
    {
        set_Value ("MDESSF", MDESSF);
        
    }
    
    /** Get Amount Discount at Invoice.
    @return Amount Discount at Invoice /CB98 */
    public java.math.BigDecimal getMDESSF() 
    {
        return get_ValueAsBigDecimal("MDESSF");
        
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
    
    /** Set Transaction Number.
    @param NUMTRA Transaction Number at CB98 */
    public void setNUMTRA (int NUMTRA)
    {
        set_Value ("NUMTRA", Integer.valueOf(NUMTRA));
        
    }
    
    /** Get Transaction Number.
    @return Transaction Number at CB98 */
    public int getNUMTRA() 
    {
        return get_ValueAsInt("NUMTRA");
        
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
    
    /** Set Section.
    @param SECCIO Section at CB98 */
    public void setSECCIO (int SECCIO)
    {
        set_Value ("SECCIO", Integer.valueOf(SECCIO));
        
    }
    
    /** Get Section.
    @return Section at CB98 */
    public int getSECCIO() 
    {
        return get_ValueAsInt("SECCIO");
        
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
