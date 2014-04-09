package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.model.MCountry;
import org.compiere.model.MRegion;
import org.compiere.model.X_C_City;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MLocation extends org.compiere.model.MLocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MLocation(Ctx ctx, int C_Country_ID, String city,
			Trx trx) {
		//super(ctx, C_Country_ID, C_Region_ID, city, trxName);
		this(ctx, 0, trx);
		if (C_Country_ID != 0)
			setC_Country_ID(C_Country_ID);
		//if (C_Region_ID != 0)
		//	setC_Region_ID(C_Region_ID);
		//System.out.println("city: "+city);
		X_C_City aux = new X_C_City(getCtx(), new Integer (city), get_Trx());
		
		setCity(aux.getName());
		//System.out.println("city2: "+get_Value("C_City_ID"));
		// TODO Auto-generated constructor stub
	}
	
	public MLocation(Ctx ctx, int C_Country_ID, int city,
			Trx trx) {

		this(ctx, 0, trx);
		if (C_Country_ID != 0)
			setC_Country_ID(C_Country_ID);

		X_C_City aux = new X_C_City(getCtx(), city, get_Trx());
		
		setCity(aux.getName());
		
	}

	public MLocation(Ctx ctx, int C_Location_ID, Trx trx) {
		super(ctx, C_Location_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MLocation(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	public MLocation(MCountry country, MRegion region) {
		super(country, region);
		// TODO Auto-generated constructor stub
	}
	
}