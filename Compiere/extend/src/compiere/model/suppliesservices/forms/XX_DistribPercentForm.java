package compiere.model.suppliesservices.forms;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.suppliesservices.processes.XX_SalesPercentage;

	public class XX_DistribPercentForm extends CPanel implements FormPanel, ActionListener, 
		TableModelListener, ListSelectionListener{
		// Tomado de Distribución (Javier Pino) y modificado para Bienes y Servicios
		/** Purchase of Supplies and Services
		 * Maria Vintimilla Funcion 21 
		 * Distribution By Sales (Stores)**/ 
		static final long serialVersionUID = 1L;
		/**	Window No			*/
		private int m_WindowNo = 0;
		/**	FormFrame			*/
		private FormFrame m_frame;
		/**	Logger				*/
		static CLogger log = CLogger.getCLogger(XX_DistribPercentForm.class);
		private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
		private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
		private int m_by = Env.getCtx().getAD_User_ID();
		private static int record = 0;
		private static int tableInit_option;
		static StringBuffer m_sql = null;
		static String m_groupBy = "";
		static String m_orderBy = "";
		private CLabel LineRefProv_Label = new CLabel();
		private CLabel xMatchedLabel = new CLabel();
		private Integer detail_id = null;
		static Integer Product_ID;
		static Integer associatedReference_ID;
		private CPanel mainPanel = new CPanel();
		private CPanel southPanel = new CPanel();
		private CPanel northPanel = new CPanel();
		private CPanel centerPanel = new CPanel();
		private CPanel xPanel = new CPanel();
		private StatusBar statusBar = new StatusBar();
		private BorderLayout mainLayout = new BorderLayout();
		private BorderLayout centerLayout = new BorderLayout(5,5);
		private GridBagLayout northLayout = new GridBagLayout();
		private GridBagLayout southLayout = new GridBagLayout();
		private CButton bUpdate = new CButton();
		private CButton bGenerate = new CButton();
		private JScrollPane xProductScrollPane = new JScrollPane();
		private TitledBorder xProductBorder = 
			new TitledBorder(Msg.translate(Env.getCtx(), "XX_PercentageDistribution"));
		private MiniTable xProductTable = new MiniTable();
		private Vector<Integer> warehouses = new Vector<Integer>();
		private Vector<BigDecimal> ware_house_percentages = new Vector<BigDecimal>();
		private Vector <Integer> vectorIDOrgs = new Vector <Integer>();
		private Vector <Integer> vectorLines = new Vector <Integer>();
		private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
		private BigDecimal sumaPorcentaje = new BigDecimal(0);
		private static Ctx context = null;
		static Ctx ctx_aux = new Ctx();
		private int tipo;
		private static Trx transaction = null;
		private boolean distribAll;
		private boolean it_work;
		private VNumber xMatched = 
			new VNumber("xMatched", false, true, false, DisplayTypeConstants.Amount, "xMatched");
		private MOrderLine detail = null;
		private MOrder order = null;
		//private MOrderLine detail = new MOrderLine(context, record, transaction.getTrxName());
			
		/**
		*	Initialize Panel
		*  @param WindowNo window
		*  @param frame frame
		*/
		public void init (int WindowNo, FormFrame frame){
			m_WindowNo = WindowNo;
			m_frame = frame;
			log.info("WinNo=" + m_WindowNo + " - AD_Client_ID=" + m_AD_Client_ID + 
					", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
			Env.getCtx().setIsSOTrx(m_WindowNo, false);
			Ctx aux = Env.getCtx();	
			detail_id = aux.getContextAsInt("#C_OrderLineForm_ID");
			System.out.println(detail_id);
			tipo = aux.getContextAsInt("#Tipo");
			
			if(tipo == 1){
				distribAll = true;
				order = new MOrder(Env.getCtx(),detail_id,null);
			}
			else{
				distribAll = false;
				detail = new MOrderLine(aux, detail_id, null);	
				detail.setXX_IsPiecesPercentage(false);
				detail.setXX_IsAmountDistrib(false);
				detail.setXX_ClearedDistrib(true);
				detail.save();
			}
			
			//detail_id = (Env.getCtx()).getContextAsInt("#C_OrderLine_ID");
			(Env.getCtx()).remove("#C_OrderLine_ID");
			 try {//	UI
				jbInit();
				dynInit();
				frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
				frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			}
			catch(Exception e){
				log.log(Level.SEVERE, "", e);
			}
		}//	init
	
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
			mainPanel.setLayout(mainLayout);
			northPanel.setLayout(northLayout);
			bUpdate.setText(Msg.translate(Env.getCtx(), "XX_ReloadPercentages"));
			bUpdate.setEnabled(true);
			southPanel.setLayout(southLayout);
			bGenerate.setText(Msg.translate(Env.getCtx(), "XX_GeneratePieces"));
			bGenerate.setEnabled(false);
			centerPanel.setLayout(centerLayout);
			xProductScrollPane.setBorder(xProductBorder);
			xProductScrollPane.setPreferredSize(new Dimension(450, 230));
			xPanel.setLayout(xLayout);
			mainPanel.add(northPanel,  BorderLayout.NORTH);
			northPanel.add(LineRefProv_Label,new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
			mainPanel.add(southPanel,BorderLayout.SOUTH);
			mainPanel.add(centerPanel, BorderLayout.CENTER);
			centerPanel.add(xProductScrollPane,  BorderLayout.NORTH);
			xProductScrollPane.getViewport().add(xProductTable, null);
			centerPanel.add(xPanel, BorderLayout.CENTER);
			xMatchedLabel.setText(Msg.translate(Env.getCtx(), "Difference"));
			southPanel.add(xMatchedLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
			southPanel.add(xMatched, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
			southPanel.add(bUpdate, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
			southPanel.add(bGenerate, new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		}//  jbInit
	
		/**
		*  Dynamic Init.
		*  Table Layout, Visual, Listener
		*/
		private void dynInit(){		
			ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), 
						"XX_WarehouseName"), ".", KeyNamePair.class),             			//  1
				new ColumnInfo(Msg.translate(Env.getCtx(), 
						"XX_PercentageAssigned"), ".", BigDecimal.class, false, false, null)  //  2
			};
			xProductTable.prepareTable(layout, "", "", false, "");
				
			//  Visual
			CompiereColor.setBackground (this);
			//  Listener
			xProductTable.getSelectionModel().addListSelectionListener(this);
			xProductTable.getModel().addTableModelListener(this);
			xProductTable.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				}	
			@Override
			public void keyReleased(KeyEvent e) {
				int row = xProductTable.getSelectedColumn();
				int column = xProductTable.getSelectedRow();
				xProductTable.editCellAt(column, row);
			}	
			@Override
			public void keyPressed(KeyEvent e) {		
				}
					
			});
			bUpdate.addActionListener(this);
			bGenerate.addActionListener(this);
			//  Init
			statusBar.setStatusLine("");
			statusBar.setStatusDB(0);
			tableInit_option=0;
			if(tipo == 1){
				tableInit(0,order.getC_Order_ID(),tipo);
			}
			else {
				tableInit(detail.get_ID(),detail.getC_Order_ID(),tipo);	
			}
			
			tableLoad ();
			xProductTable.getColumnModel().getColumn(0).setPreferredWidth(292);
			xProductTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		}//  dynInit
	
		/**
		* 	Dispose
		*/
		public void dispose(){
			if (m_frame != null)
				m_frame.dispose();
				m_frame = null;
		}//	dispose
	
		/**
		*  Action Listener
		*  @param e event
		*/
		public void actionPerformed (ActionEvent e){				
			if (e.getSource() == bUpdate)
				cmd_update();
			else if (e.getSource() == bGenerate)
				cmd_Generate();
		}//  actionPerformed
	
		/**
		*  Associate Button Pressed
		*/
		private void cmd_update(){
			xProductScrollPane.getViewport().remove(xProductTable);
			xProductTable = new MiniTable();
			xProductScrollPane.getViewport().add(xProductTable, null);
			xProductTable.getModel().addTableModelListener(this);
			ColumnInfo[] layout = new ColumnInfo[] {
					new ColumnInfo(Msg.translate(Env.getCtx(), 
							"XX_WarehouseName"), ".", KeyNamePair.class), //  1
					new ColumnInfo(Msg.translate(Env.getCtx(), 
							"XX_PercentageAssigned"), ".", BigDecimal.class, false, false, null) //  2
			};
			xProductTable.prepareTable(layout, "", "", false, "");
			//  Visual
			CompiereColor.setBackground (this);
			//  Listener
			xProductTable.getSelectionModel().addListSelectionListener(this);
			xProductTable.getModel().addTableModelListener(this);
			tableInit_option = 0;
			tableInit(detail.get_ID(), detail.getC_Order_ID(), tipo);
			tableLoad ();
			xProductTable.getColumnModel().getColumn(0).setPreferredWidth(292);
			xProductTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		}   //  cmd_update
	
		/**
		*  Process Button Pressed - Process Matching
		*/
		private void cmd_Generate(){
			Vector<Integer> codes = new Vector<Integer>();
			Vector<String> stores = new Vector<String>();
			Vector<Double> percentages = new Vector <Double>();
			for(int i = 0; i < xProductTable.getRowCount(); i++){
				if ((xProductTable.getValueAt(i, 1) == null) 
					|| (((Number)xProductTable.getValueAt(i, 1)).doubleValue() == 0))
					continue;
				stores.add(((KeyNamePair) (xProductTable.getValueAt(i, 0))).getName());
				codes.add(((KeyNamePair) (xProductTable.getValueAt(i, 0))).getKey());
				percentages.add(new Double(xProductTable.getValueAt(i, 1).toString())); 
			}//For
			if(distribAll){
				String sqlLines = " SELECT C_OrderLine_ID ID " +
				" FROM C_ORDERLINE" +
				" WHERE C_ORDER_ID = " + order.getC_Order_ID();
				try {
					PreparedStatement ps = 
						DB.prepareStatement(sqlLines, null);
					ResultSet rs = ps.executeQuery();
					while(rs.next()){ 
						vectorLines.add(rs.getInt("ID"));
					}//While
					rs.close();
					ps.close();
				}//try
				catch (SQLException e){
					e.printStackTrace();			
				}
			}
			else {
				vectorLines.add(detail.get_ID());
			}
			int qtyLines = vectorLines.size();
			
			for(int i = 0; i < vectorLines.size(); i++){
				MOrderLine Line = new MOrderLine(Env.getCtx(), vectorLines.get(i), null);
				it_work = XX_SalesPercentage.applyDistribution(Line, 
						percentages, codes, vectorIDOrgs, m_WindowNo , m_frame, tipo, order);
//				Line.setXX_DistributionType("PO");
//				Line.save();
			}
			
			if (it_work ) dispose();
		}// Fin cmd_Generate
			
		/**
		*  List Selection Listener
		*  @param e event
		*/
		public void valueChanged (ListSelectionEvent e){
			int row = xProductTable.getSelectedRow();
			if(row==-1){
		        bUpdate.setEnabled(false);
			}else{
		        bUpdate.setEnabled(true);
			}
			if (e.getValueIsAdjusting())
				return;		
		}   //  valueChanged
	
		/**
		*  Table Model Listener - calculate matched Qty
		*  @param e event
		*/
		public void tableChanged (TableModelEvent e){
			if(e.getColumn() == 1){
				sumaPorcentaje = null;
				sumaPorcentaje = new BigDecimal(0);
				for (int i = 0; i < xProductTable.getRowCount(); i++) {
					if ( xProductTable.getValueAt(i, 1) != null )
					sumaPorcentaje = sumaPorcentaje.add((BigDecimal)xProductTable.getValueAt(i, 1));				
				}
				xMatched.setValue(new BigDecimal(100).subtract(sumaPorcentaje));
				if ((new BigDecimal(100).subtract(sumaPorcentaje)).compareTo(new BigDecimal(0)) == 0 ){
					xMatched.setBackground(Color.GREEN);
					bGenerate.setEnabled(true);
					for (int i = 0; i < xProductTable.getRowCount(); i++) {
						warehouses.add(((KeyNamePair)xProductTable.getValueAt(i, 0)).getKey());
						ware_house_percentages.add((BigDecimal)xProductTable.getValueAt(i, 1));
					}// For
				}//If Suma
				else{
					xMatched.setBackground(Color.YELLOW);
					bGenerate.setEnabled(false);
				}
			}//If Column
		}   //  tableChanged
	
			
		/**
		*  Initialize Table access - create SQL, dateColumn.
		*  <br>
		*  The driving table is "hdr", e.g. for hdr.C_BPartner_ID=..
		*  The line table is "lin", e.g. for lin.M_Product_ID=..
		*  You use the dateColumn/qtyColumn variable directly as it is table specific.
		*  <br>
		*  The sql is dependent on MatchMode:
		*  - If Matched - all (fully or partially) matched records are listed
		*  - If Not Matched - all not fully matched records are listed
		*  @param display (Invoice, Shipment, Order) see MATCH_*
		*  @param matchToType (Invoice, Shipment, Order) see MATCH_*
		*/	
		private static void tableInit (int detail, int header, int tipo){
			m_sql = new StringBuffer ();
			if(tableInit_option==0){
				if(tipo == 1){
					m_sql.append(" SELECT tab.name, tab.Org, tab.percentage "+
							" FROM (" +
								" SELECT w.AD_Org_ID org, w.value value, w.Description Name, 0 percentage, " +
								" w.AD_Client_ID AD_Client_ID"+
								" FROM AD_Org w WHERE w.IsActive = 'Y' " + 
								" AND w.AD_Client_ID = " + 
								Env.getCtx().getAD_Client_ID() +
								// Línea incluida por Carmen Capote//
								" AND W.NAME <> 'ASECON'   and W.NAME <> '*'   and W.NAME <> 'CENTROBECO') tab");
				}
				else {
					m_sql.append(" SELECT tab.name, tab.Org, tab.percentage "+
							" FROM (" +
								" (SELECT w.AD_Org_ID, w.value value, w.Description name, " +
								" per.XX_PercentagePerCC percentage, per.XX_Org_ID Org, " +
								" w.AD_Client_ID AD_Client_ID " +
								" FROM AD_Org w join XX_ProductPercentDistrib per  on( w.AD_Org_ID = per.XX_Org_ID) " +
								" WHERE w.IsActive = 'Y' " + 
								" AND w.AD_Client_ID = " + 
								Env.getCtx().getAD_Client_ID() +
								" AND per.C_OrderLine_ID = "+ detail + ") " + 
								" UNION "+
								" (SELECT w.AD_Org_ID, w.value value, w.Description Name, 0, " +
								" w.AD_Org_ID AD_Org_ID, w.AD_Client_ID AD_Client_ID"+
								" FROM AD_Org w WHERE w.IsActive = 'Y' " +
								" AND w.AD_Client_ID = " + 
								Env.getCtx().getAD_Client_ID() +
								// Línea incluida por Carmen Capote//
								" AND W.NAME <> 'ASECON'   and W.NAME <> '*'   and W.NAME <> 'CENTROBECO' AND " +
								//
								"w.AD_Org_ID NOT IN " +
									" (SELECT XX_Org_ID " +
									" FROM XX_ProductPercentDistrib " +
									" WHERE C_OrderLine_ID = "+ detail + "))) tab");	
				}
				
			}//Fin if
		}   //  tableInit
	
	
		/**
		*  Fill the table using m_sql
		*  @param table table
		*/
		private void tableLoad (){
			String sql = MRole.getDefault().addAccessSQL(
				m_sql.toString()+" WHERE 1=1 ", "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
				+ m_groupBy + " ORDER BY tab.value";
			log.finest(sql);
			//System.out.println("Distrib percentage: "+sql);
			try	{
				Statement stmt = DB.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				xProductTable.loadTable(rs);
				stmt.close();
			}//try
			catch (SQLException e){
				log.log(Level.SEVERE, sql, e);
			}
		}//tableLoad
	
		public static Ctx setContext(Ctx ctx){
			return context = ctx;
		}
			
		public static int setRecord(int rec){
			return record = rec;
		}
			
		public static Trx setTransaction(Trx trx){
			return transaction = trx;
		}

}// Fin DistribPercentForm
