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
package org.compiere.grid;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;

import org.compiere.apps.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Tab to maintain Order/Sequence
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VSortTab.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VSortTab extends CPanel implements APanelTab
{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Tab Order Constructor
	 *
	 *  @param WindowNo Window No
	 *  @param AD_Table_ID Table No
	 *  @param AD_ColumnSortOrder_ID Sort Column
	 *  @param AD_ColumnSortYesNo_ID YesNo Column
	 */
	public VSortTab(int WindowNo, int AD_Table_ID, int AD_ColumnSortOrder_ID, int AD_ColumnSortYesNo_ID,
			boolean isReadOnly)
	{
		log.config("SortOrder=" + AD_ColumnSortOrder_ID + ", SortYesNo=" + AD_ColumnSortYesNo_ID);
		m_WindowNo = WindowNo;
		m_isReadOnly = isReadOnly;

		try
		{
			jbInit();
			dynInit (AD_Table_ID, AD_ColumnSortOrder_ID, AD_ColumnSortYesNo_ID);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	VSortTab

	/**	Logger			*/
	static CLogger log = CLogger.getCLogger(VSortTab.class);
	private int			m_WindowNo;
	private String		m_TableName = null;
	private String		m_ColumnSortName= null;
	private String		m_ColumnYesNoName = null;
	private String		m_KeyColumnName = null;
	private String		m_IdentifierColumnName = null;
	private boolean		m_IdentifierTranslated = false;

	private String		m_ParentColumnName = null;
	private APanel		m_aPanel = null;
	private boolean		m_isReadOnly;

	//	UI variables
	private GridBagLayout mainLayout = new GridBagLayout();
	private CLabel noLabel = new CLabel();
	private CLabel yesLabel = new CLabel();
	private CButton bAdd = new CButton();
	private CButton bRemove = new CButton();
	private CButton bUp = new CButton();
	private CButton bDown = new CButton();
	//
	DefaultListModel noModel = new DefaultListModel()
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void addElement(Object obj)
        {
            Object[] elements = toArray();
            Arrays.sort(elements);
            int index = Arrays.binarySearch(elements, obj);
            if (index < 0)
            	index = -1 * index - 1;
            if (index > elements.length)
            	super.addElement(obj);
            else
            	super.add(index, obj);
        }
        
        @Override
		public void add(int index, Object obj)
        {
            addElement(obj);
        }
    };
	DefaultListModel yesModel = new DefaultListModel();
	JList noList = new JList(noModel);
	JList yesList = new JList(yesModel);
	private JScrollPane noPane = new JScrollPane(noList);
	private JScrollPane yesPane = new JScrollPane(yesList);

	/**
	 * 	Dyanamic Init
	 *  @param AD_Table_ID Table No
	 *  @param AD_ColumnSortOrder_ID Sort Column
	 *  @param AD_ColumnSortYesNo_ID YesNo Column
	 */
	private void dynInit (int AD_Table_ID, int AD_ColumnSortOrder_ID, int AD_ColumnSortYesNo_ID)
	{
		String sql = "SELECT t.TableName, c.AD_Column_ID, c.ColumnName, e.Name,"	//	1..4
			+ "c.IsParent, c.IsKey, c.IsIdentifier, c.IsTranslated "				//	4..8
			+ "FROM AD_Table t, AD_Column c, AD_Element e "
			+ "WHERE t.AD_Table_ID=?"						//	#1
			+ " AND t.AD_Table_ID=c.AD_Table_ID"
			+ " AND (c.AD_Column_ID=? OR AD_Column_ID=?"	//	#2..3
			+ " OR c.IsParent='Y' OR c.IsKey='Y' OR c.IsIdentifier='Y')"
			+ " AND c.AD_Element_ID=e.AD_Element_ID";
		boolean trl = !Env.isBaseLanguage(Env.getCtx(), "AD_Element");
		if (trl)
			sql = "SELECT t.TableName, c.AD_Column_ID, c.ColumnName, et.Name,"	//	1..4
				+ "c.IsParent, c.IsKey, c.IsIdentifier, c.IsTranslated "		//	4..8
				+ "FROM AD_Table t, AD_Column c, AD_Element_Trl et "
				+ "WHERE t.AD_Table_ID=?"						//	#1
				+ " AND t.AD_Table_ID=c.AD_Table_ID"
				+ " AND (c.AD_Column_ID=? OR AD_Column_ID=?"	//	#2..3
				+ "	OR c.IsParent='Y' OR c.IsKey='Y' OR c.IsIdentifier='Y')"
				+ " AND c.AD_Element_ID=et.AD_Element_ID"
				+ " AND et.AD_Language=?";						//	#4
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Table_ID);
			pstmt.setInt(2, AD_ColumnSortOrder_ID);
			pstmt.setInt(3, AD_ColumnSortYesNo_ID);
			if (trl)
				pstmt.setString(4, Env.getAD_Language(Env.getCtx()));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				m_TableName = rs.getString(1);
				//	Sort Column
				if (AD_ColumnSortOrder_ID == rs.getInt(2))
				{
					log.fine("Sort=" + rs.getString(1) + "." + rs.getString(3));
					m_ColumnSortName = rs.getString(3);
					yesLabel.setText(rs.getString(4));
				}
				//	Optional YesNo
				else if (AD_ColumnSortYesNo_ID == rs.getInt(2))
				{
					log.fine("YesNo=" + rs.getString(1) + "." + rs.getString(3));
					m_ColumnYesNoName = rs.getString(3);
				}
				//	Parent2
				else if (rs.getString(5).equals("Y"))
				{
					log.fine("Parent=" + rs.getString(1) + "." + rs.getString(3));
					m_ParentColumnName = rs.getString(3);
				}
				//	KeyColumn
				else if (rs.getString(6).equals("Y"))
				{
					log.fine("Key=" + rs.getString(1) + "." + rs.getString(3));
					m_KeyColumnName = rs.getString(3);
				}
				//	Identifier
				else if (rs.getString(7).equals("Y"))
				{
					log.fine("Identifier=" + rs.getString(1) + "." + rs.getString(3));
					m_IdentifierColumnName = rs.getString(3);
					if (trl)
						m_IdentifierTranslated = "Y".equals(rs.getString(8));
				}
				else
					log.fine("??NotUsed??=" + rs.getString(1) + "." + rs.getString(3));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		noLabel.setText(Msg.getMsg(Env.getCtx(), "Available"));
		log.info(m_ColumnSortName);
	}	//	dynInit

	/**
	 * 	Static Layout
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(mainLayout);
		//
		noLabel.setText("No");
		yesLabel.setText("Yes");
        
        for (MouseMotionListener mml : noList.getMouseMotionListeners())
            noList.removeMouseMotionListener(mml);
        
        for (MouseMotionListener mml : yesList.getMouseMotionListeners())
            yesList.removeMouseMotionListener(mml);
        
        yesList.setEnabled(!m_isReadOnly);
        noList.setEnabled(!m_isReadOnly);

        MouseListener mouseListener = new MouseAdapter()
        {
            @Override
			public void mouseClicked(MouseEvent me)
            {
                if (me.getClickCount() > 1)
                {
                    JList list = (JList)me.getComponent();
                    Point p = me.getPoint();
                    int index = list.locationToIndex(p);
                    if (index > -1 && list.getCellBounds(index, index).contains(p))
                        migrateValueAcrossLists(me);
                }
            }
        };
        
        if (!m_isReadOnly)
        {
        	yesList.addMouseListener(mouseListener);
        	noList.addMouseListener(mouseListener);
        }

        ActionListener actionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                migrateValueAcrossLists(ae);
            }
        };
        
		bAdd.setIcon(Env.getImageIcon("Detail24.gif"));
		bAdd.setMargin(new Insets(2, 2, 2, 2));
        bAdd.addActionListener(actionListener);
		bAdd.setEnabled(!m_isReadOnly);
        
		bRemove.setIcon(Env.getImageIcon("Parent24.gif"));
		bRemove.setMargin(new Insets(2, 2, 2, 2));
		bRemove.addActionListener(actionListener);
		bRemove.setEnabled(!m_isReadOnly);

        MouseInputListener crossListMouseListener = new DragListener();
        if (!m_isReadOnly)
        {
        	yesList.addMouseListener(crossListMouseListener);
        	yesList.addMouseMotionListener(crossListMouseListener);
        	noList.addMouseListener(crossListMouseListener);
        	noList.addMouseMotionListener(crossListMouseListener);
        }

        actionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                migrateValueWithinYesList(ae);
            }
        };
        
		bUp.setIcon(Env.getImageIcon("Previous24.gif"));
		bUp.setMargin(new Insets(2, 2, 2, 2));
		bUp.addActionListener(actionListener);
		bUp.setEnabled(!m_isReadOnly);
        
		bDown.setIcon(Env.getImageIcon("Next24.gif"));
		bDown.setMargin(new Insets(2, 2, 2, 2));
		bDown.addActionListener(actionListener);
		bDown.setEnabled(!m_isReadOnly);

        MouseMotionListener yesListMouseMotionListener = new MouseMotionAdapter()
        {
            @Override
			public void mouseDragged(MouseEvent me)
            {
                JList list = (JList)me.getComponent();
                Point p = me.getPoint();
                int index = list.locationToIndex(p);
                if (index > -1 && list.getCellBounds(index, index).contains(p))
                    migrateValueWithinYesList(me);
            }
        };
        
        if (!m_isReadOnly)
        	yesList.addMouseMotionListener(yesListMouseMotionListener);

		yesPane.setPreferredSize(new Dimension(200, 300));
		yesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		noPane.setPreferredSize(new Dimension(200, 300));
		noList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.add(noLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(yesLabel,    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(bDown,         new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(noPane,      new GridBagConstraints(0, 1, 1, 3, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(yesPane,      new GridBagConstraints(2, 1, 1, 3, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(bUp,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(bAdd,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.add(bRemove,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
	}	//	jbInit

	/**************************************************************************
	 * 	Load Data
	 */
	public void loadData()
	{
		yesModel.removeAllElements();
		noModel.removeAllElements();
		
		//	SELECT t.AD_Field_ID,t.Name,t.SeqNo,t.IsDisplayed FROM AD_Field t WHERE t.AD_Tab_ID=? ORDER BY 4 DESC,3,2
		//	SELECT t.AD_PrintFormatItem_ID,t.Name,t.SeqNo,t.IsPrinted FROM AD_PrintFormatItem t WHERE t.AD_PrintFormat_ID=? ORDER BY 4 DESC,3,2
		//	SELECT t.AD_PrintFormatItem_ID,t.Name,t.SortNo,t.IsOrderBy FROM AD_PrintFormatItem t WHERE t.AD_PrintFormat_ID=? ORDER BY 4 DESC,3,2
		StringBuffer sql = new StringBuffer();
		//	Columns
		sql.append("SELECT t.").append(m_KeyColumnName)				//	1
			.append(m_IdentifierTranslated ? ",tt." : ",t.")
				.append(m_IdentifierColumnName)						//	2
			.append(",t.").append(m_ColumnSortName);				//	3
		if (m_ColumnYesNoName != null)
			sql.append(",t.").append(m_ColumnYesNoName);			//	4
		//	Tables
		sql.append(" FROM ").append(m_TableName).append( " t");
		if (m_IdentifierTranslated)
			sql.append(", ").append(m_TableName).append("_Trl tt");
		//	Where
		sql.append(" WHERE t.").append(m_ParentColumnName).append("=?");
		if (m_IdentifierTranslated)
			sql.append(" AND t.").append(m_KeyColumnName).append("=tt.").append(m_KeyColumnName)
				.append(" AND tt.AD_Language=?");
		//	Order
		sql.append(" ORDER BY ");
		if (m_ColumnYesNoName != null)
			sql.append("4 DESC,");		//	t.IsDisplayed DESC
		sql.append("3,2");				//	t.SeqNo, tt.Name 
		int ID = Env.getCtx().getContextAsInt( m_WindowNo, m_ParentColumnName);
		log.config(sql.toString() + " - ID=" + ID);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, ID);
			if (m_IdentifierTranslated)
				pstmt.setString(2, Env.getAD_Language(Env.getCtx()));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int key = rs.getInt(1);
				String name = rs.getString(2);
				int seq = rs.getInt(3);
				boolean isYes = seq != 0;
				if (m_ColumnYesNoName != null)
					isYes = rs.getString(4).equals("Y");
				//
				KeyNamePair pp = new KeyNamePair(key, name);
				if (isYes)
					yesModel.addElement(pp);
				else
					noModel.addElement(pp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
        
        m_aPanel.aSave.setEnabled(false);
	}	//	loadData

    /**
     * @param event
     */
    void migrateValueAcrossLists (AWTEvent event)
    {
        Object source = event.getSource();
        Object[] selObjects = (source == bAdd || source == noList) ?
            noList.getSelectedValues() : yesList.getSelectedValues();
        for (Object selObject : selObjects) {
			if (selObject == null)
	        	continue;
			
            DefaultListModel lmFrom = (source == bAdd || source == noList) ?
                noModel : yesModel;
            DefaultListModel lmTo = (lmFrom == yesModel) ? noModel : yesModel;
            lmFrom.removeElement(selObject);
            lmTo.addElement(selObject);
            
            JList list =  (source == bAdd || source == noList) ?
                yesList : noList;
            list.setSelectedValue(selObject, true);
            
            //  Enable explicit Save
            if (m_aPanel != null)
                m_aPanel.aSave.setEnabled(true);
        }
    }	//	migrateValueAcrossLists

    /**
     * 	Move within Yes List
     *	@param event event
     */
    void migrateValueWithinYesList (AWTEvent event)
    {
    	//	todo: group moves
    	
        Object[] selObjects = yesList.getSelectedValues();
        if (selObjects == null)
        	return;
        int length = selObjects.length;
        if (length == 0)
        	return;
		Object selObject = selObjects[0];
		if (selObject == null)
			return;
		//
        int[] indices = yesList.getSelectedIndices();
		int index = indices[0];
		//
        boolean change = false;
        //
        Object source = event.getSource();
        if (source == bUp)
        {
        	if (index == 0)
        		return;
        //	if (length == 1)
        	{
        		Object newObject = yesModel.getElementAt(index - 1);
        		yesModel.setElementAt(newObject, index);
        		yesModel.setElementAt(selObject, index - 1);
        		yesList.setSelectedIndex(index - 1);
        		change = true;
        	}
        }	//	up
        
        else if (source == bDown)
        {
        	if (index + indices.length >= yesModel.size ())
        		return;	
        //	if (length == 1)
        	{
        		Object newObject = yesModel.getElementAt(index + 1);
        		yesModel.setElementAt(newObject, index);
        		yesModel.setElementAt(selObject, index + 1);
        		yesList.setSelectedIndex(index + 1);
        		change = true;
        	}
        }	//	down
        
        else if (source == yesList)
        {
        	/**
        	MouseEvent me = (MouseEvent)event;
        	int newIndex = yesList.locationToIndex(me.getPoint());
        	if (index != newIndex)
        	{
        		Object newObject = yesModel.getElementAt(newIndex);
        		yesModel.setElementAt(newObject, index);
        		yesModel.setElementAt(selObject, newIndex);
        		yesList.setSelectedIndex(newIndex);
        		change = true;
        	}
        	**/
        }
        else
        	log.severe("Unknown source: " + source);
        //
    //	if (change && length > 1)
    //	  	yesList.setSelectedIndices(indices);
        
        //  Enable explicit Save
        if (change && m_aPanel != null)
            m_aPanel.aSave.setEnabled(true);
    }	//	migrateValueWithinYesList

	/**
	 * 	Register APanel
	 * 	@param panel panel
	 */
	public void registerAPanel (APanel panel)
	{
		m_aPanel = panel;
	}	//	registerAPanel

	
	/**
	 * 	Save Data
	 */
	public void saveData()
	{
		if (!m_aPanel.aSave.isEnabled())
			return;
		log.info("");
		StringBuffer sql = null;
		//	noList - Set SortColumn to null and optional YesNo Column to 'N'
		for (int i = 0; i < noModel.getSize(); i++)
		{
			KeyNamePair pp = (KeyNamePair)noModel.getElementAt(i);
			sql = new StringBuffer();
			sql.append("UPDATE ").append(m_TableName)
				.append(" SET ").append(m_ColumnSortName).append("=0");
			if (m_ColumnYesNoName != null)
				sql.append(",").append(m_ColumnYesNoName).append("='N'");
			sql.append(" WHERE ").append(m_KeyColumnName).append("=").append(pp.getKey());
			if (DB.executeUpdate((Trx) null, sql.toString()) != 1)
				log.log(Level.SEVERE, "NoModel - Not updated: " + m_KeyColumnName + "=" + pp.getKey());
		}
		//	yesList - Set SortColumn to value and optional YesNo Column to 'Y'
		for (int i = 0; i < yesModel.getSize(); i++)
		{
			KeyNamePair pp = (KeyNamePair)yesModel.getElementAt(i);
			sql = new StringBuffer();
			sql.append("UPDATE ").append(m_TableName)
				.append(" SET ").append(m_ColumnSortName).append("=").append(i+1).append("0");	//	10 steps
			if (m_ColumnYesNoName != null)
				sql.append(",").append(m_ColumnYesNoName).append("='Y'");
			sql.append(" WHERE ").append(m_KeyColumnName).append("=").append(pp.getKey());
			if (DB.executeUpdate((Trx) null, sql.toString()) != 1)
				log.log(Level.SEVERE, "YesModel - Not updated: " + m_KeyColumnName + "=" + pp.getKey());
		}
	}	//	saveData

	/**
	 * 	Unregister APanel
	 */
	public void unregisterPanel ()
	{
		saveData();
		m_aPanel = null;
	}	//	dispoase
    
    /**
     *
     */
    private class DragListener extends MouseInputAdapter
    {
        
        /**
         * Creates a VSortTab.DragListener.
         */
        public DragListener()
        {
            URL url = VSortTab.class.getResource(cursorName);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage(url);
            customCursor = toolkit.createCustomCursor(image, new Point(0, 0), "Howdy");
        }
        
        /** The cursorName. */
        private String cursorName = "/org/compiere/images/DragCursor32.gif";
        
        /** StartList	*/
        private JList 				startList = null;
        
        /** The startModel. */
        private DefaultListModel	startModel = null;
        
        /** The selObject. */
        private Object 				selObject = null;
        
        private boolean				moved = false;
        
        /** The customCursor. */
        private Cursor customCursor;
        
        /* (non-Javadoc)
         * @see javax.swing.event.MouseInputAdapter#mousePressed(java.awt.event.MouseEvent)
         */
        @Override
		public void mousePressed(MouseEvent me)
        {
            JList list = (JList)me.getComponent();
            Point p = me.getPoint();
            int index = list.locationToIndex(p);
            if (index > -1 && list.getCellBounds(index, index).contains(p))
            {
            	startList = list;
                startModel = (list == noList) ? noModel : yesModel;
                selObject = list.getModel().getElementAt(index);
            }
            if (list == noList)
            	yesList.clearSelection();
            else
            	noList.clearSelection();
            moved = false;
        }	//	mousePressed
        
        /* (non-Javadoc)
         * @see javax.swing.event.MouseInputAdapter#mouseDragged(java.awt.event.MouseEvent)
         */
        @Override
		public void mouseDragged(MouseEvent me)
        {
        	moved = true;
        	if (getCursor() != customCursor)
        		setCursor(customCursor);
        }	//	mouseDragged

        /* (non-Javadoc)
         * @see javax.swing.event.MouseInputAdapter#mouseReleased(java.awt.event.MouseEvent)
         */
        @Override
		public void mouseReleased(MouseEvent me)
        {
            if (startModel != null && moved)
            {
                Point p = me.getPoint();
                
               	JList endList = yesList;
           		DefaultListModel endModel = yesModel;

           		if (me.getComponent() == yesList)
               	{
               		if (!yesList.contains (p))
               		{
               			endList = noList;
               			endModel = noModel;
               		}
               	}
           		else
               	{
               		if (noList.contains (p))
               			return;		//	move within noList
                   	p = SwingUtilities.convertPoint (noList, p, yesList);
               	}
           		int index = endList.locationToIndex(p);
           		if (index > -1)	// && endList.getCellBounds(index, index).contains(p))
           		{
           			startModel.removeElement(selObject);
           			endModel.add(index, selObject);
           			//
           			startList.clearSelection();
           			endList.clearSelection();
           			endList.setSelectedValue(selObject, true);
           		}
            }
            
            startList = null;
            startModel = null;
            selObject = null;
            moved = false;
            setCursor(Cursor.getDefaultCursor());
        }	//	mouseReleased
    }

}	//	VSortTab

