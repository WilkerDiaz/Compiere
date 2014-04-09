package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVCNPercenReten extends X_XX_VCN_PercenReten{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVCNPercenReten.class);

	public MVCNPercenReten(Ctx ctx, int C_Order_ID, Trx trx) {
		super(ctx, C_Order_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	/** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
    
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MVCNPercenReten(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVCNPercenReten
	
	/**
	 * 	Consulta la tabla XX_VCN_PERCENRETEN, devuelve si esta activo el que esta actualmente en la BD
	 */
	private String ConsultaTablaXX_VCN_PercenRetenActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM XX_VCN_PERCENRETEN "
				   + "WHERE XX_VCN_PERCENRETEN_ID = " + getXX_VCN_PERCENRETEN_ID();
		
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
	}//ConsultaTablaXX_VCN_PercenRetenActive
	
	/**
	 * 	Consulta si tiene algun c_BPartner asociado
	 */
	private String ConsultaProveedoresConPorcenReten(String XX_PercentajeRetention_ID){
		String aux = "VACIO";

		String sql = "SELECT COUNT(*) "
				   + "FROM C_BPartner "
				   + "WHERE XX_PercentajeRetention_ID = '" + XX_PercentajeRetention_ID + "' and "
				   		  +"XX_TypeTaxPayer_ID not in (Select XX_TypeTaxPayer_ID from XX_TypeTaxPayer where upper(name) <> 'FORMAL' ) ";

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
			// TODO: handle exception
				return "ERROR";
		}
	}//ConsultaProveedoresConPorcenReten	

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{	
		if (!newRecord){
			String CantidadCBPartner = ConsultaProveedoresConPorcenReten( ""+getXX_VCN_PERCENRETEN_ID() );
			String auxActivoViejo = ConsultaTablaXX_VCN_PercenRetenActive();
			String auxActivoNuevo = getisActive();
	
			if(auxActivoViejo.equals(auxActivoNuevo)){
				/*if (CantidadCBPartner.equals("VACIO")){
					return true;
				}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
					return true;
				}else
					return false;*/
				return true;
			}else if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){
				/*if (CantidadCBPartner.equals("VACIO")){
					return true;
				}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
					return true;
				}else{
					log.saveError("Error", Msg.getMsg(getCtx(), "No se puede Desactivar los Porcentajes de Retencion. Está asociados a un Proveedor"));
					return false;
				}*/
				log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotDesactive"));
				return false;
			}else if(auxActivoViejo.equals("N") && auxActivoNuevo.equals("Y")){
				return true;
			} else
				return false;
		}else
			return true;
	}//beforeSave
	

	/**
	 * 	Before Delete
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete()
	{	
		log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotDelete"));
		return false;
	}//	beforeDelete
}
