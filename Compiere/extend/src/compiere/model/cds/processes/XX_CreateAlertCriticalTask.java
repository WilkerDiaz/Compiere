package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;

import org.compiere.model.MCampaign;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVLOBoardingGuide;
import compiere.model.cds.MVLOUnsolicitedProduct;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRCriticalTasks;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Package;

/**
 *
 *  @author     Rebecca Principal y Rosmaira Arvelo
 *  @version    
 */
public class XX_CreateAlertCriticalTask extends SvrProcess 
{
	String typeAlertVP, typeAlertAP, typeAlertAP1, typeAlertAS, typeAlertAC, typeAlertAO, typeAlertAO1, typeAlertOR, typeAlertCO, typeAlertCE, typeAlertEP, typeAlertEP1, typeAlertEP2, typeAlertCF; 
	int unsolProd, poProdDistri, distProdDet, refAssocia, product, order, orderA, orderR, inOut, orderEP, orderEP1, orderEP2, boardingGuide, orderCE;
	
	@Override
	protected String doIt() throws Exception {
		
		Integer taskID=0;
		
		//Captura variables globales		
		typeAlertAP  = Env.getCtx().getContext("#XX_TypeAlertAP");
		typeAlertAP1 = Env.getCtx().getContext("#XX_TypeAlertAP1");
		typeAlertAS  = Env.getCtx().getContext("#XX_TypeAlertAS");
		typeAlertVP  = Env.getCtx().getContext("#XX_TypeAlertVP");
		typeAlertAC  = Env.getCtx().getContext("#XX_TypeAlertAC");
		typeAlertAO  = Env.getCtx().getContext("#XX_TypeAlertAO");
		typeAlertAO1 = Env.getCtx().getContext("#XX_TypeAlertAO1");
		typeAlertOR  = Env.getCtx().getContext("#XX_TypeAlertOR");
		typeAlertCO  = Env.getCtx().getContext("#XX_TypeAlertCO");
		typeAlertEP  = Env.getCtx().getContext("#XX_TypeAlertEP");
		typeAlertEP1 = Env.getCtx().getContext("#XX_TypeAlertEP1");
		typeAlertEP2 = Env.getCtx().getContext("#XX_TypeAlertEP2");
		typeAlertCF  = Env.getCtx().getContext("#XX_TypeAlertCF");
		typeAlertCE  = Env.getCtx().getContext("#XX_TypeAlertCE");	
		
		poProdDistri  = Env.getCtx().getContextAsInt("#XX_POProdDistriCT");
		distProdDet   = Env.getCtx().getContextAsInt("#XX_DistProdDetCT");
		refAssocia = Env.getCtx().getContextAsInt("#XX_RefAssociaCT");
		unsolProd     = Env.getCtx().getContextAsInt("#XX_UnsolProdCT");
		product       = Env.getCtx().getContextAsInt("#XX_ProductCT");
		order         = Env.getCtx().getContextAsInt("#XX_OrderCT");
		orderA        = Env.getCtx().getContextAsInt("#XX_OrderACT");
		orderR        = Env.getCtx().getContextAsInt("#XX_OrderRCT");
		inOut         = Env.getCtx().getContextAsInt("#XX_InOutCT");
		orderEP       = Env.getCtx().getContextAsInt("#XX_OrderEPCT");
		orderEP1      = Env.getCtx().getContextAsInt("#XX_OrderEP1CT");
		orderEP2      = Env.getCtx().getContextAsInt("#XX_OrderEP2CT");
		boardingGuide = Env.getCtx().getContextAsInt("#XX_BoardingGuideCT");
		orderCE       = Env.getCtx().getContextAsInt("#XX_OrderCECT");			
		
		//Cierra variables globales
		Env.getCtx().remove("#XX_TypeAlertAP");
		Env.getCtx().remove("#XX_TypeAlertAP1");
		Env.getCtx().remove("#XX_TypeAlertAS");
		Env.getCtx().remove("#XX_TypeAlertVP");
		Env.getCtx().remove("#XX_TypeAlertAC");
		Env.getCtx().remove("#XX_TypeAlertAO");
		Env.getCtx().remove("#XX_TypeAlertAO1");
		Env.getCtx().remove("#XX_TypeAlertOR");
		Env.getCtx().remove("#XX_TypeAlertCO");
		Env.getCtx().remove("#XX_TypeAlertEP");
		Env.getCtx().remove("#XX_TypeAlertEP1");
		Env.getCtx().remove("#XX_TypeAlertEP2");
		Env.getCtx().remove("#XX_TypeAlertCF");
		Env.getCtx().remove("#XX_TypeAlertCE");

		Env.getCtx().remove("#XX_POProdDistriCT");
		Env.getCtx().remove("#XX_DistProdDetCT");
		Env.getCtx().remove("#XX_RefAssociaCT");
		Env.getCtx().remove("#XX_UnsolProdCT");		
		Env.getCtx().remove("#XX_ProductCT");
		Env.getCtx().remove("#XX_OrderCT");
		Env.getCtx().remove("#XX_OrderACT");
		Env.getCtx().remove("#XX_OrderRCT");
		Env.getCtx().remove("#XX_InOutCT");
		Env.getCtx().remove("#XX_OrderEPCT");
		Env.getCtx().remove("#XX_OrderEP1CT");
		Env.getCtx().remove("#XX_OrderEP2CT");
		Env.getCtx().remove("#XX_BoardingGuideCT");
		Env.getCtx().remove("XX_OrderCECT");				
				
		if(typeAlertAP.equals("AP"))
		{
			//Crea tarea critica para Asignacion de precio de ventas a un producto en la Distribucion 	
			taskID = getCriticalTask_ID("Asignación de precios de venta");

			MVMRDistributionHeader PAsigPrec= new MVMRDistributionHeader(Env.getCtx(),poProdDistri,null);
			MOrder order = new MOrder(Env.getCtx(),PAsigPrec.getC_Order_ID(),null);
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
			Integer manager = getUserBuyer(order.getXX_VMR_DEPARTMENT_ID());
			Integer supervisor = getCategoryManager(order.getXX_Category_ID());
			Integer id = PAsigPrec.get_ID();
			String assoId = id.toString();
			
			if((PAsigPrec.isXX_Alert()==false) && PAsigPrec.getXX_DistributionStatus().equals("FP"))
			{
				if(generatedAlert(taskID,"AP",PAsigPrec.get_ID(),manager,supervisor,assoId,"Departamento: " + dep.getValue() + "-" + dep.getName()))
				{
//					sendMail(manager,-1);
					
					PAsigPrec.setXX_Alert(true); 
					PAsigPrec.save();			
					
				}
				else
				{
					
				}
			}
			
			// Creado por Victor Lo Monaco
			// Envia correo al jefe de categoría correspondiente (solicitado por RMANTELLINI)
				
/**			X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), null);
			X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
			MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
			String Mensaje = Msg.getMsg( getCtx(), "XX_AssignPrice", new String[]{order.getDocumentNo(),vendor.getName(),dep.getValue(), collection.getName(), ""+PAsigPrec.get_ID()});
						
			Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_PO_READY_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, supervisor, null);
			f.ejecutarMail();  
			f = null;
	*/	
			
		}
		if(typeAlertAP1.equals("AP1"))
		{
			//Crear tarea critica para Asignacion de precio de ventas a un producto en la Distribucion con orden de compra
			taskID= getCriticalTask_ID("Asignación de precios de venta");
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			
			MVMRDistributionHeader PAsigPrec= new MVMRDistributionHeader(Env.getCtx(),distProdDet,null);
			Integer id = PAsigPrec.get_ID();
			String assoId = id.toString();		
			
			if((PAsigPrec.isXX_Alert2()==false)&& (PAsigPrec.getXX_DistributionStatus().equals("FP")))
			{
				if(generatedAlert(taskID,"AP1",PAsigPrec.get_ID(),critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),assoId,"Departamento: " + PAsigPrec.getXX_Department_Name() ))
				{
//					sendMail(-1,critTask.getXX_AssociateManager_ID());
					
					PAsigPrec.setXX_Alert2(true); 
					PAsigPrec.save();
				}				
				else
				{
					
				}
				
			}
			
			// por cada departamento implicado en la distribucion buscamos su jefe de categoria y le enviamos el correo
			String sql_asociadas = 
				" SELECT distinct(xx_vmr_department_id) " +
				" FROM XX_VMR_DistributionDetail d, xx_vmr_category c " +
				" WHERE XX_VMR_DistributionHeader_ID=" + id;
			
			try {
				PreparedStatement ps = DB.prepareStatement(sql_asociadas,null);
				ResultSet rs = ps.executeQuery();
				while (rs.next() ) { 
					MVMRDepartment dep2 = new MVMRDepartment(getCtx(), rs.getInt(1), null);
					MVMRCategory cat = new MVMRCategory(getCtx(), dep2.getXX_VMR_Category_ID(), null);
					Integer jefeCategoria = getCategoryManager(cat.getXX_CategoryManager_ID());
//					String Mensaje = Msg.getMsg( getCtx(), "XX_AssignPriceNotOrder", new String[]{""+PAsigPrec.get_ID()});
					MBPartner vendor = new MBPartner(getCtx(), PAsigPrec.getC_BPartner_ID(), null);
					MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), PAsigPrec.getXX_VMR_Department_ID(), null);
					String mensaje = Msg.getMsg( getCtx(), "XX_AssignPriceNotOrder",
								new String[]{vendor.getName(), 
									dep.getValue() + " " + dep.getName(), ""+PAsigPrec.get_ID()});
							
/**					Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_PO_READY_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, jefeCategoria, null);
					f.ejecutarMail();  
					f = null;					
*/
				}
								
