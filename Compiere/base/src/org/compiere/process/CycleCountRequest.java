package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MABCProductAssignment;
import org.compiere.model.MABCAnalysisGroup;
import org.compiere.model.MABCRank;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MCycleCountLock;
import org.compiere.model.MInventoryLineMA;
import org.compiere.model.MLocator;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_ABCRankSort;
import org.compiere.framework.PO;
import org.compiere.intf.*;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.util.CompiereUserException;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

public class CycleCountRequest extends SvrProcess{
	
	private int p_M_ABCAnalysisGroup_ID= 0;
	private int p_AD_Org_ID = 0;
	private int p_M_Warehouse_ID = 0;
	private boolean p_ConsolidateDocument = false;
	private boolean p_LockDocument = false;
	private int workingDays = 0;
	private MInventoryLine	m_line = null;
	final private int commitRecord = 1000;
	MInventory inv = null;
	ArrayList<MInventory> inventory = new ArrayList<MInventory>();;
	
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
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = "Y".equals(element.getParameter());
			else if (name.equals("LockDocument"))
				p_LockDocument = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}
	
	@Override
	protected String doIt() throws Exception
	{
		log.log(Level.FINE,"Creating Cycle Count Document "
				+ " AD_Org_ID = " + p_AD_Org_ID
				+ " M_Warehouse_ID = " + p_M_Warehouse_ID
				+ " M_ABCAnalysisGroup_ID = " + p_M_ABCAnalysisGroup_ID
				+ " Consolidate Document = " + p_ConsolidateDocument); 
		
		int total=0;

		if (p_M_ABCAnalysisGroup_ID==0)
			throw new CompiereUserException("Not a valid rank");
		
		MABCAnalysisGroup group = new MABCAnalysisGroup(getCtx(),p_M_ABCAnalysisGroup_ID,get_Trx());
		
		if(group==null)
			throw new CompiereUserException("Not a valid rank");
		
		// Check that the process is not run again in the same day for the analysis group
		Timestamp lastRunTime = group.getDateLastRun();
		if(lastRunTime != null) // null means this is the first run of the analysis group
		{
			int timeInterval = TimeUtil.getDaysBetween(lastRunTime, new Timestamp(System.currentTimeMillis()));
			if(timeInterval <1)
				throw new CompiereUserException("Cycle count has already been generated for today");
		}
		
		MABCRank[] lines = MABCRank.getLines(getCtx(), group, get_Trx());
		if(lines==null || lines.length==0)
			throw new CompiereUserException("Rank Lines not found");
		if(p_M_Warehouse_ID != 0)
		{
			MWarehouse wh = MWarehouse.get(getCtx(), p_M_Warehouse_ID);
			if(wh==null)
				throw new CompiereUserException("Not a valid Warehouse");
			if(wh.getWorkingDays() <=0 )
				throw new CompiereUserException("Working Days not defined for the warehouse");
			workingDays = wh.getWorkingDays();
		}
	
		for (MABCRank line :lines)
		{
			// Get the number of products in this rank line.
			int count = getProductCount(line);
			if(count<0)
				throw new CompiereUserException("Process encountered error in creating cycle count document");
			
			if(count==0)
			{
		    	log.log(Level.WARNING," No Product found in the rank line '"+ line.getABCRank()+ "'");
		    	continue;
			}
			
			//Get the number of products to be counted for this rank line
			int cycleCount = getCycleCount(count,line);
			if(cycleCount<0)
				throw new CompiereUserException("Process encountered error in creating cycle count document");
			
			if(cycleCount == 0)
			{
		    	log.log(Level.WARNING," No product available for counting for the rank '"+ line.getABCRank()+ "'");
		    	continue;
			}
			
			// Check that products are in queue to be counted, else reset the counted flag for the products.
			boolean allOk = MABCProductAssignment.verifyProducts(count, group, line);
			if(!allOk)
				throw new CompiereUserException("Process encountered error in creating cycle count document");
			
			//Get the sorted list of products.
			Integer [] products = Sort(line,cycleCount);
			if(products == null || products.length==0)
			{
		    	log.log(Level.WARNING," No product available for counting for the rank '"+ line.getABCRank()+ "'");
		    	continue;
			}
			
			int result = generateCycleCountDoc(products,line);
			if(result<0)
			{
				get_Trx().rollback();
				throw new CompiereUserException("Process encountered error in creating cycle count document");
			}
			else
				total+=result;
		}
		 
		if(p_LockDocument)
		   {
			ArrayList<MInventory> invForCommit = new ArrayList<MInventory>();
			   for(int i = 0;i<inventory.size();i++)
			   {
				   boolean result = MCycleCountLock.lock(inventory.get(i));
				   if(!result)
				   {
					   log.log(Level.WARNING,"Unable to Lock the Documents");
					   break;
				   }
				   invForCommit.add(inventory.get(i));
				   if(invForCommit.size()%commitRecord==0)
				   {
					   PO.saveAll(get_Trx(),invForCommit);
					   invForCommit.clear();
				   }
			   }
			   PO.saveAll(get_Trx(),invForCommit);
		   }
		if(total!=0)
		{
			group.setDateLastRun(new Timestamp(System.currentTimeMillis()));
			group.save(get_Trx());
		}
		
		return "Generated cycle count for "+total+" products";
	}
	
	private int getProductCount(MABCRank line)
	{
		log.log(Level.FINE, " Getting the number of products in the Rank Line: " + line.getABCRank());
		
		int retVal = -1;
		String sql = " SELECT COUNT(1) FROM M_ABCProductAssignment pa, M_Product p WHERE "
			       + " pa.M_ABCAnalysisGroup_ID = ? AND "
			       + " p.M_Product_ID = pa.M_Product_ID AND "
			       + " pa.M_ABCRank_ID = ? AND "
			       + " pa.AD_Client_ID = ? AND "
			       + " pa.IsActive = 'Y' AND "
			       + " p.IsActive = 'Y' AND "
			       + " pa.AD_Org_ID = ? ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, line.getM_ABCAnalysisGroup_ID());
			pstmt.setInt(2, line.getM_ABCRank_ID());
			pstmt.setInt(3, line.getAD_Client_ID());
			pstmt.setInt(4, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retVal = rs.getInt(1);
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
		log.log(Level.FINE,"Total number of products in Rank '" + line.getABCRank() + "' is " + retVal);
		return retVal;
	}
	
	private int getCycleCount(int count, MABCRank line)
	{
		log.log(Level.FINE, "Calculating the number of products to be counted in rank :" + line.getABCRank());
	    if(count==0)
	    {
	    	log.log(Level.WARNING," No Product found in the rank line '"+ line.getABCRank()+ "' to count");
	    	return 0;
	    }
	    
		int multiplier = 0;
		int retVal = -1;
		if(line.getFrequency()==null || line.getFrequency().length()==0 || line.getDuration()==BigDecimal.ZERO)
		{
			log.log(Level.SEVERE,"Setup for Rank is not complete");
			return retVal;
		}
		
		//calculate frequency of cycle count
		if(line.getFrequency().equals("D"))
			multiplier = workingDays;
		else if(line.getFrequency().equals("W"))
			multiplier = 52;
		else if (line.getFrequency().equals("M"))
			multiplier = 12;
		else if(line.getFrequency().equals("Q"))
			multiplier = 4;
		else if(line.getFrequency().equals("Y"))
			multiplier = 1;
		BigDecimal temp = line.getDuration().multiply(new BigDecimal(multiplier));
		BigDecimal rankCycleCount = new BigDecimal(workingDays).divide(temp,1,BigDecimal.ROUND_HALF_UP);
		retVal = (new BigDecimal(count).divide(rankCycleCount,0,BigDecimal.ROUND_CEILING)).intValue();
		
	    log.log(Level.FINE, "Number of products to be counted in rank '" + line.getABCRank()+ "' is "  + retVal);
		return retVal;
	}
	
	private String getSortClass(X_M_ABCRankSort sort)
	{
		String className = "";
		if(sort.getSortClassName() !=null && sort.getSortClassName().length()!=0)
			className = sort.getSortClassName();
		return className;
		
	}
	
	private RankSortInterface getRankSort(X_M_ABCRankSort sort)
	{
		String className = getSortClass(sort);
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
				RankSortInterface rule = (RankSortInterface) c.newInstance();
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
	
	public Integer[] Sort(MABCRank line,int cycleCount)
	{
		X_M_ABCRankSort sort = new X_M_ABCRankSort(line.getCtx(),line.getM_ABCRankSort_ID(),line.get_Trx());
		Integer [] products = null;
		RankSortInterface s = getRankSort(sort);
		if (s!=null)
			products = s.Sort(p_M_Warehouse_ID, line,cycleCount);
		else
			log.log(Level.SEVERE,"Unable to sort the products in the Rank '" + line.getABCRank() +"'");
		
		return products;
	}
	
	private int generateCycleCountDoc(Integer [] products, MABCRank rank)
	{
		int count =0;
		ArrayList<Integer> mProduct = new ArrayList<Integer>();
		ArrayList<Integer> mLocator = new ArrayList<Integer>();
		ArrayList<Integer> mAttributeSetInstance = new ArrayList<Integer>();
		ArrayList<Integer> mAttributeSet = new ArrayList<Integer>();
		ArrayList<BigDecimal> qty = new ArrayList<BigDecimal>();
		
		String pName = null;
		MABCRank line = new MABCRank(getCtx(),rank.getM_ABCRank_ID(),get_Trx());
		MABCAnalysisGroup group = new MABCAnalysisGroup(getCtx(),p_M_ABCAnalysisGroup_ID,get_Trx());
		ArrayList<MABCProductAssignment> prodForCommit = new ArrayList<MABCProductAssignment>();
				
		// get list of all the locators in the Warehouse
		MWarehouse wh = MWarehouse.get(getCtx(), p_M_Warehouse_ID);
		if(wh==null)
		{
			log.log(Level.SEVERE, "Invalid Warehouse");
			return -1;
		}
		
		MLocator [] locators = wh.getLocators(true);
		if(locators == null || locators.length==0)
		{
			log.log(Level.WARNING, " No Locators found in the Warehouse");
			return -1;
		}
		
		for(Integer productID : products )
		{
			MABCProductAssignment product = new MABCProductAssignment(getCtx(),productID,get_Trx());
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if(product.getDateNextCount()!=null && now.before(product.getDateNextCount()))
				continue;
			
			if(inv == null || !p_ConsolidateDocument)
			{
  			    inv =  new MInventory (getCtx(), 0, get_Trx());
  			    inv.setM_Warehouse_ID(p_M_Warehouse_ID);
  			    inv.setAD_Org_ID(p_AD_Org_ID);
  			    inv.setC_DocType_ID(group.getC_DocType_ID());
				if(!inv.save(get_Trx()))
					return -1;
				inventory.add(inv);
			}
			for(MLocator locator:locators)
			{
				if(!locator.isActive())
					continue;
				
				StringBuffer sql = new StringBuffer(
						  " SELECT p.M_Product_ID, COALESCE(s.M_Locator_ID,?), " 
						+ " COALESCE(s.M_AttributeSetInstance_ID,0),COALESCE(s.Qty,0), "
						+ " p.M_AttributeSet_ID,p.Name "
						+ " FROM M_Product p"
						+ " LEFT OUTER JOIN M_StorageDetail s ON (s.M_Product_ID=p.M_Product_ID "
						                                + " AND s.M_Locator_ID = ? "
						                                + " AND s.QtyType = 'H') "
						+ " LEFT OUTER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID "
						                                + " AND l.M_Warehouse_ID=? "
						                                + " AND l.M_Locator_ID = ?) "
						+ " WHERE p.M_Product_ID = ? "
						+ " AND p.IsStocked='Y' "
						+ " AND p.IsSummary = 'N' "
						+ " AND p.ProductType='I'");
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement (sql.toString(), get_Trx());
					pstmt.setInt(1,locator.getM_Locator_ID());
					pstmt.setInt(2,locator.getM_Locator_ID());
					pstmt.setInt(3, p_M_Warehouse_ID);
					pstmt.setInt(4, locator.getM_Locator_ID());
					pstmt.setInt(5,product.getM_Product_ID());
					rs = pstmt.executeQuery ();
					while (rs.next ())
					{
						mProduct.add(rs.getInt(1));
						mLocator.add(rs.getInt(2));
						mAttributeSetInstance.add(rs.getInt(3));
						qty.add(rs.getBigDecimal(4));
						mAttributeSet.add(rs.getInt(5));
						pName=rs.getString(6);							
					}
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
			}
			if(!createInventoryLine(inv, product,mLocator, mProduct, 
                    mAttributeSetInstance,qty, mAttributeSet))
			{
				log.log(Level.SEVERE,"Unable to Create Physical Inventory Line");
				return -1;
			}
			product.setProcessing(true);
			addLog(0, null, new BigDecimal(inv.getDocumentNo()), "Rank Line '" 
					+ line.getABCRank() + "' - Product '"+pName+"'");
			count++;
			
			mProduct.clear();
			mLocator.clear();
			mAttributeSet.clear();
			mAttributeSetInstance.clear();
			qty.clear();
			prodForCommit.add(product);
			if(prodForCommit.size()%commitRecord==0)
			{
				if(!PO.saveAll(get_Trx(), prodForCommit))
				{
					log.log(Level.SEVERE,"Unable to Update ABC Product Assignment");
					return -1;
				}
				prodForCommit.clear();
			}
				
		}
		if(!PO.saveAll(get_Trx(), prodForCommit))
		{
			log.log(Level.SEVERE,"Unable to Update ABC Product Assignment");
			return -1;
		}
	    log.log(Level.FINE,"Created Cycle count request for "+count+" products in rank line '"
	    		+line.getABCRank()+"'");
		return count;
	}
	
	/**
	 * 	Create/Add to Inventory Line
	 *	@param M_Product_ID product
	 *	@param M_Locator_ID locator
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param QtyOnHand qty
	 *	@param M_AttributeSet_ID as
	 *	@return lines added
	 */
	private boolean createInventoryLine (MInventory m_inventory, MABCProductAssignment product, 
			ArrayList<Integer> mLocator,ArrayList<Integer> mProduct,
			ArrayList<Integer> mAttributeSetInstance,ArrayList<BigDecimal> qty, 
			ArrayList<Integer> mAttributeSet)
	{
		boolean oneLinePerASI = false;
		ArrayList<MInventoryLine> lines = new ArrayList<MInventoryLine>();
		ArrayList<MInventoryLineMA> linesMA = new ArrayList<MInventoryLineMA>();
		
		for(int i = 0;i<mLocator.size();i++)
		{
			int M_Locator_ID = mLocator.get(i);
		    int M_Product_ID = mProduct.get(i);
		    int M_AttributeSetInstance_ID = mAttributeSetInstance.get(i);
		    BigDecimal QtyOnHand = qty.get(i);
		    int M_AttributeSet_ID = mAttributeSet.get(i);
		    
			if (M_AttributeSet_ID != 0)
			{
				MAttributeSet mas = MAttributeSet.get(getCtx(), M_AttributeSet_ID);
				oneLinePerASI = mas.isInstanceAttribute();
			}
			if (oneLinePerASI)
			{
				MInventoryLine line = new MInventoryLine (m_inventory, M_Locator_ID, 
					M_Product_ID, M_AttributeSetInstance_ID, 
					QtyOnHand, QtyOnHand);		//	book/count
				line.setM_ABCAnalysisGroup_ID(product.getM_ABCAnalysisGroup_ID());
				//need to save once to get the correct line number
				if(!line.save(get_Trx()))
					return false;
				lines.add(line);
				continue;
			}
			
			if (QtyOnHand.signum() == 0)
				M_AttributeSetInstance_ID = 0;
			
			if (m_line != null 
				&& m_line.getM_Locator_ID() == M_Locator_ID
				&& m_line.getM_Product_ID() == M_Product_ID)
			{
				if (QtyOnHand.signum() == 0)
					continue;
				//	Same ASI (usually 0)
				if (m_line.getM_AttributeSetInstance_ID() == M_AttributeSetInstance_ID)
				{
					m_line.setQtyBook(m_line.getQtyBook().add(QtyOnHand));
					m_line.setQtyCount(m_line.getQtyCount().add(QtyOnHand));
					
					if(lines.indexOf(m_line)==-1)
						lines.add(m_line);
					else 
					{
						lines.remove(lines.indexOf(m_line));
						lines.add(m_line);
					}
					continue;
				}
				//	Save Old Line info
				else if (m_line.getM_AttributeSetInstance_ID() != 0)
				{
					MInventoryLineMA ma = new MInventoryLineMA (m_line, 
						m_line.getM_AttributeSetInstance_ID(), m_line.getQtyBook());
					linesMA.add(ma);
						continue;
				}
				m_line.setM_AttributeSetInstance_ID(0);
				m_line.setQtyBook(m_line.getQtyBook().add(QtyOnHand));
				m_line.setQtyCount(m_line.getQtyCount().add(QtyOnHand));
				if(lines.indexOf(m_line)==-1)
					lines.add(m_line);
				else 
				{
					lines.remove(lines.indexOf(m_line));
					lines.add(m_line);
				}
				//
				MInventoryLineMA ma = new MInventoryLineMA (m_line, 
					M_AttributeSetInstance_ID, QtyOnHand);
				linesMA.add(ma);
				continue;
			}
			//	new line
			m_line = new MInventoryLine (m_inventory, M_Locator_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				QtyOnHand, QtyOnHand);		//	book/count
			m_line.setM_ABCAnalysisGroup_ID(product.getM_ABCAnalysisGroup_ID());
			
			//need to save line once to get the Line ID.
			if(!m_line.save(get_Trx()))
				return false;
			if(lines.indexOf(m_line)==-1)
				lines.add(m_line);
			else 
			{
				lines.remove(lines.indexOf(m_line));
				lines.add(m_line);
			}			
		}
		
		//first save the MA Lines
		if(!PO.saveAll(get_Trx(),linesMA))
			return false;
		if(!PO.saveAll(get_Trx(), lines))
			return false;
		
		return true;
	}	//	createInventoryLine
}