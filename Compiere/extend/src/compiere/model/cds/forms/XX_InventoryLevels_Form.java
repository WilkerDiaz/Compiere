package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;

public class XX_InventoryLevels_Form extends CPanel
implements FormPanel, ActionListener, ListSelectionListener{

	/**
	 * Cálculo de Niveles de inventario por referencia básica
	 * @author Trinimar Acevedo.
	 */
	/**	Window No	*/
	private int m_WindowNo = 0;
	/**	FormFrame	*/
	private FormFrame m_frame;
	
	private static final long serialVersionUID = 1L;
	/****Panels****/
	private CPanel mainPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CPanel requestPanel = new CPanel();
	private CPanel centerPanelS = new CPanel();
	
	private JScrollPane xScrollPane = new JScrollPane();
	
	
	/****Layouts****/
	private BorderLayout mainLayout = new BorderLayout();
	private BorderLayout centerLayout = new BorderLayout();
	private FlowLayout requestLayout = new FlowLayout();
	
	/****Labels-***/
	private CLabel typeBLabel = new CLabel();
	private CLabel dateLabel = new CLabel();
	
	/****ComboBox ****/
	private CComboBox typeBCombo = new CComboBox();
	
	/****Tables***/
	private MiniTablePreparator xTableC = new MiniTablePreparator();
	
	/****Buttons****/
	private CButton bSearch = new CButton();
	
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	 
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		
		try {
			jbInit();
			dynInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		m_frame.getContentPane().add(mainPanel, BorderLayout.NORTH);	
		m_frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		m_frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		
	}
	
	private void addActionListeners() {

  		typeBCombo.addActionListener(this);
		//dateFrom.addActionListener(this);
		bSearch.addActionListener(this);
	}
	
	private void removeActionListeners() {

  		typeBCombo.removeActionListener(this);
		//dateFrom.removeActionListener(this);
		bSearch.removeActionListener(this);
	}
	
	private void dynInit() {	
		ColumnInfo[] layout = null;
		xTableC.setRowCount(0);
		xTableC = new MiniTablePreparator();
			layout = new ColumnInfo[] {
					new ColumnInfo(("Referencia"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Inventario en tiendas"),   ".", Integer.class, false, false, ""),
					new ColumnInfo(("Inventario en CD"),   ".", Integer.class, false, false, ""),
					new ColumnInfo(("Inventario en O/C Aprobadas"),   ".", Integer.class, false, false, ""),
					new ColumnInfo(("Ventas piezas último mes"),   ".", Integer.class, false, false, ""),
					new ColumnInfo(("Inventario de seguridad"),   ".", Integer.class, false, false, ""),
			};
			xTableC.prepareTable(layout, "", "", true, "");
		
		xTableC.setAutoResizeMode(4);
		xTableC.setRowSelectionAllowed(true);
		CompiereColor.setBackground (this); 
		xScrollPane.setPreferredSize(new Dimension(1000, 550));
		//statusBar.setStatusDB(xTable.getRowCount());
		
		repaint();
	}
	
	private void jbInit() throws Exception{	

		removeActionListeners();
		xTableC.setEnabled(true);
		typeBLabel.setVisible(true);
		typeBCombo.setVisible(true);
		dateLabel.setVisible(true);
		dateFrom.setVisible(true);
		dateFrom.setEnabled(false);
		bSearch.setEnabled(false);
		uploadTypeBasic();
		addActionListeners();
		
		typeBLabel.setText("Tipo de básico: ");
		//dateLabel.setText("Fecha: ");
		bSearch.setText("Consultar");
		
		mainPanel.setLayout(mainLayout);
		requestPanel.setLayout(requestLayout);
		centerPanel.setLayout(centerLayout);
		
		requestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				"Consulta"));

		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				"Niveles de inventario"));
		
		requestPanel.add(typeBLabel);
		requestPanel.add(typeBCombo);
		//requestPanel.add(dateLabel);
		//requestPanel.add(dateFrom);
		requestPanel.add(bSearch);
		
		
		centerPanelS.add(xScrollPane, BorderLayout.CENTER);
		centerPanel.add(centerPanelS,  BorderLayout.CENTER);
		
		mainPanel.add(requestPanel,  BorderLayout.NORTH);
		mainPanel.add(centerPanel,  BorderLayout.CENTER);
		
		centerPanel.setPreferredSize(new Dimension(1100, 600));
		xScrollPane.setVisible(true);
	}

	private void uploadTypeBasic(){
		typeBCombo.removeAllItems();
		String sqlPartner = "SELECT XX_VMR_TypeBasic_ID, value FROM XX_VMR_TypeBasic " +
							"WHERE value <> 'Z' " +
							"ORDER BY value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();

			typeBCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				typeBCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		removeActionListeners();
		KeyNamePair typeB = (KeyNamePair)typeBCombo.getSelectedItem();
		xScrollPane.setVisible(false);
		if(e.getSource() == typeBCombo){
			if(typeB.getKey() != -1){
				//dateFrom.setEnabled(true);
				bSearch.setEnabled(true);
			}else{
				//dateFrom.setEnabled(false);
				bSearch.setEnabled(false);
			}
		}else if(e.getSource()== bSearch){
			tableLoad();
			xScrollPane.setVisible(true);
			
		}
		addActionListeners();
		m_frame.setCursor(Cursor.getDefaultCursor());
		
	}
	
	private void tableLoad(){
		int row = 0; 
		xTableC.setRowCount(0);
		xScrollPane.getViewport().add(xTableC, null);
		KeyNamePair typeB = (KeyNamePair)typeBCombo.getSelectedItem();
		String datef = "";
		Calendar cal = Calendar.getInstance();
//		if(dateFrom.getValue() == null){
			String anio = String.valueOf(cal.get(Calendar.YEAR));
			String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
			String dia = String.valueOf(cal.get(Calendar.DATE));
			datef = anio+"-"+mes+"-"+dia;
//		}else{
//			datef = (dateFrom.getValue()).toString();
//		}

		String sql = 
			"SELECT v.VALUE, inv.inventoryWarehouse, inv.inventoryCD, inv.approvedOC, inv.sales, inv.securityInventory "+ 
			"FROM XX_VMR_VENDORPRODREF v "+
			"JOIN xx_vmr_inventoryLevels inv ON (inv.xx_vmr_vendorProdRef_id = v.xx_vmr_vendorProdRef_id) "+
			"WHERE trunc(inv.Created) = TO_DATE('"+datef+"', 'yyyy-mm-dd') AND v.XX_VMR_TypeBasic_ID = "+typeB.getKey();
		//System.out.println(sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				xTableC.setRowCount(row + 1);
				xTableC.setValueAt(rs.getString(1), row, 0);
				xTableC.setValueAt(rs.getInt(2), row, 1);
				xTableC.setValueAt(rs.getInt(3), row, 2);
				xTableC.setValueAt(rs.getInt(4), row, 3);
				xTableC.setValueAt(rs.getInt(5), row, 4);
				xTableC.setValueAt(rs.getInt(6), row, 5);
				row++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
