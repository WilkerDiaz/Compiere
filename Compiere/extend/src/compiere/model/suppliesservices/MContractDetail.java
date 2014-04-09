//package compiere.model.suppliesservices;
//
//import java.math.BigDecimal;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//import org.compiere.util.Ctx;
//import org.compiere.util.DB;
//import org.compiere.util.Env;
//import org.compiere.util.Msg;
//import org.compiere.util.Trx;
//
//
///** Purchase of Supplies and Services
// * Maria Vintimilla Funcion 29 
// * Contract **/
//public class MContractDetail extends X_XX_ContractDetail{
//
//	private static final long serialVersionUID = 1L;
//	
//	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MContractDetail.class);
//	
//	/**
//	 * 	Standard Constructor
//	 *	@param ctx context
//	 *	@param XX_Contract_ID id
//	 *	@param trxName transaction
//	 */
//    public MContractDetail(Ctx ctx, int XX_ContractDetail_ID, Trx trxName) {
//		super(ctx, XX_ContractDetail_ID, trxName);
//		// TODO Auto-generated constructor stub
//	}
//    
//	/**
//	 * 	Load Constructor
//	 *	@param ctx context
//	 *	@param rs result set
//	 *	@param trxName transaction
//	 */
//	public MContractDetail(Ctx ctx, ResultSet rs, Trx trxName)	{
//		super(ctx, rs, trxName);
//	}//MContract
//	
//	
//	/**
//	 * 	obtain Amounts
//	 *	@param Percentage
//	 *  @param PayAmount
//	 *	@return Amount
//	 */
//	private BigDecimal obtainAmounts(BigDecimal PayAmount, BigDecimal Percentage){
//		BigDecimal Amount = PayAmount.multiply(Percentage.divide(new BigDecimal(100)));
//		return Amount;
//	}
//	
//	/**
//	 * 	Before Save
//	 *	@param newRecord new
//	 *	@return save
//	 */
//	protected boolean beforeSave (boolean newRecord) {
//		boolean save = super.beforeSave(newRecord);
//		X_XX_PayContract Pago = new X_XX_PayContract(Env.getCtx(), getXX_PAYCONTRACT_ID(), null);
//		X_XX_Contract contrato = new X_XX_Contract(Env.getCtx(), getXX_Contract_ID(), null);
//		String PaymentType = getXX_PaymentTypeDet();
//		BigDecimal detailPercent = getXX_PercenAmount();
//		BigDecimal detailAmount = getXX_ContractAmount();
//		BigDecimal payAmount = Pago.getXX_ContractAmount();
//		BigDecimal payPercent = Pago.getXX_CPercentage();
//		BigDecimal newAmount = new BigDecimal(0);
//		BigDecimal payPercentAmount = new BigDecimal(0);
//
//		if(save){
//			if((getXX_ContractAmount().compareTo(new BigDecimal(0)) == 0) && 
//					(getXX_PercenAmount().compareTo(new BigDecimal(0)) == 0) 	){
//				log.saveError("Error",Msg.translate(Env.getCtx(), 
//						"Se debe definir un monto para la distribución"));
//				return false;
//			}
//			if(PaymentType != null && PaymentType.equals("F")){
//				if(payPercent.compareTo(new BigDecimal(0)) > 0) {
//
//					//Obtain the Contract's Amount
//					String sql1 = "SELECT " +
//							" CASE WHEN p.xx_paymentrecorrency2 IS NOT NULL THEN  ( " +
//								"  CASE when p.xx_paymentrecorrency2 = 'MON' " +
//						" THEN 12 * (p.xx_cpercentage*c.xx_contractamount/100" +
//						" WHEN p.xx_paymentrecorrency2 = 'BIA' " +
//						" THEN 2 * (p.xx_cpercentage*c.xx_contractamount/100" +
//						" WHEN p.xx_paymentrecorrency2 = 'TRI' " +
//						" THEN 4 * (p.xx_cpercentage*c.xx_contractamount/100" +
//						" WHEN p.xx_paymentrecorrency2 = 'UNI' " +
//						" THEN (p.xx_cpercentage*c.xx_contractamount)/100 END) " +
//						" END PorcentajePago " +
//							" FROM XX_Contract C INNER JOIN XX_PayContract P" +
//							" ON (P.XX_Contract_ID = C.XX_Contract_ID)" +
//							" WHERE P.XX_Contract_ID = " + getXX_Contract_ID() +
//							" AND P.XX_PAYCONTRACT_ID = " + getXX_PAYCONTRACT_ID();
//					PreparedStatement pstmt1 = null;
//					ResultSet rs1 = null;
//					try{
//						pstmt1 = DB.prepareStatement(sql1, null); 
//						rs1 = pstmt1.executeQuery();
//						if(rs1.next()){
//							payPercentAmount = rs1.getBigDecimal("PorcentajePago");
//						}			    
//					}
//					catch (Exception e) {
//						e.printStackTrace();
//					}
//					finally {
//						DB.closeResultSet(rs1);
//						DB.closeStatement(pstmt1);
//					}
//				}
//				
//				
//				String sql2 = " SELECT COALESCE(SUM(XX_CONTRACTAMOUNT),0) SUMA1, " +
//							" COALESCE(SUM(XX_PERCENAMOUNT),0) SUMA2 " +
//							" FROM XX_ContractDetail " +
//							" WHERE XX_PAYCONTRACT_ID = " + getXX_PAYCONTRACT_ID();
//				
//				PreparedStatement pstmt2 = null;
//				ResultSet rs2 = null;
//				try{
//					pstmt2 = DB.prepareStatement(sql2, null); 
//					rs2 = pstmt2.executeQuery();
//					if(rs2.next()){
//						detailAmount = detailAmount.add(rs2.getBigDecimal("SUMA1"));
//						detailPercent = detailPercent.add(rs2.getBigDecimal("SUMA2"));
//					}			    
//				}
//				catch (Exception e) {
//					e.printStackTrace();
//				}
//				finally {
//					DB.closeResultSet(rs2);
//					DB.closeStatement(pstmt2);
//				}
//
//				// Se comparan los montos de acuerdo al tipo de distribucion
//				// Tipo de Distribucion: monto
//				if(contrato.isXX_IsDistrbAmount()){
//					if(detailAmount.compareTo(new BigDecimal(0)) > 0 
//							&& detailAmount.compareTo(payAmount) == 1 ){
//						log.saveError("Error",Msg.translate(Env.getCtx(), "XX_ContractDetailAmount"));
//						return false;
//					}//Fin if distrib
//					else if(detailAmount.compareTo(new BigDecimal(0)) > 0 
//							&& detailAmount.compareTo(payAmount) <= 0 ){
//						return true;
//					}	
//				}
//				// Tipo de Distribucion: porcentaje
//				else {
//					if(detailPercent.compareTo(new BigDecimal(100)) == 1 ){
//							log.saveError("Error",Msg.translate(Env.getCtx(), 
//									"XX_ContractDetailPercentage"));
//							return false;
//					}//Fin if distrib
//					else {
//						if(payPercentAmount.compareTo(new BigDecimal(0)) > 0){
//							newAmount = obtainAmounts(payPercentAmount,getXX_PercenAmount());						
//						}
//						else {
//							newAmount = obtainAmounts(Pago.getXX_ContractAmount(),getXX_PercenAmount());
//						}
//						setXX_ContractAmount(newAmount);
//						return true;
//					}
//				}
//			}// If Fixed
//		}//Fin Save
//		return save;
//	}//Fin beforeSave
//}// fin MContractDetail
package compiere.model.suppliesservices;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MContractDetail extends X_XX_ContractDetail{
	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 29 
	 * Contract **/
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MContractDetail.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_Contract_ID id
	 *	@param trxName transaction
	 */
    public MContractDetail(Ctx ctx, int XX_ContractDetail_ID, Trx trxName) {
		super(ctx, XX_ContractDetail_ID, trxName);
		// TODO Auto-generated constructor stub
	}
    
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MContractDetail(Ctx ctx, ResultSet rs, Trx trxName)	{
		super(ctx, rs, trxName);
	}//MContract
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord) {
		boolean save = super.beforeSave(newRecord);
		X_XX_PayContract Pago = new X_XX_PayContract(Env.getCtx(), getXX_PAYCONTRACT_ID(), null);
		X_XX_Contract contrato = new X_XX_Contract(Env.getCtx(), getXX_Contract_ID(), null);
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		String PaymentType = getXX_PaymentTypeDet();
		BigDecimal detailPercent = BigDecimal.ZERO;
		BigDecimal detailAmount = BigDecimal.ZERO;

		detailPercent = getXX_PercenAmount();
		detailAmount = getXX_ContractAmount();
					
		BigDecimal payAmount = Pago.getXX_ContractAmount();
		BigDecimal payPercent = Pago.getXX_CPercentage();
		BigDecimal newDetailAmount = new BigDecimal(0);
		BigDecimal newTotalDetailAmount = new BigDecimal(0);
		String SQLCalculo = ""; 

		if(save){
			if((getXX_ContractAmount().compareTo(new BigDecimal(0)) == 0) && 
					(getXX_PercenAmount().compareTo(new BigDecimal(0)) == 0) 	){
				log.saveError("Error",Msg.translate(Env.getCtx(), 
						"Se debe definir un monto para la distribución"));
				return false;
			}
			if(PaymentType != null && PaymentType.equals("F")){
				
				SQLCalculo = " SELECT COALESCE(SUM(XX_CONTRACTAMOUNT),0) Montos, " +
				" COALESCE(SUM(XX_PERCENAMOUNT),0) Porcentajes " +
				" FROM XX_ContractDetail " +
				" WHERE XX_PAYCONTRACT_ID = " + getXX_PAYCONTRACT_ID() + " AND XX_ContractDetail_ID <> " + getXX_ContractDetail_ID();

				try{
					pstmt2 = DB.prepareStatement(SQLCalculo, null); 
					rs2 = pstmt2.executeQuery();
					if(rs2.next()){
						detailAmount = detailAmount.add(rs2.getBigDecimal("Montos"));
						detailPercent = detailPercent.add(rs2.getBigDecimal("Porcentajes"));
					}			    
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					DB.closeResultSet(rs2);
					DB.closeStatement(pstmt2);
				}
				// Se comparan los montos de acuerdo al tipo de distribucion
				// Tipo de Distribucion: %
				if(detailPercent.compareTo(new BigDecimal(0)) > 0) {
					
					newTotalDetailAmount = (payAmount.multiply(detailPercent)).divide(new BigDecimal(100), BigDecimal.ROUND_UP);
					
					if(newTotalDetailAmount.compareTo(new BigDecimal(0)) > 0 
							&& newTotalDetailAmount.compareTo(payAmount) == 1 ){
						log.saveError("Error",Msg.translate(Env.getCtx(), "XX_ContractDetailAmount"));
						return false;
					}//monto distrib > monto pago
					else if(newTotalDetailAmount.compareTo(new BigDecimal(0)) > 0 
							&& newTotalDetailAmount.compareTo(payAmount) <= 0 ){
						newDetailAmount = (getXX_PercenAmount().multiply(payAmount)).divide( new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP);
						setXX_ContractAmount(newDetailAmount);
						return true;
					}//monto distrib < monto pago	
				} // distrib %
				else{
					// Tipo de Distribucion: monto
					if(detailAmount.compareTo(new BigDecimal(0)) > 0 
							&& detailAmount.compareTo(payAmount) == 1 ){
						log.saveError("Error",Msg.translate(Env.getCtx(), "XX_ContractDetailAmount"));
						return false;
					}//monto distrib > monto pago
					else if(detailAmount.compareTo(new BigDecimal(0)) > 0 
							&& detailAmount.compareTo(payAmount) <= 0 ){
						return true;
					}//monto distrib < monto pago						
				}// distrib bf
			}// If Fixed
			
		}//Fin Save
		
		//Se coloca el porcentaje si es tipo monto
		if(getXX_ContractAmount().compareTo(BigDecimal.ZERO)!=0)
		{
			X_XX_PayContract payCon = new X_XX_PayContract( Env.getCtx(), getXX_PAYCONTRACT_ID(), null);
			BigDecimal perc = (BigDecimal.valueOf(100).multiply(getXX_ContractAmount())).divide(payCon.getXX_ContractAmount(), RoundingMode.HALF_UP);
			setXX_PercenAmount(perc);
		}
		
		return save;
	}//Fin beforeSave
}// fin MContractDetail