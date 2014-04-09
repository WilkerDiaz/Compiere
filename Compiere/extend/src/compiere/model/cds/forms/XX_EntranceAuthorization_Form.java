package compiere.model.cds.forms;
 
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.*;
import org.compiere.model.MRole;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderStatus;
import compiere.model.cds.X_XX_VCN_OrderFiscalRMotive;

/**
 *  Autorizacion de Entrada
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_EntranceAuthorization_Form extends CPanel
	
	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener
	{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_EntranceAuthorization_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private CTextField orderToSearch = new CTextField();
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CLabel DocNumber = new CLabel(Msg.translate(Env.getCtx(), "C_Order_ID"));
	private CButton bSearch = new CButton();
	private CButton bConfirm = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private JScrollPane xScrollPane2 = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder(Msg.translate(Env.getCtx(), "C_Order_ID"));
	private TitledBorder xBorder2 = new TitledBorder(Msg.translate(Env.getCtx(), "XX_FiscalDataRMotives"));
	private MiniTable xTable = new MiniTable();
	private MiniTable xTable2 = new MiniTable();
	private CPanel xPanel = new CPanel();
	private CCheckBox xAuthorized = new CCheckBox(Msg.translate(Env.getCtx(), "XX_CorrectData"));
	private VDate xInvoiceDate = new VDate("DateFrom", false, false, true, DisplayTypeConstants.Date, "DateFrom");
	private CLabel xInvoiceDateLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_InvoiceDate"));
	private static int tableInit_option;
	private final int orderTable = 259;
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private HashMap<Integer,Integer>	m_Attachments = null;

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
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);

		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bSearch.setPreferredSize(new Dimension(90,22));	
		bSearch.setEnabled(true);
		
		bConfirm.setText(Msg.translate(Env.getCtx(), "XX_Confirm"));
		bConfirm.setPreferredSize(new Dimension(90,22));	
		bConfirm.setEnabled(false);
		
		xInvoiceDate.setPreferredSize(new Dimension(110,20));
		xInvoiceDate.setVisible(false);
		
		xInvoiceDateLabel.setVisible(false);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(900, 445));
		
		xScrollPane2.setBorder(xBorder2);
		xScrollPane2.setVisible(false);
		xScrollPane2.setPreferredSize(new Dimension(900, 150));
		xAuthorized.setVisible(false);
		bConfirm.setVisible(false);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		centerPanel.add(xScrollPane2,  BorderLayout.SOUTH);

		xScrollPane.getViewport().add(xTable, null);
		xScrollPane2.getViewport().add(xTable2, null);
		
		northPanel.add(DocNumber,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 10, 5), 0, 0));

		orderToSearch.setPreferredSize(new Dimension(120,20));
		orderToSearch.setMaxLength(14);
		
		northPanel.add(orderToSearch,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 10, 5), 0, 0));
		
		northPanel.add(bSearch,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 10, 5), 0, 0));
	   
	    southPanel.add(xInvoiceDateLabel,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 60, 760), 0, 0));
	    
	    southPanel.add(xInvoiceDate,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 60, 520), 0, 0));
	    
	    southPanel.add(xAuthorized,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 774), 0, 0));
		
	    
	    southPanel.add(bConfirm,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(60, 0, 10, 0), 0, 0));
	    
	    southPanel.validate();

	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),   ".", KeyNamePair.class),        //  1
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", KeyNamePair.class),     //  2
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_EstimatedDate"),   ".", String.class),		//3
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_OrderType"),   ".", String.class),  			//  4
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VLO_TypeDelivery"),   ".", String.class),  			//  5
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Returns"),   ".", String.class), 			//  6
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_IsAuthorized"),   ".", String.class),  			//  7
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_EntranceDate"),   ".", String.class),  			//  8
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_EntranceHour"),   ".", String.class)  			//  8
			
		};
		
			
		ColumnInfo[] layout2 = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "Select"),   ".",  Boolean.class, false, false,""),            //  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_CancellationMotive_ID"),   ".", KeyNamePair.class), //2
		};

		xTable.prepareTable(layout, "", "", false, "");
		//xTable.getColumnModel().getColumn(3).setMaxWidth(250);
		//xTable.getColumnModel().getColumn(5).setMaxWidth(120);
		xTable.setAutoResizeMode(3);
		
		xTable2.prepareTable(layout2, "", "", true, "");
		xTable2.getColumnModel().getColumn(0).setMaxWidth(95);
		xTable2.getColumnModel().getColumn(0).setMinWidth(95);
		xTable2.setAutoResizeMode(3);
		
		//  Visual
		CompiereColor.setBackground (this);

		//  Listener
		xTable.getSelectionModel().addListSelectionListener(this);

		xTable2.addMouseListener(new MouseListener() {
			
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				for(int i=0; i<1; i++){
					for(int j=0; j<xTable2.getRowCount(); j++){
						if(new Boolean(xTable2.getModel().getValueAt(j, i).toString())){
							xAuthorized.setSelected(false);
						}
					}
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			
				for(int i=0; i<1; i++){
					for(int j=0; j<xTable2.getRowCount(); j++){
						if(new Boolean(xTable2.getModel().getValueAt(j, i).toString())){
							xAuthorized.setSelected(false);
						}
					}
				}
			}
		});
		
		orderToSearch.addKeyListener (new KeyAdapter () {
			          
			public void keyPressed (KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		             cmd_Search();
		         }
			}
	     } // end anonymous class
		);
		
		bSearch.addActionListener(this);
		bConfirm.addActionListener(this);
		xAuthorized.addActionListener(this);
			
		//Cargo la tabla 1
		tableInit_option=0;
		tableInit(orderToSearch);
		tableLoad (xTable);
		
		//Cargo la tabla 2
		tableInit_option=2;
		tableInit(orderToSearch);
		tableLoad (xTable2);
		
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_POListedQty"));
		statusBar.setStatusDB(xTable.getRowCount());
		
	}   //  dynInit

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
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSearch)
			cmd_Search();
		else if (e.getSource() == bConfirm)
			cmd_Confirm();
		else if (e.getSource() == xAuthorized)
			cmd_Check();
		
	}   //  actionPerformed

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{
		
		orderToSearch.setValue(orderToSearch.getText().trim());
		
		if(orderToSearch.getText().equals("")){
			tableInit_option=0;
			tableInit(orderToSearch);
			tableLoad (xTable);
			
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_POListedQty"));
			statusBar.setStatusDB(xTable.getRowCount());
			
		}else{
			tableInit_option=1;
			tableInit(orderToSearch);
			tableLoad (xTable);
			
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_POListedQty"));
			statusBar.setStatusDB(xTable.getRowCount());
			
			if(xTable.getRowCount()==0){
				showOrderCondition();
			}
		}
		
	}   //  cmd_Search
	
	/**
	 *  xAuthorized check Pressed
	 */
	private void cmd_Check()
	{
		
		if(xAuthorized.isSelected()){
			for(int i=0; i<1; i++){
				for(int j=0; j<xTable2.getRowCount(); j++){
					xTable2.getModel().setValueAt(false, j, i);
				}
			}
		}
		
	} 
	
	/**
	 *  Confirm Button Pressed
	 */
	private void cmd_Confirm()
	{
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()), null);
		
		if(order.getXX_OrderType().equals("Importada")){
			
			sendMailImportedToCategoryManager();
			sendMailImportedToBuyer();
			
			order.setXX_Authorized(true);
			Calendar now = Calendar.getInstance();
			order.setXX_EntranceDate(new Timestamp(now.getTimeInMillis()));
			order.setXX_EntranceUser_ID(Env.getCtx().getAD_User_ID());
			order.save();
				
			//Cargo la tabla 1
			tableInit_option=0;
			tableInit(orderToSearch);
			tableLoad (xTable);
		
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_OrderAuthorized", new String[] {"'"+orderKey.getName()+"'"}));
			
		}
		else{//si orden nacional
			
			boolean reject=false;
			for(int i=0; i<1; i++){
				for(int j=0; j<xTable2.getRowCount(); j++){
					if(new Boolean(xTable2.getModel().getValueAt(j, i).toString())){
						reject=true;
					}
				}
			}
			
			if(xAuthorized.isSelected() && reject==false){

				//Si la O/C es de Despacho Directo
				if(order.getXX_VLO_TypeDelivery().equals("DD")){
					
					//Verifico que la fecha de la factura no sea 0
					if(xInvoiceDate.getTimestamp()!=null){
						//Verifico si tiene problemas en las fechas
						if(validDate()){
							
							if(authorized()){
								
								//Mando los correos correspondientes
								sendMailNationalAuthorized();
								
								ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_OrderAuthorized", new String[] {"'"+orderKey.getName()+"'"}));	
								
								//Cargo la tabla 1
								tableInit_option=0;
								tableInit(orderToSearch);
								tableLoad (xTable);
							}
							
						}else{
							notAuthorized(true,false);
							
							//Mando los correos correspondientes
							sendMailNationalNotAuthorized();
							
							//Cargo la tabla 1
							tableInit_option=0;
							tableInit(orderToSearch);
							tableLoad (xTable);
							
							ADialog.error(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_OrderNotAuthorizedDate", new String[] {""+orderKey.getName()+""}));
						}
						
					}else{ //si la fecha es nula
						ADialog.warn(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_MustInsertInvoiceDate"));
					}
					
				}else{  //Si la O/C no es de Despacho Directo
					
					//Verifico si tiene problemas en las fechas
					if(validDate()){
						
						if(authorized()){
							
							//Mando los correos correspondientes
							sendMailNationalAuthorized();
							
							ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_OrderAuthorized", new String[] {"'"+orderKey.getName()+"'"}));	
							
							//Cargo la tabla 1
							tableInit_option=0;
							tableInit(orderToSearch);
							tableLoad (xTable);
						}
						
					}else{
						notAuthorized(true,false);
						
						//Mando los correos correspondientes
						sendMailBuyerCMNotAuthorized(1);
						
						//Cargo la tabla 1
						tableInit_option=0;
						tableInit(orderToSearch);
						tableLoad (xTable);
						
						ADialog.error(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_OrderNotAuthorizedDate", new String[] {""+orderKey.getName()+""}));
					}
				}
				
			}else if(reject){
				
				if(order.getXX_VLO_TypeDelivery().equals("DD")){
					//Verifico que la fecha de la factura no sea 0
					if(xInvoiceDate.getTimestamp()!=null){
						
						Vector<Integer> motives = getMotives();
						
						//agrego los motivos de datos fiscales a la O/C
						Trx trans = Trx.get("trans");
						for(int i=0; i<motives.size(); i++){
							X_XX_VCN_OrderFiscalRMotive orderMotives = new X_XX_VCN_OrderFiscalRMotive (Env.getCtx(), 0, trans);
							orderMotives.setC_Order_ID(order.get_ID());
							orderMotives.setXX_VMR_CancellationMotive_ID(motives.get(i));
							orderMotives.save();
						}
						trans.commit();
						
						//Verifico si ademas tiene problemas en las fechas
						if(validDate()){
							notAuthorized(false,true);  //fechas bien y datos fiscales malos
						}else{
							notAuthorized(true,true);   //fechas mal y datos fiscales malos
						}
					
						//Mando los correos correspondientes
						sendMailNationalNotAuthorized();
						
						//Cargo la tabla 1
						tableInit_option=0;
						tableInit(orderToSearch);
						tableLoad (xTable);
						
						ADialog.error(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_OrderNotAuthorized", new String[] {""+orderKey.getName()+""}));
						
					}
					else{ //si la fecha es nula
						ADialog.warn(m_WindowNo, this.mainPanel, Msg.translate(Env.getCtx(), "XX_MustInsertInvoiceDate"));
					}
				}else{
					Vector<Integer> motives = getMotives();
					
					//agrego los motivos de datos fiscales a la O/C
					Trx trans = Trx.get("trans");
					for(int i=0; i<motives.size(); i++){
						X_XX_VCN_OrderFiscalRMotive orderMotives = new X_XX_VCN_OrderFiscalRMotive (Env.getCtx(), 0, trans);
						orderMotives.setC_Order_ID(order.get_ID());
						orderMotives.setXX_VMR_CancellationMotive_ID(motives.get(i));
						orderMotives.save();
					}
					trans.commit();
					
					//Verifico si ademas tiene problemas en las fechas
					if(validDate()){
						notAuthorized(false,true);  //fechas bien y datos fiscales malos
						//Mando los correos correspondientes
						sendMailBuyerCMNotAuthorized(2);
					}else{
						notAuthorized(true,true);   //fechas mal y datos fiscales malos
						//Mando los correos correspondientes
						sendMailBuyerCMNotAuthorized(3);
					}
				
					
					
					//Cargo la tabla 1
					tableInit_option=0;
					tableInit(orderToSearch);
					tableLoad (xTable);
					
					ADialog.error(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "XX_OrderNotAuthorized", new String[] {""+orderKey.getName()+""}));
				}
			}else{
				ADialog.beep();
				ADialog.warn(m_WindowNo, this.mainPanel, Msg.translate(Env.getCtx(), "XX_SelectFiscalMotive"));
			}
		}
	} 


	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		
		int tableRow = xTable.getSelectedRow();
			
		if(tableRow==-1) //Si no tengo nada seleccionado en la tabla de O/C
		{
			xInvoiceDateLabel.setVisible(false);
			xInvoiceDate.setVisible(false);
			bConfirm.setVisible(false);
			xAuthorized.setVisible(false);
			xScrollPane2.setVisible(false); //escondo el panel de motivos de rechazo
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_POListedQty"));
			statusBar.setStatusDB(xTable.getRowCount());
			bConfirm.setEnabled(false);
		}
		else
		{
			int orderRow = xTable.getSelectedRow();
			KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
			
			String authorized = xTable.getValueAt(orderRow, 6).toString();
			
			if((authorized.equals("No") || authorized.equals("-")) && Env.getCtx().getAD_Role_ID()!=Env.getCtx().getContextAsInt("#XX_L_VIGILANTROLE_ID")){
				xAuthorized.setSelected(false);
				
				for(int i=0; i<1; i++){
					for(int j=0; j<xTable2.getRowCount(); j++){
						xTable2.getModel().setValueAt(false, j, i);
					}
				}
				
				MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()),null);
				
				if(order.getXX_OrderType().equals("Nacional")){
					
					if(order.getXX_VLO_TypeDelivery().equals("DD")){
						xInvoiceDateLabel.setVisible(true);
						xAuthorized.setVisible(true);
						xInvoiceDate.setVisible(true);
						xScrollPane2.setVisible(true); //muestro el panel de motivos de rechazo
					}else{
						xInvoiceDateLabel.setVisible(false);
						xAuthorized.setVisible(true);
						xInvoiceDate.setVisible(false);
						xScrollPane2.setVisible(true); //muestro el panel de motivos de rechazo
					}
				}
				else{
					xScrollPane2.setVisible(false);
					xInvoiceDateLabel.setVisible(false);
					xInvoiceDate.setVisible(false);
					xAuthorized.setVisible(false);
				}
				
				bConfirm.setVisible(true);
				statusBar.setStatusLine(Msg.translate(Env.getCtx(), "SelectedOrder"));
				statusBar.setStatusDB(orderKey.getName());
				bConfirm.setEnabled(true);
			}
			else{
				xInvoiceDateLabel.setVisible(false);
				xInvoiceDate.setVisible(false);
				bConfirm.setVisible(false);
				xAuthorized.setVisible(false);
				xScrollPane2.setVisible(false); //escondo el panel de motivos de rechazo
				bConfirm.setEnabled(false);
				statusBar.setStatusLine(Msg.translate(Env.getCtx(), "SelectedOrder"));
				statusBar.setStatusDB(orderKey.getName());
			}
		}
		
		if (e.getValueIsAdjusting())
			return;

	}   //  valueChanged

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
	private static void tableInit (CTextField orderToSearch)
	{
		m_sql = new StringBuffer ();

		if(tableInit_option==0)
		{
			
			m_sql.append("SELECT tab.DOCUMENTNO, tab.C_Order_ID, part.Name, part.C_BPartner_ID, TO_CHAR(XX_EstimatedDate,'DD/MM/YYYY'), " +
					"tab.XX_OrderType, "+
					"CASE "+
					"WHEN XX_VLO_TypeDelivery='CD' THEN 'Centro de Distribución' "+
					"WHEN XX_VLO_TypeDelivery='PD' THEN 'Pre-Distribuida' " +
					"WHEN XX_VLO_TypeDelivery='DD' THEN 'Despacho Directo'  "+
					"ELSE '-' END, "+
					" (SELECT CASE count(*) " +
				     "WHEN 0 THEN 'No' " +
				     "ELSE 'Si' END " +
				     "FROM XX_VLO_RETURNOFPRODUCT WHERE C_BPartner_ID=part.C_BPartner_ID AND XX_STATUS='DPR') as returns," +
				     
				     "CASE " +
				     "WHEN tab.XX_Authorized='Y' AND tab.XX_NotAuthorized='Y' THEN 'Si' "+
				     "WHEN tab.XX_Authorized='Y' AND tab.XX_NotAuthorized='N' THEN 'Si' "+
				     "WHEN tab.XX_Authorized='N' AND tab.XX_NotAuthorized='Y' THEN 'No' "+
				     "WHEN tab.XX_Authorized='N' AND tab.XX_NotAuthorized='N' THEN '-' "+
				     "END as Authorized, "+
				     "TO_CHAR(tab.XX_EntranceDate, 'DD-MM-YYYY'), " +
				     "TO_CHAR(tab.XX_EntranceDate, 'HH:MI AM') "
				+ "FROM C_Order tab, C_BPartner part "
				+ "WHERE tab.IssoTrx = 'N' and ((XX_VLO_TYPEDELIVERY='DD' AND UPPER(XX_ORDERSTATUS)=UPPER('CH') AND XX_ProcessReceptionStore = 'N' and XX_OrderType='Nacional' and tab.DocStatus='CO') "
				+ "or (UPPER(XX_ORDERSTATUS)=UPPER('AP') and XX_OrderType='Nacional' and tab.DocStatus='CO') "
				+ "or (UPPER(XX_ORDERSTATUS)=UPPER('ETN') and XX_OrderType='Importada' and tab.DocStatus='DR' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +") "
				+ "or (UPPER(XX_ORDERSTATUS)=UPPER('LCD') and XX_OrderType='Importada' and tab.DocStatus='DR' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +") "
				+ "or (UPPER(XX_ORDERSTATUS)=UPPER('PEN') and XX_OrderType='Importada' and tab.DocStatus='DR' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +") "
				+ "or (UPPER(XX_ORDERSTATUS)=UPPER('AP') and XX_OrderType='Importada' and tab.DocStatus='CO' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +"))"
				+ "and part.C_BPartner_ID=tab.C_BPartner_ID "
				+ "and tab.XX_POTYPE='POM' AND TAB.M_WAREHOUSE_ID=(SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE W WHERE W.AD_ORG_ID=" + Env.getCtx().getAD_Org_ID() + ")");
			
			if(Env.getCtx().getAD_Role_ID()==Env.getCtx().getContextAsInt("#XX_L_VIGILANTROLE_ID")){
				m_sql.append("and TO_CHAR(tab.XX_EntranceDate, 'DD-MM-YYYY')=TO_CHAR(Sysdate, 'DD-MM-YYYY') ");
			}
			
				m_orderBy = " order by tab.C_Order_ID desc";
				
		}
		else if(tableInit_option==1)  
		{
			m_sql.append("SELECT tab.DOCUMENTNO, tab.C_Order_ID, part.Name, part.C_BPartner_ID, TO_CHAR(XX_EstimatedDate,'DD/MM/YYYY'), " +
					     "tab.XX_OrderType, " +
					     "CASE "+
							"WHEN XX_VLO_TypeDelivery='CD' THEN 'Centro de Distribución' "+
							"WHEN XX_VLO_TypeDelivery='PD' THEN 'Pre-Distribuida' " +
							"WHEN XX_VLO_TypeDelivery='DD' THEN 'Despacho Directo'  "+
							"ELSE '-' END, "+
					     " (SELECT CASE count(*) " +
					     "WHEN 0 THEN 'No' " +
					     "ELSE 'Si' END " +
					     "FROM XX_VLO_RETURNOFPRODUCT WHERE C_BPartner_ID=part.C_BPartner_ID AND XX_STATUS='DPR') as returns, "+
					     "CASE " +
					     "WHEN tab.XX_Authorized='Y' AND tab.XX_NotAuthorized='Y' THEN 'Si' "+
					     "WHEN tab.XX_Authorized='Y' AND tab.XX_NotAuthorized='N' THEN 'Si' "+
					     "WHEN tab.XX_Authorized='N' AND tab.XX_NotAuthorized='Y' THEN 'No' "+
					     "WHEN tab.XX_Authorized='N' AND tab.XX_NotAuthorized='N' THEN '-' "+
					     "END as Authorized, "+
					     "TO_CHAR(tab.XX_EntranceDate, 'DD-MM-YYYY'), " +
					     "TO_CHAR(tab.XX_EntranceDate, 'HH:MI AM') " 
					+ "FROM C_Order tab, C_BPartner part "
					+ "WHERE tab.IssoTrx = 'N' and ((XX_VLO_TYPEDELIVERY='DD' AND UPPER(XX_ORDERSTATUS)=UPPER('CH') AND XX_ProcessReceptionStore = 'N' and XX_OrderType='Nacional' and tab.DocStatus='CO') "
					+ "or (XX_VLO_TYPEDELIVERY != 'DD' AND UPPER(XX_ORDERSTATUS)=UPPER('AP') and XX_OrderType='Nacional' and tab.DocStatus='CO') "
					+ "or (UPPER(XX_ORDERSTATUS)=UPPER('ETN') and XX_OrderType='Importada' and tab.DocStatus='DR' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +") "
					+ "or (UPPER(XX_ORDERSTATUS)=UPPER('LCD') and XX_OrderType='Importada' and tab.DocStatus='DR' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +") "
					+ "or (UPPER(XX_ORDERSTATUS)=UPPER('PEN') and XX_OrderType='Importada' and tab.DocStatus='DR' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +") "
					+ "or (UPPER(XX_ORDERSTATUS)=UPPER('AP') and XX_OrderType='Importada' and tab.DocStatus='CO' AND tab.XX_ImportingCompany_ID = "+ Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") +"))"
					+  "and tab.DOCUMENTNO='"+orderToSearch.getText()+"' "
					+  "and part.C_BPartner_ID=tab.C_BPartner_ID "
					+ "and tab.XX_POTYPE='POM' AND TAB.M_WAREHOUSE_ID=(SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE W WHERE W.AD_ORG_ID=" + Env.getCtx().getAD_Org_ID() + ")");
		
			
			if(Env.getCtx().getAD_Role_ID()==Env.getCtx().getContextAsInt("#XX_L_VIGILANTROLE_ID")){
				m_sql.append("and TO_CHAR(tab.XX_EntranceDate, 'DD-MM-YYYY')=TO_CHAR(Sysdate, 'DD-MM-YYYY') ");
			}
			
					m_orderBy = "";
		}
		else if(tableInit_option==2)
		{
			m_sql.append("SELECT 'N',tab.Name, tab.XX_VMR_CANCELLATIONMOTIVE_ID "
					+ "FROM XX_VMR_CANCELLATIONMOTIVE tab, XX_VMR_MOTIVEGROUP mg " +
					  "WHERE mg.XX_VMR_MOTIVEGROUP_ID=tab.XX_VMR_MOTIVEGROUP_ID " +
					  "AND mg.XX_USE='ENTRANCE'");
					
					m_orderBy = " order by tab.Name";
		}
		
	}   //  tableInit

	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{
	//	log.finest(m_sql + " - " +  m_groupBy);
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ m_groupBy + m_orderBy;
		
		log.finest(sql);
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			table.loadTable(rs);

			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}   //  tableLoad
	
	/**
	 *  Shows the searched order condition
	 */
	private void showOrderCondition()
	{
		String SQL ="SELECT tab.DocStatus, tab.XX_OrderStatus, tab.XX_Authorized, tab.XX_NotAuthorized, tab.XX_OrderType, tab.XX_ImportingCompany_ID "+
					"FROM C_Order tab "+
					"WHERE DOCUMENTNO='"+orderToSearch.getText()+"'";
		
		SQL = MRole.getDefault().addAccessSQL(
			SQL.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		
		String status=null;
		String authorized=null;
		String compiereStatus="";
		String orderType = "";
		int importingCompany = 0;
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();	
				
			if(rs.next())
			{	
				status = rs.getString("XX_OrderStatus");
				authorized = rs.getString("XX_Authorized");
				compiereStatus = rs.getString("DocStatus");
				orderType = rs.getString("XX_OrderType");
				importingCompany = rs.getInt("XX_ImportingCompany_ID");
			}
							
			rs.close();
			pstmt.close();
							
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, SQL, e);
		}
		
		//Muestro el mensaje segun la condicion
		if(status==null)
		{
			ADialog.warn(m_WindowNo, this.mainPanel, "La orden '"+orderToSearch.getText()+"' no existe");
		}else if(status.equals("RE"))
		{
			ADialog.warn(m_WindowNo, this.mainPanel, "La orden '"+orderToSearch.getText()+"' ya ha sido recibida");
		}
		else if (authorized.equals("Y"))
		{
			ADialog.warn(m_WindowNo, this.mainPanel, "La orden '"+orderToSearch.getText()+"' ya ha sido autorizada");
		}
		else if(orderType.equals("Importada") && importingCompany != Env.getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID"))
		{
			ADialog.warn(m_WindowNo, this.mainPanel, "La orden '"+orderToSearch.getText()+"' (SITME) no puede ser autorizada, debe autorizar en su reemplazo la orden: '"+ getNationalSITME(orderToSearch.getText()) +"' ");
		}
		else
		{
			if(status.equals("LVE")){
				status = X_Ref_XX_OrderStatus.LLEGADAAVENEZUELA.name();
			}
			else if(status.equals("EP")){
				status = X_Ref_XX_OrderStatus.ENPRODUCCIÓN.name();
			}
			else if(status.equals("EAC")){
				status = X_Ref_XX_OrderStatus.ENTREGADAALAGENTEDECARGA.name();
			}
			else if(status.equals("ETI")){
				status = X_Ref_XX_OrderStatus.ENTRÁNSITOINTERNACIONAL.name();
			}
			else if(status.equals("EPN")){
				status = X_Ref_XX_OrderStatus.ENPROCESODENACIONALIZACIÓN.name();
			}
			else if(status.equals("EA")){
				status = X_Ref_XX_OrderStatus.ENADUANA.name();
			}
			
			if(compiereStatus.equals("IN")){
				status+= " (Orden Inválida)";
			}
			
			ADialog.warn(m_WindowNo, this.mainPanel, "La orden '"+orderToSearch.getText()+"' está en el estado '"+status+"' y no puede ser recibida");
		}
		
	}  
	
	/**
	 *  Envia correo al Gerente de tienda y al Asesor de Inventario si la O/C fue autorizada
	 */
	private void sendMailNationalAuthorized()
	{
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()), null);
		
		//Al Gerente de Tienda
		String Mensaje = " <"+orderKey.getName()+"> fue autorizada (entrada).";
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		Mensaje += "\n\nProveedor: " + vendor.getName() + "\nDepartamento: "+dep.getValue();
		
		
		//Selecciono el o los gerentes de Tienda
		String SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
					"AND C_BPARTNER_ID IN "+
					"("+
						"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
						"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_STOREMAN_ID")+" " +
						"AND M_WAREHOUSE_ID IN (SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE WHERE AD_ORG_ID="+order.getAD_Org_ID()+") " +
						"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
					") "+
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		
		
    	Vector<Integer> storeManagers = new Vector<Integer>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				storeManagers.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los gerentes
		Utilities f = null;
		for(int i=0; i<storeManagers.size();i++){
	
			f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_AUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
			try {
				f.ejecutarMail(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
		
		//*********************************
		//Selecciono los asesores de inventario  
    	SQL = "SELECT AD_USER_ID FROM AD_USER WHERE  ISACTIVE='Y' " +
    			"AND C_BPARTNER_ID IN "+
				"("+
					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
					"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_INVASSES_ID")+" " +
					"AND M_WAREHOUSE_ID IN (SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE WHERE AD_ORG_ID="+order.getAD_Org_ID()+") " +
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
				") "+
				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";			
		
    	Vector<Integer> warehouseAsessors = new Vector<Integer>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				warehouseAsessors.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los asesores de inventario
		Utilities m = null;
		for(int i=0; i<warehouseAsessors.size();i++){
			
			m = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_AUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, warehouseAsessors.get(i),null);
			try {
				m.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			m = null;
		}
		
		//***
		//Envio correo de la fecha estimada de la proxima entrega
		sendMailNationalNextDelivery(order.getAD_Org_ID(), storeManagers, warehouseAsessors);
		
	}
	
	/**
	 *  Envia correo al Administrador de tienda y al Asesor de Almacen si la O/C no fue autorizada
	 */
	private void sendMailNationalNotAuthorized()
	{
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()), null);
		
		//Al Gerente de Tienda
		String Mensaje = " <"+orderKey.getName()+"> no fue autorizada (entrada).";
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		Mensaje += "\n\nProveedor: " + vendor.getName() + "\nDepartamento: "+dep.getValue();
		
		//Selecciono el o los gerentes de Tienda
    	String SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
    				"AND C_BPARTNER_ID IN "+
    				"("+
    					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
    					"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_STOREMAN_ID")+" " +
    					"AND M_WAREHOUSE_ID IN (SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE WHERE AD_ORG_ID="+order.getAD_Org_ID()+")" +
    					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
    				") "+
    				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		
		
    	Vector<Integer> storeManagers = new Vector<Integer>();
    	
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				storeManagers.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los gerentes
		Utilities f = null;
		for(int i=0; i<storeManagers.size();i++){
	
			f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_NOTAUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
			try {
				f.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
		
		//*********************************
		//Selecciono los asesores de almacen
		SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
				"AND C_BPARTNER_ID IN "+
				"("+
					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
					"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_DEPASE_ID")+" " +
					"AND M_WAREHOUSE_ID IN (SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE WHERE AD_ORG_ID="+order.getAD_Org_ID()+")" +
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
				") "+
				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		
		
    	Vector<Integer> warehouseAsessors = new Vector<Integer>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				warehouseAsessors.add(rs.getInt("AD_USER_ID"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		//Envio correos a los asesores
		Utilities m = null;
		for(int i=0; i<warehouseAsessors.size();i++){
	
			m = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_NOTAUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, warehouseAsessors.get(i),null);
			try {
				m.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			m = null;
		}
		
		//***
		//Envio correo de la fecha estimada de la proxima entrega
		sendMailNationalNextDelivery(order.getAD_Org_ID(), storeManagers, warehouseAsessors);
	}
	
	/**
	 *  Envia correo al Administrador de tienda avisandole la proxima entrega
	 */
	private void sendMailNationalNextDelivery(int orgID, Vector<Integer> storeManagers, Vector<Integer> warehouseAsessors)
	{
		String SQL = "SELECT TO_CHAR(MIN(XX_EstimatedDate),'DD/MM/YYYY') " +
					 "FROM C_Order " + 
					 "WHERE IssoTrx = 'N' and "+ 
					 "SYSDATE-1<=XX_EstimatedDate "+
					 "AND AD_Org_ID="+orgID+" "+
					 "AND XX_Authorized='Y' " +
					 "AND XX_OrderStatus='AP' " +
					 "AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+") ";
		
		String nextDate = "";
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				nextDate = rs.getString("TO_CHAR(MIN(XX_EstimatedDate),'DD/MM/YYYY')");
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		String Mensaje = " "+nextDate+".";
		
		if(nextDate!=null){
		
			Utilities f = null;
			for(int i=0; i<storeManagers.size(); i++){

				f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_NEXTDELIVERY_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
				try {
					f.ejecutarMail();
				} catch (Exception e) {
					e.printStackTrace();
				}
				f = null;
			}
			
			for(int i=0; i<warehouseAsessors.size(); i++){
				
				f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_NEXTDELIVERY_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, warehouseAsessors.get(i),null);
				try {
					f.ejecutarMail();
				} catch (Exception e) {
					e.printStackTrace();
				}
				f = null;
			}
		}
	}
	
	
	private void sendMailImportedToBuyer()
	{
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()), null);
		
		Integer userBuyer = getAD_User_ID(order.getXX_UserBuyer_ID());
		
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		String Mensaje = " <"+orderKey.getName()+"> " + Msg.getMsg( Env.getCtx(), "XX_AuthotizedMailMsg", new String[]{vendor.getName(),dep.getValue()});
		
		Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_AUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, userBuyer,null);
		try {
			f.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
	}
	
	
	private void sendMailImportedToCategoryManager()
	{
		
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()), null);
		
		MVMRCategory category = new MVMRCategory(Env.getCtx(), order.getXX_Category_ID(), null);
		
		Integer categoryManager = getAD_User_ID(category.getXX_CategoryManager_ID());
		
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		String Mensaje = " <"+orderKey.getName()+"> " + Msg.getMsg( Env.getCtx(), "XX_AuthotizedMailMsg", new String[]{vendor.getName(),dep.getValue()});
		
		Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_AUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, categoryManager,null);

		try {
			f.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
	}
	
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner + " "+
					 "AND ISACTIVE='Y'";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return AD_User_ID;
	}
	
	
	/**
	 *  When Order Authorized
	 */
	private boolean authorized()
	{
		int orderRow = xTable.getSelectedRow();
		m_Attachments = null;
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()),null);
		
		xInvoiceDate.setValue(null);
		//Seteo los valores necesarios en la O/C
		
		if(order.getXX_VLO_TypeDelivery().equals("DD")){
			
			//Pregunto cuantos attachments tengo
			long countPrevious = getAttachmentsSize(order.get_ID());
			
			//Agrego el Attachment
			new Attachment(m_frame, m_WindowNo, getAD_AttachmentID(order.get_ID()), orderTable, order.get_ID(), null);
			
			//Pregunto cuantos attachments tengo ahora
			long countFinal = getAttachmentsSize(order.get_ID());
			
			if(countPrevious<countFinal){ // Si adjuntó la factura
				
				order.setXX_Authorized(true);
				Calendar now = Calendar.getInstance();
				order.setXX_EntranceDate(new Timestamp(now.getTimeInMillis()));
				order.setXX_EntranceUser_ID(Env.getCtx().getAD_User_ID());
				
				if(order.isXX_NotAuthorized()){
					order.setXX_Alert2("O/C Autorizada (Originalmente no se recibió por presentar inconsistencia)");
				}
				
				order.save();
				xInvoiceDate.setValue(null);
				return true;
				
			}else{
				ADialog.warn(m_WindowNo, this.mainPanel, Msg.translate(Env.getCtx(), "XX_MustAttachInvoice"));
				return false;
			}
			
		}else{
			order.setXX_Authorized(true);
			Calendar now = Calendar.getInstance();
			order.setXX_EntranceDate(new Timestamp(now.getTimeInMillis()));
			order.setXX_EntranceUser_ID(Env.getCtx().getAD_User_ID());
			
			if(order.isXX_NotAuthorized()){
				order.setXX_Alert2("O/C Autorizada (Originalmente no se recibió por presentar inconsistencia)");
			}
			
			order.save();
			xInvoiceDate.setValue(null);
			return true;
		}
		
	}
	
	/**
	 *  When Order Not Authorized
	 */
	private void notAuthorized(boolean error1, boolean error2){
		xInvoiceDate.setValue(null);
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()),null);
		
		//Seteo los valores necesarios en la O/C
		order.setXX_NotAuthorized(true);
		
		if(error1)
			order.setXX_EstimatedDiffersFromReal(true);
		
		if(error2){
			order.setXX_FiscalDataError(true);
		}
		
		Calendar now = Calendar.getInstance();
		order.setXX_EntranceDate(new Timestamp(now.getTimeInMillis()));
		order.setXX_RejectionDate(new Timestamp(now.getTimeInMillis()));
		order.setXX_Alert2("Orden de Compra No se Recibió por Presentar Inconsistencia.");
		order.save();
		
		/**
		 * Jorge Pires / Evaluacion de la O/C (cuando no la autorizo - 3)
		 **/
		Utilities aux = new Utilities();
		aux.ejecutarWeigth(order.get_ID(),0);
		order.setXX_Evaluated(true);
		order.save();
		aux=null;
	}
	
	/**
	 *  Valid Date
	 */
	private boolean validDate()
	{
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()),null);
		
		Timestamp invoiceDate = xInvoiceDate.getTimestamp();
		
		int days=10;
		
		if(order.getXX_VLO_TypeDelivery().equals("DD")){
			
			Calendar dateFrom = Calendar.getInstance();			
			dateFrom.setTimeInMillis(order.getXX_EstimatedDate().getTime());
			dateFrom.add(Calendar.DATE, -(days));
			
			Calendar dateUntil = Calendar.getInstance();			
			dateUntil.setTimeInMillis(order.getXX_EstimatedDate().getTime());
			dateUntil.add(Calendar.DATE, (days));
			
			if(dateFrom.getTimeInMillis()<=invoiceDate.getTime() && dateUntil.getTimeInMillis()>=invoiceDate.getTime()){
				return true;
			}
			else{
				return false;
			}
	   
		}else{

			return true;
		}
		
	}
	
	/**
	 *	Get Attachment_ID for current record.
	 *	@return ID or 0, if not found
	 */
	public int getAD_AttachmentID(Integer orderID)
	{
		if (m_Attachments == null)
			loadAttachments();
		if (m_Attachments.isEmpty())
			return 0;
		//
		Integer key = orderID;
		Integer value = m_Attachments.get(key);
		if (value == null)
			return 0;
		else
			return value.intValue();
	}	//	getAttachmentID
	
	/**************************************************************************
	 *	Load Attachments for this table
	 */
	public void loadAttachments()
	{

		String SQL = "SELECT AD_Attachment_ID, Record_ID FROM AD_Attachment "
			+ "WHERE AD_Table_ID=?";
		try
		{
			if (m_Attachments == null)
				m_Attachments = new HashMap<Integer,Integer>();
			else
				m_Attachments.clear();
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setInt(1, orderTable);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				Integer key = Integer.valueOf(rs.getInt(2));
				Integer value = Integer.valueOf(rs.getInt(1));
				m_Attachments.put(key, value);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "loadAttachments", e);
		}
		log.config("#" + m_Attachments.size());
	}	//	loadAttachment
	
	/*
	 *	Count the Attachments for this table and record
	 */
	private long getAttachmentsSize(Integer orderID){
		
		String SQL = "SELECT BINARYDATA FROM AD_Attachment "
			+ "WHERE AD_Table_ID="+orderTable+" "
			+ "AND Record_ID="+orderID;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				Blob file = rs.getBlob("BINARYDATA");
				
				if(file==null)
					continue;
			
				return(file.length());
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return 0;
	}
	
	/*
	 *	returns true si el proveedor tiene el beneficio de 5 dias
	 */
	private int daysBenefits(MOrder order){
				
		//Tengo que buscar en la matriz por su  Tipo de prov y su Rating
		MBPartner cBPartner = new  MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		Utilities ut = new Utilities();
		return ut.benefitVendorDiasFechaEntrega(cBPartner.get_ID());
	}
	
	
	/*
	 *	returns the selected motives
	 */
	private Vector<Integer> getMotives(){
		
		Vector<Integer> motives = new Vector<Integer>();
		
		for(int j=0; j<xTable2.getRowCount(); j++){
			if(new Boolean(xTable2.getModel().getValueAt(j, 0).toString())){
					
				KeyNamePair orderKey = (KeyNamePair)xTable2.getValueAt(j, 1);	
				motives.add(orderKey.getKey());
			}
		}
		
		return motives;
	}

	
	@Override
	public void tableChanged(TableModelEvent e) {
	}
	
	/**
	 *  Envia correo al Comprador y al Jefe de Categoria si la O/C no fue autorizada
	 */
	private void sendMailBuyerCMNotAuthorized(int motive)
	{
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		
		String Mensaje = " <"+orderKey.getName()+"> no fue autorizada (entrada).";
		MOrder order = new MOrder(Env.getCtx(), new Integer(orderKey.getID()), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		
		Mensaje += "\n\nProveedor: "+vendor.getName()+"\nDepartamento: "+dep.getValue();
		
		if(motive==1){
			Mensaje+="\n\nMotivo:\n\n- Discrepancia de Fechas";
		}else if(motive==2){
			Mensaje+="\n\nMotivo(s):\n\n";
			for(int i=0; i<xTable2.getRowCount(); i++){
				if((Boolean)xTable2.getValueAt(i, 0)){
					int orderRow2 = xTable2.getSelectedRow();
					KeyNamePair motiveAux = (KeyNamePair)xTable2.getValueAt(orderRow2, 1);
					Mensaje+="- "+motiveAux.getName()+"\n";	
				}
			}
		}else{
			Mensaje+="\n\nMotivo(s):\n\n- Discrepancia de Fechas\n";
			for(int i=0; i<xTable2.getRowCount(); i++){
				if((Boolean)xTable2.getValueAt(i, 0)){
					KeyNamePair motiveAux = (KeyNamePair)xTable2.getValueAt(orderRow, 1);
					Mensaje+="- "+motiveAux.getName()+"\n";	
				}
			}
		}
		
		int buyer = getAD_User_ID(order.getXX_UserBuyer_ID());
		
		//Envio correo al comprador
		Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_NOTAUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, buyer,null);
		try {
			f.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//*********************************
		//jefe de categoria
		MVMRCategory category = new MVMRCategory( Env.getCtx(), order.getXX_Category_ID(), null);
		int categoryMan = getAD_User_ID(category.getXX_CategoryManager_ID());				

		//Envio correos a los asesores
		Utilities m = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_NOTAUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, categoryMan,null);
		try {
			m.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getNationalSITME(String sitmeOrder){
		
		String documentNo = "";
		
		String SQL = "SELECT DOCUMENTNO FROM C_ORDER WHERE " +
					 "REF_ORDER_ID = (SELECT C_ORDER_ID FROM C_ORDER WHERE DOCUMENTNO='"+ sitmeOrder +"')"+
					 " AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		

		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				documentNo = rs.getString("DOCUMENTNO");
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		return documentNo;
	}
	
}//End form

