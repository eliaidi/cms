package com.wk.cms.mvc.json;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;

import org.hibernate.collection.internal.PersistentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wk.cms.model.ExtField;
import com.wk.cms.model.Field;
import com.wk.cms.model.FieldValue;
import com.wk.cms.utils.CommonUtils;

public class OneToManyFieldSerializer extends JsonSerializer<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OneToManyFieldSerializer.class);
	@Override
	public void serialize(Object value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		if (Collection.class.isAssignableFrom(value.getClass())) {
			Collection<?> colls = (Collection<?>) value;
			if (!CommonUtils.isEmpty(colls)) {
				for (Object t : colls) {
					if (t instanceof ExtField) {
						((ExtField) t).setChannel(null);
					} else if (t instanceof Field) {
						((Field) t).setParent(null);
					}else if(t instanceof FieldValue){
						((FieldValue)t).setDocument(null);
					}
				}
			}
		} else {
			if(Field.class.isAssignableFrom(value.getClass())){
				((Field) value).setExtField(null);
			}
		}

		gen.writeObject(value);
	}

}
