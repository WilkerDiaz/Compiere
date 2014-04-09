package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.model.X_Ref_SupportLevel;
import org.compiere.plaf.CompiereColor;
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
import compiere.model.cds.MVCNPurchasesBook;
import compiere.model.cds.X_Ref_Invoicing_Status;
import compiere.model.suppliesservices.X_Ref_XX_Status_LV;

/**
 * @author Jorge E. Pires G.
 *
 */
public class XX_RetentionApplication extends CPanel 	implements FormPanel, 
	ActionListener, ListSelectionListener {

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_RetentionApplication.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
			
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton modify = new CButton();
	private CButton search = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();

	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_RetApp"));
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();

	private CLabel poOrderLabel = new CLabel();
	private CLabel poInvoiceLabel = new CLabel();
	private CLabel poContractLabel = new CLabel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	private VLookup poOrder = null;
	private VComboBox poContract = new VComboBox();
	private VLookup poInvoice = null;

	private CLabel numberLabelSearch = new CLabel();
	private CTextField numberSearch = new CTextField();
	
	
	private CLabel receiptDateLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_ReceptionDate"));
	private CLabel invoiceTypeLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_InvoiceType"));
	private VDate receiptDate = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private CComboBox invoiceTypeCombo = new CComboBox();
	
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
		
		prepareLookUps();
		
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		search.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		search.setEnabled(true);
			
		southPanel.setLayout(southLayout);
		modify.setText(Msg.translate(Env.getCtx(), "XX_Show"));
		modify.setEnabled(true);
		
		centerPanel.setLayout(centerLayout);
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(900, 170));
		
		xPanel.setLayout(xLayout);
		poOrderLabel.setText(Msg.getElement(Env.getCtx(), "C_Order_ID"));
		poContractLabel.setText(Msg.getElement(Env.getCtx(), "XX_Contract_ID"));
		poInvoiceLabel.setText(Msg.getElement(Env.getCtx(), "C_Invoice_ID"));
