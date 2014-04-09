<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=requests.jsp'/>
</c:if>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: requestDetails.jsp,v 1.3 2006/05/06 03:58:37 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Request Details
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Request Details</title>

<script type="text/javascript">
function validateRequest()
{
    if(document.Request.Summary.value == '')
    {
        alert("Follow-Up field is mandatory.");
        return false;
    }
    return true;
}
function responseChanged()
{
	var doingRequest = document.Request.Close.checked;
	doingRequest |= document.Request.Escalate.checked;
	doingRequest |= document.Request.Confidential.checked;
	doingRequest |= (document.Request.Summary.value != '');
	setAttachmentDisabled(doingRequest);
}
function attachmentChanged()
{
	setResponseDisabled( document.fileLoad.file.value != '' );
}
function setResponseDisabled(disabled)
{
	document.Request.Close.disabled = disabled;
	document.Request.Escalate.disabled = disabled;
	document.Request.Confidential.disabled = disabled;
	document.Request.Summary.disabled = disabled;
	document.Request.Reset.disabled = disabled;
	document.Request.Submit.disabled = disabled;
	document.getElementById("responseDisabled").style.display=(disabled?"block":"none");
}
function setAttachmentDisabled(disabled)
{
	document.fileLoad.file.disabled = disabled;
	document.fileLoad.Submit.disabled = disabled;
	document.getElementById("attachDisabled").style.display=(disabled?"block":"none");
}
</script>
</head>
<body><div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<!-- Set Request ID and get Request		-->
<c:set target='${info}' property='id' value='${param.R_Request_ID}' />
<c:set var='request' value='${info.request}' />
<c:if test='${empty request}'>
  <c:set target='${info}' property='message' value='Request not found' />
  <c:redirect url='requests.jsp'/>
</c:if>

