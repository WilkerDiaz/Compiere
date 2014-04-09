package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;

public class XX_DisassociateOrder extends SvrProcess{
	
	@Override
	protected String doIt() throws Exception {
		MOrder orden= new MOrder(getCtx(),getRecord_ID(),get_TrxName());
		
		String SQL1= "SELECT COUNT(*) FROM C_Order WHERE ASSOCIATION_ID="+orden.getAssociation_ID();
		PreparedStatement prst1 = DB.prepareStatement(SQL1,null);
		ResultSet rs1 = prst1.executeQuery();
		if(rs1.next()){
			int max_value=rs1.getInt("COUNT(*)");
			if(max_value==2){ //si esta orden solo esta asociada a una sola... se borran los ID's de las dos..
				String SQL2= "SELECT C_Order_Id FROM C_Order WHERE ASSOCIATION_ID="+orden.getAssociation_ID()+" AND C_Order_Id<>"+orden.getC_Order_ID();
				PreparedStatement prst2 = DB.prepareStatement(SQL2,null);
				ResultSet rs2 = prst2.executeQuery();
				if(rs2.next()){
						int id_assoc=rs2.getInt("C_Order_Id");
						MOrder orden_assoc= new MOrder(getCtx(),id_assoc,get_TrxName());
						orden_assoc.setXX_IsAssociated("N");
						orden.setXX_IsAssociated("N");
						orden.setAssociation_ID(0);
						orden_assoc.setAssociation_ID(0);
						orden.save();
						orden_assoc.save();
					}
			}else if (max_value>2){
				orden.setAssociation_ID(0);
				orden.setXX_IsAssociated("N");
				orden.save();
			}else{
				rs1.close();
				prst1.close();
				return "Error: La Orden de Compra que intenta desasociar no está previamente asociada.";
			}
				
		}
		rs1.close();
		prst1.close();
		
		return null;
	}

	@Override
	protected void prepare() {
			
	}

}
