package compiere.model.bank.forms;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
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

import compiere.model.cds.forms.XX_EntranceAuthorization_Form;

public class XX_VCN_PaySpecification_Form extends CPanel
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
	static CLogger 			log = CLogger.getCLogger(XX_VCN_PaySpecification_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton bSearch = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private JScrollPane xScrollPane2 = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("Ordenes de Pago");
	private TitledBorder xBorder2 = new TitledBorder("Detalle de Pago");
	private MiniTable xTable = new MiniTable();
	private MiniTable xTable2 = new MiniTable();
	private CPanel xPanel = new CPanel();
	private static int tableInit_option;
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	//Variables mias
	private CLabel poOrderLabel = new CLabel();
	private CLabel poBusinessPartner = new CLabel();
	private CLabel poOrderDateLabel = new CLabel();
	
	private CTextField poOrder = new CTextField();
	private CTextField poSocio = new CTextField();
	private CTextField poDate = new CTextField();
	
	private  CComboBox stateTypeCombo = new CComboBox();
	
	private  VDate OrderDate = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	
	private CLabel stateTypeLabel = new CLabel(Msg.translate(Env.getCtx(), "XX_VCN_State"));
	
	private static String idsegunda;
	
	


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

		bSearch.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearch.setPreferredSize(new Dimension(90,22));	
		bSearch.setEnabled(true);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(900, 445));
		
		xScrollPane2.setBorder(xBorder2);
		xScrollPane2.setVisible(false);
		xScrollPane2.setPreferredSize(new Dimension(900, 150));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		centerPanel.add(xScrollPane2,  BorderLayout.SOUTH);

		xScrollPane.getViewport().add(xTable, null);
		xScrollPane2.getViewport().add(xTable2, null);
		
		//ASIGNO AL LABEL EL TEXTO DE ORDEN DE COMPRA
		poOrderLabel.setText("Orden de Pago");
		//ASIGNO AL LABEL EL TEXTO DE SOCIO DE NEGOCIO
		poBusinessPartner.setText(Msg.translate(Env.getCtx(), "XX_VCN_BusinessPartner"));
		//ASIGNO AL LABEL EL TEXTO DE ORDEN DE COMPRA		
		poOrderDateLabel.setText(Msg.translate(Env.getCtx(), "XX_VCN_OrderDate"));

		poOrder.setPreferredSize(new Dimension(210,20));
		poSocio.setPreferredSize(new Dimension(210,20));
		
		poDate.setPreferredSize(new Dimension(170,20));
		stateTypeCombo.setPreferredSize(new Dimension(170,20));
		
		OrderDate.setPreferredSize(new Dimension(170,20));
		
		//Order
		northPanel.add(poOrderLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 5, 5, 5), 0, 0));
		northPanel.add(poOrder,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 5, 20), 0, 0));
		
		//Fecha de Orden
				northPanel.add(poOrderDateLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 10, 5, 5), 0, 0));
				northPanel.add(OrderDate, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 5, 5), 0, 0));
		
				//Socio de negocio
				northPanel.add(poBusinessPartner,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 5, 5, 5), 0, 0));
				northPanel.add(poSocio,        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 5, 20), 0, 0));
				
				//Estado
				northPanel.add(stateTypeLabel, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 10, 5, 5), 0, 0));
				northPanel.add(stateTypeCombo, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 5, 5), 0, 0));
				
				//BOTON BUSCAR
				northPanel.add(bSearch,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 5, 5, 10), 0, 0));
				

	    
	    southPanel.validate();

	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		//Add the column definition for the table
		ColumnInfo[] layout = new ColumnInfo[] {	
				new ColumnInfo("Orden de Pago", "", String.class, true, false, ""),//0
				new ColumnInfo("Fecha de Orden",   ".", String.class),//1
				new ColumnInfo("Socio de Negocio",   ".", String.class),//2
				new ColumnInfo("Monto del Pago",   ".", Double.class),//3
				new ColumnInfo("Estado",   ".", String.class),//4
				new ColumnInfo("Fecha de Pago",   ".", String.class),//5
				new ColumnInfo("Banco",   ".", String.class),//6
				new ColumnInfo("Cheque/Transferencia",   ".", String.class),//7
				new ColumnInfo("Num Cheque/Transferencia",   ".", String.class),//8
				new ColumnInfo("Ubicacion del cheque",   ".", String.class),//9
				new ColumnInfo("Tipo",   ".", String.class,true,false, ""),//10
		};
		
			
		ColumnInfo[] layout2 = new ColumnInfo[] {
				new ColumnInfo("Categoria Socio de Negocio","", String.class,true, false, ""),//0
				new ColumnInfo("Factura",   ".", String.class),//1
				new ColumnInfo("Fecha Factura",   ".", String.class),//2
				new ColumnInfo("Fecha Vencimiento",   ".", String.class),//3
				new ColumnInfo("Orden de Compra",   ".", String.class),//4
				new ColumnInfo("Contrato",   ".", String.class),//5
				new ColumnInfo("Monto",   ".", String.class),//6
				new ColumnInfo("Cuenta Contable",   ".", String.class),//7
		};

		xTable.prepareTable(layout, "", "", false, "");
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
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		poOrder.addKeyListener (new KeyAdapter () {
			          
			public void keyPressed (KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		             cmd_Search();
		             
		         }
			}
	     } // end anonymous class
		);
		
		bSearch.addActionListener(this);
			
		//Cargo la tabla 1
		tableInit_option=0;
		tableInit(poOrder,  poSocio,  OrderDate,  stateTypeCombo);
		tableLoad (xTable);
		
		//Cargo la tabla 2
		tableInit_option=2;
		tableInit(poOrder,  poSocio,  OrderDate,  stateTypeCombo);
		tableLoad1 (xTable);
		
		statusBar.setStatusLine("Cantidad de  órdenes de pago  listadas");
		statusBar.setStatusDB(xTable.getRowCount());
		
		//AGREGO LOS ITEMS AL COMBO BOX QUE ME PERMITE FILTRAR POR ESTADO
		stateTypeCombo.addItem(new KeyNamePair(0, ""));
		stateTypeCombo.addItem(new KeyNamePair(1, "PENDIENTE"));
		stateTypeCombo.addItem(new KeyNamePair(2, "PROCESADO"));

		
		
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSearch)
			cmd_Search();
	}   //  actionPerformed
	
	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{
		
			//LLENO LAS ORDENES DE PAGO
			tableInit_option=0;
			tableInit( poOrder,  poSocio,  OrderDate,  stateTypeCombo);
			tableLoad (xTable);

			//LLENO LOS ANTICIPOS
			tableInit_option=2;
			tableInit( poOrder,  poSocio,  OrderDate,  stateTypeCombo);
			tableLoad1 (xTable);

			tableInit_option=0;
			
			
			statusBar.setStatusLine("Cantidad de  órdenes de pago  listadas");
			statusBar.setStatusDB(xTable.getRowCount());
		
	}   //  cmd_Search
	
	private void cmd_Segunda(String tipo)
	{
		
			if(tipo.equals("Selección"))
			{
				tableInit_option=1;
				tableInit( poOrder,  poSocio,  OrderDate,  stateTypeCombo);
				tableLoad2 (xTable2);
				
				statusBar.setStatusLine("Cantidad de  órdenes de pago  listadas");
				statusBar.setStatusDB(xTable.getRowCount());
				
				tableInit_option=0;
			}
			else if(tipo.equals("Anticipo")){
				
				tableInit_option=3;
				tableInit( poOrder,  poSocio,  OrderDate,  stateTypeCombo);
				tableLoad3 (xTable2);
				
				statusBar.setStatusLine("Cantidad de  órdenes de pago  listadas");
				statusBar.setStatusDB(xTable.getRowCount());
				
				tableInit_option=0;
				
			}
		
	}   //  cmd_Segunda()
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	@Override
	public void valueChanged (ListSelectionEvent e)
	{
		
		int tableRow = xTable.getSelectedRow();
			
		if(tableRow==-1) 
		{
			xScrollPane2.setVisible(false); //escondo el panel
			statusBar.setStatusLine("Cantidad de  órdenes de pago  listadas");
			statusBar.setStatusDB(xTable.getRowCount());
		}
		else
		{
			int orderRow = xTable.getSelectedRow();
			idsegunda = xTable.getValueAt(orderRow, 0).toString();
			
			String tipo= xTable.getValueAt(orderRow, 10).toString();
			cmd_Segunda(tipo);
			
			xScrollPane2.setVisible(true); //muestro el panel de motivos de rechazo
			statusBar.setStatusLine("Orden de Pago seleccionada");
		}
		
		if (e.getValueIsAdjusting())
			return;

	}   //  valueChanged

	@Override
	public void tableChanged(TableModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}


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
	 *  @param matchToType (Invoice, Shipment, Order) see MATCH_
	 * @throws SQLException *
	 */
	private static void tableInit (CTextField poOrder, CTextField poSocio, VDate OrderDate, CComboBox stateTypeCombo)
	{
		m_sql = null;
		m_sql = new StringBuffer ();
		

		if(tableInit_option==0)
		{
			
			m_sql.append(
					"SELECT"+
				    " PS.C_PAYSELECTION_ID AS ID,"+			
				    " PS.C_PAYSELECTION_ID AS PAYORDER,"+
				    " PS.CREATED AS ORDERDATE,"+
				    " BP.NAME AS PARTNER,"+
				    " PS.TOTALAMT AS PAYMONT,"+
				    " PS.PROCESSING AS STATE,"+
				    " PS.PAYDATE AS PAYDATE,"+
				    " B.NAME AS BANK,"+
				    " PSC.PAYMENTRULE AS PAYMENTRULE,"+
				    " PSC.CHECKNO AS CHECKNO,"+
				    " PSC.C_PAYSELECTIONCHECK_ID AS IDPSC"+
				" FROM"+
				    " C_PAYSELECTION PS, C_PAYSELECTIONCHECK PSC, C_BPARTNER BP, C_BANK B, C_BANKACCOUNT BA"+
				" WHERE"+
				    " PS.C_PAYSELECTION_ID = PSC.C_PAYSELECTION_ID AND"+ 
				    " BP.C_BPARTNER_ID =PSC.C_BPARTNER_ID AND "+
				    " BA.C_BANKACCOUNT_ID = PS.C_BANKACCOUNT_ID AND"+ 
				    " B.C_BANK_ID = BA.C_BANK_ID AND"+
				    " PS.PROCESSED = 'Y' AND PS.ISACTIVE = 'Y'");
			
			//AGREGO AL SELECT LOS VALORES DE LOS PARAMETROS
			
			//CAMPO DE ORDEN DE PAGO
			if (!poOrder.getText().equals("")) 
				m_sql.append(" AND PS.C_PAYSELECTION_ID = " + poOrder.getText());
			//CAMPO SOCIO DE NEGOCIO
			if (!poSocio.getText().equals("")) 
				m_sql.append(" AND BP.NAME LIKE '%" + poSocio.getValue() +"%'");
			//CAMPO FECHA DE ORDEN
			Timestamp date = (Timestamp) OrderDate.getValue();
			if (date != null) {
				m_sql.append(" AND TRUNC(PS.CREATED) = " + DB.TO_DATE(date) + " ");
			}
			//CAMPO ESTADO
				if (stateTypeCombo.getSelectedIndex() > 0) {
					if(stateTypeCombo.getSelectedIndex()==1)
						m_sql.append( " AND  PS.PROCESSING = 'N' ");
					else if (stateTypeCombo.getSelectedIndex()==2)
						m_sql.append( " AND  PS.PROCESSING = 'Y' ");
			}
				
			//System.out.println(m_sql);	
			
		}
		if(tableInit_option==1)  
		{
			m_sql.append(
					"SELECT"
					+" PS.C_PAYSELECTION_ID AS ID,"
					+" PSL.C_PAYSELECTIONLINE_ID AS IDLINEA,"
				    +" I.XX_INVOICETYPE AS CATEGORIA,"
				    +" I.DOCUMENTNO AS FACTURA,"
				    +" I.DATEINVOICED AS FECHAFACT,"
				    +" I.XX_DUEDATE AS FECHAVENC,"
				    +" O.DOCUMENTNO AS ORDENCOMPRA,"
				    +" XC.VALUE AS CONTRATO,"
				    +" to_char(PSL.PAYAMT, '999G999G999G999D99') AS MONTO"
					+" FROM"
				    +" C_PAYSELECTIONLINE PSL, C_PAYSELECTION PS, C_INVOICE I"
					+" LEFT JOIN C_ORDER O  ON  O.C_ORDER_ID = I.C_ORDER_ID"
				    +" LEFT JOIN XX_CONTRACT XC ON XC.XX_CONTRACT_ID=I.XX_CONTRACT_ID"
					+" WHERE"
				    +" PSL.C_INVOICE_ID = I.C_INVOICE_ID AND"
				    +" PS.C_PAYSELECTION_ID = PSL.C_PAYSELECTION_ID AND"
				    +" PS.C_PAYSELECTION_ID = "+idsegunda
					);
			
			
		}//fin if else = 1
		
		if(tableInit_option==2)  
		{
		m_sql.append(
				"SELECT"+
				" P.C_PAYMENT_ID AS ID,"+
			    " P.DOCUMENTNO AS PAYORDER,"+
			    " P.CREATED AS ORDERDATE,"+
			    " BP.NAME AS PARTNER,"+
			    " P.PAYAMT AS PAYMONT,"+
			    " P.PROCESSING AS STATE,"+
			    " P.XX_DATEFINALPAY AS PAYDATE,"+
			    " B.NAME AS BANK,"+
			    " P.TENDERTYPE AS PAYMENTRULE,"+
			    " P.CHECKNO AS CHECKNO"+
				" FROM"+
			    " C_PAYMENT P, C_BPARTNER BP, C_BANK B, C_BANKACCOUNT BA"+
			    " WHERE"+
			    " BP.C_BPARTNER_ID = P.C_BPARTNER_ID AND"+
			    " B.C_BANK_ID = BA.C_BANK_ID AND"+
			    " BA.C_BANKACCOUNT_ID = P.C_BANKACCOUNT_ID AND"+
			    " P.PROCESSED = 'Y' AND"+
			    " P.ISACTIVE = 'Y' AND"+
			    " P.ISAPPROVED = 'Y' AND"+
			    " P.ISPREPAYMENT = 'Y' "
				);
		
		//AGREGO AL SELECT LOS VALORES DE LOS PARAMETROS
		
		//CAMPO DE ORDEN DE PAGO
		if (!poOrder.getText().equals("")) 
			m_sql.append(" AND P.DOCUMENTNO = '" + poOrder.getText() + "'");
		//CAMPO SOCIO DE NEGOCIO
		if (!poSocio.getText().equals("")) 
			m_sql.append(" AND BP.C_BPARTNER_ID = " + poSocio.getValue());
		//CAMPO FECHA DE ORDEN
		Timestamp date = (Timestamp) OrderDate.getValue();
		if (date != null) {
			m_sql.append(" AND TRUNC(P.CREATED) = " + DB.TO_DATE(date) + " ");
		}
		//CAMPO ESTADO
			if (stateTypeCombo.getSelectedIndex() > 0) {
				if(stateTypeCombo.getSelectedIndex()==1)
					m_sql.append( " AND  P.PROCESSING = 'N' ");
				else if (stateTypeCombo.getSelectedIndex()==2)
					m_sql.append( " AND  P.PROCESSING = 'Y' ");
		}

			//System.out.println(m_sql);
		
		}//FIN DEL ELSE IF = 2

		if(tableInit_option==3)  
		{
			
			String idAnticipo = null;
				try {
					//obtengo el id del anticipo
					idAnticipo = getIdAnticipo(idsegunda);
				} catch (SQLException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				} 
			m_sql.append(
					"SELECT "
					+" O.XX_POTYPE AS CATEGORIA,"
					+" O.DOCUMENTNO AS ORDEN,"
					+" to_char(P.PAYAMT, '999G999G999G999D99') AS MONTO"
					+" FROM"
					+" C_PAYMENT P"
					+" LEFT JOIN C_ORDER O ON  O.C_ORDER_ID = P.C_ORDER_ID"
					+" WHERE"
					+" P.C_PAYMENT_ID = "+idAnticipo
					);
		}//fin if else = 3
		
	}   //  tableInit

	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{

		String sql = m_sql.toString();
		log.finest(sql);
		
		int i=0;
		table.setRowCount(i);
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		
    	try 
		{	
    		pstmt = DB.prepareStatement(sql, null);
    		rs = pstmt.executeQuery();
    		
			while(rs.next()) {	
				table.setRowCount (i+1);
				

				table.setValueAt(rs.getInt("PAYORDER"), i, 0);
				
				//COLOCAR LA FECHA DE ORDEN HAGO UN SPLIT PARA NO MOSTRAR LA HORA
				String[] lista=null;
				String cadena = rs.getString("ORDERDATE");
				lista = cadena.split(" ");
				cadena=lista[0];lista=null;lista = cadena.split("-");cadena=lista[2]+"-"+lista[1]+"-"+lista[0];
				
				table.setValueAt(cadena, i, 1);
				
				//Socio de negocio
				table.setValueAt(rs.getString("PARTNER"), i, 2);
				
				//COLOCAR MONTO DEL PAGO
				table.setValueAt(rs.getDouble("PAYMONT"), i, 3);
				
				//COLOCAR EL ESTADO DE LA ORDEN
				if (rs.getString("STATE") == null || rs.getString("STATE").equals("N"))
						table.setValueAt("Pendiente", i, 4);
				else
						table.setValueAt("Procesado", i, 4);
				 
				//COLOCAR LA FECHA DE PAGO HAGO UN SPLIT PARA NO MOSTRAR LA HORA
				String[] lista1=null;
				String cadena1 = rs.getString("PAYDATE");
				lista1 = cadena1.split(" ");
				cadena1=lista1[0];lista1=null;lista1 = cadena1.split("-");cadena1=lista1[2]+"-"+lista1[1]+"-"+lista1[0];
				table.setValueAt(cadena1, i, 5);

				//BANCO
				table.setValueAt(rs.getString("BANK"), i, 6);
				
				//COLOCAR SI ES CHEQUE/TRANSFERENCIA				
				if(rs.getString("PAYMENTRULE").equals("B"))
					table.setValueAt("Efectivo", i, 7);
				if(rs.getString("PAYMENTRULE").equals("K"))
					table.setValueAt("Tarj de debito/Credito virtual", i, 7);
				if(rs.getString("PAYMENTRULE").equals("P"))
					table.setValueAt("Giro", i, 7);
				if(rs.getString("PAYMENTRULE").equals("S"))
					table.setValueAt("Cheque", i, 7);
				if(rs.getString("PAYMENTRULE").equals("T"))
					table.setValueAt("Transferencia", i, 7);
				if(rs.getString("PAYMENTRULE").equals("02"))
					table.setValueAt("Cheque777", i, 7);
				
				//COLOCAR EL NUMERO DE CHEQUE O TRANSFERENCIA
				if(rs.getString("PAYMENTRULE").equals("T")){
					table.setValueAt(buscarNumeroTransferencia(rs.getString("IDPSC")), i, 8);
				}
				else if(rs.getString("CHECKNO") != null){
					table.setValueAt(rs.getString("CHECKNO"), i, 8);
					//COLOCAR EL STATUS DEL CHEQUE
					table.setValueAt(UbicacionSelecion(rs.getString("ID")), i, 9);
				}
				
				table.setValueAt("Selección", i, 10);
				i++;
			}

		}
		catch(SQLException e)
		{	
			e.getMessage();
		}finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}   //  tableLoad


	private static void tableLoad1 (MiniTable table)
	{

		String sql = m_sql.toString();
		log.finest(sql);
		
		int i=0;
		
		int z= table.getRowCount();
		//System.out.println(z);
		i= z;
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
    	try 
		{	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				table.setRowCount (i+1);
				
				//DOCUMENTNO
				table.setValueAt(rs.getInt("PAYORDER"), i, 0);
				
				//COLOCAR LA FECHA DE ORDEN HAGO UN SPLIT PARA NO MOSTRAR LA HORA
				String[] lista2=null;
				String cadena2 = rs.getString("ORDERDATE");
				lista2 = cadena2.split(" ");
				cadena2=lista2[0];lista2=null;lista2 = cadena2.split("-");cadena2=lista2[2]+"-"+lista2[1]+"-"+lista2[0];
				table.setValueAt(cadena2, i, 1);
				
				//SOCIO DE NEGOCIO
				table.setValueAt(rs.getString("PARTNER"), i, 2);
				//MONTO
				table.setValueAt(rs.getDouble("PAYMONT"), i, 3);
				
				//COLOCAR EL ESTADO DE LA ORDEN
				if(rs.getString("STATE") == null || rs.getString("STATE").equals("N"))
					table.setValueAt("Pendiente", i, 4);
				else
					table.setValueAt("Procesado", i, 4);
				
				
				//COLOCAR LA FECHA DE PAGO HAGO UN SPLIT PARA NO MOSTRAR LA HORA
					if(rs.getString("PAYDATE") != null )
					{
						String[] lista3=null;
						String cadena3 = rs.getString("PAYDATE");
						lista3 = cadena3.split(" ");
						cadena3=lista3[0];lista3=null;lista3 = cadena3.split("-");cadena3=lista3[2]+"-"+lista3[1]+"-"+lista3[0];
						table.setValueAt(cadena3, i, 5);
					}
					else
						table.setValueAt(rs.getString("PAYDATE"), i, 5);
				
				//BANCO
				table.setValueAt(rs.getString("BANK"), i, 6);
				
				//COLOCAR SI ES CHEQUE/TRANSFERENCIA
				if(rs.getString("PAYMENTRULE").equals("B"))
					table.setValueAt("Efectivo", i, 7);
				if(rs.getString("PAYMENTRULE").equals("K"))
					table.setValueAt("Tarj de debito/Credito virtual", i, 7);
				if(rs.getString("PAYMENTRULE").equals("P"))
					table.setValueAt("Giro", i, 7);
				if(rs.getString("PAYMENTRULE").equals("S"))
					table.setValueAt("Cheque", i, 7);
				if(rs.getString("PAYMENTRULE").equals("T"))
					table.setValueAt("Transferencia", i, 7);
				if(rs.getString("PAYMENTRULE").equals("02"))
					table.setValueAt("Cheque777", i, 7);
				
				//COLOCAR EL NUMERO DE CHEQUE
				if(rs.getString("PAYMENTRULE").equals("T")){
					table.setValueAt(buscarNumeroTransferenciaAnticipo(rs.getString("ID")), i, 8);
				}
				else if(rs.getString("CHECKNO") != null){
					table.setValueAt(rs.getString("CHECKNO"), i, 8);
				//COLOCAR EL STATUS DEL CHEQUE
					table.setValueAt(UbicacionSelecion(rs.getString("ID")), i, 9);
				}
				
				table.setValueAt("Anticipo", i, 10);

				i++;				
			}
			
		}
		catch(SQLException e)
		{	
			e.getMessage();
		}finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}   //  tableLoad
	
	
	
	private static void tableLoad2 (MiniTable table)
	{

		String sql = m_sql.toString();
		
		log.finest(sql);
		int i = 0;
		table.setRowCount(i);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
    	try 
		{	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			
			while(rs.next()) {	
				table.setRowCount (i+1);
				
				//TIPO DE CATEGORIA
				if(rs.getString("CATEGORIA").equals("A"))
					table.setValueAt("Bienes", i, 0);
				else if(rs.getString("CATEGORIA").equals("E"))
					table.setValueAt("Tipo de Gasto", i, 0);
				else if(rs.getString("CATEGORIA").equals("I"))
					table.setValueAt("Prod para la venta", i, 0);
				else if(rs.getString("CATEGORIA").equals("R"))
					table.setValueAt("Recurso", i, 0);
				else if(rs.getString("CATEGORIA").equals("S"))
					table.setValueAt("Servicio", i, 0);
				
				table.setValueAt(rs.getString("FACTURA"), i, 1);
				
				String[] lista2=null;
				String cadena2 = rs.getString("FECHAFACT");
				lista2 = cadena2.split(" ");
				cadena2=lista2[0];lista2=null;lista2 = cadena2.split("-");cadena2=lista2[2]+"-"+lista2[1]+"-"+lista2[0];
				table.setValueAt(cadena2, i, 2);
				
				lista2=null;
				cadena2 = rs.getString("FECHAVENC");
				lista2 = cadena2.split(" ");
				cadena2=lista2[0];lista2=null;lista2 = cadena2.split("-");cadena2=lista2[2]+"-"+lista2[1]+"-"+lista2[0];
				table.setValueAt(cadena2, i, 3);
				
				if (rs.getString("ORDENCOMPRA")!=null)
				table.setValueAt(rs.getString("ORDENCOMPRA"), i, 4);
				
				if (rs.getString("CONTRATO")!=null)
				table.setValueAt(rs.getString("CONTRATO"), i, 5);
				
				table.setValueAt(rs.getString("MONTO"), i, 6);
				
				table.setValueAt(getCategorySeleccion(idsegunda,rs.getString("IDLINEA")), i, 7);
				
				i++;				
			}
			
		}
		catch(SQLException e)
		{	
			e.getMessage();
		}finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}   //  tableLoad

	private static void tableLoad3 (MiniTable table)
	{

		String sql = m_sql.toString();
				
		log.finest(sql);
		int i = 0;
		table.setRowCount(i);
		
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		
    	try 
		{	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			
			while(rs.next()) {	
				table.setRowCount (i+1);
				
				//CATEGORIA
				if(rs.getString("CATEGORIA") != null && rs.getString("CATEGORIA").equals("POA")){
					table.setValueAt("O/C Bienes y Servicios", i, 0);
				}
				if(rs.getString("CATEGORIA") != null && rs.getString("CATEGORIA").equals("POM")){
					table.setValueAt("O/C Productos para la venta", i, 0);
				}

				//ORDEN COMPRA
				if(rs.getString("ORDEN") != null )
					table.setValueAt(rs.getString("ORDEN"), i, 4);
				
				//CONTRATO
				String idAnticipo = null;
				idAnticipo = getIdAnticipo(idsegunda);
				//System.out.println(idAnticipo);
				table.setValueAt(getContrato(idAnticipo), i, 5);
				
				//MONTO
				if (rs.getString("MONTO") != null) 
				table.setValueAt(rs.getString("MONTO"), i, 6);
				
				//Cuenta contable
				table.setValueAt(getCategoryAnticipo(idAnticipo), i, 7);
				i++;				
			}
			
		}
		catch(SQLException e)
		{	
			e.getMessage();
		}
    	finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}   //  tableLoad

	
	public static String UbicacionSelecion (String idSeleccion) {
		
		String sql= 
				"SELECT CPS.C_PAYSELECTION_ID, XVC.XX_VCN_LOCATIONOFCHECKS AS UBI"
				+" FROM C_PAYSELECTION CPS, C_PAYSELECTIONLINE CPSL, C_PAYSELECTIONCHECK CPSC, XX_VCN_CHECK XVC, C_PAYMENT CP"
				+" WHERE CPS.C_PAYSELECTION_ID = CPSL.C_PAYSELECTION_ID"
				+" AND CPSC.C_PAYSELECTIONCHECK_ID = CPSL.C_PAYSELECTIONCHECK_ID"
				+" AND CPSC.C_PAYMENT_ID = CP.C_PAYMENT_ID"
				+" AND XVC.C_PAYMENT_ID = CP.C_PAYMENT_ID"
				+" AND XVC.C_PAYMENT_ID = "+idSeleccion;
		int total =0;
		String mjs="";
		
					
					PreparedStatement pstmt = null;
				   	ResultSet rs = null;
				   	
				   	//ejecuto la sentencia SQL
					   	try {
					   		pstmt = DB.prepareStatement(sql,null);
					   		rs = pstmt.executeQuery();
					   		
					   		if (rs.next()){
					   			//TOTAL TIENE LA UBICACION DEL CHEQUE
					   			total= rs.getInt("UBI");
					   			
					   			}// Fin if
					   		else{
					   			return "id inexistente";
					   		}//FIN DEL ELSE
					   	} 
					   	catch (Exception e){
							System.out.println(e);
						}
					   	finally 
						{
							DB.closeResultSet(rs);
							DB.closeStatement(pstmt);
						}
					   	
			   			if (total == 1)
			   				mjs= "En oficina";
			   			if (total == 2)
			   				mjs= "Cobrado";
			   			if (total == 3)
			   				mjs= "Entregado";
			   			if (total == 4)
			   				mjs= "Perdido";
			   			if (total == 5)
			   				mjs= "Elaborado";
	return mjs;
	}//fin de Ubicacion
	
	public static String UbicacionAnticipo (String idAnticipo) {
		
		String sql= 
				"SELECT CP.C_PAYMENT_ID, XVC.XX_VCN_LOCATIONOFCHECKS as UBI"
				+" FROM XX_VCN_CHECK XVC, C_PAYMENT CP"
				+" WHERE XVC.C_PAYMENT_ID = CP.C_PAYMENT_ID"
				+" AND CP.C_PAYMENT_ID = "+idAnticipo;
		int total =0;
		String mjs="";
		
					
					PreparedStatement pstmt = null;
				   	ResultSet rs = null;
				   	
				   	//ejecuto la sentencia SQL
					   	try {
					   		pstmt = DB.prepareStatement(sql,null);
					   		rs = pstmt.executeQuery();
					   		
					   		if (rs.next()){
					   			//TOTAL TIENE LA UBICACION DEL CHEQUE
					   			total= rs.getInt("UBI");
					   			
					   			}// Fin if
					   		else{
					   			return "id inexistente";
					   		}
					   	} 
					   	catch (Exception e){
							System.out.println(e);
						}
					   	finally 
						{
							DB.closeResultSet(rs);
							DB.closeStatement(pstmt);
						}
					   	
			   			if (total == 1)
			   				mjs= "En oficina";
			   			if (total == 2)
			   				mjs= "Cobrado";
			   			if (total == 3)
			   				mjs= "Entregado";
			   			if (total == 4)
			   				mjs= "Perdido";
			   			if (total == 5)
			   				mjs= "Elaborado";
	return mjs;
	}//fin de Ubicacion
	
