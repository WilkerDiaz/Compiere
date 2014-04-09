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
/** Generated Model for XX_VLO_DispatchGuide
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_DispatchGuide extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_DispatchGuide_ID id
    @param trx transaction
    */
    public X_XX_VLO_DispatchGuide (Ctx ctx, int XX_VLO_DispatchGuide_ID, Trx trx)
    {
        super (ctx, XX_VLO_DispatchGuide_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_DispatchGuide_ID == 0)
        {
            setDocumentNo (null);
            setXX_DispatchDate (new Timestamp(System.currentTimeMillis()));
            setXX_VLO_DispatchGuide_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_DispatchGuide (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27631997633789L;
    /** Last Updated Timestamp 2012-10-10 08:01:57.0 */
    public static final long updatedMS = 1349872317000L;
    /** AD_Table_ID=1000289 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_DispatchGuide");
        
    }
    ;
    
    /** TableName=XX_VLO_DispatchGuide */
    public static final String Table_Name="XX_VLO_DispatchGuide";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
    }
    
    /** Set Alert.
    @param XX_Alert Alert */
    public void setXX_Alert (boolean XX_Alert)
    {
        set_Value ("XX_Alert", Boolean.valueOf(XX_Alert));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert() 
    {
        return get_ValueAsBoolean("XX_Alert");
        
    }
    
    /** Set Approve Dispatch Guide.
    @param XX_ApproveDispatchGuide Approve Dispatch Guide */
    public void setXX_ApproveDispatchGuide (String XX_ApproveDispatchGuide)
    {
        set_Value ("XX_ApproveDispatchGuide", XX_ApproveDispatchGuide);
        
    }
    
    /** Get Approve Dispatch Guide.
    @return Approve Dispatch Guide */
    public String getXX_ApproveDispatchGuide() 
    {
        return (String)get_Value("XX_ApproveDispatchGuide");
        
    }
    
    /** Set Arrival City.
    @param XX_ArrivalCity Arrival City */
    public void setXX_ArrivalCity (String XX_ArrivalCity)
    {
        throw new IllegalArgumentException ("XX_ArrivalCity is virtual column");
        
    }
    
    /** Get Arrival City.
    @return Arrival City */
    public String getXX_ArrivalCity() 
    {
        return (String)get_Value("XX_ArrivalCity");
        
    }
    
    /** Set Arrival Warehouse.
    @param XX_ArrivalWarehouse_ID Arrival Warehouse */
    public void setXX_ArrivalWarehouse_ID (int XX_ArrivalWarehouse_ID)
    {
        if (XX_ArrivalWarehouse_ID <= 0) set_Value ("XX_ArrivalWarehouse_ID", null);
        else
        set_Value ("XX_ArrivalWarehouse_ID", Integer.valueOf(XX_ArrivalWarehouse_ID));
        
    }
    
    /** Get Arrival Warehouse.
    @return Arrival Warehouse */
    public int getXX_ArrivalWarehouse_ID() 
    {
        return get_ValueAsInt("XX_ArrivalWarehouse_ID");
        
    }
    
    /** Set Assistant1 CI.
    @param XX_Assistant1_CI Assistant1 CI */
    public void setXX_Assistant1_CI (String XX_Assistant1_CI)
    {
        throw new IllegalArgumentException ("XX_Assistant1_CI is virtual column");
        
    }
    
    /** Get Assistant1 CI.
    @return Assistant1 CI */
    public String getXX_Assistant1_CI() 
    {
        return (String)get_Value("XX_Assistant1_CI");
        
    }
    
    /** Set First Assistant.
    @param XX_Assistant1_ID First Assistant */
    public void setXX_Assistant1_ID (int XX_Assistant1_ID)
    {
        if (XX_Assistant1_ID <= 0) set_Value ("XX_Assistant1_ID", null);
        else
        set_Value ("XX_Assistant1_ID", Integer.valueOf(XX_Assistant1_ID));
        
    }
    
    /** Get First Assistant.
    @return First Assistant */
    public int getXX_Assistant1_ID() 
    {
        return get_ValueAsInt("XX_Assistant1_ID");
        
    }
    
    /** Set Second Assistant.
    @param XX_Assistant2_ID Second Assistant */
    public void setXX_Assistant2_ID (int XX_Assistant2_ID)
    {
        if (XX_Assistant2_ID <= 0) set_Value ("XX_Assistant2_ID", null);
        else
        set_Value ("XX_Assistant2_ID", Integer.valueOf(XX_Assistant2_ID));
        
    }
    
    /** Get Second Assistant.
    @return Second Assistant */
    public int getXX_Assistant2_ID() 
    {
        return get_ValueAsInt("XX_Assistant2_ID");
        
    }
    
    /** Set Car Plate.
    @param XX_CarPlate Car Plate */
    public void setXX_CarPlate (String XX_CarPlate)
    {
        throw new IllegalArgumentException ("XX_CarPlate is virtual column");
        
    }
    
    /** Get Car Plate.
    @return Car Plate */
    public String getXX_CarPlate() 
    {
        return (String)get_Value("XX_CarPlate");
        
    }
    
    /** Set Date After Dispatch.
    @param XX_DateAfterDispatch Date After Dispatch */
    public void setXX_DateAfterDispatch (Timestamp XX_DateAfterDispatch)
    {
        set_Value ("XX_DateAfterDispatch", XX_DateAfterDispatch);
        
    }
    
    /** Get Date After Dispatch.
    @return Date After Dispatch */
    public Timestamp getXX_DateAfterDispatch() 
    {
        return (Timestamp)get_Value("XX_DateAfterDispatch");
        
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
    
    /** Set Distribution Center Movement.
    @param XX_DCMovement Distribution Center Movement */
    public void setXX_DCMovement (boolean XX_DCMovement)
    {
        set_Value ("XX_DCMovement", Boolean.valueOf(XX_DCMovement));
        
    }
    
    /** Get Distribution Center Movement.
    @return Distribution Center Movement */
    public boolean isXX_DCMovement() 
    {
        return get_ValueAsBoolean("XX_DCMovement");
        
    }
    
    /** Set Departure City.
    @param XX_DepartureCity Departure City */
    public void setXX_DepartureCity (String XX_DepartureCity)
    {
        throw new IllegalArgumentException ("XX_DepartureCity is virtual column");
        
    }
    
    /** Get Departure City.
    @return Departure City */
    public String getXX_DepartureCity() 
    {
        return (String)get_Value("XX_DepartureCity");
        
    }
    
    /** Set Departure Warehouse.
    @param XX_DepartureWarehouse_ID Departure Warehouse */
    public void setXX_DepartureWarehouse_ID (int XX_DepartureWarehouse_ID)
    {
        if (XX_DepartureWarehouse_ID <= 0) set_Value ("XX_DepartureWarehouse_ID", null);
        else
        set_Value ("XX_DepartureWarehouse_ID", Integer.valueOf(XX_DepartureWarehouse_ID));
        
    }
    
    /** Get Departure Warehouse.
    @return Departure Warehouse */
    public int getXX_DepartureWarehouse_ID() 
    {
        return get_ValueAsInt("XX_DepartureWarehouse_ID");
        
    }
    
    /** Set Dispatch Date.
    @param XX_DispatchDate Dispatch Date */
    public void setXX_DispatchDate (Timestamp XX_DispatchDate)
    {
        if (XX_DispatchDate == null) throw new IllegalArgumentException ("XX_DispatchDate is mandatory.");
        set_Value ("XX_DispatchDate", XX_DispatchDate);
        
    }
    
    /** Get Dispatch Date.
    @return Dispatch Date */
    public Timestamp getXX_DispatchDate() 
    {
        return (Timestamp)get_Value("XX_DispatchDate");
        
    }
    
    /** Pendiente = PEN */
    public static final String XX_DISPATCHGUIDESTATUS_Pendiente = X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue();
    /** Sugerido = SUG */
    public static final String XX_DISPATCHGUIDESTATUS_Sugerido = X_Ref_XX_Ref_DispatchGuideStatus.SUGERIDO.getValue();
    /** En Tienda = TIE */
    public static final String XX_DISPATCHGUIDESTATUS_EnTienda = X_Ref_XX_Ref_DispatchGuideStatus.EN_TIENDA.getValue();
    /** En Tránsito = TRA */
    public static final String XX_DISPATCHGUIDESTATUS_EnTránsito = X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue();
    /** Set Dispatch Guide Status.
    @param XX_DispatchGuideStatus Dispatch Guide Status */
    public void setXX_DispatchGuideStatus (String XX_DispatchGuideStatus)
    {
        if (!X_Ref_XX_Ref_DispatchGuideStatus.isValid(XX_DispatchGuideStatus))
        throw new IllegalArgumentException ("XX_DispatchGuideStatus Invalid value - " + XX_DispatchGuideStatus + " - Reference_ID=1000242 - PEN - SUG - TIE - TRA");
        set_Value ("XX_DispatchGuideStatus", XX_DispatchGuideStatus);
        
    }
    
    /** Get Dispatch Guide Status.
    @return Dispatch Guide Status */
    public String getXX_DispatchGuideStatus() 
    {
        return (String)get_Value("XX_DispatchGuideStatus");
        
    }
    
    /** Set Driver CI.
    @param XX_DriverCI Driver CI */
    public void setXX_DriverCI (String XX_DriverCI)
    {
        throw new IllegalArgumentException ("XX_DriverCI is virtual column");
        
    }
    
    /** Get Driver CI.
    @return Driver CI */
    public String getXX_DriverCI() 
    {
        return (String)get_Value("XX_DriverCI");
        
    }
    
    /** Set Driver.
    @param XX_Driver_ID Driver */
    public void setXX_Driver_ID (int XX_Driver_ID)
    {
        if (XX_Driver_ID <= 0) set_Value ("XX_Driver_ID", null);
        else
        set_Value ("XX_Driver_ID", Integer.valueOf(XX_Driver_ID));
        
    }
    
    /** Get Driver.
    @return Driver */
    public int getXX_Driver_ID() 
    {
        return get_ValueAsInt("XX_Driver_ID");
        
    }
    
    /** Set Kilometers Planned.
    @param XX_KilometersPlanned Kilometers Planned */
    public void setXX_KilometersPlanned (java.math.BigDecimal XX_KilometersPlanned)
    {
        set_Value ("XX_KilometersPlanned", XX_KilometersPlanned);
        
    }
    
    /** Get Kilometers Planned.
    @return Kilometers Planned */
    public java.math.BigDecimal getXX_KilometersPlanned() 
    {
        return get_ValueAsBigDecimal("XX_KilometersPlanned");
        
    }
    
    /** Set KM After Dispatch.
    @param XX_KMAfterDispatch KM After Dispatch */
    public void setXX_KMAfterDispatch (java.math.BigDecimal XX_KMAfterDispatch)
    {
        set_Value ("XX_KMAfterDispatch", XX_KMAfterDispatch);
        
    }
    
    /** Get KM After Dispatch.
    @return KM After Dispatch */
    public java.math.BigDecimal getXX_KMAfterDispatch() 
    {
        return get_ValueAsBigDecimal("XX_KMAfterDispatch");
        
    }
    
    /** Set KM Before Dispatch.
    @param XX_KMBeforeDispatch KM Before Dispatch */
    public void setXX_KMBeforeDispatch (java.math.BigDecimal XX_KMBeforeDispatch)
    {
        set_Value ("XX_KMBeforeDispatch", XX_KMBeforeDispatch);
        
    }
    
    /** Get KM Before Dispatch.
    @return KM Before Dispatch */
    public java.math.BigDecimal getXX_KMBeforeDispatch() 
    {
        return get_ValueAsBigDecimal("XX_KMBeforeDispatch");
        
    }
    
    /** Set Maximum Weight.
    @param XX_MaximumWeight Maximum Weight */
    public void setXX_MaximumWeight (java.math.BigDecimal XX_MaximumWeight)
    {
        throw new IllegalArgumentException ("XX_MaximumWeight is virtual column");
        
    }
    
    /** Get Maximum Weight.
    @return Maximum Weight */
    public java.math.BigDecimal getXX_MaximumWeight() 
    {
        return get_ValueAsBigDecimal("XX_MaximumWeight");
        
    }
    
    /** Set Number of Axles.
    @param XX_NumberOfAxles Number of Axles */
    public void setXX_NumberOfAxles (int XX_NumberOfAxles)
    {
        throw new IllegalArgumentException ("XX_NumberOfAxles is virtual column");
        
    }
    
    /** Get Number of Axles.
    @return Number of Axles */
    public int getXX_NumberOfAxles() 
    {
        return get_ValueAsInt("XX_NumberOfAxles");
        
    }
    
    /** Set Number Of Document.
    @param XX_NumberOfDocument Number Of Document */
    public void setXX_NumberOfDocument (String XX_NumberOfDocument)
    {
        throw new IllegalArgumentException ("XX_NumberOfDocument is virtual column");
        
    }
    
    /** Get Number Of Document.
    @return Number Of Document */
    public String getXX_NumberOfDocument() 
    {
        return (String)get_Value("XX_NumberOfDocument");
        
    }
    
    /** Set Observation.
    @param XX_Observation Observaciones */
    public void setXX_Observation (String XX_Observation)
    {
        set_Value ("XX_Observation", XX_Observation);
        
    }
    
    /** Get Observation.
    @return Observaciones */
    public String getXX_Observation() 
    {
        return (String)get_Value("XX_Observation");
        
    }
    
    /** Set Other Goods Total Packages.
    @param XX_OtherGoodsTotalPackages Other Goods Total Packages */
    public void setXX_OtherGoodsTotalPackages (String XX_OtherGoodsTotalPackages)
    {
        throw new IllegalArgumentException ("XX_OtherGoodsTotalPackages is virtual column");
        
    }
    
    /** Get Other Goods Total Packages.
    @return Other Goods Total Packages */
    public String getXX_OtherGoodsTotalPackages() 
    {
        return (String)get_Value("XX_OtherGoodsTotalPackages");
        
    }
    
    /** Set Percentage Occupancy Vehicle.
    @param XX_PercentageOccupancyVehicle Percentage Occupancy Vehicle */
    public void setXX_PercentageOccupancyVehicle (java.math.BigDecimal XX_PercentageOccupancyVehicle)
    {
        set_Value ("XX_PercentageOccupancyVehicle", XX_PercentageOccupancyVehicle);
        
    }
    
    /** Get Percentage Occupancy Vehicle.
    @return Percentage Occupancy Vehicle */
    public java.math.BigDecimal getXX_PercentageOccupancyVehicle() 
    {
        return get_ValueAsBigDecimal("XX_PercentageOccupancyVehicle");
        
    }
    
    /** Set Placed Order Total Package.
    @param XX_PlacedOrderTotalPackage Placed Order Total Package */
    public void setXX_PlacedOrderTotalPackage (int XX_PlacedOrderTotalPackage)
    {
        throw new IllegalArgumentException ("XX_PlacedOrderTotalPackage is virtual column");
        
    }
    
    /** Get Placed Order Total Package.
    @return Placed Order Total Package */
    public int getXX_PlacedOrderTotalPackage() 
    {
        return get_ValueAsInt("XX_PlacedOrderTotalPackage");
        
    }
    
    /** Set XX_PrintDispatchGuideMov.
    @param XX_PrintDispatchGuideMov XX_PrintDispatchGuideMov */
    public void setXX_PrintDispatchGuideMov (String XX_PrintDispatchGuideMov)
    {
        set_Value ("XX_PrintDispatchGuideMov", XX_PrintDispatchGuideMov);
        
    }
    
    /** Get XX_PrintDispatchGuideMov.
    @return XX_PrintDispatchGuideMov */
    public String getXX_PrintDispatchGuideMov() 
    {
        return (String)get_Value("XX_PrintDispatchGuideMov");
        
    }
    
    /** Set Receive Dispatch Guide.
    @param XX_ReceiveDispatchGuide Receive Dispatch Guide */
    public void setXX_ReceiveDispatchGuide (String XX_ReceiveDispatchGuide)
    {
        set_Value ("XX_ReceiveDispatchGuide", XX_ReceiveDispatchGuide);
        
    }
    
    /** Get Receive Dispatch Guide.
    @return Receive Dispatch Guide */
    public String getXX_ReceiveDispatchGuide() 
    {
        return (String)get_Value("XX_ReceiveDispatchGuide");
        
    }
    
    /** Set Return Total Packages.
    @param XX_ReturnTotalPackages Return Total Packages */
    public void setXX_ReturnTotalPackages (String XX_ReturnTotalPackages)
    {
        throw new IllegalArgumentException ("XX_ReturnTotalPackages is virtual column");
        
    }
    
    /** Get Return Total Packages.
    @return Return Total Packages */
    public String getXX_ReturnTotalPackages() 
    {
        return (String)get_Value("XX_ReturnTotalPackages");
        
    }
    
    /** Set Scan Packages.
    @param XX_ScanPackages Scan Packages */
    public void setXX_ScanPackages (String XX_ScanPackages)
    {
        set_Value ("XX_ScanPackages", XX_ScanPackages);
        
    }
    
    /** Get Scan Packages.
    @return Scan Packages */
    public String getXX_ScanPackages() 
    {
        return (String)get_Value("XX_ScanPackages");
        
    }
    
    /** Set Security Precinct.
    @param XX_SecurityPrecinct Security Precinct */
    public void setXX_SecurityPrecinct (int XX_SecurityPrecinct)
    {
        set_Value ("XX_SecurityPrecinct", Integer.valueOf(XX_SecurityPrecinct));
        
    }
    
    /** Get Security Precinct.
    @return Security Precinct */
    public int getXX_SecurityPrecinct() 
    {
        return get_ValueAsInt("XX_SecurityPrecinct");
        
    }
    
    /** Set Show Product Return.
    @param XX_ShowProductReturn Show Product Return */
    public void setXX_ShowProductReturn (String XX_ShowProductReturn)
    {
        set_Value ("XX_ShowProductReturn", XX_ShowProductReturn);
        
    }
    
    /** Get Show Product Return.
    @return Show Product Return */
    public String getXX_ShowProductReturn() 
    {
        return (String)get_Value("XX_ShowProductReturn");
        
    }
    
    /** Set Show Product Transfer.
    @param XX_ShowProductTransfer Show Product Transfer */
    public void setXX_ShowProductTransfer (String XX_ShowProductTransfer)
    {
        set_Value ("XX_ShowProductTransfer", XX_ShowProductTransfer);
        
    }
    
    /** Get Show Product Transfer.
    @return Show Product Transfer */
    public String getXX_ShowProductTransfer() 
    {
        return (String)get_Value("XX_ShowProductTransfer");
        
    }
    
    /** Set XX_ShowRepDispatGuide.
    @param XX_ShowRepDispatGuide XX_ShowRepDispatGuide */
    public void setXX_ShowRepDispatGuide (String XX_ShowRepDispatGuide)
    {
        set_Value ("XX_ShowRepDispatGuide", XX_ShowRepDispatGuide);
        
    }
    
    /** Get XX_ShowRepDispatGuide.
    @return XX_ShowRepDispatGuide */
    public String getXX_ShowRepDispatGuide() 
    {
        return (String)get_Value("XX_ShowRepDispatGuide");
        
    }
    
    /** Set XX_ShowRepExitAutho.
    @param XX_ShowRepExitAutho XX_ShowRepExitAutho */
    public void setXX_ShowRepExitAutho (String XX_ShowRepExitAutho)
    {
        set_Value ("XX_ShowRepExitAutho", XX_ShowRepExitAutho);
        
    }
    
    /** Get XX_ShowRepExitAutho.
    @return XX_ShowRepExitAutho */
    public String getXX_ShowRepExitAutho() 
    {
        return (String)get_Value("XX_ShowRepExitAutho");
        
    }
    
    /** Set XX_ShowRepPhysiDistri.
    @param XX_ShowRepPhysiDistri XX_ShowRepPhysiDistri */
    public void setXX_ShowRepPhysiDistri (String XX_ShowRepPhysiDistri)
    {
        set_Value ("XX_ShowRepPhysiDistri", XX_ShowRepPhysiDistri);
        
    }
    
    /** Get XX_ShowRepPhysiDistri.
    @return XX_ShowRepPhysiDistri */
    public String getXX_ShowRepPhysiDistri() 
    {
        return (String)get_Value("XX_ShowRepPhysiDistri");
        
    }
    
    /** Set Total Packages.
    @param XX_TotalPackages Total Packages */
    public void setXX_TotalPackages (int XX_TotalPackages)
    {
        set_Value ("XX_TotalPackages", Integer.valueOf(XX_TotalPackages));
        
    }
    
    /** Get Total Packages.
    @return Total Packages */
    public int getXX_TotalPackages() 
    {
        return get_ValueAsInt("XX_TotalPackages");
        
    }
    
    /** Set Total Packages Receive.
    @param XX_TotalPackagesReceive Total Packages Receive */
    public void setXX_TotalPackagesReceive (int XX_TotalPackagesReceive)
    {
        set_Value ("XX_TotalPackagesReceive", Integer.valueOf(XX_TotalPackagesReceive));
        
    }
    
    /** Get Total Packages Receive.
    @return Total Packages Receive */
    public int getXX_TotalPackagesReceive() 
    {
        return get_ValueAsInt("XX_TotalPackagesReceive");
        
    }
    
    /** Set Total Packages Sent.
    @param XX_TotalPackagesSent Total Packages Sent */
    public void setXX_TotalPackagesSent (int XX_TotalPackagesSent)
    {
        set_Value ("XX_TotalPackagesSent", Integer.valueOf(XX_TotalPackagesSent));
        
    }
    
    /** Get Total Packages Sent.
    @return Total Packages Sent */
    public int getXX_TotalPackagesSent() 
    {
        return get_ValueAsInt("XX_TotalPackagesSent");
        
    }
    
    /** Set Tranfer Total Packages.
    @param XX_TranfersTotalPackages Tranfer Total Packages */
    public void setXX_TranfersTotalPackages (String XX_TranfersTotalPackages)
    {
        throw new IllegalArgumentException ("XX_TranfersTotalPackages is virtual column");
        
    }
    
    /** Get Tranfer Total Packages.
    @return Tranfer Total Packages */
    public String getXX_TranfersTotalPackages() 
    {
        return (String)get_Value("XX_TranfersTotalPackages");
        
    }
    
    /** Set AS400 User Assistant.
    @param XX_UserAssistant AS400 User Assistant */
    public void setXX_UserAssistant (String XX_UserAssistant)
    {
        throw new IllegalArgumentException ("XX_UserAssistant is virtual column");
        
    }
    
    /** Get AS400 User Assistant.
    @return AS400 User Assistant */
    public String getXX_UserAssistant() 
    {
        return (String)get_Value("XX_UserAssistant");
        
    }
    
    /** Set Dispatch Guide.
    @param XX_VLO_DispatchGuide_ID Dispatch Guide */
    public void setXX_VLO_DispatchGuide_ID (int XX_VLO_DispatchGuide_ID)
    {
        if (XX_VLO_DispatchGuide_ID < 1) throw new IllegalArgumentException ("XX_VLO_DispatchGuide_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_DispatchGuide_ID", Integer.valueOf(XX_VLO_DispatchGuide_ID));
        
    }
    
    /** Get Dispatch Guide.
    @return Dispatch Guide */
    public int getXX_VLO_DispatchGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_DispatchGuide_ID");
        
    }
    
    /** Set Fleet.
    @param XX_VLO_Fleet_ID Fleet */
    public void setXX_VLO_Fleet_ID (int XX_VLO_Fleet_ID)
    {
        if (XX_VLO_Fleet_ID <= 0) set_Value ("XX_VLO_Fleet_ID", null);
        else
        set_Value ("XX_VLO_Fleet_ID", Integer.valueOf(XX_VLO_Fleet_ID));
        
    }
    
    /** Get Fleet.
    @return Fleet */
    public int getXX_VLO_Fleet_ID() 
    {
        return get_ValueAsInt("XX_VLO_Fleet_ID");
        
    }
    
    /** Set Travel.
    @param XX_VLO_Travel_ID Travel */
    public void setXX_VLO_Travel_ID (int XX_VLO_Travel_ID)
    {
        if (XX_VLO_Travel_ID <= 0) set_Value ("XX_VLO_Travel_ID", null);
        else
        set_Value ("XX_VLO_Travel_ID", Integer.valueOf(XX_VLO_Travel_ID));
        
    }
    
    /** Get Travel.
    @return Travel */
    public int getXX_VLO_Travel_ID() 
    {
        return get_ValueAsInt("XX_VLO_Travel_ID");
        
    }
    
    
}
