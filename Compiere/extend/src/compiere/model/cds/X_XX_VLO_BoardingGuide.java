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
/** Generated Model for XX_VLO_BoardingGuide
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_BoardingGuide extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_BoardingGuide_ID id
    @param trx transaction
    */
    public X_XX_VLO_BoardingGuide (Ctx ctx, int XX_VLO_BoardingGuide_ID, Trx trx)
    {
        super (ctx, XX_VLO_BoardingGuide_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_BoardingGuide_ID == 0)
        {
            setXX_VLO_BoardingGuide_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_BoardingGuide (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27647319637789L;
    /** Last Updated Timestamp 2013-04-05 16:08:41.0 */
    public static final long updatedMS = 1365194321000L;
    /** AD_Table_ID=1000192 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_BoardingGuide");
        
    }
    ;
    
    /** TableName=XX_VLO_BoardingGuide */
    public static final String Table_Name="XX_VLO_BoardingGuide";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Comments.
    @param Comments Comments or additional information */
    public void setComments (String Comments)
    {
        set_Value ("Comments", Comments);
        
    }
    
    /** Get Comments.
    @return Comments or additional information */
    public String getComments() 
    {
        return (String)get_Value("Comments");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_ValueNoCheck ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Customs Agent Real Amount.
    @param XX_AARealAmount Customs Agent Real Amount */
    public void setXX_AARealAmount (java.math.BigDecimal XX_AARealAmount)
    {
        set_Value ("XX_AARealAmount", XX_AARealAmount);
        
    }
    
    /** Get Customs Agent Real Amount.
    @return Customs Agent Real Amount */
    public java.math.BigDecimal getXX_AARealAmount() 
    {
        return get_ValueAsBigDecimal("XX_AARealAmount");
        
    }
    
    /** Set Customs Agent Real Amount.
    @param XX_AARealAmountFac Customs Agent Real Amount */
    public void setXX_AARealAmountFac (java.math.BigDecimal XX_AARealAmountFac)
    {
        set_Value ("XX_AARealAmountFac", XX_AARealAmountFac);
        
    }
    
    /** Get Customs Agent Real Amount.
    @return Customs Agent Real Amount */
    public java.math.BigDecimal getXX_AARealAmountFac() 
    {
        return get_ValueAsBigDecimal("XX_AARealAmountFac");
        
    }
    
    /** Set Agent Load Invoice Amount.
    @param XX_AGENTLOADINVOICEAMOUNT Agent Load Invoice Amount */
    public void setXX_AGENTLOADINVOICEAMOUNT (java.math.BigDecimal XX_AGENTLOADINVOICEAMOUNT)
    {
        set_Value ("XX_AGENTLOADINVOICEAMOUNT", XX_AGENTLOADINVOICEAMOUNT);
        
    }
    
    /** Get Agent Load Invoice Amount.
    @return Agent Load Invoice Amount */
    public java.math.BigDecimal getXX_AGENTLOADINVOICEAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_AGENTLOADINVOICEAMOUNT");
        
    }
    
    /** Set Alert.
    @param XX_Alert Alert */
    public void setXX_Alert (boolean XX_Alert)
    {
        set_Value ("XX_Alert", Boolean.valueOf(XX_Alert));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert() 
    {
        return get_ValueAsBoolean("XX_Alert");
        
    }
    
    /** Set Associate Purchase Order.
    @param XX_AssociateOrder Associate Purchase Order */
    public void setXX_AssociateOrder (String XX_AssociateOrder)
    {
        set_Value ("XX_AssociateOrder", XX_AssociateOrder);
        
    }
    
    /** Get Associate Purchase Order.
    @return Associate Purchase Order */
    public String getXX_AssociateOrder() 
    {
        return (String)get_Value("XX_AssociateOrder");
        
    }
    
    /** Set Cargo Agent Invoice Emission Date .
    @param XX_CARGOAGENTINVOEMISIONDATE Cargo Agent Invoice Emission Date  */
    public void setXX_CARGOAGENTINVOEMISIONDATE (Timestamp XX_CARGOAGENTINVOEMISIONDATE)
    {
        set_Value ("XX_CARGOAGENTINVOEMISIONDATE", XX_CARGOAGENTINVOEMISIONDATE);
        
    }
    
    /** Get Cargo Agent Invoice Emission Date .
    @return Cargo Agent Invoice Emission Date  */
    public Timestamp getXX_CARGOAGENTINVOEMISIONDATE() 
    {
        return (Timestamp)get_Value("XX_CARGOAGENTINVOEMISIONDATE");
        
    }
    
    /** Set Cargo Agent Invoice .
    @param XX_CARGOAGENTINVOICE Cargo Agent Invoice  */
    public void setXX_CARGOAGENTINVOICE (String XX_CARGOAGENTINVOICE)
    {
        set_Value ("XX_CARGOAGENTINVOICE", XX_CARGOAGENTINVOICE);
        
    }
    
    /** Get Cargo Agent Invoice .
    @return Cargo Agent Invoice  */
    public String getXX_CARGOAGENTINVOICE() 
    {
        return (String)get_Value("XX_CARGOAGENTINVOICE");
        
    }
    
    /** Set Cargo Agent Invoice Real Amount.
    @param XX_CargoAgentInvoiceFac Cargo Agent Invoice Real Amount */
    public void setXX_CargoAgentInvoiceFac (java.math.BigDecimal XX_CargoAgentInvoiceFac)
    {
        set_Value ("XX_CargoAgentInvoiceFac", XX_CargoAgentInvoiceFac);
        
    }
    
    /** Get Cargo Agent Invoice Real Amount.
    @return Cargo Agent Invoice Real Amount */
    public java.math.BigDecimal getXX_CargoAgentInvoiceFac() 
    {
        return get_ValueAsBigDecimal("XX_CargoAgentInvoiceFac");
        
    }
    
    /** Set Cargo Agent Invoice Reception Date .
    @param XX_CARGOAGENTINVORECEPDATE Cargo Agent Invoice Reception Date  */
    public void setXX_CARGOAGENTINVORECEPDATE (Timestamp XX_CARGOAGENTINVORECEPDATE)
    {
        set_Value ("XX_CARGOAGENTINVORECEPDATE", XX_CARGOAGENTINVORECEPDATE);
        
    }
    
    /** Get Cargo Agent Invoice Reception Date .
    @return Cargo Agent Invoice Reception Date  */
    public Timestamp getXX_CARGOAGENTINVORECEPDATE() 
    {
        return (Timestamp)get_Value("XX_CARGOAGENTINVORECEPDATE");
        
    }
    
    /** Set CD Arrival Real Date .
    @param XX_CDARRIVALREALDATE CD Arrival Real Date  */
    public void setXX_CDARRIVALREALDATE (Timestamp XX_CDARRIVALREALDATE)
    {
        set_Value ("XX_CDARRIVALREALDATE", XX_CDARRIVALREALDATE);
        
    }
    
    /** Get CD Arrival Real Date .
    @return CD Arrival Real Date  */
    public Timestamp getXX_CDARRIVALREALDATE() 
    {
        return (Timestamp)get_Value("XX_CDARRIVALREALDATE");
        
    }
    
    /** Set Close Guide.
    @param XX_CloseGuide Close Guide */
    public void setXX_CloseGuide (String XX_CloseGuide)
    {
        set_Value ("XX_CloseGuide", XX_CloseGuide);
        
    }
    
    /** Get Close Guide.
    @return Close Guide */
    public String getXX_CloseGuide() 
    {
        return (String)get_Value("XX_CloseGuide");
        
    }
    
    /** Set Close Guide Check.
    @param XX_CloseGuideCheck Close Guide Check */
    public void setXX_CloseGuideCheck (boolean XX_CloseGuideCheck)
    {
        set_Value ("XX_CloseGuideCheck", Boolean.valueOf(XX_CloseGuideCheck));
        
    }
    
    /** Get Close Guide Check.
    @return Close Guide Check */
    public boolean isXX_CloseGuideCheck() 
    {
        return get_ValueAsBoolean("XX_CloseGuideCheck");
        
    }
    
    /** Set Customs Agent Invoice Emission Date .
    @param XX_CUSTAGENINVOIEMISIONDATE Customs Agent Invoice Emission Date  */
    public void setXX_CUSTAGENINVOIEMISIONDATE (Timestamp XX_CUSTAGENINVOIEMISIONDATE)
    {
        set_Value ("XX_CUSTAGENINVOIEMISIONDATE", XX_CUSTAGENINVOIEMISIONDATE);
        
    }
    
    /** Get Customs Agent Invoice Emission Date .
    @return Customs Agent Invoice Emission Date  */
    public Timestamp getXX_CUSTAGENINVOIEMISIONDATE() 
    {
        return (Timestamp)get_Value("XX_CUSTAGENINVOIEMISIONDATE");
        
    }
    
    /** Set Customs Agent Invoice Reception Date .
    @param XX_CUSTAGENINVOIRECEPTDATE Customs Agent Invoice Reception Date  */
    public void setXX_CUSTAGENINVOIRECEPTDATE (Timestamp XX_CUSTAGENINVOIRECEPTDATE)
    {
        set_Value ("XX_CUSTAGENINVOIRECEPTDATE", XX_CUSTAGENINVOIRECEPTDATE);
        
    }
    
    /** Get Customs Agent Invoice Reception Date .
    @return Customs Agent Invoice Reception Date  */
    public Timestamp getXX_CUSTAGENINVOIRECEPTDATE() 
    {
        return (Timestamp)get_Value("XX_CUSTAGENINVOIRECEPTDATE");
        
    }
    
    /** Set Custom Agent Invoice .
    @param XX_CUSTOMAGENTINVOICE Custom Agent Invoice  */
    public void setXX_CUSTOMAGENTINVOICE (String XX_CUSTOMAGENTINVOICE)
    {
        set_Value ("XX_CUSTOMAGENTINVOICE", XX_CUSTOMAGENTINVOICE);
        
    }
    
    /** Get Custom Agent Invoice .
    @return Custom Agent Invoice  */
    public String getXX_CUSTOMAGENTINVOICE() 
    {
        return (String)get_Value("XX_CUSTOMAGENTINVOICE");
        
    }
    
    /** Set Custom Rights Cancel Date .
    @param XX_CUSTOMRIGHTSCANCELDATE Custom Rights Cancel Date  */
    public void setXX_CUSTOMRIGHTSCANCELDATE (Timestamp XX_CUSTOMRIGHTSCANCELDATE)
    {
        set_Value ("XX_CUSTOMRIGHTSCANCELDATE", XX_CUSTOMRIGHTSCANCELDATE);
        
    }
    
    /** Get Custom Rights Cancel Date .
    @return Custom Rights Cancel Date  */
    public Timestamp getXX_CUSTOMRIGHTSCANCELDATE() 
    {
        return (Timestamp)get_Value("XX_CUSTOMRIGHTSCANCELDATE");
        
    }
    
    /** Set Customs Agent Anticipation Amount .
    @param XX_CUSTOMSAGENTANTICAMOUNT Customs Agent Anticipation Amount  */
    public void setXX_CUSTOMSAGENTANTICAMOUNT (java.math.BigDecimal XX_CUSTOMSAGENTANTICAMOUNT)
    {
        set_Value ("XX_CUSTOMSAGENTANTICAMOUNT", XX_CUSTOMSAGENTANTICAMOUNT);
        
    }
    
    /** Get Customs Agent Anticipation Amount .
    @return Customs Agent Anticipation Amount  */
    public java.math.BigDecimal getXX_CUSTOMSAGENTANTICAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_CUSTOMSAGENTANTICAMOUNT");
        
    }
    
    /** AGECOM = AGC */
    public static final String XX_CUSTOMSAGENTNAME_AGECOM = X_Ref_XX_Ref_CustomAgentName.AGECOM.getValue();
    /** ALAFLETES = ALT */
    public static final String XX_CUSTOMSAGENTNAME_ALAFLETES = X_Ref_XX_Ref_CustomAgentName.ALAFLETES.getValue();
    /** BARRETO & BORGES = B&B */
    public static final String XX_CUSTOMSAGENTNAME_BARRETOBORGES = X_Ref_XX_Ref_CustomAgentName.BARRETOBORGES.getValue();
    /** MIRKO INTERNACIONAL, C.A. = MIR */
    public static final String XX_CUSTOMSAGENTNAME_MIRKOINTERNACIONALCA = X_Ref_XX_Ref_CustomAgentName.MIRKOINTERNACIONALCA.getValue();
    /** OTRO = OTR */
    public static final String XX_CUSTOMSAGENTNAME_OTRO = X_Ref_XX_Ref_CustomAgentName.OTRO.getValue();
    /** UPS = UPS */
    public static final String XX_CUSTOMSAGENTNAME_UPS = X_Ref_XX_Ref_CustomAgentName.UPS.getValue();
    /** Set Customs Agent Name .
    @param XX_CUSTOMSAGENTNAME Customs Agent Name  */
    public void setXX_CUSTOMSAGENTNAME (String XX_CUSTOMSAGENTNAME)
    {
        if (!X_Ref_XX_Ref_CustomAgentName.isValid(XX_CUSTOMSAGENTNAME))
        throw new IllegalArgumentException ("XX_CUSTOMSAGENTNAME Invalid value - " + XX_CUSTOMSAGENTNAME + " - Reference_ID=1000285 - AGC - ALT - B&B - MIR - OTR - UPS");
        set_Value ("XX_CUSTOMSAGENTNAME", XX_CUSTOMSAGENTNAME);
        
    }
    
    /** Get Customs Agent Name .
    @return Customs Agent Name  */
    public String getXX_CUSTOMSAGENTNAME() 
    {
        return (String)get_Value("XX_CUSTOMSAGENTNAME");
        
    }
    
    /** Set Customs Agent Advance Payment Date .
    @param XX_CUSTOMSAGENTPAYMENTDATE Customs Agent Advance Payment Date  */
    public void setXX_CUSTOMSAGENTPAYMENTDATE (Timestamp XX_CUSTOMSAGENTPAYMENTDATE)
    {
        set_Value ("XX_CUSTOMSAGENTPAYMENTDATE", XX_CUSTOMSAGENTPAYMENTDATE);
        
    }
    
    /** Get Customs Agent Advance Payment Date .
    @return Customs Agent Advance Payment Date  */
    public Timestamp getXX_CUSTOMSAGENTPAYMENTDATE() 
    {
        return (Timestamp)get_Value("XX_CUSTOMSAGENTPAYMENTDATE");
        
    }
    
    /** Set Customs Duties Preliquidation Reception Real Date.
    @param XX_DAPRELIQUIDARECEPREALDATE Customs Duties Preliquidation Reception Real Date */
    public void setXX_DAPRELIQUIDARECEPREALDATE (Timestamp XX_DAPRELIQUIDARECEPREALDATE)
    {
        set_Value ("XX_DAPRELIQUIDARECEPREALDATE", XX_DAPRELIQUIDARECEPREALDATE);
        
    }
    
    /** Get Customs Duties Preliquidation Reception Real Date.
    @return Customs Duties Preliquidation Reception Real Date */
    public Timestamp getXX_DAPRELIQUIDARECEPREALDATE() 
    {
        return (Timestamp)get_Value("XX_DAPRELIQUIDARECEPREALDATE");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveCargo Definitive */
    public void setXX_DefinitiveCargo (boolean XX_DefinitiveCargo)
    {
        set_Value ("XX_DefinitiveCargo", Boolean.valueOf(XX_DefinitiveCargo));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveCargo() 
    {
        return get_ValueAsBoolean("XX_DefinitiveCargo");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveCD Definitive */
    public void setXX_DefinitiveCD (boolean XX_DefinitiveCD)
    {
        set_Value ("XX_DefinitiveCD", Boolean.valueOf(XX_DefinitiveCD));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveCD() 
    {
        return get_ValueAsBoolean("XX_DefinitiveCD");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveDA Definitive */
    public void setXX_DefinitiveDA (boolean XX_DefinitiveDA)
    {
        set_Value ("XX_DefinitiveDA", Boolean.valueOf(XX_DefinitiveDA));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveDA() 
    {
        return get_ValueAsBoolean("XX_DefinitiveDA");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveDispatch Definitive */
    public void setXX_DefinitiveDispatch (boolean XX_DefinitiveDispatch)
    {
        set_Value ("XX_DefinitiveDispatch", Boolean.valueOf(XX_DefinitiveDispatch));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveDispatch() 
    {
        return get_ValueAsBoolean("XX_DefinitiveDispatch");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveFee Definitive */
    public void setXX_DefinitiveFee (boolean XX_DefinitiveFee)
    {
        set_Value ("XX_DefinitiveFee", Boolean.valueOf(XX_DefinitiveFee));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveFee() 
    {
        return get_ValueAsBoolean("XX_DefinitiveFee");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveShippingDate Definitive */
    public void setXX_DefinitiveShippingDate (boolean XX_DefinitiveShippingDate)
    {
        set_Value ("XX_DefinitiveShippingDate", Boolean.valueOf(XX_DefinitiveShippingDate));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveShippingDate() 
    {
        return get_ValueAsBoolean("XX_DefinitiveShippingDate");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveValidation Definitive */
    public void setXX_DefinitiveValidation (boolean XX_DefinitiveValidation)
    {
        set_Value ("XX_DefinitiveValidation", Boolean.valueOf(XX_DefinitiveValidation));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveValidation() 
    {
        return get_ValueAsBoolean("XX_DefinitiveValidation");
        
    }
    
    /** Set Definitive.
    @param XX_DefinitiveVzlaDate Definitive */
    public void setXX_DefinitiveVzlaDate (boolean XX_DefinitiveVzlaDate)
    {
        set_Value ("XX_DefinitiveVzlaDate", Boolean.valueOf(XX_DefinitiveVzlaDate));
        
    }
    
    /** Get Definitive.
    @return Definitive */
    public boolean isXX_DefinitiveVzlaDate() 
    {
        return get_ValueAsBoolean("XX_DefinitiveVzlaDate");
        
    }
    
    /** Set Disassociate Purchase Order.
    @param XX_DisassociateOrder Disassociate Purchase Order */
    public void setXX_DisassociateOrder (String XX_DisassociateOrder)
    {
        set_Value ("XX_DisassociateOrder", XX_DisassociateOrder);
        
    }
    
    /** Get Disassociate Purchase Order.
    @return Disassociate Purchase Order */
    public String getXX_DisassociateOrder() 
    {
        return (String)get_Value("XX_DisassociateOrder");
        
    }
    
    /** Set Expense Account .
    @param XX_EXPENSEACCOUNT Expense Account  */
    public void setXX_EXPENSEACCOUNT (String XX_EXPENSEACCOUNT)
    {
        set_Value ("XX_EXPENSEACCOUNT", XX_EXPENSEACCOUNT);
        
    }
    
    /** Get Expense Account .
    @return Expense Account  */
    public String getXX_EXPENSEACCOUNT() 
    {
        return (String)get_Value("XX_EXPENSEACCOUNT");
        
    }
    
    /** Set XX_FeeRealAmount.
    @param XX_FeeRealAmount Monto de Gasto Fee */
    public void setXX_FeeRealAmount (java.math.BigDecimal XX_FeeRealAmount)
    {
        set_Value ("XX_FeeRealAmount", XX_FeeRealAmount);
        
    }
    
    /** Get XX_FeeRealAmount.
    @return Monto de Gasto Fee */
    public java.math.BigDecimal getXX_FeeRealAmount() 
    {
        return get_ValueAsBigDecimal("XX_FeeRealAmount");
        
    }
    
    /** Set XX_FeeRealAmountFac.
    @param XX_FeeRealAmountFac Monto de Gasto Fee usado en el Factor Definitivo */
    public void setXX_FeeRealAmountFac (java.math.BigDecimal XX_FeeRealAmountFac)
    {
        set_Value ("XX_FeeRealAmountFac", XX_FeeRealAmountFac);
        
    }
    
    /** Get XX_FeeRealAmountFac.
    @return Monto de Gasto Fee usado en el Factor Definitivo */
    public java.math.BigDecimal getXX_FeeRealAmountFac() 
    {
        return get_ValueAsBigDecimal("XX_FeeRealAmountFac");
        
    }
    
    /** Set Guide Number.
    @param XX_GUIDENUMBER Guide Number */
    public void setXX_GUIDENUMBER (String XX_GUIDENUMBER)
    {
        set_Value ("XX_GUIDENUMBER", XX_GUIDENUMBER);
        
    }
    
    /** Get Guide Number.
    @return Guide Number */
    public String getXX_GUIDENUMBER() 
    {
        return (String)get_Value("XX_GUIDENUMBER");
        
    }
    
    /** Set Cargo Agent Invoice Real Amount.
    @param XX_InterFretRealAmount Cargo Agent Invoice Real Amount */
    public void setXX_InterFretRealAmount (java.math.BigDecimal XX_InterFretRealAmount)
    {
        set_Value ("XX_InterFretRealAmount", XX_InterFretRealAmount);
        
    }
    
    /** Get Cargo Agent Invoice Real Amount.
    @return Cargo Agent Invoice Real Amount */
    public java.math.BigDecimal getXX_InterFretRealAmount() 
    {
        return get_ValueAsBigDecimal("XX_InterFretRealAmount");
        
    }
    
    /** Set Month Arrival.
    @param XX_MonthArrival Month Arrival */
    public void setXX_MonthArrival (String XX_MonthArrival)
    {
        set_Value ("XX_MonthArrival", XX_MonthArrival);
        
    }
    
    /** Get Month Arrival.
    @return Month Arrival */
    public String getXX_MonthArrival() 
    {
        return (String)get_Value("XX_MonthArrival");
        
    }
    
    /** Set National Treasury Real Amount .
    @param XX_NACTREASREALAMOUNT National Treasury Real Amount  */
    public void setXX_NACTREASREALAMOUNT (java.math.BigDecimal XX_NACTREASREALAMOUNT)
    {
        set_Value ("XX_NACTREASREALAMOUNT", XX_NACTREASREALAMOUNT);
        
    }
    
    /** Get National Treasury Real Amount .
    @return National Treasury Real Amount  */
    public java.math.BigDecimal getXX_NACTREASREALAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_NACTREASREALAMOUNT");
        
    }
    
    /** Set National Treasury Real Amount.
    @param XX_NacTreasRealFac National Treasury Real Amount */
    public void setXX_NacTreasRealFac (java.math.BigDecimal XX_NacTreasRealFac)
    {
        set_Value ("XX_NacTreasRealFac", XX_NacTreasRealFac);
        
    }
    
    /** Get National Treasury Real Amount.
    @return National Treasury Real Amount */
    public java.math.BigDecimal getXX_NacTreasRealFac() 
    {
        return get_ValueAsBigDecimal("XX_NacTreasRealFac");
        
    }
    
    /** Set XX_OrderFilter.
    @param XX_OrderFilter XX_OrderFilter */
    public void setXX_OrderFilter (int XX_OrderFilter)
    {
        throw new IllegalArgumentException ("XX_OrderFilter is virtual column");
        
    }
    
    /** Get XX_OrderFilter.
    @return XX_OrderFilter */
    public int getXX_OrderFilter() 
    {
        return get_ValueAsInt("XX_OrderFilter");
        
    }
    
    /** Set Processed.
    @param XX_ProcessedImport Processed */
    public void setXX_ProcessedImport (boolean XX_ProcessedImport)
    {
        set_Value ("XX_ProcessedImport", Boolean.valueOf(XX_ProcessedImport));
        
    }
    
    /** Get Processed.
    @return Processed */
    public boolean isXX_ProcessedImport() 
    {
        return get_ValueAsBoolean("XX_ProcessedImport");
        
    }
    
    /** Set Real Dispatch Date .
    @param XX_REALDISPATCHDATE Real Dispatch Date  */
    public void setXX_REALDISPATCHDATE (Timestamp XX_REALDISPATCHDATE)
    {
        set_Value ("XX_REALDISPATCHDATE", XX_REALDISPATCHDATE);
        
    }
    
    /** Get Real Dispatch Date .
    @return Real Dispatch Date  */
    public Timestamp getXX_REALDISPATCHDATE() 
    {
        return (Timestamp)get_Value("XX_REALDISPATCHDATE");
        
    }
    
    /** Set Real Insurance Amount.
    @param XX_REALINSURANCEAMOUNT Real Insurance Amount */
    public void setXX_REALINSURANCEAMOUNT (java.math.BigDecimal XX_REALINSURANCEAMOUNT)
    {
        set_Value ("XX_REALINSURANCEAMOUNT", XX_REALINSURANCEAMOUNT);
        
    }
    
    /** Get Real Insurance Amount.
    @return Real Insurance Amount */
    public java.math.BigDecimal getXX_REALINSURANCEAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_REALINSURANCEAMOUNT");
        
    }
    
    /** Set Real Insurance Amount.
    @param XX_RealInsuranceAmountFac Real Insurance Amount */
    public void setXX_RealInsuranceAmountFac (java.math.BigDecimal XX_RealInsuranceAmountFac)
    {
        set_Value ("XX_RealInsuranceAmountFac", XX_RealInsuranceAmountFac);
        
    }
    
    /** Get Real Insurance Amount.
    @return Real Insurance Amount */
    public java.math.BigDecimal getXX_RealInsuranceAmountFac() 
    {
        return get_ValueAsBigDecimal("XX_RealInsuranceAmountFac");
        
    }
    
    /** Set Real Mechandise Management Costs.
    @param XX_RealMerchManCost Real Mechandise Management Costs */
    public void setXX_RealMerchManCost (java.math.BigDecimal XX_RealMerchManCost)
    {
        set_Value ("XX_RealMerchManCost", XX_RealMerchManCost);
        
    }
    
    /** Get Real Mechandise Management Costs.
    @return Real Mechandise Management Costs */
    public java.math.BigDecimal getXX_RealMerchManCost() 
    {
        return get_ValueAsBigDecimal("XX_RealMerchManCost");
        
    }
    
    /** Set Revert Definitive Dates.
    @param XX_RevertDefinitiveDates Revert Definitive Dates */
    public void setXX_RevertDefinitiveDates (String XX_RevertDefinitiveDates)
    {
        set_Value ("XX_RevertDefinitiveDates", XX_RevertDefinitiveDates);
        
    }
    
    /** Get Revert Definitive Dates.
    @return Revert Definitive Dates */
    public String getXX_RevertDefinitiveDates() 
    {
        return (String)get_Value("XX_RevertDefinitiveDates");
        
    }
    
    /** Set Revert Guide.
    @param XX_RevertGuide Revert Guide */
    public void setXX_RevertGuide (String XX_RevertGuide)
    {
        set_Value ("XX_RevertGuide", XX_RevertGuide);
        
    }
    
    /** Get Revert Guide.
    @return Revert Guide */
    public String getXX_RevertGuide() 
    {
        return (String)get_Value("XX_RevertGuide");
        
    }
    
    /** Set Seniat Real Amount.
    @param XX_SeniatRealAmountFac Seniat Real Amount */
    public void setXX_SeniatRealAmountFac (java.math.BigDecimal XX_SeniatRealAmountFac)
    {
        set_Value ("XX_SeniatRealAmountFac", XX_SeniatRealAmountFac);
        
    }
    
    /** Get Seniat Real Amount.
    @return Seniat Real Amount */
    public java.math.BigDecimal getXX_SeniatRealAmountFac() 
    {
        return get_ValueAsBigDecimal("XX_SeniatRealAmountFac");
        
    }
    
    /** Set SENIAT Real  Amount .
    @param XX_SENIATREALESTEEMEDAMOUNT SENIAT Real  Amount  */
    public void setXX_SENIATREALESTEEMEDAMOUNT (java.math.BigDecimal XX_SENIATREALESTEEMEDAMOUNT)
    {
        set_Value ("XX_SENIATREALESTEEMEDAMOUNT", XX_SENIATREALESTEEMEDAMOUNT);
        
    }
    
    /** Get SENIAT Real  Amount .
    @return SENIAT Real  Amount  */
    public java.math.BigDecimal getXX_SENIATREALESTEEMEDAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_SENIATREALESTEEMEDAMOUNT");
        
    }
    
    /** Set Shipflight Name .
    @param XX_SHIPFLIGHTNAME Shipflight Name  */
    public void setXX_SHIPFLIGHTNAME (String XX_SHIPFLIGHTNAME)
    {
        set_Value ("XX_SHIPFLIGHTNAME", XX_SHIPFLIGHTNAME);
        
    }
    
    /** Get Shipflight Name .
    @return Shipflight Name  */
    public String getXX_SHIPFLIGHTNAME() 
    {
        return (String)get_Value("XX_SHIPFLIGHTNAME");
        
    }
    
    /** Set Shipping Real Date .
    @param XX_SHIPPINGREALDATE Shipping Real Date  */
    public void setXX_SHIPPINGREALDATE (Timestamp XX_SHIPPINGREALDATE)
    {
        set_Value ("XX_SHIPPINGREALDATE", XX_SHIPPINGREALDATE);
        
    }
    
    /** Get Shipping Real Date .
    @return Shipping Real Date  */
    public Timestamp getXX_SHIPPINGREALDATE() 
    {
        return (Timestamp)get_Value("XX_SHIPPINGREALDATE");
        
    }
    
    /** DHL = DHL */
    public static final String XX_TRANSPAGENTNAME_DHL = X_Ref_XX_ShippingAgentNameList.DHL.getValue();
    /** TAISA = TSA */
    public static final String XX_TRANSPAGENTNAME_TAISA = X_Ref_XX_ShippingAgentNameList.TAISA.getValue();
    /** UPS = UPS */
    public static final String XX_TRANSPAGENTNAME_UPS = X_Ref_XX_ShippingAgentNameList.UPS.getValue();
    /** Set Shipping Agent Name .
    @param XX_TRANSPAGENTNAME Shipping Agent Name  */
    public void setXX_TRANSPAGENTNAME (String XX_TRANSPAGENTNAME)
    {
        if (!X_Ref_XX_ShippingAgentNameList.isValid(XX_TRANSPAGENTNAME))
        throw new IllegalArgumentException ("XX_TRANSPAGENTNAME Invalid value - " + XX_TRANSPAGENTNAME + " - Reference_ID=1000258 - DHL - TSA - UPS");
        set_Value ("XX_TRANSPAGENTNAME", XX_TRANSPAGENTNAME);
        
    }
    
    /** Get Shipping Agent Name .
    @return Shipping Agent Name  */
    public String getXX_TRANSPAGENTNAME() 
    {
        return (String)get_Value("XX_TRANSPAGENTNAME");
        
    }
    
    /** Set File Number.
    @param XX_TRANSPORTDOCUMENTNUMBER File Number */
    public void setXX_TRANSPORTDOCUMENTNUMBER (String XX_TRANSPORTDOCUMENTNUMBER)
    {
        set_Value ("XX_TRANSPORTDOCUMENTNUMBER", XX_TRANSPORTDOCUMENTNUMBER);
        
    }
    
    /** Get File Number.
    @return File Number */
    public String getXX_TRANSPORTDOCUMENTNUMBER() 
    {
        return (String)get_Value("XX_TRANSPORTDOCUMENTNUMBER");
        
    }
    
    /** Set Type Load .
    @param XX_TYPELOAD Type Load  */
    public void setXX_TYPELOAD (String XX_TYPELOAD)
    {
        set_Value ("XX_TYPELOAD", XX_TYPELOAD);
        
    }
    
    /** Get Type Load .
    @return Type Load  */
    public String getXX_TYPELOAD() 
    {
        return (String)get_Value("XX_TYPELOAD");
        
    }
    
    /** Set Validation Real Date.
    @param XX_VALIDATIONREALDATE Validation Real Date */
    public void setXX_VALIDATIONREALDATE (Timestamp XX_VALIDATIONREALDATE)
    {
        set_Value ("XX_VALIDATIONREALDATE", XX_VALIDATIONREALDATE);
        
    }
    
    /** Get Validation Real Date.
    @return Validation Real Date */
    public Timestamp getXX_VALIDATIONREALDATE() 
    {
        return (Timestamp)get_Value("XX_VALIDATIONREALDATE");
        
    }
    
    /** Set Arrival Port.
    @param XX_VLO_ArrivalPort_ID Arrival Port */
    public void setXX_VLO_ArrivalPort_ID (int XX_VLO_ArrivalPort_ID)
    {
        if (XX_VLO_ArrivalPort_ID <= 0) set_Value ("XX_VLO_ArrivalPort_ID", null);
        else
        set_Value ("XX_VLO_ArrivalPort_ID", Integer.valueOf(XX_VLO_ArrivalPort_ID));
        
    }
    
    /** Get Arrival Port.
    @return Arrival Port */
    public int getXX_VLO_ArrivalPort_ID() 
    {
        return get_ValueAsInt("XX_VLO_ArrivalPort_ID");
        
    }
    
    /** Set File Number.
    @param XX_VLO_BoardingGuide_ID File Number */
    public void setXX_VLO_BoardingGuide_ID (int XX_VLO_BoardingGuide_ID)
    {
        if (XX_VLO_BoardingGuide_ID < 1) throw new IllegalArgumentException ("XX_VLO_BoardingGuide_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_BoardingGuide_ID", Integer.valueOf(XX_VLO_BoardingGuide_ID));
        
    }
    
    /** Get File Number.
    @return File Number */
    public int getXX_VLO_BoardingGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_BoardingGuide_ID");
        
    }
    
    /** Set Dispatch Route.
    @param XX_VLO_DispatchRoute_ID ID de vía de despacho */
    public void setXX_VLO_DispatchRoute_ID (int XX_VLO_DispatchRoute_ID)
    {
        if (XX_VLO_DispatchRoute_ID <= 0) set_Value ("XX_VLO_DispatchRoute_ID", null);
        else
        set_Value ("XX_VLO_DispatchRoute_ID", Integer.valueOf(XX_VLO_DispatchRoute_ID));
        
    }
    
    /** Get Dispatch Route.
    @return ID de vía de despacho */
    public int getXX_VLO_DispatchRoute_ID() 
    {
        return get_ValueAsInt("XX_VLO_DispatchRoute_ID");
        
    }
    
    /** Set Incoterms.
    @param XX_VLO_ShippingCondition_ID ID de condición de envío */
    public void setXX_VLO_ShippingCondition_ID (int XX_VLO_ShippingCondition_ID)
    {
        if (XX_VLO_ShippingCondition_ID <= 0) set_Value ("XX_VLO_ShippingCondition_ID", null);
        else
        set_Value ("XX_VLO_ShippingCondition_ID", Integer.valueOf(XX_VLO_ShippingCondition_ID));
        
    }
    
    /** Get Incoterms.
    @return ID de condición de envío */
    public int getXX_VLO_ShippingCondition_ID() 
    {
        return get_ValueAsInt("XX_VLO_ShippingCondition_ID");
        
    }
    
    /** Set VZLA Arrival Real Date.
    @param XX_VZLAARRIVALREALDATE VZLA Arrival Real Date */
    public void setXX_VZLAARRIVALREALDATE (Timestamp XX_VZLAARRIVALREALDATE)
    {
        set_Value ("XX_VZLAARRIVALREALDATE", XX_VZLAARRIVALREALDATE);
        
    }
    
    /** Get VZLA Arrival Real Date.
    @return VZLA Arrival Real Date */
    public Timestamp getXX_VZLAARRIVALREALDATE() 
    {
        return (Timestamp)get_Value("XX_VZLAARRIVALREALDATE");
        
    }
    
    
}
