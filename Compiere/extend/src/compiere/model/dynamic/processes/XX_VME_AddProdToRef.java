package compiere.model.dynamic.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MProduct;
import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VME_Elements;
import compiere.model.dynamic.X_XX_VME_Reference;

/**
 * XX_VME_AddProdToRef
 * Se recorren los diferentes folletos para verificar las referencias y los
 * productos asociados a ella. En caso de existir nuevos productos, se asocian
 * a las referencias respectivas.
 * 
 * Adicionalmente, si un elemento tiene referencias agregadas desde OC o pedido, 
 * las mismas deben ser actualizadas con los productos asociados a la insercion
 * 
 * Se envía correo cuando se agregan productos nuevos a las referencias
 * @author Maria Vintimilla
 * @version 1.0
 * 
 */
public class XX_VME_AddProdToRef extends SvrProcess{
	public String refsUpdated = "";
	public Integer user = new Integer(0);
	
	/**
	 * No se utiliza el prepare ya que el proceso no recibe parámetros.
	 */
	protected void prepare() {		
	}

	protected String doIt() throws Exception {
		String msj = " Se procesó la adición de productos a folleto";
		PreparedStatement pstmtAM = null;
		PreparedStatement pstmtBRO = null;
		PreparedStatement pstmtOC = null;
		ResultSet rsAM = null;
		ResultSet rsBRO = null;
		ResultSet rsOC = null;
		Vector<Integer> elements = new Vector<Integer>();
		Vector<Integer> references = new Vector<Integer>();
		Vector<Integer> actions = new Vector<Integer>();
		Vector<Integer> pages = new Vector<Integer>();
		Vector<Integer> brochures = new Vector<Integer>();
		Date inicio = new Date();
		
		String SQLBRO = " SELECT B.XX_VMA_BROCHURE_ID BROID " +
				" FROM XX_VMA_MARKETINGACTIVITY AM INNER JOIN XX_VMA_BROCHURE B " +
				" ON (AM.XX_VMA_BROCHURE_ID = B.XX_VMA_BROCHURE_ID) " +
				" WHERE (sysdate <= AM.StartDate  AND AM.DocStatus = 'AP' AND sysdate <= AM.EndDate ) OR" +
				" (sysdate <= AM.EndDate AND AM.DocStatus = 'IP')" +
				" order by b.created desc"/*+
				" AND B.AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID()*/;
//		System.out.println("SQLBRO: "+SQLBRO);
		
		try{
			pstmtBRO = DB.prepareStatement(SQLBRO, null);
			rsBRO = pstmtBRO.executeQuery();
			while(rsBRO.next()){
			brochures.add(rsBRO.getInt("BROID"));
			} // while
		}//try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsBRO);
			DB.closeStatement(pstmtBRO);
		} // finally
		
//		brochures.add(1000018);
		// Se recorren los folletos para agregar los prods		
		for(int bros = 0; bros < brochures.size(); bros++){
			// SE AGREGAN PRODUCTOS A LAS REFERENCIAS QUE FUERON AGREGADAS DESDE OC
			String SQLOC = " SELECT E.XX_VME_ELEMENTS_ID ELEMID, " +
					" R.XX_VME_REFERENCE_ID REFID, " +
					" AM.XX_VMA_MARKETINGACTIVITY_ID AMID, " +
					" BP.XX_VMA_BROCHUREPAGE_ID PAGEID " +
					" FROM XX_VMA_MARKETINGACTIVITY AM INNER JOIN XX_VMA_BROCHURE B " +
					" ON (AM.XX_VMA_BROCHURE_ID = B.XX_VMA_BROCHURE_ID) " +
					" INNER JOIN XX_VMA_BROCHUREPAGE BP " +
					" ON (B.XX_VMA_BROCHURE_ID = BP.XX_VMA_BROCHURE_ID)" +
					" INNER JOIN XX_VME_ELEMENTS E " +
					" ON (BP.XX_VMA_BROCHUREPAGE_ID = E.XX_VMA_BROCHUREPAGE_ID) " +
					" INNER JOIN XX_VME_REFERENCE R " +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID)" +
					" WHERE E.ISACTIVE = 'Y' " +
					" AND B.XX_VMA_BROCHURE_ID = " + brochures.get(bros) +
					" AND (R.C_ORDER_ID IS NOT NULL OR R.XX_VMR_ORDER_ID IS NOT NULL) " +
					" AND R.XX_VME_MANTAIN = 'Y' " +
					" ORDER BY E.XX_VME_ELEMENTS_ID";
//			System.out.println("SQLOC: "+SQLOC);

