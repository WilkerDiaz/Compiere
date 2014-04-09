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
 *	Assign Set Model
 *	(Table Level)
 *	
 *  @author Jorg Janke
 *  @version $Id: MAssignSet.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MAssignSet extends X_AD_AssignSet
{
    /** Logger for class MAssignSet */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAssignSet.class);
	/** */
    private static final long serialVersionUID = -2804942671275840664L;

	/**
	 * 	Get all Assignments 
	 *	@param ctx ctx
	 *	@return Assignment array
	 */
	static public MAssignSet[] getAll(Ctx ctx)
	{
		ArrayList<MAssignSet> list = new ArrayList<MAssignSet>();
		String sql = "SELECT * FROM AD_AssignSet WHERE IsActive='Y'";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        rs = pstmt.executeQuery();
	        while (rs.next())
		        list.add(new MAssignSet(ctx, rs, null));
        }
        catch (Exception e) {
	        s_log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MAssignSet[] retValue = new MAssignSet[list.size()];
        list.toArray(retValue);
        return retValue;
	}	//	getAll
	
	/**
     * 	Execute Auto Assignment
     *	@param po PO to be modified
     *	@param newRecord new
     *	@return true if modified
	 */
	static public boolean execute (PO po, boolean newRecord)
	{
		if (s_assignments == null)
			s_assignments = getAll(po.getCtx());
		boolean modified = false;
		for (MAssignSet set : s_assignments) 
		{
	        if (!set.isActive())
	        	continue;
	        //	Check IDs
	        if (po.get_Table_ID() == set.getAD_Table_ID()
	        	&& (po.getAD_Client_ID() == set.getAD_Client_ID()
	        		|| set.getAD_Client_ID() == 0))
	        {
		        //	Check Timing
	        	String rule = set.getAutoAssignRule();
	        	if (!newRecord && rule.equals(AUTOASSIGNRULE_CreateOnly))
	        		continue;
	        	if (newRecord 
	        		&& (rule.equals(AUTOASSIGNRULE_UpdateOnly)
	        			|| rule.equals(AUTOASSIGNRULE_UpdateIfNotProcessed)))
	        		continue;
	        	//	Eliminate Processed
	        	if (rule.equals(AUTOASSIGNRULE_CreateAndUpdateIfNotProcessed)
	        		|| rule.equals(AUTOASSIGNRULE_UpdateIfNotProcessed))
	        	{
	        		int indexProcessed = po.get_ColumnIndex("Processed");
	        		if (indexProcessed != -1
	        			&& "Y".equals(po.get_Value(indexProcessed)))
	        			continue;
	        	}
	        	//
	        	if (set.executeIt(po))
	        		modified = true;
	        }
        }
		return modified;
	}	//	execute
	
	
	/**	Logger			*/
    private static CLogger s_log = CLogger.getCLogger(MAssignSet.class);
	/**	Assignments		*/
	private static MAssignSet[]	s_assignments = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AssignSet_ID id
	 *	@param trx
	 */
	public MAssignSet(Ctx ctx, int AD_AssignSet_ID, Trx trx)
	{
		super(ctx, AD_AssignSet_ID, trx);
	}	//	MAssignSet

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MAssignSet(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAssignSet
	
	/**	The Target Lines				*/
    private MAssignTarget[] m_targets = null;

    /**
     * 	Get all Target Lines
     *	@param reload reload data
     *	@return array of lines
     */
    public MAssignTarget[] getTargets(boolean reload)
    {
	    if (m_targets != null && !reload)
		    return m_targets;
	    String sql = "SELECT * FROM AD_AssignTarget "
	    	+ "WHERE AD_AssignSet_ID=? ORDER BY SeqNo";
	    ArrayList<MAssignTarget> list = new ArrayList<MAssignTarget>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, get_Trx());
	        pstmt.setInt(1, getAD_AssignSet_ID());
	        rs = pstmt.executeQuery();
	        while (rs.next())
		        list.add(new MAssignTarget(getCtx(), rs, get_Trx()));
        }
        catch (Exception e) {
	        log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_targets = new MAssignTarget[list.size()];
	    list.toArray(m_targets);
	    return m_targets;
    }	//	getTargets

    /**
     * 	Execute Auto Assignment
     *	@param po PO to be modified
     *	@return true if modified
     */
	public boolean executeIt (PO po)
	{
		if (m_targets == null)
			m_targets = getTargets(false);
		boolean modified = false;
		for (MAssignTarget target : m_targets) 
		{
	        if (!target.isActive())
	        	continue;
	        //	Check consistency
	        MColumn tColumn = target.getTargetColumn();
	        if (tColumn.getAD_Table_ID() != getAD_Table_ID())
	        	throw new IllegalArgumentException(toString() 
	        		+ ": AD_Table_ID inconsistent for " + target);
	        //
	        try
	        {
	        	modified = target.executeIt(po);
	        }
	        catch (Exception e)
	        {
	        	log.severe(e.toString());
	        	modified = false;
	        }
        }
		return modified;
	}	//	execute
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MAssignSet[")
	    	.append(get_ID()).append("-").append(getName())
	    	.append(",AD_Table_ID=").append(getAD_Table_ID())
	    	.append("]");
	    return sb.toString();
    }	//	toString
    
}	//	MAssignSet
