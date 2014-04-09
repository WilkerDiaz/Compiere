<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=payments.jsp'/>
</c:if>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: payments.jsp,v 1.3 2006/05/19 22:04:55 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Payments
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Payments</title>
</head>
<body><div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content"> 
	<h2>My Payments</h2>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <div id="formstyles">
      <form action="paymentServlet" method="get" enctype="application/x-www-form-urlencoded" name="MakePayment" id="MakePayment">
         <div class="left">
	         <label class="labelcell" for="Amt">Amount:</label>
	         <div class="right">
		         <input class="fieldcell" name="Amt" type="text" id="Amt" value="0" size="10">
		         <input type="submit" name="Submit" value="Submit">
	         </div>
         </div>
      </form>
      </div>
      <br/><br />
      <table class="contentTable">
        <tr> 
          <th class="left">Document No</th>
          <th class="left">Doc Status</th>
          <th class="left">Credit Card</th>
          <th class="left">Date</th>
          <th class="amount">Amount</th>
          <th class="left">Details</th>
        </tr>
        <c:forEach items='${info.payments}' var='payment' varStatus='status'>
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
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${payment.documentNo}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${payment.docStatusName}'/></td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${payment.r_RespMsg}'/>&nbsp;</td>
          <td class="<c:out value='${rowClass}' /> left"><fmt:formatDate value='${payment.dateTrx}'/></td>
          <td class="<c:out value='${rowClass}' /> amount"><c:out value='${payment.currencyISO}'/>&nbsp;<fmt:formatNumber value='${payment.payAmt}' type="currency" currencySymbol=""/></td>
          <td class="<c:out value='${rowClass}' /> left">&nbsp;<c:out value='${payment.r_AuthCode}'/></td>
        </tr>
        </c:forEach> 
      </table>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
