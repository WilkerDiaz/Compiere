package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MVMRComercialBudgetTab;
import compiere.model.cds.MVMRPrld01;
import compiere.model.cds.MVMRStoreDistri;

public class XX_GenerationOfFinalBudget extends SvrProcess{

	@Override
	protected String doIt() throws Exception 
	{
		//Valida que no se genere el Presupuesto Definitivo para un mismo período
		if(!getValidatePeriod())
		{
			//Busca los registros de la distribución por tienda
			String SQL = "SELECT * "
					   + "FROM XX_VMR_StoreDistri sd, XX_VMR_ComercialBudgetTab cb "
					   + "WHERE sd.XX_VMR_Department_ID=cb.XX_VMR_Department_ID "
					   + "AND sd.C_Period_ID=cb.C_Period_ID";
			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			    ResultSet rs = pstmt.executeQuery();
			    
				while(rs.next())
				{
					MVMRStoreDistri storeDistri= new MVMRStoreDistri(Env.getCtx(),rs.getInt("XX_VMR_StoreDistri_ID"),null);
					MVMRComercialBudgetTab cbt = new MVMRComercialBudgetTab(Env.getCtx(),rs.getInt("XX_VMR_ComercialBudgetTab_ID"),null);
					
					//Si la consulta es distinta de cero se crean los registros Presupuestados 
					//en el Presupuesto Definitivo
					if(storeDistri.get_ID()!=0)
					{
						MVMRPrld01 finalBudget = new MVMRPrld01(Env.getCtx(),0,null);
						finalBudget.setXX_BUDGETYEARMONTH(getPeriod(storeDistri.get_ID()));
						finalBudget.setM_Warehouse_ID(storeDistri.getM_Warehouse_ID());
						finalBudget.setXX_VMR_Category_ID(storeDistri.getXX_VMR_Category_ID());
						finalBudget.setXX_VMR_Department_ID(storeDistri.getXX_VMR_Department_ID());
						finalBudget.setXX_VMR_Line_ID(storeDistri.getXX_VMR_Line_ID());
						finalBudget.setXX_VMR_Section_ID(storeDistri.getXX_VMR_Section_ID());
						finalBudget.setXX_INVEFECBUDGETEDAMOUNT(storeDistri.getXX_InitialInventory());
						finalBudget.setXX_INVAMOUNTORIGBUDGETED(new BigDecimal(storeDistri.getXX_InitialInventoryPieces()));
						finalBudget.setXX_PURCHAMOUNTBUDGETED(storeDistri.getXX_Purchases());
						finalBudget.setXX_QUANTBUDGETEDSHOPPING(new BigDecimal(storeDistri.getXX_PurchasesPieces()));
						finalBudget.setXX_SALESAMOUNTBUD2(storeDistri.getXX_SalesDistribution());
						finalBudget.setXX_SALESAMOUNTBUD(new BigDecimal(storeDistri.getXX_SalesDistributionPiece()));
						//finalBudget.setXX_PROMSALEAMOUNTBUD(XX_PROMSALEAMOUNTBUD);
						finalBudget.setXX_PROMSALENUMBUD(new BigDecimal(0));
						//finalBudget.setXX_PECTSALEPROMBUD(XX_PECTSALEPROMBUD);
						//finalBudget.setXX_AMOUNTSALEFRBUD(XX_AMOUNTSALEFRBUD);
						finalBudget.setXX_BUDAMOUNTFRSALE(new BigDecimal(0));
						finalBudget.setXX_FINALINVAMOUNTBUD2(storeDistri.getXX_BudgetedFinalInventory());
						finalBudget.setXX_FINALINVAMOUNTBUD(new BigDecimal(storeDistri.getXX_BudFinalInventoryPiece()));
						finalBudget.setXX_ROTATIONBUD(cbt.getXX_Rotation());
						finalBudget.setXX_PERCNBUDCOVERAGE(cbt.getXX_Cobert());
						finalBudget.setXX_MARGACCORDINGBUDPURCH((storeDistri.getXX_SalesDistribution().subtract(storeDistri.getXX_Purchases())).divide(storeDistri.getXX_SalesDistribution(), 2, RoundingMode.HALF_UP));
						finalBudget.setXX_LISCKGROSSMARGPERCTBUD(new BigDecimal(0));
						finalBudget.setXX_NETMARGPERTCATTLEBUD(new BigDecimal(0));
						finalBudget.setXX_BYWINMARGPERTBUD(new BigDecimal(0));
						finalBudget.setXX_BUDDDECLINE(storeDistri.getXX_Decrease());
						finalBudget.save();
					}
					else
					{
						return "No se pudo generar el Presupuesto Definitivo, porque no existe un Presupuesto Comercial";
					}
				}
				rs.close();
				pstmt.close();			
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}			
		}
		else
		{
			return "El Presupuesto Definitivo ya fue generado para este período";
		}
		
