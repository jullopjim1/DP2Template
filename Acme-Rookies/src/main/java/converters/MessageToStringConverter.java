
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Message;

@Component
@Transactional
public class MessageToStringConverter implements Converter<Message, String> {

	@Override
	public String convert(final Message comp) {
		String result;

		if (comp == null)
			result = null;
		else
			result = String.valueOf(comp.getId());

		return result;
	}

}
