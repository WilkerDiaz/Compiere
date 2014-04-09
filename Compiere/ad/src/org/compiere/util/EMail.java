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
package org.compiere.util;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

import org.compiere.db.CConnection;
import org.compiere.interfaces.Server;
import org.compiere.model.*;

import com.sun.mail.smtp.*;

/**
 *      EMail Object.
 *      Resources:
 *      http://java.sun.com/products/javamail/index.html
 *      http://java.sun.com/products/javamail/FAQ.html
 *
 *  <p>
 *  When I try to send a message, I get javax.mail.SendFailedException:
 *              550 Unable to relay for my-address
 *  <br>
 *  This is an error reply from your SMTP mail server. It indicates that
 *  your mail server is not configured to allow you to send mail through it.
 *
 *  @author Jorg Janke
 *  @version  $Id: EMail.java 9202 2010-08-26 20:15:46Z rthng $
 */
public final class EMail implements Serializable
{
       /**
        *
        */
       private static final long serialVersionUID = 1L;

       class Attachment implements Serializable {
               private static final long serialVersionUID = 1L;
               byte[] content;
               String contentType;
               String fileName;
               public Attachment(byte[] content, String contentType, String fileName){
                       this.content = content;
                       this.contentType = contentType;
                       this.fileName = fileName;
               }
       }

       /**
        *      Full Constructor
        *  @param client the client
        *  @param from Sender's EMail address
        *  @param to   Recipient EMail address
        *  @param subject  Subject of message
        *  @param message  The message
        */
       public EMail (MClient client,
               String fromEMail, String fromName, String toEMail, String toName,
               String subject, String message, boolean serverEmail)
       {
               this (client.getCtx(), client.getSmtpHost(), client.getSmtpPort(), client.isSmtpTLS(),
                       fromEMail, fromName, toEMail, toName, subject, message, serverEmail);
       }       //      EMail

       /**
        *      Full Constructor
        *      @param ctx context
        *  @param smtpHost The mail server
        *  @param smtpPort port
        *  @param isSmtpTLS TLS protocol
        *  @param fromEMail Sender's EMail address
        *  @param toEMail   Recipient EMail address
        *  @param subject  Subject of message
        *  @param message  The message
        */
       public EMail (Ctx ctx, String smtpHost, int smtpPort, boolean isSmtpTLS,
               String fromEMail, String fromName, String toEMail, String toName,
               String subject, String message, boolean serverEmail)
       {
               setSmtpHost(smtpHost);
               if (smtpPort != 0)
                       m_smtpPort = smtpPort;
               m_isSmtpTLS = isSmtpTLS;
               if (!setFrom(fromEMail, fromName))
                       return;
               addTo(toEMail, toName);
               m_ctx = ctx;
               if (subject == null || subject.length() == 0)
                       setSubject(".");        //      pass validation
               else
                       setSubject (subject);
               if (message != null && message.length() > 0)
                       setMessageText (message);
               m_valid = isValid (true);
               m_serverEmail = serverEmail;
       }       //      EMail

       /**     From Address                            */
       private InternetAddress     m_from;
       /** To Address                                  */
       private ArrayList<InternetAddress>      m_to;
       /** CC Addresses                                */
       private ArrayList<InternetAddress>      m_cc;
       /** BCC Addresses                               */
       private ArrayList<InternetAddress>      m_bcc;
       /**     Reply To Address                        */
       private InternetAddress         m_replyTo;
       /**     Mail Subject                            */
       private String                          m_subject;
       /** Mail Plain Message                  */
       private String                          m_messageText;
       /** Mail HTML Message                   */
       private String                          m_messageHTML;
       /**     Mail SMTP Server                        */
       private String                          m_smtpHost;
       /** SMPT Port                                   */
       private int                                     m_smtpPort = 25;
       /** SMPT TLS                                    */
       private boolean                         m_isSmtpTLS = false;
       /**     Attachments                                     */
       private ArrayList<Object>       m_attachments;
       /**     UserName and Password           */
       private EMailAuthenticator      m_auth = null;
       /**     Message                                         */
       private SMTPMessage             m_msg = null;
       /**     Send from Server                        */
       private boolean                 m_serverEmail = true;
       /** Context - may be null               */
       private Ctx                                     m_ctx;

