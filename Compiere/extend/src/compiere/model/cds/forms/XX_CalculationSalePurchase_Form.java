package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.model.MPInstance;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.processes.XX_CalculationSalePurchase;

/**
 *  @author dpellegrino - Modificado por Jessica Mendoza
 *  @version    
 */
public class XX_CalculationSalePurchase_Form extends CPanel
	
	implements FormPanel, ActionListener, TableModelListener, ListSelectionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame){
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		try{
			//	UI
			jbInit();
			dynInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			
		}catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}
	}	

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_CalculationSalePurchase_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private static CTextField fiscalyear = new CTextField(); //  texto
	private static CTextField StartDate = new CTextField(); //  texto
	private static CTextField EndDate = new CTextField(); // texto
	private static CCheckBox closesalepurchase = new CCheckBox(Msg.getMsg(Env.getCtx(), "XX_CloseSalePurchase")); 
	private static CLabel labelficalyear = new CLabel(Msg.getMsg(Env.getCtx(), "XX_FiscalYear")); 
	private static CLabel labelStartDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateFrom")); 
	private static CLabel labelEndDate = new CLabel(Msg.getMsg(Env.getCtx(), "DateTo")); 
	//private static CLabel labelclosesalepurchase = new CLabel(Msg.getMsg(Env.getCtx(), "XX_CloseSalePurchase")); 
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
		
	private CButton bConfirm = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);	
	private CPanel xPanel = new CPanel();		
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	
	String periodid = null;

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
	private void jbInit() throws Exception{
		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		centerPanel.setLayout(centerLayout);
		southPanel.setLayout(southLayout);
		
		xPanel.setLayout(xLayout);
		
		northPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				Msg.getMsg(Env.getCtx(), "XX_CloseSalePurchase")));
	
		bConfirm.setText(Msg.translate(Env.getCtx(), "XX_Confirm"));
		bConfirm.setEnabled(true);
		fiscalyear.setEnabled(true);
		StartDate.setEnabled(true);
		EndDate.setEnabled(true);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		northPanel.add(labelficalyear, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 5, 5), 0, 0));	
		
		fiscalyear.setPreferredSize(new Dimension(120,20));
		northPanel.add(fiscalyear, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 12, 10, 5), 0, 0));
		
		northPanel.add(labelStartDate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 5, 5), 0, 0));
		
		StartDate.setPreferredSize(new Dimension(120,20));
		northPanel.add(StartDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(10, 10, 8, 3), 0, 0));
		
		northPanel.add(labelEndDate, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 5, 5), 0, 0));
		
		EndDate.setPreferredSize(new Dimension(120,20));
		northPanel.add(EndDate, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 12, 10, 5), 0, 0));
		
		southPanel.add(closesalepurchase, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 20));
		southPanel.add(bConfirm, new GridBagConstraints(4, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 12, 5, 12), 0, 0));
	    
	    southPanel.validate(); 
	} 

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit(){	
		campo1();
		CompiereColor.setBackground (this);
		bConfirm.addActionListener(this);
		statusBar.setStatusLine("");
		statusBar.setStatusDB("");	
	}   

	/**
	 * 	Dispose
	 */
	public void dispose(){
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;		
	}
	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e){		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bConfirm)
			cmd_Confirm();
				
	} 

	/**
	 *  Search Button Pressed
	 */
	private void cmd_Confirm(){
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		String mes = null;
		String year = null;
		Boolean close = false;

		mes = (StartDate.getText()).substring(3, 5);
		year = (StartDate.getText()).substring(6, 10);
		close =(Boolean)closesalepurchase.getValue();
				 
		Env.getCtx().setContext("#XX_MONTHDATE",mes);
		Env.getCtx().setContext("#XX_YEARDATE",year);
		Env.getCtx().setContext("#XX_PERIODID",periodid);
		Env.getCtx().setContext("#XX_CLOSESP",close);

		MPInstance mpi = new MPInstance(Env.getCtx(), 1000132, 0);
        mpi.save();
        XX_CalculationSalePurchase sai = new XX_CalculationSalePurchase();
        ProcessInfo procinfo = new ProcessInfo("Proceso Ejemplo", 1000132);
        procinfo.setAD_PInstance_ID(mpi.get_ID());
        procinfo.setRecord_ID(mpi.getRecord_ID());
        procinfo.setAD_Process_ID(1000132);
        
        /****Jessica Mendoza****/
        ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		list.add (new ProcessInfoParameter("C_Year_ID", (String)year, null, year, null));
		list.add (new ProcessInfoParameter("C_Period_ID", (String)mes, null, mes, null));
		list.add (new ProcessInfoParameter("XX_CloseSalePurchase", (Boolean)close, null, String.valueOf(close), null));
		ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
        list.toArray(pars);           
        procinfo.setParameter(pars);
        sai.startProcess(Env.getCtx(), procinfo, null);
        /****Fin código - Jessica Mendoza****/
        
        closesalepurchase.setValue(false);
        campo1();
        m_frame.setCursor(Cursor.getDefaultCursor());
        
        ADialog.info(m_WindowNo, this.mainPanel, Msg.getMsg(Env.getCtx(), "ProcessOK"));		
	} 

	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e){

	}

	@Override
	public void tableChanged(TableModelEvent e) {
	}
		
	private void campo1(){
		String SQL = ("SELECT TO_CHAR (A.StartDate, 'DD/MM/YYYY') AS StartDate, " +
				"TO_CHAR (A.EndDate, 'DD/MM/YYYY') AS EndDate, B.FISCALYEAR AS FISCALYEAR, A.C_Period_ID AS ID " +
				"FROM C_Period A, C_Year B " +
				"WHERE A.XX_CloseSalePurchase ='N' " +
				"AND A.C_Year_ID = B.C_Year_ID " +
				"AND rownum = 1 " +
				"order by A.C_Year_ID, A.PeriodNo ");
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    if(rs.next()){
		    	fiscalyear.setText(rs.getString("FISCALYEAR"));
		    	StartDate.setText(rs.getString("StartDate"));
		    	EndDate.setText(rs.getString("EndDate"));
		    	periodid = rs.getString("ID");
		    }
		    rs.close();
		    pstmt.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}


