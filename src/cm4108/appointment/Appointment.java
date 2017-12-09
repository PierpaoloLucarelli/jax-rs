package cm4108.appointment;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName="appointment")
public class Appointment{
	public String id;
	
	public Date dateTime;
	
	public int duration;
	
	public String owner;
	
	public String description;
	
	// default constructor
	public Appointment() {}
	
	public Appointment(String id, Date date, int duration, String owner, String desc) {
		this.setId(id);
		this.setDateTime(date);
		this.setDuration(duration);
		this.setOwner(owner);
		this.setDescription(desc);
	}

	@DynamoDBHashKey(attributeName="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName="dateTime")
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@DynamoDBAttribute(attributeName="duration")
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@DynamoDBAttribute(attributeName="owner")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@DynamoDBAttribute(attributeName="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	
	
}
