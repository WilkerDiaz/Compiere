package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.model.MInOut;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_InOut;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.model.MInOutLine;
import compiere.model.cds.MMovement;
import compiere.model.cds.MVLOReturnOfProduct;
import compiere.model.cds.X_Ref_XX_Ref_ContactType;
/**
 * funcion 22 Retiro de Mercancia del Proveedor
 * @author Rebcca Principal 
 * 
 *
 */
public class XX_RemoveMerchandiseProces extends SvrProcess{
	     

	//Persona que  retira la mercancia
	String p_XX_NPerWithdrawsReturn = "";
	//Cedula de la persona que retira la mercancia}
	String p_CIRif = "";
	//numero de la devolucion que se va actualizar
	Date utilDate = new Date(); //fecha actual
	long lnMilisegundos = utilDate.getTime();
	Timestamp fechaActual = new Timestamp(lnMilisegundos);
	static Ctx ctx;
	
	
	@Override
	protected String doIt() throws Exception {
		//System.out.println("Entre al proceso retiro de mercancia");
		//El registro actual de la Devolucion
		MVLOReturnOfProduct RegRetAct = new MVLOReturnOfProduct(getCtx(), getRecord_ID(), null);
		
		Boolean opcion = ADialog.ask(1, new Container(), Msg.getMsg(Env.getCtx(),"ConfirmRemoveMerchandise?"));
		if (opcion)
		{
			String Status = (String)RegRetAct.get_Value("XX_Status");

			if (Status.equals("DPR") || Status.equals("ABA"))
			{
				//actualizar estado de la devolucion a retirado
				RegRetAct.set_Value("XX_Status", "DRE");
				//Colocar boton en tocado
				RegRetAct.set_Value("XX_Remove_Merchandise", "Y");
				//Actualizar fecha de retiro de mercancia
				RegRetAct.setXX_DateDeliveryVendor( fechaActual);
				//setearle el nombre de la persona que retira devolucion
				RegRetAct.set_Value("XX_NPerWithdrawsReturn", p_XX_NPerWithdrawsReturn);
				//setearle el numero de cedula de la persona que retira
				RegRetAct.setXX_CI_RIF(p_CIRif);			
		
			}		


		} 
		else
		{
			return Msg.getMsg(Env.getCtx(),"MerchandiseRetreatCancelled");
		}
		RegRetAct.save();
		
		//Realizado por JTrias
		//Reemplaza codigo de RArvelo (Ahora se realiza con Return to Vendor y no con Sale Orders)
		if(RegRetAct.getXX_ReturnedFrom()!=null)
		{
			if((RegRetAct.getXX_Status().equals("DRE")) && (RegRetAct.getXX_ReturnedFrom().equals("ST")))
			{
				MInOut inOut = new MInOut( getCtx(), 0, get_Trx());
				MMovement movement = new MMovement( getCtx(), RegRetAct.getM_Movement_ID(), null);
				
				inOut.setC_BPartner_ID(RegRetAct.getC_BPartner_ID());
				
				//Busco la ubicacion del proveedor y el representante de ventas
				String SQL = "Select loc.C_BPartner_Location_ID, use.ad_user_id " +
							"from c_bpartner part, C_BPartner_Location loc, AD_User use " +
							"where " +
							"part.c_bpartner_id = "+ RegRetAct.getC_BPartner_ID() +" and " +
							"loc.c_bpartner_id=part.c_bpartner_id and " +
							"use.c_bpartner_id=part.c_bpartner_id and " +
							"use.xx_contacttype=" + X_Ref_XX_Ref_ContactType.SALES.getValue();
				
				PreparedStatement ps = DB.prepareStatement(SQL, get_TrxName());
				ResultSet rs = null;
	
				try {
					rs = ps.executeQuery();

					if(rs.next()){
						
						inOut.setC_BPartner_Location_ID(rs.getInt("C_BPartner_Location_ID"));
						inOut.setAD_User_ID(rs.getInt("ad_user_id"));
					}
   	
				   	rs.close();
				   	ps.close();
	
				} catch (SQLException e) {
					e.printStackTrace();
					try {
						rs.close();
						ps.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
				inOut.setC_DocType_ID(getCtx().getContextAsInt("#XX_L_C_RETURNVENDOR_ID"));
				inOut.setM_Warehouse_ID(movement.getM_WarehouseFrom_ID());
				inOut.setMovementType("V-");
				inOut.save();
				
				SQL = "select " +
				      "M_PRODUCT_ID, XX_TOTALPIECES, m_attributesetinstance_id " +
				      "from xx_vlo_returndetail " +
				      "where XX_VLO_ReturnOfProduct_ID=" + RegRetAct.get_ID();
				      
				PreparedStatement ps1 = DB.prepareStatement(SQL, null);
				ResultSet rs1 = null;
				
				try {
					rs1 = ps1.executeQuery();
			
					int i=0;
					while(rs1.next()){
						
						i=i+10;
						MInOutLine inOutLine = new MInOutLine( getCtx(), 0, get_Trx());
						
						inOutLine.setM_InOut_ID(inOut.getM_InOut_ID());
						inOutLine.setC_UOM_ID(100);
						inOutLine.setLine(i);
						inOutLine.setM_Locator_ID(getCtx().getContextAsInt("#XX_L_LOCATORCDDEVOLUCION_ID"));
						inOutLine.setM_AttributeSetInstance_ID(rs1.getInt("m_attributesetinstance_id"));
						inOutLine.setM_Product_ID(rs1.getInt("M_Product_ID"));
						inOutLine.setMovementQty(new BigDecimal(rs1.getInt("XX_TOTALPIECES")));
						
						//System.out.println("cantidad" + inOutLine.getMovementQty());
			
						inOutLine.save();
					}
   	
				   	rs1.close();
				   	ps1.close();
	
				} catch (SQLException e) {
					e.printStackTrace();
					try {
						rs1.close();
						ps1.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
				inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
				DocumentEngine.processIt(inOut, X_M_InOut.DOCACTION_Complete);
			    inOut.save();
			}
		}
		
		
		/*Realizado por RArvelo
		//Comprueba que el estatus de la devolucion sea retirado por el proveedor y venga de tienda
		if(RegRetAct.getXX_ReturnedFrom()!=null)
		{
			if((RegRetAct.getXX_Status().equals("DRE")) && (RegRetAct.getXX_ReturnedFrom().equals("ST")))
			{
				//Captura del contexto el DocumentType, el Warehouse y el locator devolucion 
				int docType   = Env.getCtx().getContextAsInt("XX_C_DOCTYPEVENDORRETURN_ID");
				int warehouse = Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID");
				int docTypShipment = Env.getCtx().getContextAsInt("XX_L_C_DOCTYPEMMSHIPMENT_ID");
				int docTypCreditMemo = Env.getCtx().getContextAsInt("XX_L_C_DOCTYPECREDITMEMO_ID");
				int locatorDevolucion = Env.getCtx().getContextAsInt("#XX_L_LOCATORCDDEVOLUCION_ID");
				
				Date utilDate = new Date();
				long lnMilisegundos = utilDate.getTime();
				Timestamp fechaActual = new Timestamp(lnMilisegundos);
				
				MWarehouse wh = new MWarehouse(Env.getCtx(),warehouse,null);
				MMovement movement = new MMovement(Env.getCtx(),RegRetAct.getM_Movement_ID(),null);
				
				//Crea una nueva Orden de Venta cuando el proveedor retira los productos devueltos
				//para poder disminuir el inventario
				MOrder sale = new MOrder(Env.getCtx(),0,null);			
				sale.setAD_Org_ID(wh.getAD_Org_ID());
				sale.setDescription("Devolución a Proveedor");
				sale.setIsSOTrx(true);
				sale.setC_DocTypeTarget_ID(docType);
				sale.setC_BPartner_ID(RegRetAct.getC_BPartner_ID());
				sale.setC_BPartner_Location_ID(getLocation(RegRetAct.getC_BPartner_ID()));
				sale.setBill_BPartner_ID(RegRetAct.getC_BPartner_ID());
				sale.setBill_Location_ID(getLocation(RegRetAct.getC_BPartner_ID()));
				sale.setXX_VMR_DEPARTMENT_ID(movement.getXX_VMR_Department_ID());
				sale.setM_Warehouse_ID(warehouse);
				sale.save();
				
				//Busca los productos asociados a la devolucion
				String SQL = "SELECT M_Product_ID, XX_TotalPieces FROM XX_VLO_ReturnDetail " 
					       + "WHERE XX_VLO_ReturnOfProduct_ID="+RegRetAct.get_ID();
		
				try{
							
					PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
					ResultSet rs = pstmt.executeQuery();
								 
					while (rs.next())
					{
						//Crea las lineas de la Orden de Venta
						MOrderLine saleLine = new MOrderLine(Env.getCtx(),0,null);
						saleLine.setM_Warehouse_ID(warehouse);
						saleLine.setAD_Org_ID(wh.getAD_Org_ID());
						saleLine.setC_Order_ID(sale.get_ID());
						saleLine.setDateOrdered(fechaActual);
						saleLine.setM_Product_ID(rs.getInt("M_Product_ID"));
						saleLine.setQtyEntered(rs.getBigDecimal("XX_TotalPieces"));
						saleLine.setC_Tax_ID(getTax(rs.getInt("M_Product_ID")));				
							
							
						//Busca el consecutivo de precio y el costo del producto en el
						String SQL2 = "SELECT pc.XX_PriceConsecutive, pc.XX_UnitPurchasePrice FROM XX_VMR_PriceConsecutive pc, M_MovementLine ml "
							        + "WHERE pc.M_Product_ID=ml.M_Product_ID "
							        + "AND pc.XX_PriceConsecutive=ml.XX_PriceConsecutive "
							        + "AND ml.M_Movement_ID="+RegRetAct.getM_Movement_ID()
							        + " AND ml.M_Product_ID="+rs.getInt("M_Product_ID");
								
						try{
										
							PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
							ResultSet rs2 = pstmt2.executeQuery();
										 
							if(rs2.next())
							{
								saleLine.setXX_PriceConsecutive(rs2.getInt("XX_PriceConsecutive"));
								saleLine.setPrice(rs2.getBigDecimal("XX_UnitPurchasePrice"));
							}						
							rs2.close();
							pstmt2.close();
									   
							}catch (Exception e) {
								log.log(Level.SEVERE, SQL2);
							}								
									
						saleLine.save();
					}
							
					rs.close();
					pstmt.close();
					
					//Completa la Orden de Venta
					sale.setDocAction(X_C_Order.DOCACTION_Complete);
				    sale.processIt (X_C_Order.DOCACTION_Complete);
				    sale.save();
							   
				}
				catch (Exception e) {
					System.out.println(e + " " + SQL);
				}
				
				//Si se generó y completó la Orden de Venta, guardo su ID en la devolución
				if((sale.get_ID()!=0) && (sale.getDocAction().equals("CL")))
				{
					//Guarda la Orden de Venta en la devolución
					RegRetAct.setC_Order_ID(sale.get_ID());
					RegRetAct.save();
					
					//Genera la entrega al proveedor					
					MInOut inOut = new MInOut(Env.getCtx(),0,null);
					inOut.setC_Order_ID(sale.get_ID());
					inOut.setDateOrdered(sale.getDateOrdered());
					inOut.setC_BPartner_ID(sale.getC_BPartner_ID());
					inOut.setC_BPartner_Location_ID(sale.getC_BPartner_Location_ID());
					inOut.setM_Warehouse_ID(sale.getM_Warehouse_ID());				
					inOut.setC_DocType_ID(docTypShipment);
					inOut.setMovementType(Env.getCtx().getContext("#XX_L_MOVETYPECUSTOSHIP_ID"));
					inOut.setIsSOTrx(true);
					inOut.save();
					
					//Genera la Factura de la devolución
					MInvoice invoice = new MInvoice(Env.getCtx(),0,null);
					invoice.setC_Order_ID(sale.get_ID());
					invoice.setDateOrdered(sale.getDateOrdered());
					invoice.setC_BPartner_ID(sale.getC_BPartner_ID());
					invoice.setC_BPartner_Location_ID(sale.getC_BPartner_Location_ID());
					invoice.setXX_VMR_Department_ID(sale.getXX_VMR_DEPARTMENT_ID());
					invoice.setM_Warehouse_ID(sale.getM_Warehouse_ID());
					invoice.setC_DocTypeTarget_ID(docTypCreditMemo);
					invoice.setC_PaymentTerm_ID(sale.getC_PaymentTerm_ID());
					invoice.setC_DocType_ID(docTypCreditMemo);
					invoice.setIsSOTrx(true);
					invoice.setM_PriceList_ID(sale.getM_PriceList_ID());
					invoice.save();
					
					//Busca el detalle de la Orden de Venta
					String SQL3 = "SELECT * FROM C_OrderLine WHERE C_Order_ID="+inOut.getC_Order_ID();
					
					try{
						
						PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
						ResultSet rs3 = pstmt3.executeQuery();
									 
						while (rs3.next())
						{	
							//Genera el detalle de la entrega
							MInOutLine inOutLine = new MInOutLine(Env.getCtx(),0,null);
							inOutLine.setM_InOut_ID(inOut.get_ID());
							inOutLine.setC_OrderLine_ID(rs3.getInt("C_OrderLine_ID"));
							inOutLine.setM_Product_ID(rs3.getInt("M_Product_ID"));
							inOutLine.setM_Locator_ID(locatorDevolucion);
							inOutLine.setC_UOM_ID(100);
							inOutLine.setQty(rs3.getBigDecimal("QtyEntered"));
							
							//Busca el ultimo valor de la linea y le suma 10 para setearselo al nuevo detalle de la entrega
							String SQL4 = "SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_InOutLine WHERE M_InOut_ID="+inOut.get_ID();
							
							try{
								
								PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null); 
								ResultSet rs4 = pstmt4.executeQuery();
											 
								if (rs4.next())
								{
									//Setea el número de línea
									inOutLine.setLine(rs4.getInt("DefaultValue"));
								}
								
								rs4.close();
								pstmt4.close();
							}
							catch (Exception e) {
								System.out.println(e + " " + SQL4);
							}						
							
							//Guarda el detalle de la entrega
							inOutLine.save();
							
							//Si se guardo el detalle de la entrega se genera el detalle de la factura
							if(inOutLine.get_ID()!=0)
							{
								MInvoiceLine invoiceLine = new MInvoiceLine(Env.getCtx(),0,null);
								invoiceLine.setC_Invoice_ID(invoice.get_ID());
								invoiceLine.setM_InOutLine_ID(inOutLine.get_ID());
								invoiceLine.setC_OrderLine_ID(rs3.getInt("C_OrderLine_ID"));
								invoiceLine.setM_Product_ID(rs3.getInt("M_Product_ID"));					
								invoiceLine.setQty(rs3.getBigDecimal("QtyEntered"));
								invoiceLine.setPriceEntered(rs3.getBigDecimal("PriceEntered"));
								invoiceLine.setPriceActual(rs3.getBigDecimal("PriceEntered"));
								invoiceLine.save();
							}
							
						}
						
						rs3.close();
						pstmt3.close();
						
						//Completa la Entrega
						inOut.setDocAction(X_M_InOut.DOCACTION_Complete);
						inOut.processIt (X_M_InOut.DOCACTION_Complete);
						inOut.save();
						
						//Completa la Factura
						invoice.setDocAction(X_C_Invoice.DOCACTION_Complete);
						invoice.processIt (X_C_Invoice.DOCACTION_Complete);
						invoice.save();
								   
					}
					catch (Exception e) {
						System.out.println(e + " " + SQL3);
					}
					
				}
				
			}
		}//fin RArvelo
		*/
		
		return "";
	}

	

	@Override
	protected void prepare() {
		System.out.println("Entre al proceso retiro de mercancia");
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) ;
				else if (name.equals("XX_NPerWithdrawsReturn"))
					p_XX_NPerWithdrawsReturn = element.getParameter().toString();
				else if (name.equals("XX_CI_RIF"))
					p_CIRif = element.getParameter().toString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}
	
	
	/*
	 * RArvelo
	 * Obtiene el location del CBParter Indicado 
	 */
	/*
	private Integer getLocation(Integer CBPartner)
	{
		Integer location=0;
		
		String SQL = "Select C_BPartner_Location_ID FROM C_BPartner_Location " +
					 "WHERE C_BPartner_ID="+CBPartner;
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				location = rs.getInt("C_BPartner_Location_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return location;
	}
	
	/*
	 *  Obtiene el ID del impuesto actual segun el producto
	 */
	/*
	private Integer getTax(Integer product)
	{
		Integer taxid = 0;
		
		String SQL = "SELECT t.C_Tax_ID " 
			+ "FROM C_Tax t, M_Product p " 
			+ "WHERE t.C_TaxCategory_ID=p.C_TaxCategory_ID "
			+ "AND M_Product_ID="+product
			+ " AND t.VALIDFROM = (SELECT MAX(VALIDFROM) FROM C_TAX WHERE C_TaxCategory_ID=p.C_TaxCategory_ID)";
		try
	    {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				taxid = rs.getInt("C_Tax_ID");
			}
			rs.close();
			pstmt.close();
	    }
	    catch (Exception e) {
	    	System.out.println( e.getMessage() );
			e.printStackTrace();
		} 
	    
	    return taxid;
	}// fin RArvelo
	*/
}
