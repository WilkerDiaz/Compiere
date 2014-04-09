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
/** Generated Model for AD_User
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_User.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_User extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_User_ID id
    @param trx transaction
    */
    public X_AD_User (Ctx ctx, int AD_User_ID, Trx trx)
    {
        super (ctx, AD_User_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_User_ID == 0)
        {
            setAD_User_ID (0);
            setIsFullBPAccess (true);	// Y
            setName (null);
            setNotificationType (null);	// E
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_User (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520916903789L;
    /** Last Updated Timestamp 2009-04-03 12:46:27.0 */
    public static final long updatedMS = 1238791587000L;
    /** AD_Table_ID=114 */
    public static final int Table_ID=114;
    
    /** TableName=AD_User */
    public static final String Table_Name="AD_User";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
    }
    
    /** Set Favorite Menu Tree.
    @param AD_Tree_MenuFavorite_ID Tree of the personal Favorite menu */
    public void setAD_Tree_MenuFavorite_ID (int AD_Tree_MenuFavorite_ID)
    {
        if (AD_Tree_MenuFavorite_ID <= 0) set_ValueNoCheck ("AD_Tree_MenuFavorite_ID", null);
        else
        set_ValueNoCheck ("AD_Tree_MenuFavorite_ID", Integer.valueOf(AD_Tree_MenuFavorite_ID));
        
    }
    
    /** Get Favorite Menu Tree.
    @return Tree of the personal Favorite menu */
    public int getAD_Tree_MenuFavorite_ID() 
    {
        return get_ValueAsInt("AD_Tree_MenuFavorite_ID");
        
    }
    
    /** Set New Menu Tree.
    @param AD_Tree_MenuNew_ID Tree of the personal Favorite menu for new items */
    public void setAD_Tree_MenuNew_ID (int AD_Tree_MenuNew_ID)
    {
        if (AD_Tree_MenuNew_ID <= 0) set_ValueNoCheck ("AD_Tree_MenuNew_ID", null);
        else
        set_ValueNoCheck ("AD_Tree_MenuNew_ID", Integer.valueOf(AD_Tree_MenuNew_ID));
        
    }
    
    /** Get New Menu Tree.
    @return Tree of the personal Favorite menu for new items */
    public int getAD_Tree_MenuNew_ID() 
    {
        return get_ValueAsInt("AD_Tree_MenuNew_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
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
    
    /** Set Bounced Info.
    @param BouncedInfo Information about the cause of bounce */
    public void setBouncedInfo (String BouncedInfo)
    {
        set_Value ("BouncedInfo", BouncedInfo);
        
    }
    
    /** Get Bounced Info.
    @return Information about the cause of bounce */
    public String getBouncedInfo() 
    {
        return (String)get_Value("BouncedInfo");
        
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
    
    /** LAN = L */
    public static final String CONNECTIONPROFILE_LAN = X_Ref_AD_User_ConnectionProfile.LAN.getValue();
    /** Terminal Server = T */
    public static final String CONNECTIONPROFILE_TerminalServer = X_Ref_AD_User_ConnectionProfile.TERMINAL_SERVER.getValue();
    /** VPN = V */
    public static final String CONNECTIONPROFILE_VPN = X_Ref_AD_User_ConnectionProfile.VPN.getValue();
    /** WAN = W */
    public static final String CONNECTIONPROFILE_WAN = X_Ref_AD_User_ConnectionProfile.WAN.getValue();
    /** Set Connection Profile.
    @param ConnectionProfile How a Java Client connects to the server(s) */
    public void setConnectionProfile (String ConnectionProfile)
    {
        if (!X_Ref_AD_User_ConnectionProfile.isValid(ConnectionProfile))
        throw new IllegalArgumentException ("ConnectionProfile Invalid value - " + ConnectionProfile + " - Reference_ID=364 - L - T - V - W");
        set_Value ("ConnectionProfile", ConnectionProfile);
        
    }
    
    /** Get Connection Profile.
    @return How a Java Client connects to the server(s) */
    public String getConnectionProfile() 
    {
        return (String)get_Value("ConnectionProfile");
        
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
    
    /** Set EMail User ID.
    @param EMailUser User Name (ID) in the Mail System */
    public void setEMailUser (String EMailUser)
    {
        set_Value ("EMailUser", EMailUser);
        
    }
    
    /** Get EMail User ID.
    @return User Name (ID) in the Mail System */
    public String getEMailUser() 
    {
        return (String)get_Value("EMailUser");
        
    }
    
    /** Set EMail User Password.
    @param EMailUserPW Password of your email user id */
    public void setEMailUserPW (String EMailUserPW)
    {
        set_Value ("EMailUserPW", EMailUserPW);
        
    }
    
    /** Get EMail User Password.
    @return Password of your email user id */
    public String getEMailUserPW() 
    {
        return (String)get_Value("EMailUserPW");
        
    }
    
    /** Set Verification Info.
    @param EMailVerify Verification information of EMail Address */
    public void setEMailVerify (String EMailVerify)
    {
        set_ValueNoCheck ("EMailVerify", EMailVerify);
        
    }
    
    /** Get Verification Info.
    @return Verification information of EMail Address */
    public String getEMailVerify() 
    {
        return (String)get_Value("EMailVerify");
        
    }
    
    /** Set EMail Verify.
    @param EMailVerifyDate Date Email was verified */
    public void setEMailVerifyDate (Timestamp EMailVerifyDate)
    {
        set_ValueNoCheck ("EMailVerifyDate", EMailVerifyDate);
        
    }
    
    /** Get EMail Verify.
    @return Date Email was verified */
    public Timestamp getEMailVerifyDate() 
    {
        return (Timestamp)get_Value("EMailVerifyDate");
        
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
    
    /** Set Generate User Key.
    @param GenerateUserKey Generate User Key */
    public void setGenerateUserKey (String GenerateUserKey)
    {
        set_Value ("GenerateUserKey", GenerateUserKey);
        
    }
    
    /** Get Generate User Key.
    @return Generate User Key */
    public String getGenerateUserKey() 
    {
        return (String)get_Value("GenerateUserKey");
        
    }
    
    /** Set EMail Bounced.
    @param IsEMailBounced The email delivery bounced */
    public void setIsEMailBounced (boolean IsEMailBounced)
    {
        set_Value ("IsEMailBounced", Boolean.valueOf(IsEMailBounced));
        
    }
    
    /** Get EMail Bounced.
    @return The email delivery bounced */
    public boolean isEMailBounced() 
    {
        return get_ValueAsBoolean("IsEMailBounced");
        
    }
    
    /** Set Full BP Access.
    @param IsFullBPAccess The user/contact has full access to Business Partner information and resources */
    public void setIsFullBPAccess (boolean IsFullBPAccess)
    {
        set_Value ("IsFullBPAccess", Boolean.valueOf(IsFullBPAccess));
        
    }
    
    /** Get Full BP Access.
    @return The user/contact has full access to Business Partner information and resources */
    public boolean isFullBPAccess() 
    {
        return get_ValueAsBoolean("IsFullBPAccess");
        
    }
    
    /** Set LDAP User Name.
    @param LDAPUser User Name used for authorization via LDAP (directory) services */
    public void setLDAPUser (String LDAPUser)
    {
        set_Value ("LDAPUser", LDAPUser);
        
    }
    
    /** Get LDAP User Name.
    @return User Name used for authorization via LDAP (directory) services */
    public String getLDAPUser() 
    {
        return (String)get_Value("LDAPUser");
        
    }
    
    /** Set Last Contact.
    @param LastContact Date this individual was last contacted */
    public void setLastContact (Timestamp LastContact)
    {
        set_Value ("LastContact", LastContact);
        
    }
    
    /** Get Last Contact.
    @return Date this individual was last contacted */
    public Timestamp getLastContact() 
    {
        return (Timestamp)get_Value("LastContact");
        
    }
    
    /** Set Last registration reminder.
    @param LastRegistrationReminder Date the user was last reminded to register the system */
    public void setLastRegistrationReminder (Timestamp LastRegistrationReminder)
    {
        set_Value ("LastRegistrationReminder", LastRegistrationReminder);
        
    }
    
    /** Get Last registration reminder.
    @return Date the user was last reminded to register the system */
    public Timestamp getLastRegistrationReminder() 
    {
        return (Timestamp)get_Value("LastRegistrationReminder");
        
    }
    
    /** Set Last Result.
    @param LastResult Result of last contact */
    public void setLastResult (String LastResult)
    {
        set_Value ("LastResult", LastResult);
        
    }
    
    /** Get Last Result.
    @return Result of last contact */
    public String getLastResult() 
    {
        return (String)get_Value("LastResult");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
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
    
    /** EMail+Notice = B */
    public static final String NOTIFICATIONTYPE_EMailPlusNotice = X_Ref_AD_User_NotificationType.E_MAIL_PLUS_NOTICE.getValue();
    /** EMail = E */
    public static final String NOTIFICATIONTYPE_EMail = X_Ref_AD_User_NotificationType.E_MAIL.getValue();
    /** Notice = N */
    public static final String NOTIFICATIONTYPE_Notice = X_Ref_AD_User_NotificationType.NOTICE.getValue();
    /** None = X */
    public static final String NOTIFICATIONTYPE_None = X_Ref_AD_User_NotificationType.NONE.getValue();
    /** Set Notification Type.
    @param NotificationType Type of Notifications */
    public void setNotificationType (String NotificationType)
    {
        if (NotificationType == null) throw new IllegalArgumentException ("NotificationType is mandatory");
        if (!X_Ref_AD_User_NotificationType.isValid(NotificationType))
        throw new IllegalArgumentException ("NotificationType Invalid value - " + NotificationType + " - Reference_ID=344 - B - E - N - X");
        set_Value ("NotificationType", NotificationType);
        
    }
    
    /** Get Notification Type.
    @return Type of Notifications */
    public String getNotificationType() 
    {
        return (String)get_Value("NotificationType");
        
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
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        throw new IllegalArgumentException ("SalesRep_ID is virtual column");
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    /** Set Supervisor.
    @param Supervisor_ID Supervisor for this user/organization - used for escalation and approval */
    public void setSupervisor_ID (int Supervisor_ID)
    {
        if (Supervisor_ID <= 0) set_Value ("Supervisor_ID", null);
        else
        set_Value ("Supervisor_ID", Integer.valueOf(Supervisor_ID));
        
    }
    
    /** Get Supervisor.
    @return Supervisor for this user/organization - used for escalation and approval */
    public int getSupervisor_ID() 
    {
        return get_ValueAsInt("Supervisor_ID");
        
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
    
    /** Set User Key.
    @param UserKey User Key is a Secret Key using which the user can authenticate a Compiere session */
    public void setUserKey (String UserKey)
    {
        set_Value ("UserKey", UserKey);
        
    }
    
    /** Get User Key.
    @return User Key is a Secret Key using which the user can authenticate a Compiere session */
    public String getUserKey() 
    {
        return (String)get_Value("UserKey");
        
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
    
    
}
