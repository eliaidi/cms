package com.wk.cms.mvc.json;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wk.cms.model.File;
import com.wk.cms.service.exception.ServiceException;

public class FileJsonSerializer extends JsonSerializer<File> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileJsonSerializer.class);
	
	@Override
	public void serialize(File value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		try {
			Map<String ,String> jsonMap = value.toJsonMap();
			gen.writeObject(jsonMap);
		} catch (ServiceException e) {
			LOGGER.error("File转换JSON失败！",e);
		}
		
	}

}
