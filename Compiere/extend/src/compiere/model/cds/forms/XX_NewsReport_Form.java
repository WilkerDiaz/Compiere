package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VLO_NewsReport;
import compiere.model.cds.X_XX_VMR_VendorProdRef;

/**
 *  News Report
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_NewsReport_Form extends CPanel

	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener
	{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		inOutID = new Integer(Env.getCtx().get_ValueAsString("#XX_NEWSREPORT_ID"));
		Env.getCtx().remove("#XX_NEWSREPORT_ID");
	
		try
		{
			//	UI
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_NewsReport_Form.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private Integer inOutID=0;
	private MInOut inOut = null;
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("");
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	private static int tableInit_option;
	private CComboBox xCombo = new CComboBox();
	private CComboBox xComboProduct = new CComboBox();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private CButton bSave = new CButton();
	private CButton bAddMotive = new CButton();
	private CLabel xReportMotive = new CLabel(Msg.translate(Env.getCtx(), "NewsReportMotive"));
	private CLabel xProduct = new CLabel(Msg.translate(Env.getCtx(), "M_Product_ID"));
	
	/**
	 *  Static Init.
	 *  <pre>
	 *  mainPanel
	 *      northPanel
	 *      centerPanel
	 *          xMatched
	 *          xPanel
	 *          xMathedTo
	 *      southPanel
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);
		
		bSave.setText(Msg.translate(Env.getCtx(), "SaveExit"));
		bSave.setPreferredSize(new Dimension(120,22));	
		bSave.setEnabled(true);
		
		xCombo.setPreferredSize(new Dimension(240,22));
		xComboProduct.setPreferredSize(new Dimension(240,22));
		
		bAddMotive.setText(Msg.translate(Env.getCtx(), "XX_Add"));
		bAddMotive.setPreferredSize(new Dimension(80,22));	
		bAddMotive.setEnabled(true);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(930, 300));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	
		xScrollPane.getViewport().add(xTable, null);
		
		southPanel.add(xProduct,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 30, 750), 0, 0));
		
		southPanel.add(xComboProduct,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 30, 400), 0, 0));
		
		southPanel.add(xReportMotive ,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 100, 30, 0), 0, 0));
		
		southPanel.add(xCombo,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
					,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 550, 30, 0), 0, 0));
		
		southPanel.add(bAddMotive,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(60, 0, 10, 200), 0, 0));
		
		southPanel.add(bSave,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(60, 200, 10, 0), 0, 0));
		 
	    southPanel.validate();
	
	}   //  jbInit
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Row"),   ".", KeyNamePair.class), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VendorReference"),   ".", String.class, false, false, ""), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "Description"),   ".", String.class, false, false, ""), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "Quantity"),   ".",  Integer.class, false, false,""),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NoSencamer"),   ".", Boolean.class, false, false,""),
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NewsReportMotive"),   ".", KeyNamePair.class, true, false,""),
				new ColumnInfo(Msg.translate(Env.getCtx(), ""),   ".", KeyNamePair.class, true, false,"")
		};
		
	
		xTable.prepareTable(layout, "", "", false, "");
		xTable.getColumnModel().getColumn(0).setMaxWidth(30);
		xTable.getColumnModel().getColumn(1).setMaxWidth(150);
		xTable.getColumnModel().getColumn(2).setMaxWidth(380);
		xTable.getColumnModel().getColumn(3).setMaxWidth(80);
		xTable.getColumnModel().getColumn(4).setMaxWidth(80);
		xTable.getColumnModel().getColumn(5).setMaxWidth(200);
		xTable.getColumnModel().getColumn(6).setMaxWidth(1);
		xTable.setAutoResizeMode(3);
		
		inOut = new MInOut( Env.getCtx(), inOutID, null);
		
		//  Visual
		CompiereColor.setBackground (this);
			
		//Cargo la tabla 1
		tableInit_option=0;
		tableInit(inOut);
		tableLoad (xTable);
		
		bSave.addActionListener(this);
		bAddMotive.addActionListener(this);
		
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "NoofRows"));
		
		MOrder order = new MOrder( Env.getCtx(), inOut.getC_Order_ID(), null);
		
		if(inOut.getDocStatus().equals("CO") || inOut.getXX_CheckAssistant_ID()==0 || 
				(inOut.get_Value("XX_OrderType").equals("Nacional") && !order.getXX_ComesFromSITME() && !newsReportVendor(order))){
			xCombo.setEnabled(false);
			xCombo.setEditable(false);
			bSave.setEnabled(false);
			bAddMotive.setEnabled(false);
			xTable.setEnabled(false);
			xComboProduct.setEnabled(false);
			xComboProduct.setEditable(false);
		}
		
		loadProducts();
		loadMotives();
		
		statusBar.setStatusDB(xTable.getRowCount());
		
	}   //  dynInit
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}	//	dispose
	
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSave)
			cmd_Save();
		else if (e.getSource() == bAddMotive)
			cmd_Add();
	}   //  actionPerformed
	
	/**
	 *  Save Button Pressed
	 */
	private void cmd_Save()
	{
		if(xTable.isEditing()){
			xTable.getCellEditor().stopCellEditing();
		}
		
		boolean save = true;
		for(int i=0; i<xTable.getRowCount(); i++){
		
			if(xTable.getValueAt( i, 3)==null){
				save = false;
				xTable.setRowSelectionInterval(i, i);
			}
			else if(xTable.getValueAt( i, 1).toString().trim().equals("") && xTable.getValueAt( i, 2).toString().trim().equals("") && (Integer)xTable.getValueAt( i, 3)>0){
				save = false;
				xTable.setRowSelectionInterval(i, i);
			}
		}
		
		if(save){
			
			Integer motiveValue = 0;
			for(int i=0; i<xTable.getRowCount(); i++){
				
				KeyNamePair pair = (KeyNamePair)xTable.getValueAt( i, 0);
				
				if(new Integer(xTable.getValueAt( i, 3).toString())>0){
					
					X_XX_VLO_NewsReport newsReport = new X_XX_VLO_NewsReport( Env.getCtx(), pair.getKey(), null);
					newsReport.setXX_VendorReference(xTable.getValueAt( i, 1).toString());
					newsReport.setDescription(xTable.getValueAt( i, 2).toString());
					newsReport.setXX_Quantity(new Integer(xTable.getValueAt( i, 3).toString()));
					newsReport.setXX_NoSencamer(new Boolean(xTable.getValueAt( i, 4).toString()));
					newsReport.setC_Order_ID(inOut.getC_Order_ID());
					motiveValue = ((KeyNamePair)xTable.getValueAt( i, 5)).getKey();
					newsReport.set_Value("XX_NewsReportMotive", motiveValue.toString());
					
					//producto sugerido
					if((KeyNamePair)xTable.getValueAt( i, 6)!=null && ((KeyNamePair)xTable.getValueAt( i, 6)).getKey()!=0)
						newsReport.set_Value("XX_AuxProduct_ID", ((KeyNamePair)xTable.getValueAt( i, 6)).getKey());
					
					newsReport.save();
				}
				else if(pair.getKey()>0){
					
					X_XX_VLO_NewsReport newsReport = new X_XX_VLO_NewsReport( Env.getCtx(), pair.getKey(), null);
					newsReport.delete(false);
				}
			}
			
			dispose();
		}
		
	}   //  cmd_Save
	
	/**
	 *  Add Button Pressed
	 */
	private void cmd_Add()
	{		
		
		if(xCombo.getItemAt(xCombo.getSelectedIndex()).toString().equals("EXCEDENTE") && xComboProduct.getSelectedIndex()<0){
		
			ADialog.info(m_WindowNo, this.mainPanel, "Debe indicar un producto si selecciona EXCEDENTE");
			return;
		}
		
		TableModel model = xTable.getModel();
		xTable.setRowCount(xTable.getRowCount()+1);
			
		int pos = xTable.getRowCount();
			
		if(xTable.getRowCount()>0 && xTable.isEditing()){
			xTable.getCellEditor().stopCellEditing();
		}
			
		if(xTable.getRowCount()!=1){
			for(int i=1; i<pos; i++){
									
				model.setValueAt(xTable.getValueAt(pos-1-i, 0), pos-i, 0);
				model.setValueAt(xTable.getValueAt(pos-1-i, 1), pos-i, 1);
				model.setValueAt(xTable.getValueAt(pos-1-i, 2), pos-i, 2);
				model.setValueAt(xTable.getValueAt(pos-1-i, 3), pos-i, 3);
				model.setValueAt(xTable.getValueAt(pos-1-i, 4), pos-i, 4);
				model.setValueAt(xTable.getValueAt(pos-1-i, 5), pos-i, 5);
				model.setValueAt(xTable.getValueAt(pos-1-i, 6), pos-i, 6);
			}
		}		
		
		Integer count = xTable.getRowCount();
		KeyNamePair pair = new KeyNamePair(0, count.toString());
		
		String desc = "";
		String ref = "";
		
		KeyNamePair prod = null;
		if(xComboProduct.getSelectedIndex()>0){
			
			desc = xComboProduct.getItemAt(xComboProduct.getSelectedIndex()).toString();
			prod = (KeyNamePair) xComboProduct.getItemAt(xComboProduct.getSelectedIndex());
			MProduct product = new MProduct( Env.getCtx(), prod.getKey(), null);
			X_XX_VMR_VendorProdRef reference = new X_XX_VMR_VendorProdRef( Env.getCtx(), product.getXX_VMR_VendorProdRef_ID(), null);
			ref = reference.getValue();
		}
		
		model.setValueAt(pair, 0, 0);
		model.setValueAt(ref, 0, 1);
		model.setValueAt(desc, 0, 2);
		model.setValueAt(0, 0, 3);
		model.setValueAt(false, 0, 4);	
		model.setValueAt(xCombo.getItemAt(xCombo.getSelectedIndex()), 0, 5);
		model.setValueAt(prod, 0, 6);
		
		statusBar.setStatusDB(xTable.getRowCount());
		
		//Reinicia combo de productos
		xComboProduct.setSelectedIndex(0);
						
	}   //  cmd_AddMotive
	
	
	/**
	 *  loadMotives
	 */
	private void loadMotives()
	{		
		
		String sql = "select VALUE, NAME FROM AD_Ref_List WHERE AD_Reference_ID = 1000349";
		
		sql = MRole.getDefault().addAccessSQL(
				sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + "";		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				xCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));				
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}			
	}   
	
	/**
	 *  loadMotives
	 */
	private void loadProducts()
	{		
		
		String sql = "select b.M_PRODUCT_ID, b.VALUE||' '||b.NAME " +
					 "FROM M_INOUTLINE a, M_PRODUCT b " +
					 "WHERE a.M_PRODUCT_ID = b. M_PRODUCT_ID " +
					 "and a.M_INOUT_ID = " + inOut.get_ID() + " ORDER BY b.VALUE";		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			xComboProduct.addItem(null);
			
			while (rs.next()) {		
				xComboProduct.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));				
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}			
	}   	
	
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{		
		if (e.getValueIsAdjusting())
			return;
	
	}   //  valueChanged
	
	/**************************************************************************
	 *  Initialize Table access - create SQL, dateColumn.
	 */
	private void tableInit (MInOut inOut)
	{	
		m_sql = new StringBuffer ();
	
		if(tableInit_option==0)
		{
			m_sql.append("SELECT  ROWNUM, tab.XX_VLO_NewsReport_ID, NVL(tab.XX_VENDORREFERENCE,' '), NVL(tab.Description,' '), " +
						 "tab.XX_Quantity, tab.XX_NOSENCAMER, b.NAME, tab.XX_NEWSREPORTMOTIVE, ' ', tab.XX_AuxProduct_ID " + 
					     "FROM XX_VLO_NewsReport tab, AD_Ref_List b " +
					     "WHERE XX_NEWSREPORTMOTIVE = b.VALUE AND  AD_Reference_ID = 1000349 AND tab.C_ORDER_ID="+inOut.getC_Order_ID());
					
					m_orderBy = " order by ROWNUM";
		}
		
		
	}   //  tableInit
	
	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{
	//	log.finest(m_sql + " - " +  m_groupBy);
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ m_groupBy + m_orderBy;
		
		log.finest(sql);
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
	
			table.loadTable(rs);
	
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}   //  tableLoad
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
	}

	
	public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer(Vector<KeyNamePair> items) {
	        super(items);
	    }

	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        if (isSelected) {
	            setForeground(table.getSelectionForeground());
	            super.setBackground(table.getSelectionBackground());
	        } else {
	            setForeground(table.getForeground());
	            setBackground(table.getBackground());
	        }

	        // Select the current value
	        setSelectedItem(value);
	        return this;
	    }
	}


	public class ComboBoxEditor extends DefaultCellEditor {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ComboBoxEditor(Vector<KeyNamePair> items) {
	        super(new JComboBox(items));
	    }
	}
	
	private boolean newsReportVendor(MOrder order){
		
		String sql = "SELECT * FROM XX_VLO_NEWSREPORTCOMPANY " +
					 "WHERE ISACTIVE = 'Y' AND C_BPARTNER_ID = " + order.getC_BPartner_ID();		

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {		
				return true;				
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return false;
	}
	
}//End form


