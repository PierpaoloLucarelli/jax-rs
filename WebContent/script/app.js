$(document).ready(function(){
	console.log("hi");
	
	$("#mainform").submit(function(e){
		e.preventDefault();
		console.log("submitting");
		var owner = $("#user").val();
		var from = $("#from").val();
		var to = $("#to").val();
		var fromDate = new Date(from).getTime() / 1000;
		var toDate = new Date(to).getTime() / 1000;
		var url = "api/appointment/user/" + owner;
		var params = "?fromDate=" + fromDate + "&toDate=" + toDate;
		url += params;
		console.log(url);
		$.get( url, function( data ) {
			console.log(data);
		});
	});
	
	$("#addNewButton").click(function(e){
		e.preventDefault();
		var appUser = $("#new_owner").val();
		var startdate = $("#new_dateTime").val();
		var duration = $("#new_duration").val();
		var description = $("#new_desc").val();
		var dateTime = new Date(startdate).getTime() / 1000;
		
		console.log(appUser + " " + dateTime + " " + duration + " " + description );
		
		$.post("api/appointment", {
			dateTime: dateTime,
			duration: duration,
			appUser: appUser,
			description: description
		}).done(function(data){
			console.log(data);
		});
	});
});

