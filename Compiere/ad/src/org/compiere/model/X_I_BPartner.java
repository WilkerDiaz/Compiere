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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for I_BPartner
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.2 Dev - $Id: X_I_BPartner.java 8524 2010-03-05 23:23:31Z nnayak $ */
public class X_I_BPartner extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_BPartner_ID id
    @param trx transaction
    */
    public X_I_BPartner (Ctx ctx, int I_BPartner_ID, Trx trx)
    {
        super (ctx, I_BPartner_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_BPartner_ID == 0)
        {
            setI_BPartner_ID (0);
            setI_IsImported (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_BPartner (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27549957661789L;
    /** Last Updated Timestamp 2010-03-05 15:39:05.0 */
    public static final long updatedMS = 1267832345000L;
    /** AD_Table_ID=533 */
    public static final int Table_ID=533;
    
    /** TableName=I_BPartner */
    public static final String Table_Name="I_BPartner";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Address 1.
    @param Address1 Address line 1 for this location */
    public void setAddress1 (String Address1)
    {
        set_Value ("Address1", Address1);
        
    }
    
    /** Get Address 1.
    @return Address line 1 for this location */
    public String getAddress1() 
    {
        return (String)get_Value("Address1");
        
    }
    
    /** Set Address 2.
    @param Address2 Address line 2 for this location */
    public void setAddress2 (String Address2)
    {
        set_Value ("Address2", Address2);
        
    }
    
    /** Get Address 2.
    @return Address line 2 for this location */
    public String getAddress2() 
    {
        return (String)get_Value("Address2");
        
    }
    
    /** Set Address 3.
    @param Address3 Address Line 3 for the location */
    public void setAddress3 (String Address3)
    {
        set_Value ("Address3", Address3);
        
    }
    
    /** Get Address 3.
    @return Address Line 3 for the location */
    public String getAddress3() 
    {
        return (String)get_Value("Address3");
        
    }
    
    /** Set Address 4.
    @param Address4 Address Line 4 for the location */
    public void setAddress4 (String Address4)
    {
        set_Value ("Address4", Address4);
        
    }
    
    /** Get Address 4.
    @return Address Line 4 for the location */
    public String getAddress4() 
    {
        return (String)get_Value("Address4");
        
    }
    
    /** Set BP Contact Greeting.
    @param BPContactGreeting Greeting for Business Partner Contact */
    public void setBPContactGreeting (String BPContactGreeting)
    {
        set_Value ("BPContactGreeting", BPContactGreeting);
        
    }
    
    /** Get BP Contact Greeting.
    @return Greeting for Business Partner Contact */
    public String getBPContactGreeting() 
    {
        return (String)get_Value("BPContactGreeting");
        
    }
    
    /** Set Location Fax.
    @param BPLFax Facsimile number */
    public void setBPLFax (String BPLFax)
    {
        set_Value ("BPLFax", BPLFax);
        
    }
    
    /** Get Location Fax.
    @return Facsimile number */
    public String getBPLFax() 
    {
        return (String)get_Value("BPLFax");
        
    }
    
    /** Set Location Phone.
    @param BPLPhone Identifies a telephone number */
    public void setBPLPhone (String BPLPhone)
    {
        set_Value ("BPLPhone", BPLPhone);
        
    }
    
    /** Get Location Phone.
    @return Identifies a telephone number */
    public String getBPLPhone() 
    {
        return (String)get_Value("BPLPhone");
        
    }
    
    /** Set Location 2nd Phone.
    @param BPLPhone2 Identifies an alternate telephone number. */
    public void setBPLPhone2 (String BPLPhone2)
    {
        set_Value ("BPLPhone2", BPLPhone2);
        
    }
    
    /** Get Location 2nd Phone.
    @return Identifies an alternate telephone number. */
    public String getBPLPhone2() 
    {
        return (String)get_Value("BPLPhone2");
        
    }
    
    /** Set Birthday.
    @param Birthday Birthday or Anniversary day */
    public void setBirthday (Timestamp Birthday)
    {
        set_Value ("Birthday", Birthday);
        
    }
    
    /** Get Birthday.
    @return Birthday or Anniversary day */
    public Timestamp getBirthday() 
    {
        return (Timestamp)get_Value("Birthday");
        
    }
    
    /** Set Business Partner Group.
    @param C_BP_Group_ID Business Partner Group */
    public void setC_BP_Group_ID (int C_BP_Group_ID)
    {
        if (C_BP_Group_ID <= 0) set_Value ("C_BP_Group_ID", null);
        else
        set_Value ("C_BP_Group_ID", Integer.valueOf(C_BP_Group_ID));
        
    }
    
    /** Get Business Partner Group.
    @return Business Partner Group */
    public int getC_BP_Group_ID() 
    {
        return get_ValueAsInt("C_BP_Group_ID");
        
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
        else
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Consolidation Reference.
    @param C_ConsolidationReference_ID This is a reference value that can be created and then associated with a Business Partner */
    public void setC_ConsolidationReference_ID (int C_ConsolidationReference_ID)
    {
        if (C_ConsolidationReference_ID <= 0) set_Value ("C_ConsolidationReference_ID", null);
        else
        set_Value ("C_ConsolidationReference_ID", Integer.valueOf(C_ConsolidationReference_ID));
        
    }
    
    /** Get Consolidation Reference.
    @return This is a reference value that can be created and then associated with a Business Partner */
    public int getC_ConsolidationReference_ID() 
    {
        return get_ValueAsInt("C_ConsolidationReference_ID");
        
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
    
    /** Set Greeting.
    @param C_Greeting_ID Greeting to print on correspondence */
    public void setC_Greeting_ID (int C_Greeting_ID)
    {
        if (C_Greeting_ID <= 0) set_Value ("C_Greeting_ID", null);
        else
        set_Value ("C_Greeting_ID", Integer.valueOf(C_Greeting_ID));
        
    }
    
    /** Get Greeting.
    @return Greeting to print on correspondence */
    public int getC_Greeting_ID() 
    {
        return get_ValueAsInt("C_Greeting_ID");
        
    }
    
    /** Set Region.
    @param C_Region_ID Identifies a geographical Region */
    public void setC_Region_ID (int C_Region_ID)
    {
        if (C_Region_ID <= 0) set_Value ("C_Region_ID", null);
        else
        set_Value ("C_Region_ID", Integer.valueOf(C_Region_ID));
        
    }
    
    /** Get Region.
    @return Identifies a geographical Region */
    public int getC_Region_ID() 
    {
        return get_ValueAsInt("C_Region_ID");
        
    }
    
    /** Set City Name.
    @param City Identifies a City */
    public void setCity (String City)
    {
        set_Value ("City", City);
        
    }
    
    /** Get City Name.
    @return Identifies a City */
    public String getCity() 
    {
        return (String)get_Value("City");
        
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
    
    /** Set Consolidation Reference Name.
    @param ConsolidationRefName Consolidation Reference Name */
    public void setConsolidationRefName (String ConsolidationRefName)
    {
        set_Value ("ConsolidationRefName", ConsolidationRefName);
        
    }
    
    /** Get Consolidation Reference Name.
    @return Consolidation Reference Name */
    public String getConsolidationRefName() 
    {
        return (String)get_Value("ConsolidationRefName");
        
    }
    
    /** Set Contact Description.
    @param ContactDescription Description of Contact */
    public void setContactDescription (String ContactDescription)
    {
        set_Value ("ContactDescription", ContactDescription);
        
    }
    
    /** Get Contact Description.
    @return Description of Contact */
    public String getContactDescription() 
    {
        return (String)get_Value("ContactDescription");
        
    }
    
    /** Set Contact Name.
    @param ContactName Business Partner Contact Name */
    public void setContactName (String ContactName)
    {
        set_Value ("ContactName", ContactName);
        
    }
    
    /** Get Contact Name.
    @return Business Partner Contact Name */
    public String getContactName() 
    {
        return (String)get_Value("ContactName");
        
    }
    
    /** Set ISO Country Code.
    @param CountryCode Upper-case two-letter alphabetic ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html */
    public void setCountryCode (String CountryCode)
    {
        set_Value ("CountryCode", CountryCode);
        
    }
    
    /** Get ISO Country Code.
    @return Upper-case two-letter alphabetic ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html */
    public String getCountryCode() 
    {
        return (String)get_Value("CountryCode");
        
    }
    
    /** Set D-U-N-S.
    @param DUNS Creditor Check (Dun & Bradstreet) Number */
    public void setDUNS (String DUNS)
    {
        set_Value ("DUNS", DUNS);
        
    }
    
    /** Get D-U-N-S.
    @return Creditor Check (Dun & Bradstreet) Number */
    public String getDUNS() 
    {
        return (String)get_Value("DUNS");
        
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
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        set_Value ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
    }
    
    /** Set Fax.
    @param Fax Facsimile number */
    public void setFax (String Fax)
    {
        set_Value ("Fax", Fax);
        
    }
    
    /** Get Fax.
    @return Facsimile number */
    public String getFax() 
    {
        return (String)get_Value("Fax");
        
    }
    
    /** Set Group Key.
    @param GroupValue Business Partner Group Key */
    public void setGroupValue (String GroupValue)
    {
        set_Value ("GroupValue", GroupValue);
        
    }
    
    /** Get Group Key.
    @return Business Partner Group Key */
    public String getGroupValue() 
    {
        return (String)get_Value("GroupValue");
        
    }
    
    /** Set Import Business Partner.
    @param I_BPartner_ID Import Business Partner */
    public void setI_BPartner_ID (int I_BPartner_ID)
    {
        if (I_BPartner_ID < 1) throw new IllegalArgumentException ("I_BPartner_ID is mandatory.");
        set_ValueNoCheck ("I_BPartner_ID", Integer.valueOf(I_BPartner_ID));
        
    }
    
    /** Get Import Business Partner.
    @return Import Business Partner */
    public int getI_BPartner_ID() 
    {
        return get_ValueAsInt("I_BPartner_ID");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set Interest Area.
    @param InterestAreaName Name of the Interest Area */
    public void setInterestAreaName (String InterestAreaName)
    {
        set_Value ("InterestAreaName", InterestAreaName);
        
    }
    
    /** Get Interest Area.
    @return Name of the Interest Area */
    public String getInterestAreaName() 
    {
        return (String)get_Value("InterestAreaName");
        
    }
    
    /** No = N */
    public static final String ISBILLTO_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISBILLTO_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Invoice Address.
    @param IsBillTo Business Partner Invoice/Bill Address */
    public void setIsBillTo (String IsBillTo)
    {
        if (!X_Ref__YesNo.isValid(IsBillTo))
        throw new IllegalArgumentException ("IsBillTo Invalid value - " + IsBillTo + " - Reference_ID=319 - N - Y");
        set_Value ("IsBillTo", IsBillTo);
        
    }
    
    /** Get Invoice Address.
    @return Business Partner Invoice/Bill Address */
    public String getIsBillTo() 
    {
        return (String)get_Value("IsBillTo");
        
    }
    
    /** No = N */
    public static final String ISCUSTOMER_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISCUSTOMER_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Customer.
    @param IsCustomer Indicates if this Business Partner is a Customer */
    public void setIsCustomer (String IsCustomer)
    {
        if (!X_Ref__YesNo.isValid(IsCustomer))
        throw new IllegalArgumentException ("IsCustomer Invalid value - " + IsCustomer + " - Reference_ID=319 - N - Y");
        set_Value ("IsCustomer", IsCustomer);
        
    }
    
    /** Get Customer.
    @return Indicates if this Business Partner is a Customer */
    public String getIsCustomer() 
    {
        return (String)get_Value("IsCustomer");
        
    }
    
    /** No = N */
    public static final String ISEMPLOYEE_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISEMPLOYEE_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Employee.
    @param IsEmployee Indicates if this Business Partner is an employee */
    public void setIsEmployee (String IsEmployee)
    {
        if (!X_Ref__YesNo.isValid(IsEmployee))
        throw new IllegalArgumentException ("IsEmployee Invalid value - " + IsEmployee + " - Reference_ID=319 - N - Y");
        set_Value ("IsEmployee", IsEmployee);
        
    }
    
    /** Get Employee.
    @return Indicates if this Business Partner is an employee */
    public String getIsEmployee() 
    {
        return (String)get_Value("IsEmployee");
        
    }
    
    /** No = N */
    public static final String ISPAYFROM_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISPAYFROM_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Pay-From Address.
    @param IsPayFrom Business Partner pays from that address and we'll send dunning letters there */
    public void setIsPayFrom (String IsPayFrom)
    {
        if (!X_Ref__YesNo.isValid(IsPayFrom))
        throw new IllegalArgumentException ("IsPayFrom Invalid value - " + IsPayFrom + " - Reference_ID=319 - N - Y");
        set_Value ("IsPayFrom", IsPayFrom);
        
    }
    
    /** Get Pay-From Address.
    @return Business Partner pays from that address and we'll send dunning letters there */
    public String getIsPayFrom() 
    {
        return (String)get_Value("IsPayFrom");
        
    }
    
    /** No = N */
    public static final String ISREMITTO_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISREMITTO_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Remit-To Address.
    @param IsRemitTo Business Partner payment address */
    public void setIsRemitTo (String IsRemitTo)
    {
        if (!X_Ref__YesNo.isValid(IsRemitTo))
        throw new IllegalArgumentException ("IsRemitTo Invalid value - " + IsRemitTo + " - Reference_ID=319 - N - Y");
        set_Value ("IsRemitTo", IsRemitTo);
        
    }
    
    /** Get Remit-To Address.
    @return Business Partner payment address */
    public String getIsRemitTo() 
    {
        return (String)get_Value("IsRemitTo");
        
    }
    
    /** No = N */
    public static final String ISSHIPTO_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISSHIPTO_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Ship Address.
    @param IsShipTo Business Partner Shipment Address */
    public void setIsShipTo (String IsShipTo)
    {
        if (!X_Ref__YesNo.isValid(IsShipTo))
        throw new IllegalArgumentException ("IsShipTo Invalid value - " + IsShipTo + " - Reference_ID=319 - N - Y");
        set_Value ("IsShipTo", IsShipTo);
        
    }
    
    /** Get Ship Address.
    @return Business Partner Shipment Address */
    public String getIsShipTo() 
    {
        return (String)get_Value("IsShipTo");
        
    }
    
    /** No = N */
    public static final String ISVENDOR_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISVENDOR_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Vendor.
    @param IsVendor Indicates if this Business Partner is a Vendor */
    public void setIsVendor (String IsVendor)
    {
        if (!X_Ref__YesNo.isValid(IsVendor))
        throw new IllegalArgumentException ("IsVendor Invalid value - " + IsVendor + " - Reference_ID=319 - N - Y");
        set_Value ("IsVendor", IsVendor);
        
    }
    
    /** Get Vendor.
    @return Indicates if this Business Partner is a Vendor */
    public String getIsVendor() 
    {
        return (String)get_Value("IsVendor");
        
    }
    
    /** Set NAICS/SIC.
    @param NAICS Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html */
    public void setNAICS (String NAICS)
    {
        set_Value ("NAICS", NAICS);
        
    }
    
    /** Get NAICS/SIC.
    @return Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html */
    public String getNAICS() 
    {
        return (String)get_Value("NAICS");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Name 2.
    @param Name2 Additional Name */
    public void setName2 (String Name2)
    {
        set_Value ("Name2", Name2);
        
    }
    
    /** Get Name 2.
    @return Additional Name */
    public String getName2() 
    {
        return (String)get_Value("Name2");
        
    }
    
    /** Set Organization Name.
    @param OrgName Name of the Organization */
    public void setOrgName (String OrgName)
    {
        set_Value ("OrgName", OrgName);
        
    }
    
    /** Get Organization Name.
    @return Name of the Organization */
    public String getOrgName() 
    {
        return (String)get_Value("OrgName");
        
    }
    
    /** Set Organization Key.
    @param OrgValue Key of the Organization */
    public void setOrgValue (String OrgValue)
    {
        set_Value ("OrgValue", OrgValue);
        
    }
    
    /** Get Organization Key.
    @return Key of the Organization */
    public String getOrgValue() 
    {
        return (String)get_Value("OrgValue");
        
    }
    
    /** Set Password.
    @param Password Password of any length (case sensitive) */
    public void setPassword (String Password)
    {
        set_Value ("Password", Password);
        
    }
    
    /** Get Password.
    @return Password of any length (case sensitive) */
    public String getPassword() 
    {
        return (String)get_Value("Password");
        
    }
    
    /** Set Phone.
    @param Phone Identifies a telephone number */
    public void setPhone (String Phone)
    {
        set_Value ("Phone", Phone);
        
    }
    
    /** Get Phone.
    @return Identifies a telephone number */
    public String getPhone() 
    {
        return (String)get_Value("Phone");
        
    }
    
    /** Set 2nd Phone.
    @param Phone2 Identifies an alternate telephone number. */
    public void setPhone2 (String Phone2)
    {
        set_Value ("Phone2", Phone2);
        
    }
    
    /** Get 2nd Phone.
    @return Identifies an alternate telephone number. */
    public String getPhone2() 
    {
        return (String)get_Value("Phone2");
        
    }
    
    /** Set ZIP.
    @param Postal Postal code */
    public void setPostal (String Postal)
    {
        set_Value ("Postal", Postal);
        
    }
    
    /** Get ZIP.
    @return Postal code */
    public String getPostal() 
    {
        return (String)get_Value("Postal");
        
    }
    
    /** Set -.
    @param Postal_Add Additional ZIP or Postal code */
    public void setPostal_Add (String Postal_Add)
    {
        set_Value ("Postal_Add", Postal_Add);
        
    }
    
    /** Get -.
    @return Additional ZIP or Postal code */
    public String getPostal_Add() 
    {
        return (String)get_Value("Postal_Add");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
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
    
    /** Set Interest Area.
    @param R_InterestArea_ID Interest Area or Topic */
    public void setR_InterestArea_ID (int R_InterestArea_ID)
    {
        if (R_InterestArea_ID <= 0) set_Value ("R_InterestArea_ID", null);
        else
        set_Value ("R_InterestArea_ID", Integer.valueOf(R_InterestArea_ID));
        
    }
    
    /** Get Interest Area.
    @return Interest Area or Topic */
    public int getR_InterestArea_ID() 
    {
        return get_ValueAsInt("R_InterestArea_ID");
        
    }
    
    /** Set Region Name.
    @param RegionName Name of the Region */
    public void setRegionName (String RegionName)
    {
        set_Value ("RegionName", RegionName);
        
    }
    
    /** Get Region Name.
    @return Name of the Region */
    public String getRegionName() 
    {
        return (String)get_Value("RegionName");
        
    }
    
    /** Set Tax ID.
    @param TaxID Tax Identification */
    public void setTaxID (String TaxID)
    {
        set_Value ("TaxID", TaxID);
        
    }
    
    /** Get Tax ID.
    @return Tax Identification */
    public String getTaxID() 
    {
        return (String)get_Value("TaxID");
        
    }
    
    /** Set Title.
    @param Title Title of the Contact */
    public void setTitle (String Title)
    {
        set_Value ("Title", Title);
        
    }
    
    /** Get Title.
    @return Title of the Contact */
    public String getTitle() 
    {
        return (String)get_Value("Title");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
        
    }
    
    
}
