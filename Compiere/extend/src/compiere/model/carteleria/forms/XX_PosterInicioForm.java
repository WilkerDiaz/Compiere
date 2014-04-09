package compiere.model.carteleria.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.ecs.xhtml.frame;
import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.X_AD_Org;
import org.compiere.model.X_AD_Role;
import org.compiere.model.X_M_Locator;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextArea;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.X_M_Product;
import compiere.model.carteleria.X_XX_VMR_POSTER;
import compiere.model.carteleria.X_XX_VMR_PRODUCTPOSTER;
import compiere.model.carteleria.X_XX_VMR_PTYPE;
import compiere.model.carteleria.X_XX_VMR_PromProdAS;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.forms.indicator.XX_Indicator;


/**
 * 
 * @author Trinimar Acevedo
 * 
 */


/****************************************************************************************************************************
 * FUNCIONALIDAD:
 * 
 * El sistema permite realizar 3 acciones:
 * 1. Crear carteles de productos seleccionados: Se muestran todos aquellos productos que han sido escaneados con el PDA.
 * 2. Crear producto manualmente: El usuario introduce el código del producto para realizar los carteles del mismo.
 * 3. Modulo de estadísticas: permite ver la cantidad de productos vendidos
 * 
 * APLICACION EN COMPIERE: Carteleria
 ****************************************************************************************************************************/

public class XX_PosterInicioForm extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	/****Window No****/
	private int m_WindowNo = 0;
	
	/**	FormFrame	*/
	private FormFrame m_frame;
		
	/****Logger****/
	static CLogger log = CLogger.getCLogger(XX_PosterInicioForm.class);
	
	/** Context general*/ 
	protected Ctx ctx = Env.getCtx();
	
	private final static int OPCION1 = 1, OPCION2 = 2, OPCION3 = 3, OPCION4 = 4;

	/****Panels****/
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private CPanel southPanelS = new CPanel();
	private CPanel addProducPanel = new CPanel();
	private CPanel mainCenterPanel = new CPanel();
	private CPanel descPosterPanel = new CPanel();
	private JScrollPane xScrollPane = new JScrollPane();
	private CPanel printPanel = new CPanel();
	private JLabel labelTabla = new JLabel();
	
	private BorderLayout mainLayout = new BorderLayout();
	private FlowLayout northLayout = new FlowLayout();
	private BorderLayout centerLayout = new BorderLayout();
	private GridBagLayout mainCenterLayout = new GridBagLayout();
	private FlowLayout addProducLayout = new FlowLayout();
	private FlowLayout descPosterLayout = new FlowLayout();
	private FlowLayout printLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private BorderLayout southLayoutS = new BorderLayout();
	private StatusBar statusBar = new StatusBar();
		
	/****Buttons****/
	private CButton bAdd = new CButton();
	private CButton bVSave = new CButton();
	private CButton bVPreview = new CButton();
	private CButton bVMainPrint = new CButton();
	private CButton bCreatePoster= new CButton();
	private CButton bViewPoster= new CButton();
	private CButton bhelp= new CButton();
	private CButton bSearch = new CButton();
	private CButton bMainPrint = new CButton();
	private CButton bPrint = new CButton();
	private CButton bModify = new CButton();
	private CButton bPrint2 = new CButton();
	private CButton bSave = new CButton();
	private CButton bPreview = new CButton();
	private CButton bExport = new CButton();
	
	/* Archivo a exportar*/
	
	private VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	private CLabel labelFile = new CLabel();
	
	/****RadioButtons****/
	private JRadioButton opcion1 = new JRadioButton();/*Crear Catel*/
	private JRadioButton opcion2 = new JRadioButton();
	private JRadioButton opcion3 = new JRadioButton();/*Modulo de estadisticas*/
	private JRadioButton print = new JRadioButton();/*Modulo de impresion*/
	private JRadioButton visual = new JRadioButton();/*Modulo de carteles en formato libre (VM)*/
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	/****CheckBox****/
	private JCheckBox opcion4Promo = new JCheckBox();
		
	/****Labels-***/
	private CLabel addProductLabel = new CLabel();
	private CLabel consecutiveLabel = new CLabel();
	private CLabel typeOfPosterLabel = new CLabel();
	private CLabel typeOfVPosterLabel = new CLabel();
	private CLabel promotionalPosterLabel = new CLabel();
	private CLabel productsLabel = new CLabel();
	private CLabel dateILabel = new CLabel();
	private CLabel dateFLabel = new CLabel();
	private CLabel departLabel = new CLabel();
	private CLabel categoryLabel = new CLabel();
	private CLabel selectPosterLabel = new CLabel(); 
	private CLabel qtyPosterLabel = new CLabel();
	private CLabel namePosterLabel = new CLabel();
	private CLabel descPosterLabel = new CLabel();
	private CLabel descPrdLabel = new CLabel();
	private CLabel LoteLabel= new CLabel();
	private CLabel vHeaderLabel= new CLabel();
	private CLabel vContentLabel= new CLabel();
	private CLabel vNameLabel= new CLabel();
	private CLabel noteLabel= new CLabel();
	private CLabel vFromLabel= new CLabel();
	private CLabel vToLabel= new CLabel();
	private CLabel vQtyLabel= new CLabel();
			
	/*** TextField***/
	private CTextField loteTextField = new CTextField();
	private CTextField addProductTextField = new CTextField();
	private CTextField consecutiveTextField = new CTextField();
	private CTextField namePosterTextField = new CTextField();
	private CTextField descPosterTextField = new CTextField();
	private CTextField vNameTextField = new CTextField();
	private CTextField vQtyField = new CTextField();
	
	/*** TextArea***/
	private CTextArea descTextArea = new CTextArea();
	private CTextArea vHeaderTextArea = new CTextArea();
	private CTextArea vContentTextArea = new CTextArea();
	
	/****ComboBox ****/
	private CComboBox TypePosterCombo = new CComboBox();
	private CComboBox TypeVPosterCombo = new CComboBox();
	private CComboBox ProductCombo = new CComboBox();
	private CComboBox DepartCombo = new CComboBox();
	private CComboBox CategoryCombo = new CComboBox();
	private CComboBox selectPosterCombo = new CComboBox();
	private CComboBox qtyPosterCombo = new CComboBox();

	/**** Combo de fechas****/
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));
	private VDate dateFromPromo = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateToPromo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));
	
	private MiniTablePreparator xTable = new MiniTablePreparator();
	
	/**** Variables****/
	static StringBuffer    m_sql = null;
	private int addedPrd = 0;
	private boolean addButton = false;
	private String htmlcod = "";
	
	private static int option = OPCION1;
	
	private Vector productID = new Vector<String>();
	private Vector productPrice = new Vector<Boolean>();
	private Vector discPrice = new Vector<Boolean>();
	private Vector productPoster = new Vector<String>();
	int org = ctx.getAD_Org_ID();
	int rolActual = ctx.getAD_Role_ID();
	String warehouse = "";
	int locator = 0;
	float iva = 0;
	

	@Override
	
	/**
	 * Inicializa la forma
	 */
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		try{
			getWarehouse();
			jbInit();
			dynInit();    
			frame.getContentPane().add(mainPanel, BorderLayout.NORTH);	
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(southPanelS, BorderLayout.SOUTH);
			productsHide();
		}
		catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}
	}

	/** Inicializador de los datos por defecto */
	private void dynInit () {	
		ColumnInfo[] layout = null;
		xTable.setRowCount(0);
		xTable = new MiniTablePreparator();
		xScrollPane.getViewport().add(xTable, null);
		if (option == OPCION3){
			layout = new ColumnInfo[] {
					new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ProductID"),   ".", String.class, false, false, ""),
					new ColumnInfo(Msg.translate(Env.getCtx(), "XX_LoteProducts"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Cantidaces"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Categoría"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Departamento"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Tienda"),   ".", String.class, false, false, ""),
			};
			xTable.prepareTable(layout, "", "", true, "");
		}
		else if(option == OPCION1){
			layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "Select"),   "0", Boolean.class, false, false, ""), 
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ProductID"),   ".", String.class, false, false, ""),  
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_LoteProducts"),   ".", String.class, false, false, ""),
				new ColumnInfo(("Precio promocional"),   "0", Boolean.class, false, false, ""),
				new ColumnInfo(("% de Descuento"),   "0", Boolean.class, false, false, ""),
			};
			xTable.prepareTable(layout, "", "", true, "");
			xTable.getColumnModel().getColumn(0).setMaxWidth(150);
		}
		
		xTable.setAutoResizeMode(4);
		xTable.setRowSelectionAllowed(true);
		CompiereColor.setBackground (this); 
		xScrollPane.setPreferredSize(new Dimension(550, 200));
		//statusBar.setStatusDB(xTable.getRowCount());
		
		repaint();
		
	}
	
  	private void addActionListeners() {

  		loteTextField.addActionListener(this);
		TypePosterCombo.addActionListener(this);
		TypeVPosterCombo.addActionListener(this);
		ProductCombo.addActionListener(this);
		DepartCombo.addActionListener(this);
		CategoryCombo.addActionListener(this);
		opcion1.addActionListener(this);
		opcion2.addActionListener(this);
		opcion3.addActionListener(this);
		print.addActionListener(this);
		visual.addActionListener(this);
		opcion4Promo.addActionListener(this);
		bCreatePoster.addActionListener(this);
		bViewPoster.addActionListener(this);
		bMainPrint.addActionListener(this);
		bAdd.addActionListener(this);
		bVSave.addActionListener(this);
		bVPreview.addActionListener(this);
		bVMainPrint.addActionListener(this);
		bPrint.addActionListener(this);
		bModify.addActionListener(this);
		bPrint2.addActionListener(this);
		bPreview.addActionListener(this);
		bSave.addActionListener(this);
		bSearch.addActionListener(this);
		bExport.addActionListener(this);
		dateFrom.addActionListener(this);
		dateTo.addActionListener(this);
		addProductTextField.addActionListener(this);
		namePosterTextField.addActionListener(this);
		descPosterTextField.addActionListener(this);
		selectPosterCombo.addActionListener(this);
		dateFromPromo.addActionListener(this);
		dateToPromo.addActionListener(this);
		vQtyField.addActionListener(this);
	}

	
	private void removeActionListeners() {
		
		loteTextField.removeActionListener(this);
		TypePosterCombo.removeActionListener(this);
		TypeVPosterCombo.removeActionListener(this);
		ProductCombo.removeActionListener(this);
		DepartCombo.removeActionListener(this);
		CategoryCombo.removeActionListener(this);
		opcion1.removeActionListener(this);
		opcion2.removeActionListener(this);
		opcion3.removeActionListener(this);
		print.removeActionListener(this);
		visual.removeActionListener(this);
		opcion4Promo.removeActionListener(this);
		bCreatePoster.removeActionListener(this);
		bViewPoster.removeActionListener(this);
		bMainPrint.removeActionListener(this);
		bAdd.removeActionListener(this);
		bVSave.removeActionListener(this);
		bVPreview.removeActionListener(this);
		bVMainPrint.removeActionListener(this);
		bPrint.removeActionListener(this);
		bModify.removeActionListener(this);
		bPrint2.removeActionListener(this);
		bPreview.removeActionListener(this);
		bSave.removeActionListener(this);
		bSearch.removeActionListener(this);
		bExport.removeActionListener(this);
		dateFrom.removeActionListener(this);
		dateTo.removeActionListener(this);
		addProductTextField.removeActionListener(this);
		namePosterTextField.removeActionListener(this);
		descPosterTextField.removeActionListener(this);
		selectPosterCombo.removeActionListener(this);
		dateFromPromo.removeActionListener(this);
		dateToPromo.removeActionListener(this);
		vQtyField.removeActionListener(this);
	}
	

	private void jbInit() throws Exception{		

		/**Crear carteles de productos seleccionados**/
		
		removeActionListeners();
		xTable.setEnabled(true);
		uploadTypePoster();
		uploadCategory();
		
		buttonGroup.add(opcion1);
		buttonGroup.add(opcion3);
		buttonGroup.add(print);
		buttonGroup.add(visual);
		
		opcion1.setSelected(true);
		/************Cartel promocional***************/
		opcion4Promo.setSelected(false);
		addActionListeners();
		
		/****
		 * DECLARACION DE LOS LABELs**
		 */		
		
		addProductLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CodProduct"));
		typeOfPosterLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TypeOfPosterLabel"));
		promotionalPosterLabel.setText(Msg.getMsg(Env.getCtx(), "XX_promotionalPosterLabel"));	
		productsLabel.setText(Msg.getMsg(Env.getCtx(), "XX_productsLabel"));
		consecutiveLabel.setText(Msg.getMsg(Env.getCtx(), "XX_consecutiveLabel"));
		dateILabel.setText(Msg.getMsg(Env.getCtx(), "XX_dateILabel"));
		dateFLabel.setText(Msg.getMsg(Env.getCtx(), "XX_dateFLabel"));
		departLabel.setText(Msg.getMsg(Env.getCtx(), "XX_departLabel"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_categoryLabel"));
		namePosterLabel.setText(Msg.getMsg(Env.getCtx(), "XX_namePoster"));
		descPosterLabel.setText(Msg.getMsg(Env.getCtx(), "XX_descPoster"));
		selectPosterLabel.setText(Msg.getMsg(Env.getCtx(), "XX_namePoster"));
		LoteLabel.setText(Msg.getMsg(Env.getCtx(), "LoteLabel"));
		
		qtyPosterLabel.setText("No. de Carteles por hoja");
		descPrdLabel.setText("Características del producto");
		
		typeOfVPosterLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TypeOfPosterLabel"));
		vNameLabel.setText("Nombre del cartel");
		vHeaderLabel.setText("Encabezado");
		vContentLabel.setText("Contenido");
		vFromLabel.setText("Promoción válida desde:");
		vToLabel.setText("Hasta: ");
		vQtyLabel.setText("Nro. de piezas disponibles:");
		
		labelFile.setText("Archivo");
				
		/**
		 * Panel Superior Menu Sistema carteleria
		 */		
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		
		mainCenterPanel.setLayout(mainCenterLayout);
		addProducPanel.setLayout(addProducLayout);
		centerPanel.add(mainCenterPanel,  BorderLayout.CENTER);
		centerPanel.add(addProducPanel,  BorderLayout.SOUTH);
		
		descPosterPanel.setLayout(descPosterLayout);
		printPanel.setLayout(printLayout);
		southPanelS.setLayout(southLayoutS);
		
		southPanelS.add(descPosterPanel, BorderLayout.NORTH);
		
		southPanelS.add(xScrollPane, BorderLayout.CENTER);
		
		southPanelS.add(printPanel,  BorderLayout.SOUTH);

		northPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				Msg.translate(Env.getCtx(), "XX_PosterSystem")));

		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				Msg.translate(Env.getCtx(), "XX_PosterSystem")));
		
		southPanelS.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				Msg.translate(Env.getCtx(), "XX_PosterSystem")));
	
	
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(centerPanel,  BorderLayout.CENTER);
		mainPanel.add(southPanelS, BorderLayout.SOUTH);
		

		/*****   Definición de los tamaños de los textField y combBox   ****************/
		
		centerPanel.setPreferredSize(new Dimension(800, 350));
		southPanelS.setPreferredSize(new Dimension(800, 350));
		addProductTextField.setPreferredSize(new Dimension(130, 20));
		consecutiveTextField.setPreferredSize(new Dimension(50, 20));
		loteTextField.setPreferredSize(new Dimension(150,20));
		TypePosterCombo.setPreferredSize(new Dimension(150,20));
		namePosterTextField.setPreferredSize(new Dimension(150, 20));
		descPosterTextField.setPreferredSize(new Dimension(150, 20));
		selectPosterCombo.setPreferredSize(new Dimension(150, 20));
		ProductCombo.setPreferredSize(new Dimension(150, 20));
		qtyPosterCombo.setPreferredSize(new Dimension(50, 20));
		addProductTextField.setMaxLength(9);
		consecutiveTextField.setMaxLength(3);
		descTextArea.setPreferredSize(new Dimension(150, 70));
		descTextArea.setColumns(32);
		descTextArea.setRows(5);
		descTextArea.setWrapStyleWord(true);
		
		TypeVPosterCombo.setPreferredSize(new Dimension(150,20));
		vNameTextField.setPreferredSize(new Dimension(150, 20));
		vHeaderTextArea.setPreferredSize(new Dimension(150, 50));
		vContentTextArea.setPreferredSize(new Dimension(150, 50));
		vQtyField.setPreferredSize(new Dimension(50, 20));
		
		/*********************  limpieza inicial de la pantalla      ***********************/
		descTextArea.setEditable(false);
		opcion4Promo.setEnabled(true);
		StatisticsHide();
		resultStatHide();
		productsHide();
		printPosterHide();
		visualPosterHide();
		
		/********************  Llamada a los métodos para mostrar las pantallas  ***********/		
		showMain();                    /* Ppal */
		
		showCreatePoster();			   /* 1.- */
		
		showStatistics();			   /* 2.- */
		
		showPrintPoster();			   /* 3.- */
		
		showVisualPoster();			   /* 4.- */
			
		showVButtons();
		
		showProductsPoster();		   /* Listar productos a cartelizar*/
		
		/**Comprobar permisos para utilizar el Cartel Virtual**/
		
		X_AD_Role rol = new X_AD_Role(Env.getCtx(), rolActual, null); 
		if(rolActual != 0 && !rol.getName().contains("Visual") && !rol.getName().contains("AdminRole")){
			visual.setVisible(false);
		}
		
	}
	
