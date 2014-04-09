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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * 	Project Phase Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectPhase.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MProjectPhase extends X_C_ProjectPhase
{
    /** Logger for class MProjectPhase */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProjectPhase.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ProjectPhase_ID id
	 *	@param trx transaction
	 */
	public MProjectPhase (Ctx ctx, int C_ProjectPhase_ID, Trx trx)
	{
		super (ctx, C_ProjectPhase_ID, trx);
		if (C_ProjectPhase_ID == 0)
		{
		//	setC_ProjectPhase_ID (0);	//	PK
		//	setC_Project_ID (0);		//	Parent
		//	setC_Phase_ID (0);			//	FK
			setCommittedAmt (Env.ZERO);
			setIsCommitCeiling (false);
			setIsComplete (false);
			setSeqNo (0);
		//	setName (null);
			setPlannedAmt(Env.ZERO);
			setPlannedQty(Env.ZERO);
			setQty (Env.ZERO);
		}
	}	//	MProjectPhase

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProjectPhase (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProjectPhase

	/**
	 * 	Parent Constructor
	 *	@param project parent
	 */
	public MProjectPhase (MProject project)
	{
		this (project.getCtx(), 0, project.get_Trx());
		setClientOrg(project);
		setC_Project_ID(project.getC_Project_ID());
	}	//	MProjectPhase

	/**
	 * 	Copy Constructor
	 *	@param project parent
	 *	@param phase copy
	 */
	public MProjectPhase (MProject project, MProjectTypePhase phase)
	{
		this (project);
		//
		setC_Phase_ID (phase.getC_Phase_ID());			//	FK
		setName (phase.getName());
		setSeqNo (phase.getSeqNo());
		setDescription(phase.getDescription());
		setHelp(phase.getHelp());
		if (phase.getM_Product_ID() != 0)
			setM_Product_ID(phase.getM_Product_ID());
		setQty(phase.getStandardQty());
	}	//	MProjectPhase

	/**
	 * 	Get Project Phase Tasks.
	 *	@return Array of tasks
	 */
	public MProjectTask[] getTasks()
	{
		ArrayList<MProjectTask> list = new ArrayList<MProjectTask>();
		String sql = "SELECT * FROM C_ProjectTask WHERE C_ProjectPhase_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_ProjectPhase_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProjectTask (getCtx(), rs, get_Trx()));
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MProjectTask[] retValue = new MProjectTask[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getTasks


	/**
	 * 	Copy Tasks from other Phase
	 *	@param fromProject from project
	 *	@param fromPhase from phase
	 *	@return number of tasks copied
	 */
	public int copyTasksFrom (MProject fromProject, MProjectPhase fromPhase)
	{
		if (fromPhase == null)
			return 0;
		int count = 0;
		int taskCount = 0;
		//
		MProjectTask[] myTasks = getTasks();
		MProjectTask[] fromTasks = fromPhase.getTasks();
		//	Copy Project Tasks
		for (MProjectTask element : fromTasks) {
			//	Check if Task already exists
			int C_Task_ID = element.getC_Task_ID();
			boolean exists = false;
			if (C_Task_ID == 0)
				exists = false;
			else
			{
				for (MProjectTask element2 : myTasks) {
					if (element2.getC_Task_ID() == C_Task_ID)
					{
						exists = true;
						break;
					}
				}
			}
			//	Phase exist
			if (exists)
				log.info("Task already exists here, ignored - " + element);
			else
			{
				MProjectTask toTask = new MProjectTask (getCtx (), 0, get_Trx());
				PO.copyValues (element, toTask, getAD_Client_ID (), getAD_Org_ID ());
				toTask.setC_ProjectPhase_ID (getC_ProjectPhase_ID ());
				if (toTask.save ())
				{
					count++;
					taskCount++;
					int lineCount = fromProject.copyLinesFrom(fromProject, 0, 0, element.getC_ProjectTask_ID(), toTask.getC_ProjectTask_ID());
					count += lineCount;
				}
			}
		}
		if (fromTasks.length != taskCount)
			log.warning("Count difference - ProjectPhase=" + fromTasks.length + " <> Saved=" + taskCount);

		return count;
	}	//	copyTasksFrom

	/**
	 * 	Copy Tasks from other Phase
	 *	@param fromPhase from phase
	 *	@return number of tasks copied
	 */
	public int copyTasksFrom (MProjectTypePhase fromPhase)
	{
		if (fromPhase == null)
			return 0;
		int count = 0;
		//	Copy Type Tasks
		MProjectTypeTask[] fromTasks = fromPhase.getTasks();
		for (MProjectTypeTask element : fromTasks) {
			MProjectTask toTask = new MProjectTask (this, element);
			if (toTask.save())
				count++;
		}
		log.fine("#" + count + " - " + fromPhase);
		if (fromTasks.length != count)
			log.log(Level.SEVERE, "Count difference - TypePhase=" + fromTasks.length + " <> Saved=" + count);

		return count;
	}	//	copyTasksFrom

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MProjectPhase[");
		sb.append (get_ID())
			.append ("-").append (getSeqNo())
			.append ("-").append (getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MProjectPhase
