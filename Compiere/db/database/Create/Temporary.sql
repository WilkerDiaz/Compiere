/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Temporary.sql,v 1.1 2006/04/21 17:51:59 jjanke Exp $
 ***
 * Title:	Temporary Tables
 * Description:	
 ************************************************************************/

DROP TABLE T_Selection CASCADE CONSTRAINTS
/
--	Truely temporary table
CREATE GLOBAL TEMPORARY TABLE T_Selection	
(
	T_Selection_ID	NUMBER(10, 0) NOT NULL	
		CONSTRAINT T_Selection_Key PRIMARY KEY
)
ON COMMIT DELETE ROWS
/


DROP TABLE T_Selection2
/
--	Temporary table over commit
CREATE GLOBAL TEMPORARY TABLE T_Selection2 
(
	Query_ID	   NUMBER	  NOT NULL,
	T_Selection_ID NUMBER(10) NOT NULL,
	CONSTRAINT T_Selection2_Key PRIMARY KEY (Query_ID,T_Selection_ID)
)
ON COMMIT PRESERVE ROWS 
/


/**
 *	Spool Table
 */
DROP SEQUENCE T_Spool_Seq
/
CREATE SEQUENCE T_Spool_Seq
	INCREMENT BY 1
	START WITH	 1
/
-- INSERT INTO T_Spool (AD_PInstance_ID, SeqNo, Msg) VALUES (123, T_Spool_Seq.NextVal, 'ggg');