//		dateLabel.setText(Msg.translate(Env.getCtx(), "Date"));
		
		numberLabelSearch.setText(Msg.translate(Env.getCtx(), "XX_WITHHOLDING"));
		numberSearch.setPreferredSize(new Dimension(116,20));
		
		poOrder.setPreferredSize(new Dimension(110,20));
		poContract.setPreferredSize(new Dimension(110,20));
		
		poInvoice.setPreferredSize(new Dimension(110,20));
		invoiceTypeCombo.setPreferredSize(new Dimension(110,20));
		
		numberSearch.setPreferredSize(new Dimension(110,20));
		receiptDate.setPreferredSize(new Dimension(110,20));
		
		//Order
		northPanel.add(poOrderLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 0, 5, 5), 0, 0));
		northPanel.add(poOrder,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 110, 5, 0), 0, 0));
		
		//Contrato
		northPanel.add(poContractLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 0, 5, 5), 0, 0));
		northPanel.add(poContract,        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 110, 5, 0), 0, 0));
	
		
		//Invoice
		northPanel.add(poInvoiceLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 250, 5, 5), 0, 0));
		northPanel.add(poInvoice, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 350, 5, 0), 0, 0));		
		
		
		//Invoice Type
		northPanel.add(invoiceTypeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 250, 5, 5), 0, 0));
		northPanel.add(invoiceTypeCombo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 350, 5, 0), 0, 0));
		
		
		northPanel.add(numberLabelSearch,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 490, 5, 0), 0, 0));
		northPanel.add(numberSearch,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 640, 5, 0), 0, 0));
		
		//Reception Date
		northPanel.add(receiptDateLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 490, 5, 5), 0, 0));
		northPanel.add(receiptDate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 640, 5, 0), 0, 0));
		
		northPanel.add(search,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(8, 620, 0, 0), 0, 0));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		xScrollPane.getViewport().add(xTable, null);		
		
		southPanel.add(modify,  new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(12, 130, 5, 70), 0, 0));
		
		}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
				
		//Add the column definition for the table
		ColumnInfo[] layout = new ColumnInfo[] {	
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Options"), "", IDColumn.class, false, false, ""),	//  0
				
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_InvoiceType"),   ".", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "BPartner"),   ".", String.class),						//  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Date"),   ".", String.class),						//  2				
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalInvCost"),   ".", Double.class),						//  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TaxableBase"),   ".", Double.class),						//  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TaxAmount"),   ".", Double.class),						//  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_withholdingTax"),   ".", Double.class),						//  2
/*				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NotifyNumber"),   ".", KeyNamePair.class),			//  3
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ControlNumber"),   ".", String.class),*/				//  4				
				new ColumnInfo(Msg.getElement(Env.getCtx(), "XX_WITHHOLDING"),   ".", String.class),				    //  5
		};
		xTable.prepareTable(layout, "", "", true, "");
		
		//  Visual
		CompiereColor.setBackground (this);

		//  Listener
		xTable.getSelectionModel().addListSelectionListener(this);
		//xTable.setAutoResizeMode(MiniTable.AUTO_RESIZE_ALL_COLUMNS);
		xTable.getColumnModel().getColumn(1).setMinWidth(130);
		xTable.getColumnModel().getColumn(2).setMinWidth(220);
		xTable.getColumnModel().getColumn(3).setMinWidth(130);
		xTable.getColumnModel().getColumn(4).setMinWidth(110);
		xTable.getColumnModel().getColumn(5).setMinWidth(110);
		xTable.getColumnModel().getColumn(6).setMinWidth(120);
		xTable.getColumnModel().getColumn(7).setMinWidth(150);
		xTable.getColumnModel().getColumn(8).setMinWidth(150);
		/*xTable.getColumnModel().getColumn(4).setMinWidth(100);
		xTable.getColumnModel().getColumn(5).setMinWidth(100);*/
		
		
		search.addActionListener(this);
		modify.addActionListener(this);

		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);
		
		invoiceTypeCombo.addItem(new KeyNamePair(0, ""));
		invoiceTypeCombo.addItem(new KeyNamePair(1, "BIENES"));
		invoiceTypeCombo.addItem(new KeyNamePair(2, "MERCANCIA"));
		invoiceTypeCombo.addItem(new KeyNamePair(3, "SERVICIO"));

		//dataVendor();  		
		//xTable.autoSize();
		
	}   //  dynInit
	
	private void prepareLookUps(){
		
		//ORDEN
		Lookup l = MLookupFactory.get(Env.getCtx(), m_WindowNo, Env
				.getCtx().getContextAsInt("#XX_L_C_ORDERCOLUMN_ID"),
				DisplayTypeConstants.Search);
		poOrder = new VLookup("C_Order_ID", false, false, true, l);
		poOrder.setVerifyInputWhenFocusTarget(false);
		
		//INVOICE
		l = MLookupFactory.get(Env.getCtx(), m_WindowNo, 3484,
				DisplayTypeConstants.Search);
		poInvoice = new VLookup("C_Invoice_ID", false, false, true, l);
		poInvoice.setVerifyInputWhenFocusTarget(false);
		
		
		//Contracts
		String sqlContract = "SELECT c.XX_CONTRACT_ID, c.VALUE " +
							 "FROM XX_CONTRACT c " +
							 "WHERE c.XX_Status = '"+X_Ref_XX_Status_LV.APPROVED_BY_LEGAL.getValue()+"'";
						
		sqlContract = MRole.getDefault().addAccessSQL(sqlContract, "c", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sqlContract += " ORDER BY c.VALUE";
						
				poContract.addItem(new KeyNamePair());
						
		try {
			PreparedStatement pstm = DB.prepareStatement(sqlContract, null);
			ResultSet rs = pstm.executeQuery(sqlContract);
					
			while (rs.next()){
				poContract.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			pstm.close();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Method that fill the form table
	 */
	private void data(){  
		String SQL = "SELECT " +
					 "pb.AD_Client_ID, " +
					 "pb.AD_Org_ID, " +
					 "pb.C_ORDER_ID, " +
					 "pb.NAME, " +
					 "pb.C_BPARTNER_ID, " +
					// "pb.XX_ControlNumberAviso, " +
					 //"pb.DOCUMENTNO, " +
					 "CASE " +
				     	"WHEN inv.XX_InvoiceType = 'S' THEN 'SERVICIO' " +
						"WHEN inv.XX_InvoiceType = 'A' THEN 'BIENES' " +
						"WHEN inv.XX_InvoiceType = 'I' THEN 'PRODUCTOS PARA LA VENTA' " +
						"ELSE '' " +
					 "END INVTYPE, " +
					// "pb.XX_DOCUMENTNO_ID, " +
					// "pb.DOCUMENTNOAVISO, " +
					 "pb.XX_WITHHOLDING, " +
					 "TO_CHAR(pb.XX_DATE,'DD-MM-YYYY') XX_DATE, " +
					 "pb.XX_NOCOMPROBANTE, " +
					 "pb.XX_MonthYear, " +
					 "ROUND(sum(pb.XX_TotalInvCost),2) XX_TotalInvCost, " +
					 "ROUND(sum(pb.XX_TaxableBase),2) XX_TaxableBase, " +
					 "ROUND(sum(pb.XX_TaxAmount),2) XX_TaxAmount, " +
					 "ROUND(sum(pb.XX_withholdingTax),2) XX_withholdingTax, " +
					 "MAX(pb.XX_VCN_PURCHASESBOOK_ID) XX_VCN_PURCHASESBOOK_ID "+
					 "FROM XX_PurchaseFormView pb, C_INVOICE inv "+
				     "WHERE pb.XX_WITHHOLDING IS NOT NULL AND pb.C_INVOICE_ID = inv.C_INVOICE_ID " +
				     "AND inv.DocStatus <> 'VO' "+
				     "AND pb.XX_WITHHOLDING != 0 ";
		
		Timestamp date = (Timestamp) receiptDate.getValue();
		if (date != null) {
			SQL += " AND TRUNC(pb.XX_DATE) = " + DB.TO_DATE(date) + " ";
		}
		
		if (invoiceTypeCombo.getSelectedIndex() > 0) {
		
			if(invoiceTypeCombo.getSelectedIndex()==1)
				SQL += " AND  inv.XX_InvoiceType = 'A' ";
			else if (invoiceTypeCombo.getSelectedIndex()==2)
				SQL += " AND  inv.XX_InvoiceType = 'I' ";
			else if (invoiceTypeCombo.getSelectedIndex()==3)
				SQL += " AND  inv.XX_InvoiceType = 'S' ";
		}
		
		if(!numberSearch.getText().isEmpty()){
			SQL+= " AND pb.XX_NOCOMPROBANTE = '"+numberSearch.getText()+"' ";
		}
		
		if (poOrder.getValue()!= null) 
    		SQL += " AND pb.C_ORDER_ID = " + poOrder.getValue();
		
		if (poInvoice.getValue()!= null) 
    		SQL += " AND inv.C_Invoice_ID = " + poInvoice.getValue();
		
		if (poContract.getSelectedIndex() != -1 && poContract.getSelectedItem() != null) 
    		SQL += " AND pb.XX_CONTRACT_ID = " + ((KeyNamePair)poContract.getSelectedItem()).getKey();
		
    	SQL = MRole.getDefault().addAccessSQL(
				SQL.toString(), "pb", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
//				+ " GROUP BY pb.AD_Client_ID, pb.AD_Org_ID, pb.C_ORDER_ID, pb.NAME, pb.C_BPARTNER_ID, pb.XX_ControlNumberAviso, pb.DOCUMENTNO, pb.C_INVOICE_ID, pb.XX_DOCUMENTNO_ID, pb.DOCUMENTNOAVISO, pb.XX_WITHHOLDING,pb.XX_DATE, pb.XX_NOCOMPROBANTE, pb.XX_MonthYear " 
				+ " GROUP BY pb.AD_Client_ID, pb.AD_Org_ID, pb.C_ORDER_ID, pb.NAME, pb.C_BPARTNER_ID, inv.XX_InvoiceType, pb.XX_WITHHOLDING, pb.XX_DATE, pb.XX_NOCOMPROBANTE, pb.XX_MonthYear " 
				+ " ORDER BY pb.XX_WITHHOLDING ";

    	int i = 0;
    	xTable.setRowCount(i);
    	try 
		{	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTable.setRowCount (i+1);			
				IDColumn id = new IDColumn(rs.getInt("XX_VCN_PURCHASESBOOK_ID"));
				id.setSelected(false);
				xTable.setValueAt(id, i, 0);
				
				xTable.setValueAt(rs.getString("INVTYPE"), i, 1);
				
				xTable.setValueAt(rs.getString("NAME"), i, 2);
				xTable.setValueAt(rs.getString("XX_Date"), i, 3);
				xTable.setValueAt(rs.getDouble("XX_TotalInvCost"), i, 4);
				xTable.setValueAt(rs.getDouble("XX_TaxableBase"), i, 5);
				xTable.setValueAt(rs.getDouble("XX_TaxAmount"), i, 6);
				xTable.setValueAt(rs.getDouble("XX_withholdingTax"), i, 7);
				xTable.setValueAt(rs.getString("XX_NOCOMPROBANTE"), i, 8);
				/*xTable.setValueAt(rs.getString("DocumentNo"), i, 2);
				xTable.setValueAt(
						new KeyNamePair (rs.getInt("XX_DOCUMENTNO_ID"), rs.getString("DocumentNoAviso")), i, 3);
				xTable.setValueAt(rs.getString("XX_ControlNumberAviso"), i, 4);
*/
				i++;				
			}
			rs.close();
			pstmt.close();			
		}
		catch(SQLException e)
		{	
			e.getMessage();
		}				
	}    	
	
    /**
     * Modify Button Method
     */
    public void modify() {
    	Vector<Object> selecc = new Vector<Object>();
    	int rows = xTable.getRowCount();
    	
    	for (int i = 0; i < rows; i++) {
    		IDColumn id = (IDColumn) xTable.getModel().getValueAt(i, 0);
    		
    		if(id.isSelected()){
    			new compiere.model.cds.Utilities().showReport(new MVCNPurchasesBook(Env.getCtx(), id.getRecord_ID(), null));
    			selecc.add(id);
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
	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{					
		if (e.getSource() == modify){
			modify();
			data();
		}else if (e.getSource() == search)
			data();
		
	}   //  actionPerformed

	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;			
	}   //  valueChanged

}
