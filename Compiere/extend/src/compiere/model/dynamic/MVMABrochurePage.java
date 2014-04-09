package compiere.model.dynamic;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;


/**
 * 
 * Esta clase permite realizar las validaciones respectivas
 * al momento de introducir, modificar o eliminar una pagina del folleto.
 * Estas validaciones correponden a triggers de base de datos que
 * se subieron a la capa del negocio.
 * 
 * @author Alejandro Prieto
 * @version 1.0
 */

public class MVMABrochurePage extends X_XX_VMA_BrochurePage{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2365747486752242363L;
	
	/** Constructor Standard 
    @param ctx context
    @param XX_VMA_BrochurePage_ID id
    @param trxName transaction
    */
    public MVMABrochurePage (Ctx ctx, int XX_VMA_BrochurePage_ID, Trx trxName)
    {
        super (ctx, XX_VMA_BrochurePage_ID, trxName);
    }
    
    /** Constructor por carga de datos
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public MVMABrochurePage (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }
    
    /**
     * Operaciones a realizar una vez que se selecciona guardar un registro
     * o pagina del folleto.
     * 
     * @param newRecord permite saber si el registro es nuevo o no.
     * @param success 	notifica si el registro se guardo correctamente.
     * @return boolean 	true si se ejecuto bien el afterSave, false si no.
     */
	protected boolean beforeSave (boolean newRecord)
	{
		//set_Value("XX_VMA_PageDept_V_ID", new Integer(0));
		return true;
	}	
	
}
