/*
 * StringToPersonalDataConverter.java
 * 
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PersonalDataRepository;
import domain.PersonalData;

@Component
@Transactional
public class StringToPersonalDataConverter implements Converter<String, PersonalData> {

	@Autowired
	PersonalDataRepository	personalDataRepository;


	@Override
	public PersonalData convert(final String text) {
		PersonalData result;
		int id;

		try {
			if (text == "")
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.personalDataRepository.findOne(id);
			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
