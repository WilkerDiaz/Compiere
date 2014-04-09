package compiere.model.cds;

import java.math.BigDecimal;

import org.compiere.model.X_I_Order;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class IOrder extends X_I_Order {
	
	public IOrder(Ctx ctx, int I_Order_ID, Trx trx) {
		super(ctx, I_Order_ID, trx);
		// TODO Auto-generated constructor stub
	}
	private static final long serialVersionUID = 1L;
	
	public String getXX_TaxSerial()
    {
    	return get_ValueAsString("XX_TaxSerial");
    }
    
	public void setXX_TaxSerial(String XX_TaxSerial)
    {
    	set_Value ("XX_TaxSerial", XX_TaxSerial);
    }
	
	public int getXX_TaxReceipt()
    {
    	return get_ValueAsInt("XX_TaxReceipt");
    }
    
	public void setXX_TaxReceipt(int XX_TaxReceipt)
    {
    	set_Value ("XX_TaxReceipt", XX_TaxReceipt);
    }
	
	public int getXX_OrigTaxReceipt()
    {
    	return get_ValueAsInt("XX_OrigTaxReceipt");
    }
    
	public void setXX_OrigTaxReceipt(int XX_OrigTaxReceipt)
    {
    	set_Value ("XX_OrigTaxReceipt", XX_OrigTaxReceipt);
    }
	
	public void setXX_PriceConsecutive(int XX_PriceConsecutive)
    {
    	set_Value ("XX_PriceConsecutive", XX_PriceConsecutive);
    }
	
	public int getXX_MachineNo()
    {
    	return get_ValueAsInt("XX_MachineNo");
    }
    
	public void setXX_TransactionNo(int XX_TransactionNo)
    {
    	set_Value ("XX_TransactionNo", XX_TransactionNo);
    }
	
	public int getXX_TransactionNo()
    {
    	return get_ValueAsInt("XX_TransactionNo");
    }
    
	public void setXX_MachineNo(int XX_MachineNo)
    {
    	set_Value ("XX_MachineNo", XX_MachineNo);
    }
	
	public BigDecimal getXX_EmployeeDiscount()
    {
    	return get_ValueAsBigDecimal("XX_EmployeeDiscount");
    }
    
	public void setXX_EmployeeDiscount(BigDecimal XX_EmployeeDiscount)
    {
    	set_Value ("XX_EmployeeDiscount", XX_EmployeeDiscount);
    }
	
	public BigDecimal getXX_BasePrice()
    {
    	return get_ValueAsBigDecimal("XX_BasePrice");
    }
    
	public void setXX_BasePrice(BigDecimal XX_BasePrice)
    {
    	set_Value ("XX_BasePrice", XX_BasePrice);
    }
	
    /**
     * @return
     */
    public String getXX_StartTime()
    {
    	return get_ValueAsString("XX_StartTime");
    }
    
    /** Set Start Time
    @param */
    public void setXX_StartTime(String time)
    {
        set_Value ("XX_StartTime", time);
    }
    
    /**
     * @return
     */
    public String getXX_EndTime()
    {
    	return get_ValueAsString("XX_EndTime");
    }
    
    /** Set Start Time
    @param */
    public void setXX_EndTime(String time)
    {
        set_Value ("XX_EndTime", time);
    }
    
	public String getXX_SaleType()
    {
    	return get_ValueAsString("XX_SaleType");
    }
    
	public void setXX_SaleType(String value)
    {
    	set_Value ("XX_SaleType", value);
    }
	
    /** Set M_AttributeSetInstance_ID.
    @param M_AttributeSetInstance_ID */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get M_AttributeSetInstance_ID.
    @return M_AttributeSetInstance_ID */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
}
