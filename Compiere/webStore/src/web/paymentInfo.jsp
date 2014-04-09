<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=paymentInfo.jsp'/>
</c:if>
<c:if test='${empty payment}'>
  <c:redirect url='index.jsp'/>
</c:if>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: paymentInfo.jsp,v 1.3 2006/05/06 02:13:56 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Payment Info
  - Variables: webOrder, webUser, payment
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Payment Info</title>
<script type="text/javascript">
	function explainCodes()
	{
		// open blank window and write to it
		win2 = window.open("", "", "width=500px, height=350px");
		// open document stream 
		win2.document.open(); 
		txtHtml = "Visa and Mastercard: Enter these three digits on the back of the card.";
		txtHtml += '<br />';
		txtHtml += '<img src="res/visaCID.jpg" />';
		txtHtml += '<br /><br />';
		txtHtml += 'American Express: Enter these four digits on the front of the card.';
		txtHtml += '<br />';
		txtHtml += '<img src="res/amexCID.jpg" />';				
		win2.document.write(txtHtml);		
	}
</script>
</head>
<body><div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content"> 
	  <c:if test='${not empty webOrder}'>
	  <h1>Thank you for your Order</h1>
      <table class="contentTable">
        <tr> 
          <th>Order</th>
          <th>Lines</th>
          <th>Shipping</th>
          <th>Tax</th>
          <th>Total</th>
        </tr>
        <tr> 
          <td class="oddRow">&nbsp;<c:out value='${webOrder.documentNo}'/></td>
          <td class="oddRow amount">&nbsp;<fmt:formatNumber value='${webOrder.totalLines}' type="currency" currencySymbol=""/></td>
          <td class="oddRow amount">&nbsp;<fmt:formatNumber value='${webOrder.freightAmt}' type="currency" currencySymbol=""/></td>
          <td class="oddRow amount">&nbsp;<fmt:formatNumber value='${webOrder.taxAmt}' type="currency" currencySymbol=""/></td>
          <td class="oddRow amount"><b><c:out value='${webOrder.currencyISO}'/>&nbsp;<fmt:formatNumber value='${webOrder.grandTotal}' type="currency" currencySymbol=""/></b></td>
        </tr>
      </table>
	  </c:if>
	  <c:if test='${empty webOrder}'>
	  <h1>Payment of  <c:out value='${payment.currencyISO}'/> <fmt:formatNumber value='${payment.payAmt}' type="currency" currencySymbol=""/></h1>
	  </c:if>
      <h2>Please enter your payment information</h2>
      <c:if test="${not empty payment.r_PnRef}">
        <p><b>Payment Info: <c:out value='${payment.r_PnRef}'/></b></p>
        <c:if test="${not empty payment.errorMessage}">
            <div class="error"><c:out value="${payment.errorMessage}"/></div>
        </c:if>
      </c:if>

      <div id="formstyles">
	  <form action="paymentServlet" method="post" enctype="application/x-www-form-urlencoded" 
	  	name="payment" target="_top" id="payment"
	    onSubmit="return checkForm(this, new Array ('CreditCardNumber','CreditCardExpMM','CreditCardExpYY','CreditCardVV','A_Name','A_Street','A_City','A_Zip'));">
		<a class="mandatory">* </a>Required field.  
		<br /><br />
        <div class="left">
            <label class="labelcell" id="LBL_CreditCard" for="Name">Credit Card:</label>
            <a class="mandatory">*</a>
            <div class="right"> 
	            <select class="fieldcell" id="ID_CreditCard" name="CreditCard" size="1">
	                <c:forEach items='${payment.creditCards}' var='cc'>
	                    <option value="<c:out value='${cc.value}'/>" <c:if test='${payment.creditCardType == cc.value}'>selected</c:if>><c:out value='${cc.name}'/></option>
	                </c:forEach>
	            </select>
	        </div>
            <br/>
		</div>
		<div class="left">
            <label class="labelcell" id="LBL_CreditCardNumber" for="CreditCardNumber">Credit Card Number</label>
            <a class="mandatory">*</a>
            <div class="right"> 
            	<input class="fieldcell" size="30" id="ID_CreditCardNumber" value='<c:out value="${payment.creditCardNumber}"/>' name="CreditCardNumber" maxlength="16" type="text"/>
            </div>
            <br/>
		</div>
		<div class="left">
            <label class="labelcell" id="LBL_Exp">Expiration Date:</label>
            <a class="mandatory">*</a>
       		<div class="right"> 
	            <select class="fieldcell" id="ID_CreditCardExpMM" name="CreditCardExpMM" size="1">
	                <c:forEach var='mm' begin="1" end="12">
	                    <option value="<c:out value='${mm}'/>" <c:if test='${payment.creditCardExpMM == mm}'>selected</c:if>><c:out value='${mm}'/></option>
	                </c:forEach>
	            </select>&nbsp;-&nbsp;
	            <select class="fieldcell" id="ID_CreditCardExpYY" name="CreditCardExpYY" class="mandatory" size="1">
	                <c:forEach var='yy' begin="7" end="20">
	                    <option value="<c:out value='${yy}'/>" <c:if test='${payment.creditCardExpYY == yy}'>selected</c:if>><c:out value='${yy+2000}'/></option>
	                </c:forEach>
	            </select>
            </div>
            <br/>
       </div>
       <div class="left">
            <label class="labelcell" id="LBL_CreditCardVV" for="CreditCardVV">Validation Code:</label>
            <a class="mandatory">*</a>
            <div class="right">
            	<input class="fieldcell" size="5" id="ID_CreditCardVV" name="CreditCardVV" maxlength="4" type="text"/>
            </div>    
            <div class="cvvcodes">
            <a onclick="explainCodes()">What is this?</a> 
      </div>                    
            <br/>
		</div>
		<div class="left">
            <label class="labelcell" id="LBL_A_Name" for="A_Name">Name on Credit Card:</label>
            <a class="mandatory">*</a>
            <div class="right">            
            	<input class="fieldcell" size="30" id="ID_A_Name" value='<c:out value="${payment.a_Name}"/>' name="A_Name" maxlength="60" type="text"/>
            </div>
            <br/>
		</div>
		<div class="left">
            <label class="labelcell" id="LBL_A_Street" for="A_Street">Street:</label>
            <a class="mandatory">*</a>
            <div class="right">
            	<input class="fieldcell" size="30" id="ID_A_Street" value='<c:out value="${payment.a_Street}"/>' name="A_Street" maxlength="60" type="text"/>
            </div>
            <br/>
        </div>
        <div class="left">
            <label class="labelcell" id="LBL_A_City" for="A_City">City:</label>
			<a class="mandatory">*</a>
            <div class="right">            
            	<input class="fieldcell" size="30" id="ID_A_City" value='<c:out value="${payment.a_City}"/>' name="A_City" maxlength="40" type="text"/>
            </div>
            <br/>
		</div>
		<div class="left">
            <label class="labelcell" id="LBL_A_Zip" for="A_Zip">Zip Code:</label>
            <a class="mandatory">*</a>
            <div class="right"> 
            	<input class="fieldcell" class="mandatory" size="10" id="ID_A_Zip" value='<c:out value="${payment.a_Zip}"/>' name="A_Zip" maxlength="10" type="text"/>
            </div>
            <br/>
		</div>
		<div class="left">
            <label class="labelcell" id="LBL_A_State" for="A_State">State:</label>
            <a class="mandatory">*</a>
            <div class="right"> 
            	<input class="fieldcell" size="10" id="ID_A_State" value='<c:out value="${payment.a_State}"/>' name="A_State" maxlength="20" type="text"/>
            </div>
            <br/>
		</div>
		<div class="left">
            <label class="labelcell" id="LBL_A_Country" for="A_Country">Country</label>
            <a class="mandatory">*</a>
            <div class="right"> 
            	<input class="fieldcell" size="30" id="ID_A_Country" value='<c:out value="${payment.a_Country}"/>' name="A_Country" maxlength="40" type="text"/>
            </div>
            <br/>
		</div>

        <c:if test="${not empty payment.errorMessage}">
            <div class="error"><c:out value="${payment.errorMessage}"/></div>
        </c:if>
        <div id="processingDiv" style="display:none"><strong>Processing ...</strong></div>        

		<br /><br />
		<input class="bluebutton" type="reset" name="Reset" value="Reset" />
      	<input class="bluebutton" type="submit" name="Submit" id="Submit" value="Submit Payment" />
      	<input name="SavePayment" type="checkbox" id="SavePayment" value="SavePayment" checked />
        Save Payment Information        	  			
		<br /><br />
      </form>
      </div>
	</div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
