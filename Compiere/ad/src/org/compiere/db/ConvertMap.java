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
package org.compiere.db;

import java.util.*;

/**
 *  Database Syntax Conversion Map.
 *
 *
 *  @version    $Id: ConvertMap.java 8414 2010-02-09 19:42:16Z freyes $
 */
public class ConvertMap
{
	/**
	 *  Return Map for PostgreSQL
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap<String, String> getPGMap()
	{
		if (s_pg.size() == 0)
			initPG();
		return s_pg;
	}   //  getPGMap

	/**
	 *  Return Map for DB/2
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap<String, String> getDB2Map()
	{
		if (s_db2.size() == 0)
			initDB2();
		return s_db2;
	}   //  getDB2Map


	/**
	 *  Return Map for MS SQL Server
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap<String, String> getMSMap()
	{
		if (s_ms.size() == 0)
			initSQLServer();
		return s_ms;
	}   //  getDB2Map
	/** Tree Map for PostgreSQL			*/
	private static TreeMap<String,String>  s_pg = new TreeMap<String,String>();
	/** Tree Map for DB/2			     */
	private static TreeMap<String,String>  s_db2 = new TreeMap<String,String>();
	/** Tree Map for SQLServer			     */
	private static TreeMap<String,String>  s_ms = new TreeMap<String,String>();

	/**
	 *  PostgreSQL Init
	 */
	static private void initPG()
	{
		//      Oracle Pattern                  Replacement

		s_pg.put("\\b::bpchar\\b",              ""); //jz gabage from EDB

		//  Data Types
		//s_pg.put("\\bNUMBER\\b",                "NUMERIC");
		//s_pg.put("\\bDATE\\b",                  "TIMESTAMP");
		s_pg.put("\\bNVARCHAR\\b",                 "VARCHAR");
		s_pg.put("\\bNVARCHAR2\\b",                 "VARCHAR");
		s_pg.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_pg.put("\\bbpchar\\b",              "CHAR");
		s_pg.put("\\bNCHAR\\b",                 "CHAR");
	//	s_pg.put("\\bBLOB\\b",                  "OID");         //  BLOB not directly supported
	//	s_pg.put("\\bCLOB\\b",                  "TEXT");        //  CLOB not directly supported
		s_pg.put("\\bMODIFY\\b",                "ALTER");		//	MODIFY COLUMN	

		//  Storage
		//s_pg.put("\\bCACHE\\b",                 "");
		s_pg.put("\\bUSING INDEX\\b",           "");
		s_pg.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_pg.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		//
		s_pg.put("\\bBITMAP INDEX\\b",          "INDEX");

		//  Functions
		s_pg.put("\\bSysDate\\b",               "CURRENT_TIMESTAMP");
		s_pg.put("\\bSYSDATE\\b",               "CURRENT_TIMESTAMP");   //  alternative: NOW()
		s_pg.put("\\bNVL\\b",                   "COALESCE");
		s_pg.put("\\bTO_DATE\\b",               "TO_TIMESTAMP");
		//s_pg.put("\\bTO_NCHAR(AD_User_ID)\\b",                 "TO_CHAR(AD_User_ID,'9999999')"); //special case for index
		s_pg.put("\\bTO_NCHAR\\b",                 "TO_CHAR");
		//
		s_pg.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "RAISE NOTICE");

		//  Temporary
		s_pg.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_pg.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_pg.put("\\bON COMMIT PRESERVE ROWS\\b",   "");

		//  DROP TABLE x CASCADE CONSTRAINTS
		s_pg.put("\\bCASCADE CONSTRAINTS\\b",   "");

		//  Select
		//jz it exists, otherwise create one.
		/* s_pg.put("\\sFROM\\s+DUAL\\b",          "");
		s_db2.put(",NULL\\b",				",'' ");  //jz there are a few statements not with DB.NULL() yet
		s_db2.put(", NULL\\b",				",'' ");
		s_db2.put(",NULL,",				",'',");
		s_db2.put("SELECT NULL,",				"SELECT '',");	
		*/	

		//  Statements
		s_pg.put("\\bELSIF\\b",                 "ELSE IF");
		s_pg.put("\\bEND CASE\\b",                 "END");

