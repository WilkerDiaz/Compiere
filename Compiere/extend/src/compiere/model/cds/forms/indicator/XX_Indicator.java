package compiere.model.cds.forms.indicator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.excel.Excel;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VFile;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.cds.MiniTablePreparator;

public abstract class XX_Indicator extends CPanel implements FormPanel, ActionListener {

	private static final long serialVersionUID = -8930684491479890705L;
	
		/** El frame de la ventana*/
	protected FormFrame m_frame; 	
		/** Contexto general*/ 
	protected Ctx ctx = Env.getCtx();
		/** Formato de Impresión para decimales */
	protected DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayTypeConstants.Amount);
		/** SQL para el query           */	
	protected StringBuilder          m_sql;
		
	public final int CATEGORIA = 0, DEPARTAMENTO = 1, LINEA = 2 , SECCION = 3, 
		PRODUCTO = 4, SOCIODENEGOCIO = 5, PERIODO = 6, 		
		ELEMENTOS = 7, CAMPOSPORGRUPO = 3, GRUPOSPORFILA = 3; 
	
	protected int parametrosMostrados = 0, fila = 0, columna = 0;
	protected Vector<Boolean> mostrarParametros = new Vector<Boolean>(ELEMENTOS); 
	
		/* Panel y campos de la ventana */
	protected CPanel mainPanel = new CPanel();		
		protected CPanel parameterPanel = new CPanel();
		protected CPanel resultPanel = new CPanel();
		protected CPanel commandPanel = new CPanel();
		protected JScrollPane dataPane = new JScrollPane();
		protected CPanel southPanel = new CPanel();
	
		/*status bar*/
		protected StatusBar statusBar = new StatusBar();
		
		/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private GridBagLayout resultLayout = new GridBagLayout();
	private FlowLayout commandLayout = new FlowLayout();
	private BorderLayout southLayout = new BorderLayout();
	
	//******* Campos del filtro disponibles
	
		//Categoría
	protected VCheckBox checkCategory = new VCheckBox();
	protected CLabel labelCategory = new CLabel();
	protected VComboBox comboCategory = new VComboBox();
		//Departamento
	protected VCheckBox checkDepartment = new VCheckBox();
	protected CLabel labelDepartment = new CLabel();
	protected VComboBox comboDepartment = new VComboBox();
		//Línea
	protected VCheckBox checkLine = new VCheckBox();
	protected CLabel labelLine = new CLabel();
	protected VComboBox comboLine = new VComboBox();
		//Sección	
	protected VCheckBox checkSection = new VCheckBox();
	protected CLabel labelSection = new CLabel();
	protected VComboBox comboSection = new VComboBox();
		//Socio de Negocio
	protected VCheckBox checkBPartner = new VCheckBox();
	protected CLabel labelBPartner = new CLabel();
	protected VComboBox comboBPartner = new VComboBox();
		//Producto
	protected VCheckBox checkProduct = new VCheckBox();
	protected CLabel labelProduct = new CLabel();
	protected VLookup lookupProduct = null;
		//Período
	protected VNumber monthPeriod = new VNumber();
	protected CLabel slashPeriod = new CLabel(" / ");
	protected VNumber yearPeriod = new VNumber();
	Box boxPeriod = Box.createHorizontalBox();	
	protected CLabel labelPeriod = new CLabel();
	
	protected CLabel labelFile = new CLabel();

		/* Botones */
	protected CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	protected CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	protected CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "ExportExcel"));
	protected VFile bFile = new VFile("File", Msg.getMsg(ctx, "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	
		/** La tabla donde se guardarán los datos */
	protected MiniTablePreparator miniTable = new MiniTablePreparator();


	protected int indx = 0;
	protected String nombreArchivo;

	/** Indica al que campos ocultar de los creados por defecto 
	 * @param campo El campo a ocultar especificado en las constantes de la clase
	 */
	protected void ocultarParametro (int campo ) {
		mostrarParametros.setElementAt(false, campo);
	}   
	
	/** Método que calcula la posición de un campo */
	private void siguientePosicion () {
		fila = parametrosMostrados / (CAMPOSPORGRUPO*GRUPOSPORFILA);
		columna = parametrosMostrados % (CAMPOSPORGRUPO*GRUPOSPORFILA);		
	}
	
	/** Permite agregar un campo a la panel de parametros y asignarlo */
	protected void agregarParametro (Component componente, int espacioHor) {
		
		siguientePosicion();
		/* Asignar la orientación dependiendo de la posicion*/
		int orientacion = 0, orientacion2 = 0 ;
		
		if (columna % CAMPOSPORGRUPO == 1) {
			orientacion = GridBagConstraints.WEST;
			orientacion2 = GridBagConstraints.HORIZONTAL;
		} else {
			orientacion = GridBagConstraints.EAST;
			orientacion2 = GridBagConstraints.NONE;
		}
	
		//Agregar al panel de parametros
		parameterPanel.add(componente,  
				new GridBagConstraints(
						columna, fila, espacioHor, 1, 0.0, 0.0,
						orientacion, orientacion2, new Insets(5, 5, 5, 5), 0, 0));
		parametrosMostrados += espacioHor;
	}
	
	/** Método de conveniencia para no repetir el parametro de espacio horizontal cuando este es = a 1 */
	protected void agregarParametro (Component componente) {
		siguientePosicion();
		agregarParametro(componente, 1);
	}
	
	/** Permite agregar un campo a la panel de parametros en la fila disponible
	 * especificando cuantas columnas ocupará 
	 * */
	protected void agregarParametro (Component componente, int fila, int columna, int espacioHor) {
		
		/* Asignar la orientación dependiendo de la posicion*/
		int orientacion = 0, orientacion2 = 0 ;
		
		if (columna % CAMPOSPORGRUPO == 1) {
			orientacion = GridBagConstraints.WEST;
			orientacion2 = GridBagConstraints.HORIZONTAL;
		} else {
			orientacion = GridBagConstraints.EAST;
			orientacion2 = GridBagConstraints.NONE;
		}
	
		//Agregar al panel de parametros
		parameterPanel.add(componente,  
				new GridBagConstraints(
						columna, fila, espacioHor, 1, 0.0, 0.0,
						orientacion, orientacion2, new Insets(5, 5, 5, 5), 0, 0));
		parametrosMostrados += espacioHor;
	}
	
	/** Inicializador de los campos por defecto, se debe llamar después de ocultar campos */
	private void inicializarParametros () {

			//Agregar nombres a las etiquetas
		labelCategory.setText(Msg.translate(ctx, "XX_VMR_Category_ID"));
		labelDepartment.setText(Msg.translate(ctx, "XX_VMR_Department_ID"));
		labelLine.setText(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
		labelSection.setText(Msg.translate(ctx, "XX_VMR_Section_ID"));
		labelProduct.setText(Msg.translate(ctx, "M_PRODUCT_ID"));
		labelBPartner.setText(Msg.translate(ctx, "C_BPartner_ID"));
		labelPeriod.setText(Msg.getMsg(ctx, "XX_MonthYear"));
		labelFile.setText(Msg.getMsg(ctx, "File"));
	    
		parametrosMostrados = 0;

		 //Muestra solo los que no se ocultaron		
		if (mostrarParametros.get(CATEGORIA)) {			
			agregarParametro(labelCategory);
			agregarParametro(comboCategory);
			agregarParametro(checkCategory);
		}
		if (mostrarParametros.get(DEPARTAMENTO)) {
			agregarParametro(labelDepartment);
			agregarParametro(comboDepartment);
			agregarParametro(checkDepartment);
		}
		if (mostrarParametros.get(LINEA)) {
			agregarParametro(labelLine);
			agregarParametro(comboLine);
			agregarParametro(checkLine);
		}
		if (mostrarParametros.get(SECCION)) {
			agregarParametro(labelSection);
			agregarParametro(comboSection);
			agregarParametro(checkSection);
		}
		if (mostrarParametros.get(PRODUCTO)) {
			agregarParametro(labelProduct);
			agregarParametro(lookupProduct);
			agregarParametro(checkProduct);
		}
		if (mostrarParametros.get(SOCIODENEGOCIO)) {
			agregarParametro(labelBPartner);
			agregarParametro(comboBPartner);
			agregarParametro(checkBPartner);
		
		}
		if (mostrarParametros.get(PERIODO)) {
			agregarParametro(labelPeriod);			
			agregarParametro(boxPeriod, 2);		
		}	
	}
	
	/** Inicializador de los datos por defecto */
	private void dynInit () {

		desactivarActionListeners();		
		cargarInfoBásica();		
		activarActionListeners();
	}
	
	@Override
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}

	/** Inicialización de Campos */
	private final void jbInit() throws Exception {
		
		CompiereColor.setBackground(this);		
		m_frame.getRootPane().setDefaultButton(bSearch);
		mainPanel.setLayout(mainLayout);

		//Creación del panel principal
		parameterPanel.setLayout(parameterLayout);
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);		
		dataPane.setPreferredSize(new Dimension(1280, 600));

		
		//Panel inferior
		southPanel.setLayout(southLayout);
		
		//Creación del panel de resultados 
		resultPanel.setLayout(resultLayout);
		southPanel.add(resultPanel, BorderLayout.CENTER);

		//Panel Inferior
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		southPanel.add(commandPanel, BorderLayout.SOUTH);
		
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		commandPanel.add(bSearch, null);
		commandPanel.add(bReset, null);				
		commandPanel.add(labelFile);
		commandPanel.add(bFile, null);
		commandPanel.add(bPrint, null);
	
		//Agregar los action listeners
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
		bPrint.addActionListener(this);
		
		//Periodo es un campo conjunto
		boxPeriod.add(labelPeriod);
		boxPeriod.add(monthPeriod);
		boxPeriod.add(slashPeriod);
		boxPeriod.add(yearPeriod);
		monthPeriod.setPreferredSize(new Dimension(monthPeriod.getPreferredSize().width/2, monthPeriod.getPreferredSize().height));
		yearPeriod.setPreferredSize(new Dimension(yearPeriod.getPreferredSize().width/2, monthPeriod.getPreferredSize().height));
		monthPeriod.setRange(1.0, 12.0);
		monthPeriod.setDisplayType(DisplayTypeConstants.Integer);
		yearPeriod.setRange(1.0, Double.MAX_VALUE);
		yearPeriod.setDisplayType(DisplayTypeConstants.Integer);
		
		//Cargo los campos por defecto en el vector
		for (int i = 0; i < ELEMENTOS ; i++) {
			mostrarParametros.add(true);
		}

		//Se ocultan los campos deseados
		ocultarParametrosDefecto();		
	
		//Se muestran los parametros 
		inicializarParametros();
		
		//Se muestran los parametros agregados
		agregarParametros();
		
	}
	
	public final void cargarInfoBásica () {
					
		desactivarActionListeners();	
		cargarCategorías();
		cargarDepartamentos();
		cargarSocios();
		
		//Restore ComboBoxes and CheckBoxes				
		configurar(comboCategory, true);
		configurar(comboDepartment, true);
		configurar(comboLine, false);
		configurar(comboSection, false);
		configurar(comboBPartner, true);
		
		configurar(checkCategory, true, false);
		configurar(checkDepartment, true, false);
		configurar(checkBPartner, true, false);
		configurar(checkProduct, true, false);		
		configurar(checkLine, false);
		configurar(checkSection, false);
	
		monthPeriod.setValue(null);
		yearPeriod.setValue(null);
		
		lookupProduct.setValue(null);
		lookupProduct.setEnabled(true);
		
		//personalizacion del hijo
		personalizar();
				
		//Actualizar los cambios
		repaint();
	
		activarActionListeners();
	}
	
	
	@Override
	/** Inicializar la forma */
 	public final void init(int WindowNo, FormFrame frame) {
		
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try {
			lookupProduct = VLookup.createProduct(m_WindowNo);			
			jbInit();
			dynInit();
			
			//frame.getContentPane().add(commandPanel, BorderLayout.CENTER);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);	
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);	
		} catch(Exception e) {
			log.log(Level.SEVERE, "", e);
		}	
	}

	/** Cargar las categorías  */
	protected final void cargarCategorías() {
		
		KeyNamePair loadKNP;
		String sql = "";						
		comboCategory.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboCategory.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCategories"));
		comboCategory.addItem(loadKNP);	
		sql = " SELECT ct.XX_VMR_CATEGORY_ID, ct.VALUE||'-'||ct.NAME " +
				" FROM XX_VMR_CATEGORY ct WHERE ct.ISACTIVE = 'Y' "; 		
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCategory.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}

	/** Inicializar los departamentos */
	protected final void cargarDepartamentos() {
		
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		
		comboDepartment.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboDepartment.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
		comboDepartment.addItem(loadKNP);	
		
		if (catg != null && catg.getKey() != 0 && catg.getKey() != 99999999){			
			sql = "SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||'-'||dp.NAME FROM XX_VMR_DEPARTMENT dp " 
				+ "WHERE dp.XX_VMR_CATEGORY_ID = " + catg.getKey() + " ORDER BY dp.VALUE ";
		}  else {					
			sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT ";
			sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
				+ " ORDER BY VALUE||'-'||NAME";									
		} 
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}		
		
	}
	
	/** Cargar las lineas */
	private void cargarLineas(){
		KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();				
		KeyNamePair loadKNP, line;		
		comboLine.removeAllItems();
		
		loadKNP = new KeyNamePair(0,"");
		comboLine.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllLines"));
		comboLine.addItem(loadKNP);
		
		String sql = "SELECT li.XX_VMR_LINE_ID, li.VALUE||'-'||li.NAME FROM XX_VMR_LINE li " 
				+ "WHERE li.XX_VMR_DEPARTMENT_ID = " + dpto.getKey() + " ORDER BY li.VALUE ";
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				line = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLine.addItem(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/** Cargar las secciones */
	private void cargarSecciones() {
			
			KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
			KeyNamePair loadKNP;
			KeyNamePair sect;
			String sql = "";
			
			comboSection.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboSection.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllSections"));
			comboSection.addItem(loadKNP);	
			sql = "SELECT se.XX_VMR_SECTION_ID, se.VALUE||'-'||se.NAME" 
				+ " FROM XX_VMR_SECTION se" 				
				+ " WHERE se.XX_VMR_LINE_ID = " + line.getKey();					
			try{
				
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					sect = new KeyNamePair(rs.getInt(1), rs.getString(2));
					comboSection.addItem(sect);
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}	
		}
	
	private void cargarSocios() {


		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair loadKNP;
		String sql = "";

		//Department selected
		if(dept == null || dept.getKey() == 0 || dept.getKey() == 99999999) {			
			comboBPartner.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboBPartner.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBusinessPartner"));
			comboBPartner.addItem(loadKNP);

			sql = "SELECT C_BPARTNER_ID, NAME FROM C_BPARTNER WHERE " +
			" ISVENDOR = 'Y' AND XX_ISVALID = 'Y' AND ISACTIVE = 'Y'";
			sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + 
			" ORDER BY NAME ";							//
		} else { 
			comboBPartner.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboBPartner.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBusinessPartner"));
			comboBPartner.addItem(loadKNP);	
			sql = " SELECT bp.C_BPARTNER_ID, bp.NAME" 
				+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, C_BPARTNER bp " 
				+ " WHERE ve.C_BPARTNER_ID = bp.C_BPARTNER_ID AND ve.XX_VMR_DEPARTMENT_ID = " 
				+ " (SELECT dp.XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT dp " +
				" WHERE dp.XX_VMR_DEPARTMENT_ID = " + dept.getKey() + ")";						
		}		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}

	/** Método invocado al presionar el botón de limpiar filtro */
	protected void limpiarFiltro() { 
		desactivarActionListeners();		
		cargarInfoBásica();
		activarActionListeners();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Desactivar los efectos de las acciones 
		desactivarActionListeners();
		
		//Se actualizan los departamentos una vez se ha modificada la categoría 
		if (e.getSource() == comboCategory){						
			configurar(comboLine, false);
			configurar(checkLine, false);
			configurar(comboSection, false);
			configurar(checkSection, false, false);
			cargarDepartamentos();			
		} 		
		//Se verifica si se ha seleccionado todas las categorias 
		else if (e.getSource() == checkCategory) {			
			configurar(comboLine, false);				
			configurar(comboSection, false);
			configurar(checkLine, false);
			configurar(checkSection, false);
			if ((Boolean)checkCategory.getValue()) {				
				configurar(comboCategory, false, 99999999 );				
				configurar(comboDepartment, false);				
				configurar(checkDepartment, false);
			} else {
				configurar(comboCategory, true);				
				configurar(comboDepartment, true);								
				configurar(checkDepartment, true, false);
				cargarDepartamentos();
			}	
		}
		//Se actualizan las lineas una vez se ha modificado el departamento 
		else if (e.getSource() == comboDepartment){		
			KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();
			if(dpto == null || dpto.getKey() == 0 || dpto.getKey() == 99999999 ) {
				configurar(comboLine, false);
				configurar(checkLine, false);
				configurar(checkSection, false);
				configurar(comboSection, false);
				return;
			}				
			cargarLineas();	
			cargarSocios();		
			configurar(comboBPartner, true);
			configurar(checkBPartner,true, false);
			configurar(comboLine, true);
			configurar(checkLine, true, false);			
			configurar(checkSection, false);
			configurar(comboSection, false);
		}
		//Se modificó el departamento
		else if(e.getSource() == checkDepartment){			
			if((Boolean)checkDepartment.getValue()){
				configurar(comboDepartment, false, 99999999);							
			}else{									
				configurar(comboDepartment, true);								
			}
			configurar(checkLine, false);
			configurar(comboLine, false);				
			configurar(comboSection, false);
			configurar(checkSection, false);
			cargarLineas();
			cargarSocios();
			configurar(checkBPartner, true, false);
			configurar(comboBPartner, true);
		}			
		//Se cargan las secciones una vez cambia la linea
		else if (e.getSource() == comboLine ){
			
			KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
			if(line == null || line.getKey() == 0 || line.getKey() == 99999999 ) {				
				configurar(checkSection, false);
				configurar(comboSection, false);
				return;
			}				
			cargarSecciones();				
			configurar(checkSection, true, false);
			configurar(comboSection, true);
		}		
		
		//Se marcó el check de todas las lineas
		else if(e.getSource() == checkLine){
			
			if((Boolean)checkLine.getValue()){
				configurar(comboLine, false, 99999999);				
				configurar(comboSection, false);
				configurar(checkSection, false);
			}else{					
				configurar(checkLine, true, false);
				configurar(comboLine, true);				
				configurar(comboSection, false);
				configurar(checkSection, false);				
				cargarSecciones();
			}
		}
		
		//Se marco el check de todas las secciones
		else if(e.getSource() == checkSection){
			if((Boolean)checkSection.getValue() == true){
				if((Boolean)checkLine.getValue() == true || ((KeyNamePair)comboLine.getSelectedItem()).getKey() != 0){
					comboSection.setValue(99999999);
					comboSection.setEnabled(false);
				}				
			}else{	
				comboSection.setEnabled(true);				
				cargarSecciones();
				comboSection.setValue(0);
			}
		}
		//Si fue el check de socio de negocio
		else if(e.getSource() == checkBPartner){
			if((Boolean)checkBPartner.getValue() == true){
				comboBPartner.setValue(99999999);
				comboBPartner.setEnabled(false);
				//cargarDepartamentos();
			}else{
				comboBPartner.setEnabled(true);
				comboBPartner.setValue(0);			
				//cargarDepartamentos();
			}
		}		
		//Si fue el check de producto
		else if(e.getSource() == checkProduct){
			if((Boolean) checkProduct.getValue() == true){
				
				try{
					lookupProduct.setValue(new KeyNamePair(0, Msg.translate(Env.getCtx(), "XX_AllProducts")));
				}catch(Exception ex){
					
				}				
				lookupProduct.setEnabled(false);
			}else{
				lookupProduct.setValue(null);
				lookupProduct.setEnabled(true);
			}
		}
		//Limpiar el filtro
		else if(e.getSource() == bReset)
			limpiarFiltro();
		
		//Imprimir la seleccion
		else if (e.getSource() == bPrint)
		{
			Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			Cursor defaultCursor = Cursor.getDefaultCursor();
			m_frame.setCursor(waitCursor);
			try {
				imprimirArchivo(miniTable, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
			m_frame.setCursor(defaultCursor);
		}
		//Execute Search
		else if(e.getSource() == bSearch) {
			try {
				llenarTabla();	
				statusBar.setStatusDB(miniTable.getRowCount());
			} catch (NullPointerException n) {
			}	
		} 
		activarActionListeners();
	}
	
	public static void imprimirArchivo (MiniTablePreparator tabla, VFile archivo, int ventana, Container contenedor) {
		
		if (archivo.getValue() == null || archivo.getValue().equals("")) {
			ADialog.error(ventana, contenedor, "XX_FileForExport");
			return;
		}
		File archivoFisico = new File((String)archivo.getValue());
		String nombre = (String)archivo.getValue();
		
		
		//Verificar la extension
		String extension = "";
		if(nombre.length()>4){
			extension = nombre.substring(nombre.length()-4, nombre.length());
		} /*else {
			ADialog.error(ventana, contenedor, "Not Excel" );
			return;
		}*/
		
		if (nombre.length()<=4 || !extension.equals(".xls")) { 
			//ADialog.error(ventana, contenedor, "Not Excel" );
			//return;		
			//Colocar el .xls
			archivo.setValue(nombre.concat(".xls"));
			archivoFisico = new File((String)archivo.getValue());
		}
			
		if (archivoFisico.exists()) {
			ADialog.error(ventana, contenedor, "XX_FileExist" );
			return;			
		} 
		Excel archivoGenerado = new Excel(archivoFisico);
		createEXCEL(archivoGenerado, tabla);
		
		//El archivo fue creado
		String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
			(String)archivo.getValue()
		});
		ADialog.info(ventana, contenedor, msg);
		archivo.setValue(null);
		return;
		
				
	}

	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(XX_Indicator.class);
	/**	Window No			*/
	protected int m_WindowNo = 0;
	
	//**************** Métodos de utilidad para esta ventana *********************************
	
	/** Habilita y deshabilita componentes apropiadamente y coloca valores por defecto si aplican */
	protected static final void configurar (Component componente, boolean habilitar) {
		if (componente instanceof VComboBox)			
			configurar((VComboBox)componente, habilitar, 0);
		else if (componente instanceof CCheckBox)
			configurar((CCheckBox)componente, habilitar, habilitar);
		else if (componente instanceof VFile) {			
			((VFile) componente).setReadWrite(habilitar);			
			if (!habilitar) {
				((VFile) componente).setValue(null);
			}			
		} else if (componente instanceof CLabel)
			componente.setVisible(habilitar);
		else componente.setEnabled(habilitar);			
	}
	
	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void configurar (VComboBox componente, boolean habilitar, int valor) {		
		componente.setEnabled(habilitar);		
		componente.setEditable(habilitar);
		componente.setValue(valor);					
	}
	
	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void configurar (CCheckBox componente, boolean habilitar, boolean valor) {		
		componente.setEnabled(habilitar);
		componente.setValue(valor);
	}
	
	/** Activa todos los listeners creados */
	protected final void activarActionListeners () {
		
		comboCategory.addActionListener(this);		
		comboDepartment.addActionListener(this);		
		comboLine.addActionListener(this);		
		comboSection.addActionListener(this);		
		checkCategory.addActionListener(this);
		checkDepartment.addActionListener(this);
		checkLine.addActionListener(this);
		checkSection.addActionListener(this);		
		checkProduct.addActionListener(this);		
		checkBPartner.addActionListener(this);
		monthPeriod.addActionListener(this);
		yearPeriod.addActionListener(this);		
	} 
	
	/** Deshabilitar Action Listeners */
	protected final void desactivarActionListeners () {
		
		comboCategory.removeActionListener(this);		
		comboDepartment.removeActionListener(this);		
		comboLine.removeActionListener(this);		
		comboSection.removeActionListener(this);
		checkCategory.removeActionListener(this);
		checkDepartment.removeActionListener(this);
		checkLine.removeActionListener(this);
		checkSection.removeActionListener(this);		
		checkProduct.removeActionListener(this);		
		checkBPartner.removeActionListener(this);
		monthPeriod.removeActionListener(this);
		yearPeriod.removeActionListener(this);
	}	
	
	// **************** Métodos que deben ser implementados por los hijos
	
	/** Debe sobreescribirse en cualquier clase que instancie a esta 
	 * En este método se indicará cuales campos por defecto no se desean mostrar
	 * 
	 * 			ocultarParametro(SOCIODENEGOCIO); 
	 * */
	protected abstract void ocultarParametrosDefecto () ;
	
	 /** Para agregar parámetros utilizará los métodos agregarParametros de la clase padre.
	 *  
	 * 			agregarParametro(Componenete)
	 *  
	 *  Debe considerarse que por cuestiones de estilo los grupos de parametros deben agregarse en grupos de tres.
	 *  En caso de ocupar más o menos espacio solicitar más espacio horizontal con el metodo
	 *  
	 *  		agregarParametro(Componente, 2)
	 *  
	 *  También es posible especificar la dirección especifica de cada campo en el grid, sin embargo esto desactivará la opción
	 *  por defecto que es el ajuste unop después del otro. */
	protected abstract void agregarParametros () ;
	
	/** Debe sobreescribirse en cualquier clase que instancie a esta 
	 * En este método se llenará la tabla al presionar buscar
	 * */
	protected abstract void llenarTabla ();
	
	/** Debe sobreescribirse en cualquier clase que instancie a esta 
	 *  En este método el hijo realizará cualquier personalización como action listeners, pre procesos, etc 
	 */
	protected abstract void personalizar () ;
	
	/** Usando el método de Crear Excel en ReporEngine con modificaciones*/ 
	public static Excel createEXCEL (Excel excel, MiniTablePreparator miniTable) {
		final int COTA_EXCEL = Short.MAX_VALUE; 
		try {
			//	for all rows (-1 = header row)			
			for (int row = -1; row < miniTable.getRowCount(); row++) {
				if (row % COTA_EXCEL == COTA_EXCEL - 1) {
					excel.createAndSetSheet("Export Compiere " + ( 1 + row / COTA_EXCEL));
				}				
				//Para todas las columnas
				int colPos = 0;				
				for (int col = 0; col < miniTable.getColumnCount(); col++){
					if (row == -1 || row % COTA_EXCEL == COTA_EXCEL - 1) {														
						excel.createRow(
								(short)0, 
								(short)colPos, 
								miniTable.getColumnName(col), 
								null,
								Excel.CELLSTYLE_HEADER,
								Excel.DISPLAY_TYPE_STRING);							
					} else 	{							
						int displayType = Excel.DISPLAY_TYPE_STRING;
						Object obj = miniTable.getValueAt(row, col);
						Object valor = "";
						String valorStr = "";
						if (obj == null)
							;
						else  {
							if (obj instanceof BigDecimal) {							
								displayType = Excel.DISPLAY_TYPE_NUMBER;
								valor = obj;
							} else if (obj instanceof Number) {							
								displayType = Excel.DISPLAY_TYPE_NUMBER;
								valor = DisplayType.getNumberFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
								valorStr = obj.toString();
							} else if (obj instanceof Integer) {
								displayType = Excel.DISPLAY_TYPE_INTEGER;
								valor = DisplayType.getNumberFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Date) {																
								displayType = Excel.DISPLAY_TYPE_DATE;
								valor = DisplayType.getDateFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Timestamp) {
								displayType = Excel.DISPLAY_TYPE_DATE;
								valor = DisplayType.getDateFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else {
								valorStr = obj.toString();
								valor = valorStr;
							}
						}						
						/* Hecho por Javier Pino, sustituyendo la llamada por otro método overloaded  */														
						excel.createRow(
								//(row + 1), -- Comentado y modificado para que imprima desde el principio de las hojas sucesivas
								(short)(row % COTA_EXCEL + 1),
								(short)colPos, 
								valor, 
								valorStr,
								Excel.CELLSTYLE_NONE,
								displayType);							
					}
					colPos++;
				}	//	printed
			}	//	for all columns
		}	//	for all rows
		catch (Exception e){
			log.log(Level.SEVERE, "createCSV(w)", e);
		}
		excel.close();
		return excel;
	}

	                                             
}


