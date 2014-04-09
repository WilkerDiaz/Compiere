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
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.model.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              GL_Journal (224)
 *  Document Types:     GLJ
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_GLJournal.java 7514 2009-04-20 21:57:40Z freyes $
 */
public class Doc_GLJournal extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trx p_trx
	 */
	public Doc_GLJournal (MAcctSchema[] ass, ResultSet rs, Trx trx)
	{
		super(ass, MJournal.class, rs, null, trx);
	}	//	Foc_GL_Journal

	/** Posting Type				*/
	private String			m_PostingType = null;
	private int				m_C_AcctSchema_ID = 0;
	
	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	@Override
	public String loadDocumentDetails()
	{
		MJournal journal = (MJournal)getPO();
		m_PostingType = journal.getPostingType();
		m_C_AcctSchema_ID = journal.getC_AcctSchema_ID();
		setDateAcct(journal.getDateAcct());
			
		//	Contained Objects
		p_lines = loadLines(journal);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails


	/**
	 *	Load Invoice Line
	 *	@param journal journal
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MJournal journal)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MJournalLine[] lines = journal.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MJournalLine line = lines[i];
			DocLine docLine = new DocLine (line, this); 
			//  --  Source Amounts
			docLine.setAmount (line.getAmtSourceDr(), line.getAmtSourceCr());
			//  --  Converted Amounts
			docLine.setConvertedAmt (m_C_AcctSchema_ID, line.getAmtAcctDr(), line.getAmtAcctCr());
			//  --  Account
			MAccount account = line.getAccount();
			docLine.setAccount (account);
			//  -- Quantity
			docLine.setQty(line.getQty(), false);
			// -- Date
			docLine.setDateAcct(journal.getDateAcct());
			docLine.setC_Period_ID(journal.getC_Period_ID());
			//	--	Organization of Line was set to Org of Account
			list.add(docLine);
		}
		//	Return Array
		int size = list.size();
		DocLine[] dls = new DocLine[size];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	@Override
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.add(p_lines[i].getAmtSource());
			sb.append("+").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  GLJ.
	 *  (only for the accounting scheme, it was created)
	 *  <pre>
	 *      account     DR          CR
	 *  </pre>
	 *  @param as acct schema
	 *  @return Fact
	 */
	@Override
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//	Other Acct Schema
		if (as.getC_AcctSchema_ID() != m_C_AcctSchema_ID)
			return facts;
		
		//  create Fact Header
		Fact fact = new Fact (this, as, m_PostingType);

		//  GLJ
		if (getDocumentType().equals(MDocBaseType.DOCBASETYPE_GLJournal))
		{
			//  account     DR      CR
			for (int i = 0; i < p_lines.length; i++)
			{
				if (p_lines[i].getC_AcctSchema_ID () == as.getC_AcctSchema_ID ())
				{
					fact.createLine (p_lines[i],
									p_lines[i].getAccount (),
									getC_Currency_ID(),
									p_lines[i].getAmtSourceDr (),
									p_lines[i].getAmtSourceCr ());
				}
			}	//	for all lines
		}
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			fact = null;
		}
		//
		facts.add(fact);
		return facts;
	}   //  createFact

}   //  Doc_GLJournal
