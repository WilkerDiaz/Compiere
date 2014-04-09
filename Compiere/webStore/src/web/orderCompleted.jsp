<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet'/>
</c:if>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: confirm.jsp,v 1.2 2006/05/06 00:41:33 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Confirmation 
  - webOrder, webUser, payment
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Order Completed</title>
</head>
<body><div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content">
	<h2>Thank you for your order.</h2>
	<p>Your order has been placed. The order number is <a href="orderDetails.jsp?C_Order_ID=<c:out value='${webOrder.c_Order_ID}'/>"></a><c:out value='${webOrder.documentNo}'/>. </p>
	<p>For security purposes, please <a href="emailVerify.jsp">verify your email address</a> if you have not done so already.</p>
	<p>Afterward, you can access your products from <a href="assets.jsp">Downloads</a> area.</p>
	  <br/><br/>
	  
      <p>&nbsp;</p>
      
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
