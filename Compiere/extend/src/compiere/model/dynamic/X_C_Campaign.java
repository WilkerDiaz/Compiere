package compiere.model.dynamic;

import java.sql.ResultSet;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class X_C_Campaign extends org.compiere.model.X_C_Campaign{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3144635342940865162L;
	
	public X_C_Campaign(Ctx ctx, int C_Campaign_ID, Trx trxName) {
		super(ctx, C_Campaign_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public X_C_Campaign(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setXX_VMA_IsCampaignApproved (boolean IsApproved)
    {
        set_Value ("XX_VMA_IsCampaignApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isXX_VMA_IsCampaignApproved() 
    {
        return get_ValueAsBoolean("XX_VMA_IsCampaignApproved");
        
    }
    
    /** Set Season.
    @param XX_VMA_Season_ID Season */
    public void setXX_VMA_Season_ID (int XX_VMA_Season_ID)
    {
        if (XX_VMA_Season_ID < 1) throw new IllegalArgumentException ("XX_VMA_Season_ID is mandatory.");
        set_Value ("XX_VMA_Season_ID", Integer.valueOf(XX_VMA_Season_ID));
        
    }
    
    /** Get Season.
    @return Season */
    public int getXX_VMA_Season_ID() 
    {
        return get_ValueAsInt("XX_VMA_Season_ID");
        
    }

    /** Set DocStatus.
    @param DocStatus */
    public void setDocStatus (String DocStatus)
    {
    	set_Value ("DocStatus", DocStatus);
        
    }
  
    /** Get DocStatus.
    @return DocStatus */
    public String getDocStatus ()
    {
    	return (String)get_Value("DocStatus");
        
    }
    
    /** Set XX_VMA_IsCampaignActive.
    @param XX_VMA_IsCampaignActive */
    public void setXX_VMA_IsCampaignActive (boolean XX_VMA_IsCampaignActive)
    {
    	set_Value ("XX_VMA_IsCampaignActive", Boolean.valueOf(XX_VMA_IsCampaignActive));
        
    }
    
    /** Get XX_VMA_IsCampaignActive.
    @return XX_VMA_IsCampaignActive */
    public boolean isXX_VMA_IsCampaignActive() 
    {
    	return get_ValueAsBoolean("XX_VMA_IsCampaignActive");
        
    }
}
