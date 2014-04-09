package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.X_M_InOut;
import org.compiere.model.X_M_InOutLine;
import org.compiere.model.X_Ref_Quantity_Type;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


/**
 *  Modelo extendido de InOut (Rec.)
 *
 *  @author     José Trías, Wilker Jóse Díaz
 *  @version    
 */
public class MInOut extends org.compiere.model.MInOut {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInOut.class);

	public MInOut(Ctx ctx, int M_InOut_ID, Trx trx) {
		super(ctx, M_InOut_ID, trx);
	}
	
	public MInOut(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	protected boolean beforeSave (boolean newRecord, boolean success){	

		// Verifica que no exista otra recepcion apuntando a la misma orden
		String SQL = "SELECT * FROM M_INOUT WHERE IssoTrx = 'N' and C_ORDER_ID=" + getC_Order_ID();

		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				rs.close();
				pstmt.close();
				log.saveError(null, Msg.getMsg(Env.getCtx(), "Ya existe una recepción con esta orden de compra asociada"));
				return false;
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
		} 
		return true;
		
	}
	

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		
		boolean save = super.afterSave(newRecord, success);
		
		if(save){
			
			MOrder order = new MOrder( getCtx(), getC_Order_ID(), null);
		    
		    //Si la O/C es de Desp Directo
			if(newRecord){
				//COMENTADO POR GHUCHET
//			    if(order.getXX_VLO_TypeDelivery().equals("DD")){
//			    	
//			    	int days=0;
//					//verifico si el prov tiene los beneficio
//					days = daysBenefits(order);
//					
//			    	Calendar dateFrom = Calendar.getInstance();
//					dateFrom.setTimeInMillis(order.getXX_EstimatedDate().getTime());
//					dateFrom.add(Calendar.DATE, -(days));
//					
//					Calendar dateUntil = Calendar.getInstance();			
//					dateUntil.setTimeInMillis(order.getXX_EstimatedDate().getTime());
//					dateUntil.add(Calendar.DATE, (days));
//					
//					Calendar today = Calendar.getInstance();
//					today.set(Calendar.AM_PM, 0);
//					today.set(Calendar.HOUR, 0);
//					today.set(Calendar.HOUR_OF_DAY, 0);
//					today.set(Calendar.MINUTE, 0);
//					today.set(Calendar.SECOND, 0);
//					today.set(Calendar.MILLISECOND, 0);
//					
//					if(dateFrom.getTimeInMillis()<=today.getTimeInMillis() && dateUntil.getTimeInMillis()>=today.getTimeInMillis()){
//						//Nada
//					}else{
//					
//						//Coloco una alerta indicando discrepancias de fechas
//						setXX_Alert("La recepción no se pudo realizar por discrepancias en fechas");
//						setDocStatus(X_M_InOut.DOCSTATUS_Invalid);
//						save();
//						
//					    /**
//						 * Jorge Pires / Evaluacion de la O/C
//						 **/
//						Utilities aux = new Utilities();
//						aux.ejecutarWeigth(order.get_ID(),0);
//						order.setXX_Evaluated(true);
//						order.save();
//						aux=null;
//					}			
//			    }
			    
			    if(order.getXX_OrderType().equalsIgnoreCase("Importada")){
				    order.setXX_StatusWhenReceipt(order.getXX_OrderStatus());
				    order.save();
			    }
			    
			}
			else{ // si no es nuevo registro actualiza el status para indicar que hay q sincronizar el registro
				
				if (isXX_Status_Sinc())
				{
					setXX_Status_Sinc(false);
					save();
				}			
			}
		}
		return save;
	}
	
	/**
	 * BeforeSave
	 * @author Jessica Mendoza
	 */
	protected boolean beforeSave(boolean newRecord){
		if (super.beforeSave(newRecord)){
			MOrder order = new MOrder(Env.getCtx(),getC_Order_ID(),null);
			MUser user = new MUser(Env.getCtx(),Env.getCtx().getAD_User_ID(),null);
			if (order.getXX_POType().equals("POA")){
				setXX_CheckAssistant_ID(user.getC_BPartner_ID());
				setXX_CheckAuxiliary_ID(user.getC_BPartner_ID());			
			}
			
			if(Env.getCtx().getAD_Role_ID()==Env.getCtx().getContextAsInt("#XX_L_ROLEWAREHOUSEASSISTANT_ID"))
				set_Value("XX_POType", "POM");
		}
		return true;
	}
	
	@Override
	protected boolean beforeDelete()
	{
		//Selecciono la cabecera de la devolucion (de haberla)
		String SQL = "SELECT XX_VLO_RETURNOFPRODUCT_ID " +
					 "FROM XX_VLO_RETURNOFPRODUCT " +
					 "WHERE C_ORDER_ID="+getC_Order_ID()+" "+
					 "AND AD_CLIENT_ID IN (0,"+getCtx().getAD_Client_ID()+")";
		
		MVLOReturnOfProduct returnOfProduct = null ;
		boolean rop=false;
		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				returnOfProduct = new MVLOReturnOfProduct( getCtx(), rs.getInt("XX_VLO_RETURNOFPRODUCT_ID"), null);
				rop=true;
			} 
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
		}
		
		//Borro los detalles de la devolucion (de haberlos)
		if(rop){
			SQL = "DELETE FROM XX_VLO_RETURNDETAIL " +
				  "WHERE XX_VLO_RETURNOFPRODUCT_ID="+returnOfProduct.get_ID()+" "+
				  "AND AD_CLIENT_ID IN (0,"+getCtx().getAD_Client_ID()+")";
	
			try {
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				pstmt.executeQuery();
				pstmt.close();
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL, e);
			}
			
			//Borro la cabecera
			returnOfProduct.delete(true);
		}
		
		//Borro los porductos no solicitados (de haberlos)
		SQL = "SELECT M_InOutLine_ID " +
			  "FROM M_InOutLine " +
			  "WHERE M_InOut_ID="+get_ID()+" "+
			  "AND C_OrderLine_ID IS NULL " +
			  "AND AD_CLIENT_ID IN (0,"+getCtx().getAD_Client_ID()+")";

		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				MInOutLine inOutLine = new MInOutLine( getCtx(), rs.getInt("M_InOutLine_ID"), null);
				inOutLine.delete(true);
			} 
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
		}

		return true;
	}
	
	@Override
	protected boolean afterDelete(boolean success)
	{
		MOrder order = new MOrder( getCtx(), getC_Order_ID(), null);
		
		if(order.getXX_OrderType().equalsIgnoreCase("Nacional")){
			order.setXX_OrderStatus("AP");
		}
		
		order.save();

		return true;
	}
	
	/*
	 *	returns true si el proveedor tiene el beneficio de N dias
	 */
	private int daysBenefits(MOrder order){
				
		//Tengo que buscar en la matriz por su  Tipo de prov y su Rating
		MBPartner cBPartner = new  MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		Utilities ut = new Utilities();
		return ut.benefitVendorDiasFechaEntrega(cBPartner.get_ID());
		
	}
	
	
    /** Set Status.
    @param Status of the Purchase Order */
    public void setXX_InOutStatus (String Status)
    {
        set_Value ("XX_InOutStatus", Status);
        
    }
    
    /** Get Status.
    @return OrderStatus of the Purchase Order */
    public String getXX_InOutStatus() 
    {
        return get_ValueAsString("XX_InOutStatus");
        
    }
	
	/*
	 * Set de XX_ScannedPackages
	 */
    public void setXX_ScannedPackages(int XX_ScannedPackages)
    {
        set_Value ("XX_ScannedPackages", Integer.valueOf(XX_ScannedPackages));   	
    }

    /*
	 * Get de XX_ScannedPackages
	 */
    public int getXX_ScannedPackages() 
    {
        return get_ValueAsInt("XX_ScannedPackages");      
    } 
    
    /*
	 * Set de XX_PackageIdentification
	 */
    public void setXX_PackageIdentification(boolean value)
    {
        set_ValueNoCheck ("XX_PackageIdentification", Boolean.valueOf(value));
        
    }
    
    public boolean isXX_PackageIdentification()
    {
        return get_ValueAsBoolean("XX_PackageIdentification");
        
    }
    
    
    /*
	 * Set de XX_WithNoCheckBenfit
	 */
    public void setXX_WithNoCheckBenefit(boolean value)
    {
        set_ValueNoCheck ("XX_WithNoCheckBenefit", Boolean.valueOf(value));
        
    }
    
    public boolean isXX_WithNoCheckBenefit()
    {
        return get_ValueAsBoolean("XX_WithNoCheckBenefit");
        
    }
    
    /** Set Complete Reception.
    @param XX_CompleteReception */
    public void setXX_CompleteReception (String XX_CompleteReception)
    {
        set_Value ("XX_CompleteReception", XX_CompleteReception);
        
    }
    
    /** Get Complete Reception.
    @return */
    public String getXX_CompleteReception() 
    {
        return (String)get_Value("XX_CompleteReception");
        
    }
    
    
    /** Set Alert.
    @param XX_Alert */
    public void setXX_Alert (String XX_Alert)
    {
        set_Value ("XX_Alert", XX_Alert);
        
    }
    
    /** Get Alert.
    @return */
    public String getXX_Alert() 
    {
        return (String)get_Value("XX_Alert");
        
    }
    
    /** Print Checkup Doc.
    @param XX_CompleteReception */
    public void setXX_PrintCheckup (String XX_PrintCheckup)
    {
        set_Value ("XX_PrintCheckup", XX_PrintCheckup);
        
    }
    
    /** Get Print Checkup Doc.
    @return */
    public String getXX_PrintCheckup() 
    {
        return (String)get_Value("XX_PrintCheckup");
        
    }
    
    /** Print Definitive Doc.
    @param XX_PrintDefinitive */
    public void setXX_PrintDefinitive (String value)
    {
        set_Value ("XX_PrintDefinitive", value);
        
    }
    
    /** Get Print Defive Doc.
    @return */
    public String getXX_PrintDefinitive() 
    {
        return (String)get_Value("XX_PrintDefinitive");
        
    }
    
    /** Set Complete Checkup.
    @param XX_CompleteCheckup */
    public void setXX_CompleteCheckup (String value)
    {
        set_Value ("XX_CompleteCheckup", value);
        
    }
    
    /** Get Complete Checkup.
    @return */
    public String getXX_CompleteCheckup() 
    {
        return (String)get_Value("XX_CompleteCheckup");
        
    }
    
    /** Set XX_ScanPackages.
    @param XX_ScanPackages */
    public void setXX_ScanPackages (String value)
    {
        set_Value ("XX_ScanPackages", value);
        
    }
    
    /** Get XX_ScanPackages.
    @return */
    public String getXX_ScanPackages() 
    {
        return (String)get_Value("XX_ScanPackages");
        
    }
    
    public void setXX_CheckAssistant_ID(int id)
    {
        if (id <= 0) set_Value ("XX_CheckAssistant_ID", null);
        else
        set_Value ("XX_CheckAssistant_ID", Integer.valueOf(id));   	
    }
    
    public int getXX_CheckAssistant_ID() 
    {
        return get_ValueAsInt("XX_CheckAssistant_ID");      
    }
    
    public void setXX_CheckAuxiliary_ID(int id)
    {
        if (id <= 0) set_Value ("XX_CheckAuxiliary_ID", null);
        else
        set_Value ("XX_CheckAuxiliary_ID", Integer.valueOf(id));   	
    }
    
    public int getXX_CheckAuxiliary_ID() 
    {
        return get_ValueAsInt("XX_CheckAuxiliary_ID");      
    }
    
    /**
     * XX_VMR_Department_ID
     * @param id
     */
    public void setXX_VMR_Department_ID(int id)
    {
        if (id <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(id));   	
    }
    
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");      
    }
    
    /**
     * XX_VMR_Category_ID
     * @param id
     */
    public void setXX_VMR_Category_ID(int id)
    {
        if (id <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(id));   	
    }
    
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");      
    }
    
    /**
     * XX_VMR_Season_ID
     * @param id
     */
    public void setXX_VMR_Season_ID(int id)
    {
        if (id <= 0) set_Value ("XX_VMR_Season_ID", null);
        else
        set_Value ("XX_VMR_Season_ID", Integer.valueOf(id));   	
    }
    
    public int getXX_VMR_Season_ID() 
    {
        return get_ValueAsInt("XX_VMR_Season_ID");      
    }
    
    /**
     * XX_VMR_Collection_ID
     * @param id
     */
    public void setXX_VMR_Collection_ID(int id)
    {
        if (id <= 0) set_Value ("XX_VMR_Collection_ID", null);
        else
        set_Value ("XX_VMR_Collection_ID", Integer.valueOf(id));   	
    }
    
    public int getXX_VMR_Collection_ID() 
    {
        return get_ValueAsInt("XX_VMR_Collection_ID");      
    }
    
    /**
     * XX_PrintedLabels
     * @param id
     */
    public void setXX_PrintedLabels(int val)
    {
        set_Value ("XX_PrintedLabels", Integer.valueOf(val));   	
    }
    
    public int getXX_PrintedLabels() 
    {
        return get_ValueAsInt("XX_PrintedLabels");      
    }
    
    /**
     * XX_VMR_Package_ID
     * @param id
     */
    public void setXX_VMR_Package_ID(int id)
    {
        if (id <= 0) set_Value ("XX_VMR_Package_ID", null);
        else
        set_Value ("XX_VMR_Package_ID", Integer.valueOf(id));   	
    }
    
    public int getXX_VMR_Package_ID() 
    {
        return get_ValueAsInt("XX_VMR_Package_ID");      
    }
    
    
    /*
	 * Status Sincronizacion al AS de Material Receipt
	 */
	public void setXX_Status_Sinc (boolean XX_Status_Sinc)
	{
		set_Value ("XX_Status_Sinc", XX_Status_Sinc);
	        
	} 
	  
	public boolean isXX_Status_Sinc() 
	{
		
		return get_ValueAsBoolean("XX_Status_Sinc");    
	}
    
    
    

	@Override
	public String completeIt()
	{		
		String sql = null, sqlMax = null;		
		Integer producto = null;
		PreparedStatement Pstmt = null;
		ResultSet rs = null, rsMaximo = null;
		boolean error = false;
		X_M_InOutLine lineaRecepcion ;
		MAttributeSetInstance NuevoAtributo = null, atributoLotSigui = null, AtributoInv = null;
		MOrderLine LineaOrden= null;
		MVMRPOLineRefProv OrderLineRef = null;
		int Lote = 0;
		BigDecimal CostoRecep = null, PvpRecep = null, unitCompVent = null, unitSale = null, unitConv = null, tradeCost = null;
		int Package = 0; 
		String loteString = null;
		Trx Transaccion = Trx.get("Transaccion");

		//Agregado por Javier Pino -     					
		MOrder Order = new MOrder(getCtx(), this.getC_Order_ID(), null); 
		//Agregado por Javier Pino - 
		Package = Order.getXX_VMR_Package_ID();
		
		if (Order.getXX_POType().equals("POM")){
			
			//Creamos el prepared statment
			//Se busca el Product en el Inventario para saber si ya a sido recibido esté producto
			// Cambio en el sql por remplazó de la tabla de inventario M_Storage por M_StorageDetail
			
			//Modificado por GHUCHET - Caso de O/C de Despacho Directo se compara con fecha estimada de llegada y no con SYSDATE
			if(Order.getXX_VLO_TypeDelivery().equals(X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO.getValue())){
				
				sql = "\nSELECT M.M_AttributeSetInstance_ID AS ATRIBUTO "+
				"\nFROM M_StorageDetail M , M_AttributeSetInstance A, C_ORDER O "+
				"\nWHERE M.M_AttributeSetInstance_ID = A.M_AttributeSetInstance_ID  " +
				"\nAND O.C_ORDER_ID = "+Order.get_ID()+
				"\nAND M.M_AttributeSetInstance_ID>0  "+
				"\nAND M.M_lOCATOR_ID >0 " +
				"\nAND QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'"+
				"\nAND M.M_Product_ID = ? "+
				"\nAND TO_CHAR(A.CREATED, 'MMYYYY') = TO_CHAR(O.XX_EstimatedDate, 'MMYYYY')" +
				"\nGROUP BY M.M_AttributeSetInstance_ID "+
				"\nORDER BY M.M_AttributeSetInstance_ID DESC " ;
			}else {
				sql = " Select M.M_AttributeSetInstance_ID AS ATRIBUTO" +
				" From  M_StorageDetail M, M_INOUT I, M_INOUTLINE L " +
				" WHERE I.M_INOUT_ID = L.M_INOUT_ID " +
				" AND M.M_AttributeSetInstance_ID = L.M_AttributeSetInstance_ID " +				
				" AND M.M_PRODUCT_ID = L.M_PRODUCT_ID" +
				" AND M.M_AttributeSetInstance_ID>=0" +
				" AND M.M_lOCATOR_ID >= 0" +
				" AND QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'"+
				" AND M.M_Product_ID = ?  AND TO_CHAR(L.CREATED, 'MMYYYY') = TO_CHAR(SYSDATE, 'MMYYYY') " +
				" GROUP BY M.M_AttributeSetInstance_ID " +
				" ORDER BY M.M_AttributeSetInstance_ID DESC " ;	
			}
			
			Pstmt = DB.prepareStatement(sql, null);
			
			//Creamos el prepared statement para buscar el maxiMo producto
			//sqlMax = " Select max(M_AttributeSetInstance_ID) AS ATRIBUTO From M_StorageDetail WHERE M_Product_ID = ?  AND QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'";	
			sqlMax = "select max(M_AttributeSetInstance_ID) AS ATRIBUTO from  M_StorageDetail WHERE M_AttributeSetInstance_ID>=0 " +
					 "AND M_lOCATOR_ID >= 0 AND M_Product_ID = ?  AND QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'";
			
			PreparedStatement PstmtMaximo = DB.prepareStatement(sqlMax, null); 
	
			//Tomamos todos las lineas(producto) que posee la recepcion para asignarle el lote al que pertenece
			for (int i=0; i<super.getLines().length;i++)
			{			
				producto = super.getLines()[i].getM_Product_ID();
				unitConv = new BigDecimal(1);
				unitSale = new BigDecimal(1);
				unitCompVent = new BigDecimal(1);	
	
				lineaRecepcion = new X_M_InOutLine(super.getCtx(),super.getLines()[i].getM_InOutLine_ID() , Transaccion);
	
				if ((producto != 0)&&(super.getLines()[i].getM_AttributeSetInstance_ID() == 0)) {
					try{
						if (lineaRecepcion.getC_OrderLine_ID() > 0) {
							//Tomamos La Linea de la ORden de Compra por el codigo que posee la linea de la recepcion
							LineaOrden= new MOrderLine(getCtx(), lineaRecepcion.getC_OrderLine_ID(), null); 
							//Tomamos la referencia por la Linea de la orden para poder buscar costos
							OrderLineRef = new MVMRPOLineRefProv (getCtx(), LineaOrden.getXX_VMR_PO_LINEREFPROV_ID(), null);	
							unitConv = new BigDecimal(new X_XX_VMR_UnitConversion(getCtx(), OrderLineRef.getXX_VMR_UnitConversion_ID(), null).getXX_UnitConversion());
						    unitSale = new BigDecimal(new X_XX_VMR_UnitConversion(getCtx(), OrderLineRef.getXX_PiecesBySale_ID(), null).getXX_UnitConversion());
						    unitCompVent = unitConv.divide(unitSale);
							CostoRecep = OrderLineRef.getPriceActual().divide(unitCompVent, 4, BigDecimal.ROUND_HALF_UP);
							PvpRecep = OrderLineRef.getXX_SalePrice();
							tradeCost = OrderLineRef.getXX_TradeCost();
						}
						else{
							CostoRecep = new BigDecimal(0.01); 
							PvpRecep = new BigDecimal(0.01);
							tradeCost = new BigDecimal(0.01);
						}
	
						Pstmt.setInt(1, producto);
						rs = Pstmt.executeQuery();
						// Si existe se tienen que realizar algunos chequeos en la recepcion del mismo}
						boolean encontrado = false;
	
						while (rs.next()) {
							int AtributoSet = rs.getInt("atributo");
							AtributoInv = new MAttributeSetInstance(getCtx(), AtributoSet, null);
	
							//Ya tienen el mismo mes y año, verificar si es el mismo costo y paquete						
							if (CostoRecep.compareTo(
									AtributoInv.get_ValueAsBigDecimal("PriceActual")) == 0 ) {
	
								if ( Package == AtributoInv.get_ValueAsInt("XX_VMR_Package_ID") ) {
	
									//si el costo y fecha son iguales es decir no han cambiado tenemos que  
									//Se le asigna el mismo atributo a esta recepcion del producto
									lineaRecepcion.setM_AttributeSetInstance_ID(AtributoSet);
									lineaRecepcion.save();
	
									//Si se encontro el atributo asociado
									encontrado = true;
									break;
								}
							}
						}
	
						//Aca debemos considerar productos no solicitados y productos solicitados
						//A los que hay que crearle nuevos lotes
						if (!encontrado) {
							
							//Creo uno usando el viejo
							//Buscar el maximo
							try  {
								
								//System.out.println(sqlMax);
								//System.out.println(producto);
	
								PstmtMaximo.setInt(1, producto);
	
								rsMaximo = PstmtMaximo.executeQuery();
	
								if (rsMaximo.next()) 
	 								if (rsMaximo.getInt("ATRIBUTO") > 0) {
	
										atributoLotSigui = new MAttributeSetInstance(getCtx(), rsMaximo.getInt("ATRIBUTO"), null);
										//Opcional
										if (atributoLotSigui.getLot() == null || (atributoLotSigui.getLot().equals("")))
											Lote = 0;
										else
											//fin
											Lote = Integer.parseInt(atributoLotSigui.getLot());
										Lote = Lote + 1;
										loteString = String.valueOf(Lote);
										NuevoAtributo = new MAttributeSetInstance(getCtx(), 0, Transaccion);
										NuevoAtributo.setLot(loteString);
										NuevoAtributo.setDescription("<<"+loteString+">>");
										NuevoAtributo.setIsActive(true);
										NuevoAtributo.setM_AttributeSet_ID(super.getLines()[i].getProduct().getM_AttributeSet_ID());
										NuevoAtributo.set_Value("PriceActual", CostoRecep);
										NuevoAtributo.set_Value("XX_SalePrice", PvpRecep);
										if (Package > 0)
											NuevoAtributo.set_Value("XX_VMR_Package_ID", Package);		
										/*Agregado por GHUCHET para 0/C de Despacho Directo*/
										if(Order.getXX_VLO_TypeDelivery().equals(X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO.getValue())){
											NuevoAtributo.set_ValueNoCheck("Created", Order.getXX_EstimatedDate());
										}
										NuevoAtributo.set_Value("XX_TradeCost", tradeCost);
										/*Hasta aqui GHUCHET*/
										NuevoAtributo.save();
										lineaRecepcion.setM_AttributeSetInstance_ID(NuevoAtributo.getM_AttributeSetInstance_ID());
										lineaRecepcion.save();	
									
									} else {
		
										//Si no lo encuentra entonces creamos el AttributeSetInstance para el producto y 
										// lo seteamos en el campo de la linea. tambien seteamos el LOT
										//Creamos una Instancia del producto para que compiere asocie el producto con una instancia
										NuevoAtributo = new MAttributeSetInstance(getCtx(), 0, Transaccion);
										NuevoAtributo.setLot("1");
										NuevoAtributo.setDescription("<<1>>");
										NuevoAtributo.setIsActive(true);
										NuevoAtributo.setM_AttributeSet_ID(super.getLines()[i].getProduct().getM_AttributeSet_ID());
										NuevoAtributo.set_Value("PriceActual", CostoRecep);
										NuevoAtributo.set_Value("XX_SalePrice", PvpRecep);
										if (Package > 0)
											NuevoAtributo.set_Value("XX_VMR_Package_ID", Package);	
										/*Agregado por GHUCHET para 0/C de Despacho Directo*/
										if(Order.getXX_VLO_TypeDelivery().equals(X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO.getValue())){
											NuevoAtributo.set_ValueNoCheck("Created", Order.getXX_EstimatedDate());
										} /*Hasta aqui GHUCHET*/
										NuevoAtributo.save();
										lineaRecepcion.setM_AttributeSetInstance_ID(NuevoAtributo.getM_AttributeSetInstance_ID());
										lineaRecepcion.save();
									}
						
							}  catch (SQLException e) {
								throw e;
							}finally{
								rsMaximo.close();
							}
						}
					} catch (Exception e) {
						error = true;
						e.printStackTrace();
						Transaccion.rollback();
					} finally{
						try {
							rs.close();			
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}			
			}
			if(!(Transaccion.commit()))
			{
				Transaccion.rollback();
				log.saveError("Error", Msg.getMsg(getCtx(), "XX_Support"));
				error = true;
			}
			try {
				Pstmt.close();
				PstmtMaximo.close();
	
			} catch (SQLException e) {
				Transaccion.rollback();
				e.printStackTrace();
			}
		}
		String aux = "";
		if (!error){
			aux = super.completeIt();
		}
		//lineaRecepcion.save();
		// NuevoAtributo.save();
		return aux;
	}

}
