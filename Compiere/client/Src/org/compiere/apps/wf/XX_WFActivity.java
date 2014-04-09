package org.compiere.apps.wf;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.compiere.model.X_AD_WF_Node;
import org.compiere.apps.ADialog;
import org.compiere.apps.AMenu;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.wf.WFActivity;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.ValueNamePair;

import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNode;


//import compiere.model.cds.MOrder;
//import compiere.model.cds.MVMRDistributionHeader;
//import compiere.model.cds.X_Ref_XX_DistributionStatus;
//import compiere.model.cds.MVMRDepartment;



/**
 *  @author Gabriela Marques
 *  @version 
 */

/** Clase que extiende la forma org.compiere.apps.wf.WFActivity
 * para adaptarla a los requerimientos particulares de CENTROBECO.
 * */

public class XX_WFActivity extends org.compiere.apps.wf.WFActivity
implements FormPanel, ActionListener, ListSelectionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CLogger log = CLogger.getCLogger(WFActivity.class);

	public XX_WFActivity()
	{
		super ();
		//	needs to call init
	}	//	WFActivity

	
	/**
	 * 	WF Activity
	 * @param menu AMenu
	 */
	public XX_WFActivity (AMenu menu)
	{
		super ();
		log.config("");
		try  {
			dynInit(0);
			jbInit();
		}
		catch(Exception e)  {
			log.log(Level.SEVERE, "", e);
		}
		m_menu = menu;
	}	//	WFActivity

	@Override

	/**
	 * 	Static Init.
	 * 	Called after Dynamic Init
	 * 	@throws Exception
	 */
	protected void jbInit () throws Exception 	{
		super.jbInit();
		//Expandimos la columna summary
		TableColumn col = selTable.getColumnModel().getColumn(2);
		//col.setWidth(800); 
		col.setMinWidth(500); //col.setMaxWidth(0);

		selPane.setPreferredSize(new Dimension(150, 80));

	}	//	jbInit

	
	
	@Override
	public int loadActivities() {
		
		//		selPane.setMinimumSize(new Dimension(100, 60));
		while (selTableModel.getRowCount() > 0)
			selTableModel.removeRow(0);	
		long start = System.currentTimeMillis();
		m_activities = new ArrayList<MWFActivity>();
		int AD_User_ID = Env.getCtx().getAD_User_ID();
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		int rolUsuario = Env.getCtx().getContextAsInt("#AD_Role_ID");// Rol del usuario
		
		String sql = "SELECT * FROM AD_WF_Activity a "
			+ "WHERE a.Processed='N' AND a.WFState='OS' AND a.AD_Client_ID="+AD_Client_ID+" AND (" //#1
			//	Owner of Activity
//			+ " a.AD_User_ID="+AD_User_ID	//	#2
			//	Invoker (if no invoker = all)
//			+ " OR " 
			+ " EXISTS (SELECT * FROM AD_WF_Responsible r WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID"
			+ " AND COALESCE(r.AD_User_ID,0)=0 AND (a.AD_User_ID="+AD_User_ID+" OR a.AD_User_ID IS NULL))"	//	#3
			// Responsible User
			+ " OR EXISTS (SELECT * FROM AD_WF_Responsible r WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID"
			+ " AND r.AD_User_ID="+AD_User_ID+" AND r.responsibletype = 'H')"		//	#4
			//	Responsible Role
			+ " OR EXISTS (SELECT * FROM AD_WF_Responsible r INNER JOIN AD_User_Roles ur ON (r.AD_Role_ID=ur.AD_Role_ID)"
			+ " WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID AND ur.AD_User_ID="+AD_User_ID 
			+ " AND ur.AD_Role_ID ="+rolUsuario+" AND r.responsibletype = 'R')"	//	#2 y #3
			//
			+ ") ORDER BY a.Priority DESC, Created";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
//		System.out.println(sql);
		try {

			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			
			List<String> departamentos = new ArrayList<String>(); // Departamentos del usuario
			List<String> categorias = new ArrayList<String>(); // Categorias del usuario
			boolean filtro = false;
//			System.out.println(AD_User_ID);
			// Si es un rol asociado a un departamento:
			if (rolUsuario == Env.getCtx().getContextAsInt("#XX_L_ROLEBUYER_ID")  // Comprador
					|| rolUsuario == Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULER_ID")  // Planificador
				) {
				filtro = true;
				//Departamentos del usuario
				String query = " SELECT nvl(XX_VMR_DEPARTMENT_ID,-1) dept, nvl(name, ' ') dept_name " +
						"	from XX_VMR_DEPARTMENT where xx_userbuyer_id = (" +
							"	select c_bpartner_id from ad_user where ad_user_id = "+AD_User_ID+")";
				PreparedStatement ps = null;
				ResultSet rs2 = null;
				try {
					ps = DB.prepareStatement(query, null);			
					rs2 = ps.executeQuery();
					while (rs2.next()) {
						departamentos.add(rs2.getString("dept"));
						departamentos.add(rs2.getString("dept_name"));
//						System.out.println(rs2.getString("dept") + "----");
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs2);
					DB.closeStatement(ps);
				}
				
			}
			
			// Si es un rol asociado a una categoría:
			else if (rolUsuario == Env.getCtx().getContextAsInt("#XX_L_ROLECATEGORYMANAGER_ID")  // Jefe de Categoria
				//	|| rolUsuario == Env.getCtx().getContextAsInt("#??")  // Importaciones
				) {
				filtro = true;
				//Categorias del usuario
				String query = " SELECT nvl(XX_VMR_CATEGORY_ID,-1) categ, nvl(name, ' ') categ_name " +
						"	from XX_VMR_CATEGORY where XX_CATEGORYMANAGER_ID = (" +
							"	select c_bpartner_id from ad_user where ad_user_id = "+AD_User_ID+")";
				PreparedStatement ps = null;
				ResultSet rs2 = null;
				try {
					ps = DB.prepareStatement(query, null);			
					rs2 = ps.executeQuery();
					while (rs2.next()) {
						categorias.add(rs2.getString("categ"));
						categorias.add(rs2.getString("categ_name"));
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs2);
					DB.closeStatement(ps);
				}
				
			}
				
			while (rs.next ()) 	{
				XX_MWFActivity activity = new XX_MWFActivity(Env.getCtx(), rs, null);
				int dept_act = activity.getDepartment() ;
				int cat_act = activity.getCategory() ;
//				System.out.println(dept_act);
				// Mostrar sólo las actividades que le corresponden
				if (!filtro | (departamentos.size() != 0 && departamentos.contains(String.valueOf(dept_act))
					//	|| (departamentos.size()==0 && activity.getAD_User_ID() == AD_User_ID)
						|| (categorias.size() != 0 && categorias.contains(String.valueOf(cat_act)))) ) { //(cat_id !=-1 && activity.getCategory()!= -1 )
					m_activities.add (activity);
					Object[] rowData = new Object[4];
					rowData[0] = activity.getPriority();
					rowData[1] = activity.getNodeName();
					String dep = departmentName(dept_act);
					rowData[2] = activity.getSummary().concat( 
							(dept_act!=-1 ? "- Departamento: " + dep : ""));
							//(departamentos.size()==0 ? "" : departamentos.get(departamentos.indexOf(String.valueOf(dept_act))+1));
					rowData[3] = activity.get_ID();
	
					selTableModel.addRow(rowData);
					if (m_activities.size() > 200)		//	HARDCODED
					{
						log.warning("More then 200 Activities - ignored");
						break;
					}
				} 
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		selTable.autoSize(false);
		log.config("#" + m_activities.size() 
				+ "(" + (System.currentTimeMillis()-start) + "ms)");
		return m_activities.size();
	}
	
	
	@Override
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == bOK) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			cmd_OK(); // Nueva versión
			this.setCursor(Cursor.getDefaultCursor());
		}
		else {
			super.actionPerformed(e);
		}
		
	}	//	actionPerformed

		
	
	//Nueva versión, overrides WFActivity.cmd_OK()
	private void cmd_OK() 	{
		
		log.config("Activity=" + m_activity);
		if (m_activity == null)
			return;
		int AD_User_ID = Env.getCtx().getAD_User_ID();
		String textMsg = fTextMsg.getText();
		//
		MWFNode node = m_activity.getNode();

		Object forward = fForward.getValue();
		if (forward != null)
		{
			log.config("Forward to " + forward);
			int fw = ((Integer)forward).intValue();
			if (fw == AD_User_ID || fw == 0)
			{
				log.log(Level.SEVERE, "Forward User=" + fw);
				return;
			}
			if (!m_activity.forwardTo(fw, textMsg))
			{
				ADialog.error(m_WindowNo, this, "CannotForward");
				return;
			}
		}
		//	User Choice - Answer
		else if (X_AD_WF_Node.ACTION_UserChoice.equals(node.getAction()))
		{
			if (m_column == null)
				m_column = node.getColumn();
			//	Do we have an answer?
			int dt = m_column.getAD_Reference_ID();
			String value = fAnswerText.getText();
			if (dt == DisplayTypeConstants.YesNo || dt == DisplayTypeConstants.List)
			{
				ValueNamePair pp = (ValueNamePair)fAnswerList.getSelectedItem();
				value = pp.getValue();
			}
			if (value == null || value.length() == 0)
			{
				ADialog.error(m_WindowNo, this, "FillMandatory", Msg.getMsg(Env.getCtx(), "Answer"));
				return;
			}
			//
			log.config("Answer=" + value + " - " + textMsg);
			try
			{
				m_activity.setUserChoice(AD_User_ID, value, dt, textMsg);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, node.getName(), e);
				ADialog.error(m_WindowNo, this, "Error", e.toString());
				return;
			}
		}
		//	User Action
		else
		{
			log.config("Action=" + node.getAction() + " - " + textMsg);
//			System.out.println("Action=" + node.getAction() + " - " + textMsg);
			try
			{
//				System.out.println("Action=" + node.getAction() + " - " + textMsg);
//				m_activity.setUserConfirmation(AD_User_ID, textMsg);
				int process = m_activity.getAD_Workflow_ID();
				
				// Se verifica que se haya realizado la acción correspondiente antes de darle al check
				if (process == Env.getCtx().getContextAsInt("#XX_WF_DEFINITIVEPRICES") ) { // Fijar precios en la distribución
					String distStatus = distributionStatus(m_activity.getRecord_ID());
					// Verificar si realmente esta distribución se encuentra pendiente
					if (distStatus.equals("FP")) { // No fueron fijados los precios
						ADialog.info(1, new Container(), "XX_IncompleteDefPricTask");
						return;
					}

				} else if (process == Env.getCtx().getContextAsInt("#XX_WF_OCAPPROVAL")) {  //(process == 1000313)  { //Aprobación de Orden de Compra (Nuevo método)
					// Verificamos si le tocaba aprobar, sin importar status
					String sqlResponsible = "SELECT XX_CT_Responsible_ID " +
							" FROM C_ORDER " +
							" WHERE C_ORDER_ID = " + m_activity.getRecord_ID();

					PreparedStatement pResp = null;
					ResultSet rsResp = null;
					int responsible = 0;
					try{
						pResp = DB.prepareStatement(sqlResponsible, null); 
						rsResp = pResp.executeQuery();
						if(rsResp.next()) {
							responsible = rsResp.getInt("XX_CT_Responsible_ID");
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						rsResp.close();
						pResp.close();
					}
					System.out.println(Env.getCtx().getContextAsInt("#AD_Role_ID"));
					String oStatus = orderStatus(m_activity.getRecord_ID());
					System.out.println(oStatus);
					if ( (!(oStatus.equals("AP")  || oStatus.equals("EP")) // No está aprobada
							&& orderIsReadyStatus(m_activity.getRecord_ID())  // No está anulada
							) && (responsible == Env.getCtx().getContextAsInt("#AD_Role_ID")) )  { // Le toca aprobar
						ADialog.info(1, new Container(), "XX_IncompleteOCapTask");
						return;
					}
						
				}
				
				else if (process == Env.getCtx().getContextAsInt("#XX_WF_COMPLETENREPORT")) {  // (process == 1001313) { // Completar Reporte de Novedad
					// Verificar si aún no ha sido completado ni anulado el reporte
					if (!completeNewsReport(m_activity.getRecord_ID())) { // No se ha completado ni anulado
						ADialog.info(1, new Container(), "XX_IncompleteNewsReport");
						return;
					} else { // Está completo, se deben completar los demás reportes de la misma OC
						completarReportesAsociados(m_activity.getRecord_ID());
					}
				}
				else if (process == Env.getCtx().getContextAsInt("#XX_WF_SITMEASSOCIATEREF")) { // Asociar Referencias y Aprobar O/C Nacional de Sitme
					String oStatus = orderStatus(m_activity.getRecord_ID());
					// Verificar si aún no ha sido completado ni anulado el reporte
					if (!isAssociateRef(m_activity.getRecord_ID())) { // No se han asociado las referencias
						ADialog.info(1, new Container(), "XX_IncompleteAssociation");
						return;
					}else if( !oStatus.equals("AP")){// No se ha aprobado
						ADialog.info(1, new Container(), "XX_IncompleteApproval");
						return;
					}
				}
				
				m_activity.setUserConfirmation(AD_User_ID, "");
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, node.getName(), e);
				ADialog.error(m_WindowNo, this, "Error", e.toString());
				return;
			}

		}
		//	Next
		if (m_menu != null)
			m_menu.updateInfo();
		else
			loadActivities();
		display(-1);
		
		
	}	//	cmd_OK

	
	private void completarReportesAsociados(int id) {
		String qActiv = " select AD_WF_ACTIVITY_ID from ad_wf_activity where WFState = 'OS' and ad_wf_process_id in (" +
				"\n  select ad_wf_process_id" +
				"\n  from ad_wf_process where requestdocumentno in (" +
				"\n          select documentno from xx_vlo_newsreport " +
				"\n			 where c_order_id = (select c_order_id from xx_vlo_newsreport where xx_vlo_newsreport_id = " + id + ")" +
				"\n     and ((xx_status = 'AO' and xx_neworder_id is not null ) or xx_status = 'AN')" +
				"\n    and xx_vlo_newsreport_id <>  " + id + 
				"\n  ) and ad_workflow_id = " + Env.getCtx().getContextAsInt("#XX_WF_COMPLETENREPORT") + 
				"\n ) ";
		
		PreparedStatement ps = null;
		ResultSet rs2 = null;
		try {
			ps = DB.prepareStatement(qActiv, null);			
			rs2 = ps.executeQuery();
			while (rs2.next()) {
				MWFActivity activ = new MWFActivity(Env.getCtx(), rs2.getInt("AD_WF_ACTIVITY_ID"),  (Trx) null) ;
				activ.setUserConfirmation(Env.getCtx().getAD_User_ID(), "");
//				activ.setWFState(StateEngine.STATE_Running);
//				activ.setAD_User_ID(getAD_User_ID());
//				activ.setWFState(StateEngine.STATE_Terminated);
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, qActiv, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs2);
			DB.closeStatement(ps);
		}
		
		System.out.println("Proceso Terminado");
			
	}
	
	
	private String departmentName(int dept_id) {
		String dept = "";
		String query = "SELECT value ||' - '|| name as NAME FROM XX_VMR_DEPARTMENT WHERE XX_VMR_DEPARTMENT_ID = "+dept_id;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				dept = rs.getString("name");
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, query, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return dept;
	}
	
	private String distributionStatus(int id) {
		String query = "select xx_distributionstatus from xx_vmr_distributionheader where XX_VMR_DistributionHeader_ID = "+id;
		String status = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				status = rs.getString("xx_distributionstatus");
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, query, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return status;
	}
	
	private String orderStatus(int id) {
		String query = "select XX_OrderStatus from c_order where c_order_ID = "+id;
		String status = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				status = rs.getString("XX_OrderStatus");
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, query, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return status;
	}
	
	private boolean orderIsReadyStatus(int id) {
		String query = "select XX_OrderReadyStatus from c_order where c_order_ID = "+id;
		String status = "";
		boolean isReady = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				status = rs.getString("XX_OrderReadyStatus");
				if (status.equals("Y")) {
					isReady =  true;
				}
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, query, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return isReady;
	}
	
	
	private boolean completeNewsReport(int id) {
		String query = " select * from xx_vlo_newsreport where xx_vlo_newsreport_id = " + id +
				" and (xx_status = 'AN' OR XX_NewOrder_id is not null) ";
		boolean isComplete = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				isComplete =  true;
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, query, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return isComplete;
	}
	
	private boolean isAssociateRef(int id) {
		String reference = "Y";
		//Verifico que todas las referencias esten asociadas al producto

		String  sql = "SELECT XX_ReferenceIsAssociated " +
		"FROM XX_VMR_PO_LineRefProv " +
		"WHERE C_ORDER_ID=" + id;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		try{
			rs = pstmt.executeQuery();
			
			while(rs.next() && reference.equals("Y"))
			{
				reference = rs.getString("XX_ReferenceIsAssociated");
			}
		}
		catch (Exception e){
		} finally {
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		if(reference.equals("Y")){
			return true;
		}else {
			return false;
		}
	}
	
}