		//  Sequences
		s_pg.put("\\bSTART WITH\\b",            "START");
		s_pg.put("\\bINCREMENT BY\\b",          "INCREMENT");

	}   //  initPG

	/**
	 *  DB/2 Init
	 */
	static private void initDB2()
	{
		//      Oracle Pattern                  Replacement

		s_db2.put("\\bTO_NCHAR\\b",                 "CHAR");

		//  Data Types
		/* 
		s_db2.put("DECIMAL(10,0)",                "INTEGER"); //jz numeric int
		s_db2.put("DECIMAL(22,0)",                "BIGINT"); //jz numeric int
		s_db2.put("\\bNUMBER(10,0)\\b",                "INTEGER"); //jz numeric int
		s_db2.put("\\bNUMBER(22,0)\\b",                "BIGINT"); //jz numeric int
		s_db2.put("\\bNUMBER(10)\\b",                "INTEGER"); //jz numeric int
		s_db2.put("\\bNUMBER(22)\\b",                "BIGINT"); //jz numeric int
		*/
		s_db2.put("\\bNUMBER\\b",                "DECIMAL(31,6)"); //jz numeric de
		s_db2.put("\\bDATE\\b",                  "TIMESTAMP");
		s_db2.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_db2.put("\\bNVARCHAR2\\b",             "VARCHAR");
		s_db2.put("\\bNCHAR\\b",                 "CHAR");
		//jz s_db2.put("\\bBLOB\\b",                  "OID");                 //  BLOB not directly supported
		// s_db2.put("\\bCLOB\\b",                  "TEXT");                //  CLOB not directly supported

		//  Storage
		s_db2.put("\\bCACHE\\b",                 "");
		s_db2.put("\\bUSING INDEX\\b",           "");
		s_db2.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_db2.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		//
		s_db2.put("\\bBITMAP INDEX\\b",          "INDEX");
		
		//  Functions
		//jz it needs () matching in pattern     s_db2.put("currencyBase(invoiceOpen",               "currencyBaseD(invoiceOpen");   //  alternative: NOW()
		s_db2.put("\\bSysDate\\b",               "CURRENT_TIMESTAMP");
		s_db2.put("\\bSYSDATE\\b",               "CURRENT_TIMESTAMP");   //  alternative: NOW()
		s_db2.put("\\bNVL\\b",                   "COALESCE");
		//jz    s_db2.put("\\bTO_DATE\\b",               "TO_TIMESTAMP");
		s_db2.put("\\bTO_DATE\\b",               "TIMESTAMP");
		//
		s_db2.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "RAISE NOTICE");

		//  Temporary
		s_db2.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_db2.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_db2.put("\\bON COMMIT PRESERVE ROWS\\b",   "");


		//  DROP TABLE x CASCADE CONSTRAINTS
		s_db2.put("\\bCASCADE CONSTRAINTS\\b",   "");

		//  Select
		//jz add nullif -> '' since derby is not stable with null value
		/* jz () problem in matching
		s_db2.put(",NULL\\b",				",nullif('a','a') ");
		s_db2.put(", NULL\\b",				",nullif('a','a') ");
		s_db2.put(",NULL,",				",nullif('a','a'),");
		s_db2.put("SELECT NULL,",				"SELECT nullif('a','a'),");		
		*/
		s_db2.put(",NULL\\b",				",'' ");
		s_db2.put(", NULL\\b",				",'' ");
		s_db2.put(",NULL,",				",'',");
		s_db2.put("SELECT NULL,",				"SELECT '',");		
		 
		s_db2.put("\\sFROM\\s+DUAL\\b",          " FROM SYSIBM.SYSDUMMY1 ");
		

		//  Statements
		s_db2.put("\\bELSIF\\b",                 "ELSE IF");

		//  Sequences
		s_db2.put("\\bSTART WITH\\b",            "START");
		s_db2.put("\\bINCREMENT BY\\b",          "INCREMENT");

	}   //  initPostgreSQL
	

	/**
	 *  MS SQL Server Init
	 */
	static private void initSQLServer()
	{
		//      Oracle Pattern                  Replacement

		//s_ms.put("\\bTO_NCHAR\\b",                 "CHAR");

		//  Data Types
		 
		//s_ms.put("DECIMAL(10,0)",                "INTEGER"); //jz numeric int
		//s_ms.put("DECIMAL(22,0)",                "BIGINT"); //jz numeric int
		//s_ms.put("\\bNUMBER(10,0)\\b",                "NUMERIC(10,0)"); //jz numeric int
		//s_ms.put("\\bNUMBER(22,0)\\b",                "NUMERIC(22,0)"); //jz numeric int
		//s_ms.put("\\bNUMBER(10)\\b",                "NUMERIC(10)"); //jz numeric int
		//s_ms.put("\\bNUMBER(22)\\b",                "NUMERIC(22)"); //jz numeric int
		
		//s_ms.put("\\bNUMBER\\(",                "NUMERIC("); //jz numeric de
		s_ms.put("\\bNUMBER\\b",                "NUMERIC"); //jz numeric de
		//s_ms.put("\\bNUMBER,",                "NUMERIC(32,6),"); //jz numeric de
		s_ms.put("\\bDATE\\b",                  "DATETIME");
		//s_ms.put("\\bDATE,",                  "DATETIME,");
		s_ms.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_ms.put("\\bNVARCHAR2\\b",             "NVARCHAR");
		//s_ms.put("\\bNVARCHAR2,",             "NVARCHAR,");
		//s_ms.put("\\bNVARCHAR2\\(",             "NVARCHAR(");
		s_ms.put("\\bBLOB\\b",                  "VARBINARY(MAX)");                 //  BLOB not directly supported
		s_ms.put("\\bCLOB\\b",                  "NTEXT");                 //  CLOB not directly supported

		//  Storage
		//s_ms.put("\\bCACHE\\b",                 "");
		s_ms.put("\\bUSING INDEX\\b",           "");
		s_ms.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_ms.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		s_ms.put("\\bBITMAP INDEX\\b",          "INDEX");
		
		//  Functions
		s_ms.put("\\bSysDate\\b",      "getdate()");
		//System.out.println("dummy ...........................");
		s_ms.put("\\bsysDate\\b",      "getdate()");

		/*
		s_ms.put("\\bacctBalance\\b",      "dbo.acctBalance");
		s_ms.put("\\baddDays\\b",      "dbo.addDays");
		s_ms.put("\\bbomPriceLimit\\b",      "dbo.bomPriceLimit");
		s_ms.put("\\bbomPriceList\\b",      "dbo.bomPriceList");
		s_ms.put("\\bbomPriceStd\\b",      "dbo.bomPriceStd");
		s_ms.put("\\bbomQtyAvailable\\b",      "dbo.bomQtyAvailable");
		s_ms.put("\\bbomQtyOnHand\\b",      "dbo.bomQtyOnHand");
		s_ms.put("\\bbomQtyOrdered\\b",      "dbo.bomQtyOrdered");
		s_ms.put("\\bbomQtyReserved\\b",      "dbo.bomQtyReserved");
		s_ms.put("\\bbpartnerRemitLocation\\b",      "dbo.bpartnerRemitLocation");
		s_ms.put("\\bcharAt\\b",      "dbo.charAt");
		s_ms.put("\\bcurrencyBase\\b",      "dbo.currencyBase");
		s_ms.put("\\bcurrencyConvert\\b",      "dbo.currencyConvert");
		s_ms.put("\\bcurrencyRate\\(\\b",      "dbo.currencyRate(");
		s_ms.put("\\bcurrencyRound\\b",      "dbo.currencyRound");
		s_ms.put("\\bdaysBetween\\b",      "dbo.daysBetween");
		s_ms.put("\\bfirstOf\\b",      "dbo.firstOf");
		s_ms.put("\\bgetChars\\b",      "dbo.getChars");
		s_ms.put("\\bgetDaysBetween\\b",      "dbo.getDaysBetween");
		s_ms.put("\\binvoiceDiscount\\b",      "dbo.invoiceDiscount");
		s_ms.put("\\binvoiceOpen\\b",      "dbo.invoiceOpen");
		s_ms.put("\\binvoicePaid\\b",      "dbo.invoicePaid");
		s_ms.put("\\bpaymentAllocated\\b",      "dbo.paymentAllocated");
		s_ms.put("\\bpaymentAvailable\\b",      "dbo.paymentAvailable");
		s_ms.put("\\bpaymentTermDiscount\\b",      "dbo.paymentTermDiscount");
		s_ms.put("\\bpaymentTermDueDate\\b",      "dbo.paymentTermDueDate");
		s_ms.put("\\bpaymentTermDueDays\\b",      "dbo.paymentTermDueDays");
		s_ms.put("\\bproductAttribute\\b",      "dbo.productAttribute");
		*/
		s_ms.put("\\bacctBalance\\b",      "[compiere].ACCTBALANCE");
		s_ms.put("\\baddDays\\b",      "[compiere].ADDDAYS");
		s_ms.put("\\bbomPriceLimit\\b",      "[compiere].BOMPRICELIMIT");
		s_ms.put("\\bbomPriceList\\b",      "[compiere].BOMPRICELIST");
		s_ms.put("\\bbomPriceStd\\b",      "[compiere].BOMPRICESTD");
		s_ms.put("\\bbomQtyAvailable\\b",      "[compiere].BOMQTYAVAILABLE");
		s_ms.put("\\bbomQtyOnHand\\b",      "[compiere].BOMQTYONHAND");
		s_ms.put("\\bbomQtyOrdered\\b",      "[compiere].BOMQTYORDERED");
		s_ms.put("\\bbomQtyReserved\\b",      "[compiere].BOMQTYRESERVED");
		s_ms.put("\\bbpartnerRemitLocation\\b",      "[compiere].BPARTNERREMITLOCATION");
		s_ms.put("\\bcharAt\\b",      "[compiere].CHARAT");
		s_ms.put("\\bcurrencyBase\\b",      "[compiere].CURRENCYBASE");
		s_ms.put("\\bcurrencyConvert\\b",      "[compiere].CURRENCYCONVERT");
		s_ms.put("\\bcurrencyRate\\(\\b",      "[compiere].CURRENCYRATE(");
		s_ms.put("\\bcurrencyRound\\b",      "[compiere].CURRENCYROUND");
		s_ms.put("\\bdaysBetween\\b",      "[compiere].DAYSBETWEEN");
		s_ms.put("\\bfirstOf\\b",      "[compiere].FIRSTOF");
		s_ms.put("\\bgetChars\\b",      "[compiere].GETCHARS");
		s_ms.put("\\bgetDaysBetween\\b",      "[compiere].GETDAYSBETWEEN");
		s_ms.put("\\binvoiceDiscount\\b",      "[compiere].INVOICEDISCOUNT");
		s_ms.put("\\binvoiceOpen\\b",      "[compiere].INVOICEOPEN");
		s_ms.put("\\binvoicePaid\\b",      "[compiere].INVOICEPAID");
		s_ms.put("\\bpaymentAllocated\\b",      "[compiere].PAYMENTALLOCATED");
		s_ms.put("\\bpaymentAvailable\\b",      "[compiere].PAYMENTAVAILABLE");
		s_ms.put("\\bpaymentTermDiscount\\b",      "[compiere].PAYMENTTERMDISCOUNT");
		s_ms.put("\\bpaymentTermDueDate\\b",      "[compiere].PAYMENTTERMDUEDATE");
		s_ms.put("\\bpaymentTermDueDays\\b",      "[compiere].PAYMENTTERMDUEDAYS");
		s_ms.put("\\bproductAttribute\\b",      "[compiere].PRODUCTATTRIBUTE");

		s_ms.put("\\bTRUNC_DATE\\b",      "[compiere].TRUNC_DATE");
		s_ms.put("\\bTRUNC2_DATE\\b",      "[compiere].TRUNC2_DATE");
		s_ms.put("\\bDUMP\\b",      "[compiere].STRDUMP");
		s_ms.put("\\bTO_NCHAR\\b",                 "STR");
		s_ms.put("\\bTO_CHAR\\b",                 "STR");
		s_ms.put("\\bNVL\\b",                   "COALESCE");

		s_ms.put("\\bTO_CHAR\\b",      "[compiere].TO_CHAR");
		s_ms.put("\\bTO_DATE\\b",      "[compiere].TO_DATE");
		s_ms.put("\\bTRIM\\b",      "[compiere].TRIM");

		
		
		//jz    s_ms.put("\\bTO_DATE\\b",               "TO_TIMESTAMP");
		//s_ms.put("\\bTO_DATE\\b",               "TIMESTAMP");
		//
		s_ms.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "PRINT");

		//  Temporary
		s_ms.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_ms.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_ms.put("\\bON COMMIT PRESERVE ROWS\\b",   "");


		//  DROP TABLE x CASCADE CONSTRAINTS
		s_ms.put("\\bCASCADE CONSTRAINTS\\b",   "");

		s_ms.put("\\sFROM\\s+DUAL\\b",          "");
		

		//  Statements
		s_ms.put("\\bELSIF\\b",                 "ELSE IF");
		s_ms.put("\\bEND CASE\\b",                 "END");
		//s_ms.put("\\bLINENO\\b",                 "[LINENO]");
		s_ms.put("\\bLineNo\\b",                 "[LineNo]");
		
		//Operators
		//s_ms.put("\\b||\\b",                 "+");

	}   //  initMS

	
}   //  ConvertMap
