
package forms;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import domain.Actor;
import domain.DomainEntity;

public class MessageForm extends DomainEntity {

	private String	subject;
	private String	body;
	private String	tags;
	private String	newTag;


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

	public String getTags() {
		return this.tags;
	}
	public void setTags(final String tags) {
		this.tags = tags;
	}

	public String getNewTag() {
		return this.newTag;
	}
	public void setNewTag(final String newTag) {
		this.newTag = newTag;
	}


	// Relationships

	private Actor	receiver;


	@ManyToOne(optional = true)
	@Valid
	public Actor getReceiver() {
		return this.receiver;
	}
	public void setReceiver(final Actor receiver) {
		this.receiver = receiver;
	}

}
