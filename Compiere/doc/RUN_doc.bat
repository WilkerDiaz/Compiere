@Title API Documentation for entire Product
@Rem $Id: RUN_doc.bat 8439 2010-02-16 22:47:26Z freyes $

rmdir /s /q API

@call documentation.bat  "..\ad\src;..\base\src;common\src;..\client\src;..\common\src;..\install\src;..\interfaces\src;..\print\src;..\serverApps\src;..\tools\src;..\webCM\src;..\webStore\src" API

@pause


