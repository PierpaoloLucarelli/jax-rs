package cm4108.appointment;

import java.util.Collection;
//general Java
import java.util.*;
//JAX-RS

import javax.ws.rs.*;
import javax.ws.rs.core.*;

//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import cm4108.Config;
import cm4108.aws.DynamoDBUtil;
import cm4108.exceptions.AppointmentNotFoundException;

@Path("/appointment")
public class AppointResources {

	// create a new appointment
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addAppintment(
			@FormParam("dateTime") long dateTime,
			@FormParam("duration") int duration,
			@FormParam("appUser") String appUser,
			@FormParam("description") String description
			) {
		try {
			Appointment appointment = new Appointment(dateTime,duration,appUser,description);
			DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
			mapper.save(appointment);
			return Response.
					status(201).
					entity("Hey "+appUser+", "+description+" saved sucessfully").
					build();
		} catch (Exception e) {
		return Response.
				status(400).
				entity("Something went wrong. Parameters accepted: id, dateTime, duration, owner, desc").
				build();		//if the client did something wrong
		}
	}
	
	// delete an appointment by its id
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteAppointment(@PathParam("id") String id) {
		DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
		Appointment appointment = mapper.load(Appointment.class,id);
		if (appointment == null)
			throw new WebApplicationException(404);
		mapper.delete(appointment);
		return Response.status(204).entity("Appointment deleted successfully").build();
	}
	
	// update an appointment by its id
	@Path("/{id}")
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateAppointment(
			@PathParam("id") String id,
			@FormParam("dateTime") long dateTime,
			@FormParam("duration") int duration,
		    @FormParam("description") String description
		    ) {
		try {
			System.out.println(id + " " + dateTime + " " + duration + " " + description);
			DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);	
			Appointment old = mapper.load(Appointment.class,id);
			old.setDateTime(dateTime);
			old.setDescription(description);
			old.setDuration(duration);
			mapper.save(old);
			return Response.status(201).entity("201 resource updated successfully").build();
			} catch (Exception e) {
				return Response.status(404).entity("bad request").build();
			}
	}
	
	// get an appointment by owner name and within a specified range
	@Path("/user/{owner}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Appointment> getAppointmentByUser(  @PathParam("owner") String appUser,
											@QueryParam("fromDate") String fromDate,
											@QueryParam("toDate") String toDate) {
		List<Appointment> result = null;
		DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
		System.out.println(fromDate);
		if (fromDate!=null && toDate!=null && appUser!=null) {
			Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
			eav.put(":val1", new AttributeValue().withS((String)appUser));
			eav.put(":val2", new AttributeValue().withN(fromDate));
			eav.put(":val3", new AttributeValue().withN(toDate));
			DynamoDBScanExpression scanExp = 
					new DynamoDBScanExpression()
					.withFilterExpression("appUser = :val1 and appDateTime > :val2 and appDateTime < :val3")
					.withExpressionAttributeValues(eav);
			result = mapper.scan(Appointment.class, scanExp);
		} else {
			throw new WebApplicationException(400);
		}
		return result;
	}
	
	// get an appointment by its id. If not found throw AppNotFoundException
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Appointment getAppointmentByID(@PathParam("id") String id)
	{
	DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
	Appointment app =mapper.load(Appointment.class,id);
	if (app!=null)
		return app;
	throw new AppointmentNotFoundException(id);
	}
	
	// list all appointmen. if no results are found return empty list
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
