package compiere.model.cds.processes;
 
import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRPOApproval;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;

public class XX_PO_OrderReadyEmail extends SvrProcess {
	
	
	@Override
	protected String doIt() throws Exception {
		
		String reference = "Y";
		
		MOrder aux = new MOrder(getCtx(),getRecord_ID(),get_Trx());
		
		// Primero verifico que la fecha de llegada este seteada
		// Esto porque al copiarla la misma queda vacia y no puedo darle a order is ready sin estar seteada
		
		if (aux.getXX_ArrivalDate()==null)
		{
			ADialog.info(1, new Container(), "XX_OrderNotReadyYet");
			Env.getCtx().setContext("#WAITPROCESS",-1); //AGREGADO POR GHUCHET
			return "";
		}
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			if( (!aux.getXX_OrderType().equalsIgnoreCase("Importada") 
					|| aux.getXX_ImportingCompany_ID()==getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") ) ) {

		// verifico que todos los productos en la matriz sean distintos de cero
			String sql = "SELECT M_Product " +
			"FROM XX_VMR_ReferenceMatrix " +
			"WHERE (m_product=0 or m_product is null) and xx_vmr_po_linerefprov_id in (select xx_vmr_po_linerefprov_id from xx_vmr_po_linerefprov where c_order_id=" + aux.getC_Order_ID()+") and xx_quantityc<>0";
			

			try{
				
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				if(rs.next())
				{
					//lanzo mensaje de error indicando que el producto beco no fue codificado correctamente para una de las referencias
					ADialog.info(1, new Container(), "XX_ProblemsWithCodification");
					 return "";
				}
			}
			catch (Exception e){
				log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
			} finally {
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
			}
		}
		
		//verifico que no hayan productos repetidos
		
			
		String sql = "select (select description from xx_vmr_vendorprodref where xx_vmr_vendorprodref_id=p.xx_vmr_vendorprodref_id) as referencia from m_product p where m_product_id in  " +
				" ( " +
					" SELECT m_product " +
					" FROM XX_VMR_ReferenceMatrix m " +
					" WHERE m_product is not null and xx_vmr_po_linerefprov_id in (select xx_vmr_po_linerefprov_id from xx_vmr_po_linerefprov where c_order_id="+aux.getC_Order_ID() +") and xx_quantityc<>0 " + 
					" group by M_Product having count(*)>1 " +
				" )";
		
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				//lanzo mensaje de error indicando que se debe borrar la matriz para el producto especificado
				commit();
				ADialog.info(1, new Container(), Msg.getMsg(getCtx(), "XX_RepeatMatrix", new String[] {rs.getString("referencia")}));
				 return "";
			}
		}
		catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}

		
		
		// Primero verifico que todas las referencias esten asociadas al producto

		sql = "SELECT XX_ReferenceIsAssociated " +
		"FROM XX_VMR_PO_LineRefProv " +
		"WHERE C_ORDER_ID=" + aux.getC_Order_ID();
		
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next() && reference.equals("Y"))
			{
				reference = rs.getString("XX_ReferenceIsAssociated");
			}
		}
		catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		
		/**
		 * Verifico que los productos tengan los precios definitivos si la O/C es DD
		 */
		boolean allDefinitive=true;
		if(aux.getXX_VLO_TypeDelivery().compareTo("DD")==0){
			
			sql = "SELECT XX_ISDEFINITIVE " +
							 "FROM XX_VMR_PO_LINEREFPROV " +
							 "WHERE C_ORDER_ID = " + aux.get_ID();
	
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
		   
		    while(rs.next())
		    {
		    	if(rs.getString("XX_IsDefinitive").equals("N")){
		    		allDefinitive=false;
		    	}
			    
		    }
		    rs.close();
		    pstmt.close();
	    }
		
		if( reference() && reference.equals("Y")
				| (aux.getXX_OrderType().equalsIgnoreCase("Importada") 
						&& aux.getXX_ImportingCompany_ID()!=getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID"))){
			if ((reference.equals("Y") && allDefinitive) 
				|| (aux.getXX_OrderType().equalsIgnoreCase("Importada") 
						&& aux.getXX_ImportingCompany_ID()!=getCtx().getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID") )
			) { //Envia un correo al jefe de la categoria y al jefe de planificacion indicando que esta lista para aprobar
				
				//Actualizo las matrices de productos (de tenerla y de tener columnas o filas en 0)
				//actualizeMatrix();
				
				String refProblem = problemsWithMatrix();
				if(refProblem==null){
				
					//Seatea la O/C como ready para los compradores
					aux.setXX_OrderReadyStatus(true);
					Calendar today = Calendar.getInstance();
					aux.setXX_OrderReadyDate(new java.sql.Timestamp(today.getTimeInMillis()));
					aux.save();	
					
					if(!aux.getXX_ComesFromSITME())
						POLimits(aux);
					
					//Ahora intentaremos aprobar la O/C
					approvePO();
					
				}
				else{
					ADialog.info(1, new Container(), Msg.getMsg(Env.getCtx(), "XX_RefWithProblem", new String[] {refProblem}));
					 Env.getCtx().setContext("#WAITPROCESS",-1); //AGREGADO POR GHUCHET
				}
			}
			else{
				ADialog.info(1, new Container(), "XX_OrderNotReadyYet");
				 Env.getCtx().setContext("#WAITPROCESS",-1); //AGREGADO POR GHUCHET
			}
		}else{
			ADialog.info(1, new Container(), "XX_NoReferences");
			 Env.getCtx().setContext("#WAITPROCESS",-1); //AGREGADO POR GHUCHET
		}
		
		// Si la orden es importada actualizamos los valores de los detalles y la cabecera para consistencia
		if (aux.getXX_OrderType().equals("Importada"))
		{
			updateValues(aux);
		}

		return "";
	}

	// Actualiza los valores
	private void updateValues(MOrder orden) {
		
		// necesitamos el factor de reposicion y el factor estimado
		BigDecimal factorReposicion = orden.getXX_ReplacementFactor();
		BigDecimal factorEstimado = orden.getXX_EstimatedFactor();
		
		// Actualizamos el costo mostrado en el detalle
		String SQL_Update = "update XX_VMR_PO_LineRefProv set priceactual=(" + factorEstimado + "*xx_unitpurchaseprice) where c_order_id="+orden.get_ID();
		DB.executeUpdate(get_Trx(),SQL_Update);
		
		// Actualizamos el margen en el detalle
		SQL_Update = "update XX_VMR_PO_LineRefProv set xx_margin=((xx_saleprice-(" + factorReposicion + "*xx_unitpurchaseprice))/xx_saleprice)*100 where c_order_id="+orden.get_ID();
		DB.executeUpdate(get_Trx(),SQL_Update);
		
		//Actualiza lineNetAmt
		String sql_LineNetAmt = "UPDATE XX_VMR_PO_LineRefProv SET LINENETAMT = (XX_CostWithDiscounts*QTY) WHERE C_Order_ID = " + orden.getC_Order_ID();
		DB.executeUpdate(get_Trx(), sql_LineNetAmt);
							
		//Valores de Cabecera			
		String sql_header = "";
		sql_header = "UPDATE C_Order po"											//////////////////////
				+ " SET (XX_ProductQuantity, TotalPVP, XX_TotalPVPPlusTax, TotalLines, XX_TotalCostBs, XX_EstimatedMargin ) ="											//////////////////////
					+ "(SELECT COALESCE(SUM(XX_LineQty),0), COALESCE(SUM(XX_LinePVPAmount),0), COALESCE(SUM(XX_LinePlusTaxAmount),0), COALESCE(SUM(LineNetAmt),0), COALESCE(SUM(PriceActual*qty),0), round(avg(xx_margin),2)  FROM XX_VMR_PO_LINEREFPROV line "
					+ "WHERE po.C_Order_ID=line.C_Order_ID ) "
				+ "WHERE po.C_Order_ID=" + orden.getC_Order_ID();

		DB.executeUpdate(get_Trx(), sql_header);

		String sql_cost = "UPDATE C_Order po"
					+ " SET XX_TotalCostBs = TotalLines"
					+ " WHERE XX_TotalCostBs<1 and po.C_Order_ID=" + orden.getC_Order_ID();
				
		DB.executeUpdate(get_Trx(), sql_cost);
				
				
		// Actualizamos el margen en la cabecera
		SQL_Update = "update C_Order set xx_estimatedmargin=(select coalesce(round(sum(qty*xx_margin)/(select XX_ProductQuantity from c_order where issotrx='N' and xx_productQuantity>0 and c_order_id=l.c_order_id),2),0) as XX_RealMargin from xx_vmr_po_linerefprov l where  c_order_id="+getRecord_ID()+" group by c_order_id) where c_order_id="+orden.get_ID();
		DB.executeUpdate(get_Trx(),SQL_Update);
		
	}
	

	@Override
	protected void prepare() {

	}
	
	private String problemsWithMatrix(){
		
		String sql = "SELECT VEN.VALUE FROM c_order ord, XX_VMR_PO_LineRefProv pol, XX_VMR_VENDORPRODREF VEN " +
					 "where ord.c_order_id="+ getRecord_ID()+ " and ord.xx_ordertype='Nacional' and ord.c_order_id=pol.c_order_ID " +
					 "and XX_VMR_PO_LineRefProv_ID NOT IN " +
					 "(select XX_VMR_PO_LineRefProv_ID from xx_vmr_referencematrix) " +
					 "and VEN.XX_VMR_VENDORPRODREF_ID=pol.XX_VMR_VENDORPRODREF_ID";
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				return rs.getString("VALUE");

			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		return null;
	}

	
	/*
	 * Tiene Referencias asociadas?
	 */
	private boolean reference()
	{
		String SQL = "SELECT * FROM XX_VMR_PO_LINEREFPROV " +
					 "WHERE C_ORDER_ID = " + getRecord_ID();
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				return true;
			}
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
				return false;
			}finally{
				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		
		return false;
	}

	/*
	 * actualizeMatrix (Actualiza las matrices de las lineas en caso de que tengan columnas o filas en 0, elimnandolas)
	 * JTrias
	 */
	private void actualizeMatrix(){

		//Para cada una de las lineas verifico
		//Por columnas
		Vector<Integer> lines = linesWithColsOnZero();
		Vector<int[]> matrix = new Vector<int[]>();
		Boolean inTheMiddle=false;
		
		for(int i=0; i<lines.size(); i++){
		 	inTheMiddle=false;
			MVMRPOLineRefProv lineRefProv = new MVMRPOLineRefProv( getCtx(), lines.get(i), null);
			matrix = getMatrixByColumn(lineRefProv.get_ID());
			
			boolean isZero = false;
			int column = -1;
			int rows = 0;
			Vector<Integer> referenceMatrixID = new Vector<Integer>();
			Vector<Integer> values1 = new Vector<Integer>();
			Vector<Integer> values1Aux = new Vector<Integer>();
			
			for(int j=matrix.size(); j>-1; j--){
				
				if(j!=0){
					
					if(column!=matrix.get(j-1)[1]){
					
						if(isZero){
											
							//Si está de ultima
							if((j+rows)==matrix.size()){
								
								for(int w=0; w<rows; w++){
									X_XX_VMR_ReferenceMatrix refMatrix = new X_XX_VMR_ReferenceMatrix( getCtx(), referenceMatrixID.get(w), null);
									refMatrix.delete(true);
									
									matrix.remove(j+rows-w-1);
								}
	
							}
							else{
								
								inTheMiddle=true;
								
								//Copio las casillas que van a reemplazar a las eliminadas
								for(int w=(j); w<matrix.size(); w++){
	
									if((w+rows)<matrix.size()){
								
										int[] array = matrix.get(w);
										
										array[0] = matrix.get(w+rows)[0];
										array[4] = matrix.get(w+rows)[4];
										array[5] = matrix.get(w+rows)[5];
										array[6] = matrix.get(w+rows)[6];
										array[7] = matrix.get(w+rows)[7];
										
										matrix.set(w,array);
									}
								}
								
								//Borro las ultimas casillas
								for(int w=0; w<rows; w++){
									
									X_XX_VMR_ReferenceMatrix refMatrix = new X_XX_VMR_ReferenceMatrix( getCtx(), matrix.get(matrix.size()-1)[2], null);
									refMatrix.delete(true);
									
									matrix.remove(matrix.size()-1);
								}
							}
							
						}else{
							
							//Descargo el vector auxiliar de values1 en el temporal para luego guardar la linea con los valores definitivos
							if(values1Aux.size()>0){
								values1.add(values1Aux.get(0));
							}
							
							isZero=true;
						}
						
						column = matrix.get(j-1)[1];
						
						//Borro las variables auxiliares
						rows=0;
						referenceMatrixID.removeAllElements();
						values1Aux.removeAllElements();
					}
					
					//Si estan en cero las cantidades
					if(matrix.get(j-1)[4]==0 && matrix.get(j-1)[5]==0){
						rows++;
						referenceMatrixID.add(matrix.get(j-1)[2]);
					}else{
						values1Aux.add(matrix.get(j-1)[0]);
						isZero=false;
					}
				
				}else{
					if(values1Aux.size()>0){
						values1.add(values1Aux.get(0));
					}
				}
			}
			
			//Modifico las referenceMatrix finales
			if(inTheMiddle){
				for(int r=0; r<matrix.size(); r++){
	
					X_XX_VMR_ReferenceMatrix refMatrix = new X_XX_VMR_ReferenceMatrix( getCtx(), matrix.get(r)[2], null);
					refMatrix.setXX_VALUE1(matrix.get(r)[0]);
					refMatrix.setXX_QUANTITYC(matrix.get(r)[4]);
					refMatrix.setXX_QUANTITYO(matrix.get(r)[5]);
					refMatrix.setXX_QUANTITYV(matrix.get(r)[6]);
					refMatrix.setM_Product(matrix.get(r)[7]);
					refMatrix.save();
				}
			}
			
			//Borro todos los values1 de la linea
			lineRefProv.setXX_Characteristic1Value1_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value1(false);
			lineRefProv.setXX_Characteristic1Value2_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value2(false);
			lineRefProv.setXX_Characteristic1Value3_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value3(false);
			lineRefProv.setXX_Characteristic1Value4_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value4(false);
			lineRefProv.setXX_Characteristic1Value5_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value5(false);
			lineRefProv.setXX_Characteristic1Value6_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value6(false);
			lineRefProv.setXX_Characteristic1Value7_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value7(false);
			lineRefProv.setXX_Characteristic1Value8_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value8(false);
			lineRefProv.setXX_Characteristic1Value9_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value9(false);
			lineRefProv.setXX_Characteristic1Value10_ID(0);
			lineRefProv.setXX_IsGeneratedCharac1Value10(false);
			
			//Volteo el vector
			Vector<Integer> values1InOrder = new Vector<Integer>();
			
			for(int w=values1.size(); w>0; w--){
				values1InOrder.add(values1.get(w-1));
			}
			
			for(int w=0; w<values1.size(); w++){
				
				if(w==0){
					lineRefProv.setXX_Characteristic1Value1_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value1(true);
				}
				if(w==1){
					lineRefProv.setXX_Characteristic1Value2_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value2(true);
				}
				if(w==2){
					lineRefProv.setXX_Characteristic1Value3_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value3(true);
				}
				if(w==3){
					lineRefProv.setXX_Characteristic1Value4_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value4(true);
				}
				if(w==4){
					lineRefProv.setXX_Characteristic1Value5_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value5(true);
				}
				if(w==5){
					lineRefProv.setXX_Characteristic1Value6_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value6(true);
				}
				if(w==6){
					lineRefProv.setXX_Characteristic1Value7_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value7(true);
				}
				if(w==7){
					lineRefProv.setXX_Characteristic1Value8_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value8(true);
				}
				if(w==8){
					lineRefProv.setXX_Characteristic1Value9_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value9(true);
				}
				if(w==9){
					lineRefProv.setXX_Characteristic1Value10_ID(values1InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac1Value10(true);
				}
				
			}
			
			lineRefProv.save();
		}
		
		//Para las filas en 0
		Vector<Integer> linesByRow = linesWithRowsOnZero();
		Vector<int[]> matrixByRow = new Vector<int[]>();
		
		for(int i=0; i<linesByRow.size(); i++){
			MVMRPOLineRefProv lineRefProv = new MVMRPOLineRefProv( getCtx(), linesByRow.get(i), null);
			inTheMiddle=false;
			matrixByRow = getMatrixByRow(lineRefProv.get_ID());
		
			boolean isZero = false;
			int row = -1;
			int columns = 0;
			Vector<Integer> referenceMatrixID = new Vector<Integer>();
			Vector<Integer> values2 = new Vector<Integer>();
			Vector<Integer> values2Aux = new Vector<Integer>();
			
			for(int j=matrixByRow.size(); j>-1; j--){
				
				if(j!=0){
					
					if(row!=matrixByRow.get(j-1)[1]){
					
						if(isZero){
											
							//Si está de ultima
							if((j+columns)==matrixByRow.size()){
								
								for(int w=0; w<columns; w++){
									X_XX_VMR_ReferenceMatrix refMatrix = new X_XX_VMR_ReferenceMatrix( getCtx(), referenceMatrixID.get(w), null);
									refMatrix.delete(true);
									matrixByRow.remove(j+columns-w-1);
								}
							}
							else{
								
								inTheMiddle=true;							
								
								//Copio las casillas que van a reemplazar a las eliminadas
								for(int w=(j); w<matrixByRow.size(); w++){
	
									if((w+columns)<matrixByRow.size()){
								
										int[] array = matrixByRow.get(w);
										
										array[0] = matrixByRow.get(w+columns)[0];
										array[4] = matrixByRow.get(w+columns)[4];
										array[5] = matrixByRow.get(w+columns)[5];
										array[6] = matrixByRow.get(w+columns)[6];
										array[7] = matrixByRow.get(w+columns)[7];
										
										matrixByRow.set(w,array);
									}
								}
								
								//Borro las ultimas casillas
								for(int w=0; w<columns; w++){
									
									X_XX_VMR_ReferenceMatrix refMatrix = new X_XX_VMR_ReferenceMatrix( getCtx(), matrixByRow.get(matrixByRow.size()-1)[2], null);
									refMatrix.delete(true);
									
									matrixByRow.remove(matrixByRow.size()-1);
								}
							}
							
						}else{
							
							//Descargo el vector auxiliar de values1 en el temporal para luego guardar la linea con los valores definitivos
							if(values2Aux.size()>0){
								values2.add(values2Aux.get(0));
							}
							
							isZero=true;
						}
						
						row = matrixByRow.get(j-1)[1];
						
						//Borro las variables auxiliares
						columns=0;
						referenceMatrixID.removeAllElements();
						values2Aux.removeAllElements();
					}
					
					//Si estan en cero las cantidades
					if(matrixByRow.get(j-1)[4]==0 && matrixByRow.get(j-1)[5]==0){
						columns++;
						referenceMatrixID.add(matrixByRow.get(j-1)[2]);
					}else{
						values2Aux.add(matrixByRow.get(j-1)[0]);
						isZero=false;
					}
				
				}else{
					if(values2Aux.size()>0){
						values2.add(values2Aux.get(0));
					}
				}
			}
			
			//Modifico las referenceMatrix finales
			if(inTheMiddle){
				for(int r=0; r<matrixByRow.size(); r++){
		
					X_XX_VMR_ReferenceMatrix refMatrix = new X_XX_VMR_ReferenceMatrix( getCtx(), matrixByRow.get(r)[2], null);
					refMatrix.setXX_VALUE2(matrixByRow.get(r)[0]);
					refMatrix.setXX_QUANTITYC(matrixByRow.get(r)[4]);
					refMatrix.setXX_QUANTITYO(matrixByRow.get(r)[5]);
					refMatrix.setXX_QUANTITYV(matrixByRow.get(r)[6]);
					refMatrix.setM_Product(matrixByRow.get(r)[7]);
					refMatrix.save();
				}
			}
			
			//Borro todos los values1 de la linea
			lineRefProv.setXX_Characteristic2Value1_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value1(false);
			lineRefProv.setXX_Characteristic2Value2_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value2(false);
			lineRefProv.setXX_Characteristic2Value3_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value3(false);
			lineRefProv.setXX_Characteristic2Value4_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value4(false);
			lineRefProv.setXX_Characteristic2Value5_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value5(false);
			lineRefProv.setXX_Characteristic2Value6_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value6(false);
			lineRefProv.setXX_Characteristic2Value7_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value7(false);
			lineRefProv.setXX_Characteristic2Value8_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value8(false);
			lineRefProv.setXX_Characteristic2Value9_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value9(false);
			lineRefProv.setXX_Characteristic2Value10_ID(0);
			lineRefProv.setXX_IsGeneratedCharac2Value10(false);
			
			//Volteo el vector
			Vector<Integer> values2InOrder = new Vector<Integer>();
			
			for(int w=values2.size(); w>0; w--){
				values2InOrder.add(values2.get(w-1));
			}
			
			//Seteo los values en la linea PO_LineRefProv
			for(int w=0; w<values2.size(); w++){
				
				if(w==0){
					lineRefProv.setXX_Characteristic2Value1_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value1(true);
				}
				else if(w==1){
					lineRefProv.setXX_Characteristic2Value2_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value2(true);
				}
				else if(w==2){
					lineRefProv.setXX_Characteristic2Value3_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value3(true);
				}
				else if(w==3){
					lineRefProv.setXX_Characteristic2Value4_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value4(true);
				}
				else if(w==4){
					lineRefProv.setXX_Characteristic2Value5_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value5(true);
				}
				else if(w==5){
					lineRefProv.setXX_Characteristic2Value6_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value6(true);
				}
				else if(w==6){
					lineRefProv.setXX_Characteristic1Value7_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value7(true);
				}
				else if(w==7){
					lineRefProv.setXX_Characteristic2Value8_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value8(true);
				}
				else if(w==8){
					lineRefProv.setXX_Characteristic2Value9_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value9(true);
				}
				else if(w==9){
					lineRefProv.setXX_Characteristic2Value10_ID(values2InOrder.get(w));
					lineRefProv.setXX_IsGeneratedCharac2Value10(true);
				}
			}
			
			//Guardo la linea
			lineRefProv.save();
		}
	}
	
	private Vector<Integer> linesWithColsOnZero(){
	
		String sql =  "SELECT DISTINCT RM.XX_VMR_PO_LINEREFPROV_ID " +
					 "FROM XX_VMR_REFERENCEMATRIX RM, C_ORDER O, XX_VMR_PO_LINEREFPROV LRP " + 
					 "WHERE " +
					 "LRP.XX_VMR_PO_LINEREFPROV_ID=RM.XX_VMR_PO_LINEREFPROV_ID " + 
					 "AND O.C_ORDER_ID=LRP.C_ORDER_ID " +
   					 "AND O.C_ORDER_ID = " + getRecord_ID() + " " +
					 "GROUP BY RM.XX_VMR_PO_LINEREFPROV_ID, RM.XX_VALUE1 " +
					 "HAVING SUM(RM.XX_QUANTITYC+RM.XX_QUANTITYO) = 0";

		Vector<Integer> lines = new Vector<Integer>();
		try{
		
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				lines.add(rs.getInt(1));
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		return lines;
	}
	
	private Vector<Integer> linesWithRowsOnZero(){
		
		String sql = "SELECT DISTINCT RM.XX_VMR_PO_LINEREFPROV_ID " +
					 "FROM XX_VMR_REFERENCEMATRIX RM, C_ORDER O, XX_VMR_PO_LINEREFPROV LRP " +
					 "WHERE "+
					 "LRP.XX_VMR_PO_LINEREFPROV_ID=RM.XX_VMR_PO_LINEREFPROV_ID " +
					 "AND O.C_ORDER_ID=LRP.C_ORDER_ID " +
					 "AND O.C_ORDER_ID = " + getRecord_ID() + " " +
					 "GROUP BY RM.XX_VMR_PO_LINEREFPROV_ID, RM.XX_VALUE2 " +
					 "HAVING SUM(RM.XX_QUANTITYC+RM.XX_QUANTITYO) = 0 ";

		Vector<Integer> lines = new Vector<Integer>();
		try{
		
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				lines.add(rs.getInt(1));
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		return lines;
	}
	
	private Vector<int[]> getMatrixByColumn(int lineID){
		
		String sql = "SELECT XX_VALUE1, XX_COLUMN, XX_VMR_REFERENCEMATRIX_ID,XX_ROW, XX_QUANTITYC, XX_QUANTITYO, XX_QUANTITYv, M_PRODUCT " +
					 "FROM XX_VMR_REFERENCEMATRIX " +
					 "WHERE XX_VMR_PO_LINEREFPROV_ID = " + lineID +
					 " ORDER BY XX_COLUMN, XX_ROW";

		System.out.println(sql);
		
		Vector<int[]> matrix = new Vector<int[]>();
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
		
			while(rs.next())
			{
				int[] array = new int[8];
				array[0] = rs.getInt(1);
				array[1] = rs.getInt(2);
				array[2] = rs.getInt(3);
				array[3] = rs.getInt(4);
				array[4] = rs.getInt(5);
				array[5] = rs.getInt(6);
				array[6] = rs.getInt(7);
				array[7] = rs.getInt(8);
				
				matrix.add(array);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		return matrix;
	}
	
	/** 
	 * 
	 * Determinar limites de la OC.
	 * @author ghuchet
	 * 
	 */
	public static void POLimits(MOrder order){

		try 
		{
			Date fechaReq = new Date();
		
			BigDecimal totalOC = new BigDecimal(0);
			BigDecimal limit = new BigDecimal(0);
			BigDecimal invFinalProy = BigDecimal.ZERO; 
			BigDecimal invFinalPresu = BigDecimal.ZERO; 
			BigDecimal sumOCApproved = BigDecimal.ZERO; 
			
			Integer idLine = 0;
			Integer idDpto = 0;
		
			String SQL30 = ("DELETE FROM XX_VMR_PO_APPROVAL WHERE C_ORDER_ID = '"+order.getC_Order_ID()+"' ");
			
			DB.executeUpdate( null, SQL30); 
			
			fechaReq = order.getXX_EstimatedDate();
			
			String SQL1 = ("SELECT DISTINCT B.XX_VMR_LINE_ID AS LINE, C.XX_VMR_DEPARTMENT_ID AS DPTO " +
					"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_DEPARTMENT C, XX_VMR_LINE D " +
					"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
					"AND A.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
					"AND B.XX_VMR_LINE_ID = D.XX_VMR_LINE_ID " +
					"AND A.C_ORDER_ID = "+ order.getC_Order_ID());

			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
		    ResultSet rs1 = pstmt1.executeQuery();
		    
		    while (rs1.next())
		    {
		    	MVMRPOApproval req = new MVMRPOApproval(Env.getCtx(), order.getC_Order_ID(), null,0);
		    	
		    	idLine = rs1.getInt("LINE");
		    	idDpto = rs1.getInt("DPTO");
		    	
		    	String SQL2 = ("SELECT SUM (XX_SALEPRICE * QTY ) AS RESULTADO " +
		    			"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B " +
		    			"WHERE B.XX_VMR_LINE_ID = '"+idLine+"' " +
		    			"AND B.C_ORDER_ID = A.C_ORDER_ID " +
		    			"AND A.C_ORDER_ID = "+ order.getC_Order_ID());
		    	
		    	PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
			    ResultSet rs2 = pstmt2.executeQuery();

			    if (rs2.next())
			    {
			    	totalOC = rs2.getBigDecimal("RESULTADO");
			    		
			    	String budgetYearMonth = "";
		  			budgetYearMonth += fechaReq.toString().subSequence(0, 4);
			    	budgetYearMonth += fechaReq.toString().subSequence(5, 7);
			    	
			    	 invFinalProy = new BigDecimal(0); // Calculo del Inventario Final Proyectado
					 
					 if (invFinalProy.compareTo(BigDecimal.ZERO)==-1) //invenProy < 0
						 invFinalProy = BigDecimal.ZERO;   	
						 

				    	String SQL3= "SELECT SUM (XX_SALEPRICE * QTY ) AS RESULTADO " +
				    			"\nFROM C_ORDER A, XX_VMR_PO_LINEREFPROV B " +
				    			"\nWHERE B.XX_VMR_LINE_ID = '"+idLine+"' " +
				    			"\nAND B.C_ORDER_ID = A.C_ORDER_ID " +
				    			"\nAND ( " +
				    			"\n	(XX_OrderType = 'Importada' and XX_OrderStatus = 'EP' and TRUNC(XX_InsertedStatusDate) = TRUNC(SYSDATE)) " +
				    			"\n OR 	 " +
				    			"\n	(XX_OrderType = 'Nacional' and XX_OrderStatus = 'AP' and TRUNC(XX_ApprovalDate) = TRUNC(SYSDATE)) " +
				    			"\n )";
				    	
				    	PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
					    ResultSet rs3 = pstmt3.executeQuery();

					    if (rs3.next())
					    {
					    	sumOCApproved = rs3.getBigDecimal("RESULTADO");
					    }
					    rs3.close();
					    pstmt3.close();
					    
					 String SQL13 = (" SELECT SUM (XX_FINALINVAMOUNTBUD2) AS Inventa_Final_Presu," +
					 				" SUM(XX_FINALINVAMOUNTPROJD) Inventa_Final_Proy" +
					 				" FROM XX_VMR_PRLD01 " +
							 		" WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' " +
						    		" AND XX_BUDGETYEARMONTH='"+budgetYearMonth+ "' ");
						    	
					 PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null); 
					 ResultSet rs13 = pstmt13.executeQuery();
								   
					 if (rs13.next())
					 {
						 invFinalPresu = rs13.getBigDecimal("Inventa_Final_Presu"); // Inventario Final Presupuestado
						 invFinalProy = rs13.getBigDecimal("Inventa_Final_Proy"); // Inventario Final Proyectado
						 if(invFinalPresu==null)
							 invFinalPresu = BigDecimal.ZERO;
						 if(invFinalProy == null)
							 invFinalProy = BigDecimal.ZERO;
						 if(sumOCApproved == null)
							 sumOCApproved = BigDecimal.ZERO;
						 //Se suma al inventario final presupuestado el total de las OC aprobadas en el dia actual. 
						 invFinalProy.add(sumOCApproved);
						 
						 limit = invFinalPresu.subtract(invFinalProy);
					 }
					 rs13.close();
					 pstmt13.close();
					 
					 BigDecimal porcentaje = BigDecimal.ZERO;
					    
					 if(invFinalPresu.compareTo(BigDecimal.ZERO)>0){
						    
						 //Calculamos el porcentaje en exceso
						 if(totalOC.compareTo(limit)<=0)
						 {
							 porcentaje = new BigDecimal(-1);
						 }
						 else{
							 porcentaje = ((totalOC.subtract(limit)).divide(invFinalPresu, 2, RoundingMode.HALF_UP)).abs();
						 }
					 }
					 else{
						 //Si el Inv. F. Presupuestano no existe el porcentaje deberia quedar en 100 para que lo aprueben los gerentes
						 porcentaje = BigDecimal.ONE;
					 }
							
					 //Para ver el porcentaje de 0 a 100
					 porcentaje = porcentaje.multiply(new BigDecimal(100));
							
					 req.setXX_PercentageExcess(porcentaje);
					 req.setXX_Limit(limit);
					 req.setXX_LimitTotal(totalOC);
					 req.setXX_VMR_Department_ID(idDpto);
					 req.setXX_VMR_Line_ID(idLine);
					 req.save();	
			    }
			    rs2.close();
			    pstmt2.close();
			      	
		    } // end while 
		    rs1.close();
		    pstmt1.close();  
		}
		catch(Exception e) 
		{  
			e.printStackTrace();
		}
	}
	
	
	private Vector<int[]> getMatrixByRow(int lineID){
		
		String sql = "SELECT XX_VALUE2, XX_ROW, XX_VMR_REFERENCEMATRIX_ID,XX_COLUMN, XX_QUANTITYC, XX_QUANTITYO, XX_QUANTITYV, M_PRODUCT " +
					 "FROM XX_VMR_REFERENCEMATRIX " +
					 "WHERE XX_VMR_PO_LINEREFPROV_ID = " + lineID +
					 " ORDER BY XX_ROW, XX_COLUMN";

		System.out.println(sql);
		
		Vector<int[]> matrix = new Vector<int[]>();
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
		
			while(rs.next())
			{
				int[] array = new int[8];
				array[0] = rs.getInt(1);
				array[1] = rs.getInt(2);
				array[2] = rs.getInt(3);
				array[3] = rs.getInt(4);
				array[4] = rs.getInt(5);
				array[5] = rs.getInt(6);
				array[6] = rs.getInt(7);
				array[7] = rs.getInt(8);
				
				matrix.add(array);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		return matrix;
	}
	
	/*
	 * Aprueba la O/C
	 */
	private void approvePO()
	{
		MPInstance mpi = new MPInstance( Env.getCtx(), 1000041, getRecord_ID()); 
		mpi.save();
			
		ProcessInfo pi = new ProcessInfo("", 1000041); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(1000041); 
		pi.setClassName(""); 
		pi.setTitle(""); 
			
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
		
	}//
}