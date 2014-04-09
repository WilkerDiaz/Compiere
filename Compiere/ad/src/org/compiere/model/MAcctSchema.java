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
import java.util.logging.*;

import org.compiere.common.CompiereStateException;
import org.compiere.util.*;

/**
 *  Accounting Schema Model (base)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MAcctSchema.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MAcctSchema extends X_C_AcctSchema
{
    /** Logger for class MAcctSchema */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAcctSchema.class);
	/** */
	private static final long serialVersionUID = 1L;


	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param C_AcctSchema_ID schema id
	 *  @return Accounting schema
	 */
	public static MAcctSchema get (Ctx ctx, int C_AcctSchema_ID)
	{
		return get(ctx, C_AcctSchema_ID, null);
	}	//	get

	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param C_AcctSchema_ID schema id
	 *  @param trx optional p_trx
	 *  @return Accounting schema
	 */
	public static MAcctSchema get (Ctx ctx, int C_AcctSchema_ID, Trx trx)
	{
		//  Check Cache
		Integer key = Integer.valueOf(C_AcctSchema_ID);
		MAcctSchema retValue = s_cache.get(ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MAcctSchema (ctx, C_AcctSchema_ID, trx);
		if (trx == null)
			s_cache.put(key, retValue);
		return retValue;
	}	//	get
	
	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param AD_Client_ID client or 0 for all
	 *  @return Array of AcctSchema of Client
	 */
	public static MAcctSchema[] getClientAcctSchema (Ctx ctx, int AD_Client_ID)
	{
		return getClientAcctSchema(ctx, AD_Client_ID, null);
	}	//	getClientAcctSchema
	
	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param AD_Client_ID client or 0 for all
	 *  @param trx optional p_trx 
	 *  @return Array of AcctSchema of Client
	 */
	public static MAcctSchema[] getClientAcctSchema (Ctx ctx, int AD_Client_ID, Trx trx)
	{
		//  Check Cache
		Integer key = Integer.valueOf(AD_Client_ID);
		if (s_schema.containsKey(key))
			return s_schema.get(ctx, key);

		//  Create New
		ArrayList<MAcctSchema> list = new ArrayList<MAcctSchema>();
		MClientInfo info = MClientInfo.get(ctx, AD_Client_ID, trx); 
		MAcctSchema as = MAcctSchema.get (ctx, info.getC_AcctSchema1_ID(), trx);
		if (as.get_ID() != 0)
			list.add(as);
		
		//	Other
		String sql = "SELECT C_AcctSchema_ID FROM C_AcctSchema acs "
			+ "WHERE IsActive='Y'"
			+ " AND EXISTS (SELECT * FROM C_AcctSchema_GL gl WHERE acs.C_AcctSchema_ID=gl.C_AcctSchema_ID)"
			+ " AND EXISTS (SELECT * FROM C_AcctSchema_Default d WHERE acs.C_AcctSchema_ID=d.C_AcctSchema_ID)"; 
		if (AD_Client_ID != 0)
			sql += " AND AD_Client_ID=?";
		sql += " ORDER BY C_AcctSchema_ID";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			if (AD_Client_ID != 0)
				pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt(1);
				if (id != info.getC_AcctSchema1_ID())	//	already in list
				{
					as = MAcctSchema.get (ctx, id, trx);
					if (as.get_ID() != 0)
						list.add(as);
				}
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//  Save
		MAcctSchema[] retValue = new MAcctSchema [list.size()];
		list.toArray(retValue);
		if (trx == null)
			s_schema.put(key, retValue);
		return retValue;
	}   //  getClientAcctSchema

	/** Cache of Client AcctSchema Arrays		**/
	private static final CCache<Integer,MAcctSchema[]> s_schema = new CCache<Integer,MAcctSchema[]>("AD_ClientInfo", 3);	//  3 clients
	/**	Cache of AcctSchemas 					**/
	private static final CCache<Integer,MAcctSchema> s_cache = new CCache<Integer,MAcctSchema>("C_AcctSchema", 3);	//  3 accounting schemas

	/**	Logger			*/
	private static final CLogger s_log = CLogger.getCLogger(MAcctSchema.class);	
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_AcctSchema_ID id
	 *	@param trx transaction
	 */
	public MAcctSchema (Ctx ctx, int C_AcctSchema_ID, Trx trx)
	{
		super (ctx, C_AcctSchema_ID, trx);
		if (C_AcctSchema_ID == 0)
		{
		//	setC_Currency_ID (0);
		//	setName (null);
			setAutoPeriodControl (true);
			setPeriod_OpenFuture(50);
			setPeriod_OpenHistory(50);
			setCostingMethod (COSTINGMETHOD_StandardCosting);
			setCostingLevel(X_C_AcctSchema.COSTINGLEVEL_Tenant);
			setIsAdjustCOGS(false);
			setGAAP (GAAP_InternationalGAAP);
			setHasAlias (true);
			setHasCombination (false);
			setIsAccrual (true);	// Y
			setCommitmentType(COMMITMENTTYPE_None);
			setIsDiscountCorrectsTax (false);
			setTaxCorrectionType(TAXCORRECTIONTYPE_None);
			setIsTradeDiscountPosted (false);
			setIsPostServices(false);
			setIsExplicitCostAdjustment(false);
			setSeparator ("-");	// -
		}
	}	//	MAcctSchema
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAcctSchema (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAcctSchema

	/**
	 * 	Parent Constructor
	 *	@param client client
	 *	@param C_Currency_ID currency
	 */
	public MAcctSchema (MClient client, int C_Currency_ID)
	{
		this (client.getCtx(), 0, client.get_Trx());
		setClientOrg(client);
		setC_Currency_ID (C_Currency_ID);
		MCurrency currency = MCurrency.get(client.getCtx(), C_Currency_ID);
		setName (client.getName() + " " + getGAAP() + "/" + get_ColumnCount() + " " + currency.getISO_Code());
	}	//	MAcctSchema


	/** Element List       */
	private MAcctSchemaElement[]	m_elements = null;
	/** GL Info				*/
	private MAcctSchemaGL			m_gl = null;
	/** Default Info		*/
	private MAcctSchemaDefault		m_default = null;
	
	private MAccount				m_SuspenseError_Acct = null;
	private MAccount				m_CurrencyBalancing_Acct = null;
	private MAccount				m_DueTo_Acct = null;
	private MAccount				m_DueFrom_Acct = null;
	/** Accounting Currency Precision	*/
	private int						m_stdPrecision = -1;
	/** Costing Currency Precision		*/
	private int						m_costPrecision = -1;
	
	/** Only Post Org					*/
	private MOrg					m_onlyOrg = null;
	/** Only Post Org Children			*/
	private Integer[] 				m_onlyOrgs = null; 

	/**************************************************************************
	 *  AcctSchema Elements
	 *  @return ArrayList of AcctSchemaElement
	 */
	public MAcctSchemaElement[] getAcctSchemaElements()
	{
		if (m_elements == null)
			m_elements = MAcctSchemaElement.getAcctSchemaElements(this);
		return m_elements;
	}   //  getAcctSchemaElements

	/**
	 *  Get AcctSchema Element
	 *  @param elementType segment type - AcctSchemaElement.ELEMENTTYPE_
	 *  @return AcctSchemaElement
	 */
	public MAcctSchemaElement getAcctSchemaElement (String elementType)
	{
		if (m_elements == null)
			getAcctSchemaElements();
		for (MAcctSchemaElement ase : m_elements) {
			if (ase.getElementType().equals(elementType))
				return ase;
		}
		return null;
	}   //  getAcctSchemaElement

	/**
	 *  Has AcctSchema Element
	 *  @param segmentType segment type - AcctSchemaElement.SEGMENT_
	 *  @return true if schema has segment type
	 */
	public boolean isAcctSchemaElement (String segmentType)
	{
		return getAcctSchemaElement(segmentType) != null;
	}   //  isAcctSchemaElement

	/**
	 * 	Get AcctSchema GL info
	 *	@return GL info
	 */
	public MAcctSchemaGL getAcctSchemaGL()
	{
		if (m_gl == null)
			m_gl = MAcctSchemaGL.get(getCtx(), getC_AcctSchema_ID());
		if (m_gl == null)
			throw new CompiereStateException("No GL Definition for C_AcctSchema_ID=" + getC_AcctSchema_ID());
		return m_gl;
	}	//	getAcctSchemaGL
	
	/**
	 * 	Get AcctSchema Defaults
	 *	@return defaults
	 */
	public MAcctSchemaDefault getAcctSchemaDefault()
	{
		if (m_default == null)
			m_default = MAcctSchemaDefault.get(getCtx(), getC_AcctSchema_ID());
		if (m_default == null)
			throw new CompiereStateException("No Default Definition for C_AcctSchema_ID=" + getC_AcctSchema_ID());
		return m_default;
	}	//	getAcctSchemaDefault

	/**
	 * 	Get accounting Schema Element ID
	 *	@return id or 0
	 */
	public int getC_Element_ID()
	{
		MAcctSchemaElement ase = getAcctSchemaElement (X_C_AcctSchema_Element.ELEMENTTYPE_Account);
		if (ase == null)
			return 0;
		return ase.getC_Element_ID();
	}	//	getC_Element_ID
	
	
	/**
	 *	String representation
	 *  @return String rep
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("AcctSchema[");
			sb.append(get_ID()).append("-").append(getName())
				.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Is Suspense Balancing active
	 *	@return suspense balancing
	 */
	public boolean isSuspenseBalancing()
	{
		if (m_gl == null)
			getAcctSchemaGL();
		return m_gl.isUseSuspenseBalancing() && m_gl.getSuspenseBalancing_Acct() != 0;
	}	//	isSuspenseBalancing

	/**
	 *	Get Suspense Error Account
	 *  @return suspense error account
	 */
	public MAccount getSuspenseBalancing_Acct()
	{
		if (m_SuspenseError_Acct != null)
			return m_SuspenseError_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getSuspenseBalancing_Acct();
		m_SuspenseError_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_SuspenseError_Acct;
	}	//	getSuspenseBalancing_Acct

	/**
	 * 	Is Currency Balancing active
	 *	@return suspense balancing
	 */
	public boolean isCurrencyBalancing()
	{
		if (m_gl == null)
			getAcctSchemaGL();
		return m_gl.isUseCurrencyBalancing();
	}	//	isSuspenseBalancing

	/**
	 *	Get Currency Balancing Account
	 *  @return currency balancing account
	 */
	public MAccount getCurrencyBalancing_Acct()
	{
		if (m_CurrencyBalancing_Acct != null)
			return m_CurrencyBalancing_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getCurrencyBalancing_Acct();
		m_CurrencyBalancing_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_CurrencyBalancing_Acct;
	}	//	getCurrencyBalancing_Acct


	/**
	 *	Get Due To Account for Segment
	 *  @param segment ignored
	 *  @return Account
	 */
	public MAccount getDueTo_Acct(String segment)
	{
		if (m_DueTo_Acct != null)
			return m_DueTo_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getIntercompanyDueTo_Acct();
		m_DueTo_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_DueTo_Acct;
	}	//	getDueTo_Acct

	/**
	 *	Get Due From Account for Segment
	 *  @param segment ignored
	 *  @return Account
	 */
	public MAccount getDueFrom_Acct(String segment)
	{
		if (m_DueFrom_Acct != null)
			return m_DueFrom_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getIntercompanyDueFrom_Acct();
		m_DueFrom_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_DueFrom_Acct;
	}	//	getDueFrom_Acct

	/**
	 * 	Set Only Org Children
	 *	@param orgs
	 */
	public void setOnlyOrgs (Integer[] orgs)
	{
		m_onlyOrgs = orgs;
	}	//	setOnlyOrgs
	
	/**
	 * 	Set Only Org Childs
	 *	@return orgs
	 */
	public Integer[] getOnlyOrgs()
	{
		return m_onlyOrgs;
	}	//	getOnlyOrgs

	/**
	 * 	Skip creating postings for this Org.
	 * 	Requires setOnlyOrgs (MReportTree requires MTree in Basis)
	 *	@param AD_Org_ID
	 *	@return true if to skip
	 */
	public boolean isSkipOrg (int AD_Org_ID)
	{
		if (getAD_OrgOnly_ID() == 0)
			return false;
		//	Only specific Organization
		if (getAD_OrgOnly_ID() == AD_Org_ID)
			return false;
		if (m_onlyOrg == null)
			m_onlyOrg = MOrg.get(getCtx(), getAD_OrgOnly_ID());
		//	Not Summary Org - i.e. skip it
		if (!m_onlyOrg.isSummary())
			return true;
		//	Set from Doc.post
		//	MReportTree.getChildIDs(getCtx(), 0, MAcctSchemaElement.ELEMENTTYPE_Organization, getAD_OrgOnly_ID());
		if (m_onlyOrgs == null)
			return false;
		for (Integer element : m_onlyOrgs) {
			if (AD_Org_ID == element.intValue())
				return false;
		}
		return true;
	}	//	isSkipOrg
	
	/**
	 * 	Get Std Precision of accounting Currency
	 *	@return precision
	 */
	public int getStdPrecision()
	{
		if (m_stdPrecision < 0)
		{
			MCurrency cur = MCurrency.get(getCtx(), getC_Currency_ID());
			m_stdPrecision = cur.getStdPrecision();
			m_costPrecision = cur.getCostingPrecision();
		}
		return m_stdPrecision;
	}	//	getStdPrecision

	/**
	 * 	Get Costing Precision of accounting Currency
	 *	@return precision
	 */
	public int getCostingPrecision()
	{
		if (m_costPrecision < 0)
			getStdPrecision();
		return m_costPrecision;
	}	//	getCostingPrecision
	
	
	/**
	 * 	Check Costing Setup.
	 * 	Make sure that there is a Cost Type and Cost Element
	 */
	public void checkCosting()
	{
		log.info(toString());
		//	Create Cost Type
		if (getM_CostType_ID() == 0)
		{
			MCostType ct = new MCostType (getCtx(), 0, get_Trx());
			ct.setClientOrg(getAD_Client_ID(), 0);
			ct.setName(getName());
			ct.save();
			setM_CostType_ID(ct.getM_CostType_ID());
		}
		
		//	Create Cost Elements
		MCostElement.getMaterialCostElement(this, getCostingMethod());
		
		//	Default Costing Level
		if (getCostingLevel() == null)
			setCostingLevel(X_C_AcctSchema.COSTINGLEVEL_Tenant);
		if (getCostingMethod() == null)
			setCostingMethod (COSTINGMETHOD_StandardCosting);
		if (getGAAP() == null)
			setGAAP (GAAP_InternationalGAAP);
	}	//	checkCosting
	
	/**
	 * 	Is Client Costing Level (default)
	 *	@return true if Client
	 */
	public boolean isCostingLevelClient()
	{
		String s = getCostingLevel();
		if (s == null || X_C_AcctSchema.COSTINGLEVEL_Tenant.equals(s))
			return true;
		return false;
	}	//	isCostingLevelClient
	
	/**
	 * 	Is Org Costing Level
	 *	@return true if Org
	 */
	public boolean isCostingLevelOrg()
	{
		return COSTINGLEVEL_Organization.equals(getCostingLevel());
	}	//	isCostingLevelOrg
	
	/**
	 * 	Is Batch Costing Level
	 *	@return true if Batch
	 */
	public boolean isCostingLevelBatch()
	{
		return COSTINGLEVEL_BatchLot.equals(getCostingLevel());
	}	//	isCostingLevelBatch

	/**
	 * 	Create Commitment Accounting
	 *	@return true if creaet commitments
	 */
	public boolean isCreateCommitment()
	{
		String s = getCommitmentType();
		if (s == null)
			return false;
		return COMMITMENTTYPE_CommitmentOnly.equals(s)
			|| COMMITMENTTYPE_CommitmentReservation.equals(s);
	}	//	isCreateCommitment

	/**
	 * 	Create Commitment/Reservation Accounting
	 *	@return true if create reservations
	 */
	public boolean isCreateReservation()
	{
		String s = getCommitmentType();
		if (s == null)
			return false;
		return COMMITMENTTYPE_CommitmentReservation.equals(s);
	}	//	isCreateReservation

	/**
	 * 	Get Tax Correction Type
	 *	@return tax correction type
	 */
	@Override
	public String getTaxCorrectionType()
	{
		if (super.getTaxCorrectionType() == null)	//	created 07/23/06 2.5.3d
			setTaxCorrectionType(isDiscountCorrectsTax() 
				? TAXCORRECTIONTYPE_Write_OffAndDiscount : TAXCORRECTIONTYPE_None);
		return super.getTaxCorrectionType ();
	}	//	getTaxCorrectionType
	
	/**
	 * 	Tax Correction
	 *	@return true if not none
	 */
	public boolean isTaxCorrection()
	{
		return !getTaxCorrectionType().equals(TAXCORRECTIONTYPE_None);
	}	//	isTaxCorrection
	
	/**
	 * 	Tax Correction for Discount
	 *	@return true if tax is corrected for Discount 
	 */
	public boolean isTaxCorrectionDiscount()
	{
		return getTaxCorrectionType().equals(TAXCORRECTIONTYPE_DiscountOnly)
			|| getTaxCorrectionType().equals(TAXCORRECTIONTYPE_Write_OffAndDiscount);
	}	//	isTaxCorrectionDiscount

	/**
	 * 	Tax Correction for WriteOff
	 *	@return true if tax is corrected for WriteOff 
	 */
	public boolean isTaxCorrectionWriteOff()
	{
		return getTaxCorrectionType().equals(TAXCORRECTIONTYPE_Write_OffOnly)
			|| getTaxCorrectionType().equals(TAXCORRECTIONTYPE_Write_OffAndDiscount);
	}	//	isTaxCorrectionWriteOff

	
	/**
	 * 	Does the dateAcct fall in the range
	 * 	@param dateAcct
	 * 	@return true if falls within range	
	 */
	public boolean isAutoPeriodControlOpen(Timestamp dateAcct)
	{
		Timestamp today = new Timestamp (System.currentTimeMillis());
		Timestamp first = TimeUtil.addDays(today, - getPeriod_OpenHistory()); 
		Timestamp last = TimeUtil.addDays(today, getPeriod_OpenFuture());
		if (dateAcct.before(first))
		{
			log.warning (dateAcct + " before first day - " + first);
			return false;
		}
		if (dateAcct.after(last))
		{
			log.warning (dateAcct + " after last day - " + first);
			return false;
		}
		return true;
	}	//	isAutoPeriodControl
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		MClientInfo info = MClientInfo.get(getCtx(), getAD_Client_ID());
		if (!isActive()
			&& info != null
			&& info.getC_AcctSchema1_ID() == getC_AcctSchema_ID())
		{
			log.saveError("Error", Msg.translate(getCtx(), "PrimaryAcctSchema"));
			return false;
		}

		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		if (super.getTaxCorrectionType() == null)
			setTaxCorrectionType(isDiscountCorrectsTax() 
				? TAXCORRECTIONTYPE_Write_OffAndDiscount : TAXCORRECTIONTYPE_None);
		checkCosting();

		//	Check Primary
		if (getAD_OrgOnly_ID() != 0 
			&& info != null
			&& info.getC_AcctSchema1_ID() == getC_AcctSchema_ID())
			setAD_OrgOnly_ID(0);
		
		return true;
	}	//	beforeSave

	@Override
	protected boolean afterDelete(boolean success) {
		return super.afterDelete(success);
	}

	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		MCurrency.clearCurrencyCache();
		return super.afterSave(newRecord, success);
	}

}	//	MAcctSchema
