package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.text.LabelView;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MRole;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MProduct;
import compiere.model.cds.MiniTablePreparator;
import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_XX_VMA_Brochure;
import compiere.model.dynamic.X_XX_VMA_BrochurePage;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VME_Elements;
import compiere.model.dynamic.X_XX_VME_Reference;

public class XX_VME_AddReferenceNew extends XX_ProductFilter{

	private static final long serialVersionUID = 1L;
	/**	Window No */
	private int m_WindowNo = 0;
	
	// Botones
	private JButton bExit = ConfirmPanel.createCancelButton(true);
	private CButton bCalculate = new CButton(Msg.translate(Env.getCtx(), "XX_Calculate"));
	
	// La tabla donde se guardarán los datos
	protected MiniTablePreparator miniTable2 = new MiniTablePreparator();
	
	// Columnas
	private ColumnInfo[] columns2 = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_VendorProdRef_ID"),".", KeyNamePair.class, "."), 
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Category_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Department_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Line_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_VMR_Section_ID"),".", KeyNamePair.class, "."),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Manual"),".", Boolean.class, false, true, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Quantity"),".", BigDecimal.class, false, true, ""),
			new ColumnInfo(Msg.translate(Env.getCtx(), "XX_Mantain"),".", Boolean.class, false, true, ""),
	};
	
	// Elemento
	private X_XX_VME_Elements elemento = new X_XX_VME_Elements(ctx, FormID, null);
	private BigDecimal cantidad = elemento.getXX_VME_QTYPUBLISHED();
	private BigDecimal precio = elemento.getXX_VME_DynamicPrice();
	private BigDecimal newRefs = new BigDecimal(0);
	private Vector<BigDecimal> refQty = XX_VME_GeneralFunctions.obtainQtyReference(elemento, true); // o false
	private Vector<BigDecimal> quantities = new Vector<BigDecimal>();
	private Vector<BigDecimal> availableQty = XX_VME_GeneralFunctions.getQtyAvailable(elemento);
	private int count = 0;
	
	// Etiquetas de cantidad y precio en elemento
	private CLabel elemQty = new CLabel(Msg.translate(Env.getCtx(), "XX_Quantity"));
	private CLabel elemPrice = new CLabel(Msg.translate(Env.getCtx(), "XX_Price"));
	private CLabel disponQty = new CLabel(Msg.translate(Env.getCtx(), "XX_Available"));
	private CTextField Qty = new CTextField(cantidad.toString());
	private CTextField Price = new CTextField(precio.toString());
	private CTextField disponible = new CTextField(availableQty.get(0).toString());
	
	// Seleccionar Manual todos
	protected CLabel labelManualAll = new CLabel("Seleccionar Manual Todos");
	protected CCheckBox checkManualAll = new CCheckBox();
	
	// Folleto, pagina y accion
	private X_XX_VMA_BrochurePage page = new X_XX_VMA_BrochurePage(ctx, elemento.getXX_VMA_BrochurePage_ID(), null);
	private X_XX_VMA_Brochure brochure = new X_XX_VMA_Brochure(ctx, page.getXX_VMA_Brochure_ID(), null);
	private int actionID = XX_VME_GeneralFunctions.obtainAM(brochure.get_ID()); 
	private X_XX_VMA_MarketingActivity action = new X_XX_VMA_MarketingActivity(ctx, actionID, null);
	private boolean allManual = false;
	
	/** removeParameter
	 * Eliminar las etiquetas y combos de la forma madre
	**/
	protected void removeParameter(){
		parameterPanel.remove(labelCategory);
		parameterPanel.remove(comboCategory);
		parameterPanel.remove(labelDepartment);
		parameterPanel.remove(comboDepartment);
		parameterPanel.remove(checkDepartment);
		parameterPanel.remove(labelLine);
		parameterPanel.remove(comboLine);
		parameterPanel.remove(checkLine);
		parameterPanel.remove(labelSection);
		parameterPanel.remove(comboSection);
		parameterPanel.remove(checkSection);
		parameterPanel.remove(labelBrand);
		parameterPanel.remove(comboBrand);
		parameterPanel.remove(checkBrand);
		parameterPanel.remove(labelBPartner);
		parameterPanel.remove(comboBPartner);
		parameterPanel.remove(checkBPartner);
		parameterPanel.remove(labelReference);
		parameterPanel.remove(comboReference);
		parameterPanel.remove(labelCodRef);
		parameterPanel.remove(textReference);
		parameterPanel.remove(checkReference);
		parameterPanel.remove(labelProduct);
		parameterPanel.remove(lookupProduct);
		parameterPanel.remove(checkProduct);
		parameterPanel.remove(labelSelectAll);
		parameterPanel.remove(checkSelectAll);
		parameterPanel.remove(bGenerate);
		parameterPanel.remove(bSearch);
		parameterPanel.remove(bReset);
		parameterPanel.remove(bCancel);
		parameterPanel.remove(labelResults);
		parameterPanel.remove(textResults);
		parameterPanel.remove(fromDate);
		parameterPanel.remove(toDate);
		parameterPanel.remove(fromLabel);
		parameterPanel.remove(toLabel);
	} 	// Fin removeParameter
	
	
	/** addParameter
	 * Agregar etiquetas de precio y cantidad del elemento al que se agregarán
	 * las referencias
	 * */
	protected void addParameter(){
		parameterPanel.add(elemPrice,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(Price,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(elemQty,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(Qty,  new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(disponQty,  new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(disponible,  new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bExit,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bCalculate,  new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// Manual todos
		parameterPanel.add(labelManualAll, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkManualAll, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
	} // Fin addParameter
	
	/** processSelection
	 *  Procesa la seleccion del usuario
	 */
	@Override
	public void processSelection(){
		super.processSelection();
		
		if(refSelected != 0){
			// Dejar de mostrar la forma anterior momentaneamente
			m_frame.setVisible(false);
			// Sí la búsqueda fue por producto
			if(product){
				getReferences(product);
			} // if prod
			else {
				dataPane.getViewport().remove(miniTable);
				
				// Remover parámetros anteriores y agregar nuevos
				removeParameter();
				addParameter();	
				bExit.addActionListener(this);
				bCalculate.addActionListener(this);
				dataPane.setPreferredSize(new Dimension(1280, 300));
				
				// Se ocultan los botones de la forma madre que no se requieren
				bGenerate.setVisible(false);
				bCancel.setVisible(false);
				bSearch.setVisible(false);
				bCalculate.setVisible(true);
				bExit.setVisible(true);
				labelSelectAll.setVisible(false);
				checkSelectAll.setVisible(false);
				
				// Precio del elemento
				Qty.setEditable(false);
				Qty.setBackground(Color.orange);
				Qty.setPreferredSize(new Dimension(80, 25));
				Qty.setHorizontalAlignment(JTextField.CENTER);
				
				// Cantidad del elemento
				Price.setEditable(false);
				Price.setBackground(Color.orange);
				Price.setPreferredSize(new Dimension(80, 25));
				Price.setHorizontalAlignment(JTextField.CENTER);
				
				// Cantidad disponible para ser asignada
				disponible.setEditable(false);
				disponible.setBackground(Color.orange);
				disponible.setPreferredSize(new Dimension(80, 25));
				disponible.setHorizontalAlignment(JTextField.CENTER);
				
				// Botones
				commandPanel.add(bExit, null);
				commandPanel.add(bCalculate, null);
				bExit.setEnabled(true);
				bExit.addActionListener(this);
				bCalculate.setEnabled(true);
				bCalculate.addActionListener(this);
				
				// Seleccionar manual todos
				checkManualAll.setEnabled(true);
				checkManualAll.setValue(false);
				checkManualAll.addActionListener(this);
				
				// Se procede a llenar la tabla de cantidades
				fillSecondTable();	
			
				// Se muestra nuevamente la tabla
				m_frame.setVisible(true);
				repaint();
			
			} // else referencia
		}
	} // Fin processSelection

	/** fillSecondTable
	 * Se encarga de llenar la tabla con las cantidades según el elemento
	 * **/
	protected void fillSecondTable(){		
		m_frame.setVisible(true);
		miniTable2.setRowCount(0);
		miniTable2 = new MiniTablePreparator();
		miniTable2.setRowHeight(miniTable2.getRowHeight() + 2 );
		dataPane.getViewport().add(miniTable2, null);
		miniTable2.prepareTable(columns2, null, null, true, null);
		
		////
		miniTable2.getSelectionModel().addListSelectionListener(this);
		miniTable2.getModel().addTableModelListener(this);
		////
		
		// Preparar el columninfo
		int rows = miniTable.getRowCount();
		int rows2 = miniTable2.getRowCount();
		
		// Se define el width de las columnas en la tabla de cantidades
		miniTable2.getColumnModel().getColumn(0).setPreferredWidth(200); // Referencia
		miniTable2.getColumnModel().getColumn(1).setPreferredWidth(60); // Categoria
		miniTable2.getColumnModel().getColumn(2).setPreferredWidth(200); // Departamento
		miniTable2.getColumnModel().getColumn(3).setPreferredWidth(200); // Linea
		miniTable2.getColumnModel().getColumn(4).setPreferredWidth(200); // Seccion
		miniTable2.getColumnModel().getColumn(5).setPreferredWidth(60);  // Manual
		miniTable2.getColumnModel().getColumn(6).setPreferredWidth(60);  // Cantidad
		miniTable2.getColumnModel().getColumn(7).setPreferredWidth(60);  // Mantener
		miniTable2.getTableHeader().setReorderingAllowed(false);
		miniTable2.setRowSelectionAllowed(true);
		
		// Se toman los tamaños de los vectores resultados para completar info
		int depTam = results.Department.size();
		int linTam = results.Line.size();
		int secTam = results.Section.size();
		int refTam = results.Reference.size();
		int brandTam = results.Brand.size();
		int venTam = results.Vendor.size();
		int partTam = results.Partner.size();
		int prodTam = results.Product.size();
		
		/** El grano del resultado contempla la selección de categoria, 
		 * departamento, línea, sección y referencia **/

		if(depTam > 0 && linTam > 0 && secTam > 0 && venTam > 0){
			// Se recorre la tabla de resultados para inicializar la tabla de cantidades
			for (int i = 0; i < rows; i++) {
				IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
				
				// Se verifica si la fila fue seleccionada por el usuario
				if (id.isSelected()){
					miniTable2.setRowCount(rows2 + 1);
					
					// Se recorren las columnas para agregarla a la tabla 
					// de cantidades en la columna correspondiente
					// Indices: Cat , Dep, Lin 2, Sec 3, Ref 4,  
					for(int j = 1; j < miniTable.getColumnCount(); j++){
						if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Category_ID"))){
							miniTable2.getModel().setValueAt(((KeyNamePair)miniTable.getModel().getValueAt(i, j)), rows2, 1);
							continue;
						}
						if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Department_ID"))){
							miniTable2.getModel().setValueAt(((KeyNamePair)miniTable.getModel().getValueAt(i, j)), rows2, 2);
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Line_ID"))){
							miniTable2.getModel().setValueAt(((KeyNamePair)miniTable.getModel().getValueAt(i, j)), rows2, 3);
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_Section_ID"))){
							miniTable2.getModel().setValueAt(((KeyNamePair)miniTable.getModel().getValueAt(i, j)), rows2, 4);
							continue;
						}
						else if(miniTable.getModel().getColumnName(j).equals(Msg.translate(ctx, "XX_VMR_vendorProdRef_ID"))){
							miniTable2.getModel().setValueAt(((KeyNamePair)miniTable.getModel().getValueAt(i, j)), rows2, 0);
							continue;
						}
					} // for
					miniTable2.getModel().setValueAt(false, rows2, 5); // Manual
					miniTable2.getModel().setValueAt(true, rows2, 7); // Mantener
					rows2++;
				} // if selected
			} // for	
			
		} // if cat-dep-lin-sec-ref
		
		/** El grano del resultado no contempla la información completa, se obtiene
		 * a partir de las columnas seleccionadas por el usuario al realizar la 
		 * búsqueda **/
		else {
			// Strings para formar el SQL que se arma dinámicamente de acuerdo
			// a lo seleccionado por el usuario en la búsqueda
			String SQLTotal = "";
			String select = " SELECT VR.VALUE||'-'||VR.NAME, VR.XX_VMR_VENDORPRODREF_ID, " +
						" CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID, " +
						" DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID, " +
						" li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, " +
						" se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID ";
			String from = " FROM  XX_VMR_VENDORPRODREF VR  INNER JOIN XX_VMR_DEPARTMENT DP " +
						" ON (DP.XX_VMR_DEPARTMENT_ID = VR.XX_VMR_DEPARTMENT_ID) " +
						" INNER JOIN XX_VMR_CATEGORY CAT  " +
						" ON (CAT.XX_VMR_CATEGORY_ID = DP.XX_VMR_CATEGORY_ID)" +
						" INNER JOIN XX_VMR_LINE li  " +
						" ON (LI.XX_VMR_LINE_ID = VR.XX_VMR_LINE_ID) " +
						" INNER JOIN XX_VMR_SECTION SE  " +
						"ON (SE.XX_VMR_SECTION_ID = VR.XX_VMR_SECTION_ID) ";
			String where = " WHERE  VR.IsActive = 'Y' AND DP.ISACTIVE = 'Y' " +
						" AND LI.IsActive = 'Y' AND SE.IsActive = 'Y' ";
			String groupBy = " GROUP BY  VR.VALUE||'-'||VR.Name, VR.XX_VMR_VENDORPRODREF_ID, " +
						" CAT.VALUE||'-'||CAT.NAME, CAT.XX_VMR_CATEGORY_ID," +
						" DP.VALUE||'-'||DP.NAME, DP.XX_VMR_DEPARTMENT_ID, " +
						" li.VALUE||'-'||li.NAME, li.XX_VMR_LINE_ID, " +
						" se.VALUE||'-'||se.NAME, se.XX_VMR_SECTION_ID   ";
			String orderBy = " ORDER BY  VR.XX_VMR_VENDORPRODREF_ID ";
			
			String depts = "";
			
			// Selecionado departamento
			if(depTam > 0){
				for(int d = 0; d < depTam; d++){
					depts += ((KeyNamePair)results.Department.get(d)).getID();
					depts += ", ";
				} // for depts
				if(depts.length()> 0 ) {
					depts = depts.substring(0, (depts.length())-2);
				}

				// Seleccionados departamentos pero no líneas, se obtienen todas las
				// referencias asociadas al departamento
				if(depTam > 0 && linTam == 0 && venTam == 0){
					where += " AND VR.XX_VMR_DEPARTMENT_ID IN (" + depts + ") ";
				} //dep-lin
				
				// Seleccionados departamentos y líneas pero no secciones, se obtienen todas las
				// referencias asociadas
				if(depTam > 0 && linTam > 0  && secTam == 0 && venTam == 0){
					String lines = "";
					for(int d = 0; d < linTam; d++){
						lines += ((KeyNamePair)results.Line.get(d)).getID();
						lines += ", ";
					} // for depts
					if(lines.length()> 0 ) {
						lines = lines.substring(0, lines.length()-2);
					}
					where +=  " AND VR.XX_VMR_LINE_ID IN (" + lines + ") ";
				} // dep-lin-sec
			} // dep
			
			// Adicionalmente el usuario seleccionó alguna marca
			if(brandTam > 0){
				String brands = "";
				for(int d = 0; d < brandTam; d++){
					brands += ((KeyNamePair)results.Brand.get(d)).getID();
					brands += ", ";
				} // for brands
				if(brands.length()> 0 ) {
					brands = brands.substring(0, brands.length()-2);
				}
				from += " INNER JOIN XX_VMR_BRAND B " +
						" ON (VR.XX_VMR_BRAND_ID = B.XX_VMR_BRAND_ID) ";
				where += " AND B.ISACTIVE = 'Y' AND VR.XX_VMR_BRAND_ID IN (" + brands + ")";
			} // brand

			// Adicionalmente el usuario seleccionó algún socio
			if(partTam > 0){
				String bps = "";
				for(int d = 0; d < partTam; d++){
					bps += ((KeyNamePair)results.Partner.get(d)).getID();
					bps += ", ";
				} // for brands
				if(bps.length()> 0 ) {
					bps = bps.substring(0, bps.length()-2);
				}
				from += " INNER JOIN C_BPARTNER BP " +
						" ON (VR.C_BPARTNER_ID = BP.C_BPARTNER_ID) ";
				where += " AND BP.ISACTIVE = 'Y' AND BP.C_BPARTNER_ID IN (" + bps + ")";
			} // socios
			
			// Adicionalmente el usuario seleccionó alguna referencia
			if(venTam > 0){
				String vendors = "";
				for(int d = 0; d < venTam; d++){
					vendors += ((KeyNamePair)results.Vendor.get(d)).getID();
					vendors += ", ";
				} // for brands
				if(vendors.length()> 0 ) {
					vendors = vendors.substring(0, vendors.length()-2);
				}
				where += " AND VR.XX_VMR_VENDORPRODREF_ID IN (" + vendors + ")";
			} // proveedores
			
			// Se arma el sql total
			SQLTotal += select + from + where + groupBy + orderBy;
			//System.out.println("SQLTotal"+SQLTotal);
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(SQLTotal, null);
				rs = pstmt.executeQuery();
				while(rs.next()){
					miniTable2.setRowCount(rows2 + 1);
					miniTable2.getModel().setValueAt(new KeyNamePair(rs.getInt(4), rs.getString(3)), rows2, 1);
					miniTable2.getModel().setValueAt(new KeyNamePair(rs.getInt(6), rs.getString(5)), rows2, 2);
					miniTable2.getModel().setValueAt(new KeyNamePair(rs.getInt(8), rs.getString(7)), rows2, 3);
					miniTable2.getModel().setValueAt(new KeyNamePair(rs.getInt(10), rs.getString(9)), rows2, 4);
					miniTable2.getModel().setValueAt(new KeyNamePair(rs.getInt(2), rs.getString(1)), rows2, 0);
					miniTable2.getModel().setValueAt(false, rows2, 5); // Manual
					miniTable2.getModel().setValueAt(true, rows2, 7); // Mantener
					rows2++;
				}
			} // try
			catch (SQLException e) { e.printStackTrace(); }
			finally {
				try { rs.close(); } 
				catch (SQLException e) { e.printStackTrace(); }
				try { pstmt.close(); } 
				catch (SQLException e) { e.printStackTrace(); }
			} // finally
		} // else cat-dep-lin-sec-ref
		
		// Se colocan las cantidades para las referencias nuevas, teniendo en
		// cuentas las previamente agragadas
		newRefs = new BigDecimal(miniTable2.getRowCount());
		int filas = miniTable2.getRowCount();
	
		// Calculando las cantidades 
		quantities = XX_VME_GeneralFunctions.calculateQuantities(elemento, 0, "S", newRefs);
		
		// Agregando en la columna cantidad
		for (int fila = 0; fila < filas; fila++) {
			// Si hay resto se coloca en la primera fila
			if(!quantities.get(1).equals(new BigDecimal(0)) && fila == 0) {
				miniTable2.getModel().setValueAt(quantities.get(1), fila, 6);
			} // primera fila
			else {
				miniTable2.getModel().setValueAt(quantities.get(0), fila, 6);
			}	
		} // filas
	
		if(availableQty.get(0).equals(new BigDecimal(0))){
			bCalculate.setEnabled(false);
		}
	} // fillSecondTable
	
	
	/** getReferences
	 * Se obtienen las referencias y productos a ser agregados al elemento
	 * dependiendo del nivel de búsqueda del usuario: por producto o referencia
	 * @param product Determina si la referencia se crea por producto
	 * */
	public void getReferences(boolean product){
		count++;
		int products = results.Product.size();
		int filas2 = miniTable2.getRowCount();
		int reference = 0;
		Vector<BigDecimal> cantidades = new Vector<BigDecimal>();
		Vector<Integer> referencias = new Vector<Integer>();
		Vector<Integer> productos = new Vector<Integer>();
		Vector<Boolean> manual = new Vector<Boolean>();
		Vector<Boolean> mantener = new Vector<Boolean>();
		Vector<Boolean> manualProd = new Vector<Boolean>();
//		Vector<Integer> categories = new Vector<Integer>();
		Vector<Integer> order = new Vector<Integer>();
		Vector<Integer> pedido = new Vector<Integer>();
		m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//		Date inicio = new Date();
		// La búsqueda refleja resultados a nivel de producto
		if(product){
			for (int fila = 0; fila < products; fila++) {
				// Se crea el producto para obtener la referencia
				MProduct prod = new MProduct(Env.getCtx(), (results.Product.get(fila)).getKey(), null);

				// Se verifica si la referencia ya existe en el elemento
//				reference = XX_VME_GeneralFunctions.obtainReference(
//						prod.getXX_VMR_VendorProdRef_ID(), elemento);
				
				// La referencia a la que se refiere el producto no se encuentra
				// asociada al elemento
//				if(reference == 0) {
					// Se obtiene la cantidad a ser asociada a la referencia
					// modificar para que calcule la cantidad en el caso de producto
					quantities = XX_VME_GeneralFunctions.calculateQuantities(elemento, 
									reference, "P", new BigDecimal(1));
					
					if(!referencias.contains(prod.getXX_VMR_VendorProdRef_ID())) {
						referencias.add(prod.getXX_VMR_VendorProdRef_ID());						
					}
					else {
						referencias.add(0);
					}
					cantidades.add(quantities.get(0));
//					categories.add(prod.getXX_VMR_Category_ID());
					manual.add(false);
					mantener.add(false);
					manualProd.add(false);
					productos.add(prod.get_ID());
					order.add(0);
					pedido.add(0);
					
//				} // else referenceID
			} // for products
			//TODO mejorar la obtencion de las cats, y refs
			XX_VME_GeneralFunctions.createElemRefNew(elemento, action, cantidades,	
					referencias, "P", manual, mantener, productos, manualProd,
					order, pedido);	
		} // productos
		
		// La búsqueda refleja resultados a nivel de referencia
		else {
			// Se agregan las referencias y los productos asociados a las mismas
			for (int fila = 0; fila < filas2; fila++) {
				reference = XX_VME_GeneralFunctions.
						obtainReference(((KeyNamePair)miniTable2.
								getModel().getValueAt(fila, 0)).getKey(), elemento);
				
				// La referencia no existe, se crea
				if(reference == 0){
					referencias.add(((KeyNamePair)miniTable2.getModel().getValueAt(fila, 0)).getKey());
					cantidades.add((BigDecimal)miniTable2.getModel().getValueAt(fila, 6));
					manual.add((Boolean)miniTable2.getModel().getValueAt(fila, 5));
					mantener.add((Boolean)miniTable2.getModel().getValueAt(fila, 7));
					manualProd.add(false);
//					categories.add(((KeyNamePair)miniTable2.getModel().getValueAt(fila, 1)).getKey());
					productos.add(0);
					order.add(0);
					pedido.add(0);
				
				}
				// La referencia ya existía, se actualiza la cantidad indepabis
				else {
					// La referencia existe, se actualiza la cantidad indepabis
					X_XX_VME_Reference ref = new X_XX_VME_Reference(Env.getCtx(), reference, null);
					if((Boolean)miniTable2.getModel().getValueAt(fila, 5)){
						ref.setXX_VME_Manual(true);
					}
					ref.setXX_VME_IndepabisQty((BigDecimal)miniTable2.getModel().getValueAt(fila, 6));

					XX_VME_GeneralFunctions.redefineQtyProd(reference,
							(BigDecimal)miniTable2.getModel().getValueAt(fila, 6),
							(Boolean)miniTable2.getModel().getValueAt(fila, 5));
					ref.save();
				} // else ref ==0
			}// for filas
			XX_VME_GeneralFunctions.createElemRefNew(elemento, action, cantidades,	
					referencias,"R", manual, mantener, productos, manualProd,order, pedido);	
		} // else referencias
		
		
		//Obteniendo cantidad de referencias
		String sql = "SELECT COUNT(*) as contador FROM XX_VME_REFERENCE WHERE XX_VME_ELEMENTS_ID="+elemento.get_ID();
		int contadorReferencias = 0;
		ResultSet rs=null;
		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				contadorReferencias = rs.getInt("contador");
			}
			rs.close();
			pstmt.close();
		}		
		catch (SQLException e) 	{
			log.log(Level.SEVERE, sql, e);
		} finally {
			try{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}catch(Exception e){}
		}
		
		// Se setea la cantidad de referencias asociadas al elemento
		//elemento.setXX_VME_QtyRefAssociated(elemento.getXX_VME_QtyRefAssociated().add(new BigDecimal(referencias.size())));
		elemento.setXX_VME_QtyRefAssociated(new BigDecimal(contadorReferencias));
		
		// Configurar la relacion de marca con elemento
		XX_VME_GeneralFunctions.setElemBrand(FormID);
		
		elemento.setXX_VME_Validated(false);
		elemento.save();
		
		// Se redefinen las cantidades de las referencias asociadas al elemento
		//X_XX_VME_Elements element = new X_XX_VME_Elements(Env.getCtx(), elemento, null);
//		Vector<BigDecimal> available = XX_VME_GeneralFunctions.getQtyAvailable(elemento);
//		BigDecimal asociadas = elemento.getXX_VME_QtyRefAssociated(); 
//		BigDecimal resta = asociadas.subtract(available.get(1));

		// Si hay referencias no manuales se procede a distribuir, de lo contrario
		// las cantidades se quedan como el usuario las definió
//		if(!resta.equals(new BigDecimal(1))){
			XX_VME_GeneralFunctions.redefineQuantities(elemento);
//		}
//		Date fin = new Date();			
//		System.out.println("Inicio: "+inicio+" Fin: "+fin);
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		// Ventana de salida
		String msj = Msg.getMsg(Env.getCtx(), "XX_AddReference");
		ADialog.info(m_WindowNo, m_frame, msj);
		dispose();

	} // Fin getReferences
	
	/**
	 *  ActionListener
	 *  @param e event
	 */
	@Override
	public void actionPerformed (ActionEvent e) {
		//Ejecutará las acciones por defecto
		super.actionPerformed(e);
		desactivarActionListeners();
		// Boton de cancelar
		if (e.getSource() == bExit) {
			dispose();
		} // bCancel

		//Boton de procesar
		else if(e.getSource() == bCalculate) {
			try {
				if(count == 0){
					getReferences(product);
				}
			} 
			catch (NullPointerException n) {
				n.printStackTrace();
			}
		} // bSearch
		
		// Seleccionar todos los elementos de la tabla
		else if(e.getSource() == checkManualAll) {
			if((Boolean)checkManualAll.getValue()){
				manualAll(true);
				allManual = true;
				disponible.setValue(0);
				disponible.setBackground(Color.GREEN);
				bCalculate.setEnabled(true);
			}
			else {
				manualAll(false);
			    disponible.setValue(availableQty.get(0).toString());
				disponible.setBackground(Color.ORANGE);
				bCalculate.setEnabled(true);
			}
		} // checkManualAll
		activarActionListeners();
	}   //  actionPerformed
	
	/** manualAll
	 * Modificar manualmente todos los elementos de la tabla 
	 * @param selected Determina si se seleccionan o no todas las referencias 
	 * */
	protected void manualAll(Boolean selected) {
		if(selected){
			for(int j = 0; j < miniTable2.getRowCount(); j++){
				miniTable2.setValueAt(selected, j, 5);	
			}
		}// if selected
		else if(!selected){
			for(int j = 0; j < miniTable2.getRowCount(); j++){
				miniTable2.setValueAt(selected, j, 5);	
			}
		}// else selected
	} // mantainAll
	
	/**
	*  Table Model Listener - calculate matched Qty
	*  @param e event
	*/
	@Override
	public void tableChanged (TableModelEvent e){
		BigDecimal suma = new BigDecimal(0);
		BigDecimal resta = new BigDecimal(0);
		int countManual = 0;
		
		// Se toman los cambios que ocurren en la tabla por parte del usuario
		// referente a las cantidades
		if(miniTable2.isEditing()){
				if(e.getColumn() == 6){
					// Se coloca manual como True
					miniTable2.setValueAt(true, e.getFirstRow(), 5);
				} // if column = 6
				countManual = 0;
				// Se recorren las filas para obtener las cantidades
				for (int i = 0; i < miniTable2.getRowCount(); i++) {
					if ( miniTable2.getValueAt(i, 6) != null && 
							((Boolean)miniTable2.getModel().getValueAt(i, 5) == true)){
						suma = suma.add((BigDecimal)miniTable2.getValueAt(i, 6));
						countManual++;
					}
				} // for suma

				resta = availableQty.get(0).subtract(suma);
				// Si las nuevas cantidades se corresponden con la del elemento, se
				// habilita el boton de calcular, de lo contrario se deshabilita
				if (resta.compareTo(new BigDecimal(0)) == 0 ){
					disponible.setValue(0);
					disponible.setBackground(Color.GREEN);
					bCalculate.setEnabled(true);

				}//If Suma
				else if(resta.compareTo(new BigDecimal(0)) < 0){
					disponible.setValue(resta);
					disponible.setBackground(Color.RED);
					bCalculate.setEnabled(false);
				}
				else {
					disponible.setValue(resta);
					disponible.setBackground(Color.GREEN);
					bCalculate.setEnabled(true);
				}

		}// if editing
	}   //  tableChanged
	
	
} // Fin XX_VME_AddReference
