package compiere.model.cds;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.X_C_Invoice;
import org.compiere.process.DocumentEngine;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.vos.DocActionConstants;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Invoice Model. Please do not set DocStatus and C_DocType_ID directly. They
 * are set in the process() method. Use DocAction and C_DocTypeTarget_ID
 * instead.
 * 
 * @author Patricia Ayuso M. Wdiaz
 * 
 */
public class MInvoice extends org.compiere.model.MInvoice {

	private static final long serialVersionUID = 1L;
	Utilities util = new Utilities();
	private int m_rol = Env.getCtx().getAD_Role_ID();

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger
			.getCLogger(MInvoice.class);

	/**
	 * Constructor
	 * 
	 * @param ctx
	 * @param C_Invoice_ID
	 * @param trxName
	 */
	public MInvoice(Ctx ctx, int C_Invoice_ID, Trx trx) {
		super(ctx, C_Invoice_ID, trx);

	}

	/**
	 * Constructor
	 * 
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MInvoice(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}

	/**
	 * Set XX_Discount1
	 * 
	 * @param Discount1
	 */
	public void setXX_Discount1(java.math.BigDecimal Discount1) {
		set_Value("XX_Discount1", Discount1);

	}

	/**
	 * Get XX_Discount1
	 * 
	 * @return Discount1
	 */
	public java.math.BigDecimal getXX_Discount1() {
		BigDecimal discount1 = get_ValueAsBigDecimal("XX_Discount1");

		return discount1;

	}

	/**
	 * Set XX_Discount2
	 * 
	 * @param Discount2
	 */
	public void setXX_Discount2(java.math.BigDecimal Discount2) {
		set_Value("XX_Discount2", Discount2);

	}

	/**
	 * Get XX_Discount2
	 * 
	 * @return Discount2
	 */
	public java.math.BigDecimal getXX_Discount2() {
		BigDecimal discount2 = get_ValueAsBigDecimal("XX_Discount2");

		return discount2;

	}

	/**
	 * Set XX_Discount3
	 * 
	 * @param Discount3
	 */
	public void setXX_Discount3(java.math.BigDecimal Discount3) {
		set_Value("XX_Discount3", Discount3);

	}

	/**
	 * Get XX_Discount3
	 * 
	 * @return Discount3
	 */
	public java.math.BigDecimal getXX_Discount3() {
		BigDecimal discount3 = get_ValueAsBigDecimal("XX_Discount3");

		return discount3;

	}

	/**
	 * Set XX_Discount4
	 * 
	 * @param Discount4
	 */
	public void setXX_Discount4(java.math.BigDecimal Discount4) {
		set_Value("XX_Discount4", Discount4);

	}

	/**
	 * Get XX_Discount4
	 * 
	 * @return Discount4
	 */
	public java.math.BigDecimal getXX_Discount4() {
		BigDecimal discount4 = get_ValueAsBigDecimal("XX_Discount4");

		return discount4;

	}

	/**
	 * Set XX_ControlNumber
	 * 
	 * @param Control
	 *            Number
	 */
	public void setXX_ControlNumber(String ControlNumber) {
		set_Value("XX_ControlNumber", ControlNumber);

	}

	/**
	 * Get XX_ControlNumber
	 * 
	 * @return Control Number
	 */
	public String getXX_ControlNumber() {
		return get_ValueAsString("XX_ControlNumber");

	}

	/**
	 * Set XX_RealExchangeRate
	 * 
	 * @param RealExchangeRate
	 */
	public void setXX_RealExchangeRate(java.math.BigDecimal RealExchangeRate) {
		set_Value("XX_RealExchangeRate", RealExchangeRate);

	}

	/**
	 * Get XX_RealExchangeRate
	 * 
	 * @return Real Exchange Rate
	 */
	public java.math.BigDecimal getXX_RealExchangeRate() {
		return get_ValueAsBigDecimal("XX_RealExchangeRate");

	}

	/**
	 * Get XX_SynchronizationBank.
	 * 
	 * @return XX_SynchronizationBank
	 */
	public Boolean getXX_SynchronizationBank() {
		return (Boolean) get_Value("XX_SynchronizationBank");
	}

	/**
	 * Set XX_SynchronizationBank.
	 * 
	 * @param XX_SynchronizationBank
	 */
	public void setXX_SynchronizationBank(Boolean XX_SynchronizationBank) {
		set_Value("XX_SynchronizationBank", XX_SynchronizationBank);
	}

	/**
	 * Get XX_SynchronizationAccount.
	 * 
	 * @return XX_SynchronizationAccount
	 */
	public Boolean getXX_SynchronizationAccount() {
		return (Boolean) get_Value("XX_SynchronizationAccount");
	}

	/**
	 * Set XX_SynchronizationAccount.
	 * 
	 * @param XX_SynchronizationAccount
	 */
	public void setXX_SynchronizationAccount(Boolean XX_SynchronizationAccount) {
		set_Value("XX_SynchronizationAccount", XX_SynchronizationAccount);
	}

