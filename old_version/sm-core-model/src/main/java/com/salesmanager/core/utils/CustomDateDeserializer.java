package com.salesmanager.core.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException,
			JsonProcessingException {
		try {
			return format.parse(jsonParser.getText());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);  
		}
	}
}
