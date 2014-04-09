package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.KeyNamePair;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_XX_VCN_EvaluationCriteria;
import compiere.model.cds.X_XX_VCN_VendorRating;

public class WeightCalculationProcess extends SvrProcess {

	/** Order to Copy				*/
	private int 	p_C_Order_ID = 0;
	
	private void InsertPromedio(int C_BPartner_ID, int SumaID, BigDecimal Suma) {
		//InsertXX_VCN_VendorRating(getCtx(), p_C_Order_ID, SumaID , C_BPartner_ID, Suma );
		String sql =  "SELECT AVG(XX_POINTS) "+ 
				      "FROM XX_VCN_VENDORRATING "+
				      "WHERE XX_C_ORDER_ID != "+ p_C_Order_ID +" and "+
				       	    "C_BPARTNER_ID = "+C_BPartner_ID+" and "+
				      	    "XX_VCN_EvaluationCriteria_ID = (select XX_VCN_EVALUATIONCRITERIA_ID " +
				      	    							"from XX_VCN_EVALUATIONCRITERIA " +
				      	    							"where UPPER(Name) = 'PUNTUACIÓN TOTAL DE LA O/C') and " +
				      	    "IsActive = 'Y' and" +
				      	    "XX_C_ORDER_ID in (select C_ORDER_ID " +
				      	    				  "from M_INOUT " +
				      	    				  "where C_ORDER_ID is not null " +
				      	    				  "and round(sysdate-MOVEMENTDATE,2) > 365)";
		
		//System.out.println("Este query: "+sql);
		String avg = new String();
		avg = BuscarValor(sql, "AVG(XX_POINTS)");
		
		if(avg == null){
			avg = new String("0");			
		}
		
		String sql1 = "Select XX_VCN_EVALUATIONCRITERIA_ID " +
				      "from XX_VCN_EVALUATIONCRITERIA " +
				      "where UPPER(Name) = 'PUNTUACIÓN ACUMULADA ANTERIOR'";
		
		InsertXX_VCN_VendorRating(getCtx(), p_C_Order_ID, new Integer(BuscarValor(sql1, "XX_VCN_EVALUATIONCRITERIA_ID")),C_BPartner_ID, new BigDecimal(avg) ) ;
	}
	
	private boolean InsertXX_VCN_VendorRating(Ctx ctx, Integer OrderID, Integer XX_EvaluationCriteria_ID, Integer XX_C_BPartner_ID, BigDecimal XX_Points){	
		X_XX_VCN_VendorRating AuxVendorRating = new X_XX_VCN_VendorRating(ctx, 0, get_TrxName());
		
		AuxVendorRating.setXX_C_Order_ID(OrderID);
		AuxVendorRating.setXX_VCN_EvaluationCriteria_ID(XX_EvaluationCriteria_ID);
		AuxVendorRating.setC_BPartner_ID(XX_C_BPartner_ID);
		AuxVendorRating.setXX_Points(XX_Points);
		
		if (AuxVendorRating.save()){
			return true;			
		}
		else{
			return false;
		}
	}

