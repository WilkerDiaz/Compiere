package compiere.model.promociones.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VComboBox;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CLabel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.forms.indicator.XX_Indicator;

public class XX_ProductSelectForm extends XX_Indicator{

	


		//Campos necesarios para este indicador 
		private VCheckBox checkTienda = new VCheckBox();
		private CLabel labelTienda = new CLabel(Msg.translate(ctx, "XX_Store"));
		private VComboBox comboTienda = new VComboBox();
		private VCheckBox checkMarca = new VCheckBox();
		private CLabel labelMarca = new CLabel(Msg.translate(ctx, "XX_VMR_Brand_ID "));
		private VComboBox comboMarca = new VComboBox();
		private VCheckBox checkReferencia= new VCheckBox();
		private CLabel labelReferencia = new CLabel(Msg.translate(ctx, "XX_VMR_VendorProdRef_ID"));
		private VComboBox comboReferencia= new VComboBox();
		private CLabel labelDetalle= new CLabel("Detalle");
		private VComboBox comboDetalle = new VComboBox();
		
		//Indice de la tabla
		int indiceTabla = 0;	
		@Override
		protected void ocultarParametrosDefecto() {		
			
			//Producto no es un campo que se desee para este indicador
			ocultarParametro(PRODUCTO);
			ocultarParametro(PERIODO);
			
		}
		
		
		@Override
		protected void agregarParametros() {
			
			agregarParametro(labelMarca);
			agregarParametro(comboMarca);
			agregarParametro(checkMarca);
			agregarParametro(labelReferencia);
			agregarParametro(comboReferencia);
			agregarParametro(checkReferencia);
			agregarParametro(labelTienda);
			agregarParametro(comboTienda);
			agregarParametro(checkTienda);
			agregarParametro(labelDetalle);
			agregarParametro(comboDetalle);
			comboReferencia.addActionListener(this);
			cargarDetalle();
			comboDetalle.addActionListener(this);
		}
		
		@Override
		protected void personalizar() {
			
			//Cargar los datos de los campos que se agregaron en este indicador
			cargarTiendas();
			cargarMarcas();
			
			//Configurar dichos campos
			configurar(comboTienda, false);
			configurar(comboMarca, false);
			configurar(comboReferencia, true);
			configurar(comboDetalle, true);

							
			//Así mismo dice que el período es un campo obligatorio para procesar
			
			//Comenzando
			configurar(bSearch, false);
			configurar(bPrint, false);
			configurar(bFile, false);
		
			//Agregar listeners a los nuevos campos
		}
		private void cargarDetalle(){
			KeyNamePair loadKNP = new KeyNamePair(1,"Producto");
			KeyNamePair loadKNP2 = new KeyNamePair(2,"Referencia");
			comboDetalle.addItem(loadKNP);
			comboDetalle.addItem(loadKNP2);
		}
		
