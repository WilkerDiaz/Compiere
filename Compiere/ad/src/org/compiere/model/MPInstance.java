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

import org.compiere.util.*;

/**
 *  Process Instance Model
 *
 *  @author Jorg Janke
 *  @version $Id: MPInstance.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MPInstance extends X_AD_PInstance
{
    /** Logger for class MPInstance */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPInstance.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_PInstance_ID instance or 0
	 *	@param ignored no transaction support
	 */
	public MPInstance (Ctx ctx, int AD_PInstance_ID, Trx trx)
	{
		super (ctx, AD_PInstance_ID, null);
		//	New Process
		if (AD_PInstance_ID == 0)
		{
			int AD_Role_ID = ctx.getAD_Role_ID();
			if (AD_Role_ID != 0)
				setAD_Role_ID(AD_Role_ID);
			setIsProcessing (false);
		}
	}	//	MPInstance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param ignored no transaction support
	 */
	public MPInstance (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, null);
	}	//	MPInstance

	/**
	 * 	Create Process Instance from Process and create parameters
	 *	@param process process
	 *	@param Record_ID Record
	 */
	public MPInstance (MProcess process, int Record_ID)
	{
		this (process.getCtx(), 0, null);
		setAD_Process_ID (process.getAD_Process_ID());
		setRecord_ID (Record_ID);
		setAD_User_ID(process.getCtx().getAD_User_ID());
		if (!save())		//	need to save for parameters
			throw new IllegalArgumentException ("Cannot Save");
		//	Set Parameter Base Info
		MProcessPara[] para = process.getParameters();
		for (MProcessPara element : para) {
			MPInstancePara pip = new MPInstancePara (this, element.getSeqNo());
			pip.setParameterName(element.getColumnName());
			pip.setInfo(element.getName());
			pip.save();
		}
	}	//	MPInstance

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param AD_Process_ID Process ID
	 *	@param Record_ID record
	 */
	public MPInstance (Ctx ctx, int AD_Process_ID, int Record_ID)
	{
		this(ctx, 0, null);
		setAD_Process_ID (AD_Process_ID);
		setRecord_ID (Record_ID);
		setAD_User_ID(ctx.getAD_User_ID());
		setIsProcessing (false);
	}	//	MPInstance


	/**	Parameters						*/
	private MPInstancePara[]		m_parameters = null;

	/**
	 * 	Get Parameters
	 *	@return parameter array
	 */
	public MPInstancePara[] getParameters()
	{
		if (m_parameters != null)
			return m_parameters;
		ArrayList<MPInstancePara> list = new ArrayList<MPInstancePara>();
		//
		String sql = "SELECT * FROM AD_PInstance_Para WHERE AD_PInstance_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				list.add(new MPInstancePara(getCtx(), rs, null));
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		m_parameters = new MPInstancePara[list.size()];
		list.toArray(m_parameters);
		return m_parameters;
	}	//	getParameters


	/**	Log Entries					*/
	private ArrayList<MPInstanceLog>	m_log	= new ArrayList<MPInstanceLog>();

	/**
	 *	Get Logs
	 *	@return array of logs
	 */
	public MPInstanceLog[] getLog()
	{
		//	load it from DB
		m_log.clear();
		String sql = "SELECT * FROM AD_PInstance_Log WHERE AD_PInstance_ID=? ORDER BY Log_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				m_log.add(new MPInstanceLog(getCtx(), rs, null));
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MPInstanceLog[] retValue = new MPInstanceLog[m_log.size()];
		m_log.toArray(retValue);
		return retValue;
	}	//	getLog

	/**
	 *	@param P_Date date
	 *	@param P_ID id
	 *	@param P_Number number
	 *	@param P_Msg msg
	 */
	public void addLog (Timestamp P_Date, int P_ID, BigDecimal P_Number, String P_Msg, Trx trx)
	{
		MPInstanceLog logEntry = new MPInstanceLog (getCtx(), getAD_PInstance_ID(), m_log.size()+1,
			P_Date, P_ID, P_Number, P_Msg, trx);
		m_log.add(logEntry);
		//	save it to DB ?
	//	log.save();
	}	//	addLog


	/**
	 * 	Set AD_Process_ID.
	 * 	Check Role if process can be performed
	 *	@param AD_Process_ID process
	 */
	@Override
	public void setAD_Process_ID (int AD_Process_ID)
	{
		int AD_Role_ID = getCtx().getAD_Role_ID();
		if (AD_Role_ID != 0)
		{
			MRole role = MRole.get(getCtx(), AD_Role_ID);
			Boolean access = role.getProcessAccess(AD_Process_ID);
			if (access == null || !access.booleanValue())
				throw new IllegalAccessError("Cannot access Process " + AD_Process_ID
					+ " with Role: " + role.getName());
		}
		super.setAD_Process_ID (AD_Process_ID);
	}	//	setAD_Process_ID

	/**
	 * 	Set Record ID.
	 * 	direct internal record ID
	 * 	@param Record_ID record
	 **/
	@Override
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0)
		{
			log.info("Set to 0 from " + Record_ID);
			Record_ID = 0;
		}
		set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
	}	//	setRecord_ID

	/**
	 * 	String Representation
	 *	@see java.lang.Object#toString()
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPInstance[")
			.append (get_ID())
			.append(",OK=").append(isOK());
		String msg = getErrorMsg();
		if (msg != null && msg.length() > 0)
			sb.append(",").append(msg);
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Dump Log
	 */
	public void log()
	{
		log.info(toString());
		MPInstanceLog[] pil = getLog();
		for (int i = 0; i < pil.length; i++)
			log.info(i + "=" + pil[i]);
	}	//	log

	/** Result OK = 1			*/
	public static final int		RESULT_OK = 1;
	/** Result FALSE = 0		*/
	public static final int		RESULT_ERROR = 0;

	/**
	 * 	Is it OK
	 *	@return Result == OK
	 */
	public boolean isOK()
	{
		return getResult() == RESULT_OK;
	}	//	isOK

	/**
	 * 	Set Result
	 *	@param ok
	 */
	public void setResult (boolean ok)
	{
		super.setResult (ok ? RESULT_OK : RESULT_ERROR);
	}	//	setResult

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (newRecord && getAD_Session_ID() == 0)
		{
			MSession session = MSession.get(getCtx());
			if (session == null)
			{
				log.saveError("Error", "Require Session");
				return false;
			}
			int AD_Session_ID = session.getAD_Session_ID();
			setAD_Session_ID(AD_Session_ID);
		}
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Update Statistics
		if (!newRecord
			&& !isProcessing()
			&& is_ValueChanged("IsProcessing"))
		{
			long ms = System.currentTimeMillis() - getCreated().getTime();
			int seconds = (int)(ms / 1000);
			if (seconds < 1)
				seconds = 1;
			MProcess prc = MProcess.get(getCtx(), getAD_Process_ID());
			prc.addStatistics(seconds);
			if (prc.get_ID() != 0 && prc.save())
				log.fine("afterSave - Process Statistics updated Sec=" + seconds);
			else
				log.warning("afterSave - Process Statistics not updated");
		}
		return success;
	}	//	afterSave

}	//	MPInstance
