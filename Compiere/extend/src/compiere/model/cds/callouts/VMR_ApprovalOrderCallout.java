package compiere.model.cds.callouts;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.cds.MVMRPOApproval;



public class VMR_ApprovalOrderCallout extends CalloutEngine {

	Date fechaActual = new Date();
	
	
	public String monthLimit (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		try 
		{
			//compiere.model.cds.callouts.VMR_ApprovalOrderCallout.monthLimit en tabla C_Order campo XX_MontLimit
			// XX_VMR_Department_ID EN XX_VMR_PO_APPROVAL
			ADialog.info(1, new Container(), "MustRefresh");
			Date fechaReq = new Date();
			Date fechaAux = new Date();
			String year = null;
			String mount = null;
			String day = null;
			String yearMount = null;
			String yearActual = null;
			String mountActual = null;
			
			
			Integer lineCode, sectionCode, depCode = 0;

			BigDecimal limit = new BigDecimal(0);
			BigDecimal limitTotal = new BigDecimal(0);
			Integer year2, mount2, day2, mountActual2, yearMount2 = 0;
			Integer vtasPtadas, vtasReal, part1,trasfEnv, trasfRec = 0;   
			Integer invFinalProy, invFinalPresu, invenProy, comcolo, ventasPre, rebajasPRe, invFinalProyMes = 0;
			Integer rebajasReal = 0;
			Integer rebajasPre = 0;
			
			
			Integer idLine = 0;
			Integer idSec = 0;
			Integer idDpto = 0;

			Ctx aux = ctx.getCtx(WindowNo);
			String DocumentNo = aux.getContext("DocumentNo");
			String poID = aux.getContext("C_Order_ID").toString();
			Integer poLine = new Integer(poID);
			
			System.out.println("order de compra "+poLine);

		
			String SQL = ("SELECT TO_DATE (TO_CHAR (XX_ARRIVALDATE,'DD/MM/YYYY'), 'DD/MM/YYYY') AS Fecha FROM C_ORDER WHERE DOCUMENTNO = '"+DocumentNo+"'");
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();
		    
		    
		    if (rs.next())
		    {
		    	fechaReq = rs.getDate("Fecha");
		    	//fechaAux = rs.getDate("Fecha");
		    }
		    rs.close();
		    pstmt.close();
		    
		    //String resultado = new String(""+fechaAux);
	    		    			    	
	    	SimpleDateFormat formatoY = new SimpleDateFormat("yyyy");
			year = formatoY.format(fechaReq);
			
			System.out.println(year);
			SimpleDateFormat formatoM = new SimpleDateFormat("MM");
			mount = formatoM.format(fechaReq);
			
			SimpleDateFormat formatoD = new SimpleDateFormat("dd");
			day = formatoD.format(fechaReq);
			
			SimpleDateFormat formatoYa = new SimpleDateFormat("yyyy");
			yearActual = formatoYa.format(fechaActual);
			
			SimpleDateFormat formatoMa = new SimpleDateFormat("MM");
			mountActual = formatoMa.format(fechaActual);
			
			mount2 = new Integer(mount);
			mountActual2 = new Integer(mountActual);
			day2 = new Integer(day);
			year2 = new Integer(year);
			

			/*String SQL1 = ("SELECT DISTINCT C.VALUE AS XX_DEPARTMENTCODE, D.VALUE AS XX_LINECODE, E.VALUE AS XX_SECTIONCODE, B.XX_LINE_ID AS LINE, B.XX_SECTION_ID AS SEC " +
					"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_DEPARTMENT C, XX_VMR_LINE D, XX_VMR_SECTION E " +
					"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
					"AND A.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
					"AND B.XX_LINE_ID = D.XX_VMR_LINE_ID " +
					"AND B.XX_SECTION_ID = E.XX_VMR_SECTION_ID " +
					"AND A.DOCUMENTNO = '"+DocumentNo+"'");*/
			
			
			String SQL1 = ("SELECT DISTINCT C.VALUE AS XX_DEPARTMENTCODE, D.VALUE AS XX_LINECODE, E.VALUE AS XX_SECTIONCODE, B.XX_VMR_LINE_ID AS LINE, B.XX_VMR_SECTION_ID AS SEC, C.XX_VMR_DEPARTMENT_ID AS DPTO " +
					"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B, XX_VMR_DEPARTMENT C, XX_VMR_LINE D, XX_VMR_SECTION E " +
					"WHERE A.C_ORDER_ID = B.C_ORDER_ID " +
					"AND A.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
					"AND B.XX_VMR_LINE_ID = D.XX_VMR_LINE_ID " +
					"AND B.XX_VMR_SECTION_ID = E.XX_VMR_SECTION_ID " +
					"AND A.DOCUMENTNO = '"+DocumentNo+"' AND A.IssoTrx = 'N' ");

			PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null); 
		    ResultSet rs1 = pstmt1.executeQuery();
		 
		    
		    while (rs1.next())
		    {
		    	//X_XX_RequisitionApproval req = new X_XX_RequisitionApproval(ctx, 0, null);
		    	MVMRPOApproval req = new MVMRPOApproval(ctx, poLine, null,0);
		    	//depCode = rs1.getInt("XX_DEPARTMENTCODE");
		    	//lineCode = rs1.getInt("XX_LINECODE");
		    	//sectionCode = rs1.getInt("XX_SECTIONCODE");
		    	idLine = rs1.getInt("LINE");
		    	idSec = rs1.getInt("SEC");
		    	idDpto = rs1.getInt("DPTO");
		    	
		    	//System.out.println(depCode);
		    	//System.out.println(lineCode);
		    	//System.out.println(sectionCode);
		    	System.out.println(idLine);
		    	System.out.println(idSec);
		    	System.out.println(idDpto);
		    	
 	
		    	//String line = new String(""+lineCode);
		    	//String sec = new String(""+sectionCode);
		    	
		    	String SQL2 = ("SELECT SUM (PRICEACTUAL * QTY ) AS RESULTADO " +
		    			"FROM C_ORDER A, XX_VMR_PO_LINEREFPROV B " +
		    			"WHERE B.XX_VMR_LINE_ID = '"+idLine+"' " +
		    			"AND B.XX_VMR_SECTION_ID = '"+idSec+"' " +
		    			"AND B.C_ORDER_ID = A.C_ORDER_ID " +
		    			"AND A.DOCUMENTNO = '"+DocumentNo+"'");
		    	
		    	PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
			    ResultSet rs2 = pstmt2.executeQuery();
			    			  
			    if (rs2.next())
			    {
			    	limit = rs2.getBigDecimal("RESULTADO");
			    
			    				    	
				  		//if (mount2 <= mountActual2 && year.equals(yearActual))
			    		if(fechaReq.compareTo(fechaActual) == -1)
				  		{
						     // *** REBAJAS PRESUPUESTADAS *** //
						    /* String SQL6 = ("SELECT SUM (XX_PROMSALEAMOUNTBUD + XX_AMOUNTSALEFRBUD + XX_FINALSALEAMOUNTBUD) AS SUMRP " +
						     				"FROM XX_VMR_PRLD01 " +
						     				"WHERE XX_CODEDEPARTMENT = '"+depCode+"' AND XX_LINECODE = '"+lineCode+"' AND XX_CODESECTION = '"+sectionCode+"' ");*/
				  			
				  			String SQL6 = ("SELECT SUM (XX_PROMSALEAMOUNTBUD + XX_AMOUNTSALEFRBUD + XX_FINALSALEAMOUNTBUD) AS SUMRP " +
				     				"FROM XX_VMR_PRLD01 " +
				     				"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
				  			
						     
						     PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
						     ResultSet rs6 = pstmt6.executeQuery();
						     
						     // *** REBAJAS REALES *** //
						     String SQL7 = ("SELECT SUM (XX_ACTAMOUNTSALEPROM + XX_AMOUNTSALEFRBUD + XX_FINALACTAMOUNTSALE) AS SUMRR " +
					     				"FROM XX_VMR_PRLD01 " +
					     				"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
						    
						     PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null); 
						     ResultSet rs7 = pstmt7.executeQuery();
						     
						     if (rs6.next())
						     {
						    	 rebajasPre = rs6.getInt("SUMRP"); // REBAJAS PRESUPUESTADAS
						    	 System.out.println("REBAJAS PRESUPUESTADAS "+rebajasPre);
						    	
						     }
						     rs6.close();
							 pstmt6.close();
							 
						     if (rs7.next())
						   	 {
							   	 rebajasReal = rs7.getInt("SUMRR"); // REBAJAS REALES
							   	System.out.println("REBAJAS REALES "+rebajasReal);
						     }
						     rs7.close();
							 pstmt7.close();
							 
						     //if(rebajasPre >= rebajasReal)
						     //{
						    	// CALCULO DE LAS VTAS PTADAS DEL MES EN CURSO
						    	 String SQL8 = ("SELECT  SUM(XX_MONESTIR) AS VTAS_PTADAS FROM XX_VMR_PRLD03 " +
						    	 		"WHERE XX_BUDGETDAY >= '"+day2+"' AND XX_MONTHBUDGET = '"+mount2+"' AND XX_YEARBUDGET = '"+year2+"' " +
						    	 		"AND XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"'");
						    	 
						    	 PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
							     ResultSet rs8 = pstmt8.executeQuery();
							     
							     							     
							     if (rs8.next())
							     {
							    	vtasPtadas = rs8.getInt("VTAS_PTADAS"); // VENTAS PRESUPUESTADAS
							    	System.out.println("VENTAS PRESUPUESTADAS "+ vtasPtadas);
							    	
							    	// BUSQUEDA DE LAS VENTAS REALES DEL MES EN CURSO
							    	String SQL9 = ("SELECT SUM (XX_AMOUNTACTUALSALE) AS ACTSALE FROM XX_VMR_PRLD01 " +
							    			"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
							    	
							    	PreparedStatement pstmt9 = DB.prepareStatement(SQL9, null); 
								    ResultSet rs9 = pstmt9.executeQuery();
								    
								    System.out.println(SQL9);
								    
								    if (rs9.next())
								    {
								    	vtasReal = rs9.getInt("ACTSALE"); // VENTAS REALES
								    	
								    	System.out.println("VENTAS REALES "+ vtasReal);
								    	
								    	String SQL10 = ("SELECT SUM (XX_AMOUNTINIINVEREAL + XX_AMOUNTPLACEDNACCAMPRA + XX_PURCHAMOUNTPLACEDIMPD + XX_PURCHAMOUNTPLADPASTMONTHS + " +
								    			"XX_NACPURCHAMOUNTRECEIVED + XX_PURCHAMOUNTREVIMPD) AS SUMA " +
								    			"FROM XX_VMR_PRLD01 WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' " +
								    			"AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
								    	
								    	PreparedStatement pstmt10 = DB.prepareStatement(SQL10, null); 
									    ResultSet rs10 = pstmt10.executeQuery();
									    
									    if (rs10.next())
									    {
									    	part1 = rs10.getInt("SUMA");
									    	
									    	System.out.println("Parte 1 "+part1);
									    	
									    	// BUSCO LAS Traspasos enviados  Traspasos recibidos   /// 
									    	String SQL11 = ("SELECT XX_TRANSFAMOUNTSENT, XX_TRANSFAMOUNTRECEIVED FROM XX_VMR_PRLD01 " +
									    			"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
									    	
									    	
									    	PreparedStatement pstmt11 = DB.prepareStatement(SQL11, null); 
										    ResultSet rs11 = pstmt11.executeQuery();
										    
										    if (rs11.next())
										    {
										    	trasfEnv = rs11.getInt("XX_TRANSFAMOUNTSENT");
										    	trasfRec = rs11.getInt("XX_TRANSFAMOUNTRECEIVED");
										    	
										    	if(rebajasPre >= rebajasReal)
										    	{
										    		//System.out.println("Entro al if");
										    		
											    	invFinalProy = part1 - vtasReal - vtasPtadas - rebajasPre - trasfEnv + trasfRec; // Calculo del Inventario Final Proyectado
											    			
											    	//System.out.println("Inventario Final Proy "+ invFinalProy);
											    	
											    	String SQL13 = ("SELECT SUM (XX_FINALINVAMOUNTBUD2) AS FINV FROM XX_VMR_PRLD01 " +
											    			"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
											    	
											    	PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null); 
												    ResultSet rs13 = pstmt13.executeQuery();
												    
												    if (rs13.next())
												    {
												    	invFinalPresu = rs13.getInt("FINV"); // Inventario Final Presupuestado
												       	limitTotal = new BigDecimal(invFinalPresu - invFinalProy);
												       	
												    	
												       	//System.out.println("Inv final presupuestado "+invFinalPresu);
												       	//System.out.println("Limite total de presupuesto beco "+limitTotal);
		
												    }
												    rs13.close();
												    pstmt13.close();
												    
											    	
										    	}
										    	else // Calculo para fechas Fututas
										    	{
										    		invFinalProy = part1 - vtasReal - vtasPtadas - rebajasReal - trasfEnv + trasfRec;
											    	
										    		System.out.println("Inventario Final Proy "+ invFinalProy);
										    		
											    	String SQL13 = ("SELECT SUM (XX_FINALINVAMOUNTBUD2) AS FINV FROM XX_VMR_PRLD01 " +
											    			"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
											    	
											    	PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null); 
												    ResultSet rs13 = pstmt13.executeQuery();
												    
												    if (rs13.next())
												    {
												    	invFinalPresu = rs13.getInt("FINV"); // Inventario Final Presupuestado
												       	limitTotal = new BigDecimal(invFinalPresu - invFinalProy);
												       	
												       	System.out.println("Inv final presupuestado "+invFinalPresu);
												       	//System.out.println("Limite total de presupuesto beco "+limitTotal);

												    }
												    rs13.close();
												    pstmt13.close();

										    	}
										    	System.out.println(limit);
										    	System.out.println(limitTotal);
										    	
										    	//if(limit<=limitTotal)
										    	if(limit.compareTo(limitTotal) < 1)
										    	{	
										    		System.out.println("entro a la comparacion");
										    		req.setIsApproved(true);
										    	}
										    	else
										    	{
										    		req.setIsApproved(false);
										    	}
										    	
										    	/*req.setAD_Client_ID(1000005);
										    	req.setAD_Org_ID(1000030);
										    	req.setC_Order_ID(poLine);
										    	req.setXX_Limit(limitTotal);
										    	req.setXX_LimitTotal(limit);
										    	req.setXX_DEPARTMENT(depCode);
										    	req.setXX_Line(lineCode);
										    	req.setXX_SECTION(sectionCode);
										    	req.save();*/

										    	
										    }
										    rs11.close();
										    pstmt11.close();
									    
									    }
									    rs10.close();
									    pstmt10.close();		             
								
								    }
								    rs9.close();
								    pstmt9.close();
							     }
							     rs8.close();
								 pstmt8.close();    	 
						    // }
						    // else
						     //{
						    	 // rebajasPre < rebajasReal
						    	// System.out.println("Entro en el ESLE");
						    	 
						     //}
							
								//req.setAD_Client_ID(1000005);
							    //req.setAD_Org_ID(1000030);
							    //req.setC_Order_ID(poLine);
							    req.setXX_Limit(limitTotal);
							    req.setXX_LimitTotal(limit);
							    req.setXX_VMR_Department_ID(idDpto);
							    req.setXX_VMR_Line_ID(idLine);
							    req.setXX_VMR_Section_ID(idSec);
							    //req.setXX_DEPARTMENT(depCode);
							    //req.setXX_Line(lineCode);
							    //req.setXX_SECTION(sectionCode);
							    req.save();
							  
				  		} // end dei if de fechas 
				  		
				  		
				  		else
				  		{
				  			System.out.println("PARA MESES FUTUROS");
				  			
				  			SimpleDateFormat formatoYM = new SimpleDateFormat("yyyyMM");
							yearMount = formatoYM.format(fechaReq);
							yearMount2 = new Integer(yearMount);
						
							
							//Busqueda del Inventario Final Proyectado del mes anterior
							String SQL14 = ("SELECT SUM (XX_FINALINVAMOUNTBUD2) AS INVFP FROM XX_VMR_PRLD01 WHERE XX_BudgetYearMonth = '"+yearMount2+"' -1 " +
				  					"AND XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
				  			
				  			PreparedStatement pstmt14 = DB.prepareStatement(SQL14, null); 
						    ResultSet rs14 = pstmt14.executeQuery();
						    
						    
						    if (rs14.next())
						    {
						    	invenProy = rs14.getInt("INVFP");
						    	
						    	// CALCULO DE LAS COMPRAS COLOCADAS
						    	String SQL15 = ("SELECT SUM (XX_AMOUNTPLACEDNACCAMPRA + XX_PURCHAMOUNTPLACEDIMPD + XX_PURCHAMOUNTPLADPASTMONTHS) AS COMCOLO " +
						    			"FROM XX_VMR_PRLD01 WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' " +
						    			"AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
						    	
						    	PreparedStatement pstmt15 = DB.prepareStatement(SQL15, null); 
							    ResultSet rs15 = pstmt15.executeQuery();
							    
							    
							    if (rs15.next())
							    {
							    	comcolo = rs15.getInt("COMCOLO");
							    	
							    	// VENTAS PRESUPUESTADAS DEL MES
							    	String SQL16 = ("SELECT SUM (XX_SALESAMOUNTBUD2) AS SALES FROM  XX_VMR_PRLD01 " +
							    			"WHERE XX_BudgetYearMonth >= '"+yearMount2+"' AND XX_VMR_DEPARTMENT_ID = '"+idDpto+"' " +
							    			"AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
							    	
							    	PreparedStatement pstmt16 = DB.prepareStatement(SQL16, null); 
								    ResultSet rs16 = pstmt16.executeQuery();
								   
							    	
								    if (rs16.next())
								    {
								    	ventasPre = rs16.getInt("SALES");
								    	
								    	// *** REBAJAS PRESUPUESTADAS *** //
									     String SQL17 = ("SELECT SUM (XX_PROMSALEAMOUNTBUD + XX_AMOUNTSALEFRBUD + XX_FINALSALEAMOUNTBUD) AS SUMRP2 " +
									     				"FROM XX_VMR_PRLD01 " +
									     				"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' AND XX_BudgetYearMonth >= '"+yearMount2+"' ");
									     
									     PreparedStatement pstmt17 = DB.prepareStatement(SQL17, null); 
									     ResultSet rs17 = pstmt17.executeQuery();
									     
									     
									     if (rs17.next())
									     {
									    	 rebajasPRe = rs17.getInt("SUMRP2");
									    	 
									    	 if (invenProy < 0)
									    	 {
									    		 invenProy = 0;
									    		 invFinalProyMes = invenProy + comcolo - ventasPre - rebajasPRe;
									    		 
									    		 String SQL13 = ("SELECT SUM (XX_FINALINVAMOUNTBUD2) AS FINV FROM XX_VMR_PRLD01 " +
											    			"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
											    	
											    	PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null); 
												    ResultSet rs13 = pstmt13.executeQuery();
												   
												    
												    if (rs13.next())
												    {
												    	invFinalPresu = rs13.getInt("FINV"); // Inventario Final Presupuestado
												    	limitTotal = new BigDecimal(invFinalPresu - invFinalProyMes);

												    }
												    rs13.close();
												    pstmt13.close();
									    		 
									    	 }
									    	 else
									    	 {
									    		 invFinalProyMes = invenProy + comcolo - ventasPre - rebajasPRe;
									    		 
									    		 String SQL13 = ("SELECT SUM (XX_FINALINVAMOUNTBUD2) AS FINV FROM XX_VMR_PRLD01 " +
											    			"WHERE XX_VMR_DEPARTMENT_ID = '"+idDpto+"' AND XX_VMR_LINE_ID = '"+idLine+"' AND XX_VMR_SECTION_ID = '"+idSec+"' ");
											    	
											    	PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null); 
												    ResultSet rs13 = pstmt13.executeQuery();
												   
												    
												    if (rs13.next())
												    {
												    	invFinalPresu = rs13.getInt("FINV"); // Inventario Final Presupuestado
												    	limitTotal = new BigDecimal(invFinalPresu - invFinalProyMes);

												    	
												    }
												    rs13.close();
												    pstmt13.close();

									    	 }
									   
									    	 	//if(limit<=limitTotal)
									    	 	if(limit.compareTo(limitTotal) < 1)
										    	{
										    		req.setIsApproved(true);
										    	}
										    	else
										    	{
										    		req.setIsApproved(false);
										    	}
									    	 	
									    	 	/*req.setAD_Client_ID(1000005);
									    	 	req.setAD_Org_ID(1000030);
									    	 	req.setC_Order_ID(poLine);
									    	 	req.setXX_Limit(limitTotal);
										    	req.setXX_LimitTotal(limit);
									    	 	req.setXX_DEPARTMENT(depCode);
										    	req.setXX_Line(lineCode);
										    	req.setXX_SECTION(sectionCode);
										    	req.save();*/

									     }
									     rs17.close();
										 pstmt17.close();
			
								    }
								    rs16.close();
								    pstmt16.close();
		
							    }
							    rs15.close();
							    pstmt15.close();
					
						    }
						    rs14.close();
						    pstmt14.close();
				  		}
				    /*}
				    rs5.close();
				    pstmt5.close();*/
				    
			    }
			    rs2.close();
			    pstmt2.close();
			    
			    //req.setAD_Client_ID(1000005);
	    	 	//req.setAD_Org_ID(1000030);
	    	 	//req.setC_Order_ID(poLine);
	    	 	req.setXX_Limit(limitTotal);
		    	req.setXX_LimitTotal(limit);
		    	req.setXX_VMR_Department_ID(idDpto);
				req.setXX_VMR_Line_ID(idLine);
				req.setXX_VMR_Section_ID(idSec);
		    	req.save();
			      	
		    } // end while 
		    rs1.close();
		    pstmt1.close();  
		

		return "";
		}
		catch(Exception e) 
		{  
			return e.getMessage(); 
		}
	} // end
}
