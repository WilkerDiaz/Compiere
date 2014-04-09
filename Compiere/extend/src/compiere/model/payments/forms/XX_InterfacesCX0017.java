package compiere.model.payments.forms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import compiere.model.payments.X_Ref_XX_ListTransferCX017;
import compiere.model.payments.X_XX_VCN_AccoutingEntry;

/**
 * 
 * @author Jessica Mendoza
 * compiere.model.payments.forms.XX_InterfacesCX0017
 */
public class XX_InterfacesCX0017 extends CPanel
implements FormPanel, ActionListener, ListSelectionListener, WindowStateListener{
	
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

	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();	
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private JScrollPane xScrollPaneHead = new JScrollPane();
	private MiniTable xTableHead = new MiniTable();
	private JScrollPane xScrollPaneDetail = new JScrollPane();
	private MiniTable xTableDetail = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private FlowLayout sLayout = new FlowLayout(FlowLayout.RIGHT, 10, 0);
	private TitledBorder xBorderHead =
		new TitledBorder(Msg.getMsg(Env.getCtx(), "XX_AccoutingEntry"));
	private TitledBorder xBorderDetail =
		new TitledBorder(Msg.getMsg(Env.getCtx(), "XX_DetailAccountEntry"));

	/****Botones****/
	private CButton bSearch = new CButton();
	private JButton bTransfer = new CButton();
	
	/****Labels-ComboBox-Fecha****/
	private CLabel typeTransferLabel = new CLabel();
	private CComboBox typeTransferCombo = new CComboBox();
	private CLabel monthLabel = new CLabel();
	private CComboBox monthCombo = new CComboBox();
	private CLabel yearLabel = new CLabel();
	private CTextField yearField = new CTextField();
	private CLabel typeAccountEntryLabel = new CLabel();
	private CComboBox typeAccountEntryCombo = new CComboBox();
	private CLabel typeOrdContLabel = new CLabel();
	private CLabel difTotalLabel = new CLabel();
	private CLabel difTotal = new CLabel();
	private CLabel datelabel = new CLabel();
	private VDate date = new VDate(Msg.translate(Env.getCtx(), "XX_DateTransfer"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "XX_DateTransfer"));
	private CLabel warehouseLabel = new CLabel();
	private CComboBox warehouseCombo = new CComboBox();
	private CLabel typeTransferSPLabel = new CLabel();
	private CComboBox typeTransferSPCombo = new CComboBox();
	
	/****Variables****/
	static Integer sync = new Integer(0); 
	NumberFormat formato = NumberFormat.getInstance();
	DecimalFormat myFormatter = new DecimalFormat("###,##0.00");
	int varTypeTransfer;
	int idWarehouse;
	int idTypeTransferSP;
	String typeTransfer = "";
	String typeAccounting = "";
	String month = "";
	String year = "";
		
	//Almacena el id del asiento contable y la diferencia entre el debe y el haber
	HashMap<Integer, BigDecimal> headHashmap;
	//Almacena el id del asiento contable y si es transferible o no
	HashMap<Integer, Boolean> transferHashmap;
	//Almacena el value del tipo de transferencia de la compra/venta
	HashMap<Integer, String> transferSPHashmap;
	
	/****Variables queries****/
	StringBuffer m_sql = null;
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bSearch)
			cmdSearch();
		else if (e.getSource() == bTransfer)
			cmdTransfer();
	}

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);	
		try{
			jbInit();
			dynInitFirst();
			frame.getContentPane().add(mainPanel, BorderLayout.NORTH);	
			//frame.setResizable(false); //Se utiliza para bloquear el boton maximizar de la ventana			
		}
		catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub		
	}
	
	/**
	 * Organiza las posiciones de cada etiqueta en la forma
	 * @throws Exception
	 */
	private void jbInit() throws Exception{
		/****Seteo de las etiquetas****/
		typeTransferLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TypeTransfer"));
		monthLabel.setText(Msg.getMsg(Env.getCtx(), "Month"));
		yearLabel.setText(Msg.getMsg(Env.getCtx(), "Year"));
		typeAccountEntryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TypeAccountEntry"));
		typeOrdContLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Type"));
		datelabel.setText(Msg.getMsg(Env.getCtx(), "XX_DateTransfer"));
		datelabel.setVisible(false);
		date.setVisible(false);
		difTotalLabel.setText(Msg.getMsg(Env.getCtx(), "Difference"));
		warehouseLabel.setText(Msg.getMsg(Env.getCtx(), "XX_WarehouseName"));
		typeTransferSPLabel.setText(Msg.getMsg(Env.getCtx(), "XX_TypeTransferSP"));
		
		difTotalLabel.setVisible(false);
		typeAccountEntryLabel.setVisible(false);
		typeAccountEntryCombo.setVisible(false);
		
		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearch.setEnabled(false);
		bTransfer.setText(Msg.getMsg(Env.getCtx(), "XX_Transfer"));
		bTransfer.setVisible(false);
		bTransfer.addActionListener(this);

		mainPanel.setLayout(mainLayout);
		
		northPanel.setLayout(northLayout);
		xPanel.setLayout(xLayout);
		
		centerPanel.setLayout(centerLayout);
		xScrollPaneHead.setBorder(xBorderHead);
		xScrollPaneHead.setPreferredSize(new Dimension(850, 200));
		xScrollPaneDetail.setBorder(xBorderDetail);
		xScrollPaneDetail.setPreferredSize(new Dimension(850, 200));
		xPanel.setLayout(xLayout);
		
		southPanel.setLayout(southLayout);
		xPanel.setLayout(sLayout);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		centerPanel.add(xScrollPaneHead,  BorderLayout.NORTH);
		centerPanel.add(xScrollPaneDetail, BorderLayout.SOUTH);
		xScrollPaneHead.getViewport().add(xTableHead);
		xScrollPaneDetail.getViewport().add(xTableDetail);
		
		/**
		 * Panel Superior
		 */			
		/****Etiquetas de la primera fila****/	
		northPanel.add(monthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,	
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 1, 2, 2), 0, 0));
		northPanel.add(monthCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 1, 2, 5), 0, 0));
		northPanel.add(yearLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 1, 2, 2), 0, 0));
		northPanel.add(yearField, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,		
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 1, 2, 5), 50, 0));
		
		/****Etiquetas de la segunda fila****/
		northPanel.add(typeTransferLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 1, 10, 2), 0, 0));
		northPanel.add(typeTransferCombo, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(12, 1, 10, 5), 0, 0));
		northPanel.add(typeAccountEntryLabel, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 1, 10, 2), 0, 0));
		northPanel.add(typeAccountEntryCombo, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.CENTER,
				new Insets(5, 1, 10, 5), 0, 0));
		northPanel.add(warehouseLabel, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 1, 10, 2), 0, 0));
		northPanel.add(warehouseCombo, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.CENTER,
				new Insets(5, 1, 10, 5), 0, 0));
		
		/****Etiqueta de la tercera fila****/
		northPanel.add(typeTransferSPLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 1, 10, 2), 0, 0));
		northPanel.add(typeTransferSPCombo, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.CENTER,
				new Insets(5, 1, 10, 5), 0, 0));
				
		/****Etiquetas de la tercera fila****/
		northPanel.add(bSearch, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 2), 15, 0));
		
		/****Etiquetas de la cuarta fila****/
		northPanel.add(datelabel, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 2), 0, 0));
		northPanel.add(date, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 2), 0, 0));	
		northPanel.add(bTransfer, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 2), 0, 0));		
		
		/**
		 * Panel Inferior
		 */				
		/****Etiquetas de la primera fila****/
		southPanel.add(difTotalLabel, new GridBagConstraints(4, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 20, 5), 0, 0));
		southPanel.add(difTotal, new GridBagConstraints(5, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 5, 20, 5), 0, 0));		
		llenarCombos();
	}
	
	/**
	 * Se encarga de llenar el listado con los tipos de transferencias
	 */
	void dynTypeTransfer() {
		
		warehouseLabel.setVisible(false);
		warehouseCombo.setVisible(false);
		typeAccountEntryLabel.setVisible(false);
		typeAccountEntryCombo.setVisible(false);
		typeTransferSPLabel.setVisible(false);
		typeTransferSPCombo.setVisible(false);
		
		typeTransferCombo.addItem(new KeyNamePair(-1, null));
		typeTransferCombo.addItem(new KeyNamePair(1, "Cuentas por Pagar Aprobadas"));
		typeTransferCombo.addItem(new KeyNamePair(2, "Cuentas por Pagar Estimadas"));
		typeTransferCombo.addItem(new KeyNamePair(3, "Compra/Venta"));
		typeTransferCombo.addItem(new KeyNamePair(4, "Diario de Caja"));
		typeTransferCombo.addItem(new KeyNamePair(5, "Banco"));
		typeTransferCombo.setBackground(false);
		typeTransferCombo.addActionListener(this);
		
		/****Captura el evento para realizar otra accion****/
		typeTransferCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {

			@Override
			public void contentsChanged(ListDataEvent e) {
				varTypeTransfer = typeTransferCombo.getSelectedIndex();
				if(varTypeTransfer == 1){ //Para la transferencia de las Cuentas por Pagar Aprobadas
					warehouseLabel.setVisible(false);
					warehouseCombo.setVisible(false);
					typeTransferSPLabel.setVisible(false);
					typeTransferSPCombo.setVisible(false);
					bSearch.setEnabled(true);
					dynTypeAccountingEntry();
				}else if (varTypeTransfer == 2){ //Para la transferencia de las Cuentas por Pagar Estimdas
					warehouseLabel.setVisible(false);
					warehouseCombo.setVisible(false);
					typeAccountEntryLabel.setVisible(false);
					typeAccountEntryCombo.setVisible(false);
					typeTransferSPLabel.setVisible(false);
					typeTransferSPCombo.setVisible(false);
					bSearch.setEnabled(true);
				}else if (varTypeTransfer == 3){ //Para la transferencia de la Compra/Venta
					typeAccountEntryLabel.setVisible(false);
					typeAccountEntryCombo.setVisible(false);
					bSearch.setEnabled(true);
					dynWarehouse();
					dynTypeTransferSP();
				}else if (varTypeTransfer == 4){//Para la transferencia del Diario de Caja
					typeAccountEntryLabel.setVisible(false);
					typeAccountEntryCombo.setVisible(false);
					typeTransferSPLabel.setVisible(false);
					typeTransferSPCombo.setVisible(false);
					bSearch.setEnabled(true);
					dynWarehouse();
				}else if (varTypeTransfer == 5){//Para la transferencia del Banco
				typeAccountEntryLabel.setVisible(false);
				typeAccountEntryCombo.setVisible(false);
				typeTransferSPLabel.setVisible(false);
				typeTransferSPCombo.setVisible(false);
				warehouseLabel.setVisible(false);
				warehouseCombo.setVisible(false);
				bSearch.setEnabled(true);
			}

			}

			@Override
			public void intervalAdded(ListDataEvent e) {
							
			}

			@Override
			public void intervalRemoved(ListDataEvent e) {
								
			}
		});
		
		m_frame.addWindowStateListener(this);
		
	}
	
	
	public void windowStateChanged(WindowEvent e) {
	        
		if(displayStateMessage(e).equals("MAXIMIZED_BOTH")){
			xScrollPaneDetail.setPreferredSize(new Dimension(850, 400));
		}
		else{
			xScrollPaneDetail.setPreferredSize(new Dimension(850, 200));
		}	
	}
	
	private String displayStateMessage(WindowEvent e) {
        
		int state = e.getNewState();
        return convertStateToString(state);
    }
	
	 String convertStateToString(int state) {
	        if (state == Frame.NORMAL) {
	            return "NORMAL";
	        }
	        String strState = " ";
	        if ((state & Frame.ICONIFIED) != 0) {
	            strState += "ICONIFIED";
	        }
	        //MAXIMIZED_BOTH is a concatenation of two bits, so
	        //we need to test for an exact match.
	        if ((state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
	            strState += "MAXIMIZED_BOTH";
	        } else {
	            if ((state & Frame.MAXIMIZED_VERT) != 0) {
	                strState += "MAXIMIZED_VERT";
	            }
	            if ((state & Frame.MAXIMIZED_HORIZ) != 0) {
	                strState += "MAXIMIZED_HORIZ";
	            }
	            if (" ".equals(strState)){
	                strState = "UNKNOWN";
	            }
	        }
	        return strState.trim();
	    }
	
	/**
	 * Se encarga de llenar el listado con los tipos de asientos
	 */
	void dynTypeAccountingEntry(){
		
		typeAccountEntryCombo.removeAllItems();
		typeAccountEntryLabel.setVisible(true);
		typeAccountEntryCombo.setVisible(true);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqltypeAccounting = "select AD_Ref_List_ID, name " +
								   "from AD_Ref_List_Trl " +
								   "where AD_Ref_List_ID in (" + 
								   		Env.getCtx().getContextAsInt("#XX_L_PROCESSITEM_ID") + 
								   		"," + Env.getCtx().getContextAsInt("#XX_L_PROCESSASSETSERV_ID") + ") ";

		try {
			pstmt = DB.prepareStatement(sqltypeAccounting, null);
			rs = pstmt.executeQuery();

			typeAccountEntryCombo.addItem(new KeyNamePair(-1,null));
			while (rs.next()) {
				typeAccountEntryCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			typeAccountEntryCombo.addActionListener(this);
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqltypeAccounting, e);
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
	 * Se encarga de mostrar los meses del año
	 */
	void dynMonth() {
		
		String[][] meses = {{String.valueOf(Calendar.JANUARY),"Enero"},
			 	{String.valueOf(Calendar.FEBRUARY),"Febrero"},
			 	{String.valueOf(Calendar.MARCH),"Marzo"},
			 	{String.valueOf(Calendar.APRIL),"Abril"},
			 	{String.valueOf(Calendar.MAY),"Mayo"},			 	
			 	{String.valueOf(Calendar.JUNE),"Junio"},
			 	{String.valueOf(Calendar.JULY),"Julio"},
			 	{String.valueOf(Calendar.AUGUST),"Agosto"},
			 	{String.valueOf(Calendar.SEPTEMBER),"Septiembre"},
			 	{String.valueOf(Calendar.OCTOBER),"Octubre"},
			 	{String.valueOf(Calendar.NOVEMBER),"Noviembre"},
			 	{String.valueOf(Calendar.DECEMBER),"Diciembre"}};

		monthCombo.addItem(new KeyNamePair(-1,null));
		for (int i = 0; i < meses.length; i++){
			monthCombo.addItem(new KeyNamePair(Integer.valueOf(meses[i][0]),meses[i][1]));
		}
	}
	
	/**
	 * Se encarga de mostrar todas las tiendas
	 */
	void dynWarehouse(){		
		warehouseCombo.removeAllItems();
		warehouseLabel.setVisible(true);
		warehouseCombo.setVisible(true);
		
		String sqlWarehouse = "select M_Warehouse_ID, value || ' - ' || name " +
							  "from M_Warehouse " +
							  "order by value asc ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlWarehouse, null);
			rs = pstmt.executeQuery();

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
		/****Captura el evento para realizar otra accion****/
		warehouseCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {
		
			@Override
			public void contentsChanged(ListDataEvent e) {
				
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
	 * Se encarga de mostrar la lista que contiene los tipos 
	 * de transferencias de la compra/venta
	 */
	void dynTypeTransferSP(){
		typeTransferSPCombo.removeAllItems();
		typeTransferSPCombo.setVisible(true);
		typeTransferSPLabel.setVisible(true);
		transferSPHashmap = new HashMap<Integer, String>();
		
		String sqlTypeTransferSP = "select b.AD_Ref_List_ID, a.name, b.value " +
								   "from AD_Ref_List_Trl a, AD_Ref_list b " +
								   "where a.AD_Ref_List_ID = b.AD_Ref_List_ID " +
								   "and a.AD_Ref_List_ID in " +
									   "(select AD_Ref_List_ID " + 
									   "from AD_Ref_List " +
									   "where AD_Reference_ID = " + 
									   		Env.getCtx().getContextAsInt("#XX_L_TYPETRANSFERSP_ID") + ") " +
								   "order by a.name";		
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlTypeTransferSP, null);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				typeTransferSPCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
				transferSPHashmap.put(rs.getInt(1), rs.getString(3));
			}
			typeTransferSPCombo.addActionListener(this);
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlTypeTransferSP, e);
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
	 * Llena los combos del filtro
	 */
	private  void llenarCombos(){
		dynTypeTransfer();
		dynMonth();
	}
	
	/**
	 * Genera las columnas para la primera tabla
	 */
	private void dynInitFirst(){
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		ColumnInfo[] layoutF = new ColumnInfo[] {
			/****0-Check****/
			new ColumnInfo("", "", IDColumn.class, false, false, ""),				
			/****1-Sucursal****/
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OfficeBranch"), ".", String.class),				
			/****2-Número de Control****/
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ControlNumber"), ".", String.class),
			/****3-Descripcion****/
			new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), ".", String.class), 
			/****4-Fecha****/
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Date"), ".", String.class),
			/****5-Total Debe****/
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalShould"), ".", float.class),	
			/****6-Total Haber****/
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalHave"), ".", float.class)
		};
		xTableHead.prepareTable(layoutF, "", "", true, "");
		CompiereColor.setBackground (this);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		xTableHead.getSelectionModel().addListSelectionListener(this);		
		xTableHead.getColumnModel().getColumn(0).setMinWidth(10);
		xTableHead.getColumnModel().getColumn(1).setMinWidth(110);
		xTableHead.getColumnModel().getColumn(1).setCellRenderer(renderCenter);
		xTableHead.getColumnModel().getColumn(2).setMinWidth(120);
		xTableHead.getColumnModel().getColumn(2).setCellRenderer(renderCenter);		
		xTableHead.getColumnModel().getColumn(3).setMinWidth(250);
		xTableHead.getColumnModel().getColumn(3).setCellRenderer(renderCenter);		
		xTableHead.getColumnModel().getColumn(4).setMinWidth(90);
		xTableHead.getColumnModel().getColumn(4).setCellRenderer(renderCenter);
		xTableHead.getColumnModel().getColumn(5).setMinWidth(120);	
		xTableHead.getColumnModel().getColumn(5).setCellRenderer(renderRight);
		xTableHead.getColumnModel().getColumn(6).setMinWidth(120);	
		xTableHead.getColumnModel().getColumn(6).setCellRenderer(renderRight);
		bSearch.addActionListener(this);
		dynInitSecond();
			
		xTableHead.addMouseListener(new MouseListener() {
			
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
				
				if (xTableHead.getSelectedRow() != -1){
					IDColumn accoutingId = (IDColumn) xTableHead.getValueAt(xTableHead.getSelectedRow(),0);
					difTotal.setText(formato.format(headHashmap.get(accoutingId.getRecord_ID())));
					difTotal.setVisible(true);
					difTotalLabel.setVisible(true);						
					difTotalLabel.setFont(new Font("Serif", Font.BOLD, 15));		
					difTotal.setFont(new Font("Serif", Font.BOLD, 15));
					//difTotal.setBackground(Color.red);
					
					if (typeTransfer.equals(X_Ref_XX_ListTransferCX017.ACCOUNTS_PAYABLE_APPROVED.getValue())){
						//Cuentas por Pagar Aprobadas
						if (accoutingId.isSelected() == true){
							transferHashmap.put(accoutingId.getRecord_ID(), true);
							datelabel.setVisible(true);
							date.setVisible(true);
							bTransfer.setVisible(true);
						}
						constructDetailCxPA(accoutingId.getRecord_ID());
					}else if (typeTransfer.equals(X_Ref_XX_ListTransferCX017.ACCOUNTS_PAYABLE_ESTIMATED.getValue())){
						if (accoutingId.isSelected() == true){
							transferHashmap.put(accoutingId.getRecord_ID(), true);
							datelabel.setVisible(true);
							date.setVisible(true);
							bTransfer.setVisible(true);
						}
						constructDetailCxPA(accoutingId.getRecord_ID());
					}else if (typeTransfer.equals(X_Ref_XX_ListTransferCX017.PURCHASE_SALE.getValue())){
						//Compra/Venta
						if (accoutingId.isSelected() == true){
							transferHashmap.put(accoutingId.getRecord_ID(), true);
							datelabel.setVisible(true);
							date.setVisible(true);
							bTransfer.setVisible(true);
						}
						constructDetailCV(accoutingId.getRecord_ID());
					}else if(typeTransfer.equals(X_Ref_XX_ListTransferCX017.CASH_BOOK.getValue())){
						//Diario de Caja
						if (accoutingId.isSelected() == true){
							transferHashmap.put(accoutingId.getRecord_ID(), true);
							datelabel.setVisible(true);
							date.setVisible(true);
							bTransfer.setVisible(true);
						}
						constructDetailDC(accoutingId.getRecord_ID());
					}else if(typeTransfer.equals(X_Ref_XX_ListTransferCX017.BANK.getValue())){
						//Banco
						if (accoutingId.isSelected() == true){
							transferHashmap.put(accoutingId.getRecord_ID(), true);
							datelabel.setVisible(true);
							date.setVisible(true);
							bTransfer.setVisible(true);
						}
						constructDetailBank(accoutingId.getRecord_ID());
					}
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
				/****0-Línea****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Line"), ".", int.class),	
				/****1-Cuenta Contable****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ElementAccount"), ".", KeyNamePair.class),				
				/****2-Oficina****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OfficeBranch"), ".", String.class),	
				/****3-Division****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Division"), ".", String.class),
				/****4-Departamento****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Department"), ".", KeyNamePair.class),
				/****5-Seccion****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "XX_Section"), ".", String.class),
				/****6-Auxiliar****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Auxiliary"), ".", String.class),
				/****7-Tipo de Documento****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_DocType_ID"), ".", String.class),
				/****8-Número de Documento****/
				new ColumnInfo(Msg.getMsg(Env.getCtx(), "DocumentNo"), ".", String.class),
				/****9-Fecha Documendo****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DocumentDate"), ".", String.class),
				/****10-Descripción****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), ".", String.class),
				/****11-Debe****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Should"), ".", float.class),
				/****12-Haber****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Have"), ".", float.class),
		};
		xTableDetail.prepareTable(layoutS, "", "", true, "");
		CompiereColor.setBackground (this);
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		xTableDetail.getSelectionModel().addListSelectionListener(this);		
		xTableDetail.getColumnModel().getColumn(0).setMinWidth(35);
		xTableDetail.getColumnModel().getColumn(0).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(1).setMinWidth(100);
		xTableDetail.getColumnModel().getColumn(1).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(2).setMinWidth(110);
		xTableDetail.getColumnModel().getColumn(2).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(3).setMinWidth(80);
		xTableDetail.getColumnModel().getColumn(3).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(4).setMinWidth(90);
		xTableDetail.getColumnModel().getColumn(4).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(5).setMinWidth(80);
		xTableDetail.getColumnModel().getColumn(5).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(6).setMinWidth(80);
		xTableDetail.getColumnModel().getColumn(6).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(7).setMinWidth(120);
		xTableDetail.getColumnModel().getColumn(7).setCellRenderer(renderRight);
		xTableDetail.getColumnModel().getColumn(8).setMinWidth(140);
		xTableDetail.getColumnModel().getColumn(8).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(9).setMinWidth(140);
		xTableDetail.getColumnModel().getColumn(9).setCellRenderer(renderCenter);
		xTableDetail.getColumnModel().getColumn(10).setMinWidth(250);
		xTableDetail.getColumnModel().getColumn(10).setCellRenderer(renderLeft);
		xTableDetail.getColumnModel().getColumn(11).setMinWidth(100);
		xTableDetail.getColumnModel().getColumn(11).setCellRenderer(renderRight);
		xTableDetail.getColumnModel().getColumn(12).setMinWidth(100);
		xTableDetail.getColumnModel().getColumn(12).setCellRenderer(renderRight);
	}
	
	private void constructHeadCxPA(){
		String sql = "";

		m_sql = new StringBuffer();
		m_sql.append("select ace.XX_Office codsuc, ace.XX_ControlNumber nrocom, " +
					 "ace.Description descom, to_char(ace.DateTrx,'dd/MM/yyyy') dateT, " +
					 "ace.XX_TotalShould debcom, ace.XX_TotalHave habcom, " +
					 "(ace.XX_TotalShould - ace.XX_TotalHave) difcom, " +
					 "ace.XX_VCN_AccoutingEntry_ID id " +
					 "from XX_VCN_AccoutingEntry ace " +
					 "where ace.IsTransferred = 'N' " +
					 "and to_char(ace.DateFrom,'MM/yyyy') = '" + month + "/" + year + "' " +
					 "and ace.XX_ProcessType = '" + typeAccounting + "' " +
					 "and ace.XX_ListCX017 = '" + typeTransfer + "' " +
					 "order by ace.DateFrom asc ");
		
		sql = m_sql.toString();
		tableInitHeadCxPA(sql);
	}
	
	private void constructHeadCxPE(){
		String sql = "";

		m_sql = new StringBuffer();
		m_sql.append("select ace.XX_Office codsuc, ace.XX_ControlNumber nrocom, " +
					 "ace.Description descom, to_char(ace.DateTrx,'dd/MM/yyyy') dateT, " +
					 "ace.XX_TotalShould debcom, ace.XX_TotalHave habcom, " +
					 "(ace.XX_TotalShould - ace.XX_TotalHave) difcom, " +
					 "ace.XX_VCN_AccoutingEntry_ID id " +
					 "from XX_VCN_AccoutingEntry ace " +
					 "where ace.IsTransferred = 'N' " +
					 "and to_char(ace.DateFrom,'MM/yyyy') = '" + month + "/" + year + "' " +
					 //"and ace.XX_ProcessType = '" + typeAccounting + "' " +
					 "and ace.XX_ListCX017 = '" + typeTransfer + "' " +
					 "order by ace.DateFrom asc ");
		
		sql = m_sql.toString();
		tableInitHeadCxPA(sql);
	}
	
	/**
	 * Genera la cabecera de una fila de compra/venta.
	 */	
	private void constructHeadCV(){
		String sql = "";

		m_sql = new StringBuffer();
		m_sql.append("select ace.XX_Office codsuc, ace.XX_ControlNumber nrocom, " +
					 "ace.Description descom, to_char(ace.DateTrx,'dd/MM/yyyy') dateT, " +
					 "ace.XX_TotalShould debcom, ace.XX_TotalHave habcom, " +
					 "(ace.XX_TotalShould - ace.XX_TotalHave) difcom, " +
					 "ace.XX_VCN_AccoutingEntry_ID id " +
					 "from XX_VCN_AccoutingEntry ace " +
					 "where ace.IsTransferred = 'N' " +
					 "and to_char(ace.DateFrom,'MM/yyyy') = '" + month + "/" + year + "' " +
					 "and M_Warehouse_id = " + ((KeyNamePair)warehouseCombo.getItemAt(warehouseCombo.getSelectedIndex())).getKey() + " " +
					 "and XX_TYPETRANSFERSP = '" + transferSPHashmap.get(((KeyNamePair)typeTransferSPCombo.getItemAt(typeTransferSPCombo.getSelectedIndex())).getKey()) + "' " +
					 "and ace.XX_ListCX017 = '" + typeTransfer + "' " +
					 "order by ace.DateFrom asc ");
		
		sql = m_sql.toString();
		tableInitHeadCV(sql);
	}
	
	/**
	 * Genera la cabecera de una fila de Diario de Caja.
	 */	
	private void constructHeadDC(){
		String sql = "";

		m_sql = new StringBuffer();
		m_sql.append("select ace.XX_Office codsuc, ace.XX_ControlNumber nrocom, " +
					 "ace.Description descom, to_char(ace.DateTrx,'dd/MM/yyyy') dateT, " +
					 "ace.XX_TotalShould debcom, ace.XX_TotalHave habcom, " +
					 "(ace.XX_TotalShould - ace.XX_TotalHave) difcom, " +
					 "ace.XX_VCN_AccoutingEntry_ID id " +
					 "from XX_VCN_AccoutingEntry ace " +
					 "where ace.IsTransferred = 'N' " +
					 "and to_char(ace.DateFrom,'MM/yyyy') = '" + month + "/" + year + "' " +
					 "and M_Warehouse_id = " + ((KeyNamePair)warehouseCombo.getItemAt(warehouseCombo.getSelectedIndex())).getKey() + " " +
					 "and ace.XX_ListCX017 = '" + typeTransfer + "' " +
					 "order by ace.DateFrom asc ");
		
		sql = m_sql.toString();
		tableInitHeadCV(sql);
	}
	
	/**
	 * Genera la cabecera de una fila de Banco
	 */	
	private void constructHeadBank(){
		String sql = "";

		m_sql = new StringBuffer();
		m_sql.append("select ace.XX_Office codsuc, ace.XX_ControlNumber nrocom, " +
					 "ace.Description descom, to_char(ace.DateTrx,'dd/MM/yyyy') dateT, " +
					 "ace.XX_TotalShould debcom, ace.XX_TotalHave habcom, " +
					 "(ace.XX_TotalShould - ace.XX_TotalHave) difcom, " +
					 "ace.XX_VCN_AccoutingEntry_ID id " +
					 "from XX_VCN_AccoutingEntry ace " +
					 "where ace.IsTransferred = 'N' " +
					 "and to_char(ace.DateFrom,'MM/yyyy') = '" + month + "/" + year + "' " +
					 "and ace.XX_ListCX017 = '" + typeTransfer + "' " +
					 "order by ace.DateFrom asc ");
		
		sql = m_sql.toString();
		tableInitHeadBank(sql);
	}
	
	
	/**
	 * Se encarga de llenar la primera tabla (asientos contables), 
	 * para el tipo de transferencia de cuentas por pagar aprobadas
	 */
	private void tableInitHeadCxPA(String sql){
		
		int i = 0;
		xTableHead.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {				
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			headHashmap = new HashMap<Integer, BigDecimal>();
			transferHashmap = new HashMap<Integer, Boolean>();
			while(rs.next()) {	
				xTableHead.setRowCount (i+1);		
				/****Opcion Check****/
				IDColumn id = new IDColumn(rs.getInt("id"));
				xTableHead.setValueAt(id, i, 0); //(valor,fila,columna)
				/****Sucursal****/
				xTableHead.setValueAt(rs.getString("codsuc"), i, 1);	
				/****Nro. Control****/
				xTableHead.setValueAt(rs.getString("nrocom"), i, 2);
				/****Descripción****/
				xTableHead.setValueAt(rs.getString("descom"), i, 3);
				/****Fecha****/
				xTableHead.setValueAt(rs.getString("dateT"), i, 4);
				/****Total Debe****/
				xTableHead.setValueAt(myFormatter.format(rs.getBigDecimal("debcom")), i, 5);
				/****Total Haber****/
				xTableHead.setValueAt(myFormatter.format(rs.getBigDecimal("habcom")), i, 6);
				
				headHashmap.put(rs.getInt("id"), rs.getBigDecimal("difcom"));
				transferHashmap.put(rs.getInt("id"), false);				
				i++;
			}		
		}
		catch(SQLException e){	
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
	 * Se encarga de llenar la primera tabla (asientos contables), 
	 * para el tipo de transferencia de compra/venta
	 */
	private void tableInitHeadCV(String sql){
		
		int i = 0;
		xTableHead.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {				
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			headHashmap = new HashMap<Integer, BigDecimal>();
			transferHashmap = new HashMap<Integer, Boolean>();
			while(rs.next()) {	
				xTableHead.setRowCount (i+1);		
				/****Opcion Check****/
				IDColumn id = new IDColumn(rs.getInt("id"));
				xTableHead.setValueAt(id, i, 0); //(valor,fila,columna)
				/****Sucursal****/
				xTableHead.setValueAt(rs.getString("codsuc"), i, 1);	
				/****Nro. Control****/
				xTableHead.setValueAt(rs.getString("nrocom"), i, 2);
				/****Descripción****/
				xTableHead.setValueAt(rs.getString("descom"), i, 3);
				/****Fecha****/
				xTableHead.setValueAt(rs.getString("dateT"), i, 4);
				/****Total Debe****/
				xTableHead.setValueAt(myFormatter.format(rs.getBigDecimal("debcom")), i, 5);
				/****Total Haber****/
				xTableHead.setValueAt(myFormatter.format(rs.getBigDecimal("habcom")), i, 6);
				
				headHashmap.put(rs.getInt("id"), rs.getBigDecimal("difcom"));
				transferHashmap.put(rs.getInt("id"), false);				
				i++;
			}				
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
	 * Se encarga de llenar la primera tabla (asientos contables), 
	 * para el tipo de transferencia de compra/venta
	 */
	private void tableInitHeadBank(String sql){
		
		int i = 0;
		xTableHead.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {				
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			headHashmap = new HashMap<Integer, BigDecimal>();
			transferHashmap = new HashMap<Integer, Boolean>();
			while(rs.next()) {	
				xTableHead.setRowCount (i+1);		
				/****Opcion Check****/
				IDColumn id = new IDColumn(rs.getInt("id"));
				xTableHead.setValueAt(id, i, 0); //(valor,fila,columna)
				/****Sucursal****/
				xTableHead.setValueAt(rs.getString("codsuc"), i, 1);	
				/****Nro. Control****/
				xTableHead.setValueAt(rs.getString("nrocom"), i, 2);
				/****Descripción****/
				xTableHead.setValueAt(rs.getString("descom"), i, 3);
				/****Fecha****/
				xTableHead.setValueAt(rs.getString("dateT"), i, 4);
				/****Total Debe****/
				xTableHead.setValueAt(myFormatter.format(rs.getBigDecimal("debcom")), i, 5);
				/****Total Haber****/
				xTableHead.setValueAt(myFormatter.format(rs.getBigDecimal("habcom")), i, 6);
				
				headHashmap.put(rs.getInt("id"), rs.getBigDecimal("difcom"));
				transferHashmap.put(rs.getInt("id"), false);				
				i++;
			}				
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

	
	private void constructDetailCxPA(int idAccoutingEntry){
		String sql = "";
			
		m_sql = new StringBuffer();
		m_sql.append("select dae.XX_VCN_Line as line, ele.value as codcta, ele.C_ElementValue_ID, " +
					 "dae.XX_Office as codsuc, dae.XX_Division as coddiv, dae.XX_Departament as coddep, " +
					 "dae.XX_SectionCode as codsec, dae.XX_Aux as numaux, dae.DocumentNo as nrodoc, " +
					 "dae.XX_DocumentType as tipdoc, to_char(dae.XX_DocumentDate,'dd/MM/yyyy') as fecdoc, " +
					 "to_char(dae.XX_DueDate,'dd/MM/yyyy') as fecven, " +
					 "dae.Description as desasi, dae.AmtAcctDr as debe, dae.AmtAcctCr as haber, " +
					 "dae.Fact_Acct_ID as id  " +
					 "from Fact_Acct dae " +
					 "inner join C_ElementValue ele on (dae.Account_ID = ele.C_ElementValue_ID) " +
					 "where dae.XX_VCN_AccoutingEntry_ID = " + idAccoutingEntry + " " +
					 "order by dae.XX_VCN_Line asc ");
		
		sql = m_sql.toString();
		tableInitDetailCxPA(sql);
	}
	
	private void constructDetailCV(int idAccoutingEntry){
		String sql = "";
			
		m_sql = new StringBuffer();
		m_sql.append("select dae.XX_VCN_Line as line, ele.value as codcta, ele.C_ElementValue_ID, " +
				 "dae.XX_Office as codsuc, dae.XX_Division as coddiv, dae.XX_Departament as coddep, " +
				 "dae.XX_SectionCode as codsec, dae.XX_Aux as numaux, dae.DocumentNo as nrodoc, " +
				 "dae.XX_DocumentType as tipdoc, to_char(dae.XX_DocumentDate,'dd/MM/yyyy') as fecdoc, " +
				 "to_char(dae.XX_DueDate,'dd/MM/yyyy') as fecven, " +
				 "dae.Description as desasi, dae.AmtAcctDr as debe, dae.AmtAcctCr as haber, " +
				 "dae.Fact_Acct_ID as id " +
				 "from Fact_Acct dae " +
				 "inner join C_ElementValue ele on (dae.Account_ID = ele.C_ElementValue_ID) " +
				 "where dae.XX_VCN_AccoutingEntry_ID = " + idAccoutingEntry + " " +
				 "order by dae.XX_VCN_Line asc ");
		
		sql = m_sql.toString();
		tableInitDetailCV(sql);
	}
	
	/*Detalle del Diario de Caja*/
	private void constructDetailDC(int idAccoutingEntry){
		String sql = "";
			
		m_sql = new StringBuffer();
		m_sql.append("select dae.XX_VCN_Line as line, ele.value as codcta, ele.C_ElementValue_ID, " +
				 "dae.XX_Office as codsuc, dae.XX_Division as coddiv, dae.XX_Departament as coddep, " +
				 "dae.XX_SectionCode as codsec, dae.XX_Aux as numaux, dae.DocumentNo as nrodoc, " +
				 "dae.XX_DocumentType as tipdoc, to_char(dae.XX_DocumentDate,'dd/MM/yyyy') as fecdoc, " +
				 "to_char(dae.XX_DueDate,'dd/MM/yyyy') as fecven, " +
				 "dae.Description as desasi, dae.AmtAcctDr as haber, dae.AmtAcctCr as debe" +
				 "dae.Fact_Acct_ID as id " +
				 "from Fact_Acct dae " +
				 "inner join C_ElementValue ele on (dae.Account_ID = ele.C_ElementValue_ID) " +
				 "where dae.XX_VCN_AccoutingEntry_ID = " + idAccoutingEntry + " " +
				 "order by dae.XX_VCN_Line asc ");
		
		sql = m_sql.toString();
		tableInitDetailCV(sql);
	}

	/*Detalle de Banco*/
	private void constructDetailBank(int idAccoutingEntry){
		String sql = "";
			
		m_sql = new StringBuffer();
		m_sql.append("select dae.XX_VCN_Line as line, ele.value as codcta, ele.C_ElementValue_ID, " +
				 "dae.XX_Office as codsuc, dae.XX_Division as coddiv, dae.XX_Departament as coddep, " +
				 "dae.XX_SectionCode as codsec, dae.XX_Aux as numaux, dae.DocumentNo as nrodoc, " +
				 "dae.XX_DocumentType as tipdoc, to_char(dae.XX_DocumentDate,'dd/MM/yyyy') as fecdoc, " +
				 "to_char(dae.XX_DueDate,'dd/MM/yyyy') as fecven, " +
				 "dae.Description as desasi, dae.AmtAcctDr as debe, dae.AmtAcctCr as haber, " +
				 "dae.Fact_Acct_ID as id " +
				 "from Fact_Acct dae " +
				 "inner join C_ElementValue ele on (dae.Account_ID = ele.C_ElementValue_ID) " +
				 "where dae.XX_VCN_AccoutingEntry_ID = " + idAccoutingEntry + " " +
				 "order by dae.XX_VCN_Line asc ");
		
		sql = m_sql.toString();
		tableInitDetailCV(sql);
	}

	
	/**
	 * Se encarga de llenar la segunda tabla (detalle de los asientos contables), 
	 * para el tipo de transferencia de cuentas por pagar aprobadas
	 */
	private void tableInitDetailCxPA(String sql){
		
		int i = 0;
		xTableDetail.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {				
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			while(rs.next()) {	
				xTableDetail.setRowCount (i+1);		
				/****Línea****/
				xTableDetail.setValueAt(rs.getInt("line"), i, 0); //(valor,fila,columna)
				/****Cuenta Contable****/
				xTableDetail.setValueAt(new KeyNamePair (rs.getInt("C_ElementValue_ID"), rs.getString("codcta")), i, 1);
				/****Sucursal****/
				xTableDetail.setValueAt(rs.getString("codsuc"), i, 2);	
				/****División****/
				xTableDetail.setValueAt(rs.getString("coddiv"), i, 3);
				/****Departamento****/
				xTableDetail.setValueAt(rs.getString("coddep"), i, 4);
				/****Sección****/
				xTableDetail.setValueAt(rs.getString("codsec"), i, 5);
				/****Nro. Auxiliar****/
				xTableDetail.setValueAt(rs.getString("numaux"), i, 6);
				/****Tipo de Documento****/
				xTableDetail.setValueAt(rs.getString("tipdoc"), i, 7);
				/****Nro. Documento****/
				xTableDetail.setValueAt(rs.getString("nrodoc"), i, 8);
				/****Fecha de Documento****/
				xTableDetail.setValueAt(rs.getString("fecdoc"), i, 9);
				/****Descripción****/
				xTableDetail.setValueAt(rs.getString("desasi"), i, 10);
				
				/****Monto Debe****/
				if(rs.getBigDecimal("debe").compareTo(BigDecimal.ZERO)==0)
					xTableDetail.setValueAt(null, i, 11);
				else
					xTableDetail.setValueAt(myFormatter.format(rs.getBigDecimal("debe")), i, 11);
				
				/****Monto Haber****/
				if(rs.getBigDecimal("haber").compareTo(BigDecimal.ZERO)==0)
					xTableDetail.setValueAt(null, i, 12);	
				else
					xTableDetail.setValueAt(myFormatter.format(rs.getBigDecimal("haber")), i, 12);	
				
				i++;
			}		
		}
		catch(SQLException e){	
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
	 * Se encarga de llenar la segunda tabla (detalle de los asientos contables), 
	 * para el tipo de transferencia de compra/venta
	 */
	private void tableInitDetailCV(String sql){		
	
	//	DecimalFormat myFormatter = new DecimalFormat("###.00");
		int i = 0;
		xTableDetail.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {				
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			while(rs.next()) {	
				xTableDetail.setRowCount (i+1);		
				/****Línea****/
				xTableDetail.setValueAt(rs.getInt("line"), i, 0); //(valor,fila,columna)
				/****Cuenta Contable****/
				xTableDetail.setValueAt(new KeyNamePair (rs.getInt("C_ElementValue_ID"), rs.getString("codcta")), i, 1);
				/****Sucursal****/
				xTableDetail.setValueAt(rs.getString("codsuc"), i, 2);	
				/****División****/
				xTableDetail.setValueAt(rs.getString("coddiv"), i, 3);
				/****Departamento****/
				xTableDetail.setValueAt(rs.getString("coddep"), i, 4);
				/****Sección****/
				xTableDetail.setValueAt(rs.getString("codsec"), i, 5);
				/****Nro. Auxiliar****/
				xTableDetail.setValueAt(rs.getString("numaux"), i, 6);
				/****Nro. Documento****/
				xTableDetail.setValueAt(rs.getString("tipdoc"), i, 7);
				/****Tipo de Documento****/
				xTableDetail.setValueAt(rs.getString("nrodoc"), i, 8);
				/****Fecha de Documento****/
				xTableDetail.setValueAt(rs.getString("fecdoc"), i, 9);
				/****Descripción****/
				xTableDetail.setValueAt(rs.getString("desasi"), i, 10);
				/****Monto Debe****/
				if(rs.getBigDecimal("debe").compareTo(BigDecimal.ZERO)==0)
					xTableDetail.setValueAt(null, i, 11);
				else
					xTableDetail.setValueAt(myFormatter.format(rs.getBigDecimal("debe")), i, 11);
				/****Monto Haber****/
				if(rs.getBigDecimal("haber").compareTo(BigDecimal.ZERO)==0)
					xTableDetail.setValueAt(null, i, 12);	
				else
					xTableDetail.setValueAt(myFormatter.format(rs.getBigDecimal("haber")), i, 12);				
				i++;
			}		
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
	 * Se encarga de validar y almacenar los valores obtenidos en el filtro 
	 */
	private void cmdSearch(){
		Calendar cal = Calendar.getInstance();
		String var = "";

		/****Buscar el mes y el año****/
		if ((monthCombo.getSelectedItem() != null) && (monthCombo.getSelectedIndex() != 0)){
			month = String.valueOf(monthCombo.getSelectedIndex());
			if (month.length() == 1)
				month = "0" + month;
		}
		if (yearField.getValue() != null){
			/****Valida que el año corresponda al actual o como mínimo al año pasado****/
			year = (String) yearField.getValue();
			int longYear = String.valueOf(year).length();
			Integer yearSys = cal.get(Calendar.YEAR);
			int longYearSys = String.valueOf(yearSys).length();			
			Integer aux = new Integer(1);

			if (longYear == longYearSys){
				if (!year.equals(String.valueOf(yearSys))){
					aux = Integer.valueOf(yearSys) - aux;
					if (!year.equals(String.valueOf(aux))){
						yearField.setValue(yearSys);
						System.out.println("El año ingresado es incorrecto. Fue corregido");
						ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), ""));
					}else
						yearField.setValue(year);
				}
			}else{
				yearField.setValue(yearSys);
				System.out.println("El año ingresado es incorrecto. Fue corregido");
				ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), ""));
			}
		}
		
		/****Buscar el tipo de transferencia****/
		if ((typeTransferCombo.getSelectedItem() != null) && (typeTransferCombo.getSelectedIndex() != 0)){
			var = ((KeyNamePair)typeTransferCombo.getSelectedItem()).getName();
			if (var.equals("Cuentas por Pagar Aprobadas"))
				typeTransfer = X_Ref_XX_ListTransferCX017.ACCOUNTS_PAYABLE_APPROVED.getValue();
			else if (var.equals("Cuentas por Pagar Estimadas"))
				typeTransfer = X_Ref_XX_ListTransferCX017.ACCOUNTS_PAYABLE_ESTIMATED.getValue();
			else if (var.equals("Compra/Venta"))
				typeTransfer = X_Ref_XX_ListTransferCX017.PURCHASE_SALE.getValue();
			else if (var.equals("Diario de Caja"))
				typeTransfer = X_Ref_XX_ListTransferCX017.CASH_BOOK.getValue();
			else if (var.equals("Banco"))
				typeTransfer = X_Ref_XX_ListTransferCX017.BANK.getValue();
		}
		
		/****Buscar el tipo de asiento****/
		if ((typeAccountEntryCombo.getSelectedItem() != null) && (typeAccountEntryCombo.getSelectedIndex() != 0)){
			if(((KeyNamePair)typeAccountEntryCombo.getSelectedItem()).getKey() 
					== Env.getCtx().getContextAsInt("#XX_L_PROCESSITEM_ID"))
				typeAccounting = "I";
			else if(((KeyNamePair)typeAccountEntryCombo.getSelectedItem()).getKey() 
					== Env.getCtx().getContextAsInt("#XX_L_PROCESSASSETSERV_ID"))
				typeAccounting = "A";
		}
		
		/****Buscar el tipo de transferencia de la compra/venta****/
		if ((typeTransferSPCombo.getSelectedItem() != null) && (typeTransferSPCombo.getSelectedIndex() != 0)){
			idTypeTransferSP = ((KeyNamePair)typeTransferSPCombo.getSelectedItem()).getKey();			
		}
		
		/****Buscar el tipo de tienda****/
		if ((warehouseCombo.getSelectedItem() != null) && (warehouseCombo.getSelectedIndex() != 0)){
			idWarehouse = ((KeyNamePair)warehouseCombo.getSelectedItem()).getKey();
		}
				
		if (varTypeTransfer == 1){
			//Cuentas por Pagar Aprobadas
			xTableDetail.setRowCount(0);
			constructHeadCxPA();
		}else if (varTypeTransfer == 2){
			//Cuentas por Pagar Estimadas
			xTableDetail.setRowCount(0);
			constructHeadCxPE();
		}else if (varTypeTransfer == 3){
			//Compra/Venta			
			xTableDetail.setRowCount(0);
			constructHeadCV();
		}else if (varTypeTransfer == 4){
			//Diario de Caja
			constructHeadDC();
		}else if (varTypeTransfer == 5){
			//Banco
			constructHeadBank();
		}else if (varTypeTransfer == -1){
			System.out.println("Debe seleccionar un tipo de transferencia");
			ADialog.error(0, new Container(), Msg.getMsg(Env.getCtx(), ""));
		}			
	}
	
	/**
	 * Llama al proceso que se encarga de la transferencia de información a la interfaz de contabilidad CX017
	 */
	private void cmdTransfer(){
		Iterator<Integer> iterator = headHashmap.keySet().iterator();       
        Timestamp dateTransfer = (Timestamp) date.getValue();
        
        if (dateTransfer != null){
        	while(iterator.hasNext()){  
            	int id = iterator.next();
            	if (transferHashmap.get(id)){
        	
        			X_XX_VCN_AccoutingEntry accoutingEntry = new X_XX_VCN_AccoutingEntry(Env.getCtx(), id, null);
	        		accoutingEntry.setDateTrx(dateTransfer);
	        		accoutingEntry.save();
	        		
            		try{
        				MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_INTERFACEACCOUNT_ID"), 0); 
        				mpi.save();
        				ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_INTERFACEACCOUNT_ID")); 
        				pi.setRecord_ID(mpi.getRecord_ID());
        				pi.setAD_PInstance_ID(mpi.get_ID());
        				pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_INTERFACEACCOUNT_ID")); 
        				pi.setClassName(""); 
        				pi.setTitle(""); 
        				ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
        				list.add(new ProcessInfoParameter("XX_VCN_AccoutingEntry_ID", 
        						String.valueOf(id), null, String.valueOf(id), null));
        				list.add(new ProcessInfoParameter("XX_TypeTransfer",
        						String.valueOf(varTypeTransfer), null, String.valueOf(varTypeTransfer), null));
        				ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
        				list.toArray(pars);
        				pi.setParameter(pars);
        				ProcessCtl pc = new ProcessCtl(null ,pi,null); 
        				pc.start();
        			}catch(Exception e){
        				log.log(Level.SEVERE,e.getMessage()); 
        			}
            	}
        	}       	
        }else{
        	ADialog.error(1, new Container(), Msg.getMsg(Env.getCtx(), "Debe ingresar la fecha de transferencia"));
        }  
	}
}
