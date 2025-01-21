function roleFields() {
	var role = document.getElementById('role');
	var clientFields = document.getElementById('clientFields');

	var roleValue = role.value;

	if (roleValue == 'CLIENT') {
		clientFields.style.removeProperty('display');
	} else {
		clientFields.style.display = 'none';
	}
}