       /**     Info Valid                                      */
       private boolean         m_valid = false;

       /** Send result Message                 */
       private String          m_sentMsg = null;

       /**     Mail Sent OK Status                             */
       public static final String      SENT_OK = "OK";

       /**     Logger                                                  */
       protected static final CLogger          log = CLogger.getCLogger (EMail.class);


       private String sendServer(){
               Server server = CConnection.get().getServer();
               try
               {
                       String username = null;
                       String password = null;
                       if (m_auth!=null){
                               PasswordAuthentication authentication =  m_auth.getPasswordAuthentication();
                               if (authentication!=null){
                                       username = authentication.getUserName();
                                       password = authentication.getPassword();
                               }
                       }
                       if (server != null)
                       {       //      See ServerBean
                               return server.sendEMail(this, username, password);
                       }
                       else
                               log.log(Level.WARNING, "No AppsServer");
                           return "Error: No AppsServer";
               }
               catch (RemoteException ex)
               {
                       log.log(Level.SEVERE, "AppsServer error", ex);
                       return ex.toString();
               }
       }

       /**
        *      Send Mail direct
        *      @return OK or error message
        */
       public String send ()
       {
               if (m_serverEmail && Ini.isClient()) {
            	   m_sentMsg = sendServer();
            	   return m_sentMsg;
               }

               log.info("(" + m_smtpHost + ") " + m_from + " -> " + m_to);
               m_sentMsg = null;
               //
               if (!isValid(true))
               {
                       m_sentMsg = "Invalid Data";
                       return m_sentMsg;
               }
               //
               Properties props = System.getProperties();
               props.put("mail.store.protocol", "smtp");
               props.put("mail.transport.protocol", "smtp");
               props.put("mail.host", m_smtpHost);
               if (CLogMgt.isLevelFinest())
                       props.put("mail.debug", "true");
               //
               //      Bit-Florin David
       props.put("mail.smtp.port", String.valueOf(m_smtpPort));
       props.put("mail.smtp.connectiontimeout", "20000" );
       props.put("mail.smtp.timeout", "20000" );
//
               //      TLS settings
               if (m_isSmtpTLS)
               {
                       props.put("mail.smtp.starttls.enable", "true");
               props.put("mail.smtp.socketFactory.port", String.valueOf(m_smtpPort));
                       props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                       props.put("mail.smtp.socketFactory.fallback", "false");
               }

               Session session = null;
               try
               {
                       if (m_auth != null)             //      createAuthenticator was called
                               props.put("mail.smtp.auth", "true");
                       session = Session.getInstance(props, m_auth);
                       session.setDebug(CLogMgt.isLevelFinest());
               }
               catch (SecurityException se)
               {
                       log.log(Level.WARNING, "Auth=" + m_auth + " - " + se.toString());
                       m_sentMsg = se.toString();
                       return se.toString();
               }
               catch (Exception e)
               {
                       log.log(Level.SEVERE, "Auth=" + m_auth, e);
                       m_sentMsg = e.toString();
                       return e.toString();
               }

               try
               {
               //      m_msg = new MimeMessage(session);
                       m_msg = new SMTPMessage(session);
                       //      Addresses
                       m_msg.setFrom(m_from);
                       InternetAddress[] rec = getTos();
                       if (rec.length == 1)
                               m_msg.setRecipient (Message.RecipientType.TO, rec[0]);
                       else
                               m_msg.setRecipients (Message.RecipientType.TO, rec);
                       rec = getCcs();
                       if (rec != null && rec.length > 0)
                               m_msg.setRecipients (Message.RecipientType.CC, rec);
                       rec = getBccs();
                       if (rec != null && rec.length > 0)
                               m_msg.setRecipients (Message.RecipientType.BCC, rec);
                       if (m_replyTo != null)
                               m_msg.setReplyTo(new Address[] {m_replyTo});
                       //
                       m_msg.setSentDate(new java.util.Date());
                       m_msg.setHeader("Comments", "CompiereMail");
               //      m_msg.setDescription("Description");
                       //      SMTP specifics
                       m_msg.setAllow8bitMIME(true);
                       //      Send notification on Failure & Success - no way to set envid in Java yet
               //      m_msg.setNotifyOptions (SMTPMessage.NOTIFY_FAILURE | SMTPMessage.NOTIFY_SUCCESS);
                       //      Bounce only header
                       m_msg.setReturnOption (SMTPMessage.RETURN_HDRS);
               //      m_msg.setHeader("X-Mailer", "msgsend");
                       //
                       setContent();
                       m_msg.saveChanges();
               //      log.fine("message =" + m_msg);
                       //
               //      Transport.send(msg);
                       Transport t = session.getTransport("smtp");
               //      log.fine("transport=" + t);
                       t.connect();
               //      t.connect(m_smtpHost, user, password);
               //      log.fine("transport connected");
                       Transport.send(m_msg);
               //      t.sendMessage(msg, msg.getAllRecipients());
                       log.fine("Success - MessageID=" + m_msg.getMessageID());
               }
               catch (MessagingException me)
               {
                       Exception ex = me;
                       StringBuffer sb = new StringBuffer("(ME)");
                       boolean printed = false;
                       do
                       {
                               if (ex instanceof SendFailedException)
                               {
                                       SendFailedException sfex = (SendFailedException)ex;
                                       Address[] invalid = sfex.getInvalidAddresses();
                                       if (!printed)
                                       {
                                               if (invalid != null && invalid.length > 0)
                                               {
                                                       sb.append (" - Invalid:");
                                                       for (Address element : invalid)
                                                               sb.append (" ").append (element);

                                               }
                                               Address[] validUnsent = sfex.getValidUnsentAddresses ();
                                               if (validUnsent != null && validUnsent.length > 0)
                                               {
                                                       sb.append (" - ValidUnsent:");
                                                       for (Address element : validUnsent)
                                                               sb.append (" ").append (element);
                                               }
                                               Address[] validSent = sfex.getValidSentAddresses ();
                                               if (validSent != null && validSent.length > 0)
                                               {
                                                       sb.append (" - ValidSent:");
                                                       for (Address element : validSent)
                                                               sb.append (" ").append (element);
                                               }
                                               printed = true;
                                       }
                                       if (sfex.getNextException() == null)
                                               sb.append(" ").append(sfex.getLocalizedMessage());
                               }
                               else if (ex instanceof AuthenticationFailedException)
                               {
                                       sb.append(" - Invalid Username/Password - " + m_auth);
                               }
                               else    //      other MessagingException
                               {
                                       String msg = ex.getLocalizedMessage();
                                       if (msg == null)
                                               sb.append(": ").append(ex.toString());
                                       else
                                       {
                                               if (msg.indexOf("Could not connect to SMTP host:") != -1)
                                               {
                                                       int index = msg.indexOf('\n');
                                                       if (index != -1)
                                                               msg = msg.substring(0, index);
                                               }
                                               String className = ex.getClass().getName();
                                               if (className.indexOf("MessagingException") != -1)
                                                       sb.append(": ").append(msg);
                                               else if (className.indexOf("ConnectException") != -1)
                                                       sb.append(" ").append(msg);
                                               else
                                                       sb.append(" ").append(className).append(": ").append(msg);

                                       }
                               }
                               //      Next Exception
                               if (ex instanceof MessagingException)
                                       ex = ((MessagingException)ex).getNextException();
                               else
                                       ex = null;
                       }
                       while (ex != null);     //      error loop
                       //
                       if (CLogMgt.isLevelFinest())
                               log.log(Level.WARNING, sb.toString(), me);
                       else
                               log.log(Level.WARNING, sb.toString());
                       m_sentMsg = sb.toString();
                       return sb.toString();
               }
               catch (Exception e)
               {
                       log.log(Level.SEVERE, "", e);
                       m_sentMsg = e.getLocalizedMessage();
                       return e.getLocalizedMessage();
               }
               //
               if (CLogMgt.isLevelFinest())
                       dumpMessage();
               m_sentMsg = SENT_OK;
               return m_sentMsg;
       }       //      send

