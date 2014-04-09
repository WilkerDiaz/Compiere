// namespace
var compiere = new Object();
compiere.webstore = new Object();
compiere.webstore.product = new Object();

//////////////////  utils 
function numeralsOnly(evt) 
{
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode :
		((evt.which) ? evt.which : 0));
	if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
}

function revealModal(divID)
{
	window.onscroll = function () { document.getElementById(divID).style.top = document.body.scrollTop; };
	document.getElementById(divID).style.display = "block";
	document.getElementById(divID).style.top = document.body.scrollTop;
}

function hideModal(divID)
{
	document.getElementById(divID).style.display = "none";
}

function submitCartForm() {
	document.forms['cartForm'].submit();
}

function startCheckout() {
	if (validateCartForm()) {
		if (hasSubscriptionProducts()) {
			revealModal('licensePopup');
		}
		else {
			acceptLicense();
		}
	}
}

function checkout() {
	var act = document.getElementById("act");
	act.value = "c";
	submitCartForm();
}

function acceptLicense() {
	var agl = document.getElementById("agreeLicense");
	agl.value = "y";
	hideModal('licensePopup');
	checkout();
}

function checkLicense() {
	var cbx = document.getElementById("cbRead");
	var btn = document.getElementById("btnAgree");
	if (cbx.checked) {
		btn.disabled = false;
	}
	else {
		btn.disabled = true;
	}
}

function updateCart() {
	validateCartForm();
	submitCartForm();
}

//////////////////  Products

function CompiereProduct(prodId, prodPrice, minQty)
{
	this.id = prodId;
	this.price = prodPrice;
	this.minQty = minQty;
}

compiere.webstore.product.STD   = new CompiereProduct("1000135", 300, 10);
compiere.webstore.product.PROF  = new CompiereProduct("1000219", 600, 10);
compiere.webstore.product.CLOUD = new CompiereProduct("1000265", 795, 10);
compiere.webstore.product.ENTR  = new CompiereProduct("1000476", 995, 10);

compiere.webstore.product.MANF  = new CompiereProduct("1000252", 150, 10);
compiere.webstore.product.WAREH = new CompiereProduct("1000253", 7500, 1);

compiere.webstore.product.ORA1  = new CompiereProduct("1000159", 60, 1);
compiere.webstore.product.ORA2  = new CompiereProduct("1000162", 120, 1);
compiere.webstore.product.ORP1  = new CompiereProduct("1000161", 1950, 1);
compiere.webstore.product.ORP2  = new CompiereProduct("1000162", 5800, 1);


//////////////////  

function getItems() {
	var lines = new Object();
	var cartForm = document.forms['cartForm'];
	for (inputName in cartForm) {
		var strs = inputName.split('_');
		if (strs.length == 2) {
			var input = cartForm[inputName];
			var propName = strs[0];
			var lineId = strs[1];
			if (lines[lineId] == null) {
				lines[lineId] = new Object();
			}
			lines[lineId][propName] = input;
		}
	}
	
	return lines;
}

function hasSubscriptionProducts() {
	var lines = getItems();
	for (lineId in lines) {
		var line = lines[lineId];
		if (line.prod == null)
			continue;
		if (   (line.prod.value == compiere.webstore.product.STD.id) 
			|| (line.prod.value == compiere.webstore.product.PROF.id)
			|| (line.prod.value == compiere.webstore.product.CLOUD.id)
			|| (line.prod.value == compiere.webstore.product.ENTR.id)
			|| (line.prod.value == compiere.webstore.product.MANF.id)
			|| (line.prod.value == compiere.webstore.product.WAREH.id) ) 
		{
			return true;
		}
	}
	return false;
}

function validateCartForm() {

    var result = true;
	var lines = getItems();
	var prodLine = null;
	var mnfLine = null;
	var wmLine = null;
	
	for (lineId in lines) {
		var line = lines[lineId];
		if (line.prod == null)
			continue;
		if ((line.prod.value == compiere.webstore.product.PROF.id)  || 
			(line.prod.value == compiere.webstore.product.CLOUD.id) || 
			(line.prod.value == compiere.webstore.product.ENTR.id)) {
			var qty = parseInt(line.qty.value);
			if (qty < 10) {
				line.qty.value = 10;
				alert('Subscription requires 10 users minimum. Number of users is set to 10.');
				result = false;
			}
			prodLine = line;
			prodLine.intQty = qty;
		}
		else if (line.prod.value == compiere.webstore.product.STD.id) {
			var qty = parseInt(line.qty.value);
			if (qty < 10) {
				line.qty.value = 10;
				alert('Subscription requires 10 users minimum. Number of users is set to 10.');
				result = false;
			}
		}
		else if (line.prod.value == compiere.webstore.product.MANF.id) {
			mnfLine = line;
			mnfLine.intQty = parseInt(line.qty.value);
		}
		else if (line.prod.value == compiere.webstore.product.WAREH.id) {
			wmLine = line;
			wmLine.intQty = parseInt(line.qty.value);
		}
	}
	
	if ((mnfLine != null) && (!mnfLine.del.checked)) {
		if ((prodLine == null) || (prodLine.del.checked)) {
			alert('Manufacturing option requires Professional, Cloud or Enterprise Edition subscription.\nPlease make sure you already purchased them beforehand.');
			result = true;
		}
		else if (mnfLine.intQty != prodLine.intQty) {
			mnfLine.intQty = prodLine.intQty;
			mnfLine.qty.value = prodLine.intQty;
			alert('Manufacturing licensed users is set to be the same with the Professional, Cloud or Enterprise edition.');
			result = false;
		}
	}
	if ((wmLine != null) && (!wmLine.del.checked)) {
		if ((prodLine == null) || (prodLine.del.checked)) {
			alert('Warehouse option requires Professional, Cloud or Enterprise Edition subscription.\nPlease make sure you already purchased them beforehand.');
			result = true;
		}
	}
	return result;
}

