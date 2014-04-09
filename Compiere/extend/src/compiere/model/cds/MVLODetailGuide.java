package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVLODetailGuide extends X_XX_VLO_DetailGuide {

	public MVLODetailGuide(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	public MVLODetailGuide(Ctx ctx, int XX_VLO_DetailGuide_ID, Trx trx) {
		super(ctx, XX_VLO_DetailGuide_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MVLODetailGuide (Ctx ctx, int detail, int guia, Trx trx )
    {
        super (ctx, detail, trx);
        super.set_ValueNoCheck("XX_VLO_BoardingGuide_ID", guia);

    } 
	
	protected boolean beforeSave (boolean newRecord)
	{
		return true;
	}

}