       /**
        *      Get Send Result Msg
        *      @return msg
        */
       public String getSentMsg()
       {
               return m_sentMsg;
       }       //      getSentMsg

       /**
        *      Was sending the Msg OK
        *      @return msg == OK
        */
       public boolean isSentOK()
       {
               return m_sentMsg != null && SENT_OK.equals(m_sentMsg);
       }       //      isSentOK

       /**
        *      Dump Message Info
        */
       private void dumpMessage()
       {
               if (m_msg == null)
                       return;
               try
               {
                       Enumeration<?> e = m_msg.getAllHeaderLines ();
                       while (e.hasMoreElements())
                               log.fine("- " + e.nextElement());
               }
               catch (MessagingException ex)
               {
                       log.log(Level.WARNING, m_msg.toString(), ex);
               }
       }       //      dumpMessage

       /**
        *      Get the message directly
        *      @return mail message
        */
       protected MimeMessage getMimeMessage()
       {
               return m_msg;
       }       //      getMessage

       /**
        *      Get Message ID or null
        *      @return Message ID e.g. <20030130004739.15377.qmail@web13506.mail.yahoo.com>
        *  <25699763.1043887247538.JavaMail.jjanke@main>
        */
       public String getMessageID()
       {
               try
               {
                       if (m_msg != null)
                               return m_msg.getMessageID ();
               }
               catch (MessagingException ex)
               {
                       log.log(Level.SEVERE, "", ex);
               }
               return null;
       }       //      getMessageID

