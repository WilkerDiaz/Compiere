/**
 * Example Table DDL SQL
 *
 */
CREATE TABLE FSRV_Visit(
    FSRV_Visit_ID             NUMBER(10, 0)     NOT NULL,
    AD_Client_ID              NUMBER(10, 0)     NOT NULL,
    AD_Org_ID                 NUMBER(10, 0)     NOT NULL,
    IsActive                  CHAR(1)           DEFAULT 'Y' NOT NULL,
    IsApproved                CHAR(1)           DEFAULT 'N' NOT NULL,
    ApprovalAmt		      NUMBER,
    Posted                    CHAR(1)           DEFAULT 'N' NOT NULL,
    Created                   DATE              DEFAULT SYSDATE NOT NULL,
    CreatedBy                 NUMBER(10, 0)     NOT NULL,
    DocumentNo	              NVARCHAR2(30)	NOT NULL,
    C_DocType_ID	      NUMBER(10, 0)     NOT NULL,
    Updated                   DATE              DEFAULT SYSDATE NOT NULL,
    UpdatedBy                 NUMBER(10, 0)     NOT NULL,
    Description               NVARCHAR2(255),
    VisitTime                 DATE              NOT NULL,
    Minutes                   NUMBER(10, 0)     NOT NULL,
    C_BPartner_ID             NUMBER(10, 0)     NOT NULL,
    C_BPartner_Location_ID    NUMBER(10, 0)     NOT NULL,
    AD_User_ID                NUMBER(10, 0)     NOT NULL,
    SalesRep_ID               NUMBER(10, 0)     NOT NULL,
    R_InterestArea_ID         NUMBER(10, 0)     NOT NULL,
    Processed                 CHAR(1),
    Processing                CHAR(1),
    DocStatus		      CHAR(2)		NOT NULL,
    DocAction		      CHAR(2)		NOT NULL
) 
/