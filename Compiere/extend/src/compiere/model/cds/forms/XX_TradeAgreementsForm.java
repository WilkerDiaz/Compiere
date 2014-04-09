package compiere.model.cds.forms;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
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
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.model.X_C_Invoice;
import org.compiere.plaf.CompiereColor;
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
import org.compiere.util.Trx;

import compiere.model.cds.MCreditNotifyReturn;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_NotificationType;
import compiere.model.cds.X_Ref_XX_REF_AgreementType;
import compiere.model.cds.X_Ref_XX_StatusNotificationCredit;
import compiere.model.cds.X_XX_VCN_TradeAgreements;
import compiere.model.importcost.X_XX_VLO_SUMMARY;

public class XX_TradeAgreementsForm extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_CreditNoteForm.class);
	// cliente 
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	 StringBuffer    m_sql = null;
	 StringBuffer    m_sql2 = null;
	 StringBuffer    m_sqlDetail = null;
	 String          m_groupBy = "";
	 String          m_groupBy2 = "";
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
	private JScrollPane xScrollPaneACC = new JScrollPane();
	private JScrollPane xScrollPaneAAP = new JScrollPane();
	private JScrollPane xScrollPaneDetails = new JScrollPane();
	private Trx trans = null;
	private MiniTable xTableACC = new MiniTable();
	private MiniTable xTableAAP = new MiniTable();
	private MiniTable xTableDetails = new MiniTable();
	String header1="<html><PRE><font size='3' face='Arial'><B>Total Compras Hechas: </B>";
	String header2="<B>Total Descuentos Generados: </B>";
	String header3="<B>% Compras Vs Descuentos: </B>";
	private CLabel totalPurchaseACC = new CLabel();
	private CLabel totalPurchaseAAP = new CLabel();

	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private TitledBorder xBorder =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_VCN_TRADEAGREEMENTS_ID"));
	private TitledBorder xBorderAAP =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_PubContribAndCommit"));
		private TitledBorder xBorderDetails =
			new TitledBorder(Msg.translate(Env.getCtx(), "XX_Details"));
	 Integer IDRegAct=0;
	//BUSINESS PARTNER Busqueda
	private CLabel vendorLabel = new CLabel();
	private VComboBox comboBPartner = new VComboBox();
	private VComboBox comboCNotifySearch = new VComboBox();

	//Fecha
	private CLabel dateLabel = new CLabel();
	//buttons
	private CButton bSearch = new CButton();
	private JButton bReset = new CButton();
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
	
	//Combo box y sus labels
	private CLabel yearLabel = new CLabel();
	private VNumber yearField = new VNumber();
	private CLabel monthLabel = new CLabel();
	private CComboBox monthCombo = new CComboBox();
	private CLabel categoryLabel = new CLabel();
	private CComboBox categoryCombo = new CComboBox();
	private CLabel departmentLabel = new CLabel();
	private CComboBox departmentCombo = new CComboBox();
	private CLabel agreementTypeLabel = new CLabel();
	private CComboBox agreementTypeCombo = new CComboBox();
	

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		trans = Trx.get("XX_ModifyDebtCredit");
		
		try
		{
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
		 * Valores de las etiquetas
		 * */
		monthLabel.setText(Msg.getMsg(Env.getCtx(), "Month"));
		yearLabel.setText(Msg.getMsg(Env.getCtx(), "Year"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Category"));
		agreementTypeLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TradeAgreementType"));
		departmentLabel.setText(Msg.translate(Env.getCtx(), "XX_Department_I"));
		vendorLabel.setText(Msg.translate(Env.getCtx(), "VendorCod"));
		
		//Ubicacion del boton de Busqueda (panel superior)
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		
		//bSearch.setPreferredSize(new Dimension(100,30));	
		bSearch.setEnabled(true);
		xPanel.setLayout(xLayout);
		
		dateLabel.setText(Msg.translate(Env.getCtx(), "Date"));
		bReset.addActionListener(this);
		
		//Ubicacion de botones Cancelar y Generar nota de credito (Panel Inferior)
		southPanel.setLayout(southLayout);
		
		centerPanel.setLayout(centerLayout);
		xScrollPaneACC.setBorder(xBorder);
		xScrollPaneACC.setPreferredSize(new Dimension(960, 220));
		xScrollPaneAAP.setBorder(xBorderAAP);
		xScrollPaneAAP.setPreferredSize(new Dimension(960, 220));
		xScrollPaneDetails.setBorder(xBorderDetails);
		xScrollPaneDetails.setPreferredSize(new Dimension(960, 220));
		
		
		xPanel.setLayout(xLayout);
		
		//Panel Superior search
		northPanel.add(bSearch,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		northPanel.add(bReset,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 5, 12), 0, 0));
		
		
		/*
		 * Combos y labels de la 1era línea
		 * */
		northPanel.add(monthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(monthCombo, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(yearLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(yearField, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(categoryLabel, new GridBagConstraints(4, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(5, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		/*
		 * Combos y labels de la 2da línea
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

		mainPanel.add(northPanel,  BorderLayout.NORTH);
		//mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
	
	    
		southPanel.add(xScrollPaneACC, new GridBagConstraints(0, 0, 1, 1, 8, 7,
				GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(12, 0, 5, 0), 5, 5));
		
		southPanel.add(totalPurchaseACC, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 250), 0, 0));

		
		southPanel.add(xScrollPaneAAP, new GridBagConstraints(0, 4, 1, 1, 8, 7,
				GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(12, 0, 5, 0), 5, 5));
		
		southPanel.add(totalPurchaseAAP, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 250), 0, 0));

		
		southPanel.add(xScrollPaneDetails, new GridBagConstraints(0, 8, 1, 1, 8, 7,
				GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(12, 0, 5, 0), 5, 5));

		
		
		xScrollPaneACC.getViewport().add(xTableACC, null);
		xScrollPaneAAP.getViewport().add(xTableAAP, null);
		xScrollPaneDetails.getViewport().add(xTableDetails, null);
		
		centerPanel.add(southPanel);
		
	
	
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
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
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
			rs.close();
			pstmt.close();

			departmentCombo.addActionListener(this);
			departmentCombo.setEnabled(true);
			departmentCombo.setEditable(true);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	
	private void dynInit()
	{	
		//Llenar los filtros de busquedas
		llenarcombos();
		// generacion de las columnas para los avisos de Creditos
		ColumnInfo[] layoutACC = new ColumnInfo[] {
		
				//0 Proveedor			
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", String.class),
				
				//1 País
				new ColumnInfo(Msg.translate(Env.getCtx(), "CountryName"),   ".", String.class), 
								
				//2 Departamento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),  ".", String.class),
				
				//3 Categoría
				new ColumnInfo(Msg.translate(Env.getCtx(), "Categoria"),  ".", String.class),
				
				//4 Mes
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Month"), ".", Integer.class),
				
				//5 Año				
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Year"),".", Integer.class),
				
				//6 Compras hechas
				new ColumnInfo(Msg.translate(Env.getCtx(), "PurchasesDone"), "total", float.class),
				
				//7 Descuentos generados
				new ColumnInfo(Msg.translate(Env.getCtx(), "GeneratedDiscount"), ".", float.class),
				
				//8 Avisos pendientes
				new ColumnInfo(Msg.translate(Env.getCtx(), "PendingAdvices"),".", float.class),
				
		};

		xTableACC.prepareTable(layoutACC, "", "", true, "");
		

		//  Visual
		CompiereColor.setBackground (this);
		xTableACC.getSelectionModel().addListSelectionListener(this);		
		xTableACC.getColumnModel().getColumn(0).setMinWidth(350);
		xTableACC.getColumnModel().getColumn(1).setMinWidth(150);
		xTableACC.getColumnModel().getColumn(2).setMinWidth(190);
		xTableACC.getColumnModel().getColumn(3).setMinWidth(110);
		xTableACC.getColumnModel().getColumn(4).setMinWidth(25);
		xTableACC.getColumnModel().getColumn(4).setMaxWidth(30);
		xTableACC.getColumnModel().getColumn(5).setMinWidth(30);
		xTableACC.getColumnModel().getColumn(5).setMaxWidth(35);
		xTableACC.getColumnModel().getColumn(6).setMinWidth(140);
		xTableACC.getColumnModel().getColumn(7).setMinWidth(150);
		xTableACC.getColumnModel().getColumn(8).setMinWidth(140);
		xTableACC.getSelectionModel().addListSelectionListener(this);		

		ColumnInfo[] layoutAAP = new ColumnInfo[] {
	
				//0 Proveedor			
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   "vendorID", KeyNamePair.class),
				
				//1 País
				new ColumnInfo(Msg.translate(Env.getCtx(), "CountryName"),   ".", KeyNamePair.class), 

				//2 Mes
				new ColumnInfo(Msg.translate(Env.getCtx(), "Month"), ".", Integer.class),
				
				//3 Año				
				new ColumnInfo(Msg.translate(Env.getCtx(), "Year"),".", Integer.class),
				
				//4 Tipo
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Notificationtype"),".", String.class),
				
				//5 Compras hechas
				new ColumnInfo(Msg.translate(Env.getCtx(), "PurchasesDone"), "total", float.class),
				
				//6 Descuentos generados
				new ColumnInfo(Msg.translate(Env.getCtx(), "GeneratedDiscount"), ".", float.class),
				
				//7 Avisos pendientes
				new ColumnInfo(Msg.translate(Env.getCtx(), "PendingAdvices"),".", float.class),
				
				
				
		};
		
		xTableAAP.prepareTable(layoutAAP, "", "", true, "");
		
		//  Visual
		CompiereColor.setBackground (this);
		xTableAAP.getSelectionModel().addListSelectionListener(this);		
		xTableAAP.getColumnModel().getColumn(0).setMinWidth(350);
		xTableAAP.getColumnModel().getColumn(1).setMinWidth(150);
		xTableAAP.getColumnModel().getColumn(2).setMinWidth(25);
		xTableAAP.getColumnModel().getColumn(2).setMaxWidth(30);
		xTableAAP.getColumnModel().getColumn(3).setMinWidth(30);
		xTableAAP.getColumnModel().getColumn(3).setMaxWidth(35);
		xTableAAP.getColumnModel().getColumn(4).setMinWidth(90);
		xTableAAP.getColumnModel().getColumn(5).setMinWidth(150);
		xTableAAP.getColumnModel().getColumn(6).setMinWidth(140);
		xTableAAP.getColumnModel().getColumn(7).setMinWidth(140);
		xTableAAP.getSelectionModel().addListSelectionListener(this);	
		
		ColumnInfo[] layoutDetails = new ColumnInfo[] {
				
				//0 Proveedor			
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", String.class),

				//1 Mes
				new ColumnInfo(Msg.translate(Env.getCtx(), "Month"), ".", Integer.class),
				
				//2 Año				
				new ColumnInfo(Msg.translate(Env.getCtx(), "Year"),".", Integer.class),
				
				//3 Departamento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),  ".", String.class),
				
				//4 Categoría
				new ColumnInfo(Msg.translate(Env.getCtx(), "Categoria"),  ".", String.class),
				
				//5 Descuento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PubAmountDisc"), ".", float.class)
				
		};
		
		xTableDetails.prepareTable(layoutDetails, "", "", true, "");
		
		//  Visual
		CompiereColor.setBackground (this);
		xTableDetails.getSelectionModel().addListSelectionListener(this);		
		xTableDetails.getColumnModel().getColumn(0).setMinWidth(350);
		xTableDetails.getColumnModel().getColumn(1).setMinWidth(25);
		xTableDetails.getColumnModel().getColumn(1).setMaxWidth(30);
		xTableDetails.getColumnModel().getColumn(2).setMinWidth(30);
		xTableDetails.getColumnModel().getColumn(2).setMaxWidth(35);
		xTableDetails.getColumnModel().getColumn(3).setMinWidth(190);
		xTableDetails.getColumnModel().getColumn(4).setMinWidth(150);
		xTableDetails.getColumnModel().getColumn(5).setMinWidth(120);
		xTableDetails.getSelectionModel().addListSelectionListener(this);	
		
		
		bSearch.addActionListener(this);
		bReset.addActionListener(this);

		
		//Se cargan los acuerdos
		tableInit_option=0;
		tableInit();
		tableDetailsInit();
		totalCalculation();
		
		xTableACC.getTableHeader().setReorderingAllowed(false);
		xTableAAP.getTableHeader().setReorderingAllowed(false);
		xTableDetails.getTableHeader().setReorderingAllowed(false);
		//xTable.setAutoResizeMode(3);
		//data();
	
		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		//estatus 
		statusBar.setStatusLine("Acuerdos Comerciales y Cartas Compromiso");
		statusBar.setStatusDB(xTableACC.getRowCount()+xTableAAP.getRowCount());
		
	}  //  dynInit  
	
	
	private  void tableInit ()
	{

		m_sql = new StringBuffer ();
		m_sql2 = new StringBuffer();

		//Mostrar todos los ACC y AAP
		if(tableInit_option==0)
		{	
			m_sql.append("select cb.value ||'-'|| cb.name vendor, co.name country, extract(month from cn.CREATED) month," +
					" extract (year from cn.CREATED) year, NVL(round(sum(cn.XX_UNITPURCHASEPRICEBS),2),0) total," +
					" dep.value || '-' || dep.name department, cat.value || '-' || cat.name category," +
					" o.XX_VMR_DEPARTMENT_ID, o.XX_VMR_CATEGORY_ID, o.C_BPARTNER_ID vendorID" +
					" from  XX_CREDITNOTIFYRETURN cn, C_BPARTNER cb, C_COUNTRY co, C_ORDER o, XX_VMR_DEPARTMENT dep, XX_VMR_CATEGORY cat" +
					" where o.C_BPARTNER_ID=cb.C_BPARTNER_ID and co.C_COUNTRY_ID=o.C_COUNTRY_ID and" +
					" cn.XX_NOTIFICATIONTYPE='ACC'and cn.C_ORDER_ID=o.C_ORDER_ID and" +
					" dep.XX_VMR_DEPARTMENT_ID=o.XX_VMR_DEPARTMENT_ID and o.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID and o.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
					" AND o.ISSOTRX = 'N'");
 
			m_groupBy =" group by cb.value ||'-'|| cb.name, co.name,  extract(month from cn.CREATED), extract (year from cn.CREATED), dep.value || '-' || dep.name," +
					" cat.value || '-' || cat.name,o.XX_VMR_DEPARTMENT_ID, o.XX_VMR_CATEGORY_ID, o.C_BPARTNER_ID";	
			
			m_sql2.append("select cb.value ||'-'|| cb.name vendor, co.name country, det.XX_MONTH month," +
					" det.XX_YEAR year, round(cn.XX_UNITPURCHASEPRICEBS,2) total, o.C_BPARTNER_ID vendorID," +
					" o.XX_VMR_DEPARTMENT_ID, o.XX_VMR_CATEGORY_ID, cn.XX_CREDITNOTIFYRETURN_ID creditNotID, cn.XX_NOTIFICATIONTYPE tipo" +
					" from  XX_CREDITNOTIFYRETURN cn, C_BPARTNER cb, C_COUNTRY co, C_ORDER o, XX_VMR_DEPARTMENT dep, XX_VMR_CATEGORY cat, XX_VCN_DETAILADVICE det" +
					" where o.C_BPARTNER_ID=cb.C_BPARTNER_ID and co.C_COUNTRY_ID=o.C_COUNTRY_ID and cn.XX_NOTIFICATIONTYPE<>'ACC'" +
					" and cn.C_ORDER_ID=o.C_ORDER_ID and dep.XX_VMR_DEPARTMENT_ID=o.XX_VMR_DEPARTMENT_ID" +
					" and o.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID and det.XX_CREDITNOTIFYRETURN_ID=cn.XX_CREDITNOTIFYRETURN_ID and o.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
					" AND o.ISSOTRX = 'N'");
			
			m_groupBy2=" group by cb.value ||'-'|| cb.name, co.name,  det.XX_MONTH, det.XX_YEAR, cn.XX_UNITPURCHASEPRICEBS," +
					" o.XX_VMR_DEPARTMENT_ID, o.XX_VMR_CATEGORY_ID, o.C_BPARTNER_ID, cn.XX_CREDITNOTIFYRETURN_ID, cn.XX_NOTIFICATIONTYPE";
			//Búsqueda por mes
			if(monthCombo.getSelectedIndex()!=0 && monthCombo.getSelectedItem() != null){
				m_sql.append(" AND ").append("extract(month from cn.created)=").append(monthCombo.getSelectedIndex());
				m_sql2.append(" AND ").append("det.XX_MONTH=").append(monthCombo.getSelectedIndex());
			}
			//Búsqueda por año
			if(yearField.getValue()!=null){
				m_sql.append(" AND ").append("extract(year from cn.created)=").append(((BigDecimal) yearField.getValue()).intValue());
				m_sql2.append(" AND ").append("det.XX_YEAR=").append(((BigDecimal) yearField.getValue()).intValue());
			}
			//Búsqueda por proveedor
			if(comboBPartner.getSelectedIndex()!=0 && comboBPartner.getSelectedItem() != null){
				if(((KeyNamePair)comboBPartner.getSelectedItem()).getKey() != 0){
					int clave_vendor=((KeyNamePair)comboBPartner.getSelectedItem()).getKey();
					m_sql.append(" AND ").append("o.C_BPartner_ID=").append(clave_vendor);
					m_sql2.append(" AND ").append("o.C_BPartner_ID=").append(clave_vendor);
				}
			}	

			
			//agrego la categoria
			if (categoryCombo.getValue() != null) {
				KeyNamePair cat = (KeyNamePair) categoryCombo.getValue();
				if (cat.getKey() != -1){
					m_sql.append(" AND o.XX_VMR_CATEGORY_ID = ").append(cat.getKey()).append(" ");
					m_sql2.append(" AND o.XX_VMR_CATEGORY_ID = ").append(cat.getKey()).append(" ");
				}
			}
			//agrego el departamento al query
			if (departmentCombo.getValue() != null) {
				KeyNamePair dep = (KeyNamePair) departmentCombo.getValue();
				if (dep.getKey() != -1){
					m_sql.append(" AND o.XX_VMR_DEPARTMENT_ID = ").append(dep.getKey()).append(" ");
					m_sql2.append(" AND o.XX_VMR_DEPARTMENT_ID = ").append(dep.getKey()).append(" ");
				}
			}
			
			
		}
		
		String SQL = m_sql.toString()+ m_groupBy;
					
    	int i = 0;
    	xTableACC.setRowCount(i);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try 
		{	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTableACC.setRowCount (i+1);
				//Proveedor
				xTableACC.setValueAt(rs.getString("vendor"),i,0);//1
				//País
				xTableACC.setValueAt(rs.getString("country"), i, 1);
				//Departamento
				xTableACC.setValueAt(rs.getString("department"), i, 2);
				//Categoría
				xTableACC.setValueAt(rs.getString("category"), i, 3);
				//Mes
				xTableACC.setValueAt(rs.getInt("month"), i,4);
				//Año
				xTableACC.setValueAt(rs.getInt("year"), i,5);
				//Compras hechas
				xTableACC.setValueAt(rs.getBigDecimal("total"), i, 6);
				
				//Se calculan los avisos cerrados(CER) de el mismo mes, año, proveedor, depto y categoría
				String SQL2 = "select nvl(round(sum(cn.XX_AMOUNT),2),0) totalDesc" +
						" from  XX_CREDITNOTIFYRETURN cn, C_BPARTNER cb, C_COUNTRY co, C_ORDER o, XX_VMR_DEPARTMENT dep, XX_VMR_CATEGORY cat" +
						" where o.C_BPARTNER_ID=cb.C_BPARTNER_ID and co.C_COUNTRY_ID=o.C_COUNTRY_ID" +
						" and cn.XX_NOTIFICATIONTYPE='ACC' and cn.C_ORDER_ID=o.C_ORDER_ID" +
						" and dep.XX_VMR_DEPARTMENT_ID=o.XX_VMR_DEPARTMENT_ID and o.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID" +
						" and XX_STATUS='CER' and extract(month from cn.CREATED)=" + rs.getInt("month")+
						" and extract (year from cn.CREATED)="+rs.getInt("year")+
						" and o.C_BPARTNER_ID="+rs.getInt("vendorID")+" and o.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
						" AND o.ISSOTRX = 'N'";
				PreparedStatement pstmt2 = null;
				ResultSet rs2 = null;
				
				try 
				{	
					pstmt2 = DB.prepareStatement(SQL2, null);
					rs2 = pstmt2.executeQuery();
					if(rs2.next())
						xTableACC.setValueAt(rs2.getBigDecimal("totalDesc"), i, 7);					
					else
						xTableACC.setValueAt(0, i, 7);
				}
				catch(SQLException e)
				{	
					System.out.print(e.getMessage());
				}
				finally{
					DB.closeResultSet(rs2);
					DB.closeStatement(pstmt2);
				}
				
				//Se calculan los avisos pendientes(ACT) de el mismo mes, año, proveedor, depto y categoría
				String SQL3 = "select nvl(round(sum(cn.XX_AMOUNT),2),0) totalDesc" +
						" from  XX_CREDITNOTIFYRETURN cn, C_BPARTNER cb, C_COUNTRY co, C_ORDER o, XX_VMR_DEPARTMENT dep, XX_VMR_CATEGORY cat" +
						" where o.C_BPARTNER_ID=cb.C_BPARTNER_ID and co.C_COUNTRY_ID=o.C_COUNTRY_ID" +
						" and cn.XX_NOTIFICATIONTYPE='ACC' and cn.C_ORDER_ID=o.C_ORDER_ID" +
						" and dep.XX_VMR_DEPARTMENT_ID=o.XX_VMR_DEPARTMENT_ID and o.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID" +
						" and XX_STATUS='ACT' and extract(month from cn.CREATED)=" + rs.getInt("month")+
						" and extract (year from cn.CREATED)="+rs.getInt("year")+
						" and o.C_BPARTNER_ID="+rs.getInt("vendorID")+" and o.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
						" AND o.ISSOTRX = 'N'";
				PreparedStatement pstmt3 = null;
				ResultSet rs3 = null;
				try 
				{	
					pstmt3 = DB.prepareStatement(SQL3, null);
					rs3 = pstmt3.executeQuery();
					if(rs3.next())
						xTableACC.setValueAt(rs3.getBigDecimal("totalDesc"), i, 8);					
					else
						xTableACC.setValueAt(0, i, 8);
				}
				catch(SQLException e)
				{	
					System.out.print(e.getMessage());
				}	
				finally{
					DB.closeResultSet(rs3);
					DB.closeStatement(pstmt3);
				}

				i++;				
			}
	
		}
		catch(SQLException e)
		{	
			e.getMessage();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		String sqlAAP = m_sql2.toString() + m_groupBy2;
		
		i = 0;
    	xTableAAP.setRowCount(i);
    	pstmt = null;
    	rs = null;
    	try 
		{	
			pstmt = DB.prepareStatement(sqlAAP, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTableAAP.setRowCount (i+1);
				//Proveedor
				xTableAAP.setValueAt(new KeyNamePair (rs.getInt("vendorID"), rs.getString("vendor")), i, 0);//1
				//País
				xTableAAP.setValueAt(new KeyNamePair (rs.getInt("creditNotID"), rs.getString("country")), i, 1);
				//Mes
				xTableAAP.setValueAt(rs.getInt("month"), i, 2);
				//Año
				xTableAAP.setValueAt(rs.getInt("year"), i, 3);
				//Tipo
				xTableAAP.setValueAt(rs.getString("tipo"), i,4);
				//Compras hechas
				xTableAAP.setValueAt(rs.getBigDecimal("total"), i,5);
				
				
				//Se calculan los avisos cerrados(CER) de el mismo mes, año, proveedor, depto y categoría
				String SQL2 = "select nvl(round(sum(cn.XX_AMOUNT),2),0) totalDesc" +
						" from  XX_CREDITNOTIFYRETURN cn, C_BPARTNER cb, C_COUNTRY co, C_ORDER o, XX_VMR_DEPARTMENT dep, XX_VMR_CATEGORY cat" +
						" where o.C_BPARTNER_ID=cb.C_BPARTNER_ID and co.C_COUNTRY_ID=o.C_COUNTRY_ID" +
						" and cn.XX_NOTIFICATIONTYPE<>'ACC' and cn.C_ORDER_ID=o.C_ORDER_ID" +
						" and dep.XX_VMR_DEPARTMENT_ID=o.XX_VMR_DEPARTMENT_ID and o.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID" +
						" and XX_STATUS='CER' and cn.XX_CREDITNOTIFYRETURN_ID="+rs.getInt("creditNotID") + 
						" and o.C_BPARTNER_ID="+rs.getInt("vendorID")+" and o.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
						" AND o.ISSOTRX = 'N'";
				PreparedStatement pstmt2 = null;
				ResultSet rs2 = null;
				
				try 
				{	
					pstmt2 = DB.prepareStatement(SQL2, null);
					rs2 = pstmt2.executeQuery();
					if(rs2.next())
						xTableAAP.setValueAt(rs2.getBigDecimal("totalDesc"), i, 6);					
					else
						xTableAAP.setValueAt(0, i, 6);
				}
				catch(SQLException e)
				{	
					System.out.print(e.getMessage());
				}
				finally{
					DB.closeResultSet(rs2);
					DB.closeStatement(pstmt2);
				}
				
				//Se calculan los avisos pendientes(ACT) de el mismo mes, año, proveedor, depto y categoría
				String SQL3 = "select nvl(round(sum(cn.XX_AMOUNT),2),0) totalDesc" +
						" from  XX_CREDITNOTIFYRETURN cn, C_BPARTNER cb, C_COUNTRY co, C_ORDER o, XX_VMR_DEPARTMENT dep, XX_VMR_CATEGORY cat" +
						" where o.C_BPARTNER_ID=cb.C_BPARTNER_ID and co.C_COUNTRY_ID=o.C_COUNTRY_ID" +
						" and cn.XX_NOTIFICATIONTYPE<>'ACC' and cn.C_ORDER_ID=o.C_ORDER_ID" +
						" and dep.XX_VMR_DEPARTMENT_ID=o.XX_VMR_DEPARTMENT_ID and o.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID" +
						" and XX_STATUS='ACT' and cn.XX_CREDITNOTIFYRETURN_ID="+rs.getInt("creditNotID")+
						" and o.C_BPARTNER_ID="+rs.getInt("vendorID")+" and o.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
						" AND o.ISSOTRX = 'N'";
				PreparedStatement pstmt3 = null;
				ResultSet rs3 = null;
				try 
				{	
					pstmt3 = DB.prepareStatement(SQL3, null);
					rs3 = pstmt3.executeQuery();
					if(rs3.next())
						xTableAAP.setValueAt(rs3.getBigDecimal("totalDesc"), i, 7);					
					else
						xTableAAP.setValueAt(0, i, 7);
				}
				catch(SQLException e)
				{	
					System.out.print(e.getMessage());
				}	
				finally{
					DB.closeResultSet(rs3);
					DB.closeStatement(pstmt3);
				}

				i++;				
			}	
	
		}
		catch(SQLException e)
		{	
			e.getMessage();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		
	}   //  tableInit
	
private void tableDetailsInit(){
		
		
		xTableAAP.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = 0;
				if (xTableAAP.getSelectedRow()==-1){
					xTableDetails.setRowCount(i);
					return;
				}
				if(e.getValueIsAdjusting()){ //Para que se ejecute solo una vez el ListSelectionListener
				KeyNamePair vendorKey = (KeyNamePair) xTableAAP.getValueAt(xTableAAP.getSelectedRow(), 0);
				KeyNamePair creditNotKey = (KeyNamePair) xTableAAP.getValueAt(xTableAAP.getSelectedRow(), 1);
				
				xTableDetails.setRowCount(i);
		    	String SQL ="select det.XX_MONTH month," +
		    			" det.XX_YEAR year, dep.value || '-' || dep.name department," +
		    			" cat.value || '-' || cat.name category, round(det.XX_PUBAMOUNT,2) amount" +
		    			" from XX_VCN_DETAILADVICE det, XX_CREDITNOTIFYRETURN cNot, XX_VMR_DEPARTMENT dep," +
		    			" XX_VMR_CATEGORY cat, C_ORDER cOrd" +
		    			" where cNot.XX_CREDITNOTIFYRETURN_ID=det.XX_CREDITNOTIFYRETURN_ID and" +
		    			" det.C_ORDER_ID=cOrd.C_ORDER_ID and cOrd.XX_VMR_DEPARTMENT_ID=dep.XX_VMR_DEPARTMENT_ID and" +
		    			" cOrd.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID and cOrd.C_BPARTNER_ID="+vendorKey.getKey()+
		    			" and det.XX_CREDITNOTIFYRETURN_ID="+creditNotKey.getKey()+" and cOrd.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+
		    			" AND cOrd.ISSOTRX = 'N'";
		    	
		    	PreparedStatement pstmt = null;
		    	ResultSet rs = null;
		    	try
		    	{	
					pstmt = DB.prepareStatement(SQL, null);
					rs = pstmt.executeQuery();			
					while(rs.next()) {	
						xTableDetails.setRowCount (i+1);
						//Proveedor
						xTableDetails.setValueAt(xTableAAP.getValueAt(xTableAAP.getSelectedRow(), 0),i,0);
						//Mes
						xTableDetails.setValueAt(rs.getInt("month"), i, 1);
						//Año
						xTableDetails.setValueAt(rs.getInt("year"), i, 2);
						//Departamento
						xTableDetails.setValueAt(rs.getString("department"), i, 3);
						//Categoría
						xTableDetails.setValueAt(rs.getString("category"), i,4);
						//Monto del descuento
						xTableDetails.setValueAt(rs.getString("amount"), i,5);
		
						i++;				
					}
				}
				catch(SQLException er)
				{	
					er.getMessage();
				}	
				finally{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
			}
		}
		});
    	
	}
	public void totalCalculation(){
		BigDecimal totalPurchaseACC = BigDecimal.ZERO;
		BigDecimal totalPurchaseAAP = BigDecimal.ZERO;
		BigDecimal totalDiscACC = BigDecimal.ZERO;
		BigDecimal totalDiscAAP = BigDecimal.ZERO;
		BigDecimal vsACC = BigDecimal.ZERO;
		BigDecimal vsAAP = BigDecimal.ZERO;
		
		for (int i = 0; i < xTableACC.getRowCount(); i++) {
			totalPurchaseACC = totalPurchaseACC.add((BigDecimal) xTableACC.getValueAt(i, 6));
			totalDiscACC = totalDiscACC.add((BigDecimal) xTableACC.getValueAt(i, 7));
		}
		
		for (int i = 0; i < xTableAAP.getRowCount(); i++) {
			totalPurchaseAAP = totalPurchaseAAP.add((BigDecimal)
					xTableAAP.getValueAt(i, 5));
			totalDiscAAP = totalDiscAAP.add((BigDecimal)
					xTableAAP.getValueAt(i, 6));
		}
		if(totalPurchaseACC!=BigDecimal.ZERO)
			vsACC = totalDiscACC.multiply(new BigDecimal(100)).divide(
					totalPurchaseACC, 2, RoundingMode.HALF_UP);
		if(totalPurchaseAAP!=BigDecimal.ZERO)
			vsAAP= totalDiscAAP.multiply(new BigDecimal(100)).divide(
					totalPurchaseAAP, 2, RoundingMode.HALF_UP);
			this.totalPurchaseACC.setText(header1+ totalPurchaseACC+ "   " +header2 +totalDiscACC + "   " +header3+"   " +vsACC  +"</PRE></font>"+"</html>");

		this.totalPurchaseAAP.setText(header1+ totalPurchaseAAP+ "   " +header2+ "   " +totalDiscAAP + "   " +header3+"   " + vsAAP +"</PRE></font>"+"</html>");

	}
	
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
			}
		}
				
	}
		
	
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{		
			tableInit();
			totalCalculation();
			if(xTableACC.getRowCount()!=0 || xTableAAP.getRowCount()!=0){
				statusBar.setStatusLine("Acuerdos Comerciales y Cartas Compromiso");
				statusBar.setStatusDB(xTableACC.getRowCount()+xTableAAP.getRowCount());
			}else{
				showCreditNotificationCondition();
			}
		}

	/**
	 *  Initial table state
	 */
	private void loadBasicInfo(){
		
		comboCNotifySearch.removeActionListener(this);
		comboBPartner.removeActionListener(this);
		monthCombo.removeAllItems();
		yearField.setDisplayType(0);
		comboBPartner.setEnabled(true);

		comboBPartner.removeAllItems();
		categoryCombo.removeAllItems();
		departmentCombo.removeAllItems();
		agreementTypeCombo.removeAllItems();

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
	
		//Cargar el combo box de mes
		monthCombo.addItem(null);
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_January"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_February"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_March"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_April"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_May"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_June"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_July"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_August"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_September"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_October"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_November"));
		monthCombo.addItem(Msg.translate(Env.getCtx(), "XX_December"));

		// Cargar proveedores
		String sql = "SELECT b.C_BPARTNER_ID, b.NAME FROM C_BPARTNER b WHERE isVendor='Y' ";
		sql = MRole.getDefault().addAccessSQL(sql, "b", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sql += " ORDER BY b.NAME";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			comboBPartner.addItem(loadKNP);
			while (rs.next()){
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
				//comboBPartner.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			comboBPartner.setEditable(false);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		

	}//fin de llenar combos



	
	 
	 /**
	  *  Shows the searched order condition
	  */
	 private void showCreditNotificationCondition()
	 {
	
	 	ADialog.warn(m_WindowNo, this.mainPanel, Msg.translate(Env.getCtx(), "XX_NoNotificationsForFilter"));	
	 }
	 
	 
}



