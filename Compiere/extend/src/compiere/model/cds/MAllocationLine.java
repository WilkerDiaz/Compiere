package compiere.model.cds;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MAllocationLine extends org.compiere.model.MAllocationLine{
	
	public MAllocationLine(Ctx ctx, int C_AllocationLine_ID, Trx trx) {
		super(ctx, C_AllocationLine_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Set Document Info
	 *	@param C_BPartner_ID partner
	 *	@param XX_Contract_ID contrato
	 *	@param C_Invoice_ID invoice
	 */
	public void setDocInfoCont (int C_BPartner_ID, int XX_Contract_ID, int C_Invoice_ID)
	{
		setC_BPartner_ID(C_BPartner_ID);
		setXX_Contract_ID(XX_Contract_ID);
		setC_Invoice_ID(C_Invoice_ID);
	}
	
	/** 
     * Set XX_Contract_ID
     * @param XX_Contract_ID 
     */
    public void setXX_Contract_ID (int XX_Contract_ID){
    	if (XX_Contract_ID <= 0) set_ValueNoCheck ("XX_Contract_ID", null);
        else
        set_ValueNoCheck ("XX_Contract_ID", Integer.valueOf(XX_Contract_ID));      
    }
    
    /** 
     * Get XX_Contract_ID
     * @return XX_Contract_ID
     */
    public int getXX_Contract_ID(){
        return get_ValueAsInt("XX_Contract_ID");      
    }

}
