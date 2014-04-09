/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: 9_UpdateDBVersion.sql 9436 2010-12-17 00:25:06Z freyes $
 ***
 * Title:	Update Database Version
 * Description:
 ************************************************************************/

UPDATE AD_System 
-----------------==========-----
  SET	ReleaseNo = '362',
        Version='2010-12-16',
-----------------==========----- 
        UpdatedBy=0,
        Created=Sysdate,
		Updated=SysDate;
--
COMMIT;
--