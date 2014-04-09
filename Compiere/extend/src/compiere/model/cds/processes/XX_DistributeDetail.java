package compiere.model.cds.processes;

import java.awt.Container;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.distribution.XX_DistributionUtilities;
import compiere.model.cds.distribution.detail.XX_BudgetDetail;
import compiere.model.cds.distribution.detail.XX_DistributionDetail;
import compiere.model.cds.distribution.detail.XX_ReplacementDetail;
import compiere.model.cds.distribution.detail.XX_SalesBudgetDetail;
import compiere.model.cds.distribution.detail.XX_SalesDetail;


/**
* Clase que invoca los procesos de distribución para un detalle
*  @author Gabrielle Huchet
*/
public class XX_DistributeDetail extends SvrProcess {
	X_XX_VMR_DistributionDetail detalle;
	int tipoDistribucion = 0;
	int mes = 0;
	int año = 0;
	boolean ventasPasadas = false;
	
	@Override
	protected String doIt() throws Exception {
		
		detalle = new X_XX_VMR_DistributionDetail(getCtx(), getRecord_ID(), null);
		tipoDistribucion = detalle.getXX_VMR_DistributionType_ID();
		//Verificar parametros
		if ((mes > 12) || (mes < 1)) {
			ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_MonthAllowed"));
			return "";
		}
		if (año <= 0) {
			ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_YearAllowed"));
			return "";
		}
		
		//La distribucion a utilizar
		XX_DistributionDetail distribucion = null;
			
		//Verificar el tipo de distribucion a utilizar y los parametros
		if (tipoDistribucion == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPESALES_ID")) {	
			if (!XX_DistributionUtilities.fechaMenorActual(mes, año)) {
				ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_LessYearMonth"));
				return ""; 
			}
			//Distribucion por ventas del producto
			distribucion = new XX_SalesDetail(getRecord_ID(), getCtx(), get_TrxName(), mes, año);
			
		} else if (tipoDistribucion == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEBUDGET_ID")) {
			if (!XX_DistributionUtilities.fechaMayorActual(mes, año)) {
				ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_LargerYearMonth"));
				return "";
			}		
			//Distribucion por presupuesto
			distribucion = new XX_BudgetDetail(getRecord_ID(), getCtx(), get_TrxName(), mes, año);
			
		} else if (tipoDistribucion == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPESALBUD_ID")) {
			if (!XX_DistributionUtilities.fechaMayorActual(mes, año)) {
				ADialog.error(1, new Container(),  Msg.translate(getCtx(), "XX_LargerYearMonth"));
				return "";
			}
			//Distribucion por ventas/presupuesto del producto
			distribucion = new XX_SalesBudgetDetail(getRecord_ID(), getCtx(), get_TrxName(), mes, año, ventasPasadas);
			
		} else if (tipoDistribucion == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEREPLAC_ID")) {
			if (!XX_DistributionUtilities.fechaMayorActual(mes, año)) { 
				ADialog.error(1, new Container(),  Msg.translate(getCtx(), "XX_LargerYearMonth"));
				return "";
			}
			//Distribucion por reposicion del producto
			distribucion = new XX_ReplacementDetail(getRecord_ID(), getCtx(), get_TrxName(), mes, año);			
		} else {
			return "";
		}
		
		//Verificada la distribucion, entonces ejecutarla
		distribucion.procesar();			
		
		return "";
	}

	@Override
	protected void prepare() {
		
		//Procesar los parametros
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("XX_Month")) {
					mes = element.getParameterAsInt();
				} else if (element.getParameterName().equals("XX_Year")) {
					año = element.getParameterAsInt();
				} else if (element.getParameterName().equals("XX_LastYearSales")) {
					ventasPasadas = element.getParameter().equals("Y");
				}
			}			
		}
		
	}
	
}
