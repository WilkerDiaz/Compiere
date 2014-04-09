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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
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
import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_AttributeSet;
import org.compiere.model.X_M_AttributeSetInstance;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDiscountAppliDetail;
import compiere.model.cds.MVMRDiscountRequest;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Category;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_LongCharacteristic;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;
import compiere.model.cds.X_XX_VMR_Section;
import compiere.model.cds.X_XX_VMR_TypeLabel;
import compiere.model.cds.X_XX_VMR_VendorProdRef;

/**
 *  Imprime Etiquetas de un producto dado un consecutivo de precio, usando el formato de Tienda
 *  @author    Gabrielle Huchet
 *  @version    
 */
public class XX_PrintProductPriceConseLabels extends CPanel
implements FormPanel, ActionListener, TableModelListener {

	private static final long serialVersionUID = 1L;

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_PrintProductLabelsForm.class);

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
	
	private X_XX_VMR_PriceConsecutive priceConsecutive = null;	
	private int productQuantity = 0, printer_glued = 0, printer_flat = 0, warehouse_id = 0;
	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame) {
		
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		Ctx aux = Env.getCtx();		
		int priceConsecutive_id = aux.getContextAsInt("#XX_VMR_PRINTLABEL_PriceConsecutive_ID");	
		productQuantity = aux.getContextAsInt("#XX_VMR_PRINTLABEL_ProductQuantity");
		warehouse_id = aux.getContextAsInt("#XX_VMR_PRINTLABEL_Warehouse_ID");
		printer_flat = aux.getContextAsInt("#XX_VMR_PRINTLABEL_Hanging");
		printer_glued = aux.getContextAsInt("#XX_VMR_PRINTLABEL_Glued");
		
			//Remove the no longer necessary items on the context		
		aux.remove("#XX_VMR_PRINTLABEL_PriceConsecutive_ID");
		aux.remove("#XX_VMR_PRINTLABEL_ProductQuantity");
		aux.remove("#XX_VMR_PRINTLABEL_Warehouse_ID");
		aux.remove("#XX_VMR_PRINTLABEL_Hanging");
		aux.remove("#XX_VMR_PRINTLABEL_Glued");

			//Creates the Header with the necessary information					
		priceConsecutive = new X_XX_VMR_PriceConsecutive(aux, priceConsecutive_id , null);
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
	private void jbInit() throws Exception {
		
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
			
		southPanel.setLayout(southLayout);
		generate.setText(Msg.translate(Env.getCtx(), "XX_PrintSelectedLabels"));
		generate.setEnabled(true);
		
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
		xProductTable.setColumnClass(6, Integer.class, true);
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
		fillProductTable();
		
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
		xProductTable.repaint();
	}   //  dynInit
	
	 private void fillProductTable(){    	   	
			
	    	int row = 0; 
			try		
			{						
				row = xProductTable.getRowCount();				
				xProductTable.setRowCount(row + 1);
				IDColumn id = new IDColumn(priceConsecutive.get_ID());
				id.setSelected(true);				
				xProductTable.setValueAt(id, row, 0);
					
				MProduct prod = new MProduct(Env.getCtx(),priceConsecutive.getM_Product_ID(), null);

				xProductTable.setValueAt( new KeyNamePair(prod.getM_Product_ID(), prod.getValue())  , row, 1);
				xProductTable.setValueAt( prod.getKeyNamePair() , row, 3);
									
				
				X_XX_VMR_VendorProdRef ref_proveedor = 
					new X_XX_VMR_VendorProdRef(Env.getCtx(), prod.getXX_VMR_VendorProdRef_ID(), null);	
				xProductTable.setValueAt( new KeyNamePair(ref_proveedor.get_ID(), ref_proveedor.getValue()) , row, 2);
				
				//Colocar el consecutivo de precio
				DecimalFormat formato = new DecimalFormat("000");
				xProductTable.setValueAt(formato.format(priceConsecutive.getXX_PriceConsecutive()), row, 4);
					
				xProductTable.setValueAt(productQuantity, row, 5);
				xProductTable.setValueAt(productQuantity, row, 6);
					
				X_XX_VMR_Category cat = new X_XX_VMR_Category(Env.getCtx(),prod.getXX_VMR_Category_ID(), null);
				xProductTable.setValueAt( cat.getKeyNamePair() , row, 7);
					
				X_XX_VMR_Department dep = new X_XX_VMR_Department(Env.getCtx(), prod.getXX_VMR_Department_ID(), null);
				xProductTable.setValueAt( dep.getKeyNamePair() , row, 8);
					
				X_XX_VMR_Line lin = new X_XX_VMR_Line(Env.getCtx(), prod.getXX_VMR_Line_ID(), null);
				xProductTable.setValueAt( lin.getKeyNamePair() , row, 9);
					
				X_XX_VMR_Section sec = new X_XX_VMR_Section(Env.getCtx(), prod.getXX_VMR_Section_ID(), null);
				xProductTable.setValueAt( sec.getKeyNamePair() , row,10);
//				
//				if (priceConsecutive.getM_AttributeSetInstance_ID() != 0) {
//					MAttributeSetInstance attins = 
//						new MAttributeSetInstance(Env.getCtx(),priceConsecutive.getM_AttributeSetInstance_ID(), null);
//					xProductTable.setValueAt( 
//							new KeyNamePair(attins.get_ID(), attins.getDescription()) , row, 11);
//				} else {
//					xProductTable.setValueAt( new KeyNamePair(0, "") , row, 11);
//				}	
							
			}
			catch(Exception e) {
				e.printStackTrace();
			}		
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
	    			KeyNamePair product_kp = (KeyNamePair)xProductTable.getValueAt(row, 3);
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
	    			KeyNamePair product_kp = (KeyNamePair)xProductTable.getValueAt(row, 3);
	    			MProduct product = new MProduct(Env.getCtx(), product_kp.getKey(), null);
	    			
	    			if (product.getXX_VMR_TypeLabel_ID() == id_label_glued ){
	    				print_labels (psZebra_glued, row, true);
	    			}  else {
	    				print_labels (psZebra_flat, row, false);
	    			} 
	    		}
	    	}		
			if (flats + glued > 0 ) {
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

				int cantidadEtiquetas = ((Number)xProductTable.getValueAt(row, 6)).intValue();
					
				MProduct producto = new MProduct(Env.getCtx(), knp_product.getKey(), null);				
				String name = producto.getName();
				/*
				 * Caracteristica larga
				 * */
				X_XX_VMR_LongCharacteristic caracLarga = new X_XX_VMR_LongCharacteristic(Env.getCtx(), producto.getXX_VMR_LongCharacteristic_ID(), null);
				String descripcion = null;
				if(producto.getM_AttributeSetInstance_ID()!=0){
					X_M_AttributeSetInstance attrSet = new X_M_AttributeSetInstance(Env.getCtx(), producto.getM_AttributeSetInstance_ID(), null);
					descripcion = attrSet.getDescription();
				}
				//DecimalFormat formato = new DecimalFormat(".##");
				DecimalFormat formato = new DecimalFormat("###,###,###.00");
				X_XX_VMR_Department dep = new X_XX_VMR_Department(Env.getCtx(), producto.getXX_VMR_Department_ID() , null);			
				String departmentCode = dep.getValue();
						
				X_XX_VMR_Line lin = new X_XX_VMR_Line(Env.getCtx(), producto.getXX_VMR_Line_ID(), null);
				String lineCode = lin.getValue();
				
				X_XX_VMR_Section sec = new X_XX_VMR_Section(Env.getCtx(), producto.getXX_VMR_Section_ID(), null);
				String seccionCode = sec.getValue();  
				
				String precio = formato.format(priceConsecutive.getXX_SalePrice());

				MWarehouse tienda = new MWarehouse(Env.getCtx(), warehouse_id, null);
				
				X_XX_VMR_PriceConsecutive consecutivo = new X_XX_VMR_PriceConsecutive(Env.getCtx(), priceConsecutive.getXX_VMR_PriceConsecutive_ID(), null);
				//Debo buscar semana, mes y año de la fecha de creacion del consecutivo
			
				Date date = (Date)consecutivo.getCreated();
				Calendar cal = new GregorianCalendar();  
				cal.setTime(date);			
				int mes = cal.get(Calendar.MONTH)+1;
				int año = cal.get(Calendar.YEAR);
				int semana = cal.get(Calendar.WEEK_OF_YEAR);

				DateFormat fechaformato = new SimpleDateFormat("MM yyyy");
	
				
				//Calcular el impuesto		
				Integer TaxCategory_ID = producto.getC_TaxCategory_ID();		
				String sql = "SELECT rate"
							+ " FROM C_Tax"
							+ " WHERE ValidFrom="			
							+ " (SELECT MAX(ValidFrom)"											
							+ " FROM C_Tax"
							+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+" AND ValidFrom <= "+DB.TO_DATE((Timestamp)date,true)+")";	
				PreparedStatement prst = DB.prepareStatement(sql,null);
				ResultSet rs = null;
				BigDecimal impuesto = new BigDecimal(1);
				try {
					rs = prst.executeQuery();
					if (rs.next()){
						impuesto = rs.getBigDecimal("rate").divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
						System.out.println("impuesto "+impuesto);
					}			
				}catch (Exception e){
					System.out.println("error al calcular el impuesto");
				}finally {
					rs.close();
					prst.close();
				}
				BigDecimal taxAux = getTaxForLabel((Timestamp)date,TaxCategory_ID);
				
				String product_plus_correlative = "" + producto.getValue() + consecutivoTostring(consecutivo.getXX_PriceConsecutive());
				
				boolean exento = false;
				if (producto.getC_TaxCategory_ID() == Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID")) {
					exento = true;				
				}
				String s = "";
				DocPrintJob job = psZebra.createPrintJob();
				
				precio = formato.format((priceConsecutive.getXX_SalePrice().add((priceConsecutive.getXX_SalePrice()).multiply(impuesto))).setScale(2, BigDecimal.ROUND_HALF_UP));
				while (product_plus_correlative.length() < 12) {
					product_plus_correlative = "0" + product_plus_correlative;
				}
//					s="^XA^PRD^XZ\n"+                                                                     
//					"^XA^JMA^\n"+                                                                       
//					"^LH07,02^FS\n"+                                                                    
//					"^FO10,10^BE,25,N^BY3, 0.5,45^FD"+ product_plus_correlative +"^FS\n"+                                 
//					 "^FO10,72^A0,15,14^FD"+departmentCode+"-"+lineCode+"-"+seccionCode+"-"+ product_plus_correlative +"       ^FS\n"+                          
//					 "^FO10,87^A0,15,14^FD"+(name.length() > 30 ? name.substring(0,29) : name)+"^FS\n"; 
//					 if(descripcion!=null ){
//						s=s+"^FO10,102^A0,15,14^FD"+ (descripcion.length() > 30 ? descripcion.substring(0,29) : descripcion) + "^FS\n";
//					 }else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
//						s=s+"^FO10,102^A0,15,14^FD"+ (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS\n";
//					 }
//
//					 s=s+"^FO10,120^AB,13,07^FDPRECIO BS^FS\n"+                                             
//					 "^FO10,173^AB,11,07^FDRIF J-00046517-7 "+  (exento ? "" : "Incluye IVA "+ taxAux +"%")+"^FS\n"+                   
//					 "^FO10,158^AB,11,07^CI10^FD"+semana+" "+fechaformato.format(date)+"^FS\n"+                                                                                   
//					 "^FO200,105^AO,25,10^CI10^FD"+precio+"^FS\n"+  
//					 "^FO200,153^AD,26,16^FDBECO^FS" +
//					 "^PQ"+cantidadEtiquetas+"^FS\n"+                                                                        
//					 "^XZ\n"+       
//					 //Control label                                                                    
//					 "^XA^PRD^XZ\N"+                                                                     
//					 "^XA^JMA^\N"+                                                                       
//					 "^LH00,15^FS\N"+                                                                    
//					 "^FO07,20^AD,50,10^FD*CONTROL*     "+SEMANA+" "+ FECHAFORMATO.FORMAT(DATE)+"^FS\N"+            
//					 "^FO28,70^A0,15,14^FDTDA:  "+TIENDA.GETVALUE()+"^FS\N"+                      
//					 "^FO28,85^A0,15,14^FDCANT:     "+CANTIDADETIQUETAS+"      PRECIO      "+PRECIO+"^FS\N"+               
//					 "^FO28,100^A0,15,14^FD" + DEPARTMENTCODE+"-"+LINECODE+"-"+SECCIONCODE+"- "+ PRODUCT_PLUS_CORRELATIVE +"^FS\N"+                                   
//					 "^FO28,118^A0,15,14^FD"+(NAME.LENGTH() > 50 ? NAME.SUBSTRING(0,49) : NAME)+"^FS\N"+                            
//					 "^FO05,140^A0,15,10^FD                                                  ^FS\N"+    
//					 "^PQ1^FS\N"+                                                                        
//					 "^XZ"; 
					 

					s="^XA^PRD^XZ\n"+                                                                     
					"^XA^JMA^\n"+                                                                       
					"^LH07,02^FS\n"+                                                                    
					"^FO10,03^BE,25,N^BY3, 0.5,45^FD"+ product_plus_correlative +"^FS\n"+                                 
					 "^FO10,62^AA,15,12^FD"+departmentCode+"-"+lineCode+"-"+seccionCode+"-"+ product_plus_correlative +"       ^FS\n"+                          
					 "^FO10,82^AA,20,10^FD"+(name.length() > 30 ? name.substring(0,29) : name)+"^FS\n"; 
					 if(descripcion!=null ){
						s=s+"^FO10,97^AA,20,10^FD"+ (descripcion.length() > 30 ? descripcion.substring(0,29) : descripcion) + "^FS\n";
					 }else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
						s=s+"^FO10,97^AA,20,10^FD"+ (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS\n";
					 }

					 s=s+"^FO10,116^AA,14,10^FDPRECIO BS^FS\n"+                                             
					 "^FO10,170^AB,11,07^FDRIF J-00046517-7 "+  (exento ? "" : "Incluye IVA "+ taxAux +"%")+"^FS\n"+                   
					 "^FO10,153^AB,11,07^CI10^FD"+semana+" "+fechaformato.format(date)+"^FS\n"+                                                                                   
					 "^FO167,153^AD,26,26^FDBECO^FS" +
					 "^FO55,115^AO,22,15^CI10^FD       "+precio+"^FS\n"+  
					 "^PQ"+cantidadEtiquetas+"^FS\n"+                                                                        
					 "^XZ\n"+       
					 //Control label 
					 "^XA^PRD^XZ\n"+                                                                     
					 "^XA^JMA^\n"+                                                                       
					 "^LH00,15^FS\n"+                                                                    
					 "^FO07,20^AD,50,10^FD*CONTROL*     "+semana+" "+ fechaformato.format(date)+"^FS\n"+            
					 "^FO28,70^A0,15,14^FDTDA:  "+tienda.getValue()+"^FS\n"+                      
					 "^FO28,85^A0,15,14^FDCANT:     "+cantidadEtiquetas+"      PRECIO      "+precio+"^FS\n"+               
					 "^FO28,100^A0,15,14^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative +"^FS\n"+                                   
					 "^FO28,118^A0,15,14^FD"+(name.length() > 50 ? name.substring(0,49) : name)+"^FS\n"+                            
					 "^FO05,140^A0,15,10^FD                                                  ^FS\n"+    
					 "^PQ1^FS\n"+                                                                        
					 "^XZ"; 
//					 "^XA^PRD^XZ\n"+                                                                     
//					 "^XA^JMA^\n"+                                                                       
//					 "^LH00,15^FS\n"+                                                                    
//					 "^FO2,5^AD,38,10^FD*CONTROL*     "+semana+" "+ fechaformato.format(date)+"^FS\n"+            
//					 "^FO05,45^A0,30,07^FD                  TDA:  "+tienda.getValue()+"^FS\n"+                      
//					 "^FO28,48^A0,15,14^FDCANT:     "+cantidadEtiquetas+"      PRECIO     "+precio+"^FS\n"+               
//					 "^FO05,95^A0,18,10^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative +"^FS\n"+                                   
//					 "^FO05,120^A0,18,10^FD"+(name.length() > 50 ? name.substring(0,49) : name)+"^FS\n"+                            
//					 "^FO05,140^A0,18,10^FD                                                  ^FS\n"+    
//					 "^PQ1^FS\n"+                                                                        
//					 "^XZ"; 
					  

				byte[] by = s.getBytes();   
				DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; 
				Doc doc = new SimpleDoc(by, flavor, null);   			   
				job.print(doc, null);   
		  
			}
			catch (Exception e)
			{   
				e.printStackTrace();  
			}
			
		}
		private static BigDecimal getTaxForLabel(Timestamp date, int C_TaxCategory_ID){
			
			BigDecimal tax = BigDecimal.ZERO;
			
			Integer TaxCategory_ID = C_TaxCategory_ID;		
			String sql = "SELECT rate"
						+ " FROM C_Tax"
						+ " WHERE ValidFrom="			
						+ " (SELECT MAX(ValidFrom)"											
						+ " FROM C_Tax"
						+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+ " "
						+ "AND ValidFrom <= TO_DATE('"+date.toString().substring(0,10)+"','YYYY-MM-DD'))";	
			
			PreparedStatement prst = DB.prepareStatement(sql,null);
			try {
				ResultSet rs = prst.executeQuery();
				if (rs.next()){
					tax = rs.getBigDecimal("rate");
				}
				rs.close();
				prst.close();
			} catch (SQLException e){
				System.out.println(e.getMessage());
			}
			
			return tax;
		}
		
		public String consecutivoTostring(int consecutivo)
		{
			String con = "";
			if (consecutivo<10)
				con = "00"+consecutivo;
			else if (consecutivo<100)
				con = "0"+consecutivo;
			else
				con = ""+consecutivo;
			return con;
		}
		
//		public void print_labels (PrintService psZebra, int row, boolean glued) {
//			try {  			
//				KeyNamePair knp_product = (KeyNamePair)xProductTable.getValueAt(row, 1);
//				KeyNamePair knp_att = (KeyNamePair)xProductTable.getValueAt(row, 3);	
//				
//				int cantidadEtiquetas = ((Number)xProductTable.getValueAt(row, 6)).intValue();						
//				String correlativo = (String) xProductTable.getValueAt(row, 4);
//				Utilities.print_labels(psZebra, knp_product, knp_att, correlativo, null, cantidadEtiquetas,warehouse_id, glued);
//			}
//			catch (Exception e) {
//				e.printStackTrace();  
//			}		
//		}
		
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
		        		ADialog.error(m_WindowNo, m_frame, "XX_PostiveLesserValuesOnly");
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
