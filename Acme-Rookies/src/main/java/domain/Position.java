
package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "company")
})
public class Position extends DomainEntity {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private String	ticker;
	private String	title;
	private String	description;
	private Date	deadLine;
	private String	profile;
	private String	skills;
	private String	technologies;
	private double	salary;
	private boolean	finalMode;
	private boolean	cancel;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Column(unique = true)
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	public Date getDeadLine() {
		return this.deadLine;
	}

	public void setDeadLine(final Date deadLine) {
		this.deadLine = deadLine;
	}
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getProfile() {
		return this.profile;
	}

	public void setProfile(final String profile) {
		this.profile = profile;
	}
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSkills() {
		return this.skills;
	}

	public void setSkills(final String skills) {
		this.skills = skills;
	}
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTechnologies() {
		return this.technologies;
	}

	public void setTechnologies(final String technologies) {
		this.technologies = technologies;
	}
	@Min(0)
	public double getSalary() {
		return this.salary;
	}

	public void setSalary(final double salary) {
		this.salary = salary;
	}

	public boolean isFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}

	public boolean isCancel() {
		return this.cancel;
	}

	public void setCancel(final boolean cancel) {
		this.cancel = cancel;
	}


	// Relationships ---------------------------------------------------------
	private Company			company;
	private List<Finder>	finders;


	//	private Audit			audit;

	@ManyToOne(optional = false)
	@Valid
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	//	@ManyToOne(optional = true)
	//	@Valid
	//	public Audit getAudit() {
	//		return this.audit;
	//	}
	//
	//	public void setAudit(final Audit audit) {
	//		this.audit = audit;
	//	}

	@Valid
	@ManyToMany
	public List<Finder> getFinders() {
		return this.finders;
	}

	public void setFinders(final List<Finder> finders) {
		this.finders = finders;
	}
}
