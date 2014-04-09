/**
 * 
 */
package org.compiere.apps;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	Data Migration Editor.
 * 	Add Records to existing Migrations
 *	@author Jorg Janke
 */
public class DataMigrationEditor extends CDialog
{
	/** */
    private static final long serialVersionUID = 1538301472101621246L;

	/**
	 *	Data Migration Editor
	 *	@param c window APanel 
	 */
	public DataMigrationEditor(Container c) 
	{
		super(Env.getFrame(c), "DataMigration", true);
		String AD_Message = init(c);
		info.setText(Msg.getMsg(Env.getCtx(), AD_Message));
		AEnv.positionCenterWindow(Env.getFrame(c), this);
		setVisible(true);
	}	//	DataMigrationEditor
	
	/**	Logger			*/
	private static CLogger 	log = CLogger.getCLogger(DataMigrationEditor.class);

	private boolean			m_ok = false;
	private int 			m_AD_Client_ID = 0;
	private int 			m_AD_Table_ID = 0;
	private int 			m_Record_ID = 0;
	
	
	CPanel mainPanel = new CPanel(new BorderLayout());	
	CTextArea info = new CTextArea();
	CComboBox migrationSel = null;
	ConfirmPanel cPanel = new ConfirmPanel(true);

	
	/**
	 * 	Initialize
	 *	@param c parent
	 *	@return error message or null
	 */
	private String init(Container c)
	{
		//	Graph Layout
		this.getContentPane().add(mainPanel);
		info.setPreferredSize(new Dimension(400, 50));
		info.setReadWrite(false);
		mainPanel.add(info, BorderLayout.CENTER);
		mainPanel.add(cPanel, BorderLayout.SOUTH);
		cPanel.addActionListener(this);
		ImageIcon ii = new ImageIcon(org.compiere.Compiere.class.getResource("images/DataMigration16.png"));
		setIconImage(ii.getImage());
		//
		APanel p = null;
		if (c instanceof APanel)
			p = (APanel)c;
		if (p == null)
			return "NoParent";
		//	Check Table & Record
		m_AD_Table_ID = p.getCurrentTab().getAD_Table_ID();
		if (m_AD_Table_ID == 0)
			return "DataMigrationNoTable";
		m_Record_ID = p.getCurrentTab().getRecord_ID();
		if (m_Record_ID == 0)
			return "DataMigrationNoRecord";
		MTable table = MTable.get(Env.getCtx(), m_AD_Table_ID);
		String tableName = table.get_TableName();
		if (tableName.indexOf("DataMigration") != -1
			|| tableName.indexOf("Log") != -1)
			return "DataMigrationInvalidTable";
		//
		m_AD_Client_ID = p.getCurrentTab().getAD_Client_ID();
		String msg = "AD_Client_ID=" + m_AD_Client_ID 
			+ ", AD_Table_ID=" + m_AD_Table_ID + ", Record_ID=" + m_Record_ID;
		log.info(msg);
		info.setToolTipText(msg);
		
		//	Search for
		String sql = "SELECT * FROM AD_DataMigration WHERE IsActive='Y'"
			+ " AND (AD_Client_ID=? OR (DataMigrationType='A' AND AD_ClientInclude_ID=?))"; 
		Vector<KeyNamePair> list = new Vector<KeyNamePair>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, Env.getCtx().getAD_Client_ID());
			pstmt.setInt(2, Env.getCtx().getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("AD_DataMigration_ID");
				String name = rs.getString("Name");
				KeyNamePair pp = new KeyNamePair(id, name);
				list.add(pp);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		if (list.size() == 0)
			return "DataMigrationNone";
		
		//	Add to Panel
		migrationSel = new CComboBox(list);
		CLabel migrationLabel = new CLabel(Msg.getElement(Env.getCtx(), "AD_DataMigration_ID"), migrationSel);
		migrationLabel.setToolTipText(msg);
		CPanel mig = new CPanel(new ALayout());
		mig.add(migrationLabel, new ALayoutConstraint(0,0));
		mig.add(migrationSel, null);
		info.addText("DataMigrationInfo");
		mainPanel.add(mig, BorderLayout.NORTH);
		
		m_ok = true;
		String unique[] = table.getUniqueIDColumns();
		if (unique == null || unique.length == 0)
			return "DataMigrationInfoUID";
		return "DataMigrationInfo";
	}	//	 init
	
	/**
	 * 	Save Record
	 */
	private boolean save()
	{
		KeyNamePair pp = (KeyNamePair)migrationSel.getSelectedItem();
		if (pp == null)
			return false;
		
		MDataMigration dm = new MDataMigration(Env.getCtx(), pp.getKey(), null);
		if (dm.get_ID() != pp.getKey())
			return false;

		//	Check AD_Client
		if (dm.getAD_Client_ID() != m_AD_Client_ID)
		{
			if (dm.getAD_ClientInclude_ID() != m_AD_Client_ID)
			{
				String oldMsg = info.getText();
				String newText = "\nTenant ID incompatibe must be " + dm.getAD_Client_ID();
				if (dm.getAD_ClientInclude_ID() != dm.getAD_Client_ID())
					newText += " or " + dm.getAD_ClientInclude_ID(); 
				info.setText(oldMsg + newText);
				return false;
			}
		}
		
		MDataMigrationEntry entry = new MDataMigrationEntry(dm, 
				m_AD_Table_ID, m_Record_ID);
		return entry.save();
	}	//	save

	/**
	 * 	Action Performed
	 * 	@param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//	OK
		if (e.getActionCommand().equals(ConfirmPanel.A_OK) && m_ok)
		{
			if (!save())
				return;
		}
		//	Cancel
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			;
		dispose();
	}	//	actionPerformed
	
}	//	DataMigrationEditor
