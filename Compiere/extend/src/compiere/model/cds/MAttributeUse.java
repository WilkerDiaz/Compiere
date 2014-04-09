package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

/**
 *	MVMRDynamicCharact
 *  @author Cadena de Suministros
 */

public class MAttributeUse extends org.compiere.model.MAttributeUse{

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	public MAttributeUse(Ctx ctx, int M_AttributeUse_ID, Trx trx) {
		super(ctx, M_AttributeUse_ID, trx);
		// TODO Auto-generated constructor stub
	}

	public MAttributeUse(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Before Save related with the number of attribute use associated with 
	 *  an attribute set. Max number: 3 
	 *  Also denied modifications on an attribute set when it's used in more
	 *  than one dynamic characteristic 
	 *	@param newRecord new
	 *	@return true/false
	 */
	@Override
	protected boolean beforeSave (boolean newRecord) 
	{
			int count = 0;
			int cont = 0;
			
			String sql = "SELECT COUNT(M_AttributeSet_ID) AS Cantidad "
				+ "FROM M_AttributeUse "
				+ "WHERE M_AttributeSet_ID= "+getM_AttributeSet_ID();

			String sql1 = "SELECT COUNT(M_ATTRIBUTESET_ID) AS Cantidad "
				+ "FROM XX_VMR_DYNAMICCHARACT "
				+ "WHERE M_ATTRIBUTESET_ID= "+getM_AttributeSet_ID();
			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
			
				if(rs.next()){
					count = rs.getInt("Cantidad");
					
				}//rs.next
				rs.close();
				pstmt.close();

				PreparedStatement pstmt1 = DB.prepareStatement(sql1, null);
				ResultSet rs1 = pstmt1.executeQuery();
			
				if(rs1.next()){
					cont = rs1.getInt("Cantidad");
				}//rs.next
				rs1.close();
				pstmt1.close();

				if(count < 3 && cont <= 1){
					return true;
				}//if
				/* Related with the number of attributes associated with an attribute set. 
				 * Max number: 3 */
				else if (count >= 3){
					log.saveError("Error", Msg.getMsg(getCtx(), "Can't save more than 3 attributes"));
					return false;
				}//else >3
				/* denied modifications on an attribute set when it's used in more
				 * than one dynamic characteristic */
				else{
					log.saveError("Error", Msg.getMsg(getCtx(), "Can't modify Attribute Set. It's used in more than one Dynamic Characteristic"));
					return false;
				}//else
			}//try
			catch (SQLException ex)
			{
				System.out.println("catch");
				log.log(Level.SEVERE, sql, ex);
				return false;
			}//catch
	}//before save
	
	
	/**
	 * 	Before Delete denied delete one attribute use when it's related to an 
	 *  attribute set that's used in more than one dynamic characteristic 
	 *	@param newRecord new
	 *	@return true/false
	 */
	@Override
	protected boolean beforeDelete() 
	{
		int borrar = 0;
		String sql = "SELECT COUNT(M_ATTRIBUTESET_ID) AS Cantidad "
			+ "FROM XX_VMR_DYNAMICCHARACT "
			+ "WHERE M_ATTRIBUTESET_ID= "+getM_AttributeSet_ID();
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
		
			if(rs.next()){
				borrar = rs.getInt("Cantidad");
			}//rs.next
			rs.close();
			pstmt.close();

			if(borrar > 1){
				log.saveError("Error", Msg.getMsg(getCtx(), "Can't modify Attribute Set. It's used in more than one Dynamic Characteristic"));
				return false;
			}//if
			else{
				return true;
			}

		}//try
		catch (SQLException ex)
		{
			System.out.println("catch");
			log.log(Level.SEVERE, sql, ex);
			return false;
		}//catch
	}//before delete
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		String nameA = new String();
		String nameB = new String();
		int no = 0;
		
		String sql1 = "SELECT Name FROM M_Attribute WHERE M_Attribute_ID= "+getM_Attribute_ID();
		try
		{
			PreparedStatement pstmt1 = DB.prepareStatement(sql1, null);
			ResultSet rs1 = pstmt1.executeQuery();
	
			if(rs1.next()){
				nameA = rs1.getString("Name");
				
			}//rs3.next
			rs1.close();
			pstmt1.close();
		
		}//try
		catch (SQLException ex)
		{
			System.out.println("catch");
			log.log(Level.SEVERE, sql1, ex);
			return false;
		}//catch
		
		String sql2 = "SELECT Name FROM M_AttributeSet WHERE M_AttributeSet_ID= "+getM_AttributeSet_ID();
		try
		{
			PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
			ResultSet rs2 = pstmt2.executeQuery();
	
			if(rs2.next()){
				nameB = rs2.getString("Name");
			}//rs3.next
			rs2.close();
			pstmt2.close();
		
		}//try
		catch (SQLException ex)
		{
			System.out.println("catch");
			log.log(Level.SEVERE, sql2, ex);
			return false;
		}//catch
		
		if(nameB.equals("nuevo")){
			String sql3 = "UPDATE M_AttributeSet SET Name='"+nameA+"' " +
			"WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID();
			no = DB.executeUpdate(get_Trx(), sql3);
			if (no != 0)
				log.fine("afterSave - Reset Instance Attribute Set");
		}
		else{
			String sql4 = "UPDATE M_AttributeSet SET Name='"+nameB+" "+nameA+"' " +
			"WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID();
			no = DB.executeUpdate(get_Trx(), sql4);
			if (no != 0)
				log.fine("afterSave - Reset Instance Attribute Set");
		}
	
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		String nameA = new String();
		String nameB = new String();
		String aux = new String();
		String aux2 = new String();
		int no = 0;
		
		String sql1 = "SELECT Name FROM M_Attribute WHERE M_Attribute_ID= "+getM_Attribute_ID();
		try
		{
			PreparedStatement pstmt1 = DB.prepareStatement(sql1, null);
			ResultSet rs1 = pstmt1.executeQuery();
			if(rs1.next()){
				nameA = rs1.getString("Name");
			}//rs1.next
			rs1.close();
			pstmt1.close();
		}//try
		catch (SQLException ex)
		{
			System.out.println("catch");
			log.log(Level.SEVERE, sql1, ex);
			return false;
		}//catch
		
		String sql2 = "SELECT Name FROM M_AttributeSet WHERE M_AttributeSet_ID= "+getM_AttributeSet_ID();
		try
		{
			PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
			ResultSet rs2 = pstmt2.executeQuery();
	
			if(rs2.next()){
				nameB = rs2.getString("Name");
			}//rs2.next
			rs2.close();
			pstmt2.close();
		}//try
		catch (SQLException ex)
		{
			System.out.println("catch");
			log.log(Level.SEVERE, sql2, ex);
			return false;
		}//catch

		int res = nameB.indexOf(nameA);
		aux = nameB.substring(0,res);

		if(res+nameA.length()==nameB.length()){
			aux2 = nameB.substring(res+nameA.length());
		}
		else{
			aux2 = nameB.substring(res+nameA.length()+1);
		}

		aux = aux+aux2;
		
		System.out.println(aux+":bla");
		
		if(aux.length() == 0){
			String sql3 = "UPDATE M_AttributeSet SET Name='nuevo' " +
			"WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID();
			no = DB.executeUpdate(get_Trx(), sql3);
			if (no != 0)
				log.fine("afterDelete - Reset Instance Attribute Set");
		}
		else{
			String sql3 = "UPDATE M_AttributeSet SET Name='"+aux+"' " +
			"WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID();
			no = DB.executeUpdate(get_Trx(), sql3);
			if (no != 0)
				log.fine("afterDelete - Reset Instance Attribute Set");
			}
///
	
		return success;
	}	//	afterDelete
	
}//	MAttributeUse