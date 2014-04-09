package compiere.model.dynamic.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.X_XX_VME_Elements;

/** XX_VME_ValidateElement
 * Proceso que se encarga de validar un elemento. Se considera un elemento valido 
 * si su cantidad se corresponde con la sumatoria de sus referencias y a su vez, 
 * la cantidad de las referencias es igual a la sumatoria de sus productos.
 * @author mvintimilla
 * @version 1.0
 * */
public class XX_VME_ValidateElement extends SvrProcess{
	private Integer elementID;
	
	@Override
	protected String doIt() throws Exception {
		int qtyElem = 0;
		int qtyRef = 0;
		int qtyProds = 0;
		int qtyRefAssoc = 0;
		boolean validElement = false;
		PreparedStatement psQuery = null;
		ResultSet rsQuery = null;
		PreparedStatement psQueryProds = null;
		ResultSet rsQueryProds = null;
		String result = "";
		String msj = "";
		X_XX_VME_Elements elemento = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		qtyElem = (elemento.getXX_VME_QTYPUBLISHED()).intValue();
		
		String SQLRef = " SELECT E.XX_VME_QTYPUBLISHED QTYELEM, " +
				" CASE WHEN R.XX_VME_REFERENCE_ID IS NULL THEN 0 ELSE R.XX_VME_REFERENCE_ID END ID, " +
				" CASE WHEN R.XX_VME_INDEPABISQTY IS NULL THEN 0 ELSE R.XX_VME_INDEPABISQTY END QTYREF, " +
				" CASE WHEN E.XX_VME_QTYREFASSOCIATED IS NULL THEN 0 ELSE E.XX_VME_QTYREFASSOCIATED END QTY " +
				" FROM XX_VME_ELEMENTS E LEFT OUTER JOIN XX_VME_REFERENCE R " +
				" ON(E.XX_VME_ELEMENTS_ID = R.XX_VME_ELEMENTS_ID) " +
				" WHERE E.XX_VME_ELEMENTS_ID = " + elementID;
		//System.out.println("SQLREF: "+SQLRef);
		
		try{
			psQuery = DB.prepareStatement(SQLRef, null);
			rsQuery = psQuery.executeQuery();
			while(rsQuery.next()){
				qtyRef = qtyRef + rsQuery.getInt("QTYREF");
				qtyRefAssoc = rsQuery.getInt("QTY");
				if(qtyRefAssoc > 0){
					// Para cada referencia se obtiene las cantidades de los productos
					// asociados a ella
					String SQLProds = " SELECT CASE WHEN SUM(P.XX_VME_INDEPABISQTY) IS NULL " +
							" THEN 0 ELSE SUM(P.XX_VME_INDEPABISQTY) END QTYPROD " +
							" FROM XX_VME_REFERENCE R INNER JOIN XX_VME_PRODUCT P " +
							" ON (P.XX_VME_REFERENCE_ID = R.XX_VME_REFERENCE_ID) " +
							" WHERE R.XX_VME_REFERENCE_ID = " +rsQuery.getInt("ID");
					//System.out.println("SQLProds"+SQLProds);
					try{
						psQueryProds = DB.prepareStatement(SQLProds, null);
						rsQueryProds = psQueryProds.executeQuery();
						while(rsQueryProds.next()){
							if(rsQueryProds.getInt("QTYPROD") == 0){
								qtyProds = qtyProds + rsQuery.getInt("QTYREF");
							}
							else {
								qtyProds = qtyProds + rsQueryProds.getInt("QTYPROD");
							}
						} // while
					}//try		
					catch(SQLException e){	e.printStackTrace(); }
					finally{
						DB.closeResultSet(rsQueryProds);
						DB.closeStatement(psQueryProds);
					}
				} //qtyrefass
			} // while
		}//try		
		catch(SQLException e){	
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rsQuery);
			DB.closeStatement(psQuery);
		}

		// Si el elemento no tiene referencias asociadas se coloca como no válido
		if(qtyRefAssoc == 0) {
			validElement = false;
			msj = Msg.getMsg(Env.getCtx(), "XX_InvalidElemRef");
		}
		else {
			// Se verifican las cantidades para las referencias y poder validar el elemento
			// Cantidad del elemento = sumatoria de cantidades de referencias
			if((qtyElem == qtyRef) && (qtyRef == qtyProds)){
				validElement= true;
				msj = Msg.getMsg(Env.getCtx(), "XX_ValidElement");
			}
			else if((qtyElem == qtyRef)	&& (qtyRef != qtyProds)){
				validElement= false;
				msj = Msg.getMsg(Env.getCtx(), "XX_InvalidElemProd");
			}
			else if((qtyElem != qtyRef)	&& (qtyRef == qtyProds)){
				validElement= false;
				msj = Msg.getMsg(Env.getCtx(), "XX_InvalidElemRef");
			}
			else if((qtyElem != qtyRef)	&& (qtyRef != qtyProds)){
				validElement= false;
				msj = Msg.getMsg(Env.getCtx(), "XX_InvalidElemProdRef");
			}
			
			// Se informa al usuario sobre la validación
			if(!validElement){
				elemento.setXX_VME_Validated(false);
				elemento.save();
				ADialog.error(1, new Container(), msj );
			}
			else {
				elemento.setXX_VME_Validated(true);
				elemento.save();
				ADialog.info(1, new Container(), msj );
			}
		} // ref asociadas
		
		return result;
	} // Fin doIt
	
	@Override
	protected void prepare() {
		elementID = getRecord_ID();
	}// Fin prepare	

} // Fin XX_VME_ValidateElement
