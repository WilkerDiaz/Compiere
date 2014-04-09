/**
 * 
 */
package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;


/**
 * @author Jorg Janke
 */
public class MBPStatus extends X_C_BP_Status
{
    /** Logger for class MBPStatus */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPStatus.class);

	/** */
    private static final long serialVersionUID = 45174439940266885L;

	/**
	 * @param ctx
	 * @param C_BP_Status_ID
	 * @param trx
	 */
	public MBPStatus(Ctx ctx, int C_BP_Status_ID, Trx trx)
	{
		super(ctx, C_BP_Status_ID, trx);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MBPStatus(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MBPStatus[").append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
}
