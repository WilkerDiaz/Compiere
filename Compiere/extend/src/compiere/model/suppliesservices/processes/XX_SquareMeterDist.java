package compiere.model.suppliesservices.processes;
import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.suppliesservices.X_XX_ProductPercentDistrib;

public class XX_SquareMeterDist extends SvrProcess{
	/** Purchase of Supplies and Services 
	 * Maria Vintimilla Funcion 22
	 * Distribution by Square Meters**/
	@Override
	protected void prepare() {

	}//prepare
	
	@Override
	protected String doIt() throws Exception {
		int NumCostCenter = 0;
		int total = 0, most = 0; double most_value_percentage = 0;
		Vector <Integer> Orgs = new Vector<Integer>();
		Vector <Integer> lines = new Vector<Integer>();
		Vector <BigDecimal> SquareMeters = new Vector<BigDecimal>();
		Vector <BigDecimal> PercentageCC = new Vector<BigDecimal>();
		Vector <BigDecimal> AmountCC = new Vector<BigDecimal>();
		Vector <BigDecimal> QuantityCC = new Vector<BigDecimal>();
		BigDecimal Difference = new BigDecimal(0);
		BigDecimal Percentage = new BigDecimal(0);
		BigDecimal Quantity = new BigDecimal(0); 
		BigDecimal Quantities = new BigDecimal(0);
		BigDecimal TotalMeters = new BigDecimal(0);
		BigDecimal Meters = new BigDecimal(0);
		BigDecimal Amounts = new BigDecimal(0);
		BigDecimal Price = new BigDecimal(0);
		BigDecimal Sum = new BigDecimal(0);
		BigDecimal Aux = new BigDecimal(0);
		BigDecimal Aux2 = new BigDecimal(0);
		BigDecimal LineAmount = new BigDecimal(0);
		int currentTable = getProcessInfo().getTable_ID();
		MOrderLine OrderLine = null;
		MOrder order = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		// Se establece si la distribucion se realiza desde la cabecera o el detalle
		if(currentTable == Env.getCtx().getContextAsInt("#XX_L_CORDERTABLE_ID")) {
			order = new MOrder(Env.getCtx(), getRecord_ID(), get_Trx());

			// Se verifica que se tengan líneas asociadas a la OC
			if(order.getXX_NumLinesOrder() == 0){
				ADialog.error(1, new Container(),Msg.translate(Env.getCtx(), "XX_OrderLines"));
				order.setXX_DistribAllLines(false);
				return "No tiene lineas";
			}// else distribucion
			
			order.setXX_DistribAllLines(true);
			
			String sqlLines = " SELECT C_OrderLine_ID ID " +
								" FROM C_ORDERLINE" +
								" WHERE C_ORDER_ID = " + order.getC_Order_ID();
			try {
				ps = DB.prepareStatement(sqlLines, null);
				rs = ps.executeQuery();
				while(rs.next()){ 
					lines.add(rs.getInt("ID"));
				}//While
			}//try
			catch (SQLException e){
				e.printStackTrace();			
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}

		}
		else if (currentTable == Env.getCtx().getContextAsInt("#XX_L_CORDERLINETABLE_ID")){
			OrderLine = new MOrderLine(Env.getCtx(), getRecord_ID(), get_Trx());
			order = new MOrder(Env.getCtx(), OrderLine.getC_Order_ID(), get_Trx());
			lines.add(OrderLine.get_ID());
		}

		// Se recorren las líneas para borrar las distribuciones previas
		for(int lin = 0; lin < lines.size(); lin++){
			/** Delete previous Distributions */
			String sql_delete = " Delete From XX_ProductPercentDistrib " +
								" Where C_OrderLine_ID = " + lines.get(lin)+
								" And C_Order_ID = " + order.get_ID();
			//System.out.println("SQL Delete m2: "+sql_delete);
			try {
				result = DB.executeUpdateEx(sql_delete, get_Trx());
				commit();
			} 
			catch (SQLException e) { e.printStackTrace(); }
			
			if(result == -1){
				ADialog.error(1, new Container(), "Error eliminando las distribuciones");
			}
		}

		/** Get Total Square Meters from all Cost Centers and
		 	total number of the Cost Centers */
		String sql_meters = " SELECT SUM(XX_SquareMeter) SUMA, " +
							" COUNT(Distinct(AD_Org_ID)) NUM " +
							" FROM AD_Org WHERE AD_Org_ID NOT IN (0,1000060,1000067)" +
							" AND AD_Client_ID = " + 
							Env.getCtx().getAD_Client_ID();
		PreparedStatement pstmt_meters = null;
		ResultSet rs_meters = null;
		try{
			pstmt_meters = DB.prepareStatement(sql_meters, null); 
			rs_meters = pstmt_meters.executeQuery();
			if (rs_meters.next()){
				TotalMeters = rs_meters.getBigDecimal("SUMA"); 
				NumCostCenter = rs_meters.getInt("NUM");
			}
		}
		catch(Exception e){
			log.saveError("ErrorSql (catch): ", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			rs_meters.close();
			pstmt_meters.close();
		}

		/** Set Percentages, Quantities and prices for the Distribution */
		String sql_dist = "SELECT CASE WHEN XX_SquareMeter IS NULL THEN 0 " +
							" ELSE XX_SquareMeter END METERS, AD_Org_ID ID " +
							" FROM AD_Org WHERE AD_Org_ID NOT IN (0,1000060,1000067) ";

		PreparedStatement pstmt_dist = null; 
		ResultSet rs_dist = null;
		try{
			pstmt_dist = DB.prepareStatement(sql_dist, null); 
			rs_dist = pstmt_dist.executeQuery();
			while (rs_dist.next()){
				Orgs.add(rs_dist.getInt("ID"));
				Meters = rs_dist.getBigDecimal("METERS");
				if(!Meters.equals(null)){
					SquareMeters.add(Meters);
				}
			}
		}
		catch(Exception e){
			log.saveError("ErrorSql (catch) set: ", e.getMessage());
		}
		finally {
			DB.closeResultSet(rs_dist);
			DB.closeStatement(pstmt_dist);
		}

		// Set Line's Distribution
		for(int j = 0; j < lines.size(); j++){
			OrderLine = new MOrderLine(Env.getCtx(),lines.get(j),get_TrxName());
			Quantity = OrderLine.getQtyEntered();
			Price = OrderLine.getPriceEntered();
			LineAmount = OrderLine.getLineNetAmt();
			for (int i = 0 ; i < SquareMeters.size() ; i++) {
				if(!SquareMeters.elementAt(i).equals(null)){
					Percentage = (SquareMeters.elementAt(i).
							divide(TotalMeters, 2, RoundingMode.HALF_UP)).
							multiply(new BigDecimal(100));
					PercentageCC.add(Percentage);
					Quantities = (Percentage.multiply(Quantity)).
					divide(new BigDecimal(100),0,RoundingMode.HALF_UP);
					QuantityCC.add(Quantities);
					Amounts = Quantities.multiply(Price);
					Sum = Sum.add(Amounts);
					AmountCC.add(Amounts);
				}//if
			}//for

			//
			for (int i = 0 ; i < PercentageCC.size() ; i++) {//Round up
				if (PercentageCC.elementAt(i).doubleValue() > most_value_percentage) {
					most = i;
					most_value_percentage = PercentageCC.elementAt(i).doubleValue();
				}// if
			} //for

			// There are difference between LineNetAmount and the calculated Amounts
			Difference = LineAmount.subtract(Sum); 
			for (int i = 0 ; i < QuantityCC.size() ; i++) {
				if(Difference.compareTo(new BigDecimal(0)) == -1){
					// Remove from orgs until de difference is 0
					Aux = ((Difference.abs()).divide(Price));
					if (QuantityCC.elementAt(i).compareTo(new BigDecimal(0)) == 1){//>0
						if ( QuantityCC.elementAt(i).compareTo(Aux) == -1  ) {// < cantidad del vector menor que el aux
							Aux = Aux.subtract(QuantityCC.elementAt(i));
							QuantityCC.set(i, Aux);
							AmountCC.set(i,QuantityCC.elementAt(i).multiply(Price));
							Difference = new BigDecimal(0);
						} //if 
						else {								
							QuantityCC.set(i, QuantityCC.elementAt(i).subtract(Aux));
							AmountCC.set(i,QuantityCC.elementAt(i).multiply(Price));
							Difference = new BigDecimal(0);
						}//else
						if (Aux.equals(0)) break;
					}// if
				}// if
				else if (Difference.compareTo(new BigDecimal(0)) == 1){ 
					Aux2 = Difference.abs().divide(Price);
					QuantityCC.set(most, (QuantityCC.elementAt(most).add(Aux2)).add(QuantityCC.elementAt(i)));
					AmountCC.set(most,QuantityCC.elementAt(most).multiply(Price));
					Difference = new BigDecimal(0);
				}//else
				else if(Difference.compareTo(new BigDecimal(0)) == 0){
//					System.out.println("Diferencia es 0");
				}
			}// for

			for (int i = 0 ; i < SquareMeters.size() ; i++) {
				if(!SquareMeters.elementAt(i).equals(null)){
					X_XX_ProductPercentDistrib distrib = 
						new X_XX_ProductPercentDistrib(Env.getCtx(),0,get_TrxName());
					distrib.setAD_Client_ID(OrderLine.getAD_Client_ID());
					distrib.setAD_Org_ID(OrderLine.getAD_Org_ID());
					distrib.setIsActive(true);
					distrib.setC_OrderLine_ID(lines.get(j));
					distrib.setC_Order_ID(OrderLine.getC_Order_ID());
					distrib.setXX_Org_ID(Orgs.elementAt(i));
					distrib.setXX_PercentagePerCC(PercentageCC.elementAt(i));
					distrib.setXX_QuantityPerCC(QuantityCC.elementAt(i));
					distrib.setXX_AmountPerCC(AmountCC.elementAt(i));
					distrib.save();
					commit();
				}
			}// for
			
			OrderLine.setXX_ClearedDistrib(true);
			OrderLine.setXX_DistributionType("ME");
			OrderLine.save();
			commit();
		} // para cada linea
		
		return "";
	}

}// XX_SquareMeterDist
