PREFIX_RQ = "";
// PREFIX_RQ = "http://localhost:9998";

var ConnectionForm = angular.module('ConnectionForm', []);
// Hello
ConnectionForm.controller("mainController", function($scope, $http) {
	var errorConnection = false;
	$scope.CredentialsCheck = function() {
		if (errorConnection == true) {
			return "btn-theme04";
		} else {
			return "btn-theme";
		}
	};
	$scope.submitData = function(person) {

		var data = 'j_username=' + encodeURIComponent(person.userID)
				+ '&j_password=' + encodeURIComponent(person.password)
				+ '&remember-me=' + person.rememberMe + '&submit=Login';
		return $http.post('api/authentication', data, {
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		}).success(function(response) {
			
			window.location.replace("/home.html");
			return response;
		}).error(function(response) {
			console.log("Failed");
			errorConnection=false;
		});
	}

	// var data = {};
	// var headers = person ? {
	// authorization : "Basic "
	// + btoa(person.userID + ":"
	// + person.password)} : {};
	// data.user = person;
	// // +person.userID
	// $http.get(PREFIX_RQ+"/api/app/account/user/",
	// {headers : headers})
	// .success(function (data, status, headers, config)
	// {
	// console.log("Succeed");
	// //console.log(person.firstname);
	// window.location.replace("/home.html");
	//                
	// })
	// .error(function (data, status, headers, config)
	// {
	// errorConnection = true;
	// console.log("Failed");
	// });
	//        
	//        
	//        
	// };

	// $(document).ready(function(){
	// $("form").validate({
	// rules: {
	// name:{
	// minlength: 3,
	// maxlength: 20,
	// required: true
	// }
	// },
	// highlight: function (element) {
	// $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
	// },
	// unhighlight: function (element) {
	// $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
	// }
	// });
	// });

	// End of js part for validating the form inputs

});