/**
Copyright 1999-2009 Compiere, Inc.
$Id$
 **/


package org.compiere.util;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.compiere.framework.PO;
import org.compiere.model.MBOM;
import org.compiere.model.MBOMAlternative;
import org.compiere.model.MBOMProduct;
import org.compiere.model.MProduct;
import org.compiere.model.X_M_BOMProduct;
import org.compiere.model.X_M_Product;
import org.compiere.vos.ProductInfo;
/**
 * @author rvodapalli
 *
 */
public class BOMDropLines {	
	private int		p_M_WorkOrder_ID = 0;
	private PO m_workorderoperation;
	static CLogger log = CLogger.getCLogger(BOMDropLines.class);
	public ArrayList<ProductInfo> productList = new ArrayList<ProductInfo>();	
	
	// recursive is true, if the BOMcomponent itself is a BOM
	private boolean recursive = false;	

	// Operations that are Processed
	private ArrayList<Integer> OpProcessed = new ArrayList<Integer>();

	// Dummy Operation
	private PO dummyOp;	

	//OperationSequenceNo of a BOM Component (used in EXPLODE BOM window)
	private int BOMOperationSeqNo = 0;
	private String m_processMsg="BOM Lines not copied";

	public String getM_processMsg() {
		return m_processMsg;
	}

	public void setM_processMsg(String msg) {
		m_processMsg = msg;
	}

	//Called when user clicks on copy OperationalRequirements button from the workorder
	public boolean addBOMLines ( Ctx ctx, Trx trx, int productID, 
			BigDecimal qty, int bomID, String bomType, String bomUse, int workorderID, boolean isprocesscalled){
		return addBOMLines (ctx, trx, productID, qty, bomID, bomType, bomUse, workorderID, isprocesscalled, -1, -1, false);
	}	

