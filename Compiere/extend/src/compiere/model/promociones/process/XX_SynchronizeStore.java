package compiere.model.promociones.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import sun.net.ftp.FtpClient;

import compiere.model.promociones.process.model.XX_Promotion;
import compiere.model.promociones.process.model.XX_Store;

public class XX_SynchronizeStore extends SvrProcess {
	
	public static Vector<XX_Store> stores = new Vector<XX_Store>();
	public static Vector<XX_Promotion> promotions = new Vector<XX_Promotion>();
	public static Vector<XX_Promotion> synchronizedPromotions = new Vector<XX_Promotion>();
	public static Vector<XX_Promotion> deletedPromotions = new Vector<XX_Promotion>();
	
	public static String promotionPath="";
	public static String detailPromotionExtPath="";
	public static String detailPromotionPath="";
	public static String conditionPath="";
	public static String winningControlTransactionPath="";
	public static String donationPath="";
	
	public static String promotionsBody="";
	public static String detailPromotionsBody="";
	public static String detailPromotionsExtBody="";
	public static String conditionBody="";
	public static String winningControlTransactionBody="";
	public static String donationBody="";
	
	static File promotionsFile = null;
	static File detailPromotionsFile = null;
	static File detailPromotionsExtFile = null;
	static File conditionFile = null;
	static File winningControlTransactionFile=null;
	static File donationFile=null;
	
	public static int sizeLine=38;
	
	public static String winningTransaction="A";//transacción premiada
	public static String gifCarByPurchase="B";//Bono Regalo por compra
	public static String decals="C";//Calcomanias
	public static String raffle="D";//Sorteo
	public static String giftWithPurchase="E";//Producto Complemetario
	public static String purchaseSavings="F";// Ahorro en compra
	public static String sponsoredProducts="G";//Productos Patrocinados 
	public static String freeProduct="H";//Mas barato Gratis
	public static String nextProductDiscountsProduct="I";//Combo por producto
	public static String discountCoupon="L";//Cupon de descuento por porcentaje
	public static String corporate = "O";//Corporativa
	public static String clasic="P";//Promocion tradicional de beco
	public static String sponsoredLine="R";//Productos Patrocinados por lineas
	public static String nextProductDiscountsDepartmen="S";//Combo por departamento
	public static String nextProductDiscountsLine="T";//Combo por linea
	public static String nextProductDiscountsSection="U";//Combo por Sección
	public static String nextProductDiscountsCategory="X";//Combo po Category
	
	public static String nextProductDiscountsReference="W";//Combo por producto
	
	public static boolean concatenar = false;

	//De Compiere
	protected void prepare() {
		System.out.println("INICIANDO TRANSFERENCIA DE PROMOCIONES:"+ Calendar.getInstance().getTime());
		stores.clear();
		stores = getStores();
		promotions.clear();
		promotions = getPromotions();
		synchronizedPromotions.clear();
		synchronizedPromotions = getSynchronizedPromotions();
		deletedPromotions.clear();
		deletedPromotions=getDeletedPromotion();
		promotionPath="promociones_Ext";
		detailPromotionExtPath="detPromocionesExt";
		detailPromotionPath="detPromociones_Ext";
		conditionPath="condicionPromocion";
		winningControlTransactionPath="transaccionPremControl";
		donationPath="donacion";
	}
	@Override
	protected String doIt() throws Exception {
		Iterator<XX_Store> i = stores.iterator();

				while (i.hasNext()){
					try{	
						XX_Store s = i.next();
						System.out.println("REALIZANDO TRANSFERENCIA DE PROMOCIONES PARA TIENDA "+s.getId());
						createPromotionalFile(s.getRoot());
						// Se Cambio la posicion de esta funcion, ya que cuando en una misma promocion existe el mismo producto
						// activo y desactivo pasaba primero el activo y despues el desactivo.
						createDeleteDetailPromotion(s);
						createDetailPromotionFile(s);
						createDetailPromotionExtFile(s);
						createConditionsPromotionsFile(s.getId(), s.getRoot());
						conditionBody="";
						createWinningControlTransactionFile(s.getId(), s.getRoot());	
						winningControlTransactionBody="";
						createDonation(s.getId(), s.getRoot());
						donationBody="";
						//createNewConsecutive(s.getId(), s.getRoot());
						createNewDetail(s);
						BufferedWriter bw;
						try {
							bw = new BufferedWriter(new FileWriter(s.getRoot()+detailPromotionPath));
							bw.write(detailPromotionsBody);
							bw.close();
						} catch (IOException e) {e.printStackTrace();}
						BufferedWriter bw1;
						try {
							bw1 = new BufferedWriter(new FileWriter(s.getRoot()+detailPromotionExtPath, concatenar));
							bw1.write(detailPromotionsExtBody);
							bw1.close();
							System.out.println("ESCRIBIENDO ARCHIVO DE PROMOCIONES PARA TIENDA "+s.getId());
						} catch (IOException e) {e.printStackTrace();}
						detailPromotionsBody="";detailPromotionsExtBody="";
						
						//consultar IP del Servidor
						InetAddress address = InetAddress.getByName(""+s.getIp()+"");
						boolean ipActiva = address.isReachable(5000);
						
						if(ipActiva)
						{
							System.out.println("ENVIANDO ARCHIVO DE PROMOCIONES PARA TIENDA "+s.getId());
							sendPromotionalFile(s.getRoot()+promotionPath,s.getPut(),s.getIp(),s.getUser(),s.getPass(),promotionPath, false);
							sendPromotionalFile(s.getRoot()+conditionPath,s.getPut(),s.getIp(),s.getUser(),s.getPass(),conditionPath, false);
							sendPromotionalFile(s.getRoot()+winningControlTransactionPath,s.getPut(),s.getIp(),s.getUser(),s.getPass(),winningControlTransactionPath, false);
							sendPromotionalFile(s.getRoot()+detailPromotionPath,s.getPut(),s.getIp(),s.getUser(),s.getPass(),detailPromotionPath, false);
							sendPromotionalFile(s.getRoot()+detailPromotionExtPath,s.getPut(),s.getIp(),s.getUser(),s.getPass(),detailPromotionExtPath, false);
							sendPromotionalFile(s.getRoot()+donationPath,s.getPut(),s.getIp(),s.getUser(),s.getPass(),donationPath, false);
							System.out.println("ENVIADO ARCHIVO DE PROMOCIONES PARA TIENDA "+s.getId());
						}
						concatenar=false;
					
					}catch(Exception e){
						System.out.println("Posible Errores, Dirección del Archivo erronea");
					}
				}
		System.out.println("ACTUALIZANDO PROMOCIONES COMO SINCRONIZADAS");
		synchronizedUpdate();
		System.out.println("FIN DE SINCRONIZACION DE PROMOCIONES:"+Calendar.getInstance().getTime());
		
		return "Proceso Realizado con exito";
	}
	//Creación de archivos para los servidores de tienda	
	private void createDonation(String idSt, String root) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if (donationFile == null){
			donationFile = new File(root, donationPath);
		}
		
