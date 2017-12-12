package cm4108.appointment;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

//general Java
import java.util.*;
//JAX-RS

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.amazonaws.regions.Regions;
//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import cm4108.Config;
import cm4108.aws.DynamoDBUtil;
import cm4108.exceptions.AppointmentNotFoundException;

@Path("/appointment")
public class AppointResources {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addAppintment(
			@FormParam("dateTime") String dateTime,
			@FormParam("duration") String duration,
			@FormParam("owner") String owner,
			@FormParam("description") String description
			) {
		try {
			Appointment appointment = new Appointment(dateTime,duration,owner,description );
			DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
			mapper.save(appointment);
			return Response.
					status(201).
					entity(dateTime+" ("+owner+","+description+") saved sucessfully").
					build();
		} catch (Exception e) {
		return Response.
				status(400).
				entity("Something went wrong. Parameters accepted: id, dateTime, duration, owner, desc").
				build();		//if the client did something wrong
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{id}")
	public Appointment getOneAppointment(@PathParam("id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
		Appointment app = mapper.load(Appointment.class,id);
		if(app!=null) {
			return app;
		}
		throw new AppointmentNotFoundException(id);
		
	} //end method
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Appointment> getAllAppointments()
	{
	DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);	
	DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();
	List<Appointment> result= mapper.scan(Appointment.class, scanExpression);
	return result;
	} //end method
	
}
