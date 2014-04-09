package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_InOut;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.As400DbManager;


public class XX_FixSales extends SvrProcess {

	private Timestamp salesDate = null;
	
	@Override
	protected void prepare() {
		
		//Agarra dia para el cálculo
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Date"))
				salesDate = (Timestamp) element.getParameter();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	Hashtable<String, BigDecimal> hashCompAmount = new Hashtable<String, BigDecimal>();
	Hashtable<String, Integer> hashCompIDs = new Hashtable<String, Integer>();
	
	@Override
	protected String doIt() throws Exception {
	
		//Carga las O/V en los HashTables
		getMapComp();
		Hashtable<String, BigDecimal> hashCR = getMapCR();
		
		//Recorre hashComp y compara vs hasCR
		Set<String> claves = hashCompAmount.keySet();
		for (String clave : claves) {
			
			if(hashCompAmount.get(clave)!=null){
				
				if(hashCR.get(clave)==null || hashCR.get(clave).compareTo(hashCompAmount.get(clave))!=0){
					
					//Eliminar O/Venta
					MOrder sale = new MOrder( Env.getCtx(), hashCompIDs.get(clave), get_Trx());
					sale.setDocAction(X_C_Order.DOCACTION_Void);
					DocumentEngine.processIt(sale, X_C_Order.DOCACTION_Void);
					sale.setDocumentNo(sale.getDocumentNo() + "_VOID");
					sale.save();
					commit();
					
					Thread.sleep(1000);
				}
						
			}else
				System.out.println("Error en uno de los valores");
		}
		
		return "Finalizado";
	}
	
	/*
	 * Get Hash (Sales) From Compiere
	 */
	private void getMapComp(){
		
		String sql = "select DocumentNo, sum((priceactual+ol.xx_employeediscount)*QTYORDERED) ventas, oc.C_Order_ID " +
					 "from c_order oc " +
					 "inner join C_OrderLine ol ON (oc.c_order_id = ol.c_order_id and issotrx='Y') " +
					 "inner join ad_org org ON (oc.ad_org_id = org.ad_org_id) " +
					 "where " +
					 "oc.docstatus = 'CO' " +
					 "and oc.dateordered = TO_DATE ('"+ new SimpleDateFormat("yyyyMMdd").format(salesDate.getTime()) +"', 'YYYYMMDD') " +
					 "group by org.value, DocumentNo, oc.C_Order_ID " +
					 "order by org.value, DocumentNo"; 
		
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		
		try {
			rs = prst.executeQuery();
	
			while (rs.next()){
				hashCompAmount.put(rs.getString(1), rs.getBigDecimal(2));
				hashCompIDs.put(rs.getString(1), rs.getInt(3));
			}
			
		} 
		catch (SQLException e){
			System.out.println("Error " + sql);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	}
	
	/*
	 * Get Hash (Sales) From CR
	 */
	private Hashtable<String, BigDecimal> getMapCR(){
		
		Hashtable<String, BigDecimal> hashCR = new Hashtable<String, BigDecimal>();
        
        As400DbManager As = new As400DbManager();
        As.conectar();

        try{
           
        	String sql = "SELECT " +
						 "concat(t.numtienda, concat('_',concat(CAST('"+ new SimpleDateFormat("dd/MM/yy").format(salesDate.getTime()) +"' AS varchar(10)), " +
						 "concat('_',concat(t.numcajainicia,concat('_',t.numtransaccion)))))) as documentno, " +
						 "SUM( case " +
						     "when tipotransaccion = 'V' then ((preciofinal+desctoempleado)*cantidad) " +
						     "else ((preciofinal+desctoempleado)*cantidad)*-1 end " +
						 ") " +
						 "as ventas FROM cr.transaccion t, cr.detalletransaccion d " +
						 "WHERE " +
						 "estadotransaccion='F' AND t.fecha=d.fecha AND " +
						 "t.numtienda=d.numtienda AND " +
						 "t.numcajafinaliza=d.numcajafinaliza AND "+
						 "t.numtransaccion=d.numtransaccion AND t.fecha='"+ new SimpleDateFormat("yyyy-MM-dd").format(salesDate.getTime()) +"' " +
						 "GROUP BY t.numtienda,t.numcajainicia,t.numtransaccion " +
						 "ORDER BY documentno"; 	
           
            Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = As.realizarConsulta(sql, sentencia);

            while (rs.next()){
				hashCR.put(rs.getString(1), rs.getBigDecimal(2));
			}
            
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return hashCR;
	}

}
