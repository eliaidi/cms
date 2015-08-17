package com.wk.cms.mvc.json;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wk.cms.model.ExtField;
import com.wk.cms.model.FieldValue;
import com.wk.cms.utils.CommonUtils;

public class OneToManyFieldSerializer extends JsonSerializer<Collection<Object>> {

	@Override
	public void serialize(Collection<Object> value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		if(!CommonUtils.isEmpty(value)){
			for(Object t : value){
				if(t instanceof ExtField){
					((ExtField) t).setChannel(null);
				}else if(t instanceof FieldValue){
					((FieldValue) t).setDocument(null);
				}
			}
		}
		gen.writeObject(value);
	}

}
