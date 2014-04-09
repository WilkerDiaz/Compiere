package compiere.model.cds.distribution;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MClient;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Category;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;
import compiere.model.cds.X_XX_VMR_Package;
import compiere.model.cds.X_XX_VMR_Section;
import compiere.model.cds.X_XX_VMR_TypeLabel;
import compiere.model.cds.X_XX_VMR_VendorProdRef;
import compiere.model.dynamic.X_XX_VMA_Season;


/**
 *  Forma que permite imprimir etiquetas de productos de una distribución
 *  @author     Gabrielle Huchet
 *  @version    
 */
public class XX_PrintDistLabelsForm extends CPanel
	implements FormPanel, ActionListener, TableModelListener {
	

	private MVMRDistributionHeader header = null;	
	int printer_glued = 0;
	int printer_flat = 0;
	private static final long serialVersionUID = 1L;
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_PrintDistLabelsForm.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
			
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton markall = new CButton();
	private CButton generate = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();

	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xProductScrollPane = new JScrollPane();
	private TitledBorder xProductBorder =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_PlacedOrderProducts"));
	private MiniTable xProductTable = new MiniTable();	
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);

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

		Ctx aux = Env.getCtx();		
		int header_id = aux.getContextAsInt("#XX_VMR_PRINTLABEL_Distribution_ID");	
		printer_flat = aux.getContextAsInt("#XX_VMR_PRINTLABEL_Hanging");
		printer_glued = aux.getContextAsInt("#XX_VMR_PRINTLABEL_Glued");
		
			//Remove the no longer necessary items on the context		
		aux.remove("#XX_VMR_PRINTLABEL_Distribution_ID");
		aux.remove("#XX_VMR_PRINTLABEL_Hanging");
		aux.remove("#XX_VMR_PRINTLABEL_Glued");

			//Creates the Header with the necessary information					
		header = new MVMRDistributionHeader(aux, header_id , null);
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
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
			
		southPanel.setLayout(southLayout);
		generate.setText(Msg.translate(Env.getCtx(), "XX_PrintSelectedLabels"));
		generate.setEnabled(false);
		
		markall.setText(Msg.translate(Env.getCtx(), "XX_CheckAll"));
		markall.setEnabled(true);
		
		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(1024, 350));

		xPanel.setLayout(xLayout);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xProductScrollPane,  BorderLayout.CENTER);
		xProductScrollPane.getViewport().add(xProductTable, null);
		
		southPanel.add(markall,  new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,new Insets(5, 12, 5, 12), 0, 0));
		southPanel.add(generate,   new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		}   //  jbInit*/

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit() {	
						
		//Add the column definition for the table
		xProductTable.addColumn(" ");				
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Product"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "VendorProductRef"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "AttributeSetInstance"));
		xProductTable.addColumn(Msg.getMsg(Env.getCtx(), "XX_PriceConsecutive"));		
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_ProductQty"));		
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_LabelQty"));
		xProductTable.addColumn(Msg.getMsg(Env.getCtx(), "XX_Category"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Department_I"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Line_I"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Section_I"));
		
		xProductTable.setMultiSelection(true);
		
		xProductTable.setColumnClass(0, IDColumn.class, false); 		
		xProductTable.setColumnClass(1, KeyNamePair.class, true);
		xProductTable.setColumnClass(2, KeyNamePair.class, true);		
		xProductTable.setColumnClass(3, KeyNamePair.class, true);
		xProductTable.setColumnClass(4, String.class, true);		
		xProductTable.setColumnClass(5, Integer.class, true);
		xProductTable.setColumnClass(6, Integer.class, false);
		xProductTable.setColumnClass(7, KeyNamePair.class, true);
		xProductTable.setColumnClass(8, KeyNamePair.class, true);
		xProductTable.setColumnClass(9, KeyNamePair.class, true);
		xProductTable.setColumnClass(10, KeyNamePair.class, true);
		
		CompiereColor.setBackground (this);			
		xProductTable.setRowHeight(xProductTable.getRowHeight() + 2);
		generate.addActionListener(this);
		markall.addActionListener(this);

		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);  		
		if (!fillProductTable()){
			generate.setEnabled(false);
			String msg = "\nNo puede imprimir las etiquetas debido a que no se generó el consecutivo de precio de algunos productos." +
					"\nDebe ejecutar nuevamente el proceso. " +
					"\nSi el problema persiste, contacte al administrador del sistema.";
			ADialog.error(1, new Container(), msg);
		}else {
			generate.setEnabled(true);
		}
		
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
        xProductTable.getTableHeader().setReorderingAllowed(false);        
		xProductTable.getModel().addTableModelListener(this);		
		xProductTable.autoSize(true);
		xProductTable.setSortEnabled(false);
		xProductTable.repaint();
	}   //  dynInit
	
	
    private boolean fillProductTable(){    	   	
    					
    	int row = 0;
    	PreparedStatement pstmt=null;
    	ResultSet rs=null;
    	boolean result = true;
		try		
		{						
			String get_details = "\nSELECT SUM(XX_PRODUCTQUANTITY) PRODUCTQTY, PRO.M_PRODUCT_ID, PRO.M_ATTRIBUTESETINSTANCE_ID, DET.XX_PRICECONSECUTIVE, DET.XX_VMR_PACKAGE_ID " +
					"\nFROM XX_VMR_ORDERREQUESTDETAIL DET " +
					"\nJOIN XX_VMR_ORDER PE ON (PE.XX_VMR_ORDER_ID = DET.XX_VMR_ORDER_ID) " +
					"\nJOIN M_PRODUCT PRO ON (DET.M_PRODUCT_ID = PRO.M_PRODUCT_ID) " +
					"\nJOIN XX_VMR_CATEGORY CAT ON (PRO.XX_VMR_CATEGORY_ID = CAT.XX_VMR_CATEGORY_ID ) " +
					"\nJOIN XX_VMR_DEPARTMENT DEP ON (DEP.XX_VMR_DEPARTMENT_ID = PRO.XX_VMR_DEPARTMENT_ID) " +
					"\nJOIN XX_VMR_VENDORPRODREF REF ON (REF.XX_VMR_VENDORPRODREF_ID = PRO.XX_VMR_VENDORPRODREF_ID) " +					
					"\nWHERE PE.XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID() + 
					"\nGROUP BY PRO.M_PRODUCT_ID, PRO.M_ATTRIBUTESETINSTANCE_ID, DET.XX_PRICECONSECUTIVE, DET.XX_VMR_PACKAGE_ID, CAT.VALUE, DEP.VALUE, REF.VALUE, PRO.VALUE  " +
					"\nORDER BY PRO.VALUE " ;

			pstmt = DB.prepareStatement(get_details, null);			
				//Setting the query parameters
			rs = pstmt.executeQuery();	
			int i = 0;
			while (rs.next()) {
				row = xProductTable.getRowCount();				
				xProductTable.setRowCount(row + 1);
				
				IDColumn id = new IDColumn(i);
				id.setSelected(true);				
				xProductTable.setValueAt(id, row, 0);
				
				MProduct prod = new MProduct(Env.getCtx(),rs.getInt("M_PRODUCT_ID"), null);
				
				X_XX_VMR_VendorProdRef ref_proveedor = 
					new X_XX_VMR_VendorProdRef(Env.getCtx(), prod.getXX_VMR_VendorProdRef_ID(), null);
				
				xProductTable.setValueAt( new KeyNamePair(ref_proveedor.get_ID(), ref_proveedor.getValue()) , row, 2);
				xProductTable.setValueAt( new KeyNamePair(prod.getM_Product_ID(), prod.getValue() + "-" + prod.getName())  , row, 1);
								
				if (rs.getInt("M_ATTRIBUTESETINSTANCE_ID") != 0) {
					MAttributeSetInstance attins = 
						new MAttributeSetInstance(Env.getCtx(),rs.getInt("M_ATTRIBUTESETINSTANCE_ID"), null);
					xProductTable.setValueAt( 
							new KeyNamePair(attins.get_ID(), attins.getDescription()) , row, 3);
				} else {
					xProductTable.setValueAt( new KeyNamePair(0, "") , row, 3);
				}								
				Integer priceConsecutive = rs.getInt("XX_PRICECONSECUTIVE");
				if( priceConsecutive <= 0 || priceConsecutive == null){
					result = false;
				}else {
					//Colocar el consecutivo de precio
					DecimalFormat formato = new DecimalFormat("000");
					xProductTable.setValueAt(formato.format(rs.getInt("XX_PRICECONSECUTIVE")), row, 4);
				}
				
				
				xProductTable.setValueAt(rs.getInt("PRODUCTQTY"), row, 5);
				xProductTable.setValueAt(rs.getInt("PRODUCTQTY"), row, 6);
				
				X_XX_VMR_Category cat = new X_XX_VMR_Category(Env.getCtx(),prod.getXX_VMR_Category_ID(), null);
				xProductTable.setValueAt( cat.getKeyNamePair() , row, 7);
				
				X_XX_VMR_Department dep = new X_XX_VMR_Department(Env.getCtx(), prod.getXX_VMR_Department_ID(), null);
				xProductTable.setValueAt( dep.getKeyNamePair() , row, 8);
				
				X_XX_VMR_Line lin = new X_XX_VMR_Line(Env.getCtx(), prod.getXX_VMR_Line_ID(), null);
				xProductTable.setValueAt( lin.getKeyNamePair() , row, 9);
				
				X_XX_VMR_Section sec = new X_XX_VMR_Section(Env.getCtx(), prod.getXX_VMR_Section_ID(), null);
				xProductTable.setValueAt( sec.getKeyNamePair() , row,10);
	
				i++;
			}				
		}
		catch(Exception E) {
			E.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}	
		return result;
	}

    public void check_all() {    	
    	boolean checkall = false; 
    	if (xProductTable.getRowCount() > 0) {    		
    		IDColumn id = (IDColumn) xProductTable.getValueAt(0, 0);
    		if (id.isSelected()) {
    			checkall = false;
    		} else {
    			checkall = true;
    		}
    	}
    	for (int row = 0; row < xProductTable.getRowCount() ; row++) {
    		IDColumn old_id = (IDColumn) xProductTable.getValueAt(row, 0);    		
    		IDColumn id = new IDColumn(old_id.getRecord_ID());
    		id.setSelected(checkall);
    		xProductTable.setValueAt(id ,row, 0);    		
    	}     
    }
    
    public void generateLabels() {
    	
    	//Se eliminó, ahora se hace en el proceso
    	//new Utilities().GenerarConsecutivo(header);
    	Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    	xProductTable.stopEditor(true);
    	m_frame.setCursor(hourglassCursor);
    	PrintService psZebra_glued = null;
    	PrintService psZebra_flat = null;
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		
		int id_label_glued = Env.getCtx().getContextAsInt("#XX_L_TYPELABELENGOMADA_ID");		
		int id_label_flat = Env.getCtx().getContextAsInt("#XX_L_TYPELABELCOLGANTE_ID");
       	
		int glued = 0, flats = 0;
    	for (int row = 0; row < xProductTable.getRowCount() ; row++) {
    		IDColumn idcol = (IDColumn)xProductTable.getValueAt(row, 0); 
    		if (idcol != null && idcol.isSelected()) {
    			KeyNamePair product_kp = (KeyNamePair)xProductTable.getValueAt(row, 1);
    			MProduct product = new MProduct(Env.getCtx(), product_kp.getKey(), null);
    			
    			if (product.getXX_VMR_TypeLabel_ID() == id_label_glued ){
    				glued++;
    			}  else if ( product.getXX_VMR_TypeLabel_ID() == id_label_flat ) {
    				flats++;    				
    			} else {
    				X_XX_VMR_TypeLabel label_type = 
    					new X_XX_VMR_TypeLabel(Env.getCtx(), product.getXX_VMR_TypeLabel_ID(), null);
    				
    				String mss = Msg.getMsg(Env.getCtx(), "XX_WrongLabelType", 
    						//Order					
    						new String[] {label_type.getValue(),label_type.getName()});    				  				
    				ADialog.error(m_WindowNo, m_frame, mss);    				
    				dispose();
    				return;
    			}
    			
    		}
    	}    	
    	psZebra_glued = services[printer_glued];
    	psZebra_flat = services[printer_flat];
		for (int row = 0; row < xProductTable.getRowCount() ; row++) {
    		IDColumn idcol = (IDColumn)xProductTable.getValueAt(row, 0); 
    		if (idcol != null && idcol.isSelected()) {
    			KeyNamePair product_kp = (KeyNamePair)xProductTable.getValueAt(row, 1);
    			MProduct product = new MProduct(Env.getCtx(), product_kp.getKey(), null);
    			
    			if (product.getXX_VMR_TypeLabel_ID() == id_label_glued ){
    				print_labels (psZebra_glued, row, true);
    			}  else {
    				print_labels (psZebra_flat, row, false);
    			} 
    		}
    	}		
		if (flats + glued > 0 ) {
			//Se actualiza la cabecera de distribución, 
			//indicando que ya se han impreso etiquetas consolidadas para esta distribución
			header.setXX_IsPrintedLabels(true);
			header.save();
			ADialog.info(m_WindowNo, m_frame, "XX_PrintedLabels");
    	}		    				
		dispose(); 		
    }
    
	/**
	 * 	Dispose
	 */
	public void dispose()	{		
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
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
		if (e.getSource() == generate)
			generateLabels();
		else if (e.getSource() == markall) {
			check_all();
		}
			
	}
	
	public void print_labels (PrintService psZebra, int row, boolean glued) {
		try {  			
			KeyNamePair knp_product = (KeyNamePair)xProductTable.getValueAt(row, 1);
			KeyNamePair knp_att = (KeyNamePair)xProductTable.getValueAt(row, 3);			
			int cantidadEtiquetas = ((Number)xProductTable.getValueAt(row, 6)).intValue();						
			String correlativo = (String) xProductTable.getValueAt(row, 4);
			Date fecha_usada = null;
			String week_created = null;
			//Buscar la semana de creación
			String sql = " SELECT MIN(XX_VMR_ORDER_ID)" +				
					" FROM XX_VMR_ORDER " + 
					" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = "+ header.get_ID();
			PreparedStatement pstmt =  null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if (rs.next()) {		
					X_XX_VMR_Order orderMin = new X_XX_VMR_Order(Env.getCtx(), rs.getInt(1), null);
					fecha_usada = new Date((orderMin.getCreated()).getTime());
					week_created = "" + orderMin.getXX_WeekCreated(); 
				} 
			}catch (Exception e) {
				
			}finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			Utilities.printDistributionLabels(psZebra, knp_product, knp_att, correlativo, header, cantidadEtiquetas, glued, fecha_usada, week_created);
		}
		catch (Exception e) {
			e.printStackTrace();  
		}		
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
        int column = e.getColumn();             
        TableModel model = (TableModel)e.getSource();  
                
        if ((row == -1) || (column == -1)) return;        
        Object obj = model.getValueAt(row, column);
    	try {     		
    		if (column == 6) {
	        	int data = ((Number)obj).intValue();
	        	int old_value =  ((Number)model.getValueAt(row, 5)).intValue();
	        	if (data <= 0) {      
	        		ADialog.error(m_WindowNo, m_frame, "XX_PostiveValuesOnly");
	        		model.setValueAt( old_value , row, column);        	
	        	}  else if (data > old_value) {
	        		ADialog.error(m_WindowNo, m_frame, "XX_PostiveLesserValuesOnly");
					model.setValueAt(old_value, e.getFirstRow(), 6);	        		
	        	} 
    		}
    	} catch (NumberFormatException exc) {}  
    	catch (NullPointerException nul) {} 
		
	}
}