			try{
 				pstmtOC = DB.prepareStatement(SQLOC, null);
				rsOC = pstmtOC.executeQuery();
				while(rsOC.next()){
					elements.add(rsOC.getInt("ELEMID"));
					references.add(rsOC.getInt("REFID"));
					actions.add(rsOC.getInt("AMID"));
					pages.add(rsOC.getInt("PAGEID"));
				} // while
			}//try
			catch(Exception e){ e.printStackTrace(); }
			finally {
				DB.closeResultSet(rsOC);
				DB.closeStatement(pstmtOC);
			} // finally
			
			// Se procesan los elementos para agregarles los productos
			for(int i = 0; i < elements.size(); i++){
				processElementsOC(elements, actions, pages, references);
			}
			elements.clear();
			actions.clear();
			pages.clear();
			references.clear();
			// SE AGREGAN PRODUCTOS A LAS REFERENCIAS QUE FUERON MARCADAS COMO MANTENER YES
			String SQLAM = " SELECT R.XX_VME_REFERENCE_ID REFID, " +
					" E.XX_VME_ELEMENTS_ID ELEMID," +
					" AM.XX_VMA_MARKETINGACTIVITY_ID AMID, " +
					" BP.XX_VMA_BROCHUREPAGE_ID PAGEID " +
					" FROM XX_VMA_MARKETINGACTIVITY AM INNER JOIN XX_VMA_BROCHURE B " +
					" ON (AM.XX_VMA_BROCHURE_ID = B.XX_VMA_BROCHURE_ID) " +
					" INNER JOIN XX_VMA_BROCHUREPAGE BP " +
					" ON (B.XX_VMA_BROCHURE_ID = BP.XX_VMA_BROCHURE_ID)" +
					" INNER JOIN XX_VME_ELEMENTS E " +
					" ON (BP.XX_VMA_BROCHUREPAGE_ID = E.XX_VMA_BROCHUREPAGE_ID) " +
					" INNER JOIN XX_VME_REFERENCE R " +
					" ON (E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID)" +
					" WHERE E.ISACTIVE = 'Y' " +
					" AND (R.C_ORDER_ID IS NULL AND R.XX_VMR_ORDER_ID IS NULL)" +
					" AND R.XX_VME_MANTAIN = 'Y' " +
					" AND B.XX_VMA_BROCHURE_ID = " + brochures.get(bros) +
					" ORDER BY E.XX_VME_ELEMENTS_ID DESC ";
//			System.out.println("SQLAM: "+SQLAM);
			
			try{
				pstmtAM = DB.prepareStatement(SQLAM, null);
				rsAM = pstmtAM.executeQuery();
				while(rsAM.next()){
					elements.add(rsAM.getInt("ELEMID"));
					references.add(rsAM.getInt("REFID"));
					actions.add(rsAM.getInt("AMID"));
					pages.add(rsAM.getInt("PAGEID"));
				} // while
			}//try
			catch(Exception e){ e.printStackTrace(); }
			finally {
				DB.closeResultSet(rsAM);
				DB.closeStatement(pstmtAM);
			} // finally
			
