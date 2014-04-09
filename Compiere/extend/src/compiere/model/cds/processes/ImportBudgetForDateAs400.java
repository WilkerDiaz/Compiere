package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.As400DbManager;

public class ImportBudgetForDateAs400 extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		As400DbManager As = new As400DbManager();
		As.conectar();
		
		Calendar date = Calendar.getInstance();
	
		Statement sentencia=null;
		//String sqlAs = "Select * from becofile.invm4" ;
		
		sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = null;
		String sql = "select codtie, codcat, coddep, codlin, codsec,a—omespre, monvenrea, canvenrea from becofile.prld01 where (CODSEC<>99 and CODLIN<>99 and CODDEP<>99 and CODCAT<>99 and CODTIE<>999) and (monvenrea>0 or canvenrea>0) and A—OMESPRE in (200907,200908,200909,200910, 200911, 200912, 201001, 201002, 201003, 201004, 201005, 201006)";	
        System.out.println(sql);
		try
        {          
        	
        	
			rs= As.realizarConsulta(sql,sentencia);
			int parar=0;
			
			while (rs.next())
			{
				
				String QueryCompiereUpdate = "UPDATE I_XX_VMR_PRLD01 SET " +	
/*				
				"XX_MOINVINPR = "+rs.getString(7)+"," +  
				"XX_CAINVINPR = "+rs.getString(8)+"," + 
				"XX_MOINVINCO = "+rs.getString(9)+"," + 
				"XX_MOINVINRE = "+rs.getString(10)+"," +  
				"XX_CAINVINRE = "+rs.getString(11)+"," + 
				"XX_MONCOMPRE = "+rs.getString(12)+"," +  
				"XX_CANCOMPRE = "+rs.getString(13)+"," +  
				"XX_MOCOMNCCO = "+rs.getString(14)+"," +  
				"XX_MOCOMNCOL = "+rs.getString(15)+"," +  
				"XX_CACOMNCOL = "+rs.getString(16)+"," + 
				"XX_MOCOMNREC = "+rs.getString(17)+"," +  
				"XX_CACOMNREC = "+rs.getString(18)+"," + 
				"XX_MOCOMICCO = "+rs.getString(19)+"," + 
				"XX_MOCOMICOL = "+rs.getString(20)+"," + 
				"XX_CACOMICOL = "+rs.getString(21)+"," + 
				"XX_MOCOMIREC = "+rs.getString(22)+"," + 
				"XX_CACOMIREC = "+rs.getString(23)+"," + 
				"XX_MOCOMCOMA = "+rs.getString(24)+"," + 
				"XX_CACOMCOMA = "+rs.getString(25)+"," + 
				"XX_MOCOMREMA = "+rs.getString(26)+"," + 
				"XX_CACOMREMA = "+rs.getString(27)+"," + 
				"XX_MONVENPRE = "+rs.getString(28)+"," + 
				"XX_CANVENPRE = "+rs.getString(29)+"," + 
				"XX_MONVENCOS = "+rs.getString(30)+"," + 
*/				"XX_MONVENREA = "+rs.getString("monvenrea")+"," + 
				"XX_CANVENREA = "+rs.getString("canvenrea")+" " +  
/*				"XX_MOREBPRPR = "+rs.getString(33)+"," + 
				"XX_CAREBPRPR = "+rs.getString(34)+"," + 
				"XX_POREBPRPR = "+rs.getString(35)+"," + 
				"XX_MOREBPRRE = "+rs.getString(36)+"," + 
				"XX_CAREBPRRE = "+rs.getString(37)+"," + 
				"XX_POREBPRRE = "+rs.getString(38)+"," + 
				"XX_MOREBFRPR = "+rs.getString(39)+"," + 
				"XX_CAREBFRPR = "+rs.getString(40)+"," + 
				"XX_POREBFRPR = "+rs.getString(41)+"," + 
				"XX_MOREBFRRE = "+rs.getString(42)+"," + 
				"XX_CAREBFRRE = "+rs.getString(43)+"," + 
				"XX_POREBFRRE = "+rs.getString(44)+"," + 
				"XX_MOREBDFPR = "+rs.getString(45)+"," + 
				"XX_CAREBDFPR = "+rs.getString(46)+"," + 
				"XX_POREBDFPR = "+rs.getString(47)+"," + 
				"XX_MOREBDFRE = "+rs.getString(48)+"," + 
				"XX_CAREBDFRE = "+rs.getString(49)+"," + 
				"XX_POREBDFRE = "+rs.getString(50)+"," + 
				"XX_MONTRAENV = "+rs.getString(51)+"," + 
				"XX_CANTRAENV = "+rs.getString(52)+"," + 
				"XX_MONTRAREC = "+rs.getString(53)+"," + 
				"XX_CANTRAREC = "+rs.getString(54)+"," + 
				"XX_MOINVFIPR = "+rs.getString(55)+"," + 
				"XX_CAINVFIPR = "+rs.getString(56)+"," + 
				"XX_MOINVFIRE = "+rs.getString(57)+"," + 
				"XX_CAINVFIRE = "+rs.getString(58)+"," + 
				"XX_MOINVFIPY = "+rs.getString(59)+"," + 
				"XX_CAINVFIPY = "+rs.getString(60)+"," + 
				"XX_MONLIMCOM = "+rs.getString(61)+"," + 
				"XX_CANLIMCOM = "+rs.getString(62)+"," + 
				"XX_PORROTPRE = "+rs.getString(63)+"," + 
				"XX_PORROTREA = "+rs.getString(64)+"," + 
				"XX_PORCOBPRE = "+rs.getString(65)+"," + 
				"XX_PORCOBREA = "+rs.getString(66)+"," + 
				"XX_POMARSCPR = "+rs.getString(67)+"," + 
				"XX_POMARSCRE = "+rs.getString(68)+"," + 
				"XX_POMARBGPR = "+rs.getString(69)+"," + 
				"XX_POMARBGRE = "+rs.getString(70)+"," + 
				"XX_POMARNGPR = "+rs.getString(71)+"," +  
				"XX_POMARNGRE = "+rs.getString(72)+"," + 
				"XX_POMARPGPR = "+rs.getString(73)+"," + 
				"XX_POMARPGRE = "+rs.getString(74)+"," + 
				"XX_MOMERMPRE = "+rs.getString(75)+"," + 
				"XX_MOMERMREA = "+rs.getString(76)+" " + 
*/	
				"where XX_CODTIE="+rs.getString("codtie")+" and " +
				"XX_canvenrea=0 and " +
				"XX_monvenrea=0 and " +
				"XX_CodCat="+rs.getString("codcat")+" and " +
				"XX_CodDep="+rs.getString("coddep")+" and " +
				"XX_CodLin="+rs.getString("codlin")+" and " +
				"XX_Codsec="+rs.getString("codsec")+" and " +
				"XX_A—OMESPRE = "+ rs.getString("a—omespre"); // OJO, esto se debe cambiar quitarle el -1000

				PreparedStatement pstmtQuery = DB.prepareStatement(QueryCompiereUpdate, null);
			    int rsQuery = pstmtQuery.executeUpdate(QueryCompiereUpdate);
			    pstmtQuery.close();
			    //if(rsQuery==0)
/**			    {
			    
				X_I_XX_VMR_Prld01 tablaCompiere = new X_I_XX_VMR_Prld01(getCtx(),0,null);
				
				tablaCompiere.setXX_CODTIE(rs.getInt(1));
				tablaCompiere.setXX_CodCat(rs.getInt(2));
				tablaCompiere.setXX_CodDep(rs.getInt(3));
				tablaCompiere.setXX_CodLin(rs.getInt(4));
				tablaCompiere.setXX_Codsec(rs.getInt(5));
				
				tablaCompiere.setXX_A—OMESPRE((new BigDecimal(rs.getString(6))).subtract(new BigDecimal(100))); // Ojo se le debe quitar el 100
				tablaCompiere.setXX_MOINVINPR(new BigDecimal(rs.getString(7)));
				tablaCompiere.setXX_CAINVINPR(new BigDecimal(rs.getString(8)));
			    tablaCompiere.setXX_MOINVINCO(new BigDecimal(rs.getString(9)));
			    tablaCompiere.setXX_MOINVINRE(new BigDecimal(rs.getString(10)));
			    tablaCompiere.setXX_CAINVINRE(new BigDecimal(rs.getString(11)));
			    tablaCompiere.setXX_MONCOMPRE(new BigDecimal(rs.getString(12)));
			    tablaCompiere.setXX_CANCOMPRE(new BigDecimal(rs.getString(13)));
			    tablaCompiere.setXX_MOCOMNCCO(new BigDecimal(rs.getString(14)));
			    tablaCompiere.setXX_MOCOMNCOL(new BigDecimal(rs.getString(15)));
			    tablaCompiere.setXX_CACOMNCOL(new BigDecimal(rs.getString(16)));
			    tablaCompiere.setXX_MOCOMNREC(new BigDecimal(rs.getString(17)));
			    tablaCompiere.setXX_CACOMNREC(new BigDecimal(rs.getString(18)));
			    tablaCompiere.setXX_MOCOMICCO(new BigDecimal(rs.getString(19)));
			    tablaCompiere.setXX_MOCOMICOL(new BigDecimal(rs.getString(20)));
			    tablaCompiere.setXX_CACOMICOL(new BigDecimal(rs.getString(21)));
			    tablaCompiere.setXX_MOCOMIREC(new BigDecimal(rs.getString(22)));
			    tablaCompiere.setXX_CACOMIREC(new BigDecimal(rs.getString(23)));
			    tablaCompiere.setXX_MOCOMCOMA(new BigDecimal(rs.getString(24)));
			    tablaCompiere.setXX_CACOMCOMA(new BigDecimal(rs.getString(25)));
			    tablaCompiere.setXX_MOCOMREMA(new BigDecimal(rs.getString(26)));
			    tablaCompiere.setXX_CACOMREMA(new BigDecimal(rs.getString(27)));
			    tablaCompiere.setXX_MONVENPRE(new BigDecimal(rs.getString(28)));
			    tablaCompiere.setXX_CANVENPRE(new BigDecimal(rs.getString(29)));
			    tablaCompiere.setXX_MONVENCOS(new BigDecimal(rs.getString(30)));
			    tablaCompiere.setXX_MONVENREA(new BigDecimal(rs.getString(31)));
			    tablaCompiere.setXX_CANVENREA(new BigDecimal(rs.getString(32)));
			    tablaCompiere.setXX_MOREBPRPR(new BigDecimal(rs.getString(33)));
			    tablaCompiere.setXX_CAREBPRPR(new BigDecimal(rs.getString(34)));
			    tablaCompiere.setXX_POREBPRPR(new BigDecimal(rs.getString(35)));
			    tablaCompiere.setXX_MOREBPRRE(new BigDecimal(rs.getString(36)));
			    tablaCompiere.setXX_CAREBPRRE(new BigDecimal(rs.getString(37)));
			    tablaCompiere.setXX_POREBPRRE(new BigDecimal(rs.getString(38)));
			    tablaCompiere.setXX_MOREBFRPR(new BigDecimal(rs.getString(39)));
			    tablaCompiere.setXX_CAREBFRPR(new BigDecimal(rs.getString(40)));
			    tablaCompiere.setXX_POREBFRPR(new BigDecimal(rs.getString(41)));
			    tablaCompiere.setXX_MOREBFRRE(new BigDecimal(rs.getString(42)));
			    tablaCompiere.setXX_CAREBFRRE(new BigDecimal(rs.getString(43)));
			    tablaCompiere.setXX_POREBFRRE(new BigDecimal(rs.getString(44)));
			    tablaCompiere.setXX_MOREBDFPR(new BigDecimal(rs.getString(45)));
			    tablaCompiere.setXX_CAREBDFPR(new BigDecimal(rs.getString(46)));
			    tablaCompiere.setXX_POREBDFPR(new BigDecimal(rs.getString(47)));
			    tablaCompiere.setXX_MOREBDFRE(new BigDecimal(rs.getString(48)));
			    tablaCompiere.setXX_CAREBDFRE(new BigDecimal(rs.getString(49)));
			    tablaCompiere.setXX_POREBDFRE(new BigDecimal(rs.getString(50)));
			    tablaCompiere.setXX_MONTRAENV(new BigDecimal(rs.getString(51)));
			    tablaCompiere.setXX_CANTRAENV(new BigDecimal(rs.getString(52)));
			    tablaCompiere.setXX_MONTRAREC(new BigDecimal(rs.getString(53)));
			    tablaCompiere.setXX_CANTRAREC(new BigDecimal(rs.getString(54)));
			    tablaCompiere.setXX_MOINVFIPR(new BigDecimal(rs.getString(55)));
			    tablaCompiere.setXX_CAINVFIPR(new BigDecimal(rs.getString(56)));
			    tablaCompiere.setXX_MOINVFIRE(new BigDecimal(rs.getString(57)));
			    tablaCompiere.setXX_CAINVFIRE(new BigDecimal(rs.getString(58)));
			    tablaCompiere.setXX_MOINVFIPY(new BigDecimal(rs.getString(59)));
			    tablaCompiere.setXX_CAINVFIPY(new BigDecimal(rs.getString(60)));
			    tablaCompiere.setXX_MONLIMCOM(new BigDecimal(rs.getString(61)));
			    tablaCompiere.setXX_CANLIMCOM(new BigDecimal(rs.getString(62)));
			    tablaCompiere.setXX_PORROTPRE(new BigDecimal(rs.getString(63)));
			    tablaCompiere.setXX_PORROTREA(new BigDecimal(rs.getString(64)));
			    tablaCompiere.setXX_PORCOBPRE(new BigDecimal(rs.getString(65)));
			    tablaCompiere.setXX_PORCOBREA(new BigDecimal(rs.getString(66)));
			    tablaCompiere.setXX_POMARSCPR(new BigDecimal(rs.getString(67)));
			    tablaCompiere.setXX_POMARSCRE(new BigDecimal(rs.getString(68)));
			    tablaCompiere.setXX_POMARBGPR(new BigDecimal(rs.getString(69)));
			    tablaCompiere.setXX_POMARBGRE(new BigDecimal(rs.getString(70)));
			    tablaCompiere.setXX_POMARNGPR(new BigDecimal(rs.getString(71)));
			    tablaCompiere.setXX_POMARNGRE(new BigDecimal(rs.getString(72)));
			    tablaCompiere.setXX_POMARPGPR(new BigDecimal(rs.getString(73)));
			    tablaCompiere.setXX_POMARPGRE(new BigDecimal(rs.getString(74)));
			    tablaCompiere.setXX_MOMERMPRE(new BigDecimal(rs.getString(75)));
			    tablaCompiere.setXX_MOMERMREA(new BigDecimal(rs.getString(76)));
	
			    tablaCompiere.save();
			   // }
				//parar++;
*/			
				
				//if(parar==10)
				//	break;
			    
			}
			rs.close();
			sentencia.close();
			
			
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}	

		
		return "";
	}

	@Override
	protected void prepare() {
		
	}

}
