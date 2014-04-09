
package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_Category;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_Order;
import compiere.model.cds.X_XX_VMR_Package;
//import compiere.model.cds.X_XX_VMR_Season;
import compiere.model.cds.X_XX_VMR_Section;
import compiere.model.dynamic.X_XX_VMA_Season;



/**
 *  
 *  @author     Javier Pino
 *  @version    
 */
public class XX_ChangeProductLabelsForm extends CPanel
	implements FormPanel, ActionListener, TableModelListener {
	

	private X_XX_VMR_Order header = null;
	Vector<KeyNamePair> alltypelabels = new Vector<KeyNamePair>();
	
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

		Ctx aux = Env.getCtx();		
		int header_id = aux.getContextAsInt("#XX_VMR_CHANGE_PlacedOrder_ID");	
		
			//Remove the no longer necessary items on the context		
		aux.remove("#XX_VMR_CHANGE_PlacedOrder_ID");		

			//Creates the Header with the necessary information					
		header = new X_XX_VMR_Order(aux, header_id , null);
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
	static CLogger 			log = CLogger.getCLogger(XX_ChangeProductLabelsForm.class);

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
	
	private VComboBox comboSameLabel = new VComboBox();
	private JLabel labelSameDistr = new JLabel(Msg.translate(Env.getCtx(), "XX_ChangeSelectedLabels"));
	
	
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
		generate.setText(Msg.translate(Env.getCtx(), "XX_SaveExit"));
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
		
		//PANEL INFERIOR
		FlowLayout commandLayout = new FlowLayout();		
		commandLayout.setAlignment(FlowLayout.CENTER);
		commandLayout.setHgap(10);
		southPanel.setLayout(commandLayout);
		southPanel.add(labelSameDistr, null);
		southPanel.add(comboSameLabel, null);
		southPanel.add(markall,null);
		southPanel.add(generate, null);

		
		}   //  jbInit*/

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit() {	
		
		String type_label = "SELECT XX_VMR_TYPELABEL_ID, NAME FROM XX_VMR_TYPELABEL T ";
		type_label = MRole.getDefault().addAccessSQL(
				type_label, "T", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		PreparedStatement pstmt = DB.prepareStatement(type_label, null);			
		//Setting the query parameters
		ResultSet rs;
		try {
			rs = pstmt.executeQuery();
			comboSameLabel.addItem(new KeyNamePair(0, ""));
			while (rs.next()) {
				alltypelabels.add ( new KeyNamePair(rs.getInt(1), rs.getString(2)));
				comboSameLabel.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();			
		}			
		//Add the column definition for the table
		xProductTable.addColumn(" ");		
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Product"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "AttributeSetInstance"));		
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_ProductQty"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_LabelType_I"));
		xProductTable.addColumn(Msg.getMsg( Env.getCtx(), "XX_Category"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Department_I"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Line_I"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Section_I"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Season _I"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "XX_Collection"));
		xProductTable.addColumn(Msg.translate(Env.getCtx(), "Package"));
		xProductTable.setMultiSelection(true);
		
		xProductTable.setColumnClass(0, IDColumn.class, false); 		
		xProductTable.setColumnClass(1, KeyNamePair.class, true); 
		xProductTable.setColumnClass(2, KeyNamePair.class, true);		
		xProductTable.setColumnClass(3, Integer.class, true);
		xProductTable.setColumnClass(4, String.class, false);
		xProductTable.setColumnClass(5, KeyNamePair.class, true);
		xProductTable.setColumnClass(6, KeyNamePair.class, true);
		xProductTable.setColumnClass(7, KeyNamePair.class, true);
		xProductTable.setColumnClass(8, KeyNamePair.class, true);
		xProductTable.setColumnClass(9, KeyNamePair.class, true);
		xProductTable.setColumnClass(10, KeyNamePair.class, true);
		xProductTable.setColumnClass(11, KeyNamePair.class, true);
		
		CompiereColor.setBackground (this);
		xProductTable.setRowHeight(xProductTable.getRowHeight() + 2);
		/*xProductTable.getColumnModel().getColumn(0).setPreferredWidth(10);
		xProductTable.getColumnModel().getColumn(1).setPreferredWidth(130);*/
					
		xProductTable.getTableHeader().setReorderingAllowed(false);
		TableColumn col = xProductTable.getColumnModel().getColumn(4);				
		col.setCellEditor(new ComboBoxEditor(alltypelabels));
		col.setCellRenderer(new ComboBoxRenderer(alltypelabels));

		generate.addActionListener(this);
		markall.addActionListener(this);
		comboSameLabel.addActionListener(this);

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
		xProductTable.getModel().addTableModelListener(this);
		
		xProductTable.autoSize(false);
		xProductTable.repaint();
	}   //  dynInit
	
	
    private void fillProductTable(){    	   	
    					
    	int row = 0; 
		try	 {						
			String get_details = 
				" SELECT M_PRODUCT_ID, SUM(XX_PRODUCTQUANTITY), XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID," +
				" XX_VMR_LINE_ID, XX_VMR_SECTION_ID, XX_VMA_SEASON_ID, XX_VMR_COLLECTION_ID, XX_VMR_PACKAGE_ID" +
				" FROM XX_VMR_ORDERREQUESTDETAIL " +
				" WHERE XX_VMR_ORDER_ID = " + header.get_ID() + 
				" GROUP BY XX_VMR_CATEGORY_ID, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID," +
				" XX_VMR_SECTION_ID, XX_VMA_SEASON_ID, XX_VMR_COLLECTION_ID, XX_VMR_PACKAGE_ID, M_PRODUCT_ID " ;			
			PreparedStatement pstmt = DB.prepareStatement(get_details, null);			
				//Setting the query parameters
			ResultSet rs = pstmt.executeQuery();			
			while (rs.next()) {
				row = xProductTable.getRowCount();				
				xProductTable.setRowCount(row + 1);			
				
				IDColumn id = new IDColumn(rs.getInt(1));
				id.setSelected(false);
				xProductTable.setValueAt(id, row, 0);
				
				MProduct prod = new MProduct(Env.getCtx(),rs.getInt(1), null);
				xProductTable.setValueAt( prod.getKeyNamePair() , row, 1);								
				
				if (prod.getM_AttributeSetInstance_ID() != 0) {
					MAttributeSetInstance attins = 
						new MAttributeSetInstance(Env.getCtx(),prod.getM_AttributeSetInstance_ID(), null);
					xProductTable.setValueAt( 
							new KeyNamePair(attins.get_ID(), attins.getDescription()) , row, 2);
				} else {
					xProductTable.setValueAt( new KeyNamePair(0, "") , row, 2);
				}				
				xProductTable.setValueAt(rs.getInt(2), row, 3);
				
				for (int i = 0; i < alltypelabels.size() ; i++ ) {
					if (prod.getXX_VMR_TypeLabel_ID() == alltypelabels.elementAt(i).getKey()) {
						xProductTable.setValueAt(alltypelabels.elementAt(i), row, 4);
						break;
					}
				}
				X_XX_VMR_Category cat = new X_XX_VMR_Category(Env.getCtx(),rs.getInt(3), null);
				xProductTable.setValueAt( cat.getKeyNamePair() , row, 5);
				
				X_XX_VMR_Department dep = new X_XX_VMR_Department(Env.getCtx(), rs.getInt(4), null);
				xProductTable.setValueAt( dep.getKeyNamePair() , row, 6);
				
				X_XX_VMR_Line lin = new X_XX_VMR_Line(Env.getCtx(),rs.getInt(5), null);
				xProductTable.setValueAt( lin.getKeyNamePair() , row, 7);
				
				X_XX_VMR_Section sec = new X_XX_VMR_Section(Env.getCtx(), rs.getInt(6), null);
				xProductTable.setValueAt( sec.getKeyNamePair() , row, 8);
				
				X_XX_VMA_Season sea = new X_XX_VMA_Season(Env.getCtx(), rs.getInt(7), null);
				xProductTable.setValueAt( new KeyNamePair(sea.get_ID(), sea.getName())  , row, 9);
				
				X_XX_VMR_Collection col = new X_XX_VMR_Collection(Env.getCtx(), rs.getInt(8) , null);
				xProductTable.setValueAt(col.getKeyNamePair(), row, 10);
				
				X_XX_VMR_Package pack = new X_XX_VMR_Package(Env.getCtx(), rs.getInt(9), null);
				xProductTable.setValueAt( new KeyNamePair(pack.get_ID(), pack.getName()) , row, 11);
			}
			rs.close();
			pstmt.close();				
		}
		catch(Exception E) {
			E.printStackTrace();
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
    
    public void setAllDistrib(){		
		
		KeyNamePair distribution = (KeyNamePair)comboSameLabel.getSelectedItem();
		
		//Si se coloca en blanco el combo, entonces no ocurre nada
		if (distribution.getKey() == 0)
			return ;
		int rows = xProductTable.getRowCount();
		if (rows > 0) {
			for (int i = 0 ; i < rows ;  i++) {					
				IDColumn id = (IDColumn)xProductTable.getModel().getValueAt(i, 0);
				if (id.isSelected()) {
					xProductTable.setValueAt(distribution, i, 4);					
				}
			}				
		}
		xProductTable.repaint();
	}
	
    public void generateDistribution() {       	
    	xProductTable.stopEditor(true);
    	Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    	m_frame.setCursor(hourglassCursor);
    	Trx trans =Trx.get("ChangeProductLabel"); 
    	int changed_rows = 0;
    	int wrong_ones = 0;
    	for (int row = 0; row < xProductTable.getRowCount() ; row++) {
    		IDColumn idcol = (IDColumn)xProductTable.getValueAt(row, 0); 
    		if (idcol != null && idcol.isSelected()) {
    			KeyNamePair product_kp = (KeyNamePair)xProductTable.getValueAt(row, 1);    			
    			KeyNamePair type_label = (KeyNamePair)xProductTable.getValueAt(row, 4);      			
    			if (type_label != null ) {
	    			MProduct product = new MProduct(Env.getCtx(), product_kp.getKey(), null);
	    			product.setXX_VMR_TypeLabel_ID(type_label.getKey());    
	    			
	    			//Para indicar que viene de esta ventana para que producto no valide
	    			Env.getCtx().setContext("#XX_L_COMESFROMPLACEDORDER","Y");
	    			
	    			if (product.save(trans)) 
	    				changed_rows++;
	    			else wrong_ones++;		
    			}
    		}
    	}
    	if (wrong_ones > 0) {    		
    		trans.rollback();
    		ADialog.error(m_WindowNo, m_frame, "SaveError");
    		dispose();
    	} else {
    		if ( changed_rows > 0 && trans.commit() ) { 
        		ADialog.info(m_WindowNo, m_frame, "XX_ChangedLabel");    		
        	} 
    		dispose();
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
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
		if (e.getSource() == generate)
			generateDistribution();
		else if (e.getSource() == markall) {
			check_all();
		} else if(e.getSource() == comboSameLabel) {
			setAllDistrib();
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
    		if (column == 4) {
	        	if (obj != null) {
	        		KeyNamePair type = (KeyNamePair)obj;
	        	}
    		} 
    	}  catch (NullPointerException nul) {
    		
    	} 
		
	}
	private class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer(Vector<KeyNamePair> items) {
            super(items);
        }
    
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
    
            // Select the current value
            setSelectedItem(value);
            return this;
        }
    }

	
	private class ComboBoxEditor extends DefaultCellEditor {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ComboBoxEditor(Vector<KeyNamePair> items) {
            super(new JComboBox(items));
        }
    }


}
