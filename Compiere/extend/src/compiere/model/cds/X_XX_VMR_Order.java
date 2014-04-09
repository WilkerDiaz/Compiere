/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_Order
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Order extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Order_ID id
    @param trx transaction
    */
    public X_XX_VMR_Order (Ctx ctx, int XX_VMR_Order_ID, Trx trx)
    {
        super (ctx, XX_VMR_Order_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Order_ID == 0)
        {
            setM_Warehouse_ID (0);
            setXX_OrderBecoCorrelative (null);
            setXX_OrderRequestStatus (null);
            setXX_VMR_Order_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Order (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27635119976789L;
    /** Last Updated Timestamp 2012-11-15 11:21:00.0 */
    public static final long updatedMS = 1352994660000L;
    /** AD_Table_ID=1000148 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Order");
        
    }
    ;
    
    /** TableName=XX_VMR_Order */
    public static final String Table_Name="XX_VMR_Order";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        throw new IllegalArgumentException ("C_BPartner_ID is virtual column");
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Order_ID()));
        
    }
    
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        throw new IllegalArgumentException ("Description is virtual column");
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Approval Date.
    @param XX_ApprovalDate Approval Date */
    public void setXX_ApprovalDate (Timestamp XX_ApprovalDate)
    {
        set_Value ("XX_ApprovalDate", XX_ApprovalDate);
        
    }
    
    /** Get Approval Date.
    @return Approval Date */
    public Timestamp getXX_ApprovalDate() 
    {
        return (Timestamp)get_Value("XX_ApprovalDate");
        
    }
    
    /** Set Assignment Date.
    @param XX_AssignmentDate Assignment Date */
    public void setXX_AssignmentDate (Timestamp XX_AssignmentDate)
    {
        set_Value ("XX_AssignmentDate", XX_AssignmentDate);
        
    }
    
    /** Get Assignment Date.
    @return Assignment Date */
    public Timestamp getXX_AssignmentDate() 
    {
        return (Timestamp)get_Value("XX_AssignmentDate");
        
    }
    
    /** Set Change Products Label.
    @param XX_ChangeProductLabel Cambia el tipo de etiqueta a varios productos de un pedido */
    public void setXX_ChangeProductLabel (String XX_ChangeProductLabel)
    {
        set_Value ("XX_ChangeProductLabel", XX_ChangeProductLabel);
        
    }
    
    /** Get Change Products Label.
    @return Cambia el tipo de etiqueta a varios productos de un pedido */
    public String getXX_ChangeProductLabel() 
    {
        return (String)get_Value("XX_ChangeProductLabel");
        
    }
    
    /** Set Checkup Assistant.
    @param XX_CheckAssistant_ID Checkup Assistant */
    public void setXX_CheckAssistant_ID (int XX_CheckAssistant_ID)
    {
        if (XX_CheckAssistant_ID <= 0) set_Value ("XX_CheckAssistant_ID", null);
        else
        set_Value ("XX_CheckAssistant_ID", Integer.valueOf(XX_CheckAssistant_ID));
        
    }
    
    /** Get Checkup Assistant.
    @return Checkup Assistant */
    public int getXX_CheckAssistant_ID() 
    {
        return get_ValueAsInt("XX_CheckAssistant_ID");
        
    }
    
    /** Set Checkup Auxiliary.
    @param XX_CheckAuxiliary_ID Checkup Auxiliary */
    public void setXX_CheckAuxiliary_ID (int XX_CheckAuxiliary_ID)
    {
        if (XX_CheckAuxiliary_ID <= 0) set_Value ("XX_CheckAuxiliary_ID", null);
        else
        set_Value ("XX_CheckAuxiliary_ID", Integer.valueOf(XX_CheckAuxiliary_ID));
        
    }
    
    /** Get Checkup Auxiliary.
    @return Checkup Auxiliary */
    public int getXX_CheckAuxiliary_ID() 
    {
        return get_ValueAsInt("XX_CheckAuxiliary_ID");
        
    }
    
    /** Set Number of collaborators.
    @param XX_CollaboratorsQty Number of collaborators */
    public void setXX_CollaboratorsQty (int XX_CollaboratorsQty)
    {
        set_Value ("XX_CollaboratorsQty", Integer.valueOf(XX_CollaboratorsQty));
        
    }
    
    /** Get Number of collaborators.
    @return Number of collaborators */
    public int getXX_CollaboratorsQty() 
    {
        return get_ValueAsInt("XX_CollaboratorsQty");
        
    }
    
    /** Set Collection.
    @param XX_CollectionName Collection */
    public void setXX_CollectionName (String XX_CollectionName)
    {
        throw new IllegalArgumentException ("XX_CollectionName is virtual column");
        
    }
    
    /** Get Collection.
    @return Collection */
    public String getXX_CollectionName() 
    {
        return (String)get_Value("XX_CollectionName");
        
    }
    
    /** Set Complete Order Request.
    @param XX_Complete Complete Order Request */
    public void setXX_Complete (String XX_Complete)
    {
        set_Value ("XX_Complete", XX_Complete);
        
    }
    
    /** Get Complete Order Request.
    @return Complete Order Request */
    public String getXX_Complete() 
    {
        return (String)get_Value("XX_Complete");
        
    }
    
    /** Set Complete Order Request.
    @param XX_CompleteDDispatch Complete Order Request */
    public void setXX_CompleteDDispatch (String XX_CompleteDDispatch)
    {
        set_Value ("XX_CompleteDDispatch", XX_CompleteDDispatch);
        
    }
    
    /** Get Complete Order Request.
    @return Complete Order Request */
    public String getXX_CompleteDDispatch() 
    {
        return (String)get_Value("XX_CompleteDDispatch");
        
    }
    
    /** Set Correlative Generator.
    @param XX_CorrelativeGenerator Correlative Generator */
    public void setXX_CorrelativeGenerator (int XX_CorrelativeGenerator)
    {
        set_Value ("XX_CorrelativeGenerator", Integer.valueOf(XX_CorrelativeGenerator));
        
    }
    
    /** Get Correlative Generator.
    @return Correlative Generator */
    public int getXX_CorrelativeGenerator() 
    {
        return get_ValueAsInt("XX_CorrelativeGenerator");
        
    }
    
    /** Set Date Status At Bay.
    @param XX_DateStatusAtBay Date Status At Bay */
    public void setXX_DateStatusAtBay (Timestamp XX_DateStatusAtBay)
    {
        set_Value ("XX_DateStatusAtBay", XX_DateStatusAtBay);
        
    }
    
    /** Get Date Status At Bay.
    @return Date Status At Bay */
    public Timestamp getXX_DateStatusAtBay() 
    {
        return (Timestamp)get_Value("XX_DateStatusAtBay");
        
    }
    
    /** Set Date Status On Store.
    @param XX_DateStatusOnStore Date Status On Store */
    public void setXX_DateStatusOnStore (Timestamp XX_DateStatusOnStore)
    {
        set_Value ("XX_DateStatusOnStore", XX_DateStatusOnStore);
        
    }
    
    /** Get Date Status On Store.
    @return Date Status On Store */
    public Timestamp getXX_DateStatusOnStore() 
    {
        return (Timestamp)get_Value("XX_DateStatusOnStore");
        
    }
    
    /** Set Date Status Pending.
    @param XX_DateStatusPending Date Status Pending */
    public void setXX_DateStatusPending (Timestamp XX_DateStatusPending)
    {
        set_Value ("XX_DateStatusPending", XX_DateStatusPending);
        
    }
    
    /** Get Date Status Pending.
    @return Date Status Pending */
    public Timestamp getXX_DateStatusPending() 
    {
        return (Timestamp)get_Value("XX_DateStatusPending");
        
    }
    
    /** Set Date Status On Transit.
    @param XX_DateStatusTransit Date Status On Transit */
    public void setXX_DateStatusTransit (Timestamp XX_DateStatusTransit)
    {
        set_Value ("XX_DateStatusTransit", XX_DateStatusTransit);
        
    }
    
    /** Get Date Status On Transit.
    @return Date Status On Transit */
    public Timestamp getXX_DateStatusTransit() 
    {
        return (Timestamp)get_Value("XX_DateStatusTransit");
        
    }
    
    /** Set Dispatch Guide.
    @param XX_DispatchGuide_ID Numero de Guia de Despacho de un Pedido */
    public void setXX_DispatchGuide_ID (int XX_DispatchGuide_ID)
    {
        if (XX_DispatchGuide_ID <= 0) set_Value ("XX_DispatchGuide_ID", null);
        else
        set_Value ("XX_DispatchGuide_ID", Integer.valueOf(XX_DispatchGuide_ID));
        
    }
    
    /** Get Dispatch Guide.
    @return Numero de Guia de Despacho de un Pedido */
    public int getXX_DispatchGuide_ID() 
    {
        return get_ValueAsInt("XX_DispatchGuide_ID");
        
    }
    
    /** Set Has Dispath Guide Assigned.
    @param XX_HasDispatchGuide Has Dispath Guide Assigned */
    public void setXX_HasDispatchGuide (boolean XX_HasDispatchGuide)
    {
        throw new IllegalArgumentException ("XX_HasDispatchGuide is virtual column");
        
    }
    
    /** Get Has Dispath Guide Assigned.
    @return Has Dispath Guide Assigned */
    public boolean isXX_HasDispatchGuide() 
    {
        return get_ValueAsBoolean("XX_HasDispatchGuide");
        
    }
    
    /** Set Movement Dispatch.
    @param XX_MovementDispatch_ID Movement Dispatch */
    public void setXX_MovementDispatch_ID (int XX_MovementDispatch_ID)
    {
        if (XX_MovementDispatch_ID <= 0) set_Value ("XX_MovementDispatch_ID", null);
        else
        set_Value ("XX_MovementDispatch_ID", Integer.valueOf(XX_MovementDispatch_ID));
        
    }
    
    /** Get Movement Dispatch.
    @return Movement Dispatch */
    public int getXX_MovementDispatch_ID() 
    {
        return get_ValueAsInt("XX_MovementDispatch_ID");
        
    }
    
    /** Set Movement Request.
    @param XX_MovementRequest_ID Movement Request */
    public void setXX_MovementRequest_ID (int XX_MovementRequest_ID)
    {
        if (XX_MovementRequest_ID <= 0) set_Value ("XX_MovementRequest_ID", null);
        else
        set_Value ("XX_MovementRequest_ID", Integer.valueOf(XX_MovementRequest_ID));
        
    }
    
    /** Get Movement Request.
    @return Movement Request */
    public int getXX_MovementRequest_ID() 
    {
        return get_ValueAsInt("XX_MovementRequest_ID");
        
    }
    
    /** Set Order Beco Correlative.
    @param XX_OrderBecoCorrelative Order Beco Correlative */
    public void setXX_OrderBecoCorrelative (String XX_OrderBecoCorrelative)
    {
        if (XX_OrderBecoCorrelative == null) throw new IllegalArgumentException ("XX_OrderBecoCorrelative is mandatory.");
        set_ValueNoCheck ("XX_OrderBecoCorrelative", XX_OrderBecoCorrelative);
        
    }
    
    /** Get Order Beco Correlative.
    @return Order Beco Correlative */
    public String getXX_OrderBecoCorrelative() 
    {
        return (String)get_Value("XX_OrderBecoCorrelative");
        
    }
    
    /** Set Order Check Date.
    @param XX_OrderCheckDate Order Check Date */
    public void setXX_OrderCheckDate (Timestamp XX_OrderCheckDate)
    {
        throw new IllegalArgumentException ("XX_OrderCheckDate is virtual column");
        
    }
    
    /** Get Order Check Date.
    @return Order Check Date */
    public Timestamp getXX_OrderCheckDate() 
    {
        return (Timestamp)get_Value("XX_OrderCheckDate");
        
    }
    
    /** Set Order Date.
    @param XX_OrderDate Order Date */
    public void setXX_OrderDate (Timestamp XX_OrderDate)
    {
        throw new IllegalArgumentException ("XX_OrderDate is virtual column");
        
    }
    
    /** Get Order Date.
    @return Order Date */
    public Timestamp getXX_OrderDate() 
    {
        return (Timestamp)get_Value("XX_OrderDate");
        
    }
    
    /** Set Order Ready.
    @param XX_OrderReady Order Ready */
    public void setXX_OrderReady (boolean XX_OrderReady)
    {
        set_Value ("XX_OrderReady", Boolean.valueOf(XX_OrderReady));
        
    }
    
    /** Get Order Ready.
    @return Order Ready */
    public boolean isXX_OrderReady() 
    {
        return get_ValueAsBoolean("XX_OrderReady");
        
    }
    
    /** Anulado = AN */
    public static final String XX_ORDERREQUESTSTATUS_Anulado = X_Ref_XX_VMR_OrderStatus.ANULADO.getValue();
    /** En Bahia = EB */
    public static final String XX_ORDERREQUESTSTATUS_EnBahia = X_Ref_XX_VMR_OrderStatus.EN_BAHIA.getValue();
    /** En Tránsito = ET */
    public static final String XX_ORDERREQUESTSTATUS_EnTránsito = X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue();
    /** Por Fijar Precio = FP */
    public static final String XX_ORDERREQUESTSTATUS_PorFijarPrecio = X_Ref_XX_VMR_OrderStatus.POR_FIJAR_PRECIO.getValue();
    /** Por Etiquetar = PE */
    public static final String XX_ORDERREQUESTSTATUS_PorEtiquetar = X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue();
    /** En Tienda = TI */
    public static final String XX_ORDERREQUESTSTATUS_EnTienda = X_Ref_XX_VMR_OrderStatus.EN_TIENDA.getValue();
    /** Set Order Status.
    @param XX_OrderRequestStatus Order Status */
    public void setXX_OrderRequestStatus (String XX_OrderRequestStatus)
    {
        if (XX_OrderRequestStatus == null) throw new IllegalArgumentException ("XX_OrderRequestStatus is mandatory");
        if (!X_Ref_XX_VMR_OrderStatus.isValid(XX_OrderRequestStatus))
        throw new IllegalArgumentException ("XX_OrderRequestStatus Invalid value - " + XX_OrderRequestStatus + " - Reference_ID=1000183 - AN - EB - ET - FP - PE - TI");
        set_Value ("XX_OrderRequestStatus", XX_OrderRequestStatus);
        
    }
    
    /** Get Order Status.
    @return Order Status */
    public String getXX_OrderRequestStatus() 
    {
        return (String)get_Value("XX_OrderRequestStatus");
        
    }
    
    /** Re-Distribución Automática = A */
    public static final String XX_ORDERREQUESTTYPE_Re_DistribuciónAutomática = X_Ref_XX_OrderRequestType.RE__DISTRIBUCIÓN_AUTOMÁTICA.getValue();
    /** Despacho Directo = D */
    public static final String XX_ORDERREQUESTTYPE_DespachoDirecto = X_Ref_XX_OrderRequestType.DESPACHO_DIRECTO.getValue();
    /** Pre-Distribución = P */
    public static final String XX_ORDERREQUESTTYPE_Pre_Distribución = X_Ref_XX_OrderRequestType.PRE__DISTRIBUCIÓN.getValue();
    /** Re-Distribución = R */
    public static final String XX_ORDERREQUESTTYPE_Re_Distribución = X_Ref_XX_OrderRequestType.RE__DISTRIBUCIÓN.getValue();
    /** Set Order Request Type.
    @param XX_OrderRequestType Order Request Type */
    public void setXX_OrderRequestType (String XX_OrderRequestType)
    {
        if (!X_Ref_XX_OrderRequestType.isValid(XX_OrderRequestType))
        throw new IllegalArgumentException ("XX_OrderRequestType Invalid value - " + XX_OrderRequestType + " - Reference_ID=1000206 - A - D - P - R");
        set_Value ("XX_OrderRequestType", XX_OrderRequestType);
        
    }
    
    /** Get Order Request Type.
    @return Order Request Type */
    public String getXX_OrderRequestType() 
    {
        return (String)get_Value("XX_OrderRequestType");
        
    }
    
    /** ANULADA = AN */
    public static final String XX_ORDERSTATUS_ANULADA = X_Ref_XX_OrderStatus.ANULADA.getValue();
    /** APROBADA = AP */
    public static final String XX_ORDERSTATUS_APROBADA = X_Ref_XX_OrderStatus.APROBADA.getValue();
    /** CHEQUEADA = CH */
    public static final String XX_ORDERSTATUS_CHEQUEADA = X_Ref_XX_OrderStatus.CHEQUEADA.getValue();
    /** EN ADUANA = EA */
    public static final String XX_ORDERSTATUS_ENADUANA = X_Ref_XX_OrderStatus.ENADUANA.getValue();
    /** ENTREGADA AL AGENTE DE CARGA = EAC */
    public static final String XX_ORDERSTATUS_ENTREGADAALAGENTEDECARGA = X_Ref_XX_OrderStatus.ENTREGADAALAGENTEDECARGA.getValue();
    /** EN PRODUCCIÓN = EP */
    public static final String XX_ORDERSTATUS_ENPRODUCCIÓN = X_Ref_XX_OrderStatus.ENPRODUCCIÓN.getValue();
    /** EN PROCESO DE NACIONALIZACIÓN = EPN */
    public static final String XX_ORDERSTATUS_ENPROCESODENACIONALIZACIÓN = X_Ref_XX_OrderStatus.ENPROCESODENACIONALIZACIÓN.getValue();
    /** EN TRÁNSITO INTERNACIONAL = ETI */
    public static final String XX_ORDERSTATUS_ENTRÁNSITOINTERNACIONAL = X_Ref_XX_OrderStatus.ENTRÁNSITOINTERNACIONAL.getValue();
    /** EN TRÁNSITO NACIONAL = ETN */
    public static final String XX_ORDERSTATUS_ENTRÁNSITONACIONAL = X_Ref_XX_OrderStatus.ENTRÁNSITONACIONAL.getValue();
    /** LLEGADA A CD = LCD */
    public static final String XX_ORDERSTATUS_LLEGADAACD = X_Ref_XX_OrderStatus.LLEGADAACD.getValue();
    /** LLEGADA A VENEZUELA = LVE */
    public static final String XX_ORDERSTATUS_LLEGADAAVENEZUELA = X_Ref_XX_OrderStatus.LLEGADAAVENEZUELA.getValue();
    /** PENDIENTE = PEN */
    public static final String XX_ORDERSTATUS_PENDIENTE = X_Ref_XX_OrderStatus.PENDIENTE.getValue();
    /** PROFORMA = PRO */
    public static final String XX_ORDERSTATUS_PROFORMA = X_Ref_XX_OrderStatus.PROFORMA.getValue();
    /** RECIBIDA = RE */
    public static final String XX_ORDERSTATUS_RECIBIDA = X_Ref_XX_OrderStatus.RECIBIDA.getValue();
    /** SITME = SIT */
    public static final String XX_ORDERSTATUS_SITME = X_Ref_XX_OrderStatus.SITME.getValue();
    /** Set PO Status.
    @param XX_OrderStatus Estado de la Orden de Compra */
    public void setXX_OrderStatus (String XX_OrderStatus)
    {
        if (!X_Ref_XX_OrderStatus.isValid(XX_OrderStatus))
        throw new IllegalArgumentException ("XX_OrderStatus Invalid value - " + XX_OrderStatus + " - Reference_ID=1000103 - AN - AP - CH - EA - EAC - EP - EPN - ETI - ETN - LCD - LVE - PEN - PRO - RE - SIT");
        throw new IllegalArgumentException ("XX_OrderStatus is virtual column");
        
    }
    
    /** Get PO Status.
    @return Estado de la Orden de Compra */
    public String getXX_OrderStatus() 
    {
        return (String)get_Value("XX_OrderStatus");
        
    }
    
    /** Importada = Importada */
    public static final String XX_ORDERTYPE_Importada = X_Ref_XX_OrderType.IMPORTADA.getValue();
    /** Nacional = Nacional */
    public static final String XX_ORDERTYPE_Nacional = X_Ref_XX_OrderType.NACIONAL.getValue();
    /** Set Order Type.
    @param XX_OrderType Tipo de Orden (Nacional / Internacional) */
    public void setXX_OrderType (String XX_OrderType)
    {
        if (!X_Ref_XX_OrderType.isValid(XX_OrderType))
        throw new IllegalArgumentException ("XX_OrderType Invalid value - " + XX_OrderType + " - Reference_ID=1000049 - Importada - Nacional");
        throw new IllegalArgumentException ("XX_OrderType is virtual column");
        
    }
    
    /** Get Order Type.
    @return Tipo de Orden (Nacional / Internacional) */
    public String getXX_OrderType() 
    {
        return (String)get_Value("XX_OrderType");
        
    }
    
    /** Set Package Quantity.
    @param XX_PackageQuantity Quantity of Packages of an Order Request */
    public void setXX_PackageQuantity (int XX_PackageQuantity)
    {
        set_Value ("XX_PackageQuantity", Integer.valueOf(XX_PackageQuantity));
        
    }
    
    /** Get Package Quantity.
    @return Quantity of Packages of an Order Request */
    public int getXX_PackageQuantity() 
    {
        return get_ValueAsInt("XX_PackageQuantity");
        
    }
    
    /** Set Check Assistant.
    @param XX_POCheckAssistant_ID Check Assistant */
    public void setXX_POCheckAssistant_ID (int XX_POCheckAssistant_ID)
    {
        throw new IllegalArgumentException ("XX_POCheckAssistant_ID is virtual column");
        
    }
    
    /** Get Check Assistant.
    @return Check Assistant */
    public int getXX_POCheckAssistant_ID() 
    {
        return get_ValueAsInt("XX_POCheckAssistant_ID");
        
    }
    
    /** Set Check Auxiliar.
    @param XX_POCheckAuxiliar_ID Check Auxiliar */
    public void setXX_POCheckAuxiliar_ID (int XX_POCheckAuxiliar_ID)
    {
        throw new IllegalArgumentException ("XX_POCheckAuxiliar_ID is virtual column");
        
    }
    
    /** Get Check Auxiliar.
    @return Check Auxiliar */
    public int getXX_POCheckAuxiliar_ID() 
    {
        return get_ValueAsInt("XX_POCheckAuxiliar_ID");
        
    }
    
    /** Set Print Consolidate Distribution Document.
    @param XX_PrintConsolidateDoc Print Consolidate Distribution Document */
    public void setXX_PrintConsolidateDoc (String XX_PrintConsolidateDoc)
    {
        set_Value ("XX_PrintConsolidateDoc", XX_PrintConsolidateDoc);
        
    }
    
    /** Get Print Consolidate Distribution Document.
    @return Print Consolidate Distribution Document */
    public String getXX_PrintConsolidateDoc() 
    {
        return (String)get_Value("XX_PrintConsolidateDoc");
        
    }
    
    /** Set Print Label.
    @param XX_PrintLabel Imprime las Etiquetas Beco */
    public void setXX_PrintLabel (String XX_PrintLabel)
    {
        set_Value ("XX_PrintLabel", XX_PrintLabel);
        
    }
    
    /** Get Print Label.
    @return Imprime las Etiquetas Beco */
    public String getXX_PrintLabel() 
    {
        return (String)get_Value("XX_PrintLabel");
        
    }
    
    /** Set Print Label.
    @param XX_PrintLabelDD Imprime las Etiquetas Beco */
    public void setXX_PrintLabelDD (String XX_PrintLabelDD)
    {
        set_Value ("XX_PrintLabelDD", XX_PrintLabelDD);
        
    }
    
    /** Get Print Label.
    @return Imprime las Etiquetas Beco */
    public String getXX_PrintLabelDD() 
    {
        return (String)get_Value("XX_PrintLabelDD");
        
    }
    
    /** Set Print Physical Distribution Doc..
    @param XX_PrintPhysicalDistribution Print Physical Distribution Doc. */
    public void setXX_PrintPhysicalDistribution (String XX_PrintPhysicalDistribution)
    {
        set_Value ("XX_PrintPhysicalDistribution", XX_PrintPhysicalDistribution);
        
    }
    
    /** Get Print Physical Distribution Doc..
    @return Print Physical Distribution Doc. */
    public String getXX_PrintPhysicalDistribution() 
    {
        return (String)get_Value("XX_PrintPhysicalDistribution");
        
    }
    
    /** Set Synchronized.
    @param XX_Synchronized Indica si el registro ya fue exportado */
    public void setXX_Synchronized (boolean XX_Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(XX_Synchronized));
        
    }
    
    /** Get Synchronized.
    @return Indica si el registro ya fue exportado */
    public boolean isXX_Synchronized() 
    {
        return get_ValueAsBoolean("XX_Synchronized");
        
    }
    
    /** Set Total Pieces Qty.
    @param XX_TotalPiecesQty Total Pieces Qty */
    public void setXX_TotalPiecesQty (int XX_TotalPiecesQty)
    {
        throw new IllegalArgumentException ("XX_TotalPiecesQty is virtual column");
        
    }
    
    /** Get Total Pieces Qty.
    @return Total Pieces Qty */
    public int getXX_TotalPiecesQty() 
    {
        return get_ValueAsInt("XX_TotalPiecesQty");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Department_ID is virtual column");
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        if (XX_VMR_DistributionHeader_ID <= 0) set_ValueNoCheck ("XX_VMR_DistributionHeader_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_DistributionHeader_ID", Integer.valueOf(XX_VMR_DistributionHeader_ID));
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set Placed Order.
    @param XX_VMR_Order_ID Placed Order */
    public void setXX_VMR_Order_ID (int XX_VMR_Order_ID)
    {
        if (XX_VMR_Order_ID < 1) throw new IllegalArgumentException ("XX_VMR_Order_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Order_ID", Integer.valueOf(XX_VMR_Order_ID));
        
    }
    
    /** Get Placed Order.
    @return Placed Order */
    public int getXX_VMR_Order_ID() 
    {
        return get_ValueAsInt("XX_VMR_Order_ID");
        
    }
    
    /** Set Warehouse Assistant.
    @param XX_WarehouseAssistant_ID Warehouse Assistant */
    public void setXX_WarehouseAssistant_ID (int XX_WarehouseAssistant_ID)
    {
        if (XX_WarehouseAssistant_ID <= 0) set_Value ("XX_WarehouseAssistant_ID", null);
        else
        set_Value ("XX_WarehouseAssistant_ID", Integer.valueOf(XX_WarehouseAssistant_ID));
        
    }
    
    /** Get Warehouse Assistant.
    @return Warehouse Assistant */
    public int getXX_WarehouseAssistant_ID() 
    {
        return get_ValueAsInt("XX_WarehouseAssistant_ID");
        
    }
    
    /** Set Week Created.
    @param XX_WeekCreated Week Created */
    public void setXX_WeekCreated (int XX_WeekCreated)
    {
        set_Value ("XX_WeekCreated", Integer.valueOf(XX_WeekCreated));
        
    }
    
    /** Get Week Created.
    @return Week Created */
    public int getXX_WeekCreated() 
    {
        return get_ValueAsInt("XX_WeekCreated");
        
    }
    
    
}
