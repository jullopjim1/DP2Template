
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.Actor;
import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

	//Repository-----------------------------------------------------------------

	@Autowired
	private CreditCardRepository	creditCardRepository;

	//Service-----------------------------------------------------------------

	@Autowired
	private ActorService			actorService;
	@Autowired
	private ServiceUtils			serviceUtils;


	public CreditCard create() {
		final CreditCard creditCard = new CreditCard();
		return creditCard;
	}

	public CreditCard save(CreditCard creditCard) {
		this.serviceUtils.checkObjectSave(creditCard);
		if (creditCard.getId() > 0) {
			final Actor actor = this.actorService.findPrincipal();
			Assert.isTrue(actor.getCreditCard().equals(creditCard));
		}
		final Date date = new Date(System.currentTimeMillis());
		final int nowYear = 1900 + date.getYear();
		Assert.isTrue(creditCard.getExpirationYear() >= nowYear);
		if (creditCard.getExpirationYear() == nowYear)
			Assert.isTrue(creditCard.getExpirationMonth() > date.getMonth());
		creditCard = this.creditCardRepository.save(creditCard);
		this.creditCardRepository.flush();
		return creditCard;
	}
	public void delete(final CreditCard creditCard) {
		final Actor actor = this.actorService.findPrincipal();
		Assert.isTrue(actor.getCreditCard().equals(creditCard));
		this.creditCardRepository.delete(creditCard);
	}

	public void delete1(final CreditCard creditCard) {
		this.creditCardRepository.delete(creditCard);
	}

	public CreditCard findOne(final int CreditCardId) {
		return this.creditCardRepository.findOne(CreditCardId);
	}

	public Collection<CreditCard> findAll() {
		return this.creditCardRepository.findAll();
	}

	public CreditCard findCreditCardByActor(final int actorId) {
		return this.creditCardRepository.findCreditCardByActor(actorId);
	}

}