	private Vector<KeyNamePair> SelectEvaluationCriteria() {
		String AuxName = new String();
		Integer AuxID = null;
		Vector<KeyNamePair> AuxEvaluationCriteria = new Vector<KeyNamePair>();
		
		String sql1 =  "SELECT upper(Name),  XX_VCN_EVALUATIONCRITERIA_ID "+ 
					   "FROM XX_VCN_EVALUATIONCRITERIA "+
					   "WHERE IsActive = 'Y'";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql1, null);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()){
				AuxName = rs.getString("upper(Name)");
				AuxID = rs.getInt("XX_VCN_EVALUATIONCRITERIA_ID");
				AuxEvaluationCriteria.add(new KeyNamePair(AuxID,AuxName));
			}
			rs.close();
			pstmt.close();
			return AuxEvaluationCriteria;
			
		} catch (Exception e) {
			return null;
		}
	}

	private String BuscarValor(String sql, String Campo){
		try {
			String AuxCampo = new String("VACIO"); 
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				AuxCampo = rs.getString(Campo);				
			}
			rs.close();
			pstmt.close();
			return AuxCampo;			
		}catch (SQLException e) {
			return null;
		}
	}	

	private String BuscarValorAcuerdos(String sql){
		try {
			String AuxCampo = new String();
			int NumDescuentos = 0;
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				AuxCampo = rs.getString("XX_ApplyFixedDiscount");
				if (AuxCampo.equals("Y")){
					AuxCampo = rs.getString("XX_FixVolDiscoPercen");
				}else{
					AuxCampo = rs.getString("XX_FirstVarVolDiscoPercen");
				}
				if(!AuxCampo.equals("0"))
					NumDescuentos++;
				AuxCampo = rs.getString("XX_DiscountGosPercentage");
				if(!AuxCampo.equals("0"))
					NumDescuentos++;
				AuxCampo = rs.getString("XX_CreditStoreOpening");
				if(!AuxCampo.equals("0"))
					NumDescuentos++;
				AuxCampo = rs.getString("XX_DiscRecogDeclPercen");
				if(!AuxCampo.equals("0"))
					NumDescuentos++;
				AuxCampo = rs.getString("XX_DiscAfterSalePercen");
				if(!AuxCampo.equals("0"))
					NumDescuentos++;
				AuxCampo = rs.getString("XX_ParticiBecoBrochPercen");
				if(!AuxCampo.equals("0"))
					NumDescuentos++;
				AuxCampo = rs.getString("XX_CentraDiscDeliPercen");
				if(!AuxCampo.equals("0"))
					NumDescuentos++;
			}
			rs.close();
			pstmt.close();
			return new Integer(NumDescuentos).toString();			
		}catch (SQLException e) {
			return null;
		}
	}	

	
	
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	
	@Override
	protected String doIt() throws Exception {
		String sql = new String();
		String queComparo = new String();
		String porcentaje = new String();
		String Puntos = new String();
		Integer SumaID = new Integer(0);
		Vector<KeyNamePair> AuxEvaluationCriteria = SelectEvaluationCriteria();
		BigDecimal Suma = new BigDecimal(0);
		
		
		//Actualizo la Orden de Compra en Chequeada
		MOrder AuxOrder = new MOrder(getCtx(), p_C_Order_ID, get_TrxName());
		//AuxOrder.setXX_OrderStatus("Proforma");
		AuxOrder.save();
		
		/*
		 * se hace el insert de la del promerio de los anteriores. El valor no toma en cuenta los valores obtenidos en
		 * esta orden de compra. Es la puntuacion que se tiene para esta orden de compra
		 */
		InsertPromedio(AuxOrder.getC_BPartner_ID(), SumaID, Suma);
		
		//recorrer el vector de Criterios de Evaluacion
		for (int i = 0; i < AuxEvaluationCriteria.size(); i++){
			if (AuxEvaluationCriteria.elementAt(i).getName().equals("DESCUENTOS POR ACUERDO COMERCIAL")){
				X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(getCtx(), AuxEvaluationCriteria.elementAt(i).getKey(), get_TrxName());
				
				sql = "select XX_ApplyFixedDiscount , "+
					       "XX_FixVolDiscoPercen ,"+ 
					       "XX_FirstVarVolDiscoPercen ,"+ 
					       "XX_DiscountGosPercentage ,"+
					       "XX_CreditStoreOpening ,"+
					       "XX_DiscRecogDeclPercen ,"+
					       "XX_DiscAfterSalePercen ,"+
					       "XX_ParticiBecoBrochPercen ,"+
					       "XX_CentraDiscDeliPercen " +
					  "from XX_VCN_TRADEAGREEMENTS " +
					  "where XX_C_BPARTNER_ID = " + AuxOrder.getC_BPartner_ID() + " and "+
					  		"XX_CATEGORYCODE = " + AuxOrder.getXX_Category_ID() + " and "+
							"XX_DATEEFFECTIVEFROM <= SYSDATE and "+
							"XX_EFFECTIVEDATETO >= SYSDATE";
				queComparo = BuscarValorAcuerdos(sql);
				
				sql = "SELECT XX_Percentage " +
					   "FROM XX_VCN_WeightCalculation " +
					   "WHERE XX_EvaluationCriteria_ID = (SELECT XX_VCN_EvaluationCriteria_ID " +
					   									 "FROM XX_VCN_EvaluationCriteria " +
					   									 "WHERE UPPER(NAME) = '" + AuxEvaluationCriteria.elementAt(i) + "') AND " +
					   		  queComparo + " <= XX_MaxValueRange AND " +
					   		  queComparo + " >= XX_MinValueRange";
				porcentaje = BuscarValor(sql, "XX_Percentage");
				
				Puntos = AuxEval.getXX_POINT().toString();
								
				InsertXX_VCN_VendorRating(getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

				Suma = Suma.add( (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
			}else if(AuxEvaluationCriteria.elementAt(i).getName().equals("CUMPLIMIENTO DE ENTREGA EN FECHA")){
				X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(getCtx(), AuxEvaluationCriteria.elementAt(i).getKey(), get_TrxName());
				
				Timestamp aux = AuxOrder.getXX_ArrivalDate();
				GregorianCalendar gc = new GregorianCalendar(aux.getYear(), aux.getMonth(), aux.getDay());
				aux =AuxOrder.getXX_EstimatedDate();
				GregorianCalendar gc1 = new GregorianCalendar(aux.getYear(), aux.getMonth(), aux.getDay());
				
				//Obtengo los objetos Date para cada una de ellas
				Date fec1 = gc.getTime();
				Date fec2 = gc1.getTime();
				//Realizo la operación
				long time = fec2.getTime() - fec1.getTime();
				//Muestro el resultado en días
				Long time1 = time/(3600*24*1000);
				
				//Si la entrega llega antes, le asigno el % que tenga Cero
				if(time1 < 0){
					time1 = new Long(0);
				}
				
				sql = "SELECT XX_Percentage " +
					   "FROM XX_VCN_WeightCalculation " +
					   "WHERE XX_EvaluationCriteria_ID = (SELECT XX_VCN_EvaluationCriteria_ID " +
					   									 "FROM XX_VCN_EvaluationCriteria " +
					   									 "WHERE UPPER(NAME) = '" + AuxEvaluationCriteria.elementAt(i) + "') AND " +
					   		  time1.toString() + " <= XX_MaxValueRange AND " +
					   		  time1.toString() + " >= XX_MinValueRange";
				
				
				porcentaje = BuscarValor(sql, "XX_Percentage");
				
				Puntos = AuxEval.getXX_POINT().toString();
								
				InsertXX_VCN_VendorRating(getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

				Suma = Suma.add( (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
			}else if(AuxEvaluationCriteria.elementAt(i).getName().equals("CUMPLIMIENTO DE ENTREGA EN CANTIDAD")){
				/*
				 * FALTA CODIGO DE "CUMPLIMIENTO DE ENTREGA EN CANTIDAD"
				 * NO SE TIENE DEFINIDO DONDE SE TOMA LOS VALORES 
				 */
			}else if(AuxEvaluationCriteria.elementAt(i).getName().equals("CUMPLIMIENTO DE ENTREGA EN CALIDAD")){
				/*
				 * FALTA CODIGO DE "CUMPLIMIENTO DE ENTREGA EN CALIDAD"
				 * NO SE TIENE DEFINIDO DONDE SE TOMA LOS VALORES 
				 */
			}else if(AuxEvaluationCriteria.elementAt(i).getName().equals("CONDICIÓN DE PAGO")){
				X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(getCtx(), AuxEvaluationCriteria.elementAt(i).getKey(), get_TrxName());
				
				Integer AuxPaymentTerm = AuxOrder.getC_PaymentTerm_ID();
								
				sql = "SELECT XX_DaysFunding " +
					  "FROM C_PaymentTerm " +
					  "WHERE C_PaymentTerm_ID = " + AuxPaymentTerm.toString() + " ";
				queComparo = BuscarValor(sql, "XX_DaysFunding");
				
				sql = "SELECT XX_Percentage " +
					   "FROM XX_VCN_WeightCalculation " +
					   "WHERE XX_EvaluationCriteria_ID = (SELECT XX_VCN_EvaluationCriteria_ID " +
					   									 "FROM XX_VCN_EvaluationCriteria " +
					   									 "WHERE UPPER(NAME) = '" + AuxEvaluationCriteria.elementAt(i) + "') AND " +
					   		  queComparo + " <= XX_MaxValueRange AND " +
					   		  queComparo + " >= XX_MinValueRange";
				porcentaje = BuscarValor(sql, "XX_Percentage");
				
				Puntos = AuxEval.getXX_POINT().toString();
								
				InsertXX_VCN_VendorRating(getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

				Suma = Suma.add( (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
			}else if(AuxEvaluationCriteria.elementAt(i).getName().equals("IDENTIFICACIÓN DE LA MERCANCÍA Y DOCUMENTOS")){
				/*
				 * FALTA CODIGO DE "IDENTIFICACIÓN DE LA MERCANCÍA Y DOCUMENTOS"
				 * NO SE TIENE DEFINIDO DONDE SE TOMA LOS VALORES 
				 */
			}else if(AuxEvaluationCriteria.elementAt(i).getName().equals("ENTREGA DIRECTA EN TIENDA")){
				X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(getCtx(), AuxEvaluationCriteria.elementAt(i).getKey(), get_TrxName());
				
				queComparo = AuxOrder.getXX_StoreDistribution();
				
				sql = "SELECT XX_Percentage " +
					   "FROM XX_VCN_WeightCalculation " +
					   "WHERE XX_EvaluationCriteria_ID = (SELECT XX_VCN_EvaluationCriteria_ID " +
					   									 "FROM XX_VCN_EvaluationCriteria " +
					   									 "WHERE UPPER(NAME) = '" + AuxEvaluationCriteria.elementAt(i) + "') AND '" +
					   		  queComparo + "' = XX_ATRIBUTTEVALUE ";
				porcentaje = BuscarValor(sql, "XX_Percentage");

				Puntos = AuxEval.getXX_POINT().toString();
								
				InsertXX_VCN_VendorRating(getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

				Suma = Suma.add( (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
			}else if(AuxEvaluationCriteria.elementAt(i).getName().equals("PUNTUACIÓN TOTAL DE LA O/C")){
				SumaID = AuxEvaluationCriteria.elementAt(i).getKey();
			}			
		}
		
		InsertXX_VCN_VendorRating(getCtx(), p_C_Order_ID, SumaID , AuxOrder.getC_BPartner_ID(), Suma );
		/*
		 * se hace el insert de la del promerio de los anteriores. El valor no toma en cuenta los valores obtenidos en
		 * esta orden de compra. Es la puntuacion que se tiene para esta orden de compra
		 */
		//InsertPromedio(AuxOrder.getC_BPartner_ID(), SumaID, Suma);
		
		return Suma.toString();
	}

}
