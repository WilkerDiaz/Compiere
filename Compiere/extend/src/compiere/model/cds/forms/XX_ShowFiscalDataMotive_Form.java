package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *  Show Fiscal Data Rejection Motives
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_ShowFiscalDataMotive_Form extends CPanel

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
		
		POrder_ID = new Integer(Env.getCtx().get_ValueAsString("#XX_SHOWFISCALMOTIVE_ORDER_ID"));
		Env.getCtx().remove("#XX_SHOWFISCALMOTIVE_ORDER_ID");
	
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
	
	private static Integer POrder_ID=0;
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
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(650, 395));
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
	
		xScrollPane.getViewport().add(xTable, null);
		 
	    southPanel.validate();
	
	}   //  jbInit
	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "Description"),   ".", String.class),            //  1
		};
	
		xTable.prepareTable(layout, "", "", false, "");
		xTable.setAutoResizeMode(3);
		
		//  Visual
		CompiereColor.setBackground (this);
			
		//Cargo la tabla 1
		tableInit_option=0;
		tableInit();
		tableLoad (xTable);
		
		
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_FiscalDataMotives"));
		statusBar.setStatusDB(xTable.getRowCount());
		
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
	}   //  actionPerformed
	
	
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
	 *  <br>
	 *  The driving table is "hdr", e.g. for hdr.C_BPartner_ID=..
	 *  The line table is "lin", e.g. for lin.M_Product_ID=..
	 *  You use the dateColumn/qtyColumn variable directly as it is table specific.
	 *  <br>
	 *  The sql is dependent on MatchMode:
	 *  - If Matched - all (fully or partially) matched records are listed
	 *  - If Not Matched - all not fully matched records are listed
	 *  @param display (Invoice, Shipment, Order) see MATCH_*
	 *  @param matchToType (Invoice, Shipment, Order) see MATCH_*
	 */
	private static void tableInit ()
	{
		m_sql = new StringBuffer ();
	
		if(tableInit_option==0)
		{
			m_sql.append("SELECT tab.Name "
					+ "FROM XX_VMR_CANCELLATIONMOTIVE tab, XX_VMR_MOTIVEGROUP mg " +
					  "WHERE mg.XX_VMR_MOTIVEGROUP_ID=tab.XX_VMR_MOTIVEGROUP_ID " +
					  "AND mg.XX_USE='ENTRANCE' " +
					  "AND XX_VMR_CANCELLATIONMOTIVE_ID IN (" +
					  "SELECT XX_VMR_CANCELLATIONMOTIVE_ID " +
					  "FROM XX_VCN_ORDERFISCALRMOTIVE " +
					  "WHERE C_ORDER_ID="+POrder_ID +
					  ")");
					
					m_orderBy = " order by tab.Name";
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
	

}//End form