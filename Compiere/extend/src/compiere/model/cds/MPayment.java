package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.birt.BIRTReport;

public class MPayment extends org.compiere.model.MPayment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoice.class);
	Utilities util = new Utilities();
	private int ad_role = Env.getCtx().getAD_Role_ID();
	
	public MPayment(Ctx ctx, int C_Payment_ID, Trx trx) {
		super(ctx, C_Payment_ID, trx);
	}
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPayment(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 * @author Jessica Mendoza
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){	
		if(super.beforeSave(newRecord)){

			//Si es un anticipo, 
			//coloca eliminable la cuenta por pagar estimada, que contenga la menor fecha de dicha O/C
			if (getIsAdvance()){
				if (newRecord){			
					/*Validar que los roles: Jefe de Finanzas, Coordinador de Finanzas, Analista de Cuentas por pagar y 
					Auxiliar de Cuentas por pagar, sean los que puedan crear un anticipo*/
					if (ad_role == Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID") || 
							ad_role == Env.getCtx().getContextAsInt("") || 
							ad_role == Env.getCtx().getContextAsInt("#XX_L_ROLEANALYSTAPAYABLE_ID") || 
							ad_role == Env.getCtx().getContextAsInt("")){								
						String sql = "select min(XX_DateEstimated), XX_Dispensable, XX_VCN_EstimatedAPayable_ID " +
							  	     "from XX_VCN_EstimatedAPayable " +
							  	     "where C_Order_ID = " + getC_Order_ID() + " " +
							  	     "group by XX_VCN_EstimatedAPayable_ID, XX_Dispensable ";
		
						PreparedStatement pstmt = null; 
						ResultSet rs = null;
						try{
							pstmt = DB.prepareStatement(sql, get_Trx()); 
							rs = pstmt.executeQuery();
							if(rs.next()){	
								if (rs.getString(2).equals("N")){
									//Colocar eliminable la CxPE, con dicha O/C
									String update = "update XX_VCN_EstimatedAPayable " +
													"set XX_Dispensable = 'Y' " +
													"where XX_VCN_EstimatedAPayable_ID = " + rs.getInt(3);
									DB.executeUpdate(get_Trx(), update);
								}
							}	
						}catch (Exception e) {
							log.log(Level.SEVERE, sql);
						}finally{
							try {
							rs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								pstmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				//Si está completado el registro:
				if (getDocStatus().equals("CO")){
					if (ad_role == Env.getCtx().getContextAsInt("#XX_L_ROLECOORDAPAYABLE_ID") || 
							ad_role == Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID")){
						//1. Eliminar la cuenta por pagar estimada que contenga dicha O/C
						String sqlDelete = "delete from XX_VCN_EstimatedAPayable " +
									       "where C_Order_ID = " + getC_Order_ID() + " " +
									       "and XX_Dispensable = 'Y' ";
						DB.executeUpdate(get_Trx(), sqlDelete);

						//3. Enviar dicho anticipo a la interfaz de pago
						ArrayList<Integer> listPay = new ArrayList<Integer>();
						listPay.add(getC_Payment_ID());
						interfaceOrderPay(listPay);
					}
				}
			}
			
			
			//Calcula el monto total local del pago, a través del factor definitivo de la O/C
			MOrder order = new MOrder(Env.getCtx(), getC_Order_ID(), get_Trx());
			if (order.getXX_OrderType().equalsIgnoreCase("Importada")){
				setXX_PayAmtLocal(getPayAmt().multiply(order.getXX_DefinitiveFactor()));
			}
		}
		return true;
	}
	
	/**
	 * Se encarga de ejecutar el proceso de la interfaz de pago
	 * @author Jessica Mendoza
	 * @param idPay lista con los identificadores de los 
	 * pagos que se deben procesar
	 */
	public void interfaceOrderPay(ArrayList<Integer> idPay){
		try{
			MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_INTERFACEORDERPAY_ID"), 0); 
			mpi.save();
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_INTERFACEORDERPAY_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_INTERFACEORDERPAY_ID")); 
			pi.setClassName(""); 
			pi.setTitle(""); 
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("C_Payment_ID", idPay, null, "", null));
			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
			ProcessCtl pc = new ProcessCtl(null ,pi, null); 
			pc.start();
		}catch(Exception e){
			log.log(Level.SEVERE,e.getMessage()); 
		}
	}

	
	/**
	 * Se encarga de al completar enviar un anticipo a la Interfaz con banco
	 * @author Rubi Cornejo
	 * @return new status (Complete, In Progress, Invalid, Waiting ..)
	 */

	public String completeIt()
	{
		String complete = super.completeIt();
		
			if (getIsAdvance()){
				if (ad_role == Env.getCtx().getContextAsInt("#XX_L_ROLECOORDAPAYABLE_ID") || 
					ad_role == Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID")){
					//1. Eliminar la cuenta por pagar estimada que contenga dicha O/C
					String sqlDelete = "delete from XX_VCN_EstimatedAPayable " +
							       "where C_Order_ID = " + getC_Order_ID() + " " +
							       "and XX_Dispensable = 'Y' ";
					DB.executeUpdate(get_Trx(), sqlDelete);
				
					//2. Enviar dicho anticipo a la interfaz de pago
					ArrayList<Integer> listPay = new ArrayList<Integer>();
					listPay.add(getC_Payment_ID());
					interfaceOrderPay(listPay);

					//3. Generar el Reporte definitivo de pago
					reportDefinitive(getC_Payment_ID());
				}			
			}
				
		return complete;
	}	//	completeIt
	
	/**
	 * Genera el reporte del definitivo de pago, 
	 * para los diferentes tipo de orden
	 * @param to C_Payment_id
	 * Creado por Rubì Cornejo
	 */
	public void reportDefinitive(Integer pay){ 
		int idPay = pay;
		String designName = "DefinitiveLiveAnt";
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("pago_id");
		myReport.parameterValue.add(idPay);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	/** Set Payment amount local.
    @param XX_PayAmtLocal Amount being paid local*/
    public void setXX_PayAmtLocal (java.math.BigDecimal XX_PayAmtLocal){
        set_Value ("XX_PayAmtLocal", XX_PayAmtLocal);       
    }

    /** Get Payment amount.
    @return Amount being paid local*/
    public java.math.BigDecimal getXX_PayAmtLocal() 
    {
        return get_ValueAsBigDecimal("XX_PayAmtLocal");
        
    }
	
	/** Get XX_SynchronizationBank.
    @return XX_SynchronizationBank */
    public Boolean getXX_SynchronizationBank() 
    {
        return (Boolean)get_Value("XX_SynchronizationBank");
    }
    
    /** Set XX_SynchronizationBank.
    @param XX_SynchronizationBank */
    public void setXX_SynchronizationBank (Boolean XX_SynchronizationBank)
    {
        set_Value ("XX_SynchronizationBank", XX_SynchronizationBank);
    }
    
	/** Get XX_UserPrintReport_ID.
    @return XX_UserPrintReport_ID */
    public int getXX_UserPrintReport_ID() 
    {
        return (Integer)get_Value("XX_UserPrintReport_ID");
    }
    
    /** Set XX_UserPrintReport_ID.
    @param XX_UserPrintReport_ID */
    public void setXX_UserPrintReport_ID (int XX_UserPrintReport_ID)
    {
        set_Value ("XX_UserPrintReport_ID", XX_UserPrintReport_ID);
    }
    
	/** Set Advance.
    @param XX_IsAdvance Indicates if this document is advance */
    public void setXX_IsAdvance (boolean XX_IsAdvance)
    {
        set_ValueNoCheck ("XX_IsAdvance", Boolean.valueOf(XX_IsAdvance));
        
    }
    
    /** Get Advance.
    @return Indicates if this document is advance */
    public boolean getIsAdvance() 
    {
        return get_ValueAsBoolean("XX_IsAdvance");
        
    }
    
    /** Set XX_AccountPayableStatus
    @param  */
    public void setXX_AccountPayableStatus (String XX_AccountPayableStatus)
    {
        set_Value ("XX_AccountPayableStatus", XX_AccountPayableStatus);
      
    }
    
    /** Get XX_AccountPayableStatus
    @return  Estado de las cuentas por pagar en factura */
    public String getXX_AccountPayableStatus() 
    {
        return (String) get_Value("XX_AccountPayableStatus");
        
    }
    
    /** Set XX_DateFinalPay
    @param  */
    public void setXX_DateFinalPay (Timestamp XX_DateFinalPay)
    {
        set_Value ("XX_DateFinalPay", XX_DateFinalPay);
      
    }
    
    /** Get XX_DateFinalPay
    @return  La fecha de pago */
    public Timestamp getXX_DateFinalPay() 
    {
        return (Timestamp) get_Value("XX_DateFinalPay");
        
    }
    
    //Ivan Valdes Migliore
    
	/** Set XX_VCN_ManualCheck
    @param XX_VCN_ManualCheck Indicates if this document is manual check */
    public void setXX_VCN_ManualCheck (boolean XX_VCN_ManualCheck)
    {
        set_ValueNoCheck ("XX_VCN_ManualCheck", Boolean.valueOf(XX_VCN_ManualCheck));
        
    }
    
	/** Get XX_VCN_ManualCheck
    @return XX_VCN_ManualCheck */
    public Boolean getXX_VCN_ManualCheck() 
    {
        return (Boolean)get_Value("XX_VCN_ManualCheck");
    }

    /** Get XX_VCN_ElementValue
    @return XX_VCN_ElementValue */
    public int getXX_VCN_ElementValue() 
    {
        return (Integer)get_Value("XX_VCN_ElementValue");
    }
    
    /** Set XX_VCN_ElementValue
    @param XX_VCN_ElementValue */
    public void setXX_VCN_ElementValue (int XX_VCN_ElementValue)
    {
        set_Value ("XX_VCN_ElementValue", XX_VCN_ElementValue);
    }
}
