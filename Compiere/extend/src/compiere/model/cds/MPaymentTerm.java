package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MPaymentTerm extends org.compiere.model.MPaymentTerm{
	
	private static final long serialVersionUID = 1L;

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	public MPaymentTerm(Ctx ctx, int C_Order_ID, Trx trx) {
		super(ctx, C_Order_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPaymentTerm(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MPaymentTerm
	
    /** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
    
	/**
	 * 	Consulta la tabla C_PAYMENTTERM, devuelve si esta activo el que esta actualmente en la BD
	 */
	private String ConsultaTablaC_PaymentTermActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM C_PAYMENTTERM "
				   + "WHERE C_PAYMENTTERM_ID = " + getC_PaymentTerm_ID();
		
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
	}//ConsultaTablaC_PaymentTermActive
	
	/**
	 * 	Consulta si tiene algun c_BPartner asociado
	 */
	private String ConsultaProveedoresConPaymentTerm(String PO_PaymentTerm_ID){
		String aux = "VACIO";

		String sql = "SELECT COUNT(*) "
				   + "FROM C_BPartner "
				   + "WHERE PO_PaymentTerm_ID = '" + PO_PaymentTerm_ID + "'";

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
	}//ConsultaProveedoresConPaymentTerm	

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	
		/* Comentado por Jessica Mendoza
		 * Se comentó porque el beforeSave del modelo de Compiere llama al método validate() 
		 * que se encarga de volver a validar la condición de pago. Dicho proceso fue sustituido 
		 * por la clase XX_ValidatePaymentTerm que se encarga de validar todos los tipos de 
		 * condiciones de pago, a diferencia del validate(), que solo valida las condiciones de
		 * pago simples, es decir, donde le porcentaje inicial es 100% */
		//if(super.beforeSave(newRecord)){
			
			/****Jessica Mendoza****/
			//Se busca el máximo valor de la clave de búsqueda y se suma 1
			if (newRecord){
				int value = 0;
				String sql = "select max(to_number(value)) from C_PaymentTerm where value <> 'Immediate' ";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {	
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();			
					if(rs.next()) {
						value = rs.getInt(1) + 1;
						setValue(String.valueOf(value));
					}
				}catch(SQLException e){	
					e.getMessage();
				} 
				finally{
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}				
			/****Fin código - Jessica Mendoza****/
			
			String CantidadCBPartner = ConsultaProveedoresConPaymentTerm( ""+getC_PaymentTerm_ID() );
			String auxActivoViejo = ConsultaTablaC_PaymentTermActive();
			String auxActivoNuevo = getisActive();

			if (!newRecord){
				if(auxActivoViejo.equals(auxActivoNuevo)){
					/*System.out.println(CantidadCBPartner);
					if (CantidadCBPartner.equals("VACIO")){
						return true;
					}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
						return true;
					}else
						return false;*/
					return true;
				}else if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){
					//System.out.println(CantidadCBPartner);
					if (CantidadCBPartner.equals("VACIO")){
						return true;
					}else if (new Integer (CantidadCBPartner).intValue() <= 0 ){
						return true;
					}else{
						log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotDesactive"));
						return false;					
					}
				}else if(auxActivoViejo.equals("N") && auxActivoNuevo.equals("Y")){
					return true;
				} else
					return false;		
			}else{
				int newValue = getLastValue()+1;
				if(newValue > 999) {
					log.saveError("Error", "Máximo 999 Condiciones de Pago. Contacte al Administrador del Sistema.");
					return false;
				}else {
					setValue(String.valueOf(newValue));
					return true;
				}
			}
		/*}else
			return false;*/
	}//beforeSave
	
	private int getLastValue() {
		
		int result = 0;
		String sql = "SELECT MAX(TO_NUMBER(VALUE)) FROM C_PAYMENTTERM WHERE "+
	     		"AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
		rs = prst.executeQuery();
		
		while (rs.next()){
			result = rs.getInt(1);
		}

		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
			return 0;
		}finally {
			try {
				rs.close();
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		return result;
	}

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
