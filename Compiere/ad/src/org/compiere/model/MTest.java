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

import java.math.*;
import java.sql.*;

import org.compiere.*;
import org.compiere.util.*;

/**
 *	Test Model
 *
 *  @author Jorg Janke
 *  @version $Id: MTest.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MTest extends X_Test
{
    /** Logger for class MTest */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTest.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param Test_ID
	 *	@param trx transaction
	 */
	public MTest(Ctx ctx, int Test_ID, Trx trx)
	{
		super (ctx, Test_ID, trx);
	}	//	MTest

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTest(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTest

	/**
	 * 	Test Object Constructor
	 *	@param ctx context
	 *	@param testString test string
	 *	@param testNo test no
	 */
	public MTest (Ctx ctx, String testString, int testNo)
	{
		super(ctx, 0, null);
		testString = testString + "_" + testNo;
		setName(testString);
		setDescription(testString + " " + testString + " " + testString);
		setHelp (getDescription() + " - " + getDescription());
		setT_Date(new Timestamp (System.currentTimeMillis()));
		setT_DateTime(new Timestamp (System.currentTimeMillis()));
		setT_Integer(testNo);
		setT_Amount(new BigDecimal(testNo));
		setT_Number(Env.ONE.divide(new BigDecimal(testNo), BigDecimal.ROUND_HALF_UP));
		//
		setC_Currency_ID(100);		//	USD
		setC_Location_ID(109);		//	Monroe
		setC_UOM_ID(100);			//	Each
	//	setC_BPartner_ID(C_BPartner_ID);
	//	setC_Payment_ID(C_Payment_ID);
	//	setM_Locator_ID(M_Locator_ID);
	//	setM_Product_ID(M_Product_ID);
	}	//	MTest


	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	@Override
	protected boolean beforeDelete ()
	{
		log.info("***");
		return true;
	}

	/**
	 * 	After Delete
	 *	@param success
	 *	@return success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		log.info("*** Success=" + success);
		return success;
	}

	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		log.info("New=" + newRecord + " ***");
		return true;
	}

	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		log.info("New=" + newRecord + ", Seccess=" + success + " ***");
		return success;
	}	//	afterSave


	/*************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup(true);
		Ctx ctx = Env.getCtx();

		MTest t1 = new MTest (ctx, 0, null);
		t1.setName("Test1");
		t1.setDescription("123456789 123456789 123456789 ");
		t1.setT_Integer(333);

		System.out.println("->" + t1.getCharacterData() + "<-");
		t1.save();





		/** Test CLOB	*/
		t1.setCharacterData("Long Text JJ");
		t1.save();
		int Test_ID = t1.getTest_ID();
		//
		MTest t2 = new MTest (Env.getCtx(), Test_ID, null);
		System.out.println("->" + t2.getCharacterData() + "<-");

		t2.delete(true);


		/**	Volume Test
		for (int i = 1; i < 20000; i++)
		{
			new MTest (ctx, "test", i).save();
		}
		/** */
	}	//	main

}	//	MTest
