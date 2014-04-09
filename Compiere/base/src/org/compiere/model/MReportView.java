package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;

/**
 * 	Report View Model
 *	@author Jorg Janke
 */
public class MReportView extends X_AD_ReportView
{
    /** Logger for class MReportView */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReportView.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param AD_ReportView_ID id
	 *	@param trx p_trx
	 */
	public MReportView(Ctx ctx, int AD_ReportView_ID, Trx trx)
	{
		super(ctx, AD_ReportView_ID, trx);
	}	//	MReportView

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MReportView(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MReportView
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MReportView[")
	    	.append(get_ID()).append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
	
}	//	MReportView
