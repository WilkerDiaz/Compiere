package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.ProcessCtl;
import org.compiere.framework.Query;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_PatternOfDiscount;

/**
 * 
 * @author Rosmaira Arvelo
 *
 */
public class XX_GenerationPatternOfDiscount extends SvrProcess {

	int cantInventIni   = 0;	
	int cantVentaMes    = 0;
	int cantInventFin   = 0;
	int cantInvIniDep   = 0;
	int cantVentaMesDep = 0;
	int cantInvFinDep   = 0;
	int diasVida        = 0;
	int quantity        = 0;	
		
	BigDecimal montoInventIni   = new BigDecimal(0);
	BigDecimal montoVentaMes    = new BigDecimal(0);	
	BigDecimal montoInventFin   = new BigDecimal(0);
	BigDecimal cantInventProm   = new BigDecimal(0);
	BigDecimal montoInventProm  = new BigDecimal(0);
	BigDecimal cantRotacion     = new BigDecimal(0);	
	BigDecimal montoInvIniDep   = new BigDecimal(0);
	BigDecimal montoVentaMesDep = new BigDecimal(0);
	BigDecimal montoInvFinDep   = new BigDecimal(0);
	BigDecimal cantInvPromDep   = new BigDecimal(0);
	BigDecimal montoInvPromDep  = new BigDecimal(0);
	BigDecimal cantRotaDep      = new BigDecimal(0);
	BigDecimal amount           = new BigDecimal(0);
	
	Calendar calendario = Calendar.getInstance();
	Date fechaActual = calendario.getTime();
	
	int typeInv = Env.getCtx().getContextAsInt("XX_L_TYPEINVENTORYTENDENCIA_ID");
	
	X_XX_VMR_PatternOfDiscount patternDiscount = new X_XX_VMR_PatternOfDiscount(Env.getCtx(),0,null);
	
	@Override
	protected String doIt() throws Exception 
	{	
		//Obtiene fecha actual del sistema
		calendario.getTime();				
		
		//Si el día es el 1ero del mes le resto uno al mes y calculo la Pauta de Rebajas del mes anterior
		if(calendario.get(Calendar.DAY_OF_MONTH) == 1)
		{
			calendario.add(Calendar.DAY_OF_MONTH, -1);
			fechaActual = calendario.getTime();
		}
		
		int month = 0;
		int year = 0;
		
		month = calendario.get(Calendar.MONTH)+1;
		year = calendario.get(Calendar.YEAR);
		String fecha = "";
		
		if(month<10){
			 fecha = "0"+month+"-"+year;
		}else{
			fecha = ""+month+"-"+year;
		}
		
		String SQLDel = "DELETE FROM XX_VMR_PatternOfDiscount";
			
		try 
		{
			PreparedStatement pstmtDel = DB.prepareStatement(SQLDel, null);
			ResultSet rsDel = pstmtDel.executeQuery();					
			rsDel.close();
			pstmtDel.close();
							
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, SQLDel, e);
			return "";
		}
		
