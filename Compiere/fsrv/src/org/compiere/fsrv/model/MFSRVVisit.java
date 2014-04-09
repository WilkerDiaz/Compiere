package org.compiere.fsrv.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.api.UICallout;
import org.compiere.model.MDocType;
import org.compiere.process.DocAction;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.compiere.util.Env.QueryParams;
import org.compiere.vos.DocActionConstants;

public class MFSRVVisit extends X_FSRV_Visit implements DocAction {
    /** Logger for class MFSRVVisit */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MFSRVVisit.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 *	@param ctx context
	 *	@param FSRV_Visit_ID id
	 *	@param trxName transaction
	 */
	public MFSRVVisit(Ctx ctx, int FSRV_Visit_ID, Trx trx) 
	{
		super(ctx, FSRV_Visit_ID, trx);
	}	//	MVisit

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MFSRVVisit(Ctx ctx, ResultSet rs, Trx trx) 
	{
		super(ctx, rs, trx);
	}	//	MVisit
	
	/**
	 * 	Before Save "Trigger"
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	protected boolean beforeSave(boolean newRecord) 
	{
		//	Invoice at least 5 minutes
		if (newRecord && getMinutes() < 5)
			setMinutes(5);
			
		return true;
	}	//	beforeSave

	@Override
	public boolean approveIt() {
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}

	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String completeIt() {
		setProcessed(true);
		setDocAction(DOCACTION_Closed);
		return DocActionConstants.STATUS_Completed;
	}

	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return BigDecimal.valueOf(getMinutes());
	}

	@Override
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String prepareIt() {
		if (!DOCACTION_Completed.equals(getDocAction()))
			setDocAction(DOCACTION_Completed);

		return DocActionConstants.STATUS_InProgress;
	}

	/** Process Message */
	private String m_processMsg = null;
	/** Just Prepared Flag */
	private final boolean m_justPrepared = false;

	@Override
	public boolean reActivateIt() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * 	Set Business Partner - Callout
	 *	@param oldC_BPartner_ID old BP
	 *	@param newC_BPartner_ID new BP
	 *	@param windowNo window no
	 */
	@UICallout public void setC_BPartner_ID (String oldC_BPartner_ID,
			String newC_BPartner_ID, int windowNo) throws Exception
		{
		if ((newC_BPartner_ID == null) || (newC_BPartner_ID.length() == 0))
			return;
		int C_BPartner_ID = Integer.parseInt(newC_BPartner_ID);
		if (C_BPartner_ID == 0)
			return;


		String sql = "SELECT cc.AD_User_ID "
			+ "FROM AD_User cc "
			+ "WHERE cc.C_BPartner_ID=? AND cc.IsActive='Y'";		//	#1

		int AD_User_ID = DB.getSQLValue((Trx)null, sql, C_BPartner_ID);
		if(AD_User_ID > 0)
			setAD_User_ID(AD_User_ID);		
	}	//	setC_BPartner_ID
	
	public String getDocBaseType()
	{
		MDocType docType = new MDocType(getCtx(), getC_DocType_ID(), get_Trx());
		if(docType != null)
			return docType.getDocBaseType();
		
		return "";
	}

	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}

	@Override
	public Timestamp getDocumentDate() {
		// TODO Auto-generated method stub
		return getCreated();
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams(
				"SELECT AD_Org_ID FROM FSRV_Visit WHERE FSRV_Visit_ID=? ",
				new Object[] { getFSRV_Visit_ID()});
	}

}
