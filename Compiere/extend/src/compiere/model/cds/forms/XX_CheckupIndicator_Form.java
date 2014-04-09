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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.forms.indicator.XX_Indicator;


public class XX_CheckupIndicator_Form extends CPanel 
implements FormPanel, ActionListener,  ListSelectionListener{

	/**
	 *  @author Gabrielle Huchet
	 *  @version 
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */


	/**	Window No	*/
	private int m_WindowNo = 0;
	/**	FormFrame	*/
	private FormFrame m_frame;
	/**	Logger		*/
	static CLogger log = CLogger.getCLogger(XX_CheckupIndicator_Form.class);

	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();

	private Integer NO_TEXTIL = Env.getCtx().getContextAsInt("#XX_L_PC_NOTEXTIL_ID");
	private Integer TEXTIL = Env.getCtx().getContextAsInt("#XX_L_PC_TEXTIL_ID");
	private Double JORNADA = 0.0;

	static String	   m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	/* Panel de la ventana */
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("Indicador de Chequeo");
	private CPanel xPanel = new CPanel();
	
	/* Layouts */
	private BorderLayout mainLayout = new BorderLayout();
	private GridBagLayout northLayout = new GridBagLayout();
	private FlowLayout southLayout = new FlowLayout();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	

	/* Botones */
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "Search"));
	private CButton bClear = new CButton(Msg.translate(Env.getCtx(), "Clear"));
	private CButton bPrint = new CButton(Msg.translate(Env.getCtx(), "Print"));

	/* Archivo a exportar*/
	private VFile bFile = new VFile("File", Msg.getMsg(Env.getCtx(), "File"),true, false, true, true, JFileChooser.SAVE_DIALOG);
	

	private CLabel labelFile = new CLabel();
	
	/* Campos del filtro disponibles */
	private CLabel assistantLabel = new CLabel();
	private CComboBox assistantCombo = new CComboBox();
	private CCheckBox allCheckAssistants = new CCheckBox();
	private CLabel dateFromLabel = new CLabel();
	private CLabel dateToLabel = new CLabel();
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));
	private Date to;	
	private Date from;

	/** La tabla donde se guardarán los datos*/
	private MiniTablePreparator xTable = new MiniTablePreparator();

	
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;

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
	}	//	init
	/**
	 */
	private void jbInit() throws Exception
	{
		
		dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		dateToLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		assistantLabel.setText(Msg.translate(Env.getCtx(), "XX_CheckAssistant"));
		
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);

		bSearch.setEnabled(true);
		bClear.setEnabled(true);
		
		labelFile.setText(Msg.getMsg(Env.getCtx(), "File"));
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(900, 445));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);

		xScrollPane.getViewport().add(xTable, null);
		
		northPanel.add(assistantLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(assistantCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(allCheckAssistants, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(dateFromLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(dateFrom, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(dateToLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
			    ,GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(12, 12, 10, 5), 0, 0));
		northPanel.add(dateTo, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 12, 10, 5), 0, 0));
		
		southLayout.setAlignment(FlowLayout.RIGHT);
		southLayout.setHgap(15);
		southPanel.add(bSearch, null);
		southPanel.add(bClear, null);
		southPanel.add(labelFile, null);
		southPanel.add(bFile, null);
		southPanel.add(bPrint, null);
	    southPanel.validate();
	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		m_frame.getRootPane().setDefaultButton(bSearch);
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_CheckAssistant_ID"),".", String.class),	     //  0 Asistente de Chequeo
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_PurchaseOrdersChecked"),".", Integer.class),            //  1 O/C Chequeadas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_OrdersLabels"),".", Integer.class),        //  2 Pedidos Etiquetados
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_ReturnedItems"),".", Integer.class),	         //  3 Items Devueltos
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_ReturnedPieces"),".", Integer.class),          //  4 Piezas Devueltas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_CheckedTextilePieces"),".", Integer.class),   //  5 Piezas Textil Chequeadas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_CheckedNonTextilePieces"),".", Integer.class),//  6 Piezas No Textil Chequeadas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_CheckedPieces"),".", Integer.class),	         //  7 Piezas Chequeadas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_LabeledPieces"),".", Integer.class),	         //  8 Piezas Etiquetadas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_ConsolidatedPieces"),".", Integer.class),	         //  9 Consolidado de piezas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_CheckRates"),".", Integer.class),	         //  10 Tasa de Chequeo
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_ReceivedPieces"),".", Integer.class),	         //  11 Piezas Recibidas
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_GoalMonth"),".", String.class),                  //  12 Meta Mes

		};
		
		JORNADA = Double.valueOf(Env.getCtx().get_ValueAsString("#XX_L_WORKINGDAY"));
		
		xTable.prepareTable(layout, "", "", false, "");
		xTable.autoSize();// AutoResizeMode(4);
		
		
		Calendar calendarActual = Calendar.getInstance();
		to = calendarActual.getTime();
		calendarActual.set(Calendar.DATE, 01);
		from = (calendarActual).getTime();
		
		//  Visual
		CompiereColor.setBackground (this);
		
		//  Listener
		xTable.getSelectionModel().addListSelectionListener(this);	
		addActionListeners ();
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_CIListedQty"));
		statusBar.setStatusDB(xTable.getRowCount());
		
		// Agregar las acciones
		dynCheckAssistant();
		assistantCombo.setSelectedIndex(0);
		dateFrom.setValue(from);
		dateTo.setValue(to);
	
	}   //  dynInit


	/** Activa todos los listeners creados */
	protected final void addActionListeners () {
		
		bSearch.addActionListener(this);
		bClear.addActionListener(this);
		allCheckAssistants.addActionListener(this);
		bPrint.addActionListener(this);
		dateFrom.addActionListener(this);
		dateTo.addActionListener(this);
		assistantCombo.addActionListener(this);
		
		
	} 
	
	/** Deshabilitar Action Listeners */
	protected final void removeActionListeners () {
		
		bSearch.removeActionListener(this);
		bClear.removeActionListener(this);
		allCheckAssistants.removeActionListener(this);
		bPrint.removeActionListener(this);
		dateFrom.removeActionListener(this);
		dateTo.removeActionListener(this);
		assistantCombo.removeActionListener(this);

	}	
	void disable_CheckAssistant() {

		assistantCombo.setEnabled(false);
		assistantCombo.setEditable(false);
	}
	
	void dynCheckAssistant() {
		
		if(allCheckAssistants.isSelected()){
			disable_CheckAssistant();
			assistantCombo.setSelectedIndex(1);
		}else{
			assistantCombo.removeAllItems();
			String sql = "SELECT C_BPARTNER_ID, NAME " +
			"FROM C_BPARTNER WHERE isActive='Y' "+
			"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_CHASSI_ID");
	
			//#XX_L_JOBPOSITION_CHASSI_ID = 1000091
			
			sql += " ORDER BY NAME ";
			PreparedStatement pstmt =null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				assistantCombo.addItem(new KeyNamePair(-1, null));
				assistantCombo.addItem(new KeyNamePair(0,Msg.translate(Env.getCtx(), "XX_AllCheckAssistants")));
				while (rs.next()) {
					assistantCombo.addItem(new KeyNamePair(rs.getInt(1), rs
							.getString(2)));
				}
				

				assistantCombo.setEnabled(true);
				assistantCombo.setEditable(true);
		
			} catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
			}finally {
				try {
					rs.close();
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}	//	dispose

	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		removeActionListeners ();
		
		if (e.getSource() == bSearch){
			//Verificar que los campos periodo se hayan llenado
			if (dateFrom.getValue() == null || dateTo.getValue() == null) {				
				String msg = Msg.getMsg(Env.getCtx(), "XX_DateEmpty");
				ADialog.error(m_WindowNo, m_frame, msg);
			}else {
				cmd_Search();
			}
		}
		else if (e.getSource() == allCheckAssistants){
			cmd_Check();
		}else if (e.getSource() == bClear) {			
			clearFilter();
		}
		else if (e.getSource() == bPrint)
		{
			try {			
				XX_Indicator.imprimirArchivo( xTable, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
		}
	
	
		m_frame.setCursor(Cursor.getDefaultCursor());
		addActionListeners ();
	}   //  actionPerformed

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{	
		tableInit();	
		tableLoad(xTable);	
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_CIListedQty"));
		statusBar.setStatusDB(xTable.getRowCount());
			

	}   //  cmd_Search
	
	/**
	 * allCheckAssistants check Pressed
	 */
	private void cmd_Check()
	{
		dynCheckAssistant();
		
	} 
	
	/** Limpia el filtro*/
	void clearFilter() {
		
		assistantCombo.setSelectedIndex(0);
		dateFrom.setValue(null);
		dateTo.setValue(null);
		allCheckAssistants.setSelected(false);
		dynCheckAssistant();
		dateFrom.setValue(from);
		dateTo.setValue(to);
	}
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		
		int tableRow = xTable.getSelectedRow();
			
		if(tableRow==-1) //Si no tengo nada seleccionado en la tabla 
		{
		
			statusBar.setStatusLine(Msg.translate(Env.getCtx(),"XX_CIListedQty")); 
			statusBar.setStatusDB(xTable.getRowCount());
		}
		else
		{
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_SelectedAssistant")); 
			String assiKey = (String) xTable.getValueAt(tableRow, 0);
			statusBar.setStatusDB(assiKey); // Colocar asistente seleccionado
		
		}
		
		if (e.getValueIsAdjusting())
			return;

	}   //  valueChanged

	/**************************************************************************
	 *  Initialize Table access - create SQL, dateColumn.

	 */
	private void tableInit ()
	{
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		
		m_sql = "WITH " +
				"OC_Chequeadas AS "+ 
				"\n(Select io.XX_CheckAssistant_ID checkA, count(io.C_Order_ID) OC_Chequeadas "+ 
				"\nfrom C_Order o, M_InOut io "+ 
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and io.C_Order_ID = o.C_Order_ID "+ 
				"\nAND io.XX_InOutStatus = 'CH' "+ 
				"\nAND TRUNC(o.XX_CheckupDate)  >= "+ DB.TO_DATE(from, true)+
				"\nAND TRUNC(o.XX_CheckupDate)  <= "+DB.TO_DATE(to, true)+
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID "+ 
				"\n) "+ 
				"\n, "+
				"\nPedidos_Revisados AS "+
				"\n(Select pe.XX_CheckAssistant_ID checkA, Count(pe.XX_VMR_order_ID) Pedidos_Revisados "+
				"\nfrom  XX_VMR_Order pe "+
				"\nwhere TRUNC(pe.XX_DATESTATUSATBAY)  >= "+DB.TO_DATE(from, true)+
				"\nAND TRUNC(pe.XX_DATESTATUSATBAY)  <= "+DB.TO_DATE(to, true)+
				"\nAND pe.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY pe.XX_CheckAssistant_ID "+
				"\n) "+
				"\n, "+
				"\nItems_Devueltos AS "+
				"\n(Select  io.XX_CheckAssistant_ID checkA, count(M_InOutLine_ID) Items_Devueltos "+
				"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o "+
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and o.C_Order_ID = io.C_Order_ID "+
				"\nAND iol.M_InOut_ID = io.M_InOut_ID "+
				"\nAND (iol.xx_extrapieces + iol.scrappedqty) > 0 "+
				"\nAND TRUNC(o.XX_CheckupDate)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(o.XX_CheckupDate)  <= "+DB.TO_DATE(to, true) +
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID "+
				"\n)"+
				"\n, "+
				"\nPiezas_Devueltas AS "+
				"\n(select io.XX_CheckAssistant_ID checkA, sum(iol.xx_extrapieces + iol.scrappedqty)  Piezas_Devueltas "+
				"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o "+
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and o.C_Order_ID = io.C_Order_ID "+
				"\nAND iol.M_InOut_ID = io.M_InOut_ID "+
				"\nAND TRUNC(o.XX_CheckupDate)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(o.XX_CheckupDate)  <= "+DB.TO_DATE(to, true) +
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID "+
				"\n) "+
				"\n, "+
				"\nPiezas_Textil_Chequeadas AS "+
				"\n(select io.XX_CheckAssistant_ID checkA, sum(iol.PickedQty) Piezas_Textil_Chequeadas "+
				"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o, M_Product p "+
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and o.C_Order_ID = io.C_Order_ID "+
				"\nAND iol.M_InOut_ID = io.M_InOut_ID "+
				"\nAND iol.M_Product_ID = p.M_Product_ID "+
				"\nAND p.XX_VMR_ProductClass_ID = "+TEXTIL+
				"\nAND TRUNC(o.XX_CheckupDate)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(o.XX_CheckupDate)  <= "+DB.TO_DATE(to, true) +
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID "+
				"\n) "+
				"\n, "+
				"\nPiezas_NoTextil_Chequeadas AS "+
				"\n(select io.XX_CheckAssistant_ID checkA, sum(iol.PickedQty) Piezas_NoTextil_Chequeadas "+
				"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o, M_Product p "+
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and iol.M_InOut_ID = io.M_InOut_ID "+
				"\nAND o.C_Order_ID = io.C_Order_ID "+
				"\nAND iol.M_Product_ID = p.M_Product_ID "+
				"\nAND p.XX_VMR_ProductClass_ID = "+NO_TEXTIL+
				"\nAND TRUNC(o.XX_CheckupDate)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(o.XX_CheckupDate)  <=  "+DB.TO_DATE(to, true) +
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID "+
				"\n) "+
				"\n, "+
				"\nPiezas_Chequeadas AS "+
				"\n(select io.XX_CheckAssistant_ID checkA, sum(iol.PickedQty) Piezas_Chequeadas "+
				"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o "+
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and o.C_Order_ID = io.C_Order_ID "+
				"\nAND iol.M_InOut_ID = io.M_InOut_ID "+
				"\nAND TRUNC(o.XX_CheckupDate)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(o.XX_CheckupDate)  <= "+DB.TO_DATE(to, true) +
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID "+
				"\n) "+
				"\n, "+
				"\nPiezas_Etiquetadas AS "+
				"\n(select pe.XX_CheckAssistant_ID checkA, sum(de.XX_PRODUCTQUANTITY) Piezas_Etiquetadas "+
				"\nfrom  xx_vmr_order pe "+
				"\ninner join XX_VMR_ORDERREQUESTDETAIL de on (pe.xx_vmr_order_id = de.xx_vmr_order_id) "+
				"\nwhere TRUNC(pe.XX_DATESTATUSATBAY)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(pe.XX_DATESTATUSATBAY)  <= "+DB.TO_DATE(to, true) +
				"\nAND pe.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY pe.XX_CheckAssistant_ID "+
				"\n) "+
				"\n, "+
				"\nChequeadas_Colaboradores_Temp AS "+
				"\n(select io.XX_CheckAssistant_ID checkA, " +
				"\nDECODE(io.XX_CollaboratorsQty,0,sum(iol.PickedQty),null,sum(iol.PickedQty),sum(iol.PickedQty)/io.XX_CollaboratorsQty) Chequeadas_Colaboradores_Temp "+
				"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o "+
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and o.C_Order_ID = io.C_Order_ID "+
				"\nAND iol.M_InOut_ID = io.M_InOut_ID "+
				"\nAND TRUNC(o.XX_CheckupDate)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(o.XX_CheckupDate)  <= "+DB.TO_DATE(to, true) +
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID, io.M_InOut_ID, io.XX_CollaboratorsQty "+
				"\n) "+
				"\n, "+
				"\nChequeadas_Colaboradores AS "+
				"\n(select checkA, sum(Chequeadas_Colaboradores_Temp) Chequeadas_Colaboradores "+
				"\nfrom  Chequeadas_Colaboradores_Temp "+
				"\nGROUP BY checkA "+
				"\n) "+
				"\n, "+
				"\nEtiquetadas_Colaboradores_Temp AS "+
				"\n(select pe.XX_CheckAssistant_ID checkA, " +
				"\nDECODE(pe.XX_CollaboratorsQty,0,sum(de.XX_PRODUCTQUANTITY),null,sum(de.XX_PRODUCTQUANTITY),sum(de.XX_PRODUCTQUANTITY)/pe.XX_CollaboratorsQty) Etiquetadas_Colaboradores_Temp " +
				"\nfrom  xx_vmr_order pe "+
				"\ninner join XX_VMR_ORDERREQUESTDETAIL de on (pe.xx_vmr_order_id = de.xx_vmr_order_id) "+
				"\nwhere TRUNC(pe.XX_DATESTATUSATBAY)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(pe.XX_DATESTATUSATBAY)  <= "+DB.TO_DATE(to, true) +
				"\nAND pe.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY pe.XX_CheckAssistant_ID, pe.XX_VMR_Order_ID, pe.XX_CollaboratorsQty "+
				"\n) "+
				"\n, "+
				"\nEtiquetadas_Colaboradores AS "+
				"\n(select checkA, sum(Etiquetadas_Colaboradores_Temp) Etiquetadas_Colaboradores "+
				"\nfrom  Etiquetadas_Colaboradores_Temp "+
				"\nGROUP BY checkA "+
				"\n) "+
				"\n, "+
				"\nPiezas_Recibidas AS "+
				"\n(select io.XX_CheckAssistant_ID checkA, sum(ol.QtyOrdered) Piezas_Recibidas "+
				"\nfrom C_Order o, C_OrderLine ol, M_InOut io "+
				"\nwhere o.Issotrx = 'N' and io.Issotrx = 'N' and ol.C_Order_ID = o.C_Order_ID "+
				"\nAND o.C_Order_ID = io.C_Order_ID "+
				"\nAND TRUNC(o.XX_ReceptionDate)  >= "+DB.TO_DATE(from, true) +
				"\nAND TRUNC(o.XX_ReceptionDate)  <= "+DB.TO_DATE(to, true) +
				"\nAND o.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY io.XX_CheckAssistant_ID "+
				"\n) "+
				"\n, "+
				"\nMeta_Mes AS "+
				"\n(select pag.XX_CheckAssistant_ID checkA, CASE WHEN sum(pag.XX_PiecesGoal) IS NULL THEN 0 ELSE sum(pag.XX_PiecesGoal) END  Meta_Mes "+
				"\nfrom XX_VLO_PiecesAssistantGoal pag "+
				"\nwhere pag.XX_Year >= TO_NUMBER(TO_CHAR("+DB.TO_DATE(from, true)+",'YYYY')) "+
				"\nAND pag.XX_Month >= TO_NUMBER(TO_CHAR("+DB.TO_DATE(from, true)+",'MM')) "+
				"\nAND pag.XX_Year <= TO_NUMBER(TO_CHAR("+DB.TO_DATE(to, true)+",'YYYY')) "+
				"\nAND pag.XX_Month <= TO_NUMBER(TO_CHAR("+DB.TO_DATE(to, true)+",'MM')) "+
				"\nAND pag.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID()+
				"\nGROUP BY pag.XX_CheckAssistant_ID "+
				"\n) " +
				"\n "+
				"\nSELECT ";
			
			if(assistantCombo.getSelectedIndex() != 0)
				m_sql += "\nbp.name, ";
			else 
				m_sql += "'"+Msg.translate(Env.getCtx(), "XX_AllCheckAssistants")+"',";   //1
				
			if(assistantCombo.getSelectedIndex() != 0){
				m_sql += "\nDECODE(A.OC_Chequeadas,NULL,0,A.OC_Chequeadas) OC_Chequeadas,  "+   //2
					"\nDECODE(B.Pedidos_Revisados,NULL,0,B.Pedidos_Revisados) Pedidos_Revisados,  "+  //3
					"\nDECODE(C.Items_Devueltos,NULL,0,C.Items_Devueltos) Items_Devueltos,  "+  //4
					"\nDECODE(D.Piezas_Devueltas,NULL,0,D.Piezas_Devueltas) Piezas_Devueltas,   "+   //5
					"\nDECODE(E.Piezas_Textil_Chequeadas,NULL,0,E.Piezas_Textil_Chequeadas) Piezas_Textil_Chequeadas, "+   //6
					"\nDECODE(F.Piezas_NoTextil_Chequeadas,NULL,0,F.Piezas_NoTextil_Chequeadas) Piezas_NoTextil_Chequeadas,  "+   //7
					"\nDECODE(G.Piezas_Chequeadas,NULL,0,G.Piezas_Chequeadas) Piezas_Chequeadas,  "+   //8
					"\nDECODE(H.Piezas_Etiquetadas,NULL,0,H.Piezas_Etiquetadas) Piezas_Etiquetadas,  "+   //9
					"\nDECODE(I.Chequeadas_Colaboradores,NULL,0,I.Chequeadas_Colaboradores) Chequeadas_Colaboradores,  "+   //10
					"\nDECODE(J.Etiquetadas_Colaboradores,NULL,0,J.Etiquetadas_Colaboradores) Etiquetadas_Colaboradores,  "+   //11
					"\nDECODE(K.Piezas_Recibidas,NULL,0,K.Piezas_Recibidas)  Piezas_Recibidas, "+   //12
					"\nDECODE(L.Meta_Mes,NULL,'-',L.Meta_Mes) Meta_Mes "+    //13
					"\nFROM C_BPARTNER bp "+
					"\nLEFT JOIN OC_Chequeadas A ON (A.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Pedidos_Revisados B ON (B.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Items_Devueltos C ON (C.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Devueltas D ON (D.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Textil_Chequeadas E ON (E.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_NoTextil_Chequeadas F ON (F.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Chequeadas G ON (G.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Etiquetadas H ON (H.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Chequeadas_Colaboradores I ON (I.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Etiquetadas_Colaboradores J ON (J.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Recibidas K ON (K.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Meta_Mes L ON (L.checkA = bp.C_BPARTNER_id) "+
					"\nWHERE  bp.isActive='Y' AND bp.C_JOB_ID ="+ Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_CHASSI_ID")+
					"\nAND bp.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID();
				}
				else{
					m_sql += "\nDECODE(sum(A.OC_Chequeadas),NULL,0,sum(A.OC_Chequeadas)) OC_Chequeadas,  "+   //2
					"\nDECODE(sum(B.Pedidos_Revisados),NULL,0,sum(B.Pedidos_Revisados)) Pedidos_Revisados,  "+  //3
					"\nDECODE(sum(C.Items_Devueltos),NULL,0,sum(C.Items_Devueltos)) Items_Devueltos,  "+  //4
					"\nDECODE(sum(D.Piezas_Devueltas),NULL,0,sum(D.Piezas_Devueltas)) Piezas_Devueltas,   "+   //5
					"\nDECODE(sum(E.Piezas_Textil_Chequeadas),NULL,0,sum(E.Piezas_Textil_Chequeadas)) Piezas_Textil_Chequeadas, "+   //6
					"\nDECODE(sum(F.Piezas_NoTextil_Chequeadas),NULL,0,sum(F.Piezas_NoTextil_Chequeadas)) Piezas_NoTextil_Chequeadas,  "+   //7
					"\nDECODE(sum(G.Piezas_Chequeadas),NULL,0,sum(G.Piezas_Chequeadas)) Piezas_Chequeadas,  "+   //8
					"\nDECODE(sum(H.Piezas_Etiquetadas),NULL,0,sum(H.Piezas_Etiquetadas)) Piezas_Etiquetadas,  "+   //9
					"\nDECODE(sum(I.Chequeadas_Colaboradores),NULL,0,sum(I.Chequeadas_Colaboradores)) Chequeadas_Colaboradores,  "+   //10
					"\nDECODE(sum(J.Etiquetadas_Colaboradores),NULL,0,sum(J.Etiquetadas_Colaboradores)) Etiquetadas_Colaboradores,  "+   //11
					"\nDECODE(sum(K.Piezas_Recibidas),NULL,0,sum(K.Piezas_Recibidas))  Piezas_Recibidas, "+   //12
					"\nDECODE(sum(L.Meta_Mes),NULL,'-',sum(L.Meta_Mes)) Meta_Mes "+    //13
					"\nFROM C_BPARTNER bp "+
					"\nLEFT JOIN OC_Chequeadas A ON (A.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Pedidos_Revisados B ON (B.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Items_Devueltos C ON (C.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Devueltas D ON (D.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Textil_Chequeadas E ON (E.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_NoTextil_Chequeadas F ON (F.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Chequeadas G ON (G.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Etiquetadas H ON (H.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Chequeadas_Colaboradores I ON (I.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Etiquetadas_Colaboradores J ON (J.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Piezas_Recibidas K ON (K.checkA = bp.C_BPARTNER_id) "+
					"\nLEFT JOIN Meta_Mes L ON (L.checkA = bp.C_BPARTNER_id) "+
					"\nWHERE  bp.isActive='Y' AND bp.C_JOB_ID ="+ Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_CHASSI_ID")+
					"\nAND bp.AD_Client_ID = "+ Env.getCtx().getAD_Client_ID();
					}
		
		if (assistantCombo.getSelectedIndex() > 1) {
			KeyNamePair assi = (KeyNamePair) assistantCombo.getValue();
			if (assi.getKey() != -1)
				m_sql += "\nAND bp.C_BPartner_ID = "+assi.getKey();
		}

	}
	
	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */

	private void tableLoad (MiniTable table)
	{
		String sql = m_sql.toString();
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		Calendar toDate = Calendar.getInstance();
		toDate.setTimeInMillis(to.getTime());
		Calendar fromDate = Calendar.getInstance();
		fromDate.setTimeInMillis(from.getTime());
		
		PreparedStatement stmt=null;
		ResultSet rs = null;

		try
		{
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			int row = 0;
			while (rs.next()) {
				Double piezasColaboradores = 0.0;
				Integer totalConsolidadoPiezas = 0;
				Integer tasaChequeo = 0;
				Integer diasHabiles = lookupTotalDays(fromDate, toDate)-lookupWeekend(fromDate, toDate)-lookupNonBusinessDay(fromDate, toDate);
	
				table.setRowCount(row + 1);
				table.setValueAt(rs.getString(1), row, 0); // 0 Asistente
				table.setValueAt(rs.getInt(2), row, 1);    //  1 O/C Chequeadas
				table.setValueAt(rs.getInt(3), row, 2);  //  2 Pedidos Etiquetados
				table.setValueAt(rs.getInt(4), row, 3);    //  3 Items Devueltos
				table.setValueAt(rs.getInt(5), row, 4);  //  4 Piezas Devueltas
				table.setValueAt(rs.getInt(6), row, 5);  //  5 Piezas Textil Chequeadas
				table.setValueAt(rs.getInt(7), row, 6); //  6 Piezas No Textil Chequeadas
				table.setValueAt(rs.getInt(8), row, 7);  //  7 Piezas Chequeadas
				table.setValueAt(rs.getInt(9), row, 8);   //  8 Piezas Etiquetadas
				totalConsolidadoPiezas = rs.getInt(9)+rs.getInt(8);
				table.setValueAt(totalConsolidadoPiezas, row, 9);   //  9 Consolidado de piezas
				piezasColaboradores = rs.getDouble(10)+rs.getDouble(11);
			//	System.out.println("\nJORNADA "+JORNADA+", piezasColaboradores "+piezasColaboradores+ ", diasHabiles "+diasHabiles);
				if(JORNADA !=0 && diasHabiles !=0){
					tasaChequeo =(int) Math.round(piezasColaboradores/JORNADA/diasHabiles);
				}
			//	System.out.println("\ntasaChequeo "+tasaChequeo+" ROW: "+row);
				table.setValueAt(tasaChequeo, row, 10);   //10 Tasa de Chequeo
				table.setValueAt(rs.getInt(12), row, 11); //  11 Piezas Recibidas
				table.setValueAt(rs.getString(13), row, 12);   //  12 Meta Mes
				row++;	
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}   //  tableLoad
	
	//Comentado porque se modificó el código para obtener el resultado más rápido
/*
	private void tableInit2()
	{
		m_sql = new StringBuffer ();
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();

			m_sql.append("select ");
			
			if(assistantCombo.getSelectedIndex() != 0)
				m_sql.append("\nbp.name, ");
			else m_sql.append("\nj.name, ");                //1
			
			m_sql.append("\n(Select count(io.C_Order_ID) "+
						"\nfrom C_Order o, M_InOut io "+
						"\nwhere io.C_Order_ID = o.C_Order_ID " +
						"\nAND io.XX_InOutStatus = 'CH' ");
	
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
				
			m_sql.append("\n)OC_Chequeadas, " +                 //2
					"\n(Select Count(pe.XX_VMR_order_ID) "+
			        "from  XX_VMR_Order pe ");
			
			m_sql.append("\nwhere TRUNC(pe.XX_DATESTATUSATBAY) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(pe.XX_DATESTATUSATBAY) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND pe.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("AND pe.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
		
			m_sql.append("\n) Pedidos_Etiquetados, " +              //3
					"\n(Select count(M_InOutLine_ID) " +
					"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o " +
					"\nwhere o.C_Order_ID = io.C_Order_ID " +
					"\nAND iol.M_InOut_ID = io.M_InOut_ID " +
					"\nAND (iol.xx_extrapieces + iol.scrappedqty) > 0 ");

			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");

			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
			          
			m_sql.append("\n) Items_Devueltos, ");       //4
			
			m_sql.append("\n(select sum(iol.xx_extrapieces + iol.scrappedqty) " +
					"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o " +
					"\nwhere o.C_Order_ID = io.C_Order_ID " +
					"\nAND iol.M_InOut_ID = io.M_InOut_ID ");

			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\n AND TRUNC(o.XX_CheckupDate) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 

			m_sql.append("\n) Piezas_Devueltas, ");       //5
			
			m_sql.append("\n(select sum(iol.PickedQty) " +
					"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o, M_Product p " +
					"\nwhere o.C_Order_ID = io.C_Order_ID " +
					"\nAND iol.M_InOut_ID = io.M_InOut_ID " +
					"\nAND iol.M_Product_ID = p.M_Product_ID " +
					"\nAND p.XX_VMR_ProductClass_ID = "+TEXTIL);

			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
			
			m_sql.append("\n) Piezas_Textil_Chequeadas, ");            //6
			
			m_sql.append("\n(select sum(iol.PickedQty) " +
					"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o, M_Product p " +
					"\nwhere iol.M_InOut_ID = io.M_InOut_ID " +
					"\nAND o.C_Order_ID = io.C_Order_ID " +
					"\nAND iol.M_Product_ID = p.M_Product_ID " +
					"\nAND p.XX_VMR_ProductClass_ID = "+NO_TEXTIL);
			
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
			
			m_sql.append("\n) Piezas_NoTextil_Chequeadas, ");                   //7
			
			m_sql.append("\n(select sum(iol.PickedQty) " +
					"\nfrom  M_InOutLine iol,  M_InOut io, C_Order o " +
					"\nwhere o.C_Order_ID = io.C_Order_ID " +
					"\nAND iol.M_InOut_ID = io.M_InOut_ID ");

			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)	
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
			           
			m_sql.append("\n) Piezas_Chequeadas, ");                   //8
			
			m_sql.append("\n(select sum(de.XX_PRODUCTQUANTITY) " +
					"\nfrom  xx_vmr_order pe " +
					"\ninner join XX_VMR_ORDERREQUESTDETAIL de on (pe.xx_vmr_order_id = de.xx_vmr_order_id)");
			
			m_sql.append("\nwhere TRUNC(pe.XX_DATESTATUSATBAY) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(pe.XX_DATESTATUSATBAY) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND pe.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)	
				m_sql.append("\nAND pe.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
			           
			m_sql.append("\n) Piezas_Etiquetadas, ");            //9
			m_sql.append("\n(select sum(ol.QtyOrdered) " +
					"\nfrom  C_Order o, C_OrderLine ol, M_InOut io " +
					"\nwhere ol.C_Order_ID = o.C_Order_ID " +
					"\nAND o.C_Order_ID = io.C_Order_ID");

			m_sql.append("\nAND TRUNC(o.XX_ReceptionDate) ").append(" >= ").append(
						DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(o.XX_ReceptionDate) ").append(" <= ").append(
						DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			
			if(assistantCombo.getSelectedIndex() > 0)	
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
			           
			m_sql.append("\n) Piezas_Recibidas, ");               //10
			
			m_sql.append("\n(select sum(pag.XX_PiecesGoal) " +
					"\nfrom XX_VLO_PiecesAssistantGoal pag ");
			
			m_sql.append("\nwhere pag.XX_Year >= TO_NUMBER(TO_CHAR("+DB.TO_DATE(from,true)+",'YYYY')) ");
			m_sql.append("\nAND pag.XX_Month >= TO_NUMBER(TO_CHAR("+DB.TO_DATE(from,true)+",'MM')) ");
			m_sql.append("\nAND pag.XX_Year <= TO_NUMBER(TO_CHAR("+DB.TO_DATE(to,true)+",'YYYY')) ");
			m_sql.append("\nAND pag.XX_Month <= TO_NUMBER(TO_CHAR("+DB.TO_DATE(to,true)+",'MM')) ");
			m_sql.append("\nAND pag.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("\nAND pag.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 

			m_sql.append("\n) Meta_Mes, ");          //11
			
			m_sql.append("\n(Select sum(io.XX_CollaboratorsQty)/count(io.C_Order_ID) "+
					"\nfrom C_Order o, M_InOut io "+
					"\nwhere io.C_Order_ID = o.C_Order_ID " +
					"\nAND io.XX_InOutStatus = 'CH' ");

			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" >= ").append(
					DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(o.XX_CheckupDate) ").append(" <= ").append(
					DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND o.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
		
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("\nAND io.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
				m_sql.append("\n) Colab_OC_Chequeadas, "+           //12
					
					"\n(Select sum(pe.XX_CollaboratorsQty)/Count(pe.XX_VMR_order_ID) "+
	        "from  XX_VMR_Order pe ");
			m_sql.append("\nwhere TRUNC(pe.XX_DATESTATUSATBAY) ").append(" >= ").append(
					DB.TO_DATE(from, true)).append(" ");
			m_sql.append("\nAND TRUNC(pe.XX_DATESTATUSATBAY) ").append(" <= ").append(
					DB.TO_DATE(to, true)).append(" ");
			m_sql.append("\nAND pe.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
		
			if(assistantCombo.getSelectedIndex() > 0)		
				m_sql.append("AND pe.XX_CheckAssistant_ID = bp.C_BPARTNER_id "); 
	
			m_sql.append("\n) Colab_Pedidos_Etiquetados ");             //13
			
			m_sql.append("\nfrom C_BPARTNER bp ");
			
			if (assistantCombo.getSelectedIndex() == 0) 
				m_sql.append("\n, C_JOB j ");
			
			m_sql.append("\nwhere bp.isActive='Y' AND bp.C_JOB_ID = " + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_CHASSI_ID"));
			m_sql.append("\nAND bp.AD_Client_ID = ").append(Env.getCtx().getAD_Client_ID()).append(" ");
	
			if (assistantCombo.getSelectedIndex() == 0) 
				m_sql.append("\nAND bp.C_JOB_ID = j.C_JOB_ID ");
			
			if (assistantCombo.getSelectedIndex() > 1) {
				KeyNamePair assi = (KeyNamePair) assistantCombo.getValue();
				if (assi.getKey() != -1)
					m_sql.append("\nAND bp.C_BPartner_ID = ").append(
							assi.getKey()).append(" ");
			}
			if (assistantCombo.getSelectedIndex() == 0){
				m_sql.append("\ngroup by j.name");
			}
		
	}   //  tableInit
	*/
	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
/*
	private void tableLoad2 (MiniTable table, boolean allAssistant)
	{
		String sql = m_sql.toString();
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		Calendar toDate = Calendar.getInstance();
		toDate.setTimeInMillis(to.getTime());
		Calendar fromDate = Calendar.getInstance();
		fromDate.setTimeInMillis(from.getTime());
		
		PreparedStatement stmt=null;
		ResultSet rs = null;

		try
		{
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			int row = 0;
			while (rs.next()) {
				Integer nroColaboradores = 0;
				Integer totalConsolidadoPiezas = 0;
				Double tasaChequeo = 0.0;
				Integer diasHabiles = lookupTotalDays(fromDate, toDate)-lookupWeekend(fromDate, toDate)-lookupNonBusinessDay(fromDate, toDate);
	
				table.setRowCount(row + 1);
				if (allAssistant)
					table.setValueAt(Msg.translate(Env.getCtx(), "XX_AllCheckAssistants"), row, 0); //  0 Asistente de Chequeo
				else table.setValueAt(rs.getString(1), row, 0);
				table.setValueAt(rs.getInt(2), row, 1);    //  1 O/C Chequeadas
				table.setValueAt(rs.getInt(3), row, 2);  //  2 Pedidos Etiquetados
				if (rs.getInt(4)> 0)
					table.setValueAt(rs.getInt(4), row, 3);    //  3 Items Devueltos
				else table.setValueAt(0, row, 3);
				if (rs.getInt(5)> 0)
					table.setValueAt(rs.getInt(5), row, 4);  //  3 Items Devueltos
				else table.setValueAt(0, row, 4);
				if (rs.getInt(6)> 0)
					table.setValueAt(rs.getInt(6), row, 5);  //  5 Piezas Textil Chequeadas
				else table.setValueAt(0, row,5);
				if (rs.getInt(7)> 0)
					table.setValueAt(rs.getInt(7), row, 6); //  6 Piezas No Textil Chequeadas
				else table.setValueAt(0, row,6);
				if (rs.getInt(8)> 0)
					table.setValueAt(rs.getInt(8), row, 7);  //  7 Piezas Chequeadas
				else table.setValueAt(0, row,7);
				if (rs.getInt(9)> 0)
					table.setValueAt(rs.getInt(9), row, 8);   //  8 Piezas Etiquetadas
				else table.setValueAt(0, row,8);
				if (rs.getInt(8)>= 0 && rs.getInt(9)>= 0){
					totalConsolidadoPiezas = rs.getInt(9)+rs.getInt(8);
					table.setValueAt(rs.getInt(9)+rs.getInt(8), row, 9);   //  9 Consolidado de piezas
				}else if (rs.getInt(8)>= 0){
					totalConsolidadoPiezas = rs.getInt(8);         
					table.setValueAt(rs.getInt(8), row, 9);
				}else if(rs.getInt(9)>= 0){
					totalConsolidadoPiezas = rs.getInt(9);
					table.setValueAt(rs.getInt(9), row, 9);
				}
				else 
					table.setValueAt(0, row, 9);
				if(rs.getInt(12)>=0 && rs.getInt(13)>=0)
					nroColaboradores = rs.getInt(12)+rs.getInt(13);
				else if(rs.getInt(12)>=0)
					nroColaboradores = rs.getInt(12);
				else if(rs.getInt(13)>=0)
					nroColaboradores = rs.getInt(13);
				
				System.out.println("\nJORNADA "+JORNADA+", nroColaboradores "+nroColaboradores+
						"\n diasHabiles "+diasHabiles+", totalConsolidadoPiezas "+totalConsolidadoPiezas);
				if(nroColaboradores !=0 && JORNADA !=0 && diasHabiles !=0){
					tasaChequeo = totalConsolidadoPiezas/nroColaboradores/JORNADA/diasHabiles;
				}
				System.out.println("\ntasaChequeo "+tasaChequeo);
				table.setValueAt((double)Math.round(tasaChequeo*100)/100, row, 10);   //10 Tasa de Chequeo
				if (rs.getInt(10)> 0)
					table.setValueAt(rs.getInt(10), row, 11); //  11 Piezas Recibidas
				else table.setValueAt(0, row, 11);
				table.setValueAt(rs.getInt(11), row, 12);   //  12 Meta Mes
				row++;
			
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}   //  tableLoad
	
*/
	
	private Integer lookupTotalDays(Calendar fromDate, Calendar toDate){
		
		Calendar dateTemp = Calendar.getInstance();
		dateTemp.setTime(fromDate.getTime());
		Integer days =0;
		while (dateTemp.compareTo(toDate)<=0) {
			days ++;
			dateTemp.add(Calendar.DATE, 1);
		}
		return days;
	}

	private Integer lookupWeekend(Calendar fromDate, Calendar toDate){
		
		Calendar dateTemp = Calendar.getInstance();
		dateTemp.setTime(fromDate.getTime());
		Integer days =0;
		while (dateTemp.compareTo(toDate) <= 0) {
			int dayOfWeek = dateTemp.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY){
				days ++;
			}
			dateTemp.add(Calendar.DATE, 1);
		}
		return days;
	}
	
	private Integer lookupNonBusinessDay(Calendar fromDate, Calendar toDate) {
		
		int dias = 0;
		Timestamp from = new Timestamp(fromDate.getTimeInMillis());
		Timestamp to= new Timestamp(toDate.getTimeInMillis());
		//Busco cuantos dias feriados hay entre los dias de holgura a la fecha de recepción
		String sql = "\nSELECT Count(*) dias " +
		"\nFROM C_NonBusinessDay WHERE AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		"\n AND trunc(DATE1) >= "+DB.TO_DATE(from, true)+
		"\n AND trunc(DATE1) <= "+DB.TO_DATE(to, true)+
		"\n AND to_char(DATE1,'d') NOT IN (1,7)";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();				
			while(rs.next()){
				dias = rs.getInt(1);
			}

		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql,e.getMessage());
		}
		finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return dias;
	}

}
