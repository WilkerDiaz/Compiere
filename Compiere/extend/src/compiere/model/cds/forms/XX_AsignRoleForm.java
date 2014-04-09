package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MiniTablePreparator;

/**
 * 	Forma para asignar funcionalidad a roles.
 * @author Gabriela Marques
 *
 */

public class XX_AsignRoleForm  extends CPanel implements FormPanel, ActionListener,
TableModelListener, ListSelectionListener {
		

	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		try {
			//	UI
			jbInit(); // Layouts
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
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
	static CLogger 			log = CLogger.getCLogger(XX_CheckupDaysForm.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static StringBuffer    m_sqlDetail = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	String		   m_selectT1 = ""; //Tabla1
	String		   m_selectT2 = ""; //Tabla detalle
	static StringBuilder sql;
	
	
	// LAYOUT >>>>>>>>
		//panel - tablas
		private CPanel mainPanel = new CPanel();
		private BorderLayout mainLayout = new BorderLayout();
		private CPanel northPanel = new CPanel(); 
		private CPanel centerPanel = new CPanel(); 
		private BorderLayout northLayout = new BorderLayout();
		private BorderLayout centerLayout = new BorderLayout();
		private CPanel panelItems = new CPanel();
		private CPanel panelSearch = new CPanel();
		private GridBagLayout parameterLayout = new GridBagLayout();
		private CPanel commandPanel = new CPanel();
		private FlowLayout commandLayout = new FlowLayout();
		private FlowLayout searchLayout = new FlowLayout();
//		private CPanel southPanel = new CPanel();
//		private BorderLayout southLayout = new BorderLayout();
//		private StatusBar statusBar = new StatusBar();
		private JScrollPane dataPane = new JScrollPane();
		private CPanel resultPanel = new CPanel();
		private GridBagLayout resultLayout = new GridBagLayout();
	// FIN LAYOUT <<<<<<<<<<<
	
	// FILTROS >>>>>>>>>>>
		//Checkboxes
		private CLabel labelRol = new CLabel("Rol");
		private CCheckBox checkRol = new CCheckBox();
		private CLabel labelVPF = new CLabel("Ventana/Proceso/Forma");
		private CCheckBox checkVPF = new CCheckBox();
		// OPCION Rol
		private CLabel labelRolList = new CLabel("Rol:");
		private static CComboBox rolSearch = new CComboBox();
		private CLabel labelItemList = new CLabel("Item:");
		private static CComboBox comboItemRol = new CComboBox();	
		// OPCION VPF
		// Ventana
		private CLabel labelWindow = new CLabel("Ventana:");
		private static CComboBox windowSearch = new CComboBox();
		// Proceso
		private CLabel labelProcess = new CLabel("Proceso:");
		private static CComboBox processSearch = new CComboBox();
		// Forma
		private CLabel labelForm = new CLabel("Forma:");
		private static CComboBox formSearch = new CComboBox();
		
		private CLabel searchLabel = new CLabel();
		
		
		// Seleccionar todos
		private CLabel allActiveLabel = new CLabel("Marcar todos Activo");
		private CCheckBox allActive = new CCheckBox();
		private CLabel allRWLabel = new CLabel("Marcar todos Lectura/Escritura");
		private CCheckBox allRW = new CCheckBox();
		Boolean allChanged = false; // Flag para indicar si se utilizaron los check allActive/ allChanged
		
	// FIN FILTROS <<<<<<<
	
	//buttons
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bSave = ConfirmPanel.createSaveButton(true);	
	private CButton bSearch = new CButton();
	private JButton bReset = ConfirmPanel.createResetButton(true);
	private TitledBorder xBorder = new TitledBorder("asd");
	private MiniTablePreparator miniTable = new MiniTablePreparator();

	// Listas
	private ArrayList<Integer> updateList = new ArrayList<Integer>();
	
	// Opciones seleccionadas
	KeyNamePair rolSel, itemSel, winSel, procSel, formSel = new KeyNamePair();
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
	private void jbInit() throws Exception {

//		removeActionListeners();

		//  Visual
		CompiereColor.setBackground (this);
		m_frame.getRootPane().setDefaultButton(bSearch);
		
		
		// NORTHPANEL
		northPanel.setLayout(northLayout);
		
		//ITEMS
		panelItems();
			
		northPanel.add(panelItems, BorderLayout.NORTH);
//		northPanel.add(commandPanel, BorderLayout.SOUTH);
		//+++++++++++++++++++++++++
		
		//PANEL CENTRAL
		resultPanel.setLayout(resultLayout);
		
		panelSearch.setLayout(searchLayout);
		searchLayout.setAlignment(FlowLayout.LEFT);
		searchLayout.setHgap(10);
		panelSearch.add("label", searchLabel);
		
		dataPane.getViewport().add(resultPanel);
		dataPane.setPreferredSize(new Dimension(900, 600));
//		
		dataPane.setBorder(null);
		centerPanel.setLayout(centerLayout);
		centerPanel.add(panelSearch, BorderLayout.NORTH);
		centerPanel.add(dataPane, BorderLayout.CENTER);
		
		// MAIN PANEL
		mainPanel.setLayout(mainLayout);	
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		//PANEL DE COMANDOS (bajo main panel)
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(allActive, null);
		commandPanel.add(allActiveLabel, null);
		commandPanel.add(allRW, null);
		commandPanel.add(allRWLabel, null);
		commandPanel.add(bCancel, null);
		bReset.setPreferredSize(new Dimension(60,37));	
		commandPanel.add(bReset, null);
		commandPanel.add(bSave, null);
		
//		addActionListeners();
		
			
	}   //  jbInit

	
	/**
	 * 	 Función que crea el panel de filtro
	 */
	private void panelItems() {
		panelItems.setLayout(parameterLayout);
		
		//********PARÁMETROS********//
		int i=0;
		//CHECKS
		panelItems.add(checkRol, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 60, 5, 5), 0, 0));//new Insets(8, 60, 5, 5), 0, 0));
		panelItems.add(labelRol, new GridBagConstraints(2, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(8, 0, 5, 5), 0, 0));	//new Insets(8, 0, 5, 5), 0, 0));		
		// CHECK VPF
		panelItems.add(checkVPF, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 5, 5), 0, 0));//new Insets(8, 5, 5, 5), 0, 0));
		panelItems.add(labelVPF, new GridBagConstraints(5, i++, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(8, 0, 5, 100), 0, 0));//new Insets(8, 0, 5, 130), 0, 0));

		// ITEM	
		panelItems.add(labelItemList, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 60, 5, 5), 0, 0));
		panelItems.add(comboItemRol, new GridBagConstraints(3, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 100), 0, 0));
		
		// ROL	
		panelItems.add(labelRolList, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		panelItems.add(rolSearch, new GridBagConstraints(6, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 5), 0, 0));
	
		//Las siguientes tres dependen de lo seleccionado en item
		
