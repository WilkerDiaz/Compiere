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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
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

import compiere.model.cds.X_Ref_AD_User_NotificationType;
import compiere.model.cds.X_Ref_XX_StatusReturn;
/**
 * forma para consultar todas las devolucion es generadas.
 * @author Rebecca Principal
 *
 */

public class XX_ReturnsSearchForm extends CPanel
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
static CLogger 			log = CLogger.getCLogger(XX_ReturnsSearchForm.class);

private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
private int     m_by = Env.getCtx().getAD_User_ID();

static StringBuffer    m_sql = null;
static StringBuffer    m_sqlDetail = null;
static String          m_groupBy = "";
static String          m_orderBy = "";
//panel - tablas
private CPanel mainPanel = new CPanel();
private StatusBar statusBar = new StatusBar();
private BorderLayout mainLayout = new BorderLayout();
private CPanel northPanel = new CPanel();
private GridBagLayout northLayout = new GridBagLayout();
private CPanel southPanel = new CPanel();
private GridBagLayout southLayout = new GridBagLayout();
private CPanel centerPanel = new CPanel();
private TitledBorder xTableBorder = new TitledBorder(Msg.getMsg(Env.getCtx(), "XX_VLO_ReturnOfProduct_ID"));
private BorderLayout centerLayout = new BorderLayout(5,5);
private  MiniTable xTable = new MiniTable();
private JScrollPane xScrollPane = new JScrollPane();
private MiniTable xTable2 = new MiniTable();
private TitledBorder xTableBorder2 = new TitledBorder(Msg.getMsg(Env.getCtx(),"ReturnDetail"));
private JScrollPane xScrollPane2 = new JScrollPane();
//private CPanel xPanel = new CPanel();
/** Calls   = C */
public static final String NOTIFICATIONTYPE_Calls = X_Ref_AD_User_NotificationType.CALLS.getValue();
/** EMail = E */
public static final String NOTIFICATIONTYPE_EMail = X_Ref_AD_User_NotificationType.E_MAIL.getValue();

//Codigo de la Devolucion
private CLabel DocNumber = new CLabel(Msg.getMsg(Env.getCtx(), "Return No"));
private static CComboBox comboReturnToSearch = new CComboBox();
//BUSINESS PARTNER
private CLabel labelBPartner = new CLabel(Msg.getMsg(Env.getCtx(), "BPartner"));
private static CComboBox comboBPartner = new CComboBox();
//Motivo de Devoluciones
private CLabel ReturnM = new CLabel(Msg.getMsg(Env.getCtx(), "Return Motive"));
private static  CComboBox comboReturnMToSearch = new CComboBox();
//Estatus De la Devolucion
private  CLabel StatusReturn = new CLabel(Msg.getMsg(Env.getCtx(),"XX_Status"));
private static  JComboBox comboStatusReturnSearch = new CComboBox();
private static Vector<String> EstDevol = new Vector<String>();
//Orden de Compra
private CLabel ordenLabel = new CLabel(Msg.translate(Env.getCtx(), "C_Order_ID"));
private static VLookup orden = null;
//Factura
private CLabel invoiceLabel = new CLabel(Msg.translate(Env.getCtx(), "C_Invoice_ID"));
private CTextField invoice =  new CTextField();
//buttons
private CButton bSearch = new CButton();
private JButton bReset = ConfirmPanel.createResetButton(Msg.getMsg(Env.getCtx(),"Clear"));

static //variable para e id de la columna id de devolucion seleccionda
KeyNamePair returnKey=null;
static int ReturnID=0;



private static int tableInit_option;


private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);

