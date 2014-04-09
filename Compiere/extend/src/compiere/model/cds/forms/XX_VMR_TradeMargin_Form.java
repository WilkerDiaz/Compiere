package compiere.model.cds.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.forms.indicator.XX_Indicator;

/*** Forma que muestra el porcentaje de cumplimiento del margen comercial
 * @author ghuchet*/

public class XX_VMR_TradeMargin_Form extends XX_Indicator {
	
	private static final long serialVersionUID = -6440503234752622560L;
	
	private CCheckBox checkConsolidated= new CCheckBox("Mostrar Acumulado");
	
	/** La tabla donde se guardarán todos datos */
	private MiniTablePreparator miniTableAll = new MiniTablePreparator();
	
	//	
	private CLabel labelXX_TradeMargin = new CLabel("Margen Inicial Bs.");
	private CTextField XX_TradeMargin= new CTextField();
	
	private CLabel labelAdjR = new CLabel("Regalía Bs.");
	private CTextField AdjR= new CTextField();
	
	private CLabel labelAdjC = new CLabel("Carga en Tránsito Bs.");
	private CTextField AdjC= new CTextField();
	
	private CLabel labelAdjN = new CLabel("Nota de Crédito/Debito Bs.");
	private CTextField AdjN= new CTextField();
	
	private CLabel labelAdjP = new CLabel("Promoción Merchandising Bs.");
	private CTextField AdjP= new CTextField();
	
	private CLabel labelXX_TradeMarginDef = new CLabel(Msg.translate(ctx, "XX_TradeMarginDef") + " Bs.");
	private CTextField XX_TradeMarginDef= new CTextField();
	
	private CLabel labelXX_BudgetMargin = new CLabel(Msg.translate(ctx, "XX_BudgetMargin")+ " Bs.");
	private CTextField XX_BudgetMargin= new CTextField();
	
	private CLabel labelXX_TradeMarginPerc = new CLabel(Msg.translate(ctx, "XX_TradeMarginPerc")+" Definitivo");
	private CTextField XX_TradeMarginPerc= new CTextField();
	
	private CLabel labelXX_BudgetMarginPerc = new CLabel(Msg.translate(ctx, "XX_BudgetMarginPerc"));
	private CTextField XX_BudgetMarginPerc= new CTextField();
	
	private CLabel labelXX_CompliancePerc = new CLabel(Msg.translate(ctx, "XX_CompliancePerc"));
	private CTextField XX_CompliancePerc= new CTextField();
	
	private CLabel labelXX_Sales = new CLabel("Venta Neta Bs.");
	private CTextField XX_Sales= new CTextField();
	
	private CLabel labelXX_Discounts = new CLabel("Rebajas Bs.");
	private CTextField XX_Discounts= new CTextField();
	
	private CLabel labelXX_EmployeeDiscounts = new CLabel("Descuento Sobre Ventas Bs.");
	private CTextField XX_EmployeeDiscounts= new CTextField();


	@Override
	protected void ocultarParametrosDefecto() {	

		//Parametros que no se desean para este indicador
		ocultarParametro(PRODUCTO);
		ocultarParametro(SECCION);
		ocultarParametro(LINEA);
		ocultarParametro(SOCIODENEGOCIO);
	}
	
