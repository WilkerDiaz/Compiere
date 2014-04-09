package compiere.model.cds.imports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_XX_VCN_SalePurchase;

public class XX_ImportSalePurchase extends SvrProcess {

	Vector<Integer> departmentIDs = new Vector<Integer>();
	Vector<String> departmentCodes = new Vector<String>();
	Vector<String> storeCodes = new Vector<String>();
	Vector<Integer> storeIDs = new Vector<Integer>();
	int year = 0;
	int month = 0;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Month"))
				month = new Integer(element.getParameter().toString());
			else if (name.equals("Year"))
				year = new Integer(element.getParameter().toString());
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		
		String queryDelete = "DELETE FROM XX_VCN_SALEPURCHASE WHERE XX_MONTH = "+ month +" AND XX_YEAR = " + year;
		
		PreparedStatement pstmtQuery = DB.prepareStatement(queryDelete, null);
	    pstmtQuery.executeUpdate(queryDelete);
	    pstmtQuery.close();
	    
	     
		//Busqueda de los registros del mes
		As400DbManager As = new As400DbManager();
		As.conectar();
		
		Statement statementAS400=null;
		
		statementAS400 = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = null;
		
		String sql = "SELECT * FROM ictfile.INVD02 " +
					 "WHERE CVAÑO0 = 2013 AND CVMES0 = 3 " +
					 "ORDER BY CVTIE0, CVDPT0, CVTRE0";                        
		
		try
        {          
 	
			rs= As.realizarConsulta(sql,statementAS400);
			
			int dep=0;
			int store=0;
			int inserted=0;
			X_XX_VCN_SalePurchase sp = null;
			
			while (rs.next())
			{
				inserted++;
				if(inserted==1){
					
					getAllDeparments();
					getAllStores();
				}
				
				//Codigo de Tienda
				store = getStoreID(rs.getString("CVTIE0"));
				if(store==0)
					continue;
				
				//Codigo de Departamento
				dep = getDepartmentID(rs.getString("CVDPT0"));
				if(dep==0)
					continue;

			    //Se genera el registro
			    sp = new X_XX_VCN_SalePurchase(Env.getCtx(), 0, get_TrxName());
			    sp.setM_Warehouse_ID(store);
		    	sp.setXX_VMR_Department_ID(dep);
		    	sp.setXX_Month(month);
		    	sp.setXX_Year(year);
		    	sp.setXX_AmountMonth(rs.getBigDecimal("CVMOM0"));
		    	sp.setXX_TypeReg(String.format("%02d", rs.getInt("CVTRE0")));
		       	sp.setXX_AmountAcu(rs.getBigDecimal("CVMOA0"));		
			    sp.save();	
			}
			
			statementAS400.close();
			rs.close();
			As.desconectar();
			
        }catch (Exception e) {
        	e.printStackTrace();
		}
		finally{
			statementAS400.close();
        	As.desconectar();
			DB.closeResultSet(rs);
			DB.closeStatement(pstmtQuery);
		}
		
		return "Fin de la Impotacion";
	}
	
	/*
	 * Obtiene todas
	 */
	private void getAllStores(){
		
		String sql = "SELECT VALUE, M_WAREHOUSE_ID " +
				     "FROM M_WAREHOUSE " +
				     "WHERE UPPER(VALUE) = LOWER(VALUE) " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				storeCodes.add(rs.getString("VALUE"));
				storeIDs.add(rs.getInt("M_WAREHOUSE_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}

	/*
	 * ID de la tienda especificada
	 */
	private int getStoreID(String cellValue){
		
		int index=-1;  // OJO CON ESTO (ACTUALMENTE LAS TIENDAS TIENEN 3 DIGITOS Ejm: 002)
		if(cellValue.length()==1){
			cellValue = "00" + cellValue;
		}else if (cellValue.length()==2) {
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<storeCodes.size(); i++){
			
			if(storeCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return storeIDs.get(index);
		else
			return 0;
	}
	
	/*
	 * Obtiene todos los departamentos
	 */
	private void getAllDeparments(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID " +
				     "FROM XX_VMR_DEPARTMENT WHERE  " +
				     " AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				departmentCodes.add(rs.getString("VALUE"));
				departmentIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	/*
	 * Obtiene el ID del departamento indicado
	 */
	private int getDepartmentID(String cellValue){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<departmentCodes.size(); i++){
			
			if(departmentCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return departmentIDs.get(index);
		else
			return 0;
	}

}
