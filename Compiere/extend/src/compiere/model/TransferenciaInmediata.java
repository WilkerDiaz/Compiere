package compiere.model;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import sun.net.ftp.FtpClient;

import compiere.model.promociones.process.XX_SynchronizeStore;
import compiere.model.promociones.process.model.XX_Promotion;
import compiere.model.promociones.process.model.XX_Store;

public class TransferenciaInmediata extends SvrProcess {
	private XX_Promotion promotion= null;
	//private XX_Store store=null;
	public static Vector<XX_Store> stores = new Vector<XX_Store>();
	
	public static String promotionPath="";
	public static String detailPromotionExtPath="";
	public static String detailPromotionPath="";
	public static String conditionPath="";
	public static String winningControlTransactionPath="";
	public static String donationPath="";
	
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
	
	public static boolean concatenar = false;
	
	private String msg = "";
	private XX_SynchronizeStore sync;
	
	
	@Override
	protected String doIt() throws Exception {
		sync = new XX_SynchronizeStore();
		for(XX_Store store:stores){
			if(!msg.equals("")) 
				throw new Exception(msg);
			System.out.println("REALIZANDO TRANSFERENCIA INMEDIATA PARA TIENDA "+store.getId());
			createPromotionalFile(store.getRoot());
			createDetailPromotionFile(store);
			createDetailPromotionExtFile(store);
			createConditionsPromotionsFile(store.getId(), store.getRoot());
			createWinningControlTransactionFile(store.getId(), store.getRoot());	
			createDonation(store.getId(), store.getRoot());
			/*createDeleteDetailPromotion(store);
			createNewConsecutive(store.getId(), store.getRoot());
			createNewDetail(store);*/
			
			
			
			System.out.println("ENVIANDO ARCHIVO DE PROMOCIONES PARA TIENDA "+store.getId());
	
			sendPromotionalFile(store.getRoot()+promotionPath,store.getPut(),store.getIp(),store.getUser(),store.getPass(),promotionPath, false);
			sendPromotionalFile(store.getRoot()+conditionPath,store.getPut(),store.getIp(),store.getUser(),store.getPass(),conditionPath, false);
			sendPromotionalFile(store.getRoot()+winningControlTransactionPath,store.getPut(),store.getIp(),store.getUser(),store.getPass(),winningControlTransactionPath, false);
			sendPromotionalFile(store.getRoot()+detailPromotionPath,store.getPut(),store.getIp(),store.getUser(),store.getPass(),detailPromotionPath, false);
			sendPromotionalFile(store.getRoot()+detailPromotionExtPath,store.getPut(),store.getIp(),store.getUser(),store.getPass(),detailPromotionExtPath, false);
			sendPromotionalFile(store.getRoot()+donationPath,store.getPut(),store.getIp(),store.getUser(),store.getPass(),donationPath, false);
		}
		if(!msg.equals("")) throw new Exception(msg);
			//return msg;
		concatenar=false;
		System.out.println("FIN DE SINCRONIZACION DE PROMOCIONES CON TRANSFERENCIA INMEDIATA");
		return "Proceso Realizado con exito";
	}