/*
 *METODO QUE SE ENCARGARA DE BUSCAR LA CUENTA CONTABLE CUANDO ES SELECCION 	
 */
	private static String getCategorySeleccion(String id,String idlinea) throws SQLException{
		
		String sql = "select ord.XX_POType, ord.XX_PurchaseType, con.XX_Contract_ID, doc.C_DocType_ID TIPDOC, " +
				"inv.DocumentNo NRODOC, to_char(inv.DateInvoiced,'dd.mm.yyyy') FECDOC, " +
				"(case when inv.XX_DueDate > inv.DateInvoiced then " +
				"to_char(inv.XX_DueDate,'dd.mm.yyyy') else to_char(inv.DateInvoiced,'dd.mm.yyyy') " +
				"end) FECVEN,  "+
				"ord.DocumentNo, psl.PayAmt MONASI, to_char(psl.PayAmt, '999G999G999G999D99') FORMATO, con.XX_Lease, par.value  " +
				"from C_PaySelection pay  " +
				"inner join C_PaySelectionLine psl on (pay.C_PaySelection_ID = psl.C_PaySelection_ID AND psl.C_PaySelectionLine_ID="+idlinea+") " +
				"inner join C_Invoice inv on (psl.C_Invoice_ID = inv.C_Invoice_ID) " +
				"inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
				"inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
				"left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
				"left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
				"where pay.C_PaySelection_ID = " + id;
		
		int tipoCategoria = 0;
		String accountaux = "";
		StringBuffer m_where = new StringBuffer();
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
								
				if (rs.getString("XX_POType") == null)
					if ((rs.getString("XX_LEASE")==null) || rs.getString("XX_LEASE").equals("N"))
						//Para Servicios que no sean Arrendadmientos se pasa la categoria de producto Standard (CCAPOTE)
						tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID");
					else {
						//Para Servicios de tipo Arrendadmientos se pasa la categoria de producto Servicio con la marca de Arrendamiento (CCAPOTE)
						tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
						m_where.append(" and XX_Lease = 'Y' ");
					}
				else{
					// Para Mercancia para la Venta
					if (rs.getString("XX_PurchaseType") == null){
						tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID");
						// Para ubicar la cuenta que le corresponde a Mercancia (CCAPOTE)  
						m_where.append(" and XX_ElementType = 'Nacional' " +
									   "and XX_Transitional <> 'Y' ");
					}
					else if ((rs.getString("XX_PurchaseType").equals("SU")) || (rs.getString("XX_PurchaseType").equals("SE")))
						//Se pasa servicios porque es la misma cuenta tanto la servicios como para sumistros y materiales
						tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID"); 					
					else if (rs.getString("XX_PurchaseType").equals("FA")){
						//Para Activo Fijo se le pasa categoría del producto “Servicio” y marcada como “Cuentas por Pagar” 
						tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
						m_where.append(" and XX_AccountPayable = 'Y' ");
					}
				}
				accountaux = accounts(tipoCategoria, m_where);
			}
			
		}catch(Exception e){
			log.log(Level.SEVERE, sql);	
		}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
		}

		
		return accountaux;
	}

	/*
	 *METODO QUE SE ENCARGARA DE BUSCAR LA CUENTA CONTABLE CUANDO ES ANTICIPO 	
	 */
		private static String getCategoryAnticipo(String id) throws SQLException{
			
			String sql = "select bp.value VALUE, ord.XX_POType, ord.XX_PurchaseType, " +
					"ord.DocumentNo NRODOC, to_char(ord.Created,'dd.mm.yyyy') FECDOC, " +
					"to_char(sysdate,'dd.mm.yyyy') FECVEN, pay.PayAmt MONASI,to_char(pay.PayAmt, '999G999G999G999D99') FORMATO, to_char('ANT') TIPDOC " +
					"from C_Payment pay " +
					"inner join C_Order ord on (pay.C_Order_ID = ord.C_Order_ID) " +
					"inner join C_bpartner bp on (ord.c_bpartner_id = bp.c_bpartner_id) "+
					"where pay.C_Payment_ID = " + id;
			
			int tipoCategoria = 0;
			String accountaux = "";
			StringBuffer m_where = new StringBuffer();
			PreparedStatement pstmt = null; 
			ResultSet rs = null; 
			try{
				pstmt = DB.prepareStatement(sql, null); 
				rs = pstmt.executeQuery(); 
				if(rs.next()){
									
					if (rs.getString("XX_POType") == null)
						if ((rs.getString("XX_LEASE")==null) || rs.getString("XX_LEASE").equals("N"))
							//Para Servicios que no sean Arrendadmientos se pasa la categoria de producto Standard (CCAPOTE)
							tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID");
						else {
							//Para Servicios de tipo Arrendadmientos se pasa la categoria de producto Servicio con la marca de Arrendamiento (CCAPOTE)
							tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
							m_where.append(" and XX_Lease = 'Y' ");
						}
					else{
						// Para Mercancia para la Venta
						if (rs.getString("XX_PurchaseType") == null){
							tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID");
							// Para ubicar la cuenta que le corresponde a Mercancia (CCAPOTE)  
							m_where.append(" and XX_ElementType = 'Nacional' " +
										   "and XX_Transitional <> 'Y' ");
						}
						else if ((rs.getString("XX_PurchaseType").equals("SU")) || (rs.getString("XX_PurchaseType").equals("SE")))
							//Se pasa servicios porque es la misma cuenta tanto la servicios como para sumistros y materiales
							tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID"); 					
						else if (rs.getString("XX_PurchaseType").equals("FA")){
							//Para Activo Fijo se le pasa categoría del producto “Servicio” y marcada como “Cuentas por Pagar” 
							tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
							m_where.append(" and XX_AccountPayable = 'Y' ");
						}
					}
					accountaux = accounts(tipoCategoria, m_where);
				}
				
			}catch(Exception e){
				log.log(Level.SEVERE, sql);	
			}finally{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
			}

			
			return accountaux;
		}
	
	
	/**
	 * Se encarga de buscar la cuenta contable, a través de los parámetros de entrada
	 * @param tipoOC Tipo de la Orden de Compra
	 * @param tipoBien Tipo de Bien
	 * @return La cuenta contable
	 */
	public static String accounts (int tipoCategoria, StringBuffer m_where){
		String sqlAccount = "select cev.value, cev.description " +
		 					"from C_ElementValue cev " +
		 					"inner join M_Product_Category mpc on (cev.M_Product_Category_ID = mpc.M_Product_Category_ID) " +
		 					"where cev.AccountType = 'L' " +
							"and mpc.M_Product_Category_ID = " + tipoCategoria + m_where;
		
		String account = "";
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		try{
			pstmt = DB.prepareStatement(sqlAccount, null); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				account = rs.getString(1)+" "+rs.getString(2);
			}
		}catch(Exception e){
			log.log(Level.SEVERE, sqlAccount);	
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return account;
	}

	
	private static String getIdAnticipo(String id) throws SQLException{
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String idAnticipo = null;
		
		String  sql= 
				"SELECT C_PAYMENT_ID" +
				" FROM C_PAYMENT" +
				" WHERE DOCUMENTNO = '"+id+"'";
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				idAnticipo = (String)rs.getString("C_PAYMENT_ID");
			}
		}catch(Exception e){
			log.log(Level.SEVERE, sql);	
		}finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
			
		
		return idAnticipo;
	}

