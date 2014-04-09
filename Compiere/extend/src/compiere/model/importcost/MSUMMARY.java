package compiere.model.importcost;

import java.math.BigDecimal;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

@SuppressWarnings("serial")
public class MSUMMARY extends X_XX_VLO_SUMMARY {

	public MSUMMARY(Ctx ctx, int summary, Trx trx, int socio, int pais, int orden
			, int formaPago, BigDecimal montoCosto,  int Departamento, int WAREHOUSE, String DataType) {
		super(ctx, summary, trx);
		setC_BPartner_ID(socio);
		setC_Country_ID(pais);
		setC_Order_ID(orden);
		setC_PaymentTerm_ID(formaPago);
		setXX_VMR_Department_ID(Departamento);
		setXX_Cosant(montoCosto);
		setM_Warehouse_ID(WAREHOUSE);
		setDataType(DataType);
	}
	
}
