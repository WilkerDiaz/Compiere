package compiere.model.suppliesservices;

import java.sql.ResultSet;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

import java.math.BigDecimal;

/**
 *  Modelo extendido de AD_Org
 *  @author María Vintimilla
 *  @version    
 */
public class MOrg extends org.compiere.model.MOrg {
	/** */
    private static final long serialVersionUID = 1L;
    
    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_Contract_ID id
	 *	@param trxName transaction
	 */
    public MOrg(Ctx ctx, int AD_Org_ID, Trx trxName) {
		super(ctx, AD_Org_ID, trxName);
		// TODO Auto-generated constructor stub
	}
    
    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MOrg(Ctx ctx, ResultSet rs, Trx trxName)	{
		super(ctx, rs, trxName);
	}//MContract
	
	/** Purchase of Supplies and Services
	 * Maria Vintimilla Función 22
	 * Distribution By Square Meter**/
	
	/** Set SquareMeter Per Cost Center.
    @param XX_SquareMeter Square Meter Per Cost Center */
    public void setXX_SquareMeter(BigDecimal XX_SquareMeter) {
        set_Value ("XX_SquareMeter", XX_SquareMeter);
        
    }
    
    /** Get Square Meter Per Cost Center.
    @return Square Meter Per Cost Center */
    public java.math.BigDecimal getXX_SquareMeter() {
        return get_ValueAsBigDecimal("XX_SquareMeter");
        
    }
    
}// Fin MOrg
