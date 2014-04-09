package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRPOLineRefProv;


public class VME_PriceProductCallout extends CalloutEngine {
	
	public String priceBandBeco (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		System.out.println("pricebandBeco");
		
		PreparedStatement pstmtC = null;
		ResultSet rsC = null;
		try
		{

			MVMRPOLineRefProv lineRefProv = new MVMRPOLineRefProv( ctx, (Integer)mTab.getValue("XX_VMR_PO_LineRefProv_ID"), null);
			MOrder order = new MOrder( ctx, lineRefProv.getC_Order_ID(), null);

			//String conceptoProducto = ctx.getContext(WindowNo, "XX_ConceptValue_ID");
			Integer conceptoProducto = lineRefProv.getXX_VME_ConceptValue_ID();
			
			//String seccionProducto = ctx.getContext(WindowNo,"XX_SECTION_ID");
			Integer seccionProducto = lineRefProv.getXX_VMR_Section_ID();
			
			//String lineaProducto = ctx.getContext(WindowNo,"XX_LINE_ID");
			Integer lineaProducto = lineRefProv.getXX_VMR_Line_ID();
			
			//String departamentoProducto = ctx.getContext(WindowNo,"XX_VMR_DEPARTMENT_ID");
			Integer departamentoProducto = order.getXX_VMR_DEPARTMENT_ID();
			
			/** Agregado por Javier Pino, para que funcione en distribucion */ 
			if ( mTab.getValue("M_Product_ID") != null ) {
				MProduct producto = new MProduct(ctx, (Integer)mTab.getValue("M_Product_ID"), null);
				conceptoProducto = producto.getXX_VME_ConceptValue_ID();
				departamentoProducto = producto.getXX_VMR_Department_ID();
				seccionProducto = producto.getXX_VMR_Section_ID();
				lineaProducto = producto.getXX_VMR_Line_ID();
			}
			/** Fin agregado Javier Pino*/
			
			String conceptoComparar=null;
			
			String SQLA = "select P.xx_comparisonvalue_id " +
						  "from xx_vme_priceband P , xx_vme_conceptvalue C " +
						  "where C.XX_VME_ConceptValue_ID = "+conceptoProducto+" and " +
						  //"where C.XX_VME_ConceptValue_ID = 1000002 and " +
						  //"where C.XX_VME_ConceptValue_ID = 1000002 and " +
						  "C.XX_VME_ConceptValue_id = P.xx_conceptvalue_id";
			
			//System.out.println("sqla "+SQLA);
			
			PreparedStatement pstmtA = DB.prepareStatement(SQLA, null);
			ResultSet rsA = pstmtA.executeQuery();
			
			if(rsA.next())
			{
				conceptoComparar=rsA.getString("xx_comparisonvalue_id");
				//conceptoComparar="1000002";
			}
			rsA.close();
			pstmtA.close();

			Double promedioSeccion =0.0;
			Double promedioLinea =0.0;
			Double promedioDepartamento =0.0;
			
			String SQL = "SELECT AVG(AUX.A) valor FROM " +
						 "(SELECT MAX(P.XX_SALEPRICE) A, P.M_PRODUCT_ID " +
						 "FROM XX_VMR_PRICECONSECUTIVE P, M_PRODUCT M " +
						 "WHERE ";
				 
						if(conceptoComparar==null){
							SQL+="M.XX_VME_ConceptValue_ID IS NULL and ";
					    }else{
					    	SQL+="M.XX_VME_ConceptValue_ID="+conceptoComparar+" and ";
					    }
						 
						SQL+="M.XX_VMR_Section_ID = "+seccionProducto+" " +
			//			"P.m_product_id <> "+ctx.getContext(WindowNo, "M_Product_ID")+" and "+
						"AND P.M_PRODUCT_ID = M.M_PRODUCT_ID " +
						"group by P.M_PRODUCT_ID) AUX";
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			//System.out.println("sql "+SQL);
		
			if(rs.next())
			{
				promedioSeccion = rs.getDouble("valor");
			}
			rs.close();
			pstmt.close();
			
			if (promedioSeccion==0.0)
			{
				String SQL2 = "SELECT AVG(AUX.A) valor FROM " +
							  "(SELECT MAX(P.XX_SALEPRICE) A, P.M_PRODUCT_ID " +
				 			  "FROM XX_VMR_PRICECONSECUTIVE P, M_PRODUCT M " +
				 			  "WHERE ";
				 			    
				 			  if(conceptoComparar==null){
				 				  SQL2+="M.XX_VME_ConceptValue_ID IS NULL and ";
							  }else{
							      SQL2+="M.XX_VME_ConceptValue_ID="+conceptoComparar+" and ";
							  }
				 			  
							   SQL2+="M.XX_VMR_Line_ID = "+lineaProducto+" " +
//				 			  "P.m_product_id <> "+ctx.getContext(WindowNo, "M_Product_ID")+" and "+
				 			  "AND P.M_PRODUCT_ID = M.M_PRODUCT_ID " +
				 			  "group by P.M_PRODUCT_ID) AUX";
				
				PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
				ResultSet rs2 = pstmt2.executeQuery();
				//System.out.println("sql2"+SQL2);
			
				if(rs2.next())
				{
					promedioLinea = rs2.getDouble("valor");
				}
				rs2.close();
				pstmt2.close();
				
				if(promedioLinea==0.0)
				{
					String SQL3 ="SELECT AVG(AUX.A) valor FROM " +
								 "(SELECT MAX(P.XX_SALEPRICE) A, P.M_PRODUCT_ID " +
		 			  			 "FROM XX_VMR_PRICECONSECUTIVE P, m_product M " +
		 			  			 "WHERE ";
			 			  		  
		 			  			 if(conceptoComparar==null){
					 				  SQL2+="M.XX_VME_ConceptValue_ID IS NULL and ";
								  }else{
								      SQL2+="M.XX_VME_ConceptValue_ID="+conceptoComparar+" and ";
								  }
					
		 			  			 SQL3 += "M.XX_VMR_Department_ID = "+departamentoProducto+" " +
//		 			  			 "P.m_product_id <> "+ctx.getContext(WindowNo, "M_Product_ID")+" and "+
		 			  			 "AND P.M_PRODUCT_ID = M.M_PRODUCT_ID " +
		 			  			 "group by P.M_PRODUCT_ID) AUX";
					
					PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null);
					ResultSet rs3 = pstmt3.executeQuery();
					//System.out.println("TRES");
					if(rs3.next())
					{
						promedioDepartamento = rs3.getDouble("valor");
					}
					rs3.close();
					pstmt3.close();
				}
			}
			
			Double promedio = promedioSeccion+promedioLinea+promedioDepartamento;
			
			System.out.println("Promedio -> "+promedio);
			
			// Si no tiene precio en depart, linea y seccion
			if(promedio==0.0){
				return "";
			}
			
			String SQLC = "SELECT * " +
			  "FROM xx_vme_priceband P , xx_vme_conceptvalue C " +
			  "WHERE C.xx_vme_conceptvalue_id = "+conceptoProducto+" AND " +
			  "C.xx_vme_conceptvalue_id = P.xx_conceptvalue_id";

			pstmtC = DB.prepareStatement(SQLC, null);
			rsC = pstmtC.executeQuery();
			
			System.out.println(SQLC);

			if(rsC.next())
			{
				Double precioProducto =  new Double(ctx.getContext(WindowNo,"XX_SalePricePlusTax"));
				Double low = rsC.getDouble("xx_lowrank");
				Double high = rsC.getDouble("xx_highrank");
				
				//if((low-rsC.getDouble("xx_percentagevalue")) < 0 )
				//{
				//	low = new Double(0);
				//}

				Double percentage = rsC.getDouble("xx_percentagevalue");
				
				Double incrementaBanda = promedio * (percentage/100);
				
				String operador = rsC.getString("xx_operating");
				
				DB.closeStatement(pstmtC);
				DB.closeResultSet(rsC);
				
				// Menor que 10000012
				// Mayor que 10000013
				
				C_OrderCallout precio = new C_OrderCallout();
				
				System.out.println(operador);
				
			    if(operador.equals("Minor"))
			    {
			    	//Double bandaMayor = (promedio - promedio*(low/100))+incrementaBanda;
					//Double bandaMenor = (promedio - promedio*(high/(100)))-incrementaBanda;
					
					BigDecimal doubleAux = new BigDecimal((promedio - promedio*(low/100))+incrementaBanda);
					doubleAux = doubleAux.setScale(2,BigDecimal.ROUND_HALF_UP);
					Double bandaMayor = doubleAux.doubleValue();
					
					doubleAux = new BigDecimal((promedio - promedio*(high/(100)))-incrementaBanda);
					doubleAux = doubleAux.setScale(2,BigDecimal.ROUND_HALF_UP);
					Double bandaMenor = doubleAux.doubleValue();
					
					if((precioProducto <= bandaMayor)&& (precioProducto >= bandaMenor))
					{
						System.out.println(1);
						mTab.setValue("XX_CanSetDefinitive", "Y");
						return "" ;
					}
					else
					{
						//Si el precio no esta entre las bandas entonces no lo dejo colocar el precio como definitivo
						mTab.setValue("XX_CanSetDefinitive", "N");
						return "Advertencia, el precio debe estar entre las bandas "+ bandaMenor +" y "+ bandaMayor+" Precio BECO bandas "+precio.priceBeco(new BigDecimal(bandaMenor))+" y "+ PrecioBecoRebaja(new BigDecimal(bandaMayor));
					}
			    }
			    
			    if(operador.equals("higher"))
			    {
			    	//Double bandaMayor = (promedio + promedio*(high/(100))+incrementaBanda);
			    	//Double bandaMenor = (promedio + promedio*(low/100))-incrementaBanda;
			    	
			    	BigDecimal doubleAux = new BigDecimal((promedio + promedio*(high/(100))+incrementaBanda));
					doubleAux = doubleAux.setScale(2,BigDecimal.ROUND_HALF_UP);
					Double bandaMayor = doubleAux.doubleValue();
					
					doubleAux = new BigDecimal((promedio + promedio*(low/100))-incrementaBanda);
					doubleAux = doubleAux.setScale(2,BigDecimal.ROUND_HALF_UP);
					Double bandaMenor = doubleAux.doubleValue();
				
					if((precioProducto <= bandaMayor)&&(precioProducto >= bandaMenor))
					{
						System.out.println(2);
						mTab.setValue("XX_CanSetDefinitive", "Y");
						return "" ;
					}
					else
					{
						//Si el precio no esta entre las bandas entonces no lo dejo colocar el precio como definitivo
						mTab.setValue("XX_CanSetDefinitive", "N");
						
						String message = "Advertencia, el precio deberia estar entre las bandas " +
						""+ bandaMenor +" y "+ bandaMayor +"Precio BECO bandas" ;
						//+precio.priceBeco(new BigDecimal(bandaMenor))+" y ";
						//+ PrecioBecoRebaja(new BigDecimal(bandaMayor));
						
						return message;
					}
			    }				
				
			}

			return "";
		}
		catch(Exception e)
		{			
			System.out.println("Error an la base de datos "+e.getMessage());
		} finally {
			DB.closeStatement(pstmtC);
			DB.closeResultSet(rsC);
		}
			
