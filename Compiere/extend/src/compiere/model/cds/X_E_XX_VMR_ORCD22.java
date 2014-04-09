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
/** Generated Model for E_XX_VMR_ORCD22
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_ORCD22 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_ORCD22_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_ORCD22 (Ctx ctx, int E_XX_VMR_ORCD22_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_ORCD22_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_ORCD22_ID == 0)
        {
            setE_XX_VMR_ORCD22_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_ORCD22 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27573198597789L;
    /** Last Updated Timestamp 2010-11-29 18:58:01.0 */
    public static final long updatedMS = 1291073281000L;
    /** AD_Table_ID=1000346 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_ORCD22");
        
    }
    ;
    
    /** TableName=E_XX_VMR_ORCD22 */
    public static final String Table_Name="E_XX_VMR_ORCD22";
    
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
    
    /** Set E_XX_VMR_ORCD22_ID.
    @param E_XX_VMR_ORCD22_ID E_XX_VMR_ORCD22_ID */
    public void setE_XX_VMR_ORCD22_ID (int E_XX_VMR_ORCD22_ID)
    {
        if (E_XX_VMR_ORCD22_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_ORCD22_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_ORCD22_ID", Integer.valueOf(E_XX_VMR_ORCD22_ID));
        
    }
    
    /** Get E_XX_VMR_ORCD22_ID.
    @return E_XX_VMR_ORCD22_ID */
    public int getE_XX_VMR_ORCD22_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_ORCD22_ID");
        
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
