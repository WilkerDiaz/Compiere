package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class Storage 
{
	
	/** Logger */
	private static CLogger s_log = CLogger.getCLogger(Storage.class);
	
	
	//////////////////////// Value Object Class
	public static class VO 
	{
		private int productID;
		private int attributeSetInstanceID;
		private int locatorID;

		private BigDecimal onHandQty;
		private BigDecimal dedicatedQty;
		private BigDecimal allocatedQty;
//		private BigDecimal reservedQty;
//		private BigDecimal orderedQty;

		public VO(int M_Product_ID, int M_Locator_ID, 
				int M_AttributeSetInstance_ID,
				BigDecimal onhandQty,
				BigDecimal dedicatedQty, BigDecimal allocatedQty) 
		{
			this.productID = M_Product_ID;
			this.attributeSetInstanceID = M_AttributeSetInstance_ID;
			this.locatorID = M_Locator_ID;
			this.onHandQty = onhandQty;
			this.dedicatedQty = dedicatedQty;
			this.allocatedQty = allocatedQty;
		}

		public int getM_AttributeSetInstance_ID() {
			return attributeSetInstanceID;
		}

		public int getM_Locator_ID() {
			return locatorID;
		}

		public int getM_Product_ID() {
			return productID;
		}

		public BigDecimal getAvailableQty() {
			return getQtyOnHand().subtract(getQtyDedicated()).subtract(
					getQtyAllocated());
		}

		public BigDecimal getQtyOnHand() {
			return onHandQty;
		}
		public void setQtyOnHand(BigDecimal qty) {
			onHandQty = qty;
		}

		public BigDecimal getQtyDedicated() {
			return dedicatedQty;
		}
		public BigDecimal getQtyAllocated() {
			return allocatedQty;
		}

	}

	//////////////////////// Record Class
	
	public static class Record
	{
		private Map<X_Ref_Quantity_Type, MStorageDetail> storageMap = 
			new HashMap<X_Ref_Quantity_Type, MStorageDetail>();
		// reference storage detail for location, product and ASI info
		private MStorageDetail refDetail;
		
		
		public Record(Ctx ctx, int M_Locator_ID,
				int M_Product_ID, int M_AttributeSetInstance_ID,
				Collection<X_Ref_Quantity_Type> types, Trx trx) throws Exception 
		{
			if ((types == null) || types.size() < 1) {
				throw new Exception("Quantity types must not be null or empty");
			}
			Collection<MStorageDetail> storages = MStorageDetail.getMultipleForUpdate(ctx, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID, types, trx);
			for (MStorageDetail storage : storages) {
				storageMap.put(X_Ref_Quantity_Type.getEnum(storage.getQtyType()), storage);
				refDetail = storage;
			}
		}
		
		public Record(Collection<MStorageDetail> storageDetails) 
		{
			for (MStorageDetail storage : storageDetails) {
				storageMap.put(X_Ref_Quantity_Type.getEnum(storage.getQtyType()), storage);
				refDetail = storage;
			}
		}

		/**
		 * Gets a storage detail of specificied qty type
		 * If none found, create it
		 * @param qtyType  quantity type to get
		 * @return storage detail for the quantity type
		 */
		public MStorageDetail getDetail(X_Ref_Quantity_Type qtyType) 
		{
			return getDetail(qtyType, true);
		}
		
		
		/**
		 * Gets a storage detail of specificied qty type
		 * @param qtyType	 quantity type to get
		 * @param toCreate   if true, create detail if not found
		 * @return storage detail for the quantity type
		 */
		public MStorageDetail getDetail(X_Ref_Quantity_Type qtyType, boolean toCreate) 
		{
			MStorageDetail detail = storageMap.get(qtyType);
			if (detail == null) {
				detail = MStorageDetail.getForRead(refDetail.getCtx(), refDetail.getM_Locator_ID(), 
						refDetail.getM_Product_ID(), refDetail.getM_AttributeSetInstance_ID(), 
						qtyType, refDetail.get_Trx());
				if (detail == null && toCreate) {
					detail = MStorageDetail.getCreate(refDetail.getCtx(), refDetail.getM_Locator_ID(), 
							refDetail.getM_Product_ID(), refDetail.getM_AttributeSetInstance_ID(), 
							qtyType, refDetail.get_Trx());
					
				}
				storageMap.put(qtyType, detail);
			}
			return detail;
		}
		
	    public int getM_AttributeSetInstance_ID() {
	        return refDetail.getM_AttributeSetInstance_ID();
	    }
	    public int getM_Locator_ID() {
	        return refDetail.getM_Locator_ID();
	    }
	    
	    public int getM_Product_ID() {
	        return refDetail.getM_Product_ID();
	    }
		public int getM_Warehouse_ID() {
			return refDetail.getM_Warehouse_ID();
		}	
		
		
		public BigDecimal getAvailableQty() { 
			return getQtyOnHand().subtract(getQtyAllocated()).subtract(getQtyDedicated());
		}
		public BigDecimal getQtyOnHand()    { return getQty(X_Ref_Quantity_Type.ON_HAND); }
		public BigDecimal getQtyAllocated() { return getQty(X_Ref_Quantity_Type.ALLOCATED); }
		public BigDecimal getQtyDedicated() { return getQty(X_Ref_Quantity_Type.DEDICATED); }
		public BigDecimal getQtyOrdered()   { return getQty(X_Ref_Quantity_Type.ORDERED); }
		public BigDecimal getQtyExpected()  { return getQty(X_Ref_Quantity_Type.EXPECTED); }
		
		public BigDecimal getQty(X_Ref_Quantity_Type qtyType) {
			MStorageDetail detail = storageMap.get(qtyType);
			return ((detail == null) ? Env.ZERO : detail.getQty());
		}
		
		public void setDetailsBulkUpdate(boolean bulkUpdate) {
			for (MStorageDetail storage : storageMap.values()) {
				storage.setIsBulkUpdate(bulkUpdate);
			}
		}
		
		public boolean validate() 
		{
			MWarehouse wh = MWarehouse.get (refDetail.getCtx(), getM_Warehouse_ID());
			
			if(wh.isDisallowNegativeInv())
			{
				if(getQtyOnHand().signum() < 0 ||
						getQtyDedicated().signum() < 0 ||
						getQtyAllocated().signum() < 0 ||
						getQtyExpected().signum() < 0 )
				{
					s_log.saveError("Error", Msg.getMsg(refDetail.getCtx(), "NegativeInventoryDisallowed"));
					return false;
				}
				
				BigDecimal qtyOnHand = Env.ZERO;
				BigDecimal qtyDedicated = Env.ZERO;
				BigDecimal qtyAllocated = Env.ZERO;
				
				String sql = "SELECT COALESCE(SUM(QtyOnHand),0),COALESCE(SUM(QtyDedicated),0),COALESCE(SUM(QtyAllocated),0) "
					+ "FROM M_Storage_V s"
					+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID) "
					+ "WHERE s.M_Product_ID=?"		//	#1
					+ " AND l.M_Warehouse_ID=?"
					+ " AND l.M_Locator_ID=?"
					+ " AND M_AttributeSetInstance_ID<>?";
				
				ResultSet rs = null;
				PreparedStatement pstmt = null;

				try
				{
					pstmt = DB.prepareStatement (sql, refDetail.get_Trx());
					pstmt.setInt (1, getM_Product_ID());
					pstmt.setInt (2, getM_Warehouse_ID());
					pstmt.setInt (3, getM_Locator_ID());
					pstmt.setInt(4, getM_AttributeSetInstance_ID());
					rs = pstmt.executeQuery ();
					if (rs.next ())
					{
						qtyOnHand = rs.getBigDecimal(1);
						qtyDedicated = rs.getBigDecimal(2);
						qtyAllocated = rs.getBigDecimal(3);
					}
				}
				catch (Exception e)
				{
					s_log.log(Level.SEVERE, sql, e);
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				
				qtyOnHand = qtyOnHand.add(getQtyOnHand());
				qtyDedicated = qtyDedicated.add(getQtyDedicated());
				qtyAllocated = qtyAllocated.add(getQtyAllocated());
				
				if(qtyOnHand.signum() < 0 ||
						qtyOnHand.compareTo(qtyDedicated.add(qtyAllocated)) < 0 ||
						getQtyOnHand().compareTo(getQtyDedicated().add(getQtyAllocated())) < 0)
				{
					s_log.saveError("Error", Msg.getMsg(refDetail.getCtx(), "NegativeInventoryDisallowed"));
					return false;
				}
			}
			return true;
		}
	}
	
	/////////////////////////////////////////////

	/**
	 * Create storage for qty types that don't exist and return specified qty type  
	 */
	public static MStorageDetail getCreateDetails(Ctx ctx, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, X_Ref_Quantity_Type qtyTypeToGet, Trx trx)
	{
		MStorageDetail detailToGet = null;
		// create storage for all qty types
		for (X_Ref_Quantity_Type qtyType : X_Ref_Quantity_Type.values()) {
			MStorageDetail detail = MStorageDetail.getCreate(ctx, M_Locator_ID, M_Product_ID, 
					M_AttributeSetInstance_ID, qtyType, trx);
			detail.save(trx);
			if (qtyType == qtyTypeToGet)
				detailToGet = detail;
		}
		return detailToGet;
	}

	/**
	 * Create storage for qty types that don't exist and return specified qty type  
	 */
	public static Storage.Record getCreateRecord(Ctx ctx, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, Trx trx)
	{
		Collection<MStorageDetail> details = new ArrayList<MStorageDetail>();
		// create storage for all qty types
		for (X_Ref_Quantity_Type qtyType : X_Ref_Quantity_Type.values()) {
			MStorageDetail detail = MStorageDetail.getCreate(ctx, M_Locator_ID, M_Product_ID, 
					M_AttributeSetInstance_ID, qtyType, trx);
			detail.save(trx);
			details.add(detail);
		}
		
		return new Storage.Record(details);
	}
	
	/////////////////////////////////////////////
	
	/**
	 * Get all Storages for Product
	 * 
	 * @param ctx
	 *            context
	 * @param M_Product_ID
	 *            product
	 * @param M_Locator_ID
	 *            locator
	 * @param trx
	 *            transaction
	 * @return existing or null
	 */
	public static List<Storage.Record> getAll(Ctx ctx, 
			int M_Product_ID, int M_Locator_ID, BigDecimal qtyToDeliver, Trx trx) 
	{
		List<MStorageDetail> details = new ArrayList<MStorageDetail>();
		final List<Storage.Record> storages = new ArrayList<Storage.Record>();
		
		String sql = "select s.* "
			+ " from M_STORAGEDETAIL  s " 
			+ " where s.M_PRODUCT_ID = ? and s.M_LOCATOR_ID = ? "
		    + " and s.QTYTYPE in ('H','A','D') "
			+ " and exists (select 1 from M_STORAGEDETAIL t "
							+ " where t.QTYTYPE = 'H' "
						    + " and t.QTY <> 0 "
							+ " and s.AD_CLIENT_ID = t.AD_CLIENT_ID "
							+ " and s.AD_ORG_ID    = t.AD_ORG_ID "
							+ " and s.M_PRODUCT_ID = t.M_PRODUCT_ID "
							+ " and s.M_LOCATOR_ID = t.M_LOCATOR_ID  "
							+ " and s.M_AttributeSetInstance_ID = t.M_AttributeSetInstance_ID ) "
			+ " order by s.M_ATTRIBUTESETINSTANCE_ID ";
		
		if (DB.isOracle())
			sql += " FOR UPDATE OF s.QTY ";
		else	
			sql += " FOR UPDATE OF s ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			rs = pstmt.executeQuery();
			
			int prevASI = -1;
			while (rs.next()) {

				MStorageDetail detail = new MStorageDetail(ctx, rs, trx);
				if (prevASI == -1) {
					prevASI = detail.getM_AttributeSetInstance_ID();
				}
				if (detail.getM_AttributeSetInstance_ID() != prevASI) {
					Storage.Record record = new Storage.Record(details);
					storages.add(record);
					details.clear();
				}
				details.add(detail);
			}
			if (!details.isEmpty()) {
				Storage.Record record = new Storage.Record(details);
				storages.add(record);
				details.clear();
			}
		} catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		final List<Storage.Record> availableStorages = new ArrayList<Storage.Record>();
		for (Storage.Record storage : storages ) {
			if (storage.getAvailableQty().compareTo(Env.ZERO) <= 0) 
				continue;
			availableStorages.add(storage);
			if (qtyToDeliver != null) {
				qtyToDeliver = qtyToDeliver.subtract(storage.getAvailableQty());
				if (qtyToDeliver.compareTo(Env.ZERO) <= 0)
					break;
			}
		}
		
		return availableStorages;
	} // getAll
	
	/**
	 * 	Get all Storages for Product with ASI
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param M_Locator_ID locator
	 *	@param FiFo first in-first-out
	 *	@param qtyToDeliver the quantity to deliver, or null if no limit
	 *	@param trx transaction
	 *	@return existing or null
	 */
	public static List<Storage.Record> getAllWithASI(Ctx ctx, int M_Product_ID, int M_Locator_ID, 
			boolean FiFo, BigDecimal qtyToDeliver, Trx trx) 
	{
		List<MStorageDetail> details = new ArrayList<MStorageDetail>();
		final List<Storage.Record> storages = new ArrayList<Storage.Record>();
		String sql = "select s.* "
			+ " from M_STORAGEDETAIL  s " 
			+ " where s.M_PRODUCT_ID = ? and s.M_LOCATOR_ID = ? "
		    + " and s.QTYTYPE in ('H','A','D') "
			+ " and s.M_AttributeSetInstance_ID > 0 "
			+ " and exists (select 1 from M_STORAGEDETAIL t "
							+ " where t.QTYTYPE = 'H' "
						    + " and t.QTY <> 0 "
							+ " and s.AD_CLIENT_ID = t.AD_CLIENT_ID "
							+ " and s.AD_ORG_ID    = t.AD_ORG_ID "
							+ " and s.M_PRODUCT_ID = t.M_PRODUCT_ID "
							+ " and s.M_LOCATOR_ID = t.M_LOCATOR_ID  "
							+ " and s.M_AttributeSetInstance_ID = t.M_AttributeSetInstance_ID ) "
			+ " order by s.M_ATTRIBUTESETINSTANCE_ID ";
		
		if (!FiFo)
			sql += " DESC ";
		if (DB.isOracle())
			sql += " FOR UPDATE of s.QTY ";
		else 
			sql += " FOR UPDATE of s";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			rs = pstmt.executeQuery();
			int prevASI = -1;
			while (rs.next()) {
				MStorageDetail detail = new MStorageDetail(ctx, rs, trx);
				if (prevASI == -1) {
					prevASI = detail.getM_AttributeSetInstance_ID();
				}
					
				if (detail.getM_AttributeSetInstance_ID() != prevASI) {
					Storage.Record record = new Storage.Record(details);
					storages.add(record);
					details.clear();
					prevASI = detail.getM_AttributeSetInstance_ID();
				}
				details.add(detail);
			}
			if (!details.isEmpty()) {
				Storage.Record record = new Storage.Record(details);
				storages.add(record);
				details.clear();
			}
			
		} catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		
		final List<Storage.Record> availableStorages = new ArrayList<Storage.Record>();
		for (Storage.Record storage : storages ) {
			if (storage.getAvailableQty().compareTo(Env.ZERO) <= 0) 
				continue;
			availableStorages.add(storage);
			if (qtyToDeliver != null) {
				qtyToDeliver = qtyToDeliver.subtract(storage.getAvailableQty());
				if (qtyToDeliver.compareTo(Env.ZERO) <= 0)
					break;
			}
		}
		
		return availableStorages;
	} // getAll
	
	
	/**
	 * Get Storage Info for Warehouse
	 * 
	 * @param ctx
	 *            context
	 * @param M_Warehouse_ID
	 * @param M_Product_ID
	 *            product
	 * @param M_AttributeSetInstance_ID
	 *            instance
	 * @param M_AttributeSet_ID
	 *            attribute set
	 * @param allAttributeInstances
	 *            if true, all attribute set instances
	 * @param minGuaranteeDate
	 *            optional minimum guarantee date if all attribute instances
	 * @param FiFo
	 *            first in-first-out
	 * @param trx
	 *            transaction
	 * @return existing - ordered by location priority (desc) and/or guarantee
	 *         date
	 */
	public static List<Storage.VO> getWarehouse(Ctx ctx, int M_Warehouse_ID,
			int M_Product_ID, int M_AttributeSetInstance_ID,
			int M_AttributeSet_ID, boolean allAttributeInstances,
			Timestamp minGuaranteeDate, boolean FiFo, boolean allocationCheck,
			int M_SourceZone_ID, Trx trx) 
	{
		if (M_Warehouse_ID == 0 || M_Product_ID == 0)
			return null;

		if (M_AttributeSet_ID == 0)
			allAttributeInstances = true;
		else {
			MAttributeSet mas = MAttributeSet.get(ctx, M_AttributeSet_ID);
			if (!mas.isInstanceAttribute())
				allAttributeInstances = true;
		}

		List<Storage.VO> list = new ArrayList<Storage.VO>();
		String sql = null;
		// All Attribute Set Instances
		if (allAttributeInstances) {
			sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'H' THEN Qty ELSE 0 END),0) QtyOnhand,"
					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'D' THEN Qty ELSE 0 END),0) QtyDedicated,"
					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'A' THEN Qty ELSE 0 END),0) QtyAllocated "
					+ "FROM M_StorageDetail s"
					+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID)"
					+ " LEFT OUTER JOIN M_AttributeSetInstance asi ON (s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID) "
					+ "WHERE l.M_Warehouse_ID=?" + " AND s.M_Product_ID=? ";

			if (allocationCheck)
				sql += "AND l.IsAvailableForAllocation='Y' ";

			if (M_SourceZone_ID != 0)
				sql += "AND l.M_Locator_ID IN "
						+ " (SELECT M_Locator_ID FROM M_ZoneLocator WHERE M_Zone_ID = ? ) ";

			if (minGuaranteeDate != null) {
				sql += "AND (asi.GuaranteeDate IS NULL OR asi.GuaranteeDate>?) "
						+ "GROUP BY asi.GuaranteeDate, l.PriorityNo, s.M_Product_ID, s.M_Locator_ID, s.M_AttributeSetInstance_ID "
						+ "ORDER BY asi.GuaranteeDate, l.PriorityNo DESC, M_AttributeSetInstance_ID";
			} else {
				sql += "GROUP BY l.PriorityNo, s.M_Product_ID, s.M_Locator_ID, s.M_AttributeSetInstance_ID "
						+ "ORDER BY l.PriorityNo DESC, s.M_AttributeSetInstance_ID";
			}
			if (!FiFo)
				sql += " DESC";
			sql += ", QtyOnHand DESC";
		}
		else {
			// Specific Attribute Set Instance
			sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'H' THEN Qty ELSE 0 END),0) QtyOnhand,"
					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'D' THEN Qty ELSE 0 END),0) QtyDedicated,"
					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'A' THEN Qty ELSE 0 END),0) QtyAllocated "
					+ "FROM M_StorageDetail s"
					+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
					+ "WHERE l.M_Warehouse_ID=?"
					+ " AND s.M_Product_ID=?"
					+ " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";

			if (allocationCheck)
				sql += "AND l.IsAvailableForAllocation='Y' ";

			if (M_SourceZone_ID != 0)
				sql += "AND l.M_Locator_ID IN "
						+ " (SELECT M_Locator_ID FROM M_ZoneLocator WHERE M_Zone_ID = ? ) ";
			sql += "GROUP BY l.PriorityNo, s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID "
					+ "ORDER BY l.PriorityNo DESC, M_AttributeSetInstance_ID";

			if (!FiFo)
				sql += " DESC";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trx);
			int index = 1;
			pstmt.setInt(index++, M_Warehouse_ID);
			pstmt.setInt(index++, M_Product_ID);
			if (M_SourceZone_ID != 0)
				pstmt.setInt(index++, M_SourceZone_ID);
			if (!allAttributeInstances)
				pstmt.setInt(index++, M_AttributeSetInstance_ID);
			else if (minGuaranteeDate != null)
				pstmt.setTimestamp(index++, minGuaranteeDate);
			rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				index = 1;
				int rs_M_Product_ID = rs.getInt(index++);
				int rs_M_Locator_ID = rs.getInt(index++);
				int rs_M_AttributeSetInstance_ID = rs.getInt(index++);
				BigDecimal rs_QtyOnhand = rs.getBigDecimal(index++);
				BigDecimal rs_QtyDedicated = rs.getBigDecimal(index++);
				BigDecimal rs_QtyAllocated = rs.getBigDecimal(index++);
				list.add(new Storage.VO(rs_M_Product_ID, rs_M_Locator_ID, 
						rs_M_AttributeSetInstance_ID,
						rs_QtyOnhand,
						rs_QtyDedicated, rs_QtyAllocated));
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return list;
	} 

	public static List<Storage.VO> getWarehouse(Ctx ctx,
			int M_Warehouse_ID, int M_Product_ID,
			int M_AttributeSetInstance_ID, int M_AttributeSet_ID,
			boolean allAttributeInstances, Timestamp minGuaranteeDate,
			boolean FiFo, Trx trx) {
		return getWarehouse(ctx, M_Warehouse_ID, M_Product_ID,
				M_AttributeSetInstance_ID, M_AttributeSet_ID,
				allAttributeInstances, minGuaranteeDate, FiFo, false, 0, trx);
	}
	
	/***************
	 * 
	 * @param ctx
	 * @param M_Warehouse_ID
	 * @param M_Product_ID
	 * @param M_AttributeSetInstance_ID
	 * @param M_AttributeSet_ID
	 * @param allAttributeInstances
	 * @param minGuaranteeDate
	 * @param FiFo
	 * @param allocationCheck
	 * @param M_SourceZone_ID
	 * @param trx
	 * @return
	 */
