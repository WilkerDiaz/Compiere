package compiere.model.dynamic;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


public class MVMEElements extends X_XX_VME_Elements{
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(X_XX_VME_Elements.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5867347281567892103L;
	
    /** Constructor Standard
    @param ctx context
    @param XX_VME_Elements_ID id
    @param trxName transaction
    */
    public MVMEElements (Ctx ctx, int XX_VME_Elements_ID, Trx trxName)  {
        super (ctx, XX_VME_Elements_ID, trxName);
    }
    
    /** Constructor por carga de datos
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public MVMEElements (Ctx ctx, ResultSet rs, Trx trxName)  {
        super (ctx, rs, trxName);
    }
    
    /**
     * Se comprueba que el elemento tenga una cantidad a publicar.
     * También se verifica que al menos una de las tres siguientes es distinta de cero:
     * Precio dinámica, precio promocional dinámica y porcentaje de descuento
     * @param newRecord	permite saber si el registro es nuevo o no.
     * @return boolean 	true si se realizó el beforeSave correctamente, false si no.
     */
    
    protected boolean beforeSave (boolean newRecord){
    	int counter = 0;
    	
    	// Verificion de cantidad a publicar
    	if(get_ValueAsBigDecimal("XX_VME_QTYPUBLISHED").compareTo(new BigDecimal("0")) == 0){
    		log.saveError("Error", Msg.getMsg(getCtx(), "La cantidad a publicar no puede ser 0"));
    		return false;
    	} // QtyEntered
    	
    	// Verificación de precios y descuento
    	if(get_ValueAsBigDecimal("XX_VME_PromoDynPrice").compareTo(new BigDecimal("0")) != 0){
    		counter++;
    	}
    	if(get_ValueAsBigDecimal("XX_VME_DynamicPrice").compareTo(new BigDecimal("0")) != 0){
    		counter++;
    	}
    	if(get_ValueAsBigDecimal("XX_VME_DiscountPercentage").compareTo(new BigDecimal("0")) != 0){
    		counter++;
    	}
    	
    	if(counter < 1){
    		log.saveError("Error", Msg.translate( Env.getCtx(), "XX_ElementsSave" ));
    		return false;
    	}
    	
    	if(getXX_VME_Type() != null && !getXX_VME_Type().equalsIgnoreCase("")){
    		X_XX_VMA_BrochurePage bp = new X_XX_VMA_BrochurePage(Env.getCtx(), getXX_VMA_BrochurePage_ID(), null);
    		
			if(bp.getXX_VMA_PageType().equals("P") & getXX_VME_Type().equals("I")){
				setXX_VME_Type(null);
				log.saveError("Error", Msg.translate( Env.getCtx(), "XX_ProductPage" ));
				return false;
			}
		} else {
			log.saveError("Error", Msg.getMsg(getCtx(), "Debe indicar el tipo del elemento"));
    		return false;
		}
    	// Verificion de cantidad a publicar
    	if(get_ValueAsBigDecimal("XX_VME_QTYPUBLISHED").compareTo(new BigDecimal("0")) == 0){
    		log.saveError("Error", Msg.getMsg(getCtx(), "La cantidad a publicar no puede ser 0"));
    		return false;
    	} // QtyEntered
    	
		
    	return true;
    }// before save
    
    /**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success){
		if (!success)
			return success;
		
		//Se cambia la cantidad a publicar, se debe redistribuir
		if (!newRecord && (is_ValueChanged("XX_VME_QTYPUBLISHED"))){
			if(getXX_VME_QtyRefAssociated().compareTo(new BigDecimal(0)) > 0){
				MVMEElements elemento = new MVMEElements (getCtx(), getXX_VME_Elements_ID(), get_Trx());
				XX_VME_GeneralFunctions.redefineQuantities(elemento);
			}
		}
    	return true;
		
	} // afterSave
	   
    
} // MVMEElements
