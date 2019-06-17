
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "rookie")
})
public class Curricula extends DomainEntity {

	//--------Atributos propios
	private boolean	original;

	//--------Relaciones
	private Rookie	rookie;


	@ManyToOne(optional = true)
	@Valid
	public Rookie getRookie() {
		return this.rookie;
	}

	public void setRookie(final Rookie rookie) {
		this.rookie = rookie;
	}

	public boolean getOriginal() {
		return this.original;
	}

	public void setOriginal(final boolean original) {
		this.original = original;
	}

}
