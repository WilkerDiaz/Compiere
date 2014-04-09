package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VFile;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;


import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_XX_OrderStatus;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.forms.indicator.XX_Indicator;

/**
 *  @author Gabrielle Huchet
 *  @version 
 */

public class XX_TotalPieces_Form extends CPanel implements FormPanel,
ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** FormFrame */
	private FormFrame m_frame;
	/** Window No */
	private int m_WindowNo = 0;
	/** Contexto general*/ 
	protected Ctx ctx = Env.getCtx();
	
	/* Panel y campos de la ventana */
	private CPanel mainPanel = new CPanel();
	private CPanel parameterPanel = new CPanel();
	private CPanel resultPanel = new CPanel();
	private CPanel resultPanelTop = new CPanel();
	private CPanel resultPanelDown = new CPanel();
	private CPanel commandPanel = new CPanel();
	private JScrollPane dataPane = new JScrollPane();
	
	private CPanel panelRE = new CPanel();
	private CPanel panelCH = new CPanel();
	
	private CPanel panelPE = new CPanel();
	private CPanel panelEB = new CPanel();
	private CPanel panelET = new CPanel();
	private CPanel panelTI = new CPanel();
	private CPanel panelPR = new CPanel();
	private CPanel panelPP = new CPanel();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private BorderLayout resultLayout = new BorderLayout();
	private FlowLayout resultLayoutTop = new FlowLayout();	
	private FlowLayout resultLayoutDown = new FlowLayout();	
	private FlowLayout commandLayout = new FlowLayout();


	private final int PURCHASEORDER = 0, DISTRIBUTION = 1, 
	STATUSPURCHASE = 5, STATUSPLACED = 4, TOTALPIECESPURCHASE = 6, TOTALPIECESPLACED = 5,
	FIELDSPERGROUP = 3;

	int option = PURCHASEORDER;
	
	private int displayParameter = 0, row = 0, col = 0;
	
	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(XX_TotalPieces_Form.class);

	//******* Campos del filtro disponibles
	//Radio button
	private JRadioButton rPurchOrder = new JRadioButton(Msg.translate(Env.getCtx(), "C_Order_ID"),true);
	private JRadioButton rPlaceOrder = new JRadioButton(Msg.translate(Env.getCtx(), "XX_VMR_Order_ID"),false);
	private CPanel panelGroup = new CPanel();
	private ButtonGroup buttonGroup = new ButtonGroup();
	//Categoría
	private VCheckBox checkCategory = new VCheckBox();
	private CLabel labelCategory = new CLabel(Msg.translate(ctx, "XX_VMR_Category_ID"));
	private CComboBox comboCategory = new CComboBox();
	//Departamento
	private VCheckBox checkDepartment = new VCheckBox();
	private CLabel labelDepartment = new CLabel(Msg.translate(ctx, "XX_VMR_Department_ID"));
	private CComboBox comboDepartment = new CComboBox();
	//Colección
	private VCheckBox checkCollection = new VCheckBox();
	private CLabel labelCollection = new CLabel(Msg.translate(ctx, "XX_VMR_Collection_ID"));
	private CComboBox comboCollection = new CComboBox();
	//Paquete
	private VCheckBox checkPackage = new VCheckBox();
	private CLabel labelPackage = new CLabel(Msg.translate(ctx, "XX_VMR_Package_ID"));
	private CComboBox comboPackage = new CComboBox();
	//Orden de Compra
	private CLabel labelPurchaseOrder = new CLabel(Msg.translate(Env.getCtx(), "C_Order_ID"));
	private VLookup lookupPurchaseOrder = null;	
	//Temporada
	private VCheckBox checkSeason = new VCheckBox();
	private CLabel labelSeason = new CLabel(Msg.translate(Env.getCtx(), "XX_VMR_Season_ID"));
	private CComboBox comboSeason = new CComboBox();
	//Estado O/C
	private VCheckBox checkOPurchaseStatus = new VCheckBox();
	private CLabel labelOPurchaseStatus= new CLabel(Msg.translate(ctx,"XX_OrderStatus"));
	private CComboBox comboOPurchaseStatus = new CComboBox();
	private Vector<String> OPurchaseStatus_name = new Vector<String>();
	//Distribución (Pedidos)
	private CLabel labelDistribution = new CLabel(Msg.translate(Env.getCtx(), "XX_VMR_DistributionHeader_ID"));
	private CComboBox comboDistribution = new CComboBox();
	//Estado Pedido
	private VCheckBox checkOPlacedStatus = new VCheckBox();
	private CLabel labelOPlacedStatus= new CLabel(Msg.translate(ctx,"XX_OrderRequestStatus"));
	private CComboBox comboOPlacedStatus = new CComboBox();
	private Vector<String> OPlacedStatus_name = new Vector<String>();
	// Total piezas Recibidas
	private CLabel labeltotalPiecesRE = new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " Recibidas:");
	private CLabel totalPiecesRE= new CLabel();
	// Total piezas Chequeadas
	private CLabel labeltotalPiecesCH = new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " Chequeadas:");
	private CLabel totalPiecesCH = new CLabel();
	// Total piezas en Tienda
	private CLabel labeltotalPiecesTI = new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " en " 
			+Msg.translate(Env.getCtx(), "M_Warehouse_ID")+":");
	private CLabel totalPiecesTI = new CLabel();
	// Total piezas en Tránsito
	private CLabel labeltotalPiecesET = new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " en Tránsito:");
	private CLabel totalPiecesET = new CLabel();
	// Total piezas en Bahía
	private CLabel labeltotalPiecesEB = new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " en Bahía:");
	private CLabel totalPiecesEB = new CLabel();
	// Total piezas en Por Etiquetar
	private CLabel labeltotalPiecesPE = new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " por Etiquetar:");
	private CLabel totalPiecesPE = new CLabel();
	// Total piezas en Distribucioens Pendientes Redistribucion
	private CLabel labeltotalPiecesPR= new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " Pendientes Redistribución: ");
	private CLabel totalPiecesPR = new CLabel();
	// Total piezas en Distribucioens Pendientes Predistribuidas
	private CLabel labeltotalPiecesPP= new CLabel(Msg.translate(Env.getCtx(), "XX_Total")+" "
			+Msg.translate(Env.getCtx(), "XX_Pieces")+ " Pendientes Predistribuidas: ");
	private CLabel totalPiecesPP = new CLabel();
	/* Botones */
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	private CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	private CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "Print"));
	
	/* Archivo a exportar*/
	private VFile bFile = new VFile("File", Msg.getMsg(ctx, "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	private CLabel labelFile = new CLabel(Msg.getMsg(Env.getCtx(), "File"));
	
	/** La tabla donde se guardarán los datos */
	protected MiniTablePreparator miniTable = new MiniTablePreparator();
	
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose


	/** Inicializar la forma */
	public void init(int WindowNo, FormFrame frame) {
		// TODO Auto-generated method stub
		m_WindowNo = WindowNo;
		m_frame = frame;
		
		try {		
			Env.getCtx().setIsSOTrx(m_WindowNo, false);
			Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
					.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
					DisplayTypeConstants.Search);
			lookupPurchaseOrder = new VLookup("C_Order_ID", false, false, true, l);
			lookupPurchaseOrder.setVerifyInputWhenFocusTarget(false);
			
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);			
		} catch(Exception e) {
			log.log(Level.SEVERE, "", e);
		}	
	}
	
	/** Inicializador de los datos por defecto */
	private void dynInit () {

		//removeActionListeners();		
		uploadBasicInfo();		
		//addActionListeners();
	}
	
	/** Inicialización de Campos */
	private final void jbInit() throws Exception {
		
		CompiereColor.setBackground(this);		
		m_frame.getRootPane().setDefaultButton(bSearch);
		mainPanel.setLayout(mainLayout);

		//Creación del panel principal
		parameterPanel.setLayout(parameterLayout);
		mainPanel.add(resultPanel, BorderLayout.SOUTH);
		resultPanel.setLayout(resultLayout);
		resultPanel.add(resultPanelTop, BorderLayout.NORTH);
		resultPanel.add(resultPanelDown, BorderLayout.CENTER);
		resultPanelTop.setLayout(resultLayoutTop);
		resultPanelDown.setLayout(resultLayoutDown);
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		resultLayoutTop.setHgap(30);
		resultLayoutDown.setHgap(30);

		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);		
		dataPane.setPreferredSize(new Dimension(1280, 600));

		//Panel Inferior
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
	
		commandPanel.add(bSearch, null);
		commandPanel.add(bReset, null);	
		commandPanel.add(labelFile, null);
		commandPanel.add(bFile, null);
		commandPanel.add(bPrint, null);
	
		//Agregar los action listeners
		addActionListeners();
		
	   // Group the radio buttons.
	   buttonGroup.add(rPurchOrder);
	   buttonGroup.add(rPlaceOrder);
	   
      // Panel of radio buttons.
	   panelGroup.add(rPurchOrder);
	   panelGroup.add(rPlaceOrder);

	   addParameter(panelGroup, 1 ,0, 2, parameterPanel);
	   addParameters();
	   setOptionParameters();
	}
	
	private void addParameters() {

		addParameter(labelPurchaseOrder,1,4,1, parameterPanel);
		addParameter(lookupPurchaseOrder,1,5,1, parameterPanel);
		addParameter(labelSeason,2,0,1, parameterPanel);
		addParameter(comboSeason,2,1,1, parameterPanel);
		
		addParameter(labelDistribution,2,0,1, parameterPanel);
		addParameter(comboDistribution,2,1,1, parameterPanel);
		
		addParameter(labelCategory,2,2,1, parameterPanel);
		addParameter(comboCategory,2,3,1, parameterPanel);
		addParameter(labelDepartment,2,4,1, parameterPanel);
		addParameter(comboDepartment,2,5,1, parameterPanel);
		addParameter(labelCollection,3,0,1, parameterPanel);
		addParameter(comboCollection,3,1,1, parameterPanel);
		addParameter(labelPackage,3,2,1, parameterPanel);
		addParameter(comboPackage,3,3,1, parameterPanel);
		
		addParameter(labelOPurchaseStatus,3,4,1, parameterPanel);
		addParameter(comboOPurchaseStatus,3,5,1, parameterPanel);
		
		addParameter(labelOPlacedStatus,3,4,1, parameterPanel);
		addParameter(comboOPlacedStatus,3,5,1, parameterPanel);
	

		panelRE.add(labeltotalPiecesRE);
		panelRE.add(totalPiecesRE);
		panelCH.add(labeltotalPiecesCH);
		panelCH.add(totalPiecesCH);
		
		panelPE.add(labeltotalPiecesPE);
		panelPE.add(totalPiecesPE);
		panelEB.add(labeltotalPiecesEB);
		panelEB.add(totalPiecesEB);
		panelET.add(labeltotalPiecesET);
		panelET.add(totalPiecesET);
		panelTI.add(labeltotalPiecesTI);
		panelTI.add(totalPiecesTI);
		panelPR.add(labeltotalPiecesPR);
		panelPR.add(totalPiecesPR);
		panelPP.add(labeltotalPiecesPP);
		panelPP.add(totalPiecesPP);
		
		resultPanelTop.add(panelRE,null);
		resultPanelTop.add(panelCH,null);
		resultPanelTop.add(panelPE,null);
		resultPanelTop.add(panelEB,null);
		resultPanelTop.add(panelET,null);
		resultPanelTop.add(panelTI,null);
		resultPanelDown.add(panelPR,null);
		resultPanelDown.add(panelPP,null);
		
		
	}

	private void setOptionParameters() {

		if(option == PURCHASEORDER){
			set(comboDistribution,false,false ,0);
			set(comboOPlacedStatus,false,false, 0);
			set(lookupPurchaseOrder, true,true);
			set(comboSeason,true);
			set(comboOPurchaseStatus,true);
			set(labelDistribution,false);
			set(labelOPlacedStatus,false);
			set(labelPurchaseOrder,true);
			set(labelSeason,true);
			set(labelOPurchaseStatus,true);
			panelCH.setVisible(true);
			panelRE.setVisible(true);
			panelPE.setVisible(false);
			panelEB.setVisible(false);
			panelET.setVisible(false);
			panelTI.setVisible(false);
			panelPR.setVisible(false);
			panelPP.setVisible(false);
		}else if (option == DISTRIBUTION){
			set(comboDistribution,true);
			set(comboOPlacedStatus,true);
			set(lookupPurchaseOrder,false,false);
			set(comboSeason,false,false, 0);
			set(comboOPurchaseStatus,false,false, 0);
			set(labelDistribution,true);
			set(labelOPlacedStatus,true);
			set(labelPurchaseOrder,false);
			set(labelSeason,false);
			set(labelOPurchaseStatus,false);
			panelCH.setVisible(false);
			panelRE.setVisible(false);
			panelPE.setVisible(true);
			panelEB.setVisible(true);
			panelET.setVisible(true);
			panelTI.setVisible(true);
			panelPR.setVisible(true);
			panelPP.setVisible(true);
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		//Desactivar los efectos de las acciones 
		//removeActionListeners();
		
		//
		if (e.getSource() == rPurchOrder){
			if(rPurchOrder.isSelected()){
				option = PURCHASEORDER;
				setOptionParameters();
			}
			uploadBasicInfo();
		}
		else if (e.getSource() == rPlaceOrder ){
			if(rPlaceOrder.isSelected()){
				option = DISTRIBUTION;
				setOptionParameters();
			}
			uploadBasicInfo();
		}
		else if (e.getSource() == comboCategory ){
			KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
			if (catg != null && catg.getKey() != 0 && catg.getKey() != 99999999){ 
		//	if (catg != null  && catg.getKey() != 99999999){ 
				set(comboDepartment, true);		
				uploadDepartments();
			} else {
				comboDepartment.setSelectedIndex(0);
				set(comboDepartment, false);				
			}	
		}
		else if (e.getSource() == comboCollection ){
			KeyNamePair collection = (KeyNamePair)comboCollection.getSelectedItem();
			if (collection!= null && collection.getKey() != 0 && collection.getKey() != 99999999){ 
	//		if (collection != null  && collection.getKey() != 99999999){ 
				set(comboPackage, true);
				uploadPackages();
			} else {
				comboPackage.setSelectedIndex(0);
				set(comboPackage, false);				
			}	
		}//Limpiar el filtro
		else if(e.getSource() == bReset){
			resetFilter();
		}
		//Execute Search
		else if(e.getSource() == bSearch) {
			try {
				llenarTabla();			
			} catch (NullPointerException n) {
			}	
		} 
		else if (e.getSource() == bPrint)
		{
			try {			
				XX_Indicator.imprimirArchivo(miniTable, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
		}
		
		//addActionListeners();
		
	}

	/** Permite agregar un campo a la panel de parametros en la fila disponible
	 * especificando cuantas columnas ocupará 
	 * */
	protected void addParameter (Component component, int row, int col, int horSpace, CPanel panel) {
		
		/* Asignar la orientación dependiendo de la posicion*/
		int orientation = 0, orientation2 = 0 ;
		
		if (col % FIELDSPERGROUP == 1) {
			orientation = GridBagConstraints.WEST;
			orientation2 = GridBagConstraints.HORIZONTAL;
		} else {
			orientation = GridBagConstraints.EAST;
			orientation2 = GridBagConstraints.NONE;
		}
	
		//Agregar al panel de parametros
		panel.add(component,  
				new GridBagConstraints(
						col, row, horSpace, 1, 0.0, 0.0,
						orientation, orientation2, new Insets(5, 5, 5, 5), 0, 0));
	//	displayParameter += horSpace;
	}
	
	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		
		comboCategory.addActionListener(this);	
		comboCollection.addActionListener(this);
		bReset.addActionListener(this);
		bSearch.addActionListener(this);
	    rPurchOrder.addActionListener(this);
	    rPlaceOrder.addActionListener(this);
	    bPrint.addActionListener(this);
	} 
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		
		comboCategory.removeActionListener(this);	
		comboCollection.removeActionListener(this);
		bReset.removeActionListener(this);
		bSearch.removeActionListener(this);
	    rPurchOrder.removeActionListener(this);
	    rPlaceOrder.removeActionListener(this);
	    bPrint.removeActionListener(this);

	}	
	
	/** Método invocado al presionar el botón de limpiar filtro */
	protected void resetFilter() { 
		removeActionListeners();		
		uploadBasicInfo();
		addActionListeners();
	}
	
	
	
	public final void uploadBasicInfo() {
		
		removeActionListeners();	
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
	
		uploadCategory();
		uploadDepartments();
		uploadCollection();
		uploadPackages();

		//Restore ComboBoxes and CheckBoxes		
		if(option == PURCHASEORDER){
			set(lookupPurchaseOrder, true,true);
			set(comboSeason, true);
			uploadSeasons();
			uploadStatusPurchaseOrder();
		}else if(option == DISTRIBUTION){
			set(comboDistribution, true);
			uploadDistributions();
			uploadStatusPlacedOrder();
		}
		set(comboCategory, true);
		set(comboDepartment, false);
		set(comboPackage, false);
		set(comboCollection, true);
		
		labeltotalPiecesRE.setVisible(false);
		totalPiecesRE.setVisible(false);
		labeltotalPiecesCH.setVisible(false);
		totalPiecesCH.setVisible(false);
		
		labeltotalPiecesPE.setVisible(false);
		totalPiecesPE.setVisible(false);
		labeltotalPiecesEB.setVisible(false);
		totalPiecesEB.setVisible(false);
		labeltotalPiecesET.setVisible(false);
		totalPiecesET.setVisible(false);
		labeltotalPiecesTI.setVisible(false);
		totalPiecesTI.setVisible(false);
		labeltotalPiecesPR.setVisible(false);
		totalPiecesPR.setVisible(false);
		labeltotalPiecesPP.setVisible(false);
		totalPiecesPP.setVisible(false);


		//Actualizar los cambios
		repaint();
	
		addActionListeners();
	}
	
	//**************** Métodos de utilidad para esta ventana *********************************
	
	private void uploadStatusPlacedOrder() {
		comboOPlacedStatus.removeAllItems();
		OPlacedStatus_name.removeAllElements();
		// LLenar los combo de listas
		comboOPlacedStatus.addItem("");
		OPlacedStatus_name.add("");
		comboOPlacedStatus.addItem(Msg.getMsg(Env.getCtx(), "XX_AllStatus"));
		OPlacedStatus_name.add(Msg.getMsg(Env.getCtx(), "XX_AllStatus"));
		try{
			for (X_Ref_XX_VMR_OrderStatus v : X_Ref_XX_VMR_OrderStatus.values()) {
				if(v.getValue() != "AN" && v.getValue()!="TI" && v.getValue()!="FP"){
					comboOPlacedStatus.addItem(v.getValue() + "-" + v);
					OPlacedStatus_name.add(v.getValue());
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		comboOPlacedStatus.setEnabled(true);		
		comboOPlacedStatus.setEditable(true);
		comboOPlacedStatus.setSelectedIndex(0);
	}

	private void uploadStatusPurchaseOrder() {
		comboOPurchaseStatus.removeAllItems();
		OPurchaseStatus_name.removeAllElements();
		// LLenar los combo de listas
		comboOPurchaseStatus.addItem("");
		OPurchaseStatus_name.add("");
		comboOPurchaseStatus.addItem(Msg.getMsg(Env.getCtx(), "XX_AllStatus"));
		OPurchaseStatus_name.add(Msg.getMsg(Env.getCtx(), "XX_AllStatus"));
		try{
			for (X_Ref_XX_OrderStatus v : X_Ref_XX_OrderStatus.values()) {
				if(v.getValue()== "RE" || v.getValue()=="CH"){
					comboOPurchaseStatus.addItem(v.getValue() + "-" + v);
					OPurchaseStatus_name.add(v.getValue());
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	
		comboOPurchaseStatus.setEnabled(true);		
		comboOPurchaseStatus.setEditable(true);
		comboOPurchaseStatus.setSelectedIndex(0);
	}


	private void uploadDistributions() {
		KeyNamePair loadKNP;
		String sql = "";						
		comboDistribution.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboDistribution.addItem(loadKNP);
		
		sql = "SELECT dt.XX_VMR_DISTRIBUTIONHEADER_ID" +
				"\nFROM XX_VMR_DISTRIBUTIONHEADER dt WHERE dt.ISACTIVE = 'Y' " +
				"\nAND dt.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY dt.XX_VMR_DISTRIBUTIONHEADER_ID"; 			
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
	}

	private void uploadSeasons() {
		KeyNamePair loadKNP;
		String sql = "";						
		comboSeason.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboSeason.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllSeasons"));
		comboSeason.addItem(loadKNP);
		
		sql = "SELECT se.XX_VMA_SEASON_ID, se.NAME " +
				"\nFROM XX_VMA_SEASON se WHERE se.ISACTIVE = 'Y' " +
				"\nAND se.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY se.NAME"; 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboSeason.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
		
	}


	private void uploadPackages() {
		KeyNamePair collection = (KeyNamePair)comboCollection.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		
		comboPackage.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboPackage.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllPackages"));
		comboPackage.addItem(loadKNP);
	
		if (collection != null && (collection.getKey() != 0 || collection.getKey() != 99999999)){			
			sql = "SELECT pa.XX_VMR_PACKAGE_ID, pa.NAME FROM XX_VMR_PACKAGE pa " +
				"\nWHERE pa.XX_VMR_COLLECTION_ID = " + collection.getKey() + 
				"\n AND pa.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY pa.NAME ";
		} else {					
			sql = "SELECT XX_VMR_PACKAGE_ID, NAME FROM XX_VMR_PACKAGE " +
				"\n WHERE AD_Client_ID = "+ ctx.getAD_Client_ID()+
				 " ORDER BY NAME";									
		} 
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboPackage.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}		
	}


	private void uploadCollection() {
		KeyNamePair loadKNP;
		String sql = "";						
		comboCollection.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboCollection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCollections"));
		comboCollection.addItem(loadKNP);
		sql = "SELECT co.XX_VMR_COLLECTION_ID, co.NAME " +
				"\nFROM XX_VMR_COLLECTION co WHERE co.ISACTIVE = 'Y' or co.ISACTIVE = 'N'" +
				"\nAND co.AD_Client_ID = "+ ctx.getAD_Client_ID()+
				"\n ORDER BY co.NAME"; 			
		try{			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCollection.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}	
	}


	private void uploadDepartments() {
		
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();				
		KeyNamePair loadKNP;
		String sql;
		
		comboDepartment.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboDepartment.addItem(loadKNP);	
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
		comboDepartment.addItem(loadKNP);	
		if (catg != null && catg.getKey() != 0){			
			sql = "SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||'-'||dp.NAME FROM XX_VMR_DEPARTMENT dp " 
				+ "WHERE dp.XX_VMR_CATEGORY_ID = " + catg.getKey() +  " ORDER BY dp.VALUE ";
		}  else {					
			sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT " +
					"WHERE AD_Client_ID = "+ ctx.getAD_Client_ID();
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


	private void uploadCategory() {
		
		KeyNamePair loadKNP;
		String sql = "";						
		comboCategory.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboCategory.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCategories"));
		comboCategory.addItem(loadKNP);
		sql = " SELECT ct.XX_VMR_CATEGORY_ID, ct.VALUE||'-'||ct.NAME " +
				" FROM XX_VMR_CATEGORY ct WHERE ct.ISACTIVE = 'Y' AND ct.AD_Client_ID = "+ ctx.getAD_Client_ID(); 			
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

	/** Llena la tabla a mostrar con los datos obtenidos del query*/
	protected void llenarTabla() {
			m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			//Si no se ha cargado el header previamente
			miniTable.setRowCount(0);
			miniTable = new MiniTablePreparator();
	

			//Calcular el query
			try {
				if(option==DISTRIBUTION){
					calculateQueryOrderPlaced();
					calculateQueryTotalPiecesOnStore();
					calculateQueryTotalPiecesOnCD();
					calculateQueryTotalPiecesPendingRedist();
					calculateQueryTotalPiecesPendingPredist();
				}else if(option==PURCHASEORDER){
					calcularQueryPurchaseOrder();
					calculateQueryTotalPiecesStatusPurchase();
				}
				miniTable.setRowSelectionAllowed(true);
				miniTable.setSelectionBackground(Color.white);
				//miniTable.autoSize();
				miniTable.setAutoResizeMode(4);
				miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
				miniTable.getTableHeader().setReorderingAllowed(false);			
			} catch (Exception e) {}
			
			m_frame.setCursor(Cursor.getDefaultCursor());		
		}
	
	private void calculateQueryTotalPiecesPendingPredist() {
		
		KeyNamePair distribution= (KeyNamePair)comboDistribution.getSelectedItem();
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();

		String sql = "";
		String where = "";
		String from = "";

		sql += "\nSELECT  SUM(PD.XX_DISTRIBUTEDQTY) cant " +
				"\nFROM XX_VMR_DISTRIBUTIONHEADER D INNER JOIN XX_VMR_PO_PRODUCTDISTRIB PD " +
				"\nON (D.XX_VMR_DISTRIBUTIONHEADER_ID = PD.XX_VMR_DISTRIBUTIONHEADER_ID) " +
				"\n INNER JOIN C_ORDER O ON (O.C_ORDER_ID = D.C_ORDER_ID)";

		where += "\nWHERE D.XX_DISTRIBUTIONSTATUS IN ('FP','PE','QT') AND D.C_ORDER_ID IS NOT NULL";
		//Categoría
		if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0 ) {	
			where += "\nAND O.XX_VMR_CATEGORY_ID = " + catg.getKey();
		}	
		//Departamento			
		if(dept != null && dept.getKey()!=99999999 &&  dept.getKey()!= 0) {
			where += "\nAND O.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
		}
		//Collección
		if (coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {
			where += "\nAND O.XX_VMR_COLLECTION_ID = " + coll.getKey();
		}	
		//Paquete 
		if(pack != null && (pack.getKey()!=99999999 && pack.getKey()!= 0 )){
			where += "\nAND O.XX_VMR_PACKAGE_ID = " + pack.getKey();
		}	
		//Distribucion ID
		if(distribution != null && distribution.getKey() != 0){
			where += "\nAND D.XX_VMR_DISTRIBUTIONHEADER_ID = " +distribution.getKey();}
		
	
		sql += from + where;
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next())
			{
				String result = rs.getString(1);
				if(result!=null){
					totalPiecesPP.setText(result);
					
				}else {
					totalPiecesPP.setText("0");
				}		
				totalPiecesPP.setVisible(true);
				labeltotalPiecesPP.setVisible(true);
			}			
		} catch (SQLException e) {			
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


	private void calculateQueryTotalPiecesPendingRedist() {

		
		KeyNamePair distribution= (KeyNamePair)comboDistribution.getSelectedItem();
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();

		String sql = "";
		String where = "";
		String from = "";

		sql += "\nSELECT  SUM(DD.XX_DESIREDQUANTITY) cant " +
				"\nFROM XX_VMR_DISTRIBUTIONHEADER D INNER JOIN XX_VMR_DISTRIBUTIONDETAIL DD " +
				"\non (D.XX_VMR_DISTRIBUTIONHEADER_ID = DD.XX_VMR_DISTRIBUTIONHEADER_ID) ";

			where += "\nWHERE D.XX_DISTRIBUTIONSTATUS IN ('FP','PE','QT') AND C_ORDER_ID IS NULL";
		//Categoría
		if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0 ) {	
			from += "\nLEFT JOIN XX_VMR_Department de ON (de.XX_VMR_Department_ID = DD.XX_VMR_Department_ID) "; 
			where += "\nAND de.XX_VMR_Category_ID = " + catg.getKey();
		}	
		//Departamento			
		if(dept != null && dept.getKey()!=99999999 &&  dept.getKey()!= 0) {
			where += "\nAND DD.XX_VMR_Department_ID = " + dept.getKey();
		}
		//Collección
		if (coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {
			where += "\nAND DD.XX_VMR_Collection_ID = " + coll.getKey();
		}	
		//Paquete 
		if(pack != null && (pack.getKey()!=99999999 && pack.getKey()!= 0 )){
			where += "\nAND DD.XX_VMR_Package_ID = " + pack.getKey();
		}	
		//Distribucion ID
		if(distribution != null && distribution.getKey() != 0){
			where += "\nAND D.XX_VMR_DISTRIBUTIONHEADER_ID = " +distribution.getKey();
		}
		
		sql += from + where;
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next())
			{
				String result = rs.getString(1);
				if(result!=null){
					totalPiecesPR.setText(result);
					
				}else {
					totalPiecesPR.setText("0");
				}		
				totalPiecesPR.setVisible(true);
				labeltotalPiecesPR.setVisible(true);
			}			
		} catch (SQLException e) {			
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


	private void calculateQueryTotalPiecesStatusPurchase() {
		
		Integer totalRE = 0;
		Integer totalCH = 0;
		try{
			for (int i = 0; i < miniTable.getRowCount(); i++) {
				if(miniTable.getValueAt(i, STATUSPURCHASE).equals(X_Ref_XX_OrderStatus.RECIBIDA.toString())){
					totalRE = totalRE + (Integer)miniTable.getValueAt(i,TOTALPIECESPURCHASE);
				}else if(miniTable.getValueAt(i, STATUSPURCHASE).equals(X_Ref_XX_OrderStatus.CHEQUEADA.toString())){
					totalCH = totalCH + (Integer)miniTable.getValueAt(i,TOTALPIECESPURCHASE);
				}
			}
				totalPiecesRE.setText(totalRE.toString());
				totalPiecesRE.setVisible(true);
				labeltotalPiecesRE.setVisible(true);
				
				totalPiecesCH.setText(totalCH.toString());
				totalPiecesCH.setVisible(true);
				labeltotalPiecesCH.setVisible(true);
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void calculateQueryTotalPiecesOnCD() {
		
		Integer totalEB = 0;
		Integer totalET = 0;
		Integer totalPE = 0;
		try{
			for (int i = 0; i < miniTable.getRowCount(); i++) {
				if(miniTable.getValueAt(i, STATUSPLACED).equals(X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.toString())){
					totalPE = totalPE + (Integer)miniTable.getValueAt(i,TOTALPIECESPLACED);
				}else if(miniTable.getValueAt(i, STATUSPLACED).equals(X_Ref_XX_VMR_OrderStatus.EN_BAHIA.toString())){
					totalEB = totalEB + (Integer)miniTable.getValueAt(i,TOTALPIECESPLACED);
				}else if(miniTable.getValueAt(i, STATUSPLACED).equals(X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.toString())){
					totalET = totalET + (Integer)miniTable.getValueAt(i,TOTALPIECESPLACED);
				}
			}

				totalPiecesEB.setText(totalEB.toString());
				totalPiecesEB.setVisible(true);
				labeltotalPiecesEB.setVisible(true);
				
				totalPiecesET.setText(totalET.toString());
				totalPiecesET.setVisible(true);
				labeltotalPiecesET.setVisible(true);
				
				totalPiecesPE.setText(totalPE.toString());
				totalPiecesPE.setVisible(true);
				labeltotalPiecesPE.setVisible(true);
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void calculateQueryTotalPiecesOnStore() {
		
		KeyNamePair distribution= (KeyNamePair)comboDistribution.getSelectedItem();
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();

		String sql = "";
		String where2 = "";
		String from2 = "";

		sql += "\nSELECT  SUM(de2.XX_ProductQuantity) cant " +
				"\nFROM XX_VMR_Order pe2 INNER JOIN XX_VMR_OrderRequestDetail de2 on (de2.XX_VMR_Order_ID = pe2.XX_VMR_Order_ID) " +
				"\nINNER JOIN M_ATTRIBUTESETINSTANCE asi2 on (asi2.M_ATTRIBUTESETINSTANCE_ID =  de2.XX_PRODUCTBATCH_ID) ";
				 
			where2 += "\nWHERE pe2.XX_OrderRequestStatus = 'TI' ";
		/*}else if(option == CD){
			where2 += "\nWHERE pe2.XX_OrderRequestStatus != 'TI' AND pe2.XX_OrderRequestStatus != 'AN' ";
		}*/
			
		//Categoría
		if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0 ) {	
			from2 += "\nLEFT JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = de2.XX_VMR_Category_ID) ";
				if (where2.isEmpty())
					where2 += "\nc.XX_VMR_Category_ID = " + catg.getKey();
				else 
					where2 += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
		}	
		//Departamento			
		if(dept != null && dept.getKey()!=99999999 &&  dept.getKey()!= 0) {
			from2 += "\nLEFT JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = de2.XX_VMR_Department_ID) "; 
				if (where2.isEmpty())
					where2 += "\nd.XX_VMR_Department_ID = " + dept.getKey();
				else
					where2 += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
			
		}
		//Paquete 
		if((coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) || 
				(pack != null && (pack.getKey()!=99999999 && pack.getKey()!= 0 ))) {	
			from2 +=  "\nLEFT JOIN XX_VMR_Package pa on (pa.XX_VMR_Package_ID = asi2.XX_VMR_Package_ID)";
			if(pack != null && (pack.getKey()!=99999999 && pack.getKey()!= 0 )){
					if (where2.isEmpty())
						where2 += "\nasi2.XX_VMR_Package_ID = " + pack.getKey();
					else 
						where2 += "\nAND asi2.XX_VMR_Package_ID = " + pack.getKey();
				}
		}	
		//Collección
		if (coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {
				if (where2.isEmpty())
					where2 += "\npa.XX_VMR_Collection_ID = " + coll.getKey();
				else 
					where2 += "\nAND pa.XX_VMR_Collection_ID = " + coll.getKey();
		}	
		if(distribution != null && distribution.getKey() != 0){
			where2 += "\nAND pe2.XX_VMR_DISTRIBUTIONHEADER_ID = " +distribution.getKey();}
		
		sql += from2 + where2;

		//Distribucion
			String from = "";
			String where = "";
			String in = "";
			in += "\nAND pe2.XX_VMR_DISTRIBUTIONHEADER_ID in " +
				"\n(select pe.XX_VMR_DISTRIBUTIONHEADER_ID " +
				"\nFROM  XX_VMR_Order pe INNER JOIN XX_VMR_OrderRequestDetail de on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID) " +
				"\nINNER JOIN M_ATTRIBUTESETINSTANCE asi on (asi.M_ATTRIBUTESETINSTANCE_ID =  de.XX_PRODUCTBATCH_ID)";
		
			where += "pe.XX_OrderRequestStatus != 'TI' AND pe.XX_OrderRequestStatus != 'AN'";
			//Categoría
			if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0 ) {	
				from += "\nLEFT JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = de.XX_VMR_Category_ID) ";
					if (where.isEmpty())
						where += "\nc.XX_VMR_Category_ID = " + catg.getKey();
					else 
						where += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
			}	
			//Departamento			
			if(dept != null && dept.getKey()!=99999999 &&  dept.getKey()!= 0) {
				from += "\nLEFT JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = de.XX_VMR_Department_ID) "; 
					if (where.isEmpty())
						where += "\nd.XX_VMR_Department_ID = " + dept.getKey();
					else
						where += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
				
			}
			//Paquete 
			if((coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) || 
					(pack != null && (pack.getKey()!=99999999 && pack.getKey()!= 0 ))) {	
				from +=  "\nLEFT JOIN XX_VMR_Package pa on (pa.XX_VMR_Package_ID = asi.XX_VMR_Package_ID)";
				if(pack != null && (pack.getKey()!=99999999 && pack.getKey()!= 0 )){
						if (where.isEmpty())
							where += "\nasi.XX_VMR_Package_ID = " + pack.getKey();
						else 
							where += "\nAND asi.XX_VMR_Package_ID = " + pack.getKey();
					}
			}	
			//Collección
			if (coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {
					if (where.isEmpty())
						where += "\npa.XX_VMR_Collection_ID = " + coll.getKey();
					else 
						where += "\nAND pa.XX_VMR_Collection_ID = " + coll.getKey();
			}	
			if(distribution != null && distribution.getKey() != 0){
				where += "\nAND pe.XX_VMR_DISTRIBUTIONHEADER_ID = " +distribution.getKey();}
			
			if(!where.isEmpty()){
				where = "WHERE "+ where;
			}
			sql += in + from + where + " )";
		
			
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next())
			{
				String result = rs.getString(1);
				if(result!=null){
					totalPiecesTI.setText(result);
					
				}else {
					totalPiecesTI.setText("0");
				}		
				totalPiecesTI.setVisible(true);
				labeltotalPiecesTI.setVisible(true);
			}			
		} catch (SQLException e) {			
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
	
	
	/** Se determina el query de acuerdo a los parámetros ingresados cuando la opcion seleccionada es pedido*/
	private void calculateQueryOrderPlaced () {
	
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair distribution= (KeyNamePair)comboDistribution.getSelectedItem();
		String status = (String)comboOPlacedStatus.getSelectedItem();
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		
		String with = ""; 
		String select = "";
		String from =  "\nFROM XX_VMR_Order pe INNER JOIN XX_VMR_OrderRequestDetail de on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID) " +
				"\nINNER JOIN M_ATTRIBUTESETINSTANCE asi on (asi.M_ATTRIBUTESETINSTANCE_ID =  de.XX_PRODUCTBATCH_ID)";
		String where = "";
		String groupBy = "";
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
		
		if (where.isEmpty())
			where += "\npe.XX_OrderRequestStatus != 'TI' AND pe.XX_OrderRequestStatus != 'AN'"; 
		else where += "\nAND pe.XX_OrderRequestStatus != 'TI' AND pe.XX_OrderRequestStatus != 'AN'"; 
		
		//Distribucion
		if(distribution != null && distribution.getKey() != 0){
			if (where.isEmpty())
				where += "\npe.XX_VMR_DISTRIBUTIONHEADER_ID = " +distribution.getKey();
				
			else
				where += "\nAND pe.XX_VMR_DISTRIBUTIONHEADER_ID = " +distribution.getKey();
		}
	
		from += "\nLEFT JOIN XX_VMR_Category c ON (c.XX_VMR_Category_ID = de.XX_VMR_Category_ID) ";
		columnasAgregadas.add(colCatg);
		if (groupBy.isEmpty())
			groupBy += "\nc.value||'-'||c.name";
		else 
			groupBy += "\n,c.value||'-'||c.name";
		from += "\nLEFT JOIN XX_VMR_Department d ON (d.XX_VMR_Department_ID = de.XX_VMR_Department_ID) "; 
		columnasAgregadas.add(colDept);
		if (groupBy.isEmpty())
			groupBy += "\nd.value||'-'||d.name";
		else 
			groupBy += "\n,d.value||'-'||d.name";
		from +=  "\nLEFT JOIN XX_VMR_Package pa on (pa.XX_VMR_Package_ID = asi.XX_VMR_Package_ID)";
		if (groupBy.isEmpty())
			groupBy += "\npa.name";
		else 
			groupBy += "\n,pa.name";
		from += "\nLEFT JOIN XX_VMR_Collection co ON (co.XX_VMR_Collection_ID = pa.XX_VMR_Collection_ID) ";
		columnasAgregadas.add(colColl);
		columnasAgregadas.add(colPack);
		if (groupBy.isEmpty())
			groupBy += "\nco.name";
		else 
			groupBy += "\n,co.name";
		columnasAgregadas.add(colStatusPlaced);
		if (groupBy.isEmpty())
			groupBy += "\npe.XX_OrderRequestStatus";
		else 
			groupBy += "\n,pe.XX_OrderRequestStatus";
		
		//Categoría
		if(catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
			
			if (catg.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\nc.XX_VMR_Category_ID = " + catg.getKey();
				else 
					where += "\nAND c.XX_VMR_Category_ID = " + catg.getKey();
		}	
		//Departamento			
		if(dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {

			if (dept.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\nd.XX_VMR_Department_ID = " + dept.getKey();
				else
					where += "\nAND d.XX_VMR_Department_ID = " + dept.getKey();
			
		}
		//Paquete 
		if(pack != null && (pack.getKey()==99999999 || pack.getKey()!= 0 )){			
				if (pack.getKey()!= 99999999 ) 
					if (where.isEmpty())
						where += "\nasi.XX_VMR_Package_ID = " + pack.getKey();
					else 
						where += "\nAND asi.XX_VMR_Package_ID = " + pack.getKey();
		}
		
		//Collección
		if(coll != null && (coll.getKey()==99999999 || coll.getKey()!= 0 )) {
			if (coll.getKey()!= 99999999 ) 
				if (where.isEmpty())
					where += "\npa.XX_VMR_Collection_ID = " + coll.getKey();
				else 
					where += "\nAND pa.XX_VMR_Collection_ID = " + coll.getKey();
		}	

		//status
		if(status != null && (status != "" || status == Msg.getMsg(Env.getCtx(), "XX_AllStatus"))){
	
			if(status != Msg.getMsg(Env.getCtx(), "XX_AllStatus")){
				if (where.isEmpty())
					where += "\npe.XX_OrderRequestStatus = " +	"'" + OPlacedStatus_name.elementAt(comboOPlacedStatus.getSelectedIndex())+ "'"; 
				else where += "\nAND pe.XX_OrderRequestStatus = " + "'" + OPlacedStatus_name.elementAt(comboOPlacedStatus.getSelectedIndex())+ "'";
			}
		}

		// Agregar columnas de las cantidades	
		columnasAgregadas.add(colTotalPlaced);

		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
		String sql =  with + select + from ;
		if (!where.isEmpty()) {
			where = "\nWHERE  " + where;
			sql += where;
		}
		if (!groupBy.isEmpty()) {
			groupBy = "\nGROUP BY " + groupBy;
			sql += groupBy;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		}  finally {
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

	/** Se determina el query de acuerdo a los parámetros ingresados cuando la opcion seleccionada es Orden de Compra*/
	private void calcularQueryPurchaseOrder () {
	
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		Integer order= (Integer)lookupPurchaseOrder.getValue();	
		KeyNamePair season = (KeyNamePair)comboSeason.getSelectedItem();
	//	String status = (String)comboOPurchaseStatus.getSelectedItem();
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
	//	
		
		String with_cheq = ""; 
		String with_recib = ""; 
		String where_recib = "";
		String where="";
		String where_temp="";
		String sql= ""; 
		String select= ""; 
		
		//Temporada
		if(season != null && season .getKey()!=99999999 && season.getKey()!= 0 ) {	
				if (where.isEmpty()){
					where += "\noc.season = " + season.getKey();
					where_temp += "\noc.XX_VMA_Season_ID = " + season.getKey();
				}else{ 
					where += "\nAND oc.season = " + season.getKey();
					where_temp += "\nAND oc.XX_VMA_Season_ID = " + season.getKey();
				}
		}			
	 //Categoría
		if(catg != null && catg.getKey()!=99999999 && catg.getKey()!= 0 ) {	
				if (where.isEmpty()){
					where+= "\noc.catg = " + catg.getKey();
					where_temp += "\noc.XX_VMR_Category_ID = " + catg.getKey();
				}else { 
					where += "\nAND oc.catg = " + catg.getKey();
					where_temp += "\nAND oc.XX_VMR_Category_ID = " + catg.getKey();					
				}
		}	
		//Departamento			
		if(dept != null && dept.getKey()!=99999999 &&  dept.getKey()!= 0) {
				if (where.isEmpty()){
					where += "\noc.dept = " + dept.getKey();
					where_temp += "\noc.XX_VMR_Department_ID = " + dept.getKey();	
				}
				else{
					where += "\nAND oc.dept =" + dept.getKey();
					where_temp += "\nAND oc.XX_VMR_Department_ID = " + dept.getKey();	
				}
			
		}
		//Collección
		if(coll != null && coll.getKey()!=99999999 && coll.getKey()!= 0) {

				if (where.isEmpty()){
					where += "\noc.coll= " + coll.getKey();
					where_temp += "\noc.XX_VMR_Collection_ID = " + coll.getKey();
				}else { 
					where += "\nAND oc.coll =" + coll.getKey();
					where_temp += "\nAND oc.XX_VMR_Collection_ID = " + coll.getKey();
				}
		}	
		//Paquete 
		if(pack != null && pack.getKey()!=99999999 && pack.getKey()!= 0 ){			
					if (where.isEmpty()){
						where += "\noc.pack =" + pack.getKey();
						where_temp += "\noc.XX_VMR_Package_ID = " + pack.getKey();
					}else { 
						where += "\nAND oc.pack = " + pack.getKey();
						where_temp += "\nAND oc.XX_VMR_Package_ID = " + pack.getKey();
					}
		}
		if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())!="RE"){
		
			with_cheq += "WITH " +
			//Total Piezas de O/C Chequeadas Pre-distribuidas que están en tienda
		   "\nEN_TIENDA_PD AS " +
			    "\n(" +
			    "\nSELECT o.XX_VMA_Season_ID season, o.XX_VMR_Category_ID catg, " +
				    "\no.XX_VMR_Department_ID dept, o.XX_VMR_Collection_ID coll, o.XX_VMR_Package_ID pack, " +
				    "\no.XX_OrderStatus status, SUM(de.XX_ProductQuantity) cantTienda " +
				"\nFROM  C_Order o INNER JOIN XX_VMR_Order pe on (o.C_Order_ID = pe.C_Order_ID) " +
			    	"\nINNER JOIN XX_VMR_OrderRequestDetail de on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID)  " +
			    "\nWHERE o.isSOTRX='N' AND pe.XX_OrderRequestStatus = 'TI' " +
				    "\nAND o.XX_OrderStatus ='CH' " +
				    "\nAND o.XX_VLO_TypeDelivery = 'PD' AND O.XX_POTYPE = 'POM' ";
		   if(order != null ){
			   with_cheq += "\nAND o.C_Order_ID = " +order;
			}
		   with_cheq  += 
			   "\nGROUP BY o.XX_VMA_Season_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, " +
			    	"\no.XX_VMR_Collection_ID, o.XX_VMR_Package_ID, o.XX_OrderStatus" +
			    "\n)," +
				//Total Piezas de O/C Chequeadas Pre-distribuidas 
		   "\nEN_OC_PD AS " +
			    "\n(" +
			    "\nSELECT  o.XX_VMA_Season_ID season, o.XX_VMR_Category_ID catg, " +
			   		"\no.XX_VMR_Department_ID dept, o.XX_VMR_Collection_ID coll, o.XX_VMR_Package_ID pack, " +
			   		"\no.XX_OrderStatus status, SUM(iol.PickedQty)-SUM(iol.ScrappedQty) cantOC " +
			    "\nFROM  C_Order o INNER JOIN M_InOut io on (io.C_Order_ID = o.C_Order_ID) " +
			    	"\nINNER JOIN M_InOutLine iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
			    "\nWHERE io.isSOTRX='N' AND o.isSOTRX='N' AND o.XX_OrderStatus ='CH' AND O.XX_POTYPE = 'POM' " +
			    	"\nAND o.XX_VLO_TypeDelivery = 'PD' ";
		   if(order != null ){
			   with_cheq += "\nAND o.C_Order_ID = " +order;
		   }
		   with_cheq += 
			   "\nGROUP BY o.XX_VMA_Season_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, " +
			    	"\no.XX_VMR_Collection_ID, o.XX_VMR_Package_ID, o.XX_OrderStatus " +
			    "\n), ";
		   
		   with_cheq +=  
			//Total Piezas de O/C en estado Chequeada  de tipo Centro de Distribución que están en tienda
		    "EN_TIENDA_CD AS " +
		    	"\n(" +
		    	"\nSELECT o.XX_VMA_Season_ID season, o.XX_VMR_Category_ID catg, " +
		    		"\no.XX_VMR_Department_ID dept, o.XX_VMR_Collection_ID coll, o.XX_VMR_Package_ID pack, " +
		    		"\no.XX_OrderStatus status, SUM(de.XX_ProductQuantity) cantTienda " +
		    	"\nFROM C_Order o INNER JOIN M_InOut io on (io.C_Order_ID = o.C_Order_ID) " +
		    		"\nINNER JOIN M_InOutLine iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
		    		"\nINNER JOIN XX_VMR_OrderRequestDetail de on (iol.M_Product_ID = de.M_Product_ID) " +
		    		"\nINNER JOIN  XX_VMR_Order pe on (de.XX_VMR_Order_ID = pe.XX_VMR_Order_ID) " +
		    	"\nWHERE io.isSOTRX='N' AND o.isSOTRX='N' AND o.XX_OrderStatus ='CH' AND O.XX_POTYPE = 'POM' " +
		    		"\nAND o.XX_VLO_TypeDelivery = 'CD' " +
		    		"\nAND pe.XX_OrderRequestStatus = 'TI' " +
		    		"\nAND pe.XX_DATESTATUSONSTORE > o.XX_CHECKUPDATE ";
		    if(order != null ){
		    	with_cheq += "\nAND o.C_Order_ID = " +order;
		 	}
		    with_cheq  += 		    
		    	"\nGROUP BY o.XX_VMA_Season_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, " +
		    	"\no.XX_VMR_Collection_ID, o.XX_VMR_Package_ID, o.XX_OrderStatus " +
		    	"\n)," +
		    	//Total Piezas de O/C  en estado Chequeada de tipo Centro de Distribución
		    "\nEN_OC_CD AS " +
		    	"\n(" +
		    	"\nSELECT  o.XX_VMA_Season_ID season, o.XX_VMR_Category_ID catg, " +
		    		"\no.XX_VMR_Department_ID dept, o.XX_VMR_Collection_ID coll, o.XX_VMR_Package_ID pack, " +
		    		"\no.XX_OrderStatus status, SUM(iol.PickedQty)-SUM(iol.ScrappedQty) cantOC " +
		    	"\nFROM  C_Order o INNER JOIN M_INOUT io on (io.C_Order_ID = o.C_Order_ID) " +
		    		"\nINNER JOIN M_INOUTLINE iol on (iol.M_INOUT_ID = io.M_INOUT_ID) " +
		    	"\nWHERE io.isSOTRX='N' AND o.isSOTRX='N' AND o.XX_OrderStatus ='CH' AND O.XX_POTYPE = 'POM' " +
		    	"\nAND o.XX_VLO_TypeDelivery = 'CD' ";
		    if(order != null ){
		    	with_cheq += "\nAND o.C_Order_ID = " +order;
		 	}
		    with_cheq  += 
		    	"\nGROUP BY o.XX_VMA_Season_ID, o.XX_VMR_Category_ID, o.XX_VMR_Department_ID, " +
		    	"\no.XX_VMR_Collection_ID, o.XX_VMR_Package_ID, o.XX_OrderStatus " +
		    	"\n),";
		   
		 //Total Piezas de O/C menos total piezas en tienda en estado Chequeada de tipo Pre-distribuida 
		    with_cheq  += 
		    	"\nCHEQUEADAS_PD AS " +
			    "\n(" +
			    "\nSELECT s.Name season, c.Value||'-'||c.Name catg, d.Value||'-'||d.Name dept, " +
			    	"\nco.Name coll, pa.Name pack, " +
			    	"\n(CASE WHEN oc.status ='CH' THEN 'CHEQUEADA' " +
					      "\nWHEN oc.status ='RE' THEN 'RECIBIDA' " +
					      "\nELSE '-' END) status, " +
			    	"\n(CASE WHEN ti.cantTienda is null THEN oc.cantOC " +
			    	"\nWHEN oc.cantOC - ti.cantTienda < 0  THEN 0 " +
			    	"\nELSE (oc.cantOC - ti.cantTienda) END) TotalPieces " +
			    "\nFROM EN_OC_PD oc " +
			    	"\nLEFT JOIN EN_TIENDA_PD ti ON " +
			    		"\n(oc.season = ti.season " +
			    		"\nAND oc.catg = ti.catg " +
			    		"\nAND oc.dept = ti.dept " +
			    		"\nAND oc.coll = ti.coll " +
			    		"\nAND oc.pack = ti.pack " +
			    		"\nAND oc.status = ti.status) " +
			    	"\nLEFT JOIN XX_VMA_Season s ON (oc.season = s.XX_VMA_Season_ID) " +
			    	"\nLEFT JOIN XX_VMR_Category c ON (oc.catg = c.XX_VMR_Category_ID) " +
			    	"\nLEFT JOIN XX_VMR_Department d ON (oc.dept = d.XX_VMR_Department_ID) " +
			    	"\nLEFT JOIN XX_VMR_Collection co ON (oc.coll = co.XX_VMR_Collection_ID) " +
			    	"\nLEFT JOIN XX_VMR_Package pa ON (oc.pack = pa.XX_VMR_Package_ID)";
		    if (!where.isEmpty())
		    	 with_cheq  += "WHERE " + where;
		    with_cheq += "\n),";
		  //Total Piezas de O/C menos total piezas en tienda en estado Chequeada de tipo En Centro de Distribución
		    with_cheq  += 
		    	"\nCHEQUEADAS_CD AS " +
			    "\n(" +
			    "\nSELECT s.Name season, c.Value||'-'||c.Name catg, d.Value||'-'||d.Name dept, " +
			    	"\nco.Name coll, pa.Name pack, " +
			    	"\n(CASE WHEN oc.status ='CH' THEN 'CHEQUEADA' " +
					      "\nWHEN oc.status ='RE' THEN 'RECIBIDA' " +
					      "\nELSE '-' END) status, " +
			    	"\n(CASE WHEN ti.cantTienda is null THEN oc.cantOC " +
			    	"\nWHEN oc.cantOC - ti.cantTienda < 0  THEN 0 " +
			    	"\nELSE (oc.cantOC - ti.cantTienda) END) TotalPieces " +
			    "\nFROM EN_OC_CD oc " +
			    	"\nLEFT JOIN EN_TIENDA_CD ti ON " +
			    		"\n(oc.season = ti.season " +
			    		"\nAND oc.catg = ti.catg " +
			    		"\nAND oc.dept = ti.dept " +
			    		"\nAND oc.coll = ti.coll " +
			    		"\nAND oc.pack = ti.pack " +
			    		"\nAND oc.status = ti.status) " +
			    	"\nLEFT JOIN XX_VMA_Season s ON (oc.season = s.XX_VMA_Season_ID) " +
			    	"\nLEFT JOIN XX_VMR_Category c ON (oc.catg = c.XX_VMR_Category_ID) " +
			    	"\nLEFT JOIN XX_VMR_Department d ON (oc.dept = d.XX_VMR_Department_ID) " +
			    	"\nLEFT JOIN XX_VMR_Collection co ON (oc.coll = co.XX_VMR_Collection_ID) " +
			    	"\nLEFT JOIN XX_VMR_Package pa ON (oc.pack = pa.XX_VMR_Package_ID)";
		    if (!where.isEmpty())
		    	 with_cheq  += "\nWHERE " + where;
		    with_cheq += "\n)";
	
		}
		if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())!="CH"){
			 //Total Piezas de O/C en estado Recibida
			with_recib += 
				"\nOC_RECIBIDAS AS" +
				"\n(" +
				"\nSELECT   s.Name season, c.Value||'-'||c.Name catg, d.Value||'-'||d.Name dept, " +
					"\nco.Name coll, pa.Name pack, " +
					"\n(CASE WHEN oc.XX_OrderStatus ='CH' THEN 'CHEQUEADA' " +
					      "\nWHEN oc.XX_OrderStatus ='RE' THEN 'RECIBIDA' " +
					      "\nELSE '-' END) status, " +
					"\nSUM(oc.XX_ProductQuantity) TotalPieces " +
				"\nFROM  C_Order oc " +
				"\nLEFT JOIN XX_VMA_Season s ON (oc.XX_VMR_Season_ID = s.XX_VMA_Season_ID) " +
				"\nLEFT JOIN XX_VMR_Category c ON (oc.XX_VMR_Category_ID = c.XX_VMR_Category_ID) " +
				"\nLEFT JOIN XX_VMR_Department d ON (oc.XX_VMR_Department_ID = d.XX_VMR_Department_ID) " +
				"\nLEFT JOIN XX_VMR_Collection co ON (oc.XX_VMR_Collection_ID = co.XX_VMR_Collection_ID) " +
				"\nLEFT JOIN XX_VMR_Package pa ON (oc.XX_VMR_Package_ID = pa.XX_VMR_Package_ID) ";
			
			
			where_recib += where_temp;
			if (where.isEmpty())
				where_recib += "\noc.XX_OrderStatus ='RE'";
			else 
				where_recib += "\nAND oc.XX_OrderStatus ='RE'";
			
			where_recib = "\nWHERE oc.isSOTRX='N' AND OC.XX_POTYPE = 'POM' AND " + where_recib;
			with_recib += where_recib;
			  if(order != null ){
				   with_recib += "\nAND oc.C_Order_ID = " +order;
				}
			with_recib += "\nGROUP BY s.Name ,c.Value||'-'||c.Name, d.Value||'-'||d.Name , co.Name , pa.Name , oc.XX_OrderStatus) ";
		}
		
			if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())=="CH"){
			select = "\n" + miniTable.prepareTable(columnOrderPurchase, null, null, false, null);
			sql =  with_cheq + select +
		   		"\nFROM (SELECT *  from CHEQUEADAS_CD WHERE TotalPieces >  0 " +
		   		"\nUNION ALL " +
		   		"\nSELECT * from CHEQUEADAS_PD  WHERE TotalPieces > 0 ) " +
		   		"\nGROUP BY season, catg, dept, coll, pack, status " +
		   		"\nORDER BY season, catg, dept, coll, pack, status";
		}else if(OPurchaseStatus_name.elementAt(comboOPurchaseStatus.getSelectedIndex())=="RE"){
			select = "\n" + miniTable.prepareTable(columnOrderPurchaseRE, null, null, false, null);
			sql = "WITH" + with_recib + select + 
					"\nFROM OC_RECIBIDAS " +
					"\nORDER BY season, catg, dept, coll, pack, status";
		}else {
			select = "\n" + miniTable.prepareTable(columnOrderPurchase, null, null, false, null);
			sql = with_cheq + ", " + with_recib + select +
	   		"\nFROM (SELECT *  " +
	   			  "\nFROM (SELECT * FROM CHEQUEADAS_CD WHERE TotalPieces >  0 " +
	   				      "\nUNION ALL " +
	   					  "\nSELECT * FROM CHEQUEADAS_PD  WHERE TotalPieces > 0 )" +
	   			  "\nUNION ALL " +
	   			  "\nSELECT * FROM OC_RECIBIDAS)" +				  
	   		"\nGROUP BY season, catg, dept, coll, pack, status " +
	   		"\nORDER BY season, catg, dept, coll, pack, status";
		}
		
		loadOrderPurchaseTable(sql);
	}


	private void loadOrderPurchaseTable(String sql) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		}  finally {
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


	/** Habilita y deshabilita componentes apropiadamente y coloca valores por defecto si aplican */
	protected static final void set (Component component, boolean enable) {
		if (component instanceof CComboBox)			
			set((CComboBox)component, enable, true, 0);
		else if (component instanceof CCheckBox)
			set((CCheckBox)component, enable, enable);
		else if (component instanceof VFile) {			
			((VFile) component).setReadWrite(enable);			
			if (!enable) {
				((VFile) component).setValue(null);
			}			
		} else if (component instanceof CLabel)
			component.setVisible(enable);
		else {
			component.setEnabled(enable);			
		}
	}
	
	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void set (CComboBox component, boolean enable, boolean visible, int value) {		
		component.setValue(value);	
		component.setEnabled(enable);		
		component.setEditable(enable);
		component.setVisible(visible);
					
	}

	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void set (VLookup component, boolean enable, boolean visible) {		
		component.setEnabled(enable);		
		component.setVisible(visible);
		component.setValue(null);					
	}
	
	
	/** Habilita y deshabilita componentes apropiadamente */
	protected static final void set (CCheckBox component, boolean enable, boolean value) {		
		component.setEnabled(enable);
		component.setValue(value);
	}
	
	//Cabeceras de las columnas para tabla de la opcion Orden de Compra
	private ColumnInfo[] columnOrderPurchase = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Season_ID"),"season", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"), "catg", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"), "dept", String.class),
		    new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"), "coll", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Package_ID"), "pack", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderStatus"), "status", String.class),
			new ColumnInfo( Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"),
					"sum(TotalPieces)", Integer.class)
	};
	
	private ColumnInfo[] columnOrderPurchaseRE = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Season_ID"),"season", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"), "catg", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"), "dept", String.class),
		    new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"), "coll", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Package_ID"), "pack", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderStatus"), "status", String.class),
			new ColumnInfo( Msg.translate(Env.getCtx(), "Amount")+ " " +Msg.translate(Env.getCtx(), "XX_Pieces"),
					"TotalPieces", Integer.class)
	};

	//Cabeceras de las columnas para tabla de la opcion Pedido
	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"), 
			"\nc.Value||'-'||c.Name", String.class);	
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),
			"\nd.value||'-'||d.Name", String.class);	
	private ColumnInfo colColl = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Collection_ID"), 
			"\nco.Name", String.class);	
	private ColumnInfo colPack = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Package_ID"),
			"\npa.Name", String.class);
	private ColumnInfo colStatusPlaced = new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderRequestStatus"),
			"\n(CASE WHEN pe.XX_OrderRequestStatus ='EB' THEN 'EN_BAHIA' " +
			"\nWHEN pe.XX_OrderRequestStatus ='ET' THEN 'EN_TRÁNSITO' " +
			"\nWHEN pe.XX_OrderRequestStatus ='PE' THEN 'POR_ETIQUETAR' " +
			"\nWHEN pe.XX_OrderRequestStatus ='TT' THEN 'EN_TIENDA' " +
			"\nELSE '-' END) ", String.class);

	private ColumnInfo colTotalPlaced= new ColumnInfo( Msg.translate(Env.getCtx(), "Amount")+ 
			" " +Msg.translate(Env.getCtx(), "XX_Pieces"), "\nSUM(de.XX_ProductQuantity)", Integer.class);
	
}
