package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;


import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.processes.XX_PercentualDistributionRedistribution;

/**
 *
 *  @author     
 *  @version    
 */
public class XX_DistribByPercentageForm extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener
	{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_DistribByPercentageForm.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private CLabel LineRefProv_Label = new CLabel();
	private Integer  XX_VMR_DistributionHeader_ID = null;
	static  Ctx ctx_aux = new Ctx();
	static  Integer Product_ID;
	static  Integer associatedReference_ID;
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton bUpdate = new CButton();
	private CButton bGenerate = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xProductScrollPane = new JScrollPane();
	private TitledBorder xProductBorder = new TitledBorder(Msg.translate(Env.getCtx(), "XX_PercentageDistribution"));
	private MiniTable xProductTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	private Vector<Integer> warehouses = new Vector<Integer>();
	private Vector<BigDecimal> ware_house_percentages = new Vector<BigDecimal>();

	private static int tableInit_option;

	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	private BigDecimal sumaPorcentaje = new BigDecimal(0);

	private CLabel xMatchedLabel = new CLabel();
	private VNumber xMatched = new VNumber("xMatched", false, true, false, DisplayTypeConstants.Amount, "xMatched");
	
	private static Ctx context = null;
	private static int record = 0;
	private static Trx transaction = null;
	
	private X_XX_VMR_DistributionDetail detail = new X_XX_VMR_DistributionDetail(context, record, transaction);
	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		XX_VMR_DistributionHeader_ID = (Env.getCtx()).getContextAsInt("#XX_VMR_DISTRIBUTIONHEADER_ID");
		
	   	(Env.getCtx()).remove("#XX_VMR_DISTRIBUTIONHEADER_ID");
	    
		try
		{
			//	UI
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			//
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init


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
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		
	
		bUpdate.setText(Msg.translate(Env.getCtx(), "XX_ReloadPercentages"));
		bUpdate.setEnabled(true);
		southPanel.setLayout(southLayout);
		bGenerate.setText(Msg.translate(Env.getCtx(), "XX_GeneratePieces"));
		bGenerate.setEnabled(false);
		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(450, 200));
		
		xPanel.setLayout(xLayout);
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		northPanel.add(LineRefProv_Label,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		
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
	
	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{		
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WarehouseName"),   ".", KeyNamePair.class),             			//  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PercentageAssigned"),   ".", BigDecimal.class, false, false, null)  //  2
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
		tableInit(detail.getXX_VMR_DistributionHeader_ID(), detail.getXX_VMR_DistributionDetail_ID());
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

	
	/**************************************************************************
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
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_WarehouseName"),   ".", KeyNamePair.class),             			//  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PercentageAssigned"),   ".", BigDecimal.class, false, false, null)  //  2
		};
		
		xProductTable.prepareTable(layout, "", "", false, "");
		
		//  Visual
		CompiereColor.setBackground (this);
		
		//  Listener
		xProductTable.getSelectionModel().addListSelectionListener(this);
		xProductTable.getModel().addTableModelListener(this);

		tableInit_option = 0;
		tableInit(detail.getXX_VMR_DistributionHeader_ID(), detail.getXX_VMR_DistributionDetail_ID());
		tableLoad ();
				
		xProductTable.getColumnModel().getColumn(0).setPreferredWidth(292);
		xProductTable.getColumnModel().getColumn(1).setPreferredWidth(130);
	}   //  cmd_update

	/**
	 *  Process Button Pressed - Process Matching
	 */
	private void cmd_Generate(){
				
		Vector<Integer> codes = new Vector<Integer>();
		Vector<String>  stores = new Vector<String>();
		Vector<BigDecimal> percentages = new Vector<BigDecimal>();
		
		for(int i = 0; i < xProductTable.getRowCount(); i++){
			if ((xProductTable.getValueAt(i, 1) == null) 
				|| (((Number)xProductTable.getValueAt(i, 1)).doubleValue() == 0))
				continue;

			stores.add(((KeyNamePair) (xProductTable.getValueAt(i, 0))).getName());
			codes.add(((KeyNamePair) (xProductTable.getValueAt(i, 0))).getKey());
			percentages.add((BigDecimal)xProductTable.getValueAt(i, 1));			
		}
		boolean it_work = XX_PercentualDistributionRedistribution.applyDistribution(
				detail, percentages, codes, m_WindowNo , m_frame);
		if (it_work ) dispose();
		
	}
	
	
	/**************************************************************************
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

	
	/***************************************************************************
	 *  Table Model Listener - calculate matchd Qty
	 *  @param e event
	 */
	public void tableChanged (TableModelEvent e)
	{
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
				}
			}else{
				xMatched.setBackground(Color.YELLOW);
				bGenerate.setEnabled(false);
			}
		}
	}   //  tableChanged

	
	/**************************************************************************
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
	private static void tableInit (int header, int detail){
		m_sql = new StringBuffer ();
		
		if(tableInit_option==0)
		{
			/*Se modificó el sql para que solo muestre el CD asociado a la distribución - PROYECTO CD VALENCIA*/
			/*Se modificó para que solo tome tiendas que reciban pedidos*/

			m_sql.append(   "SELECT tab.value||'-'||tab.name, tab.id, tab.percentage "+
							"FROM ((SELECT w.M_WAREHOUSE_ID id, w.value value, w.NAME name, per.XX_PERCENTAGE percentage, w.AD_ORG_ID AD_ORG_ID, w.AD_CLIENT_ID AD_CLIENT_ID" +
								    " FROM M_WAREHOUSE w, XX_VMR_STOREPERCENTDISTRIB per" +
								    " WHERE w.M_WAREHOUSE_ID = per.M_WAREHOUSE_ID AND w.ISACTIVE = 'Y' AND XX_NotReceiveOrder = 'N' " + 
								    " AND per.XX_VMR_DISTRIBUTIONDETAIL_ID = "+ detail + ") " + 
								    " UNION "+
								    "(SELECT w.M_WAREHOUSE_ID id, w.value value, w.NAME NAME, 0, w.AD_ORG_ID AD_ORG_ID, w.AD_CLIENT_ID AD_CLIENT_ID"+
								    " FROM M_WAREHOUSE w "+
								    " WHERE W.ISACTIVE = 'Y' AND w.M_WAREHOUSE_ID NOT IN (SELECT M_WAREHOUSE_ID " +
								                                   " FROM XX_VMR_STOREPERCENTDISTRIB " +
								                                   " WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = "+ detail + ") " +
								                                   		"AND W.XX_ISSTORE ='Y' AND XX_NotReceiveOrder = 'N' " + 
								     ") "+
								     " UNION "+
									    "(SELECT w.M_WAREHOUSE_ID id, w.value value, w.NAME NAME, 0, w.AD_ORG_ID AD_ORG_ID, w.AD_CLIENT_ID AD_CLIENT_ID"+
									    " FROM M_WAREHOUSE w "+
									    " WHERE W.ISACTIVE = 'Y' AND w.M_WAREHOUSE_ID NOT IN (SELECT M_WAREHOUSE_ID " +
									                                   " FROM XX_VMR_STOREPERCENTDISTRIB " +
									                                   " WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = "+ detail + ") AND w.M_WAREHOUSE_ID = " +Utilities.obtenerDistribucionCD(header) + 
									     ") "+
								") tab"	);
						
		}
	}   //  tableInit


	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private void tableLoad ()
	{
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ m_groupBy + " ORDER BY tab.value";
		
		log.finest(sql);		
		System.out.println(sql);
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			xProductTable.loadTable(rs);
			stmt.close();
		}
		catch (SQLException e)
		{
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

}//ConvertReference

