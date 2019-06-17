
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.MessageService;
import services.ServiceUtils;
import domain.Message;
import forms.MessageForm;

@Controller
@RequestMapping("message")
public class MessageController extends AbstractController {

	// Services

	@Autowired
	private MessageService			messageService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ServiceUtils			serviceUtils;
	@Autowired
	private ConfigurationService	configurationService;


	// List

	@RequestMapping("actor/list")
	public ModelAndView list(@RequestParam(required = false) final String tag) {
		final ModelAndView res = new ModelAndView("message/list");
		if (tag == null) {
			final Collection<Message> messages = this.messageService.findMyMessages();
			res.addObject("messages", messages);
			res.addObject("requestURI", "message/actor/list.do");
		} else {
			final Collection<Message> messages = this.messageService.findMyMessagesByTag(tag);
			res.addObject("messages", messages);
			res.addObject("requestURI", "message/actor/list.do");
		}
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}
	// Send

	@RequestMapping("actor/send")
	public ModelAndView send() {
		final Message message = this.messageService.create();
		final MessageForm form = this.messageService.construct(message);
		return this.createEditModelAndView(form, false);
	}

	@RequestMapping(value = "actor/send", method = RequestMethod.POST, params = "save")
	public ModelAndView send(final MessageForm messageForm, final BindingResult binding) {
		ModelAndView res = null;
		final Message message = this.messageService.deconstruct(messageForm, false, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(messageForm, false);
		else
			try {
				this.messageService.save(message);
				res = new ModelAndView("redirect:/message/actor/list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(messageForm, false, "cannot.commit.error");
			}
		return res;
	}

	// Broadcast

	@RequestMapping("administrator/broadcast")
	public ModelAndView broadcast() {
		final Message message = this.messageService.create();
		final MessageForm form = this.messageService.construct(message);
		return this.createEditModelAndView(form, true);
	}

	@RequestMapping(value = "administrator/broadcast", method = RequestMethod.POST, params = "save")
	public ModelAndView broadcast(final MessageForm messageForm, final BindingResult binding) {
		ModelAndView res = null;
		final Message message = this.messageService.deconstruct(messageForm, true, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(messageForm, true);
		else
			try {
				this.messageService.saveBroadcast(message);
				res = new ModelAndView("redirect:/message/actor/list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(messageForm, true, "cannot.commit.error");
			}
		return res;
	}

	// Delete

	@RequestMapping(value = "actor/delete")
	public ModelAndView send(@RequestParam(required = true) final Integer messageId) {
		final ModelAndView res = new ModelAndView("redirect:/message/actor/list.do");
		final Message message = this.messageService.findOne(messageId);
		try {
			this.messageService.delete(message);
		} catch (final Throwable oops) {
			res.addObject("message", "cannot.commit.error");
		}
		return res;
	}

	// Add tag

	@RequestMapping(value = "actor/send", method = RequestMethod.POST, params = "addTag")
	public ModelAndView addTagSend(final MessageForm messageForm, final BindingResult binding) {
		final Collection<String> tags = this.messageService.strToTags(messageForm.getTags());
		if (messageForm.getNewTag() == null)
			this.serviceUtils.addCustomFormMessageError("messageForm", "newTag", "org.hibernate.validator.constraints.NotBlank.message", binding);
		else
			tags.add(messageForm.getNewTag());
		messageForm.setTags(this.messageService.tagsToStr(tags));
		return this.createEditModelAndView(messageForm, false);
	}

	@RequestMapping(value = "administrator/broadcast", method = RequestMethod.POST, params = "addTag")
	public ModelAndView addTagBroadcast(final MessageForm messageForm, final BindingResult binding) {
		final Collection<String> tags = this.messageService.strToTags(messageForm.getTags());
		if (messageForm.getNewTag() == null)
			this.serviceUtils.addCustomFormMessageError("messageForm", "newTag", "org.hibernate.validator.constraints.NotBlank.message", binding);
		else
			tags.add(messageForm.getNewTag());
		messageForm.setTags(this.messageService.tagsToStr(tags));
		return this.createEditModelAndView(messageForm, true);
	}

	// Remove tag

	@RequestMapping(value = "actor/send", method = RequestMethod.POST, params = "removeTag")
	public ModelAndView removeTagSend(final MessageForm messageForm) {
		final Collection<String> tags = this.messageService.strToTags(messageForm.getTags());
		tags.remove(new ArrayList<String>(tags).get(tags.size() - 1));
		messageForm.setTags(this.messageService.tagsToStr(tags));
		return this.createEditModelAndView(messageForm, false);
	}

	@RequestMapping(value = "administrator/broadcast", method = RequestMethod.POST, params = "removeTag")
	public ModelAndView removeTagBroadcast(final MessageForm messageForm) {
		final Collection<String> tags = this.messageService.strToTags(messageForm.getTags());
		tags.remove(new ArrayList<String>(tags).get(tags.size() - 1));
		messageForm.setTags(this.messageService.tagsToStr(tags));
		return this.createEditModelAndView(messageForm, true);
	}

	// Show

	@RequestMapping("actor/show")
	public ModelAndView show(@RequestParam(required = true) final Integer messageId) {
		final ModelAndView res = new ModelAndView("message/show");
		final Message message = this.messageService.show(messageId);
		res.addObject("messageObject", message);
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}

	// Ancillary methods

	private ModelAndView createEditModelAndView(final MessageForm messageForm, final boolean isBroadcast) {
		return this.createEditModelAndView(messageForm, isBroadcast, null);
	}

	private ModelAndView createEditModelAndView(final MessageForm messageForm, final boolean isBroadcast, final String message) {
		final ModelAndView res = new ModelAndView("message/send");
		res.addObject("isBroadcast", isBroadcast);
		res.addObject("actors", this.actorService.findAllExceptMe());
		if (isBroadcast)
			res.addObject("actionURL", "message/administrator/broadcast.do");
		else
			res.addObject("actionURL", "message/actor/send.do");
		res.addObject("message", message);
		res.addObject("messageForm", messageForm);
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;
	}

}
