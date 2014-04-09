package compiere.model.payments.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import compiere.model.birt.BIRTReport;
import compiere.model.cds.Utilities;


/**
 * Forma en Compiere: XX_BalanceAPayableForm (Saldo de las Cuentas por Pagar)
 * Se encarga de generar las cuentas por pagar, de las facturas 
 * que se encuentran pendientes por pagar
 * @author Jessica Mendoza
 *
 */
public class XX_BalanceAPayableForm extends CPanel
implements FormPanel, ActionListener, ListSelectionListener {

private static final long serialVersionUID = 1L;
	
	/****Window No****/
	private int m_WindowNo = 0;
	
	/****FormFrame****/
	private FormFrame m_frame;
	
	/****Logger****/
	static CLogger log = CLogger.getCLogger(XX_AuxiliaryAccountPayForm.class);
	
	/****Cliente****/
	private int m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int m_by = Env.getCtx().getAD_User_ID();
	StringBuffer m_sql = null;
	StringBuffer m_sqlDetail = null;
	String m_groupBy = "";
	String m_orderBy = "";
	Ctx ctx = Env.getCtx();
	
	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	
	private CPanel beforePanel = new CPanel();
	private BorderLayout beforeLayout = new BorderLayout();
	private CPanel afterPanel = new CPanel();
	private BorderLayout afterLayout = new BorderLayout();
	private JSplitPane splitBefore = new JSplitPane();
	
	private CPanel northPanelB = new CPanel(); //campos before
	private GridBagLayout northLayoutB = new GridBagLayout();
	private CPanel northPanelA = new CPanel(); //campos after
	private GridBagLayout northLayoutA = new GridBagLayout();
	private CPanel centerPanelB = new CPanel(); //tabla before
	private BorderLayout centerLayoutB = new BorderLayout(5,5);
	private CPanel centerPanelA = new CPanel(); //tabla after
	private BorderLayout centerLayoutA = new BorderLayout(5,5);
	private CPanel central = new CPanel(); //filtro
	private GridBagLayout centralLayout = new GridBagLayout();
	private CPanel southPanelB = new CPanel(); //totales before
	private GridBagLayout southLayoutB = new GridBagLayout();
	private CPanel southPanelA = new CPanel(); //totales after
	private GridBagLayout southLayoutA = new GridBagLayout();
	private CPanel medio = new CPanel();
	private GridBagLayout medioLayout = new GridBagLayout();
	
	private JScrollPane xScrollPaneBefore = new JScrollPane();
	private JScrollPane xScrollPaneAfter = new JScrollPane();
	private MiniTable xTableBefore = new MiniTable();
	private MiniTable xTableAfter = new MiniTable();	
	
	private TitledBorder xBorderTables =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_BalanceAP"));
	private TitledBorder xBorderBefore = 
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_Present")); 
	private TitledBorder xBorderAfter =
		new TitledBorder(Msg.translate(Env.getCtx(), "XX_Comparative")); 

	/****Botones****/
	private CButton bSearchB = new CButton();
	private CButton bSearchA = new CButton();
	private CButton bReport = new CButton();

	/****Labels-ComboBox-Fecha****/
	private CLabel monthLabel = new CLabel();
	private CComboBox monthCombo = new CComboBox();
	private CLabel quarterlyLabel = new CLabel();
	private CComboBox quarterlyCombo = new CComboBox();
	private CLabel dateFromlabelB = new CLabel();
	private CLabel dateToLabelB = new CLabel();
	private VDate dateFromB = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateToB = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo")); 
	private CLabel dateFromlabelA = new CLabel();
	private CLabel dateToLabelA = new CLabel();
	private VDate dateFromA = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateToA = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));	
	private CLabel categoryLabel = new CLabel();
	private CComboBox categoryCombo = new CComboBox();	
	private CLabel partnerLabel = new CLabel();
	private CComboBox partnerCombo = new CComboBox();
	private CLabel paymentTermLabel = new CLabel();
	private CComboBox paymentTermCombo = new CComboBox();
	
	private CLabel payForeignLabelB = new CLabel();
	private CLabel amountForeignB = new CLabel();
	private CLabel payLocalLabelB = new CLabel();
	private CLabel amountLocalB = new CLabel();
	private CLabel payForeignLabelA = new CLabel();
	private CLabel amountForeignA = new CLabel();
	private CLabel payLocalLabelA = new CLabel();
	private CLabel amountLocalA = new CLabel();
	private CLabel medioLabel = new CLabel();
	
	private JRadioButton periodoRButton = new JRadioButton("Rango", true);
	private JRadioButton mesRButton = new JRadioButton("Mes" , false);
	private JRadioButton trimestreRButton = new JRadioButton("Trimestral" , false);

	private ButtonGroup bgroup = new ButtonGroup();

	/****Variables****/
	static Integer sync = new Integer(0);
	Utilities util = new Utilities();
	int contA = 0;
	int contB = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS"); 
	Timestamp toPrimero;
	Timestamp fromPrimero;
	Timestamp toSegundo;
	Timestamp fromSegundo;
	NumberFormat formato = NumberFormat.getInstance();
	String sqlPrimero;
	String sqlSegundo;

	@Override
	public void dispose() {
		
	}

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		try{
			jbInit();
			dynInitFirst();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);	
			frame.setResizable(false); //Se utiliza para bloquear el boton maximizar de la ventana
		}
		catch(Exception e){
			log.log(Level.SEVERE, "", e);
		}		
	}
	
	/**
	 * Organiza las posiciones de cada etiqueta en la forma
	 * @throws Exception
	 */
	private void jbInit() throws Exception{
		/****Seteo de las etiquetas****/
		monthLabel.setText(Msg.getMsg(Env.getCtx(), "Month"));
		quarterlyLabel.setText(Msg.getMsg(Env.getCtx(), "XX_Quarterly"));
		dateFromlabelB.setText(Msg.translate(Env.getCtx(), "XX_DateFromPay"));
		dateToLabelB.setText(Msg.translate(Env.getCtx(), "XX_DateToPay"));
		dateFromlabelA.setText(Msg.translate(Env.getCtx(), "XX_DateFromPay"));
		dateToLabelA.setText(Msg.translate(Env.getCtx(), "XX_DateToPay"));
		categoryLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CategoryComercial"));
		partnerLabel.setText(Msg.getMsg(Env.getCtx(), "BPartner"));
		paymentTermLabel.setText(Msg.getMsg(Env.getCtx(), "XX_PaymentTerm"));		
		payForeignLabelB.setText(Msg.translate(Env.getCtx(), "XX_TotalForeign"));
		payLocalLabelB.setText(Msg.translate(Env.getCtx(), "XX_TotalNational"));
		payForeignLabelA.setText(Msg.translate(Env.getCtx(), "XX_TotalForeign"));
		payLocalLabelA.setText(Msg.translate(Env.getCtx(), "XX_TotalNational"));		
		medioLabel.setText("");
		
		monthCombo.addActionListener(this);
		quarterlyCombo.addActionListener(this);
		
		bgroup.add(periodoRButton);
		bgroup.add(mesRButton);
		bgroup.add(trimestreRButton);
		periodoRButton.addActionListener(this);
		mesRButton.addActionListener(this);
		trimestreRButton.addActionListener(this);
		
		bSearchB.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearchB.setEnabled(true);	
		bSearchB.addActionListener(this);
		bSearchA.setText(Msg.translate(Env.getCtx(), "XX_Search"));
		bSearchA.setEnabled(true);	
		bSearchA.addActionListener(this);
		bReport.setText(Msg.translate(Env.getCtx(), "XX_Report"));
		bReport.setEnabled(false);
		bReport.addActionListener(this);
		
		central.setLayout(centralLayout);
		northPanelB.setLayout(northLayoutB);	
		northPanelA.setLayout(northLayoutA);
		southPanelB.setLayout(southLayoutB);
		southPanelA.setLayout(southLayoutA);
		medio.setLayout(medioLayout);

		centerPanelB.setLayout(centerLayoutB); //Primera tabla
		xScrollPaneBefore.setBorder(xBorderTables);
		xScrollPaneBefore.setPreferredSize(new Dimension(670, 220));
		xScrollPaneBefore.getViewport().add(xTableBefore);
		centerPanelB.add(xScrollPaneBefore,  BorderLayout.NORTH);

		mainPanel.setLayout(mainLayout);
		mainPanel.add(beforePanel, BorderLayout.NORTH);
		beforePanel.setLayout(beforeLayout);
		beforePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		beforePanel.setBorder(xBorderBefore);
		beforePanel.add(central, BorderLayout.NORTH);
		beforePanel.add(northPanelB,  BorderLayout.CENTER); 	
		beforePanel.add(splitBefore, BorderLayout.SOUTH);
		splitBefore.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitBefore.setTopComponent(centerPanelB);
		splitBefore.setBottomComponent(southPanelB);
		splitBefore.add(centerPanelB, JSplitPane.TOP);
		splitBefore.add(southPanelB, JSplitPane.BOTTOM);
		splitBefore.setContinuousLayout(true);
		splitBefore.setPreferredSize(new Dimension(670,270));
		splitBefore.setDividerLocation(220);

		centerPanelA.setLayout(centerLayoutA); //Segunda tabla
		xScrollPaneAfter.setBorder(xBorderTables);
		xScrollPaneAfter.setPreferredSize(new Dimension(670, 220));
		xScrollPaneAfter.getViewport().add(xTableAfter);
		centerPanelA.add(xScrollPaneAfter,  BorderLayout.NORTH);

		mainPanel.add(medio, BorderLayout.CENTER);
		mainPanel.add(afterPanel, BorderLayout.SOUTH);
		afterPanel.setLayout(afterLayout);
		afterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		afterPanel.setBorder(xBorderAfter);
		afterPanel.add(northPanelA, BorderLayout.NORTH);
		afterPanel.add(centerPanelA,  BorderLayout.CENTER); 	
		afterPanel.add(southPanelA, BorderLayout.SOUTH);

		/****Panel Before****/
		central.add(periodoRButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		central.add(mesRButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		central.add(trimestreRButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		
		/****Etiquetas de la primera fila****/
		northPanelB.add(dateFromlabelB, new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 12, 5, 5), 0, 0));
		northPanelB.add(dateFromB, new GridBagConstraints(1, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 5, 5, 0), 0, 0));
		northPanelB.add(dateToB, new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 5, 5, 0), 0, 0));
		northPanelB.add(dateToLabelB, new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 5), 0, 0));
		
		/****Etiquetas de la segunda fila****/
		northPanelB.add(monthLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(3, 5, 5, 5), 0, 0));
		northPanelB.add(monthCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 5, 5, 0), 0, 0));
		northPanelB.add(quarterlyCombo, new GridBagConstraints(2, 1, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 5, 5, 0), 0, 0));
		northPanelB.add(quarterlyLabel, new GridBagConstraints(3, 1, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NORTHWEST, 
				new Insets(3, 5, 5, 5), 0, 0));
	
		/****Etiquetas de la tercera fila****/
		northPanelB.add(partnerLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(3, 5, 5, 5), 0, 0));
		northPanelB.add(partnerCombo, new GridBagConstraints(1, 2, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 5, 5, 0), 0, 0)); 
		northPanelB.add(paymentTermCombo, new GridBagConstraints(2, 2, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 5, 5, 0), 0, 0));
		northPanelB.add(paymentTermLabel, new GridBagConstraints(3, 2, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(3, 5, 5, 5), 0, 0));
		
		/****Etiquetas de la cuarta fila****/
		northPanelB.add(categoryLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(3, 5, 15, 5), 0, 0));
		northPanelB.add(categoryCombo, new GridBagConstraints(1, 3, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 5, 15, 0), 0, 0)); 
		northPanelB.add(bSearchB, new GridBagConstraints(4, 3, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 12, 15, 12), 0, 0));
		
		/****Etiquetas de la quinta fila****/
		southPanelB.add(payLocalLabelB,   new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 10, 12, 5), 0, 0));
		southPanelB.add(amountLocalB,   new GridBagConstraints(1, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 12, 0), 0, 0));
		southPanelB.add(payForeignLabelB,   new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 12, 5), 0, 0));
		southPanelB.add(amountForeignB,   new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 12, 0), 0, 0));
		/****Fin del Panel Before****/
		
		/****Panel central****/
		medio.add(medioLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		/****Fin del Panel Central****/
		
		/****Panel After****/
		/****Etiquetas de la primera fila****/
		northPanelA.add(dateFromlabelA, new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 12, 5, 5), 0, 0));
		northPanelA.add(dateFromA, new GridBagConstraints(1, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 5, 5, 0), 15, 0));
		northPanelA.add(dateToA, new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 5, 5, 0), 15, 0));
		northPanelA.add(dateToLabelA, new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(12, 7, 5, 5), 0, 0));
		
		/****Etiquetas de la segunda fila****/
		northPanelA.add(bSearchA, new GridBagConstraints(5, 1, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 12, 15, 12), 0, 0)); 
		northPanelA.add(bReport, new GridBagConstraints(6, 1, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 12, 15, 12), 0, 0)); 

		/****Etiquetas de la quinta fila****/
		southPanelA.add(payLocalLabelA,   new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 10, 12, 5), 0, 0));
		southPanelA.add(amountLocalA,   new GridBagConstraints(1, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 12, 0), 0, 0));
		southPanelA.add(payForeignLabelA,   new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 5, 12, 5), 0, 0));
		southPanelA.add(amountForeignA,   new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 0, 12, 0), 0, 0));
		/****Fin del Panel After****/
		
		monthCombo.setVisible(false);
		monthLabel.setVisible(false);
		quarterlyCombo.setVisible(false);
		quarterlyLabel.setVisible(false);
	}

	/**
	 * Carga las categorias comerciales (moda, hogar, deporte, niños, belleza)
	 */
	void dynCategory() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		categoryCombo.removeActionListener(this);
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE||'-'||NAME "
				+ " FROM XX_VMR_CATEGORY ";
		sql += " ORDER BY VALUE||'-'||NAME ";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED,
				MRole.SQL_RO);
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
		} finally {
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
	 * Carga los meses del año
	 */
	void dynMonth() {

		GregorianCalendar calendario = new GregorianCalendar();

		String[][] meses = {{String.valueOf(calendario.JANUARY),"Enero"},
			 	{String.valueOf(calendario.FEBRUARY),"Febrero"},
			 	{String.valueOf(calendario.MARCH),"Marzo"},
			 	{String.valueOf(calendario.APRIL),"Abril"},
			 	{String.valueOf(calendario.MAY),"Mayo"},			 	
			 	{String.valueOf(calendario.JUNE),"Junio"},
			 	{String.valueOf(calendario.JULY),"Julio"},
			 	{String.valueOf(calendario.AUGUST),"Agosto"},
			 	{String.valueOf(calendario.SEPTEMBER),"Septiembre"},
			 	{String.valueOf(calendario.OCTOBER),"Octubre"},
			 	{String.valueOf(calendario.NOVEMBER),"Noviembre"},
			 	{String.valueOf(calendario.DECEMBER),"Diciembre"}};

		monthCombo.addItem(new KeyNamePair(-1,null));
		for (int i = 0; i < meses.length; i++){
			monthCombo.addItem(new KeyNamePair(Integer.valueOf(meses[i][0]),meses[i][1]));
		}
	}
	
	/**
	 * Carga los trimestres del año
	 */
	void dynQuarterly() {

		String[][] trimestre = {{"0","Ene - Mar"},
			 					{"1","Abr - Jun"},
			 					{"2","Jul - Sep"},
			 					{"3","Oct - Dic"}};
		
		quarterlyCombo.addItem(new KeyNamePair(-1,null));
		for (int i = 0; i < trimestre.length; i++){
			quarterlyCombo.addItem(new KeyNamePair(Integer.valueOf(trimestre[i][0]),trimestre[i][1]));
		}
	}
	
	/**
	 * Carga los socio de negocios
	 */
	void dynPartner(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlPartner = "select C_BPartner_ID, name " +
							"from C_BPartner " +
							"order by name ";
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();

			partnerCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				partnerCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			partnerCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPartner, e);
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
	 * Carga las condiciones de pago
	 */
	void dynPaymentTerm(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlPartner = "select C_PaymentTerm_ID, name " +
							"from C_PaymentTerm " +
							"order by name ";
		try {
			pstmt = DB.prepareStatement(sqlPartner, null);
			rs = pstmt.executeQuery();

			paymentTermCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				paymentTermCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			paymentTermCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPartner, e);
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
	 * Llena los combos del filtro
	 */
	private  void llenarCombos(){
		dynCategory();
		dynPartner();
		dynPaymentTerm();
	}
	
	/**
	 * Genera las columnas para la primera tabla
	 */
	private void dynInitFirst(){
		llenarCombos();
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutF = new ColumnInfo[] {	
				/****0-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),
				/****1-Monto Local****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountLocal"), ".", float.class),				
				/****2-Monto Extranjero****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountForeign"), ".", float.class),
		};			
		xTableBefore.prepareTable(layoutF, "", "", true, "");
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		CompiereColor.setBackground (this);
		xTableBefore.getSelectionModel().addListSelectionListener(this);		
		xTableBefore.getColumnModel().getColumn(0).setMinWidth(283);
		xTableBefore.getColumnModel().getColumn(0).setCellRenderer(renderLeft);
		xTableBefore.getColumnModel().getColumn(1).setMinWidth(185);	
		xTableBefore.getColumnModel().getColumn(1).setCellRenderer(renderRight);
		xTableBefore.getColumnModel().getColumn(2).setMinWidth(185);	
		xTableBefore.getColumnModel().getColumn(2).setCellRenderer(renderRight);
		dynInitSecond();
	}
	
	/**
	 * Genera las columnas para la segunda tabla
	 */
	private void dynInitSecond(){
		DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
		ColumnInfo[] layoutF = new ColumnInfo[] {	
				/****0-Proveedor****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), ".", KeyNamePair.class),
				/****1-Monto Local****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountLocal"), ".", float.class),				
				/****2-Monto Extranjero****/
				new ColumnInfo(Msg.translate(Env.getCtx(), "XX_AmountForeign"), ".", float.class),
		};			
		xTableAfter.prepareTable(layoutF, "", "", true, "");
		renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
		renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
		CompiereColor.setBackground (this);
		xTableAfter.getSelectionModel().addListSelectionListener(this);		
		xTableAfter.getColumnModel().getColumn(0).setMinWidth(283);
		xTableAfter.getColumnModel().getColumn(0).setCellRenderer(renderLeft);
		xTableAfter.getColumnModel().getColumn(1).setMinWidth(185);	
		xTableAfter.getColumnModel().getColumn(1).setCellRenderer(renderRight);
		xTableAfter.getColumnModel().getColumn(2).setMinWidth(185);	
		xTableAfter.getColumnModel().getColumn(2).setCellRenderer(renderRight);
	}

	/**
	 * Se encarga de carga de información la primera tabla, a través de una consulta sql
	 */
	private void tableInitF(){
		//Calendar fecha = Calendar.getInstance();
		Calendar fechaTo = Calendar.getInstance();
		Calendar fechaFrom = Calendar.getInstance();
		Date dateTo;
		Date dateFrom;
		int mes = 0;
		int mesPrimero = 0;
		int mesSegundo = 0;
		BigDecimal montoLocal = new BigDecimal(0);
		BigDecimal montoExtranjero = new BigDecimal(0);
		
		m_sql = new StringBuffer();
		String m_groupBy = "";
		String m_sqlUnion = "";
		String m_groupByUnion = "";

		m_sql.append("select par.name, par.C_BPartner_ID, " +
			     	 "(case when cur.Iso_Code = 'VEB' then round((inv.GrandTotal - " +
			     	 "coalesce((select withholdingTaxInvoice(inv.C_Invoice_ID) from dual),0) - " +
			     	 "nvl((select sum(a.XX_RetainedAmount) from C_InvoiceLine a where inv.C_Invoice_ID = a.C_Invoice_ID),0) " +
			     	 "),2) else null end) as mLocal, " +
			     	 "(case when cur.Iso_Code <> 'VEB' then round(inv.GrandTotal,2) else null end) as mExtranjero " +
			     	 "from C_Invoice inv " +
			     	 "inner join C_Currency cur on (inv.C_Currency_ID = cur.C_Currency_ID) " +
			     	 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
			     	 "inner join C_BP_Status bps on (par.C_BP_Status_ID = bps.C_BP_Status_ID) " +
			     	 "left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
			     	 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
			     	 "where (inv.XX_AccountPayableStatus = 'A' or inv.XX_AccountPayableStatus = 'S') " +
			     	 "and inv.DocStatus = 'CO' " +
			     	 "and inv.isSoTrx = 'N' " +
			     	 "and bps.name = 'Activo' " +
			     	 "and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) " +
			     	 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') ");
		m_groupBy = "group by par.name, par.C_BPartner_ID, cur.Iso_Code, inv.GrandTotal, inv.C_Invoice_ID ";
		
		m_sqlUnion = "union " +
					 "select par.name, par.C_BPartner_ID, " +
					 "(case when cur.Iso_Code = 'VEB' then round(pay.PayAmt * (-1),2) else null end) as mLocal, " +
					 "(case when cur.Iso_Code <> 'VEB' then round(pay.PayAmt * (-1),2) else null end) as mExtranjero " +
					 "from C_Payment pay " +
					 "inner join C_BPartner par on (pay.C_BPartner_ID = par.C_BPartner_ID) " +
					 "inner join C_Currency cur on (cur.C_Currency_ID = pay.C_Currency_ID) " +
					 "inner join C_Order ord on (ord.C_Order_ID = pay.C_Order_ID) " +
					 "where pay.DocStatus = 'CO' " +
					 "and pay.XX_IsAdvance = 'Y' " +
					 "and pay.XX_AccountPayableStatus = 'A' " +
					 "and pay.C_Order_ID is not null ";
		m_groupByUnion = "group by par.name, par.C_BPartner_ID, cur.ISO_CODE, pay.PayAmt " +
						 "order by 1 asc";
		
		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Hace la busqueda por las fechas desde y hasta de los filtros****/		
		toPrimero = (Timestamp) dateToB.getValue();
		fromPrimero = (Timestamp) dateFromB.getValue();
		if (fromPrimero != null && toPrimero != null){
			m_sql.append("and inv.XX_DueDate ").append(" >= ").append(
					DB.TO_DATE(fromPrimero)).append(" ");
			m_sql.append("and inv.XX_DueDate ").append(" <= ").append(
					DB.TO_DATE(toPrimero)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(fromPrimero) + " ";
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(toPrimero) + " ";
		}else if (fromPrimero != null){
			m_sql.append("and inv.XX_DueDate ").append(" >= ").append(
					DB.TO_DATE(fromPrimero, true)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(fromPrimero) + " ";
		}else if (toPrimero != null){
			m_sql.append("and inv.XX_DueDate ").append(" <= ").append(
					DB.TO_DATE(toPrimero)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(toPrimero) + " ";
		}
		/****Buscar los saldos de las cuentas por pagar segun el mes, contra la fecha de facturación****/
		if ((monthCombo.getSelectedIndex() != 0)  && (monthCombo.getSelectedItem() != null)){
			mes = ((KeyNamePair)monthCombo.getSelectedItem()).getKey();
			fechaFrom.set(Calendar.DATE, 01);
			fechaFrom.set(Calendar.MONTH, (mes+1));
			dateFrom = fechaFrom.getTime();
			fromPrimero = new Timestamp(dateFrom.getTime());
			fechaTo.set(Calendar.DATE, util.ultimoDiaMes(mes+1));
			fechaTo.set(Calendar.MONTH, (mes+1));
			dateTo = fechaTo.getTime();
			toPrimero = new Timestamp(dateTo.getTime());
			m_sql.append("and inv.XX_DueDate ").append(" >= ").append(
					DB.TO_DATE(fromPrimero)).append(" ");
			m_sql.append("and inv.XX_DueDate ").append(" <= ").append(
					DB.TO_DATE(toPrimero)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(fromPrimero) + " ";
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(toPrimero) + " ";
			
		}
		/****Buscar los saldos de las cuentas por pagar segun el trimestre, contra la fecha de facturación****/
		if ((quarterlyCombo.getSelectedIndex() != 0)  && (quarterlyCombo.getSelectedItem() != null)){
			mes = ((KeyNamePair)quarterlyCombo.getSelectedItem()).getKey();
			if (mes == 0){
				mesPrimero = 0;
				mesSegundo = 2;
			}else if (mes == 1){
				mesPrimero = 3;
				mesSegundo = 5;
			}else if (mes == 2){
				mesPrimero = 6;
				mesSegundo = 8;
			}else if (mes == 3){
				mesPrimero = 9;
				mesSegundo = 11;
			}
			fechaFrom.set(Calendar.DATE, 01);
			fechaFrom.set(Calendar.MONTH, (mesPrimero+1));
			dateFrom = fechaFrom.getTime();
			fromPrimero = new Timestamp(dateFrom.getTime());
			fechaTo.set(Calendar.DATE, util.ultimoDiaMes(mesSegundo+1));
			fechaTo.set(Calendar.MONTH, (mesSegundo+1));
			dateTo = fechaTo.getTime();
			toPrimero = new Timestamp(dateTo.getTime());

			m_sql.append("and inv.XX_DueDate ").append(" >= ").append(
					DB.TO_DATE(fromPrimero)).append(" ");
			m_sql.append("and inv.XX_DueDate ").append(" <= ").append(
					DB.TO_DATE(toPrimero)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(fromPrimero) + " ";
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(toPrimero) + " ";
		}
		/****Buscar los saldos de las cuentas por pagar segun el proveedor seleccionado****/
		if ((partnerCombo.getSelectedIndex() != 0)  && (partnerCombo.getSelectedItem() != null)){
			m_sql.append("and ").append("par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
			m_sqlUnion = m_sqlUnion + "and par.name = '" + partnerCombo.getSelectedItem() + "' ";
		}
		/****Buscar los saldos de las cuentas por pagar segun la categoria comercial****/
		if ((categoryCombo.getSelectedIndex() != 0) && (categoryCombo.getSelectedItem() != null)){
			int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
			m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" ");		
		}
		/****Buscar los saldos de las cuentas por pagar segun la condición de pago****/
		if ((paymentTermCombo.getSelectedIndex() != 0) && (paymentTermCombo.getSelectedItem() != null)){
			int condicion = ((KeyNamePair)paymentTermCombo.getSelectedItem()).getKey();
			m_sql.append("and ").append("inv.C_PaymentTerm_ID = ").append(condicion).append(" ");
		}

		sqlPrimero = "select u.name, u.C_BPartner_ID, sum(u.mLocal), sum(u.mExtranjero) " +
					 "from (" + 
					 m_sql.toString() + m_groupBy + m_sqlUnion + m_groupByUnion + 
					 ") u " +
					 "group by u.name, u.C_BPartner_ID " +
					 "order by 1 asc";
		int i = 0;
		xTableBefore.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sqlPrimero, null);
			rs = pstmt.executeQuery();
			amountLocalB.setText(formato.format(new BigDecimal(0)).toString());
			amountLocalB.setFont(new Font("Serif", Font.BOLD,12));
			amountForeignB.setText(formato.format(new BigDecimal(0)).toString());
			amountForeignB.setFont(new Font("Serif", Font.BOLD,12));
			while(rs.next()) {	
				xTableBefore.setRowCount (i+1);	
				/****Proveedor****/
				xTableBefore.setValueAt((new KeyNamePair(rs.getInt(2),rs.getString(1))), i, 0);
				/****Monto Local****/
				if (rs.getBigDecimal(3) != null){
					montoLocal = montoLocal.add(rs.getBigDecimal(3)).setScale(2);
					xTableBefore.setValueAt(formato.format(rs.getBigDecimal(3).setScale(2)), i, 1);
					amountLocalB.setText(formato.format(montoLocal).toString());
					amountLocalB.setFont(new Font("Serif", Font.BOLD,12));
				}else
					xTableBefore.setValueAt(formato.format(new BigDecimal(0).setScale(2)), i, 1);
				/****Monto Extranjero****/
				if (rs.getBigDecimal(4) != null){
					montoExtranjero = montoExtranjero.add(rs.getBigDecimal(4)).setScale(2);
					xTableBefore.setValueAt(formato.format(rs.getBigDecimal(4).setScale(2)), i, 2);	
					amountForeignB.setText(formato.format(montoExtranjero).toString());
					amountForeignB.setFont(new Font("Serif", Font.BOLD,12));
				}else
					xTableBefore.setValueAt(formato.format(new BigDecimal(0).setScale(2)), i, 2);	
				i++;				
			}	
		}
		catch(SQLException e){	
			e.getMessage();
		} 
		finally{
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
	 * Se encarga de carga de información la segunda tabla, a través de una consulta sql
	 */
	private void tableInitS(){
		BigDecimal montoLocal = new BigDecimal(0);
		BigDecimal montoExtranjero = new BigDecimal(0);
		
		m_sql = new StringBuffer();
		String m_groupBy = "";
		String m_sqlUnion = "";
		String m_groupByUnion = "";

		m_sql.append("select par.name, par.C_BPartner_ID, " +
			     	 "(case when cur.Iso_Code = 'VEB' then (inv.GrandTotal - " +
			     	 "coalesce((select withholdingTaxInvoice(inv.C_Invoice_ID) from dual),0) - " +
			     	 "nvl((select sum(a.XX_RetainedAmount) from C_InvoiceLine a where inv.C_Invoice_ID = a.C_Invoice_ID),0) " +
			     	 ") else null end) as mLocal, " +
			     	 "(case when cur.Iso_Code <> 'VEB' then inv.GrandTotal else null end) as mExtranjero " +
			     	 "from C_Invoice inv " +
			     	 "inner join C_Currency cur on (inv.C_Currency_ID = cur.C_Currency_ID) " +
			     	 "inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
			     	 "inner join C_BP_Status bps on (par.C_BP_Status_ID = bps.C_BP_Status_ID) " +
			     	 "left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
			     	 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
			     	 "left outer join primero u on (par.C_BPartner_ID = u.C_BPartner_ID) " +
			     	 "where (inv.XX_AccountPayableStatus = 'A' or inv.XX_AccountPayableStatus = 'S') " +
			     	 "and inv.DocStatus = 'CO' " +
			     	 "and inv.isSoTrx = 'N' " +
			     	 "and bps.name = 'Activo' " +
			     	 "and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) " +
			     	 "and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') ");
		m_groupBy = "group by par.name, par.C_BPartner_ID, cur.Iso_Code, inv.GrandTotal, inv.C_Invoice_ID ";
		
		m_sqlUnion = "union " +
					 "select par.name, par.C_BPartner_ID, " +
					 "(case when cur.Iso_Code = 'VEB' then pay.PayAmt * (-1) else null end) as mLocal, " +
					 "(case when cur.Iso_Code <> 'VEB' then pay.PayAmt * (-1) else null end) as mExtranjero " +
					 "from C_Payment pay " +
					 "inner join C_BPartner par on (pay.C_BPartner_ID = par.C_BPartner_ID) " +
					 "inner join C_Currency cur on (cur.C_Currency_ID = pay.C_Currency_ID) " +
					 "inner join C_Order ord on (ord.C_Order_ID = pay.C_Order_ID) " +
					 "where pay.DocStatus = 'CO' " +
					 "and pay.XX_IsAdvance = 'Y' " +
					 "and pay.XX_AccountPayableStatus = 'A' " +
					 "and pay.C_Order_ID is not null ";
		m_groupByUnion = "group by par.name, par.C_BPartner_ID, cur.ISO_CODE, pay.PayAmt " +
						 "order by 1 asc";

		/**
		 * Se verifican las condiciones de los filtros para realizar 
		 * una busqueda mas detallada
		 */
		/****Hace la busqueda por las fechas desde y hasta de los filtros****/	
		toSegundo = (Timestamp) dateToA.getValue();
		fromSegundo = (Timestamp) dateFromA.getValue();
		if (fromSegundo != null && toSegundo != null){
			m_sql.append("and inv.XX_DueDate ").append(" >= ").append(
					DB.TO_DATE(fromSegundo)).append(" ");
			m_sql.append("and inv.XX_DueDate ").append(" <= ").append(
					DB.TO_DATE(toSegundo)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(fromSegundo) + " ";
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(toSegundo) + " ";
		}else if (fromSegundo != null){
			m_sql.append("and inv.XX_DueDate ").append(" >= ").append(
					DB.TO_DATE(fromSegundo, true)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx >= " + DB.TO_DATE(fromSegundo) + " ";
		}else if (toSegundo != null){
			m_sql.append("and inv.XX_DueDate ").append(" <= ").append(
					DB.TO_DATE(toSegundo)).append(" ");
			m_sqlUnion = m_sqlUnion + "and pay.DateTrx <= " + DB.TO_DATE(toSegundo) + " ";
		}
		/****Buscar los saldos de las cuentas por pagar segun el proveedor seleccionado****/
		if ((partnerCombo.getSelectedIndex() != 0)  && (partnerCombo.getSelectedItem() != null)){
			m_sql.append("and ").append("par.name = '").append(partnerCombo.getSelectedItem()).append("' ");
			m_sqlUnion = m_sqlUnion + "and par.name = '" + partnerCombo.getSelectedItem() + "' ";
		}
		/****Buscar los saldos de las cuentas por pagar segun la categoria comercial****/
		if ((categoryCombo.getSelectedIndex() != 0) && (categoryCombo.getSelectedItem() != null)){
			int categoria = ((KeyNamePair)categoryCombo.getSelectedItem()).getKey();
			m_sql.append("and ").append("dep.XX_VMR_Category_ID = ").append(categoria).append(" ");		
		}
		/****Buscar los saldos de las cuentas por pagar segun la condición de pago****/
		if ((paymentTermCombo.getSelectedIndex() != 0) && (paymentTermCombo.getSelectedItem() != null)){
			int condicion = ((KeyNamePair)paymentTermCombo.getSelectedItem()).getKey();
			m_sql.append("and ").append("inv.C_PaymentTerm_ID = ").append(condicion).append(" ");
		}

		sqlSegundo = "select v.name, v.C_BPartner_ID, sum(v.mLocal), sum(v.mExtranjero) " +
		 			 "from (" +  
		 			 "with " +
					 "primero as ( " +
					 sqlPrimero + 
					 ") " + m_sql.toString() + m_groupBy + m_sqlUnion + m_groupByUnion + 
					 ") v " +
					 "group by v.name, v.C_BPartner_ID " +
					 "order by 1 asc";

		int i = 0;
		xTableAfter.setRowCount(i);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sqlSegundo, null);
			rs = pstmt.executeQuery();
			amountLocalA.setText(formato.format(new BigDecimal(0)));
			amountLocalA.setFont(new Font("Serif", Font.BOLD,12));
			amountForeignA.setText(formato.format(new BigDecimal(0)));
			amountForeignA.setFont(new Font("Serif", Font.BOLD,12));
			while(rs.next()) {	
				xTableAfter.setRowCount (i+1);	
				/****Proveedor****/
				xTableAfter.setValueAt((new KeyNamePair(rs.getInt(2),rs.getString(1))), i, 0);
				/****Monto Local****/
				if (rs.getBigDecimal(3) != null){
					montoLocal = montoLocal.add(rs.getBigDecimal(3)).setScale(2, RoundingMode.DOWN);
					xTableAfter.setValueAt(formato.format(rs.getBigDecimal(3).setScale(2, RoundingMode.DOWN)), i, 1);
					amountLocalA.setText(formato.format(montoLocal));
					amountLocalA.setFont(new Font("Serif", Font.BOLD,12));
				}else
					xTableAfter.setValueAt(formato.format(new BigDecimal(0).setScale(2, RoundingMode.DOWN)), i, 1);
				/****Monto Extranjero****/
				if (rs.getBigDecimal(4) != null){
					montoExtranjero = montoExtranjero.add(rs.getBigDecimal(4)).setScale(2, RoundingMode.DOWN);
					xTableAfter.setValueAt(formato.format(rs.getBigDecimal(4).setScale(2, RoundingMode.DOWN)), i, 2);	
					amountForeignA.setText(formato.format(montoExtranjero));
					amountForeignA.setFont(new Font("Serif", Font.BOLD,12));
				}else
					xTableAfter.setValueAt(formato.format(new BigDecimal(0).setScale(2, RoundingMode.DOWN)), i, 2);
				i++;				
			}	
		}
		catch(SQLException e){	
			e.getMessage();
		} 
		finally{
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
	 * Busca los parámetros de entrada para generar el reporte
	 */
	public void reportBalanceCxp(){
		Calendar fechaPrimero = Calendar.getInstance();
		Calendar fechaSegundo = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int mes = 0;
		int mesPrimero = 0;
		int mesSegundo = 0;
		String fechaDActual = "";
		String fechaHActual = "";
		String fechaDComparativa = "";
		String fechaHComparativa = "";
		Timestamp toB = null;
		Timestamp fromB = null; 
		Timestamp toA = null;
		Timestamp fromA = null;

		if ((monthCombo.getSelectedIndex() != 0)  && (monthCombo.getSelectedItem() != null)){
			mes = ((KeyNamePair)monthCombo.getSelectedItem()).getKey();
			fechaPrimero.set(Calendar.DATE, 1);
			fechaPrimero.set(Calendar.MONTH, mes+1);
			fechaPrimero.set(Calendar.YEAR, fechaPrimero.get(Calendar.YEAR));
			fechaSegundo.set(Calendar.DATE, util.ultimoDiaMes(mes+1));
			fechaSegundo.set(Calendar.MONTH, mes+1);
			fechaSegundo.set(Calendar.YEAR, fechaSegundo.get(Calendar.YEAR));
			fechaDActual = sdf.format(fechaPrimero);
			fechaHActual = sdf.format(fechaSegundo);
		}else if ((quarterlyCombo.getSelectedIndex() != 0)  && (quarterlyCombo.getSelectedItem() != null)){
				mes = ((KeyNamePair)quarterlyCombo.getSelectedItem()).getKey();
				if (mes == 0){
					mesPrimero = 0;
					mesSegundo = 2;
				}else if (mes == 1){
					mesPrimero = 3;
					mesSegundo = 5;
				}else if (mes == 2){
					mesPrimero = 6;
					mesSegundo = 8;
				}else if (mes == 3){
					mesPrimero = 9;
					mesSegundo = 11;
				}
				fechaPrimero.set(Calendar.DATE, 1);
				fechaPrimero.set(Calendar.MONTH, mesPrimero+1);
				fechaPrimero.set(Calendar.YEAR, fechaPrimero.get(Calendar.YEAR));
				fechaSegundo.set(Calendar.DATE, util.ultimoDiaMes(mesSegundo+1));
				fechaSegundo.set(Calendar.MONTH, mesSegundo+1);
				fechaSegundo.set(Calendar.YEAR, fechaSegundo.get(Calendar.YEAR));
				fechaDActual = sdf.format(fechaPrimero);
				fechaHActual = sdf.format(fechaSegundo);
		}else{
			toB = (Timestamp) dateToB.getValue();
			fromB = (Timestamp) dateFromB.getValue();
			fechaDActual = sdf.format(new Date(fromB.getTime()));
			fechaHActual = sdf.format(new Date(toB.getTime()));
		}
		
		toA = (Timestamp) dateToA.getValue();
		fromA = (Timestamp) dateFromA.getValue();
		fechaDComparativa = sdf.format(new Date(fromA.getTime()));
		fechaHComparativa = sdf.format(new Date(toA.getTime()));
		
		/****Reporte****/
		generateReport(fechaDComparativa,fechaHComparativa,fechaDActual,fechaHActual);
	}
	
	/**
	 * Genera el reporte de la evolución de la cuentas por pagar
	 * @param fechaDComparativa fecha desde comparativo
	 * @param fechaHComparativa fecha hasta comparativo
	 * @param fechaDActual fecha desde actual
	 * @param fechaHActual fecha hasta actual
	 */
	public void generateReport(String fechaDComparativa, String fechaHComparativa, 
			String fechaDActual, String fechaHActual){
		
		String designName = "BalanceCxP"; 
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("fechaDAnterior");
		myReport.parameterValue.add(fechaDComparativa);
		myReport.parameterName.add("fechaHAnterior");
		myReport.parameterValue.add(fechaHComparativa);
		myReport.parameterName.add("fechaDActual");
		myReport.parameterValue.add(fechaDActual);
		myReport.parameterName.add("fechaHActual");
		myReport.parameterValue.add(fechaHActual);
		
		//Correr Reporte
		myReport.runReport(designName, "xls");
	}
	
	/**
	 * Busca la información a través de los filtros
	 * @param variable indica a cual tabla pertenece el boton
	 */
	private void cmdSearch(String variable){	
		if (variable.equals("Before"))
			tableInitF();
		else{
			tableInitS();
			if ((contA == 1) && (contB == 1))
				bReport.setEnabled(true);
		}
	}

	/**
	 * Se encarga de mostrar los campos del rango: fecha desde y fecha hasta
	 */
	private void cmdPeriodo(){
		dateFromlabelB.setVisible(true);
		dateFromB.setVisible(true);
		dateToLabelB.setVisible(true);
		dateToB.setVisible(true);
		monthLabel.setVisible(false);
		monthCombo.setVisible(false);
		quarterlyLabel.setVisible(false);
		quarterlyCombo.setVisible(false);
	}

	/**
	 * Se encarga de mostrar el campo de mes
	 */
	private void cmdMes(){
		monthLabel.setVisible(true);
		monthCombo.setVisible(true);
		monthCombo.removeAllItems();
		dynMonth();
		quarterlyCombo.setVisible(false);
		quarterlyLabel.setVisible(false);
		dateFromlabelB.setVisible(false);
		dateFromB.setVisible(false);
		dateToLabelB.setVisible(false);
		dateToB.setVisible(false);
	}
	
	/**
	 * Se encaraga de mostrar el campo trimestral
	 */
	private void cmdTrimestral(){
		quarterlyCombo.setVisible(true);
		quarterlyLabel.setVisible(true);
		quarterlyCombo.removeAllItems();
		dynQuarterly();
		monthLabel.setVisible(false);
		monthCombo.setVisible(false);
		dateFromlabelB.setVisible(false);
		dateFromB.setVisible(false);
		dateToLabelB.setVisible(false);
		dateToB.setVisible(false);		
	}
	
	/**
	 * Busca la información previo a la generación del reporte
	 */
	private void cmdReport(){
		reportBalanceCxp();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == bSearchB){
			contB = 1;
			cmdSearch("Before");
		}else if (e.getSource() == bSearchA){
			contA = 1;
			cmdSearch("After");
		}else if (e.getSource() == periodoRButton){
			cmdPeriodo();			
		}else if (e.getSource() == mesRButton){
			cmdMes();
		}else if (e.getSource() == trimestreRButton){
			cmdTrimestral();
		}else if (e.getSource() == bReport){
			cmdReport();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

	}

}
