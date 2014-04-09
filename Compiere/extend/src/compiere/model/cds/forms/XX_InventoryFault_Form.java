package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
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

public class XX_InventoryFault_Form extends CPanel
implements FormPanel, ActionListener, ListSelectionListener{

	/**
	 * Cálculo de fallas de inventario por productos para una referencia básica
	 * @author Trinimar Acevedo.
	 */
	/**	Window No	*/
	private int m_WindowNo = 0;
	/**	FormFrame	*/
	private FormFrame m_frame;
	private static final long serialVersionUID = 1L;
	/****Panels****/
	private CPanel mainPanel = new CPanel();
	
	private CPanel northPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	
	private CPanel centerPanelS = new CPanel();
	private CPanel southPanelS = new CPanel();
	
	private JScrollPane xScrollPaneA = new JScrollPane();
	private JScrollPane xScrollPaneB = new JScrollPane();
	
	
	/****Layouts****/
	private BorderLayout mainLayout = new BorderLayout();
	
	private FlowLayout northLayout = new FlowLayout();
	private BorderLayout centerLayout = new BorderLayout();
	private BorderLayout southLayout = new BorderLayout();
	
	/****Labels****/
	private CLabel yearLabel = new CLabel();
	private CLabel monthLabel = new CLabel();
	
	/****ComboBox****/
	private CComboBox yearCombo = new CComboBox();
	private CComboBox monthCombo = new CComboBox();
	
	/****Tables****/
	private MiniTablePreparator xTableA = new MiniTablePreparator();
	private MiniTablePreparator xTableB = new MiniTablePreparator();
	
	/****Buttons****/
	private CButton bSearch = new CButton();

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
			dynInitA();
			dynInitB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		m_frame.getContentPane().add(mainPanel, BorderLayout.NORTH);	
		m_frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		m_frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		m_frame.setResizable(false);
		
	}
	
	private void addActionListeners() {

  		yearCombo.addActionListener(this);
		monthCombo.addActionListener(this);
		bSearch.addActionListener(this);
	}
	
	private void removeActionListeners() {

  		yearCombo.removeActionListener(this);
		monthCombo.removeActionListener(this);
		bSearch.removeActionListener(this);
	}
	
	private void dynInitA() {	
		ColumnInfo[] layout = null;
		xTableA.setRowCount(0);
		xTableA = new MiniTablePreparator();
		//xScrollPaneA.getViewport().add(xTableA, null);
			layout = new ColumnInfo[] {
					new ColumnInfo(("Tipo de Básico"),   ".", String.class, false, false, ""),
					new ColumnInfo(("%de Falla Real"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Meta"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Desviación"),   ".", String.class, false, false, ""),
			};
			xTableA.prepareTable(layout, "", "", true, "");
		
		xTableA.setAutoResizeMode(4);
		xTableA.setRowSelectionAllowed(true);
		CompiereColor.setBackground (this); 
		xScrollPaneA.setPreferredSize(new Dimension(550, 150));
		//statusBar.setStatusDB(xTable.getRowCount());
		
	}
	
	private void dynInitB() {	
		ColumnInfo[] layout = null;
		xTableB.setRowCount(0);
		xTableB = new MiniTablePreparator();
			layout = new ColumnInfo[] {
					new ColumnInfo(("Tienda"),   ".", String.class, false, false, ""),
					new ColumnInfo(("AAA"),   ".", String.class, false, false, ""),
					new ColumnInfo(("B"),   ".", String.class, false, false, ""),
					new ColumnInfo(("C"),   ".", String.class, false, false, ""),
					new ColumnInfo(("D"),   ".", String.class, false, false, ""),
					new ColumnInfo(("Total"),   ".", String.class, false, false, ""),
			};
			xTableB.prepareTable(layout, "", "", true, "");
		
		xTableB.setAutoResizeMode(4);
		xTableB.setRowSelectionAllowed(true);
		CompiereColor.setBackground (this); 
		xScrollPaneB.setPreferredSize(new Dimension(550, 150));
		//statusBar.setStatusDB(xTable.getRowCount());
		
	}
	
	private void jbInit() throws Exception{	
		
		removeActionListeners();
		xTableA.setEnabled(true);
		xTableB.setEnabled(true);
		yearLabel.setVisible(true);
		yearCombo.setVisible(true);
		monthLabel.setVisible(true);
		monthCombo.setVisible(true);
		bSearch.setEnabled(false);
		bSearch.setVisible(true);
		uploadYear();
		addActionListeners();
		
		yearLabel.setText("Año: ");
		monthLabel.setText("Mes: ");
		bSearch.setText("Consultar");
		
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		northPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				"Consulta"));

		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				"Detalle por tipos de básicos"));
		
		southPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				"Consulta por tienda y tipo de básico"));
		
		northPanel.add(yearLabel);
		northPanel.add(yearCombo);
		northPanel.add(monthLabel);
		northPanel.add(monthCombo);
		northPanel.add(bSearch);
		
		
		centerPanelS.add(xScrollPaneA, BorderLayout.CENTER);
		centerPanel.add(centerPanelS,  BorderLayout.CENTER);
		
		southPanelS.add(xScrollPaneB, BorderLayout.CENTER);
		southPanel.add(southPanelS,  BorderLayout.CENTER);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(centerPanel,  BorderLayout.CENTER);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		
		centerPanel.setPreferredSize(new Dimension(600, 200));
		southPanel.setPreferredSize(new Dimension(600, 200));
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) { 
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		removeActionListeners();
		KeyNamePair year = (KeyNamePair)yearCombo.getSelectedItem();
		KeyNamePair month = (KeyNamePair)monthCombo.getSelectedItem();
		xScrollPaneA.setVisible(false);
		xScrollPaneB.setVisible(false);
		
		if(e.getSource() == yearCombo){
			if(year.getKey() != -1){
				uploadMonth();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				monthCombo.setEnabled(true);
			}else{
				monthCombo.setEnabled(false);
			}
		}else if(e.getSource()== monthCombo){
			if(month.getKey() != -1 && year.getKey() != -1){
				bSearch.setEnabled(true);
			}else{
				bSearch.setEnabled(false);
			}
		}else if(e.getSource()== bSearch){
			tableLoadA();
			tableLoadB();
			xScrollPaneA.setVisible(true);
			xScrollPaneB.setVisible(true);
			
		}
		addActionListeners();
		m_frame.setCursor(Cursor.getDefaultCursor());
	}
	
	private void tableLoadA(){
		
		int row = 0; 
		int f = 0;
		int m = 0;
		xTableA.setRowCount(0);
		xScrollPaneA.getViewport().add(xTableA, null);
		KeyNamePair year = (KeyNamePair)yearCombo.getSelectedItem();
		KeyNamePair month = (KeyNamePair)monthCombo.getSelectedItem();
		m = month.getKey()+1;
		NumberFormat NF = NumberFormat.getInstance();
		NF.setMaximumFractionDigits(2);
		String sql = "select  t.value, t.xx_vmr_typebasic_id, i.faultpercentage*100, t.goal, i.faultpercentage*100/t.goal from xx_vmr_typebasic t " +
		"join xx_vmr_inventoryfault i on (i.xx_vmr_typebasic_id = t.xx_vmr_typebasic_id) " +
		"where i.typeinventoryfault = '3' and t.value <> 'Z' " +
		"and InventoryYear = "+year.getKey()+" and ClosedMonth = "+m+" "+
		"order by (t.value)";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			xTableA.setRowCount(row + 1);
			xTableA.setValueAt("AAA", row, 0); 
			row++;
			xTableA.setRowCount(row + 1);
			xTableA.setValueAt("B", row, 0); 
			row++;
			xTableA.setRowCount(row + 1);
			xTableA.setValueAt("C", row, 0); 
			row++;
			xTableA.setRowCount(row + 1);
			xTableA.setValueAt("D", row, 0); 
			while (rs.next()) {
				if(rs.getString(1).equals("AAA")){
					f = 0;
				}else if (rs.getString(1).equals("B")){
					f = 1;
				}else if (rs.getString(1).equals("C")){
					f = 2;
				}else {
					f = 3;
				}

				xTableA.setValueAt(NF.format(rs.getDouble(3)), f, 1);
				xTableA.setValueAt(rs.getString(4), f, 2);
				xTableA.setValueAt(NF.format(rs.getDouble(5)), f, 3);
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
	
private void tableLoadB(){
		
		int row = 0; 
		int f=0;
		int c=0;
		int m = 0;
		Double t1 = 0.0;
		Double t2 = 0.0;
		Double t3 = 0.0;
		Double t7 = 0.0;
		Double t9 = 0.0;
		Double t10 = 0.0;
		Double t15 = 0.0;
		Double t16 = 0.0;
		Double t17 = 0.0;
		KeyNamePair year = (KeyNamePair)yearCombo.getSelectedItem();
		KeyNamePair month = (KeyNamePair)monthCombo.getSelectedItem();
		m = month.getKey()+1;
		xTableB.setRowCount(0);
		xScrollPaneB.getViewport().add(xTableB, null);
		String sql = "WITH tb as (select value, m_warehouse_id " +
				"from m_warehouse where value in ('001', '002', '003', '007', '009', '010', '015', '016', '017') " +
				"order by value)  " +
				"select t.value, i.faultpercentage, i.m_warehouse_id, tb.value  from xx_vmr_typebasic t " +
				"join xx_vmr_inventoryfault i on (i.xx_vmr_typebasic_id = t.xx_vmr_typebasic_id) " +
				"join tb on (tb.m_warehouse_id = i.m_warehouse_id) " +
				"where i.typeinventoryfault = '4' and t.value <> 'Z' " +
				"and InventoryYear = "+year.getKey()+" and ClosedMonth = "+m+" "+
				"group by i.m_warehouse_id, t.value, i.faultpercentage, tb.value  " +
				"order by (t.value) ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		NumberFormat NF = NumberFormat.getInstance();
		NF.setMaximumFractionDigits(2); 
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T001", row, 0); 
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T002", row, 0); 
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T003", row, 0); 
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T007", row, 0); 
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T009", row, 0); 
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T010", row, 0); 
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T015", row, 0);
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T016", row, 0); 
			row++;
			xTableB.setRowCount(row + 1);
			xTableB.setValueAt("T017", row, 0); 
			while (rs.next()) {
				if(rs.getString(4).equals("001")){
					t1 = t1 + rs.getDouble(2)*100;
					f = 0;
					xTableB.setValueAt(NF.format(t1/4), f, 5);
				}else if (rs.getString(4).equals("002")){
					t2 = t2 + rs.getDouble(2)*100;
					f = 1;
					xTableB.setValueAt(NF.format(t2/4), f, 5);
				}else if (rs.getString(4).equals("003")){
					t3 = t3 + rs.getDouble(2)*100;
					f = 2;
					xTableB.setValueAt(NF.format(t3/4), f, 5);
				}else if (rs.getString(4).equals("007")){
					t7 = t7 + rs.getDouble(2)*100;
					f =3;
					xTableB.setValueAt(NF.format(t7/4), f, 5);
				}else if (rs.getString(4).equals("009")){
					t9 = t9 + rs.getDouble(2)*100;
					f = 4;
					xTableB.setValueAt(NF.format(t9/4), f, 5);
				}else if (rs.getString(4).equals("010")){
					t10 = t10 + rs.getDouble(2)*100;
					f = 5;
					xTableB.setValueAt(NF.format(t10/4), f, 5);
				}else if (rs.getString(4).equals("015")){
					t15 = t15 + rs.getDouble(2)*100;
					f = 6;
					xTableB.setValueAt(NF.format(t15/4), f, 5);
				}else if (rs.getString(4).equals("016")){
					t16 = t16 + rs.getDouble(2)*100;
					f = 7;
					xTableB.setValueAt(NF.format(t16/4), f, 5);
				}else{
					t17 = t17 + rs.getDouble(2)*100;
					f = 8;
					xTableB.setValueAt(NF.format(t17/4), f, 5);
				}
				
				if (rs.getString(1).equals("AAA")){
					c = 1;
				}else if (rs.getString(1).equals("B")){
					c = 2;
				}else if (rs.getString(1).equals("C")){
					c = 3;
				}else{
					c = 4;
				}
				xTableB.setValueAt(NF.format(rs.getDouble(2)*100), f, c);
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

	
	private void uploadYear(){
		yearCombo.removeAllItems();
		String sqlPartner = "SELECT DISTINCT InventoryYear FROM XX_VMR_InventoryFault " +
							"ORDER BY InventoryYear";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();
			yearCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				yearCombo.addItem(new KeyNamePair(rs.getInt(1), rs.getString(1)));
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
	
	private void uploadMonth(){
		monthCombo.removeAllItems();
		
		KeyNamePair year = (KeyNamePair)yearCombo.getSelectedItem();
		String sqlPartner = "SELECT DISTINCT ClosedMonth FROM XX_VMR_InventoryFault " +
							"WHERE InventoryYear = "+year.getKey()+" AND typeInventoryFault = '3' "+
							"ORDER BY ClosedMonth";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String monthName = "";
		int month = 0;
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();
			
			monthCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				month = rs.getInt(1)-1;
//				Calendar cal = Calendar.getInstance();
//				cal.set(Calendar.MONTH, month);
				/* Solo retorna el nombre del primer mes
				 * monthName = cal.getDisplayName(month, cal.SHORT, Locale.getDefault());
				 */
				monthName = monthName(month);
				monthCombo.addItem(new KeyNamePair(month, monthName.toUpperCase()));
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
	

	public String monthName(int month) {
		String name = "";
		switch (month){
		case 0:
			name = "ENERO";
			break;
		case 1:
			name = "FEBRERO";
			break;
		case 2:
			name = "MARZO";
			break;
		case 3:
			name = "ABRIL";
			break;
		case 4:
			name = "MAYO";
			break;
		case 5:
			name = "JUNIO";
			break;
		case 6:
			name = "JULIO";
			break;
		case 7:
			name = "AGOSTO";
			break;
		case 8:
			name = "SEPTIEMBRE";
			break;
		case 9:
			name = "OCTUBRE";
			break;
		case 10:
			name = "NOVIEMBRE";
			break;
		case 11:
			name = "DICIEMBRE";
			break;
		}
		return name;
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
