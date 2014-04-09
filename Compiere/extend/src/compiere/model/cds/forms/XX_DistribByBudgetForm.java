package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.logging.*;

import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.processes.XX_PercentualDistributionRedistribution;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.border.TitledBorder;

/**
 *  
 *
 *  @author     
 *  @version    
 */
public class XX_DistribByBudgetForm extends CPanel
	implements FormPanel, ActionListener {
	
	


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
			//
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
	static CLogger 			log = CLogger.getCLogger(XX_DistribByBudgetForm.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private CLabel LineRefProv_Label = new CLabel();
	static Ctx ctx_aux = new Ctx();
	static Integer Product_ID;
	static Integer associatedReference_ID;
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private CButton bAssociate = new CButton();
	private CButton bNewProduct = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	//private CButton bDisassociate = new CButton();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xProductScrollPane = new JScrollPane();
	private TitledBorder xProductBorder = new TitledBorder(Msg.translate(Env.getCtx(), "XX_ChooseWarehouses"));
	private MiniTable xProductTable = new MiniTable();
	private JScrollPane xAssociateScrollPane = new JScrollPane();
	//private TitledBorder xAssociateBorder = new TitledBorder(private TitledBorder xProductBorder = new TitledBorder(Msg.translate(Env.getCtx(), "XX_PercentageDistribution")););
	private MiniTable xAssociateTable = new MiniTable();
	private CPanel xPanel = new CPanel();
	
	DecimalFormat formato = new DecimalFormat("#,##0.00");
	
	private Vector<String> warehouse_names = new Vector<String>();
	private Vector<Integer> warehouse_codes = new Vector<Integer>();
	private Vector<Double> quantity_bud = new Vector<Double>();
	private Vector<Double> percentages_sal = new Vector<Double>();
	private Vector<Double> percentages_ave = new Vector<Double>();
	private Vector<Boolean> selection = new Vector<Boolean>();
	private Vector<Integer> budget = new Vector<Integer>();

	private Vector<Double> initial_percentages = new Vector<Double>();
	
	private static int record = 0;
	private static Trx trxname = null;
	private static Ctx context = null;
	
	int position_cd = -1;	
	private double totalBudget = 0;

	private X_XX_VMR_DistributionDetail detail = new X_XX_VMR_DistributionDetail(context, record, trxname);
	
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);

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
	
		bAssociate.setText(Msg.translate(Env.getCtx(), "XX_CalculateDistribution"));
		bAssociate.setEnabled(true);
		southPanel.setLayout(southLayout);
		bNewProduct.setText(Msg.translate(Env.getCtx(), "XX_GeneratePieces"));
		bNewProduct.setEnabled(true);
		
		
		centerPanel.setLayout(centerLayout);
		xProductScrollPane.setBorder(xProductBorder);
		xProductScrollPane.setPreferredSize(new Dimension(475, 200));
		xAssociateScrollPane.setPreferredSize(new Dimension(450, 58));
		
		xPanel.setLayout(xLayout);
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		northPanel.add(LineRefProv_Label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xProductScrollPane,  BorderLayout.NORTH);
		xProductScrollPane.getViewport().add(xProductTable, null);
		
		xAssociateScrollPane.getViewport().add(xAssociateTable, null);
		
		southPanel.add(bAssociate,   new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		southPanel.add(bNewProduct,   new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		
		
		
		}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		// COLUMNAS///////////////////////
		
		ColumnInfo[] layout = new ColumnInfo[] {			
			new ColumnInfo(" ", "", Boolean.class, false, false, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"),  ".", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_PercentageAssigned"), ".", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(),"XX_Chart"), ".", Double.class)
		};

		xProductTable.prepareTable(layout, "", "", false, "");

		//  Visual
		CompiereColor.setBackground (this);

		bAssociate.addActionListener(this);
		//bDisassociate.addActionListener(this);
		bNewProduct.addActionListener(this);

		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		IndicatorCellRenderer renderer = new IndicatorCellRenderer(0, 100);
        renderer.setStringPainted(false);
     
        renderer.setBackground(xProductTable.getBackground());
         
        // set limit value and fill color
        Hashtable<Integer, Color> limitColors = new Hashtable<Integer, Color>();
        limitColors.put(new Integer(0), Color.green);
        limitColors.put(new Integer(60), Color.yellow);
        limitColors.put(new Integer(80), Color.red);
        renderer.setLimits(limitColors);
        
        xProductTable.getTableHeader().setReorderingAllowed(false);
        xProductTable.setSortEnabled(false);
        xProductTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);        
        xProductTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
				
		gettingData();
		tableLoad();
		
		
	}   //  dynInit
	
	public static Ctx setContext(Ctx ctx){
		return context = ctx;
	}
	
	public static int setRecord(int rec){
		return record = rec;
	}
	
	public static Trx setTrxName(Trx trx){
		return trxname = trx;
	}
	
	
	
	private void gettingData(){
		
		Vector<Integer> temp_codes = new Vector<Integer>();
		Vector<Double>   temp_percentages = new Vector<Double>();
				
		//distributionType = detail.getXX_VMR_DistributionType_ID();
		//Jorge Pires - Ordenar tiendas!
		String sql_w ="SELECT M_WAREHOUSE_ID, VALUE||'-'||NAME FROM M_WAREHOUSE WHERE ISACTIVE = 'Y' " ;
		sql_w = MRole.getDefault().addAccessSQL(sql_w, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		sql_w += " ORDER BY VALUE||'-'||NAME";			

		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql_w, null);
			ResultSet rs = pstmt.executeQuery();
			
			int iterations = 0;
			while(rs.next())
			{	
				selection.add(true);
				warehouse_codes.add(rs.getInt("M_WAREHOUSE_ID"));
				
				if (rs.getInt("M_WAREHOUSE_ID") == Env.getCtx().getContextAsInt(
							"#XX_L_WAREHOUSECENTRODIST_ID")) {
						position_cd = iterations;
				}
				initial_percentages.add(new Double(0));
				warehouse_names.add(rs.getString(2));
				quantity_bud.add(0.0);
				percentages_sal.add((double)0);
				percentages_ave.add((double)0);
				iterations++;
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql_w, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				temp_codes.add(rs.getInt("M_WAREHOUSE_ID"));
				temp_percentages.add(rs.getDouble("XX_PERCENTAGE"));
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
		
		getDistribByBudget();
		tableLoad();
		

	}

	
	private void getDistribByBudget(){
		
		Calendar actualDate = Calendar.getInstance();
		int year = actualDate.get(Calendar.YEAR);
		int month = actualDate.get(Calendar.MONTH) + 1;
		String yearmonth;
		
		Vector<Integer> codes = new Vector<Integer>();
		
		if(month < 10)
			yearmonth = year + "0" + month;
		else
			yearmonth = "" + year + month;

		int dept = detail.getXX_VMR_Department_ID();
		int line = detail.getXX_VMR_Line_ID();
		int sect = detail.getXX_VMR_Section_ID();

		String sql = 
			"SELECT wh.M_WAREHOUSE_ID, wh.NAME, SUM(ip.XX_SALESAMOUNTBUD) FROM XX_VMR_PRLD01 ip" 
			+ " INNER JOIN M_WAREHOUSE wh ON (ip.M_WAREHOUSE_ID = wh.M_WAREHOUSE_ID) ";
				
		sql += " WHERE ip.XX_BUDGETYEARMONTH = " + yearmonth ;
		
		if(line != 0){
			sql +=  " AND ip.XX_VMR_LINE_ID = " + line;
		}
		if(sect != 0){
			sql += " AND ip.XX_VMR_SECTION_ID = " + sect; 		
		}		 
				
		sql += " AND ip.XX_VMR_DEPARTMENT_ID = " + dept; 
		
		sql = MRole.getDefault().addAccessSQL(sql, "ip", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);	
			sql += " GROUP BY wh.M_WAREHOUSE_ID, wh.NAME " + 	" ORDER BY wh.M_WAREHOUSE_ID";
		
		//System.out.println(sql);
		totalBudget = 0;
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{	
				codes.add(rs.getInt(1));
				budget.add(rs.getInt(3));
				totalBudget +=  rs.getInt(3);
				
			}			
			rs.close();
			pstmt.close();						
		}		
		catch(SQLException e)
		{	
			e.getMessage();
		}	
		for (int i = 0; i < codes.size(); i++){
			for(int j = 0; j < warehouse_codes.size(); j++ ){
				if(warehouse_codes.get(j).equals(codes.get(i))){
					double temp = (budget.get(i)/totalBudget)*100;			
					if (totalBudget == 0.0)
						initial_percentages.set(j, 0.0);
					else 
						initial_percentages.set(j, temp); 					
					break;
				}
			}
		}		
	}

    public void recalculate() {
	    
    	double sum = 0;
    	for (int i = 0 ; i < xProductTable.getRowCount(); i++) {	
			if (i == position_cd) continue;    		
    		boolean selected = (Boolean)xProductTable.getValueAt(i, 0);
			if (selected) { 				
				sum += initial_percentages.get(i);
			} 			
		}
		for (int i = 0 ; i < xProductTable.getRowCount(); i++) {
    		boolean selected = (Boolean)xProductTable.getValueAt(i, 0);
    		if (i == position_cd) continue;
    		
			if (!selected ) { 
				xProductTable.setValueAt(formato.format(0), i, 2) ;
				xProductTable.setValueAt(0, i, 3) ;
			} else {
				xProductTable.setValueAt( formato.format(initial_percentages.get(i)), i, 2) ;
				xProductTable.setValueAt( initial_percentages.get(i) , i, 3) ;
			}
		}		
		if ( 100.0 - sum > 0) {
			//System.out.println(" resta " + (100.0 - sum));
			xProductTable.setValueAt( formato.format( (100.0 - sum)), position_cd, 2);
			xProductTable.setValueAt( (100 - sum) , position_cd, 3);
			xProductTable.setValueAt(true, position_cd, 0);
		} else {
			xProductTable.setValueAt( formato.format( 0), position_cd, 2);
			xProductTable.setValueAt( 0 , position_cd, 3);
			xProductTable.setValueAt(false, position_cd, 0);			
		}
		xProductTable.autoSize();
		xProductTable.repaint();		
		return;
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

	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{				
		if (e.getSource() == bAssociate)
			recalculate();
		
		else if (e.getSource() == bNewProduct)
			savingData();		
	}  


	/**
	 *  Fill the table using m_sql
	 *  @param table table
	 */
	private void tableLoad ()
	{
		xProductTable.setRowCount(warehouse_codes.size());
		xProductTable.removeAll();
		
		
		for(int i = 0; i < warehouse_codes.size(); i++)
		{
			if (i != position_cd)
				xProductTable.getModel().setValueAt(selection.get(i), i, 0);
			else xProductTable.setValueAt( false, position_cd, 0);
			xProductTable.getModel().setValueAt(warehouse_names.get(i),i,1);
			xProductTable.getModel().setValueAt(
					formato.format(initial_percentages.get(i))	,i,2);
			xProductTable.getModel().setValueAt(initial_percentages.get(i),i,3);
		}
		
		xProductTable.autoSize();
		
		
	}   //  tableLoad


	private void savingData(){		
		Vector<BigDecimal> percentages = new Vector<BigDecimal>();		
		for(int i = 0; i < xProductTable.getRowCount(); i++){		
			percentages.add(new BigDecimal (((Number)xProductTable.getValueAt(i, 3)).doubleValue()));			
		}		
		boolean it_work = XX_PercentualDistributionRedistribution.applyDistribution(
				detail, percentages, warehouse_codes, m_WindowNo , m_frame);
		if (it_work ) dispose();

	}

   //  ConvertReference

	class IndicatorCellRenderer extends JProgressBar implements TableCellRenderer {
	  
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Hashtable<Integer, Color> limitColors;

		private int[] limitValues;

		public IndicatorCellRenderer() {
			super(JProgressBar.HORIZONTAL);
			setBorderPainted(false);
		}
		
		public IndicatorCellRenderer(int min, int max) {
			super(JProgressBar.HORIZONTAL, min, max);
			setBorderPainted(false);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value,
	          boolean isSelected, boolean hasFocus, int row, int column) {
			
			int n = 0;
			if (!(value instanceof Number)) {
				String str;
				if (value instanceof String) {
					str = (String) value;
				} else {
					str = value.toString();
				}
				try {
					n = Integer.valueOf(str).intValue();
				} catch (NumberFormatException ex) {
				}
			} else {
				n = ((Number) value).intValue();
			}
			
			Color color = getColor(n);
			if (color != null) {
				setForeground(color);
			}
			setString(value.toString()+" %");
			setValue(n);
			return this;
	    }
		
		public void setLimits(Hashtable<Integer, Color> limitColors) {
			this.limitColors = limitColors;
			int i = 0;
			int n = limitColors.size();
		    limitValues = new int[n];
		    Enumeration<Integer> e = limitColors.keys();
		    while (e.hasMoreElements()) {
		    	limitValues[i++] = ((Integer) e.nextElement()).intValue();
		    }
		    sort(limitValues);
	    }

	    private Color getColor(int value) {
		    Color color = null;
		    if (limitValues != null) {
		    	int i;
		    	for (i = 0; i < limitValues.length; i++) {
		    		if (limitValues[i] < value) {
		    			color = (Color) limitColors.get(new Integer(limitValues[i]));
		    		}
			    }
		    }
		    return color;
	    }
	  
	    private void sort(int[] a) {
		  
		    int n = a.length;
		    for (int i = 0; i < n - 1; i++) {
		    	int k = i;
		    	for (int j = i + 1; j < n; j++) {
		    		if (a[j] < a[k]) {
		    			k = j;
		    	    }
		        }
		        int tmp = a[i];
		        a[i] = a[k];
		        a[k] = tmp;
		    }
	    }
	}
}


