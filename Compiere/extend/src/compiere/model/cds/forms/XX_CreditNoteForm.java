package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Container;
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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.model.X_C_Invoice;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.DocumentEngine;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.cds.MCreditNotifyReturn;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_NotificationType;
import compiere.model.cds.X_Ref_XX_StatusNotificationCredit;
import compiere.model.cds.X_XX_VCN_TradeAgreements;
import compiere.model.importcost.X_XX_VLO_SUMMARY;


/**
 * forma para crear las notas de creditos a partir de avisos de credito por descuentos de almacenaje
 * de la mercancia devuelta de un proveedor
 * @author Rebecca Principal
 *
 */
public class XX_CreditNoteForm extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_CreditNoteForm.class);
	// cliente 
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	private int Inv=0;
	 StringBuffer    m_sql = null;
	 StringBuffer    m_sqlDetail = null;
	 String          m_groupBy = "";
	 String          m_orderBy = "";
	 Ctx ctx=Env.getCtx();
	//panel - tablas
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private JScrollPane xScrollPaneDetails = new JScrollPane();
	private XX_MiniTableCreditNote xTable = new XX_MiniTableCreditNote();
	private MiniTable xTableDetails = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private TitledBorder xBorder =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_CreditNotify"));
	private TitledBorder xBorderDetails = 
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_Details"));
	//Numero del aviso de credito
	 Integer IDRegAct=0;
	//BUSINESS PARTNER Busqueda
	private CLabel vendorLabel = new CLabel();
	private VComboBox comboBPartner = new VComboBox();
	private VComboBox comboCNotifySearch = new VComboBox();
	//estado de aviso de Credito
	private  CLabel StatusCNotify = new CLabel(Msg.translate(Env.getCtx(), "XX_Status"));
	private  VComboBox comboStatusCNotifySearch = new VComboBox();
	private  Vector<String> EstCNotify = new Vector<String>();
	private  Vector<String> notType = new Vector<String>();
	//Fecha
	private CLabel dateLabel = new CLabel();
	private VDate calendar = new VDate();
	//buttons
	private CButton bGenerateNoteCredit = new CButton();
	private CButton bCancelNoteCredit = new CButton();
	private CButton bSearch = new CButton();
	private JButton bReset = new CButton();
	//Datos de entrada del usuario
	private CLabel controlLabel = new CLabel();
	private CLabel numberLabel = new CLabel();
	private CTextField control = new CTextField();
	private CTextField DocNumb = new CTextField();
	private  int tableInit_option;
	// keyname
	KeyNamePair loadKNP = new KeyNamePair(0, "");
	 //variable para e id de la columna id de devolucion seleccionda
	KeyNamePair CNotifyKey=null;
	 int CNotifyID=0, vendor=0;
	// OJO DOCUMENT SEQUENCE para notify				
	Date utilDate = new Date(); 
	long lnMilisegundos = utilDate.getTime();
	Timestamp fechaActual = new Timestamp(lnMilisegundos);
	
	
	/*
	 * Agregando por Jorge Pires
	 * */
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
	private CLabel departmentLabel = new CLabel();
	private CComboBox departmentCombo = new CComboBox();
	private CLabel POLabel = new CLabel();
	private VLookup purchaseSearch = null;	
	private CLabel notificationTypeLabel = new CLabel();
	private CComboBox notificationTypeCombo = new CComboBox();
	

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
		try
		{
			Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
					.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
					DisplayTypeConstants.Search);
			purchaseSearch = new VLookup("C_Order_ID", false, false, true, l);
			purchaseSearch.setVerifyInputWhenFocusTarget(false);

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
		
	}

	//organizacion de la forma
	private void jbInit() throws Exception{
		/*
		 * Pongo valores a las etiquetas
		 * */
		dateToLabel.setText(Msg.getMsg(Env.getCtx(), "DateFrom"));
		dateFromlabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Category"));
		notificationTypeLabel.setText(Msg.getMsg(Env.getCtx(), "XX_NotificationType"));
		POLabel.setText(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"));
		departmentLabel.setText(Msg.translate(Env.getCtx(), "XX_Department_I"));

		
		//Ubicacion del boton de Busqueda (panel superior)
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		
		//bSearch.setPreferredSize(new Dimension(100,30));	
		bSearch.setEnabled(true);
		xPanel.setLayout(xLayout);
		vendorLabel.setText(Msg.translate(Env.getCtx(), "VendorCod"));
		dateLabel.setText(Msg.translate(Env.getCtx(), "Date"));
		bReset.addActionListener(this);
		
		//Ubicacion de botones Cancelar y Generar nota de credito (Panel Inferior)
		southPanel.setLayout(southLayout);
		//Cancelar Nota de Credito
		bCancelNoteCredit.setText(Msg.translate(Env.getCtx(), "XX_CancelCN"));
		//bCancelNoteCredit.setPreferredSize(new Dimension(180,30));	
		bCancelNoteCredit.setEnabled(true);	
		//Generar Nota de Credito
		bGenerateNoteCredit.setText(Msg.translate(Env.getCtx(), "XX_CreateCN"));
		//bGenerateNoteCredit.setPreferredSize(new Dimension(180,30));	
		bGenerateNoteCredit.setEnabled(true);
		//Dimensionar 
		
		centerPanel.setLayout(centerLayout);
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(960, 220));
		xScrollPaneDetails.setBorder(xBorderDetails);
		xScrollPaneDetails.setPreferredSize(new Dimension(960, 220));
		
		xPanel.setLayout(xLayout);
		
		//Panel Superior comboCNotifySearch
/*		northPanel.add(DocNumber,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(comboCNotifySearch,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(vendorLabel,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(comboBPartner,        new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(StatusCNotify,    new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(comboStatusCNotifySearch,        new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));*/
		northPanel.add(bSearch,   new GridBagConstraints(8, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		northPanel.add(bReset,    new GridBagConstraints(9, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		
		
		/*
		 * estoy poniendo los combos en sus sitios primera linea
		 * */
		northPanel.add(dateToLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(dateFrom, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(dateFromlabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(dateTo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(categoryLabel, new GridBagConstraints(4, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(5, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		/*
		 * estoy poniendo los combos en sus sitios segunda linea
		 */
		northPanel.add(departmentLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(departmentCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(vendorLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(comboBPartner, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(POLabel, new GridBagConstraints(4, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(purchaseSearch, new GridBagConstraints(5, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));

		/*
		 * estoy poniendo los combos en sus sitios segunda linea
		 */
		northPanel.add(StatusCNotify, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboStatusCNotifySearch, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		northPanel.add(notificationTypeLabel, new GridBagConstraints(2, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(notificationTypeCombo, new GridBagConstraints(3, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		centerPanel.add(xScrollPaneDetails, BorderLayout.SOUTH);
		xScrollPane.getViewport().add(xTable, null);
		xScrollPaneDetails.getViewport().add(xTableDetails, null);
		//Panel Inferior
		controlLabel.setText(Msg.translate(Env.getCtx(), "XX_ControlNumber"));
		control.setPreferredSize(new Dimension(100,20));
		numberLabel.setText(Msg.translate(Env.getCtx(), "XX_DocumentNo"));
		DocNumb.setPreferredSize(new Dimension(100,20));
		//numero sw documento=numero de la factura
		southPanel.add(numberLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(12, 0, 5, 5), 0, 0));
		southPanel.add(DocNumb,        new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(12, 0, 5, 0), 0, 0));
		//control
		southPanel.add(controlLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(12, 0, 5, 0), 0, 0));
		southPanel.add(control,        new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(12, 0, 5, 0), 0, 0));
		//fecha de generacion de la nota de credito del proveedor
		southPanel.add(dateLabel,      new GridBagConstraints(0, 2, 2,1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 0, 5, 5), 0, 0));
		southPanel.add(calendar,        new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		southPanel.add(bCancelNoteCredit,  new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(12, 30, 5, 0), 0, 0));
		southPanel.add(bGenerateNoteCredit,  new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(12, 50, 5, 0), 0, 0));
		
	
	}   //  jbInit
	//Datos 
	void dynCategory() {

		categoryCombo.removeActionListener(this);
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_CATEGORY ";
		sql += " ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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

	void dynDepartament() {
		KeyNamePair cat = (KeyNamePair) categoryCombo.getSelectedItem();
		departmentCombo.removeActionListener(this);
		departmentCombo.removeAllItems();
		
		String sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_DEPARTMENT ";

		if (cat != null && cat.getKey() != -1) {
			sql += " WHERE XX_VMR_CATEGORY_ID = " + cat.getKey();
		}
		sql += " ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			departmentCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				departmentCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}

			departmentCombo.addActionListener(this);
			departmentCombo.setEnabled(true);
			departmentCombo.setEditable(true);
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
	
	
	private void dynInit()
	{	
		//Llenar los filtros de busquedas
		llenarcombos();
		// generacion de las columnas para los avisos de Creditos
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Options"), "", IDColumn.class, false, false, ""),//0
				//1 Numero de la devolucion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_CreditNotifyReturn_ID"),   ".", KeyNamePair.class), //1 
				
				//2 Proveedor
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", KeyNamePair.class), //2
				
				//3 Description
				new ColumnInfo(Msg.translate(Env.getCtx(), "Description"),  "Description", String.class),//3
				
				//4 Departamento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),  "XX_VMR_Department_ID", String.class),//4
				
				//5 Orden de Compra
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_ORDER_ID"),  "C_ORDER_ID", String.class),//2
				//6 Factor de cambio 
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ChangeFactor"), "XX_ChangeFactor", float.class),//3
				//7 Moneda 
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Currency_ID"),".", KeyNamePair.class),//4
				//8 Monto neto de la devolucion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_UNITPURCHASEPRICE"), "XX_UNITPURCHASEPRICE", float.class),	//5
				//9 Monto de la Notificacion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Amount"), "XX_Amount", float.class),	//5
				//10 Tax Category
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_TaxCategory_ID"),".", KeyNamePair.class),//6
				//11 Monto iva
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Amount_IVA"),"XX_Amount_IVA", float.class),	//7
				//12 Monto Total es un campo SQL
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Total"), "Total", float.class),//8

				//13 Estado del Aviso de Credito	
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Status"),   ".", KeyNamePair.class),//10
				
				//14 Numero del Documento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DocumentNo"),  "XX_DocumentNo", String.class),//11
				
				//15 Tipo de Aviso
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Notificationtype"),  "XX_Notificationtype", String.class),//15

		};
		ColumnInfo[] layoutDetails = new ColumnInfo[] {
				//0 Mes
				new ColumnInfo(Msg.translate(Env.getCtx(), "Month"), ".", Integer.class),
				
				//1 Año
				new ColumnInfo(Msg.translate(Env.getCtx(), "Year"),   ".", Integer.class), 
				
				//2 Tienda
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WarehouseName"),   ".", String.class), 
				
				//3 Departamento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),  "XX_VMR_Department_ID", String.class),
						
				//4 Costo Moneda Origen
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_UNITPURCHASEPRICE"), "XX_UNITPURCHASEPRICE", float.class),	
				
				//5 Factura
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),   ".", String.class), 
				
				//6 Descuento por Reconocimiento de Merma
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RecogDisc"),  ".", float.class),
				
				//7 Descuento por Servicio Post Venta				
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AfterSaleDisc"), ".", float.class),
				
				//8 Descuento por Centraliación de Entregas				
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_CentDelDisc"),".", float.class),
				
				//9 Descuento por Aporte a Publicidad
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PubAmountDisc"), ".", float.class)

		};
		xTable.prepareTable(layout, "", "", true, "");
		xTableDetails.prepareTable(layoutDetails, "", "", true, "");

		//  Visual
		CompiereColor.setBackground (this);
		xTable.getSelectionModel().addListSelectionListener(this);		
		xTable.getColumnModel().getColumn(1).setMinWidth(110);
		xTable.getColumnModel().getColumn(2).setMinWidth(130);
		xTable.getColumnModel().getColumn(3).setMinWidth(140);
		xTable.getColumnModel().getColumn(4).setMinWidth(130);
		xTable.getColumnModel().getColumn(5).setMinWidth(110);
		xTable.getColumnModel().getColumn(6).setMinWidth(110);
		xTable.getColumnModel().getColumn(7).setMinWidth(90);
		xTable.getColumnModel().getColumn(8).setMinWidth(110);
		xTable.getColumnModel().getColumn(9).setMinWidth(110);
		xTable.getColumnModel().getColumn(10).setMinWidth(110);
		xTable.getColumnModel().getColumn(11).setMinWidth(110);
		xTable.getColumnModel().getColumn(12).setMinWidth(60);
		xTable.getColumnModel().getColumn(13).setMinWidth(50);
		xTable.getColumnModel().getColumn(14).setMinWidth(100);
		xTable.getSelectionModel().addListSelectionListener(this);		
		xTableDetails.getColumnModel().getColumn(0).setMinWidth(30);
		xTableDetails.getColumnModel().getColumn(1).setMinWidth(40);
		xTableDetails.getColumnModel().getColumn(2).setMinWidth(140);
		xTableDetails.getColumnModel().getColumn(3).setMinWidth(130);
		xTableDetails.getColumnModel().getColumn(4).setMinWidth(110);
		xTableDetails.getColumnModel().getColumn(5).setMinWidth(110);
		xTableDetails.getColumnModel().getColumn(6).setMinWidth(90);
		xTableDetails.getColumnModel().getColumn(7).setMinWidth(110);
		xTableDetails.getColumnModel().getColumn(8).setMinWidth(110);
		xTableDetails.getColumnModel().getColumn(9).setMinWidth(110);
		bSearch.addActionListener(this);
		bReset.addActionListener(this);
		bGenerateNoteCredit.addActionListener(this);
		bCancelNoteCredit.addActionListener(this);
		//Cargo  Los avisos Existentes
		tableInit_option=0;
		tableInit();
		tableDetailsInit();
		
		xTable.getTableHeader().setReorderingAllowed(false);
		xTableDetails.getTableHeader().setReorderingAllowed(false);
		xTableDetails.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//xTable.setAutoResizeMode(3);
		//data();
	
		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		//estatus 
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_CreditNotifyN"));
		statusBar.setStatusDB(xTable.getRowCount());
		
	}  //  dynInit  
	
	/*
	 * Método para llenar la tabla de detalles dependiendo del aviso de crédito
	 * que se elija
	 */
	private void tableDetailsInit(){
		
		
		xTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				KeyNamePair creditID=null;
				int i = 0;
				if (xTable.getSelectedRow()==-1){
					xTableDetails.setRowCount(i);
					return;
				}
				creditID = (KeyNamePair) xTable.getModel().getValueAt(xTable.getSelectedRow(),1);				
				xTableDetails.setRowCount(i);
		    	String SQL ="SELECT det.XX_MONTH, det.XX_YEAR, war.name Tienda," +
		    			" dep.VALUE||'-'||dep.NAME Departamento, ROUND(det.XX_UNITPURCHASEPRICE,2) XX_UNITPURCHASEPRICE," +
		    			" ord.DOCUMENTNO, ROUND(det.XX_DISCRECOGDECLAMOUNT,2) XX_DISCRECOGDECLAMOUNT, ROUND(det.XX_DISCAFTERSALEAMOUNT,2) XX_DISCAFTERSALEAMOUNT," +
		    			" ROUND(det.XX_CENTRADISCDELIAMOUNT,2) XX_CENTRADISCDELIAMOUNT, det.XX_PUBAMOUNT" +
		    			" FROM XX_VCN_DETAILADVICE det, C_Order ord, XX_VMR_DEPARTMENT dep," +
		    			" M_WAREHOUSE war" +
		    			" WHERE det.C_Order_ID=ord.C_Order_ID" +
		    			" AND det.XX_VMR_DEPARTMENT_ID = dep.XX_VMR_DEPARTMENT_ID (+) " +
		    			" AND war.M_WAREHOUSE_ID (+) = det.M_WAREHOUSE_ID" +
		    			" AND det.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
		    			" AND det.XX_CREDITNOTIFYRETURN_ID="+ creditID.getKey() +" " +
		    			"ORDER BY dep.VALUE, ord.DocumentNo ";
		    	PreparedStatement pstmt = null;
				ResultSet rs = null;
		    	try
		    	{	
					pstmt = DB.prepareStatement(SQL, null);
					rs = pstmt.executeQuery();			
					while(rs.next()) {	
						xTableDetails.setRowCount (i+1);
						//Mes
						xTableDetails.setValueAt(rs.getInt("XX_MONTH"),i,0);
						//Año
						xTableDetails.setValueAt(rs.getInt("XX_YEAR"), i, 1);
						//Tienda
						xTableDetails.setValueAt(rs.getString("Tienda"), i, 2);
						//Departamento
						xTableDetails.setValueAt(rs.getString("Departamento"), i, 3);
						//Costo Origen
						xTableDetails.setValueAt(rs.getFloat("XX_UNITPURCHASEPRICE"), i,4);
						//Factura
						xTableDetails.setValueAt(rs.getString("DOCUMENTNO"), i,5);
						//Descuento por merma
						xTableDetails.setValueAt(rs.getFloat("XX_DISCRECOGDECLAMOUNT"), i, 6);
						//Descuento Post Venta
						xTableDetails.setValueAt(rs.getFloat("XX_DISCAFTERSALEAMOUNT"), i, 7);
						//Descuento por Centralización de Ventas
						xTableDetails.setValueAt(rs.getFloat("XX_CENTRADISCDELIAMOUNT"), i,8);
						//Descuento por Aporte A publicidad
						xTableDetails.setValueAt(rs.getFloat("XX_PUBAMOUNT"), i, 9);
	
						
						i++;				
					}	
				}
				catch(SQLException er)
				{	
					er.getMessage();
				}finally{
					try {
						rs.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					try {
						pstmt.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}	
			}
		});
    	
	}
	
	private  void tableInit ()
	{
		//Colocar solo las facturas que que correspondan al mes anterior hasta el mes actual
		Calendar auxCalendar = Calendar.getInstance(), 
		 dateInit = Calendar.getInstance(), 
		 dateEnd = Calendar.getInstance();
		auxCalendar.setTime((Date) calendar.getValue());
		int month = auxCalendar.get(Calendar.MONTH);
		int year = auxCalendar.get(Calendar.YEAR);
		int lastDay = auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		m_sql = new StringBuffer ();
		Inv=0;
		// Actual Taxable Period
		dateInit.set(year, month, 1, 0, 0, 0);
		dateEnd.set(year, month, lastDay, 0, 0, 0);
	
		//Mostrar todos los avisos de creditos activos!!!h
		if(tableInit_option==0)
		{	
			m_sql.append("SELECT CNot.VALUE, CNot.XX_CREDITNOTIFYRETURN_ID, part.Name, part.C_BPARTNER_ID, CNot.Description, dep.value||'-'||dep.Name Departamento, ord.DocumentNo, CNot.C_ORDER_ID,CNot.XX_CHANGEFACTOR,cur.ISO_CODE,CNot.C_CURRENCY_ID,CNot.XX_UNITPURCHASEPRICE,ROUND(CNot.XX_AMOUNT,2) XX_AMOUNT,tax.NAME as Impuesto,CNot.C_Tax_ID,CNot.XX_AMOUNT_IVA, ROUND(SUM(XX_AMOUNT+XX_AMOUNT_IVA),2) as Total,CNot.XX_Status,CNot.XX_DocumentNo, CNot.XX_NOTIFICATIONTYPE  " 
					+ "FROM XX_CREDITNOTIFYRETURN CNot, " +
						"C_BPARTNER part, " +
						"C_ORDER ord, " +
						"C_CURRENCY cur, " +
						"XX_VMR_Department dep, " +
						"C_TAX tax "
					+ "WHERE ord.C_ORDER_ID=CNot.C_ORDER_ID "
					+ "AND ord.C_BPARTNER_ID=part.C_BPARTNER_ID " 
					+ "AND ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID (+) " 
					+ "AND cur.C_CURRENCY_ID=CNot.C_CURRENCY_ID "
					+ "AND tax.C_Tax_ID=CNot.C_Tax_ID  "
					+ ""); 
			m_groupBy =" Group By CNot.VALUE, CNot.XX_CREDITNOTIFYRETURN_ID,ord.DocumentNo,CNot.C_ORDER_ID,CNot.XX_CHANGEFACTOR,cur.ISO_CODE,CNot.C_CURRENCY_ID,CNot.XX_UNITPURCHASEPRICE,CNot.XX_AMOUNT,tax.NAME,CNot.C_Tax_ID,CNot.XX_AMOUNT_IVA,part.Name, part.C_BPARTNER_ID,CNot.XX_Status,CNot.XX_DocumentNo,CNot.Description,dep.value||'-'||dep.Name, CNot.XX_NOTIFICATIONTYPE ";	
			
			//Busqueda por Id del aviso de credito pilas -1
			if(comboCNotifySearch.getSelectedIndex()!=0 && comboCNotifySearch.getSelectedItem() != null){
				int clave_CN=((KeyNamePair)comboCNotifySearch.getSelectedItem()).getKey();
				m_sql.append(" AND ").append("CNot.XX_CreditNotifyReturn_ID=").append(clave_CN);
			}
			//busqueda por proveedor
			if(comboBPartner.getSelectedIndex()!=0 && comboBPartner.getSelectedItem() != null){
				if(((KeyNamePair)comboBPartner.getSelectedItem()).getKey() != 0){
					int clave_vendor=((KeyNamePair)comboBPartner.getSelectedItem()).getKey();
					m_sql.append(" AND ").append("part.C_BPartner_ID=").append(clave_vendor);
				}
			}	
			
			Timestamp to = (Timestamp) dateTo.getValue();
			Timestamp from = (Timestamp) dateFrom.getValue();
			if (from != null && to != null) {
				m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" >= ").append(
						DB.TO_DATE(from)).append(" ");
				m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" <= ").append(
						DB.TO_DATE(to)).append(" ");
			} else if (from != null)
				m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			else if (to != null)
				m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" <= ").append(
						DB.TO_DATE(to)).append(" ");
			
			//agrego la categoria
			if (categoryCombo.getValue() != null) {
				KeyNamePair cat = (KeyNamePair) categoryCombo.getValue();
				if (cat.getKey() != -1)
					m_sql.append(" AND ord.XX_VMR_CATEGORY_ID = ").append(
							cat.getKey()).append(" ");
			}
			//agrego el departamento al query
			if (departmentCombo.getValue() != null) {
				KeyNamePair dep = (KeyNamePair) departmentCombo.getValue();
				if (dep.getKey() != -1)
					m_sql.append(" AND ord.XX_VMR_DEPARTMENT_ID = ").append(
							dep.getKey()).append(" ");
			}
			//agrego el tipo de notificacion al query
			if(notificationTypeCombo.getSelectedIndex()!=0 && notificationTypeCombo.getSelectedItem() != null){	
	
				m_sql.append(" AND ").append("CNot.XX_NOTIFICATIONTYPE = ").append("'").append(notType.get(notificationTypeCombo.getSelectedIndex())).append("'");				
			}
			
		}else
			if(tableInit_option==1)
			{
				m_sql.append("SELECT CNot.VALUE, CNot.XX_CREDITNOTIFYRETURN_ID, part.Name, part.C_BPARTNER_ID, CNot.Description, dep.value||'-'||dep.Name Departamento, ord.DocumentNo, CNot.C_ORDER_ID,CNot.XX_CHANGEFACTOR,cur.ISO_CODE,CNot.C_CURRENCY_ID,CNot.XX_UNITPURCHASEPRICE,ROUND(CNot.XX_AMOUNT,2) XX_AMOUNT,tax.NAME as Impuesto,CNot.C_Tax_ID,ROUND(CNot.XX_AMOUNT_IVA,2) XX_AMOUNT_IVA, ROUND((XX_AMOUNT+XX_AMOUNT_IVA),2) as Total,CNot.XX_Status,CNot.XX_DocumentNo, CNot.XX_NOTIFICATIONTYPE  " 
						+ "FROM XX_CREDITNOTIFYRETURN CNot, C_BPARTNER part, C_ORDER ord, C_CURRENCY cur, C_TAXCATEGORY tax, XX_VMR_Department dep " 
						+ "WHERE ord.C_ORDER_ID=CNot.C_ORDER_ID "
						+ "AND ord.C_BPARTNER_ID=part.C_BPARTNER_ID "
						+ "AND ord.XX_VMR_Department_ID = dep.XX_VMR_Department_ID (+) " 
						+ "AND cur.C_CURRENCY_ID=CNot.C_CURRENCY_ID "
						+ " AND tax.C_TaxCategory_ID=CNot.C_TaxCategory_ID  "); 
				m_groupBy =" Group By CNot.VALUE, CNot.XX_CREDITNOTIFYRETURN_ID,ord.DocumentNo,CNot.C_ORDER_ID,CNot.XX_CHANGEFACTOR,cur.ISO_CODE,CNot.C_CURRENCY_ID,CNot.XX_UNITPURCHASEPRICE,CNot.XX_AMOUNT,tax.NAME,CNot.C_Tax_ID,CNot.XX_AMOUNT_IVA,part.Name, part.C_BPARTNER_ID,CNot.XX_Status,CNot.XX_DocumentNo,CNot.Description,dep.value||'-'||dep.Name, CNot.XX_NOTIFICATIONTYPE ";	
				
				//Busqueda el estado del aviso de credito
				if(comboStatusCNotifySearch.getSelectedIndex()!=0 && comboStatusCNotifySearch.getSelectedItem() != null){	
					String clave_CNStatus=(String) comboStatusCNotifySearch.getSelectedItem();
					clave_CNStatus=clave_CNStatus.substring(0,clave_CNStatus.indexOf("-")); 
					for(int i=0;i<EstCNotify.size();i++ ) {
						if(clave_CNStatus.equals(EstCNotify.get(i).toString())){
							m_sql.append(" AND ").append("CNot.XX_STATUS = ").append("'").append(clave_CNStatus).append("'");
						}
					}
				}
		
				Timestamp to = (Timestamp) dateTo.getValue();
				Timestamp from = (Timestamp) dateFrom.getValue();
				if (from != null && to != null) {
					m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" >= ").append(
							DB.TO_DATE(from)).append(" ");
					m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" <= ").append(
							DB.TO_DATE(to)).append(" ");
				} else if (from != null)
					m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" >= ").append(
							DB.TO_DATE(from, true)).append(" ");
				else if (to != null)
					m_sql.append(" AND TRUNC(CNot.CREATED) ").append(" <= ").append(
							DB.TO_DATE(to)).append(" ");
				
				//agrego la categoria
				if (categoryCombo.getValue() != null) {
					KeyNamePair cat = (KeyNamePair) categoryCombo.getValue();
					if (cat.getKey() != -1)
						m_sql.append(" AND ord.XX_VMR_CATEGORY_ID = ").append(
								cat.getKey()).append(" ");
				}
				
				//agrego la O/C
				if (purchaseSearch.getValue() != null) {
					Integer order = (Integer) purchaseSearch.getValue();
					if (order != 0)
						m_sql.append(" AND ord.C_Order_ID = ").append(
								order).append(" ");
				}
				
				//agrego el departamento al query
				if (departmentCombo.getValue() != null) {
					KeyNamePair dep = (KeyNamePair) departmentCombo.getValue();
					if (dep.getKey() != -1)
						m_sql.append(" AND ord.XX_VMR_DEPARTMENT_ID = ").append(
								dep.getKey()).append(" ");
				}
				//agrego el tipo de notificacion al query
				if(notificationTypeCombo.getSelectedIndex()!=0 && notificationTypeCombo.getSelectedItem() != null){	
		
					m_sql.append(" AND ").append("CNot.XX_NOTIFICATIONTYPE = ").append("'").append(notType.get(notificationTypeCombo.getSelectedIndex())).append("'");				
				}	
			}
		//si coloca fecha debera ser los avisos hasta esa fecha.
		/**if(calendar.getValue()!=null){
			m_sql.append(" AND ").append("CNot.CREATED BETWEEN " +
			"TO_TIMESTAMP('"+ SDF.format(dateInit.getTime()) +"', 'YYYY-MM-DD HH24:MI:SS') " +
			"AND TO_TIMESTAMP('"+ SDF.format(dateEnd.getTime()) +"', 'YYYY-MM-DD HH24:MI:SS') ");
		}*/
		
		m_orderBy = " Order By CNot.VALUE DESC ";
		
		String SQL = MRole.getDefault().addAccessSQL(
				m_sql.toString(), "CNot", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
				+ m_groupBy + m_orderBy;
			;
			
    	int i = 0;
    	xTable.setRowCount(i);
    	PreparedStatement pstmt = null;
		ResultSet rs = null;	
    	try 
		{	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTable.setRowCount (i+1);			
				IDColumn id = new IDColumn(rs.getInt(2));
				id.setSelected(false);
				xTable.setValueAt(id, i,0);
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("XX_CREDITNOTIFYRETURN_ID"), rs.getString("VALUE")),i,1);//1
				//proveedor
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("C_BPARTNER_ID"), rs.getString("Name")), i, 2);
				//Description
				xTable.setValueAt(rs.getString("Description"), i, 3);
				//Departamento
				xTable.setValueAt(rs.getString("Departamento"), i, 4);
				//Orden de compra
				xTable.setValueAt(
						new KeyNamePair(rs.getInt("C_ORDER_ID"), rs.getString("DocumentNo")), i,5);//rs.getString(3), i, 2));//2
				//factor de cambio 5-3
				xTable.setValueAt(rs.getInt("XX_CHANGEFACTOR"), i,6);
				//Moneda cur.ISO_CODE,CNot.C_CURRENCY_ID
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("C_CURRENCY_ID"), rs.getString("ISO_CODE")), i, 7);
				//Costo de Origen
				xTable.setValueAt(rs.getFloat("XX_UNITPURCHASEPRICE"), i, 8);
				//monto
				xTable.setValueAt(rs.getFloat("XX_AMOUNT"), i,9);
				//impuesto
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("C_Tax_ID"), rs.getString("Impuesto")), i, 10);
				//monto del impuesto
				xTable.setValueAt(rs.getFloat("XX_AMOUNT_IVA"), i, 11);
				//total 
				xTable.setValueAt(rs.getFloat("Total"), i, 12);
				
				//Estado
				xTable.setValueAt(rs.getString("XX_Status"), i, 13);
				//numero del Document
				xTable.setValueAt(rs.getString("XX_DocumentNo"), i, 14);
				
				//tipo de aviso
				xTable.setValueAt(rs.getString("XX_NotificationType"), i, 15);

				
				i++;				
			}
				
	
		}
		catch(SQLException e)
		{	
			e.getMessage();
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

	}   //  tableInit
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		
		if (e.getSource() == bSearch){
			cmd_Search();
		}else if (e.getSource() == categoryCombo) {
			dynDepartament();
		}else{
			//Clean Form
			if(e.getSource() == bReset){
				loadBasicInfo();
			}else{
				if(e.getSource() == bGenerateNoteCredit){
					//generar nota de credito
					int Reg=0; boolean avi=false;
					GeneratedCreditNotify(Reg,avi);
				}else{
					if(e.getSource() == bCancelNoteCredit){
						//Cancelar aviso y cambiar estado del aviso
						CancelarAvisoCredito();
					}
				}
			}
				
		}
		
	} 
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{		
		//Selecciono id del aviso o al proveedor aviso actuales
		if((comboCNotifySearch.getSelectedIndex()!=0) || (comboBPartner.getSelectedIndex()!=0) ){
			tableInit_option=0;		
			tableInit();
			if(xTable.getRowCount()!=0){
				statusBar.setStatusLine("Avisos/Notas");
				statusBar.setStatusDB(xTable.getRowCount());
			}else{
				showCreditNotificationCondition();
			}
		}
		else{
		//estado de los avisos 
			if((comboStatusCNotifySearch.getSelectedIndex()!=0)){
					tableInit_option=1;		
					tableInit();
					if(xTable.getRowCount()!=0){
						statusBar.setStatusLine("Avisos/Notas");
						statusBar.setStatusDB(xTable.getRowCount());
					}else{
						showCreditNotificationCondition();
					}
			}
			else{			
				tableInit_option=1;		
				tableInit();
				if(xTable.getRowCount()!=0){
					statusBar.setStatusLine("Avisos/Notas");
					statusBar.setStatusDB(xTable.getRowCount());
				}else{
					showCreditNotificationCondition();
				}
			}
		}
		
	}   //  cmd_Search

	/**
	 *  Table initial state 
	 */
	private void loadBasicInfo(){
		
		comboCNotifySearch.removeActionListener(this);
		comboBPartner.removeActionListener(this);
		comboStatusCNotifySearch.removeActionListener(this);
		//Restore ComboBoxes and CheckBoxes	
		comboCNotifySearch.setEnabled(true);
		comboBPartner.setEnabled(true);
		comboStatusCNotifySearch.setEnabled(true); 
		
		comboCNotifySearch.removeAllItems();
		comboBPartner.removeAllItems();
		comboStatusCNotifySearch.removeAllItems();
		categoryCombo.removeAllItems();
		departmentCombo.removeAllItems();
		notificationTypeCombo.removeAllItems();

		//Llenar los filtros de busquedas
		llenarcombos();
	}



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
	}	//	dispose
	/**
	 * 
	 */
	private  void llenarcombos(){
		dynCategory();
		dynDepartament();
		/*
		 * Actualizo con la fecha de hoy el campo fecha hasta
		 * */
		java.util.Calendar now = java.util.Calendar.getInstance();
		dateTo.setValue(now.getTime());

		
		//Cargar combo por id de el aviso de credito
		String sql = "SELECT XX_CreditNotifyReturn_ID, Value FROM XX_CreditNotifyReturn CNot" ;
		sql = MRole.getDefault().addAccessSQL(sql, "CNot", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sql += " ORDER BY Value ASC";		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			comboCNotifySearch.addItem(loadKNP);
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCNotifySearch.addItem(loadKNP);
				//comboCNotifySearch.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			comboCNotifySearch.setEditable(false);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		// Bringing Vendors
		sql = "SELECT b.C_BPARTNER_ID, b.NAME FROM C_BPARTNER b WHERE b.ISVENDOR = 'Y' ";
		sql = MRole.getDefault().addAccessSQL(sql, "b", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sql += " ORDER BY b.NAME";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			comboBPartner.addItem(loadKNP);
			while (rs.next()){
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
				//comboBPartner.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			comboBPartner.setEditable(false);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		//cargar informacion de estatus de la Devolucion
		//LLenar los combo de listas
		comboStatusCNotifySearch.addItem("");
		for( X_Ref_XX_StatusNotificationCredit  v : X_Ref_XX_StatusNotificationCredit.values()) {
			comboStatusCNotifySearch.addItem (v.getValue()+"-"+ v);
			EstCNotify.add(v.getValue());
		}
		
		//LLenar los combo de tipo de aviso
		notificationTypeCombo.addItem("");
		notType.add("");
		for( X_Ref_XX_NotificationType v : X_Ref_XX_NotificationType.values()) {
			
			if(v.getValue().equalsIgnoreCase("AAP"))
				notificationTypeCombo.addItem ("APORTE A PUBLICIDAD");
			else if(v.getValue().equalsIgnoreCase("ACC"))
				notificationTypeCombo.addItem ("ACUERDO COMERCIAL");
			else if(v.getValue().equalsIgnoreCase("DEE"))
				notificationTypeCombo.addItem ("DEMORA EN ENTREGA");
			else if(v.getValue().equalsIgnoreCase("GAL"))
				notificationTypeCombo.addItem ("GASTOS DE ALMACENAJE");
			else
				notificationTypeCombo.addItem (v.name().replace('_', ' '));
			
			notType.add(v.getValue());
		}
		
		calendar.setValue(now.getTime());
	}//fin de llenar combos


	/**
	 * cancelar aviso de credito
	 * se realiza cuando el proveedor no realiza la nota de credito por lo que los avisos deben ser anulados
	 */
	 public void CancelarAvisoCredito() {    
			
			int rows = xTable.getRowCount(); //XX_CreditNoteCancelConfirm
     		boolean confirm= ADialog.ask(m_WindowNo, new Container(), Msg.translate(Env.getCtx(), "XX_CreditNoteCancelConfirm"));
			if (confirm){//JOptionPane.OK_OPTION == confirmado
				
				boolean canceled = false;
				
				for (int i = 0; i < rows; i++) {
		    		IDColumn id = (IDColumn) xTable.getModel().getValueAt(i,0);
		    		
		    		int notifyId = id.getRecord_ID();
		    		
		    		if(id.isSelected()){
		    			// Pedimos confirmación	    			
		    			String sql;
							// Updating status CreditNotifyReturn 
			    			sql = new String ("UPDATE XX_CREDITNOTIFYRETURN CNot"
			    					+ " SET CNot.XX_STATUS = 'ANU'"
			    					+ " WHERE CNot.XX_STATUS <> 'ANU' AND CNot.XX_STATUS <> 'CER' AND CNot.XX_CREDITNOTIFYRETURN_ID = " + notifyId);
			    			
			    			sql = MRole.getDefault().addAccessSQL(sql, "", 
			    					MRole.SQL_NOTQUALIFIED, MRole.SQL_RW);
			    			int no = DB.executeUpdate(null, sql.toString());
			    			
			    			if( no>0 )
			    				canceled = true;
			    			
			    			log.fine("Update credit notify = " + no);
		    		}/**else{
		    			ADialog.error(m_WindowNo, new Container(),"Aviso (s) No Seleccionados");
		    		}*/
		    	}//XX_CreditNoteCancel
	    		
				if(canceled)
					ADialog.info(m_WindowNo, new Container(),Msg.translate(Env.getCtx(), "XX_CreditNoteCancel"));
			}
			tableInit_option=0;
			tableInit();
	    	
	    }
	 /**
	  * Generar nota de credito en la tabla de facturas y en libro
	  */
	 public void GeneratedCreditNotify(int Reg, boolean avi) {
		 		 
		// Verifying Document Number Field
    	if (DocNumb.getValue().equals("")) {
			ADialog.error(m_WindowNo, new Container(), 
					Msg.getMsg(Env.getCtx(), "DocumentNo"));
			return;
		}
    	// Verifying control  Number Field
    	if (control.getValue().equals("")) {
			ADialog.error(m_WindowNo, new Container(), 
					Msg.getMsg(Env.getCtx(), "XX_ControlNumber"));
			return;
		}
    	// Verifying Date Credit Notify vendor
    	if (calendar.toString().equals("")) {
			ADialog.error(m_WindowNo, new Container(), 
					Msg.getMsg(Env.getCtx(), "Date"));
			return;
		}
    	
    	//generar la nueva nota de credito 
    	
    	Timestamp dateAux = calendar.getTimestamp();
    	
    	int rows = xTable.getRowCount();
    	String 	notifyNo = DocNumb.getText(), controlNo = control.getText();
    	MCreditNotifyReturn AvisoCredit = null;
    	MInvoice notifyCredit=null;//XX_CreditNoteConfirm
    	BigDecimal amountIva= BigDecimal.ZERO;
    	//BigDecimal factor=null;
    	
 		boolean confirm= ADialog.ask(m_WindowNo, new Container(), Msg.translate(Env.getCtx(), "XX_CreditNoteConfirm"));
		
 		if (confirm){
 				
			 

 			KeyNamePair vendorID = null;
 			KeyNamePair oldTax = null;
 			int oldTaxID = 0;
 			int oldVendor = 0;
 			String oldType = "";
 			String type = "";
 			boolean diff = false;
 			int count = 0;
 			
 			for (int j=0; j<rows; j++) {

 				IDColumn id = (IDColumn) xTable.getModel().getValueAt(j,0);
				id = (IDColumn) xTable.getModel().getValueAt(j,0);
 				
 				if(id.isSelected()){
 					count++;
 					vendorID = (KeyNamePair) xTable.getModel().getValueAt(j,2);
 					oldTax = (KeyNamePair) xTable.getModel().getValueAt(j,10);
 					type = xTable.getModel().getValueAt(j,15).toString();

 					if(oldVendor==0){ 
 						oldTaxID = oldTax.getKey();
 						oldVendor = vendorID.getKey();
 						oldType = type;
 					}
 					
 					if(oldVendor != vendorID.getKey() || !oldType.equals(type) || oldTaxID != oldTax.getKey())
 						diff=true;
 				}
 			}		

 				
 			if(!diff)
 			{	
				for (int i=0; i<rows; i++) {
						
					IDColumn id = (IDColumn) xTable.getModel().getValueAt(i,0);
					id = (IDColumn) xTable.getModel().getValueAt(i,0);
						
			   		int notifyId = id.getRecord_ID();
			    		
			    	if(id.isSelected()){
			    			
			   			AvisoCredit = new MCreditNotifyReturn(Env.getCtx(), notifyId, null);
			   			//factor = AvisoCredit.getXX_ChangeFactor();
			    		
			    		if(((Reg==0)&&(avi==false))){
			    	
			    			vendor = oldVendor;
			   				//Generar el Aviso de Credito en las Facturas
			   				Inv=generateCreditNotify(AvisoCredit, notifyNo, controlNo, vendor, dateAux, count);
			   				avi=true; Reg=1;	
			    		}
			    		
			   			if(Inv!=0){
			    
			    			//Pararme en el aviso de credito generado paar generar sus lineas
			    		    notifyCredit = new MInvoice (Env.getCtx(), Inv, null);
			    				
			    			MCreditNotifyReturn AuxCN = new MCreditNotifyReturn(Env.getCtx(), notifyId, null);
			    				 
			    			amountIva = amountIva.add(AuxCN.getXX_Amount_IVA());
			    			//factor = factor.add(AvisoCredit.getXX_ChangeFactor());
			    			
			    			//generar las lineas de la factura con los avisos de creditos seleccionados
			    			generarlineanotacredito(AuxCN,notifyCredit);
			    				
			    			//Actualizar estado del y numero del docuemnto del aviso de credito 
			    			AuxCN.setXX_Status(X_Ref_XX_StatusNotificationCredit.CERRADO.getValue());
			    			AuxCN.setXX_DocumentNo(notifyNo);
			   				AuxCN.save();
			   			}
			    			
			    	} // if Registro seleccionado
			    		
		    	}//fin del for
		   
				//System.out.println("Monto de iva "+amountIva);
				if(avi){
					BigDecimal negative = new BigDecimal(-1);
					//Se completa la nota de credito
					notifyCredit.setXX_TaxAmount(amountIva.multiply(negative));
		    		notifyCredit.setDocAction(X_C_Invoice.DOCACTION_Complete);
		    		DocumentEngine.processIt(notifyCredit, MInvoice.DOCACTION_Complete);
					notifyCredit.save();
					saveNotifyOnSummary(AvisoCredit, notifyCredit);
						
					/* Se crea el registro en el libro de compras si NO es de tipo Aporte a Publicidad ni Carta Compromiso
					 * y que sea moneda Nacional				
					 */
					if (!(type.equals("AAP") || type.equals("CAC")) && AvisoCredit.getC_Currency_ID()==205){
						Utilities PurchasesBook = new Utilities();
						PurchasesBook.createPurchasesBook(notifyCredit, sameTaxPeriod(dateAux), false, null);
					}
					ADialog.info(m_WindowNo, new Container(),Msg.translate(Env.getCtx(), "XX_CreditNoteGenerated"));
				}					
				
	 		}else
	 		{
	 			//Deben ser del mismo proveedor y tipo de aviso
	 			ADialog.info(m_WindowNo, new Container(),Msg.translate(Env.getCtx(), "XX_SameVendorAndType"));
	 		}//fin del if factura generada invice	
			//}//fin del if confirm
 		}
		tableInit_option=0;
		tableInit();
	 }  //fin del metodo
	 
	 private boolean sameTaxPeriod(Timestamp dateAux){
		 
		 MOrder order = null;
		 String period = "";
		 int rows = xTable.getRowCount();
		 KeyNamePair c_order_id = null;
		 
		 //Verificar que todas esten en el mismo periodo
		 for(int i=0; i<rows; i++){
			 
			 IDColumn id = (IDColumn) xTable.getModel().getValueAt(i,0);
			 id = (IDColumn) xTable.getModel().getValueAt(i,0);
				
			 if(id.isSelected()){
				
				 c_order_id = (KeyNamePair) xTable.getModel().getValueAt(i,5);
				 order = new MOrder(Env.getCtx(), c_order_id.getKey(), null);
				
				 if(period.equals("")){
					 Calendar aux = Calendar.getInstance();
					 aux.setTimeInMillis(order.getXX_InvoiceDate().getTime());
					 if((aux.get(Calendar.DAY_OF_MONTH)-1) <= 15){
						 period = "1"+new Integer(aux.get(Calendar.MONTH)+1);
					 }else{
						 period = "2"+new Integer(aux.get(Calendar.MONTH)+1);
					 }
				 }else{
					 
					 String periodoAux = new String();
						
					 Calendar aux = Calendar.getInstance();
					 aux.setTimeInMillis(order.getXX_InvoiceDate().getTime());
					 if((aux.get(Calendar.DAY_OF_MONTH)-1) <= 15){
						 periodoAux = "1"+new Integer(aux.get(Calendar.MONTH)+1);
					 }else{
						 periodoAux = "2"+new Integer(aux.get(Calendar.MONTH)+1);
					 }
						
					 if(!period.equals(periodoAux)){
						 return false;
					 }
				 }
			 }
		 }
		 
		 //Ahora verifico que la fecha actual (cuando estoy convirtiendo estas notas) esté en el periodo impositivo 
		 //en comun de las facturas
		 Calendar today = Calendar.getInstance();
		 today.setTimeInMillis(dateAux.getTime());
		 String periodAux = new String();
		 if((today.get(Calendar.DAY_OF_MONTH)-1) <= 15){
			 periodAux = "1"+new Integer(today.get(Calendar.MONTH)+1);
		 }else{
			 periodAux = "2"+new Integer(today.get(Calendar.MONTH)+1);
		 }
		 
		 if(!period.equals(periodAux)){
			 return false;
		 }
		 
		 return true;
	 } 
	 
	 
	 /**
	  *  Shows the searched order condition
	  */
	 private void showCreditNotificationCondition()
	 {
	 	//Muestro el mensaje segun la condicion	 	
	 	ADialog.warn(m_WindowNo, this.mainPanel, Msg.translate(Env.getCtx(), "XX_NoNotificationsForFilter"));	
	 }
	 
	 //crear la factura del aviso de credito
	 public int generateCreditNotify(MCreditNotifyReturn AvisoCredit,String notifyNo, String controlNo, int vendor,Timestamp dateDoc, int count){
		
		 int save =0;
			//System.out.println("proveedor "+vendor+" aviso "+AvisoCredit.getValue());
			 //Crear el nuevo registro de la factura
			 MInvoice notifyCredit = new MInvoice (Env.getCtx(), 0, null);
			 
			 MOrder mOrder = new MOrder(Env.getCtx(), AvisoCredit.getC_Order_ID(), null);
			 
			 if ("POM".equals(mOrder.getXX_POType()))
				 notifyCredit.setXX_InvoiceType("I");
			 else {
				 if ("POA".equals(mOrder.getXX_POType()) & ("SE".equals(mOrder.getXX_PurchaseType())))
					 notifyCredit.setXX_InvoiceType("S");
				 else 
					 notifyCredit.setXX_InvoiceType("A");
			 	}
			 
			 //System.out.println(notifyCredit.getXX_InvoiceType());
			 notifyCredit.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCD_ID")); 
			 notifyCredit.setDocStatus (X_C_Invoice.DOCSTATUS_Drafted);		//	Draft
			 notifyCredit.setC_DocType_ID(0);
			 notifyCredit.setC_BPartner_ID(vendor); 
			 notifyCredit.setC_BPartner_Location_ID(mOrder.getC_BPartner_Location_ID());
			 notifyCredit.setSalesRep_ID(Env.getCtx().getAD_User_ID());
			 notifyCredit.setIsApproved (false);
			 notifyCredit.setIsPaid (false);
			 notifyCredit.setIsInDispute(false);
			 notifyCredit.setIsSOTrx(false);
			 notifyCredit.setC_PaymentTerm_ID(mOrder.getC_PaymentTerm_ID());
			 notifyCredit.setPaymentRule(mOrder.getPaymentRule());
			 //Amounts are updated by trigger when adding lines
			 notifyCredit.setGrandTotal(new BigDecimal(0));
			 notifyCredit.setTotalLines(new BigDecimal(0));
			 notifyCredit.setIsTransferred (false);
			 notifyCredit.setPosted (false);
			 notifyCredit.setProcessed (false);		
			 notifyCredit.setSendEMail(false);
			 //fecha de creacion 
			 notifyCredit.setDateInvoiced (dateDoc);
			 notifyCredit.setDateAcct (dateDoc);
			 notifyCredit.setDatePrinted(null);
			 notifyCredit.setIsPrinted (false);
			 notifyCredit.setIsTaxIncluded(false);
			 notifyCredit.setIsDiscountPrinted(false);
			 notifyCredit.setIsPayScheduleValid(false);
			 notifyCredit.setIsReturnTrx(false);
			 notifyCredit.setIsSelfService(false);
			 notifyCredit.setXX_ApprovalDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));

			 //proveedor pendiente 
			 //setearle los valores del incluidos en el aviso de credito generado
			 
			 //orden de compra para la linea
			 notifyCredit.setC_Order_ID(AvisoCredit.getC_Order_ID());
			 //Se coloca una de las facturas de esta o/c (la de monto mas alto)
			 //Cargar combo por id de el aviso de credito
			 String sql = "SELECT i.C_Invoice_ID FROM C_Invoice i WHERE i.C_Order_ID = "+ AvisoCredit.getC_Order_ID() 
			 			+ " AND i.C_DocTypeTarget_ID = " + Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID");
			 
			 sql = MRole.getDefault().addAccessSQL(sql, "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);	
			 sql += " order by grandtotal desc";
			 PreparedStatement pstmt = null;
			 ResultSet rs = null;
			 try
			 {
				 pstmt = DB.prepareStatement(sql, null);
				 rs = pstmt.executeQuery();

				 if (rs.next())
				 {
					 notifyCredit.setRef_Invoice_ID(rs.getInt("C_Invoice_ID"));
				 }
		
			 }
			 catch (SQLException e)
			 {
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
			 
			 
			 //numero de control
			 notifyCredit.setDocumentNo(notifyNo);
			 notifyCredit.setXX_ControlNumber(controlNo);
			 //moneda
			 notifyCredit.setC_Currency_ID(AvisoCredit.getC_Currency_ID());
			 //tipo de documento Generado
			 notifyCredit.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID"));
			 //descripcion del documento generado
			 notifyCredit.setDescription(AvisoCredit.getDescription());
			 System.out.println(AvisoCredit.getDescription());
			 notifyCredit.save();
			 save=notifyCredit.getC_Invoice_ID();
			 String SQL2 = "SELECT XX_VCN_TRADEAGREEMENTS_ID from XX_VCN_TRADEAGREEMENTS" +
			 		" where C_BPARTNER_ID=" +vendor+ " and AD_CLIENT_ID="+  ctx.getAD_Client_ID()+
			 		" and DOCUMENTNOORDER= " +
			 		"(Select DOCUMENTNO from C_ORDER where C_ORDER_ID="+ AvisoCredit.getC_Order_ID()+")";
			 PreparedStatement pstmt1 = null;
			 ResultSet rs1 = null;
			 try
			 {
				 pstmt1 = DB.prepareStatement(SQL2, null);
				 rs1 = pstmt1.executeQuery();
		
				 if (rs1.next())
				 {
					 X_XX_VCN_TradeAgreements agreement = new X_XX_VCN_TradeAgreements(ctx, rs1.getInt(1), null);
					 agreement.setXX_Status("VENCIDO");
					 agreement.save();
				 }
					
				
			 }
			 catch (SQLException e)
			 {
				 log.log(Level.SEVERE, sql, e);
			 } finally{
				 try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				 try {
					pstmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			 }
			
			 
		return save;
	 }
	 
	 public int generarlineanotacredito(MCreditNotifyReturn AvisoCredit,MInvoice notifyCredit){
		
		 int lineId=0;
		 //System.out.println("entrar a linea del aviso de credito ");
		//generar una nueva linea(detalle del aviso) y setearle los valores que vienen en el aviso de credito
		 MInvoiceLine  NotyCredLine= new MInvoiceLine(Env.getCtx(), 0, null);
		 
		 NotyCredLine.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCD_ID"));
		 NotyCredLine.addDescription(AvisoCredit.getDescription());
		 NotyCredLine.setC_Invoice_ID(notifyCredit.getC_Invoice_ID());
		// NotyCredLine.setQtyInvoiced(AvisoCredit.getXX_Amount());
		 //System.out.println(" monto de Iva "+AvisoCredit.getXX_Amount_IVA());
		 
		 BigDecimal negative = new BigDecimal(-1);
		 NotyCredLine.setPriceActual(AvisoCredit.getXX_Amount().multiply(negative));
		 NotyCredLine.set_Value("XX_PriceActualInvoice", AvisoCredit.getXX_Amount().multiply(negative));
		 NotyCredLine.set_Value("XX_PriceEnteredInvoice", AvisoCredit.getXX_Amount().multiply(negative));
		 NotyCredLine.setPriceEntered(AvisoCredit.getXX_Amount().multiply(negative));
		 NotyCredLine.setC_Tax_ID(AvisoCredit.getC_Tax_ID());
	
		 NotyCredLine.setQtyInvoiced(new BigDecimal(1.00));
		 NotyCredLine.setQty(1);
		 NotyCredLine.setQtyInvoiced(new BigDecimal(1.00));
		 
		 //NotyCredLine.setLineNetAmt(new BigDecimal(0.00));
		 //agregar la columna se tax de a la tabla del aviso
		 NotyCredLine.setC_Tax_ID(AvisoCredit.getC_Tax_ID());
		 //LINENETAMT
		 NotyCredLine.save();
		 
		 lineId= NotyCredLine.getC_InvoiceLine_ID();
		
		 //valores obligatorios
		 //valores que vienen del aviso de credito
	    return lineId;
	 }
	 
	 boolean saveNotifyOnSummary(MCreditNotifyReturn creditNotify, MInvoice invoice){
		 BigDecimal multiplyRate = BigDecimal.ONE;
		 BigDecimal amount = creditNotify.getXX_Amount();
		 int countryID=0;
		 int warehouseCentDist = ctx.getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID");
		 String SQL1= "select C_COUNTRY_ID" +
	 		" from C_ORDER" +
	 		" where C_ORDER_ID="+invoice.getC_Order_ID()+
	 		" and AD_CLIENT_ID="+  ctx.getAD_Client_ID();
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 try
		 {
			 pstmt = DB.prepareStatement(SQL1, null);
			 rs = pstmt.executeQuery();

			 if (rs.next())
				 countryID=rs.getInt(1);
			
		 }
		 catch (SQLException e)
		 {
			 log.log(Level.SEVERE, SQL1, e);
			 return false;
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
		 
		 if(creditNotify.getC_Currency_ID()!=205){
			 String SQL= "select conv.MULTIPLYRATE" +
			 		" from C_CONVERSION_RATE conv, C_CURRENCY cur, C_PERIOD per" +
			 		" where conv.C_CURRENCY_ID="+creditNotify.getC_Currency_ID()+
			 		" and per.C_PERIOD_ID=conv.XX_PERIOD_ID" +
			 		" and conv.AD_CLIENT_ID="+  ctx.getAD_Client_ID()+
			 		" and sysdate between per.STARTDATE and per.ENDDATE";
			 PreparedStatement pstmt1 = null;
			 ResultSet rs1 = null;
			 try
			 {
				 pstmt1 = DB.prepareStatement(SQL, null);
				 rs1 = pstmt1.executeQuery();
				 //Se convierte a Bs.
				 if (rs1.next())
				 {
					 multiplyRate= new BigDecimal(rs1.getInt(1));
					 amount = amount.multiply(multiplyRate);
				 }
					
				 
			 }
			 catch (SQLException e)
			 {
				 log.log(Level.SEVERE, SQL, e);
				 return false;
			 }finally{
				 try {
					rs1.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				 try {
					pstmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			 }		 
		 }

		//Si es Centro de Distribución se distribuye en las tiendas
		 if (invoice.getM_Warehouse_ID()==warehouseCentDist){
			 String SQL3= "select M_WAREHOUSE_ID, RATE" +
		 		" from XX_VSI_DISTIMPORTCOSTS" +
		 		" where AD_CLIENT_ID="+  ctx.getAD_Client_ID()+
		 		" and sysdate between DATEFROM and DATETO";
			 PreparedStatement pstmt2 = null;
			 ResultSet rs2 = null;
			 try
			 {
				 pstmt2 = DB.prepareStatement(SQL3, null);
				 rs2 = pstmt2.executeQuery();
				 
				 while(rs2.next()){
					 X_XX_VLO_SUMMARY summary = new X_XX_VLO_SUMMARY(ctx, 0, null);
					 summary.setC_BPartner_ID(invoice.getC_BPartner_ID());
					 summary.setC_Order_ID(invoice.getC_Order_ID());
					 summary.setC_PaymentTerm_ID(invoice.getC_PaymentTerm_ID());
					 summary.setXX_VMR_Department_ID(invoice.getXX_VMR_Department_ID());
					 summary.setC_Country_ID(countryID);
					 summary.setDataType("4");
					 summary.setM_Warehouse_ID(rs2.getInt(1));
					 summary.setXX_Cosant(creditNotify.getXX_Amount().multiply(rs2.getBigDecimal(2).divide(new BigDecimal(100))));
					 summary.save();
				 }	
				 
			 }
			 catch (SQLException e)
			 {
				 log.log(Level.SEVERE, SQL3, e);
				 return false;
			 }finally{
				 try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				 try {
					pstmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			 }
		 }
		 //Si no es C.D se guarda el total
		 else{
			 X_XX_VLO_SUMMARY summary = new X_XX_VLO_SUMMARY(ctx, 0, null);
			 summary.setC_BPartner_ID(invoice.getC_BPartner_ID());
			 summary.setC_Order_ID(invoice.getC_Order_ID());
			 summary.setC_PaymentTerm_ID(invoice.getC_PaymentTerm_ID());
			 summary.setXX_VMR_Department_ID(invoice.getXX_VMR_Department_ID());
			 summary.setC_Country_ID(countryID);
			 summary.setDataType("4");
			 summary.setM_Warehouse_ID(invoice.getM_Warehouse_ID());
			 summary.setXX_Cosant(creditNotify.getXX_Amount());
			 summary.save();
		 }
			 
			 
		 
		 return true;
	 }
	 
}
