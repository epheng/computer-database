$(document).ready(function() {
	$.validator.addMethod(
			"regex",
			function(value, element, regexp) {
				var re = new RegExp(regexp);
				return this.optional(element) || re.test(value);
			},
			"Please enter a valid date"
	);
	$("#form").validate({
		rules: {
			computerName: {
				required: true,
				minlength: 3
			},
			introduced: {
				regex: /^((0[1-9]|[12]\d|3[01])(\/|-)(0[1-9]|1[012])(\/|-)\d\d(\d\d)?|\d\d\d\d-(0[1-9]|1[012])-(0[1-9]|[12]\d|3[01]))$/
			},
			discontinued: {
				regex: /^((0[1-9]|[12]\d|3[01])(\/|-)(0[1-9]|1[012])(\/|-)\d\d(\d\d)?|\d\d\d\d-(0[1-9]|1[012])-(0[1-9]|[12]\d|3[01]))$/
			}
		}
	});
});