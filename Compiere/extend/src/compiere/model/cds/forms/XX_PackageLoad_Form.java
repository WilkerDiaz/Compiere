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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MWarehouse;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRDepartment;


/**
 *  Carga de Paquetes
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_PackageLoad_Form extends CPanel
	
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
		
		receipt_ID = new Integer(Env.getCtx().get_ValueAsString("#XX_RM_RECEIPT_ID"));
		Env.getCtx().remove("#XX_RM_RECEIPT_ID");
		
		MInOut inOut = new MInOut(Env.getCtx(), receipt_ID, null);

		order = new MOrder(Env.getCtx(), inOut.getC_Order_ID(), null);

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
	static CLogger 			log = CLogger.getCLogger(XX_PackageLoad_Form.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();

	static StringBuffer    m_sql = null;
	static String          m_groupBy = "";
	static String          m_orderBy = "";
	
	private CTextField packageNumber = new CTextField();
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CLabel DocNumber = new CLabel(Msg.translate(Env.getCtx(), "PackageCode"));
	private CButton bSave = new CButton();
	private CButton bReset = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private JScrollPane xScrollPane2 = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder(Msg.translate(Env.getCtx(), "LoadedPackages"));
	private TitledBorder xBorder2 = new TitledBorder(Msg.translate(Env.getCtx(), "NotLoadedPackages"));
	private MiniTable xTable = new MiniTable();
	private MiniTable xTable2 = new MiniTable();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private CLabel lWaiting = new CLabel(Msg.translate(Env.getCtx(), "WaitingForInput"));
	private String reason = "";
	private int packageCount = 0;
	private MOrder order = null;
	private int receipt_ID = 0;


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
		bSave.setVisible(false);
		
		bReset.setText(Msg.translate(Env.getCtx(), "Reset"));
		bReset.setPreferredSize(new Dimension(120,22));	
		bReset.setVisible(false);
		
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(300, 600));
		xScrollPane.setVisible(false);
		
		xScrollPane2.setBorder(xBorder2);
		xScrollPane2.setPreferredSize(new Dimension(300, 200));
		xScrollPane2.setVisible(false);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		centerPanel.add(xScrollPane2,  BorderLayout.SOUTH);

		xScrollPane.getViewport().add(xTable, null);
		xScrollPane2.getViewport().add(xTable2, null);

		northPanel.add(DocNumber,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 10, 5), 0, 0));

		packageNumber.setPreferredSize(new Dimension(220,20));
		packageNumber.setMaxLength(40);
		
		northPanel.add(packageNumber,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 10, 5), 0, 0));
		
		southPanel.add(lWaiting,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(300, 240, 300, 240), 0, 0));
		
		 southPanel.add(bReset,   new GridBagConstraints(100, 0, 0, 0, 0.0, 0.0
					,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 10, 200), 0, 0)); 
		 
	    southPanel.add(bSave,   new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 200, 10, 0), 0, 0));
	    
	    southPanel.validate();

	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "Qty"),   ".", Integer.class),            //  1
			new ColumnInfo(Msg.translate(Env.getCtx(), "Package"),   ".", String.class),         //  2
		};
		
		ColumnInfo[] layout2 = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "Qty"),   ".", Integer.class),            //  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "Package"),   ".", String.class),         //  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "Error"),   ".", String.class),         //  2
			};
		

		xTable.prepareTable(layout, "", "", false, "");
		xTable.getColumnModel().getColumn(0).setMaxWidth(55);
		xTable.setAutoResizeMode(3);
		
		xTable2.prepareTable(layout2, "", "", false, "");
		xTable2.getColumnModel().getColumn(0).setMaxWidth(55);
		xTable2.setAutoResizeMode(3);
		
		
		//  Visual
		CompiereColor.setBackground (this);

		//  Listener
		xTable.getSelectionModel().addListSelectionListener(this);
		
		m_frame.setDefaultCloseOperation(0); //DO_NOTHING_ON_CLOSE
		//m_frame.setAlwaysOnTop(true);
		
		m_frame.addWindowListener( new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				//m_frame.requestFocus();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				boolean answer = ADialog.ask(m_WindowNo, m_frame.getContentPane(), Msg.getMsg(Env.getCtx(), "ExitPackages"));
				
				if(answer){
					dispose();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				packageNumber.requestFocus();
			}
		});

		
		packageNumber.addKeyListener (new KeyAdapter () {
			          
			public void keyPressed (KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER && packageNumber.getText().length() > 0) {
					xScrollPane.setVisible(true);
					packageNumber.setValue(packageNumber.getText().trim());
					cmd_preLoad();
		        }
			}
			
			public void keyTyped(KeyEvent e)
			{
				char input = e.getKeyChar();
				
				if(input == 'b'){
					e.setKeyChar('B');
					input = 'B';
				}
				else if(input == 'p'){
					e.setKeyChar('P');
					input = 'P';
				}
			}
			
	     } // end anonymous class
		);
		
		bSave.addActionListener(this);
		bReset.addActionListener(this);
				
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "LoadedPackagesQty"));
		statusBar.setStatusDB(packageCount);
		
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
		if (e.getSource() == bSave){
			cmd_Save();
		}
		else{
		if (e.getSource() == bReset)
			cmd_Reset();
		}
		
	}   //  actionPerformed

	
	/**
	 *  pre Load Packages
	 */
	private void cmd_preLoad()
	{	
		
		if(packageNumber.getText().length()>0){
			
			boolean correct = true;
			boolean firstTime1 = true;
			boolean firstTime2 = true;
			
			String codeNumber="";
			int Qty=0;
			if(packageNumber.getText().length()==28){
			
				codeNumber = packageNumber.getText();
				
				//Desconcateno el codigo de barras leido
				String type = codeNumber.substring(0, 1);   //1
				int cd_BPartner = new Integer(codeNumber.substring(1, 8));   //7
				int cd_POrder = new Integer(codeNumber.substring(8, 16));      //8
				int cd_Department = new Integer(codeNumber.substring(16, 19));      //3
				int cd_Store = new Integer(codeNumber.substring(19, 22));      //3
				int cd_packageNo = new Integer(codeNumber.substring(22, 25));      //3
				//int cd_packageByPO = new Integer(codeNumber.substring(25, 28));      //3
				
				if(!type.equals("B") && !type.equals("P")){
					correct=false;
					reason = "El código no contiene indentificador B/P";
				}
				
				
				//Verifico que no se repita el mismo bulto
				if(type.equals("B")){
					
					String codeNumberAux="";
					for(int i=0; i<xTable.getRowCount(); i++){
						
						codeNumberAux = xTable.getValueAt(i, 1).toString();
						if(codeNumberAux.equals(codeNumber)){
							correct=false;
							reason="El bulto ya fue registrado";
						}
					}
				}
				
				if(correct){
					
					//Ahora debo verificar todos los datos del codigo de barra (deben coincidir con la O/C de la recepcion)
					
					//Verifico el Proveedor
					MBPartner realBPartner = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
					
					int aux1 = new Integer(realBPartner.getValue());
					int aux2 = new Integer(cd_BPartner);
					if(aux1!=aux2){
						correct = false;
						reason = "El proveedor no coincide";
					}
					
					//Verifico la O/C
		
					aux1 = new Integer(order.getDocumentNo());
					aux2 = new Integer(cd_POrder);
					if(aux1!=aux2){
						correct = false;
						reason = "El número de O/C no coincide";
					}
					
					//Verifico el departamento
					MVMRDepartment realDepartment= new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
					
					aux1 = new Integer(realDepartment.getValue());
					aux2 = new Integer(cd_Department);
	
					if(aux1!=aux2){
						correct = false;
						reason = "El departamento no coincide";
					}
					
					//Verifico la tienda
					MWarehouse realStore= new MWarehouse( Env.getCtx(), order.getM_Warehouse_ID(), null);
					
					aux1 = new Integer(realStore.getValue());
					aux2 = new Integer(cd_Store);
					if(aux1 != aux2){
						correct = false;
						reason = "La tienda no coincide";
					}
					
					//Numero de bultos
					if(type.equals("B")){
						Qty=1;
					}
					else{
						Qty=cd_packageNo;
					}
				}
			}
			else if(packageNumber.getText().length()>28){
				correct=false;
				reason = "El código contiene más de 28 caracteres";
				
			}else{
				correct=false;
				reason = "El código contiene menos de 28 caracteres";
			}
			
			if(correct){ //Si todo está correcto
				if(firstTime1){
					bReset.setVisible(true);
					xScrollPane.setVisible(true);
					lWaiting.setVisible(false);
					bSave.setVisible(true);
					firstTime1=false;
					this.repaint();
				}
				cmd_correctLoad(Qty);
			}else{
				if(firstTime2){
					bReset.setVisible(true);
					xScrollPane2.setVisible(true);
					lWaiting.setVisible(false);
					bSave.setVisible(false);
					bSave.setVisible(true);
					firstTime1=false;
				}
				cmd_incorrectLoad(codeNumber);
			}
		}
		
		packageNumber.setValue("");
	} 
	
	/**
	 *  Load Packages
	 */
	private void cmd_correctLoad(int Qty)
	{	
		TableModel model = xTable.getModel();
		xTable.setRowCount(xTable.getRowCount()+1);
		
		int pos = xTable.getRowCount();
		if(xTable.getRowCount()!=1){
			for(int i=1; i<pos; i++){
								
				model.setValueAt(xTable.getValueAt(pos-1-i, 0), pos-i, 0);
				model.setValueAt(xTable.getValueAt(pos-1-i, 1), pos-i, 1);
			}
		}
		
		model.setValueAt(new Integer(Qty), 0, 0);
		model.setValueAt(packageNumber.getText(), 0, 1);
			
		packageCount=0;
		for(int i=0; i<xTable.getRowCount(); i++){
			packageCount = packageCount + new Integer(xTable.getValueAt(i, 0).toString());
		}
		statusBar.setStatusDB(packageCount);
	} 
	
	/**
	 *  Load wrong Packages
	 */
	private void cmd_incorrectLoad(String codeNumber)
	{	
		
		String codeNumberAux="";
		int rep=-1;
		for(int i=0; i<xTable2.getRowCount(); i++){
				
			codeNumberAux = xTable2.getValueAt(i, 1).toString();
			if(codeNumberAux.equals(codeNumber)){
				rep=i;
			}
		}
		
		if(rep==-1){
			TableModel model = xTable2.getModel();
			xTable2.setRowCount(xTable2.getRowCount()+1);
			
			int pos = xTable2.getRowCount();
			if(xTable2.getRowCount()!=1){
				for(int i=1; i<pos; i++){
									
					model.setValueAt(xTable2.getValueAt(pos-1-i, 0), pos-i, 0);
					model.setValueAt(xTable2.getValueAt(pos-1-i, 1), pos-i, 1);
					model.setValueAt(xTable2.getValueAt(pos-1-i, 2), pos-i, 2);
				}
			}
			
			model.setValueAt(1, 0, 0);
			model.setValueAt(packageNumber.getText(), 0, 1);
			model.setValueAt(reason, 0, 2);
		}
		else{
			TableModel model = xTable2.getModel();
			
			model.setValueAt(new Integer(xTable2.getValueAt(rep, 0).toString())+1, rep, 0);

		}
	
	} 
		
	/**
	 *  Confirm Button Pressed
	 */
	private void cmd_Save()
	{					
		boolean answer = ADialog.ask(m_WindowNo, m_frame.getContentPane(), Msg.getMsg(Env.getCtx(), "SavePackages", new String[] {""+packageCount+""}));
		
		if(answer){
			
			MInOut inOut = new MInOut(Env.getCtx(), receipt_ID, null);
			inOut.setXX_ScannedPackages(packageCount);
			
			if(packageCount>0){
				inOut.setXX_PackageIdentification(true);
			}else{
				inOut.setXX_PackageIdentification(false);
			}
			
			inOut.setXX_ScanPackages("Y");
			inOut.save();
			
			dispose();
		}
	}
	
	/**
	 *  Reset Button Pressed
	 */
	private void cmd_Reset()
	{
		boolean answer = ADialog.ask(m_WindowNo, m_frame.getContentPane(), Msg.getMsg(Env.getCtx(), "ClearPackages"));
		
		if(answer){
		
			DefaultTableModel model = (DefaultTableModel)xTable.getModel();
			
			while (model.getRowCount()>0){
				model.removeRow(0);
			}
			
			DefaultTableModel model2 = (DefaultTableModel)xTable2.getModel();
			
			while (model2.getRowCount()>0){
				model2.removeRow(0);
			}
			
			bReset.setVisible(false);
			bSave.setVisible(false);
			xScrollPane.setVisible(false);
			xScrollPane2.setVisible(false);
			lWaiting.setVisible(true);
		}
		packageNumber.requestFocus();
	}

	/**************************************************************************
	 *  List Selection Listener
	 *  @param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		
		if (e.getValueIsAdjusting())
			return;

	}   //  valueChanged
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
	}
	
	
}//End form

