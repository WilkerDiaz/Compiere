package compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MREGALOPPORCOMPRA extends X_XX_REGALOPPORCOMPRA {
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 7662944873977207759L;
    public MREGALOPPORCOMPRA (Ctx ctx, int XX_REGALOPPORCOMPRA_ID, Trx trx)
    {
        super (ctx, XX_REGALOPPORCOMPRA_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_REGALOPPORCOMPRA_ID == 0)
        {
            setTipoPromocion (null);
            setXX_PROMOCIONES_ID (0);
            setXX_REGALOPPORCOMPRA_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public MREGALOPPORCOMPRA (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    protected boolean beforeSave(boolean newRecord){
		String sql = "Select cantprodcomprar, xx_montominimo, tienda, porcdescuento, regalo, artexistentes " +
		"from XX_RegalopPorCompra where XX_Regalopporcompra_id<>"+getXX_REGALOPPORCOMPRA_ID()+" and XX_Promociones_ID = "+getXX_PROMOCIONES_ID();
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				String tienda = rs.getString("tienda");
				BigDecimal monto = rs.getBigDecimal("xx_montominimo");
				int cantidad = rs.getInt("cantprodcomprar");
				BigDecimal porcdescuento = rs.getBigDecimal("porcdescuento");
				String regalo = rs.getString("regalo");
				int artexistentes= rs.getInt("artexistentes");
				if ((get_ValueAsString("tienda").equals(tienda) 
						|| get_ValueAsString("tienda").equals("00") 
						|| tienda.equals("00")) ){
					if(getXX_MontoMinimo().compareTo(monto)==0
							&& getCantProdComprar()==cantidad 
							&& getPorcDescuento().compareTo(porcdescuento)==0
							&& getArtExistentes()==artexistentes){
						return true;
					}else if(ADialog.ask(EnvConstants.WINDOW_INFO, null, "¡Debe tener los mismos valores para toda la promoción!")){
						String sql2="";
						if (getTipoPromocion().equals("1000100"))
							sql2 ="Update xx_regalopporcompra set xx_montominimo="+getXX_MontoMinimo()
							+",  porcdescuento="+getPorcDescuento()+" where xx_promociones_id="+getXX_PROMOCIONES_ID();
						else if(getTipoPromocion().equals("1000300"))
							sql2 ="Update xx_regalopporcompra set cantprodcomprar="+getCantProdComprar()+",  porcdescuento="
							+get_ValueAsBigDecimal("PorcDescuento")+" where xx_promociones_id="+getXX_PROMOCIONES_ID();
						else if(getTipoPromocion().equals("1001200"))
							sql2 ="Update xx_regalopporcompra set regalo='"+getRegalo()+"', artexistentes="+getArtExistentes()
							+" where xx_promociones_id="+getXX_PROMOCIONES_ID();
						DB.executeUpdate(null, sql2 );
							return true;
						}else return false;
				}//else return true;
			}
			rs.close();
			pstmt.close();
			return true;
		}catch (SQLException e){e.printStackTrace();}
		return true;
    }
}

