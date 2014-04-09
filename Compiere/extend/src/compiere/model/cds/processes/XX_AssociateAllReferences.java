package compiere.model.cds.processes;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JDialog;

import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MClient;
import org.compiere.model.MPInstance;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_M_AttributeInstance;
import org.compiere.model.X_M_AttributeSetInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.swing.CTextPane;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Trx;
import org.compiere.util.Util;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.X_Ref_M_Product_ProductType;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_Line;
import compiere.model.cds.X_XX_VMR_PO_LineRefProv;
import compiere.model.cds.X_XX_VMR_ReferenceMatrix;
import compiere.model.cds.X_XX_VMR_Section;


/** Permite asociar cada referencia de una orden de compra el producto correspondiente, 
 *  (solo en caso de que no exista más de un producto a asociar por referencia)
 *  En caso de no existir el producto se crea automáticamente.
 *  
 *  @author Gabrielle Huchet
 */

public class XX_AssociateAllReferences extends SvrProcess {

	
	Ctx ctx = Env.getCtx();
	Trx trx = get_Trx();
	private String messageNoTitle = "";
	private String messageAsoTitle = "";
	private String messageRefTitle = "";
	private String msgNewProdTitle = "";
	private String messageNo = "";
	private String messageAso = "";
	private String messageRef = "";
	private String messageEmpty = "";
	private String msgNewProd = "";
	private MOrder order = null;
	private int  approvePO = 0;

	
	protected void prepare() {
	
	}


	protected String doIt() throws Exception {
		
		approvePO = Env.getCtx().getContextAsInt("#APPROVEPOPROCESS");
		//Se setea en False el Batch Mode para que corra algunas partes del código de afterSave en MProduct, ya que exige este campo en False 
		getCtx().setBatchMode(false);
	
		int dialog=Env.getCtx().getContextAsInt("#Dialog_Associate_Aux");

		if(dialog==1){
			ADialog.info(1,new Container(),"MustRefresh");
			Env.getCtx().remove("#Dialog_Associate_Aux");
		}
		
		messageAsoTitle = "Se asociaron las siguientes combinaciones, con los siguientes productos:\n";
		
		messageNoTitle = "Las siguientes combinaciones no se han podido asociar automáticamente:\n";
		
		messageRefTitle = "Debe completar los campos Clasificación de Producto y/o Tipo de Etiqueta en las siguientes referencias:\n";
				
		msgNewProdTitle = "Los siguientes productos se crearon automáticamente:\n";
		
		messageEmpty = "No se encontraron referencias pendientes de asociación";

		order = new MOrder(ctx, getRecord_ID(), trx);
		
		//Se asocian las referencias que cumplan con no tener más de un producto posible para cada combinación de atributos,
		//y en caso de no tener producto se crea uno nuevo.
		AsociateReferences(order.getC_Order_ID());
		
		//Si todas las referencias tienen producto asociado  se procede a completar información en la O/C 
	    Boolean check = true;
		
		String sql = "\nSELECT A.XX_REFERENCEISASSOCIATED AS ASOCIATE " +
				"\nFROM XX_VMR_PO_LINEREFPROV A , C_ORDER B " +
				"\nWHERE B.ISSOTRX='N' AND A.C_ORDER_ID = B.C_ORDER_ID " +
				"\nAND A.C_ORDER_ID = "+order.getC_Order_ID();
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
	    
		    while(rs.next())
		    {
		    	if(rs.getString("ASOCIATE").trim().equalsIgnoreCase("N")){
		    		check = false;
		    	}
		    }
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
 
		if(msgNewProd.compareTo("")!=0){
			msgNewProdTitle += msgNewProd;
			CTextPane tPanel= new CTextPane();
			setInfoMessage(msgNewProdTitle, tPanel);
			JScrollPane scrollpane = new JScrollPane(tPanel);
			scrollpane.setBorder(null);
			scrollpane.setPreferredSize(new Dimension(500,300)); 
			JOptionPane optionPane = new JOptionPane(scrollpane,JOptionPane.INFORMATION_MESSAGE);  
			JDialog dial = optionPane.createDialog("");  
			dial.setSize(new Dimension(600,400));
			dial.setResizable(true);  
			dial.setVisible(true); 
		}
		if(messageAso.compareTo("")!=0){
			messageAsoTitle += messageAso;
			CTextPane tPanel= new CTextPane();
			setInfoMessage(messageAsoTitle, tPanel);
			JScrollPane scrollpane = new JScrollPane(tPanel);
			scrollpane.setBorder(null);
			scrollpane.setPreferredSize(new Dimension(500,400)); 
			JOptionPane optionPane = new JOptionPane(scrollpane,JOptionPane.INFORMATION_MESSAGE);  
			JDialog dial = optionPane.createDialog("");  
			dial.setSize(new Dimension(tPanel.getWidth()+50,tPanel.getHeight()));
			dial.setResizable(true);  
			dial.setVisible(true); 
		}
		if(messageRef.compareTo("")!=0){
			messageRefTitle += messageRef;
			CTextPane tPanel= new CTextPane();
			setInfoMessage(messageRefTitle, tPanel);
			JScrollPane scrollpane = new JScrollPane(tPanel);
			scrollpane.setBorder(null);
			scrollpane.setPreferredSize(new Dimension(600,400)); 
			JOptionPane optionPane = new JOptionPane(scrollpane,JOptionPane.INFORMATION_MESSAGE);  
			JDialog dial = optionPane.createDialog("");  
			dial.setSize(new Dimension(tPanel.getWidth()+50,tPanel.getHeight()));
			dial.setResizable(true);  
			dial.setVisible(true); 
		}
		if(messageNo.compareTo("")!=0){
			messageNoTitle += messageNo;
			CTextPane tPanel= new CTextPane();
			setInfoMessage(messageNoTitle, tPanel);
			JScrollPane scrollpane = new JScrollPane(tPanel);
			scrollpane.setBorder(null);
			scrollpane.setPreferredSize(new Dimension(500,300)); 
			JOptionPane optionPane = new JOptionPane(scrollpane,JOptionPane.INFORMATION_MESSAGE);  
			JDialog dial = optionPane.createDialog("");  
			dial.setSize(new Dimension(600,400));
			dial.setResizable(true);  
			dial.setVisible(true); 
		}
		if (messageNo.compareTo("")==0 && messageAso.compareTo("")==0 && messageRefTitle.compareTo("")==0){
			ADialog.info(1,new Container(),messageEmpty);
		}
		
		if(check) {
			completeInfo(order);
			if(order.getXX_ComesFromSITME()==true && approvePO == 1){
				//Proceso Colocar la Orden Compra Lista y Aprobar: XX_PO_OrderReadyEmail
				int APPROVEPROCESS_ID = 1000108;//Env.getCtx().getContextAsInt("#XX_L_PROCESS   ");
				//Se llama al Proceso de Orden Lista y de Aprobar Orden de Compra: XX_PO_OrderReadyEmail
				callProcess(APPROVEPROCESS_ID, order.get_ID());
			}
		}
		
		else {
			// si alguna referencia no tiene producto asociado
			sql = "\nUPDATE C_Order po"	+										
				"\nSET XX_NatTreasuryEstAmount= 0" +
				"\n, XX_SENIATESTEEMEDAMUNT=  0"+
				"\nWHERE po.C_Order_ID=" + order.getC_Order_ID();						
				DB.executeUpdate(null, sql);
				
			if(order.getXX_ComesFromSITME()==true && approvePO == 1){
				Env.getCtx().setContext("#WAITPROCESS",-1); //AGREGADO POR GHUCHET
				order.set_Value("XX_SitmeUnassociatedRef","Y");
				order.save(trx);
				commit();
			}
			//sendMailSitmeToNational(order.get_ID(), order.getRef_Order_ID());
		}
		

		return "";
	}

