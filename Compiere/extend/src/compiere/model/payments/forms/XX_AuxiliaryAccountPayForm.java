package compiere.model.payments.forms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.Container;
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
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.common.constants.EnvConstants;
import org.compiere.grid.ed.VDate;
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

import java.text.SimpleDateFormat;

import compiere.model.birt.BIRTReport;
import compiere.model.cds.Utilities;
import compiere.model.payments.X_XX_VCN_BalanceByPartner;


/**
 * Forma en compiere: Auxiliary Account Payable
 * 
 * Hace el cierre de las cuentas por pagar, modificando el saldo por socio de negocio, 
 * y el mes del cierre, y generando los reportes definitivos detallado y consolidado. 
 * Y por otra parte genera los reportes preliminares 
 * detallado y consolidado (bienes-servicios/nacional e importado y mercancia/nacional e importada)
 * 
 * @author Jessica Mendoza
 *
 */
public class XX_AuxiliaryAccountPayForm extends CPanel
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
	Ctx ctx=Env.getCtx();
	
	/****Paneles****/
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
	private CPanel northPanel = new CPanel();
	private GridBagLayout northLayout = new GridBagLayout();
	private CPanel centerPanel = new CPanel();
	private GridBagLayout centerLayout = new GridBagLayout();

	/****Botones****/
	private CButton bGenerate = new CButton();
	
	/****CheckBox****/
	private JCheckBox closeAccount = new JCheckBox();
	
	/****Labels-ComboBox-Fecha****/
	private CLabel dateFromlabel = new CLabel();
	private CLabel dateToLabel = new CLabel();
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo")); 
	private CLabel categoryPartnerLabel = new CLabel();
	private CComboBox categoryPartnerCombo = new CComboBox();
	private CLabel countryLabel = new CLabel();
	private CComboBox countryCombo = new CComboBox();
	private CLabel partnerLabel = new CLabel();
	private CComboBox partnerCombo = new CComboBox();

	/****Variables****/
	float cuenta = 0;
	String cuentaS;
	static Integer sync = new Integer(0);
	Utilities util = new Utilities();
	String tipoFactura;
	String paisName;
	String socioNegocioName;
	int paisId;
	int socioNegocioId;

	@Override
	/**
	 * Inicializa la forma
	 */
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);
		try{
			jbInit();
			dynInitNorth();
			frame.getContentPane().add(mainPanel, BorderLayout.NORTH);	
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
		dateFromlabel.setText(Msg.translate(Env.getCtx(), "XX_DateFromPay"));
		dateToLabel.setText(Msg.translate(Env.getCtx(), "XX_DateToPay"));
		categoryPartnerLabel.setText(Msg.getMsg(Env.getCtx(), "XX_CategoryPartner"));
		countryLabel.setText(Msg.getMsg(Env.getCtx(), "Country"));
		closeAccount.setText(Msg.getMsg(Env.getCtx(), "XX_CloseAccountPayable"));
		closeAccount.setSelected(false);
		closeAccount.addActionListener(this);
		partnerLabel.setText(Msg.getMsg(Env.getCtx(), "BPartner"));
		bGenerate.setText(Msg.translate(Env.getCtx(), "Generate"));
		bGenerate.setEnabled(true);	
		bGenerate.addActionListener(this);

		mainPanel.setLayout(mainLayout);
		northPanel.setLayout(northLayout);
		northPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				Msg.translate(Env.getCtx(), "XX_CloseAccountPayable")));
		xPanel.setLayout(xLayout);
		
		centerPanel.setLayout(centerLayout);
		xPanel.setLayout(xLayout);
		
		mainPanel.add(northPanel,  BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		/**
		 * Panel Superior
		 */		
		/****Etiquetas de la primera fila****/
		northPanel.add(dateFromlabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(12, 12, 5, 5), 0, 0));
		northPanel.add(dateFrom, new GridBagConstraints(1, 0, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(12, 5, 5, 0), 0, 0));
		northPanel.add(dateTo, new GridBagConstraints(2, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, 
				new Insets(12, 5, 5, 0), 0, 0));
		northPanel.add(dateToLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(12, 0, 5, 5), 0, 0));
		

		/****Etiquetas de la segunda fila****/
		northPanel.add(partnerLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 5, 5, 5), 0, 0));
		northPanel.add(partnerCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 	
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 5, 0), 0, 0)); 
		northPanel.add(categoryPartnerCombo, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 0), 0, 0));
		northPanel.add(categoryPartnerLabel, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 12, 5, 5), 0, 0));
		
	
		/****Etiquetas de la tercera fila****/
		northPanel.add(countryLabel, new GridBagConstraints(0, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 0));
		northPanel.add(countryCombo, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 5, 0), 0, 0));

		/****Etiquetas de la cuarta fila****/
		centerPanel.add(closeAccount, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 5, 5, 5), 0, 20));
		centerPanel.add(bGenerate, new GridBagConstraints(4, 0, 1, 1, 0.0, 
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 12, 5, 12), 0, 0));

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
	 * Carga los paises
	 */
	void dynCountry(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlCountry = "select C_Country_ID, name " + 
							"from C_Country " +
							"order by name ";
		try {
			pstmt = DB.prepareStatement(sqlCountry, null);
			rs = pstmt.executeQuery();

			countryCombo.addItem(new KeyNamePair(-1, null));
			while (rs.next()) {
				countryCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			countryCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlCountry, e);
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
	 * Carga los tipos de facturas (bienes, servicios, productos para la venta)
	 */
	void dynCategoryPartner(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlCategoria = "select AD_Ref_List_ID, NAME " +
		 					  "from AD_Ref_List_Trl " +
		 					  "where AD_Ref_List_ID IN ("+ Env.getCtx().getContextAsInt("#XX_L_ASSETSMPRODUCT_ID") +
		 					  "," + Env.getCtx().getContextAsInt("#XX_L_ITEMMPRODUCT_ID") +
		 					  "," + Env.getCtx().getContextAsInt("#XX_L_SERVICESMPRODUCT_ID") +
		 					  ") and AD_Language= '"+ Env.getCtx().getContext("#AD_Language") + "'";
		try {
			pstmt = DB.prepareStatement(sqlCategoria, null);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				categoryPartnerCombo.addItem(new KeyNamePair(rs.getInt(1), rs
						.getString(2)));
			}
			
			categoryPartnerCombo.addActionListener(this);
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlCategoria, e);
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
		
		/****Captura el evento para realizar otra accion****/
		categoryPartnerCombo.getCompleteComboBoxModel().addListDataListener(new ListDataListener() {

			@Override
			public void contentsChanged(ListDataEvent e) {
				if(categoryPartnerCombo.getSelectedIndex()>0){
					String tipo = "";
					int catP = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
					if (catP == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID")))
						tipo = "S";
					else if (catP == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID")))
						tipo = "A";
					else
						tipo = "I";
					dynDateTo(tipo);					
				}			
			}

			@Override
			public void intervalAdded(ListDataEvent e) {
							
			}

			@Override
			public void intervalRemoved(ListDataEvent e) {
								
			}
		});	
	}
	
	/**
	 * Carga la fecha correspondiente a la tabla de cierre de cuentas por pagar
	 */
	void dynDateTo(String tipo){
		String sqlFecha = "select XX_Month, XX_Year " +
						  "from XX_VCN_CloseAccountPayable " +
						  "where XX_InvoiceType = '" + tipo + "' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Calendar calendarFrom = Calendar.getInstance();
		Calendar calendarTo = Calendar.getInstance();
		try {
			pstmt = DB.prepareStatement(sqlFecha, null);
			rs = pstmt.executeQuery();		
			while (rs.next()) {
				
				calendarFrom.set(Calendar.MONTH, rs.getInt(1) - 1);
				calendarFrom.set(Calendar.DAY_OF_MONTH, 01);
				calendarFrom.set(Calendar.YEAR, rs.getInt(2));
				Timestamp fechaFrom = new Timestamp(calendarFrom.getTimeInMillis());
				dateFrom.setValue(fechaFrom);
				
				calendarTo.set(Calendar.MONTH, rs.getInt(1) - 1);
				calendarTo.set(Calendar.DATE, util.ultimoDiaMes(rs.getInt(1)));
				calendarTo.set(Calendar.YEAR, rs.getInt(2));
				Timestamp fechaTo = new Timestamp(calendarTo.getTimeInMillis());
				dateTo.setValue(fechaTo);
				
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlFecha, e);
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
	 * Se encarga de llamar al metodo que llena los filtros
	 */
	private void dynInitNorth(){
		llenarCombos();
	}
	
	/**
	 *  Ejecuta el metodo correspondiente a la accion
	 *  @param e La acccion del evento
	 */
	public void actionPerformed (ActionEvent e){			
		if (e.getSource() == bGenerate){
			cmdGenerate();
		}
	}
	
	/**
	 *  Busca la informacion a través de los filtros
	 */
	private void cmdGenerate(){	
		Timestamp to = (Timestamp) dateTo.getValue();
		Timestamp from = (Timestamp) dateFrom.getValue();
		String monthToS = "", monthFromS = "", dayTo = "", dayFrom = "";
		
		GregorianCalendar calendarTo = new GregorianCalendar();
		calendarTo.setTime(to);
		int monthTo = calendarTo.get(Calendar.MONTH) + 1;
		int yearTo = calendarTo.get(Calendar.YEAR);

		GregorianCalendar calendarFrom = new GregorianCalendar();
		calendarFrom.setTime(from);
		int monthFrom = calendarFrom.get(Calendar.MONTH) + 1;
		int yearFrom = calendarFrom.get(Calendar.YEAR);
		
		if (String.valueOf(monthTo).length() == 1)
			monthToS = "0" + (monthTo);
		else
			monthToS = String.valueOf(monthTo);
		if (String.valueOf(monthFrom).length() == 1)
			monthFromS = "0" + (monthFrom);
		else
			monthFromS = String.valueOf(monthFrom);
		if (String.valueOf(calendarFrom.get(Calendar.DATE)).length() == 1)
			dayFrom = "0" + calendarFrom.get(Calendar.DATE);
		else
			dayFrom = String.valueOf(calendarFrom.get(Calendar.DATE));
		if (String.valueOf(calendarTo.get(Calendar.DATE)).length() == 1)
			dayTo = "0" + calendarTo.get(Calendar.DATE);
		else
			dayTo = String.valueOf(calendarTo.get(Calendar.DATE));
			
		String fFrom = dayFrom+"/"+monthFromS+"/"+calendarFrom.get(Calendar.YEAR);
		String fTo = dayTo+"/"+monthToS+"/"+calendarTo.get(Calendar.YEAR);
		
		tipoFactura = "";
		int tipoFID = ((KeyNamePair)categoryPartnerCombo.getSelectedItem()).getKey();
		paisName = ((KeyNamePair)countryCombo.getSelectedItem()).getName();
		socioNegocioName = ((KeyNamePair)partnerCombo.getSelectedItem()).getName();
		paisId = ((KeyNamePair)countryCombo.getSelectedItem()).getKey();
		socioNegocioId = ((KeyNamePair)partnerCombo.getSelectedItem()).getKey();
		
		if (tipoFID == Integer.parseInt(Env.getCtx().getContext("#XX_L_SERVICESMPRODUCT_ID")))
			tipoFactura = "S";
		else if (tipoFID == Integer.parseInt(Env.getCtx().getContext("#XX_L_ASSETSMPRODUCT_ID")))
			tipoFactura = "A";
		else
			tipoFactura = "I";
		
		//PRUEBA
		//newAmountBalanceNational(6,to,from);
		//newAmountBalanceImport(6,to,from);
		
		/****Generar el Cierre de las Cuentas por Pagar****/		
		if ((monthTo == monthFrom) && (yearTo == yearFrom)){
			//Se esta realizando el cierre de las cuentas por pagar
			if (closeAccount.isSelected()){
				int mes = monthCloseActual();
				//Valida si se está haciendo el cierre en el mes correspondiente
				if((monthTo > mes) || (monthTo < mes) || (monthFrom > mes) || (monthFrom < mes)){
					ADialog.error(EnvConstants.WINDOW_INFO, new Container(), 
							Msg.getMsg(Env.getCtx(), "XX_DateCloseError"));
					dynDateTo(tipoFactura);			
				}else{
					//Valida que el usuario no haya seleccionado algún proveedor o pais					
					if ((socioNegocioName != "") | (paisName != "")){
						ADialog.error(EnvConstants.WINDOW_INFO, new Container(), 
								Msg.getMsg(Env.getCtx(), "XX_NotPartnerCountryCloseCxP"));
					}else{
						newAmountBalanceNational(mes,to,from);
						//newAmountBalanceImport(mes,to,from);
						updateMonthClose(mes,yearTo);
						/****Generar el Reporte Definitivo: Detallado y Consolidado****/
						if (tipoFactura.equals("I")){
							closeConsolidated(fTo,fFrom,monthTo-1,"pdf","IN");
							//closeConsolidated(fTo,fFrom,monthTo-1,"pdf","II");
							closeDetail(fTo,fFrom,monthTo-1,"pdf","IN");
							//closeDetail(fTo,fFrom,monthTo-1,"pdf","II");
						}else if (tipoFactura.equals("A")){
							closeConsolidated(fTo,fFrom,monthTo-1,"pdf","AN");
							//closeConsolidated(fTo,fFrom,monthTo-1,"pdf","AI");
							closeDetail(fTo,fFrom,monthTo-1,"pdf","AN");
							//closeDetail(fTo,fFrom,monthTo-1,"pdf","AI");
						}else{
							closeConsolidated(fTo,fFrom,monthTo-1,"pdf","SN");
							//closeConsolidated(fTo,fFrom,monthTo-1,"pdf","SI");
							closeDetail(fTo,fFrom,monthTo-1,"pdf","SN");
							//closeDetail(fTo,fFrom,monthTo-1,"pdf","SI");
						}
					}
				}
			}else{ //Solo se generan los reportes
				/****Generar el Reporte Preliminar: Detallado y Consolidado****/
				if (tipoFactura.equals("I")){
					closeConsolidated(fTo,fFrom,monthTo-1,"xls","IN");
					//closeConsolidated(fTo,fFrom,monthTo-1,"xls","II");
					closeDetail(fTo,fFrom,monthTo-1,"xls","IN");
					//closeDetail(fTo,fFrom,monthTo-1,"xls","II");
				}else if (tipoFactura.equals("A")){
					closeConsolidated(fTo,fFrom,monthTo-1,"xls","AN");
					//closeConsolidated(fTo,fFrom,monthTo-1,"xls","AI");
					closeDetail(fTo,fFrom,monthTo-1,"xls","AN");
					//closeDetail(fTo,fFrom,monthTo-1,"xls","AI");
				}else{
					closeConsolidated(fTo,fFrom,monthTo-1,"xls","SN");
					//closeConsolidated(fTo,fFrom,monthTo-1,"xls","SI");
					closeDetail(fTo,fFrom,monthTo-1,"xls","SN");
					//closeDetail(fTo,fFrom,monthTo-1,"xls","SI");
				}
			}
		}else{
			if (monthTo != monthFrom){
				ADialog.error(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(Env.getCtx(), "XX_DateMonthError"));
				dynDateTo(tipoFactura);
			}else{ 
				if (yearTo != yearFrom){
					ADialog.error(EnvConstants.WINDOW_INFO, new Container(), Msg.getMsg(Env.getCtx(), "XX_DateYearError"));
					dynDateTo(tipoFactura);
				}
			}
		}	
	}

	/**
	 * Se encarga de generar el reporte del Cierre de las cuentas por pagar Preliminar
	 * @param to fecha hasta
	 * @param from fecha desde
	 * @param formato formato de salida del reporte
	 */
	public void closeConsolidated(String to, String from, int mes, String formato, String tipo){
		
		if (socioNegocioId < 0)
			socioNegocioId = 0;
		if (paisId < 0)
			paisId = 0;

		String designName = "CloseConsolidated" + tipo; 

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		//Agregar parametro
		myReport.parameterName.add("fechaDesde");
		myReport.parameterValue.add(from);
		myReport.parameterName.add("fechaHasta");
		myReport.parameterValue.add(to);
		myReport.parameterName.add("mes");
		myReport.parameterValue.add(mes);
		myReport.parameterName.add("proveedor");
		myReport.parameterValue.add(socioNegocioId);
		myReport.parameterName.add("pais");
		myReport.parameterValue.add(paisId);
		
		//Correr Reporte
		myReport.runReport(designName, formato);	
	}
	
	/**
	 * Se encarga de generar el reporte del Cierre de las cuenats por pagar Detallado
	 * @param to fecha hasta
	 * @param from fecha desde
	 * @param mes
	 * @param formato formato de salida del reporte
	 */
	public void closeDetail(String to, String from, int mes, String formato, String tipo){
		
		if (socioNegocioId < 0)
			socioNegocioId = 0;
		if (paisId < 0)
			paisId = 0;
		
		String designName = "CloseDetail" + tipo; 

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("mesX");
		myReport.parameterValue.add(mes);
		myReport.parameterName.add("fechaDesde");
		myReport.parameterValue.add(from);
		myReport.parameterName.add("fechaHasta");
		myReport.parameterValue.add(to);
		myReport.parameterName.add("proveedor");
		myReport.parameterValue.add(socioNegocioId);
		myReport.parameterName.add("pais");
		myReport.parameterValue.add(paisId);
		
		//Correr Reporte
		myReport.runReport(designName, formato);	
	}
	
	/**
	 * Busca el tipo de orden de compra que tiene el socio de negocio
	 * @return
	 */
	public String tipoPartner(String namePartner){
		String tipo = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String sql = "select ord.XX_POType " +
		 			 "from C_Order ord, C_BPartner par " +
		 			 "where ord.C_BPartner_ID = par.C_BPartner_ID " +
		 			 "and par.name = '" + namePartner + "' ";
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				tipo = rs.getString("XX_POType");
			}
			
		}catch (Exception e){
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
		return tipo;
	}
	
	/**
	 * Busca el mes en que se deberia hacer el cierre de las cuentas por pagar
	 * @return el mes
	 */
	public int monthCloseActual(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlFecha = "select XX_Month " +
		  				  "from XX_VCN_CloseAccountPayable " +
		  				  "where XX_InvoiceType = '" + tipoFactura + "' ";
		int mes = 0;
		try {
			pstmt = DB.prepareStatement(sqlFecha, null);
			rs = pstmt.executeQuery();		
			if (rs.next()) {
				mes = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlFecha, e);
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
		return mes;
	}
	
	/**
	 * Actualiza el mes y año del Cierre de las cuentas por pagar
	 * @param mes mes actual
	 * @param año año actual
	 */
	public void updateMonthClose(int mes, int año){		
		if (mes == 12){
			mes = 01;
			año = año + 1;
		}else{
			mes = mes + 1;
		}

		String sqlUpdate = "update XX_VCN_CloseAccountPayable " +
						   "set XX_Month = " + mes + " , XX_Year = " + año + " " +
						   "where XX_InvoiceType = '" + tipoFactura + "' ";
		DB.executeUpdate(null, sqlUpdate);
	}
	
	/**
	 * Actualiza el saldo de los proveedores nacionales en la tabla de Saldos 'XX_VCN_BalanceByPartner'
	 * @param mes mes actual
	 */
	public void newAmountBalanceNational(int mes, Timestamp to, Timestamp from){
		/****Buscar el saldo actual, calcular el nuevo saldo y actualizarlo****/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fHasta = sdf.format(new Date(to.getTime()));
		String fDesde = sdf.format(new Date(from.getTime()));		
		String sqlP;
		String sqlF;
		String where;
		String groupBy;
		String orderBy;
		String sqlUnion;
		String groupByUnion;

		sqlP = "select u.C_BPartner_ID, u.C_Currency_ID, sum(u.totalMo-b) " +
			   "from ( ";
		
		String sqlAmount = "with " +
						   "saldo as " +
						   "(select XX_AmountMonthNational as amountMn, C_BPartner_ID as partner " +
						   "from XX_VCN_BalanceByPartner " +
						   "where XX_Month = " + (mes-1) ;
		if (tipoFactura.equals("I")){
			   sqlAmount = sqlAmount + " and XX_INVOICETYPE = 'I' " + ") ";  
			}else if (tipoFactura.equals("A")){
				sqlAmount = sqlAmount + " and XX_INVOICETYPE = 'A' " + ") ";   
			}else if (tipoFactura.equals("S")){
				sqlAmount = sqlAmount + " and XX_INVOICETYPE = 'S' " + ") ";  
			}

		if (!tipoFactura.equals("I")){
			String sqlPartner =  "select par.C_BPartner_ID, cur.C_Currency_ID, " +
								"sum((inv.GrandTotal - (coalesce((select WithholdingTaxInvoice (inv.C_Invoice_ID) " +
								"from dual),0)) - (nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a " +
								"where inv.C_Invoice_ID = a.C_Invoice_ID),0)))+ (coalesce(sal.amountMn,0))) as totalMo, sum(0) b  " +	
								"from C_Invoice inv " +
								"inner join C_BPartner par on (par.C_BPartner_ID = inv.C_BPartner_ID) " +
								"inner join C_DocType doc on (doc.C_DocType_ID = inv.C_DocTypeTarget_ID) " +
								"inner join C_Currency cur on (inv.C_Currency_ID = cur.C_Currency_ID) " +
								"left outer join saldo sal on (par.C_BPartner_ID = sal.partner) " +
								"left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
								"left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) ";	
			where = "where inv.DocStatus = 'CO'  " +
					"and inv.isSoTrx = 'N' " +
					"and ord.XX_InvoiceDate is not null " +
					"and ((inv.XX_ApprovalDate is not null " +
					"and inv.XX_ApprovalDate between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd')) or " +
					"(inv.XX_ApprovalDate is null " +
					"and inv.created between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd') " +
					"and inv.XX_CONTROLNUMBER = ' ') " +
					"or (inv.XX_ACCOUNTPAYABLESTATUS = 'P' " + 
					"and inv.XX_DATEPAID between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd'))) " +
					"and inv.XX_InvoiceType = '"+ tipoFactura +"' " +
					"and ord.XX_InvoicingStatus = 'AP' " +
					"and (ord.XX_OrderType = 'Nacional' or con.XX_ContractType = 'Nacional') ";
		 
			groupBy = "group by par.C_BPartner_ID, cur.C_Currency_ID "; 
		
			sqlUnion = "union " +
					"select par.C_BPartner_ID, cur.C_Currency_ID, " +
					" sum(0), (pay.PayAmt) as b " +
					"from C_Payment pay " +
					"left join C_AllocationLine alc on (pay.C_Payment_id = alc.C_Payment_id) " +
					"left join C_Order ord on (ord.C_Order_ID = alc.C_Order_ID) " +
					"left join C_Order orde on (orde.C_Order_ID = pay.C_Order_ID) " +
					"inner join C_BPartner par on (par.C_BPartner_ID = pay.C_BPartner_ID) " +
					"inner join C_Currency cur on (pay.C_Currency_ID = cur.C_Currency_ID) " +
					"where pay.DocStatus = 'CO' " +
					"and pay.C_Currency_ID = 205 " +
					"and pay.DateTrx between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd') ";
			groupByUnion = "group by par.C_BPartner_ID, cur.C_Currency_ID, pay.PayAmt ";
		
			orderBy = "order by 1 asc ";
			
			if (tipoFactura.equals("A")){
				sqlUnion = sqlUnion + "and ((alc.C_Order_ID is not null and ord.XX_POType = 'POA' and ord.XX_PurchaseType in ('FA','SU')) or  " +
									"(pay.C_Order_ID is not null and orde.XX_POType = 'POA' and orde.XX_PurchaseType in ('FA','SU'))) ";
			}else if (tipoFactura.equals("S")){
				where = where + "and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) " +
						"and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') ";
						//"and (ord.XX_OrderType = 'Importada' or con.XX_ContractType = 'Importada') ";
				sqlUnion = sqlUnion + "and ((alc.C_Order_ID is not null and ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE' ) or " +
									"(pay.C_Order_ID is not null and orde.XX_POType = 'POA' and orde.XX_PurchaseType = 'SE')) ";
			}
		
		/*
		 * if (tipoFactura.equals("I")){
			sqlUnion = sqlUnion + "and ord.XX_POType = 'POM' ";
		}
		
		if (!tipoFactura.equals("S")){
			String sqlPartner = sqlPartner + "inner join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) ";
			where = where + "and ord.XX_InvoiceDate is not null " +
					"and ord.XX_InvoicingStatus = 'AP' " +
					"and ord.XX_OrderType = 'Importada' ";
		}
		 */
		
		sqlF = ") u " +
		   "group by u.C_BPartner_ID, u.C_Currency_ID " +
		   "order by 1 asc ";

		String sql = sqlP + sqlAmount + sqlPartner + where + groupBy + sqlUnion + groupByUnion + orderBy + sqlF;
		updateBalanceByPartner(sql, mes);	
	}
	}
	
	/**
	 * Actualiza el saldo de los proveedores nacionales en la tabla de Saldos 'XX_VCN_BalanceByPartner'
	 * @param mes mes actual
	 */
	public void newAmountBalanceImport(int mes, Timestamp to, Timestamp from){
		/****Buscar el saldo actual, calcular el nuevo saldo y actualizarlo****/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fHasta = sdf.format(new Date(to.getTime()));
		String fDesde = sdf.format(new Date(from.getTime()));		
		String sqlP;
		String sqlF;
		String where;
		String groupBy;
		String orderBy;
		String sqlUnion;
		String groupByUnion;

		sqlP = "select u.C_BPartner_ID, u.C_Currency_ID, sum(u.totalMo-b), sum(u.totalMl) " +
			   "from ( ";
		
		String sqlAmount = "with " +
						   "saldo as " +
						   "(select XX_AmountMonthImport as amountMi, XX_AmountMonthNational as amountMn, " +
						   "C_BPartner_ID as partner " +
						   "from XX_VCN_BalanceByPartner " +
						   "where XX_Month = " + (mes-1); 
						   if (tipoFactura.equals("I")){
							   sqlAmount = sqlAmount + " and XX_INVOICETYPE = 'I' " + ") ";  
							}else if (tipoFactura.equals("A")){
								sqlAmount = sqlAmount + " and XX_INVOICETYPE = 'A' " + ") ";   
							}else if (tipoFactura.equals("S")){
								sqlAmount = sqlAmount + " and XX_INVOICETYPE = 'S' " + ") ";  
							}
		
		if (!tipoFactura.equals("I")){
			String sqlPartner =  "select par.C_BPartner_ID, cur.C_Currency_ID, " +
								"sum((inv.GrandTotal - (coalesce((select WithholdingTaxInvoice (inv.C_Invoice_ID) " +
								"from dual),0)) - (nvl((select sum(a.XX_RetainedAmount) from XX_VCN_ISLRAMOUNT a " +
								"where inv.C_Invoice_ID = a.C_Invoice_ID),0)))+ (coalesce(sal.amountMn,0))) as totalMo, sum(0) b  " +	
								"from C_Invoice inv " +
								"inner join C_BPartner par on (par.C_BPartner_ID = inv.C_BPartner_ID) " +
								"inner join C_DocType doc on (doc.C_DocType_ID = inv.C_DocTypeTarget_ID) " +
								"inner join C_Currency cur on (inv.C_Currency_ID = cur.C_Currency_ID) " +
								"left outer join saldo sal on (par.C_BPartner_ID = sal.partner) " +
								"left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
								"left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) ";	
			where = "where inv.DocStatus = 'CO'  " +
					"and inv.isSoTrx = 'N' " +
					"and ord.XX_InvoiceDate is not null " +
					"and ((inv.XX_ApprovalDate is not null " +
					"and inv.XX_ApprovalDate between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd')) or " +
					"(inv.XX_ApprovalDate is null " +
					"and inv.created between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd') " +
					"and inv.XX_CONTROLNUMBER = ' ') " +
					"or (inv.XX_ACCOUNTPAYABLESTATUS = 'P' " + 
					"and inv.XX_DATEPAID between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd'))) " +
					"and inv.XX_InvoiceType = '"+ tipoFactura +"' " +
					"and ((inv.XX_Contract_ID is not null or inv.C_Order_ID is not null) " +
					"and (ord.XX_OrderType = 'Nacional' or con.XX_ContractType = 'Nacional') " +
					"and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) or " +
					"(inv.XX_Contract_ID is null or inv.C_Order_ID is  null)) " +
					"and (ord.XX_InvoicingStatus = 'AP' or inv.XX_INVOICINGSTATUSCONTRACT = 'AP') ";
		 
			groupBy = "group by par.C_BPartner_ID, cur.C_Currency_ID "; 
		
			sqlUnion = "union " +
					"select par.C_BPartner_ID, cur.C_Currency_ID, " +
					" sum(0), (pay.PayAmt) as b" +
					"from C_Payment pay " +
					"left join C_AllocationLine alc on (pay.C_Payment_id = alc.C_Payment_id) " +
					"left join C_Order ord on (ord.C_Order_ID = alc.C_Order_ID) " +
					"left join C_Order orde on (orde.C_Order_ID = pay.C_Order_ID) " +
					"inner join C_BPartner par on (par.C_BPartner_ID = pay.C_BPartner_ID) " +
					"inner join C_Currency cur on (pay.C_Currency_ID = cur.C_Currency_ID) " +
					"left outer join XX_Contract con on (pay.XX_Contract_ID = con.XX_Contract_ID) " +
					"where pay.DocStatus = 'CO' " +
					"and pay.C_Order_ID is not null " +
					"and pay.C_Currency_ID = 205 " +
					"and pay.DateTrx between to_date('"+ fDesde + "','yyyy-MM-dd') and to_date('"+ fHasta +"','yyyy-MM-dd') ";
			groupByUnion = "group by par.C_BPartner_ID, cur.C_Currency_ID, pay.PayAmt, pay.XX_PayAmtLocal, sal.amountMn, sal.amountMi ";
		
			orderBy = "order by 1 asc ";
			
			if (tipoFactura.equals("A")){
				sqlUnion = sqlUnion + "and ((ord.XX_InvoiceDate is not null and ord.XX_POType = 'POA' " +
									"and ord.XX_PurchaseType in ('FA','SU')) or pay.XX_Contract_ID is not null) ";
			}else if (tipoFactura.equals("S")){
				sqlPartner = sqlPartner + "left outer join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) " +
				 			 "left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) ";
				where = where + "and (ord.XX_InvoiceDate is not null or inv.XX_Contract_ID is not null) " +
						"and (ord.XX_InvoicingStatus = 'AP' or inv.XX_InvoicingStatusContract = 'AP') " +
						"and (ord.XX_OrderType = 'Importada' or con.XX_ContractType = 'Importada') ";
				sqlUnion = sqlUnion + "and ((ord.XX_POType = 'POA' and ord.XX_PurchaseType = 'SE') " +
									  "or pay.XX_Contract_ID is not null) ";
			}
		
		/*
		if (tipoFactura.equals("I")){
			sqlUnion = sqlUnion + "and ord.XX_POType = 'POM' ";
		}
		
		if (!tipoFactura.equals("S")){
			String sqlPartner = sqlPartner + "inner join C_Order ord on (ord.C_Order_ID = inv.C_Order_ID) ";
			where = where + "and ord.XX_InvoiceDate is not null " +
					"and ord.XX_InvoicingStatus = 'AP' " +
					"and ord.XX_OrderType = 'Importada' ";
		}
		*/
		sqlF = ") u " +
		   "group by u.C_BPartner_ID, u.C_Currency_ID " +
		   "order by 1 asc ";

		String sql = sqlP + sqlAmount + sqlPartner + where + groupBy + sqlUnion + groupByUnion + orderBy + sqlF;
		updateBalanceByPartner(sql, mes);		
	}
	}
	
	/**
	 * Actualiza o crea un nuevo registro, con el saldo del proveedor, en el mes correspondiente
	 * @param sql
	 * @return
	 */
	public void updateBalanceByPartner(String sql, int mes){
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();	
			while (rs.next()) {
				int id = searchPartnerInBalance(rs.getInt(1),mes);				
				//Busca el saldo del proveedor del mes anterior
				//Vector<BigDecimal> amount = searchAmountPartner(rs.getInt(1),(mes-1));
				if (id == 0){
					X_XX_VCN_BalanceByPartner balance = new X_XX_VCN_BalanceByPartner(Env.getCtx(),0,null);
					balance.setC_BPartner_ID(rs.getInt(1));
					if (rs.getInt(2) == 205) //Nacional
						balance.setXX_AmountMonthNational(rs.getBigDecimal(3));
					else{ //Importado
						balance.setXX_AmountMonthImport(rs.getBigDecimal(3));
						balance.setXX_AmountMonthNational(rs.getBigDecimal(4));
					}
					balance.setC_Currency_ID(rs.getInt(2)); 
					balance.setXX_Month(mes);
					balance.setXX_InvoiceType(tipoFactura);
					balance.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
					balance.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
					balance.save();
				}else{
					X_XX_VCN_BalanceByPartner balance = new X_XX_VCN_BalanceByPartner(Env.getCtx(),id,null);			
					if (rs.getInt(2) == 205) //Nacional
						balance.setXX_AmountMonthNational(rs.getBigDecimal(3));
					else{ //Importado
						balance.setXX_AmountMonthImport(rs.getBigDecimal(3));
						balance.setXX_AmountMonthNational(rs.getBigDecimal(4));
					}
					balance.setC_Currency_ID(rs.getInt(2));
					balance.save();
				}
			}
		}catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}finally{
			try {
				rs.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}try {
				pstmt.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Se encarga de buscar si existe un registro con ese socio de negocio y ese mes
	 * @param idPartner identificador del socio de negocio
	 * @param mes
	 * @return
	 */
	private Integer searchPartnerInBalance(int idPartner, int mes){
		int id = 0;
		String sql = "select XX_VCN_BalanceByPartner_ID " +
					 "from XX_VCN_BalanceByPartner " +
					 "where C_BPartner_ID = " + idPartner + " " +
					 "and XX_Month = " + mes + " " +
					 "and XX_InvoiceType = '" + tipoFactura + "' ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();	
			if (rs.next())
				id = rs.getInt(1);
		}catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}finally{
			try {
				rs.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}try {
				pstmt.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	
	/**
	 * Busca el saldo del proveedor del mes anterior al que se está 
	 * realizando el cierre de las cuentas por pagar
	 * @param idPartner identificador del proveedor
	 * @param mes
	 * @return
	 */
	private Vector<BigDecimal> searchAmountPartner(int idPartner, int mes){		
		Vector<BigDecimal> amount = new Vector<BigDecimal>(2);
		String sql = "select XX_AmountMonthImport as amountMo, " +
					 "XX_AmountMonthNational as amountMl " +
					 "from XX_VCN_BalanceByPartner " +
					 "where XX_Month = " + (mes-1) + " " +
					 "and C_BPartner_ID = " + idPartner;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();	
			if (rs.next()){
				amount.add(rs.getBigDecimal(1));
				amount.add(rs.getBigDecimal(2));
			}
		}catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}finally{
			try {
				rs.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}try {
				pstmt.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return amount;
	}
	
	/**
	 * Llena los combos del filtro
	 */
	private  void llenarCombos(){
		dynCategoryPartner();
		dynPartner();
		dynCountry();
		dynDateTo("S");
	}

	/**
	 * Se encarga de tomar el registro seleccionado de la primera tabla y llamar 
	 * al metodo tableInitS() que se encarga de llenar la segunda tabla y a su 
	 * vez el tableInitT() que se encarga de llenar la tercera tabla
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {		
	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
