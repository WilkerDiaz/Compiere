<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=requests.jsp'/>
</c:if>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: requests.jsp,v 1.6 2006/05/25 17:16:58 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Request Summary
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Requests</title>
</head>
<body>
<div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content"> 
	<h2>My Requests</h2>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  
	  <table class="contentTable">
        <tr> 
          <th class="left">Document No</th>
          <th class="left">Summary</th>
          <th class="left">Status</th>
          <th class="left">Assigned</th>
          <th class="left">Created</th>
        </tr>
        <c:forEach items='${info.requestsOwn}' var='request' varStatus='status'>
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
          <td class="<c:out value='${rowClass}' /> left"><a href="requestDetails.jsp?R_Request_ID=<c:out value='${request.r_Request_ID}'/>"><c:out value='${request.documentNo}'/></a></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${request.summary}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${request.statusName}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${request.salesRepName}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><fmt:formatDate value='${request.created}'/> <c:out value='${request.createdByName}'/></td>
        </tr>
        </c:forEach> 
      </table>
      <br />
	  <input class="bluebutton" type="button" name="NewRequest" onclick="top.location='request.jsp';" value="New Request"/>
	  <br /><br />
	  <!-- div id="formstyles">
	  <form action="javascript:void(0)" method="post" enctype="application/x-www-form-urlencoded" name="search" id="search">
		<div class="left">
			<label class="labelcell" for="RequestType_ID">Request Type:</label>
			<div class="right">
				<select class="fieldcell" name="RequestType_ID" id="ID_RequestType_ID">
					<option value="0" selected="selected">ANY</option>
					<option value="100">Request for Quotation</option>
					<option value="101">Service Request</option>
					<option value="102">Warranty</option>
				</select>
			</div>
		<br/>
		</div>
		<div class="left">
			<label class="labelcell" for="RefOrder_ID">Order Reference:</label>
			<div class="right">
		    	<cws:requestOrder bpartnerID='${webUser.bpartnerID}'/>
		    </div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Text in Summary:</label>
			<div class="right">
		    	<input class="fieldcell" type="text" name="Summary" id="ID_Summary"/>
		    </div>
		    <br/>	
		</div>
		<div class="left">
			<label class="labelcell">Confidential:</label>
			<div class="right">
				<select class="fieldcell">
					<option>ANY</option>
					<option>Confidential Only</option>
					<option>Non-Confidential Only</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Importance:</label>
			<div class="right">
				<select class="fieldcell">
					<option>ANY</option>
					<option>Low</option>
					<option>Medium</option>
					<option>High</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Priority:</label>
			<div class="right">
				<select class="fieldcell">
					<option>ANY</option>
					<option>Low</option>
					<option>Medium</option>
					<option>High</option>
				</select>					
			</div>
			<br/>
		</div>
		<div class="left">
			<label class="labelcell">Status:</label>
			<div class="right">
				<select class="fieldcell">
					<option>ANY</option>
					<option>Open</option>
					<option>Closed</option>
					<option>Pending</option>
				</select>
			</div>
			<br />
		</div>
		<div class="left">
			<label class="labelcell">Attachments:</label>
			<div class="right">
				<select class="fieldcell">
					<option>ANY</option>
					<option>Has Attachments Only</option>
					<option>No Attachments Only</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Start Date:</label>
			<div class="right">
				<input class="fieldcell" type="text" size="8" />
				<img src="res/Calendar16.gif" border="0"/>
			</div>
			<br/>		
		</div>		
		<div class="left">
			<label class="labelcell">End Date:</label>
			<div class="right">
				<input class="fieldcell" type="text" size="8"/>
				<img src="res/Calendar16.gif" border="0"/>		
			</div>	              
			<br/>	
		</div>
		<div class="left">
			<label class="labelcell">Assigned:</label>
			<div class="right">
				<select class="fieldcell">
					<option>ANY</option>
					<option>GardenAdmin</option>
					<option>GardenUser</option>
				</select>
			</div>
			<br />
		</div>		
	
	  <br /><br />													
	  <input class="bluebutton" type="button" name="Search" value="Search Requests"/>
	  <input class="bluebutton" type="button" name="Reset" value="Reset"/>
      </form>
      </div > -->
      <br/>
	        
      </div> 
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
