package compiere.model.cds;
 
import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRDistributionHeader extends X_XX_VMR_DistributionHeader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5890434841357936829L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	public MVMRDistributionHeader(Ctx ctx, int XX_VMR_DistributionHeader_ID,
			Trx trx) {
		super(ctx, XX_VMR_DistributionHeader_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MVMRDistributionHeader (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	@Override
	protected boolean beforeDelete() {		
		if (super.getXX_DistributionStatus().equals(
				X_Ref_XX_DistributionStatus.APROBADA.getValue())) {
			log.saveError("Error", Msg.getMsg(getCtx(),"XX_UndeletableAppDistribution"));
			return false;
		} else if (super.getXX_DistributionStatus().equals(
				X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue())) {
			log.saveError("Error", Msg.getMsg(getCtx(), "XX_UndeletablePDFDistribution"));
			return false;			
		} else {
			if (super.getC_Order_ID() != 0) {
				MOrder order = new MOrder(getCtx(), getC_Order_ID(), null);
				
				//Solo si la orden ha sido anulada
				if (order.getXX_OrderStatus().equals("AN")) {
					return true;
				} else {
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_UndeletablePODistribution"));
					return false;
				}
			}
			else {
				//Redistributions should release the products the reserved
				return true;
			}						
		} 
	}
	
	
	private boolean tieneChequeo(Integer PO){
		String sql = "SELECT * FROM M_INOUT " +
				     "WHERE isSOTRX='N' AND XX_INOUTSTATUS = '"+X_Ref_XX_OrderStatus.CHEQUEADA.getValue()+"' " +
				     "AND C_ORDER_ID = "+PO+"";
		boolean tiene = false;
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				tiene = true;

			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e){
			// TODO: handle exception
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return false;
		}	
		return tiene;
	}
	
	/*
	 * Jorge E. Pires G.
	 * 
	 **/
	private boolean EnviarCorreoDistribucionLista(Integer BPartner, Integer PO, String Attachment, int indicador) {
		MOrder po = new MOrder(getCtx(), PO, get_Trx());
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_POWasDistrib", 
				 new String[]{ po.getDocumentNo()
							 });
		if (indicador == 0){
			String sql = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + BPartner ;
			
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_User_ID");
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_POWASDISTRIB_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
		}else if(indicador == 1){
			String sql = "SELECT AD_USER_ID FROM AD_User_Roles WHERE AD_ROLE_ID="+getCtx().getContextAsInt("#XX_L_ROLECHECKUPCOORDINATOR_ID")+"";
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					Integer UserAuxID = rs.getInt("AD_USER_ID");
					Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_POWASDISTRIB_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
					f.ejecutarMail();
					f = null;
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
		}
		return true;
	}
	
	private int buscarAuxiliarChequeo(Integer PO){
		Integer UserAuxID = -1;
		
		String sql = "SELECT XX_CHECKASSISTANT_ID "
				   + "FROM M_INOUT "
				   + "WHERE isSOTRX='N' AND C_ORDER_ID = " + PO ;
	
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				UserAuxID = rs.getInt("XX_CHECKASSISTANT_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return -1;
		}
		return UserAuxID;
	}
	
	
	/*
	 * 	Consulta el Coordinador Chequeo
	 */
	public Integer ConsultarCoordinadorChequeo(){
		Integer aux = null;
		
		String sql = "select C_BPartner_ID " +
					 "from C_BPartner " +
				     "where C_JOB_ID = "+getCtx().getContext("#XX_L_JOBPOSITION_COORCHEQ_ID")+" and " +
				     "IsActive = 'Y' ";
		
		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getInt("C_BPartner_ID");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return -1;
		}
	}//ConsultarJefePlanificacion
	
	/*
	 * Fin Jorge E. Pires G.
	 * */

	protected boolean beforeSave(boolean newRecord) {
		/*
		 * Jorge Pires - envia correo a Asistente de Chequeo y Coordinador de Chequeo
		 */
		if(getXX_DistributionStatus().equals(MVMRDistributionHeader.XX_DISTRIBUTIONSTATUS_Aprobada)){
			if (!((String)get_ValueOld("XX_DistributionStatus")).equals(MVMRDistributionHeader.XX_DISTRIBUTIONSTATUS_Aprobada)) {
				if(tieneChequeo(getC_Order_ID())){
					//envio correo
					MOrder orden = new  MOrder(Env.getCtx(), getC_Order_ID(), null);
					if(orden.getXX_VLO_TypeDelivery().compareTo("DD")!=0){ // NO DE DESPACHO DIRECTO
						String Attachment = null;							
						Integer auxChequeo = buscarAuxiliarChequeo(getC_Order_ID());
						EnviarCorreoDistribucionLista(auxChequeo, getC_Order_ID(), Attachment,0);
						
						Integer coorChequeo = ConsultarCoordinadorChequeo();
						EnviarCorreoDistribucionLista(coorChequeo, getC_Order_ID(), Attachment,1);	
					}
				}
			}
		}
		//Se iguala el Nro de Documento con el ID de la distribución
		if(get_ID() != 0 && !getDocumentNo().equals(String.valueOf(get_ID())))
			set_ValueNoCheck("DocumentNo",String.valueOf(get_ID()));
		
		return true;
	}

	protected boolean afterSave (boolean newRecord, boolean success){	

		boolean save = super.afterSave(newRecord, success);
		if(save){			
			if(!newRecord){
			}
			if(isXX_IsDefinitive()){
				if(get_ValueOld("XX_IsDefinitive").equals(false)){
					MOrder order = null;
					X_XX_VMR_DistributionHeader dist = new X_XX_VMR_DistributionHeader(getCtx(), get_ID(), get_Trx());
					if(getC_Order_ID()>0)
						order = new  MOrder(Env.getCtx(), getC_Order_ID(), null);
				
					if(order==null){ 
						String sql = "\nSELECT DPD.XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
						 "\nFROM XX_VMR_DISTRIBPRODUCTDETAIL DPD JOIN  XX_VMR_DISTRIBUTIONDETAIL DD ON (DPD.XX_VMR_DISTRIBUTIONDETAIL_ID = DD.XX_VMR_DISTRIBUTIONDETAIL_ID) " +
						 "\nWHERE DD.XX_VMR_DISTRIBUTIONHEADER_ID = "+dist.getXX_VMR_DistributionHeader_ID();
						PreparedStatement pstmt =null;
						ResultSet rs = null;
						try{
							pstmt = DB.prepareStatement(sql, null);
							rs = pstmt.executeQuery();
								
							
							Integer dpdID = null;
							int cantidad = 0;
							
							while(rs.next()){
								dpdID = rs.getInt("XX_VMR_DISTRIBPRODUCTDETAIL_ID");
								X_XX_VMR_DistribProductDetail dpd = new X_XX_VMR_DistribProductDetail(getCtx(), dpdID, get_Trx());
								if(dpd.getXX_SalePricePlusTax() != null && dpd.getXX_SalePricePlusTax().compareTo(new BigDecimal(0))>0){
									if(dpd.isXX_IsDefinitive() == false ){
										dpd.setXX_IsDefinitiveIndividual(true);
										dpd.save();
									}
								}else cantidad++;
							
							}
							rs.close();
							pstmt.close();
							if(cantidad > 0)
								ADialog.info(EnvConstants.WINDOW_INFO, new Container(), "Se fijaron los precios definitivos de los productos de la distribución "+dist.get_ID() +" a excepción de "+cantidad+" productos que tienen precio igual a 0");
							else ADialog.info(EnvConstants.WINDOW_INFO, new Container(), "Se fijaron los precios definitivos de todas los productos de la distribución "+dist.get_ID());							
							
						}catch (SQLException e){
							log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
						}finally{
							try {
								rs.close();
								pstmt.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}else if(order!=null){ // PRE-DISTRIBUIDA
					String sql = "\nSELECT PD.XX_VMR_PO_PRODUCTDISTRIB_ID " +
					 "\nFROM XX_VMR_PO_PRODUCTDISTRIB PD " +
					 "\nWHERE PD.XX_VMR_DISTRIBUTIONHEADER_ID = "+dist.getXX_VMR_DistributionHeader_ID();
					PreparedStatement pstmt =null;
					ResultSet rs = null;
					try{
						pstmt = DB.prepareStatement(sql, null);
						rs = pstmt.executeQuery();
							
						
						Integer pdID = null;
						int cantidad = 0;
						
						while(rs.next()){
							pdID = rs.getInt("XX_VMR_PO_PRODUCTDISTRIB_ID");
							X_XX_VMR_PO_ProductDistrib pd = new X_XX_VMR_PO_ProductDistrib(getCtx(), pdID, get_Trx());
							if(pd.getXX_SalePricePlusTax() != null && pd.getXX_SalePricePlusTax().compareTo(new BigDecimal(0))>0){
								if(pd.isXX_IsDefinitive() == false ){
									pd.setXX_IsDefinitiveIndividual(true);
									pd.save();
								}
							}else cantidad++;
						
						}
						rs.close();
						pstmt.close();
						if(cantidad > 0)
							ADialog.info(EnvConstants.WINDOW_INFO, new Container(), "Se fijaron los precios definitivos de los productos de la distribución "+dist.get_ID() +" a excepción de "+cantidad+" productos que tienen precio igual a 0");
						else ADialog.info(EnvConstants.WINDOW_INFO, new Container(), "Se fijaron los precios definitivos de todas los productos de la distribución "+dist.get_ID());							
						
					}catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}finally{
						try {
							rs.close();
							pstmt.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			}else if(get_ValueOld("XX_IsDefinitive").equals(true)){
				MOrder order = null;
				X_XX_VMR_DistributionHeader dist = new X_XX_VMR_DistributionHeader(getCtx(), get_ID(), get_Trx());
				if(getC_Order_ID()>0)
					order = new  MOrder(Env.getCtx(), getC_Order_ID(), null);
			
				if(order==null){ 
					String sql = "\nSELECT DPD.XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
					 "\nFROM XX_VMR_DISTRIBPRODUCTDETAIL DPD JOIN  XX_VMR_DISTRIBUTIONDETAIL DD ON (DPD.XX_VMR_DISTRIBUTIONDETAIL_ID = DD.XX_VMR_DISTRIBUTIONDETAIL_ID) " +
					 "\nWHERE DD.XX_VMR_DISTRIBUTIONHEADER_ID = "+dist.getXX_VMR_DistributionHeader_ID();
					PreparedStatement pstmt =null;
					ResultSet rs = null;
					try{
						pstmt = DB.prepareStatement(sql, null);
						rs = pstmt.executeQuery();
							
						Integer dpdID = null;
							
						while(rs.next()){
							dpdID = rs.getInt("XX_VMR_DISTRIBPRODUCTDETAIL_ID");
							X_XX_VMR_DistribProductDetail dpd = new X_XX_VMR_DistribProductDetail(getCtx(), dpdID, get_Trx());
							dpd.setXX_IsDefinitiveIndividual(false);
							dpd.save();
	
						}
					
							
						ADialog.info(EnvConstants.WINDOW_INFO, new Container(), "Se ha removido el precio definitivo de todos los productos de la distribución "+dist.get_ID());					
	
					}catch (SQLException e){
							log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}finally{
						try {
							rs.close();
							pstmt.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else if (order!=null){
					String sql = "\nSELECT PD.XX_VMR_PO_PRODUCTDISTRIB_ID " +
					 "\nFROM XX_VMR_PO_PRODUCTDISTRIB PD " +
					 "\nWHERE PD.XX_VMR_DISTRIBUTIONHEADER_ID = "+dist.getXX_VMR_DistributionHeader_ID();
					PreparedStatement pstmt =null;
					ResultSet rs = null;
					try{
						pstmt = DB.prepareStatement(sql, null);
						rs = pstmt.executeQuery();
							
						Integer pdID = null;
							
						while(rs.next()){
							pdID = rs.getInt("XX_VMR_PO_PRODUCTDISTRIB_ID");
							X_XX_VMR_PO_ProductDistrib pd = new X_XX_VMR_PO_ProductDistrib(getCtx(), pdID, get_Trx());
							pd.setXX_IsDefinitiveIndividual(false);
							pd.save();
						}
					
							
						ADialog.info(EnvConstants.WINDOW_INFO, new Container(), "Se ha removido el precio definitivo de todos los productos de la distribución "+dist.get_ID());					
	
					}catch (SQLException e){
							log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}finally{
						try {
							rs.close();
							pstmt.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}	
		}
		return save;
	}

	public int getM_Warehouse_ID() {
		 return get_ValueAsInt("M_Warehouse_ID");
	}
}
