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
			for(var i = 0 ; i < data.length ; i++){
				$("#results").append("<li appNumber='" + data[i].id + "' class='appointment'>" +
						"<p class='user'>" + data[i].appUser + "</p>" + 
						"<p class='date'>" + data[i].dateTime + "</p>" +
						"<p class='duration'>" + data[i].duration + "</p>" + 
						"<p class='description'>" + data[i].description + "</p>" + 
						"</li>");
			}
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
	
	$('body').on('click', 'li.appointment', function() {
		var $form = $("<form>", {id: "editApp"});
		$form.append("<h1>Edit an appointment</h1>");
		$form.append("<label>Description</label>");
		$form.append( "<input type='text' value='" + $(this).find(".user").html() ) + "'>";
		$('body').append($form);
	});
});

