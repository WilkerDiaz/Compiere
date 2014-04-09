package org.compiere.db; 
import java.sql.*;

import junit.framework.*;

import org.compiere.util.*;

public class DBTestCase extends TestCase {
	/**
	DROP TABLE X_Test;
	CREATE TABLE X_Test
	(
	    Text1   NVARCHAR2(2000) NULL,
	    Text2   VARCHAR2(2000)  NULL
	);
	 **/

	public DBTestCase(String t, int cons_len) {
		super(t);
		init(cons_len);
	}
	public static int count = 0;
	public void testInsert() {
		count++;
		PreparedStatement pstmt = null;
		try
		{
			String myString = count+"";
			//Connection conn2 = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			/** **/
			pstmt = DB.prepareStatement
			("INSERT INTO X_Test(Text1, Text2) values(?,?)", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
			
			//PreparedStatement pstmt = DB.prepareStatement
			//("INSERT INTO X_Test(Text1, Text2) values(?,?)", null);

			pstmt.setString(1, myString); // NVARCHAR2 column
			pstmt.setString(2, myString); // VARCHAR2 column
			assertTrue(pstmt.executeUpdate() == 1);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(pstmt);
		}
	}
	private Connection[] cons = null;
	private int cons_len = 0;
	//private static int idx = 0;
	
	public void init(int cons_len) {
		this.cons_len = cons_len;
		cons = new Connection[cons_len];
		for(int i=0; i<cons_len; i++) { 
			cons[i] = DB.createConnection(true, Connection.TRANSACTION_READ_COMMITTED);
			System.out.println("Allocate connection"+ i+":"+cons[i]);
		}

	}
	private int idx=0;
	public void testInsertWithConnections() {
		idx++;
		count++;
		Connection c = cons[idx%cons_len];
		PreparedStatement pstmt = null;
		try
		{
			String myString = count+"";
			//System.out.println("Connection is:"+c);
			//Connection conn2 = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			/** **/
			pstmt = 
			c.prepareStatement
			("INSERT INTO X_Test(Text1, Text2) values(?,?)", 
			ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			
			//PreparedStatement pstmt = DB.prepareStatement
			//("INSERT INTO X_Test(Text1, Text2) values(?,?)", null);

			pstmt.setString(1, myString); // NVARCHAR2 column
			pstmt.setString(2, myString); // VARCHAR2 column
			assertTrue(pstmt.executeUpdate() == 1);			
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(pstmt);
		}
	}
	public void testInsertWithNativeCache() {
		count++;

		PreparedStatement pstmt = null;
		try
		{
			String myString = count+"";
			//System.out.println("Connection is:"+c);
			Connection c = DB.createConnection(true, Connection.TRANSACTION_READ_COMMITTED);
			/** **/
			pstmt = 
			c.prepareStatement
			("INSERT INTO X_Test(Text1, Text2) values(?,?)", 
			ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			System.out.println("Con is:"+c);
			//PreparedStatement pstmt = DB.prepareStatement
			//("INSERT INTO X_Test(Text1, Text2) values(?,?)", null);

			pstmt.setString(1, myString); // NVARCHAR2 column
			pstmt.setString(2, myString); // VARCHAR2 column
			assertTrue(pstmt.executeUpdate() == 1);			
			c.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(pstmt);
		}

	}

	public void testSelect() {
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement
			("SELECT count(*) from X_Test", (Trx) null);
//			assertTrue(pstmt.executeUpdate() == 1);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(pstmt);
		}
	}
	
	public void testBoth() {
		//long start = new Date().getTime();
		testInsert();
		testSelect();
		//long end = new Date().getTime();
		//System.out.println("start:"+start+" end:"+end);
	}
	public void testNop() {
	}
	public void testTransactionAtomicity() {
		/*
		PreparedStatement pstmt = DB.prepareStatement
		("INSERT INTO X_Test(Text1, Text2) values(?,?)", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
		*/
	}
}
