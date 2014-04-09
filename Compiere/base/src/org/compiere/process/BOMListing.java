/**
 * 
 */
package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MBOM;
import org.compiere.model.MBOMProduct;
import org.compiere.model.MProduct;
import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.QueryUtil;

/**
 * @author Prashanth
 *
 */
public class BOMListing extends SvrProcess {

	/** Product         */
	private int         p_M_Product_ID = 0;
	/** Org */
	private int         p_AD_Org_ID = 0;
	/** Product Category          */
	private int         p_M_Product_Category_ID = 0;
	/** BOMUse              */
	private String      p_BOMUse = null;
	/** BOM Type                */
	private String      p_BOMType = null;
	/** Level */
	private int         p_Level = 0;

	private StringBuffer whereClause = new StringBuffer();
	Integer rootID = null;

	private ArrayList<Integer> bomCheckProductIDs = null;

	private ArrayList<Integer> productIDs = new ArrayList<Integer>();
	public BOMListing() {

	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		log.info("M_Product_ID=" +  p_M_Product_ID
				+ ",M_Product_Category_ID=" + p_M_Product_Category_ID
				+ ",BOMUse=" + p_BOMUse
				+ ",BOMType=" + p_BOMType);		


		//  Delete (just to be sure)
		StringBuffer sql = new StringBuffer ("DELETE FROM T_BOMDetails WHERE AD_PInstance_ID=");
		sql.append(getAD_PInstance_ID());		
		DB.executeUpdate(get_TrxName(), sql.toString());

		//set the whereClause.
		if ( p_BOMUse!=null || p_BOMType!=null )
		{
			if(p_BOMUse!=null)
			{
				whereClause.append(" BOMUse = '" + p_BOMUse+"'");
			}
			if(p_BOMType!=null )
			{
				if(p_BOMUse!= null)
				{
					whereClause.append(" AND BOMType = '"+p_BOMType+"'");
				}
				else
				{
					whereClause.append(" BOMType = '"+p_BOMType+"'");
				}
			}

		}

		String stmt = null;

		//get the list of products
		if(p_M_Product_ID!=0)
		{
			productIDs.add(Integer.valueOf(p_M_Product_ID));
		}		
		else if(p_M_Product_Category_ID!=0)
		{
			String sqlCategory = new String (" SELECT M_Product_ID FROM M_Product WHERE M_Product_Category_ID = ? " +
			"AND IsActive='Y' AND IsBOM='Y' ORDER BY Name");

			MRole role = MRole.getDefault(getCtx(), false);
			stmt = role.addAccessSQL(sqlCategory, "M_Product", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		}
		else
		{
			//both product and product category not mentioned.
			String sqlGeneral = new String (" SELECT M_Product.M_Product_ID FROM M_Product INNER JOIN " +
					"M_BOM ON ( M_Product.M_Product_ID = M_BOM.M_Product_ID ) WHERE M_Product.IsActive='Y' AND IsBOM='Y' ");
			if(p_AD_Org_ID!=0)
				sqlGeneral = sqlGeneral.concat(" AND M_Product.AD_Org_ID = ? ");
			if(whereClause.length() > 0)
				sqlGeneral = sqlGeneral.concat(" AND ").concat(whereClause.toString());				
			sqlGeneral = sqlGeneral.concat(" ORDER BY M_Product.Name");

			MRole role = MRole.getDefault(getCtx(), false);
			stmt = role.addAccessSQL(sqlGeneral, "M_Product", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		}
		if(stmt!=null)
		{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(stmt,get_Trx());
				if(p_M_Product_Category_ID!=0)
					pstmt.setInt(1,p_M_Product_Category_ID);
				else if(p_AD_Org_ID !=0)
					pstmt.setInt(1, p_AD_Org_ID);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					if(!productIDs.contains(rs.getInt(1)))						
					productIDs.add(rs.getInt(1));
				}
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql.toString(), e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}//if
		//now work with the products in the list, insert into the T_BOMDetails table		

		for(Integer productID :  productIDs )
		{
			MBOM[] boms = null;
			if(whereClause.length()== 0)
			{
				boms = MBOM.getOfProduct(getCtx(), productID.intValue(), get_Trx(), null);
			}
			else
			{
				boms = MBOM.getOfProduct(getCtx(), productID.intValue(), get_Trx(), whereClause.toString());	
			}

			for(MBOM bom: boms )
			{
				rootID = null;
				Integer parentID = addToT_BomDetails(productID, bom.getM_BOM_ID(), 0 , 0 , 1);
				rootID = parentID;			
				if(p_Level >= 0)
					addProductBOMDetails(bom , 0, parentID );
			}
		}


		return null;
	}

	/**
	 * Insert into the T_BOMDetails table
	 * @param productID Product
	 * @param bomID Bom ID
	 * @param level Level Number
	 * @param parentID Parent
	 * @param qty Quantity
	 * @return
	 */
	private int addToT_BomDetails(Integer productID, int bomID, int level, Integer parentID, int qty) {

		int T_BOMDetails_ID = QueryUtil.getSQLValue(get_Trx(), "SELECT T_BOMDetails_ID_Seq.NEXTVAL FROM DUAL");
		Integer BomID = null;		
		if(parentID.intValue() == 0)
		{
			parentID = null;
		}		
		if(bomID!=0)
		{
			BomID = bomID;
		}
		if(level == 0)
			level =  p_Level;
		MProduct product = MProduct.get(getCtx(), productID);
		StringBuffer sql = new StringBuffer ("INSERT INTO T_BOMDetails "
				+ "(AD_PInstance_ID, M_Product_ID, M_BOM_ID, LevelNo , T_BOMDetails_ID,"
				+ " AD_Client_ID, AD_Org_ID, Qty, T_RootBOMDetails_ID, T_ParentBOMDetails_ID) "
				+ "VALUES ( ?,?,?,?,?,?,?,?,?,? )");
		Object[] params = new Object[]{getAD_PInstance_ID(),
				                       productID,
				                       BomID,
				                       level,
				                       T_BOMDetails_ID,
				                       product.getAD_Client_ID(),
				                       p_AD_Org_ID,
				                       qty,
				                       rootID,
				                       parentID };
		DB.executeUpdate(get_Trx(), sql.toString(),params);
		return T_BOMDetails_ID;
	}

	/**
	 * Add the Product and BOM details to T_BOMDetails table
	 * @param bom BOM
	 * @param level Level
	 * @param product Product
	 */
	private void addProductBOMDetails(MBOM bom, int level, Integer parentID) {


		if(bom == null)
			return;
		if(level == 0)
		{
			//check the BOM Valididty
			bomCheckProductIDs = new ArrayList<Integer>();
			bomCheckProductIDs.add(bom.getM_Product_ID());
		}
		MBOMProduct[] BOMProducts = MBOMProduct.getBOMLinesOrderByProductName(bom, true);
		if (BOMProducts.length == 0)
		{		
			return ;
		}
		for (MBOMProduct BOMProduct : BOMProducts) 
		{
			int compParentID = addToT_BomDetails(BOMProduct.getM_ProductBOM_ID(), BOMProduct.getM_ProductBOMVersion_ID(), level + 1, parentID, BOMProduct.getBOMQty().intValue());
			//check if this component is a BOM and is verified
			MProduct compProduct = MProduct.get(getCtx(), BOMProduct.getM_ProductBOM_ID());

			if(compProduct.isBOM()  && ((p_Level > ( level + 1) ) || p_Level==0))
			{	
				if(bomCheckProductIDs.contains(compProduct.getM_Product_ID()))
				{
					log.warning (compProduct.getName() + ": " + compProduct.getName() + " is recursively included.");
					continue;				
				}	
				else
				{
					bomCheckProductIDs.add(compProduct.getM_Product_ID());
				}
				MBOM compBOM = null;
				//check if a component BOM is mentioned.
				if(BOMProduct.getM_ProductBOMVersion_ID()!= 0)
					compBOM = MBOM.get(getCtx(), BOMProduct.getM_ProductBOMVersion_ID());
				if(compBOM!=null)
				{
					addProductBOMDetails(compBOM, level + 1, compParentID);
				}
				else
				{
					//check if the same Type and Use component BOM exists.
					String restriction = "BOMType='" + bom.getBOMType() + "' AND BOMUse='" + bom.getBOMUse() + "'" + " AND IsActive = 'Y' ";
					MBOM[] boms = MBOM.getOfProduct(getCtx(), compProduct.getM_Product_ID(), get_Trx(),
							restriction);
					if (boms.length != 0)
					{
						addProductBOMDetails(boms[0], level + 1, compParentID);
					}

				}
			}			
		}//for
	}



	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = element.getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = element.getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = element.getParameterAsInt();
			else if (name.equals("BOMUse"))
				p_BOMUse = (String)element.getParameter();
			else if (name.equals("BOMType"))
				p_BOMType = (String)element.getParameter();	
			else if(name.equals("LevelNo"))
				p_Level = element.getParameterAsInt();
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

}
