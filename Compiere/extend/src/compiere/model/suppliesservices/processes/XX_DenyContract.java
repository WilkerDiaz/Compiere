package compiere.model.suppliesservices.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.suppliesservices.X_Ref_XX_Status_LV;
import compiere.model.suppliesservices.X_XX_Contract;

public class XX_DenyContract extends SvrProcess{
	/** Purchase of Supplies and Services 
	 * Maria Vintimilla Funcion 33
	 * Deny a contract.**/
	Integer Legal_ID = 0;
	Integer Advisor_ID = 0;
	Integer Role = 0;
	Integer Sales_ID = 0;
	Integer Merchandising_ID = 0;
	Integer Personal_ID = 0;
	Integer Logistic_ID = 0;
	Integer System_ID = 0;
	Integer Control_ID = 0;
	Integer Merchan_ID = 0;
	@Override
	protected String doIt() throws Exception {
		try{
			Role = Env.getCtx().getContextAsInt("#AD_Role_ID"); 
			X_XX_Contract contrato = 
				new X_XX_Contract(Env.getCtx(),getRecord_ID(),null);
			Advisor_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGALADVISOR_ID"));
			Sales_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESALES_ID"));
			Merchandising_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHANDISING_ID"));
			Personal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEPERSONAL_ID"));
			Logistic_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELOGISTIC_ID"));
			System_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESYSTEM_ID"));
			Control_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLECONTROL_ID"));
			Merchan_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHAN_ID"));
			Legal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGAL_ID"));
			
			if(Role.equals(Legal_ID) || Role.equals(Sales_ID) || Role.equals(Merchandising_ID) || 
					Role.equals(Personal_ID) || Role.equals(Logistic_ID) || 
					Role.equals(System_ID) || Role.equals(Control_ID) || 
					Role.equals(Merchan_ID)){//Denied by Roles
				if(contrato.getXX_Status().equals(X_Ref_XX_Status_LV.REVIEW.getValue())){
					contrato.setXX_Status("E");
					contrato.save();
				}
				else{
					log.saveError("Error", Msg.translate(Env.getCtx(), "XX_DenyContract"));
				}
			}// If
			else { // Denied by Others
				log.saveError("Error", Msg.translate(Env.getCtx(), "XX_DenyOthers"));
			}
			return "";
		}//try
		catch(Exception e){
			return e.getMessage();
		}
	}// fin doIt

	@Override
	protected void prepare() {
		
	}// prepare
	
}// Fin XX_DenyCntract
