package org.compiere.db;
import org.compiere.Compiere;

import com.clarkware.junitperf.*;
import junit.framework.Test;

public class DBThroughputUnderLoadTest {

	static int maxUsers = 50;
	static long maxElapsedTime = 500000;

	public static void main(String[] args) {
		Compiere.startup(false);

		int numCons = 2;
		//Test testCase = new DBTestCase("testInsertWithConnections", numCons);
		Test testCase = new DBTestCase("testInsertWithNativeCache", numCons);
		Test loadTest = new LoadTest(testCase, maxUsers);
		Test timedTest = new TimedTest(loadTest, maxElapsedTime);

		junit.textui.TestRunner.run(timedTest);

		/*testCase = new DBTestCase("testInsert", numCons);
		loadTest = new LoadTest(testCase, maxUsers);
		timedTest = new TimedTest(loadTest, maxElapsedTime);
		junit.textui.TestRunner.run(timedTest);
*/
		
/*		testCase = new DBTestCase("testInsert", 1);
		loadTest = new LoadTest(testCase, maxUsers);
		timedTest = new TimedTest(loadTest, maxElapsedTime);
		junit.textui.TestRunner.run(timedTest);

		testCase = new DBTestCase("testSelect");
		loadTest = new LoadTest(testCase, maxUsers);
		timedTest = new TimedTest(loadTest, maxElapsedTime);
		junit.textui.TestRunner.run(timedTest);

		testCase = new DBTestCase("testInsert");
		loadTest = new LoadTest(testCase, maxUsers);
		timedTest = new TimedTest(loadTest, maxElapsedTime);
		junit.textui.TestRunner.run(timedTest);

		testCase = new DBTestCase("testNop");
		loadTest = new LoadTest(testCase, maxUsers);
		timedTest = new TimedTest(loadTest, maxElapsedTime);
		junit.textui.TestRunner.run(timedTest);
*/
	}
}