KeyNamePair loadKNP = new KeyNamePair(0, "");
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
	
	orden = createOrder(m_WindowNo);
	
	invoice.setPreferredSize(new Dimension(100, 20));
	invoice.setText("");

	//buttons
	bSearch.setText(Msg.translate(Env.getCtx(), "Search"));
	bSearch.setPreferredSize(new Dimension(80,30));	
	bSearch.setEnabled(true);
	
	bReset.addActionListener(this);
	
	xScrollPane.setBorder(xTableBorder);
	xScrollPane.setPreferredSize(new Dimension(800, 600));
	
	xScrollPane2.setBorder(xTableBorder2);
	xScrollPane2.setVisible(false);
	xScrollPane2.setPreferredSize(new Dimension(800, 200));
	
	mainPanel.add(northPanel,  BorderLayout.NORTH);
	mainPanel.add(southPanel,  BorderLayout.SOUTH);
	mainPanel.add(centerPanel, BorderLayout.CENTER);
	
	centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	centerPanel.add(xScrollPane2,  BorderLayout.SOUTH);

	xScrollPane.getViewport().add(xTable, null);
	xScrollPane2.getViewport().add(xTable2, null);	
	
		northPanel.add(DocNumber,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(comboReturnToSearch,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(labelBPartner,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboBPartner,        new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(ReturnM,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboReturnMToSearch,        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(StatusReturn,      new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(comboStatusReturnSearch,        new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(ordenLabel,      new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(orden, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		northPanel.add(invoiceLabel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(invoice, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 0), 0, 0));
		
		northPanel.add(bSearch,   new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 12, 5, 12), 0, 0));
		northPanel.add(bReset,    new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 12, 5, 12), 0, 0));
		
		southPanel.validate();
		
}   //  jbInit


/**
 *  Table initial state 
 */
private void loadBasicInfo(){
	
	comboReturnToSearch.removeActionListener(this);
	comboBPartner.removeActionListener(this);
	comboReturnMToSearch.removeActionListener(this);
	comboStatusReturnSearch.removeActionListener(this);
	orden.removeActionListener(this);
	
	//Restore ComboBoxes and CheckBoxes		
	
	comboReturnToSearch.setEnabled(true);
	comboBPartner.setEnabled(true);
	comboReturnMToSearch.setEnabled(true); 
	comboStatusReturnSearch.setEnabled(true);
	orden.setValue(null);
	
	comboReturnToSearch.removeAllItems();
	comboBPartner.removeAllItems();
	comboReturnMToSearch.removeAllItems();
	comboStatusReturnSearch.removeAllItems();
	//Llenar los filtros de busquedas
	llenarcombos();
	
}

/**
 *  Dynamic Init.
 *  Table Layout, Visual, Listener
 */
private void dynInit()
{	
		//Llenar los filtros de busquedas
		loadBasicInfo();
		// generacion de las columnas de Cabecera y detalle para devoluciones
		ColumnInfo[] layout = new ColumnInfo[] {
				//Numero de la devolucion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VLO_ReturnOfProduct_ID"),   ".", KeyNamePair.class),            //  1  
				//Fecha de la Devolucion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DateDEV"), ".", String.class),	//2
				//Total Producto a Devolver 
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalPieces"), ".", String.class),	//2
				//Estado de la Devolucion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Status"),"XX_Status", String.class),				//  6
				//pilas con la orde de compra correlativa
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_ORDER_ID"),  ".",KeyNamePair.class),//  3
				//Proveedor
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),   ".", KeyNamePair.class),         // 4
				//Nombre de la Persona Retira la Devolucion   
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NPERWITHDRAWSRETURN"),"XX_NPERWITHDRAWSRETURN", String.class),				// 8
				//Fecha de Retiro de la  Devolucion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_DateDeliveryVendor"), ".", String.class),	//2
				// Tipo de Notificacion 
				new ColumnInfo(Msg.translate(Env.getCtx(), "NOTIFICATIONTYPE"),"NOTIFICATIONTYPE", String.class),				// 7
				//Fecha 1°er Aviso
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NOTIFICATIONDATE"),  ".", String.class),	//2
				//Fecha 2°do Aviso
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NOTIFICATIONDATE2"),  ".", String.class),	//2
				//Fecha 3r Aviso
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NOTIFICATIONDATE3"),  ".", String.class),	//2
				//Asistente de Chequeo
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_IdAsis_ID"),".", KeyNamePair.class),
				//Tienda
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Warehouse_ID"),".", String.class),
				//N° de movimiento de Tienda
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Movement_ID"),".", String.class)
		};
			
		ColumnInfo[] layoutDetail = new ColumnInfo[] {
				// Referencia del Proveedor
				new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductRef"),   ".", KeyNamePair.class),	
				// Código del Producto
				new ColumnInfo(Msg.translate(Env.getCtx(), "ProductKey"),   ".", String.class),
				// Descripcion del prodcuto
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Product_ID"),   ".", KeyNamePair.class),
				// Total de Piezas por producto a devolver
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_TotalPieces"),          ".", Integer.class),
				// motivo de la Devolucion
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_CancellationMotive_ID"),   ".", KeyNamePair.class),
				//costo
				new ColumnInfo(Msg.translate(Env.getCtx(), "PriceActual"),   ".", BigDecimal.class),
				//costo
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_SalePrice"),   ".", BigDecimal.class) 
			};


		
		xTable.prepareTable(layout, "", "", false, "");
		xTable.setAutoResizeMode(3);
		xTableBorder.setTitle(Msg.getMsg(Env.getCtx(),"Return"));
		//xScrollPane.repaint();
	
		xTable2.prepareTable(layoutDetail, "", "", true, "");
		xTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		xTableBorder2.setTitle(Msg.getMsg(Env.getCtx(),"ReturnDetail"));
		xTable2.setAutoResizeMode(3);
		//xScrollPane2.repaint();

	//  Visual
	CompiereColor.setBackground (this);

	//  Listener
	xTable.getSelectionModel().addListSelectionListener(this);
	xTable.getModel().addTableModelListener(this);
	xTable.addMouseListener(new MouseListener() {
		
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
						
					}
				}
			}
		}
	});
					
	bSearch.addActionListener(this);
	bReset.addActionListener(this);
	orden.addActionListener(this);

	statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "XX_Returns"));
	statusBar.setStatusDB(0);
	//Cargo  tabla 1 Devoluciones
	tableInit_option=0;
	tableInit(invoice);
	tableLoad (xTable);	
	
	//  Init
	statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "XX_Returns"));	
	statusBar.setStatusDB(xTable.getRowCount());
	
	xTable.setAutoResizeMode(MiniTable.AUTO_RESIZE_OFF);
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
	if (e.getSource() == bSearch){
		cmd_Search();
	}else{
		//Clean Form
		if(e.getSource() == bReset){}
			loadBasicInfo();
	}
	
}   //  actionPerformed

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Search()
	{		
		tableInit_option=0;		
		tableInit(invoice);
		tableLoad (xTable);
		if(xTable.getRowCount()!=0){
			xScrollPane2.setVisible(false);
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "XX_Returns"));
			statusBar.setStatusDB(xTable.getRowCount());
		}
		else{
			xScrollPane2.setVisible(false); //escondo el panel de motivos de rechazo
			ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(),"FindZeroRecords"));	
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"FindZeroRecords"));
		}
	}   //  cmd_Search
  
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
private static void tableInit (CTextField invoice)
{
	m_sql = new StringBuffer ();
	m_sqlDetail=new StringBuffer ();

	if(tableInit_option==0)
	{
		//add list pendiente part.ISVENDOR='Y' AND empl.ISEMPLOYEE='Y' AND
		m_sql.append("SELECT Distinct tab.VALUE, tab.XX_VLO_RETURNOFPRODUCT_ID, tab.Created as XX_DATEDEV, sum(rdet.XX_TotalPieces) as Total, rlis.name,ord.DocumentNo, tab.C_ORDER_ID,part.Name, part.C_BPARTNER_ID, tab.XX_NPERWITHDRAWSRETURN, tab.XX_DATEDELIVERYVENDOR, " +
				"(select Name from  AD_Ref_List where AD_Reference_ID=344 AND Value=tab.NOTIFICATIONTYPE),tab.XX_NOTIFICATIONDATE, tab.XX_NOTIFICATIONDATE2, tab.XX_NOTIFICATIONDATE3, empl.Name, tab.XX_IDASIS_ID, wr.value, tab.xx_DocumentNo " 
				+ "FROM XX_VLO_RETURNOFPRODUCT tab, C_BPartner part, C_BPartner empl, XX_VMR_CANCELLATIONMOTIVE mot, XX_VLO_RETURNDETAIL rdet, " +
				  "C_Order ord, AD_Ref_List rlis, M_Warehouse wr "
				+ "WHERE  tab.ISACTIVE='Y' AND part.ISACTIVE='Y' "
				+ "AND part.C_BPartner_ID=tab.C_BPartner_ID "
				+ "AND empl.C_BPartner_ID(+)=tab.XX_IDASIS_ID "
				+ "AND tab.M_WAREHOUSE_ID = wr.M_WAREHOUSE_ID(+) "
				+ "AND tab.XX_VLO_RETURNOFPRODUCT_ID=rdet.XX_VLO_RETURNOFPRODUCT_ID "
				+ "AND rdet.XX_VMR_CANCELLATIONMOTIVE_ID=mot.XX_VMR_CANCELLATIONMOTIVE_ID "
				+ "AND ord.C_Order_ID(+)=tab.C_Order_ID "
				+ "AND rlis.AD_Reference_ID="+X_Ref_XX_StatusReturn.AD_Reference_ID 
				+ " AND rlis.Value=TRIM(tab.XX_Status) ");
		m_groupBy = " GROUP BY tab.VALUE, tab.XX_VLO_RETURNOFPRODUCT_ID, tab.Created , rlis.name, ord.DocumentNo, tab.C_ORDER_ID,part.Name, part.C_BPARTNER_ID, tab.XX_NPERWITHDRAWSRETURN, tab.XX_DATEDELIVERYVENDOR,tab.XX_NOTIFICATIONDATE, tab.XX_NOTIFICATIONDATE2, tab.XX_NOTIFICATIONDATE3, empl.Name, tab.XX_IDASIS_ID, NOTIFICATIONTYPE, wr.value, tab.xx_DocumentNo "; 
		m_orderBy = " order by tab.Value ";
		
	}else
		if(tableInit_option==1)
		{
			m_sql.append("SELECT Distinct ref.value, ref.XX_VMR_VendorProdRef_ID, prod.Value, prod.Name, prod.M_Product_ID, " +
						"rdet.XX_TOTALPIECES,mot.Name, mot.XX_VMR_CANCELLATIONMOTIVE_ID, " +
						"(CASE WHEN asi.priceactual is not null then asi.priceactual else mov.priceactual END) priceactual, " +
						"(CASE WHEN asi.xx_saleprice is not null then asi.xx_saleprice else mov.xx_saleprice END) xx_salesprice " +
						"FROM XX_VLO_RETURNOFPRODUCT tab " +
						"INNER JOIN C_BPARTNER part ON (part.C_BPartner_ID = tab.C_BPartner_ID) " +
						"INNER JOIN XX_VLO_RETURNDETAIL rdet ON (tab.XX_VLO_RETURNOFPRODUCT_ID=rdet.XX_VLO_RETURNOFPRODUCT_ID) " +
						"INNER JOIN XX_VMR_CANCELLATIONMOTIVE mot ON (rdet.XX_VMR_CANCELLATIONMOTIVE_ID=mot.XX_VMR_CANCELLATIONMOTIVE_ID) "+
						"INNER JOIN M_Product prod ON (rdet.M_Product_ID= prod.M_Product_ID) "+
						"INNER JOIN XX_VMR_VendorProdRef ref ON (ref.XX_VMR_VendorProdRef_ID=prod.XX_VMR_VENDORPRODREF_ID) "+
						"LEFT OUTER JOIN M_MovementLine mov ON (mov.M_Movement_ID = tab.M_Movement_ID and mov.M_Product_ID = prod.M_Product_ID) "+
						"LEFT OUTER JOIN M_AttributeSetInstance asi ON (mov.M_AttributeSetInstance_ID = asi.M_AttributeSetInstance_ID) "+
						"WHERE  tab.XX_VLO_RETURNOFPRODUCT_ID=" +returnKey.getID()
									);
			m_orderBy = " order by mot.Name, ref.value ";			
		}
	//Busqueda por Id de devolucion pilas -1
	if(comboReturnToSearch.getSelectedIndex()!=0){
		int clave_Return=((KeyNamePair)comboReturnToSearch.getSelectedItem()).getKey();
		m_sql.append(" AND ").append("tab.XX_VLO_RETURNOFPRODUCT_ID=").append(clave_Return);
	}
	//busqueda por proveedor
	if(comboBPartner.getSelectedIndex()!=0){
		int clave_vendor=((KeyNamePair)comboBPartner.getSelectedItem()).getKey();
		m_sql.append(" AND ").append("part.C_BPartner_ID=").append(clave_vendor);
	}
	//Busqueda por Motivo de Devolucion esta en la tabla de detalles
	if((tableInit_option==0)&&(comboReturnMToSearch.getSelectedIndex()!=0)){	
	int clave_ReturMotive=((KeyNamePair)comboReturnMToSearch.getSelectedItem()).getKey();
	m_sql.append(" AND ").append("mot.XX_VMR_CANCELLATIONMOTIVE_ID=").append( clave_ReturMotive);
	}
	//Busqueda por Estado de la Devolucion 
	if((tableInit_option==0)&&(comboStatusReturnSearch.getSelectedIndex()!=0)){	
		int clave_ReturnStatus=((KeyNamePair)comboStatusReturnSearch.getSelectedItem()).getKey();
		m_sql.append(" AND ").append("rlis.AD_Ref_List_ID= ").append("'").append(clave_ReturnStatus).append("'");
		
	}
	//Búsqueda por Orden de Compra
	if(orden.getValue() != null){
		m_sql.append(" AND ").append("tab.C_Order_ID=").append(orden.getValue());
	}
	//Búsqueda por Factura (Credito)
	if(!invoice.getText().equals("")){
		m_sql.append(" AND ")
		.append("tab.C_Invoice_ID in (select C_Invoice_ID from C_Invoice WHERE isSOTrx='N' and DocumentNo='")
		.append(invoice.getText().trim()).append("')");
	}
	
}   //  tableInit
/**************************************************************************
 *  List Selection Listener
 *  @param e event
 */
