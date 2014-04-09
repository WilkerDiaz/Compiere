package compiere.model.dynamic.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.X_XX_VMA_BrochurePage;
import compiere.model.dynamic.X_XX_VMA_PageDepartment;

/**
 * 
 * Esta clase contiene el proceso que se va a ejecutar al momento de querer
 * eliminar un departamento que fue asociado a una página del folleto
 * 
 * @author Maria Vintimilla
 * @version 1.0
 */

public class XX_VME_DropDepOfPage extends SvrProcess{

	// Identificador de la página del folleto
	private int p_XX_VMA_BrochurePage_ID = 0;
	
	// Y si se van a borrar todos los elementos, N si no.
	private String DeleteAll = "N";
	
	// Y si se van a mover los elementos, N si no.
	private String MoveAll = "N";
	
	// De querer mover los elementos, se selecciona la página a la cual se 
	// quieren mover.
	private int Page = 0;
	
	private int Dep = 0;
	
	/**
	 * prepare
	 * Se toman los parámetros del proceso, entre ellos tenemos la opción de 
	 * borrar todos los elementos asociados, mover los elementos asociados y 
	 * la página a donde se quieren mover.
	 */
	protected void prepare() {	
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;	
			else if (name.equals("XX_VMA_DeleteAll"))
				DeleteAll = (String) para[i].getParameter();
			else if (name.equals("XX_VMA_MoveAll"))
				MoveAll = (String) para[i].getParameter();
			else if (name.equals("XX_VMA_Page"))
				Page = para[i].getParameterAsInt();
			else if (name.equals("XX_VMR_Department_ID"))
				Dep = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
//		System.out.println("Delete all: "+DeleteAll+" Move all: "+MoveAll
//				+" Page: "+Page+" Dep: "+Dep);
		p_XX_VMA_BrochurePage_ID = getRecord_ID();
	} // prepare
	
	/**
	 * doIt
	 * procedimiento que ejecuta la eliminación del departamento asociado
	 * a una página del folleto
	 */
	protected String doIt() throws Exception {
		int res = 0;
		// Pagina
		X_XX_VMA_BrochurePage page = 
			new X_XX_VMA_BrochurePage(Env.getCtx(),p_XX_VMA_BrochurePage_ID, null);

		// Relacion pagina/departamento
		X_XX_VMA_PageDepartment pd = 
			new X_XX_VMA_PageDepartment(Env.getCtx(),page.getXX_VMA_PageDept_V_ID(),null);
		
		int departamento = pd.getXX_VMR_Department_ID();

		/* En caso de que los parámetros se mantengan en su estado inicial, solo 
		 * se va a poder eliminar el departamento si no hay elementos asociados 
		 * al mismo en la página */
		if(DeleteAll.equals("N") && MoveAll.equals("N") && Page==0){
			deleteDepartamento(departamento);
			page.setXX_VMA_PageDept_V_ID(0);
			page.save();
			return Msg.translate(Env.getCtx(), "XX_DeptRemoved");
		} // if
		/* Si se selecciona borrar los elementos, se borra tanto el departamento 
		 * como los elementos asociados */
		else if(DeleteAll.equals("Y")&& MoveAll.equals("N")&& Page==0){
			res = deleteElementos(departamento);
			page.setXX_VMA_PageDept_V_ID(0);
			page.save();
			return Msg.getMsg( Env.getCtx(), "XX_DeptDel", 
					new String[]{Integer.toString(res)});
		} // else
		/* Si se selecciona borrar los elementos o no, pero además se selecciona 
		 * mover los elementos a una página, se proceden a cambiar las referencias 
		 * de los elementos a la nueva página y se elimina el departamento de la 
		 * página */
		else if((DeleteAll.equals("Y")||DeleteAll.equals("N"))&& 
				MoveAll.equals("Y")&& Page!=0){
			res = updateElementos(departamento);
			page.setXX_VMA_PageDept_V_ID(0);
			page.save();
			return Msg.getMsg(Env.getCtx(), "XX_ItemsAdded",
					new String[]{Integer.toString(res)});
		} // else if
		/* Caso en que selecciona una página y no la opción de mover los elementos, 
		 * da error */
		else if ((DeleteAll.equals("Y")||DeleteAll.equals("N"))&& MoveAll.equals("N")&& Page!=0){
			page.setXX_VMA_PageDept_V_ID(0);
			page.save();
			ADialog.error(Env.WINDOW_INFO, null, 
					Msg.translate(Env.getCtx(), "XX_NotDelDept"));
			throw new Exception(Msg.translate(Env.getCtx(), "XX_Modifications"));
		} // else if
		/* Caso en que se selecciona mover los elementos pero no se seleccionó la 
		 * página a donde moverlos */
		else{
			page.setXX_VMA_PageDept_V_ID(0);
			page.save();
			ADialog.error(Env.WINDOW_INFO, null, "Error", 
					Msg.translate(Env.getCtx(), "XX_DepartmentDel"));
			throw new Exception(Msg.translate(Env.getCtx(), "XX_Modifications"));
		} // else
	} // Fin doIt
	