		String sql="SELECT  a.XX_VME_Donations_ID, DateFrom, DateFinish, a.name, ELEMENTTYPE, AMOUNT, total, a.IsActive " +
				"FROM XX_VME_Donations a inner join XX_VME_DONATIONSTORE b on a.XX_VME_Donations_ID=b.XX_VME_Donations_ID and b.isactive like 'Y' " +
				"inner join m_warehouse c on b.M_WAREHOUSE_ID=c.M_WAREHOUSE_ID " +
				"WHERE (DateFrom>=current_date OR DateFinish>=current_date) " +
				"AND XX_ApproveMar like 'Y' AND XX_Synchronized<>a.IsActive and c.value='"+idSt+"'";
		try{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			   rs = pstmt.executeQuery();
			int detail=0;
			while (rs.next()){
				donationBody += rs.getString("XX_VME_Donations_ID")+"["+detail
				+"["+rs.getString("DateFrom").substring(8, 10)+"/"+rs.getString("DateFrom").substring(5, 7)+"/"+rs.getString("DateFrom").substring(0, 4)
				+"["+rs.getString("DateFinish").substring(8, 10)+"/"+rs.getString("DateFinish").substring(5, 7)+"/"+rs.getString("DateFinish").substring(0, 4)
				+"["+rs.getString("name")+"["+rs.getString("name")+"["+rs.getString("ELEMENTTYPE")+"["+rs.getString("IsActive")
				+"["+rs.getString("AMOUNT")+"[N["+rs.getString("TOTAL")+"[\r\n";
			}
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(root+donationPath));
				bw.write(donationBody);
				bw.close();
			} catch (IOException e) {e.printStackTrace();}
		} catch (Exception e) {e.printStackTrace();}
		finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
	}
	private void createConditionsPromotionsFile(String idSt, String root) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if (conditionFile == null){
			conditionFile = new File(root, conditionPath);
		}
		Iterator<XX_Promotion> i = promotions.iterator();
		while (i.hasNext()){
			XX_Promotion p = i.next();
			if (p.getType().equals(raffle)){
				String sql="select XX_conditions from XX_VMR_DetailPromotionExt e " +
						"INNER JOIN xx_vmr_promocondwarehouse p ON e.XX_VMR_PROMOCONDITIONVALUE_ID=p.XX_VMR_PROMOCONDITIONVALUE_ID " +
						"where XX_VMR_Promotion_ID="+p.getId()+" AND (p.XX_WarehouseBecoNumber='000' OR p.XX_WarehouseBecoNumber='"+idSt+"')";

				
				try{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					   rs = pstmt.executeQuery();
					while (rs.next()){
						Iterator<String> lin = dividedIntoLines(sizeLine,  cleanName(rs.getString("XX_conditions"))).iterator();
						int order=0;
						while(lin.hasNext())
						conditionBody += (p.getId()+"["+order+++"["+lin.next()+"[N[\r\n").replace('¬','*');
					}
					BufferedWriter bw;
					try {
						bw = new BufferedWriter(new FileWriter(root+conditionPath));
						bw.write(conditionBody);
						bw.close();
					} catch (IOException e) {e.printStackTrace();}
				} catch (Exception e) {e.printStackTrace();}
				finally{
					DB.closeStatement(pstmt);
					DB.closeResultSet(rs);
				}
			}
		}
	}
	private void createDetailPromotionFile(XX_Store s) {
		if (detailPromotionsFile == null)
			detailPromotionsFile = new File(s.getRoot(), detailPromotionPath);
		Iterator<XX_Promotion> i = promotions.iterator();
		while (i.hasNext()){
			XX_Promotion p = i.next();
			if (p.getType().equals(clasic))getDetailPromotion(p.getId(),s, false);
		}
	}	
	private void createDetailPromotionExtFile(XX_Store s) {
		if (detailPromotionsExtFile == null)
			detailPromotionsExtFile = new File(s.getId(), detailPromotionPath);
		Iterator<XX_Promotion> i = promotions.iterator();
		while (i.hasNext()){
			XX_Promotion p = i.next();
			if (!p.getType().equals("P")) getDetailPromotionExt(p.getId(), p.getType(),false, s);
		}
	}		
	private void createPromotionalFile(String root) throws IOException {	
		if (promotionsFile == null){
			promotionsFile = new File(root, promotionPath);
		}
		Iterator<XX_Promotion> i = promotions.iterator();
		while (i.hasNext()){
			XX_Promotion p = i.next();
			promotionsBody += 	p.getId()+"["+p.getType()+"["+p.getStartDate().substring(0,4)+"-"+p.getStartDate().substring(5,7)
								+"-"+p.getStartDate().substring(8, 10)+"["+(p.getStartTime()==null?"00:00:00":p.getStartTime().substring(11, 19))+"["+
								p.getEndDate().substring(0,4)+"-"+p.getEndDate().substring(5,7)+"-"+p.getEndDate().substring(8, 10)+"["+
								(p.getEndTime()==null?"23:59:59":p.getEndTime().substring(11, 19))+"["+p.getPriority()+"[\r\n";
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(root+promotionPath));
		bw.write(promotionsBody);
		bw.close();
		promotionsBody = "";
	}
	private void createWinningControlTransactionFile(String idSt, String root){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if (winningControlTransactionFile == null){
			winningControlTransactionFile = new File(root, winningControlTransactionPath);
		}
		Iterator<XX_Promotion> i = promotions.iterator();
		while (i.hasNext()){
			XX_Promotion p = i.next();
			if (p.getType().equals(winningTransaction)){
				String sql="select XX_QuantityPurchase, xx_DiscountAmount from XX_VMR_DetailPromotionExt e" +
						"INNER JOIN xx_vmr_promocondwarehouse p ON e.XX_VMR_PROMOCONDITIONVALUE_ID=p.XX_VMR_PROMOCONDITIONVALUE_ID " +
						"where XX_VMR_Promotion_ID="+p.getId()+" AND (p.XX_WarehouseBecoNumber='000' OR p.XX_WarehouseBecoNumber='"+idSt+"')";
				

				try{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					   rs = pstmt.executeQuery();
					while (rs.next()){
						winningControlTransactionBody += rs.getString("XX_QuantityPurchase")+"["+rs.getString("XX_QuantityPurchase")
						+"["+rs.getString("xx_DiscountAmount")+"["+rs.getString("xx_DiscountAmount")+"["+p.getId()+"[\r\n";
					}
					BufferedWriter bw;
					try {
						bw = new BufferedWriter(new FileWriter(root+winningControlTransactionPath));
						bw.write(winningControlTransactionBody);
						bw.close();
					} catch (IOException e) {e.printStackTrace();}
				} catch (Exception e) {e.printStackTrace();}
				finally{
					DB.closeStatement(pstmt);
					DB.closeResultSet(rs);
				}
			}
		}
	}
	//Utilitarios
	private String cleanName(String name) {
		// TODO Apéndice de método generado automáticamente
		name=name.replace("ñ", "ni");
		name=name.replace("Ñ", "NI");
		name=name.replace("á", "a");
		name=name.replace("Á", "A");
		name=name.replace("é", "e");
		name=name.replace("É", "é");
		name=name.replace("í", "í");
		name=name.replace("Í", "I");
		name=name.replace("ó", "o");
		name=name.replace("Ó", "o");
		name=name.replace("ú", "u");
		name=name.replace("Ú", "U");
		name=name.replace("'", "");
		name=name.replace("?", "");
		name=name.replace("¿", "");
		return name;
	}	
	private Vector<String> dividedIntoLines(int size, String text) {
		Vector<String> lineasResultado = new Vector<String>();
		int letras = 0;
		boolean dividir = false;
		if (text!=null){
			do {
				// Colocamos las palabras de las lineas
				dividir = ((letras + size) < text.length());
				String lineaActual = ((letras + size) < text.length())
							? text.substring(letras, letras + size)
							: text.substring(letras);
				int j = lineaActual.length();
				if (lineaActual.contains("\n")){
					lineaActual = lineaActual.replace('\n', '¬');
					dividir=true;
			}
				
				if (dividir) {
					// Buscamos donde cortar las palabras para la siguiente linea
					while ((j>1)
							&& (lineaActual.charAt(j-1) != '.') 
							&& (lineaActual.charAt(j-1) != ' ') 
							&& (lineaActual.charAt(j-1) != ',') 
							&& (lineaActual.charAt(j-1) != ':')
							&& (lineaActual.charAt(j-1) != '¬')){
						j--;
					}
				}
				if (j<=1)
					if (j<0){
						lineaActual = lineaActual.substring(lineasResultado.lastElement().length(),lineaActual.length());
						letras += lineasResultado.lastElement().length();
					}
					j = lineaActual.length();
				lineasResultado.addElement(lineaActual.substring(0,j));
				
				letras += j;
			} while (letras < text.length());
		}
		return lineasResultado;
	}	
	// Necesidades Extras
	private String defineLevel(int type, int id) {//Define en que nivel del seccionario se va a bajar la promocion
		String sql=	"SELECT XX_VMR_Category_ID, XX_VMR_Department_ID, XX_VMR_Line_ID, XX_VMR_Section_ID" +
					", XX_VMR_VendorProdRef_ID, XX_VMR_Brand_ID, M_Product_ID " +
					"FROM XX_VMR_DetailPromotionExt WHERE XX_VMR_Promotion_ID="+id;
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			int level=0;//1=Categoria, 2=Departamento, 3=Linea, 4=Seccion, 5=Producto 6 Referencia
			while (rs.next()){
				if (rs.getString("XX_VMR_Category_ID")==null)Msg.translate(get_TrxName().getTrxName(),"Invalid Detail");
				else if (((rs.getString("XX_VMR_Department_ID")!= null && rs.getString("XX_VMR_Brand_ID")!= null) || (rs.getString("XX_VMR_VendorProdRef_ID")!= null) || (rs.getString("XX_VMR_Section_ID")!=null)) && rs.getString("M_Product_ID")==null && level<1) level=6;
				else if (rs.getString("XX_VMR_Department_ID")== null && level<1)level=1;
				else if (rs.getString("XX_VMR_Line_ID")==null && level<2 && rs.getString("XX_VMR_Department_ID")!= null && level != 6)level=2;
				else if (rs.getString("XX_VMR_Section_ID")==null && level<3 && rs.getString("XX_VMR_Line_ID")!=null && level != 6)level=3;
				//else if (rs.getString("M_Product_ID")==null && rs.getString("XX_VMR_VendorProdRef_ID")==null && rs.getString("XX_VMR_Brand_ID")==null && level<4 && rs.getString("XX_VMR_Section_ID")!=null) level=4;
				else if ((rs.getString("M_Product_ID")!=null/* || rs.getInt("XX_VMR_VendorProdRef_ID")!=0 || rs.getInt("XX_VMR_Brand_ID")!=0*/) && level!=5 && rs.getString("XX_VMR_Section_ID")!=null && rs.getString("XX_VMR_VendorProdRef_ID")!=null) {level=5; break;}
			}
			rs.close();
			pstmt.close();
			if (level==0)Msg.translate(get_TrxName().getTrxName(),"Invalid Detail");
			if (type==1000300){
				if (level<4)return sponsoredLine;
				/*else
					if (level==6)
						return nextProductDiscountsReference; WDIAZ OJO Cuando se cambie la Caja que acepte Productos Patrocinados por Referencia se descomoenta esta parte*/
					else return sponsoredProducts;
			} else // solo queda la opcion de combo 1000400
				switch (level){
				case 1: return nextProductDiscountsCategory; 
				case 2: return nextProductDiscountsDepartmen;
				case 3: return nextProductDiscountsLine;
				case 4: return nextProductDiscountsSection;
				case 5: return nextProductDiscountsProduct;
				case 6: return nextProductDiscountsReference;
				}	
		}catch (SQLException e){e.printStackTrace();}
		return null;
	}
	
	//Query para buscar las promociones nuevas que estan aprobadas y no sincronizadas
	
	private Vector<XX_Promotion> getPromotions() {
		String sql ="SELECT  XX_VMR_Promotion_ID, XX_TypePromotion, DateFrom, TimeSlotStart, DateFinish" +
				", TimeSlotEnd, Priority, XX_Synchronized, IsActive " +
				"FROM XX_VMR_Promotion WHERE (DateFrom>=current_date OR  (DateFinish + interval '1439' MINUTE)>=current_date) " +
				"AND (XX_ApproveMar like 'Y' OR XX_ApproveMer like 'Y') AND XX_Synchronized like 'N' and  IsActive like 'Y'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next()){
				int id=rs.getInt("XX_VMR_Promotion_ID");
				int typeAux=rs.getInt("XX_TypePromotion");
				String startDate=rs.getString("DateFrom");
				String startTime=rs.getString("TimeSlotStart");
				String endDate=rs.getString("DateFinish");
				String endTime=rs.getString("TimeSlotEnd");
				int priority=rs.getInt("Priority");
				String sync=rs.getString("XX_Synchronized");
				String act=rs.getString("IsActive");
				String type= "";
					switch (typeAux){
					case 1000100: type=purchaseSavings; break;
					case 1000200: type=freeProduct; break;
					case 1000300: type=defineLevel(typeAux, id); break;
					case 1000400: type=defineLevel(typeAux, id); break;
					case 1000500: type=corporate; break;
					case 1000600: type=decals; break;
					case 1000700: type=winningTransaction; break;
					case 1000800: type=gifCarByPurchase; break;
					case 1000900: type=discountCoupon; break;
					case 1001000: type=giftWithPurchase; break;	
					case 1001100: type=clasic; break;
					case 1001200: type=raffle; break;
					}
				promotions.add(new XX_Promotion(id, type, startDate, startTime, endDate, endTime, priority, sync, act));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return promotions;
	}	
	private Vector<XX_Store> getStores() {
		String sql=	"SELECT Value, IP_Address, Username, Password, XX_RootFTP, XX_PutFTP " +
					"FROM XX_VSI_WAREHOUSENET a INNER JOIN M_Warehouse b " +
					"ON a.M_Warehouse_ID=b.M_Warehouse_ID WHERE a.IsActive LIKE 'Y' " +
					"ORDER BY Value";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next()){
				String id=rs.getString(1);
				String ip=rs.getString(2);
				String user=rs.getString(3);
				String pass=rs.getString(4);
				String root=rs.getString(5);
				String put=rs.getString(6);
				stores.add(new XX_Store(id, ip, user, pass, root, put));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}		
		return stores;
	}
	public String getDetailPromotion(int idPromotion, XX_Store s, boolean delete){
		DecimalFormat dfc = new DecimalFormat("000");
		DecimalFormat dfp = new DecimalFormat("000000000");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String idStore = s.getId();
		String conditions = getConditions(idPromotion,idStore);
		if(conditions!=null){
		String sql=	"SELECT XX_VMR_Promotion_ID, XX_DiscountRate, b.value as departamento, null as linea, null as producto" +
				", (select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
				", null consecutivo, XX_DiscountAmount, a.isactive " +
				" FROM XX_VMR_DetailPromotionExt a inner join XX_VMR_DEPARTMENT b " +
					" on (a.XX_VMR_CATEGORY_ID=b.XX_VMR_CATEGORY_ID and a.XX_VMR_DEPARTMENT_ID is null and a.XX_VMR_Line_ID is null " +
						" and a.XX_VMR_section_id is null and a.m_product_id is null and a.XX_VMR_PRICECONSECUTIVE_ID is null " +
						" and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null) " +
						" WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID=" +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )"+
				" union " +
				" SELECT distinct XX_VMR_Promotion_ID, XX_DiscountRate" +
				", (SELECT Value FROM XX_VMR_DEPARTMENT WHERE XX_VMR_DEPARTMENT_ID=a.XX_VMR_DEPARTMENT_ID) AS Departamento" +
				", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=a.XX_VMR_Line_ID) AS Linea, null as producto" +
				", (select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name, null consecutivo" +
				", XX_DiscountAmount, a.isactive " +
				" FROM XX_VMR_DetailPromotionExt a inner join XX_VSI_CATEGDEPARTTLINEVIEW b " +
				" on (a.XX_VMR_CATEGORY_ID=b.XX_VMR_CATEGORY_ID and a.XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID " +
						" and a.XX_VMR_Line_ID=b.XX_VMR_Line_ID and a.XX_VMR_section_id is null and a.m_product_id is null " +
						" and a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null) " +
					" or (a.XX_VMR_CATEGORY_ID=b.XX_VMR_CATEGORY_ID and a.XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID " +
						" and a.XX_VMR_Line_ID is null and a.XX_VMR_section_id is null and a.m_product_id is null " +
						" and a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null)" +
						" WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID=" +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )"+
				" union " +
				" select XX_VMR_Promotion_ID, XX_DiscountRate" +
				", (SELECT Value FROM XX_VMR_DEPARTMENT WHERE XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID) AS Departamento" +
				", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=b.XX_VMR_Line_ID) AS Linea, b.value" +
				", (select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name" +
				", c.XX_PRICECONSECUTIVE, XX_DiscountAmount, a.isactive " +
				" from XX_VMR_DetailPromotionExt a inner join m_product b " +
				" on (a.xx_vmr_section_id=b.xx_vmr_section_id and a.m_product_id is null and a.XX_VMR_PRICECONSECUTIVE_ID is null " +
						" and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null) " +
					" or (a.xx_vmr_section_id=b.xx_vmr_section_id and a.m_product_id=b.m_product_id " +
						" and a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null) " +
					" or (a.xx_vmr_section_id=b.xx_vmr_section_id and a.m_product_id=b.m_product_id " +
						" and a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID =b.XX_VMR_VENDORPRODREF_ID) " +
					" or (a.XX_VMR_Category_ID = b.XX_VMR_Category_ID and a.XX_VMR_Department_ID = b.XX_VMR_Department_ID and a.m_product_id is null and a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_BRAND_ID=b.XX_VMR_BRAND_ID " +
						"and a.XX_VMR_VENDORPRODREF_ID is null) " +
					" or (a.xx_vmr_section_id=b.xx_vmr_section_id and a.m_product_id is null and a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_BRAND_ID is null " +
						"and a.XX_VMR_VENDORPRODREF_ID=b.XX_VMR_VENDORPRODREF_ID) " +
				" inner join XX_VMR_PRICECONSECUTIVE c " +
				//" on a.XX_VMR_PRICECONSECUTIVE_ID=c.XX_VMR_PRICECONSECUTIVE_ID " +
					" ON (b.m_product_id=c.m_product_id)" +
					" WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID=" +idPromotion+
					" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" ) ORDER BY XX_VMR_Promotion_ID";
		try{
			 pstmt = DB.prepareStatement(sql, get_TrxName());
			    rs = pstmt.executeQuery();
			int detail=1;
			while (rs.next()) 
				detailPromotionsBody += rs.getInt("XX_VMR_Promotion_ID")+"["+detail+++"[["+rs.getString("departamento")
				+"["+(rs.getShort("Linea")==0?"\\N":rs.getShort("Linea"))+"["
				+(rs.getInt("producto")==0?"\\N":(dfp.format(rs.getInt("producto"))+dfc.format(rs.getInt("consecutivo"))))
				+"["+ rs.getDouble("XX_DiscountRate")+"["+ rs.getDouble("XX_DiscountAmount")+"["+(delete?"E":(rs.getString("isactive").equals("Y")?"A":"E"))+"\r\n";
		} catch (Exception e) {e.printStackTrace();}
		finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}	
		
		}
		return detailPromotionsBody;
	}
	private String getConditions(int idPromotion, String idStore) {
		String sql = "SELECT pcv.XX_VMR_PROMOCONDITIONVALUE_ID FROM XX_VMR_PROMOCONDITIONVALUE pcv " +
				"INNER JOIN XX_VMR_PROMOCONDWAREHOUSE pcw ON pcv.XX_VMR_PROMOCONDITIONVALUE_ID=pcw.XX_VMR_PROMOCONDITIONVALUE_ID " +
				"WHERE XX_VMR_Promotion_ID = " +idPromotion+" AND (XX_WarehouseBecoNumber='000' OR XX_WarehouseBecoNumber='"+idStore+"')";
		String conditions = "";
		PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			while(rs.next()){
				conditions += rs.getInt(1)+", ";
			}

			if(conditions.length()>0){
				conditions = conditions.substring(0, conditions.length()-2);
				return conditions;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
				
		return null;
	}
	
	public String getDetailPromotionExtTI(int idPromotion,String typePromotion,  boolean delete, XX_Store s, String path){
		detailPromotionExtPath=path;
		return  getDetailPromotionExt(idPromotion,typePromotion, delete, s);
	}
	private String getDetailPromotionExt(int idPromotion,String typePromotion,  boolean delete, XX_Store s){
		String sql="";
		String idSt = s.getId();
		String conditions = getConditions(idPromotion,idSt);
		if(conditions!=null){
		if (typePromotion.equals(winningTransaction) || typePromotion.equals(gifCarByPurchase) || typePromotion.equals(decals)
			||  typePromotion.equals(freeProduct) || typePromotion.equals(discountCoupon)
			|| typePromotion.equals(corporate)){
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			sql="SELECT XX_VMR_Promotion_ID, XX_DiscountRate, XX_MinimumPurchase, XX_QuantityPurchase" +
					", XX_AmountGifted, XX_GiftAccumulate, XX_CONDITIONS, XX_DiscountAmount, XX_Gift, " +
					"(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
					"from XX_VMR_DetailPromotionExt a WHERE XX_VMR_Promotion_ID= " +idPromotion+
					" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			try{
				 pstmt = DB.prepareStatement(sql, get_TrxName());
				    rs = pstmt.executeQuery();
				while (rs.next())
					detailPromotionsExtBody+=rs.getInt("XX_VMR_Promotion_ID")+"[1["+rs.getInt("XX_DiscountRate")
					+"[NULL[NULL[NULL[NULL[NULL["+rs.getDouble("XX_MinimumPurchase")+"["+rs.getInt("XX_QuantityPurchase")
					+"["+rs.getInt("XX_AmountGifted")+"[NULL["+rs.getDouble("XX_DiscountAmount")+"[A["+cleanName(rs.getString("name"))
					+"[NULL[NULL["+rs.getString("XX_GifTAccumulate")+"[\r\n";
			} catch (Exception e) {e.printStackTrace();}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}else{
			if(typePromotion.equals(raffle) || typePromotion.equals(giftWithPurchase)){
		
				sql= "SELECT XX_VMR_Promotion_ID, XX_DiscountRate,XX_MinimumPurchase, XX_QuantityPurchase, "+
						" XX_AmountGifted, XX_GiftAccumulate, XX_ProductSelection, a.IsActive,  "+
						" XX_GIFT as name , "+
						" (SELECT Value FROM XX_VMR_CATEGORY WHERE XX_VMR_CATEGORY_ID=a.XX_VMR_CATEGORY_ID) AS Categoria, "+
						" b.value as departamento,null as linea,null as seccion, null as producto, null as referenciaProveedor , "+
						" null XX_PriceConsecutive, XX_DiscountAmount, null as marca, XX_GroupDiscount, XX_GiftAccumulate "+
					" FROM XX_VMR_DetailPromotionExt a inner join XX_VMR_DEPARTMENT b on "+
						" (a.XX_VMR_CATEGORY_ID=b.XX_VMR_CATEGORY_ID and a.XX_VMR_DEPARTMENT_ID "+
						" is null and a.XX_VMR_Line_ID is null and a.XX_VMR_section_id is null "+
						" and a.m_product_id is null and a.XX_VMR_PRICECONSECUTIVE_ID is null "+
						" and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null) "+
						" OR (a.XX_VMR_CATEGORY_ID=b.XX_VMR_CATEGORY_ID and "+
						" a.XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID and a.XX_VMR_Line_ID is "+
						" null and a.XX_VMR_section_id is null and a.m_product_id is null and "+
						" a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_BRAND_ID is null and "+
						" a.XX_VMR_VENDORPRODREF_ID is null) "+
					" WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID="+idPromotion+" AND "+
						" XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )"+
					" union "+
					" SELECT distinct XX_VMR_Promotion_ID, XX_DiscountRate ,XX_MinimumPurchase, XX_QuantityPurchase,  "+
						" XX_AmountGifted, XX_GiftAccumulate, XX_ProductSelection, a.IsActive, "+
						" XX_GIFT as name, "+
						" (SELECT Value FROM XX_VMR_CATEGORY WHERE XX_VMR_CATEGORY_ID=a.XX_VMR_CATEGORY_ID) AS Categoria, "+
						" (SELECT Value FROM XX_VMR_DEPARTMENT WHERE XX_VMR_DEPARTMENT_ID=a.XX_VMR_DEPARTMENT_ID) AS Departamento, "+
						" (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=a.XX_VMR_Line_ID) AS Linea, null as seccion, "+
						" null as producto , null as referencia , null consecutivo , XX_DiscountAmount, null as marca, XX_GroupDiscount, XX_GiftAccumulate "+
					" FROM XX_VMR_DetailPromotionExt a inner join XX_VSI_CATEGDEPARTTLINEVIEW b on "+
						" (a.XX_VMR_CATEGORY_ID=b.XX_VMR_CATEGORY_ID and a.XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID and "+
						" a.XX_VMR_Line_ID=b.XX_VMR_Line_ID and a.XX_VMR_section_id is null and a.m_product_id is null and a.XX_VMR_PRICECONSECUTIVE_ID is null and "+
						" a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null) "+
					" WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID="+idPromotion+" AND "+
						" XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )"+
					" union "+
					" select XX_VMR_Promotion_ID, XX_DiscountRate ,XX_MinimumPurchase, XX_QuantityPurchase,  "+
					" XX_AmountGifted, XX_GiftAccumulate, XX_ProductSelection, a.IsActive, "+
					" XX_GIFT as name , "+
					" (SELECT Value FROM XX_VMR_CATEGORY WHERE XX_VMR_CATEGORY_ID=a.XX_VMR_CATEGORY_ID) AS Categoria, "+
					" (SELECT Value FROM XX_VMR_DEPARTMENT WHERE XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID) AS Departamento , "+
					" (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=b.XX_VMR_Line_ID) AS Linea, "+
					" (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=b.XX_VMR_Section_ID) AS Section, b.value AS producto, "+
					" (SELECT Value FROM XX_VMR_VendorProdRef WHERE XX_VMR_VendorProdRef_ID=b.XX_VMR_VendorProdRef_ID) AS referencia, "+
					" c.XX_PRICECONSECUTIVE AS Consecutivo, XX_DiscountAmount, "+
					" (SELECT Value FROM XX_VMR_Brand WHERE XX_VMR_Brand_ID=a.XX_VMR_Brand_ID) AS marca, XX_GroupDiscount, XX_GiftAccumulate " +
					" FROM  XX_VMR_DetailPromotionExt a inner join m_product b on "+
						" (a.m_product_id=b.m_product_id and a.XX_VMR_PRICECONSECUTIVE_ID is "+
						" null and a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null)  "+
						" inner join XX_VMR_PRICECONSECUTIVE c on "+
						" a.XX_VMR_PRICECONSECUTIVE_ID=c.XX_VMR_PRICECONSECUTIVE_ID or "+
						" (a.XX_VMR_PRICECONSECUTIVE_ID is null and  b.m_product_id=c.m_product_id)" +
					" WHERE XX_Synchronized='N' and "+
						" XX_VMR_Promotion_ID="+idPromotion+" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )"+
					" union "+
					" select XX_VMR_Promotion_ID, XX_DiscountRate ,XX_MinimumPurchase, XX_QuantityPurchase, "+ 
						" XX_AmountGifted, XX_GiftAccumulate, XX_ProductSelection, a.IsActive, "+
						" XX_GIFT , "+
						" (SELECT Value FROM XX_VMR_CATEGORY WHERE XX_VMR_CATEGORY_ID=a.XX_VMR_CATEGORY_ID) AS Categoria, "+
						" (SELECT Value FROM XX_VMR_DEPARTMENT WHERE XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID) AS Departamento , "+
						" (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=b.XX_VMR_Line_ID) AS Linea, "+
						" (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=b.XX_VMR_Section_ID) AS Section, null as producto, "+
						" (SELECT Value FROM XX_VMR_VendorProdRef WHERE XX_VMR_VendorProdRef_ID=b.XX_VMR_VendorProdRef_ID) AS referencia, "+
						"null AS Consecutivo, XX_DiscountAmount,"+
						" (SELECT Value FROM XX_VMR_Brand WHERE XX_VMR_Brand_ID=a.XX_VMR_Brand_ID) AS marca, XX_GroupDiscount, XX_GiftAccumulate" +
					" FROM  XX_VMR_DetailPromotionExt a inner join  XX_VMR_VendorProdRef b on "+
						" (a.XX_VMR_DEPARTMENT_ID=b.XX_VMR_DEPARTMENT_ID and "+
						" a.XX_VMR_Line_ID=b.XX_VMR_Line_ID and "+
						" a.xx_vmr_section_id=b.xx_vmr_section_id and a.m_product_id is null and "+
						" a.XX_VMR_PRICECONSECUTIVE_ID is null and "+
						" a.XX_VMR_BRAND_ID is null and a.XX_VMR_VENDORPRODREF_ID is null) or "+
						" (a.XX_VMR_BRAND_ID=b.XX_VMR_BRAND_ID and a.m_product_id is null and "+
						" a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_VENDORPRODREF_ID is "+
						" null and a.XX_VMR_Category_ID is null and a.XX_VMR_Department_ID is null "+
						" and a.XX_VMR_Section_ID is null and a.XX_VMR_Line_ID is null) or "+
						" (a.XX_VMR_BRAND_ID=b.XX_VMR_BRAND_ID and a.m_product_id is null and "+
						" a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_VENDORPRODREF_ID is "+
						" null and a.XX_VMR_Category_ID is null and a.XX_VMR_Department_ID is null "+
						" and a.XX_VMR_Section_ID=b.XX_VMR_SECTION_ID and a.XX_VMR_Line_ID is null) or "+
						" (a.XX_VMR_BRAND_ID=b.XX_VMR_BRAND_ID and a.m_product_id is null and "+
						" a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_VENDORPRODREF_ID is "+
						" null and a.XX_VMR_Department_ID =b.XX_VMR_Department_ID "+
						" and a.XX_VMR_Section_ID is null and a.XX_VMR_Line_ID is null) or "+
						" (a.XX_VMR_BRAND_ID=b.XX_VMR_BRAND_ID and a.m_product_id is null and "+
						" a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_VENDORPRODREF_ID is "+
						" null and a.XX_VMR_Department_ID =b.XX_VMR_Department_ID "+
						" and a.XX_VMR_Section_ID is null and a.XX_VMR_Line_ID=b.XX_VMR_Line_ID) or "+
						" (a.m_product_id is null and a.XX_VMR_PRICECONSECUTIVE_ID is null and "+
						" a.XX_VMR_BRAND_ID is null and "+
						" a.XX_VMR_VENDORPRODREF_ID=b.XX_VMR_VENDORPRODREF_ID) OR "+
						" (a.XX_VMR_BRAND_ID=b.XX_VMR_BRAND_ID and a.m_product_id is null and "+
						" a.XX_VMR_PRICECONSECUTIVE_ID is null and a.XX_VMR_VENDORPRODREF_ID is "+
						" null and a.XX_VMR_Department_ID is null "+
						" and a.XX_VMR_Section_ID is null and a.XX_VMR_Line_ID is null and b.XX_VMR_Department_ID IN  "+
						" (SELECT c.XX_VMR_DEPARTMENT_ID FROM XX_VSI_CATEGDEPARTTLINEVIEW c WHERE a.XX_VMR_Category_ID=c.XX_VMR_Category_ID)) "+
					" WHERE XX_Synchronized='N' and b.isactive = 'Y' and XX_VMR_Promotion_ID="+idPromotion+" AND " +
							"XX_VMR_PromoConditionValue_ID IN ( " +conditions+" ) ORDER BY XX_VMR_Promotion_ID ";
			}else if(typePromotion.equals(nextProductDiscountsProduct))
				sql= "SELECT XX_VMR_Promotion_ID , XX_DiscountRate, a.isactive" +
						", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
						", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=c.XX_VMR_Department_ID) AS Departamento" +
						", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=c.XX_VMR_Line_ID) AS Linea" +
						", (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=c.XX_VMR_Section_ID) AS Seccion" +
						", c.value as Producto" +
						", (select value from XX_VMR_BRAND where XX_VMR_BRAND_ID=c.XX_VMR_BRAND_ID) AS Marca" +
						", (SELECT Value FROM XX_VMR_VendorProdRef where XX_VMR_VendorProdRef_ID=c.XX_VMR_VendorProdRef_ID) AS ReferenciaProveedor" +
						", b.XX_PriceConsecutive, XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE,   XX_AMOUNTGIFTED, XX_GROUPDISCOUNT" +
						", XX_GIFTACCUMULATE, XX_CONDITIONS, XX_DISCOUNTAMOUNT, XX_GIFT, NULL AS MARCA, NULL AS ReferenciaProveedor " +
						",(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
					"FROM XX_VMR_DetailPromotionExt a inner join M_Product c on " +
						"(a.M_Product_ID=c.M_Product_ID) or " +
						"(a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID=c.XX_VMR_BRAND_ID) or " +
						"(a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID=c.XX_VMR_VendorProdRef_ID and a.XX_VMR_BRAND_ID is null) or " +
						"(a.XX_VMR_Section_ID=c.XX_VMR_Section_ID and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) or " +
						"(a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) or " +
						"(a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) or " +
						"(a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) " +
						"INNER JOIN XX_VMR_PriceConsecutive b on b.M_Product_ID=c.M_Product_ID " +
					"WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID= " +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if (typePromotion.equals(sponsoredProducts))
				sql="SELECT XX_VMR_Promotion_ID , XX_DiscountRate, a.isactive" +
						", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
						", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=a.XX_VMR_Department_ID) AS Departamento" +
						", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=a.XX_VMR_Line_ID) AS Linea" +
						", (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=a.XX_VMR_Section_ID) AS Seccion, c.value as Producto" +
						", b.XX_PriceConsecutive, XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE,   XX_AMOUNTGIFTED, XX_GROUPDISCOUNT" +
						", XX_GIFTACCUMULATE, XX_CONDITIONS, XX_DISCOUNTAMOUNT, XX_GIFT, NULL AS MARCA, NULL AS ReferenciaProveedor " +
						",(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
					" FROM XX_VMR_DetailPromotionExt a " +
						"inner join M_Product c on (a.M_Product_ID=c.M_Product_ID) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID=c.XX_VMR_Section_ID and a.XX_VMR_VendorProdRef_ID=c.XX_VMR_VendorProdRef_ID and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID=c.XX_VMR_Section_ID and a.XX_VMR_VendorProdRef_ID is null and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null) " +
						"INNER JOIN XX_VMR_PriceConsecutive b on b.M_Product_ID=c.M_Product_ID " +
					"WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID= " +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if (typePromotion.equals(sponsoredLine))
				sql="SELECT XX_VMR_Promotion_ID , XX_DiscountRate, a.isactive" +
						", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
						", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=a.XX_VMR_Department_ID) AS Departamento" +
						", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=c.XX_VMR_Line_ID) AS Linea" +
						", (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=a.XX_VMR_Section_ID) AS Seccion" +
						", null as Producto, null as XX_PriceConsecutive, XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE" +
						",   XX_AMOUNTGIFTED, XX_GROUPDISCOUNT, XX_GIFTACCUMULATE, XX_CONDITIONS, XX_DISCOUNTAMOUNT, XX_GIFT" +
						", NULL AS MARCA, NULL AS ReferenciaProveedor" +
						",(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
					"FROM XX_VMR_DetailPromotionExt a inner join XX_VSI_CategDeparttLineView c on  (a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID is null and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null)" +
					"WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID= " +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if(typePromotion.equals(purchaseSavings)/* || typePromotion.equals(giftWithPurchase)*/)
				sql="SELECT XX_VMR_Promotion_ID , XX_DiscountRate, a.isactive" +
						", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
						", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=c.XX_VMR_Department_ID) AS Departamento" +
						", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=c.XX_VMR_Line_ID) AS Linea" +
						", (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=c.XX_VMR_Section_ID) AS Seccion, c.value as Producto" +
						", b.XX_PriceConsecutive, XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE,   XX_AMOUNTGIFTED, XX_GROUPDISCOUNT" +
						", XX_GIFTACCUMULATE, XX_CONDITIONS, XX_DISCOUNTAMOUNT, XX_GIFT, NULL AS MARCA, NULL AS ReferenciaProveedor " +
						",(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
					" FROM XX_VMR_DetailPromotionExt a " +
						"inner join M_Product c on (a.M_Product_ID=c.M_Product_ID) " +
						"or (a.XX_VMR_Section_ID=c.XX_VMR_Section_ID and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID is null and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null) " +
						"INNER JOIN XX_VMR_PriceConsecutive b on b.M_Product_ID=c.M_Product_ID " +
					"WHERE XX_Synchronized='N' and XX_VMR_Promotion_ID= " +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if(typePromotion.equals(nextProductDiscountsCategory))
				sql="SELECT XX_VMR_Promotion_ID, XX_DiscountRate, a.isactive" +
						", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
						", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=a.XX_VMR_Department_ID) AS Departamento" +
						", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=a.XX_VMR_Line_ID) AS Linea" +
						", (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=a.XX_VMR_Section_ID) AS Seccion" +
						", (SELECT Value FROM M_Product WHERE M_Product_ID=a.M_Product_ID) AS Producto" +
						", b.XX_PriceConsecutive AS Consecutivo" +
						", (SELECT Value FROM XX_VMR_BRAND where XX_VMR_BRAND_ID=a.XX_VMR_BRAND_ID) AS Marca" +
						", (SELECT Value FROM XX_VMR_VendorProdRef where XX_VMR_VendorProdRef_ID=a.XX_VMR_VendorProdRef_ID) AS ReferenciaProveedor" +
						", XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE, XX_AMOUNTGIFTED, XX_GROUPDISCOUNT, XX_GIFTACCUMULATE" +
						", XX_CONDITIONS, XX_DISCOUNTAMOUNT, XX_GIFT,(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
					" FROM  XX_VMR_DetailPromotionExt a INNER JOIN " +
						"XX_VMR_PriceConsecutive b ON a.M_Product_ID=b.M_Product_ID WHERE XX_Synchronized='N' and a.XX_VMR_Department_ID is null " +
						"and XX_VMR_Line_ID is null and a.XX_VMR_Section_id is null and a.M_Product_ID is null and " +
						"XX_VMR_Promotion_ID= " +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if(typePromotion.equals(nextProductDiscountsDepartmen))
				sql="SELECT distinct XX_VMR_Promotion_ID, XX_DiscountRate, a.isactive" +
						", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
						", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=b.XX_VMR_Department_ID) AS Departamento" +
						", null AS Linea" +
						", null AS Seccion, null AS Producto, null Consecutivo, null AS Marca, null AS ReferenciaProveedor" +
						", XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE, XX_AMOUNTGIFTED, XX_GROUPDISCOUNT, XX_GIFTACCUMULATE, XX_CONDITIONS" +
						", XX_DISCOUNTAMOUNT, XX_GIFT " +
						",(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name  " +
					"FROM  XX_VMR_DetailPromotionExt a inner join XX_VSI_CATEGDEPARTTLINEVIEW b " +
						"on (a.XX_VMR_Category_ID = b.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null) " +
						" or (a.XX_VMR_Category_ID=b.XX_VMR_Category_ID and a.XX_VMR_Department_ID=b.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null)" +
					"WHERE XX_Synchronized='N' and a.XX_VMR_Section_id is null and a.M_Product_ID is null and " +
						"XX_VMR_Promotion_ID= " +idPromotion+
						" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if(typePromotion.equals(nextProductDiscountsLine))
				sql="SELECT XX_VMR_Promotion_ID, XX_DiscountRate, a.isactive" +
						", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
						", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=b.XX_VMR_Department_ID) AS Departamento" +
						", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=b.XX_VMR_Line_ID) AS Linea" +
						", null AS Seccion, null AS Producto, null Consecutivo, null AS Marca, null AS ReferenciaProveedor" +
						", XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE, XX_AMOUNTGIFTED, XX_GROUPDISCOUNT, XX_GIFTACCUMULATE, XX_CONDITIONS" +
						", XX_DISCOUNTAMOUNT, XX_GIFT " +
						",(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name  " +
					"FROM  XX_VMR_DetailPromotionExt a inner join XX_VSI_CATEGDEPARTTLINEVIEW b " +
						"on (a.XX_VMR_Category_ID = b.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null) " +
						" or (a.XX_VMR_Category_ID=b.XX_VMR_Category_ID and a.XX_VMR_Department_ID=b.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null)" +
						" or (a.XX_VMR_Category_ID=b.XX_VMR_Category_ID and a.XX_VMR_Department_ID=b.XX_VMR_Department_ID and a.XX_VMR_Line_ID=b.XX_VMR_Line_ID) " +
					"WHERE XX_Synchronized='N' and a.XX_VMR_Section_id is null and a.M_Product_ID is null and " +
						"XX_VMR_Promotion_ID= " +idPromotion+" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if(typePromotion.equals(nextProductDiscountsSection))
				sql="SELECT XX_VMR_Promotion_ID, XX_DiscountRate, a.isactive" +
					", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
					", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=a.XX_VMR_Department_ID) AS Departamento" +
					", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=a.XX_VMR_Line_ID) AS Linea" +
					", (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=a.XX_VMR_Section_ID) AS Seccion" +
					", (SELECT Value FROM M_Product WHERE M_Product_ID=a.M_Product_ID) AS Producto" +
					", b.XX_PriceConsecutive" +
					", (SELECT Value FROM XX_VMR_BRAND where XX_VMR_BRAND_ID=a.XX_VMR_BRAND_ID) AS Marca " +
					", (SELECT Value FROM XX_VMR_VendorProdRef where XX_VMR_VendorProdRef_ID=a.XX_VMR_VendorProdRef_ID) AS ReferenciaProveedor" +
					", XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE, XX_AMOUNTGIFTED, XX_GROUPDISCOUNT, XX_GIFTACCUMULATE" +
					", XX_CONDITIONS, XX_DISCOUNTAMOUNT, XX_GIFT ,(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
				" FROM  XX_VMR_DetailPromotionExt a INNER JOIN " +
					"XX_VMR_PriceConsecutive b ON a.M_Product_ID=b.M_Product_ID " +
				"WHERE XX_Synchronized='N' and" +
					" a.M_Product_ID is null and XX_VMR_Promotion_ID= " +idPromotion+
					" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			else if(typePromotion.equals(nextProductDiscountsReference)) // Los Primeros 4 casos es si seleccionan (Dep, Lin, Sec, Ref) con Marca, y los 2 ultimos es para que baje por referencia cuando el usuario seleccione hasta seccion y referencia sin marca,
				sql = " SELECT XX_VMR_Promotion_ID, XX_DiscountRate, a.isactive " +
				" , (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria " +
				" , (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=a.XX_VMR_Department_ID) AS Departamento " +
				" , (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=b.XX_VMR_Line_ID) AS Linea " +
				" , (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=b.XX_VMR_Section_ID) AS Seccion " +
				" , null AS Producto, null Consecutivo " +
				" , (SELECT Value FROM XX_VMR_BRAND where XX_VMR_BRAND_ID=b.XX_VMR_BRAND_ID) AS Marca " +
				" , (SELECT Value FROM XX_VMR_VendorProdRef where XX_VMR_VendorProdRef_ID=b.XX_VMR_VendorProdRef_ID) AS ReferenciaProveedor " +
				" , XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE, XX_AMOUNTGIFTED, XX_GROUPDISCOUNT, XX_GIFTACCUMULATE " +
				" , XX_CONDITIONS, XX_DISCOUNTAMOUNT, XX_GIFT ,(select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name  " +
				" FROM  XX_VMR_DetailPromotionExt a, XX_VMR_VendorProdRef b " +
				" WHERE " +
				" (         (a.XX_VMR_Department_ID = b.XX_VMR_Department_ID  " +
				"       and a.XX_VMR_Line_ID is null " +
				"       and a.XX_VMR_Section_ID is null " +
				"       and a.XX_VMR_Brand_ID = b.XX_VMR_Brand_ID) " +
				" or " +
				"           (a.XX_VMR_Department_ID = b.XX_VMR_Department_ID  " +
				"       and a.XX_VMR_Line_ID = b.XX_VMR_Line_ID " +
				"       and a.XX_VMR_Section_ID is null " +
				"       and a.XX_VMR_Brand_ID = b.XX_VMR_Brand_ID) " +
				" or   " +
				"           (a.XX_VMR_Department_ID = b.XX_VMR_Department_ID  " +
				"       and a.XX_VMR_Line_ID = b.XX_VMR_Line_ID " +
				"       and a.XX_VMR_Section_ID = b.XX_VMR_Section_ID " +
				"       and a.XX_VMR_Brand_ID = b.XX_VMR_Brand_ID " +
				"       and a.XX_VMR_VendorProdRef_ID is null ) " +
			    " or   " +
				"           (a.XX_VMR_Department_ID = b.XX_VMR_Department_ID  " +
				"       and a.XX_VMR_Line_ID = b.XX_VMR_Line_ID " +
				"       and a.XX_VMR_Section_ID = b.XX_VMR_Section_ID " +
				"       and a.XX_VMR_Brand_ID = b.XX_VMR_Brand_ID " +
				"       and a.XX_VMR_VendorProdRef_ID = b.XX_VMR_VendorProdRef_ID" +
				"	    and a.M_Product_ID is null ) " +
				" or   " +
		        "   		(a.XX_VMR_Department_ID = b.XX_VMR_Department_ID  " +
		        "		and a.XX_VMR_Line_ID = b.XX_VMR_Line_ID " +
		        "		and a.XX_VMR_Section_ID = b.XX_VMR_Section_ID " +
		        "		and a.XX_VMR_Brand_ID  is null " +
		        "		and a.XX_VMR_VendorProdRef_ID is null ) " +
	            " or   " +
		        "     		(a.XX_VMR_Department_ID = b.XX_VMR_Department_ID " +
		        "		and a.XX_VMR_Line_ID = b.XX_VMR_Line_ID " +
		        "		and a.XX_VMR_Section_ID = b.XX_VMR_Section_ID " +
		        " 		and a.XX_VMR_Brand_ID is null " +
		        " 		and a.XX_VMR_VendorProdRef_ID = b.XX_VMR_VendorProdRef_ID  ) " +
				" ) " +
				" and a.XX_Synchronized='N'  " +
				" and b.isactive = 'Y' " +
				" and XX_VMR_Promotion_ID=" +idPromotion+
				" AND XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				   rs = pstmt.executeQuery();
				DecimalFormat dfc = new DecimalFormat("000");
				DecimalFormat dfp = new DecimalFormat("000000000");
				int detail=1;
				BufferedWriter bw1;
				
				while (rs.next()){
					
					try {
						bw1 = new BufferedWriter(new FileWriter(s.getRoot()+detailPromotionExtPath,(concatenar)));
						bw1.write(rs.getInt("XX_VMR_Promotion_ID")+"["+detail+++"["+rs.getBigDecimal("XX_DiscountRate")
								+"["+rs.getString("Categoria")+"["+(rs.getString("Departamento")==null?"NULL":rs.getString("Departamento"))
								+"["+((rs.getString("Marca")==null)?"NULL":rs.getString("Marca"))+"["+(rs.getString("Linea")==null?"NULL":rs.getString("Linea"))
								+"["+((rs.getString("ReferenciaProveedor")==null)?"NULL":rs.getString("ReferenciaProveedor"))
								+"["+rs.getDouble("XX_MINIMUMPURCHASE")+"["+rs.getInt("XX_QUANTITYPURCHASE")+"["+rs.getInt("XX_AmountGifted")
								+"["+((rs.getString("Producto")==null)?"NULL":dfp.format(rs.getInt("Producto"))+dfc.format(rs.getInt("XX_PriceConsecutive")))
								+"["+rs.getDouble("XX_DISCOUNTAMOUNT")+"["+(delete?"E":(rs.getString("isactive").equals("Y")?"A":"E"))
								+"["+ cleanName(rs.getString("name"))+"["+rs.getString("XX_GROUPDISCOUNT")
								+"["+((rs.getString("Seccion")==null)?"NULL":rs.getString("Seccion"))+"["+rs.getString("XX_GifTAccumulate")+"[\r\n");
						bw1.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					concatenar=true;
					
					/*detailPromotionsExtBody+=rs.getInt("XX_VMR_Promotion_ID")+"["+detail+++"["+rs.getInt("XX_DiscountRate")
					+"["+rs.getString("Categoria")+"["+(rs.getString("Departamento")==null?"NULL":rs.getString("Departamento"))
					+"["+((rs.getString("Marca")==null)?"NULL":rs.getString("Marca"))+"["+(rs.getString("Linea")==null?"NULL":rs.getInt("Linea"))
					+"["+((rs.getString("ReferenciaProveedor")==null)?"NULL":rs.getString("ReferenciaProveedor"))
					+"["+rs.getDouble("XX_MINIMUMPURCHASE")+"["+rs.getInt("XX_QUANTITYPURCHASE")+"["+rs.getInt("XX_AmountGifted")
					+"["+((rs.getString("Producto")==null)?"NULL":dfp.format(rs.getInt("Producto"))+dfc.format(rs.getInt("XX_PriceConsecutive")))
					+"["+rs.getDouble("XX_DISCOUNTAMOUNT")+"["+(delete?"E":(rs.getString("isactive").equals("Y")?"A":"E"))
					+"["+ cleanName(rs.getString("name"))+"["+rs.getString("XX_GROUPDISCOUNT")
					+"["+((rs.getString("Seccion")==null)?"NULL":rs.getString("Seccion"))+"["+rs.getString("XX_GifTAccumulate")+"[\r\n";*/
				}
			} catch (Exception e) {e.printStackTrace();}
			finally{
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
			}
		}
		}
		return detailPromotionsExtBody;
	}
	private void sendPromotionalFile(String root, String put, String ip
		, String user, String pass, String path, Boolean consecutive) {//Envia los archivos a las tiendas
		int total = 0;
		OutputStream out = null;
		InputStream in = null;
		FtpClient ftpClient = new FtpClient();
		try {
			ftpClient.setConnectTimeout(500000);
			ftpClient.openServer(ip); 
			ftpClient.login(user, pass); 
			ftpClient.binary();
			if (consecutive) 
				out = ftpClient.append(path);
			else 
				out = ftpClient.put(path);
			in = new FileInputStream( root );
			byte c[] = new byte[4096];
			int read = 0;
			while (( read = in.read(c) ) != -1 ){
				out.write(c, 0, read);
				total+=read;
			} 
		} catch (Exception e) {
			System.out.println("No Hay archivo en \""+root+"\" para promociones vigentes");
		}finally{
			try{
				in.close(); 
				out.close();
				ftpClient.closeServer();
			}catch(Exception e){}
		}
	}
	private void synchronizedUpdate(){//Marca las promociones que ya se bajaron a los servidores de tienda
		
		PreparedStatement pstmt = null;
		
		Iterator<XX_Promotion> i = promotions.iterator();
		while (i.hasNext()){
			int x = i.next().getId();
			String sql = "update XX_VMR_Promotion set XX_Synchronized='Y', XX_InWareHouse = 'Y' where XX_VMR_Promotion_id="+x;
			try{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.executeUpdate(sql);
			} catch (SQLException e) {e.printStackTrace();}
			finally{
				DB.closeStatement(pstmt);
			}
			sql="update XX_VMR_DetailPromotionExt set XX_SYNCHRONIZED='Y' where XX_VMR_DetailPromotionExt_ID="+x;
			try{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.executeUpdate(sql);
			} catch (SQLException e) {e.printStackTrace();}
			finally{
				DB.closeStatement(pstmt);
			}
		}		
	}
	private Vector<XX_Promotion> getSynchronizedPromotions() {
		String sql ="SELECT  a.XX_VMR_Promotion_ID, XX_TypePromotion FROM XX_VMR_Promotion a inner join XX_VMR_DetailPromotionExt b " +
					"on  a.XX_VMR_Promotion_ID=b.XX_VMR_Promotion_ID and b.XX_Synchronized='N' and b.isactive='Y'  " +
					"WHERE (DateFrom>=current_date OR DateFinish>=current_date) AND (XX_ApproveMar like 'Y' OR XX_ApproveMer like 'Y') " +
					"AND a.XX_Synchronized='Y' AND a.isactive='Y' group by a.xx_vmr_promotion_id, XX_TypePromotion ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next()){
				int id=rs.getInt("XX_VMR_Promotion_ID");
				int typeAux=rs.getInt("XX_TypePromotion");
				String type= "";
					switch (typeAux){
					case 1000100: type=purchaseSavings; break;
					case 1000200: type=freeProduct; break;
					case 1000300: type=defineLevel(typeAux, id); break;
					case 1000400: type=defineLevel(typeAux, id); break;
					case 1000500: type=corporate; break;
					case 1000600: type=decals; break;
					case 1000700: type=winningTransaction; break;
					case 1000800: type=gifCarByPurchase; break;
					case 1000900: type=discountCoupon; break;
					case 1001000: type=giftWithPurchase; break;
					case 1001100: type=clasic; break;
					case 1001200: type=raffle; break;
					}
					synchronizedPromotions.add(new XX_Promotion(id, type, null, null, null, null, 0, null, null));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return synchronizedPromotions;
	}
	
	// WDIAZ Se comento ya que se utilizaba en el metodo de newconsecutive que fue comentado
	/*private int numberOfDetails(int idPromotion) {
		String sql="SELECT count(*) as x FROM XX_VMR_DetailPromotionExt a inner join M_Product c " +
				" on (a.M_Product_ID=c.M_Product_ID and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) " +
				" or (a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID=c.XX_VMR_BRAND_ID) " +
				" or (a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID=c.XX_VMR_VendorProdRef_ID and a.XX_VMR_BRAND_ID is null) " +
				" or (a.XX_VMR_Section_ID=c.XX_VMR_Section_ID and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null)  " +
				" or (a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null)  " +
				" or (a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null)  " +
				" or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) " +
				" INNER JOIN XX_VMR_PriceConsecutive b on b.M_Product_ID=c.M_Product_ID where b.created<current_date-1 and xx_vmr_promotion_id="+idPromotion;
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet r = pstmt.executeQuery();
			if (r.next()){
				int x=r.getInt("x");
				r.close();
				pstmt.close();
				return x;
			}
			r.close();
			pstmt.close();
			return 0;
		}catch (SQLException e) {
			e.printStackTrace();
			return 0;}
	}*/
	private Vector<XX_Promotion> getDeletedPromotion() {
		String sql ="SELECT  a.XX_VMR_Promotion_ID, XX_TypePromotion FROM XX_VMR_Promotion a inner join XX_VMR_DetailPromotionExt b " +
		"on  a.XX_VMR_Promotion_ID=b.XX_VMR_Promotion_ID  " +
		"WHERE (DateFrom>=current_date OR DateFinish>=current_date) AND  XX_InWareHouse = 'Y' " +
		"AND a.isactive='N' group by a.xx_vmr_promotion_id, XX_TypePromotion ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next()){
				int id=rs.getInt("XX_VMR_Promotion_ID");
				int typeAux=rs.getInt("XX_TypePromotion");
				String type= "";
					switch (typeAux){
					case 1000100: type=purchaseSavings; break;
					case 1000200: type=freeProduct; break;
					case 1000300: type=defineLevel(typeAux, id); break;
					case 1000400: type=defineLevel(typeAux, id); break;
					case 1000500: type=corporate; break;
					case 1000600: type=decals; break;
					case 1000700: type=winningTransaction; break;
					case 1000800: type=gifCarByPurchase; break;
					case 1000900: type=discountCoupon; break;
					case 1001000: type=giftWithPurchase; break;
					case 1001100: type=clasic; break;
					case 1001200: type=raffle; break;
					}
					deletedPromotions.add(new XX_Promotion(id, type, null, null, null, null, 0, null, null));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return deletedPromotions;
	}
	//Mantenimiento de promociones
	private void createNewDetail (XX_Store s){
		Iterator<XX_Promotion> i = synchronizedPromotions.iterator();
		while (i.hasNext()){
			XX_Promotion p = i.next();
			if(p.getType().equals(clasic))getDetailPromotion(p.getId(),s, false);
			else getDetailPromotionExt(p.getId(),p.getType(),false, s);
		}
	}
	
	// WDIAZ Se comento porque el código no esta realizando nada en la operación, 07-2013
	/*private void createNewConsecutive(String idSt, String root){
		//TODO: OJO
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Iterator<XX_Promotion> i = synchronizedPromotions.iterator();
		String conditions = null;
		while (i.hasNext()){
			XX_Promotion p = i.next();
			conditions = getConditions(p.getId(),idSt);
			if(conditions!=null){
			if (p.getType().equals(giftWithPurchase)||p.getType().equals(purchaseSavings)||p.getType().equals(sponsoredProducts)
					||p.getType().equals(nextProductDiscountsProduct)||p.getType().equals(clasic)){
				int detail=numberOfDetails(p.getId());
				String sql ="select (select value from m_product where m_product_id=a.M_product_id) as product, XX_PRICECONSECUTIVE " +
						"from XX_VMR_PRICECONSECUTIVE a where created>current_date-1  and m_product_id in " +
						"(SELECT c.m_product_id FROM XX_VMR_DetailPromotionExt a inner join xx_vmr_promotion b " +
						"on a.xx_vmr_promotion_id=b.xx_vmr_promotion_id and xx_typepromotion<>1001100 " +
						"or a.m_product_id is not null or a.XX_VMR_BRAND_ID is not null or a.XX_VMR_VendorProdRef_ID is not null" +
						" inner join M_Product c on a.xx_vmr_promotion_id=" +p.getId()+
						" and ((a.M_Product_ID=c.M_Product_ID) " +
						"or (a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID=c.XX_VMR_BRAND_ID) " +
						"or (a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID=c.XX_VMR_VendorProdRef_ID and a.XX_VMR_BRAND_ID is null) " +
						"or (a.XX_VMR_Section_ID=c.XX_VMR_Section_ID and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) " +
						"or (a.XX_VMR_Line_ID=c.XX_VMR_Line_ID and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) " +
						"or (a.XX_VMR_Department_ID=c.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null) " +
						"or (a.XX_VMR_Category_ID=c.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null and a.XX_VMR_Section_ID is null and a.M_Product_ID is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null)))";
				try{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					 rs = pstmt.executeQuery();
					while(rs.next()){
						int product = rs.getInt("product");
						int consecutive = rs.getInt("XX_PRICECONSECUTIVE");
						DecimalFormat dfc = new DecimalFormat("000");
						DecimalFormat dfp = new DecimalFormat("000000000");
						String sql2="";
						sql2 = "select XX_VMR_Promotion_ID , XX_DiscountRate" +
								", (SELECT Value FROM XX_VMR_Category WHERE XX_VMR_Category_ID=a.XX_VMR_Category_ID) AS Categoria" +
								", (SELECT Value FROM XX_VMR_Department WHERE XX_VMR_Department_ID=a.XX_VMR_Department_ID) AS Departamento" +
								", (SELECT Value FROM XX_VMR_Line WHERE XX_VMR_Line_ID=a.XX_VMR_Line_ID) AS Linea" +
								", (SELECT Value FROM XX_VMR_Section WHERE XX_VMR_Section_ID=a.XX_VMR_Section_ID) AS Seccion" +
								", (select value from XX_VMR_BRAND where XX_VMR_BRAND_ID=a.XX_VMR_BRAND_ID) AS Marca" +
								", (SELECT Value FROM XX_VMR_VendorProdRef where XX_VMR_VendorProdRef_ID=a.XX_VMR_VendorProdRef_ID) AS ReferenciaProveedor" +
								", XX_MINIMUMPURCHASE, XX_QUANTITYPURCHASE,   XX_AMOUNTGIFTED, XX_GROUPDISCOUNT, XX_GIFTACCUMULATE, XX_CONDITIONS, XX_DISCOUNTAMOUNT" +
								", XX_GIFT, NULL AS MARCA, NULL AS ReferenciaProveedor " +
								", (select name from XX_VMR_Promotion where XX_VMR_Promotion_ID=a.XX_VMR_Promotion_ID) as name " +
								"from XX_VMR_DetailPromotionExt a inner join m_product b " +
								"on (a.m_product_id=b.m_product_id ) " +
								"or (a.XX_VMR_Section_ID=b.XX_VMR_Section_ID and  a.m_product_id is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null ) " +
								"or (a.XX_VMR_Line_ID=b.XX_VMR_Line_ID and a.XX_VMR_Section_ID is null and a.m_product_id is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null ) " +
								"or (a.XX_VMR_Department_ID=b.XX_VMR_Department_ID and a.XX_VMR_Line_ID is null  and a.XX_VMR_Section_ID is null and a.m_product_id is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null ) " +
								"or (a.XX_VMR_Category_ID=b.XX_VMR_Category_ID and a.XX_VMR_Department_ID is null and a.XX_VMR_Line_ID is null  and a.XX_VMR_Section_ID is null and a.m_product_id is null and a.XX_VMR_VendorProdRef_ID is null and a.XX_VMR_BRAND_ID is null ) " +
								"where xx_vmr_promotion_id=1000000 and b.value='"+product+"' and XX_VMR_PromoConditionValue_ID IN ( " +conditions+" )";
						try{
							 pstmt2 = DB.prepareStatement(sql2, get_TrxName());
							    rs2 = pstmt2.executeQuery();
							if (rs2.next()){
								if(p.getType().equals(clasic)){						
									detailPromotionsBody += rs2.getInt("XX_VMR_Promotion_ID")+"["+detail+++"[["+rs2.getString("departamento")
									+"["+(rs2.getShort("Linea")==0?"\\N":rs2.getShort("Linea"))+"["
									+dfp.format(rs2.getInt("producto")+dfc.format(rs2.getInt("consecutivo")))
									+"["+cleanName(rs2.getString("name"))+"["+ rs2.getInt("XX_DiscountRate")+"[0.00[A\r\n";
									BufferedWriter bw;
									try {
										bw = new BufferedWriter(new FileWriter(root+detailPromotionPath));
										bw.write(detailPromotionsBody);
										bw.close();
									} catch (IOException e) {e.printStackTrace();}
								}else{
									detailPromotionsExtBody+=rs2.getInt("XX_VMR_Promotion_ID")+"["+detail+++"["+rs2.getInt("XX_DiscountRate")
									+"["+rs2.getString("Categoria")+"["+rs2.getString("Departamento")
									+"["+rs2.getString("Marca")+"["+rs2.getInt("Linea")+"["+rs2.getString("ReferenciaProveedor")
									+"["+rs2.getDouble("XX_MINIMUMPURCHASE")+"["+rs2.getInt("XX_QUANTITYPURCHASE")+"["+rs2.getInt("XX_DISCOUNTAMOUNT")
									+"["+dfp.format(product)
									+dfc.format(consecutive)
									+"["+rs2.getDouble("XX_DISCOUNTAMOUNT")
									+"[A["+ cleanName(rs2.getString("name"))
									+"["+rs2.getString("XX_GROUPDISCOUNT")
									+"["+rs2.getString("Seccion")+"[NULL[\r\n";
								}
							}
						} catch (SQLException e) {e.printStackTrace();System.out.println("No se encontro detalle para el producto="+product+" de la promoción="+p.getId());}
						finally{
							DB.closeStatement(pstmt2);
							DB.closeResultSet(rs2);
						}
					}
				} catch (SQLException e) {e.printStackTrace();}
				finally{
					DB.closeStatement(pstmt);
					DB.closeResultSet(rs);
				}
			}
		}
		}
	}*/
	private void createDeleteDetailPromotion(XX_Store s){
		if (detailPromotionsExtFile == null)
			detailPromotionsExtFile = new File(s.getRoot(), detailPromotionPath);
		Iterator<XX_Promotion> i = deletedPromotions.iterator();
		while (i.hasNext()){
			XX_Promotion p = i.next();
			if (!p.getType().equals("P")) 
				getDetailPromotionExt(p.getId(), p.getType(),true, s);
			else
				getDetailPromotion(p.getId(),s, true);
		}
	}
	
	public boolean getConcatenar(){
		return concatenar;
	}
}