	/**
	 *	Convierte Texto plano en HTML y Calcula Tamaño del Dialogo.
	 *  @param message message
	 */
	private void setInfoMessage(String message, CTextPane info)
	{
		StringBuffer sb = new StringBuffer (message.length()+20);
		sb.append("<b>");
		String html = Util.maskHTML(message);
		char[] chars = html.toCharArray();
		boolean first = true;
		int paras = 0;
		for (char c : chars) {
			if (c == '\n')
			{
				if (first)
				{
					sb.append("</b>");
					first = false;
				}
				if (paras > 0)
					sb.append("<br>");
				else
					sb.append("<p>");
				paras++;
			}
			else
				sb.append(c);
		}
		info.setText(sb.toString());
		Dimension size = info.getPreferredSize();
		size.width = 450;
		size.height = (Math.max(paras, message.length()/60)+1) * 30;
		size.height = Math.min(size.height, 600);
		info.setPreferredSize(size);
		
		info.setRequestFocusEnabled(false);
		info.setReadWrite(false);
		info.setOpaque(false);
		info.setBorder(null);
		//
		info.setCaretPosition(0);
	}	//	calculateSize
	
	/**
	 * Asocia las referencias de una orden de compra con los productos sugeridos correspondientes.
	 * @param orderID
	 */
	private void AsociateReferences(int orderID) {
		

		int lineRefProvID = 0;
		 X_XX_VMR_PO_LineRefProv lineRefProv = null;
		String sql = "\nSELECT LRP.XX_VMR_PO_LINEREFPROV_ID" +
				"\nFROM XX_VMR_PO_LINEREFPROV LRP JOIN C_ORDER O ON (LRP.C_ORDER_ID = O.C_ORDER_ID)" +
				"\nWHERE O.ISSOTRX='N' AND O.C_ORDER_ID = "+orderID;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
	    ResultSet rs = null;
	    
	    try{
	    	rs = pstmt.executeQuery();
	    	 while(rs.next()){
	    		 lineRefProvID = rs.getInt("XX_VMR_PO_LINEREFPROV_ID");
	    		 lineRefProv = new X_XX_VMR_PO_LineRefProv(ctx, lineRefProvID, trx);
	    		 
	    		 //Si la referencia tiene matriz de características
	    		 if(lineRefProv.isXX_WithCharacteristic()){
	    			 //Se crean los productos que no aún no existen
	    			 createNewProductWith(lineRefProv);
	    			 //Se asocian las referencias con caraceristicas que tengan 1 y solo 1 producto posible para asociar
	    			 cmd_associateAll(lineRefProv);
	    			 //Se agrega al mensaje de Referencias no asociadas, aquellos que tengan más de 1 producto posible para asociar
	    			 cmd_unassociatedDupl(lineRefProv);
	    		//Si la referencia NO tiene matriz de características
	    		 }else {
	    			 //Se verifica que la referencia no tenga producto asociado, si no tiene se asocia
	    			 if(!verify(lineRefProv)){
	    				 cmd_associate(lineRefProv);
	    			 }
	    		 }
	    		 fullAssociated(lineRefProvID);
			    }
	    }catch (Exception e) {
	    	e.printStackTrace();
	    }finally {
	    	DB.closeResultSet(rs);
	    	DB.closeStatement(pstmt);
	   }
	}

