package cm4108.exceptions;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

public class InvalidParamException extends WebApplicationException
{
public InvalidParamException()
{
super(Response.status(Response.Status.NOT_FOUND).entity("Invalid parameters sent!").type("text/plain").build());
} //end method
} //end class
