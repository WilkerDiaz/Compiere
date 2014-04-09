<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=invoices.jsp'/>
</c:if>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: commissionRuns.jsp,v 1.2 2006/05/06 00:41:33 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Invoices
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Commissions</title>
</head>
<body><div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content"> 
	<h2>My Commissions</h2>
	<c:if test='${not empty info.info}'>
	  <p><b><c:out value='${info.message}'/></b></p>
	</c:if>
      <table class="contentTable">
        <tr> 
          <th class="left">Document No</th>
          <th class="left">Description</th>
          <th class="left">Start Date</th>
          <th class="amount">Grand Total</th>
        </tr>
        <c:forEach items='${info.commissionRuns}' var='commissionRun' varStatus='status'> 
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
          <td class="<c:out value='${rowClass}' /> left"><a href="comissionAmts.jsp?C_CommissionRun_ID=<c:out value='${commissionRun.c_CommissionRun_ID}'/>"><c:out value='${commissionRun.documentNo}'/></a></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${commissionRun.description}'/>&nbsp;</td>
          <td class="<c:out value='${rowClass}' /> left"><fmt:formatDate value='${commissionRun.startDate}'/></td>
          <td class="<c:out value='${rowClass}' /> amount"><fmt:formatNumber value='${commissionRun.grandTotal}' type="currency" currencySymbol=""/></td>
        </tr>
        </c:forEach> 
      </table>
      <p>&nbsp;</p></div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
