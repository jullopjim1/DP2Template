
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Company;

@Component
@Transactional
public class CompanyToStringConverter implements Converter<Company, String> {

	@Override
	public String convert(final Company comp) {
		String result;

		if (comp == null)
			result = null;
		else
			result = String.valueOf(comp.getId());

		return result;
	}

}