//		 Ventana	
		panelItems.add(labelWindow, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		panelItems.add(windowSearch, new GridBagConstraints(6, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 5), 0, 0));
		
		
		// Proceso	
		panelItems.add(labelProcess, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		panelItems.add(processSearch, new GridBagConstraints(6, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 5), 0, 0));

		// Forma
		panelItems.add(labelForm, new GridBagConstraints(4, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		panelItems.add(formSearch, new GridBagConstraints(6, i, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 5), 0, 0));
		
		// Botón buscar
		bSearch.setText("Buscar");
		bSearch.setPreferredSize(new Dimension(100,28));	
		bSearch.setEnabled(true);
		panelItems.add(bSearch, new GridBagConstraints(7, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 25), 0, 0));
	}
	
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit() 	{	
		
		//Configuración por defecto
		loadBasicInfo();
	}   //  dynInit


	/**
	 *  Función que establece el estado inicial de los elementos de la forma 
	 */
	private void loadBasicInfo() {
		removeActionListeners();
		
		//Llenar los filtros de busquedas
		llenarcombosRol();
				
		//Por defecto opción ROL
		configRol();
		
		// Botones inferiores ocultos
		commandPanel.setVisible(false);
		
		addActionListeners();
		
	}
	
	

	/**
	 * 	Dispose
	 */
	public void dispose() 	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}	//	dispose


	/**
	 * 	Función que carga los roles al combo correspondiente.
	 */
	private  void llenarcombosRol(){
//		removeActionListeners();
		KeyNamePair blanco = new KeyNamePair(0, new String()); //En blanco
		
		comboItemRol.removeAllItems();
		comboItemRol.addItem(blanco);
		comboItemRol.addItem(new KeyNamePair(1, "Ventana"));
		comboItemRol.addItem(new KeyNamePair(2, "Proceso"));
		comboItemRol.addItem(new KeyNamePair(3, "Forma"));
		
		rolSearch.removeAllItems();

		// Cargar roles
		String sql = "SELECT DISTINCT AD_ROLE_ID, NAME" +
				" FROM  AD_ROLE where isactive = 'Y' ORDER BY Name ASC";
		KeyNamePair loadKNP = new KeyNamePair(0, new String()); //Blanco
		rolSearch.addItem(loadKNP); //
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				rolSearch.addItem(loadKNP); 
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
	}
	
	/**
	 * 	Función que establece las configuraciones por defecto en la opción ROL
	 */
	private void configRol() {
		
		checkRol.setValue(true);
		checkVPF.setValue(false);
		
		labelRolList.setVisible(true);
		rolSearch.setVisible(true);
		comboItemRol.setSelectedIndex(0);
		rolSearch.setSelectedIndex(0);
		//Ocultar campos
		labelWindow.setVisible(false);
		windowSearch.setVisible(false);
		labelProcess.setVisible(false);
		processSearch.setVisible(false);
		labelForm.setVisible(false);
		formSearch.setVisible(false);
	}
	
	/**
	 * 	Función que establece las configuraciones por defecto en la opción Ventana/Proceso/Forma
	 */
	private void configVPF() {
		
		checkRol.setValue(false);
		checkVPF.setValue(true);
		//Ocultar campos
		labelRolList.setVisible(false);
		rolSearch.setVisible(false);
		comboItemRol.setSelectedIndex(0);
		
	}
	
	
	/** 
	 * 	Función que inicializa el combobox del item seleccionado.
	 * @param item
	 * @param tabla
	 */
	private void cargarItem(CComboBox item, String tabla) {
		
		item.removeAllItems();

		// Cargar item
		String sql = "\nselect t."+tabla+"_id, t.name||decode(mtrl.name, null,' ',t.name, ' ','  -  '||mtrl.name)" +
				"\nfrom "+tabla+" t " +
				"\nleft join ad_menu m on (t."+tabla+"_id = m."+tabla+"_id and m.isactive = 'Y' ) " +
				"\nleft join ad_menu_trl mtrl on (mtrl.ad_menu_id = m.ad_menu_id ) " +
				"\nwhere t.isactive = 'Y'" +
				"\norder by t.name";
		System.out.println(sql);
		KeyNamePair loadKNP = new KeyNamePair(0, new String()); //Blanco
		item.addItem(loadKNP); //
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				item.addItem(loadKNP); // Referencias
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e) {		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// Botón Cancelar
		if (e.getSource() == bCancel) {
			dispose();
		}
		// Check Rol
		else if(e.getSource() == checkVPF && !((Boolean) checkVPF.getValue())
				|| (e.getSource() == checkRol && ((Boolean) checkRol.getValue()))) {
			removeActionListeners();
			
			// Coloca la configuración por defecto para ROL
			configRol();

			dataPane.validate();
			resultPanel.validate();

			addActionListeners();
		} 
		// Check VPF
		else if ( (e.getSource() == checkRol && !((Boolean) checkRol.getValue()))
				|| (e.getSource() == checkVPF && ((Boolean) checkVPF.getValue())) ) {
			removeActionListeners();
			
			// Coloca la configuración por defecto
			configVPF();
			
			dataPane.validate();
			resultPanel.validate();

			addActionListeners();
		}
		else 
			if (e.getSource() == comboItemRol && !(Boolean) checkVPF.getValue() &&  (Boolean) checkRol.getValue()  ) {
			
		}
		// Combo item opción rol
		else
			if (e.getSource() == comboItemRol && ((Boolean) checkVPF.getValue()) &&  !((Boolean) checkRol.getValue())  ) {			
			removeActionListeners();
			KeyNamePair coll = (KeyNamePair)comboItemRol.getSelectedItem();
			if (coll != null && coll.getKey() != 0 ){ 
				if (coll.getKey() == 1) {  // Ventana
					labelWindow.setVisible(true);
					windowSearch.setVisible(true);
					cargarItem(windowSearch, "ad_window");
					
					labelProcess.setVisible(false);
					processSearch.setVisible(false);
					labelForm.setVisible(false);
					formSearch.setVisible(false);
				} else if (coll.getKey() == 2) { // Proceso
					labelProcess.setVisible(true);
					processSearch.setVisible(true);
					cargarItem(processSearch, "ad_process");
					
					labelWindow.setVisible(false);
					windowSearch.setVisible(false);
					labelForm.setVisible(false);
					formSearch.setVisible(false);
				} else if (coll.getKey() == 3) { // Forma
					labelForm.setVisible(true);
					formSearch.setVisible(true);
					cargarItem(formSearch, "ad_form");
					
					labelWindow.setVisible(false);
					windowSearch.setVisible(false);
					labelProcess.setVisible(false);
					processSearch.setVisible(false);
				} 
			}	else {
				labelWindow.setVisible(false);
				windowSearch.setVisible(false);
				labelProcess.setVisible(false);
				processSearch.setVisible(false);
				labelForm.setVisible(false);
				formSearch.setVisible(false);
			}
			addActionListeners();
		}
		// Botón de búsqueda
		else if (e.getSource() == bSearch) {
			removeActionListeners();
			// Actualizar variables
			rolSel = (KeyNamePair)rolSearch.getSelectedItem();
			itemSel = (KeyNamePair)comboItemRol.getSelectedItem();
			winSel = (KeyNamePair)windowSearch.getSelectedItem();	
			procSel = (KeyNamePair)processSearch.getSelectedItem();	
			formSel = (KeyNamePair)formSearch.getSelectedItem();
			
			cmd_Search();
			// Hacer visible los botones inferiores
			commandPanel.setVisible(true);
			addActionListeners();
		} 
		// Botón guardar
		else if (e.getSource() == bSave) {
			if ( ADialog.ask(m_WindowNo, new Container(), 
						Msg.translate(Env.getCtx(), "XX_ConfirmBeforeSave")
						//"¿Guardar cambios?"
						) ) {
				cmd_Save();
			}
		}
		// Botón Reset: vuelve a realizar el query sin guardar nada y sin tomar en cuenta
		// las opciones seleccionadas actualmente
		else if (e.getSource() == bReset) {
			removeActionListeners();
			cmd_Search();
			addActionListeners();
		}
		// Toggle active
		else if (e.getSource() == allActive) {
			removeActionListeners();
			if ((Boolean) allActive.getValue()) { // Todos los active a true
				toggleActive(true, columnasAgregadas.size()-2);
			} else { //todos los active a false
				toggleActive(false, columnasAgregadas.size()-2);
			}
			addActionListeners();
		}
		// Toggle readwrite
		else if (e.getSource() == allRW) {
			removeActionListeners();
			if ((Boolean) allRW.getValue()) { // Todos los readwrite a true
				allActive.setValue(true);
				toggleActive(true, columnasAgregadas.size()-2); // forzar active true
				toggleActive(true, columnasAgregadas.size()-1);
			} else { //todos los active a false
				toggleActive(false, columnasAgregadas.size()-1);
			}
			addActionListeners();
		}
		else {}
	}   //  actionPerformed

	
	
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()  {

		resultPanel.removeAll();
		updateList.clear();
		allChanged = false;
		allActive.setValue(false);
		allRW.setValue(false);
//		resultPanel.validate();
		
		// Opción seleccionada ROL
		if ((Boolean) checkRol.getValue()) {
			if (itemSel == null  | itemSel.getKey() == 0) {				
				ADialog.error(m_WindowNo, m_frame, "Seleccione Ítem");
				return;
			} else if (rolSel == null | rolSel.getKey() == 0) {				
				ADialog.error(m_WindowNo, m_frame, "Seleccione Rol");
				return;
			}
//				panelSearch.removeAll();
//				panelSearch.add(new CLabel("Rol: " +rol.getName()), null);
			searchLabel.setText("Rol: " +rolSel.getName());
			xBorder.setTitle(itemSel.getName());
			dataPane.setBorder(xBorder);
			
			//SQL
			String sql = "";
			if (itemSel.getKey() == 1) { //Ventana
				// Cargar Ventanas
				sql = " SELECT w.ad_window_id, " +
						" w.name, w.description, e.name, mtrl.name, " +
						" case wa.isactive when 'Y' then '1' else '0' end active, " +
						" case wa.isreadwrite when 'Y' then '1' else '0' end readwrite " +
						" FROM ad_window w join ad_entitytype e using (entitytype)" +
						" left join ad_window_access wa on (w.ad_window_id = wa.ad_window_id and ad_role_id = " + rolSel.getKey() + ")" +
						" left join ad_menu m on (w.ad_window_id = m.ad_window_id and m.isactive = 'Y')" +
						" left join ad_menu_trl mtrl on (mtrl.ad_menu_id = m.ad_menu_id )" +
						" where w.isactive = 'Y' and trim(w.name) is not null " +
						" order by w.name";
				
			} else if (itemSel.getKey() == 2) { // Proceso
				// Cargar Procesos
				sql = " SELECT p.ad_process_id,  p.name, p.description, e.name, mtrl.name, " +
						" case pa.isactive when 'Y' then '1' else '0' end active," +
						" case pa.isreadwrite when 'Y' then '1' else '0' end readwrite" +
						" FROM ad_process p join ad_entitytype e using (entitytype)" +
						" left join ad_process_access pa on (p.ad_process_id = pa.ad_process_id and ad_role_id = " + rolSel.getKey() + ")" +
						" left join ad_menu m on (p.ad_process_id = m.ad_process_id and m.isactive = 'Y')" +
						" left join ad_menu_trl mtrl on (mtrl.ad_menu_id = m.ad_menu_id )" +
						" where p.isactive = 'Y' and trim(p.name) is not null ";
				
			} else if (itemSel.getKey() == 3) { // Forma
				// Cargar Formas
				sql = " SELECT f.ad_form_id,  f.name, f.description, e.name, mtrl.name, " +
						" case fa.isactive when 'Y' then '1' else '0' end active," +
						" case fa.isreadwrite when 'Y' then '1' else '0' end readwrite" +
						" FROM ad_form f join ad_entitytype e using (entitytype)" +
						" left join ad_form_access fa on (f.ad_form_id = fa.ad_form_id and ad_role_id = " + rolSel.getKey() + ")" +
						" left join ad_menu m on (f.ad_form_id = m.ad_form_id and m.isactive = 'Y')" +
						" left join ad_menu_trl mtrl on (mtrl.ad_menu_id = m.ad_menu_id )" +
						" where f.isactive = 'Y' and trim(f.name) is not null ";
				
			}
			
			try {
				//Agrego las columnas 
				columnasAgregadas = new Vector<ColumnInfo>();
//					columnasAgregadas.add(new ColumnInfo("", "w.ad_window_id", IDColumn.class, false, false, ""));
				columnasAgregadas.add(name);
				columnasAgregadas.add(description);
				columnasAgregadas.add(entity);
				columnasAgregadas.add(menu);
				columnasAgregadas.add(active);
				columnasAgregadas.add(readwrite);
				llenarTabla(sql);	
//				statusBar.setStatusDB(miniTable.getRowCount());
			} catch (NullPointerException n) {
				n.printStackTrace();
			}	
		} 
		
		// Opción seleccionada PVF
		else {
			if (itemSel == null  | itemSel.getKey() == 0) {				
				ADialog.error(m_WindowNo, m_frame, "Seleccione Ítem");
				return;
			} 
			KeyNamePair itemValue;
			String sql = "", tabla = "", elemento = "";
			if (itemSel.getKey() == 1) { // Ventana
				itemValue = winSel;	
				tabla = "ad_window"; elemento = "Ventana";
			} else if (itemSel.getKey() == 2) { // Proceso
				itemValue = procSel;	
				tabla = "ad_process"; elemento = "Proceso";
			} else if (itemSel.getKey() == 3) { // Forma
				itemValue = formSel;	
				tabla = "ad_form"; elemento = "Forma";
			} else {
				return;
			}
			
			// Validar opción
			if (itemValue == null | itemValue.getKey() == 0) {				
				ADialog.error(m_WindowNo, m_frame, "Seleccione "+ elemento);
				return;
			} 
			searchLabel.setText(itemSel.getName() + ": " + itemValue.getName());
			xBorder.setTitle("Rol");
			dataPane.setBorder(xBorder);
			
			
			sql = "SELECT r.ad_role_id,  r.name, r.description," +
					" case ia.isactive when 'Y' then '1' else '0' end active, " +
					" case ia.isreadwrite when 'Y' then '1' else '0' end readwrite" +
					" FROM ad_role r " +
					" left join "+tabla+"_access ia on (r.ad_role_id = ia.ad_role_id " +
								" and "+tabla+"_id = " + itemValue.getKey() + ") " +
					" where r.isactive = 'Y' and trim(r.name) is not null";
			
			try {
				//Agrego las columnas 
				columnasAgregadas = new Vector<ColumnInfo>();
//				columnasAgregadas.add(new ColumnInfo("", "w.ad_window_id", IDColumn.class, false, false, ""));
				columnasAgregadas.add(name);
				columnasAgregadas.add(description);
				columnasAgregadas.add(active);
				columnasAgregadas.add(readwrite);
			
				llenarTabla(sql);	
//				statusBar.setStatusDB(miniTable.getRowCount());
			} catch (NullPointerException n) {
				n.printStackTrace();
			}	
		}
		
		mainPanel.validate();
	}   //  cmd_Search
	  
	
	
	/**
	 * 	Save Button Pressed
	 */
	private void cmd_Save() {
		List<Object[]> bulkParamsUpdate = new ArrayList<Object[]>();
		List<Object[]> bulkParamsInsert = new ArrayList<Object[]>();
		ArrayList<Object> params = null;
		
		// Setear opciones
		int itemId = 0, roleId = 0;
		String tabla = "";
		if ((Boolean)checkRol.getValue()) {
			roleId = rolSel.getKey();			
			if (itemSel.getKey() == 1) { // Ventana
				tabla = "ad_window"; 
			} else if (itemSel.getKey() == 2) { // Proceso
				tabla = "ad_process";
			} else if (itemSel.getKey() == 3) { // Ventana
				tabla = "ad_form";
			} 
		} else {
			if (itemSel.getKey() == 1) { // Ventana
				tabla = "ad_window"; 
				itemId = winSel.getKey();	
			} else if (itemSel.getKey() == 2) { // Proceso
				tabla = "ad_process";
				itemId = procSel.getKey();
			} else if (itemSel.getKey() == 3) { // Ventana
				tabla = "ad_form";
				itemId = formSel.getKey();	
			} 
		}
		
		// Solo tomar en cuenta las opciones clickeadas
		int cantidad = (allChanged ? miniTable.getRowCount() : updateList.size());
		for (int i=0; i< cantidad ; i++) {
			int row = allChanged ? i : updateList.get(i); // Solo los seleccionados
			KeyNamePair kpn = (KeyNamePair) miniTable.getModel().getValueAt(row, 0);
			String active =
				(Boolean)miniTable.getModel().getValueAt(row, columnasAgregadas.size()-2) ? "Y" : "N";
			String readwrite = 
				(Boolean)miniTable.getModel().getValueAt(row, columnasAgregadas.size()-1) ? "Y" : "N";
			
			// Campos faltantes
			if ((Boolean)checkRol.getValue()) {
				itemId = kpn.getKey();
			} else {
				roleId = kpn.getKey();
			}
//				System.out.println(itemId);
//				System.out.println(roleId);
				
			params = new ArrayList<Object>();	
			params.add(active);
			params.add(readwrite);
			params.add(itemId);
			params.add(roleId);
			
			// Revisar si ya se encuentra en la BD (solución temporal, mejorar para casos masivos)
			if (itemInBD(tabla, itemId, roleId)) { // Actualizar
				bulkParamsUpdate.add(params.toArray());
			} else { // Insertar
				bulkParamsInsert.add(params.toArray());
			}
				
		}
		
		//Actualizo los detalles con su respectiva condición
		if (!bulkParamsInsert.isEmpty()) {
			String insert = "INSERT INTO "+tabla+ "_ACCESS "
				+ "(AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY," 
				+ " ISACTIVE, ISREADWRITE, " + tabla+"_ID,  AD_ROLE_ID ) " 
				+ " VALUES ("+Env.getCtx().getAD_Client_ID() + ", " + Env.getCtx().getAD_Org_ID() 
						+ ", SYSDATE, " + Env.getCtx().getAD_User_ID() + ", SYSDATE, "
						+ Env.getCtx().getAD_User_ID()+", ?,?,?,?) ";
	
			DB.executeBulkUpdate((Trx) null, insert, bulkParamsInsert, false, true);
		}
		if (!bulkParamsUpdate.isEmpty()) {
			String update = "UPDATE "+tabla+"_access set ISACTIVE=?, ISREADWRITE=? WHERE " +
				tabla + "_ID = ? AND ad_role_id = ?";
			
			DB.executeBulkUpdate((Trx) null, update, bulkParamsUpdate, false, true);
		}
		
		// Reseteamos
		updateList.clear();
		allChanged = false;
		
	}

	
	
	/** 
	 * 	Función que llena la tabla a mostrar con los datos obtenidos del query
	 * @param sql
	 */
	protected void llenarTabla(String sql) {
		
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
		//Si no se ha cargado el header previamente
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);
		
		miniTable.getModel().removeTableModelListener(this);
		miniTable.getModel().addTableModelListener(this);
		miniTable.getSelectionModel().removeListSelectionListener(this);
		miniTable.getSelectionModel().addListSelectionListener(this);
		
		//Calcular el query
		try {
			calcularQuery(sql);

			miniTable.setRowSelectionAllowed(true);
			miniTable.setSelectionBackground(Color.white);
			miniTable.autoSize();
			miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
			miniTable.getTableHeader().setReorderingAllowed(false);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		m_frame.setCursor(Cursor.getDefaultCursor());		
	}


	/** 
	 * 	Función que ejecuta el query indicado.
	 * @param sql
	 */
	public void calcularQuery (String sql) {

		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		//miniTable.prepareTable(layout, null, null, false, null);
		miniTable.prepareTable(layout, "", "", true, "");

//		System.out.println("QUERY "+ sql);
	
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
	
			int rows = 0;
			while (rs.next()) {
				int total = columnasAgregadas.size()-2;
				miniTable.setRowCount(rows + 1);
				// Columna ID
				miniTable.setValueAt(new KeyNamePair(rs.getInt(1), rs.getString(2)), rows, 0);
//				miniTable.setValueAt(new KeyNamePair(rs.getInt(0),rs.getString(1)), rows, 1);
				for (int i=1; i<columnasAgregadas.size()-2; i++) { 
					miniTable.setValueAt(rs.getString(i+2), rows, i);
				}	
				miniTable.setValueAt(rs.getBoolean("active"), rows, total++); //IsActive
				miniTable.setValueAt(rs.getBoolean("readwrite"), rows, total); //IsReadWrite
				miniTable.repaint();
				rows++;
//				miniTable.getValueAt(rows-1, 4).getClass();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (ps != null)
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
		}					
		
	
	}
	
	
	/**
	 * 	Función auxiliar que permite colocar activos o inactivos todos los campos de 
	 * una columna determinada
	 * @param active
	 * @param column
	 */
	private void toggleActive(boolean active, int column) {
		for (int row=0; row<miniTable.getRowCount(); row++) {
			miniTable.getModel().setValueAt(active, row, column);
		} 
		// Marcamos todos los campos como modificados
		allChanged = true;
	}
	
	
	/**
	 * 	Función auxiliar que determina si existe una entrada en la tabla <<item>>_access.
	 * Solución temporal. Debe cambiarse por un método más eficiente para bulk updates
	 * @param tabla
	 * @param id
	 * @return
	 */
	private boolean itemInBD(String tabla, int id, int rol_id) {
		String query = "SELECT "+tabla+"_id , ad_role_id  FROM "+tabla+"_access " +
				" WHERE "+tabla+"_id = "+id+" and ad_role_id = " + rol_id;
		
		PreparedStatement ps = DB.prepareStatement(query, null);
		ResultSet rs = null;
		
		try {
			rs = ps.executeQuery();
			if (rs.next()){
				return true;
			} else {
				return false;
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
	}
		
	
	/* MANEJO DE LISTENERS */
	
	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		checkRol.addActionListener(this);
		checkVPF.addActionListener(this);
		rolSearch.addActionListener(this);
		comboItemRol.addActionListener(this);
		windowSearch.addActionListener(this);
		processSearch.addActionListener(this);
		formSearch.addActionListener(this);
		allActive.addActionListener(this);
		allRW.addActionListener(this);

		bCancel.addActionListener(this);
		bSave.addActionListener(this);
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
	} //addActionListeners
	
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		checkRol.removeActionListener(this);
		checkVPF.removeActionListener(this);
		rolSearch.removeActionListener(this);
		comboItemRol.removeActionListener(this);
		windowSearch.removeActionListener(this);
		processSearch.removeActionListener(this);
		formSearch.removeActionListener(this);
		allActive.removeActionListener(this);
		allRW.removeActionListener(this);

		bCancel.removeActionListener(this);
		bSave.removeActionListener(this);
		bReset.removeActionListener(this);
		bSearch.removeActionListener(this);
	} // removeActionListeners

	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		int column = miniTable.getSelectedColumn();
		int row = miniTable.getSelectedRow();
		if (row ==-1 || column == -1) {
			return;
		}
		// Agregar la fila a la lista de modificadas
		if (!allChanged && column >= columnasAgregadas.size()-2 && !updateList.contains(row) ) {
			updateList.add(row); 
		}
						
		if (column == columnasAgregadas.size()-1) {
			// Readwrite
			// Si readwrite = true y active = false, colocar active = true
			if ((Boolean)miniTable.getModel().getValueAt(row, column) &
					!(Boolean)miniTable.getModel().getValueAt(row, column-1)) {
				miniTable.getModel().setValueAt(true, row, column-1);
			}
		}				
	}


	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
	}

	
	//Cabeceras de las columnas de la tabla
	Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
	private ColumnInfo name = new ColumnInfo("Nombre",".", KeyNamePair.class);
	private ColumnInfo description = new ColumnInfo("Descripción",".",String.class);
	private ColumnInfo entity = new ColumnInfo("Entidad",".",String.class);
	private ColumnInfo active = new ColumnInfo("Activo",".",Boolean.class, false, true, "");
	private ColumnInfo readwrite = new ColumnInfo("Lectura/Escritura",".",Boolean.class, false, true, "");
	private ColumnInfo menu = new ColumnInfo("Menú",".",String.class);
	
}
