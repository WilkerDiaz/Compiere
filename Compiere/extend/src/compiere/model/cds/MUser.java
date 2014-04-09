package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.compiere.model.MRole;
import org.compiere.model.X_AD_User;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MUser extends org.compiere.model.MUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);
	
	public MUser(Ctx ctx, int AD_User_ID, Trx trx) {
		super(ctx, AD_User_ID, trx);
	}

	public MUser(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
    /** Set XX_ContactType.
    @param XX_ContactType */
    public void setXX_ContactType (String XX_ContactType)
    {
        set_Value ("XX_ContactType", XX_ContactType);
        
    }
    
    /** Get XX_ContactType.
    @return XX_ContactType */
    public String getXX_ContactType() 
    {
        return (String)get_Value("XX_ContactType");
    }

	public MUser(MBPartner bp) {
		super(bp);
	}

	/*
     * Metodo para validar Email
     */
    public boolean isEmail(String correo) {
        Pattern pat = null;
        Matcher mat = null;        
        //pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        //pat = Pattern.compile("^(([\\-\\_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z]+\\.){1}([0-9a-zA-Z][0-9a-zA-Z]([0-9a-zA-Z])?)(\\.[0-9a-zA-Z][0-9a-zA-Z])?)$");
        pat = Pattern.compile("^(([\\-\\_.w]*[0-9a-zA-Z])*@(([\\-\\_.w]*[0-9a-zA-Z])*\\.){1}([0-9a-zA-Z][0-9a-zA-Z]([0-9a-zA-Z])?)(\\.[0-9a-zA-Z][0-9a-zA-Z])?)$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            return true;
        }else{
            return false; ////////////////// OJO: Fue cambiado a true para que acepte correos con - y _ pero
        }  				/////////////////   en realidad se deberia modificar la expresion regular anterior para
    }					////////////////	que los incluya y colocar esta linea en falso
    
	/**
	 * Chequea que sea numeros un String
	 */
	private boolean isNumeric(String cadena)
	{	try {
			if (cadena == null){
				return true;
			}
			Long.parseLong(cadena);
			return true;	
		} catch (NumberFormatException nfe){
			nfe.printStackTrace();
			return false;	
		}
	}
	
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
		if(super.beforeDelete()){
			X_AD_User User = new X_AD_User(getCtx(),get_ID(), null);		
			Integer ContactosSales = ConsultarTablasBPartner(User.getC_BPartner_ID(), " AD_User", getCtx().getContext("#XX_L_CONTACTTYPESALES"));
			Integer ContactosAdmin = ConsultarTablasBPartner(User.getC_BPartner_ID(), " AD_User", getCtx().getContext("#XX_L_CONTACTTYPEADMINISTRATIVE"));
			
			if(getXX_ContactType()!=null){
				if(getXX_ContactType().equals(X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue())){
					if(ContactosAdmin == 1){
						String mensaje = Msg.getMsg(getCtx(), "XX_NotDeleteInactiveVendor", new String[] {" Administrative Contact"});
						log.saveError("Error", mensaje);
						return false;
					}
				}else if(getXX_ContactType().equals(X_Ref_XX_Ref_ContactType.SALES.getValue())){
					if (ContactosSales == 1){
						String mensaje = Msg.getMsg(getCtx(), "XX_NotDeleteInactiveVendor", new String[] {" Sales Contact"});
						log.saveError("Error", mensaje);
						return false;
					}
				}
			}
			return true;
		}else{
			return false;
		}
	}//beforeDelete
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
    protected boolean beforeSave (boolean newRecord)
	{ 
		if(super.beforeSave(newRecord)){
			if(!isNumeric(getPhone()) || (getPhone2() != null && !getPhone2().equals("") && !isNumeric(getPhone2())) || (getFax() != null && !getFax().equals("") && !isNumeric(getFax())) ){
				log.saveError("Error", Msg.getMsg(getCtx(), "XX_PhoneOnlyNumber"));
				return false;
			}
			
			MBPartner socio = new MBPartner(getCtx(), getC_BPartner_ID(), null);
			if (socio.getIsVendor())
			{
/**				if(!isEmail(getEMail())){ 
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_EmailValid"));
					return false;
				} else
*/					return true;
			}
			else
				return true;

		}else
			return false;
			
	}

}
