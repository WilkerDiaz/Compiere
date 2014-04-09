package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.framework.PO;
import org.compiere.intf.RankAnalysisInterface;
import org.compiere.model.MABCAnalysisGroup;
import org.compiere.model.MABCProductAssignment;
import org.compiere.model.MABCRank;
import org.compiere.model.X_M_ABCRankAnalysis;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;
import org.compiere.util.Ini;

public class AssignABCRank extends SvrProcess{
	
	private int p_AD_Org_ID = 0;
	private int p_M_Warehouse_ID=0;
	private int p_M_ABCAnalysisGroup_ID = 0;
	private int p_M_PriceList_Version_ID = 0;
	private int p_M_ABCRankAnalysis_ID = 0;
	private int p_DaysForAnalysis =0;
	final private int commitRecord = Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));
	
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = element.getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = element.getParameterAsInt();
			else if (name.equals("M_ABCAnalysisGroup_ID"))
				p_M_ABCAnalysisGroup_ID = element.getParameterAsInt();
			else if (name.equals("M_ABCRankAnalysis_ID"))
				p_M_ABCRankAnalysis_ID = element.getParameterAsInt();
			else if (name.equals("M_PriceList_Version_ID"))
					p_M_PriceList_Version_ID = element.getParameterAsInt();
			else if (name.equals("DaysForAnalysis"))
				p_DaysForAnalysis = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}
	
	@Override
	protected String doIt() throws Exception
	{
		MABCAnalysisGroup group = new MABCAnalysisGroup(getCtx(),p_M_ABCAnalysisGroup_ID,get_Trx());
		if(group==null)
			throw new CompiereUserException("Not a valid ABC Analysis Group");
		
		if(p_M_Warehouse_ID != group.getM_Warehouse_ID())
			throw new CompiereUserException("ABC Analysis Group is not defined for the selected Warehouse");
		
		MABCRank[] lines = MABCRank.getLines(getCtx(),group,get_Trx());
		if (lines == null || lines.length==0)
			throw new CompiereUserException("No Lines found in the Rank");
		
		X_M_ABCRankAnalysis analysis = new X_M_ABCRankAnalysis(getCtx(),p_M_ABCRankAnalysis_ID,get_Trx());
		if(analysis==null)
			throw new CompiereUserException("Not a Valid ABC Analysis Criteria");
		
		if(analysis.isBasedOnPriceList()&& p_M_PriceList_Version_ID==0)
			throw new CompiereUserException("Not a Valid Price List");

		//Check for any open inventory cycle count documents
		if(isOpenDocument())
			throw new CompiereUserException("Open Cycle Count Documents for the rank");
		
		//Get the sorted list of products based on analysis criteria
		Integer[] products = doAnalysis(group,analysis);
		if(products==null)
			return "No Products to Analyse";
		
		int totalProducts = products.length;
		if(totalProducts==0)
			return "No Products to Analyse";
			
		// Delete ABC Product Assignment for this Analysis group
		if(!deleteProductAssignment())
		{
			get_Trx().rollback();
			throw new CompiereUserException("Error while deleting ABC Product Assignment");			
		}
		
		// Create Records in MABCProductAssignment for the Analysis Group
		ArrayList<MABCProductAssignment> prods = new ArrayList<MABCProductAssignment>();
		int counter = 0;
		for (MABCRank line:lines)
		{
			int rankCount = new BigDecimal(totalProducts).multiply(line.getPercentage())
			                        .divide(new BigDecimal(100),0,BigDecimal.ROUND_CEILING).intValue();
			for(int i = 0;i<rankCount && counter<products.length; i++)
			{
				MABCProductAssignment prod = MABCProductAssignment.getForProduct(getCtx(),products[counter].intValue(),
						                                                         p_M_ABCAnalysisGroup_ID,get_Trx());
				if(prod == null)
					throw new CompiereUserException("Not able to Generate ABC Product Assignment");
				
				prod.setAD_Client_ID(line.getAD_Client_ID());
				prod.setAD_Org_ID(p_AD_Org_ID);
				prod.setM_Warehouse_ID(p_M_Warehouse_ID);
				prod.setM_Product_ID(products[counter]);
				prod.setM_ABCAnalysisGroup_ID(p_M_ABCAnalysisGroup_ID);
				prod.setM_ABCRank_ID(line.getM_ABCRank_ID());
				prod.setIsActive(true);
				prods.add(prod);
				counter++;
				if( counter%commitRecord==0)
				{
			        if(!PO.saveAll(get_Trx(),prods))
			        {
			        	get_Trx().rollback();
			        	throw new CompiereUserException("Not Able to save ABC Product Assignments");
			        }
			        prods.clear();
				}
			}
		}
        if(!PO.saveAll(get_Trx(),prods))
        {
        	get_Trx().rollback();
        	throw new CompiereUserException("Not Able to save ABC Product Assignments");
        }
		return "Success";
	}
	
	private String getAnalysisClass(X_M_ABCRankAnalysis analysis)
	{
		String className = "";
		if(analysis.getAnalysisClassName() !=null && analysis.getAnalysisClassName().length()!=0)
			className = analysis.getAnalysisClassName();
		return className;
		
	}
	
	private RankAnalysisInterface getRankAnalysis(X_M_ABCRankAnalysis analysis)
	{
		String className = getAnalysisClass(analysis);
		if(className==null || className.length()==0)
		{
			log.log(Level.SEVERE, "Invalid Rule Class");
			return null;
		}
		try
		{
			Class<?> c = Class.forName(className);
			if(c!=null)
			{
				RankAnalysisInterface rule = (RankAnalysisInterface) c.newInstance();
				return rule;
			}
			else
			{
				log.log(Level.SEVERE, "Invalid Rule Class");
				return null;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Invalid Rule Class");
		}
		return null;
	}

	private Integer[] doAnalysis(MABCAnalysisGroup group, X_M_ABCRankAnalysis analysis)
	{
		Integer [] products = null;
		RankAnalysisInterface s = getRankAnalysis(analysis);
		int analysisDays = p_DaysForAnalysis;
		if(analysisDays==0)
			analysisDays=group.getDaysForAnalysis();
		if (s!=null)
			products = s.rankAnalysis(group, p_M_PriceList_Version_ID,p_DaysForAnalysis);
		else
			log.log(Level.SEVERE,"Error instantiating class");
		
		return products;
	}
	
	private boolean isOpenDocument()
	{
		boolean retVal = false;
		String sql = " SELECT 1 FROM M_Inventory inv " 
			       + " INNER JOIN M_InventoryLine line ON (inv.M_Inventory_ID = line.M_Inventory_ID) "
			       + " INNER JOIN M_ABCAnalysisGroup gr ON (line.M_ABCAnalysisGroup_ID = gr.M_ABCAnalysisGroup_ID "
			                                            + " AND gr.AD_Client_ID = line.AD_Client_ID ) "
			       + " WHERE gr.M_ABCAnalysisGroup_ID = ? "
			       + " AND gr.AD_Client_ID = ? "
			       + " AND (inv.IsLocked = 'Y' OR inv.DocStatus IN ('IP','DR')) ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,get_Trx());
			pstmt.setInt(1,p_M_ABCAnalysisGroup_ID);
			pstmt.setInt(2,getAD_Client_ID());
			rs = pstmt.executeQuery();
			if(rs.next())
				retVal = true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retVal;
	}

	private boolean deleteProductAssignment()
	{
		boolean retVal = false;
		String sql = "DELETE M_ABCProductAssignment WHERE M_ABCAnalysisGroup_ID = ? AND AD_Client_ID = ? ";
		ArrayList< Object > params = new ArrayList< Object >();
		params.add(p_M_ABCAnalysisGroup_ID);
		params.add(getAD_Client_ID());
		int deleted = DB.executeUpdate(get_Trx(), sql,params);
		if(deleted <0)
		{
			log.log(Level.WARNING,"Unable to delete the product assignment for ABC Analysis Group");
			retVal= false;
		}
		else
		{
			log.log(Level.FINE,"Deleted: "+ deleted + "ABC Product Assignments");
			retVal= true;
		}
		return retVal;
	}
}
