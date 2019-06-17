
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Flugot;

@Component
@Transactional
public class FlugotToStringConverter implements Converter<Flugot, String> {

	@Override
	public String convert(final Flugot a) {
		String result;

		if (a == null)
			result = null;
		else
			result = String.valueOf(a.getId());

		return result;
	}

}
