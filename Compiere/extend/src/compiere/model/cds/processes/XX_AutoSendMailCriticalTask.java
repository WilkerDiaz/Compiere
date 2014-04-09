package compiere.model.cds.processes;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MOrder;
import compiere.model.cds.MUser;
import compiere.model.cds.MVLOUnsolicitedProduct;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRCriticalTasks;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.Utilities;

/**
 *
 *  @author     Rebecca Principal y Rosmaira Arvelo
 *  @version    
 */
public class XX_AutoSendMailCriticalTask extends SvrProcess {
	 
	 String Mensaje="";
	 Calendar calendario = Calendar.getInstance();
	 java.util.Date now = calendario.getTime(); 
	 java.sql.Timestamp fechaActual = new java.sql.Timestamp(now.getTime()); 
	 String fechaAux = ""; 
	 Date FechaReg=new Date();
	 private static int Date_option=0;
	 Integer IDRegAct=0, Reg=0;
	 //int actividad,actividad2;
		
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		//Genera alertas Codificación de los productos y O/C por recibirse
		generarAlertCTAsociarProduct();
		
		//Envía correo a los responsables de completar las tareas críticas, 
		//un día después que éstas se crearon
		sendMailManager();
		
		//Envía correo a los supervisores de los responsables de completar las tareas críticas,
		//cuando el plazo límite ha transcurrido 
		sendMailSupervisor();
		