	@Override
	protected void prepare() {
		stores = new Vector<XX_Store>();
		ProcessInfoParameter[] para = getParameter();
		
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Tienda")){
				stores = getStores(element.getParameter().toString());
			}
		}
		//Comprobar si el proceso se corre desde la ventana de tabla XX_VMR_Promotion
		promotion= getPromotion();
		
		//promotionID=x_promociones.getXX_VMR_Promotion_ID();
		
		
		System.out.println("INICIANDO TRANSFERENCIA INMEDIATA");

		promotionPath="transfInmedPromoCR_Ext";
		detailPromotionExtPath="transfInmedPromTdaExt";
		detailPromotionPath="transfInmedPromTda_Ext";
		conditionPath="transfInmedCondPromocion";
		winningControlTransactionPath="transfInmedTranPremControl";
		donationPath="transfInmedDonacion";
		
		
		
	
		
		// Correr sincronizador de la promocion
		
		
		/*try {
			System.out.println(codPromocion+" "+detalle+" "+tienda);
			String[] sincroniza= new String[]{"/opt/SincCompiereServTienda/iniciarSincronizadorp.sh", codPromocion+" "+detalle+" "+tienda};
			System.out.println(sincroniza.toString());
			Runtime.getRuntime().exec(sincroniza);
			//Runtime.getRuntime().exec("/opt/SincCompiereServTienda/./iniciarSincronizadorp.sh "+codPromocion+" "+detalle+" "+tienda);
			//Runtime.getRuntime().exec("/opt/SincCompiereServTienda/./iniciarSincronizadorp.sh 1000340 0 17");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
	}

	//Creación de archivos para los servidores de tienda	
	private void createDonation(String idSt, String root) {
		String donationBody="";
		if (donationFile == null){
			donationFile = new File(root, donationPath);
		}
		String sql="SELECT  a.XX_VME_Donations_ID, DateFrom, DateFinish, a.name, ELEMENTTYPE, AMOUNT, total, a.IsActive " +
				"FROM XX_VME_Donations a inner join XX_VME_DONATIONSTORE b on a.XX_VME_Donations_ID=b.XX_VME_Donations_ID and b.isactive like 'Y' " +
				"inner join m_warehouse c on b.M_WAREHOUSE_ID=c.M_WAREHOUSE_ID " +
				"WHERE (DateFrom>=current_date OR DateFinish>=current_date) " +
				"AND XX_ApproveMar like 'Y' AND a.IsActive like 'Y' and c.value='"+idSt+"'";
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
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
	}
	private void createConditionsPromotionsFile(String idSt, String root) {
		String conditionBody="";
		if (conditionFile == null){
			conditionFile = new File(root, conditionPath);
		}

		if (promotion.getType().equals(raffle)){
			String sql="select XX_conditions from XX_VMR_DetailPromotionExt e " +
					"INNER JOIN xx_vmr_promocondwarehouse p ON e.XX_VMR_PROMOCONDITIONVALUE_ID=p.XX_VMR_PROMOCONDITIONVALUE_ID " +
					"where XX_VMR_Promotion_ID="+promotion.getId()+" AND (p.XX_WarehouseBecoNumber='000' OR p.XX_WarehouseBecoNumber='"+idSt+"')";
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()){
					Iterator<String> lin = dividedIntoLines(sizeLine,  cleanName(rs.getString("XX_conditions"))).iterator();
					int order=0;
					while(lin.hasNext())
					conditionBody += (promotion.getId()+"["+order+++"["+lin.next()+"[N[\r\n").replace('¬','*');
				}
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new FileWriter(root+conditionPath));
					bw.write(conditionBody);
					bw.close();
				} catch (IOException e) {e.printStackTrace();}
			} catch (Exception e) {e.printStackTrace();}
		}
		
	}
	private void createDetailPromotionFile(XX_Store s) {
		if (detailPromotionsFile == null)
			detailPromotionsFile = new File(s.getRoot(), detailPromotionPath);		
		if (promotion.getType().equals(clasic))getDetailPromotion(promotion.getId(),s);
	}	
	private void createDetailPromotionExtFile(XX_Store s) {
		if (detailPromotionsExtFile == null)
			detailPromotionsExtFile = new File(s.getId(), detailPromotionPath);
		if (!promotion.getType().equals("P")) getDetailPromotionExt(promotion.getId(), promotion.getType(),false, s)	;
	}		
	private void createPromotionalFile(String root) throws IOException {	
		String promotionsBody="";
		
		if (promotionsFile == null){
			promotionsFile = new File(root, promotionPath);
		}

		promotionsBody += 	promotion.getId()+"["+promotion.getType()+"["+promotion.getStartDate().substring(0,4)+"-"+promotion.getStartDate().substring(5,7)
							+"-"+promotion.getStartDate().substring(8, 10)+"["+(promotion.getStartTime()==null?"00:00:00":promotion.getStartTime().substring(11, 19))+"["+
							promotion.getEndDate().substring(0,4)+"-"+promotion.getEndDate().substring(5,7)+"-"+promotion.getEndDate().substring(8, 10)+"["+
							(promotion.getEndTime()==null?"23:59:59":promotion.getEndTime().substring(11, 19))+"["+promotion.getPriority()+"[\r\n";
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(root+promotionPath));
		bw.write(promotionsBody);
		bw.close();
		promotionsBody = "";
	}
	private void createWinningControlTransactionFile(String idSt, String root){
		String winningControlTransactionBody="";
		if (winningControlTransactionFile == null){
			winningControlTransactionFile = new File(root, winningControlTransactionPath);
		}
		
		if (promotion.getType().equals(winningTransaction)){
			String sql="select XX_QuantityPurchase, xx_DiscountAmount from XX_VMR_DetailPromotionExt e" +
					"INNER JOIN xx_vmr_promocondwarehouse p ON e.XX_VMR_PROMOCONDITIONVALUE_ID=p.XX_VMR_PROMOCONDITIONVALUE_ID " +
					"where XX_VMR_Promotion_ID="+promotion.getId()+" AND (p.XX_WarehouseBecoNumber='000' OR p.XX_WarehouseBecoNumber='"+idSt+"')";
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()){
					winningControlTransactionBody += rs.getString("XX_QuantityPurchase")+"["+rs.getString("XX_QuantityPurchase")
					+"["+rs.getString("xx_DiscountAmount")+"["+rs.getString("xx_DiscountAmount")+"["+promotion.getId()+"[\r\n";
				}
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new FileWriter(root+winningControlTransactionPath));
					bw.write(winningControlTransactionBody);
					bw.close();
				} catch (IOException e) {e.printStackTrace();}
			} catch (Exception e) {e.printStackTrace();}
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
		name=name.replace("É", "E");
		name=name.replace("í", "i");
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
			int level=0;//1=Categoria, 2=Departamento, 3=Linea, 4=Seccion, 5=Producto
			while (rs.next()){
				if (rs.getString("XX_VMR_Category_ID")==null)Msg.translate(get_TrxName().getTrxName(),"Invalid Detail");
				else if (rs.getString("XX_VMR_Department_ID")== null && level<1)level=1;
				else if (rs.getString("XX_VMR_Line_ID")==null && level<2 && rs.getString("XX_VMR_Department_ID")!= null)level=2;
				else if (rs.getString("XX_VMR_Section_ID")==null && level<3 && rs.getString("XX_VMR_Line_ID")!=null)level=3;
				else if (rs.getString("M_Product_ID")==null && rs.getString("XX_VMR_VendorProdRef_ID")==null && rs.getString("XX_VMR_Brand_ID")==null && level<4 && rs.getString("XX_VMR_Section_ID")!=null) level=4;
				else if ((rs.getString("M_Product_ID")!=null || rs.getInt("XX_VMR_VendorProdRef_ID")!=0 || rs.getInt("XX_VMR_Brand_ID")!=0) && level<5 && rs.getString("XX_VMR_Section_ID")!=null) {level=5; break;}
			}
			rs.close();
			pstmt.close();
			if (level==0)Msg.translate(get_TrxName().getTrxName(),"Invalid Detail");
			if (type==1000300){
				if (level<4)return sponsoredLine;
				else return sponsoredProducts;
			} else // solo queda la opcion de combo 1000400
				switch (level){
				case 1: return nextProductDiscountsCategory; 
				case 2: return nextProductDiscountsDepartmen;
				case 3: return nextProductDiscountsLine;
				case 4: return nextProductDiscountsSection;
				case 5: return nextProductDiscountsProduct;
				}	
		}catch (SQLException e){e.printStackTrace();}
		return null;
	}
	private XX_Promotion getPromotion() {
		String sql ="SELECT  XX_VMR_Promotion_ID, XX_TypePromotion, DateFrom, TimeSlotStart, DateFinish" +
				", TimeSlotEnd, Priority, XX_Synchronized, IsActive " +
				"FROM XX_VMR_Promotion WHERE (DateFrom>=current_date OR  (DateFinish + interval '1439' MINUTE)>=current_date) " +
				"AND (XX_ApproveMar like 'Y' OR XX_ApproveMer like 'Y') and  IsActive like 'Y' " +
				"AND XX_VMR_Promotion_ID="+ getRecord_ID();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			if (rs.next()){
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
				return new XX_Promotion(id, type, startDate, startTime, endDate, endTime, priority, sync, act);
			}else{
				msg = "La promoción no cumple las condiciones para ser transferida.";
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return null;
	}	
	
	private Vector<XX_Store> getStores(String storeN) {
		String  sql ="";
		if(storeN.equals("00")){
			sql=	"SELECT Value, IP_Address, Username, Password, XX_RootFTP, XX_PutFTP " +
					"FROM XX_VSI_WAREHOUSENET a INNER JOIN M_Warehouse b " +
					"ON a.M_Warehouse_ID=b.M_Warehouse_ID WHERE a.IsActive LIKE 'Y' " +
					"ORDER BY Value";
		}else{
			sql=	"SELECT Value, IP_Address, Username, Password, XX_RootFTP, XX_PutFTP " +
					"FROM XX_VSI_WAREHOUSENET a INNER JOIN M_Warehouse b " +
					"ON a.M_Warehouse_ID=b.M_Warehouse_ID WHERE a.IsActive LIKE 'Y' " +
					"AND Value = "+storeN +" ORDER BY Value";
		}
		
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
	
	private String getDetailPromotion(int idPromotion, XX_Store s){
		XX_SynchronizeStore.detailPromotionsBody = "";
		String detailPromotionsBody=sync.getDetailPromotion(idPromotion, s, false);
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(s.getRoot()+detailPromotionPath, false));
				bw.write(detailPromotionsBody);
				bw.close();
			} catch (IOException e) {e.printStackTrace();}
		
		return detailPromotionsBody;
	}
	private String getDetailPromotionExt(int idPromotion,String typePromotion,  boolean delete, XX_Store s){

		String detailPromotionsExtBody = sync.getDetailPromotionExtTI(idPromotion, typePromotion, delete, s,detailPromotionExtPath);
		concatenar = sync.getConcatenar();

		BufferedWriter bw1;
		try {
			bw1 = new BufferedWriter(new FileWriter(s.getRoot()+detailPromotionExtPath,concatenar));
			bw1.write(detailPromotionsExtBody);
			bw1.close();
			System.out.println("ESCRIBIENDO ARCHIVO DE PROMOCIONES PARA TIENDA "+s.getId());
		} catch (IOException e) {e.printStackTrace();}


		return detailPromotionsExtBody;
	}
	private void sendPromotionalFile(String root, String put, String ip
		, String user, String pass, String path, Boolean consecutive)  {//Envia los archivos a las tiendas
		
		FtpClient ftpClient = new FtpClient();
		OutputStream out = null;
		InputStream in = null;
		try {
			try{
				ftpClient.openServer(ip); 
				ftpClient.login(user, pass); 
				ftpClient.binary();
			}catch (IOException e) {
				msg = "No se pudo conectar con el servidor de tienda "+ip;
			}
			if (consecutive) out = ftpClient.append(path);
			else out = ftpClient.put(path);
			in = new FileInputStream(root);
			byte c[] = new byte[4096];
			int read = 0;
			while (( read = in.read(c) ) != -1 ){
				out.write(c, 0, read);
			}
		}  catch (Exception e) {
			System.out.println("No Hay archivo en \""+root+"\" para promociones vigentes");
		}finally{
			try{
				if (in != null)
					in.close(); 
				if (in != null)
				out.close();
				ftpClient.closeServer(); 
			}catch(Exception e){
				
			}
		}
	}
	
	

}