	/**
	 * @param ctx
	 * @param trx
	 * @param productID
	 * @param qty
	 * @param bomID
	 * @param bomType
	 * @param bomUse
	 * @param workorderID
	 * @param isprocesscalled
	 * @param level
	 * @param lineNo
	 * @param loadComponents
	 * @return
	 */
	public boolean addBOMLines ( Ctx ctx, Trx trx, int productID, 
			BigDecimal qty, int bomID, String bomType, String bomUse, int workorderID, boolean isprocesscalled, int level, int lineNo, boolean loadComponents)
	{		
		boolean loadBOMComponents = loadComponents;		
		if(level == 0 && lineNo == 0){
			//  Called from Explode BOM window		
			loadBOMComponents = true;		
		}	
		p_M_WorkOrder_ID = workorderID;		
		MProduct product = new MProduct(ctx, productID, trx);
		MBOM bom = new MBOM(ctx, bomID, trx);		
		PO workorder = getWorkOrderInstance(ctx, workorderID, trx);

		// If the Component BOM is not specified, look for a BOM with the same 
		// BOM Type and Use as the parent product
		MBOMProduct[] bomLines;		
		if (bomID == 0)
		{
			log.fine(product.toString());
			bomLines = MBOMProduct.getBOMLines(product, bomType, bomUse);
		}
		else
		{
			log.fine(bom.toString());
			bomLines = MBOMProduct.getBOMLines(bom);
			bomType = bom.getBOMType();
			bomUse = bom.getBOMUse();
		}
		//  Get all the Components used to Explode a BOM
		if(loadComponents == true)
			for (MBOMProduct bomLine : bomLines) {	
				if(workorderID!= 0)
				{
					MProduct componentProduct = new MProduct(ctx, bomLine.getM_ProductBOM_ID(), null);
					if(!componentProduct.getProductType().equals(X_M_Product.PRODUCTTYPE_Item))
					{
						log.fine(product.getName() + ": " + componentProduct.getName()+"'s ProductType is not Item. Products of type Item are only processed");	
						continue;
					}
				}
				addBOMLine (ctx, trx, bomLine, qty, bomType, bomUse, workorder, isprocesscalled, level, lineNo, loadBOMComponents);
			}
		else
		{
			int seqNoMatch = 0;
			PO woo = getWorkOrderOperationInstance2(ctx, 0, trx);
			Class<?> wooClass = getWorkOrderOperationClass();
			try {
				// getting the WorkOrder operations
				Method getOfWO = wooClass.getMethod("getOfWorkOrder", new Class[] {getWorkOrderClass(), String.class, String.class});	
				PO[] woos = (PO[]) getOfWO.invoke(woo, workorder, null, "SeqNo");					
				for (int i = 0; i < bomLines.length; i++)
				{	
					MProduct componentProduct = new MProduct(ctx, bomLines[i].getM_ProductBOM_ID(), null);
					if(!componentProduct.getProductType().equals(X_M_Product.PRODUCTTYPE_Item))
					{
						log.fine(product.getName() + ": " + componentProduct.getName()+"'s ProductType is not Item. Products of type Item are only processed");					 
						continue;
					}
					// If the BOMcomponent itself is a BOM then, all the child components of this product are included under the operation 
					// where the product(BOMcomponent) has to be included 
					if(recursive == true)
					{				
						m_workorderoperation = dummyOp;
						if (!(addBOMLine (ctx, trx, bomLines[i], qty, bomType, bomUse, workorder, isprocesscalled, level, lineNo, loadBOMComponents)))
							return false;				
						continue;
					}		
					recursive = false;
					MBOMProduct newbomproduct = bomLines[i];			
					seqNoMatch = 0;
					for (PO woop : woos)
					{ 						
						int WOseqNo = 0;
						Method getSeqNo = wooClass.getMethod("getSeqNo", new Class[] {});	
						WOseqNo =  (Integer) getSeqNo.invoke(woop);
						
						// Each component's Operation sequence number is compared with the sequence numbers of operations 
						// Once they are matched, the component gets copied under that operation
						if(  bomLines[i].getOperationSeqNo()== WOseqNo)
						{				 
							seqNoMatch++;
							m_workorderoperation = woop;
							dummyOp = woop;
							//If the component under the operation sequence exists � update the new quantity on the component(No other fields are updated)
							PO woc = getWorkOrderComponentInstance2(ctx, 0, trx);
							Class<?> wocClass = getWorkOrderComponentClass();
							Method getOfWOO = wocClass.getMethod("getOfWorkOrderOperation", new Class[] {getWorkOrderOperationClass(), String.class, String.class});	
							PO[] wocs = (PO[]) getOfWOO.invoke(woc, woop, " M_Product_ID = " + bomLines[i].getM_ProductBOM_ID(), null);
							if(wocs.length > 0){
								Method SetQuantity = wocClass.getMethod("setQtyRequired", new Class[] { BigDecimal.class });
								Method GetQuantity = wocClass.getMethod("getQtyRequired", new Class[] {});
								SetQuantity.invoke(wocs[0], bomLines[i].getBOMQty().add((BigDecimal) GetQuantity.invoke(wocs[0])));	
								if (!wocs[0].save())					
								{							
									log.saveError("Error", "Work Order Component NOT updated");
									return false;
								}
								if(!OpProcessed.contains(WOseqNo))
									OpProcessed.add(WOseqNo);
							}						
							else {
								if (!(addBOMLine (ctx, trx, bomLines[i], qty, bomType, bomUse, workorder, isprocesscalled, level, lineNo, loadBOMComponents)))
									return false;
								if(!OpProcessed.contains(WOseqNo))
									OpProcessed.add(WOseqNo);
							}
							break;
						}	
						recursive = false;
					}
					//If there is no operation with the specified BOMComponent's OperationSequenceNumber, a dummy operation is created.
					if(seqNoMatch > 0)
						continue;
					else
					{	
						// Default UOM to Day		
						String sql = "SELECT C_UOM_ID FROM C_UOM WHERE Name LIKE ?";
						int uomID = QueryUtil.getSQLValue(null, sql, "Day");

						PO woop = getWorkOrderOperationInstance(ctx,
								trx,
								workorderID, 							
								100, 	// This is the Default Operation Key-Name pair seeded as System Data
								0, 
								uomID, 
								BigDecimal.ZERO, 
								BigDecimal.ZERO,					 
								null,					
								newbomproduct.getOperationSeqNo(), 
								true,
								false,
								false,
								false,
								isprocesscalled);		
						if(woop == null){							
							setM_processMsg( "There are no WorkCenters defined for the warehouse that is provided.Create a WorkCenter to proceed." );
							log.saveError("Error", "Work Order Operation NOT created");
							return false;
							
						}

						if (!woop.save())					
						{							
							log.saveError("Error", "Work Order Operation NOT created");
							return false;
						}	
						if(!OpProcessed.contains(newbomproduct.getOperationSeqNo()))
							OpProcessed.add(newbomproduct.getOperationSeqNo());					
						Method getOfWO2 = wooClass.getMethod("getOfWorkOrder", new Class[] {getWorkOrderClass(), String.class, String.class});	
						woos = (PO[])getOfWO2.invoke(woo, workorder, null, "SeqNo");
						m_workorderoperation = woop;
						dummyOp = m_workorderoperation;
						addBOMLine (ctx, trx, newbomproduct, qty, bomType, bomUse, workorder, isprocesscalled, level, lineNo, loadBOMComponents);
					}
				}
			}
			catch (NoSuchMethodException e) {				
				e.printStackTrace();
			} catch (IllegalArgumentException e) {				
				e.printStackTrace();
			} catch (InvocationTargetException e) {				
				e.printStackTrace();
			} catch (IllegalAccessException e) {				
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Traverse bom components recursively
	 * @param explodeBOM 
	 */
	private boolean addBOMLine ( Ctx ctx, Trx trx, MBOMProduct line, 
			BigDecimal qty, String bomType, String bomUse,PO workorder, boolean isprocesscalled, int level, int lineNo, boolean loadBOMComponents)
	{		
		log.fine(line.toString());
		String bomProductType = line.getBOMProductType();
		if (bomProductType == null)
			bomProductType = X_M_BOMProduct.BOMPRODUCTTYPE_StandardProduct;	

		MProduct componentProduct = line.getComponent();
		if (componentProduct == null)
			return false;
		BigDecimal lineQty = line.getBOMQty().multiply(qty);

		// Only explode BOM further if the component is not stocked
		if (!(componentProduct.isStocked()) && componentProduct.isBOM() && componentProduct.isVerified())
		{
			BOMOperationSeqNo = line.getOperationSeqNo();
			recursive = true;
			if (line.getM_ProductBOMVersion_ID() == 0)
			{			
				if (!(addBOMLines (ctx, trx, componentProduct.get_ID(), lineQty, 0, bomType, bomUse, p_M_WorkOrder_ID, isprocesscalled, level+1, lineNo, loadBOMComponents)))		//	recursive
					return false;
			}
			else
			{
				MBOM componentBOM = new MBOM(ctx, line.getM_ProductBOMVersion_ID(), trx);			
				if (!(addBOMLines (ctx, trx,   line.getM_BOMProduct_ID(), lineQty, componentBOM.getM_BOM_ID(), 
						componentBOM.getBOMType(), componentBOM.getBOMUse(), p_M_WorkOrder_ID, isprocesscalled, level+1, lineNo, loadBOMComponents)))
					return false;
			}
			if(loadBOMComponents)
				recursive = false;
		}
		else
		{	
			if( loadBOMComponents == false)		
				addComponent (m_workorderoperation, componentProduct, workorder, lineQty, line.getSupplyType());
			// Get the line details (used for exploding a BOM) 
			else
			{				
				String fieldIdentifier = null;
				String groupName = null;
				String levelStr = String.valueOf(level);
				if (X_M_BOMProduct.BOMPRODUCTTYPE_Alternative.equals(bomProductType) 
						|| X_M_BOMProduct.BOMPRODUCTTYPE_AlternativeDefault.equals(bomProductType) )
				{
					MBOMAlternative group = new MBOMAlternative(ctx, line.getM_BOMAlternative_ID(), null);
					groupName = group.getName();
					fieldIdentifier = groupName + "_" + lineNo + "_" + levelStr;
				}
				else
					fieldIdentifier = line.getLine() + "_" + lineNo + "_" + levelStr;

				//If the BOMcomponent itself is a BOM then, all the child components of this product are included under the operation 
				// where the product(BOMcomponent) has to be included
				// Here we are passing the BOMProduct's operation sequenceNo instead of the components Operation SequenceNo
				if(recursive)
					addDisplay (ctx, line.getBOM().getM_Product_ID(), componentProduct.getM_Product_ID(), 
							bomProductType, componentProduct.getName(), lineQty, groupName,
							line.getSupplyType(), BOMOperationSeqNo, fieldIdentifier);
				else
					addDisplay (ctx, line.getBOM().getM_Product_ID(), componentProduct.getM_Product_ID(), 
							bomProductType, componentProduct.getName(), lineQty, groupName,
							line.getSupplyType(), line.getOperationSeqNo(), fieldIdentifier);
			}
		}
		return true;
	}	//	addBOMLine


	/**
	 * Add BOM components to Work Order Components
	 *
	 */
	private boolean addComponent (PO workorderoperation, MProduct product,PO workorder, BigDecimal lineQty, String supplyType)
	{
		log.fine("addComponent: Product=" + product.toString());

		PO woc = getWorkOrderComponentInstance(workorder, workorderoperation, product, lineQty,
				supplyType);				
		if (woc.save())
		{			
			return true;
		}
		else
		{
			log.saveError("Error", "Work Order Component NOT created");
			return false;
		}
	}	//	addComponent

	/**
	 * Add leaf level component to list of Products
	 *
	 */
	// Components are added to the ProductList (used to load the components in Explode BOM window)  
	private void addDisplay (Ctx ctx,int parentM_Product_ID,
			int M_Product_ID, String bomProductType, String name, BigDecimal lineQty, String groupName,
			String supplyType, int OperationSeqNo, String fieldIdentifier)
	{
		log.fine("M_Product_ID=" + M_Product_ID + ",Type=" + bomProductType + ",Name=" + name + ",Qty=" + lineQty);
		//

		if (X_M_BOMProduct.BOMPRODUCTTYPE_StandardProduct.equals(bomProductType))
		{			
			String title = Msg.getMsg(ctx, "Standard");
			productList.add(new ProductInfo(M_Product_ID,name,title,null,bomProductType,lineQty.toString(),
					supplyType, OperationSeqNo, fieldIdentifier));
		}
		else if (X_M_BOMProduct.BOMPRODUCTTYPE_OptionalProduct.equals(bomProductType))
		{
			String title = Msg.getMsg(ctx, "Optional"); 
			productList.add(new ProductInfo(M_Product_ID,name,title,null,bomProductType,lineQty.toString(),
					supplyType, OperationSeqNo, fieldIdentifier));
		}
		else	//	Alternative
		{
			String title = groupName;
			productList.add(new ProductInfo(M_Product_ID,name,title,groupName,bomProductType,lineQty.toString(),
					supplyType, OperationSeqNo, fieldIdentifier));
		}

	}	//	addDisplay


	/**
	 * @return
	 */
	public ArrayList<ProductInfo> getProductList ()
	{
		return productList;
	}

	/**
	 * @param 
	 * @return Operations Processed
	 */
	public ArrayList<Integer> operationsProcessed()
	{
		return OpProcessed;
	}// Total number of operations that are copied 


	/**
	 * 	Get MWorkOrder class
	 *	@return class or null
	 */
	private Class<?> getWorkOrderClass()
	{
		String className = "org.compiere.cmfg.model.MWorkOrder";
		try
		{
			Class<?> clazz = Class.forName(className);
			return clazz;
		}
		catch (Exception e)
		{
			log.warning("Error getting class for " + className+ ": - " + e.toString());
		}
		return null;
	}	//	getWorkOrderClass

	/**
	 * 	Get MWorkOrderComponent class
	 *	@return class or null
	 */
	private Class<?> getWorkOrderComponentClass()
	{
		String className = "org.compiere.cmfg.model.MWorkOrderComponent";
		try
		{
			Class<?> clazz = Class.forName(className);
			return clazz;
		}
		catch (Exception e)
		{
			log.warning("Error getting class for " + className + ": - " + e.toString());
		}
		return null;
	}	//	getWorkOrderComponentClass

	/**
	 * 	Get MWorkOrder instance
	 *	@param workOrderID work order ID
	 * @param trx 
	 *	@return instance or null
	 */
	private PO getWorkOrderInstance(Ctx ctx, int workOrderID, Trx trx)
	{
		Class<?> clazz = getWorkOrderClass();
		if (clazz == null)
			return null;

		try
		{
			Constructor<?> constr = clazz.getConstructor(Ctx.class, int.class, Trx.class);
			PO retValue = (PO)constr.newInstance(ctx, workOrderID, trx);
			return retValue;
		}
		catch (Exception e)
		{
			log.warning("Error instantiating constructor for org.compiere.cmfg.model.MWorkOrder:" + e.toString());
		}
		return null;
	}	//	getWorkOrderInstance


	/**
	 * @param workorder
	 * @param workorderOp
	 * @param componentProduct
	 * @param componentQuantity
	 * @param componentSupplyType
	 * @return
	 */
	private PO getWorkOrderComponentInstance(PO workorder, PO workorderOp, MProduct componentProduct, BigDecimal componentQuantity,
			String componentSupplyType)
	{
		Class<?> clazz = getWorkOrderComponentClass();
		if (clazz == null)
			return null;
		try
		{
			Class<?> MWorkOrderClass = getWorkOrderClass();
			Class<?> MWorkOrderOperationClass = getWorkOrderOperationClass();

			Constructor<?> constr = clazz.getConstructor(MWorkOrderClass, MWorkOrderOperationClass, MProduct.class, BigDecimal.class, String.class);
			PO retValue = (PO)constr.newInstance(MWorkOrderClass.cast(workorder), MWorkOrderOperationClass.cast(workorderOp), componentProduct, componentQuantity,
					componentSupplyType);				
			return retValue;
		}
		catch (Exception e)
		{
			log.warning("Error instantiating constructor for org.compiere.cmfg.model.MWorkOrderComponent:" + e.toString());
		}
		return null;
	}     //getWorkOrderComponentInstance

	private PO getWorkOrderComponentInstance2(Ctx ctx, int M_WorkOrderComponent_ID, Trx trx) {

		Class<?> clazz = getWorkOrderComponentClass();
		if (clazz == null)
			return null;

		try
		{
			Constructor<?> constr = clazz.getConstructor(Ctx.class, int.class, Trx.class);	
			PO retValue = (PO) constr.newInstance( ctx, M_WorkOrderComponent_ID, trx);

			return retValue;
		}
		catch (Exception e)
		{
			log.warning("Error instantiating constructor for org.compiere.cmfg.model.MWorkOrderComponent:" + e.toString());
		}
		return null;
	}		

	/**
	 * @return
	 */
	private Class<?> getWorkOrderOperationClass()
	{
		String className = "org.compiere.cmfg.model.MWorkOrderOperation";
		try
		{
			Class<?> clazz = Class.forName(className);
			return clazz;
		}
		catch (Exception e)
		{
			log.warning("Error getting class for " + className + ": - " + e.toString());
		}
		return null;
	}	
	/**
	 * @param ctx
	 * @param trx
	 * @param M_WorkOrder_ID
	 * @param M_Operation_ID
	 * @param M_StandardOperation_ID
	 * @param C_UOM_ID
	 * @param SetupTime
	 * @param UnitRuntime
	 * @param Description
	 * @param SeqNo
	 * @param isActive
	 * @param IsHazmat
	 * @param IsPermitRequired
	 * @param IsOptional
	 * @param isprocesscalled
	 * @return
	 */
	private PO getWorkOrderOperationInstance(Ctx ctx, Trx trx, int M_WorkOrder_ID, int M_Operation_ID, int M_StandardOperation_ID, int C_UOM_ID, BigDecimal SetupTime,
			BigDecimal UnitRuntime, String Description, int SeqNo, boolean isActive, boolean IsHazmat, boolean IsPermitRequired, boolean IsOptional, boolean isprocesscalled)
	{
		Class<?> clazz = getWorkOrderOperationClass();
		if (clazz == null)
			return null;

		try
		{
			Constructor<?> constr = clazz.getConstructor(Ctx.class,Trx.class, int.class, int.class, int.class, int.class, BigDecimal.class, 
					BigDecimal.class, String.class,	int.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);	
			PO retValue = (PO) constr.newInstance( ctx, trx, M_WorkOrder_ID, M_Operation_ID, M_StandardOperation_ID, C_UOM_ID, SetupTime,
					UnitRuntime, Description, SeqNo, isActive, IsHazmat, IsPermitRequired, IsOptional, isprocesscalled);

			return retValue;
		}
		catch (Exception e)
		{
			log.warning("Error instantiating constructor for org.compiere.cmfg.model.MWorkOrderOperation:" + e.toString());
		}
		return null;
	}

	/**
	 * @param ctx
	 * @param M_WorkOrderOperation_ID
	 * @param trx
	 * @return
	 */
	private PO getWorkOrderOperationInstance2(Ctx ctx, int M_WorkOrderOperation_ID, Trx trx) {

		Class<?> clazz = getWorkOrderOperationClass();
		if (clazz == null)
			return null;

		try
		{
			Constructor<?> constr = clazz.getConstructor(Ctx.class, int.class, Trx.class);	
			PO retValue = (PO) constr.newInstance( ctx, M_WorkOrderOperation_ID, trx);

			return retValue;
		}
		catch (Exception e)
		{
			log.warning("Error instantiating constructor for org.compiere.cmfg.model.MWorkOrderOperation:" + e.toString());
		}
		return null;
	}		
}