//	public static List<Storage.VO> getAllWithASI(Ctx ctx, int M_Product_ID, int M_Locator_ID, 
//			boolean FiFo, BigDecimal qtyToDeliver, Trx trx)
//	{
//		if (M_Product_ID == 0)
//			return null;
//
//
//		List<Storage.VO> list = new ArrayList<Storage.VO>();
//		// Specific Attribute Set Instance
//		String sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
//				+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'H' THEN Qty ELSE 0 END),0) QtyOnhand,"
//				+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'D' THEN Qty ELSE 0 END),0) QtyDedicated,"
//				+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'A' THEN Qty ELSE 0 END),0) QtyAllocated "
//				+ "FROM M_StorageDetail s"
//				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
//				+ "WHERE l.M_Warehouse_ID=?"
//				+ " AND s.M_Product_ID=?"
//				+ " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";
//
//		if (allocationCheck)
//			sql += "AND l.IsAvailableForAllocation='Y' ";
//
//		if (M_SourceZone_ID != 0)
//			sql += "AND l.M_Locator_ID IN "
//					+ " (SELECT M_Locator_ID FROM M_ZoneLocator WHERE M_Zone_ID = ? ) ";
//		sql += "GROUP BY l.PriorityNo, s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID "
//				+ "ORDER BY l.PriorityNo DESC, M_AttributeSetInstance_ID";
//
//		if (!FiFo)
//			sql += " DESC";
//		// All Attribute Set Instances
//		if (allAttributeInstances) {
//			sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
//					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'H' THEN Qty ELSE 0 END),0) QtyOnhand,"
//					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'D' THEN Qty ELSE 0 END),0) QtyDedicated,"
//					+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'A' THEN Qty ELSE 0 END),0) QtyAllocated "
//					+ "FROM M_StorageDetail s"
//					+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID)"
//					+ " LEFT OUTER JOIN M_AttributeSetInstance asi ON (s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID) "
//					+ "WHERE l.M_Warehouse_ID=?" + " AND s.M_Product_ID=? ";
//
//			if (allocationCheck)
//				sql += "AND l.IsAvailableForAllocation='Y' ";
//
//			if (M_SourceZone_ID != 0)
//				sql += "AND l.M_Locator_ID IN "
//						+ " (SELECT M_Locator_ID FROM M_ZoneLocator WHERE M_Zone_ID = ? ) ";
//
//			if (minGuaranteeDate != null) {
//				sql += "AND (asi.GuaranteeDate IS NULL OR asi.GuaranteeDate>?) "
//						+ "GROUP BY asi.GuaranteeDate, l.PriorityNo, s.M_Product_ID, s.M_Locator_ID, s.M_AttributeSetInstance_ID "
//						+ "ORDER BY asi.GuaranteeDate, l.PriorityNo DESC, M_AttributeSetInstance_ID";
//			} else {
//				sql += "GROUP BY l.PriorityNo, s.M_Product_ID, s.M_Locator_ID, s.M_AttributeSetInstance_ID "
//						+ "ORDER BY l.PriorityNo DESC, s.M_AttributeSetInstance_ID";
//			}
//			if (!FiFo)
//				sql += " DESC";
//			sql += ", COALESCE(SUM(CASE WHEN QtyType LIKE 'H' THEN Qty ELSE 0 END),0) DESC";
//		}
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try {
//			pstmt = DB.prepareStatement(sql, trx);
//			int index = 1;
//			pstmt.setInt(index++, M_Warehouse_ID);
//			pstmt.setInt(index++, M_Product_ID);
//			if (M_SourceZone_ID != 0)
//				pstmt.setInt(index++, M_SourceZone_ID);
//			if (!allAttributeInstances)
//				pstmt.setInt(index++, M_AttributeSetInstance_ID);
//			else if (minGuaranteeDate != null)
//				pstmt.setTimestamp(index++, minGuaranteeDate);
//			rs = pstmt.executeQuery();
//			while (rs.next()) 
//			{
//				index = 1;
//				int rs_M_Product_ID = rs.getInt(1);
//				int rs_M_Locator_ID = rs.getInt(index++);
//				int rs_M_AttributeSetInstance_ID = rs.getInt(index++);
//				BigDecimal rs_QtyOnhand = rs.getBigDecimal(index++);
//				BigDecimal rs_QtyDedicated = rs.getBigDecimal(index++);
//				BigDecimal rs_QtyAllocated = rs.getBigDecimal(index++);
//				list.add(new Storage.VO(rs_M_Product_ID, rs_M_Locator_ID,
//						rs_M_AttributeSetInstance_ID, rs_QtyOnhand,
//						rs_QtyDedicated, rs_QtyAllocated));
//			}
//		} catch (Exception e) {
//			s_log.log(Level.SEVERE, sql, e);
//		}
//		finally {
//			DB.closeResultSet(rs);
//			DB.closeStatement(pstmt);
//		}
//		
//		return list;
//	} 	
		
	
	/**
	 * Get Available Qty. The call is accurate only if there is a storage record
	 * and assumes that the product is stocked
	 * 
	 * @param M_Warehouse_ID
	 *            wh
	 * @param M_Product_ID
	 *            product
	 * @param M_AttributeSetInstance_ID
	 *            masi
	 * @param trx
	 *            transaction
	 * @return qty available (QtyOnHand-QtyReserved) or null
	 */
	public static BigDecimal getQtyAvailable(int M_Warehouse_ID,
			int M_Product_ID, int M_AttributeSetInstance_ID, Trx trx) 
	{
		BigDecimal QtyOnHand = Env.ZERO;
		BigDecimal QtyReserved = Env.ZERO;
		
		String sql = "SELECT COALESCE(SUM(CASE WHEN QtyType LIKE 'H' AND l.IsAvailableToPromise = 'Y' THEN Qty ELSE 0 END), 0) QtyOnHand, "
				+ "COALESCE(SUM(CASE WHEN QtyType LIKE 'R' THEN Qty ELSE 0 END), 0) QtyReserved "
				+ "FROM M_StorageDetail s "
				+ "INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID) "
				+ "WHERE s.IsActive = 'Y' AND s.M_Product_ID=? " // #1
				+ "AND l.IsActive = 'Y' AND l.M_Warehouse_ID=? ";
		if (M_AttributeSetInstance_ID != 0)
			sql += "AND M_AttributeSetInstance_ID=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Warehouse_ID);
			if (M_AttributeSetInstance_ID != 0)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				QtyOnHand = rs.getBigDecimal(1);
				QtyReserved = rs.getBigDecimal(2);
				if (rs.wasNull())
					QtyOnHand = Env.ZERO;
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
	    } finally {
	        DB.closeResultSet( rs );
	        DB.closeStatement( pstmt );
	    }
		
		s_log.fine("M_Warehouse_ID=" + M_Warehouse_ID + ",M_Product_ID="
				+ M_Product_ID + " : " + " QtyOnHand=" + QtyOnHand
				+ ", QtyReserved=" + QtyReserved);
		return QtyOnHand.subtract(QtyReserved);
	} 
	
	
	/////////////
	
	/**************************************************************************
	 * Get Location with highest Locator Priority and a sufficient OnHand Qty
	 * 
	 * @param M_Warehouse_ID
	 *            warehouse
	 * @param M_Product_ID
	 *            product
	 * @param M_AttributeSetInstance_ID
	 *            asi
	 * @param Qty
	 *            qty
	 * @param trx
	 *            transaction
	 * @return id
	 */
	public static int getLocatorID(int M_Warehouse_ID, int M_Product_ID,
			int M_AttributeSetInstance_ID, BigDecimal Qty, Trx trx) 
	{
		int M_Locator_ID = 0;
		int firstM_Locator_ID = 0;
		String sql = "SELECT s.M_Locator_ID, s.Qty "
				+ "FROM M_StorageDetail s"
				+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID)"
				+ " INNER JOIN M_Product p ON (s.M_Product_ID=p.M_Product_ID)"
				+ " LEFT OUTER JOIN M_AttributeSet mas ON (p.M_AttributeSet_ID=mas.M_AttributeSet_ID) "
				+ "WHERE l.M_Warehouse_ID=?"
				+ " AND s.M_Product_ID=?"
				+ " AND (mas.IsInstanceAttribute IS NULL OR mas.IsInstanceAttribute='N' OR s.M_AttributeSetInstance_ID=?)"
				+ " AND l.IsActive='Y' " + " AND s.QtyType='H' "
				+ "ORDER BY l.PriorityNo DESC, s.Qty DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setInt(2, M_Product_ID);
			pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BigDecimal QtyOnHand = rs.getBigDecimal(2);
				if (QtyOnHand != null && Qty.compareTo(QtyOnHand) <= 0) {
					M_Locator_ID = rs.getInt(1);
					break;
				}
				if (firstM_Locator_ID == 0)
					firstM_Locator_ID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (M_Locator_ID != 0)
			return M_Locator_ID;
		return firstM_Locator_ID;
	} // getM_Locator_ID	

	
	public static BigDecimal getProductQty(Ctx ctx, int M_Product_ID,
			X_Ref_Quantity_Type qtyType, Trx trx) 
	{
		final String sql = "SELECT COALESCE(SUM(QTY),0) FROM M_StorageDetail "
				+ " WHERE M_Product_ID=? AND QtyType=?";
		BigDecimal qty = BigDecimal.ZERO;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setString(2, qtyType.getValue());
			rs = pstmt.executeQuery();
			if (rs.next())
				qty = rs.getBigDecimal(1);
		} catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return qty;
	} // getProductQty

	
	
	/////////////  ADD QUANTITIES

	public static boolean addQtys(Ctx ctx, int M_Warehouse_ID,
			int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID,
			BigDecimal diffQtyOnHand,
			BigDecimal diffQtyReserved, BigDecimal diffQtyOrdered, Trx trx) {
		return addQtys(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
				M_AttributeSetInstance_ID, M_AttributeSetInstance_ID,
				diffQtyOnHand, diffQtyReserved, diffQtyOrdered, 
				Env.ZERO, Env.ZERO, Env.ZERO, trx);
	} 

	public static boolean addQtys(Ctx ctx, int M_Warehouse_ID,
			int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID,
			BigDecimal diffQtyOnHand, BigDecimal diffQtyReserved, BigDecimal diffQtyOrdered,
			BigDecimal diffQtyDedicated, BigDecimal diffQtyExpected, BigDecimal diffQtyAllocated, 
			Trx trx) {
		return addQtys(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
				M_AttributeSetInstance_ID, M_AttributeSetInstance_ID,
				diffQtyOnHand, diffQtyReserved, diffQtyOrdered, 
				diffQtyDedicated, diffQtyExpected, diffQtyAllocated, 
				trx);
	} 
	
	public static boolean addQtys(Ctx ctx, int M_Warehouse_ID,
			int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID,
			int reservationAttributeSetInstance_ID, BigDecimal diffQtyOnHand,
			BigDecimal diffQtyReserved, BigDecimal diffQtyOrdered, Trx trx) {
		return addQtys(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
				M_AttributeSetInstance_ID, reservationAttributeSetInstance_ID,
				diffQtyOnHand, diffQtyReserved, diffQtyOrdered, Env.ZERO,
				Env.ZERO, Env.ZERO, trx);
	} 

	public static boolean addQtys(Ctx ctx, int M_Warehouse_ID,
			int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID,
			int reservationAttributeSetInstance_ID, BigDecimal diffQtyOnHand,
			BigDecimal diffQtyReserved, BigDecimal diffQtyOrdered,
			BigDecimal diffQtyDedicated, BigDecimal diffQtyExpected,
			BigDecimal diffQtyAllocated, Trx trx) 
	{
		if (diffQtyOnHand!=null && diffQtyOnHand.compareTo(Env.ZERO) != 0) {
			if (!MStorageDetail.add(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
					M_AttributeSetInstance_ID,
					reservationAttributeSetInstance_ID, diffQtyOnHand,
					X_Ref_Quantity_Type.ON_HAND, trx))
				return false;
		}
		if (diffQtyReserved!=null && diffQtyReserved.compareTo(Env.ZERO) != 0) {
			if (!MStorageDetail.add(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
					M_AttributeSetInstance_ID,
					reservationAttributeSetInstance_ID, diffQtyReserved,
					X_Ref_Quantity_Type.RESERVED, trx))
				return false;
		}
		if (diffQtyOrdered!=null && diffQtyOrdered.compareTo(Env.ZERO) != 0) {
			if (!MStorageDetail.add(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
					M_AttributeSetInstance_ID,
					reservationAttributeSetInstance_ID, diffQtyOrdered,
					X_Ref_Quantity_Type.ORDERED, trx))
				return false;
		}
		if (diffQtyDedicated!=null && diffQtyDedicated.compareTo(Env.ZERO) != 0) {
			if (!MStorageDetail.add(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
					M_AttributeSetInstance_ID,
					reservationAttributeSetInstance_ID, diffQtyDedicated,
					X_Ref_Quantity_Type.DEDICATED, trx))
				return false;
		}
		if (diffQtyAllocated!=null && diffQtyAllocated.compareTo(Env.ZERO) != 0) {
			if (!MStorageDetail.add(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
					M_AttributeSetInstance_ID,
					reservationAttributeSetInstance_ID, diffQtyAllocated,
					X_Ref_Quantity_Type.ALLOCATED, trx))
				return false;
		}
		if (diffQtyExpected!=null && diffQtyExpected.compareTo(Env.ZERO) != 0) {
			if (!MStorageDetail.add(ctx, M_Warehouse_ID, M_Locator_ID, M_Product_ID,
					M_AttributeSetInstance_ID,
					reservationAttributeSetInstance_ID, diffQtyExpected,
					X_Ref_Quantity_Type.EXPECTED, trx))
				return false;
		}
		return true;
	}	

}
