package org.compiere.model;

import java.sql.*;
import java.util.*;

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * 	Recent Item Model
 *	@author Jorg Janke
 */
public class MRecentItem extends X_AD_RecentItem
{
    /** Logger for class MRecentItem */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRecentItem.class);
	/** */
    private static final long serialVersionUID = -3964163357301080033L;

    
    /**
     * 	Add PO to Recent Item
     *	@param po PO
     *	@param type I=Insert, U=Update, V=View
     *	@return true if added
     */
    public static boolean add(PO po, String type)
    {
    	int AD_Table_ID = po.get_Table_ID();
    	int Record_ID = po.get_ID();
    	if (AD_Table_ID == 0 || Record_ID == 0)
    		return false;
    //	MRecentItem ri = new MRecentItem(po, type);
    //	return ri.save();
    	return false;
    }	//	add
    
    
    /**
     * 	Get Recent Items
     *	@param ctx context with User and Role
     *	@return list of recent items
     */
    public static ArrayList<MRecentItem> getRecent(Ctx ctx)
    {
    //	int AD_User_ID = ctx.getAD_User_ID();
    //	int AD_Role_ID = ctx.getAD_Role_ID();
    	
    	
    	return null;
    }	//	getRecent
    
    
    
    /**
     * 	Standard Constructor
     * 	@param ctx ctx
     *	@param AD_RecentItem_ID id
     *	@param trx p_trx
     */
	public MRecentItem(Ctx ctx, int AD_RecentItem_ID, Trx trx)
    {
	    super(ctx, AD_RecentItem_ID, trx);
    }	//	MRecentItem
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MRecentItem(Ctx ctx, ResultSet rs, Trx trx)
    {
	    super(ctx, rs, trx);
    }	//	MRecentItem
	
	/**
	 * 	PO Constructor
	 *	@param po persistent object 
     *	@param type I=Insert, U=Update, V=View
	 */
	public MRecentItem(PO po, String type)
    {
	    this(po.getCtx(), 0, po.get_Trx());
    	int AD_User_ID = getCtx().getAD_User_ID();
    	int AD_Role_ID = getCtx().getAD_Role_ID();
    	setAD_User_ID(AD_User_ID);
    	setAD_Role_ID(AD_Role_ID);
    //	setAD_Table_ID(po.get_Table_ID());
    	setRecord_ID(po.get_ID());
    //	setRecentType(type);
    }	//	MRecentItem
	
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MRecentItem[").append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString

	/**
     * 	Extended String Representation
     *	@return info
     */
    @Override
	public String toStringX()
    {
	    StringBuffer sb = new StringBuffer();
	    return sb.toString();
    }	//	toStringX
    
}	//	MRecentItem
