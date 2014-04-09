package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import compiere.model.cds.X_Ref_XX_Ref_DispatchGuideStatus;
import compiere.model.cds.X_Ref_XX_Ref_OrderDetailPackageStatus;
import compiere.model.cds.X_Ref_XX_Ref_TypeDispatchGuide;
import compiere.model.cds.X_XX_VLO_DetailDispatchGuide;
import compiere.model.cds.X_XX_VLO_DispatchGuide;
import compiere.model.cds.X_XX_VLO_OrderDetailPackage;
import compiere.model.cds.X_XX_VLO_Travel;
import compiere.model.cds.X_XX_VMR_Order;


/**
 *  Carga de Paquetes
 *
 *  @author     Jorge Pires
 *  @version    
 */
public class XX_OrderPackageLoad_Form extends CPanel
	
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
		
		dg_ID = new Integer(Env.getCtx().get_ValueAsString("#XX_DG_DISPATCH_ID"));
		Env.getCtx().remove("#XX_DG_DISPATCH_ID");
		
		//X_XX_VLO_DispatchGuide dispatchGuide = new X_XX_VLO_DispatchGuide(Env.getCtx(), dg_ID, null);

//		order = new MOrder(Env.getCtx(), inOut.getC_Order_ID(), null);

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
	static CLogger 			log = CLogger.getCLogger(XX_OrderPackageLoad_Form.class);

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
//	private MOrder order = null;
	private Integer dg_ID = 0;


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
				
				if(input == 'd'){
					e.setKeyChar('D');
					input = 'D';
				}
				else if(input == 'p'){
					e.setKeyChar('P');
					input = 'P';
				}else if(input == 'r'){
					e.setKeyChar('R');
					input = 'R';
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

	private String buscarDetalleDispatchGuideConPedido(String pedido, Integer DispatchGuide){
		String XX_VLO_DETAILDISPATCHGUIDE_ID = null;
		
		String sql = "SELECT XX_VLO_DETAILDISPATCHGUIDE_ID " +
					 "FROM XX_VLO_DETAILDISPATCHGUIDE " +
					 "WHERE XX_VLO_DISPATCHGUIDE_ID = " +DispatchGuide+" "+
					 "AND XX_VMR_ORDER_ID = " +pedido+ " ";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()){
				XX_VLO_DETAILDISPATCHGUIDE_ID = rs.getString("XX_VLO_DETAILDISPATCHGUIDE_ID");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.fine(e.getMessage());
			return null;
		}
		
		return XX_VLO_DETAILDISPATCHGUIDE_ID;		
	}

	private String buscarPedidoID(String becoOrderCorrelative){
		String XX_VMR_ORDER_ID = null;
		
		String sql = "SELECT XX_VMR_ORDER_ID " +
					 "FROM XX_VMR_ORDER " +
					 "WHERE XX_ORDERBECOCORRELATIVE = '" +becoOrderCorrelative+ "'";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()){
				XX_VMR_ORDER_ID = rs.getString("XX_VMR_ORDER_ID");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.fine(e.getMessage());
			return null;
		}
		
		return XX_VMR_ORDER_ID;		
	}
	
	private String buscarBultoID(String value){
		String XX_VMR_ORDER_ID = null;
		
		String sql = "SELECT XX_VLO_ORDERDETAILPACKAGE_ID " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE " +
					 "WHERE VALUE = '" +value+ "'";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()){
				XX_VMR_ORDER_ID = rs.getString("XX_VLO_ORDERDETAILPACKAGE_ID");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.fine(e.getMessage());
			return null;
		}
		
		return XX_VMR_ORDER_ID;		
	}
	
	private Vector<String> buscarPedidoporDispatchGuide (String dispatchGuide){
		Vector<String> placedOrdersAux = new Vector<String>();
		String sql = "SELECT DDG.XX_VMR_ORDER_ID " +
					 "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
					 "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '" +X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+ "' " +
					 "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+ dispatchGuide +" " +
					 "AND DDG.XX_VMR_ORDER_ID IS NOT NULL";

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				placedOrdersAux.add(rs.getString("XX_VMR_ORDER_ID"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
		}
		
		return placedOrdersAux;
	}
	
	private Vector<String> buscarPedidoIDporDispatchGuide (String dispatchGuide){
		Vector<String> placedOrdersAux = new Vector<String>();
		String sql = "SELECT DDG.XX_VLO_DETAILDISPATCHGUIDE_ID " +
					 "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
					 "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '" +X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+ "' " +
					 "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+ dispatchGuide +" " +
					 "AND DDG.XX_VMR_ORDER_ID IS NOT NULL";

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				placedOrdersAux.add(rs.getString("XX_VLO_DETAILDISPATCHGUIDE_ID"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
		}
		
		return placedOrdersAux;
	}
	
	private Vector<String> buscarBultosPedido (String pedido){
		Vector<String> bultosAux = new Vector<String>();
		String sql = "SELECT DP.VALUE " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE DP " +
					 "WHERE DP.XX_VMR_ORDER_ID = " +pedido+ " ";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				bultosAux.add(rs.getString("VALUE"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
			return new Vector<String>();
		}
		
		return bultosAux;
	}
	
	private Vector<String> buscarBultosRegistrados (String pedido, Integer Status){
		Vector<String> bultosAux = new Vector<String>();
		String sql = "SELECT DP.VALUE " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE DP " +
					 "WHERE DP.XX_VMR_ORDER_ID = " +pedido+ " " +
					 "AND DP.XX_VLO_DISPATCHGUIDE_ID <> " + dg_ID +" "+
					 "AND DP.XX_ORDERDETAILPACKAGESTATUS = '";
		
		if(Status == 0){
			sql = sql + X_Ref_XX_Ref_OrderDetailPackageStatus.APROBADO.getValue()+"'";
		}else if(Status == 1){
			sql = sql + X_Ref_XX_Ref_OrderDetailPackageStatus.RECIBIDO.getValue()+"'";
		}
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				bultosAux.add(rs.getString("VALUE"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
			return new Vector<String>();
		}
		
		return bultosAux;
	}
	
	private Vector<Integer> buscarBultosID (String pedido, Integer dispatchGuide){
		Vector<Integer> bultosAux = new Vector<Integer>();
		String sql = "SELECT DP.XX_VLO_ORDERDETAILPACKAGE_ID " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE DP " +
					 "WHERE DP.XX_VMR_ORDER_ID = " +pedido+ " " +
					 "AND DP.XX_VLO_DISPATCHGUIDE_ID = "+dispatchGuide+"";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				bultosAux.add(rs.getInt("XX_VLO_ORDERDETAILPACKAGE_ID"));
			}
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.fine(e.getMessage());
			return new Vector<Integer>();
		}
		
		return bultosAux;
	}
	
	/**
	 *  pre Load Packages
	 */
	private void cmd_preLoad(){	
		
		if(packageNumber.getText().length()>0){
			
			boolean correct = true;
			boolean firstTime1 = true;
			boolean firstTime2 = true;
			
			String codeNumber="";
			int Qty=1;
			if(packageNumber.getText().length() == 21){
			
				codeNumber = packageNumber.getText();
				
				//Desconcateno el codigo de barras leido
				String placedOrderType = codeNumber.substring(0, 1);   
				String pedido = new String (codeNumber.substring(0, 11));   
				String bulto = new String(codeNumber.substring(11)); 
				
				Vector<String> pedidosDispatchGuide = null;
				Vector<String> bultosPerdido = null;
				Vector<String> bultosPerdidoRegistrado = null;

				if(!placedOrderType.equals("D") && !placedOrderType.equals("P") && !placedOrderType.equals("R")){
					correct=false;
					reason = "El código no contiene indentificador D/P/R";
				}
				
				
				//Verifico que no se repita el mismo bulto
				String codeNumberAux="";
				for(int i=0; i<xTable.getRowCount(); i++){
					
					codeNumberAux = xTable.getValueAt(i, 1).toString();
					if(codeNumberAux.equals(codeNumber)){
						correct=false;
						reason="El bulto ya fue registrado";
					}
				}
				
				if(buscarPedidoID(pedido) == null){
					correct=false;
					reason = "No existe el Pedido";
				}else{
					pedidosDispatchGuide = buscarPedidoporDispatchGuide(dg_ID.toString());
					if(!pedidosDispatchGuide.contains(buscarPedidoID(pedido))){
						correct = false;
						reason = "El pedido no esta en esta Guia de Despacho";
					}else{
						bultosPerdido = buscarBultosPedido(buscarPedidoID(pedido));
						if(!bultosPerdido.contains(new Integer(bulto).toString())){
							correct = false; 
							reason = "El bulto no pertenece al Pedido";
						}else{
							X_XX_VLO_DispatchGuide dispatchGuide = new X_XX_VLO_DispatchGuide(Env.getCtx(), dg_ID, null);
							
							if(dispatchGuide.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue())){
								bultosPerdidoRegistrado = buscarBultosRegistrados(buscarPedidoID(pedido), 0);
								if(bultosPerdidoRegistrado.contains(new Integer(bulto).toString())){
									correct = false; 
									reason = "El bulto esta Registrado en otra Guia de Despacho";
								}
								bultosPerdidoRegistrado = buscarBultosRegistrados(buscarPedidoID(pedido), 1);
								if(bultosPerdidoRegistrado.contains(new Integer(bulto).toString())){
									correct = false; 
									reason = "El bulto esta Registrado en otra Guia de Despacho";
								}
							}else if(dispatchGuide.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue())){
								bultosPerdidoRegistrado = buscarBultosRegistrados(buscarPedidoID(pedido), 1);
								if(bultosPerdidoRegistrado.contains(new Integer(bulto).toString())){
									correct = false; 
									reason = "El bulto esta Registrado en otra Guia de Despacho";
								}
							}
							
						}
					}
					
				}

			}
			else if(packageNumber.getText().length()>21){
				correct=false;
				reason = "El código contiene más de 21 caracteres";
				
			}else{
				correct=false;
				reason = "El código contiene menos de 21 caracteres";
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
		X_XX_VLO_DispatchGuide dispatchGuide = new X_XX_VLO_DispatchGuide(Env.getCtx(), dg_ID, null);
		X_XX_VLO_DetailDispatchGuide detailDispatchGuide = null;
		X_XX_VMR_Order pedido = null;
		X_XX_VLO_OrderDetailPackage detallePedido = null;
		X_XX_VLO_Travel travel = null;
		
		boolean answer = ADialog.ask(m_WindowNo, m_frame.getContentPane(), Msg.getMsg(Env.getCtx(), "SavePackages", new String[] {""+packageCount+""}));
		
		if(answer){
		
			//inicializo
			Vector<String> pedidosDispatchGuide = buscarPedidoporDispatchGuide(dg_ID.toString());
			Vector<String> pedidosIDDispatchGuide = buscarPedidoIDporDispatchGuide(dg_ID.toString());
			for (int j = 0; j < pedidosDispatchGuide.size(); j++) {
				Vector<Integer> bultosID = buscarBultosID(pedidosDispatchGuide.elementAt(j), dg_ID);
				
				if(dispatchGuide.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue())){
					travel = new X_XX_VLO_Travel(Env.getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), null);
					travel.setXX_TotalPackagesSent(travel.getXX_TotalPackagesSent()-bultosID.size());
					travel.save();
					
					for (int k = 0; k < bultosID.size(); k++) {
						detallePedido = new X_XX_VLO_OrderDetailPackage(Env.getCtx(), bultosID.elementAt(k) , null);
						detallePedido.setXX_OrderDetailPackageStatus(X_Ref_XX_Ref_OrderDetailPackageStatus.PENDIENTE.getValue());
						detallePedido.setXX_VLO_DispatchGuide_ID(-1);
						detallePedido.save();	
					}
					
					detailDispatchGuide = new X_XX_VLO_DetailDispatchGuide(Env.getCtx(), new Integer(pedidosIDDispatchGuide.elementAt(j)) , null);
					detailDispatchGuide.setXX_PackagesSent(0);
					detailDispatchGuide.save();
					
				}else if(dispatchGuide.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue())){
					travel = new X_XX_VLO_Travel(Env.getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), null);
					travel.setXX_TotalPackagesReceive(travel.getXX_TotalPackagesReceive()-bultosID.size());
					travel.save();
					
					for (int k = 0; k < bultosID.size(); k++) {
						detallePedido = new X_XX_VLO_OrderDetailPackage(Env.getCtx(), bultosID.elementAt(k) , null);
						detallePedido.setXX_OrderDetailPackageStatus(X_Ref_XX_Ref_OrderDetailPackageStatus.APROBADO.getValue());
						detallePedido.save();	
					}
					
					detailDispatchGuide = new X_XX_VLO_DetailDispatchGuide(Env.getCtx(), new Integer(pedidosIDDispatchGuide.elementAt(j)) , null);
					detailDispatchGuide.setXX_PackagesReceived(0);
					detailDispatchGuide.save();
				}
			}
			
			detallePedido = null;
			detailDispatchGuide = null;
			//termino inicializacion
			
			
			
			
			String codeNumber = new String();
			
			if(dispatchGuide.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue())){				
				dispatchGuide.setXX_TotalPackagesSent(packageCount);
				travel = new X_XX_VLO_Travel(Env.getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), null);
				travel.setXX_TotalPackagesSent(packageCount);
				travel.save();
				
				for(int i=0; i<xTable.getRowCount(); i++){
					codeNumber = xTable.getValueAt(i, 1).toString();
					String placedOrder = new String (codeNumber.substring(0, 11));   
					String orderDetailPackage = new String(codeNumber.substring(11));      
					
					pedido = new X_XX_VMR_Order(Env.getCtx(), new Integer(buscarPedidoID(placedOrder)), null);
					
					detailDispatchGuide = new X_XX_VLO_DetailDispatchGuide(Env.getCtx(), new Integer (buscarDetalleDispatchGuideConPedido(""+pedido.get_ID(), dg_ID )) , null);
					detailDispatchGuide.setXX_PackagesSent(detailDispatchGuide.getXX_PackagesSent()+1);
					detailDispatchGuide.save();
					
					detallePedido = new X_XX_VLO_OrderDetailPackage(Env.getCtx(), new Integer(buscarBultoID(new Integer(orderDetailPackage).toString())) , null);
					detallePedido.setXX_VLO_DispatchGuide_ID(dg_ID);
					detallePedido.setXX_OrderDetailPackageStatus(X_Ref_XX_Ref_OrderDetailPackageStatus.APROBADO.getValue());
					detallePedido.save();			
				}	
			}else if(dispatchGuide.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue())){
				dispatchGuide.setXX_TotalPackagesReceive(packageCount);
				travel = new X_XX_VLO_Travel(Env.getCtx(), dispatchGuide.getXX_VLO_Travel_ID(), null);
				travel.setXX_TotalPackagesReceive(packageCount);
				travel.save();
				
				for(int i=0; i<xTable.getRowCount(); i++){
					codeNumber = xTable.getValueAt(i, 1).toString();
					String placedOrder = new String (codeNumber.substring(0, 11));   
					String orderDetailPackage = new String(codeNumber.substring(11));      
					
					pedido = new X_XX_VMR_Order(Env.getCtx(), new Integer(buscarPedidoID(placedOrder)), null);
					
					detailDispatchGuide = new X_XX_VLO_DetailDispatchGuide(Env.getCtx(), new Integer (buscarDetalleDispatchGuideConPedido(""+pedido.get_ID(), dg_ID)) , null);
					detailDispatchGuide.setXX_PackagesReceived(detailDispatchGuide.getXX_PackagesReceived()+1);
					detailDispatchGuide.save();
					
					detallePedido = new X_XX_VLO_OrderDetailPackage(Env.getCtx(), new Integer(buscarBultoID(new Integer(orderDetailPackage).toString())) , null);
					detallePedido.setXX_VLO_DispatchGuide_ID(dg_ID);
					detallePedido.setXX_OrderDetailPackageStatus(X_Ref_XX_Ref_OrderDetailPackageStatus.RECIBIDO.getValue());
					detallePedido.save();	
				}
				
			}

			dispatchGuide.save();
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

