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
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VMR_Brand;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DistribDetailTemp;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.X_XX_VMR_HeaderAssociation;
import compiere.model.cds.X_XX_VMR_Package;

import compiere.model.cds.processes.XX_BatchNumberInfo;
import compiere.model.cds.processes.XX_CreateDistributionHeader;


public class XX_NotAvailableDistribForm extends CPanel 
	implements FormPanel, ActionListener, TableModelListener {
	
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
		
	
	//REFERENCE
	private VCheckBox checkReference = new VCheckBox();
	private CLabel labelReference = new CLabel();
	
	//QUANTITIES
	private CLabel labelStockQtyName = new CLabel();
	private CLabel labelStockQty = new CLabel();
	
	private CLabel labelDesiredQty = new CLabel();
	//BUTTONS
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bGenerate = ConfirmPanel.createProcessButton(true);	
	private CButton bReset = new CButton(Msg.translate(Env.getCtx(), "XX_ClearFilter"));
	private CButton bSearch = new CButton(Msg.translate(Env.getCtx(), "XX_Search"));
	
	//private JButton bSameDistrib = new JButton();
	//OTHERS
	private VComboBox comboSameDistr = new VComboBox();;
	private JLabel dataStatus = new JLabel();
	private int indx = 0;

	private Vector<KeyNamePair> allDistrib = new Vector<KeyNamePair>();
	
	
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
		labelReference.setText(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"));
		labelCollection.setText(Msg.translate(Env.getCtx(), "XX_Collection"));
	    labelPackage.setText(Msg.translate(Env.getCtx(), "Package"));	
		labelStockQtyName.setText(Msg.getMsg(Env.getCtx(), "XX_InventoryQty") + ":");
		labelStockQty.setText("0");
		labelDesiredQty.setText("0");
		
		
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
		
		//PANEL DERECHO
		parameterPanel.add(labelReference,  new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(checkReference,  new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					
		parameterPanel.add(bReset,    new GridBagConstraints(7, 3, 2, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(bSearch,    new GridBagConstraints(7, 4, 2, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		miniTable.setRowHeight(miniTable.getRowHeight() + 2);
		
		
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		
		dataPane.setPreferredSize(new Dimension(1280, 600));

		//PANEL INFERIOR
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);

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
		checkCollection.setValue(false);
		checkPackage.setEnabled(false);
		checkReference.setEnabled(true);
		
		
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
		
		//Cargar las colecciones
		loadCollections();
		
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
	/**
	 *  Query and generate TableInfo according with the user specifications
	 */
	private void loadTableInfo(){
		
		
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		log.config("");
		//  not yet initialized
		dataPane.getViewport().remove(miniTable);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable, null);

		miniTable.getModel().addTableModelListener(this);
		
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

		
		KeyNamePair cat = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
		KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
		KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
		KeyNamePair bran = (KeyNamePair)comboBrand.getSelectedItem();
		KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
		KeyNamePair coll = (KeyNamePair)comboCollection.getSelectedItem();
		KeyNamePair pack = (KeyNamePair)comboPackage.getSelectedItem();
		

		ColumnInfo colCat = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), "cat.VALUE||'-'||cat.Name", KeyNamePair.class, true, false, "cat.XX_VMR_CATEGORY_ID");
		ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"), "dp.VALUE||'-'||dp.Name", KeyNamePair.class, true, false, "dp.XX_VMR_DEPARTMENT_ID");
		ColumnInfo colLine = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Line_ID"), "li.VALUE||'-'||li.NAME", KeyNamePair.class, true, false, "li.XX_VMR_LINE_ID");
		ColumnInfo colSect = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Section_ID"), "se.VALUE||'-'||se.NAME", KeyNamePair.class, true, false, "se.XX_VMR_SECTION_ID");
		ColumnInfo colBran = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Brand"), "br.VALUE||'-'||br.Name", KeyNamePair.class, true, false, "br.XX_VMR_BRAND_ID");
		ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), "bp.NAME", KeyNamePair.class, true, false, "bp.C_BPARTNER_ID");
		ColumnInfo colProd = new ColumnInfo(Msg.translate(ctx, "M_PRODUCT_ID"), "iv.VALUE||'-'||iv.NAME", KeyNamePair.class, true, false, "iv.M_PRODUCT_ID");
		ColumnInfo colQnty = new ColumnInfo(Msg.translate(ctx, "XX_CurrentInventory"), "SUM(iv.QtyOnHand)", Integer.class);
		ColumnInfo colAtt = new ColumnInfo (Msg.translate(ctx, "PAttribute"), "att.description", KeyNamePair.class, true, false, "iv.M_ATTRIBUTESETINSTANCE_ID");
		
		//La coleccion y el paquete
		ColumnInfo colColl = new ColumnInfo(Msg.translate(ctx, "XX_Collection"), "IV.COLECCION_NOMBRE", KeyNamePair.class, true, false,  "IV.COLECCION_ID" );
		ColumnInfo colPack = new ColumnInfo(Msg.translate(ctx, "Package"), "IV.PAQUETE_NOMBRE", KeyNamePair.class, true, false, "IV.PAQUETE_ID");	
		
		//Redistribuido
		ColumnInfo colQtyRese = new ColumnInfo(Msg.translate(ctx, "XX_Redistributed"), "SUM(CASE WHEN RESERVADO.cant IS NULL THEN 0 ELSE RESERVADO.cant END)/count(*) ", Integer.class,true,false,null);	
		
		//Predistribuido
		ColumnInfo colQtyPred = new ColumnInfo(Msg.translate(ctx, "XX_Predistributed"), "SUM(CASE WHEN PREDISTRIBUIDO.PQTY IS NULL THEN 0 ELSE PREDISTRIBUIDO.PQTY END)/count(*) ", Integer.class,true,false,null);

		//CHEQUEADO
		ColumnInfo colQtyCheq = new ColumnInfo(Msg.translate(ctx, "XX_Checked"), "SUM(CASE WHEN CHEQUEADO.CHEQ IS NULL THEN 0 ELSE CHEQUEADO.CHEQ END)/count(*) ", Integer.class,true,false,null);
		
		//Disponible
		ColumnInfo colQtyDisp = new ColumnInfo(Msg.translate(ctx, "QtyAvailable"), "SUM(iv.QtyOnHand) " +
																				"- SUM(CASE WHEN RESERVADO.cant IS NULL THEN 0 ELSE RESERVADO.cant END)/count(*) " +
																				"- SUM(CASE WHEN PREDISTRIBUIDO.PQTY IS NULL THEN 0 ELSE PREDISTRIBUIDO.PQTY END)/count(*) " +
																				"- SUM(CASE WHEN CHEQUEADO.CHEQ IS NULL THEN 0 ELSE CHEQUEADO.CHEQ END)/count(*) ", Integer.class,true,false,null);
		
		
		//Referencia
		ColumnInfo colRef = new ColumnInfo(Msg.translate(ctx, "XX_VMR_vendorProdRef_ID"), "vr.VALUE||'-'||vr.Name", KeyNamePair.class, true, false, "vr.XX_VMR_VENDORPRODREF_ID");
		
		//Cambios hechos por Javier Pino
		Vector<ColumnInfo> columns = new Vector<ColumnInfo>();
		String groupby = new String();
		String from = new String(" FROM ");
		String where = new String(" WHERE ");		
		String group_by_ID = "";
		String tableName = "";
		boolean ref = false;
		
		//Clausula With - Los queries que no cambian y no son correlacionados
		String with = 
			" WITH " +
			
				//Piezas disponibles
				" DISPONIBLE AS ( SELECT M_PRODUCT_ID, SUM(QTY) AS QTY FROM M_STORAGEDETAIL " +
					" WHERE M_LOCATOR_ID = " + Env.getCtx().getContext("#XX_L_LOCATORCDCHEQUEADO_ID") +
					" AND QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'"+
					" AND M_AttributeSetInstance_ID>=0" +
					" AND M_lOCATOR_ID >= 0" +
					" GROUP BY M_PRODUCT_ID HAVING SUM(QTY) > 0), " +
					
				//Aquellos productos que esten en una redistribucion no aprobada					
				" RESERVADO AS ( SELECT T.M_PRODUCT_ID,T.XX_DESIREDQUANTITY cant FROM XX_VMR_DISTRIBDETAILTEMP T)," +
				
				//Aquellos productos que esten en un pedido de una predistribuida que este pendiente por etiquetar
				" PREDISTRIBUIDO AS ( SELECT M_PRODUCT_ID,SUM(XX_DISTRIBUTEDQTY)/count(*) PQTY from XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_ORDER P " +
					" ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) WHERE P.XX_ORDERREQUESTSTATUS = '" +
						X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue() + "' GROUP BY D.XX_VMR_DISTRIBUTIONHEADER_ID, M_PRODUCT_ID)," +

				//Aquellos productos que esten en una predistribuida y que han sido chequeados	
				" CHEQUEADO AS " +
				     " (SELECT M_PRODUCT_ID, SUM(XX_DISTRIBUTEDQTY) CHEQ from XX_VMR_PO_PRODUCTDISTRIB D " +
				     " JOIN XX_VMR_DistributionHeader H ON (h.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) " +
				     " WHERE H.XX_DistributionStatus IN ('QR', 'QT') GROUP BY M_PRODUCT_ID) ";
		
		//Combinar estas condiciones en un query	
		from += " XX_CurrentStorageView iv " +
			" JOIN DISPONIBLE ON (IV.M_PRODUCT_ID = DISPONIBLE.M_PRODUCT_ID) " +
			" LEFT JOIN RESERVADO ON (IV.M_PRODUCT_ID = RESERVADO.M_PRODUCT_ID ) " +
			" LEFT JOIN PREDISTRIBUIDO ON (IV.M_PRODUCT_ID = PREDISTRIBUIDO.M_PRODUCT_ID) " +
			" LEFT JOIN CHEQUEADO ON ( IV.M_PRODUCT_ID = CHEQUEADO.M_PRODUCT_ID ) " + 
			"JOIN M_PRODUCT p ON ( IV.M_PRODUCT_ID = p.M_PRODUCT_ID) " +
			"JOIN XX_VMR_VENDORPRODREF vr ON ( vr.XX_VMR_VENDORPRODREF_ID = p.XX_VMR_VENDORPRODREF_ID) "; 
		
		
		//Parte de las condiciones que realizan algo similar al NOT IN 
		where += " iv.M_LOCATOR_ID = " + Env.getCtx().getContext("#XX_L_LOCATORCDCHEQUEADO_ID") ;
				
		//Esto hace la diferencia de         
		//				distribuir = disponible - ( reservado + predistribuido + chequeado ) 
		where += " AND (RESERVADO.M_PRODUCT_ID IS NOT NULL " +
				 " OR PREDISTRIBUIDO.M_PRODUCT_ID IS NOT NULL " +
				 " OR CHEQUEADO.M_PRODUCT_ID IS NOT NULL)  ";
		
		//Leer Categoria
		boolean category = false;
		if((Boolean)checkCategory.getValue()==true || cat.getKey()==99999999 ||  cat.getKey()!= 0 ){
			
			category = true;
			
			from += " INNER JOIN XX_VMR_CATEGORY cat ON (cat.XX_VMR_CATEGORY_ID = iv.XX_VMR_CATEGORY_ID) ";
			from += " INNER JOIN XX_VMR_DEPARTMENT dp ON (dp.XX_VMR_DEPARTMENT_ID = iv.XX_VMR_DEPARTMENT_ID) ";
			columns.add(colCat);
			columns.add(colDept);
			group_by_ID = "dp.XX_VMR_DEPARTMENT_ID";
			groupby += ", cat.VALUE||'-'||cat.Name, cat.XX_VMR_CATEGORY_ID, dp.VALUE||'-'||dp.Name, dp.XX_VMR_DEPARTMENT_ID";
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
					from += " INNER JOIN XX_VMR_DEPARTMENT dp ON (dp.XX_VMR_DEPARTMENT_ID = iv.XX_VMR_DEPARTMENT_ID) ";
					columns.add(colDept);
					group_by_ID = "dp.XX_VMR_DEPARTMENT_ID";
					groupby += ", dp.VALUE||'-'||dp.Name, dp.XX_VMR_DEPARTMENT_ID";
					
					if((Boolean)checkReference.getValue()==true){
						columns.add(colRef);
						groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
						ref = true;
					}
				
					tableName = "dp";
				}
				
				if (dept.getKey()!= 99999999 ) 
					where += " AND dp.XX_VMR_DEPARTMENT_ID = " + dept.getKey();				
			}		
		}
		//Leer Linea
		if((Boolean)checkLine.getValue()==true || line.getKey() == 99999999 || line.getKey()!= 0){
			
			from += " INNER JOIN XX_VMR_LINE li ON (li.XX_VMR_LINE_ID = iv.XX_VMR_LINE_ID)" ;
			columns.add(colLine);
			group_by_ID = "li.XX_VMR_LINE_ID";
			groupby += ", li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID";			
			tableName = "li";
			
			if (line.getKey()!= 99999999)
				where += " AND li.XX_VMR_LINE_ID = " + line.getKey();
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID, iv.M_PRODUCT_ID";
				ref = true;
			}		
		}
		
		//Leyendo seccion 
		if ((Boolean)checkSection.getValue()==true || sect.getKey() == 99999999 || sect.getKey()!= 0){
			
			from += " INNER JOIN XX_VMR_SECTION se ON (se.XX_VMR_SECTION_ID = iv.XX_VMR_SECTION_ID) ";
			columns.add(colSect);
			group_by_ID = "se.XX_VMR_SECTION_ID";
			groupby += ", se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID";			
			tableName = "se";
			
			if (sect.getKey()!= 99999999 ) 
				where += " AND se.XX_VMR_SECTION_ID = " + sect.getKey();
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
		}
		
		//Leyendo marca
		if ((Boolean)checkBrand.getValue()==true || bran.getKey() == 99999999 || bran.getKey()!= 0){
			
			from += " INNER JOIN XX_VMR_BRAND br ON (iv.XX_VMR_BRAND_ID = br.XX_VMR_BRAND_ID) ";
			columns.add(colBran);
			group_by_ID = "br.XX_VMR_BRAND_ID";
			groupby += ", br.VALUE||'-'||br.Name, br.XX_VMR_BRAND_ID";			
			tableName = "br";
			
			if (bran.getKey()!= 99999999) 
				where += " AND br.XX_VMR_BRAND_ID = " + bran.getKey();	
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
		}
		
		//Leyendo proveedor
		if((Boolean)checkBPartner.getValue() == true || bpar.getKey() == 99999999 || bpar.getKey() != 0){
			
			from += " INNER JOIN C_BPARTNER bp ON (bp.C_BPARTNER_ID = iv.C_BPARTNER_ID) ";
			columns.add(colBPar);
			group_by_ID = "bp.C_BPARTNER_ID";
			groupby += ", bp.NAME, bp.C_BPARTNER_ID";			
			tableName = "bp";
			
			if (bpar.getKey()!= 99999999) 
				where += " AND bp.C_BPARTNER_ID = " + bpar.getKey();	
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
		}
		
		//Leyendo coleccion 
		if((Boolean)checkCollection.getValue()==true || coll.getKey()==99999999 || coll.getKey()!= 0 ){
			
			columns.add(colColl);
			where += " AND iv.Coleccion_ID IS NOT NULL";
			group_by_ID = "iv.Coleccion_id";
			groupby += ", iv.Coleccion_Nombre, iv.Coleccion_Id";			
			tableName = "iv";
			
			if (coll.getKey()!= 99999999) 
				where += " AND iv.coleccion_id = " + coll.getKey();	
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}
		}
			
		//Leyendo paquetes
		if (pack == null) {}
		else if((Boolean)checkPackage.getValue()==true || pack.getKey() == 99999999 || pack.getKey()!= 0 ){
					
			columns.add(colPack);
			where += " AND iv.Paquete_ID IS NOT NULL";
			group_by_ID = "iv.paquete_id";
			groupby += ", iv.paquete_nombre, iv.paquete_id";			
			tableName = "iv";
			
			if (pack.getKey()!= 99999999) 
				where += " AND iv.paquete_id = " + pack.getKey();
			
			if((Boolean)checkReference.getValue()==true && !ref){
				columns.add(colRef);
				groupby += ", vr.VALUE||'-'||vr.NAME, vr.XX_VMR_VENDORPRODREF_ID";
				ref = true;
			}	
		}
				
		//Leyendo producto
		if((Boolean)checkProduct.getValue() == true || lookupProduct.getValue() != null ){
			
			from += " LEFT OUTER JOIN M_ATTRIBUTESETINSTANCE att ON (iv.M_ATTRIBUTESETINSTANCE_ID = att.M_ATTRIBUTESETINSTANCE_ID )";
			columns.add(colProd);
			columns.add(colAtt);
			group_by_ID = "iv.M_PRODUCT_ID";
			groupby += ", iv.VALUE||'-'||iv.NAME, iv.M_PRODUCT_ID, att.description, iv.M_ATTRIBUTESETINSTANCE_ID";			
			tableName = "iv";
			
			if (lookupProduct.getValue() != null && checkProduct.getValue().equals(false)) 
				where += " AND iv.M_PRODUCT_ID = " + lookupProduct.getValue();	
			
		}

		//Si se intenta buscar sin seleccionar filtro, debe irse
		if (columns.size() == 0) {
			m_frame.setCursor(Cursor.getDefaultCursor());
			return;
		}

		columns.add(colQnty);
		columns.add(colQtyRese);
		columns.add(colQtyPred);
		columns.add(colQtyCheq);
		columns.add(colQtyDisp);
					
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
			m_sql.append(where);
			m_sql.append(" GROUP BY " + group_by_ID);
			m_sql.append(groupby);
			m_sql.append(" HAVING SUM(IV.QTYONHAND)>0 ");
			m_sql.append(" ORDER BY " + group_by_ID);

		} catch (Exception e) {
			m_frame.setCursor(Cursor.getDefaultCursor());
			return;
		}		
		try
		{		
			System.out.println(m_sql.toString());
			PreparedStatement pstmt = DB.prepareStatement(m_sql.toString(), null);			
			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.log(Level.SEVERE, m_sql.toString(), e);
			m_frame.setCursor(Cursor.getDefaultCursor());
		}
		
		miniTable.setMultiSelection(true);
		miniTable.setRowSelectionAllowed(false);
		
		//Updating STOCK QUANTITY LABEL
		Integer stockQty = 0;
		
		for(int i = 0; i < miniTable.getRowCount(); i++)
			stockQty = stockQty + (Integer)miniTable.getModel().getValueAt(i, indx-3);
		
		labelStockQty.setText(m_format.format(stockQty));
		
		//miniTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		
		calculateSelection();
		miniTable.autoSize();
		miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
		miniTable.getTableHeader().setReorderingAllowed(false);
		
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
		//Clean Form
		else if(e.getSource() == bReset)
			loadBasicInfo();
		
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
		else if(e.getSource() == bSearch) {
			try {
				loadTableInfo();
			} catch (NullPointerException n) {
				n.printStackTrace();
			}
			is_purchaseorder = false;
		}		
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
					desiredQty += ((Number)miniTable.getValueAt(i, 9)).doubleValue();					
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
			}
		}

		//  Information
		
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(m_format.format(desiredQty));
		//info.append(Msg.getMsg(Env.getCtx(), "Remaining")).append(" ").append(m_format.format(remaining));
		dataStatus.setText(info.toString());
		//
		bGenerate.setEnabled(m_noSelected != 0 && desiredQty != 0 && noZero);		
		
	}   //  calculateSelection
	
	

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
	
}