/*************** Interfaz .- Posiciones de los campos *********/
	
	/*******************************************************************************
	 * Ppal.- Este nivel muestra la lista del menu para que el usuario haga check  *
	 * 																			   *
	 * .- MENU PRINCIPAL												   		   *
	 * 																			   *
	 ******************************************************************************/	
	public void showMain(){

		/**Crear carteles de productos seleccionados**/
		opcion1.setText(Msg.getMsg(Env.getCtx(), "XX_CreatePoster"));
		/********Crear productos manualmente************/
		//opcion2.setText(Msg.getMsg(Env.getCtx(), "XX_CreateProductPoster"));
		/************Modulo de estadisticas***************/
		opcion3.setText(Msg.getMsg(Env.getCtx(), "XX_CreateModuleEstad"));
		/************Modulo de impresión***************/
		print.setText(Msg.getMsg(Env.getCtx(), "XX_PrintPoster"));
		/************Cartel Virtual***************/
		visual.setText(Msg.getMsg(Env.getCtx(), "XX_VirtualPoster"));
		
		northPanel.add(opcion1);
		northPanel.add(opcion3);
		northPanel.add(print);
		northPanel.add(visual);

	}
	/*******************************************************************************
	 * 1.-Este nivel muestra el módulo para escojer el tipo de cartel			   *
	 * 																			   *
	 * .- OPCION 1 del menú														   *
	 * 																			   *
	 ******************************************************************************/	
	public void showCreatePoster(){
		
		productsHide();
		
			/***************   Lote de Productos  *************/
		mainCenterPanel.add(LoteLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 
					0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
					new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(loteTextField, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		
			/**********Cartel promocional***************/	
		
		mainCenterPanel.add(promotionalPosterLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(opcion4Promo, new GridBagConstraints(4 , 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		
			/***************   Tipo de cartel  *************/
		mainCenterPanel.add(typeOfPosterLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(TypePosterCombo, new GridBagConstraints(3, 1, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
				
					/**********Boton de ver cartel prototipo***************/	
		bViewPoster.setText(Msg.translate(Env.getCtx(), "bViewPoster"));	
		
		mainCenterPanel.add(bViewPoster, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
				
					/***************   Descripcion del cartel  *************/
		mainCenterPanel.add(namePosterLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(namePosterTextField, new GridBagConstraints(3, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(descPrdLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(descTextArea, new GridBagConstraints(3, 3, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		noteLabel.setText("*Campo obligatorio (Producto único)");
		noteLabel.setForeground(Color.red);
		
		mainCenterPanel.add(noteLabel, new GridBagConstraints(3, 4, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
					
					/**********Boton de crear cartel***************/	
		bCreatePoster.setText(Msg.translate(Env.getCtx(), "bCreatePoster"));	
		
		mainCenterPanel.add(bCreatePoster, new GridBagConstraints(3, 5, 1, 0, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		
		/**********Agregar producto manualmente***************/	
		addProducPanel.add(addProductLabel);
		addProducPanel.add(addProductTextField);
		addProducPanel.add(consecutiveLabel);
		addProducPanel.add(consecutiveTextField);
		bAdd.setText(Msg.translate(Env.getCtx(), "bAdd"));	
		addProducPanel.add(bAdd);
		
	}

	/*******************************************************************************
	 * 3.- Este nivel muestra el Módulo de estadisticas 					       *
	 * 																			   *
	 * .- OPCION 3 del menú														   *
	 * 																			   *
	 ******************************************************************************/	
	public void showStatistics(){
			
		productsHide();
		
		/**********Busqueda por categoria***************/	

		mainCenterPanel.add(categoryLabel, new GridBagConstraints(2, 6, 1, 1, 0.0, 
					0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
					new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(CategoryCombo, new GridBagConstraints(4, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		
		/**********Busqueda por departamento***************/	

		mainCenterPanel.add(departLabel, new GridBagConstraints(2, 8, 1, 1, 0.0, 
					0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
					new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(DepartCombo, new GridBagConstraints(4, 8, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		
		/**********Busqueda por producto***************/	
		
		mainCenterPanel.add(productsLabel, new GridBagConstraints(2, 10, 1, 1, 0.0, 
					0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
					new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(ProductCombo, new GridBagConstraints(4, 10, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		
		/********Busqueda por fecha inicial************/
		
		mainCenterPanel.add(dateILabel, new GridBagConstraints(6, 6, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(dateFrom, new GridBagConstraints(8, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		
		/********Busqueda por fecha final************/
		
		mainCenterPanel.add(dateFLabel, new GridBagConstraints(6, 8, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(dateTo, new GridBagConstraints(8, 8, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		
		/*Boton de busqueda*/	
		bSearch.setText(Msg.translate(Env.getCtx(), "bSearch"));	
		
		mainCenterPanel.add(bSearch, new GridBagConstraints(8, 10, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		
	}
	/*******************************************************************************
	 * 1.1.- Este nivel muestra el Módulo de agregar productos al cartel 	       *
	 * 																			   *
	 * .- OPCION 1.1 Agregar productos al cartel	LoteCombo2					   *
	 * 																			   *
	 ******************************************************************************/	
	public void showProductsPoster(){
		bSave.setText(Msg.translate(Env.getCtx(), "bSave"));
		bPreview.setText(Msg.translate(Env.getCtx(), "bPreview"));
		bPrint.setText(Msg.translate(Env.getCtx(), "bPrint"));
		bMainPrint.setText(Msg.translate(Env.getCtx(), "bPrint"));
		bExport.setText("Exportar");
		printLayout.setHgap(20);
		printPanel.add(bSave);
		printPanel.add(bPreview);
		printPanel.add(bMainPrint);
		printPanel.add(labelFile, null);
		printPanel.add(bFile, null);
		printPanel.add(bExport);
			
	}
	
	public void showVButtons(){
		bVSave.setText("Guardar");
		bVPreview.setText("Vista Previa");	
		bVMainPrint.setText("Imprimir");
		addProducPanel.add(bVSave);
		addProducPanel.add(bVPreview);
		addProducPanel.add(bVMainPrint);
	}
	
	
	public void showPrintPoster(){

		productsHide();
		
		mainCenterPanel.add(selectPosterLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(selectPosterCombo, new GridBagConstraints(4, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
//		mainCenterPanel.add(qtyPosterLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 
//				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
//				new Insets(0, 5, 5, 5), 0, 0));
//		
//		mainCenterPanel.add(qtyPosterCombo, new GridBagConstraints(4, 3, 1, 1, 0.0, 
//				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
//				new Insets(0, 5, 5, 5), 0, 0));
		
		bPrint2.setText(Msg.translate(Env.getCtx(), "bPrint"));	
		
		mainCenterPanel.add(bPrint2, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));	
		
	}
	
	public void showVisualPoster(){
		
		mainCenterPanel.add(vNameLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vNameTextField, new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(typeOfVPosterLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(TypeVPosterCombo, new GridBagConstraints(3, 1, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vHeaderLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vHeaderTextArea, new GridBagConstraints(3, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vContentLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vContentTextArea, new GridBagConstraints(3, 3, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vFromLabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(dateFromPromo, new GridBagConstraints(3, 4, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vToLabel, new GridBagConstraints(2, 5, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(dateToPromo, new GridBagConstraints(3, 5, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vQtyLabel, new GridBagConstraints(2, 6, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		mainCenterPanel.add(vQtyField, new GridBagConstraints(3, 6, 1, 1, 0.0, 
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
	}

	/**************** Muestra u oculta los  campos *****************/
	
	/* 1.- Se muestran los parametros para seleccionar el tipo de cartel*/
	public void showTypeOfPoster(){
		
		bCreatePoster.setVisible(true);
		bhelp.setVisible(true);
		bViewPoster.setVisible(true);
		loteTextField.setVisible(true);
		LoteLabel.setVisible(true);
		typeOfPosterLabel.setVisible(true);
		TypePosterCombo.setVisible(true);
		opcion4Promo.setVisible(true);
		promotionalPosterLabel.setVisible(true);
		namePosterLabel.setVisible(true);
		namePosterTextField.setVisible(true);
//		descPosterLabel.setVisible(true);
//		descPosterTextField.setVisible(true);
		descPrdLabel.setVisible(true);
		descTextArea.setVisible(true);
		bAdd.setVisible(true);
		addProductLabel.setVisible(true);
		addProductTextField.setVisible(true);
		consecutiveLabel.setVisible(true);
		consecutiveTextField.setVisible(true);
		noteLabel.setVisible(true);
		
	}
	/* 1.- Se ocultan los parametros para seleccionar el tipo de cartel*/
	public void HideTypeOfPoster(){
		
		bCreatePoster.setVisible(false);
		bhelp.setVisible(false);
		bViewPoster.setVisible(false);
		loteTextField.setVisible(false);
		LoteLabel.setVisible(false);
		typeOfPosterLabel.setVisible(false);
		TypePosterCombo.setVisible(false);
		opcion4Promo.setVisible(false);
		promotionalPosterLabel.setVisible(false);
		namePosterLabel.setVisible(false);
		namePosterTextField.setVisible(false);
//		descPosterLabel.setVisible(false);
//		descPosterTextField.setVisible(false);
		descPrdLabel.setVisible(false);
		descTextArea.setVisible(false);
		bAdd.setVisible(false);
		addProductLabel.setVisible(false);
		addProductTextField.setVisible(false);
		consecutiveLabel.setVisible(false);
		consecutiveTextField.setVisible(false);
		noteLabel.setVisible(false);
		
	}

	public void EnableTypeOfPoster(){
		bCreatePoster.setEnabled(true);
		//bhelp.setEnabled(true);
		//bViewPoster.setEnabled(true);
		//typeOfPosterLabel.setEnabled(true);
		//TypePosterCombo.setEnabled(true);
	}
	
	public void DisableTypeOfPoster(){
		bCreatePoster.setEnabled(false);
		//bhelp.setEnabled(false);
		//bViewPoster.setEnabled(false);
		//typeOfPosterLabel.setEnabled(false);
		//TypePosterCombo.setEnabled(false);
	}
	
	/* 3.- Para mostrar los parametros, de busqueda para estadisticas*/	
	public void StatisticsShow(){	
		categoryLabel.setVisible(true);
		CategoryCombo.setVisible(true);
		departLabel.setVisible(true);
		DepartCombo.setVisible(true);
		productsLabel.setVisible(true);
		ProductCombo.setVisible(true);		
		dateILabel.setVisible(true);
		dateFrom.setVisible(true);
		dateFLabel.setVisible(true);
		dateTo.setVisible(true);
		bSearch.setVisible(true);
		
	}
	/* 3.- Para ocultar los parametros, de busqueda para estadisticas*/
	public void StatisticsHide(){
		
		categoryLabel.setVisible(false);
		CategoryCombo.setVisible(false);
		departLabel.setVisible(false);
		DepartCombo.setVisible(false);
		productsLabel.setVisible(false);
		ProductCombo.setVisible(false);		
		dateILabel.setVisible(false);
		dateFrom.setVisible(false);
		dateFLabel.setVisible(false);
		dateTo.setVisible(false);
		bSearch.setVisible(false);

	}
	
	public void resultStatShow(){	
		xScrollPane.setVisible(true);
		labelFile.setVisible(true);
		bFile.setVisible(true);
		bExport.setVisible(true);
		
	}
	
	public void resultStatHide(){	
		xScrollPane.setVisible(false);
		labelFile.setVisible(false);
		bFile.setVisible(false);
		bExport.setVisible(false);
		
	}
	
	public void productsShow(){
		
		xScrollPane.setVisible(true);
		bSave.setVisible(true);
		bPreview.setVisible(true);
		bMainPrint.setVisible(true);
		//bPrint.setVisible(true);
		
		
	}
	
	public void productsHide(){	
		
		xScrollPane.setVisible(false);
		bSave.setVisible(false);
		bPreview.setVisible(false);
		bMainPrint.setVisible(false);
		//bPrint.setVisible(false);
		
	}
	
	public void printPosterShow(){	
		selectPosterLabel.setVisible(true);
		selectPosterCombo.setVisible(true);
//		qtyPosterLabel.setVisible(true);
//		qtyPosterCombo.setVisible(true);
		bPrint2.setVisible(true);
		
	}

	public void printPosterHide(){	
		selectPosterLabel.setVisible(false);
		selectPosterCombo.setVisible(false);
//		qtyPosterLabel.setVisible(false);
//		qtyPosterCombo.setVisible(false);
		bPrint2.setVisible(false);
	}
	
	public void visualPosterShow(){	
		typeOfVPosterLabel.setVisible(true);
		TypeVPosterCombo.setVisible(true);
		vHeaderLabel.setVisible(true);
		vHeaderTextArea.setVisible(true);
		vNameLabel.setVisible(true);
		vNameTextField.setVisible(true);
		vContentLabel.setVisible(true);
		vContentTextArea.setVisible(true);
		bVSave.setVisible(true);
		bVPreview.setVisible(true);
		bVMainPrint.setVisible(true);
		vFromLabel.setVisible(true);
		vToLabel.setVisible(true);
		dateFromPromo.setVisible(true);
		dateToPromo.setVisible(true);
		vQtyLabel.setVisible(true);
		vQtyField.setVisible(true);
	}

	public void visualPosterHide(){	
		typeOfVPosterLabel.setVisible(false);
		TypeVPosterCombo.setVisible(false);
		vHeaderLabel.setVisible(false);
		vHeaderTextArea.setVisible(false);
		vNameLabel.setVisible(false);
		vNameTextField.setVisible(false);
		vContentLabel.setVisible(false);
		vContentTextArea.setVisible(false);
		bVSave.setVisible(false);
		bVPreview.setVisible(false);
		bVMainPrint.setVisible(false);
		vFromLabel.setVisible(false);
		vToLabel.setVisible(false);
		dateFromPromo.setVisible(false);
		dateToPromo.setVisible(false);
		vQtyLabel.setVisible(false);
		vQtyField.setVisible(false);
	}
	
	/**
	 *  Ejecuta el método correspondiente a las diferentes acciones definidas
	 *  @param  e La acción del evento
	 */
	public void actionPerformed (ActionEvent e){	
		runMain(e);
		runButton(e);
	
	}
	
	/********** Check Acciones.-Acciones del menu principal *******/ 
	public void runMain (ActionEvent e){
		removeActionListeners();
		
		
		/**** Llamada a crear carteles de productos seleccionados**/
		if (e.getSource() == opcion1){	
			option = OPCION1;
			TypePosterCombo.setSelectedIndex(-1);
			descTextArea.setEditable(false);
			opcion4Promo.setSelected(false);
			opcion4Promo.setEnabled(true);
			loteTextField.setText("");
			addProductTextField.setText("");
			consecutiveTextField.setText("");
			namePosterTextField.setText("");
			descPosterTextField.setText("");
			descTextArea.setText("");
			dynInit();
			StatisticsHide();
			resultStatHide();
			printPosterHide();
			visualPosterHide();
			showTypeOfPoster(); 
			

		/************Llamada al modulo de estadisticas***************/		
		}else if(e.getSource() == opcion3){	
			option = OPCION3;
			productsHide();
			dynInit();
			CategoryCombo.setSelectedIndex(-1);
			DepartCombo.setSelectedIndex(-1);
			ProductCombo.setSelectedIndex(-1);
			dateFrom.setValue(null);
			dateTo.setValue(null);
			HideTypeOfPoster();	
			printPosterHide();
			visualPosterHide();
			resultStatHide();
			StatisticsShow();
			uploadCategory();
			uploadDepartments();
			uploadProduct();
			
		}else if(e.getSource() == print){
			option = OPCION2;
			qtyPosterCombo.setEnabled(false);
			uploadPoster();
			productsHide();
			HideTypeOfPoster();	
			StatisticsHide();
			resultStatHide();
			visualPosterHide();
			printPosterShow();
			bPrint2.setEnabled(false);
			
		}else if(e.getSource() == visual){	
			option = OPCION4;
			TypeVPosterCombo.setSelectedIndex(-1);
			vNameTextField.setText("");
			vHeaderTextArea.setText("");
			vContentTextArea.setText("");
			vQtyField.setText("1");
			productsHide();
			HideTypeOfPoster();	
			StatisticsHide();
			resultStatHide();
			visualPosterShow();
			printPosterHide();
		}
		addActionListeners();
	}
	

	/** Botones Acciones.- Referentes a los botones **/
	public void runButton(ActionEvent e){                  										  
		
		removeActionListeners();
		KeyNamePair TypePoster = (KeyNamePair)TypePosterCombo.getSelectedItem();
		KeyNamePair TypeVPoster = (KeyNamePair)TypeVPosterCombo.getSelectedItem();
		String LotePoster = loteTextField.getText();
		KeyNamePair printPoster = (KeyNamePair)selectPosterCombo.getSelectedItem();
		JFrame j = new JFrame();
		
		if (e.getSource() == bCreatePoster){	
			if(!LotePoster.equals("")){
				try {
					tableLoad();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				productsShow();	
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorLotePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}					
		}else if (e.getSource() == bViewPoster){			
			if(TypePoster.getKey()!=-1){
				productsHide();
				getHelp();	
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorTypePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}			
		}else if (e.getSource() == TypePosterCombo){
			if(TypePoster.getKey()!=-1){
				if(TypePoster.getName().contains("único")){
//					opcion4.setSelected(false);
//					opcion4.setEnabled(false);
					descTextArea.setEditable(true);
				}else{
//					opcion4.setEnabled(true);
					descTextArea.setEditable(false);
				}
				/*if(TypePoster.getName().contains("Display")){
					if(!opcion4.isSelected()){
						xTable.setColumnReadOnly(3, false);
					}else{
						setNotSelectedColumn(3);
						xTable.setColumnReadOnly(3, true);
					}
					xTable.setColumnReadOnly(4, false);
				}else{*/
					if(opcion4Promo.isSelected()){
						setNotSelectedColumn(3);
						xTable.setColumnReadOnly(3, true);
						xTable.setColumnReadOnly(4, false);
					}else{
						setNotSelectedColumn(3);
						xTable.setColumnReadOnly(3, true);
						setNotSelectedColumn(4);
						xTable.setColumnReadOnly(4, true);
					}
				//}
			}
		}else if(e.getSource() == opcion4Promo){
			if (opcion4Promo.isSelected()){
				setNotSelectedColumn(3);
				xTable.setColumnReadOnly(3, true);
				xTable.setColumnReadOnly(4, false);
			}else{
				setNotSelectedColumn(3);
				xTable.setColumnReadOnly(3, true);
				setNotSelectedColumn(4);
				xTable.setColumnReadOnly(4, true);
			}
		}else if(e.getSource() == CategoryCombo){
			uploadDepartments();
		}else if(e.getSource() == DepartCombo){
			uploadProduct();
		}else if(e.getSource()== bAdd){		
				addButton = true;
				String productId = String.valueOf(Integer.parseInt(addProductTextField.getText()));
				String consc = consecutiveTextField.getText();
				productsHide();
				if(!productId.isEmpty()){
					productsHide();
					try {
						int product = searchProduct(productId, consc);
						if (product != 0){
							tableLoadPrd(productId, consc);
							productsShow();	
							addedPrd +=1; 
						}else{
							ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorAddingProduct", new String[] {"'"+productId+"'"}));
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}else{
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorEmptyProduct"));
				}
			
		}else if(e.getSource()==bSearch){		
			tableLoadStat();
			resultStatShow();
		}else if(e.getSource()==bSave){	
			if(TypePoster.getKey()!=-1){
				getSelectedProduct();
				if(productID.size() > 0){
					verifyTypePoster(TypePoster.getName(), productID.size(), 1);
				}else{
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorSelectedPrd", new String[] {""}));
				}
				
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorTypePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}
		}else if(e.getSource()==bPreview){
			if(TypePoster.getKey()!=-1){
				getSelectedProduct();
				if(productID.size() > 0){
					getProducts(1);
					verifyTypePoster(TypePoster.getName(), productID.size(), 2);
				}else{
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorSelectedPrd", new String[] {""}));
				}
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorTypePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}

		}else if(e.getSource()==bPrint){	
			PrintUtilities.printComponent(labelTabla);
			
		}else if(e.getSource() == selectPosterCombo){
			if(printPoster.getKey()!= -1){
				uploadQtyPoster();
				qtyPosterCombo.setEnabled(true);
				qtyPosterCombo.setSelectedIndex(0);
				bPrint2.setEnabled(true);
			}else{
				qtyPosterCombo.setSelectedIndex(0);
				qtyPosterCombo.setEnabled(false);
				bPrint2.setEnabled(false);
			}
		}else if(e.getSource()==bMainPrint){		
			if(TypePoster.getKey()!=-1){
				getSelectedProduct();
				if(productID.size() > 0){
					getProducts(1);
					verifyTypePoster(TypePoster.getName(), productID.size(), 3);
				}else{
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorSelectedPrd", new String[] {""}));
				}
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorTypePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}
		}else if(e.getSource()==bVSave){	
			if(TypeVPoster.getKey()!=-1){
				if(vContentTextArea.getText().equals("") || vHeaderTextArea.getText().equals("")){
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorContentPoster", new String[] {""}));
				}else{
					InsertarPosterVisual();
				}
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorTypePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}
		}else if(e.getSource()==bVPreview){
			
			if(TypeVPoster.getKey()!=-1){
				if(vContentTextArea.getText().equals("") || vHeaderTextArea.getText().equals("")){
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorContentPoster", new String[] {""}));
				}else{
					if(TypeVPoster.getName().contains("Vertical")){
						createLibreCarta("", "","");
					}else if (TypeVPoster.getName().contains("Horizontal")){
						createLibreCartaL("", "","");
					}else if(TypeVPoster.getName().contains("Cubo")){
						createLibreCubo("","","");
					}else if(TypeVPoster.getName().contains("Base")){
						createLibreBasePlana("","","");
					}else{
						createLibreVitrina();
					}
				}
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorTypePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}
		}else if(e.getSource()==bVMainPrint){
			
			if(TypeVPoster.getKey()!=-1){
				if(vContentTextArea.getText().equals("") || vHeaderTextArea.getText().equals("")){
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorContentPoster", new String[] {""}));
				}else{
					if(TypeVPoster.getName().contains("Vertical")){
						createLibreCarta("","","");
					}else if (TypeVPoster.getName().contains("Horizontal")){
						createLibreCartaL("","", "");
					}else if(TypeVPoster.getName().contains("Cubo")){
						createLibreCubo("","","");
					}else if(TypeVPoster.getName().contains("Base")){
						createLibreBasePlana("","","");
					}else{
						createLibreVitrina();
					}
				}
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorTypePoster", new String[] {"'"+TypePoster.getName()+"'"}));
			}
		}else if(e.getSource()==bPrint2){		
			X_XX_VMR_POSTER poster = new X_XX_VMR_POSTER(Env.getCtx(), printPoster.getKey(), null);
			X_XX_VMR_PTYPE type = new X_XX_VMR_PTYPE(Env.getCtx(), poster.getXX_VMR_PTYPE_ID(), null);
			if(type.getName().contains("Libre")){
				String[] texto =poster.getdescPoster().split("---");
				String promo = "";
				if(texto.length == 3){
					promo = texto[2];
				}
				if(type.getName().contains("Vertical")){
					createLibreCarta(texto[0], texto[1], promo);
				}else if (type.getName().contains("Horizontal")){
					createLibreCartaL(texto[0], texto[1], promo);
				}else if(type.getName().contains("Cubo")){
					createLibreCubo(texto[0], texto[1], promo);
				}else {
					createLibreBasePlana(texto[0], texto[1], promo);
				}
			}else{
				getProducts(2);
				verifyTypePoster(type.getName(), getTypePoster(printPoster.getKey()), 4);

			}
		}else if(e.getSource()==bExport){
			XX_Indicator.imprimirArchivo(xTable, bFile, m_WindowNo, m_frame); 
		}
		
		addActionListeners();
	}
	
	/***************** Carga de  filtros ****************/
	/** Carga datos del filtro de tipos de carteles */ 
	private void uploadTypePoster(){
		String sqlPartner = "select XX_VMR_PType_ID, name " +
							"from XX_VMR_PType " +
							"order by name ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();

			TypePosterCombo.addItem(new KeyNamePair(-1, null));
			TypeVPosterCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				if(rs.getString(2).contains("Libre")){
					TypeVPosterCombo.addItem(new KeyNamePair(rs.getInt(1), rs
							.getString(2)));
				}else{
					TypePosterCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
				}
			}
			addProductTextField.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPartner, e);
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
	
	
	/***************** Carga de  filtros ****************/
	/** Verifica que el código introducido esté asociado a un producto en 
	 * la base de datos 
	 * @param productCod Código beco de un producto (Value)
	 * @param cons String correspondiente al consecutivo de precio del producto
	 * @return product Id del producto encontrado
	 */ 
	private int searchProduct(String productCod, String cons) {
		String sqlPartner = "";
		if(cons.equals("")){
			/*Se verifica que existe al menos un consecutivo de precio asignado al producto*/
			sqlPartner = "select p.M_Product_ID, p.name, pc.xx_priceconsecutive " +
			"from M_product p JOIN xx_vmr_priceconsecutive pc " +
			"ON (p.m_product_id = pc.m_product_id) " +
			"where p.value='"+Integer.parseInt(productCod)+"'";
		}else{
			sqlPartner = "select p.M_Product_ID, p.name " +
			"from M_product p JOIN xx_vmr_priceconsecutive pc " +
			"ON (p.m_product_id = pc.m_product_id) " +
			"where  pc.xx_priceconsecutive = "+Integer.parseInt(cons)+" and p.value='"+Integer.parseInt(productCod)+"'";	
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int product = 0;
		
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				product = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPartner, e);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return product;
	}
	
	/**
	 * Método para obtener el valor de la tienda del contexto
	 */
	private void getWarehouse() {
		String sql = "select m.value, l.m_locator_id " +
					"from M_warehouse m join M_Locator l on (l.m_warehouse_id = m.m_warehouse_id) " +
					"where l.value LIKE '%EN TIENDA%' and m.AD_Org_ID="+org;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				warehouse = rs.getString(1);
				locator = rs.getInt(2);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}finally{
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
	
	/** Carga la informacion de la categoria y valida que se pueda seleccionar el departamento**/
	public final void uploadBasicInfo() {
			uploadCategory();
			valUploadDepartments();
			  
	}
	
	/** Carga la información del combo de departamentos, dependiendo de la categoria seleccionada**/
	public void valUploadDepartments(){

				KeyNamePair catg = (KeyNamePair)CategoryCombo.getSelectedItem();
				System.out.println(catg);
				if (catg != null  && catg.getKey() != 99999999){ 
					set(DepartCombo, true);		
					uploadDepartments();
				} else if(catg.getKey() == 99999999){
					DepartCombo.setSelectedIndex(0);
					set(DepartCombo, false);				
				}	
		}
	
	/** Carga datos del filtro categorías */
	private void uploadCategory() {
	
		KeyNamePair loadKNP;
		String sql = "";						
		CategoryCombo.removeAllItems();
		loadKNP = new KeyNamePair(-1,"");  													
		CategoryCombo.addItem(loadKNP);
		sql = " SELECT ct.XX_VMR_CATEGORY_ID, ct.VALUE||'-'||ct.NAME " +
				" FROM XX_VMR_CATEGORY ct WHERE ct.ISACTIVE = 'Y' AND ct.AD_Client_ID = "+ ctx.getAD_Client_ID(); 	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				CategoryCombo.addItem(loadKNP);			
			}				
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
		finally{
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
	
	/** Carga datos del filtro departamentos */ 
	private void uploadDepartments() {
		
		KeyNamePair catg = (KeyNamePair)CategoryCombo.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DepartCombo.removeAllItems();
		loadKNP = new KeyNamePair(-1,"");
		DepartCombo.addItem(loadKNP);
		
		if (catg.getKey() != -1 && catg != null){			
			sql = "SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||'-'||dp.NAME FROM XX_VMR_DEPARTMENT dp " 
				+ "WHERE dp.XX_VMR_CATEGORY_ID = " + catg.getKey() +  " ORDER BY dp.VALUE ";
		}  else {					
			sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT " +
					"WHERE AD_Client_ID = "+ ctx.getAD_Client_ID()+ " ORDER BY VALUE||'-'||NAME";									
		} 
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				DepartCombo.addItem(loadKNP);			
			}				
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
	 
	/** Carga datos del filtro de productos*/
	private void uploadProduct() {
	
		KeyNamePair catg = (KeyNamePair)CategoryCombo.getSelectedItem();				
		KeyNamePair dep = (KeyNamePair)DepartCombo.getSelectedItem();
		KeyNamePair loadKNP;
		String sql;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductCombo.removeAllItems();
		loadKNP = new KeyNamePair(-1,"");
		ProductCombo.addItem(loadKNP);
		
		sql = "SELECT DISTINCT p.M_PRODUCT_ID, p.name FROM M_PRODUCT p, XX_VMR_PRODUCTPOSTER xp " +
					"WHERE p.M_PRODUCT_ID = xp.M_PRODUCT_ID";
		
		if (catg.getKey() != -1 && catg.getKey() != 99999999){			
			sql = sql +" AND p.XX_VMR_CATEGORY_ID = " + catg.getKey();
		}  else if (dep.getKey() != -1 && dep.getKey() != 99999999) {					
			sql = sql +" AND p.XX_VMR_DEPARTMENT_ID = " + dep.getKey();
		} 
		sql = sql + " ORDER BY p.M_PRODUCT_ID";		
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(1)+"-"+rs.getString(2));
				ProductCombo.addItem(loadKNP);			
			}				
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
		
	/** Carga datos a imprimir en la tabla del módulo de estadísticas*/ 
	private void tableLoadStat(){
		
		int row = 0; 
		xTable.setRowCount(0);
		KeyNamePair catg = (KeyNamePair)CategoryCombo.getSelectedItem();
		KeyNamePair dep = (KeyNamePair)DepartCombo.getSelectedItem();
		KeyNamePair prod = (KeyNamePair)ProductCombo.getSelectedItem();
		String datef= "";	
		String datet= "";
		
		String sql = "SELECT P.VALUE, P.NAME, "+
			"SUM(CASE WHEN OL.QTYENTERED IS NOT NULL THEN OL.QTYENTERED ELSE 0 END) CANTIDAD, "+
			"C.VALUE||'-'||C.NAME, D.VALUE||'-'||D.NAME, W.VALUE||'-'||W.NAME "+
			"FROM C_ORDERLINE OL "+
			"JOIN C_ORDER O ON (O.C_ORDER_ID = OL.C_ORDER_ID) "+
			"JOIN M_PRODUCT P ON (P.M_PRODUCT_ID = OL.M_PRODUCT_ID) "+
			"JOIN XX_VMR_PRODUCTPOSTER PP ON (P.M_PRODUCT_ID = PP.M_PRODUCT_ID) "+
			"JOIN M_WAREHOUSE W ON (O.M_WAREHOUSE_ID = W. M_WAREHOUSE_ID) "+
			"JOIN XX_VMR_CATEGORY C ON (C.XX_VMR_CATEGORY_ID = P.XX_VMR_CATEGORY_ID) "+
			"JOIN XX_VMR_DEPARTMENT D ON (D.XX_VMR_DEPARTMENT_ID = P.XX_VMR_DEPARTMENT_ID) ";
		
		String where = "WHERE ISSOTRX = 'Y' "+
			"AND OL.QTYENTERED <> 0 "+
			"AND O.DATEORDERED <= sysdate ";

		if(catg.getKey() != -1 && catg != null){
			where += " AND C.XX_VMR_CATEGORY_ID = "+catg.getKey();
		}
		if(dep.getKey() != -1 && dep != null){
			where += " AND D.XX_VMR_DEPARTMENT_ID = "+dep.getKey();
		}
		
		if(prod.getKey() != -1 && prod != null){
			where += "AND OL.M_PRODUCT_ID ="+prod.getKey();
		}
		if(dateFrom.getValue() != null){
			datef = (dateFrom.getValue()).toString();
			where += " AND trunc(O.DATEORDERED) >= "+"TO_DATE ('"+datef.substring(0, 10)+"', 'yyyy-mm-dd')";
		}
		if(dateTo.getValue() != null){
			datet = (dateTo.getValue()).toString();
			where += " AND trunc(O.DATEORDERED) <= "+"TO_DATE ('"+datet.substring(0, 10)+"', 'yyyy-mm-dd')";
		}
		sql += where+" GROUP BY P.VALUE , P.NAME, C.VALUE||'-'||C.NAME , D.VALUE||'-'||D.NAME, W.VALUE||'-'||W.NAME ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				xTable.setRowCount(row + 1);
				xTable.setValueAt(rs.getString(1), row, 0); 
				xTable.setValueAt(rs.getString(2), row, 1);
				xTable.setValueAt(rs.getString(3), row, 2);
				xTable.setValueAt(rs.getString(4), row, 3);
				xTable.setValueAt(rs.getString(5), row, 4);
				xTable.setValueAt(rs.getString(6), row, 5);
				row++;
			}
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
	
	/** Carga datos del filtro de carteles disponibles para imprimir*/ 
	private void uploadPoster(){
		selectPosterCombo.removeAllItems();
		String sqlPartner = "";
		if (warehouse.equals("")){
			/*Uruasios System o BecoAdmin*/
			sqlPartner = "SELECT p.XX_VMR_Poster_ID, p.name " +
			"FROM XX_VMR_Poster p JOIN XX_VMR_PType t " +
			"ON (p.XX_VMR_PType_ID = t.XX_VMR_PType_ID) " +
			"WHERE p.isactive = 'Y' " +
			"AND (p.posterWarehouse IS NULL "+
			"OR t.name LIKE '%Libre%') " +
			"ORDER BY p.name ";
		}else{
			sqlPartner = "SELECT p.XX_VMR_Poster_ID, p.name " +
			"FROM XX_VMR_Poster p JOIN XX_VMR_PType t " +
			"ON (p.XX_VMR_PType_ID = t.XX_VMR_PType_ID) " +
			"WHERE p.isactive = 'Y' " +
			"AND (p.posterWarehouse = '"+warehouse+"' "+
			"OR t.name LIKE '%Libre%') " +
			"ORDER BY p.name ";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();

			selectPosterCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				selectPosterCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPartner, e);
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
	 * Rutina que actualiza dinámicamente el combo de cantidad máxima 
	 * de carteles por página a imprimir 
	 */
	private void uploadQtyPoster(){
		qtyPosterCombo.removeAllItems();
		KeyNamePair poster = (KeyNamePair)selectPosterCombo.getSelectedItem();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlPartner = "SELECT t.name " +
		"FROM XX_VMR_Poster p JOIN XX_VMR_Ptype t " +
		"ON (p.XX_VMR_Ptype_id = t.XX_VMR_Ptype_id) " +
		"WHERE p.isactive = 'Y' AND p.XX_VMR_Poster_id = "+poster.getKey();
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getString(1).equals("Elisa /Jota")){
					for(int i = 1; i <= 10; i++) {
						qtyPosterCombo.addItem(new KeyNamePair(i, String.valueOf(i)));
					}
				}else{
					qtyPosterCombo.addItem(new KeyNamePair(1, String.valueOf(1)));
					qtyPosterCombo.addItem(new KeyNamePair(2, String.valueOf(2)));
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPartner, e);
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
	
	
	/**************************************************************************
	 *  Listar los productos asociados a un lote específico
	 * @throws SQLException 
	 */
	private void tableLoad () throws SQLException
	{
		
		String codProd;
		ResultSet rs = null;
		String lote = loteTextField.getText();	
		Statement ps_s = null;
		PreparedStatement pstmt = null;
		ResultSet rsc = null;
		String product = "";
		int row = 0; 
		//xScrollPane.getViewport().add(xTable);
		As400DbManager as400 = new As400DbManager();
		as400.conectar();
		
		if(addedPrd == xTable.getRowCount() && addButton){
			row = addedPrd;
		}else{
			xTable.setRowCount(0);
			addedPrd = 0;
		}
		try {
			
			ps_s = as400.conexion.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			String org = "select XX_Suc from ad_org where ad_org_id="+Env.getCtx().getAD_Org_ID()+"";
			PreparedStatement psOrg = null;
			ResultSet rsOrg = null;
			String tienda=null;
			try{
				psOrg = DB.prepareStatement(org, null);
				rsOrg = psOrg.executeQuery();
				if(rsOrg.next()){
					tienda=rsOrg.getString("XX_Suc");
				}
			} catch(SQLException e){	
				e.printStackTrace();
			}
			finally{
				DB.closeResultSet(rsOrg);
				DB.closeStatement(psOrg);
			}
			
			codProd= "select DISTINCT s.PRDPTC from ictfile.solptc s where s.lotptc=" + lote+" ";
			if(tienda!=null){
				codProd+=" and s.tndptc='"+tienda+"'";
			}
			
			
			rs= as400.realizarConsulta(codProd, ps_s);
			while(rs.next()){
				
				int value = Integer.parseInt(rs.getString(1).substring(0, rs.getString(1).length() - 3));
				product = "select m_product_id, name from m_product where value = "+"'"+value+"'";
				try{
				pstmt = DB.prepareStatement(product, null);
				rsc = pstmt.executeQuery();
				if (rsc.next()){
					xTable.setRowCount(row + 1);
					xTable.setValueAt(false, row, 0); 
					xTable.setValueAt(rs.getString(1), row, 1); 
					xTable.setValueAt(rsc.getString(2), row, 2); 
					xTable.isCellEditable(row, 2);
					xTable.setValueAt(false, row, 3); 
					xTable.setValueAt(false, row, 4);
					
					row++;
				}
				}catch (SQLException e0){
					e0.printStackTrace();
				} finally{
					rsc.close();
					pstmt.close();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			as400.desconectar();
			rs.close();
			ps_s.close();
		}
		addButton = false;
		//xTable.repaint();
		
	}   //  tableLoad
	
	
	/**
	 * Agrega un producto a la lista productos seleccionables para generar carteles
	 * @param prodCod Código beco del producto
	 * @param consc Consecutivo de precio del producto
	 * @throws SQLException
	 */
	private void tableLoadPrd (String prodCod, String consc) throws SQLException
	{
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String product = "";
		String value = "";
		
		int row = 0; 
		
		row = xTable.getRowCount();
				
		product = "SELECT p.value, p.name, max(pc.xx_priceconsecutive) from m_product p LEFT JOIN xx_vmr_priceconsecutive pc " +
				"ON (p.m_product_id = pc.m_product_id) WHERE p.value = '"+prodCod+"' "+
				" GROUP BY p.value, p.name";
				
					//" and ad_org_id ="+org;
		try{
			
			pstmt = DB.prepareStatement(product, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				if (consc.isEmpty() && rs.getInt(3)!=0){
					consc = String.valueOf(rs.getInt(3));
				}
				if(!consc.isEmpty()){
					while(consc.length()<3){
						consc = '0'+consc;
					}
				}
				value = rs.getString(1);
				while(value.length()<9){
					value = '0'+value;
				}
				
				xTable.setRowCount(row + 1);
				xTable.setValueAt(false, row, 0); 
				xTable.setValueAt(value+consc, row, 1); 
				xTable.setValueAt(rs.getString(2), row, 2); 
				xTable.setValueAt(false, row, 3); 
				xTable.setValueAt(false, row, 4); 
				row++;
			}
			}catch (SQLException e0){
				e0.printStackTrace();
			} finally{
 				rs.close();
				pstmt.close();
		}
		xTable.repaint();
		
	}
	
	/**
	 * Coloca todos los elementos de una columna particular como no seleccionados
	 * @param column Columna a desmarcar 
	 */
	public void setNotSelectedColumn(int column){
		
		for(int j=0; j<xTable.getRowCount(); j++){
			xTable.setValueAt(false, j, column);
		}
		xTable.repaint();
	}
	
	/**Proceso que recoge en un vector los productos seleccionados 
	 * para un cartel
	 */
	public synchronized void getSelectedProduct(){
		productID.removeAllElements();
		productPrice.removeAllElements();
		discPrice.removeAllElements();
		String selectedKey = "";
		for(int j=0; j<xTable.getRowCount(); j++){
			if(new Boolean(xTable.getModel().getValueAt(j, 0).toString())){
				selectedKey = (String)xTable.getValueAt(j, 1)+"-"+xTable.getValueAt(j, 2);	
				productID.add(selectedKey);
				if(new Boolean(xTable.getModel().getValueAt(j, 3).toString())){
					productPrice.add(true);
				}else{
					productPrice.add(false);
				}
				if(new Boolean(xTable.getModel().getValueAt(j, 4).toString())){
					discPrice.add(true);
				}else{
					discPrice.add(false);
				}
			}
		}
	}
	
	/**
	 * Crea un vector con la información de los productos 
	 * de un cartel por orden de precio
	 * @return result Vector de productos
	 */
	public Vector<String> orderProducts(){
		Vector result = new Vector<String>();
		double priceTemp = 0.0;
		double price = 0.0;
		boolean add = false;
		int cont = 0;
		while(cont < productPoster.size() && productPoster.size()>0){
			System.out.println((productPoster.get(cont)).toString());
			priceTemp = Double.parseDouble((productPoster.get(cont)).toString().split("---")[0]);
			for(int i=0; i<result.size(); i++){
				price = Double.parseDouble((result.get(i)).toString().split("---")[0]);
				if(price>=priceTemp){
					result.add(i, productPoster.get(cont));
					add = true;
					break;
				}
			}
			if(!add){
				result.add(productPoster.get(cont));
			}
			add = false;
			cont++;
		}
		return result;
	}
	
	/**
	 * Función que verifica si un producto es nuevo 
	 * (Máximo consecutivo de precio = 1)
	 * @param cod value del producto a evaluar
	 * @return valor booleano
	 */
	public boolean isNewProduct(String cod){
		boolean isNew = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String product = "SELECT p.value, max(pc.xx_priceconsecutive) " +
				"FROM m_product p JOIN xx_vmr_priceconsecutive pc " +
				"ON (p.m_product_id = pc.m_product_id) " +
				"WHERE p.value = '"+cod+"' "+
				"GROUP BY p.value";

		try {
			pstmt = DB.prepareStatement(product, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if(rs.getInt(2) == 1){
					isNew = true;
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, product, e);
		}finally{
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
		return isNew;
	}
	
	/**
	 * Rutina que almacena en el vector productPoster los códigoas de los 
	 * productos correcpondientes al cartel que se va a crear
	 * @param option 
	 * 			1: Cartel no guandado
	 * 			2: Cartel en DB
	 */
	public synchronized void getProducts(int option){
		productPoster.removeAllElements();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		int cod = 0;
		int consecutive = 0;
		int cont = 0;
		String prd = "";
		String descTA = null;
		String[] productTmp = null;
		/*Cartel que no ha sido guardado*/
		if(option == 1){
			if(descTextArea.isEnabled()){
				descTA = descTextArea.getText();
			}
			if(isInPromotion()){
				for(int i = 0; i<productID.size(); i++){
					productTmp = ((String) productID.get(i)).split("-");
					if(productTmp[0].length() == 12){
						cod = Integer.parseInt(productTmp[0].substring(0, 9));
						consecutive = Integer.parseInt(productTmp[0].substring(9));
					}else{
						cod = Integer.parseInt(productTmp[0]);
					}
					if(Boolean.valueOf(productPrice.get(i).toString()) || opcion4Promo.isSelected()){
						prd = getPromoProduct(cod, consecutive, productTmp[1]);
						prd += ("---"+discPrice.get(i).toString())+"---"+descTA;
					}else{
						prd = getFullProducts(cod, consecutive, productTmp[1]);
						prd += "---false"+"---"+descTA;
					}
					productPoster.add(prd);
				}
			}else{
				if(opcion4Promo.isSelected()){
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorPromotional", new String[] {""}));
				}else{
					ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorPromotionalPrice", new String[] {""}));
				}
			}
		}else{
			/*Cartel existente en la base de datos*/
			KeyNamePair poster = (KeyNamePair)selectPosterCombo.getSelectedItem();
			sql = "SELECT pp.value, pp.SHOWDISC, pp.charactPrd, p.xx_vmr_promotional, pp.name " +
					"FROM xx_vmr_poster p JOIN xx_vmr_productposter pp " +
					"ON (p.xx_vmr_poster_id = pp.xx_vmr_poster_id) " +
					"JOIN m_product m ON (pp.m_product_id = m.m_product_id) " +
					"WHERE p.xx_vmr_poster_id = "+poster.getKey();
			
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					System.out.println(rs.getString(2)+"\n");
					productTmp = rs.getString(1).split("-");
					if(productTmp[0].length() == 12){
						cod = Integer.parseInt(productTmp[0].substring(0, 9));
						consecutive = Integer.parseInt(productTmp[0].substring(9));
					}else{
						cod = Integer.parseInt(productTmp[0]);
					}
					if(rs.getString(4).equals("Y") || rs.getString(2).equals("Y")){
						prd = getPromoProduct(cod, consecutive, rs.getString(5));
					}else{
						prd = getFullProducts(cod, consecutive, rs.getString(5));
					}
					prd += "---"+rs.getString(2);
					prd += "---"+rs.getString(3);
					productPoster.add(prd);
					cont++;
				}
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
	}
	
	/**
	 * Función que obtiene toda la información de un producto que 
	 * será impreso en el cartel con precio full 
	 * @param cod Value del producto
	 * @param consecutive Consecutio de precio asociado al producto
	 * @return prd String con información detallada del producto
	 */
	public String getFullProducts(int cod, int consecutive, String name){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String prd = "";
		
		String sql = "SELECT 0, m.m_product_id, m.description, pc.XX_saleprice "+
			"FROM M_PRODUCT m JOIN XX_VMR_PRICECONSECUTIVE pc ON (m.m_product_id = pc.m_product_id) " +
			"WHERE m.value ='"+cod+"' AND pc.XX_priceconsecutive = "+consecutive+" " +
			"UNION " +
			"select max(xx_vmr_PO_ProductDistrib_id), p.M_Product_ID, p.description, pd.XX_SALEPRICEPLUSTAX " +
			"from M_product p JOIN xx_vmr_PO_ProductDistrib pd " +
			"ON (p.m_product_id = pd.m_product_id) " +
			"where  pd.xx_isDefinitive = 'Y' and p.value = '"+cod+"' " +
			"group by  p.M_Product_ID, p.name, p.description, pd.xx_salepriceplustax";
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				prd = String.valueOf((rs.getBigDecimal(4).multiply(new BigDecimal(1.12))).setScale(2, RoundingMode.HALF_UP))+"---"+rs.getString(2)+"---"+name+"---"+rs.getString(3);
			}
		}catch (SQLException e) {
			e.printStackTrace();
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
		if(isNewProduct(String.valueOf(cod))){
			prd += "---"+String.valueOf(true);
		}else{
			prd += "---"+String.valueOf(false);
		}
		return prd;
		
	}
	
	/**
	 * Función que obtiene toda la información de un producto que 
	 * será impreso en el cartel con precio promocional 
	 * @param cod Value del producto
	 * @param consecutive Consecutio de precio asociado al producto
	 * @return prd String con información detallada del producto
	 */
	public String getPromoProduct(int cod, int consecutive, String name){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String prd = "";
		String sql = "";
		String sql0 = "";
		int promotion = 0;
		int prty = 0;

		/*sql0 = "SELECT MAX(p.codprom), 0 "+
			"FROM m_product m JOIN  XX_VMR_PromProdAS p ON (m.value = p.codProd) "+
			"WHERE m.value = '"+cod+"' and statusprom <>9 and p.finaldatep >= sysdate " +
			"GROUP BY m.m_product_ID " +
			"UNION " +
			"SELECT MAX(dp.XX_VMR_DETAILPROMOTIONEXT_ID), min(p.priority) from m_product mp "+
			"JOIN XX_VMR_DETAILPROMOTIONEXT dp ON (mp.xx_vmr_category_id = dp.xx_vmr_category_id  or " +
			"mp.XX_VMR_department_id = dp.XX_VMR_department_id or mp.XX_VMR_line_id = dp.XX_VMR_line_id " +
			"or mp.XX_VMR_section_id = dp.XX_VMR_section_id or mp.XX_VMR_vendorprodref_id = dp.XX_VMR_vendorprodref_id " +
			"or mp.XX_VMR_Brand_id = dp.XX_VMR_Brand_id) " +
			"JOIN XX_VMR_PROMOTION p ON (p.XX_VMR_PROMOTION_id = dp.XX_VMR_PROMOTION_id) "+
			"WHERE mp.value = '"+cod+"'  " + 
			"and ((dp.xx_discountrate <> 0 and XX_VMR_PRICECONSECUTIVE_ID is null) or " +
			"(dp.xx_discountrate = 0 and XX_VMR_PRICECONSECUTIVE_ID is not null)) " +
			"GROUP BY mp.m_product_ID";
		*/
		sql0 = " with detalleProducto as " +
				"( " +
				"SELECT MAX(dp.XX_VMR_DETAILPROMOTIONEXT_ID), p.priority " +
				"FROM m_product mp " +
				       "JOIN XX_VMR_DETAILPROMOTIONEXT dp " +
				          "ON (    mp.xx_vmr_category_id = dp.xx_vmr_category_id " +
				              "AND mp.XX_VMR_department_id = dp.XX_VMR_department_id " +
				              "AND dp.XX_VMR_line_id is null " +
				              "AND dp.XX_VMR_section_id is null " +
				              "AND dp.XX_VMR_vendorprodref_id is null " +
				              ") " +
				       "JOIN XX_VMR_PROMOTION p " +
				          "ON (p.XX_VMR_PROMOTION_id = dp.XX_VMR_PROMOTION_id) " +
				"WHERE      mp.value = '"+cod+"' and p.datefinish >= sysdate and p.IsActive = 'Y' " +
				"GROUP BY mp.m_product_ID, p.priority " +
		
				"UNION " +
		
				"SELECT MAX(dp.XX_VMR_DETAILPROMOTIONEXT_ID), p.priority " +
				  "FROM m_product mp " +
				       "JOIN XX_VMR_DETAILPROMOTIONEXT dp " +
				          "ON (    mp.xx_vmr_category_id = dp.xx_vmr_category_id " +
				              "AND mp.XX_VMR_department_id = dp.XX_VMR_department_id " +
				              "AND mp.XX_VMR_line_id = dp.XX_VMR_line_id " +
				              "AND dp.XX_VMR_section_id is null " +
				              "AND dp.XX_VMR_vendorprodref_id is null " +
				              ") " +
				       "JOIN XX_VMR_PROMOTION p " +
				          "ON (p.XX_VMR_PROMOTION_id = dp.XX_VMR_PROMOTION_id) " +
				"WHERE     mp.value = '"+cod+"' and p.datefinish >= sysdate and p.IsActive = 'Y' " +
				"GROUP BY mp.m_product_ID, p.priority " +
		
				"UNION " +
		
				"SELECT MAX(dp.XX_VMR_DETAILPROMOTIONEXT_ID), p.priority " +
				  "FROM m_product mp " +
				       "JOIN XX_VMR_DETAILPROMOTIONEXT dp " +
				          "ON (    mp.xx_vmr_category_id = dp.xx_vmr_category_id " +
				              "AND mp.XX_VMR_department_id = dp.XX_VMR_department_id " +
				              "AND mp.XX_VMR_line_id = dp.XX_VMR_line_id " +
				              "AND mp.XX_VMR_section_id = dp.XX_VMR_section_id " +
				              "AND dp.XX_VMR_vendorprodref_id is null " +
				              ") " +
				       "JOIN XX_VMR_PROMOTION p " +
				          "ON (p.XX_VMR_PROMOTION_id = dp.XX_VMR_PROMOTION_id) " +
				"WHERE     mp.value = '"+cod+"' and p.datefinish >= sysdate and p.IsActive = 'Y' " +
				"GROUP BY mp.m_product_ID, p.priority " +
		
				"UNION " +
		
				"SELECT MAX(dp.XX_VMR_DETAILPROMOTIONEXT_ID), p.priority " +
				  "FROM m_product mp " +
				       "JOIN XX_VMR_DETAILPROMOTIONEXT dp " +
				          "ON (    mp.xx_vmr_category_id = dp.xx_vmr_category_id " +
				              "AND mp.XX_VMR_department_id = dp.XX_VMR_department_id " +
				              "AND mp.XX_VMR_line_id = dp.XX_VMR_line_id " +
				              "AND mp.XX_VMR_section_id = dp.XX_VMR_section_id " +
				              "AND mp.XX_VMR_vendorprodref_id = dp.XX_VMR_vendorprodref_id " +
				              ") " +
				       "JOIN XX_VMR_PROMOTION p " +
				          "ON (p.XX_VMR_PROMOTION_id = dp.XX_VMR_PROMOTION_id) " +
				"WHERE     mp.value = '"+cod+"' and p.datefinish >= sysdate and p.IsActive = 'Y' " +
				"GROUP BY mp.m_product_ID, p.priority " +
				") " +
		
				"SELECT * FROM detalleProducto ORDER BY priority ASC ";
		

		try {
			pstmt = DB.prepareStatement(sql0, null);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				promotion = rs.getInt(1);
				prty = rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		if(prty != 0){
			/*sql = "select  pc.m_product_id, max(pc.xx_saleprice),dp.xx_discountrate||'---'|| " +
					"to_char(pr.datefrom, 'DD/MM/YY')||'---'||to_char(pr.datefinish,'DD/MM/YY') , " +
					"case " +
					"when dp.XX_vmr_priceconsecutive_id is null then max(pc.xx_saleprice)*(100-dp.xx_discountrate)/100 " +
					"else (select xx_saleprice from XX_vmr_priceconsecutive " +
					"where dp.XX_vmr_priceconsecutive_id = XX_vmr_priceconsecutive_id) " +
					"end as price " +
					"from m_product m join  XX_vmr_priceconsecutive pc on (m.m_product_id = pc.m_product_id), " +
					"xx_vmr_detailpromotionext dp join xx_vmr_promotion pr on (dp.xx_vmr_promotion_id = pr.xx_vmr_promotion_id) " +
					"where dp.xx_vmr_detailpromotionext_id = "+promotion+" and m.value = '"+cod+"' "+
					"group by pc.m_product_id, m.name, dp.XX_vmr_priceconsecutive_id, dp.xx_discountrate, pr.datefrom, pr.datefinish";*/
			
			sql = "SELECT pc.m_product_id," +
					      "max(pc.xx_saleprice), " +
					       "CASE " +
					           "WHEN dp.xx_discountrate > 0 " +
					            "THEN dp.xx_discountrate " +
					            "ELSE ROUND ((100 - ((dp.xx_discountAmount * 100)/ max(pc.xx_saleprice))), 2)  " +
					       "END " +
					       
					       "|| '---' " +
					       "|| to_char(pr.datefrom, 'DD/MM/YY') " +
					       "|| '---' " +
					       "|| to_char(pr.datefinish, 'DD/MM/YY'), " +
					       "CASE " +
					          "WHEN dp.XX_vmr_priceconsecutive_id IS NULL and dp.xx_discountrate > 0 " +
					          "THEN max(pc.xx_saleprice) * (100 - dp.xx_discountrate) / 100 " +
					          "WHEN dp.XX_vmr_priceconsecutive_id IS NULL and dp.xx_discountAmount > 0 " +
					          "THEN max(dp.xx_discountAmount) " +
					          "ELSE   " +
					            "max(dp.xx_discountAmount) " +
					       "END " +
					          "AS price " +
					  "FROM    m_product m " +
					       "JOIN " +
					          "XX_vmr_priceconsecutive pc " +
					       "ON (m.m_product_id = pc.m_product_id), " +
					          "xx_vmr_detailpromotionext dp " +
					       "JOIN " +
					          "xx_vmr_promotion pr " +
					       "ON (dp.xx_vmr_promotion_id = pr.xx_vmr_promotion_id) " +
					 "WHERE dp.xx_vmr_detailpromotionext_id = "+promotion+" AND m.value = '"+cod+"' " +
					"GROUP BY pc.m_product_id, " +
					         "m.name, " +
					         "dp.XX_vmr_priceconsecutive_id, " +
					         "dp.xx_discountrate, " +
					        "dp.xx_discountAmount, " +
					         "pr.datefrom, " +
					         "pr.datefinish ";
		}else{
				sql = "select  pc.m_product_id, pc.xx_saleprice, dp.discprom||'---'|| " +
					"to_char(dp.initialdate, 'DD/MM/YY')||'---'||to_char(dp.finaldatep,'DD/MM/YY') , "+
					"case "+
					"when dp.priceprom = 0 then max(pc.xx_saleprice)*(100-dp.discprom)/100 "+
					"else dp.priceprom "+ 
					"end as price "+
					"from m_product m join XX_VMR_promprodas dp on (m.value = dp.codprod) "+
					"join XX_vmr_priceconsecutive pc on (m.m_product_id = pc.m_product_id) "+
					"where dp.codprom = "+promotion+" and m.value = '"+cod+"' and pc.xx_priceconsecutive = "+consecutive+" "+
					"group by pc.m_product_id, m.name, pc.xx_saleprice, dp.discprom, dp.priceprom, dp.initialdate, dp.finaldatep";
		}
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				prd = String.valueOf((rs.getBigDecimal(4).multiply(new BigDecimal(1.12))).setScale(2, RoundingMode.HALF_UP))+
				"---"+rs.getString(1)+"---"+name+"---"+
				String.valueOf((rs.getBigDecimal(2).multiply(new BigDecimal(1.12))).setScale(2, RoundingMode.HALF_UP))+"---"+rs.getString(3);
			}		
		} catch (SQLException e) {
			e.printStackTrace();
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
		if(isNewProduct(String.valueOf(cod))){
			prd += "---"+String.valueOf(true);
		}else{
			prd += "---"+String.valueOf(false);
		}		
		return prd;
	}
	
	/*
	 * Es el método utilizado para crear la cabecera del cartel
	 * en la tabla XX_VMR_POSTER
	 **/
	public void InsertarPoster(){
		
		KeyNamePair posterType = (KeyNamePair)TypePosterCombo.getSelectedItem();
		String name = namePosterTextField.getText();
		String desc = descPosterTextField.getText();
		int idPoster = posterType.getKey();
		boolean allPromo = true;
		boolean promo = false; 
		allPromo = isInPromotion();
		if(opcion4Promo.isSelected()){
			promo = true;
		}
		if(name.equals("")){
			name = "Nuevo cartel";
		}
		if (allPromo){
			X_XX_VMR_POSTER cartel = new X_XX_VMR_POSTER(Env.getCtx(), 0, null);
			cartel.setName(name);
			cartel.setdescPoster(desc);
			cartel.setAD_Org_ID(org);
			cartel.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
			cartel.setposterWarehouse(warehouse);
			cartel.setposterLocator(locator);
			cartel.setXX_VMR_Promotional(promo);
			cartel.setXX_VMR_PTYPE_ID(idPoster);
			cartel.save();
			
			ProductsPoster(cartel.get_ID(), promo);
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_CreatedPoster", new String[] {""}));
		}else{
			if(opcion4Promo.isSelected()){
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorPromotional", new String[] {""}));
			}else{
				ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorPromotionalPrice", new String[] {""}));
			}
		}	 
		
	}
	
	/**
	 * Crea un registro en la tabla XX_VMR_ProductPoster por cada producto 
	 * asociado a un cartel que se está creando
	 * @param cartel ID del cartel recién creado
	 * @param promo Valor booleano que indica si el cartel es promocional  
	 */
	public void ProductsPoster(int cartel, boolean promo){
		String[] productTmp = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		int cod = 0;
		int consecutive = 0;
		
		X_XX_VMR_PRODUCTPOSTER product = null;
		
		for (int i=0; i < productID.size(); i++){
			productTmp = ((String) productID.get(i)).split("-");
			if(productTmp[0].length() == 12){
				cod = searchProduct(productTmp[0].substring(0, 9), productTmp[0].substring(9));
				consecutive = Integer.parseInt(productTmp[0].substring(9));
			}else{
				cod = searchProduct(productTmp[0], "");
			}
			X_XX_VMR_POSTER poster = new X_XX_VMR_POSTER(Env.getCtx(), cartel, null);
			sql = "SELECT m.m_product_id, m.name, m.description "+
			"FROM M_PRODUCT m "+
			"WHERE m.m_product_id ="+cod;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					product= new X_XX_VMR_PRODUCTPOSTER (Env.getCtx(), 0, null);
					product.setAD_Org_ID(poster.getAD_Org_ID());
					product.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
					product.setValue(productTmp[0]);
					product.setM_Product_ID(rs.getInt(1));
					product.setDescProd(rs.getString(2));
					product.setName(productTmp[1]);
					product.setXX_VMR_POSTER_ID(cartel);
					if(promo || Boolean.valueOf(productPrice.get(i).toString())){
						getCodPromotions(product, Integer.parseInt(productTmp[0].substring(0, 9)));
						product.setshowDisc(Boolean.valueOf(discPrice.get(i).toString()));
					}
					if(descTextArea.isEditable()){
						product.setcharactPrd(descTextArea.getText());
					}
					product.save();
				}
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
	}
	
	/**
	 * Crea un registro en la tabla XX_VMR_Poster, por cada cartel de formato libre guardado
	 */
	public void InsertarPosterVisual(){
		
		KeyNamePair posterType = (KeyNamePair)TypeVPosterCombo.getSelectedItem();
		String name = vNameTextField.getText();
		String header = vHeaderTextArea.getText();
		String content = vContentTextArea.getText();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String datef = "";
		String datet = "";
		int nro = 1;
		String promo = "";
		if(dateFromPromo.getValue()!=null && dateToPromo.getValue()!=null){
			datef = sdf.format((Date)dateFromPromo.getValue());
			datet = sdf.format((Date)dateToPromo.getValue());
			nro = Integer.parseInt(vQtyField.getText());
			promo = datef+"::"+datet+"::"+String.valueOf(nro);
		}
			
		int idPoster = posterType.getKey();
		
		if(name.equals("")){
			name = "Nuevo cartel formato libre";
		}
			X_XX_VMR_POSTER cartel = new X_XX_VMR_POSTER(Env.getCtx(), 0, null);
			cartel.setName(name);
			cartel.setdescPoster(saveContent(header)+"---"+saveContent(content)+"---"+promo);
			cartel.setAD_Org_ID(org);
			cartel.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
			cartel.setposterWarehouse(warehouse);
			cartel.setposterLocator(locator);
			cartel.setXX_VMR_PTYPE_ID(idPoster);
			cartel.save();
			
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_CreatedPoster", new String[] {""}));	 
		
	}
	
	/**
	 * 
	 * @param content Texto del campo Contenido del cartel
	 * @param option Opción de formato requerido
	 * 			1: Formato HTML para imprimir cartel actual
	 * 			2: Formato HTML para imprimir cartel guardado en bd
	 * @return result String con el formato solicitado
	 */
	public String getContent(String content, int option, int maxC, int linesC){
		String result = "";
		if(content.length()>maxC){
			content = content.substring(0, maxC);
		}
		int i = 0;
		String[] v = null;
		if (option == 1){
			v = content.split("\n");
			while(i<v.length && i<=linesC){
				result +=v[i]+"<br>";
				i++;
			}
		}else{
			v = content.split("::");
			while(i<v.length && i<=linesC){
				result +=v[i]+"<br>" ;
				i++;
			}	
		}
		return result;
	}
	
	/**
	 * Función que sirve para ajustar el texto ingresado por los usuarios al formato
	 * de Encabezado y Contenido
	 * @param content Texto al que se aplica el formato
	 * @return Contenido formateado
	 */
	public String saveContent(String content){
		String result = "";
		int i = 0;
		String[] v = null;
		v = content.split("\n");
		while(i<v.length){
			result +=v[i]+"::" ;
			i++;
		}	
		return result;
	}
	
	/**
	 * Método que verifica que existe al menos una promoción asociada 
	 * para cada producto de un cartel seleccionado como precio promocional
	 * @return result Valor booleano
	 */
	public boolean isInPromotion(){
		boolean result = true;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		int cod = 0;
	
		for (int i=0; i < productID.size(); i++){
			cod = Integer.parseInt(productID.get(i).toString().substring(0, 9));
			if (opcion4Promo.isSelected() || Boolean.getBoolean(productPrice.get(i).toString())){
				sql = "SELECT value "+
				"FROM M_PRODUCT "+
				"WHERE value in (SELECT DISTINCT m.value "+
				"from m_product m JOIN  XX_VMR_PromProdAS p ON (m.value = p.codProd) "+
				"WHERE m.value = '"+cod+"') OR value in (SELECT mp.value from m_product mp "+
				"JOIN XX_VMR_DETAILPROMOTIONEXT dp ON (mp.xx_vmr_category_id = dp.xx_vmr_category_id)  "+
				"WHERE mp.value = '"+cod+"')";
				try {
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();
					if(!rs.next()) {
						result = false;
						break;
					}
				} catch (SQLException e) {
				e.printStackTrace();
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
		}
		return result;
	}
	
	/**
	 * Método que obtiene el código de promoción de mayor prioridad para el producto product
	 * @param product Objeto de tipo ProductPoster (producto en cartel)
	 * @param cod Value del producto
	 */
	public void getCodPromotions(X_XX_VMR_PRODUCTPOSTER product, int cod){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql1 = "";
		
		sql1 = "SELECT MAX(p.codprom), 0 "+
			"FROM m_product m JOIN  XX_VMR_PromProdAS p ON (m.value = p.codProd) "+
			"WHERE m.value = '"+cod+"' and statusprom <>9 and p.finaldatep >= sysdate " +
			"GROUP BY m.m_product_ID " +
			"UNION " +
			"SELECT MAX(dp.XX_VMR_DETAILPROMOTIONEXT_ID), min(p.priority) from m_product mp "+
			"JOIN XX_VMR_DETAILPROMOTIONEXT dp ON (mp.xx_vmr_category_id = dp.xx_vmr_category_id)  " +
			"JOIN XX_VMR_PROMOTION p ON (p.XX_VMR_PROMOTION_id = dp.XX_VMR_PROMOTION_id) "+
			"WHERE mp.value = '"+cod+"' " +
			"and p.datefinish >= sysdate " +
			"GROUP BY mp.m_product_ID";
		try {
			pstmt = DB.prepareStatement(sql1, null);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				product.setcodProm(rs.getInt(1));
				product.setPriorityProm(rs.getInt(2));
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	 * 
	 * @param typePoster Nombre del tipo de cartel seleccionado
	 * @param numProd Número de productos asociados al cratel
	 * @param option Tipo de ación a realizar
	 * 			1: Crear cartel
	 * 			2: Mostrar vista previa del cartel
	 * 			3: Imprimir cartel sin guardar
	 * 			4: Imprimir cartel creado
	 */
	public boolean verifyTypePoster(String typePoster, int numProd, int option){
		
		boolean print = false;
		
		if(typePoster.equals("Elisa /Jota")){
			if(numProd == 1){
				//createElisa();
				print = true;
			}
		}else if(typePoster.equals("Cubo único producto")){
			if(numProd == 1){
				//createCuboUnico();
				print = true;
			}
		}else if(typePoster.equals("Base Plana único")){
			if(numProd == 1){
				//createBasePlanaUnico();
				print = true;
			}
		}else if(typePoster.equals("Base Plana Familia de Productos de Toallas")){
			if(numProd <=5 && numProd >=4){
				//createBasePlanaT();
				print = true;
			}
		}else if(typePoster.equals("Display / Tarimas")){
			if(numProd <=10 && numProd >=3){
				//createVitrinas();
				print = true;
			}
		}else if(typePoster.equals("Cubo")){
			if(numProd <=2 && numProd >=1){
				//createCubo();
				print = true;
			}
		}else if(typePoster.equals("Cubo Colgado")){
			if(numProd <=2 && numProd >=1){
				//createCubo();
				print = true;
			}
		}else if(typePoster.equals("Base Plana")){
			if(numProd <=2 && numProd >=1){
				//createBasePlana();
				print = true;
			}
		}else if(typePoster.equals("Base Plana Familia de Productos de Cama")){
			if(numProd <=5 && numProd >=4){
				//createBasePlanaC();
				print = true;
			}
		}else {
			if(numProd <=10 && numProd >=3){
				//createVitrinas();
				print = true;
			}
		}
		if(print){
			if(option == 1){
				InsertarPoster();
			}else{
				printPoster(typePoster);
			}
		}else{
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_NumberOfProducts", new String[] {""}));
		}
		return print;
		
	}
	
	/**
	 * Método genera la vista previa con opción a impresión de los carteles
	 * @param typePoster Tipo de cartel a imprimir
	 */
	public void printPoster(String typePoster){
		
		if(typePoster.equals("Elisa /Jota")){
				createElisa();
		}else if(typePoster.equals("Cubo único producto")){
				createCuboUnico();
		}else if(typePoster.equals("Base Plana único")){
				createBasePlanaUnico();
		}else if(typePoster.equals("Base Plana Familia de Productos de Toallas")){
				createBasePlanaT();
		}else if(typePoster.equals("Display / Tarimas")){
				createVitrinas();
		}else if(typePoster.equals("Cubo")){
				createCubo();
		}else if(typePoster.equals("Cubo Colgado")){
				createCubo();
		}else if(typePoster.equals("Base Plana")){
				createBasePlana();
		}else if(typePoster.equals("Base Plana Familia de Productos de Cama")){
				createBasePlanaC();
		}else {
			createVitrinas();
		}
	}
		
	
	/**Crea el Frame de vista previa de los carteles**/
	public synchronized JFrame getPreview() {
		JFrame jFrame = new JFrame();
		JPanel southp= new JPanel();
		JPanel mainp= new JPanel();
		ScrollPaneLayout cLayout = new ScrollPaneLayout();
		//JLabel labelTabla = new JLabel();
		JScrollPane sp = new JScrollPane();
		labelTabla.setText(htmlcod); 
		System.out.println(labelTabla.getText());
		mainp.setBackground(Color.white);
		FlowLayout printLayout = new FlowLayout();
		
		southp.add(bPrint);
		southp.setLayout(printLayout);
		jFrame.setTitle("Vista preliminar");
		mainp.add(labelTabla,  null);
		sp.setLayout(cLayout);
		sp.getViewport().add(mainp, null);

		jFrame.getContentPane().add(sp, BorderLayout.CENTER);
		jFrame.getContentPane().add(southp, BorderLayout.SOUTH);
		
		jFrame.setMenuBar(null);
		jFrame.setSize(850, 850);
		jFrame.setResizable(false);
		return jFrame;
	}

	/**Crea el Frame que muestra la ubicación sugerida de los carteles
	 * según su tipo
	 */
	public void getHelp() {
		KeyNamePair posterType = (KeyNamePair)TypePosterCombo.getSelectedItem();
		JFrame jFrame;
		jFrame = new JFrame();
		ImageIcon img = new ImageIcon();
		JScrollPane sp = new JScrollPane();
		jFrame.getContentPane().add(sp, BorderLayout.CENTER);
		if(posterType.getName().equals("Elisa /Jota")){
			img = new ImageIcon(getClass().getResource("Dibujo Elisa y jota.jpg"));
		}else if(posterType.getName().contains("Cubo")){
			img = new ImageIcon(getClass().getResource("Dibujo Cubo.jpg"));
		}else if(posterType.getName().equals("Display / Tarimas") || posterType.getName().equals("Vitrinas")){
			img = new ImageIcon(getClass().getResource("Dibujo Display.jpg"));
		}else if(posterType.getName().contains("Base Plana")){
			img = new ImageIcon(getClass().getResource("Dibujo Base Plana.jpg"));
		}
		
		JLabel imageLabel = new JLabel(img);
		sp.getViewport().add(imageLabel, null);
		jFrame.setMenuBar(null);
		jFrame.setTitle("Ubicación Sugerida");
		jFrame.setSize(img.getIconWidth(), img.getIconHeight());
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );

	}
	
	/**
	 * Crea el formato HTML de los carteles de tipo Elisa/Jota
	 */
	public void createElisa() {
		
		String price = "";
		String name = "";
		String prdDesc = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		String discount = "";
		int productQty = 0;
		String newPrd = "";
		String[] temp;
		Vector products = orderProducts();
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out; 
		
		KeyNamePair numPoster = (KeyNamePair) qtyPosterCombo.getSelectedItem();
		htmlcod = "<html><head><title>Formato Actualizado Precio BECO</title><style type=text/css>" +
		".a{color:#000000;text-decoration:none;padding-left: 0.6cm;font-family: \"Arial\";font-size: 13pt;}" +
		".b{color:#000000;text-decoration:none;padding-left: 0.6cm;font-family: \"Arial\";font-size: 16pt;}" +
		".c{color:#000000;text-decoration:none;padding-left: 0.6cm; padding-bottom: 10px;font-family: \"Arial\";font-weight: 600;font-size: 15pt;Text-align:right;}" +
		".d{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.6cm;font-family: \"Arial\";font-size: 5pt;Text-align:center;}" +
		".e{color:#000000;text-decoration:none;padding-right: 0.6cm;font-family: \"Arial\";font-weight: 600;font-size: 42pt;}" +
		".f{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.6cm;font-family: \"Arial\";font-size: 10pt;}" +
		".g{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.6cm;font-family: \"Arial\";font-size: 7pt;}" +
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head><body onload='salir()'>" +
		"<table style = 'border:2px solid black;width:8.5cm; height:4cm;text-wrap:unrestricted; table-layout=fixed;'>"+
		"<tr><td><table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
		for (int i = 0; i<products.size(); i++){
			temp = products.get(i).toString().split("---");
			name = temp[2];
			if(temp.length == 6 || temp.length == 7){
				price = temp[0];
				if(temp[3].equals("null")){
					prdDesc = "";
				}else{
					prdDesc = temp[3];
				}
				newPrd = temp[4];
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				//htmlcod +="<tr><td width=100% align=\"left\" class=b colspan=2>" + name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+ "</td></tr>" ;
				htmlcod +="<tr><td width=100% align=\"left\" class=b colspan=2>" + name+ "</td></tr>" ;
				htmlcod += "<tr><td width=100% align=\"left\" class=a colspan=2 >" + prdDesc + "</td></tr>" +
				"<tr><td width=50% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td><td width=50% align=\"right\" class=e colspan=1>" + price + "</td></tr>";
			}else{
				
				price = temp[0];
				fullPrice = temp[3].replaceAll(",", ".");
				dateFrom = temp[5];
				dateTo = temp[6];
				newPrd = temp[7];
				productQty = qtyProduct(Integer.valueOf(temp[1]));
				prdDesc = "Antes: Bs. ";
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				if (temp[8].equals("Y") && !temp[4].equals("0")){
					discount = "Descuento "+temp[4]+"%. ";
				}
				if(productQty > 0){
					//htmlcod +="<tr><td width=100% align=\"left\" class=b colspan=2>" +  name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+ "</td></tr>" ;
					htmlcod +="<tr><td width=100% align=\"left\" class=b colspan=2>" +  name+ "</td></tr>" ;
					htmlcod +="<tr><td width=100% align=\"left\" class=a colspan=2 >" +discount+prdDesc + fullPrice +"</td></tr>"+
					"<tr><td width=50% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td><td width=50% align=\"right\" class=e colspan=1>" + price + "</td></tr>"+
					"<tr><td class=d width=100% colspan=2 align=\"center\">Promoci&oacute;n autorizada por el INDEPABIS. " +
					"V&aacute;lida del "+dateFrom+" al "+dateTo+". "+productQty+" pzas.</td></tr>";
				}
			}
		}
		htmlcod += "<tr><td width=100% class=d colspan=2 align=\"center\">CENTROBECO C.A. • RIF: J-00046517-7 • Todos nuestros precios incluyen I.V.A.</td></tr>" +
			"</table></td></tr></table></body></html>";
		
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcod);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene el tipo de cartel asociado a un poster
	 * @param idPoster Código del poster
	 * @return result: String con el tipo del poster ingresado
	 */
	public int getTypePoster(int idPoster){
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		sql = "SELECT COUNT(pp.xx_vmr_productposter_id) " +
				"FROM xx_vmr_poster p JOIN xx_vmr_productposter pp " +
				"ON (p.xx_vmr_poster_id = pp.xx_vmr_poster_id) " +
				"WHERE p.xx_vmr_poster_id = "+idPoster;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Calcula el número de piezas disponibles en tienda (sumatoria de todas las tiendas) de un producto
	 * @param prdId Identificador de un producto
	 * @return qty Número de piezas disponibles del producto en tienda
	 */
	public int qtyProduct(int prdId){
		int qty = 0;
		String sql = "SELECT SUM(s.qty) FROM M_storageDetail s " +
				"JOIN m_product p ON (p.m_product_id = s.m_product_id) " +
				"JOIN m_locator ml ON (s.m_locator_id = ml.m_locator_id) " +
				"WHERE s.m_product_id = '"+prdId+"' AND s.qtytype = 'H' " +
				"AND ml.value LIKE '%EN TIENDA%' " +
				"GROUP BY s.m_product_id";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				qty += rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return qty;
	}
	
	/**
	 * Crea el formato HTML de los carteles de tipo Cubo
	 */
	public void createCubo(){
		String price = "";
		String name = "";
		String prdDesc = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		String discount = "";
		int productQty = 0;
		String newPrd = "";
		String[] temp;
		Vector products = orderProducts();
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		int i = 0;
		htmlcod =	"<html><head>"+
		"<title>Formato Actualizado Precio BECO</title>"+
		"<style type=text/css>"+
		".a{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-top: 0.3cm;padding-right: 0.6cm;font-family: \"Arial\";min-font-size: 30pt;font-size: 32pt;line-height: 30pt;}"+
		".b{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.7m;font-family: \"Arial\";font-size: 13pt;line-height: 16pt;}"+
		".c{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-bottom: 0.3cm;font-family: \"Arial\";font-weight: 600;font-size: 14pt;line-height: 16pt;Text-align:right;}"+
		".d{color:#000000;text-decoration:none;padding-right: 0.6cm;font-family: \"Arial\";font-weight: 600;font-size: 52pt;line-height: 60pt;}"+
		".e{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.6cm;padding-bottom: 0.3cm;padding-top: 0.1cm;font-family: \"Arial\";font-size: 7pt;line-height: 15pt;}"+
        ".f{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.6cm;padding-bottom: 0.1cm;font-family: \"Arial\";font-size: 5pt;}"+
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head><body onload='salir()'>"+
		"<table style='border:2px solid black;width:10.5cm;height:11.5cm;text-wrap:unrestricted; table-layout=fixed;'>"+
		"<tr>"+
		"<td style=\" padding:0; vertical-align:center;\">"+
		"<table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
			
			for(i = 0; i<products.size(); i++){
				temp = products.get(i).toString().split("---");
				name = temp[2];
				if(temp.length == 6 || temp.length == 7){
					price = temp[0];
					newPrd = temp[4];
					
					if(temp[3].equals("null")){
						prdDesc = "";
					}else{
						prdDesc = temp[3];
					}
					if(Boolean.getBoolean(newPrd)){
						prdDesc = "Nuevo. "+prdDesc;
					}
					//htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=2>"+ name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>"+
					htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=2>"+ name+"</td></tr>"+
					"<tr><td align=\"left\" width=150% class=b colspan=2> "+prdDesc+"</td></tr>"+
					"<tr><td width=50% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td>"+
					"<td width=50% align=\"right\"  class=d colspan=1>"+price+"</td></tr>";
				}else{
					price = temp[0];
					name = temp[2];
					fullPrice = temp[3].replaceAll(",", ".");
					dateFrom = temp[5];
					dateTo = temp[6];
					newPrd = temp[7];
					productQty = qtyProduct(Integer.valueOf(temp[1]));
					prdDesc = "Antes: Bs. ";
					if(Boolean.getBoolean(newPrd)){
						prdDesc = "Nuevo. "+prdDesc;
					}
					if (temp[8].equals("Y") && !temp[4].equals("0")){
						discount = "Descuento "+temp[4]+"%. ";
					}
					if(productQty > 0){
						//htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=2>"+ name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>";
						htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=2>"+ name+"</td></tr>";
						htmlcod +="<tr><td width=150% align=\"left\" class=b colspan=2 >" +discount+ prdDesc + fullPrice +"</td></tr>"+
						"<tr><td width=50% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td>" +
						"<td width=50% align=\"right\" class=d colspan=1>" + price+ "</td></tr>"+
						"<tr><td class=f colspan=2 align=\"center\" valign=top>Promoci&oacute;n autorizada por el INDEPABIS. " +
						"V&aacute;lida del "+dateFrom+" al "+dateTo+". Piezas disponibles: "+productQty+"</td></tr>";
					}
				}
			}

			htmlcod += "<tr><td width=100% class=e colspan=2 align=\"center\">CENTROBECO C.A. • RIF: J-00046517-7 • Todos nuestros precios incluyen I.V.A.</td></tr>" ;
			htmlcod += "</table></td></tr></table></body></html>";
			
			try {
				out = new PrintWriter(new FileWriter(doc));
				out.println(htmlcod);
				out.close();
				BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Imprime las características de un producto con un formato 
	 * definido según el cartel
	 * @param charact Texto que contiene las características del producto
	 * @return html Texto en formato HTML para formar el cartel
	 */
	public String getCharacteristics(String charact){
		String html = "";
		int i = 0;
		String[] lines = charact.split("\n");
		
		while(i<5){
			html += "<tr><td align=\"left\" width=150% class=b colspan=2>" ;
			if(i<lines.length){
				html +=	"• "+lines[i] ;
			}else{
				html +=	" " ;
			}
			html += "</td></tr>";
			i++;
		}
		return html;
	}
	
	/**
	 * Crea el formato HTML de los carteles de tipo Cubo de Único producto
	 */
	public void createCuboUnico(){
		String price = "";
		String name = "";
		String charact = "";
		String newPrd = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		int productQty = 0;
		String prdDesc = "";
		String discount = "";
		String[] temp;
		Vector products = orderProducts();
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		temp = products.get(0).toString().split("---");
		price = temp[0];
		name = temp[2];
		if(option == OPCION1 && descTextArea.isEditable() && descTextArea.getText().equals("")){
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorDescPoster", new String[] {""}));
		}else{
			htmlcod =	"<html><head>"+
			"<title>Formato Actualizado Precio BECO</title>"+
			"<style type=text/css>"+
			".a{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-top: 0.3cm;padding-right: 0.6cm;font-family: \"Arial\";min-font-size: 30pt;font-size: 32pt;line-height: 30pt;}"+
			".b{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.5m;font-family: \"Arial\";font-size: 13pt;line-height: 16pt;}"+
			".c{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-bottom: 0.3cm;font-family: \"Arial\";font-weight: 600;font-size: 20pt;line-height: 22pt;Text-align:right;}"+
			".d{color:#000000;text-decoration:none;padding-right: 0.6cm;font-family: \"Arial\";font-weight: 600;font-size: 52pt;line-height: 60pt;}"+
			".e{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.6cm;padding-bottom: 0.3cm;padding-top: 0.1cm;font-family: \"Arial\";font-size: 7pt;line-height: 15pt;Text-align:center;}"+
			"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head><body onload='salir()'>"+
			"<table style='border:2px solid black;width:10.5cm;height:11.5cm; text-wrap:unrestricted; table-layout=fixed;'>"+
			"<tr>"+
			"<td style=\" padding:0; vertical-align:center;\">"+
			"<table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
			if(temp.length == 7){
				newPrd = temp[4];
				charact = temp[6];
				
				//"<tr><td width=150% align=\"left\" class=a colspan=2>"+name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>";
				htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=2>"+name+"</td></tr>";
				if(Boolean.getBoolean(newPrd)){
					htmlcod += "<tr><td align=\"left\" width=150% class=b colspan=2>Nuevo.</td></tr>";
				}else{
					htmlcod +="<tr><td align=\"left\" width=150% class=b colspan=2></td></tr>";
				}
				htmlcod += getCharacteristics(charact);
				htmlcod +="<tr><td width=70% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td>"+
				"<td width=80% align=\"right\"  class=d colspan=1>"+price+"</td>"+
				"</tr>";
			}else{
				
				price = temp[0];
				fullPrice = temp[3].replaceAll(",", ".");
				dateFrom = temp[5];
				dateTo = temp[6];
				newPrd = temp[7];
				charact = temp[9];
				productQty = qtyProduct(Integer.valueOf(temp[1]));
				prdDesc = "Antes: Bs. ";
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				if (temp[8].equals("Y") && !temp[4].equals("0")){
					discount = "Descuento "+temp[4]+"%. ";
				}
				if(productQty > 0){
					htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=2>"+name+"</td></tr>";
					htmlcod +="<tr><td align=\"left\" width=150% class=b colspan=2>"+discount+prdDesc + fullPrice +"</td></tr>" ;

					htmlcod += getCharacteristics(charact);
					htmlcod +="<tr><td width=70% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td>"+
					"<td width=80% align=\"right\"  class=d colspan=1>"+price+"</td>"+
					"</tr>"+
					"<tr><td class=e width=100% colspan=2 align=\"center\">Promoci&oacute;n autorizada por el INDEPABIS. " +
					"V&aacute;lida del "+dateFrom+" al "+dateTo+". Piezas disponibles: "+productQty+"</td></tr>";
				}
			}
			htmlcod +="<tr><td width=150% class=e colspan=2 align=\"center\">Precio Incluye I.V.A. • Rif J-00046517-7 • CENTROBECO C.A.</td></tr>"+
			"</table></td></tr></table></body></html>";
			
			try {
				out = new PrintWriter(new FileWriter(doc));
				out.println(htmlcod);
				out.close();
				BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Crea el formato HTML de los carteles de tipo Base Plana
	 */
	public void createBasePlana() {
		String price = "";
		String name = "";
		String prdDesc = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		String discount = "";
		int productQty = 0;
		int i = 0;
		String newPrd = "";
		String[] temp;
		Vector products = orderProducts();
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		htmlcod = "<html><head><title>Formato Actualizado Precio BECO</title><style type=text/css>" +
		".a{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-top: 0.4cm;padding-right: 0.6cm;font-family: \"Arial\";min-font-size: 38pt;font-size: 40pt;line-height: 38pt;}" +
		".b{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.6cm;font-family: \"Arial\";font-size: 16pt;;line-height: 0.9cm;}" +
		".c{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-bottom: 0.4cm;font-family: \"Arial\";font-weight: 600;font-size: 24pt;Text-align:right;}" +
		".d{color:#000000;text-decoration:none;padding-right: 0.6cm;font-family: \"Arial\";font-weight: 600;font-size: 64pt;}" +
		".e{color:#000000;text-decoration:none;padding-bottom: 0.4cm;padding-top: 0.4cm;font-family: \"Arial\";font-size: 9pt;Text-align:center;}" +
		".f{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-bottom: 0.1cm;font-family: \"Arial\";font-size: 7pt;Text-align:center;}" +
		".g{color:#000000;text-decoration:none;padding-right: 20px;font-family: \"Arial\";font-size: 7pt;}" +
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head>" +
		"<body onload='salir()'><table style='border:2px solid black;width:18cm;height:13.5cm; text-wrap:unrestricted; table-layout=fixed;' >" +
		"<tr><td style=\" padding:0; vertical-align:center;\">" +
		"<table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
		
		for(i = 0; i<products.size(); i++){
			temp = products.get(i).toString().split("---");
			
			name = temp[2];
			
			if(temp.length == 6 || temp.length == 7){
				price = temp[0];
				if(temp[3].equals("null")){
					prdDesc = "";
				}else{
					prdDesc = temp[3];
				}
				newPrd = temp[4];
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				//htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=2>"+name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>";
				htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=2>"+name+"</td></tr>";
				htmlcod +="<tr><td width=150% align=\"left\" class=b colspan=2>"+prdDesc+"</td></tr>"+
				"<tr><td width=70% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td><td width=80% align=\"right\" " +
				"class=d colspan=1>" + price + "</td></tr>" ;
			}else{
				price = temp[0];
				fullPrice = temp[3].replaceAll(",", ".");
				dateFrom = temp[5];
				dateTo = temp[6];
				newPrd = temp[7];
				productQty = qtyProduct(Integer.valueOf(temp[1]));
				prdDesc = "Antes: Bs. ";
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				if (temp[8].equals("Y") && !temp[4].equals("0")){
					discount = "Descuento "+temp[4]+"%. ";
				}
				if(productQty > 0){
					//htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=2>"+name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>"+
					htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=2>"+name+"</td></tr>"+
					"<tr><td width=150% align=\"left\" class=b colspan=2>"+discount+prdDesc+fullPrice+"</td></tr>"+
					"<tr><td width=70% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td><td width=80% align=\"right\" " +
					"class=d colspan=1>"+price+"</td></tr>" ;
					htmlcod += "<tr><td  class=f width=150%  colspan=2 align=\"center\" valign=top>Promoci&oacute;n autorizada por el INDEPABIS. " +
					"V&aacute;lida del "+dateFrom+" al "+dateTo+". Piezas disponibles: "+productQty+"</td></tr>";
				}
			}
		}

		htmlcod +="<tr><td width=150% class=e colspan=2 align=\"center\">CENTROBECO C.A. • RIF: J- 00046517-7 • Todos nuestros precios incluyen I.V.A.</td></tr>";
		htmlcod += "</table></td></tr></table></body></html>";
		
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcod);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Crea el formato HTML de los carteles de tipo Base plana
	 * (Productos de Cama)
	 */
	public void createBasePlanaC(){
		String price = "";
		String name = "";
		String prdDesc = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		String discount = "";
		int productQty = 0;
		int i = 0;
		String newPrd = "";
		String[] temp;
		Vector products = orderProducts();	
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		htmlcod = "<html>" +
			"<head>" +
			"<title>Formato Actualizado Precio BECO</title>" +
			"<style type=text/css>" +
			".a{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-top: 0.4cm;padding-right: 0.6cm;font-family: \"Arial\";min-font-size: 18pt;font-size: 20pt;line-height: 24pt;}" +
			".b{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.6cm;font-family: \"Arial\";font-size: 16pt;line-height: 16pt;}" +
			".c{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.2cm;font-family: \"Arial\";font-weight: 600;font-size: 17pt;}" +
			".d{color:#000000;text-decoration:none;padding-right: 0.6cm;font-family: \"Arial\";font-weight: 600;font-size: 40pt;line-height: 44pt;}" +
			".e{color:#000000;text-decoration:none;padding-left: 0.5cm;padding-right: 0.5cm;padding-bottom: 0.5cm;padding-top: 0.7cm;font-family: \"Arial\";font-size: 9pt;Text-align:center;}" +
            ".f{color:#000000;text-decoration:none;padding-left: 0.5cm;padding-right: 0.5cm;padding-bottom: 0.1cm;padding-top: 0.1cm;font-family: \"Arial\";font-size: 7pt;Text-align:center;}"+
			"</style><script language=JavaScript>function salir() {self.print();self.close();}</script>" +
			"</head>"+
			"<body onload='salir()'><table style='border:2px solid black;width:18cm;height:13.5cm;text-wrap:unrestricted; table-layout=fixed;' " +
			"<tr><td style=\" padding:0; vertical-align:center;\"><table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
			for(i = 0; i<products.size(); i++){
				temp = products.get(i).toString().split("---");
				name = temp[2];
				if(temp.length == 6 || temp.length == 7){
					price = temp[0];
					if(temp[3].equals("null")){
						prdDesc = "";
					}else{
						prdDesc = temp[3];
					}
					newPrd = temp[4];
					if(Boolean.getBoolean(newPrd)){
						prdDesc = "Nuevo. "+prdDesc;
					}
					
					//htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=3>" +name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+ "<td></tr>"+
					htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=3>" +name+ "<td></tr>"+
					"<tr><td>&nbsp;</td></tr>"+
					"<tr><td width=70% align=\"left\" class=b colspan=1>"+ prdDesc +"</td>" +
					"<td width=20% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td>"+
					"<td width=60% align=\"right\"  class=d colspan=1>" + price + "</td></tr>";
				}else{
					price = temp[0];
					fullPrice = temp[3].replaceAll(",", ".");
					dateFrom = temp[5];
					dateTo = temp[6];
					newPrd = temp[7];
					productQty = qtyProduct(Integer.valueOf(temp[1]));
					prdDesc = "Antes: Bs. ";
					if(Boolean.getBoolean(newPrd)){
						prdDesc = "Nuevo. "+prdDesc;
					}
					if (temp[8].equals("Y") && !temp[4].equals("0")){
						discount = "Descuento "+temp[4]+"%. ";
					}
					if(productQty > 0){
						//htmlcod += "<tr><td width=150% align=\"left\" class=b colspan=3>"+ name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>" +
						htmlcod += "<tr><td width=150% align=\"left\" class=b colspan=3>"+ name+"</td></tr>" +
						"<tr><td width=70% align=\"left\" class=b colspan=1>"+ discount+prdDesc + fullPrice +"</td>" +
						"<td width=20% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td>"+
						"<td width=60% align=\"right\"  class=d colspan=1>" + price +  "</td></tr>"+
						"<tr><td class=f colspan=3 align=\"center\">Promoción Autorizada por el " +
						"INDEPABIS. Válida desde el "+dateFrom+" al "+dateTo+". Piezas disponibles: "+productQty+".</td></tr>";
					}
				}
			}	

			htmlcod += "<tr><td width=150% class=e colspan=3 align=\"center\">CENTROBECO C.A. • RIF: J- 00046517-7 • Todos nuestros precios incluyen I.V.A.</td></tr>";
			htmlcod += "</table></td></tr></table></body></html>";
			
			try {
				out = new PrintWriter(new FileWriter(doc));
				out.println(htmlcod);
				out.close();
				BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	
	/**
	 * Crea el formato HTML de los carteles de tipo Base Plana 
	 * (Toallas)
	 */
	public void createBasePlanaT(){
		String price = "";
		String name = "";
		String prdDesc = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		String discount = "";
		int productQty = 0;
		int i = 0;
		String newPrd = "";
		String[] temp;
		Vector products = orderProducts();	
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		htmlcod = "<html>" +
			"<head>" +
			"<title>Formato Actualizado Precio BECO</title>" +
			"<style type=text/css>" +
			".a{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-top: 0.4cm;padding-right: 0.6cm;font-family: \"Arial\";min-font-size: 18pt;font-size: 20pt;line-height: 24pt;}" +
			".b{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.6cm;font-family: \"Arial\";font-size: 16pt;line-height: 16pt;}" +
			".c{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.2cm;font-family: \"Arial\";font-weight: 600;font-size: 17pt;}" +
			".d{color:#000000;text-decoration:none;padding-right: 0.6cm;font-family: \"Arial\";font-weight: 600;font-size: 30pt;line-height: 34pt;}" +
			".e{color:#000000;text-decoration:none;padding-left: 0.5cm;padding-right: 0.5cm;padding-bottom: 0.5cm;padding-top: 0.7cm;font-family: \"Arial\";font-size: 9pt;Text-align:center;}" +
            ".f{color:#000000;text-decoration:none;padding-left: 0.5cm;padding-right: 0.5cm;padding-bottom: 0.1cm;padding-top: 0.1cm;font-family: \"Arial\";font-size: 7pt;Text-align:center;}"+
			"</style><script language=JavaScript>function salir() {self.print();self.close();}</script>" +
			"</head>"+
			"<body onload='salir()'><table style='border:2px solid black;width:18cm;height:13.5cm;text-wrap:unrestricted; table-layout=fixed;'>" +
			"<tr><td style=\" padding:0; vertical-align:center;\"><table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
			for(i = 0; i<products.size(); i++){
				temp = products.get(i).toString().split("---");
				name = temp[2];
				if(temp.length == 6 || temp.length == 7){
					price = temp[0];
					if(temp[3].equals("null")){
						prdDesc = "";
					}else{
						prdDesc = temp[3];
					}
					newPrd = temp[4];
					if(Boolean.getBoolean(newPrd)){
						prdDesc = "Nuevo. "+prdDesc;
					}
					
					//htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=3>" +name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+ "<td></tr>"+
					htmlcod += "<tr><td width=150% align=\"left\" class=a colspan=3>" +name+ "<td></tr>"+
					"<tr><td>&nbsp;</td></tr>"+
					"<tr><td width=70% align=\"left\" class=b colspan=1>"+ prdDesc +"</td>" +
					"<td width=20% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td>"+
					"<td width=60% align=\"right\"  class=d colspan=1>" + price + "</td></tr>";
				}else{
					price = temp[0];
					fullPrice = temp[3].replaceAll(",", ".");
					dateFrom = temp[5];
					dateTo = temp[6];
					newPrd = temp[7];
					productQty = qtyProduct(Integer.valueOf(temp[1]));
					prdDesc = "Antes: Bs. ";
					if(Boolean.getBoolean(newPrd)){
						prdDesc = "Nuevo. "+prdDesc;
					}
					if (temp[8].equals("Y") && !temp[4].equals("0")){
						discount = "Descuento "+temp[4]+"%. ";
					}
					if(productQty > 0){
						//htmlcod += "<tr><td width=150% align=\"left\" class=b colspan=3>"+ name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>" +
						htmlcod += "<tr><td width=150% align=\"left\" class=b colspan=3>"+ name+"</td></tr>" +
						"<tr><td width=70% align=\"left\" class=b colspan=1>"+ discount+prdDesc + fullPrice +"</td>" +
						"<td width=20% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td>"+
						"<td width=60% align=\"right\"  class=d colspan=1>" + price +  "</td></tr>"+
						"<tr><td class=f colspan=3 align=\"center\">Promoción Autorizada por el " +
						"INDEPABIS. Válida desde el "+dateFrom+" al "+dateTo+". Piezas disponibles: "+productQty+".</td></tr>";
					}
				}
			}	

			htmlcod += "<tr><td width=150% class=e colspan=3 align=\"center\">CENTROBECO C.A. • RIF: J- 00046517-7 • Todos nuestros precios incluyen I.V.A.</td></tr>";
			htmlcod += "</table></td></tr></table></body></html>";
			
			try {
				out = new PrintWriter(new FileWriter(doc));
				out.println(htmlcod);
				out.close();
				BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Crea el formato HTML de los carteles de tipo Base Plana Único producto
	 */
	public void createBasePlanaUnico() {
		String price = "";
		String name = "";
		String charact = "";
		String newPrd = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		int productQty = 0;
		String prdDesc = "";
		String discount = "";
		String[] temp;
		Vector products = orderProducts();
		temp = products.get(0).toString().split("---");
		price = temp[0];
		name = temp[2];
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		if(option == OPCION1 && descTextArea.isEditable() && descTextArea.getText().equals("")){
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_ErrorDescPoster", new String[] {""}));
		}else{		
			htmlcod="<html><head><title>Formato Actualizado Precio BECO</title>"+
			"<style type=text/css>"+
			".a{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-top: 0.5cm;padding-right: 0.7cm;font-family: \"Arial\";min-font-size: 50pt;font-size: 52pt;line-height: 50pt;}"+
			".b{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.5m;font-family: \"Arial\";font-size: 20pt;line-height: 22pt;}"+
			".c{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.5cm;font-family: \"Arial\";font-weight: 600;font-size: 40pt;line-height: 25pt;Text-align:right;}"+
			".d{color:#000000;text-decoration:none;padding-right: 0.7cm;font-family: \"Arial\";font-weight: 600;font-size: 90pt;line-height: 80pt;}"+
			".e{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.7cm;padding-bottom: 0.5cm;font-family: \"Arial\";font-size: 10pt;Text-align:center;}"+
			"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head>" +
			"<body onload='salir()'><table style='border:2px solid black;width:18cm;height:13.5cm; text-wrap:unrestricted; table-layout=fixed;'>"+
			"<tr><td style=\" padding:0; vertical-align:center;\"><table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
			newPrd = temp[4];
			charact = temp[6];
			if(temp.length == 7){
				charact = temp[6];
				
				//"<tr><td width=150% align=\"left\" class=a colspan=2>"+name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>";
				htmlcod+="<tr><td width=150% align=\"left\" class=a colspan=2>"+name+"</td></tr>";
				
				if(Boolean.getBoolean(newPrd)){
					htmlcod += "<tr><td align=\"left\" width=150% class=b colspan=2>Nuevo</td></tr>";
				}else{
					htmlcod += "<tr><td align=\"left\" width=150% class=b colspan=2></td></tr>";
				}
				htmlcod += getCharacteristics(charact);
				htmlcod +="<tr><td width=70% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td>"+
				"<td width=80% align=\"right\"  class=d colspan=1>"+price+"</td></tr>";
			}else{
				
				price = temp[0];
				fullPrice = temp[3].replaceAll(",", ".");
				dateFrom = temp[5];
				dateTo = temp[6];
				newPrd = temp[7];
				charact = temp[9];
				productQty = qtyProduct(Integer.valueOf(temp[1]));
				prdDesc = "Antes: Bs. ";
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				if (temp[8].equals("Y") && !temp[4].equals("0")){
					discount = "Descuento "+temp[4]+"%. ";
				}
				if(productQty > 0){
					htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=2>"+name+"</td></tr>";
					htmlcod +="<tr><td align=\"left\" width=150% class=b colspan=2>"+discount+prdDesc + fullPrice +"</td></tr>";
					htmlcod += getCharacteristics(charact);
					htmlcod +="<tr><td width=70% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td>"+
					"<td width=80% align=\"right\"  class=d colspan=1>"+price+"</td>"+
					"</tr>"+
					"<tr><td class=e width=100% colspan=2 align=\"center\">Promoci&oacute;n autorizada por el INDEPABIS. " +
					"V&aacute;lida del "+dateFrom+" al "+dateTo+". Piezas disponibles: "+productQty+"</td></tr>";
				}
			}
			htmlcod+="<tr><td class=e colspan=2 align=\"center\">Precio Incluye I.V.A. - Rif J-00046517-7 - CENTROBECO C.A.</td>"+
				"</tr></table></td></tr></table></body></html>";
			
			try {
				out = new PrintWriter(new FileWriter(doc));
				out.println(htmlcod);
				out.close();
				BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Crea el formato HTML de los carteles de tipo Vitrina o Display
	 */
	public void createVitrinas(){
		String price = "";
		String name = "";
		String prdDesc = "";
		String fullPrice = "";
		String dateFrom = "";
		String dateTo = "";
		String discount = "";
		int productQty = 0;
		int i = 0;
		String newPrd = "";
		String[] temp;
		Vector products = orderProducts();
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		htmlcod ="<html>"+
		"<head>"+
			"<title>Formato Actualizado Precio BECO</title>"+
				"<style type=text/css>"+
					".a{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-top: 0.3cm;padding-right: 0.7cm;font-family: \"Arial\";min-font-size: 17pt;font-size: 19pt;line-height: 17pt;}"+
					".b{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.7m;font-family: \"Arial\";font-size: 9pt;line-height: 10pt; vertical-align:top;}"+
					".c{color:#000000;text-decoration:none;padding-left: 0.7cm;font-family: \"Arial\";font-weight: 600;font-size: 12pt;line-height: 10pt;Text-align:right;}"+
					".d{color:#000000;text-decoration:none;padding-right: 0.7cm;font-family: \"Arial\";font-weight: 600;font-size: 30pt;line-height: 25pt;}"+
					".e{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.7cm;padding-bottom: 0.1cm;padding-top: 0.1cm;font-family: \"Arial\";font-size: 6pt;Text-align:center;}"+
				"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head>";
		htmlcod +=	"<body onload='salir()'>"+
		"<table style = 'border:2px solid black; width:10.5cm; height:24.3cm; text-wrap:unrestricted; table-layout: fixed;' >"+
		"<tr>"+
		"<td style=\" padding:0; vertical-align:center;\">"+
		"<table cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
		
		
		for(i = 0; i<products.size(); i++){
			temp = products.get(i).toString().split("---");
			name = temp[2];
			if(temp.length == 6 || temp.length == 7){
				price = temp[0];
				if(temp[3].equals("null")){
					prdDesc = "";
				}else{
					prdDesc = temp[3];
				}
				newPrd = temp[4];
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				//htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=3>"+name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+"</td></tr>"+
				htmlcod +="<tr><td width=150% align=\"left\" class=a colspan=3>"+name+"</td></tr>"+
				"<tr><td>&nbsp;</td><tr>"+
				"<tr><td width=70% align=\"left\" valign=top class=b colspan=1>"+prdDesc+"</td>"+
				"<td width=20% align=\"right\" class=c valign=bottom colspan=1 >Bs.</td>"+
				"<td width=60% align=\"right\"  class=d colspan=1>"+price+"</td>"+
				"</tr>";
			}else{
				price = temp[0];
				fullPrice = temp[3].replaceAll(",", ".");
				dateFrom = temp[5];
				dateTo = temp[6];
				newPrd = temp[7];
				productQty = qtyProduct(Integer.valueOf(temp[1]));
				prdDesc = "Antes: Bs. ";
				if(Boolean.getBoolean(newPrd)){
					prdDesc = "Nuevo. "+prdDesc;
				}
				if (temp[8].equals("Y") && !temp[4].equals("0")){
					discount = "Descuento "+temp[4]+"%. ";
				}
				if(productQty > 0){
					//htmlcod += "<tr><td border=0 width=150% align=\"left\" class=a colspan=3>" +name.substring(0, 1).toUpperCase()+(name.substring(1,name.length())).toLowerCase()+ "</td></tr>"+
					htmlcod += "<tr><td border=0 width=150% align=\"left\" class=a colspan=3>" +name+ "</td></tr>"+
					"<tr><td border=0 width=40% align=\"left\" class=b colspan=1>"+ discount+prdDesc + fullPrice+"</td>" +
					"<td border=0 width=50% align=\"right\" class=c valign=bottom colspan=1 >Ahora Bs.</td>"+
					"<td border=0 width=60% align=\"right\"  class=d colspan=1>" + price +  "</td></tr>"+
					"<tr><td border=0 class=e colspan=3 align=\"left\">Promoción Autorizada por el INDEPABIS. " +
					"Válida desde el "+dateFrom+" al "+dateTo+". Piezas disponibles: "+productQty+"</td></tr>";
				}
			}
		
		}

		htmlcod += "<tr><td class=e colspan=3 align=\"center\">CENTROBECO C.A. • RIF: J- 00046517-7 • Todos nuestros precios incluyen I.V.A.</td></tr>";
		htmlcod += "</table></td></tr></table></body></html>";
		
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcod);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	/**
	 * Crea el formato HTML de los carteles formato libre de tipo Carta Vertical 
	 * @param head String que representa el encabezado del cartel
	 * @param cont String con el contenido del cartel
	 * @param promo String con las fechas de vigencia de la promoción, si aplica
	 */
	public void createLibreCarta(String head, String cont, String promo) {
		
		String header = "";
		String content = "";
		String datef = "";
		String datet = "";
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String noPieces = "";
		int maxHeader = 20;
		int maxContent = 23;
		int maxLines = 6;
		if(head.equals("") && cont.equals("")){
			header = getContent(vHeaderTextArea.getText(),1, maxHeader, 2);
			content = getContent(vContentTextArea.getText(),1, maxContent, maxLines);
			if(dateFromPromo.getValue()!= null && dateToPromo.getValue() != null){
				datef = sdf.format((Date)dateFromPromo.getValue());
				datet = sdf.format((Date)dateToPromo.getValue());
				noPieces = (vQtyField.getText());
			}
		}else{
			header = getContent(head, 3, maxHeader, 2);
			content = getContent(cont, 3, maxContent, maxLines);
			if(!promo.equals("")){
				datef = promo.split("::")[0];
				datet = promo.split("::")[1];
				noPieces = (promo.split("::")[2]);
			}
		}
		
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		
		htmlcod="<html><head><title>Formato Actualizado Precio BECO</title>"+
		"<style type=text/css>"+
		".a{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-top: 0.5cm;padding-right: 0.7cm;font-family: \"Arial\";font-weight: 600;font-size: 65pt;Text-align:center;}"+
		".b{color:#000000;text-decoration:none;padding-left: 0.9cm;padding-right: 0.5m;font-family: \"Arial\";font-weight: 900;font-size: 20pt;line-height: 22pt;}"+
		".c{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.5cm;font-family: \"Arial\";font-weight: 900;font-size: 40pt;line-height: 25pt;}"+
		".d{color:#000000;text-decoration:none;padding-right: 0.7cm;padding-left: 0.7cm;font-family: \"Arial\";font-size: 48pt;line-height: 50pt;Text-align:center;}"+
		".e{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.7cm;font-family: \"Arial\";font-size: 10pt;Text-align:center;}"+
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head>" +
		"<body onload='salir()'><table style='border:2px solid black;width:21.59cm;height:27.94cm;text-wrap:unrestricted; table-layout: fixed;'>"+
		"<tr><td>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;</td></tr>"+
		"<tr><td width=150% align=\"center\" class=a colspan=2>"+header+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;</td></tr>"+
		"<tr><td width=150% align=\"center\"  class=d colspan=2>"+content+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;</td></tr>";
		if (!datef.equals("") && !datet.equals("")){
			htmlcod+="<tr><td class=e colspan=2 align=\"center\">Promoción Autorizada por el INDEPABIS. " +
			"Válida desde el "+datef+" al "+datet+". "+noPieces+" piezas disponibles.</td></tr>";
		}
		htmlcod+="<tr><td class=e colspan=2 align=\"center\">Precio Incluye I.V.A. - Rif J-00046517-7 - CENTROBECO C.A.</td>"+
		"</tr>" +
		"</td></tr></table></body></html>";
		
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcod);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Crea el formato HTML de los carteles formato libre de tipo Carta Horizontal
	 * @param head String que representa el encabezado del cartel
	 * @param cont String con el contenido del cartel
	 * @param promo String con las fechas de vigencia de la promoción, si aplica
	 */
	public void createLibreCartaL(String head, String cont, String promo) {
		
		String header = "";
		String content = "";
		String datef = "";
		String datet = "";
		String noPieces = "";
		int maxHeader = 20;
		int maxContent = 32;
		int maxLines = 5;
		
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		if(head.equals("") && cont.equals("")){
			header = getContent(vHeaderTextArea.getText(),1, maxHeader, 2);
			content = getContent(vContentTextArea.getText(),1, maxContent, maxLines);
			if(dateFromPromo.getValue()!= null && dateToPromo.getValue() != null){
				datef = sdf.format((Date)dateFromPromo.getValue());
				datet = sdf.format((Date)dateToPromo.getValue());
				noPieces = (vQtyField.getText());
			}
		}else{
			header = getContent(head, 3, maxHeader, 2);
			content = getContent(cont, 3, maxContent, maxLines);
			if(!promo.equals("")){
				datef = promo.split("::")[0];
				datet = promo.split("::")[1];
				noPieces = (promo.split("::")[2]);
			}
		}
		
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		
		htmlcod="<html><head><title>Formato Actualizado Precio BECO</title>"+
		"<style type=text/css>"+
		".a{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-top: 0.5cm;padding-right: 0.7cm;font-family: \"Arial\";font-weight: 600;font-size: 65pt;Text-align:center;}"+
		".b{color:#000000;text-decoration:none;padding-left: 0.9cm;padding-right: 0.5m;font-family: \"Arial\";font-weight: 900;font-size: 20pt;line-height: 22pt;}"+
		".c{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.5cm;font-family: \"Arial\";font-weight: 900;font-size: 40pt;line-height: 25pt;}"+
		".d{color:#000000;text-decoration:none;padding-right: 0.7cm;padding-left: 0.7cm;font-family: \"Arial\";font-size: 48pt;line-height: 50pt;Text-align:center;}"+
		".e{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.7cm;font-family: \"Arial\";font-size: 10pt;Text-align:center;}"+
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head>" +
		"<body onload='salir()'><table style='border:2px solid black;height:21.59cm;width:27.94cm;text-wrap:unrestricted; table-layout: fixed;'>"+
		"<tr><td>"+
		"<tr><td width=150% align=\"center\" class=a colspan=2>"+header+"</td></tr>"+
		"<br>"+
		"<tr><td width=150% align=\"center\"  class=d colspan=2>"+content+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp</td></tr>";
		if (!datef.equals("") && !datet.equals("")){
			htmlcod+="<tr><td class=e colspan=2 align=\"center\">Promoción Autorizada por el INDEPABIS. " +
			"Válida desde el "+datef+" al "+datet+". "+noPieces+" piezas disponibles.</td></tr>";
		}
		htmlcod+="<tr><td class=e colspan=2 align=\"center\">Precio Incluye I.V.A. - Rif J-00046517-7 - CENTROBECO C.A.</td>"+
		"</tr>" +
		"</td></tr></table></body></html>";
		
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcod);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Crea el formato HTML de los carteles formato libre de tipo Cubo
	 * @param head String que representa el encabezado del cartel
	 * @param cont String con el contenido del cartel
	 * @param promo String con las fechas de vigencia de la promoción, si aplica
	 */
	public void createLibreCubo(String head, String cont, String promo) {
		String header = "";
		String content = "";
		String datef = "";
		String datet = "";
		String noPieces = "";
		int maxHeader = 10;
		int maxContent = 25;
		int maxLines = 3;
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		if(head.equals("") && cont.equals("")){
			header = getContent(vHeaderTextArea.getText(),1, maxHeader, 2);
			content = getContent(vContentTextArea.getText(),1, maxContent, maxLines);
			if(dateFromPromo.getValue()!= null && dateToPromo.getValue() != null){
				datef = sdf.format((Date)dateFromPromo.getValue());
				datet = sdf.format((Date)dateToPromo.getValue());
				noPieces = (vQtyField.getText());
			}
		}else{
			header = getContent(head, 3, maxHeader, 2);
			content = getContent(cont, 3, maxContent, maxLines);
			if(!promo.equals("")){
				datef = promo.split("::")[0];
				datet = promo.split("::")[1];
				noPieces = (promo.split("::")[2]);
			}
		}
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		htmlcod =	"<html><head>"+
		"<title>Formato Actualizado Precio BECO</title>"+
		"<style type=text/css>"+
		".a{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-top: 0.5cm;padding-right: 0.6cm;font-family: \"Arial\";font-weight: 600;font-size: 40pt;line-height: 42pt;Text-align:center;}"+
		".b{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-right: 0.5m;font-family: \"Arial\";font-weight: 900;font-size: 13pt;line-height: 16pt;}"+
		".c{color:#000000;text-decoration:none;padding-left: 0.6cm;padding-bottom: 0.3cm;font-family: \"Arial\";font-size: 24pt;line-height: 22pt;}"+
		".d{color:#000000;text-decoration:none;padding-right: 0.6cm;padding-left: 0.6cm;font-family: \"Arial\";font-size: 22pt;line-height: 25pt;Text-align:center;}"+
		".e{color:#000000;text-decoration:none;padding-right: 0.6cm;padding-left: 0.6cm;font-family: \"Arial\";font-size: 7pt;line-height: 15pt;Text-align:center;}"+
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head><body onload='salir()'>"+
		"<table style='border:2px solid black;width:10.5cm;height:11.5cm;text-wrap:unrestricted;table-layout=fixed;'>"+
		"<tr><td>"+
		"<tr><td width=150% align=\"center\" class=a colspan=2>"+header+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;</td></tr>"+
		"<tr><td width=150% align=\"center\"  class=d colspan=2>"+content+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;<br>&nbsp;</td></tr>";
		if (!datef.equals("") && !datet.equals("")){
			htmlcod+="<tr><td class=e colspan=2 align=\"center\">Promoción Autorizada por el INDEPABIS. " +
			"Válida desde el "+datef+" al "+datet+". "+noPieces+" piezas disponibles.</td></tr>";
		}
		htmlcod+="<tr><td class=e colspan=2 align=\"center\">Precio Incluye I.V.A. - Rif J-00046517-7 - CENTROBECO C.A.</td>"+
		"</tr>" +
		"</td></tr></table></body></html>";
		
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcod);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Crea el formato HTML de los carteles formato libre de tipo Base Plana 
	 * @param head String que representa el encabezado del cartel
	 * @param cont String con el contenido del cartel
	 * @param promo String con las fechas de vigencia de la promoción, si aplica
	 */
	public void createLibreBasePlana(String head, String cont, String promo) {
		String header = "";
		String content = "";
		String datef = "";
		String datet = "";
		String noPieces = "";
		int maxHeader = 18;
		int maxContent = 35;
		int maxLines = 3;
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		if(head.equals("") && cont.equals("")){
			header = getContent(vHeaderTextArea.getText(),1, maxHeader, 2);
			content = getContent(vContentTextArea.getText(),1, maxContent, maxLines);
			if(dateFromPromo.getValue()!= null && dateToPromo.getValue() != null){
				datef = sdf.format((Date)dateFromPromo.getValue());
				datet = sdf.format((Date)dateToPromo.getValue());
				noPieces = (vQtyField.getText());
			}
		}else{
			header = getContent(head, 3, maxHeader, 2);
			content = getContent(cont, 3, maxContent, maxLines);
			if(!promo.equals("")){
				datef = promo.split("::")[0];
				datet = promo.split("::")[1];
				noPieces = (promo.split("::")[2]);
			}
		}
		String ruta = "FormatoCartelProducto.html";
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		
		htmlcod="<html><head><title>Formato Actualizado Precio BECO</title>"+
		"<style type=text/css>"+
		".a{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-top: 0.5cm;padding-right: 0.7cm;font-family: \"Arial\";font-weight: 600;font-size: 48pt;line-height: 50pt;Text-align:center;}"+
		".b{color:#000000;text-decoration:none;padding-left: 0.9cm;padding-right: 0.5m;font-family: \"Arial\";font-weight: 900;font-size: 20pt;line-height: 22pt;}"+
		".c{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.5cm;font-family: \"Arial\";font-weight: 900;font-size: 40pt;line-height: 25pt;}"+
		".d{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.7cm;font-family: \"Arial\";font-size: 28pt;line-height: 30pt;Text-align:center;}"+
		".e{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-right: 0.7cm;padding-bottom: 0.5cm;font-family: \"Arial\";font-size: 10pt;Text-align:center;}"+
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head>" +
		"<body onload='salir()'><table style='border:2px solid black;width:18cm;height:13.5cm;text-wrap:unrestricted; table-layout: fixed;'>"+
		"<tr><td>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;</td></tr>"+
		"<tr><td width=150% align=\"center\" class=a colspan=2>"+header+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;</td></tr>"+
		"<tr><td width=150% align=\"center\"  class=d colspan=2>"+content+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;</td></tr>";
		if (!datef.equals("") && !datet.equals("")){
			htmlcod+="<tr><td class=e colspan=2 align=\"center\">Promoción Autorizada por el INDEPABIS. " +
			"Válida desde el "+datef+" al "+datet+". "+noPieces+" piezas disponibles.</td></tr>";
		}
		htmlcod+="<tr><td class=e colspan=2 align=\"center\">Precio Incluye I.V.A. - Rif J-00046517-7 - CENTROBECO C.A.</td>"+
		"</tr>" +
		"</td></tr></table></body></html>";
		
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcod);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	                            /****************** Metodos utilitarios **********************/

	/** Habilita y deshabilita componentes apropiadamente y coloca valores por defecto si aplican */
	protected static final void set (Component component, boolean enable) {
		if (component instanceof CComboBox)			
			set((CComboBox)component, enable, true, 0);	 
		else {
			component.setEnabled(enable);			
		}
	}
	
	public void createLibreVitrina() {
		String header = vHeaderTextArea.getText();
		String content = vContentTextArea.getText();
		
		htmlcod="<html><head><title>Formato Actualizado Precio BECO</title>"+
		"<style type=text/css>"+
		".a{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-top: 0.5cm;padding-right: 0.7cm;font-family: \"Arial\";font-weight: 700;font-size: 52pt;line-height: 50pt;}"+
		".b{color:#000000;text-decoration:none;padding-left: 0.9cm;padding-right: 0.5m;font-family: \"Arial\";font-weight: 900;font-size: 20pt;line-height: 22pt;}"+
		".c{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.5cm;font-family: \"Arial\";font-weight: 900;font-size: 40pt;line-height: 25pt;}"+
		".d{color:#000000;text-decoration:none;padding-right: 0.7cm;font-family: \"Arial Narrow\";font-weight: 900;font-size: 90pt;line-height: 80pt;}"+
		".e{color:#000000;text-decoration:none;padding-left: 0.7cm;padding-bottom: 0.5cm;padding-top: 0.7cm;font-family: \"Arial\";font-weight: 900;font-size: 12pt;}"+
		"</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head><body onload='salir()'>"+
		"<table border = 1 style='border:2px solid black;width:10.5cm;height:24.3cm;'>"+
		"<tr><td><table cellpadding=0 cellspacing=0 border=0 bgcolor=white>"+
		"<tr><td width=150% align=\"left\" class=a colspan=2>"+header+"</td></tr>"+
		"<tr><td colspan=2>&nbsp;<br>&nbsp;<br>&nbsp;</td><tr><tr>"+
		"<td width=50% align=\"right\"  class=d colspan=2>"+content+"</td></tr><tr>"+
		"<td class=e colspan=2 align=\"center\">Precio Incluye I.V.A. - Rif J-00046517-7 - CENTROBECO C.A.</td>"+
		"</tr></table></td></tr></table></body></html>";
	}
	
	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void set (CComboBox component, boolean enable, boolean visible, int value) {		
		component.setValue(value);
		component.setEnabled(enable);		
		component.setEditable(enable);
		component.setVisible(visible);
					
	}
	
	//public void printPoster(){
		/*String ruta = "FormatoCartelProducto.html";
		String htmlcode = "";
		
		htmlcode = "<html><head><title>Formato Grande Precio BECO 2 Productos</title><style type=text/css>.a{color:#000000;text-decoration:none;padding-right: 12px;font-family: \"Arial\";font-size: 11pt;}.b{color:#000000;text-decoration:none;padding-right: 12px;font-family: \"Arial\";font-weight: 900;font-size: 17pt;}.c{color:#000000;text-decoration:none;padding-left: 12px;font-family: \"Arial\";font-weight: 900;font-size: 11pt;}.d{color:#000000;text-decoration:none;padding-left: 12px;font-family: \"Arial\";font-weight: 900;font-size: 22pt;}.e{color:#000000;text-decoration:none;padding-left: 12px;font-family: \"Arial Narrow\";font-weight: 900;font-size: 40pt;}.f{color:#000000;text-decoration:none;padding-left: 12px;font-family: \"Arial\";font-weight: 900;font-size: 15pt;}.g{color:#000000;text-decoration:none;padding-right: 12px;font-family: \"Arial\";font-size: 7pt;}</style><script language=JavaScript>function salir() {self.print();self.close();}</script></head>";
		htmlcode += "\n"+"<body onload='salir()'><table width=100% cellpadding=0 cellspacing=0 border=0 bgcolor=white>";
		htmlcode += "<tr height=160><td width=40% align=right style='border-top:   2px solid black; border-left: 2px solid black;border-right: 2px solid black' class=b>&nbsp;</td><td align=left class=d valign=bottom>&nbsp;</td><td>&nbsp;</td></tr>";
		htmlcode += "<tr><td width=40% align=right style='border-right: 2px solid black' class=b>nombre </td><td align=left class=d valign=bottom rowspan=\"3\">Bs.F.</td><TD align=\"left\" class=e width=\"46%\" rowspan=\"3\">precioBsF</TD></tr>";
		htmlcode += "<tr><td width=40% align=right style='border-right: 2px solid black' class=a>carac </td></tr>";
		htmlcode += "<tr><td width=40% align=right style='border-right: 2px solid black' class=a>marca</td></tr>";
		htmlcode += "<tr height=20><td width=40% align=right class=b>&nbsp;</td><td align=left class=d valign=bottom>&nbsp;</td><td>&nbsp;</td></tr>";
		htmlcode += "<tr><td width=40% align=right style='border-right: 2px solid black' class=b>nombre2</td><td align=left class=d valign=bottom rowspan=\"3\">Bs.F.</td><TD align=\"left\" class=e width=\"46%\" rowspan=\"3\">precio2BsF</TD></tr>";
		htmlcode += "<tr><td width=40% align=right style='border-right: 2px solid black' class=a>carac2</td></tr>";
		htmlcode += "<tr><td width=40% align=right style='border-right: 2px solid black' class=a>marca2</td></tr>";
		htmlcode += "<tr height=\"30\"><td align=center class=g size=2 valign=bottom colspan=\"3\">Precios con I.V.A incluído. CENTROBECO C.A. RIF: J-654378-0</td></tr>";
		htmlcode += "</table></body></html>";
		
		File doc = new File(ruta);
		doc.getPath().toString();
		String pathArchivo = doc.getAbsolutePath();
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(doc));
			out.println(htmlcode);
			out.close();
			BrowserLauncher.openURL("File:\\\\\\" + pathArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		public int print (Graphics g, PageFormat f, int pageIndex)
		   {
		      Graphics2D g2 = (Graphics2D) g;  // Allow use of Java 2 graphics on
		                                       // the print pages :

		      // A rectangle that shows the printable area of the page, allowing
		      // for margins all round. To be drawn on the first page (index = 0).
		      Rectangle2D rect = new Rectangle2D.Double(f.getImageableX(),
		                                                f.getImageableY(),
		                                                f.getImageableWidth(),
		                                                f.getImageableHeight());

		      // A simple circle to go on the second page (index = 1).
		      Ellipse2D circle = new Ellipse2D.Double(100,100,100,100);

		      switch (pageIndex)
		      {
		         case 0 : g2.setColor(Color.black);   // Page 1 : print a rectangle
		                  g2.draw(rect);
		                  return 0;
		         case 1 : g2.setColor(Color.red);     // Page 2 : print a circle
		                  g2.draw(circle);
		                  return 0;
		         default: return 1;        // No other pages
		      }
		   }
		
	//}
	
							/************************************  FIN ***************************************/
	
	
	@Override
	public void valueChanged(ListSelectionEvent e) {		
	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}