       /**     Getter/Setter ********************************************************/

       /**
        *      Create Authenticator for User
        *      @param username user name
        *      @param password user password
        *      @return Authenticator or null
        */
	public EMailAuthenticator createAuthenticator (String username, String password)
       {
               if (username == null || password == null)
               {
                       log.warning("Ignored - " +  username + "/" + password);
                       m_auth = null;
               }
               else
               {
               //      log.fine("setEMailUser: " + username + "/" + password);
                       m_auth = new EMailAuthenticator (username, password);
               }
               return m_auth;
       }       //      createAuthenticator

       /**
        *  Get Sender
        *  @return Sender's internet address
        */
       public InternetAddress getFrom()
       {
               return m_from;
       }   //  getFrom

       /**
        *  Set Sender
        *  @param newFromEMail Sender's email address
        *  @return false if not valid
        */
       public boolean setFrom(String newFromEMail, String newFromName)
       {
               if (newFromEMail == null)
               {
                       m_valid = false;
                       return m_valid;
               }
               try
               {
                       if (newFromName == null)
                               m_from = new InternetAddress (newFromEMail, true);
                       else
                               m_from = new InternetAddress (newFromEMail, newFromName);
                       m_valid = true;
               }
               catch (Exception e)
               {
                       log.log(Level.WARNING, newFromEMail + ": " + e.toString());
                       m_valid = false;
               }
               return m_valid;
       }   //  setFrom

