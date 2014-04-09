package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.vos.*;

public class MCostUpdate extends X_M_CostUpdate  {
    /** Logger for class MCostUpdate */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCostUpdate.class);
	
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 *  @param trx p_trx name
	 */
	public MCostUpdate(Ctx ctx, int M_CostUpdate_ID, Trx trx)
	{
		super (ctx, M_CostUpdate_ID, trx);
	}
	
	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trx transaction
	 */
	public MCostUpdate (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);	
		setProcessed(true);
		setDocStatus(DocActionConstants.STATUS_Completed);
		setDocAction(DocActionConstants.ACTION_Close);
		return true;
	}	//	beforeSave
	
	
	public MCostUpdateLine[] getLines ()
	{
		ArrayList<MCostUpdateLine> list = new ArrayList<MCostUpdateLine> ();
		StringBuffer sql = new StringBuffer("SELECT * FROM M_CostUpdateline WHERE M_CostUpdate_ID=? ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, this.getM_CostUpdate_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MCostUpdateLine ol = new MCostUpdateLine(getCtx(), rs, get_Trx());
				list.add(ol);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		MCostUpdateLine[] lines = new MCostUpdateLine[list.size ()];
		list.toArray (lines);
		return lines;
	}	//	getLines
	
	}
