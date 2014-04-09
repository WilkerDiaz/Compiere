package compiere.model.payments.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.*;
import org.compiere.model.MRole;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

import compiere.model.birt.BIRTReport;

/**
 *  Consulta de Comprobantes
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_ISLRReceiptSearchForm extends CPanel
	
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
		m_WindowNo = WindowNo; //
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
	static CLogger 			log = CLogger.getCLogger(XX_ISLRReceiptSearchForm.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private CTextField receiptNo = new CTextField();
	private CLabel vendorLabel = new CLabel("Proveedor");
	private VComboBox comboBPartner = new VComboBox();
	private VDate xInvoicedDate = new VDate("DateFrom", false, false, true, DisplayTypeConstants.Date, "DateFrom");
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CLabel invDate = new CLabel(Msg.translate(Env.getCtx(), "XX_InvoiceDate"));
	private CLabel DocNumber = new CLabel("Numero de Comprobante");
	private CButton bSearch = new CButton();
	private CButton bReport = new CButton();
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
	private static int tableInit_option;
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);

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
		
		comboBPartner.setPreferredSize(new Dimension(240,20));
		xInvoicedDate.setPreferredSize(new Dimension(120,20));

		bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
		bSearch.setPreferredSize(new Dimension(90,22));	
		bSearch.setEnabled(true);
		
		bReport.setText(Msg.translate(Env.getCtx(), "XX_Show"));
		bReport.setPreferredSize(new Dimension(90,22));	
		bReport.setEnabled(false);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(900, 445));
		
		xScrollPane2.setBorder(xBorder2);
		xScrollPane2.setVisible(false);
		xScrollPane2.setPreferredSize(new Dimension(900, 220));
		bReport.setVisible(false);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		centerPanel.add(xScrollPane2,  BorderLayout.SOUTH);

		xScrollPane.getViewport().add(xTable, null);
		xScrollPane2.getViewport().add(xTable2, null);
		
		northPanel.add(DocNumber,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		northPanel.add(receiptNo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add(vendorLabel,    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 30, 0, 0), 0, 0));
		
		northPanel.add(comboBPartner,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		northPanel.add( invDate,	new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
		
		northPanel.add(xInvoicedDate,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
		
		receiptNo.setPreferredSize(new Dimension(120,20));
		receiptNo.setMaxLength(14);
		
		northPanel.add(bSearch,   new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 20, 10, 0), 0, 0));
	
	    southPanel.add(bReport,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
	    
	    southPanel.validate();

	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ISLRReceiptNo"),   ".", KeyNamePair.class),        //  1
			
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", String.class),     			  //  2		
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TypePersonISLR"),   ".", String.class),			  //3
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_InvoiceDate"),   ".", String.class),  			//  4
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_Invoice_ID"),   ".", String.class),  			 	//  5
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ControlNumber"),   ".", String.class), 			//  6
			
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PaymentType"),   ".", BigDecimal.class),  			//  7
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RetainedAmount"),   ".", BigDecimal.class)  			//  8
		};
		
			
		ColumnInfo[] layout2 = new ColumnInfo[] {          	        
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Base"),   ".", BigDecimal.class),  		//  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "Codigo"),   ".", String.class),  					//  3
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PaymentType"),   ".", String.class),  				//  4
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PercentOfRetention"),   ".", BigDecimal.class),  //  5
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_RetainedAmount"),   ".", BigDecimal.class)  			//  6
		};

		xTable.prepareTable(layout, "", "", false, "");
		xTable.getColumnModel().getColumn(0).setMaxWidth(80);
		//xTable.getColumnModel().getColumn(5).setMaxWidth(120);
		xTable.setAutoResizeMode(3);
		
		xTable2.prepareTable(layout2, "", "", true, "");
		xTable2.setAutoResizeMode(3);
		
		//  Visual
		CompiereColor.setBackground (this);

		//  Listener
		xTable.getSelectionModel().addListSelectionListener(this);
		
		receiptNo.addKeyListener (new KeyAdapter () {
			          
			public void keyPressed (KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		             cmd_Search();
		         }
			}
	     } // end anonymous class
		);
		
		bSearch.addActionListener(this);
		bReport.addActionListener(this);
			
		//Cargo la tabla 1
		tableInit_option=0;
		tableInit(receiptNo, xInvoicedDate, comboBPartner, 0);
		tableLoad (xTable);
		
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ReceiptsToShow"));
		statusBar.setStatusDB(xTable.getRowCount());
		
		loadVendor();
		
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
		else if (e.getSource() == bReport)
			cmd_ShowReport();
		
	}   //  actionPerformed

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{
		
		receiptNo.setValue(receiptNo.getText().trim());
		
		if(receiptNo.getText().equals("") && xInvoicedDate.getTimestamp()==null && (comboBPartner.getSelectedIndex()==0)){
			tableInit_option=0;
			tableInit(receiptNo, xInvoicedDate, comboBPartner, 0);
			tableLoad (xTable);
			
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ReceiptsToShow"));
			statusBar.setStatusDB(xTable.getRowCount());
			
		}else{
			tableInit_option=2;
			tableInit(receiptNo, xInvoicedDate, comboBPartner, 0);
			tableLoad (xTable);
			
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ReceiptsToShow"));
			statusBar.setStatusDB(xTable.getRowCount());
			
			if(xTable.getRowCount()==0){
				
			}
		}
		
	}   //  cmd_Search
	
	/**
	 *  Confirm Button Pressed
	 */
	private void cmd_ShowReport()
	{
		int tableRow = xTable.getSelectedRow();
		KeyNamePair invoice = (KeyNamePair)xTable.getValueAt(tableRow, 0);
		
		String designName = "ISLRReceipt";

		//Intanciar reporte
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("invoiceID");
		myReport.parameterValue.add(invoice.getKey());
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
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
			bReport.setVisible(false);
			xScrollPane2.setVisible(false); 
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ReceiptsToShow"));
			statusBar.setStatusDB(xTable.getRowCount());
			bReport.setEnabled(false);
		}
		else
		{
			KeyNamePair invoice = (KeyNamePair)xTable.getValueAt(tableRow, 0);
			
			//Cargo la tabla 2
			tableInit_option=1;
			tableInit(receiptNo, xInvoicedDate, comboBPartner, invoice.getKey());
			tableLoad (xTable2);
			
			xScrollPane2.setVisible(true);
			bReport.setVisible(true);
			statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ReceiptsToShow"));
			statusBar.setStatusDB(xTable.getRowCount());
			bReport.setEnabled(true);
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
	private static void tableInit (CTextField receiptNo, VDate xInvoicedDate, VComboBox comboBPartner, Integer invoice)
	{
		m_sql = new StringBuffer ();

		if(tableInit_option==0)
		{
			
			/*m_sql.append(
			
			"SELECT A.XX_ISLRRECEIPTNO, A.C_INVOICE_ID, D.XX_CI_RIF ||' - '||D.NAME, " +
			  "CASE " +
				"WHEN XX_TypePersonISLR = 'PJD' THEN 'Jurídica Domiciliada' " +
				"WHEN XX_TypePersonISLR = 'PJND' THEN 'Jurídica No Domiciliada' " +
				"WHEN XX_TypePersonISLR = 'PNR' THEN 'Persona Natural' " +
				"WHEN XX_TypePersonISLR = 'PNNR' THEN 'Pasaporte con Visa de Negocios' " + 
			  "ELSE ' ' END PersonISLR, " +
			       "TO_CHAR(C.XX_INVOICEDATE, 'DD/MM/YYYY') AS INVDATE, A.DOCUMENTNO, A.XX_CONTROLNUMBER AS NUMCONTROL, " +
			       "SUM(B.LINENETAMT), SUM(B.XX_RETAINEDAMOUNT) AS RETAINEDAMOUNT " +
			"FROM COMPIERE.C_INVOICE A, COMPIERE.C_INVOICELINE B, COMPIERE.C_ORDER C, COMPIERE.C_BPARTNER D " +
			"WHERE A.C_INVOICE_ID = B.C_INVOICE_ID AND B.XX_RETAINEDAMOUNT > 0 " +
			    "AND C.C_ORDER_ID = A.C_ORDER_ID AND D.C_BPARTNER_ID = A.C_BPARTNER_ID ");
			
			m_groupBy = "GROUP BY A.C_INVOICE_ID, A.XX_ISLRRECEIPTNO, D.XX_CI_RIF ||' - '||D.NAME, XX_TypePersonISLR, C.XX_INVOICEDATE, A.DOCUMENTNO, A.XX_CONTROLNUMBER";
			
			m_orderBy = " ORDER BY A.XX_ISLRRECEIPTNO, A.XX_ISLRRECEIPTNO desc";*/
			
			m_sql.append(
			
			"WITH RetainedAmount as ( "+
					"SELECT SUM(XX_RETAINEDAMOUNT) XX_RETAINEDAMOUNT, C_INVOICE_ID FROM XX_VCN_ISLRAMOUNT " +
					"GROUP BY C_INVOICE_ID " +
					")" +
			"SELECT A.XX_ISLRRECEIPTNO, A.C_INVOICE_ID, D.XX_CI_RIF ||' - '||D.NAME, " +
				"CASE " +
			        "WHEN XX_TypePersonISLR = 'PJD' THEN 'Jurídica Domiciliada' " +
			        "WHEN XX_TypePersonISLR = 'PJND' THEN 'Jurídica No Domiciliada' " +  
			        "WHEN XX_TypePersonISLR = 'PNR' THEN 'Persona Natural' " +
					"WHEN XX_TypePersonISLR = 'PNNR' THEN 'Pasaporte con Visa de Negocios' " +
					"ELSE ' ' END PersonISLR, " +
				"TO_CHAR(A.XX_APPROVALDATE, 'DD/MM/YYYY') AS INVDATE, A.DOCUMENTNO, A.XX_CONTROLNUMBER AS NUMCONTROL, " +
				"SUM(B.LINENETAMT), E.XX_RETAINEDAMOUNT AS RETAINEDAMOUNT " +
			"FROM COMPIERE.C_INVOICE A, COMPIERE.C_INVOICELINE B, COMPIERE.C_BPARTNER D, Retainedamount E " +
			"WHERE A.DOCSTATUS = 'CO' AND A.C_INVOICE_ID = B.C_INVOICE_ID " +
			"AND D.C_BPARTNER_ID = A.C_BPARTNER_ID " +
			"AND A.C_INVOICE_ID = E.C_INVOICE_ID AND B.XX_VCN_ISLRRETENTION_ID IS NOT NULL AND XX_ISLRRECEIPTNO > 0 ");
			
			m_groupBy = "GROUP BY A.C_INVOICE_ID, A.XX_ISLRRECEIPTNO, D.XX_CI_RIF ||' - '||D.NAME, XX_TypePersonISLR, A.XX_APPROVALDATE, A.DOCUMENTNO, A.XX_CONTROLNUMBER, E.XX_RETAINEDAMOUNT ";	
			m_orderBy = "ORDER BY A.XX_ISLRRECEIPTNO, A.XX_ISLRRECEIPTNO desc " ;
			
			System.out.println(m_sql + " " + m_groupBy);
		}
		else if(tableInit_option==1)
		{
			m_sql.append("SELECT  SUM(B.LINENETAMT), " +
						 "D.VALUE, D.NAME, D.XX_PERCENTOFRETENTION, " +
						 "E.XX_RETAINEDAMOUNT AS RETAINEDAMOUNT " +
						 "FROM COMPIERE.C_INVOICE A, COMPIERE.C_INVOICELINE B, " +
						 "COMPIERE.XX_VCN_ISLRRETENTION D, XX_VCN_ISLRAMOUNT E " +
						 "WHERE A.DOCSTATUS='CO' AND A.C_INVOICE_ID = B.C_INVOICE_ID AND E.XX_RETAINEDAMOUNT <> 0 " +
						 "AND D.XX_VCN_ISLRRETENTION_ID (+) = B.XX_VCN_ISLRRETENTION_ID " +
						 "AND A.C_INVOICE_ID = "+ invoice +" AND E.C_INVOICE_ID = A.C_INVOICE_ID " +
						 "AND E.XX_VCN_ISLRRETENTION_ID = D.XX_VCN_ISLRRETENTION_ID "		 
			);
			
			m_groupBy = "GROUP BY D.VALUE, D.NAME, D.XX_PERCENTOFRETENTION, E.XX_RETAINEDAMOUNT";
			m_orderBy = "";
		}
		else if(tableInit_option==2)
		{
			m_sql.append( "WITH RetainedAmount as ( "+
						    "SELECT SUM(XX_RETAINEDAMOUNT) XX_RETAINEDAMOUNT, C_INVOICE_ID FROM XX_VCN_ISLRAMOUNT " +
						    "GROUP BY C_INVOICE_ID " +
						  ")" +
						  "SELECT A.XX_ISLRRECEIPTNO, A.C_INVOICE_ID, D.XX_CI_RIF ||' - '||D.NAME, " +
						  "CASE " +
						  	"WHEN XX_TypePersonISLR = 'PJD' THEN 'Jurídica Domiciliada' " +
						  	"WHEN XX_TypePersonISLR = 'PJND' THEN 'Jurídica No Domiciliada' " +  
						  	"WHEN XX_TypePersonISLR = 'PNR' THEN 'Persona Natural' " +
						  	"WHEN XX_TypePersonISLR = 'PNNR' THEN 'Pasaporte con Visa de Negocios' " +
						  	"ELSE ' ' END PersonISLR, " +
						  "TO_CHAR(A.XX_APPROVALDATE, 'DD/MM/YYYY') AS INVDATE, A.DOCUMENTNO, A.XX_CONTROLNUMBER AS NUMCONTROL, " +
						  "SUM(B.LINENETAMT), E.XX_RETAINEDAMOUNT AS RETAINEDAMOUNT " +
						  "FROM COMPIERE.C_INVOICE A, COMPIERE.C_INVOICELINE B, COMPIERE.C_ORDER C, COMPIERE.C_BPARTNER D, Retainedamount E " +
						  "WHERE A.DOCSTATUS = 'CO' AND A.C_INVOICE_ID = B.C_INVOICE_ID " +
						  "AND C.C_ORDER_ID (+) = A.C_ORDER_ID AND D.C_BPARTNER_ID = A.C_BPARTNER_ID " +
						  "AND A.C_INVOICE_ID = E.C_INVOICE_ID AND B.XX_VCN_ISLRRETENTION_ID IS NOT NULL ");		    
					
					if(!receiptNo.getText().trim().equals("")){
						m_sql.append("AND A.XX_ISLRRECEIPTNO = " + receiptNo.getText());
					}    
					
					if(xInvoicedDate.getTimestamp()!=null){
						m_sql.append("AND TO_CHAR(C.XX_INVOICEDATE,'YYYY-MM-DD') = '" + xInvoicedDate.getTimestamp().toString().substring(0, xInvoicedDate.getTimestamp().toString().length()-11));
						m_sql.append("'");
					}    
					
					//busqueda por proveedor
					if(comboBPartner.getSelectedIndex()!=0 && comboBPartner.getSelectedItem() != null){
						if(((KeyNamePair)comboBPartner.getSelectedItem()).getKey() != 0){
							int clave_vendor=((KeyNamePair)comboBPartner.getSelectedItem()).getKey();
							m_sql.append(" AND A.C_BPartner_ID=").append(clave_vendor);
						}
					}
					
					m_sql.append("");
					
					m_groupBy = "GROUP BY A.C_INVOICE_ID, A.XX_ISLRRECEIPTNO, D.XX_CI_RIF ||' - '||D.NAME, XX_TypePersonISLR, A.XX_APPROVALDATE, A.DOCUMENTNO, A.XX_CONTROLNUMBER, E.XX_RETAINEDAMOUNT ";	
					m_orderBy = "ORDER BY A.XX_ISLRRECEIPTNO, A.XX_ISLRRECEIPTNO desc " ;
		}
		
	}   //  tableInit

	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{
		String sql = m_sql.toString() + m_groupBy + m_orderBy;
		
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
	
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
	}
	
	/*
	 * Bringing Vendors
	 */
	private void loadVendor(){
		
		String sql = "SELECT b.C_BPARTNER_ID, b.NAME FROM C_BPARTNER b WHERE b.ISVENDOR = 'Y'";
		sql = MRole.getDefault().addAccessSQL(sql, "b", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sql += " ORDER BY b.NAME";
		KeyNamePair loadKNP = new KeyNamePair(0, "");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			loadKNP=new KeyNamePair(0, new String());
			comboBPartner.addItem(loadKNP);
			while (rs.next()){
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			comboBPartner.setEditable(false);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	
}//End form

