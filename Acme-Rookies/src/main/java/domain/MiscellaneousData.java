
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import cz.jirutka.validator.collection.constraints.EachSafeHtml;
import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "curricula")
})
public class MiscellaneousData extends DomainEntity {

	//--------------Atributos propios
	private String			text;
	private List<String>	attachments;
	private boolean			original;

	//--------------Relaciones
	private Curricula		curricula;


	//----------------------------------------------------
	@NotBlank
	@NotNull
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@ElementCollection
	@Valid
	@EachURL
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final List<String> attachments) {
		this.attachments = attachments;
	}

	public boolean getOriginal() {
		return this.original;
	}

	public void setOriginal(final boolean original) {
		this.original = original;
	}

	//----------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = true)
	public Curricula getCurricula() {
		return this.curricula;
	}

	public void setCurricula(final Curricula curricula) {
		this.curricula = curricula;
	}

}
