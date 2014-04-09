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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MInOut;
import compiere.model.cds.MInOutLine;
import compiere.model.cds.MOrderLine;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVLOReturnOfProduct;
import compiere.model.cds.X_XX_VLO_ReturnDetail;

/**
 *  Show Checkup Return Motives
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_CheckupReturnMotives_Form extends CPanel

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
		
		inOutLineID = new Integer(Env.getCtx().get_ValueAsString("#XX_CHECKUPRETURNMOTIVE_ID"));
		Env.getCtx().remove("#XX_CHECKUPRETURNMOTIVE_ID");
	
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
	static CLogger 			log = CLogger.getCLogger(XX_AssociateReference_Form.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private Integer inOutLineID=0;
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder("");
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	private static int tableInit_option;
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private CComboBox xCombo = new CComboBox();
	private CButton bSave = new CButton();
	private CButton bAddMotive = new CButton();
	private CLabel xReturnMotive = new CLabel(Msg.translate(Env.getCtx(), "Return Motive"));
	
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
		
		bSave.setText(Msg.translate(Env.getCtx(), "SaveExit"));
		bSave.setPreferredSize(new Dimension(120,22));	
		bSave.setEnabled(true);
		
		xCombo.setPreferredSize(new Dimension(240,22));
		
		bAddMotive.setText(Msg.translate(Env.getCtx(), "XX_Add"));
		bAddMotive.setPreferredSize(new Dimension(80,22));	
		bAddMotive.setEnabled(true);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(580, 300));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	
		xScrollPane.getViewport().add(xTable, null);
		
		southPanel.add(xReturnMotive ,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 30, 380), 0, 0));
		
		southPanel.add(xCombo,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
					,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 30, 0), 0, 0));
		
		southPanel.add(bAddMotive,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 340, 30, 0), 0, 0));
		
		southPanel.add(bSave,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(70, 0, 10, 0), 0, 0));
		 
	    southPanel.validate();
	
	}   //  jbInit
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "Quantity"),   ".",  Integer.class, false, false,""),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Return Motive"),   ".", KeyNamePair.class),   
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Remove"),   ".", Boolean.class, false, false,"")  
		};
	
		xTable.prepareTable(layout, "", "", false, "");
		xTable.getColumnModel().getColumn(0).setMaxWidth(150);
		xTable.getColumnModel().getColumn(2).setMaxWidth(100);
		xTable.setAutoResizeMode(3);
		
		MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutLineID, null);
		
		xTable.addMouseListener(new MouseListener() {
			
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				removeRow();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				removeRow();
				
				int scrappedQty = 0;
				for(int i=0; i<xTable.getRowCount(); i++){
					scrappedQty = scrappedQty + (Integer)xTable.getValueAt(i, 0);
				}
				statusBar.setStatusDB(scrappedQty);
				
				MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutLineID, null);
				
				int auxQty = 0;
				if(inOutLine.getTargetQty().intValue()>inOutLine.getPickedQty().intValue()){
					auxQty = inOutLine.getPickedQty().intValue();
				}
				else{
					auxQty = inOutLine.getTargetQty().intValue();
				}
				
				if(scrappedQty>auxQty){
					bSave.setEnabled(false);
				}else{
					bSave.setEnabled(true);
				}
			}
		});
		
		xTable.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				int row = xTable.getSelectedColumn();
				int column = xTable.getSelectedRow();
				xTable.editCellAt(column, row);
				
				removeRow();
				
				int scrappedQty = 0;
				for(int i=0; i<xTable.getRowCount(); i++){
					scrappedQty = scrappedQty + (Integer)xTable.getValueAt(i, 0);
				}
				statusBar.setStatusDB(scrappedQty);
				
				MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutLineID, null);
				
				int auxQty = 0;
				if(inOutLine.getTargetQty().intValue()>inOutLine.getPickedQty().intValue()){
					auxQty = inOutLine.getPickedQty().intValue();
				}
				else{
					auxQty = inOutLine.getTargetQty().intValue();
				}
				
				if(scrappedQty>auxQty){
					bSave.setEnabled(false);
				}else{
					bSave.setEnabled(true);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				removeRow();
			}
			
		});
		
		//Cargo los Motivos
		String SQL = "Select XX_VMR_CANCELLATIONMOTIVE_ID, CONCAT(VALUE,CONCAT('-',NAME)) " +
				     "FROM XX_VMR_CANCELLATIONMOTIVE CM, XX_VMR_MOTIVEGROUP MG " +
					 "WHERE MG.XX_VMR_MOTIVEGROUP_ID = CM.XX_VMR_MOTIVEGROUP_ID " +
					 "AND XX_VMR_CANCELLATIONMOTIVE_ID<>" + Env.getCtx().getContext("#XX_L_SURPLUSCANCELMOTIVE_ID") +
					 " AND XX_VMR_CANCELLATIONMOTIVE_ID NOT IN " +
					 "(SELECT XX_VMR_CANCELLATIONMOTIVE_ID FROM XX_VLO_RETURNDETAIL " +
					 "WHERE M_PRODUCT_ID="+inOutLine.getM_Product_ID()+" AND " +
					 "XX_VLO_RETURNOFPRODUCT_ID="+hasReturnMotive(inOutLineID)+")"+
					 "AND MG.XX_USE='RETURNM' ORDER BY NAME ASC";

		KeyNamePair loadKNP = new KeyNamePair(0, "");
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next())
			{
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				xCombo.addItem(loadKNP);    //Agrego los motivos
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		
		//  Visual
		CompiereColor.setBackground (this);
			
		//Cargo la tabla 1
		tableInit_option=0;
		tableInit(inOutLineID);
		tableLoad (xTable);
		
		bSave.addActionListener(this);
		bAddMotive.addActionListener(this);
		
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_ScrappedQty"));
		
		MInOut inOut = new MInOut( Env.getCtx(), inOutLine.getM_InOut_ID(), null);
		
		if(inOut.getDocStatus().equals("CO")){
			bSave.setEnabled(false);
			bAddMotive.setEnabled(false);
			xTable.setEnabled(false);
			xCombo.setEnabled(false);
		}
		
		int scrappedQty = 0;
		for(int i=0; i<xTable.getRowCount(); i++){
			scrappedQty = scrappedQty + (Integer)xTable.getValueAt(i, 0);
		}
		statusBar.setStatusDB(scrappedQty);
		
		int auxQty = 0;
		if(inOutLine.getTargetQty().intValue()>inOutLine.getPickedQty().intValue()){
			auxQty = inOutLine.getPickedQty().intValue();
		}
		else{
			auxQty = inOutLine.getTargetQty().intValue();
		}
		
		if(scrappedQty>auxQty){
			bSave.setEnabled(false);
		}else{
			bSave.setEnabled(true);
		}
		
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
		if (e.getSource() == bSave)
			cmd_Save();
		else if (e.getSource() == bAddMotive)
			cmd_AddMotive();
	}   //  actionPerformed
	
	private void removeRow(){
		
		int tableRow = xTable.getSelectedRow();
		if(tableRow!=-1)
		{
			if(xTable.getValueAt(tableRow, 2)!=null){
				if(xTable.getValueAt(tableRow, 2).equals(true)){
					DefaultTableModel model = (DefaultTableModel) xTable.getModel();
					xCombo.addItem(xTable.getValueAt(tableRow, 1));
					model.removeRow(tableRow);
				}
			}
		}
	}
	
	/**
	 *  Save Button Pressed
	 */
	private void cmd_Save()
	{
		if(xTable.isEditing()){
			xTable.getCellEditor().stopCellEditing();
		}
		
		MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutLineID, null);
		int auxQty = 0;
		if(inOutLine.getTargetQty().intValue()>inOutLine.getPickedQty().intValue()){
			auxQty = inOutLine.getPickedQty().intValue();
		}
		else{
			auxQty = inOutLine.getTargetQty().intValue();
		}
		
		int scrappedQty = 0;
		for(int i=0; i<xTable.getRowCount();i++){
			scrappedQty = scrappedQty + (Integer) xTable.getValueAt(i, 0);
		}
		
		statusBar.setStatusDB(scrappedQty);
		
		if(scrappedQty>auxQty){
			bSave.setEnabled(false);
		}else{
			bSave.setEnabled(true);
		
			MInOut inOut = new MInOut( Env.getCtx(), inOutLine.getM_InOut_ID(), null);
			
			if(xTable.getRowCount()!=0){
				
				int returnMID = hasReturnMotive(inOutLineID);
				
				//Si tiene detalles los borro (excepto los sobrantes)
				Vector<X_XX_VLO_ReturnDetail> details = allReturnDetails(returnMID);
					
				if(details.size()>0)
				{
					for(int i=0; i<details.size(); i++){
						details.get(i).delete(true);
					}
				}
					
				MVLOReturnOfProduct returnOfProduct = new MVLOReturnOfProduct( Env.getCtx(), returnMID, null);
				//returnOfProduct.setXX_Status("DPR");
				returnOfProduct.setXX_Status("PRE"); //Antes de completar
				returnOfProduct.setC_BPartner_ID(inOut.getC_BPartner_ID());
				returnOfProduct.setC_Order_ID(inOut.getC_Order_ID());
				returnOfProduct.setNotificationType("E");
				returnOfProduct.setXX_IdAsis_ID(inOut.getXX_CheckAssistant_ID());
				returnOfProduct.setXX_ReturnedFrom("CH");
				returnOfProduct.save();
				
				//Comienzo Javier Pino 
				MProduct product = new MProduct(Env.getCtx(), inOutLine.getM_Product_ID(), null);
				MOrderLine line = new MOrderLine(Env.getCtx(), inOutLine.getC_OrderLine_ID(), null);
				
				//Calcular el impuesto				
				String sql = "SELECT (rate/100)"
							+ " FROM C_Tax"
							+ " WHERE ValidFrom="			
							+ " (SELECT MAX(ValidFrom)"											
							+ " FROM C_Tax"
							+ " WHERE C_TaxCategory_ID=" + product.getC_TaxCategory_ID()+")";	
				PreparedStatement prst = DB.prepareStatement(sql,null);
				BigDecimal Tax = Env.ZERO;
				try {
					ResultSet rs = prst.executeQuery();
					if (rs.next()){
						Tax = rs.getBigDecimal(1);
					}
					rs.close();
					prst.close();
				} catch (SQLException e){}
				
				//Fin Javier Pino
				for(int i=0; i<xTable.getRowCount(); i++){
					
					X_XX_VLO_ReturnDetail returnDetail = new X_XX_VLO_ReturnDetail( Env.getCtx(), 0, null);
					returnDetail.setXX_VLO_ReturnOfProduct_ID(returnOfProduct.get_ID());					
					KeyNamePair canMotive = (KeyNamePair) xTable.getValueAt(i, 1);
					int Qty = (Integer) xTable.getValueAt(i, 0);
					returnDetail.setXX_VMR_CancellationMotive_ID(canMotive.getKey());
					returnDetail.setM_Product_ID(inOutLine.getM_Product_ID());
					returnDetail.setXX_TotalPieces(Qty);					
					returnDetail.setPriceActual(line.getPriceActual());
					if (product.getC_TaxCategory_ID() != 0 )
						returnDetail.setC_TaxCategory_ID(product.getC_TaxCategory_ID());
					returnDetail.setTaxAmt(Tax.multiply(line.getPriceActual().multiply(new BigDecimal(Qty))));					
					returnDetail.save();
				}		
			}
			else{//Si No se guardaron motivos
				
				int returnMID = hasReturnMotive(inOutLineID);
				if(returnMID!=0){
					
					Vector<X_XX_VLO_ReturnDetail> details = allReturnDetails(returnMID);
					
					if(details.size()>0)
					{
						for(int i=0; i<details.size(); i++){
							details.get(i).delete(true);
						}
					}
				}
			}
			
			inOutLine.setScrappedQty(new BigDecimal(scrappedQty));
			inOutLine.save();
			dispose();	
		}
		
	}   //  cmd_Save
	
	/**
	 *  Add Button Pressed
	 */
	private void cmd_AddMotive()
	{
		if(xCombo.getItemCount()!=0){
				
			TableModel model = xTable.getModel();
			xTable.setRowCount(xTable.getRowCount()+1);
			
			int pos = xTable.getRowCount();
			
			if(xTable.getRowCount()>0 && xTable.isEditing()){
				xTable.getCellEditor().stopCellEditing();
			}
			
			if(xTable.getRowCount()!=1){
				for(int i=1; i<pos; i++){
									
					model.setValueAt(xTable.getValueAt(pos-1-i, 0), pos-i, 0);
					model.setValueAt(xTable.getValueAt(pos-1-i, 1), pos-i, 1);
				}
			}
			
			model.setValueAt(new Integer(0), 0, 0);
			model.setValueAt(xCombo.getSelectedItem(), 0, 1);
			model.setValueAt(false, 0, 2);
			
			xCombo.removeItemAt(xCombo.getSelectedIndex());
		}		
	}   //  cmd_AddMotive
	
	
	
	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{		
		if (e.getValueIsAdjusting())
			return;
	
	}   //  valueChanged
	
	/**************************************************************************
	 *  Initialize Table access - create SQL, dateColumn.
	 */
	private static void tableInit (int inOutLineID_aux)
	{
		MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutLineID_aux, null);
		MInOut inOut = new MInOut( Env.getCtx(), inOutLine.getM_InOut_ID(), null);
		
		m_sql = new StringBuffer ();
	
		if(tableInit_option==0)
		{
			m_sql.append("SELECT tab.XX_TOTALPIECES, can.NAME, tab.XX_VMR_CANCELLATIONMOTIVE_ID, tab.XX_VMR_CANCELLATIONMOTIVE_ID, 'N' " + 
					     "FROM XX_VLO_RETURNDETAIL tab, XX_VMR_CANCELLATIONMOTIVE can " +
					     "WHERE tab.M_PRODUCT_ID=" + inOutLine.getM_Product_ID() + " " +
					     "AND tab.XX_VMR_CANCELLATIONMOTIVE_ID<>" + Env.getCtx().getContext("#XX_L_SURPLUSCANCELMOTIVE_ID") +
					     "AND can.XX_VMR_CANCELLATIONMOTIVE_ID=tab.XX_VMR_CANCELLATIONMOTIVE_ID " +
					     "AND tab.XX_VLO_RETURNOFPRODUCT_ID IN " +
					     	"(SELECT XX_VLO_RETURNOFPRODUCT_ID FROM XX_VLO_RETURNOFPRODUCT " +
					     	 "WHERE C_ORDER_ID="+inOut.getC_Order_ID()+")");
					
					m_orderBy = " order by tab.XX_VMR_CANCELLATIONMOTIVE_ID";
		}

	
	}   //  tableInit
	
	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private static void tableLoad (MiniTable table)
	{
	//	log.finest(m_sql + " - " +  m_groupBy);
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "tab", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ m_groupBy + m_orderBy;
		
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
	
	private int hasReturnMotive(int inOutlineID){
		
		MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutlineID, null);
		MInOut inOut = new MInOut( Env.getCtx(), inOutLine.getM_InOut_ID(), null);
		
		String SQL = "SELECT tab.XX_VLO_RETURNOFPRODUCT_ID " + 
					     "FROM XX_VLO_RETURNOFPRODUCT tab " +
					     "WHERE C_ORDER_ID="+inOut.getC_Order_ID();
		
		int returnID=0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next())
			{
				returnID = rs.getInt("XX_VLO_RETURNOFPRODUCT_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		
		return returnID;
	}
	
	private Vector<X_XX_VLO_ReturnDetail> allReturnDetails(int returnMotiveID){
		
		Vector<X_XX_VLO_ReturnDetail> details = new Vector<X_XX_VLO_ReturnDetail>();
		MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutLineID, null);
		
		String SQL = "SELECT tab.XX_VLO_RETURNDETAIL_ID " + 
					     "FROM XX_VLO_RETURNDETAIL tab " +
					     "WHERE M_PRODUCT_ID=" + inOutLine.getM_Product_ID() +
					     " AND XX_VMR_CANCELLATIONMOTIVE_ID<>" + Env.getCtx().getContext("#XX_L_SURPLUSCANCELMOTIVE_ID") +
					     " AND XX_VLO_RETURNOFPRODUCT_ID="+returnMotiveID;
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
		
			X_XX_VLO_ReturnDetail detail = null;
			while (rs.next())
			{
				detail = new X_XX_VLO_ReturnDetail( Env.getCtx(),rs.getInt("XX_VLO_RETURNDETAIL_ID"), null);
				details.add(detail);
			}
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		
		return details;
	}

}//End form