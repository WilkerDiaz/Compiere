package compiere.model.cds.processes;

import java.math.BigDecimal;

import org.compiere.model.MConversionRate;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MOrder;


public class XX_MarginUpdate extends SvrProcess {

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		MOrder orden = new MOrder(getCtx(),getRecord_ID(), null);
		
		// necesitamos el factor de reposicion y el factor estimado
		BigDecimal factorReposicion = orden.getXX_ReplacementFactor();
		BigDecimal factorEstimado = orden.getXX_EstimatedFactor();
		
		String SQL_Update = "";
		
		if (orden.getXX_OrderType().equals("Importada"))
		{
			// Actualizamos el costo mostrado en el detalle
			SQL_Update = "update XX_VMR_PO_LineRefProv set priceactual=(" + factorEstimado + "*xx_unitpurchaseprice) where c_order_id="+orden.get_ID();
			DB.executeUpdate(get_Trx(),SQL_Update);
	
			// Actualizamos el margen en el detalle
			SQL_Update = "\nUPDATE XX_VMR_PO_LINEREFPROV REFE SET XX_MARGIN=" +
					"\n((xx_saleprice-(("+ factorReposicion +" * xx_unitpurchaseprice)/ " +
					"\n(SELECT U.XX_UNITCONVERSION										 " +
					"\nFROM XX_VMR_UNITCONVERSION U " +
					"\nWHERE U.XX_VMR_UNITCONVERSION_ID= REFE.XX_VMR_UNITCONVERSION_ID)* " +
					"\n(SELECT U.XX_UNITCONVERSION								 " +
					"\nFROM XX_VMR_UNITCONVERSION U " +
					"\nWHERE U.XX_VMR_UNITCONVERSION_ID= REFE.XX_PIECESBYSALE_ID)))/xx_saleprice)*100 " +
					"\nWHERE REFE.C_Order_ID="+orden.get_ID();

			DB.executeUpdate(get_Trx(),SQL_Update);
		}
		
		//Actualiza lineNetAmt
		String sql_LineNetAmt = "UPDATE XX_VMR_PO_LineRefProv SET LINENETAMT = (XX_CostWithDiscounts*QTY) WHERE C_Order_ID = " + orden.getC_Order_ID();

		DB.executeUpdate(get_Trx(), sql_LineNetAmt);
					
		//Valores de Cabecera			
		String sql_header = "";

		sql_header = "UPDATE C_Order po"											//////////////////////
		+ " SET (XX_ProductQuantity, TotalPVP, XX_TotalPVPPlusTax, TotalLines, XX_TotalCostBs, XX_EstimatedMargin ) ="											//////////////////////
			+ "(SELECT COALESCE(SUM(XX_LineQty),0), COALESCE(SUM(XX_LinePVPAmount),0), COALESCE(SUM(XX_LinePlusTaxAmount),0), COALESCE(SUM(LineNetAmt),0), COALESCE(SUM(PriceActual*qty),0), round(avg(xx_margin),2)  FROM XX_VMR_PO_LINEREFPROV line "
			+ "WHERE po.C_Order_ID=line.C_Order_ID ) "
		+ "WHERE po.C_Order_ID=" + orden.getC_Order_ID();

		DB.executeUpdate(get_Trx(), sql_header);

		String sql_cost = "UPDATE C_Order po"
			+ " SET XX_TotalCostBs = TotalLines"
			+ " WHERE XX_TotalCostBs<1 and po.C_Order_ID=" + orden.getC_Order_ID();
		
		DB.executeUpdate(get_Trx(), sql_cost);
		
		
		// Actualizamos el margen en la cabecera
		SQL_Update = "update C_Order set xx_estimatedmargin=(select coalesce(round(sum(qty*xx_margin)/(select XX_ProductQuantity from c_order where issotrx='N' and xx_productQuantity>0 and c_order_id=l.c_order_id),2),0) as XX_RealMargin from xx_vmr_po_linerefprov l where  c_order_id="+getRecord_ID()+" group by c_order_id) where c_order_id="+orden.get_ID();
		DB.executeUpdate(get_Trx(),SQL_Update);
		
		return "Valores Actualizados correctamente (margenes y costos)";
	}

}
