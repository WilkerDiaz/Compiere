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
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Trx;
/** Generated Model for XX_VLO_ImportsResult
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id$ */
public class X_XX_VLO_ImportsResult extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_ImportsResult_ID id
    @param trxName transaction
    */
    public X_XX_VLO_ImportsResult (Ctx ctx, int XX_VLO_ImportsResult_ID, Trx trx)
    {
        super (ctx, XX_VLO_ImportsResult_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_ImportsResult_ID == 0)
        {
            setXX_VLO_IMPORTSRESULT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_XX_VLO_ImportsResult (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27535644417789L;
    /** Last Updated Timestamp 2009-09-21 08:45:01.0 */
    public static final long updatedMS = 1253519101000L;
    /** AD_Table_ID=1000147 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_ImportsResult");
        
    }
    ;
    
    /** TableName=XX_VLO_ImportsResult */
    public static final String Table_Name="XX_VLO_ImportsResult";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"XX_VLO_ImportsResult");
    

    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner Business Partner */
    public void setC_BPartner (String C_BPartner)
    {
        set_Value ("C_BPartner", C_BPartner);
        
    }
    
    /** Get Business Partner.
    @return Business Partner */
    public String getC_BPartner() 
    {
        return (String)get_Value("C_BPartner");
        
    }
    
    /** Set Date Promised.
    @param DatePromised Date Order was promised */
    public void setDatePromised (Timestamp DatePromised)
    {
        set_ValueNoCheck ("DatePromised", DatePromised);
        
    }
    
    /** Get Date Promised.
    @return Date Order was promised */
    public Timestamp getDatePromised() 
    {
        return (Timestamp)get_Value("DatePromised");
        
    }
    
    /** Set Multiply Rate.
    @param MultiplyRate Multiply Rate */
    public void setMultiplyRate (int MultiplyRate)
    {
        set_Value ("MultiplyRate", Integer.valueOf(MultiplyRate));
        
    }
    
    /** Get Multiply Rate.
    @return Multiply Rate */
    public int getMultiplyRate() 
    {
        return get_ValueAsInt("MultiplyRate");
        
    }
    
    /** Set Customs Agent Real Amount.
    @param XX_AARealAmount Customs Agent Real Amount */
    public void setXX_AARealAmount (int XX_AARealAmount)
    {
        set_Value ("XX_AARealAmount", Integer.valueOf(XX_AARealAmount));
        
    }
    
    /** Get Customs Agent Real Amount.
    @return Customs Agent Real Amount */
    public int getXX_AARealAmount() 
    {
        return get_ValueAsInt("XX_AARealAmount");
        
    }
    
    /** Set Agent Load Invoice Amount.
    @param XX_AGENTLOADINVOICEAMOUNT Agent Load Invoice Amount */
    public void setXX_AGENTLOADINVOICEAMOUNT (int XX_AGENTLOADINVOICEAMOUNT)
    {
        set_Value ("XX_AGENTLOADINVOICEAMOUNT", Integer.valueOf(XX_AGENTLOADINVOICEAMOUNT));
        
    }
    
    /** Get Agent Load Invoice Amount.
    @return Agent Load Invoice Amount */
    public int getXX_AGENTLOADINVOICEAMOUNT() 
    {
        return get_ValueAsInt("XX_AGENTLOADINVOICEAMOUNT");
        
    }
    
    /** Set Dollar Total Amount.
    @param XX_AMOUNTTOTALDOL Dollar Total Amount */
    public void setXX_AMOUNTTOTALDOL (int XX_AMOUNTTOTALDOL)
    {
        set_Value ("XX_AMOUNTTOTALDOL", Integer.valueOf(XX_AMOUNTTOTALDOL));
        
    }
    
    /** Get Dollar Total Amount.
    @return Dollar Total Amount */
    public int getXX_AMOUNTTOTALDOL() 
    {
        return get_ValueAsInt("XX_AMOUNTTOTALDOL");
        
    }
    
    /** Set Eur. Total Amount.
    @param XX_AMOUNTTOTALEUR Eur. Total Amount */
    public void setXX_AMOUNTTOTALEUR (int XX_AMOUNTTOTALEUR)
    {
        set_Value ("XX_AMOUNTTOTALEUR", Integer.valueOf(XX_AMOUNTTOTALEUR));
        
    }
    
    /** Get Eur. Total Amount.
    @return Eur. Total Amount */
    public int getXX_AMOUNTTOTALEUR() 
    {
        return get_ValueAsInt("XX_AMOUNTTOTALEUR");
        
    }
    
    /** Set Fecha de Llegada.
    @param XX_ArrivalDate Fecha de Llegada */
    public void setXX_ArrivalDate (Timestamp XX_ArrivalDate)
    {
        set_ValueNoCheck ("XX_ArrivalDate", XX_ArrivalDate);
        
    }
    
    /** Get Fecha de Llegada.
    @return Fecha de Llegada */
    public Timestamp getXX_ArrivalDate() 
    {
        return (Timestamp)get_Value("XX_ArrivalDate");
        
    }
    
    /** Set Arrival Port.
    @param XX_ARRIVALPORT Arrival Port */
    public void setXX_ARRIVALPORT (String XX_ARRIVALPORT)
    {
        set_Value ("XX_ARRIVALPORT", XX_ARRIVALPORT);
        
    }
    
    /** Get Arrival Port.
    @return Arrival Port */
    public String getXX_ARRIVALPORT() 
    {
        return (String)get_Value("XX_ARRIVALPORT");
        
    }
    
    /** Set Cargo Agent Delivery Estimated Date .
    @param XX_CARAGENTDELIVESTEMEDDATE Cargo Agent Delivery Estimated Date  */
    public void setXX_CARAGENTDELIVESTEMEDDATE (Timestamp XX_CARAGENTDELIVESTEMEDDATE)
    {
        set_ValueNoCheck ("XX_CARAGENTDELIVESTEMEDDATE", XX_CARAGENTDELIVESTEMEDDATE);
        
    }
    
    /** Get Cargo Agent Delivery Estimated Date .
    @return Cargo Agent Delivery Estimated Date  */
    public Timestamp getXX_CARAGENTDELIVESTEMEDDATE() 
    {
        return (Timestamp)get_Value("XX_CARAGENTDELIVESTEMEDDATE");
        
    }
    
    /** Set Cargo Agent Delivery Real Date .
    @param XX_CARAGENTDELIVREALDATE Cargo Agent Delivery Real Date  */
    public void setXX_CARAGENTDELIVREALDATE (Timestamp XX_CARAGENTDELIVREALDATE)
    {
        set_ValueNoCheck ("XX_CARAGENTDELIVREALDATE", XX_CARAGENTDELIVREALDATE);
        
    }
    
    /** Get Cargo Agent Delivery Real Date .
    @return Cargo Agent Delivery Real Date  */
    public Timestamp getXX_CARAGENTDELIVREALDATE() 
    {
        return (Timestamp)get_Value("XX_CARAGENTDELIVREALDATE");
        
    }
    
    /** Set Cargo Agent Invoice Emission Date .
    @param XX_CARGOAGENTINVOEMISIONDATE Cargo Agent Invoice Emission Date  */
    public void setXX_CARGOAGENTINVOEMISIONDATE (Timestamp XX_CARGOAGENTINVOEMISIONDATE)
    {
        set_ValueNoCheck ("XX_CARGOAGENTINVOEMISIONDATE", XX_CARGOAGENTINVOEMISIONDATE);
        
    }
    
    /** Get Cargo Agent Invoice Emission Date .
    @return Cargo Agent Invoice Emission Date  */
    public Timestamp getXX_CARGOAGENTINVOEMISIONDATE() 
    {
        return (Timestamp)get_Value("XX_CARGOAGENTINVOEMISIONDATE");
        
    }
    
    /** Set Cargo Agent Invoice .
    @param XX_CARGOAGENTINVOICE Cargo Agent Invoice  */
    public void setXX_CARGOAGENTINVOICE (int XX_CARGOAGENTINVOICE)
    {
        set_Value ("XX_CARGOAGENTINVOICE", Integer.valueOf(XX_CARGOAGENTINVOICE));
        
    }
    
    /** Get Cargo Agent Invoice .
    @return Cargo Agent Invoice  */
    public int getXX_CARGOAGENTINVOICE() 
    {
        return get_ValueAsInt("XX_CARGOAGENTINVOICE");
        
    }
    
    /** Set Cargo Agent Invoice Reception Date .
    @param XX_CARGOAGENTINVORECEPDATE Cargo Agent Invoice Reception Date  */
    public void setXX_CARGOAGENTINVORECEPDATE (Timestamp XX_CARGOAGENTINVORECEPDATE)
    {
        set_ValueNoCheck ("XX_CARGOAGENTINVORECEPDATE", XX_CARGOAGENTINVORECEPDATE);
        
    }
    
    /** Get Cargo Agent Invoice Reception Date .
    @return Cargo Agent Invoice Reception Date  */
    public Timestamp getXX_CARGOAGENTINVORECEPDATE() 
    {
        return (Timestamp)get_Value("XX_CARGOAGENTINVORECEPDATE");
        
    }
    
    /** Set Cargo Agent Name .
    @param XX_CARGOANGENTNAME Cargo Agent Name  */
    public void setXX_CARGOANGENTNAME (String XX_CARGOANGENTNAME)
    {
        set_Value ("XX_CARGOANGENTNAME", XX_CARGOANGENTNAME);
        
    }
    
    /** Get Cargo Agent Name .
    @return Cargo Agent Name  */
    public String getXX_CARGOANGENTNAME() 
    {
        return (String)get_Value("XX_CARGOANGENTNAME");
        
    }
    
    /** Set Code Catalog.
    @param XX_CATALOGCODE Code Catalog */
    public void setXX_CATALOGCODE (String XX_CATALOGCODE)
    {
        set_Value ("XX_CATALOGCODE", XX_CATALOGCODE);
        
    }
    
    /** Get Code Catalog.
    @return Code Catalog */
    public String getXX_CATALOGCODE() 
    {
        return (String)get_Value("XX_CATALOGCODE");
        
    }
    
    /** Set Category.
    @param XX_CATEGORY Category */
    public void setXX_CATEGORY (String XX_CATEGORY)
    {
        set_Value ("XX_CATEGORY", XX_CATEGORY);
        
    }
    
    /** Get Category.
    @return Category */
    public String getXX_CATEGORY() 
    {
        return (String)get_Value("XX_CATEGORY");
        
    }
    
    /** Set CD Estimated Arrival Date.
    @param XX_CDARRIVALESTEEMEDDATE CD Estimated Arrival Date */
    public void setXX_CDARRIVALESTEEMEDDATE (Timestamp XX_CDARRIVALESTEEMEDDATE)
    {
        set_ValueNoCheck ("XX_CDARRIVALESTEEMEDDATE", XX_CDARRIVALESTEEMEDDATE);
        
    }
    
    /** Get CD Estimated Arrival Date.
    @return CD Estimated Arrival Date */
    public Timestamp getXX_CDARRIVALESTEEMEDDATE() 
    {
        return (Timestamp)get_Value("XX_CDARRIVALESTEEMEDDATE");
        
    }
    
    /** Set CD Arrival Real Date .
    @param XX_CDARRIVALREALDATE CD Arrival Real Date  */
    public void setXX_CDARRIVALREALDATE (Timestamp XX_CDARRIVALREALDATE)
    {
        set_ValueNoCheck ("XX_CDARRIVALREALDATE", XX_CDARRIVALREALDATE);
        
    }
    
    /** Get CD Arrival Real Date .
    @return CD Arrival Real Date  */
    public Timestamp getXX_CDARRIVALREALDATE() 
    {
        return (Timestamp)get_Value("XX_CDARRIVALREALDATE");
        
    }
    
    /** Set Check Date.
    @param XX_CHECKDATE Check Date */
    public void setXX_CHECKDATE (Timestamp XX_CHECKDATE)
    {
        set_ValueNoCheck ("XX_CHECKDATE", XX_CHECKDATE);
        
    }
    
    /** Get Check Date.
    @return Check Date */
    public Timestamp getXX_CHECKDATE() 
    {
        return (Timestamp)get_Value("XX_CHECKDATE");
        
    }
    
    /** Set Condition Payment.
    @param XX_CONDITIONPAYMENT Condition Payment */
    public void setXX_CONDITIONPAYMENT (String XX_CONDITIONPAYMENT)
    {
        set_Value ("XX_CONDITIONPAYMENT", XX_CONDITIONPAYMENT);
        
    }
    
    /** Get Condition Payment.
    @return Condition Payment */
    public String getXX_CONDITIONPAYMENT() 
    {
        return (String)get_Value("XX_CONDITIONPAYMENT");
        
    }
    
    /** Set Consignee.
    @param XX_CONSIGNEE Consignee */
    public void setXX_CONSIGNEE (String XX_CONSIGNEE)
    {
        set_Value ("XX_CONSIGNEE", XX_CONSIGNEE);
        
    }
    
    /** Get Consignee.
    @return Consignee */
    public String getXX_CONSIGNEE() 
    {
        return (String)get_Value("XX_CONSIGNEE");
        
    }
    
    /** Set Country.
    @param XX_Country Country */
    public void setXX_Country (String XX_Country)
    {
        set_Value ("XX_Country", XX_Country);
        
    }
    
    /** Get Country.
    @return Country */
    public String getXX_Country() 
    {
        return (String)get_Value("XX_Country");
        
    }
    
    /** Set Customs Agent Invoice Emission Date .
    @param XX_CUSTAGENINVOIEMISIONDATE Customs Agent Invoice Emission Date  */
    public void setXX_CUSTAGENINVOIEMISIONDATE (Timestamp XX_CUSTAGENINVOIEMISIONDATE)
    {
        set_ValueNoCheck ("XX_CUSTAGENINVOIEMISIONDATE", XX_CUSTAGENINVOIEMISIONDATE);
        
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
        set_ValueNoCheck ("XX_CUSTAGENINVOIRECEPTDATE", XX_CUSTAGENINVOIRECEPTDATE);
        
    }
    
    /** Get Customs Agent Invoice Reception Date .
    @return Customs Agent Invoice Reception Date  */
    public Timestamp getXX_CUSTAGENINVOIRECEPTDATE() 
    {
        return (Timestamp)get_Value("XX_CUSTAGENINVOIRECEPTDATE");
        
    }
    
    /** Set Custom Agent Invoice .
    @param XX_CUSTOMAGENTINVOICE Custom Agent Invoice  */
    public void setXX_CUSTOMAGENTINVOICE (int XX_CUSTOMAGENTINVOICE)
    {
        set_Value ("XX_CUSTOMAGENTINVOICE", Integer.valueOf(XX_CUSTOMAGENTINVOICE));
        
    }
    
    /** Get Custom Agent Invoice .
    @return Custom Agent Invoice  */
    public int getXX_CUSTOMAGENTINVOICE() 
    {
        return get_ValueAsInt("XX_CUSTOMAGENTINVOICE");
        
    }
    
    /** Set Custom Rights Cancel Date .
    @param XX_CUSTOMRIGHTSCANCELDATE Custom Rights Cancel Date  */
    public void setXX_CUSTOMRIGHTSCANCELDATE (Timestamp XX_CUSTOMRIGHTSCANCELDATE)
    {
        set_ValueNoCheck ("XX_CUSTOMRIGHTSCANCELDATE", XX_CUSTOMRIGHTSCANCELDATE);
        
    }
    
    /** Get Custom Rights Cancel Date .
    @return Custom Rights Cancel Date  */
    public Timestamp getXX_CUSTOMRIGHTSCANCELDATE() 
    {
        return (Timestamp)get_Value("XX_CUSTOMRIGHTSCANCELDATE");
        
    }
    
    /** Set Customs Agent Anticipation Amount .
    @param XX_CUSTOMSAGENTANTICAMOUNT Customs Agent Anticipation Amount  */
    public void setXX_CUSTOMSAGENTANTICAMOUNT (int XX_CUSTOMSAGENTANTICAMOUNT)
    {
        set_Value ("XX_CUSTOMSAGENTANTICAMOUNT", Integer.valueOf(XX_CUSTOMSAGENTANTICAMOUNT));
        
    }
    
    /** Get Customs Agent Anticipation Amount .
    @return Customs Agent Anticipation Amount  */
    public int getXX_CUSTOMSAGENTANTICAMOUNT() 
    {
        return get_ValueAsInt("XX_CUSTOMSAGENTANTICAMOUNT");
        
    }
    
    /** Set Customs Agent Name .
    @param XX_CUSTOMSAGENTNAME Customs Agent Name  */
    public void setXX_CUSTOMSAGENTNAME (String XX_CUSTOMSAGENTNAME)
    {
        set_Value ("XX_CUSTOMSAGENTNAME", XX_CUSTOMSAGENTNAME);
        
    }
    
    /** Get Customs Agent Name .
    @return Customs Agent Name  */
    public String getXX_CUSTOMSAGENTNAME() 
    {
        return (String)get_Value("XX_CUSTOMSAGENTNAME");
        
    }
    
    /** Set Department.
    @param XX_DEPARTMENT Departamento */
    public void setXX_DEPARTMENT (String XX_DEPARTMENT)
    {
        set_Value ("XX_DEPARTMENT", XX_DEPARTMENT);
        
    }
    
    /** Get Department.
    @return Departamento */
    public String getXX_DEPARTMENT() 
    {
        return (String)get_Value("XX_DEPARTMENT");
        
    }
    
    /** Set Expense Account .
    @param XX_EXPENSEACCOUNT Expense Account  */
    public void setXX_EXPENSEACCOUNT (int XX_EXPENSEACCOUNT)
    {
        set_Value ("XX_EXPENSEACCOUNT", Integer.valueOf(XX_EXPENSEACCOUNT));
        
    }
    
    /** Get Expense Account .
    @return Expense Account  */
    public int getXX_EXPENSEACCOUNT() 
    {
        return get_ValueAsInt("XX_EXPENSEACCOUNT");
        
    }
    
    /** Set Number Guia.
    @param XX_GUIANUM Number Guia */
    public void setXX_GUIANUM (int XX_GUIANUM)
    {
        set_Value ("XX_GUIANUM", Integer.valueOf(XX_GUIANUM));
        
    }
    
    /** Get Number Guia.
    @return Number Guia */
    public int getXX_GUIANUM() 
    {
        return get_ValueAsInt("XX_GUIANUM");
        
    }
    
    /** Set Incoterms .
    @param XX_INCOTERMS Incoterms  */
    public void setXX_INCOTERMS (String XX_INCOTERMS)
    {
        set_Value ("XX_INCOTERMS", XX_INCOTERMS);
        
    }
    
    /** Get Incoterms .
    @return Incoterms  */
    public String getXX_INCOTERMS() 
    {
        return (String)get_Value("XX_INCOTERMS");
        
    }
    
    /** Set International Freigt Real Amount.
    @param XX_InterFretRealAmount International Freigt Real Amount */
    public void setXX_InterFretRealAmount (int XX_InterFretRealAmount)
    {
        set_Value ("XX_InterFretRealAmount", Integer.valueOf(XX_InterFretRealAmount));
        
    }
    
    /** Get International Freigt Real Amount.
    @return International Freigt Real Amount */
    public int getXX_InterFretRealAmount() 
    {
        return get_ValueAsInt("XX_InterFretRealAmount");
        
    }
    
    /** Set Invoice Number.
    @param XX_INVOICENRO Invoice Number */
    public void setXX_INVOICENRO (String XX_INVOICENRO)
    {
        set_Value ("XX_INVOICENRO", XX_INVOICENRO);
        
    }
    
    /** Get Invoice Number.
    @return Invoice Number */
    public String getXX_INVOICENRO() 
    {
        return (String)get_Value("XX_INVOICENRO");
        
    }
    
    /** Set National Freigt Real Amount.
    @param XX_NacFretRealAmount National Freigt Real Amount */
    public void setXX_NacFretRealAmount (int XX_NacFretRealAmount)
    {
        set_Value ("XX_NacFretRealAmount", Integer.valueOf(XX_NacFretRealAmount));
        
    }
    
    /** Get National Freigt Real Amount.
    @return National Freigt Real Amount */
    public int getXX_NacFretRealAmount() 
    {
        return get_ValueAsInt("XX_NacFretRealAmount");
        
    }
    
    /** Set National Treasury Real Amount .
    @param XX_NACTREASREALAMOUNT National Treasury Real Amount  */
    public void setXX_NACTREASREALAMOUNT (int XX_NACTREASREALAMOUNT)
    {
        set_Value ("XX_NACTREASREALAMOUNT", Integer.valueOf(XX_NACTREASREALAMOUNT));
        
    }
    
    /** Get National Treasury Real Amount .
    @return National Treasury Real Amount  */
    public int getXX_NACTREASREALAMOUNT() 
    {
        return get_ValueAsInt("XX_NACTREASREALAMOUNT");
        
    }
    
    /** Set Order Number.
    @param XX_ORDERNUM Order Number */
    public void setXX_ORDERNUM (int XX_ORDERNUM)
    {
        set_Value ("XX_ORDERNUM", Integer.valueOf(XX_ORDERNUM));
        
    }
    
    /** Get Order Number.
    @return Order Number */
    public int getXX_ORDERNUM() 
    {
        return get_ValueAsInt("XX_ORDERNUM");
        
    }
    
    /** Set PO Status.
    @param XX_OrderStatus Estado de la Orden de Compra */
    public void setXX_OrderStatus (String XX_OrderStatus)
    {
        set_Value ("XX_OrderStatus", XX_OrderStatus);
        
    }
    
    /** Get PO Status.
    @return Estado de la Orden de Compra */
    public String getXX_OrderStatus() 
    {
        return (String)get_Value("XX_OrderStatus");
        
    }
    
    /** Set Pieces Total.
    @param XX_PIECESTOTAL Pieces Total */
    public void setXX_PIECESTOTAL (int XX_PIECESTOTAL)
    {
        set_Value ("XX_PIECESTOTAL", Integer.valueOf(XX_PIECESTOTAL));
        
    }
    
    /** Get Pieces Total.
    @return Pieces Total */
    public int getXX_PIECESTOTAL() 
    {
        return get_ValueAsInt("XX_PIECESTOTAL");
        
    }
    
    /** Set SENIAT Real  Amount .
    @param XX_SENIATREALESTEEMEDAMOUNT SENIAT Real  Amount  */
    public void setXX_SENIATREALESTEEMEDAMOUNT (int XX_SENIATREALESTEEMEDAMOUNT)
    {
        set_Value ("XX_SENIATREALESTEEMEDAMOUNT", Integer.valueOf(XX_SENIATREALESTEEMEDAMOUNT));
        
    }
    
    /** Get SENIAT Real  Amount .
    @return SENIAT Real  Amount  */
    public int getXX_SENIATREALESTEEMEDAMOUNT() 
    {
        return get_ValueAsInt("XX_SENIATREALESTEEMEDAMOUNT");
        
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
    
    /** Set Shipping Port .
    @param XX_SHIPPINGPORT Shipping Port  */
    public void setXX_SHIPPINGPORT (String XX_SHIPPINGPORT)
    {
        set_Value ("XX_SHIPPINGPORT", XX_SHIPPINGPORT);
        
    }
    
    /** Get Shipping Port .
    @return Shipping Port  */
    public String getXX_SHIPPINGPORT() 
    {
        return (String)get_Value("XX_SHIPPINGPORT");
        
    }
    
    /** Set Shipping Real Date .
    @param XX_SHIPPINGREALDATE Shipping Real Date  */
    public void setXX_SHIPPINGREALDATE (Timestamp XX_SHIPPINGREALDATE)
    {
        set_ValueNoCheck ("XX_SHIPPINGREALDATE", XX_SHIPPINGREALDATE);
        
    }
    
    /** Get Shipping Real Date .
    @return Shipping Real Date  */
    public Timestamp getXX_SHIPPINGREALDATE() 
    {
        return (Timestamp)get_Value("XX_SHIPPINGREALDATE");
        
    }
    
    /** Set Estimated Shipping Date .
    @param XX_SHIPPREALESTEEMEDDATE Estimated Shipping Date  */
    public void setXX_SHIPPREALESTEEMEDDATE (Timestamp XX_SHIPPREALESTEEMEDDATE)
    {
        set_ValueNoCheck ("XX_SHIPPREALESTEEMEDDATE", XX_SHIPPREALESTEEMEDDATE);
        
    }
    
    /** Get Estimated Shipping Date .
    @return Estimated Shipping Date  */
    public Timestamp getXX_SHIPPREALESTEEMEDDATE() 
    {
        return (Timestamp)get_Value("XX_SHIPPREALESTEEMEDDATE");
        
    }
    
    /** Set SIDUNEA Transmission Esteemed Date .
    @param XX_SINDUNEATRANSESTEEMEDDATE SIDUNEA Transmission Esteemed Date  */
    public void setXX_SINDUNEATRANSESTEEMEDDATE (Timestamp XX_SINDUNEATRANSESTEEMEDDATE)
    {
        set_ValueNoCheck ("XX_SINDUNEATRANSESTEEMEDDATE", XX_SINDUNEATRANSESTEEMEDDATE);
        
    }
    
    /** Get SIDUNEA Transmission Esteemed Date .
    @return SIDUNEA Transmission Esteemed Date  */
    public Timestamp getXX_SINDUNEATRANSESTEEMEDDATE() 
    {
        return (Timestamp)get_Value("XX_SINDUNEATRANSESTEEMEDDATE");
        
    }
    
    /** Set SINDUNEA Transmission Real Date .
    @param XX_SINDUNEATRANSREALDATE SINDUNEA Transmission Real Date  */
    public void setXX_SINDUNEATRANSREALDATE (Timestamp XX_SINDUNEATRANSREALDATE)
    {
        set_ValueNoCheck ("XX_SINDUNEATRANSREALDATE", XX_SINDUNEATRANSREALDATE);
        
    }
    
    /** Get SINDUNEA Transmission Real Date .
    @return SINDUNEA Transmission Real Date  */
    public Timestamp getXX_SINDUNEATRANSREALDATE() 
    {
        return (Timestamp)get_Value("XX_SINDUNEATRANSREALDATE");
        
    }
    
    /** Set Total AS.
    @param XX_TOTALAS Total AS */
    public void setXX_TOTALAS (int XX_TOTALAS)
    {
        set_Value ("XX_TOTALAS", Integer.valueOf(XX_TOTALAS));
        
    }
    
    /** Get Total AS.
    @return Total AS */
    public int getXX_TOTALAS() 
    {
        return get_ValueAsInt("XX_TOTALAS");
        
    }
    
    /** Set Total Day.
    @param XX_TOTALDAY Total Day */
    public void setXX_TOTALDAY (int XX_TOTALDAY)
    {
        set_Value ("XX_TOTALDAY", Integer.valueOf(XX_TOTALDAY));
        
    }
    
    /** Get Total Day.
    @return Total Day */
    public int getXX_TOTALDAY() 
    {
        return get_ValueAsInt("XX_TOTALDAY");
        
    }
    
    /** Set Total Day Vendor Vs CD.
    @param XX_TOTALDAYPROVCD Total Day Vendor Vs CD */
    public void setXX_TOTALDAYPROVCD (int XX_TOTALDAYPROVCD)
    {
        set_ValueNoCheck ("XX_TOTALDAYPROVCD", Integer.valueOf(XX_TOTALDAYPROVCD));
        
    }
    
    /** Get Total Day Vendor Vs CD.
    @return Total Day Vendor Vs CD */
    public int getXX_TOTALDAYPROVCD() 
    {
        return get_ValueAsInt("XX_TOTALDAYPROVCD");
        
    }
    
    /** Set Total Dollar Invoice.
    @param XX_TOTALDOLINVOICE Total Dollar Invoice */
    public void setXX_TOTALDOLINVOICE (int XX_TOTALDOLINVOICE)
    {
        set_Value ("XX_TOTALDOLINVOICE", Integer.valueOf(XX_TOTALDOLINVOICE));
        
    }
    
    /** Get Total Dollar Invoice.
    @return Total Dollar Invoice */
    public int getXX_TOTALDOLINVOICE() 
    {
        return get_ValueAsInt("XX_TOTALDOLINVOICE");
        
    }
    
    /** Set Total Eur. Invoice.
    @param XX_TOTALEURINVOICE Total Eur. Invoice */
    public void setXX_TOTALEURINVOICE (int XX_TOTALEURINVOICE)
    {
        set_Value ("XX_TOTALEURINVOICE", Integer.valueOf(XX_TOTALEURINVOICE));
        
    }
    
    /** Get Total Eur. Invoice.
    @return Total Eur. Invoice */
    public int getXX_TOTALEURINVOICE() 
    {
        return get_ValueAsInt("XX_TOTALEURINVOICE");
        
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
    
    /** Set XX_VLO_IMPORTSRESULT_ID.
    @param XX_VLO_IMPORTSRESULT_ID XX_VLO_IMPORTSRESULT_ID */
    public void setXX_VLO_IMPORTSRESULT_ID (int XX_VLO_IMPORTSRESULT_ID)
    {
        if (XX_VLO_IMPORTSRESULT_ID < 1) throw new IllegalArgumentException ("XX_VLO_IMPORTSRESULT_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_IMPORTSRESULT_ID", Integer.valueOf(XX_VLO_IMPORTSRESULT_ID));
        
    }
    
    /** Get XX_VLO_IMPORTSRESULT_ID.
    @return XX_VLO_IMPORTSRESULT_ID */
    public int getXX_VLO_IMPORTSRESULT_ID() 
    {
        return get_ValueAsInt("XX_VLO_IMPORTSRESULT_ID");
        
    }
    
    /** Set Estimated Arrival Date To Venezuela.
    @param XX_VZLAARRIVALESTEMEDDATE Estimated Arrival Date To Venezuela */
    public void setXX_VZLAARRIVALESTEMEDDATE (Timestamp XX_VZLAARRIVALESTEMEDDATE)
    {
        set_ValueNoCheck ("XX_VZLAARRIVALESTEMEDDATE", XX_VZLAARRIVALESTEMEDDATE);
        
    }
    
    /** Get Estimated Arrival Date To Venezuela.
    @return Estimated Arrival Date To Venezuela */
    public Timestamp getXX_VZLAARRIVALESTEMEDDATE() 
    {
        return (Timestamp)get_Value("XX_VZLAARRIVALESTEMEDDATE");
        
    }
    
    /** Set VZLA Arrival Real Date.
    @param XX_VZLAARRIVALREALDATE VZLA Arrival Real Date */
    public void setXX_VZLAARRIVALREALDATE (Timestamp XX_VZLAARRIVALREALDATE)
    {
        set_ValueNoCheck ("XX_VZLAARRIVALREALDATE", XX_VZLAARRIVALREALDATE);
        
    }
    
    /** Get VZLA Arrival Real Date.
    @return VZLA Arrival Real Date */
    public Timestamp getXX_VZLAARRIVALREALDATE() 
    {
        return (Timestamp)get_Value("XX_VZLAARRIVALREALDATE");
        
    }
    
    
}