public void valueChanged (ListSelectionEvent e)
{
	
	int tableRow = xTable.getSelectedRow();
	
	if(tableRow!=-1) //Si no tengo nada seleccionado en la tabla de O/C
	{
		int orderRow = xTable.getSelectedRow();
		KeyNamePair orderKey = (KeyNamePair)xTable.getValueAt(orderRow, 0);
		returnKey=orderKey;
		xScrollPane2.setVisible(true); //muestro el panel de motivos de rechazo
		//cargar la tabla 2
		tableInit_option=1;
		tableInit(invoice);
		tableLoad (xTable2);
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(),"SelectedReturn"));
		
		statusBar.setStatusDB(orderKey.getName());
	
	}
	
	if (e.getValueIsAdjusting())
		return;
}   //  valueChanged

/**
 *  Fill the table using m_sql
 *  @param table table
 */
private static void tableLoad (MiniTable table)
{
	String sql = "";
	
	if(tableInit_option==0)
	{
		sql = MRole.getDefault().addAccessSQL(
			  m_sql.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ m_groupBy + m_orderBy;
	}
	else
	{
		sql = MRole.getDefault().addAccessSQL(
			  m_sql.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
		    + m_orderBy;
	}
	
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
public class ComboBoxEditor extends DefaultCellEditor {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComboBoxEditor(Vector<KeyNamePair> items) {
        super(new JComboBox(items));
    }
}
/**
 * 
 */
private  void llenarcombos(){
	//Cargar informacion de Proveedor
	String sql = "SELECT XX_VLO_ReturnOfProduct_ID, Value FROM XX_VLO_ReturnOfProduct ORDER BY Value ASC";
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = pstmt.executeQuery();
		loadKNP=new KeyNamePair(0, new String());
		
		comboReturnToSearch.addItem(loadKNP);
		
		while (rs.next())
		{
			loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
			comboReturnToSearch.addItem(loadKNP);//agregar los id devoluciones existentes!
		}
		rs.close();
		pstmt.close();
	}
	catch (SQLException e)
	{
		log.log(Level.SEVERE, sql, e);
	}
	//Loading BPARTNER
	sql = "SELECT Distinct part.C_BPARTNER_ID, part.VALUE, part.NAME FROM C_BPARTNER part, XX_VLO_ReturnOfProduct tab WHERE part.ISVENDOR='Y' AND part.C_BPartner_ID=tab.C_BPartner_ID  ORDER BY part.NAME ASC";
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = pstmt.executeQuery();
		loadKNP=new KeyNamePair(0, new String());
		comboBPartner.addItem(loadKNP);//
		while (rs.next())
		{
			loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2)+" - "+rs.getString(3));
			comboBPartner.addItem(loadKNP);//agrego los Proveedores
		}
		rs.close();
		pstmt.close();
	}
	catch (SQLException e)
	{
		log.log(Level.SEVERE, sql, e);
	}
	//Cargar Motivos de Devoluciones
	//crear keynameinfo 
	sql = "select XX_VMR_CANCELLATIONMOTIVE_ID,name from XX_VMR_CANCELLATIONMOTIVE " +
		  "where XX_VMR_MOTIVEGROUP_ID = "+ Env.getCtx().getContextAsInt("#XX_RETURNMOTIVE_ID")+" "+ //hacer keynameinfo para motive grupo @#XX_RETURNMOTIVE_ID@
		  "ORDER BY XX_VMR_CANCELLATIONMOTIVE_ID ASC";
	
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = pstmt.executeQuery();
		loadKNP=new KeyNamePair(0, new String());
		comboReturnMToSearch.addItem(loadKNP);//
		while (rs.next())
		{
			loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
			comboReturnMToSearch.addItem(loadKNP);//Agregar Motivos de Devoluciones
		}
		rs.close();
		pstmt.close();
	}
	catch (SQLException e)
	{
		log.log(Level.SEVERE, sql, e);
	}
	
	//cargar informacion de estatus de la Devolucion
	sql = "SELECT rlis.AD_Ref_List_ID, rlis.name, rlis.Value "
        + "FROM AD_Ref_List rlis, AD_Reference ref "
        + "WHERE rlis.AD_Reference_ID=ref.AD_Reference_ID AND ref.name='XX_StatusReturn'";
	
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = pstmt.executeQuery();
		loadKNP=new KeyNamePair(0, new String());
		comboStatusReturnSearch.addItem(loadKNP);//
		while (rs.next())
		{
			loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
			comboStatusReturnSearch.addItem(loadKNP);//Agregar Estatus de Devoluciones
		}
		rs.close();
		pstmt.close();
	}
	catch (SQLException e)
	{
		log.log(Level.SEVERE, sql, e);
	}	
}

/**
 *  Create Optional Order Search Lookup
 *  @param WindowNo window
 *  @return VLookup
 */
public static VLookup createOrder (int WindowNo)
{
	int AD_Column_ID = 1009800;    
	try
	{
		Lookup lookup = MLookupFactory.get (Env.getCtx(), WindowNo,
			AD_Column_ID, DisplayTypeConstants.Search);
		return new VLookup ("XX_OrderInfo_ID", false, false, true, lookup);
	}
	catch (Exception e)
	{
		log.log(Level.SEVERE, "", e);
	}
	return null;
}   //  createOrder

}
