
package forms;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import domain.Audit;

public class FlugotForm {

	//Atributes-----------------------------------------

	Integer			id;
	private String	ticker;
	private Date	publicationDate;
	private String	body;
	private String	picture;
	private Audit	audit;
	private boolean	finalMode;


	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	@NotBlank
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@SafeHtml
	@NotBlank
	@Length(min = 0, max = 100)
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@URL
	@SafeHtml
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public boolean isFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Audit getAudit() {
		return this.audit;
	}

	public void setAudit(final Audit audit) {
		this.audit = audit;
	}

}
