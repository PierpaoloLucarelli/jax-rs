package cm4108;

//JAX-RS
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("/test")
public class TestClass
{
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response dummyGet()
	{
	return Response.status(200).entity("Congratulations! Jersey is working!").build();
	} //end method
} //end class