	@Override
	protected void agregarParametros() {
		
		agregarParametro(checkConsolidated);
		
		XX_TradeMargin.setPreferredSize(new Dimension(130,20));
		AdjR.setPreferredSize(new Dimension(130,20));
		AdjC.setPreferredSize(new Dimension(130,20));
		AdjN.setPreferredSize(new Dimension(130,20));
		AdjP.setPreferredSize(new Dimension(130,20));
		XX_TradeMarginDef.setPreferredSize(new Dimension(130,20));
		XX_BudgetMargin.setPreferredSize(new Dimension(130,20));
		XX_TradeMarginPerc.setPreferredSize(new Dimension(130,20));
		XX_BudgetMarginPerc.setPreferredSize(new Dimension(130,20));
		XX_CompliancePerc.setPreferredSize(new Dimension(130,20));
		XX_Sales.setPreferredSize(new Dimension(130,20));
		XX_EmployeeDiscounts.setPreferredSize(new Dimension(130,20));
		XX_Discounts.setPreferredSize(new Dimension(130,20));
		
		XX_TradeMargin.setEditable(false);
		AdjR.setEditable(false);
		AdjC.setEditable(false);
		AdjN.setEditable(false);
		AdjP.setEditable(false);
		XX_TradeMarginDef.setEditable(false);
		XX_BudgetMargin.setEditable(false);
		XX_TradeMarginPerc.setEditable(false);
		XX_BudgetMarginPerc.setEditable(false);
		XX_CompliancePerc.setEditable(false);
		XX_Sales.setEditable(false);
		XX_EmployeeDiscounts.setEditable(false);
		XX_Discounts.setEditable(false);
		
		//primera columna
		resultPanel.add(labelXX_Sales,new GridBagConstraints(
				0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_Sales,new GridBagConstraints(
				1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelXX_TradeMarginDef,new GridBagConstraints(
				0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_TradeMarginDef,new GridBagConstraints(
				1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelXX_TradeMarginPerc,new GridBagConstraints(
				0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_TradeMarginPerc,new GridBagConstraints(
				1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelXX_BudgetMargin,new GridBagConstraints(
				0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_BudgetMargin,new GridBagConstraints(
				1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelXX_BudgetMarginPerc,new GridBagConstraints(
				0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_BudgetMarginPerc,new GridBagConstraints(
				1, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelXX_CompliancePerc,new GridBagConstraints(
				0, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_CompliancePerc,new GridBagConstraints(
				1, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		//Segunda columna
		resultPanel.add(labelXX_TradeMargin,new GridBagConstraints(
				2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_TradeMargin,new GridBagConstraints(
				3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelXX_EmployeeDiscounts,new GridBagConstraints(
				2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_EmployeeDiscounts,new GridBagConstraints(
				3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelAdjR,new GridBagConstraints(
				2, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(AdjR,new GridBagConstraints(
				3, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelAdjN,new GridBagConstraints(
				2, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(AdjN,new GridBagConstraints(
				3, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelAdjP,new GridBagConstraints(
				2, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(AdjP,new GridBagConstraints(
				3, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));	
		resultPanel.add(labelAdjC,new GridBagConstraints(
				2, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(AdjC,new GridBagConstraints(
				3, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		//tercera columna
		resultPanel.add(labelXX_Discounts,new GridBagConstraints(
				4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(XX_Discounts,new GridBagConstraints(
				5, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
	}

	@Override
	protected void personalizar() {
				
		statusBar.setStatusLine(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID")+'s');
		

		dataPane.setPreferredSize(new Dimension(1200, 400)); //Redimensionar el primer panel
		
		//Así mismo dice que el período es un campo obligatorio para procesar
		monthPeriod.setBackground(true);
		yearPeriod.setBackground(true);
		
		//Comenzando
		configurar(bSearch, false);
		configurar(bPrint, false);
		configurar(bFile, false);

		checkConsolidated.addActionListener(this);		
	}
	
	protected void limpiarFiltro () {
		
		//Limpia los campos del filtro padre
		super.limpiarFiltro();		

		
	}
	

	/** En este método agrego acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		
		//Esta opción exporta un archivo xls con los datos filtrados tanto del mes y año como los acumulados del año fiscal
		if (e.getSource() == bPrint){
			if((Boolean)checkCategory.getValue()){
				calcularQueryCatAll();
			}else{
				calcularQueryDepAll();
			}
					Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
					Cursor defaultCursor = Cursor.getDefaultCursor();
					m_frame.setCursor(waitCursor);
					try {
						imprimirArchivo(miniTableAll, bFile, m_WindowNo, m_frame); 
					} catch (Exception ex) {
						log.log(Level.SEVERE, "", ex);
					}			
					m_frame.setCursor(defaultCursor);
		}else {
			//Ejecutará las acciones por defecto
			super.actionPerformed(e);
		}
		desactivarActionListeners ();
		checkConsolidated.removeActionListener(this);	
		if (e.getSource() == monthPeriod || e.getSource() == yearPeriod) {
			boolean procesar = true;
			if (monthPeriod.getValue() != null) {
				monthPeriod.setBackground(false);				
			} else {
				monthPeriod.setBackground(true);
				procesar = false;
			}				
			if (yearPeriod.getValue() != null) {
				yearPeriod.setBackground(false);				
			} else {
				yearPeriod.setBackground(true);
				procesar = false;
			}
			//Verificar que los campos periodo se hayan marcado
			configurar(bSearch, procesar);			
		} 
		else if (e.getSource() == checkConsolidated) {

			if((Boolean)checkCategory.getValue()){
				if((Boolean)checkConsolidated.getValue()){
					calcularQueryCatAcum();
					calcularQueryConsolidated(true);
				}else{
					calcularQueryCat();
					calcularQueryConsolidated(false);
				}
			}else{
				if((Boolean)checkConsolidated.getValue()){
					calcularQueryDepAcum();
					calcularQueryConsolidated(true);
				}else{
					calcularQueryDep();
					calcularQueryConsolidated(false);
				}
			}
		}
		//Execute Search
		else if(e.getSource() == bSearch) {

			if((Boolean)checkCategory.getValue()){
				if((Boolean)checkConsolidated.getValue()){
					calcularQueryCatAcum();
					calcularQueryConsolidated(true);
				}else{
					calcularQueryCat();
					calcularQueryConsolidated(false);
				}
			}else{
				if((Boolean)checkConsolidated.getValue()){
					calcularQueryDepAcum();
					calcularQueryConsolidated(true);
				}else{
					calcularQueryDep();
					calcularQueryConsolidated(false);
				}
			}
			
		} 
		 activarActionListeners ();
		checkConsolidated.addActionListener(this);	
	}
	

	@Override
	protected void llenarTabla() {
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//Si no se ha cargado el header previamente
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		
		//Si está marcado el check de categoría se agrupa por categoría, en casa
		//contrario se agrupa por departamento.
		//Si está marcado el check de mostrar acumulado (checkConsolidated)
		//se nmuestra el acumulado del año fiscal al que pertenece el mes y año seleccionado
		try {

			if((Boolean)checkCategory.getValue()){
				if((Boolean)checkConsolidated.getValue()){
					calcularQueryCatAcum();
				}else{
					calcularQueryCat();
				}
			}else{
				if((Boolean)checkConsolidated.getValue()){
					calcularQueryDepAcum();
				}else{
					calcularQueryDep();

				}
			}

			miniTable.setRowSelectionAllowed(true);
			miniTable.setSelectionBackground(Color.white);
			miniTable.autoSize();
			miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
			miniTable.getTableHeader().setReorderingAllowed(false);			
			if (miniTable.getRowCount() > 0) {
				configurar(bFile, true);				
				configurar(bPrint, true);
			} else {
				configurar(bFile, false);
				configurar(bPrint, false);
			}
		} catch (Exception e) {}
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		
		
	}

	/** Se determina el query para el mes y año seleccionado, agrupado por Dpto*/
	public void calcularQueryDep () {
				
		int month=0;
		int year=0;
		
		if (monthPeriod.getValue() != null ){
			month = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			year = (Integer)yearPeriod.getValue();	;
			
		}	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();		

		
		String queryFrom =
			"\n FROM XX_VMR_TradeMargin tm" +
			"\n JOIN XX_VMR_Department dep ON (dep.XX_VMR_Department_ID = tm.XX_VMR_Department_ID)" +
			"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = dep.XX_VMR_Category_ID)";
			
		String query =
			"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month = "+month;
			
			
		String select=" ";
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
		
		
		//Categoría
		
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
	
			if (catg.getKey()!= 99999999 )			
					query += "\nAND cat.XX_VMR_Category_ID = " + catg.getKey();
		}		
		//Departamento			
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			
			if (dept.getKey()!= 99999999 ) 
					query += "\nAND dep.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
			
		}
		
		//Agrego las columnas 
		columnasAgregadas.add(colCatg);
		columnasAgregadas.add(colDept);
		columnasAgregadas.add(colSales);
		columnasAgregadas.add(colRealDef);
		columnasAgregadas.add(colRealPerc);
		columnasAgregadas.add(colBudget);
		columnasAgregadas.add(colBudgetPerc);
		columnasAgregadas.add(colPercent);
		columnasAgregadas.add(colDiscounts);
		columnasAgregadas.add(colReal);
		columnasAgregadas.add(colEmployeeDiscounts);
		columnasAgregadas.add(colAdjR);
		columnasAgregadas.add(colAdjP);
		columnasAgregadas.add(colAdjN);
		columnasAgregadas.add(colAdjC);
		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);

		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
	
		String orderBy = "\nOrder by cat.value||'-'||cat.Name, dep.value||'-'||dep.NAME ";
		String sql = select + queryFrom + query+ orderBy;
		
		//System.out.println("TABLA: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}			
	}
	
	/** Se determina el query acumulado para el año fiscal del mes y año seleccionado, agrupado por Dpto*/
	public void calcularQueryDepAcum() {
				
		int month=0;
		int year=0;
		
		if (monthPeriod.getValue() != null ){
			month = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			year = (Integer)yearPeriod.getValue();	;
			
		}	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();		

		
		String queryFrom =
			"\n FROM XX_VMR_TradeMargin tm" +
			"\n JOIN XX_VMR_Department dep ON (dep.XX_VMR_Department_ID = tm.XX_VMR_Department_ID)" +
			"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = dep.XX_VMR_Category_ID)";
			 
		String query = "\n";
				if(month<7){
					query =	"\nwhere ((tm.XX_Year ="+year+" AND tm.XX_Month <= "+month+") or (tm.XX_Year ="+(year-1)+" AND tm.XX_Month >= 7))";

				}else {
					query =	"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month >= 7 and tm.XX_Month <= "+month ;

				}
			
			
		String select=" ";
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
		
		
		//Categoría
		
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
	
			if (catg.getKey()!= 99999999 )			
					query += "\nAND cat.XX_VMR_Category_ID = " + catg.getKey();
		}		
		//Departamento			
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			
			if (dept.getKey()!= 99999999 ) 
					query += "\nAND dep.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
			
		}
		
		//Agrego las columnas 
		columnasAgregadas.add(colCatg);
		columnasAgregadas.add(colDept);
		columnasAgregadas.add(colSalesAcum);
		columnasAgregadas.add(colRealDefAcum);
		columnasAgregadas.add(colRealPercAcum);
		columnasAgregadas.add(colBudgetAcum);
		columnasAgregadas.add(colBudgetPercAcum);
		columnasAgregadas.add(colPercentAcum);
		columnasAgregadas.add(colDiscountsAcum);
		columnasAgregadas.add(colRealAcum);
		columnasAgregadas.add(colEmployeeDiscountsAcum);
		columnasAgregadas.add(colAdjRAcum);
		columnasAgregadas.add(colAdjPAcum);
		columnasAgregadas.add(colAdjNAcum);
		columnasAgregadas.add(colAdjCAcum);
		
		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);

		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
	
		String groupBy = "\nGroup by cat.value||'-'||cat.Name, dep.value||'-'||dep.NAME ";
		String orderBy = "\nOrder by cat.value||'-'||cat.Name, dep.value||'-'||dep.NAME ";

		String sql = select + queryFrom + query  + groupBy + orderBy;

		//System.out.println("TABLA: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}
	

	/** Se determina el query del mes y el acumulado para el año fiscal del mes y año seleccionado, agrupado por Dpto*/
	public void calcularQueryDepAll () {
				
		int month=0;
		int year=0;
		
		if (monthPeriod.getValue() != null ){
			month = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			year = (Integer)yearPeriod.getValue();	;
			
		}	

		ColumnInfo[] columnsAllDep = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), ".",String.class),
				new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"),".",String.class),	
				new ColumnInfo("Venta Neta Bs.", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginDef")+ " Bs.", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginPerc"), ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMargin")+ " Bs.", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMarginPerc"), ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_CompliancePerc"), ".",BigDecimal.class),
				new ColumnInfo("Rebajas Bs.", ".",BigDecimal.class),
				new ColumnInfo("Margen Inicial Bs.", ".",BigDecimal.class),
				new ColumnInfo("Descuento sobre Ventas Bs.", ".",BigDecimal.class),
				new ColumnInfo("Regalía Bs.","\ntm.XX_Adj_R",BigDecimal.class),
				new ColumnInfo("Promoción Merchandising Bs.","\ntm.XX_Adj_P",BigDecimal.class),
				new ColumnInfo("Nota de Crédito/Debito Bs.","\ntm.XX_Adj_N",BigDecimal.class),
				new ColumnInfo("Carga en Tránsito Bs.","\ntm.XX_Adj_C",BigDecimal.class),
				new ColumnInfo("Venta Neta Bs. Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginDef")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginPerc")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMargin")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMarginPerc")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_CompliancePerc")+" Acum", ".",BigDecimal.class),
				new ColumnInfo("Rebajas Bs. Acum", ".",BigDecimal.class),
				new ColumnInfo("Margen Inicial Bs.Acum", ".",BigDecimal.class),
				new ColumnInfo("Descuento sobre Ventas Bs. Acum", ".",BigDecimal.class),
				new ColumnInfo("Regalía Acum",".",BigDecimal.class),
				new ColumnInfo("Promoción Merchandising Acum",".",BigDecimal.class),
				new ColumnInfo("Nota de Crédito/Debito Acum",".",BigDecimal.class),
				new ColumnInfo("Carga en Tránsito Acum",".",BigDecimal.class),
		};
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTableAll.setRowCount(0);
		miniTableAll = new MiniTablePreparator();

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();		

		String withAcum = "\n WITH TradeAcum AS (" +
				"\n SELECT tm.XX_VMR_Department_ID, " +
				"\nDECODE(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount),NULL,0,round((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0)))/(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount))*100,2)) RealPercAcum, " +
				"\nDECODE(SUM(XX_BudgetSalesAmount),0,0,ROUND(SUM(XX_BudgetMargin)/SUM(XX_BudgetSalesAmount)*100,2)) BudgetPercAcum ,"+
				"\nround((sum(XX_RealSalesAmount)-sum(XX_TradeCost) - sum(NVL(XX_ZeroDiscount,0))),2) RealMarginAcum,  " +
				"\nround((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0))),2) RealDefAcum," +
				"\nsum(XX_BudgetMargin) BudgetMarginAcum," + 
				"\nROUND(DECODE(DECODE(SUM(XX_BudgetSalesAmount),0,0,SUM(XX_BudgetMargin)),0,0," +
				"\n(sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N) - sum(NVL(XX_ZeroDiscount,0))))/SUM(XX_BudgetMargin)*100),2) PercentAcum," +
				"\nsum(tm.XX_Adj_R) AdjRAcum, "+
				"\nsum(tm.XX_Adj_C) AdjCAcum, "+
				"\nsum(tm.XX_Adj_N) AdjNAcum, "+
				"\nsum(tm.XX_Adj_P) AdjPAcum," +
				"\nsum(tm.XX_Discount) DiscountAcum," +
				"\nsum(tm.XX_EmployeeDiscount) EmployeeDiscountAcum," +
				"\nSUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount) SalesAcum "+
				"\n FROM XX_VMR_TradeMargin tm" +
				"\n JOIN XX_VMR_Department dep ON (dep.XX_VMR_Department_ID = tm.XX_VMR_Department_ID)" +
				"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = dep.XX_VMR_Category_ID)" ;
				if(month<7){
					withAcum +=	"\nwhere ((tm.XX_Year ="+year+" AND tm.XX_Month <= "+month+") or (tm.XX_Year ="+(year-1)+" AND tm.XX_Month >= 7))";

				}else {
					withAcum +=	"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month >= 7 and tm.XX_Month <= "+month ;

				}
		
		String withMonth = "\n), Trade AS (" +
				"\n SELECT tm.XX_VMR_Department_ID, " +
				"\ntm.XX_TradeMarginPerc RealPerc, " +
				"\ntm.XX_BudgetMarginPerc BudgetPerc,"+
				"\ntm.XX_TradeMargin RealMargin,  " +
				"\ntm.XX_TradeMarginDef RealDef," +
				"\ntm.XX_BudgetMargin BudgetMargin," + 
				"\ntm.XX_CompliancePerc Percent," +
				"\ntm.XX_Adj_R AdjR, "+
				"\ntm.XX_Adj_C AdjC, "+
				"\ntm.XX_Adj_N AdjN, "+
				"\ntm.XX_Adj_P AdjP, " +
				"\ntm.XX_Discount Discount," +
				"\ntm.XX_EmployeeDiscount EmployeeDiscount," +
				"\nNVL(tm.XX_RealSalesAmount,0) - NVL(tm.XX_EmployeeDiscount,0) Sales"+
				"\n FROM XX_VMR_TradeMargin tm" +
				"\n JOIN XX_VMR_Department dep ON (dep.XX_VMR_Department_ID = tm.XX_VMR_Department_ID)" +
				"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = dep.XX_VMR_Category_ID)" +
				"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month = "+month;


		String queryExtra = " ";
		//Categoría
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
	
			if (catg.getKey()!= 99999999 )			
					queryExtra += "\nAND cat.XX_VMR_Category_ID = " + catg.getKey();
		}		
		//Departamento			
		if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {
			
			if (dept.getKey()!= 99999999 ) 
				queryExtra += "\nAND dep.XX_VMR_DEPARTMENT_ID = " + dept.getKey();
				
		}
		
		String select =
				"\n SELECT cat.value||'-'||cat.Name, " +
						"\ndep.value||'-'||dep.NAME, " +
						"\nSales," +
						"\nRealDef," +
						"\nRealPerc, " +
						"\nBudgetMargin," + 
						"\nBudgetPerc,"+
						"\nPercent," +
						"\nDiscount," +
						"\nRealMargin,  " +
						"\nEmployeeDiscount, "+
						"\nAdjR, "+
						"\nAdjP, "+
						"\nAdjN, "+
						"\nAdjC, "+
						"\nSalesAcum," +
						"\nRealDefAcum," +
						"\nRealPercAcum, " +
						"\nBudgetMarginAcum," + 
						"\nBudgetPercAcum ,"+
						"\nPercentAcum, " +
						"\nDiscountAcum, "+
						"\nRealMarginAcum,  " +
						"\nEmployeeDiscountAcum, "+
						"\nAdjRAcum, "+
						"\nAdjPAcum, "+
						"\nAdjNAcum, "+
						"\nAdjCAcum "+
				"\n FROM TradeAcum ta " +
				"\n JOIN XX_VMR_Department dep ON (dep.XX_VMR_Department_ID = ta.XX_VMR_Department_ID)" +
				"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = dep.XX_VMR_Category_ID)" +
				"\n LEFT JOIN  Trade t on (ta.XX_VMR_Department_ID = t.XX_VMR_Department_ID) "+
				"\n Order by cat.value||'-'||cat.Name, dep.value||'-'||dep.NAME ";
				
		miniTableAll.prepareTable(columnsAllDep,  "", "", true, "");

		String groupByWith = "\nGroup by   tm.XX_VMR_Department_ID";

		String sql = withAcum + queryExtra + groupByWith + withMonth + queryExtra + ") " +select;
		
		System.out.println("TABLA: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTableAll.loadTable(rs);
			miniTableAll.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}			
	}

	/** Se determina el query para el mes y año seleccionado, agrupado por Categoria*/
	public void calcularQueryCat () {
				
		int month=0;
		int year=0;
		
		if (monthPeriod.getValue() != null ){
			month = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			year = (Integer)yearPeriod.getValue();	;
			
		}	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		String queryFrom =
			"\n FROM XX_VMR_TradeMargin tm" +
			"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = tm.XX_VMR_Category_ID)";
			
		String query =
			"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month = "+month;
			
			
		String select=" ";
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		//Agrego las columnas 
		columnasAgregadas.add(colCatg);
		columnasAgregadas.add(colSales);
		columnasAgregadas.add(colRealDef);
		columnasAgregadas.add(colRealPerc);
		columnasAgregadas.add(colBudget);
		columnasAgregadas.add(colBudgetPerc);
		columnasAgregadas.add(colPercent);
		columnasAgregadas.add(colDiscounts);
		columnasAgregadas.add(colReal);
		columnasAgregadas.add(colEmployeeDiscounts);
		columnasAgregadas.add(colAdjR);
		columnasAgregadas.add(colAdjP);
		columnasAgregadas.add(colAdjN);
		columnasAgregadas.add(colAdjC);
		

		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);

		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
	
		String orderBy = "\nOrder by cat.value||'-'||cat.Name ";
		String sql = select + queryFrom + query+ orderBy;
		
		//System.out.println("TABLA: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}			
	}
	
	/** Se determina el query del acumulado para el año fiscal del mes y año seleccionado, agrupado por Categoria*/
	public void calcularQueryCatAcum () {
				
		int month=0;
		int year=0;
		
		if (monthPeriod.getValue() != null ){
			month = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			year = (Integer)yearPeriod.getValue();	;
			
		}	
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);

		String queryFrom =
			"\n FROM XX_VMR_TradeMargin tm" +
			"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = tm.XX_VMR_Category_ID)";
			
		String query = "\n";
		if(month<7){
			query =	"\nwhere ((tm.XX_Year ="+year+" AND tm.XX_Month <= "+month+") or (tm.XX_Year ="+(year-1)+" AND tm.XX_Month >= 7))";

		}else {
			query =	"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month >= 7 and tm.XX_Month <= "+month ;

		}
			
		String select=" ";
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();

		//Agrego las columnas 
		columnasAgregadas.add(colCatg);
		columnasAgregadas.add(colSalesAcum);
		columnasAgregadas.add(colRealDefAcum);
		columnasAgregadas.add(colRealPercAcum);
		columnasAgregadas.add(colBudgetAcum);
		columnasAgregadas.add(colBudgetPercAcum);
		columnasAgregadas.add(colPercentAcum);
		columnasAgregadas.add(colDiscountsAcum);
		columnasAgregadas.add(colRealAcum);
		columnasAgregadas.add(colEmployeeDiscountsAcum);
		columnasAgregadas.add(colAdjRAcum);
		columnasAgregadas.add(colAdjPAcum);
		columnasAgregadas.add(colAdjNAcum);
		columnasAgregadas.add(colAdjCAcum);
		

		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);

		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
	
		String orderBy = "\nOrder by cat.value||'-'||cat.Name ";
		String groupBy = "\nGroup by cat.value||'-'||cat.Name ";

		String sql = select + queryFrom + query  + groupBy + orderBy;
		
		//System.out.println("TABLA: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}			
	}
	

	/** Se determina el query del mes y el  acumulado para el año fiscal del mes y año seleccionado, agrupado por Categoria*/
	public void calcularQueryCatAll () {
				
		int month=0;
		int year=0;
		
		if (monthPeriod.getValue() != null ){
			month = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			year = (Integer)yearPeriod.getValue();	;
			
		}	

		ColumnInfo[] columnsAllDep = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), ".",String.class),
				new ColumnInfo("Venta Neta Bs.", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginDef")+ " Bs.", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginPerc"), ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMargin")+ " Bs.", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMarginPerc"), ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_CompliancePerc"), ".",BigDecimal.class),
				new ColumnInfo("Rebajas Bs.", ".",BigDecimal.class),
				new ColumnInfo("Margen Inicial Bs.", ".",BigDecimal.class),
				new ColumnInfo("Descuento sobre Ventas Bs.", ".",BigDecimal.class),
				new ColumnInfo("Regalía Bs.","\ntm.XX_Adj_R",BigDecimal.class),
				new ColumnInfo("Promoción Merchandising Bs.","\ntm.XX_Adj_P",BigDecimal.class),
				new ColumnInfo("Nota de Crédito/Debito Bs.","\ntm.XX_Adj_N",BigDecimal.class),
				new ColumnInfo("Carga en Tránsito Bs.","\ntm.XX_Adj_C",BigDecimal.class),
				new ColumnInfo("Venta Neta Bs. Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginDef")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginPerc")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMargin")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_BudgetMarginPerc")+" Acum", ".",BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "XX_CompliancePerc")+" Acum", ".",BigDecimal.class),
				new ColumnInfo("Rebajas Bs. Acum", ".",BigDecimal.class),
				new ColumnInfo("Margen Inicial Bs.Acum", ".",BigDecimal.class),
				new ColumnInfo("Descuento sobre Ventas Bs. Acum", ".",BigDecimal.class),
				new ColumnInfo("Regalía Acum",".",BigDecimal.class),
				new ColumnInfo("Promoción Merchandising Acum",".",BigDecimal.class),
				new ColumnInfo("Nota de Crédito/Debito Acum",".",BigDecimal.class),
				new ColumnInfo("Carga en Tránsito Acum",".",BigDecimal.class),

		};
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
		miniTableAll.setRowCount(0);
		miniTableAll = new MiniTablePreparator();

		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();

		String withAcum = "\n WITH TradeAcum AS (" +
				"\n SELECT tm.XX_VMR_Category_ID, " +
				"\nDECODE(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount),NULL,0,round((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N))- sum(NVL(XX_ZeroDiscount,0)))/(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount))*100,2)) RealPercAcum, " +
				"\nDECODE(SUM(XX_BudgetSalesAmount),0,0,ROUND(SUM(XX_BudgetMargin)/SUM(XX_BudgetSalesAmount)*100,2)) BudgetPercAcum ,"+
				"\nround((sum(XX_RealSalesAmount)-sum(XX_TradeCost) - sum(NVL(XX_ZeroDiscount,0))),2) RealMarginAcum,  " +
				"\nround((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0))),2) RealDefAcum," +
				"\nsum(XX_BudgetMargin) BudgetMarginAcum," + 
				"\nROUND(DECODE(DECODE(SUM(XX_BudgetSalesAmount),0,0,SUM(XX_BudgetMargin)),0,0," +
				"\n(sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0)))/SUM(XX_BudgetMargin)*100),2) PercentAcum," +
				"\nsum(tm.XX_Adj_R) AdjRAcum, "+
				"\nsum(tm.XX_Adj_C) AdjCAcum, "+
				"\nsum(tm.XX_Adj_N) AdjNAcum, "+
				"\nsum(tm.XX_Adj_P) AdjPAcum, "+
				"\nsum(tm.XX_Discount) DiscountAcum," +
				"\nsum(tm.XX_EmployeeDiscount) EmployeeDiscountAcum," +
				"\nSUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount) SalesAcum "+
				"\n FROM XX_VMR_TradeMargin tm" +
				"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = tm.XX_VMR_Category_ID)" ;
				if(month<7){
					withAcum +=	"\nwhere ((tm.XX_Year ="+year+" AND tm.XX_Month <= "+month+") or (tm.XX_Year ="+(year-1)+" AND tm.XX_Month >= 7))";

				}else {
					withAcum +=	"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month >= 7 and tm.XX_Month <= "+month ;

				}
		
		String withMonth = "\n), Trade AS (" +
				"\n SELECT tm.XX_VMR_Category_ID, " +
				"\ntm.XX_TradeMarginPerc RealPerc, " +
				"\ntm.XX_BudgetMarginPerc BudgetPerc,"+
				"\ntm.XX_TradeMargin RealMargin,  " +
				"\ntm.XX_TradeMarginDef RealDef," +
				"\ntm.XX_BudgetMargin BudgetMargin," + 
				"\ntm.XX_CompliancePerc Percent," +
				"\ntm.XX_Adj_R AdjR, "+
				"\ntm.XX_Adj_C AdjC, "+
				"\ntm.XX_Adj_N AdjN, "+
				"\ntm.XX_Adj_P AdjP, "+
				"\ntm.XX_Discount Discount," +
				"\ntm.XX_EmployeeDiscount EmployeeDiscount," +
				"\nNVL(tm.XX_RealSalesAmount,0) - NVL(tm.XX_EmployeeDiscount,0) Sales"+
				"\n FROM XX_VMR_TradeMargin tm" +
				"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = tm.XX_VMR_Category_ID)" +
				"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month = "+month;


		String queryExtra = " ";
		//Categoría
		if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
	
			if (catg.getKey()!= 99999999 )			
					queryExtra += "\nAND cat.XX_VMR_Category_ID = " + catg.getKey();
		}		
		
		String select =
				"\n SELECT cat.value||'-'||cat.Name, " +
						"\nSales," +
						"\nRealDef," +
						"\nRealPerc, " +
						"\nBudgetMargin," + 
						"\nBudgetPerc,"+
						"\nPercent," +
						"\nDiscount," +
						"\nRealMargin,  " +
						"\nEmployeeDiscount, "+
						"\nAdjR, "+
						"\nAdjP, "+
						"\nAdjN, "+
						"\nAdjC, "+
						"\nSalesAcum," +
						"\nRealDefAcum," +
						"\nRealPercAcum, " +
						"\nBudgetMarginAcum," + 
						"\nBudgetPercAcum ,"+
						"\nPercentAcum, " +
						"\nDiscountAcum, "+
						"\nRealMarginAcum,  " +
						"\nEmployeeDiscountAcum, "+
						"\nAdjRAcum, "+
						"\nAdjPAcum, "+
						"\nAdjNAcum, "+
						"\nAdjCAcum "+
				"\n FROM TradeAcum ta " +
				"\n JOIN XX_VMR_Category cat ON (cat.XX_VMR_Category_ID = ta.XX_VMR_Category_ID)" +
				"\n LEFT JOIN  Trade t on (ta.XX_VMR_Category_ID = t.XX_VMR_Category_ID) "+
				"\n Order by cat.value||'-'||cat.Name ";
				
		
		miniTableAll.prepareTable(columnsAllDep,  "", "", true, "");

		String groupByWith = "\nGroup by   tm.XX_VMR_Category_ID";

		String sql = withAcum + queryExtra + groupByWith + withMonth + queryExtra + ") " +select;
		
		//System.out.println("TABLA: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTableAll.loadTable(rs);
			miniTableAll.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}			
	}
	

	//Calcula el acumulado del año fiscal si acum es True, si acum es False calcula el mes de la consulta
	private void calcularQueryConsolidated(boolean acum) {
		
		int month=0;
		int year=0;
		KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
		KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();		
		
		if (monthPeriod.getValue() != null ){
			month = (Integer)monthPeriod.getValue(); 
		}
		if (yearPeriod.getValue() != null ){
			
			year = (Integer)yearPeriod.getValue();	;
			
		}	
		String sql = "\n SELECT  " +
				"\nto_char(DECODE(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount),NULL,0,round((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0)))/(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount))*100,2)), '999G999G999G999D99') RealPercAcum, " +
				"\nto_char(DECODE(SUM(XX_BudgetSalesAmount),0,0,ROUND(SUM(XX_BudgetMargin)/SUM(XX_BudgetSalesAmount)*100,2)), '999G999G999G999D99') BudgetPercAcum ,"+
				"\nto_char(round((sum(XX_RealSalesAmount)-sum(XX_TradeCost) - sum(NVL(XX_ZeroDiscount,0))),2), '999G999G999G999D99') RealMarginAcum,  " +
				"\nto_char(round((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0))),2), '999G999G999G999D99') RealDefAcum," +
				"\nto_char(sum(XX_BudgetMargin), '999G999G999G999D99') BudgetMarginAcum," + 
				"\nto_char(ROUND(DECODE(DECODE(SUM(XX_BudgetSalesAmount),0,0,SUM(XX_BudgetMargin)),0,0," +
				"\n(sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0)))/SUM(XX_BudgetMargin)*100),2), '999G999G999G999D99') PercentAcum," +
				"\nto_char(sum(tm.XX_Adj_R), '999G999G999G999D99') AdjRAcum, "+
				"\nto_char(sum(tm.XX_Adj_C), '999G999G999G999D99') AdjCAcum, "+
				"\nto_char(sum(tm.XX_Adj_N), '999G999G999G999D99') AdjNAcum, "+
				"\nto_char(sum(tm.XX_Adj_P), '999G999G999G999D99') AdjPAcum, "+
				"\nto_char(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount), '999G999G999G999D99') SalesAcum, "+
				"\nto_char(sum(XX_EmployeeDiscount), '999G999G999G999D99') EmployeeDiscountAcum, "+
				"\nto_char(sum(XX_Discount), '999G999G999G999D99') DiscountAcum "+
				"\n FROM XX_VMR_TradeMargin tm" ;

				if(acum){
					if(month<7){
						sql +=	"\nwhere ((tm.XX_Year ="+year+" AND tm.XX_Month <= "+month+") or (tm.XX_Year ="+(year-1)+" AND tm.XX_Month >= 7))";

					}else {
						sql +=	"\nwhere tm.XX_Year ="+year+" AND tm.XX_Month >= 7 and tm.XX_Month <= "+month ;

					}
				}else {
					sql += "\nwhere tm.XX_Year ="+year+" AND tm.XX_Month = "+month ;
				}
			
				//Departamento			
				if((Boolean)checkDepartment.getValue()==true || dept != null && (dept.getKey()==99999999 ||  dept.getKey()!= 0)) {			
					if (dept.getKey()!= 99999999 ) 
						sql += "\nAND tm.XX_VMR_DEPARTMENT_ID = " + dept.getKey();		
					else {
						if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
							if (catg.getKey()!= 99999999 )			
								sql += "\nAND tm.XX_VMR_Category_ID = " + catg.getKey();
							else sql += "\nAND XX_VMR_Category_ID is not null";
						}else {
							sql += "\nAND XX_VMR_Category_ID is not null";
						}
					}
				}else {
					//Categoría
					if((Boolean)checkCategory.getValue()==true || catg != null && (catg.getKey()==99999999 || catg.getKey()!= 0 )) {	
						if (catg.getKey()!= 99999999 )			
							sql += "\nAND tm.XX_VMR_Category_ID = " + catg.getKey();
						else sql += "\nAND XX_VMR_Category_ID is not null";
					}else {
						sql += "\nAND XX_VMR_Category_ID is not null";
					}
				}
				
			
		
		System.out.println("TABLA: "+sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			
			if(rs.next()){
				XX_TradeMargin.setText(rs.getString("RealMarginAcum"));
				AdjR.setText(rs.getString("AdjRAcum"));
				AdjC.setText(rs.getString("AdjCAcum"));
				AdjN.setText(rs.getString("AdjNAcum"));
				AdjP.setText(rs.getString("AdjPAcum"));
				XX_TradeMarginDef.setText(rs.getString("RealDefAcum"));
				XX_BudgetMargin.setText(rs.getString("BudgetMarginAcum"));
				XX_TradeMarginPerc.setText(rs.getString("RealPercAcum"));
				XX_BudgetMarginPerc.setText(rs.getString("BudgetPercAcum"));
				XX_CompliancePerc.setText(rs.getString("PercentAcum"));
				XX_Sales.setText(rs.getString("SalesAcum"));
				XX_Discounts.setText(rs.getString("DiscountAcum"));
				XX_EmployeeDiscounts.setText(rs.getString("EmployeeDiscountAcum"));
			}
			
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}			
	}
	
	//Cabeceras de las columnas de la tabla para un mes

	private ColumnInfo colCatg = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), 
			"\ncat.value||'-'||cat.Name",String.class);
	private ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"),
			"\ndep.value||'-'||dep.NAME",String.class);	
	private ColumnInfo colRealPerc = new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginPerc"), 
			"\ntm.XX_TradeMarginPerc",BigDecimal.class);
	private ColumnInfo colBudgetPerc = new ColumnInfo(Msg.translate(ctx, "XX_BudgetMarginPerc"), 
			"\ntm.XX_BudgetMarginPerc",BigDecimal.class);
	private ColumnInfo colReal = new ColumnInfo(Msg.translate(ctx, "XX_TradeMargin") +" Bs.", 
			"\ntm.XX_TradeMargin",BigDecimal.class);
	private ColumnInfo colRealDef = new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginDef") +" Bs.", 
			"\ntm.XX_TradeMarginDef",BigDecimal.class);
	private ColumnInfo colBudget = new ColumnInfo(Msg.translate(ctx, "XX_BudgetMargin") +" Bs.", 
			"\ntm.XX_BudgetMargin",BigDecimal.class);
	private ColumnInfo colPercent = new ColumnInfo(Msg.translate(ctx, "XX_CompliancePerc"), 
			"\ntm.XX_CompliancePerc",BigDecimal.class);
	private ColumnInfo colAdjR = new ColumnInfo("Regalía Bs.","\ntm.XX_Adj_R",BigDecimal.class);
	private ColumnInfo colAdjC = new ColumnInfo("Carga en Tránsito Bs.","\ntm.XX_Adj_C",BigDecimal.class);
	private ColumnInfo colAdjN = new ColumnInfo("Nota de Crédito/Debito Bs.","\ntm.XX_Adj_N",BigDecimal.class);
	private ColumnInfo colAdjP = new ColumnInfo("Promoción Merchandising Bs.","\ntm.XX_Adj_P",BigDecimal.class);
	private ColumnInfo colSales = new ColumnInfo("Venta Neta Bs.","\nnvl(tm.XX_RealSalesAmount,0)-nvl(tm.XX_EmployeeDiscount,0)",BigDecimal.class);
	private ColumnInfo colDiscounts = new ColumnInfo("Rebajas Bs.","\ntm.XX_Discount",BigDecimal.class);
	private ColumnInfo colEmployeeDiscounts = new ColumnInfo("Descuento Sobre Ventas Bs.","\ntm.XX_EmployeeDiscount",BigDecimal.class);

	//Cabeceras de las columnas de la tabla para el acumulado
	private ColumnInfo colRealPercAcum = new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginPerc"), 
			"\nDECODE(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount),0,0,round((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0)))/(SUM(XX_RealSalesAmount)-sum(XX_EmployeeDiscount))*100,2))",BigDecimal.class);
	private ColumnInfo colBudgetPercAcum = new ColumnInfo(Msg.translate(ctx, "XX_BudgetMarginPerc"), 
			"\nDECODE(SUM(XX_BudgetSalesAmount),0,0,ROUND(SUM(XX_BudgetMargin)/SUM(XX_BudgetSalesAmount)*100,2))",BigDecimal.class);
	private ColumnInfo colRealAcum = new ColumnInfo("Margen Inicial Bs.", 
			"\nround((sum(XX_RealSalesAmount)-sum(XX_TradeCost) - sum(NVL(XX_ZeroDiscount,0))),2)",BigDecimal.class);
	private ColumnInfo colRealDefAcum = new ColumnInfo(Msg.translate(ctx, "XX_TradeMarginDef") +" Bs.", 
			"\nround((sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0))),2)",BigDecimal.class);
	private ColumnInfo colBudgetAcum = new ColumnInfo(Msg.translate(ctx, "XX_BudgetMargin") +" Bs.", 
			"\nsum(XX_BudgetMargin)",BigDecimal.class);
	private ColumnInfo colPercentAcum = new ColumnInfo(Msg.translate(ctx, "XX_CompliancePerc"), 
			"\nROUND(DECODE(DECODE(SUM(XX_BudgetMargin),0,0,SUM(XX_BudgetMargin)),0,0," +
			"(sum(XX_RealSalesAmount)-sum(XX_EmployeeDiscount)-(sum(XX_TradeCost) + sum(XX_ADJ_R + XX_ADJ_P + XX_ADJ_C + XX_ADJ_N)) - sum(NVL(XX_ZeroDiscount,0)))/SUM(XX_BudgetMargin)*100),2)",BigDecimal.class);
	private ColumnInfo colAdjRAcum = new ColumnInfo("Regalía Bs.","\nsum(tm.XX_Adj_R)",BigDecimal.class);
	private ColumnInfo colAdjCAcum = new ColumnInfo("Carga en Tránsito Bs.","\nsum(tm.XX_Adj_C)",BigDecimal.class);
	private ColumnInfo colAdjNAcum = new ColumnInfo("Nota de Crédito/Debito Bs.","\nsum(tm.XX_Adj_N)",BigDecimal.class);
	private ColumnInfo colAdjPAcum = new ColumnInfo("Promoción Merchandising Bs.","\nsum(tm.XX_Adj_P)",BigDecimal.class);
	private ColumnInfo colSalesAcum = new ColumnInfo("Venta Neta Bs.","\nSUM(tm.XX_RealSalesAmount)-sum(tm.XX_EmployeeDiscount)",BigDecimal.class);
	private ColumnInfo colDiscountsAcum = new ColumnInfo("Rebajas Bs.","\nsum(tm.XX_Discount)",BigDecimal.class);
	private ColumnInfo colEmployeeDiscountsAcum = new ColumnInfo("Descuento Sobre Ventas Bs.","\nsum(tm.XX_EmployeeDiscount)",BigDecimal.class);

}