		return "";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Se envía un correo al Supervisor del responsable de completar la tarea crítica, cuando
	 * el plazo límite ha transcurrido
	 */
	private void sendMailSupervisor() 
	{				
		//Busca todas las tareas criticas activas, es decir que no se han ejecutado
		String SQL = "SELECT * FROM XX_VMR_CriticalTaskForClose WHERE XX_StatusCriticalTask='Act'";
		
		PreparedStatement pstmt = null;
		ResultSet	 rs = null;
		try{
			pstmt = DB.prepareStatement(SQL,null);
			rs = pstmt.executeQuery(SQL);
			  
			while(rs.next())
			{
				MVMRCriticalTaskForClose task= new MVMRCriticalTaskForClose(getCtx(),rs.getInt("XX_VMR_CriticalTaskForClose_ID"), null);
				MVMRCriticalTasks criTask = new MVMRCriticalTasks(Env.getCtx(),task.getXX_VMR_CriticalTasks_ID(),null);
				
				//Fecha de creación de la tarea crítica
				java.sql.Date Fechacreated= rs.getDate("Created");
				
				//Suma a la fecha de creación los días para realizar la tarea, para verificar
				//que no haya pasado el plazo límite
				String Fechatope=CambiarFechas(Fechacreated,+task.getXX_TasksAlert());
				Timestamp ft = Timestamp.valueOf(Fechatope.concat(" 00:00:00.0"));
				String fecha=fechaActual.toString(); 
				fecha=fecha.substring(0, 10);
				Timestamp fe = Timestamp.valueOf(fecha.concat(" 00:00:00.0"));
				
				//Verifica que la tarea esté activa y la fecha actual sea mayor a la fecha tope,
				//y así mandar un correo al supervisor informándole que el encargado no ha realizado
				//la tarea crítica
				if((task.isActive()==true)&&(fe.compareTo(ft)>0))
				{
					//Asignación de precio de venta
					if(task.getXX_TypeTask().equals("AP"))
					{
						sendMail(criTask.getXX_Task(),task.getXX_AssociateSupervisor_ID(),-1);
							
					}
					//Asignación de precio de venta
					if(task.getXX_TypeTask().equals("AP1"))
					{
						sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
						
					}
					//Codificación de los productos
					if(task.getXX_TypeTask().equals("AS"))
					{
						sendMail(criTask.getXX_Task(),task.getXX_AssociateSupervisor_ID(),-1);
							
					}
					//Confirmación codificación de los productos en línea genérica
					if(task.getXX_TypeTask().equals("VP"))
					{
						sendMail(criTask.getXX_Task(),task.getXX_AssociateSupervisor_ID(),-1);
							
					}
					//Asignación de código arancelario
					if(task.getXX_TypeTask().equals("AC"))
					{
						sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
							
					}
					//Aprobación de O/C sin sobregiro
					if(task.getXX_TypeTask().equals("AO"))
					{
						sendMail(criTask.getXX_Task(),task.getXX_AssociateSupervisor_ID(),-1);
							
					}
					//Aprobación de O/C con sobregiro
					if(task.getXX_TypeTask().equals("AO1"))
					{
						sendMail(criTask.getXX_Task(),task.getXX_AssociateSupervisor_ID(),-1);
							
					}
					//O/C por recibirse
					if(task.getXX_TypeTask().equals("OR"))
					{
						sendMail(criTask.getXX_Task(),task.getXX_AssociateSupervisor_ID(),-1);
							
					}
					//O/C pendiente de chequear
					if(task.getXX_TypeTask().equals("CO"))
					{
						sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
							
					}
					//Pedido pendiente por etiquetar
					if(task.getXX_TypeTask().equals("EP"))
					{
						sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
							
					}
					//Pedido pendiente por etiquetar
					if(task.getXX_TypeTask().equals("EP1"))
					{
						sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
							
					}
					//Pedido pendiente por etiquetar
					if(task.getXX_TypeTask().equals("EP2"))
					{
						sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
					
					}
					//Cálculo de factores
					if(task.getXX_TypeTask().equals("CF"))
					{
						sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
							
					}
					//Cierre de expediente y ajuste de carga en tránsito
					if(task.getXX_TypeTask().equals("CE"))
					{
						//Coordinador de Inmportaciones no quiere recibir correo
						//sendMail(criTask.getXX_Task(),-1,task.getXX_SupervisorRole_ID());
							
					}	
				}
			}
		}	
		catch (Exception e)
		{
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	
	
	/*
	 * Envía correo a los responsables de completar las tareas críticas,
	 * un día después que éstas se crearon
	 */
	private void sendMailManager() 
	{				
		//Busca todas las tareas criticas activas, es decir que no se han ejecutado
		String SQL = "SELECT * FROM XX_VMR_CriticalTaskForClose WHERE XX_StatusCriticalTask='Act'";
		
		PreparedStatement pstmt = null;
		ResultSet	 rs = null;
		try{
			pstmt = DB.prepareStatement(SQL,null);
			rs = pstmt.executeQuery(SQL);
			  
			while(rs.next())
			{
				MVMRCriticalTaskForClose task= new MVMRCriticalTaskForClose(getCtx(),rs.getInt("XX_VMR_CriticalTaskForClose_ID"), null);
				MVMRCriticalTasks criTask = new MVMRCriticalTasks(Env.getCtx(),task.getXX_VMR_CriticalTasks_ID(),null);
				
				//Fecha de creación de la tarea crítica
				java.sql.Date Fechacreated= rs.getDate("Created");				
				
				int day   = Fechacreated.getDate();
				int month = Fechacreated.getMonth()+1;
				String date = Fechacreated.toString();
				int year  = Integer.parseInt(date.substring(0,4));
				String fechacrea = "";
				
				if(day<10)
				{
					if(month<10){
						fechacrea = year+"-0"+month+"-0"+day;
					}else{
						fechacrea = year+"-"+month+"-0"+day;
					} 
				}
				else
				{
					if(month<10){
						fechacrea = year+"-0"+month+"-"+day;
					}else{
						fechacrea = year+"-"+month+"-"+day;
					}
				}				
				
				Timestamp fc = Timestamp.valueOf(fechacrea.concat(" 00:00:00.0"));
				//String Fechatope = CambiarFechas(Fechacreated,+task.getXX_TasksAlert());
				//Timestamp ft = Timestamp.valueOf(Fechatope.concat(" 00:00:00.0"));
				String fecha = fechaActual.toString(); 
				fecha = fecha.substring(0, 10);
				Timestamp fa = Timestamp.valueOf(fecha.concat(" 00:00:00.0"));
				
				//Verifica que la tarea esté activa y la fecha actual sea mayor a la fecha de creación,
				//y así mandar un correo al encargado para que realice la tarea crítica
				if((task.isActive()==true)&&(fa.compareTo(fc)>0))
				{
					//Asignación de precio de venta
					if(task.getXX_TypeTask().equals("AP"))
					{
						MVMRDistributionHeader dh= new MVMRDistributionHeader(getCtx(),task.getXX_ActualRecord_ID(),get_TrxName());
						MOrder order = new MOrder(Env.getCtx(),dh.getC_Order_ID(),null);
						Integer manager = getUserBuyer(order.getXX_VMR_DEPARTMENT_ID());
						
						sendMail(manager,-1);
							
					}
					//Asignación de precio de venta
					if(task.getXX_TypeTask().equals("AP1"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
						
					}
					//Codificación de los productos
					if(task.getXX_TypeTask().equals("AS"))
					{
						MOrder order = new MOrder(Env.getCtx(),task.getXX_ActualRecord_ID(),null);
						Integer manager = getUserBuyer(order.getXX_VMR_DEPARTMENT_ID());
						
						sendMail(manager,-1);
							
					}
					//Confirmación codificación de los productos en línea genérica
					if(task.getXX_TypeTask().equals("VP"))
					{
						MVLOUnsolicitedProduct PNoSol= new MVLOUnsolicitedProduct(Env.getCtx(),task.getXX_ActualRecord_ID(),null);
						MOrder order = new MOrder(Env.getCtx(),PNoSol.getC_Order_ID(),null);
						Integer manager = getUserBuyer(order.getXX_VMR_DEPARTMENT_ID());
						
						sendMail(manager,-1);
							
					}
					//Asignación de código arancelario
					if(task.getXX_TypeTask().equals("AC"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
							
					}
					//Aprobación de O/C sin sobregiro
					if(task.getXX_TypeTask().equals("AO"))
					{
						MOrder Order= new MOrder(Env.getCtx(),task.getXX_ActualRecord_ID(),null);
						Integer manager = getPlanner(Order.getXX_VMR_DEPARTMENT_ID());
						
						sendMail(manager,-1);
							
					}
					//Aprobación de O/C con sobregiro
					if(task.getXX_TypeTask().equals("AO1"))
					{
						MOrder Order= new MOrder(Env.getCtx(),task.getXX_ActualRecord_ID(),null);
						Integer manager = getPlanner(Order.getXX_VMR_DEPARTMENT_ID());
						Integer manager2 = getHeadOfPlanning(manager);
						
						sendMail(manager2,-1);
							
					}
					//O/C por recibirse
					if(task.getXX_TypeTask().equals("OR"))
					{
						MOrder Order= new MOrder(Env.getCtx(),task.getXX_ActualRecord_ID(),null);
						Integer manager = getUserBuyer(Order.getXX_VMR_DEPARTMENT_ID());
						
						sendMail(manager,-1);
							
					}
					//O/C pendiente de chequear
					if(task.getXX_TypeTask().equals("CO"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
							
					}
					//Pedido pendiente por etiquetar
					if(task.getXX_TypeTask().equals("EP"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
							
					}
					//Pedido pendiente por etiquetar
					if(task.getXX_TypeTask().equals("EP1"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
							
					}
					//Pedido pendiente por etiquetar
					if(task.getXX_TypeTask().equals("EP2"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
					
					}
					//Cálculo de factores
					if(task.getXX_TypeTask().equals("CF"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
							
					}
					//Cierre de expediente y ajuste de carga en tránsito
					if(task.getXX_TypeTask().equals("CE"))
					{
						sendMail(-1,task.getXX_AssociateManager_ID());
							
					}	
				}
			}
		}	
		catch (Exception e)
		{
			log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	/**
	 * Cambiar Fechas 
	 * @param fechaestimada
	 * @return String fecha convertida a String
	 * @uthor Rebecca Principal 14/05/2010
	 */
	private String CambiarFechas(java.sql.Date fechaestimada,int Date_option){
		String fecha = fechaestimada.toString();
		//se trunca la fecha a 10 caracteres
		fecha=fecha.substring(0, 10);
		
		  try
		    {    
	          	Date date ; 
	          	//Cambiar formato de la fecha incluir el la hora new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
	          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	          	date = (Date)formatter.parse(fecha);
	          	Calendar cal = new GregorianCalendar(); 
	            cal.setTimeInMillis(fechaestimada.getTime()); 
	      
	          	cal.add(Calendar.DATE,Date_option);
	          		
	          	//
	          	formatter = new SimpleDateFormat("yyyy-MM-dd");
	          	// Calendar to Date Conversion
	          	int year = cal.get(Calendar.YEAR);
	          	int month = cal.get(Calendar.MONTH);
	          	int day = cal.get(Calendar.DATE);
	          	Date auxDate = new Date((year-1900), month, day);
	          	fecha=formatter.format(auxDate);
		    }
		    catch (ParseException e)
		    {
		    	log.log(Level.SEVERE, "Fecha", e);   
		    }
		return fecha;    
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
				String SQL = "SELECT AD_User_ID FROM AD_User_Roles WHERE isactive='Y' and AD_User_ID<>0 AND AD_User_ID<>100 AND AD_ROLE_ID<>0 AND AD_Role_ID="+managerRol;
				
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
	
	/*
	 * Envia correo a Superior cuando el encargado no ha ejecutado la tarea crítica y se ha cumplido
	 * el plazo de la misma	   
	 */
	private void sendMail(String task, int supervisor, int supervisorRol) throws Exception
	{			
		try
		{
			Utilities f = null;			
			
			//Si el supervisor es un usuario en específico
			if(supervisor!=-1)
			{
				MUser user = new MUser(Env.getCtx(),supervisor,null);
				String Mensaje = Msg.getMsg(Env.getCtx(), "XX_TaskCriticalSuper", new String[] {"'"+task+"'", "'"+user.getName()+"'"});
				f = new Utilities(Env.getCtx(), null,Env.getCtx().getContextAsInt("#XX_L_MT_CRITICALTASKSUPE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, supervisor, null);
				f.ejecutarMail();
				f = null;
			}
			//Si el supervisor es un rol específico
			if(supervisorRol!=-1)
			{
				String SQL = "SELECT AD_User_ID FROM AD_User_Roles WHERE isactive='Y' and AD_User_ID<>0 AND AD_User_ID<>100 AND AD_ROLE_ID<>0 AND AD_Role_ID="+supervisorRol;
				
				try
				{
					PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				    ResultSet rs = pstmt.executeQuery();			
				    
					while (rs.next())
					{
						MUser user = new MUser(Env.getCtx(),rs.getInt("AD_User_ID"),null);
						String Mensaje = Msg.getMsg(Env.getCtx(), "XX_TaskCriticalSuper", new String[] {"'"+task+"'", "'"+user.getName()+"'"});
						f = new Utilities(Env.getCtx(), null,Env.getCtx().getContextAsInt("#XX_L_MT_CRITICALTASKSUPE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, user.get_ID(), null);
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
	
	/**
	 * Generar automaticamente la alerta para Asociar Productos y visualizar las O/C por recibir, 48 horas antes de la fecha estimada	 
	 */
	public void generarAlertCTAsociarProduct()
	{
		//Buscar la fecha estimada de la orden de compra 		
		String SQL = "SELECT C_Order_ID, decode(XX_EstimatedDate,NULL,SYSDATE,XX_EstimatedDate) As XX_EstimatedDate "
				   + "FROM C_Order "
				   + "WHERE XX_OrderStatus='AP'";
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL,null);
			ResultSet	 rs = pstmt.executeQuery(SQL);
			
			MVMRCriticalTasks criticalTask = new MVMRCriticalTasks(Env.getCtx(),getCriticalTask_ID("Orden de Compra por recibirse"),null);
			
			while(rs.next())
			{
				MOrder Order= new MOrder(getCtx(),rs.getInt("C_Order_ID"),get_TrxName());
					
				java.sql.Date Fechaestimada= rs.getDate("XX_EstimatedDate");
				String Fechatope=CambiarFechas(Fechaestimada,-criticalTask.getXX_TasksAlert());
				String fecha=fechaActual.toString(); 
				fecha=fecha.substring(0, 10);
				if((fecha.equals(Fechatope)))
				{
					//Generar alerta de orden de compra por recibirse					
					if(Order.get_ValueAsBoolean("XX_Alert5")==false)
					{
						Env.getCtx().setContext("#XX_TypeAlertOR","OR");
						Env.getCtx().setContext("#XX_OrderRCT",Order.get_ID());
						
						//LLama al proceso de generar alerta 
						MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"),Order.get_ID()); 
						mpi.save();
						
						ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
						pi.setRecord_ID(mpi.getRecord_ID());
						pi.setAD_PInstance_ID(mpi.get_ID());
						pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
						pi.setClassName(""); 
						pi.setTitle(""); 
						
						ProcessCtl pc = new ProcessCtl(null ,pi,null); 
						pc.start();
					}
				}
			}
			rs.close();
			pstmt.close();
		}	
		catch (Exception e){
		log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		//Buscar la fecha estimaa de la orden de compra 
		SQL = "SELECT DISTINCT(ord.C_Order_ID), decode(ord.XX_EstimatedDate,NULL,SYSDATE,ord.XX_EstimatedDate) As XX_EstimatedDate "
			+ "FROM C_Order ord,  XX_VMR_PO_LineRefProv asp "
			+ "WHERE ord.C_Order_ID=asp.C_Order_ID "
			+ "AND asp.XX_ReferenceIsAssociated='N' "
			+ "AND (ord.XX_OrderStatus='EP' OR ord.XX_OrderStatus='EAC' "
			+ "OR ord.XX_OrderStatus='ETI' OR ord.XX_OrderStatus='LVE' "
			+ "OR ord.XX_OrderStatus='EPN' OR ord.XX_OrderStatus='ETN')";
		
		MVMRCriticalTasks criticalTask = new MVMRCriticalTasks(Env.getCtx(),getCriticalTask_ID("Codificación de los productos"),null);
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL,null);
			ResultSet	 rs = pstmt.executeQuery(SQL);
			 
			while(rs.next())
			{				
				java.sql.Date Fechaestimada= rs.getDate("XX_EstimatedDate");
				String Fechatope=CambiarFechas(Fechaestimada,-criticalTask.getXX_TasksAlert());
				String fecha=fechaActual.toString(); 
				fecha=fecha.substring(0, 10);
					
				if((fecha.equals(Fechatope)))
				{
					MOrder order= new MOrder(getCtx(),rs.getInt("C_Order_ID"),get_TrxName());
					
					if((order.isXX_Alert9()==false))
					{
						//Generar alertar por asociacion de productos
						Env.getCtx().setContext("#XX_TypeAlertAS","AS");
						Env.getCtx().setContext("#XX_RefAssociaCT",order.get_ID());
						
						//LLama al proceso de generar alerta 							
						MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"), order.get_ID()); 
						mpi.save();
						
						ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
						pi.setRecord_ID(mpi.getRecord_ID());
						pi.setAD_PInstance_ID(mpi.get_ID());
						pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
						pi.setClassName(""); 
						pi.setTitle(""); 
							
						ProcessCtl pc = new ProcessCtl(null ,pi,null); 
						pc.start();
					}
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
		log.saveError("ErrorSql: ", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
	}	
	
	//Realizado por Rosmaira Arvelo
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
	}//fin getCriticalTask_ID 
	
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
	}//RArvelo

}
