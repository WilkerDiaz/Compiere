package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


public class XX_CleanLongCharacteristic extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
			
		int seccion = 0;
		String nombre = "";
		String SQL = "SELECT NAME, XX_VMR_SECTION_ID, COUNT(*) FROM XX_VMR_LONGCHARACTERISTIC GROUP BY NAME, XX_VMR_SECTION_ID HAVING COUNT(*)>1"; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			while(rs.next())
			{
				nombre = rs.getString(1);
				seccion = rs.getInt(2);
				String SQL2 = "select xx_vmr_longcharacteristic_id from xx_vmr_longcharacteristic where name='"+nombre+"' and xx_vmr_section_ID="+seccion+" order by xx_vmr_longcharacteristic_id";
				PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null);
				ResultSet rs2 = pstmt2.executeQuery();
				int primero = 0;
				if(rs2.next())
				{
					primero = rs2.getInt(1);
				}
				while (rs2.next())
				{
					try {
						String sql3 = "UPDATE XX_VMR_VENDORPRODREF SET XX_VMR_LONGCHARACTERISTIC_ID="+primero+" WHERE XX_VMR_LONGCHARACTERISTIC_ID=" + rs2.getInt(1);
							DB.executeUpdate(null, sql3);
							
							String sql7 = "UPDATE m_product SET XX_VMR_LONGCHARACTERISTIC_ID="+primero+" WHERE XX_VMR_LONGCHARACTERISTIC_ID=" + rs2.getInt(1);
							DB.executeUpdate(null, sql7);

						String sql4 = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_VMR_LONGCHARACTERISTIC_ID="+primero+" WHERE XX_VMR_LONGCHARACTERISTIC_ID=" + rs2.getInt(1);
						DB.executeUpdate(null, sql4);
						
						
						String sql5 = "UPDATE I_XX_VMR_VENDORPRODREF SET XX_VMR_LONGCHARACTERISTIC_ID="+primero+" WHERE XX_VMR_LONGCHARACTERISTIC_ID=" + rs2.getInt(1);
						DB.executeUpdate(null, sql5);
						
						String sql6 = "DELETE FROM XX_VMR_LONGCHARACTERISTIC WHERE XX_VMR_LONGCHARACTERISTIC_ID=" + rs2.getInt(1);
						DB.executeUpdate(null, sql6);
					} catch (Exception e)
					{
							System.out.println("Error");
					}
					


					
					
				}
						
		    }

			
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return "";
					
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

}

