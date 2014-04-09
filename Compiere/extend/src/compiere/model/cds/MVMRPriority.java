package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRPriority extends X_XX_VMR_Priority{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	
	public MVMRPriority(Ctx ctx, int XX_VMR_Priority_ID, Trx trx) {
		super(ctx, XX_VMR_Priority_ID, trx);
		// TODO Auto-generated constructor stub
	}
	public MVMRPriority(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean beforeSave (boolean newRecord)
	{	
		deleteOCPrioritiesCH();
		deleteOCPrioritiesPE();
		Integer priorityID= getXX_VMR_Priority_ID();
		Integer pos= getXX_Position();
		Integer order = getC_Order_ID();
		Integer dist = getXX_VMR_DistributionHeader_ID();
		String sql;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		if((order == null || order == 0) && (dist == null || dist == 0)){
			log.saveError("Error ", Msg.getMsg( getCtx(), "XX_PriorityNull"));
			return false;
		}else if(order != null && order != 0){
			sql = "\nSELECT O.DocumentNo " +
			   "\nFROM XX_VMR_Priority P INNER JOIN C_Order O ON (P.C_Order_ID=O.C_Order_ID) "+
			   "\nWHERE P.XX_VMR_Priority_ID != "+priorityID +" AND P.C_Order_ID = "+order;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if(rs.next()){
					log.saveError("Error", Msg.getMsg( getCtx(), "XX_ExistOC", rs.getInt(1)));
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				try {
					pstmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sql = "\nSELECT C_Order_ID " +
			   "\nFROM XX_VMR_Priority "+
			   "\nWHERE XX_Position = "+pos+
			   "\nAND C_Order_ID is not null ";
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if(rs.next()){
					log.saveError("Error", Msg.getMsg( getCtx(), "XX_PositionOC",pos));
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				try {
					pstmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else if(dist !=null && dist !=0){
			sql = "\nSELECT XX_VMR_DistributionHeader_ID " +
			   "\nFROM XX_VMR_Priority "+
			   "\nWHERE XX_VMR_Priority_ID !="+priorityID +" AND XX_VMR_DistributionHeader_ID = "+dist;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if(rs.next()){
					log.saveError("Error", Msg.getMsg( getCtx(), "XX_ExistDist",rs.getInt(1)));
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				try {
					pstmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sql = "\nSELECT XX_VMR_DistributionHeader_ID " +
			   "\nFROM XX_VMR_Priority "+
			   "\nWHERE XX_Position = "+pos+
			   "\nAND XX_VMR_DistributionHeader_ID is not null ";
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if(rs.next()){
					log.saveError("Error", Msg.getMsg( getCtx(), "XX_PositionDistrib",pos));
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				try {
					pstmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			return true;
					
	}//beforeSave

	/** Elimina de la tabla XX_VMR_PRIORITY las ordenes de compra que ya fueron chequeadas */
	private void deleteOCPrioritiesCH() {
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		PreparedStatement psDelete =null;
		
		String sql = "\nSELECT P.C_ORDER_ID " +
				"\nFROM XX_VMR_PRIORITY P INNER JOIN C_ORDER O ON (P.C_ORDER_ID = O.C_ORDER_ID)" +
				"\nWHERE XX_ORDERSTATUS != 'RE'";
		try {
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				try{
				String sqlDelete = "DELETE FROM XX_VMR_PRIORITY WHERE C_ORDER_ID = " +rs.getInt(1);
				psDelete = DB.prepareStatement(sqlDelete,null);
				psDelete.execute();
				}catch (Exception e) {
					System.out.println("Error borrando orden de compra con prioridad " +e.getMessage());
				}finally{
					psDelete.close();
				}
			}
		}catch (Exception e) {
			 System.out.println("Error borrando O/C con prioridad" +e.getMessage());
		}finally{
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/** Elimina de la tabla XX_VMR_PRIORITY las distribuciones que ya fueron chequeadas */
	private void deleteOCPrioritiesPE() {
		PreparedStatement stmt = null;
	    ResultSet rs = null;
		PreparedStatement psDelete =null;
		
		String sql = "\nSELECT P.XX_VMR_DISTRIBUTIONHEADER_ID " +
				"\nFROM XX_VMR_PRIORITY P INNER JOIN XX_VMR_ORDER PE ON (P.XX_VMR_DISTRIBUTIONHEADER_ID = PE.XX_VMR_DISTRIBUTIONHEADER_ID)" +
				"\nWHERE PE.XX_ORDERREQUESTSTATUS != 'PE' ";
		try {
			stmt = DB.prepareStatement(sql, null);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				try{
				String sqlDelete = "DELETE FROM XX_VMR_PRIORITY WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " +rs.getInt(1);
				psDelete = DB.prepareStatement(sqlDelete,null);
				psDelete.execute();
				}catch (Exception e) {
					System.out.println("Error borrando distribución con prioridad " +e.getMessage());
				}finally{
					psDelete.close();
				}
			}
		}catch (Exception e) {
			 System.out.println("Error borrando distribuciones con prioridad" +e.getMessage());
		}finally{
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	
	



}
