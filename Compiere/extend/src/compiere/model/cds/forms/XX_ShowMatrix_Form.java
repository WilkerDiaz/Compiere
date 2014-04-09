package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.Date;
import java.util.Vector;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.X_M_AttributeValue;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.X_XX_VMR_PO_LineRefProv;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.cds.processes.XX_MyTableModel;


/**
 *  Convert Vendors Reference to BECO products
 *
 *  @author     José Trías
 *  @version    
 */
public class XX_ShowMatrix_Form extends CPanel
	
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

		LineRefProv_ID = new Integer(Env.getCtx().get_ValueAsString("#XX_RM_LINEREFPROV_ID"));
		LineRefProv = new X_XX_VMR_PO_LineRefProv(Env.getCtx(),LineRefProv_ID,null);
		GenerateMatrix = Env.getCtx().get_ValueAsString("#GenerateMatrix");
		
		Env.getCtx().remove("#XX_RM_LINEREFPROV_ID");
		Env.getCtx().remove("#GenerateMatrix");
		
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
	
	private int totalQty = 0;
	MVMRPOLineRefProv line = null;

	private X_XX_VMR_PO_LineRefProv LineRefProv = null;
	private Integer LineRefProv_ID=null;
	private String GenerateMatrix=null;
	
	static Ctx ctx_aux = new Ctx();
	
	static Integer associatedReference_ID;
	public static Integer selectedReference_ID;
	private int PU;
	private int PS;
	private boolean multiple=true;
	
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private CButton bSave = new CButton();
	private CPanel southPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder = new TitledBorder(Msg.translate(Env.getCtx(), "ReferenceCombinations"));
	private XX_MiniTableMatrix xTable = new XX_MiniTableMatrix();
	private CPanel xPanel = new CPanel();
	private Vector <Integer> columnsIDs;
	private Vector <Integer> rowsIDs;
	private int packageMultiple;
	private Object[][] dataAux = null;
	
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
		//northPanel.setLayout(northLayout);
		
		bSave.setText(Msg.translate(Env.getCtx(), "SaveExit"));
		//southPanel.setLayout(southLayout);
		centerPanel.setLayout(centerLayout);

		xScrollPane.setBorder(xBorder);
		
		//veo el tamaño de los vectores para setear el tamaño del scrollpane
		columnsIDs = columnsVector(LineRefProv_ID);
		rowsIDs = rowsVector(LineRefProv_ID);
		
		//Tamaño de las filas
		int tam=0;
		if (rowsIDs.size()==0){tam=90;}
		if (rowsIDs.size()==1){tam=90;} 
		if (rowsIDs.size()==2){tam=138;} 
		if (rowsIDs.size()==3){tam=186;} 
		if (rowsIDs.size()==4){tam=234;} 
		if (rowsIDs.size()==5){tam=282;} 
		if (rowsIDs.size()==6){tam=330;} 
		if (rowsIDs.size()==7){tam=378;} 
		if (rowsIDs.size()==8){tam=426;} 
		if (rowsIDs.size()==9){tam=474;}
		if (rowsIDs.size()==10){tam=522;}
		
		xScrollPane.setPreferredSize(new Dimension(((columnsIDs.size()+2)*90), tam));
		dataAux = new Object[rowsIDs.size()][columnsIDs.size()];
		
		xPanel.setLayout(xLayout);
		mainPanel.add(northPanel,  BorderLayout.NORTH);
	
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xPanel, BorderLayout.CENTER);
		
		centerPanel.add(xScrollPane,  BorderLayout.NORTH);
		xScrollPane.getViewport().add(xTable, null);
		
		southPanel.add(bSave,   new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 5, 12), 0, 0));
		
		}   //  jbInit

	
	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit()
	{	
		
		//creacion de la tabla que simula los row Headers
		String[] nullHeader = null;
		
		String[] rowsNames = rowsNames(rowsIDs);
		
		if(GenerateMatrix.equals("Y")){
			
			//si la matriz esta vacia la crea, si no, agrega los campos adicionales si existen
			if(emptyData()){
			
				createData(columnsIDs,rowsIDs,columnsIDs.size(),rowsIDs.size());
			}else{
				
				Integer[] tamano=tam(); // se consulta el tamaño de la matriz actual
				int tam1=columnsIDs.size()-1 ;
				int tam2=((rowsIDs.size())-1);
				
				if(tamano[2]==0 && rowsNames.length!=0){ //si el Value2 era 0 originalmente (no habia caracteristica 2)
					deleteData();
					createData(columnsIDs,rowsIDs,columnsIDs.size(),rowsIDs.size());
				}else{
		
					//si el tamaño difiere del actual se agregan las columnas nuevas
					if(tamano[0]!=tam1 || tamano[1]/3!=tam2){
						addData(columnsIDs,rowsIDs,tamano[0],tamano[1]/3,tam1,tam2);	
					}
				}
			}
			
			LineRefProv.setXX_DeleteMatrix("Y");
			LineRefProv.setXX_ShowMatrix("Y");
			LineRefProv.setXX_GenerateMatrix("N");
			LineRefProv.save();
		}
		
		
		Object[][] objectRowNames = {};
		
		if(rowsIDs.size()==0){
			
			nullHeader = new String[1];
			nullHeader[0]="";
			
			objectRowNames = new Object[3][1];
			objectRowNames[0][0]="CC";
			objectRowNames[1][0]="CO";
			objectRowNames[2][0]="CV";
			
		}else{
			
			nullHeader = new String[2];
			nullHeader[0]="";
			nullHeader[1]="";
			
			objectRowNames = new Object[rowsIDs.size()*3][2];
	
			boolean flagCC=true;
			boolean flagCO=false;
			boolean flagCV=false;
			
			for(int i=0; i<2; i++){
				for(int j=0; j<(rowsIDs.size()*3); j++){

					if(i==0){
						if(j%3==0){
							objectRowNames[j][i]=rowsNames[j/3];
						}else{
							objectRowNames[j][i]="";	
						}
					}
					
					if(i==1){
						if(flagCC==true){
							objectRowNames[j][i]="CC";
							flagCC=false;
							flagCO=true;
						}else if(flagCO==true){
							objectRowNames[j][i]="CO";
							flagCO=false;
							flagCV=true;
						}else if(flagCV==true){
							objectRowNames[j][i]="CV";
							flagCV=false;
							flagCC=true;
						}
					}
				}
			}
		}
		
		XX_MyTableModel headerData = new XX_MyTableModel(objectRowNames,nullHeader);
		JTable rowHeader = new JTable(headerData);
		        
		//impide que se puedan seleccionar los rowHeaders
		rowHeader.setFocusable(false);

		LookAndFeel.installColorsAndFont
		         (rowHeader, "TableHeader.background", 
		         "TableHeader.foreground", "TableHeader.font");

		rowHeader.setCellSelectionEnabled(false);
		
		
		rowHeader.setIntercellSpacing(new Dimension(0, 0));
		Dimension d = rowHeader.getPreferredScrollableViewportSize();
		d.width = rowHeader.getPreferredSize().width;
		rowHeader.setPreferredScrollableViewportSize(d);
		rowHeader.setRowHeight(xTable.getRowHeight());
		rowHeader.setDefaultRenderer(Object.class, new XX_RowHeaderRenderer());
		
		if(rowsIDs.size()==0){
			rowHeader.getColumnModel().getColumn(0).setPreferredWidth(8);
		}else{
			rowHeader.getColumnModel().getColumn(1).setPreferredWidth(8);
		}
		
		xScrollPane.setRowHeaderView(rowHeader);

		JTableHeader corner = rowHeader.getTableHeader();
		corner.setReorderingAllowed(false);
		corner.setResizingAllowed(false);

		xScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
		
		xTable.setAutoResizeMode(3);
		
		String[] columnsNames = columnsNames(columnsIDs);
		
		Object[][] data = data(columnsIDs, rowsIDs);
		
		//el +1 es para la columna de los totales
		ColumnInfo[] layout = new ColumnInfo[columnsIDs.size()+1];
		
		for(int i=0;i<columnsIDs.size()+1;i++){
			if(i==columnsIDs.size()){
				layout[i] = new ColumnInfo("Total", "", Integer.class, false, false, "");
			}else{
				layout[i] = new ColumnInfo(columnsNames[i], "", Integer.class, false, false, "");
			}
		}

		xTable.prepareTable(layout, "", "", false, "");
		
		xTable.setRowSelectionAllowed(false);
		xTable.getTableHeader().setReorderingAllowed(false);

		TableModel model = xTable.getModel();
		
		if(rowsIDs.size()==0){ //si no tiene caracteristica2
			for(int row=0;row<3;row++){
				
				xTable.setRowCount(row+1);
						
				for(int col=0;col<columnsIDs.size();col++){
					model.setValueAt(data[row][col], row, col);
				}
			}
		}
		else{
			for(int row=0;row<rowsIDs.size()*3;row++){
				
				xTable.setRowCount(row+1);
						
				for(int col=0;col<columnsIDs.size();col++){
					model.setValueAt(data[row][col], row, col);
				}
			}
		}

		//Cargo los totales
		int totals = 0;
		for(int j=0; j<xTable.getRowCount(); j++){
			
			for (int i=0; i<xTable.getColumnCount()-1; i++){
				totals = totals + (Integer)xTable.getValueAt(j, i);
			}
			
			xTable.setValueAt(totals, j, xTable.getColumnCount()-1);
			totals=0;
		}
		
		xTable.getColumnModel().getColumn(xTable.getColumnCount()-1).setWidth(60);
		
		//  Visual
		CompiereColor.setBackground (this);

		//xTable.getSelectionModel().addListSelectionListener(this);
		
		xTable.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(!xTable.isEditing()){
					showData(columnsIDs,rowsIDs);
					dataAux=tableData();
					if(multiple==false){
						ADialog.error(m_WindowNo,m_frame,"PMultiple");
					}
					multiple=true;
				}		
				
				int row = xTable.getSelectedColumn();
				int column = xTable.getSelectedRow();
				xTable.editCellAt(column, row);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
			
		});
		
		xTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {	
				
				if(!xTable.isEditing()){
					showData(columnsIDs,rowsIDs);
					dataAux=tableData();
					if(multiple==false){
						ADialog.error(m_WindowNo,m_frame,"PMultiple");
					}
					multiple=true;
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {	
			}
		});
			
		
		bSave.addActionListener(this);

		//  Init
		//statusBar.setStatusLine("Dimensión ["+rowsIDs.size()+"x"+columnsIDs.size()+"]");
		//statusBar.setStatusDB(0);
		
		bSave.setEnabled(true);
		
		//PU
		getCV();
		//toma el multiplo de empaques
		packageMultiple=LineRefProv.getXX_PackageMultiple();
		//hago el respaldo de la data de la tabla para cuando tenga que resetear por el multipo de empaque
		dataAux=tableData();
		
		
		MOrder order = new MOrder(Env.getCtx(),LineRefProv.getC_Order_ID(),null);
		
		boolean block=false;
		String oS = order.getXX_OrderStatus();
		String compS = order.getDocStatus();
		if(oS.equals("AN") || compS.equals("CO")){
			block=true;
		}
		
		if(order.isXX_OrderReadyStatus() && order.getXX_OrderType().equalsIgnoreCase("Nacional"))
			block = true;
		
		if(order.getXX_ComesFromSITME())
			block = true;
		
		if(order.getXX_StoreDistribution().equals("Y") || block){
			xTable.setEnabled(false);
			bSave.setEnabled(false);
		}
		
		productQty();

	}   //  dynInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
		line = new MVMRPOLineRefProv(Env.getCtx(), LineRefProv_ID, null);
		
		String SQL = null;
		
		if(GenerateMatrix.equals("Y")){	
			
			if(line.getXX_Characteristic1Value1_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value1='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
				    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value2_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value2='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;

				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value3_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value3='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value4_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value4='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value5_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value5='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value6_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value6='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value7_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value7='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value8_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value8='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value9_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value9='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic1Value10_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac1Value10='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			
			
			if(line.getXX_Characteristic2Value1_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value1='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value2_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value2='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value3_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value3='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value4_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value4='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value5_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value5='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value6_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value6='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value7_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value7='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value8_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value8='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value9_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value9='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			if(line.getXX_Characteristic2Value10_ID()!=0){
				SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_IsGeneratedCharac2Value10='Y' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
			    
				int no = DB.executeUpdate(null, SQL);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			

			SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_ReferenceIsAssociated='N' WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
		    
			int no = DB.executeUpdate(null, SQL);
			if (no != 1)
				log.log(Level.SEVERE, "Header update #" + no);
		}


		MOrder order = new MOrder(Env.getCtx(),LineRefProv.getC_Order_ID(),null);
		
		String oS = order.getXX_OrderStatus();
		String compS = order.getDocStatus();
		if(!oS.equals("AN") && !compS.equals("CO")){
			fullAssociated();
		}
		
	}	//	dispose
	
	
	private void fullAssociated(){
		
		boolean associated=true;
		
		String SQL ="Select * " +
		"from XX_VMR_REFERENCEMATRIX " +
		"where M_product IS NULL AND XX_QUANTITYV <> 0 AND XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.get_ID();		
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				associated=false;
			}
									
			rs.close();
			pstmt.close();
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		
		if(associated==true){
			LineRefProv.setXX_ReferenceIsAssociated(true);
			LineRefProv.save();
		}else{
			LineRefProv.setXX_ReferenceIsAssociated(false);
			LineRefProv.save();
		}
		
	}

	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (e.getSource() == bSave)
			cmd_Save(columnsIDs,rowsIDs);
			
	}   //  actionPerformed

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(!xTable.isEditing()){
			showData(columnsIDs,rowsIDs);
			dataAux=tableData();
			if(multiple==false){
				ADialog.error(m_WindowNo,m_frame,"PMultiple");
			}
			multiple=true;
		}		
		
		int row = xTable.getSelectedColumn();
		int column = xTable.getSelectedRow();
		xTable.editCellAt(column, row);
	}
	
	private Vector <Integer> columnsVector(Integer LineRefProv_ID){
		
		X_XX_VMR_PO_LineRefProv Line = new X_XX_VMR_PO_LineRefProv(Env.getCtx(),LineRefProv_ID, null);
		
		Vector <Integer> vector = new Vector <Integer>();
		
		if(Line.getXX_Characteristic1Value1_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value1_ID());
		}
		if(Line.getXX_Characteristic1Value2_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value2_ID());
		}
		if(Line.getXX_Characteristic1Value3_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value3_ID());
		}
		if(Line.getXX_Characteristic1Value4_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value4_ID());
		}
		if(Line.getXX_Characteristic1Value5_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value5_ID());
		}
		if(Line.getXX_Characteristic1Value6_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value6_ID());
		}
		if(Line.getXX_Characteristic1Value7_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value7_ID());
		}
		if(Line.getXX_Characteristic1Value8_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value8_ID());
		}
		if(Line.getXX_Characteristic1Value9_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value9_ID());
		}
		if(Line.getXX_Characteristic1Value10_ID()!=0){
			vector.add(Line.getXX_Characteristic1Value10_ID());
		}
		
		return vector;
	}
	
	private String[] columnsNames(Vector <Integer> vector){
		
		String[] columnNames= new String[vector.size()];
		
		for (int i=0;i<vector.size();i++){
			
			X_M_AttributeValue attribute = new X_M_AttributeValue(Env.getCtx(),vector.get(i), null);
			columnNames[i]=attribute.getName();
		}
		
		return columnNames;
	}
	
	private Vector <Integer> rowsVector(Integer LineRefProv_ID){
		
		X_XX_VMR_PO_LineRefProv Line = new X_XX_VMR_PO_LineRefProv(Env.getCtx(),LineRefProv_ID, null);
		
		Vector <Integer> vector = new Vector <Integer>();
		
		if(Line.getXX_Characteristic2Value1_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value1_ID());
		}
		if(Line.getXX_Characteristic2Value2_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value2_ID());
		}
		if(Line.getXX_Characteristic2Value3_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value3_ID());
		}
		if(Line.getXX_Characteristic2Value4_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value4_ID());
		}
		if(Line.getXX_Characteristic2Value5_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value5_ID());
		}
		if(Line.getXX_Characteristic2Value6_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value6_ID());
		}
		if(Line.getXX_Characteristic2Value7_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value7_ID());
		}
		if(Line.getXX_Characteristic2Value8_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value8_ID());
		}
		if(Line.getXX_Characteristic2Value9_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value9_ID());
		}
		if(Line.getXX_Characteristic2Value10_ID()!=0){
			vector.add(Line.getXX_Characteristic2Value10_ID());
		}
		
		return vector;
	}
	
	private String[] rowsNames(Vector <Integer> vector){
		
		String[] columnNames= new String[vector.size()];
		
		for (int i=0;i<vector.size();i++){
			
			X_M_AttributeValue attribute = new X_M_AttributeValue(Env.getCtx(),vector.get(i), null);
			columnNames[i]=attribute.getName();
		}
		
		return columnNames;
	}
	
	private Object[][] data(Vector <Integer> vector,Vector <Integer> vector2){
			
		Object[][] data = null;
		
		int row;
		if(vector2.size()==0){
			row=1;
			data = new Object[3][vector.size()];
		}
		else{
			row=vector2.size()*3;
			data = new Object[vector2.size()*3][vector.size()];
		}
			
		for (int i=0;i<vector.size();i++){
			for (int j=0;j<row;j++){
				
				String SQL =
					"Select XX_QuantityC, XX_QuantityO, XX_QuantityV "
					+"from XX_VMR_REFERENCEMATRIX " 
					+"where XX_Column="+i+" AND XX_Row="+j+" AND XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.getXX_VMR_PO_LineRefProv_ID();
					
				try 
				{
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();	
						
					if(rs.next())
					{	
						data[j][i]=rs.getInt("XX_QuantityC");
						data[j+1][i]=rs.getInt("XX_QuantityO");
						data[j+2][i]=rs.getInt("XX_QuantityV");
					}
									
					rs.close();
					pstmt.close();
									
				}
				catch(Exception e)
				{	
					e.getMessage();
				}
			}
		}
		
		return data;
	}

	Date fechaActual = new Date();
	private void cmd_Save(Vector <Integer> vector, Vector <Integer> vector2){
		
		if(xTable.isEditing()){
			xTable.getCellEditor().stopCellEditing();
		}
		
		showData(columnsIDs,rowsIDs);
		
/**		if(exceedsQty()){
			ADialog.error(m_WindowNo,m_frame,"XX_ExceedsQTY");
			return;
		}
*/		
		int row;
		if(vector2.size()==0){
			row=1;
		}
		else{
			row=vector2.size();
		}
		
		for (int i=0;i<vector.size();i++){
			for (int j=0;j<row;j++){
				
				String SQL =
					"UPDATE XX_VMR_REFERENCEMATRIX SET "
				    +"XX_QUANTITYC = "+xTable.getValueAt(j*3,i);
				    
				    SQL+=",XX_QUANTITYO = "+xTable.getValueAt(j*3+1,i);
				
				    SQL+=",XX_QUANTITYV = "+xTable.getValueAt(j*3+2,i);
				
				    SQL+=" WHERE XX_Column="+i+" AND XX_Row="+j*3+" AND XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
				    
				    try {
				    	DB.executeUpdate(null, SQL);
				    } catch(Exception e)
				    {
				    	System.out.println("Error al updatear la matriz");
				    	e.printStackTrace();
				    }
				
			}
		}
		
		line = new MVMRPOLineRefProv(Env.getCtx(), LineRefProv_ID, null);
		
		if(GenerateMatrix.equals("Y")){	
			
			if(line.getXX_Characteristic1Value1_ID()!=0){
				line.setXX_IsGeneratedCharac1Value1(true);
			}
			if(line.getXX_Characteristic1Value2_ID()!=0){
				line.setXX_IsGeneratedCharac1Value2(true);
			}
			if(line.getXX_Characteristic1Value3_ID()!=0){
				line.setXX_IsGeneratedCharac1Value3(true);
			}
			if(line.getXX_Characteristic1Value4_ID()!=0){
				line.setXX_IsGeneratedCharac1Value4(true);
			}
			if(line.getXX_Characteristic1Value5_ID()!=0){
				line.setXX_IsGeneratedCharac1Value5(true);
			}
			if(line.getXX_Characteristic1Value6_ID()!=0){
				line.setXX_IsGeneratedCharac1Value6(true);
			}
			if(line.getXX_Characteristic1Value7_ID()!=0){
				line.setXX_IsGeneratedCharac1Value7(true);
			}
			if(line.getXX_Characteristic1Value8_ID()!=0){
				line.setXX_IsGeneratedCharac1Value8(true);
			}
			if(line.getXX_Characteristic1Value9_ID()!=0){
				line.setXX_IsGeneratedCharac1Value9(true);
			}
			if(line.getXX_Characteristic1Value10_ID()!=0){
				line.setXX_IsGeneratedCharac1Value10(true);
			}
			
			
			if(line.getXX_Characteristic2Value1_ID()!=0){
				line.setXX_IsGeneratedCharac2Value1(true);
			}
			if(line.getXX_Characteristic2Value2_ID()!=0){
				line.setXX_IsGeneratedCharac2Value2(true);
			}
			if(line.getXX_Characteristic2Value3_ID()!=0){
				line.setXX_IsGeneratedCharac2Value3(true);
			}
			if(line.getXX_Characteristic2Value4_ID()!=0){
				line.setXX_IsGeneratedCharac2Value4(true);
			}
			if(line.getXX_Characteristic2Value5_ID()!=0){
				line.setXX_IsGeneratedCharac2Value5(true);
			}
			if(line.getXX_Characteristic2Value6_ID()!=0){
				line.setXX_IsGeneratedCharac2Value6(true);
			}
			if(line.getXX_Characteristic2Value7_ID()!=0){
				line.setXX_IsGeneratedCharac2Value7(true);
			}
			if(line.getXX_Characteristic2Value8_ID()!=0){
				line.setXX_IsGeneratedCharac2Value8(true);
			}
			if(line.getXX_Characteristic2Value9_ID()!=0){
				line.setXX_IsGeneratedCharac2Value9(true);
			}
			if(line.getXX_Characteristic2Value10_ID()!=0){
				line.setXX_IsGeneratedCharac2Value10(true);
			}
			
			line.setXX_ReferenceIsAssociated(false);
		}
		
		updatePOHeader();
		importData();
		if(multiple==false){
			ADialog.error(m_WindowNo,m_frame,"PMultiple");
			dispose();
		}else{
			dispose();
		}
			
	}
	
	private void getCV(){
		
		//se obtienen la unidad de compra y la unidad de venta para el CV
		String SQL =
			"Select uc.XX_UNITCONVERSION, us.XX_UNITCONVERSION "+
			"from XX_VMR_PO_LINEREFPROV lp, XX_VMR_UnitConversion uc, XX_VMR_UnitConversion us "+
			"where "+
			"lp.XX_PiecesBySale_ID = us.XX_VMR_UnitConversion_ID "+
			"and lp.XX_VMR_UnitConversion_ID = uc.XX_VMR_UnitConversion_ID "+
			"and lp.XX_VMR_PO_LINEREFPROV_ID="+LineRefProv_ID;
		
		PU=1;
		PS=1;
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();	
			
			while(rs.next()) //compruebo que ya exista la matriz
			{	
				PU=rs.getInt(1);
				PS=rs.getInt(2);
			}
						
			rs.close();
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
	}
	
	private void showData(Vector<Integer> vector, Vector<Integer> vector2){
		
		int row;
		if(vector2.size()==0){
			row=1;
		}
		else{
			row=vector2.size()*3;
		}
		
		for (int i=0;i<vector.size();i++){
			for (int j=0;j<row;j++){
			
				Integer value = (Integer) xTable.getValueAt(j,i);
				
				if(value==null){
					xTable.setValueAt(0, j, i);
				}
			}
		}
		
		for (int i=0;i<vector.size();i++){
			for (int j=0;j<row;j=j+3){
			    
				Integer CC = (Integer) xTable.getValueAt(j,i);
				
				if(CC%packageMultiple!=0){
					
					xTable.setValueAt(dataAux[j/3][i], j/3, i);
					CC=(Integer) xTable.getValueAt(j,i);
					multiple=false;
				}
				
				//TODO
				//se multiplica el multiplo de empaques por la cantidad de CC
				int CV;
				CV=(CC*PU)/PS;
				
				//se setea el CV
				xTable.setValueAt(CV, j+2, i);
				
				//despues de mostrar la data se hace update en la BD
			}
		}
	
		//Cargo los totales
		int totals = 0;
		for(int j=0; j<xTable.getRowCount(); j++){
			
			for (int i=0; i<xTable.getColumnCount()-1; i++){
				totals = totals + (Integer)xTable.getValueAt(j, i);
			}
			
			xTable.setValueAt(totals, j, xTable.getColumnCount()-1);
			totals=0;
		}
		
		//Actualizo la cantidad de Productos
		productQty();
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// Auto-generated method stub
	}
	
	private Object[][] tableData(){
		
		Object[][] data = null;
		
		int row;
		if(rowsIDs.size()==0){
			row=1;
			data = new Object[1][columnsIDs.size()];
		}
		else{
			row=rowsIDs.size();
			data = new Object[rowsIDs.size()][columnsIDs.size()];
		}
		
		for(int i=0; i<columnsIDs.size(); i++){
			for(int j=0; j<row; j++){
			
				data[j][i] = xTable.getValueAt(j, i);
			}
		}
		
		return data;
	}
	
	private boolean emptyData(){
		
		String SQL =
			"Select *"
			+"from XX_VMR_REFERENCEMATRIX " 
			+"where XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID");
		
		boolean empty=true;
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();	
			
			if(rs.next()) //compruebo que ya exista la matriz
			{	
				empty=false;
			}
						
			rs.close();
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
		
		return empty;
		
	}
	
	private String createData(Vector <Integer> vector, Vector <Integer> vector2, int tamanoX, int tamanoY){
		
		if(tamanoY==0){
			tamanoY=1;
		}
		
		for (int i=0;i<tamanoX;i++){
			for (int j=0;j<tamanoY*3;j=j+3){
		
				X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(),0, null);
				
				matrix.setXX_VALUE1(vector.get(i));
				
				if(vector2.size()==0){
					matrix.setXX_VALUE2(0);
				}else{
					matrix.setXX_VALUE2(vector2.get(j/3));	
				}
				
				matrix.setXX_COLUMN(i);
				matrix.setXX_ROW(j);
				matrix.setXX_QUANTITYC(0);
				matrix.setXX_QUANTITYV(0);
				matrix.setXX_QUANTITYO(0);
				matrix.setXX_VMR_PO_LineRefProv_ID((Integer)LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID"));
				
				matrix.save();
			}
		}
		
		return "";
	}
	
	private String addData(Vector <Integer> vector, Vector <Integer> vector2,int oldColumn, int oldRow,int maxColumn, int maxRow){
		
		if(rowsIDs.size()!=0){
			for (int i=oldColumn+1;i<maxColumn+1;i++){
				for (int j=0;j<rowsIDs.size()*3;j=j+3){				
					
					X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(), 0, null);
							
					matrix.setXX_VALUE1(vector.get(i));
					matrix.setXX_VALUE2(vector2.get(j/3));
					matrix.setXX_COLUMN(i);
					matrix.setXX_ROW(j);
					matrix.setXX_QUANTITYC(0);
					matrix.setXX_QUANTITYV(0);
					matrix.setXX_QUANTITYO(0);
					matrix.setXX_VMR_PO_LineRefProv_ID((Integer)LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID"));
					matrix.save();	    
				}
			}
		}
		else{
			for (int i=oldColumn+1;i<maxColumn+1;i++){
				for (int j=0;j<3;j=j+3){				
					
					X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(), 0, null);
							
					matrix.setXX_VALUE1(vector.get(i));
					matrix.setXX_VALUE2(0);
					matrix.setXX_COLUMN(i);
					matrix.setXX_ROW(j);
					matrix.setXX_QUANTITYC(0);
					matrix.setXX_QUANTITYV(0);
					matrix.setXX_QUANTITYO(0);
					matrix.setXX_VMR_PO_LineRefProv_ID((Integer)LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID"));
					matrix.save();	    
				}
			}
		}

		int columns=0;
		if((oldColumn!=maxColumn) && (oldRow!=maxRow)){
			columns=oldColumn+1;
		}else{
			columns=columnsIDs.size();
		}
		
		for (int i=0;i<columns;i++){
			for (int j=(oldRow+1)*3;j<(maxRow+1)*3;j=j+3){
					
				X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(),0, null);
					
				matrix.setXX_VALUE1(vector.get(i));
				matrix.setXX_VALUE2(vector2.get(j/3));
				matrix.setXX_COLUMN(i);
				matrix.setXX_ROW(j);
				matrix.setXX_QUANTITYC(0);
				matrix.setXX_QUANTITYV(0);
				matrix.setXX_QUANTITYO(0);
				matrix.setXX_VMR_PO_LineRefProv_ID((Integer)LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID"));
				matrix.save();		    
			}
		}
		
		return "";
	}
	
	private boolean deleteData(){
		String SQL =
			"Delete XX_VMR_REFERENCEMATRIX " 
			+"where XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID");
		 
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();					
			rs.close();
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			e.getMessage();
			return false;
		}
		
		return true;
		
	}
	
	private Integer[] tam(){
		
		String SQL = 
			"Select Max(XX_Column), Max(XX_Row), Max(XX_Value2) "
			+"from XX_VMR_REFERENCEMATRIX " 
			+"where XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID");
		
		Integer[] tam = new Integer[3];
		try 
		{	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				tam[0] = rs.getInt("Max(XX_Column)");
				tam[1] = rs.getInt("Max(XX_Row)");
				tam[2] = rs.getInt("Max(XX_Value2)");
			}
			
			rs.close();
			pstmt.close();
						
		}
		catch(Exception e)
		{	
			e.getMessage();
		}
		
		return tam;
	}
	
	private void updatePOHeader()
	{
		
		Integer CantCompra = 0;
		Integer CantObsequio = 0;
		
		for(int i=0; i<xTable.getRowCount(); i=i+3){
			
			for(int j=0; j<xTable.getColumnCount()-1; j++){

				CantCompra = CantCompra + (Integer)xTable.getValueAt(i, j);
				
			}
		}
		
		String SQL =
			"Select SUM(XX_QUANTITYO) AS QtyObsequio"
			+" from XX_VMR_REFERENCEMATRIX" 
			+" where XX_VMR_PO_LINEREFPROV_ID="+LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID");
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();	
			
			while(rs.next()) //compruebo que ya exista la matriz
			{	
				CantObsequio = rs.getInt("QtyObsequio");
			}
						
		}
		catch(Exception e1)
		{	
			log.log(Level.SEVERE,SQL,"Error en el primer query");
			e1.getMessage();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		String ordertype = "";
		
		SQL = "select xx_ordertype"
		    + " from c_order"
		    + " where c_order_id=" 
		    + 				" (select c_order_id"
		    + 				" from XX_VMR_PO_LINEREFPROV"
		    + 				" where XX_VMR_PO_LINEREFPROV_Id=" + LineRefProv_ID + ")";
		    
		try 
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();	
			
			while(rs.next()) 
			{	
				ordertype = rs.getString("xx_ordertype");
			}
						
		}
		catch(Exception e1)
		{	
			log.log(Level.SEVERE,SQL,"Error al buscar el typo de orden");
			e1.getMessage();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		BigDecimal LineNetAmt = new BigDecimal(0);

			LineNetAmt = ((BigDecimal)LineRefProv.get_Value("XX_CostWithDiscounts")).multiply(new BigDecimal(CantCompra));
			LineNetAmt = LineNetAmt.setScale (2,BigDecimal.ROUND_HALF_UP);

		
		Integer UnitConversion = 0;
		
		SQL = "select XX_UnitConversion"
		    + " from XX_VMR_UnitConversion"
		    + " where XX_VMR_UnitConversion_ID=" + LineRefProv.get_Value("XX_VMR_UnitConversion_ID");
		
		try 
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();	
			
			while(rs.next()) 
			{	
				UnitConversion = rs.getInt("XX_UnitConversion");
			}
						
		}
		catch(Exception e1)
		{	
			log.log(Level.SEVERE,SQL,"Error al buscar la unidad de conversion de compra");
			e1.getMessage();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		int cantVenta = 1;
		
		SQL = "select XX_UnitConversion"
			    + " from XX_VMR_UnitConversion"
			    + " where XX_VMR_UnitConversion_ID=" + LineRefProv.get_Value("XX_PiecesBySale_ID");
			
			try 
			{
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();	
				
				while(rs.next()) 
				{	
					cantVenta = rs.getInt("XX_UnitConversion");
				}
							
			}
			catch(Exception e1)
			{	
				log.log(Level.SEVERE,SQL,"Error al buscar la unidad de conversion de venta");
				e1.getMessage();
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

		BigDecimal SalePrice = (BigDecimal)LineRefProv.get_Value("XX_SalePrice");
		SalePrice = SalePrice.setScale (2,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal LinePVPAmount = SalePrice.multiply(new BigDecimal(CantCompra*UnitConversion).divide(new BigDecimal(cantVenta), 4, RoundingMode.HALF_UP));
		LinePVPAmount = LinePVPAmount.setScale (2,BigDecimal.ROUND_HALF_UP);
		BigDecimal SalePricePlusTax = (BigDecimal)LineRefProv.get_Value("XX_SalePricePlusTax");
		SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
		BigDecimal LinePlusTaxAmount = SalePricePlusTax.multiply(new BigDecimal(CantCompra*UnitConversion).divide(new BigDecimal(cantVenta), 4, RoundingMode.HALF_UP));
		LinePlusTaxAmount = LinePlusTaxAmount.setScale (2,BigDecimal.ROUND_HALF_UP);
		
		//TODO
		//Integer LineQty = (CantCompra*UnitConversion);
		Integer LineQty = (CantCompra);

		SQL = "UPDATE XX_VMR_PO_LINEREFPROV SET " +
		" Qty=" + CantCompra + ", XX_GiftsQty=" + CantObsequio + " , XX_LineQty=" + LineQty + " , LineNetAmt=" + LineNetAmt + 
		" , XX_LinePVPAmount=" + LinePVPAmount + " ,XX_SalePricePlusTax=" + SalePricePlusTax + " ,XX_LinePlusTaxAmount=" + LinePlusTaxAmount + 
		" WHERE XX_VMR_PO_LINEREFPROV_ID="+line.getXX_VMR_PO_LineRefProv_ID();

		int no = DB.executeUpdate(null, SQL);
		if (no != 1)
			log.log(Level.SEVERE, "Header update #" + no);
		
		String sql = "UPDATE C_Order po"											//////////////////////
			+ " SET (XX_ProductQuantity, TotalPVP, XX_TotalPVPPlusTax, TotalLines ) ="											//////////////////////
				+ "(SELECT COALESCE(SUM(XX_LineQty),0), COALESCE(SUM(XX_LinePVPAmount),0), COALESCE(SUM(XX_LinePlusTaxAmount),0), COALESCE(SUM(LineNetAmt),0)  FROM XX_VMR_PO_LINEREFPROV line "
				+ "WHERE po.C_Order_ID=line.C_Order_ID) "
			+ "WHERE po.C_Order_ID=" + LineRefProv.getC_Order_ID();						//////////////////////
		
		//System.out.println("C_Order header:"+sql);
		
		no = DB.executeUpdate(null, sql);
		if (no != 1)
			log.log(Level.SEVERE, "Header update #" + no);
		
	}
	
	private void importData()
	{
		/** 
		 * 
		 * Logica de la data de imports
		 * Por: JTrias
		 * 
		 */
		MOrder order = new MOrder(Env.getCtx(), LineRefProv.getC_Order_ID(), null);
		
		String updateOrder = null; 
		
		//XX_INTNACESTMEDAMOUNT
		String SQL = ("SELECT A.XX_INTERFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFI " +
		    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
		    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
		    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
		    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
		    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
		    		"AND A.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+") " +
		    		"AND C.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			if(rs.next())
			{
				//debo dividir el valor entre 100 porq es un porcentaje
				BigDecimal aux = rs.getBigDecimal("MEFI").divide(new BigDecimal(100));
				//Lo rendondeo  a 2 decimales
				aux=aux.setScale(2,BigDecimal.ROUND_UP);
				//seteo
				updateOrder= "Update C_Order set XX_INTNACESTMEDAMOUNT=" + aux + " where c_order_id=" + order.getC_Order_ID();
				
				int no = DB.executeUpdate(null, updateOrder);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//XX_NationalEsteemedAmount
		SQL = ("SELECT DISTINCT A.XX_NACFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFN " +
		    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
		    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
		    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
		    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
		    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
		    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
		    		"AND A.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+") " +
		    		"AND C.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
		    
		 try{
			
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
			 
		    if(rs.next())
		    {
		    	//debo dividir el valor entre 100 porq es un porcentaje
				BigDecimal aux = rs.getBigDecimal("MEFN").divide(new BigDecimal(100));
				//Lo rendondeo  a 2 decimales
				aux=aux.setScale(2,BigDecimal.ROUND_UP);
				//seteo
				updateOrder= "Update C_Order set XX_NationalEsteemedAmount=" + aux + " where c_order_id=" + order.getC_Order_ID();
				
				int no = DB.executeUpdate(null, updateOrder);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
			}
		    
		 }catch (Exception e) {
			 log.log(Level.SEVERE, SQL);
		 } finally {
			 DB.closeResultSet(rs);
			 DB.closeStatement(pstmt);
		 }

		//XX_CustomsAgentEsteemedAmount
		SQL = ("SELECT A.XX_ESTIMATEDPERTUSAGENT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEAA " +
	    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
	    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
	    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
	    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
	    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
	    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
	    		"AND A.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+") " +
	    		"AND C.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
			    
		 try{
				
			pstmt = DB.prepareStatement(SQL, null); 
		    rs = pstmt.executeQuery();
				 
		    if(rs.next())
		    {
		    	//debo dividir el valor entre 100 porq es un porcentaje
				BigDecimal aux = rs.getBigDecimal("MEAA").divide(new BigDecimal(100));
				//Lo rendondeo  a 2 decimales
				aux=aux.setScale(2,BigDecimal.ROUND_UP);
				//seteo
				updateOrder= "Update C_Order set XX_CustomsAgentEsteemedAmount=" + aux + " where c_order_id=" + order.getC_Order_ID();
				
				int no = DB.executeUpdate(null, updateOrder);
				if (no != 1)
					log.log(Level.SEVERE, "Header update #" + no);
		    }
			    
		 }catch (Exception e) {
			 log.log(Level.SEVERE, SQL);
		 } finally {
			 DB.closeResultSet(rs);
			 DB.closeStatement(pstmt);
		 }
		 
		//XX_EsteemedInsuranceAmount
		 BigDecimal costo = new BigDecimal(0);
		 BigDecimal impuesto = new BigDecimal(0);
		 
		 SQL = ("SELECT G.XX_RATE, (C.TOTALLINES * H.MULTIPLYRATE) AS Costo " +
		    		"FROM C_ORDER C, XX_VLO_DISPATCHROUTE G, C_CONVERSION_RATE H " +
		    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
		    		"AND C.XX_VLO_DISPATCHROUTE_ID = G.XX_VLO_DISPATCHROUTE_ID " +
		    		"AND H.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " );
				    
		 try{
					
			 pstmt = DB.prepareStatement(SQL, null); 
			 rs = pstmt.executeQuery();
			    
			 if(rs.next())
			 {
				 costo = rs.getBigDecimal("Costo");
			     costo = costo.add(order.getXX_NationalEsteemedAmount());
			     costo = costo.add(order.getXX_INTNACESTMEDAMOUNT());
			     costo = costo.add(impuesto);
			     costo = costo.multiply(rs.getBigDecimal("XX_RATE"));
			     costo = costo.divide(new BigDecimal(100));   
			     
			     BigDecimal aux = costo;
				 //Lo rendondeo  a 2 decimales
				 aux=aux.setScale(2,BigDecimal.ROUND_UP);
				 //seteo
				 updateOrder= "Update C_Order set XX_ESTEEMEDINSURANCEAMOUNT=" + aux + " where c_order_id=" + order.getC_Order_ID();
				 
					int no = DB.executeUpdate(null, updateOrder);
					if (no != 1)
						log.log(Level.SEVERE, "Header update #" + no);
			 }
				    
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
	}
	
	
	private void productQty(){
		
		int qty=0;
		
		for(int i=0; i<xTable.getRowCount(); i=i+3){
			
			for(int j=0; j<xTable.getColumnCount()-1; j++){

				qty = qty + (Integer)xTable.getValueAt(i, j);
			}
		}
		
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "ProductQty"));
		statusBar.setStatusDB(qty);
		totalQty = qty;
	}
	
	/**
	 * Esta funcion indica si la O/C posee mas de N piezas
	 */
/**	private boolean exceedsQty(){
		
		int max = 5000;
		int actual = 0;
		
		String SQL = "Select sum(lp.qty*uc.XX_UNITCONVERSION/us.XX_UNITCONVERSION) SALEQTY " +
					 "from XX_VMR_PO_LINEREFPROV lp, XX_VMR_UnitConversion uc, XX_VMR_UnitConversion us " +
					 "where " +
					 "lp.XX_PiecesBySale_ID = us.XX_VMR_UnitConversion_ID " +
					 "and lp.XX_VMR_UnitConversion_ID = uc.XX_VMR_UnitConversion_ID " +
					 "and lp.XX_VMR_PO_LINEREFPROV_ID IN " +
					 "(SELECT XX_VMR_PO_LINEREFPROV_ID FROM XX_VMR_PO_LINEREFPROV WHERE C_ORDER_ID = " + LineRefProv.getC_Order_ID(); 
					 
		if(LineRefProv.get_ID()>0)
			SQL += " AND XX_VMR_PO_LINEREFPROV_ID <> " + LineRefProv.get_ID();
					 
		SQL += ")";
		
		PreparedStatement prst = DB.prepareStatement(SQL, null);
		ResultSet rs = null;
		
		try {
			
			rs = prst.executeQuery();
			
			while (rs.next()){
				actual = actual + rs.getInt(1);
			}
			
		} catch (SQLException e){
			System.out.println(e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		
		actual = actual + saleQty();
		
		if(actual > max)
			return true;
		
		return false;
	}
*/	
	private int saleQty(){
		
		int qty=0;
		
		for(int i=0; i<xTable.getRowCount(); i=i+3){
			
			for(int j=0; j<xTable.getColumnCount()-1; j++){

				qty = qty + (Integer)xTable.getValueAt(i+2, j);
			}
		}
	
		return qty;
	}
	
}   //  ConvertReference