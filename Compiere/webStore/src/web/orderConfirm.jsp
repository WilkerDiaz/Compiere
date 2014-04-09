<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=orderConfirm.jsp'/>
</c:if>
<c:if test='${empty payment}'>
  <c:redirect url='index.jsp'/>
</c:if>
<html>
<!--
  - - -
  - Web Payment Info
  - Variables: webOrder, webUser, payment
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Order Confirmation</title>
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
<body>
<div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>    
	<div id="leftcontent">
	<div id="rightcontent">
	  	  <h2>Order Information</h2>
		  <table>
	        <tr> 
	          <td><b>Product</b></td>
	          <td><b>Price</b></td>
	          <td><b>Quantity</b></td>
	          <td class="amount"><b>Total (<c:out value='${priceList.currency}'/>)</b></td>
	        </tr>
	        <c:forEach items='${webBasket.lines}' var='line' varStatus='status'> 
	      	<jsp:useBean id="status" type="javax.servlet.jsp.jstl.core.LoopTagStatus" />
	       	<tr> 
	           <td ><c:out value='${line.name}'/></td>
		    		<input id="inProdId<c:out value='${status.count}'/>" type="hidden"  size="10" 
			 		 name="prod_<c:out value='${status.count}'/>"
			 		 value="<c:out value='${line.m_Product_ID}'/>"
			 		 />
	            <td class="amount"><fmt:formatNumber value='${line.price}' type="currency" currencySymbol=""/></td>
	            <td class="amount">
					<fmt:formatNumber value='${line.quantity}'/>
	            </td>
	            <td class="amount"><fmt:formatNumber value='${line.total}' type="currency" currencySymbol=""/></td>
	          </tr>
	          </c:forEach> 
	          <tr> 
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td class="amount">
	              <hr style="color:#1B1B1B;" />
	              <fmt:formatNumber value='${webBasket.total}' type="currency" currencySymbol=""/>
	            </td>
	          </tr>          
	        </table> 	
	  </div>      	
      <h2>Please enter your payment information</h2>      
      <div id="formstyles">
      <form action="orderServlet" method="post" enctype="application/x-www-form-urlencoded" 
	  	name="payment" target="_top" id="payment"
	    onSubmit="return checkForm(this, new Array ('CreditCardNumber','CreditCardExpMM','CreditCardExpYY','CreditCardVV','A_Name','A_Street','A_City','A_Zip'), 'Submit', 'Processing..');">
      	<input id="orderConfirm" name="orderConfirm" type="hidden" value="y"/>	    
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
		<input class="bluebutton" type="button" name="Checkout" id="Checkout" value="Update" onClick="/cart.jsp" />
		<input class="bluebutton" type="reset" name="Reset" value="Reset" />
      	<input class="bluebutton" type="submit" name="Submit" id="Submit" value="Place Order" />
      	<input name="SavePayment" type="checkbox" id="SavePayment" value="SavePayment" checked />
        Save Payment Information        	  			
		<br /><br />						
						
      </form>
      </div>
<!--      
     	<c:out value='${webUser.name}'/><br/>
     	<c:out value='${webUser.company}'/><br/>
     	<c:out value='${webUser.address}'/><br/>
     	<c:out value='${webUser.address2}'/><br/>
     	<c:out value='${webUser.city}'/>&nbsp;<c:out value='${webUser.postal}'/><br/>
     	<c:out value='${webUser.regionName}'/><br/>
     	<c:out value='${webUser.countryName}'/><br/>
-->
      <%--  <c:if test='${not empty webOrder}'> --%>
	  <!-- <h2>Thank you for your Order</h2> -->
	  <%--</c:if>--%>
	  <%--  
	  <c:if test='${empty webOrder}'>
	  <h1>Payment of  <c:out value='${payment.currencyISO}'/> <fmt:formatNumber value='${payment.payAmt}' type="currency" currencySymbol=""/></h1>
	  </c:if>
	  --%>
	  <c:if test="${not empty payment.r_PnRef}">
        <p><b>Payment Info: <c:out value='${payment.r_PnRef}'/></b></p>
        <c:if test="${not empty payment.errorMessage}">
            <div class="error"><c:out value="${payment.errorMessage}"/></div>
        </c:if>
      </c:if>
	</div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
