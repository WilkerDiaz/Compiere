package compiere.model.cds;
import java.text.DecimalFormat;
import java.util.logging.Level;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class PrintBarCode extends SvrProcess {

	int printer = -1;
	X_XX_VMR_PriceConsecutive consecutive = null;
	int label_qty = 0;
	int warehouse_id = 0; 
	PrintService[] services = null;
	
	@Override
	protected String doIt() throws Exception {

		try {  
			if (label_qty <= 0) {
				log.log(Level.WARNING, "QtyEnteredZero");
				return Msg.translate(getCtx(), "QtyEnteredZero");
			}			
			PrintService psZebra = services[printer];
			MProduct producto = new MProduct(getCtx(), getRecord_ID(), get_TrxName());
			MAttributeSetInstance atributos = new MAttributeSetInstance(getCtx(), 
					producto.getM_AttributeSetInstance_ID(), null);
			DecimalFormat formato = new DecimalFormat("000");

			boolean engomada = true;
			if (producto.getXX_VMR_TypeLabel_ID() == Env.getCtx().getContextAsInt("#XX_L_TYPELABELCOLGANTE_ID")) {
				engomada = false;				
			}
			Utilities.print_labels(psZebra, new KeyNamePair(producto.get_ID(), producto.getName()), 
					new KeyNamePair(atributos.get_ID(), atributos.getDescription()), formato.format(consecutive.getXX_PriceConsecutive())
					, null, label_qty, warehouse_id, engomada);
		}
		catch (Exception e)
		{   
			e.printStackTrace();  
		}
		return null;
	}

	@Override
	protected void prepare() {
		
		services = PrintServiceLookup.lookupPrintServices(null, null);
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) {
				
			} else if (name.equals("Printer Name")) {
				for(int i=0; i<services.length; i++){
					if(services[i].getName().equals(element.getParameter())){
						printer = i;
						break;
					}
				}				
			} else if (name.equals("XX_PriceConsecutive")) {
				consecutive = new X_XX_VMR_PriceConsecutive(getCtx(), element.getParameterAsInt(), null);				
			} else if (name.equals("Label Quantity")) {
				label_qty = element.getParameterAsInt();			
			} else if (name.equals("M_Warehouse_ID")) {
				warehouse_id = element.getParameterAsInt();			
			} 
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
	}

}
