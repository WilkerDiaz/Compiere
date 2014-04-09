package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MAttributeSet extends org.compiere.model.MAttributeSet{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Attribute_ID id
	 *	@param trxName transaction
	 */
	public MAttributeSet (Ctx ctx, int M_Attribute_ID, Trx trx)
	{
		super (ctx, M_Attribute_ID, trx);
	}	//	MAttribute

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MAttributeSet (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttribute
	
    /** Set Characteristic Type.
    @param Number identifier of the entity */
    public void setValue (String CharType)  //// CDS ////
    {
    	
    	if (CharType.equals("") ) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", CharType);
        
    }
    
    /** Get Characteristic Type.
    @return Alphanumeric identifier of the entity */
    public String getValue() 
    {
        return get_ValueAsString("Value");
        
    }
    
    public BigDecimal getXX_TipCar() 
    {
        return get_ValueAsBigDecimal("XX_TIPCAR");
        
    }
    
    public BigDecimal getXX_TipCar2() 
    {
        return get_ValueAsBigDecimal("XX_TIPCAR2");
        
    }
    
    public BigDecimal getXX_Tipcar1() 
    {
        return get_ValueAsBigDecimal("XX_TIPCAR1");
        
    }
    
    public void setXX_TipCar(BigDecimal CharType) 
    {
    	set_Value ("XX_TipCar", CharType);
    }
    
    public void setXX_TipCar2(BigDecimal CharType) 
    {
    	set_Value ("XX_TIPCAR2", CharType);
        
    }
    
    public void setXX_Tipcar1(BigDecimal CharType) 
    {
    	set_Value ("XX_TIPCAR1", CharType);
    }
    
    public boolean getXX_Importing() 
    {
        return get_ValueAsBoolean("XX_Importing");
        
    }
    
    public void setXX_Importing(boolean CharType) 
    {
    	if(CharType)
    		set_Value ("XX_Importing", "Y");
    	else
    		set_Value ("XX_Importing", "N");
    }
}
