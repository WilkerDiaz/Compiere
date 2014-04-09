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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import org.compiere.util.CCache;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

/**
 * 	Currency Model.
 *
 *  @author Jorg Janke
 *  @version $Id: MCurrency.java 8848 2010-05-27 23:39:31Z rthng $
 */
public class MCurrency extends X_C_Currency
{
    /** Logger for class MCurrency */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCurrency.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Currency Constructor
	 *	@param ctx context
	 *	@param C_Currency_ID id
	 *	@param trx transaction
	 */
	public MCurrency (Ctx ctx, int C_Currency_ID, Trx trx)
	{
		super (ctx, C_Currency_ID, trx);
		if (C_Currency_ID == 0)
		{
			setIsEMUMember (false);
			setIsEuro (false);
			setStdPrecision (2);
			setCostingPrecision (4);
		}
	}	//	MCurrency

	/**
	 * 	Currency Constructor
	 *	@param ctx context
	 *	@param ISO_Code ISO
	 *	@param Description Name
	 *	@param CurSymbol symbol
	 *	@param StdPrecision prec
	 *	@param CostingPrecision prec
	 *	@param trx transaction
	 */
	public MCurrency (Ctx ctx, String ISO_Code,
			String Description, String CurSymbol, int StdPrecision, int CostingPrecision, Trx trx)
	{
		super(ctx, 0, trx);
		setISO_Code(ISO_Code);
		setDescription(Description);
		setCurSymbol(CurSymbol);
		setStdPrecision (StdPrecision);
		setCostingPrecision (CostingPrecision);
		setIsEMUMember (false);
		setIsEuro (false);
	}	//	MCurrency

	/**
	 *	Currency Constructor
	 * 	@param ctx context
	 *  @param rs ResultSet
	 *	@param trx transaction
	 */
	public MCurrency (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCurrency

	/**	Store System Currencies			**/
	private static final CCache<Integer,MCurrency> s_currencies = new CCache<Integer,MCurrency>("C_Currency", 50);

	/**
	 * 	Get Currency
	 *	@param ctx Context
	 *	@param C_Currency_ID currency
	 *	@return ISO Code
	 */
	public static MCurrency get (Ctx ctx, int C_Currency_ID)
	{
		//	Try Cache
		Integer key = Integer.valueOf(C_Currency_ID);
		MCurrency retValue = s_currencies.get(ctx, key);
		if (retValue != null)
			return retValue;

		//	Create it
		retValue = new MCurrency(ctx, C_Currency_ID, null);
		//	Save in System
		if (retValue.getAD_Client_ID() == 0)
			s_currencies.put(key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Currency Iso Code.
	 *	@param ctx Context
	 *	@param C_Currency_ID currency
	 *	@return ISO Code
	 */
	public static String getISO_Code (Ctx ctx, int C_Currency_ID)
	{
		String contextKey = "C_Currency_" + C_Currency_ID;
		String retValue = ctx.getContext(contextKey);
		if (retValue != null && retValue.length() > 0)
			return retValue;

		//	Create it
		MCurrency c = get(ctx, C_Currency_ID);
		retValue = c.getISO_Code();
		ctx.setContext(contextKey, retValue);
		return retValue;
	}	//	getISO

	/**
	 * 	Get Standard Precision.
	 *	@param ctx Context
	 *	@param C_Currency_ID currency
	 *	@return Standard Precision
	 */
	public static int getStdPrecision (Ctx ctx, int C_Currency_ID)
	{
		MCurrency c = get(ctx, C_Currency_ID);
		return c.getStdPrecision();
	}	//	getStdPrecision

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		return "MCurrency[" + getC_Currency_ID()
		+ "-" + getISO_Code() + "-" + getCurSymbol()
		+ "," + getDescription()
		+ ",Precision=" + getStdPrecision() + "/" + getCostingPrecision() + "]";
	}	//	toString

	public static void clearCurrencyCache() {
		if(DB.isOracle()) {
			Connection c = DB.createConnection(true, Connection.TRANSACTION_READ_COMMITTED);
			CallableStatement proc = null;
			try {
				proc = c.prepareCall("{ call clearcurrencycache()}");
				proc.execute();
			}
			catch(Exception e) {
				log.severe("Cannot clear currency cache"+e.getMessage());
			}
			finally {
				if(proc != null) {
					try {
						proc.close();
					}catch(Exception e)
					{}
				}

			}
		}}
}	//	MCurrency
