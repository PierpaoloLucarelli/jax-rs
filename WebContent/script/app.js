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
				var appDate = new Date(data[i].dateTime*1000);
				$("#results").append("<li appNumber='" + data[i].id + "' class='appointment'>" +
						"<p class='date'>" + appDate +"</p>" +
						"<p class='user'>" + data[i].appUser + "</p>" +  
						"<p class='description'>" + data[i].description + "</p>" + 
						"</li>");
			}
		});
	});
	
	$("#addNewButton").click(function(e){
		e.preventDefault();
		var appUser = $("#new_owner").val();
		var startdate = $("#new_date").val();
		var startTime = $("#new_time").val();
		var duration = $("#new_duration").val();
		var description = $("#new_desc").val();
		var $date = new Date(startdate);
		var finalDate = new Date($date.getFullYear(), $date.getMonth(), $date.getDay(), startTime.split(":")[0], startTime.split(":")[1], 0,0 );
		var dateTime = finalDate.getTime() / 1000;
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
		var id = $(this).attr("appnumber");
		var url = "api/appointment/" + id;
		$('.popup').attr("appnumber", id);
		$.get( url, function( data ) {
			console.log(data);
			var date = new Date(data.dateTime*1000);
			console.log(date.getFullYear);
			var dd = date.getDate();
			var mm = date.getMonth()+1;
			var yyyy = date.getFullYear();
			if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} 
			var dateString = yyyy+'-'+mm+'-'+dd;
			console.log(dateString);
			$("#edit_date").datepicker({dateFormat: 'yy-mm-dd'});
			$("#edit_date").datepicker('setDate', new Date(dateString));
			$("#edit_desc").val(data.description);
			$('#edit_time').val(date.getHours() + ":" + (date.getMinutes()<10?'0':'') + date.getMinutes());
			$("#edit_duration").val(data.duration);
		});
	});
	
	$("#delete_edit").click(function(){
		var id = $(".popup").attr("appnumber");
		$.ajax({
		    url: 'api/appointment/'+id,
		    type: 'DELETE',
		    success: function(result) {
		        console.log(result);
		    }
		});
	});
	
	$("#save_edit").click(function(){
		var id = $(".popup").attr("appnumber");
		var startdate = $("#edit_date").val();
		var startTime = $("#edit_time").val();
		var description = $("#edit_desc").val();
		var duration =  $("#edit_duration").val();
		var $date = new Date(startdate);
		console.log($date.getDate());
		var finalDate = new Date($date.getFullYear(), $date.getMonth(), $date.getDate(), startTime.split(":")[0], startTime.split(":")[1], 0,0 );
		var dateTime = finalDate.getTime() / 1000;
		$.ajax({
		    url: 'api/appointment/'+id,
		    type: 'PUT',
		    data: {
		    		id: id,
		    		dateTime: dateTime,
		    		duration: duration,
		    		description: description
		    },
		    success: function(result) {
		        console.log(result);
		    }
		});
	});
	
});

