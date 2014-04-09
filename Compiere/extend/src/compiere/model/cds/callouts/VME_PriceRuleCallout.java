package compiere.model.cds.callouts;


import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class VME_PriceRuleCallout extends CalloutEngine {
		
	Integer cero = new Integer(0);
	Integer max = Integer.MAX_VALUE;	
	public String lowRank (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
		{			
			try 
			{
				GridField campoHighRank = 	mTab.getField("XX_HIGHRANK");
				Integer higtRank = (Integer) campoHighRank.getValue(); 
				Integer lowRank = (Integer)mField.getValue();
				
				
				if( (lowRank.compareTo(cero) != 0) )// && (higtRank.compareTo(cero) != 0) )
				{					
					String SQL = "select XX_LOWRANK,XX_HIGHRANK from XX_VME_PRICERULE order by xx_lowrank  asc";
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					Integer valor=null;	
					boolean flag = false;
					while(rs.next())
					{	
						if ( lowRank.intValue() >= rs.getInt("XX_LOWRANK") && lowRank.intValue() <= rs.getInt("XX_HIGHRANK") )
						{
							System.out.println("ENTRO");
							//mTab.setValue("XX_LOWRANK", rs.getInt("XX_HIGHRANK")+1);
							mTab.setValue("XX_LOWRANK",0);
							flag = true;
							//return "Valor recomendado low, "+valor;
						}
						System.out.println("BANDAMAYOR "+rs.getInt("XX_HIGHRANK"));
						valor = rs.getInt("XX_HIGHRANK");
					} 
					rs.close();
					pstmt.close();
					System.out.println("Valor " +valor);
					if(flag == true)
					{
						if(valor.equals(max))
						{
							return"El maximo limite fue usado";
						}
						valor = valor +1;
						return "Valor recomendado low, "+valor;
					}
					else
					{
						return"";
					}
				}	
				
					return"";
			}
			catch(Exception e)
			{	
				return "ERROR";
			}
		}
	
	public String highRank (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		try 
		{
			GridField campoLowRank = 	mTab.getField("XX_LOWRANK");
			Integer lowRank = (Integer) campoLowRank.getValue(); 
			Integer highRank = (Integer)mField.getValue();
			
			if( (highRank.compareTo(cero) != 0))//&&(lowRank.compareTo(cero) != 0) )
			{

				String SQL = "select XX_LOWRANK,XX_HIGHRANK from XX_VME_PRICERULE order by xx_lowrank  asc";
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				ResultSet rs = pstmt.executeQuery();
				Integer valor=null;	
				boolean flag = false;
				while(rs.next())
				{	
					//if ((lowRank.intValue() >= rs.getInt("XX_LOWRANK") && lowRank.intValue() <= rs.getInt("XX_HIGHRANK")) || ((highRank.intValue() >= rs.getInt("XX_LOWRANK") && highRank.intValue() <= rs.getInt("XX_HIGHRANK"))))
					if ( highRank.intValue() >= rs.getInt("XX_LOWRANK") && highRank.intValue() <= rs.getInt("XX_HIGHRANK") )
					{
						//mTab.setValue("XX_LOWRANK", rs.getInt("XX_HIGHRANK")+1);
						mTab.setValue("XX_HIGHRANK", 0);
						flag = true;
						//return "Valor recomendado higt, "+ rs.getInt("XX_HIGHRANK")+1;
					}
					valor = rs.getInt("XX_HIGHRANK");
				}
				
				rs.close();
				pstmt.close();
				
				if(flag == true)
				{
					if(valor.equals(max))
					{
						return"El maximo limite fue usado";
					}
					valor = valor +1;
					return "Valor recomendado low, "+valor;
				}
				else
				{
					return"";
				}
				
				//if( (lowRank.compareTo(highRank) >= 0) )
				//{	
				//	mTab.setValue("XX_HIGHRANK", cero);
				//	return "Error en los Rangos";
				//}
				

			}
	
				return"";
			
		}
		catch(Exception e)
		{	
			return "ERROR";
		}

	}	
	public String check (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		
	String check = mField.getValue().toString();
		if(check.equals("true"))
			mTab.setValue("XX_HIGHRANK", max);
		if(check.equals("false"))
			mTab.setValue("XX_HIGHRANK", cero);
		return "";
	}
}