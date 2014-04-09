package compiere.model.importcost;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.compiere.model.GridField;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MVLOBoardingGuide;


public class FinalUnsubscribe extends SvrProcess {

	MVLOBoardingGuide bg = null;
	String name = null;
	int x = 0;
	
	@Override
	protected String doIt() throws Exception {
		/*if (name == null){
			;
		}else if (name.equals("10000042")){
			bg.setXX_DefinitiveAgent("N");
			BigDecimal monto = bg.getXX_DefinitiveAgent().negate();
		}else if (name.equals("10000043")){
			bg.setXX_DefinitiveInsurance("N");
			BigDecimal monto = bg.getXX_DefinitiveInsurance().negate();
		}else if (name.equals("10000044")){
			bg.setXX_DefinitiveInterFret("N");
			BigDecimal monto = bg.getXX_DefinitiveInterFret().negate();
		}else if (name.equals("10000045")){
			bg.setXX_DefinitiveNacInv("N");
			BigDecimal monto = bg.getXX_DefinitiveNacInv().negate();
		}else if (name.equals("10000046")){
			bg.setXX_DefinitiveNacTreas("N");
			BigDecimal monto = bg.getXX_DefinitiveNacInv().negate();
		}else if (name.equals("10000047")){
			bg.setXX_DefinitiveSeniat("N");
			BigDecimal monto = bg.getXX_DefinitiveSeniat().negate();
		}*/
		switch (x){

		case 1:
			bg.setXX_DefinitiveAgent("N");
			break;
		case 2:
			bg.setXX_DefinitiveInsurance("N");
			break;
		case 3:
			bg.setXX_DefinitiveInterFret("N");
			break;
		case 4:
			bg.setXX_DefinitiveNacInv("N");
			break;
		case 5:
			bg.setXX_DefinitiveNacTreas("N");
			break;
		case 6:
			bg.setXX_DefinitiveSeniat("N");
			break;
		default:
			break;
		}
		
		bg.save();
		return "Monto Reversado";
	}

	@Override
	protected void prepare() {
		bg = new MVLOBoardingGuide(getCtx(), getRecord_ID(), get_TrxName());
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para)	
		{
			String parametro = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (parametro.equals("XX_VLO_Definitive_List"))
				name=element.getParameter().toString();
		}
		if (name == null){
			x=0;
		}else if (name.equals("10000042")){
			x=1;
		}else if (name.equals("10000043")){
			x=2;
		}else if (name.equals("10000044")){
			x=3;
		}else if (name.equals("10000045")){
			x=4;
		}else if (name.equals("10000046")){
			x=5;
		}else if (name.equals("10000047")){
			x=6;
		}
	}
}
