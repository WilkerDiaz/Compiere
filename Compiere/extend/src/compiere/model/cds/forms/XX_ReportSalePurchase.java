package compiere.model.cds.forms;
 
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MProduct;

/**
 *  Consulta del movimiento de la compra/venta y generación de los reportes
 *
 *  @author Jessica Mendoza
 *  @version    
 */
public class XX_ReportSalePurchase extends CPanel

	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener
	{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	NumberFormat formato = NumberFormat.getInstance();
	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		m_frame.setName("UnsolicitedProduct");
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		Env.getCtx().remove("#XX_CheckupInOutLine");
		try{
			jbInit();
			dynInitFirst();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);		
		}catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}
	}	
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_UnsolicitedProduct_Form.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	static String          union = "";
	static Ctx             ctx_aux = Env.getCtx();
	static MProduct        productReference = new MProduct(Env.getCtx(),0,null);
	
	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	//private CPanel southPanel = new CPanel();
	//private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder(Msg.getMsg(Env.getCtx(), "XX_SalePurchase"));
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	/****CheckBox-Labels-ComboBox****/
	private JCheckBox checkWarehouseCD = new JCheckBox(Msg.getMsg(Env.getCtx(), "XX_WithOneStore"));	
	private CLabel categoryLabel = new CLabel(Msg.getMsg(Env.getCtx(), "XX_CategoryComercial"));
	private CComboBox categoryCombo = new CComboBox();
	private CLabel warehouse = new CLabel(Msg.getMsg(Env.getCtx(), "XX_WarehouseName"));	
	private static CComboBox  warehouseCombo = new CComboBox();
	private CLabel department = new CLabel(Msg.getMsg(Env.getCtx(), "XX_Department"));	
	private static CComboBox  departmentCombo = new CComboBox();	
	private static VNumber  year = new VNumber();
	private static VNumber  month = new VNumber();
	private CLabel yearLabel = new CLabel(Msg.getMsg(Env.getCtx(), "Year"));	
	private CLabel monthLabel = new CLabel(Msg.getMsg(Env.getCtx(), "Month"));
	
	/****Botones****/
	private CButton bSearch = new CButton();
	private CButton bReport = new CButton();	
	
	KeyNamePair loadKNP = new KeyNamePair(0, "");
	
	/**
	 * Organiza las posiciones de cada etiqueta en la forma
	 *  @throws Exception
	 */
	private void jbInit() throws Exception{

		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
	    //southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);
	
		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bSearch.setPreferredSize(new Dimension(80,22));	
		bSearch.setEnabled(true);
		bSearch.addActionListener(this);
		bReport.setText(Msg.translate(Env.getCtx(), "XX_Report"));
		bReport.setPreferredSize(new Dimension(80,22));	
		bReport.setEnabled(false);
		bReport.addActionListener(this);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(750, 250));		
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		//mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		xScrollPane.getViewport().add(xTable, null);
		
		/**
		 * Panel Superior
		 */		
		/****Etiquetas de la primera fila****/
		northPanel.add(monthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(month, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 5, 0), 0, 0));	
		northPanel.add(yearLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(year, new GridBagConstraints(3 ,0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 5, 0), 0, 0));

		/****Etiquetas de la segunda fila****/
		northPanel.add(warehouse, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 12, 5, 5), 0, 0));	
		northPanel.add(warehouseCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 0, 5, 0), 0, 0));	
		northPanel.add(department, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 12, 5, 5), 0, 0));
		northPanel.add(departmentCombo, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 0, 5, 0), 0, 0));
		
		/****Etiquetas de la tercera fila****/
		northPanel.add(categoryLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 12, 5, 5), 0, 0));		
		northPanel.add(categoryCombo, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 0, 5, 0), 0, 0));		
		northPanel.add(checkWarehouseCD, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.WEST, 
				new Insets(0, 10, 5, 5), 0, 0));
		
		/****Etiquetas de la cuarta fila****/
		northPanel.add(bSearch, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 12), 0, 0));		
		northPanel.add(bReport, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 12), 0, 0)); 
	}   

	private void loadInfo(){
		warehouseCombo.removeActionListener(this);
		month.removeActionListener(this);
		departmentCombo.removeActionListener(this);
		year.removeActionListener(this);
		
		warehouseCombo.removeAllItems();		
		departmentCombo.removeAllItems();
		month.setValue(null);
		year.setValue(null);
		
		dynCategory();
		dynWarehouse();
		dynDepartment();
		
		checkWarehouseCD.setEnabled(true);
		checkWarehouseCD.setSelected(false);
		checkWarehouseCD.addActionListener(this);			
	}
	
	/**
	 * Carga las categorias comerciales (moda, hogar, deporte, niños, belleza)
	 */
	void dynCategory() {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME " +
					 "FROM XX_VMR_CATEGORY " +
					 "ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
						MRole.SQL_RO);
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			categoryCombo.addItem(new KeyNamePair(-1, null)); 
			while (rs.next()) {
				categoryCombo.addItem(new KeyNamePair(rs.getInt(1), rs
					.getString(2)));
			}
						
			categoryCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Carga las tiendas
	 */
	void dynWarehouse() {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT M_WAREHOUSE_ID, VALUE ||'-'|| NAME " +
	     			 "FROM M_WAREHOUSE ORDER BY VALUE ASC";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			warehouseCombo.addItem(new KeyNamePair(-1, null)); 
			while (rs.next()) {
				warehouseCombo.addItem(new KeyNamePair(rs.getInt(1), rs
					.getString(2)));
			}		
			warehouseCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		/****Captura el evento para realizar otra accion****/
		warehouseCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {

			@Override
			public void contentsChanged(ListDataEvent e) {
				if(warehouseCombo.getSelectedIndex() > 0)
					checkWarehouseCD.setEnabled(false);	
				else
					checkWarehouseCD.setEnabled(true);						
			}

			@Override
			public void intervalAdded(ListDataEvent e) {
							
			}

			@Override
			public void intervalRemoved(ListDataEvent e) {
								
			}
		});
	}
	
	/**
	 * Carga los departamentos
	 */
	void dynDepartment() {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT DISTINCT XX_VMR_DEPARTMENT_ID, VALUE ||'-'|| NAME " +
		   	  		 "FROM XX_VMR_DEPARTMENT " +
		   	  		 "ORDER BY VALUE ||'-'||NAME ASC";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			departmentCombo.addItem(new KeyNamePair(-1, null)); 
			while (rs.next()) {
				departmentCombo.addItem(new KeyNamePair(rs.getInt(1), rs
					.getString(2)));
			}		
			departmentCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Genera las columnas de la tabla
	 */
	private void dynInitFirst(){
		loadInfo();

		ColumnInfo[] layoutF = new ColumnInfo[] {
				/****0-Tipo de Registro****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RegistrationType"), "", String.class),								
				/****1-Monto Mensual****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_MonthAmount"), ".", BigDecimal.class),
				/****2-Monto Acumulado****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AccumulatedAmount"), ".", BigDecimal.class),		
		};
		xTable.prepareTable(layoutF, "", "", true, "");
		xTable.getSelectionModel().addListSelectionListener(this);		
		xTable.setAutoResizeMode(3);
		xTable.getColumnModel().getColumn(0).setMinWidth(300);
		xTable.getColumnModel().getColumn(1).setMinWidth(140);
		xTable.getColumnModel().getColumn(2).setMinWidth(140);
		
	}
	
	/**
	 * Carga la tabla de compra/venta
	 */
	private void tableInit (){		
		m_sql = new StringBuffer ();
		
		m_sql.append ("SELECT " +
					  "(SELECT NAME FROM AD_Ref_List rf WHERE VALUE = a AND AD_Reference_ID=1000219) as TYPEREG, " +
					  "sum(MONTHAMOUNT) monto, sum(ACCUMULATEDAMOUNT) montoacum " +
					  "FROM ("+
					  "SELECT " +
					  "TO_NUMBER(sp.XX_TYPEREG) a, sp.XX_AMOUNTMONTH AS MONTHAMOUNT, sp.XX_AMOUNTACU AS ACCUMULATEDAMOUNT " +
					  "FROM XX_VCN_SALEPURCHASE sp " +
					  "WHERE sp.ISACTIVE='Y' " +
					  "and sp.AD_Client_ID in (0,"+Env.getCtx().getAD_Client_ID()+") " );
            

		if (warehouseCombo.getSelectedIndex() != 0){
			int idTienda=((KeyNamePair)warehouseCombo.getSelectedItem()).getKey();
			m_sql.append("and ").append("sp.M_Warehouse_ID = ").append(idTienda).append(" ");
		}else{
			if (!checkWarehouseCD.isSelected()){ //sin tienda 1
				m_sql.append("and ").append("sp.M_Warehouse_ID <> 1000053 ");
			}	
		}
		
		if (departmentCombo.getSelectedIndex()!=0){
			int idDepartmento=((KeyNamePair)departmentCombo.getSelectedItem()).getKey();
			m_sql.append("and ").append("sp.XX_VMR_Department_ID = ").append(idDepartmento).append(" ");
		}
		
		if (year.getValue() != null){
			m_sql.append("and ").append("sp.XX_Year = ").append(year.getValue()).append(" ");
		}
		
		if (month.getValue() != null){
			m_sql.append("and ").append("sp.XX_Month = ").append(month.getValue()).append(" ");
		}
		
		if (categoryCombo.getSelectedIndex()!=0){
			int idCat = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
			m_sql.append("AND XX_VMR_DEPARTMENT_ID IN (" +
				"SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT WHERE XX_VMR_CATEGORY_ID = ").append(idCat).append(")");
		}
	
		union = " UNION ALL Select Rownum r, 0, 0 From DUAL Connect By Rownum <= 38) ";
		
		m_groupBy = "group by a ";
		m_orderBy = "order by a asc ";	

		String sql = m_sql.toString() + union + m_groupBy.toString() + m_orderBy.toString();

		int i = 0;
		xTable.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTable.setRowCount (i+1);		
				/****Tipo de Registro****/
				xTable.setValueAt(rs.getString("typeReg"), i, 0);
				/****Monto mensual****/
				xTable.setValueAt(rs.getBigDecimal("monto"), i, 1);
				/****Monto acumulado****/
				xTable.setValueAt(rs.getBigDecimal("montoacum"), i, 2);
				
				//xTable.setValueAt(formato.format(monto(rs.getString("typeReg"),"MONTH",tienda).setScale(2, RoundingMode.DOWN)), i, 2);
				/****Monto acumulado****/
				//xTable.setValueAt(formato.format(monto(rs.getString("typeReg"),"ACU",tienda).setScale(2, RoundingMode.DOWN)), i, 3);
				i++;
			}
			
			if(i>0)
				dynamicPercentage();
			
		}catch(SQLException e){	
			e.getMessage();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		bReport.setEnabled(true);
	} 
	
	/**
	 * Calcula el monto para los tipo de registros 39 y 40
	 * @param typeReg tipo de registro
	 * @param typeAmount tipo de monto (acumulado, mensual)
	 * @return
	 */
	public BigDecimal monto(String typeReg, String typeAmount, String tienda){
		BigDecimal total = new BigDecimal(0);
		String sql = "select amountSale('" + tipoRegistro(typeReg) + "','" + typeAmount + "','" + tienda + "') from dual ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {	
				total = rs.getBigDecimal(1);
			}	
		}catch(SQLException e){	
			e.getMessage();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return total;
	}
	
	/**
	 * Se encarga de buscar el value del tipo de registro
	 * @param typeReg nombre del tipo de registro
	 * @return
	 */
	public String tipoRegistro(String typeReg){
		String sql = "select value " +
					 "from AD_Ref_List " +
					 "where AD_Reference_ID = 1000219 " +
					 "and name = '" + typeReg + "' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {	
				typeReg = rs.getString("value");
			}	
		}catch(SQLException e){	
			e.getMessage();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return typeReg;
	}
	
	/**
	 * 	Dispose
	 */
	public void dispose(){
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;				
	}
		
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e){		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSearch)
			cmd_Search();
		else if (e.getSource() == bReport)
			cmd_Report();		
	}  
	
	/**
	 * Funcionalidad al ejecutar el botón Buscar
	 */
	private void cmd_Search(){				
		if ((month.getValue()!= null) && (year.getValue()!= null)){
			tableInit();			
			if(xTable.getRowCount()!=0){
				statusBar.setStatusLine("Records");
				statusBar.setStatusDB(xTable.getRowCount());				
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, "Not found data");
				statusBar.setStatusLine("Not found products");
			}
		}else{
			xScrollPane.getViewport().remove(xTable);
			xTable = new MiniTable();
			xScrollPane.getViewport().add(xTable, null);
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "SelectData"));
			loadInfo();
			year.setValue(null);
			month.setValue(null);
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "XX_NotFoundData"));
		}	
	}   
	
	/**
	 * Funcionalidad al ejecutar el botón Reporte
	 */
	private void cmd_Report(){
		
		int store = 0;
		int deparment = 0;
		int withCD = 0;
		int category = 0;

		if ((categoryCombo.getSelectedItem() != null) && (categoryCombo.getSelectedIndex() != 0))
			category = ((KeyNamePair) categoryCombo.getSelectedItem()).getKey();
			
		if ((warehouseCombo.getSelectedItem() != null) && (warehouseCombo.getSelectedIndex() != 0))
			store =  ((KeyNamePair)warehouseCombo.getSelectedItem()).getKey();
		
		if ((departmentCombo.getSelectedItem() != null) && (departmentCombo.getSelectedIndex() != 0))
			deparment = ((KeyNamePair)departmentCombo.getSelectedItem()).getKey();
		
		if(checkWarehouseCD.isSelected())
			withCD = 1;
		
		BigDecimal r39 = new BigDecimal(xTable.getValueAt(38, 1).toString());
		BigDecimal r39A = new BigDecimal(xTable.getValueAt(38, 2).toString());
		BigDecimal r40 = new BigDecimal(xTable.getValueAt(39, 1).toString());
		BigDecimal r40A = new BigDecimal(xTable.getValueAt(39, 2).toString());
		
		generatedReport(store, deparment, withCD, category, r39, r39A, r40, r40A);
		bReport.setEnabled(false);
	}
	
	/**
	 * Genera el reporte correspondiente
	 * @param tienda 
	 * @param departamento
	 * @param tiendaCd
	 */
	private void generatedReport(int tienda, int departamento, int withCD, int category, 
			BigDecimal r39, BigDecimal r39A, BigDecimal r40, BigDecimal r40A){
		
		String designName = "MovementPurchaseSales";

		//Instanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("store");
		myReport.parameterValue.add(tienda);
		myReport.parameterName.add("department");
		myReport.parameterValue.add(departamento);
		myReport.parameterName.add("category");
		myReport.parameterValue.add(category);
		myReport.parameterName.add("CD");
		myReport.parameterValue.add(withCD);
		myReport.parameterName.add("month");
		myReport.parameterValue.add(new BigDecimal(month.getValue().toString()).toBigInteger());
		myReport.parameterName.add("year");
		myReport.parameterValue.add(new BigDecimal(year.getValue().toString()).toBigInteger());
		myReport.parameterName.add("company");
		myReport.parameterValue.add(Env.getCtx().getAD_Client_ID());
		
		myReport.parameterName.add("r39");
		myReport.parameterValue.add(r39);
		myReport.parameterName.add("r39A");
		myReport.parameterValue.add(r39A);
		myReport.parameterName.add("r40");
		myReport.parameterValue.add(r40);
		myReport.parameterName.add("r40A");
		myReport.parameterValue.add(r40A);
		
		//Correr Reporte
		myReport.runReport(designName, "pdf");
	}
		
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e){	
	
	}  
	
	@Override
	public void tableChanged(TableModelEvent e) {
		
	}
	
	/*
	 * Calcula dinamicamente los registros 39 y 40
	 */
	private void dynamicPercentage(){
		
		BigDecimal hundred = new BigDecimal(100);
		
		xTable.setRowCount (39);
		xTable.setValueAt("39-PORCENTAJE SOBRE VENTAS (38/7)", 38, 0);
		
		BigDecimal reg39 = BigDecimal.ZERO;
		if(new BigDecimal(xTable.getValueAt(06, 1).toString()).compareTo(BigDecimal.ZERO)!=0)
			reg39 = new BigDecimal(xTable.getValueAt(37, 1).toString()).divide(new BigDecimal(xTable.getValueAt(06, 1).toString()), BigDecimal.ROUND_HALF_UP,2).multiply(hundred);
		xTable.setValueAt(reg39, 38, 1);
		
		BigDecimal reg39_Acum = BigDecimal.ZERO;
		if(new BigDecimal(xTable.getValueAt(06, 2).toString()).compareTo(BigDecimal.ZERO)!=0)
			reg39_Acum = new BigDecimal(xTable.getValueAt(37, 2).toString()).divide(new BigDecimal(xTable.getValueAt(06, 2).toString()), BigDecimal.ROUND_HALF_UP,2).multiply(hundred);
		xTable.setValueAt(reg39_Acum, 38, 2);	

		xTable.setRowCount (40);
		xTable.setValueAt("40-PORCENTAJE MARGEN POR GANAR S/INVENTARIO FINAL (26/19)", 39, 0);
		
		BigDecimal reg40 = BigDecimal.ZERO;
		if(new BigDecimal(xTable.getValueAt(18, 1).toString()).compareTo(BigDecimal.ZERO)!=0)
			reg40 = new BigDecimal(xTable.getValueAt(25, 1).toString()).divide(new BigDecimal(xTable.getValueAt(18, 1).toString()), BigDecimal.ROUND_HALF_UP,2).multiply(hundred);
		xTable.setValueAt(reg40, 39, 1);
		
		BigDecimal reg40_Acum = BigDecimal.ZERO;
		if(new BigDecimal(xTable.getValueAt(18, 2).toString()).compareTo(BigDecimal.ZERO)!=0)
			reg40_Acum = new BigDecimal(xTable.getValueAt(25, 2).toString()).divide(new BigDecimal(xTable.getValueAt(18, 2).toString()), BigDecimal.ROUND_HALF_UP,2).multiply(hundred);
		xTable.setValueAt(reg40_Acum, 39, 2);
		
	}
}

