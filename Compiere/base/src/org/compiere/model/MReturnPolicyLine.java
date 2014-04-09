package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;

public class MReturnPolicyLine extends X_M_ReturnPolicyLine {
    /** Logger for class MReturnPolicyLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReturnPolicyLine.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MReturnPolicyLine(Ctx ctx, int M_ReturnPolicyLine_ID, Trx trx) {
		super(ctx, M_ReturnPolicyLine_ID, trx);
		// TODO Auto-generated constructor stub
	}

	public MReturnPolicyLine(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

}