	/**
	 * Set XX_TaxAmount
	 * 
	 * @param TaxAmount
	 */
	public void setXX_TaxAmount(java.math.BigDecimal TaxAmount) {
		set_Value("XX_TaxAmount", TaxAmount);

	}

	/**
	 * Get XX_TaxAmount
	 * 
	 * @return XX_TaxAmount
	 */
	public java.math.BigDecimal getXX_TaxAmount() {
		return get_ValueAsBigDecimal("XX_TaxAmount");

	}

	/**
	 * Set XX_VMR_DEPARTMENT_ID
	 * 
	 * @param Department
	 */
	public void setXX_VMR_Department_ID(int Department) {
		if (Department < 1)
			throw new IllegalArgumentException(
					"XX_VMR_Department_ID is mandatory.");
		set_Value("XX_VMR_Department_ID", Integer.valueOf(Department));

	}

	/**
	 * Get XX_VMR_DEPARTMENT_ID
	 * 
	 * @return Department ID
	 */
	public int getXX_VMR_Department_ID() {
		return get_ValueAsInt("XX_VMR_Department_ID");

	}

	/**
	 * Set M_Warehouse_ID
	 * 
	 * @param Warehouse
	 */
	public void setM_Warehouse_ID(int Warehouse) {
		if (Warehouse < 1)
			throw new IllegalArgumentException("M_Warehouse_ID is mandatory.");
		set_Value("M_Warehouse_ID", Integer.valueOf(Warehouse));

	}

	/**
	 * Get M_Warehouse_ID
	 * 
	 * @return Warehouse ID
	 */
	public int getM_Warehouse_ID() {
		return get_ValueAsInt("M_Warehouse_ID");

	}

	/**
	 * Set XX_Contract_ID
	 * 
	 * @param XX_Contract_ID
	 */
	public void setXX_Contract_ID(int XX_Contract_ID) {
		if (XX_Contract_ID <= 0)
			set_ValueNoCheck("XX_Contract_ID", null);
		else
			set_ValueNoCheck("XX_Contract_ID", Integer.valueOf(XX_Contract_ID));
	}

	/**
	 * Get XX_Contract_ID
	 * 
	 * @return XX_Contract_ID
	 */
	public int getXX_Contract_ID() {
		return get_ValueAsInt("XX_Contract_ID");
	}

	/**
	 * Set XX_ParcelQty
	 * 
	 * @param XX_ParcelQty
	 */
	public void setXX_ParcelQty(int XX_ParcelQty) {
		set_Value("XX_ParcelQty", XX_ParcelQty);
	}

	/**
	 * Get XX_ParcelQty
	 * 
	 * @return XX_ParcelQty
	 */
	public int getXX_ParcelQty() {
		return (Integer) get_Value("XX_ParcelQty");
	}

	/**
	 * Print Definitive Doc.
	 * 
	 * @param XX_PrintDefinitive
	 */
	public void setXX_PrintDefinitive(String value) {
		set_Value("XX_PrintDefinitive", value);

	}

	/**
	 * Get Print Definitive Doc.
	 * 
	 * @return
	 */
	public String getXX_PrintDefinitive() {
		return (String) get_Value("XX_PrintDefinitive");

	}

	public void setXX_IsDiferentPrice(boolean XX_IsDiferentPrice) {
		set_Value("XX_IsDiferentPrice", Boolean.valueOf(XX_IsDiferentPrice));

	}

	/**
	 * Get Alert.
	 * 
	 * @return Alert
	 */
	public boolean isXX_IsDiferentPrice() {
		return get_ValueAsBoolean("XX_IsDiferentPrice");

	}

	@Override
	protected boolean beforeDelete() {

		/**** Jessica Mendoza ****/
		if (getXX_Contract_ID() != 0) {
			String update = "update XX_VCN_EstimatedAPayable "
					+ "set XX_Dispensable = 'N' " + "where XX_Contract_ID = "
					+ getXX_Contract_ID() + " " + "and XX_DateEstimated = "
					+ DB.TO_DATE(getXX_DueDate()) + " ";
			DB.executeUpdate(null, update);
		}
		/**** Fin código - Jessica Mendoza ****/

		// Se borran las distribuciones
		String delete = "UPDATE XX_PRODUCTPERCENTDISTRIB SET C_INVOICE_ID = NULL, C_INVOICELINE_ID = NULL WHERE C_Invoice_ID ="
				+ get_ID();
		DB.executeUpdate(null, delete);

		// Se borran las distribuciones
		String update = "UPDATE M_INOUT SET C_INVOICE_ID = null  WHERE C_Invoice_ID ="
				+ get_ID();
		DB.executeUpdate(null, update);

		if (getDocStatus().compareTo(X_C_Invoice.DOCSTATUS_Drafted) == 0)
			return true;
		else
			return super.beforeDelete();
	}

	/**
	 * After Save
	 * 
	 * @param newRecord
	 *            new
	 * @param success
	 *            success
	 * @return success Modificado por WDiaz
	 */
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		boolean save = super.afterSave(newRecord, success);

