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
/** Generated Model for C_Lead
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Lead.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Lead extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Lead_ID id
    @param trx transaction
    */
    public X_C_Lead (Ctx ctx, int C_Lead_ID, Trx trx)
    {
        super (ctx, C_Lead_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Lead_ID == 0)
        {
            setC_Lead_ID (0);
            setDocumentNo (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Lead (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27502698724789L;
    /** Last Updated Timestamp 2008-09-04 17:10:08.0 */
    public static final long updatedMS = 1220573408000L;
    /** AD_Table_ID=923 */
    public static final int Table_ID=923;
    
    /** TableName=C_Lead */
    public static final String Table_Name="C_Lead";
    
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
    
    /** Set Partner Name.
    @param BPName Account or Business Partner Name */
    public void setBPName (String BPName)
    {
        set_Value ("BPName", BPName);
        
    }
    
    /** Get Partner Name.
    @return Account or Business Partner Name */
    public String getBPName() 
    {
        return (String)get_Value("BPName");
        
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
    
    /** Set BP Size.
    @param C_BP_Size_ID Business Partner Size */
    public void setC_BP_Size_ID (int C_BP_Size_ID)
    {
        if (C_BP_Size_ID <= 0) set_Value ("C_BP_Size_ID", null);
        else
        set_Value ("C_BP_Size_ID", Integer.valueOf(C_BP_Size_ID));
        
    }
    
    /** Get BP Size.
    @return Business Partner Size */
    public int getC_BP_Size_ID() 
    {
        return get_ValueAsInt("C_BP_Size_ID");
        
    }
    
    /** Set BP Status.
    @param C_BP_Status_ID Business Partner Status */
    public void setC_BP_Status_ID (int C_BP_Status_ID)
    {
        if (C_BP_Status_ID <= 0) set_Value ("C_BP_Status_ID", null);
        else
        set_Value ("C_BP_Status_ID", Integer.valueOf(C_BP_Status_ID));
        
    }
    
    /** Get BP Status.
    @return Business Partner Status */
    public int getC_BP_Status_ID() 
    {
        return get_ValueAsInt("C_BP_Status_ID");
        
    }
    
    /** Set Agent.
    @param C_BPartnerSR_ID Business Partner (Agent or Sales Rep) */
    public void setC_BPartnerSR_ID (int C_BPartnerSR_ID)
    {
        if (C_BPartnerSR_ID <= 0) set_Value ("C_BPartnerSR_ID", null);
        else
        set_Value ("C_BPartnerSR_ID", Integer.valueOf(C_BPartnerSR_ID));
        
    }
    
    /** Get Agent.
    @return Business Partner (Agent or Sales Rep) */
    public int getC_BPartnerSR_ID() 
    {
        return get_ValueAsInt("C_BPartnerSR_ID");
        
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
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set City.
    @param C_City_ID City */
    public void setC_City_ID (int C_City_ID)
    {
        if (C_City_ID <= 0) set_Value ("C_City_ID", null);
        else
        set_Value ("C_City_ID", Integer.valueOf(C_City_ID));
        
    }
    
    /** Get City.
    @return City */
    public int getC_City_ID() 
    {
        return get_ValueAsInt("C_City_ID");
        
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
    
    /** Set Industry Code.
    @param C_IndustryCode_ID Business Partner Industry Classification */
    public void setC_IndustryCode_ID (int C_IndustryCode_ID)
    {
        if (C_IndustryCode_ID <= 0) set_Value ("C_IndustryCode_ID", null);
        else
        set_Value ("C_IndustryCode_ID", Integer.valueOf(C_IndustryCode_ID));
        
    }
    
    /** Get Industry Code.
    @return Business Partner Industry Classification */
    public int getC_IndustryCode_ID() 
    {
        return get_ValueAsInt("C_IndustryCode_ID");
        
    }
    
    /** Set Position.
    @param C_Job_ID Job Position */
    public void setC_Job_ID (int C_Job_ID)
    {
        if (C_Job_ID <= 0) set_Value ("C_Job_ID", null);
        else
        set_Value ("C_Job_ID", Integer.valueOf(C_Job_ID));
        
    }
    
    /** Get Position.
    @return Job Position */
    public int getC_Job_ID() 
    {
        return get_ValueAsInt("C_Job_ID");
        
    }
    
    /** Set Lead Qualification.
    @param C_LeadQualification_ID Lead Qualification evaluation */
    public void setC_LeadQualification_ID (int C_LeadQualification_ID)
    {
        if (C_LeadQualification_ID <= 0) set_Value ("C_LeadQualification_ID", null);
        else
        set_Value ("C_LeadQualification_ID", Integer.valueOf(C_LeadQualification_ID));
        
    }
    
    /** Get Lead Qualification.
    @return Lead Qualification evaluation */
    public int getC_LeadQualification_ID() 
    {
        return get_ValueAsInt("C_LeadQualification_ID");
        
    }
    
    /** Set Lead.
    @param C_Lead_ID Business Lead */
    public void setC_Lead_ID (int C_Lead_ID)
    {
        if (C_Lead_ID < 1) throw new IllegalArgumentException ("C_Lead_ID is mandatory.");
        set_ValueNoCheck ("C_Lead_ID", Integer.valueOf(C_Lead_ID));
        
    }
    
    /** Get Lead.
    @return Business Lead */
    public int getC_Lead_ID() 
    {
        return get_ValueAsInt("C_Lead_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
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
    
    /** Set Sales Region.
    @param C_SalesRegion_ID Sales coverage region */
    public void setC_SalesRegion_ID (int C_SalesRegion_ID)
    {
        if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
        else
        set_Value ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
    }
    
    /** Get Sales Region.
    @return Sales coverage region */
    public int getC_SalesRegion_ID() 
    {
        return get_ValueAsInt("C_SalesRegion_ID");
        
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
    
    /** Set Create BP.
    @param CreateBP Create BP */
    public void setCreateBP (String CreateBP)
    {
        set_Value ("CreateBP", CreateBP);
        
    }
    
    /** Get Create BP.
    @return Create BP */
    public String getCreateBP() 
    {
        return (String)get_Value("CreateBP");
        
    }
    
    /** Set Create Project.
    @param CreateProject Create Project */
    public void setCreateProject (String CreateProject)
    {
        set_Value ("CreateProject", CreateProject);
        
    }
    
    /** Get Create Project.
    @return Create Project */
    public String getCreateProject() 
    {
        return (String)get_Value("CreateProject");
        
    }
    
    /** Set Create Request.
    @param CreateRequest Create Request */
    public void setCreateRequest (String CreateRequest)
    {
        set_Value ("CreateRequest", CreateRequest);
        
    }
    
    /** Get Create Request.
    @return Create Request */
    public String getCreateRequest() 
    {
        return (String)get_Value("CreateRequest");
        
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
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Hot = 1 */
    public static final String LEADRATING_Hot = X_Ref_C_Lead_Rating.HOT.getValue();
    /** Warm = 4 */
    public static final String LEADRATING_Warm = X_Ref_C_Lead_Rating.WARM.getValue();
    /** Cold = 9 */
    public static final String LEADRATING_Cold = X_Ref_C_Lead_Rating.COLD.getValue();
    /** Set Lead Rating.
    @param LeadRating Rating of the Lead */
    public void setLeadRating (String LeadRating)
    {
        if (!X_Ref_C_Lead_Rating.isValid(LeadRating))
        throw new IllegalArgumentException ("LeadRating Invalid value - " + LeadRating + " - Reference_ID=421 - 1 - 4 - 9");
        set_Value ("LeadRating", LeadRating);
        
    }
    
    /** Get Lead Rating.
    @return Rating of the Lead */
    public String getLeadRating() 
    {
        return (String)get_Value("LeadRating");
        
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
    
    /** Set Employees.
    @param NumberEmployees Number of employees */
    public void setNumberEmployees (int NumberEmployees)
    {
        set_Value ("NumberEmployees", Integer.valueOf(NumberEmployees));
        
    }
    
    /** Get Employees.
    @return Number of employees */
    public int getNumberEmployees() 
    {
        return get_ValueAsInt("NumberEmployees");
        
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
    
    /** Set Request.
    @param R_Request_ID Request from a Business Partner or Prospect */
    public void setR_Request_ID (int R_Request_ID)
    {
        if (R_Request_ID <= 0) set_Value ("R_Request_ID", null);
        else
        set_Value ("R_Request_ID", Integer.valueOf(R_Request_ID));
        
    }
    
    /** Get Request.
    @return Request from a Business Partner or Prospect */
    public int getR_Request_ID() 
    {
        return get_ValueAsInt("R_Request_ID");
        
    }
    
    /** Set Source.
    @param R_Source_ID Source for the Lead or Request */
    public void setR_Source_ID (int R_Source_ID)
    {
        if (R_Source_ID <= 0) set_Value ("R_Source_ID", null);
        else
        set_Value ("R_Source_ID", Integer.valueOf(R_Source_ID));
        
    }
    
    /** Get Source.
    @return Source for the Lead or Request */
    public int getR_Source_ID() 
    {
        return get_ValueAsInt("R_Source_ID");
        
    }
    
    /** Set Status.
    @param R_Status_ID Request Status */
    public void setR_Status_ID (int R_Status_ID)
    {
        if (R_Status_ID <= 0) set_Value ("R_Status_ID", null);
        else
        set_Value ("R_Status_ID", Integer.valueOf(R_Status_ID));
        
    }
    
    /** Get Status.
    @return Request Status */
    public int getR_Status_ID() 
    {
        return get_ValueAsInt("R_Status_ID");
        
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
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_Value ("Remote_Addr", Remote_Addr);
        
    }
    
    /** Get Remote Addr.
    @return Remote Address */
    public String getRemote_Addr() 
    {
        return (String)get_Value("Remote_Addr");
        
    }
    
    /** Set Remote Host.
    @param Remote_Host Remote host Info */
    public void setRemote_Host (String Remote_Host)
    {
        set_Value ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID <= 0) set_Value ("SalesRep_ID", null);
        else
        set_Value ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    /** Set Sales Volume.
    @param SalesVolume Total Volume of Sales in Thousands of Base Currency */
    public void setSalesVolume (int SalesVolume)
    {
        set_Value ("SalesVolume", Integer.valueOf(SalesVolume));
        
    }
    
    /** Get Sales Volume.
    @return Total Volume of Sales in Thousands of Base Currency */
    public int getSalesVolume() 
    {
        return get_ValueAsInt("SalesVolume");
        
    }
    
    /** Set Send EMail to Contact.
    @param SendNewEMail Send new EMail to Contact */
    public void setSendNewEMail (String SendNewEMail)
    {
        set_Value ("SendNewEMail", SendNewEMail);
        
    }
    
    /** Get Send EMail to Contact.
    @return Send new EMail to Contact */
    public String getSendNewEMail() 
    {
        return (String)get_Value("SendNewEMail");
        
    }
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        set_Value ("Summary", Summary);
        
    }
    
    /** Get Summary.
    @return Textual summary of this request */
    public String getSummary() 
    {
        return (String)get_Value("Summary");
        
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
    
    /** Set URL.
    @param URL Full URL address - e.g. http://www.compiere.org */
    public void setURL (String URL)
    {
        set_Value ("URL", URL);
        
    }
    
    /** Get URL.
    @return Full URL address - e.g. http://www.compiere.org */
    public String getURL() 
    {
        return (String)get_Value("URL");
        
    }
    
    
}