       /**
        *  Add To Recipient
        *  @param newToEMail Recipient's email address
        *      @return true if valid
        */
       public boolean addTo (String newToEMail, String newToName)
       {
               if (newToEMail == null || newToEMail.length() == 0)
               {
                       m_valid = false;
                       return m_valid;
               }
               InternetAddress ia = null;
               try
               {
                       if (newToName == null)
                               ia = new InternetAddress (newToEMail, true);
                       else
                               ia = new InternetAddress (newToEMail, newToName);
               }
               catch (Exception e)
               {
                       log.log(Level.WARNING, newToEMail + ": " + e.toString());
                       m_valid = false;
                       return m_valid;
               }
               if (m_to == null)
                       m_to = new ArrayList<InternetAddress>();
               m_to.add(ia);
               return m_valid;
       }   //  addTo

       /**
        *  Get Recipient
        *  @return Recipient's internet address
        */
       public InternetAddress getTo()
       {
               if (m_to == null || m_to.size() == 0)
                       return null;
               InternetAddress ia = m_to.get(0);
               return ia;
       }   //  getTo

       /**
        *  Get TO Recipients
        *  @return Recipient's internet address
        */
       public InternetAddress[] getTos()
       {
               if (m_to == null || m_to.size() == 0)
                       return null;
               InternetAddress[] ias = new InternetAddress[m_to.size()];
               m_to.toArray(ias);
               return ias;
       }   //  getTos

       /**
        *      Add CC Recipient
        *      @param newCc EMail cc Recipient
        *      @return true if valid
        */
       public boolean addCc (String newCc)
       {
               if (newCc == null || newCc.length() == 0)
                       return false;
               InternetAddress ia = null;
               try
               {
                       ia = new InternetAddress (newCc, true);
               }
               catch (Exception e)
               {
                       log.log(Level.WARNING, newCc + ": " + e.toString());
                       return false;
               }
               if (m_cc == null)
                       m_cc = new ArrayList<InternetAddress>();
               m_cc.add (ia);
               return true;
       }       //      addCc

       /**
        *  Get CC Recipients
        *  @return Recipient's internet address
        */
       public InternetAddress[] getCcs()
       {
               if (m_cc == null || m_cc.size() == 0)
                       return null;
               InternetAddress[] ias = new InternetAddress[m_cc.size()];
               m_cc.toArray(ias);
               return ias;
       }   //  getCcs

       /**
        *      Add BCC Recipient
        *      @param newBcc EMail cc Recipient
        *      @return true if valid
        */
       public boolean addBcc (String newBcc)
       {
               if (newBcc == null || newBcc.length() == 0)
                       return false;
               InternetAddress ia = null;
               try
               {
                       ia = new InternetAddress (newBcc, true);
               }
               catch (Exception e)
               {
                       log.log(Level.WARNING, newBcc + ": " + e.getMessage());
                       return false;
               }
               if (m_bcc == null)
                       m_bcc = new ArrayList<InternetAddress>();
               m_bcc.add (ia);
               return true;
       }       //      addBcc

       /**
        *  Get BCC Recipients
        *  @return Recipient's internet address
        */
       public InternetAddress[] getBccs()
       {
               if (m_bcc == null || m_bcc.size() == 0)
                       return null;
               InternetAddress[] ias = new InternetAddress[m_bcc.size()];
               m_bcc.toArray(ias);
               return ias;
       }   //  getBccs

       /**
        *  Set Reply to Address
        *  @param newTo email address
        *      @return true if valid
        */
       public boolean setReplyTo (String newTo)
       {
               if (newTo == null || newTo.length() == 0)
                       return false;
               InternetAddress ia = null;
               try
               {
                       ia = new InternetAddress (newTo, true);
               }
               catch (Exception e)
               {
                       log.log(Level.WARNING, newTo + ": " + e.toString());
                       return false;
               }
               m_replyTo = ia;
               return true;
       }   //  setReplyTo

       /**
        *  Get Reply To
        *  @return Reoly To internet address
        */
       public InternetAddress getReplyTo()
       {
               return m_replyTo;
       }   //  getReplyTo


