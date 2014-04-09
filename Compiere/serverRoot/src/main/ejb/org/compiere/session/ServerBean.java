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
package org.compiere.session;

import java.io.NotSerializableException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.RowSet;

import org.compiere.Compiere;
import org.compiere.acct.Doc;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.controller.GridFieldVO;
import org.compiere.controller.GridTabVO;
import org.compiere.framework.POInfo;
import org.compiere.framework.POInfoColumn;
import org.compiere.framework.Query;
import org.compiere.model.GridWindowVO;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MLookupInfo;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentProcessor;
import org.compiere.model.MSequence;
import org.compiere.model.MTask;
import org.compiere.model.MUser;
import org.compiere.model.PaymentProcessor;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.server.*;
import org.compiere.util.CLogger;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.CStatementVO;
import org.compiere.util.CacheMgt;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.compiere.wf.MWorkflow;

/**
 * 	Compiere Server Bean.
 *
 *  @ejb.bean name="compiere/Server"
 *		display-name="Compiere Server Session Bean"
 *		type="Stateless"
 *		view-type="both"
 *      transaction-type="Bean"
 *      jndi-name="compiere/Server"
 *      local-jndi-name="compiere/ServerLocal"
 *
 *  @ejb.ejb-ref ejb-name="compiere/Server"
 *  	view-type="both"
 *		ref-name="compiere/Server"
 *  @ejb.ejb-ref ejb-name="compiere/Server"
 *  	view-type="local"
 *		ref-name="compiere/ServerLocal"
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ServerBean.java 9198 2010-08-25 23:18:52Z rthng $
 */
