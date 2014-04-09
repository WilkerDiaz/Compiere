package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRTypeTax extends X_XX_VMR_TypeTax{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRTypeTax.class);

	public MVMRTypeTax(Ctx ctx, int C_Order_ID, Trx trx) {
		super(ctx, C_Order_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRTypeTax(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MVMRTypeTax
    
	/** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
    
	/**
	 * 	Consulta la tabla XX_VMR_TypeTax, devuelve si esta activo el que esta actualmente en la BD
	 */
	private String ConsultaTablaXX_VMR_TypeTaxActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM XX_VMR_TypeTax "
				   + "WHERE XX_VMR_TypeTax_ID = " + getXX_VMR_TYPETAX_ID();
		
		try{
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
	}//ConsultaTablaXX_VMR_TypeTaxActive
	
	/**
	 * 	Consulta si tiene algun C_BPartner asociado
	 */
	private String ConsultaProveedoresConTypeTax(String XX_TypeTax_ID){
		String aux = "VACIO";

		String sql = "SELECT COUNT(*) "
				   + "FROM C_BPartner "
				   + "WHERE XX_TypeTax_ID = '" + XX_TypeTax_ID + "'";

		try{
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
	}//ConsultaProveedoresConTypeTax	

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){	
		if (!newRecord){
			String CantidadCBPartner = ConsultaProveedoresConTypeTax( ""+getXX_VMR_TYPETAX_ID() );
			String auxActivoViejo = ConsultaTablaXX_VMR_TypeTaxActive();
			String auxActivoNuevo = getisActive();

			if(auxActivoViejo.equals(auxActivoNuevo)){
				/*if (CantidadCBPartner.equals("VACIO")){
					return true;
				}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
					return true;
				}else
					return false;/*/
				return true;
			}else if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){
				if (CantidadCBPartner.equals("VACIO")){
					return true;
				}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
					return true;
				}else{
					log.saveError("Error", Msg.getMsg(getCtx(), "No se puede Desactivar los Tipos de Impuestos. Esta asociado a un proveedor"));
					return false;
				}
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
		log.saveError("Error", Msg.getMsg(getCtx(), "No se puede Borrar los Tipos de Impuestos"));
		return false;
	}//	beforeDelete
}
