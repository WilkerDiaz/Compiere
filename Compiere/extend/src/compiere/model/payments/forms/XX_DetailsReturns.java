package compiere.model.payments.forms;

import java.awt.BorderLayout;
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
import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
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


import compiere.model.cds.Utilities;

/**
 * Detalle de las Devoluciones
 * @author Jessica Mendoza
 *
 */
public class XX_DetailsReturns extends CPanel
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
	private int m_by = Env.getCtx().getAD_User_ID();
	StringBuffer m_sql = null;
	String m_groupBy = "";
	String m_orderBy = "";
	Ctx ctx = Env.getCtx();
	
	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);	
	private JScrollPane xScrollPane = new JScrollPane();
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private TitledBorder xBorder =
		new TitledBorder(Msg.getMsg(Env.getCtx(), "XX_DetailReturns"));

	/****Botones****/
	private CButton bSearch = new CButton();
	private JButton bReset = new CButton();
	
	/****Labels-ComboBox-Fecha****/
	private CLabel dateLabel = new CLabel();
	private VDate date = new VDate(Msg.translate(Env.getCtx(), "Date"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "Date"));
	private CLabel typePartnerLabel = new CLabel();
	private CComboBox typePartnerCombo = new CComboBox();
	private CLabel warehouseLabel = new CLabel();
	private CComboBox warehouseCombo = new CComboBox();
	private CLabel departmentLabel = new CLabel();
	private CComboBox departmentCombo = new CComboBox();
	private CLabel totalGeneralLabel = new CLabel();
	private CLabel precioGeneralLabel = new CLabel();
	private CLabel costoGeneralLabel = new CLabel();
	private CLabel precioLabel = new CLabel();
	private CLabel costoLabel = new CLabel();
	
	/****Variables****/
	Utilities util = new Utilities();
	NumberFormat formato = NumberFormat.getInstance();

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);	
		try{
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);	
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
		dateLabel.setText(Msg.getMsg(Env.getCtx(), "Date"));
		typePartnerLabel.setText(Msg.getMsg(Env.getCtx(), "XX_OrderType"));
		warehouseLabel.setText(Msg.getMsg(Env.getCtx(), "XX_WarehouseName"));
		departmentLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Department"));
		totalGeneralLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TotalReturns"));   
		precioLabel.setText(Msg.getMsg(Env.getCtx(), "XX_AmountSale"));
		costoLabel.setText(Msg.getMsg(Env.getCtx(), "XX_AmountCost"));
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearch.setEnabled(true);
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		bReset.addActionListener(this);
		mainPanel.setLayout(mainLayout);
		
		northPanel.setLayout(northLayout);
		xPanel.setLayout(xLayout);
		
		centerPanel.setLayout(centerLayout);
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(920, 220));
		xPanel.setLayout(xLayout);
		
		southPanel.setLayout(southLayout);
		xPanel.setLayout(xLayout);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		xScrollPane.getViewport().add(xTable);
		
		/**
		 * Panel Superior
		 */		
		/****Etiquetas de la primera fila****/	
		northPanel.add(typePartnerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(typePartnerCombo, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(dateLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,	
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						12, 5, 5, 5), 0, 0));
		northPanel.add(date, new GridBagConstraints(3, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		
		/****Etiquetas de la segunda fila****/
		northPanel.add(warehouseLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		northPanel.add(warehouseCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 0), 0, 0));
		northPanel.add(departmentLabel, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		northPanel.add(departmentCombo, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 0), 0, 0));
		
		/****Etiquetas de la tercera fila****/
		northPanel.add(bSearch, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 12), 0, 0));		
		northPanel.add(bReset, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 5, 12), 0, 0)); 
		
		/**
		 * Panel Inferior
		 */
		/****Etiquetas de la primera fila****/
		southPanel.add(precioLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 15, 12, 5), 0, 0));
		southPanel.add(costoLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(12, 25, 12, 0), 0, 0));
		
		/****Etiquetas de la segunda fila****/
		southPanel.add(totalGeneralLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(5, 5, 12, 5), 0, 0));
		southPanel.add(precioGeneralLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(5, 45, 12, 0), 0, 0));
		southPanel.add(costoGeneralLabel, new GridBagConstraints(3, 2, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(5, 55, 12, 0), 0, 0));
		southPanel.validate();

		totalGeneralLabel.setVisible(false);
		precioLabel.setVisible(false);
		costoLabel.setVisible(false);
	}
	
	/**
	 * Carga una lista con los tipos de ordenes (nacional, importada)
	 */
	void dynTypePartner(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlType = "select value, name " +
						 "from AD_Ref_List " +
						 "where AD_Reference_ID = 1000050 " +
						 "order by name ";	
		try {
			pstmt = DB.prepareStatement(sqlType, null);
			rs = pstmt.executeQuery();

			typePartnerCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				typePartnerCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			typePartnerCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlType, e);
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
	 * Carga una lista con todas las tiendas
	 */
	void dynWarehouse(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlWarehouse = "select M_Warehouse_ID, value||' '||name " +
							  "from M_Warehouse " +
							  "order by value ";
		try {
			pstmt = DB.prepareStatement(sqlWarehouse, null);
			rs = pstmt.executeQuery();

			warehouseCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				warehouseCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			warehouseCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlWarehouse, e);
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
	 * Carga una lista con todas los departamentos
	 */
	void dynDepartmet(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlDepartment = "select XX_VMR_Department_ID, value||' '||name " +
							  "from XX_VMR_Department " +
							  "order by value ";
		try {
			pstmt = DB.prepareStatement(sqlDepartment, null);
			rs = pstmt.executeQuery();

			departmentCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				departmentCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			departmentCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlDepartment, e);
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
	 * Genera las columnas para la primera tabla
	 */
	private void dynInit(){
		llenarCombos();
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutF = new ColumnInfo[] {
				/****0-Departamento****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Department"), "", KeyNamePair.class),				
				/****1-Fecha de Solicitud****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_DateApplication"), ".", String.class),				
				/****2-Nro. de Solicitud****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_NumberApplication"), ".", String.class),
				/****3-Código de Producto****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "Product"), ".", String.class),
				/****4-Cantidad Producto****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_ProductQty"), ".", int.class),
				/****5-Precio Anterior****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_PreviousPrice"), ".", float.class),
				/****6-Precio Actual****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_CurrentPrice"), ".", float.class),
				/****7-Costo Anterior****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_PreviousCost"), ".", float.class),
				/****8-Costo Actual****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_CurrentCost"), ".", float.class),
				/****9-Monto de Venta****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_AmountSale"), ".", float.class),
				/****10-Venta/Impuesto****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_SaleTax"), ".", float.class),
				/****11-Monto de Costo****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_AmountCost"), ".", float.class),
				/****12-Costo/Impuesto****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_CostTax"), ".", float.class),
		};
		xTable.prepareTable(layoutF, "", "", true, "");
		CompiereColor.setBackground (this);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		xTable.getSelectionModel().addListSelectionListener(this);		
		xTable.getColumnModel().getColumn(0).setMinWidth(220);
		xTable.getColumnModel().getColumn(0).setCellRenderer(renderLeft);
		xTable.getColumnModel().getColumn(1).setMinWidth(120);
		xTable.getColumnModel().getColumn(1).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(2).setMinWidth(120);
		xTable.getColumnModel().getColumn(2).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(3).setMinWidth(100);
		xTable.getColumnModel().getColumn(3).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(4).setMinWidth(130);
		xTable.getColumnModel().getColumn(4).setCellRenderer(renderCenter);
		xTable.getColumnModel().getColumn(5).setMinWidth(100);	
		xTable.getColumnModel().getColumn(5).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(6).setMinWidth(100);	
		xTable.getColumnModel().getColumn(6).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(7).setMinWidth(100);
		xTable.getColumnModel().getColumn(7).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(8).setMinWidth(100);	
		xTable.getColumnModel().getColumn(8).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(9).setMinWidth(100);	
		xTable.getColumnModel().getColumn(9).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(10).setMinWidth(100);	
		xTable.getColumnModel().getColumn(10).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(11).setMinWidth(100);	
		xTable.getColumnModel().getColumn(11).setCellRenderer(renderRight);
		xTable.getColumnModel().getColumn(12).setMinWidth(100);
		xTable.getColumnModel().getColumn(12).setCellRenderer(renderRight);
		bSearch.addActionListener(this);
		bReset.addActionListener(this);	
	}
	
	/**
	 * Carga la primera tabla del panel central norte
	 */
	private void tableInitF(){
		BigDecimal precioGeneral = new BigDecimal(0);
		BigDecimal costoGeneral = new BigDecimal(0);
		GregorianCalendar cal = new GregorianCalendar(); 
		m_sql = new StringBuffer();

		m_sql.append("with consecutive1 as " +
				"(select XX_SalePrice as precio, XX_PriceConsecutive as nro, M_Product_ID as idPro, " +
				"M_AttributeSetInstance_ID as idInst, XX_UnitPurchasePrice as costo " +
				"from XX_VMR_PriceConsecutive), " +
				"consecutive2 as " +
				"(select XX_SalePrice as precio, XX_PriceConsecutive as nro, M_Product_ID as idPro, " +
				"M_AttributeSetInstance_ID as idInst, XX_UnitPurchasePrice as costo " +
				"from XX_VMR_PriceConsecutive) " +
				"select dep.XX_VMR_Department_ID, dep.value||'  '||dep.name, to_char(mov.Created,'dd/MM/yyyy'), " +
				"mov.documentNo, pro.value, pro.M_Product_ID, con1.precio as pActual, con1.costo as cActual, " +
				"con2.precio as pAnterior, con2.costo as cAnterior, sum(mol.XX_ApprovedQty) as cantProduct, " +
				"sum(mol.XX_SalePrice) as precio, sum(mol.TaxAmt) as precioTax, " +
				"sum(asi.PriceActual) as costo, sum(mol.TaxAmt) as costoTax " +
				"from M_Movement mov " +
				"inner join M_MovementLine mol on (mov.M_Movement_ID = mol.M_Movement_ID) " +
				"inner join M_Warehouse war on (mov.M_WarehouseFrom_ID = war.M_Warehouse_ID) " +
				"inner join C_BPartner par on (mov.C_BPartner_ID = par.C_BPartner_ID) " +
				"inner join XX_VMR_Department dep on (mov.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
				"left join M_AttributeSetInstance asi on (asi.M_AttributeSetInstance_ID = mol.M_AttributeSetInstance_ID) " +
				"inner join M_Product pro on (mol.M_Product_ID = pro.M_Product_ID) " +
				"inner join consecutive1 con1 on (con1.idPro = pro.M_Product_ID and asi.M_AttributeSetInstance_ID = con1.idInst) " +
				"inner join consecutive2 con2 on (con2.idPro = pro.M_Product_ID and asi.M_AttributeSetInstance_ID = con2.idInst) " +
				"where mov.XX_Status in ('AT','IT') " +
				"and mov.C_DocType_ID = 1000355 " +
				"and con1.nro = (select max(XX_PriceConsecutive) from XX_VMR_PriceConsecutive where M_Product_ID = pro.M_Product_ID) " +
				"and con2.nro = ((select max(XX_PriceConsecutive) from XX_VMR_PriceConsecutive where M_Product_ID = pro.M_Product_ID) - 1) ");

		m_groupBy = "group by dep.XX_VMR_Department_ID, dep.value, dep.name, mov.documentNo, pro.value, con1.precio, con1.costo, " +
					"mov.Created, pro.M_Product_ID, con2.precio, con2.costo ";
		m_orderBy = "order by dep.value asc  ";

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Hace la busqueda por la fecha del filtro****/
		Timestamp fecha = (Timestamp) date.getValue();		
		if (fecha != null){
			cal.setTime(fecha);
			System.out.println(cal.get(GregorianCalendar.MONTH)+1);
			m_sql.append("and to_char(mov.XX_DispatchDate,'yyyy') = ").append(cal.get(GregorianCalendar.YEAR)).append(" ");
			m_sql.append("and to_char(mov.XX_DispatchDate,'MM') = ").append(cal.get(GregorianCalendar.MONTH)+1).append(" ");
		}
		/****Buscar los proveedores segun el tipo de socio de negocio****/
		if ((typePartnerCombo.getSelectedItem() != null) && (typePartnerCombo.getSelectedIndex() != 0)){
			int tipo = ((KeyNamePair)typePartnerCombo.getSelectedItem()).getKey();
			m_sql.append("and par.XX_VendorClass = ").append(tipo).append(" ");
		}
		/****Hace la busqueda por la tienda****/
		if ((warehouseCombo.getSelectedItem() != null) && (warehouseCombo.getSelectedIndex() != 0)){
			int idTienda = ((KeyNamePair)warehouseCombo.getSelectedItem()).getKey();
			m_sql.append("and war.M_Warehouse_ID = ").append(idTienda).append(" ");
		}
		/****Hace la busqueda por el departamento****/
		if ((departmentCombo.getSelectedItem() != null) && (departmentCombo.getSelectedIndex() != 0)){
			int idDep = ((KeyNamePair)departmentCombo.getSelectedItem()).getKey();
			m_sql.append("and dep.XX_VMR_Department_ID = ").append(idDep).append(" ");
		}

		String SQL = m_sql.toString() + m_groupBy.toString() + m_orderBy.toString();
		int i = 0;
		xTable.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTable.setRowCount (i+1);		
				/****0-Departamento****/
				xTable.setValueAt(new KeyNamePair (rs.getInt("XX_VMR_Department_ID"), rs.getString(2)), i, 0);				
				/****1-Fecha de Solicitud****/
				xTable.setValueAt(rs.getString(3), i, 1);				
				/****2-Nro. de Solicitud****/
				xTable.setValueAt(rs.getString(4), i, 2);
				/****3-Código de Producto****/
				xTable.setValueAt(new KeyNamePair (rs.getInt("M_Product_ID"), rs.getString(5)), i, 3);
				/****4-Cantidad Producto****/
				xTable.setValueAt(rs.getInt(11), i, 4);
				/****5-Precio Anterior****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(9).setScale(2, RoundingMode.DOWN)), i, 5);
				/****6-Precio Actual****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(7).setScale(2, RoundingMode.DOWN)), i, 6);
				/****7-Costo Anterior****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(10).setScale(2, RoundingMode.DOWN)), i, 7);
				/****8-Costo Actual****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(8).setScale(2, RoundingMode.DOWN)), i, 8);
				/****9-Monto de Venta****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(12).setScale(2, RoundingMode.DOWN)), i, 9);
				precioGeneral = precioGeneral.add(rs.getBigDecimal(12)).setScale(2, RoundingMode.DOWN);
				/****10-Venta/Impuesto****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(13).setScale(2, RoundingMode.DOWN)), i, 10);
				/****11-Monto de Costo****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(14).setScale(2, RoundingMode.DOWN)), i, 11);
				costoGeneral = costoGeneral.add(rs.getBigDecimal(14)).setScale(2, RoundingMode.DOWN);
				/****12-Costo/Impuesto****/
				xTable.setValueAt(formato.format(rs.getBigDecimal(15).setScale(2, RoundingMode.DOWN)), i, 12);
				i++;
			}	
			totalGeneralLabel.setVisible(true);
			precioLabel.setVisible(true);
			costoLabel.setVisible(true);
			precioGeneralLabel.setText(formato.format(precioGeneral));
			precioGeneralLabel.setFont(new Font("Serif", Font.BOLD,14));
			costoGeneralLabel.setText(formato.format(costoGeneral));
			costoGeneralLabel.setFont(new Font("Serif", Font.BOLD,14));
					
		}catch(SQLException e){	
			e.getMessage();
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
	}
	
	/**
	 * Llena los combos del filtro
	 */
	private void llenarCombos(){
		dynTypePartner();
		dynWarehouse();
		dynDepartmet();
	}
	
	@Override
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}
	
	/**
	 *  Busca la informacion a través de los filtros 
	 *  arreglar
	 */
	private void cmdSearch(){
		tableInitF();
	}
	
	/**
	 *  Remueve los items de todos los campos filtros
	 */
	private void removeItemsToReset(){
		warehouseCombo.removeAllItems();
		departmentCombo.removeAllItems();
		typePartnerCombo.removeAllItems();
		xTable.setRowCount(0);
		totalGeneralLabel.setVisible(false);
		precioGeneralLabel.setVisible(false);
		precioLabel.setVisible(false);
		costoGeneralLabel.setVisible(false);
		costoLabel.setVisible(false);
		llenarCombos();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bSearch){
			cmdSearch();
		}else if (e.getSource() == bReset){
			removeItemsToReset();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	
	}

}