	/**
	 *  deleteDepartamento
	 * Esta función se encarga de verificar que no haya ningún elemento asociado
	 * a la página, porque de lo contrario no se puede borrar el departamento. Si
	 * no existe un elemento asociado, se borra el departamento de manera correcta.
	 * @author Alejandro Prieto
	 * @author Maria Vintimilla
	 * @param departamento	departamento que se quiere eliminar de la página del folleto
	 * @throws Exception
	 */
	private void deleteDepartamento(int departamento) throws Exception{
		String sql = "SELECT * " +
				" FROM XX_VME_PRODUCT P INNER JOIN XX_VME_REFERENCE R " +
				" ON (P.XX_VME_REFERENCE_ID = R.XX_VME_REFERENCE_ID) " +
				" INNER JOIN XX_VME_ELEMENTS E " +
				" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
				" INNER JOIN XX_VMA_BrochurePage BP " +
				" ON (E.XX_VMA_BROCHUREPAGE_ID = BP.XX_VMA_BROCHUREPAGE_ID) " +
				" INNER JOIN XX_VMA_PageDepartment PD " +
				" ON (BP.XX_VMA_BrochurePage_ID = PD.XX_VMA_BrochurePage_ID) " +
				" WHERE  PD.XX_VMA_BrochurePage_ID = " + p_XX_VMA_BrochurePage_ID+
				" AND R.XX_VMR_Department_ID = PD.XX_VMR_Department_ID " +
				" AND PD.XX_VMR_Department_ID = " + departamento +
				" AND rownum = 1";
//		System.out.println("SQLDELDEP: " + sql);
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int res = 0;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				ADialog.error(Env.WINDOW_INFO, null, "Error",
						Msg.translate(Env.getCtx(), "XX_DepElemAsso"));
				throw new Exception(Msg.translate(Env.getCtx(), "XX_Modifications"));
			} // if
			else{
				
				// Sentencia que permite borrar el departamento asociado a la página
				sql = "DELETE FROM XX_VMA_PageDepartment " +
						" WHERE XX_VMR_Department_ID = " + departamento +
						" AND XX_VMA_BrochurePage_ID = " + p_XX_VMA_BrochurePage_ID;
				 int result = DB.executeUpdateEx(sql, get_Trx());
			} // else
		} // try
		catch(SQLException e){
			throw new Exception(Msg.getMsg(Env.getCtx(), "XX_UpdateElement", 
					new String[]{e.toString()}));
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // Fin deleteDepartamento
	
	/**
	 * deleteElementos
	 * Esta función permite eliminar los elementos asociados al departamento 
	 * que se quiere eliminar. Además elimina el departamento asociado a la 
	 * página
	 * @author Alejandro Prieto
	 * @author Maria Vintimilla
	 * @param departamento 	departamento que se quiere eliminar de la página 
	 * 						del folleto
	 * @param pageDep Página de la cuál se desea borrar el departamento
	 * @return número de elementos que se eliminaron
	 * @throws Exception
	 */
	private int deleteElementos(int departamento) throws Exception {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		int[] deleteCounts;
		int elementId = 0;
		
		String SQLElem = " SELECT E.XX_VME_ELEMENTS_ID " +
				" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
				" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
				" WHERE E.XX_VMA_BrochurePage_ID = " + p_XX_VMA_BrochurePage_ID +
				" AND R.XX_VMR_DEPARTMENT_ID = " + departamento;
//		System.out.println("SQLDELETE : " + SQLElem);
		
		try {
			pstmt = DB.prepareStatement(SQLElem,null);
			rs = pstmt.executeQuery();
			stmt = DB.createStatement();		
			
			while(rs.next()){
				/* Sentencia que permite eliminar los elementos asociados a un 
				 * grupo de productos, así como a la página y departamento que 
				 * se quiere eliminar */
				elementId = rs.getInt(1);
				String SQLDelProd = " DELETE FROM XX_VME_PRODUCT " +
								" WHERE XX_VME_REFERENCE_ID IN (" +
								" SELECT XX_VME_REFERENCE_ID " +
								" FROM XX_VME_REFERENCE " +
								" WHERE XX_VME_ELEMENTS_ID = " + elementId +
								" AND XX_VMR_DEPARTMENT_ID = " + departamento +")";
				//System.out.println("otra sentencia : " + SQLDelProd);
				
				String SQLDelRef = " DELETE FROM XX_VME_REFERENCE " +
								" WHERE XX_VME_ELEMENTS_ID = " + elementId +
								" AND XX_VMR_DEPARTMENT_ID = " + departamento;
				//System.out.println("otra sentencia : " + SQLDelRef);
				
				String SQLDelElem = " DELETE FROM XX_VME_ELEMENTS " +
								" WHERE XX_VME_ELEMENTS_ID = " + elementId;
				stmt.addBatch(SQLDelProd);
				stmt.addBatch(SQLDelRef);
				stmt.addBatch(SQLDelElem);
			}
			
			/* Se agrega al batch la sentencia para eliminar la relación 
			 * página-departamento */
			stmt.addBatch(" DELETE FROM XX_VMA_PAGEDEPARTMENT " +
						" WHERE XX_VMR_DEPARTMENT_ID = " + departamento+
						" AND XX_VMA_BROCHUREPAGE_ID = " + p_XX_VMA_BrochurePage_ID);
			
			deleteCounts = stmt.executeBatch();
			stmt.close();
			
			return sumaBatch(deleteCounts);
		} // try
		catch(SQLException e){
			throw new Exception(Msg.getMsg(Env.getCtx(), "XX_UpdateElement", 
					new String[]{e.toString()}));
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // Fin deleteElementos
	
	/**
	 * updateElementos
	 * Esta función además de eliminar el departamento asociado a la página, 
	 * permite mover los elementos a otra página del folleto sin tener que 
	 * eliminarlos.
	 * @author Alejandro Prieto
	 * @author Maria Vintimilla
	 * @param departamento	departamento que se va a eliminar de la página del 
	 * 						folleto
	 * @return				número de elementos que se movieron a otra página
	 * @throws Exception
	 */
	private int updateElementos(int departamento)throws Exception{
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		int[] deleteCounts;
		int i = 0;
		ArrayList array = new ArrayList();
		
		/* Query sql para verificar que el departamento existe o no en la página 
		 * a la que se van a mover los elementos */
		String SQLDep = " SELECT * " +
				" FROM XX_VMA_PAGEDEPARTMENT " +
				" WHERE XX_VMA_BROCHUREPAGE_ID = " + Page + 
				" AND XX_VMR_DEPARTMENT_ID = "+departamento;
		//System.out.println("primer query : "+sql);
		
		/* Sentencia sql para poder eliminar el departamento de la página */ 
		String sqlDelete = " DELETE FROM XX_VMA_PageDepartment " +
					" WHERE XX_VMR_DEPARTMENT_ID = " + departamento +
					" AND XX_VMA_BROCHUREPAGE_ID = " + p_XX_VMA_BrochurePage_ID;
		//System.out.println("primer query : "+sqlDelete);

		X_XX_VMA_BrochurePage page1 = 
			new X_XX_VMA_BrochurePage(Env.getCtx(),Page,null);
		String PageType = page1.getXX_VMA_PageType();
		try{
			pstmt = DB.prepareStatement(SQLDep,null);
			rs = pstmt.executeQuery();
			if(!rs.next()){
				/* Se crea el departamento en la página a donde se van a mover 
				 * los elementos */
				X_XX_VMA_PageDepartment pd = 
					new X_XX_VMA_PageDepartment(Env.getCtx(),0,null);
				pd.setXX_VMA_BrochurePage_ID(Page);
				pd.setXX_VMR_Department_ID(departamento);
				pd.save();
			}
			stmt = DB.createStatement();
			
			/* Se verifica si la página a donde se va es de imágenes */
			if(PageType.equals("I")){
				/* Se buscan los elementos de tipo imagen que se van a mover */
				String SQLIma = " SELECT E.XX_VME_ELEMENTS_ID ID" +
					" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
					" WHERE E.XX_VMA_BROCHUREPAGE_ID = " + p_XX_VMA_BrochurePage_ID +
					" AND R.XX_VMR_DEPARTMENT_ID = " + departamento +
					" AND E.XX_VME_TYPE = 'I'";
				pstmt = DB.prepareStatement(SQLIma,null);
				rs = pstmt.executeQuery();
				if(rs.next()){
					int elementID = rs.getInt("ID");
					/* Sentencia sql que permite cambiar los elementos de tipo 
					 * imagen a la página que se eligió */
					String SQLUpd = " UPDATE XX_VME_ELEMENTS " +
						" SET XX_VMA_BROCHUREPAGE_ID = " + Page +
						" WHERE XX_VME_ELEMENTS_ID = "+ elementID +
						" AND E.XX_VME_Type = 'I')";
					//System.out.println("SQLUpd: " + SQLUpd);
					stmt.addBatch(SQLUpd);
					
					/* Sentencia sql que elimina los demás elementos que no son 
					 * de tipo imagen */
					String SQLUpd2 = " DELETE FROM XX_VME_ELEMENTS " +
						" WHERE XX_VME_ELEMENTS_ID IN " +
						" (SELECT E.XX_VME_ELEMENTS_ID " +
						" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
						" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
						" WHERE E.XX_VMA_BROCHUREPAGE_ID = " + p_XX_VMA_BrochurePage_ID +
						" AND R.XX_VMR_DEPARTMENT_ID = " + departamento +
						" AND E.XX_VME_Type != 'I')";
					//System.out.println("SQLUpd2" + SQLUpd2);
					stmt.addBatch(SQLUpd2);
					deleteCounts = stmt.executeBatch();
					stmt.close();
					return sumaBatch(deleteCounts);	
				} // if 
				else{
					rs.close();
					pstmt.close();
					throw new Exception(Msg.translate(Env.getCtx(), "XX_MoveProduct"));
				}	
			/* Se verifica que la página a donde se van a mover los elementos 
			 * sea de tipo producto */
			} // if pagina imagen
			else if(PageType.equals("P")){
				/* Sentencia sql que permite mover los elementos diferentes a 
				 * una imagen a la página que se seleccionó como parámetro */
				String SQLUpd = " UPDATE XX_VME_ELEMENTS " +
					" SET XX_VMA_BROCHUREPAGE_ID = " + Page +
					" WHERE XX_VME_ELEMENTS_ID IN " +
					" (SELECT E.XX_VME_ELEMENTS_ID " +
					" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
					" WHERE E.XX_VMA_BROCHUREPAGE_ID = " + p_XX_VMA_BrochurePage_ID +
					" AND R.XX_VMR_DEPARTMENT_ID = " + departamento +
					" AND E.XX_VME_Type = 'P')";
				//System.out.println("SQLUpd" + SQLUpd);
				stmt.addBatch(SQLUpd);
				/* Sentencia sql que eliminar los elementos de tipo imagen que 
				 * no se pueden agregar a una página de productos */
				String SQLDel =  " DELETE FROM XX_VME_ELEMENTS " +
					" WHERE XX_VME_ELEMENTS_ID IN " +
					" (SELECT E.XX_VME_ELEMENTS_ID " +
					" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
					" WHERE E.XX_VMA_BROCHUREPAGE_ID = " + p_XX_VMA_BrochurePage_ID +
					" AND R.XX_VMR_DEPARTMENT_ID = " + departamento +
					" AND E.XX_VME_Type = 'I')";
				//System.out.println("SQLDel: " + SQLDel);
				stmt.addBatch(SQLDel);

				/* Se agrega la sentencia que elimina el departamento de la página */
				stmt.addBatch(sqlDelete);
				/* Se ejecuta el batch de sentencias */
				deleteCounts = stmt.executeBatch();
				stmt.close();
				return sumaBatch(deleteCounts);		
			} // if
			/* El tipo de pagina es MIXTA */
			else{
				/* Cuando la página a donde se mueven los elementos es mixta, se 
				 * procede a mover todo tipo de elemento que este asociado al 
				 * departamento que se quiere eliminar
				 */
				String SQLDel = " UPDATE XX_VME_ELEMENTS " +
					" SET XX_VMA_BROCHUREPAGE_ID = " + Page +
					" WHERE XX_VME_ELEMENTS_ID IN " +
					" (SELECT E.XX_VME_ELEMENTS_ID " +
					" FROM XX_VME_ELEMENTS E INNER JOIN XX_VME_REFERENCE R " +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
					" WHERE E.XX_VMA_BROCHUREPAGE_ID = " + p_XX_VMA_BrochurePage_ID +
					" AND R.XX_VMR_DEPARTMENT_ID = " + departamento; 
				//System.out.println("SQLDel: " + SQLDel);
				stmt.addBatch(SQLDel);
				
				/* Se agrega la sentencia para eliminar el departamento al Batch */
				stmt.addBatch(sqlDelete);
				/* Se ejecuta el batch de sentencias */
				deleteCounts = stmt.executeBatch();
				stmt.close();
				return sumaBatch(deleteCounts);
			}// else
		}
		catch(SQLException e){
			throw new Exception("No se puedieron actualizar los elementos por " +
					"el siguiente error:"+e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	} // Fin updateElementos
		
	/**
	 * sumaBatch
	 * Esta función suma los totales de las sentencias que se ejcutan en Batch
	 * @author Alejandro Prieto
	 * @param sentence	arreglo de los totales de los registros que se modificaron 
	 * 					ejecutando el Batch
	 * @return			total de registros alterados
	 */
	private int sumaBatch(int[] sentence){
		int valor=0;
		for(int i = 0; i < sentence.length; i++){
			valor += sentence[i];
		}
		return valor;
	} // Fin sumaBatch
	
} // Fin XX_VMA_DropDeptOfPage
