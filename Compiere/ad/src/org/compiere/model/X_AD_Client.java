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
/** Generated Model for AD_Client
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Client.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Client extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Client_ID id
    @param trx transaction
    */
    public X_AD_Client (Ctx ctx, int AD_Client_ID, Trx trx)
    {
        super (ctx, AD_Client_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Client_ID == 0)
        {
            setAutoArchive (null);	// N
            setIsCostImmediate (false);	// N
            setIsMultiLingualDocument (false);
            setIsPostImmediate (false);	// N
            setIsServerEMail (false);
            setIsSmtpAuthorization (false);	// N
            setIsUseBetaFunctions (true);	// Y
            setMMPolicy (null);	// F
            setName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Client (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=112 */
    public static final int Table_ID=112;
    
    /** TableName=AD_Client */
    public static final String Table_Name="AD_Client";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_Value ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** All (Reports, Documents) = 1 */
    public static final String AUTOARCHIVE_AllReportsDocuments = X_Ref_AD_Client_AutoArchive.ALL_REPORTS_DOCUMENTS.getValue();
    /** Documents = 2 */
    public static final String AUTOARCHIVE_Documents = X_Ref_AD_Client_AutoArchive.DOCUMENTS.getValue();
    /** External Documents = 3 */
    public static final String AUTOARCHIVE_ExternalDocuments = X_Ref_AD_Client_AutoArchive.EXTERNAL_DOCUMENTS.getValue();
    /** None = N */
    public static final String AUTOARCHIVE_None = X_Ref_AD_Client_AutoArchive.NONE.getValue();
    /** Set Auto Archive.
    @param AutoArchive Enable and level of automatic Archive of documents */
    public void setAutoArchive (String AutoArchive)
    {
        if (AutoArchive == null) throw new IllegalArgumentException ("AutoArchive is mandatory");
        if (!X_Ref_AD_Client_AutoArchive.isValid(AutoArchive))
        throw new IllegalArgumentException ("AutoArchive Invalid value - " + AutoArchive + " - Reference_ID=334 - 1 - 2 - 3 - N");
        set_Value ("AutoArchive", AutoArchive);
        
    }
    
    /** Get Auto Archive.
    @return Enable and level of automatic Archive of documents */
    public String getAutoArchive() 
    {
        return (String)get_Value("AutoArchive");
        
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
    
    /** Set Document Directory.
    @param DocumentDir Directory for documents from the application server */
    public void setDocumentDir (String DocumentDir)
    {
        set_Value ("DocumentDir", DocumentDir);
        
    }
    
    /** Get Document Directory.
    @return Directory for documents from the application server */
    public String getDocumentDir() 
    {
        return (String)get_Value("DocumentDir");
        
    }
    
    /** Set EMail Test.
    @param EMailTest Test EMail */
    public void setEMailTest (String EMailTest)
    {
        set_Value ("EMailTest", EMailTest);
        
    }
    
    /** Get EMail Test.
    @return Test EMail */
    public String getEMailTest() 
    {
        return (String)get_Value("EMailTest");
        
    }
    
    /** Set Cost Immediately.
    @param IsCostImmediate Update Costs immediately for testing */
    public void setIsCostImmediate (boolean IsCostImmediate)
    {
        set_Value ("IsCostImmediate", Boolean.valueOf(IsCostImmediate));
        
    }
    
    /** Get Cost Immediately.
    @return Update Costs immediately for testing */
    public boolean isCostImmediate() 
    {
        return get_ValueAsBoolean("IsCostImmediate");
        
    }
    
    /** Set Multi Lingual Documents.
    @param IsMultiLingualDocument Documents are Multi Lingual */
    public void setIsMultiLingualDocument (boolean IsMultiLingualDocument)
    {
        set_Value ("IsMultiLingualDocument", Boolean.valueOf(IsMultiLingualDocument));
        
    }
    
    /** Get Multi Lingual Documents.
    @return Documents are Multi Lingual */
    public boolean isMultiLingualDocument() 
    {
        return get_ValueAsBoolean("IsMultiLingualDocument");
        
    }
    
    /** Set Post Immediately.
    @param IsPostImmediate Post the accounting immediately for testing */
    public void setIsPostImmediate (boolean IsPostImmediate)
    {
        set_Value ("IsPostImmediate", Boolean.valueOf(IsPostImmediate));
        
    }
    
    /** Get Post Immediately.
    @return Post the accounting immediately for testing */
    public boolean isPostImmediate() 
    {
        return get_ValueAsBoolean("IsPostImmediate");
        
    }
    
    /** Set Server EMail.
    @param IsServerEMail Send EMail from Server */
    public void setIsServerEMail (boolean IsServerEMail)
    {
        set_Value ("IsServerEMail", Boolean.valueOf(IsServerEMail));
        
    }
    
    /** Get Server EMail.
    @return Send EMail from Server */
    public boolean isServerEMail() 
    {
        return get_ValueAsBoolean("IsServerEMail");
        
    }
    
    /** Set SMTP Authentication.
    @param IsSmtpAuthorization Your mail server requires Authentication */
    public void setIsSmtpAuthorization (boolean IsSmtpAuthorization)
    {
        set_Value ("IsSmtpAuthorization", Boolean.valueOf(IsSmtpAuthorization));
        
    }
    
    /** Get SMTP Authentication.
    @return Your mail server requires Authentication */
    public boolean isSmtpAuthorization() 
    {
        return get_ValueAsBoolean("IsSmtpAuthorization");
        
    }
    
    /** Set SMTP TLS.
    @param IsSmtpTLS Use TLS for SMTP communication */
    public void setIsSmtpTLS (boolean IsSmtpTLS)
    {
        set_Value ("IsSmtpTLS", Boolean.valueOf(IsSmtpTLS));
        
    }
    
    /** Get SMTP TLS.
    @return Use TLS for SMTP communication */
    public boolean isSmtpTLS() 
    {
        return get_ValueAsBoolean("IsSmtpTLS");
        
    }
    
    /** Set Use Beta Functions.
    @param IsUseBetaFunctions Enable the use of Beta Functionality */
    public void setIsUseBetaFunctions (boolean IsUseBetaFunctions)
    {
        set_Value ("IsUseBetaFunctions", Boolean.valueOf(IsUseBetaFunctions));
        
    }
    
    /** Get Use Beta Functions.
    @return Enable the use of Beta Functionality */
    public boolean isUseBetaFunctions() 
    {
        return get_ValueAsBoolean("IsUseBetaFunctions");
        
    }
    
    /** Set LDAP Query.
    @param LDAPQuery Query to authenticate users for that client with LDAP */
    public void setLDAPQuery (String LDAPQuery)
    {
        set_Value ("LDAPQuery", LDAPQuery);
        
    }
    
    /** Get LDAP Query.
    @return Query to authenticate users for that client with LDAP */
    public String getLDAPQuery() 
    {
        return (String)get_Value("LDAPQuery");
        
    }
    
    /** FiFo = F */
    public static final String MMPOLICY_FiFo = X_Ref__MMPolicy.FI_FO.getValue();
    /** LiFo = L */
    public static final String MMPOLICY_LiFo = X_Ref__MMPolicy.LI_FO.getValue();
    /** Set Material Policy.
    @param MMPolicy Material Movement Policy */
    public void setMMPolicy (String MMPolicy)
    {
        if (MMPolicy == null) throw new IllegalArgumentException ("MMPolicy is mandatory");
        if (!X_Ref__MMPolicy.isValid(MMPolicy))
        throw new IllegalArgumentException ("MMPolicy Invalid value - " + MMPolicy + " - Reference_ID=335 - F - L");
        set_Value ("MMPolicy", MMPolicy);
        
    }
    
    /** Get Material Policy.
    @return Material Movement Policy */
    public String getMMPolicy() 
    {
        return (String)get_Value("MMPolicy");
        
    }
    
    /** Set Model Validation Classes.
    @param ModelValidationClasses List of data model validation classes separated by ;
     */
    public void setModelValidationClasses (String ModelValidationClasses)
    {
        set_Value ("ModelValidationClasses", ModelValidationClasses);
        
    }
    
    /** Get Model Validation Classes.
    @return List of data model validation classes separated by ;
     */
    public String getModelValidationClasses() 
    {
        return (String)get_Value("ModelValidationClasses");
        
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
    
    /** Set Request EMail.
    @param RequestEMail EMail address to send automated mails from or receive mails for automated processing (fully qualified) */
    public void setRequestEMail (String RequestEMail)
    {
        set_Value ("RequestEMail", RequestEMail);
        
    }
    
    /** Get Request EMail.
    @return EMail address to send automated mails from or receive mails for automated processing (fully qualified) */
    public String getRequestEMail() 
    {
        return (String)get_Value("RequestEMail");
        
    }
    
    /** Set Request Folder.
    @param RequestFolder EMail folder to process incoming emails;
     if empty INBOX is used */
    public void setRequestFolder (String RequestFolder)
    {
        set_Value ("RequestFolder", RequestFolder);
        
    }
    
    /** Get Request Folder.
    @return EMail folder to process incoming emails;
     if empty INBOX is used */
    public String getRequestFolder() 
    {
        return (String)get_Value("RequestFolder");
        
    }
    
    /** Set Request User.
    @param RequestUser User Name (ID) of the email owner */
    public void setRequestUser (String RequestUser)
    {
        set_Value ("RequestUser", RequestUser);
        
    }
    
    /** Get Request User.
    @return User Name (ID) of the email owner */
    public String getRequestUser() 
    {
        return (String)get_Value("RequestUser");
        
    }
    
    /** Set Request User Password.
    @param RequestUserPW Password of the user name (ID) for mail processing */
    public void setRequestUserPW (String RequestUserPW)
    {
        set_Value ("RequestUserPW", RequestUserPW);
        
    }
    
    /** Get Request User Password.
    @return Password of the user name (ID) for mail processing */
    public String getRequestUserPW() 
    {
        return (String)get_Value("RequestUserPW");
        
    }
    
    /** Set Mail Host.
    @param SmtpHost Hostname of Mail Server for SMTP and IMAP */
    public void setSmtpHost (String SmtpHost)
    {
        set_Value ("SmtpHost", SmtpHost);
        
    }
    
    /** Get Mail Host.
    @return Hostname of Mail Server for SMTP and IMAP */
    public String getSmtpHost() 
    {
        return (String)get_Value("SmtpHost");
        
    }
    
    /** Set SMTP Port.
    @param SmtpPort Mail service port */
    public void setSmtpPort (int SmtpPort)
    {
        set_Value ("SmtpPort", Integer.valueOf(SmtpPort));
        
    }
    
    /** Get SMTP Port.
    @return Mail service port */
    public int getSmtpPort() 
    {
        return get_ValueAsInt("SmtpPort");
        
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
    
    /** Set XX_RetentionAgent.
    @param XX_RetentionAgent  */
    public void setXX_RetentionAgent (boolean XX_RetentionAgent)   {
        set_Value ("XX_RetentionAgent", Boolean.valueOf(XX_RetentionAgent));
    }
    
    /** Get XX_RetentionAgent.
    @return XX_RetentionAgent */
    public boolean isXX_RetentionAgent()    {
        return get_ValueAsBoolean("XX_RetentionAgent"); 
    }
    
    
}
