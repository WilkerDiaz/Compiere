

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

////////////////// cart functions 

function submitCartForm() {
	document.forms['cartForm'].submit();
}

function startCheckout() {
	checkout();	
}

function checkout() {
	var act = document.getElementById("act");
	act.value = "c";
	submitCartForm();
}

function acceptLicense() {
}

function checkLicense() {
}

function updateCart() {
	submitCartForm()	
}


function validateCartForm() {
}

