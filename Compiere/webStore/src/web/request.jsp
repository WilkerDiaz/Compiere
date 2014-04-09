<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=request.jsp&SalesRep_ID=${param.SalesRep_ID}'/> 
</c:if> 
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: request.jsp,v 1.2 2006/05/06 00:41:33 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Request
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Request</title>
</head>
<body>
<div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content">
	<h2>Contact Us</h2>
	<div id="formstyles">
      <form method="post" name="Request" action="requestServlet" enctype="application/x-www-form-urlencoded"
	  	onSubmit="return checkForm(this, new Array ('Summary'));">

            <input name="Source" type="hidden" value=""/>
            <input name="Info" type="hidden" value=""/>
            <script language="Javascript">
              document.Request.Source.value=document.referrer;
              document.Request.Info.value=document.lastModified;
            </script>
            <input name="ForwardTo" type="hidden" value="<c:out value='${param.ForwardTo}'/>"/>
            <c:if test='${not empty param.SalesRep_ID}'>
              <input name="SalesRep_ID" type="hidden" value="<c:out value='${param.SalesRep_ID}'/>"/>
            </c:if>
            <c:if test='${empty param.SalesRep_ID}'>
              <input name="SalesRep_ID" type="hidden" value="<c:out value='${webUser.salesRep_ID}'/>"/>
            </c:if>
			<div class="left">
              <label class="labelcell">From: &nbsp;
              	<c:out value='${webUser.name}'/> / <c:out value='${webUser.email}'/>
              </label>
              <br/>
			</div>
			<div class="left">
                <label class="labelcell" id="ID_RequestType" for="RequestType">Request Type:</label>
                <div class="right" class="fieldcell">
                	<cws:requestType/>
                </div>
              <br/>
            </div>
			<div class="left">
                <label class="labelcell" id="ID_RequestType" for="RequestType">Optional Order Reference:</label>
                <div class="right" class="fieldcell">
                	<cws:requestOrder bpartnerID='${webUser.bpartnerID}'/>
                </div>
              <br/>
           </div>   
           
            <!--<label id="ID_Summary" for="Summary">Question - Issue - Request:</label>-->
            <div class="left">
            <label class="labelcell"></label>
            	<div class="right" class="fieldcell">
            		<input name="Confidential" type="checkbox" id="Confidential" value="Confidential"> Confidential Information
            	</div>
            </div>
            <br /><br />
          
            <textarea name="Summary" cols="80" rows="8" id="ID_Summary" class="wideText"></textarea>
            <div class="entryNote">
                Summary: 1500 characters max
                <br/>
                Attachments: Click on the document number after submitting.
            </div>
			<br style="clear:both"/>

            <input class="bluebutton" name="Reset" type="reset" value="Reset"/>
            <input class="bluebutton" name="Submit" type="submit" value="Submit">

      </form>
      </div>
      </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
