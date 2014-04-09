package compiere.model.cds.processes;

import java.awt.Container;
import java.awt.Dialog;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.formula.ParseContext;
import jxl.read.biff.BiffException;

import org.compiere.apps.ADialog;
import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_Ref_Quantity_Type;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.X_M_Product;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDistribProductDetail;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VMR_DistribDetailTemp;
import compiere.model.cds.X_XX_VMR_DistribProdPerStore;
import compiere.model.cds.X_XX_VMR_DistribProductDetail;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_StorePercentDistrib;
import compiere.model.cds.X_XX_VMR_StoreQuantityDistrib;
import compiere.model.cds.X_XX_VMR_VendorProdRef;

/**
 *  Permite distribuir productos a partir de un archivo excel
 *
 *  @author     Gabrielle Huchet
 *  @version    
 */


public class XX_DistributionByExcel extends SvrProcess {

	static CLogger log = CLogger.getCLogger(XX_ImportPTransfer.class);
	private String file = null;
	private int warehouseCD = 0;
	private int locatorCD = 0; 
	private int orgCD = 0;
	private Vector<MWarehouse> stores = new Vector<MWarehouse>();
	private int distID = 0; 
	private int detailID = 0; 
	private boolean isOk = true;
	private StringBuffer message =new StringBuffer();
	private StringBuffer prodDesc = new StringBuffer();
	private int EMPTY = 0;
	private int IMCOMPLETE = 1;
	private int COMPLETE = 2;
 	private int statusDist = EMPTY;
	
	@Override
	protected String doIt() throws Exception {
		String msg = "";
		if (file == null) {
			msg =  Msg.translate( getCtx(), "File Not Loaded");
		} else {
			
			if(!file.substring(file.length()-4, file.length()).equals(".xls")){
				if(file.substring(file.length()-5, file.length()).equals(".xlsx")){
					msg = Msg.translate( getCtx(), "Excel Earlier Format");;	
				}else{
					msg =Msg.translate( getCtx(), "Not Excel");
				}
			}else{	
				MWarehouse MWarehouse = new MWarehouse(Env.getCtx(), warehouseCD, null);
				orgCD = MWarehouse.getAD_Org_ID();
				locatorCD = Utilities.obtenerLocatorChequeado(warehouseCD).get_ID();
				getAllStores();
				msg = readFile();
			}
		}
		return msg;
	}

