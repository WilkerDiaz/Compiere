package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.common.CompiereStateException;
import org.compiere.framework.PO;
import org.compiere.model.MLocator;
import org.compiere.model.MPInstance;
import org.compiere.model.MProductLocator;
import org.compiere.model.X_I_Locator;
import org.compiere.util.CLogMgt;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Login;

public class ImportLocator extends SvrProcess {

	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Organization to be imported to		*/
	private int				m_AD_Org_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;
	
	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=? " ;	

	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	@Override
	protected String doIt() throws Exception {
		String sql = null;
		int no = 0;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql =  "DELETE FROM I_Locator "
				  + "WHERE I_IsImported='Y'"
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
			log.fine("Delete Old Impored =" + no);
		}

		//Set Client from Key
		sql =  "UPDATE I_Locator l"
				  + " SET AD_Client_ID = (SELECT AD_Client_ID FROM AD_Client c WHERE c.Value = l.ClientValue), " 
				  +	" ClientName = (SELECT Name FROM AD_Client c WHERE c.Value = l.ClientValue), "
				  + " Updated = COALESCE (Updated, SysDate),"
				  + " UpdatedBy = COALESCE (UpdatedBy, 0)"
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				  + " AND AD_Client_ID is NULL"
				  + " AND ClientValue is NOT NULL";
		no = DB.executeUpdate(get_Trx(), sql);
		log.info ("Set client from key =" + no);

		//	Set Client from Name
		sql =  "UPDATE I_Locator l"
				  + " SET AD_Client_ID = (SELECT AD_Client_ID FROM AD_Client c WHERE c.Name = l.ClientName), "
				  + " ClientValue = (SELECT Value FROM AD_Client c WHERE c.Name = l.ClientName),"
				  + " Updated = COALESCE (Updated, SysDate),"
				  + " UpdatedBy = COALESCE (UpdatedBy, 0)"
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				  + " AND AD_Client_ID is NULL"
				  + " AND ClientName is NOT NULL";
		no = DB.executeUpdate(get_Trx(), sql);
		log.info ("Set client from name =" + no);

		//Set Org from Key
		sql =  "UPDATE I_Locator l"
					  + " SET AD_Org_ID = (SELECT AD_Org_ID FROM AD_Org o WHERE o.Value = l.OrgValue), "
					  + " OrgName = (SELECT Name FROM AD_Org o WHERE o.Value = l.OrgValue), "
					  + " Updated = COALESCE (Updated, SysDate),"
					  + " UpdatedBy = COALESCE (UpdatedBy, 0)"
					  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
					  + " AND (AD_Org_ID is NULL OR AD_Org_ID =0)"
					  + " AND OrgValue is NOT NULL";
		no = DB.executeUpdate(get_Trx(), sql);
		log.info ("Set org from key =" + no);

		//	Set Org from Name
		sql =  "UPDATE I_Locator l"
					  + " SET AD_Org_ID = (SELECT AD_Org_ID FROM AD_Org o WHERE o.Name = l.OrgName), "
					  + " OrgValue = (SELECT Value FROM AD_Org o WHERE o.Name = l.OrgName), "
					  + " Updated = COALESCE (Updated, SysDate),"
					  + " UpdatedBy = COALESCE (UpdatedBy, 0)"
					  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
					  + " AND (AD_Org_ID is NULL OR AD_Org_ID =0)"
					  + " AND OrgName is NOT NULL";
		no = DB.executeUpdate(get_Trx(), sql);
		log.info ("Set Org from name =" + no);


