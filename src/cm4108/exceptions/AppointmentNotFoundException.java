package cm4108.exceptions;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

public class AppointmentNotFoundException extends WebApplicationException
{
public AppointmentNotFoundException(String id)
{
super(Response.status(Response.Status.NOT_FOUND).entity("Appointment id "+id+" not found").type("text/plain").build());
} //end method
} //end class
