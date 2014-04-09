package compiere.model.payments.forms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MPInstance;
import org.compiere.model.MPaySelection;
import org.compiere.model.MPaySelectionLine;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
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
import org.compiere.util.Trx;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MOrder;
import compiere.model.cds.MPayment;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;


/**
 * Forma en compiere: Planning Week Pay
 * Se encarga de buscar los socios de negocios que tienen el estado de las cuentas 
 * por pagar 'activo' y el estado del documento 'completado' para que el usuario 
 * genere el plan de la semana de pago. Dicha forma trae consigo todos los documentos
 * existentes para una orden de compra asoaciada a un socio de negocio.
 * 
 * @author Jessica Mendoza
 *
 */
public class XX_PlanPayForm extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	/****Window No****/
	private int m_WindowNo = 0;
	
	/****FormFrame****/
	private FormFrame m_frame;
	
	/****Logger****/
	static CLogger log = CLogger.getCLogger(XX_PlanPayForm.class);
	
	/****Cliente****/
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_AD_Rol_ID = Env.getCtx().getAD_Role_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	StringBuffer m_sql = null;
	StringBuffer m_sql2 = null;
	StringBuffer m_sqlWith = null;
	StringBuffer m_sqlDetail = null;
	String m_sqlUnion = "";
	String m_groupByUnion = "";
	String m_groupBy = "";
	String m_groupByWith = "";
	String m_orderBy = "";
	Ctx ctx=Env.getCtx();
	
	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private BorderLayout southLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPanePartner = new JScrollPane();
	private MiniTable xTablePartner = new MiniTable();
	private JScrollPane xScrollPaneOrder = new JScrollPane();
	private MiniTable xTableOrder = new MiniTable();
	private JScrollPane xScrollPaneDocument = new JScrollPane();
	private MiniTable xTableDocument = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private TitledBorder xBorderPartner =
		new TitledBorder(Msg.getMsg(Env.getCtx(), "XX_PlanPay"));
	private TitledBorder xBorderOrder =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_DetailPay"));
	private TitledBorder xBorderDocument =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_DocumentPay"));

	/****Botones****/
	private CButton bSearch = new CButton();
	private JButton bReset = new CButton();
	private JButton bFinal = new CButton();
	
	/****Labels-ComboBox-Fecha****/
	private CLabel categoryPartnerLabel = new CLabel();
	private CComboBox categoryPartnerCombo = new CComboBox();
	private CLabel dateFromlabel = new CLabel();
	private CLabel dateToLabel = new CLabel();
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));
	private CLabel categoryLabel = new CLabel();
	private CComboBox categoryCombo = new CComboBox();
	private CLabel typePartnerLabel = new CLabel();
	private CComboBox typePartnerCombo = new CComboBox();
	private CLabel totalPagoLabel = new CLabel();
	private CLabel montoLabel = new CLabel();
	
	/****Variables****/
	Trx trx = Trx.get("transaction");
	static Integer sync = new Integer(0); 
	Utilities util = new Utilities();
	NumberFormat formato = NumberFormat.getInstance();
	String tipoFactura = "";
	String tipoFacturaSimp = "";
	String tipoOrderNI = "";
	String tipoOrder = "";
	int idSN;
	int idOC;
	int idDoc;
	int count = 0;
	int contDoc = 1;
	BigDecimal definitiveFactor = new BigDecimal(0);
	int selectedIndexFirstTable = 0;

	@Override
	/**
	 * Inicializa la forma
	 */
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);	
		try{
			jbInit();
			dynInitFirst();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			dynInitThird();
			frame.getContentPane().add(mainPanel, BorderLayout.SOUTH);	
			frame.setResizable(false); //Se utiliza para bloquear el boton maximizar de la ventana
		}
		catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * Organiza las posiciones de cada etiqueta en la forma
	 * @throws Exception
	 */
	private void jbInit() throws Exception{
		/****Seteo de las etiquetas****/
		categoryPartnerLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CategoryPartner"));
		dateToLabel.setText(Msg.getMsg(Env.getCtx(), "DateTo"));
		dateFromlabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CategoryComercial"));
		typePartnerLabel.setText(Msg.getMsg(Env.getCtx(), "XX_OrderType"));
		totalPagoLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TotalPay"));
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearch.setEnabled(true);
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		bReset.addActionListener(this);
		bFinal.setText(Msg.getMsg(Env.getCtx(), "XX_Final"));
		bFinal.setEnabled(false);
		bFinal.addActionListener(this);

		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		xPanel.setLayout(xLayout);
		
		centerPanel.setLayout(centerLayout);
		xScrollPanePartner.setBorder(xBorderPartner);
		xScrollPanePartner.setPreferredSize(new Dimension(920, 220));
		xScrollPaneOrder.setBorder(xBorderOrder);
		xScrollPaneOrder.setPreferredSize(new Dimension(920,220));
		xPanel.setLayout(xLayout);
		
		southPanel.setLayout(southLayout);
		xScrollPaneDocument.setBorder(xBorderDocument);
		xScrollPaneDocument.setPreferredSize(new Dimension(920,220));
		xPanel.setLayout(xLayout);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xScrollPanePartner,  BorderLayout.NORTH);
		centerPanel.add(xScrollPaneOrder, BorderLayout.SOUTH);
		southPanel.add(xScrollPaneDocument, BorderLayout.CENTER);
		xScrollPanePartner.getViewport().add(xTablePartner);
		xScrollPaneOrder.getViewport().add(xTableOrder);
		xScrollPaneDocument.getViewport().add(xTableDocument);
		
		/**
		 * Panel Superior
		 */		
		/****Etiquetas de la primera fila****/
		northPanel.add(categoryPartnerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryPartnerCombo, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(dateFromlabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,	
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(dateFrom, new GridBagConstraints(3, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(dateToLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(dateTo, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,		
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));	
		
		/****Etiquetas de la segunda fila****/
		northPanel.add(categoryLabel, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(typePartnerLabel, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(typePartnerCombo, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));

		/****Etiquetas de la tercera fila****/
		northPanel.add(totalPagoLabel, new GridBagConstraints(2, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(montoLabel, new GridBagConstraints(3, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.CENTER,
				new Insets(12, 5, 5, 12), 0, 0));
		northPanel.add(bSearch, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));		
		northPanel.add(bReset, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0)); 
		northPanel.add(bFinal, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
	}

	/**
	 * Carga las categorias comerciales (moda, hogar, deporte, niños, belleza)
	 */
	void dynCategory() {
		
		if (categoryPartnerCombo.getSelectedItem() != null){
			if(((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey() != 0){
				String tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getName();
				if (tipoFactura.equals("Productos para la Venta")){
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					categoryCombo.setEditable(true);
					categoryCombo.setEnabled(true);
					categoryCombo.removeActionListener(this);
					String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME "
							+ " FROM XX_VMR_CATEGORY ";
					sql += " ORDER BY VALUE||'-'||NAME ";
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
				}else{
					categoryCombo.removeAllItems();
					categoryCombo.setEditable(false);
					categoryCombo.setEnabled(false);
				}
			}
		}
	}
	
	/**
	 * Carga los tipos de facturas (bienes, servicio, productos para la venta)
	 */
	void dynCategoryPartner(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlCategoria = "select AD_Ref_List_ID, NAME " +
		 					  "from AD_Ref_List_Trl " +
		 					  "where AD_Ref_List_ID IN ("+ Env.getCtx().getContextAsInt("#XX_L_ASSETSMPRODUCT_ID") +
		 					  "," + Env.getCtx().getContextAsInt("#XX_L_ITEMMPRODUCT_ID") +
		 					  "," + Env.getCtx().getContextAsInt("#XX_L_SERVICESMPRODUCT_ID") +
		 					  ") and AD_Language= '"+ Env.getCtx().getContext("#AD_Language") + "'";
		try {
			pstmt = DB.prepareStatement(sqlCategoria, null);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				categoryPartnerCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			categoryPartnerCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlCategoria, e);
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
		categoryPartnerCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {

			@Override
			public void contentsChanged(ListDataEvent e) {
				if(categoryPartnerCombo.getSelectedIndex()>-1){
					dynCategory();					
				}else{
					categoryCombo.removeAllItems();
					categoryCombo.setEditable(false);
					categoryCombo.setEnabled(false);
				}			
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
	 * Carga la lista que contiene los tipos de ordenes (nacional, importada)
	 */
	void dynTypePartner(){
		
		for( X_Ref_XX_OrderType v : X_Ref_XX_OrderType.values()) {
			typePartnerCombo.addItem (v.getValue());
		}	
	}

	/**
	 * Genera las columnas para la primera tabla
	 */
	private void dynInitFirst(){
		llenarCombos();
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutF = new ColumnInfo[] {
				/****0-Check****/
				new ColumnInfo("", "", IDColumn.class, false, false, ""),				
				/****1-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),				
				/****2-Total General****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalOverall"), ".", float.class),
				/****3-Moneda****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Currency_ID"), ".", KeyNamePair.class),
				/****4-Notas de Aviso****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OutstandingNotices"), ".", String.class),
				/****5-Total de las devoluciones****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ApproceForReturn"), ".", float.class),
				/****6-Total de las piezas devueltas****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalParts"), ".", int.class),			
		};
		xTablePartner.prepareTable(layoutF, "", "", true, "");
		CompiereColor.setBackground (this);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		xTablePartner.getSelectionModel().addListSelectionListener(this);		
		xTablePartner.getColumnModel().getColumn(0).setMinWidth(10);
		xTablePartner.getColumnModel().getColumn(1).setMinWidth(300);
		xTablePartner.getColumnModel().getColumn(1).setCellRenderer(renderLeft);
		xTablePartner.getColumnModel().getColumn(2).setMinWidth(140);
		xTablePartner.getColumnModel().getColumn(2).setCellRenderer(renderRight);
		xTablePartner.getColumnModel().getColumn(3).setMinWidth(90);
		xTablePartner.getColumnModel().getColumn(3).setCellRenderer(renderCenter);
		xTablePartner.getColumnModel().getColumn(4).setMinWidth(150);
		xTablePartner.getColumnModel().getColumn(4).setCellRenderer(renderCenter);
		xTablePartner.getColumnModel().getColumn(5).setMinWidth(160);	
		xTablePartner.getColumnModel().getColumn(5).setCellRenderer(renderRight);
		xTablePartner.getColumnModel().getColumn(6).setMinWidth(160);	
		xTablePartner.getColumnModel().getColumn(6).setCellRenderer(renderRight);
		bSearch.addActionListener(this);
		bReset.addActionListener(this);	
		dynInitSecond();
			
		xTablePartner.addMouseListener(new MouseListener() {
						
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			
				IDColumn columnIdPartner = (IDColumn) xTablePartner.getValueAt(xTablePartner.getSelectedRow(),0);
				idSN = columnIdPartner.getRecord_ID();
				/****Verifica si el check esta desactivado para llamar al metodo que hace el 
				 * update del estado de cuentas por pagar y modificar el monto total****/					
				if (columnIdPartner.isSelected() == false){	
						String montoOld = montoLabel.getText().replace(".","").replace(",", ".").toString();
						String montoPartner = (String) xTablePartner.getValueAt(xTablePartner.getSelectedRow(), 2);	
						montoPartner = montoPartner.replace(".", "").replace(",", ".");
						montoLabel.setText((new BigDecimal(montoOld).subtract(new BigDecimal(montoPartner))).setScale(2, RoundingMode.DOWN).toString());					
						modificarStatus(idSN,0,0,"Partner");
						tableInitF();
						if (xTableOrder.getRowCount() != 0)
							xTableOrder.setRowCount(0);
						if (xTableDocument.getRowCount() != 0)
							xTableDocument.setRowCount(0);
				}else{
					tableInitS(xTablePartner.getSelectedRow(),columnIdPartner.isSelected(),0);
					if (xTableDocument.getRowCount() != 0)
						xTableDocument.setRowCount(0);
				}
			}
		});	
	}

	/**
	 * Genera las columnas para la segunda tabla
	 */
	private void dynInitSecond(){
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutS = new ColumnInfo[] {
				/****0-Check****/
				new ColumnInfo("", "", IDColumn.class, false, false, ""),				
				/****1-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),				
				/****2-OC/Contrato****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderContract"), ".", String.class),	
				/****3-Monto****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountGeneral"), ".", float.class),
				/****4-Moneda****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Currency_ID"), ".", KeyNamePair.class),
				/****5-Factura****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Invoice_ID"), ".", KeyNamePair.class),
		};
		xTableOrder.prepareTable(layoutS, "", "", true, "");
		CompiereColor.setBackground (this);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		xTableOrder.getSelectionModel().addListSelectionListener(this);		
		xTableOrder.getColumnModel().getColumn(0).setMinWidth(110);
		xTableOrder.getColumnModel().getColumn(1).setMinWidth(300);
		xTableOrder.getColumnModel().getColumn(1).setCellRenderer(renderLeft);
		xTableOrder.getColumnModel().getColumn(2).setMinWidth(140);
		xTableOrder.getColumnModel().getColumn(2).setCellRenderer(renderCenter);
		xTableOrder.getColumnModel().getColumn(3).setMinWidth(130);
		xTableOrder.getColumnModel().getColumn(3).setCellRenderer(renderRight);
		xTableOrder.getColumnModel().getColumn(4).setMinWidth(100);
		xTableOrder.getColumnModel().getColumn(4).setCellRenderer(renderCenter);
		xTableOrder.getColumnModel().getColumn(5).setMinWidth(130);
		xTableOrder.getColumnModel().getColumn(5).setCellRenderer(renderCenter);
		
		xTableOrder.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {

				if (xTableOrder.getSelectedRow() != -1){
					IDColumn columnIdOrder = (IDColumn) xTableOrder.getValueAt(xTableOrder.getSelectedRow(),0);
					idOC = columnIdOrder.getRecord_ID();
					String contratoId = (String) xTableOrder.getValueAt(xTableOrder.getSelectedRow(),2); //posición donde se encuetra el monto general
					
					if(xTablePartner.getSelectedRow() != -1)
						selectedIndexFirstTable = xTablePartner.getSelectedRow();
					
					IDColumn columnIdPartner = (IDColumn) xTablePartner.getValueAt(selectedIndexFirstTable, 0);
					/****Verifica si el check esta desactivado para llamar al metodo que hace el 
					 * update del estado de cuentas por pagar y modificar el monto total****/					
					if (columnIdOrder.isSelected() == false){
						String montoOld = montoLabel.getText().replace(".","").replace(",", ".").toString();
						String montoOrder = (String) xTableOrder.getValueAt(xTableOrder.getSelectedRow(), 3); //posición donde se encuetra el monto general
						montoOrder = montoOrder.replace(".", "").replace(",", ".");
						montoLabel.setText((new BigDecimal(montoOld).subtract(new BigDecimal(montoOrder))).setScale(2, RoundingMode.DOWN).toString());
						if (columnIdOrder.getRecord_ID() == 0 && !contratoId.equals("0"))
							modificarStatus(columnIdPartner.getRecord_ID(), Integer.parseInt(contratoId), 0, "Contrato");
						else if(columnIdOrder.getRecord_ID()!=0)
							modificarStatus(columnIdPartner.getRecord_ID(), idOC, 0, "Order");
						else{
							KeyNamePair invoice = (KeyNamePair) xTableOrder.getValueAt(xTableOrder.getSelectedRow(),5); 
							modificarStatus(columnIdPartner.getRecord_ID(), invoice.getKey(), 0, "Single");
						}
						
						tableInitF();
						tableInitS(-1,columnIdPartner.isSelected(),idSN);
						if (xTableDocument.getRowCount() != 0)
							xTableDocument.setRowCount(0);
						
						if(xTableOrder.getRowCount()>0){
							xTablePartner.setRowSelectionInterval(selectedIndexFirstTable, selectedIndexFirstTable);
						}
						
					}else{
						if (xTableDocument.getRowCount() == 0){ //llena la tercera tabla
							tableInitT(xTableOrder.getSelectedRow(),columnIdOrder.isSelected());
						}else{
							if (idSN == columnIdPartner.getRecord_ID()){
								tableInitT(xTableOrder.getSelectedRow(),columnIdOrder.isSelected());	
							}else{
								xTableOrder.removeAll();
								xTableDocument.removeAll(); 
								//llena la segunda tabla
								if (xTableDocument.getRowCount() != 0)
									xTableDocument.setRowCount(0);
								tableInitS(xTableOrder.getSelectedRow(),columnIdPartner.isSelected(),0);
							}		
						}
					}
				}			
			}
		});
	}

	/**
	 * Genera las columnas para la tercera tabla
	 */
	private void dynInitThird(){
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutT = new ColumnInfo[] {
				/****0-Check****/
				new ColumnInfo("", "", IDColumn.class, false, false, ""),				
				/****1-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),				
				/****2-OC/Contrato****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderContract"), ".", String.class),				
				/****3-Documento****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "ArchivedDocuments"), ".", KeyNamePair.class),
				/****4-Moneda****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Currency_ID"), ".", KeyNamePair.class),
				/****5-Base****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Base"), ".", float.class),				
				/****6-IVA****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Tax"), ".", float.class),
				/****7-Retencion IVA****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WithholdingPay"), ".", float.class),
				/****8-Retencion ISLR****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RetainedAmountISLR"), ".", float.class),
				/****9-Monto****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountGeneral"), ".", float.class),
				/****10-Anticipo****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Advance"), ".", float.class),
				/****11-Fecha de entrada****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DateEntrace"), ".", String.class),
				/****12-Fecha de vencimiento****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DueDate"), ".", String.class),};
		
		xTableDocument.prepareTable(layoutT, "", "", true, "");
		CompiereColor.setBackground (this);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		xTableDocument.getSelectionModel().addListSelectionListener(this);		
		xTableDocument.getColumnModel().getColumn(0).setMinWidth(110);
		xTableDocument.getColumnModel().getColumn(1).setMinWidth(180);
		xTableDocument.getColumnModel().getColumn(1).setCellRenderer(renderLeft);
		xTableDocument.getColumnModel().getColumn(2).setMinWidth(100);
		xTableDocument.getColumnModel().getColumn(2).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(3).setMinWidth(150);
		xTableDocument.getColumnModel().getColumn(3).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(4).setMinWidth(80);
		xTableDocument.getColumnModel().getColumn(4).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(5).setMinWidth(80);
		xTableDocument.getColumnModel().getColumn(5).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(6).setMinWidth(90);
		xTableDocument.getColumnModel().getColumn(6).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(7).setMinWidth(110);
		xTableDocument.getColumnModel().getColumn(7).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(8).setMinWidth(120);
		xTableDocument.getColumnModel().getColumn(8).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(9).setMinWidth(100);
		xTableDocument.getColumnModel().getColumn(9).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(10).setMinWidth(90);
		xTableDocument.getColumnModel().getColumn(10).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(11).setMinWidth(120);
		xTableDocument.getColumnModel().getColumn(11).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(12).setMinWidth(140);
		xTableDocument.getColumnModel().getColumn(12).setCellRenderer(renderCenter);

		xTableDocument.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
	
				IDColumn columnIdDocument = (IDColumn) xTableDocument.getValueAt(xTableDocument.getSelectedRow(),0);
				idDoc = columnIdDocument.getRecord_ID();
				IDColumn columnIdPartner = (IDColumn) xTablePartner.getValueAt(xTablePartner.getSelectedRow(), 0);
				IDColumn columnIdOrder = (IDColumn) xTableOrder.getValueAt(xTableOrder.getSelectedRow(), 0);
				/****Verifica si el check esta desactivado para llamar al metodo que hace el 
				 * update del estado de cuentas por pagar y modificar el monto total****/					
				if (columnIdDocument.isSelected() == false){
					String montoOld = montoLabel.getText().replace(".","").replace(",", ".").toString();
					String montoDocument = (String) xTableDocument.getValueAt(xTableDocument.getSelectedRow(), 9); //posición donde se encuetra el monto general
					montoDocument = montoDocument.replace(".", "").replace(",", ".");
					montoLabel.setText((new BigDecimal(montoOld).subtract(new BigDecimal(montoDocument))).setScale(2, RoundingMode.DOWN).toString());
					modificarStatus(columnIdPartner.getRecord_ID(),columnIdOrder.getRecord_ID(),idDoc, "Document");
					tableInitF();
					tableInitS(-1, true,idSN);
					if (xTableDocument.getRowCount() != 0)
					xTableDocument.setRowCount(0);
				}else{
					tableInitT(xTableOrder.getSelectedRow(),columnIdOrder.isSelected());
				}
			}
		});
	}   
	
	/**
	 * Carga la primera tabla del panel central norte
	 */
	private void tableInitF(){
		xTablePartner.getSelectionModel().removeListSelectionListener(this);
		BigDecimal cuenta = new BigDecimal(0);
		m_sql = new StringBuffer();
		m_sqlWith = new StringBuffer();

		m_sql.append("with pagoOrd " +
				 "as (select sum(pay.PayAmt) totalAnt, pay.C_Order_ID ordId " +
				 "from C_Payment pay " +
				 "where pay.DocStatus = 'CO' " +
				 "and pay.XX_SynchronizationBank = 'Y' " +
				 "and pay.XX_AccountPayableStatus = 'A' " +
				 "Group by pay.C_Order_ID), " +
				 "pagoCont " +
				 "as (select sum(pay.PayAmt) totalAnt, pay.XX_Contract_ID contId " +
				 "from C_Payment pay " +
				 "where pay.DocStatus = 'CO' " +
				 "and pay.XX_SynchronizationBank = 'Y' " +
				 "and pay.XX_AccountPayableStatus = 'A' " +
				 "Group by pay.XX_Contract_ID), " +
				 "pagoFact " +
				 "as (select sum(pay.PayAmt) totalAnt, pay.C_Invoice_ID factId " +
				 "from C_Payment pay " +
				 "where pay.DocStatus = 'CO' " +
				 "and pay.XX_SynchronizationBank = 'Y' " +
				 "and pay.XX_AccountPayableStatus = 'A'" +
				 "Group by pay.C_Invoice_ID) " +
						 "select (inv.GrandTotal " +
						 "- nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0) " +
						 "- coalesce((select withholdingTaxInvoice(inv.C_Invoice_ID) from dual),0) " +
						 "- coalesce(pgO.totalAnt, 0) " +
						 "- coalesce(pgC.totalAnt,0)" +
						 "- coalesce(pgF.totalAnt,0)) as total, " +
						 "par.name, par.C_BPartner_ID, cur.C_Currency_ID, cur.ISO_CODE " +
						 "from C_Invoice inv " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "inner join C_Currency cur on (cur.C_Currency_ID = inv.C_Currency_ID) " +
						 "inner join C_BP_Status bps on (par.C_BP_Status_ID = bps.C_BP_Status_ID) " +
						 "left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
						 "left outer join XX_VMR_Department dep on (ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
						 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
						 "left outer join pagoOrd pgO on (pgO.ordId = ord.C_Order_ID) " +
						 "left outer join pagoCont pgC on (pgC.contId = con.XX_Contract_ID) " +
						 "left outer join pagoFact pgF on (pgF.factId = inv.C_Invoice_ID) " +
						 "where inv.DocStatus = 'CO' " +
						 "and inv.isSoTrx = 'N' " +
						 "and bps.name = 'Activo' " +
						 "and inv.XX_AccountPayableStatus = 'A' " +
						 "and ((ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) OR (inv.C_ORDER_ID IS NULL AND inv.XX_CONTRACT_ID IS NULL)) " +
						 "and ((ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') OR (inv.C_ORDER_ID IS NULL AND inv.XX_CONTRACT_ID IS NULL AND inv.XX_InvoicingStatusContract = 'AP')) ");
						 
		m_groupBy = "group by par.name, " +
					"par.C_BPartner_ID, " +
					"cur.C_Currency_ID, " +
					"cur.ISO_CODE, " +
					"inv.GrandTotal, " +
					"inv.C_Invoice_ID, " +
					"pgO.totalAnt, " +
					"pgC.totalAnt, " +
					"pgF.totalAnt ";

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Hace la busqueda por la fecha hasta del filtro****/
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		if (from != null){
			m_sql.append("and inv.XX_DatePaid ").append(" >= ").append(
					DB.TO_DATE(from)).append(" ");
		}
		if (to != null){
			m_sql.append("and inv.XX_DatePaid ").append(" <= ").append(
					DB.TO_DATE(to)).append(" ");
		}
		/****Buscar los proveedores segun el tipo de factura****/
		if (categoryPartnerCombo.getSelectedItem() != null){
			if(((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey() != 0){
				int tipoFacturaId = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getName();
				if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID"))){
					tipoFacturaSimp = "A";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType in ('FA','SU') ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID"))){
					tipoFacturaSimp = "S";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE' ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					tipoFacturaSimp = "I";
					tipoOrder = "and ord.XX_POType = 'POM' ";
				}
				m_sql.append("and ").append("inv.XX_InvoiceType = '").append(tipoFacturaSimp).append("' ");
			}
		}
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedItem() != null) && (categoryCombo.getSelectedIndex() != 0)){
			if (tipoFacturaSimp.equals("I")){
				if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() > 0){
					int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
					m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
				}
			}						
		}
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if (typePartnerCombo.getSelectedItem() != null){
			
			if(typePartnerCombo.getSelectedItem().equals("Nacional")){
				
				m_sql.append("and ").append("((ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).
				append("' or con.XX_ContractType like '").append(typePartnerCombo.getSelectedItem()).append("%') " +
						" or (inv.C_ORDER_ID IS NULL AND inv.XX_CONTRACT_ID IS NULL AND inv.C_CURRENCY_ID = 205))");
			}else{
			
				m_sql.append("and ").append("(ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).
					append("'  or con.XX_ContractType like '").append(typePartnerCombo.getSelectedItem()).append("%') ");
			}
		}
		
		
		
		String SQL = "select round(sum(u.total), 2), u.name, u.C_BPartner_ID, u.C_Currency_ID, u.Iso_Code " +
		   			 "from ( " + 
		   			 m_sql.toString() + m_groupBy.toString() +
		   			 ") u " +
		   			 "group by u.name, u.C_BPartner_ID, u.C_Currency_ID, u.Iso_Code " +
		   			 "order by 2 asc";
		
		int i = 0;		
		Vector<Integer> vector = new Vector<Integer>(2);
		boolean bool;
		xTablePartner.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {				
			pstmt = DB.prepareStatement(SQL, null);
			System.out.println(SQL);
			rs = pstmt.executeQuery();	
			while(rs.next()) {	
				xTablePartner.setRowCount (i+1);		
				/****Opcion Check****/
				IDColumn id = new IDColumn(rs.getInt(3));//el rs.getInt(valor) busca el id o entero que esta en la posicion "valor" en el query y lo coloca por debajo
				id.setSelected(true);
				xTablePartner.setValueAt(id, i, 0);//(valor,fila,columna)
				/****Proveedor****/
				xTablePartner.setValueAt(
					new KeyNamePair (rs.getInt("C_BPartner_ID"), rs.getString("name")),i,1);			
				/****Monto****/
				xTablePartner.setValueAt(formato.format(rs.getBigDecimal(1).setScale(2)), i, 2);
				cuenta = cuenta.add(rs.getBigDecimal(1)).setScale(2);
				/****Moneda****/
				xTablePartner.setValueAt(
						new KeyNamePair (rs.getInt("C_CURRENCY_ID"), rs.getString("ISO_CODE")), i, 3);
				/****Avisos Pendientes****/
				bool = this.verificarAvisos(rs.getInt("C_BPartner_ID"));
				if (bool == true)
					xTablePartner.setValueAt(Msg.getMsg(Env.getCtx(), "XX_DescriptionNoticies"), i, 4);
				else
					xTablePartner.setValueAt("No", i, 4);
				/****Devoluciones****/
				vector = util.totalDevolucion(rs.getInt("C_BPartner_ID"));
				if (vector.isEmpty()){
					/****Devoluciones pendientes****/
					xTablePartner.setValueAt(formato.format(new BigDecimal(0).setScale(2)), i, 5);
					/****Total de piezas devueltas****/
					xTablePartner.setValueAt(0, i, 6);
				}else{
					/****Devoluciones pendientes****/
					xTablePartner.setValueAt(formato.format(new BigDecimal(vector.get(0)).setScale(2)), i, 5);
					/****Total de piezas devueltas****/
					xTablePartner.setValueAt(vector.get(1), i, 6);
				}
				i++;
			}	
			montoLabel.setText(formato.format(cuenta)); //es la suma del total general de todos los socio de negocios
			montoLabel.setFont(new Font("Serif", Font.BOLD,12));		
		}
		catch(SQLException e){	
			e.printStackTrace();
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
		if (xTablePartner.getRowCount() > 0 && bFinal.isEnabled())
			bFinal.setEnabled(true);
		else
			bFinal.setEnabled(false);
		xTablePartner.getSelectionModel().addListSelectionListener(this);
	}
	
	/**
	 * Carga la segunda tabla del panel central sur
	 */
	public void tableInitS(int matchedRow, boolean bool, int idPartner){
		xTableOrder.getSelectionModel().removeListSelectionListener(this);
		String SQL = "";
		if (matchedRow >= 0){
			IDColumn columnId = (IDColumn) xTablePartner.getValueAt(matchedRow,0);
			SQL = ordenes(columnId.getRecord_ID());
		}else
			SQL = ordenes(idPartner);	
		int i = 0;
		xTableOrder.setRowCount(i);	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();		
			while(rs.next()) {	
				xTableOrder.setRowCount (i+1);		
				/****Opcion Check****/
				IDColumn id;
				if (rs.getString("documentNo") == null){
					id = new IDColumn(rs.getInt(8));//el rs.getInt(valor) busca el id o entero que esta en la posicion "valor" en el query y lo coloca por debajo
				}else{
					id = new IDColumn(rs.getInt(5));//el rs.getInt(valor) busca el id o entero que esta en la posicion "valor" en el query y lo coloca por debajo
				}
				id.setSelected(true);
				xTableOrder.setValueAt(id, i, 0);//(valor,fila,columna)
				/****Proveedor****/
				xTableOrder.setValueAt(
					new KeyNamePair (rs.getInt("C_BPartner_ID"), rs.getString("name")),i,1);
				/****Orden****/
				if (rs.getString("documentNo") == null){
					xTableOrder.setValueAt(String.valueOf(rs.getInt(9)), i, 2);
				}else{
					xTableOrder.setValueAt(rs.getString("documentNo"), i, 2);
				}
				/****Monto****/
				xTableOrder.setValueAt(formato.format(rs.getBigDecimal(1).setScale(2)), i, 3);
				/****Moneda****/
				xTableOrder.setValueAt(
					new KeyNamePair (rs.getInt("C_CURRENCY_ID"), rs.getString("ISO_CODE")), i, 4);
				/*** Factura ***/
				xTableOrder.setValueAt(
						new KeyNamePair (rs.getInt("INVID"), rs.getString("INVDOC")), i, 5);
				i++;				
			}		
		}
		catch(SQLException e){	
			e.printStackTrace();
		} 
		finally{

			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		xTableOrder.getSelectionModel().addListSelectionListener(this);
	}
	
	/**
	 * Carga la tercera tabla del panel sur
	 */
	private void tableInitT(int matchedRow, boolean bool){
		xTableDocument.getSelectionModel().removeListSelectionListener(this);
		IDColumn columnId = (IDColumn) xTableOrder.getValueAt(matchedRow,0);
		KeyNamePair invoice = (KeyNamePair) xTableOrder.getValueAt(matchedRow,5);
		IDColumn partner = (IDColumn) xTablePartner.getValueAt(xTablePartner.getSelectedRow(),0);
		String contratoId;
		String SQL = null;
		
		if (columnId.getRecord_ID() == 0){
			contratoId = (String) xTableOrder.getValueAt(matchedRow, 2);
			SQL = documentos(Integer.parseInt(contratoId), invoice.getKey(), partner.getRecord_ID());
		}else{
			SQL = documentos(columnId.getRecord_ID(), invoice.getKey(), partner.getRecord_ID());
		}
		int i = 0;
		xTableDocument.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();		
			while(rs.next()) {		
				xTableDocument.setRowCount (i+1);		
				/****Opcion Check****/
				IDColumn id;
				if (rs.getString(7).equals("AP Payment"))
					id = new IDColumn(rs.getInt("paymentId"));
				else
					id = new IDColumn(rs.getInt("invoiceId"));
				id.setSelected(bool);
				xTableDocument.setValueAt(id, i, 0);//(valor,fila,columna)
				/****Proveedor****/
				xTableDocument.setValueAt(
					new KeyNamePair (rs.getInt("C_BPartner_ID"), rs.getString(2)),i,1);
				/****Orden/Contrato****/
				if (rs.getString(4) == null)
					xTableDocument.setValueAt(String.valueOf(rs.getInt(19)), i, 2);
				else
					xTableDocument.setValueAt(rs.getString(4), i, 2);
				/****Documento****/
				if (rs.getString(7).equals("AP Payment")){
					xTableDocument.setValueAt(
							new KeyNamePair (rs.getInt("C_DocType_ID"), "Anticipo: "+rs.getString(16)), i, 3);
				}else{
					xTableDocument.setValueAt(
							new KeyNamePair (rs.getInt("C_DocType_ID"), rs.getString(7)+": "+rs.getString(16)), i, 3);
				}				
				/****Moneda****/
				xTableDocument.setValueAt(
						new KeyNamePair (rs.getInt("C_CURRENCY_ID"), rs.getString("ISO_CODE")), i, 4);
				/****Base****/
				xTableDocument.setValueAt(formato.format(rs.getBigDecimal("TotalLines").setScale(2)), i, 5);			
				/****IVA****/
				xTableDocument.setValueAt(formato.format(rs.getBigDecimal("XX_TaxAmount").setScale(2)), i, 6);
				/****Retencion IVA****/
				if (rs.getString(7).equals("AP Payment"))
					xTableDocument.setValueAt(formato.format(new BigDecimal(0).setScale(2)), i, 7);
				else{
					if (typePartnerCombo.getSelectedItem().equals("Nacional")){
						xTableDocument.setValueAt(formato.format(new BigDecimal(
								util.retencion(rs.getInt("invoiceId"))).setScale(2, RoundingMode.HALF_UP)), i, 7);
					}else{
						xTableDocument.setValueAt(formato.format(new BigDecimal(0).setScale(2)), i, 7);
					}
				}			
				/****Retencion ISLR****/
				xTableDocument.setValueAt(formato.format(rs.getBigDecimal("RETAINEDISLR").setScale(2)), i, 8);		
				/****Monto****/
				if (rs.getString(7).equals("AP Payment"))
					xTableDocument.setValueAt(formato.format(new BigDecimal(0).setScale(2)), i, 9);				
				else
					xTableDocument.setValueAt(formato.format(rs.getBigDecimal(1).setScale(2)), i, 9);
				/****Anticipo****/
				if (rs.getString(7).equals("AP Payment"))
					xTableDocument.setValueAt(formato.format(rs.getBigDecimal(1).setScale(2)), i, 10);
				else 
					xTableDocument.setValueAt(formato.format(new BigDecimal(0).setScale(2)), i, 10);
				/****Fecha de entrada****/
				if (rs.getString(7).equals("AP Payment"))
					xTableDocument.setValueAt("", i, 11);
				else
					xTableDocument.setValueAt(rs.getString(12), i, 11);
				/****Fecha de vencimiento****/
				if (rs.getString(7).equals("AP Payment"))
					xTableDocument.setValueAt("", i, 12);
				else
					xTableDocument.setValueAt(rs.getString(13), i, 12);
				i++;				
			}
		}
		catch(SQLException e){	
			e.printStackTrace();	
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		xTableDocument.getSelectionModel().addListSelectionListener(this);
	}

	/**
	 *  Ejecuta el metodo correspondiente a la accion
	 *  @param e La acccion del evento
	 */
	public void actionPerformed (ActionEvent e){			
		if (e.getSource() == bSearch){
			cmdSearch();
		}else if (e.getSource() == bReset){
			removeItemsToReset();			
		}else if (e.getSource() == bFinal){
			cmdFinal();
		}
	}

	/**
	 *  Busca la informacion a través de los filtros 
	 *  arreglar
	 */
	private void cmdSearch(){
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		if (from != null && to != null){
			bFinal.setEnabled(false);
		}else if (from == null && to != null){ 
			if ((m_AD_Rol_ID == Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID")) ||
					(m_AD_Rol_ID == Env.getCtx().getContextAsInt("#XX_L_ROLECOORDAPAYABLE_ID")))
				bFinal.setEnabled(true);	
		}
		montoLabel.setText(formato.format(new BigDecimal(0)));
		xTableOrder.setRowCount(0);
		xTableDocument.setRowCount(0);
		tableInitF();
	}

	/**
	 *  Remueve los items de todos los campos filtros
	 */
	private void removeItemsToReset(){
		categoryPartnerCombo.removeAllItems();
		categoryCombo.removeAllItems();
		typePartnerCombo.removeAllItems();
		xTablePartner.setRowCount(0);
		xTableOrder.setRowCount(0);
		xTableDocument.setRowCount(0);
		llenarCombos();
	}

	/**
	 * Genera los pagos en Seleccion de Pago y Payment para el conjunto de facturas asociadas a un socio de negocio.
	 */
	private void cmdFinal(){
		
		if(trx.toString().contains("null")){
			trx = Trx.get("transaction");
		}
		
		ArrayList<Integer> listPay = new ArrayList<Integer>();
		int cantPartner = xTablePartner.getRowCount();
		//int idPago = 0;
		IDColumn partner;	
		Boolean bool = false;
		//KeyNamePair moneda;
		Timestamp to = (Timestamp) dateTo.getValue();
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(to);
		tipoOrderNI = typePartnerCombo.getSelectedItem().toString();

		//Valida que el día de la fecha hasta, sea jueves
		//Por solicitud de Miguel Yanes se requiere hacer planificación de pago cualquier día no solo el jueves
		//if (diaSemana(to.getDay()).toUpperCase().equals(Env.getCtx().get("#XX_L_DAYWEEKPAY").toUpperCase())){
			if (((categoryCombo.getSelectedItem() == null) && (categoryCombo.getSelectedIndex() == -1)) 
				|| (categoryCombo.getSelectedIndex() == 0)){
					
				for (int p = 0; p < cantPartner; p++){
					partner = (IDColumn) xTablePartner.getValueAt(p, 0);					
					//Valida que si el tipo de documento asociado al proveedor es un anticipo 
					//no lo coloque como parte de la semana de pago
					if ((tipoFacturaSimp.equals("A")) || (tipoFacturaSimp.equals("I"))){
						int id = selectionPay(to,partner.getRecord_ID());
						if (id != 0){
							listPay.add(id);
							trx = Trx.get("transaction");
							bool = true;
						}
					}else{	//tipo de factura -> Servicio
						String checkPay = checkPagadoInvoice(partner.getRecord_ID());
						if (checkPay.equals("N")){
							int id = selectionPay(to,partner.getRecord_ID());
							if (id != 0){
								listPay.add(id);
								trx = Trx.get("transaction");
								bool = true;
							}
						}else{	//tipo de factura -> Servicio y checkPay -> Y			
							listPay = servicios(to,partner.getRecord_ID());
							if (listPay != null)
								bool = true;
						}			
					}
					if (bool){
						trx.commit();
						
					}else{
						bool = false;
						trx.rollback();
					}
				}
				if (bool){
					//Modifica las cuentas por pagar y anticipos que se encuentran suspendidos a activos
					cambioStatusDocument(0,"A","",tipoFacturaSimp);
					cambioStatusDocument(0,"A","Anticipo","");
					trx.commit();
				}

				int mes = calTo.get(Calendar.MONTH);
				ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_FinalProcess1"),
						calTo.get(Calendar.DATE)+"-"+(mes+1)+"-"+calTo.get(Calendar.YEAR) 
						+ Msg.getMsg(Env.getCtx(), "XX_FinalProcess2") + montoLabel.getText());			
			}else{
				/*System.out.println("Para generar el definitivo de la semana de pago, " +
						"la categoría comercial no puede tener valor");*/
				ADialog.error(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_CategoryError"));
			}
		//}else{
			//System.out.println("El día de la Fecha Hasta, para generar el definitivo, tiene que ser jueves");
	//		ADialog.error(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_DateToError"));
	//	}
		if (bool){
			
			//Se imprime el documento definitivo
			reportDefinitive(to);
			
			//Dialogo para permitir la impresion del documento
			ADialog.info(1, new Container(), "Verifique el documento definitivo");
			
			//Se imprime el documento de especificacion de pago
			reportPaymentDetail(to);
			
			//Dialogo para permitir la impresion del documento
			ADialog.info(1, new Container(), "Verifique el documento de Especificacion de Pago");

			//Se setean en isPrinted los PaySelections
			String payIsPrinted = "update C_PaySelection " +
					     		  "set isPrinted = 'Y' " +
					     		  "where C_PaySelection_ID IN (";
			
			for(int i=0; i < listPay.size(); i++){
				
				if(i<(listPay.size()-1))
					payIsPrinted += listPay.get(i) + ", ";
				else
					payIsPrinted += listPay.get(listPay.size()-1) + ")";
			}
			
			DB.executeUpdate( null, payIsPrinted);
			
			//Llama a la interfaz de pago para que envie el documento al centralizado
			interfaceOrderPay(listPay);
		}
	}
	
	/**
	 * Busca las ordenes de compra asociadas a un socio de negocio
	 * @param idPartner
	 * @return
	 */
	public String ordenes(int idPartner){
		m_sql = new StringBuffer();
		m_sqlWith = new StringBuffer();
		String tipoOrder = "";
		
		m_sql.append("with pagoOrd " +
					 "as (select sum(pay.PayAmt) totalAnt, pay.C_Order_ID ordId " +
					 "from C_Payment pay " +
					 "where pay.DocStatus = 'CO' " +
					 "and pay.XX_SynchronizationBank = 'Y' " +
					 "and pay.XX_AccountPayableStatus = 'A' " +
					 "Group by pay.C_Order_ID), " +
					 "pagoCont " +
					 "as (select sum(pay.PayAmt) totalAnt, pay.XX_Contract_ID contId " +
					 "from C_Payment pay " +
					 "where pay.DocStatus = 'CO' " +
					 "and pay.XX_SynchronizationBank = 'Y' " +
					 "and pay.XX_AccountPayableStatus = 'A' " +
					 "Group by pay.XX_Contract_ID), " +
					 "pagoFact " +
					 "as (select sum(pay.PayAmt) totalAnt, pay.C_Invoice_ID factId " +
					 "from C_Payment pay " +
					 "where pay.DocStatus = 'CO' " +
					 "and pay.XX_SynchronizationBank = 'Y' " +
					 "and pay.XX_AccountPayableStatus = 'A'" +
					 "Group by pay.C_Invoice_ID) " +
						 "select (inv.GrandTotal " +
						 "- nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID), 0) " +
						 "- coalesce((select WithholdingTaxInvoice (inv.C_Invoice_ID) from dual),0)" +
						 "- coalesce(pgO.totalAnt,0) " +
						 "- coalesce(pgC.totalAnt,0)" +
						 "- coalesce(pgF.totalAnt,0)) as total, " +
						 "par.name, par.C_BPartner_ID, ord.documentNo, ord.C_Order_ID, cur.C_Currency_ID, " +
						 "cur.ISO_CODE, inv.XX_Contract_ID " +
						 "from C_Invoice inv " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "inner join C_Currency cur on (cur.C_Currency_ID = inv.C_Currency_ID) " +
						 "left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
						 "left outer join XX_VMR_Department dep on (ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
						 "inner join C_BP_Status bps on (par.C_BP_Status_ID = bps.C_BP_Status_ID) " +
						 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
						 "left outer join pagoOrd pgO on (pgO.ordId = ord.C_Order_ID) " +
						 "left outer join pagoCont pgC on (pgC.contId = con.XX_Contract_ID) " +
						 "left outer join pagoFact pgF on (pgF.factId = inv.C_Invoice_ID) " +
						 "where inv.DocStatus = 'CO' " +
						 "and inv.isSoTrx = 'N' " +
						 "and  bps.name = 'Activo' " +
						 "and inv.XX_AccountPayableStatus = 'A' " +
						 "and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) " +
						 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') " +
						 "and par.C_BPartner_ID = ").append(idPartner).append(" ");
		m_groupBy = "group by par.name, " +
					"par.C_BPartner_ID, " +
					"ord.documentNo, " +
					"ord.C_Order_ID, " +
					"cur.C_Currency_ID, " +
					"cur.ISO_CODE, " +
					"inv.XX_Contract_ID, " +
					"inv.GrandTotal, " +
					"pgO.totalAnt, " +
					"pgC.totalAnt, " +
					"pgF.totalAnt, C_Invoice_ID ";

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Hace la busqueda por la fecha hasta del filtro****/
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		if (from != null){
			m_sql.append("and inv.XX_DatePaid ").append(" >= ").append(DB.TO_DATE(from)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(from) + " ";
		}
		if (to != null){
			m_sql.append("and inv.XX_DatePaid ").append(" <= ").append(DB.TO_DATE(to)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(to) + " ";
		}
		/****Buscar los proveedores segun el tipo de factura****/
		if (categoryPartnerCombo.getSelectedItem() != null){
			if(((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey() != 0){
				int tipoFacturaId = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getName();
				if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID"))){
					tipoFacturaSimp = "A";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType in ('FA','SU') ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID"))){
					tipoFacturaSimp = "S";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE' ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					tipoFacturaSimp = "I";
					tipoOrder = "and ord.XX_POType = 'POM' ";
				}
				m_sql.append("and ").append("inv.XX_InvoiceType = '").append(tipoFacturaSimp).append("' ");
				m_sqlUnion = m_sqlUnion + tipoOrder;
			}
		}
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedItem() != null) && (categoryCombo.getSelectedIndex() != 0)){
			if (categoryPartnerCombo.getSelectedItem() != null){
				if (tipoFacturaSimp.equals("I")){
					if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() != 0){
						int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
						m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
					}
				}
			}			
		}
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if (typePartnerCombo.getSelectedItem() != null){
			
			if(typePartnerCombo.getSelectedItem().equals("Nacional")){
				
				m_sql.append("and ").append("(ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).
				append("' or con.XX_ContractType like '").append(typePartnerCombo.getSelectedItem()).append("%') " +
						"");

				m_sqlUnion = m_sqlUnion + "and ord.XX_OrderType = '" + typePartnerCombo.getSelectedItem() + "' ";
				
			}else{
			
				m_sql.append("and ").append("(ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).
					append("'  or con.XX_ContractType like '").append(typePartnerCombo.getSelectedItem()).append("%') ");
				m_sqlUnion = m_sqlUnion + "and ord.XX_OrderType = '" + typePartnerCombo.getSelectedItem() + "' ";
			}
		}
		
		
		String firstPart= "select round(sum(u.total),2), u.name, u.C_BPartner_ID, u.documentNo, u.C_Order_ID, " +
						   "u.C_Currency_ID, u.Iso_Code, u.XX_Contract_ID, " +
						   "(select value from XX_Contract cont where cont.XX_Contract_ID = u.XX_Contract_ID) condoc, " +
						   "NULL invID, NULL invDOC " +
						   "from ( " + 
						   m_sql.toString() + m_groupBy.toString() + 
						   " ) u " +
						   "group by u.name, u.C_BPartner_ID, u.documentNo, u.C_Order_ID, u.C_Currency_ID, " +
						   "u.Iso_Code, u.XX_Contract_ID ";
						  // "order by 4 asc ";
		
		
		//SECOND PART (UNION)
		m_sql = new StringBuffer();
		m_sqlWith = new StringBuffer();
		m_groupBy = "";
		
		m_sql.append("with pagoOrd " +
				 "as (select sum(pay.PayAmt) totalAnt, pay.C_Order_ID ordId " +
				 "from C_Payment pay " +
				 "where pay.DocStatus = 'CO' " +
				 "and pay.XX_SynchronizationBank = 'Y' " +
				 "and pay.XX_AccountPayableStatus = 'A' " +
				 "Group by pay.C_Order_ID), " +
				 "pagoCont " +
				 "as (select sum(pay.PayAmt) totalAnt, pay.XX_Contract_ID contId " +
				 "from C_Payment pay " +
				 "where pay.DocStatus = 'CO' " +
				 "and pay.XX_SynchronizationBank = 'Y' " +
				 "and pay.XX_AccountPayableStatus = 'A' " +
				 "Group by pay.XX_Contract_ID), " +
				 "pagoFact " +
				 "as (select sum(pay.PayAmt) totalAnt, pay.C_Invoice_ID factId " +
				 "from C_Payment pay " +
				 "where pay.DocStatus = 'CO' " +
				 "and pay.XX_SynchronizationBank = 'Y' " +
				 "and pay.XX_AccountPayableStatus = 'A'" +
				 "Group by pay.C_Invoice_ID) " +
						 "select (inv.GrandTotal " +
						 "- nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID), 0) " +
						 "- coalesce((select WithholdingTaxInvoice (inv.C_Invoice_ID) from dual),0)" +
						 "- coalesce(pgO.totalAnt,0) " +
						 "- coalesce(pgC.totalAnt,0)" +
						 "- coalesce(pgF.totalAnt,0)) as total, " +
						 "par.name, par.C_BPartner_ID, ord.documentNo, ord.C_Order_ID, cur.C_Currency_ID, " +
						 "cur.ISO_CODE, inv.XX_Contract_ID, C_Invoice_ID, inv.DocumentNo invDoc " +
						 "from C_Invoice inv " +
						 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
						 "inner join C_Currency cur on (cur.C_Currency_ID = inv.C_Currency_ID) " +
						 "left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
						 "left outer join XX_VMR_Department dep on (ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
						 "inner join C_BP_Status bps on (par.C_BP_Status_ID = bps.C_BP_Status_ID) " +
						 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
						 "left outer join pagoOrd pgO on (pgO.ordId = ord.C_Order_ID) " +
						 "left outer join pagoCont pgC on (pgC.contId = con.XX_Contract_ID) " +
						 "left outer join pagoFact pgF on (pgF.factId = inv.C_Invoice_ID) " +
						 "where inv.DocStatus = 'CO' " +
						 "and inv.isSoTrx = 'N' " +
						 "and  bps.name = 'Activo' " +
						 "and inv.XX_AccountPayableStatus = 'A' " +
						 "and (inv.C_ORDER_ID IS NULL AND inv.XX_CONTRACT_ID IS NULL) " +
						 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') " +
						 "and par.C_BPartner_ID = ").append(idPartner).append(" ");
		m_groupBy = "group by par.name, " +
					"par.C_BPartner_ID, " +
					"ord.documentNo, " +
					"ord.C_Order_ID, " +
					"cur.C_Currency_ID, " +
					"cur.ISO_CODE, " +
					"inv.XX_Contract_ID, " +
					"inv.GrandTotal, " +
					"pgO.totalAnt, " +
					"pgC.totalAnt, " +
					"pgF.totalAnt, C_Invoice_ID, inv.Documentno ";

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Hace la busqueda por la fecha hasta del filtro****/
		to = (Timestamp) dateTo.getValue();
		from = (Timestamp) dateFrom.getValue();
		if (from != null){
			m_sql.append("and inv.XX_DatePaid ").append(" >= ").append(DB.TO_DATE(from)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(from) + " ";
		}
		if (to != null){
			m_sql.append("and inv.XX_DatePaid ").append(" <= ").append(DB.TO_DATE(to)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(to) + " ";
		}
		/****Buscar los proveedores segun el tipo de factura****/
		if (categoryPartnerCombo.getSelectedItem() != null){
			if(((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey() != 0){
				int tipoFacturaId = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getName();
				if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID"))){
					tipoFacturaSimp = "A";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType in ('FA','SU') ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID"))){
					tipoFacturaSimp = "S";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE' ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					tipoFacturaSimp = "I";
					tipoOrder = "and ord.XX_POType = 'POM' ";
				}
				m_sql.append("and ").append("inv.XX_InvoiceType = '").append(tipoFacturaSimp).append("' ");
				m_sqlUnion = m_sqlUnion + tipoOrder;
			}
		}
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedItem() != null) && (categoryCombo.getSelectedIndex() != 0)){
			if (categoryPartnerCombo.getSelectedItem() != null){
				if (tipoFacturaSimp.equals("I")){
					if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() != 0){
						int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
						m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
					}
				}
			}			
		}
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if (typePartnerCombo.getSelectedItem() != null){
			
			if(typePartnerCombo.getSelectedItem().equals("Nacional")){
				
				m_sql.append("and inv.C_CURRENCY_ID = 205 ");
			}else{
			
				m_sql.append("and inv.C_CURRENCY_ID <> 205 ");
			}
		}
		
		
		String secondPart= "UNION " +
						   "select round(sum(u.total),2), u.name, u.C_BPartner_ID, u.documentNo, u.C_Order_ID, " +
						   "u.C_Currency_ID, u.Iso_Code, u.XX_Contract_ID, " +
						   "(select value from XX_Contract cont where cont.XX_Contract_ID = u.XX_Contract_ID) condoc, " +
						   "C_Invoice_ID invID, invDoc invDOC " +
						   "from ( " + 
						   m_sql.toString() + m_groupBy.toString() + 
						   " ) u " +
						   "group by u.name, u.C_BPartner_ID, u.documentNo, u.C_Order_ID, u.C_Currency_ID, " +
						   "u.Iso_Code, u.XX_Contract_ID, u.C_Invoice_ID, invDoc ";
						  // "order by 4 asc ";
		
		String finalSQL = firstPart + secondPart;
		
		return finalSQL;
	}
	
	/**
	 * Busca los documentos asociados a una orden de compra especifica
	 * @param idOrderContract
	 * @return
	 */
	public String documentos(int idOrderContract, int invoice, int partner){
		m_sql = new StringBuffer();

		m_sql.append("select " +
				"round((inv.GrandTotal - " +
				"nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0) - " +
				"nvl((select WithholdingTaxInvoice(inv.C_Invoice_ID) from dual),0)),2) GrandTotal, " +
				"par.name, par.C_BPartner_ID, ord.documentNo, ord.C_Order_ID, doc.C_DocType_ID, " +
				"doc.name, cur.C_Currency_ID, cur.ISO_CODE, round(inv.TotalLines,2) TotalLines, round(inv.XX_TaxAmount,2) XX_TaxAmount, " +
				"to_char(inv.DateInvoiced,'dd/mm/yyyy'),to_char(inv.XX_DueDate,'dd/mm/yyyy'), " +
				"inv.C_Invoice_ID invoiceId, con.XX_Contract_ID ContractPayment, inv.DocumentNo, " +
				"round((select nvl(sum(p.xx_retainedamount),0) from XX_VCN_ISLRAMOUNT p where p.c_invoice_id = inv.c_invoice_id),2) RETAINEDISLR, " +
				"0 paymentId, con.value " +
				"from C_Invoice inv " +
				"inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
				"left outer join C_Currency cur on (cur.C_Currency_ID = inv.C_Currency_ID ) " +
				"left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
				 "left outer join XX_VMR_Department dep on (ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
				"left outer join C_DocType doc on (doc.C_DocType_ID = inv.C_DocTypeTarget_ID) " +
				"left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
				"where inv.DocStatus = 'CO' " +
				"and inv.isSoTrx = 'N' " +
				"and inv.XX_AccountPayableStatus = 'A' AND inv.C_BPartner_ID = " + partner + " ");
				
				if(idOrderContract!=0){
					m_sql.append("and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null or inv.C_Invoice_ID is not null) " +
					"and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') " +
					"and (ord.C_Order_ID = " + idOrderContract + " or inv.XX_Contract_ID = " + idOrderContract + ") ");
				}else
				{
					m_sql.append("AND inv.C_INVOICE_ID = "+ invoice +" ");
				}
		
		m_sqlUnion = "union " +
					 "select round(pay.PayAmt * (-1),2) GrandTotal, par.name, par.C_BPartner_ID, ord.documentNo, " +
					 "ord.C_Order_ID, doc.C_DocType_ID, doc.name, cur.C_Currency_ID, cur.ISO_CODE, " +
					 "pay.DiscountAmt, pay.DiscountAmt, TO_CHAR (pay.DateTrx, 'dd/mm/yyyy'), " +
					 "TO_CHAR (pay.DateTrx, 'dd/mm/yyyy'), inv.C_Invoice_ID invoiceId, pay.XX_Contract_ID ContractPayment, " +
					 "pay.DocumentNo, pay.DiscountAmt RETAINEDISLR, pay.C_Payment_ID paymentId, con.value " +
					 "from C_Payment pay " +
					 "inner join C_BPartner par on (pay.C_BPartner_ID = par.C_BPartner_ID) " +
					 "inner join C_Currency cur on (cur.C_Currency_ID = pay.C_Currency_ID) " +
					 "left outer join C_Order ord on (ord.C_Order_ID = pay.C_Order_ID) " +
					 "left outer join XX_Contract con on (con.XX_Contract_ID = pay.XX_Contract_ID) " +
					 "inner join C_DocType doc on (doc.C_DocType_ID = pay.C_DocType_ID) " +
					 "inner join C_Invoice inv on (inv.C_Order_ID = pay.C_Order_ID or inv.XX_Contract_ID = pay.XX_Contract_ID or inv.C_Invoice_ID = pay.C_Invoice_ID ) " +
					 "where pay.DocStatus = 'CO' and inv.DocStatus = 'CO' " +
					 "and pay.XX_IsAdvance = 'Y' " +
					 "and pay.XX_AccountPayableStatus = 'A' " +
					 "and (inv.C_Order_ID = " + idOrderContract + " or con.XX_Contract_ID = " + idOrderContract + " or  pay.C_INVOICE_ID = "+ invoice +") " +
					 "and pay.XX_SynchronizationBank = 'Y' " +
					 "and inv.XX_AccountPayableStatus = 'A' " +
					 "and inv.C_DocTypeTarget_ID = "+Env.getCtx().getContext("#XX_C_DOCTYPE_ID");

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Hace la busqueda por la fecha hasta del filtro****/
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		if (from != null){
			m_sql.append("and inv.XX_DatePaid ").append(" >= ").append(DB.TO_DATE(from)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(from) + " ";
		}
		if (to != null){
			m_sql.append("and inv.XX_DatePaid ").append(" <= ").append(DB.TO_DATE(to)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(to) + " ";
		}
		/****Buscar los proveedores segun el tipo de factura****/
		if (categoryPartnerCombo.getSelectedItem() != null){
			if(((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey() != 0){
				int tipoFacturaId = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getName();
				if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID"))){
					tipoFacturaSimp = "A";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType in ('FA','SU') ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID"))){
					tipoFacturaSimp = "S";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE' ";
				}else if (tipoFacturaId == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					tipoFacturaSimp = "I";
					tipoOrder = "and ord.XX_POType = 'POM' ";
				}
				m_sql.append("and ").append("inv.XX_InvoiceType = '").append(tipoFacturaSimp).append("' ");
				//m_sqlUnion = m_sqlUnion + tipoOrder;
			}
		}
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedItem() != null) && (categoryCombo.getSelectedIndex() != 0)){
			if (categoryPartnerCombo.getSelectedItem() != null){
				if (tipoFacturaSimp.equals("I")){
					if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() != 0){
						int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
						m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
					}
				}
			}		
		}
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if (typePartnerCombo.getSelectedItem() != null && idOrderContract!=0){
			m_sql.append("and ").append("(ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).
				append("'  or con.XX_ContractType like '").append(typePartnerCombo.getSelectedItem()).append("%') ");
			//m_sqlUnion = m_sqlUnion + "and ord.XX_OrderType = '" + typePartnerCombo.getSelectedItem() + "' ";
		}
		
		m_orderBy = "order by GrandTotal desc ";
		System.out.println(m_sql.toString() + m_sqlUnion + m_orderBy);
		return m_sql.toString() + m_sqlUnion + m_orderBy;
	}
	
	/**
	 * Verifica si el Socio de Negocio tien algun aviso pendiente
	 * @param id C_BPartner_ID
	 * @return
	 */
	public boolean verificarAvisos(int id){
		boolean bool = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		String sqlNotices = "select cre.XX_CreditNotifyReturn_ID " +		
							"from XX_CreditNotifyReturn cre, C_Order ord " + 
							"where cre.XX_Status = 'ACT' " +
							"and cre.C_Order_ID = ord.C_Order_ID " +
							"and ord.C_BPartner_ID = "+id;
		try {	
			pstmt = DB.prepareStatement(sqlNotices, null);
			rs = pstmt.executeQuery();			
			if(rs.next()){
				bool = true;
				return bool;
			}
					
		}
		catch(SQLException e){	
			e.getMessage();			
		} finally{
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return bool;
	}
	
	/**
	 * Busca el nombre del día de la semana a partir del día
	 * @param dia
	 * @return
	 */
	public String diaSemana(int dia){
		String semana = "";
		switch(dia){
			case 0: semana = "Domingo"; break;
			case 1: semana = "Lunes"; break;
			case 2: semana = "Martes"; break;
			case 3: semana = "Miercoles"; break;
			case 4: semana = "Jueves"; break;
			case 5: semana = "Viernes"; break;
			case 6: semana = "Sábado"; break;
		}
		return semana;
	}

	/**
	 * Genera el reporte del definitivo de la semana de pago, 
	 * para los diferentes tipo de factura y tipo de orden
	 * @param to fecha hasta
	 */
	public void reportDefinitive(Timestamp to){ 
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(to);
		String designName = "DefinitiveLive";
		String tipoF = "";
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		int mes = calTo.get(Calendar.MONTH);
		mes=mes+1;
		
		if (tipoFacturaSimp.equals("I"))
			tipoF = "Mercancia";
		else
			tipoF = tipoFactura;
		
		//Agregar parametro
		myReport.parameterName.add("fechaHasta");
		myReport.parameterValue.add((calTo.get(Calendar.DATE)+"/"+(String.format("%02d", mes))+"/"+calTo.get(Calendar.YEAR)).toString());
		myReport.parameterName.add("tipoFactura");
		myReport.parameterValue.add(tipoF);
		myReport.parameterName.add("tipoOrder");
		myReport.parameterValue.add(tipoOrderNI.toString());
		myReport.parameterName.add("tipofacturaTemp");
		myReport.parameterValue.add(tipoFacturaSimp.toString());
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	/**
	 * Genera el pago en seleccion de pago, sus lineas y el pago generado, para los 
	 * tipos de factura de servicios, y que están chequeados como pagados  
	 * Además alimenta el monto definitivo en la orden de compra asociada al socio de negocio
	 * @param to
	 * @param idPartner
	 */
	public ArrayList<Integer> servicios(Timestamp to, int idPartner){
		boolean bool = false;
		MOrder order;
		ArrayList<Integer> listPay = new ArrayList<Integer>();
		ArrayList<Integer> vacio = new ArrayList<Integer>();
		/****Se buscan las ordenes de compra asociadas al socio de negocio****/
		int lineas = 1;
		String sqlOrder = ordenes(idPartner);
		PreparedStatement pstmtOrder = null;
		ResultSet rsOrder = null;
		try {	
			pstmtOrder = DB.prepareStatement(sqlOrder, null);
			rsOrder = pstmtOrder.executeQuery();			
			while(rsOrder.next()) {	
				IDColumn idOrder = new IDColumn(rsOrder.getInt(5));
				String sqlDocument = "";
				
				int invoice =  rsOrder.getInt(10);
				
				/****Se buscan los documentos asociados a una orden de compra****/
				if (idOrder.getRecord_ID() == 0)
					sqlDocument = documentos(rsOrder.getInt(8), invoice, idPartner);
				else
					sqlDocument = documentos(idOrder.getRecord_ID(), invoice, idPartner);
				PreparedStatement pstmtDocument = null;
				ResultSet rsDocument = null;
				try {	
					pstmtDocument = DB.prepareStatement(sqlDocument, null);
					rsDocument = pstmtDocument.executeQuery();			
					while(rsDocument.next()) {	
						int idPagoSeleccion = generarPaySelection(to,idPartner);
						//generarPaySelectionLine(idPagoSeleccion,idDocument.getRecord_ID(),idPartner,idOrder.getRecord_ID(),idInvoice,lineas,rsOrder.getInt(8));
						selectionPayLine(idPagoSeleccion, lineas, rsDocument);
						procesarCrearPagos(idPagoSeleccion);
						listPay.add(idPagoSeleccion);
						/*int moneda = rsDocument.getInt(8);
						MPaySelection paySelection = new MPaySelection(Env.getCtx(),idPagoSeleccion,null);
						int idPago = paymentServiciesPayInvoice(idPartner,idOrder.getRecord_ID(),rsDocument.getInt("C_Invoice_ID"),
								to,paySelection.getTotalAmt(),moneda);
						int idCheck = setPaymentCheck(idPagoSeleccion);
						MPaySelectionCheck check = new MPaySelectionCheck(Env.getCtx(),idCheck,null);
						check.setC_Payment_ID(idPago);
						check.setC_BPartner_ID(idPartner);
						check.setPaymentRule("S");
						check.save();*/
						lineas++;
					}
					bool = true;
				}
				catch(SQLException e){
					e.getMessage();
				} finally{
					rsDocument.close();
					pstmtDocument.close();
				}
				
				/****Modifica el monto definitivo en la orden de compra****/
				if (typePartnerCombo.getSelectedItem().equals("Importada")){
					order = new MOrder(Env.getCtx(),idOrder.getRecord_ID(),null); //objeto order
					BigDecimal montoOrderTotal = rsOrder.getBigDecimal(1);
					order.setXX_INVOCECANCEAMOUNT(order.getXX_INVOCECANCEAMOUNT().add(montoOrderTotal));	
					order.save();
				}
			}
			bool = true;
		}
		catch(SQLException e){	
			e.getMessage();
		} finally{
			try {
				rsOrder.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmtOrder.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		if (bool)
			return listPay;
		else
			return vacio;
	}
	
	/**
	 * Genera el pago en seleccion de pago, sus lineas y el pago generado, 
	 * para los tipos de factura de bienes y mercancia
	 * Además alimenta el monto definitivo en la orden de compra asociada al socio de negocio
	 * @param to
	 * @param idPartner
	 * @return el id de la seleccion de pago
	 */
	public int selectionPay(Timestamp to, int idPartner){
		boolean bool = false;
		MOrder order;
		int idPago = generarPaySelection(to,idPartner);
		/****Se buscan las ordenes de compra asociadas al socio de negocio****/
		int lineas = 1;
		String sqlOrder = ordenes(idPartner);
		PreparedStatement pstmtOrder = null;
		ResultSet rsOrder = null;	
		try {	
			pstmtOrder = DB.prepareStatement(sqlOrder, null);
			rsOrder = pstmtOrder.executeQuery();			
			while(rsOrder.next()) {	
				IDColumn idOrder = new IDColumn(rsOrder.getInt(5));
				/****Se buscan los documentos asociados a una orden de compra****/
				String sqlDocument;
				int invoice =  rsOrder.getInt(10);
				
				if (idOrder.getRecord_ID() == 0)
					sqlDocument = documentos(rsOrder.getInt(8), invoice, idPartner);
				else
					sqlDocument = documentos(idOrder.getRecord_ID(), invoice, idPartner);
				PreparedStatement pstmtDocument = null;
				ResultSet rsDocument = null;
				try {	
					pstmtDocument = DB.prepareStatement(sqlDocument, null);
					rsDocument = pstmtDocument.executeQuery();			
					while(rsDocument.next()) {	
						selectionPayLine(idPago,lineas,rsDocument);
						lineas++;
					}
					bool = true;
				}
				catch(SQLException e){	
					System.out.println(e.getMessage());
				} finally{
					rsDocument.close();
					pstmtDocument.close();	
				}
				
				/****Modifica el monto definitivo en la orden de compra****/
				if (typePartnerCombo.getSelectedItem().equals("Importada")){
					order = new MOrder(Env.getCtx(),idOrder.getRecord_ID(),null); //objeto order
					BigDecimal montoOrderTotal = rsOrder.getBigDecimal(1);
					order.setXX_INVOCECANCEAMOUNT(order.getXX_INVOCECANCEAMOUNT().add(montoOrderTotal));	
					order.save();
				}
			}	
			bool = true;
		}
		catch(SQLException e){	
			e.getMessage();
		} finally{
			try {
				rsOrder.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmtOrder.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (bool){
			procesarCrearPagos(idPago);
			return idPago;
		}else
			return 0;
	}
	
	/**
	 * Busca el id del Pago generado en seleccion de pago
	 * @param idPago
	 * @return
	 */
	public int setPaymentCheck(int idPago){
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String sql = "select C_PaySelectionCheck_ID " +
					 "from C_PaySelectionCheck " +
					 "where C_PaySelection_ID = " + idPago;
		
		int idCheck = 0;
		try{
			pstmt = DB.prepareStatement(sql, trx); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				idCheck = rs.getInt("C_PaySelectionCheck_ID");
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
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
		return idCheck;
	}
	
	/**
	 * Ejecuta el proceso de Crear Pagos en Seleccion de Pago, una vez creadas las lineas
	 * @param idPago
	 */
	public void procesarCrearPagos(int idPago){
		try{
			MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_PROCESSCREATEPAY_ID"), idPago); 
			mpi.save();
			
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_PROCESSCREATEPAY_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_PROCESSCREATEPAY_ID")); 
			pi.setClassName(""); 
			pi.setTitle(""); 
			
			ProcessCtl pc = new ProcessCtl(null ,pi, trx); 
			pc.start(); 
		}
		catch(Exception e){
			log.log(Level.SEVERE,e.getMessage());
		}
	}
	
	/**
	 * Modifica el estado de las cuentas por pagar
	 * @param idDocument identificador del documento
	 * @param status estado al que se quiere modificar 
	 */
	public void cambioStatusDocument(int idDocument, String status, String anticipo, String tipoFacturaSimp){	
		//Timestamp fecha = (Timestamp) dateTo.getValue();
		String sql = "";
		
		if (anticipo != ""){
			if (idDocument != 0)			
				sql = "update C_Payment " +
				  	  "set XX_AccountPayableStatus = '" + status + "' " +
				  	  "where C_Payment_ID = " + idDocument;
			else
				sql = "update C_Payment " +
					  "set XX_AccountPayableStatus = '" + status + "' " +
					  "where C_Payment_ID in " +
					  		"(select C_Payment_ID " +
					  		"from C_Payment " +
					  		"where XX_AccountPayableStatus = 'S' " +
					  		"and DocStatus = 'CO' " +
					  		"and XX_SynchronizationBank = 'Y' " +
					  		"and XX_IsAdvance = 'Y')";
		}else{
			if (idDocument != 0)
				sql = "update C_Invoice " +
					  "set XX_AccountPayableStatus = '" + status + "', " +
					  "isPaid = 'Y' " +
					  "where C_Invoice_ID = " + idDocument;		
			else
				sql = "update C_Invoice " +
					  "set XX_AccountPayableStatus = '" + status + "', " +
					  "isPaid = 'Y' " +
					  "where C_Invoice_ID in " +
					  		"(select inv.C_Invoice_ID " +
					  		"from C_Invoice inv " +
					  		"where inv.XX_AccountPayableStatus = 'S' " +
					  		"and inv.isSoTrx = 'N' and xx_invoicetype = '" + tipoFacturaSimp + "' ) ";
		}
		try{
			DB.executeUpdate(trx, sql);
		}catch(Exception e){
			log.log(Level.SEVERE, sql);
		}		
	}
	
	/**
	 * Se encarga de buscar el nombre de un proveedor específico
	 * @param idPartner del socio de negocio
	 */
	public String infoPartner(int idPartner){
		String sql = "select par.name " +
					 "from C_BPartner par " +
					 "where C_BPartner_ID = " + idPartner;

		String name = "";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				name = rs.getString("name");
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
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
		return name;
	}
	
	/**
	 * Genera la seleccion de pago
	 * @param fechaHasta
	 * @param idPartner
	 * @return el id de la seleccion del pago
	 */
	public int generarPaySelection(Timestamp fechaHasta, int idPartner){

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		String fHasta = df.format(new Date(fechaHasta.getTime())); 

		MPaySelection pago = new MPaySelection(Env.getCtx(), 0, trx);
		pago.setName(infoPartner(idPartner));
		pago.setDescription("Pago de " + tipoFactura + " " + tipoOrderNI +" -- Semana de pago hasta " + fHasta);
		pago.setC_BankAccount_ID(Env.getCtx().getContextAsInt("#XX_L_CBANKACCOUNT_ID"));
		pago.setPayDate(fechaHasta);
		pago.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCONTROL_ID"));   
		//pago.setC_Currency_ID(205);
		pago.save();
		return (pago.getC_PaySelection_ID());
	}

	/**
	 * Genera la línea de la selección de pago, por cada documento de la factura
	 * @param idSelectionPago identificador de la seleccion de pago (cabecera)
	 * @param numLinea numero de línea
	 * @param documento
	 * @throws SQLException 
	 */
	public void selectionPayLine(int idSelectionPago, int numLinea, ResultSet documento) throws SQLException{
		MPaySelectionLine line = new MPaySelectionLine(Env.getCtx(),0,trx);
		line.setC_PaySelection_ID(idSelectionPago);		
		line.setC_Invoice_ID(documento.getInt("invoiceId"));
		line.setPaymentRule("S");		
		if (documento.getString(7).equals("AP Payment")){
			line.setDescription("Anticipo número: " + documento.getString(16));	
			line.set_CustomColumn("C_Payment_ID", documento.getInt("PAYMENTID"));
		}else{
			if (documento.getInt("C_Order_ID") == 0)
				line.setDescription("Contrato número: " + documento.getInt(19));
			else if (documento.getInt("C_Order_ID") != 0){
				line.setDescription("Orden de Compra número: " + documento.getString(4));	
				MOrder order = new MOrder(Env.getCtx(), documento.getInt("C_Order_ID"), trx);
				definitiveFactor =  order.getXX_DefinitiveFactor();
			}
		}
		line.setLine(numLinea);		
		
		//** JTRIAS FIX MONTOS CORRECTOS
		line.setOpenAmt(new MInvoice( Env.getCtx(), line.getC_Invoice_ID(), null).getGrandTotal());
		line.setPayAmt(documento.getBigDecimal(1));
		line.setDiscountAmt(line.getOpenAmt().subtract(line.getPayAmt()));
		
		line.setC_Currency_ID(documento.getInt("C_Currency_ID"));
		line.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCONTROL_ID"));
		line.save();
		if (documento.getString(7).equals("AP Payment"))
			cambioStatusDocument(documento.getInt("paymentId"),"E","Anticipo","");
		else
			cambioStatusDocument(documento.getInt("invoiceId"),"E","","");
	}	

	/**
	 * Genera el pago por el monto de cada socio de negocio nacional para los tipos de 
	 * factura, de bienes y productos para la venta
	 */
	public int payment(int idPartner, Timestamp fechaPago, BigDecimal monto, int moneda){
		MPayment pago = new MPayment(Env.getCtx(), 0, trx);
		pago.setC_BPartner_ID(idPartner);
		pago.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEAPPAYMENT_ID"));		
		if (tipoOrderNI.equals("Nacional")){
			pago.setPayAmt(monto);
			pago.setXX_PayAmtLocal(monto);
		}else{
			pago.setPayAmt(monto);
			if (definitiveFactor.compareTo(new BigDecimal(0)) == 0)
				pago.setXX_PayAmtLocal(monto);
			else
				pago.setXX_PayAmtLocal(monto.multiply(definitiveFactor));
		} 
		pago.setC_Currency_ID(moneda);		
		pago.setDateTrx(fechaPago);
		pago.setDateAcct(fechaPago);
		pago.setC_BankAccount_ID(Env.getCtx().getContextAsInt("#XX_L_CBANKACCOUNT_ID"));
		pago.setIsApproved(true);
		pago.setXX_AccountPayableStatus("A");
		pago.setXX_IsAdvance(false);
		String sql = "select max(documentNo)+1 from C_Payment";
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sql, trx); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				pago.setDocumentNo(rs.getString(1));
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
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
		pago.save();
		pago.setDocAction(MPayment.DOCACTION_Complete);
		DocumentEngine.processIt(pago, MPayment.DOCACTION_Complete);
		pago.save();
		return (pago.getC_Payment_ID());
	}
	
	/**
	 * Verifica si el Socio de Negocio, tiene la opción de 
	 * Pago por Factura seleccionada
	 * @param idPartner identificador del socio de negocio
	 * @return
	 */
	public String checkPagadoInvoice(int idPartner){
		String sql = "select XX_InvoicePayments " +
					 "from C_BPartner " +
					 "where C_BPartner_ID = " +idPartner;
		String checkPay = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				checkPay = rs.getString("XX_InvoicePayments");
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
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
		return checkPay;
	}
	
	/**
	 * Genera el pago del socio de negocio de tipo servicio, y pago por factura
	 * @param idPartner identificador del socio de negocio
	 * @param idOrder identificador de la O/C
	 * @param idInvoice identificador de la factura
	 * @param fechaPago
	 * @param monto
	 * @param moneda
	 * @return
	 */
	public int paymentServiciesPayInvoice(int idPartner, int idOrder,int idInvoice, Timestamp fechaPago, BigDecimal monto, int moneda){
		MPayment pago = new MPayment(Env.getCtx(), 0, trx);	
		pago.setC_BPartner_ID(idPartner);
		pago.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEAPPAYMENT_ID")); 
		pago.setPayAmt(monto);
		pago.setC_Currency_ID(moneda);
		pago.setDateTrx(fechaPago);
		pago.setDateAcct(fechaPago);
		pago.setC_BankAccount_ID(Env.getCtx().getContextAsInt("#XX_L_CBANKACCOUNT_ID"));
		pago.setC_Invoice_ID(idInvoice);
		pago.setC_Order_ID(idOrder);
		pago.setIsApproved(true);
		pago.setXX_IsAdvance(false);
		pago.save();
		pago.setDocAction(MPayment.DOCACTION_Complete);
		DocumentEngine.processIt(pago, MPayment.DOCACTION_Complete);	
		pago.save();							
		return (pago.getC_Payment_ID());	
	}
	
	/**
	 * Modifica el status de las cuentas por pagar a Suspendido
	 * cuando sea desactivado el socio de negocio
	 * @param id
	 * @param condicion
	 */
	public void modificarStatus(int idPartner, int idOrder, int idDocument, String condicion){
		Timestamp to = (Timestamp) dateTo.getValue();
		m_sql = new StringBuffer();
		m_sql2 = new StringBuffer();
		String sql = "";
		String sql2 = "";
		if (condicion.equals("Partner")){			
			m_sql.append("update ( " +
							 "select inv.XX_AccountPayableStatus " +
							 "from C_Invoice inv " +
							 "left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
							 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " + 
							 "where inv.C_BPartner_ID = " + idPartner + " " +
							 "and inv.DocStatus = 'CO' " +
							 "and inv.XX_AccountPayableStatus = 'A' " +
							 "and inv.isSoTrx = 'N' " +
							 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') " +
							 "and inv.XX_DatePaid <= ").append(DB.TO_DATE(to)).append(") " +
						 "set XX_AccountPayableStatus = 'S' ");
			m_sql2.append("update ( " +
							  "select pay.XX_AccountPayableStatus " +
							  "from C_Payment pay " +
							  "where exists (select C_Order_ID from C_Invoice where C_Order_ID = pay.C_Order_ID) " +
							  "and pay.C_Order_Id is not null " +
							  "and pay.C_BPartner_ID = " + idPartner + " " +
							  "and pay.DocStatus = 'CO' " +
							  "and pay.XX_SynchronizationBank = 'Y' " +
							  "and pay.XX_AccountPayableStatus = 'A' " +
							  "and pay.XX_IsAdvance = 'Y' " +
							  "and pay.DateTrx <= ").append(DB.TO_DATE(to)).append(") " +
						  "set XX_AccountPayableStatus = 'S' ");
			sql = m_sql.toString();
			sql2 = m_sql2.toString();
		}else if (condicion.equals("Order")){			
			m_sql.append("update ( " +
							 "select inv.XX_AccountPayableStatus " +
							 "from C_Invoice inv, C_Order ord " + 
							 "where inv.C_Order_ID = ord.C_Order_ID " +
							 "and ord.XX_InvoicingStatus = 'AP' " +
							 "and inv.C_Order_ID = " + idOrder + " " +
							 "and inv.C_BPartner_ID = " + idPartner + " " +
							 "and inv.DocStatus = 'CO' " +
							 "and inv.XX_AccountPayableStatus = 'A' " +
							 "and inv.isSoTrx = 'N' " +
				 			 "and inv.XX_DatePaid <= ").append(DB.TO_DATE(to)).append(") " +
			 			 "set XX_AccountPayableStatus = 'S' ");
			m_sql2.append("update ( " +
							  "select pay.XX_AccountPayableStatus " +
							  "from C_Payment pay " +
							  "where exists (select C_Order_ID from C_Invoice where C_Order_ID = pay.C_Order_ID) " +
							  "and pay.C_Order_Id is not null " +
							  "and pay.C_BPartner_ID = " + idPartner + " " +
							  "and pay.C_Order_ID = " + idOrder + " " +
							  "and pay.DocStatus = 'CO' " +
							  "and pay.XX_SynchronizationBank = 'Y' " +
							  "and pay.XX_AccountPayableStatus = 'A' " +
							  "and pay.XX_IsAdvance = 'Y' " +
							  "and pay.DateTrx <= ").append(DB.TO_DATE(to)).append(") " +
						  "set XX_AccountPayableStatus = 'S' ");
			sql = m_sql.toString();
			sql2 = m_sql2.toString();
		}else if (condicion.equals("Contrato")){
			m_sql.append("update ( " +
							 "select inv.XX_AccountPayableStatus " +
							 "from C_Invoice, XX_Contract con " +
					 	 	 "where inv.XX_Contract_ID = con.XX_Contract_ID " +
					 	 	 "and inv.XX_InvoicingStatusContract = 'AP' " +
					 	 	 "and inv.XX_Contract_ID = " + idOrder + " " +
					 	 	 "and inv.C_BPartner_ID = " + idPartner + " " + 
					 	 	 "and inv.DocStatus = 'CO' " +
					 	 	 "and inv.XX_AccountPayableStatus = 'A' " +
					 	 	 "and inv.isSoTrx = 'N' " +
				 			 "and inv.XX_DatePaid <= ").append(DB.TO_DATE(to)).append(") " +
			 			 "set inv.XX_AccountPayableStatus = 'S' ");
			sql = m_sql.toString();			
		}else if (condicion.equals("Document")){
			m_sql.append("update C_Invoice " +
					     "set XX_AccountPayableStatus = 'S' " + 
					     "where C_Invoice_ID = " + idDocument + " " +
					     //"and C_Order_ID = " + idOrder + " " +
					     "and C_BPartner_ID = " + idPartner + " " +
					     "and DocStatus = 'CO' " +
					     "and XX_AccountPayableStatus = 'A' " +
					     "and isSoTrx = 'N' ");
			sql = m_sql.toString();
		}else if (condicion.equals("Anticipo")){
			m_sql.append("update C_Payment " +
						 "set XX_AccountPayableStatus = 'S' " +
						 "where C_Order_ID = " + idOrder + " " +
						 "and documentNo = '" + idDocument + " " +
						 "and C_BPartner_ID = " + idPartner + " " +
						 "and XX_SynchronizationBank = 'Y' " +
						 "and DocStatus = 'CO' " +
						 "and XX_IsAdvance = 'Y' " +
						 "and XX_AccountPayableStatus = 'A' " +
						 "and DateTrx <= ").append(DB.TO_DATE(to));
			sql = m_sql.toString();
		}
		else if (condicion.equals("Single")){
			
			m_sql.append("update C_Invoice inv " +
						 "SET XX_AccountPayableStatus = 'S' " +
						 "WHERE inv.C_Invoice_ID = " + idOrder + " " +
						 "and inv.C_BPartner_ID = " + idPartner + " " +
						 "and inv.DocStatus = 'CO' " +
						 "and inv.XX_AccountPayableStatus = 'A' " +
						 "and inv.isSoTrx = 'N' " +
						 "and inv.XX_DatePaid <= ").append(DB.TO_DATE(to)).append("");
			sql = m_sql.toString();
		}
		
		DB.executeUpdate(null, sql);
		if (!sql2.equals(""))
			DB.executeUpdate(null, sql2);
	}
	
	/**
	 * Se encarga de tomar el registro seleccionado de la primera tabla y llamar 
	 * al metodo tableInitS() que se encarga de llenar la segunda tabla y a su 
	 * vez el tableInitT() que se encarga de llenar la tercera tabla
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;	
	}

	@Override
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}
	
	/**
	 * Llena los combos del filtro
	 */
	private  void llenarCombos(){
		dynCategoryPartner();
		dynTypePartner();
		dynCategory();
	}
	
	/**
	 * Se encarga de ejecutar el proceso de la interfaz de pago, 
	 * a través de la selección de pago
	 * @param idPaySelection lista con los identificadores de las 
	 * selecciones de pago
	 */
	public void interfaceOrderPay(ArrayList<Integer> idPaySelection){
		try{
			MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_INTERFACEORDERPAY_ID"), 0); 
			mpi.save();
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_INTERFACEORDERPAY_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_INTERFACEORDERPAY_ID")); 
			pi.setClassName(""); 
			pi.setTitle(""); 
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("C_PaySelection_ID", idPaySelection, null, "", null));
			//list.add(new ProcessInfoParameter("C_Payment_ID", 0, null, "", null));
			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
			ProcessCtl pc = new ProcessCtl(null ,pi,trx); 
			pc.start();
		}catch(Exception e){
			log.log(Level.SEVERE,e.getMessage()); 
		}
	}
	
	
	/**
	 * Genera el reporte de especificacion de pago, 
	 * para los diferentes tipo de factura y tipo de orden
	 * @param to fecha hasta
	 */
	public void reportPaymentDetail(Timestamp to){ 
		
		String designName = "PaymentDetail";
		
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(to);
		int mes = calTo.get(Calendar.MONTH);
		mes=mes+1;
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		myReport.parameterName.add("date");
		myReport.parameterValue.add((calTo.get(Calendar.DATE)+"/"+(String.format("%02d", mes))+"/"+calTo.get(Calendar.YEAR)).toString());
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}

}