		sql =  "UPDATE I_Locator l"
			  + " SET AD_Client_ID = COALESCE (AD_Client_ID,?),"
			  + " AD_Org_ID = COALESCE (AD_Org_ID,?),"
			  + " IsActive = COALESCE (IsActive, 'Y'),"
			  + " IsDefault = COALESCE (IsDefault, 'N'),"
			  + " IsAvailableToPromise = COALESCE (IsAvailableToPromise, 'Y'),"
			  + " IsAvailableForAllocation = COALESCE (IsAvailableForAllocation, 'Y'),"
			  + " Created = COALESCE (Created, SysDate),"
			  + " CreatedBy = COALESCE (CreatedBy, 0),"
			  + " Updated = COALESCE (Updated, SysDate),"
			  + " UpdatedBy = COALESCE (UpdatedBy, 0),"
			  + " I_ErrorMsg = NULL,"
			  + " I_IsImported = 'N' "
			  + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL";
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID, m_AD_Org_ID });
		log.info ("Reset=" + no);

		String ts = DB.isPostgreSQL() ?	"COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg";  
		sql =  "UPDATE I_Locator l "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Org, '"
				+ "WHERE (AD_Org_ID IS NULL "
				+ " OR NOT EXISTS (SELECT * FROM AD_Org oo WHERE l.AD_Org_ID=oo.AD_Org_ID AND oo.IsSummary='N' AND oo.IsActive='Y'))"
				+ " AND I_IsImported<>'Y'" 
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Org=" + no);

		//Set Warehouse from Key
		sql =  "UPDATE I_Locator l"
				  + " SET M_Warehouse_ID = (SELECT M_Warehouse_ID FROM M_Warehouse w WHERE w.Value = l.WarehouseValue), "
				  + " WarehouseName = (SELECT Name FROM M_Warehouse w WHERE w.Value = l.WarehouseValue), "
				  + " Updated = COALESCE (Updated, SysDate),"
				  + " UpdatedBy = COALESCE (UpdatedBy, 0)"
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				  + " AND (M_Warehouse_ID is NULL OR M_Warehouse_ID =0)"
				  + " AND WarehouseValue is NOT NULL"
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.info ("Set Warehouse from key =" + no);

		//	Set Warehouse from Name
		sql =  "UPDATE I_Locator l"
				  + " SET M_Warehouse_ID = (SELECT M_Warehouse_ID FROM M_Warehouse w WHERE w.Name = l.WarehouseName), "
				  + " WarehouseValue = (SELECT Value FROM M_Warehouse w WHERE w.Name = l.WarehouseName), "
				  + " Updated = COALESCE (Updated, SysDate),"
				  + " UpdatedBy = COALESCE (UpdatedBy, 0)"
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				  + " AND (M_Warehouse_ID is NULL OR M_Warehouse_ID =0)"
				  + " AND WarehouseName is NOT NULL"
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.info ("Set Warehouse from name =" + no);

		sql =  "UPDATE I_Locator l "
				+ " SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Warehouse, '"
				+ " WHERE M_Warehouse_ID IS NULL "
				+ " AND I_IsImported<>'Y'" 
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Warehouse=" + no);

		sql =  "UPDATE I_Locator l "
				+ " SET M_Locator_ID=(SELECT M_Locator_ID FROM M_Locator loc "
				+ " WHERE l.Value=loc.Value " 
				+ " AND l.AD_Client_ID=loc.AD_Client_ID "
				+ " AND l.M_Warehouse_ID=loc.M_Warehouse_ID) "
				+ " WHERE M_Locator_ID IS NULL "
				+ " AND l.Value IS NOT NULL "
				+ " AND I_IsImported<>'Y' "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.info("Locator Existing Value=" + no);

		sql =  "UPDATE I_Locator i "
				+ " SET M_Locator_ID=(SELECT MAX(M_Locator_ID) FROM M_Locator l "
				+ " WHERE i.X=l.X AND i.Y=l.Y AND i.Z=l.Z "
				+ " AND (i.Bin IS NULL OR i.Bin = l.Bin) "
				+ " AND (i.Position IS NULL OR i.Position =l.Position) "
				+ " AND i.M_Warehouse_ID=l.M_Warehouse_ID "
				+ " AND i.AD_Client_ID=l.AD_Client_ID) "
				+ " WHERE M_Locator_ID IS NULL AND X IS NOT NULL AND Y IS NOT NULL AND Z IS NOT NULL "
				+ " AND I_IsImported<>'Y' " 
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Locator from X,Y,Z =" + no);

		sql =  "UPDATE I_Locator l "
				+ " SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Aisle, Bay and Row segments are mandatory' "
				+ " WHERE I_IsImported<>'Y' "
				+ " AND (M_Locator_ID IS NULL OR M_Locator_ID=0) "
				+ " AND (X IS NULL OR Y IS NULL OR Z IS NULL) "
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
			if (no != 0)
				log.warning("Missing Aisle, Bay or Row=" + no);

		//Set Picking UOM from Name
		sql =  "UPDATE I_Locator l "
				  + " SET Picking_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.Name = l.PickingUOMName), "
				  + " PickingUOMSymbol = (SELECT UOMSymbol FROM C_UOM u WHERE u.Name = l.PickingUOMName), "
				  + " Updated = COALESCE (Updated, SysDate), "
				  + " UpdatedBy = COALESCE (UpdatedBy, 0) "
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL) "
				  + " AND (Picking_UOM_ID is NULL OR Picking_UOM_ID =0) "
				  + " AND PickingUOMName is NOT NULL "
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.info ("Set Picking UOM from name =" + no);

		//	Set Picking UOM from Symbol
		sql =  "UPDATE I_Locator l"
				  + " SET Picking_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.UOMSymbol = l.PickingUOMSymbol), "
				  + " PickingUOMName = (SELECT Name FROM C_UOM u WHERE u.UOMSymbol = l.PickingUOMSymbol), "
				  + " Updated = COALESCE (Updated, SysDate), "
				  + " UpdatedBy = COALESCE (UpdatedBy, 0) "
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL) "
				  + " AND (Picking_UOM_ID is NULL OR Picking_UOM_ID =0) "
				  + " AND PickingUOMSymbol is NOT NULL "
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.info ("Set Picking UOM from symbol=" + no);

		sql =  "UPDATE I_Locator l "
				+ " SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Picking UOM, ' "
				+ " WHERE Picking_UOM_ID IS NULL "
				+ " AND (l.PickingUOMName IS NOT NULL OR l.PickingUOMSymbol IS NOT NULL) "
				+ " AND I_IsImported<>'Y' " 
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Picking UOM=" + no);

		//Set Stocking UOM from Name
		sql =  "UPDATE I_Locator l "
				  + " SET Stocking_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.Name = l.StockingUOMName), "
				  + " StockingUOMSymbol = (SELECT UOMSymbol FROM C_UOM u WHERE u.Name = l.StockingUOMName), "
				  + " Updated = COALESCE (Updated, SysDate), "
				  + " UpdatedBy = COALESCE (UpdatedBy, 0) "
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL) "
				  + " AND (Stocking_UOM_ID is NULL OR Stocking_UOM_ID =0) "
				  + " AND StockingUOMName is NOT NULL "
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.info ("Set Stocking UOM from name =" + no);

		//	Set Stocking UOM from Symbol
		sql =  "UPDATE I_Locator l"
				  + " SET Stocking_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.UOMSymbol = l.StockingUOMSymbol), "
				  + " StockingUOMName = (SELECT Name FROM C_UOM u WHERE u.UOMSymbol = l.StockingUOMSymbol), "
				  + " Updated = COALESCE (Updated, SysDate),"
				  + " UpdatedBy = COALESCE (UpdatedBy, 0)"
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				  + " AND (Stocking_UOM_ID is NULL OR Stocking_UOM_ID =0)"
				  + " AND StockingUOMSymbol is NOT NULL"
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.info ("Set Stocking UOM from symbol=" + no);

		sql =  "UPDATE I_Locator l "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Stocking UOM, '"
				+ "WHERE Stocking_UOM_ID IS NULL "
				+ "AND (l.StockingUOMName IS NOT NULL OR l.StockingUOMSymbol IS NOT NULL) "
				+ "AND I_IsImported<>'Y' " 
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning ("Invalid Stocking UOM=" + no);
		
		//	Set Product from Product Key
		sql =  "UPDATE I_Locator l"
				  + " SET M_Product_ID=(SELECT M_Product_ID FROM M_Product m"
				  + " WHERE l.ProductValue=m.Value AND l.AD_Client_ID=m.AD_Client_ID ), "
				  + " ProductName =(SELECT Name FROM M_Product m"
				  + " WHERE l.ProductValue=m.Value AND l.AD_Client_ID=m.AD_Client_ID ) "
				  + " WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Product from ProductValue =" + no);

			//	Set Product from Product Name
		sql =  "UPDATE I_Locator l"
				  + " SET M_Product_ID = (SELECT M_Product_ID FROM M_Product m"
				  + " WHERE l.ProductName=m.Name AND l.AD_Client_ID=m.AD_Client_ID ), "
				  + " ProductValue =(SELECT Value FROM M_Product m"
				  + " WHERE l.ProductName=m.Name AND l.AD_Client_ID=m.AD_Client_ID )"
				  + " WHERE M_Product_ID IS NULL AND ProductName IS NOT NULL"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		log.fine("Set Product from ProductValue =" + no);


		// Error - Invalid Product
		sql =  "UPDATE I_Locator l"
				  + " SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Product, ' "
				  + " WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				  + " AND l.M_Product_ID IS NULL"
				  + " AND (l.productName IS NOT NULL OR l.ProductValue is NOT NULL)" 
				  + STD_CLIENT_CHECK;				  
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		if (no != 0)
			log.warning("Invalid Product=" + no);


		commit();

		//	-- New Locators -----------------------------------------------------

		int noInserted = 0;
		int noUpdated = 0;

		//	Go through Locator Records w/o
		sql =  "SELECT * FROM I_Locator "
			 	+ "WHERE I_IsImported='N'" 
			 	+ STD_CLIENT_CHECK
				+ "ORDER BY I_Locator_ID";
		

		Map<Integer, X_I_Locator> importLocatorMap = new HashMap<Integer, X_I_Locator>();
		List<MLocator> locatorsToSave = new ArrayList<MLocator>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery ();

			while (rs.next ())
			{
				X_I_Locator loc = new X_I_Locator (getCtx(), rs, get_TrxName());
				importLocatorMap.put(loc.getI_Locator_ID(), loc);
				
				int M_Locator_ID = loc.getM_Locator_ID();
				if(M_Locator_ID == 0)
					noUpdated++;
				else
					noInserted++;

				MLocator locator = new MLocator (M_Locator_ID, loc);
				locatorsToSave.add(locator);
				if(locatorsToSave.size() > COMMITCOUNT) {
					saveLocators(locatorsToSave, importLocatorMap);
					locatorsToSave.clear();
					importLocatorMap.clear();
				}
			}
			saveLocators(locatorsToSave, importLocatorMap);
		}
		catch (Exception e)	{
			log.log(Level.SEVERE, "Locator - " + sql.toString(), e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

				
		//Set Error to indicator to not imported
		sql =  "UPDATE I_Locator "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y' "
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {m_AD_Client_ID});
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noUpdated), " @M_Locator_ID@: @Updated@");
		addLog (0, null, new BigDecimal (noInserted), " @M_Locator_ID@: @Inserted@");
		return "#" + noInserted + "/" + noUpdated;

	}

	private void saveLocators(List<MLocator> locatorsToSave, 
								Map<Integer, X_I_Locator> importLocatorMap) {
		if(locatorsToSave.isEmpty() && importLocatorMap.isEmpty())
			return;
		
		if(!PO.saveAll(get_Trx(), locatorsToSave)) 
			throw new CompiereStateException("Could not save locators");

		for(MLocator locator : locatorsToSave) {
			X_I_Locator imp = importLocatorMap.get(locator.getI_Locator_ID());
			imp.setM_Locator_ID(locator.getM_Locator_ID());
		}
		
		List <MProductLocator> productLocatorsToSave = new ArrayList<MProductLocator>();
		List <X_I_Locator> importLocatorsToSave = new ArrayList<X_I_Locator>(importLocatorMap.values());
		
		for(X_I_Locator imp: importLocatorsToSave) {
			if(imp.getM_Product_ID()!=0)
			{
				MProductLocator pl=MProductLocator.getOfProductLocator(getCtx(), imp.getM_Product_ID(), imp.getM_Locator_ID());
				if(pl == null)
					pl= new MProductLocator(getCtx(), imp.getM_Product_ID(), imp.getM_Locator_ID(), get_Trx());
				pl.setMaxQuantity(imp.getProductMaxQuantity());
				pl.setMinQuantity(imp.getProductMinQuantity());
				productLocatorsToSave.add(pl);
			}
			imp.setI_IsImported(X_I_Locator.I_ISIMPORTED_Yes);
			imp.setProcessed(true);
		}

		if(!PO.saveAll(get_Trx(), productLocatorsToSave)) 
			throw new CompiereStateException("Could not save product locators");
		
		if(!PO.saveAll(get_Trx(), importLocatorsToSave)) 
			throw new CompiereStateException("Could not save import locators records");
	}

	@Override
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("AD_Org_ID"))
				m_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	public static void main(String[] args)
    {
		System.setProperty ("PropertyFile", "/home/namitha/Useful/Compiere.properties");
		Compiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//	Same Login entries as entered
		Ini.setProperty(Ini.P_UID, "GardenAdmin");
		Ini.setProperty(Ini.P_PWD, "GardenAdmin");
		Ini.setProperty(Ini.P_ROLE, "GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG, "HQ");
		Ini.setProperty(Ini.P_WAREHOUSE, "HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE, "English");
		Ini.setProperty(Ini.P_IMPORT_BATCH_SIZE, "100");
		
		Ctx ctx = Env.getCtx();
		Login login = new Login(ctx);
		if (!login.batchLogin(null, null))
			System.exit(1);

		//	Reduce Log level for performance
		CLogMgt.setLoggerLevel(Level.WARNING, null);
		CLogMgt.setLevel(Level.WARNING);

		//	Data from Login Context
		int AD_Client_ID = ctx.getAD_Client_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		//	Hardcoded
		int AD_Process_ID = 390;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();
		
		ProcessInfo pi = new ProcessInfo("Import", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(false);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		DB.startLoggingUpdates();		
		// Step 3: Run the process directly
		ImportLocator test = new ImportLocator();
		test.m_AD_Client_ID = ctx.getAD_Client_ID();
		test.m_AD_Org_ID = ctx.getAD_Org_ID();
		test.m_deleteOldImported = true;
		
		long start = System.currentTimeMillis();
		
		test.startProcess(ctx, pi, null);
		
		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		System.out.println("Total: " + duration + "s");

		// Step 4: get results
		if (pi.isError())
			System.err.println("Error: " + pi.getSummary());
		else
			System.out.println("OK: " + pi.getSummary());
		System.out.println(pi.getLogInfo());

		// stop logging database updates
		String logResult = DB.stopLoggingUpdates(0);
		System.out.println(logResult);
		
    }	
}
