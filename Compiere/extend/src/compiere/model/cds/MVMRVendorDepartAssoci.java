package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MRole;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRVendorDepartAssoci extends X_XX_VMR_VendorDepartAssoci {
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRVendorDepartAssoci.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_VMR_VendorDepartAssoci_ID id
	 *	@param trx transaction
	 */
	public MVMRVendorDepartAssoci(Ctx ctx, int XX_VMR_VendorDepartAssoci_ID,	Trx trx) 
	{
		super(ctx, XX_VMR_VendorDepartAssoci_ID, trx);
	}//MVCNVendorCountryDistri
	
	/** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
	
	/**
	 * 	Consulta la tabla XX_VMR_VENDORDEPARTASSOCI, devuelve si esta activo el que esta actualmente en la BD
	 */
	private String ConsultaTablaXX_VMR_VENDORDEPARTASSOCIActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM XX_VMR_VENDORDEPARTASSOCI "
				   + "WHERE XX_VMR_VENDORDEPARTASSOCI_ID = " + getXX_VMR_VENDORDEPARTASSOCI_ID();
		
		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("ISACTIVE");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "ERROR";
		}
	}//ConsultaTablaXX_VMR_VENDORDEPARTASSOCIActive
    
	/**
	 * 	Consultar BPartner
	 */
	private Integer ConsultarTablasBPartner(int C_BPartner_ID, String Tabla, String Contacto){
		Integer aux = null;
		String sql = "SELECT COUNT(*) "
				   + "FROM "+ Tabla +" "
				   + "WHERE C_BPARTNER_ID = " + C_BPartner_ID + " ";
		
		if (Contacto != null){
			sql = sql + " and XX_ContactType = "+ Contacto;
		}
		
		sql = MRole.getDefault().addAccessSQL(
				sql, "", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		//System.out.println(sql);
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getInt("COUNT(*)");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return aux;
		}
	}//ConsultarTablasBPartner

	/**
	 * 
	 */
	protected boolean beforeDelete() {
		X_XX_VMR_VendorDepartAssoci vendorDepartment = new X_XX_VMR_VendorDepartAssoci(getCtx(),get_ID(), null);		
		Integer PaisesDistrib = ConsultarTablasBPartner(vendorDepartment.getC_BPartner_ID(), "XX_VMR_VendorDepartAssoci", null);
		
		if (PaisesDistrib == 1){
			String mensaje = Msg.getMsg(getCtx(), "XX_NotDeleteInactiveVendor", new String[] {" Department Associated"});
			log.saveError("Error", mensaje);
			//ADialog.info(1, new Container(), mensaje);
			return false;
		}else{
			return true;
		}
	}//beforeDelete
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRVendorDepartAssoci(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVMRVendorDepartAssoci
	
	/**
	 * 	Consulta los departamentos asociados a un Proveedor
	 */
	private String ConsultaDepartamentosAsociados(int Department_ID, int C_BPartner_ID){
		String aux = "VACIO";

		String sql = "SELECT count(*) "
				   + "FROM XX_VMR_VendorDepartAssoci "
				   + "WHERE C_BPartner_ID = " + C_BPartner_ID + " AND " 
				   		 + "XX_VMR_DEPARTMENT_ID = " + Department_ID;

		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("COUNT(*)");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return aux;
		}
	}//ConsultaListaPaymentRule
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{	
		String CantidadDepartamento = ConsultaDepartamentosAsociados(getXX_VMR_Department_ID(), getC_BPartner_ID());
		String auxActivoViejo = ConsultaTablaXX_VMR_VENDORDEPARTASSOCIActive();
		String auxActivoNuevo = getisActive();
		
		if (!newRecord){
			if(auxActivoViejo.equals(auxActivoNuevo)){
				if (CantidadDepartamento.equals("VACIO")){
					return true;
				}else if (new Integer (CantidadDepartamento).intValue() <= 0 ){
					return true;
				}else{
					log.saveError("Error", Msg.getMsg(getCtx(), "Ya esta Asociado ese departamento a este Proveedor"));
					return false;			
				}
			}else if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){
				return true;
			}else if(auxActivoViejo.equals("N") && auxActivoNuevo.equals("Y")){
				return true;
			} else
				return false;
		}else{
			if (CantidadDepartamento.equals("VACIO")){
				return true;
			}else if (new Integer (CantidadDepartamento).intValue() <= 0 ){
				return true;
			}else{
				log.saveError("Error", Msg.getMsg(getCtx(), "Ya esta Asociado ese departamento a este Proveedor"));
				return false;			
			}
		}
		
	}//	beforeSave

}//MVCNVendorCountryDistri
