package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

/**
 *  Modelo extendido de InOutLine
 *
 *  @author     José Trías
 *  @version    
 */
public class MInOutLine extends org.compiere.model.MInOutLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInOutLine.class);
	
	int PU=0;
	int PS=0;

	public MInOutLine(Ctx ctx, int M_InOut_ID, Trx trx) {
		super(ctx, M_InOut_ID, trx);
	}
	
	public MInOutLine(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	/*
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		MInOut inOutDevolu = new MInOut(Env.getCtx(),getM_InOut_ID(),null);
		
		//Si es una recepción de material
		if(!inOutDevolu.isSOTrx())
		{
			
			MInOut inOut = new MInOut( getCtx(), getM_InOut_ID(), null);
			MOrder order = new MOrder( getCtx(), inOut.getC_Order_ID(), null);
			
			//Si la O/C es nacional
			if(order.getXX_OrderType().equals("Nacional")){
				
				//Si viene de SITME la cantidad recibida no puede ser mayor a la cantidad ordenada
				if((order.getXX_ComesFromSITME() || newsReportVendor(order)) && getPickedQty().compareTo(getTargetQty())==1){
					setPickedQty(getTargetQty());
				}
				
				//La cantidad recibida no puede ser menor a la cantidad devuelta
				if(getPickedQty().compareTo(getScrappedQty())==-1){
					setPickedQty(getScrappedQty());
				}
				
				BigDecimal automaticReturn = getPickedQty().subtract(getTargetQty());
				BigDecimal result = new BigDecimal(0);
				
				if(automaticReturn.compareTo(new BigDecimal(0))==1){
					setXX_ExtraPieces(automaticReturn.intValue());
					setXX_MissingPieces(0);
					result = automaticReturn;
				}
				else if(automaticReturn.compareTo(new BigDecimal(0))==-1){
					setXX_ExtraPieces(0);
					setXX_MissingPieces((-1)*automaticReturn.intValue());
				}
				else if(automaticReturn.compareTo(new BigDecimal(0))==0){
					setXX_ExtraPieces(0);
					setXX_MissingPieces(0);
				}
				
				// Devoluciones de calidad del tamaño de TargetQty si es >= que getPickedQty()
				// Si no del tamaño de getPickedQty()
				
				if(getPickedQty().compareTo(getTargetQty())==-1){
					setQtyEntered(getPickedQty().subtract(getScrappedQty()));
				}
				else{
					setQtyEntered(getTargetQty().subtract(getScrappedQty()));
				}
				
				setMovementQty(getQtyEntered());
				
				int returnMID = 0;
				if(result.compareTo(new BigDecimal(0))==1){
					
					returnMID = hasReturnMotive(get_ID());
					
				    MVLOReturnOfProduct returnOfProduct = new MVLOReturnOfProduct( getCtx(), returnMID, null);				
					Vector<X_XX_VLO_ReturnDetail> details = new Vector<X_XX_VLO_ReturnDetail>();
					
					int Qty = result.intValue();
					
					//si hay cabecera de return
					if(returnMID!=0){	
						details = surplusReturnDetail(returnMID);
						
						//modifico el detalle del sobrante
						if(details.size()>0)
						{
							for(int i=0; i<details.size(); i++){
								details.get(i).setXX_TotalPieces(Qty);
								details.get(i).save();
							}
						}else{							
							
							/*MProduct product = new MProduct(getCtx(), getM_Product_ID(), null);
							MOrderLine line = new MOrderLine(getCtx(), getC_OrderLine_ID(), null);
							//Calcular el impuesto				
							String sql = "SELECT (rate/100)"
										+ " FROM C_Tax"
										+ " WHERE ValidFrom="			
										+ " (SELECT MAX(ValidFrom)"											
										+ " FROM C_Tax"
										+ " WHERE C_TaxCategory_ID=" + product.getC_TaxCategory_ID()+")";	
							PreparedStatement prst = DB.prepareStatement(sql,null);
							BigDecimal Tax = new BigDecimal(1);
							try {
								ResultSet rs = prst.executeQuery();
								if (rs.next()){
									Tax = rs.getBigDecimal(1);
								}
								rs.close();
								prst.close();
							} catch (SQLException e){}
							*/
							//Creo el detalle del sobrante
							X_XX_VLO_ReturnDetail returnDetail = new X_XX_VLO_ReturnDetail( Env.getCtx(), 0, null);
							returnDetail.setXX_VLO_ReturnOfProduct_ID(returnOfProduct.get_ID());							
							returnDetail.setXX_VMR_CancellationMotive_ID(Env.getCtx().getContextAsInt("#XX_L_SURPLUSCANCELMOTIVE_ID"));
							returnDetail.setM_Product_ID(getM_Product_ID());
							returnDetail.setXX_TotalPieces(Qty);
							//returnDetail.setPriceActual(line.getPriceActual());
							returnDetail.setPriceActual(Env.ZERO);
							//returnDetail.setC_TaxCategory_ID(product.getC_TaxCategory_ID());
							returnDetail.setTaxAmt(Env.ZERO);
							returnDetail.save();
						}
					}
					else{//Si no hay cabecera la creo
						
						returnOfProduct.setXX_Status("PRE");
						returnOfProduct.setC_BPartner_ID(inOut.getC_BPartner_ID());
						returnOfProduct.setC_Order_ID(inOut.getC_Order_ID());
						returnOfProduct.setNotificationType("E");
						returnOfProduct.setXX_IdAsis_ID(inOut.getXX_CheckAssistant_ID());
						returnOfProduct.setXX_ReturnedFrom("CH");
						returnOfProduct.save();
						
						MProduct product = new MProduct(getCtx(), getM_Product_ID(), null);
						MOrderLine line = new MOrderLine(getCtx(), getC_OrderLine_ID(), null);
						//Calcular el impuesto				
						String sql = "SELECT (rate/100)"
									+ " FROM C_Tax"
									+ " WHERE ValidFrom="			
									+ " (SELECT MAX(ValidFrom)"											
									+ " FROM C_Tax"
									+ " WHERE C_TaxCategory_ID=" + product.getC_TaxCategory_ID()+")";	
						PreparedStatement prst = DB.prepareStatement(sql,null);
						BigDecimal Tax = new BigDecimal(1);
						try {
							ResultSet rs = prst.executeQuery();
							if (rs.next()){
								Tax = rs.getBigDecimal(1);
							}
							rs.close();
							prst.close();
						} catch (SQLException e){}
						
						//Creo el detalle del sobrante
						X_XX_VLO_ReturnDetail returnDetail = new X_XX_VLO_ReturnDetail( Env.getCtx(), 0, null);
						returnDetail.setXX_VLO_ReturnOfProduct_ID(returnOfProduct.get_ID());
						returnDetail.setXX_VMR_CancellationMotive_ID(Env.getCtx().getContextAsInt("#XX_L_SURPLUSCANCELMOTIVE_ID"));
						returnDetail.setM_Product_ID(getM_Product_ID());
						returnDetail.setXX_TotalPieces(Qty);
						returnDetail.setPriceActual(line.getPriceActual());
						//returnDetail.setPriceActual(Env.ZERO);
						returnDetail.setC_TaxCategory_ID(product.getC_TaxCategory_ID());
						returnDetail.setTaxAmt(Tax.multiply(line.getPriceActual()));
						//returnDetail.setTaxAmt(Env.ZERO);
						returnDetail.save();
					}		
				}
				else{
					
					returnMID = hasReturnMotive(get_ID());
					
					if(returnMID!=0){
						Vector<X_XX_VLO_ReturnDetail> details = surplusReturnDetail(returnMID);
						//Borro el detalle del sobrante, si los tiene
						if(details.size()>0)
						{
							for(int i=0; i<details.size(); i++){
								details.get(i).delete(true);
							}
						}
						
						if(notDetails(returnMID)){
							X_XX_VLO_ReturnOfProduct returnOfProduct = new X_XX_VLO_ReturnOfProduct( Env.getCtx(), returnMID, get_Trx());
							returnOfProduct.delete(true);
							
						}
						
					}
				}	
			}
			else{//Si es internacional
				
				BigDecimal diff = getPickedQty().subtract(getTargetQty());
				
				if(diff.compareTo(new BigDecimal(0))==1){
					setXX_ExtraPieces(diff.intValue());
					setXX_MissingPieces(0);
				}
				else if(diff.compareTo(new BigDecimal(0))==-1){
					setXX_ExtraPieces(0);
					setXX_MissingPieces((-1)*diff.intValue());
				}
				else if(diff.compareTo(new BigDecimal(0))==0){
					setXX_ExtraPieces(0);
					setXX_MissingPieces(0);
				}
				
				setQtyEntered(getPickedQty());
				setMovementQty(getQtyEntered());
			}
		}
		
		return true;
	}
	
	@Override
	protected boolean beforeDelete()
	{
		
		if(getC_OrderLine_ID()!=0){
			
			log.saveError("Error", Msg.getMsg(getCtx(), "DeleteMInOutLine"));
			return false;
		}
		else{
	
			int unsolicitedProductID = unsolicitedProductID();
			if(unsolicitedProductID!=0){
				
				X_XX_VLO_UnsolicitedProduct unsProd = new X_XX_VLO_UnsolicitedProduct(getCtx(), unsolicitedProductID, null);
				//MProduct product = new MProduct (getCtx(), unsProd.getM_Product_ID(), null);

				if(unsProd.delete(true)){
					System.out.println("ok");
				}else{
					System.out.println("no");
				}
				
				/*MVMRVendorProdRef vendorProdRef = new MVMRVendorProdRef(getCtx(), product.getXX_VMR_VendorProdRef_ID(), null);
				MVMRLine line = new MVMRLine(getCtx(), vendorProdRef.getXX_VMR_Line_ID(), null);
				MVMRSection section = new MVMRSection(getCtx(), vendorProdRef.getXX_VMR_Section_ID(), null);
				
				if(section.getValue().equals("99") && line.getValue().equals("99")){
					System.out.println("2");
					//product.delete(true);
					//vendorProdRef.delete(true);
				}*/
			}
		}
		
		return true;
	}
	
	private int unsolicitedProductID(){
		
		String sql = "SELECT XX_VLO_UNSOLICITEDPRODUCT_ID " +
					 "FROM XX_VLO_UNSOLICITEDPRODUCT " +
					 "WHERE M_INOUTLINE_ID="+get_ID();
	     
		PreparedStatement priceRulePstmt = null;
		ResultSet rs = null;
		try{
			
			priceRulePstmt = DB.prepareStatement(sql, null);
			rs = priceRulePstmt.executeQuery();
		
		while (rs.next())
		{
			return rs.getInt("XX_VLO_UNSOLICITEDPRODUCT_ID");
		}
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {priceRulePstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return 0;
	}
	
	/*
	 * XX_ExtraPieces
	 */
    public void setXX_ExtraPieces (int pieces)
    {
        set_Value ("XX_ExtraPieces", pieces);    
    }
  
    public int getXX_ExtraPieces() 
    {
        return get_ValueAsInt("XX_ExtraPieces"); 
    }
    
	/*
	 * XX_MissingPieces
	 */
    public void setXX_MissingPieces(int pieces)
    {
        set_Value ("XX_MissingPieces", pieces); 
    }
  
    public int getXX_MissingPieces() 
    {
        return get_ValueAsInt("XX_MissingPieces");
    }
    
    /*
	 * Set de XX_CodeToValidate
	 */
    public void setXX_CodeToValidate(boolean value)
    {
        set_ValueNoCheck ("XX_CodeToValidate", Boolean.valueOf(value));
    }
    
    public boolean isXX_CodeToValidate()
    {
        return get_ValueAsBoolean("XX_CodeToValidate");
    }
    
    private int hasReturnMotive(int inOutlineID){
		
		MInOutLine inOutLine = new MInOutLine( Env.getCtx(), inOutlineID, null);
		MInOut inOut = new MInOut( Env.getCtx(), inOutLine.getM_InOut_ID(), null);
		
		String SQL = "SELECT tab.XX_VLO_RETURNOFPRODUCT_ID " + 
					     "FROM XX_VLO_RETURNOFPRODUCT tab " +
					     "WHERE C_ORDER_ID="+inOut.getC_Order_ID();
		System.out.println(SQL);
		int returnID=0;
	
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next())
			{
				returnID = rs.getInt("XX_VLO_RETURNOFPRODUCT_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		
		return returnID;
	}
    
    private boolean notDetails(int returnMotiveID){
    	
    	String SQL = "SELECT tab.XX_VLO_RETURNDETAIL_ID " + 
	     "FROM XX_VLO_RETURNDETAIL tab " +
	     "WHERE XX_VLO_RETURNOFPRODUCT_ID="+returnMotiveID;

    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				return false;
			}

		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
    	
    	return true;
    }
 
    private Vector<X_XX_VLO_ReturnDetail> surplusReturnDetail(int returnMotiveID){
		
		Vector<X_XX_VLO_ReturnDetail> details = new Vector<X_XX_VLO_ReturnDetail>();
		
		String SQL = "SELECT tab.XX_VLO_RETURNDETAIL_ID " + 
					     "FROM XX_VLO_RETURNDETAIL tab " +
					     "WHERE M_PRODUCT_ID=" + getM_Product_ID() +
					     " AND XX_VMR_CANCELLATIONMOTIVE_ID=" + Env.getCtx().getContext("#XX_L_SURPLUSCANCELMOTIVE_ID") +
					     " AND XX_VLO_RETURNOFPRODUCT_ID="+returnMotiveID;
		System.out.println(SQL);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
		
			X_XX_VLO_ReturnDetail detail = null;
			while (rs.next())
			{
				detail = new X_XX_VLO_ReturnDetail( Env.getCtx(),rs.getInt("XX_VLO_RETURNDETAIL_ID"), null);
				details.add(detail);
			}
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		
		return details;
	}
	
    private boolean newsReportVendor(MOrder order){
		
		String sql = "SELECT * FROM XX_VLO_NEWSREPORTCOMPANY " +
					 "WHERE ISACTIVE = 'Y' AND C_BPARTNER_ID = " + order.getC_BPartner_ID();		

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {		
				return true;				
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return false;
	}
    
}
