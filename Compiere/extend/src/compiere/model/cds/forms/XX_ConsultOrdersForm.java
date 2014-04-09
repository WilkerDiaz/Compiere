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
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 * Forma para realizar una consulta de compras.
 * @author Gabriela Marques
 *
 */

public class XX_ConsultOrdersForm  extends CPanel implements FormPanel, ActionListener {
		

	/**
	 * 
	 */
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
			jbInit(); // Layouts
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			//frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
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

	
	
	// LAYOUT >>>>>>>>
		//panel - tablas
		private CPanel mainPanel = new CPanel();
		private BorderLayout mainLayout = new BorderLayout();
		private CPanel northPanel = new CPanel(); 
		private BorderLayout northLayout = new BorderLayout();
		private CPanel parameterPanel2 = new CPanel();
		private GridBagLayout parameterLayout = new GridBagLayout();
		private CPanel commandPanel = new CPanel();
		private FlowLayout commandLayout = new FlowLayout();
	// FIN LAYOUT <<<<<<<<<<<
	
	// FILTROS >>>>>>>>>>>
		//Fechas pedido
		private CLabel labelDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateFrom")+":");
		private CLabel labelToDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateTo")+":");
		private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"), false, false, true, 
				DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateFrom"));
		private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),	false, false, true,
				DisplayTypeConstants.Date, Msg.translate(Env.getCtx(), "DateTo"));

		//Tipo de OC
		private CLabel labelOCType= new CLabel(Msg.translate(Env.getCtx(),"XX_OrderType"));	
		private VComboBox comboOrderedby= new VComboBox();
		
	// FIN FILTROS <<<<<<<
	protected CLabel labelFile = new CLabel(Msg.getMsg(Env.getCtx(), "File"));
	protected CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "ExportExcel"));
	protected VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	
	//buttons
	private CButton bSearch = new CButton();
	private JButton bReset = ConfirmPanel.createResetButton(Msg.getMsg(Env.getCtx(),"Clear"));

	Date fechaActual = new Date();
	Date FechaAyer = new Date( fechaActual.getTime()-86400000);
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat dateFormatBD = new SimpleDateFormat("yyyyMM");
	
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

		removeActionListeners();

		//  Visual
		CompiereColor.setBackground (this);
		m_frame.getRootPane().setDefaultButton(bSearch);
		mainPanel.setLayout(mainLayout);
		
		northPanel.setLayout(northLayout);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		
		//Creación del panel de filtro1 
		crearPanel(); //Por defecto
		
		//Panel de botones
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		northPanel.add(commandPanel, BorderLayout.SOUTH);
		bSearch.setText("Exportar");
		bSearch.setPreferredSize(new Dimension(100,28));	
		bSearch.setEnabled(true);
		commandPanel.add(bSearch, null);
		commandPanel.add(bReset, null);	
		
		addActionListeners();
		
			
	}   //  jbInit

	
	private void crearPanel() {
		parameterPanel2.setLayout(parameterLayout);
		northPanel.add(parameterPanel2, BorderLayout.NORTH);
		
		int i=0;
		
		// Fechas
		parameterPanel2.add(labelOCType, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 5, 5), 0, 0));
		parameterPanel2.add(comboOrderedby, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 60), 0, 0));
		
		parameterPanel2.add(labelDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 60, 5, 5), 0, 0));
		parameterPanel2.add(dateFrom, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 60), 0, 0));
		
		parameterPanel2.add(labelToDate, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 0, 5, 5), 0, 0));
		parameterPanel2.add(dateTo, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 60), 0, 0));
		
		
		parameterPanel2.add(labelFile, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(8, 5, 5, 5), 0, 0));
		parameterPanel2.add(bFile, new GridBagConstraints(3, 2, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(8, 0, 5, 60), 0, 0));
