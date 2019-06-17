
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import security.LoginService;
import security.UserAccount;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository ----------------------------------------------------------------
	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ServiceUtils			serviceUtils;


	//Constructor----------------------------------------------------------------------------

	public SocialProfileService() {
		super();
	}

	// Simple CRUD methods -------------------------------------------------------------------
	public SocialProfile create(final int actorId) {
		final SocialProfile socialProfile = new SocialProfile();
		socialProfile.setActor(this.actorService.findOne(actorId));
		this.serviceUtils.checkActor(socialProfile.getActor());
		return socialProfile;
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> socialProfiles;

		socialProfiles = this.socialProfileRepository.findAll();
		Assert.notNull(socialProfiles);

		return socialProfiles;
	}
	public SocialProfile findOne(final int socialProfileId) {
		SocialProfile socialProfile;
		socialProfile = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(socialProfileId);

		return socialProfile;
	}

	public SocialProfile save(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		this.checkPrincipal(socialProfile);
		final Integer actorsitoId = socialProfile.getActor().getId();
		final UserAccount userAccount = LoginService.getPrincipal();
		final Integer actorId = this.actorService.findActorByUsername(userAccount.getUsername()).getId();
		SocialProfile result;
		if (actorId.equals(actorsitoId)) {
			//		this.serviceUtils.checkActor(socialProfile.getActor());
			this.serviceUtils.checkObjectSave(socialProfile);

			result = this.socialProfileRepository.save(socialProfile);
		} else
			result = null;
		return result;
	}

	public void delete(final SocialProfile socialProfile) {

		Assert.notNull(socialProfile);
		this.checkPrincipal(socialProfile);
		final Integer actorsitoId = socialProfile.getActor().getId();
		final UserAccount userAccount = LoginService.getPrincipal();
		final Integer actorId = this.actorService.findActorByUsername(userAccount.getUsername()).getId();
		if (actorId.equals(actorsitoId)) {
			//		this.serviceUtils.checkActor(socialProfile.getActor());
			this.serviceUtils.checkObjectSave(socialProfile);

			this.socialProfileRepository.delete(socialProfile);
		}
	}

	//Other Methods-----------------------------------------------------------------
	public Boolean checkPrincipal(final SocialProfile socialProfile) {
		final UserAccount u = socialProfile.getActor().getUserAccount();
		Assert.isTrue(u.equals(LoginService.getPrincipal()), "este perfil no corresponde con este actor");
		return true;
	}

	public Collection<SocialProfile> findProfileByActorId(final int actorId) {
		return this.socialProfileRepository.findProfileByActorId(actorId);
	}
}