				rs.close();
				ps.close();
			} catch (SQLException e){
				e.printStackTrace();
				ADialog.error(1, new Container(), "XX_DatabaseAccessError");				
			}
			
			
			
			
			
		}
		if(typeAlertAS.equals("AS"))
		{
			//Crear tarea critica para Asociacion de los productos a una distribucion 
			taskID= getCriticalTask_ID("Codificación de los productos");
			 
			MOrder order = new MOrder(Env.getCtx(),refAssocia,null);
			Integer manager = getUserBuyer(order.getXX_VMR_DEPARTMENT_ID());
			Integer supervisor = getCategoryManager(order.getXX_Category_ID());
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(),order.getXX_VMR_DEPARTMENT_ID(),null);
			
			if((order.isXX_Alert9()==false)){
				
				if(generatedAlert(taskID,"AS",order.get_ID(),manager,supervisor,order.getDocumentNo(),"Orden: " + order.getDocumentNo() + ", Departamento: " + dep.getValue()+"-"+dep.getName() ))
				{
//					sendMail(manager,-1);
					
					order.setXX_Alert9(true); 
					order.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertVP.equals("VP"))
		{
			//Crea tarea critica para validar un producto no solicitado
			taskID= getCriticalTask_ID("Confirmación de codificación de los productos en línea genérica");
			
			MVLOUnsolicitedProduct PNoSol= new MVLOUnsolicitedProduct(Env.getCtx(),unsolProd,null);
			MOrder order = new MOrder(Env.getCtx(),PNoSol.getC_Order_ID(),null);
			Integer manager = getUserBuyer(order.getXX_VMR_DEPARTMENT_ID());
			Integer supervisor = getCategoryManager(order.getXX_Category_ID());
			
			if((PNoSol.isXX_Alert()==false)&&(PNoSol.isXX_ValidateProduct()==false))
			{
				if(generatedAlert(taskID,"VP",PNoSol.get_ID(),manager,supervisor,PNoSol.getValue(),"Orden: " + order.getDocumentNo()))
				{					
					PNoSol.setXX_Alert(true); 
					PNoSol.save();					
					
				}
				else
				{
					
				}
				
			}
		}
		if(typeAlertAC.equals("AC"))
		{
			// Comentado porque por los momentos ordenmaron suspender codigo arancelario como tarea critica
			//Crear tarea critica  para Asignacion de codigo arancelario
/**			taskID= getCriticalTask_ID("Asignación de código arancelario");
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			MProduct PAsigCod= new MProduct(Env.getCtx(),product,null);
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(),PAsigCod.getXX_VMR_Department_ID(),null);
			
			if((PAsigCod.isXX_Alert()==false)&&((PAsigCod.getXX_CodeTariff().equals("")||(PAsigCod.getXX_CodeTariff().equals(null)))))
			{
				if(generatedAlert(taskID,"AC",product,critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),PAsigCod.getValue(),"Producto: "+ PAsigCod.getValue()+", Departamento: " + dep.getValue()+"-"+dep.getName()))
				{					
					PAsigCod.setXX_Alert(true); 
					PAsigCod.save();				
					
				}
				else
				{
					
				}
			}
			*/
		}
		if(typeAlertAO.equals("AO"))
		{
			//Crear tarea critica Aprobacion de una Oden de Compra 
			taskID= getCriticalTask_ID("Aprobación de Orden de Comprasin sobregiro"); 
			
			MOrder Order= new MOrder(Env.getCtx(),order,null);
			Integer manager = getPlanner(Order.getXX_VMR_DEPARTMENT_ID());
			Integer supervisor = getHeadOfPlanning(manager);
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(),Order.getXX_VMR_DEPARTMENT_ID(),null);
			
			if(Order.getXX_Alert3()==false)
			{
				if(generatedAlert(taskID,"AO",Order.get_ID(),manager,supervisor,Order.getDocumentNo(),"Departamento: " + dep.getValue()))
				{					
					Order.setXX_Alert3(true); 
					Order.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertAO1.equals("AO1"))
		{
			//Crear tarea critica Aprobacion de una Oden de Compra 
			taskID= getCriticalTask_ID("Aprobación de Orden de Compra con sobregiro");
			
			MOrder Order= new MOrder(Env.getCtx(),orderA,null);
			Integer manager = getPlanner(Order.getXX_VMR_DEPARTMENT_ID());
			Integer manager2 = getCategoryManager(Order.getXX_Category_ID());
			Integer supervisor = getHeadOfPlanning(manager);
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(),Order.getXX_VMR_DEPARTMENT_ID(),null);
			
			if(Order.isXX_Alert7()==false)
			{
				if(generatedAlert(taskID,"AO1",Order.get_ID(),manager2,supervisor,Order.getDocumentNo(),dep.getValue()))
				{					
					Order.setXX_Alert7(true); 
					Order.save();					
					
				}
			}
		}
		if(typeAlertOR.equals("OR"))
		{
			//Crear tarea critica para Orden de Compra por Recibirse
			taskID= getCriticalTask_ID("Orden de compra por recibirse");
			
			MOrder Order= new MOrder(Env.getCtx(),orderR,null);
			Integer manager = getUserBuyer(Order.getXX_VMR_DEPARTMENT_ID());
			Integer supervisor = getCategoryManager(Order.getXX_Category_ID());
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(),Order.getXX_VMR_DEPARTMENT_ID(),null);
			
			if((Order.get_ValueAsBoolean("XX_Alert5")==false))
			{
				//System.out.println("Entre alertas "+actividad);
				if(generatedAlert(taskID,"OR",Order.get_ID(),manager,supervisor,Order.getDocumentNo(),"Orden: " + Order.getDocumentNo() + ", Departamento: " + dep.getValue()+"-"+dep.getName()))
				{
//					sendMail(manager,-1);
					
					Order.setXX_Alert5(true); 
					Order.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertCO.equals("CO"))
		{
			//Crear tarea critica Oden de Compra Pendiente por Chequear
			taskID= getCriticalTask_ID("Orden de compra pendiente por chequear");
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			
			MInOut inout = new MInOut(Env.getCtx(),inOut,null);			
			MOrder Order= new MOrder(Env.getCtx(),inout.getC_Order_ID(),null); 
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(),Order.getXX_VMR_DEPARTMENT_ID(),null);
			
			if((Order.get_ValueAsBoolean("XX_Alert4")==false)&& (Order.getXX_OrderStatus().equals("RE")))
			{
				if(generatedAlert(taskID,"CO",inout.get_ID(),critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),Order.getDocumentNo(),"Order: " + Order.getDocumentNo() + ", Departamento:" + dep.getValue()+"-"+dep.getName()))
				{
//					sendMail(-1,critTask.getXX_AssociateManager_ID());
					
					Order.setXX_Alert4(true); 
					Order.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertEP.equals("EP"))
		{
			//Crear tarea critica pedidos Pendiente por Etiquetar
			taskID= getCriticalTask_ID("Pedido pendiente por etiquetar");
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			
			MOrder Order= new MOrder(Env.getCtx(),orderEP,null);
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), Order.getXX_VMR_DEPARTMENT_ID(), null);
			
			if((Order.get_ValueAsBoolean("XX_Alert8")==false)&& (Order.getXX_OrderStatus().equals("AP")))
			{
				if(generatedAlert(taskID,"EP",Order.get_ID(),critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),Order.getDocumentNo(),"Orden: " + Order.getDocumentNo() + ", Departamento: " + dep.getValue() + "-" + dep.getName()))
				{
//					sendMail(-1,critTask.getXX_AssociateManager_ID());
					
					Order.setXX_Alert8(true); 
					Order.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertEP1.equals("EP1"))
		{
			//Crear tarea critica pedidos Pendiente por Etiquetar
			taskID= getCriticalTask_ID("Pedido pendiente por etiquetar");
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			MVMRDistributionHeader distribution= new MVMRDistributionHeader(Env.getCtx(),orderEP1,null);
			
			Integer count = getPendingLabelCount(distribution.getC_Order_ID());			
			Integer id = distribution.get_ID();
			String assoId = id.toString();
			
			MOrder Order= new MOrder(Env.getCtx(),distribution.getC_Order_ID(),null);
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), Order.getXX_VMR_DEPARTMENT_ID(), null);
			
			if((count!=0)&&(distribution.isXX_Alert3()==false))
			{
				if(generatedAlert(taskID,"EP1",distribution.get_ID(),critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),assoId,"Orden: " + Order.getDocumentNo() + ", Departamento: " + dep.getValue() + "-" + dep.getName()))
				{
//					sendMail(-1,critTask.getXX_AssociateManager_ID());
					
					distribution.setXX_Alert3(true); 
					distribution.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertEP2.equals("EP2"))
		{
			//Crear tarea critica pedidos Pendiente por Etiquetar
			taskID= getCriticalTask_ID("Pedido pendiente por etiquetar");
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			MVMRDistributionHeader distribution= new MVMRDistributionHeader(Env.getCtx(),orderEP2,null);
			
			Integer count = getPendingLabelCDCount(distribution.get_ID());	
			Integer id = distribution.get_ID();
			String assoId = id.toString();
			
//			MOrder Order= new MOrder(Env.getCtx(),distribution.getC_Order_ID(),null);
//			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), Order.getXX_VMR_DEPARTMENT_ID(), null);
			
			if((count!=0)&&(distribution.isXX_Alert3()==false))
			{
				if(generatedAlert(taskID,"EP2",distribution.get_ID(),critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),assoId,"Departamento: " + distribution.getXX_Department_Name()))
				{
//					sendMail(-1,critTask.getXX_AssociateManager_ID());
					
					distribution.setXX_Alert3(true); 
					distribution.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertCF.equals("CF"))
		{
			//Crear tarea critica Calculo de Factores
			taskID= getCriticalTask_ID("Cálculo de factores"); 
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			
			MVLOBoardingGuide BoardingGuide = new MVLOBoardingGuide(Env.getCtx(),boardingGuide,get_Trx());
			
			if((BoardingGuide.getXX_DAPRELIQUIDARECEPREALDATE()!=null)&&(BoardingGuide.get_ValueAsBoolean("XX_Alert")==false))
			{
				if(generatedAlert(taskID,"CF",BoardingGuide.get_ID(),critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),BoardingGuide.getValue(),"Guía de Embarque: " + BoardingGuide.getValue()))
				{
//					sendMail(-1,critTask.getXX_AssociateManager_ID());
					
					BoardingGuide.setXX_Alert(true); 
					BoardingGuide.save();					
					
				}
				else
				{
					
				}
			}
		}
		if(typeAlertCE.equals("CE"))
		{
			//Crear tarea critica para Cierre de Expendiente y ajustes de carga en Transito
			taskID= getCriticalTask_ID("Cierre de expediente y ajuste de carga en tránsito");
			MVMRCriticalTasks critTask = new MVMRCriticalTasks(Env.getCtx(), taskID, null);
			
			MOrder Order= new MOrder(Env.getCtx(),orderCE,null);
			MVLOBoardingGuide boardingGuide = new MVLOBoardingGuide(Env.getCtx(),Order.getXX_VLO_BOARDINGGUIDE_ID(),null);
			
			if((Order.get_ValueAsBoolean("XX_Alert6")==false)&&(Order.getXX_OrderStatus().equals("RE")&&(Order.getC_Country_ID()!=339)))
			{
				if(generatedAlert(taskID,"CE",Order.getXX_VLO_BOARDINGGUIDE_ID(),critTask.getXX_AssociateManager_ID(),critTask.getXX_AssociateSupervisor_ID(),boardingGuide.getValue(),"Guía de Embarque: " + boardingGuide.getValue()))
				{
//					sendMail(-1,critTask.getXX_AssociateManager_ID());
					
					Order.setXX_Alert6(true); 
					Order.save();					
					
				}
				else
				{
					
				}
			}
		}
		
		//CreateAutoAlertCriticalTask();
		return "";
	}	
	
	//Crea el registro de la alerta critica
	public boolean generatedAlert(Integer taskID, String typeTask, int id, Integer manager, Integer supervisor, String associatedValue, String extra)
	{
		MVMRCriticalTaskForClose Task= new MVMRCriticalTaskForClose(Env.getCtx(), 0, null);
		Task.setXX_StatusCriticalTask("Act");
		Task.setXX_VMR_CriticalTasks_ID(taskID);
		Task.setXX_TypeTask(typeTask);
		Task.setXX_PWFValProd("N");
		Task.setXX_ActualRecord_ID(id);
		Task.setXX_Value(associatedValue);	

		Task.setXX_DepartmentNumber(extra);
		
		//Si la tarea critica debe realizarse por un usuario específico
		if((typeTask.equals("AP"))||(typeTask.equals("AS"))||(typeTask.equals("VP"))||(typeTask.equals("AO"))||(typeTask.equals("AO1"))||(typeTask.equals("OR")))
		{
			Task.setXX_AssociateManager_ID(manager);
			Task.setXX_AssociateSupervisor_ID(supervisor);			
		}
		else //Si la tarea critica debe realizarse por un rol especifico
		{
			Task.setXX_ManagerRole_ID(manager);
			Task.setXX_SupervisorRole_ID(supervisor);			
		}
		
		Task.save();
		
		//Comprueba que la tarea crítica se haya creado
		if(Task.get_ID()!=0)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
    /*
	 *	Obtengo el ID de la tarea critica segun el tipo
	 */
	private Integer getCriticalTask_ID(String type){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTasks_ID FROM XX_VMR_CriticalTasks "
				   + "WHERE XX_Task='"+type+"'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				criticalTask = rs.getInt("XX_VMR_CriticalTasks_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return criticalTask;
	}//fin getCriticalTask_ID RArvelo
			
	/*
	 * Obtiene el UserBuyer segun el departamento
	 */
	private Integer getUserBuyer(Integer depart)
	{
		Integer manager=0;
		
		String SQL = "SELECT AD_User_ID " 
				   + "FROM AD_User "
				   + "WHERE C_BPartner_ID = (SELECT XX_UserBuyer_ID " 
				   + "FROM XX_VMR_Department " 
				   + "WHERE XX_VMR_DEPARTMENT_ID="+depart+")";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				manager = rs.getInt("AD_User_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return manager;
	}	
	
	/*
	 * Obtiene el CategoryManager segun la categoria
	 */
	private Integer getCategoryManager(Integer category)
	{
		Integer supervisor=0;
		
		String SQL = "SELECT AD_User_ID " 
				   + "FROM AD_User "
				   + "WHERE C_BPartner_ID = (SELECT XX_CATEGORYMANAGER_ID " 
				   + "FROM XX_VMR_CATEGORY " 
				   + "WHERE XX_VMR_CATEGORY_ID="+category+")";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				supervisor = rs.getInt("AD_User_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return supervisor;
	}
	
	/*
	 * Obtiene el Planner segun el departamento
	 */
	private Integer getPlanner(Integer depart)
	{
		Integer supervisor=0;
		
		String SQL = "SELECT AD_User_ID " 
				   + "FROM AD_User "
				   + "WHERE C_BPartner_ID = (SELECT XX_InventorySchedule_ID " 
				   + "FROM XX_VMR_Department " 
				   + "WHERE XX_VMR_DEPARTMENT_ID="+depart+")";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				supervisor = rs.getInt("AD_User_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return supervisor;
	}
	
	/*
	 * Obtiene el HeadOfPlanning segun el planificador
	 */
	private Integer getHeadOfPlanning(Integer planner)
	{
		Integer headOfPlanning=0;
		
		String SQL = "SELECT Supervisor_ID " +
					 "FROM AD_User " +
					 "WHERE AD_User_ID=" + planner;
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				headOfPlanning = rs.getInt("Supervisor_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return headOfPlanning;
	}
	
	/*
	 * Obtiene el MarketingManager segun el jefe de planificacion
	 */
	private Integer getMarketingManager(Integer headOfPlanning)
	{
		Integer marketingManager=0;
		
		String SQL = "SELECT Supervisor_ID " +
					 "FROM AD_User " +
					 "WHERE AD_User_ID=" + headOfPlanning;
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				marketingManager = rs.getInt("Supervisor_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return marketingManager;
	}
	
	/*
	 *	Obtengo la cantidad de pedidos PD por etiquetar segun la orden
	 */
	private Integer getPendingLabelCount(Integer order){
		
		Integer count=0;
		String SQL = "SELECT COUNT(XX_VMR_Order_ID) AS Cuenta FROM XX_VMR_Order "
			   + "WHERE C_Order_ID="+order;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
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
	
	/*
	 *	Obtengo la cantidad de pedidos CD por etiquetar segun la distribucion
	 */
	private Integer getPendingLabelCDCount(Integer distribution){
		
		Integer count=0;
		String SQL = "SELECT COUNT(XX_VMR_Order_ID) AS Cuenta FROM XX_VMR_Order "
			   + "WHERE SUBSTR(XX_OrderBecoCorrelative,5)='"+distribution+"'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
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
	
	
	// Victor Lo Monaco
	// coloca en el string departamentos, los departamentos asociados a una distribucion
	private Vector departamentosAsociados(Integer cabecera_ID) {
		String sql_asociadas = 
			" SELECT distinct(select value from xx_vmr_department where xx_vmr_department_id=d.XX_VMR_DEPARTMENT_ID) " +
			" FROM XX_VMR_DistributionDetail d " +
			" WHERE XX_VMR_DistributionHeader_ID=" + cabecera_ID;
		
		String mensaje = "Departamentos: ";	
		Vector<String> departamentos = new Vector<String>();
		try {
			PreparedStatement ps = DB.prepareStatement(sql_asociadas,null);
			ResultSet rs = ps.executeQuery();
			while (rs.next() ) { 
				mensaje = mensaje + rs.getInt(1) + " ";
				departamentos.add(rs.getString(1));
			}
			rs.close();
			ps.close();
		} catch (SQLException e){
			e.printStackTrace();
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");				
		}
		departamentos.insertElementAt(mensaje, 0);
		return departamentos;
	}
	
	
	/*
	 * Envia correo al Responsable para que ejecute la tarea crítica pendiente
	 */
	private void sendMail(int manager, int managerRol) throws Exception
	{
		try
		{
			Utilities f = null;			
			
			//Si el encargado es un usuario en específico
			if(manager!=-1)
			{
				f = new Utilities(Env.getCtx(), null,Env.getCtx().getContextAsInt("#XX_L_MT_CRITICALTASKMANA_ID"), "", -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, manager, null);
				f.ejecutarMail();
				f = null;
			}
			//Si el encargado es un rol específico
			if(managerRol!=-1)
			{
				String SQL = "SELECT AD_User_ID FROM AD_User_Roles WHERE AD_Role_ID="+managerRol;
				
				try
				{
					PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				    ResultSet rs = pstmt.executeQuery();			
				    
					while (rs.next())
					{				
						f = new Utilities(Env.getCtx(), null,Env.getCtx().getContextAsInt("#XX_L_MT_CRITICALTASKMANA_ID"), "", -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, rs.getInt("AD_User_ID"), null);
						f.ejecutarMail();
						f = null;
					}
					rs.close();
					pstmt.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}				
			}			
		}
		catch (Exception e)
		{
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		//
	}
	
}// fin de la clase 
