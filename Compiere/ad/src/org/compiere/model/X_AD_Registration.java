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
/** Generated Model for AD_Registration
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Registration.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Registration extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Registration_ID id
    @param trx transaction
    */
    public X_AD_Registration (Ctx ctx, int AD_Registration_ID, Trx trx)
    {
        super (ctx, AD_Registration_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Registration_ID == 0)
        {
            setAD_Registration_ID (0);
            setIsRegistered (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Registration (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27498804794789L;
    /** Last Updated Timestamp 2008-07-21 15:31:18.0 */
    public static final long updatedMS = 1216679478000L;
    /** AD_Table_ID=625 */
    public static final int Table_ID=625;
    
    /** TableName=AD_Registration */
    public static final String Table_Name="AD_Registration";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Registration.
    @param AD_Registration_ID System Registration */
    public void setAD_Registration_ID (int AD_Registration_ID)
    {
        if (AD_Registration_ID < 1) throw new IllegalArgumentException ("AD_Registration_ID is mandatory.");
        set_ValueNoCheck ("AD_Registration_ID", Integer.valueOf(AD_Registration_ID));
        
    }
    
    /** Get System Registration.
    @return System Registration */
    public int getAD_Registration_ID() 
    {
        return get_ValueAsInt("AD_Registration_ID");
        
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
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Company.
    @param Company Company */
    public void setCompany (String Company)
    {
        set_Value ("Company", Company);
        
    }
    
    /** Get Company.
    @return Company */
    public String getCompany() 
    {
        return (String)get_Value("Company");
        
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
    
    /** Up to 10 employees = 1000000 */
    public static final String EMPLOYEERANGE_UpTo10Employees = X_Ref__EmployeeRange.UP_TO10_EMPLOYEES.getValue();
    /** 11-20 employees = 1000001 */
    public static final String EMPLOYEERANGE_11_20Employees = X_Ref__EmployeeRange._11_20_EMPLOYEES.getValue();
    /** 21-100 employees = 1000002 */
    public static final String EMPLOYEERANGE_21_100Employees = X_Ref__EmployeeRange._21_100_EMPLOYEES.getValue();
    /** 101-200 employees = 1000003 */
    public static final String EMPLOYEERANGE_101_200Employees = X_Ref__EmployeeRange._101_200_EMPLOYEES.getValue();
    /** 201-500 employees = 1000004 */
    public static final String EMPLOYEERANGE_201_500Employees = X_Ref__EmployeeRange._201_500_EMPLOYEES.getValue();
    /** 501-1000 employees = 1000005 */
    public static final String EMPLOYEERANGE_501_1000Employees = X_Ref__EmployeeRange._501_1000_EMPLOYEES.getValue();
    /** 1000-2000 employees = 1000006 */
    public static final String EMPLOYEERANGE_1000_2000Employees = X_Ref__EmployeeRange._1000_2000_EMPLOYEES.getValue();
    /** Over 2000 employees = 1000007 */
    public static final String EMPLOYEERANGE_Over2000Employees = X_Ref__EmployeeRange.OVER2000_EMPLOYEES.getValue();
    /** Set Employees.
    @param EmployeeRange Number of employees */
    public void setEmployeeRange (String EmployeeRange)
    {
        if (!X_Ref__EmployeeRange.isValid(EmployeeRange))
        throw new IllegalArgumentException ("EmployeeRange Invalid value - " + EmployeeRange + " - Reference_ID=470 - 1000000 - 1000001 - 1000002 - 1000003 - 1000004 - 1000005 - 1000006 - 1000007");
        set_Value ("EmployeeRange", EmployeeRange);
        
    }
    
    /** Get Employees.
    @return Number of employees */
    public String getEmployeeRange() 
    {
        return (String)get_Value("EmployeeRange");
        
    }
    
    /** Set First Name.
    @param FirstName First name */
    public void setFirstName (String FirstName)
    {
        set_Value ("FirstName", FirstName);
        
    }
    
    /** Get First Name.
    @return First name */
    public String getFirstName() 
    {
        return (String)get_Value("FirstName");
        
    }
    
    /** Arts, entertainment & recreation = 1000000 */
    public static final String INDUSTRY_ArtsEntertainmentRecreation = X_Ref__IndustryCode.ARTS_ENTERTAINMENT_RECREATION.getValue();
    /** Construction = 1000001 */
    public static final String INDUSTRY_Construction = X_Ref__IndustryCode.CONSTRUCTION.getValue();
    /** Education = 1000002 */
    public static final String INDUSTRY_Education = X_Ref__IndustryCode.EDUCATION.getValue();
    /** Finance & insurance = 1000003 */
    public static final String INDUSTRY_FinanceInsurance = X_Ref__IndustryCode.FINANCE_INSURANCE.getValue();
    /** Health care & social assistance = 1000004 */
    public static final String INDUSTRY_HealthCareSocialAssistance = X_Ref__IndustryCode.HEALTH_CARE_SOCIAL_ASSISTANCE.getValue();
    /** Manufacturing = 1000005 */
    public static final String INDUSTRY_Manufacturing = X_Ref__IndustryCode.MANUFACTURING.getValue();
    /** Mining = 1000006 */
    public static final String INDUSTRY_Mining = X_Ref__IndustryCode.MINING.getValue();
    /** Public sector = 1000007 */
    public static final String INDUSTRY_PublicSector = X_Ref__IndustryCode.PUBLIC_SECTOR.getValue();
    /** Real estate, rental & leasing = 1000008 */
    public static final String INDUSTRY_RealEstateRentalLeasing = X_Ref__IndustryCode.REAL_ESTATE_RENTAL_LEASING.getValue();
    /** Retail trade = 1000009 */
    public static final String INDUSTRY_RetailTrade = X_Ref__IndustryCode.RETAIL_TRADE.getValue();
    /** Services = 1000010 */
    public static final String INDUSTRY_Services = X_Ref__IndustryCode.SERVICES.getValue();
    /** Transportation & warehousing = 1000011 */
    public static final String INDUSTRY_TransportationWarehousing = X_Ref__IndustryCode.TRANSPORTATION_WAREHOUSING.getValue();
    /** Utilities = 1000012 */
    public static final String INDUSTRY_Utilities = X_Ref__IndustryCode.UTILITIES.getValue();
    /** Wholesale trade = 1000013 */
    public static final String INDUSTRY_WholesaleTrade = X_Ref__IndustryCode.WHOLESALE_TRADE.getValue();
    /** Other = 1000014 */
    public static final String INDUSTRY_Other = X_Ref__IndustryCode.OTHER.getValue();
    /** Set Industry.
    @param Industry Industry classification */
    public void setIndustry (String Industry)
    {
        if (!X_Ref__IndustryCode.isValid(Industry))
        throw new IllegalArgumentException ("Industry Invalid value - " + Industry + " - Reference_ID=471 - 1000000 - 1000001 - 1000002 - 1000003 - 1000004 - 1000005 - 1000006 - 1000007 - 1000008 - 1000009 - 1000010 - 1000011 - 1000012 - 1000013 - 1000014");
        set_Value ("Industry", Industry);
        
    }
    
    /** Get Industry.
    @return Industry classification */
    public String getIndustry() 
    {
        return (String)get_Value("Industry");
        
    }
    
    /** Set Registered.
    @param IsRegistered The application is registered. */
    public void setIsRegistered (boolean IsRegistered)
    {
        set_ValueNoCheck ("IsRegistered", Boolean.valueOf(IsRegistered));
        
    }
    
    /** Get Registered.
    @return The application is registered. */
    public boolean isRegistered() 
    {
        return get_ValueAsBoolean("IsRegistered");
        
    }
    
    /** Set Last Name.
    @param LastName Last name */
    public void setLastName (String LastName)
    {
        set_Value ("LastName", LastName);
        
    }
    
    /** Get Last Name.
    @return Last name */
    public String getLastName() 
    {
        return (String)get_Value("LastName");
        
    }
    
    /** Set Keep me informed of Compiere news.
    @param OptIn Subscribe to communication from Compiere */
    public void setOptIn (boolean OptIn)
    {
        set_Value ("OptIn", Boolean.valueOf(OptIn));
        
    }
    
    /** Get Keep me informed of Compiere news.
    @return Subscribe to communication from Compiere */
    public boolean isOptIn() 
    {
        return get_ValueAsBoolean("OptIn");
        
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
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_ValueNoCheck ("Record_ID", null);
        else
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_ValueNoCheck ("Remote_Addr", Remote_Addr);
        
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
        set_ValueNoCheck ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
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
