package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MTax;
import org.compiere.model.MUOMConversion;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;
import compiere.model.suppliesservices.X_XX_Contract;

public class InvoiceLineCallout extends CalloutEngine {
	
	/**	Logger							*/
	protected transient CLogger	log = CLogger.getCLogger (getClass());
	Utilities util = new Utilities();

	public String amt (Ctx ctx, int WindowNo, GridTab mTab, 
			GridField mField, Object value, Object oldValue)	{
		
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int C_UOM_To_ID = ctx.getContextAsInt( WindowNo, "C_UOM_ID");
		int M_Product_ID = ctx.getContextAsInt( WindowNo, "M_Product_ID");
		int C_Invoice_ID = ctx.getContextAsInt( WindowNo, "C_Invoice_ID");
		int StdPrecision = DB.getSQLValue(null,
				"SELECT c.StdPrecision FROM C_Currency c INNER JOIN C_Invoice x ON (x.C_Currency_ID=c.C_Currency_ID) WHERE x.C_Invoice_ID=?", 
				C_Invoice_ID);
		if (StdPrecision < 0)
		{
			log.warning("Precision=" + StdPrecision + " - set to 2");
			StdPrecision = 2;
		}

		BigDecimal QtyEntered, QtyInvoiced, PriceEntered, PriceActual;
		//	get values
		QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
		QtyInvoiced = (BigDecimal)mTab.getValue("QtyInvoiced");
		log.fine("QtyEntered=" + QtyEntered + ", Invoiced=" + QtyInvoiced + ", UOM=" + C_UOM_To_ID);
		
		PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		PriceActual = (BigDecimal)mTab.getValue("PriceActual");
				
		//	Qty changed - recalc price
		if ((mField.getColumnName().equals("QtyInvoiced") ||
			 mField.getColumnName().equals("M_Product_ID")) 
			&& !"N".equals(ctx.getContext( WindowNo, "DiscountSchema")))
		{
			if (QtyInvoiced == null) // este se puede quedar por si no se trae el valor de la O/C
				QtyInvoiced = QtyEntered; 
			
			if (QtyInvoiced.scale() > StdPrecision)
				QtyInvoiced = QtyInvoiced.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			
			if (QtyEntered.scale() > StdPrecision)
				QtyEntered = QtyEntered.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			
			mTab.setValue("QtyEntered", QtyEntered);
			mTab.setValue("QtyInvoiced", QtyInvoiced);
		}
		else if (mField.getColumnName().equals("XX_PriceActualInvoice"))
		{
			
			PriceEntered = (BigDecimal)value;
			if (PriceActual == null)
				PriceActual = PriceEntered;
			
			if (PriceActual.scale() > StdPrecision)
				PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			
			if (PriceEntered.scale() > StdPrecision)
				PriceEntered = PriceEntered.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			PriceActual = (BigDecimal)value;
			mTab.setValue("PriceActual", (BigDecimal)PriceActual);
			log.fine("amt - PriceEntered=" + PriceEntered + " -> PriceActual=" + PriceActual);
		}
		
		/** Doing the QtyInvoiced conversion by UOM data */
		BigDecimal QtyInvoiced1 = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyInvoiced);
		if (QtyInvoiced1.scale() > StdPrecision)
			QtyInvoiced1 = QtyInvoiced1.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);		
		
		//	Line Net Amt
		BigDecimal LineNetAmt = QtyInvoiced1.multiply(PriceActual);
		
		/**  Discount entered - Calculate Line Net Amount */
		BigDecimal porc = new BigDecimal(100);
		
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		log.info("LineNetAmt=" + LineNetAmt);
		mTab.setValue("LineNetAmt", LineNetAmt);
		//	Calculate Tax Amount for PO
		boolean IsSOTrx = "Y".equals(ctx.getContext( WindowNo, "IsSOTrx"));
		if (!IsSOTrx)
		{
			BigDecimal TaxAmt = org.compiere.util.Env.ZERO;
			if (mField.getColumnName().equals("TaxAmt"))
			{
				TaxAmt = (BigDecimal)mTab.getValue("TaxAmt");
			}
			else
			{
				Integer taxID = (Integer)mTab.getValue("C_Tax_ID");
				if (taxID != null)
				{
					MTax tax = new MTax (ctx, taxID, null);
					BigDecimal rate = tax.getRate();
					TaxAmt = LineNetAmt.multiply(rate.divide(porc));		
					mTab.setValue("TaxAmt", TaxAmt);
				}
			}
			//	Add it up
			mTab.setValue("LineTotalAmt", LineNetAmt.add(TaxAmt));
		}
		
		setCalloutActive(false);
		return "";
	}

	/**
	 * Se encarga de realizar la conversion de una moneda extranjera a local, 
	 * en la línea de la factura, multiplicando el costo de venta (PriceActual) 
	 * por el factor de cambio definitivo, ubicado en la O/C
	 * @author Jessica Mendoza
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return
	 */
	public String conversionLine (Ctx ctx, int WindowNo, GridTab mTab, 
			GridField mField, Object value, Object oldValue){
		
		if (value == null){
			Integer idInvoice = (Integer) mTab.getValue("C_Invoice_ID");
			int idOrder = 0;
			BigDecimal total = new BigDecimal(0);
			
			String sql = "select C_Order_ID from C_Invoice where C_Invoice_ID = " + idInvoice;
			PreparedStatement priceRulePstmt = null;
			ResultSet rs = null;
			try{
				priceRulePstmt = DB.prepareStatement(sql, null);
				rs = priceRulePstmt.executeQuery();
				if (rs.next()){
					idOrder = rs.getInt(1);
				}
				
			}catch(Exception e){
				return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
			} finally{
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					priceRulePstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			MOrder order = new MOrder(Env.getCtx(),idOrder, null);
			total = (BigDecimal)mTab.getValue("PriceActual");
			if(order.getXX_OrderType().equals("Importada")){				
				mTab.setValue("XX_PriceActualInvoice", total.multiply(order.getXX_DefinitiveFactor())); 
			}else{
				mTab.setValue("XX_PriceActualInvoice", total);
			}		
			
		}		
		return "";
	}
	
	/**
	 * Se encarga de realizar la conversion de una moneda extranjera a local, 
	 * en la factura, multiplicando el total de la factura (GrandTotal) 
	 * por el factor de cambio definitivo, ubicado en la O/C
	 * @author Jessica Mendoza
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return
	 */
	public String conversionInvoice (Ctx ctx, int WindowNo, GridTab mTab, 
			GridField mField, Object value, Object oldValue){
		
		if (value == null){
			Integer idOrder = (Integer) mTab.getValue("C_Order_ID");
			BigDecimal total = new BigDecimal(0);
			MOrder order = new MOrder(Env.getCtx(),idOrder, null);
			total = (BigDecimal)mTab.getValue("GrandTotal");

			Integer idFactura = (Integer) mTab.getValue("C_Invoice_ID");
			float re = util.retencion(idFactura);
			BigDecimal retencion = new BigDecimal(re);
			
			if(order.getXX_OrderType().equals("Importada")){				
				mTab.setValue("XX_GrandTotalLocal", total.multiply(order.getXX_DefinitiveFactor())); 
			}else{
				mTab.setValue("XX_GrandTotalLocal", total.subtract(retencion));
			}		
			
		}		
		return "";
	}
	
}
