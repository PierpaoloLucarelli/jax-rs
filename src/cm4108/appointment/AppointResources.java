package cm4108.appointment;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;
//general Java
import java.util.*;
//JAX-RS

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.amazonaws.regions.Regions;
//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import cm4108.Config;
import cm4108.aws.DynamoDBUtil;
import cm4108.exceptions.AppointmentNotFoundException;

@Path("/appointment")
public class AppointResources {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addAppintment(
			@FormParam("dateTime") long dateTime,
			@FormParam("duration") String duration,
			@FormParam("appUser") String appUser,
			@FormParam("description") String description
			) {
		try {
			System.out.println("dateis");
			System.out.println(dateTime);
			Appointment appointment = new Appointment(dateTime,duration,appUser,description );
			DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
			mapper.save(appointment);
			return Response.
					status(201).
					entity(dateTime+" ("+appUser+","+description+") saved sucessfully").
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
	
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	public Collection getAppointmentByUser( @FormParam("owner") String owner,
//								@FormParam("dateTime") String dateTime,
//								@FormParam("duration") int duration,
//								@FormParam("description") String description) {
//		DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
//		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
//		eav.put(":owner", new AttributeValue().withS(owner));
//		DynamoDBScanExpression scanExp = new DynamoDBScanExpression()
//				.withFilterExpression("owner = :owner")
//				.withExpressionAttributeValues(eav);
//		List<Appointment> result = mapper.scan(Appointment.class, scanExp);
//		return result;
//	}
	
	@Path("/user/{owner}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Appointment> getAppointmentByUser(  @PathParam("owner") String appUser,
											@QueryParam("fromDate") String fromDate,
											@QueryParam("toDate") String toDate) {
		List<Appointment> result = null;
		DynamoDBMapper mapper=DynamoDBUtil.getMapper(Config.AWS_REGION);
		System.out.println(fromDate);
		System.out.println(toDate);
		System.out.println(appUser);
		if (fromDate!=null && toDate!=null) {
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
