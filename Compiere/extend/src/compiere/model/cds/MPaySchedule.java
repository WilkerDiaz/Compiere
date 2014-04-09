package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MPaySchedule extends org.compiere.model.MPaySchedule{

	public MPaySchedule(Ctx ctx, int C_PaySchedule_ID, Trx trxName) {
		super(ctx, C_PaySchedule_ID, trxName);
		
	}

	public MPaySchedule(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	
		boolean aux = true;
		
		if(super.beforeSave(newRecord)){
			
			Integer diaFinanciamiento = getXX_DaysFundingTwo();
			BigDecimal porcentajeRestante = getXX_PercentageRemainingTwo();
			BigDecimal porcentajeInicial = getPercentage();
			
			if (porcentajeInicial.intValue() != 100){
				if (porcentajeRestante.equals(new BigDecimal(0))){
					//mensaje de error. El porcentaje restante debe ser distinto de cero
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_ErrorPercentageRemaining"));
					return aux = false;
				}else{ 
					if (diaFinanciamiento == 0){
						//mensaje de error. El dia de financiamiento no puede ser cero
						log.saveError("Error", Msg.getMsg(getCtx(), "XX_ErrorDayFunding"));
						return aux = false;
					}else {
						return aux = true;
					}
				}
			}
		}
		return aux;
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success){
		/* Comentado por Jessica Mendoza
		 * Se retorna true sin llamar al modelo padre, porque dicho método llama al método validate() 
		 * que se encarga de volver a validar la condición de pago (NO se quiere que se vuelva a validar 
		 * por su condición, nativa de Compiere). Dicho proceso fue sustituido 
		 * por la clase XX_ValidatePaymentTerm que se encarga de validar todos los tipos de 
		 * condiciones de pago, a diferencia del validate(), que solo valida las condiciones de
		 * pago simples, es decir, donde le porcentaje inicial es 100% */
		return true;
	}
	
	/** Set Days Funding Second Pay.
    @param Funding Days in which payment is due */
    public void setXX_DaysFundingTwo (int XX_DaysFundingTwo)
    {
        set_Value ("XX_DaysFundingTwo", Integer.valueOf(XX_DaysFundingTwo));
        
    }
    
    /** Get Days Funding Second Pay.
    @return Funding Days in which payment is due */
    public int getXX_DaysFundingTwo() 
    {
        return get_ValueAsInt("XX_DaysFundingTwo");
        
    }
    
    /** Set Days Funding Three Pay.
    @param Funding Days in which payment is due */
    public void setXX_DaysFundingThree (int XX_DaysFundingThree)
    {
        set_Value ("XX_DaysFundingThree", Integer.valueOf(XX_DaysFundingThree));
        
    }
    
    /** Get Days Funding Three Pay.
    @return Funding Days in which payment is due */
    public int getXX_DaysFundingThree() 
    {
        return get_ValueAsInt("XX_DaysFundingThree");
        
    }
   
    /** Set Percentage Remaining Second Pay.
    @param Remainder of the payment is due */
    public void setXX_PercentageRemainingTwo (BigDecimal XX_PercentageRemainingTwo)
    {
        set_Value ("XX_PercentageRemainingTwo", XX_PercentageRemainingTwo);
        
    }
    
    /** Get Percentage Remaining Second Pay.
    @return Remainder of the payment is due */
    public BigDecimal getXX_PercentageRemainingTwo() 
    {
        return get_ValueAsBigDecimal("XX_PercentageRemainingTwo");
        
    }
    
    /** Set Percentage Remaining Thrid Pay.
    @param Remainder of the payment is due */
    public void setXX_PercentageRemainingThree (BigDecimal XX_PercentageRemainingThree)
    {
        set_Value ("XX_PercentageRemainingThree", XX_PercentageRemainingThree);
        
    }
    
    /** Get Percentage Remaining Thrid Pay.
    @return Remainder of the payment is due */
    public BigDecimal getXX_PercentageRemainingThree() 
    {
        return get_ValueAsBigDecimal("XX_PercentageRemainingThree");
        
    }
    
    /** Set Three Option.
    @param XX_IsThreeOption */
    public void setXX_IsThreeOption (boolean XX_IsThreeOption)
    {
        set_ValueNoCheck ("XX_IsThreeOption", Boolean.valueOf(XX_IsThreeOption));
        
    }
    
    /** Get Three Option.
    @return */
    public boolean getXX_IsThreeOption() 
    {
        return get_ValueAsBoolean("XX_IsThreeOption");
        
    }
    
    
}
