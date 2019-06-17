
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Provider;

@Component
@Transactional
public class ProviderToStringConverter implements Converter<Provider, String> {

	@Override
	public String convert(final Provider position) {
		String result;

		if (position == null)
			result = null;
		else
			result = String.valueOf(position.getId());

		return result;
	}

}
