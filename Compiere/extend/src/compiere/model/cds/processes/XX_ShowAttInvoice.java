package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import org.compiere.apps.Attachment;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import compiere.model.cds.MInOut;

public class XX_ShowAttInvoice extends SvrProcess {

	private HashMap<Integer,Integer>	m_Attachments = null;
	private final int orderTable = 259;
	
	@Override
	protected String doIt() throws Exception {

		MInOut inOut = new MInOut( getCtx(), getRecord_ID(), null);
		
		//Consultamos el Attachment de la O/C
		new Attachment(null, 0, getAD_AttachmentID(inOut.getC_Order_ID()), orderTable, inOut.getC_Order_ID(), null);
		
		return "";
	}

	@Override
	protected void prepare() {
	}

	/**
	 *	Get Attachment_ID for current record.
	 *	@return ID or 0, if not found
	 */
	public int getAD_AttachmentID(Integer orderID)
	{
		if (m_Attachments == null)
			loadAttachments();
		if (m_Attachments.isEmpty())
			return 0;
		//
		Integer key = orderID;
		Integer value = m_Attachments.get(key);
		if (value == null)
			return 0;
		else
			return value.intValue();
	}	//	getAttachmentID
	
	/**************************************************************************
	 *	Load Attachments for this table
	 */
	public void loadAttachments()
	{

		String SQL = "SELECT AD_Attachment_ID, Record_ID FROM AD_Attachment "
			+ "WHERE AD_Table_ID=?";
		try
		{
			if (m_Attachments == null)
				m_Attachments = new HashMap<Integer,Integer>();
			else
				m_Attachments.clear();
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setInt(1, orderTable);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				Integer key = Integer.valueOf(rs.getInt(2));
				Integer value = Integer.valueOf(rs.getInt(1));
				m_Attachments.put(key, value);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "loadAttachments", e);
		}
		log.config("#" + m_Attachments.size());
	}	//	loadAttachment
	
}