public class ServerBean implements SessionBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logger				*/
	private static CLogger log = CLogger.getCLogger(ServerBean.class);
	//
	private static int		s_no = 0;
	private int				m_no = 0;
	//
	private int				m_windowCount = 0;
	private int				m_postCount = 0;
	private int				m_processCount = 0;
	private int				m_workflowCount = 0;
	private int				m_paymentCount = 0;
	private int				m_nextSeqCount = 0;
	private int				m_stmt_rowSetCount = 0;
	private int				m_stmt_updateCount = 0;
	private int				m_cacheResetCount = 0;
	private int				m_updateLOBCount = 0;
	private int				m_restartCount = 0;

	/**
	 *  Get and create Window Model Value Object
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param ctx   Environment Properties
	 *  @param WindowNo  number of this window
	 *  @param AD_Window_ID  the internal number of the window, if not 0, AD_Menu_ID is ignored
	 *  @param AD_Menu_ID ine internal menu number, used when AD_Window_ID is 0
	 *  @return initialized Window Model
	 */
	public GridWindowVO getWindowVO (Ctx ctx, int WindowNo, int AD_Window_ID, int AD_Menu_ID)
	{
		log.info ("getWindowVO[" + m_no + "] Window=" + AD_Window_ID);
	//	log.fine(ctx);
		GridWindowVO vo = GridWindowVO.create(ctx, WindowNo, AD_Window_ID, AD_Menu_ID);
		m_windowCount++;
		return vo;
	}	//	getWindowVO


	/**
	 *  Post Immediate
	 *  @ejb.interface-method view-type="both"
	 *
	 *	@param	ctx Client Context
	 *  @param  AD_Client_ID    Client ID of Document
	 *  @param  AD_Table_ID     Table ID of Document
	 *  @param  Record_ID       Record ID of this document
	 *  @param  force           force posting
	 *  @param trx transaction
	 *  @return null, if success or error message
	 */
	public String postImmediate (Ctx ctx, 
		int AD_Client_ID, int AD_Table_ID, int Record_ID, boolean force, Trx trx)
	{
		log.info ("[" + m_no + "] Table=" + AD_Table_ID + ", Record=" + Record_ID);
		m_postCount++;
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(ctx, AD_Client_ID);
		return Doc.postImmediate(ass, AD_Table_ID, Record_ID, force, trx);
	}	//	postImmediate

	/*************************************************************************
	 *  Get Prepared Statement ResultSet
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param info Result info
	 *  @return RowSet
	 *  @throws NotSerializableException
	 */
	public RowSet pstmt_getRowSet (CStatementVO info) 
		throws NotSerializableException
	{
		log.finer("[" + m_no + "]");
		m_stmt_rowSetCount++;
		try
		{
			CPreparedStatement pstmt = new CPreparedStatement(info);
			return pstmt.remote_getRowSet();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, info.getSql(), e);
		}
		return null;
	}	//	pstmt_getRowSet

	/**
	 *  Get Statement ResultSet
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param info Result info
	 *  @return RowSet
	 */
	public RowSet stmt_getRowSet (CStatementVO info)
	{
		log.finer("[" + m_no + "]");
		m_stmt_rowSetCount++;
		CPreparedStatement stmt = new CPreparedStatement(info);
		return stmt.remote_getRowSet();
	}	//	stmt_getRowSet

	/**
	 *  Execute Update
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param info Result info
	 *  @return row count
	 */
	public int stmt_executeUpdate (CStatementVO info)
	{
		log.finer("[" + m_no + "]");
		m_stmt_updateCount++;
		CPreparedStatement pstmt = new CPreparedStatement(info);
		return pstmt.remote_executeUpdate();
	}	//	stmt_executeUpdate

	/*************************************************************************
	 *	Get next number for Key column = 0 is Error.
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param AD_Client_ID client
	 *  @param TableName table name
	 * 	@param trx optional Transaction Name
	 *  @return next no
	 */
	public int getNextID (int AD_Client_ID, String TableName, Trx trx)
	{
		int retValue = MSequence.getNextID (AD_Client_ID, TableName);
		log.finer("[" + m_no + "] " + TableName + " = " + retValue);
		m_nextSeqCount++;
		return retValue;
	}	//	getNextID

	/**
	 * 	Get Document No from table
	 *  @ejb.interface-method view-type="both"
	 * 
	 *	@param AD_Client_ID client
	 *	@param TableName table name
	 * 	@param trx optional Transaction Name
	 *	@return document no or null
	 */
	public String getDocumentNo (int AD_Client_ID, String TableName, Trx trx)
	{
		m_nextSeqCount++;
		String dn = MSequence.getDocumentNo (AD_Client_ID, TableName, trx);
		if (dn == null)		//	try again
			dn = MSequence.getDocumentNo (AD_Client_ID, TableName, trx);
		return dn;
	}	//	GetDocumentNo
	
	/**
	 * 	Get Document No based on Document Type
	 *  @ejb.interface-method view-type="both"
	 * 
	 *	@param C_DocType_ID document type
	 * 	@param trx optional Transaction Name
	 *	@return document no or null
	 */
	public String getDocumentNo (int C_DocType_ID, Trx trx)
	{
		m_nextSeqCount++;
		String dn = MSequence.getDocumentNo (C_DocType_ID, trx);
		if (dn == null)		//	try again
			dn = MSequence.getDocumentNo (C_DocType_ID, trx);
		return dn;
	}	//	getDocumentNo


	/*************************************************************************
	 *  Process Remote
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param ctx Context
	 *  @param pi Process Info
	 *  @return resulting Process Info
	 */
    public ProcessInfo process (Ctx ctx, ProcessInfo pi)
	{
		String className = pi.getClassName();
		log.info(className + " - " + pi);
		m_processCount++;
		//	Get Class
		Class clazz = null;		//	XDoclet: no generics!
		try
		{
			clazz = Class.forName (className);
		}
		catch (ClassNotFoundException ex)
		{
			log.log(Level.WARNING, className, ex);
			pi.setSummary ("ClassNotFound", true);
			return pi;
		}
		//	Get Process
		SvrProcess process = null;
		try
		{
			process = (SvrProcess)clazz.newInstance ();
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "Instance for " + className, ex);
			pi.setSummary ("InstanceError", true);
			return pi;
		}
		//	Start Process
		Trx p_trx = Trx.get("ServerPrc");
		try
		{
			process.startProcess (ctx, pi, p_trx);
			pi = process.getProcessInfo();
			p_trx.commit();
			p_trx.close();
		}
		catch (Exception ex1)
		{
			p_trx.rollback();
			p_trx.close();
			pi.setSummary ("ProcessError", true);
			return pi;
		}
		return pi;
	}	//	process


	/*************************************************************************
	 *  Run Workflow (and wait) on Server
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param ctx Context
	 *  @param pi Process Info
	 *  @param AD_Workflow_ID id
	 *  @return process info
	 */
	public ProcessInfo workflow (Ctx ctx, ProcessInfo pi, int AD_Workflow_ID)
	{
		log.info ("[" + m_no + "] " + AD_Workflow_ID);
		m_workflowCount++;
		MWorkflow wf = MWorkflow.get (ctx, AD_Workflow_ID);
		if (pi.isBatch()){
			WorkflowProcessor wp = WorkflowProcessor.getWorkflowProcessor(wf.getBackground_WFProcessor_ID());
			wf.start(pi, wp.getExecutor());
		}
		else {
			WorkflowProcessor wp = WorkflowProcessor.getWorkflowProcessor(wf.getAD_WorkflowProcessor_ID());
			wf.startWait(pi, wp.getExecutor());
		}
		log.fine(pi.toString());
		return pi;
	}	//	workflow

	/**
	 *  Online Payment from Server
	 *  @ejb.interface-method view-type="both"
	 *	Called from MPayment processOnline
	 *  @param ctx Context
	 *  @param C_Payment_ID payment
	 *  @param C_PaymentProcessor_ID processor
	 *  @param trx transaction
	 *  @return true if approved
	 */
	public boolean paymentOnline (Ctx ctx, 
		int C_Payment_ID, int C_PaymentProcessor_ID, Trx trx)
	{
		MPayment payment = new MPayment (ctx, C_Payment_ID, trx);
		MPaymentProcessor mpp = new MPaymentProcessor (ctx, C_PaymentProcessor_ID, null);
		log.info ("[" + m_no + "] " + payment + " - " + mpp);
		m_paymentCount++;
		boolean approved = false;
		try
		{
			PaymentProcessor pp = PaymentProcessor.create(mpp, payment);
			if (pp == null)
				payment.setErrorMessage("No Payment Processor");
			else
			{
				approved = pp.processCC ();
				if (approved)
					payment.setErrorMessage(null);
				else
					payment.setErrorMessage("From " +  payment.getCreditCardName() 
						+ ": " + payment.getR_RespMsg());
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			payment.setErrorMessage("Payment Processor Error");
		}
		payment.save();	
		return approved;
	}	//	paymentOnline

	/**
	 *  Create EMail from Server (Request User)
	 *  @ejb.interface-method view-type="both"
	 *  @param ctx Context
	 *  @param AD_Client_ID client 
	 *	@param toEMail recipient email address
	 *	@param subject subject
	 *	@param message message
	 *  @return EMail
	 */
	public EMail createEMail (Ctx ctx, int AD_Client_ID, 
		String toEMail, String toName, String subject, String message)
	{
		MClient client = MClient.get(ctx, AD_Client_ID);
		return client.createEMail(toEMail, toName, subject, message);
	}	//	createEMail
	
	/**
	 * Send EMail from Server 
	 * @ejb.interface-method view-type="both"
	 * @param Email email
	 * @param String user
	 * @param String password
	 * @return String    */
	public String sendEMail( org.compiere.util.EMail email, String user, String password ) {
		email.createAuthenticator(user, password); 
		return email.send();
	}

	/**
	 *  Create EMail from Server (Request User)
	 *  @ejb.interface-method view-type="both"
	 *  @param ctx Context
	 *  @param AD_Client_ID client 
	 *  @param AD_User_ID user to send email from
	 *	@param toEMail recipient email address
	 *	@param subject subject
	 *	@param message message
	 *  @return EMail or null
	 */
	public EMail createEMail (Ctx ctx, int AD_Client_ID,
		int AD_User_ID,
		String toEMail, String toName, String subject, String message)
	{
		MClient client = MClient.get(ctx, AD_Client_ID);
		MUser from = new MUser (ctx, AD_User_ID, null);
		return client.createEMail(from, toEMail, toName, subject, message);
	}	//	createEMail

	
	/**
	 *  Create EMail from Server (Request User)
	 *  @ejb.interface-method view-type="both"
	 *  @param AD_Task_ID task 
	 *  @return execution trace
	 */
	public String executeTask (int AD_Task_ID)
	{
		MTask task = new MTask (Env.getCtx(), AD_Task_ID, null);	//	Server Context
		return task.execute();
	}	//	executeTask
	
	
	/**
	 *  Cash Reset
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param tableName table name
	 *  @param Record_ID record or 0 for all
	 * 	@return number of records reset
	 */
	public int cacheReset (String tableName, int Record_ID)
	{
		log.config(tableName + " - " + Record_ID);
		m_cacheResetCount++;
		return CacheMgt.get().reset(tableName, Record_ID);
	}	//	cacheReset
	
	/*************************************************************************
	 *  Restart Application Servers/Processes
	 *  @ejb.interface-method view-type="both"
	 *  @return true if restarted
	 */
	public boolean restartProcesses()
	{
		log.config("");
		m_restartCount++;
		return CompiereServerMgr.restart();
	}	//	restartProcesses

	/**
	 *  LOB update
	 *  @ejb.interface-method view-type="both"
	 *
	 *  @param sql table name
	 *  @param displayType display type (i.e. BLOB/CLOB)
	 * 	@param value the data
	 * 	@return true if updated
	 */
	public boolean updateLOB (String sql, int displayType, Object value)
	{
		if (sql == null || value == null)
		{
			log.fine("No sql or data");
			return false;
		}
		log.fine(sql);
		m_updateLOBCount++;
		boolean success = true;
		Connection con = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = con.prepareStatement(sql);
			if (displayType == DisplayTypeConstants.TextLong)
				pstmt.setString(1, (String)value);
			else
				pstmt.setBytes(1, (byte[])value);
			pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			log.log(Level.FINE, sql, e);
			success = false;
		}
		finally
		{
			DB.closeStatement(pstmt);
		}
		//	Success - commit local p_trx
		if (success)
		{
			try
			{
				con.commit();
				con.close();
				con = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "commit" , e);
				success = false;
			}
		}
		//	Error - roll back
		if (!success)
		{
			log.severe ("rollback");
			try
			{
				con.rollback();
				con.close();
				con = null;
			}
			catch (Exception ee)
			{
				log.log(Level.SEVERE, "rollback" , ee);
			}
		}
		
		//	Clean Connection
		try
		{
			if (con != null)
				con.close();
			con = null;
		}
		catch (Exception e)
		{
			con = null;
		}
		return success;
	}	//	updateLOB

	
	/**************************************************************************
	 * 	Describes the instance and its content for debugging purpose
	 *  @ejb.interface-method view-type="both"
	 * 	@return Debugging information about the instance and its content
	 */
	public String getStatus()
	{
		StringBuffer sb = new StringBuffer("ServerBean[");
		sb.append(m_no)
			.append("-Window=").append(m_windowCount)
			.append(",Post=").append(m_postCount)
			.append(",Process=").append(m_processCount)
			.append(",NextSeq=").append(m_nextSeqCount)
			.append(",Workflow=").append(m_workflowCount)
			.append(",Payment=").append(m_paymentCount)
			.append(",RowSet=").append(m_stmt_rowSetCount)
			.append(",Update=").append(m_stmt_updateCount)
			.append(",CacheReset=").append(m_cacheResetCount)
			.append(",SvrRetart=").append(m_restartCount)
			.append(",UpdateLob=").append(m_updateLOBCount)
			.append("]");
		return sb.toString();
	}	//	getStatus


	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return getStatus();
	}	//	toString

	
	/**************************************************************************
	 * 	Create the Session Bean
	 * 	@throws EJBException
	 * 	@throws CreateException
	 *  @ejb.create-method view-type="both"
	 */
	public void ejbCreate() throws EJBException, CreateException
	{
		m_no = ++s_no;
		try
		{
			if (!Compiere.startup(false, "ServerBean"))
				throw new CreateException("Compiere could not start");
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "ejbCreate", ex);
		//	throw new CreateException ();
		}
		log.info ("#" + getStatus());
	}	//	ejbCreate


	// -------------------------------------------------------------------------
	// Framework Callbacks
	// -------------------------------------------------------------------------

	/**
	 * Method setSessionContext
	 * @param aContext SessionContext
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#setSessionContext(SessionContext)
	 */
	public void setSessionContext (SessionContext aContext) throws EJBException
	{
	}	//	setSessionContext

	/**
	 * Method ejbActivate
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException
	{
		if (log == null)
			log = CLogger.getCLogger(getClass());
		log.info ("ejbActivate " + getStatus());
	}	//	ejbActivate

	/**
	 * Method ejbPassivate
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException
	{
		log.info ("ejbPassivate " + getStatus());
	}	//	ejbPassivate

	/**
	 * Method ejbRemove
	 * @throws EJBException
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException
	{
		log.info ("ejbRemove " + getStatus());
	}	//	ejbRemove


	/**************************************************************************
	 * 	Dump SerialVersionUID of class 
	 *	@param clazz class
	 */
	protected static void dumpSVUID (Class clazz)		//	XDoclet: no generics
	{
		String s = clazz.getName() 
			+ " ==\nstatic final long serialVersionUID = "
			+ java.io.ObjectStreamClass.lookup(clazz).getSerialVersionUID()
			+ "L;\n";
		System.out.println (s);
	}	//	dumpSVUID

	/**
	 * 	Print UID of used classes.
	 * 	R2.5.1h		

org.compiere.process.ProcessInfo ==
static final long serialVersionUID = -1993220053515488725L;

org.compiere.util.CStatementVO ==
static final long serialVersionUID = -3393389471515956399L;

org.compiere.model.MQuery ==
static final long serialVersionUID = 1511402030597166113L;

org.compiere.model.POInfo ==
static final long serialVersionUID = -5976719579744948419L;

org.compiere.model.POInfoColumn ==
static final long serialVersionUID = -3983585608504631958L;

org.compiere.model.MWindowVO ==
static final long serialVersionUID = 3802628212531678981L;

org.compiere.model.MTabVO ==
static final long serialVersionUID = 9160212869277319305L;

org.compiere.model.MFieldVO ==
static final long serialVersionUID = 4385061125114436797L;

org.compiere.model.MLookupInfo ==
static final long serialVersionUID = -7958664359250070233L;

	 *
	 *	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		dumpSVUID(ProcessInfo.class);
		dumpSVUID(CStatementVO.class);
		dumpSVUID(Query.class);
		dumpSVUID(POInfo.class);
		dumpSVUID(POInfoColumn.class);
		dumpSVUID(GridWindowVO.class);
		dumpSVUID(GridTabVO.class);
		dumpSVUID(GridFieldVO.class);
		dumpSVUID(MLookupInfo.class);
	}	//	main

}	//	ServerBean