		return "";
	}
	
	public BigDecimal PrecioBecoRebaja (BigDecimal entrada)
	{
		C_OrderCallout precio = new C_OrderCallout();
		//BigDecimal entrada = new BigDecimal(99);
		
		BigDecimal salida = precio.priceBeco(entrada);
		
		//System.out.println("SALIDA NORMAL"+salida);
		
		BigDecimal rebaja=new BigDecimal(0);
		int i = entrada.intValue();
		boolean flag = false;
			while(flag == false)
			//for(int i=0;i<entrada.intValue();i++)
			{	
				if ((precio.priceBeco(new BigDecimal(i)).compareTo(salida)==0) )
				{
					//System.out.println("entro");
					//rebaja = precio.priceBeco(new BigDecimal(i));
					i--;
				}
				else
				{
					rebaja = precio.priceBeco(new BigDecimal(i));
					flag=true;
				}
			}
			return rebaja;
		//System.out.println("SALIDA REBAJA " + rebaja);
	}
	public String priceBeco (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{			
		System.out.println("pricebeco ... ");
		try 
		{
				BigDecimal precio = (BigDecimal)mField.getValue();
				
				String priceRuleSQL = "select xx_lowrank,xx_highrank,xx_termination,xx_increase,xx_infinitevalue from xx_vme_pricerule order by (xx_lowrank)";
				PreparedStatement priceRulePstmt = DB.prepareStatement(priceRuleSQL, null);
				ResultSet priceRuleRs = priceRulePstmt.executeQuery();
	
				Integer precioInt = precio.intValue();
				BigDecimal precioBig = new BigDecimal(precioInt);
				while(priceRuleRs.next())
				{	
					 if(precioBig.compareTo(priceRuleRs.getBigDecimal("xx_lowrank"))>=0 && precioBig.compareTo(priceRuleRs.getBigDecimal("xx_highrank"))<=0) 
				     {
				    	 Integer incremento = priceRuleRs.getInt("xx_increase");
				    	  
				    	 for(Integer i=priceRuleRs.getInt("xx_lowrank")-1;i<=priceRuleRs.getInt("xx_highrank");i=i+incremento)
				    	 {
				    		 BigDecimal var =new BigDecimal(i);
				    		 
				    		 if(precioBig.compareTo(var) <= 0)
				    		 {
				    			  BigDecimal beco=var;
				    			  
				    			 BigDecimal terminacion = priceRuleRs.getBigDecimal("xx_termination");
				    			 if(terminacion.intValue()==0)
				    			 {
				    				 beco = var.add(terminacion);
				    			 }
				    			 else
				    			 {
				    				var = var.divide(new BigDecimal(10));
				    				Integer aux= var.intValue()*10;
				    				beco = new BigDecimal(aux).add(terminacion);
				    			 }
				    			 //mTab.setValue("PriceList", beco);
				    			 priceRuleRs.close();
				 				 priceRulePstmt.close();
				 				 
				 				 if(beco.compareTo(precio)==0)
				 				 {
				 					 return priceBandBeco(ctx,WindowNo,mTab,mField,value,oldValue);
				 					 //return"";
				 				 }
				 				 else
				 				 {
				 					 mTab.setValue("PriceList", beco);
				 						return "";
				 					 //return "Precio Beco Sugerido "+beco.toString();
				 				 }
				    		 }
 
				    	 }
				     }
				}
				priceRuleRs.close();
				priceRulePstmt.close();
				
				return priceBandBeco(ctx,WindowNo,mTab,mField,value,oldValue);
				//return "";
		}		

		catch(Exception e)
		{	
			return e.getMessage();
		}
	}
	
	public String priceBecoGlobal (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue, int option, BigDecimal auxValue)
	{			
		//System.out.println("priceBecoGlobal");
		try 
		{
			BigDecimal precio;
			
			if(option==1)
				precio = (BigDecimal)mTab.getValue("XX_SalePricePlusTax");
			else
				precio = auxValue;
				
				//VME_PriceProductCallout banda = new VME_PriceProductCallout();
				
				String priceRuleSQL = "select xx_lowrank,xx_highrank,xx_termination,xx_increase,xx_infinitevalue from xx_vme_pricerule order by (xx_lowrank)";
				PreparedStatement priceRulePstmt = DB.prepareStatement(priceRuleSQL, null);
				ResultSet priceRuleRs = priceRulePstmt.executeQuery();
	
				Integer precioInt = precio.intValue();
				BigDecimal precioBig = new BigDecimal(precioInt);
				while(priceRuleRs.next())
				{	
					 if(precioBig.compareTo(priceRuleRs.getBigDecimal("xx_lowrank"))>=0 && precioBig.compareTo(priceRuleRs.getBigDecimal("xx_highrank"))<=0) 
				     {
				    	 Integer incremento = priceRuleRs.getInt("xx_increase");
				    	  
				    	 for(Integer i=priceRuleRs.getInt("xx_lowrank")-1;i<=priceRuleRs.getInt("xx_highrank");i=i+incremento)
				    	 {
				    		 BigDecimal var =new BigDecimal(i);
				    		 
				    		 if(precioBig.compareTo(var) <= 0)
				    		 {
				    			  BigDecimal beco=var;
				    			  
				    			 BigDecimal terminacion = priceRuleRs.getBigDecimal("xx_termination");
				    			 if(terminacion.intValue()==0)
				    			 {
				    				 beco = var.add(terminacion);
				    			 }
				    			 else
				    			 {
				    				var = var.divide(new BigDecimal(10));
				    				Integer aux= var.intValue()*10;
				    				beco = new BigDecimal(aux).add(terminacion);
				    			 }
				    			 //mTab.setValue("PriceList", beco);
				    			 priceRuleRs.close();
				 				 priceRulePstmt.close();
				 				 
				 				 if(beco.compareTo(precio)==0 && option!=5)
				 				 {
				 					return "";//ModifyPVP(ctx, WindowNo, mTab, mField,value);
				 				 }
				 				 else
				 				 {
				 					 //Jpires
				 					 //lo llamo desde XX_CreateDiscountPDA. Si es 5 es q lo estoy llamando desde algo que no es un callout x eso no lo seteo en el mTab
				 					 if(option != 5)
				 						 mTab.setValue("XX_SalePricePlusTax", beco);
				 					 
				 					 if(option==1)
				 					 {
					 					// return banda.priceBandBeco(ctx, WindowNo, mTab, mField, value, oldValue);//banda.priceBandBeco(ctx, WindowNo, mTab, mField, value, oldValue);
					 					 return priceBandBeco(ctx, WindowNo, mTab, mField, value, oldValue);//banda.priceBandBeco(ctx, WindowNo, mTab, mField, value, oldValue);
				 					 }
				 					 else
				 					 {
				 						 return beco.toString();
				 					 }
				 				 }
				    		 }
 
				    	 }
				     }
				}
				priceRuleRs.close();
				priceRulePstmt.close();
				
				
				return "";//ModifyPVP(ctx, WindowNo, mTab, mField,value);
		}		

		catch(Exception e)
		{	
			return e.getMessage();
		}
	}
}