		private void cargarReferencias() {
			KeyNamePair loadKNP
			, dpto = (KeyNamePair)comboDepartment.getSelectedItem()
			, line = (KeyNamePair)comboLine.getSelectedItem()
			, section=(KeyNamePair)comboSection.getSelectedItem()
			, partner = (KeyNamePair) comboBPartner.getSelectedItem();
			String sql = "";						
			comboReferencia.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboReferencia.addItem(loadKNP);	
			sql = " SELECT XX_VMR_VENDORPRODREF_ID, VALUE||'-'||NAME " +
					" FROM XX_VMR_VENDORPRODREF WHERE ISACTIVE = 'Y' " ;
			if (partner.getKey()!=0)
				sql+= " and C_BPARTNER_ID="+partner.getKey();
			else ;
			if (dpto.getKey()!=0)
				sql+= " and (XX_VMR_DEPARTMENT_ID="+dpto.getKey()+" and XX_VMR_LINE_ID=null and XX_VMR_SECTION_ID=null) ";
			else ;
			if (dpto.getKey()!=0 && line.getKey()!=0)
				sql+= " or (XX_VMR_DEPARTMENT_ID ="+dpto.getKey()+" and XX_VMR_LINE_ID="+(line==null?"0":line.getKey())+" and XX_VMR_SECTION_ID=null)";
			else;
			if (dpto.getKey()!=0 && line.getKey()!=0 && section.getKey()!=0)
				sql+= " or (XX_VMR_DEPARTMENT_ID ="+dpto.getKey()+"and XX_VMR_LINE_ID="+(line==null?"0":line.getKey())+" and XX_VMR_SECTION_ID="+(section==null?"0":section.getKey())+")" ;
			else ;
				sql+= "ORDER BY VALUE"; 		
			try{			
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
					comboReferencia.addItem(loadKNP);			
				}			
				rs.close();
				pstmt.close();		
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}		
		}

		private void cargarMarcas() {
			KeyNamePair loadKNP;
			String sql = "";						
			comboMarca.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboMarca.addItem(loadKNP);	
			sql = " SELECT XX_VMR_BRAND_ID, VALUE||'-'||NAME " +
					" FROM XX_VMR_BRAND WHERE ISACTIVE = 'Y' " +
					"ORDER BY VALUE"; 		
			try{			
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
					comboMarca.addItem(loadKNP);			
				}			
				rs.close();
				pstmt.close();		
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}	
		}

		protected void limpiarFiltro () {
			
			//Limpia los campos del filtro padre
			super.limpiarFiltro();		
			
			//Borrar aquellos campos que se agregaron en esta forma
			configurar(comboTienda, false);	
			configurar(comboMarca, false);
			configurar(comboReferencia, true);
			configurar(comboDetalle, false);
		}
		
		
		@Override
		/** En este método agrego acciones a las acciones predeterminadas */
		public void actionPerformed(ActionEvent e) {
			
			//Ejecutará las acciones por defecto
			super.actionPerformed(e);
			
			//Desactivar los escuchadores mientras realizo las modifciaciones
			desactivarActionListeners();

			//Acciones adicionales a las que vienen con el padre para deshabilitar un campo
			if (e.getSource() == checkCategory) {			
				configurar(comboDepartment, false);	
				
			}
			else if (e.getSource() == comboCategory) {			
				KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
				if (catg != null && catg.getKey() != 0 && catg.getKey() != 99999999){ 
					configurar(comboDepartment, true);
					configurar(comboLine, true);
					configurar(comboSection, true);
					configurar(comboReferencia, true);
				} else {
					configurar(comboDepartment, false);
					configurar(comboLine, false);
					configurar(comboSection, false);
					configurar(comboReferencia, false);
				}	
			}else if (e.getSource() == comboDepartment) {
				KeyNamePair dep = (KeyNamePair)comboDepartment.getSelectedItem();
				if (dep != null && dep.getKey() != 0 && dep.getKey() != 99999999){ 
					configurar(comboLine, true);
					configurar(comboSection, true);
					configurar(comboReferencia, true);
				} else {
					configurar(comboLine, false);
					configurar(comboSection, false);
					configurar(comboReferencia, false);
				}	
			} else if (e.getSource() == comboLine) {
				KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
				if (line != null && line.getKey() != 0 && line.getKey() != 99999999){ 
					configurar(comboSection, true);
					configurar(comboReferencia, true);
				} else {
					configurar(comboSection, false);
					configurar(comboReferencia, false);
				}	
			}else if (e.getSource() == comboSection) {
				KeyNamePair line = (KeyNamePair)comboSection.getSelectedItem();
				if (line != null && line.getKey() != 0 && line.getKey() != 99999999){ 
					cargarReferencias();
					configurar(comboReferencia, true);
				} else {
					configurar(comboReferencia, false);
				}
			}
			
			//Activar los escuchadores
			activarActionListeners();
		}

		
		/** Cargar las tiendas  */
		protected final void cargarTiendas() {
			
			KeyNamePair loadKNP;
			String sql = "";						
			comboTienda.removeAllItems();
			loadKNP = new KeyNamePair(0,"");
			comboTienda.addItem(loadKNP);
			loadKNP = new KeyNamePair(99999999,Msg.translate(Env.getCtx(), "XX_AllStores"));
			comboTienda.addItem(loadKNP);	
			sql = " SELECT ct.M_WAREHOUSE_ID, ct.VALUE||'-'||ct.NAME " +
					" FROM M_WAREHOUSE ct WHERE ct.ISACTIVE = 'Y' " +
					"AND ct.M_WAREHOUSE_ID != " + Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID")
					+ "ORDER BY ct.VALUE"; 		
			try{			
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
					comboTienda.addItem(loadKNP);			
				}			
				rs.close();
				pstmt.close();		
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}	
		}
		
		/** Cargar los compradores */
		
		@Override
		protected void llenarTabla() {

			m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			//Verificar que el periodo se haya 
			if (monthPeriod.getValue() == null || yearPeriod.getValue() == null) {
				ADialog.error(m_WindowNo, m_frame, "XX_MandatoryPeriod" );
				return;		
			}	
			//Si no se ha cargado el header previamente
			miniTable.setRowCount(0);
			miniTable = new MiniTablePreparator();

			//Calcular el query
			try {
				calcularQuery();			
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
		
		/** Se determina el query de acuerdo a los parámetros ingresados*/
		public void calcularQuery () {
					
			int mes = (Integer)monthPeriod.getValue(); 
			int año = (Integer)yearPeriod.getValue();	
			
			//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
			miniTable.setRowCount(0);
			miniTable = new MiniTablePreparator();
			dataPane.getViewport().add(miniTable);

			KeyNamePair catg = (KeyNamePair)comboCategory.getSelectedItem();
			KeyNamePair dept = (KeyNamePair)comboDepartment.getSelectedItem();
			KeyNamePair line = (KeyNamePair)comboLine.getSelectedItem();
			KeyNamePair sect = (KeyNamePair)comboSection.getSelectedItem();
			KeyNamePair bpar = (KeyNamePair)comboBPartner.getSelectedItem();
			KeyNamePair refp = (KeyNamePair)comboReferencia.getSelectedItem();
			KeyNamePair marc = (KeyNamePair)comboMarca.getSelectedItem();
			String sql ="select * from m_product " +
					"where xx_vmr_category_id=" +catg+
					"and XX_vmr_departamento_id= " +dept+
					"and xx_vmr_line_id= " +line+
					"and XX_vmr_section_id=" +sect+
					"and xx_VMRbrand_id= " +marc+
					"and XX_VMR_VENDORPRODREF_ID="+refp;
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
				//Cerrar los statements
				if (rs != null)
					try {
						rs.close();
						rs = null;
					} catch (SQLException e) {}
				
				if (ps != null)
					try {
						ps.close();
						ps = null;
					} catch (SQLException e) {}		
			}
			
			//Calcular el indicador
			final int SellThroughPieces  = indiceTabla;
			final int SellThroughAmount = indiceTabla + 1;
			final int PerformancePieces = indiceTabla + 2;
			final int PerformanceAmount = indiceTabla + 3;		
			final int SalesPieces = indiceTabla + 4;
			final int SalesAmount = indiceTabla + 5;		
			final int InventoryPieces = indiceTabla + 6;
			final int InventoryAmount = indiceTabla + 7;
			final int AditionPieces = indiceTabla + 8;
			final int AditionAmount = indiceTabla + 9;		
			for (int fila = 0; fila < miniTable.getRowCount(); fila++) {
				configurarIndicadorVentas(fila, SalesPieces, InventoryPieces, SellThroughPieces);
				configurarIndicadorVentas(fila, SalesAmount, InventoryAmount, SellThroughAmount);
				configurarIndicadorDesempeño(fila, SalesPieces, InventoryPieces, AditionPieces, PerformancePieces);
				configurarIndicadorDesempeño(fila, SalesAmount, InventoryAmount, AditionAmount, PerformanceAmount);
			}		
		}
		
		/** Configurar el indicador de ventas*/
		private void configurarIndicadorVentas (int fila, int dividendoInd, int divisorInd, int resultadoInd) {
			
			Double dividendo =(Double)miniTable.getValueAt(fila, dividendoInd);
			Double divisor = (Double)miniTable.getValueAt(fila, divisorInd);
			if (dividendo == null)
				dividendo = 0.0;
			if (divisor == null)
				divisor = 0.0;
			if (divisor == 0.0) 
				miniTable.setValueAt(new Double(0.0), fila, resultadoInd);
			else
				miniTable.setValueAt(new BigDecimal(dividendo/divisor).multiply(Env.ONEHUNDRED).setScale(2, RoundingMode.HALF_EVEN).doubleValue(), fila, resultadoInd);
		}
		
		/** Configurar el indicador de desempeño */
		private void configurarIndicadorDesempeño (int fila, int dividendoInd, int divisorIndA, int divisorIndB, int resultadoInd) {
			
			Double dividendo =(Double)miniTable.getValueAt(fila, dividendoInd);
			Double divisorA = (Double)miniTable.getValueAt(fila, divisorIndA);
			Double divisorB = (Double)miniTable.getValueAt(fila, divisorIndB);
			if (dividendo == null)
				dividendo = 0.0;
			if (divisorA == null)
				divisorA = 0.0;
			if (divisorB == null)
				divisorB = 0.0;
			if ((divisorA + divisorB)  == 0.0) 
				miniTable.setValueAt(new Double(0.0), fila, resultadoInd);
			else
				miniTable.setValueAt(new BigDecimal(dividendo/(divisorA + divisorB)).multiply(Env.ONEHUNDRED).
						setScale(2, RoundingMode.HALF_EVEN).doubleValue(), fila, resultadoInd);
		}

		//Cabeceras de las columnas de la tabla 
		private ColumnInfo colCatg = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Category_ID"), 
				"\nct.Value||'-'||ct.Name", KeyNamePair.class, true, false, "\nct.XX_VMR_Category_ID");
		private ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Department_ID"),
				"\ndp.value||'-'||dp.Name", KeyNamePair.class, true, false, "\ndp.XX_VMR_DEPARTMENT_ID");
		private ColumnInfo colLine = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Line_ID"),
				"\nli.VALUE||'-'||li.NAME", KeyNamePair.class, true, false, "\nli.XX_VMR_LINE_ID");
		private ColumnInfo colSect = new ColumnInfo(Msg.translate(ctx, "XX_VMR_Section_ID"), 
				"\nse.VALUE||'-'||se.NAME", KeyNamePair.class, true, false, "\nse.XX_VMR_SECTION_ID");
		private ColumnInfo colBPar = new ColumnInfo(Msg.translate(ctx, "C_BPARTNER_ID"), 
				"\nbp.NAME", KeyNamePair.class, true, false, "\nbp.C_BPARTNER_ID");
		private ColumnInfo colSto = new ColumnInfo(Msg.translate(ctx, "XX_Store"),
				"\nwa.VALUE||'-'||wa.NAME", KeyNamePair.class, true, false, "\nuni.M_WAREHOUSE_ID");
		




}