	/**
	 * Si la referencia tiene todas sus combinaciones asociadas a un producto se 
	 * hace update a la tabla XX_VMR_PO_LineRefProv indicando que todas las 
	 * combinaciones de la referencia están asociadas (XX_ReferenceIsAssociated='Y'),
	 * en caso contrario indica que la referencia no está asociada (XX_ReferenceIsAssociated='N').
	 * @param lineRefProvID
	 */
	private void fullAssociated(int lineRefProvID){
		
		boolean associated=false;
		
		String sql ="\nSelect * " +
		"\nfrom  XX_VMR_REFERENCEMATRIX " +
		"\nwhere (M_product IS NOT NULL OR XX_QUANTITYV = 0) AND XX_VMR_PO_LINEREFPROV_ID="+lineRefProvID;		
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		try 
		{
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				associated=true;
			}
									
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,sql,a);
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if(associated==true){
			String SQL10 = "UPDATE XX_VMR_PO_LineRefProv "
			    +" SET XX_ReferenceIsAssociated='Y'"
			    + " WHERE XX_VMR_PO_LineRefProv_ID="+lineRefProvID;
				
				DB.executeUpdate(null, SQL10);
			
		}else{
			String SQL10 = "UPDATE XX_VMR_PO_LineRefProv "
			    +" SET XX_ReferenceIsAssociated='N'"
			    + " WHERE XX_VMR_PO_LineRefProv_ID="+lineRefProvID;
				System.out.println(lineRefProvID);
				DB.executeUpdate(null, SQL10);
		}
		
	}
	
	/** Asocia las combinaciones de una referencia siempre que éstas solo tengan un único producto sugerido
	 * Agrega a un mensaje las combinaciones con su producto asociado
	 * */
	private void cmd_associateAll(X_XX_VMR_PO_LineRefProv LineRefProv) {
	
		//Asocio para cada una de las referencia que tengan 1 solo producto sugerido
		Vector<Vector<Object>> allInfo = new Vector<Vector<Object>>();
		allInfo = getCombinationAndProduct(LineRefProv);
		
		//Si hay productos que asociar
		if(allInfo.size()>0){

			
			Vector<Object> info = null;
			for(int i=0; i<allInfo.size(); i++){
	
				info = allInfo.get(i);
				
				if(info.get(2)!=null){
					messageAso += info.get(1)+"-"+ info.get(2)+": "+ info.get(4)+" ("+info.get(5)+")"+"\n";
				}else{
					messageAso += info.get(1)+": "+ info.get(4)+" ("+info.get(5)+")"+"\n";
				}
			}

			for(int i=0; i<allInfo.size(); i++){
				//Actualizo
				info = allInfo.get(i);
				doUpdate((Integer)info.get(3), (Integer)info.get(0));
				}
			}
	
	}   //  cmd_associateAll
	
	/**
	 * Retorna el id del único producto sugerido, si tiene mas de 1 o ninguno retorna 0
	 */
	private Vector<Vector<Object>> getCombinationAndProduct(X_XX_VMR_PO_LineRefProv LineRefProv){
		
		String sql = "\nSELECT XX_VMR_ReferenceMatrix_ID, " +
			"\n(SELECT NAME FROM M_ATTRIBUTEVALUE WHERE M_ATTRIBUTEVALUE_ID = rm.XX_Value1) as Char1," +
			"\n(SELECT NAME FROM M_ATTRIBUTEVALUE WHERE M_ATTRIBUTEVALUE_ID = rm.XX_Value2) as Char2," +
			"\np.M_Product_ID, " +
			"\np.value, " +
			"\np.Name "+
			"\nFROM XX_VMR_ReferenceMatrix rm, M_Product p "+
			"\nWHERE rm.XX_VMR_PO_LINEREFPROV_ID= "+ LineRefProv.get_ID() +" "+
			"\nAND p.C_TAXCATEGORY_ID="+ LineRefProv.getC_TaxCategory_ID()+ " "+
			"\nAND p.XX_VMR_VENDORPRODREF_ID = " + LineRefProv.getXX_VMR_VendorProdRef_ID() + " " +
			"\nAND p.M_ATTRIBUTESETINSTANCE_ID "+ 
			    "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where M_ATTRIBUTEVALUE_ID = rm.XX_Value1) " +
			"\nAND p.M_ATTRIBUTESETINSTANCE_ID " +
			    "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where (M_ATTRIBUTEVALUE_ID = rm.XX_Value2 or 0 = rm.XX_Value2)) " +
			"\nAND rm.XX_VMR_ReferenceMatrix_ID IN ("+
			    "\nSELECT XX_VMR_ReferenceMatrix_ID "+
			    "\nFROM XX_VMR_ReferenceMatrix rm, M_Product p "+ 
			    "\nWHERE rm.XX_VMR_PO_LINEREFPROV_ID= "+ LineRefProv.get_ID() +" "+
			    "\nAND p.XX_VMR_VENDORPRODREF_ID = " + LineRefProv.getXX_VMR_VendorProdRef_ID() + " " +
			    "\nAND p.C_TAXCATEGORY_ID="+ LineRefProv.getC_TaxCategory_ID()+ " "+
			    "\nAND rm.M_Product IS NULL AND XX_QUANTITYV <> 0 " +
			    "\nAND p.M_ATTRIBUTESETINSTANCE_ID " +
			        "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where M_ATTRIBUTEVALUE_ID = rm.XX_Value1) " +
			    "\nAND p.M_ATTRIBUTESETINSTANCE_ID " +
			        "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where (M_ATTRIBUTEVALUE_ID = rm.XX_Value2 or 0 = rm.XX_Value2)) " +
			    "\ngroup by XX_VMR_ReferenceMatrix_ID " +
			    "\nhaving count(XX_VMR_ReferenceMatrix_ID) = 1 " +
			"\n)";
			
		Vector<Vector<Object>> allInfo = new Vector<Vector<Object>>();
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				Vector<Object> info = new Vector<Object>();
				info.add(rs.getInt(1));
				info.add(rs.getString(2));
				info.add(rs.getString(3));
				info.add(rs.getInt(4));
				info.add(rs.getString(5));
				info.add(rs.getString(6));
				
				//Agrego la informacion de la linea al vector principal
				allInfo.add(info);
			}
			
									
			rs.close();
			pstmt.close();				
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,sql,a);
		}
				
		return allInfo;
	}
	
	/** Agrega a un mensaje las combinacions de una referencia que no 
	 * pueden ser asociadas por tener más de un producto sugerido 
	 * */
	private void cmd_unassociatedDupl(X_XX_VMR_PO_LineRefProv LineRefProv) {
		
		//No se pueden asociar referencias que tengan más de un producto sugerido
		Vector<Vector<Object>> allInfo = new Vector<Vector<Object>>();
		allInfo = getDuplicateProduct(LineRefProv);
		MVMRVendorProdRef VendorProdRef = new MVMRVendorProdRef(ctx, LineRefProv.getXX_VMR_VendorProdRef_ID(), trx);
		
		//Si hay más de un producto sugerido
		if(allInfo.size()>0){
			
			Vector<Object> info = null;
			for(int i=0; i<allInfo.size(); i++){
	
				info = allInfo.get(i);
				
				if(info.get(2)!=null){
					messageNo += VendorProdRef.getName()+": "+info.get(1)+"-"+ info.get(2)+"\n";
				}else{
					messageNo += VendorProdRef.getName()+": "+info.get(1)+"\n";
				}
			}
		}
	
	}   //  cmd_unassociatedDupl
	
	/**
	 * Retorna un vector con aquellas combinaciones de una referencia con caracteristicas  
	 * que tienen más de un producto a asociar sugerido
	 */
	private Vector<Vector<Object>> getDuplicateProduct(X_XX_VMR_PO_LineRefProv LineRefProv){
		
		String sql = "\nSELECT XX_VMR_ReferenceMatrix_ID, " +
			"\n(SELECT NAME FROM M_ATTRIBUTEVALUE WHERE M_ATTRIBUTEVALUE_ID = rm.XX_Value1) as Char1," +
			"\n(SELECT NAME FROM M_ATTRIBUTEVALUE WHERE M_ATTRIBUTEVALUE_ID = rm.XX_Value2) as Char2" +
			"\nFROM XX_VMR_ReferenceMatrix rm, M_Product p "+
			"\nWHERE rm.XX_VMR_PO_LINEREFPROV_ID= "+ LineRefProv.get_ID() +" "+
			"\nAND p.C_TAXCATEGORY_ID="+ LineRefProv.getC_TaxCategory_ID()+ " "+
			"\nAND p.XX_VMR_VENDORPRODREF_ID = " + LineRefProv.getXX_VMR_VendorProdRef_ID() + " " +
			"\nAND p.M_ATTRIBUTESETINSTANCE_ID "+ 
			    "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where M_ATTRIBUTEVALUE_ID = rm.XX_Value1) " +
			"\nAND p.M_ATTRIBUTESETINSTANCE_ID " +
			    "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where (M_ATTRIBUTEVALUE_ID = rm.XX_Value2 or 0 = rm.XX_Value2)) " +
			"\nAND rm.XX_VMR_ReferenceMatrix_ID IN ("+
			    "\nSELECT XX_VMR_ReferenceMatrix_ID "+
			    "\nFROM XX_VMR_ReferenceMatrix rm, M_Product p "+ 
			    "\nWHERE rm.XX_VMR_PO_LINEREFPROV_ID= "+ LineRefProv.get_ID() +" "+
			    "\nAND p.XX_VMR_VENDORPRODREF_ID = " + LineRefProv.getXX_VMR_VendorProdRef_ID() + " " +
			    "\nAND p.C_TAXCATEGORY_ID="+ LineRefProv.getC_TaxCategory_ID()+ " "+
			    "\nAND rm.M_Product IS NULL AND XX_QUANTITYV <> 0 " +
			    "\nAND p.M_ATTRIBUTESETINSTANCE_ID " +
			        "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where M_ATTRIBUTEVALUE_ID = rm.XX_Value1) " +
			    "\nAND p.M_ATTRIBUTESETINSTANCE_ID " +
			        "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where (M_ATTRIBUTEVALUE_ID = rm.XX_Value2 or 0 = rm.XX_Value2)) " +
			    "\ngroup by XX_VMR_ReferenceMatrix_ID " +
			    "\nhaving count(XX_VMR_ReferenceMatrix_ID) > 1 " +
			"\n)";
			
		Vector<Vector<Object>> allInfo = new Vector<Vector<Object>>();
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		try 
		{
			 rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				Vector<Object> info = new Vector<Object>();
				info.add(rs.getInt(1));
				info.add(rs.getString(2));
				info.add(rs.getString(3));
				
				//Agrego la informacion de la linea al vector principal	
				allInfo.add(info);
			}
					
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,sql,a);
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
				
		return allInfo;
	}

	/**
	 * Retorna un vector con aquellas combinaciones de una referencia con caracteristicas 
	 * que no tienen ningún producto posible para asociar
	 */
	private Vector<Vector<Object>> getProductsToCreate(X_XX_VMR_PO_LineRefProv LineRefProv){
		
		String sql = "\nSELECT XX_VMR_ReferenceMatrix_ID, " +
			"\n(SELECT M_ATTRIBUTEVALUE_ID FROM M_ATTRIBUTEVALUE WHERE M_ATTRIBUTEVALUE_ID = rm.XX_Value1) as attr1," +
			"\n(SELECT M_ATTRIBUTEVALUE_ID FROM M_ATTRIBUTEVALUE WHERE M_ATTRIBUTEVALUE_ID = rm.XX_Value2) as attr2 " +
			"\nFROM XX_VMR_ReferenceMatrix rm" +
			"\nLEFT JOIN M_Product p ON "+
			"\n(p.C_TAXCATEGORY_ID="+ LineRefProv.getC_TaxCategory_ID()+ " "+
			"\nAND p.XX_VMR_VENDORPRODREF_ID = " + LineRefProv.getXX_VMR_VendorProdRef_ID() + " " +
			"\nAND p.M_ATTRIBUTESETINSTANCE_ID "+ 
			    "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where M_ATTRIBUTEVALUE_ID = rm.XX_Value1) " +
			"\nAND p.M_ATTRIBUTESETINSTANCE_ID " +
			    "\nIN (Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where (M_ATTRIBUTEVALUE_ID = rm.XX_Value2 or 0 = rm.XX_Value2)))" +
			 "\nWHERE rm.XX_VMR_PO_LINEREFPROV_ID= "+ LineRefProv.get_ID() +" "+
			"\nAND p.M_Product_ID IS NULL" +
			"\nAND rm.M_Product IS NULL" +
			"\nAND rm.XX_QUANTITYV <> 0";
			
		Vector<Vector<Object>> allInfo = new Vector<Vector<Object>>();
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		try 
		{
			 rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				Vector<Object> info = new Vector<Object>();
				info.add(rs.getInt(1));
				
				//Agrego la informacion de la linea al vector principal	
				allInfo.add(info);
			}
					
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,sql,a);
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
				
		return allInfo;
	}
	
	/**
	 *  Hace el update en la matriz de referencias agregando el producto asociado 
	 *  a una combinación de atributos de una referencia.
	 */
	public void doUpdate(int Product, int referenceID){
		
		X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(),referenceID,null);
		matrix.setM_Product(Product);
		matrix.save();

	}
	
	/** Crea los producto no existentes de una referencia con características
	 * 
	 * @param lineRefProv
	 * @return 
	 */
	private String createNewProductWith(X_XX_VMR_PO_LineRefProv lineRefProv){

		//No se pueden asociar referencias que tengan más de un producto sugerido
		Vector<Vector<Object>> allInfo = new Vector<Vector<Object>>();
		allInfo = getProductsToCreate(lineRefProv);
		MVMRVendorProdRef vendorProdRef = new MVMRVendorProdRef(ctx, lineRefProv.getXX_VMR_VendorProdRef_ID(), null);
		
		if (vendorProdRef.getXX_VMR_TypeLabel_ID() > 0 && vendorProdRef.getXX_VMR_ProductClass_ID() > 0 ){
			KeyNamePair referenceValue1 = null;
			KeyNamePair referenceValue2 = null;
			int refMatrixID  = 0;
			//Si hay más de un producto sugerido
			if(allInfo.size()>0){					
				Vector<Object> info = null;
				for(int i=0; i<allInfo.size(); i++){
				
					info = allInfo.get(i);
					refMatrixID  =  (Integer) info.get(0);
					
					String sql ="\nselect (select Name from M_Attributevalue m where m.M_Attributevalue_ID=tab.XX_Value1) Name1, tab.XX_Value1, " +
							  "\n(select Name from M_Attributevalue m where m.M_Attributevalue_ID=tab.XX_Value2) Name2, tab.XX_Value2 "+
							  "\nFROM XX_VMR_ReferenceMatrix tab " +
							  "\nWHERE tab.XX_VMR_PO_LINEREFPROV_ID ="+lineRefProv.getXX_VMR_PO_LineRefProv_ID()+
							  "\nAND tab.XX_VMR_ReferenceMatrix_ID = " +refMatrixID+
							  "\nAND tab.M_Product IS NULL" +
							  "\nAND tab.XX_QUANTITYV <> 0";

					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					ResultSet rs = null;
					try 
					{
						 rs = pstmt.executeQuery();
						
						while(rs.next())
						{	
							referenceValue1 = new KeyNamePair(rs.getInt("XX_Value1"), rs.getString("Name1"));
							referenceValue2 = new KeyNamePair(rs.getInt("XX_Value2"), rs.getString("Name2"));
						}
								
					}
					catch(Exception a)
					{	
						log.log(Level.SEVERE,sql,a);
					}finally {
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}
							
					//Selecciono el Attribute set
					int attributeSet = vendorProdRef.getM_AttributeSet_ID();
					
					//Creo el M_AttributeSetInstance
					X_M_AttributeSetInstance newAttributeSetInstance = new X_M_AttributeSetInstance(ctx,0,trx);
					newAttributeSetInstance.setM_AttributeSet_ID(attributeSet);
					if(lineRefProv.getXX_Characteristic2_ID()!=0){
						newAttributeSetInstance.setDescription(referenceValue1.getName()+"_"+referenceValue2.getName());
					}else{
						newAttributeSetInstance.setDescription(referenceValue1.getName());
					}
					newAttributeSetInstance.save();
						
					X_M_AttributeInstance newAttributeInstance = new X_M_AttributeInstance(ctx,0,trx);
					newAttributeInstance.setM_AttributeSetInstance_ID(newAttributeSetInstance.getM_AttributeSetInstance_ID());
					newAttributeInstance.setM_AttributeValue_ID(referenceValue1.getKey());
					newAttributeInstance.setValue(referenceValue1.getName());
					newAttributeInstance.setM_Attribute_ID(lineRefProv.getXX_Characteristic1_ID());
					newAttributeInstance.save();
						
					if(lineRefProv.getXX_Characteristic2_ID()!=0){
						X_M_AttributeInstance newAttributeInstance2 = new X_M_AttributeInstance(ctx,0,trx);
						newAttributeInstance2.setM_AttributeSetInstance_ID(newAttributeSetInstance.getM_AttributeSetInstance_ID());
						newAttributeInstance2.setM_AttributeValue_ID(referenceValue2.getKey());
						newAttributeInstance2.setValue(referenceValue2.getName());
						newAttributeInstance2.setM_Attribute_ID(lineRefProv.getXX_Characteristic2_ID());
						newAttributeInstance2.save();
					}
					
					commit();
					
				    X_XX_VMR_Department depart = new X_XX_VMR_Department(getCtx(),vendorProdRef.getXX_VMR_Department_ID(),null);				    
				    int category = depart.getXX_VMR_Category_ID();
				    
				    X_XX_VMR_Line line = new X_XX_VMR_Line(getCtx(), vendorProdRef.getXX_VMR_Line_ID(),null);
				    int typeInventory = line.getXX_VMR_TypeInventory_ID();
				    
				    MProduct newProduct = new MProduct(getCtx(), vendorProdRef.getXX_VMR_Department_ID(), 
				    		vendorProdRef.getXX_VMR_Line_ID(), vendorProdRef.getXX_VMR_Section_ID(), vendorProdRef.get_ID(),
				    		vendorProdRef.getC_TaxCategory_ID(),vendorProdRef.getXX_VME_ConceptValue_ID(),typeInventory, get_Trx());
				   
					// Se buscará si por la referencia para producto ya existe para asignarle el Tipo de Exhibición
					String sql2 = "select * from M_Product where XX_VMR_DEPARTMENT_ID = "+vendorProdRef.getXX_VMR_Department_ID()+" and " +
						  "XX_VMR_LINE_ID = "+vendorProdRef.getXX_VMR_Line_ID()+" and XX_VMR_SECTION_ID = "+vendorProdRef.getXX_VMR_Section_ID()+" and " +
						  "XX_VMR_VendorProdRef_id = "+vendorProdRef.getXX_VMR_VendorProdRef_ID()+" AND XX_VMR_TypeExhibition_ID IS NOT NULL order by M_Product_ID desc";
					PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
					ResultSet rs2 = null;
					try {
						System.out.println(sql2);
						rs2 = pstmt.executeQuery();
						if (rs2.next()){
							newProduct.setXX_VMR_TypeExhibition_ID(rs2.getInt("XX_VMR_TypeExhibition_ID"));	 
						}
					} catch (SQLException e) {
						
						e.printStackTrace();
					}finally{
						DB.closeResultSet(rs2);
						DB.closeStatement(pstmt2);
					}
					
				    if(vendorProdRef.getXX_VMR_Section_ID() >0){
				    	X_XX_VMR_Section section = new X_XX_VMR_Section(Env.getCtx(), vendorProdRef.getXX_VMR_Section_ID(), null);
				    	newProduct.setName(section.getName());
					 }else {
						 newProduct.setName(vendorProdRef.getName());
					 }
				    
				    newProduct.setXX_VMR_Category_ID(category);
				    newProduct.setXX_VMR_LongCharacteristic_ID(vendorProdRef.getXX_VMR_LongCharacteristic_ID());
				    newProduct.setXX_VMR_Brand_ID(vendorProdRef.getXX_VMR_Brand_ID());				    
				    newProduct.setXX_VMR_UnitConversion_ID(vendorProdRef.getXX_VMR_UnitConversion_ID());
				    newProduct.setXX_PiecesBySale_ID(vendorProdRef.getXX_PiecesBySale_ID());
				    newProduct.setXX_VMR_UnitPurchase_ID(vendorProdRef.getXX_VMR_UnitPurchase_ID());
					newProduct.setXX_SaleUnit_ID(vendorProdRef.getXX_SaleUnit_ID());
				    newProduct.setC_Country_ID(order.getC_Country_ID());
				    newProduct.setIsActive(true);		
				    newProduct.setC_BPartner_ID(vendorProdRef.getC_BPartner_ID());
				    newProduct.setXX_VMR_TypeLabel_ID(vendorProdRef.getXX_VMR_TypeLabel_ID());
				    newProduct.setXX_VMR_ProductClass_ID(vendorProdRef.getXX_VMR_ProductClass_ID());
				    newProduct.setM_AttributeSetInstance_ID(newAttributeSetInstance.get_ID());
				    newProduct.setM_AttributeSet_ID(attributeSet);
				    newProduct.setProductType(X_Ref_M_Product_ProductType.ITEM.getValue());
				    newProduct.save();
					get_Trx().commit();
					
					msgNewProd +=  newProduct.getValue()+"-"+ newProduct.getName()+"\n";
				}		
			}
		}else {
			messageRef += vendorProdRef.getValue()+"-"+vendorProdRef.getName()+"\n";
			messageNo += vendorProdRef.getValue()+"-"+vendorProdRef.getName()+"\n";
		}
		return "";	    	
	}   //  cmd_newProduct
	
	/**
	 *  Indica si una referencia de la O/C dada tiene producto asociado
	 */
	private boolean verify(X_XX_VMR_PO_LineRefProv lineRefProv){
		String sql ="SELECT XX_VMR_ReferenceMatrix_ID "
			+ "FROM XX_VMR_ReferenceMatrix "
			+ "WHERE XX_VMR_PO_LINEREFPROV_ID="+lineRefProv.getXX_VMR_PO_LineRefProv_ID();	
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				return true;
							
		}
		catch (SQLException e){
			log.log(Level.SEVERE, sql, e);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return false;
	} 
	
	/**Verifica si existe un producto a asociar a la referencia, si no existe lo crea, 
	 * luego asocia el producto a la referencia sin caracteristicas.
	 * 
	 * @param lineRefProv
	 */
	
	private void cmd_associate(X_XX_VMR_PO_LineRefProv lineRefProv)
	{
		MVMRVendorProdRef vendorProdRef = new MVMRVendorProdRef(ctx, lineRefProv.getXX_VMR_VendorProdRef_ID(), trx);
		int productID = getProductToAssociate(vendorProdRef.get_ID());
		
		//Si el producto no existe se crea
		if(productID == 0){
			if (vendorProdRef.getXX_VMR_TypeLabel_ID()>0 && vendorProdRef.getXX_VMR_ProductClass_ID() >0 ){
				productID = createNewProduct(lineRefProv,vendorProdRef);
			}else {
				messageRef += vendorProdRef.getValue()+"-"+vendorProdRef.getName()+"\n";
			}
		}
		//Si el producto existe se asocia el producto a la linea de referencia en la matrix de referencias
		if (productID > 0 ) {
			doInsert(lineRefProv, productID);
			messageAso += vendorProdRef.getValue()+"-"+vendorProdRef.getName()+"\n";
					
		//Si el producto esta duplicado o no se pudo crear se agrega la referencia al mensaje de no asociados
		}else {
			messageNo += vendorProdRef.getValue()+"-"+vendorProdRef.getName()+"\n";
		}
	}   //  cmd_associate


	/** Crea un producto dado una referencia sin caracteristicas
	 * 
	 * @param lineRefProv
	 * @param vendorProdRef
	 * @return id del nuevo producto
	 */
	private int createNewProduct(X_XX_VMR_PO_LineRefProv lineRefProv,MVMRVendorProdRef vendorProdRef) {
		int productID = 0;
		try {
			//Selecciono el departamento
			X_XX_VMR_Department depart = new X_XX_VMR_Department(getCtx(),vendorProdRef.getXX_VMR_Department_ID(),get_Trx());				    
			int category = depart.getXX_VMR_Category_ID();
			
			//Selecciono la línea
			X_XX_VMR_Line line = new X_XX_VMR_Line(getCtx(), vendorProdRef.getXX_VMR_Line_ID(), get_Trx());
			int typeInventory = line.getXX_VMR_TypeInventory_ID();
				   		
			MProduct newProduct = new MProduct(getCtx(), vendorProdRef.getXX_VMR_Department_ID(), 
				    vendorProdRef.getXX_VMR_Line_ID(), vendorProdRef.getXX_VMR_Section_ID(), vendorProdRef.get_ID(),
				    vendorProdRef.getC_TaxCategory_ID(),vendorProdRef.getXX_VME_ConceptValue_ID(),typeInventory, get_Trx());
				   
			// Se buscará si por la referencia para producto ya existe para asignarle el Tipo de Exhibición
			String sql = "select * from M_Product where XX_VMR_DEPARTMENT_ID = "+vendorProdRef.getXX_VMR_Department_ID()+" and " +
				  "XX_VMR_LINE_ID = "+vendorProdRef.getXX_VMR_Line_ID()+" and XX_VMR_SECTION_ID = "+vendorProdRef.getXX_VMR_Section_ID()+" and " +
				  "XX_VMR_VendorProdRef_id = "+vendorProdRef.getXX_VMR_VendorProdRef_ID()+" order by M_Product_ID desc";
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = null;
			try {
				rs = pstmt.executeQuery();
				if (rs.next()) newProduct.setXX_VMR_TypeExhibition_ID(rs.getInt("XX_VMR_TypeExhibition_ID"));	 
			} catch (SQLException e) {
				
				e.printStackTrace();
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
		    if(vendorProdRef.getXX_VMR_Section_ID() >0){
		    	X_XX_VMR_Section section = new X_XX_VMR_Section(Env.getCtx(), vendorProdRef.getXX_VMR_Section_ID(), null);
		    	newProduct.setName(section.getName());
			 }else {
				 newProduct.setName(vendorProdRef.getName());
			 }
		    
			newProduct.setXX_VMR_Category_ID(category);
			newProduct.setXX_VMR_LongCharacteristic_ID(vendorProdRef.getXX_VMR_LongCharacteristic_ID());
			newProduct.setXX_VMR_Brand_ID(vendorProdRef.getXX_VMR_Brand_ID());				    
			newProduct.setXX_VMR_UnitConversion_ID(vendorProdRef.getXX_VMR_UnitConversion_ID());
			newProduct.setXX_PiecesBySale_ID(vendorProdRef.getXX_PiecesBySale_ID());
			newProduct.setXX_VMR_UnitPurchase_ID(vendorProdRef.getXX_VMR_UnitPurchase_ID());
			newProduct.setXX_SaleUnit_ID(vendorProdRef.getXX_SaleUnit_ID());
			newProduct.setC_Country_ID(order.getC_Country_ID());
			newProduct.setIsActive(true);		
			newProduct.setC_BPartner_ID(vendorProdRef.getC_BPartner_ID());
			newProduct.setXX_VMR_TypeLabel_ID(vendorProdRef.getXX_VMR_TypeLabel_ID());
			newProduct.setXX_VMR_ProductClass_ID(vendorProdRef.getXX_VMR_ProductClass_ID());
		    newProduct.setM_AttributeSet_ID( Env.getCtx().getContextAsInt("#XX_L_P_ATTRIBUTESETST_ID"));
		    newProduct.setProductType(X_Ref_M_Product_ProductType.ITEM.getValue());
			newProduct.save();

			get_Trx().commit();
			productID=newProduct.get_ID();
			
			msgNewProd +=  newProduct.getValue()+"-"+ newProduct.getName()+"\n";
		}catch (Exception e){
			return -1;
		}
	
		return productID;
	}

	/** Indica el identificador de un producto dado una referencia, 
	 * si no existe devuelve 0, si existe más de uno devuelve -1
	 * */
	private int getProductToAssociate(int vendorProdRefID) {
		
		int productID = 0;
		String sql = "\nSELECT M_PRODUCT_ID"+
				"\nFROM M_PRODUCT"+
				"\nWHERE XX_VMR_VENDORPRODREF_ID="+vendorProdRefID+
				"\nAND ISACTIVE='Y'" +
				"\nAND M_ATTRIBUTESET_ID = " + Env.getCtx().getContextAsInt("#XX_L_P_ATTRIBUTESETST_ID");
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			boolean exists = false;
			while(rs.next()){
				if(!exists){
					productID = rs.getInt(1);
					exists = true;
				}else {
					return -1;
				}
			}

							
		}
		catch (SQLException e){
			log.log(Level.SEVERE, sql, e);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return productID;
	}
	
	/**
	 *  Hace el insert en la tabla REFERENCEMATRIX dada una linea de referencia y un producto
	 */
	public void doInsert(X_XX_VMR_PO_LineRefProv lineRefProv, int productID){
		
		//Asocio producto
		X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(Env.getCtx(),0,null);
		
		matrix.setXX_VALUE1(0);
		matrix.setXX_VALUE2(0);
		matrix.setXX_COLUMN(0);
		matrix.setXX_ROW(0);
		matrix.setXX_QUANTITYC(lineRefProv.getQty());
		matrix.setXX_QUANTITYV(lineRefProv.getSaleQty());
		matrix.setXX_QUANTITYO(lineRefProv.getXX_GiftsQty());
		matrix.setXX_VMR_PO_LineRefProv_ID(lineRefProv.get_ID());
		matrix.setM_Product(productID);
		
		matrix.save();

	}

	/**Se completa información de la O/C: Monto tesorería Nacional y  Monto Tasa Seniat 
	 * (se copió código de clase XX_AssociateReferenceWith_Form del método dispose())
	 * 
	 * @param order
	 */
	private void completeInfo(MOrder order) {
		
		try{
		    int orderID = order.getC_Order_ID();
		    BigDecimal cifTotal = new BigDecimal(0);
		    Integer maritimo = 0;
		    Integer aereo = 0;
		    Integer terrestre = 0;
		    BigDecimal montoBs = new BigDecimal(0);
		    BigDecimal rate = new BigDecimal(0);
		    BigDecimal Seguro = new BigDecimal(0);
		    BigDecimal perfleteInt = new BigDecimal(0);  
		    BigDecimal fleteInternac = new BigDecimal(0);
		    
		    Integer productid = 0;
			Integer poline = 0;
			BigDecimal costoitem = new BigDecimal(0);
			BigDecimal porcentitem = new BigDecimal(0);
			BigDecimal cifuni = new BigDecimal(0);
		    BigDecimal percentarancel = new BigDecimal(0);
			BigDecimal arancelitem = new BigDecimal(0);
			BigDecimal rateseniat = new BigDecimal(0);
			BigDecimal montoseniat = new BigDecimal(0);
			BigDecimal ratetesoreria = new BigDecimal(0);
			BigDecimal montotesoreria = new BigDecimal(0);
			BigDecimal tasaaduana = new BigDecimal(0);
			BigDecimal part1 = new BigDecimal(0);
			BigDecimal part2 = new BigDecimal(0);
			BigDecimal iva = new BigDecimal(0);
			BigDecimal part = new BigDecimal(0);
			BigDecimal montesorerianac = new BigDecimal(0);
  	
		    String SQL2 = ("SELECT (A.TOTALLINES * B.MULTIPLYRATE) + A.XX_INTNACESTMEDAMOUNT + A.XX_ESTEEMEDINSURANCEAMOUNT  AS CIFTOTAL " +
					"FROM C_ORDER A, C_CONVERSION_RATE B " +
					"WHERE B.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
					"AND A.C_ORDER_ID = '"+orderID+"' " +
					"AND A.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+") " +
					"AND B.AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
				
			PreparedStatement pstmt2 = DB.prepareStatement(SQL2, null); 
			ResultSet rs2 = pstmt2.executeQuery();
			if (rs2.next()){
				 // Calculo del CIF Total	 
				   cifTotal = rs2.getBigDecimal("CIFTOTAL");
			}
			rs2.close();
			pstmt2.close();
			
				 
			// Busco la via de despacho de la O/C
			String SQL3 = ("SELECT XX_L_DISPATCHROUTE AS AEREO, XX_L_DISPATCHROUTEMAR AS MARITIMO, XX_L_DISPATCHROUTETER AS TERRESTRE " +
				 "FROM XX_VSI_KEYNAMEINFO ");
				 
			PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null); 
			ResultSet rs3 = pstmt3.executeQuery();
				 
			if(rs3.next()){
				maritimo = rs3.getInt("MARITIMO");
				terrestre = rs3.getInt("TERRESTRE");
				aereo = rs3.getInt("AEREO");
			}
			rs3.close();
			pstmt3.close();
				  
			String SQL4 = ("SELECT (A.TOTALLINES * B.MULTIPLYRATE) AS MONTO " +
					 		"FROM C_ORDER A, C_CONVERSION_RATE B " +
					 		"WHERE B.C_CONVERSION_RATE_ID = A.XX_CONVERSIONRATE_ID " +
					 		"AND A.C_ORDER_ID = '"+orderID+"' ");
					 
			PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null); 
			ResultSet rs4 = pstmt4.executeQuery();
				 
			if(rs4.next()){
				// Monto de la O/C en Bs
				montoBs = rs4.getBigDecimal("MONTO");	 					 
			}
			rs4.close();
			pstmt4.close();
				 
			if(order.getXX_VLO_DispatchRoute_ID()== maritimo){
					 
				String SQL5 = ("SELECT XX_RATE AS RATE " +
					 	"FROM XX_VLO_DispatchRoute " +
					 	"WHERE XX_VLO_DispatchRoute_ID = '"+maritimo+"' ");
					 
				PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
				ResultSet rs5 = pstmt5.executeQuery();
					 
				if(rs5.next()){
					rate = rs5.getBigDecimal("RATE").divide(new BigDecimal(100));
					// Calculo del Seguro con Via Maritimo 
					Seguro = montoBs.multiply(rate);
				}
				rs5.close();
				pstmt5.close();
				
			}else if(order.getXX_VLO_DispatchRoute_ID()== aereo){
				String SQL5 = ("SELECT XX_RATE AS RATE " +
						 		"FROM XX_VLO_DispatchRoute " +
						 		"WHERE XX_VLO_DispatchRoute_ID = '"+aereo+"' ");
				PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
				ResultSet rs5 = pstmt5.executeQuery();
						 
				if(rs5.next()){
					rate = rs5.getBigDecimal("RATE").divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					// Calculo del Seguro con Via aereo 
					Seguro = montoBs.multiply(rate);
				}
				rs5.close();
				pstmt5.close();
			}else if(order.getXX_VLO_DispatchRoute_ID()== terrestre){
				String SQL5 = ("SELECT XX_RATE AS RATE " +
								"FROM XX_VLO_DispatchRoute " +
								"WHERE XX_VLO_DispatchRoute_ID = '"+terrestre+"' ");
						 
				PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
				ResultSet rs5 = pstmt5.executeQuery();
						 
				if(rs5.next()){
					rate = rs5.getBigDecimal("RATE").divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					// Calculo del Seguro con Via terrestre 
					Seguro = montoBs.multiply(rate);
				}
				rs5.close();
				pstmt5.close();
			}
			
			// Busco el porcentaje del flete internacional segun el proveedor, puerto y pais de la O/C
				 
			String SQL6 = ("SELECT DISTINCT A.XX_INTERFREESTIMATEPERT AS PERTFLEINTER " +
				 		"FROM XX_VLO_COSTSPERCENT A, C_ORDER B " +
				 		"WHERE B.XX_VLO_ARRIVALPORT_ID = '"+order.getXX_VLO_ArrivalPort_ID()+"' " +
				 		"AND B.C_BPARTNER_ID = '"+order.getC_BPartner_ID()+"' " +
				 		"AND B.C_COUNTRY_ID = '"+order.getC_Country_ID()+"' " +
				 		"AND B.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				 		"AND B.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				 		"AND B.C_BPARTNER_ID = A.C_BPARTNER_ID " +
				 		"AND B.C_COUNTRY_ID = A.C_COUNTRY_ID ");
				  
				 
			PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
			ResultSet rs6 = pstmt6.executeQuery();
				 
			if(rs6.next()) {
				// Porcentaje del flete internacional
				perfleteInt = rs6.getBigDecimal("PERTFLEINTER").divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
				//Flete Internacional 
				fleteInternac = montoBs.multiply(perfleteInt);
			}
			rs6.close();
			pstmt6.close();
				 
			// Busco los productos que tienen asociados las Ref de la O/C
				
			String SQL7 = ("SELECT C.M_PRODUCT AS PRODUCT, A.XX_VMR_PO_LINEREFPROV_ID AS POLINE " +
						"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B, XX_VMR_REFERENCEMATRIX C " +
						"WHERE A.XX_VMR_PO_LINEREFPROV_ID = C.XX_VMR_PO_LINEREFPROV_ID " +
						"AND A.C_ORDER_ID = B.C_ORDER_ID " +
						"AND A.C_ORDER_ID = '"+orderID+"' ");
				 
			PreparedStatement pstmt7 = DB.prepareStatement(SQL7, null); 
			ResultSet rs7 = pstmt7.executeQuery(); 
				
			while(rs7.next()){
				// Calculo del Porcentaje de cada item = costo de cada item Bs / Monto O/C Bs
				productid = rs7.getInt("PRODUCT");
				poline = rs7.getInt("POLINE");

				// Costo de cada item Bs
				String SQL8 = ("SELECT (A.LINENETAMT * E.MULTIPLYRATE) AS COSTO " +
							"FROM XX_VMR_PO_LINEREFPROV A, XX_VMR_REFERENCEMATRIX C, M_PRODUCT D, C_CONVERSION_RATE E, C_ORDER F  " +
							"WHERE A.XX_VMR_PO_LINEREFPROV_ID = C.XX_VMR_PO_LINEREFPROV_ID " +
							"AND C.M_PRODUCT = D.M_PRODUCT_ID " +
							"AND E.C_CONVERSION_RATE_ID = F.XX_CONVERSIONRATE_ID " +
							"AND A.C_ORDER_ID = F.C_ORDER_ID " +
							"AND C.M_PRODUCT = '"+productid+"' " +
							"AND A.XX_VMR_PO_LINEREFPROV_ID = '"+poline+"' ");
					
					
				PreparedStatement pstmt8 = DB.prepareStatement(SQL8, null); 
				ResultSet rs8 = pstmt8.executeQuery(); 
						
				if(rs8.next()){
					costoitem = rs8.getBigDecimal("COSTO");
					
					//Porcentaje de cada item = costo de cada item Bs / Monto O/C Bs
					porcentitem = (costoitem.divide(montoBs, 8, RoundingMode.HALF_UP));
					
					//CIF Unitario = Porcentaje de cada Item * CIF total
					cifuni = porcentitem.multiply(cifTotal);
					
					// Busco Porcentaje Arancelario
					String SQL9 = ("SELECT (D.XX_PERCENTAGETARIFF/100) AS PERARANCEL " +
								"FROM XX_VMR_PO_LINEREFPROV A, C_ORDER B, XX_VMR_REFERENCEMATRIX C, M_PRODUCT D " +
								"WHERE A.XX_VMR_PO_LINEREFPROV_ID = C.XX_VMR_PO_LINEREFPROV_ID " +
								"AND A.C_ORDER_ID = B.C_ORDER_ID " +
								"AND C.M_PRODUCT = D.M_PRODUCT_ID " +
								"AND D.M_PRODUCT_ID = '"+productid+"' " +
								"AND A.C_ORDER_ID = '"+orderID+"' ");
						
					PreparedStatement pstmt9 = DB.prepareStatement(SQL9, null); 
					ResultSet rs9 = pstmt9.executeQuery();

					if(rs9.next()){
						// Porcentaje Arancelario
						percentarancel = rs9.getBigDecimal("PERARANCEL");
						
						//Arancel de cada item = CIF unitario * Porcentaje Arancelario
						//YA CON LA SUMATORIA ES DECIR CALCULO DE UNA VEZ EL IMPUESTO DE IMPORTACION CON TODOS LOS PRODUCTOS
						arancelitem = cifuni.multiply(percentarancel).add(arancelitem);
					}
					rs9.close();
					pstmt9.close();
				}
				rs8.close();
				pstmt8.close();
			}// end While rs7
			rs7.close();
			pstmt7.close();
			
			String SQL9 = ("SELECT XX_RATE/100 AS RATESENIAT FROM XX_VLO_IMPORTRATE WHERE NAME = 'Tasa de Servicio de aduanas SENIAT' " +
				 		"And AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
				    
			PreparedStatement pstmt9 = DB.prepareStatement(SQL9, null); 
			ResultSet rs9 = pstmt9.executeQuery();
				    
			if (rs9.next()){
				rateseniat = rs9.getBigDecimal("RATESENIAT");
				
				//Monto tasa  seniat= impuesto de importación * tasa seniat (0,5%)
				montoseniat = arancelitem.multiply(rateseniat);

				String SQL10 = ("SELECT XX_RATE/100 AS RATETESORERIA FROM XX_VLO_IMPORTRATE WHERE NAME = 'Tasa de Servicio Aduana Tesorería' " +
					 		"And AD_Client_ID IN(0,"+Env.getCtx().getAD_Client_ID()+")");
					    
				PreparedStatement pstmt10 = DB.prepareStatement(SQL10, null); 
				ResultSet rs10 = pstmt10.executeQuery();
						    
				if (rs10.next()) {
					ratetesoreria = rs10.getBigDecimal("RATETESORERIA");
					
					//Monto tasa tesoreria = impuesto de importación * tasa tesoreria (0,5%)
					montotesoreria = arancelitem.multiply(ratetesoreria);
					  	
					//Monto Tasa aduanera =monto tasa seniat + monto tasa tesoreria
					tasaaduana = montoseniat.add(montotesoreria);
				}
				rs10.close();
				pstmt10.close();
			}
			rs9.close();
			pstmt9.close();
	
			//Calculo del IVA = (CIF total + Impuesto de importación + tasa aduanera) *%iva actual
			String SQL11 = ("SELECT MAX (VALIDFROM) AS FECHA, A.RATE/100 AS IVACT " +
				 		"FROM C_TAX A, C_TAXCATEGORY B " +
				 		"WHERE A.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID " +
				 		"AND B.DESCRIPTION = 'Impuesto al Valor Agregado' " +
				 		"AND A.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID " +
				 		"GROUP BY A.RATE ");
				 
			PreparedStatement pstmt11 = DB.prepareStatement(SQL11, null); 
			ResultSet rs11 = pstmt11.executeQuery();
				 
			if(rs11.next()){
				part1 = cifTotal.add(arancelitem); 
				part2 = part1.add(tasaaduana);
				iva = part2.multiply(rs11.getBigDecimal("IVACT"));
			}
			rs11.close();
			pstmt11.close();
				 
			//Monto tesorería Nacional = IVA + impuesto de importación + monto tasa tesoreria
			part = iva.add(arancelitem);
			montesorerianac = part.add(montotesoreria);
			
			//redondeo los BigDecimal
			montesorerianac = montesorerianac.setScale(2,BigDecimal.ROUND_UP);
			montoseniat = montoseniat.setScale(2,BigDecimal.ROUND_UP);
			String sql = "UPDATE C_Order po"											
						+ " SET XX_NatTreasuryEstAmount=" + montesorerianac
						+ "    , XX_SENIATESTEEMEDAMUNT=" + montoseniat
						+ " WHERE po.C_Order_ID=" + orderID;						
			DB.executeUpdate(null, sql);
		} // end try
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void callProcess(int ProcessID, int recordID){
		MPInstance mpi = new MPInstance( Env.getCtx(), ProcessID, recordID); 
		mpi.save();
		ProcessInfo pi = new ProcessInfo("", ProcessID); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(ProcessID); 
		pi.setClassName(""); 
		pi.setTitle(""); 
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
	}//

		/** Envía correo a Comprador de la O/C SITME y a los jefes de categoría indicando que se genero una O/C Nacional correspondiente
		 * y que no fue posible la asociación automática de todas las referencias. */
		private void sendMailSitmeToNational(int orderID, int orderSITME_ID) {
			
			try {
				MOrder order = new MOrder(getCtx(), orderID, null);
				MOrder orderSITME = new MOrder(getCtx(), orderSITME_ID, null);
				
				int buyerID = getAD_User_ID(order.getXX_UserBuyer_ID());
				X_AD_User buyer = new X_AD_User( Env.getCtx(), buyerID, null);
				
				MVMRDepartment dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
				MVMRCategory catg = new MVMRCategory(getCtx(),dep.getXX_VMR_Category_ID(), null);
				
				int catgManagerID = getAD_User_ID(catg.getXX_CategoryManager_ID());
				X_AD_User catgManager = new X_AD_User( Env.getCtx(), catgManagerID, null);
				
				String emailTo = buyer.getEMail();
				String emailTo2 = catgManager.getEMail();
				if(emailTo.contains("@")){
					MClient m_client = MClient.get(Env.getCtx());
					String subject = " Generada O/C Nacional Nro "+order.getValue();
					MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
					MBPartner vendorSITME = new MBPartner(getCtx(), orderSITME.getC_BPartner_ID(), null);
					String msg = "Se ha generado la O/C Nacional -PROFORMA- Nro "+order.getDocumentNo()+" del Proveedor "+vendor.getValue()+"-"+vendor.getName()+
							" que sustituye a la O/C Importada "+orderSITME.getDocumentNo()+" del Proveedor "+vendorSITME.getValue()+"-"+vendorSITME.getName();
					msg +="\n\n No fue posible realizar la asociación automática de todas las referencias." +
							"\n\nFavor proceder a realizar la asociación de las referencias faltantes.";
		
					EMail email = m_client.createEMail(null, emailTo, "Comprador", subject, msg);
							
					if (email != null){		
						if(emailTo2.contains("@")){
							email.addTo(emailTo2, "Jefe de Categoría");
						}
						String status = email.send();
						log.info("Email Send status: " + status);
						if (email.isSentOK()){}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}// FIN GHUCHET
	
		private Integer getAD_User_ID(Integer CBPartner)
		{
			Integer AD_User_ID=0;
			
			String SQL = "Select AD_USER_ID FROM AD_USER " +
						 "WHERE C_BPartner_ID IN "+CBPartner;
			
			PreparedStatement pstmt = null; 
			ResultSet rs = null;
			try{
				
				pstmt = DB.prepareStatement(SQL, null); 
				rs = pstmt.executeQuery();
					 
				if(rs.next())
				{
					AD_User_ID = rs.getInt("AD_USER_ID");
				}
				   
				}catch (Exception e) {
					log.log(Level.SEVERE, SQL);
				} finally
				{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
			
			return AD_User_ID;
		}

}
