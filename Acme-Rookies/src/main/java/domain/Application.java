
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "rookie")
})
public class Application extends DomainEntity {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private Date	publishMoment;
	private String	answerExplanation;
	private String	answerCode;
	private Date	submitMoment;
	private String	status;


	@Past
	public Date getPublishMoment() {
		return this.publishMoment;
	}

	public void setPublishMoment(final Date publishMoment) {
		this.publishMoment = publishMoment;
	}
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAnswerExplanation() {
		return this.answerExplanation;
	}

	public void setAnswerExplanation(final String answerExplanation) {
		this.answerExplanation = answerExplanation;
	}
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAnswerCode() {
		return this.answerCode;
	}

	public void setAnswerCode(final String answerCode) {
		this.answerCode = answerCode;
	}

	@Past
	public Date getSubmitMoment() {
		return this.submitMoment;
	}

	public void setSubmitMoment(final Date submitMoment) {
		this.submitMoment = submitMoment;
	}
	@NotBlank
	@Pattern(regexp = "^PENDING$|^SUBMITTED$|^ACCEPTED$|^REJECTED$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}


	// Relationships ---------------------------------------------------------
	private Problem		problem;
	private Position	position;
	private Rookie		rookie;
	private Curricula	curricula;


	@Valid
	@ManyToOne(optional = false)
	public Problem getProblem() {
		return this.problem;
	}

	public void setProblem(final Problem problem) {
		this.problem = problem;
	}
	@Valid
	@ManyToOne(optional = false)
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}
	@Valid
	@ManyToOne(optional = false)
	public Rookie getRookie() {
		return this.rookie;
	}

	public void setRookie(final Rookie rookie) {
		this.rookie = rookie;
	}

	@Valid
	@OneToOne
	public Curricula getCurricula() {
		return this.curricula;
	}

	public void setCurricula(final Curricula curricula) {
		this.curricula = curricula;
	}

}
