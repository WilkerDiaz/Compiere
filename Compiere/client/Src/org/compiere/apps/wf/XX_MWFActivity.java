package org.compiere.apps.wf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.wf.WFActivity;
import org.compiere.framework.PO;
import org.compiere.model.MPInstance;
import org.compiere.model.X_AD_WF_Node;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.compiere.wf.MWorkflow;
import org.compiere.apps.ProcessCtl;

/**
 *  @author Gabriela Marques
 *  @version 
 */

/** Clase que extiende la forma org.compiere.apps.wf.WFActivity
 * para adaptarla a los requerimientos particulares de CENTROBECO.
 * */

public class XX_MWFActivity extends org.compiere.wf.MWFActivity {

	public XX_MWFActivity(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CLogger log = CLogger.getCLogger(WFActivity.class);
	
	public int getDepartment() {
		int dept_id = -1;
		PO po = getPO();
		if (po == null)
			return -1;
		
		// Primero verificar si se encuentra almacenado el departamento, y tiene valor no nulo
		int index = po.get_ColumnIndex("XX_VMR_Department_ID");
		if (index != -1)  { //Está almacenada la variable
			if (po.get_Value(index) != null) { // Tiene valor no nulo, listo
				return (Integer)po.get_Value(index);
			}
		}
		
		// CASO CABECERA DE DISTRIBUCION
		if (getAD_Table_ID() == get_Table_ID("XX_VMR_DistributionHeader")) {
			index = po.get_ColumnIndex("XX_VMR_DistributionHeader_ID");
			if (index != -1)  { //Está almacenada la variable
				
				String query = " SELECT nvl(XX_VMR_DEPARTMENT_ID, -1) dept " +
						"	from XX_VMR_DISTRIBUTIONDETAIL " +
						"	where xx_vmr_distributionheader_id = " + po.get_Value(index) ;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = DB.prepareStatement(query, null);			
					rs = ps.executeQuery();
					// Cabeceras del XLS
					while (rs.next()) {
//						System.out.println("Dept activity: "+rs.getInt("dept"));
						return rs.getInt("dept"); 
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
				}
			}
		}
		
		// CASO ORDEN DE COMPRA
		if (getAD_Table_ID() == get_Table_ID("C_ORDER")) {
			index = po.get_ColumnIndex("C_ORDER_ID");
			if (index != -1)  { //Está almacenada la variable
				String query = " SELECT nvl(XX_VMR_DEPARTMENT_ID, -1) dept " +
						"	from C_ORDER " +
						"	where c_order_id = " + po.get_Value(index) ;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = DB.prepareStatement(query, null);			
					rs = ps.executeQuery();
					while (rs.next()) {
//						System.out.println("Dept activity: "+rs.getInt("dept"));
						return rs.getInt("dept"); 
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
				}
			}
		}
		
//		
//		index = po.get_ColumnIndex("C_Order_ID");
//		if (index != -1) {
//			System.out.println("3 c_order: " +  po.get_ColumnName(index) + ", " + po.get_Value(index));
//			String order = (String)po.get_Value(index);
//			String query = "SELECT XX_VMR_DEPARTMENT_ID from C_Order where C_Order_ID=" + order ;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//			try {
//				pstmt = DB.prepareStatement(query, (Trx) null);
//				rs = pstmt.executeQuery ();
//				rs.first();
//				return rs.getString("XX_VMR_DEPARTMENT_ID");
//			} catch (Exception e) {
//				log.log(Level.SEVERE, query, e);
//				return null;
//			} finally {
//				DB.closeResultSet(rs);
//				DB.closeStatement(pstmt);
//			}
//		} else {
//			System.out.println("4");
//			return null;
//		}
		
		return dept_id;

	}	//	getDepartment
	
	
	public int getCategory() {
		int cat_id = -1;
		PO po = getPO();
		if (po == null)
			return -1;
		
		// Primero verificar si se encuentra almacenada la categoría, y tiene valor no nulo
		int index = po.get_ColumnIndex("XX_VMR_Category_ID");
		if (index != -1)  { //Está almacenada la variable
			if (po.get_Value(index) != null) { // Tiene valor no nulo, listo
				return (Integer)po.get_Value(index);
			}
		}
		
		// CASO CABECERA DE DISTRIBUCION
		if (getAD_Table_ID() == get_Table_ID("XX_VMR_DistributionHeader")) {
			index = po.get_ColumnIndex("XX_VMR_DistributionHeader_ID");
			if (index != -1)  { //Está almacenada la variable
				String query = " SELECT nvl(XX_VMR_CATEGORY_ID, -1) categ " +
						"	from xx_vmr_department where xx_vmr_department_id = " +
						" (select distinct xx_vmr_department_id from XX_VMR_DistributionDetail where " +
						"  xx_vmr_distributionheader_id = " + po.get_Value(index) + " ) ";
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = DB.prepareStatement(query, null);			
					rs = ps.executeQuery();
					while (rs.next()) {
//						System.out.println("Cat activity: "+rs.getInt("categ"));
						return rs.getInt("categ"); 
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
				}
			}
		}
		
		// CASO ORDEN DE COMPRA
		if (getAD_Table_ID() == get_Table_ID("C_Order")) {
			index = po.get_ColumnIndex("C_Order_ID");
			if (index != -1)  { //Está almacenada la variable
				String query = " SELECT nvl(XX_VMR_CATEGORY_ID, -1) categ " +
						"	from c_order where c_order_id = " + po.get_Value(index);
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = DB.prepareStatement(query, null);			
					rs = ps.executeQuery();
					while (rs.next()) {
//						System.out.println("Cat activity: "+rs.getInt("categ"));
						return rs.getInt("categ"); 
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
				}
			}
		}
		
		
		return cat_id;

	}	//	getCategory
		
	
	
	/**
	 * 	Perform Work.
	 * 	Set Text Msg.
	 * 	@param p_trx transaction
	 *	@return true if completed, false otherwise
	 *	@throws Exception if error
	 */
	protected boolean performWork (Trx p_trx) throws Exception
	{
		
		String action = m_node.getAction();

//		System.out.println(m_node.getAction() + " " + X_AD_WF_Node.ACTION_SubWorkflow );
		/******	Sleep (Start/End)			******/
		
		if (X_AD_WF_Node.ACTION_SubWorkflow.equals(action)) {
			log.info (m_node + " " + p_trx);
//			System.out.println(m_node + " " + p_trx);
			if (m_node.getPriority() != 0)		//	overwrite priority if defined
				setPriority(m_node.getPriority());
			
//			System.out.println("Workflow:AD_Workflow_ID=" + m_node.getAD_Workflow_ID());
			//log.warning ("Workflow:AD_Workflow_ID=" + m_node.getAD_Workflow_ID());
			//Inicia proceso de Workflow
			MWorkflow wf = MWorkflow.get (Env.getCtx(), m_node.getAD_Workflow_ID());
			MPInstance mpi = new MPInstance( Env.getCtx(), m_node.getAD_Workflow_ID(), getRecord_ID()); 
			mpi.save();
			ProcessInfo pi = new ProcessInfo("", m_node.getAD_Workflow_ID()); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(m_node.getAD_Workflow_ID()); 
			pi.setClassName(""); 
			pi.setTitle(""); 
			ProcessCtl pc = new ProcessCtl(null ,pi,null); 
			pc.start();
			return true;
		} else {
			super.performWork(p_trx);
		}
		//
		throw new IllegalArgumentException("Invalid Action (Not Implemented) =" + action);
	}	//	performWork

	
	
}