       /**************************************************************************
        *  Set Subject
        *  @param newSubject Subject
        */
       public void setSubject(String newSubject)
       {
               if (newSubject == null || newSubject.length() == 0)
                       m_valid = false;
               else
                       m_subject = newSubject;
       }   //  setSubject

       /**
        *  Get Subject
        *  @return subject
        */
       public String getSubject()
       {
               return m_subject;
       }   //  getSubject

       /**
        *  Set Message
        *  @param newMessage message
        */
       public void setMessageText (String newMessage)
       {
               if (newMessage == null || newMessage.length() == 0)
                       m_valid = false;
               else
               {
                       m_messageText = newMessage;
                       if (!m_messageText.endsWith("\n"))
                               m_messageText += "\n";
               }
       }   //  setMessage

       /**
        *  Get MIME String Message - line ending with CRLF.
        *  @return message
        */
       public String getMessageCRLF()
       {
               if (m_messageText == null)
                       return "";
               char[] chars = m_messageText.toCharArray();
               StringBuffer sb = new StringBuffer();
               for (int i = 0; i < chars.length; i++)
               {
                       char c = chars[i];
                       if (c == '\n')
                       {
                               int previous = i-1;
                               if (previous >= 0 && chars[previous] == '\r')
                                       sb.append(c);
                               else
                                       sb.append("\r\n");
                       }
                       else
                               sb.append(c);
               }
       //      log.fine("IN  " + m_messageText);
       //      log.fine("OUT " + sb);

               return sb.toString();
       }   //  getMessageCRLF

       /**
        *  Set HTML Message
        *  @param html message
        */
       public void setMessageHTML (String html)
       {
               if (html == null || html.length() == 0)
                       m_valid = false;
               else
               {
                       m_messageHTML = html;
                       if (!m_messageHTML.endsWith("\n"))
                               m_messageHTML += "\n";
               }
       }   //  setMessageHTML

       /**
        *  Set HTML Message
        *  @param subject subject repeated in message as H2
        *      @param message message
        */
       public void setMessageHTML (String subject, String message)
       {
               m_subject = subject;
               StringBuffer sb = new StringBuffer("<HTML>\n")
                       .append("<HEAD>\n")
                       .append("<TITLE>\n")
                       .append(subject + "\n")
                       .append("</TITLE>\n")
                       .append("</HEAD>\n");
               sb.append("<BODY>\n")
                       .append("<H2>" + subject + "</H2>" + "\n")
                       .append(message)
                       .append("\n")
                       .append("</BODY>\n");
               sb.append("</HTML>\n");
               m_messageHTML = sb.toString();
       }   //  setMessageHTML

       /**
        *  Get HTML Message
        *  @return message
        */
       public String getMessageHTML()
       {
               return m_messageHTML;
       }   //  getMessageHTML

       /**
        *      Add file Attachment
        *      @param file file to attach
        */
       public void addAttachment (File file)
       {
               if (file == null)
                       return;
               if (m_attachments == null)
                       m_attachments = new ArrayList<Object>();
               if (m_serverEmail){
                       // read into bytes
                       byte[] bytes = null;
                       InputStream is = null;
                       try {
                    	   is = new FileInputStream(file);
                    	   long length = file.length();
                    	   if (length>Integer.MAX_VALUE){
                    		   throw new IOException("File "+file.getName()+" is too large");
                    	   }
                    	   bytes = new byte[(int)length];
                    	   int offset = 0;
                    	   int numRead = 0;
                    	   while(offset<bytes.length && (numRead=is.read(bytes,offset,bytes.length - offset)) >= 0){
                    		   offset += numRead;
                    	   }
                    	   if (offset < bytes.length) {
                    		   throw new IOException("Could not complete read file "+file.getName());
                    	   }
                    	   is.close();
                       } catch (IOException e){
                    	   log.log(Level.SEVERE, "Cannot attach file to email: ", e);
                       }
                       m_attachments.add(new Attachment(bytes, "application/pdf", file.getName()));
               }
               else m_attachments.add(file);
       }       //      addAttachment

