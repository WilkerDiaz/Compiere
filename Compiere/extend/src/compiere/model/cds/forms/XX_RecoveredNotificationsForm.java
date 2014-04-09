package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JOptionPane;
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
import org.compiere.grid.ed.VComboBox;
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
import compiere.model.cds.X_Ref_XX_NotificationType;

public class XX_RecoveredNotificationsForm extends CPanel
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
	private MiniTable xTable = new MiniTable();
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

	private  Vector<String> notType = new Vector<String>();
	//Fecha
	private VDate calendar = new VDate();
	//buttons
	private CButton bSearch = new CButton();
	private CButton bReset = new CButton();
	private CButton bExport = new CButton();
	
	private  int tableInit_option;

	// keyname
	KeyNamePair loadKNP = new KeyNamePair(0, "");
	 //variable para e id de la columna id de devolucion seleccionda
	KeyNamePair CNotifyKey=null;
	 int CNotifyID=0, vendor=0;
	
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
	private CLabel notificationTypeLabel = new CLabel();
	private CComboBox notificationTypeCombo = new CComboBox();
	private CLabel recupNacCheque = new CLabel(); //Recuperado Proveedores Nacionales que contengan N° de cheque
	private CLabel recupIntTransf = new CLabel(); //Recuperado. Proveedores Internacionales que contengan N° de transferencia
	private CLabel noRecupNacCheque = new CLabel(); //No Recuperado Proveedores Nacionales que contengan N° de cheque
	private CLabel noRecupIntTransf = new CLabel(); //No Recuperado Proveedores Internacionales que contengan N° de transferencia

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		
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
		 * Pongo valores a las etiquetas
		 * */
		dateToLabel.setText(Msg.getMsg(Env.getCtx(), "DateFrom"));
		dateFromlabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Category"));
		departmentLabel.setText(Msg.translate(Env.getCtx(), "XX_Department_I"));
		notificationTypeLabel.setText(Msg.getMsg(Env.getCtx(), "XX_NotificationType"));

		
		//Ubicacion del boton de Busqueda (panel superior)
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		bExport.setText(Msg.translate(Env.getCtx(), "Export"));
		bExport.setPreferredSize(new Dimension(90, 24));
		
		bSearch.setEnabled(true);
		xPanel.setLayout(xLayout);
		vendorLabel.setText(Msg.translate(Env.getCtx(), "VendorCod"));
		bReset.addActionListener(this);
		comboBPartner.addActionListener(this);
		notificationTypeCombo.addActionListener(this);
		
		southPanel.setLayout(southLayout);
		
		centerPanel.setLayout(centerLayout);
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(960, 220));
		xScrollPaneDetails.setBorder(xBorderDetails);
		xScrollPaneDetails.setPreferredSize(new Dimension(960, 220));
		
		xPanel.setLayout(xLayout);
		
		northPanel.add(bSearch, new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,new Insets(12, 30, 5, 0), 0, 0));
		northPanel.add(bReset, new GridBagConstraints(9, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 20, 5, 20), 0, 0));
		northPanel.add(bExport,   new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 20, 5, 20), 0, 0));
		
		
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
		
		northPanel.add(notificationTypeLabel, new GridBagConstraints(4, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(notificationTypeCombo, new GridBagConstraints(5, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		centerPanel.add(xScrollPaneDetails, BorderLayout.SOUTH);
		xScrollPane.getViewport().add(xTable, null);
		xScrollPaneDetails.getViewport().add(xTableDetails, null);
		
		southPanel.add(recupNacCheque, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 250), 0, 0));
		southPanel.add(recupIntTransf, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 250), 0, 0));
		southPanel.add(noRecupNacCheque, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 250), 0, 0));
		southPanel.add(noRecupIntTransf, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 250), 0, 0));
		recupNacCheque.setText("Total Recuperado Proveedores Nacionales");
		recupIntTransf.setText("Total Recuperado Proveedores Internacionales");
		noRecupNacCheque.setText("Total por Recuperar Proveedores Nacionales");
		noRecupIntTransf.setText("Total por Recuperar Proveedores Internacionales");

	}   //  jbInit
	//Datos 
	void dynCategory() {

		categoryCombo.removeActionListener(this);
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_CATEGORY ";
		sql += " ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			categoryCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				categoryCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			rs.close();
			pstmt.close();
			categoryCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
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
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

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
	}
	
	
	private void dynInit()
	{	
		//Llenar los filtros de busquedas
		llenarcombos();
		// generacion de las columnas para los avisos de Creditos
		ColumnInfo[] layout = new ColumnInfo[] {
				
				//0 Numero de aviso
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_CreditNotifyReturn_ID"),   ".", String.class), //1 
				
				//1 Proveedor			
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", String.class),
				
				//2 País
				new ColumnInfo(Msg.translate(Env.getCtx(), "CountryName"),   ".", KeyNamePair.class), 
								
				//3 Concepto
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Concept"),  ".", String.class),
				
				//4 Mes/Año
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_MONTHYEAR"), ".", String.class),
			
				//5 Tipo de Aviso
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Notificationtype"),  ".", String.class),//15
				
				//6 Monto del Descuento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Amount"), ".", BigDecimal.class),
					
				//7 Monto iva
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Amount_IVA"),".", BigDecimal.class),
				
				//8 Monto Total
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Total"), "Total", BigDecimal.class),

				//9 Monto Total en USD
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AMOUNTTOTALDOL"), ".", BigDecimal.class),

				//10 Fecha de creación del aviso 
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Created"), ".", String.class),
				
				//11 Fecha de creación del aviso 
				new ColumnInfo(Msg.translate(Env.getCtx(), "DateDoc"), ".", String.class),
				
				//12 Fecha del descuento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ChargeDate"), ".", String.class),
				
				
		};
		ColumnInfo[] layoutDetails = new ColumnInfo[] {
				//0 Proveedor			
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", String.class),
				
				//1 Departamento
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),  "XX_VMR_Department_ID", String.class),
				
				//2 Categoría
				new ColumnInfo(Msg.translate(Env.getCtx(), "Categoria"),  ".", String.class),
				
				//3 Descuento por Reconocimiento de Merma
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RecogDisc"),  ".", float.class),
				
				//4 Descuento por Servicio Post Venta				
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AfterSaleDisc"), ".", float.class),
				
				//5 Descuento por Centraliación de Entregas				
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_CentDelDisc"),".", float.class),
				
				//6 Descuento por Aporte a Publicidad
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PubAmountDisc"), ".", float.class)

		};
		xTable.prepareTable(layout, "", "", true, "");
		xTableDetails.prepareTable(layoutDetails, "", "", true, "");

		//  Visual
		CompiereColor.setBackground (this);
		xTable.getSelectionModel().addListSelectionListener(this);		
		xTable.getColumnModel().getColumn(0).setMinWidth(90);
		xTable.getColumnModel().getColumn(1).setMinWidth(220);
		xTable.getColumnModel().getColumn(2).setMinWidth(110);
		xTable.getColumnModel().getColumn(3).setMinWidth(160);
		xTable.getColumnModel().getColumn(4).setMinWidth(70);
		xTable.getColumnModel().getColumn(5).setMinWidth(110);
		xTable.getColumnModel().getColumn(6).setMinWidth(90);
		xTable.getColumnModel().getColumn(7).setMinWidth(80);
		xTable.getColumnModel().getColumn(8).setMinWidth(90);
		xTable.getColumnModel().getColumn(9).setMinWidth(95);
		xTable.getColumnModel().getColumn(10).setMinWidth(90);
		xTable.getColumnModel().getColumn(11).setMinWidth(60);
		xTable.getColumnModel().getColumn(12).setMinWidth(70);
		xTable.getSelectionModel().addListSelectionListener(this);
		
		xTableDetails.getColumnModel().getColumn(0).setMinWidth(30);
		xTableDetails.getColumnModel().getColumn(1).setMinWidth(40);
		xTableDetails.getColumnModel().getColumn(2).setMinWidth(140);
		xTableDetails.getColumnModel().getColumn(3).setMinWidth(130);
		xTableDetails.getColumnModel().getColumn(4).setMinWidth(110);
		xTableDetails.getColumnModel().getColumn(5).setMinWidth(110);
		xTableDetails.getColumnModel().getColumn(6).setMinWidth(90);

		bSearch.addActionListener(this);
		bReset.addActionListener(this);
		bExport.addActionListener(this);
		//Se cargan todos los avisos
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
		
		bExport.setEnabled(true);
		
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
				creditID = (KeyNamePair) xTable.getModel().getValueAt(xTable.getSelectedRow(),2);				
				xTableDetails.setRowCount(i);
		    	String SQL ="SELECT dep.VALUE||'-'||dep.NAME Departamento,cat.value ||'-'||cat.name Categoria, det.XX_DISCRECOGDECLAMOUNT," +
		    			" det.XX_DISCAFTERSALEAMOUNT, det.XX_CENTRADISCDELIAMOUNT, det.XX_PUBAMOUNT" +
		    			" FROM XX_VCN_DETAILADVICE det, XX_VMR_DEPARTMENT dep, XX_VMR_CATEGORY cat" +
		    			" WHERE det.XX_VMR_DEPARTMENT_ID = dep.XX_VMR_DEPARTMENT_ID (+) AND cat.XX_VMR_CATEGORY_ID (+) = dep.XX_VMR_CATEGORY_ID" +
		    			" AND det.AD_CLIENT_ID="+ ctx.getAD_Client_ID()+" AND det.XX_CREDITNOTIFYRETURN_ID="+ creditID.getKey();
		    	try
		    	{	
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();			
					while(rs.next()) {	
						xTableDetails.setRowCount (i+1);
						//Proveedor
						xTableDetails.setValueAt(xTable.getValueAt(xTable.getSelectedRow(), 1),i,0);
						//Departamento
						xTableDetails.setValueAt(rs.getString("Departamento"), i, 1);
						//Categoría
						xTableDetails.setValueAt(rs.getString("Categoria"), i,2);
						//Descuento por merma
						xTableDetails.setValueAt(rs.getFloat("XX_DISCRECOGDECLAMOUNT"), i, 3);
						//Descuento Post Venta
						xTableDetails.setValueAt(rs.getFloat("XX_DISCAFTERSALEAMOUNT"), i, 4);
						//Descuento por Centralización de Ventas
						xTableDetails.setValueAt(rs.getFloat("XX_CENTRADISCDELIAMOUNT"), i,5);
						//Descuento por Aporte A publicidad
						xTableDetails.setValueAt(rs.getFloat("XX_PUBAMOUNT"), i, 6);
	
						
						i++;				
					}
					rs.close();
					pstmt.close();	
			
				}
				catch(SQLException er)
				{	
					er.getMessage();
				}	
			}
		});
    	
	}
	/*
	 * Método para cargar los datos en la tabla principal, se ejecuta el query y se plican los filtros si los hay
	 */
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
		// Actual Taxable Period
		dateInit.set(year, month, 1, 0, 0, 0);
		dateEnd.set(year, month, lastDay, 0, 0, 0);
	
		//Mostrar todos los avisos de creditos
		if(tableInit_option==0)
		{	
			m_sql.append("select cb.value ||'-'|| cb.name vendor, co.C_COUNTRY_ID countryID, " +
					"co.name country, " +
					"CASE WHEN det.XX_MONTH is NOT null THEN det.XX_MONTH ||' / '|| det.XX_YEAR " +
					"     ELSE TO_CHAR(cn.CREATED, 'MM/YYYY') " +
					"END monthYear, " +
					"cn.description, " +
					"round(cn.XX_AMOUNT,2) amount, round(cn.XX_AMOUNT_IVA,2) iva, round(cn.XX_AMOUNT+cn.XX_AMOUNT_IVA,2) total, " +
					"CASE WHEN det.XX_MONTH is NOT null THEN TO_CHAR(det.XX_MONTH) " +
				     	 "ELSE TO_CHAR(cn.CREATED, 'MM') " +
				    "END XX_MONTH, " +
				    "CASE WHEN det.XX_YEAR is NOT null THEN TO_CHAR(det.XX_YEAR) " +
				     	 "ELSE TO_CHAR(cn.CREATED, 'YYYY') " +
				    "END XX_YEAR, " +
					"o.XX_VMR_DEPARTMENT_ID, o.XX_VMR_CATEGORY_ID, o.C_BPARTNER_ID vendorID, " +
					"cn.XX_CREDITNOTIFYRETURN_ID creditID, cn.C_ORDER_ID, to_char(cn.created,'dd-mm-yyyy') dateCreated " +
					"from  XX_CREDITNOTIFYRETURN cn, C_BPARTNER cb, C_COUNTRY co, C_ORDER o, XX_VMR_DEPARTMENT dep, " +
					"XX_VMR_CATEGORY cat, XX_VCN_DETAILADVICE det, C_BPARTNER_LOCATION cbl, C_LOCATION clo " +
					"where o.C_BPARTNER_ID=cb.C_BPARTNER_ID and cn.C_ORDER_ID=o.C_ORDER_ID AND XX_STATUS = 'CER' " +
					"AND dep.XX_VMR_DEPARTMENT_ID=o.XX_VMR_DEPARTMENT_ID and o.XX_VMR_CATEGORY_ID=cat.XX_VMR_CATEGORY_ID " +
					"and cb.C_Bpartner_ID=cbl.C_Bpartner_ID and clo.C_LOCATION_ID=cbl.C_LOCATION_ID and " +
					"co.C_COUNTRY_ID=clo.C_COUNTRY_ID and det.XX_CREDITNOTIFYRETURN_ID(+)=cn.XX_CREDITNOTIFYRETURN_ID" +
					" and o.AD_CLIENT_ID="+ ctx.getAD_Client_ID()); 
			
			m_groupBy =" group by cb.value, cb.name, co.C_COUNTRY_ID, co.name,cn.CREATED , cn.description, cn.XX_AMOUNT, " +
					"XX_AMOUNT_IVA, det.XX_MONTH, det.XX_YEAR, o.XX_VMR_DEPARTMENT_ID, o.XX_VMR_CATEGORY_ID, " +
					"o.C_BPARTNER_ID, cn.XX_CREDITNOTIFYRETURN_ID, cn.C_ORDER_ID";

			//Se agrega el proveedor
			if(comboBPartner.getSelectedIndex()!=0 && comboBPartner.getSelectedItem() != null){
				if(((KeyNamePair)comboBPartner.getSelectedItem()).getKey() != 0){
					int clave_vendor=((KeyNamePair)comboBPartner.getSelectedItem()).getKey();
					m_sql.append(" AND ").append("cb.C_BPartner_ID=").append(clave_vendor);
				}
			}	
	
			//Se agrega la categoría
			if (categoryCombo.getValue() != null) {
				KeyNamePair cat = (KeyNamePair) categoryCombo.getValue();
				if (cat.getKey() != -1)
					m_sql.append(" AND o.XX_VMR_CATEGORY_ID = ").append(
							cat.getKey()).append(" ");
			}
			//Se agrega el departamento
			if (departmentCombo.getValue() != null) {
				KeyNamePair dep = (KeyNamePair) departmentCombo.getValue();
				if (dep.getKey() != -1)
					m_sql.append(" AND dep.XX_VMR_DEPARTMENT_ID = ").append(
							dep.getKey()).append(" ");
			}
			//agrego el tipo de notificacion al query
			if(notificationTypeCombo.getSelectedIndex()!=0 && notificationTypeCombo.getSelectedItem() != null){	
	
				m_sql.append(" AND ").append("XX_NOTIFICATIONTYPE = ").append("'").append(notType.get(notificationTypeCombo.getSelectedIndex())).append("'");				
			}
			
			//Se agregan las fechas desde-hasta
			Timestamp to = (Timestamp) dateTo.getValue();
			Timestamp from = (Timestamp) dateFrom.getValue();
			if (from != null && to != null) {
				m_sql.append(" AND TRUNC(cn.CREATED) ").append(" >= ").append(
						DB.TO_DATE(from)).append(" ");
				m_sql.append(" AND TRUNC(cn.CREATED) ").append(" <= ").append(
						DB.TO_DATE(to)).append(" ");
			} else if (from != null)
				m_sql.append(" AND TRUNC(cn.CREATED) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			else if (to != null)
				m_sql.append(" AND TRUNC(cn.CREATED) ").append(" <= ").append(
						DB.TO_DATE(to)).append(" ");
			
		}
		
		String SQL = m_sql.toString()+ m_groupBy;
			
			
    	int i = 0;
    	xTable.setRowCount(i);
    	try 
		{	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTable.setRowCount (i+1);			
				//Número de aviso
				xTable.setValueAt(rs.getString("creditID"),i,0);
				
				//proveedor
				xTable.setValueAt(rs.getString("vendor"),i,1);
				
				//pais
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("creditID"), rs.getString("country")), i, 2);
				
				//Concepto
				String SQL2 = "select ta.XX_CONCEPT" +
						" from XX_VCN_TRADEAGREEMENTS ta, C_ORDER co, XX_CREDITNOTIFYRETURN cn" +
						" where ta.DOCUMENTNOORDER=co.DOCUMENTNO and co.C_ORDER_ID=cn.C_ORDER_ID and cn.XX_NOTIFICATIONTYPE='CAC'" +
						" and cn.XX_CREDITNOTIFYRETURN_ID="+rs.getInt("creditID") +" and co.AD_CLIENT_ID="+ ctx.getAD_Client_ID();
				try 
				{	
					PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
					ResultSet rs2 = pstmt2.executeQuery();
					if(rs2.next())
						xTable.setValueAt(rs2.getString(1), i, 3);					
					rs2.close();
					pstmt2.close();	
				}
				catch(SQLException e)
				{	
					System.out.print(e.getMessage());
				}
				
				
				//XX_MONTHYEAR
				xTable.setValueAt(rs.getString("monthYear"), i, 4);
				
				//Tipo de Aviso
				xTable.setValueAt(rs.getString("description"), i, 5);
				
				//Monto del Aviso
				xTable.setValueAt(rs.getBigDecimal("amount"), i,6);
				
				//Monto del IVA
				xTable.setValueAt(rs.getBigDecimal("iva"), i,7);
				
				//Monto Total
				xTable.setValueAt(rs.getBigDecimal("total"), i, 8);
				
				//Monto total en USD
				if(rs.getInt("countryID")!=339){//Si el proveedor es internacional
					String SQL3 = "select rate.MULTIPLYRATE" +
							" from C_CONVERSION_RATE rate, C_PERIOD peri" +
							" where rate.XX_PERIOD_ID=peri.C_PERIOD_ID and sysdate between STARTDATE" +
							" and ENDDATE and C_CURRENCY_ID=100 and rate.AD_CLIENT_ID="+ ctx.getAD_Client_ID();
					try 
					{	
						PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null);
						ResultSet rs3 = pstmt3.executeQuery();
						if(rs3.next())
							xTable.setValueAt(rs.getBigDecimal("total").divide(rs3.getBigDecimal(1),2, RoundingMode.HALF_UP), i, 9);					
						rs3.close();
						pstmt3.close();	
					}
					catch(SQLException e)
					{	
						System.out.print(e.getMessage());
					}
				}
				//Fecha de creación del aviso
				xTable.setValueAt(rs.getString("dateCreated"), i, 10);
				
				//Fecha emisión del proceso
				String SQL4 = "select to_char(ci.CREATED,'dd-mm-yyyy')" +
						" from C_INVOICE ci, XX_CREDITNOTIFYRETURN cn" +
						" where ci.DOCUMENTNO=cn.XX_DOCUMENTNO and ci.C_BPARTNER_ID=" + rs.getInt("vendorID")+
						" and ci.C_DocTypeTarget_ID="+ ctx.getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID")+
						" and ci.AD_CLIENT_ID="+ ctx.getAD_Client_ID();
			try 
			{	
				PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null);
				ResultSet rs4 = pstmt4.executeQuery();
				if(rs4.next())
					xTable.setValueAt(rs4.getString(1), i, 11);					
				rs4.close();
				pstmt4.close();	
			}
			catch(SQLException e)
			{	
				System.out.print(e.getMessage());
			}
		
				i++;				
			}
			rs.close();
			pstmt.close();	
	
		}
		catch(SQLException e)
		{	
			e.getMessage();
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
			bExport.setEnabled(true);
		}else if (e.getSource() == categoryCombo) {
			dynDepartament();
			bExport.setEnabled(false);
		}
		else if (e.getSource() == comboBPartner) {
			bExport.setEnabled(false);
		}
		else if (e.getSource() == departmentCombo) {
			bExport.setEnabled(false);
		}
		else if (e.getSource() == notificationTypeCombo) {
			bExport.setEnabled(false);
		}
		else if(e.getSource() == bReset){
			loadBasicInfo();
			bExport.setEnabled(false);
		}
		else if(e.getSource() == bExport){
			generateReport();
		}
	} 
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{		
		//Se realiza la búsqueda si con los filtros seleccionados, si no hay filtros seleccionados se busca todo
			tableInit_option=0;		
			tableInit();
			if(xTable.getRowCount()!=0){
				statusBar.setStatusLine("Avisos/Notas");
				statusBar.setStatusDB(xTable.getRowCount());
			}else
				showCreditNotificationCondition();
	}	

	/**
	 *  Table initial state 
	 */
	private void loadBasicInfo(){
		
		comboBPartner.removeActionListener(this);
		comboBPartner.setEnabled(true); 

		comboBPartner.removeAllItems();
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

		// Cargar proveedores
		String sql = "SELECT b.C_BPARTNER_ID, b.NAME FROM C_BPARTNER b WHERE isVendor='Y' ";
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
	  *  Shows the searched order condition
	  */
	 private void showCreditNotificationCondition()
	 {
	 	//Muestro el mensaje segun la condicion	 	
	 	ADialog.warn(m_WindowNo, this.mainPanel, Msg.translate(Env.getCtx(), "XX_NoNotificationsForFilter"));	
	 }
	 
	 /**
	  * 
	  */
	 private void generateReport(){
		 
		 String[] options = new String[] {"PDF", "EXCEL"};
		   Integer answer = JOptionPane.showOptionDialog(null, "Exportar datos en formato:", "Reporte de Avisos Recuperados", 
		        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
		        null, options, options[0]);
		   
		 int cat = 0;
		 int vendor = 0;
		 int dep = 0; 
		 String type = "X";
		 String format = "";
		 String myDateFrom = "01/01/1900";
		 String myDateTo = "01/01/2900";
		 
		 if(answer==-1)
			 return;
		 else if (answer==0)
			 format = "pdf";
		 else if (answer==1)
			 format = "xls";
		 
		//Proveedor
		if(comboBPartner.getSelectedIndex()!=0 && comboBPartner.getSelectedItem() != null){
			vendor = ((KeyNamePair)comboBPartner.getSelectedItem()).getKey();
		}	

		//Categoría
		if (categoryCombo.getValue() != null && categoryCombo.getSelectedIndex()!=0) {
			cat = ((KeyNamePair) categoryCombo.getValue()).getKey();
		}
		
		//Departamento
		if (departmentCombo.getValue() != null && departmentCombo.getSelectedIndex()!=0) {
			dep = ((KeyNamePair) departmentCombo.getValue()).getKey();
		}
		
		//agrego el tipo de notificacion al query
		if(notificationTypeCombo.getSelectedIndex()!=0 && notificationTypeCombo.getSelectedItem() != null){	
			type = notType.get(notificationTypeCombo.getSelectedIndex());		
		}
		
		//Fechas
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(dateTo.getValue()!=null)
			myDateTo = sdf.format((Timestamp) dateTo.getValue());
		if(dateFrom.getValue()!=null)
			myDateFrom = sdf.format((Timestamp) dateFrom.getValue());

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("COMPANY");
		myReport.parameterValue.add(Env.getCtx().getAD_Client_ID());
		myReport.parameterName.add("DESDE");
		myReport.parameterValue.add(myDateFrom);
		myReport.parameterName.add("HASTA");
		myReport.parameterValue.add(myDateTo);
		myReport.parameterName.add("DEPART");
		myReport.parameterValue.add(dep);
		myReport.parameterName.add("CATEG");
		myReport.parameterValue.add(cat);
		myReport.parameterName.add("TYPE");
		myReport.parameterValue.add(type);
		myReport.parameterName.add("PROV");
		myReport.parameterValue.add(vendor);
		
		//Correr Reporte
		myReport.runReport("RecoveredNotices", format);
	 }
	 
}

