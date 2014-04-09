/**
 * 
 */
package compiere.model.importcost;

import java.math.BigDecimal;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

/**
 * @author aavila
 *
 */
public class MImportCostDetail extends X_XX_VLO_ImportCostDetail {
	
	private int x_xx_vlo_boardingGuide = 0;
	private int c_bpartnet_id = 0;

	/**
	 * 
	 */
	public MImportCostDetail(Ctx ctx, int XX_VLO_ImportCostDetail_ID, Trx trx) {
		// TODO Auto-generated constructor stub
		super (ctx, XX_VLO_ImportCostDetail_ID, trx);
	}
	public MImportCostDetail (Ctx ctx, int detail, Trx trx, int guia, int socio )
    {
        super (ctx, detail, trx);
        setX_xx_vlo_boardingGuide(guia);
        setX_xx_C_bpartnet_id(socio);
        setXX_OTHER(new BigDecimal(0));

    }
	
    public int getX_xx_vlo_boardingGuide() 
    {
        return get_ValueAsInt("XX_VLO_BoardingGuide_ID");
        
    }
    public int getC_bpartnet_id() 
    {
        return get_ValueAsInt("C_BPartnet_id");
        
    }
    public void setX_xx_vlo_boardingGuide(int x_xx_vlo_boardingGuide)
    {
        set_ValueNoCheck("XX_VLO_BoardingGuide_ID", x_xx_vlo_boardingGuide);
        
    }
    public void setX_xx_C_bpartnet_id(int c_bpartnet_id)
    {
        set_Value("C_BPartner_ID", c_bpartnet_id);
        
    }
}