       /**
        *      Add url based file Attachment
        *      @param url url content to attach
        */
       public void addAttachment (URL url)
       {
               if (url == null)
                       return;
               if (m_attachments == null)
                       m_attachments = new ArrayList<Object>();
               m_attachments.add(url);
       }       //      addAttachment

       /**
        *      Add attachment.
        *  (converted to ByteArrayDataSource)
        *      @param data data
        *      @param type MIME type
        *      @param name name of attachment
        */
       public void addAttachment (byte[] data, String type, String name)
       {
               ByteArrayDataSource byteArray = new ByteArrayDataSource (data,type).setName(name);
               addAttachment (byteArray);
       }       //      addAttachment

       /**
        *      Add arbitary Attachment
        *      @param dataSource content to attach
        */
       public void addAttachment (DataSource dataSource)
       {
               if (dataSource == null)
                       return;
               if (m_attachments == null)
                       m_attachments = new ArrayList<Object>();
               m_attachments.add(dataSource);
       }       //      addAttachment

       /**
        *      Set the message content
        *      @throws MessagingException
        *      @throws IOException
        */
       private void setContent ()
               throws MessagingException, IOException
       {
               //      Local Character Set
               String charSetName = System.getProperty("file.encoding");       //      Cp1252
               if (charSetName == null || charSetName.length() == 0)
                       charSetName = "UTF-8";  // WebEnv.ENCODING - alternative iso-8859-1
               //
               m_msg.setSubject (getSubject(), charSetName);

               //      Simple Message
               if (m_attachments == null || m_attachments.size() == 0)
               {
                       if (m_messageHTML == null || m_messageHTML.length () == 0)
                               m_msg.setText (getMessageCRLF(), charSetName);
                       else
                               m_msg.setDataHandler (new DataHandler
                                       (new ByteArrayDataSource (m_messageHTML, charSetName, "text/html")));
                       //
                       log.fine("(simple) " + getSubject());
               }
               else    //      Multi part message      ***************************************
               {
                       //      First Part - Message
                       MimeBodyPart mbp_1 = new MimeBodyPart();
                       mbp_1.setText("");
                       if (m_messageHTML == null || m_messageHTML.length () == 0)
                               mbp_1.setText (getMessageCRLF(), charSetName);
                       else
                               mbp_1.setDataHandler (new DataHandler
                                       (new ByteArrayDataSource (m_messageHTML, charSetName, "text/html")));

                       // Create Multipart and its parts to it
                       Multipart mp = new MimeMultipart();
                       mp.addBodyPart(mbp_1);
                       log.fine("(multi) " + getSubject() + " - " + mbp_1);

                       //      for all attachments
                       for (int i = 0; i < m_attachments.size(); i++)
                       {
                               Object attachment = m_attachments.get(i);
                               DataSource ds = null;
                               if (attachment instanceof File)
                               {
                                       File file = (File)attachment;
                                       if (file.exists())
                                               ds = new FileDataSource (file);
                                       else
                                       {
                                               log.log(Level.WARNING, "File does not exist: " + file);
                                               continue;
                                       }
                               }
                               else if (attachment instanceof URL)
                               {
                                       URL url = (URL)attachment;
                                       ds = new URLDataSource (url);
                               }
                               else if (attachment instanceof DataSource)
                                       ds = (DataSource)attachment;
                               else if (attachment instanceof Attachment){
                                       Attachment a = (Attachment)attachment;
                                       ds = new ByteArrayDataSource (a.content, a.contentType).setName(a.fileName);
                               }
                               else
                               {
					log.log(Level.WARNING, "Attachement type unknown: " + attachment);
                                       continue;
                               }
                               //      Attachment Part
                               MimeBodyPart mbp_2 = new MimeBodyPart();
                               mbp_2.setDataHandler(new DataHandler(ds));
                               mbp_2.setFileName(ds.getName());
                               log.fine("Added Attachment " + ds.getName() + " - " + mbp_2);
                               mp.addBodyPart(mbp_2);
                       }

                       //      Add to Message
                       m_msg.setContent(mp);
               }       //      multi=part
       }       //      setContent


