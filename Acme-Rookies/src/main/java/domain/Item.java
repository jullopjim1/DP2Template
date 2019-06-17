
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

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import cz.jirutka.validator.collection.constraints.EachSafeHtml;
import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "provider")
})
public class Item extends DomainEntity {

	//Atributtes--------------------------------------------------------------
	private String			name;
	private String			description;
	private List<String>	link;
	private List<String>	pictures;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@ElementCollection
	@Valid
	@EachURL
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getLink() {
		return this.link;
	}

	public void setLink(final List<String> link) {
		this.link = link;
	}

	@ElementCollection
	@Valid
	@EachURL
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final List<String> pictures) {
		this.pictures = pictures;
	}


	// Relationships ---------------------------------------------------------
	private Provider	provider;


	@Valid
	@ManyToOne(optional = false)
	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(final Provider provider) {
		this.provider = provider;
	}

}
