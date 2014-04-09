/*************************************************************************
 * The contents of this file are subject to the compiere1 License.  You may
 * obtain a copy of the License at    http://www.compiere1.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: compiere1 ERP+CRM
 * Copyright (C) 1999-2007 Jorg Janke, compiere1, Inc. All Rights Reserved.
 *************************************************************************
 ****
 * Title:	Alter schema for reference DB
 * Description:	
 *	Run as reference
 *      :compiereID serves old schema name 
 *      :referenceID serves new schema name
 ************************************************************************/

alter schema :compiereID rename to :referenceID; 

exit

