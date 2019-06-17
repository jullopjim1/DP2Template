
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Finder;
import domain.Message;
import domain.Position;
import domain.Rookie;
import forms.MessageForm;

@Service
@Transactional
public class MessageService {

	// Repositories and services

	@Autowired
	private MessageRepository		messageRepository;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private ServiceUtils			serviceUtils;
	@Autowired(required = false)
	private Validator				validator;


	// CRUD

	public Message findOne(final Integer messageId) {
		return this.messageRepository.findOne(messageId);
	}

	public Collection<Message> findAll() {
		Collection<Message> res;
		res = this.messageRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Message create() {
		final Message res = new Message();
		res.setMoment(new Date(System.currentTimeMillis() - 3000));
		res.setSender(this.actorService.findPrincipal());
		res.setTags(new ArrayList<String>());
		return res;
	}

	public Message save(final Message m) {
		LoginService.getPrincipal();
		final Message message = (Message) this.serviceUtils.checkObjectSave(m);
		if (message.getId() == 0) {
			message.setMoment(new Date(System.currentTimeMillis() - 1000));
			message.setSender(this.actorService.findPrincipal());
		} else
			m.setTags(message.getTags());
		if (m.getTags() == null)
			m.setTags(new ArrayList<String>());
		final Message res = this.messageRepository.save(m);
		return res;
	}

	public Collection<Message> saveBroadcast(final Message m) {
		this.serviceUtils.checkAuthority(Authority.ADMIN);
		final Collection<Message> res = new ArrayList<Message>();
		if (!m.getTags().contains("SYSTEM"))
			m.getTags().add("SYSTEM");
		final Collection<Actor> actors = this.actorService.findAllExceptMe();
		for (final Actor a : actors) {
			m.setReceiver(a);
			final Message saved = this.save(m);
			res.add(saved);
		}
		return res;
	}

	public void delete(final Message m) {
		final Message message = (Message) this.serviceUtils.checkObjectSave(m);
		this.serviceUtils.checkAnyActor(new Actor[] {
			message.getSender(), message.getReceiver()
		});
		if (message.getTags().contains("DELETED"))
			this.messageRepository.delete(message);
		else {
			message.getTags().add("DELETED");
			this.save(m);
		}
	}

	public Message show(final Integer messageId) {
		this.serviceUtils.checkId(messageId);
		final Message message = this.messageRepository.findOne(messageId);
		Assert.notNull(message);
		this.serviceUtils.checkAnyActor(new Actor[] {
			message.getSender(), message.getReceiver()
		});
		return message;
	}

	public MessageForm construct(final Message m) {
		final MessageForm res = new MessageForm();
		res.setId(m.getId());
		res.setVersion(m.getVersion());
		res.setBody(m.getBody());
		res.setReceiver(m.getReceiver());
		res.setSubject(m.getSubject());
		res.setTags(this.tagsToStr(m.getTags()));
		return res;
	}

	public Message deconstruct(final MessageForm form, final boolean isBroadcast, final BindingResult binding) {
		final Message res = this.create();
		res.setBody(form.getBody());
		res.setReceiver(form.getReceiver());
		res.setSubject(form.getSubject());
		if (form.getTags() != null)
			res.setTags(this.strToTags(form.getTags()));
		if (form.getReceiver() == null && !isBroadcast)
			this.serviceUtils.addCustomFormMessageError("messageForm", "receiver", "message.mustreceiver", binding);
		this.validator.validate(res, binding);
		return res;
	}

	public Collection<String> strToTags(final String str) {
		final String[] tags = str.split("\\[|\\]");
		final Collection<String> res = new ArrayList<String>();
		for (String tag : tags) {
			tag = tag.trim();
			if (tag.length() > 0)
				res.add(tag);
		}
		return res;
	}

	public String tagsToStr(final Collection<String> tags) {
		String res = "";
		for (String tag : tags) {
			tag = tag.trim();
			if (tag.length() > 0)
				res = res + "[" + tag + "] ";
		}
		return res;
	}

	public boolean containsSpam(final Message m) {
		boolean res = false;
		final Collection<String> strings = Arrays.asList(m.getBody(), m.getSubject());
		strings.addAll(m.getTags());
		final Collection<String> spamWords = this.configurationService.findOne().getSpamWordsEN();
		spamWords.addAll(this.configurationService.findOne().getSpamWordsES());
		for (final String s : strings) {
			for (final String spamWord : spamWords)
				if (s.contains(spamWord)) {
					res = true;
					break;
				}
			if (res)
				break;
		}
		return res;
	}

	public Collection<Message> findSendedMessages() {
		return this.messageRepository.findSendedMessages(this.actorService.findPrincipal().getId());
	}

	public Collection<Message> findSendedMessages(final Actor a) {
		return this.messageRepository.findSendedMessages(a.getId());
	}

	public Collection<Message> findMyMessages() {
		return this.messageRepository.findMyMessages(this.actorService.findPrincipal().getId());
	}

	public Collection<Message> findMyMessagesByTag(final String tag) {
		Assert.notNull(tag);
		Collection<Message> res = null;
		if (tag.isEmpty())
			res = this.messageRepository.findMyMessages(this.actorService.findPrincipal().getId());
		else
			res = this.messageRepository.findMyMessagesByTag(this.actorService.findPrincipal().getId(), tag);
		return res;
	}

	public void notificationApplication(final Application a) {
		final Application application = (Application) this.serviceUtils.checkObjectSave(a);
		final Rookie rookie = application.getRookie();
		final Company company = application.getPosition().getCompany();
		this.generateNotificationApplication(rookie, a.getPosition().getTicker());
		this.generateNotificationApplication(company, a.getPosition().getTicker());
	}

	public void notificationPosition(final Position newPosition) {
		final Collection<Finder> finders = this.finderService.findAll();
		for (final Finder f : finders) {
			final Collection<Position> positions = this.finderService.findPositionsByFinder(f);
			if (positions.contains(newPosition))
				this.generateNotificationFinder(f.getRookie(), newPosition.getTicker());
		}
	}

	private void generateNotificationApplication(final Actor actor, final String ticker) {
		final Message message = this.create();
		message.setSender(this.actorService.findActorByUsername("system"));
		message.setBody("The application to the position " + ticker + " has changed its status \r\n" + "La solicitud de la posición " + ticker + " ha cambiado sus estado \r\n");
		message.setSubject("Application status changed // Cambio del estado de la solicitud");
		message.setReceiver(actor);
		final Collection<String> tags = message.getTags();
		tags.add("NOTIFICATION");
		message.setTags(tags);
		this.messageRepository.save(message);
	}

	private void generateNotificationFinder(final Rookie rookie, final String ticker) {
		final Message message = this.create();
		message.setSender(this.actorService.findActorByUsername("system"));
		message.setBody("This position may be interesting " + ticker + " \r\n" + "Esta posición puede ser interesante " + ticker + " \r\n");
		message.setSubject("A new interesting position // Una nueva posición interesante");
		message.setReceiver(rookie);
		final Collection<String> tags = message.getTags();
		tags.add("NOTIFICATION");
		message.setTags(tags);
		this.messageRepository.save(message);
	}

	public void notificationRebranding() {
		this.configurationService.findOne();
		final Message message = this.create();
		message.setSender(this.actorService.findActorByUsername("system"));
		message.setBody("The system has been rebranded to \"Acme-Rookies\" \r\n" + "El sistema ha cambiado de nombre a \"Acme-Rookies\" \r\n");
		message.setSubject("Rebranding // Cambio de nombre");
		final Collection<String> tags = message.getTags();
		tags.add("NOTIFICATION");
		tags.add("REBRANDING");
		message.setTags(tags);
		this.saveBroadcast(message);
	}

	public void deleteMyMessages() {
		final Collection<Message> messages = this.findMyMessages();
		for (final Message m : messages)
			while (this.findOne(m.getId()) != null)
				this.delete(m);
	}

}
