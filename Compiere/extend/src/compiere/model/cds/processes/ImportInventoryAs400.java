package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_I_XX_VCN_INVM14;

public class ImportInventoryAs400 extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
	
		As400DbManager As = new As400DbManager();
		As.conectar();
		
	
		Statement sentencia=null;
		//String sqlAs = "Select * from becofile.invm4" ;
		
		sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = null;
		String sql = "select * from becofile.invm14";   ;	
        try
        {          

			rs= As.realizarConsulta(sql,sentencia);
			int parar=0;
			
			while (rs.next())
			{
				
				String QueryCompiereUpdate = "UPDATE i_xx_vcn_invm14 SET " +
						
						"XX_CANTINVINI="+rs.getString(10)+"," +
						"XX_MONTINVINI="+rs.getString(11)+"," +
						"XX_CANTCOMPRA="+rs.getString(12)+"," +
						"XX_MONTCOMPRA="+rs.getString(13)+"," +
						"XX_CANTVENTAS="+rs.getString(14)+"," +
						"XX_MONTVENTAS="+rs.getString(15)+"," +
						"XX_CANTMOVIMI="+rs.getString(16)+"," +
						"XX_MONTMOVIMI="+rs.getString(17)+"," +
						"XX_CANTAJUSTE="+rs.getString(18)+"," +
						"XX_MONTAJUSTE="+rs.getString(19)+"," +
						"XX_CANTAJUANT="+rs.getString(20)+" " +
						
						"where xx_tienda="+rs.getString(1)+" and " +
						"xx_codcat="+rs.getString(2)+" and " +
						"xx_coddep="+rs.getString(3)+" and " +
						"xx_codlin="+rs.getString(4)+" and " +
						"xx_codsec="+rs.getString(5)+" and " +
						"xx_codpro="+rs.getString(6)+" and " +
						"xx_conpre="+rs.getString(7)+" and " +
						"XX_MESINV="+rs.getString(8)+" and " +
						"XX_ANOINV="+rs.getString(9);
				
				PreparedStatement pstmtQuery = DB.prepareStatement(QueryCompiereUpdate, null);
			    int rsQuery = pstmtQuery.executeUpdate(QueryCompiereUpdate);
			    pstmtQuery.close();
			    
			    if(rsQuery==0)
			    {
			    
				X_I_XX_VCN_INVM14 tablaCompiere = new X_I_XX_VCN_INVM14(getCtx(),0,null);
				
				tablaCompiere.setXX_TIENDA(rs.getInt(1));
				tablaCompiere.setXX_CodCat(rs.getInt(2));
				tablaCompiere.setXX_CodDep(rs.getInt(3));
				tablaCompiere.setXX_CodLin(rs.getInt(4));
				tablaCompiere.setXX_Codsec(rs.getInt(5));
				tablaCompiere.setXX_CODPRO(rs.getInt(6));
				tablaCompiere.setXX_CONPRE(new BigDecimal(rs.getString(7)));
				
				tablaCompiere.setXX_MESINV(new BigDecimal(rs.getString(8)));
				tablaCompiere.setXX_ANOINV(new BigDecimal(rs.getString(9)));
				tablaCompiere.setXX_CANTINVINI(new BigDecimal(rs.getString(10)));
				tablaCompiere.setXX_MONTINVINI(new BigDecimal(rs.getString(11)));
				tablaCompiere.setXX_CANTCOMPRA(new BigDecimal(rs.getString(12)));
				tablaCompiere.setXX_MONTCOMPRA(new BigDecimal(rs.getString(13)));
				tablaCompiere.setXX_CANTVENTAS(new BigDecimal(rs.getString(14)));
				tablaCompiere.setXX_MONTVENTAS(new BigDecimal(rs.getString(15)));
				tablaCompiere.setXX_CANTMOVIMI(new BigDecimal(rs.getString(16)));
				tablaCompiere.setXX_MONTMOVIMI(new BigDecimal(rs.getString(17)));
				tablaCompiere.setXX_CANTAJUSTE(new BigDecimal(rs.getString(18)));
				tablaCompiere.setXX_MONTAJUSTE(new BigDecimal(rs.getString(19)));
				tablaCompiere.setXX_CANTAJUANT(new BigDecimal(rs.getString(20)));
			
			    tablaCompiere.save();
			    }
				parar++;
			
				
			if(parar==10)
				break;
			    
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
		// TODO Auto-generated method stub

	}


}
