package compiere.model.cds.processes;

import java.awt.Container;
import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.distribution.*;



/**
* Clase que invoca los procesos de distribucion
*  @author    JPino
*  @author    GHuchet
*  @version    
*/
public class XX_DistributeAll extends SvrProcess{

	int distributionType = 0;
	int month = 0;
	int year = 0;
	boolean previousSales = false;
	
	@Override
	protected String doIt() throws Exception {
		
		//Verificar parametros
		if ((month > 12) || (month < 1)) {
			ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_MonthAllowed"));
			return "";
		}
		if (year <= 0) {
			ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_YearAllowed"));
			return "";
		}
		
		//La distribucion a utilizar
		XX_Distribution distribution = null;
			
		//Verificar el tipo de distribucion a utilizar y los parametros
		if (distributionType == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPESALES_ID")) {	
			if (!XX_DistributionUtilities.fechaMenorActual(month, year)) {
				ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_LessYearMonth"));
				return ""; 
			}
			//Distribucion por ventas del producto
			distribution = new XX_Sales(getRecord_ID(), getCtx(), get_TrxName(), month, year);
			
		} else if (distributionType == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEBUDGET_ID")) {
			if (!XX_DistributionUtilities.fechaMayorActual(month, year)) {
				ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_LargerYearMonth"));
				return "";
			}		
			//Distribucion por presupuesto
			distribution = new XX_Budget(getRecord_ID(), getCtx(), get_TrxName(), month, year);
			
		} else if (distributionType == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPESALBUD_ID")) {
			if (!XX_DistributionUtilities.fechaMayorActual(month, year)) {
				ADialog.error(1, new Container(),  Msg.translate(getCtx(), "XX_LargerYearMonth"));
				return "";
			}
			//Distribucion por ventas/presupuesto del producto
			distribution = new XX_SalesBudget(getRecord_ID(), getCtx(), get_TrxName(), month, year, previousSales);
			
		} else if (distributionType == Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEREPLAC_ID")) {
			if (!XX_DistributionUtilities.fechaMayorActual(month, year)) { 
				ADialog.error(1, new Container(),  Msg.translate(getCtx(), "XX_LargerYearMonth"));
				return "";
			}
			//Distribucion por reposicion del producto
			distribution = new XX_Replacement(getRecord_ID(), getCtx(), get_TrxName(), month, year);			
		} else {
			return "";
		}
		MVMRDistributionHeader dist = new MVMRDistributionHeader(getCtx(), getRecord_ID(), get_Trx());
		dist.setXX_DistributionTypeApplied(distributionType);
		dist.setXX_Month(month);
		dist.setXX_Year(year);
		dist.setXX_UseLastYearSales(previousSales);
		dist.save();
		//Verificada la distribucion, entonces ejecutarla
		distribution.procesar();			
		
		return "";
	}

	@Override
	protected void prepare() {
		
		//Procesar los parametros
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("XX_VMR_DistributionType_ID") ) {
					distributionType = element.getParameterAsInt();
				} else if (element.getParameterName().equals("XX_Month")) {
					month = element.getParameterAsInt();
				} else if (element.getParameterName().equals("XX_Year")) {
					year = element.getParameterAsInt();
				} else if (element.getParameterName().equals("XX_LastYearSales")) {
					previousSales = element.getParameter().equals("Y");
				}
			}			
		}
		
	}
	
	
}