/*
 *METODO QUE OBTIENE EL CONTRATO DE UN ANTICIPO DE PAGO 
*/
	private static String getContrato(String id) throws SQLException{
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String Contrato = "";
		
		String  sql= 
				"SELECT C_ORDER_ID AS C1, XX_CONTRACT_ID AS C2" +
				" FROM C_PAYMENT" +
				" WHERE C_PAYMENT_ID = "+id;
		
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				if(rs.getString("C1") != null && rs.getString("C2") == null)
					Contrato = (String)rs.getString("C1");
				if(rs.getString("C1") == null && rs.getString("C2") != null)
					Contrato = getContratoLease((String)rs.getString("C2"));
				if(rs.getString("C1") == null && rs.getString("C2") == null)
					Contrato = " ";
				
			}
		}catch(Exception e){
			log.log(Level.SEVERE, sql);	
		}finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
			
		return Contrato;
	}
	
	
private static String getContratoLease(String id) throws SQLException{
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String Contrato = "";
		
		String  sql= 
				"SELECT XX_LEASE AS C" +
				" FROM XX_CONTRACT" +
				" WHERE XX_CONTRACT_ID = "+id;
		
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				if(rs.getString("C1") != null)
					Contrato = (String)rs.getString("C");
			}
		}catch(Exception e){
			log.log(Level.SEVERE, sql);	
		}finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
			
		return Contrato;
	}

