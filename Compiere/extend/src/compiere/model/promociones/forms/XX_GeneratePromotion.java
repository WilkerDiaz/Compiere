package compiere.model.promociones.forms;


import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import org.compiere.apps.ADialog;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import compiere.model.cds.forms.XX_ProductFilter;
import compiere.model.promociones.MVMRConditionPromotion;
import compiere.model.promociones.X_Ref_XX_TypeSelection;

public class XX_GeneratePromotion extends XX_ProductFilter{

	private static final long serialVersionUID = 1L;
	private static final String abrDepartamento = "DEP";
	static final int posicionCategoria = 10;
	static final int posicionDepartamento = 11;
	static final int posicionLinea = 12;
	static final int posicionSeccion = 13;
	static final int posicionReferencia = 14;
	static final int posicionProducto = 15;
	
	String columnasInsertObligatoria = "AD_Client_ID, AD_Org_ID, CreatedBy, UpdatedBy, XX_VMR_Promotion_ID,XX_VMR_DetailPromotionext_ID, XX_VMR_PromoConditionValue_ID ";
	Integer promocion = 0;
	
	/**	Window No */
	
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
	} 	// Fin removeParameter
	
	
	
	/** processSelection
	 *  Procesa la seleccion del usuario
	 */
	@Override
	public void processSelection(){
		super.processSelection();
		
		///////////////////////////////////////////////////////////////////
		// Se toman los tamaños de los vectores resultados para completar info
		//int rows = miniTable.getRowCount();
		int depTam = results.Department.size();
		int linTam = results.Line.size();
		int secTam = results.Section.size();
		int brandTam = results.Brand.size();
		int venTam = results.Vendor.size();  //Tamaño de Referencia del proveedor
		int partTam = results.Partner.size(); // Tamaño de Proveedor
		int prodTam = results.Product.size(); // Tamaño del producto
		ArrayList<String> listaItemSelec = new ArrayList<String>(); // Cuando la Seleccion del usuario sea > 1000 entonces realizara el ciclo de Insercion n veces

	// Strings para formar el SQL que se arma dinámicamente de acuerdo
	// a lo seleccionado por el usuario en la búsqueda...Parametros
		promocion = Integer.parseInt(Env.getCtx().getContext("#XX_VMR_Promotion_ID"));
		//Integer tiendaID = Integer.parseInt(Env.getCtx().getContext("#M_WareHouse_ID")); // Tienda, tengo que seleccionarla la que se encuentra en la cabecera. Por contexto
		//String tienda = new X_M_Warehouse(ctx, tiendaID, null).getValue();
		//Último ID de la Tabla Detalle Promoción
		
		Integer tipoPromocion = Integer.parseInt(Env.getCtx().getContext("#XX_TypePromotion"));
			// Columnas Obligatorias para Compiere, mas los ID de la Promocion y los generados por los detalles		
		
		// Buscamos las columnas en la tabla de Condicion Promocion para armar el Insert de Select
		
		ArrayList<MVMRConditionPromotion> columnasSelec = null;
		String from = " FROM ";
		int posicion = 0;
		//String orderBy = " ORDER BY  VR.XX_VMR_VENDORPRODREF_ID ";
		
	
	//Si en el Filtro se seleccionó hasta Producto
	String producto = "";
	if(prodTam > 0)
	{
		  from += " M_Product PRO INNER JOIN XX_VMR_Department DEP ON (DEP.XX_VMR_Department_ID = PRO.XX_VMR_Department_ID)";
		  //Posicion, Representa a la posicion de la tabla de condicion promocion la cual buscara sus columnas y armar el Insert de Select
		  String abrProduct = "PRO";
		  posicion = posicionProducto;
		  columnasSelec = seleccionarColumnas(tipoPromocion, posicion);
		  ArrayList<String> InsertSelect =  construirInsertSelect(columnasSelec, abrProduct);
		  //select += columnasSelectObligatoria() + InsertSelect.get(0);
		 // insert = columnasInsertObligatoria + InsertSelect.get(1);
		  
		  
		for(int d = 0; d < prodTam; d++){
			producto += ((KeyNamePair)results.Product.get(d)).getID();
			producto += ", ";
			if (d%990 == 0 && d>0)
			{
				listaItemSelec.add(producto);
				producto = "";
			}
		}
		
		listaItemSelec.add(producto);
		insertarDetalleVarios(listaItemSelec, InsertSelect, abrProduct, from, "M_Product");
		/*for (int i=0; i<listaItemSelec.size(); i++)
		{
			select = " SELECT ";
			select += columnasSelectObligatoria() + InsertSelect.get(0);
			where = " WHERE ";
			producto = listaItemSelec.get(i);
			if (producto.equals(""))
				continue;
			if(producto.length()> 0 ) {
				producto = producto.substring(0, (producto.length())-2);
			}
			
			where += armarWhere(abrProduct, "M_Product", producto, promocion);
			
			select += from + where;// + orderBy;
			insertarDetalle(insert, select);
		}*/
		
		
	}
	else
	//  Referencia de Proveedor 
		if(venTam > 0){
			from += " XX_VMR_VendorProdRef REF INNER JOIN XX_VMR_Department DEP ON (DEP.XX_VMR_Department_ID = REF.XX_VMR_Department_ID) ";
			String abrReferencia = "REF";
			posicion = posicionReferencia;
		    columnasSelec = seleccionarColumnas(tipoPromocion, posicion);
		    ArrayList<String> InsertSelect =  construirInsertSelect(columnasSelec, abrReferencia);
		   // insert = columnasInsertObligatoria + InsertSelect.get(1);
		    
			String referenciaProveedor = "";
			for(int d = 0; d < venTam; d++){
				referenciaProveedor += ((KeyNamePair)results.Vendor.get(d)).getID();
				referenciaProveedor += ", ";
				if (d%990 == 0 && d>0)
				{
					listaItemSelec.add(referenciaProveedor);
					referenciaProveedor = "";
				}
			} // for brands
			
			listaItemSelec.add(referenciaProveedor);
			
			insertarDetalleVarios(listaItemSelec, InsertSelect, abrReferencia, from, "XX_VMR_VendorProdRef");
			
			/*
			for (int i=0; i<listaItemSelec.size(); i++)
			{
				select = " SELECT ";
				select += columnasSelectObligatoria() + InsertSelect.get(0);
				where = " WHERE ";
				referenciaProveedor = listaItemSelec.get(i);
				if (referenciaProveedor.equals(""))
					continue;
				if(referenciaProveedor.length()> 0 ) {
					referenciaProveedor = referenciaProveedor.substring(0, referenciaProveedor.length()-2);
				}
				
				where += armarWhere(abrReferencia, "XX_VMR_VendorProdRef", referenciaProveedor, promocion);
				
				select += from + where;// + orderBy;
				insertarDetalle(insert, select);
			}*/
			
		} // Referencia del Proveedor
		else 
		// marca
			if (brandTam > 0 || partTam > 0)
			{
				ADialog.info(1, new Container(), "Por favor marque el CHECK(Todos) de referencia de proveedor, Busque nuevamente)");
			}
			else
			// Seccion
			if(secTam > 0)
			{
				from += " XX_VMR_Section SEC INNER JOIN XX_VMR_Line LIN ON (LIN.XX_VMR_Line_ID = SEC.XX_VMR_Line_ID) INNER JOIN XX_VMR_Department DEP ON (DEP.XX_VMR_Department_ID = LIN.XX_VMR_Department_ID) ";
				String abrSeccion = "SEC";
				posicion = posicionSeccion;
				
				 columnasSelec = seleccionarColumnas(tipoPromocion, posicion);
				 ArrayList<String> InsertSelect =  construirInsertSelect(columnasSelec, abrSeccion);
				 //select += columnasSelectObligatoria() + InsertSelect.get(0);
				 //insert = columnasInsertObligatoria + InsertSelect.get(1);
				 
				String seccion = "";
				for(int d = 0; d < secTam; d++){
					seccion += ((KeyNamePair)results.Section.get(d)).getID();
					seccion += ", ";
					if (d%990 == 0 && d>0)
					{
						listaItemSelec.add(seccion);
						seccion = "";
					}
				}
				
				listaItemSelec.add(seccion);
				
				insertarDetalleVarios(listaItemSelec, InsertSelect, abrSeccion, from, "XX_VMR_Section");
				
				/*
				if(seccion.length()> 0 ) {
					seccion = seccion.substring(0, (seccion.length())-2);
				}
				
				where += armarWhere(abrSeccion, "XX_VMR_Section", seccion, promocion);
			
				select += from + where;// + orderBy;
				//System.out.println("Seccion Select :"+select);
				//System.out.println("Seccion Insert:"+insert);
				int itemInsertados = insertarDetalle(insert, select);*/
			}
			else 
				// Linea
				if(linTam > 0)
				{
					from += " XX_VMR_Line LIN INNER JOIN XX_VMR_Department DEP ON (DEP.XX_VMR_Department_ID = LIN.XX_VMR_Department_ID) ";
					String abrLinea = "LIN";
					posicion = posicionLinea;
					
					columnasSelec = seleccionarColumnas(tipoPromocion, posicion);
					ArrayList<String> InsertSelect =  construirInsertSelect(columnasSelec, abrLinea);
					//select += columnasSelectObligatoria() + InsertSelect.get(0);
					//insert = columnasInsertObligatoria + InsertSelect.get(1);
					
					String linea = "";
					for(int d = 0; d < linTam; d++){
						linea += ((KeyNamePair)results.Line.get(d)).getID();
						linea += ", ";
						if (d%990 == 0 && d>0)
						{
							listaItemSelec.add(linea);
							linea = "";
						}
					}
					
					listaItemSelec.add(linea);
					
					insertarDetalleVarios(listaItemSelec, InsertSelect, abrLinea, from, "XX_VMR_Line");
					
					/*if(linea.length()> 0 ) {
						linea = linea.substring(0, (linea.length())-2);
					}
					
					where += armarWhere(abrLinea, "XX_VMR_Line", linea, promocion);
					
					select += from + where;// + orderBy;
					//System.out.println("Linea Select :"+select);
					//System.out.println("Linea Insert:"+insert);
					insertarDetalle(insert, select);*/
				}
				else 
					// Departamento
					if(depTam > 0)
					{
						from += " XX_VMR_Department DEP ";
						String abrDepartamento = "DEP";
						posicion = posicionDepartamento;
						
						columnasSelec = seleccionarColumnas(tipoPromocion, posicion);
						ArrayList<String> InsertSelect =  construirInsertSelect(columnasSelec, abrDepartamento);
						//select += columnasSelectObligatoria() + InsertSelect.get(0);
						//insert = columnasInsertObligatoria + InsertSelect.get(1);
						String departamento = "";
						
						for(int d = 0; d < depTam; d++){
							departamento += ((KeyNamePair)results.Department.get(d)).getID();
							departamento += ", ";
							if (d%990 == 0 && d>0)
							{
								listaItemSelec.add(departamento);
								departamento = "";
							}
						}
						
						listaItemSelec.add(departamento);
						
						insertarDetalleVarios(listaItemSelec, InsertSelect, abrDepartamento, from, "XX_VMR_Department");
						/*
						if(departamento.length()> 0 ) {
							departamento = departamento.substring(0, (departamento.length())-2);
						}

						where += armarWhere(abrDepartamento, "XX_VMR_Department", departamento, promocion);
						
						select += from + where;// + orderBy;
						//System.out.println("Departamento Select :"+select);
						//System.out.println("Departamento Insert:"+insert);
						insertarDetalle(insert, select);*/
						
					}

////////////////////////////////////////////////////////////////////
} // Fin processSelection

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void fillSecondTable() {
		// TODO Auto-generated method stub
		
	} // Fin XX_VME_AddReference
	
	
	//Función que busca el último código de la tabla detalle promoción,
	private Integer IdDetallePromocion()
	{
		String ultimoDetallePromocion= "select max(XX_VMR_DetailPromotionExt_ID) from XX_VMR_DetailPromotionExt";
		PreparedStatement ultDetProm = DB.prepareStatement(ultimoDetallePromocion,null);
		ResultSet maximo = null;
		Integer detallePromocion = 0;
		try {
			maximo = ultDetProm.executeQuery();
			while (maximo.next()) {
				detallePromocion = maximo.getInt(1); 
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally{
			DB.closeResultSet(maximo);
			DB.closeStatement(ultDetProm);
		}
		return detallePromocion;
	}
	

	// Relizamos el Insert en la tabla de detalle promocion dependiendo de seleccion del usuario. Obvia las que ya estan en la promocion
	private int insertarDetalle(String insert, String select)
	{
		String InsertarResultado = "Insert into XX_VMR_DetailPromotionExt("+insert+")("+select+")";
		int cuantos = 0;
		//System.out.println("Insert de Select:"+InsertarResultado);
		try {
			 cuantos = DB.executeUpdate(null, InsertarResultado);
			updateDetailPromotionSequence();
			//System.out.println("Cuantos Insertó:" + cuantos);
		}catch (Exception e) {
			ADialog.info(1, new Container(), "Error al Insertar, Comuniquese con soporte");
			return  0;
		}
		
		return  cuantos;
		
	}
	private ArrayList<MVMRConditionPromotion> seleccionarColumnas(Integer tipoPromocion, int posicion)
	{
		ResultSet rsColumnas = null;
		//nombres de las columnas dependiendo de la posicion que se pasa por parametro
		MVMRConditionPromotion claseCondicion = null;
		ArrayList<MVMRConditionPromotion> columnas = new ArrayList<MVMRConditionPromotion>();
		String sqlCondicionPromocion ="select b.columnname, a.XX_TypeSelection from XX_VMR_ConditionPromotion a " +
        				"inner join AD_Column b on (a.AD_Column_ID = b.AD_Column_ID) where XX_TypePromotion = "+tipoPromocion+" " +
        				"and XX_Position >=1 and XX_Position <= "+posicion+" order by XX_Position";
		PreparedStatement pstmt = DB.prepareStatement(sqlCondicionPromocion, null);
		try {
			rsColumnas = pstmt.executeQuery();
			while (rsColumnas.next())
			{
				claseCondicion = new MVMRConditionPromotion(Env.getCtx(), 0, null);
				claseCondicion.setColumnName(rsColumnas.getString(1));
				claseCondicion.setXX_TypeSelection(rsColumnas.getString(2));
				columnas.add(claseCondicion);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rsColumnas);
			DB.closeStatement(pstmt);
		}
		return columnas;
	}
	
	private ArrayList<String> construirInsertSelect(ArrayList<MVMRConditionPromotion> columnasSelec, String abreviatura)
	{
		MVMRConditionPromotion columnasCondiciones = null;
		ArrayList<String> insertSelect = new ArrayList<String>();
		String columnasInsert = "", columnasSelect = "";
		int aux=0;
		for (int i = 0; i<columnasSelec.size(); i++){
			  columnasCondiciones = columnasSelec.get(i);
			  columnasSelect += ", ";
			  columnasInsert += ", ";
			  if (columnasCondiciones.getXX_TypeSelection().compareTo(X_Ref_XX_TypeSelection.PARAMETER.getValue())==0)
				  columnasSelect += "'"+Env.getCtx().getContext("#"+columnasCondiciones.getColumnName()+"")+"'";
			  else
			  {
				  if (aux == 0 || aux == 1)
				  {
				      columnasSelect += abrDepartamento+"."+columnasCondiciones.getColumnName();
				      aux+=1;
				  }else
					  columnasSelect += ""+abreviatura+"."+columnasCondiciones.getColumnName();
			  }	  
				  
			  columnasInsert += columnasCondiciones.getColumnName();
		  }
		insertSelect.add(columnasSelect);
		insertSelect.add(columnasInsert);
		
		return insertSelect;
	}
	private String armarWhere(String abreviatura, String nombreTabla, String dataIn, Integer idPromocion)
	{
		 
		String where = " "+abreviatura+".IsActive = 'Y' AND "+abreviatura+"."+nombreTabla+"_ID IN (" + dataIn + ") AND "+abreviatura+"."+nombreTabla+"_ID NOT IN (select case when DETPRO."+nombreTabla+"_ID is null then 0 else DETPRO."+nombreTabla+"_ID end from XX_VMR_DetailPromotionExt DETPRO where DETPRO.XX_VMR_Promotion_ID = "+idPromocion+" and DETPRO.IsActive = 'Y')";
	
		return where;
	}
	
	private void updateDetailPromotionSequence(){
		Integer lastDetail = IdDetallePromocion()+1;
		int user =Env.getCtx().getAD_User_ID();
		int client =Env.getCtx().getAD_Client_ID();
		String sql = "UPDATE AD_SEQUENCE SET CURRENTNEXT="+lastDetail+"," +
				" UPDATEDBY="+user+" WHERE NAME='XX_VMR_DetailPromotionExt'";
		DB.executeUpdate(null, sql);
		int idSequence = DB.getNextID(client, "XX_VMR_PromoConditionValue",null);
		while(idSequence<lastDetail){
			idSequence = DB.getNextID(client, "XX_VMR_PromoConditionValue", null);
		}
	}
	
	private String columnasSelectObligatoria()
	{
		Integer cliente = Env.getCtx().getAD_Client_ID(), org = Env.getCtx().getAD_Org_ID(), creadoPor = Env.getCtx().getAD_User_ID();
		Integer actualizadoPor = Env.getCtx().getAD_User_ID();
		Integer condicionPromocion = Integer.parseInt(Env.getCtx().getContext("#XX_VMR_PromoConditionValue_ID"));
		Integer  detallePromocion = IdDetallePromocion();
		String columnasSelectObligatoria =cliente+" as cliente, "+org+" as org, "+creadoPor+" as creadoPor, " +actualizadoPor+" as actualizadoPor, " +
                " "+promocion +" as promocion," +detallePromocion+ " + rownum as detallepromocion, " +condicionPromocion+" as condicionPromocion ";
		
		return columnasSelectObligatoria;
	}
	
	private void insertarDetalleVarios(ArrayList<String> listaItemSelec, ArrayList<String> InsertSelect, String abr, String from, String nombreTabla)
	{
		String select ="";
		String where ="";
		String itemSelec = "";
		int itemInsertados = 0;
		 String insert = columnasInsertObligatoria + InsertSelect.get(1);
		 
		for (int i=0; i<listaItemSelec.size(); i++)
		{
			select = " SELECT " + columnasSelectObligatoria() + InsertSelect.get(0);
			where = " WHERE ";
			itemSelec = listaItemSelec.get(i);
			if (itemSelec.equals(""))
				continue;
			if(itemSelec.length()> 0 ) {
				itemSelec = itemSelec.substring(0, itemSelec.length()-2);
			}
			
			where += armarWhere(abr, nombreTabla, itemSelec, promocion);
			
			select += from + where;// + orderBy;
			itemInsertados += insertarDetalle(insert, select);
		}
		
		if (itemInsertados > 0)
			ADialog.info(1, new Container(), "Todo Insertado Correctamente. Nro. Registro:"+itemInsertados);
		
	}
	
	
	
}