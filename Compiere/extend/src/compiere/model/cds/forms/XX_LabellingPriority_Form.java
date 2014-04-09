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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.CachedRowSetImpl;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_XX_Ref_PriorityMotive;
import compiere.model.cds.forms.indicator.XX_Indicator;



public class XX_LabellingPriority_Form extends CPanel 
implements FormPanel, ActionListener,  ListSelectionListener{

	/**Da una lista ordenada de los pedidos que tienen prioridad de etiquetado
	 *  @author Gabrielle Huchet
	 *  @version 
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */


	/**	Window No	*/
	private int m_WindowNo = 0;
	/**	FormFrame	*/
	private FormFrame m_frame;
	/**	Logger		*/
	static CLogger log = CLogger.getCLogger(XX_LabellingPriority_Form.class);
	
	/* Panel de la ventana */
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("Distribuciones Pendientes de Etiquetar");
	private CPanel xPanel = new CPanel();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private GridBagLayout northLayout = new GridBagLayout();
	private FlowLayout southLayout = new FlowLayout();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	

	/* Botones */
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "Update") + " Lista");
	private CButton bClear = new CButton(Msg.translate(Env.getCtx(), "Clear"));
	private CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "ExportExcel"));
	private CButton bManagePriority = new CButton(Msg.translate(Env.getCtx(), "XX_ManagePriorityDist"));
	private CButton searchSelected = new CButton(Msg.translate(Env.getCtx(), "Search")+ " Distribución");
	
	/* Archivo a exportar*/
	private VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	private CLabel labelFile = new CLabel();
	
	/* Campos del filtro disponibles */
	//Fecha de busqueda
	private VDate dateSearch = new VDate(Msg.translate(Env.getCtx(), "Date"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "Date"));
	private CLabel dateLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_Date"));
	//Distribución (Pedidos)
	private CLabel labelDistribution = new CLabel(Msg.translate(Env.getCtx(), "XX_VMR_DistributionHeader_ID"));
	private CComboBox comboDistribution = new CComboBox();

	//Ubicación  - PROYECTO CD VALENCIA 
	private CLabel locatorLabel =  new CLabel(Msg.translate(Env.getCtx(), "M_Locator_ID"));
	private CComboBox locatorCombo = new CComboBox();
	
	/** La tabla donde se guardarán los datos*/
	private MiniTablePreparator table = new MiniTablePreparator();
	private static int MAX_PRIORIDAD = 1111; 
	
	Hashtable<Integer, Vector<Object>> tableAux;
	
	
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;

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
	/**
	 */
	private void jbInit() throws Exception
	{
			
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);

		bSearch.setEnabled(true);
		bClear.setEnabled(true);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(800, 400));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);

		xScrollPane.getViewport().add(table, null);
		
		northPanel.add(dateLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(dateSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(locatorLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(locatorCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(labelDistribution, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(comboDistribution, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(searchSelected, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		
	
		
		southLayout.setAlignment(FlowLayout.RIGHT);
		southLayout.setHgap(15);		
		southPanel.add(bManagePriority, null);
		southPanel.add(bSearch, null);
	//	southPanel.add(bClear, null);
		southPanel.add(labelFile, null);
		southPanel.add(bFile, null);
		southPanel.add(bPrint, null);
	    southPanel.validate();
	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		m_frame.getRootPane().setDefaultButton(bSearch);
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo("",   ".", Integer.class),        			// Posición
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_DISTRIBUTIONHEADER_ID"),   ".", String.class),        //  1 Distribución
			new ColumnInfo("Departamentos",   ".", String.class),        //  Departamentos
			new ColumnInfo("Marcas",   ".", String.class),        //  Marcas
			new ColumnInfo("Folletos",   ".", String.class),        //  Folletos
			new ColumnInfo(Msg.translate(Env.getCtx(), "ProductQty"),   ".", Integer.class),        //  Cantidad de Productos
			new ColumnInfo("Motivo",   ".", String.class),        //  Motivo
			new ColumnInfo("Prioridades Máx:1,Min:4",   ".", String.class),        //   Prioridades
			new ColumnInfo("Días en Espera",   ".", Integer.class), 		//Días en Espera
			new ColumnInfo("Asistente Asignado",   ".", Boolean.class), 		//Tiene Asistente Asignado
		};
		table.prepareTable(layout, "", "", false, "");
		table.setAutoResizeMode(2);
		table.setSortEnabled(false);

		Calendar actualDate = Calendar.getInstance();
		dateSearch.setValue(actualDate.getTime());
		dateSearch.setEnabled(false);
	
		//  Visual
		CompiereColor.setBackground (this);
		
		//  Listener
		table.getSelectionModel().addListSelectionListener(this);	
		addActionListeners();
		dynLocators();
		uploadDistributions();
		cmd_Search();
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_PlacedOrderLQty"));
		statusBar.setStatusDB(table.getRowCount());

	}   //  dynInit

	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		
		bSearch.addActionListener(this);
		bClear.addActionListener(this);
		bPrint.addActionListener(this);
		comboDistribution.addActionListener(this);
		locatorCombo.addActionListener(this);
		searchSelected.addActionListener(this);
	    bManagePriority.addActionListener(this);
	} 
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		
		bSearch.removeActionListener(this);
		bClear.removeActionListener(this);
	    bPrint.removeActionListener(this);
	    comboDistribution.removeActionListener(this);
	    locatorCombo.removeActionListener(this);
	    searchSelected.removeActionListener(this);
	    bManagePriority.removeActionListener(this);
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


	private void uploadDistributions() {
		comboDistribution.removeActionListener(this);
		KeyNamePair loadKNP;
		String sql = "";						
		comboDistribution.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboDistribution.addItem(loadKNP);
		
		int orgID = 0;
		if(locatorCombo.getSelectedItem()!=null){
			KeyNamePair locator = (KeyNamePair) locatorCombo.getSelectedItem();
			orgID = locator.getKey();
		}
		
		sql = "SELECT PE.XX_VMR_DISTRIBUTIONHEADER_ID, PE.XX_VMR_DISTRIBUTIONHEADER_ID" +
				"\nFROM XX_VMR_ORDER PE WHERE PE.XX_ORDERREQUESTSTATUS = 'PE'" +
				"\nAND PE.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+" AND PE.AD_ORG_ID = "+ orgID +
				"\nGROUP BY PE.XX_VMR_DISTRIBUTIONHEADER_ID" +
				"\nORDER BY PE.XX_VMR_DISTRIBUTIONHEADER_ID"; 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(1));
				comboDistribution.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
		comboDistribution.addActionListener(this);
	}
	
	private boolean dynLocators() {
		locatorCombo.removeActionListener(this);
		String sql = "\nSELECT  W.AD_ORG_ID, W.VALUE||'-'||W.NAME " +
				"\nFROM M_WAREHOUSE W WHERE   W.XX_ISSTORE = 'N' ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		locatorCombo.removeAllItems();
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while(rs.next()) {
				locatorCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {ps.close();}
				catch (Exception e) {}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {}				
			} 
		}		
		locatorCombo.setSelectedIndex(0);
		locatorCombo.addActionListener(this);
		return (locatorCombo.getItemCount() > 1);

	}
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		removeActionListeners();
		if (e.getSource() == bSearch){
			cmd_Search();
		}else if (e.getSource() == bManagePriority){
			AWindow window_pe = new AWindow();
	    	window_pe.initWindow(Env.getCtx().getContextAsInt("#XX_L_W_PLACEDOPRIORITY_ID"), null); 
	    	AEnv.showCenterScreen(window_pe);
		}
		else if (e.getSource() == bClear) {			
			clearFilter();
		}
		else if (e.getSource() == bPrint)
		{
			try {			
				XX_Indicator.imprimirArchivo( table, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}		
		}
		else if (e.getSource() == comboDistribution){
			table.getSelectionModel().removeSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
			for (int i = 0; i < table.getRowCount(); i++) {
				if(comboDistribution != null && comboDistribution.getValue()!=null ){
					if(Integer.parseInt((String) table.getValueAt(i, 1)) == ((KeyNamePair)comboDistribution.getValue()).getKey()){
						table.getSelectionModel().setSelectionInterval(i,i);
						break;
					}
				}
			}
		}else if (e.getSource() == searchSelected){
			table.getSelectionModel().removeSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
			for (int i = 0; i < table.getRowCount(); i++) {
				if(comboDistribution != null && comboDistribution.getValue()!=null ){
					if(Integer.parseInt((String) table.getValueAt(i, 1)) == ((KeyNamePair)comboDistribution.getValue()).getKey()){
						table.getSelectionModel().setSelectionInterval(i,i);
						break;
					}
				}
			}
		}else if (e.getSource() == locatorCombo){
			cmd_Search();
		}
		addActionListeners();
	
		m_frame.setCursor(Cursor.getDefaultCursor());

	}   //  actionPerformed

	 /**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{	
		
		tableLoad();	
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_PlacedOrderLQty"));
		statusBar.setStatusDB(table.getRowCount());
		table.getSelectionModel().removeSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
		for (int i = 0; i < table.getRowCount(); i++) {
			if(comboDistribution != null && comboDistribution.getValue()!=null ){
				if(Integer.parseInt((String) table.getValueAt(i, 1)) == ((KeyNamePair)comboDistribution.getValue()).getKey()){
					table.getSelectionModel().setSelectionInterval(i,i);
					break;
				}
			}
		}
		uploadDistributions();
			

	}   //  cmd_Search
	
	
	/** Limpia el filtro*/
	void clearFilter() {
	
		//dateSearch.setValue(null);
	}
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		removeActionListeners();
		int tableRow = table.getSelectedRow();
			
		if(tableRow==-1) //Si no tengo nada seleccionado en la tabla 
		{
			statusBar.setStatusLine(Msg.translate(Env.getCtx(),"XX_PlacedOrderLQty")); 
			statusBar.setStatusDB(table.getRowCount());
		}
		else
		{
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_PlacedOrdersSelected")); 
			statusBar.setStatusDB((String)table.getValueAt(tableRow, 1)); // Colocar nro de distribución
		
		}
		addActionListeners();
		if (e.getValueIsAdjusting())
			return;

	}   //  valueChanged

	private void qtyValues(Trx trans){
		
	    PreparedStatement stmt_2 =null;
	    ResultSet rs_2 = null;
	    String sql_temp = "";
	    
	    try
		{
			sql_temp = "SELECT PR.XX_VMR_DISTRIBUTIONHEADER_ID, SUM(DE.XX_PRODUCTQUANTITY) " +
						      "FROM XX_VMR_ORDER PE INNER JOIN XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID) "+
						      "INNER JOIN XX_TEMP_PRIORITY PR ON (PR.XX_VMR_DISTRIBUTIONHEADER_ID = PE.XX_VMR_DISTRIBUTIONHEADER_ID) " +
						      "WHERE PE.XX_ORDERREQUESTSTATUS = 'PE' " +
						      "GROUP BY PR.XX_VMR_DISTRIBUTIONHEADER_ID ";
			
			stmt_2 = DB.prepareStatement(sql_temp, trans);
			rs_2 = stmt_2.executeQuery();
			
			while (rs_2.next()) { //cantidad de productos
				
				tableAux.get(rs_2.getInt(1)).add(rs_2.getInt(2));

			}

		}catch (SQLException e){
			log.log(Level.SEVERE, sql_temp, e);
		}
		finally {
			DB.closeStatement(stmt_2);
			DB.closeResultSet(rs_2);
		}	
	}
	
	private void departValues(Trx trans){
		
	    PreparedStatement stmt_2 =null;
	    ResultSet rs_2 = null;
	    String sql_temp = "";
	    String dpts = "";

	    try
		{
			 sql_temp = "SELECT PR.XX_VMR_DISTRIBUTIONHEADER_ID, D.VALUE||'-'||D.NAME " +
					    "FROM XX_VMR_ORDER PE INNER JOIN XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID) " +
					    "INNER JOIN XX_VMR_DEPARTMENT D ON (DE.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
					    "INNER JOIN XX_TEMP_PRIORITY PR ON (PR.XX_VMR_DISTRIBUTIONHEADER_ID = PE.XX_VMR_DISTRIBUTIONHEADER_ID) " +
					    "GROUP BY D.VALUE||'-'||D.NAME, PR.XX_VMR_DISTRIBUTIONHEADER_ID ORDER BY PR.XX_VMR_DISTRIBUTIONHEADER_ID";
			
			stmt_2 = DB.prepareStatement(sql_temp, trans);
			rs_2 = stmt_2.executeQuery();
			int dist = 0;
			int i = 0;
			while (rs_2.next()) { //cantidad de productos
	
				if(dist != rs_2.getInt(1)){
					
					if(i!=0)
						tableAux.get(dist).add(dpts);
	
					dist = rs_2.getInt(1);
					dpts = null;
				}
				
				if(dpts ==null){
				    dpts = rs_2.getString(2);
				}else
				    dpts = dpts+", "+rs_2.getString(2);
				
				i++;
			}
			
			tableAux.get(dist).add(dpts);

		}catch (SQLException e){
			log.log(Level.SEVERE, sql_temp, e);
		}
		finally {
			DB.closeStatement(stmt_2);
			DB.closeResultSet(rs_2);
		}
	}

	private void brandValues(Trx trans){
		
	    PreparedStatement stmt_2 =null;
	    ResultSet rs_2 = null;
	    String sql_temp = "";
	    String brands = "";

	    try
		{
	    	sql_temp = "SELECT PR.XX_VMR_DISTRIBUTIONHEADER_ID, B.NAME " +
					   "FROM XX_VMR_ORDER PE INNER JOIN XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID) " +
					   "INNER JOIN M_PRODUCT P ON (DE.M_PRODUCT_ID =P.M_PRODUCT_ID) " +
					   "INNER JOIN XX_VMR_BRAND B ON (B.XX_VMR_BRAND_ID =P.XX_VMR_BRAND_ID) " +
					   "INNER JOIN XX_TEMP_PRIORITY PR ON (PR.XX_VMR_DISTRIBUTIONHEADER_ID = PE.XX_VMR_DISTRIBUTIONHEADER_ID) " +
					   "GROUP BY B.NAME, PR.XX_VMR_DISTRIBUTIONHEADER_ID ORDER BY PR.XX_VMR_DISTRIBUTIONHEADER_ID";
			
			stmt_2 = DB.prepareStatement(sql_temp, trans);
			rs_2 = stmt_2.executeQuery();
			int dist = 0;
			int i = 0;
			while (rs_2.next()) { //cantidad de productos
	
				if(dist != rs_2.getInt(1)){
					
					if(i!=0)
						tableAux.get(dist).add(brands);
	
					dist = rs_2.getInt(1);
					brands = null;
				}
				
				if(brands ==null){
					brands = rs_2.getString(2);
				}else
					brands = brands +", "+rs_2.getString(2);
				
				i++;
		
			}
			
			tableAux.get(dist).add(brands);

		}catch (SQLException e){
			log.log(Level.SEVERE, sql_temp, e);
		}
		finally {
			DB.closeStatement(stmt_2);
			DB.closeResultSet(rs_2);
		}
	}
	
	private void brochureValues(Trx trans){
		
	    PreparedStatement stmt_2 =null;
	    ResultSet rs_2 = null;
	    String sql_temp = "";
	    String broch = "";

	    try
		{
	    	sql_temp = "SELECT PR.XX_VMR_DISTRIBUTIONHEADER_ID, B.NAME  " +
					   "FROM  XX_VMA_BROCHURE B INNER JOIN XX_VMA_BROCHUREPAGE BP on (B.XX_VMA_BROCHURE_ID= BP.XX_VMA_BROCHURE_ID) " +
					   "INNER JOIN  XX_VME_ELEMENT E ON (BP.XX_VMA_BROCHUREPAGE_ID = E.XX_VMA_BROCHUREPAGE_ID) " +
					   "INNER JOIN  XX_VMR_ORDERREQUESTDETAIL DE  ON ( DE.M_PRODUCT_ID = E.M_PRODUCT_ID) " +
					   "RIGHT OUTER JOIN XX_VMR_ORDER PE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID) " +
					   "INNER JOIN XX_TEMP_PRIORITY PR ON (PR.XX_VMR_DISTRIBUTIONHEADER_ID = PE.XX_VMR_DISTRIBUTIONHEADER_ID) " +
					   "GROUP BY B.NAME, PR.XX_VMR_DISTRIBUTIONHEADER_ID ORDER BY PR.XX_VMR_DISTRIBUTIONHEADER_ID";
			
			stmt_2 = DB.prepareStatement(sql_temp, trans);
			rs_2 = stmt_2.executeQuery();
			int dist = 0;
			int i = 0;
			while (rs_2.next()) { //cantidad de productos
	
				if(dist != rs_2.getInt(1)){
					
					if(i!=0){
						if(broch==null)
							broch="";
							
						tableAux.get(dist).add(broch);
					}
	
					dist = rs_2.getInt(1);
					broch = null;
				}
				
				if(broch ==null){
					broch = rs_2.getString(2);
				}else
					broch = broch +", "+rs_2.getString(2);
				
				i++;
			}
			
			if(broch == null)
				broch = "";
			
			tableAux.get(dist).add(broch);

		}catch (SQLException e){
			log.log(Level.SEVERE, sql_temp, e);
		}
		finally {
			DB.closeStatement(stmt_2);
			DB.closeResultSet(rs_2);
		}
	}
	
private void auxValues(Trx trans){
		
	    PreparedStatement stmt_2 =null;
	    ResultSet rs_2 = null;
	    String sql_temp = "";
	    boolean auxAsig = false;
	    
	    try
		{
	    	sql_temp = "SELECT XX_CHECKASSISTANT_ID, O.XX_VMR_DISTRIBUTIONHEADER_ID FROM XX_VMR_ORDER O INNER JOIN XX_TEMP_PRIORITY PR ON (PR.XX_VMR_DISTRIBUTIONHEADER_ID = O.XX_VMR_DISTRIBUTIONHEADER_ID)";
			
			stmt_2 = DB.prepareStatement(sql_temp, trans);
			rs_2 = stmt_2.executeQuery();
			
			while (rs_2.next()) { //cantidad de productos
	
			if (rs_2.getInt(1)>0)
				auxAsig = true;
			else
				auxAsig = false;
				
			tableAux.get(rs_2.getInt(2)).add(auxAsig);
			}

		}catch (SQLException e){
			log.log(Level.SEVERE, sql_temp, e);
		}
		finally {
			DB.closeStatement(stmt_2);
			DB.closeResultSet(rs_2);
		}
	}

	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private void tableLoad ()
	{
		int orgID = Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCD_ID");
		if(locatorCombo.getSelectedItem()!=null){
			KeyNamePair locator = (KeyNamePair) locatorCombo.getSelectedItem();
			orgID = locator.getKey();
		}
		deleteOCPrioritiesPE();
		table.setRowCount(0);
		Timestamp date= (Timestamp)dateSearch.getValue();	
		String sql_temp = null;
		
		String sql =   "\nWITH "+
		   //LOS PEDIDOS EN ESTADO "PENDIENTE POR ETIQUETAR" QUE SE LES HA AÑADIDO UNA PRIORIDAD MANUALMENTE 
		   "\nPRIORIDAD_FORZADA AS(" +
		   "\nSELECT PE.XX_VMR_ORDER_ID, -P.XX_POSITION||'0000' NIVEL_PRIORIDAD, 0 PRIORIDAD_SECUNDARIA " +
		   "\nFROM XX_VMR_PRIORITY P INNER JOIN XX_VMR_ORDER PE ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = PE.XX_VMR_DISTRIBUTIONHEADER_ID) " +
		   "\nWHERE  PE.AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID()+" AND PE.XX_ORDERREQUESTSTATUS = 'PE'   AND  P.ISACTIVE = 'Y' " +
		   "\n)," +
		   //LOS PEDIDOS EN ESTADO "PENDIENTE POR ETIQUETAR" QUE ESTAN EN EL PROXIMO FOLLETO, Y FALTAN MENOS DE 1 MES PARA QUE SALGA DICHO FOLLETO
		   "\nPRIORIDAD_1 AS("+
		   "\nSELECT PE.XX_VMR_ORDER_ID, '1000' NIVEL_PRIORIDAD, 0 PRIORIDAD_SECUNDARIA  "+
		   "\nFROM  XX_VMR_ORDER PE INNER JOIN  XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID) "+
		   "\nWHERE PE.XX_ORDERREQUESTSTATUS = 'PE' AND "+
		   "\nDE.M_PRODUCT_ID IN "+
		   "\n(SELECT E.M_PRODUCT_ID  "+
		   "\nFROM XX_VMA_MARKETINGACTIVITY M, XX_VMA_BROCHURE B, XX_VMA_BROCHUREPAGE BP, XX_VME_ELEMENT E "+
		   "\nWHERE M.XX_VMA_BROCHURE_ID= B.XX_VMA_BROCHURE_ID AND  "+
		   "\nB.XX_VMA_BROCHURE_ID= BP.XX_VMA_BROCHURE_ID AND  "+
		   "\nBP.XX_VMA_BROCHUREPAGE_ID = E.XX_VMA_BROCHUREPAGE_ID  AND  "+
		   "\nM.STARTDATE IN (SELECT MIN(STARTDATE) FROM XX_VMA_MARKETINGACTIVITY WHERE "+DB.TO_DATE(date,true)+" < STARTDATE  "+
		   "\nAND "+DB.TO_DATE(date,true)+" >= ADD_MONTHS(STARTDATE,-1))) "+
		   "\nGROUP BY PE.XX_VMR_ORDER_ID "+
		   "\nORDER BY COUNT(DE.XX_VMR_ORDERREQUESTDETAIL_ID), PE.XX_VMR_ORDER_ID "+
		   "\n),"+
		   //LOS PEDIDOS EN ESTADO "PENDIENTE POR ETIQUETAR" QUE PERTENECEN A LA COLECCIÓN ACTUAL
		   "\nPRIORIDAD_2 AS ("+
		   "\nSELECT  PE.XX_VMR_ORDER_ID, '100' NIVEL_PRIORIDAD, 0 PRIORIDAD_SECUNDARIA  "+
		   "\nFROM XX_VMR_ORDER PE INNER JOIN  XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID)"+
		   "\nINNER JOIN XX_VMR_LINE L ON (L.XX_VMR_LINE_ID = DE.XX_VMR_LINE_ID AND L.XX_VMR_TYPEINVENTORY_ID = 1000027) "+
		   "\nINNER JOIN XX_VMR_COLLECTION C ON (C.XX_VMR_COLLECTION_ID = DE.XX_VMR_COLLECTION_ID "+
		   "\nAND TO_CHAR("+DB.TO_DATE(date,true)+",'MM') IN (C.XX_STARTINGMONTH, C.XX_ENDINGMONTH)) "+
		   "\nWHERE PE.XX_ORDERREQUESTSTATUS = 'PE'  "+
		   "\nGROUP BY  PE.XX_VMR_ORDER_ID "+
		   "\n)," +
		   "\nINVENTARIO AS ( "+
		   "\nSELECT SUM(XX_FINALINVAMOUNTBUD2) INVPRE, SUM(XX_INVAMOUNTFINALREAL) INVREAL, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
		   "\nFROM XX_VMR_PRLD01 " +
		   "\nWHERE  XX_BUDGETYEARMONTH =  TO_CHAR("+DB.TO_DATE(date,true)+",'YYYYMM')"+
		   "\nAND M_WAREHOUSE_ID != "+ Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID")+
		   "\nGROUP BY M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID" +
		   "\n), " +
		   //LOS PEDIDOS EN ESTADO "PENDIENTE POR ETIQUETAR" QUE TIENEN MENOR INVENTARIO QUE EL PRESUPUESTADO
		   "\nPRIORIDAD_3 AS (" +
		   "\nSELECT PE.XX_VMR_ORDER_ID, '10' NIVEL_PRIORIDAD, SUM(I.INVPRE-I.INVREAL) PRIORIDAD_SECUNDARIA  " +
		   "\nFROM XX_VMR_ORDER PE INNER JOIN  XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID)" +
		   "\nINNER JOIN INVENTARIO I ON (DE.XX_VMR_DEPARTMENT_ID = I.XX_VMR_DEPARTMENT_ID AND DE.XX_VMR_LINE_ID = I.XX_VMR_LINE_ID) " +
		   "\nWHERE I.INVPRE > I.INVREAL AND PE.XX_ORDERREQUESTSTATUS = 'PE' " +
		   "\nGROUP BY PE.XX_VMR_ORDER_ID " +
		   "\n)," +
		   "\nVENTAS AS (" +
		   "\nSELECT SUM(XX_SALESAMOUNTBUD2) VENTASPRE, SUM(XX_AMOUNTACTUALSALE) VENTASREAL, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
		   "\nFROM XX_VMR_PRLD01 " +
		   "\nWHERE  XX_BUDGETYEARMONTH = TO_CHAR("+DB.TO_DATE(date,true)+",'YYYYMM')"+
		   "\nAND M_WAREHOUSE_ID != "+ Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID")+
		   "\nGROUP BY M_WAREHOUSE_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
		   "\n), " +
		   //LOS PEDIDOS EN ESTADO "PENDIENTE POR ETIQUETAR" QUE TIENEN MAYOR VENTA QUE LA PRESUPUESTADA
		   "\nPRIORIDAD_4 AS( " +
		   "\nSELECT PE.XX_VMR_ORDER_ID, '1' NIVEL_PRIORIDAD, SUM(V.VENTASREAL-V.VENTASPRE)  PRIORIDAD_SECUNDARIA" +
		   "\nFROM XX_VMR_ORDER PE INNER JOIN  XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID) " +
		   "\nINNER JOIN VENTAS V ON (DE.XX_VMR_DEPARTMENT_ID = V.XX_VMR_DEPARTMENT_ID AND DE.XX_VMR_LINE_ID = V.XX_VMR_LINE_ID) " +
		   "\nWHERE V.VENTASPRE < V.VENTASREAL AND PE.XX_ORDERREQUESTSTATUS = 'PE' " +
		   "\nGROUP BY PE.XX_VMR_ORDER_ID " +
		   "\n)," +
		   //LOS PEDIDOS EN ESTADO "PENDIENTE POR ETIQUETAR" 
		   "\nPOR_ETIQUETAR AS( " +  
		   "\nSELECT PE.XX_VMR_ORDER_ID, '0' NIVEL_PRIORIDAD, " +
		   "\nCASE WHEN SUM(V.VENTASREAL-V.VENTASPRE) IS NULL THEN -999999999 ELSE SUM(V.VENTASREAL-V.VENTASPRE) END PRIORIDAD_SECUNDARIA " +
		   "\nFROM XX_VMR_ORDER PE INNER JOIN  XX_VMR_ORDERREQUESTDETAIL DE ON (PE.XX_VMR_ORDER_ID = DE.XX_VMR_ORDER_ID) " +
		   "\nLEFT JOIN VENTAS V ON (DE.XX_VMR_DEPARTMENT_ID = V.XX_VMR_DEPARTMENT_ID AND DE.XX_VMR_LINE_ID = V.XX_VMR_LINE_ID) " +
		   "\nWHERE PE.XX_ORDERREQUESTSTATUS = 'PE' " +
		   "\nGROUP BY PE.XX_VMR_ORDER_ID " +
		   "\n)," +
		   "\nTODAS_PRIORIDADES AS (" +
		   "\nSELECT * FROM  PRIORIDAD_1 UNION " +
		   "\nSELECT * FROM  PRIORIDAD_2 UNION " +
		   "\nSELECT * FROM  PRIORIDAD_3 UNION " +
		   "\nSELECT * FROM  PRIORIDAD_4 UNION " +
		   "\nSELECT * FROM  POR_ETIQUETAR UNION " +
		   "\nSELECT * FROM  PRIORIDAD_FORZADA " +
		   "\n)"+
		   "\nSELECT DH.XX_VMR_DISTRIBUTIONHEADER_ID," +
		   "\nCASE WHEN MIN(NIVEL_PRIORIDAD)< 0 THEN TO_NUMBER(MIN(NIVEL_PRIORIDAD)) ELSE SUM(DISTINCT NIVEL_PRIORIDAD) END," +
		   "\nCASE WHEN SUM(DISTINCT NIVEL_PRIORIDAD)=1111 THEN 'Prioridades 1,2,3,4' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1110 THEN 'Prioridades 1,2,3' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1101 THEN 'Prioridades 1,2,4' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1100 THEN 'Prioridades 1,2' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1011 THEN 'Prioridades 1,3,4' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1010 THEN 'Prioridades 1,3' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1001 THEN 'Prioridades 1,4' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1000 THEN 'Prioridad 1' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=111 THEN 'Prioridad 2,3,4' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=110 THEN 'Prioridad 2,3' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=101 THEN 'Prioridad 2,4' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=100 THEN 'Prioridad 2' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=11 THEN 'Prioridad 3,4' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=10 THEN 'Prioridad 3' "+
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=1 THEN 'Prioridad 4' " +
		   "\nWHEN  SUM(DISTINCT NIVEL_PRIORIDAD)=0 THEN 'Sin Prioridad' ELSE 'Prioridad Forzada' END PRIORIDADES, " +
		   "\nTRUNC(AVG(SYSDATE - PE.CREATED)) " +
		   "\nFROM  TODAS_PRIORIDADES T INNER JOIN XX_VMR_ORDER PE ON (PE.XX_VMR_ORDER_ID = T.XX_VMR_ORDER_ID AND PE.AD_ORG_ID = "+ orgID +") " +
		   "\nINNER JOIN XX_VMR_DISTRIBUTIONHEADER DH ON (PE.XX_VMR_DISTRIBUTIONHEADER_ID = DH.XX_VMR_DISTRIBUTIONHEADER_ID)" +
		   "\nLEFT JOIN XX_VMR_ORDERREQUESTDETAIL DE ON (DE.XX_VMR_ORDER_ID = PE.XX_VMR_ORDER_ID) " +
		   "\nGROUP BY DH.XX_VMR_DISTRIBUTIONHEADER_ID " +
		   "\nORDER BY SUM(DISTINCT NIVEL_PRIORIDAD) DESC, SUM(PRIORIDAD_SECUNDARIA) DESC, TRUNC(AVG(SYSDATE - PE.CREATED)) DESC, DH.XX_VMR_DISTRIBUTIONHEADER_ID";
	
        //System.out.println(sql);
        PreparedStatement stmt =null;
        ResultSet rs =null;
        
        PreparedStatement stmt_2 =null;
        ResultSet rs_2 = null;
        
		try {
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			
			int row = 0;
			int rowPrio = 0;
			String sqlInsert = "";
			Trx trans = Trx.get("trans");
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			tableAux = new Hashtable<Integer, Vector<Object>>();
			
			while(crs.next()){
				
				sqlInsert = "INSERT INTO XX_TEMP_PRIORITY "
				         + "(XX_VMR_DISTRIBUTIONHEADER_ID)"
				         + " VALUES ("+ crs.getInt(1) +") ";
				DB.executeUpdate(trans, sqlInsert);
				
				tableAux.put(crs.getInt(1), new Vector<Object>());
			}
			
			qtyValues(trans);
			departValues(trans);
			brandValues(trans);
			brochureValues(trans);
			auxValues(trans);
			
			trans.commit();
			
			crs.beforeFirst();
			
			while (crs.next()) {
				
				String prioridad = crs.getString(2);
				String posicion = null;
				table.setRowCount(row + 1);
				String dpts = String.valueOf(tableAux.get(crs.getInt(1)).get(1)); 
				String brands = String.valueOf(tableAux.get(crs.getInt(1)).get(2));
				Integer productQty = Integer.parseInt(tableAux.get(crs.getInt(1)).get(0).toString());
				String brochure = String.valueOf(tableAux.get(crs.getInt(1)).get(3));
				String motive= null;
				Boolean auxAsig = Boolean.valueOf(tableAux.get(crs.getInt(1)).get(4).toString());
			
				
				if(Integer.parseInt(prioridad)*(-1) > MAX_PRIORIDAD ){
					posicion = prioridad.substring(1, prioridad.length()-4);
					try
					{
						sql_temp = "\nSELECT XX_MOTIVE " +
								"\nFROM XX_VMR_PRIORITY P "+
								"\nWHERE XX_VMR_DISTRIBUTIONHEADER_ID = "+crs.getInt(1);
						stmt_2 = DB.prepareStatement(sql_temp, null);
						rs_2 = stmt_2.executeQuery();
						while (rs_2.next()) {  //cantidad de productos
							for (X_Ref_XX_Ref_PriorityMotive v : X_Ref_XX_Ref_PriorityMotive.values()) {
								if (v.getValue().compareTo(rs_2.getString(1))==0)
									motive = v.name();
							}
						}
					
					}catch (SQLException e){
						//System.out.println("Error agregando motivos: "+e);
						log.log(Level.SEVERE, sql_temp, e);
					}
					finally {
						DB.closeResultSet(rs_2);
						DB.closeStatement(stmt_2);
					}
					if (Integer.parseInt(posicion)< row){
					//	System.out.println("posicion menor q total:"+rs.getString(1)+", "+rs.getString(2)+", "+posicion);
						rowPrio = Integer.parseInt(posicion)-1;
						moveRow(rowPrio, row-1, row);
						table.setValueAt(rowPrio+1, rowPrio, 0); 
						table.setValueAt(crs.getString(1), rowPrio, 1); //Pedido
						table.setValueAt(dpts, rowPrio, 2); // Departamentos
						table.setValueAt(brands, rowPrio, 3); // Marcas
						table.setValueAt(brochure, rowPrio,4 ); // Folletos
						table.setValueAt(productQty, rowPrio, 5); //Cantidad de productos
						table.setValueAt(motive, rowPrio, 6); //Motivo
						table.setValueAt(crs.getString(3), rowPrio, 7); //Prioridades
						table.setValueAt(crs.getInt(4), rowPrio, 8); //Días en Espera
						table.setValueAt(auxAsig, rowPrio, 9); //Asistente Asignado
					}else {
					//	System.out.println("posicion mayor q total:"+rs.getString(1)+","+rs.getString(2));
						table.setValueAt(row+1, row, 0); 
						table.setValueAt(crs.getString(1), row, 1); //Pedido
						table.setValueAt(dpts, row, 2); // Departamentos
						table.setValueAt(brands, row, 3); // Marcas
						table.setValueAt(brochure, row, 4); // Marcas
						table.setValueAt(productQty, row, 5); //Cantidad de productos
						table.setValueAt(motive, row, 6); //Motivo
						table.setValueAt(crs.getString(3), row, 7); //Prioridades
						table.setValueAt(crs.getInt(4), row, 8); //Días en Espera
						table.setValueAt(auxAsig, row, 9); //Asistente Asignado
					}
				}else {
				//	System.out.println("menor prioridad:"+rs.getString(1)+","+rs.getString(2));
					table.setValueAt(row+1, row, 0); 
					table.setValueAt(crs.getString(1), row, 1); //Pedido
					table.setValueAt(dpts, row, 2); // Departamentos
					table.setValueAt(brands, row, 3); // Marcas
					table.setValueAt(brochure, row, 4); // Marcas
					table.setValueAt(productQty, row, 5); //Cantidad de productos
					table.setValueAt("", row, 6); //Motivo
					table.setValueAt(crs.getString(3), row, 7); //Prioridades
					table.setValueAt(crs.getInt(4), row, 8); //Días en Espera
					table.setValueAt(auxAsig, row, 9); //Asistente Asignado
				}
				row++;
			}
		
		}catch (SQLException e){
			log.log(Level.SEVERE, sql, e);
		}finally {

			DB.closeResultSet(rs);
			DB.closeStatement(stmt);	
		}
	}   //  tableLoad

	/**Mueve una o más filas comenzando desde ini hasta end 
	 * y las coloca una fila mas abajo hasta llegar a newEnd */
	private void moveRow(int ini, int end, int newEnd) {
		int row = newEnd;
		for (int j = end; j >= ini; j--) {
			table.setValueAt(row+1, row, 0); 				  // posición
			table.setValueAt(table.getValueAt(j, 1), row, 1); // Pedido
			table.setValueAt(table.getValueAt(j, 2), row, 2); // Departamentos
			table.setValueAt(table.getValueAt(j, 3), row, 3); // Marcas
			table.setValueAt(table.getValueAt(j, 4), row, 4); // Folletos
			table.setValueAt(table.getValueAt(j, 5), row, 5); // Cantidad de productos
			table.setValueAt(table.getValueAt(j, 6), row, 6); // Motivo
			table.setValueAt(table.getValueAt(j, 7), row, 7); // Prioridades 
			table.setValueAt(table.getValueAt(j, 8), row, 8); // Días en Espera
			table.setValueAt(table.getValueAt(j, 9), row, 9); //Asistente Asignado
			row =row-1;
		}
	}
	
	/** Elimina de la tabla XX_VMR_PRIORITY las distribuciones que ya fueron chequeadas */
	private void deleteOCPrioritiesPE() {
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		
		String sql = "\nSELECT P.XX_VMR_DISTRIBUTIONHEADER_ID " +
				"\nFROM XX_VMR_PRIORITY P INNER JOIN XX_VMR_ORDER PE ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = PE.XX_VMR_DISTRIBUTIONHEADER_ID)" +
				"\nWHERE PE.XX_ORDERREQUESTSTATUS != 'PE' ";
		try {
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
		
				String sqlDelete = "DELETE FROM XX_VMR_PRIORITY WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " +rs.getInt(1);	
				DB.executeUpdate(null, sqlDelete);
		
			}
		}catch (Exception e) {
			System.out.println("\nError eliminando distribución de la tabla de prioridades " +e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(stmt);
		}
	}


}