/*
 * METODO QUE SE ENCARGA DE BUSCAR EL ID DE UNA TRANSFERENCIA DE UNA SELECCION DE PAGO
 */
private static String buscarNumeroTransferencia(String Id) {
	// TODO Auto-generated method stub
	String transferencia="";
	String sql=
			"SELECT CP.XX_VCN_BANKTRANSFER_ID as ID"
			+" FROM C_PAYSELECTIONCHECK CPSC, C_PAYMENT CP"
			+" WHERE CP.C_PAYMENT_ID= CPSC.C_PAYMENT_ID"
			+" AND CPSC.C_PAYSELECTIONCHECK_ID= "+ Id;
	
	PreparedStatement pstmt = null; 
	ResultSet rs = null;
	
	try{
		pstmt = DB.prepareStatement(sql, null); 
		rs = pstmt.executeQuery(); 
		if(rs.next()){
				transferencia = Integer.toString(rs.getInt(1));
		}
	}catch(Exception e){
		log.log(Level.SEVERE, sql);	
	}finally 
	{
		DB.closeResultSet(rs);
		DB.closeStatement(pstmt);
	}
		
	return transferencia;	
}

/*
 * METODO QUE SE ENCARGA DE BUSCAR EL ID DE UNA TRANSFERENCIA DE UN ANTICIPO DE PAGO
 */
private static String buscarNumeroTransferenciaAnticipo(String Id) {
	// TODO Auto-generated method stub
	String transferencia="";
	String sql=
			"SELECT CP.XX_VCN_BANKTRANSFER_ID as ID"
			+" FROM C_PAYSELECTIONCHECK CPSC, C_PAYMENT CP"
			+" WHERE CP.C_PAYMENT_ID= "+ Id;
	
	PreparedStatement pstmt = null; 
	ResultSet rs = null;
	
	try{
		pstmt = DB.prepareStatement(sql, null); 
		rs = pstmt.executeQuery(); 
		if(rs.next()){
				transferencia = Integer.toString(rs.getInt(1));
		}
	}catch(Exception e){
		log.log(Level.SEVERE, sql);	
	}finally 
	{
		DB.closeResultSet(rs);
		DB.closeStatement(pstmt);
	}
		
	return transferencia;

}

}//XX_VCN_PAYSPECIFICATION_FORM