       /**************************************************************************
        *  Set SMTP Host or address
        *  @param newSmtpHost Mail server
        */
       public void setSmtpHost(String newSmtpHost)
       {
               if (newSmtpHost == null || newSmtpHost.length() == 0)
                       m_valid = false;
               else
                       m_smtpHost = newSmtpHost;
       }   //  setSMTPHost

       /**
        *  Get Mail Server name or address
        *  @return mail server
        */
       public String getSmtpHost()
       {
               return m_smtpHost;
       }   //  getSmtpHosr

       /**
        *  Is Info valid to send EMail
        *  @return true if email is valid and can be sent
        */
       public boolean isValid()
       {
               return m_valid;
       }   //  isValid

       /**
        *  Re-Check Info if valid to send EMail
        *      @param recheck if true check main variables
        *  @return true if email is valid and can be sent
        */
       public boolean isValid (boolean recheck)
       {
               if (!recheck)
                       return m_valid;

               //  From
               if (m_from == null
                       || m_from.getAddress().length() == 0
                       || m_from.getAddress().indexOf(' ') != -1)
               {
                       log.warning("From is invalid=" + m_from);
                       return false;
               }
               //      To
               InternetAddress[] ias = getTos();
               if (ias == null)
               {
                       log.warning("No To");
                       return false;
               }
               for (int i = 0; i < ias.length; i++)
               {
                       if (ias[i] == null
                               || ias[i].getAddress().length() == 0
                               || ias[i].getAddress().indexOf(' ') != -1)
                       {
                               log.warning("To(" + i + ") is invalid=" + ias[i]);
                               return false;
                       }
               }

               //      Host
               if (m_smtpHost == null || m_smtpHost.length() == 0)
               {
                       log.warning("SMTP Host is invalid" + m_smtpHost);
                       return false;
               }

               //      Subject
               if (m_subject == null || m_subject.length() == 0)
               {
                       log.warning("Subject is invalid=" + m_subject);
                       return false;
               }
               return true;
       }   //  isValid

       /**
        *      String Representation
        *      @return info
        */
       @Override
       public String toString ()
       {
               StringBuffer sb = new StringBuffer ("EMail[")
                       .append(m_smtpHost)
                       .append(":").append(m_smtpPort);
               if (m_isSmtpTLS)
                       sb.append("(TLS)");
               sb.append(",From:").append(m_from)
                       .append(",To:").append(getTo())
                       .append(",Subject=").append(getSubject());
               if (m_valid)
                       sb.append(",valid");
               else
                       sb.append(",NOTvalid");
               sb.append ("]");
               return sb.toString ();
       }       //      toString

       /**************************************************************************
        *  Test.
        *  java -cp CTools.jar;CClient.jar org.compiere.util.EMail main info@compiere.org jjanke@compiere.org "My Subject"  "My Message"
        *      --
        *      If you get SendFailedException: 550 5.7.1 Unable to relay for ..
        *      Check:
        *      - Does the SMTP server allow you to relay
        *    (Exchange: SMTP server - Access)
        *  - Did you authenticate (setEmailUser)
        *  @param args Array of arguments
        */
       public static void main (String[] args)
       {
               org.compiere.Compiere.startup(true);

               if (args.length != 5)
               {
                       System.out.println("Parameters: smtpHost from to subject message");
                       System.out.println("Example: java org.compiere.util.EMail mail.acme.com joe@acme.com sue@acme.com HiThere CheersJoe");
                       System.exit(1);
               }
               EMail email = new EMail(new Ctx(), args[0], 25, false,
                       args[1], args[1], args[2], args[2], args[3], args[4], true);
               email.send();
       }   //  main

}       //      EMail