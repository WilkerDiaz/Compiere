package compiere.model.importcost;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.DB;

public class Ajuste {
	private BigDecimal cant =  null;
	private String typeDelivery = null;
	private int warehouseId = 0;
	private int socio = 0;
	private int pais = 0;
	private int orden = 0; 
	private int formaPago = 0;   		 
	private int departamento = 0;
	private int detail = 0;
	private int guiaNroID=0;
	//private BigDecimal montoAjustado = null;
	
	public Ajuste(BigDecimal cant, String typeDelivery, int warehouseId,
			int socio, int pais, int orden, int formaPago, int departamento,
			int detail, int guiaNroID) {
		// TODO Auto-generated constructor stub
		this.cant= cant;
		this.typeDelivery = typeDelivery;
		this.departamento = departamento;
		this.detail = detail;
		this.formaPago = formaPago;
		this.orden = orden;
		this.socio = socio;
		this.warehouseId = warehouseId;
		this.pais = pais;
		this.guiaNroID=guiaNroID;
	}

	public BigDecimal getCant() {
		return cant;
	}

	public void setCant(BigDecimal cant) {
		this.cant = cant;
	}

	public String getTypeDelivery() {
		return typeDelivery;
	}

	public void setTypeDelivery(String typeDelivery) {
		this.typeDelivery = typeDelivery;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public int getSocio() {
		return socio;
	}

	public void setSocio(int socio) {
		this.socio = socio;
	}

	public int getPais() {
		return pais;
	}

	public void setPais(int pais) {
		this.pais = pais;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public int getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(int formaPago) {
		this.formaPago = formaPago;
	}

	public int getDepartamento() {
		return departamento;
	}

	public void setDepartamento(int departamento) {
		this.departamento = departamento;
	}

	public int getDetail() {
		return detail;
	}

	public void setDetail(int detail) {
		this.detail = detail;
	}

	public BigDecimal getPeso() {
		return new BigDecimal(getCant().doubleValue()/getCantProv().doubleValue());
	}

	public int getGuiaNroID() {
		return guiaNroID;
	}

	public void setGuiaNroID(int guiaNroID) {
		this.guiaNroID = guiaNroID;
	}

	public BigDecimal getCantProv() {
		return buscarCantProv();
	}
	
	public BigDecimal getMontoAjustado(int orden){
		BigDecimal monto = new BigDecimal(0);
		String sql =  "SELECT SUM(XX_COSANT) AS XX_COSANT FROM XX_VLO_SUMMARY " +
				"WHERE DataType = 'T' AND C_ORDER_ID =" + orden;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		try {
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) if (rs.getBigDecimal("XX_COSANT")!=null) monto=rs.getBigDecimal("XX_COSANT");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return monto;
	}
	public BigDecimal buscarCantProv(){
		String sql= "select sum(TOTALLINES * XX_REALEXCHANGERATE) as total from c_order " +
				"where XX_VLO_BOARDINGGUIDE_ID="+getGuiaNroID()+" and C_BPARTNER_ID="+getSocio();
		BigDecimal cantProv = new BigDecimal(0); 
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		try {
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) cantProv = rs.getBigDecimal("total");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cantProv;
	}
}
