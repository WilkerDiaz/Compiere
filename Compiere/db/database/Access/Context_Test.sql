DECLARE
    string		VARCHAR2(60);
	no			NUMBER;
BEGIN
--	Compiere_Context.Login('SuperUser','System','Friedenburg Mandant','GERGER');
--	Compiere_Context.Login('SuperUser','System','System Administrator');
--	Compiere_Context.Login('System','System','System Administrator');
	Compiere_Context.Login('Compiere','Internal','Server');
    --
	SELECT COUNT(*) INTO No FROM C_DocType;
    DBMS_OUTPUT.PUT_LINE('Count= ' || No);
END;

