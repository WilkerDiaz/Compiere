package compiere.model.cds.forms;
 
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VFile;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.*;
import org.compiere.model.MRole;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Reporte de Presupuesto Definitivo
 *
 *  @author     dpellegrino
 *  @modificado jtrias
 *  @version    
 */


public class XX_ReportFinalBudgetForm extends CPanel


	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener, Printable
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
		m_frame.setName("Presupuesto");
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
	
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
	static CLogger 			log = CLogger.getCLogger(XX_UnsolicitedProduct_Form.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static String SQL = null;
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	static Ctx             ctx_aux = Env.getCtx();	
	
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
	private TitledBorder xBorder = new TitledBorder(Msg.getMsg(Env.getCtx(), "Budget"));
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	
	DecimalFormat df = new DecimalFormat("#,##0.00");
	int pos = 0;
	
	//private static MOrder     order;	
	private static CComboBox  WarehouseCombo = new CComboBox();
	private static CComboBox  CategoryCombo = new CComboBox();
	private static CComboBox  DepartmentCombo = new CComboBox();
	private static CComboBox  LineaCombo = new CComboBox();
	private static CComboBox  SeccionCombo = new CComboBox();
	private static CComboBox  TypeRecordCombo = new CComboBox();
	
	private static VNumber  year = new VNumber();
	private static VNumber  month = new VNumber();
	static //private static VNumber    product = new VNumber();
	ColumnInfo[] layout2 = new ColumnInfo[3];
	private CLabel Warehouse = new CLabel(Msg.getMsg(Env.getCtx(), "Warehouse"));
	private CLabel Category = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Category"));
	private CLabel Department = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Department"));
	private CLabel Linea = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Line"));
	private CLabel Seccion = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Section"));
	private CLabel TypeRecord = new CLabel(Msg.getMsg(Env.getCtx(), "XX_TypeRecord"));
	private CLabel yearLabel = new CLabel(Msg.getMsg(Env.getCtx(), "Year"));	
	private CLabel monthLabel = new CLabel(Msg.getMsg(Env.getCtx(), "Month"));
	TableCellRenderer cellR = null;
	
	private CButton bSearch = new CButton();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private CButton bNexmonth = new CButton();
	private CButton bPreviousmonth = new CButton();
	private CButton bPrint = ConfirmPanel.createExportButton(false);
	private VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	private Integer mesaux = 0;
	private Integer yearaux = 0;
	Boolean aux = false;
	Boolean aux2 = false;
	
	
	KeyNamePair loadKNP = new KeyNamePair(0, "");
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
	
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bSearch.setPreferredSize(new Dimension(80,22));	
		bSearch.setEnabled(true);
		
		bSearch.addActionListener(this);
		bCancel.addActionListener(this);
		bPrint.addActionListener(this);
		
		bNexmonth.setText(Msg.translate(Env.getCtx(), "NextMonth"));
		bNexmonth.setPreferredSize(new Dimension(115,22));	
		bNexmonth.setEnabled(true);
		bNexmonth.addActionListener(this);
		
		bPreviousmonth.setText(Msg.translate(Env.getCtx(), "PreviousMonth"));
		bPreviousmonth.setPreferredSize(new Dimension(120,22));	
		bPreviousmonth.setEnabled(true);
		bPreviousmonth.addActionListener(this);
		
		xScrollPane.setBorder(xBorder);	
		xScrollPane.setPreferredSize(new Dimension(750, 525));
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	
		xScrollPane.getViewport().add(xTable, null);
		
		northPanel.add(month,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		northPanel.add(yearLabel,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		
		northPanel.add(year,new GridBagConstraints(3,0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		northPanel.add(monthLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		
		northPanel.add(Warehouse,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		
		northPanel.add(WarehouseCombo,        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		
		northPanel.add(Category,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		
		northPanel.add(CategoryCombo,        new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		
		northPanel.add(Department,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 5, 5), 0, 0));
		
		northPanel.add(DepartmentCombo,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
		
		northPanel.add(Linea,     new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 5, 5), 0, 0));
		
		northPanel.add(LineaCombo,    new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
		
		northPanel.add(Seccion,     new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 5, 5), 0, 0));
		
		northPanel.add(SeccionCombo,    new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
		
		northPanel.add(TypeRecord,     new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 5, 5), 0, 0));
		
		northPanel.add(TypeRecordCombo,    new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
					
		northPanel.add(bSearch,   new GridBagConstraints(0, 7, 10, 0, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		
		southPanel.add(bPreviousmonth,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
		
		southPanel.add(bNexmonth,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 80, 10, 0), 0, 0));
		
		southPanel.add(bFile,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 80, 10, 0), 0, 0));
		
		southPanel.add(bPrint,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
		
				
	    southPanel.validate();
   
	
	}   //  jbInit
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{			
		loadInfo();
		DepartmentCombo.setEnabled(false);
		DepartmentCombo.setEditable(false);
		LineaCombo.setEditable(false);
		LineaCombo.setEnabled(false);
		SeccionCombo.setEnabled(false);
		SeccionCombo.setEditable(false);
		
		df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
		
	}   //  dynInit
	
	private void loadInfo()
	{
		year.setValue(null);
		month.setValue(null);
		
		//Carga de combos
		
		loadSto();
		loadCat();
		loadType();
		
		 layout2 = new ColumnInfo[] {
			
			new ColumnInfo(Msg.translate(Env.getCtx(), "Concept"),   ".", String.class),     // 1 
			new ColumnInfo(Msg.translate(Env.getCtx(), ""),   ".", BigDecimal.class),     
			new ColumnInfo(Msg.translate(Env.getCtx(), ""),   ".", BigDecimal.class),     
			new ColumnInfo(Msg.translate(Env.getCtx(), ""),   ".", BigDecimal.class),     
			new ColumnInfo(Msg.translate(Env.getCtx(), ""),   ".", BigDecimal.class),     
			new ColumnInfo(Msg.translate(Env.getCtx(), ""),   ".", BigDecimal.class),     
			new ColumnInfo(Msg.translate(Env.getCtx(), ""),   ".", BigDecimal.class),     
			
		};
		//layout2[1].setColHeader("Hola");
		 
		xTable.prepareTable(layout2, "", "", false, "");
		//xTable.setAutoResizeMode(1);
		xTable.getColumnModel().getColumn(0).setPreferredWidth(260);
		xTable.getColumnModel().getColumn(1).setPreferredWidth(160);
		xTable.getColumnModel().getColumn(2).setPreferredWidth(160);
		xTable.getColumnModel().getColumn(3).setPreferredWidth(160);
		xTable.getColumnModel().getColumn(4).setPreferredWidth(160);
		xTable.getColumnModel().getColumn(5).setPreferredWidth(160);
		xTable.getColumnModel().getColumn(6).setPreferredWidth(160);
		
		cellR = xTable.getColumnModel().getColumn(0).getCellRenderer();
		
		//  Visual
		CompiereColor.setBackground (this);
	
		//  Listener
		xTable.getSelectionModel().addListSelectionListener(this);
		xTable.getModel().addTableModelListener(this);
	
		CategoryCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
	
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {

				if(CategoryCombo.getSelectedIndex()>0){
					loadDep();
					DepartmentCombo.setEditable(true);
					DepartmentCombo.setEnabled(true);
					LineaCombo.removeAllItems();
					LineaCombo.setEditable(false);
					LineaCombo.setEnabled(false);
					SeccionCombo.removeAllItems();
					SeccionCombo.setEditable(false);
					SeccionCombo.setEnabled(false);
				}
				else{
					DepartmentCombo.removeAllItems();
					DepartmentCombo.setEditable(false);
					LineaCombo.removeAllItems();
					LineaCombo.setEditable(false);
					SeccionCombo.removeAllItems();
					SeccionCombo.setEditable(false);
					DepartmentCombo.setEnabled(false);
					LineaCombo.setEnabled(false);
					SeccionCombo.setEnabled(false);
				}	
			}
		});	
		
		DepartmentCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
	
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {
				
				if(DepartmentCombo.getSelectedIndex()>0){
					loadLin();
					LineaCombo.setEditable(true);
					LineaCombo.setEnabled(true);
					SeccionCombo.removeAllItems();
					SeccionCombo.setEditable(false);
					SeccionCombo.setEnabled(false);
					System.out.println("dep");
				}
				else{
					LineaCombo.removeAllItems();
					LineaCombo.setEditable(false);
					SeccionCombo.removeAllItems();
					SeccionCombo.setEditable(false);
					LineaCombo.setEnabled(false);
					SeccionCombo.setEnabled(false);
				}
			}
		});
		
		LineaCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
	
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {
				
				if(LineaCombo.getSelectedIndex()>0){
					loadSec();
					SeccionCombo.setEditable(true);
					SeccionCombo.setEnabled(true);
					System.out.println("lin");
				}
				else{
					SeccionCombo.removeAllItems();
					SeccionCombo.setEditable(false);
					SeccionCombo.setEnabled(false);
				}
			}
		});	
		
		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
	}
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}	//	dispose
	
	/**
	 * Carga del combobox de las tiendas
	 */
	private void loadSto(){
		
		WarehouseCombo.removeAllItems();
		
		String sql = "SELECT M_WAREHOUSE_ID, NAME FROM M_WAREHOUSE ORDER BY VALUE";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			WarehouseCombo.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(2));
				WarehouseCombo.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/**
	 * Carga del combobox de las categorias
	 */
	private void loadCat(){
		
		CategoryCombo.removeAllItems();
		
		String sql = "SELECT XX_VMR_Category_id, NAME, VALUE FROM XX_VMR_Category WHERE ISACTIVE = 'Y' ";
		sql += " order by value";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			CategoryCombo.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(3) +"-"+  rs.getString(2));
				CategoryCombo.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/**
	 * Carga del combobox de los departamentos
	 */
	private void loadDep(){
		
		DepartmentCombo.removeAllItems();
		
		String sql = "SELECT DISTINCT XX_VMR_DEPARTMENT_ID, NAME, VALUE FROM XX_VMR_DEPARTMENT WHERE ISACTIVE = 'Y'";
		
		if(CategoryCombo.getSelectedIndex()>0){
			int clave_Category =((KeyNamePair)CategoryCombo.getSelectedItem()).getKey();
			sql += " AND XX_VMR_CATEGORY_ID=" + clave_Category;
		}
		
		sql += " order by value";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			DepartmentCombo.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(3) +"-"+ rs.getString(2));
				DepartmentCombo.addItem(loadKNP);//agrego los codigos de los proveedores
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}
	
	
	/**
	 * Carga el combobox de las lineas
	 */
	private void loadLin(){
		
		LineaCombo.removeAllItems();
		
		String sql = "SELECT XX_VMR_Line_id, NAME,VALUE FROM XX_VMR_Line WHERE ISACTIVE = 'Y' ";
		
		if(DepartmentCombo.getSelectedIndex()>0){
			int clave_dep=((KeyNamePair)DepartmentCombo.getSelectedItem()).getKey();
			sql += " AND XX_VMR_Department_ID = " +clave_dep;
		}
		
		sql += " order by value";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			LineaCombo.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(3) +"-"+  rs.getString(2));
				LineaCombo.addItem(loadKNP);//agrego los codigos de los proveedores
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	
	/**
	 * Carga del combobox de las secciones
	 */
	private void loadSec(){
		
		SeccionCombo.removeAllItems();
		
		String sql = "SELECT XX_VMR_Section_id, NAME, VALUE FROM XX_VMR_Section WHERE ISACTIVE = 'Y' ";
		
		if(LineaCombo.getSelectedIndex()>0){
			int clave_lin =((KeyNamePair)LineaCombo.getSelectedItem()).getKey();
			sql += " AND XX_VMR_Line_ID=" + clave_lin;
		}
		
		sql += " order by value";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			SeccionCombo.addItem(loadKNP);
			
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1),rs.getString(3) +"-"+  rs.getString(2));
				SeccionCombo.addItem(loadKNP);//agrego los codigos de los proveedores
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	
	private void loadType(){
		
		TypeRecordCombo.removeAllItems();
		
		loadKNP = new KeyNamePair(1, new String("Bolívares"));
		TypeRecordCombo.addItem(loadKNP);
		loadKNP = new KeyNamePair(2, new String("Piezas"));
		TypeRecordCombo.addItem(loadKNP);
		loadKNP = new KeyNamePair(3, new String("Rotación/Cobertura"));
		TypeRecordCombo.addItem(loadKNP);
		//loadKNP = new KeyNamePair(4, new String("Rebajas/Margen")); MOMENTANEAMENTE NO DISPONIBLE TODO
		//TypeRecordCombo.addItem(loadKNP);
	}
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSearch){	
			cmd_Search();
		}
		else if (e.getSource() == bCancel)
			dispose();
		else if (e.getSource() == bPreviousmonth)
			cmd_Previousmonth();
		else if (e.getSource() == bNexmonth)
			cmd_Nexmonth();
		else if(e.getSource() == bPrint)
			try {
				createCSV();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		
	}   //  actionPerformed
	
	 
	private void cmd_Search()
	{		
		if((month.getValue()!= null) & (year.getValue()!= null)){
			
			aux = true;
			mesaux = 0;
			yearaux = 0;
			aux2 = false;
			
			DecimalFormat formato = new DecimalFormat("00");
			Integer mes = new Integer (formato.format(month.getValue()));
			Integer years = new Integer (formato.format(year.getValue()));
			 
			mesaux = mes;
			yearaux = years;
			
			String myMonthAux = mes.toString();	
			
			if(myMonthAux.length()==1)
				myMonthAux = "0"+mes.toString();
			
			String myYearAux = years.toString();
			
			if(mes<=12)
			{	
			
				if(mes == 1)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Enero-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Febrero-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Marzo-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Abril-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Mayo-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Junio-"+years);
				}
				else if(mes == 2)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Febrero-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Marzo-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Abril-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Mayo-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Junio-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Julio-"+years);
				}
				else if(mes == 3)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Marzo-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Abril-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Mayo-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Junio-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Julio-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Agosto-"+years);
				}
				else if(mes == 4)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Abril-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Mayo-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Junio-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Julio-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Agosto-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Septiembre-"+years);
				}
				else if(mes == 5)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Mayo-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Junio-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Julio-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Agosto-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Septiembre-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Octubre-"+years);
				}
				else if(mes == 6)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Junio-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Julio-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Agosto-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Septiembre-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Octubre-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Noviembre-"+years);
				}
				else if(mes == 7)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Julio-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Agosto-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Septiembre-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Octubre-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Noviembre-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Diciembre-"+years);
				}
				else if(mes == 8)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Agosto-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Septiembre-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Octubre-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Noviembre-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Diciembre-"+years);
					years++;
					xTable.getColumnModel().getColumn(6).setHeaderValue("Enero-"+years);
				}
				else if(mes == 9)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Septiembre-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Octubre-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Noviembre-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Diciembre-"+years);
					years++;
					xTable.getColumnModel().getColumn(5).setHeaderValue("Enero-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Febrero-"+years);
				}
				else if(mes == 10)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Octubre-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Noviembre-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Diciembre-"+years);
					years++;
					xTable.getColumnModel().getColumn(4).setHeaderValue("Enero-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Febrero-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Marzo-"+years);
				}
				else if(mes == 11)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Noviembre-"+years);
					xTable.getColumnModel().getColumn(2).setHeaderValue("Diciembre-"+years);
					years = years +1;
					xTable.getColumnModel().getColumn(3).setHeaderValue("Enero-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Febrero-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Marzo-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Abril-"+years);
				}
				else if(mes == 12)
				{
					xTable.getColumnModel().getColumn(1).setHeaderValue("Diciembre-"+years);
					years = years +1;
					xTable.getColumnModel().getColumn(2).setHeaderValue("Enero-"+years);
					xTable.getColumnModel().getColumn(3).setHeaderValue("Febrero-"+years);
					xTable.getColumnModel().getColumn(4).setHeaderValue("Marzo-"+years);
					xTable.getColumnModel().getColumn(5).setHeaderValue("Abril-"+years);
					xTable.getColumnModel().getColumn(6).setHeaderValue("Mayo-"+years);
				}
				
				
				if( ((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==1 || ((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==2 ){
					
					//Pintando de colores las celdas
					xTable.getColumnModel().getColumn(0).setCellRenderer(new XX_BudgetRenderer());	
					xTable.getColumnModel().getColumn(1).setCellRenderer(new XX_BudgetRenderer());	
					xTable.getColumnModel().getColumn(2).setCellRenderer(new XX_BudgetRenderer());
					xTable.getColumnModel().getColumn(3).setCellRenderer(new XX_BudgetRenderer());	
					xTable.getColumnModel().getColumn(4).setCellRenderer(new XX_BudgetRenderer());	
					xTable.getColumnModel().getColumn(5).setCellRenderer(new XX_BudgetRenderer());
					xTable.getColumnModel().getColumn(6).setCellRenderer(new XX_BudgetRenderer());
				}
				else{
					xTable.getColumnModel().getColumn(0).setCellRenderer(cellR);	
					xTable.getColumnModel().getColumn(1).setCellRenderer(cellR);	
					xTable.getColumnModel().getColumn(2).setCellRenderer(cellR);
					xTable.getColumnModel().getColumn(3).setCellRenderer(cellR);	
					xTable.getColumnModel().getColumn(4).setCellRenderer(cellR);	
					xTable.getColumnModel().getColumn(5).setCellRenderer(cellR);
					xTable.getColumnModel().getColumn(6).setCellRenderer(cellR);
				}
				
				TableModel model = xTable.getModel();
				xTable.setModel(model);
							
				Vector<String> names = getNames();
				Vector<BigDecimal> month1 = getMonth(myYearAux+myMonthAux);
				Vector<BigDecimal> month2 = getMonth(getNextYearMonth(myYearAux+myMonthAux));
				Vector<BigDecimal> month3 = getMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux)));
				Vector<BigDecimal> month4 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux))));
				Vector<BigDecimal> month5 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux)))));
				Vector<BigDecimal> month6 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux))))));
					
				//Llenado de tabla		
				for(int row=0; row<names.size(); row++){
						
					xTable.setRowCount(row+1);
						
					model.setValueAt(names.get(row), row, 0);
					
					if(month1.get(row)!=null)
						model.setValueAt(df.format(month1.get(row)), row, 1);
					else
						model.setValueAt( 0, row, 1);
						
					if(month2.get(row)!=null)
						model.setValueAt(df.format(month2.get(row)), row, 2);
					else
						model.setValueAt( 0, row, 2);
					
					if(month3.get(row)!=null)
						model.setValueAt(df.format(month3.get(row)), row, 3);						
					else
						model.setValueAt( 0, row, 3);
						
					if(month4.get(row)!=null)
						model.setValueAt(df.format(month4.get(row)), row, 4);
					else
						model.setValueAt( 0, row, 4);
					
					if(month5.get(row)!=null)
						model.setValueAt(df.format(month5.get(row)), row, 5);
					else
						model.setValueAt( 0, row, 5);
					
					if(month6.get(row)!=null)
						model.setValueAt(df.format(month6.get(row)), row, 6);
					else
						model.setValueAt( 0, row, 6);
				}	
				
				m_frame.repaint();
				
				if(xTable.getRowCount()>0){
					//bSave.setEnabled(false);
					statusBar.setStatusLine("Records");
					statusBar.setStatusDB(xTable.getRowCount());				
				}
				else{
					//bSave.setEnabled(false);
					ADialog.info(m_WindowNo, this.mainPanel, "Not found data");
					statusBar.setStatusLine("Not found products");
				}
			}// end if valida mes.
			else
			{
				System.out.println("Mes no valido");
				xScrollPane.getViewport().remove(xTable);
				xTable = new MiniTable();
				xScrollPane.getViewport().add(xTable, null);
				statusBar.setStatusLine("No se encontraron registros");	
				statusBar.setStatusDB(0);
				loadInfo();
				ADialog.info(m_WindowNo, this.mainPanel, "Mes no valido.");
				year.setValue(null);
				month.requestFocus();
			}
				
		}
		else{
			
			xScrollPane.getViewport().remove(xTable);
			xTable = new MiniTable();
			xScrollPane.getViewport().add(xTable, null);
			statusBar.setStatusLine("No se encontraron registros");
			statusBar.setStatusDB(0);
			loadInfo();
			ADialog.info(m_WindowNo, this.mainPanel, "Indique Mes y Año");
			year.setValue(null);	
			month.requestFocus();
		}
		
	}   //  cmd_Search
	
	private String getNextYearMonth(String yearMonth_Actual)
	{
		Integer year = new Integer (yearMonth_Actual.substring(0, 4));
		Integer month = new Integer (yearMonth_Actual.substring(4, 6));
		
		if(yearMonth_Actual.substring(4, 6).equals("12")){
			year = year + 1;
			month = 1;
		}
		else{
			month = month + 1;
		}
		
		String myMonthAux = month.toString();	
		
		if(myMonthAux.length()==1)
			myMonthAux = "0"+month.toString();
		
		return year.toString() + myMonthAux;
		
	}
	
	private String getPastYearMonth(String yearMonth_Actual)
	{
		Integer year = new Integer (yearMonth_Actual.substring(0, 4));
		Integer month = new Integer (yearMonth_Actual.substring(4, 6));
		
		if(yearMonth_Actual.substring(4, 6).equals("01")){
			year = year - 1;
			month = 12;
		}
		else{
			month = month - 1;
		}
		
		String myMonthAux = month.toString();	
		
		if(myMonthAux.length()==1)
			myMonthAux = "0"+month.toString();
		
		return year.toString() + myMonthAux;
		
	}
	
	private Vector<String> getNames()
	{
		Vector<String> names = new Vector<String>();
		
		if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==1 || ((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==2){
			
			names.add("Inventario Inicial Presupuestado");
			names.add("Inventario Inicial Real");
			names.add("Compras Presupuestadas");
			names.add("Compras Colocadas Total");
			names.add("Compras Colocadas Nacional");
			names.add("Compras Colocadas Importadas");
			names.add("Compras Colocadas Mes Anterior");
			names.add("Compras Recibidas Total");
			names.add("Compras Chequeadas Total");
			names.add("Compras Chequeadas Nacional");
			names.add("Compras Chequeadas Importadas");
			names.add("Compras Chequeadas Mes Anterior");
			names.add("Ventas Presupuestadas");
			names.add("Ventas Reales");
			names.add("Traspasos/Pedidos Enviados");
			names.add("Traspasos/Pedidos Recibidos");
			names.add("Devoluciones");
			names.add("Rebajas Total Presupuestada");
			names.add("Rebajas Total Real");
			names.add("Rebajas Promociónales Presupuestadas");
			names.add("Rebajas Promociónales Real");
			names.add("Rebajas FR Presupuestadas");
			names.add("Rebajas FR Real");
			names.add("Rebajas Definitivas Presupuestadas");
			names.add("Rebajas Definitivas Real");
			names.add("Inventario Final Presupuestado");
			names.add("Inventario Final Real");
			names.add("Inventario Final Proyectado");
			names.add("Límite de Compras");
		}
		else if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==3){
			
			names.add("Rotación Presupuestada");
			names.add("Rotación Real");
			names.add("Cobertura Presupuestada");
			names.add("Cobertura Real");
		}
		else if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==4){
			
			names.add("Margen Bruto Ganado Real");
			names.add("Margen Según Compra de la Venta");
			names.add("Margen Neto Real");
			names.add("Margen Por Ganar Real");
		}
	
		return names; 
	}
	
	private Vector<BigDecimal> getMonth(String monthAux)
	{
		Vector<BigDecimal> month = new Vector<BigDecimal>();
		int howManyRows = 0;
		String SQL = "";
		String pastYearMonth = getPastYearMonth(monthAux);
		
		//Bolivares
		if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==1){
			
			howManyRows = 29;
			SQL = "select " +
					 "SUM(XX_INVEFECBUDGETEDAMOUNT) Inv_Inicial_Pres," +
					 "SUM(XX_AMOUNTINIINVEREAL) Inv_Inicial_Real," +
					 "SUM(XX_PURCHAMOUNTBUDGETED) Compras_Pres," +
					 "SUM(XX_AMOUNTPLACEDNACPURCHCOST+XX_PURCHAMOUNTPLACEDCOSTIMP+XX_PURCHAMOUNTPLADPASTMONTHS) Compras_Col," +
					 "SUM(XX_AMOUNTPLACEDNACPURCHCOST) Compras_Col_Nac," +
					 "SUM(XX_PURCHAMOUNTPLACEDCOSTIMP) Compras_Col_Imp," +
					 "SUM(XX_PURCHAMOUNTPLADPASTMONTHS) Comp_Col_Mes_Ant," +
					 "SUM(XX_ReceiptPVP) Rec_PVP," +
					 "SUM(NVL(XX_NACPURCHAMOUNTRECEIVED, 0) + NVL(XX_PURCHAMOUNTREVIMPD, 0) " +
					   "+ NVL(XX_PURCHAMOUNTREDPASTMONTHS, 0) + NVL(XX_PlacedOrderPVPAdjustment, 0)) Com_CHEQ_Total," +
					 "SUM(XX_NACPURCHAMOUNTRECEIVED) Comp_CHEQ_Nacio," +
					 "SUM(XX_PURCHAMOUNTREVIMPD) Comp_CHEQ_Imp," +
					 "SUM(XX_PURCHAMOUNTREDPASTMONTHS) Comp_CHEQ_Mes_Ant," +
					 "SUM(XX_SALESAMOUNTBUD2) Ventas_Presupu," +
					 "SUM(XX_AMOUNTACTUALSALE) Ventas_Reales," +
					 "SUM(XX_TRANSFAMOUNTSENT) Traspasos_Enviados," +
					 "SUM(XX_TRANSFAMOUNTRECEIVED) Traspasos_Recibidos," +
					 "SUM(XX_RETURNSPVP) Dev_PVP," +
					 "SUM(XX_PROMSALEAMOUNTBUD + XX_AMOUNTSALEFRBUD + XX_FINALSALEAMOUNTBUD) Rebajas_Total_Presu," +
					 "SUM(XX_ACTAMOUNTSALEPROM + XX_ACTAMOUNTSALEFR + XX_FINALACTAMOUNTSALE) Rebajas_Total_Real," +
					 "SUM(XX_PROMSALEAMOUNTBUD) Reb_Promoci_Presup," +
					 "SUM(XX_ACTAMOUNTSALEPROM) Reb_Promoci_Real," +
					 "SUM(XX_AMOUNTSALEFRBUD) Reb_FR_Presu," +
					 "SUM(XX_ACTAMOUNTSALEFR) Reb_FR_Real," +
					 "SUM(XX_FINALSALEAMOUNTBUD) Reb_Def_Pres," +
					 "SUM(XX_FINALACTAMOUNTSALE) Reb_Def_Real," +
					 "SUM(XX_FINALINVAMOUNTBUD2) Inventa_Final_Presu," +
					 "SUM(XX_INVAMOUNTFINALREAL) Inventa_Final_Real," +
					 "SUM(XX_FINALINVAMOUNTPROJD) Inventa_Final_Proy," +
					 "SUM(XX_AMOUNTPURCHASELIMIT) Limite_de_Compras ";
			
			SQL += "from xx_vmr_prld01 B " +
			   "where B.XX_BUDGETYEARMONTH = " + monthAux + " ";
		}
		else if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==2) //Piezas
		{
			howManyRows = 29;
			SQL = "select " +
				  "SUM(XX_INVAMOUNTORIGBUDGETED) Inv_Inicial_Pres," +
				  "SUM(XX_NUMINIINVEREAL) Inv_Inicial_Real," +
				  "SUM(XX_QUANTBUDGETEDSHOPPING) Compras_Pres," +
				  "SUM(XX_NUMNACSHOPPINGPLACED+XX_PURCHQUANTIMPDPLACED+XX_PURCHQUANTPLACEDMONTHS) Compras_Col," +
				  "SUM(XX_NUMNACSHOPPINGPLACED) Compras_Col_Nac," +
				  "SUM(XX_PURCHQUANTIMPDPLACED) Compras_Col_Imp," +
				  "SUM(XX_PURCHQUANTPLACEDMONTHS) Comp_Col_Mes_Ant," +
				  "SUM(XX_ReceiptQTY) Rec_Qty," +
				  "SUM(XX_QUANTPURCHNAC+XX_QUANTPURCHAMOUNTSREV+XX_NUMMONTHSREDSHOP) Com_CHEQ_Total," +
				  "SUM(XX_QUANTPURCHNAC) Comp_CHEQ_Nacio," +
				  "SUM(XX_QUANTPURCHAMOUNTSREV) Comp_CHEQ_Imp," +
				  "SUM(XX_NUMMONTHSREDSHOP) Comp_CHEQ_Mes_Ant," +
				  "SUM(XX_SALESAMOUNTBUD) Ventas_Presupu," +
			      "SUM(XX_QUANTACTUALSALE) Ventas_Reales," +
				  "SUM(XX_NUMTRANSFSENT) Traspasos_Enviados," +
				  "SUM(XX_NUMBTRANSFREV) Traspasos_Recibidos," +
				  "SUM(XX_RETURNSQTY) Dev_Qty," +
				  "SUM(XX_PROMSALENUMBUD + XX_BUDAMOUNTFRSALE+ XX_FINALBUDAMOUNTSALE) Rebajas_Total_Presu," +
				  "SUM(XX_AMOUNTSALEPROMINTERESTS + XX_AMOUNTSALEFRINTERESTS +  XX_FINALSALEAMOUNTINTERESTS) Rebajas_Total_Real," +
				  "SUM(XX_PROMSALENUMBUD) Reb_Promoci_Presup," +
				  "SUM(XX_AMOUNTSALEPROMINTERESTS) Reb_Promoci_Real," +
				  "SUM(XX_BUDAMOUNTFRSALE) Reb_FR_Presu," +
				  "SUM(XX_AMOUNTSALEFRINTERESTS) Reb_FR_Real," +
				  "SUM(XX_FINALBUDAMOUNTSALE) Reb_Def_Pres," +
				  "SUM(XX_FINALSALEAMOUNTINTERESTS) Reb_Def_Real," +
				  "SUM(XX_FINALINVAMOUNTBUD) Inventa_Final_Presu," +
				  "SUM(XX_NUMREALFINALINV) Inventa_Final_Real," +
				  "SUM(XX_NUMPROJDFINALINV) Inventa_Final_Proy," +
				  "SUM(XX_QUANTITYPURCHLIMIT) Limite_de_Compras ";
		
			SQL += "from xx_vmr_prld01 B " +
			   "where B.XX_BUDGETYEARMONTH = " + monthAux + " ";
		}
		else if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==3) //Rotacion
		{
			howManyRows = 4;
			
			SQL = "WITH TABLA as (SELECT * FROM XX_VMR_PRLD01 A WHERE  A.XX_BUDGETYEARMONTH=" + pastYearMonth + ")" +
				  "select " +

				  "ROUND(CASE WHEN sum(NVL(past.XX_FINALINVAMOUNTBUD2,0) + NVL(p.XX_FINALINVAMOUNTBUD2,0)) = 0 THEN 0 " +
				  "ELSE (sum(NVL(p.XX_SALESAMOUNTBUD2,0)) * 12) / (sum(NVL(past.XX_FINALINVAMOUNTBUD2,0) + NVL(p.XX_FINALINVAMOUNTBUD2,0))/2) END,2) ROTACION_PRES, " +

				  "ROUND(CASE WHEN sum(NVL(past.XX_INVAMOUNTFINALREAL,0) + NVL(p.XX_INVAMOUNTFINALREAL,0)) = 0 THEN 0 " +
				  "ELSE (sum(NVL(p.XX_AmountActualSale,0)) * 12) / (sum(NVL(past.XX_INVAMOUNTFINALREAL,0) + NVL(p.XX_INVAMOUNTFINALREAL,0))/2) END,2) ROTACION_REAL, " +
				  
				  "NVL(ROUND(CASE WHEN SUM(p.XX_SALESAMOUNTBUD2) <> 0 THEN SUM(p.XX_INVEFECBUDGETEDAMOUNT)/SUM(p.XX_SALESAMOUNTBUD2) ELSE 0 END, 2), 0) Cober_Pres, " +
				  "NVL(ROUND(CASE WHEN SUM(p.XX_AmountActualSale) <> 0 THEN SUM(p.XX_AMOUNTINIINVEREAL)/SUM(p.XX_AmountActualSale) ELSE 0 END, 2), 0) Cober_Real " +
				  
				  "FROM XX_VMR_PRLD01 p, TABLA past " +
				  "WHERE p.XX_BUDGETYEARMONTH = "+ monthAux + " " + 
				  "AND p.M_WareHouse_ID = past.M_WareHouse_ID and p.XX_VMR_Category_ID = past.XX_VMR_Category_ID " +
				  "AND p.XX_VMR_Department_ID = past.XX_VMR_Department_ID AND p.XX_VMR_LINE_ID = past.XX_VMR_LINE_ID " +
				  "AND p.XX_VMR_Section_ID = past.XX_VMR_Section_ID";
			
		}
		else if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==4) //Margen
		{
			howManyRows = 4;
			
			SQL = "select " +
				  "ROUND(case when  sum(XX_AMOUNTACTUALSALE) <> 0 then( sum(XX_AMOUNTACTUALSALE - XX_AMOUNTSALECOST) / sum(XX_AMOUNTACTUALSALE)) * 100 else 0 end, 2) MargenBrutoGanado, " +
				  "0," +
				  "0," +
				  //"ROUND(case when  sum(XX_AMOUNTACTUALSALE_NET) <> 0 then( sum(XX_AMOUNTACTUALSALE_NET - XX_AMOUNTSALECOST) / sum(XX_AMOUNTACTUALSALE_NET)) * 100 else 0 end, 2) MargenCompraVenta, " +
				  "ROUND(case when sum(XX_AMOUNTINIINVEREAL) <> 0 then (sum(XX_AMOUNTINIINVEREAL - XX_INIAMOUNTINVECOST) / sum(XX_AMOUNTINIINVEREAL)) * 100 else 0 end, 2) MargenPorGanar " +
				  "from xx_vmr_prld01 where " +
				  "XX_BUDGETYEARMONTH = " + monthAux + " " ;	
		}
						
					 
		if(WarehouseCombo.getSelectedIndex()>0){
			int clave_Warehouse=((KeyNamePair)WarehouseCombo.getSelectedItem()).getKey();
			SQL += " AND M_Warehouse_ID=" +clave_Warehouse;
		 }
			
		if(CategoryCombo.getSelectedIndex()>0){
			int clave_Category =((KeyNamePair)CategoryCombo.getSelectedItem()).getKey();
			SQL += " AND XX_VMR_CATEGORY_ID=" + clave_Category;
		}
			
		if(DepartmentCombo.getSelectedIndex()>0){
			int clave_Department =((KeyNamePair)DepartmentCombo.getSelectedItem()).getKey();
			SQL += " AND XX_VMR_DEPARTMENT_ID=" +clave_Department;
		}
			
		if(LineaCombo.getSelectedIndex()>0){
			int clave_Linea =((KeyNamePair)LineaCombo.getSelectedItem()).getKey();
			SQL += " AND XX_VMR_LINE_ID = " +clave_Linea;
		}
			
		if(SeccionCombo.getSelectedIndex()>0){
			int clave_Seccion =((KeyNamePair)SeccionCombo.getSelectedItem()).getKey();
			SQL += " AND XX_VMR_SECTION_ID=" + clave_Seccion;
		}
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				for(int i=1; i<=howManyRows; i++){					
					month.add(rs.getBigDecimal(i));
				}
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		return month; 
	}
	
	private void cmd_Nexmonth()
	{
		if(mesaux!=0 && yearaux != 0){
		
			if(mesaux==12){
				mesaux = 1;
				yearaux = yearaux + 1;
			}
			else{
				mesaux = mesaux + 1;
			}
			
			int yearaux2=0;

			if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==3 || ((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==4){
				tableInit();
				tableLoad (xTable);
			}
			else{
				
				String myMonthAux = mesaux.toString();	
				
				if(myMonthAux.length()==1)
					myMonthAux = "0"+mesaux.toString();
				
				String myYearAux = yearaux.toString();
				
				TableModel model = xTable.getModel();
				xTable.setModel(model);
						
				Vector<String> names = getNames();
				Vector<BigDecimal> month1 = getMonth(myYearAux+myMonthAux);
				Vector<BigDecimal> month2 = getMonth(getNextYearMonth(myYearAux+myMonthAux));
				Vector<BigDecimal> month3 = getMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux)));
				Vector<BigDecimal> month4 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux))));
				Vector<BigDecimal> month5 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux)))));
				Vector<BigDecimal> month6 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux))))));
				
				
				//Llenado de tabla
				for(int row=0; row<names.size(); row++){
					
					xTable.setRowCount(row+1);
					
					model.setValueAt(names.get(row), row, 0);
					model.setValueAt(month1.get(row), row, 1);
					model.setValueAt(month2.get(row), row, 2);
					model.setValueAt(month3.get(row), row, 3);	
					model.setValueAt(month4.get(row), row, 4);
					model.setValueAt(month5.get(row), row, 5);
					model.setValueAt(month6.get(row), row, 6);
				}
			}
			
			if(mesaux == 1)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Enero-" + yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Febrero-" + yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Marzo-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Junio-"+ yearaux);
			}
			else if(mesaux == 2)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Febrero-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Marzo-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Julio-"+ yearaux);
			}
			else if(mesaux == 3)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Marzo-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Agosto-"+ yearaux);
			}
			else if(mesaux == 4)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Septiembre-"+ yearaux);
			}
			else if(mesaux == 5)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Octubre-"+ yearaux);
			}
			else if(mesaux == 6)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Octubre-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Noviembre-"+ yearaux);
			}
			else if(mesaux == 7)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Octubre-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Noviembre-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Diciembre-"+ yearaux);
			}
			else if(mesaux == 8)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Octubre-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Noviembre-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Diciembre-"+ yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(6).setHeaderValue("Enero-"+ yearaux2);
			}
			else if(mesaux == 9)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Octubre-"+yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Noviembre-"+yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Diciembre-"+yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(5).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Febrero-"+ yearaux2);
			}
			else if(mesaux == 10)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Octubre-"+yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Noviembre-"+yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Diciembre-"+yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(4).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Febrero-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Marzo-"+ yearaux2);
			}
			else if(mesaux == 11)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Noviembre-"+yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Diciembre-"+yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(3).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Febrero-"+ yearaux2);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Marzo-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Abril-"+ yearaux2);
			}
			else if(mesaux == 12)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Diciembre-"+ yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(2).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Febrero-"+ yearaux2);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Marzo-"+ yearaux2);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Abril-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Mayo-"+ yearaux2);
			}
			
			m_frame.repaint();
		}
		
	} //cmd_Nexmonth
	

	private void cmd_Previousmonth()
	{
		if(mesaux!=0 && yearaux != 0){
			
			if(mesaux==1){
				mesaux = 12;
				yearaux = yearaux - 1;
			}
			else{
				mesaux = mesaux - 1;
			}
			
			int yearaux2=0;

			if(((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==3 || ((KeyNamePair)TypeRecordCombo.getSelectedItem()).getKey()==4){
				tableInit();
				tableLoad (xTable);
			}
			else{
				
				String myMonthAux = mesaux.toString();	
				
				if(myMonthAux.length()==1)
					myMonthAux = "0"+mesaux.toString();
				
				String myYearAux = yearaux.toString();
				
				TableModel model = xTable.getModel();
				xTable.setModel(model);
						
				Vector<String> names = getNames();
				Vector<BigDecimal> month1 = getMonth(myYearAux+myMonthAux);
				Vector<BigDecimal> month2 = getMonth(getNextYearMonth(myYearAux+myMonthAux));
				Vector<BigDecimal> month3 = getMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux)));
				Vector<BigDecimal> month4 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux))));
				Vector<BigDecimal> month5 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux)))));
				Vector<BigDecimal> month6 = getMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(getNextYearMonth(myYearAux+myMonthAux))))));
				
				
				//Llenado de tabla
				for(int row=0; row<names.size(); row++){
					
					xTable.setRowCount(row+1);
					
					model.setValueAt(names.get(row), row, 0);
					model.setValueAt(month1.get(row), row, 1);
					model.setValueAt(month2.get(row), row, 2);
					model.setValueAt(month3.get(row), row, 3);	
					model.setValueAt(month4.get(row), row, 4);
					model.setValueAt(month5.get(row), row, 5);
					model.setValueAt(month6.get(row), row, 6);	
				}
			}
			
			if(mesaux == 1)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Enero-" + yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Febrero-" + yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Marzo-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Junio-"+ yearaux);
			}
			else if(mesaux == 2)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Febrero-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Marzo-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Julio-"+ yearaux);
			}
			else if(mesaux == 3)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Marzo-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Agosto-"+ yearaux);
			}
			else if(mesaux == 4)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Abril-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Septiembre-"+ yearaux);
			}
			else if(mesaux == 5)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Mayo-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Octubre-"+ yearaux);
			}
			else if(mesaux == 6)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Junio-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Octubre-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Noviembre-"+ yearaux);
			}
			else if(mesaux == 7)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Julio-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Octubre-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Noviembre-"+ yearaux);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Diciembre-"+ yearaux);
			}
			else if(mesaux == 8)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Agosto-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Octubre-"+ yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Noviembre-"+ yearaux);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Diciembre-"+ yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(6).setHeaderValue("Enero-"+ yearaux2);
			}
			else if(mesaux == 9)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Septiembre-"+ yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Octubre-"+yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Noviembre-"+yearaux);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Diciembre-"+yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(5).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Febrero-"+ yearaux2);
			}
			else if(mesaux == 10)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Octubre-"+yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Noviembre-"+yearaux);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Diciembre-"+yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(4).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Febrero-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Marzo-"+ yearaux2);
			}
			else if(mesaux == 11)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Noviembre-"+yearaux);
				xTable.getColumnModel().getColumn(2).setHeaderValue("Diciembre-"+yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(3).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Febrero-"+ yearaux2);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Marzo-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Abril-"+ yearaux2);
			}
			else if(mesaux == 12)
			{
				xTable.getColumnModel().getColumn(1).setHeaderValue("Diciembre-"+ yearaux);
				yearaux2 = yearaux + 1;
				xTable.getColumnModel().getColumn(2).setHeaderValue("Enero-"+ yearaux2);
				xTable.getColumnModel().getColumn(3).setHeaderValue("Febrero-"+ yearaux2);
				xTable.getColumnModel().getColumn(4).setHeaderValue("Marzo-"+ yearaux2);
				xTable.getColumnModel().getColumn(5).setHeaderValue("Abril-"+ yearaux2);
				xTable.getColumnModel().getColumn(6).setHeaderValue("Mayo-"+ yearaux2);
			}
			
			m_frame.repaint();
		}

	} //cmd_Nexmonth
	

	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{	
	}   //  valueChanged
	

	/**************************************************************************
	 *  Initialize Table access - create SQL, dateColumn.
	 *  <br>
	 *  The driving table is "hdr", e.g. for hdr.C_BPartner_ID=..
	 *  The line table is "lin", e.g. for lin.M_Product_ID=..
	 *  You use the dateColumn/qtyColumn variable directly as it is table specific.
	 *  <br>
	 *  The sql is dependent on MatchMode:
	 *  - If Matched - all (fully or partially) matched records are listed
	 *  - If Not Matched - all not fully matched records are listed
	 *  @param display (Invoice, Shipment, Order) see MATCH_*
	 *  @param matchToType (Invoice, Shipment, Order) see MATCH_*
	 */
	private void tableInit ()
	{
		

	}   //  tableInit


	public static Double divcero(String yearmonth)
	{
		Double INVPROM = new Double(0);
		Double part =  new Double(0);
		Double PORROTPRETEMP = new Double(0);
	
		
		
			SQL  =  ("SELECT SUM(XX_SALESAMOUNTBUD2 * 12) AS PART, SUM((XX_INVEFECBUDGETEDAMOUNT + XX_FINALINVAMOUNTBUD2)/2) AS PART1 FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH = '"+yearmonth+"' ");  //) AS MES1, " +
		
			if(WarehouseCombo.getSelectedIndex()>0){
				int clave_Warehouse=((KeyNamePair)WarehouseCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("M_Warehouse_ID=").append(clave_Warehouse);
				SQL += (" AND M_Warehouse_ID='"+clave_Warehouse+"' ");
			}
			
			if(CategoryCombo.getSelectedIndex()>0){
				int clave_Category =((KeyNamePair)CategoryCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_CATEGORY_ID=").append(clave_Category);
				SQL += (" AND XX_VMR_CATEGORY_ID='"+clave_Category+"' ");
			}
			
			if(DepartmentCombo.getSelectedIndex()>0){
				int clave_Department =((KeyNamePair)DepartmentCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_DEPARTMENT_ID=").append(clave_Department);
				SQL += (" AND XX_VMR_DEPARTMENT_ID='"+clave_Department+"' ");
			}
			
			if(LineaCombo.getSelectedIndex()>0){
				int clave_Linea =((KeyNamePair)LineaCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_LINE_ID=").append(clave_Linea);
				SQL += (" AND XX_VMR_LINE_ID='"+clave_Linea+"' ");
			}
			
			if(SeccionCombo.getSelectedIndex()>0){
				int clave_Seccion =((KeyNamePair)SeccionCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_LINE_ID=").append(clave_Seccion);
				SQL += (" AND XX_VMR_SECTION_ID='"+clave_Seccion+"' ");
			}
			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			    ResultSet rs = pstmt.executeQuery();
			    
			    if(rs.next())
			    {
			    	part = rs.getDouble("PART");
			    	INVPROM = rs.getDouble("PART1");
			    				    	
			    	//if(INVPROM.compareTo(new BigDecimal(0)) == 0)
			    	if(INVPROM == 0) 
			    	{
			    		//System.out.println("menos a cero");
			    		//System.out.println(part);
			    		PORROTPRETEMP = new Double(0);
			    	}
			    	else
			    	{
			    		//System.out.println("mayor a cero");
			    		//PORROTPRETEMP = part.divide(INVPROM, 3, 4);
			    		PORROTPRETEMP = part/INVPROM;
			    		//System.out.println(PORROTPRETEMP);
			    	}
			    }
			    pstmt.close();
			    rs.close();
			}
			catch (Exception e) {
			}

		 return PORROTPRETEMP;
	}
	
	public static Double divcero2 (String yearmonth)
	{
		Double total = new Double(0);
		Double part =  new Double(0);
		Double part1 = new Double(0);
		Double part2 = new Double(0);
	
		 //XX_SALESAMOUNTBUD2 rebajasMargenDinamico
		 
		//SUM(XX_PROMSALEAMOUNTBUD + XX_AMOUNTSALEFRBUD + XX_FINALSALEAMOUNTBUD)/2)*100
		
			SQL  =  ("SELECT SUM(XX_PROMSALEAMOUNTBUD + XX_AMOUNTSALEFRBUD + XX_FINALSALEAMOUNTBUD) AS PART, SUM(XX_SALESAMOUNTBUD2) AS PART1 FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH = '"+yearmonth+"' ");  //) AS MES1, " +
		
			if(WarehouseCombo.getSelectedIndex()>0){
				int clave_Warehouse=((KeyNamePair)WarehouseCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("M_Warehouse_ID=").append(clave_Warehouse);
				SQL += (" AND M_Warehouse_ID='"+clave_Warehouse+"' ");
			}
			
			if(CategoryCombo.getSelectedIndex()>0){
				int clave_Category =((KeyNamePair)CategoryCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_CATEGORY_ID=").append(clave_Category);
				SQL += (" AND XX_VMR_CATEGORY_ID='"+clave_Category+"' ");
			}
			
			if(DepartmentCombo.getSelectedIndex()>0){
				int clave_Department =((KeyNamePair)DepartmentCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_DEPARTMENT_ID=").append(clave_Department);
				SQL += (" AND XX_VMR_DEPARTMENT_ID='"+clave_Department+"' ");
			}
			
			if(LineaCombo.getSelectedIndex()>0){
				int clave_Linea =((KeyNamePair)LineaCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_LINE_ID=").append(clave_Linea);
				SQL += (" AND XX_VMR_LINE_ID='"+clave_Linea+"' ");
			}
			
			if(SeccionCombo.getSelectedIndex()>0){
				int clave_Seccion =((KeyNamePair)SeccionCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_LINE_ID=").append(clave_Seccion);
				SQL += (" AND XX_VMR_SECTION_ID='"+clave_Seccion+"' ");
			}
			

			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			    ResultSet rs = pstmt.executeQuery();
			    
			    if(rs.next())
			    {
			    	part = rs.getDouble("PART");
			    	part1 = rs.getDouble("PART1");
			    
			    	if(part1 == 0) 
			    	{
			    		//System.out.println("menos a cero");
			    		//System.out.println(part);
			    		total = new Double(0);
			    	}
			    	else
			    	{
			    		System.out.println("mayor a cero");
			    		part2 = part/part1;
			    		total = part2 * 100;
			    		System.out.println(total);
			    	}
			    }
			    pstmt.close();
			    rs.close();
			}
			catch (Exception e) {
			
			}

		 return total;
	}
	
	public static Double divcero3 (String yearmonth)
	{
		Double total = new Double(0);
		Double part =  new Double(0);
		Double part1 = new Double(0);
		Double part2 = new Double(0);
	
		//XX_AMOUNTACTUALSALE
		
		//SUM(((XX_ACTAMOUNTSALEPROM + XX_ACTAMOUNTSALEFR + XX_FINALACTAMOUNTSALE)/2)*100) 
		
			SQL  =  ("SELECT SUM(XX_ACTAMOUNTSALEPROM + XX_ACTAMOUNTSALEFR + XX_FINALACTAMOUNTSALE) AS PART, SUM(XX_AMOUNTACTUALSALE) AS PART1 FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH = '"+yearmonth+"' ");  //) AS MES1, " +
		
			if(WarehouseCombo.getSelectedIndex()>0){
				int clave_Warehouse=((KeyNamePair)WarehouseCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("M_Warehouse_ID=").append(clave_Warehouse);
				SQL += (" AND M_Warehouse_ID='"+clave_Warehouse+"' ");
			}
			
			if(CategoryCombo.getSelectedIndex()>0){
				int clave_Category =((KeyNamePair)CategoryCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_CATEGORY_ID=").append(clave_Category);
				SQL += (" AND XX_VMR_CATEGORY_ID='"+clave_Category+"' ");
			}
			
			if(DepartmentCombo.getSelectedIndex()>0){
				int clave_Department =((KeyNamePair)DepartmentCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_DEPARTMENT_ID=").append(clave_Department);
				SQL += (" AND XX_VMR_DEPARTMENT_ID='"+clave_Department+"' ");
			}
			
			if(LineaCombo.getSelectedIndex()>0){
				int clave_Linea =((KeyNamePair)LineaCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_LINE_ID=").append(clave_Linea);
				SQL += (" AND XX_VMR_LINE_ID='"+clave_Linea+"' ");
			}
			
			if(SeccionCombo.getSelectedIndex()>0){
				int clave_Seccion =((KeyNamePair)SeccionCombo.getSelectedItem()).getKey();
				//m_sql.append(" AND ").append("XX_VMR_LINE_ID=").append(clave_Seccion);
				SQL += (" AND XX_VMR_SECTION_ID='"+clave_Seccion+"' ");
			}	
			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			    ResultSet rs = pstmt.executeQuery();
			    
			    if(rs.next())
			    {
			    	part = rs.getDouble("PART");
			    	part1 = rs.getDouble("PART1");
			    
			    	if(part1 == 0) 
			    	{
			    		//System.out.println("menos a cero");
			    		//System.out.println(part);
			    		total = new Double(0);
			    	}
			    	else
			    	{
			    		//System.out.println("mayor a cero");
			    		part2 = part/part1;
			    		total = part2 * 100;
			    		//System.out.println(total);
			    	}
			    }
			    pstmt.close();
			    rs.close();
			}
			catch (Exception e) {
		
			}

		 return total;
	}
	
	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{	
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ m_groupBy + m_orderBy;
		
		log.finest(sql);
		System.out.println(sql);
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
	public void tableChanged(TableModelEvent e) 
	{
		
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {

		return 0;
	}
	
	/** Crear un archivo csv dado una */
	public void createCSV () throws Exception {
		
		File archivo = new File((String)bFile.getValue());
		try {
			
			char delimitador = '\t';
			//Crear el archivo necesario
			FileWriter fw = new FileWriter (archivo, false);
			BufferedWriter writer = new BufferedWriter(fw);		
			
			//Escribir la cabecera
			StringBuffer linea = new StringBuffer();
						
			linea.append("PRESUPUESTO " + xTable.getColumnModel().getColumn(1).getHeaderValue());
			writer.write(linea.toString());
			writer.write(Env.NL);
			linea.setLength(0);
			
			if(WarehouseCombo.getSelectedIndex()>0){
				linea.append("Almacen: " + WarehouseCombo.getSelectedItem());
				writer.write(linea.toString());
				writer.write(Env.NL);
				linea.setLength(0);
			}
			if(CategoryCombo.getSelectedIndex()>0){
				linea.append("Categoría: " + CategoryCombo.getSelectedItem());
				writer.write(linea.toString());
				writer.write(Env.NL);
				linea.setLength(0);
			}
			if(DepartmentCombo.getSelectedIndex()>0){
				linea.append("Departamento: " + DepartmentCombo.getSelectedItem());
				writer.write(linea.toString());
				writer.write(Env.NL);
				linea.setLength(0);
			}
			if(LineaCombo.getSelectedIndex()>0){
				linea.append("Linea: " + LineaCombo.getSelectedItem());
				writer.write(linea.toString());
				writer.write(Env.NL);
				linea.setLength(0);
			}
			if(SeccionCombo.getSelectedIndex()>0){
				linea.append("Sección: " + SeccionCombo.getSelectedItem());
				writer.write(linea.toString());
				writer.write(Env.NL);
				linea.setLength(0);
			}
			
			
			linea.append("Tipo de Registro: " + TypeRecordCombo.getSelectedItem());
			writer.write(linea.toString());
			writer.write(Env.NL);
			linea.setLength(0);
			
			writer.write("\t");
			writer.write(Env.NL);
			linea.setLength(0);
			
			for (int i = 0; i < xTable.getColumnCount(); i++) {
				if (i > 0) {
					linea.append(delimitador);					
				}
				
				linea.append( xTable.getColumnModel().getColumn(i).getHeaderValue());
			}
			writer.write(linea.toString());
			writer.write(Env.NL);
			linea.setLength(0);
			
			for (int row = 0; row < xTable.getRowCount(); row++){
				for (int column = 0; column < xTable.getColumnCount(); column++) {
					if (column > 0) {
						linea.append(delimitador);					
					}
					
					if (xTable.getValueAt( row, column) != null) {
						linea.append(xTable.getValueAt( row, column));				
					} 
				}
				writer.write(linea.toString());
				writer.write(Env.NL);
				linea.setLength(0);
			}			
			
			writer.flush();
			writer.close();
			
			//El archivo fue creado
			String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
				archivo.getName()
			});
			ADialog.info(1, new Container(), msg);			
			
		}  catch (FileNotFoundException fnfe) {
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
	
}//End form


