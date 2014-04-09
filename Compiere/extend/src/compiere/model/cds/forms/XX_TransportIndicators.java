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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.MLookupFactory;
import org.compiere.plaf.CompiereColor;
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

import com.sun.org.apache.bcel.internal.generic.NEW;

import compiere.model.cds.MiniTablePreparator;

public class XX_TransportIndicators extends CPanel implements FormPanel,
ActionListener  {

	/**
	 *  @author Gabrielle Huchet
	 *  @version 
	 */
	
	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(XX_TransportIndicators.class);

	/** FormFrame */
	private FormFrame m_frame;
	/** Window No */
	private int m_WindowNo = 0;
	/** Contexto general*/ 
	protected Ctx ctx = Env.getCtx();
	
	/* Panel y campos de la ventana */
	private CPanel mainPanel = new CPanel();
	private CPanel parameterPanel = new CPanel();
	private CPanel commandPanel = new CPanel();
	private JScrollPane dataPane = new JScrollPane();
	private JScrollPane dataPane2 = new JScrollPane();
	private JScrollPane dataPane3 = new JScrollPane();
	private CPanel resultPanel = new CPanel();
	private CLabel labelDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateFrom")+":");
	private CLabel labelToDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateTo")+":");
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"), false, false, true, 
			DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),	false, false, true,
			DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateTo"));
	//Tiendas
	private CLabel labelStoreFrom = new CLabel(Msg.getMsg(Env.getCtx(), "XX_OriginStore")+":");
	private static CComboBox comboStoreFrom = new CComboBox();
	private CLabel labelStoreTo = new CLabel(Msg.getMsg(Env.getCtx(), "XX_DestinationStore")+":");
	private static CComboBox comboStoreTo = new CComboBox();
	
	private Date actualDate = new Date();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	//private FlowLayout parameterLayout = new FlowLayout();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private FlowLayout commandLayout = new FlowLayout();
	private GridBagLayout resultLayout = new GridBagLayout();	
	
	/*Ventana secundaria que muestra datos por circuitos*/
	private JDialog circuitWindow = null;
	private JButton circuitButton = new JButton("Circuitos del Período");
	
	/*Ventana secundaria que muestra datos por flota*/
	private JDialog fleetWindow = null;
	private JButton fleetButton = new JButton("Flota del Período");
	
	/** La tabla donde se guardarán los datos por circuito*/
	private MiniTablePreparator miniTable = new MiniTablePreparator();
	/** La tabla donde se guardarán los datos por flota*/
	private MiniTablePreparator miniTable2 = new MiniTablePreparator();
	
	private String circuitTitle =  "Circuitos del día";
	private String fleetTitle = "Flota del día";
	private TitledBorder borderPane2 = new TitledBorder(circuitTitle);
	private TitledBorder borderPane3 = new TitledBorder(fleetTitle);

	private ArrayList<JFormattedTextField> resultFields = new ArrayList<JFormattedTextField>();
	
	//Cabeceras de las columnas para tabla de datos por circuito
	private ColumnInfo[] columnsTableByCircuit = new ColumnInfo[] {
			new ColumnInfo("Circuito",".", String.class),
			new ColumnInfo("Km Planificados",".", Double.class),
			new ColumnInfo("Km Reales",".", Double.class),
			new ColumnInfo("Dispersion de Km",".", Double.class),
			new ColumnInfo("Tiempo en Ruta",".", String.class)
			
	};

	//Cabeceras de las columnas para tabla de datos por flota
	private ColumnInfo[] columnsTableByFleet = new ColumnInfo[] {
			new ColumnInfo("Flota",".", String.class),
			new ColumnInfo("Capacidad Máxima en Bultos del día",".", Double.class),
			new ColumnInfo("Capacidad Media en Bultos Equivalentes del día",".", Double.class),
			new ColumnInfo("Total Real en Bultos del día",".", Double.class),
		
	};
	private final static int FIELDSPERGROUP = 2, TOTALRESULTS = 16;
	
	/* Botones */
	private CButton bUpload = new CButton(Msg.translate(Env.getCtx(), "Update"));
	
	private static final long serialVersionUID = 1L;

	@Override
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;		
	}

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		
		try {		
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);	
		} catch(Exception e) {
			log.log(Level.SEVERE, "", e);
		}	
		
	}

	/** Inicialización de Campos */
	private final void jbInit() throws Exception {
		
		CompiereColor.setBackground(this);		
		m_frame.getRootPane().setDefaultButton(bUpload);
	
		//Creación del panel principal
		mainPanel.setLayout(mainLayout);
		
		parameterPanel.setLayout(parameterLayout);
		//parameterLayout.setAlignment(FlowLayout.CENTER);
		//parameterLayout.setHgap(15);
		//parameterPanel.setPreferredSize(new Dimension(800, 50));
		//parameterPanel.add(labelDate);
		//parameterPanel.add(vDate);
		//parameterPanel.add(labelToDate);
		//parameterPanel.add(vToDate);
		//parameterPanel.add(bUpload);
		
		// Fechas
		parameterPanel.add(labelDate, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		parameterPanel.add(dateFrom, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 130), 0, 0));
		parameterPanel.add(labelToDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		parameterPanel.add(dateTo, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 130), 0, 0));
		// Tienda origen
		parameterPanel.add(labelStoreFrom, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		parameterPanel.add(comboStoreFrom, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		// Tienda destino
		parameterPanel.add(labelStoreTo, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		parameterPanel.add(comboStoreTo, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		// Botón 
		parameterPanel.add(bUpload,   new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 50, 5, 0), 0, 0));
		
		
		resultPanel.setLayout(resultLayout);
		
		mainPanel.add(parameterPanel, BorderLayout.NORTH);	
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(resultPanel);	
		dataPane.setPreferredSize(new Dimension(800, 300));
		
		//Panel Inferior
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(circuitButton, null);
		commandPanel.add(fleetButton, null);
		
		/*Ventana secundaria de datos por circuito*/
		circuitWindow = new JDialog(m_frame,"Circuitos del Día");

		circuitWindow.add(dataPane2, BorderLayout.CENTER);
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		dataPane2.getViewport().add(miniTable,null);
		dataPane2.setPreferredSize(new Dimension(600, 300));
		dataPane2.setBorder(borderPane2);
		circuitWindow.pack();
		
		// cerrar ventana secundaria
		circuitWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				circuitWindow.setVisible(false);
			}
		
			public void windowClosed(WindowEvent e) {
				circuitWindow.setVisible(false);
			}
		});
		
		/*Ventana secundaria de datos por flota*/
		fleetWindow = new JDialog(m_frame,"Flota del Día");
		fleetWindow.add(dataPane3, BorderLayout.CENTER);
		miniTable2.setRowHeight(miniTable.getRowHeight() + 2);
		dataPane3.getViewport().add(miniTable2,null);
		dataPane3.setPreferredSize(new Dimension(600, 300));
		dataPane3.setBorder(borderPane3);
		fleetWindow.pack();
		
		// cerrar ventana secundaria
		fleetWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				fleetWindow.setVisible(false);
			}
		
			public void windowClosed(WindowEvent e) {
				fleetWindow.setVisible(false);
			}
		});
	
	
		//Agregar los action listeners
		addActionListeners();
	}
	
	/** Inicializador de los datos por defecto */
	private void dynInit () {

		removeActionListeners();		
		//dateFrom.setValue(actualDate);
		dateTo.setValue(actualDate);
	
		comboStoreFrom.setEnabled(true);
		comboStoreTo.setEnabled(true);
		comboStoreFrom.removeAllItems();
		comboStoreTo.removeAllItems();
		llenarCombos();
		
		for (int i = 0; i < TOTALRESULTS; i++) {
			JFormattedTextField temp = new JFormattedTextField();
			temp.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			temp.setBackground(Color.LIGHT_GRAY);
			temp.setEditable(false);
			temp.setPreferredSize(new Dimension(50,20));
			resultFields.add(temp);
		}
	
		//CAPACIDAD MÁXIMA BULTOS
		addLabel(new CLabel("Capacidad Máxima en Bultos:"),1,0,1);
		addLabel(resultFields.get(0),1,1,1);
		addLabel(new CLabel("Capacidad Media en Bultos Equivalentes:"),1,3,1);
		addLabel(resultFields.get(1),1,4,1);
		addLabel(new CLabel("Total Real de Bultos:"),2,0,1);
		addLabel(resultFields.get(2),2,1,1);
		
		//OCUPACION FLOTA BECO
		addLabel(new CLabel("% Ocupación Flota BECO:"),3,0,1);
		addLabel(resultFields.get(3),3,1,1);
		
		//OCUPACION FLOTA TERCEROS
		addLabel(new CLabel("% Ocupación Flota Terceros:"),3,3,1);
		addLabel(resultFields.get(4),3,4,1);
		
		//TOTAL FLOTA BECO
		addLabel(new CLabel("Total Flota BECO:"),5,0,1);
		addLabel(resultFields.get(5),5,1,1);
		
		//TOTAL FLOTA TERCEROS
		addLabel(new CLabel("Total Flota Terceros:"),5,3,1);
		addLabel(resultFields.get(6),5,4,1);
		
		//TOTAL FLOTA BECO DISPONIBLE
		addLabel(new CLabel("Total Flota Beco Disponible:"),6,0,1);
		addLabel(resultFields.get(7),6,1,1);
		
		//TOTAL FLOTA TERCEROS DISPONIBLE
		addLabel(new CLabel("Total Flota Terceros Disponible:"),6,3,1);
		addLabel(resultFields.get(8),6,4,1);
		
		//UTILIZACION FLOTA BECO
		addLabel(new CLabel("% Utilización Flota BECO:"),7,0,1);
		addLabel(resultFields.get(9),7,1,1);
		
		//UTILIZACION FLOTA TERCEROS
		addLabel(new CLabel("% Utilización Flota Terceros:"),7,3,1);
		addLabel(resultFields.get(10),7,4,1);
		
		//EFICIENCIA FLOTA BECO
		addLabel(new CLabel("% Eficiencia Flota BECO:"),8,0,1);
		addLabel(resultFields.get(11),8,1,1);
		
		//CANTIDAD GD BECO
		addLabel(new CLabel("Cantidad Guías de Despacho Flota BECO:"),9,0,1);
		addLabel(resultFields.get(12),9,1,1);
		
		//CANTIDAD GD TERCEROS
		addLabel(new CLabel("Cantidad Guías de Despacho Terceros:"),9,3,1);
		addLabel(resultFields.get(13),9,4,1);
		
		//BULTOS GD BECO
		addLabel(new CLabel("Bultos/Guías de Despacho Flota BECO:"),10,0,1);
		addLabel(resultFields.get(14),10,1,1);
		
		//BULTOS GD TERCEROS
		addLabel(new CLabel("Bultos/Guías de Despacho Flota Terceros:"),10,3,1);
		addLabel(resultFields.get(15),10,4,1);
		
		uploadResult();	
		addActionListeners();
	}
	
	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		comboStoreFrom.addActionListener(this);
		comboStoreTo.addActionListener(this);
		bUpload.addActionListener(this);
		circuitButton.addActionListener(this);
		fleetButton.addActionListener(this);
	}
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		comboStoreFrom.removeActionListener(this);
		comboStoreTo.removeActionListener(this);
		bUpload.removeActionListener(this);
		circuitButton.removeActionListener(this);
		fleetButton.removeActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		removeActionListeners();	
		//Execute Upload
		if(e.getSource() == bUpload) {
			try {
				if(dateFrom.getTimestamp()==null || dateTo.getTimestamp()==null){
					// El período es obligatorio
					String msg = Msg.getMsg(Env.getCtx(), "Date Mandatory");
					ADialog.error(m_WindowNo, m_frame, msg);
				} else if(((Date)dateFrom.getValue()).after(actualDate) | ((Date)dateTo.getValue()).after(actualDate)){
					String msg = Msg.getMsg(Env.getCtx(), "XX_DateAfter");
					ADialog.error(m_WindowNo, m_frame, msg);
				} else if (((Date)dateFrom.getValue()).after((Date)dateTo.getValue())) { 
					// Fecha inicio posterior a la fecha de finalización
					String msg = Msg.getMsg(Env.getCtx(), "XX_EndStartDate");
					ADialog.error(m_WindowNo, m_frame, msg);
				} else { //if(dateFrom.getValue()!=null && !((Date)dateFrom.getValue()).after(actualDate)){
					uploadResult();	
				}
			} catch (NullPointerException n) {
				n.printStackTrace();
			}	
		} 
		else if(e.getSource() == circuitButton) {
			try {
				
				llenarTabla();
			
		        // Se obtienen las dimensiones en pixels de la pantalla.
		        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		        
		        // Se obtienen las dimensiones en pixels de la ventana.
		        Dimension ventana = circuitWindow.getSize();
		        
		        // Una cuenta para situar la ventana en el centro de la pantalla.
		        circuitWindow.setLocation(
		            (pantalla.width - ventana.width) / 2,
		            (pantalla.height - ventana.height) / 2);
		        
		    	circuitWindow.pack();
				circuitWindow.setVisible(true);
				
				
			} catch (NullPointerException n) {
			}	
		} 
		else if(e.getSource() == fleetButton) {
			try {
				
				llenarTabla2();
			
		        // Se obtienen las dimensiones en pixels de la pantalla.
		        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		        
		        // Se obtienen las dimensiones en pixels de la ventana.
		        Dimension ventana = fleetWindow.getSize();
		        
		        // Una cuenta para situar la ventana en el centro de la pantalla.
		        fleetWindow.setLocation(
		            (pantalla.width - ventana.width) / 2,
		            (pantalla.height - ventana.height) / 2);
		        
		        fleetWindow.pack();
		        fleetWindow.setVisible(true);
				
				
			} catch (NullPointerException n) {
			}	
		} 
		addActionListeners();	
	}

	private void uploadResult() {

		uploadInfoLabels();
	}

	/**
	 * Llena los comboboxes de tiendas origen y destino, simultáneamente
	 */
	private  void llenarCombos(){
		String sql = "SELECT M_WAREHOUSE_ID, name from M_Warehouse order by Value asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		KeyNamePair loadKNP = new KeyNamePair(0, new String()); //En blanco
		comboStoreFrom.addItem(loadKNP);
		comboStoreTo.addItem(loadKNP);
		int selected = Env.getCtx().getContextAsInt("#M_Warehouse_ID");
		try  {
			KeyNamePair selectedKNP = loadKNP;
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				if (rs.getInt(1) == selected) {
					selectedKNP = loadKNP;
				}
				comboStoreFrom.addItem(loadKNP); //Combo Tienda Origen
				comboStoreTo.addItem(loadKNP); //Combo Tienda Destino
			}
			comboStoreFrom.setSelectedItem(selectedKNP);
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}  finally {			
			//Cerrar los statements
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
			
				uploadCircuitData();
				miniTable.setRowSelectionAllowed(true);
				miniTable.setSelectionBackground(Color.white);
				miniTable.setAutoResizeMode(4);
				miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
				miniTable.getTableHeader().setReorderingAllowed(false);			
			} catch (Exception e) {
				
			}
			
			m_frame.setCursor(Cursor.getDefaultCursor());		
		}
	
	/** Llena la tabla a mostrar con los datos obtenidos del query*/
	protected void llenarTabla2() {
			m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			//Si no se ha cargado el header previamente
			miniTable2.setRowCount(0);
			miniTable2 = new MiniTablePreparator();

			//Calcular el query
			try {
			
				uploadCapacityByFleetData();
				miniTable2.setRowSelectionAllowed(true);
				miniTable2.setSelectionBackground(Color.white);
				miniTable2.setAutoResizeMode(4);
				miniTable2.setRowHeight(miniTable2.getRowHeight() + 2 );
				miniTable2.getTableHeader().setReorderingAllowed(false);			
			} catch (Exception e) {
				
			}
			
			m_frame.setCursor(Cursor.getDefaultCursor());		
		}
	private void uploadInfoLabels() {

		String dateF = DB.TO_DATE((Timestamp) dateFrom.getValue(),true); 
		String dateT = DB.TO_DATE((Timestamp) dateTo.getValue(),true); 
		
		String filtro = "\n	WHERE TRUNC(XX_DISPATCHDATE) between " + dateF + " and " + dateT +
						"\n		AND  XX_DISPATCHGUIDESTATUS in ('TRA', 'TIE') "; //Sólo GD con estado En Tienda o En Tránsito
		if (comboStoreFrom.getSelectedIndex()!=0) {
			filtro += "\n		AND XX_DEPARTUREWAREHOUSE_ID = '"+ ((KeyNamePair)comboStoreFrom.getSelectedItem()).getKey() +"' ";
		}
		if (comboStoreTo.getSelectedIndex()!=0) {
			filtro += "\n		AND XX_ARRIVALWAREHOUSE_ID = '" + ((KeyNamePair)comboStoreTo.getSelectedItem()).getKey() +"'" ;
		}
		
		String sql = "\nWITH  "+
		"\n CAPACIDAD_BULTOS AS ( "+
		"\n	SELECT (CASE WHEN SUM(F.XX_AVERCAPEQUIPACK*F.XX_ADJUSTMENTSAMOUNT)>0 THEN ROUND(SUM(F.XX_AVERCAPEQUIPACK*F.XX_ADJUSTMENTSAMOUNT),2) ELSE 0 END) CAPAC_MAXIMA_BULTOS_FLOTA_DIA, "+
		"\n	SUM(F.XX_AVERCAPEQUIPACK) CAPAC_MEDIA_BULTO_EQUIV_DIA, "+
		"\n	SUM(DG.XX_TOTALPACKAGESSENT) TOTAL_BULTOS_DIA "+
		"\n	FROM XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON (F.XX_VLO_FLEET_ID= DG.XX_VLO_FLEET_ID) " + filtro +
		"\n	), "+
		"\n OCUPACION_FLOTA_BECO AS ( "+
		"\n	SELECT (CASE WHEN SUM(F.XX_PackageQuantity*F.XX_AdjustmentsAmount)>0 " +
		"\n	                    THEN ROUND(SUM(DG.XX_TOTALPACKAGESSENT)*100/SUM(F.XX_PackageQuantity*F.XX_AdjustmentsAmount),2) ELSE 0 END) OCUPACION_FLOTA_BECO" +
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro +  
		"\n	AND F.XX_BECOFLEET = 'Y' "+
		"\n	), "+
		"\n OCUPACION_FLOTA_TERCEROS AS ( "+
		"\n	SELECT  (CASE WHEN SUM(F.XX_PackageQuantity*F.XX_AdjustmentsAmount)>0 " +
		"\n                   THEN ROUND(SUM(DG.XX_TOTALPACKAGESSENT)*100/SUM(F.XX_PackageQuantity*F.XX_AdjustmentsAmount),2) ELSE 0 END) OCUPACION_FLOTA_TERCEROS "+
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro + 
		"\n	AND F.XX_BECOFLEET = 'N' "+
		"\n	), "+
		"\n TOTAL_FLOTA_BECO AS ( "+
		"\n	SELECT COUNT(DISTINCT F.XX_VLO_FLEET_ID) TOTAL_FLOTA_BECO " +
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro + 
		"\n	AND F.XX_BECOFLEET = 'Y' "+
		"\n	), "+
		"\n TOTAL_FLOTA_TERCEROS AS ( "+
		"\n	SELECT COUNT(DISTINCT F.XX_VLO_FLEET_ID) TOTAL_FLOTA_TERCEROS " +
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro + 
		"\n	AND F.XX_BECOFLEET = 'N' "+
		"\n	), "+
		"\n TOTAL_FLOTA_BECO_DISP AS ( "+
		"\n	SELECT COUNT(F.XX_VLO_FLEET_ID) TOTAL_FLOTA_BECO_DISP " +
		"\n	FROM  XX_VLO_FLEET F " + 
		"\n	WHERE F.XX_BECOFLEET = 'Y' AND ISACTIVE='Y' "+
		"\n	), "+
		"\n TOTAL_FLOTA_TERCEROS_DISP AS ( "+
		"\n	SELECT COUNT(F.XX_VLO_FLEET_ID) TOTAL_FLOTA_TERCEROS_DISP " +
		"\n	FROM  XX_VLO_FLEET F " + 
		"\n	WHERE F.XX_BECOFLEET = 'N' AND ISACTIVE='Y' "+
		"\n	), "+
		"\n DIAS_TRABAJADOS AS ( "+
		"\n	SELECT COUNT(DISTINCT DG.XX_DISPATCHDATE) DIAS_TRABAJADOS "+
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG "+ filtro +
		"\n	), "+
		"\n UTILIZACION_FLOTA_BECO AS ( "+
		"\n	SELECT (CASE WHEN t2.DIAS_TRABAJADOS >0" +
		"\n	             THEN ROUND(t1.TOTAL_FLOTA_BECO*100/(t2.DIAS_TRABAJADOS*t3.TOTAL_FLOTA_BECO_DISP),2) ELSE 0 END) UTILIZACION_FLOTA_BECO  "+
		"\n	FROM TOTAL_FLOTA_BECO t1, DIAS_TRABAJADOS t2, TOTAL_FLOTA_BECO_DISP t3"+
		"\n	), "+
		"\n UTILIZACION_FLOTA_TERCEROS AS ( "+
		"\n	SELECT (CASE WHEN t2.DIAS_TRABAJADOS >0" +
		"\n	            THEN ROUND(t1.TOTAL_FLOTA_TERCEROS*100/(t2.DIAS_TRABAJADOS*t3.TOTAL_FLOTA_TERCEROS_DISP),2) ELSE 0 END) UTILIZACION_FLOTA_TERCEROS  " +
		"\n	FROM TOTAL_FLOTA_TERCEROS t1, DIAS_TRABAJADOS t2, TOTAL_FLOTA_TERCEROS_DISP t3 "+
		"\n	), "+
		"\n EFICIENCIA_FLOTA_BECO AS ( "+
		"\n	SELECT ROUND(t2.UTILIZACION_FLOTA_BECO * t1.OCUPACION_FLOTA_BECO,2) EFICIENCIA_FLOTA_BECO  "+
		"\n	FROM OCUPACION_FLOTA_BECO t1, UTILIZACION_FLOTA_BECO t2 "+
		"\n	), "+
		"\n CANTIDAD_GD_BECO AS( "+
		"\n	SELECT COUNT(DISTINCT DG.XX_VLO_DISPATCHGUIDE_ID) CANTIDAD_GD_BECO_MES "+
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro +
		"\n	AND F.XX_BECOFLEET = 'Y' "+
		"\n	), "+
		"\n CANTIDAD_GD_TERCEROS AS( "+
		"\n	SELECT COUNT(DISTINCT DG.XX_VLO_DISPATCHGUIDE_ID) CANTIDAD_GD_TERCEROS_MES "+
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro +
		"\n	AND F.XX_BECOFLEET = 'N' "+
		"\n	),  "+
		"\n BULTOS_GD_BECO_MES AS( "+
		"\n	SELECT (CASE WHEN COUNT(DG.XX_VLO_DISPATCHGUIDE_ID)>0 THEN ROUND(SUM(DG.XX_TOTALPACKAGESSENT)/COUNT(DG.XX_VLO_DISPATCHGUIDE_ID),2) ELSE 0 END) BULTOS_GD_BECO_MES "+
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro +
		"\n	AND F.XX_BECOFLEET = 'Y' "+
		"\n	),   "+
		"\n BULTOS_GD_TERCEROS_MES AS( "+
		"\n	SELECT (CASE WHEN COUNT(DG.XX_VLO_DISPATCHGUIDE_ID)>0 THEN ROUND(SUM(DG.XX_TOTALPACKAGESSENT)/COUNT(DG.XX_VLO_DISPATCHGUIDE_ID),2) ELSE 0 END) BULTOS_GD_TERCEROS_MES "+
		"\n	FROM  XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON(DG.XX_VLO_FLEET_ID = F.XX_VLO_FLEET_ID) "+ filtro +
		"\n	AND F.XX_BECOFLEET = 'N' "+
		"\n	) "+
		"\n	SELECT * "+
		"\n	FROM CAPACIDAD_BULTOS, "+
		"\n                 OCUPACION_FLOTA_BECO, OCUPACION_FLOTA_TERCEROS, "+
		"\n                 TOTAL_FLOTA_BECO, TOTAL_FLOTA_TERCEROS, TOTAL_FLOTA_BECO_DISP, TOTAL_FLOTA_TERCEROS_DISP,"+
		"\n                 UTILIZACION_FLOTA_BECO, UTILIZACION_FLOTA_TERCEROS, " +
		"\n                 EFICIENCIA_FLOTA_BECO, "+
		"\n                 CANTIDAD_GD_BECO, CANTIDAD_GD_TERCEROS, "+
		"\n                 BULTOS_GD_BECO_MES, BULTOS_GD_TERCEROS_MES ";

		//System.out.println("Principal: "+sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				for (int j = 0; j < TOTALRESULTS; j++) {
					JFormattedTextField temp = resultFields.get(j);
					Double result = (rs.getDouble(j+1));
					temp.setValue(result);
					resultFields.set(j, temp);
				}
			}	
			dataPane.repaint();
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
		

		repaint();
	}

	private void uploadCircuitData() {
		
		dataPane2.getViewport().add(miniTable);
		//borderPane2.setTitle(circuitTitle+" "+ DateFormat.getDateInstance(DateFormat.SHORT).format(date));
		borderPane2.setTitle(circuitTitle);
		
		String dateF = DB.TO_DATE((Timestamp) dateFrom.getValue(),true); 
		String dateT = DB.TO_DATE((Timestamp) dateTo.getValue(),true); 

		String filtro = "\n	WHERE TRUNC(XX_DISPATCHDATE) between " + dateF + " and " + dateT +
						"\n		AND  XX_DISPATCHGUIDESTATUS in ('TRA', 'TIE') "; //Sólo GD con estado En Tienda o En Tránsito
		if (comboStoreFrom.getSelectedIndex()!=0) {
			filtro += "\n		AND XX_DEPARTUREWAREHOUSE_ID = '"+ ((KeyNamePair)comboStoreFrom.getSelectedItem()).getKey() +"' ";
		}
		if (comboStoreTo.getSelectedIndex()!=0) {
			filtro += "\n		AND XX_ARRIVALWAREHOUSE_ID = '" + ((KeyNamePair)comboStoreTo.getSelectedItem()).getKey() +"' " ;
		}

		String sql = "\nWITH KILOMETROS AS(SELECT W2.NAME||'-'||W1.NAME CIRCUITO, SUM(XX_KILOMETERSPLANNED) KM_PLANIFICADOS, "+
			"\nSUM(XX_KMAFTERDISPATCH-XX_KMBEFOREDISPATCH) KM_REALES, "+
			"\nSUM(XX_KILOMETERSPLANNED)-SUM(XX_KMAFTERDISPATCH-XX_KMBEFOREDISPATCH) DISPERSION_KM "+
			"\nFROM XX_VLO_DISPATCHGUIDE DG LEFT JOIN M_WAREHOUSE W1 ON (DG.XX_ARRIVALWAREHOUSE_ID = W1.M_WAREHOUSE_ID ) "+
			"\nLEFT JOIN M_WAREHOUSE W2 ON (DG.XX_DEPARTUREWAREHOUSE_ID = W2.M_WAREHOUSE_ID ) "+ filtro +
			"\nGROUP BY W2.NAME||'-'||W1.NAME)," +
			"\nTIEMPO_RUTA AS (SELECT W2.NAME||'-'||W1.NAME CIRCUITO, AVG((DG.XX_DATESTATUSONSTORE-DG.XX_DATESTATUSTRANSIT)*24) TIEMPO" +
			"\nFROM XX_VLO_DISPATCHGUIDE DG LEFT JOIN M_WAREHOUSE W1 ON (DG.XX_ARRIVALWAREHOUSE_ID = W1.M_WAREHOUSE_ID ) "+
			"\nLEFT JOIN M_WAREHOUSE W2 ON (DG.XX_DEPARTUREWAREHOUSE_ID = W2.M_WAREHOUSE_ID ) "+ filtro +
			"\nGROUP BY W2.NAME||'-'||W1.NAME" +
			"\n)" +
			"\nSELECT KM.CIRCUITO, KM.KM_PLANIFICADOS, KM.KM_REALES, KM.DISPERSION_KM, " +
			"\n(CASE " +
			"\n WHEN TR.TIEMPO IS NOT NULL AND trunc(mod((TR.TIEMPO)*24*60,60)) < 10 AND trunc(mod((TR.TIEMPO)*24*60*60,60)) < 10 THEN " +
			"\n trunc(mod((TR.TIEMPO)*24,24))||':0'||trunc(mod((TR.TIEMPO)*24*60,60))||':0'||trunc(mod((TR.TIEMPO)*24*60*60,60)) " +
			"\n WHEN TR.TIEMPO IS NOT NULL AND trunc(mod((TR.TIEMPO)*24*60,60)) < 10 THEN " +
			"\n trunc(mod((TR.TIEMPO)*24,24))||':0'||trunc(mod((TR.TIEMPO)*24*60,60))||':'||trunc(mod((TR.TIEMPO)*24*60*60,60)) " +
			"\n WHEN TR.TIEMPO IS NOT NULL AND trunc(mod((TR.TIEMPO)*24*60*60,60)) < 10 THEN " +
			"\n trunc(mod((TR.TIEMPO)*24,24))||':'||trunc(mod((TR.TIEMPO)*24*60,60))||':0'||trunc(mod((TR.TIEMPO)*24*60*60,60))" +
			"\n WHEN TR.TIEMPO IS NOT NULL THEN " +
			"\ntrunc(mod((TR.TIEMPO)*24,24))||':'||trunc(mod((TR.TIEMPO)*24*60,60))||':'||trunc(mod((TR.TIEMPO)*24*60*60,60))" +
			"\n ELSE '-' END)" +
			"\nFROM KILOMETROS KM INNER JOIN TIEMPO_RUTA TR ON (KM.CIRCUITO = TR.CIRCUITO) ";
		
		//System.out.println(sql);
		miniTable.prepareTable(columnsTableByCircuit, "", "", true, "");
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
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
	
	private void uploadCapacityByFleetData() {

		String dateF = DB.TO_DATE((Timestamp) dateFrom.getValue(),true); 
		String dateT = DB.TO_DATE((Timestamp) dateTo.getValue(),true); 

		String filtro = "\n	WHERE TRUNC(XX_DISPATCHDATE) between " + dateF + " and " + dateT +
						"\n		AND  XX_DISPATCHGUIDESTATUS in ('TRA', 'TIE') "; //Sólo GD con estado En Tienda o En Tránsito
		if (comboStoreFrom.getSelectedIndex()!=0) {
			filtro += "\n		AND XX_DEPARTUREWAREHOUSE_ID = '"+ ((KeyNamePair)comboStoreFrom.getSelectedItem()).getKey() +"' ";
		}
		if (comboStoreTo.getSelectedIndex()!=0) {
			filtro += "\n		AND XX_ARRIVALWAREHOUSE_ID = '" + ((KeyNamePair)comboStoreTo.getSelectedItem()).getKey() +"'" ;
		}
		
		dataPane3.getViewport().add(miniTable2);
		//borderPane3.setTitle(fleetTitle+" "+ DateFormat.getDateInstance(DateFormat.SHORT).format(date));
		borderPane3.setTitle(fleetTitle);
		
		String sql = 
				"\nSELECT  F.XX_TYPEVEHICLE||' - '||F.XX_BRAND_NAME||' - '|| F.XX_CARPLATE, "+
				"\nSUM(F.XX_AVERCAPEQUIPACK*XX_ADJUSTMENTSAMOUNT) CAPACIDAD_MAXIMA_BULTOS_FLOTA, "+
				"\nSUM(F.XX_AVERCAPEQUIPACK*XX_ADJUSTMENTSAMOUNT)*0.9 CAPACIDAD_MEDIA_BULTOS_EQUIV, "+
				"\nSUM(DG.XX_TOTALPACKAGESSENT) TOTAL_BULTOS_FLOTA "+
				"\nFROM XX_VLO_DISPATCHGUIDE DG INNER JOIN XX_VLO_FLEET F ON (F.XX_VLO_FLEET_ID= DG.XX_VLO_FLEET_ID) "+ filtro +
				"\nGROUP BY F.XX_TYPEVEHICLE||' - '||F.XX_BRAND_NAME||' - '||F.XX_CARPLATE "+
				"\nORDER BY F.XX_TYPEVEHICLE||' - '||F.XX_BRAND_NAME||' - '||F.XX_CARPLATE ";
				
		
		miniTable2.prepareTable(columnsTableByFleet, "", "", true, "");
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable2.loadTable(rs);
			miniTable2.repaint();
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

	
	/* Permite agregar un campo a la panel de resultados en la fila disponible
	 * especificando cuantas columnas ocupará 
	 * */
	
	private void addLabel(Component component, int row, int col, int horSpace) {
		
		/* Asignar la orientación dependiendo de la posicion*/
		int orientation = 0, orientation2 = 0 ;
		
		if (col % FIELDSPERGROUP == 1) {
			orientation = GridBagConstraints.WEST;
			orientation2 = GridBagConstraints.HORIZONTAL;
		} else {
			orientation = GridBagConstraints.EAST;
			orientation2 = GridBagConstraints.BOTH;
		}
	
		//Agregar al panel de parametros
		resultPanel.add(component,  
				new GridBagConstraints(
						col, row, horSpace, 1, 0.0, 0.0,
						orientation, orientation2, new Insets(5, 5, 5, 5), 0, 0));
	//	displayParameter += horSpace;
	}
	
	/** Permite agregar un campo a la panel de parametros en la fila disponible
	 * especificando cuantas columnas ocupará 
	 * */
	protected void addParameter (Component component, int row, int col, int horSpace) {
		
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
		parameterPanel.add(component,  
				new GridBagConstraints(
						col, row, horSpace, 1, 0.0, 0.0,
						orientation, orientation2, new Insets(5, 5, 5, 5), 0, 0));
	//	displayParameter += horSpace;
	}
}
