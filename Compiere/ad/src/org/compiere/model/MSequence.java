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
import java.util.concurrent.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Sequence Model.
 *	@see org.compiere.process.SequenceCheck
 *  @author Jorg Janke
 *  @version $Id: MSequence.java 9203 2010-08-26 21:30:55Z kvora $
 */
public class MSequence extends X_AD_Sequence
{
    /** Logger for class MSequence */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSequence.class);

    private static final long serialVersionUID = 1L;
	/** Test with transaction						*/
	private static final boolean	TEST_TRX = false;
	/** Log Level for Next ID Call					*/
	private static final Level LOGLEVEL = Level.ALL;

	// The batch sizes indicate how many numbers we 'grab' and hold in the
	// s_sequences in-memory cache. Bigger values will be slightly more efficient
	// but run a slight risk of leaving bigger gaps.
	private static final int PRIMARY_KEY_BATCH_SIZE = 100;
	private static final int DOCUMENT_NO_BATCH_SIZE = 100;

	/**	Sequence for Table Document No's	*/
	private static final String	PREFIX_DOCSEQ = "DocumentNo_";
	/**	Start Number			*/
	public static final int		INIT_NO = 1000000;	//	1 Mio
	/**	Start System Number		*/
	public static final int		INIT_SYS_NO = 100;
	/** Static Logger			*/
	private static CLogger 		s_log = CLogger.getCLogger(MSequence.class);

	//initialize all to 0 so nextSeq < endSeq won't be satisfied when Sequence is 1st created 
	private static class Sequence {
		int nextSeq = 0;
		int incrementNo = 0;
		int endSeq = 0;
		String suffix;
		String prefix;
		boolean isGapless = true;
	}
	
	private static final ConcurrentHashMap<String, Sequence> s_sequences = new ConcurrentHashMap<String, Sequence>();
	
	public static void onSystemShutdown() {
		for(Map.Entry<String, Sequence> entry :s_sequences.entrySet()) {
			final String[] tokens = entry.getKey().split("\\.");
			final String tableName = tokens[1];
			final int AD_Client_ID = Integer.parseInt(tokens[0]);
			final String isTableID = tokens[2];
			final String selectSQL;
			// TODO seems reversed? AD_Client_ID==-1 for Document No? missing ad_client_ID check?
			if(AD_Client_ID == -1) 
				selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_Sequence_ID "
					+ "FROM AD_Sequence "
					+ "WHERE AD_Sequence_ID=?"
					+ " AND IsActive='Y' AND IsTableID='N' AND IsAutoSequence='Y' "
					+ " FOR UPDATE";
			else
			{
				String beginSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_Sequence_ID "
					+ "FROM AD_Sequence "
					+ "WHERE Name=? "
					+ "AND IsActive='Y' ";
					 
				String endSQL = "AND IsTableID=? AND IsAutoSequence='Y' "
					+ " FOR UPDATE";

				if ("Y".equals(isTableID))
					selectSQL = beginSQL + "AND AD_Client_ID=? " + endSQL; 
				else
					selectSQL = beginSQL + endSQL;
			}
			final Sequence seq = entry.getValue();
			//at this point there should not be a need for syncrhonization, just for safety
			synchronized(seq) {
				final Trx trx = Trx.get("MSequence.onSystemShutdown()");
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = trx.getConnection().prepareStatement(selectSQL, ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_UPDATABLE);
					if(AD_Client_ID == -1)
						pstmt.setInt(1, Integer.parseInt(tableName));
					else
					{
						if ("Y".equals(isTableID))
							pstmt.setString(1, tableName);
						else
						{
							pstmt.setString(1, PREFIX_DOCSEQ+tableName);
						}
						pstmt.setString(2, isTableID);
					}

					rs = pstmt.executeQuery();
					
					if (rs.next()) {

						final int columnIndex = isCompiereSys(AD_Client_ID) ? 2:1;
						final int dbNextSeq = rs.getInt(columnIndex);
						// only when db nextseq equals to the jvm endseq then i'll write back. this is so if there are multiple
						//jvms running, i know that other jvms already advanced the sequenes so that i don't mess with it
						if(dbNextSeq == seq.endSeq) {
							seq.endSeq = seq.nextSeq;
							rs.updateInt(columnIndex, seq.nextSeq);
							rs.updateRow();
						}
					}
				} catch (Exception e) {
					s_log.log(Level.SEVERE, tableName + " - " + e.getMessage(), e);
				} finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);

					if (trx != null) {
						trx.commit();
						trx.close();
					}
				}
			}
		}
	}
	
	public static boolean isCompiereSys(int AD_Client_ID) {
		boolean compiereSys = Ini.isPropertyBool(Ini.P_COMPIERESYS);
		if (compiereSys && AD_Client_ID > 11)
			compiereSys = false;
		return compiereSys;
	}
	
	/**
	 * 
	 *	Get next number for Key column = 0 is Error.
	 *  @param AD_Client_ID client
	 * @param tableName table name
	 *  @return next no or (-1=not found, -2=error)
	 */
	public static int getNextID (int AD_Client_ID, String tableName)
	{
		if (tableName == null || tableName.length() == 0)
			throw new IllegalArgumentException("TableName missing");
	
		final boolean compiereSys = isCompiereSys(AD_Client_ID);

		final String hashKey = AD_Client_ID + "." + tableName+".Y";
		Sequence seq = s_sequences.get(hashKey);
		if (seq == null) {
			// Standard idiom using putIfAbsent(). This will properly handle
			// multiple simultaneous inserts without resorting to manual locking
			Sequence newSeq = new Sequence();
			seq = s_sequences.putIfAbsent(hashKey, newSeq);
			if (seq == null)
				seq = newSeq;
		}

		synchronized(seq) {
			if(seq.nextSeq < seq.endSeq) {
				final int retValue = seq.nextSeq;
				seq.nextSeq+=seq.incrementNo;
				return retValue;
			}
			else {
				return getNextIDFromDB(tableName, compiereSys, seq);
			}
		}
	}
				
	private static int getNextIDFromDB(String tableName,
					final boolean compiereSys, Sequence seq) {
		int retValue = -1;

		if (CLogMgt.isLevel(LOGLEVEL))
			s_log.log(LOGLEVEL, tableName + " - CompiereSys=" + compiereSys  + " [" + null + "]");
		
		final String selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_Sequence_ID "
			+ "FROM AD_Sequence "
			+ "WHERE Name=?"
			+ " AND IsActive='Y' AND IsTableID='Y' AND IsAutoSequence='Y' "
			+ " FOR UPDATE";


		Trx trx = Trx.get("MSequence.getNextID()");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = trx.getConnection().prepareStatement(selectSQL, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, tableName);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				seq.incrementNo = rs.getInt(3);

				final int columnIndex = compiereSys ? 2:1;
				retValue = rs.getInt(columnIndex);
				seq.nextSeq = retValue + seq.incrementNo;
				// if system, then no gap, since system sequence has
				// a smaller ceiling
				seq.endSeq = retValue + (compiereSys ? 1:PRIMARY_KEY_BATCH_SIZE) * seq.incrementNo;

				rs.updateInt(columnIndex, seq.endSeq);
				rs.updateRow();
			} else
				s_log.severe("No record found - " + tableName);
			
		} catch (Exception e) {
			s_log.log(Level.SEVERE, tableName + " - " + e.getMessage(), e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			
			if (trx != null) {
				trx.commit();
				trx.close();
			}
		}

		s_log.finest (retValue + " - Table=" + tableName + " [" + null + "]");
		return retValue;
	}
	

	/***************************************************************************
	 * Get Document No from table
	 * 
	 * @param AD_Client_ID
	 *            client
	 * @param tableName
	 *            table name
	 * @param trx
	 *            optional Transaction Name
	 * @return document no or null
	 */
	public static String   getDocumentNo (int AD_Client_ID, String tableName, final Trx trx)
	{
		if (Util.isEmpty(tableName))
			throw new IllegalArgumentException("TableName missing");

		if (CLogMgt.isLevel(LOGLEVEL))
			s_log.log(LOGLEVEL, tableName + " - CompiereSys=" + isCompiereSys(AD_Client_ID)  + " [" + trx + "]");

		String hashKey = AD_Client_ID + "." + tableName+".N";
		return getDocumentNO(AD_Client_ID, hashKey, trx, new Object[] {tableName, AD_Client_ID}, false);

	}	

	/**
	 * 	Get Document No based on Document Type
	 *	@param C_DocType_ID document type
	 * 	@param trx optional Transaction Name
	 *	@return document no or null
	 */
	public static String getDocumentNo (int C_DocType_ID, final Trx trx)
	{
		if (C_DocType_ID == 0)
		{
			s_log.severe ("C_DocType_ID=0");
			return null;
		}
		final MDocType dt = MDocType.get (Env.getCtx(), C_DocType_ID);	//	wrong for SERVER, but r/o
		if (dt != null && !dt.isDocNoControlled())
		{
			s_log.finer("DocType_ID=" + C_DocType_ID + " Not DocNo controlled");
			return null;
		}
		if (dt == null || dt.getDocNoSequence_ID() == 0)
		{
			s_log.warning ("No Sequence for DocType - " + dt);
			return null;
		}

		final int AD_Client_ID = dt.getAD_Client_ID();

		//	Check CompiereSys
		if (CLogMgt.isLevel(LOGLEVEL))
			s_log.log(LOGLEVEL, "DocType_ID=" + C_DocType_ID + " [" + trx + "]");
		String hashKey = "-1." + dt.getDocNoSequence_ID();
		return getDocumentNO(AD_Client_ID, hashKey, trx, new Object[] {dt.getDocNoSequence_ID()}, true);
	}

	private static PreparedStatement createRsForDocumentNo(Trx localTrx, String selectSQL, boolean useSequenceID, Object[] params) 
	throws Exception {
		PreparedStatement pstmt = localTrx.getConnection().prepareStatement(selectSQL,
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		if(useSequenceID)
			pstmt.setInt(1, (Integer)params[0]);
		else {
			pstmt.setString(1, PREFIX_DOCSEQ+(String)params[0]);
			pstmt.setInt(2, (Integer)params[1]);
		}
		return pstmt;

	}
	private static String getDocumentNO(int AD_Client_ID, String hashKey, Trx trx, Object[] params, boolean useSequenceID) {

		String selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, Prefix, Suffix, AD_Sequence_ID FROM AD_Sequence ";

		StringBuilder whereClause = new StringBuilder();
		if(useSequenceID)
			whereClause.append("WHERE AD_Sequence_ID=?");
		else
			whereClause.append("WHERE Name=?"
					+ " AND AD_Client_ID = ?") ;
		whereClause.append(" AND IsActive='Y' AND IsTableID='N' AND IsAutoSequence='Y' FOR UPDATE ");
		selectSQL += whereClause;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int next = -1;
		Sequence seq = s_sequences.get(hashKey);
		if (seq == null) {
			// Standard idiom using putIfAbsent(). This will properly handle
			// multiple simultaneous inserts without resorting to manual locking
			Sequence newSeq = new Sequence();
			seq = s_sequences.putIfAbsent(hashKey, newSeq);
			if (seq == null)
				seq = newSeq;
			Trx initTrx = null;
			try {
				initTrx = Trx.get("MSequence.getDocumentNoInit()");
				pstmt = createRsForDocumentNo(initTrx, "SELECT IsGapless FROM AD_Sequence "+whereClause, useSequenceID, params);
				rs = pstmt.executeQuery();
				if(rs.next())
					seq.isGapless = "Y".equals(rs.getString(1));
			}
			catch(Exception e) {
				s_log.log(Level.SEVERE, "can't find record for document no init");
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
				
				if( initTrx != null) {
					initTrx.commit();
					initTrx.close();
				}

			}

		}

		synchronized(seq) {
			if(seq.nextSeq < seq.endSeq) {
				next = seq.nextSeq;
				seq.nextSeq += seq.incrementNo;
			}
			else {

				//if document no is NOT gapless, it means we can use our sequence caching algorithm
				//NOTE: the first time a seq object is created, the isGapless is always true, therefore no caching algorithm, but that is OK as long as the ensuing getDocumentNO() uses it
				//interval == 1 meaning every call would go to db, effectively gapless

				int batchSize = seq.isGapless ? 1 : DOCUMENT_NO_BATCH_SIZE;
				Trx localTrx = seq.isGapless ? trx : null;
				if( localTrx == null)
					localTrx = Trx.get("MSequence.getDocumentNo()");

				try	
				{
					pstmt = createRsForDocumentNo(localTrx, selectSQL, useSequenceID, params);
					rs = pstmt.executeQuery();
					if (rs.next())
					{
						seq.incrementNo = rs.getInt(3);
						seq.prefix = rs.getString(4);
						seq.suffix = rs.getString(5);

						int columnIndex = isCompiereSys(AD_Client_ID)?2 : 1;
						next = rs.getInt(columnIndex);
						seq.nextSeq = next + seq.incrementNo;
						seq.endSeq = next + (isCompiereSys(AD_Client_ID)?1:batchSize) * seq.incrementNo;
						rs.updateInt(columnIndex, seq.endSeq);
						rs.updateRow();
					}
					else
					{
						if(useSequenceID) {
							s_log.warning ("(DocType)- no record found - " + params[0]);
							next = -2;
						}
						//if using name, try use getNextID as a fall back
						else {
							s_log.warning ("(Table) - no record found - " + params[0]);
							next = MSequence.getNextID((Integer)params[1], (String)params[0]);
						}
					}
				}
				catch (Exception e)	{
					s_log.log(Level.SEVERE, "(DocType) [" + localTrx + "]", e);
					next = -2;
				}
				finally	{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
					
					if( localTrx != null && localTrx != trx ) {
						localTrx.commit();
						localTrx.close();
					}
				}
			}
		}
		//	Error
		if (next < 0)
			return null;

		//	create DocumentNo
		StringBuilder doc = new StringBuilder();
		if (seq.prefix != null && seq.prefix.length() > 0)
			doc.append(seq.prefix);
		doc.append(next);
		if (seq.suffix != null && seq.suffix.length() > 0)
			doc.append(seq.suffix);
		String documentNo = doc.toString();
		return documentNo;
	}	


	/**************************************************************************
	 *	Check/Initialize Client DocumentNo/Value Sequences
	 *	@param ctx context
	 *	@param AD_Client_ID client
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean checkClientSequences (Ctx ctx, int AD_Client_ID, Trx trx)
	{
		String sql = "SELECT TableName "
			+ "FROM AD_Table t "
			+ "WHERE IsActive='Y' AND IsView='N'"
			//	Get all Tables with DocumentNo or Value
			+ " AND AD_Table_ID IN "
			+ "(SELECT AD_Table_ID FROM AD_Column "
			+ "WHERE ColumnName = 'DocumentNo' OR ColumnName = 'Value')"
			//	Ability to run multiple times
			+ " AND 'DocumentNo_' || TableName NOT IN "
			+ "(SELECT Name FROM AD_Sequence s "
			+ "WHERE s.AD_Client_ID=?)";
		int counter = 0;
		boolean success = true;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				final String tableName = rs.getString(1);
				s_log.fine("Add: " + tableName);
				final MSequence seq = new MSequence (ctx, AD_Client_ID, tableName, trx);
				if (seq.save())
					counter++;
				else {
					s_log.severe ("Not created - AD_Client_ID=" + AD_Client_ID
							+ " - "  + tableName);
					success = false;
				}
			}
		}
		catch (Exception e)	{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally	{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		s_log.info ("AD_Client_ID=" + AD_Client_ID
				+ " - created #" + counter
				+ " - success=" + success);
		return success;
	}	


	/**
	 * 	Create Table ID Sequence
	 * 	@param ctx context
	 * 	@param TableName table name
	 *	@param trx transaction
	 * 	@return true if created
	 */
	public static boolean createTableSequence (Ctx ctx, String TableName, Trx trx)
	{
		final MSequence seq = new MSequence (ctx, 0, trx);
		seq.setClientOrg(0, 0);
		seq.setName(TableName);
		seq.setDescription("Table " + TableName);
		seq.setIsTableID(true);
		return seq.save();
	}	

	/**
	 * 	Delete Table ID Sequence
	 * 	@param ctx context
	 * 	@param TableName table name
	 *	@param trx transaction
	 * 	@return true if created
	 */
	public static boolean deleteTableSequence (Ctx ctx, String TableName, Trx trx)
	{
		final MSequence seq = get (ctx, TableName, trx);
		return seq.delete(true);
	}	

	/**
	 * 	Get Table Sequence
	 *	@param ctx context
	 *	@param tableName table name
	 *	@return Sequence
	 */
	public static MSequence get (Ctx ctx, String tableName, Trx trx)
	{
		final String sql = "SELECT * FROM AD_Sequence "
							 + "WHERE UPPER(Name)=?"
							+ " AND IsTableID='Y'";
		MSequence retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setString (1, tableName.toUpperCase());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MSequence (ctx, rs, trx);
			if (rs.next())
				s_log.log(Level.SEVERE, "More then one sequence for " + tableName);
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	


	

	/**************************************************************************
	 *	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Sequence_ID id
	 *	@param trx transaction
	 */
	public MSequence (Ctx ctx, int AD_Sequence_ID, Trx trx)
	{
		super(ctx, AD_Sequence_ID, trx);
		if (AD_Sequence_ID == 0)
		{
			//	setName (null);
			setIsTableID(false);
			setStartNo (INIT_NO);
			setCurrentNext (INIT_NO);
			setCurrentNextSys (INIT_SYS_NO);
			setIncrementNo (1);
			setIsAutoSequence (true);
			setIsAudited(false);
			setStartNewYear(false);
		}
	}	

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MSequence (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	

	/**
	 * 	New Document Sequence Constructor
	 *	@param ctx context
	 *	@param AD_Client_ID owner
	 *	@param tableName name
	 *	@param trx transaction
	 */
	public MSequence (Ctx ctx, int AD_Client_ID, String tableName, Trx trx)
	{
		this (ctx, 0, trx);
		setClientOrg(AD_Client_ID, 0);			//	Client Ownership
		setName(PREFIX_DOCSEQ + tableName);
		setDescription("DocumentNo/Value for Table " + tableName);
		setIsGapless(true);
	}	

	/**
	 * 	New Document Sequence Constructor
	 *	@param ctx context
	 *	@param AD_Client_ID owner
	 *	@param sequenceName name
	 *	@param StartNo start
	 *	@param trx p_trx
	 */
	public MSequence (Ctx ctx, int AD_Client_ID, String sequenceName, int StartNo, Trx trx)
	{
		this (ctx, 0, trx);
		setClientOrg(AD_Client_ID, 0);			//	Client Ownership
		setName(sequenceName);
		setDescription(sequenceName);
		setStartNo(StartNo);
		setCurrentNext(StartNo);
		setCurrentNextSys(StartNo/10);
	}	



	/**
	 * 	Validate Table Sequence Values
	 *	@return true if updated
	 */
	public boolean validateTableIDValue()
	{
		if (!isTableID())
			return false;
		final String tableName = getName();
		final int AD_Column_ID = QueryUtil.getSQLValue(null, 
										"SELECT MAX(c.AD_Column_ID) "
										+ "FROM AD_Table t"
										+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
										+ "WHERE t.TableName=?"
										+ " AND c.ColumnName=?", 
										new Object[]{tableName, tableName+"_ID"});
		if (AD_Column_ID <= 0)
			return false;

		MSystem system = MSystem.get(getCtx());
		int IDRangeEnd = 0;
		if (system.getIDRangeEnd() != null)
			IDRangeEnd = system.getIDRangeEnd().intValue();
		boolean change = false;
		String info = null;

		//	Current Next
		String sql = "SELECT MAX(" + tableName + "_ID) FROM " + tableName;
		if (IDRangeEnd > 0)
			sql += " WHERE " + tableName + "_ID < " + IDRangeEnd;
		int maxTableID = QueryUtil.getSQLValue(null, sql);
		if (maxTableID < INIT_NO)
			maxTableID = INIT_NO - 1;
		maxTableID++;		//	Next
		if (getCurrentNext() < maxTableID)
		{
			setCurrentNext(maxTableID);
			info = "CurrentNext=" + maxTableID;
			change = true;
		}

		//	Get Max System_ID used in Table
		sql = "SELECT MAX(" + tableName + "_ID) FROM " + tableName
		+ " WHERE " + tableName + "_ID < " + INIT_NO;
		int maxTableSysID = QueryUtil.getSQLValue(null, sql);
		if (maxTableSysID <= 0)
			maxTableSysID = INIT_SYS_NO - 1;
		maxTableSysID++;	//	Next
		if (getCurrentNextSys() < maxTableSysID)
		{
			setCurrentNextSys(maxTableSysID);
			if (info == null)
				info = "CurrentNextSys=" + maxTableSysID;
			else
				info += " - CurrentNextSys=" + maxTableSysID;
			change = true;
		}
		if (info != null)
			log.config(getName() + " - " + info);
		return change;
	}	


	/**************************************************************************
	 *	Test
	 *	@param args ignored
	 */
	static public void main (String[] args)
	{
		org.compiere.Compiere.startup(true);
		CLogMgt.setLevel(Level.SEVERE);
		CLogMgt.setLoggerLevel(Level.SEVERE, null);
		s_list = new Vector<Integer>(1000);

		/**	Lock Test Start **
		Trx trx = "test";
		System.out.println(DB.getDocumentNo(115, trx));
		System.out.println(DB.getDocumentNo(116, trx));
		System.out.println(DB.getDocumentNo(117, trx));
		System.out.println(DB.getDocumentNo(118, trx));
		System.out.println(DB.getDocumentNo(118, trx));
		System.out.println(DB.getDocumentNo(117, trx));

		/**	Lock Test **
		trx = "test1";
		System.out.println(DB.getDocumentNo(115, trx));	//	hangs here as supposed
		System.out.println(DB.getDocumentNo(116, trx));
		System.out.println(DB.getDocumentNo(117, trx));
		System.out.println(DB.getDocumentNo(118, trx));
		/** **/

		/** Time Test	*/
		long time = System.currentTimeMillis();
		Thread[] threads = new Thread[10];
		for (int i = 0; i < 10; i++)
		{
			Runnable r = new GetIDs(i);
			threads[i] = new Thread(r);
			threads[i].start();
		}
		for (int i = 0; i < 10; i++)
		{
			try
			{
				threads[i].join();
			}
			catch (InterruptedException e)
			{
			}
		}
		time = System.currentTimeMillis() - time;

		System.out.println("-------------------------------------------");
		System.out.println("Size=" + s_list.size() + " (should be 1000)");
		Integer[] ia = new Integer[s_list.size()];
		s_list.toArray(ia);
		Arrays.sort(ia);
		Integer last = null;
		int duplicates = 0;
		for (Integer element : ia) {
			if (last != null)
			{
				if (last.compareTo(element) == 0)
				{
					//	System.out.println(i + ": " + ia[i]);
					duplicates++;
				}
			}
			last = element;
		}
		System.out.println("-------------------------------------------");
		System.out.println("Size=" + s_list.size() + " (should be 1000)");
		System.out.println("Duplicates=" + duplicates);
		System.out.println("Time (ms)=" + time + " - " + (float)time/s_list.size() + " each" );
		System.out.println("-------------------------------------------");



		int AD_Client_ID = 0;
		int C_DocType_ID = 115;	//	GL
		String TableName = "C_Invoice";
		Trx trx = Trx.get("x");
		Trx p_trx = trx;

		System.out.println ("none " + getNextID (0, "Test"));
		System.out.println ("----------------------------------------------");
		System.out.println ("trx1 " + getNextID (0, "Test"));
		System.out.println ("trx2 " + getNextID (0, "Test"));
		//	p_trx.rollback();
		System.out.println ("trx3 " + getNextID (0, "Test"));
		//	p_trx.commit();
		System.out.println ("trx4 " + getNextID (0, "Test"));
		//	p_trx.rollback();
		//	p_trx.close();
		System.out.println ("----------------------------------------------");
		System.out.println ("none " + getNextID (0, "Test"));
		System.out.println ("==============================================");

		p_trx = trx;
		System.out.println ("none " + getDocumentNo(AD_Client_ID, TableName, null));
		System.out.println ("----------------------------------------------");
		System.out.println ("trx1 " + getDocumentNo(AD_Client_ID, TableName, trx));
		System.out.println ("trx2 " + getDocumentNo(AD_Client_ID, TableName, trx));
		p_trx.rollback();
		System.out.println ("trx3 " + getDocumentNo(AD_Client_ID, TableName, trx));
		p_trx.commit();
		System.out.println ("trx4 " + getDocumentNo(AD_Client_ID, TableName, trx));
		p_trx.rollback();
		p_trx.close();
		System.out.println ("----------------------------------------------");
		System.out.println ("none " + getDocumentNo(AD_Client_ID, TableName, null));
		System.out.println ("==============================================");


		p_trx = trx;
		System.out.println ("none " + getDocumentNo(C_DocType_ID, null));
		System.out.println ("----------------------------------------------");
		System.out.println ("trx1 " + getDocumentNo(C_DocType_ID, trx));
		System.out.println ("trx2 " + getDocumentNo(C_DocType_ID, trx));
		p_trx.rollback();
		System.out.println ("trx3 " + getDocumentNo(C_DocType_ID, trx));
		p_trx.commit();
		System.out.println ("trx4 " + getDocumentNo(C_DocType_ID, trx));
		p_trx.rollback();
		p_trx.close();
		System.out.println ("----------------------------------------------");
		System.out.println ("none " + getDocumentNo(C_DocType_ID, null));
		System.out.println ("==============================================");
	}	

	/** Test		*/
	static Vector<Integer> s_list = null;

	/**
	 * 	Test Sequence - Get IDs
	 *
	 *  @author Jorg Janke
	 *  @version $Id: MSequence.java 9203 2010-08-26 21:30:55Z kvora $
	 */
	public static class GetIDs implements Runnable
	{
		/**
		 * 	Get IDs
		 *	@param i
		 */
		public GetIDs (int i)
		{
			m_i = i;
		}
		/** Instance	*/
		private int 	m_i;
		private int		m_errors = 0;
		private int		m_no = 0;

		private	Trx		m_trx = null;

		/**
		 * 	Run
		 */
		public void run()
		{
			System.out.println("Run #" + m_i + " - started");
			if (TEST_TRX)
			{
				m_trx = Trx.get ("Number" + m_i);
				m_trx.getConnection();
			}


			for (int i = 0; i < 100; i++)
			{
				try
				{
					int no = DB.getNextID(0, "Test", m_trx);
					if (m_trx != null)
						m_trx.commit();
					//
					s_list.add(Integer.valueOf(no));
					m_no++;
					//	System.out.println("#" + m_i + ": " + no);
				}
				catch (Exception e)	{
					m_errors++;
					System.err.println("#" + m_i + "-" + m_errors
							+ ": " + e.toString());
				}
			}
			if (m_trx != null)
				m_trx.close();
			System.out.println("Run #" + m_i
					+ " - complete - Errors=" + m_errors
					+ " - Created=" + m_no);
		}	//	run

		/**
		 * 	Info
		 *	@return info
		 */
		@Override
		public String toString()
		{
			return "GetID #" + m_i + " - Size=" + s_list.size();
		}
	}	

}	//	MSequence
