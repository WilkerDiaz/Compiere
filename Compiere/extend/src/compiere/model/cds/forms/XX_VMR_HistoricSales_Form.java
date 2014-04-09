package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import jxl.DateCell;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.dynamic.XX_VME_ProductFilterResults;

/** XX_VMR_HistoricSales_Form
 * Forma que permite al usuario filtrar las ventas. 
 * @author Maria Vintimilla
 * */

public class XX_VMR_HistoricSales_Form extends CPanel implements FormPanel, 
ActionListener, TableModelListener, ListSelectionListener{


	private static final long serialVersionUID = 1L;
	
	/** Inicializar la forma
	 *  @param WindowNo window
	 *  @param frame frame
	 */ 
	public final void init (int WindowNo, FormFrame frame){
		
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try	{
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		}
		catch(Exception e)	{
			log.log(Level.SEVERE, "", e);
		}
	}	// Fin init
	
	protected Ctx ctx = Env.getCtx();
	/**	Window No */
	private int m_WindowNo = 0;
	/**	FormFrame */
	protected FormFrame m_frame;
	/** Format */
	protected DecimalFormat m_format = DisplayType.getNumberFormat(DisplayTypeConstants.Amount);
	/** SQL for Query */
	protected StringBuilder m_sql;
	/** Number of selected rows */
	protected int m_noSelected = 0;
	
	protected Vector<String> selected = new Vector<String>();
	
	private int indx = 0;

	/**	Logger */
	protected static CLogger log = CLogger.getCLogger(XX_VMR_HistoricSales_Form.class);
		
	//PANEL
	protected CPanel mainPanel = new CPanel();
	protected BorderLayout mainLayout = new BorderLayout();
	protected CPanel parameterPanel = new CPanel();
	protected MiniTablePreparator miniTable = new MiniTablePreparator();
	protected CPanel commandPanel = new CPanel();
	protected GridBagLayout parameterLayout = new GridBagLayout();
	protected JScrollPane dataPane = new JScrollPane();
	protected FlowLayout commandLayout = new FlowLayout();
	
	//BOTONES
	protected CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	protected CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	
	//CATEGORIA
	protected CLabel labelCategory = new CLabel();
	protected VComboBox comboCategory = new VComboBox();
	
	//DEPARTAMENTO
	protected CLabel labelDepartment = new CLabel();
	protected VComboBox comboDepartment = new VComboBox();
	
	//TIENDA
	protected CLabel labelTienda = new CLabel();
	protected VComboBox comboTienda = new VComboBox();
	
	// FECHAS 
	protected VDate fromDate = new VDate();
	protected VDate toDate = new VDate();
	protected CLabel fromLabel = new CLabel();
	protected CLabel toLabel = new CLabel();
	//OTROS
//	protected CLabel lastWeekLabel = new CLabel();
//	protected CTextField lastWeekText = new CTextField();
	
	//OTROS
	protected CLabel labelResults = new CLabel();
	protected CTextField textResults = new CTextField();
	
	// La tabla donde se guardarán los datos
	protected MiniTablePreparator tableResults = new MiniTablePreparator();
	
	// Form ID
//	protected Integer FormID = Integer.parseInt(Env.getCtx().getContext("#XX_HistoricSalesForm_ID"));

	// Vectores que contiene los resultados del filtro
	protected Vector<KeyNamePair> Category = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Department = new Vector<KeyNamePair>();
	protected Vector<KeyNamePair> Tienda = new Vector<KeyNamePair>();
	
	// Columnas 
	private ColumnInfo[] columnsRef = new ColumnInfo[] { 
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DAY"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Date"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RealSales"),".",String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_BudgetSales"),".", String.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Percentage"),".", String.class, ".")
	};
	
	/** Static Init
	 *  @throws Exception
	 */
	private final void jbInit() throws Exception {
		bSearch.setPreferredSize(new Dimension(100, 25));
		bReset.setPreferredSize(new Dimension(100, 25));
		CompiereColor.setBackground(this);		
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);	
	
		labelCategory.setText(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"));
		labelDepartment.setText(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"));
		labelTienda.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
//		lastWeekLabel.setText(Msg.translate(Env.getCtx(), "XX_LastWeek"));
		fromLabel.setText(Msg.translate(Env.getCtx(), "XX_From"));
		toLabel.setText(Msg.translate(Env.getCtx(), "XX_To"));

		// PANEL CREATION
		mainPanel.add(parameterPanel, BorderLayout.NORTH);		
		
		// CATEGORIA
		parameterPanel.add(labelCategory,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboCategory,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// DEPARTAMENTO
		parameterPanel.add(labelDepartment,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboDepartment,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// WAREHOUSE
		parameterPanel.add(labelTienda,  new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(comboTienda,   new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// FROM
		parameterPanel.add(fromLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fromDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
		// TO
		parameterPanel.add(toLabel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(toDate, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));		
		// BUSCAR
		parameterPanel.add(bSearch, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// RESET
		parameterPanel.add(bReset,    new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// SEMANA PASADA
//		parameterPanel.add(lastWeekLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
//			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,	new Insets(5, 5, 5, 5), 0, 0));
//		parameterPanel.add(lastWeekText, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
//			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
//	
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		dataPane.setPreferredSize(new Dimension(1200, 500));

		//PANEL INFERIOR
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.CENTER);
		commandLayout.setHgap(10);
		
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
	}   //  jbInit
	
	/** Dynamic Init.
	 *  - Load Department Info
	 *  - Load BPartner Info
	 *  - Load Brand Info
	 *  - Init Table
	 */
	protected void dynInit() {
		desactivarActionListeners();
		loadBasicInfo();
		activarActionListeners();
	}   //  dynInit
	
	/** loadBasicInfo
	 *  Table initial state 
	 */
	public final void loadBasicInfo() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		desactivarActionListeners();
		//Restaura los ComboBoxes y CheckBoxes		
		comboCategory.setEnabled(true);
		comboDepartment.setEnabled(false);
		comboTienda.setEnabled(true);
		comboCategory.removeAllItems();
		comboDepartment.removeAllItems();
		comboTienda.removeAllItems();
		fromDate.setEnabled(false);
		toDate.setEnabled(false);
		fromDate.setEnabled(true);
		toDate.setEnabled(true);
		
		//Setting Default Items
		KeyNamePair loadKNP = new KeyNamePair(0, "");
		comboCategory.addItem(loadKNP);
		comboDepartment.addItem(loadKNP);
		comboTienda.addItem(loadKNP);
		
		// Cantidad del elemento
//		lastWeekText.setEditable(false);
//		lastWeekText.setBackground(Color.LIGHT_GRAY);
//		lastWeekText.setPreferredSize(new Dimension(80, 25));
//		lastWeekText.setHorizontalAlignment(JTextField.CENTER);
		comboDepartment.setPreferredSize(new Dimension(200, 25));
		comboTienda.setPreferredSize(new Dimension(200, 25));
		
		// CARGA DE CATEGORIAS
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||' - '||NAME FROM XX_VMR_CATEGORY WHERE ISACTIVE='Y' ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " AND ISACTIVE = 'Y' ORDER BY VALUE||' - '||NAME";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCategory.addItem(loadKNP);	
			}
			comboCategory.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE DEPARTAMENTOS
		sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||' - '||NAME FROM XX_VMR_DEPARTMENT ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " AND ISACTIVE = 'Y'  ORDER BY VALUE||' - '||NAME";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboDepartment.setSelectedIndex(0);
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// CARGA DE TIENDAS
		sql = "SELECT M_WAREHOUSE_ID, VALUE||' - '||NAME FROM M_WAREHOUSE " ;
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) +
				" AND ISACTIVE = 'Y'  ORDER BY VALUE||' - '||NAME";
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboTienda.addItem(loadKNP);
			}
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		
		activarActionListeners();
	} // loadBasicInfo
	
	/** loadDepartmentInfo
	 * Cargar información de departamentos
	 * */
	protected final void loadDepartmentInfo(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		comboDepartment.removeAllItems();
		comboDepartment.setEnabled(true);
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
//		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair loadKNP;
		String sql = "";
		
		loadKNP = new KeyNamePair(0,"");
		comboDepartment.addItem(loadKNP);
		sql = " SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||' - '||dp.NAME" 
			+ " FROM XX_VMR_DEPARTMENT dp";	

		if(cat != null && cat.getKey() != 99999999){
			sql += " WHERE dp.XX_VMR_CATEGORY_ID = " + cat.getKey();
		}
		sql += " ORDER BY dp.VALUE||' - '||dp.NAME";	
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);			
			}			
		}
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		}	
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // loadDepartmentInfo
	
	
	/** Activa todos los listeners creados */
	protected final void activarActionListeners () {
		comboCategory.addActionListener(this);
		comboDepartment.addActionListener(this);
		comboTienda.addActionListener(this);
		fromDate.addActionListener(this);
		toDate.addActionListener(this);
	}//addActionListeners
	
	/** Deshabilitar Action Listeners */
	protected final void desactivarActionListeners () {
		comboCategory.removeActionListener(this);
		comboDepartment.removeActionListener(this);
		comboTienda.removeActionListener(this);
		fromDate.removeActionListener(this);
		toDate.removeActionListener(this);
	} // removeActionListeners

	/** loadTableInfo
	 *  Busqueda para llenar la TableInfo de acuerdo con las especificaciones 
	 *  del usuario
	 */
	protected void loadTableInfo(){
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		tableResults.setRowCount(0);
		tableResults = new MiniTablePreparator();
		tableResults.setRowHeight(tableResults.getRowHeight() + 2 );
		dataPane.getViewport().add(tableResults, null);
		
		tableResults.getSelectionModel().addListSelectionListener(this);
		tableResults.getModel().addTableModelListener(this);
		
		Timestamp dateFrom = (Timestamp)fromDate.getValue();
		Timestamp dateTo = (Timestamp)toDate.getValue();
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair tienda = (KeyNamePair)comboTienda.getSelectedItem();
		
		Calendar week = new GregorianCalendar(); 

		int firstWeek = 0;
		int dayOfWeek = 0;
		// Se obtiene el valor de la primera semana
		if(dateFrom != null){
			week.setTime(dateFrom);
		}
		else{
			Date date= new java.util.Date();
			week.setTime(new Timestamp(date.getTime()));
		}
		
		firstWeek = week.get(Calendar.WEEK_OF_YEAR)-1;
		dayOfWeek = week.get(Calendar.DAY_OF_WEEK);
		
		String select = "";
		String selectLastWeek = "";
		String from = " ";
		String where = " WHERE ";
		String and = " AND ";
		String group = " GROUP BY TO_CHAR(XX_VMR_SALESDATE, 'IW'), TO_CHAR(XX_VMR_SALESDATE, 'DAY'), " +
				" XX_VMR_SALESDATE" +
				" ORDER BY XX_VMR_SALESDATE ASC ";
		String SQLTotal = "";

		if(cat.getKey()!= 0){
				from += " LEFT OUTER JOIN XX_VMR_CATEGORY CAT ON (H.XX_VMR_CATEGORY_ID = CAT.XX_VMR_CATEGORY_ID)";
				if (where.length() > 7)
					where += and;
				where += " CAT.ISACTIVE = 'Y' AND CAT.XX_VMR_CATEGORY_ID = " + cat.getKey();
		} // if categoria
		
		//Leer Departamento
		if(dept.getKey()!= 0){
				from += " LEFT OUTER JOIN XX_VMR_DEPARTMENT DP ON (H.XX_VMR_DEPARTMENT_ID=DP.XX_VMR_DEPARTMENT_ID)"; 
				if (where.length() > 7)
					where += and;
				where += " DP.ISACTIVE = 'Y' AND DP.XX_VMR_DEPARTMENT_ID = " + dept.getKey();				
		} // if dept != null
		
		//Leer Tienda
		if(tienda.getKey()!= 0){
				from += " LEFT OUTER JOIN M_WAREHOUSE W ON (H.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID)"; 
				if (where.length() > 7)
					where += and;
				where += " W.ISACTIVE = 'Y' AND W.M_WAREHOUSE_ID = " + tienda.getKey();				
		} // if tienda != null
		
		// Leyendo fecha
		if(dateFrom != null ){
			if (where.length() > 7)
				where += and;
				where += " TRUNC(XX_VMR_SALESDATE)  >= " +DB.TO_DATE(dateFrom,true);
		} // date
		
		if(dateTo != null ){
			if (where.length() > 7)
				where += and;
			where += " TRUNC(XX_VMR_SALESDATE)  <= " +DB.TO_DATE(dateTo,true);
		} // date

		select += " SELECT " +
				" TO_CHAR(XX_VMR_SALESDATE, 'IW') SWEEK, " +				//1		
				" TO_CHAR(XX_VMR_SALESDATE, 'DAY') SDAY, " +				//2
				" TO_CHAR(XX_VMR_SALESDATE, 'DD/MM/YYYY') SDATE, "+								//3
				" TO_CHAR(ROUND(COALESCE(SUM(XX_VMR_SALESACUMAMOUNT),0), 2), '999G999G999D99') REAL, " +					//4
				" TO_CHAR(ROUND(COALESCE(SUM(DISTINCT XX_VMR_SALESBUTGETAMOUNT),0), 2), '999G999G999D99') BUDGET, "+				//5
				" TO_CHAR(ROUND(COALESCE(((SUM(XX_VMR_SALESACUMAMOUNT)*100)/SUM(DISTINCT XX_VMR_SALESBUTGETAMOUNT)),0),2), '999G999G999D99') PERCENTAGE, " +	//6
				" TO_CHAR(XX_VMR_SALESDATE, 'D') DAY, " + //7
				" ROUND(COALESCE(SUM(XX_VMR_SALESACUMAMOUNT),0), 2) REALB, " +					//8
				" ROUND(COALESCE(SUM(DISTINCT XX_VMR_SALESBUTGETAMOUNT),0), 2) BUDGETB, "+				//9
				" ROUND(COALESCE(((SUM(XX_VMR_SALESACUMAMOUNT)*100)/SUM(DISTINCT XX_VMR_SALESBUTGETAMOUNT)),0),2) PERCENTAGEB " +	//10
				" FROM  XX_VMR_HISTORICSALES H /*LEFT OUTER JOIN XX_VMR_PRLD03 PROD " +
				" ON (H.XX_VMR_DEPARTMENT_ID = PROD.XX_VMR_DEPARTMENT_ID)*/" ;

		if (where.length() == 7){
			where = "";
		}
		SQLTotal = select + from + where + group;
	System.out.println("SQLTotal: "+ SQLTotal);
		
		//Preparar la tabla	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectFinal = tableResults.prepareTable(columnsRef, "", "", true, "");
		
		try {
			pstmt = DB.prepareStatement(SQLTotal, null);
			rs = pstmt.executeQuery();		

			int row = 0;
			int currentWeek = 0;
			BigDecimal weekTotalSales = new BigDecimal(0);
			BigDecimal weekTotalBudget = new BigDecimal(0);
			BigDecimal weekTotalPercent = new BigDecimal(0);
			
			while (rs.next()) {
				currentWeek = rs.getInt(1);
				int nextWeek = firstWeek + 1;
				dayOfWeek = rs.getInt(7);
				tableResults.setRowCount(row + 1);
				
				// Si es la misma semana se imprimen los valores de los días
				if(currentWeek == firstWeek || (currentWeek == nextWeek && dayOfWeek == 7)){
					tableResults.setValueAt(rs.getString(2), row, 0);			// Day
					tableResults.setValueAt(rs.getString(3), row, 1);			// Date
					tableResults.setValueAt(rs.getString(4), row, 2);		// Reales
					tableResults.setValueAt(rs.getString(5), row, 3);		// Presupuesto
					tableResults.setValueAt(rs.getString(6), row, 4);		// Percentage
					row++;
				}
				// Si la semana es distinta se imprimen los valores del total semana
				else{
					if(row != 0 & dayOfWeek == 1){
					firstWeek = rs.getInt(1);
					tableResults.setValueAt("TOTAL SEMANA", row, 0);			// Day
					tableResults.setValueAt("", row, 1);			// Date
					tableResults.setValueAt(String.format("%,.2f", weekTotalSales), row, 2);		// Reales
					tableResults.setValueAt(String.format("%,.2f",weekTotalBudget), row, 3);		// Presupuesto
					tableResults.setValueAt(String.format("%,.2f",weekTotalPercent), row, 4);		// Percentage
					weekTotalSales = new BigDecimal(0);
					weekTotalBudget = new BigDecimal(0);
					weekTotalPercent = new BigDecimal(0);

					tableResults.getColumnModel().getColumn(0).setCellRenderer(render); 
					tableResults.getColumnModel().getColumn(1).setCellRenderer(render); 
					tableResults.getColumnModel().getColumn(2).setCellRenderer(render); 
					tableResults.getColumnModel().getColumn(3).setCellRenderer(render); 
					tableResults.getColumnModel().getColumn(4).setCellRenderer(render); 

					row++;

					tableResults.setRowCount(row + 1);
					}
					tableResults.setValueAt(rs.getString(2), row, 0);			// Day
					tableResults.setValueAt(rs.getString(3), row, 1);			// Date
					tableResults.setValueAt(rs.getString(4), row, 2);		// Reales
					tableResults.setValueAt(rs.getString(5), row, 3);		// Presupuesto
					tableResults.setValueAt(rs.getString(6), row, 4);		// Percentage
					row++;
				}
				weekTotalSales = weekTotalSales.add(rs.getBigDecimal(8));
				weekTotalBudget = weekTotalBudget.add(rs.getBigDecimal(9));
				weekTotalPercent = (weekTotalSales.multiply(new BigDecimal(100))).
									divide(weekTotalBudget, BigDecimal.ROUND_HALF_UP);
			}// while rs
			tableResults.autoSize(true);
			tableResults.setMultiSelection(true);
		} // try 
		catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		tableResults.repaint();		
		TableColumn col = tableResults.getColumnModel().getColumn(indx+1);
		tableResults.setMultiSelection(true);
		tableResults.setRowSelectionAllowed(false);
		tableResults.autoSize();
		tableResults.setRowHeight(tableResults.getRowHeight() + 2 );
		tableResults.getTableHeader().setReorderingAllowed(false);
		m_frame.setCursor(Cursor.getDefaultCursor());
		
	}   //  loadTableInfo
	
	/**
	 * 	Dispose
	 */
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose
	
	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e) {
		desactivarActionListeners();
		//Category Combo 
		if (e.getSource() == comboCategory){			
			if(comboCategory.getValue() != null){
				if(!comboCategory.getValue().equals(Integer.valueOf(0))){
					comboDepartment.setEnabled(true);
					comboDepartment.removeActionListener(this);
					loadDepartmentInfo();
					comboDepartment.addActionListener(this);
				}
				else{
					comboDepartment.setEnabled(false);
				}
			}
		}//Category Combo 
		
		//Clean Form
		else if(e.getSource() == bReset){
			limpiarFiltro();
		} // bReset
		
		//Execute Search
		else if(e.getSource() == bSearch) {
			try {
				loadTableInfo();
			} 
			catch (NullPointerException n) {
				n.printStackTrace();
			}
		} // bSearch
		
		activarActionListeners();
	}   //  actionPerformed
	

	public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
		private static final long serialVersionUID = 1L;
		public ComboBoxRenderer(Vector<KeyNamePair> items) {
            super(items);
        }
    
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } 
            else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
    
            // Select the current value
            setSelectedItem(value);
            return this;
        }
    } // ComboBoxRenderer

	
	public class ComboBoxEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		public ComboBoxEditor(Vector<KeyNamePair> items) {
            super(new JComboBox(items));
        }
    } // ComboBoxEditor


	/**
	 * List Selection Listener - get Info and fill xMatchedTo
	 * @param e event
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
	} // valueChanged
	

	/** Método invocado al presionar el botón de limpiar filtro */
	protected void limpiarFiltro() { 
		loadBasicInfo();
	}

	@Override
	public void tableChanged(TableModelEvent paramTableModelEvent) {
		// TODO Auto-generated method stub
		
	}
	
	TableCellRenderer render = new TableCellRenderer() { 
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) { 
			//Si values es nulo dara problemas de renderizado, por lo tanto se pone como vacio 
			JLabel lbl = new JLabel(value == null? "": value.toString()); 
			lbl.setBackground(Color.LIGHT_GRAY);
			lbl.setOpaque(true); 
		
			if(tableResults.getValueAt(row,0).equals("TOTAL SEMANA")) 
				{
				//lbl.setHorizontalAlignment(SwingConstants.RIGHT); //alina a la izquierda 
				lbl.setForeground(Color.BLACK); //fuente azul 
				lbl.setFont(new Font("Arial", Font.BOLD, 12));
				lbl.setBackground(Color.GRAY);
				} 
			if(column != 0){ 
				lbl.setHorizontalAlignment(SwingConstants.RIGHT);
			} 
			return lbl; 
		} 
	}; 
} //XX_ProductFilter