package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MCycleCountLock extends X_M_CycleCountLock{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static CLogger	s_log	= CLogger.getCLogger (MCycleCountLock.class);
	private static final CCache<CycleCountLockKey,MCycleCountLock>	s_cache	= new CCache<CycleCountLockKey,MCycleCountLock>("M_CycleCountLock", 1000);

	/**
	 * @param ctx
	 * @param MCycleCountLock_ID
	 * @param trx
	 */
	public MCycleCountLock(Ctx ctx, int MCycleCountLock_ID, Trx trx) {
		super(ctx, MCycleCountLock_ID, trx);

	}
	
	public static MCycleCountLock getLock(Ctx ctx, MInventory inv,int M_Product_ID, int M_Locator_ID, Trx trx)
	{
		MCycleCountLock lock = null;
		String sql = " SELECT M_CycleCountLock_ID FROM M_CycleCountLock "
			       + " WHERE M_Inventory_ID = ? "
			       + " AND M_Product_ID = ? "
			       + " AND M_Locator_ID = ? "
			       + " AND IsLocked = 'Y' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,trx);
			pstmt.setInt(1, inv.getM_Inventory_ID());
			pstmt.setInt(2, M_Product_ID);
			pstmt.setInt(3, M_Locator_ID);
			rs = pstmt.executeQuery();
			if(rs.next())
				lock = new MCycleCountLock(ctx, rs.getInt(1),trx);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return lock;
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MCycleCountLock(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	public static boolean lock(MInventory inv)
	{
		boolean retVal = true;
		ArrayList<MCycleCountLock> list = new ArrayList<MCycleCountLock>();
		if(inv.getIsLocked() != null && inv.getIsLocked().equals("Y"))
		{
			s_log.log(Level.INFO, "Already Locked");
			return retVal;
		}
			
		MInventoryLine[] lines = inv.getLines(true);
		if(lines == null || lines.length==0)
		{
			s_log.log(Level.INFO,"Lines not found in the document");
			return retVal;
		}
		for (MInventoryLine line : lines)
		{
			if(line.getM_ABCAnalysisGroup_ID()!=0)
			{
				if (!MCycleCountLock.lockExists(inv.getCtx(),line.getM_Product_ID(),line.getM_Locator_ID(),inv.get_Trx()))
				{
					MCycleCountLock lock = new MCycleCountLock(inv.getCtx(),0,inv.get_Trx());
					lock.setM_Inventory_ID(inv.getM_Inventory_ID());
					lock.setM_Product_ID(line.getM_Product_ID());
					lock.setM_Locator_ID(line.getM_Locator_ID());
					lock.setM_Warehouse_ID(inv.getM_Warehouse_ID());
					lock.setIsLocked(true);
					lock.setIsActive(true);
					lock.setAD_Client_ID(line.getAD_Client_ID());
					lock.setAD_Org_ID(line.getAD_Org_ID());
					list.add(lock);
					// Cache the lock
					CycleCountLockKey key = new CycleCountLockKey(line.getM_Product_ID(),line.getM_Locator_ID());
					s_cache.put(key,lock);
				}
				else
					s_log.log(Level.INFO, "Already Locked");
					
			}
		}
		if(saveAll(inv.get_Trx(), list))
		{
			inv.setIsLocked("Y");
			inv.setUnLock("N");
			retVal = true;
		}
		else
		{
			s_log.log(Level.SEVERE,"Unable to Save the Changes");
			retVal = false;
			s_cache.reset(); //something went wrong. Reset the cache
		}
			
		return retVal;
		
	}
	
	public static boolean unLock(MInventory inv,MInventoryLine invLine)
	{
		boolean retVal = true;
		ArrayList<MCycleCountLock> list = new ArrayList<MCycleCountLock>();
		MInventoryLine[] lines = inv.getLines(true);
		for (MInventoryLine line : lines)
		{
			if(invLine!=null && invLine.getM_InventoryLine_ID()!=line.getM_InventoryLine_ID())
				continue;
				
			if(line.getM_ABCAnalysisGroup_ID()!=0)
			{
				MCycleCountLock lock = MCycleCountLock.getLock(inv.getCtx(),inv,line.getM_Product_ID(),line.getM_Locator_ID(),inv.get_Trx());
				if (lock != null)
				{
					lock.setIsLocked(false);
					list.add(lock);
					CycleCountLockKey key = new CycleCountLockKey(line.getM_Product_ID(),line.getM_Locator_ID());
					s_cache.remove(key);
				}
				else
					s_log.log(Level.INFO, "Lock Not found");
					
			}
		}
		if(saveAll(inv.get_Trx(),list))
		{
			inv.setIsLocked("N");
			inv.setUnLock("Y");
			retVal = true;
		}
		else
		{
			s_log.log(Level.SEVERE,"Unable to Save the Changes");
			retVal = false;
			s_cache.reset(); //something went wrong reset the cache
		}

		return retVal;
		
	}
	
	public static boolean lockExists(Ctx ctx, int M_Product_ID, int M_Locator_ID, Trx trx)
	{
		CycleCountLockKey key = new CycleCountLockKey(M_Product_ID,M_Locator_ID);
		MCycleCountLock lock = s_cache.get(ctx, key);
		if(lock!=null)
			return true;
		
		boolean retVal = false;
		String sql = " SELECT * FROM M_CycleCountLock WHERE M_Product_ID = ? AND M_Locator_ID = ? AND IsLocked = 'Y' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				// Cache the lock
				MCycleCountLock newLock = new MCycleCountLock(ctx,rs,trx);
				s_cache.put(key,newLock);
				retVal = true;
			}
			else
				retVal = false;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql.toString());
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retVal;
	}
	
	public static void delete(MInventory inv, MInventoryLine line)
	{
		MCycleCountLock lock = null;
		String sql = " SELECT M_CycleCountLock_ID FROM M_CycleCountLock "
			       + " WHERE M_Inventory_ID = ? "
			       + " AND M_Product_ID = ? "
			       + " AND M_Locator_ID = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,inv.get_Trx());
			pstmt.setInt(1, inv.getM_Inventory_ID());
			pstmt.setInt(2, line.getM_Product_ID());
			pstmt.setInt(3, line.getM_Locator_ID());
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				lock = new MCycleCountLock(inv.getCtx(), rs.getInt(1),inv.get_Trx());
				if(lock!=null)
					lock.delete(true);
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
}
