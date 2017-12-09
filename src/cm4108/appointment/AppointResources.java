package cm4108.appointment;

import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import cm4108.aws.DynamoDBUtil;

@Path("/appointment")
public class AppointResources {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addAppintment(
			@FormParam("id") String id,
			@FormParam("dateTime") Date dateTime,
			@FormParam("duration") int duration,
			@FormParam("owner") String owner,
			@FormParam("description") String description
			) {
		try {
			Appointment appointment = new Appointment(id,dateTime,duration,owner,description );
			DynamoDBMapper mapper=DynamoDBUtil.getMapper(null);
			mapper.save(appointment);
			return Response.
					status(201).
					entity(id+"/"+dateTime+" ("+owner+","+description+") saved sucessfully").
					build();
		} catch (Exception e) {
		return Response.
				status(400).
				entity("Something went wrong. Parameters accepted: id, dateTime, duration, owner, desc").
				build();		//if the client did something wrong
		}
	}
	
	
}
