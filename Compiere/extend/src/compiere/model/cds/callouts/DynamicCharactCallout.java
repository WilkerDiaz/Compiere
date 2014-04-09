package compiere.model.cds.callouts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class DynamicCharactCallout extends CalloutEngine{
	/**
	 *  Change the name and description according the attribute set name
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	
	public String M_AttributeSet_ID (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer attribute = (Integer) mField.getValue();
		//GridField attributeSet = mTab.getField("M_AttributeSet_ID");
		if(attribute!=null){ 
			try 
			{
				String SQL = "SELECT Name FROM M_AttributeSet "
					+"WHERE M_AttributeSet_ID = "+ attribute; 
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next())
				{	
					/* Set Name, description and search key */
					mTab.setValue("Name",rs.getString("Name"));
				}
				rs.close();
				pstmt.close();
			}
			catch(Exception e)
			{	
				return e.getMessage();
			}
		}//if
		return "";
	}//attribute code id
	
	public String XX_SectionCode_ID (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer sect = (Integer) mField.getValue();
		GridField Dep = mTab.getField("XX_VMR_Department_ID");
		GridField Lin = mTab.getField("XX_VMR_Line_ID");
		GridField Sec = mTab.getField("XX_VMR_Section_ID");
		String nameD = new String();
		String nameL = new String();
		String nameS = new String();

		if(sect!=null){ 
			/* Select department code */
			try 
			{
				String SQL = "SELECT Value FROM XX_VMR_DEPARTMENT "
					+"WHERE XX_VMR_DEPARTMENT_ID = "+(Integer)Dep.getValue(); 
				PreparedStatement pstmt1 = DB. prepareStatement (SQL.toString(), null); 
				ResultSet rs1 = pstmt1.executeQuery();
				if(rs1.next()){
					nameD = rs1.getString("Value");
					rs1.close();
					pstmt1.close();
				}
			}
			catch(Exception e)
			{	
				return e.getMessage();
			}	
			
			/* Select line code */
			try 
			{
				String SQL1 = "SELECT Value FROM XX_VMR_LINE "
					+"WHERE XX_VMR_LINE_ID = "+(Integer)Lin.getValue(); 
				PreparedStatement pstmt2 = DB. prepareStatement (SQL1.toString(), null); 
				ResultSet rs2 = pstmt2.executeQuery();
				if(rs2.next()){
					nameL = rs2.getString("Value");
					rs2.close();
					pstmt2.close();
				}
			}
			catch(Exception e)
			{	
				return e.getMessage();
			}
			
			/* Select section code */
			try 
			{
				String SQL1 = "SELECT Value FROM XX_VMR_SECTION "
					+"WHERE XX_VMR_SECTION_ID = "+(Integer)Sec.getValue(); 
				PreparedStatement pstmt2 = DB. prepareStatement (SQL1.toString(), null); 
				ResultSet rs2 = pstmt2.executeQuery();
				if(rs2.next()){
					nameS = rs2.getString("Value");
					rs2.close();
					pstmt2.close();
				}
			}
			catch(Exception e)
			{	
				return e.getMessage();
			}
				
			/* Set value according de, line and section codes */
			mTab.setValue("Value",nameD+nameL+nameS);

		}//if
		return "";
	}//attribute code id
	
	
}//Fin Dynamic
