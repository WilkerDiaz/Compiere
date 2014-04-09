package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MLocator;

public class InventoryAdjustment extends SvrProcess {

	@Override
	protected void prepare() {
	
	}
	
	Hashtable<Integer, Integer> stores = new Hashtable<Integer, Integer>();
	Hashtable<Integer, Integer> products = new Hashtable<Integer, Integer>();
	int store = 3;
	int org = 0;
	int locatorTransit = 0;
	int docType = 1000336;
	int warehouse = 0;

	private static boolean processActive = false;

	/**
	 * 	Is Process Active
	 *	@return true if active
	 */
	protected static boolean isProcessActive()
	{
		return processActive;
	}

	/**
	 * 	Set Process (in)active
	 *	@param active active
	 */
	protected static void setProcessActive (boolean active)
	{
		processActive = active;
	}
	
	@Override
	protected String doIt() throws Exception {
		
		if(isProcessActive())
			return "";
		
		setProcessActive(true);
		
		if(1==1)
			return "YA BORRO";
		
		Vector<Vector<Integer>> as = new Vector<Vector<Integer>>();
		Vector<Vector<Integer>> asAux = new Vector<Vector<Integer>>();
		Vector<Vector<Integer>> asOrig = new Vector<Vector<Integer>>();
		Vector<Integer> asControl = new Vector<Integer>();
		Vector<Vector<Integer>> compiere = new Vector<Vector<Integer>>();
		Vector<Vector<Integer>> compiereAux = new Vector<Vector<Integer>>();
		
		//Cargamos las tiendas y productos
		getStores();
		getProducts();
		

		MInventoryLine inventoryLine = null;
		MInventory inventory = null;
	
		MLocator locator = new MLocator( Env.getCtx(), stores.get(store), null);

		//Seteo de Org y Locators de Transito
		if(store==1){
			org = 1000068;
			locatorTransit = 1000075;
		}
		else if(store==2){
			org = 1000070;
			locatorTransit = 1000082;
		}
		else if(store==3){
			org = 1000069;
			locatorTransit = 1000076;
		}
		else if(store==7){
			org = 1000071;
			locatorTransit = 1000083;
		}
		else if(store==9){
			org = 1000072;
			locatorTransit = 1000077;
		}
		else if(store==10){
			org = 1000074;
			locatorTransit = 1000079;
		}
		else if(store==15){
			org = 1000073;
			locatorTransit = 1000078;
		}
		else if(store==16){
			org = 1000075;
			locatorTransit = 1000080;
		}
		else if(store==17){
			org = 1000076;
			locatorTransit = 1000081;
		}
		
		warehouse = locator.getM_Warehouse_ID();
		
		//Consultar Inventario AS400
		ResultSet rs = null;
		PreparedStatement sentencia = null;
		ResultSet rs_Compiere = null;
		PreparedStatement prst_Compiere = null;
		
		As400DbManager As = new As400DbManager();
		
		rs=getInventoryAS(sentencia, As);
		
		while(rs.next()){
			
			Vector<Integer> asLine = new Vector<Integer>();
			Vector<Integer> asLineAux = new Vector<Integer>();
			Vector<Integer> asLineOrig = new Vector<Integer>();
			
			asLine.add(stores.get(rs.getInt("TIENDA")));
			asLine.add(rs.getInt("LOTE"));
			asLine.add(products.get(rs.getInt("CODPRO")));
			asLine.add(rs.getInt("QTY"));
			as.add(asLine);
			
			asLineAux.add(stores.get(rs.getInt("TIENDA")));
			asLineAux.add(rs.getInt("LOTE"));
			asLineAux.add(products.get(rs.getInt("CODPRO")));
			asAux.add(asLineAux);
			
			asLineOrig.add(rs.getInt("TIENDA"));
			asLineOrig.add(rs.getInt("LOTE"));
			asLineOrig.add(rs.getInt("CODPRO"));
			asLineOrig.add(rs.getInt("QTY"));
			asOrig.add(asLineOrig);
			
			asControl.add(0);
		}
		
		rs.close();
		
		//Consultar Inventario Coompiere
		String sql = "select e.value TIENDA, c.value CODPRO, b.lot, max(b.m_attributesetinstance_id) ATT, NVL(sum(a.QTY),0) QTYNEW, sum(f.QTY) QTYACTUAL " +
					 "from XX_Vista_StorageMirrorActual a, m_attributesetinstance b, m_product c, m_locator d, m_warehouse e, m_storagedetail f " +
					 "where " +
					 "f.m_attributesetinstance_id = b.m_attributesetinstance_ID " +
					 "and f.m_product_id = c.m_product_id " +
					 "and f.m_locator_id = d.m_locator_id " +
					 "and d.m_warehouse_id = e.m_warehouse_id " +
					 "and f.m_locator_id = " + stores.get(store) + " " +
					 "and a.m_attributesetinstance_id (+) = f.m_attributesetinstance_ID " +
					 "and a.m_product_id (+) = f.m_product_id " +
					 "and a.m_locator_id (+) = f.m_locator_id " +
					 "and f.QTYTYPE = 'H' and b.lot is not null " +
					 "group by e.value, c.value, b.lot " +
					 "order by e.value, c.value, b.lot";

		try {
			
			prst_Compiere = DB.prepareStatement(sql, null);
			rs_Compiere = prst_Compiere.executeQuery();
			
			while(rs_Compiere.next()){
				
				Vector<Integer> compiereLine = new Vector<Integer>();
				Vector<Integer> compiereLineAux = new Vector<Integer>();

				compiereLine.add(stores.get(rs_Compiere.getInt("TIENDA")));
				compiereLine.add(rs_Compiere.getInt("LOT"));
				compiereLine.add(products.get(rs_Compiere.getInt("CODPRO")));
				compiereLine.add(rs_Compiere.getInt("QTYNEW"));
				compiereLine.add(rs_Compiere.getInt("QTYACTUAL"));
				compiereLine.add(rs_Compiere.getInt("ATT"));
				compiere.add(compiereLine);	
				
				compiereLineAux.add(stores.get(rs_Compiere.getInt("TIENDA")));
				compiereLineAux.add(rs_Compiere.getInt("LOT"));
				compiereLineAux.add(products.get(rs_Compiere.getInt("CODPRO")));
				compiereAux.add(compiereLineAux);	
			}
			
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}
		
		rs_Compiere.close();
		prst_Compiere.close();
		
		
		//Comparacion
		int asEnCompiere = 0;
		int index = 0;
		int oldIndex = 0;
		int mustBeZero = 0;
		int duplicadoAttribute = 0;
		int toChange = 0;
		int toChangeReal = 0;
		int dontNeedChange = 0;
		int result = 0;
		int desface = 0;
		
		for(int i=0; i<compiereAux.size(); i++){		
			
			if(i%40000==0){
				System.out.println("Compiere, Recorriendo el reg: " + i + " de: " + compiereAux.size());
				System.out.println("AS, Recorriendo el reg: " + index + " de: " + asAux.size());
				System.out.println("Deberian ser cero: " + mustBeZero);
				System.out.println("Desface: " + desface);
				System.out.println("Encontrados: " + asEnCompiere);
				System.out.println("A cambiar de esos encontrados: " + toChange);
				System.out.println("A cambiar realmente de esos encontrados: " + toChangeReal);
				System.out.println("No hace falta cambiar: " + dontNeedChange);
				System.out.println("Problemas de attributeset duplicado: " + duplicadoAttribute);
				System.out.println("\n*****\n");
			}
			
			//Creo la cabecera del inventory
			if(i==0){
				inventory = new MInventory( getCtx(), 0, null);
				inventory.setC_DocType_ID(docType);
				inventory.setAD_Org_ID(org);
				inventory.setM_Warehouse_ID(warehouse);
				inventory.setDescription("Inventario Fisico General 2012 (Tienda "+store+")");
				inventory.save();
			}
			
			if(asAux.contains(compiereAux.get(i))){
				
				asEnCompiere++;
				
				oldIndex = index;
				index = asAux.indexOf(compiereAux.get(i));
				
				if(asControl.get(index)==1)
					System.out.println("SEGUNDA VEZ **************************************************************");
				
				//Control
				asControl.set(index, 1);
				
				if(oldIndex == index){
					duplicadoAttribute++;
				}
					
				
				result = as.get(index).get(3) + compiere.get(i).get(3);
				
				if((compiere.get(i).get(3)==0 && compiere.get(i).get(4)==0))
				{
					desface++;
				}else{
				
					if(result != compiere.get(i).get(4)){
					
						toChange++;
								
						inventoryLine = new MInventoryLine( getCtx(), 0, null);
				 		inventoryLine.setAD_Org_ID(org);
				 		inventoryLine.setM_Inventory_ID(inventory.get_ID());
				 		inventoryLine.setM_Locator_ID(stores.get(store));
						inventoryLine.setInventoryType("D");
			 			inventoryLine.setM_AttributeSetInstance_ID(compiere.get(i).get(5));
				 		inventoryLine.setM_Product_ID(compiere.get(i).get(2));
				 		inventoryLine.setQtyBook(BigDecimal.valueOf(compiere.get(i).get(4))); 				
				 		inventoryLine.setQtyCount(BigDecimal.valueOf(result));
				 		inventoryLine.save();
					}
				}

				//asAux.remove(index);
				//index = index - 1;
			}
			else{
				
				//No está en Mary_Lote entonces debe ser cero
				result = 0 + compiere.get(i).get(3);
				
				if(result != compiere.get(i).get(4) && compiere.get(i).get(4)!=0){
					
					mustBeZero++;			
						
					inventoryLine = new MInventoryLine( getCtx(), 0, null);
			 		inventoryLine.setAD_Org_ID(org);
			 		inventoryLine.setM_Inventory_ID(inventory.get_ID());
			 		inventoryLine.setM_Locator_ID(stores.get(store));
			 		inventoryLine.setInventoryType("D");
			 		inventoryLine.setM_AttributeSetInstance_ID(compiere.get(i).get(5));
			 		inventoryLine.setM_Product_ID(compiere.get(i).get(2));
			 		inventoryLine.setQtyBook(BigDecimal.valueOf(compiere.get(i).get(4)));				
			 		inventoryLine.setQtyCount(BigDecimal.valueOf(result));
			 		inventoryLine.setDescription("No estaba en los ajustes del AS, debía ser cero");
			 		inventoryLine.save();
				}
			}
		}
		
		
		//resolver los no encontrados. Se agregan directamente las cantidades
		if(store!=1){
			noEncontrados(asControl, as, inventory.get_ID());
			//noEncontrados(asControl, as, 0);
		}else
		{
			noEncontradosCD(asControl, asOrig, 0);
		}
		
		//Limpieza de Transito
		cleanTransit();
		
		//RESUMEN FINAL
		System.out.println("Deberian ser cero porque no estaban en Mary_Lote: " + mustBeZero);
		System.out.println("Encontrados en Mary_Lote (a cambiar + no hace falta cambiar): " + asEnCompiere);
		System.out.println("A cambiar de esos encontrados: " + toChange);
		System.out.println("A cambiar realmente de esos encontrados: " + toChangeReal);
		System.out.println("No hace falta cambiar: " + dontNeedChange);
		System.out.println("Problemas de attributeset duplicado: " + duplicadoAttribute);
		
		if(store==1){
			cleanMerma();
		}
		
		return "Registros de compiere en AS: " + asEnCompiere + " / A Cambiar: " + toChangeReal + " + " + mustBeZero + " / no hace falta: " + dontNeedChange;
	}

