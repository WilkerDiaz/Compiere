package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;

public class XX_AssociatePO extends SvrProcess{
	
	private int documentno_asoc;

	@Override
	protected String doIt() throws Exception {
		MOrder orden= new MOrder(getCtx(),getRecord_ID(),get_TrxName());
		MOrder orden_assoc= new MOrder(getCtx(),documentno_asoc,get_TrxName());

		if (orden_assoc.getXX_IsAssociated().equals("Y")) {
			orden.setAssociation_ID(orden_assoc.getAssociation_ID());
			orden.setXX_IsAssociated("Y");
			orden.save();						
		} else {
			orden_assoc.setXX_IsAssociated("Y");
			orden.setXX_IsAssociated("Y");
			String SQL1= "SELECT MAX(ASSOCIATION_ID) FROM C_Order";
			PreparedStatement prst1 = DB.prepareStatement(SQL1,null);
			ResultSet rs1 = prst1.executeQuery();
			if(rs1.next()){
				int max_value = rs1.getInt(1);
				max_value++;
				orden.setAssociation_ID(max_value);
				orden_assoc.setAssociation_ID(max_value);
				orden.save();
				orden_assoc.save();
			}
			rs1.close();
			prst1.close();			
		}
		return null;
	}

	@Override
	protected void prepare() {
				
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("C_Order_ID")) {				
				documentno_asoc = (Integer) element.getParameterAsInt();		
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}		
	}

}
