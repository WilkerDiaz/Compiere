package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VCN_Inventory;

/**
* Importa Ajuste de Inventario del AS400 a la tabla XX_VCN_Inventory
* @author ghuchet
*
*/
public class ImportInventoryAdjustments  extends SvrProcess {
	
	private String myLog = "";
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override 
	protected String doIt() throws Exception {
		
	
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -1);
		int month = date.get(Calendar.MONTH) + 1;
		int year = date.get(Calendar.YEAR);
		
		//month = 8;
		System.out.println("Inicio: "+new Date());
	
		deleteInv();
		importInvAdjustments(null, month, year);
		eraseAdjInv(month, year);
		updateInventoryLot(null, month, year);
		updateInventoryCDLotNull(null, month, year);
		updateInventoryStore(null, month, year);
		System.out.println("Fin: "+new Date());
		return myLog;
	}
	
	private int getBatch(int lot, int prod) {

		int result = 0;
		String sql = "SELECT A.M_ATTRIBUTESETINSTANCE_ID "+
			"FROM M_INOUTLINE A, M_INOUT B, M_PRODUCT C, M_ATTRIBUTESETINSTANCE D  "+
			"WHERE A.M_INOUT_ID = B.M_INOUT_ID AND A.M_PRODUCT_ID = C.M_PRODUCT_ID  "+
			"AND A.M_ATTRIBUTESETINSTANCE_ID = D.M_ATTRIBUTESETINSTANCE_ID "+
			"AND B.ISSOTRX = 'N' "+
			"AND C.VALUE = '"+prod+"'"+
			"AND D.LOT = "+lot+
			"AND B.AD_CLIENT_ID = " + getCtx().getAD_Client_ID()+
			"AND ROWNUM =1 ";

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
		rs = prst.executeQuery();
		
		while (rs.next()){
			result = rs.getInt(1);
		}

		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally {
			try {
				rs.close();
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return result;
	}


	private void importInvAdjustments(Object object, int month, int year)  throws Exception{
		
        String sql = "SELECT CODPRO, (CASE WHEN CONPRE >= 8000 THEN 0 ELSE CONPRE END) CONPRECIO, TIENDA, AÑOINV, MESINV, " +
        		" SUM(CANTAJUSTE) CANTAJUSTE, SUM(MONTAJUSTE) MONTAJUSTE, SUM(CANTAJUANT) CANTAJUANT, SUM(MONTAJUANT) MONTAJUANT" +
        		" FROM BECOFILE.INVM14 WHERE AÑOINV = "+year+" AND MESINV = "+month+" " +
        				" AND ( CANTAJUSTE <> 0 OR MONTAJUSTE  <> 0 OR CANTAJUANT  <> 0 OR MONTAJUANT  <> 0 )" +
        				" GROUP BY CODPRO, (CASE WHEN CONPRE >= 8000 THEN 0 ELSE CONPRE END), TIENDA, AÑOINV, MESINV ";
      //Busqueda de los registros del mes
		As400DbManager As = new As400DbManager();
		As.conectar();
		
		Statement statementAS400=null;
		
		statementAS400 = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = null;
		
		System.out.println(sql);
		try
        {          
			int lote = 0, cons = 0;
			rs= As.realizarConsulta(sql,statementAS400);
			while(rs.next()){
				cons = rs.getInt("CONPRECIO");
				lote = 0;
				if (cons >= 800) {
					lote =  rs.getInt("CONPRECIO") - 800;
					lote = getBatch(lote, rs.getInt("CODPRO"));
					cons = 0;
				}
				
				String sqlInsert = "INSERT INTO XX_VCN_INVENTORYADJUSTMENTS "
				         + "(PRODUCT, PRICECONSECUTIVE, WAREHOUSE, YEAR, MONTH, QTY, AMOUNT, PREVIOUSQTY,  PREVIOUSAMOUNT, LOTEID)"
				         + " VALUES ("+ rs.getInt("CODPRO") +","+ cons +","+ rs.getInt("TIENDA") +","+ rs.getInt("AÑOINV") +","
				         + rs.getInt("MESINV") +","+ rs.getInt("CANTAJUSTE") +","+ rs.getBigDecimal("MONTAJUSTE") 
				         +","+ rs.getInt("CANTAJUANT") +","+ rs.getBigDecimal("MONTAJUANT") +","+lote+") ";
				DB.executeUpdate(null, sqlInsert);
			
			}
			
			
        }catch (Exception e) {
        	e.printStackTrace();
        	myLog += "Error: " + e.getMessage() + "\n";
		}finally {
			statementAS400.close();   	
        	As.desconectar();
			DB.closeResultSet(rs);
		}
		//updateLotCD(month,year);
	}
	
	private void updateLotCD(int month, int year) throws Exception {
			PreparedStatement psUpdate =null;
			try {
				System.out.println("Actualizando lotes en CD...");
				String sqlUpdate = "\nUPDATE xX_VCN_INVENTORYADJUSTMENTS I SET I.LOTEID = "+
					"\nnvl((SELECT A.M_ATTRIBUTESETINSTANCE_ID "+
					"FROM M_INOUTLINE A, M_INOUT B, M_PRODUCT C, M_ATTRIBUTESETINSTANCE D  "+
					"WHERE A.M_INOUT_ID = B.M_INOUT_ID AND A.M_PRODUCT_ID = C.M_PRODUCT_ID  "+
					"AND A.M_ATTRIBUTESETINSTANCE_ID = D.M_ATTRIBUTESETINSTANCE_ID "+
					"AND B.ISSOTRX = 'N' "+
					"AND C.VALUE = 'I.CODPROD'"+
					"AND D.LOT = I.PRICECONSECUTIVE"+
					"AND ROWNUM =1 ),0), I.PRICECONSECUTIVE = 0 "+
					"\nWHERE PRICECONSECUTIVE >= 800 "+
					"\nAND I.YEAR = " + year + " AND I.MONTH = " + month;
		
				psUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
				psUpdate.execute();
				commit();
				System.out.println("Fin de Actualizando lotes en CD...");
			}catch (Exception e) {
				 throw new Exception("Error actualizando lotes de CD" +e.getMessage());
			}finally{
				psUpdate.close();
			}
			
	}
	
	private void updateInventoryCDLotNull(Trx trans, int month, int year) {
		System.out.println("Inicio update Ajustes");

		String sql = "SELECT LOTEID, " +
				"\nMAX(nvl(INV.XX_VCN_INVENTORY_ID,0)) XX_VCN_INVENTORY_ID, P.M_PRODUCT_ID, NVL(ADJ.PRICECONSECUTIVE,0) CONSECUTIVEPRICE , W.M_WAREHOUSE_ID, " +
				"\nSUM(NVL(ADJ.QTY,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) QTY, SUM(NVL(ADJ.AMOUNT,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) AMOUNT," +
				"\nSUM(NVL(ADJ.PREVIOUSQTY,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) PREVIOUSQTY, " +
				"\nSUM(NVL(ADJ.PREVIOUSAMOUNT,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) PREVIOUSAMOUNT  " +
				"\nFROM XX_VCN_INVENTORYADJUSTMENTS ADJ " +
				"\nJOIN M_PRODUCT P ON (to_char(ADJ.PRODUCT) = to_char(P.VALUE)) " +
				"\nJOIN M_WAREHOUSE W ON (ADJ.WAREHOUSE = W.VALUE) " +
				"\nLEFT JOIN XX_VCN_INVENTORY INV  ON (ADJ.YEAR = INV.XX_INVENTORYYEAR AND  ADJ.MONTH = INV.XX_INVENTORYMONTH " +
				"\nAND  P.M_PRODUCT_ID = INV.M_PRODUCT_ID AND ADJ.PRICECONSECUTIVE = INV.XX_CONSECUTIVEPRICE AND W.M_WAREHOUSE_ID = INV.M_WAREHOUSE_ID  " +
				"\nAND LOTEID = nvl(INV.M_ATTRIBUTESETINSTANCE_ID,0)) " +
				"\nWHERE ADJ.YEAR = "+year+" AND  ADJ.MONTH = "+month+
				"\n and ADJ.LOTEID = 0 and w.XX_ISSTORE = 'N' "+
				"\nGROUP BY P.M_PRODUCT_ID,NVL(ADJ.PRICECONSECUTIVE,0), W.M_WAREHOUSE_ID, LOTEID ";
				
		 PreparedStatement ps = null;
		 ResultSet rs = null;	
		 int registroInv;
		 System.out.println(sql);
		 int i = 0;
		try {
			 ps = DB.prepareStatement(sql, null);
			 rs = ps.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 while (rs.next() ) {
					registroInv = rs.getInt("XX_VCN_INVENTORY_ID");
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto de Ajuste
						lineaInventario.setXX_AdjustmentsAmount((rs.getBigDecimal("AMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste
						lineaInventario.setXX_ADJUSTMENTSQUANTITY((rs.getBigDecimal("QTY")).setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste previo
						lineaInventario.setXX_PREVIOUSADJUSTMENTSAMOUNT((rs.getBigDecimal("PREVIOUSAMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste previo
						lineaInventario.setXX_PREVIOUSADJUSTMENTSQUANTITY((rs.getBigDecimal("PREVIOUSQTY")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();
					 }else {
						
						//Si el registro no existe crearlo con el resultado
						MProduct product = new MProduct(getCtx(), rs.getInt("M_Product_ID"), null);

						Integer lot = 0;
						BigDecimal costPrice = new BigDecimal(0);
					
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(product.get_ID());
						 lineaInventario.setXX_VMR_Category_ID(product.getXX_VMR_Category_ID());
						 lineaInventario.setXX_VMR_Department_ID(product.getXX_VMR_Department_ID());
						 lineaInventario.setXX_VMR_Line_ID(product.getXX_VMR_Line_ID());
						 lineaInventario.setXX_VMR_Section_ID(product.getXX_VMR_Section_ID());
						 lineaInventario.setM_Warehouse_ID(rs.getInt("M_WAREHOUSE_ID"));
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(lot);
						 lineaInventario.setXX_ConsecutivePrice(costPrice); 
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(costPrice.setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste
						 lineaInventario.setXX_AdjustmentsAmount((rs.getBigDecimal("AMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste
						 lineaInventario.setXX_ADJUSTMENTSQUANTITY((rs.getBigDecimal("QTY")).setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste previo
						 lineaInventario.setXX_PREVIOUSADJUSTMENTSAMOUNT((rs.getBigDecimal("PREVIOUSAMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste previo
						 lineaInventario.setXX_PREVIOUSADJUSTMENTSQUANTITY((rs.getBigDecimal("PREVIOUSQTY")).setScale(2, RoundingMode.HALF_EVEN));
						 
						 if (!lineaInventario.save()) {
					        	myLog += "Error guardando registro nuevo ";
						 }
						 i++;
						 if(i % 100 == 0){
							 System.out.println("nuevoss: "+i);
						 }
					}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
        	myLog += "Error : " + e.getMessage() + "\n";
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	 
		 }
		 System.out.println("Nuevos: " + i);
		 System.out.println("Fin update Ajustes");
		
	}
	private void updateInventoryLot(Trx trans, int month, int year) {
		System.out.println("Inicio update Ajustes");

		String sql = "SELECT LOTEID, " +
				"\nMAX(nvl(INV.XX_VCN_INVENTORY_ID,0)) XX_VCN_INVENTORY_ID, P.M_PRODUCT_ID, NVL(ADJ.PRICECONSECUTIVE,0) CONSECUTIVEPRICE , W.M_WAREHOUSE_ID, " +
				"\nSUM(NVL(ADJ.QTY,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) QTY, SUM(NVL(ADJ.AMOUNT,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) AMOUNT," +
				"\nSUM(NVL(ADJ.PREVIOUSQTY,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) PREVIOUSQTY, " +
				"\nSUM(NVL(ADJ.PREVIOUSAMOUNT,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) PREVIOUSAMOUNT  " +
				"\nFROM XX_VCN_INVENTORYADJUSTMENTS ADJ " +
				"\nJOIN M_PRODUCT P ON (to_char(ADJ.PRODUCT) = to_char(P.VALUE)) " +
				"\nJOIN M_WAREHOUSE W ON (ADJ.WAREHOUSE = W.VALUE) " +
				"\nLEFT JOIN XX_VCN_INVENTORY INV  ON (ADJ.YEAR = INV.XX_INVENTORYYEAR AND  ADJ.MONTH = INV.XX_INVENTORYMONTH " +
				"\nAND  P.M_PRODUCT_ID = INV.M_PRODUCT_ID AND ADJ.PRICECONSECUTIVE = INV.XX_CONSECUTIVEPRICE AND W.M_WAREHOUSE_ID = INV.M_WAREHOUSE_ID  " +
				"\nAND LOTEID = INV.M_ATTRIBUTESETINSTANCE_ID) " +
				"\nWHERE ADJ.YEAR = "+year+" AND  ADJ.MONTH = "+month+
				"\n and ADJ.LOTEID > 0 "+
				"\nGROUP BY P.M_PRODUCT_ID,NVL(ADJ.PRICECONSECUTIVE,0), W.M_WAREHOUSE_ID, LOTEID ";
				
		 PreparedStatement ps = null;
		 ResultSet rs = null;	
		 int registroInv;
		 System.out.println(sql);
		 int i = 0;
		try {
			 ps = DB.prepareStatement(sql, null);
			 rs = ps.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 while (rs.next() ) {
					registroInv = rs.getInt("XX_VCN_INVENTORY_ID");
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto de Ajuste
						lineaInventario.setXX_AdjustmentsAmount((rs.getBigDecimal("AMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste
						lineaInventario.setXX_ADJUSTMENTSQUANTITY((rs.getBigDecimal("QTY")).setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste previo
						lineaInventario.setXX_PREVIOUSADJUSTMENTSAMOUNT((rs.getBigDecimal("PREVIOUSAMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste previo
						lineaInventario.setXX_PREVIOUSADJUSTMENTSQUANTITY((rs.getBigDecimal("PREVIOUSQTY")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();
					 }else {
						
						//Si el registro no existe crearlo con el resultado
						MProduct product = new MProduct(getCtx(), rs.getInt("M_Product_ID"), null);

						Integer lot = 0;
						BigDecimal costPrice = new BigDecimal(0);
						lot =rs.getInt("LOTEID");
						
		
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(product.get_ID());
						 lineaInventario.setXX_VMR_Category_ID(product.getXX_VMR_Category_ID());
						 lineaInventario.setXX_VMR_Department_ID(product.getXX_VMR_Department_ID());
						 lineaInventario.setXX_VMR_Line_ID(product.getXX_VMR_Line_ID());
						 lineaInventario.setXX_VMR_Section_ID(product.getXX_VMR_Section_ID());
						 lineaInventario.setM_Warehouse_ID(rs.getInt("M_WAREHOUSE_ID"));
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(lot);
						 lineaInventario.setXX_ConsecutivePrice(costPrice); 
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(costPrice.setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste
						 lineaInventario.setXX_AdjustmentsAmount((rs.getBigDecimal("AMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste
						 lineaInventario.setXX_ADJUSTMENTSQUANTITY((rs.getBigDecimal("QTY")).setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste previo
						 lineaInventario.setXX_PREVIOUSADJUSTMENTSAMOUNT((rs.getBigDecimal("PREVIOUSAMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste previo
						 lineaInventario.setXX_PREVIOUSADJUSTMENTSQUANTITY((rs.getBigDecimal("PREVIOUSQTY")).setScale(2, RoundingMode.HALF_EVEN));
						 
						 if (!lineaInventario.save()) {
					        	myLog += "Error guardando registro nuevo ";
						 }
						 i++;
						 if(i % 100 == 0){
							 System.out.println("nuevoss: "+i);
						 }
					}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
        	myLog += "Error : " + e.getMessage() + "\n";
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	 
		 }
		 System.out.println("Nuevos: " + i);
		 System.out.println("Fin update Ajustes");
		
	}

	private void updateInventoryStore(Trx trans, int month, int year) {
		System.out.println("Inicio update Ajustes");

		String sql = "SELECT  " +
				"\nMAX(nvl(INV.XX_VCN_INVENTORY_ID,0)) XX_VCN_INVENTORY_ID, P.M_PRODUCT_ID, NVL(ADJ.PRICECONSECUTIVE,0) CONSECUTIVEPRICE , W.M_WAREHOUSE_ID, " +
				"\nSUM(NVL(ADJ.QTY,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) QTY, SUM(NVL(ADJ.AMOUNT,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) AMOUNT," +
				"\nSUM(NVL(ADJ.PREVIOUSQTY,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) PREVIOUSQTY, " +
				"\nSUM(NVL(ADJ.PREVIOUSAMOUNT,0))/decode(COUNT(XX_VCN_INVENTORY_ID),null,1,0,1,COUNT(XX_VCN_INVENTORY_ID)) PREVIOUSAMOUNT  " +
				"\nFROM XX_VCN_INVENTORYADJUSTMENTS ADJ " +
				"\nJOIN M_PRODUCT P ON (to_char(ADJ.PRODUCT) = to_char(P.VALUE)) " +
				"\nJOIN M_WAREHOUSE W ON (ADJ.WAREHOUSE = W.VALUE) " +
				"\nLEFT JOIN XX_VCN_INVENTORY INV  ON (ADJ.YEAR = INV.XX_INVENTORYYEAR AND  ADJ.MONTH = INV.XX_INVENTORYMONTH " +
				"\nAND  P.M_PRODUCT_ID = INV.M_PRODUCT_ID AND ADJ.PRICECONSECUTIVE = INV.XX_CONSECUTIVEPRICE AND W.M_WAREHOUSE_ID = INV.M_WAREHOUSE_ID  " +
				"\n) " +
				"\nWHERE ADJ.YEAR = "+year+" AND  ADJ.MONTH = "+month+
				"\n and ADJ.LOTEID = 0 and w.XX_ISSTORE = 'Y' "+
				"\nGROUP BY P.M_PRODUCT_ID,NVL(ADJ.PRICECONSECUTIVE,0), W.M_WAREHOUSE_ID ";
				
		 PreparedStatement ps = null;
		 ResultSet rs = null;	
		 int registroInv;
		 System.out.println(sql);
		 int i = 0;
		try {
			 ps = DB.prepareStatement(sql, null);
			 rs = ps.executeQuery();
			 X_XX_VCN_Inventory lineaInventario = null;
			 while (rs.next() ) {
					registroInv = rs.getInt("XX_VCN_INVENTORY_ID");
					if (registroInv > 0) {
						lineaInventario = new X_XX_VCN_Inventory(getCtx(),registroInv, null);
							//Monto de Ajuste
						lineaInventario.setXX_AdjustmentsAmount((rs.getBigDecimal("AMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste
						lineaInventario.setXX_ADJUSTMENTSQUANTITY((rs.getBigDecimal("QTY")).setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste previo
						lineaInventario.setXX_PREVIOUSADJUSTMENTSAMOUNT((rs.getBigDecimal("PREVIOUSAMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste previo
						lineaInventario.setXX_PREVIOUSADJUSTMENTSQUANTITY((rs.getBigDecimal("PREVIOUSQTY")).setScale(2, RoundingMode.HALF_EVEN));
						lineaInventario.save();
					 }else {
						
						//Si el registro no existe crearlo con el resultado
						MProduct product = new MProduct(getCtx(), rs.getInt("M_Product_ID"), null);
						 
						PreparedStatement psTemp =null; 
						ResultSet rsTemp =null;
						Integer lot = 0;
						BigDecimal costPrice = new BigDecimal(0);
						try {
							String sqlTemp = "\nSELECT nvl(PC.XX_UNITPURCHASEPRICE,0) XX_UNITPURCHASEPRICE, PC.M_ATTRIBUTESETINSTANCE_ID  " +
									"\nFROM XX_VMR_PRICECONSECUTIVE PC "+
									"\nWHERE PC.XX_PRICECONSECUTIVE = " + rs.getBigDecimal("CONSECUTIVEPRICE") +
									"\nAND PC.M_PRODUCT_ID = " +rs.getInt("M_Product_ID");
							//System.out.println("Lote "+sqlTemp);
							psTemp = DB.prepareStatement(sqlTemp, null);
							rsTemp = psTemp.executeQuery();
							if(rsTemp.next()){
								costPrice =rsTemp.getBigDecimal("XX_UNITPURCHASEPRICE");
								lot =rsTemp.getInt("M_ATTRIBUTESETINSTANCE_ID");
							}
									
						}catch (Exception e) {
							e.printStackTrace();
				        	myLog += "Error buscando consecutivo: " + e.getMessage() + "\n";
						}finally{
							DB.closeResultSet(rsTemp);
							DB.closeStatement(psTemp);		 
						}
		
						 lineaInventario = new X_XX_VCN_Inventory(getCtx(), 0, null);
						 lineaInventario.setM_Product_ID(product.get_ID());
						 lineaInventario.setXX_VMR_Category_ID(product.getXX_VMR_Category_ID());
						 lineaInventario.setXX_VMR_Department_ID(product.getXX_VMR_Department_ID());
						 lineaInventario.setXX_VMR_Line_ID(product.getXX_VMR_Line_ID());
						 lineaInventario.setXX_VMR_Section_ID(product.getXX_VMR_Section_ID());
						 lineaInventario.setM_Warehouse_ID(rs.getInt("M_WAREHOUSE_ID"));
						 lineaInventario.setXX_INVENTORYYEAR(new BigDecimal(year));
						 lineaInventario.setXX_INVENTORYMONTH(new BigDecimal(month));
						 lineaInventario.setM_AttributeSetInstance_ID(lot);
						 lineaInventario.setXX_ConsecutivePrice(rs.getBigDecimal("CONSECUTIVEPRICE")); 
						 lineaInventario.setXX_INITIALINVENTORYAMOUNT(Env.ZERO);
						 lineaInventario.setXX_INITIALINVENTORYQUANTITY(Env.ZERO);
						 lineaInventario.setXX_InitialInventoryCostPrice(costPrice.setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste
						 lineaInventario.setXX_AdjustmentsAmount((rs.getBigDecimal("AMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste
						 lineaInventario.setXX_ADJUSTMENTSQUANTITY((rs.getBigDecimal("QTY")).setScale(2, RoundingMode.HALF_EVEN));
							//Monto de Ajuste previo
						 lineaInventario.setXX_PREVIOUSADJUSTMENTSAMOUNT((rs.getBigDecimal("PREVIOUSAMOUNT")).setScale(2, RoundingMode.HALF_EVEN));
							//Cantidad de Ajuste previo
						 lineaInventario.setXX_PREVIOUSADJUSTMENTSQUANTITY((rs.getBigDecimal("PREVIOUSQTY")).setScale(2, RoundingMode.HALF_EVEN));
						 
						 if (!lineaInventario.save()) {
					        	myLog += "Error guardando registro nuevo ";
						 }
						 i++;
						 if(i % 100 == 0){
							 System.out.println("nuevoss: "+i);
						 }
					}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
        	myLog += "Error : " + e.getMessage() + "\n";
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	 
		 }
		 System.out.println("Nuevos: " + i);
		 System.out.println("Fin update Ajustes");
		
	}
	/** Inicializa en 0 los ajustes del inventario del mes seleccionado */
	public void eraseAdjInv(int month, int year) throws Exception {
		PreparedStatement psUpdate =null;
		
		try {
			String sqlUpdate = "\nUPDATE XX_VCN_INVENTORY SET XX_PREVIOUSADJUSTMENTSAMOUNT = 0, " +
					"\nXX_PREVIOUSADJUSTMENTSQUANTITY = 0, XX_ADJUSTMENTSAMOUNT = 0, XX_ADJUSTMENTSQUANTITY= 0  " +
					"WHERE XX_INVENTORYYEAR = " + year + " AND XX_INVENTORYMONTH = " + month;
			psUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
			psUpdate.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error inicializando ajuste inventario mensual" +e.getMessage());
		}finally{
			psUpdate.close();
		}
		
/*		PreparedStatement psDelete =null;
		String sqlDelete = "";
		try {
			sqlDelete = "DELETE FROM XX_VCN_INVENTORY  WHERE XX_INVENTORYYEAR = " + year + " AND XX_INVENTORYMONTH = " + month+
			" AND XX_SHOPPINGQUANTITY = 0 AND XX_SHOPPINGAMOUNT = 0 AND XX_SALESQUANTITY = 0 " +
			" AND XX_SALESAMOUNT = 0 AND XX_MOVEMENTQUANTITY = 0 and XX_MOVEMENTAMOUNT = 0 " +
			" AND XX_INITIALINVENTORYQUANTITY = 0 AND XX_INITIALINVENTORYAMOUNT = 0 ";
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			System.out.println(sqlDelete);
			 throw new Exception("Error borrando ajuste inventario mensual" +e.getMessage());
		}finally{
			psDelete.close();
		}
		*/
		 System.out.println("Inicializando Ajustes");
	}

	/** Borra el inventario del mes seleccionado */
	public void deleteInv() throws Exception {
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VCN_INVENTORYADJUSTMENTS";
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			System.out.println("Error borrando ajuste inventario mensual (XX_VCN_INVENTORYADJUSTMENTS) " +e.getMessage());
			 throw new Exception("Error borrando ajuste inventario mensual (XX_VCN_INVENTORYADJUSTMENTS) " +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
}
