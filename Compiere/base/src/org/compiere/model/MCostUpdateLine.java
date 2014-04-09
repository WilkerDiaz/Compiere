package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;

public class MCostUpdateLine extends X_M_CostUpdateLine {
    /** Logger for class MCostUpdateLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCostUpdateLine.class);
	
	private static final long serialVersionUID = 1L;
	
	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 *  @param trx p_trx name
	 */
	public MCostUpdateLine(Ctx ctx, int M_CostUpdateline_ID, Trx trx)
	{
		super (ctx, M_CostUpdateline_ID, trx);
	}
	
	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trx transaction
	 */
	public MCostUpdateLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	public MCostUpdateLine(Ctx ctx, MCostUpdate costupdate, Trx trx)
	{
		super (ctx, 0, trx);
		setM_CostUpdate_ID(costupdate.getM_CostUpdate_ID());
	}

}
