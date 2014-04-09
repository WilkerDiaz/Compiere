package compiere.model.cds.forms;


import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.Map.Entry;
import java.util.logging.*;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VMR_Brand;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DistribDetailTemp;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.X_XX_VMR_HeaderAssociation;
import compiere.model.cds.X_XX_VMR_Package;

import compiere.model.cds.processes.XX_BatchNumberInfo;
import compiere.model.cds.processes.XX_CreateDistributionHeader;


public class VMR_InventoryDistribForm extends CPanel 
	implements FormPanel, ActionListener,  ListSelectionListener, TableModelListener {
	
/** @todo withholding */
	
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
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			lookupProduct = VLookup.createProduct(m_WindowNo);
			
			jbInit();
			dynInit();
			
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	private Ctx ctx = Env.getCtx();
	private boolean is_purchaseorder = false;
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** Format                  */
	private DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayTypeConstants.Amount);
	/** SQL for Query           */
	private StringBuilder          m_sql;
	/** Number of selected rows */
	private int             m_noSelected = 0;
	/** Client ID               */

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VMR_InventoryDistribForm.class);
	/** Distribution mark*/

		
	//PANEL
	private CPanel mainPanel = new CPanel();
		
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private MiniTablePreparator miniTable = new MiniTablePreparator();
	
	
	private CPanel commandPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JScrollPane dataPane = new JScrollPane();
	private FlowLayout commandLayout = new FlowLayout();
	
	//DETALLE
	private BorderLayout dataLayout = new BorderLayout();
	private CPanel dataPaneGeneral = new CPanel();
	private JScrollPane dataPaneDetail = new JScrollPane();
	private MiniTablePreparator miniTableDetail = new MiniTablePreparator();
	private TitledBorder borderDetail = new TitledBorder("Detalle Tiendas");
	

	
	//CATEGORY
	private VCheckBox checkCategory = new VCheckBox();
	private CLabel labelCategory = new CLabel();
	private VComboBox comboCategory = new VComboBox();
	
	//DEPARTMENT
	private VCheckBox checkDepartment = new VCheckBox();
	private CLabel labelDepartment = new CLabel();
	private VComboBox comboDepartment = new VComboBox();
	//LINE
	private VCheckBox checkLine = new VCheckBox();
	private CLabel labelLine = new CLabel();
	private VComboBox comboLine = new VComboBox();
	//SECTION
	private VCheckBox checkSection = new VCheckBox();
	private CLabel labelSection = new CLabel();
	private VComboBox comboSection = new VComboBox();
	//BRAND
	private VCheckBox checkBrand = new VCheckBox();
	private CLabel labelBrand = new CLabel();
	private VComboBox comboBrand = new VComboBox();
	//BUSINESS PARTNER
	private VCheckBox checkBPartner = new VCheckBox();
	private CLabel labelBPartner = new CLabel();
	private VComboBox comboBPartner = new VComboBox();
	//PRODUCT
	private VCheckBox checkProduct = new VCheckBox();
	private CLabel labelProduct = new CLabel();
	private VLookup lookupProduct = null;
	
	//Collection
	private VCheckBox checkCollection = new VCheckBox();
	private CLabel labelCollection = new CLabel();
	private VComboBox comboCollection = new VComboBox();
	
	//Package
	private VCheckBox checkPackage = new VCheckBox();
	private CLabel labelPackage = new CLabel();
	private VComboBox comboPackage = new VComboBox();
		
	//PURCHASE ORDER
	private VCheckBox checkPOrder = new VCheckBox();
	private CLabel labelPOrder = new CLabel();
	
	//REFERENCE
	private VCheckBox checkReference = new VCheckBox();
	private CLabel labelReference = new CLabel();

	/**Agregado Proyecto CD Valencia **/
	//LOCATOR     
	private CLabel labelLocator = new CLabel();
	private VComboBox comboLocator = new VComboBox();
	private int locatorCD = 0;
	private int warehouseCD = 0;
	private int orgCD = 0;
	
	/**Agregado Requerimiento Falla de Inventario*/
	//INVENTORY FAILS
	private VCheckBox checkInventoryFails = new VCheckBox();
	private CLabel labelInventoryFails = new CLabel();
	
	
	//QUANTITIES
	private CLabel labelStockQtyName = new CLabel();
	private CLabel labelStockQty = new CLabel();
	private CLabel labelDesiredQtyName = new CLabel();
	private CLabel labelDesiredQty = new CLabel();
	//BUTTONS
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bGenerate = ConfirmPanel.createProcessButton(true);	
	private CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	
	//private JButton bSameDistrib = new JButton();
	//OTHERS
	private VComboBox comboSameDistr = new VComboBox();
	private JLabel labelSameDistr = new JLabel();
	private JLabel dataStatus = new JLabel();
	private int indx = 0;

	private Vector<KeyNamePair> allDistrib = new Vector<KeyNamePair>();
	
	private Vector<Vector<KeyNamePair>> columnsInfo = new Vector<Vector<KeyNamePair>>();
	private Vector<Integer> columnsIDs = new Vector<Integer>();
	
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		bSearch.setPreferredSize(new Dimension(100, 25));
		m_frame.getRootPane().setDefaultButton(bSearch);
		bReset.setPreferredSize(new Dimension(100, 25));
		
		CompiereColor.setBackground(this);		
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		
		//
		labelCategory.setText(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"));
		labelDepartment.setText(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"));
		labelLine.setText(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
		labelSection.setText(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"));
		labelBrand.setText(Msg.translate(Env.getCtx(), "XX_VMR_Brand"));
		labelBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		labelProduct.setText(Msg.translate(Env.getCtx(), "M_PRODUCT_ID"));
		labelPOrder.setText(Msg.translate(Env.getCtx(), "XX_PredistributedPO"));
		labelReference.setText(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"));
		labelCollection.setText(Msg.translate(Env.getCtx(), "XX_Collection"));
	    labelPackage.setText(Msg.translate(Env.getCtx(), "Package"));	
		labelStockQtyName.setText(Msg.getMsg(Env.getCtx(), "XX_InventoryQty") + ":");
		labelStockQty.setText("0");
		labelDesiredQtyName.setText(Msg.translate(Env.getCtx(), "XX_DesiredQty") + ":");
		labelDesiredQty.setText("0");
		labelSameDistr.setText(Msg.translate(Env.getCtx(), "XX_DistributeAllBy"));
		labelLocator.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));	
		labelInventoryFails.setText("Mostrar Falla de Inventario");
		
		bReset.addActionListener(this);
		bGenerate.addActionListener(this);
		bCancel.addActionListener(this);
		bSearch.addActionListener(this);
		//bSameDistrib.addActionListener(this);

		//PANEL CREATION
		mainPanel.add(parameterPanel, BorderLayout.NORTH);		

		//PANEL IZQUIERDO
		parameterPanel.add(labelCategory,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(comboCategory,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkCategory,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(labelDepartment,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(comboDepartment,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkDepartment,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));	

		parameterPanel.add(labelLine,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(comboLine,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkLine,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//--	
		parameterPanel.add(labelSection,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		parameterPanel.add(comboSection,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkSection,  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
				
		parameterPanel.add(labelCollection,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(comboCollection,  new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkCollection,  new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));				
		
		parameterPanel.add(labelStockQtyName,  new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					
		parameterPanel.add(labelStockQty,  new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		//PANEL CENTRAL							
		parameterPanel.add(labelBrand,  new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		parameterPanel.add(comboBrand,   new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkBrand,  new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//--
		parameterPanel.add(labelBPartner,  new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(comboBPartner,  new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkBPartner,  new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//--		
		parameterPanel.add(labelProduct,  new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(lookupProduct,  new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkProduct,  new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(labelProduct,  new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		parameterPanel.add(lookupProduct,  new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			
		parameterPanel.add(checkProduct,  new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(labelPackage,  new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(comboPackage,  new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkPackage,  new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));	
		
		parameterPanel.add(labelLocator,  new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(comboLocator,  new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelDesiredQtyName,  new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(labelDesiredQty,  new GridBagConstraints(6, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		//PANEL DERECHO
		parameterPanel.add(labelPOrder,  new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkPOrder,  new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(labelReference,  new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkReference,  new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(labelInventoryFails,  new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkInventoryFails,  new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					
		parameterPanel.add(bReset,    new GridBagConstraints(7, 3, 2, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(bSearch,    new GridBagConstraints(7, 4, 2, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
	//	miniTable.setSelectionForeground(Color.BLUE);
		miniTableDetail.setRowHeight(miniTableDetail.getRowHeight() + 2);

		ColumnInfo[] layoutDetailSearch = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "M_Warehouse_ID"), ".",String.class, "."),
				new ColumnInfo("Venta Estimada",".",Integer.class, "."),
				new ColumnInfo("Reposición", ".",	Integer.class, "." ),	
				new ColumnInfo("Inventario", ".",Integer.class, "."),
				new ColumnInfo("Venta Real", ".",	Integer.class, "." ),	
				new ColumnInfo("Falla", ".",Integer.class, "."),
				new ColumnInfo("Falla Producto", ".",Integer.class, "."),
				};
		miniTableDetail.prepareTable(layoutDetailSearch, "", "", false, "");
	
		mainPanel.add(dataPaneGeneral, BorderLayout.CENTER);
		mainPanel.add(dataStatus, BorderLayout.SOUTH);

		dataPaneGeneral.setLayout(dataLayout);
		dataPaneGeneral.add(dataPane, BorderLayout.NORTH);
		
		dataPaneGeneral.add(dataPaneDetail, BorderLayout.CENTER);
		dataPaneDetail.getViewport().add(miniTableDetail,null);
		dataPane.getViewport().add(miniTable, null);
		borderDetail.setTitle(Msg.getMsg(Env.getCtx(), "Detalle Tiendas"));
		dataPaneDetail.setBorder(borderDetail);
		dataPaneDetail.repaint();
		miniTableDetail.setSortEnabled(true);
		dataPaneGeneral.setPreferredSize(new Dimension(1280, 600));
		dataPane.setPreferredSize(new Dimension(1240, 350));
		
		
		miniTable.getTableHeader().setFocusable(true);
		miniTable.getSelectionModel().addListSelectionListener(this);  

		
		//PANEL INFERIOR
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
	
		commandPanel.add(labelSameDistr, null);
		commandPanel.add(comboSameDistr, null);
		commandPanel.add(bCancel, null);
		commandPanel.add(bGenerate, null);
	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  - Load Department Info
	 *  - Load BPartner Info
	 *  - Load Brand Info
	 *  - Init Table
	 */
	private void dynInit()
	{
		
		loadBasicInfo();
		
	}   //  dynInit

	
	
	/**
	 *  Table initial state 
	 */
	private void loadBasicInfo() {

		is_purchaseorder = false;
		
		comboLocator.removeActionListener(this);
		comboCategory.removeActionListener(this);
		checkCategory.removeActionListener(this);
		comboCollection.removeActionListener(this);
		comboDepartment.removeActionListener(this);
		checkDepartment.removeActionListener(this);
		comboLine.removeActionListener(this);
		checkLine.removeActionListener(this);
		comboSection.removeActionListener(this);
		checkSection.removeActionListener(this);
		comboBrand.removeActionListener(this);
		checkBrand.removeActionListener(this);
		comboBPartner.removeActionListener(this);
		checkBPartner.removeActionListener(this);
		lookupProduct.removeActionListener(this);
		checkProduct.removeActionListener(this);
		comboSameDistr.removeActionListener(this);
		checkPOrder.removeActionListener(this);

		//Restore ComboBoxes and CheckBoxes		
		
		comboCategory.setEnabled(true);
		comboDepartment.setEnabled(true);
		comboLine.setEnabled(false);
		comboSection.setEnabled(false);
		comboBrand.setEnabled(true);
		comboBPartner.setEnabled(true);
		comboSameDistr.setEnabled(false);
		comboSameDistr.setEditable(false);
		comboCollection.setEnabled(true);
		comboCollection.setEditable(true);
		comboPackage.setEnabled(false);
		comboPackage.setEditable(false);
		
		comboLocator.setEnabled(true);
		comboLocator.setEditable(true);
		comboLocator.setMandatory(true);
		bSearch.setEnabled(false);
		
		if (comboSameDistr.getItemCount() > 0) {
			comboSameDistr.setSelectedIndex(0);
		}
		
		bGenerate.setEnabled(false);
		
		comboCategory.removeAllItems();
		comboDepartment.removeAllItems();
		comboLine.removeAllItems();
		comboSection.removeAllItems();
		comboBrand.removeAllItems();
		comboBPartner.removeAllItems();
		
		lookupProduct.setValue(null);
		lookupProduct.setEnabled(true);
		checkCategory.setValue(false);
		checkDepartment.setEnabled(true);
		checkDepartment.setValue(false);
		checkLine.setValue(false);
		checkLine.setEnabled(false);
		checkSection.setValue(false);
		checkSection.setEnabled(false);
		checkBrand.setEnabled(true);
		checkBPartner.setEnabled(true);
		checkProduct.setEnabled(true);
		checkBrand.setValue(false);
		checkBPartner.setValue(false);
		checkProduct.setValue(false);
		checkPOrder.setValue(false);
		checkCollection.setValue(false);
		checkPackage.setEnabled(false);
		checkReference.setEnabled(true);
		checkInventoryFails.setEnabled(true);
		
		
		//Setting Default Items
		KeyNamePair loadKNP = new KeyNamePair(0, "");
		
		comboCategory.addItem(loadKNP);
		comboDepartment.addItem(loadKNP);
		comboLine.addItem(loadKNP);
		comboSection.addItem(loadKNP);
		comboBrand.addItem(loadKNP);
		comboBPartner.addItem(loadKNP);		
		if (comboSameDistr.getItemCount() == 0)
			comboSameDistr.addItem(loadKNP);
		
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllCategorys"));
		comboCategory.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllDepartments"));
		comboDepartment.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllLines"));
		comboLine.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllSections") );
		comboSection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllBrands") );
		comboBrand.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999, Msg.translate(Env.getCtx(), "XX_AllBusinessPartner") );
		comboBPartner.addItem(loadKNP);
		
		
		//Loading Categorys
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME FROM XX_VMR_CATEGORY WHERE ISACTIVE='Y' ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " ORDER BY VALUE||'-'||NAME";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboCategory.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboCategory.setSelectedIndex(0);
		}		
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		//Loading Departments
		sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
			+ " ORDER BY VALUE||'-'||NAME";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);	
			}
			rs.close();
			pstmt.close();
			comboDepartment.setSelectedIndex(0);
		}		
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		
		//Loading Brands
		sql = "SELECT XX_VMR_BRAND_ID, VALUE||'-'||NAME FROM XX_VMR_BRAND " ;
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) +
				" ORDER BY VALUE||'-'||NAME";
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBrand.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		//Loading Business Partners
		sql = "SELECT C_BPARTNER_ID, NAME FROM C_BPARTNER WHERE " +
				" ISVENDOR = 'Y' AND XX_ISVALID = 'Y' AND ISACTIVE = 'Y'";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + 
			  " ORDER BY NAME ";
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}		
		if (allDistrib.isEmpty()) {			
			sql = "SELECT XX_VMR_DISTRIBUTIONTYPE_ID, NAME FROM XX_VMR_DISTRIBUTIONTYPE " +
					"WHERE XX_VMR_DISTRIBUTIONTYPE_ID != "
						+ Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID") ;
			sql = MRole.getDefault().addAccessSQL(
					sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + " ORDER BY XX_VMR_DISTRIBUTIONTYPE_ID";		
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {								
					allDistrib.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));				
					comboSameDistr.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				log.log(Level.SEVERE, sql, e);
			}
		}
		
		//Cargar las colecciones
		loadCollections();
		//Cargar Centros de Distribución
		loadedLocators();
		
		comboLocator.addActionListener(this);
		comboCollection.addActionListener(this);
		comboCategory.addActionListener(this);
		checkCategory.addActionListener(this);
		comboDepartment.addActionListener(this);
		checkDepartment.addActionListener(this);
		comboLine.addActionListener(this);
		checkLine.addActionListener(this);
		comboSection.addActionListener(this);
		checkSection.addActionListener(this);
		comboBrand.addActionListener(this);
		checkBrand.addActionListener(this);
		comboBPartner.addActionListener(this);
		checkBPartner.addActionListener(this);
		lookupProduct.addActionListener(this);
		checkProduct.addActionListener(this);
		comboSameDistr.addActionListener(this);
		checkPOrder.addActionListener(this);
		checkCollection.addActionListener(this);
		checkPackage.addActionListener(this);
		
	}
	
	private void loadDepartmentInfo(){
		
		comboDepartment.removeAllItems();
		
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();		
		KeyNamePair loadKNP;
		String sql = "";
				
		if(bpar == null || bpar.getKey() == 0 || bpar.getKey() == 99999999){
			//if (dept == null || dept.getKey() == 0 || dept.getKey() == 99999999) { 
				comboDepartment.removeAllItems();
				loadKNP = new KeyNamePair(0,"");
				comboDepartment.addItem(loadKNP);
				loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
				comboDepartment.addItem(loadKNP);	
				
				sql = "SELECT XX_VMR_DEPARTMENT_ID, VALUE||'-'||NAME FROM XX_VMR_DEPARTMENT ";
				
				if(cat!=null && cat.getKey() != 99999999 && cat.getKey()!=0){
					sql += " WHERE XX_VMR_CATEGORY_ID = " + cat.getKey();
				}
				
				sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) 
					+ " ORDER BY VALUE||'-'||NAME";			
			//} else
			//	return;
		} else {
			//if (dept == null || dept.getKey() == 0 || dept.getKey() == 99999999) { 
				comboDepartment.removeAllItems();
				loadKNP = new KeyNamePair(0,"");
				comboDepartment.addItem(loadKNP);
				loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllDepartments"));
				comboDepartment.addItem(loadKNP);	
				sql = "SELECT dp.XX_VMR_DEPARTMENT_ID, dp.VALUE||'-'||dp.NAME" 
					+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, XX_VMR_DEPARTMENT dp" 
					+ " WHERE ve.XX_VMR_DEPARTMENT_ID = dp.XX_VMR_DEPARTMENT_ID"
					+ " AND ve.C_BPARTNER_ID = " + bpar.getKey();	
				
				if(cat!=null && cat.getKey() != 99999999){
					sql += " AND XX_VMR_CATEGORY_ID = " + cat.getKey();
				}
					
					
			//} else 
				//return;
		} 
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboDepartment.addItem(loadKNP);			
			}			
			rs.close();
			pstmt.close();		
			comboDepartment.setSelectedItem(dept);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}		
	}
	
	/**
	 *  Load Line Info
	 */
	private void loadLineInfo(){
		
		
		KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair line;
		String sql = "";
		
		if(dpto == null)
			return;
		
		comboLine.removeAllItems();
		comboLine.setEnabled(true);
		checkLine.setEnabled(true);
		loadKNP = new KeyNamePair(0,"");
		comboLine.addItem(loadKNP);
		comboSection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllLines"));
		comboLine.addItem(loadKNP);
		
		if (dpto.getKey() == 0){
			return;
		}
		else if(dpto.getKey() == 99999999){
			return;
		}else{
			//comboLine.removeAllItems();
			//comboSection.removeAllItems();
			sql = "SELECT li.XX_VMR_LINE_ID, li.VALUE||'-'||li.NAME FROM XX_VMR_LINE li " 
				+ "WHERE li.XX_VMR_DEPARTMENT_ID = " + dpto.getKey() + " ORDER BY li.VALUE ";				
		}
							
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				line = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboLine.addItem(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	private void loadSectionInfo(){
		
		KeyNamePair dpto = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair loadKNP;
		KeyNamePair sect;
		String sql = "";
		
		if(dpto == null || line == null )
			return;
		
		comboSection.removeAllItems();
		checkSection.setEnabled(true);
		
		comboSection.removeAllItems();
		loadKNP = new KeyNamePair(0,"");
		comboSection.addItem(loadKNP);
		loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllSections"));
		comboSection.addItem(loadKNP);	
		
		if(line.getKey() == 0){
			return;
		}
		else if (line.getKey() == 99999999)
			return;
		else {
			
			comboSection.setEnabled(true);
			sql = "SELECT se.XX_VMR_SECTION_ID, se.VALUE||'-'||se.NAME" 
				+ " FROM XX_VMR_SECTION se" 				
				+ " WHERE se.XX_VMR_LINE_ID = " + line.getKey();
		}		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				sect = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboSection.addItem(sect);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
	}
	
	private void loadBPartnerInfo(){
		
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		
		KeyNamePair loadKNP;
		String sql = "";
		
		//Department selected
		if(dept == null || dept.getKey() == 0 || dept.getKey() == 99999999) {			
				comboBPartner.removeAllItems();
				loadKNP = new KeyNamePair(0,"");
				comboBPartner.addItem(loadKNP);
				loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBusinessPartner"));
				comboBPartner.addItem(loadKNP);
				
				sql = "SELECT C_BPARTNER_ID, NAME FROM C_BPARTNER WHERE " +
					" ISVENDOR = 'Y' AND XX_ISVALID = 'Y' AND ISACTIVE = 'Y'";
					sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO) + 
					" ORDER BY NAME ";							//
		} else {
			//if (bpar == null || bpar.getKey() == 0 || bpar.getKey() == 99999999) { 
				comboBPartner.removeAllItems();
				loadKNP = new KeyNamePair(0,"");
				comboBPartner.addItem(loadKNP);
				loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllBusinessPartner"));
				comboBPartner.addItem(loadKNP);	
				sql = " SELECT bp.C_BPARTNER_ID, bp.NAME" 
					+ " FROM XX_VMR_VENDORDEPARTASSOCI ve, C_BPARTNER bp " 
					+ " WHERE ve.C_BPARTNER_ID = bp.C_BPARTNER_ID AND ve.XX_VMR_DEPARTMENT_ID = " 
					+ " (SELECT dp.XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT dp " +
							" WHERE dp.XX_VMR_DEPARTMENT_ID = " + dept.getKey() + ")";						
		}		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				comboBPartner.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
			comboBPartner.setSelectedItem(bpar);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/** Llena el combo box con la informacion de las coelcciones disponibles*/
	private void loadCollections() {
		
		if (comboPackage.getItemCount() > 0) {
			comboPackage.setSelectedIndex(0);
			checkPackage.setValue(false);
		}
		
		if (comboCollection.getItemCount() > 0) {
			comboCollection.setSelectedIndex(0);
			return;
		}
			
		//String sql = "SELECT C.C_CAMPAIGN_ID, C.NAME FROM C_CAMPAIGN C WHERE C.ISACTIVE = 'Y' ORDER BY C.NAME";
		String sql = "SELECT C.XX_VMR_COLLECTION_ID, C.NAME FROM XX_VMR_COLLECTION C WHERE C.ISACTIVE = 'Y' ORDER BY C.NAME";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			comboCollection.addItem(new KeyNamePair(0, ""));
			comboCollection.addItem(new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllCollections")));
			while (rs.next()) {
				comboCollection.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {ps.close();}
				catch (Exception e) {}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {}				
			} 
		}
		comboCollection.setSelectedIndex(0);
		
			
			
	} 
	
	private boolean loadedPackages (int collection) {
		
		String sql = "SELECT XX_VMR_PACKAGE_ID, NAME FROM XX_VMR_PACKAGE WHERE XX_VMR_COLLECTION_ID = ? ORDER BY NAME";
		PreparedStatement ps = null;
		ResultSet rs = null;
		comboPackage.removeAllItems();
		try {
			ps = DB.prepareStatement(sql, null);
			ps.setInt(1, collection);
			rs = ps.executeQuery();
			comboPackage.addItem(new KeyNamePair(0, ""));
			comboPackage.addItem(new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllPackages")));
			while (rs.next()) {
				comboPackage.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {ps.close();}
				catch (Exception e) {}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {}				
			} 
		}		
		comboPackage.setSelectedIndex(0);
		return (comboPackage.getItemCount() > 2);

	}
	
	private boolean loadedLocators() {
		
		String sql = "\nSELECT L.M_LOCATOR_ID, W.VALUE||'-'||W.NAME " +
				"\nFROM M_LOCATOR L, M_WAREHOUSE W WHERE L.M_WAREHOUSE_ID = W.M_WAREHOUSE_ID  AND W.XX_ISSTORE = 'N' " +
				"AND upper(L.VALUE) like '%CHEQUEADO%'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		comboLocator.removeAllItems();
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			comboLocator.addItem(new KeyNamePair(0, ""));
			while (rs.next()) {
				comboLocator.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {ps.close();}
				catch (Exception e) {}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {}				
			} 
		}		
		comboLocator.setSelectedIndex(0);
		return (comboLocator.getItemCount() > 1);

	}
	/**
	 *  Query and generate TableInfo according with the user specifications
	 */
	private void loadTableInfo(){
		
		
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		miniTableDetail.setRowCount(0);
		log.config("");
		//  not yet initialized
		dataPane.getViewport().remove(miniTable);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable, null);

		miniTable.getModel().addTableModelListener(this);
		miniTable.getSelectionModel().addListSelectionListener(this);
		miniTable.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				int row = miniTable.getSelectedColumn();
				int column = miniTable.getSelectedRow();
				miniTable.editCellAt(column, row);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
			
		});
		comboSameDistr.setEnabled(true);
		comboSameDistr.setEditable(true);
		
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bran = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		
		//Este vector contiene las columnas y con los los id correspondientes a cada fila de la tabla
		columnsInfo = new  Vector<Vector<KeyNamePair>>();
		
		ColumnInfo colCat = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), "cat.VALUE||'-'||cat.Name", KeyNamePair.class, true, false, "cat.XX_VMR_CATEGORY_ID");
		ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"), "dp.VALUE||'-'||dp.Name", KeyNamePair.class, true, false, "dp.XX_VMR_DEPARTMENT_ID");
		ColumnInfo colLine = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Line_ID"), "li.VALUE||'-'||li.NAME", KeyNamePair.class, true, false, "li.XX_VMR_LINE_ID");
		ColumnInfo colSect = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Section_ID"), "se.VALUE||'-'||se.NAME", KeyNamePair.class, true, false, "se.XX_VMR_SECTION_ID");
		ColumnInfo colBran = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Brand"), "br.VALUE||'-'||br.Name", KeyNamePair.class, true, false, "br.XX_VMR_BRAND_ID");
		ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), "bp.NAME", KeyNamePair.class, true, false, "bp.C_BPARTNER_ID");
		ColumnInfo colProd = new ColumnInfo(Msg.translate(ctx, "M_PRODUCT_ID"), "p.prod_value_name", KeyNamePair.class, true, false, "p.M_PRODUCT_ID");
		ColumnInfo colQnty = new ColumnInfo(Msg.translate(ctx, "XX_StockQty"), "sum(p.available)", Integer.class);
		ColumnInfo colAtt = new ColumnInfo (Msg.translate(ctx, "PAttribute"), "att.description", KeyNamePair.class, true, false, "att.M_ATTRIBUTESETINSTANCE_ID");
		
		//La coleccion y el paquete
		ColumnInfo colColl = new ColumnInfo(Msg.translate(ctx, "XX_Collection"), "COL.NAME", KeyNamePair.class, true, false,  "COL.XX_VMR_COLLECTION_ID" );
		//ColumnInfo colPack = new ColumnInfo(Msg.translate(ctx, "Package"), "IV.PAQUETE_NOMBRE", KeyNamePair.class, true, false, "IV.PAQUETE_ID");		
		ColumnInfo colPack = new ColumnInfo(Msg.translate(ctx, "Package"), "PAK.NAME", KeyNamePair.class, true, false, "PAK.XX_VMR_PACKAGE_ID");		

		//Las cantidades
		ColumnInfo colQtyS = new ColumnInfo(Msg.translate(ctx, "XX_DesiredQty"), "sum(p.available)", Integer.class,false,false,null);	
		ColumnInfo colDist = new ColumnInfo(Msg.translate(ctx, "XX_DistributionMethod"), "'-'", String.class,false,false,null);
		
		//Referencia
		ColumnInfo colRef = new ColumnInfo(Msg.translate(ctx, "XX_VMR_vendorProdRef_ID"), "vr.VALUE||'-'||vr.Name", KeyNamePair.class, true, false, "vr.XX_VMR_VENDORPRODREF_ID");
		//Multiplo de Empaque, Ingnorar multiplo de empaque AGREGADO GHUCHET
		ColumnInfo colPackMul = new ColumnInfo(Msg.translate(ctx, "XX_PackageMultiple"), "DECODE(vr.XX_PackageMultiple,NULL,1, vr.XX_PackageMultiple)", Integer.class);
		ColumnInfo colIgnorePackMul = new ColumnInfo("Ignorar Múltiplo de Empaque", "'N'", Boolean.class, false, false,"");
		ColumnInfo colInconsistency = new ColumnInfo("Inconsistencia", "0", Integer.class,true,true,null);
		
		//Cambios hechos por Javier Pino
		Vector<ColumnInfo> columns = new Vector<ColumnInfo>();
		String groupby = new String();
		String from = new String(" FROM ");
		String where = new String(" WHERE ");		
		String group_by_ID = "";
		String tableName = "";
		boolean ref = false;
		
		String selectRepo = "IT.XX_VMR_DEPARTMENT_ID ";
		String whereRepo = " 1=1 ";
		
		//Clausula With - Los queries que no cambian y no son correlacionados
		String with = 
			"\n WITH " +
			
				//Piezas disponibles
				"\n PRODUCTO_DISPONIBLE AS ( select m_product_id, available from (SELECT M_PRODUCT_ID, sum(available) as available FROM " +
					"\n (SELECT M_PRODUCT_ID, SUM(QTY) AS AVAILABLE FROM M_STORAGEDETAIL " +
										"\n WHERE M_LOCATOR_ID = " + locatorCD + " AND QTYTYPE = 'H' " +
										"\n AND M_AttributeSetInstance_ID>=0 " +
										"\n AND M_lOCATOR_ID >= 0 " +
										"\n GROUP BY M_PRODUCT_ID HAVING SUM(QTY) > 0 " +
					"\n union all " + 
					//Aquellos productos que esten en una redistribucion no aprobada
					"\n SELECT M_PRODUCT_ID, -1*SUM(CANT) AS NOTAVAILABLE FROM " + 
					"\n (SELECT M_PRODUCT_ID,SUM(XX_DESIREDQUANTITY) AS CANT FROM XX_VMR_DISTRIBDETAILTEMP WHERE M_WAREHOUSE_ID = "+warehouseCD+" " +
							" GROUP BY M_PRODUCT_ID " +
					"\n union all " +
					//Aquellos productos que esten en un pedido de una predistribuida que este pendiente por etiquetar
					"\n SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_ORDER P ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE P.XX_ORDERREQUESTSTATUS = 'PE' AND P.AD_Org_ID = "+orgCD+" GROUP BY M_PRODUCT_ID " +
					"\n union all " +
					//Aquellos productos que esten en una predistribuida y que han sido chequeados	
					"\n SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_DistributionHeader H ON (h.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE H.XX_DistributionStatus IN ('QR', 'QT') AND H.M_WAREHOUSE_ID = "+warehouseCD+" " +
							" GROUP BY M_PRODUCT_ID " +
					"\n ) GROUP BY M_PRODUCT_ID " +
					"\n ) GROUP BY M_PRODUCT_ID) where available>0) ";

		//Combinar estas condiciones en un query	
		from += "\n DISPONIBLE p ";

		String sqlInvStore = "1 = 1 ";
		
		//Leyendo coleccion 
		if((Boolean)checkCollection.getValue()==true || coll.getKey()==99999999 || coll.getKey()!= 0 ){

			from += "\n JOIN M_STORAGEDETAIL D ON (p.M_PRODUCT_ID=D.M_PRODUCT_ID AND D.M_LOCATOR_ID = " + locatorCD+"  AND D.QTYTYPE= 'H' AND QTY>0)" +
//					"\n LEFT OUTER JOIN M_ATTRIBUTESETINSTANCE att ON (p.M_ATTRIBUTESETINSTANCE_ID = att.M_ATTRIBUTESETINSTANCE_ID ) " +
					"\n JOIN M_ATTRIBUTESETINSTANCE INS ON (D.M_ATTRIBUTESETINSTANCE_ID=INS.M_ATTRIBUTESETINSTANCE_ID) " +
					"\n JOIN XX_VMR_PACKAGE PAK ON (INS.XX_VMR_PACKAGE_ID = PAK.XX_VMR_PACKAGE_ID) " +
					"\n JOIN XX_VMR_COLLECTION COL ON (COL.XX_VMR_COLLECTION_ID = PAK.XX_VMR_COLLECTION_ID) " +
					"\n "; 
			columns.add(colColl);
			group_by_ID = "COL.XX_VMR_COLLECTION_ID";
			groupby += ", COL.NAME, COL.XX_VMR_COLLECTION_ID";				
			sqlInvStore += "\nAND  it.XX_VMR_COLLECTION_ID = COL.XX_VMR_COLLECTION_ID ";	
			tableName = "col";
			
			if (coll.getKey()!= 99999999) 
				where += " AND COL.XX_VMR_COLLECTION_ID = " + coll.getKey();	
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			
			}
		}	
		
		//Leyendo paquetes
		if (pack == null) {}
		else if((Boolean)checkPackage.getValue()==true || pack.getKey() == 99999999 || pack.getKey()!= 0 ){
			if((Boolean)checkCollection.getValue()==true || coll.getKey()==99999999 || coll.getKey()!= 0 ){
				
			}
			else{
			from += "\n JOIN M_STORAGEDETAIL D ON (p.M_PRODUCT_ID=D.M_PRODUCT_ID AND D.M_LOCATOR_ID = " + locatorCD +"  AND D.QTYTYPE= 'H' AND QTY>0))" +
//					"\n LEFT OUTER JOIN M_ATTRIBUTESETINSTANCE att ON (p.M_ATTRIBUTESETINSTANCE_ID = att.M_ATTRIBUTESETINSTANCE_ID ) " +
					"\n JOIN M_ATTRIBUTESETINSTANCE INS ON (D.M_ATTRIBUTESETINSTANCE_ID=INS.M_ATTRIBUTESETINSTANCE_ID) " +
					"\n JOIN XX_VMR_PACKAGE PAK ON (INS.XX_VMR_PACKAGE_ID = PAK.XX_VMR_PACKAGE_ID) " +
					"\n "; 
			}
			columns.add(colPack);
			group_by_ID = "PAK.XX_VMR_PACKAGE_ID";
			groupby += ", PAK.NAME, PAK.XX_VMR_PACKAGE_ID";			
			tableName = "PAK";
			sqlInvStore += "\nAND it.XX_VMR_PACKAGE_ID = PAK.XX_VMR_PACKAGE_ID";

	
			if (pack.getKey()!= 99999999) 
				where += " AND  PAK.XX_VMR_PACKAGE_ID= " + pack.getKey();
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}	
		}
		
		//Leer Categoria
		boolean category = false;
		if((Boolean)checkCategory.getValue()==true || cat.getKey()==99999999 ||  cat.getKey()!= 0 ){
			
			category = true;
			from += "\n INNER JOIN XX_VMR_DEPARTMENT dp ON (dp.XX_VMR_DEPARTMENT_ID = p.XX_VMR_DEPARTMENT_ID) ";
			from += "\n INNER JOIN XX_VMR_CATEGORY cat ON (cat.XX_VMR_CATEGORY_ID = dp.XX_VMR_CATEGORY_ID) ";

			columns.add(colCat);
			columns.add(colDept);
			group_by_ID = "dp.XX_VMR_DEPARTMENT_ID";
			groupby += ", cat.VALUE||'-'||cat.Name, cat.XX_VMR_CATEGORY_ID, dp.VALUE||'-'||dp.Name, dp.XX_VMR_DEPARTMENT_ID";
			sqlInvStore += "\nAND it.XX_VMR_DEPARTMENT_ID = dp.XX_VMR_DEPARTMENT_ID ";	


			tableName = "cat";
			
			if((Boolean)checkReference.getValue()==true){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
			
			if (cat.getKey()!= 99999999 ) 
				where += " AND cat.XX_VMR_CATEGORY_ID = " + cat.getKey();
			
		}
		
		//Leer Departamento
		if(dept!=null){
			if((Boolean)checkDepartment.getValue()==true || dept.getKey()==99999999 ||  dept.getKey()!= 0 ){
				
				if(!category){
					from += "\n INNER JOIN XX_VMR_DEPARTMENT dp ON (dp.XX_VMR_DEPARTMENT_ID = p.XX_VMR_DEPARTMENT_ID) ";
					columns.add(colDept);
					group_by_ID = "dp.XX_VMR_DEPARTMENT_ID";
					groupby += ", dp.VALUE||'-'||dp.Name, dp.XX_VMR_DEPARTMENT_ID";
					sqlInvStore += "\nAND it.XX_VMR_DEPARTMENT_ID = dp.XX_VMR_DEPARTMENT_ID ";	

					
					if((Boolean)checkReference.getValue()==true){
						columns.add(colRef);
						groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
						ref = true;
					}
				
					tableName = "dp";
				}
				
				if (dept.getKey()!= 99999999 ) {
					where += " AND dp.XX_VMR_DEPARTMENT_ID = " + dept.getKey();		
					whereRepo += "AND it.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
				}
			}		
		
		}
		//Leer Linea
		if((Boolean)checkLine.getValue()==true || line.getKey() == 99999999 || line.getKey()!= 0){
			
			from += "\n INNER JOIN XX_VMR_LINE li ON (li.XX_VMR_LINE_ID = p.XX_VMR_LINE_ID)" ;
			columns.add(colLine);
			group_by_ID = "li.XX_VMR_LINE_ID";
			groupby += ", li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID";		
			tableName = "li";
			sqlInvStore += "\nAND IT.XX_VMR_LINE_ID =LI.XX_VMR_LINE_ID ";	

			if (line.getKey()!= 99999999){
				where += " AND li.XX_VMR_LINE_ID = " + line.getKey();
				whereRepo += "AND it.XX_VMR_LINE_ID = " + line.getKey();
			}
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
			selectRepo += ",IT.XX_VMR_LINE_ID";
		}
		
		//Leyendo seccion 
		if ((Boolean)checkSection.getValue()==true || sect.getKey() == 99999999 || sect.getKey()!= 0){
			
			from += "\n INNER JOIN XX_VMR_SECTION se ON (se.XX_VMR_SECTION_ID = p.XX_VMR_SECTION_ID) ";
			columns.add(colSect);
			group_by_ID = "se.XX_VMR_SECTION_ID";
			groupby += ", se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID";		
			tableName = "se";
			sqlInvStore += "\nAND IT.XX_VMR_SECTION_ID =SE.XX_VMR_SECTION_ID ";	
			
			if (sect.getKey()!= 99999999 ) {
				where += " AND se.XX_VMR_SECTION_ID = " + sect.getKey();
				whereRepo += " AND it.XX_VMR_SECTION_ID = " + sect.getKey();
			}
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
			selectRepo += ",IT.XX_VMR_SECTION_ID";
		}
		
		//Leyendo marca
		if ((Boolean)checkBrand.getValue()==true || bran.getKey() == 99999999 || bran.getKey()!= 0){
			
			from += "\n INNER JOIN XX_VMR_BRAND br ON (p.XX_VMR_BRAND_ID = br.XX_VMR_BRAND_ID) ";
			columns.add(colBran);
			group_by_ID = "br.XX_VMR_BRAND_ID";
			groupby += ", br.VALUE||'-'||br.Name, br.XX_VMR_BRAND_ID";	
			tableName = "br";
			sqlInvStore += "\nAND it.XX_VMR_BRAND_ID =BR.XX_VMR_BRAND_ID ";
			
			if (bran.getKey()!= 99999999) {
				where += " AND br.XX_VMR_BRAND_ID = " + bran.getKey();	
				whereRepo += " AND it.XX_VMR_BRAND_ID = " + bran.getKey();
			}
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
			selectRepo += ",IT.XX_VMR_BRAND_ID";
		}
		
		//Leyendo proveedor
		if((Boolean)checkBPartner.getValue() == true || bpar.getKey() == 99999999 || bpar.getKey() != 0){
			
			from += "\n INNER JOIN C_BPARTNER bp ON (bp.C_BPARTNER_ID = p.C_BPARTNER_ID) ";
			columns.add(colBPar);
			group_by_ID = "bp.C_BPARTNER_ID";
			groupby += ", bp.NAME, bp.C_BPARTNER_ID";	
			tableName = "bp";
			sqlInvStore += "\nAND it.C_BPARTNER_ID =BP.C_BPARTNER_ID ";
			
			if (bpar.getKey()!= 99999999) {
				where += " AND bp.C_BPARTNER_ID = " + bpar.getKey();	
				whereRepo += "AND it.C_BPARTNER_ID = " + bpar.getKey();
			}
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
			selectRepo += ",IT.C_BPARTNER_ID";
		}
//COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
//		//Leyendo paquetes
//		if (pack == null) {}
//		else if((Boolean)checkPackage.getValue()==true || pack.getKey() == 99999999 || pack.getKey()!= 0 ){
//					
//			colQnty = new ColumnInfo(Msg.translate(ctx, "XX_StockQty"), "sum(IV.QTYONHAND)", Integer.class);
//			colQtyS = new ColumnInfo(Msg.translate(ctx, "XX_DesiredQty"), "sum(IV.QTYONHAND)", Integer.class,false,false,null);	
//
//			
//			with = 				
//					" DISPONIBLE AS ( select m_product_id, available from (SELECT M_PRODUCT_ID, sum(available) as available FROM " +
//					" (SELECT M_PRODUCT_ID, SUM(QTY) AS AVAILABLE FROM M_STORAGEDETAIL " +
//					" WHERE M_LOCATOR_ID = " + locatorCD + " AND QTYTYPE = 'H' " +
//					" AND M_AttributeSetInstance_ID>=0 " +
//					" AND M_lOCATOR_ID >= 0 " +
//					" GROUP BY M_PRODUCT_ID HAVING SUM(QTY) > 0 " +
//					" union all " + 
//					" SELECT M_PRODUCT_ID, -1*SUM(CANT) AS NOTAVAILABLE FROM " + 
//					" (SELECT M_PRODUCT_ID,SUM(XX_DESIREDQUANTITY) AS CANT FROM XX_VMR_DISTRIBDETAILTEMP WHERE M_WAREHOUSE_ID = "+warehouseCD+" GROUP BY M_PRODUCT_ID " +
//					" union all " +
//					" SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_ORDER P ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE P.XX_ORDERREQUESTSTATUS = 'PE' AND P.AD_Org_ID = "+orgCD+"  GROUP BY M_PRODUCT_ID " +
//					" union all " +
//					" SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_DistributionHeader H ON (h.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE H.XX_DistributionStatus IN ('QR', 'QT') AND H.M_WAREHOUSE_ID = "+warehouseCD+" GROUP BY M_PRODUCT_ID " +
//					" ) GROUP BY M_PRODUCT_ID " +
//					" ) GROUP BY M_PRODUCT_ID) where available>0) ";
//
//			//Combinar estas condiciones en un query	
//			from = " FROM XX_CurrentStorageView iv " +
//				"JOIN M_PRODUCT p ON ( IV.M_PRODUCT_ID = p.M_PRODUCT_ID) " +
//				"JOIN XX_VMR_VENDORPRODREF vr ON ( vr.XX_VMR_VENDORPRODREF_ID = p.XX_VMR_VENDORPRODREF_ID) "; 
//			
//			//Parte de las condiciones que realizan algo similar al NOT IN 
//			where = " WHERE iv.M_LOCATOR_ID = " + locatorCD ;
//		
//			columns.add(colPack);
//			where += " AND iv.Paquete_ID IS NOT NULL";
//			group_by_ID = "iv.paquete_id";
//			groupby += ", iv.paquete_nombre, iv.paquete_id";			
//			tableName = "iv";
//			sqlInvStore += "\nAND it.XX_VMR_PACKAGE_ID = iv.paquete_id ";
//
//			
//			if (pack.getKey()!= 99999999) 
//				where += " AND iv.paquete_id = " + pack.getKey();
//			
//			if((Boolean)checkReference.getValue()==true && !ref){
//				columns.add(colRef);
//				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
//				ref = true;
//			}	
//		}
//HASTA AQUI COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
		
		//Leyendo producto
		if((Boolean)checkProduct.getValue() == true || lookupProduct.getValue() != null ){
			from += "\n LEFT OUTER JOIN M_ATTRIBUTESETINSTANCE att ON (p.M_ATTRIBUTESETINSTANCE_ID = att.M_ATTRIBUTESETINSTANCE_ID )";
			columns.add(colProd);
			columns.add(colAtt);
			group_by_ID = "p.M_PRODUCT_ID";
			groupby += ", p.prod_value_name, p.M_PRODUCT_ID, att.description, att.M_ATTRIBUTESETINSTANCE_ID";			
			tableName = "p";
			sqlInvStore += "\nAND it.M_PRODUCT_ID = p.M_PRODUCT_ID ";

			
			if (lookupProduct.getValue() != null && checkProduct.getValue().equals(false)) {
				where += " AND p.M_PRODUCT_ID = " + lookupProduct.getValue();	
				whereRepo += " AND it.M_PRODUCT_ID = " + lookupProduct.getValue();	
			}
			
			selectRepo += ",it.M_PRODUCT_ID ";
		}

		//Si se intenta buscar sin seleccionar filtro, debe irse
		if (columns.size() == 0) {
			m_frame.setCursor(Cursor.getDefaultCursor());
			return;
		}
		
		if((Boolean)checkReference.getValue()==true){
			from +="\nJOIN XX_VMR_VENDORPRODREF vr ON ( vr.XX_VMR_VENDORPRODREF_ID = p.XX_VMR_VENDORPRODREF_ID) ";
			columns.add(colInconsistency);
			columns.add(colIgnorePackMul);
			columns.add(colPackMul);
			groupby += ", vr.XX_PackageMultiple";
			sqlInvStore += "\nAND it.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID ";
			
			selectRepo += ",it.XX_VMR_VENDORPRODREF_ID ";
		
		}
		String groupbyDisp = selectRepo;
		with += "\n, DISPONIBLE AS ( " +
				"\nSELECT " +selectRepo;
				if((Boolean)checkProduct.getValue() == true || lookupProduct.getValue() != null ){
					with += "\n,it.value||'-'||it.name prod_value_name, it.M_ATTRIBUTESETINSTANCE_ID";
					groupbyDisp += ", it.value||'-'||it.name, it.M_ATTRIBUTESETINSTANCE_ID "; 
				}
			with +=	"\n, sum(d.available)  available" +
					"\nFROM PRODUCTO_DISPONIBLE d "+
					"\nJOIN M_PRODUCT it ON ( it.M_PRODUCT_ID = d.M_PRODUCT_ID) " +
					"\nWHERE " +whereRepo +
					 "\nGROUP BY  "+groupbyDisp +" " +
					 "\nHAVING SUM(d.available)>0 )\n";
		
		if((Boolean)checkInventoryFails.getValue()==true){
			with += "\n, FALLA_INVENTARIO AS (" +
					"\nSELECT " +selectRepo+
					"\n,NVL(SUM(it.ESTIMATEDSALES),0) Venta_Estimada," +
					"\nSUM(it.REPOSICION)  Reposicion," +
					"\nNVL(SUM(it.QTYSTORE+it.QTYTRANSIT),0) Inventario," +
					"\nSUM(it.REALSALES) Venta_Real," +
					"\nSUM(it.AVERAGESALES) Venta_Promedio " +
					"\n,SUM(it.ReposicionNegativos) ReposicionNegativos" +
					"\nFROM XX_ESTIMATEDSALES it "+
					"\nWHERE " +whereRepo +
					 "\nGROUP BY  "+selectRepo +")\n" ;
			from += "\n LEFT JOIN FALLA_INVENTARIO IT ON ("+sqlInvStore+")";
			groupby += "\n, IT.Venta_Estimada, IT.Reposicion, IT.Inventario, nvl(IT.Venta_Real,0) , IT.Venta_Promedio, IT.ReposicionNegativos ";
			ColumnInfo colVentaEstimada = new ColumnInfo("Venta Estimada", "nvl(IT.Venta_Estimada+0.5,0)", Integer.class);
			ColumnInfo colReposicion = new ColumnInfo("Reposición", "nvl(IT.Reposicion,0)", Integer.class);
			ColumnInfo colInvenTienda = new ColumnInfo("Inventario en Tienda", " nvl(IT.Inventario,0) ", Integer.class);
			ColumnInfo colRealSales = new ColumnInfo("Venta Real", "nvl(IT.Venta_Real,0) ", Integer.class);
			ColumnInfo colReposicionCol = new ColumnInfo("Falla", "CASE WHEN nvl(IT.Reposicion,0) > 0 THEN -1 WHEN nvl(IT.Reposicion,0) = 0 THEN 0 ELSE 1 END", BigDecimal.class);
			ColumnInfo colReposicionColNeg = new ColumnInfo("Falla Producto", "CASE WHEN round(nvl(IT.ReposicionNegativos,0)) = 0 THEN 0 ELSE 1 END", BigDecimal.class);
			columns.add(colVentaEstimada);
			columns.add(colReposicion);
			columns.add(colRealSales);
			columns.add(colInvenTienda);
			columns.add(colReposicionCol);
			columns.add(colReposicionColNeg);
		}
		columns.add(colQnty);
		columns.add(colQtyS);
		columns.add(colDist);		

		//Preparar el columninfo
		ColumnInfo [] columns_array = new ColumnInfo[columns.size() + 1];
		columns.insertElementAt(new ColumnInfo("", group_by_ID, IDColumn.class, false, false, ""), 0);
		
		for (int i = 0; i < columns.size() ; i++) {
			columns_array[i] = columns.elementAt(i);
		}		
		indx = columns_array.length - 2;
				
		//Preparar la tabla				
		try {
			String select = miniTable.prepareTable(columns_array, "", "", true, tableName);
			m_sql = new StringBuilder(with.length() + where.length() + from.length() + select.length() );
			m_sql.append(with);
			m_sql.append(select);
			m_sql.append(from);
			if (where.length()>7) // si tiene 7 o menos quiere decir que no hay ni una sola condicion en el where, por lo cual quitamos el where
			{
				if (((where.substring(0,14)).toUpperCase()).contains("AND")) // si el where despues de "where" tiene un and, se lo quitamos
				{
					where = (where.toUpperCase()).replaceFirst("AND", " ");
				}
				m_sql.append(where);
			}
			m_sql.append("\n GROUP BY " + group_by_ID);
			m_sql.append(groupby);
			m_sql.append("\n ORDER BY " + group_by_ID);
		} 
		catch (Exception e) {
			e.printStackTrace();
			m_frame.setCursor(Cursor.getDefaultCursor());
			return ;
		}		
		System.out.println(m_sql.toString());
		PreparedStatement pstmt = DB.prepareStatement(m_sql.toString(), null);	
		ResultSet rs =null;
		try
		{	//System.out.println(m_sql.toString());
			rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.log(Level.SEVERE, m_sql.toString(), e);
			m_frame.setCursor(Cursor.getDefaultCursor());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//Setting ComboBox for Redistribution
		VComboBox distribution = new VComboBox();		
		for(int i = 0; i < allDistrib.size(); i++)	
			distribution.addItem(allDistrib.get(i));
		
		TableColumn col = miniTable.getColumnModel().getColumn(indx+1);		
		col.setCellEditor(new ComboBoxEditor(allDistrib));
		col.setCellRenderer(new ComboBoxRenderer(allDistrib));
		miniTable.setMultiSelection(true);
	//	miniTable.setRowSelectionAllowed(false);   //COMENTADO POR GHUCHET
		
		//Updating STOCK QUANTITY LABEL
		Integer stockQty = 0;
		
		for(int i = 0; i < miniTable.getRowCount(); i++)
			stockQty = stockQty + (Integer)miniTable.getModel().getValueAt(i, indx-1);
		
		labelStockQty.setText(m_format.format(stockQty));
		
		//miniTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		
		calculateSelection();
		miniTable.autoSize();
		miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
		miniTable.getTableHeader().setReorderingAllowed(false);
		
		//AGREGADO GHUCHET
		if((Boolean)checkReference.getValue()==true){
			XX_IndicatorCellRenderer renderer = new XX_IndicatorCellRenderer(0, 1);
	        renderer.setStringPainted(false);
	        renderer.setBackground(Color.lightGray);
			 // set limit value and fill color
	        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();        
	        limitColors.put(new Integer(0), Color.yellow); 
	        renderer.setLimits(limitColors);        
	        
	        miniTable.getColumnModel().getColumn(miniTable.getColumnModel().getColumnIndex("Inconsistencia")).setCellRenderer(renderer);
	        miniTable.getColumnModel().getColumn(miniTable.getColumnModel().getColumnIndex("Inconsistencia")).setMaxWidth(82);
		  }
		if((Boolean)checkInventoryFails.getValue()==true){
			XX_IndicatorCellRenderer rendererInv = new XX_IndicatorCellRenderer(-2,1);
		    rendererInv.setStringPainted(false);
		    rendererInv.setBackground(Color.getHSBColor( 0.333f, 1.000f, 0.7f ));
			 // set limit value and fill color
		    Hashtable<Integer, Color> limitColorsInv = new Hashtable<Integer, Color>();  
		    limitColorsInv.put(0, Color.getHSBColor(0.000f, 1.000f, 0.87f));
		    limitColorsInv.put(-1, Color.getHSBColor( 0.167f, 1.000f, 1.000f)); //YELLOW
		    limitColorsInv.put(-2, Color.getHSBColor( 0.333f, 1.000f, 0.7f )); //GREEN
		 
		    rendererInv.setLimits(limitColorsInv);        
		  
		    miniTable.getColumnModel().getColumn(miniTable.getColumnModel().getColumnIndex("Falla")).setCellRenderer(rendererInv);
	        miniTable.getColumnModel().getColumn(miniTable.getColumnModel().getColumnIndex("Falla")).setMaxWidth(82);
	        
	        XX_IndicatorCellRenderer rendererInv2 = new XX_IndicatorCellRenderer(-2,1);
		    rendererInv2.setStringPainted(false);
		    rendererInv2.setBackground(Color.getHSBColor( 0.333f, 1.000f, 0.7f ));
			 // set limit value and fill color
		    Hashtable<Integer, Color> limitColorsInv2 = new Hashtable<Integer, Color>();  
		    limitColorsInv2.put(0, Color.getHSBColor(0.000f, 1.000f, 0.87f));
		    limitColorsInv2.put(-1, Color.getHSBColor( 0.333f, 1.000f, 0.7f )); //GREEN
		 
		    rendererInv2.setLimits(limitColorsInv2);        
		  
		    miniTable.getColumnModel().getColumn(miniTable.getColumnModel().getColumnIndex("Falla Producto")).setCellRenderer(rendererInv2);
	        miniTable.getColumnModel().getColumn(miniTable.getColumnModel().getColumnIndex("Falla Producto")).setMaxWidth(82);
		}
		//FIN AGREGADO GHUCHET
		
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		
	}   //  loadTableInfo
	
	/**
	 * Genera las tablas de acuerdo al filtro seleccionado
	 * @AUTHOR Javier Pino (Computacion 6 )
	 * */
	private void loadTablePOInfo() {
		
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		dataPane.getViewport().remove(miniTable);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable, null); 
		//dataPane.setPreferredSize(new Dimension(1024, 300));
		
		miniTable.addColumn(" ");
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_PurchaseOrder"));
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"));
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_VMR_Brand"));
		miniTable.addColumn(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_OrderType"));
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_OrderStatus"));
		
		//Agregadas estas dos columnas
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_Collection"));
		miniTable.addColumn(Msg.translate(Env.getCtx(), "Package"));
		
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_ProductQty"));
		miniTable.addColumn(Msg.translate(Env.getCtx(), "XX_DistributionMethod"));

		miniTable.setMultiSelection(true);
		miniTable.setRowSelectionAllowed(true);
		
		//The renderers for the columns
		miniTable.setColumnClass(0, IDColumn.class, false); //  1-PO order
		miniTable.setColumnClass(1, KeyNamePair.class, true); //  1-PO order
		miniTable.setColumnClass(2, KeyNamePair.class, true); //  2-Department
		miniTable.setColumnClass(3, KeyNamePair.class, true); //  3-Brand
		miniTable.setColumnClass(4, KeyNamePair.class, true); //  3-Business
		miniTable.setColumnClass(5, String.class, true); //  5-type
		miniTable.setColumnClass(6, String.class, true); //  5-type
		
		miniTable.setColumnClass(7, String.class, true); //  
		miniTable.setColumnClass(8, String.class, true); //  5-type
		
		miniTable.setColumnClass(9, Integer.class, true); //  6-qty
		miniTable.setColumnClass(10, String.class, false); //  7-method
			
		//Add listeners		
		miniTable.getTableHeader().setReorderingAllowed(false);
		miniTable.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {				
				int row = miniTable.getSelectedColumn();
				int column = miniTable.getSelectedRow();
				miniTable.editCellAt(column, row);
			}
			public void keyPressed(KeyEvent e) {}			
		});				
		//miniTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		
		comboSameDistr.setEnabled(true);
		comboSameDistr.setEditable(true);
		
		
		//Build the sql string 
		StringBuilder sql = new StringBuilder();
		String s =" SELECT PO.DOCUMENTNO, PO.XX_VMR_DEPARTMENT_ID, PO.C_BPARTNER_ID, " +  
			" PO.XX_ORDERTYPE, PO.XX_ORDERSTATUS, PO.XX_PRODUCTQUANTITY, PO.C_ORDER_ID, PO.XX_VMR_COLLECTION_ID, PO.XX_VMR_PACKAGE_ID" +
			" FROM C_ORDER PO ";
		s = MRole.getDefault().addAccessSQL(s, "PO", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		sql.append(s);
		sql.append (" AND PO.XX_VLO_TYPEDELIVERY = 'PD' AND PO.ISACTIVE = 'Y' AND PO.ISSOTRX = 'N' ");
		sql.append(" AND ((PO.XX_ORDERTYPE = 'Importada' AND PO.XX_ORDERSTATUS NOT IN (" + "'PRO', 'AN') AND PO.XX_IMPORTINGCOMPANY_ID = " + Env.getCtx().getContext("#XX_L_VSI_CLIENTCENTROBECO_ID")+ ") ");
		sql.append(" OR  (PO.XX_ORDERTYPE = 'Nacional' AND PO.XX_ORDERSTATUS NOT IN ('PRO', 'AN'))) ");
		sql.append(" AND PO.M_WAREHOUSE_ID = "+warehouseCD); //AGREGADO PROYECTO CD VALENCIA
		sql.append(" AND PO.C_ORDER_ID NOT IN ");
		sql.append(" (SELECT D.C_ORDER_ID FROM XX_VMR_DISTRIBUTIONHEADER D ");
			sql.append(" WHERE D.XX_DISTRIBUTIONSTATUS NOT IN ('" + X_Ref_XX_DistributionStatus.ANULADA.getValue() + "') " +
					"AND D.C_ORDER_ID IS NOT NULL) ");
		
			//Checks the selection
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		KeyNamePair brand = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();

		if (cat!=null && !(Boolean)checkCategory.getValue() && cat.getKey() != 99999999 && cat.getKey() != 0)  
			sql.append(" AND PO.XX_VMR_CATEGORY_ID = " + cat.getKey() + " ");
		if (dept!=null && !(Boolean)checkDepartment.getValue() && dept.getKey() != 99999999 && dept.getKey() != 0)  
			sql.append(" AND PO.XX_VMR_DEPARTMENT_ID = " + dept.getKey() + " ");		
		if (!(Boolean)checkBPartner.getValue() && bpar.getKey() != 99999999  && bpar.getKey() != 0)
			sql.append(" AND PO.C_BPARTNER_ID = " + bpar.getKey() + " ");		
		if (!(Boolean)checkCollection.getValue() && coll.getKey() != 99999999 && coll.getKey() != 0)  
			sql.append(" AND PO.XX_VMR_COLLECTION_ID = " + coll.getKey() + " ");
		if (pack == null) {} 
		else {
			if (!(Boolean)checkPackage.getValue() && pack.getKey() != 99999999 && pack.getKey() != 0)  
				sql.append(" AND PO.XX_VMR_PACKAGE_ID = " + pack.getKey() + " ");
		}
		
		
		sql.append(" ORDER BY PO.DOCUMENTNO ");	

		int docno, dep_id, par_id, qty, order_id, collection_id, package_id ; 
		String type , status;
		//Reading the result
		try {
			
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();			
			int row = miniTable.getRowCount();
			while (rs.next()) {
				docno = rs.getInt(1);
				dep_id = rs.getInt(2);				
				par_id = rs.getInt(3);				 
				type = rs.getString(4);
				status = rs.getString(5);			
				qty  = rs.getInt(6);
				order_id = rs.getInt(7);
				collection_id = rs.getInt(8);
				package_id = rs.getInt(9);
								
				KeyNamePair related_brand = null;
				boolean found = false;
				
				String sql_brandqty = " SELECT COUNT(DISTINCT(L.XX_VMR_BRAND_ID)) FROM XX_VMR_PO_LINEREFPROV L WHERE " +
					" L.C_ORDER_ID = " + order_id + " AND L.XX_VMR_BRAND_ID IS NOT NULL ";				
				PreparedStatement psmt_brandqty = DB.prepareStatement(sql_brandqty, null);
				try {					
					ResultSet rs_brandqty = psmt_brandqty.executeQuery();					
					String sql_brand = 
						" SELECT L.XX_VMR_BRAND_ID FROM XX_VMR_PO_LINEREFPROV L WHERE " +
							" L.C_ORDER_ID = " + order_id + " AND L.XX_VMR_BRAND_ID IS NOT NULL " ;							
					PreparedStatement ps_brand = DB.prepareStatement(sql_brand, null);
					if (rs_brandqty.next()) {
						if (rs_brandqty.getInt(1) > 1) {
							if ((Boolean)checkBrand.getValue() || brand.getKey() == 99999999 || brand.getKey() == 0) {
								related_brand = new KeyNamePair(0, Msg.translate(Env.getCtx(), "XX_Several"));	
								found = true;
							} else {
								related_brand = new KeyNamePair(0, Msg.translate(Env.getCtx(), "XX_Several"));
								ResultSet rs_brand = ps_brand.executeQuery();								
								while (rs_brand.next()) {
									if (rs_brand.getInt(1) == brand.getKey()) {
										found = true;
										break;
									} 
								}								
								rs_brand.close();								
							}
						} else {							
							ResultSet rs_brand = ps_brand.executeQuery();
							related_brand = new KeyNamePair(0, Msg.translate(Env.getCtx(), "XX_None"));
							if (rs_brand.next()) {
								if ((Boolean)checkBrand.getValue() || brand.getKey() == 99999999 
										|| brand.getKey() == 0) {
									
									X_XX_VMR_Brand brand2 = 
										new X_XX_VMR_Brand(Env.getCtx(), rs_brand.getInt(1) , null);
									related_brand = brand2.getKeyNamePair();
									found = true;
								} else {
									if (rs_brand.getInt(1) == brand.getKey()) {
										related_brand = brand;
										found = true;
									} else {
										found = false;
									}
								}																
							} else {
								if ((Boolean)checkBrand.getValue() || brand.getKey() == 99999999 
										|| brand.getKey() == 0) {										
									found = true;
								} else {
									found = false;
								}
							}							
							rs_brand.close();								
						}  
					}								
					ps_brand.close();
					psmt_brandqty.close();
					rs_brandqty.close();
				} catch (SQLException e) {
					e.printStackTrace();
					ADialog.error(m_WindowNo, m_frame, Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"));
				}
				if (!found)	
					continue;
				
				miniTable.setRowCount(row + 1);
				miniTable.setValueAt(new IDColumn(row), row, 0);				
				miniTable.setValueAt(new KeyNamePair(order_id, ""+docno), row, 1);
				
				X_XX_VMR_Department d = new X_XX_VMR_Department(Env.getCtx(), dep_id, null);				
				miniTable.setValueAt(new KeyNamePair(dep_id, d.getValue()+ "-"+ d.getName()), row, 2);
				
				miniTable.setValueAt(related_brand, row, 3);
				
				MBPartner b = new MBPartner(Env.getCtx(), par_id, null);				
				miniTable.setValueAt(new KeyNamePair (par_id, b.getName()), row, 4);
				
				miniTable.setValueAt(type, row, 5);
				miniTable.setValueAt(status, row, 6);
				
				X_XX_VMR_Collection c = new X_XX_VMR_Collection(Env.getCtx(), collection_id, null);
				miniTable.setValueAt(new KeyNamePair(collection_id, c.getName()), row, 7);
				
				X_XX_VMR_Package p = new X_XX_VMR_Package(Env.getCtx(), package_id, null); 
				miniTable.setValueAt(new KeyNamePair(package_id, p.getName()), row, 8);
				miniTable.setValueAt(qty, row, 9);			
				row++;
			}
			pstmt.close();
			rs.close();
					
			//Load the distributions
			VComboBox distribution = new VComboBox();				
			for (int i = 0; i < allDistrib.size(); i++)	
				distribution.addItem(allDistrib.get(i));
			
			TableColumn col = miniTable.getColumnModel().getColumn(10);				
			col.setCellEditor(new ComboBoxEditor(allDistrib));
			col.setCellRenderer(new ComboBoxRenderer(allDistrib));
			miniTable.getModel().addTableModelListener(this);
			miniTable.autoSize(false);
			
			//Updating STOCK QUANTITY LABEL
			Integer stockQty = 0;			
			for (int i = 0; i < miniTable.getRowCount(); i++)
				stockQty += ((Number)miniTable.getValueAt(i, 9)).intValue();
			
			labelStockQty.setText(m_format.format(stockQty));
			miniTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			miniTable.getTableHeader().setReorderingAllowed(false);
			miniTable.setRowHeight(miniTable.getRowHeight() + 2);
			m_frame.pack();
			//m_frame.setExtendedState(m_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH); 
			AEnv.showCenterScreen(m_frame);
			
		} catch (SQLException e) {
			e.printStackTrace();			
			}
		
		m_frame.setCursor(Cursor.getDefaultCursor());
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

	/**************************************************************************************/
	/**************************************************************************
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		
		//Category Combo 
		if (e.getSource() == comboCategory && !is_purchaseorder){			
			
			if(!comboCategory.getValue().equals(Integer.valueOf(0))){
				loadDepartmentInfo();
				if(comboCategory.getValue().equals(99999999))
					checkDepartment.setEnabled(true);
				else
					checkDepartment.setEnabled(false);
				comboLine.setEnabled(false);
				checkLine.setEnabled(false);
				comboSection.setEnabled(false);
				checkSection.setEnabled(false);
			}else{
				loadDepartmentInfo();
				checkDepartment.setEnabled(true);
				comboLine.setEnabled(false);
				checkLine.setEnabled(false);
				comboSection.setEnabled(false);
				checkSection.setEnabled(false);
			}
		}
		//Category Check
		else if(e.getSource() == checkCategory && !is_purchaseorder){
			
			if((Boolean)checkCategory.getValue() == true){
				comboCategory.setValue(99999999);
				comboCategory.setEnabled(false);
				
				loadDepartmentInfo();
				comboDepartment.setEnabled(true);
				checkDepartment.setEnabled(true);
				
				comboLine.setValue(0);
				comboLine.setEnabled(false);
				comboSection.setValue(0);
				comboSection.setEnabled(false);
				
			}else{
				comboCategory.setEnabled(true);
				comboDepartment.setEnabled(true);
				comboLine.setEnabled(true);
				comboSection.setEnabled(true);
				checkDepartment.setValue(false);
				checkLine.setValue(false);
				checkSection.setValue(false);
				loadBasicInfo();
			}
		}
		else if(e.getSource() == checkCategory && is_purchaseorder){
			
			if((Boolean)checkCategory.getValue() == true){
				comboCategory.setValue(99999999);
				comboCategory.setEnabled(false);				
			}else{	
				comboCategory.setValue(0);
				comboCategory.setEnabled(true);								
			}
		}
		//Updating Line ComboBox after a Department is selected 
		else if (e.getSource() == comboDepartment && !is_purchaseorder){			
			loadLineInfo();
			
			comboBPartner.removeActionListener(this);
			loadBPartnerInfo();
			comboBPartner.addActionListener(this);
			checkBPartner.setValue(false);
			
			if(comboDepartment.getValue() != null){
				if(comboDepartment.getValue().equals(Integer.valueOf(0)))
					comboLine.setEnabled(false);
				else
					comboLine.setEnabled(true);
			}
		}
		
		//Department CheckBox  
		else if(e.getSource() == checkDepartment && !is_purchaseorder){
			
			if((Boolean)checkDepartment.getValue() == true){
				comboDepartment.setValue(99999999);
				comboDepartment.setEnabled(false);
				comboLine.setValue(0);
				comboLine.setEnabled(false);
				comboSection.setValue(0);
				comboSection.setEnabled(false);
				
			}else{	
				comboDepartment.setEnabled(true);
				comboLine.setEnabled(true);
				comboSection.setEnabled(true);
				checkLine.setValue(false);
				checkLine.setValue(false);
				loadBasicInfo();
			}
		}
		else if(e.getSource() == checkDepartment && is_purchaseorder){
			
			if((Boolean)checkDepartment.getValue() == true){
				comboDepartment.setValue(99999999);
				comboDepartment.setEnabled(false);				
			}else{	
				comboDepartment.setValue(0);
				comboDepartment.setEnabled(true);								
			}
		}
		
		//Updating Section ComboBox after a Line is selected
		else if (e.getSource() == comboLine ){
			loadSectionInfo();
		}
		
		//Line CheckBox
		else if(e.getSource() == checkLine){
			if((Boolean)checkLine.getValue() == true){
				if((Boolean)checkDepartment.getValue() == true || ((KeyNamePair)comboDepartment.getSelectedItem()).getKey() != 0){
					comboLine.setValue(99999999);
					comboLine.setEnabled(false);
				}else{
					ADialog.error(m_WindowNo, this, "No es posible seleccionar todas las Líneas sin especificar el Departamento", Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
					checkLine.setValue(false);
				}	
			}else{
				comboLine.setEnabled(true);
				loadLineInfo();
				comboLine.setValue(0);
			}
		}
		
		//Section CheckBox
		else if(e.getSource() == checkSection){
			if((Boolean)checkSection.getValue() == true){
				if((Boolean)checkLine.getValue() == true || ((KeyNamePair)comboLine.getSelectedItem()).getKey() != 0){
					comboSection.setValue(99999999);
					comboSection.setEnabled(false);
				}else{
					ADialog.error(m_WindowNo, this, "You must specified a Line before choose a Section", Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"));
					checkSection.setValue(false);
				}
				
			}else{	
				comboSection.setEnabled(true);
				
				loadSectionInfo();
				comboSection.setValue(0);
			}
		}
		
		//Brand CheckBox
		else if(e.getSource() == checkBrand){
			if((Boolean)checkBrand.getValue() == true){
				comboBrand.setValue(99999999);
				comboBrand.setEnabled(false);
			}else{
				comboBrand.setEnabled(true);
				comboBrand.setValue(0);
			}
		}
		
		//Updating Department ComboBox when a Business Partner is selected
		else if(e.getSource() == comboBPartner) {
			comboDepartment.removeActionListener(this);
			loadDepartmentInfo();
			comboDepartment.addActionListener(this);
		}
		//Business Partner CheckBox
		else if(e.getSource() == checkBPartner){
			if((Boolean)checkBPartner.getValue() == true){
				comboBPartner.setValue(99999999);
				comboBPartner.setEnabled(false);
			}else{
				comboBPartner.setEnabled(true);
				comboBPartner.setValue(0);			
				loadBPartnerInfo();
			}
		}
		
		//Product CheckBox
		else if(e.getSource() == checkProduct){
			if((Boolean) checkProduct.getValue() == true){
				
				try{
					lookupProduct.setValue(new KeyNamePair(0, Msg.translate(Env.getCtx(), "XX_AllProducts")));
				}catch(Exception ex){
					
				}				
				lookupProduct.setEnabled(false);
			}else{
				lookupProduct.setValue(null);
				lookupProduct.setEnabled(true);
			}
		}
		
		else if(e.getSource() == checkPOrder){
			if((Boolean) checkPOrder.getValue() == true){
				
				comboCategory.setValue(0);
				checkCategory.setValue(false);
				
				comboDepartment.setValue(0);
				checkDepartment.setValue(false);
				
				comboLine.setValue(0);
				comboLine.setEnabled(false);
				
				checkLine.setValue(false);
				checkLine.setEnabled(false);
				
				comboSection.setValue(0);
				comboSection.setEnabled(false);
				
				checkSection.setValue(false);
				checkSection.setEnabled(false);
				
				comboBrand.setValue(0);
				checkBrand.setValue(false);
								
				comboBPartner.setValue(0);				
				checkBPartner.setValue(false);
				
				lookupProduct.setValue(null);
				lookupProduct.setEnabled(false);
				
				checkProduct.setValue(false);
				checkProduct.setEnabled(false);
				
				checkReference.setValue(false);
				checkReference.setEditable(false);
								
			}else{
				checkReference.setEditable(true);
				loadBasicInfo();
			}
		}
		//Clean Form
		else if(e.getSource() == bReset){
			miniTable.setRowCount(0);
			miniTableDetail.setRowCount(0);
			loadBasicInfo();
			miniTable.repaint();
			miniTableDetail.repaint();
		}
		
		//Generate Selection
		else if (e.getSource() == bGenerate && !is_purchaseorder)
		{
			Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			Cursor defaultCursor = Cursor.getDefaultCursor();
			m_frame.setCursor(waitCursor);
			String mss = "¿Desea agrupar su selección en un solo detalle?";
			boolean ok  = false; //ADialog.ask(1, new Container(), mss);
			int seleccion = JOptionPane.showOptionDialog(this, mss, 
					"Seleccione una opción", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,   new Object[] { "Si", "No"},  "Si");
			if (seleccion != -1){
			   if((seleccion + 1)==1)  ok = true;
			   else ok = false;
			}

			processSelection(ok);
			
			miniTable.setRowCount(0);
			miniTableDetail.setRowCount(0);
			loadBasicInfo();
			miniTable.repaint();
			miniTableDetail.repaint();
			m_frame.setCursor(defaultCursor);
		}
		
		else if (e.getSource() == bGenerate && is_purchaseorder)
		{
			Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			Cursor defaultCursor = Cursor.getDefaultCursor();
			m_frame.setCursor(waitCursor);
			processPOSelection();
			loadTablePOInfo();
			miniTable.repaint();
			m_frame.setCursor(defaultCursor);		
		}
		//La causa es el combo de coleccion
		else if (e.getSource().equals(comboCollection)) {
			KeyNamePair collection = (KeyNamePair)comboCollection.getSelectedItem();
			if ((collection != null) && (collection.getKey() != 99999999) && (collection.getKey() != 0)) {
				if (loadedPackages(collection.getKey())) {
					checkPackage.setEnabled(true);
					checkPackage.setValue(false);
					comboPackage.setEnabled(true);
					comboPackage.setEditable(true);
					return;
				}
			}		
			checkPackage.setEnabled(false);
			checkPackage.setValue(false);
			comboPackage.setEnabled(false);
			comboPackage.setEditable(false);
			comboPackage.setValue(0);
		}
		//La causa es el check de coleccion
		else if (e.getSource().equals(checkCollection)) {
			if((Boolean)checkCollection.getValue() == true){
				comboCollection.setValue(99999999);
				comboCollection.setEnabled(false);
				comboCollection.setEditable(false);				
			}else{
				comboCollection.setEnabled(true);
				comboCollection.setEditable(true);
				comboCollection.setValue(0);
								
				comboPackage.setValue(0);
				checkPackage.setValue(false);
			}
		}
		//La causa es el check de paquete
		else if (e.getSource().equals(checkPackage)) {
			if((Boolean)checkPackage.getValue() == true){
				comboPackage.setValue(99999999);
				comboPackage.setEnabled(false);
				comboPackage.setEditable(false);				
			}else{
				comboPackage.setEnabled(true);
				comboPackage.setEditable(true);
				comboPackage.setValue(0);
			}
		}
		else if(e.getSource() == comboSameDistr)
			setAllDistrib();
		
		//Cancel Button Action
		else if (e.getSource() == bCancel)
			dispose();

		//Execute Search
		else if(e.getSource() == bSearch && (Boolean)checkPOrder.getValue()==false) {
			try {
				KeyNamePair locator = (KeyNamePair)comboLocator.getSelectedItem();
				locatorCD = locator.getKey();
				MWarehouse MWarehouse = Utilities.obtenerWarehouse(locatorCD);
				warehouseCD =MWarehouse.getM_Warehouse_ID();
				orgCD = MWarehouse.getAD_Org_ID();
				loadTableInfo();
			} catch (NullPointerException n) {
				n.printStackTrace();
			}
			is_purchaseorder = false;
		}		
		else if(e.getSource() == bSearch && (Boolean)checkPOrder.getValue()==true) {
			KeyNamePair locator = (KeyNamePair)comboLocator.getSelectedItem();
			locatorCD = locator.getKey();
			MWarehouse MWarehouse = Utilities.obtenerWarehouse(locatorCD);
			warehouseCD =MWarehouse.getM_Warehouse_ID();
			orgCD = MWarehouse.getAD_Org_ID();
			loadTablePOInfo();
			is_purchaseorder = true;
		}
		/**Agregado Proyecto CD Valencia */
		else if(e.getSource() == comboLocator) {
			KeyNamePair locator = (KeyNamePair)comboLocator.getSelectedItem();
			if(locator !=null && locator.getKey() != 0){
				bSearch.setEnabled(true);
			}else {
				bSearch.setEnabled(false);
			}
		}
		
		else {}
	}   //  actionPerformed


	/**************************************************************************************/
	
	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e) {
		
		/** If the selection regards a PO -  Javier Pino´s code stars here */
		int rows = miniTable.getRowCount(), selected_rows = 0,  distributed_rows = 0;
		Double desiredQty = 0.0;
		if (is_purchaseorder) {		
			for (int i = 0 ; i < rows ;  i++) {								
				IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
				if (id != null && id.isSelected()) {
					selected_rows++;					
					desiredQty +=((Number)miniTable.getValueAt(i, 9)).doubleValue();	
					KeyNamePair pair = (KeyNamePair)miniTable.getValueAt(i, 10);
					if (pair != null) 
						distributed_rows++;						
				}
			}
			labelDesiredQty.setText(m_format.format(desiredQty));
			
			if ( (selected_rows > 0) && (distributed_rows == selected_rows))
				bGenerate.setEnabled(true);
			else 
				bGenerate.setEnabled(false);	
			/** Javier Pino's code ends here*/
		} else {
			if(e.getColumn() == indx)
				calculateSelection();
			else if(e.getColumn()== 0)
				calculateSelection();
			else if(e.getColumn()== indx -3)
				calculateSelection();
			else if (e.getColumn() == indx + 1) {				
			}
		} 
		miniTable.repaint();
	}   //  valueChanged
	
	/****************************************************************************************/
	
	
	/**
	 *  Apply Distribution for all rows
	 *  @param
	 *  
	 */
	public void setAllDistrib(){
		miniTable.stopEditor(true);
		
		KeyNamePair distribution = (KeyNamePair)comboSameDistr.getSelectedItem();		
		int rows = miniTable.getRowCount();
		
		/** If the selection regards a PO -  Javier Pino´s code starts here */
		if (is_purchaseorder) {
			if (rows > 0) {
				for (int i = 0 ; i < rows ;  i++) {					
					IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
					if (id.isSelected()) {
						miniTable.setValueAt(distribution, i, 10);						
					}
				}				
			}
			miniTable.repaint();
		/** Javier Pino's code ends here*/
		} else {		
			if(rows > 0){				
				for (int i = 0; i < rows; i++){
					IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);					
					if (id.isSelected()) {
						miniTable.setValueAt(distribution, i, indx + 1);						
					}
					
				}
			}
			miniTable.repaint();
			calculateSelection();
		}
	}
	
	
	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection() {
		
		if(m_sql == null)
			return;
		
		m_noSelected = 0;
		Integer desiredQty = 0;
		boolean noZero = true;
		//Para verificar si el boton es activable
		boolean activable = true;
		int rows = miniTable.getRowCount();		
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				Integer qtyInserted = (Integer) miniTable.getModel().getValueAt(i, indx);
				Integer stockRow = (Integer) miniTable.getModel().getValueAt(i, indx-1);
				Float stock10Per = new Float(stockRow)/10;
				if( qtyInserted == null ||  qtyInserted == 0)
					noZero = false;
				else{
					if(qtyInserted <= stockRow){
						
						/*AGREGADO GHUCHET*/				
				//Si esta seleccionado el check de referencia y no se esta ignorando el multiplo de empaque de la referencia
						if((Boolean)checkReference.getValue()==true){
							Boolean ignorePackMul = (Boolean) miniTable.getModel().getValueAt(i, miniTable.getColumnModel().getColumnIndex("Ignorar Múltiplo de Empaque"));		/*MODIFICADO GHUCHET*/
							if(!ignorePackMul){
								Integer packMul = (Integer) miniTable.getModel().getValueAt(i, miniTable.getColumnModel().getColumnIndex(Msg.translate(ctx, "XX_PackageMultiple")));			/*MODIFICADO GHUCHET*/
								//Si la cantidad deseada no cumple con el multiplo de empaque 	
								if(qtyInserted%packMul !=0){
									ADialog.error(m_WindowNo, this, "La cantidad a distribuir no cumple con el múltipo de empaque.");
									//miniTable.getModel().setValueAt(0, i, indx);
									//id.setSelected(false);
									//Ver si hay inconsistencias y avisar de ellas
									miniTable.setValueAt(new Integer(1), i, miniTable.getColumnModel().getColumnIndex("Inconsistencia")); /*MODIFICADO GHUCHET*/
									activable = false;
								} else {
									miniTable.setValueAt(new Integer(0), i, miniTable.getColumnModel().getColumnIndex("Inconsistencia")); /*MODIFICADO GHUCHET*/
								}
							}else miniTable.setValueAt(new Integer(0), i, miniTable.getColumnModel().getColumnIndex("Inconsistencia")); /*MODIFICADO GHUCHET*/
						}
						/*FIN AGREGADO GHUCHET*/
						if((stockRow - qtyInserted) >= stock10Per ||  (stockRow - qtyInserted) == 0)
							desiredQty = desiredQty + qtyInserted;
						else{
							ADialog.error(m_WindowNo, this, "No puede dejar menos del 10% del inventario sin distribuir.", Msg.translate(Env.getCtx(), "Inventory Redistribution"));
							miniTable.getModel().setValueAt(0, i, indx);
						}	
					}else{
						ADialog.error(m_WindowNo, this, "La cantidad deseada no puede sobrepasar la cantidad en inventario.", Msg.translate(Env.getCtx(), "Inventory Redistribution"));
						miniTable.getModel().setValueAt(0, i, indx);
					}
				}
				m_noSelected++;
			}else if((Boolean)checkReference.getValue()==true) 
				miniTable.setValueAt(new Integer(0), i, miniTable.getColumnModel().getColumnIndex("Inconsistencia")); /*MODIFICADO GHUCHET*/
		}

		//  Information
		
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(m_format.format(desiredQty));
		//info.append(Msg.getMsg(Env.getCtx(), "Remaining")).append(" ").append(m_format.format(remaining));
		dataStatus.setText(info.toString());
		labelDesiredQty.setText(m_format.format(desiredQty));
		//
		bGenerate.setEnabled(m_noSelected != 0 && desiredQty != 0 && noZero && activable);		
		
	}   //  calculateSelection
	
	/**
	 *  Process Selection
	 */
	private void processSelection(boolean group){
	 
		miniTable.stopEditor(true);
		int rows = miniTable.getRowCount();
		
		miniTable.setRowSelectionInterval(0,0);
		
		MVMRDistributionHeader header = new MVMRDistributionHeader(ctx, 0, null);
		
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bran = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, -1);
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int yearmonth;
		
		if(month != 12){
			yearmonth = (year*100) + month;
		}else{
			yearmonth = ((year-1)*100) + month;
		}
		
		int stockQty = 0;
		int desiredQty = 0;
		
		String sql = "";
		
		if(cat != null && cat.getKey() != 0)
			header.setXX_Department_Name("Todos los Departamentos (Categoria "+ cat.getName() +")");
		
		if(dept != null && dept.getKey() != 0)
			header.setXX_Department_Name(dept.getName());
		
		if(line != null && line.getKey() != 0)
			header.setXX_Line_Name(line.getName());
		
		if(sect != null && sect.getKey() != 0)
			header.setXX_Section_Name(sect.getName());
		
		if(bran != null && bran.getKey() != 0)
			header.setXX_Brand_Name(bran.getName());
		
		if(bpar != null && bpar.getKey() != 0)
			header.setXX_BPartner_Name(bpar.getName());
		
		if(lookupProduct.getValue() != null )
			header.setXX_Product_Name("" + lookupProduct.getValue());
		
		if (coll != null) 
			header.setXX_CollectionName(coll.getName());
		
		if (pack != null) {
			header.setXX_PackageName(pack.getName());
		}
				
		header.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE.getValue());		
		header.setXX_HasTextilProducts(true);
		header.setM_Warehouse_ID(warehouseCD);
		header.save();
	
		int how_many_good = 0;
		if(!group){
			//Creating a new Redistribution Header-Detail
			X_XX_VMR_DistributionDetail detail = null;
			for (int i = 0; i < rows; i++) {
				IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
				
				if (id.isSelected()){
		
					detail = new X_XX_VMR_DistributionDetail(ctx, 0, null);				
					detail.setXX_VMR_DistributionHeader_ID(header.getXX_VMR_DistributionHeader_ID());
					 
					sql = 
							" WITH DISPONIBLE AS ( select m_product_id, available from (SELECT M_PRODUCT_ID, sum(available) as available FROM " +
									" (SELECT M_PRODUCT_ID, SUM(QTY) AS AVAILABLE FROM M_STORAGEDETAIL " +
									" WHERE M_LOCATOR_ID = " +locatorCD + " AND QTYTYPE = 'H' " +
									" AND M_AttributeSetInstance_ID>=0 " +
									" AND M_lOCATOR_ID >= 0 " +
									" GROUP BY M_PRODUCT_ID HAVING SUM(QTY) > 0 " +
									" union all " + 
									" SELECT M_PRODUCT_ID, -1*SUM(CANT) AS NOTAVAILABLE FROM " + 
									" (SELECT M_PRODUCT_ID,SUM(XX_DESIREDQUANTITY) AS CANT FROM XX_VMR_DISTRIBDETAILTEMP WHERE M_WAREHOUSE_ID = "+warehouseCD+" GROUP BY M_PRODUCT_ID " +
									" union all " +
									" SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_ORDER P ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE P.XX_ORDERREQUESTSTATUS = 'PE' AND P.AD_Org_ID = "+orgCD+"  GROUP BY M_PRODUCT_ID " +
									" union all " +
									" SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_DistributionHeader H ON (h.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE H.XX_DistributionStatus IN ('QR', 'QT') AND H.M_WAREHOUSE_ID = "+warehouseCD+" GROUP BY M_PRODUCT_ID " +
									" ) GROUP BY M_PRODUCT_ID " +
									" ) GROUP BY M_PRODUCT_ID) where available>0) ";
					
					sql += " SELECT iv.XX_VMR_DEPARTMENT_ID, iv.XX_VMR_LINE_ID, iv.XX_VMR_SECTION_ID, iv.XX_VMR_BRAND_ID," +
						" iv.C_BPARTNER_ID, iv.M_PRODUCT_ID, " +
						//"SUM(iv.QtyOnHand) " +
						" SUM(d.available) " + //MODIFICADO POR GHUCHET - ANTES TOMABA LAS CANTIDADES DEL M_STORAGEDETAIL SIN RESTAR LO QUE ESTABA RESERVADO
						" FROM M_PRODUCT iv, disponible d ";
										
					sql += " WHERE iv.m_product_id=d.m_product_id ";
					
					for(int j = 0; j < miniTable.getColumnCount(); j++){
	
						if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Department_ID"))){
							detail.setXX_VMR_Department_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							sql = sql + " AND iv.XX_VMR_DEPARTMENT_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_VendorProdRef_ID"))){
							detail.setXX_VMR_VendorProdRef_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							sql = sql + " AND iv.XX_VMR_VENDORPRODREF_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();					
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Line_ID"))){
							detail.setXX_VMR_Line_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							sql = sql + " AND iv.XX_VMR_LINE_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();					
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Section_ID"))){
							detail.setXX_VMR_Section_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							sql = sql + " AND iv.XX_VMR_SECTION_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();					
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Brand"))){
							detail.setXX_VMR_Brand_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							sql = sql + " AND iv.XX_VMR_BRAND_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();						
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "C_BPARTNER_ID"))){
							detail.setC_BPartner_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							sql = sql + " AND iv.C_BPARTNER_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "M_PRODUCT_ID"))){
							detail.setM_Product_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							sql = sql + " AND iv.M_PRODUCT_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
						}	
	//COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
	//					else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_Collection"))){
	//						detail.setXX_VMR_Collection_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
	//						sql = sql + " AND iv.COLECCION_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
	//					}
	//					else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "Package"))){
	//						detail.setXX_VMR_Package_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
	//						sql = sql + " AND iv.PAQUETE_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
	//					}
	//HASTA AQUI COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_StockQty"))){
							detail.setXX_StockQuantity((Integer)miniTable.getModel().getValueAt(i, j));
							stockQty = (Integer)miniTable.getModel().getValueAt(i, j);
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_DesiredQty"))){
							detail.setXX_DesiredQuantity((Integer)miniTable.getModel().getValueAt(i, j));
							desiredQty = (Integer)miniTable.getModel().getValueAt(i, j);
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_DistributionMethod"))){
							if (miniTable.getModel().getValueAt(i, j) != null) {
								if (miniTable.getModel().getValueAt(i, j).toString().equals("-"))
									detail.setXX_VMR_DistributionType_ID(0);
								else 
									detail.setXX_VMR_DistributionType_ID(((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey());
							}
							else detail.setXX_VMR_DistributionType_ID(0);
						}
					}								
					/**Fin Javier Pino*/
	// VLOMONACO						
					sql += " GROUP BY iv.XX_VMR_DEPARTMENT_ID, iv.XX_VMR_LINE_ID, iv.XX_VMR_SECTION_ID, iv.XX_VMR_BRAND_ID, " +
							"iv.C_BPARTNER_ID, iv.M_PRODUCT_ID";
					//FIN
					detail.setXX_InitDate(yearmonth);
					detail.save();	
					if ( generateDetailTemp(i+1, header.get_ID(), detail.get_ID(), sql, stockQty, desiredQty)) {
						how_many_good++;
					} else {					
						//No habian productos para el detalle. Eliminarlo	
						
						try {
							String sql_delete = "DELETE FROM XX_VMR_DistributionDetail where XX_VMR_DistributionDetail_ID =" + detail.get_ID();
							DB.executeUpdate(null, sql_delete);
						} catch (Exception e) {
							ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
							e.printStackTrace();
						}
					}				
					m_frame.setVisible(true);
				}			
			}	
		}else {
			//Creating a new Redistribution Header-Detail
			X_XX_VMR_DistributionDetail detail = null;
			for (int i = 0; i < rows; i++) {
				IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
				
				if (id.isSelected()){
		
					if (detail == null){
						detail = new X_XX_VMR_DistributionDetail(ctx, 0, null);		
						detail.setXX_VMR_DistributionHeader_ID(header.getXX_VMR_DistributionHeader_ID());
						detail.setXX_StockQuantity(0);
						detail.setXX_DesiredQuantity(0);
						detail.setXX_InitDate(yearmonth);
						if(dept != null && dept.getKey() != 0 && dept.getKey() != 99999999)
							detail.setXX_VMR_Department_ID(dept.getKey());
						if(line != null && line.getKey() != 0 && line.getKey() != 99999999)
							detail.setXX_VMR_Line_ID(line.getKey());
						if(sect != null && sect.getKey() != 0 && sect.getKey() != 99999999)
							detail.setXX_VMR_Section_ID(sect.getKey());		
						if(bran != null && bran.getKey() != 0 & bran.getKey() != 99999999)
							detail.setXX_VMR_Brand_ID(bran.getKey());	
						if(bpar != null && bpar.getKey() != 0 && bpar.getKey() != 99999999)
							detail.setC_BPartner_ID(bpar.getKey());
//COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
//						if (coll != null) 
//						detail.setXX_VMR_Collection_ID(coll.getKey());
//						if (pack != null) 
//						detail.setXX_VMR_Package_ID(pack.getKey());
//HASTA AQUI COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
						detail.setDescription(" ");
						if(cat != null && cat.getKey() != 0)
							detail.setDescription(detail.getDescription()+" - "+"Todos los Departamentos (Categoria "+ cat.getName() +")");				
						if(dept != null && dept.getKey() != 0)
							detail.setDescription(detail.getDescription()+dept.getName());
						if(line != null && line.getKey() != 0)
							detail.setDescription(detail.getDescription()+" - "+line.getName());	
						if(sect != null && sect.getKey() != 0)
							detail.setDescription(detail.getDescription()+" - "+sect.getName());
						if(bran != null && bran.getKey() != 0)
							detail.setDescription(detail.getDescription()+" - "+bran.getName());
						if(bpar != null && bpar.getKey() != 0)
							detail.setDescription(detail.getDescription()+" - "+bpar.getName());
						if(lookupProduct.getValue() != null )
							detail.setDescription(detail.getDescription()+" - "+ lookupProduct.getValue());
						if (coll != null) 
							detail.setDescription(detail.getDescription()+" - "+coll.getName());
						if (pack != null) {
							detail.setDescription(detail.getDescription()+" - "+pack.getName());
						}		
						detail.save();
					}
					 
					sql = 
							" WITH DISPONIBLE AS ( select m_product_id, available from (SELECT M_PRODUCT_ID, sum(available) as available FROM " +
									" (SELECT M_PRODUCT_ID, SUM(QTY) AS AVAILABLE FROM M_STORAGEDETAIL " +
									" WHERE M_LOCATOR_ID = " +locatorCD + " AND QTYTYPE = 'H' " +
									" AND M_AttributeSetInstance_ID>=0 " +
									" AND M_lOCATOR_ID >= 0 " +
									" GROUP BY M_PRODUCT_ID HAVING SUM(QTY) > 0 " +
									" union all " + 
									" SELECT M_PRODUCT_ID, -1*SUM(CANT) AS NOTAVAILABLE FROM " + 
									" (SELECT M_PRODUCT_ID,SUM(XX_DESIREDQUANTITY) AS CANT FROM XX_VMR_DISTRIBDETAILTEMP WHERE M_WAREHOUSE_ID = "+warehouseCD+" GROUP BY M_PRODUCT_ID " +
									" union all " +
									" SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_ORDER P ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE P.XX_ORDERREQUESTSTATUS = 'PE' AND P.AD_Org_ID = "+orgCD+"  GROUP BY M_PRODUCT_ID " +
									" union all " +
									" SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) AS CANT from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_DistributionHeader H ON (h.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE H.XX_DistributionStatus IN ('QR', 'QT') AND H.M_WAREHOUSE_ID = "+warehouseCD+" GROUP BY M_PRODUCT_ID " +
									" ) GROUP BY M_PRODUCT_ID " +
									" ) GROUP BY M_PRODUCT_ID) where available>0) ";
					
					sql += " SELECT iv.XX_VMR_DEPARTMENT_ID, iv.XX_VMR_LINE_ID, iv.XX_VMR_SECTION_ID, iv.XX_VMR_BRAND_ID," +
						" iv.C_BPARTNER_ID, iv.M_PRODUCT_ID, " +
						" SUM(d.available) " + //MODIFICADO POR GHUCHET - ANTES TOMABA LAS CANTIDADES DEL M_STORAGEDETAIL SIN RESTAR LO QUE ESTABA RESERVADO
						" FROM M_PRODUCT iv, disponible d ";
										
					sql += " WHERE iv.m_product_id=d.m_product_id ";
					
					for(int j = 0; j < miniTable.getColumnCount(); j++){

						if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Department_ID"))){
							sql = sql + " AND iv.XX_VMR_DEPARTMENT_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_VendorProdRef_ID"))){
							sql = sql + " AND iv.XX_VMR_VENDORPRODREF_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();					
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Line_ID"))){			
							sql = sql + " AND iv.XX_VMR_LINE_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();					
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Section_ID"))){			
							sql = sql + " AND iv.XX_VMR_SECTION_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();					
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Brand"))){							
							sql = sql + " AND iv.XX_VMR_BRAND_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();						
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "C_BPARTNER_ID"))){
							sql = sql + " AND iv.C_BPARTNER_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "M_PRODUCT_ID"))){
							sql = sql + " AND iv.M_PRODUCT_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
						}					
//COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
//						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_Collection"))){			
//							sql = sql + " AND iv.COLECCION_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
//						}
//						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "Package"))){	
//							sql = sql + " AND iv.PAQUETE_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(i, j)).getKey();
//						}
//HASTA AQUI COMENTADO POR GHUCHET HASTA QUE SE ARREGLE LA PARTE DE COLECCION Y PAQUETE
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_StockQty"))){
							detail.setXX_StockQuantity(detail.getXX_StockQuantity()+(Integer)miniTable.getModel().getValueAt(i, j));
							stockQty = (Integer)miniTable.getModel().getValueAt(i, j);
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_DesiredQty"))){
							detail.setXX_DesiredQuantity(detail.getXX_DesiredQuantity()+(Integer)miniTable.getModel().getValueAt(i, j));
							desiredQty = (Integer)miniTable.getModel().getValueAt(i, j);
						}
					}								
					sql += " GROUP BY iv.XX_VMR_DEPARTMENT_ID, iv.XX_VMR_LINE_ID, iv.XX_VMR_SECTION_ID, iv.XX_VMR_BRAND_ID, " +
							"iv.C_BPARTNER_ID, iv.M_PRODUCT_ID";

					detail.save();	
					if ( generateDetailTemp(i+1, header.get_ID(), detail.get_ID(), sql, stockQty, desiredQty)) {
						how_many_good++;
					} else {					
						//No habian productos para el detalle. Eliminarlo	
						/*try {
							String sql_delete = "DELETE FROM XX_VMR_DistributionDetail where XX_VMR_DistributionDetail_ID =" + detail.get_ID();
							DB.executeUpdate(null, sql_delete);
						} catch (Exception e) {
							ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
							e.printStackTrace();
						}*/
					}				
					m_frame.setVisible(true);
				}			
			}
		}
		//Jorge Pires Cambiar condicion! Si todos estan bien 
		if (how_many_good > 0) {
			//Si al menos se creo un detalle
			//if ( trx.commit() ){				
				String mss = Msg.getMsg(Env.getCtx(), "XX_LongHeaderCreated", 
						new String[] { "<" + header.get_ID() + ">"
					
				});
				ADialog.info(m_WindowNo, m_frame, mss);
			//}
		} else {
			//Si no se pudo crear el detalle			
			
			setCursor(Cursor.getDefaultCursor());
			
			//Debe borrarse la cabecera creada si no tiene detalles
			String sql_delete_header = "DELETE FROM XX_VMR_DISTRIBUTIONHEADER " +
					" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID();
			
		    try {
		    	DB.executeUpdate(null, sql_delete_header);
		    } catch(Exception e)
		    {
		    	e.printStackTrace();
		    }

			ADialog.info(m_WindowNo, m_frame, "XX_NoHeaderCreated");
		}
				
		checkProduct.setValue(false);
		lookupProduct.setValue(null);
		lookupProduct.setEnabled(true);
		checkReference.setValue(false);
	}   //  processSelection()
		
	/**
	 * @author Javier Pino (Computacion 6) 
	 *  Process PO Selection  
	 *  */
	private void processPOSelection() {
		int rows = miniTable.getRowCount(), selected_rows = 0;
		Vector<String> purchase_orders = new Vector<String>();
		Vector<Integer> all_purchase_orders = new Vector<Integer>();				
		if (is_purchaseorder) { 
			if (rows > 0) {
				
				
				for (int i = 0 ; i < rows ;  i++) {	
					IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);
					if (id.isSelected()) {	
						selected_rows++;
						KeyNamePair order = (KeyNamePair)miniTable.getValueAt(i, 1);
						KeyNamePair dist = (KeyNamePair)miniTable.getValueAt(i, 10);	
						
						if (all_purchase_orders.contains(order.getKey())) {
							//Ya fue agregada y creada con sus asociadas							
							continue;							
						}						
						Vector<Integer> ordenes_predistribuidas = new Vector<Integer>();
						Vector<Integer> ordenes_centrodist = new Vector<Integer>();
						Vector<MOrder> asociadas = XX_BatchNumberInfo.associatedPurchaseOrders ( order.getKey(),	m_WindowNo, m_frame );
						boolean error_asociadasCD = false, error_predistribuidas = false;
						for (int j = 0; j < asociadas.size() ; j++) {
							MOrder order_aj = asociadas.get(j);							
							if (order_aj.getXX_VLO_TypeDelivery().equals("CD")) {
								if (order_aj.getXX_OrderStatus().equals("CH")) {
									ordenes_centrodist.add(asociadas.get(j).getC_Order_ID());
								} else {
									String mss = Msg.getMsg(Env.getCtx(), "XX_UncheckPO", 
											new String[] {order.getName()});
									ADialog.error(m_WindowNo, m_frame, mss);
									error_asociadasCD = true;									
									break;								
								}
							} else { //Predistribuidas

								if (order_aj.isActive() && order_aj.getXX_OrderType().equals("Nacional") && 
										
										!order_aj.getXX_OrderStatus().equals("AN") && !order_aj.getXX_OrderStatus().equals("PRO")) {
									ordenes_predistribuidas.add(asociadas.get(j).getC_Order_ID());
								} else if ( order_aj.isActive() && order_aj.getXX_OrderType().equals("Importada") &&
										!order_aj.getXX_OrderStatus().equals("PRO") && !order_aj.getXX_OrderStatus().equals("AN") ) {									
									ordenes_predistribuidas.add(asociadas.get(j).getC_Order_ID());
								}  else {
									String mss = Msg.getMsg(Env.getCtx(), "XX_NotApprovedPO", 
											new String[] {
										order.getName()
										});
									ADialog.error(m_WindowNo, m_frame, mss);
									error_asociadasCD = true;
									break;
								}										
							}
						}
						
						if (error_asociadasCD || error_predistribuidas) {							
							continue; //Se ignora la orden de compra							
						}
						int grupo_no = 0;
						if (ordenes_predistribuidas.size() > 0 || ordenes_centrodist.size() > 0) {
							String mss = Msg.getMsg(Env.getCtx(), "XX_AssociatedOrders", 
									new String[] {
								order.getName()
								});
							ADialog.info(m_WindowNo,m_frame, mss);
							
							//CREAR GRUPO
							String sql_grupo = " SELECT COALESCE ( MAX(XX_ASSOCIATIONNUMBER), 0) FROM XX_VMR_HEADERASSOCIATION  ";							
							PreparedStatement ps = DB.prepareStatement(sql_grupo, null);
							try {
								ResultSet rs = ps.executeQuery();
								if (rs.next()) {									
									grupo_no = rs.getInt(1) + 1 ;
								}
								rs.close();
								ps.close();
							} catch (SQLException e) {
								ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
								e.printStackTrace();								
							}
						}									
												
						int header_id_asociado = 0;
						Trx trans = Trx.get("XX_CREATEDISTHEADER");
						
						if (ordenes_centrodist.size() > 0) {
							for (int j = 0; j < ordenes_centrodist.size() ; j++ ) {								
								MOrder orden_aj = new MOrder(Env.getCtx(), ordenes_centrodist.get(j), null);
								int producto_inters = 0;
								String sql_product = " SELECT M_PRODUCT_ID FROM XX_VMR_DISTRIBDETAILTEMP T WHERE M_WAREHOUSE_ID = "+warehouseCD;
								sql_product = 
									MRole.getDefault().addAccessSQL(sql_product, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
								String sql = " SELECT REC.M_PRODUCT_ID FROM M_INOUTLINE REC " +
										" INNER JOIN C_ORDERLINE LIN ON (REC.C_ORDERLINE_ID = LIN.C_ORDERLINE_ID) " +
										" INNER JOIN XX_VMR_PO_LINEREFPROV REF ON (LIN.XX_VMR_PO_LINEREFPROV_ID = REF.XX_VMR_PO_LINEREFPROV_ID) " +
										" WHERE REC.M_LOCATOR_ID = " + locatorCD +
										" AND REF.C_ORDER_ID = " + orden_aj.get_ID() + 
										" AND REC.QTYENTERED > 0 ";
								sql += " INTERSECT " + sql_product;								
								PreparedStatement ps = DB.prepareStatement(sql, null);
								try {
									ResultSet rs = ps.executeQuery();
									if (rs.next()) {
										producto_inters = rs.getInt(1);
									}
									rs.close();
									ps.close();									
								} catch (SQLException e) {
									ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
									e.printStackTrace();
								}
								if (producto_inters > 0) {
									MProduct producto = new MProduct(Env.getCtx(), producto_inters, null);
									String mss = Msg.getMsg(Env.getCtx(), "XX_NotApprovedDistributedProduct", 
											new String[] {
										"" + producto.get_ID(), producto.getName(), order.getName()
										});									
									ADialog.error(m_WindowNo, m_frame, mss );
									continue;									
								}		
							}
							
							//Crear una cabecera de distribution
							MVMRDistributionHeader header = 
								new MVMRDistributionHeader(Env.getCtx(), 0, trans);
							StringBuilder cd_po = new StringBuilder();
							cd_po.append("[");
							for (int j = 0; j < ordenes_centrodist.size() - 1 ; j++ ) {								
								MOrder orden_aj = new MOrder(Env.getCtx(), ordenes_centrodist.get(j), null);
								cd_po.append(orden_aj.getDocumentNo());
								cd_po.append(", ");								
							} 
							MOrder orden_af = 
								new MOrder(Env.getCtx(), 
										ordenes_centrodist.get(ordenes_centrodist.size() - 1), null);
							cd_po.append(orden_af.getDocumentNo());
							cd_po.append("]");
							String mss = Msg.getMsg(Env.getCtx(), "XX_DistribHeaderOrders", 
									new String[] {cd_po.toString()});
							header.setDescription(mss);
							header.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE.getValue());
							header.setM_Warehouse_ID(warehouseCD);
							header.save();
							
							for (int j = 0; j < ordenes_centrodist.size() ; j++ ) {								
								MOrder orden_aj = new MOrder(Env.getCtx(), ordenes_centrodist.get(j), null);																
								if ( artificialDetailTemp(header.get_ID(), dist.getKey(), 1, orden_aj, null ,trans)) {									
								} else {									
									trans.rollback();
									return;									
								}
							}

							//Se asocia la cabecera
							X_XX_VMR_HeaderAssociation asociado = 
								new X_XX_VMR_HeaderAssociation(Env.getCtx(),0, trans);
							asociado.setXX_AssociationNumber(grupo_no);
							asociado.setXX_VMR_DistributionHeader_ID(header.get_ID());
							asociado.save();
						}
						
						//Para cada una de las predistribuidas
						for (int j = 0; j < ordenes_predistribuidas.size(); j++) {
							MOrder orden_aj = new MOrder(Env.getCtx(), ordenes_predistribuidas.get(j), null);
							all_purchase_orders.add(orden_aj.get_ID());	
							purchase_orders.add(orden_aj.getDocumentNo());
							header_id_asociado = 
								XX_CreateDistributionHeader.create(orden_aj.get_ID(),
										dist.getKey(), trans, warehouseCD);
							
							//Se asocia la cabecera
							X_XX_VMR_HeaderAssociation asociado = 
								new X_XX_VMR_HeaderAssociation(Env.getCtx(),0, trans);
							asociado.setXX_AssociationNumber(grupo_no);
							asociado.setXX_VMR_DistributionHeader_ID(header_id_asociado);
							asociado.save();							
						}						
						header_id_asociado = 
							XX_CreateDistributionHeader.create(order.getKey(), dist.getKey(), trans, warehouseCD);
						all_purchase_orders.add(order.getKey());
						purchase_orders.add(order.getName());
						
						if (ordenes_centrodist.size() > 0 || ordenes_predistribuidas.size() > 0) {
							//Se asocia la cabecera
							X_XX_VMR_HeaderAssociation asociado = 
								new X_XX_VMR_HeaderAssociation(Env.getCtx(),0, trans);
							asociado.setXX_AssociationNumber(grupo_no);
							asociado.setXX_VMR_DistributionHeader_ID(header_id_asociado);
							asociado.save();													
						}
						trans.commit();
					}
				}			
				if (all_purchase_orders.size() > 0) {
					String mss = Msg.getMsg(Env.getCtx(), "XX_HeaderCreated", 
							//Order					
							new String[] {purchase_orders.toString()});
					ADialog.info(m_WindowNo, m_frame, mss);				
				}
				
			}
		}
	}
	
	public boolean artificialDetailTemp(int header_id, int dist_id,
			float percent,MOrder orden_aj, Hashtable<Integer, Boolean> prodincluidos , Trx trx){
			
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, -1);
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int yearmonth;		
		if(month != 12){
			yearmonth = (year*100) + month;
		}else{
			yearmonth = ((year-1)*100) + month;
		}
		try {
			String sql = " SELECT " +
			" PRO.XX_VMR_DEPARTMENT_ID, PRO.XX_VMR_LINE_ID, " +
			" PRO.XX_VMR_SECTION_ID, PRO.XX_VMR_BRAND_ID, PRO.C_BPARTNER_ID, " +										
			" REC.M_PRODUCT_ID, REC.MOVEMENTQTY FROM M_INOUTLINE REC " +
			" INNER JOIN C_ORDERLINE LIN ON (REC.C_ORDERLINE_ID = LIN.C_ORDERLINE_ID) " +
			" INNER JOIN XX_VMR_PO_LINEREFPROV REF ON " +
			" (LIN.XX_VMR_PO_LINEREFPROV_ID = REF.XX_VMR_PO_LINEREFPROV_ID) " +
			" INNER JOIN M_PRODUCT PRO ON (REC.M_PRODUCT_ID = PRO.M_PRODUCT_ID) " +  
			" WHERE REC.M_LOCATOR_ID = " +locatorCD+
			" AND REF.C_ORDER_ID = " + orden_aj.get_ID() + 
			" AND REC.MOVEMENTQTY > 0 ";								
			
			X_XX_VMR_DistributionDetail detail =
				new X_XX_VMR_DistributionDetail(ctx, 0, trx);				
			detail.setXX_VMR_DistributionHeader_ID(header_id);
			detail.setXX_VMR_DistributionType_ID(dist_id);
			
			String mss = Msg.getMsg(Env.getCtx(), "XX_DistribDetailOrder", 
						new String[] {orden_aj.getDocumentNo()});	
			detail.setDescription( Msg.getMsg(Env.getCtx(), mss) );
			detail.setXX_VMR_Department_ID(orden_aj.getXX_VMR_DEPARTMENT_ID());				
			detail.setXX_InitDate(yearmonth);			
			detail.save();
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);			
			ResultSet rs = pstmt.executeQuery();
			int suma = 0;
			int incluidos = 0, ignorados = 0;			
			while(rs.next()) {				
				if (prodincluidos != null) {
					if (prodincluidos.containsKey(rs.getInt(6))) {
						ignorados++;
						continue;
					} else {
						prodincluidos.put(rs.getInt(6), true);
					}					
				}
				X_XX_VMR_DistribDetailTemp detailTemp = 
					new X_XX_VMR_DistribDetailTemp(ctx, 0, trx);	
				
				detailTemp.setXX_VMR_DistributionDetail_ID(detail.get_ID());
				detailTemp.setXX_VMR_Department_ID(rs.getInt(1));
				detailTemp.setXX_VMR_Line_ID(rs.getInt(2));
				detailTemp.setXX_VMR_Section_ID(rs.getInt(3));
				detailTemp.setXX_VMR_Brand_ID(rs.getInt(4));
				detailTemp.setC_BPartner_ID(rs.getInt(5));
				detailTemp.setM_Product_ID(rs.getInt(6));									
				detailTemp.setXX_REGISTERSTATUS(new BigDecimal(1));
				detailTemp.setM_Warehouse_ID(warehouseCD);  //Agregado Proyecto CD Valencia
				
				int required = Math.round(percent*rs.getInt(7));	
				suma += required;
				if (required > 0) {
					detailTemp.setXX_DesiredQuantity(new BigDecimal(required));
					detailTemp.save();
				} else {
					ignorados++;
					continue;
				}
				incluidos++;
			}
			rs.close();
			pstmt.close();
			if (incluidos > 0) {
				detail.load(trx);
				detail.setXX_DesiredQuantity(suma);
				detail.save();				
			}
		}
		catch (Exception e) {
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	public boolean generateDetailTemp(int linea, int header, int detail, String sql, float stockQty, float desiredQty){
		
		float percent = desiredQty/stockQty;
			
		try {				
			Hashtable<Integer, MOrder> ordenes_asociadas = new Hashtable<Integer,MOrder>();
			Hashtable<Integer, Boolean> productos_y_sugeridos = new Hashtable<Integer, Boolean>();
			PreparedStatement pstmt = DB.prepareStatement(sql, 
					ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_READ_ONLY, null );			
			ResultSet rs = pstmt.executeQuery();			
			int distribuidos = 0, ignorados = 0;			
			while(rs.next()) {
				distribuidos++; 
						
				int qtydes = Math.round(percent*rs.getInt(7));
				if (qtydes == 0) {
					ignorados++;
					continue;
				}
				//Agregar el producto que no fue ignorado
				productos_y_sugeridos.put(rs.getInt(6), true);
				
				//Obtener las ordenes de compra donde fue adquirido y que estan asociadas
				XX_BatchNumberInfo info = new XX_BatchNumberInfo();
				Vector<Integer> oc_asociadas = 
					info.hasAssociatedPOProduct(rs.getInt(6), qtydes, m_WindowNo, m_frame, warehouseCD);
			
				//Para  cada una de las ordenes de compra donde se compro que estan asociadas a otras
				for (int i = 0; i < oc_asociadas.size(); i++) {
					Vector<MOrder> associated_po = 
						XX_BatchNumberInfo.associatedPurchaseOrders(oc_asociadas.get(i), m_WindowNo,m_frame);
					
					
					//Todas las ordenes de compra asociadas a las ordenes de compra donde se compro
					for (int j = 0; j < associated_po.size() ; j++) {
						ordenes_asociadas.put(associated_po.get(j).get_ID(), associated_po.get(j));
					} 					
				}							
			}
			if (!ordenes_asociadas.isEmpty()) { //Los productos tienen ordenes de compra asociadas
				
				//Verificar el estado de las ordenes de compra asociadas
				StringBuilder str = new StringBuilder();
				for (Entry<Integer, MOrder> entry : ordenes_asociadas.entrySet()) {

					if (!entry.getValue().getXX_OrderStatus().equals("CH")) {
						String mss = Msg.getMsg(Env.getCtx(), "XX_NotCheckedPOError", 
								new String[] {
									entry.getValue().getDocumentNo() 
									+ "-" + 
									entry.getValue().getXX_OrderStatus()});
						ADialog.ask(m_WindowNo, m_frame, mss);
						return false;
					}					
					str.append("\t");
					str.append(entry.getValue().getDocumentNo() + "-" + entry.getValue().getXX_OrderStatus());
					str.append("\n");
				}				
				
				//Preguntarle al usuario que desea hacer respecto a las sociadas
				DecimalFormat formato = new DecimalFormat("#,##0.00");
				String mss = Msg.getMsg(Env.getCtx(), "XX_IncludedPO", 
						new String[] {"" + linea , formato.format(percent*100), "\n" + str.toString()});
				boolean associate = ADialog.ask(m_WindowNo, m_frame, mss);
				if (!associate)
					return false;
								
				//Traer los productos de las ordenes de compra asociadas en el procentaje
				for (Entry<Integer, MOrder> entry : ordenes_asociadas.entrySet()) {

					//Creating a new Redistribution Header-Detail with Purchase Order Info
					if ( ! artificialDetailTemp(header, 0, percent, entry.getValue(), 
							productos_y_sugeridos,  null)) 
						return false;
				}
				
				//Agregar los productos que no fueron desechados
				//rs = pstmt.executeQuery();
				rs.beforeFirst();
				while(rs.next()) {
					if (!productos_y_sugeridos.containsKey(rs.getInt(6))) {
						continue;
					}
				
					X_XX_VMR_DistribDetailTemp detailTemp = new X_XX_VMR_DistribDetailTemp(ctx, 0, null);				
					detailTemp.setXX_VMR_DistributionDetail_ID(detail);
					detailTemp.setXX_VMR_Department_ID(rs.getInt(1));
					detailTemp.setXX_VMR_Line_ID(rs.getInt(2));
					detailTemp.setXX_VMR_Section_ID(rs.getInt(3));
					detailTemp.setXX_VMR_Brand_ID(rs.getInt(4));
					detailTemp.setC_BPartner_ID(rs.getInt(5));
					detailTemp.setM_Product_ID(rs.getInt(6));				
					detailTemp.setXX_REGISTERSTATUS(new BigDecimal(1));
					detailTemp.setM_Warehouse_ID(warehouseCD);  //Agregado Proyecto CD Valencia
					
					int qtydes = Math.round(percent*rs.getInt(7));
					if ( qtydes > 0 ) {
						detailTemp.setXX_DesiredQuantity(new BigDecimal (qtydes));
						detailTemp.save();
					}
				}
				rs.close();
				pstmt.close();
				
				//CREAR GRUPO
				int grupo_no = 0;
				String sql_grupo = " SELECT COALESCE ( MAX(XX_ASSOCIATIONNUMBER), 0) FROM XX_VMR_HEADERASSOCIATION ";				
				
				PreparedStatement ps = DB.prepareStatement(sql_grupo, null);
				try {
					ResultSet rs1 = ps.executeQuery();
					if (rs1.next()) {									
						grupo_no = rs1.getInt(1) + 1 ;
					}
					rs1.close();
					ps.close();
				} catch (SQLException e) {
					ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
					e.printStackTrace();
				}
				if (grupo_no == 0) 
					grupo_no = 1;
				
				X_XX_VMR_HeaderAssociation ass = 
					new X_XX_VMR_HeaderAssociation(ctx, 0, null);
				ass.setXX_VMR_DistributionHeader_ID(header);
				ass.setXX_AssociationNumber(grupo_no);
				ass.save();
				return true;
			} else {				
				//Curso normal		
				if ((distribuidos - ignorados) == 0) {
					ADialog.error(m_WindowNo, m_frame, "XX_AllProductsIgnored");
					rs.close();
					pstmt.close();
					return false;
				}
				//rs = pstmt.executeQuery();
				rs.beforeFirst();
				while(rs.next()) {
					if (!productos_y_sugeridos.containsKey(rs.getInt(6))) {
						continue;
					}					
					X_XX_VMR_DistribDetailTemp detailTemp = new X_XX_VMR_DistribDetailTemp(ctx, 0, null);				
					detailTemp.setXX_VMR_DistributionDetail_ID(detail);
					detailTemp.setXX_VMR_Department_ID(rs.getInt(1));
					detailTemp.setXX_VMR_Line_ID(rs.getInt(2));
					detailTemp.setXX_VMR_Section_ID(rs.getInt(3));
					detailTemp.setXX_VMR_Brand_ID(rs.getInt(4));
					detailTemp.setC_BPartner_ID(rs.getInt(5));
					detailTemp.setM_Product_ID(rs.getInt(6));				
					detailTemp.setXX_REGISTERSTATUS(new BigDecimal(1));
					detailTemp.setM_Warehouse_ID(warehouseCD);  //Agregado Proyecto CD Valencia
					
					int qtydes = Math.round(percent*rs.getInt(7));
					if ( qtydes > 0 ) {
						detailTemp.setXX_DesiredQuantity(new BigDecimal (qtydes));
						detailTemp.save();
					}
				}
				rs.close();
				pstmt.close();
			}
		} catch (SQLException e)
		{
			ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
			//e.printStackTrace();
			return false;
		}
		catch (Exception F)
		{
			//F.printStackTrace();			
			return false;
		}
		return true;		
	}
	
	public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
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

	
	public class ComboBoxEditor extends DefaultCellEditor {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ComboBoxEditor(Vector<KeyNamePair> items) {
            super(new JComboBox(items));
        }
    }
	
	/**************************************************************************
	 * List Selection Listener - get Info and fill xMatchedTo
	 * @author ghuchet
	 * @param e event
	 */
	public void valueChanged(ListSelectionEvent e) {
		if((Boolean)checkInventoryFails.getValue()==true){
			if (e.getValueIsAdjusting())
				return;
			int matchedRow = miniTable.getSelectedRow();
			loadDetail(matchedRow);
		}
	}

	/**Carga el detalle de la fila seleccionada
	 * @author ghuchet
	 * */
	private void loadDetail(int matchedRow) {
		
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		miniTable.repaint();

		if (matchedRow != -1) {
			
			String from = "\nFROM  M_WAREHOUSE W " +
					"\nLEFT JOIN XX_ESTIMATEDSALES ES ON (W.M_WAREHOUSE_ID = ES.M_WAREHOUSE_ID ";

			
			String where = "\nWHERE  W.XX_ISSTORE = 'Y' ";
			
			for(int j = 0; j < miniTable.getColumnCount(); j++){

				if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Department_ID"))){
					from = from + "\nAND ES.XX_VMR_DEPARTMENT_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(matchedRow, j)).getKey();
				}
				else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_VendorProdRef_ID"))){
					from = from + "\nAND ES.XX_VMR_VENDORPRODREF_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(matchedRow, j)).getKey();					
				}
				else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Line_ID"))){
					from = from + "\nAND ES.XX_VMR_LINE_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(matchedRow, j)).getKey();					
				}
				else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Section_ID"))){
					from = from + "\nAND ES.XX_VMR_SECTION_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(matchedRow, j)).getKey();					
				}
				else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Brand"))){
					from = from + "\nAND ES.XX_VMR_BRAND_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(matchedRow, j)).getKey();						
				}
				else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "C_BPARTNER_ID"))){
					from = from + "\nAND ES.C_BPARTNER_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(matchedRow, j)).getKey();
				}
				else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "M_PRODUCT_ID"))){
					from = from + "\nAND ES.M_PRODUCT_ID = " + ((KeyNamePair)miniTable.getModel().getValueAt(matchedRow, j)).getKey();
				}		
			}			
			
			String select = "\nSELECT /*+ NO_CPU_COSTING */ W.VALUE||'-'||W.NAME, NVL(SUM(ES.ESTIMATEDSALES)+0.5,0) , " +
			"\n round(NVL(SUM(ES.REPOSICION),0)), " +
			"\nNVL(SUM(ES.QTYSTORE+ES.QTYTRANSIT),0), NVL(SUM(ES.REALSALES),0), " +
			"\nCASE WHEN round(NVL(SUM(ES.REPOSICION),0)) > -1  THEN  0  ELSE 1 END, "+
			"\nCASE WHEN round(NVL(SUM(ES.REPOSICIONNEGATIVOS),0)) > -1  THEN 0 ELSE 1 END ";
			
			XX_IndicatorCellRenderer renderer;
			renderer = new XX_IndicatorCellRenderer(-2,1);
		    renderer.setStringPainted(false);
		    renderer.setBackground(Color.getHSBColor( 0.333f, 1.000f, 0.7f ));
			 // set limit value and fill color
		    Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();  
		    limitColors.put(0, Color.getHSBColor(0.000f, 1.000f, 0.87f));
		    limitColors.put(-1, Color.getHSBColor( 0.333f, 1.000f, 0.7f )); //GREEN
		 
		    renderer.setLimits(limitColors);        
		  
		    miniTableDetail.getColumnModel().getColumn(5).setCellRenderer(renderer);
			miniTableDetail.getColumnModel().getColumn(5).setMaxWidth(82);
			
			
			XX_IndicatorCellRenderer rendererNeg;
			rendererNeg = new XX_IndicatorCellRenderer(-2,1);
		    rendererNeg.setStringPainted(false);
		    rendererNeg.setBackground(Color.getHSBColor( 0.333f, 1.000f, 0.7f ));
			 // set limit value and fill color
		    Hashtable<Integer, Color> limitColorsNeg = new Hashtable<Integer, Color>();  
		  //  limitColors.put((int)inventoryQty,  Color.RED);
		    limitColorsNeg.put(0, Color.getHSBColor(0.000f, 1.000f, 0.87f));
		    limitColorsNeg.put(-1, Color.getHSBColor( 0.333f, 1.000f, 0.7f )); //GREEN
		 
		    rendererNeg.setLimits(limitColorsNeg);        
		  
		    miniTableDetail.getColumnModel().getColumn(6).setCellRenderer(rendererNeg);
			miniTableDetail.getColumnModel().getColumn(6).setMaxWidth(82);

			
			String sql = select + from + ")" + where;
			sql += "\nGROUP BY W.VALUE||'-'||W.NAME" ; 
			sql += "\nORDER BY W.VALUE||'-'||W.NAME";
			System.out.println(sql);
		
			int row = 0;
			miniTableDetail.setRowCount(row);
			PreparedStatement ps = DB.prepareStatement(sql, null);
			ResultSet rs = null;
			try {
				rs = ps.executeQuery();				
				while (rs.next()) {
					miniTableDetail.setRowCount(row + 1);
					miniTableDetail.setValueAt(rs.getString(1), row, 0);
					miniTableDetail.setValueAt(rs.getInt(2), row, 1);
					miniTableDetail.setValueAt(rs.getInt(3), row, 2);
					miniTableDetail.setValueAt(rs.getInt(4), row, 3);
					miniTableDetail.setValueAt(rs.getInt(5), row, 4);
					miniTableDetail.setValueAt(rs.getInt(6), row, 5);
					miniTableDetail.setValueAt(rs.getInt(7), row, 6);
					row++;
				}
				rs.close();
				ps.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				ADialog.error(m_WindowNo, m_frame, "XX_DatabaseAccessError");
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
		}		
	
		m_frame.setCursor(Cursor.getDefaultCursor());
		miniTableDetail.autoSize(true);
		miniTableDetail.repaint();
		dataPaneDetail.repaint();
		m_frame.setCursor(Cursor.getDefaultCursor());
	}
}


