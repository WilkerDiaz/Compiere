package compiere.model.cds;

import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MMovement extends org.compiere.model.MMovement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5890434841357936829L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMovement.class);

	public MMovement(Ctx ctx, int XX_VMR_DistributionHeader_ID,
			Trx trx) {
		super(ctx, XX_VMR_DistributionHeader_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MMovement (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	public String getXX_StatusName () {
		
		String estado = "";
		if (getXX_Status().equals("AC")) 
			estado = "APROBADO EN CD";
		else if (getXX_Status().equals("AP")) 
			estado = "APROBADO COMPRADOR";
		else if (getXX_Status().equals("AT")) 
			estado = "APROBADO EN TIENDA";
		else if (getXX_Status().equals("CR")) 
			estado = "CREADO";
		else if (getXX_Status().equals("ED")) 
			estado = "EN ESPERA POR DESPACHO";
		else if (getXX_Status().equals("IT")) 
			estado = "EN TRÁNSITO";
		else if (getXX_Status().equals("PE")) 
			estado = "PENDIENTE";
		else if (getXX_Status().equals("RV")) 
			estado = "REVERSADO";
		else if (getXX_Status().equals("VO")) 
			estado = "ANULADO";
		return estado ;
		
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) {	
			
		if (super.beforeSave(newRecord)) {
			
			if (newRecord) {
				if(getC_DocType_ID() != Env.getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID") && getC_DocType_ID()!=1000335){
					
					//Evitar que se guarde un traspaso a la misma tienda
					MLocator locator = new MLocator(Env.getCtx(), getM_Locator_ID(), null);
					MLocator locator_to = new MLocator(Env.getCtx(), getM_LocatorTo_ID(), null);
					
					if ( locator.getM_Warehouse_ID() == locator_to.getM_Warehouse_ID() )  {
						log.saveError("SaveError", 
								Msg.getMsg(Env.getCtx(), "XX_SameLocators"));
						return false;
//					} else if (getM_Locator_ID() == Env.getCtx().getContextAsInt("XX_L_LOCATORCDDEVOLUCION_ID")) {
					}else if(Utilities.esCDDevolucion(getM_Locator_ID())) {
						//Evitar que algo salga del locator devolucion
						log.saveError("SaveError", 
								Msg.getMsg(Env.getCtx(), "XX_ErrorLocatorTranferFrom"));
						return false;
					} 
					
					//No deben existir traspasos desde centro de distribucion
//					if ( locator.getM_Warehouse_ID() == 
//						Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID") && getC_DocType_ID()!=1000335) {
					if ( Utilities.esCD(locator.getM_Warehouse_ID()) && getC_DocType_ID()!=1000335) {
						log.saveError("SaveError", Msg.getMsg(Env.getCtx(), "XX_MovementFromCD"));
						return false;
					}
										
					if (getC_DocType_ID() == Env.getCtx().getContextAsInt("XX_L_DOCTYPETRANSFER_ID")) {
											
						//Si es traspaso evitar que vaya a centro devolucion
//						if (getM_LocatorTo_ID() == Env.getCtx().getContextAsInt("XX_L_LOCATORCDDEVOLUCION_ID")) {
						if(Utilities.esCDDevolucion(getM_LocatorTo_ID())) {
							log.saveError("SaveError", 
									Msg.getMsg(Env.getCtx(), "XX_ErrorLocatorTransferTo"));
							return false;
						}	
						
						//No deben existir traspasos hasta centro de distribucion
//						if ( locator_to.getM_Warehouse_ID() == Env.getCtx().getContextAsInt("XX_L_WAREHOUSECENTRODIST_ID")) {
						if ( Utilities.esCD(locator_to.getM_Warehouse_ID())) {
							log.saveError("SaveError", Msg.getMsg(Env.getCtx(), "XX_ProductTransferToCD"));
							return false;
						}
					} 
				}
			}  
		} 
		return true;
	}
	
    public void setXX_Synchronized (boolean Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(Synchronized));
        
    }
	
	@Override
	protected boolean beforeDelete() {

		if (!getXX_Status().equals("CR")) {
				log.saveError("Error", Msg.getMsg(Env.getCtx(), "XX_CannotDeleteMove"));
				return false;
		}
		return super.beforeDelete();
	}
	
	/** @author Javier Pino
	 *  Obtiene el Locator de Origen 
    @return Country */
    public int getM_Locator_ID() {
        return get_ValueAsInt("M_Locator_ID");        
    }
    
    /** @author Javier Pino
	 *  Obtiene el Locator de destino de este traspaso
    @return Country */
    public int getM_LocatorTo_ID() {
        return get_ValueAsInt("M_LocatorTo_ID");        
    }
// Wdiaz set del Locator en Movimiento
    public void setM_Locator_ID(int locator) {
		set_ValueNoCheck("M_Locator_ID", locator);	     
    }
    
	public void setM_LocatorTo_ID(int locatorTo) {
		set_ValueNoCheck("M_LocatorTo_ID", locatorTo);	     
    }
    
    /** @author Javier Pino
     * Obtiene la fecha de modificacion del registro
     */
    public Timestamp getXX_VoidDate() {
    	return (Timestamp)get_Value("XX_VoidDate");    	
    }

    /** @author Javier Pino
     * Actualiza la fecha de anulacion del registro
     */
    public void setXX_VoidDate(Timestamp XX_VoidDate)  {
	        set_Value ("XX_VoidDate", XX_VoidDate);	        
	} 
    
    /** @author Javier Pino
     * Actualiza el usuario de anulacion
     */
    public void setXX_UserVoid_ID (int XX_UserVoid_ID) {    		
    	    set_Value("XX_UserVoid_ID", XX_UserVoid_ID);	
    }
    
    public void setXX_VoidMethod (String XX_VoidMethod) {
    	set_Value("XX_VoidMethod", XX_VoidMethod);    	
    } 
    
    public void setXX_RequestDate (Timestamp XX_RequestDate) {
    	set_Value("XX_RequestDate", XX_RequestDate);    	
    }
    
    public void setXX_DispatchDate (Timestamp XX_DispatchDate) {
    	set_Value("XX_DispatchDate", XX_DispatchDate);    	
    }
        
    public void setXX_Status (String XX_Status) {
    	set_Value("XX_Status", XX_Status);
    }
    
    public void setXX_PackageQuantity (int XX_PackageQuantity) {
    	set_Value("XX_PackageQuantity",XX_PackageQuantity);
    }
    
    public int getXX_PackageQuantity () {
    	return get_ValueAsInt("XX_PackageQuantity");
    }
    
    public String getXX_Status () {
    	return get_ValueAsString("XX_Status");
    }
    
    public int getXX_VMR_Category_ID() {
        return get_ValueAsInt("XX_VMR_Category_ID");        
    }
    
    public void setXX_VMR_Category_ID(int category) {
		set_ValueNoCheck("XX_VMR_Category_ID", category);	     
    }
    
    public int getXX_VMR_Department_ID() {
        return get_ValueAsInt("XX_VMR_Department_ID");        
    }
    
    public void setXX_VMR_Department_ID(int department) {
		set_ValueNoCheck("XX_VMR_Department_ID", department);	     
    }

    public int getXX_TransferMotive_ID() {
        return get_ValueAsInt("XX_TransferMotive_ID");        
    }
    
    public void setXX_TransferMotive_ID(int XX_TransferMotive_ID) {
		set_ValueNoCheck("XX_TransferMotive_ID", XX_TransferMotive_ID);	     
    }
    
    public void setXX_MovementFrom_ID (int XX_MovementFrom) {
    	set_Value("XX_MovementFrom_ID", XX_MovementFrom);    	
    } 
    
    public int getXX_MovementFrom_ID() {
        return get_ValueAsInt("XX_MovementFrom_ID");        
    }
    
    public int getXX_UserVoid_ID() {
        return get_ValueAsInt("XX_UserVoid_ID");        
    }
    

    public int getM_WarehouseTo_ID() {
        return get_ValueAsInt("M_WarehouseTo_ID");        
    }
    
    public void setM_WarehouseTo_ID(int M_WarehouseTo_ID) {
		set_ValueNoCheck("M_WarehouseTo_ID", M_WarehouseTo_ID);	     
    }
    
    public int getM_WarehouseFrom_ID() {
        return get_ValueAsInt("M_WarehouseFrom_ID");        
    }
    
    public void setM_WarehouseFrom_ID(int M_WarehouseFrom_ID) {
		set_ValueNoCheck("M_WarehouseFrom_ID", M_WarehouseFrom_ID);	     
    }

    public Timestamp getXX_RequestDate() {
    	return (Timestamp)get_Value("XX_RequestDate");    	
    }
    
    public Timestamp getXX_DispatchDate() {
    	return (Timestamp)get_Value("XX_DispatchDate");    	
    }
    
    public void setC_BPartner_ID (int C_BPartner_ID ) {
    	set_Value ("C_BPartner_ID", Integer.valueOf( C_BPartner_ID ));    	
    }
    
  
    
}
