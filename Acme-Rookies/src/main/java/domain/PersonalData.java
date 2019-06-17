
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
//@Table(indexes = {
//	@Index(columnList = "curricula")
//})
public class PersonalData extends DomainEntity {

	//--------------Atributos propios
	private String		fullName;
	private String		statement;
	private String		phoneNumber;
	private String		gitHubProfile;
	private String		linkedInProfile;
	private boolean		original;

	//--------------Relaciones
	private Curricula	curricula;


	//----------------------------------------------------
	@NotBlank
	@NotNull
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	@NotBlank
	@NotNull
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getStatement() {
		return this.statement;
	}

	public void setStatement(final String statement) {
		this.statement = statement;
	}

	@NotBlank
	@NotNull
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@NotBlank
	@NotNull
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@URL
	public String getGitHubProfile() {
		return this.gitHubProfile;
	}

	public void setGitHubProfile(final String gitHubProfile) {
		this.gitHubProfile = gitHubProfile;
	}

	@NotBlank
	@NotNull
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@URL
	public String getLinkedInProfile() {
		return this.linkedInProfile;
	}

	public void setLinkedInProfile(final String linkedInProfile) {
		this.linkedInProfile = linkedInProfile;
	}

	public boolean getOriginal() {
		return this.original;
	}

	public void setOriginal(final boolean original) {
		this.original = original;
	}

	//-------------------------Relaciones

	@NotNull
	@Valid
	@OneToOne(optional = false)
	public Curricula getCurricula() {
		return this.curricula;
	}

	public void setCurricula(final Curricula curricula) {
		this.curricula = curricula;
	}

}
