/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.rmi.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.mail.internet.*;
import java.io.*;

import org.compiere.db.*;
import org.compiere.interfaces.*;
import org.compiere.util.*;

/**
 *  Client Model
 *
 *  @author Jorg Janke
 *  @version $Id: MClient.java 9202 2010-08-26 20:15:46Z rthng $
 */
public class MClient extends X_AD_Client
{
   /** Logger for class MClient */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MClient.class);
       /**
        *
        */
       private static final long serialVersionUID = 1L;


       /**
        *      Get client
        *      @param ctx context
        *      @param AD_Client_ID id
        *      @return client
        */
       public static MClient get (Ctx ctx, int AD_Client_ID)
       {
               Integer key = Integer.valueOf (AD_Client_ID);
               MClient client = s_cache.get(ctx, key);
               if (client != null)
                       return client;
               client = new MClient (ctx, AD_Client_ID, null);
               if (AD_Client_ID == 0)
                       client.load((Trx)null);
               if (AD_Client_ID == client.getAD_Client_ID())
                       s_cache.put (key, client);
               else
               {
                       s_log.severe("Not found AD_Client_ID=" + AD_Client_ID);
                       client = null;
               }
               return client;
       }       //      get

       /**
        *      Get all clients
        *      @param ctx context
        *      @return clients
        */
       public static MClient[] getAll (Ctx ctx)
       {
               ArrayList<MClient> list = new ArrayList<MClient>();
               String sql = "SELECT * FROM AD_Client";
               PreparedStatement pstmt = null;
               ResultSet rs = null;
               try
               {
                       pstmt = DB.prepareStatement(sql, (Trx) null);
                       rs = pstmt.executeQuery ();
                       while (rs.next ())
                       {
                               MClient client = new MClient (ctx, rs, null);
                               s_cache.put (Integer.valueOf (client.getAD_Client_ID()), client);
                               list.add (client);
                       }
               }
               catch (Exception e) {
                       s_log.log(Level.SEVERE, sql, e);
               }
       finally {
               DB.closeResultSet(rs);
               DB.closeStatement(pstmt);
       }

               MClient[] retValue = new MClient[list.size()];
               list.toArray (retValue);
               return retValue;
       }       //      getAll

       /**
        *      Get optionally cached client
        *      @param ctx context
        *      @return client
        */
       public static MClient get (Ctx ctx)
       {
               return get (ctx, ctx.getAD_Client_ID());
       }       //      get

       /**     Static Logger                           */
       private static final CLogger    s_log   = CLogger.getCLogger (MClient.class);
       /**     Cache                                           */
	private static final CCache<Integer,MClient>	s_cache = new CCache<Integer,MClient>("AD_Client", 3);


       /**************************************************************************
        *      Standard Constructor
        *      @param ctx context
        *      @param AD_Client_ID id
        *      @param createNew create new
        *      @param trx transaction
        */
       public MClient (Ctx ctx, int AD_Client_ID, boolean createNew, Trx trx)
       {
               super (ctx, AD_Client_ID, trx);
               m_createNew = createNew;
               if (AD_Client_ID == 0)
               {
                       if (m_createNew)
                       {
                       //      setValue (null);
                       //      setName (null);
                               setAD_Org_ID(0);
                               setIsMultiLingualDocument (false);
                               setIsSmtpAuthorization (false);
                               setIsUseBetaFunctions (false);
                               setIsServerEMail(false);
                               setAD_Language(Language.getBaseAD_Language());
                               setAutoArchive(AUTOARCHIVE_None);
                               setMMPolicy (MMPOLICY_FiFo);    // F
                               setIsPostImmediate(false);
                               setIsCostImmediate(false);
                               setSmtpPort(25);
                               setIsSmtpTLS(false);
                       }
                       else
                               load(get_Trx());
               }
       }       //      MClient

       /**
        *      Standard Constructor
        *      @param ctx context
        *      @param AD_Client_ID id
        *      @param trx transaction
        */
       public MClient (Ctx ctx, int AD_Client_ID, Trx trx)
       {
               this (ctx, AD_Client_ID, false, trx);
       }       //      MClient

       /**
        *      Load Constructor
        *      @param ctx context
        *      @param rs result set
        *      @param trx transaction
        */
       public MClient (Ctx ctx, ResultSet rs, Trx trx)
       {
               super (ctx, rs, trx);
       }       //      MClient


       /**     Client Info                                     */
       private MClientInfo             m_info = null;
       /** Language                                    */
       private Language                        m_language = null;
       /** New Record                                  */
       private boolean                         m_createNew = false;
       /** Client Info Setup Tree for Account  */
       private int                                     m_AD_Tree_Account_ID;

       /**
        *      Get SMTP Host
        *      @return SMTP or loaclhost
        */
       @Override
       public String getSmtpHost()
       {
               String s = super.getSmtpHost();
               if (s == null)
                       s = "localhost";
               return s;
       }       //      getSMTPHost


       /**
        *      Get SMTP Port
        *      @return port (default 25)
        */
       @Override
       public int getSmtpPort()
       {
               int p = super.getSmtpPort();
               if (p == 0)
                       setSmtpPort(25);
               return super.getSmtpPort();
       }       //      getSMTPPort

       /**
        *      Get Client Info
        *      @return Client Info
        */
       public MClientInfo getInfo()
       {
               if (m_info == null)
                       m_info = MClientInfo.get (getCtx(), getAD_Client_ID(), get_Trx());
               return m_info;
       }       //      getMClientInfo

       /**
        *      String Representation
        *      @return info
        */
       @Override
       public String toString()
       {
               StringBuffer sb = new StringBuffer ("MClient[")
                       .append(get_ID()).append("-").append(getValue())
                       .append("]");
               return sb.toString();
       }       //      toString

       /**
        *      Get Default Accounting Currency
        *      @return currency or 0
        */
       public int getC_Currency_ID()
       {
               if (m_info == null)
                       getInfo();
               if (m_info != null)
                       return m_info.getC_Currency_ID();
               return 0;
       }       //      getC_Currency_ID

       /**
        *      Get Language
        *      @return client language
        */
       public Language getLanguage()
       {
               if (m_language == null)
               {
                       m_language = Language.getLanguage(getAD_Language());
                       m_language = Env.verifyLanguage (m_language);
               }
               return m_language;
       }       //      getLanguage


       /**
        *      Set AD_Language
        *      @param AD_Language new language
        */
       @Override
       public void setAD_Language (String AD_Language)
       {
               m_language = null;
               super.setAD_Language (AD_Language);
       }       //      setAD_Language

       /**
        *      Get AD_Language
        *      @return Language
        */
       @Override
       public String getAD_Language ()
       {
               String s = super.getAD_Language ();
               if (s == null)
                       return Language.getBaseAD_Language();
               return s;
       }       //      getAD_Language

       /**
        *      Get Locale
        *      @return locale
        */
       public Locale getLocale()
       {
               Language lang = getLanguage();
               if (lang != null)
                       return lang.getLocale();
               return Locale.getDefault();
       }       //      getLocale


       /**************************************************************************
        *      Create Trees and Setup Client Info
        *      @param language language
        *      @return true if created
        */
       public boolean setupClientInfo (String language)
       {
               //      Create Trees
               String sql = null;
               if (Env.isBaseLanguage (language, "AD_Ref_List"))       //      Get TreeTypes & Name
                       sql = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=120 AND IsActive='Y'";
               else
                       sql = "SELECT l.Value, t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
                               + "WHERE l.AD_Reference_ID=120 AND l.AD_Ref_List_ID=t.AD_Ref_List_ID AND l.IsActive='Y'";

               //  Tree IDs
               int AD_Tree_Org_ID=0, AD_Tree_BPartner_ID=0, AD_Tree_Project_ID=0,
                       AD_Tree_SalesRegion_ID=0, AD_Tree_Product_ID=0,
                       AD_Tree_Campaign_ID=0, AD_Tree_Activity_ID=0;

               boolean success = false;
               PreparedStatement pstmt = null;
               ResultSet rs = null;
               try
               {
                       pstmt = DB.prepareStatement(sql, get_Trx());
                       rs = pstmt.executeQuery();
                       MTree tree = null;
                       while (rs.next())
                       {
                               String treeType = rs.getString(1);
                               if (treeType.equals(X_AD_Tree.TREETYPE_Other)
                                       || treeType.startsWith("U"))
                                       continue;
                               String name = getName() + " " + rs.getString(2);
                               //
                               if (treeType.equals(X_AD_Tree.TREETYPE_Organization))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       AD_Tree_Org_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_BPartner))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       AD_Tree_BPartner_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_Project))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       AD_Tree_Project_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_SalesRegion))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       AD_Tree_SalesRegion_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_Product))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       AD_Tree_Product_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_ElementValue))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       m_AD_Tree_Account_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_Campaign))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       AD_Tree_Campaign_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_Activity))
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                                       AD_Tree_Activity_ID = tree.getAD_Tree_ID();
                               }
                               else if (treeType.equals(X_AD_Tree.TREETYPE_Menu))      //      No Menu
                                       success = true;
                               else    //      PC (Product Category), BB (BOM)
                               {
                                       tree = new MTree (this, name, treeType);
                                       success = tree.save();
                               }
                               if (!success)
                               {
                                       log.log(Level.SEVERE, "Tree NOT created: " + name);
                                       break;
                               }
                       }
               }
               catch (SQLException e1) {
                       log.log(Level.SEVERE, "Trees", e1);
                       success = false;
               }
               finally {
                       DB.closeResultSet(rs);
                       DB.closeStatement(pstmt);
               }


               if (!success)
                       return false;

               //      Create ClientInfo
               MClientInfo clientInfo = new MClientInfo (this,
                       AD_Tree_Org_ID, AD_Tree_BPartner_ID, AD_Tree_Project_ID,
                       AD_Tree_SalesRegion_ID, AD_Tree_Product_ID,
                       AD_Tree_Campaign_ID, AD_Tree_Activity_ID, get_Trx());
               success = clientInfo.save();
               return success;
       }       //      createTrees

       /**
        *      Get AD_Tree_Account_ID created in setup client info
        *      @return Account Tree ID
        */
       public int getSetup_AD_Tree_Account_ID()
       {
               return m_AD_Tree_Account_ID;
       }       //      getSetup_AD_Tree_Account_ID

       /**
        *      Is Auto Archive on
        *      @return true if auto archive
        */
       public boolean isAutoArchive()
       {
               String aa = getAutoArchive();
               return aa != null && !aa.equals(AUTOARCHIVE_None);
       }       //      isAutoArchive


       /**
        *      Update Trl Tables automatically?
        *      @param TableName table name
        *      @return true if automatically translated
        */
       public boolean isAutoUpdateTrl (String TableName)
       {
               if (super.isMultiLingualDocument())
                       return false;
               if (TableName == null)
                       return false;
               //      Not Multi-Lingual Documents - only Doc Related
               if (TableName.startsWith("AD"))
                       return false;
               return true;
       }       //      isMultiLingualDocument

       /**
        *      Get Primary Accounting Schema
        *      @return Acct Schema or null
        */
       public MAcctSchema getAcctSchema()
       {
               if (m_info == null)
                       m_info = MClientInfo.get (getCtx(), getAD_Client_ID(), get_Trx());
               if (m_info != null)
               {
                       int C_AcctSchema_ID = m_info.getC_AcctSchema1_ID();
                       if (C_AcctSchema_ID != 0)
                               return MAcctSchema.get(getCtx(), C_AcctSchema_ID);
               }
               log.severe("Not found for AD_Client_ID=" + getAD_Client_ID());
               return null;
       }       //      getAcctSchema

       /**
        *      Save
        *      @return true if saved
        */
       @Override
       public boolean save ()
       {
               if (get_ID() == 0 && !m_createNew)
                       return saveUpdate();
               return super.save ();
       }       //      save


       /**************************************************************************
        *      Test EMail
        *      @return OK or error
        */
       public String testEMail()
       {
               if (getRequestEMail() == null || getRequestEMail().length() == 0)
                       return "No Request EMail for " + getName();
               //
               EMail email = createEMail (getRequestEMail(), getName(),
                       "Compiere EMail Test",
                       "Compiere EMail Test: " + toString());
               if (email == null)
                       return "Could not create EMail: " + getName();
               try
               {
                       String msg = email.send();
                       if (EMail.SENT_OK.equals (msg))
                       {
                               log.info("Sent Test EMail to " + getRequestEMail());
                               return "OK";
                       }
                       else
                       {
                               log.warning("Could NOT send Test EMail from "
                                       + getSmtpHost() + ": " + getRequestEMail()
                                       + " (" + getRequestUser()
                                       + ") to " + getRequestEMail() + ": " + msg);
                               return msg;
                       }
               }
               catch (Exception ex)
               {
                       log.severe(getName() + " - " + ex.getLocalizedMessage());
                       return ex.getLocalizedMessage();
               }
       }       //      testEMail

       /**
        *      Send EMail from Request User - with trace
        *      @param AD_User_ID recipient
        *      @param subject subject
        *      @param message message
        *      @param attachment optional attachment
        *      @return true if sent
        */
       public boolean sendEMail (int AD_User_ID,
               String subject, String message, File attachment)
       {
               MUser to = MUser.get(getCtx(), AD_User_ID);
               String toEMail = to.getEMail();
               if (toEMail == null || toEMail.length() == 0)
               {
                       log.warning("No EMail address for recipient: " + to);
                       return false;
               }
               if (to.isEMailBounced())
               {
                       log.warning("EMail bounced for recipient: " + to);
                       return false;
               }
               EMail email = createEMail(null, to, subject, message);
               if (email == null)
                       return false;
               if (attachment != null)
                       email.addAttachment(attachment);
               try
               {
                       return sendEmailNow(null, to, email);
               }
               catch (Exception ex)
               {
                       log.severe(getName() + " - " + ex.getLocalizedMessage());
                       return false;
               }
       }       //      sendEMail

       /**
        *      Send EMail from Request User - no trace
        *      @param toEMail recipient email address
        *      @param subject subject
        *      @param message message
        *      @param attachment optional attachment
        *      @return true if sent
        */
       public boolean sendEMail (String toEMail, String toName,
               String subject, String message, File attachment)
       {
               EMail email = createEMail(toEMail, toName, subject, message);
               if (email == null)
                       return false;
               if (attachment != null)
                       email.addAttachment(attachment);
               try
               {
                       String msg = email.send();
                       if (EMail.SENT_OK.equals (msg))
                       {
                               log.info("Sent EMail " + subject + " to " + toEMail);
                               return true;
                       }
                       else
                       {
                               log.warning("Could NOT Send Email: " + subject
                                       + " to " + toEMail + ": " + msg
                                       + " (" + getName() + ")");
                               return false;
                       }
               }
               catch (Exception ex)
               {
                       log.severe(getName() + " - " + ex.getLocalizedMessage());
                       return false;
               }
       }       //      sendEMail


       /**
        *      Send EMail from User
        *      @param from sender
        *      @param to recipient
        *      @param subject subject
        *      @param message message
        *      @param attachment optional attachment
        *      @return true if sent
        */
       public boolean sendEMail (MUser from, MUser to,
               String subject, String message, File attachment)
       {
               EMail email = createEMail(from, to, subject, message);
               if (email == null)
                       return false;
               if (attachment != null)
                       email.addAttachment(attachment);
               InternetAddress emailFrom = email.getFrom();
               try
               {
                       return sendEmailNow(from, to, email);
               }
               catch (Exception ex)
               {
                       log.severe(getName() + " - from " + emailFrom
                               + " to " + to + ": " + ex.getLocalizedMessage());
                       return false;
               }
       }       //      sendEMail

       /**
        *      Send Email Now
        *      @param from optional from user
        *      @param to to user
        *      @param email email
        *      @return true if sent
        */
       private boolean sendEmailNow(MUser from, MUser to, EMail email)
       {
               String msg = email.send();
               //
               X_AD_UserMail um = new X_AD_UserMail(getCtx(), 0, null);
               um.setClientOrg(this);
               um.setAD_User_ID(to.getAD_User_ID());
               um.setSubject(email.getSubject());
               um.setMailText(email.getMessageCRLF());
               if (email.isSentOK())
                       um.setMessageID(email.getMessageID());
               else
               {
                       um.setMessageID(email.getSentMsg());
                       um.setIsDelivered(X_AD_UserMail.ISDELIVERED_No);
               }
               um.save();

               //
               if (email.isSentOK())
               {
                       if (from != null)
                               log.info("Sent Email: " + email.getSubject()
                                       + " from " + from.getEMail()
                                       + " to " + to.getEMail());
                       else
                               log.info("Sent Email: " + email.getSubject()
                                       + " to " + to.getEMail());
                       return true;
               }
               else
               {
                       if (from != null)
                               log.warning("Could NOT Send Email: " + email.getSubject()
                                       + " from " + from.getEMail()
                                       + " to " + to.getEMail() + ": " + msg
                                       + " (" + getName() + ")");
                       else
                               log.warning("Could NOT Send Email: " + email.getSubject()
                                       + " to " + to.getEMail() + ": " + msg
                                       + " (" + getName() + ")");
                       return false;
               }
       }       //      sendEmailNow

       /************
        *      Create EMail from Request User
        *      @param to recipient
        *      @param subject subject
        *      @param message message
        *      @return EMail or null
        */
       public EMail createEMail (String toEMail, String toName,
               String subject, String message)
       {
               if (toEMail == null || toEMail.length() == 0)
               {
                       log.warning("No To");
                       return null;
               }
               //
               EMail email = new EMail (this,
                                  getRequestEMail(), getName(),
                                  toEMail, toName,
                                  subject, message, isServerEMail());
               if (email == null || !email.isValid())
               {
                       log.info(email.toString());
                       return null;
               }
               if (isSmtpAuthorization())
                       email.createAuthenticator (getRequestUser(), getRequestUserPW());
               return email;
       }       //      createEMail

       /**
        *      Create EMail from User
        *      @param from optional sender
        *      @param to recipient
        *      @param subject subject
        *      @param message message
        *      @return EMail
        */
       public EMail createEMail (MUser from, MUser to,
               String subject, String message)
       {
               if (to == null)
               {
                       log.warning("No To user");
                       return null;
               }
               if (to.getEMail() == null || to.getEMail().length() == 0)
               {
                       log.warning("No To address: " + to);
                       return null;
               }
               if (to.isEMailBounced())
               {
                       log.warning("EMail bounced: " + to.getBouncedInfo()
                               + " - " + to.getEMail());
                       return null;
               }
               return createEMail (from, to.getEMail(), to.getName(), subject, message);
       }       //      createEMail

       /**
        *      Create EMail from User
        *      @param from optional sender
        *      @param toEMail recipient
        *      @param subject sunject
        *      @param message nessage
        *      @return EMail or null
        */
       public EMail createEMail (MUser from, String toEMail, String toName,
               String subject, String message)
       {
               if (toEMail == null || toEMail.length() == 0)
               {
                       log.warning("No To address");
                       return null;
               }
               //      No From - send from Request
               if (from == null)
                       return createEMail (toEMail, toName, subject, message);
               //      No From details - Error
               if (from.getEMail() == null
                       || from.getEMailUser() == null || from.getEMailUserPW() == null)
               {
                       log.warning("From EMail incomplete: " + from + " (" + getName() + ")");
                       return null;
               }
               //
               EMail email = new EMail (this,
                                  from.getEMail(), from.getName(),
                                  toEMail, toName,
                                  subject,
                                  message, isServerEMail());
               if (!email.isValid())
               {
                       log.info(email.toString());
                       return null;
               }
               if (isSmtpAuthorization())
                       email.createAuthenticator (from.getEMailUser(), from.getEMailUserPW());
               return email;
       }       //      createEMail

}       //      MClient