package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.cds.MOrder;
import compiere.model.cds.X_Ref_XX_OrderStatus;

/** Modifica el valor de la Tasa de Cambio de una O/C
 * @author Gabrielle Huchet
 */

public class XX_ChangeRealExchangeRate extends SvrProcess {

	BigDecimal newRate;
	@Override
	protected void prepare() {
		//Procesar los parametros
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("NewRealExchangeRate") ) {
					newRate = (BigDecimal)element.getParameter();
				} 
			}			
		}
	}

	@Override
	protected String doIt() throws Exception {
		MOrder order = new MOrder(getCtx(),getRecord_ID(), get_Trx());
		if (!order.getXX_OrderStatus().equalsIgnoreCase("AN") && !order.getXX_OrderStatus().equalsIgnoreCase("AP")
				&& !order.getXX_OrderStatus().equalsIgnoreCase("CH") && !order.getXX_OrderStatus().equalsIgnoreCase("RE"))	
		{
			 if (newRate.compareTo(new BigDecimal(0))>0 &&
					(!order.getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.APROBADA.getValue()) &&
					!order.getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.ANULADA.getValue()) && 
					!order.getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.CHEQUEADA.getValue()) &&
					!order.getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.RECIBIDA.getValue()) )){
				order.set_Value("XX_ChangeRealExchangeRate", "Y");
				order.setXX_RealExchangeRate(newRate);
				order.save();
			}else if (newRate.compareTo(new BigDecimal(0))<=0)
				ADialog.error(1, new Container(), "El Tipo de Cambio debe ser mayor a 0");
			else 
				ADialog.error(1, new Container(), "La O/C NO debe estar en estado APROBADO, RECIBIDO, CHEQUEADO O ANULADO para modificar el Tipo de Cambio");
				
				return "";
		} else
		{
			ADialog.error(1, new Container(), "La O/C NO debe estar en estado APROBADO, RECIBIDO, CHEQUEADO O ANULADO para modificar el Tipo de Cambio");
			
			return "";
		}

	}

}
