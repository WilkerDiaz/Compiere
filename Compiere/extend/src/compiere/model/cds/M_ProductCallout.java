package compiere.model.cds;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class M_ProductCallout extends CalloutEngine{
	/**
	 *  Present the attribute set according the department, line and section codes selected.
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	
	public String XX_SectionCode_ID (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer section1 = (Integer) mField.getValue();
		
		GridField department = mTab.getField("XX_DepartmentCode_ID");
		GridField line = mTab.getField("XX_LineCode_ID");
		GridField section = mTab.getField("XX_SectionCode_ID");
		
		if(section1!=null){ 

			try 
			{
				String SQL = "SELECT M_AttributeSet_ID FROM M_Product "
					+"WHERE M_AttributeSet_ID IN (SELECT M_AttributeSet_ID FROM XX_VMR_DynamicCharact " 
					+"WHERE XX_DepartmentCode_ID = "+(Integer)department.getValue()+" " 
					+"AND XX_LineCode_ID = "+(Integer)line.getValue()+" " 
					+"AND XX_SectionCode_ID = "+(Integer)section.getValue()+ ")";
				
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				ResultSet rs = pstmt.executeQuery();
				System.out.println("Paso el select anidado");
			
				while(rs.next())
				{	
					System.out.println("Entro al while");
					mTab.setValue("M_AttributeSet_ID",rs.getString("M_AttributeSet_ID"));
				}
			
				rs.close();
				pstmt.close();
			
			}//try
			
			catch(Exception e)
			{	
				return e.getMessage();
			}//catch
			
		}//if
		
		return "";
	}//section code id
}//Fin M_ProductCallout