		//Borra el Presupuesto Comercial
		deleteComercialBudgetTab();
		deleteAssortmentPlan();
		deletePurchasePlan();
		deleteStoreDistri();
		
		return "Se ha generado correctamente el Presupuesto Definitivo";
	}
		
	/*
	 *  Obtiene el año y mes de la Distribución por tienda según el ID de la Distribución por tienda
	 */
	private int getPeriod(Integer storeDistri)
	{
		int period = 0;
		
		String SQL = "SELECT to_char(p.StartDate,'yyyymm') "
				   + "FROM C_Period p, XX_VMR_StoreDistri s " 
				   + "WHERE p.C_Period_ID=s.C_Period_ID "
				   + "AND s.XX_VMR_StoreDistri_ID="+storeDistri;
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			if(rs.next())
			{
				period = Integer.parseInt(rs.getString(1));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return period;
	}
	
	/*
	 *  Valida el año y mes del Store Distribution para no gererar un Presupuesto Definitivo 
	 *  con el mismo período
	 */
	private boolean getValidatePeriod()
	{
		boolean b = false;		
		
		String SQL = "SELECT DISTINCT(to_char(p.StartDate,'yyyymm')) "
				   + "FROM C_Period p, XX_VMR_StoreDistri s "  
				   + "WHERE p.C_Period_ID=s.C_Period_ID "
				   + "INTERSECT " 
				   + "SELECT DISTINCT(to_char(p.StartDate,'yyyymm')) " 
				   + "FROM C_Period p, XX_VMR_PRLD01 pr " 
				   + "WHERE to_char(p.StartDate,'yyyymm')=pr.XX_BUDGETYEARMONTH";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while(rs.next())
			{								
				if(!rs.getString(1).equals(0))
				{
					b = true;
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return b;
	}
	
	/*
	 * Borra todos los registros del Presupuesto Comercial
	 */
	private boolean deleteComercialBudgetTab()
	{
		String SQL = "DELETE FROM XX_VMR_ComercialBudgetTab";
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();					
			rs.close();
			pstmt.close();
							
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, SQL, e);
			return false;
		}
		
		return true;
	}
	
	/*
	 * Borra todos los registros del Plan de Surtido
	 */
	private boolean deleteAssortmentPlan()
	{
		String SQL = "DELETE FROM XX_VMR_AssortmentPlan";
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();					
			rs.close();
			pstmt.close();
							
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, SQL, e);
			return false;
		}
		
		return true;
	}
	
	/*
	 * Borra todos los registros del Plan de Compras
	 */
	private boolean deletePurchasePlan()
	{
		String SQL = "DELETE FROM XX_VMR_PurchasePlan";
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();					
			rs.close();
			pstmt.close();
							
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, SQL, e);
			return false;
		}
		
		return true;
	}
	
	/*
	 * Borra todos los registros de la Distribución por tienda
	 */
	private boolean deleteStoreDistri()
	{
		String SQL = "DELETE FROM XX_VMR_StoreDistri";
		
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();					
			rs.close();
			pstmt.close();
							
		}
		catch(Exception e)
		{	
			log.log(Level.SEVERE, SQL, e);
			return false;
		}
		
		return true;
	}
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