<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content"> 
	  <h2>Request <c:out value='${request.documentNo}'/></h2>	  
	  <c:if test='${not empty info.info}'>
	    <p><c:out value='${info.message}'/></p>
	  </c:if>        
          <table class="internalTable">
          	  <tr>
	              <td colspan="3" class="lineItem">
	                  <label>Summary:</label>&nbsp;
	                  <c:out value='${request.summary}'/>
	              </td>
              </tr>
                <tr>
                    <td class="lineItem">
                        <label>Type:</label>&nbsp;<c:out value='${request.requestTypeName}'/><br/>
                        <label>Category:</label>&nbsp;<c:out value='${request.categoryName}'/><br/>
                        <label>Group:</label>&nbsp;<c:out value='${request.groupName}'/><br/>
                        <label>Importance:</label>&nbsp;<c:out value='${request.priorityUserText}'/><br/>
                        <label>Priority:</label>&nbsp;<c:out value='${request.priorityText}'/><br/>
                        <label>Status:</label>&nbsp;<c:out value='${request.statusName}'/><br/>
                        <label>Result:</label>&nbsp;<c:out value='${request.result}'/><br/>
                    </td>
                    <td class="lineItem">
                        <label>Creation Date:</label>&nbsp;<fmt:formatDate value='${request.created}'/><br/>
                        <label>Created By:</label>&nbsp;<c:out value='${request.createdByName}'/><br/>
                        <label>Confidentiality:</label>&nbsp;<c:out value='${request.confidentialText}'/><br/>
                        <label>Resolution Name:</label>&nbsp;<c:out value='${request.resolutionName}'/><br/>
                        <label>Resolution Type:</label>&nbsp;<c:out value='${request.dueTypeText}'/><br/>
                        <label>Next Action:</label>&nbsp;<fmt:formatDate value='${request.dateNextAction}'/><br/>
                    </td>                    
                </tr>
            </table>

             <h3>Attachments</h3>
             <c:if test='${not empty request.attachment}'>
             	<c:if test='${not empty request.attachment.textMsg}'>
                  <label><c:out value='${request.attachment.textMsg}'/></label>
              </c:if>
                 <c:forEach items='${request.attachment.entries}' var='entry'>
                     <a href="requestServlet?R_Request_ID=<c:out value='${request.r_Request_ID}'/>&AttachmentIndex=<c:out value='${entry.index}'/>" target="_blank">
                             <c:out value='${entry.name}'/>
                     </a><br/>
                 </c:forEach>
             </c:if>
      <br/>

 
      <c:if test='${request.webCanUpdate}'>
	      <form method="post" name="Request" action="requestServlet" enctype="application/x-www-form-urlencoded"
				onSubmit="return validateRequest()">

          <input name="Source" type="hidden" value=""/>
          <input name="Info" type="hidden" value=""/>
          <script language="Javascript">
            document.Request.Source.value=document.referrer;
            document.Request.Info.value=document.lastModified;
          </script>
          <input name="ForwardTo" type="hidden" value="<c:out value='${param.ForwardTo}'/>"/>
          <input name="SalesRep_ID" type="hidden" value="<c:out value='${webUser.salesRep_ID}'/>"/>
          <input name="R_Request_ID" type="hidden" id="R_Request_ID" value="<c:out value='${request.r_Request_ID}'/>" />
          <h3>Response</h3>

           <label class="labelcell">From:&nbsp;&nbsp;</label>
           	<c:out value='${webUser.name}'/> / <c:out value='${webUser.email}'/>
           <br/>
           
           <br />
 		   <label class="labelcell">Action:</label> 				
           <br/>           

            &nbsp;&nbsp;&nbsp;&nbsp;<input name="Close" type="checkbox" id="Close" value="Close" onclick="responseChanged()">Close request
			<br />
             
            &nbsp;&nbsp;&nbsp;&nbsp;<input name="Escalate" type="checkbox" id="Escalate" value="Escalate" onclick="responseChanged()">Escalate request
            <br/>

            &nbsp;&nbsp;&nbsp;&nbsp;<input name="Confidential" type="checkbox" id="Confidential" value="Confidential" onclick="responseChanged()">Confidential Information
            <br/>

            <h3>Follow-Up</h3>
            <textarea name="Summary" cols="80" rows="8" id="ID_Summary" class="wideText" onkeyup="responseChanged()"></textarea>
            <div class="entryNote">
                Follow-Up: 1500 characters max; for longer text, submit as an attachment
            </div>
			<br /><br />
			<div id="responseDisabled" class="disabledMsg">Response Form disabled due to Attach File Form being in use</div>
              
            <input class="bluebutton" name="Reset" type="reset" value="Reset" onclick="document.Request.reset();responseChanged();"/>
            <input class="bluebutton" name="Submit" type="submit" value="Submit">
      </form>
      <br/>
	  <h3>Attach File</h3>
	  <form action="requestServlet" method="post" enctype="multipart/form-data" name="fileLoad" id="fileLoad" onreset="attachmentChanged()">	  		
			<input name="R_Request_ID" type="hidden" id="R_Request_ID" value="<c:out value='${request.r_Request_ID}'/>">
			<div id="attachDisabled" class="disabledMsg">Attach file form disabled due to response form being in use.</div>
			<label class="labelcell" for="file">File to Attach: </label>
			<input name="file" type="file" id="file" size="40" onchange="attachmentChanged()" onkeyup="attachmentChanged()">
			<input type="submit" name="Submit" value="Upload">	  
	  	<br />
      </form>

	  </c:if>

	  <h3>History</h3>
	  <br />
      <table class="contentTable">
        <tr> 
          <th class="left">Created</th>
          <th class="left">By</th>
          <th class="left">Result</th>
        </tr>
        <c:forEach items='${request.updatesCustomer}' var='update' varStatus='status'>
        	<jsp:useBean id="status" type="javax.servlet.jsp.jstl.core.LoopTagStatus" />
        	<c:choose>
        		<c:when test="<%= status.getCount() %2 == 0 %>">
	        		<c:set var="rowClass" value="evenRow"/>
        		</c:when>
        		<c:otherwise>
	        		<c:set var="rowClass" value="oddRow"/>
        		</c:otherwise>
        	</c:choose> 
        <tr> 
          <td class="<c:out value='${rowClass}' /> left"><fmt:formatDate value='${update.created}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${update.createdByName}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${update.result}'/>&nbsp;</td>
        </tr>
        </c:forEach> 
        <tr> 
          <th class="left">Updated</th>
          <th class="left">By</th>
          <th class="left">Old Values</th>
        </tr>
        <c:forEach items='${request.actions}' var='action' varStatus='status2'>
        	<jsp:useBean id="status2" type="javax.servlet.jsp.jstl.core.LoopTagStatus" />
        	<c:choose>
        		<c:when test="<%= status2.getCount() %2 == 0 %>">
	        		<c:set var="rowClass" value="evenRow"/>
        		</c:when>
        		<c:otherwise>
	        		<c:set var="rowClass" value="oddRow"/>
        		</c:otherwise>
        	</c:choose> 
        <tr> 
          <td class="<c:out value='${rowClass}' /> left"><fmt:formatDate value='${action.created}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${action.createdByName}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${action.changesHTML}' escapeXml='false'/>&nbsp;</td>
        </tr>
        </c:forEach> 
      </table>
      <p>&nbsp;</p>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
