
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "sender")
})
public class Message extends DomainEntity {

	// Attributes

	private Date				moment;
	private String				subject;
	private String				body;
	private Collection<String>	tags;


	@NotNull
	@Past
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getSubject() {
		return this.subject;
	}
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}
	public void setBody(final String body) {
		this.body = body;
	}

	@NotNull
	@ElementCollection
	@EachNotBlank
	public Collection<String> getTags() {
		return this.tags;
	}
	public void setTags(final Collection<String> tags) {
		this.tags = tags;
	}


	// Relationships

	private Actor	sender;
	private Actor	receiver;


	@ManyToOne(optional = false)
	@Valid
	public Actor getSender() {
		return this.sender;
	}
	public void setSender(final Actor sender) {
		this.sender = sender;
	}

	@ManyToOne(optional = false)
	@Valid
	public Actor getReceiver() {
		return this.receiver;
	}
	public void setReceiver(final Actor receiver) {
		this.receiver = receiver;
	}

}
