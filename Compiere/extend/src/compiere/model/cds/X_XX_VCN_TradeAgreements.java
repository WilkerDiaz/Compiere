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
/** Generated Model for XX_VCN_TradeAgreements
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_TradeAgreements extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_TradeAgreements_ID id
    @param trx transaction
    */
    public X_XX_VCN_TradeAgreements (Ctx ctx, int XX_VCN_TradeAgreements_ID, Trx trx)
    {
        super (ctx, XX_VCN_TradeAgreements_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_TradeAgreements_ID == 0)
        {
            setC_Currency_ID (0);
            setValue (null);
            setXX_AGREEMENTCODE (Env.ZERO);
            setXX_AgreementType (null);
            setXX_CENTRADISCDELIAMOUNT (Env.ZERO);
            setXX_CENTRADISCDELIPERCEN (Env.ZERO);
            setXX_CREDITSTOREOPENING (Env.ZERO);
            setXX_CURRENCYCODE (Env.ZERO);
            setXX_DATEEFFECTIVEFROM (new Timestamp(System.currentTimeMillis()));
            setXX_DATEVALIDEXCLFROM (new Timestamp(System.currentTimeMillis()));
            setXX_DATEVALIDEXCLTO (new Timestamp(System.currentTimeMillis()));
            setXX_DISCAFTERSALEAMOUNT (Env.ZERO);
            setXX_DISCAFTERSALEPERCEN (Env.ZERO);
            setXX_DiscOpenStorAmount (Env.ZERO);
            setXX_DiscOpenStorPercent (Env.ZERO);
            setXX_DISCOUNTGOSAMOUNT (Env.ZERO);
            setXX_DISCOUNTGOSPERCENTAGE (Env.ZERO);
            setXX_DISCRECOGDECLAMOUNT (Env.ZERO);
            setXX_DISCRECOGDECLPERCEN (Env.ZERO);
            setXX_EFFECTIVEDATETO (new Timestamp(System.currentTimeMillis()));
            setXX_FIRSTVARVOLDISCOFROM (Env.ZERO);
            setXX_FIRSTVARVOLDISCOPERCEN (Env.ZERO);
            setXX_FIRSTVARVOLDISCOTO (Env.ZERO);
            setXX_FIXVOLDISCOAMOUNT (Env.ZERO);
            setXX_FIXVOLDISCOPERCEN (Env.ZERO);
            setXX_InitDate (new Timestamp(System.currentTimeMillis()));
            setXX_PARTICIBECOBROCHAMOUNT (Env.ZERO);
            setXX_PARTICIBECOBROCHPERCEN (Env.ZERO);
            setXX_PAYMENTRULE (Env.ZERO);
            setXX_PRODTRADEAGREEMENT (Env.ZERO);
            setXX_PubAmount (Env.ZERO);
            setXX_PubPercent (Env.ZERO);
            setXX_SECONDVARVOLDISCOFROM (Env.ZERO);
            setXX_SECONDVARVOLDISCOPERCEN (Env.ZERO);
            setXX_SECONDVARVOLDISCOTO (Env.ZERO);
            setXX_STATEREGISTER (Env.ZERO);
            setXX_THIRDVARVOLDISCOFROM (Env.ZERO);
            setXX_THIRDVARVOLDISCOPERCEN (Env.ZERO);
            setXX_THIRDVARVOLDISCOTO (Env.ZERO);
            setXX_TYPECREDITSTOREOPENING (Env.ZERO);
            setXX_TypeTax_ID (0);
            setXX_VCN_TRADEAGREEMENTS_ID (0);
            setXX_VMR_Category_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_TradeAgreements (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27619490959789L;
    /** Last Updated Timestamp 2012-05-18 13:57:23.0 */
    public static final long updatedMS = 1337365643000L;
    /** AD_Table_ID=1000104 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_TradeAgreements");
        
    }
    ;
    
    /** TableName=XX_VCN_TradeAgreements */
    public static final String Table_Name="XX_VCN_TradeAgreements";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set DOCUMENTNOORDER.
    @param DOCUMENTNOORDER DOCUMENTNOORDER */
    public void setDOCUMENTNOORDER (int DOCUMENTNOORDER)
    {
        set_Value ("DOCUMENTNOORDER", Integer.valueOf(DOCUMENTNOORDER));
        
    }
    
    /** Get DOCUMENTNOORDER.
    @return DOCUMENTNOORDER */
    public int getDOCUMENTNOORDER() 
    {
        return get_ValueAsInt("DOCUMENTNOORDER");
        
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
    
    /** Set XX_AGREEMENTCODE.
    @param XX_AGREEMENTCODE XX_AGREEMENTCODE */
    public void setXX_AGREEMENTCODE (java.math.BigDecimal XX_AGREEMENTCODE)
    {
        if (XX_AGREEMENTCODE == null) throw new IllegalArgumentException ("XX_AGREEMENTCODE is mandatory.");
        set_Value ("XX_AGREEMENTCODE", XX_AGREEMENTCODE);
        
    }
    
    /** Get XX_AGREEMENTCODE.
    @return XX_AGREEMENTCODE */
    public java.math.BigDecimal getXX_AGREEMENTCODE() 
    {
        return get_ValueAsBigDecimal("XX_AGREEMENTCODE");
        
    }
    
    /** Acuerdo Comercial = AC */
    public static final String XX_AGREEMENTTYPE_AcuerdoComercial = X_Ref_XX_REF_AgreementType.ACUERDO_COMERCIAL.getValue();
    /** Carta Compromiso = CC */
    public static final String XX_AGREEMENTTYPE_CartaCompromiso = X_Ref_XX_REF_AgreementType.CARTA_COMPROMISO.getValue();
    /** Set XX_AgreementType.
    @param XX_AgreementType Tipo de acuerdo */
    public void setXX_AgreementType (String XX_AgreementType)
    {
        if (XX_AgreementType == null) throw new IllegalArgumentException ("XX_AgreementType is mandatory");
        if (!X_Ref_XX_REF_AgreementType.isValid(XX_AgreementType))
        throw new IllegalArgumentException ("XX_AgreementType Invalid value - " + XX_AgreementType + " - Reference_ID=1001649 - AC - CC");
        set_Value ("XX_AgreementType", XX_AgreementType);
        
    }
    
    /** Get XX_AgreementType.
    @return Tipo de acuerdo */
    public String getXX_AgreementType() 
    {
        return (String)get_Value("XX_AgreementType");
        
    }
    
    /** Set XX_APPLYFIXEDDISCOUNT.
    @param XX_APPLYFIXEDDISCOUNT XX_APPLYFIXEDDISCOUNT */
    public void setXX_APPLYFIXEDDISCOUNT (boolean XX_APPLYFIXEDDISCOUNT)
    {
        set_Value ("XX_APPLYFIXEDDISCOUNT", Boolean.valueOf(XX_APPLYFIXEDDISCOUNT));
        
    }
    
    /** Get XX_APPLYFIXEDDISCOUNT.
    @return XX_APPLYFIXEDDISCOUNT */
    public boolean isXX_APPLYFIXEDDISCOUNT() 
    {
        return get_ValueAsBoolean("XX_APPLYFIXEDDISCOUNT");
        
    }
    
    /** Set XX_BECOREPRESENTATIVE.
    @param XX_BECOREPRESENTATIVE XX_BECOREPRESENTATIVE */
    public void setXX_BECOREPRESENTATIVE (String XX_BECOREPRESENTATIVE)
    {
        set_Value ("XX_BECOREPRESENTATIVE", XX_BECOREPRESENTATIVE);
        
    }
    
    /** Get XX_BECOREPRESENTATIVE.
    @return XX_BECOREPRESENTATIVE */
    public String getXX_BECOREPRESENTATIVE() 
    {
        return (String)get_Value("XX_BECOREPRESENTATIVE");
        
    }
    
    /** Set XX_CEDULABECOREPRESE.
    @param XX_CEDULABECOREPRESE XX_CEDULABECOREPRESE */
    public void setXX_CEDULABECOREPRESE (String XX_CEDULABECOREPRESE)
    {
        set_Value ("XX_CEDULABECOREPRESE", XX_CEDULABECOREPRESE);
        
    }
    
    /** Get XX_CEDULABECOREPRESE.
    @return XX_CEDULABECOREPRESE */
    public String getXX_CEDULABECOREPRESE() 
    {
        return (String)get_Value("XX_CEDULABECOREPRESE");
        
    }
    
    /** Set XX_CEDULAVENDORREPRESE.
    @param XX_CEDULAVENDORREPRESE XX_CEDULAVENDORREPRESE */
    public void setXX_CEDULAVENDORREPRESE (String XX_CEDULAVENDORREPRESE)
    {
        set_Value ("XX_CEDULAVENDORREPRESE", XX_CEDULAVENDORREPRESE);
        
    }
    
    /** Get XX_CEDULAVENDORREPRESE.
    @return XX_CEDULAVENDORREPRESE */
    public String getXX_CEDULAVENDORREPRESE() 
    {
        return (String)get_Value("XX_CEDULAVENDORREPRESE");
        
    }
    
    /** Set XX_CENTRADISCDELIAMOUNT.
    @param XX_CENTRADISCDELIAMOUNT XX_CENTRADISCDELIAMOUNT */
    public void setXX_CENTRADISCDELIAMOUNT (java.math.BigDecimal XX_CENTRADISCDELIAMOUNT)
    {
        if (XX_CENTRADISCDELIAMOUNT == null) throw new IllegalArgumentException ("XX_CENTRADISCDELIAMOUNT is mandatory.");
        set_Value ("XX_CENTRADISCDELIAMOUNT", XX_CENTRADISCDELIAMOUNT);
        
    }
    
    /** Get XX_CENTRADISCDELIAMOUNT.
    @return XX_CENTRADISCDELIAMOUNT */
    public java.math.BigDecimal getXX_CENTRADISCDELIAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_CENTRADISCDELIAMOUNT");
        
    }
    
    /** Set XX_CENTRADISCDELIPERCEN.
    @param XX_CENTRADISCDELIPERCEN XX_CENTRADISCDELIPERCEN */
    public void setXX_CENTRADISCDELIPERCEN (java.math.BigDecimal XX_CENTRADISCDELIPERCEN)
    {
        if (XX_CENTRADISCDELIPERCEN == null) throw new IllegalArgumentException ("XX_CENTRADISCDELIPERCEN is mandatory.");
        set_Value ("XX_CENTRADISCDELIPERCEN", XX_CENTRADISCDELIPERCEN);
        
    }
    
    /** Get XX_CENTRADISCDELIPERCEN.
    @return XX_CENTRADISCDELIPERCEN */
    public java.math.BigDecimal getXX_CENTRADISCDELIPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_CENTRADISCDELIPERCEN");
        
    }
    
    /** Set XX_CheckOwnBrand.
    @param XX_CheckOwnBrand XX_CheckOwnBrand */
    public void setXX_CheckOwnBrand (boolean XX_CheckOwnBrand)
    {
        set_Value ("XX_CheckOwnBrand", Boolean.valueOf(XX_CheckOwnBrand));
        
    }
    
    /** Get XX_CheckOwnBrand.
    @return XX_CheckOwnBrand */
    public boolean isXX_CheckOwnBrand() 
    {
        return get_ValueAsBoolean("XX_CheckOwnBrand");
        
    }
    
    /** Set XX_CheckRepForBeco.
    @param XX_CheckRepForBeco Representación & Distribución exclusiva para BECO */
    public void setXX_CheckRepForBeco (boolean XX_CheckRepForBeco)
    {
        set_Value ("XX_CheckRepForBeco", Boolean.valueOf(XX_CheckRepForBeco));
        
    }
    
    /** Get XX_CheckRepForBeco.
    @return Representación & Distribución exclusiva para BECO */
    public boolean isXX_CheckRepForBeco() 
    {
        return get_ValueAsBoolean("XX_CheckRepForBeco");
        
    }
    
    /** Set Concept.
    @param XX_Concept Concept */
    public void setXX_Concept (String XX_Concept)
    {
        set_Value ("XX_Concept", XX_Concept);
        
    }
    
    /** Get Concept.
    @return Concept */
    public String getXX_Concept() 
    {
        return (String)get_Value("XX_Concept");
        
    }
    
    /** Set Created By.
    @param XX_CreatedBy Created By */
    public void setXX_CreatedBy (String XX_CreatedBy)
    {
        set_Value ("XX_CreatedBy", XX_CreatedBy);
        
    }
    
    /** Get Created By.
    @return Created By */
    public String getXX_CreatedBy() 
    {
        return (String)get_Value("XX_CreatedBy");
        
    }
    
    /** Set XX_CREDITSTOREOPENING.
    @param XX_CREDITSTOREOPENING XX_CREDITSTOREOPENING */
    public void setXX_CREDITSTOREOPENING (java.math.BigDecimal XX_CREDITSTOREOPENING)
    {
        if (XX_CREDITSTOREOPENING == null) throw new IllegalArgumentException ("XX_CREDITSTOREOPENING is mandatory.");
        set_Value ("XX_CREDITSTOREOPENING", XX_CREDITSTOREOPENING);
        
    }
    
    /** Get XX_CREDITSTOREOPENING.
    @return XX_CREDITSTOREOPENING */
    public java.math.BigDecimal getXX_CREDITSTOREOPENING() 
    {
        return get_ValueAsBigDecimal("XX_CREDITSTOREOPENING");
        
    }
    
    /** Set XX_CURRENCYCODE.
    @param XX_CURRENCYCODE XX_CURRENCYCODE */
    public void setXX_CURRENCYCODE (java.math.BigDecimal XX_CURRENCYCODE)
    {
        if (XX_CURRENCYCODE == null) throw new IllegalArgumentException ("XX_CURRENCYCODE is mandatory.");
        set_Value ("XX_CURRENCYCODE", XX_CURRENCYCODE);
        
    }
    
    /** Get XX_CURRENCYCODE.
    @return XX_CURRENCYCODE */
    public java.math.BigDecimal getXX_CURRENCYCODE() 
    {
        return get_ValueAsBigDecimal("XX_CURRENCYCODE");
        
    }
    
    /** Set XX_DATEEFFECTIVEFROM.
    @param XX_DATEEFFECTIVEFROM XX_DATEEFFECTIVEFROM */
    public void setXX_DATEEFFECTIVEFROM (Timestamp XX_DATEEFFECTIVEFROM)
    {
        if (XX_DATEEFFECTIVEFROM == null) throw new IllegalArgumentException ("XX_DATEEFFECTIVEFROM is mandatory.");
        set_ValueNoCheck ("XX_DATEEFFECTIVEFROM", XX_DATEEFFECTIVEFROM);
        
    }
    
    /** Get XX_DATEEFFECTIVEFROM.
    @return XX_DATEEFFECTIVEFROM */
    public Timestamp getXX_DATEEFFECTIVEFROM() 
    {
        return (Timestamp)get_Value("XX_DATEEFFECTIVEFROM");
        
    }
    
    /** Set XX_DATEVALIDEXCLFROM.
    @param XX_DATEVALIDEXCLFROM XX_DATEVALIDEXCLFROM */
    public void setXX_DATEVALIDEXCLFROM (Timestamp XX_DATEVALIDEXCLFROM)
    {
        if (XX_DATEVALIDEXCLFROM == null) throw new IllegalArgumentException ("XX_DATEVALIDEXCLFROM is mandatory.");
        set_ValueNoCheck ("XX_DATEVALIDEXCLFROM", XX_DATEVALIDEXCLFROM);
        
    }
    
    /** Get XX_DATEVALIDEXCLFROM.
    @return XX_DATEVALIDEXCLFROM */
    public Timestamp getXX_DATEVALIDEXCLFROM() 
    {
        return (Timestamp)get_Value("XX_DATEVALIDEXCLFROM");
        
    }
    
    /** Set XX_DATEVALIDEXCLTO.
    @param XX_DATEVALIDEXCLTO XX_DATEVALIDEXCLTO */
    public void setXX_DATEVALIDEXCLTO (Timestamp XX_DATEVALIDEXCLTO)
    {
        if (XX_DATEVALIDEXCLTO == null) throw new IllegalArgumentException ("XX_DATEVALIDEXCLTO is mandatory.");
        set_ValueNoCheck ("XX_DATEVALIDEXCLTO", XX_DATEVALIDEXCLTO);
        
    }
    
    /** Get XX_DATEVALIDEXCLTO.
    @return XX_DATEVALIDEXCLTO */
    public Timestamp getXX_DATEVALIDEXCLTO() 
    {
        return (Timestamp)get_Value("XX_DATEVALIDEXCLTO");
        
    }
    
    /** Set XX_DISCAFTERSALEAMOUNT.
    @param XX_DISCAFTERSALEAMOUNT XX_DISCAFTERSALEAMOUNT */
    public void setXX_DISCAFTERSALEAMOUNT (java.math.BigDecimal XX_DISCAFTERSALEAMOUNT)
    {
        if (XX_DISCAFTERSALEAMOUNT == null) throw new IllegalArgumentException ("XX_DISCAFTERSALEAMOUNT is mandatory.");
        set_Value ("XX_DISCAFTERSALEAMOUNT", XX_DISCAFTERSALEAMOUNT);
        
    }
    
    /** Get XX_DISCAFTERSALEAMOUNT.
    @return XX_DISCAFTERSALEAMOUNT */
    public java.math.BigDecimal getXX_DISCAFTERSALEAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_DISCAFTERSALEAMOUNT");
        
    }
    
    /** Set XX_DISCAFTERSALEPERCEN.
    @param XX_DISCAFTERSALEPERCEN XX_DISCAFTERSALEPERCEN */
    public void setXX_DISCAFTERSALEPERCEN (java.math.BigDecimal XX_DISCAFTERSALEPERCEN)
    {
        if (XX_DISCAFTERSALEPERCEN == null) throw new IllegalArgumentException ("XX_DISCAFTERSALEPERCEN is mandatory.");
        set_Value ("XX_DISCAFTERSALEPERCEN", XX_DISCAFTERSALEPERCEN);
        
    }
    
    /** Get XX_DISCAFTERSALEPERCEN.
    @return XX_DISCAFTERSALEPERCEN */
    public java.math.BigDecimal getXX_DISCAFTERSALEPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_DISCAFTERSALEPERCEN");
        
    }
    
    /** Set XX_DiscOpenStorAmount.
    @param XX_DiscOpenStorAmount Descuento por Inauguración de Tiendas (Monto) */
    public void setXX_DiscOpenStorAmount (java.math.BigDecimal XX_DiscOpenStorAmount)
    {
        if (XX_DiscOpenStorAmount == null) throw new IllegalArgumentException ("XX_DiscOpenStorAmount is mandatory.");
        set_Value ("XX_DiscOpenStorAmount", XX_DiscOpenStorAmount);
        
    }
    
    /** Get XX_DiscOpenStorAmount.
    @return Descuento por Inauguración de Tiendas (Monto) */
    public java.math.BigDecimal getXX_DiscOpenStorAmount() 
    {
        return get_ValueAsBigDecimal("XX_DiscOpenStorAmount");
        
    }
    
    /** Set XX_DiscOpenStorPercent.
    @param XX_DiscOpenStorPercent Descuento por Inauguración de Tienda (Porcentaje) */
    public void setXX_DiscOpenStorPercent (java.math.BigDecimal XX_DiscOpenStorPercent)
    {
        if (XX_DiscOpenStorPercent == null) throw new IllegalArgumentException ("XX_DiscOpenStorPercent is mandatory.");
        set_Value ("XX_DiscOpenStorPercent", XX_DiscOpenStorPercent);
        
    }
    
    /** Get XX_DiscOpenStorPercent.
    @return Descuento por Inauguración de Tienda (Porcentaje) */
    public java.math.BigDecimal getXX_DiscOpenStorPercent() 
    {
        return get_ValueAsBigDecimal("XX_DiscOpenStorPercent");
        
    }
    
    /** Set XX_DISCOUNTGOSAMOUNT.
    @param XX_DISCOUNTGOSAMOUNT XX_DISCOUNTGOSAMOUNT */
    public void setXX_DISCOUNTGOSAMOUNT (java.math.BigDecimal XX_DISCOUNTGOSAMOUNT)
    {
        if (XX_DISCOUNTGOSAMOUNT == null) throw new IllegalArgumentException ("XX_DISCOUNTGOSAMOUNT is mandatory.");
        set_Value ("XX_DISCOUNTGOSAMOUNT", XX_DISCOUNTGOSAMOUNT);
        
    }
    
    /** Get XX_DISCOUNTGOSAMOUNT.
    @return XX_DISCOUNTGOSAMOUNT */
    public java.math.BigDecimal getXX_DISCOUNTGOSAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_DISCOUNTGOSAMOUNT");
        
    }
    
    /** Set XX_DISCOUNTGOSPERCENTAGE.
    @param XX_DISCOUNTGOSPERCENTAGE XX_DISCOUNTGOSPERCENTAGE */
    public void setXX_DISCOUNTGOSPERCENTAGE (java.math.BigDecimal XX_DISCOUNTGOSPERCENTAGE)
    {
        if (XX_DISCOUNTGOSPERCENTAGE == null) throw new IllegalArgumentException ("XX_DISCOUNTGOSPERCENTAGE is mandatory.");
        set_Value ("XX_DISCOUNTGOSPERCENTAGE", XX_DISCOUNTGOSPERCENTAGE);
        
    }
    
    /** Get XX_DISCOUNTGOSPERCENTAGE.
    @return XX_DISCOUNTGOSPERCENTAGE */
    public java.math.BigDecimal getXX_DISCOUNTGOSPERCENTAGE() 
    {
        return get_ValueAsBigDecimal("XX_DISCOUNTGOSPERCENTAGE");
        
    }
    
    /** Set XX_DISCRECOGDECLAMOUNT.
    @param XX_DISCRECOGDECLAMOUNT XX_DISCRECOGDECLAMOUNT */
    public void setXX_DISCRECOGDECLAMOUNT (java.math.BigDecimal XX_DISCRECOGDECLAMOUNT)
    {
        if (XX_DISCRECOGDECLAMOUNT == null) throw new IllegalArgumentException ("XX_DISCRECOGDECLAMOUNT is mandatory.");
        set_Value ("XX_DISCRECOGDECLAMOUNT", XX_DISCRECOGDECLAMOUNT);
        
    }
    
    /** Get XX_DISCRECOGDECLAMOUNT.
    @return XX_DISCRECOGDECLAMOUNT */
    public java.math.BigDecimal getXX_DISCRECOGDECLAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_DISCRECOGDECLAMOUNT");
        
    }
    
    /** Set XX_DISCRECOGDECLPERCEN.
    @param XX_DISCRECOGDECLPERCEN XX_DISCRECOGDECLPERCEN */
    public void setXX_DISCRECOGDECLPERCEN (java.math.BigDecimal XX_DISCRECOGDECLPERCEN)
    {
        if (XX_DISCRECOGDECLPERCEN == null) throw new IllegalArgumentException ("XX_DISCRECOGDECLPERCEN is mandatory.");
        set_Value ("XX_DISCRECOGDECLPERCEN", XX_DISCRECOGDECLPERCEN);
        
    }
    
    /** Get XX_DISCRECOGDECLPERCEN.
    @return XX_DISCRECOGDECLPERCEN */
    public java.math.BigDecimal getXX_DISCRECOGDECLPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_DISCRECOGDECLPERCEN");
        
    }
    
    /** Set XX_EFFECTIVEDATETO.
    @param XX_EFFECTIVEDATETO XX_EFFECTIVEDATETO */
    public void setXX_EFFECTIVEDATETO (Timestamp XX_EFFECTIVEDATETO)
    {
        if (XX_EFFECTIVEDATETO == null) throw new IllegalArgumentException ("XX_EFFECTIVEDATETO is mandatory.");
        set_ValueNoCheck ("XX_EFFECTIVEDATETO", XX_EFFECTIVEDATETO);
        
    }
    
    /** Get XX_EFFECTIVEDATETO.
    @return XX_EFFECTIVEDATETO */
    public Timestamp getXX_EFFECTIVEDATETO() 
    {
        return (Timestamp)get_Value("XX_EFFECTIVEDATETO");
        
    }
    
    /** Set XX_Expire.
    @param XX_Expire XX_Expire */
    public void setXX_Expire (String XX_Expire)
    {
        set_Value ("XX_Expire", XX_Expire);
        
    }
    
    /** Get XX_Expire.
    @return XX_Expire */
    public String getXX_Expire() 
    {
        return (String)get_Value("XX_Expire");
        
    }
    
    /** Set XX_FIRSTVARVOLDISCOFROM.
    @param XX_FIRSTVARVOLDISCOFROM XX_FIRSTVARVOLDISCOFROM */
    public void setXX_FIRSTVARVOLDISCOFROM (java.math.BigDecimal XX_FIRSTVARVOLDISCOFROM)
    {
        if (XX_FIRSTVARVOLDISCOFROM == null) throw new IllegalArgumentException ("XX_FIRSTVARVOLDISCOFROM is mandatory.");
        set_Value ("XX_FIRSTVARVOLDISCOFROM", XX_FIRSTVARVOLDISCOFROM);
        
    }
    
    /** Get XX_FIRSTVARVOLDISCOFROM.
    @return XX_FIRSTVARVOLDISCOFROM */
    public java.math.BigDecimal getXX_FIRSTVARVOLDISCOFROM() 
    {
        return get_ValueAsBigDecimal("XX_FIRSTVARVOLDISCOFROM");
        
    }
    
    /** Set XX_FIRSTVARVOLDISCOPERCEN.
    @param XX_FIRSTVARVOLDISCOPERCEN XX_FIRSTVARVOLDISCOPERCEN */
    public void setXX_FIRSTVARVOLDISCOPERCEN (java.math.BigDecimal XX_FIRSTVARVOLDISCOPERCEN)
    {
        if (XX_FIRSTVARVOLDISCOPERCEN == null) throw new IllegalArgumentException ("XX_FIRSTVARVOLDISCOPERCEN is mandatory.");
        set_Value ("XX_FIRSTVARVOLDISCOPERCEN", XX_FIRSTVARVOLDISCOPERCEN);
        
    }
    
    /** Get XX_FIRSTVARVOLDISCOPERCEN.
    @return XX_FIRSTVARVOLDISCOPERCEN */
    public java.math.BigDecimal getXX_FIRSTVARVOLDISCOPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_FIRSTVARVOLDISCOPERCEN");
        
    }
    
    /** Set XX_FIRSTVARVOLDISCOTO.
    @param XX_FIRSTVARVOLDISCOTO XX_FIRSTVARVOLDISCOTO */
    public void setXX_FIRSTVARVOLDISCOTO (java.math.BigDecimal XX_FIRSTVARVOLDISCOTO)
    {
        if (XX_FIRSTVARVOLDISCOTO == null) throw new IllegalArgumentException ("XX_FIRSTVARVOLDISCOTO is mandatory.");
        set_Value ("XX_FIRSTVARVOLDISCOTO", XX_FIRSTVARVOLDISCOTO);
        
    }
    
    /** Get XX_FIRSTVARVOLDISCOTO.
    @return XX_FIRSTVARVOLDISCOTO */
    public java.math.BigDecimal getXX_FIRSTVARVOLDISCOTO() 
    {
        return get_ValueAsBigDecimal("XX_FIRSTVARVOLDISCOTO");
        
    }
    
    /** Set XX_FIXVOLDISCOAMOUNT.
    @param XX_FIXVOLDISCOAMOUNT XX_FIXVOLDISCOAMOUNT */
    public void setXX_FIXVOLDISCOAMOUNT (java.math.BigDecimal XX_FIXVOLDISCOAMOUNT)
    {
        if (XX_FIXVOLDISCOAMOUNT == null) throw new IllegalArgumentException ("XX_FIXVOLDISCOAMOUNT is mandatory.");
        set_Value ("XX_FIXVOLDISCOAMOUNT", XX_FIXVOLDISCOAMOUNT);
        
    }
    
    /** Get XX_FIXVOLDISCOAMOUNT.
    @return XX_FIXVOLDISCOAMOUNT */
    public java.math.BigDecimal getXX_FIXVOLDISCOAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_FIXVOLDISCOAMOUNT");
        
    }
    
    /** Set XX_FIXVOLDISCOPERCEN.
    @param XX_FIXVOLDISCOPERCEN XX_FIXVOLDISCOPERCEN */
    public void setXX_FIXVOLDISCOPERCEN (java.math.BigDecimal XX_FIXVOLDISCOPERCEN)
    {
        if (XX_FIXVOLDISCOPERCEN == null) throw new IllegalArgumentException ("XX_FIXVOLDISCOPERCEN is mandatory.");
        set_Value ("XX_FIXVOLDISCOPERCEN", XX_FIXVOLDISCOPERCEN);
        
    }
    
    /** Get XX_FIXVOLDISCOPERCEN.
    @return XX_FIXVOLDISCOPERCEN */
    public java.math.BigDecimal getXX_FIXVOLDISCOPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_FIXVOLDISCOPERCEN");
        
    }
    
    /** Set XX_InitDate.
    @param XX_InitDate XX_InitDate */
    public void setXX_InitDate (Timestamp XX_InitDate)
    {
        if (XX_InitDate == null) throw new IllegalArgumentException ("XX_InitDate is mandatory.");
        set_Value ("XX_InitDate", XX_InitDate);
        
    }
    
    /** Get XX_InitDate.
    @return XX_InitDate */
    public Timestamp getXX_InitDate() 
    {
        return (Timestamp)get_Value("XX_InitDate");
        
    }
    
    /** Set XX_MARK.
    @param XX_MARK XX_MARK */
    public void setXX_MARK (String XX_MARK)
    {
        set_Value ("XX_MARK", XX_MARK);
        
    }
    
    /** Get XX_MARK.
    @return XX_MARK */
    public String getXX_MARK() 
    {
        return (String)get_Value("XX_MARK");
        
    }
    
    /** Set XX_NATIONALITYBECOREPRESE.
    @param XX_NATIONALITYBECOREPRESE XX_NATIONALITYBECOREPRESE */
    public void setXX_NATIONALITYBECOREPRESE (String XX_NATIONALITYBECOREPRESE)
    {
        set_Value ("XX_NATIONALITYBECOREPRESE", XX_NATIONALITYBECOREPRESE);
        
    }
    
    /** Get XX_NATIONALITYBECOREPRESE.
    @return XX_NATIONALITYBECOREPRESE */
    public String getXX_NATIONALITYBECOREPRESE() 
    {
        return (String)get_Value("XX_NATIONALITYBECOREPRESE");
        
    }
    
    /** Set XX_NATIONALITYVENDORREPRESE.
    @param XX_NATIONALITYVENDORREPRESE XX_NATIONALITYVENDORREPRESE */
    public void setXX_NATIONALITYVENDORREPRESE (String XX_NATIONALITYVENDORREPRESE)
    {
        set_Value ("XX_NATIONALITYVENDORREPRESE", XX_NATIONALITYVENDORREPRESE);
        
    }
    
    /** Get XX_NATIONALITYVENDORREPRESE.
    @return XX_NATIONALITYVENDORREPRESE */
    public String getXX_NATIONALITYVENDORREPRESE() 
    {
        return (String)get_Value("XX_NATIONALITYVENDORREPRESE");
        
    }
    
    /** Set Observation.
    @param XX_Observation Observaciones */
    public void setXX_Observation (String XX_Observation)
    {
        set_Value ("XX_Observation", XX_Observation);
        
    }
    
    /** Get Observation.
    @return Observaciones */
    public String getXX_Observation() 
    {
        return (String)get_Value("XX_Observation");
        
    }
    
    /** Set XX_PARTICIBECOBROCHAMOUNT.
    @param XX_PARTICIBECOBROCHAMOUNT XX_PARTICIBECOBROCHAMOUNT */
    public void setXX_PARTICIBECOBROCHAMOUNT (java.math.BigDecimal XX_PARTICIBECOBROCHAMOUNT)
    {
        if (XX_PARTICIBECOBROCHAMOUNT == null) throw new IllegalArgumentException ("XX_PARTICIBECOBROCHAMOUNT is mandatory.");
        set_Value ("XX_PARTICIBECOBROCHAMOUNT", XX_PARTICIBECOBROCHAMOUNT);
        
    }
    
    /** Get XX_PARTICIBECOBROCHAMOUNT.
    @return XX_PARTICIBECOBROCHAMOUNT */
    public java.math.BigDecimal getXX_PARTICIBECOBROCHAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_PARTICIBECOBROCHAMOUNT");
        
    }
    
    /** Set XX_PARTICIBECOBROCHPERCEN.
    @param XX_PARTICIBECOBROCHPERCEN XX_PARTICIBECOBROCHPERCEN */
    public void setXX_PARTICIBECOBROCHPERCEN (java.math.BigDecimal XX_PARTICIBECOBROCHPERCEN)
    {
        if (XX_PARTICIBECOBROCHPERCEN == null) throw new IllegalArgumentException ("XX_PARTICIBECOBROCHPERCEN is mandatory.");
        set_Value ("XX_PARTICIBECOBROCHPERCEN", XX_PARTICIBECOBROCHPERCEN);
        
    }
    
    /** Get XX_PARTICIBECOBROCHPERCEN.
    @return XX_PARTICIBECOBROCHPERCEN */
    public java.math.BigDecimal getXX_PARTICIBECOBROCHPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_PARTICIBECOBROCHPERCEN");
        
    }
    
    /** Set XX_PAYMENTRULE.
    @param XX_PAYMENTRULE XX_PAYMENTRULE */
    public void setXX_PAYMENTRULE (java.math.BigDecimal XX_PAYMENTRULE)
    {
        if (XX_PAYMENTRULE == null) throw new IllegalArgumentException ("XX_PAYMENTRULE is mandatory.");
        set_Value ("XX_PAYMENTRULE", XX_PAYMENTRULE);
        
    }
    
    /** Get XX_PAYMENTRULE.
    @return XX_PAYMENTRULE */
    public java.math.BigDecimal getXX_PAYMENTRULE() 
    {
        return get_ValueAsBigDecimal("XX_PAYMENTRULE");
        
    }
    
    /** Set XX_PRODTRADEAGREEMENT.
    @param XX_PRODTRADEAGREEMENT XX_PRODTRADEAGREEMENT */
    public void setXX_PRODTRADEAGREEMENT (java.math.BigDecimal XX_PRODTRADEAGREEMENT)
    {
        if (XX_PRODTRADEAGREEMENT == null) throw new IllegalArgumentException ("XX_PRODTRADEAGREEMENT is mandatory.");
        set_Value ("XX_PRODTRADEAGREEMENT", XX_PRODTRADEAGREEMENT);
        
    }
    
    /** Get XX_PRODTRADEAGREEMENT.
    @return XX_PRODTRADEAGREEMENT */
    public java.math.BigDecimal getXX_PRODTRADEAGREEMENT() 
    {
        return get_ValueAsBigDecimal("XX_PRODTRADEAGREEMENT");
        
    }
    
    /** Set XX_PubAmount.
    @param XX_PubAmount Aporte a publicidad (Monto) */
    public void setXX_PubAmount (java.math.BigDecimal XX_PubAmount)
    {
        if (XX_PubAmount == null) throw new IllegalArgumentException ("XX_PubAmount is mandatory.");
        set_Value ("XX_PubAmount", XX_PubAmount);
        
    }
    
    /** Get XX_PubAmount.
    @return Aporte a publicidad (Monto) */
    public java.math.BigDecimal getXX_PubAmount() 
    {
        return get_ValueAsBigDecimal("XX_PubAmount");
        
    }
    
    /** Set XX_PubPercent.
    @param XX_PubPercent Aporte a publicidad (Porcentaje) */
    public void setXX_PubPercent (java.math.BigDecimal XX_PubPercent)
    {
        if (XX_PubPercent == null) throw new IllegalArgumentException ("XX_PubPercent is mandatory.");
        set_Value ("XX_PubPercent", XX_PubPercent);
        
    }
    
    /** Get XX_PubPercent.
    @return Aporte a publicidad (Porcentaje) */
    public java.math.BigDecimal getXX_PubPercent() 
    {
        return get_ValueAsBigDecimal("XX_PubPercent");
        
    }
    
    /** Set Register.
    @param XX_Register Register */
    public void setXX_Register (String XX_Register)
    {
        set_Value ("XX_Register", XX_Register);
        
    }
    
    /** Get Register.
    @return Register */
    public String getXX_Register() 
    {
        return (String)get_Value("XX_Register");
        
    }
    
    /** Set XX_SECONDVARVOLDISCOFROM.
    @param XX_SECONDVARVOLDISCOFROM XX_SECONDVARVOLDISCOFROM */
    public void setXX_SECONDVARVOLDISCOFROM (java.math.BigDecimal XX_SECONDVARVOLDISCOFROM)
    {
        if (XX_SECONDVARVOLDISCOFROM == null) throw new IllegalArgumentException ("XX_SECONDVARVOLDISCOFROM is mandatory.");
        set_Value ("XX_SECONDVARVOLDISCOFROM", XX_SECONDVARVOLDISCOFROM);
        
    }
    
    /** Get XX_SECONDVARVOLDISCOFROM.
    @return XX_SECONDVARVOLDISCOFROM */
    public java.math.BigDecimal getXX_SECONDVARVOLDISCOFROM() 
    {
        return get_ValueAsBigDecimal("XX_SECONDVARVOLDISCOFROM");
        
    }
    
    /** Set XX_SECONDVARVOLDISCOPERCEN.
    @param XX_SECONDVARVOLDISCOPERCEN XX_SECONDVARVOLDISCOPERCEN */
    public void setXX_SECONDVARVOLDISCOPERCEN (java.math.BigDecimal XX_SECONDVARVOLDISCOPERCEN)
    {
        if (XX_SECONDVARVOLDISCOPERCEN == null) throw new IllegalArgumentException ("XX_SECONDVARVOLDISCOPERCEN is mandatory.");
        set_Value ("XX_SECONDVARVOLDISCOPERCEN", XX_SECONDVARVOLDISCOPERCEN);
        
    }
    
    /** Get XX_SECONDVARVOLDISCOPERCEN.
    @return XX_SECONDVARVOLDISCOPERCEN */
    public java.math.BigDecimal getXX_SECONDVARVOLDISCOPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_SECONDVARVOLDISCOPERCEN");
        
    }
    
    /** Set XX_SECONDVARVOLDISCOTO.
    @param XX_SECONDVARVOLDISCOTO XX_SECONDVARVOLDISCOTO */
    public void setXX_SECONDVARVOLDISCOTO (java.math.BigDecimal XX_SECONDVARVOLDISCOTO)
    {
        if (XX_SECONDVARVOLDISCOTO == null) throw new IllegalArgumentException ("XX_SECONDVARVOLDISCOTO is mandatory.");
        set_Value ("XX_SECONDVARVOLDISCOTO", XX_SECONDVARVOLDISCOTO);
        
    }
    
    /** Get XX_SECONDVARVOLDISCOTO.
    @return XX_SECONDVARVOLDISCOTO */
    public java.math.BigDecimal getXX_SECONDVARVOLDISCOTO() 
    {
        return get_ValueAsBigDecimal("XX_SECONDVARVOLDISCOTO");
        
    }
    
    /** Set XX_STATEREGISTER.
    @param XX_STATEREGISTER XX_STATEREGISTER */
    public void setXX_STATEREGISTER (java.math.BigDecimal XX_STATEREGISTER)
    {
        if (XX_STATEREGISTER == null) throw new IllegalArgumentException ("XX_STATEREGISTER is mandatory.");
        set_Value ("XX_STATEREGISTER", XX_STATEREGISTER);
        
    }
    
    /** Get XX_STATEREGISTER.
    @return XX_STATEREGISTER */
    public java.math.BigDecimal getXX_STATEREGISTER() 
    {
        return get_ValueAsBigDecimal("XX_STATEREGISTER");
        
    }
    
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (String XX_Status)
    {
        set_Value ("XX_Status", XX_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_Status() 
    {
        return (String)get_Value("XX_Status");
        
    }
    
    /** Set Tax Amount.
    @param XX_TaxAmount Tax Amount */
    public void setXX_TaxAmount (java.math.BigDecimal XX_TaxAmount)
    {
        set_Value ("XX_TaxAmount", XX_TaxAmount);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount */
    public java.math.BigDecimal getXX_TaxAmount() 
    {
        return get_ValueAsBigDecimal("XX_TaxAmount");
        
    }
    
    /** Set XX_THIRDVARVOLDISCOFROM.
    @param XX_THIRDVARVOLDISCOFROM XX_THIRDVARVOLDISCOFROM */
    public void setXX_THIRDVARVOLDISCOFROM (java.math.BigDecimal XX_THIRDVARVOLDISCOFROM)
    {
        if (XX_THIRDVARVOLDISCOFROM == null) throw new IllegalArgumentException ("XX_THIRDVARVOLDISCOFROM is mandatory.");
        set_Value ("XX_THIRDVARVOLDISCOFROM", XX_THIRDVARVOLDISCOFROM);
        
    }
    
    /** Get XX_THIRDVARVOLDISCOFROM.
    @return XX_THIRDVARVOLDISCOFROM */
    public java.math.BigDecimal getXX_THIRDVARVOLDISCOFROM() 
    {
        return get_ValueAsBigDecimal("XX_THIRDVARVOLDISCOFROM");
        
    }
    
    /** Set XX_THIRDVARVOLDISCOPERCEN.
    @param XX_THIRDVARVOLDISCOPERCEN XX_THIRDVARVOLDISCOPERCEN */
    public void setXX_THIRDVARVOLDISCOPERCEN (java.math.BigDecimal XX_THIRDVARVOLDISCOPERCEN)
    {
        if (XX_THIRDVARVOLDISCOPERCEN == null) throw new IllegalArgumentException ("XX_THIRDVARVOLDISCOPERCEN is mandatory.");
        set_Value ("XX_THIRDVARVOLDISCOPERCEN", XX_THIRDVARVOLDISCOPERCEN);
        
    }
    
    /** Get XX_THIRDVARVOLDISCOPERCEN.
    @return XX_THIRDVARVOLDISCOPERCEN */
    public java.math.BigDecimal getXX_THIRDVARVOLDISCOPERCEN() 
    {
        return get_ValueAsBigDecimal("XX_THIRDVARVOLDISCOPERCEN");
        
    }
    
    /** Set XX_THIRDVARVOLDISCOTO.
    @param XX_THIRDVARVOLDISCOTO XX_THIRDVARVOLDISCOTO */
    public void setXX_THIRDVARVOLDISCOTO (java.math.BigDecimal XX_THIRDVARVOLDISCOTO)
    {
        if (XX_THIRDVARVOLDISCOTO == null) throw new IllegalArgumentException ("XX_THIRDVARVOLDISCOTO is mandatory.");
        set_Value ("XX_THIRDVARVOLDISCOTO", XX_THIRDVARVOLDISCOTO);
        
    }
    
    /** Get XX_THIRDVARVOLDISCOTO.
    @return XX_THIRDVARVOLDISCOTO */
    public java.math.BigDecimal getXX_THIRDVARVOLDISCOTO() 
    {
        return get_ValueAsBigDecimal("XX_THIRDVARVOLDISCOTO");
        
    }
    
    /** Set XX_TotalTax.
    @param XX_TotalTax Impuesto total */
    public void setXX_TotalTax (java.math.BigDecimal XX_TotalTax)
    {
        set_Value ("XX_TotalTax", XX_TotalTax);
        
    }
    
    /** Get XX_TotalTax.
    @return Impuesto total */
    public java.math.BigDecimal getXX_TotalTax() 
    {
        return get_ValueAsBigDecimal("XX_TotalTax");
        
    }
    
    /** Set XX_TYPECREDITSTOREOPENING.
    @param XX_TYPECREDITSTOREOPENING XX_TYPECREDITSTOREOPENING */
    public void setXX_TYPECREDITSTOREOPENING (java.math.BigDecimal XX_TYPECREDITSTOREOPENING)
    {
        if (XX_TYPECREDITSTOREOPENING == null) throw new IllegalArgumentException ("XX_TYPECREDITSTOREOPENING is mandatory.");
        set_Value ("XX_TYPECREDITSTOREOPENING", XX_TYPECREDITSTOREOPENING);
        
    }
    
    /** Get XX_TYPECREDITSTOREOPENING.
    @return XX_TYPECREDITSTOREOPENING */
    public java.math.BigDecimal getXX_TYPECREDITSTOREOPENING() 
    {
        return get_ValueAsBigDecimal("XX_TYPECREDITSTOREOPENING");
        
    }
    
    /** Set Type of Tax.
    @param XX_TypeTax_ID Tipo de Impuesto */
    public void setXX_TypeTax_ID (int XX_TypeTax_ID)
    {
        if (XX_TypeTax_ID < 1) throw new IllegalArgumentException ("XX_TypeTax_ID is mandatory.");
        set_Value ("XX_TypeTax_ID", Integer.valueOf(XX_TypeTax_ID));
        
    }
    
    /** Get Type of Tax.
    @return Tipo de Impuesto */
    public int getXX_TypeTax_ID() 
    {
        return get_ValueAsInt("XX_TypeTax_ID");
        
    }
    
    /** Set TRADEAGREEMENTS ID.
    @param XX_VCN_TRADEAGREEMENTS_ID Id de la tabla Acuerdos Comerciales */
    public void setXX_VCN_TRADEAGREEMENTS_ID (int XX_VCN_TRADEAGREEMENTS_ID)
    {
        if (XX_VCN_TRADEAGREEMENTS_ID < 1) throw new IllegalArgumentException ("XX_VCN_TRADEAGREEMENTS_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_TRADEAGREEMENTS_ID", Integer.valueOf(XX_VCN_TRADEAGREEMENTS_ID));
        
    }
    
    /** Get TRADEAGREEMENTS ID.
    @return Id de la tabla Acuerdos Comerciales */
    public int getXX_VCN_TRADEAGREEMENTS_ID() 
    {
        return get_ValueAsInt("XX_VCN_TRADEAGREEMENTS_ID");
        
    }
    
    /** Set Vendor Country.
    @param XX_VendorCountry_ID País del Proveedor  */
    public void setXX_VendorCountry_ID (int XX_VendorCountry_ID)
    {
        if (XX_VendorCountry_ID <= 0) set_ValueNoCheck ("XX_VendorCountry_ID", null);
        else
        set_ValueNoCheck ("XX_VendorCountry_ID", Integer.valueOf(XX_VendorCountry_ID));
        
    }
    
    /** Get Vendor Country.
    @return País del Proveedor  */
    public int getXX_VendorCountry_ID() 
    {
        return get_ValueAsInt("XX_VendorCountry_ID");
        
    }
    
    /** Set XX_VENDORNAME.
    @param XX_VENDORNAME XX_VENDORNAME */
    public void setXX_VENDORNAME (String XX_VENDORNAME)
    {
        set_Value ("XX_VENDORNAME", XX_VENDORNAME);
        
    }
    
    /** Get XX_VENDORNAME.
    @return XX_VENDORNAME */
    public String getXX_VENDORNAME() 
    {
        return (String)get_Value("XX_VENDORNAME");
        
    }
    
    /** Set XX_VENDORREPRESE.
    @param XX_VENDORREPRESE XX_VENDORREPRESE */
    public void setXX_VENDORREPRESE (String XX_VENDORREPRESE)
    {
        set_Value ("XX_VENDORREPRESE", XX_VENDORREPRESE);
        
    }
    
    /** Get XX_VENDORREPRESE.
    @return XX_VENDORREPRESE */
    public String getXX_VENDORREPRESE() 
    {
        return (String)get_Value("XX_VENDORREPRESE");
        
    }
    
    /** Set Vendor Type.
    @param XX_VendorType_ID Describe el Tipo de Proveedor */
    public void setXX_VendorType_ID (int XX_VendorType_ID)
    {
        throw new IllegalArgumentException ("XX_VendorType_ID is virtual column");
        
    }
    
    /** Get Vendor Type.
    @return Describe el Tipo de Proveedor */
    public int getXX_VendorType_ID() 
    {
        return get_ValueAsInt("XX_VendorType_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID < 1) throw new IllegalArgumentException ("XX_VMR_Category_ID is mandatory.");
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
    }
    
    
}
