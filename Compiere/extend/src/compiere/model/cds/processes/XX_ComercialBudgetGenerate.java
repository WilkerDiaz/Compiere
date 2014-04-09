package compiere.model.cds.processes;

import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class XX_ComercialBudgetGenerate extends SvrProcess{

	@Override
	protected String doIt() throws Exception {

		//Proceso que genera la distribucion por departamento  XX_L_ProcessGenComBudget_ID
		/*MPInstance mpi4 = new MPInstance(getCtx(), Env.getCtx().getContextAsInt("#XX_L_PROCESSGENCOMBUDGET_ID"), getRecord_ID());
		mpi4.save();
		XX_GenerateComercialBudget gcb = new XX_GenerateComercialBudget();
		ProcessInfo procinfo4 = new ProcessInfo("Proceso", Env.getCtx().getContextAsInt("#XX_L_PROCESSGENCOMBUDGET_ID"));
		gcb.startProcess(getCtx(), procinfo4, null);*/
		
		//Proceso que genera el plan de surtido    
		MPInstance mpi = new MPInstance(getCtx(), Env.getCtx().getContextAsInt("#XX_L_PROCESSASSORTMENTPLAN_ID"), getRecord_ID());
		mpi.save();
		XX_AssortmentPlan assortment = new XX_AssortmentPlan();
		ProcessInfo procinfo = new ProcessInfo("Proceso Plan de Surtido", Env.getCtx().getContextAsInt("#XX_L_PROCESSASSORTMENTPLAN_ID"));
		assortment.startProcess(getCtx(), procinfo, null);
		
		//Proceso que genera el plan de compra  XX_L_ProcessPurchasePlan_ID
		MPInstance mpi1 = new MPInstance(getCtx(), Env.getCtx().getContextAsInt("#XX_L_PROCESSPURCHASEPLAN_ID"), getRecord_ID());
		mpi1.save();
		XX_PurchasePlan purchase = new XX_PurchasePlan();
		ProcessInfo procinfo1 = new ProcessInfo("Proceso Plan de Compra", Env.getCtx().getContextAsInt("#XX_L_PROCESSPURCHASEPLAN_ID"));
		purchase.startProcess(getCtx(), procinfo1, null);
		
		//Proceso del calculo de piezas XX_L_ProcessCalcuPiece_ID
		MPInstance mpi2 = new MPInstance(getCtx(), Env.getCtx().getContextAsInt("#XX_L_PROCESSCALCUPIECE_ID"), getRecord_ID());
		mpi2.save();
		XX_CalculationPiece pieces = new XX_CalculationPiece();
		ProcessInfo procinfo2 = new ProcessInfo("Proceso de Calculo de Piezas", Env.getCtx().getContextAsInt("#XX_L_PROCESSCALCUPIECE_ID"));
		pieces.startProcess(getCtx(), procinfo2, null);
		
		//Proceso de Distribucion por tienda  XX_L_ProcessStoreDistri_ID
		MPInstance mpi3 = new MPInstance(getCtx(), Env.getCtx().getContextAsInt("#XX_L_PROCESSSTOREDISTRI_ID"), getRecord_ID());
		mpi3.save();
		XX_StoreDistribution store = new XX_StoreDistribution();
		ProcessInfo procinfo3 = new ProcessInfo("Proceso de Distribucion por tienda", Env.getCtx().getContextAsInt("#XX_L_PROCESSSTOREDISTRI_ID"));
		store.startProcess(getCtx(), procinfo3, null);
		
		return "Proceso Realizado";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	
}
