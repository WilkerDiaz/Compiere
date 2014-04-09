package compiere.model.cds;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MDocType;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.processes.XX_BatchNumberInfo;

public class MMovementLine extends org.compiere.model.MMovementLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5890434841357936829L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMovementLine.class);

	public MMovementLine(Ctx ctx, int MMovementLine_ID,
			Trx trx) {
		super(ctx, MMovementLine_ID, trx);	
	}
	
	public MMovementLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
    public void setXX_Synchronized (boolean Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(Synchronized));
        
    }
    
    @Override
    protected boolean beforeDelete() {
    	if (super.beforeDelete()) {
    		MMovement movement = new MMovement(Env.getCtx(), getM_Movement_ID(), null);
    		if (!movement.getXX_Status().equals("CR")) {
    			log.saveError("Error", Msg.getMsg(getCtx(), "CannotDeleteTrx"));
    			return false;
    		}   else return true; 			
    	}   else return false; 	
    }

	@Override
	protected boolean beforeSave(boolean newRecord) {	
	
		
		MMovement movement = new MMovement(Env.getCtx(), getM_Movement_ID(), get_Trx());
		if (super.beforeSave(newRecord)) {
			
			if (newRecord && movement.getC_DocType_ID() != getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID")) {
							
				setM_Locator_ID(movement.getM_Locator_ID());
				setM_LocatorTo_ID(movement.getM_LocatorTo_ID());
				
				if (!movement.getXX_Status().equals("CR")) {
					log.saveError("Error", 
							Msg.translate(Env.getCtx(), "XX_MLinesNotCreated"));
					return false;
				}
				
				//Verificar que no se haya perdido informacion del consecutivo de precios				
				Integer product, priceconsecutive;
				BigDecimal saleprice = Env.ZERO, priceactual = Env.ZERO;				
				
				//Verificar que estén todos los argumentos
				product = getM_Product_ID();
				priceconsecutive = getXX_PriceConsecutive();
				
				MDocType tipoDocumento = new MDocType(Env.getCtx(), movement.getC_DocType_ID(), get_Trx());
				
				if (priceconsecutive==null && movement.getC_DocType_ID()!=1000335)
				{
					log.saveError("Error", 
							Msg.translate(Env.getCtx(), "Debe colocar un consecutivo de precio para el producto"));
					return false;
				}
					
				
				if (product == 0 || (priceconsecutive == 0 && movement.getC_DocType_ID()!=1000335)) {
					log.saveError("Error", 
							Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));
					return false;
				}
				
				// Verificar si se va a repetir el producto que cambie el consecutivo
				String consecutivo = " SELECT L.XX_PRICECONSECUTIVE FROM M_MOVEMENTLINE L " +
						"WHERE L.M_PRODUCT_ID  = " + product + " AND L.M_MOVEMENT_ID  = " + movement.get_ID();	
				boolean se_repite_consecutivo = false;
				try {					
					PreparedStatement ps_consecutivo = DB.prepareStatement(consecutivo, get_Trx());
					ResultSet rs_consecutivo = ps_consecutivo.executeQuery();					
					while (rs_consecutivo.next()) {
						
						if (rs_consecutivo.getInt(1) == priceconsecutive) {
							se_repite_consecutivo = true;
							break;
						}
					} 
					rs_consecutivo.close();
					ps_consecutivo.close();
				} catch (SQLException e) {					
					log.saveError("Error", Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"));
					return false;
				}				
				if (se_repite_consecutivo && movement.getC_DocType_ID()!=1000335) {
					log.saveError("Error", Msg.translate(Env.getCtx(), "XX_SamePriceConsecutive"));
					return false;
				}
				
				//Usando el priceconsecutive y el precio deberia calcularse el costo
				String sql = " SELECT XX_SALEPRICE, XX_UNITPURCHASEPRICE " +				
					" FROM XX_VMR_PRICECONSECUTIVE " + 
					" WHERE M_PRODUCT_ID = " + product + 
					" AND XX_PRICECONSECUTIVE = " + priceconsecutive +
					" AND ROWNUM = 1";
				try {
					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						
						saleprice = rs.getBigDecimal("XX_SALEPRICE");
						priceactual = rs.getBigDecimal("XX_UNITPURCHASEPRICE");
						setXX_SalePrice(saleprice);
						setPriceActual(priceactual);

					} 
					rs.close();
					pstmt.close();
				}catch (Exception e) {					
					log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));
					return false;				
				}
				
				MProduct pr = new MProduct( Env.getCtx(), product, null);
				if (pr.getC_TaxCategory_ID() > 0) {
					String sql_rate = " SELECT (RATE/100) FROM C_TAX " +
					" WHERE C_TaxCategory_ID= ? " + 
					" AND  ValidFrom <= sysdate " +
					" and rownum = 1 " +
					" order by ValidFrom desc ";	
					PreparedStatement prst_tax = null;
					ResultSet rs_tax = null;
					try { 
						
						prst_tax = DB.prepareStatement(sql_rate,null);
						prst_tax.setInt(1, pr.getC_TaxCategory_ID());
						rs_tax = prst_tax.executeQuery();
						if (rs_tax.next()){
														
							BigDecimal tax = rs_tax.getBigDecimal(1);
							tax = tax.multiply(priceactual);
							tax = tax.setScale(2, RoundingMode.HALF_DOWN);
							setTaxAmt(tax);
							setC_TaxCategory_ID(pr.getC_TaxCategory_ID());	
							
						} else {
							log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound"));
							return false;
						}

					} catch (SQLException e){
						log.saveError("Error", Msg.translate(Env.getCtx(), "Error al buscar la tasa de impuesto"));								
					} finally {
						try {
							rs_tax.close();
							prst_tax.close();
						} catch (SQLException e) {}
					}
				}

				//Las devoluciones no pueden ser de varios proveedores				
				if (movement.getC_DocType_ID() == 
					getCtx().getContextAsInt("XX_L_DOCTYPERETURN_ID")) { 
					
					//Verificar que es el mismo proveedor
					String sql2 = " SELECT P.C_BPARTNER_ID FROM M_MOVEMENTLINE L JOIN M_PRODUCT P " +
							" ON (P.M_PRODUCT_ID = L.M_PRODUCT_ID ) WHERE L.M_MOVEMENT_ID  = " + movement.get_ID();				
					try {
						int bpartner = 0;
						PreparedStatement pstmt = DB.prepareStatement(sql2, null);
						ResultSet rs = pstmt.executeQuery();					
						if (rs.next()) {
							bpartner = rs.getInt(1);						
						} 
						rs.close();
						pstmt.close();
						
						MProduct mproduct = new MProduct(Env.getCtx(), getM_Product_ID(), null);						
						if (bpartner != 0 ) {							
							if (mproduct.getC_BPartner_ID() != bpartner) {
								log.saveError("Error", Msg.translate(Env.getCtx(), "XX_DifferentBPartner"));
								return false;
							}
						}
						
						//Los proveedores no pueden ser internacionales en una devolucion
						MBPartner proveedor = new MBPartner(Env.getCtx(), mproduct.getC_BPartner_ID(), null);						
						if (proveedor.getXX_VendorClass().equals("" + getCtx().getContextAsInt("XX_L_VENDCLASSIMP"))) {
							log.saveError("Error", Msg.translate(Env.getCtx(), "XX_InternationalReturn"));
							return false;
						}
						
						//Actualizar el business partner en la cebecera
						//Se cambio a un Update Directo ya que 362 da error cuando se actualiza la cabecera con el modelo desde el HIJO
						try{
							String sqlUpdateProove = "update M_Movement set C_BPartner_ID = " + mproduct.getC_BPartner_ID() + " where M_Movement_ID = " + movement.getM_Movement_ID();

							DB.executeUpdate(get_Trx(),sqlUpdateProove);

						}catch (Exception e) {
							e.printStackTrace();
						}
						
						/*movement.setC_BPartner_ID(mproduct.getC_BPartner_ID());
						movement.save();*/
						
						
					}catch (SQLException e) {
						e.printStackTrace();
						log.saveError("Error", Msg.translate(Env.getCtx(), "XX_DatabaseAccessError"));
						return false;
					}
				}
			}			
		}
		if (movement.getC_DocType_ID() != getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID")
				&& (movement.getXX_Status().compareTo(X_Ref_XX_TransferStatus.APROBADOENTIENDA.getValue())!=0)) {
			
			//Verificar que las cantidades requeridas sean menores que las disponibles
			XX_BatchNumberInfo lote_info = new XX_BatchNumberInfo();
			XX_BatchNumberInfo.Informacion info = lote_info.crearInfo();
			
			info.setConsecutivo(getXX_PriceConsecutive());
			info.setLocator(getM_Locator_ID());
			info.setProducto(getM_Product_ID());
			info.setAlmacen(movement.getM_WarehouseFrom_ID());
			
			//Buscar la cantidad disponible
			String respuesta = info.cantidadProductoConsecutivo();
			if (info.isCorrecto()) {			
				/*if (getQtyRequired().compareTo(info.getCantidadDisponible()) == 1) {
					String mss = Msg.getMsg(Env.getCtx(), "XX_ReqLessThanAvail", 
							new String[] {
								"" + getQtyRequired().setScale(0), 
								"" + info.getCantidadDisponible(),							
								"" + getXX_PriceConsecutive()});			
					log.saveError("Error", mss);
					return false;
				}*/
			} else {
				log.saveError("Error", respuesta);
				return false;
			}

			BigDecimal approvedQty = getXX_ApprovedQty();	
			BigDecimal movementQty = getQtyRequired();
			if (movementQty.compareTo(Env.ZERO) <= 0) {
				log.saveError("Error", Msg.translate(Env.getCtx(), "QtyEnteredZero"));
				return false;				
			}
			if (approvedQty.compareTo(movementQty) == 1) {
				log.saveError("Error", Msg.translate(Env.getCtx(), "XX_AppLessThanMov"));
				return false;			
			}
			
			//Al final como es un mensaje se puede dejar acá -- 
			//Indicar si el producto existe en otro movimiento
			
		}		

		
		if (getXX_ApprovedQty().compareTo(getXX_QuantityReceived()) < 0 && (movement.getXX_Status().compareTo(X_Ref_XX_TransferStatus.APROBADOENTIENDA.getValue())==0))
		{
			log.saveError("Error", Msg.translate(Env.getCtx(), "XX_MoreReceived"));
			return false;
		}
		
		return true;
	}
	
	/** @author Javier Pino
	 * Obtiene el producto que se desea utilizar en el traspaso 
     */
    public int getM_Product_ID() {
        return get_ValueAsInt("M_Product_ID");        
    }
    
    /** @author Javier Pino
	 *  Obtiene el consecutivo de precios	 *  
     */
    public int getXX_PriceConsecutive() {
        return get_ValueAsInt("XX_PriceConsecutive");        
    }
    
    public void setXX_PriceConsecutive(int consecutivoPrecio) {
		set_ValueNoCheck("XX_PriceConsecutive", consecutivoPrecio);	     
    }
    
    public int getXX_VMR_Brand_ID() {
        return get_ValueAsInt("XX_VMR_Brand_ID");        
    }
    
    public void setXX_VMR_Brand_ID(int brand) {
		set_ValueNoCheck("XX_VMR_Brand_ID", brand);	     
    }
    
    public int getXX_VMR_Line_ID() {
        return get_ValueAsInt("XX_VMR_Line_ID");        
    }
    
    public void setXX_VMR_Line_ID(int line) {
		set_ValueNoCheck("XX_VMR_Line_ID", line);	     
    }
    
    public int getXX_VMR_Section_ID() {
        return get_ValueAsInt("XX_VMR_Section_ID");        
    }
    
    public void setXX_VMR_Section_ID(int section) {
		set_ValueNoCheck("XX_VMR_Section_ID", section);	     
    }

    public void setXX_SalePrice( BigDecimal XX_SalePrice) {
    	set_Value ("XX_SalePrice", XX_SalePrice );    	
    }
    
    public void setPriceActual( BigDecimal PriceActual) {
    	set_Value ("PriceActual", PriceActual );    	
    }
    
    public void setTaxAmt (BigDecimal TaxAmt) {
    	set_Value ("TaxAmt", TaxAmt );
    }
    
    public void setC_TaxCategory_ID (int C_TaxCategory_ID) {
    	set_Value ("C_TaxCategory_ID", C_TaxCategory_ID );
    }
    
    
    public BigDecimal getXX_ApprovedQty() {
    	return get_ValueAsBigDecimal("XX_ApprovedQty");
    }
    
    public BigDecimal getQtyRequired() {
    	return get_ValueAsBigDecimal("QtyRequired");
    }
    
    public void setQtyRequired( BigDecimal QtyRequired ) {
    	set_Value("QtyRequired", QtyRequired);
    }

    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID ) {
    	set_Value ("M_AttributeSetInstance_ID", Integer.valueOf( M_AttributeSetInstance_ID ));    	
    }
    
    public void setM_AttributeSetInstanceTo_ID (int M_AttributeSetInstanceTo_ID ) {
    	set_Value ("M_AttributeSetInstance_ID", Integer.valueOf( M_AttributeSetInstanceTo_ID ));    	
    }
    
    public void setXX_ReturnMotive_ID (int XX_ReturnMotive_ID ) {
    	set_Value ("XX_ReturnMotive_ID", Integer.valueOf( XX_ReturnMotive_ID ));    	
    }
    
    public int  getXX_ReturnMotive_ID () {
    	return get_ValueAsInt("XX_ReturnMotive_ID");    	
    }
    
    public BigDecimal getXX_QuantityReceived() {
    	return get_ValueAsBigDecimal("XX_QuantityReceived");
    }
    
}
