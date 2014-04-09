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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_ISLRRetention
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_ISLRRetention extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_ISLRRetention_ID id
    @param trx transaction
    */
    public X_XX_VCN_ISLRRetention (Ctx ctx, int XX_VCN_ISLRRetention_ID, Trx trx)
    {
        super (ctx, XX_VCN_ISLRRetention_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_ISLRRetention_ID == 0)
        {
            setXX_ISLRUT (false);
            setXX_Subtrahend (false);
            setXX_VCN_ISLRRetention_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_ISLRRetention (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27607904866789L;
    /** Last Updated Timestamp 2012-01-05 11:35:50.0 */
    public static final long updatedMS = 1325779550000L;
    /** AD_Table_ID=1002661 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_ISLRRetention");
        
    }
    ;
    
    /** TableName=XX_VCN_ISLRRetention */
    public static final String Table_Name="XX_VCN_ISLRRetention";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_ISLRUT.
    @param XX_ISLRUT XX_ISLRUT */
    public void setXX_ISLRUT (boolean XX_ISLRUT)
    {
        set_Value ("XX_ISLRUT", Boolean.valueOf(XX_ISLRUT));
        
    }
    
    /** Get XX_ISLRUT.
    @return XX_ISLRUT */
    public boolean isXX_ISLRUT() 
    {
        return get_ValueAsBoolean("XX_ISLRUT");
        
    }
    
    /** Set Minimum Payment.
    @param XX_MinimumPayment Minimum Payment */
    public void setXX_MinimumPayment (java.math.BigDecimal XX_MinimumPayment)
    {
        set_Value ("XX_MinimumPayment", XX_MinimumPayment);
        
    }
    
    /** Get Minimum Payment.
    @return Minimum Payment */
    public java.math.BigDecimal getXX_MinimumPayment() 
    {
        return get_ValueAsBigDecimal("XX_MinimumPayment");
        
    }
    
    /** Set Percent Basis.
    @param XX_PercentBasis Percent Basis */
    public void setXX_PercentBasis (java.math.BigDecimal XX_PercentBasis)
    {
        set_Value ("XX_PercentBasis", XX_PercentBasis);
        
    }
    
    /** Get Percent Basis.
    @return Percent Basis */
    public java.math.BigDecimal getXX_PercentBasis() 
    {
        return get_ValueAsBigDecimal("XX_PercentBasis");
        
    }
    
    /** Set Percent Of Retention.
    @param XX_PercentOfRetention Percent Of Retention */
    public void setXX_PercentOfRetention (java.math.BigDecimal XX_PercentOfRetention)
    {
        set_Value ("XX_PercentOfRetention", XX_PercentOfRetention);
        
    }
    
    /** Get Percent Of Retention.
    @return Percent Of Retention */
    public java.math.BigDecimal getXX_PercentOfRetention() 
    {
        return get_ValueAsBigDecimal("XX_PercentOfRetention");
        
    }
    
    /** Set SENIAT Code.
    @param XX_SENIATCode SENIAT Code */
    public void setXX_SENIATCode (String XX_SENIATCode)
    {
        set_Value ("XX_SENIATCode", XX_SENIATCode);
        
    }
    
    /** Get SENIAT Code.
    @return SENIAT Code */
    public String getXX_SENIATCode() 
    {
        return (String)get_Value("XX_SENIATCode");
        
    }
    
    /** Set Subtrahend.
    @param XX_Subtrahend Subtrahend */
    public void setXX_Subtrahend (boolean XX_Subtrahend)
    {
        set_Value ("XX_Subtrahend", Boolean.valueOf(XX_Subtrahend));
        
    }
    
    /** Get Subtrahend.
    @return Subtrahend */
    public boolean isXX_Subtrahend() 
    {
        return get_ValueAsBoolean("XX_Subtrahend");
        
    }
    
    /** PJD - PERSONA JURIDICA = PJD */
    public static final String XX_TYPEPERSONISLR_PJD_PERSONAJURIDICA = X_Ref_XX_Ref_TypePersonISLR.PJ_D__PERSONAJURIDICA.getValue();
    /** PJND - COMPAÑIAS EXTRANJERAS = PJND */
    public static final String XX_TYPEPERSONISLR_PJND_COMPAÑIASEXTRANJERAS = X_Ref_XX_Ref_TypePersonISLR.PJN_D__COMPAÑIASEXTRANJERAS.getValue();
    /** PNNR - PASAPORTE CON VISA DE NEGOCIOS = PNNR */
    public static final String XX_TYPEPERSONISLR_PNNR_PASAPORTECONVISADENEGOCIOS = X_Ref_XX_Ref_TypePersonISLR.PNN_R__PASAPORTECONVISADENEGOCIOS.getValue();
    /** PNR - PERSONA NATURAL = PNR */
    public static final String XX_TYPEPERSONISLR_PNR_PERSONANATURAL = X_Ref_XX_Ref_TypePersonISLR.PN_R__PERSONANATURAL.getValue();
    /** Set Type of Person ISLR.
    @param XX_TypePersonISLR Type of Person ISLR */
    public void setXX_TypePersonISLR (String XX_TypePersonISLR)
    {
        if (!X_Ref_XX_Ref_TypePersonISLR.isValid(XX_TypePersonISLR))
        throw new IllegalArgumentException ("XX_TypePersonISLR Invalid value - " + XX_TypePersonISLR + " - Reference_ID=1001867 - PJD - PJND - PNNR - PNR");
        set_Value ("XX_TypePersonISLR", XX_TypePersonISLR);
        
    }
    
    /** Get Type of Person ISLR.
    @return Type of Person ISLR */
    public String getXX_TypePersonISLR() 
    {
        return (String)get_Value("XX_TypePersonISLR");
        
    }
    
    /** Set ISRL Retention.
    @param XX_VCN_ISLRRetention_ID ISRL Retention */
    public void setXX_VCN_ISLRRetention_ID (int XX_VCN_ISLRRetention_ID)
    {
        if (XX_VCN_ISLRRetention_ID < 1) throw new IllegalArgumentException ("XX_VCN_ISLRRetention_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_ISLRRetention_ID", Integer.valueOf(XX_VCN_ISLRRetention_ID));
        
    }
    
    /** Get ISRL Retention.
    @return ISRL Retention */
    public int getXX_VCN_ISLRRetention_ID() 
    {
        return get_ValueAsInt("XX_VCN_ISLRRetention_ID");
        
    }
    
    
}