			// Se procesan los elementos para agregarles los productos
//			for(int i = 0; i < elements.size(); i++){
				processElements(elements, actions, pages, references);
//			}
				elements.clear();
				actions.clear();
				pages.clear();
				references.clear();
		}
		Date fin = new Date();
		System.out.println("Agregar productos a Referencias en folleto: Inicio: "+inicio+" final: "+fin);
		return msj;
	} // doIt
	
	/** processElementsOC
	 * Procesa los elementos cuyas referencias se colocaron desde OC o Pedido
	 * @param elementID Identificador del elemento sobre el que se trabaja
	 * @param actionID Identificador de la accion
	 * @param pageID Identificador de la pagina
	 * */
	public void processElementsOC(Vector<Integer> elements, Vector<Integer> actions, 
			Vector<Integer> pages, Vector<Integer> references){
		String 	message = "";
		String order = "";
		X_XX_VME_Elements elemento = null;
		X_XX_VMA_MarketingActivity activity = null;
		
		refsUpdated = "";
		
		// Se procesan las referencias para asociarles los productos 
		for(int ref = 0; ref < references.size(); ref++){
			elemento = new X_XX_VME_Elements(Env.getCtx(), elements.get(ref), null);
			activity = new X_XX_VMA_MarketingActivity(Env.getCtx(),actions.get(ref), null);
			X_XX_VME_Reference reference = new X_XX_VME_Reference(Env.getCtx(), references.get(ref), null);
//			System.out.println("elemento y referencia "+ activity.getName()+ " "+elemento.getName()+" "+reference.getName());
			order = addProdsOrder(activity, reference, elemento, pages.get(ref));
//			System.out.println("ref oc:"+ref );
//		}

		// Si se agregaron productos, se envía el correo
		if(refsUpdated.length() > 0) {
			refsUpdated = refsUpdated.substring(0, refsUpdated.length()-2);	
			message += Msg.getMsg( Env.getCtx(), "XX_AddProdOrder", new String[]{elemento.getName(),refsUpdated});
			//System.out.println(message);
			if(refsUpdated.length() > 0) {
//				Utilities f = new Utilities(Env.getCtx(), null,
//						Env.getCtx().getContextAsInt("#XX_L_MT_ADDPRODTOREF_ID"), message, -1, 
//						Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, user, null);
//				try { f.ejecutarMail(); } 
//				catch (Exception e) { e.printStackTrace(); }
//				f = null;
				
				// Redistribuir las cantidades
				XX_VME_GeneralFunctions.redefineQuantities(elemento);
			}
			
		} // produc
		}	
	} // Fin processElementsOC

	/** addProdsOrder
	 * Procesa las referencias que se colocaron desde OC o pedido, se envía correo
	 * con la información actualizada para la referencia
	 * @param activity Actividad de mercadeo
	 * @param reference Identificador de la referencia
	 * @param element element Elemento al que se asocian los productos (referencias)
	 * */
	public String addProdsOrder(X_XX_VMA_MarketingActivity activity, 
			X_XX_VME_Reference reference, X_XX_VME_Elements elemento, Integer pageID){
		Vector<Integer> add = new Vector<Integer>();
		Vector<BigDecimal> cantidades = new Vector<BigDecimal>();
		Vector<Integer> referencias = new Vector<Integer>();
		Vector<Integer> productos = new Vector<Integer>();
		Vector<Boolean> manual = new Vector<Boolean>();
		Vector<Boolean> mantener = new Vector<Boolean>();
		Vector<Boolean> manualProd = new Vector<Boolean>();
		Vector<Integer> orderV = new Vector<Integer>();
		Vector<Integer> pedidoV = new Vector<Integer>();
		String SQLProds = "";
		Integer order = new Integer(0);
		String orderNum = "";
		PreparedStatement pstmtAdd = null;
		ResultSet rsAdd = null;

		if(reference.getC_Order_ID() != 0) {
			order = reference.getC_Order_ID();
			SQLProds = " SELECT P.M_PRODUCT_ID PRODID, " 
				+ " P.NAME NAME, "
				+ " C.DOCUMENTNO DOCNUMBER," 
				+ " P.XX_VMR_VENDORPRODREF_ID REF, "
				+ " 'OC' TIPO, "
				+ " C.C_ORDER_ID ID"
				+ " FROM  C_ORDER C INNER JOIN C_ORDERLINE CL "
				+ " ON (C.C_ORDER_ID = CL.C_ORDER_ID)"
				+ " INNER JOIN M_PRODUCT P "
				+ " ON (CL.M_PRODUCT_ID = P.M_PRODUCT_ID) "
				+ " WHERE CL.C_ORDER_ID = " + order
				+ " AND P.XX_VMR_VENDORPRODREF_ID = " + reference.getXX_VMR_VendorProdRef_ID()
				+ " AND C.XX_ORDERSTATUS IN ('RE','AP','CH','SIT')" 
				+ " AND P.M_PRODUCT_ID NOT IN " 
				+ "( SELECT PROD.M_PRODUCT_ID " 
				+ " FROM XX_VME_PRODUCT PROD INNER JOIN XX_VME_REFERENCE REF " 
				+ " ON (PROD.XX_VME_REFERENCE_ID = REF.XX_VME_REFERENCE_ID) " 
				+ " WHERE REF.XX_VME_ELEMENTS_ID = " + elemento.get_ID()
				+ " AND REF.XX_VMR_VENDORPRODREF_ID = " + reference.getXX_VMR_VendorProdRef_ID() + " )"; 
		}
		if(reference.getXX_VMR_Order_ID() != 0){
			order = reference.getXX_VMR_Order_ID();
			SQLProds = " SELECT DET.M_PRODUCT_ID PRODID, "
				+ " P.NAME NAME, " 
				+ " ORD.XX_OrderBecoCorrelative DOCNUMBER, "
				+ " P.XX_VMR_VENDORPRODREF_ID REF, "
				+ " 'PE' TIPO, "
				+ " ORD.XX_VMR_ORDER_ID ID"
				+ " FROM XX_VMR_ORDER ORD INNER JOIN XX_VMR_ORDERREQUESTDETAIL DET "
				+ " ON (ORD.XX_VMR_ORDER_ID = DET.XX_VMR_ORDER_ID) "
				+ " INNER JOIN M_PRODUCT P ON " 
				+ " (P.M_PRODUCT_ID = DET.M_PRODUCT_ID) " 
				+ " INNER JOIN XX_VMR_VENDORPRODREF VR "
				+ " ON (P.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID)"
				+ " WHERE DET.XX_VMR_ORDER_ID = " + order
				+ " AND VR.XX_VMR_VENDORPRODREF_ID = " + reference.getXX_VMR_VendorProdRef_ID()
				+ " AND P.M_PRODUCT_ID NOT IN " 
				+ "( SELECT PROD.M_PRODUCT_ID " 
				+ " FROM XX_VME_PRODUCT PROD INNER JOIN XX_VME_REFERENCE REF " 
				+ " ON (PROD.XX_VME_REFERENCE_ID = REF.XX_VME_REFERENCE_ID) " 
				+ " WHERE REF.XX_VME_ELEMENTS_ID = " + elemento.get_ID()
				+ " AND REF.XX_VMR_VENDORPRODREF_ID = " + reference.getXX_VMR_VendorProdRef_ID() + " )"; 
		}
//		System.out.println(SQLProds);
		
//		System.out.println("AM: "+activity.getName()+" Elemento: "+elemento.getName()+ " referencia: "+reference.getName()+" "+SQLProds.length());
		if(SQLProds.length() == 0){
			System.out.println("no hay sql");
		}
		try {
			pstmtAdd = DB.prepareStatement(SQLProds, null);
			rsAdd = pstmtAdd.executeQuery();
			while (rsAdd.next()) {
				add.add(rsAdd.getInt("PRODID"));
				orderNum = rsAdd.getString("DOCNUMBER");
				if(rsAdd.getString("TIPO").equals("OC")){
					orderV.add(rsAdd.getInt("ID"));
					pedidoV.add(0);
				}
				else {
					orderV.add(0);
					pedidoV.add(rsAdd.getInt("ID"));
				}
				referencias.add(rsAdd.getInt("REF"));
				cantidades.add(new BigDecimal(0));
				productos.add(rsAdd.getInt("PRODID"));
				manual.add(false);
				mantener.add(false);
				manualProd.add(false);
				refsUpdated += rsAdd.getString("NAME") + " O/C o Pedido: " + orderNum + ", ";
			}				
		}//try 
		catch (SQLException e) { 
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rsAdd); 
			DB.closeStatement(pstmtAdd);
		} 
	
		// Se asocian los productos
		if(productos.size() > 0){
			XX_VME_GeneralFunctions.createElemRefNew(elemento, activity, cantidades,	
					referencias, "A", manual, mantener, productos, manualProd, orderV, pedidoV);
			user = reference.getCreatedBy();
		}
	
		// Devuelve el numero de la OC o Pedido
		return orderNum;

	} // Fin addProdstoRef
	
	/** processElements
	 * Procesa los elementos cuyas referencias se colocaron como manuales
	 * @param elementID Identificador del elemento sobre el que se trabaja
	 * @param actionID Identificador de la accion
	 * @param pageID Identificador de la pagina	 * */
	public void processElements(Vector<Integer> elements, Vector<Integer> actions, 
			Vector<Integer> pages, Vector<Integer> references){
		String 	message = "";
		X_XX_VME_Elements elemento = null;
		X_XX_VMA_MarketingActivity activity = null;
		if(elements.size()!=0){
			int elementoAnterior = elements.get(0);  
			// Se procesan las referencias para asociarles los productos 
			for(int ref = 0; ref < references.size(); ref++){
				elemento = new X_XX_VME_Elements(Env.getCtx(), elements.get(ref), null);
				
				activity = new X_XX_VMA_MarketingActivity(Env.getCtx(),actions.get(ref), null);
				X_XX_VME_Reference reference = new X_XX_VME_Reference(Env.getCtx(), references.get(ref), null);
	//			System.out.println("elemento y referencia "+ activity.getName()+ " "+elemento.getName()+" "+reference.getName());
				addProdstoRef(activity, reference, elemento, pages.get(ref));
	
			
				if(refsUpdated.length() > 0) {
					refsUpdated = refsUpdated.substring(0, refsUpdated.length()-2);
					
					message += Msg.getMsg( Env.getCtx(), "XX_AddProdToRef", new String[]{elemento.getName(), refsUpdated});
					//System.out.println(message);
					// Si se agregaron productos, se envía el correo
		//			Utilities f = new Utilities(Env.getCtx(), null,
		//					Env.getCtx().getContextAsInt("#XX_L_MT_ADDPRODTOREF_ID"), message, -1, 
		//					Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, user, null);
		//			try { f.ejecutarMail(); } 
		//			catch (Exception e) { e.printStackTrace(); }
		//			f = null;
					
					// Se redistribuyen las cantidades
					if(elementoAnterior!=elemento.getXX_VME_Elements_ID()){
						XX_VME_GeneralFunctions.redefineQuantities(elemento);
						elementoAnterior = elemento.getXX_VME_Elements_ID();
					}
				} // msj
			}
		}
	} // Fin processReferences
	
	/** addProdstoRef
	 * Procesa las referencias que se colocaron como manuales, se envía correo
	 * con la información actualizada para la referencia
	 * @param activity Actividad de mercadeo
	 * @param referenceID Identificador de la referencia
	 * @param element element Elemento al que se asocian los productos (referencias)
	 * */
	public void addProdstoRef(X_XX_VMA_MarketingActivity activity, 
			X_XX_VME_Reference ref, X_XX_VME_Elements elemento, Integer pageID){
		Vector<Integer> products = new Vector<Integer>();
		Vector<Integer> add = new Vector<Integer>();
		Vector<String> names = new Vector<String>();
		Vector<BigDecimal> cantidades = new Vector<BigDecimal>();
		Vector<Integer> referencias = new Vector<Integer>();
		Vector<Integer> productos = new Vector<Integer>();
		Vector<Boolean> manual = new Vector<Boolean>();
		Vector<Boolean> mantener = new Vector<Boolean>();
		Vector<Boolean> manualProd = new Vector<Boolean>();
		Vector<Integer> order = new Vector<Integer>();
		Vector<Integer> pedido = new Vector<Integer>();
		String SQLAdd = "";
		PreparedStatement pstmtAdd = null;
		ResultSet rsAdd = null;
		// Para todas las referencias mantain, se procede a verificar los 
		// productos asociados, si falta alguno se agrega
		products.clear();
		add.clear();
		names.clear();
		refsUpdated = "";
			
		// Se verifican nuevos productos asociados a la referencia
		SQLAdd = " SELECT p.m_product_id PRODID, " +
			" P.NAME NOMBRE, " +
			// Se revisa en inventario
			" (SELECT  CASE WHEN  SUM(S.QTY) IS NULL THEN 0   ELSE SUM(S.QTY) END QTY " +
			" FROM M_STORAGEDETAIL S JOIN M_LOCATOR L ON(L.M_LOCATOR_ID = S.M_LOCATOR_ID)   " +
			" WHERE S.M_PRODUCT_ID = p.M_PRODUCT_ID AND L.ISDEFAULT = 'Y'  AND S.QTYTYPE = 'H' ) qtyM, " +
			
			// Se revisa OC con productos asociados
			" case when (SELECT case when SUM(L.QTYORDERED)  is null then 0 else SUM(L.QTYORDERED) end as CANTIDAD   " +
			" FROM C_ORDERLINE L JOIN C_ORDER O   ON (L.C_ORDER_ID=O.C_ORDER_ID)   " +
			" WHERE L.M_PRODUCT_ID = p.M_PRODUCT_ID " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) >= sysdate     " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) <= "+ DB.TO_DATE(activity.getEndDate()) +
			" AND O.ISSOTRX = 'N' AND O.XX_ORDERSTATUS IN ('AP','CH','RE')) is null then 0" +
			" else" +
			" (SELECT case when SUM(L.QTYORDERED)  is null then 0 else SUM(L.QTYORDERED) end as CANTIDAD   " +
			" FROM C_ORDERLINE L JOIN C_ORDER O   ON (L.C_ORDER_ID=O.C_ORDER_ID)   " +
			" WHERE L.M_PRODUCT_ID = p.M_PRODUCT_ID " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) >= sysdate     " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) <= "+ DB.TO_DATE(activity.getEndDate()) +
			" AND O.ISSOTRX = 'N' AND O.XX_ORDERSTATUS IN ('AP','CH','RE')) end qtyOC, " +
			
			// Se revisa OC con referencias asociadas
			" case when (SELECT case when SUM(L.XX_LineQty)  is null then 0 else SUM(L.XX_LineQty) end as CANTIDAD   " +
			" FROM XX_VMR_PO_LINEREFPROV L JOIN C_ORDER O ON (L.C_ORDER_ID=O.C_ORDER_ID)   " +
			" WHERE L.XX_VMR_VENDORPRODREF_ID = VP.XX_VMR_VENDORPRODREF_ID " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) >= sysdate     " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) <= "+ DB.TO_DATE(activity.getEndDate()) +
			" AND O.ISSOTRX = 'N' AND O.XX_ORDERSTATUS = 'AP') is null then 0" +
			" else" +
			" (SELECT case when SUM(L.XX_LineQty)  is null then 0 else SUM(L.XX_LineQty) end as CANTIDAD " +
			" FROM XX_VMR_PO_LINEREFPROV L JOIN C_ORDER O   ON (L.C_ORDER_ID=O.C_ORDER_ID)   " +
			" WHERE L.XX_VMR_VENDORPRODREF_ID = VP.XX_VMR_VENDORPRODREF_ID " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) >= sysdate     " +
			" AND TRUNC(O.XX_ESTIMATEDDATE) <= "+ DB.TO_DATE(activity.getEndDate()) +
			" AND O.ISSOTRX = 'N' AND O.XX_ORDERSTATUS IN ('AP')) end qtyREF" +
			" FROM M_PRODUCT p " +
			" LEFT OUTER JOIN XX_VMR_VENDORPRODREF VP " +
			" ON (VP.XX_VMR_VENDORPRODREF_ID = P.XX_VMR_VENDORPRODREF_ID)" +
			" WHERE VP.XX_VMR_VENDORPRODREF_ID = "+ ref.getXX_VMR_VendorProdRef_ID() +
			" AND P.M_PRODUCT_ID NOT IN " +
			"( SELECT P.M_PRODUCT_ID " +
			" FROM XX_VMA_MarketingActivity m inner join XX_VMA_BROCHURE B on (m.xx_vma_brochure_id=b.xx_vma_brochure_id ) " +
			"inner join XX_VMA_BROCHUREPAGE BP ON (B.XX_VMA_BROCHURE_ID = BP.XX_VMA_BROCHURE_ID) " +
			"INNER JOIN XX_VME_ELEMENTS E  ON (E.XX_VMA_BROCHUREPAGE_ID = BP.XX_VMA_BROCHUREPAGE_ID) " +
			"INNER JOIN XX_VME_REFERENCE REF  ON (E.XX_VME_ELEMENTS_ID = REF.XX_VME_ELEMENTS_ID) " +
			"INNER JOIN XX_VME_PRODUCT P  ON (REF.XX_VME_REFERENCE_ID = P.XX_VME_REFERENCE_ID)  " +
			" WHERE  (E.XX_VME_TYPE = 'P' OR E.XX_VME_TYPE = 'M')  and m.xx_vma_marketingactivity_id =" + activity.get_ID() +
			" and REF.XX_VME_ELEMENTS_ID = " + elemento.get_ID() +
			" AND REF.XX_VMR_VENDORPRODREF_ID = " + ref.getXX_VMR_VendorProdRef_ID() + " )"; 
		//System.out.println("SQLAdd: "+SQLAdd);