//		parameterPanel2.add(bPrint, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
//				new Insets(8, 5, 5, 5), 0, 0));
		
	}
	

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit() 	{	
		//Llenar los filtros de busquedas
		loadBasicInfo();
	}   //  dynInit

	
	

	/**
	 *  Table initial state 
	 */
	private void loadBasicInfo() {
		removeActionListeners();
				
		//Limpiar combos
		dateFrom.setValue(null);
		dateTo.setValue(null);
		//Llenar los filtros de busquedas
		llenarcombos();
		bFile.setValue(null);
//		bFile.setBackground(true);
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
	 * 
	 */
	private  void llenarcombos(){
		
		// Tipo de Orden
		KeyNamePair loadKNP;
		comboOrderedby.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboOrderedby.addItem(loadKNP);
		loadKNP = new KeyNamePair(1,Msg.translate(Env.getCtx(), "National"));
		comboOrderedby.addItem(loadKNP);
		loadKNP = new KeyNamePair(2,Msg.translate(Env.getCtx(), "Imported"));
		comboOrderedby.addItem(loadKNP);

	}
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e) {		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Cargar lista de referencias si se ha seleccionado BPartner
		
		
		if (e.getSource() == bSearch) {
			if (bFile.getValue()==null || bFile.getValue().equals("")) {
				ADialog.info(m_WindowNo, this.mainPanel, "Debe indicar la ruta del archivo.");	
			} else if (dateFrom.getValue()==null || dateFrom.getValue().equals("")
					|| dateTo.getValue()==null || dateTo.getValue().equals("") ) {
				ADialog.info(m_WindowNo, this.mainPanel, "Debe indicar el rango de fechas.");	
			} else {
				removeActionListeners();
				cmd_Search();
				addActionListeners();
			}
		} 
		else if (e.getSource() == bReset) {
			loadBasicInfo();
		} else {}
		
	}   //  actionPerformed

	
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()  {

		KeyNamePair tipo = (KeyNamePair)comboOrderedby.getSelectedItem();
		// Manejo de fechas
		String ffrom= dateFormatBD.format(dateFrom.getValue());
		String fto = dateFormatBD.format(dateTo.getValue());
		if (((Date)dateFrom.getValue()).after((Date)dateTo.getValue())) {
			ADialog.info(m_WindowNo, this.mainPanel, "Rango de fechas inválido");	
			return;
		}

		String consulta = " SELECT a.documentno as Nro_Recepcion, b.documentno as Nro_Orden, " +
					" c.value as Departamento, d.description as Pais, round(b.xx_definitivefactor, 2)  as Factor_Definitivo, " +
					" to_char(b.xx_receptiondate, 'DD/MM/YYYY') as Fecha_Recepcion, " +
					" to_char(b.XX_CHECKUPDATE, 'DD/MM/YY') as Fecha_Chequeo,  " +
					" sum(e.PICKEDQTY)as Cantidad_Chequeada, b.totallines as Costo_Moneda_Origen " +
				" FROM m_inout a, c_order b, XX_VMR_Department c,  c_country d, m_inoutline e" +
				" WHERE (TO_CHAR(b.xx_receptiondate,'YYYYMM') between '"+ffrom+"' and '"+fto+"') ";
				if (tipo.getKey()==1) { // Nacional
					consulta += " and b.c_country_id = 339 " ;
				} else if (tipo.getKey()==2){ // Importada
					consulta += " and b.c_country_id <> 339 " ;
				}
		consulta +=	" and a.c_order_id = b.C_order_id " +
					" and b.xx_VMR_Department_ID = c.xx_VMR_Department_ID " +
					" and b.c_country_id = d.c_country_id " +
					" and a.m_inout_id = e.m_inout_id" +
				" GROUP BY a.documentno, b.documentno, c.value, d.description, b.xx_definitivefactor, " +
					" b.xx_receptiondate, b.XX_CHECKUPDATE, b.totallines,  b.c_country_id" +
				" ORDER BY a.documentno";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String nombre = (String)bFile.getValue();
		//Colocar la extensión si no fue agregada en la ruta
		if (!nombre.substring(nombre.length()-4, nombre.length()).equals(".xls")) {
			nombre += ".xls";
		}
		
		try{
			pstmt = DB.prepareStatement(consulta, null); 
			rs = pstmt.executeQuery();
			
			// Cabeceras del XLS
			List<String> titulos = new ArrayList<String>();
			Collections.addAll(titulos,"Nro_Recepcion", "Nro_Orden", "Departamento", "Pais", "Factor_Definitivo", 
					"Fecha_Recepcion", "Fecha_Chequeo", "Cantidad_Chequeada", "Costo_Moneda_Origen");
			
			// Columnas en el XLS a redimensionar (numColumna, tamaño)
			List<Integer> sizes = new ArrayList<Integer>();
			Collections.addAll(sizes, 
									0, 15, //Nro_Recepcion
									1, 15, //Nro_Orden
									2, 15, //Departamento
									3, 15, //Pais
									4, 20, //Factor_Definitivo
									5, 20, //Fecha_Recepcion
									6, 20, //Fecha_Chequeo
									7, 22, //Cantidad_Chequeada
									8, 22); // Costo_Moneda_Origen

			// Crear archivo
			crearXLS(rs, nombre, "Consulta de Compras", titulos, sizes);
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, e1.getMessage());
			}
			log.log(Level.SEVERE, e.getMessage());
		}
		
		ADialog.info(m_WindowNo, this.mainPanel, "Consulta exportada en el archivo "+nombre);	
		
	}   //  cmd_Search
	  

	
	/** Crear un archivo xls
	 * Se crea el archivo con el contenido del reporte de acuerdo a los valores
	 * obtenidos previamente
	 * @param rs Tabla a exportar.
	 * @param nombreArchivo Nombre del archivo a generar.
	 * @param titulos Cabecera de la tabla.
	 * @param sizes Tamaños de las columnas a modificar.
	 */
	public static void crearXLS (ResultSet rs, String nombreArchivo, String nombreTab, 
								List<String> titulos, List<Integer> sizes) throws Exception {
		String nombre2 = nombreArchivo;

		File archivo = new File(nombreArchivo);
		int i = 1; 
		
		WritableWorkbook workbook = null;
		
		try {
			workbook = Workbook.createWorkbook(archivo);
		} catch (Exception e) {
			e.printStackTrace();
			while (archivo.exists()) {
				nombre2 = nombreArchivo+" ("+ ++i +")";
				archivo = new File(nombre2 +".xls");
				System.out.println("No se pudo reescribir el archivo. "+i);
			}
			workbook = Workbook.createWorkbook(archivo);
		}
		System.out.println("Archivo creado en: "+nombreArchivo);
		WritableSheet s = workbook.createSheet(nombreTab,0);
		
		// Definición del formato de las celdas cabecera 
		WritableFont boldf = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
		boldf.setColour(Colour.BLACK);
		WritableCellFormat cf1 = new WritableCellFormat(boldf);
		cf1.setBackground(Colour.GRAY_25);
		//cf1.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
		cf1.setWrap(false);
		cf1.setAlignment(Alignment.CENTRE);
		
		// Definición del formato de las celdas de contenido
		WritableFont noboldf = new WritableFont(WritableFont.ARIAL,10, WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(noboldf);
		//cf2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		cf2.setWrap(false);
		
		// Escribir el archivo
		int fila = 1;
		int row = -1;int tab = 2;
		
		while (rs.next()) { //& row <=200000) {
			if (row % 65535 == 65535 - 1) { // Nuevo tab
				s = workbook.createSheet(nombreTab + tab,tab++);
				fila = 1; 
			}
			
			if (fila == 1 || row % 65535 == 65535 - 1) { // Cabecera del nuevo tab
				// Cabecera
				for (int li=0;li<titulos.size();li++) {
					s.addCell(new Label(li, 0, titulos.get(li), cf1));
				}
				// Redimensión de algunas columnas
				for (int li=0;li<sizes.size();li++) {
					s.setColumnView(sizes.get(li++), sizes.get(li));
				}
			}
			
			//Contenido
			for (int c = 0; c<titulos.size(); c++) {
				Object obj = rs.getObject(c+1);
				if (obj == null) {
					s.addCell(new Label(c, fila, "", cf2));
				}
				else if (obj instanceof java.lang.Number) {					
					s.addCell(new Number(c, fila, rs.getDouble(c+1), cf2)); 
				}
				else {
					if (obj.toString().startsWith("'")) {
						s.addCell(new Label(c, fila, obj.toString().substring(1), cf2));
					} else {
						s.addCell(new Label(c, fila, obj.toString(), cf2));
					}
				}
			}
			
			fila++;
			row++;
			
		} // end while

		workbook.write();
		workbook.close();

	} // Fin crearXLS
	
	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		dateFrom.addActionListener(this);
		dateTo.addActionListener(this);
		comboOrderedby.addActionListener(this);

		bReset.addActionListener(this);
		bSearch.addActionListener(this);
	} //addActionListeners
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		dateFrom.removeActionListener(this);
		dateTo.removeActionListener(this);
		comboOrderedby.removeActionListener(this);

		bReset.removeActionListener(this);
		bSearch.removeActionListener(this);
	} // removeActionListeners



}