	@Override
	/** Obtener los parametros */
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("File") ) {
					file = element.getParameter().toString();
				}else if (element.getParameterName().equals("M_Warehouse_ID") ) {
					warehouseCD = element.getParameterAsInt();
				}
			}			
		}
	}
	
	
	public String readFile()  throws IOException  {
		
		StringBuffer messageStatus = new StringBuffer();
		File inputWorkbook = new File(file);
		Workbook w;
		try {
			String msg = "";
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
	
			//int defaultRows = sheet.getRows();
			
			//Si la cantidad de columnas es 6
			if(sheet.getColumns()>=6 && sheet.getRows() > 1){
		
				//Valido que las cabeceras tengan el formato correcto	
				if(!sheet.getCell(0, 0).getContents().equals("CODIGO PROVEEDOR") 
						|| !sheet.getCell(1, 0).getContents().equals("REFERENCIA")
						|| !sheet.getCell(2, 0).getContents().equals("COLOR")
						|| !sheet.getCell(3, 0).getContents().equals("TALLAS")
						|| !sheet.getCell(4, 0).getContents().equals("CURVA DE TALLAS")
						|| !sheet.getCell(5, 0).getContents().equals("PIEZAS 002")
						|| !sheet.getCell(6, 0).getContents().equals("PIEZAS 003")
						|| !sheet.getCell(7, 0).getContents().equals("PIEZAS 007")
						|| !sheet.getCell(8, 0).getContents().equals("PIEZAS 009")
						|| !sheet.getCell(9, 0).getContents().equals("PIEZAS 010")
						|| !sheet.getCell(10, 0).getContents().equals("PIEZAS 015")
						|| !sheet.getCell(11, 0).getContents().equals("PIEZAS 016")
						|| !sheet.getCell(12, 0).getContents().equals("PIEZAS 017")
						|| !sheet.getCell(13, 0).getContents().equals("TOTAL")
				) {
					msg = Msg.translate( getCtx(), "Column Names");
					return msg;
				}	
			//Se crea la cabecera de distribución
				distID = createHeaderDist();

				if(distID > 0){
					for (int i = 1; i < sheet.getRows(); i++) {
						
						//Capturo el codigo del proveedor
						String vendor_value = sheet.getCell(0, i).getContents();
						if (vendor_value.isEmpty()){
							break;
						}
						Integer vendor = getVendor(vendor_value);
						if (vendor == null){
							isOk=false;
							message.append("\nError en celda A fila "+(i+1)+". Proveedor no existe.");
							break;
						}
						
						//Capturo la referencia
						String ref_value = sheet.getCell(1, i).getContents();
						Integer ref = getReference(ref_value);					
						if (ref == null ) {
							message.append("\nError en celda B fila "+(i+1)+". Referencia de proveedor no existe.");
							//isOk=false;
							continue;
						}			
						
						//Capturo el color
						String color_value = sheet.getCell(2, i).getContents();				
						if (color_value.isEmpty() || color_value == null ) {
							message.append("\nError en celda C fila "+(i+1)+". Color no tiene valor.");
							isOk=false;
							break;
						}	
						
						//Capturo las tallas
						String size_value = sheet.getCell(3, i).getContents();		
						if (size_value.isEmpty() || size_value == null ) {
							message.append("\nError en celda D fila "+(i+1)+". Tallas no tiene valor.");
							isOk=false;
							break;
						}
						
						//Capturo la curva de tallas
						String sizeCurve_value = sheet.getCell(4, i).getContents();		
						if (sizeCurve_value.isEmpty() || sizeCurve_value == null ) {
							message.append("\nError en celda E fila "+(i+1)+". Curva de Tallas no tiene valor.");
							isOk=false;
							break;
						}
						
						//Capturo las cantidad de piezas de las tiendas
						Vector<Integer> qtyStores = new Vector<Integer>();
						for (int j = 5; j < 13 ; j++) {
							String qty_value = sheet.getCell(j, i).getContents();
							Integer qty = null;
							try {
								if (qty_value.isEmpty() || qty_value == null ) {
									qty=0;
								}else {
									qty = Integer.parseInt(qty_value);
								}
							} catch (NumberFormatException e) {
								isOk=false;
								message.append("\nError en celda F-M fila "+(i+1)+". Alguna cantidad no es un número.");	
								break;
							}
							qtyStores.add(qty);
						}
						
						//Capturo las cantidad total de piezas
						String totalQty_value = sheet.getCell(13, i).getContents();
						Integer totalQty = null;
						try {
							if (!totalQty_value.isEmpty() && totalQty_value != null)
							totalQty = Integer.parseInt(totalQty_value);
						} catch (NumberFormatException e) {
							isOk=false;
							message.append("\nError en celda F-M fila "+(i+1)+". Alguna cantidad no es un número.");
							break;
						}
						if (totalQty == null ) {
							isOk=false;
							message.append("\nError en celda F-M fila "+(i+1)+". Total de piezas no tiene valor.");							
							break;
						}	
						try {
							isOk = createDetailsProductDist(vendor, vendor_value, ref, ref_value, color_value, size_value, sizeCurve_value, qtyStores, totalQty);
						} catch (Exception e) {
							isOk = false;
							message.append("\nError al intentar crear el detalle de distribución de la fila "+(i+1)+".");
							e.printStackTrace();
						}
	
					if (!isOk){
						deleteDist();
						rollback();
						System.out.println("Error en línea "+(i+1));
						ADialog.error(1, new Container(), message.toString());
						return "No se pudo completar el proceso. \n\nCorrija posibles errores en el archivo a importar e intente de nuevo." +
								"\nError en línea "+(i+1)+". \n"+message.toString();
						}
					}
					if (statusDist == COMPLETE){
						MVMRDistributionHeader dist = new MVMRDistributionHeader(getCtx(), distID, null);
						dist.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE.getValue());
						dist.save();
						messageStatus.append(".\n Se distribuyeron las piezas completas de todos los productos.");
						System.out.println(message);
					}else if (statusDist == IMCOMPLETE){
						messageStatus.append(".\n Se distribuyeron las piezas incompletas para algunos productos.");
						System.out.println(message);
					}
					
					//createStorePercentDistrib();
					if (!isOk){
						rollback();
						deleteDist();
						ADialog.error(1, new Container(), message.toString());
						return "No se pudo completar el proceso. Corrija posibles errores en el archivo a importar e intente de nuevo.";
						
					}else if (statusDist== EMPTY) {
						rollback();
						deleteDist();
						return "No se creo la distribución. No hay piezas disponibles en CD para los productos a distribuir.";
					
					}else {
						commit();
					}
				} else return "No se pudo completar el proceso. Problema al crear la cabecera de la distribución.";
			}else {
				return Msg.translate( getCtx(), "6 Columns");
			}			
		} catch (BiffException e){		
			//REVISAR
			rollback();
			deleteDist();
			log.log(Level.SEVERE, e.getMessage());
		}
		return "Proceso completado. Se ha creado con éxito la distribucion No "+distID+"\n "+ messageStatus;	
	}
	
	private int createHeaderDist(){

		int distID = 0;
		X_XX_VMR_DistributionHeader dist = new X_XX_VMR_DistributionHeader(Env.getCtx(), 0, null);
		dist.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE_POR_REDISTRIBUCION_Y_APROBACION.getValue());
		dist.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
		dist.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
		dist.setXX_Product_Name("Todos los productos");
		dist.setDescription("Distribución por Excel");
		dist.setXX_VMR_DistributionType_ID(Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID")); //Distribución por piezas - 1000006
		dist.setXX_DistributionTypeApplied(Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID")); //Distribución por piezas - 1000006
		dist.setXX_AllCalculated(true);
		dist.setM_Warehouse_ID(warehouseCD);
		dist.save();
		distID = dist.getXX_VMR_DistributionHeader_ID();
		return distID;
	}
	
	private boolean createDetailsProductDist(int vendor, String vendor_value, int ref, String ref_value, String color,
			String size_value, String sizeCurve_value, Vector<Integer> qtyStores, int totalQty) throws Exception{
		
		//Arreglo con las tallas a distribuir de esta referencia y color
		String[] arraySize = size_value.split(":");
		//Arreglo con la curva de talla para los productos de esta referencia y color
		String[] arraySizeCurve = sizeCurve_value.split(":");
		//Total de piezas de una curva de talla para los productos de esta referencia y color
		int totalSizeCurve = getTotalSizeCurve(arraySizeCurve);
		//Se crea o modifica el detalle de distribución de esta referencia y color
		createDistDetail(ref, totalQty);
		//Si la cantidad de elementos de la curva de tallas no coincide con la cantidad de elementos en las tallas
		int sizeQty =arraySize.length;
		int curveQty =arraySizeCurve.length;
		prodDesc = new StringBuffer();
		prodDesc.append(vendor_value).append("-").append(ref_value).append("-").append(color);
		if( sizeQty != curveQty){
			message.append("\n"+Msg.getMsg(getCtx(), "XX_SizeDifferentCurve", 
					new String[] {Integer.toString(sizeQty), Integer.toString(curveQty)})+"\n "+prodDesc);
			return true;  //deberia ser false si no se quiere permitir este error
		}
		//Si el total de curva de talla no es multipo de la cantidad a distribuir 
		if (totalQty % totalSizeCurve != 0 || !isOk){
			message.append("\n"+Msg.translate(getCtx(), "XX_QtyNotMulCurveSize")+"\n "+prodDesc);
			return true; //deberia ser false si no se quiere permitir este error
		}
		
		for (int i = 0; i < arraySize.length; i++) {
			//Curvas de tallas a distribuir de esta referencia, color y talla
			int qtySizeCurve = Integer.parseInt(arraySizeCurve[i]);
			int prodID =0;
			boolean prodOk = true;
			prodDesc = new StringBuffer();
			prodDesc.append(vendor_value).append("-").append(ref_value).append("-").append(color).append("-").append(arraySize[i]);
			if(qtySizeCurve>0 ){
				String sql = "\nSELECT P.M_PRODUCT_ID "+
					"\nFROM M_PRODUCT P  "+
					"\nJOIN M_ATTRIBUTESETINSTANCE ASI ON (P.M_ATTRIBUTESETINSTANCE_ID = ASI.M_ATTRIBUTESETINSTANCE_ID)  "+
					"\nJOIN M_ATTRIBUTEINSTANCE AITALLA ON (AITALLA.M_ATTRIBUTESETINSTANCE_ID = P.M_ATTRIBUTESETINSTANCE_ID) "+
					"\nJOIN M_ATTRIBUTEINSTANCE AICOLOR ON (AICOLOR.M_ATTRIBUTESETINSTANCE_ID = P.M_ATTRIBUTESETINSTANCE_ID) "+
					"\nJOIN M_ATTRIBUTEVALUE AVTALLA ON (AITALLA.M_ATTRIBUTEVALUE_ID = AVTALLA.M_ATTRIBUTEVALUE_ID) "+
					"\nJOIN M_ATTRIBUTEVALUE AVCOLOR ON (AICOLOR.M_ATTRIBUTEVALUE_ID = AVCOLOR.M_ATTRIBUTEVALUE_ID) "+
					"\nWHERE  P.C_BPARTNER_ID = " +vendor+
					"\nAND P.XX_VMR_VENDORPRODREF_ID = " +ref+
					"\nAND AVCOLOR.M_ATTRIBUTE_ID = "+  Env.getCtx().getContextAsInt("#XX_L_M_ATTRIBUTECOLOR_ID")+ //1000201
					"\nAND trim(AVCOLOR.NAME) = trim('" +color+"')"+
					"\nAND AVTALLA.M_ATTRIBUTE_ID = "+ Env.getCtx().getContextAsInt("#XX_L_M_ATTRIBUTESIZE_ID")+
					"\n AND trim(AVTALLA.NAME) = trim('" +arraySize[i]+"')"; //1000258 
					
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = DB.prepareStatement(sql, null);
					rs = ps.executeQuery();
					if (rs.next()) {
						prodID = rs.getInt(1);	
						if (getQtyAvailable(prodID,1, false)>0){
							prodOk = createDistProdDetail(detailID, prodID, totalSizeCurve, qtySizeCurve, qtyStores);
							//isOk = createDistProdDetail(detailID, prodID, totalSizeCurve, qtySizeCurve, qtyStores);
							if (!prodOk){
								message.append("\n"+Msg.translate(getCtx(), "XX_ErrorCreatingProdDist")+" "+prodDesc+".");
								//ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_ErrorCreatingProdDist")); 
								//return false;
							}
						}
					}else {
						message.append("\n"+Msg.translate(getCtx(), "XX_ProductNotExist")+" "+prodDesc+".");
						//ADialog.error(1, new Container(), Msg.translate(getCtx(), "XX_ProductNotExist")); 
						//return false;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}finally{
					try {
						rs.close();
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return true;
	}

	/** Crear detalle de distribución */
	private void createDistDetail(int vendorRef, int qty) throws Exception{
		
		X_XX_VMR_VendorProdRef ref = new X_XX_VMR_VendorProdRef(Env.getCtx(), vendorRef, null);
		X_XX_VMR_DistributionDetail distD = new X_XX_VMR_DistributionDetail(Env.getCtx(), detailID, null);
		if(detailID == 0){
			distD.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
			distD.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
			distD.setXX_VMR_DistributionHeader_ID(distID);
			distD.setXX_CalculatedPER(true);
			distD.setXX_CalculatedQTY(true);
			distD.setXX_DesiredQuantity(qty);
			distD.setC_BPartner_ID(ref.getC_BPartner_ID());
			distD.setXX_DistributionApplied(true);
			distD.setXX_VMR_DistributionType_ID(Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPIECES_ID")); //Distribución por piezas - 1000006
			distD.save();
			detailID=distD.get_ID();
		}else {
			distD.setXX_DesiredQuantity(distD.getXX_DesiredQuantity()+qty);
			distD.save();
		}
	}

	private int getOldDisProdDetail(int detailID, int prodID){
		int detailProd = 0;
		String sql = "SELECT  XX_VMR_DISTRIBPRODUCTDETAIL_ID "+
		"\nFROM XX_VMR_DISTRIBPRODUCTDETAIL" +
		"\nWHERE M_PRODUCT_ID = " +prodID+" AND XX_VMR_DistributionDetail_ID ="+detailID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				detailProd = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return detailProd;
	}
	
	private int getOldDisProdStore(int distProd, int M_Warehouse_ID){
		
		int distStore = 0;
		String sql = "SELECT  XX_VMR_DISTRIBPRODPERSTORE_ID "+
		"\nFROM XX_VMR_DISTRIBPRODPERSTORE " +
		"\nWHERE XX_VMR_DISTRIBPRODUCTDETAIL_ID = " +distProd+" AND M_WAREHOUSE_ID ="+M_Warehouse_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				 distStore = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return distStore;
	}
	private boolean createDistProdDetail(int detailID, int prodID, int totalSizeCurve, int qtySizeCurve, Vector<Integer> qtyStores)  throws Exception{
		
		X_XX_VMR_DistributionDetail distD = new X_XX_VMR_DistributionDetail(Env.getCtx(), detailID, null);
		MProduct prd = new MProduct(Env.getCtx(), prodID , null);
		X_XX_VMR_VendorProdRef ref = new X_XX_VMR_VendorProdRef(Env.getCtx(), prd.getXX_VMR_VendorProdRef_ID(), null);
		boolean priceOk = true;
		//Temporal para guardar la cantidad total a distribuir de este producto
		Integer qtyProd = 0;
		//Temporal para guardar la cantidad a distribuir de este producto para cada tiendas
		Integer qtyStore =0;
		//Temporal de tienda a distribuir
		Integer store = null;
		//Temporal de distribucion de una tienda
		int distStoreID = 0;
		//Se busca si ya se distribuyó el producto en el detalle
		int distPrdID = getOldDisProdDetail(detailID,prodID);
		MVMRDistribProductDetail distPrd = null;
	
		distPrd = new MVMRDistribProductDetail(Env.getCtx(), distPrdID, get_Trx());
		
		if(distPrdID == 0){
			//Se crea detalle de producto de la distribución
			distPrd.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
			distPrd.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
			distPrd.setM_Product_ID(prodID);
			priceOk = ProductCostOrigin(prodID, qtyProd, distPrd);
			if(!priceOk) return false;
			distPrd.setM_AttributeSet_ID(prd.getM_AttributeSet_ID());
			distPrd.setM_AttributeSetInstance_ID(prd.getM_AttributeSetInstance_ID());
			distPrd.setXX_Quantity(qtyProd);
			distPrd.setXX_VMR_DistributionDetail_ID(detailID);
			distPrd.setXX_CanSetDefinitive(true);
			distPrd.save();
		}
			//Se crea la distribucion por tienda
			for (int i = 0; i < qtyStores.size(); i++) {
				qtyStore = qtyStores.get(i);
				//Verifica que la cantidad a distribuir de este producto a la tienda 
				//sea multiplo del total de la curva de talla
				if( qtyStore % totalSizeCurve == 0){	
					if(qtyStore != null && qtyStore>0){
						store = stores.get(i).get_ID();
						distStoreID =0;
						if(distPrdID !=0){
							distStoreID = getOldDisProdStore(distPrd.get_ID(), store);
						}
						
						qtyStore = (qtyStore/totalSizeCurve)*qtySizeCurve;
						X_XX_VMR_DistribProdPerStore distStore = new X_XX_VMR_DistribProdPerStore(Env.getCtx(), distStoreID,  get_Trx());
						qtyProd+= qtyStore;
						if(distStoreID == 0){
							distStore.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
							distStore.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
							distStore.setM_Warehouse_ID(store);
							distStore.setXX_VMR_DistribProductDetail_ID(distPrd.getXX_VMR_DistribProductDetail_ID());
							distStore.setXX_Quantity(qtyStore);
							//distStore.setXX_SizeCurveCompliance(true);
						}else {
							qtyStore =qtyStore + distStore.getXX_Quantity();
							distStore.setXX_Quantity(qtyStore);
						}
						distStore.save();
						
					}
				}else {
					message.append("\n"+Msg.translate(getCtx(), "XX_QtyNotMulCurveSize")+"\nTienda "+stores.get(i).getValue()+".");
					return false;
				}
			}
			//Verifica que la cantidad a distribuir del producto este disponible
			int qtyCD = getQtyAvailable(prodID, qtyProd,true);
			if(qtyCD > 0){
				if (qtyCD != qtyProd){
					statusDist = IMCOMPLETE;
				}else if (qtyCD == qtyProd && statusDist==EMPTY ){
					statusDist = COMPLETE;
				}
				int tempID =0;
				String sql = "SELECT  XX_VMR_DISTRIBDETAILTEMP_ID "+
				"\nFROM XX_VMR_DISTRIBDETAILTEMP" +
				"\nWHERE M_PRODUCT_ID = " +prodID+" AND XX_VMR_DistributionDetail_ID ="+detailID;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						tempID = rs.getInt(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally{
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
//				//Se almacena en la tabla temporal la cantidad a distribuir 
//				//de el producto a manera de reservar esa cantidad 
//				//hasta que se creen y etiqueten los pedidos de la distribucion
				BigDecimal cantTemp = new BigDecimal(0);
				X_XX_VMR_DistribDetailTemp distTmp = new X_XX_VMR_DistribDetailTemp(Env.getCtx(), tempID, get_Trx());
				distTmp.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
				distTmp.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				distTmp.setXX_ConsecutivePrice(getPriceConsecutive(distD.getM_Product_ID()));
				distTmp.setXX_VMR_DistributionDetail_ID(detailID);
				distTmp.setM_Product_ID(prodID);
				distTmp.setC_BPartner_ID(ref.getC_BPartner_ID());
				distTmp.setXX_VMR_Department_ID(ref.getXX_VMR_Department_ID());
				distTmp.setXX_VMR_Line_ID(ref.getXX_VMR_Line_ID());
				distTmp.setXX_VMR_Section_ID(ref.getXX_VMR_Section_ID());
				distTmp.setXX_VMR_Brand_ID(ref.getXX_VMR_Brand_ID());
				distTmp.setM_Warehouse_ID(warehouseCD); //Agregado Proyecto CD Valencia
				if(tempID==0){
					distTmp.setXX_DesiredQuantity(new BigDecimal(qtyCD));
				}else {
					cantTemp = distTmp.getXX_DesiredQuantity();
					cantTemp = cantTemp.add(new BigDecimal(qtyCD));
					distTmp.setXX_DesiredQuantity(cantTemp);
				}
				distTmp.save();
				if(distPrdID == 0){
					distPrd.setXX_Quantity(qtyCD);
				}else {
					int qty = distPrd.getXX_Quantity()+qtyCD;
					distPrd.setXX_Quantity(qty);
				}
				
				distPrd.save();
				commit();
			}else {
				rollback();
			}
		return true;
	}
	
	private boolean ProductCostOrigin (int mproduct_id, int pieces, X_XX_VMR_DistribProductDetail distProd) {
		
		String sql = 
			 " SELECT STO.QTY QTY, LOT.XX_SALEPRICE SALE, LOT.M_ATTRIBUTESETINSTANCE_ID LOTE , " +
			 " LOT.PRICEACTUAL PRICE, PRO.C_TAXCATEGORY_ID CAT, " +
			 " (SELECT rate/100 FROM C_Tax WHERE ValidFrom= (SELECT MAX(ValidFrom) FROM C_Tax " + 
			 "    WHERE C_TaxCategory_ID= PRO.C_TaxCategory_ID) and ROWNUM <= 1) TAXAMOUNT " +
			 " FROM M_STORAGEDETAIL STO INNER JOIN M_ATTRIBUTESETINSTANCE LOT " + 
			 " ON ( STO.M_ATTRIBUTESETINSTANCE_ID = LOT.M_ATTRIBUTESETINSTANCE_ID ) " +
			 " JOIN M_PRODUCT PRO ON (STO.M_PRODUCT_ID = PRO.M_PRODUCT_ID ) " +
			 " WHERE STO.M_PRODUCT_ID = " + mproduct_id + 
			 " AND STO.M_AttributeSetInstance_ID>=0" +
			 " AND STO.M_lOCATOR_ID >= 0" +
			 " AND STO.M_LOCATOR_ID = " + locatorCD +  
			 " AND STO.QTY > 0 " + 
			 " AND STO.QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"' " + 
			 " ORDER BY STO.M_ATTRIBUTESETINSTANCE_ID";  
	
		PreparedStatement ps = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
		double total = 0;
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) 
				total += rs.getDouble(1); 			
//			if (total < pieces ) {				
//				rs.close();
//				ps.close();
//				MProduct product = new MProduct(Env.getCtx(), mproduct_id, null);
//				String mss = Msg.getMsg(Env.getCtx(), "XX_InventoryInconsistency", 
//						new String[] {product.getValue()+ "-"+ product.getName()});
//				message.append("\n"+mss+".");
//				//ADialog.error(1, new Container(), mss);
//				return false;
//			}
			Vector<Integer> tax_cat = new Vector<Integer>();
			Vector<Double> cost = new Vector<Double>();	
			Vector <Double> sale_amount = new Vector<Double>();			
			Vector<Double> price_actual = new Vector<Double>();
			Vector<Double> tax_amount = new Vector<Double>();
			//System.out.println(sql);
			
			//Se recorre nuevamente el resultset
			rs.beforeFirst();
			while (rs.next()) {	
				sale_amount.add(rs.getDouble("SALE"));
				price_actual.add(rs.getDouble("PRICE"));				
				tax_cat.add(rs.getInt("CAT"));												
				tax_amount.add( rs.getDouble("TAXAMOUNT") * rs.getDouble("SALE") );
			
				if ( pieces - rs.getInt("QTY") > 0 ) {										
					cost.add(rs.getDouble("PRICE"));					
					pieces -= rs.getInt(2) ;
				} else {					
					cost.add(rs.getDouble("PRICE"));					
					pieces = 0 ;
					break;
				}				
			}			
			rs.close();
			ps.close();
			
			//Verify wich one is the largest cost			
			double product_cost = 0.01;	
			int max_index = -1;
			for (int j = 0; j < cost.size() ; j++) {
				if ( product_cost < cost.elementAt(j) ) {
					product_cost = cost.elementAt(j);
					max_index = j;					
				} 				
			}
			if (max_index != -1) {											
				distProd.setXX_UnitPurchasePrice(new BigDecimal(price_actual.get(max_index))) ;
				distProd.setC_TaxCategory_ID(tax_cat.get(max_index));								
				if (sale_amount.get(max_index) != 0.0) {
					
					//Solo en este caso podemos colocar margen
					distProd.setXX_Margin( new BigDecimal(
								(100*(sale_amount.get(max_index) - price_actual.get(max_index)) / sale_amount.get(max_index))
								).setScale(2, RoundingMode.HALF_EVEN));	
				}
				distProd.setXX_SalePrice(new BigDecimal(sale_amount.get(max_index)).setScale(2, RoundingMode.HALF_EVEN));
				distProd.setXX_TaxAmount(new BigDecimal(tax_amount.get(max_index)).setScale(2, RoundingMode.HALF_EVEN));
				distProd.setPriceActual(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				distProd.setXX_SalePricePlusTax(
						new BigDecimal(sale_amount.get(max_index) + tax_amount.get(max_index)).setScale(2, RoundingMode.HALF_EVEN)
				);
			} else {
				distProd.setC_TaxCategory_ID(tax_cat.get(0));	
				distProd.setXX_UnitPurchasePrice(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN)); 
				distProd.setXX_SalePrice(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				distProd.setXX_TaxAmount(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				distProd.setPriceActual(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				distProd.setXX_SalePricePlusTax(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
							
			}			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**Revisa si existe la cantidad a distribuir de un producto en cd*/
	private int getQtyAvailable(int prodID, int qtyPrd, boolean printCant){
		String sql = "\nWITH " +
				"\nTOTAL_RESERVADO AS " +
				"\n(SELECT  (CASE WHEN SUM(XX_DesiredQuantity) IS NULL THEN 0 ELSE SUM(XX_DesiredQuantity) END) RESERVADO " +
				"\nFROM XX_VMR_DISTRIBDETAILTEMP" +
				"\nWHERE M_PRODUCT_ID = " +prodID+ "AND M_WAREHOUSE_ID = "+warehouseCD+
				"\n)," +
				//Aquellos productos que esten en un pedido de una predistribuida que este pendiente por etiquetar
				"\n PREDISTRIBUIDO AS ( SELECT (CASE WHEN SUM(D.XX_DISTRIBUTEDQTY) IS NULL " +
				"\n THEN 0 ELSE SUM(D.XX_DISTRIBUTEDQTY) END) PREDIST" +
				"\nFROM XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_ORDER P " +
				"\nON (P.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) " +
				"\nWHERE  D.M_PRODUCT_ID = "+prodID+" AND P.XX_ORDERREQUESTSTATUS = '" +
					X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue() + "' AND P.AD_ORG_ID = "+orgCD+
				"\n)," +
				//Aquellos productos que esten en una predistribuida y que han sido chequeados	
				"\nCHEQUEADO AS " +
				"\n(SELECT (CASE WHEN SUM(D.XX_DISTRIBUTEDQTY) IS NULL THEN 0 ELSE SUM(D.XX_DISTRIBUTEDQTY) END) CHEQUEADO "+
				"\nFROM XX_VMR_PO_PRODUCTDISTRIB D " +
				"\nJOIN XX_VMR_DISTRIBUTIONHEADER H ON (H.XX_VMR_DISTRIBUTIONHEADER_ID = D.XX_VMR_DISTRIBUTIONHEADER_ID) " +
				"\nWHERE H.XX_DISTRIBUTIONSTATUS IN ('QR', 'QT') AND H.M_WAREHOUSE_ID = "+warehouseCD+" AND D.M_PRODUCT_ID = "+prodID+
				"), "+
				"\n TOTAL_CD AS " +
				"\n(SELECT  (CASE WHEN SUM(S.QTY) IS NULL THEN 0 ELSE SUM(S.QTY) END) CD  " +
				"\nFROM M_STORAGEDETAIL S " +
				"\nWHERE S.M_LOCATOR_ID = " + locatorCD +  
				"\nAND S.QTY > 0 " + 
				"\nAND S.QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"' " + 
				"\nAND S.M_PRODUCT_ID = "+prodID+
				"\n)" +
				"\nSELECT CD-(RESERVADO+PREDIST+CHEQUEADO)" +
				"\nFROM TOTAL_RESERVADO, TOTAL_CD,  PREDISTRIBUIDO, CHEQUEADO";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//System.out.println(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			MProduct product = new MProduct(getCtx(), prodID, null);
			if(rs.next()) {
				int qtyCD = rs.getInt(1);
				if (qtyCD < qtyPrd){
					message.append("\n"+Msg.getMsg(getCtx(), "XX_QtyNotAvailable", 
							new String[] {product.getValue()+ "-"+ product.getName()})).append(", "+prodDesc);
					if(printCant) message.append("\nCantidad disponible en CD: "+qtyCD +". Cantidad solicitada: "+qtyPrd+".");
					qtyPrd = qtyCD;
				}
			}else {
				message.append("\n"+Msg.getMsg(getCtx(), "XX_QtyNotAvailable", 
						new String[] {product.getValue()+ "-"+ product.getName()}));
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return qtyPrd;
	}
	
	private BigDecimal getPriceConsecutive(int prdID){
		BigDecimal priceC = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "Select Max(XX_PriceConsecutive) From XX_VMR_PriceConsecutive " +
				     "Where M_Product_ID = "+prdID+" AND XX_ConsecutiveOrigin = 'P'";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				priceC = rs.getBigDecimal(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return priceC;
	}
	
	private int getTotalSizeCurve(String[] arraySizeCurve) {
		
		int total = 0, temp = 0;

		try{
			for (int i = 0; i < arraySizeCurve.length; i++) {
				temp = Integer.parseInt(arraySizeCurve[i]);
				total +=temp;
			}
		}catch (Exception e) {
			isOk = false;
			e.printStackTrace();
		}

		return total;
	}

	
	/** Dado un codigo de proveedor retorna un C_BPartner_ID */
	private Integer getVendor(String vendor_value) {
		Integer returned = null;		
		if (vendor_value == null) 
			return returned;
		
		String sql = "SELECT C_BPartner_ID " +
		"FROM C_BPartner  " +
		"WHERE trim(value) = trim('"+vendor_value+"') AND ISACTIVE = 'Y' ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = rs.getInt(1);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return returned;
	}
	
	/** Dado un codigo de una referencia retorna un XX_VMR_VendorProdRef_ID */
	private Integer getReference(String ref_value) {
		Integer returned = null;		
		if (ref_value == null) 
			return returned;
		
		String sql = "SELECT XX_VMR_VendorProdRef_ID " +
		"FROM XX_VMR_VendorProdRef  " +
		"WHERE trim(value) = trim('"+ref_value+"') AND ISACTIVE = 'Y'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = rs.getInt(1);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return returned;
	}
		
	/** Obtiene todos los almacenes disponibles*/
	private void getAllStores(){
		
		String sql = "SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE WHERE VALUE!= 001 ORDER BY VALUE";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();		
			while (rs.next()){
				MWarehouse store = new MWarehouse(Env.getCtx(), rs.getInt(1), null);
				stores.add(store);
			}

		} catch (SQLException e){			
			log.log(Level.SEVERE, e.getMessage());
		}finally{
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
	}


	private void deleteDist() {
		try {
			deleteDistDetailTemp();
			deleteDistProdPerStore();
			deleteDistProdDetail();
			deleteStorePercentDistrib();
			deleteStoreQuantityDistrib();
			deleteDistDetail();
			deleteDistHeader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	private void deleteDistHeader() throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_DISTRIBUTIONHEADER WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+distID;
			System.out.println(sqlDelete);
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			message.append("\nError borrando cabecera de distribución. Distribución No: "+distID);
			 throw new Exception("Error borrando cabecera de distribución" +e.getMessage());
		}finally{
			psDelete.close();
		}
		
	}
	private void deleteDistDetail() throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_DISTRIBUTIONDETAIL WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+distID;
			System.out.println(sqlDelete);
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			message.append("\nError borrando detalle de distribución. Distribución No: "+distID);
			 throw new Exception("Error borrando detalle de distribución" +e.getMessage());
		}finally{
			psDelete.close();
		}
	}
	private void deleteDistProdDetail() throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_DistribProductDetail WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN (" +
					"SELECT XX_VMR_DISTRIBUTIONDETAIL_ID FROM XX_VMR_DISTRIBUTIONDETAIL WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+distID+")";
			System.out.println(sqlDelete);
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			message.append("\nError borrando detalle de producto de distribución. Distribución No: "+distID);
			 throw new Exception("Error borrando detalle de producto de distribución" +e.getMessage());
		}finally{
			psDelete.close();
		}
	}
	private void deleteDistProdPerStore() throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "\nDELETE FROM XX_VMR_DISTRIBPRODPERSTORE WHERE XX_VMR_DISTRIBPRODUCTDETAIL_ID IN (" +
					"\nSELECT XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
					"\nFROM XX_VMR_DISTRIBPRODUCTDETAIL PD JOIN XX_VMR_DISTRIBUTIONDETAIL DD " +
					"\nON (PD.XX_VMR_DISTRIBUTIONDETAIL_ID = DD.XX_VMR_DISTRIBUTIONDETAIL_ID) WHERE DD.XX_VMR_DISTRIBUTIONHEADER_ID ="+distID+")";
			System.out.println(sqlDelete);
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			message.append("\nError borrando detalle de distribución por tiendas. Distribución No: "+distID);
			 throw new Exception("Error borrando detalle de distribución por tiendas" +e.getMessage());
		}finally{
			psDelete.close();
		}
	}
	private void deleteStorePercentDistrib() throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_StorePercentDistrib WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN (" +
					"SELECT XX_VMR_DISTRIBUTIONDETAIL_ID FROM XX_VMR_DISTRIBUTIONDETAIL WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+distID+")";
			System.out.println(sqlDelete);
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			message.append("\nError borrando porcentajes de distribución de las tiendas. Distribución No: "+distID);
			 throw new Exception("Error borrando porcentajes de distribución de las tiendas" +e.getMessage());
		}finally{
			psDelete.close();
		}
	}
	
	private void deleteStoreQuantityDistrib() throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = "DELETE FROM XX_VMR_StoreQuantityDistrib WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN (" +
					"SELECT XX_VMR_DISTRIBUTIONDETAIL_ID FROM XX_VMR_DISTRIBUTIONDETAIL WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+distID+")";
			System.out.println(sqlDelete);
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			message.append("\nError borrando registros de cantidades de distribución de las tiendas. Distribución No: "+distID);
			 throw new Exception("Error borrando registros de cantidades de distribución de las tiendas" +e.getMessage());
		}finally{
			psDelete.close();
		}
	}
	
	private void deleteDistDetailTemp() throws Exception {
		
		PreparedStatement psDelete =null;
		try {
			String sqlDelete = " DELETE FROM XX_VMR_DISTRIBDETAILTEMP " +
			" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN ( " +
			" SELECT XX_VMR_DISTRIBUTIONDETAIL_ID FROM XX_VMR_DISTRIBUTIONDETAIL " +
				" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + distID+ " )";
			System.out.println(sqlDelete);
			psDelete = DB.prepareStatement(sqlDelete,get_TrxName());
			psDelete.execute();
			commit();
		}catch (Exception e) {
			message.append("\nError borrando registros de tabla temporal de distribucion. Distribución No: "+distID);
			 throw new Exception("Error borrando registros de tabla temporal de distribucion" +e.getMessage());
		}finally{
			psDelete.close();
		}
	}
	
	private void createStorePercentDistrib(){

		
		MVMRDistributionHeader distH = new MVMRDistributionHeader(Env.getCtx(), distID, null);
		X_XX_VMR_DistributionDetail detail = null;
		String sql ="SELECT XX_VMR_DistributionDetail_ID FROM XX_VMR_DistributionDetail " +
		"WHERE XX_VMR_DistributionHeader_ID = "+distID;
		String sql2 ="";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		//System.out.println(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				detail = new X_XX_VMR_DistributionDetail(Env.getCtx(), rs.getInt(1), null);

				sql2 ="SELECT s.M_Warehouse_ID, sum(s.XX_Quantity) FROM XX_VMR_DistribProductDetail p " +
				"JOIN XX_VMR_DistribProdPerStore s ON (p.XX_VMR_DistribProductDetail_ID = s.XX_VMR_DistribProductDetail_ID) " +
				"WHERE p.XX_VMR_DistributionDetail_ID = "+detail.get_ID()+" "+
				"GROUP BY s.M_Warehouse_ID";
				//System.out.println(sql2);
				pstmt2 = null;
				rs2 = null;
				
				try {
					pstmt2 = DB.prepareStatement(sql2, null);
					rs2 = pstmt2.executeQuery();
			
					while(rs2.next()) {
						X_XX_VMR_StorePercentDistrib percent = new X_XX_VMR_StorePercentDistrib(Env.getCtx(), 0, null);
						percent.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
						percent.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
						percent.setXX_VMR_DistributionDetail_ID(detail.get_ID());
						percent.setXX_VMR_DistributionType_ID(distH.getXX_VMR_DistributionType_ID());
						percent.setM_Warehouse_ID(rs2.getInt(1));
						percent.setXX_Percentage(BigDecimal.valueOf(rs2.getInt(2)*100/detail.getXX_DesiredQuantity()));
						percent.save();
						X_XX_VMR_StoreQuantityDistrib qty = new X_XX_VMR_StoreQuantityDistrib(Env.getCtx(), 0, null);
						qty.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
						qty.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
						qty.setXX_VMR_DistributionDetail_ID(detail.get_ID());
						qty.setXX_VMR_DistributionType_ID(distH.getXX_VMR_DistributionType_ID());
						qty.setM_Warehouse_ID(rs2.getInt(1));
						qty.setXX_Quantity(rs2.getInt(2));
						qty.save();
					}
				} catch (SQLException e) {
					isOk = false;
					e.printStackTrace();
				} finally{
					try {
						rs2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						pstmt2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			isOk = false;
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
