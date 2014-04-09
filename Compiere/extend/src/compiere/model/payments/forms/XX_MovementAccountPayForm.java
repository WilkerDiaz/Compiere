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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.EnvConstants;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
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

import java.text.NumberFormat;

import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;


/**
 * Forma en compiere: Movement Account Pay
 * 
 * Contiene todas las deudas que se tienen pendientes por pagar a los proveedores
 * Además contiene todos los documentos asociados a un proveedor específico 
 * 
 * @author Jessica Mendoza
 *
 */
public class XX_MovementAccountPayForm extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	/****Window No****/
	private int m_WindowNo = 0;
	
	/****FormFrame****/
	private FormFrame m_frame;
	
	/****Logger****/
	static CLogger log = CLogger.getCLogger(XX_MovementAccountPayForm.class);
	
	/****Cliente****/
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	private int m_rol = Env.getCtx().getAD_Role_ID();
	StringBuffer m_sql = null;
	StringBuffer m_sqlUnion = null;
	String m_groupBy = "";
	String m_groupByUnion = "";
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
	private JScrollPane xScrollPaneDocument = new JScrollPane();
	private MiniTable xTableDocument = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private TitledBorder xBorderPartner =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_AccountPayable"));
	private TitledBorder xBorderDocument =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_DetailAPayable"));

	/****Botones****/
	private CButton bSearch = new CButton();
	private JButton bReset = new CButton();
	private JButton bFinal = new CButton();
	
	/****Labels-ComboBox-Fecha****/
	private CLabel categoryPartnerLabel = new CLabel();
	private CComboBox categoryPartnerCombo = new CComboBox();
	private CLabel partnerLabel = new CLabel();
	private CComboBox partnerCombo = new CComboBox();
	private CLabel categoryLabel = new CLabel();
	private CComboBox categoryCombo = new CComboBox();
	private CLabel typePartnerLabel = new CLabel();
	private CComboBox typePartnerCombo = new CComboBox();
	private CLabel totalPagoLabel = new CLabel();
	private CLabel montoLabel = new CLabel();
	
	/****Variables****/
	float cuenta = 0;
	String cuentaS;
	static Integer sync = new Integer(0);
	Utilities util = new Utilities();
	NumberFormat formato = NumberFormat.getInstance();
	int count = 0;

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
			dynInitSecondM();
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
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CategoryComercial"));
		typePartnerLabel.setText(Msg.getMsg(Env.getCtx(), "XX_OrderType"));
		totalPagoLabel.setText(Msg.getMsg(Env.getCtx(), "XX_BalancePartner"));
		partnerLabel.setText(Msg.getMsg(Env.getCtx(), "BPartner"));
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearch.setEnabled(true);
		bSearch.addActionListener(this);
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		bReset.addActionListener(this);
		bFinal.setText(Msg.getMsg(Env.getCtx(), "XX_Unlock"));
		bFinal.addActionListener(this);

		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		xPanel.setLayout(xLayout);
		
		centerPanel.setLayout(centerLayout);
		xScrollPanePartner.setBorder(xBorderPartner);
		xScrollPanePartner.setPreferredSize(new Dimension(920, 220));
		xPanel.setLayout(xLayout);
		
		southPanel.setLayout(southLayout);
		xScrollPaneDocument.setBorder(xBorderDocument);
		xScrollPaneDocument.setPreferredSize(new Dimension(920,220));
		xPanel.setLayout(xLayout);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xScrollPanePartner,  BorderLayout.CENTER);
		southPanel.add(xScrollPaneDocument, BorderLayout.CENTER);
		xScrollPanePartner.getViewport().add(xTablePartner);
		xScrollPaneDocument.getViewport().add(xTableDocument);
		
		/**
		 * Panel Superior
		 */		
		/****Etiquetas de la primera fila****/
		northPanel.add(partnerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(partnerCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 20, 0)); //donde esta el 20, es donde se indica el largo del campo
		northPanel.add(typePartnerLabel, new GridBagConstraints(2, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(typePartnerCombo, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));

		/****Etiquetas de la segunda fila****/
		northPanel.add(categoryPartnerLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryPartnerCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(categoryLabel, new GridBagConstraints(2, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(3, 1, 1, 1, 0.0,
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

		categoryCombo.removeActionListener(this);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
	}
	
	/**
	 * Carga los socio de negocios
	 */
	void dynPartner(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlPartner = "select C_BPartner_ID, name " +
							"from C_BPartner " +
							"order by name ";
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();

			partnerCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				partnerCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			partnerCombo.addActionListener(this);
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
	 * Carga los tipos de facturas (bienes, servicios, productos para la venta)
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

			categoryPartnerCombo.addItem(new KeyNamePair(-1, null));
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
	}
	
	/**
	 * Carga la lista que contiene los tipos de ordenes (nacional, importada)
	 */
	void dynTypePartner(){
		//typePartnerCombo.addItem("");
		int i=0;
		for( X_Ref_XX_OrderType v : X_Ref_XX_OrderType.values()) {
			typePartnerCombo.addItem (v.getValue());
			i++;
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
				/****0-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),				
				/****1-Total General****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalOverall"), ".", float.class),
				/****2-Bloqueado por devoluciones****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_BlockedReturns"), ".", String.class)
		};			
		xTablePartner.prepareTable(layoutF, "", "", true, "");
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		CompiereColor.setBackground (this);
		xTablePartner.getSelectionModel().addListSelectionListener(this);		
		xTablePartner.getColumnModel().getColumn(0).setMinWidth(300);
		xTablePartner.getColumnModel().getColumn(0).setCellRenderer(renderLeft);
		xTablePartner.getColumnModel().getColumn(1).setMinWidth(100);	
		xTablePartner.getColumnModel().getColumn(1).setCellRenderer(renderRight);
		xTablePartner.getColumnModel().getColumn(2).setMinWidth(100);	
		xTablePartner.getColumnModel().getColumn(2).setCellRenderer(renderCenter);
		
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
			
				KeyNamePair idPartner = (KeyNamePair) xTablePartner.getValueAt(xTablePartner.getSelectedRow(), 0);
				tableInitSM(idPartner);
				/*String tipo = tipoPartner(idPartner.getName());
				if (tipo == null)
					tableInitSM(idPartner);
				else{
					if (tipo.equals("POM"))
						tableInitSM(idPartner);
					else 
						tableInitS(idPartner);
				}*/
			}
		});	
	}

	/**
	 * Genera las columnas para la segunda tabla (Para mercancia)
	 */
	private void dynInitSecondM(){
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutT = new ColumnInfo[] {			
				/****0-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),	
				/****1-Factura****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "Invoice"), ".", String.class),
				/****2-OC/Contrato****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderContract"), ".", String.class),	
				/****3-Documento****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "ArchivedDocuments"), ".", KeyNamePair.class),
				/****4-Departamento****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Department"), ".", KeyNamePair.class),
				/****5-Categoria****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Category"), ".", KeyNamePair.class),
				/****6-Pais****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Country"), ".", KeyNamePair.class),
				/****7-Moneda****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Currency_ID"), ".", KeyNamePair.class),
				/****8-Base****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Base"), ".", float.class),
				/****9-IVA****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Tax"), ".", float.class),
				/****10-Retencion IVA****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WithholdingPay"), ".", float.class),
				/****11-Retencion ISLR****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RetainedAmountISLR"), ".", float.class),
				/****12-Monto****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountGeneral"), ".", float.class),
				/****13-Anticipo****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Advance"), ".", float.class),
				/****14-Condicion de Pago****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ConditionPay"), ".", KeyNamePair.class),
				/****15-Fecha de entrada****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DateEntrace"), ".", String.class),
				/****16-Fecha de vencimiento****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DueDate"), ".", String.class),};
		
		xTableDocument.prepareTable(layoutT, "", "", true, "");
		CompiereColor.setBackground (this);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		xTableDocument.getSelectionModel().addListSelectionListener(this);		
		xTableDocument.getColumnModel().getColumn(0).setMinWidth(300);
		xTableDocument.getColumnModel().getColumn(0).setCellRenderer(renderLeft);
		xTableDocument.getColumnModel().getColumn(1).setMinWidth(80);
		xTableDocument.getColumnModel().getColumn(1).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(2).setMinWidth(80);
		xTableDocument.getColumnModel().getColumn(2).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(3).setMinWidth(80);
		xTableDocument.getColumnModel().getColumn(3).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(4).setMinWidth(170);
		xTableDocument.getColumnModel().getColumn(4).setCellRenderer(renderLeft);
		xTableDocument.getColumnModel().getColumn(5).setMinWidth(80);
		xTableDocument.getColumnModel().getColumn(5).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(6).setMinWidth(60);
		xTableDocument.getColumnModel().getColumn(6).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(7).setMinWidth(80);
		xTableDocument.getColumnModel().getColumn(7).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(8).setMinWidth(60);
		xTableDocument.getColumnModel().getColumn(8).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(9).setMinWidth(60);
		xTableDocument.getColumnModel().getColumn(9).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(10).setMinWidth(110);
		xTableDocument.getColumnModel().getColumn(10).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(11).setMinWidth(120);
		xTableDocument.getColumnModel().getColumn(11).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(12).setMinWidth(90);
		xTableDocument.getColumnModel().getColumn(12).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(13).setMinWidth(90);
		xTableDocument.getColumnModel().getColumn(13).setCellRenderer(renderRight);
		xTableDocument.getColumnModel().getColumn(14).setMinWidth(140);
		xTableDocument.getColumnModel().getColumn(14).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(15).setMinWidth(110);
		xTableDocument.getColumnModel().getColumn(15).setCellRenderer(renderCenter);
		xTableDocument.getColumnModel().getColumn(16).setMinWidth(130);
		xTableDocument.getColumnModel().getColumn(16).setCellRenderer(renderCenter);
	}
	
	/**
	 * Carga la primera tabla del panel central norte
	 */
	private void tableInitF(){
		BigDecimal cuenta = new BigDecimal(0);
		String tipoOrder = "";
		m_sql = new StringBuffer();
		m_sqlUnion = new StringBuffer();

		m_sql.append("select round((inv.GrandTotal - " +
					 "nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0) - " +
					 "coalesce((select withholdingTaxInvoice(inv.C_Invoice_ID) from dual),0)),2) as total, " +
			         "par.name as partner, par.C_BPartner_ID, sta.name " +
			         "from C_Invoice inv " +
			         "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
			         "left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
			         "left outer join XX_VMR_Department dep on (inv.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
			         "inner join C_BP_Status sta  on (sta.C_BP_Status_ID = par.C_BP_Status_ID) " +
					 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
			         "where inv.XX_AccountPayableStatus = 'A' " +
			     	 "and inv.DocStatus = 'CO' " +
			     	 "and inv.isSoTrx = 'N' " +
			     	 "and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) " +
			     	 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') ");
		m_groupBy = "group by par.name, par.C_BPartner_ID, sta.name, inv.GrandTotal, inv.C_Invoice_ID ";
		m_orderBy = "order by par.name asc ";
		
		m_sqlUnion.append("union " +
						  "select round(pay.PayAmt * (-1),2) total, " +
						  "par.name as partner, par.C_BPartner_ID, sta.name " +
						  "from C_Payment pay " +
						  "inner join C_BPartner par on (par.C_BPartner_ID = pay.C_BPartner_ID) " +
						  "inner join C_Order ord on (ord.C_Order_ID = pay.C_Order_ID) " +
						  "inner join C_BP_Status sta  on (sta.C_BP_Status_ID = par.C_BP_Status_ID) " +
						  "where pay.DocStatus = 'CO' " +
						  "and pay.XX_IsAdvance = 'Y' " +
						  "and pay.XX_AccountPayableStatus = 'A' " +
						  "and pay.C_Order_ID is not null ");
		m_groupByUnion = "group by par.name, par.C_BPartner_ID, sta.name, pay.PayAmt ";
		
		m_orderBy = "order by 2 asc ";

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Buscar el proveedor seleccionado****/
		if ((partnerCombo.getSelectedItem() != null) && (partnerCombo.getSelectedIndex() != 0)){
			m_sql.append("and par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
			m_sqlUnion.append("and par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
		}
		/****Buscar los proveedores segun el tipo de factura****/
		if ((categoryPartnerCombo.getSelectedIndex() != 0) && (categoryPartnerCombo.getSelectedItem() != null)){
			if(((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey() != 0){
				int tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				String tipoFacturaValue = "";
				if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID"))){
					tipoFacturaValue = "A";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType in ('FA','SU') ";
				}else if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID"))){
					tipoFacturaValue = "S";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE' ";
				}else if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					tipoFacturaValue = "I";
					tipoOrder = "and ord.XX_POType = 'POM' ";
				}
				m_sql.append("and ").append("inv.XX_InvoiceType = '").append(tipoFacturaValue).append("' ");
				m_sqlUnion.append(tipoOrder);
			}
		}
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedIndex() != 0) && (categoryCombo.getSelectedItem() != null)){
			if (categoryPartnerCombo.getSelectedIndex() != 0){
				int tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() != 0){
						int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
						m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
					}
				}
			}else{
				if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() != 0){
					int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
					m_sql.append("and ").append("inv.XX_InvoiceType = 'I' ");
					m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
				}
			}			
		}
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if (typePartnerCombo.getSelectedItem() != null){
			m_sql.append("and ").append("(ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).
				append("' or con.XX_ContractType like '").append(typePartnerCombo.getSelectedItem()).append("%') ");
			m_sqlUnion.append("and ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).append("' ");
		}

		String SQL = "select sum(u.total), u.partner, u.C_BPartner_ID, u.name " +
					 "from (" + 
				     m_sql.toString() + m_groupBy.toString() + m_sqlUnion.toString() + m_groupByUnion + m_orderBy.toString() +
				     ") u " +
				     "group by u.partner, u.C_BPartner_ID, u.name " +
				     "order by 2 asc ";
		
		int i = 0;
		String devTienda;
		xTablePartner.setRowCount(i);
		xTableDocument.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		try {	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTablePartner.setRowCount (i+1);		
				/****Proveedor****/
				xTablePartner.setValueAt(
					new KeyNamePair (rs.getInt("C_BPartner_ID"), rs.getString(2)),i,0);
				/****Monto****/
				xTablePartner.setValueAt(formato.format(rs.getBigDecimal(1).setScale(2)), i, 1);
				cuenta = cuenta.add(rs.getBigDecimal(1)).setScale(2);
				/****Bloqueado por devoluciones****/
				devTienda = rs.getString(4);
				if (devTienda.equals("Bloqueado")){
					xTablePartner.setValueAt("Si", i, 2);
				}else{
					xTablePartner.setValueAt("No", i, 2);
				}
				i++;				
			}
			montoLabel.setText(formato.format(cuenta)); //es la suma del total general de todos los socio de negocios
			montoLabel.setFont(new Font("Serif", Font.BOLD,12));			
		}
		catch(SQLException e){	
			e.getMessage();
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
	
	/**
	 * Carga la segunda tabla del panel sur (Para mercancia)
	 */
	private void tableInitSM(KeyNamePair idPartner){
		xTableDocument.getSelectionModel().removeListSelectionListener(this);
		m_sql = new StringBuffer();
		m_sqlUnion = new StringBuffer();
		String tipoOrder = "";
		
		/**
		 * Trae todos los proveedores que tengan como estado de cuentas por pagar la opcion ACTIVO
		 */
		m_sql.append("select par.C_BPartner_ID, par.name, inv.documentNo invPay1, inv.C_Invoice_ID invPay2, ord.documentNo, doc.C_DocType_ID, " +
					 "to_char(doc.name) as nameDoc, to_char(dep.value), to_char(dep.name), to_char(cat.name), con.C_Country_ID, con.name, " +
					 "cur.C_Currency_ID, cur.ISO_CODE, round(inv.TotalLines,2) as TotalLines, round(inv.XX_TaxAmount,2) as XX_TaxAmount, " +
					 "round((inv.GrandTotal - " +
					 "(nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a where inv.C_Invoice_ID = a.C_Invoice_ID),0)) - " +
					 "(coalesce((select WithHoldingTaxInvoice(inv.C_Invoice_ID) from dual),0))),2) GrandTotal, " +
					 "pat.C_PaymentTerm_ID, pat.name, " +
					 "to_char(inv.DateInvoiced,'dd/mm/yyyy'), to_char(inv.XX_DueDate,'dd/mm/yyyy'), " +
					 "cot.XX_Contract_ID, " +
					 "round((select nvl(sum(p.xx_retainedamount),0) from XX_VCN_ISLRAMOUNT p where p.c_invoice_id = inv.c_invoice_id),2) RETAINEDISLR, " +
					 "nvl(round((select withHoldingTaxInvoice(inv.C_Invoice_ID) from dual),2),0) as retencion " +
					 "from C_Invoice inv " +
					 "left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " + 
					 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " + 
					 "inner join C_DocType doc on (doc.C_DocType_ID = inv.C_DocTypeTarget_ID) " +
					 "inner join C_Currency cur on (cur.C_Currency_ID = inv.C_Currency_ID) " +
					 "left outer join XX_VMR_Department dep on (inv.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
					 "left outer join C_Country con on (con.C_Country_ID = ord.C_Country_ID) " +
					 "inner join C_PaymentTerm pat on (pat.C_PaymentTerm_ID = inv.C_PaymentTerm_ID) " +
					 "left outer join XX_VMR_Category cat on (dep.XX_VMR_Category_ID = cat.XX_VMR_Category_ID) " +
					 "left outer join XX_Contract cot on (inv.XX_Contract_ID = cot.XX_Contract_ID) " +
					 "where inv.XX_AccountPayableStatus = 'A' " + 
					 "and inv.DocStatus = 'CO' " +
					 "and inv.isSoTrx = 'N' " +
					 "and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) " +
					 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') " +
					 "and par.C_BPartner_ID = " + idPartner.getKey() + " "); 

		m_sqlUnion.append("union select par.C_BPartner_ID, par.name, pay.documentNo invPay1, " +
						 "pay.C_Payment_ID invPay2, ord.documentNo, doc.C_DocType_ID, " +
						 "(case when doc.name = 'AP Payment' then 'Anticipo' else to_char(doc.name) end) as nameDoc, to_char(' '), " +
						 "to_char(' '), to_char(' '), con.C_Country_ID, con.name, cur.C_Currency_ID, cur.ISO_CODE, 0, 0, " +
						 "round((pay.PayAmt * (-1)),2), pat.C_PaymentTerm_ID, pat.name, to_char(pay.DateTrx,'dd/mm/yyyy') dateMov, " +
						 "null, 0, 0, 0 " +
						 "from C_Payment pay " +
						 "inner join C_Order ord on (pay.C_Order_ID = ord.C_Order_ID) " +
						 "inner join C_BPartner par on (pay.C_BPartner_ID = par.C_BPartner_ID) " +
						 "inner join C_DocType doc on (doc.C_DocType_ID = pay.C_DocType_ID) " +
						 "inner join C_Currency cur on (cur.C_Currency_ID = pay.C_Currency_ID) " +
						 "left outer join C_Country con on (con.C_Country_ID = ord.C_Country_ID) " +
						 "left outer join C_PaymentTerm pat on (pat.C_PaymentTerm_ID = par.C_PaymentTerm_ID) " +
						 "where pay.DocStatus = 'CO' " +
						 "and pay.C_Order_ID is not null " +
						 "and pay.XX_IsAdvance = 'Y' " +
						 "and XX_AccountPayableStatus = 'A' " +
						 //"and XX_SynchronizationBank = 'Y' " +
						 "and par.C_BPartner_ID = " + idPartner.getKey()).append(" ");
		
		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Buscar el proveedor seleccionado****/
		if ((partnerCombo.getSelectedItem() != null) && (partnerCombo.getSelectedIndex() != 0)){
			m_sql.append("and par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
			m_sqlUnion.append("and par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
		}
		/****Buscar los proveedores segun el tipo de factura****/
		if ((categoryPartnerCombo.getSelectedIndex() != 0) && (categoryPartnerCombo.getSelectedItem() != null)){
			if(((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey() != 0){
				int tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				String tipoFacturaValue = "";
				if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID"))){
					tipoFacturaValue = "A";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType in ('FA','SU') ";
				}else if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID"))){
					tipoFacturaValue = "S";
					tipoOrder = "and ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE' ";
				}else if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					tipoFacturaValue = "I";
					tipoOrder = "and ord.XX_POType = 'POM' ";
				}
				m_sql.append("and ").append("inv.XX_InvoiceType = '").append(tipoFacturaValue).append("' ");
				m_sqlUnion.append(tipoOrder);
			}
		}
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedIndex() != 0) && (categoryCombo.getSelectedItem() != null)){
			if (categoryPartnerCombo.getSelectedItem() != null){
				int tipoFactura = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
				if (tipoFactura == Integer.parseInt(Env.getCtx().getContext("#XX_L_ITEMMPRODUCT_ID"))){
					if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() != 0){
						int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
						m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
					}
				}
			}else{
				if(((KeyNamePair)categoryCombo.getSelectedItem()).getKey() != 0){
					int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
					m_sql.append("and ").append("inv.XX_InvoiceType = 'I' ");
					m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" "); 
				}
			}			
		}
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if ((typePartnerCombo.getSelectedIndex() != 0) && (typePartnerCombo.getSelectedItem() != null)){
			m_sql.append("and ").append("(ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).
				append("' or cot.XX_ContractType like '").append(typePartnerCombo.getSelectedItem()).append("%') ");
			m_sqlUnion.append("and ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).append("' ");
		}

		String SQL = m_sql.toString() + m_sqlUnion.toString() + "order by 5";
		int i = 0;
		xTableDocument.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTableDocument.setRowCount (i+1);		
				/****Proveedor****/
				xTableDocument.setValueAt(
					new KeyNamePair (rs.getInt("C_BPartner_ID"), rs.getString(2)),i,0);
				/****Factura****/
				xTableDocument.setValueAt(rs.getString(3), i, 1);
				/****Orden****/
				if (rs.getString(5) == null)
					xTableDocument.setValueAt(rs.getString(21), i, 2);
				else
					xTableDocument.setValueAt(rs.getString(5), i, 2);
				/****Documento****/
				xTableDocument.setValueAt(
					new KeyNamePair (rs.getInt("C_DocType_ID"), rs.getString(7)), i, 3);
				/****Departamento****/
				if ((rs.getString(5) == null) || (rs.getString(7).equals("Anticipo")) || (rs.getString(8) == null))
					xTableDocument.setValueAt("", i, 4);
				else 
					xTableDocument.setValueAt("  " + rs.getString(8) + " - " + rs.getString(9), i, 4);
				/****Categoria****/
				if (rs.getString(5) == null)
					xTableDocument.setValueAt("", i, 5);
				else
					xTableDocument.setValueAt(rs.getString(10), i, 5);
				/****Pais****/
				if (rs.getString(5) == null)
					xTableDocument.setValueAt(nombrePais(rs.getInt("C_Currency_ID")), i, 6);
				else
					xTableDocument.setValueAt(
					new KeyNamePair (rs.getInt("C_Country_ID"), rs.getString(12)), i, 6);
				/****Moneda****/
				xTableDocument.setValueAt(
						new KeyNamePair (rs.getInt("C_Currency_ID"), rs.getString("Iso_Code")), i, 7);
				/****Base****/
				xTableDocument.setValueAt(formato.format(rs.getBigDecimal("TotalLines").setScale(2)), i, 8);
				/****IVA****/
				xTableDocument.setValueAt(formato.format(rs.getBigDecimal("XX_TaxAmount").setScale(2)), i, 9);
				/****Retencion****/
				xTableDocument.setValueAt(formato.format(rs.getBigDecimal("retencion").setScale(2)), i, 10);	
				/****Retencion ISLR****/
				xTableDocument.setValueAt(formato.format(rs.getBigDecimal("RETAINEDISLR").setScale(2)), i, 11);
				/****Monto****/
				if (rs.getString(7).equals("Anticipo"))
					xTableDocument.setValueAt(formato.format(new BigDecimal(0)), i, 12);
				else
					xTableDocument.setValueAt(formato.format(rs.getBigDecimal(17).setScale(2)), i, 12);
				/****Anticipo****/
				if (rs.getString(7).equals("Anticipo"))
					xTableDocument.setValueAt(formato.format(rs.getBigDecimal(17)), i, 13);
				else
					xTableDocument.setValueAt(formato.format(new BigDecimal(0)), i, 13);
				/****Condicion de Pago****/
				xTableDocument.setValueAt(
						new KeyNamePair (rs.getInt("C_PaymentTerm_ID"), rs.getString(19)), i, 14);
				/****Fecha de entrada****/
				xTableDocument.setValueAt(rs.getString(20), i, 15);
				/****Fecha de vencimiento****/
				xTableDocument.setValueAt(rs.getString(21), i, 16);
				i++;				
			}		
		}
		catch(SQLException e){	
			e.getMessage();			
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
			cmdSearch();
		}
	}
	
	/**
	 *  Busca la informacion a través de los filtros 
	 */
	private void cmdSearch(){	
		tableInitF();
	}
	
	/**
	 * Busca el nombre del pais, que está asociado a una moneda
	 * @param moneda identificador de la moneda
	 * @return
	 */
	public String nombrePais(int moneda){
		String pais = "";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String sql = "select name from C_Country where C_Currency_ID = " + moneda;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				pais = rs.getString("name");
		}
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
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
		return pais;
	}

	/**
	 * Busca el tipo de orden de compra que tiene el socio de negocio
	 * @return
	 */
	public String tipoPartner(String namePartner){
		String tipo = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String sql = "select ord.XX_POType " +
		 			 "from C_Order ord, C_BPartner par " +
		 			 "where ord.C_BPartner_ID = par.C_BPartner_ID " +
		 			 "and par.name = '" + namePartner + "' ";
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				tipo = rs.getString("XX_POType");
		}
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
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
		return tipo;
	}
	/**
	 *  Remueve los items de todos los campos filtros
	 */
	private void removeItemsToReset(){
		categoryPartnerCombo.removeAllItems();
		categoryCombo.removeAllItems();
		typePartnerCombo.removeAllItems();
		partnerCombo.removeAllItems();
		llenarCombos();
	}

	/**
	 *  Desbloqueara los Socios de negocio, cambiando el C_BP_Status a 'activo'.
	 *  Ademas se valida que los roles autorizados para ejecutar el proceso 
	 *  son: Coordinador de Cuentas por pagar y Jefe de Finanzas.
	 */
	private void cmdFinal(){
		int idCoordCXP = Env.getCtx().getContextAsInt("#XX_L_ROLECOORDAPAYABLE_ID");
		int idJefeFinanzas = Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID");
		if ((m_rol == idCoordCXP) || (m_rol == idJefeFinanzas)){
			int row = xTablePartner.getRowCount();
			KeyNamePair partner ;
			String status;
			int idStatus = 0;
			for (int i = 0; i < row ; i++){
				partner = (KeyNamePair) xTablePartner.getValueAt(i, 0);
				status = (String) xTablePartner.getValueAt(i, 2);
				if (status.equals("Si")){
					status = "Activo";
				}
				idStatus = returnIdStatus(status);
				PreparedStatement pstmtUpd = null;
				String sqlUpdate = " update C_BPartner " +
								   " set C_BP_Status_ID = " + idStatus +
								   " where C_BPartner_ID = " + partner.getKey(); 
				try {
					DB.executeUpdate(null, sqlUpdate);
				} catch (Exception e) {
					log.log(Level.SEVERE, e.getMessage());
				} finally{
					DB.closeStatement(pstmtUpd);
				}
			}
		}else{
			ADialog.info(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(Env.getCtx(), "XX_RoleIncorrect"));
		}
	}
	
	/**
	 *
	 * @return el id del status correspondiente al nombre del parametro
	 */
	public int returnIdStatus(String status){
		int idStatus = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String sql = "select C_BP_Status_ID " +
					 "from C_BP_Status " +
					 "where name = '" +status+ "' ";		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				idStatus = rs.getInt("C_BP_Status_ID");
			}
			
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
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
		return idStatus;
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
		dynPartner();
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
	
}
