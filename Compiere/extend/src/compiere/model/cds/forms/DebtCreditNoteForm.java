package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.DocumentEngine;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MVCNApplicationNumber;
import compiere.model.cds.MVCNPercenReten;
import compiere.model.cds.MVCNPurchasesBook;
import compiere.model.cds.Utilities;

import java.text.SimpleDateFormat;

/**
 * @author Patricia Ayuso M. / Jorge E. Pires G.
 *
 */
public class DebtCreditNoteForm extends CPanel 	implements FormPanel, 
	ActionListener, ListSelectionListener {

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(DebtCreditNoteForm.class);

	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
			
	private CPanel mainPanel = new CPanel();
	private StatusBar statusBar = new StatusBar();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CButton modify = new CButton();
	private CButton search = new CButton();
	private CPanel southPanel = new CPanel();
	private GridBagLayout southLayout = new GridBagLayout();

	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout(5,5);
	private JScrollPane xScrollPane = new JScrollPane();
	private TitledBorder xBorder =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_DebtCreditNotify"));
	private MiniTable xTable = new MiniTable();
	private CPanel xPanel = new CPanel();

	private CLabel vendorLabel = new CLabel();
	private CLabel dateLabel = new CLabel();
	private CLabel controlLabel = new CLabel();
	private CLabel numberLabel = new CLabel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private VComboBox vendor = new VComboBox();
	private VDate calendar = new VDate();
	private CTextField control = new CTextField();
	private CTextField number = new CTextField();
	private CLabel categoryLabel = new CLabel();
	private CComboBox categoryCombo = new CComboBox();
	
	/*
	 * Jorge Pires
	 * */
	private CLabel numberLabelSearch = new CLabel();
	private CTextField numberSearch = new CTextField();

	private Trx trans = null;
	Utilities util = new Utilities();
		
	private static final SimpleDateFormat SDF =
        new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); //.SSS
	
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

		trans = Trx.get("XX_ModifyDebtCredit");
				
		try
		{
			//	UI
			jbInit();
			dynInit();
			dynCategory();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

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
		search.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		search.setEnabled(true);
			
		southPanel.setLayout(southLayout);
		modify.setText(Msg.translate(Env.getCtx(), "XX_Replace"));
		modify.setEnabled(true);
		
		centerPanel.setLayout(centerLayout);
		xScrollPane.setBorder(xBorder);
		xScrollPane.setPreferredSize(new Dimension(880, 170));
		
		xPanel.setLayout(xLayout);
		vendorLabel.setText(Msg.translate(Env.getCtx(), "VendorCod"));
		dateLabel.setText(Msg.translate(Env.getCtx(), "Date"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CategoryComercial"));
		
		northPanel.add(vendorLabel,new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, 
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(vendor,new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, 
				new Insets(12, 5, 5, 5), 0, 0));		
		northPanel.add(categoryLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,	
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 5, 5), 0, 0));
		northPanel.add(categoryCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 0), 0, 0));
		northPanel.add(numberLabelSearch,    new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, 
				new Insets(12, 5, 5, 5), 0, 0)); 
		northPanel.add(numberSearch,        new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, 
				new Insets(12, 0, 5, 0), 0, 0));		
		northPanel.add(search,  new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.CENTER, 
				new Insets(0, 12, 5, 12), 0, 0)); 
		
		
		numberLabelSearch.setText(Msg.translate(Env.getCtx(), "XX_NotifyNumber"));
		numberSearch.setPreferredSize(new Dimension(100,20));
		/*
		 * Fin - Jorge Pires
		 * */
		
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(xScrollPane,  BorderLayout.CENTER);
		xScrollPane.getViewport().add(xTable, null);

		controlLabel.setText(Msg.translate(Env.getCtx(), "XX_ControlNumber"));
		control.setPreferredSize(new Dimension(100,20));
		numberLabel.setText(Msg.translate(Env.getCtx(), "DocumentNo"));
		number.setPreferredSize(new Dimension(100,20));
		
		southPanel.add(numberLabel,    new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 10, 5, 0), 0, 0));
		southPanel.add(number,        new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 120, 5, 0), 0, 0));
		southPanel.add(controlLabel,    new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 230, 5, 0), 0, 0));
		southPanel.add(control,        new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 370, 5, 0), 0, 0));
		
		southPanel.add(dateLabel,				new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 480, 5, 0), 0, 0));
		
		southPanel.add(calendar,        new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(12, 520, 5, 0), 0, 0));
		
		
		southPanel.add(modify,  new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(12, 700, 5, 70), 0, 0));
		
		}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  Table Layout, Visual, Listener
	 */
	private void dynInit(){	
		vendor.addItem(new KeyNamePair(0, new String("")));
		// Bringing Vendors
		String sql = "SELECT b.C_BPARTNER_ID, b.NAME " +
					 "FROM C_BPARTNER b " +
					 "WHERE b.ISACTIVE = 'Y' " +
					 "AND b.ISVENDOR = 'Y' " +
					 "AND b.XX_ISVALID = 'Y' ";

		sql = MRole.getDefault().addAccessSQL(sql, "b", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		sql += " ORDER BY b.NAME";
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = DB.prepareStatement(sql, null);
			rs = pstm.executeQuery(sql);
			
			while (rs.next()){
				vendor.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			
			//vendor.setEditable(false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		Calendar now = Calendar.getInstance();
		calendar.setValue(now.getTime());
		
		//Add the column definition for the table
		ColumnInfo[] layout = new ColumnInfo[] {			
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Options"), "", IDColumn.class, false, false, ""),	//  0
				new ColumnInfo(Msg.translate(Env.getCtx(), "BPartner"),   ".", String.class),						//  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "Invoice"),   ".", String.class),						//  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NotifyNumber"),   ".", KeyNamePair.class),			//  3
				new ColumnInfo(Msg.getElement(Env.getCtx(), "DATEINVOICED"),   ".", String.class),				    //  4				
				new ColumnInfo(Msg.getElement(Env.getCtx(), "TotalLines"),   ".", Number.class),					//  5				
				new ColumnInfo(Msg.getElement(Env.getCtx(), "XX_TaxAmount"),   ".", Number.class),					//  6				
				new ColumnInfo(Msg.getElement(Env.getCtx(), "GrandTotal"),   ".", Number.class),					//  7			
				
				//new ColumnInfo(Msg.translate(Env.getCtx(), "XX_NotifyNumber"),   ".", String.class),				//  4
				//new ColumnInfo(Msg.translate(Env.getCtx(), "XX_ControlNumber"),   ".", String.class),				//  4				
				//new ColumnInfo(Msg.translate(Env.getCtx(), "fecha aviso"),   ".", String.class),				    //  5				
				//new ColumnInfo(Msg.translate(Env.getCtx(), "Taxable base"),   ".", Number.class),					//  6				
				//new ColumnInfo(Msg.translate(Env.getCtx(), "tax amount"),   ".", Number.class),					//  7				
				//new ColumnInfo(Msg.translate(Env.getCtx(), "total cost"),   ".", Number.class),					//  8				
		};
		xTable.prepareTable(layout, "", "", true, "");
		
		//  Visual
		CompiereColor.setBackground (this);

		
		xTable.getSelectionModel().addListSelectionListener(this);
		xTable.getColumnModel().getColumn(1).setMinWidth(220);
		xTable.getColumnModel().getColumn(2).setMinWidth(105);
		xTable.getColumnModel().getColumn(3).setMinWidth(105);
		xTable.getColumnModel().getColumn(4).setMinWidth(105);
		xTable.getColumnModel().getColumn(5).setMinWidth(105);
		xTable.getColumnModel().getColumn(6).setMinWidth(105);
		xTable.getColumnModel().getColumn(7).setMinWidth(105);

		
		//  Listener
		search.addActionListener(this);
		modify.addActionListener(this);

		//  Init
		statusBar.setStatusLine("");
		statusBar.setStatusDB(0);

		//dataVendor();  		
		//xTable.autoSize();
		
	}   //  dynInit
	
	/**
	 * Carga las categorias comerciales (moda, hogar, deporte, niños, belleza)
	 */
	void dynCategory() {

		categoryCombo.removeActionListener(this);
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_CATEGORY ";
		sql += " ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			categoryCombo.addItem(new KeyNamePair(-1, null)); 
			while (rs.next()) {
				categoryCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			categoryCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
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
	
	/**
	 * Method that fill the form table
	 */
	private void data(){  
		
		Calendar auxCalendar = Calendar.getInstance(), 
				 dateInit = Calendar.getInstance(), 
				 dateEnd = Calendar.getInstance(),
				 dateInitAnt = Calendar.getInstance(), 
				 dateEndAnt = Calendar.getInstance();
		auxCalendar.setTime((Date) calendar.getValue());
		int day = auxCalendar.get(Calendar.DATE);
		int month = auxCalendar.get(Calendar.MONTH);
		int year = auxCalendar.get(Calendar.YEAR);
		int lastDay = auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		//System.out.println("AUQI: "+numberSearch.getText());
		
		if(day < 16){
			// Actual Taxable Period
			dateInit.set(year, month, 1, 0, 0, 0);
			dateEnd.set(year, month, 15, 0, 0, 0);
			// Previous Taxable Period
			dateInitAnt.set(year, month, 16, 0, 0, 0);
			dateInitAnt.add(Calendar.MONTH, -1);
			int monthAnt = dateInitAnt.get(Calendar.MONTH);
			int lastDayAnt = dateInitAnt.getActualMaximum(Calendar.DAY_OF_MONTH);
			dateEndAnt.set(year, monthAnt, lastDayAnt, 0, 0, 0);
		}else{
			// Actual Taxable Period
			dateInit.set(year, month, 16, 0, 0, 0);
			dateEnd.set(year, month, lastDay, 0, 0, 0);
			// Previous Taxable Period
			dateInitAnt.set(year, month, 1, 0, 0, 0);
			dateEndAnt.set(year, month, 15, 0, 0, 0);
		}
		
		String SQL = 
			"SELECT b.NAME, j.DOCUMENTNO, i.C_Invoice_ID, i.DOCUMENTNO, " +
			"i.XX_CONTROLNUMBER, i.DATEINVOICED, " +
			/*
			 * Jorge Pires - agrego campos que se necesitan
			 */
			"TO_CHAR(i.DATEINVOICED, 'DD/MM/YYYY') DATEINVOICED, round(i.TotalLines,2) TotalLines , round(i.XX_TaxAmount,2) XX_TaxAmount , round(i.GrandTotal,2) GrandTotal " +
			"FROM C_INVOICE i " +
			"JOIN C_BPARTNER b ON b.C_BPARTNER_ID = i.C_BPARTNER_ID " +
			"LEFT JOIN XX_VMR_Department dep on (i.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
			"LEFT JOIN C_INVOICE j ON j.C_Invoice_ID = i.REF_INVOICE_ID " +
			"JOIN XX_VCN_PurchasesBook p on (i.C_Invoice_ID = p.XX_DocumentNo_ID and p.XX_Status='CO') " +
			"WHERE i.C_DOCTYPETARGET_ID IN ("+Env.getCtx().getContext("#XX_L_C_DOCTYPEDEBIT_ID")+", "+Env.getCtx().getContext("#XX_L_C_DOCTYPECREDIT_ID")+") " +
			"AND i.ISSOTRX = 'N' AND p.XX_Converted = 'N' ";

    	/*
    	 * Jorge Pires
    	 * */
		if(!numberSearch.getText().isEmpty()){
			SQL+= " AND i.DOCUMENTNO = '"+numberSearch.getText()+"' ";
		}
    	/*
    	 * Fin - Jorge Pires
    	 * */
		
		if (vendor.getSelectedIndex() != -1 && vendor.getSelectedItem() != null) 
			if(((KeyNamePair)vendor.getSelectedItem()).getKey() != 0)
    		SQL += " AND b.C_BPARTNER_ID = " + ((KeyNamePair)vendor.getSelectedItem()).getKey() + " ";
		
		/****Jessica Mendoza****/
		/****Buscar los proveedores segun la categoria comercial****/
		if ((categoryCombo.getSelectedIndex() != 0) && (categoryCombo.getSelectedItem() != null)){
			int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
			SQL += " and dep.XX_VMR_Category_ID = " + categoria + " "; 		
		}	
		/****Fin código - Jessica Mendoza****/
		
	    SQL = MRole.getDefault().addAccessSQL(
				SQL.toString(), "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
				+ " ORDER BY b.NAME";
	
	    int i = 0;
	    xTable.setRowCount(i);
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
	    try{ 		
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();			
			while(rs.next()) {	
				xTable.setRowCount (i+1);			
				IDColumn id = new IDColumn(rs.getInt(3));
				id.setSelected(false);
				xTable.setValueAt(id, i, 0);
				xTable.setValueAt(rs.getString(1), i, 1);
				xTable.setValueAt(rs.getString(2), i, 2);
				xTable.setValueAt(
						new KeyNamePair (rs.getInt(3), rs.getString(4)), i, 3);
				//xTable.setValueAt(rs.getString(5), i, 4);
				/*
				 * Jorge Pires - agrego a la tabla el XX_DATE, XX_TaxableBase, XX_TaxAmount, XX_TotalInvCost
				 */
				xTable.setValueAt(rs.getString(7), i, 4);
				xTable.setValueAt(rs.getDouble(8), i, 5);
				xTable.setValueAt(rs.getDouble(9), i, 6);
				xTable.setValueAt(rs.getDouble(10), i, 7);
				i++;				
			}			
		}catch(SQLException e){	
			e.getMessage();
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

	private int soloAvisosCredODeb(Vector<Object> avisos){ 
		IDColumn id = null;
		boolean mismoPeriodo = true;
		String periodoImpositivo = new String();
		
		boolean credito = false;
		boolean debito = false;
		int declaration = 0;
		String yearMonth;
		
		if(avisos != null){
			if(!avisos.isEmpty()){
				for (int i = 0; i < avisos.size(); i++){
					id = (IDColumn)avisos.elementAt(i);
					
					MInvoice avisoAux = new MInvoice(Env.getCtx(), id.getRecord_ID(), null);
					
					if(i==0){
						Calendar aux = Calendar.getInstance();
						aux.setTimeInMillis(avisoAux.getDateInvoiced().getTime());
						/****Jessica Mendoza****/
						yearMonth = String.valueOf(aux.get(Calendar.YEAR));
						if (String.valueOf((aux.get(Calendar.MONTH)+1)).length() == 1) 
							yearMonth = yearMonth + "0" + String.valueOf(aux.get(Calendar.MONTH)+1);
						else
							yearMonth = yearMonth + String.valueOf(aux.get(Calendar.MONTH)+1);
						/****Fin código - Jessica Mendoza****/
						if(aux.get(Calendar.DAY_OF_MONTH) <= 15){ 							
							//Jessica Mendoza
							/* Validar si la declaracion este creada y/o cerrada 
							 * para un numero de declaracion, compañia, año/mes */
							//declaration = declarationPeriodTax(avisoAux.getAD_Client_ID(),yearMonth,1);
							periodoImpositivo = "1"+new Integer(aux.get(Calendar.MONTH)+1);
						}else{
							//Jessica Mendoza
							/* Validar si la declaracion este creada y/o cerrada 
							 * para un numero de declaracion, compañia, año/mes */
							//declaration = declarationPeriodTax(avisoAux.getAD_Client_ID(),yearMonth,2);
							periodoImpositivo = "2"+new Integer(aux.get(Calendar.MONTH)+1);
						}
					}else{
						String periodoAux = new String();
						
						Calendar aux = Calendar.getInstance();
						aux.setTimeInMillis(avisoAux.getDateInvoiced().getTime());
						/****Jessica Mendoza****/
						yearMonth = String.valueOf(aux.get(Calendar.YEAR));
						if (String.valueOf((aux.get(Calendar.MONTH)+1)).length() == 1) 
							yearMonth = yearMonth + "0" + String.valueOf(aux.get(Calendar.MONTH)+1);
						else
							yearMonth = yearMonth + String.valueOf(aux.get(Calendar.MONTH)+1);
						/****Fin código - Jessica Mendoza****/
						if(aux.get(Calendar.DAY_OF_MONTH) <= 15){
							//Jessica Mendoza
							/* Validar si la declaracion este creada y/o cerrada 
							 * para un numero de declaracion, compañia, año/mes */
							//declaration = declarationPeriodTax(avisoAux.getAD_Client_ID(),yearMonth,1);
							periodoAux = "1"+new Integer(aux.get(Calendar.MONTH)+1);
						}else{
							//Jessica Mendoza
							/* Validar si la declaracion este creada y/o cerrada 
							 * para un numero de declaracion, compañia, año/mes */
							//declaration = declarationPeriodTax(avisoAux.getAD_Client_ID(),yearMonth,2);
							periodoAux = "2"+new Integer(aux.get(Calendar.MONTH)+1);
						}
						
						if(!periodoImpositivo.equals(periodoAux)){
							mismoPeriodo = false;
						}
					}

					if(avisoAux.getC_DocTypeTarget_ID() == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID")){
						credito = true;
					}else if(avisoAux.getC_DocTypeTarget_ID() == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEDEBIT_ID")){
						debito = true;
					}
				}
			}
		}
		
		declaration = 2;
		if (declaration == 1)
			return 8; //Está cerrada la declaración, para ese periodo impositivo
		else if (declaration == 3)
			return 9; //No está creada la declaración, para ese periodo impositivo
		else if (declaration == 2){
			//La declaración esta correcta, para ese periodo impositivo

			if(!mismoPeriodo){
				return 5; //no estan en el mismo periodo impositivo
			}
			
			if(credito == debito){
				if(credito == true){
					return 1; //esta seleccionando avisos de Debito y avisos de Credito				
				}
			}else if(credito == debito){
				if(credito == false){
					return 2; //no son ni avisos de Credito ni Debito
				}
			}else if(credito != debito){
				if(credito){
					String periodoAux = new String();
					
					Calendar aux = Calendar.getInstance();
					if(aux.get(Calendar.DAY_OF_MONTH) <= 15){
						periodoAux = "1"+new Integer(aux.get(Calendar.MONTH)+1);
					}else{
						periodoAux = "2"+new Integer(aux.get(Calendar.MONTH)+1);
					}
					
					if (!periodoImpositivo.equals(periodoAux)){
						return 6; // La nota de Credito no es del mismo periodo impositivo al Actual
					}else{
						return 3; //esta bien. selecciono solo avisos de credito o avisos de Debito (CREDITO)					
					}	
				}
				else{
					String periodoAux = new String();
					
					Calendar aux = Calendar.getInstance();
					if(aux.get(Calendar.DAY_OF_MONTH) <= 15){
						periodoAux = "1"+new Integer(aux.get(Calendar.MONTH)+1);
					}else{
						periodoAux = "2"+new Integer(aux.get(Calendar.MONTH)+1);
					}
					
					if (!periodoImpositivo.equals(periodoAux)){
						return 4; // La nota de Debito no es del mismo periodo impositivo al Actual
					}else{
						return 7; //esta bien. selecciono solo avisos de credito o avisos de Debito (DEBITO)					
					}	
				}
			}
		}
		return 2;
	}
	
	/**
	 * Se encarga de buscar para un año, mes, numero de declaracion 
	 * y compañia específica, si en el detalle de la declaracion 
	 * está creado y cerrado dicho registro
	 * @author Jessica Mendoza
	 * @param idClient identificador de la compañía
	 * @param yearMonth año/mes
	 * @param numeroDeclaracion numero de la declaración
	 * @return
	 */
	public Integer declarationPeriodTax(int idClient, String yearMonth, int numeroDeclaracion){
		String sql = "select XX_CheckEndPeriod " +
					 "from XX_VCN_DetailDeclaration " +
					 "where AD_Client_ID = " + idClient + " " +
					 "and XX_NumberDeclaration_ID = " +
					 	"(select XX_VCN_TaxPeriod_ID " +
					 	"from XX_VCN_TaxPeriod " +
					 	"where XX_NumberDeclaration = " + numeroDeclaracion + ") " +
					 "and XX_YearMonth = '" + yearMonth + "' ";
		int resultado = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				if (rs.getString(1).equals("Y"))
					resultado = 1; //está cerrado
				else
					resultado = 2; //no está cerrado
			}else
				resultado = 3; //no está creado
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
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
		return resultado;
	}
	
    /**
     * Modify Button Method
     */
    public void modify() {   
    	Vector<Object> selecc = new Vector<Object>();
    	int soloAvisoCreODeb = 0;
    	
    	Date utilDate = new Date();
		long lnMilisegundos = utilDate.getTime();
		Timestamp fechaActual = new Timestamp(lnMilisegundos);
			
    	int rows = xTable.getRowCount();
    	String 	notifyNo = number.getText(),
				controlNo = control.getText();
    	
    	Timestamp fechaFactura = calendar.getTimestamp();
    	
    	for (int i = 0; i < rows; i++) {
    		IDColumn id = (IDColumn) xTable.getModel().getValueAt(i, 0);
    		//int notifyId = id.getRecord_ID();
    		
    		if(id.isSelected()){
    			selecc.add(id);
    		}    		
    	}
    	   	
    	if(selecc.size() == 0){
    		ADialog.info(m_WindowNo, new Container(),"Debe seleccionar un aviso");
    		soloAvisoCreODeb = 0;
    	}if(selecc.size() >= 1){
    		soloAvisoCreODeb = soloAvisosCredODeb(selecc);
    	}
    	
    	if(soloAvisoCreODeb == 6){
    		/****Jessica Mendoza****/
    		/****El aviso de credito no esta en el mismo periodo impositivo actual***/
    		/****Se genera la nota, el reverso, pero NO la retencion ****/
    		MVCNPurchasesBook pBook = null;
    		int applNum = 0;
    		/****Condicion: Si el usuario escogio solo una nota de credito****/
    		if(selecc.size() == 1){
    			for (int i = 0; i < selecc.size(); i++) {
    				IDColumn id = (IDColumn) selecc.elementAt(i);
    				int notifyId = id.getRecord_ID();
    				/****Actualiza el numero de documento, el numero de control y la fecha de
    				 * la facturacion en la factura correspondiente 'notifyId'****/
    				MInvoice mInvoice = new MInvoice(Env.getCtx(), notifyId, null);
    				mInvoice.setXX_ControlNumber(controlNo);
    				mInvoice.setDocumentNo(notifyNo);
    				mInvoice.setDateInvoiced(fechaFactura);
    				mInvoice.save();
    				log.fine("Update Invoice = " + notifyId);
    				
    				String sql0 = "Select XX_VCN_PurchasesBook_ID from XX_VCN_PurchasesBook " 
    					+ "where XX_Status='CO' and XX_DocumentNo_ID = " + notifyId;
    				PreparedStatement pstmt = null;
					ResultSet rs = null;
    				try {
    					pstmt = DB.prepareStatement(sql0, null);
    					rs = pstmt.executeQuery();
    					while (rs.next()){
    						pBook = new MVCNPurchasesBook(Env.getCtx(), rs.getInt(1), null);
    						/****Se crea el reverso de la nota de credito****/
    						MVCNPurchasesBook pBookReverso = new MVCNPurchasesBook(Env.getCtx(), 0, null);
    						pBookReverso.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
    						pBookReverso.setC_BPartner_ID(pBook.getC_BPartner_ID());
    						pBookReverso.setXX_DocumentDate(pBook.getXX_DocumentDate());
    						pBookReverso.setXX_DATE(fechaActual);
    						pBookReverso.setC_Order_ID(pBook.getC_Order_ID());
    						pBookReverso.setC_Invoice_ID(pBook.getC_Invoice_ID());
    						pBookReverso.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
    						
    						pBookReverso.setXX_DocumentNo(pBook.getXX_DocumentNo());
    						
    						pBookReverso.setXX_ControlNumber(pBook.getXX_ControlNumber());
    						pBookReverso.setXX_TaxableBase(pBook.getXX_TaxableBase().multiply(new BigDecimal(-1)));
    						pBookReverso.setXX_ExemptBase(pBook.getXX_ExemptBase().multiply(new BigDecimal(-1)));
    						pBookReverso.setXX_NotSubjectBase(pBook.getXX_NotSubjectBase().multiply(new BigDecimal(-1)));
    						pBookReverso.setC_Tax_ID(pBook.getC_Tax_ID());
    						pBookReverso.setXX_TaxAmount(pBook.getXX_TaxAmount().multiply(new BigDecimal(-1)));						
    						pBookReverso.setXX_TotalInvCost(pBook.getXX_TotalInvCost().multiply(new BigDecimal(-1)));
    						pBookReverso.setXX_Converted(true);
    						pBookReverso.setXX_WithholdingTax(new BigDecimal(0));
    						pBookReverso.save();		    			
    					}
    					
    				} catch (SQLException e) {
    					log.log(Level.SEVERE, sql0, e);
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
    			/****Se crea la nota del proveedor con la suma de los montos, 
    			 * de los avisos selecionados****/						
    			MVCNPurchasesBook pBookNota = new MVCNPurchasesBook(Env.getCtx(), 0, null);
    			pBookNota.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
    			pBookNota.setC_BPartner_ID(pBook.getC_BPartner_ID());
    			pBookNota.setXX_DocumentDate(fechaFactura);
    			pBookNota.setXX_DATE(fechaActual);
    			pBookNota.setC_Order_ID(pBook.getC_Order_ID());
    			pBookNota.setC_Invoice_ID(pBook.getC_Invoice_ID());
    			pBookNota.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
    			
    			pBookNota.setXX_DocumentNo(notifyNo);
    			
    			pBookNota.setXX_ControlNumber(controlNo);
    			pBookNota.setXX_Withholding(applNum);
    			pBookNota.setXX_TaxableBase(pBook.getXX_TaxableBase());
    			pBookNota.setXX_ExemptBase(pBook.getXX_ExemptBase());
    			pBookNota.setXX_NotSubjectBase(pBook.getXX_NotSubjectBase());
    			pBookNota.setC_Tax_ID(pBook.getC_Tax_ID());
    			pBookNota.setXX_TaxAmount(pBook.getXX_TaxAmount());
    			pBookNota.setXX_WithholdingTax(new BigDecimal(0));
    			pBookNota.setXX_TotalInvCost(pBook.getXX_TotalInvCost());
    			pBookNota.setXX_Converted(true);
    			pBookNota.save();
    		}
    		else{ 
    			/****Condicion: Si el usuario escogio mas de una nota de credito****/
    			BigDecimal XX_TaxableBase = new BigDecimal(0);
    			BigDecimal XX_ExemptBase  = new BigDecimal(0);
    			BigDecimal XX_NotSubjectBase  = new BigDecimal(0);
    			BigDecimal XX_TaxAmount  = new BigDecimal(0);
    			BigDecimal XX_WithholdingTax  = new BigDecimal(0);
    			BigDecimal XX_TotalInvCost = new BigDecimal(0);
    			MBPartner proveedor = null;			
    			MInvoice mInvoice = null;
    			int notifyId = 0;			
    			for (int i = 0; i < selecc.size(); i++) {
    				IDColumn id = (IDColumn) selecc.elementAt(i);
    				notifyId = id.getRecord_ID();
    				/****Actualiza el numero de documento, el numero de control y la fecha de
    				 * la facturacion en la factura correspondiente 'notifyId'****/
    				mInvoice = new MInvoice(Env.getCtx(), notifyId, null);
    				mInvoice.setXX_ControlNumber(controlNo);
    				mInvoice.setDocumentNo(notifyNo);
    				mInvoice.setDateInvoiced(fechaFactura);
    				mInvoice.save();
    				log.fine("Update Invoice = " + notifyId);
    					
    				String sql0 = "Select XX_VCN_PurchasesBook_ID " +
    						  "from XX_VCN_PurchasesBook " +
    						  "where XX_Status='CO' and XX_DocumentNo_ID = " + notifyId;
    				PreparedStatement pstmt = null;
					ResultSet rs = null;
    				try {
    					pstmt = DB.prepareStatement(sql0, null);
    					rs = pstmt.executeQuery();
    					while (rs.next()){
    						pBook = new MVCNPurchasesBook(Env.getCtx(), rs.getInt(1), null);
    						/****Se crea el reverso de la nota de credito****/
    						MVCNPurchasesBook pBookReverso = new MVCNPurchasesBook(Env.getCtx(), 0, null);
    						pBookReverso.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
    						pBookReverso.setC_BPartner_ID(pBook.getC_BPartner_ID());
    						pBookReverso.setXX_DocumentDate(pBook.getXX_DocumentDate());
    						pBookReverso.setXX_DATE(fechaActual);
    						pBookReverso.setC_Order_ID(pBook.getC_Order_ID());
    						pBookReverso.setC_Invoice_ID(pBook.getC_Invoice_ID());
    						pBookReverso.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
    						
    						pBookReverso.setXX_DocumentNo(pBook.getXX_DocumentNo());
    						
    						pBookReverso.setXX_ControlNumber(pBook.getXX_ControlNumber());
    						pBookReverso.setXX_TaxableBase(pBook.getXX_TaxableBase().multiply(new BigDecimal(-1)));
    						pBookReverso.setXX_ExemptBase(pBook.getXX_ExemptBase().multiply(new BigDecimal(-1)));
    						pBookReverso.setXX_NotSubjectBase(pBook.getXX_NotSubjectBase().multiply(new BigDecimal(-1)));
    						pBookReverso.setC_Tax_ID(pBook.getC_Tax_ID());
    						pBookReverso.setXX_TaxAmount(pBook.getXX_TaxAmount().multiply(new BigDecimal(-1)));
    						pBookReverso.setXX_WithholdingTax(new BigDecimal(0));   						
    						pBookReverso.setXX_TotalInvCost(pBook.getXX_TotalInvCost().multiply(new BigDecimal(-1)));
    						pBookReverso.setXX_Converted(true);
    						pBookReverso.save();
						
    						/****Suma los montos de los avisos selecionados****/
    						XX_TaxableBase = XX_TaxableBase.add(pBook.getXX_TaxableBase());
    						XX_ExemptBase = XX_ExemptBase.add(pBook.getXX_ExemptBase());
    						XX_NotSubjectBase = XX_NotSubjectBase.add(pBook.getXX_NotSubjectBase());
    						XX_TaxAmount = XX_TaxAmount.add(pBook.getXX_TaxAmount());
    						XX_TotalInvCost = XX_TotalInvCost.add(pBook.getXX_TotalInvCost());
											
    						proveedor = new MBPartner(Env.getCtx(), pBook.getC_BPartner_ID(), null);
    						if(!proveedor.getXX_TypeTaxPayer_ID().equals(Env.getCtx().getContext("#XX_L_TYPETAXPAYERFOR_ID"))){
    							MVCNPercenReten porcentaje = new MVCNPercenReten(Env.getCtx(), proveedor.getXX_PercentajeRetention_ID(), null);							
    							XX_WithholdingTax = XX_WithholdingTax.add(pBook.getXX_TaxAmount().multiply(porcentaje.getXX_PERCENRETEN()).divide(new BigDecimal(100)));
    						}else{
    							XX_WithholdingTax = XX_WithholdingTax.add(pBook.getXX_WithholdingTax());
    						}
    					}
    					
    				} catch (SQLException e) {
    					log.log(Level.SEVERE, sql0, e);
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
	
    			/****Se crea la nota del proveedor con la suma de los montos, 
    			 * de los avisos selecionados****/						
    			MVCNPurchasesBook pBookNota = new MVCNPurchasesBook(Env.getCtx(), 0, null);
    			pBookNota.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
    			pBookNota.setC_BPartner_ID(pBook.getC_BPartner_ID());
    			pBookNota.setXX_DocumentDate(fechaFactura);
    			pBookNota.setXX_DATE(fechaActual);
    			pBookNota.setC_Order_ID(pBook.getC_Order_ID());
    			pBookNota.setC_Invoice_ID(pBook.getC_Invoice_ID());
    			pBookNota.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
    			
    			pBookNota.setXX_DocumentNo(notifyNo);
    			
    			pBookNota.setXX_ControlNumber(controlNo);
    			pBookNota.setXX_Withholding(applNum);
    			pBookNota.setXX_TaxableBase(XX_TaxableBase);
    			pBookNota.setXX_ExemptBase(XX_ExemptBase);
    			pBookNota.setXX_NotSubjectBase(XX_NotSubjectBase);
    			pBookNota.setC_Tax_ID(pBook.getC_Tax_ID());
    			pBookNota.setXX_TaxAmount(XX_TaxAmount);
    			pBookNota.setXX_WithholdingTax(new BigDecimal(0)); 
    			pBookNota.setXX_TotalInvCost(XX_TotalInvCost);
    			pBookNota.setXX_Converted(true);
    			pBookNota.save();
    		} 	
    		/****Fin - Jessica Mendoza****/
    		
    		pBook.setXX_WithholdingTax(new BigDecimal(0));
    		pBook.setXX_Converted(true);
    		pBook.save();
    		
    	}else if(soloAvisoCreODeb == 5)
    		ADialog.info(m_WindowNo, new Container(),"no estan los avisos en el mismo periodo impositivo");
    	
    	else if(soloAvisoCreODeb == 1)
    		ADialog.info(m_WindowNo, new Container(),"no puede seleccionar avisos de credito y debito");

		else if(soloAvisoCreODeb == 2)
    		ADialog.info(m_WindowNo, new Container(),"no son avisos, son facturas");

		else if(soloAvisoCreODeb == 3 || soloAvisoCreODeb == 7){
			// CREDITO y DEBITO
			MVCNPurchasesBook pBook = null;
			
			if(selecc.size() == 1){
				for (int i = 0; i < selecc.size(); i++) {
					IDColumn id = (IDColumn) selecc.elementAt(i);
		    		int notifyId = id.getRecord_ID();
		    		//System.out.println("Credito: "+notifyId);
		    		
					// Updating C_Invoice documentNo & controlNumber
	    			MInvoice mInvoice = new MInvoice(Env.getCtx(), notifyId, null);
	    			mInvoice.setXX_ControlNumber(controlNo);
	    			mInvoice.setDocumentNo(notifyNo);
	    			mInvoice.setDateInvoiced(fechaFactura);
	    			if(mInvoice.getDocStatus().equals("DR")){
		    			mInvoice.setDocAction(MInvoice.DOCACTION_Complete);
		    			DocumentEngine.processIt(mInvoice, MInvoice.DOCACTION_Complete);
	    			}
	    			mInvoice.setXX_ApprovalDate(fechaActual);
	    			mInvoice.save();
	    			log.fine("Update Invoice = " + notifyId);
	    			
	    			// Generating Retention Application Number
	    			int applNum = 0;
	    			if(mInvoice.getXX_TaxAmount().compareTo(new BigDecimal(0)) != 0){
		    			MVCNApplicationNumber mApplNum = new MVCNApplicationNumber(Env.getCtx(), 0, null);
		    			applNum = (mApplNum.generateApplicationNumber(fechaActual, 
		    							mInvoice.getC_Order_ID(), false, null)); 
	    			}
	    					
	    			String sql0 = "Select XX_VCN_PurchasesBook_ID from XX_VCN_PurchasesBook " 
	    				+ "where XX_Status='CO' and XX_DocumentNo_ID = " + notifyId;
	    			PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(sql0, null);
						rs = pstmt.executeQuery();
						while (rs.next()){
							pBook = new MVCNPurchasesBook(Env.getCtx(), rs.getInt(1), null);
							pBook.setXX_ControlNumber(controlNo);
							MBPartner proveedor = new MBPartner(Env.getCtx(), pBook.getC_BPartner_ID(), null);
							
							if(!proveedor.getXX_TypeTaxPayer_ID().equals(Env.getCtx().getContextAsInt("#XX_L_TYPETAXPAYERFOR_ID"))){
								MVCNPercenReten porcentaje = new MVCNPercenReten(Env.getCtx(), proveedor.getXX_PercentajeRetention_ID(), null);	
								if (util.clientRetentionAgent(m_AD_Client_ID))
									pBook.setXX_WithholdingTax(pBook.getXX_TaxAmount().multiply(porcentaje.getXX_PERCENRETEN()).divide(new BigDecimal(100)));
				    			else
				    				pBook.setXX_WithholdingTax(new BigDecimal(0)); 
							}
							
							if(applNum != 0)
								pBook.setXX_Withholding(applNum);
							pBook.setXX_DocumentDate(fechaFactura);
							pBook.setXX_DATE(fechaActual);
							pBook.setXX_Converted(true);
							pBook.setXX_SynchronizedPB(false);
							
							pBook.setXX_DocumentNo(notifyNo);
							
							pBook.save();
							
							
							mInvoice.setDateInvoiced(fechaFactura);
			    			mInvoice.setTotalLines(pBook.getXX_TaxableBase());
			    			mInvoice.setXX_TaxAmount(pBook.getXX_TaxAmount());
			    			mInvoice.setGrandTotal(pBook.getXX_TotalInvCost());
							mInvoice.save();
			    			
						}
						
						// Showing Retention Document
						Utilities util = new Utilities();
						util.showReport(pBook);
					} catch (SQLException e) {
						log.log(Level.SEVERE, sql0, e);
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
					//fin try catch
				}//fin del for
			}//fin del if(selecc.size() == 1)
			else{ 
				/*********/
				//Vector<Object> listaAvisosCopiar = new Vector<Object>();
				
				//si selecciona mas de un aviso de credito
				BigDecimal XX_TaxableBase = new BigDecimal(0);
				BigDecimal XX_ExemptBase  = new BigDecimal(0);
				BigDecimal XX_NotSubjectBase  = new BigDecimal(0);
				BigDecimal XX_TaxAmount  = new BigDecimal(0);
				BigDecimal XX_WithholdingTax  = new BigDecimal(0);
				BigDecimal XX_TotalInvCost = new BigDecimal(0);
				MBPartner proveedor = null;
				
				MInvoice mInvoice = null;
				int applNum = 0;
				int notifyId = 0;
    			
				
				for (int i = 0; i < selecc.size(); i++) {
					IDColumn id = (IDColumn) selecc.elementAt(i);
		    		notifyId = id.getRecord_ID();
		    		//System.out.println("Debito: "+notifyId);
		    		
		    		// Updating C_Invoice documentNo & controlNumber
	    			mInvoice = new MInvoice(Env.getCtx(), notifyId, null);
	    			mInvoice.setXX_ControlNumber(controlNo);
	    			mInvoice.setDocumentNo(notifyNo);
	    			mInvoice.setDateInvoiced(fechaFactura);
	    			mInvoice.setXX_ApprovalDate(fechaActual);
	    			mInvoice.setDocAction(MInvoice.DOCACTION_Complete);
	    			DocumentEngine.processIt(mInvoice, MInvoice.DOCACTION_Complete);
					mInvoice.save();
					
	    			log.fine("Update Invoice = " + notifyId);
	    					
	    			String sql0 = "Select XX_VCN_PurchasesBook_ID " +
	    						  "from XX_VCN_PurchasesBook " +
	    						  "where XX_Status='CO' and XX_DocumentNo_ID = " + notifyId;
	    			PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(sql0, null);
						rs = pstmt.executeQuery();
						while (rs.next()){
							pBook = new MVCNPurchasesBook(Env.getCtx(), rs.getInt(1), null);
							pBook.setXX_DocumentDate(fechaFactura);
							pBook.setXX_DATE(fechaActual);
							pBook.setXX_DocumentNo(notifyNo);
							pBook.setXX_Converted(true);
							pBook.save();
							
							//creo el reverso
							MVCNPurchasesBook pBookReverso = new MVCNPurchasesBook(Env.getCtx(), 0, null);
							pBookReverso.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
							pBookReverso.setC_BPartner_ID(pBook.getC_BPartner_ID());
							pBookReverso.setXX_DocumentDate(pBook.getXX_DocumentDate());
							pBookReverso.setXX_DATE(fechaActual);
							pBookReverso.setC_Order_ID(pBook.getC_Order_ID());
							pBookReverso.setC_Invoice_ID(pBook.getC_Invoice_ID());
							pBookReverso.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
							pBookReverso.setXX_DocumentNo(pBook.getXX_DocumentNo());
							pBookReverso.setXX_ControlNumber(pBook.getXX_ControlNumber());
							//pBookReverso.setXX_Withholding(pBook.getXX_Withholding());
							pBookReverso.setXX_TaxableBase(pBook.getXX_TaxableBase().multiply(new BigDecimal(-1)));
							pBookReverso.setXX_ExemptBase(pBook.getXX_ExemptBase().multiply(new BigDecimal(-1)));
							pBookReverso.setXX_NotSubjectBase(pBook.getXX_NotSubjectBase().multiply(new BigDecimal(-1)));
							pBookReverso.setC_Tax_ID(pBook.getC_Tax_ID());
							pBookReverso.setXX_TaxAmount(pBook.getXX_TaxAmount().multiply(new BigDecimal(-1)));
							if (util.clientRetentionAgent(m_AD_Client_ID))
								pBookReverso.setXX_WithholdingTax(pBook.getXX_WithholdingTax().multiply(new BigDecimal(-1)));
			    			else
			    				pBookReverso.setXX_WithholdingTax(new BigDecimal(0)); 
							pBookReverso.setXX_TotalInvCost(pBook.getXX_TotalInvCost().multiply(new BigDecimal(-1)));
							pBookReverso.setXX_Converted(true);
							pBookReverso.save();
							
							XX_TaxableBase = XX_TaxableBase.add(pBook.getXX_TaxableBase());
							XX_ExemptBase = XX_ExemptBase.add(pBook.getXX_ExemptBase());
							XX_NotSubjectBase = XX_NotSubjectBase.add(pBook.getXX_NotSubjectBase());
							XX_TaxAmount = XX_TaxAmount.add(pBook.getXX_TaxAmount());
							//XX_WithholdingTax = XX_WithholdingTax.add(pBook.getXX_WithholdingTax());
							XX_TotalInvCost = XX_TotalInvCost.add(pBook.getXX_TotalInvCost());
							
							
							proveedor = new MBPartner(Env.getCtx(), pBook.getC_BPartner_ID(), null);
							if(!proveedor.getXX_TypeTaxPayer_ID().equals(Env.getCtx().getContext("#XX_L_TYPETAXPAYERFOR_ID"))){
								MVCNPercenReten porcentaje = new MVCNPercenReten(Env.getCtx(), proveedor.getXX_PercentajeRetention_ID(), null);							
								XX_WithholdingTax = XX_WithholdingTax.add(pBook.getXX_TaxAmount().multiply(porcentaje.getXX_PERCENRETEN()).divide(new BigDecimal(100)));
							}else{
								XX_WithholdingTax = XX_WithholdingTax.add(pBook.getXX_WithholdingTax());
							}
							
							
						}
						
					} catch (SQLException e) {
						log.log(Level.SEVERE, sql0, e);
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
				
				// Generating Retention Application Number
    			if(mInvoice.getXX_TaxAmount().compareTo(new BigDecimal(0)) != 0){
	    			MVCNApplicationNumber mApplNum = new MVCNApplicationNumber(Env.getCtx(), 0, null);
	    			applNum = (mApplNum.generateApplicationNumber(fechaActual, 
	    							mInvoice.getC_Order_ID(), false, null)); 
    			}
    			
				//creo la nota						
				MVCNPurchasesBook pBookNota = new MVCNPurchasesBook(Env.getCtx(), 0, null);
				pBookNota.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
				pBookNota.setC_BPartner_ID(pBook.getC_BPartner_ID());
				pBookNota.setXX_DocumentDate(fechaFactura);
				pBookNota.setXX_DATE(fechaActual);
				pBookNota.setC_Order_ID(pBook.getC_Order_ID());
				pBookNota.setC_Invoice_ID(pBook.getC_Invoice_ID());
				pBookNota.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
				
				pBookNota.setXX_DocumentNo(notifyNo);
				
				pBookNota.setXX_ControlNumber(controlNo);
				pBookNota.setXX_Withholding(applNum);
				pBookNota.setXX_TaxableBase(XX_TaxableBase);
				pBookNota.setXX_ExemptBase(XX_ExemptBase);
				pBookNota.setXX_NotSubjectBase(XX_NotSubjectBase);
				pBookNota.setC_Tax_ID(pBook.getC_Tax_ID());
				pBookNota.setXX_TaxAmount(XX_TaxAmount);
				if (util.clientRetentionAgent(m_AD_Client_ID))
					pBookNota.setXX_WithholdingTax(XX_WithholdingTax);
    			else
    				pBookNota.setXX_WithholdingTax(new BigDecimal(0)); 
				pBookNota.setXX_TotalInvCost(XX_TotalInvCost);
				pBookNota.setXX_Converted(true);
				pBookNota.save();
				
				mInvoice.setDateInvoiced(fechaFactura);
    			mInvoice.setTotalLines(XX_TaxableBase);
    			mInvoice.setXX_TaxAmount(XX_TaxAmount);
    			mInvoice.setGrandTotal(XX_TotalInvCost);
				mInvoice.save();				
				
				// Showing Retention Document
				Utilities util = new Utilities();
				util.showReport(pBookNota);
				/***************/
			}//fin del else. if(selecc.size() == 1) else
						
		}else if(soloAvisoCreODeb == 4){
			//Vector<Object> listaAvisosCopiar = new Vector<Object>();
			
			// DEBITO - diferente periodo impositivo
			BigDecimal XX_TaxableBase = new BigDecimal(0);
			BigDecimal XX_ExemptBase  = new BigDecimal(0);
			BigDecimal XX_NotSubjectBase  = new BigDecimal(0);
			BigDecimal XX_TaxAmount  = new BigDecimal(0);
			BigDecimal XX_WithholdingTax  = new BigDecimal(0);
			BigDecimal XX_TotalInvCost = new BigDecimal(0);
			
			MBPartner proveedor = null;
			
			MVCNPurchasesBook pBook = null;
			
			for (int i = 0; i < selecc.size(); i++) {
				IDColumn id = (IDColumn) selecc.elementAt(i);
	    		int notifyId = id.getRecord_ID();

	    		//System.out.println("Debito: "+notifyId);
	    		
	    		// Updating C_Invoice documentNo & controlNumber
    			MInvoice mInvoice = new MInvoice(Env.getCtx(), notifyId, null);
    			
    			if(mInvoice.getDocStatus().equals("DR")){
	    			mInvoice.setDocAction(MInvoice.DOCACTION_Complete);
	    			DocumentEngine.processIt(mInvoice, MInvoice.DOCACTION_Complete);
	    			mInvoice.setXX_ApprovalDate(fechaActual);
					mInvoice.save();
				}
				//listaAvisosCopiar.add(mInvoice);
				
    			log.fine("Update Invoice = " + notifyId);
    			
    					
    			String sql0 = "Select XX_VCN_PurchasesBook_ID from XX_VCN_PurchasesBook " 
    				+ "where XX_Status='CO' and XX_DocumentNo_ID = " + notifyId;
    			PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(sql0, null);
					rs = pstmt.executeQuery();
					while (rs.next()){
						pBook = new MVCNPurchasesBook(Env.getCtx(), rs.getInt(1), null);
						//pBook.setXX_DocumentDate(fechaFactura);
						//pBook.setXX_DATE(fechaActual);
						//pBook.setXX_ControlNumber(controlNo);
						/*if(applNum != 0)
							pBook.setXX_Withholding(applNum);*/
						//pBook.save();
						
						//creo el reverso
						MVCNPurchasesBook pBookReverso = new MVCNPurchasesBook(Env.getCtx(), 0, null);
						pBookReverso.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
						pBookReverso.setC_BPartner_ID(pBook.getC_BPartner_ID());
						pBookReverso.setXX_DocumentDate(pBook.getXX_DocumentDate());
						pBookReverso.setXX_DATE(fechaActual);
						pBookReverso.setC_Order_ID(pBook.getC_Order_ID());
						pBookReverso.setC_Invoice_ID(pBook.getC_Invoice_ID());
						pBookReverso.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
						
						pBookReverso.setXX_DocumentNo(pBook.getXX_DocumentNo());
						
						pBookReverso.setXX_ControlNumber(pBook.getXX_ControlNumber());
						//pBookReverso.setXX_Withholding(pBook.getXX_Withholding());
						pBookReverso.setXX_TaxableBase(pBook.getXX_TaxableBase().multiply(new BigDecimal(-1)));
						pBookReverso.setXX_ExemptBase(pBook.getXX_ExemptBase().multiply(new BigDecimal(-1)));
						pBookReverso.setXX_NotSubjectBase(pBook.getXX_NotSubjectBase().multiply(new BigDecimal(-1)));
						pBookReverso.setC_Tax_ID(pBook.getC_Tax_ID());
						pBookReverso.setXX_TaxAmount(pBook.getXX_TaxAmount().multiply(new BigDecimal(-1)));
						if (util.clientRetentionAgent(m_AD_Client_ID))
							pBookReverso.setXX_WithholdingTax(pBook.getXX_WithholdingTax().multiply(new BigDecimal(-1)));
		    			else
		    				pBookReverso.setXX_WithholdingTax(new BigDecimal(0));
						pBookReverso.setXX_TotalInvCost(pBook.getXX_TotalInvCost().multiply(new BigDecimal(-1)));
						pBookReverso.setXX_Converted(true);
						pBookReverso.save();
						
						XX_TaxableBase = XX_TaxableBase.add(pBook.getXX_TaxableBase());
						XX_ExemptBase = XX_ExemptBase.add(pBook.getXX_ExemptBase());
						XX_NotSubjectBase = XX_NotSubjectBase.add(pBook.getXX_NotSubjectBase());
						XX_TaxAmount= XX_TaxAmount.add(pBook.getXX_TaxAmount());
						//XX_WithholdingTax.add(pBook.getXX_WithholdingTax());
						XX_TotalInvCost = XX_TotalInvCost.add(pBook.getXX_TotalInvCost());
						
						proveedor = new MBPartner(Env.getCtx(), pBook.getC_BPartner_ID(), null);
						if(!proveedor.getXX_TypeTaxPayer_ID().equals(Env.getCtx().getContext("#XX_L_TYPETAXPAYERFOR_ID"))){
							MVCNPercenReten porcentaje = new MVCNPercenReten(Env.getCtx(), proveedor.getXX_PercentajeRetention_ID(), null);							
							XX_WithholdingTax = XX_WithholdingTax.add(pBook.getXX_TaxAmount().multiply(porcentaje.getXX_PERCENRETEN()).divide(new BigDecimal(100)));
						}else{
							XX_WithholdingTax = XX_WithholdingTax.add(pBook.getXX_WithholdingTax());
						}
		    			
					}
					
					// Generating Retention Application Number
	    			int applNum = 0;
	    			if(mInvoice.getXX_TaxAmount().compareTo(new BigDecimal(0)) != 0){
		    			MVCNApplicationNumber mApplNum = new MVCNApplicationNumber(Env.getCtx(), 0, null);
		    			applNum = (mApplNum.generateApplicationNumber(fechaActual, 
		    							mInvoice.getC_Order_ID(), false, null)); 
	    			}
					
					//creo la nota						
					MVCNPurchasesBook pBookNota = new MVCNPurchasesBook(Env.getCtx(), 0, null);
					pBookNota.setM_Warehouse_ID(pBook.getM_Warehouse_ID());
					pBookNota.setC_BPartner_ID(pBook.getC_BPartner_ID());
					pBookNota.setXX_DocumentDate(fechaFactura);
					pBookNota.setXX_DATE(fechaActual);
					pBookNota.setC_Order_ID(pBook.getC_Order_ID());
					pBookNota.setC_Invoice_ID(pBook.getC_Invoice_ID());
					pBookNota.setXX_DocumentNo_ID(pBook.getXX_DocumentNo_ID());
					
					pBookNota.setXX_DocumentNo(notifyNo);
					
					pBookNota.setXX_ControlNumber(controlNo);
					pBookNota.setXX_Withholding(applNum);
					pBookNota.setXX_TaxableBase(XX_TaxableBase);
					pBookNota.setXX_ExemptBase(XX_ExemptBase);
					pBookNota.setXX_NotSubjectBase(XX_NotSubjectBase);
					pBookNota.setC_Tax_ID(pBook.getC_Tax_ID());
					pBookNota.setXX_TaxAmount(XX_TaxAmount);
					
					/*proveedor = new MBPartner(Env.getCtx(), pBook.getC_BPartner_ID(), null);
					if(!proveedor.getXX_TypeTaxPayer_ID().equals(Env.getCtx().getContext("#XX_L_TYPETAXPAYERFOR_ID"))){
						MVCNPercenReten porcentaje = new MVCNPercenReten(Env.getCtx(), proveedor.getXX_PercentajeRetention_ID(), null);							
						pBookNota.setXX_WithholdingTax(pBook.getXX_TaxAmount().multiply(porcentaje.getXX_PERCENRETEN()).divide(new BigDecimal(100)));
					}else{ 
						pBookNota.setXX_WithholdingTax(pBook.getXX_WithholdingTax());
					}*/
					
					if (util.clientRetentionAgent(m_AD_Client_ID))
						pBookNota.setXX_WithholdingTax(XX_WithholdingTax);
	    			else
	    				pBookNota.setXX_WithholdingTax(new BigDecimal(0));

					pBookNota.setXX_TotalInvCost(XX_TotalInvCost);
					pBookNota.setXX_Converted(true);
					pBookNota.save();
					
					mInvoice.setDateInvoiced(fechaFactura);
	    			mInvoice.setTotalLines(XX_TaxableBase);
	    			mInvoice.setXX_TaxAmount(XX_TaxAmount);
	    			mInvoice.setGrandTotal(XX_TotalInvCost);
					mInvoice.save();
					
					// Showing Retention Document
					Utilities util = new Utilities();
					util.showReport(pBookNota);
					
				} catch (SQLException e) {
					log.log(Level.SEVERE, sql0, e);
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
			
			pBook.setXX_WithholdingTax(new BigDecimal(0));
			pBook.setXX_Converted(true);
    		pBook.save();
		}
    	
    	if (soloAvisoCreODeb == 8){
			//System.out.println("Está cerrada la declaración, para ese periodo impositivo");
			ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_ClosedPeriodTax"));
		}else if (soloAvisoCreODeb == 9){
			//System.out.println("No está creada la declaración, para ese periodo impositivo");
			ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_CreatedDeclarationPeriod"));
		}else{
	    	/****Jessica Mendoza****/
	    	/****Se verifica el estado del socio de negocio, para ser modificado 
	    	 * dependiendo de que tenga o no avisos pendientes por recibir****/
	    	if (((KeyNamePair)vendor.getSelectedItem()).getKey() != 0){
	    		String status = statusPartner(((KeyNamePair)vendor.getSelectedItem()).getKey());
	    		if (status.equals("BLO")){
	    			boolean flag = receivePending(((KeyNamePair)vendor.getSelectedItem()).getKey());
	    			if (flag == true)
	    				updateStatusPartner(((KeyNamePair)vendor.getSelectedItem()).getKey());
	    		}
	    	}
		}
    }

    /**
     * Verifica si el socio de negocio tiene avisos pendientes por recibir
     * @author Jessica Mendoza
     * @param idPartner id del socio de negocio
     * @return boolean
     */
    public boolean receivePending(int idPartner){
    	int count = 0;
    	String sql = "select count(*) " +
					 "from C_Invoice i " +
					 "join C_BPartner b on (b.C_BPartner_ID = i.C_BPartner_ID) " +
					 "join XX_VMR_Department dep on (i.XX_VMR_Department_ID = dep.XX_VMR_Department_ID) " +
					 "left JOIN C_Invoice j on (j.C_Invoice_ID = i.REF_Invoice_ID) " +
					 "where i.C_DocTypeTarget_ID in ("+Env.getCtx().getContext("#XX_L_C_DOCTYPEDEBIT_ID")+", "+
					 Env.getCtx().getContext("#XX_L_C_DOCTYPECREDIT_ID")+") " +
					 "and trim(i.XX_ControlNumber) is null " +
					 "and ((i.DocStatus = 'DR') or (i.DocStatus = 'CO')) " +
					 "and b.C_BPartner_ID = " + idPartner;
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
    	try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		} finally{
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (count == 0)
			return true;
		else
			return false;
    }
    
    /**
     * Modifica el estado del socio de negocio a ACTIVO
     * @author Jessica Mendoza
     * @param idPartner id del socio de negocio
     */
    public void updateStatusPartner(int idPartner){
    	String sql = "update C_BPartner " +
    				 "set C_BP_Status_ID = (select C_BP_Status_ID from C_BP_Status where value = 'ACT') " +
    				 "where C_BPartner_ID = " + idPartner;

	    try {
	    	DB.executeUpdate(null, sql);
	    } catch(Exception e)
	    {
	    	log.log(Level.SEVERE, sql);
	    	e.printStackTrace();
	    }
    	
    }
    
    /**
     * Busca el status del socio de negocio
     * @author Jessica Mendoza
     * @param idPartner id del socio de negocio
     * @return status estado del socio de negocio
     */
    public String statusPartner(int idPartner){
    	String status = null;
    	String sql = "select sta.value " +
    				 "from C_BP_Status sta, C_BPartner par " +
    				 "where  par.C_BP_Status_ID = sta.C_BP_Status_ID " +
    				 "and par.C_BPartner_id = " + idPartner;
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
    	try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				status = rs.getString(1);
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
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
    	return status;
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
	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{					
		if (e.getSource() == modify){
			modify();
			data();
		}else if (e.getSource() == search)
			data();
		
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

}
