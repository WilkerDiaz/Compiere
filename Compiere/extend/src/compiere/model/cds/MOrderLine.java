package compiere.model.cds;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.suppliesservices.X_Ref_XX_DistributionType_LV;

public class MOrderLine extends org.compiere.model.MOrderLine{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);

	/** XX_DistributionType AD_Reference_ID=1000299 */
	public static final int XX_DISTRIBUTIONTYPE_AD_Reference_ID=1000299;
	/** Distribution by Square Meters = ME */
	public static final String XX_DISTRIBUTIONTYPE_DistributionBySquareMeters = X_Ref_XX_DistributionType_LV.DISTRIBUTION_BY_SQUARE_METERS.getValue();
	/** Manual Distribution (Pieces/BsF) = PI */
	public static final String XX_DISTRIBUTIONTYPE_ManualDistributionPiecesBsF = X_Ref_XX_DistributionType_LV.MANUAL_DISTRIBUTION_PIECES_BS_F.getValue();
	/** Distribution by Percentages = PO */
	public static final String XX_DISTRIBUTIONTYPE_DistributionByPercentages = X_Ref_XX_DistributionType_LV.DISTRIBUTION_BY_PERCENTAGES.getValue();
	/** Distribution by Sales = SA */
	public static final String XX_DISTRIBUTIONTYPE_DistributionBySales = X_Ref_XX_DistributionType_LV.DISTRIBUTION_BY_SALES.getValue();

	public MOrderLine(Ctx ctx, int C_OrderLine_ID, Trx trx) {
		super(ctx, C_OrderLine_ID, trx);
	}
	public MOrderLine (Ctx ctx, ResultSet rs, Trx trxName)   {
		super(ctx, rs, trxName);
	}


	/*
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if it can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		String docType = "";
		int Products_ID = 0;

		/****Jessica Mendoza****/
		Utilities util = new Utilities();
		boolean bool = util.cuentaContable(getM_Product_ID());
		if (bool){
			/****Fin código - Jessica Mendoza****/

			boolean save = super.beforeSave(newRecord);
			if(save){
				/*** Purchase of Supplies and Services
				 * Maria Vintimilla Funcion 007 */ 
				MOrder cabecera = new MOrder(Env.getCtx(), getC_Order_ID(), get_Trx());
				docType = cabecera.getXX_POType();

				// Verifica de la OC es de Bienes y Servicios
				if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
					
					// Tax
					BigDecimal TaxAmount = new BigDecimal(0);
					BigDecimal TaxRate = new BigDecimal(0);
					int TaxRate_ID = getC_Tax_ID();
					BigDecimal Amount = getPriceActual();
					
					String sql1 = " Select Rate " +
								" From C_Tax " +
								" Where C_Tax_ID = " + TaxRate_ID +
								" AND AD_Client_ID = " + 
								Env.getCtx().getAD_Client_ID();
					PreparedStatement pstmt1 = null;
					ResultSet rs1 = null;
					
					try{
						pstmt1 = DB.prepareStatement(sql1, null); 
						rs1 = pstmt1.executeQuery();
						if(rs1.next()){
							TaxRate = rs1.getBigDecimal("RATE");
						}		    
					}//try
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						DB.closeResultSet(rs1);
						DB.closeStatement(pstmt1);
					}

					TaxAmount = ((Amount.multiply(TaxRate)).divide(
							new BigDecimal(100), 4, RoundingMode.HALF_UP)).multiply(getQtyEntered());	
					//System.out.println("Impuesto: "+TaxAmount);
					setTaxAmt(TaxAmount); 

					// Seleccionar los productos de la o/c y comparar sus precios
					// con el producto actual. Si son iguales no dejar guardar
					Vector <BigDecimal> precios = new Vector<BigDecimal>();
					String sqlPrecios = " SELECT PRICEENTERED PRECIO " +
										" FROM C_ORDERLINE" +
										" WHERE C_ORDER_ID = " + getC_Order_ID()+
										" AND C_OrderLine_ID != " + getC_OrderLine_ID() +
										" AND M_PRODUCT_ID = " + getM_Product_ID();
					PreparedStatement psprecios = null;
					ResultSet rsprecios = null;
					
					try {
						psprecios = DB.prepareStatement(sqlPrecios, null);
						rsprecios = psprecios.executeQuery();
						while(rsprecios.next()){
							BigDecimal precioLines = rsprecios.getBigDecimal("PRECIO"); 
							precios.add(precioLines);
						}//While
					}//try
					catch (SQLException e){
						System.out.println("Error (PO): " + e);			
					}
					finally{
						DB.closeResultSet(rsprecios);
						DB.closeStatement(psprecios);
					}

					// Producto, verifica si hay otros productos iguales al que se
					// esta ingresando
					String sqlprod = "Select M_Product_ID " +
								"From C_OrderLine " +
								"Where C_Order_ID = "+getC_Order_ID()+
								"And C_OrderLine_ID != "+getC_OrderLine_ID();
					PreparedStatement pstmt = null;
					ResultSet rs7 = null;
					try {
						pstmt = DB.prepareStatement(sqlprod, null);
						rs7 = pstmt.executeQuery();
						while(rs7.next()){
							Products_ID = rs7.getInt("M_Product_ID");
							if(getM_Product_ID() == Products_ID && 
									precios.contains(getPriceEntered().setScale(2, RoundingMode.HALF_UP))){
								log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_SaveError"));
								return false;
							}
						}//While
					}//try
					catch (SQLException e){
						System.out.println("Error (PO): " + e);			
					}
					finally {
						DB.closeResultSet(rs7);
						DB.closeStatement(pstmt);
					}

					String error_msg = "Será reaplicada la distribución";
					
					// Verificar si Qtyentered o PriceActual fueron cambiados
					if(is_ValueChanged("QtyEntered") || is_ValueChanged("PriceEntered")){
						if (isXX_IsDistribApplied()){
							ADialog.info(1, new Container(),error_msg);
							setXX_IsDistribApplied(false);
						}//if isXX_IsDistribApplied()
					}//valueChanged

				}// doctype
			}//save
			return save;
		}
		else{
			ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_NotElementValue"));
		}	
		return false;
	}// beforesave
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success){
		boolean save = super.afterSave(newRecord, success);
		if(save){
			if (super.afterSave(newRecord, success)){ // si after save original
				MOrder cabecera = new MOrder(Env.getCtx(), getC_Order_ID(), get_Trx());
				String docType = cabecera.getXX_POType();

				// Verify if the PO is for Assets/Services
				if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
					BigDecimal Quantity = new BigDecimal(0);
					String sql2 = "SELECT COALESCE(SUM(QtyOrdered),0) SUM "
								+ "FROM C_OrderLine "
								+ "WHERE C_Order_ID = "+ getC_Order_ID();
					//System.out.println(sql2);
					PreparedStatement prst2 = DB.prepareStatement(sql2,null);
					ResultSet rs2 = null;
					
					try {
						rs2 = prst2.executeQuery();
						if (rs2.next()){
							Quantity = rs2.getBigDecimal("SUM");
						}
					}//try
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						DB.closeResultSet(rs2);
						DB.closeStatement(prst2);
					}

					// Set XX_productQuantity
					String sql3 = "UPDATE C_Order "
						+ " SET XX_ProductQuantity = "+ Quantity
						+ " WHERE C_Order_ID = "+ getC_Order_ID();
					//System.out.println(sql3);
					int result = 0;					
					try {
						result = DB.executeUpdateEx(sql3, get_Trx());
					} 
					catch (SQLException e) { e.printStackTrace(); }

					if(result == -1){
						log.saveError("SaveError", Msg.translate(Env.getCtx(), "Error actualizando cantidades"));
						return false;
					}

					//// Estaba en el MOrderLine de Compiere se pasa al de CDS
					/** Purchase of Supplies and Services
					 * Maria Vintimilla Funcion 008 **/
					String sql4 = "UPDATE C_Order "
						+ "SET XX_IVA = GrandTotal - TotalLines "
						+ "WHERE C_Order_ID = " + getC_Order_ID();
					int result2 = 0;					
					try {
						result2 = DB.executeUpdateEx(sql4, get_Trx());
					} 
					catch (SQLException e) { e.printStackTrace(); }

					if(result2 == -1){
						log.saveError("SaveError", Msg.translate(Env.getCtx(), "XX_SaveErrorIVA"));
						return false;
					}

					if(newRecord){
						// Se setea el campo en la cabecera indicando que se agregó una linea
						// Maria Vintimilla
						String UpdateOrder = " UPDATE C_ORDER SET XX_NumLinesOrder = (XX_NumLinesOrder + 1)" 
							+ " WHERE C_ORDER_ID = " + getC_Order_ID();
						int result3 = 0;					
						try {
							result3 = DB.executeUpdateEx(UpdateOrder, get_Trx());
						} 
						catch (SQLException e) { e.printStackTrace(); }

						if(result3 == -1){
							log.saveError("SaveError", Msg.translate(Env.getCtx(), "Error actualizando numero de lineas"));
							return false;
						}
					}
				}//doctype

			}//after
		}//save
		return save;
	}// aftersave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	@Override
	protected boolean afterDelete (boolean success)	{
		if (!success)
			return success;
		MOrder cabecera = new MOrder(Env.getCtx(), getC_Order_ID(), get_Trx());
		

		// Funcion 106 Distribucion de OC desde Cabecera
		// Se elimina la linea en el contador de lineas en la cabecera
		// Maria Vintimilla
		if (cabecera.getXX_NumLinesOrder() > 0){
			String UpdateOrder = " UPDATE C_ORDER SET XX_NumLinesOrder = (XX_NumLinesOrder - 1)"  
				+ " WHERE C_ORDER_ID = " + getC_Order_ID();
			int result = 0;					
			try {
				result = DB.executeUpdateEx(UpdateOrder, get_Trx());
			} 
			catch (SQLException e) { e.printStackTrace(); }

			if(result == -1){
				log.saveError("SaveError", Msg.translate(Env.getCtx(), "Error actualizando numero de lineas"));
				return false;
			}
		}

		return true;
	}	//	afterDelete


	/** Set Vendor Reference
    @param XX_VMR_PO_LINEREFPROV_ID Vendor Reference  */
	public void setXX_VMR_PO_LineRefProv_ID (int XX_VMR_PO_LineRefProv_ID){
		if (XX_VMR_PO_LineRefProv_ID <= 0) set_ValueNoCheck ("XX_VMR_PO_LineRefProv_ID", null);
		else
			set_ValueNoCheck ("XX_VMR_PO_LineRefProv_ID", Integer.valueOf(XX_VMR_PO_LineRefProv_ID));
	}

	/** Get Vendor Reference .
    @return Vendor Reference  */
	public int getXX_VMR_PO_LINEREFPROV_ID(){
		return get_ValueAsInt("XX_VMR_PO_LineRefProv_ID");   
	}

	public int getXX_PriceConsecutive(){
		return get_ValueAsInt("XX_PriceConsecutive");
	}

	public void setXX_PriceConsecutive (int XX_PriceConsecutive){
		set_Value ("XX_PriceConsecutive", XX_PriceConsecutive);
	}

	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 007**/

	/** Set TaxAmt.
	@param TaxAmt TaxAmt on MOrderLine */
	public void setTaxAmt(BigDecimal TaxAmt){
		set_Value ("TaxAmt", TaxAmt);   	
	}

	/** Get TaxAmt.
	@return TaxAmt TaxAmt on MOrderLine */
	public BigDecimal getTaxAmt(){
		return get_ValueAsBigDecimal("TaxAmt");      
	}

	/** Set XX_VMR_UnitConversion_ID.
	@param XX_VMR_UnitConversion_ID Unit Conversion on MOrderLine */
	public void setXX_VMR_UnitConversion_ID(int XX_VMR_UnitConversion_ID){
		set_Value ("XX_VMR_UnitConversion_ID", XX_VMR_UnitConversion_ID);   	
	}

	/** Get XX_VMR_UnitConversion_ID.
	@return XX_VMR_UnitConversion_ID Unit Conversion on MOrderLine */
	public int getXX_VMR_UnitConversion_ID(){
		return get_ValueAsInt("XX_VMR_UnitConversion_ID");      
	}

	/** Set XX_VMR_UnitPurchase_ID.
	@return XX_VMR_UnitPurchase_ID Unit Purchase on MOrderLine */
	public void setXX_VMR_UnitPurchase_ID(int XX_VMR_UnitPurchase_ID){
		set_Value ("XX_VMR_UnitPurchase_ID", XX_VMR_UnitPurchase_ID);
	}

	/** Get XX_VMR_UnitPurchase_ID.
	@param XX_VMR_UnitPurchase_ID Uni Purchase on MOrderLine */
	public int getXX_VMR_UnitPurchase_ID(){
		return get_ValueAsInt("XX_VMR_UnitPurchase_ID");
	}

	/** Set XX_DistributionApplied.
    @param XX_DistributionApplied XX_DistributionApplied */
	public void setXX_IsDistribApplied (boolean XX_IsDistribApplied)  {
		set_Value ("XX_IsDistribApplied", Boolean.valueOf(XX_IsDistribApplied));

	}

	/** Get XX_DistributionApplied.
    @return XX_DistributionApplied */
	public boolean isXX_IsDistribApplied(){
		return get_ValueAsBoolean("XX_IsDistribApplied");

	}


	/** Set Is Amount distribution.
    @param XX_IsAmountDistrib Is Amount distribution */
	public void setXX_IsAmountDistrib (boolean XX_IsAmountDistrib)   {
		set_Value ("XX_IsAmountDistrib", Boolean.valueOf(XX_IsAmountDistrib));

	}

	/** Get IsAmount distribution.
    @return Is Amount distribution */
	public boolean isXX_IsAmountDistrib()  {
		return get_ValueAsBoolean("XX_IsAmountDistrib");

	}

	/** Set Is Pieces Percentage.
    @param XX_IsPiecesPercentage Is Pieces Percentage */
	public void setXX_IsPiecesPercentage (boolean XX_IsPiecesPercentage)   {
		set_Value ("XX_IsPiecesPercentage", Boolean.valueOf(XX_IsPiecesPercentage));

	}

	/** Get Is Pieces Percentage.
    @return Is Pieces Percentage */
	public boolean isXX_IsPiecesPercentage()  {
		return get_ValueAsBoolean("XX_IsPiecesPercentage");

	}

	/** Is test a valid value.
    @param test testvalue
    @return true if valid **/
	public static boolean isXX_DistributionTypeValid(String test) {
		return X_Ref_XX_DistributionType_LV.isValid(test);

	}
	/** Set Distribution Type.
    @param XX_DistributionType Distribution Type */
	public void setXX_DistributionType (String XX_DistributionType) {
		if (!isXX_DistributionTypeValid(XX_DistributionType))
			throw new IllegalArgumentException ("XX_DistributionType Invalid value - " + XX_DistributionType + " - Reference_ID=1000299 - ME - PI - PO - SA");
		set_Value ("XX_DistributionType", XX_DistributionType);

	}

	/** Get Distribution Type.
    @return Distribution Type */
	public String getXX_DistributionType()  {
		return (String)get_Value("XX_DistributionType");

	}

	/** Set XX_ClearedDistrib.
    @param XX_ClearedDistrib XX_ClearedDistrib */
	public void setXX_ClearedDistrib (boolean XX_ClearedDistrib)  {
		set_Value ("XX_ClearedDistrib", Boolean.valueOf(XX_ClearedDistrib));

	}

	/** Get XX_ClearedDistrib.
    @return XX_ClearedDistrib */
	public boolean isXX_ClearedDistrib(){
		return get_ValueAsBoolean("XX_ClearedDistrib");

	}

}// Fin MOrderLine