		if (save) {

			/** Updating InvoicingStatus in Purchase Order */
			Integer orderID = getC_Order_ID();
			if (orderID == null || orderID.intValue() == 0)
				save = true;
			else {
				MOrder mOrder = new MOrder(getCtx(), orderID, null);
				if ((mOrder.getXX_InvoicingStatus().compareTo("AP") != 0)
						&& (mOrder.getXX_InvoicingStatus().compareTo("AF") != 0)
						&& (mOrder.getXX_InvoicingStatus().compareTo("EP") != 0)) {
					mOrder.setXX_InvoicingStatus("EP");

					if (mOrder.save())
						log.fine("Update Purchase Order's XX_InvoicingStatus ");
				}
			}
			save = true;

		}

		return save;
	}

	/**************************************************************************
	 * Before Save
	 * 
	 * @param newRecord
	 *            new
	 * @return true
	 * @author Modificado por Jessica Mendoza
	 */
	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		setC_DocType_ID(getC_DocTypeTarget_ID());
		
		String sql = "";
		String where = "";
		Boolean bool = false;
		Utilities util = new Utilities();
		int diasBeneficio = 0;
		int diasTotalesI = 0;
		int diasTotalesII = 0;
		int diasTotalesIII = 0;
		Vector<Integer> condicionPago = new Vector<Integer>(6);
		Calendar cal = Calendar.getInstance();
		Calendar calendarEstimadaI = Calendar.getInstance();
		Calendar calendarEstimadaII = Calendar.getInstance();
		Calendar calendarEstimadaIII = Calendar.getInstance();
		Timestamp fechaEstimada = new Timestamp(0);
		Timestamp fechaEstimadaS = new Timestamp(0);
		Timestamp fechaEstimadaT = new Timestamp(0);
		Vector<Timestamp> vector = new Vector<Timestamp>(3);

		// Cuando no tiene O/C ni contrato, se valida la moneda segun el
		// proveedor
		if (!isSOTrx() && getC_Order_ID() == 0 && getXX_Contract_ID() == 0) {

			MBPartner partner = new MBPartner(Env.getCtx(), getC_BPartner_ID(),
					null);

			if (partner.getXX_VendorClass().equalsIgnoreCase("10000005")
					&& getC_Currency_ID() != 205) {
				setC_Currency_ID(205);
			}
		}

		/**** Jessica Mendoza ****/
		// Valida que solo el Coordinador de Cuentas po Pagar, pueda modificar
		// el año y/o mes
		// una vez completada la factura
		if (!isSOTrx()
				&& (getXX_InvoiceType().equals("A") || getXX_InvoiceType()
						.equals("S"))
				&& ((get_ValueOld("XX_Month") != getXX_Month()) || (get_ValueOldAsInt("XX_Year") != getXX_Year()))
				& (m_rol != Env.getCtx().getContextAsInt(
						"#XX_L_ROLECOORDAPAYABLE_ID"))
				& (getDocStatus().equals("CO"))) {
			// System.out.println("El mes y/o año solo puede ser modificado por el Coordinador de Cuentas por Pagar");
			ADialog.error(0, new Container(),
					Msg.getMsg(Env.getCtx(), "XX_NotRoleMonthYear"));
			return false;
		} else {

			if (newRecord)
				setXX_AccountPayableStatus("A");

			// Valida que el año corresponda al actual o como mínimo al año
			// pasado
			if (!isSOTrx()
					&& (getXX_InvoiceType().equals("A") || getXX_InvoiceType()
							.equals("S"))) {
				Integer yearFac = getXX_Year();
				Integer year = cal.get(Calendar.YEAR);
				int longYear = String.valueOf(year).length();
				int longValue = String.valueOf(yearFac).length();
				Integer aux = new Integer(1);

				if (longYear == longValue) {
					if (yearFac != year) {
						aux = year - aux;
						if (yearFac.compareTo(aux)!=0)
							setXX_Year(year);
						else
							setXX_Year(aux);
					}
				} else {
					if (yearFac == 0)
						setXX_Year(yearFac);
					else
						setXX_Year(year);
				}
			}
			/**** Fin código - Jessica Mendoza ****/

			if (!isSOTrx()) { // Verifica si la factura no es de tipo Cliente
				MOrder mOrder = new MOrder(getCtx(), getC_Order_ID(), null);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				/****
				 * Se verifica si la fecha estimada y/o condición de pago fue
				 * modificado
				 ****/
				if (getXX_DatePaid() != null) {
					if ((m_rol == Env.getCtx().getContextAsInt(
							"#XX_L_ROLEFINANCIALMANAGER_ID"))
							|| (m_rol == Env.getCtx().getContextAsInt(
									"#XX_L_ROLECOORDAPAYABLE_ID"))) {
						Date fechaPagoOld = fechaPagoOld();
						if (fechaPagoOld != null
								&& !df.format(fechaPagoOld.getTime()).equals(
										df.format(getXX_DatePaid().getTime()))) {
							String fechaPago = df.format(new Date(
									getXX_DatePaid().getTime()));
							String SQLFecha = "update C_Invoice "
									+ "set XX_DatePaid = to_date('" + fechaPago
									+ "','yyyy-MM-dd') "
									+ "where C_Invoice_ID = "
									+ getC_Invoice_ID();
							DB.executeUpdate(get_Trx(), SQLFecha);
						}
						if (condicionPagoOld() != getC_PaymentTerm_ID()) {
							String SQLCondicion = "update C_Invoice "
									+ "set C_PaymentTerm_ID = "
									+ getC_PaymentTerm_ID() + " "
									+ "where C_Invoice_ID = "
									+ getC_Invoice_ID();
							DB.executeUpdate(get_Trx(), SQLCondicion);
						}
					}
					/**** Fin ****/
				} else {
					setSalesRep_ID(getCtx().getAD_User_ID());

					// Busca la mínima fecha estimada, asociada a la O/C o
					// contrato
					sql = "select min(XX_DateEstimated), XX_VCN_EstimatedAPayable_ID "
							+ "from XX_VCN_EstimatedAPayable ";

					/** Setting fields from O/C */
					if (getC_Order_ID() > 0) {

						/****
						 * Setea el monto local para dicha factura, si la O/C es
						 * de mercancía
						 ****/
						if (mOrder.getXX_OrderType().equals("Importada")) {
							setXX_GrandTotalLocal(getGrandTotal().multiply(
									mOrder.getXX_DefinitiveFactor()));
						}

						/****
						 * Setear la fecha de recepcion y usuario de la O/C
						 * asociada a la factura
						 ****/
						if (getXX_InvoiceReceiptDate() == null) {
							setXX_InvoiceReceiptDate(mOrder
									.getXX_ReceptionDate());
							setXX_UserInvoiceReceipt_ID(mOrder
									.getXX_ReceptionUser_ID());
						}

						// Si no es Orden de Venta
						if (!mOrder.isSOTrx()) {
							setDateOrdered(mOrder.getXX_EstimatedDate());
							setPaymentRule(mOrder.getPaymentRule());
							setC_PaymentTerm_ID(mOrder.getC_PaymentTerm_ID());
							setXX_RealExchangeRate(mOrder
									.getXX_RealExchangeRate());
							if (mOrder.getXX_VMR_DEPARTMENT_ID() > 0) // Modificado
																		// por
																		// Jessica
																		// Mendoza
								setXX_VMR_Department_ID(mOrder
										.getXX_VMR_DEPARTMENT_ID());
							setM_Warehouse_ID(mOrder.getM_Warehouse_ID());

							/**** Calculo de la fecha de vencimiento O/C ****/
							if ((getXX_DueDate() == null)
									|| (getXX_DatePaid() == null)) {
								diasBeneficio = util
										.benefitVendorDayAdvance(getC_BPartner_ID());
								condicionPago = util
										.infoCondicionPago(getC_PaymentTerm_ID());

								if (mOrder.getXX_OrderType().equals("Nacional")) {
									diasTotalesI = condicionPago.get(1)
											- diasBeneficio;
									diasTotalesII = condicionPago.get(4)
											- diasBeneficio;
									diasTotalesIII = condicionPago.get(7)
											- diasBeneficio;
								} else {
									diasTotalesI = condicionPago.get(1);
									diasTotalesII = condicionPago.get(4);
									diasTotalesIII = condicionPago.get(7);
								}

								vector = util.calcularFecha(
										getC_PaymentTerm_ID(), getC_Order_ID(),
										"factura");
								if (vector.get(0) != null
										|| vector.get(1) != null
										|| vector.get(2) != null) {
									if (condicionPago.get(0) == 100) {
										fechaEstimada = vector.get(0);
										calendarEstimadaI
												.setTimeInMillis(fechaEstimada
														.getTime());
										calendarEstimadaI.add(Calendar.DATE,
												diasTotalesI);
										fechaEstimada = new Timestamp(
												calendarEstimadaI
														.getTimeInMillis());
										setXX_DatePaid(fechaEstimada);
										setXX_DueDate(fechaEstimada);
									} else if ((condicionPago.get(0) + condicionPago
											.get(3)) == 100) {
										fechaEstimadaS = vector.get(1);
										calendarEstimadaII
												.setTimeInMillis(fechaEstimadaS
														.getTime());
										calendarEstimadaII.add(Calendar.DATE,
												diasTotalesII);
										fechaEstimadaS = new Timestamp(
												calendarEstimadaII
														.getTimeInMillis());
										setXX_DatePaid(fechaEstimadaS);
										setXX_DueDate(fechaEstimadaS);
									} else {
										fechaEstimadaT = vector.get(2);
										calendarEstimadaIII
												.setTimeInMillis(fechaEstimadaT
														.getTime());
										calendarEstimadaIII.add(Calendar.DATE,
												diasTotalesIII);
										fechaEstimadaT = new Timestamp(
												calendarEstimadaIII
														.getTimeInMillis());
										setXX_DatePaid(fechaEstimadaT);
										setXX_DueDate(fechaEstimadaT);
									}

									// Parte del la busqueda de la mínima fecha
									// de las cuentas por pagar estimadas
									where = "where C_Order_ID = "
											+ getC_Order_ID() + " ";
									bool = true;
								}
							}
							/****
							 * Fin cálculo de la Fecha de vencimiento para las
							 * O/C
							 ****/
						}
					} else {
						if ((getXX_Contract_ID() != 0)
								&& ((getXX_DueDate() == null) || (getXX_DatePaid() == null))) {
							// Parte del la busqueda de la mínima fecha de las
							// cuentas por pagar estimadas
							where = "where XX_Contract_ID = "
									+ getXX_Contract_ID() + " ";
							bool = true;
						}
					}
					if (bool) {
						sql = sql
								+ where
								+ "group by XX_DateEstimated, XX_VCN_EstimatedAPayable_ID "
								+ "order by XX_DateEstimated, XX_VCN_EstimatedAPayable_ID asc ";
						PreparedStatement pstmt = null;
						ResultSet rs = null;
						try {
							pstmt = DB.prepareStatement(sql, get_Trx());
							rs = pstmt.executeQuery();
							if (rs.next()) {
								/****
								 * Cálculo de la fecha vencimiento para los
								 * contratos
								 ****/
								fechaEstimada = rs.getTimestamp(1);
								// Se coloca el registro en eliminable, para que
								// pueda eliminarse al momento
								// de hacer la aprobación de la factura con
								// dicho contrato
								String update = "update XX_VCN_EstimatedAPayable "
										+ "set XX_Dispensable = 'Y' "
										+ "where XX_VCN_EstimatedAPayable_ID = "
										+ rs.getInt(2);
								DB.executeUpdate(get_Trx(), update);
							} else {
								System.out
										.println("El contrato/OC no está registrado en las cuentas por pagar estimadas");
							}
						} catch (Exception e) {
							log.log(Level.SEVERE, sql);
						} finally {
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

						if (getXX_Contract_ID() != 0) {
							setXX_DueDate(fechaEstimada);
							setXX_DatePaid(fechaEstimada);
							if (getDocStatus().equals("CO")) {
								String update = "update XX_Contract "
										+ "set XX_InvoicingStatus = 'EP' "
										+ "where XX_Contract_ID = "
										+ getXX_Contract_ID();
								DB.executeUpdate(get_Trx(), update);
							}
						}
					}
				}
			}
		}
		return super.beforeSave(newRecord);
	}

	/**
	 * Busca el tipo de orden de compra (nacional - importada)
	 * 
	 * @author Jessica Mendoza
	 * @param idOrder
	 *            id de la orden de compra
	 * @return value el tipo de la orden de compra
	 */
	public String orderType(int idOrder) {
		String sql = "select XX_OrderType " + "from C_Order "
				+ "where C_Order_ID = " + getC_Order_ID();

		String value = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				value = rs.getString(1);
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, sql);
		} finally {
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
		return value;
	}

	/**
	 * Busca la condicion de pago de la factura
	 * 
	 * @author Jessica Mendoza
	 * @return
	 */
	public int condicionPagoOld() {
		String SQLConditionPaidOld = "select C_PaymentTerm_ID "
				+ "from C_Invoice " + "where (C_Invoice_ID = "
				+ getC_Invoice_ID() + ")";

		int condicionPOld = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQLConditionPaidOld, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				condicionPOld = rs.getInt("C_PaymentTerm_ID");
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, SQLConditionPaidOld);
		} finally {
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
		return condicionPOld;
	}

	/**
	 * 
	 * @author Jessica Mendoza
	 * @return La fecha de pago anterior
	 */
	public Date fechaPagoOld() {
		String SQLDatePaidOld = "select XX_DatePaid " + "from C_Invoice "
				+ "where C_Invoice_ID = " + getC_Invoice_ID();

		Date fechaPOld = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQLDatePaidOld, get_Trx());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				fechaPOld = rs.getDate("XX_DatePaid");
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, SQLDatePaidOld);
		} finally {
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
		return fechaPOld;
	}
	
	public String prepareIt()
	{
		MInvoice mInvoice = new MInvoice(getCtx(), get_ID(), get_Trx());
		
		//Ivan Valdes
		//Valida que cada una de las lineas de facturas tengan linea de distribucion si son facturas de bienes o servicios
		//Tambien valida que coincidan los montos de las lineas de factura con las lineas de distribucion
		if (mInvoice.getXX_InvoiceType().equals("A") || mInvoice.getXX_InvoiceType().equals("S")){
			if(validarLineaDeDistibucion(get_ID())){
				ADialog.info(EnvConstants.WINDOW_INFO, new Container(), "No se puede completar, las lineas de factura deben tener linea de distribución y con el mismo monto.");
				return DocActionConstants.STATUS_Drafted;
				}
		}
		return DocActionConstants.STATUS_InProgress;
	}
	/**
	 * Complete Document
	 * 
	 * @return new status (Complete, In Progress, Invalid, Waiting ..)
	 *         Modificado por WDiaz
	 */
	public String completeIt() {
		MInvoice mInvoice = new MInvoice(getCtx(), get_ID(), get_Trx());
		String status = null;

		// Jessica Mendoza
		// Valida que el rol (asistente de contabilidad) sea el autorizado, para
		// completar una factura de bienes y servicios
		if ((((mInvoice.getXX_InvoiceType().equals("A")) || (mInvoice
				.getXX_InvoiceType().equals("S"))))
				|| (mInvoice.getXX_InvoiceType().equals("I"))) {
			if (mInvoice.getC_DocTypeTarget_ID() == getCtx().getContextAsInt(
					"#XX_C_DOCTYPE_ID")) {

				// Jessica Mendoza
				if (getXX_Contract_ID() == 0) {
					// Searching for quantity or price difference between
					// Purchase Order and Invoice
					BigDecimal diff = new BigDecimal(0), diff1 = new BigDecimal(
							0), zero = new BigDecimal(0);
					boolean flag = false;
					// Query de Patricia Buscando referencia por totales
					/*
					 * String sql =
					 * "Select (il.LINENETAMT - po.LINENETAMT) as Diff " +
					 * "from c_invoiceline il " +
					 * "join c_orderline ol on ol.c_orderline_id = il.c_orderline_id "
					 * +
					 * "join XX_VMR_PO_LINEREFPROV po on po.XX_VMR_PO_LINEREFPROV_id = ol.XX_VMR_PO_LINEREFPROV_id "
					 * + "where c_invoice_id = " + getC_Invoice_ID();
					 */
					String sql = "select (PriceEntered - PriceActual) as DifCos , (QtyEntered - QtyInvoiced) as DifCant "
							+ "from C_Invoice i join  C_InvoiceLine il on (i.C_Invoice_ID = il.C_Invoice_ID) "
							+ "where i.c_invoice_id = " + getC_Invoice_ID();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(sql, null);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							diff = rs.getBigDecimal(1);
							diff1 = rs.getBigDecimal(2);
							if (diff.compareTo(zero) != 0) {
								flag = true;
								break;
							} else if (diff1.compareTo(zero) != 0) {
								flag = true;
								break;
							}
						}

					} catch (SQLException e) {
						log.log(Level.SEVERE, sql, e);
					} finally {
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

					// If there is a difference, display a message
					// if the user want to correct, return ""
					if (flag
							&& (getC_Order_ID() != 0 || getXX_Contract_ID() != 0)) {
						boolean answer = ADialog.ask(EnvConstants.WINDOW_INFO,
								new Container(), Msg.getMsg(Env.getCtx(),
										"Invoice Difference Message"));
						if (!answer)
							return DocActionConstants.STATUS_Drafted;
					}
					/** Updating O/C Invoicing Status */
					MOrder mOrder = new MOrder(getCtx(), getC_Order_ID(), null);
					if ((mOrder.getXX_InvoicingStatus().compareTo("AP") != 0)
							&& (mOrder.getXX_InvoicingStatus().compareTo("AF") != 0)) {
						mOrder.setXX_InvoicingStatus("AF");

						if (getC_Order_ID() > 0 && mOrder.save())
							log.fine("Update Purchase Order's XX_InvoicingStatus ");
					}
					status = super.completeIt();
				} else { // Jessica Mendoza
					status = DocActionConstants.STATUS_Completed;
				}
			} else
				status = super.completeIt();
		}

		return status;
	}

	/**
	 * Set XX_DueDate.
	 * 
	 * @param Remainder
	 *            of the payment is due
	 */
	public void setXX_DueDate(Timestamp XX_DueDate) {
		set_Value("XX_DueDate", XX_DueDate);

	}

	/**
	 * Get XX_DueDate.
	 * 
	 * @return La fecha de vencimiento
	 */
	public Timestamp getXX_DueDate() {
		return (Timestamp) get_Value("XX_DueDate");

	}

	/**
	 * Set XX_DatePaid
	 * 
	 * @param
	 */
	public void setXX_DatePaid(Timestamp XX_DatePaid) {
		set_Value("XX_DatePaid", XX_DatePaid);

	}

	/**
	 * Get XX_DatePaid
	 * 
	 * @return La fecha de pago
	 */
	public Timestamp getXX_DatePaid() {
		return (Timestamp) get_Value("XX_DatePaid");

	}

	/**
	 * Set XX_InvoicingStatusContract
	 * 
	 * @param
	 */
	public void setXX_InvoicingStatusContract(String XX_InvoicingStatusContract) {
		set_Value("XX_InvoicingStatusContract", XX_InvoicingStatusContract);

	}

	/**
	 * Get XX_InvoicingStatusContract
	 * 
	 * @return Estado de a facturación para los contratos
	 */
	public String getXX_InvoicingStatusContract() {
		return (String) get_Value("XX_InvoicingStatusContract");

	}

	/**
	 * Set XX_AccountPayableStatus
	 * 
	 * @param
	 */
	public void setXX_AccountPayableStatus(String XX_AccountPayableStatus) {
		set_Value("XX_AccountPayableStatus", XX_AccountPayableStatus);

	}

	/**
	 * Get XX_AccountPayableStatus
	 * 
	 * @return Estado de las cuentas por pagar en factura
	 */
	public String getXX_AccountPayableStatus() {
		return (String) get_Value("XX_AccountPayableStatus");

	}

	/**
	 * Set "XX_RejectedInvoiceButton
	 * 
	 * @param
	 */
	public void setXX_RejectedInvoiceButton(Character XX_RejectedInvoiceButton) {
		set_Value("XX_RejectedInvoiceButton", XX_RejectedInvoiceButton);

	}

	/**
	 * Get "XX_RejectedInvoiceButton
	 * 
	 * @return Boton del rechazo de la factura
	 */
	public Character getXX_RejectedInvoiceButton() {
		return (Character) get_Value("XX_RejectedInvoiceButton");

	}

	/**
	 * Set XX_ReasonRejectionInvoice
	 * 
	 * @param
	 */
	public void setXX_ReasonRejectionInvoice(String XX_ReasonRejectionInvoice) {
		set_Value("XX_ReasonRejectionInvoice", XX_ReasonRejectionInvoice);

	}

	/**
	 * Get XX_ReasonRejectionInvoice
	 * 
	 * @return Motivo del rechazo de la factura
	 */
	public String getXX_ReasonRejectionInvoice() {
		return (String) get_Value("XX_ReasonRejectionInvoice");

	}

	/**
	 * Set setXX_InvoiceType
	 * 
	 * @param
	 */
	public void setXX_InvoiceType(String XX_InvoiceType) {
		set_Value("XX_InvoiceType", XX_InvoiceType);

	}

	/**
	 * Get XX_InvoiceType
	 * 
	 * @return Tipo de factura (bienes,servicios,productos para la venta)
	 */
	public String getXX_InvoiceType() {
		return (String) get_Value("XX_InvoiceType");

	}

	/**
	 * Set XX_Month
	 * 
	 * @param
	 */
	public void setXX_Month(String XX_Month) {
		set_Value("XX_Month", XX_Month);

	}

	/**
	 * Get XX_Month
	 * 
	 * @return Mes
	 */
	public String getXX_Month() {
		return (String) get_Value("XX_Month");

	}

	/**
	 * Set XX_Year
	 * 
	 * @param
	 */
	public void setXX_Year(int XX_Year) {
		set_Value("XX_Year", XX_Year);

	}

	/**
	 * Get XX_Year
	 * 
	 * @return Año
	 */
	public int getXX_Year() {
		return get_ValueAsInt("XX_Year");

	}

	/**
	 * Set XX_GrandTotalLocal
	 * 
	 * @param XX_GrandTotalLocal
	 */
	public void setXX_GrandTotalLocal(BigDecimal XX_GrandTotalLocal) {
		set_Value("XX_GrandTotalLocal", XX_GrandTotalLocal);

	}

	/**
	 * Get XX_GrandTotalLocal
	 * 
	 * @return XX_GrandTotalLocal
	 */
	public BigDecimal getXX_GrandTotalLocal() {
		BigDecimal XX_GrandTotalLocal = get_ValueAsBigDecimal("XX_GrandTotalLocal");

		return XX_GrandTotalLocal;

	}

	/**
	 * Set XX_InvoiceReceiptDate
	 * 
	 * @param
	 */
	public void setXX_InvoiceReceiptDate(Timestamp XX_InvoiceReceiptDate) {
		set_Value("XX_InvoiceReceiptDate", XX_InvoiceReceiptDate);

	}

	/**
	 * Get XX_InvoiceReceiptDate
	 * 
	 * @return La fecha de recepción asociada a la O/C
	 */
	public Timestamp getXX_InvoiceReceiptDate() {
		return (Timestamp) get_Value("XX_InvoiceReceiptDate");

	}

	/**
	 * Set XX_UserInvoiceReceipt_ID
	 * 
	 * @param XX_UserInvoiceReceipt_ID
	 */
	public void setXX_UserInvoiceReceipt_ID(int XX_UserInvoiceReceipt_ID) {
		set_Value("XX_UserInvoiceReceipt_ID", XX_UserInvoiceReceipt_ID);
	}

	/**
	 * Get XX_UserInvoiceReceipt_ID
	 * 
	 * @return Usuario de recepción asociada a la O/C
	 */
	public int getXX_UserInvoiceReceipt_ID() {
		return (Integer) get_Value("XX_UserInvoiceReceipt_ID");
	}

	/**
	 * Set XX_UserInvoiceApproval_ID
	 * 
	 * @param XX_UserInvoiceApproval_ID
	 */
	public void setXX_UserInvoiceApproval_ID(int XX_UserInvoiceApproval_ID) {
		set_Value("XX_UserInvoiceApproval_ID", XX_UserInvoiceApproval_ID);
	}

	/**
	 * Get XX_UserInvoiceApproval_ID
	 * 
	 * @return Usuario de aprobacion asociada a la O/C
	 */
	public int getXX_UserInvoiceApproval_ID() {
		return (Integer) get_Value("XX_UserInvoiceApproval_ID");
	}

	/**
	 * Set XX_RejectionDate
	 * 
	 * @param
	 */
	public void setXX_RejectionDate(Timestamp XX_RejectionDate) {
		set_Value("XX_RejectionDate", XX_RejectionDate);

	}

	/**
	 * Get XX_RejectionDate
	 * 
	 * @return La fecha de recepción asociada a la O/C
	 */
	public Timestamp getXX_RejectionDate() {
		return (Timestamp) get_Value("XX_RejectionDate");

	}

	/**
	 * Get XX_UserInvoiceRejection_ID
	 * 
	 * @return Usuario de recepción asociada a la O/C
	 */
	public int getXX_UserInvoiceRejection_ID() {
		return (Integer) get_Value("XX_UserInvoiceRejection_ID");
	}

	/**
	 * Set XX_UserInvoiceRejection_ID
	 * 
	 * @param XX_UserInvoiceRejection_ID
	 */
	public void setXX_UserInvoiceRejection_ID(int XX_UserInvoiceRejection_ID) {
		set_Value("XX_UserInvoiceRejection_ID", XX_UserInvoiceRejection_ID);
	}

	/**
	 * Set XX_ApprovalDate
	 * 
	 * @param
	 */
	public void setXX_ApprovalDate(Timestamp XX_ApprovalDate) {
		set_Value("XX_ApprovalDate", XX_ApprovalDate);

	}

	/**
	 * Get XX_ApprovalDate
	 * 
	 * @return La fecha de aprobacion asociada a la O/C
	 */
	public Timestamp getXX_ApprovalDate() {
		return (Timestamp) get_Value("XX_ApprovalDate");

	}

	 private boolean validarLineaDeDistibucion(int get_ID) {
			// TODO Auto-generated method stub
			  
			  boolean error = false; //VARIABLE DE VERIFICACION 

			  //VALIDA QUE LAS LINEAS DE FACTURA TENGAN LINEAS DE DISTRIBUCION
			  String sql=
			  "SELECT CI.C_INVOICE_ID AS FACTURA ,CIL.C_INVOICELINE_ID AS LINEA, XXP.XX_PRODUCTPERCENTDISTRIB_ID AS DISTRIBUCION, XXC.XX_PERCENAMOUNT AS DISTRIBUCION1 "
			  +" FROM C_INVOICE CI LEFT OUTER JOIN C_INVOICELINE CIL ON CI.C_INVOICE_ID = CIL.C_INVOICE_ID "
			  +" LEFT OUTER JOIN XX_PRODUCTPERCENTDISTRIB XXP ON CIL.C_INVOICELINE_ID = XXP.C_INVOICELINE_ID"
			  +" LEFT OUTER JOIN XX_ContractDetail XXC ON CI.XX_PAYCONTRACT_ID = XXC.XX_PAYCONTRACT_ID AND XXC.ISACTIVE= 'Y' "
			  +" WHERE CI.C_INVOICE_ID = "+get_ID;
			  
				PreparedStatement pstmt = null; 
				ResultSet rs = null;
				try{
					pstmt = DB.prepareStatement(sql, get_Trx()); 
					rs = pstmt.executeQuery();
					while(rs.next()){
						if(rs.getString("DISTRIBUCION") == null && rs.getString("DISTRIBUCION1") == null){
							error=true;
						}
					}
					
				}catch (Exception e) {
					log.log(Level.SEVERE, sql);
				}finally{
						try {
							rs.close();
							pstmt.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
				//VALIDA QUE EL MONTO DE LAS LINEAS DE FACTURAS SEAN IGUAL A LA SUMA DEL MONTO DE LAS LINEAS DE DISTRIBUCION
				  
				String sql1=
						  "SELECT C.LINENETAMT AS LINEA, (SELECT SUM(X.XX_AMOUNTPERCC) FROM XX_ProductPercentDistrib X WHERE C.C_InvoiceLine_ID=X.C_InvoiceLine_ID ) AS SUMA, " +
						  "(SELECT SUM(det.XX_CONTRACTAMOUNT) FROM XX_CONTRACTDETAIL det WHERE det.xx_contract_id  = i.xx_contract_id   ) AS SUMA1 " +   
						  "FROM C_InvoiceLine C  " +
						  "inner join C_Invoice I on I.C_Invoice_ID=C.C_Invoice_ID " +
						  " WHERE I.C_Invoice_ID = " + get_ID;

						  
							PreparedStatement pstmt1 = null; 
							ResultSet rs1 = null;
							try{
								pstmt1 = DB.prepareStatement(sql1, get_Trx()); 
								rs1 = pstmt1.executeQuery();
								while(rs1.next()){
									if(rs1.getString("LINEA").compareTo(rs1.getString("SUMA")) != 0 && rs1.getString("LINEA").compareTo(rs1.getString("SUMA1")) != 0){
										error=true;
									}
								}
								
							}catch (Exception e) {
								log.log(Level.SEVERE, sql);
							}finally{
									try {
										rs1.close();
										pstmt1.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}

			return error;
		  }

}
