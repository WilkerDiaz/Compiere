//
//  Check if the form inputs listed in fieldNames array are all filled out
//  enable 
//
function checkForm(form, fieldNames, submitInput, submitText) 
{
    var emptyFields = new Array(); 
	
    for (var i = 0; i < fieldNames.length; i++) {
		var fname = fieldNames[i];
		if (form[fname] != null && form[fname].value != null) {
			if (form[fname].value == "") 
				emptyFields.push(fname);
			}
		}
	
	if (emptyFields.length > 0) {
		var msg = 'Please enter the required fields : \n';
		for (var i = 0; i < emptyFields.length; i++) {
		   msg += emptyFields[i] + '\n';
		}
		alert(msg);
		return false;
	}
	
	if (submitInput != null && form[submitInput] != null) {
		form[submitInput].disabled = true;
		if (submitText != null)
			form[submitInput].value = submitText;
//		alert('Processing...');
	}
	return true;
}
