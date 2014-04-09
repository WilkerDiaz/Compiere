/**
 * Dictionary Views
 * Author Jorg Janke
 */

CREATE OR REPLACE VIEW CP_PK
AS
SELECT   'PK' || t.AD_Table_ID AS ConstraintName,
    t.TableName, c.ColumnName, 0 AS SeqNo,
    t.AD_Table_ID, c.AD_Column_ID,
    NULL AS PK_Column_ID, 
    NULL AS PK_TableName, NULL AS PK_ColumnName
FROM AD_Table t
INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)
WHERE c.IsKey='Y' AND t.IsView='N' AND c.IsParent='N' 
UNION
SELECT 'PK' || t.AD_Table_ID AS ConstraintName, 
    t.TableName, c.ColumnName, c.SeqNo, 
    t.AD_Table_ID, c.AD_Column_ID,
    cPK.AD_Column_ID AS PK_Column_ID, 
    tPK.TableName AS PK_TableName, cPK.ColumnName AS PK_ColumnName
FROM AD_Table t
  INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)
  LEFT OUTER JOIN AD_Column cPK ON (c.AD_Element_ID=cPK.AD_Element_ID AND cPK.IsKey='Y')
  LEFT OUTER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID AND tPK.IsView='N')
WHERE c.IsParent='Y' AND t.IsView='N'
  AND t.AD_Table_ID NOT IN (SELECT AD_Table_ID FROM AD_Column WHERE IsKey='Y')
  AND ((tPK.TableName IS NULL AND cPK.ColumnName IS NULL) 
  OR (tPK.TableName IS NOT NULL AND cPK.ColumnName IS NOT NULL))
/

CREATE OR REPLACE VIEW CP_FK
AS
SELECT 'FK' || t.AD_Table_ID || '_' || c.AD_Column_ID AS ConstraintName,
    t.TableName, c.ColumnName, 
    t.AD_Table_ID, c.AD_Column_ID,
    cPK.AD_Column_ID AS PK_Column_ID, cPK.AD_Table_ID AS PK_Table_ID,
    tPK.TableName AS PK_TableName, cPK.ColumnName AS PK_ColumnName, c.ConstraintType,
    c.AD_CLIENT_ID, c.AD_ORG_ID, c.ISACTIVE, c.CREATED, c.CREATEDBY, c.UPDATED, c.UPDATEDBY
FROM AD_Table t
  INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)
  INNER JOIN AD_Column cPK ON (c.AD_Element_ID=cPK.AD_Element_ID AND cPK.IsKey='Y')
  INNER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID AND tPK.IsView='N')
WHERE c.IsKey='N' AND c.AD_Reference_Value_ID IS NULL
  AND t.IsView='N' AND c.ColumnSQL IS NULL
UNION   -- References
SELECT 'FK' || t.AD_Table_ID || '_' || c.AD_Column_ID AS ConstraintName,
    t.TableName, c.ColumnName, 
    t.AD_Table_ID, c.AD_Column_ID,
    cPK.AD_Column_ID AS PK_Column_ID, cPK.AD_Table_ID AS PK_Table_ID,
    tPK.TableName AS PK_TableName, cPK.ColumnName AS PK_ColumnName, c.ConstraintType,
    c.AD_CLIENT_ID, c.AD_ORG_ID, c.ISACTIVE, c.CREATED, c.CREATEDBY, c.UPDATED, c.UPDATEDBY
FROM AD_Table t
  INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID AND c.AD_Reference_Value_ID IS NOT NULL)
  INNER JOIN AD_Ref_Table rt ON (c.AD_Reference_Value_ID=rt.AD_Reference_ID)
  INNER JOIN AD_Column cPK ON (rt.Column_Key_ID=cPK.AD_Column_ID)
  INNER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID)
WHERE c.IsKey='N' 
  AND t.IsView='N' AND c.ColumnSQL IS NULL
UNION   --  Accounts
SELECT 'FK' || t.AD_Table_ID || '_' || c.AD_Column_ID AS ConstraintName,
    t.TableName, c.ColumnName, 
    t.AD_Table_ID, c.AD_Column_ID,
    cPK.AD_Column_ID AS PK_Column_ID, cPK.AD_Table_ID AS PK_Table_ID,
    tPK.TableName AS PK_TableName, cPK.ColumnName AS PK_ColumnName, c.ConstraintType,
    c.AD_CLIENT_ID, c.AD_ORG_ID, c.ISACTIVE, c.CREATED, c.CREATEDBY, c.UPDATED, c.UPDATEDBY
FROM AD_Table t
  INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID AND c.AD_Reference_ID = 25)
  INNER JOIN AD_Column cPK ON (cPK.AD_Column_ID=1014)
  INNER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID)
WHERE c.IsKey='N' 
  AND t.IsView='N' AND c.ColumnSQL IS NULL
/

CREATE OR REPLACE VIEW CP_DEP
AS
SELECT t.TableName, t.AD_Table_ID, c.ColumnName, c.AD_Column_ID,
    tt.TableName AS Dependent_TableName, tt.AD_Table_ID AS Dependent_Table_ID,
    cc.ColumnName AS Dependent_ColumnName, cc.AD_Column_ID AS Dependent_Column_ID
FROM AD_Table t
  INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID AND c.IsKey='Y')
  INNER JOIN AD_Column cc ON (c.AD_Element_ID=cc.AD_Element_ID AND cc.IsParent='Y')
  INNER JOIN AD_Table tt ON (cc.AD_Table_ID=tt.AD_Table_ID AND tt.IsView='N')
WHERE t.IsView='N'
/

CREATE OR REPLACE VIEW CP_REF
AS
SELECT t.TableName, t.AD_Table_ID, c.ColumnName, c.AD_Column_ID,
    tt.TableName AS Dependent_TableName, tt.AD_Table_ID AS Dependent_Table_ID,
    cc.ColumnName AS Dependent_ColumnName, cc.AD_Column_ID AS Dependent_Column_ID
FROM AD_Table t
  INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID AND c.IsKey='Y')
  INNER JOIN AD_Column cc ON (c.AD_Element_ID=cc.AD_Element_ID)
  INNER JOIN AD_Table tt ON (cc.AD_Table_ID=tt.AD_Table_ID AND tt.IsView='N')
WHERE t.IsView='N'
UNION
SELECT t.TableName, t.AD_Table_ID, c.ColumnName, c.AD_Column_ID,
    tt.TableName AS Dependent_TableName, tt.AD_Table_ID AS Dependent_Table_ID,
    cc.ColumnName AS Dependent_ColumnName, cc.AD_Column_ID AS Dependent_Column_ID
FROM AD_Table t
  INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID AND c.IsKey='Y')
  INNER JOIN AD_Ref_Table rt ON (c.AD_Table_ID=rt.AD_Table_ID)
  INNER JOIN AD_Column cc ON (cc.AD_Reference_Value_ID=rt.AD_Reference_ID)
  INNER JOIN AD_Table tt ON (cc.AD_Table_ID=tt.AD_Table_ID AND tt.IsView='N')
WHERE t.IsView='N'
/
