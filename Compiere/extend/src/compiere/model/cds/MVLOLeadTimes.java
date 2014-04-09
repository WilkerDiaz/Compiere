package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVLOLeadTimes extends X_XX_VLO_LeadTimes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MVLOLeadTimes(Ctx ctx, int XX_VLO_LeadTimes_ID, Trx trxName) {
		super(ctx, XX_VLO_LeadTimes_ID, trxName);
	}
	
	public MVLOLeadTimes(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{

		// Que no se pueda guardar lead time para un periodo desactivado
		String SQL2 = "Select * from XX_VLO_DateLeadTime where xx_datefrom='"+getXX_DATEH()+"' and isactive='N'";
			
		PreparedStatement ps2 = DB.prepareStatement(SQL2, null);
		ResultSet rs2 = null;
		
		try {
			
			rs2 = ps2.executeQuery();

			if(rs2.next()){
				ADialog.error(1, new Container(), "No se pueden modificar leadtimes de periodos anteriores");						
				return false;
			}

		} catch (Exception e) {
			System.out.println("Error " + e);
		} finally {
			DB.closeStatement(ps2);
			DB.closeResultSet(rs2);
		}
		
		// Que no se puedan repetir los lead times
		String SQL = "Select * from xx_vlo_leadtimes where c_bpartner_id="+getC_BPartner_ID() + " and c_country_id="+getC_Country_ID() + 
			" and xx_vlo_arrivalport_id="+getXX_VLO_ArrivalPort_ID() + " and XX_DATEH='"+getXX_DATEH()+"'";

				
				PreparedStatement ps = DB.prepareStatement(SQL, null);
				ResultSet rs = null;
	
				try {
					
					rs = ps.executeQuery();

					if(rs.next()){
						ADialog.error(1, new Container(), "Ya existe un lead time cargado para este proveedor, pais y puerto");						
						return false;
					}
	
				} catch (Exception e) {
					System.out.println("Error " + e);
				} finally {
					DB.closeStatement(ps);
					DB.closeResultSet(rs);
				}


		setXX_IMPORTSLOGISTICSTIMETLI(
				getXX_TRANSITTIMETT().add(getXX_INTERNACARRIVALTIMETEI())
				.add(getXX_NACARRIVALTIMETEN()).add(getXX_NATIONALIZATIONTIMETNAC())
				.add(getXX_TIMEREGISTCELLATIONTRC())
		);
		
		return true;
	}

}
