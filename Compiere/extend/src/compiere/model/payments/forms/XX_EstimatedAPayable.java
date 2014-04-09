package compiere.model.payments.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

import java.text.NumberFormat;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
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

import compiere.model.birt.BIRTReport;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.payments.X_Ref_XX_POType_LV;


/**
 * Forma en compiere: Estimated Account Payable
 * 
 * 
 * @author Jessica Mendoza
 *
 */
public class XX_EstimatedAPayable extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	/****Window No****/
	private int m_WindowNo = 0;
	
	/****FormFrame****/
	private FormFrame m_frame;
	
	/****Logger****/
	static CLogger log = CLogger.getCLogger(XX_EstimatedAPayable.class);
	
	/****Cliente****/
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	
	/****Variables para el Query****/
	StringBuffer m_sql = null;
	String m_groupBy = "";
	String m_orderBy = "";
	Ctx ctx=Env.getCtx();
	
	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private JScrollPane xScrollPane = new JScrollPane();
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private TitledBorder xBorderPartner =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_EstimatedAPayable"));

	/****Botones****/
	private CButton bSearch = new CButton();
	private JButton bReset = new CButton();
	private CButton bReport = new CButton("Exportar a Excel");
	
	/****Labels-ComboBox-Fecha****/
	private CLabel typePartnerLabel = new CLabel();
	private CComboBox typePartnerCombo = new CComboBox();
	private CLabel partnerLabel = new CLabel();
	private CComboBox partnerCombo = new CComboBox();
	private CLabel typeOrderLabel = new CLabel();
	private CComboBox typeOrderCombo = new CComboBox();
	private CLabel dateLabel = new CLabel();
	private VDate date = new VDate(Msg.translate(Env.getCtx(), "XX_DateEstimated"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "XX_DateEstimated"));
	private CLabel currencyLabel = new CLabel();
	private CComboBox currencyCombo = new CComboBox();
	private CLabel companyImportLabel = new CLabel();
	private CComboBox companyImportCombo = new CComboBox();
	private CLabel categoryLabel = new CLabel();
	private CComboBox categoryCombo = new CComboBox();
	private CLabel payForeignLabel = new CLabel();
	private CLabel amountForeign = new CLabel();
	private CLabel payLocalLabel = new CLabel();
	private CLabel amountLocal = new CLabel();
	
	/****Variables****/
	static Integer sync = new Integer(0);
	Utilities util = new Utilities();
	NumberFormat formato = NumberFormat.getInstance();
	String poTypeValue = "";
	String poType = "";

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
		frame.setResizable(false); //Se utiliza para bloquear el boton maximizar de la ventana
		
		try{
			jbInit();
			dynInitFirst();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);	
		}catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * Organiza las posiciones de cada etiqueta en la forma
	 * @throws Exception
	 */
	private void jbInit() throws Exception{
		/****Seteo de las etiquetas****/		
		typeOrderLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TypePO"));
		dateLabel.setText(Msg.translate(Env.getCtx(), "XX_DateEstimated"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CategoryComercial"));
		typePartnerLabel.setText(Msg.translate(Env.getCtx(), "XX_TypeOrder"));
		partnerLabel.setText(Msg.getMsg(Env.getCtx(), "BPartner"));
		currencyLabel.setText(Msg.translate(Env.getCtx(), "C_Currency_ID"));
		companyImportLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CompanyImport"));
		payForeignLabel.setText(Msg.translate(Env.getCtx(), "XX_AmountForeign"));
		payLocalLabel.setText(Msg.translate(Env.getCtx(), "XX_AmountLocal"));
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearch.setEnabled(true);
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		bReset.addActionListener(this);
		
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		xPanel.setLayout(xLayout);
		
		centerPanel.setLayout(centerLayout);
		xScrollPane.setBorder(xBorderPartner);
		xScrollPane.setPreferredSize(new Dimension(920, 220));
		xPanel.setLayout(xLayout);
		
		southPanel.setLayout(southLayout);
		xPanel.setLayout(xLayout);
		bReport.setEnabled(false);

		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		xScrollPane.getViewport().add(xTable);

		/**
		 * Panel Superior
		 */		
		/****Etiquetas de la primera fila****/
		northPanel.add(companyImportLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(companyImportCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0)); 
		northPanel.add(currencyLabel, new GridBagConstraints(2, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(currencyCombo, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		currencyCombo.setBackground(false);
		currencyCombo.setBackground(Color.PINK);
		northPanel.add(dateLabel, new GridBagConstraints(4, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(date, new GridBagConstraints(5, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0)); 

		/****Etiquetas de la segunda fila****/
		northPanel.add(partnerLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(partnerCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(typePartnerLabel, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(typePartnerCombo, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		/****Etiquetas de la tercera fila****/
		northPanel.add(typeOrderLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(typeOrderCombo, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(categoryLabel, new GridBagConstraints(2, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(3, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		categoryCombo.setEditable(false);
		categoryCombo.setEnabled(false);
			
		/****Etiquetas de la cuarta fila****/
		northPanel.add(bSearch, new GridBagConstraints(5, 2, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 0, 5, 0), 0, 0));		 
		northPanel.add(bReset, new GridBagConstraints(5, 2, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 80, 5, 0), 0, 0));
		
		northPanel.add(bReport, new GridBagConstraints(5, 3, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0, 20, 0, 10), 0, 0)); 
		
		/**
		 * Panel Inferior     
		 */
		southPanel.add(payForeignLabel,   new GridBagConstraints(1, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 12, 5), 0, 0));
		southPanel.add(amountForeign,   new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 12, 0), 0, 0));
		southPanel.add(payLocalLabel,   new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 10, 12, 5), 0, 0));
		southPanel.add(amountLocal,   new GridBagConstraints(4, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 12, 0), 0, 0));
		southPanel.validate();
	}

	/**
	 * Carga las categorias comerciales (moda, hogar, deporte, niños, belleza)
	 */
	void dynCategory() {
		if (typeOrderCombo.getSelectedIndex()>0){
			if (typeOrderCombo.getSelectedItem().equals("O/C Mercancía")){
				categoryCombo.setEditable(true);
				categoryCombo.setEnabled(true);
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
			}else{
				categoryCombo.removeAllItems();
				categoryCombo.setEditable(false);
				categoryCombo.setEnabled(false);
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
	 * Carga la lista que contiene los tipos de ordenes (nacional, importada)
	 */
	void dynTypePartner(){
		typePartnerCombo.addItem("");
		int i = 0;
		for( X_Ref_XX_OrderType v : X_Ref_XX_OrderType.values()) {
			typePartnerCombo.addItem (v.getValue());
			i++;
		}	
	}
	
	/**
	 * Carga la lista que contiene los tipo de ordenes de compra 
	 * (OC de mercancia y OC de bienes y servicios)
	 */
	void dynOrderType(){
		typeOrderCombo.addItem("");
		int i = 0;
		for( X_Ref_XX_POType_LV v : X_Ref_XX_POType_LV.values()){
			if (v.getValue().equals("POM"))
				typeOrderCombo.addItem("O/C Mercancía");
			else
				typeOrderCombo.addItem("O/C Bienes y Servicios");
			i++;
		}
		
		/****Captura el evento para realizar otra accion****/
		typeOrderCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {

			@Override
			public void contentsChanged(ListDataEvent e) {
				if(typeOrderCombo.getSelectedIndex()>0){
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
	 * Carga la lista que contiene las compañias importadoras
	 */
	void dynCompanyImport(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlCompany = "select XX_VSI_Client_ID, name " +
						    "from XX_VSI_Client " +
						    "order by name ";
		try {
			pstmt = DB.prepareStatement(sqlCompany, null);
			rs = pstmt.executeQuery();
			
			companyImportCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				companyImportCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			companyImportCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlCompany, e);
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
	 * Carga la lista que contiene los tipos de moneda
	 */
	void dynCurrency(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlCurrency = "select C_Currency_ID, Iso_Code||' - '||description " +
							"from C_Currency " +
							"order by Iso_Code||' - '||description ";
		try {
			pstmt = DB.prepareStatement(sqlCurrency, null);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				currencyCombo.addItem(new KeyNamePair(rs.getInt(1), 
						rs.getString(2)));
			}
			
			currencyCombo.setSelectedItem(new KeyNamePair(205, "VEB - Bolivar")); //coloca por defecto VEB
			currencyCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlCurrency, e);
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
	 * Genera las columnas para la tabla
	 */
	private void dynInitFirst(){
		llenarCombos();
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutF = new ColumnInfo[] {				
				/****0-Compañia Importadora****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_CompanyImport"), ".", KeyNamePair.class),				
				/****1-Orden de Compra****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderContract"), ".", String.class),
				/****2-Tipo de Orden de Compra****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Type"), ".", String.class),
				/****3-Categoria****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Category"), ".", KeyNamePair.class),
				/****4-Departamento****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Department"), ".", KeyNamePair.class), 
				/****5-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),
				/****6-Descripcion****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), ".", String.class),
				/****7-Tipo de Orden****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TypeOrder"), ".", String.class),
				/****8-País****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Country_ID"), ".", KeyNamePair.class),			
				/****9-Moneda****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_Currency_ID"), ".", String.class),
				/****10-Condición de Pago****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_PaymentTerm_ID"), ".", KeyNamePair.class),
				/****11-Saldo Extranjero****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountForeign"), ".", float.class),
				/****12-Saldo Nacional****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountLocal"), ".", float.class),
				/****13-Fecha de Pago Estimda****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DateEstimated"), ".", String.class),
				/****14-Semana de Pago estimada****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WeekEstimated"), ".", String.class),				
		};			
		xTable.prepareTable(layoutF, "", "", true, "");
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		CompiereColor.setBackground (this);
		xTable.getSelectionModel().addListSelectionListener(this);		
		xTable.getColumnModel().getColumn(0).setMinWidth(150);
		xTable.getColumnModel().getColumn(0).setCellRenderer(renderLeft);
		xTable.getColumnModel().getColumn(1).setMinWidth(80);	
		xTable.getColumnModel().getColumn(1).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(2).setMinWidth(210);	
		xTable.getColumnModel().getColumn(2).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(3).setMinWidth(80);	
		xTable.getColumnModel().getColumn(3).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(4).setMinWidth(90);
		xTable.getColumnModel().getColumn(4).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(5).setMinWidth(400);	
		xTable.getColumnModel().getColumn(5).setCellRenderer(renderLeft);
		xTable.getColumnModel().getColumn(6).setMinWidth(200);	
		xTable.getColumnModel().getColumn(6).setCellRenderer(renderLeft);
		xTable.getColumnModel().getColumn(7).setMinWidth(150);	
		xTable.getColumnModel().getColumn(7).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(8).setMinWidth(100);
		xTable.getColumnModel().getColumn(8).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(9).setMinWidth(80);	
		xTable.getColumnModel().getColumn(9).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(10).setMinWidth(200);	
		xTable.getColumnModel().getColumn(10).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(11).setMinWidth(160);	
		xTable.getColumnModel().getColumn(11).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(12).setMinWidth(155);
		xTable.getColumnModel().getColumn(12).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(13).setMinWidth(110);	
		xTable.getColumnModel().getColumn(13).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(14).setMinWidth(120);	
		xTable.getColumnModel().getColumn(14).setCellRenderer(renderRight);
		bSearch.addActionListener(this);
		bReset.addActionListener(this);
		bReport.addActionListener(this);
	}

	/**
	 * Carga la tabla del panel central norte
	 */
	private void tableInitF(){
        BigDecimal amountF = new BigDecimal(0);
        BigDecimal amountL = new BigDecimal(0);
        m_sql = new StringBuffer();
        String evaluar = "";
        
		m_sql.append("select cli.XX_Company, cli.name, ord.documentNo, ord.XX_POType, " +
				"cat.XX_VMR_Category_ID, cat.name, dep.XX_VMR_Department_ID, dep.name, " +
				"par.C_BPartner_ID, par.name, ord.XX_OrderType, cou.C_Country_ID, cou.name, " +
				"pay.C_PaymentTerm_ID, pay.name, cur.C_Currency_ID, cur.Iso_Code, round(eap.XX_AmountForeign,2) as XX_AmountForeign, " +
				"round(eap.XX_AmountLocal,2) as XX_AmountLocal, to_char(eap.XX_DateEstimated,'dd/mm/yyyy'), eap.XX_WeekEstimated, " +
				"adc.name, adc.AD_Client_ID, cot.XX_Contract_ID, cot.value, eap.Description, cot.XX_ContractType " +
				"from XX_VCN_EstimatedAPayable eap " +
				"left outer join C_Order ord on (eap.C_Order_ID = ord.C_Order_ID) " +
				"left outer join XX_VSI_Client cli on (cli.XX_VSI_Client_ID = ord.XX_ImportingCompany_ID) " +
				"left outer join XX_VMR_Category cat on (eap.XX_VMR_Category_ID = cat.XX_VMR_Category_ID) " +
				"left outer join XX_VMR_Department dep on (eap.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
				"inner join C_BPartner par on (eap.C_BPartner_ID = par.C_BPartner_ID) " +
				"left outer join C_Country cou on (eap.C_Country_ID = cou.C_Country_ID) " +
				"inner join C_PaymentTerm pay on (eap.C_PaymentTerm_ID = pay.C_PaymentTerm_ID) " +
				"inner join C_Currency cur on (eap.C_Currency_ID = cur.C_Currency_ID) " +
				"left outer join AD_Client adc on (eap.AD_Client_ID = adc.AD_Client_ID) " +
				"left outer join XX_Contract cot on (eap.XX_Contract_ID = cot.XX_Contract_ID) " +
				"where ");
		
		m_orderBy = "order by ord.XX_POType, cot.XX_Contract_ID, eap.XX_DateEstimated asc ";

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Buscar el proveedor seleccionado****/
		if ((partnerCombo.getSelectedIndex() != 0) && (partnerCombo.getSelectedItem() != null)){
			if (evaluar.equals("")){
				m_sql.append("par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
				evaluar = " ";
			}else
				m_sql.append("and ").append("par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
		}
		
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedIndex() != 0) && (categoryCombo.getSelectedItem() != null)){
			int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
			if (evaluar.equals("")){
				m_sql.append("cat.XX_VMR_Category_ID = ").append(categoria).append(" ");
				evaluar = " ";
			}else
				m_sql.append("and ").append("cat.XX_VMR_Category_ID = ").append(categoria).append(" "); 			
		}
		
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if ((typePartnerCombo.getSelectedIndex() != 0) && (typePartnerCombo.getSelectedItem() != null)){
			if (evaluar.equals("")){
				m_sql.append("ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).append("' ");
				evaluar = " ";
			}else
				m_sql.append("and ").append("ord.XX_OrderType = '").append(typePartnerCombo.getSelectedItem()).append("' ");
		}
		
		/****Buscar los proveedores segun el tipo de la orden****/
		if ((typeOrderCombo.getSelectedIndex() != 0) && (typeOrderCombo.getSelectedItem() != null)){
			poType = (String) typeOrderCombo.getSelectedItem();
			if (poType.equals("O/C Mercancía"))
				poTypeValue = "POM";
			else
				poTypeValue = "POA";
			if (evaluar.equals("")){
				m_sql.append("ord.XX_POType = '").append(poTypeValue).append("' ");
				evaluar = " ";
			}else
				m_sql.append("and ").append("ord.XX_POType = '").append(poTypeValue).append("' ");
		}
		
		/****Busca los proveedores segun la compañia importadora****/
		if ((companyImportCombo.getSelectedIndex() != 0) && (companyImportCombo.getSelectedItem() != null)){
			if (evaluar.equals("")){
				m_sql.append("cli.name = '").append(companyImportCombo.getSelectedItem()).append("' ");
				evaluar = " ";
			}else
				m_sql.append("and ").append("cli.name = '").append(companyImportCombo.getSelectedItem()).append("' ");
		}
		
		/****Busca los proveedores segun la moneda****/
		if ((currencyCombo.getSelectedIndex() != 0) && (currencyCombo.getSelectedItem() != null)){
			String currency = currencyCombo.getSelectedItem().toString();
			String result[] = currency.split(" - ");
			if (evaluar.equals("")){
				m_sql.append("cur.Iso_Code = '").append(result[0]).append("' ");
				evaluar = " ";
			}else
				m_sql.append("and ").append("cur.Iso_Code = '").append(result[0]).append("' ");
		}
		
		/****Hace la busqueda por la fecha****/
		Timestamp to = (Timestamp) date.getValue();
		if (to != null){
			if (evaluar.equals("")){
				m_sql.append("eap.XX_DateEstimated = ").append(DB.TO_DATE(to)).append(" ");
				evaluar = " ";
			}else
				m_sql.append("and ").append("eap.XX_DateEstimated = ").append(DB.TO_DATE(to)).append(" ");
		}
		
		String SQL = m_sql.toString() + m_orderBy.toString();
		int i = 0;
		Vector<String> pais = new Vector<String>(2);
		xTable.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		try {	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();	
			amountForeign.setText(formato.format(new BigDecimal(0))); //.add(rs.getBigDecimal("XX_AmountForeign")).toString()
			amountForeign.setFont(new Font("Serif", Font.BOLD,12));
			amountLocal.setText(formato.format(new BigDecimal(0))); //add(rs.getBigDecimal("XX_AmountLocal")).toString()
			amountLocal.setFont(new Font("Serif", Font.BOLD,12));
			while(rs.next()) {	
				xTable.setRowCount (i+1);		
				/****Compañia Importadora****/
				if ((rs.getString(2) == null) && (rs.getString("Iso_Code").equals("VEB"))){
					xTable.setValueAt(
							new KeyNamePair (rs.getInt("AD_Client_ID"), rs.getString(22)),i,0);
				}else{
					xTable.setValueAt(
							new KeyNamePair (rs.getInt("XX_Company"), rs.getString(2)),i,0);
				}
				/****Orden de Compra o Contrato****/
				if (rs.getString("documentNo") == null)
					xTable.setValueAt(rs.getString(25), i, 1);
				else
					xTable.setValueAt(rs.getString("documentNo"), i, 1);
				/****Tipo de O/C****/
				String poType = rs.getString("XX_POType");
				if (poType == null)
					xTable.setValueAt("Contrato", i, 2);
				else{
					if (poType.equals("POM"))
						poType = "O/C Mercancía";
					else if (poType.equals("POA"))
							poType = "O/C Bienes y Servicios";
						xTable.setValueAt(poType, i, 2);
				}
				/****Categoria****/
				int categoria = rs.getInt("XX_VMR_Category_ID");
				if (categoria != 0)
					xTable.setValueAt(
							new KeyNamePair (rs.getInt("XX_VMR_Category_ID"), rs.getString(6)), i, 3);
				else
					xTable.setValueAt("--------", i, 3);
				/****Departamento****/
				int departamento = rs.getInt("XX_VMR_Department_ID");
				if (departamento != 0)
					xTable.setValueAt(
							new KeyNamePair (rs.getInt("XX_VMR_Department_ID"), rs.getString(8)), i, 4);
				else
					xTable.setValueAt("--------", i, 4);
				/****Proveedor****/
				xTable.setValueAt(
					new KeyNamePair (rs.getInt("C_BPartner_ID"), "  " + rs.getString(10)), i, 5);
				/****Descripcion****/
				xTable.setValueAt("  " + rs.getString("Description"), i, 6);
				/****Tipo de Orden****/
				String tipoOrder = rs.getString("XX_OrderType");
				if (tipoOrder == null)
					xTable.setValueAt(rs.getString("XX_ContractType"), i, 7);
				else
					xTable.setValueAt(rs.getString("XX_OrderType"), i, 7);
				/****País****/
				if (rs.getInt("C_Country_ID") == 0) {
					pais = nombrePais(rs.getInt("C_Currency_ID"));
					xTable.setValueAt(
							new KeyNamePair (Integer.parseInt(pais.get(1)), pais.get(0)), i, 8);
				}else
					xTable.setValueAt(
						new KeyNamePair (rs.getInt("C_Country_ID"), rs.getString(13)), i, 8);
				/****Moneda****/
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("C_Currency_ID"), rs.getString("Iso_Code")), i, 9);
				/****Condición de Pago****/
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("C_PaymentTerm_ID"), rs.getString(15)), i, 10);
				/****Saldo Extranjero****/			
				xTable.setValueAt(formato.format(rs.getBigDecimal("XX_AmountForeign").setScale(2)), i, 11);
				amountF = amountF.add(rs.getBigDecimal("XX_AmountForeign")).setScale(2); 
				/****Saldo Nacional****/			
				xTable.setValueAt(formato.format(rs.getBigDecimal("XX_AmountLocal").setScale(2)), i, 12);
				amountL = amountL.add(rs.getBigDecimal("XX_AmountLocal")).setScale(2);
				/****Fecha Estimada****/
				xTable.setValueAt(rs.getString(20), i, 13);
				/****Semana Estimada****/
				xTable.setValueAt(rs.getString("XX_WeekEstimated"), i, 14);
				amountForeign.setText(formato.format(amountF)); //.add(rs.getBigDecimal("XX_AmountForeign")).toString()
				amountForeign.setFont(new Font("Serif", Font.BOLD,12));
				amountLocal.setText(formato.format(amountL)); //add(rs.getBigDecimal("XX_AmountLocal")).toString()
				amountLocal.setFont(new Font("Serif", Font.BOLD,12));
				i++;							
			}
				
		}
		catch(SQLException e){
			e.printStackTrace();
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
		
	}
	
	/**
	 * Busca el nombre del pais, dependiendo del tipo de moneda
	 * @param idCurrency identificador de la moneda
	 * @return
	 */
	public Vector<String> nombrePais(int idCurrency){
		String sql = "select name, C_Country_ID from C_Country where C_Currency_ID = " + idCurrency;
		Vector<String> pais = new Vector<String>(2);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			if (rs.next()) {
				pais.add(rs.getString(1));
				pais.add(String.valueOf(rs.getInt(2)));
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
		
		return pais;
	}
	
	/**
	 *  Ejecuta el metodo correspondiente a la accion
	 *  @param e La acccion del evento
	 */
	public void actionPerformed (ActionEvent e){			
		if (e.getSource() == bSearch){
			
			cmdSearch();
			
			if(xTable.getRowCount()>0)
				bReport.setEnabled(true);
			else
				bReport.setEnabled(false);
			
		}else if (e.getSource() == bReset){
			removeItemsToReset();			
		}
		else if (e.getSource() == bReport){
			exportExcel();			
		}
	}
	
	/**
	 *  Busca la informacion a través de los filtros 
	 */
	private void cmdSearch(){	
		tableInitF();
	}

	/**
	 *  Remueve los items de todos los campos filtros
	 */
	private void removeItemsToReset(){
		categoryCombo.removeAllItems();
		typePartnerCombo.removeAllItems();
		partnerCombo.removeAllItems();
		typeOrderCombo.removeAllItems();
		companyImportCombo.removeAllItems();
		llenarCombos();
		xTable.removeAll();		
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
		dynTypePartner();
		dynOrderType();
		dynCompanyImport();
		//dynCategory();
		dynCurrency();
		dynPartner();
	}

	/**
	 *
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {		

	}
	
	/**
	 * exporta la tabla resultado a excel
	 */
	private  void exportExcel(){

		KeyNamePair vendorID = new KeyNamePair(0, "");
		if(partnerCombo.getSelectedIndex()>0)
			vendorID = (KeyNamePair) partnerCombo.getSelectedItem();
		
		KeyNamePair impCompanyID = new KeyNamePair(0, "");
		if(companyImportCombo.getSelectedIndex()>0)
			impCompanyID = (KeyNamePair) companyImportCombo.getSelectedItem();
		
		KeyNamePair categoryID = new KeyNamePair(0, "");
		if(categoryCombo.getSelectedIndex()>0)
			categoryID = (KeyNamePair) categoryCombo.getSelectedItem();
		
		KeyNamePair currencyID = new KeyNamePair(0, "");
		if(currencyCombo.getSelectedIndex()>0)
			currencyID = (KeyNamePair) currencyCombo.getSelectedItem();
		
		String orderSource = "X";
		if(typePartnerCombo.getSelectedIndex()>0)
			orderSource = typePartnerCombo.getSelectedItem().toString();
		
		String typeOrder = "X";
		if(typeOrderCombo.getSelectedIndex()>0){
			typeOrder = typeOrderCombo.getSelectedItem().toString();
			if (typeOrder.equals("O/C Mercancía"))
				typeOrder = "POM";
			else
				typeOrder = "POA";
		}
		
		String estimatedDate = "X";
		if(date.getTimestamp()!=null)
			estimatedDate = date.getDisplay();
			
		String designName = "EstimatedAPayable";

		//Intanciar reporte
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametros
		myReport.parameterName.add("vendorID");
		myReport.parameterValue.add(vendorID.getKey());
		
		myReport.parameterName.add("impCompanyID");
		myReport.parameterValue.add(impCompanyID.getKey());
		
		myReport.parameterName.add("categoryID");
		myReport.parameterValue.add(categoryID.getKey());
		
		myReport.parameterName.add("currencyID");
		myReport.parameterValue.add(currencyID.getKey());
		
		myReport.parameterName.add("orderSource");
		myReport.parameterValue.add(orderSource);
		
		myReport.parameterName.add("orderType");
		myReport.parameterValue.add(typeOrder);
		
		myReport.parameterName.add("estimatedDate");
		myReport.parameterValue.add(estimatedDate);
		
		myReport.parameterName.add("client");
		myReport.parameterValue.add(Env.getCtx().getAD_Client_ID());
		
		//Correr Reporte
		myReport.runReport(designName,"xls");
	}
}
