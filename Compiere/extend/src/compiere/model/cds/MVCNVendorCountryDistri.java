package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MRole;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVCNVendorCountryDistri extends X_XX_VCN_VendorCountryDistri {
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVCNVendorCountryDistri.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_VCN_VendorCountryDistri_ID id
	 *	@param trxName transaction
	 */
	public MVCNVendorCountryDistri(Ctx ctx, int XX_VCN_VendorCountryDistri_ID,	Trx trx) 
	{
		super(ctx, XX_VCN_VendorCountryDistri_ID, trx);
	}//MVCNVendorCountryDistri

	/** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
	
	/**
	 * 	Consulta la tabla XX_VCN_VendorCountryDistri, devuelve si esta activo el que esta actualmente en la BD
	 */
	private String ConsultaTablaXX_VCN_VendorCountryDistriActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM XX_VCN_VendorCountryDistri "
				   + "WHERE XX_VCN_VendorCountryDistri_ID = " + getXX_VCN_VENDORCOUNTRYDISTRI_ID();
		
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
	}//ConsultaTablaXX_VCN_VendorCountryDistriActive
    
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MVCNVendorCountryDistri(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVCNVendorCountryDistri
	
	/**
	 * 	Consulta la los paises de distribucion por un Proveedor
	 */
	private String ConsultaPaisesDistribucion(int Country_ID, int C_BPartner_ID){
		String aux = "VACIO";

		String sql = "SELECT count(*) "
				   + "FROM XX_VCN_VendorCountryDistri "
				   + "WHERE C_BPartner_ID = " + C_BPartner_ID + " AND " 
				   		 + "XX_country_id = " + Country_ID;

		try{
			////System.out.println(sql);
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
			// TODO: handle exception
				return aux;
		}
	}//ConsultaPaisesDistribucion

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
		X_XX_VCN_VendorCountryDistri vendorCountry = new X_XX_VCN_VendorCountryDistri(getCtx(),get_ID(), null);		
		Integer PaisesDistrib = ConsultarTablasBPartner(vendorCountry.getC_BPartner_ID(), "XX_VCN_VendorCountryDistri", null);
		
		if (PaisesDistrib == 1){
			String mensaje = Msg.getMsg(getCtx(), "XX_NotDeleteInactiveVendor", new String[] {"Country Distribution"});
			log.saveError("Error", mensaje);
			//ADialog.info(1, new Container(), mensaje);
			return false;
		}else{
			return true;
		}
	}//beforeDelete
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{	
		String CantidadPaises = ConsultaPaisesDistribucion(getXX_Country_ID(), getC_BPartner_ID());
		String auxActivoViejo = ConsultaTablaXX_VCN_VendorCountryDistriActive();
		String auxActivoNuevo = getisActive();
		
		
		if (!newRecord){
			if(auxActivoViejo.equals(auxActivoNuevo)){
				if (CantidadPaises.equals("VACIO")){
					return true;
				}else if (new Integer (CantidadPaises).intValue() <= 0 ){
					return true;
				}else{
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_VendorCountry"));
					return false;			
				}
			}else if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){
				return true;
			}else if(auxActivoViejo.equals("N") && auxActivoNuevo.equals("Y")){
				return true;
			} else
				return false;
		}else{
			if (CantidadPaises.equals("VACIO")){
				return true;
			}else if (new Integer (CantidadPaises).intValue() <= 0 ){
				return true;
			}else{
				log.saveError("Error", Msg.getMsg(getCtx(), "XX_VendorCountry"));
				return false;			
			}
		}

	}//	beforeSave

}//MVCNVendorCountryDistri