//		System.out.println("AM: "+activity.getName()+"Elemento: "+elemento.getName()+ " referencia: "+ref.getName()+" "+SQLAdd.length());
		if(SQLAdd.length() == 0){
			System.out.println("no hay sql");
		}
		try{
			pstmtAdd = DB.prepareStatement(SQLAdd, null);
			rsAdd = pstmtAdd.executeQuery();
			boolean productoAsociado = false;
			while(rsAdd.next()){
				if(rsAdd.getInt("QTYM") > 0 || rsAdd.getInt("QTYOC") > 0 || rsAdd.getInt("qtyREF") > 0){
					productoAsociado = true;
					mantener.add(false);
					manual.add(false);
					manualProd.add(false);
					add.add(rsAdd.getInt("PRODID"));
					names.add(rsAdd.getString("NOMBRE"));
				}
			}
			/*if(!productoAsociado && !ref.isXX_VME_Manual()){
				ref.setXX_VME_IndepabisQty(new BigDecimal(0));
				ref.setXX_VME_Manual(true);
				ref.save();
			}*/
			if(productoAsociado && ref.isXX_VME_Manual() && ref.getXX_VME_IndepabisQty().equals(new BigDecimal(0))){
				ref.setXX_VME_Manual(false);
				ref.save();
			}
			
		} //try
		catch(Exception e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsAdd); 
			DB.closeStatement(pstmtAdd);
		}// finally
			
		// Se compara con los productos asociados y se agrega de ser necesario
		for(int j = 0; j < add.size(); j++){			
			// Se crea el producto para obtener la referencia
			MProduct prod = new MProduct(Env.getCtx(), add.get(j), null);
			if(!referencias.contains(prod.getXX_VMR_VendorProdRef_ID())) {
					referencias.add(prod.getXX_VMR_VendorProdRef_ID());						
			}
			cantidades.add(new BigDecimal(0));
			manual.add(false);
			mantener.add(false);
			manualProd.add(false);
			productos.add(prod.get_ID());
			order.add(0);
			pedido.add(0);
			refsUpdated += names.get(j) + ", ";
		} // for 
		
		if(add.size() > 0) {
			// Se asocian los productos
			XX_VME_GeneralFunctions.createElemRefNew(elemento, activity, cantidades,	
					referencias, "A", manual, mantener, productos, manualProd, order, pedido);
			user = ref.getCreatedBy();
		}
				
	} // Fin addProdstoRef
	
} // FIn
