package compiere.model.payments.forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.model.MOrderLine;
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

import compiere.model.cds.MInvoice;
import compiere.model.cds.MInvoiceLine;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;


/**
 * Forma en compiere: Invoice Client
 * Se encarga de crear los diferentes registros en factura(cliente) 
 * a partir de una orden de compra especifica, además de generar
 * dos (2) reportes para imprimir la factura.
 * 
 * @author Jessica Mendoza
 *
 */
public class XX_InvoiceClientForm extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	/****Window No****/
	private int m_WindowNo = 0;
	
	/****Logger****/
	static CLogger log = CLogger.getCLogger(XX_InvoiceClientForm.class);
	
	/****Cliente****/
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	StringBuffer m_sql = null;
	StringBuffer m_sqlDetail = null;
	String m_groupBy = "";
	String m_orderBy = "";
	Ctx ctx=Env.getCtx();
	
	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private GridBagLayout centerLayout = new GridBagLayout();

	/****Botones****/
	private CButton bGenerate = new CButton();
	
	/****Labels-ComboBox-Fecha****/
	private CLabel ordenCompraLabel = new CLabel();
	private CComboBox ordenCompraCombo = new CComboBox();

	/****Variables****/
	float cuenta = 0;
	String cuentaS;
	static Integer sync = new Integer(0);
	Utilities util = new Utilities();

	@Override
	/**
	 * Inicializa la forma
	 */
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		try{
			jbInit();
			dynInitNorth();
			frame.getContentPane().add(mainPanel, BorderLayout.NORTH);	
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
		ordenCompraLabel.setText(Msg.getMsg(Env.getCtx(), "XX_PurchaseOrder"));
		ordenCompraCombo.setBackground(false);
		ordenCompraCombo.setBackground(Color.PINK);
		bGenerate.setText(Msg.translate(Env.getCtx(), "Generate"));
		bGenerate.setEnabled(true);	
		bGenerate.addActionListener(this);

		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		northPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				Msg.translate(Env.getCtx(), "XX_InvoiceClientMsg")));
		xPanel.setLayout(xLayout);
		
		centerPanel.setLayout(centerLayout);
		xPanel.setLayout(xLayout);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		/**
		 * Panel Superior
		 */		
		/****Etiquetas de la primera fila****/
		northPanel.add(ordenCompraLabel, new GridBagConstraints(0, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(ordenCompraCombo, new GridBagConstraints(1, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 20), 0, 0));

		/****Etiquetas de la segunda fila****/
		centerPanel.add(bGenerate, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(10, 5, 5, 5), 0, 0));

	}
	
	/**
	 * Carga las ordenes de compra de tipo mercancia
	 */
	void dynOrdenCompra() {
		ordenCompraCombo.removeAllItems();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select C_Order_ID, documentNo " + 
					 "from C_Order " +
					 "where XX_OrderStatus  NOT IN ('AN', 'PRO') " +
					 "and XX_OrderType = 'Nacional' " +
					 "and XX_PoType = 'POM' " +
					 "and XX_INVOICINGSTATUS = 'PE' " +
					 "and isSoTrx='N' " +
					 //"and XX_ImportingCompany_ID <> 0 " + 
					 "order by documentNo ";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordenCompraCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			ordenCompraCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
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
	 * Se encarga de llamar al metodo que llena los filtros
	 */
	private void dynInitNorth(){
		dynOrdenCompra();
	}
	
	/**
	 *  Ejecuta el metodo correspondiente a la accion
	 *  @param e La acccion del evento
	 */
	public void actionPerformed (ActionEvent e){			
		if (e.getSource() == bGenerate){
			cmdGenerate();
			dynInitNorth();
		}
	}
	
	/**
	 *  Busca la informacion a través de los filtros
	 */
	private void cmdGenerate(){	
		int idOrder = 0;
		int lineas = 0;
		int cantLineas = 13; //Son la cantidad de líneas que se deben almacenar por factura cliente
		Date fecha = new Date();		
		Timestamp fechaActual = new Timestamp(fecha.getTime());
		
		if ((ordenCompraCombo.getSelectedItem() != null)){
			idOrder = ((KeyNamePair)ordenCompraCombo.getSelectedItem()).getKey();
					
			MOrder po = new MOrder(Env.getCtx(),idOrder,null);			
			MOrderLine[] poLines = po.getLines(true, null);
			if (poLines == null || poLines.length == 0){
				log.warning("No Lines - " + po);
			}

			lineas = poLines.length; //cantidad de líneas que tiene la OC
			int i = 0;
			while (lineas > 0){
				/****Generar la factura(cliente)****/
				MInvoice invoice = new MInvoice(Env.getCtx(),0,null);
				//invoice.setAD_Client_ID(po.getXX_ImportingCompany_ID()); //AGREGAR COMO AD_CLIENT LA COMPAÑIA IMPORTADORA DE LA OC
				invoice.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCONTROL_ID"));
				invoice.setAD_Client_ID(po.getAD_Client_ID()); //DE PRUEBA
				invoice.setC_Order_ID(po.getC_Order_ID());
				invoice.setDateOrdered(po.getDateOrdered());
				invoice.setPOReference(po.getPOReference());
				invoice.setDescription(po.getDescription());
				invoice.setDateAcct(fechaActual);
				invoice.setXX_ParcelQty(1);
				//invoice.setC_BPartner_ID(po.getAD_Client_ID()); //AGREGAR COMO SOCIO DE NEGOCIO EL AD_CLIENT DE LA OC
				invoice.setC_BPartner_ID(po.getC_BPartner_ID()); //DE PRUEBA
				invoice.setC_BPartner_Location_ID(po.getC_BPartner_Location_ID());
				invoice.setAD_User_ID(po.getAD_User_ID());
				invoice.setM_PriceList_ID(po.getM_PriceList_ID());
				invoice.setC_Currency_ID(po.getC_Currency_ID());
				invoice.setSalesRep_ID(po.getSalesRep_ID());
				invoice.setDocStatus("DR");
				invoice.setC_DocType_ID(po.getC_DocType_ID());
				invoice.setC_PaymentTerm_ID(po.getC_PaymentTerm_ID());
				invoice.setIsSOTrx(true);
				
				invoice.setPaymentRule(po.getPaymentRule());
				invoice.setCreateFrom("N");
				invoice.setCopyFrom("N");
				invoice.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID"));
				invoice.setC_DocTypeTarget_ID(invoice.getC_DocType_ID());
				invoice.setXX_InvoiceType("I");
				invoice.setIsSOTrx(false);

				invoice.save();
							
				/****Generar las líneas de la factura(cliente)****/		
				int cont = 0;
				while (cont <= cantLineas-1 && i != poLines.length){							
					MInvoiceLine invoiceLine = new MInvoiceLine (invoice);
					invoiceLine.setC_Invoice_ID(invoice.getC_Invoice_ID());
					invoiceLine.setM_InOutLine_ID(idMInOutLine(poLines[i].getM_Product_ID()));
					invoiceLine.setM_Product_ID(poLines[i].getM_Product_ID());
					invoiceLine.setDescription(poLines[i].getDescription());
					invoiceLine.setQtyEntered(poLines[i].getQtyEntered());			
					invoiceLine.setPriceEntered(poLines[i].getPriceEntered());
					invoiceLine.setPriceActual(poLines[i].getPriceActual());
					invoiceLine.setC_OrderLine_ID(poLines[i].getC_OrderLine_ID());
					invoiceLine.setXX_PriceActualInvoice(poLines[i].getPriceEntered());
					invoiceLine.setXX_PriceEnteredInvoice(poLines[i].getPriceActual());
					invoiceLine.setPriceList(poLines[i].getPriceList());	
					invoiceLine.setC_Tax_ID(poLines[i].getC_Tax_ID());
					invoiceLine.setAD_Client_ID(invoice.getAD_Client_ID());	
					invoiceLine.save();			
					cont++;
					i += 1;
				}				
				lineas = lineas - cantLineas;
			}		
		}
	}
	
	/**
	 * Busca el identificador de la descripción del producto
	 * @param idProduct id del Producto
	 * @return
	 */
	public int idMInOutLine (int idProduct){
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		String sql = "select M_InOutLine_ID " +
				     "from M_InOutLine " +
				     "where M_Product_ID = " + idProduct;
		int id = 0;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()){
				id = rs.getInt("M_InOutLine_ID");
			}
					
		}
		catch(SQLException e){	
			e.getMessage();			
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
		return id;
	}
	
	/**
	 * Busca el porcentaje de ganancia para realizar el re-calculo 
	 * del precio del producto
	 * @return
	 */
	public BigDecimal porcentageGanancia(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select XX_Percentage from XX_VCN_PercentageReturn ";
		BigDecimal porcentaje = new BigDecimal(0);
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()){
				porcentaje = rs.getBigDecimal("XX_Percentage");
			}
					
		}
		catch(SQLException e){	
			e.getMessage();			
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
		return porcentaje;
	}

	/**
	 * Se encarga de tomar el registro seleccionado de la primera tabla y llamar 
	 * al metodo tableInitS() que se encarga de llenar la segunda tabla y a su 
	 * vez el tableInitT() que se encarga de llenar la tercera tabla
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {		
	
	}

	@Override
	public void dispose() {
		
	}
}
