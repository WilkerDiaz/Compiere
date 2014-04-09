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
package org.compiere.apps.search;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.common.FieldType;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.common.constants.EnvConstants;
import org.compiere.framework.Query;
import org.compiere.grid.ed.VEditor;
import org.compiere.grid.ed.VEditorFactory;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.GridField;
import org.compiere.model.MRole;
import org.compiere.model.MUserQuery;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CDialog;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTabbedPane;
import org.compiere.swing.CTable;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Null;
import org.compiere.util.Trx;
import org.compiere.util.ValueNamePair;

/**
 *  Find/Search Records.
 *	Based on AD_Find for persistency, query is build to restrict info
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Find.java,v 1.3 2006/07/30 00:51:27 jjanke Exp $
 */
public final class Find extends CDialog
		implements ActionListener, ChangeListener, ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Find Constructor
	 *	@param owner Frame Dialog Owner
	 *  @param targetWindowNo WindowNo of target window
	 *	@param title 
	 *	@param AD_Table_ID
	 *	@param tableName
	 *	@param whereExtended
	 *	@param findFields
	 *  @param minRecords number of minimum records
	 */
	public Find (Frame owner, int targetWindowNo, String title, int AD_Tab_ID,
		int AD_Table_ID, String tableName, String whereExtended,
		GridField[] findFields, int minRecords)
	{
		super(owner, Msg.getMsg(Env.getCtx(), "Find") + ": " + title, true);
		log.info(title);
		//
		m_targetWindowNo = targetWindowNo;
		m_AD_Tab_ID = AD_Tab_ID;
		m_AD_Table_ID = AD_Table_ID;
		m_tableName = tableName;
		
		//BECO ORDEN DE COMPRA BUSCA CON ISSOTRX = 'N'
		if((owner.getName().contains("AWindow_181") && m_AD_Table_ID == 259) || (owner.getName().contains("AWindow_183") && m_AD_Table_ID == 318)){
			whereExtended = "isSOTRX='N'";
		}
		
		m_whereExtended = whereExtended;
		m_findFields = findFields;
		//
		m_query = new Query (tableName);
		m_query.addRestriction(whereExtended);
		//	Required for Column Validation
		m_ctx.setContext(m_targetWindowNo, "Find_Table_ID", m_AD_Table_ID);
		//  Context for Advanced Search Grid is WINDOW_FIND
		m_ctx.setContext(EnvConstants.WINDOW_FIND, "Find_Table_ID", m_AD_Table_ID);
		//
		try
		{
			jbInit();
			initFind();
			if (m_total < minRecords)
			{
				dispose();
				return;
			}
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Find", e);
		}
		//
		this.getRootPane().setDefaultButton(confirmPanelS.getOKButton());
		AEnv.showCenterWindow(owner, this);
	}	//	Find

	/** Target Window No            */
	private int				m_targetWindowNo;
	/** Context						*/
	private Ctx				m_ctx = Env.getCtx();
	/** The Tab						*/
	private int				m_AD_Tab_ID;
	/**	Table ID					*/
	private int				m_AD_Table_ID;
	/** Table Name					*/
	private String			m_tableName;
	/** Where						*/
	private String			m_whereExtended;
	/** Search Fields          		*/
	private GridField[]		m_findFields;
	/** Button Fields				*/
	private ArrayList<Integer> m_buttonFields = new ArrayList<Integer>();
	/** Resulting query             */
	private Query			m_query = null;
	/**	History Days (0=all)		*/
	private int 			m_days = 0;
	/**	History Created Flag		*/
	private boolean			m_created = true;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Find.class);
	
	/** Number of records			*/
	private int				m_total;
	private PreparedStatement	m_pstmt;
	//
	private	boolean			hasValue = false;
	private boolean			hasDocNo = false;
	private	boolean			hasName = false;
	private	boolean			hasDescription = false;
	/**	Line in Simple Content		*/
	private int				m_sLine = 6;
	
	/**	List of VEditors			*/
	private ArrayList<VEditor>			m_sEditors = new ArrayList<VEditor>();
	/** Target Fields with AD_Column_ID as key  */
	private Hashtable<Integer,GridField>	m_targetFields = new Hashtable<Integer,GridField>();

	/**	For Grid Controller			*/
	public static final int		TABNO = 99;
	/** Length of Fields on first tab	*/
	public static final int		FIELDLENGTH = 20;
	
	private CTabbedPane tabbedPane = new CTabbedPane();
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	private StatusBar statusBar = new StatusBar();
	//
	private ConfirmPanel confirmPanelS = new ConfirmPanel(true);
	private CPanel scontentPanel = new CPanel(new GridBagLayout());
	private CPanel simplePanel = new CPanel(new BorderLayout());
	private CLabel valueLabel = new CLabel();
	private CLabel nameLabel = new CLabel();
	private CLabel descriptionLabel = new CLabel();
	private CTextField valueField = new CTextField();
	private CTextField nameField = new CTextField();
	private CTextField descriptionField = new CTextField();
	private CLabel docNoLabel = new CLabel();
	private CTextField docNoField = new CTextField();
	//
	private CPanel  historyPanel = new CPanel();
	private CButton bShowAll = new CButton();
	private CButton bShowMonth = new CButton();
	private CButton bShowWeek = new CButton();
	private CButton bShowDay = new CButton();
	private CButton bShowYear = new CButton();
	private CCheckBox cbCreated = new CCheckBox(Msg.getMsg(m_ctx, "Created"), true);
	private CComboBox comboQueriesS = new CComboBox();
	//	Advanced
	private CPanel advancedPanel = new CPanel(new BorderLayout());
	private ConfirmPanel confirmPanelA = new ConfirmPanel(true, true, false, false, false, false, true);
	
	private CPanel advancedSavedQueries = new CPanel(new GridBagLayout());
	private CComboBox comboQueriesA = new CComboBox();
	private CButton bDeleteQuery = CButton.getSmall("Delete");
	
	private JScrollPane advancedScrollPane = new JScrollPane();
	private CTable advancedTable = new CTable();
	private Vector<AdvancedRow> advancedData = new Vector<AdvancedRow>();
	private CPanel advancedEntry = new CPanel (new GridBagLayout());

	/** Index ColumnName = 0		*/
	static final int		INDEX_COLUMNNAME = 0;
	/** Index Operator = 1			*/
	static final int		INDEX_OPERATOR = 1;
	/** Index Value = 2				*/
	static final int		INDEX_VALUE = 2;
	/** Index Value2 = 3			*/
	static final int		INDEX_VALUE2 = 3;

	private CComboBox 	columns = null;
	private CComboBox 	operators = null;
	private FindValueEditor	valueEditor = null;
	private FindValueEditor	valueEditor2 = null;
	private CButton bDeleteRow = CButton.getSmall("Delete");
	private CButton bSaveRow = CButton.getSmall("Save");
	private CTextField savedQueryName = new CTextField(15);

	/**
	 *	Static Init.
	 *  <pre>
	 *  historyPanel
	 *  tabbedPane
	 *      simplePanel
	 *          scontentPanel
	 *          confirmPanelS
	 *      advancedPanel
	 *          saveSearch
	 *          GC
	 *          confirmPanelA
	 *  southPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		southPanel.setLayout(southLayout);
		//
		bShowAll.setText(Msg.getMsg(m_ctx, "All"));
		bShowAll.addActionListener(this);
		bShowYear.setText(Msg.getMsg(m_ctx, "Year"));
		bShowYear.addActionListener(this);
		bShowMonth.setText(Msg.getMsg(m_ctx, "Month"));
		bShowMonth.addActionListener(this);
		bShowWeek.setText(Msg.getMsg(m_ctx, "Week"));
		bShowWeek.addActionListener(this);
		bShowDay.setText(Msg.getMsg(m_ctx, "Day"));
		bShowDay.addActionListener(this);
		bShowDay.setDefaultCapable(true);
		this.getRootPane().setDefaultButton(bShowDay);
		cbCreated.setToolTipText ("If not selected: Last updated");

		historyPanel.setBorder(BorderFactory.createTitledBorder(Msg.getMsg(m_ctx, "VHistory", true)));
		historyPanel.add(bShowAll);
		historyPanel.add(bShowYear);
		historyPanel.add(bShowMonth);
		historyPanel.add(bShowWeek);
		historyPanel.add(bShowDay);
		historyPanel.add(cbCreated);
		if (fillQuery())
			historyPanel.add(comboQueriesS);
		this.getContentPane().add(historyPanel, BorderLayout.CENTER);
		//
		valueLabel.setLabelFor(valueField);
		valueLabel.setText(Msg.translate(m_ctx,"Value"));
		nameLabel.setLabelFor(nameField);
		nameLabel.setText(Msg.translate(m_ctx,"Name"));
		descriptionLabel.setLabelFor(descriptionField);
		descriptionLabel.setText(Msg.translate(m_ctx,"Description"));
		valueField.setText("%");
		valueField.setColumns(FIELDLENGTH);
		nameField.setText("%");
		nameField.setColumns(FIELDLENGTH);
		descriptionField.setText("%");
		descriptionField.setColumns(FIELDLENGTH);
		scontentPanel.setToolTipText(Msg.getMsg(m_ctx,"FindTip"));
		docNoLabel.setLabelFor(docNoField);
		docNoLabel.setText(Msg.translate(m_ctx,"DocumentNo"));
		docNoField.setText("%");
		docNoField.setColumns(FIELDLENGTH);
		southPanel.add(statusBar, BorderLayout.SOUTH);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		//
		simplePanel.add(confirmPanelS, BorderLayout.SOUTH);
		simplePanel.add(scontentPanel, BorderLayout.CENTER);
		scontentPanel.add(valueLabel,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(nameLabel,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(descriptionLabel,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
		scontentPanel.add(valueField,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		scontentPanel.add(descriptionField,    new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		scontentPanel.add(docNoLabel,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(nameField,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		scontentPanel.add(docNoField,    new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		//
		Component spaceE = Box.createHorizontalStrut(8);
		Component spaceN = Box.createVerticalStrut(8);
		Component spaceW = Box.createHorizontalStrut(8);
		Component spaceS = Box.createVerticalStrut(8);
		scontentPanel.add(spaceE,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceN,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceW,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceS,  new GridBagConstraints(2, 15, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		//
		tabbedPane.add(simplePanel, Msg.getMsg(m_ctx,"Find"));
		
		//	Advanced
		CLabel lcomboQueriesA = new CLabel(Msg.getMsg(m_ctx, "GetSavedQuery"));
		CLabel lsavedQueryName = new CLabel(Msg.getMsg(m_ctx, "SaveQuery"));
		savedQueryName.setReadWrite(false);
		advancedSavedQueries.add(lcomboQueriesA, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		advancedSavedQueries.add(comboQueriesA, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		bDeleteQuery.setToolTipText(Msg.getMsg(m_ctx, "QueryDelete"));
		bDeleteQuery.addActionListener(this);
		bDeleteQuery.setEnabled(false);
		advancedSavedQueries.add(bDeleteQuery, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		advancedSavedQueries.add(lsavedQueryName, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 0, 0));
		advancedSavedQueries.add(savedQueryName, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		//
		CPanel advancedCenter = new CPanel(new BorderLayout()); 
		advancedScrollPane.setPreferredSize(new Dimension(250, 150));
		advancedCenter.add(advancedScrollPane, BorderLayout.NORTH);
		advancedCenter.add(advancedEntry, BorderLayout.CENTER);
		//
		advancedPanel.add(advancedSavedQueries, BorderLayout.NORTH);
		advancedPanel.add(advancedCenter, BorderLayout.CENTER);
		advancedPanel.add(confirmPanelA, BorderLayout.SOUTH);
		advancedTable.getSelectionModel().addListSelectionListener(this);
		advancedScrollPane.getViewport().add(advancedTable, null);
		tabbedPane.add(advancedPanel, Msg.getMsg(m_ctx,"Advanced"));
		//
		this.getContentPane().add(tabbedPane, BorderLayout.NORTH);
		//
		confirmPanelA.addActionListener(this);
		confirmPanelS.addActionListener(this);
		//
		JButton b = ConfirmPanel.createNewButton(true);
		confirmPanelS.addComponent (b);
		b.addActionListener(this);
	}	//	jbInit

	/**
	 * 	Fill Query
	 *	@return true if queries exist
	 */
	private boolean fillQuery()
	{
		MUserQuery[] queries = MUserQuery.get (m_ctx, m_AD_Tab_ID, m_AD_Table_ID);
		if (queries.length == 0)
			return false;
		//
		Vector<ValueNamePair> vectorS = new Vector<ValueNamePair>();
		ValueNamePair pp = new ValueNamePair("", Msg.getMsg (m_ctx, "SelectSavedQuery"));
		vectorS.add(pp);
		for (MUserQuery query : queries) {
			pp = new ValueNamePair(query.getCode(), query.getName());
			vectorS.add(pp);
		}
		ComboBoxModel modelS = new DefaultComboBoxModel(vectorS);
		comboQueriesS.setModel(modelS);
		comboQueriesS.setToolTipText (Msg.getMsg(m_ctx,"QueryName"));
		comboQueriesS.addActionListener(this);
		//
		Vector<MUserQuery> vectorA = new Vector<MUserQuery>();
		vectorA.add(null);
		for (MUserQuery element : queries)
			vectorA.add(element);
		ComboBoxModel modelA = new DefaultComboBoxModel(vectorA);
		comboQueriesA.setModel(modelA);
		comboQueriesA.setToolTipText (Msg.getMsg(m_ctx,"QueryName"));
		comboQueriesA.setEditable(true);
		comboQueriesA.addActionListener(this);
		return true;
	}	//	fillQuery

	/**
	 *	Dynamic Init = Find Fields
	 *  Set up GridController
	 */
	private void initFind()
	{
		log.config("");
		List<GridField> specificSelectionCols = new ArrayList<GridField>();
		List<GridField> unassignedSelectionCols = new ArrayList<GridField>();

		//	Get Info from target Tab
		for (GridField mField : m_findFields) {
			if (mField.isEncrypted())
				continue;
			if (!mField.isDisplayed())
				continue;
			String columnName = mField.getColumnName();

			if (columnName.equals("Value"))
				hasValue = true;
			else if (columnName.equals("Name"))
				hasName = true;
			else if (columnName.equals("DocumentNo"))
				hasDocNo = true;
			else if (columnName.equals("Description"))
				hasDescription = true;
			else if (mField.isSelectionColumn() || (columnName.indexOf("Name") != -1))
			{
				if (mField.getSelectionSeqNo() == 0)
					unassignedSelectionCols.add(mField);
				else
				{
					boolean added = false;
					for(int i=0; i< specificSelectionCols.size(); i++)
					{
						if(mField.getSelectionSeqNo() <= specificSelectionCols.get(i).getSelectionSeqNo())
						{
							specificSelectionCols.add(i, mField);
							added = true;
							break;
						}
					}
					if(!added)
						specificSelectionCols.add(mField);
				}
			}

			//  TargetFields
			m_targetFields.put (Integer.valueOf(mField.getAD_Column_ID()), mField);
		}   //  for all target tab fields

		
		for (GridField selectionCol:specificSelectionCols)
		{
			addSelectionColumn(selectionCol);
		}
		for (GridField selectionCol:unassignedSelectionCols)
		{
			addSelectionColumn(selectionCol);
		}
		
		//	Disable simple query fields
		valueLabel.setVisible(hasValue);
		valueField.setVisible(hasValue);
		if (hasValue)
			valueField.addActionListener(this);
		docNoLabel.setVisible(hasDocNo);
		docNoField.setVisible(hasDocNo);
		if (hasDocNo)
			docNoField.addActionListener(this);
		nameLabel.setVisible(hasName);
		nameField.setVisible(hasName);
		if (hasName)
			nameField.addActionListener(this);
		descriptionLabel.setVisible(hasDescription);
		descriptionField.setVisible(hasDescription);
		if (hasDescription)
			descriptionField.addActionListener(this);

		//	Get Total
		m_total = getNoOfRecords(null, false);
		setStatusDB (m_total);
		statusBar.setStatusLine("");

		tabbedPane.addChangeListener(this);

		//	Better Labels for OK/Cancel
		confirmPanelA.getOKButton().setToolTipText(Msg.getMsg(m_ctx,"QueryEnter"));
		confirmPanelA.getCancelButton().setToolTipText(Msg.getMsg(m_ctx,"QueryCancel"));
		confirmPanelS.getOKButton().setToolTipText(Msg.getMsg(m_ctx,"QueryEnter"));
		confirmPanelS.getCancelButton().setToolTipText(Msg.getMsg(m_ctx,"QueryCancel"));
		
		bDeleteRow.addActionListener(this);
		bDeleteRow.setToolTipText(Msg.getMsg(m_ctx, "QueryDeleteRow"));
		bSaveRow.addActionListener(this);
		bSaveRow.setToolTipText(Msg.getMsg(m_ctx, "QuerySaveRow"));
	}	//	initFind

	/**
	 * 	Add Selection Column to first Tab (Simple)
	 * 	@param mField field
	 */
	private void addSelectionColumn (GridField mField)
	{
		log.fine(mField.getHeader());
		int displayLength = mField.getDisplayLength();
		if (displayLength > FIELDLENGTH)
			mField.setDisplayLength(FIELDLENGTH);
		else
			displayLength = 0;
		
		//	Editor
		VEditor editor = null;
		if (mField.isLookup())
		{
			VLookup vl = new VLookup(mField.getColumnName(), false, false, true,
				mField.getLookup());
			vl.setName(mField.getColumnName());
			editor = vl;
		}
		else
		{
			editor = VEditorFactory.getEditor(mField, false);
			editor.setMandatory(false);
			editor.setReadWrite(true);
		}
		CLabel label = VEditorFactory.getLabel(mField);
		//
		if (displayLength > 0)		//	set it back
			mField.setDisplayLength(displayLength);
		//
		m_sLine++;
		if (label != null)	//	may be null for Y/N
			scontentPanel.add(label,   new GridBagConstraints(1, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
		scontentPanel.add((Component)editor,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		m_sEditors.add(editor);
		editor.addActionListener(this);
	}	//	addSelectionColumn


	/**
	 *  Init Advanced Tab (called when tab changed)
	 */
	private void initFindAdvanced()
	{
		log.config("");
		
		advancedEntry.removeAll();
		advancedData = new Vector<AdvancedRow>();
		Vector<String> columnNames = new Vector<String>(); 
		columnNames.add(Msg.translate(m_ctx,"AD_Column_ID"));
		columnNames.add(Msg.translate(m_ctx,"Operator"));
		columnNames.add(Msg.translate(m_ctx,"QueryValue"));
		columnNames.add(Msg.translate(m_ctx,"QueryValue2"));
		//
		DefaultTableModel model = new DefaultTableModel(advancedData, columnNames)
		{
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {return false;}
		};
		advancedTable.setModel(model);
		advancedTable.setSortEnabled(false);
		advancedTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        advancedTable.setRowSelectionAllowed(true);
        advancedTable.setColumnSelectionAllowed(false);

        m_buttonFields.clear();
        
		//	0 = Columns
		ArrayList<ValueNamePair> items = new ArrayList<ValueNamePair>();
		for (int c = 0; c < m_findFields.length; c++)
		{
			GridField field = m_findFields[c];
			if (field.isEncrypted())
				continue;
			String columnName = field.getColumnName();
			if (field.getDisplayType() == DisplayTypeConstants.Button) 
			{
				if (field.getAD_Reference_Value_ID() == 0)
					continue;
				if (columnName.toUpperCase().endsWith("_ID"))
					field.setDisplayType(DisplayTypeConstants.Table);
				else
					field.setDisplayType(DisplayTypeConstants.List);
				field.loadLookup();
				m_buttonFields.add(c);
			}
			String header = field.getHeader();
			if (header == null || header.length() == 0)
			{
				header = Msg.getElement(m_ctx, columnName);
				if (header == null || header.length() == 0)
					continue;
			}
			if (field.isKey())
				header += (" (ID)");
			ValueNamePair pp = new ValueNamePair(columnName, header);
			items.add(pp);
		}
		ValueNamePair[] cols = new ValueNamePair[items.size() + 1];
		items.toArray(cols);
		Arrays.sort(cols);		//	sort alpha
		columns = new CComboBox(cols);
		columns.addActionListener(this);
		columns.setAutoReducible(false);
		columns.setEditable(false);
		Dimension size = columns.getPreferredSize();
		size.width = 180;
		columns.setPreferredSize(size);
		//
		String name = columnNames.get(INDEX_COLUMNNAME);
		CLabel lcolumn = new CLabel(name);
		lcolumn.setLabelFor(columns);
		advancedEntry.add(lcolumn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		advancedEntry.add(columns, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		TableColumn tc = advancedTable.getColumnModel().getColumn(INDEX_COLUMNNAME);
		tc.setPreferredWidth(150);
		
		//	1 = Operators
		operators = new CComboBox(Query.OPERATORS);
		operators.addActionListener(this);
		operators.setAutoReducible(false);
		operators.setEnabled(false);
		size = operators.getPreferredSize();
		size.width = 50;
		operators.setPreferredSize(size);
		//
		name = columnNames.get(INDEX_OPERATOR);
		CLabel loperator = new CLabel(name);
		loperator.setLabelFor(operators);
		advancedEntry.add(loperator, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		advancedEntry.add(operators, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		tc = advancedTable.getColumnModel().getColumn(INDEX_OPERATOR);
		tc.setPreferredWidth(50);

		// 	2 = QueryValue
		valueEditor = new FindValueEditor();
		valueEditor.setEnabled(false);
		size = valueEditor.getPreferredSize();
		size.width = 120;
		valueEditor.setPreferredSize(size);
		name = columnNames.get(INDEX_VALUE);
		CLabel lvalue = new CLabel(name);
		lvalue.setLabelFor(valueEditor);
		advancedEntry.add(lvalue, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		advancedEntry.add(valueEditor, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE);
		tc.setPreferredWidth(120);

		// 	3 = QueryValue2
		valueEditor2 = new FindValueEditor();
		valueEditor2.setEnabled(false);
		name = columnNames.get(INDEX_VALUE2);
		CLabel lvalue2 = new CLabel(name);
		lvalue2.setLabelFor(valueEditor2);
		advancedEntry.add(lvalue2, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		advancedEntry.add(valueEditor2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE2);
		tc.setPreferredWidth(50);
		//
		advancedEntry.add(bDeleteRow, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 0, 0));
		advancedEntry.add(bSaveRow, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
		bSaveRow.setEnabled(false);
		bDeleteRow.setEnabled(false);
		//
		pack();
	}   //  initFindAdvanced

	/**
	 *	Dispose window
	 */
	@Override
	public void dispose()
	{
		log.fine("");

		//  Find SQL
		if (m_pstmt != null)
		{
			try 
			{
				m_pstmt.close();
			} 
			catch (SQLException e)
			{}
		}
		m_pstmt = null;

		//  TargetFields
		if (m_targetFields != null)
			m_targetFields.clear();
		m_targetFields = null;
		//
		if (m_sEditors != null)
		{
			for (int i = 0; i < m_sEditors.size(); i++)
				m_sEditors.get(i).removeActionListener(this);
			m_sEditors.clear();
		}
		m_sEditors = null;
		
		removeAll();
		super.dispose();
	}	//	dispose

	
	/**************************************************************************
	 *	Action Listener
	 *  @param e ActionEvent
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		log.fine(e.getActionCommand());
		Object source = e.getSource();
		m_days = 0;
		m_query = null;
		//	Cancel - Simple & Advances
		if (e.getActionCommand() == ConfirmPanel.A_CANCEL)
			cmd_cancel();
		//	New - Simple
		else if (e.getActionCommand() == ConfirmPanel.A_NEW)
		{
			m_query = Query.getNoRecordQuery(m_tableName, true);
			m_total = 0;
			dispose();
		}

		//	Refresh - Advanced
		else if (e.getActionCommand() == ConfirmPanel.A_REFRESH)
			cmd_refresh();
		//	Saved Query Retrieve - Advanced 
		else if (source == comboQueriesA)
			cmd_savedQueryLoad();
		//	Delete Query
		else if (source == bDeleteQuery)
			cmd_savedQueryDelete();
		
		//	Set Advanced Column
		else if (source == columns)
		{
			ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
			if (column != null)
			{
				String columnName = column.getValue();
				log.config("Column=" + columnName);
				if (columnName.toUpperCase().endsWith("_ID") || columnName.endsWith("_Acct"))
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS_ID));
				else if (columnName.startsWith("Is"))
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS_YN));
				else
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS));
				GridField field = getTargetMField(columnName);
				valueEditor.setEditor(field);
				bSaveRow.setEnabled(true);
			}
			operators.setEnabled(true);
			valueEditor.setEnabled(true);
			valueEditor2.setEnabled(false);
			advancedTable.getSelectionModel().clearSelection();
		}
		//	Set Advanced Operator
		else if (source == operators)
		{
			ValueNamePair operator = (ValueNamePair)operators.getSelectedItem();
			if (operators != null)
			{
				String op = operator.getValue();
				log.config("Operator: " + op);
				if (Query.BETWEEN.equals(op))
				{
					ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
					String columnName = column.getValue();
					GridField field = getTargetMField(columnName);
					valueEditor2.setEditor(field);
					valueEditor2.setEnabled(true);
				}
			}
			else
			{
				bSaveRow.setEnabled(false);
				valueEditor2.setEnabled(false);
			}
			advancedTable.getSelectionModel().clearSelection();
		}
		else if (source == bSaveRow)
			cmd_saveRow();
		else if (source == bDeleteRow)
			cmd_deleteRow();
		
		//	History
		else if (source == comboQueriesS || source == bShowDay || source == bShowWeek 
			|| source == bShowMonth || source == bShowYear || source == bShowAll)
		{
			if (e.getSource() == bShowDay)
				m_days = 1;
			else if (e.getSource() == bShowWeek)
				m_days = 7;
			else if (e.getSource() == bShowMonth)
				m_days = 31;
			else if (e.getSource() == bShowYear)
				m_days = 356;
			else
				m_days = 0;		//	all
			m_created = cbCreated.isSelected();
			//
			Object xx = comboQueriesS.getSelectedItem();
			if (xx instanceof ValueNamePair)
			{
				ValueNamePair pp = (ValueNamePair)xx;
				m_query = new Query(m_tableName);
				m_query.addRestriction (pp.getValue());
				int no = getNoOfRecords(m_query, true);
				m_query.setRecordCount(no);
				if (no != 0)
					dispose();
			}
			else
				dispose();
		}
		
		else    // ConfirmPanel.A_OK and enter in fields - Simple & Advanced
		{
			if (source == confirmPanelA.getOKButton())
				cmd_okAdvanced();
			else
			{
				if(source == confirmPanelS.getOKButton())
					cmd_okSimple();
				//	Close when hitting enter in text field on first tab
				Component[] simpleComponents = scontentPanel.getComponents();
				for (Component component : simpleComponents) 
				{
					if (component == source)
					{
						cmd_okSimple();
						break;
					}
				}
			}
		}
	}	//	actionPerformed

	/**
	 *  Change Listener (Tab change)
	 *  @param e ChangeEbent
	 */
	public void stateChanged(ChangeEvent e)
	{
		if (tabbedPane.getSelectedIndex() == 0)
			this.getRootPane().setDefaultButton(confirmPanelS.getOKButton());
		else
		{
			initFindAdvanced();
			this.getRootPane().setDefaultButton(confirmPanelA.getOKButton());
		}
	}	//  stateChanged

	/**
	 *	Simple OK Button pressed
	 */
	private void cmd_okSimple()
	{
		//	Create Query String
		m_query = new Query(m_tableName);
		if (hasValue && !valueField.getText().equals("%") && valueField.getText().length() != 0)
		{
			String value = valueField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("Value", Query.LIKE, value, valueLabel.getText(), value);
		}
		//
		if (hasDocNo && !docNoField.getText().equals("%") && docNoField.getText().length() != 0)
		{
			String value = docNoField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("DocumentNo", Query.LIKE, value, docNoLabel.getText(), value);
		}
		//
		if ((hasName) && !nameField.getText().equals("%") && nameField.getText().length() != 0)
		{
			String value = nameField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("Name", Query.LIKE, value, nameLabel.getText(), value);
		}
		//
		if (hasDescription && !descriptionField.getText().equals("%") && descriptionField.getText().length() != 0)
		{
			String value = descriptionField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("Description", Query.LIKE, value, descriptionLabel.getText(), value);
		}
		//	Special Editors
		for (int i = 0; i < m_sEditors.size(); i++)
		{
			VEditor ved = m_sEditors.get(i);
			Object value = ved.getValue();
			if (value != null && value.toString().length() > 0)
			{
				String ColumnName = ved.getName();
				String sqlQuery = ColumnName;
				GridField field = ved.getField();
				if (field != null)
				{
					String sql = field.getColumnSQL(false);	//	virtual column
					if (sql != null && sql.length() > 0)
						sqlQuery = sql;
				}
				log.fine(ColumnName + "=" + value);
				if (value.toString().indexOf("%") != -1)
					m_query.addRestriction(sqlQuery, Query.LIKE, value, ColumnName, ved.getDisplay());
				else
					m_query.addRestriction(sqlQuery, Query.EQUAL, value, ColumnName, ved.getDisplay());
			}
		}	//	editors


		//	Test for no records
		if (getNoOfRecords(m_query, true) != 0)
			dispose();
	}	//	cmd_ok_Simple

	/**
	 *	Advanced OK Button pressed
	 */
	private void cmd_okAdvanced()
	{
		//	save pending
		cmd_saveAdvanced();
		
		/* nnayak : Reset display type for buttons */
		for (int c = 0; c < m_buttonFields.size(); c++)
		{
			if (m_findFields[m_buttonFields.get( c )] != null)
				m_findFields[m_buttonFields.get( c )].setDisplayType( DisplayTypeConstants.Button );
		}
		
		if (getNoOfRecords(m_query, true) != 0)
			dispose();
	}	//	cmd_ok_Advanced

	/**
	 *	Cancel Button pressed
	 */
	private void cmd_cancel()
	{
	//	log.fine("");
		m_query = null;
		m_total = 999999;
		dispose();
	}	//	cmd_ok


	/**
	 *	Save (Advanced)
	 */
	private void cmd_saveAdvanced()
	{
		log.fine("");
		//
		cmd_saveRow();	//	unsaved 
		m_query = getQueryAdvanced();
		if (m_query.getRestrictionCount() == 0)
			return;
		String where = m_query.getWhereClause();

		String name = savedQueryName.getText();
		if (name != null && name.length() == 0)
			name = null;
		
		//	Update Existing Query
		MUserQuery uq = (MUserQuery)comboQueriesA.getSelectedItem();
		if (uq != null)
		{
			if (name != null)
				uq.setName(name);
			uq.setCode (where);
			uq.setAD_Tab_ID	(m_AD_Tab_ID);
			uq.setAD_Table_ID (m_AD_Table_ID);
			//
			if (uq.save())
			{
				AdvancedRow.store(uq, advancedData);
				ADialog.info (m_targetWindowNo, this, "Updated", uq.getName());
			}
			else
				ADialog.warn (m_targetWindowNo, this, "SaveError", uq.getName());
		}
		else if (name != null)
		{
			uq = new MUserQuery (m_ctx, 0, null);
			uq.setName(name);
			uq.setCode (where);
			uq.setAD_Tab_ID	(m_AD_Tab_ID);
			uq.setAD_Table_ID (m_AD_Table_ID);
			//
			if (uq.save())
			{
				AdvancedRow.store(uq, advancedData);
				ADialog.info (m_targetWindowNo, this, "Saved", uq.getName());
			}
			else
				ADialog.warn (m_targetWindowNo, this, "SaveError", uq.getName());
		}
	}	//	cmd_save

	/**
	 * 	Get Query from Advanced Tab
	 *	@return query
	 */
	private Query getQueryAdvanced()
	{
		Query query = new Query(m_tableName);
		for (int i = 0; i < advancedData.size(); i++)
		{
			AdvancedRow row = advancedData.get(i);
			//	Column
			ValueNamePair column = row.getColumn();
			String infoName = column.getName();
			String columnName = column.getValue();
			GridField field = getTargetMField(columnName);
			String columnSQL = field.getColumnSQL(false);
			//	Op
			String operator = row.getOperator().getValue();

			//	Value	******
			ValueNamePair value = row.getValue();
			Object parsedValue = parseValue(field, value);
			String infoDisplay = null;
			if (value == null)
			{
				if (Query.BETWEEN.equals(operator))
					continue;	//	no null in between
				parsedValue = Null.NULLString;
				infoDisplay = "NULL";
			}
			else
				infoDisplay = value.getName();
			//	Value2	******
			if (Query.BETWEEN.equals(operator))
			{
				ValueNamePair value2 = row.getValue2();
				if (value2 == null)
					continue;
				Object parsedValue2 = parseValue(field, value2);
				String infoDisplay_to = value2.getName();
				if (parsedValue2 == null)
					continue;
				query.addRangeRestriction(columnSQL, parsedValue, parsedValue2,
					infoName, infoDisplay, infoDisplay_to);
			}
			else
				query.addRestriction(columnSQL, operator, parsedValue,
					infoName, infoDisplay);
		}
		return query;
	}	//	getQueryAdvanced
	
	/**
	 * 	Parse Value
	 * 	@param field column
	 * 	@param in value
	 * 	@return data type corrected value
	 */
	private Object parseValue (GridField field, ValueNamePair pp)
	{
		if (pp == null)
			return null;
		int dt = field.getDisplayType();
		String in = pp.getValue();
		if (in == null || in.equals(Null.NULLString))
			return null;
		try
		{
			//	Return Integer
			if (dt == DisplayTypeConstants.Integer
				|| (FieldType.isID(dt) && field.getColumnName().toUpperCase().endsWith("_ID")))
			{
				int i = Integer.parseInt(in);
				return Integer.valueOf(i);
			}
			//	Return BigDecimal
			else if (FieldType.isNumeric(dt))
			{
				return DisplayType.getNumberFormat(dt).parse(in);
			}
			//	Return Timestamp
			else if (FieldType.isDate(dt))
			{
				long time = 0;
				try
				{
					return Timestamp.valueOf(in);
				}
				catch (Exception e)
				{
					log.log(Level.WARNING, in + "(" + in.getClass() + ")" + e);
					time = DisplayType.getDateFormat(dt).parse(in).getTime();
				}
				return new Timestamp(time);
			}
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "Object=" + in, ex);
			String error = ex.getLocalizedMessage();
			if (error == null || error.length() == 0)
				error = ex.toString();
			StringBuffer errMsg = new StringBuffer();
			errMsg.append(field.getColumnName()).append(" = ").append(in).append(" - ").append(error);
			//
			ADialog.error(0, this, "ValidationError", errMsg.toString());
			return null;
		}

		return in;
	}	//	parseValue

	/**
	 *	Delete User Query
	 */
	private void cmd_savedQueryDelete()
	{
		MUserQuery uq = (MUserQuery)comboQueriesA.getSelectedItem();
		if (uq == null)
			return;
		log.info(uq.toStringX());
		String name = uq.getName(); 
		if (uq.delete(true))
		{
			ADialog.info (m_targetWindowNo, this, "Deleted", name);
			MutableComboBoxModel modelA = (MutableComboBoxModel)comboQueriesA.getModel();
			modelA.removeElement(uq);
			modelA.setSelectedItem(null);
			savedQueryName.setText(null);
			savedQueryName.setReadWrite(false);
		}
		else
			ADialog.warn (m_targetWindowNo, this, "DeleteError");
	}	//	cmd_delete

	/**
	 *	Refresh - Advanced
	 */
	private void cmd_refresh()
	{
		log.fine("");
		cmd_saveRow();	//	unsaved 
		Query temp = getQueryAdvanced();
		int records = getNoOfRecords(temp, true);
		setStatusDB (records);
		statusBar.setStatusLine("");
	}	//	cmd_refresh

	/**
	 * 	Load saved Query - Advanced
	 */
	private void cmd_savedQueryLoad()
	{
		MUserQuery uq = (MUserQuery)comboQueriesA.getSelectedItem();
		if (uq == null)
		{
			savedQueryName.setText(null);
			savedQueryName.setReadWrite(false);
			return;
		}
		log.info(uq.toStringX());
		advancedData.removeAllElements();
		
		//	Load Info Table
		Vector<AdvancedRow> ad = AdvancedRow.load(uq);
		if (ad != null)
		{
			for (int i = 0; i < ad.size(); i++)
            {
	            AdvancedRow row = ad.get(i);
				advancedData.add(row);
            }
		}
		((DefaultTableModel)advancedTable.getModel())
			.fireTableDataChanged();
		advancedTable.getSelectionModel().clearSelection();
		bDeleteQuery.setEnabled(true);
		savedQueryName.setText(uq.getName());
		savedQueryName.setReadWrite(true);
	}	//	cmd_savedQueryLoad

	/**
	 * 	Save Advanced Row
	 * 	@return true if row saved
	 */
	private boolean cmd_saveRow()
	{
		ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
		if (column == null || column.getValue().length() == 0)
			return false;
		//
		ValueNamePair operator = (ValueNamePair)operators.getSelectedItem();
		String op = operator.getValue();
		//	Value
		Object value = valueEditor.getValue();
		String valueDisplay = valueEditor.getDisplay();
		/**	List - (NOT) IN
		Object[] values = null;
		String[] valuesDisplay = null;
		if (Query.EQUAL.equals(op) || Query.NOT_EQUAL.equals(op)
			|| Query.IN.equals(op) || Query.NOT_IN.equals(op))
		{
			values = valueEditor.getValues();
			if (values == null || values.length == 1)
			{
				if (Query.IN.equals(op))
					op = Query.EQUAL;
				if (Query.NOT_IN.equals(op))
					op = Query.NOT_EQUAL;
			}
			else
			{
				valuesDisplay = valueEditor.getDisplays();
				if (Query.EQUAL.equals(op))
					op = Query.IN;
				if (Query.NOT_EQUAL.equals(op))
					op = Query.NOT_IN;
			}
		}	*/
		ValueNamePair v = null;
		if (value == null)
		{
			if (Query.EQUAL.equals(op) || Query.NOT_EQUAL.equals(op))
				v = new ValueNamePair(Null.NULLString, "NULL");
			else
			{
				operators.setSelectedIndex(0);
				return false;
			}
		}
		else
		{
			if (value instanceof Boolean)
				value = ((Boolean)value).booleanValue() ? "Y" : "N";
			v = new ValueNamePair(value.toString(), valueDisplay);
		}
		
		//	Value 2
		ValueNamePair v2 = null;
		if (Query.BETWEEN.equals(op))
		{
			Object value2 = valueEditor2.getValue();
			String value2Display = valueEditor2.getDisplay();
			if (value2 == null)
				return false;
			else
			{
				if (value2 instanceof Boolean)
					value2 = ((Boolean)value).booleanValue() ? "Y" : "N";
				v2 = new ValueNamePair(value2.toString(), value2Display);
			}
		}
		
		AdvancedRow row = new AdvancedRow(column, operator, v, v2);
		log.info(row.toString());
		int index = advancedData.size();
		advancedData.add(row);
		((DefaultTableModel)advancedTable.getModel())
			.fireTableRowsInserted(index, index);
		advancedTable.getSelectionModel().clearSelection();
		columns.setSelectedIndex(0);
		operators.setSelectedIndex(0);
		valueEditor.setValue(null);
		valueEditor2.setValue(null);
		savedQueryName.setReadWrite(true);
		return true;
	}	//	cmd_saveRow
	
	/**
	 * 	Delete selected Advanced Row
	 */
	private void cmd_deleteRow()
	{
		int index = advancedTable.getSelectedRow();
		log.info("#" + index);
		if (index == -1)
			return;
		advancedData.remove(index);
		((DefaultTableModel)advancedTable.getModel())
			.fireTableRowsDeleted(index, index);
		advancedTable.getSelectionModel().clearSelection();
	}	//	cmd_deleteRow

	/**
	 * 	Value Changed - Advanced Table selection changed
	 *	@param e event
	 */
	public void valueChanged(ListSelectionEvent e)
    {
		if (e.getValueIsAdjusting())
			return;
		int index = advancedTable.getSelectedRow();
		bDeleteRow.setEnabled(index != -1);
		log.fine("#" + index);
    }	//	valueChanged

	
	/**************************************************************************
	 *	Get Query - Retrieve result
	 *  @return String representation of query
	 */
	public Query getQuery()
	{
		MRole role = MRole.getDefault();
		if (role.isQueryMax(getTotalRecords()))
		{
			m_query = Query.getNoRecordQuery (m_tableName, false);
			m_total = 0;
			log.warning("Query - over max");
		}
		else
			log.info("Query=" + m_query);
		return m_query;
	}	//	getQuery

	/**
	 * 	Get Total Records
	 *	@return no of records
	 */
	public int getTotalRecords()
	{
		return m_total;
	}	//	getTotalRecords
	
	/**
	 * 	Get selected number of days
	 * 	@return days or -1 for all
	 */
	public int getCurrentDays()
	{
		return m_days;
	}	//	getCurrentDays

	/**
	 * 	Get created or updated
	 * 	@return true if created
	 */
	public boolean getIsCreated()
	{
		return m_created;
	}	//	getIsCreated

	/**
	 *	Get the number of records of target tab
	 *  @param query where clause for target tab
	 * 	@param alertZeroRecords show dialog if there are no records
	 *  @return number of selected records
	 */
	private int getNoOfRecords (Query query, boolean alertZeroRecords)
	{
		log.config(query == null ? "" : query.toString());
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
		sql.append(m_tableName);
		boolean hasWhere = false;
		if (m_whereExtended != null && m_whereExtended.length() > 0)
		{
			sql.append(" WHERE ").append(m_whereExtended);
			hasWhere = true;
		}
		if (query != null && query.isActive())
		{
			if (hasWhere)
				sql.append(" AND ");
			else
				sql.append(" WHERE ");
			sql.append(query.getWhereClause());
		}
		//	Add Access
		String finalSQL = MRole.getDefault().addAccessSQL(sql.toString(), 
			m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		finalSQL = Env.parseContext(m_ctx, m_targetWindowNo, finalSQL, false);
		m_ctx.setContext(m_targetWindowNo, TABNO, "FindSQL", finalSQL);

		//  Execute Qusery
		m_total = 999999;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(finalSQL, (Trx) null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				m_total = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, finalSQL, e);
		}
		MRole role = MRole.getDefault(); 
		//	No Records
		if (m_total == 0 && alertZeroRecords)
			ADialog.info(m_targetWindowNo, this, "FindZeroRecords");
		//	More then allowed
		else if (query != null && role.isQueryMax(m_total))
			ADialog.error(m_targetWindowNo, this, "FindOverMax", 
				m_total + " > " + role.getMaxQueryRecords());
		else
			log.config("#" + m_total);
		//
		if (query != null)
			statusBar.setStatusToolTip (query.getWhereClause());
		return m_total;
	}	//	getNoOfRecords

	/**
	 *	Display current count
	 *  @param currentCount String representation of current/total
	 */
	private void setStatusDB (int currentCount)
	{
		String text = " " + currentCount + " / " + m_total + " ";
		statusBar.setStatusDB(text);
	}	//	setDtatusDB

	/**
	 * 	Get Target MField
	 * 	@param columnName column name
	 * 	@return MField
	 */
	private GridField getTargetMField (String columnName)
	{
		if (columnName == null)
			return null;
		for (GridField field : m_findFields) {
			if (columnName.equals(field.getColumnName()))
				return field;
		}
		return null;
	}	//	getTargetMField    
		
}	//	Find
