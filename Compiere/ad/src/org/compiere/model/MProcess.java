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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Process Model
 *
 *  @author Jorg Janke
 *  @version $Id: MProcess.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MProcess extends X_AD_Process
{
    /** Logger for class MProcess */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProcess.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MProcess from Cache
	 *	@param ctx context
	 *	@param AD_Process_ID id
	 *	@return MProcess
	 */
	public static MProcess get (Ctx ctx, int AD_Process_ID)
	{
		Integer key = Integer.valueOf (AD_Process_ID);
		MProcess retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MProcess (ctx, AD_Process_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get MProcess by Value
	 *	@param ctx context
	 *	@param value value
	 *	@return MProcess or null
	 */
	public static MProcess getByValue (Ctx ctx, String value)
	{
		MProcess retValue = null;
		String sql = "SELECT * FROM AD_Process p "
			+ "WHERE p.Value LIKE ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, value);
			rs = pstmt.executeQuery ();
			if (rs.next())
			{
				retValue = new MProcess (ctx, rs, null);
				//	Save in cache
				Integer key = Integer.valueOf (retValue.getAD_Process_ID());
				s_cache.put (key, retValue);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	getByValue

	/**
	 * 	Get MProcess from Menu
	 *	@param ctx context
	 *	@param AD_Menu_ID id
	 *	@return MProcess or null
	 */
	public static MProcess getFromMenu (Ctx ctx, int AD_Menu_ID)
	{
		MProcess retValue = null;
		String sql = "SELECT * FROM AD_Process p "
			+ "WHERE EXISTS (SELECT * FROM AD_Menu m "
				+ "WHERE m.AD_Process_ID=p.AD_Process_ID AND m.AD_Menu_ID=?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Menu_ID);
			rs = pstmt.executeQuery ();
			if (rs.next())
			{
				retValue = new MProcess (ctx, rs, null);
				//	Save in cache
				Integer key = Integer.valueOf (retValue.getAD_Process_ID());
				s_cache.put (key, retValue);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	getFromMenu


	/**
	 * 	Get AD_Process_ID by Value
	 *	@param ctx context
	 *	@param value value
	 *	@return MProcess or null
	 */
	public static int getIDByValue (Ctx ctx, String value)
	{
		int retValue = -1;
		String sql = "SELECT AD_Process_ID FROM AD_Process p "
			+ "WHERE p.Value LIKE ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, value);
			rs = pstmt.executeQuery ();
			if (rs.next())
			{
				retValue = rs.getInt(1);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	getByValue
	/**	Cache						*/
	private static final CCache<Integer,MProcess>	s_cache	= new CCache<Integer,MProcess>("AD_Process", 20);
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MProcess.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Process_ID process
	 *	@param ignored no transaction
	 */
	public MProcess (Ctx ctx, int AD_Process_ID, Trx trx)
	{
		super (ctx, AD_Process_ID, null);
		if (AD_Process_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsReport (false);
			setIsServerProcess(false);
			setAccessLevel (ACCESSLEVEL_All);
			setEntityType (ENTITYTYPE_UserMaintained);
			setIsBetaFunctionality(false);
		}
	}	//	MProcess

	/**
	 * 	Load Contsructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param ignored no transaction
	 */
	public MProcess (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, null);
	}	//	MProcess


	/**	Parameters					*/
	private MProcessPara[]		m_parameters = null;

	/**
	 * 	Get Parameters
	 *	@return parameters
	 */
	public MProcessPara[] getParameters()
	{
		if (m_parameters != null)
			return m_parameters;
		ArrayList<MProcessPara> list = new ArrayList<MProcessPara>();
		//
		String sql = "SELECT * FROM AD_Process_Para WHERE AD_Process_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, getAD_Process_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProcessPara(getCtx(), rs, null));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_parameters = new MProcessPara[list.size()];
		list.toArray(m_parameters);
		return m_parameters;
	}	//	getParameters

	/**
	 * 	Get Parameter with ColumnName
	 *	@param name column name
	 *	@return parameter or null
	 */
	public MProcessPara getParameter(String name)
	{
		getParameters();
		for (MProcessPara element : m_parameters) {
			if (element.getColumnName().equals(name))
				return element;
		}
		return null;
	}	//	getParameter



	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MProcess[")
			.append (get_ID())
			.append("-").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString


	/**************************************************************************
	 * 	Process SQL Procedures w/o parameter
	 *	@param Record_ID record
	 *	@return Process Instance
	 */
	public MPInstance processIt (int Record_ID)
	{
		MPInstance pInstance = new MPInstance (this, Record_ID);
		//	Lock
		pInstance.setIsProcessing(true);
		pInstance.save();

		boolean ok = true;

		//	PL/SQL Procedure
		String ProcedureName = getProcedureName();
	//	String Classname = getClassname();
		if ((ProcedureName != null) && (ProcedureName.length() > 0))
			ok = startProcess (ProcedureName, pInstance);
	//	else if (Classname != null && Classname.length() > 0)
	//		ok = startClass(Classname, pi, p_trx);


		//	Unlock
		pInstance.setResult(ok ? MPInstance.RESULT_OK : MPInstance.RESULT_ERROR);
		pInstance.setIsProcessing(false);
		pInstance.save();
		//
		pInstance.log();
		return pInstance;
	}	//	process

	/**
	 * 	Process It (sync)
	 *	@param pi Process Info
	 *	@param p_trx transaction
	 *	@return true if OK
	 */
	public boolean processIt (ProcessInfo pi, Trx p_trx)
	{
		if (pi.getAD_PInstance_ID() == 0)
		{
			MPInstance pInstance = new MPInstance (this, pi.getRecord_ID());
			//	Lock
			pInstance.setIsProcessing(true);
			pInstance.save();
		}

		boolean ok = false;

		//	Java Class
		String Classname = getClassname();
		if (Util.isEmpty(Classname))
		{
			if (isReport())
				ok = true;		//	optional
			else
			{
				String msg = "No Classname for " + getName();
				pi.setSummary(msg, ok);
				log.warning(msg);
			}
		}
		else
			ok = startClass(Classname, pi, p_trx);
		return ok;
	}	//	process

	/**
	 * 	Is this a Java Process
	 *	@return true if java process
	 */
	public boolean isJavaProcess()
	{
		String Classname = getClassname();
		return ((Classname != null) && (Classname.length() > 0));
	}	//	is JavaProcess

	/**
	 *  Start Database Process
	 *  @param ProcedureName PL/SQL procedure name
	 *  @param pInstance process instance
	 *	see ProcessCtl.startProcess
	 *  @return true if success
	 */
	private boolean startProcess (String ProcedureName, MPInstance pInstance)
	{
		int AD_PInstance_ID = pInstance.getAD_PInstance_ID();
		//  execute on this thread/connection
		log.info(ProcedureName + "(" + AD_PInstance_ID + ")");
		String sql = "{call " + ProcedureName + "(?)}";
		try
		{
			DB.executeCall(sql, AD_PInstance_ID);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			pInstance.setResult(MPInstance.RESULT_ERROR);
			pInstance.setErrorMsg(e.getLocalizedMessage());
			return false;
		}
		pInstance.setResult(MPInstance.RESULT_OK);
		return true;
	}   //  startProcess


	/**
	 *  Start Java Class (sync).
	 *      instanciate the class implementing the interface ProcessCall.
	 *  The class can be a Server/Client class (when in Package
	 *  org compiere.process or org.compiere.model) or a client only class
	 *  (e.g. in org.compiere.report)
	 *
	 *  @param Classname    name of the class to call
	 *  @param pi	process info
	 *  @param p_trx transaction
	 *  @return     true if success
	 *	see ProcessCtl.startClass
	 */
	private boolean startClass (String Classname, ProcessInfo pi, Trx p_trx)
	{
		log.info(Classname  + "(" + pi + ")");
		boolean retValue = false;
		ProcessCall myObject = null;
		try
		{
			Class<?> myClass = Class.forName(Classname);
			myObject = (ProcessCall)myClass.newInstance();
			if (myObject == null)
				retValue = false;
			else
				retValue = myObject.startProcess(getCtx(), pi, p_trx);
		}
		catch (Exception e)
		{
			pi.setSummary("Error Start Class " + Classname, true);
			log.log(Level.SEVERE, Classname, e);
			throw new RuntimeException(e);
		}
		return retValue;
	}   //  startClass


	/**
	 * 	Is it a Workflow
	 *	@return true if Workflow
	 */
	public boolean isWorkflow()
	{
		return getAD_Workflow_ID() > 0;
	}	//	isWorkflow


	/**
	 * 	Update Statistics
	 *	@param seconds sec
	 */
	public void addStatistics (int seconds)
	{
		setStatistic_Count(getStatistic_Count() + 1);
		setStatistic_Seconds(getStatistic_Seconds().add(new BigDecimal(seconds)));
	}	//	addStatistics


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)	//	Add to all automatic roles
		{
			MRole[] roles = MRole.getOf(getCtx(), "IsManual='N'");
			for (MRole element : roles) {

				MProcessAccess pa = new MProcessAccess(this, element.getAD_Role_ID());
				
				// SR 10019752: Set the tenant from the role for access record
				pa.setAD_Client_ID(element.getAD_Client_ID());
				
				// SR 10019752: Allow read-write access
				pa.setIsReadWrite(true);
				
				pa.save();
			}
		}
		//	Menu/Workflow
		else if (is_ValueChanged("IsActive") || is_ValueChanged("Name")
			|| is_ValueChanged("Description") || is_ValueChanged("Help"))
		{
			MMenu[] menues = MMenu.get(getCtx(), "AD_Process_ID=" + getAD_Process_ID());
			for (MMenu element : menues) {
				element.setIsActive(isActive());
				element.setName(getName());
				element.setDescription(getDescription());
				element.save();
			}
			X_AD_WF_Node[] nodes = MWindow.getWFNodes(getCtx(), "AD_Process_ID=" + getAD_Process_ID());
			for (X_AD_WF_Node element : nodes) {
				boolean changed = false;
				if (element.isActive() != isActive())
				{
					element.setIsActive(isActive());
					changed = true;
				}
				if (element.isCentrallyMaintained())
				{
					element.setName(getName());
					element.setDescription(getDescription());
					element.setHelp(getHelp());
					changed = true;
				}
				if (changed)
					element.save();
			}
		}
		return success;
	}	//	afterSave

}	//	MProcess
