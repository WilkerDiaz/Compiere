package compiere.model.promociones;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVMRConditionPromotion extends X_XX_VMR_ConditionPromotion {
	
	private String columnName;
	private String prefijo;
	
	private static final long serialVersionUID = 1L;
	
	public MVMRConditionPromotion(Ctx ctx, int XX_VMR_ConditionPromotion_ID,
			Trx trx) {
		super(ctx, XX_VMR_ConditionPromotion_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MVMRConditionPromotion(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getPrefijo() {
		return prefijo;
	}

	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	
	

	
	
	

}
