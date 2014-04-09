package compiere.model.cds.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;

public class VMR_BrandCallout extends CalloutEngine {

	
	public String ShowBrand(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		String Text="";
		boolean aux = true;
		try
		{
			if ((mField.getValue()!=null) && (!(mField.getValue().equals(""))))
			{
				String sql = "select name from XX_VMR_BRAND where upper(name) like upper('%"+mField.getValue()+"%')";
				PreparedStatement priceRulePstmt = DB.prepareStatement(sql, null);
				ResultSet rs = priceRulePstmt.executeQuery();
				
				  while (rs.next())
				   {
					//   if (aux)
					//	   Text="          Lista de Posibles Marcas Ya Creadas";
					  
					   Text = Text+" \n "+rs.getString("name");
					//   aux = false;
				   }
				  rs.close();
				  priceRulePstmt.close();
				
				 mTab.setValue("Comments",Text);	
			}
			else
				 mTab.setValue("Comments","");
		}catch (Exception e) {
			return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
		}	
		return "";
	}
}