		String SQL = "SELECT SUM(XX_INITIALINVENTORYQUANTITY) AS CANTINI, " 
				   + "SUM(XX_INITIALINVENTORYAMOUNT) AS MONTOINI, " 
				   + "(SUM(XX_INITIALINVENTORYQUANTITY) + SUM(XX_PREVIOUSADJUSTMENTSQUANTITY) + " 
				   + "SUM(XX_SHOPPINGQUANTITY) + SUM(XX_SALESQUANTITY) + SUM(XX_MOVEMENTQUANTITY) + " 
				   + "SUM(XX_ADJUSTMENTSQUANTITY)) AS CANTFIN, (SUM(XX_INITIALINVENTORYAMOUNT) + "
				   + "SUM(XX_PREVIOUSADJUSTMENTSAMOUNT) + SUM(XX_SHOPPINGAMOUNT) + SUM(XX_SALESAMOUNT) + "
				   + "SUM(XX_MOVEMENTAMOUNT) + SUM(XX_AdjustmentsAmount)) AS MONTOFIN, "
				   + "((SUM(XX_INITIALINVENTORYQUANTITY) + SUM(XX_INITIALINVENTORYQUANTITY) + "
				   + "SUM(XX_PREVIOUSADJUSTMENTSQUANTITY) + SUM(XX_SHOPPINGQUANTITY) + SUM(XX_SALESQUANTITY) + "
				   + "SUM(XX_MOVEMENTQUANTITY) + SUM(XX_ADJUSTMENTSQUANTITY))/2) AS CANTPROM, "
				   + "((SUM(XX_INITIALINVENTORYQUANTITY) + SUM(XX_INITIALINVENTORYAMOUNT) + SUM(XX_PREVIOUSADJUSTMENTSAMOUNT) + "
				   + "SUM(XX_SHOPPINGAMOUNT) + SUM(XX_SALESAMOUNT) + SUM(XX_MOVEMENTAMOUNT) + "
				   + "SUM(XX_AdjustmentsAmount))/2) AS MONTOPROM, M_Product_ID "
				   + "FROM XX_VCN_Inventory "
				   + "WHERE XX_INVENTORYMONTH='11' AND XX_INVENTORYYEAR='2009' "//to_char(Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy') "
				   + " GROUP BY M_Product_ID";
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{		// System.out.println(rs.getInt(7));	
				cantInventIni   = rs.getInt(1);
				montoInventIni  = rs.getBigDecimal(2);
				cantInventFin   = rs.getInt(3);
				montoInventFin  = rs.getBigDecimal(4);
				cantInventProm  = rs.getBigDecimal(5);
				montoInventProm = rs.getBigDecimal(6);
				/*System.out.println(cantInventIni);
				System.out.println(montoInventIni);
				System.out.println(cantInventFin);
				System.out.println(montoInventFin);
				System.out.println(cantInventProm);
				System.out.println(montoInventProm);*/
				
				if((rs.getInt(7)!=0)&&(cantInventFin!=0)&&(montoInventFin!=new BigDecimal(0))&&(cantInventProm!=new BigDecimal(0))&&(montoInventProm!=new BigDecimal(0)))
				{
					String SQL2 = "SELECT SUM(XX_SALESQUANTITY), SUM(XX_SALESAMOUNT), M_Product_ID "
					    + "FROM XX_VCN_Inventory "
					    + "WHERE M_Product_ID="+rs.getInt(7)
					    + " AND XX_INVENTORYMONTH='11' AND XX_INVENTORYYEAR='2009' "//to_char(Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy') "
					    + " GROUP BY M_Product_ID";
			
					try
					{
						PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
					    ResultSet rs2 = pstmt2.executeQuery();			
					    
						while (rs2.next())
						{
							cantVentaMes  = rs2.getInt(1);
							montoVentaMes = rs2.getBigDecimal(2);
							/*System.out.println(cantVentaMes);
							System.out.println(montoVentaMes);*/
							
							if((cantVentaMes!=0)&&(montoVentaMes!=new BigDecimal(0)))
							{
								cantRotacion  = new BigDecimal(cantVentaMes*12).divide(cantInventProm,2,RoundingMode.HALF_UP);
								
								MProduct producto = new MProduct(Env.getCtx(),rs.getInt(7),null);
								
								String SQL3 = "SELECT SUM(XX_INITIALINVENTORYQUANTITY) AS CANTINI, "
										    + "SUM(XX_INITIALINVENTORYAMOUNT) AS MONTOINI, "  
										    + "(SUM(XX_INITIALINVENTORYQUANTITY) + SUM(XX_PREVIOUSADJUSTMENTSQUANTITY) + "  
										    + "SUM(XX_SHOPPINGQUANTITY) + SUM(XX_SALESQUANTITY) + SUM(XX_MOVEMENTQUANTITY) + "  
										    + "SUM(XX_ADJUSTMENTSQUANTITY)) AS CANTFIN, (SUM(XX_INITIALINVENTORYAMOUNT) + " 
										    + "SUM(XX_PREVIOUSADJUSTMENTSAMOUNT) + SUM(XX_SHOPPINGAMOUNT) + SUM(XX_SALESAMOUNT) + " 
										    + "SUM(XX_MOVEMENTAMOUNT) + SUM(XX_AdjustmentsAmount)) AS MONTOFIN, " 
										    + "((SUM(XX_INITIALINVENTORYQUANTITY) + SUM(XX_INITIALINVENTORYQUANTITY) + " 
										    + "SUM(XX_PREVIOUSADJUSTMENTSQUANTITY) + SUM(XX_SHOPPINGQUANTITY) + SUM(XX_SALESQUANTITY) + " 
										    + "SUM(XX_MOVEMENTQUANTITY) + SUM(XX_ADJUSTMENTSQUANTITY))/2) AS CANTPROM, "
										    + "((SUM(XX_INITIALINVENTORYQUANTITY) + SUM(XX_INITIALINVENTORYAMOUNT) + SUM(XX_PREVIOUSADJUSTMENTSAMOUNT) + " 
										    + "SUM(XX_SHOPPINGAMOUNT) + SUM(XX_SALESAMOUNT) + SUM(XX_MOVEMENTAMOUNT) + "
										    + "SUM(XX_AdjustmentsAmount))/2) AS MONTOPROM, XX_VMR_Department_ID " 
										    + "FROM XX_VCN_Inventory " 
										    + "WHERE XX_VMR_Department_ID="+producto.getXX_VMR_Department_ID()
										    + " AND XX_INVENTORYMONTH='11' AND XX_INVENTORYYEAR='2009' "//to_char(Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy') "
										    + " GROUP BY XX_VMR_Department_ID";
								
								try
								{
									PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
								    ResultSet rs3 = pstmt3.executeQuery();			
								    
									while (rs3.next())
									{
										cantInvIniDep   = rs3.getInt(1);
										montoInvIniDep  = rs3.getBigDecimal(2);
										cantInvFinDep   = rs.getInt(3);
										montoInvFinDep  = rs.getBigDecimal(4);
										cantInvPromDep  = rs.getBigDecimal(5);
										montoInvPromDep = rs.getBigDecimal(6);
										/*System.out.println(cantInvIniDep);
										System.out.println(montoInvIniDep);
										System.out.println(cantInvFinDep);
										System.out.println(montoInvFinDep);
										System.out.println(cantInvPromDep);
										System.out.println(montoInvPromDep);*/
										
										if((cantInvFinDep!=0)&&(montoInvFinDep!=new BigDecimal(0))&&(cantInvPromDep!=new BigDecimal(0))&&(montoInvPromDep!=new BigDecimal(0)))
										{
											String SQL4 = "SELECT SUM(XX_SALESQUANTITY), SUM(XX_SALESAMOUNT), XX_VMR_Department_ID "
											    + "FROM XX_VCN_Inventory "
											    + "WHERE XX_VMR_Department_ID="+producto.getXX_VMR_Department_ID()
											    + " AND XX_INVENTORYMONTH='11' AND XX_INVENTORYYEAR='2009' "//to_char(Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy') "
											    + " GROUP BY XX_VMR_Department_ID";
									
											try
											{
												PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null); 
											    ResultSet rs4 = pstmt4.executeQuery();			
											    
												while (rs4.next())
												{
													cantVentaMesDep  = rs4.getInt(1);
													montoVentaMesDep = rs4.getBigDecimal(2);
													/*System.out.println(cantVentaMesDep);
													System.out.println(montoVentaMesDep);*/
													
													if((cantVentaMesDep!=0)&&(montoVentaMesDep!=new BigDecimal(0)))
													{
														cantRotaDep = new BigDecimal(cantVentaMesDep*12).divide(cantInvPromDep,2,RoundingMode.HALF_UP);
														//System.out.println(cantRotacion+"rota"+cantRotaDep+"dsssss"+cantRotacion.compareTo(cantRotaDep));
														if(cantRotacion.compareTo(cantRotaDep) < 0)
														{
															int cant  = 0;
															int consecutive = 0;
															BigDecimal monto = new BigDecimal(0);
															int patternDiscountID = 0;											
															
															String SQL5 = "SELECT (SUM(XX_INITIALINVENTORYQUANTITY) + SUM(XX_PREVIOUSADJUSTMENTSQUANTITY) + "  
																		+ "SUM(XX_SHOPPINGQUANTITY) + SUM(XX_SALESQUANTITY) + SUM(XX_MOVEMENTQUANTITY) + " 
																		+ "SUM(XX_ADJUSTMENTSQUANTITY)) AS CANTFIN, M_Product_ID, "
																		+ "XX_VMR_Category_ID, XX_CONSECUTIVEPRICE, M_AttributeSetInstance_ID "
																		+ "FROM XX_VCN_Inventory "  
																		+ "WHERE M_Product_ID="+rs.getInt(7)
																		+ " AND XX_INVENTORYMONTH='11' AND XX_INVENTORYYEAR='2009' "//to_char(Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy') "
																		+ "GROUP BY M_Product_ID, XX_VMR_Category_ID, XX_CONSECUTIVEPRICE, M_AttributeSetInstance_ID";
															
															try
															{
																PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
															    ResultSet rs5 = pstmt5.executeQuery();			
															    
																while (rs5.next())
																{
																	 cant  = rs5.getInt(1);
																	 consecutive = rs5.getInt(4);
																	 
																	 if((cant != 0) && (consecutive != 0))
																	 {
																		 String SQL6 = "SELECT DISTINCT(i.XX_ConsecutivePrice), i.M_Product_ID, "
																			 		 + "i.M_AttributeSetInstance_ID, c.Created, c.XX_SalePrice, "
																			 		 + "m.XX_VMR_TypeInventory_ID, r.XX_Range1, r.XX_Range2, r.XX_Range3 "
																			 		 + "FROM XX_VCN_Inventory i, XX_VMR_PriceConsecutive c, M_Product m, XX_VMR_RangeDaysPromote r "
																			 		 + "WHERE i.M_Product_ID="+rs.getInt(7)
																			 		 + " AND i.M_Product_ID=c.M_Product_ID "
																			 		 + "AND i.M_Product_ID=m.M_Product_ID "
																			 		 + "AND i.M_AttributeSetInstance_ID="+rs5.getInt(5)
																		 			 + " AND i.M_AttributeSetInstance_ID=c.M_AttributeSetInstance_ID "
																		 			 + "AND i.XX_ConsecutivePrice="+rs5.getInt(4)
																		 			 + " AND i.XX_VMR_Category_ID=r.XX_VMR_Category_ID "
																		 			 + "AND m.XX_VMR_TypeInventory_ID=r.XX_VMR_TypeInventory_ID "
																		 			 + "AND i.XX_INVENTORYMONTH='11' AND i.XX_INVENTORYYEAR='2009' ";//to_char(i.Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy')";
																		 
																		 try
																			{
																				PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
																			    ResultSet rs6 = pstmt6.executeQuery();			
																			    
																				if(rs6.next())
																				{
																					monto = rs6.getBigDecimal(5);
																					long fechaInicial = rs6.getDate(4).getTime();
																					long fechaFinal = fechaActual.getTime();
																					long diferencia = fechaFinal - fechaInicial;
																					double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
																					diasVida = (int)dias;
																					/*System.out.println(rs6.getInt(7));
																					System.out.println(rs6.getInt(8));
																					System.out.println(rs6.getInt(9));*/
																					patternDiscountID = getPatternOfDiscount(rs.getInt(7));
																					
																					if(patternDiscountID != 0)
																					{
																						//instancio pauta de rebaja con el id que devolvio
																						patternDiscount = new X_XX_VMR_PatternOfDiscount(Env.getCtx(),patternDiscountID,null);
																					}
																					else
																					{
																						//instancio pauta de rebaja nueva
																						patternDiscount = new X_XX_VMR_PatternOfDiscount(Env.getCtx(),0,null);
																					}
																					
																					if((diasVida >= rs6.getInt(7))&&(diasVida < rs6.getInt(8)))
																					{
																						patternDiscount.setM_Product_ID(producto.get_ID());
																						patternDiscount.setXX_QuantityPromotion20(patternDiscount.getXX_QuantityPromotion20()+cant);
																						patternDiscount.setXX_AmountPromotion20(patternDiscount.getXX_AmountPromotion20().add(monto.multiply(new BigDecimal(cant))));
																						patternDiscount.save();
																					}
																					else if((diasVida >= rs6.getInt(8))&&((rs6.getInt(9)==0)||(diasVida < rs6.getInt(9))))
																					{
																						patternDiscount.setM_Product_ID(producto.get_ID());
																						patternDiscount.setXX_QuantityPromotion30(patternDiscount.getXX_QuantityPromotion30()+cant);
																						patternDiscount.setXX_AmountPromotion30(patternDiscount.getXX_AmountPromotion30().add(monto.multiply(new BigDecimal(cant))));
																						patternDiscount.save();
																					}
																					else if((diasVida >= rs6.getInt(9))&&(rs6.getInt(9) != 0))
																					{
																						patternDiscount.setM_Product_ID(producto.get_ID());
																						patternDiscount.setXX_QuantityPromotion50(patternDiscount.getXX_QuantityPromotion50()+cant);
																						patternDiscount.setXX_AmountPromotion50(patternDiscount.getXX_AmountPromotion50().add(monto.multiply(new BigDecimal(cant))));
																						patternDiscount.save();
																					}
																					
																					if(patternDiscount.get_ID()!=0)
																					{
																						quantity = patternDiscount.getXX_QuantityPromotion20()+patternDiscount.getXX_QuantityPromotion30()+patternDiscount.getXX_QuantityPromotion50();
																						amount   = patternDiscount.getXX_AmountPromotion20().add(patternDiscount.getXX_AmountPromotion30()).add(patternDiscount.getXX_AmountPromotion50());
																																											
																						patternDiscount.setXX_FinalInventoryQuantity(quantity);
																						patternDiscount.setXX_FinalInventoryAmount(amount);
																						patternDiscount.setXX_DATE(fecha);
																						patternDiscount.save();
																					}
				
																				}
																				rs6.close();
																				pstmt6.close();
																			}
																			catch (SQLException e)
																			{
																				e.printStackTrace();
																			}
																	 }
																}
																rs5.close();
																pstmt5.close();
															}
															catch (SQLException e)
															{
																e.printStackTrace();
															}
														}
													}
													else
													{
														int cant  = 0;
														int consecutive = 0;
														BigDecimal monto = new BigDecimal(0);
														int patternDiscountID = 0;
														
														String SQL5 = "SELECT (SUM(i.XX_INITIALINVENTORYQUANTITY) + SUM(i.XX_PREVIOUSADJUSTMENTSQUANTITY) + "  
																	+ "SUM(i.XX_SHOPPINGQUANTITY) + SUM(i.XX_SALESQUANTITY) + SUM(i.XX_MOVEMENTQUANTITY) + " 
																	+ "SUM(i.XX_ADJUSTMENTSQUANTITY)) AS CANTFIN, i.M_Product_ID, "
																	+ "i.XX_VMR_Category_ID, i.XX_CONSECUTIVEPRICE, i.M_AttributeSetInstance_ID "
																	+ "FROM XX_VCN_Inventory i, M_Product m "  
																	+ "WHERE i.M_Product_ID="+rs.getInt(7)
																	+ " AND m.M_Product_ID=i.M_Product_ID "
																	+ "AND m.XX_VMR_TypeInventory_ID="+typeInv
																	+ " AND XX_INVENTORYMONTH='11' AND XX_INVENTORYYEAR='2009' "//to_char(Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy') "
																	+ "GROUP BY M_Product_ID, XX_VMR_Category_ID, XX_CONSECUTIVEPRICE, M_AttributeSetInstance_ID";
														
														try
														{
															PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
														    ResultSet rs5 = pstmt5.executeQuery();			
														    
															while (rs5.next())
															{
																 cant  = rs5.getInt(1);	
																 consecutive = rs5.getInt(4);
																 
																 if((cant != 0) && (consecutive != 0))
																 {
																	 String SQL6 = "SELECT DISTINCT(i.XX_ConsecutivePrice), i.M_Product_ID, "
																		 		 + "i.M_AttributeSetInstance_ID, c.Created, c.XX_SalePrice, "
																		 		 + "m.XX_VMR_TypeInventory_ID, r.XX_Range1, r.XX_Range2, r.XX_Range3 "
																		 		 + "FROM XX_VCN_Inventory i, XX_VMR_PriceConsecutive c, M_Product m, XX_VMR_RangeDaysPromote r "
																		 		 + "WHERE i.M_Product_ID="+rs.getInt(7)
																		 		 + " AND i.M_Product_ID=c.M_Product_ID "
																		 		 + "AND i.M_Product_ID=m.M_Product_ID "
																		 		 + "AND i.M_AttributeSetInstance_ID="+rs5.getInt(5)
																	 			 + " AND i.M_AttributeSetInstance_ID=c.M_AttributeSetInstance_ID "
																	 			 + "AND i.XX_ConsecutivePrice="+rs5.getInt(4)
																	 			 + " AND i.XX_VMR_Category_ID=r.XX_VMR_Category_ID "
																	 			 + "AND m.XX_VMR_TypeInventory_ID=r.XX_VMR_TypeInventory_ID "
																	 			 + "AND i.XX_INVENTORYMONTH='11' AND i.XX_INVENTORYYEAR='2009' ";//to_char(i.Updated,'mmyyyy')=to_char(to_date('"+fecha+"','mm-yyyy'),'mmyyyy')";
																	 
																	 try
																		{
																			PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
																		    ResultSet rs6 = pstmt6.executeQuery();			
																		    
																			if(rs6.next())
																			{
																				monto = rs6.getBigDecimal(5);
																				long fechaInicial = rs6.getDate(4).getTime();
																				long fechaFinal = fechaActual.getTime();
																				long diferencia = fechaFinal - fechaInicial;
																				double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
																				diasVida = (int)dias;
																				
																				patternDiscountID = getPatternOfDiscount(rs.getInt(7));
																				
																				if(patternDiscountID != 0)
																				{
																					//instancio pauta de rebaja con el id que devolvio
																					patternDiscount = new X_XX_VMR_PatternOfDiscount(Env.getCtx(),patternDiscountID,null);
																				}
																				else
																				{
																					//instancio pauta de rebaja nueva;
																					patternDiscount = new X_XX_VMR_PatternOfDiscount(Env.getCtx(),0,null);
																				}
																				
																				if(diasVida > rs6.getInt(9))
																				{
																					patternDiscount.setM_Product_ID(producto.get_ID());
																					patternDiscount.setXX_DATE(fecha);
																					patternDiscount.setXX_QuantityPromotion50(cant);
																					patternDiscount.setXX_AmountPromotion50(monto.multiply(new BigDecimal(cant)));
																					
																					if((patternDiscount.getXX_QuantityPromotion20()!=0)||(patternDiscount.getXX_QuantityPromotion30()!=0)||(patternDiscount.getXX_QuantityPromotion50()!=0))
																					{
																						quantity = patternDiscount.getXX_QuantityPromotion20()+patternDiscount.getXX_QuantityPromotion30()+patternDiscount.getXX_QuantityPromotion50();
																						amount   = patternDiscount.getXX_AmountPromotion20().add(patternDiscount.getXX_AmountPromotion30()).add(patternDiscount.getXX_AmountPromotion50());
																																											
																						patternDiscount.setXX_FinalInventoryQuantity(quantity);
																						patternDiscount.setXX_FinalInventoryAmount(amount);																						
																					}
																					patternDiscount.save();
																				}
			
																			}
																			rs6.close();
																			pstmt6.close();
																		}
																		catch (SQLException e)
																		{
																			e.printStackTrace();
																		}
																 }
															}
															rs5.close();
															pstmt5.close();
														}
														catch (SQLException e)
														{
															e.printStackTrace();
														}
													}
													
												}
												rs4.close();
												pstmt4.close();
											}
											catch (SQLException e)
											{
												e.printStackTrace();
											}
										}
									}
									rs3.close();
									pstmt3.close();
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
							}
						}
						rs2.close();
						pstmt2.close();						
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}	
				}
			}
			rs.close();
			pstmt.close();			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		if(getCountPatternOfDiscount()!=0)
		{
			//Abre la ventana de Pauta de Rebajas
			AWindow window_patternDiscount = new AWindow();
			Query query = Query.getEqualQuery("ISACTIVE", "Y");
			String wind = Env.getCtx().getContext("#XX_L_W_PATTERNOFDISCOUNT_ID");
			Integer win = Integer.parseInt(wind);
			window_patternDiscount.initWindow(win, query);
			AEnv.showCenterScreen(window_patternDiscount);
						
			//LLama al proceso de exportar la data de compiere al as/400 
			MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_PROCESSPATTERNDISCOUNT_ID"), patternDiscount.get_ID()); 
			mpi.save();
			
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_PROCESSPATTERNDISCOUNT_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_PROCESSPATTERNDISCOUNT_ID")); 
			pi.setClassName(""); 
			pi.setTitle(""); 
			
			ProcessCtl pc = new ProcessCtl(null ,pi,null); 
			pc.start();
			
			return "";
		}
		else
		{
			return "No hay Pauta de Rebajas";
		}
	}	
	
	/*
	 *  Obtiene el ID de la Pauta de Rebaja segun el producto
	 */
	private Integer getPatternOfDiscount(Integer product)
	{
		Integer ID = 0;
		
		String SQL = "SELECT * FROM XX_VMR_PatternOfDiscount WHERE M_Product_ID="+product;
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			if(rs.next())
			{
				ID = rs.getInt("XX_VMR_PatternOfDiscount_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return ID;
	}
	
	/*
	 *  Obtiene la cantidad de registros de la Pauta de Rebaja
	 */
	private Integer getCountPatternOfDiscount()
	{
		Integer count = 0;
		
		String SQL = "SELECT COUNT(*) AS Cuenta FROM XX_VMR_PatternOfDiscount";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			if(rs.next())
			{
				count = rs.getInt("Cuenta");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return count;
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