	/*
	 * Inventario del AS
	 */
	private ResultSet getInventoryAS(Statement sentencia, As400DbManager As)
	{
		String sql;
        ResultSet r = null;
        
        As.conectar();
        	 	
        try{

            sql = "SELECT  TIENDA, CODPRO, LOTE, SUM(CAN_INV) as QTY " +
            	  "FROM LIBRTEMP.mary_lote where reg_act <> 'S' and tienda = " + store + " AND LOTE <> 0 " +
                  "GROUP BY tienda, codpro, lote " +
                  "ORDER by tienda, codpro, lote";

            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarConsulta(sql, sentencia);

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	
	/*
	 * Obtiene todas las tiendas por codigo
	 */
	private void getStores(){
		
		String sql = "select a.M_LOCATOR_ID, TO_NUMBER(b.value) as code " +
					 "from m_locator a, M_WareHouse b " +
					 "where ISDEFAULT = 'Y' and a.M_WAREHOUSE_ID = b.M_WAREHOUSE_ID " +
				     "AND b.AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				stores.put(rs.getInt("code"), rs.getInt("M_LOCATOR_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	/*
	 * Obtiene todas los productos por codigo
	 */
	private void getProducts(){
		
		String sql = "select a.M_Product_ID, TO_NUMBER(a.value) as code " +
					 "from m_product a " +
					 "where value <> 'Standard' " +
				     "AND a.AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				products.put(rs.getInt("code"), rs.getInt("M_Product_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	
	/*
	 * 
	 */
	public void procesado(int codpro, int tienda, int lote, As400DbManager As)
	{      
        try
        {
        	String SQL = ("UPDATE LIBRTEMP.mary_lote SET reg_act = 'S' WHERE tienda = '"+tienda+"' and lote = "+ lote +" and codpro = "+codpro+ "");
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	sentencia.executeUpdate(SQL);
        	sentencia.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
       
		}
	}
	
	
	public void noEncontrados(Vector<Integer> asControl, Vector<Vector<Integer>> as, int inventoryID){
		
		ResultSet rs = null;
		PreparedStatement prst = null;
		int noEncontrados = 0;
		int noEncontradosAgregados = 0;
		
		for(int i=0; i<asControl.size(); i++){
			
			if(asControl.get(i)==0){
				
				noEncontrados++;
			
				//Consultar Inventario Coompiere
				String sql = "select min(b.M_ATTRIBUTESETINSTANCE_ID) M_ATTRIBUTESETINSTANCE_ID " +
							 "from m_transaction a, M_ATTRIBUTESETINSTANCE b " + 
							 "where a.M_ATTRIBUTESETINSTANCE_ID = b.M_ATTRIBUTESETINSTANCE_ID " +
							 "and lot = "+ as.get(i).get(1) +" and M_PRODUCT_ID = " + as.get(i).get(2) + " " +
							 "group by b.M_ATTRIBUTESETINSTANCE_ID ";

				try {
					
					prst = DB.prepareStatement(sql, null);
					rs = prst.executeQuery();
					
					MInventoryLine inventoryLine = null;
					
					if(rs.next()){
						
						noEncontradosAgregados++;
						
						inventoryLine = new MInventoryLine( getCtx(), 0, null);
		 				inventoryLine.setAD_Org_ID(org);
		 				inventoryLine.setM_Inventory_ID(inventoryID);
		 				inventoryLine.setM_Locator_ID(stores.get(store));
		 				inventoryLine.setInventoryType("D");
		 				inventoryLine.setM_AttributeSetInstance_ID(rs.getInt("M_ATTRIBUTESETINSTANCE_ID"));
		 				inventoryLine.setM_Product_ID(as.get(i).get(2));
		 				inventoryLine.setQtyBook(BigDecimal.valueOf(0));	 
		 				inventoryLine.setDescription("No estaba en tienda, se agrega el lote con las piezas");
		 				inventoryLine.setQtyCount(BigDecimal.valueOf(as.get(i).get(3)));
		 				inventoryLine.save();
						
					}else
					{
						System.out.println("TAMPOCO");
						System.out.println(as.get(i));
					}
					
					rs.close();
					prst.close();
					
				} catch (SQLException e){
					log.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		
		System.out.println("*********************RESUMEN FINAL***************************");
		System.out.println("No encontrados de Mary_Lote: " + noEncontrados);
		System.out.println("No encontrados de Mary_Lote y agregados: " + noEncontradosAgregados);
	}
	
	
	public void noEncontradosCD(Vector<Integer> asControl, Vector<Vector<Integer>> as, int inventoryID){

		int noEncontrados = 0;
		int noEncontradosAgregados = 0;
		
		for(int i=0; i<asControl.size(); i++){
			
			if(asControl.get(i)==0){
				
				noEncontrados++;
				System.out.println(as.get(i));
			}
		}
		
		System.out.println("*********************RESUMEN FINAL***************************");
		System.out.println("No encontrados de Mary_Lote: " + noEncontrados);
		System.out.println("No encontrados de Mary_Lote y agregados: " + noEncontradosAgregados);
	}
	
	/*
	 * Clean Merma
	 */
	public void cleanMerma(){
		
		
		int locatorMerma = 1000084;
		
		//Merma
		String sql = "select M_ATTRIBUTESETINSTANCE_ID, M_PRODUCT_ID, QTY from m_storagedetail where m_locator_id = "+ locatorMerma +" and QTYTYPE = 'H' and QTY <> 0";
		ResultSet rs = null;
		PreparedStatement prst = null;
		MInventoryLine inventoryLine = null;
		MInventory inventory = null;
		int cleanMerma = 0;
		
		try {
			
			//Creo la cabecera del ajuste de merma
			inventory = new MInventory( getCtx(), 0, null);
			inventory.setC_DocType_ID(docType);
			inventory.setAD_Org_ID(org);
			inventory.setM_Warehouse_ID(warehouse);
			inventory.setDescription("Ajuste de Merma 2012 (Tienda "+store+")");
			inventory.save();
			
			prst = DB.prepareStatement(sql, null);
			rs = prst.executeQuery();
		
			while(rs.next()){
				
				cleanMerma++;
				
				inventoryLine = new MInventoryLine( getCtx(), 0, null);
 				inventoryLine.setAD_Org_ID(org);
 				inventoryLine.setM_Inventory_ID(inventory.get_ID());
 				inventoryLine.setM_Locator_ID(locatorMerma);
 				inventoryLine.setInventoryType("D");
 				inventoryLine.setM_AttributeSetInstance_ID(rs.getInt("M_ATTRIBUTESETINSTANCE_ID"));
 				inventoryLine.setM_Product_ID(rs.getInt("M_PRODUCT_ID"));
 				inventoryLine.setQtyBook(BigDecimal.valueOf(rs.getInt("QTY")));	 				
 				inventoryLine.setQtyCount(BigDecimal.valueOf(0));
 				inventoryLine.save();
			}
			
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}
		
		System.out.println("MERMA a 0: " + cleanMerma);
		
	}
	
	
	/*
	 * Clean Transito
	 */
	public void cleanTransit(){
		
		
		//transito
		String sql = "select M_ATTRIBUTESETINSTANCE_ID, M_PRODUCT_ID, QTY from m_storagedetail where m_locator_id = "+ locatorTransit +" and QTYTYPE = 'H' and QTY < 0";
		ResultSet rs = null;
		PreparedStatement prst = null;
		MInventoryLine inventoryLine = null;
		MInventory inventory = null;
		int cleanMerma = 0;
		
		try {
			
			//Creo la cabecera del ajuste de merma
			inventory = new MInventory( getCtx(), 0, null);
			inventory.setC_DocType_ID(docType);
			inventory.setAD_Org_ID(org);
			inventory.setM_Warehouse_ID(warehouse);
			inventory.setDescription("Ajuste de Transito 2012 (Tienda "+store+")");
			inventory.save();
			
			prst = DB.prepareStatement(sql, null);
			rs = prst.executeQuery();
		
			while(rs.next()){
				
				cleanMerma++;
				
				inventoryLine = new MInventoryLine( getCtx(), 0, null);
 				inventoryLine.setAD_Org_ID(org);
 				inventoryLine.setM_Inventory_ID(inventory.get_ID());
 				inventoryLine.setM_Locator_ID(locatorTransit);
 				inventoryLine.setInventoryType("D");
 				inventoryLine.setM_AttributeSetInstance_ID(rs.getInt("M_ATTRIBUTESETINSTANCE_ID"));
 				inventoryLine.setM_Product_ID(rs.getInt("M_PRODUCT_ID"));
 				inventoryLine.setQtyBook(BigDecimal.valueOf(rs.getInt("QTY")));	 				
 				inventoryLine.setQtyCount(BigDecimal.valueOf(0));
 				inventoryLine.save();
			}
			
			rs.close();
			prst.close();
			
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}
		
		System.out.println("Transito a 0: " + cleanMerma);
		
	}
	
	
	public void borrarInventoryLine(){
		
		//Merma
				String sql = "Select M_InventoryLine_ID from M_InventoryLine where M_Inventory_ID IN (1086919, 1087019, 1087519, 1087619, 1087620, 1087719)";
				ResultSet rs = null;
				PreparedStatement prst = null;
				
				try {
					
					prst = DB.prepareStatement(sql, null);
					rs = prst.executeQuery();
				
					String sqlDelete = "";
					int cuantos = 0;
					while(rs.next()){
						
						cuantos ++;
						sqlDelete = "Delete from M_InventoryLine where M_InventoryLine_ID = " + rs.getInt("M_InventoryLine_ID");
						
						DB.executeUpdate(null, sqlDelete);
						System.out.println("LLEVO: " + cuantos);
					}
					
					rs.close();
					prst.close();
					
				} catch (SQLException e){
					log.log(Level.SEVERE, e.getMessage());
				}
		
	}
	
}